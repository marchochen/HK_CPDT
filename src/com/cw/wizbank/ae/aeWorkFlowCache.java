package com.cw.wizbank.ae;

/**
 * <p>Title: </p>
 * <p>Description: based version: core version 3.5.52</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Cyberwisdom.net</p>
 * @author Emily
 * @version 1.0
 */
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


// add for xml parse
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.xerces.parsers.DOMParser;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.utils.CommonLog;

public class aeWorkFlowCache {
    private final static boolean DEBUG = false;

    /**
     * from aeWorkFlowEvent
     * added for the application list of approver
     * Emily, 20020906
     * public: used by aeWorkFlowEvent, backward compatible
     *
     * from aeAction
     * unique in all source
     * Emily, 20021014
     */
    // cachedAppStatusList - Hashtable [ key: workflow_node_[tpl_id]/event_trigger_node_[tpl_id];
    //                                   value: template xml node ]
    public static Hashtable cachedWorkFlowXML = null;
    // cachedAppStatusList - Hashtable [ key: participantRoleExtId; value: templateList ]
    public static Hashtable cachedAppStatusList = null;
    public static Hashtable cachedSysStatusList = null;
    public static Hashtable cachedSelfStatusList = null;

    public static Hashtable cached_queue_rule = null; 
    public static Hashtable cached_process_xml = null;
    public static Hashtable cached_process_status = null;
    public static Hashtable cached_process_action = null;
    public static Hashtable cached_event_trigger = null;
    
    // private -> public, from aeWorkFlowEvent
    // Tags will be looked at in workflow template
    public final static String WORKFLOW_TAG        =   "workflow";
    public final static String EVENTTRIGGER_TAG    =   "event_trigger";
    
    public final static String QUEUERULE_TAG = "queue_rule";

    // private -> public, from aeWorkFlowEvent
    // Hash key prefix
    public final static String WORKFLOW_NODE       =   "workflow_node_";
    public final static String EVENT_TRIGGER_NODE  =   "event_trigger_node_";
    public final static String QUEUE_RULE_NODE  =   "queue_rule_node_";

    // added to cache the workflow (2004-03-16 Emily)
    public static boolean isCached = false;

    // workFlowTitle - Hashtable [key: template id; value: template title]
    private static Hashtable workFlowTitle = null;
    
    private static Hashtable workFlowTplXML = null;

    // template type
    private final static String WORKFLOW_TEMPLATE =    "WORKFLOW";
    private final static String DOCWORKFLOW_TEMPLATE = "DOCWORKFLOW";
    private final static String QUEUE_RULE_CONDITIONS_TYPE = "or";

    public final static String DELIMITER = "~";

    private Connection con = null;

    public aeWorkFlowCache(Connection con)
            throws IOException, SQLException, cwException {
        if (con == null) {
            new SQLException("Cache failed: connection error!");
        }

        /**
         * variable initializion...
         */
        // added to cache the workflow (2004-03-16 Emily)
        if (this.isCached) {
            return;
        }
        this.con = con;
        if (this.cachedWorkFlowXML == null) {
            this.cachedWorkFlowXML = new Hashtable();
        }
        if (this.cachedAppStatusList == null) {
            this.cachedAppStatusList = new Hashtable();
        }
        if( this.cachedSysStatusList == null ) {
        	this.cachedSysStatusList = new Hashtable();
        }
        if( this.cachedSelfStatusList == null ) {
        	this.cachedSelfStatusList = new Hashtable();
        }
        
        if(cached_queue_rule == null) {
        	cached_queue_rule = new Hashtable();
        }
        if(cached_process_xml == null) {
        	cached_process_xml = new Hashtable();
        }
        if(cached_process_status == null) {
        	cached_process_status = new Hashtable();
        }
        if(cached_process_action == null) {
        	cached_process_action = new Hashtable();
        }        
        if(cached_event_trigger == null) {
        	cached_event_trigger = new Hashtable();
        }

        /**
         * caching...
         */
        this.cacheWorkFlow(this.cachedWorkFlowXML);
        this.cacheStatusList(this.cachedWorkFlowXML, this.cachedAppStatusList, ProcessStatus.APP);
		this.cacheStatusList(this.cachedWorkFlowXML, this.cachedSysStatusList, ProcessStatus.SYS);
		this.cacheStatusList(this.cachedWorkFlowXML, this.cachedSelfStatusList, ProcessStatus.SELF);
		
		this.parseWorkFlowTplXML();

        // added to cache the workflow (2004-03-16 Emily)
        this.isCached = true;
    }
    /**
     *
     * @param prof
     * @param cachedWorkFlow - Hashtable [ key: workflow_node_[tpl_id]/event_trigger_node_[tpl_id];
     *                                     value: template xml node ]
     */
    public void cacheWorkFlow(Hashtable workFlowXML)
            throws IOException, SQLException, cwException {
        // workflow has been cached
        if (workFlowXML != null && workFlowXML.size() > 0) {
            return;
        }

//        if (this.DEBUG) {
        	CommonLog.info("Caching workflow...");
//        }

        this.getAllWorkFlowList(workFlowXML);
        return;
    }


