package com.cw.wizbank.ae;

import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;

import com.cw.wizbank.util.cwXSL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

// class for workflow processing (2001.02.24 wai)
public class aeWorkFlow {
    //workflow constant
    private static final String REMOVE_ACTION_ID = "-1";
    private static final String ITEM_CANCELLED_ACTION_ID = "-2";
    
    // rules in XSL
    private final String statusInitXSL  = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output omit-xml-declaration=\"yes\" method=\"xml\"/><xsl:template match=\"/status_init\"><application_process><xsl:for-each select=\"workflow/process\"><xsl:element name=\"process\"><xsl:attribute name=\"id\"><xsl:value-of select=\"@id\"/></xsl:attribute><xsl:attribute name=\"name\"><xsl:value-of select=\"@name\"/></xsl:attribute><xsl:element name=\"status\"><xsl:attribute name=\"id\">1</xsl:attribute><xsl:attribute name=\"name\"><xsl:value-of select=\"status[@id = 1]/@name\"/></xsl:attribute></xsl:element></xsl:element></xsl:for-each></application_process></xsl:template></xsl:stylesheet>";
    private final String actionCheckXSL = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"text\"/><xsl:template match=\"/action_check\"><xsl:variable name=\"pid\" select=\"current/@process_id\"/><xsl:variable name=\"sid\" select=\"current/@status_id\"/><xsl:variable name=\"aid\" select=\"current/@action_id\"/><xsl:variable name=\"appl_proc\" select=\"application_process\"/><xsl:variable name=\"app_status\" select=\"application_process/process[@id = $pid]/status[@id = $sid]\"/><xsl:variable name=\"rule\" select=\"action_rule/action[@process_id = $pid and @status_id = $sid and @id = $aid]\"/><xsl:choose><xsl:when test=\"not($app_status)\">ER</xsl:when><xsl:when test=\"not($rule)\">ER</xsl:when><xsl:when test=\"$rule/condition and $rule/conditions\">ER</xsl:when><xsl:when test=\"$rule/condition\"><xsl:call-template name=\"cond_single\"><xsl:with-param name=\"cond\" select=\"$rule/condition\"/><xsl:with-param name=\"appl\" select=\"$appl_proc\"/></xsl:call-template></xsl:when><xsl:when test=\"$rule/conditions\"><xsl:call-template name=\"cond_group\"><xsl:with-param name=\"cond\" select=\"$rule/conditions\"/><xsl:with-param name=\"appl\" select=\"$appl_proc\"/></xsl:call-template></xsl:when><xsl:otherwise>OK</xsl:otherwise></xsl:choose></xsl:template><xsl:template name=\"cond_single\"><xsl:param name=\"cond\"/><xsl:param name=\"appl\"/><xsl:choose><xsl:when test=\"$appl/process[@id = $cond/@process_id]/status[@id = $cond/@status_id]\">OK</xsl:when><xsl:otherwise>NG</xsl:otherwise></xsl:choose></xsl:template><xsl:template name=\"cond_group\"><xsl:param name=\"cond\"/><xsl:param name=\"appl\"/><xsl:variable name=\"type\"><xsl:value-of select=\"$cond/@type\"/></xsl:variable><xsl:variable name=\"boo\"><xsl:for-each select=\"$cond/condition\"><xsl:call-template name=\"cond_single\"><xsl:with-param name=\"cond\" select=\".\"/><xsl:with-param name=\"appl\" select=\"$appl\"/></xsl:call-template></xsl:for-each><xsl:for-each select=\"$cond/conditions\"><xsl:call-template name=\"cond_group\"><xsl:with-param name=\"cond\" select=\".\"/><xsl:with-param name=\"appl\" select=\"$appl\"/></xsl:call-template></xsl:for-each></xsl:variable><xsl:choose><xsl:when test=\"contains($boo,'ER')\">ER</xsl:when><xsl:when test=\"$type = 'and'\"><xsl:choose><xsl:when test=\"contains($boo,'NG')\">NG</xsl:when><xsl:otherwise>OK</xsl:otherwise></xsl:choose></xsl:when><xsl:when test=\"$type = 'or'\"><xsl:choose><xsl:when test=\"contains($boo,'OK')\">OK</xsl:when><xsl:otherwise>NG</xsl:otherwise></xsl:choose></xsl:when><xsl:otherwise>ER</xsl:otherwise></xsl:choose></xsl:template></xsl:stylesheet>";
    private final String statusCheckXSL = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output omit-xml-declaration=\"yes\" method=\"xml\"/><xsl:template match=\"/status_check\"><xsl:variable name=\"pid\" select=\"current/@process_id\"/><xsl:variable name=\"sid\" select=\"current/@status_id\"/><xsl:variable name=\"aid\" select=\"current/@action_id\"/><xsl:variable name=\"nsid\" select=\"workflow/process[@id = $pid]/status[@id = $sid]/action[@id = $aid]/@next_status\"/><xsl:variable name=\"nextstatus\" select=\"workflow/process[@id = $pid]/status[@id = $nsid]\"/><application_process><xsl:for-each select=\"application_process/process\"><xsl:element name=\"process\"><xsl:attribute name=\"id\"><xsl:value-of select=\"@id\"/></xsl:attribute><xsl:attribute name=\"name\"><xsl:value-of select=\"@name\"/></xsl:attribute><xsl:choose><xsl:when test=\"@id = $pid\"><xsl:element name=\"status\"><xsl:attribute name=\"id\"><xsl:value-of select=\"$nextstatus/@id\"/></xsl:attribute><xsl:attribute name=\"name\"><xsl:value-of select=\"$nextstatus/@name\"/></xsl:attribute></xsl:element></xsl:when><xsl:otherwise><xsl:element name=\"status\"><xsl:attribute name=\"id\"><xsl:value-of select=\"status/@id\"/></xsl:attribute><xsl:attribute name=\"name\"><xsl:value-of select=\"status/@name\"/></xsl:attribute></xsl:element></xsl:otherwise></xsl:choose></xsl:element></xsl:for-each></application_process></xsl:template></xsl:stylesheet>";
    private final String queueCheckXSL  = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"text\"/><xsl:template match=\"/queue_check\"><xsl:variable name=\"appl_proc\" select=\"application_process\"/><xsl:for-each select=\"queue_rule/next_queue\"><xsl:choose><xsl:when test=\"condition and conditions\"></xsl:when><xsl:when test=\"condition\"><xsl:variable name=\"boo_single\"><xsl:call-template name=\"cond_single\"><xsl:with-param name=\"cond\" select=\"condition\"/><xsl:with-param name=\"appl\" select=\"$appl_proc\"/></xsl:call-template></xsl:variable><xsl:if test=\"$boo_single = 'OK'\"><xsl:value-of select=\"@name\"/></xsl:if></xsl:when><xsl:when test=\"conditions\"><xsl:variable name=\"boo_group\"><xsl:call-template name=\"cond_group\"><xsl:with-param name=\"cond\" select=\"conditions\"/><xsl:with-param name=\"appl\" select=\"$appl_proc\"/></xsl:call-template></xsl:variable><xsl:if test=\"$boo_group = 'OK'\"><xsl:value-of select=\"@name\"/></xsl:if></xsl:when><xsl:otherwise></xsl:otherwise></xsl:choose></xsl:for-each></xsl:template><xsl:template name=\"cond_single\"><xsl:param name=\"cond\"/><xsl:param name=\"appl\"/><xsl:choose><xsl:when test=\"$appl/process[@id = $cond/@process_id]/status[@id = $cond/@status_id]\">OK</xsl:when><xsl:otherwise>NG</xsl:otherwise></xsl:choose></xsl:template><xsl:template name=\"cond_group\"><xsl:param name=\"cond\"/><xsl:param name=\"appl\"/><xsl:variable name=\"type\"><xsl:value-of select=\"$cond/@type\"/></xsl:variable><xsl:variable name=\"boo\"><xsl:for-each select=\"$cond/condition\"><xsl:call-template name=\"cond_single\"><xsl:with-param name=\"cond\" select=\".\"/><xsl:with-param name=\"appl\" select=\"$appl\"/></xsl:call-template></xsl:for-each><xsl:for-each select=\"$cond/conditions\"><xsl:call-template name=\"cond_group\"><xsl:with-param name=\"cond\" select=\".\"/><xsl:with-param name=\"appl\" select=\"$appl\"/></xsl:call-template></xsl:for-each></xsl:variable><xsl:choose><xsl:when test=\"contains($boo,'ER')\">ER</xsl:when><xsl:when test=\"$type = 'and'\"><xsl:choose><xsl:when test=\"contains($boo,'NG')\">NG</xsl:when><xsl:otherwise>OK</xsl:otherwise></xsl:choose></xsl:when><xsl:when test=\"$type = 'or'\"><xsl:choose><xsl:when test=\"contains($boo,'OK')\">OK</xsl:when><xsl:otherwise>NG</xsl:otherwise></xsl:choose></xsl:when><xsl:otherwise>ER</xsl:otherwise></xsl:choose></xsl:template></xsl:stylesheet>";
    private final String queueReturnXSL = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"text\"/><xsl:template match=\"/queue_return\"><xsl:variable name=\"que_pid\" select=\"workflow/process[@type = 'queue']/@id\"/><xsl:value-of select=\"application_process/process[@id = $que_pid]/status/@name\"/></xsl:template></xsl:stylesheet>";
    private final String processStatusInitXSL = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"text\"/><xsl:template match=\"/application_process\"><xsl:value-of select=\"process[@id = '1']/status/@name\"/></xsl:template></xsl:stylesheet>";

