package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.xerces.parsers.DOMParser;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cwn.wizbank.utils.CommonLog;

/**
Access Control on WorkFlow Template
*/
public class AcWorkFlow {
    
    private Connection con;
    private aeApplication app;
    private long usr_ent_id;
    private String rol_ext_id;
    private Document workFlowDoc;
    
    public AcWorkFlow(Connection con, String workFlow) throws IOException, cwException {
        this.con = con;
        
        //if(workFlowDoc == null) {
            StringBuffer xmlBuf = new StringBuffer(1024);
            xmlBuf.append("<access_control>")
                .append(workFlow)
                .append("</access_control>");
            try {
                DOMParser xmlParser = new DOMParser();
                xmlParser.parse(new InputSource(new StringReader(xmlBuf.toString())));
                this.workFlowDoc = xmlParser.getDocument();
            }
            catch (SAXException e) {
                throw new cwException("SAXException :" + e.getMessage());
            }
        //}
        return;
    }
    
    /**
    Given an user, role pair to test if can execute the input action on the application
    */
    public boolean checkPrivilege(long usr_ent_id, String rol_ext_id, 
                                  long process_id, long status_id, 
                                  long action_id, aeApplication app,
                                  boolean triggerAction) 
                                  throws SQLException {
        this.app = app;
        this.usr_ent_id = usr_ent_id;
        this.rol_ext_id = rol_ext_id;
        boolean result=false;
        if(!triggerAction) {
            Vector v_src = new Vector();
            Vector v_role = new Vector();
            Vector v_approver_type = new Vector();
            getAccessSrcNRole(v_src, v_role, v_approver_type, process_id, status_id, action_id);
            if(v_src.size() == 0 && v_role.size() == 0 && v_approver_type.size() == 0) {
                result = true;
            }
            else {
                String src;
                String role;
                for(int i=0; i<v_src.size(); i++) {
                    src = (String) v_src.elementAt(i);
                    role = (String) v_role.elementAt(i);
                    if(role.equalsIgnoreCase(rol_ext_id)) {
                        result = procAccessRule4Role(src);
                    }
                    if(result == true) {
                        break;
                    }
                }
            }
            if(result == false) {
                String approver_type;
                for(int i=0;i<v_approver_type.size(); i++) {
                    approver_type = (String)v_approver_type.elementAt(i);
                    if(approver_type.equals("current_approvers")) {
                        result = DbAppnApprovalList.isCurrentApprover(con, app.app_id, usr_ent_id);
                    }
                    if(result == true) {
                        break;
                    }
                }
            }
        }
        else {
            result = true;
        }
        return result;
    }
    
    /**
    Given an user, role pair and the src, check if the pair has access on the application
    @param src role src from workflow template
    */
    private boolean procAccessRule4Role(String src) 
                                   throws SQLException {
        boolean result = false;
        if(src.equalsIgnoreCase("sys")) {
            result = true;
        }
        else if(src.equalsIgnoreCase("app")) {
            dbRegUser usr = new dbRegUser();
            usr.usr_ent_id = this.usr_ent_id;
            Vector v_usr_ent_id = 
                ViewRoleTargetGroup.getTargetGroupsLrn(this.con, this.usr_ent_id, this.rol_ext_id, false);
            result = v_usr_ent_id.contains(new Long(app.app_ent_id));
        }
        else if(src.equalsIgnoreCase("self")
            && this.usr_ent_id == this.app.app_ent_id) {
            result = true;
        }
        return result;
    }

    /**
    Put the src and id of tag <access> into input Vectors
    according to input process, status, action
    */
    private void getAccessSrcNRole(Vector v_src, Vector v_role, Vector v_approver_type,
                                  long process_id, long status_id, 
                                  long action_id) {
        Node root = this.workFlowDoc.getDocumentElement();
        if(root == null) return;
        Node workflow = getChildNodeByName(root, "workflow");
        if(workflow == null) return;
        Node process = getChildNodeByAtt(workflow, "id", process_id + "");
        if(process == null) return;
        Node status = getChildNodeByAtt(process, "id", status_id + "");
        if(status == null) return;
        Node action = getChildNodeByAtt(status, "id", action_id + "");
        if(action == null) return;
        Node access = getChildNodeByName(action, "access");
        if(access == null) return;
        
        NodeList role_list = access.getChildNodes();
        String s_src;
        String s_role;
        String s_approver_type;
        if(role_list != null) {
            Node role = null;
            NamedNodeMap roleAtt = null;
            CommonLog.debug("role_list.getLength() = " + role_list.getLength());
            for(int i=0; i<role_list.getLength(); i++) {
                role = role_list.item(i);
                if(role.getNodeName().equals("role")) {
                    s_src = getAttValueByName(role, "src");
                    if(s_src == null) {
                        s_src = "";
                    }
                    v_src.addElement(s_src);
                    s_role = getAttValueByName(role, "id");
                    if(s_role == null) {
                        s_role = "";
                    }
                    v_role.addElement(s_role);
                } else if(role.getNodeName().equals("approver")) {
                    s_approver_type = getAttValueByName(role, "type");
                    if(s_approver_type == null) {
                        s_approver_type = "";
                    }
                    v_approver_type.addElement(s_approver_type);
                }
            }
        }        
        return;
    }
    
    /**
    Copied from aeWorkFlow<BR>
    Get the child node of the specified name, return the first ocurrence if multiple
    @param inNode parent node
    @param inName name of the target child node
    */
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
    
    /**
    Copied from aeWorkFlow<BR>
    get the child node with the specified attribute/value, return the first ocurrence if multiple
    @param inNode parent node
    @param inAttName name of the attribute
    @param inAttValue value of the attribute that the child node should bear
    */
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

    /**
    get the value of the attribute of the specified node
    @param inNode current node
    @param inAttName name of the target attribute
    */
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
    
}