    /**
     * added for the application list of approver
     * Emily, 20020902
     * data structure:
     * VARIABLE              TYPE
     * appStatusList   Hashtable [ key: participantRoleExtId; value: templateList ]
     * participantRoleExtId  String [ rol_ext_id of current role ]
     * templateList          Hashtable [ key: templateId; value: template ]
     * statusList            Template
     * status                ProcessStatus
     */
    /**
     * cache the status in which approver participant
     * @param prof
     * @param workFlowXML
     * @param appStatusList - Hashtable [ key: participantRoleExtId; value: templateList ]
     * @throws IOException
     * @throws SQLException
     * @throws cwException
     */
    public void cacheStatusList(Hashtable workFlowXML,
                                Hashtable appStatusList,
                                String roleSrc)
            throws IOException, SQLException, cwException {

		Vector v_cache_status = new Vector();
        // status has been cached
        if( appStatusList != null && appStatusList.size() > 0 ) {
            return;
        }

        // cache workflow node
        if (workFlowXML == null) {
            this.cacheWorkFlow(workFlowXML);
        }

        if (this.DEBUG) {
        	CommonLog.debug("Caching status list...");
        }

        aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);

        Enumeration enumeration = workFlowXML.keys();
        String tplId;

        Hashtable templateStatusList = new Hashtable();
        /**
         * parse the workflow template, get all status (ProcessStatus)
         */
        while (enumeration.hasMoreElements()) {
            tplId = ((String)enumeration.nextElement());
            if (tplId.indexOf(this.WORKFLOW_NODE) == -1) {
                continue;
            }
            // assume: process_id = 1
            Vector value = workFlow.getStatusList((Node)workFlowXML.get(tplId), 1, roleSrc);
            templateStatusList.put(tplId.substring(this.WORKFLOW_NODE.length()), value);
        }

        // get all the role available
        AccessControlWZB acl = new AccessControlWZB();
        Vector roleList = acl.getAllRole(this.con);

        // go through the role list
        for (int i = 0; i < roleList.size(); i++) {
            // templateList - Hashtable [ key: templateId; value: statusList ]
            Hashtable templateList = new Hashtable();
            String role = (String)roleList.elementAt(i);
            enumeration = workFlowXML.keys();
            // go through the workflow template
            while (enumeration.hasMoreElements()) {
                tplId = ((String)enumeration.nextElement());
                if (tplId.indexOf(this.WORKFLOW_NODE) == -1) {
                    continue;
                }
                tplId = tplId.substring(this.WORKFLOW_NODE.length());
                Template template = new Template();
                /**
                 * retrieve the template title to display
                 */
                if (this.workFlowTitle != null && this.workFlowTitle.size() > 0) {
                    template.setTitle(((Template)this.workFlowTitle.get(tplId)).getTitle());
                    template.setType(((Template)this.workFlowTitle.get(tplId)).getType());
                } else { // ?
                    PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_get_workflow_template +
                                                                       SqlStatements.sql_get_workflow_template_cond_id);
                    stmt.setInt(1, Integer.parseInt(tplId));
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        template.setTitle(rs.getString("tpl_title"));
                        template.setType(rs.getString("ttp_title"));
                        rs.close();
                        stmt.close();
                    } else {
                    	if(rs!=null)rs.close();
                        stmt.close();
                        continue; // ?
                    }
              
                }