    private final String getActionIdXSL = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"text\"/><xsl:template match=\"/application_process\"><xsl:variable name=\"frStatus\" select=\"@status_from\"/><xsl:variable name=\"toStatus\" select=\"@status_to\"/><xsl:for-each select=\"workflow/process\"><xsl:variable name=\"delimiter\">[|]</xsl:variable><xsl:variable name=\"next_status\" select=\"status[@name=$toStatus]\"/><xsl:variable name=\"cur_status\" select=\"status[@name=$frStatus]\"/><xsl:variable name=\"action\" select=\"status[@name=$frStatus]/action[@next_status = $next_status/@id]\"/><xsl:value-of select=\"@id\"/><xsl:value-of select=\"$delimiter\"/><xsl:value-of select=\"$cur_status/@id\"/><xsl:value-of select=\"$delimiter\"/><xsl:value-of select=\"$next_status/@id\"/><xsl:value-of select=\"$delimiter\"/><xsl:value-of select=\"$action/@id\"/><xsl:value-of select=\"$delimiter\"/><xsl:value-of select=\"$action/@verb\"/></xsl:for-each></xsl:template></xsl:stylesheet>";
    private final String getAllStatusXSL = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"text\"/><xsl:template match=\"/application_process\"><xsl:for-each select=\"workflow/process/status\"><xsl:if test=\"position() != 1\">[|]</xsl:if><xsl:value-of select=\"@name\"/></xsl:for-each></xsl:template></xsl:stylesheet>";    