                // construct the xml
                template.setXmlStr("<workflow id=\""+cwUtils.escNull(tplId)+"\" title=\""+cwUtils.esc4XML(template.getTitle())+"\"/>");

                /**
                 * check status, keep the status only need to be approved
                 */
                /*
                Vector value = (Vector)templateStatusList.get(tplId);
                for (int j = 0; j < value.size(); j++) {
                    ProcessStatus status = (ProcessStatus)value.elementAt(j);

                    if (this.DEBUG) {
                        System.out.print(status.getStatusName()+"["+status.getRoleList().size()+"]:");
                        System.out.println(status.getRoleList());
                        System.out.println("role:"+role);
                    }

                    if (!status.getRoleList().contains(role) &&
                        ((String)status.getRoleList().get(role)) != null &&
                        ((String)status.getRoleList().get(role)).equalsIgnoreCase(ProcessStatus.APP)) {
                        template.addStatus(status);
                    }

                }
                if (template.getStatusList().length > 0) {
                    templateList.put(tplId, template);
                }
                */

                Vector value = (Vector)templateStatusList.get(tplId);
                for(int j=0; j<value.size(); j++){
                	template.addStatus((ProcessStatus)value.elementAt(j));
                }
                if( template.getStatusList().length > 0 ){
                	templateList.put(tplId, template);
                }

            }
            if (templateList.size() > 0) {
                appStatusList.put(role, templateList);
            }
        }
    }

    /**
     * get the workflow list with current role
     * @param prof
     * @return
     */
    public String getWorkFlowXML(loginProfile prof, Hashtable appStatusList)
            throws SQLException {
        if (this.DEBUG) {
        	CommonLog.debug("getWorkFlowXML begin..."+appStatusList);
        }

        StringBuffer result = new StringBuffer(1024);
        if (appStatusList == null) {
            result.append("<workflow_list/>");
            return result.toString();
        }

        // get the template list according to the current role
        Hashtable templateList = (Hashtable)appStatusList.get(prof.current_role);
        if (templateList == null) {
            result.append("<workflow_list/>");
            return result.toString();
        }

        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_get_workflow_template +
                                                           SqlStatements.sql_get_workflow_template_cond_owner);
        stmt.setLong(1, prof.root_ent_id);
        ResultSet rs = stmt.executeQuery();

        result.append("<workflow_list>");
        while (rs.next()) {
            Template template = (Template)templateList.get(rs.getString("tpl_id"));
            if (this.DEBUG) {
                if (template != null) {
                	CommonLog.debug(template.getXmlStr());
                }
            }
            if (template != null &&
                template.getType().equalsIgnoreCase(this.WORKFLOW_TEMPLATE)) {
                result.append(template.getXmlStr());
            }
        }
        result.append("</workflow_list>");
        stmt.close();
        if (this.DEBUG) {
        	CommonLog.debug("getWorkFlowXML end. workflow list: " + result.toString());
        }
        return result.toString();