    // enhanced rules for specifying the "to" state when inserting an application
    public final String statusInitXSL2 = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output omit-xml-declaration=\"yes\" method=\"xml\"/><xsl:template match=\"/status_init\"><xsl:variable name=\"status_id\" select=\"@status_id\"/><xsl:variable name=\"action_id\" select=\"@action_id\"/><application_process><xsl:for-each select=\"workflow/process\"><xsl:variable name=\"next_status\" select=\"status[@id=$status_id]/action[@id=$action_id]/@next_status\"/><xsl:element name=\"process\"><xsl:attribute name=\"id\"><xsl:value-of select=\"@id\"/></xsl:attribute><xsl:attribute name=\"name\"><xsl:value-of select=\"@name\"/></xsl:attribute><xsl:element name=\"status\"><xsl:attribute name=\"id\"><xsl:value-of select=\"$next_status\"/></xsl:attribute><xsl:attribute name=\"name\"><xsl:value-of select=\"status[@id = $next_status]/@name\"/></xsl:attribute></xsl:element></xsl:element></xsl:for-each></application_process></xsl:template></xsl:stylesheet>";
    public final String getProcessStatusBySysStatusXSL = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:output method=\"text\"/><xsl:template match=\"/application_process\"><xsl:variable name=\"app_status\" select=\"@app_status\"/><xsl:for-each select=\"queue_rule/next_queue[@name = $app_status]/descendant::*[name() = 'condition']\"><xsl:if test=\"position() != 1\">[|]</xsl:if><xsl:variable name=\"my_id\" select=\"@status_id\"/><xsl:value-of select=\"/application_process/workflow/process/status[@id = $my_id]/@name\"/></xsl:for-each></xsl:template></xsl:stylesheet>";

    /*
    private Document statusInitNode  = null;
    private Document actionCheckNode = null;
    private Document statusCheckNode = null;
    private Document queueCheckNode  = null;
    private Document queueReturnNode = null;
    private Document processStatusInitNode = null;
    */

    protected Hashtable xslHash = new Hashtable();

    protected String xmlHeader;