//        return "hello!!!";
    }

    /**
     * get all the workflow template
     * @param workFlowXML
     * @throws SQLException
     * @throws IOException
     * @throws cwException
     */
    private void getAllWorkFlowList(Hashtable workFlowXML)
            throws SQLException, IOException, cwException {
        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_get_workflow_template);
        ResultSet rs = stmt.executeQuery();
        this.workFlowTitle = new Hashtable();
        workFlowTplXML = new Hashtable();
        while (rs.next()) {
//            if (rs.getString("ttp_title").equalsIgnoreCase(this.WORKFLOW_TEMPLATE)) {
        	String tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
            this.parseWorkFlow(tpl_xml,
                               rs.getInt("tpl_id"),
                               workFlowXML);
//            }
            workFlowTplXML.put(rs.getString("tpl_id"), tpl_xml);
            Template template = new Template();
            template.setTitle(rs.getString("tpl_title"));
            template.setType(rs.getString("ttp_title"));
            this.workFlowTitle.put(new String(rs.getString("tpl_id")), template);
        }
        rs.close();
        stmt.close();

        return;
    }

    /**
     * get the status list with specified role id & template id
     * @param appStatusList
     * @param roleExtId
     * @param templateId
     * @return
     */
    public Vector getStatusList(Hashtable appStatusList, String roleExtId, int templateId) {
        Vector result = new Vector();

        Hashtable templateList = (Hashtable)appStatusList.get(roleExtId);
        if (templateList != null) {
            String templateIdStr = String.valueOf(templateId);
            Template template = (Template)templateList.get(templateIdStr);
            if (template != null) {
                ProcessStatus[] statusList = (ProcessStatus[])template.getStatusList();
                for (int i = 0; i < statusList.length; i++) {
                    result.addElement(statusList[i].getStatusName());
                }
            }
        }

        return result;
    }

    /**
     * get the status with specified status id
     * @param appStatusList
     * @param roleExtId
     * @param templateId
     * @param statusId
     * @return
     */
    public String getStatusById(Hashtable appStatusList, String roleExtId,
                                int templateId, String statusId) {
        String result = null;

        if(statusId!=null && statusId.indexOf(DELIMITER)>0){
            String[] statusIds = cwUtils.splitToString(statusId, DELIMITER);
            String[] statusName = new String[statusIds.length];
            for(int i=0;i<statusIds.length;i++) {
                statusName[i] = "";
                try {
                    int id = Integer.parseInt(statusIds[i]);
                    Hashtable templateList = (Hashtable)appStatusList.get(roleExtId);
                    if (templateList != null) {
                        String templateIdStr = String.valueOf(templateId);
                        Template template = (Template)templateList.get(templateIdStr);
                        if (template != null) {
                            ProcessStatus[] statusList = (ProcessStatus[])template.getStatusList();
                            for (int j = 0; j < statusList.length; j++) {
                                if (statusList[j].getStatusId() == id) {
                                    statusName[i] = statusList[j].getStatusName();
                                    break;
                                }
                            }
                        }
                    }
                } catch(NumberFormatException e) {
                    ;
                }
            }
            result = cwUtils.array2list(statusName);
        } else {
            try {
                // test if process_status is a number
                int id = Integer.parseInt(statusId);
                Hashtable templateList = (Hashtable)appStatusList.get(roleExtId);
                if (templateList != null) {
                    String templateIdStr = String.valueOf(templateId);
                    Template template = (Template)templateList.get(templateIdStr);
                    if (template != null) {
                        ProcessStatus[] statusList = (ProcessStatus[])template.getStatusList();
                        for (int i = 0; i < statusList.length; i++) {
                            if (statusList[i].getStatusId() == id) {
                                result = statusList[i].getStatusName();
                                break;
                            }
                        }
                    }
                }
            } catch(NumberFormatException e) {
                //Do nothing if process_status is not a number
                ;
            }
        }

        return result;
    }

    /**
     * parsed the XML string, cached in memory
     * @param templateXML
     * @param templateId
     * @param workFlowXML
     * @throws IOException
     * @throws cwException
     */
    private void parseWorkFlow(String templateXML, int templateId,
                               Hashtable workFlowXML)
            throws IOException, cwException {
        //store the workflow template
        Node workFlowNode = null;
        Node eventTriggerNode = null;
        Node queueRuleNode = null;

        String tplIdStr = Integer.toString(templateId);

        if (templateXML != null) {
//            if (this.actionXML == null || workFlowNode == null || eventTriggerNode == null
//                || this.actionXML.length() != actionXML.length() || !this.actionXML.equalsIgnoreCase(actionXML)) {
//                this.actionXML = actionXML;
//System.out.println("Parse ACTION XML....................................");
            DOMParser xmlParser = new DOMParser();
            try {
                StringBuffer wellFormXML = new StringBuffer();
                wellFormXML.append("<well_form>").append(templateXML).append("</well_form>");
                xmlParser.parse(new InputSource(new StringReader(wellFormXML.toString())));
//System.out.println(wellFormXML.toString());
            } catch (SAXException e) {
                throw new cwException ("SAXException : " + e);
            }
            Document document = xmlParser.getDocument();
            Node root = document.getFirstChild();       //node wrapped by <well_from>
            NodeList nodelist = root.getChildNodes();   //get all childs of <well_form>
                                                        // eg. workflow, action_rule,....
            Node node;
            String nodeName;
            for (int i = 0; i < nodelist.getLength(); i++) {
                node = nodelist.item(i);
                nodeName = node.getNodeName();
//System.out.println("nodeName = " + nodeName);
                if (nodeName.equalsIgnoreCase(WORKFLOW_TAG)) {
//System.out.println("Get WORKFLOW XML...................................");
                    workFlowNode = node.cloneNode(true);
                    workFlowXML.put(WORKFLOW_NODE + tplIdStr, workFlowNode);
                }
                else if (nodeName.equalsIgnoreCase(EVENTTRIGGER_TAG)) {
//System.out.println("Get TRIGGER XML...................................");
                    eventTriggerNode = node.cloneNode(true);
                    workFlowXML.put(EVENT_TRIGGER_NODE + tplIdStr, eventTriggerNode);
                }
                //added by Shelley (07-04-10)
                else if (nodeName.equalsIgnoreCase(QUEUERULE_TAG)) {
                	queueRuleNode = node.cloneNode(true);
                	workFlowXML.put(QUEUE_RULE_NODE + tplIdStr, queueRuleNode);
                }
                // only get these 2 childs is enough
                if (workFlowNode != null && eventTriggerNode != null && queueRuleNode != null) {
                    break;
                }
            }
//        }
        }
        return;
    }

    /**
     * only for test/debug
     */