    public aeWorkFlow(String inXMLHeader) {
        this.xmlHeader = inXMLHeader;

        xslHash.put("statusInitXSL", statusInitXSL);
        xslHash.put("statusInitXSL2", statusInitXSL2);
        xslHash.put("actionCheckXSL", actionCheckXSL);
        xslHash.put("statusCheckXSL", statusCheckXSL);
        xslHash.put("queueCheckXSL", queueCheckXSL);
        xslHash.put("queueReturnXSL", queueReturnXSL);
        xslHash.put("processStatusInitXSL", processStatusInitXSL);
        xslHash.put("getActionIdXSL", getActionIdXSL);
        xslHash.put("getAllStatusXSL", getAllStatusXSL);
        xslHash.put("getProcessStatusBySysStatusXSL", getProcessStatusBySysStatusXSL);
        return;
    }

    // from the input current processes' status, get the status name of process id = 1
    // inCurrStatus: application's current status in XML format
    // return: application's initial process status
    public String initProcessStatus(String inCurrStatus) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String output;

        input.append(xmlHeader).append(inCurrStatus);
        output = cwXSL.processFromString(input.toString(), (String)xslHash.get("processStatusInitXSL"));
        
        return output;
    }

    // from the input status transformation rules, determine the initial status
    // inRule: rules of workflow in XML format
    // return: application's initial status in XML format
    // from the input status transformation rules, determine the initial status
    // inRule: rules of workflow in XML format
    // return: application's initial status in XML format
    public String initStatus(String inRule) throws cwException {
        return initStatus(inRule, 0, 0);
    }
    
    public String initStatus(String inRule, long status_id, long action_id) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String output;

        input.append(xmlHeader).append("<status_init status_id=\"").append(status_id).append("\" action_id=\"").append(action_id).append("\">").append(inRule).append("</status_init>");
        
        if (status_id > 0 && action_id > 0) {
            output = cwXSL.processFromString(input.toString(), (String)xslHash.get("statusInitXSL2"));
        }
        else {
            output = cwXSL.processFromString(input.toString(), (String)xslHash.get("statusInitXSL"));
        }

        return output;
    }

    // from the input action and status and action rules,
    // determine if the action is valid
    // inAction: action in XML format
    // inCurrStatus: application's current status in XML format
    // inRule: rules of action in XML format
    // return: true (ok to proceed this action) or false (reject this action)
    public boolean checkAction(String inAction, String inCurrStatus, String inRule) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String resultStr;
        boolean resultValue;

        input.append(xmlHeader).append("<action_check>").append(inAction).append(inCurrStatus).append(inRule).append("</action_check>");
        resultStr = cwXSL.processFromString(input.toString(), (String)xslHash.get("actionCheckXSL"));
        if (resultStr.equals("OK"))
            resultValue = true;
        else
            resultValue = false;

        return resultValue;
    }

    // from the input action and status and status transformation rules, determine the new status
    // inAction: action in XML format
    // inCurrStatus: application's current status in XML format
    // inRule: rules of workflow in XML format
    // return: application's new status in XML format
    public String checkStatus(String inAction, String inCurrStatus, String inRule) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String output;

        input.append(xmlHeader).append("<status_check>").append(inAction).append(inCurrStatus).append(inRule).append("</status_check>");
        output = cwXSL.processFromString(input.toString(), (String)xslHash.get("statusCheckXSL"));
        
        return output;
    }