//    public void output(Hashtable workFlowXML,
//                       Hashtable appStatusList) {
//        try {
//            this.cacheStatusList(this.con, workFlowXML, appStatusList);
//            Enumeration roleEnum = appStatusList.keys();
//            while (roleEnum.hasMoreElements()) {
//                String role = (String)roleEnum.nextElement();
//                System.out.println("\n1:role="+role);
//                Hashtable templateList = (Hashtable)appStatusList.get(role);
//                Enumeration templateEnum = templateList.keys();
//                while (templateEnum.hasMoreElements()) {
//                    String templateId = (String)templateEnum.nextElement();
//                    if (!templateId.equals("23")) {
//                        continue;
//                    }
//                    System.out.println("2:template="+templateId);
//                    Template template = (Template)templateList.get(templateId);
//                    System.out.println("3:template title="+template.getTitle()+"("+template.getXmlStr()+")");
//                    ProcessStatus[] statusList = (ProcessStatus[])template.getStatusList();
//                    System.out.print("4:status["+statusList.length+"]=");
//                    for (int i = 0; i < statusList.length; i++) {
//                        System.out.print(statusList[i].getStatusName()+", ");
//                    }
//                    System.out.println();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    
    public void parseWorkFlowTplXML() throws cwException {
    	Node workFlowNode = null;
    	Node queueRuleNode = null;
    	Node eventTriggerNode = null;
    	Enumeration enumeration = workFlowTplXML.keys();
    	while (enumeration.hasMoreElements()) {
    		String tpl_id = (String)enumeration.nextElement();
    		String tpl_xml = (String)workFlowTplXML.get(tpl_id);
    		workFlowNode = (Node)cachedWorkFlowXML.get(WORKFLOW_NODE + tpl_id);
    		eventTriggerNode = (Node)cachedWorkFlowXML.get(EVENT_TRIGGER_NODE + tpl_id);
    		queueRuleNode = (Node)cachedWorkFlowXML.get(QUEUE_RULE_NODE + tpl_id);
	    	String key = "";
	    	if (queueRuleNode != null) {
	    		NodeList nextQueueLst = queueRuleNode.getChildNodes();
	    		for (int i=0; i<nextQueueLst.getLength(); i++) {
	    			if (((nextQueueLst.item(i)).getNodeName()).equals(aeWorkFlowEvent.NEXTQUEUE_TAG)) {
	    				Element nextQueueNode = (Element)nextQueueLst.item(i);
	    				String name = nextQueueNode.getAttribute(aeWorkFlowEvent.ATTR_NAME);
	    				NodeList consLst = nextQueueNode.getChildNodes();
	    				for (int j=0; j<consLst.getLength(); j++) {
	    					if(((consLst.item(j)).getNodeName()).equals(aeWorkFlowEvent.CONDITIONS_TAG)){
	    						Element consNode = (Element) consLst.item(j);
	    						if(consNode.getAttribute(aeWorkFlowEvent.ATTR_CONDITIONS_TYPE).equals(QUEUE_RULE_CONDITIONS_TYPE)) {
		    						NodeList conLst = consNode.getChildNodes();
		    						for (int k=0; k<conLst.getLength(); k++) {
		    							if(((conLst.item(k)).getNodeName()).equals(aeWorkFlowEvent.CONDITION_TAG)) {
		    								Element conNode = (Element)conLst.item(k);
		    								String process_id = conNode.getAttribute(aeWorkFlowEvent.ATTR_PROCESS_ID);
		    								String status_id = conNode.getAttribute(aeWorkFlowEvent.ATTR_STATUS_ID);
		    								key = tpl_id + DELIMITER + process_id + DELIMITER + status_id;
		    								cached_queue_rule.put(key, name);
		    							}
		    						}
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    	if(workFlowNode != null) {
	    		NodeList processLst = workFlowNode.getChildNodes();
	    		for (int i=0; i<processLst.getLength(); i++) {
	    			if (((processLst.item(i)).getNodeName()).equals(aeWorkFlowEvent.PROCESS_TAG)) {
	    				Element processNode = (Element)processLst.item(i);
	    				String str_process_id = processNode.getAttribute(aeWorkFlowEvent.ATTR_ID); //process_id
	    				NodeList statusLst = processNode.getChildNodes();
	    				for (int j=0; j<statusLst.getLength(); j++) {
	    					if(((statusLst.item(j)).getNodeName()).equals(aeWorkFlowEvent.STATUS_TAG)) {
	    						Element statusNode = (Element)statusLst.item(j);
	    						String str_status_id = statusNode.getAttribute(aeWorkFlowEvent.ATTR_ID); //status_id
	    						String status_name = statusNode.getAttribute(aeWorkFlowEvent.ATTR_NAME);
	    						key = tpl_id + DELIMITER + str_process_id + DELIMITER + str_status_id;
	    						cached_process_status.put(key, status_name);
	    						long status_id = Long.valueOf(str_status_id).longValue(); 
	    						NodeList actionLst = statusNode.getChildNodes();
	    						for(int k=0; k<actionLst.getLength(); k++) {
	    							if(((actionLst.item(k)).getNodeName()).equals(aeWorkFlowEvent.ACTION_TAG)) {
	    								Element actionNode = (Element)actionLst.item(k);
	    								String str_action_id = actionNode.getAttribute(aeWorkFlowEvent.ATTR_ID); //action_id
	    								//the hashtable process_action_value stores hash_process_action's value
	    								Hashtable process_action_value = new Hashtable();  
	    								String action_name = actionNode.getAttribute(aeWorkFlowEvent.ATTR_NAME);
	    								String action_verb = actionNode.getAttribute(aeWorkFlowEvent.ATTR_VERB);
	    								String action_next_status = actionNode.getAttribute(aeWorkFlowEvent.ATTR_NEXT_STATUS);
	    								process_action_value.put(aeWorkFlowEvent.ATTR_NAME, action_name);
	    								process_action_value.put(aeWorkFlowEvent.ATTR_VERB, action_verb);
	    								process_action_value.put(aeWorkFlowEvent.ATTR_NEXT_STATUS, action_next_status);
	    								key = tpl_id + DELIMITER + str_process_id + DELIMITER + str_status_id + DELIMITER + str_action_id;
	    								cached_process_action.put(key, process_action_value);
	    								long action_id = Long.valueOf(str_action_id).longValue(); 
	    								aeWorkFlow wf = new aeWorkFlow(dbUtils.xmlHeader);
	    								String process_xml = wf.initStatus(tpl_xml, status_id, action_id);
	    								cached_process_xml.put(key, process_xml);
	    							}
	    						}
	    					}
	    				}
	    			}
	    		}
	    	}
	    	if(eventTriggerNode != null) {
	    		NodeList eventLst = eventTriggerNode.getChildNodes();
	    		for (int i=0; i<eventLst.getLength(); i++) {
	    			if(((eventLst.item(i)).getNodeName()).equals(aeWorkFlowEvent.EVENT_TAG)) {
	    				Element eventNode = (Element)eventLst.item(i);
	    				String process_id = eventNode.getAttribute(aeWorkFlowEvent.ATTR_PROCESS_ID);
	    				String status_from = eventNode.getAttribute(aeWorkFlowEvent.ATTR_STATUS_FROM);
	    				String status_to = eventNode.getAttribute(aeWorkFlowEvent.ATTR_STATUS_TO);
	    				key = tpl_id + DELIMITER + process_id + DELIMITER + status_from + DELIMITER + status_to;
	    				cached_event_trigger.put(key, eventNode);
	    			}
	    		}
	    	}
    	}	    	
    }
    
    public static String getStatusFromQueueRule(long tpl_id, long process_id, long status_id) {
    	String result  = "";
    	if(cached_queue_rule != null) {
    		String key = String.valueOf(tpl_id) + DELIMITER + String.valueOf(process_id) + DELIMITER + String.valueOf(status_id);
    		result = (String)cached_queue_rule.get(key);
    	}
    	return result;
    }
    
    public static String getProcessStatusFromCached(long tpl_id, long process_id, long status_id) {
    	String result = "";
    	if(cached_process_status != null) {
    		String key = String.valueOf(tpl_id) + DELIMITER + String.valueOf(process_id) + DELIMITER + String.valueOf(status_id);
    		result = (String)cached_process_status.get(key);
    	}
    	return result;
    }
    
    public static Node getEventNodeFromCachedEventTrig(long tpl_id, long process_id, String status_fr, String status_to) {
    	Node result  = null;
    	if(cached_event_trigger != null) {
    		String key = String.valueOf(tpl_id) + DELIMITER + String.valueOf(process_id) + DELIMITER + status_fr + DELIMITER + status_to;
    		result = (Node)cached_event_trigger.get(key);
    	}
    	return result;
    }
    
    public static Hashtable getProActionFromCached(long tpl_id, String process_id, String status_id, String action_id) {
    	Hashtable result = null;
    	if(cached_process_action != null) {
    		String key = String.valueOf(tpl_id) + DELIMITER + process_id + DELIMITER + status_id + DELIMITER + action_id;
    		result = (Hashtable)cached_process_action.get(key);
    	}
    	return result;
    }
    
    public static String getProXmlFromCached(long tpl_id, long process_id, long status_id, long action_id) {
    	String result = "";
    	if(cached_process_xml != null) {
    		String key = String.valueOf(tpl_id) + DELIMITER + String.valueOf(process_id) + DELIMITER + String.valueOf(status_id) + DELIMITER + String.valueOf(action_id);
    		result = (String)cached_process_xml.get(key);
    	}
    	return result;
    }
    
}