// from the input action and status and action rules,
    // determine if the action is valid
    // inAction: action in XML format
    // inCurrStatus: application's current status in XML format
    // inRule: rules of action in XML format
    // return: process_id, fr_status_id, to_status_id, action id, action_verb, 
    public String[] getActionId(String frStatus, String toStatus, String inRule) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String resultStr;
        boolean resultValue;
        
        input.append(xmlHeader).append("<application_process status_from=\"").append(frStatus).append("\" status_to=\"").append(toStatus).append("\">").append(inRule).append("</application_process>");
        resultStr = cwXSL.processFromString(input.toString(), (String)xslHash.get("getActionIdXSL"));

        String[] result = cwUtils.splitToString(resultStr, "[|]");
        if (result.length != 5 ){
            throw new cwException("ERROR IN PROCESS WORKFLOW, WORKFLOW STATUS INCOMPLETED, from: " + frStatus + " to: " + toStatus);
        }else{
            for (int i=0; i<result.length; i++){
                if (result[i].length() ==0){
                    throw new cwException("INVALID WORKFLOW, STATUS:(" + frStatus + " TO " + toStatus+ ") NOT DEFINED");
                }
            }
        }
        return result;
    }    

    public String[] getAllStatus(String inRule) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String resultStr;
        boolean resultValue;
        
        input.append(xmlHeader).append("<application_process>").append(inRule).append("</application_process>");
        resultStr = cwXSL.processFromString(input.toString(), (String)xslHash.get("getAllStatusXSL"));

        String[] result = cwUtils.splitToString(resultStr, "[|]");
        if (result.length < 1){
            throw new cwException("aeWorkFlow.getAllStatus: INVALID WORKFLOW, NO STATUS DEFINED");
        }
        return result;
    }    
    
    // e.g. pass in app_status : WITHDRAWN, return workflow status "withdrawned"
    public String[] getProcessStatusBySysStatus(String inRule, String app_status) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String resultStr;
        boolean resultValue;
        input.append(xmlHeader).append("<application_process app_status=\"").append(app_status).append("\">").append(inRule).append("</application_process>");
        resultStr = cwXSL.processFromString(input.toString(), (String)xslHash.get("getProcessStatusBySysStatusXSL"));

        String[] result = cwUtils.splitToString(resultStr, "[|]");
        return result;        
    }
    
    // from the input status and queue management rules, determine the next queue to be
    // inCurrStatus: application's new status in XML format
    // inRule: rules of queue management in XML format
    // return: name of the new queue or blank if no change
    public String checkQueue(String inCurrStatus, String inRule) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String output;

        input.append(xmlHeader).append("<queue_check>").append(inCurrStatus).append(inRule).append("</queue_check>");
        output = cwXSL.processFromString(input.toString(), (String)xslHash.get("queueCheckXSL"));
        
        return output;
    }

    // (a new one over checkQueue() for the queue_rule portion replaced by a process with type=queue)
    // from the input status and workflow, determine the next queue to be
    // inCurrStatus: application's new status in XML format
    // inRule: workflow in XML format
    // return: name of the new queue or blank if error
    public String returnQueue(String inCurrStatus, String inRule) throws cwException {
        StringBuffer input  = new StringBuffer(1024);
        String output;

        input.append(xmlHeader).append("<queue_return>").append(inCurrStatus).append(inRule).append("</queue_return>");
        output = cwXSL.processFromString(input.toString(), (String)xslHash.get("queueReturnXSL"));
        
        return output;
    }

    // from the input rule and process, return the action that should be taken
    // inRule: rule of workflow in XML format
    // inProcess: type of the process
    // actionOK: action property indicator
    // on return: a Vector with the attributes of the action, null if action invalid
    // [0]: process id
    // [1]: status id
    // [2]: action id
    // [3]: from status
    // [4]: to status
    // [5]: verb
    public Vector getAction(String inRule, String inProcess, boolean actionOK) throws IOException {
        String sid = "1";   // status id must be 1
        String aid = null;
        if (actionOK)
            aid = "1";      // action id must be 1 for positive action
        else
            aid = "2";      // action id must be 2 for negative action

        Vector action = null;
        Node rtNode = preprocessXML(inRule).getDocumentElement();
        if (rtNode == null) return action;
        Node wfNode = getChildNodeByName(rtNode, "workflow");
        if (wfNode == null) return action;
        Node pNode  = getChildNodeByAtt(wfNode, "type", inProcess);
        if (pNode == null) return action;
        Node sNode  = getChildNodeByAtt(pNode, "id", sid);
        if (sNode == null) return action;
        Node aNode  = getChildNodeByAtt(sNode, "id", aid);
        if (aNode == null) return action;
        Node xsNode = getChildNodeByAtt(pNode, "id", getAttValueByName(aNode, "next_status"));
        if (xsNode == null) return action;
        String pid      = getAttValueByName(pNode, "id");
        String frStatus = getAttValueByName(sNode, "name");
        String toStatus = getAttValueByName(xsNode, "name");
        String frVerb   = getAttValueByName(aNode, "verb");

        action = new Vector();
        action.addElement(pid);
        action.addElement(sid);
        action.addElement(aid);
        action.addElement(frStatus);
        action.addElement(toStatus);
        action.addElement(frVerb);
        return action;
    }

    /**
    Get the remove action details of the input workflow template (tpl_id), process id and status id
    @param tpl_id template id of the workflow template
    @param processId process id of the workflow process
    @param statusId status id of the workflow status within the workflow process
    @return Vector with the following [index] and value:
            [0]: process id
            [1]: status id
            [2]: action id
            [3]: from status
            [4]: to status
            [5]: verb
    */
    public Vector getRemoveAction(long tpl_id, String processId, String statusId) throws IOException {
        return getAction(tpl_id, processId, statusId, REMOVE_ACTION_ID);
    }

    /**
    Get the item cancelled action details of the input workflow template (tpl_id), process id and status id
    @param tpl_id template id of the workflow template
    @param processId process id of the workflow process
    @param statusId status id of the workflow status within the workflow process
    @return Vector with the following [index] and value:
            [0]: process id
            [1]: status id
            [2]: action id
            [3]: from status
            [4]: to status
            [5]: verb
    */
    public Vector getItemCancelledAction(long tpl_id, String processId, String statusId) throws IOException {
        return getAction(tpl_id, processId, statusId, ITEM_CANCELLED_ACTION_ID);
    }

    /**
    Get the action details of the input workflow template (tpl_id), process id and status id
    @param tpl_id template id of the workflow template
    @param processId process id of the workflow process
    @param statusId status id of the workflow status within the workflow process
    @param actionId action id of the workflow action to be read
    @return Vector with the following [index] and value:
            [0]: process id
            [1]: status id
            [2]: action id
            [3]: from status
            [4]: to status
            [5]: verb
    */
    private Vector getAction(long tpl_id, String processId, String statusId, String actionId) throws IOException {

        Vector action = null;

        //get the workflow node
        Node wfNode = (Node) aeWorkFlowCache.cachedWorkFlowXML.get(aeWorkFlowCache.WORKFLOW_NODE + Long.toString(tpl_id));
        if (wfNode == null) return action;
        
        //get the process node
        Node pNode  = getChildNodeByNameAndAtt(wfNode, "process", "id", processId);
        if (pNode == null) return action;

        //get the status node
        Node sNode  = getChildNodeByNameAndAtt(pNode, "status", "id", statusId);
        if (sNode == null) return action;
        
        //get the action node
        Node aNode  = getChildNodeByNameAndAtt(sNode, "action", "id", actionId);
        if (aNode == null) return action;

        //get the next status node
        Node xsNode = getChildNodeByAtt(pNode, "id", getAttValueByName(aNode, "next_status"));
        if (xsNode == null) return action;

        String frStatus = getAttValueByName(sNode, "name");
        String toStatus = getAttValueByName(xsNode, "name");
        String frVerb   = getAttValueByName(aNode, "verb");

        action = new Vector();
        action.addElement(processId);
        action.addElement(statusId);
        action.addElement(actionId);
        action.addElement(frStatus);
        action.addElement(toStatus);
        action.addElement(frVerb);
        return action;
    }

    /**
    Parse the input process xml (app_process_xml) into Vector.
    @param inProcessXML input process xml (app_process_xml)
    @return Vector with the following [index] and value:
            [0] process id
            [1] process name
            [2] status id
            [3] status name
    */
    public Vector parseProcessXML(String inProcessXML) throws IOException {
        Vector vProcess = null;
        
        //parse the XML and get the root node
        Node rtNode = preprocessXML(inProcessXML).getDocumentElement();
        if (rtNode == null) return vProcess;
        
        //get the node "process"
        Node pNode = getChildNodeByName(rtNode, "process");
        if (pNode == null) return vProcess;

        //get the node "status"
        Node sNode  = getChildNodeByName(pNode, "status");
        if (sNode == null) return vProcess;

        vProcess = new Vector();
        vProcess.addElement(getAttValueByName(pNode, "id"));
        vProcess.addElement(getAttValueByName(pNode, "name"));
        vProcess.addElement(getAttValueByName(sNode, "id"));
        vProcess.addElement(getAttValueByName(sNode, "name"));
        return vProcess;
    }


    //get the current status of each process
    //inCurStatus: application's current status in XML format
    public Hashtable getCurProcessStatus(String inCurStatus) throws IOException {
        Hashtable h = new Hashtable();
        Node rtNode = preprocessXML(inCurStatus).getDocumentElement();
        if (rtNode == null) return h;
        NodeList process_list = rtNode.getChildNodes();
        String process_id;
        String status_name;
        if(process_list != null) {
            for(int i=0; i<process_list.getLength(); i++) {
                Node process = process_list.item(i);
                if(process == null) break;
                Node status = getChildNodeByName(process, "status");
                if(status == null) break;
                process_id = getAttValueByName(process, "id");
                status_name = getAttValueByName(status, "name");
                if(process_id != null && status_name != null) {
                    h.put(process_id, status_name);
                }
            }
        }
        return h;
    }

    // get the child node of the specified name, return the first ocurrence if multiple
    // inNode: parent node
    // inName: name of the target child node
    private Node getChildNodeByName(Node inNode, String inName) {
        NodeList childList = inNode.getChildNodes();
        Node childNode = null;
        int childCnt = childList.getLength();
        int childIdx = 0;
        for (; childIdx < childCnt; childIdx++) {
            childNode = childList.item(childIdx);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) continue;
            if (childNode.getNodeName().equals(inName)) break;
        }
        if (childIdx >= childCnt) childNode = null;
        return childNode;
    }

    // get the child node with the specified attribute/value, return the first ocurrence if multiple
    // inNode: parent node
    // inAttName: name of the attribute
    // inAttValue: value of the attribute that the child node should bear
    private Node getChildNodeByAtt(Node inNode, String inAttName, String inAttValue) {
        NodeList childList = inNode.getChildNodes();
        Node childNode = null;
        NamedNodeMap childAtt = null;
        int childCnt = childList.getLength();
        int childIdx = 0;
        for (; childIdx < childCnt; childIdx++) {
            childNode = childList.item(childIdx);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) continue;
            childAtt = childNode.getAttributes();
            if (childAtt == null) continue;
            Node attNode = childAtt.getNamedItem(inAttName);
            if (attNode != null && attNode.getNodeValue().equals(inAttValue)) break;
        }
        if (childIdx >= childCnt) childNode = null;
        return childNode;
    }

    // get the child node with the specified attribute/value, return the first ocurrence if multiple
    // inNode: parent node
    // inAttName: name of the attribute
    // inAttValue: value of the attribute that the child node should bear
    private Node getChildNodeByNameAndAtt(Node inNode, String inName, String inAttName, String inAttValue) {
        NodeList childList = inNode.getChildNodes();
        Node childNode = null;
        NamedNodeMap childAtt = null;
        int childCnt = childList.getLength();
        int childIdx = 0;
        for (; childIdx < childCnt; childIdx++) {
            childNode = childList.item(childIdx);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) continue;
            if (childNode.getNodeName().equals(inName)) {
                childAtt = childNode.getAttributes();
                if (childAtt == null) continue;
                Node attNode = childAtt.getNamedItem(inAttName);
                if (attNode != null && attNode.getNodeValue().equals(inAttValue)) break;
            }
        }
        if (childIdx >= childCnt) childNode = null;
        return childNode;
    }


	private String getChildNodeAttValueByNameAndAtt(Node inNode, String attName, String inName, String inAttName, String inAttValue) {
		Node node = this.getChildNodeByNameAndAtt(inNode, inName, inAttName, inAttValue);
		return this.getAttValueByName(node, attName);
	}


    // get the value of the attribute of the specified node
    // inNode: current node
    // inAttName: name of the target attribute
    private String getAttValueByName(Node inNode, String inAttName) {
        NamedNodeMap nodeAtt = inNode.getAttributes();
        String outStr = null;
        if (nodeAtt != null) {
            Node att = nodeAtt.getNamedItem(inAttName);
            if (att != null) {
                outStr = att.getNodeValue();
            }
        }
        return outStr;
    }

    // pre-process an XML
    private Document preprocessXML(String inStr) throws IOException {
        try {
            DOMParser xmlParser = new DOMParser();
            xmlParser.parse(new InputSource(new StringReader(inStr)));
            return xmlParser.getDocument();
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String getProcessStatus(String inRule, String pid, String sid) throws IOException {
        if(pid == null) pid = "1";
        if(sid == null) sid = "1";
        Node rtNode = preprocessXML(inRule).getDocumentElement();
        if (rtNode == null) return null;
        Node wfNode = getChildNodeByName(rtNode, "workflow");
        if (wfNode == null) return null;
        Node pNode  = getChildNodeByNameAndAtt(wfNode, "process", "id", pid);
        if (pNode == null) return null;
        Node sNode  = getChildNodeByNameAndAtt(pNode, "status", "id", sid);
        if (sNode == null) return null;
        return getAttValueByName(sNode, "name");
    }

    /**
     * added for the application list of approver
     * Emily, 20020902
     */
    private final boolean DEBUG = false;
    /**
     * parse the workflow xml
     * @param workflowNode
     * @param pId
     * @return
     * @throws IOException
     */
    public Vector getStatusList(Node workflowNode, int pId) throws IOException {
    	return getStatusList(workflowNode, pId, null);
    }
    public Vector getStatusList(Node workflowNode, int pId, String roleSrc) throws IOException {
        /**
         * retrieve the process by id [process\@id]
         */
        // "process" element
        Node pNode = this.getChildNodeByNameAndAtt(workflowNode, "process", "id", String.valueOf(pId));
        if (pNode == null) {
            throw new IOException("the element named \"process[@id='" + pId +"']\" not found.");
        }

        Vector statusList = new Vector();
        NodeList sNodeList = pNode.getChildNodes();
        for (int i = 0; i < sNodeList.getLength(); i++) {
            if (this.DEBUG) {
            	CommonLog.debug(sNodeList.getLength()+"=====i="+i);
            }
            Node sNode = sNodeList.item(i);
            if (sNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (sNode.getNodeName().equals("status")) {
                // "status" element
                Hashtable roleList = new Hashtable();
                ProcessStatus status = new ProcessStatus();
                status.setProcessid(pId);
                status.setStatusId(Integer.parseInt(this.getAttValueByName(sNode, "id")));
                status.setStatusName(this.getAttValueByName(sNode, "name"));
                

                StringBuffer xmlBuf = new StringBuffer();
                xmlBuf.append("<application_process>");
                xmlBuf.append("<process id=\"").append(pId).append("\">");
                xmlBuf.append("<status id=\"").append(status.getStatusId()).append("\" name=\"").append(status.getStatusName()).append("\" >");

                NodeList aNodeList = sNode.getChildNodes();
                for (int j = 0; j < aNodeList.getLength(); j++) {
                    if (this.DEBUG) {
                    	CommonLog.debug(aNodeList.getLength()+"#####j="+j);
                    }
                    // "action" element
                    Node aNode = aNodeList.item(j);
                    if (aNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    if (aNode.getNodeName().equals("action")) {
                        /**
                         * get the role list
                         * "access" element
                         * <access>
                         *     <role src="sys" id="TADM_1"/>
                         *     <role src="app" id="HRMAN_1"/>
                         * </access>
                         */
                        String aId = this.getAttValueByName(aNode, "id");
                        String aName = this.getAttValueByName(aNode, "name");
                        String aVerb = this.getAttValueByName(aNode, "verb");
                        String aNextStatusId = this.getAttValueByName(aNode, "next_status");
                        String aNextStatusName = this.getChildNodeAttValueByNameAndAtt(pNode, "name", "status", "id", aNextStatusId);
                        
                        Node accessNode = this.getChildNodeByName(aNode, "access");
                        // no "access" element existed [for Event Trigger]
                        if (accessNode == null) {
                            continue;
                        }
                        // all child node of "access" element
                        NodeList rNodeList = accessNode.getChildNodes();
                        for (int k = 0; k < rNodeList.getLength(); k++) {
                            Node rNode = rNodeList.item(k);
                            if (rNode.getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }
                            if (rNode.getNodeName().equals("role")) {
                                if (!status.getRoleList().contains(this.getAttValueByName(rNode, "id"))) {
                                	String src = this.getAttValueByName(rNode, "src");
                                	if( roleSrc == null ) {
										status.putRole(this.getAttValueByName(rNode, "id"), src);                                		
                                	} else if( src.equalsIgnoreCase(roleSrc) ) {
                                		status.putRole(this.getAttValueByName(rNode, "id"), src);
                                	} else {
                                		continue;
                                	}
                                	xmlBuf.append("<action ")
                                		  .append(" id=\"").append(aId).append("\" ")
                                		  .append(" name=\"").append(aName).append("\" ")
                                		  .append(" verb=\"").append(aVerb).append("\" ")
                                		  .append(">");
									xmlBuf.append("<next_status ")
										  .append(" id=\"").append(aNextStatusId).append("\" ")
										  .append(" name=\"").append(aNextStatusName).append("\" ")
										  .append("/>");
									xmlBuf.append("</action>");
                                }
                            }
                        }
                    }
                }
                xmlBuf.append("</status>")
                      .append("</process>")
                      .append("</application_process>");
                	
                status.setActionXML(xmlBuf.toString());
                
                if( status.getRoleList().size() > 0 ) {
					statusList.addElement(status);
                }

                if (this.DEBUG) {
                	CommonLog.debug("process_id="+status.getProcessId());
                	CommonLog.debug(",status_id="+status.getStatusId());
                	CommonLog.debug(",status_name="+status.getStatusName());
                	CommonLog.debug("role:");
                    Enumeration testEnum = status.getRoleList().keys();
                    while (testEnum.hasMoreElements()) {
                        String curKey = (String)testEnum.nextElement();
                        CommonLog.debug(curKey+": "+status.getRoleList().get(curKey));
                    }
                }
            }
        }

        return statusList;
    }
}