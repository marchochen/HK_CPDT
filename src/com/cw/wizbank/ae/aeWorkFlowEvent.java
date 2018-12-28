package com.cw.wizbank.ae;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.accesscontrol.AcQueueManager;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.cf.CFCertificate;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class aeWorkFlowEvent {

    //Tags will be looked at in workflow template
    // from aeWorkFlowCache
    public final static String WORKFLOW_TAG        =   aeWorkFlowCache.WORKFLOW_TAG;     // "workflow";
    public final static String EVENTTRIGGER_TAG    =   aeWorkFlowCache.EVENTTRIGGER_TAG; // "event_trigger";
    public final static String EVENT_TAG           =   "event";
    public final static String EVENTFUNCT_TAG      =   "event_func";
    public final static String ACTION_TAG          =   "action";
    public final static String FUNCTRETURN_TAG     =   "func_return";
    public final static String PROCESS_TAG         =   "process";
    public final static String STATUS_TAG          =   "status";
    public final static String FUNCPARAM_TAG       =   "func_param";
    
    //queue_rule
    public final static String NEXTQUEUE_TAG       =   "next_queue";
    public final static String CONDITIONS_TAG      =   "conditions";
    public final static String CONDITION_TAG       =   "condition";
    public final static String ATTR_CONDITIONS_TYPE  = "type";

    //Attributes will be looked at workflow template
    public final static String ATTR_PROCESS_ID     =   "process_id";
    public final static String ATTR_STATUS_ID      =   "status_id";
    public final static String ATTR_ID             =   "id";
    public final static String ATTR_STATUS_FROM    =   "status_from";
    public final static String ATTR_STATUS_TO      =   "status_to";
    public final static String ATTR_NAME           =   "name";
    public final static String ATTR_VERB           =   "verb";
    public final static String ATTR_NEXT_STATUS    =   "next_status";
    public final static String ATTR_VALUE          =   "value";
    public final static String ATTR_ROLLBACK       =   "rollback";
    public final static String ATTR_FUNCPARAM_NAME =   "name";
    public final static String ATTR_FUNCPARAM_VALUE    =   "value";


    //Hash key prefix
    // from aeWorkFlowCache
    public final static String WORKFLOW_NODE       =   aeWorkFlowCache.WORKFLOW_NODE;      // "workflow_node_"
    public final static String EVENT_TRIGGER_NODE  =   aeWorkFlowCache.EVENT_TRIGGER_NODE; // "event_trigger_node_"
    public final static String QUEUE_RULE_NODE     =   aeWorkFlowCache.QUEUE_RULE_NODE; 

    //Name of Event Function
    public final static String FUNC_CHECK_CAPACITY = "check_capacity";
    public final static String FUNC_REQUIRE_APPROVAL = "require_approval";
    public final static String FUNC_AUTO_ENROLL = "auto_enroll";
    public final static String FUNC_AUTO_APPROVE = "auto_approve";
    public final static String FUNC_INIT_ATTENDANCE = "init_attendance";
    public final static String FUNC_MARK_ATTENDANCE_INCOMPLETE = "mark_attendance_incomplete";
    public final static String FUNC_MARK_ATTENDANCE_WITHDRAWN = "mark_attendance_withdrawn";
    public final static String FUNC_SEND_ENROLLMENT_NEW_NOTIFY = "send_enrollment_new_notify";
    public final static String FUNC_SEND_NOTIFY = "SEND_NOTIFY";
    public final static String INS_JI_RECIPIENT = "INS_JI_RECIPIENT";
    public final static String RM_JI_RECIPIENT = "RM_JI_RECIPIENT";
    public final static String FUNC_TO_NEXT_APPROVERS = "to_next_approvers";
    public final static String FUNC_END_APPROVAL = "end_approval";
    
    /*
    public final static String FUNC_SEND_ENROLLMENT_APPROVED_NOTIFY = "SEND_ENROLLMENT_APPROVED_NOTIFY";
    public final static String FUNC_SEND_ENROLLMENT_WAITLISTED_NOTIFY = "SEND_ENROLLMENT_WAITLISTED_NOTIFY";
    public final static String FUNC_SEND_ENROLLMENT_NOT_APPROVED_NOTIFY = "SEND_ENROLLMENT_NOT_APPROVED_NOTIFY";
    public final static String FUNC_SEND_ENROLLMENT_ASSIGNED_NOTIFY = "SEND_ENROLLMENT_ASSIGNED_NOTIFY";
    public final static String FUNC_SEND_ENROLLMENT_CONFIRMED_NOTIFY = "SEND_ENROLLMENT_CONFIRMED_NOTIFY";
    public final static String FUNC_SEND_ENROLLMENT_NOT_CONFIRMED_NOTIFY = "SEND_ENROLLMENT_NOT_CONFIRMED_NOTIFY";
    public final static String FUNC_SEND_ENROLLMENT_WITHDRAWAL_APPROVED_NOTIFY = "SEND_ENROLLMENT_WITHDRAWAL_APPROVED_NOTIFY";
    public final static String FUNC_SEND_COURSE_CANCELLED_NOTIFY = "SEND_COURSE_CANCELLED_NOTIFY";
    public final static String FUNC_SEND_ATTENDANCE_NO_SHOW_NOTIFY = "SEND_ATTENDANCE_NO_SHOW_NOTIFY";
    */
    public final static String FUNC_ENROL = "ENROL";
    public final static String FUNC_UNENROL = "UNENROL";
    public final static String FUNC_RM_APPN = "RM_APPN";
    public final static String FUNC_FROM_TRIGGER_ACTION = "FROM_TRIGGER_ACTION";
    public final static String FUNC_CHECK_ACTION_ID = "CHECK_ACTION_ID";
    public final static String FUNC_RM_APPN_JI = "RM_APPN_JI";
    //add for capture user profile as app_usr_prof_xml when the is admitted
    public final static String FUNC_CAPTURE_USR_PROFILE = "CAPTURE_USR_PROFILE";

    public final static String FUNC_RETURN_TRUE = "true";
    public final static String FUNC_RETURN_FALSE = "false";
    public final static String FUNC_RETURN_OK = "ok";
    public final static String FUNC_RETURN_NO_SUPERVISOR = "no_supervisor";
    public final static String FUNC_RETURN_END_OF_APPROVAL = "end_of_approval";

    //store the workflow template
    public Node workFlowNode = null;
    public Node eventTriggerNode = null;
    public Node queueRuleNode = null;
    public String actionXML = null;

    public loginProfile prof = null;  //site default sys user profile
    public loginProfile cur_prof = null;   //prof of the action performer

    public loginProfile login_prof = null;   //prof of the login user
    public dbRegUser loginUser = null;

    //class variables
    public Connection con = null;
    private aeApplication app = null;
    public boolean fromTriggerAction = false;  //indicate if this event comes from a trigger action
    private long aah_id; //aah_id of the application action
    public long action_id; //action id of the action triggered this event
    public String ach_content; //appn action comment
    public static final String DEFAULT_DELIMITER = "~";

    // add for skippung payment event - dennis
    public final static String FUNC_CHECK_ZERO_FEE = "CHECK_ZERO_FEE";
    public final static String FUNC_INS_OPENITEM = "INS_OPEN_ITEM";

	//auto_enroll_ind
	public boolean auto_enroll_ind=true;
    
     //send_mail_ind
    public boolean send_mail_ind=true;
    
    private String tplIdStr;
    
    private static Hashtable cachedWorkFlowXML = new Hashtable();

    /*
    Constructor of aeWorkFlowEvent, parse the string to a node
    */
    public aeWorkFlowEvent(Connection con, String actionXML, long tplId)
        throws IOException, cwException{

        this.con = con;
        String tplIdStr = Long.toString(tplId);
        this.tplIdStr = Long.toString(tplId);

        /**
         * added to cache the workflow & status list - Emily, 20020906
         */
        try {
            aeWorkFlowCache workFlow = new aeWorkFlowCache(con);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        if (this.cachedWorkFlowXML.size() == 0) {
            this.cachedWorkFlowXML = aeWorkFlowCache.cachedWorkFlowXML;
        }


        if (this.cachedWorkFlowXML.containsKey(WORKFLOW_NODE + tplIdStr) &&
            this.cachedWorkFlowXML.containsKey(EVENT_TRIGGER_NODE + tplIdStr) &&
            cachedWorkFlowXML.containsKey(QUEUE_RULE_NODE + tplIdStr)) {
            workFlowNode = (Node)cachedWorkFlowXML.get(WORKFLOW_NODE + tplIdStr);
            eventTriggerNode = (Node)cachedWorkFlowXML.get(EVENT_TRIGGER_NODE + tplIdStr);
            queueRuleNode = (Node)cachedWorkFlowXML.get(QUEUE_RULE_NODE + tplIdStr);
            return;
        }


//        if(actionXML != null) {
//            //if(this.actionXML == null || workFlowNode == null || eventTriggerNode == null
//            //    || this.actionXML.length() != actionXML.length() || !this.actionXML.equalsIgnoreCase(actionXML)) {
//
//            //    this.actionXML = actionXML;
//    //System.out.println("Parse ACTION XML....................................");
//                DOMParser xmlParser = new DOMParser();
//                try{
//                    StringBuffer wellFormXML = new StringBuffer();
//                    wellFormXML.append("<well_form>").append(actionXML).append("</well_form>");
//                    xmlParser.parse(new InputSource(new StringReader(wellFormXML.toString())));
//                    //System.out.println(wellFormXML.toString());
//                }catch( SAXException e ) {
//                    throw new cwException ("SAXException : " + e);
//                }
//                Document document = xmlParser.getDocument();
//                Node root = document.getFirstChild();       //node wrapped by <well_from>
//                NodeList nodelist = root.getChildNodes();   //get all childs of <well_form>
//                                                            // eg. workflow, action_rule,....
//                Node node;
//                String nodeName;
//                for(int i=0; i<nodelist.getLength(); i++) {
//                    node = nodelist.item(i);
//                    nodeName = node.getNodeName();
//                    //System.out.println("nodeName = " + nodeName);
//                    if( nodeName.equalsIgnoreCase(WORKFLOW_TAG) ) {
//    //System.out.println("Get WORKFLOW XML...................................");
//                        workFlowNode = node;
//                        cachedWorkFlowXML.put(WORKFLOW_NODE + tplIdStr, workFlowNode);
//                    }
//                    else if( nodeName.equalsIgnoreCase(EVENTTRIGGER_TAG) ) {
//    //System.out.println("Get TRIGGER XML...................................");
//                        eventTriggerNode = node;
//                        cachedWorkFlowXML.put(EVENT_TRIGGER_NODE + tplIdStr, eventTriggerNode);
//                    }
//                    // only get these 2 childs is enough
//                    if( workFlowNode != null && eventTriggerNode != null )
//                        break;
//                }
//            }
//        }
        return;
    }
    
    
    private boolean reloadWorkFlow() throws IOException, SQLException, cwException{
    	CommonLog.info("Reload workflow start... tplIdStr:" + tplIdStr);
    	boolean result = false;
    	aeWorkFlowCache.isCached = false;
    	aeWorkFlowCache.cachedWorkFlowXML = null;
    	aeWorkFlowCache.cachedAppStatusList = null;
    	aeWorkFlowCache.cachedSysStatusList = null;
    	aeWorkFlowCache.cachedSelfStatusList = null;
    	aeWorkFlowCache.cached_queue_rule = null;
    	aeWorkFlowCache.cached_process_xml = null;
    	aeWorkFlowCache.cached_process_status = null;
    	aeWorkFlowCache.cached_process_action = null;
    	aeWorkFlowCache.cached_event_trigger = null;
    	aeWorkFlowCache workFlow = new aeWorkFlowCache(con);
    	
    	cachedWorkFlowXML = aeWorkFlowCache.cachedWorkFlowXML;
    	if (cachedWorkFlowXML.containsKey(WORKFLOW_NODE + tplIdStr) &&
              cachedWorkFlowXML.containsKey(EVENT_TRIGGER_NODE + tplIdStr) &&
              cachedWorkFlowXML.containsKey(QUEUE_RULE_NODE + tplIdStr)) {
            workFlowNode = (Node)cachedWorkFlowXML.get(WORKFLOW_NODE + tplIdStr);
            eventTriggerNode = (Node)cachedWorkFlowXML.get(EVENT_TRIGGER_NODE + tplIdStr);
            queueRuleNode = (Node)cachedWorkFlowXML.get(QUEUE_RULE_NODE + tplIdStr);
            if(eventTriggerNode!=null){
            	NodeList nodelist = eventTriggerNode.getChildNodes();
            	CommonLog.info("Reload workflow... eventTriggerNode size: " + nodelist.getLength());
            	result = true;
            }else{
            	CommonLog.info("Reload workflow... eventTriggerNode is null ");
            }
        }
    	
    	CommonLog.info("Reload workflow end...");
    	return result;
    }

    /**
    Process the event if the status change match the event trigger
    @param con Connection to database
    @param app application to be handled
    @param aah_id application action id
    @param cur_prof loginProfile of the action taker
    @param login_prof loginProfile of the login user
    @param process_id process id of the application
    @param fr from status of the application
    @param to to status of the application
    @param action_id action id of the action taken
    @param ach_content comment made on the action
    @param fromTriggerAction indicate if this trigger is from a triggered action
    */
    public void eventTrigger(Connection con, aeApplication app, long aah_id,
                             loginProfile cur_prof, loginProfile login_prof,
                             long process_id, String fr, String to,
                             long action_id, String ach_content,
                             boolean fromTriggerAction)
        throws SQLException, IOException, cwSysMessage, cwException ,qdbException{

if (eventTriggerNode == null ){
	CommonLog.info("eventTrigger, eventTriggerNode == null ");
}else{
	CommonLog.debug("eventTrigger");
}
            //no event trigger, do nothing
            //no application specified, do nothing
            //no connection given, do nothing
            //no loginProfile, do nothing
            if( eventTriggerNode == null || app == null
                || con == null || cur_prof == null) {
                //System.out.println("something is null");
                return;
            }
            //scan the childs, find a matched event
            else {
                this.con = con;
                this.app = app;

                if(this.prof == null) {
                    //System.out.println("input profile " + cur_prof.usr_id);
                    this.prof = dbRegUser.getSiteDefaultSysProfile(con, cur_prof);
                    //System.out.println("after get site sys profile " + this.prof.usr_id);
                }

                if(this.cur_prof == null || cur_prof.usr_ent_id != this.prof.usr_ent_id) {
                    this.cur_prof = cur_prof;
                }

                if( this.login_prof == null ){
                    this.login_prof = login_prof;
                    this.loginUser = new dbRegUser();
                    this.loginUser.usr_ent_id = login_prof.usr_ent_id;
                    this.prof.my_top_tc_id = login_prof.my_top_tc_id;
                    
                    try{
                        this.loginUser.get(con);
                    }catch(qdbException e){
                        throw new cwException(e.getMessage());
                    }

                }

                this.fromTriggerAction = fromTriggerAction;
                this.action_id = action_id;
                this.ach_content = ach_content;
                this.aah_id = aah_id;
                Hashtable attrTable = new Hashtable();
                attrTable.put(ATTR_PROCESS_ID, Long.toString(process_id));
                attrTable.put(ATTR_STATUS_FROM, fr);
                attrTable.put(ATTR_STATUS_TO, to);
                NodeList nodelist = eventTriggerNode.getChildNodes();
                Node node;
                CommonLog.info("before match process_id, fr, to, action id: " + process_id + "," + fr + "," + to + "," + action_id);
                CommonLog.info("the size of eventTriggerNode ChildNodes: " + nodelist.getLength());
                if(nodelist.getLength()!=35){
                	if(this.reloadWorkFlow()) nodelist = eventTriggerNode.getChildNodes();
                	else return;
                }

                for(int i=0; i<nodelist.getLength(); i++) {
                    node = nodelist.item(i);
                    if( (node.getNodeName()).equalsIgnoreCase(EVENT_TAG) ) {
                        if( checkAttribute((Element)node, attrTable) ) {
                        	CommonLog.info("Attributes match process_id, fr, to, action id: " + process_id + "," + fr + "," + to + "," + action_id);
                        	CommonLog.info("match node size: " + node.getChildNodes().getLength());
                            processEvent(node);
                        }
                    }
                }


            }

            return;
        }


    // check the attributes match the required status
    public boolean checkAttribute(Element element, Hashtable attrTable)
    {

        String attrName;
        String attrValue;
        Enumeration enumeration = attrTable.keys();
        boolean result = true;
        //System.out.println("Enter check Attribute");
        while( enumeration.hasMoreElements() ) {

            attrName = (String)enumeration.nextElement();
            attrValue = (String)attrTable.get(attrName);
            //System.out.println(attrName);
            //System.out.println(attrValue);
            //once an attribute does not match, return false
            if( !(element.getAttribute(attrName)).equalsIgnoreCase(attrValue) ) {
                result = false;
                break;
            }
        }

        return result;
    }



    // process the event
    // if the event is perform a action, call queue manager to ins action
    // if the event is perform a function, call the function
    public void processEvent(Node eventNode)
        throws SQLException, IOException, cwSysMessage, cwException ,qdbException{

            NodeList nodelist = eventNode.getChildNodes();
            Node node;
            String nodeName;
            for(int i=0; i<nodelist.getLength(); i++) {
                node = nodelist.item(i);
                nodeName = node.getNodeName();
                if( nodeName.equalsIgnoreCase(EVENTFUNCT_TAG) ) {
//                	System.out.println("in EVENTFUNCT_TAG:"+nodeName);

                    processFunct((Element)node);


                } else if ( nodeName.equalsIgnoreCase(ACTION_TAG) ) {
//                	System.out.println("in action_tag:"+nodeName);
                    processAction((Element)node);
                }

            }

            return;
        }

    //get function param of the event_func
    //eventFunctElement Node of the event_func
    public Hashtable getFunctParam(Element eventFunctElement) throws IOException {

        Hashtable param = new Hashtable();
        NodeList functParamNodeList = eventFunctElement.getChildNodes();
        for(int i=0; i<functParamNodeList.getLength(); i++) {
            Node functParamNode = functParamNodeList.item(i);
            if((functParamNode.getNodeName()).equalsIgnoreCase(FUNCPARAM_TAG)) {
                String param_name = ((Element)functParamNode).getAttribute(ATTR_FUNCPARAM_NAME);
                String param_value = ((Element)functParamNode).getAttribute(ATTR_FUNCPARAM_VALUE);
                param.put(param_name, param_value);
            }
        }
        return param;
    }


    //process the function and following action if necessary
    public void processFunct(Element eventFunctElement)
        throws SQLException, cwException, IOException, cwSysMessage ,qdbException{

            String functName = eventFunctElement.getAttribute(ATTR_NAME);
            String returnValue = null;
            Hashtable param = getFunctParam(eventFunctElement);
            CommonLog.debug("functName = " + functName);
            if( functName.equalsIgnoreCase(FUNC_CHECK_CAPACITY ) ) {
                //System.out.println("Trigger check_capacity");
                String process_status = (String)param.get("process_status");
                String delimiter = (String)param.get("delimiter");
                if(delimiter == null) delimiter = DEFAULT_DELIMITER;
                String[] process_status_lst = (process_status == null) ?
                                                null : aeReqParam.split(process_status, delimiter);
                try {
                    app.getWithItem(con);
                }
                catch(qdbException e) {
                    throw new cwException(e.getMessage());
                }
                AcQueueManager acqm = new AcQueueManager(con);
                if(acqm.hasAdminPrivilege(login_prof.usr_ent_id, login_prof.current_role) || app.itm_not_allow_waitlist_ind || login_prof.isCancelEnrol) { //if not allow waitlist, TA will not check item capacity 
                	returnValue = FUNC_RETURN_TRUE;
                } else {
                	returnValue = !(app.isItemCapacityExceed(con, process_status_lst, true)) ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
                }
            // << BEGIN add for skippung payment event - dennis
            } else if( functName.equalsIgnoreCase(FUNC_CHECK_ZERO_FEE) ) {
            	CommonLog.debug("Trigger CHECK_ZERO_FEE");
                aeItem itm = new aeItem();
                itm.itm_id = this.app.app_itm_id;
                itm.getItemFee(con);
                returnValue = (itm.itm_fee == 0) ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
            } else if( functName.equalsIgnoreCase(FUNC_INS_OPENITEM) ) {
            	CommonLog.debug("Trigger INS_OPENITEM");
                aeItem itm = new aeItem();
                itm.itm_id = this.app.app_itm_id;
                itm.get(con);
                try {
                    if(itm.itm_fee > 0) {
                        aeAdapter.insOpenItem(con
                                            ,this.app.app_ent_id
                                            ,this.app.app_id
                                            ,this.app.app_create_usr_id
                                            ,aeQueueManager.OPENITEM_INVOICE
                                            ,itm.itm_fee
                                            ,itm.itm_fee_ccy
                                                ,this.app.app_create_timestamp
                                            ,itm.itm_code
                                            ,itm.itm_title
                                            ,aeQueueManager.APPNOPENITEM_APPLYEASY
                                            ,new Long(this.app.app_id).toString()
                                            ,aeQueueManager.APPNOPENITEM_APPLYEASY);
                    }
                } catch (qdbException e) {
                    throw new cwException(e.getMessage());
                }
            // >> END
            } else if( functName.equalsIgnoreCase(FUNC_END_APPROVAL) ) {
                String action = (String)param.get("action");
                aeQueueManager qm = new aeQueueManager();
                qm.send_mail_ind = send_mail_ind;
                qm.endApproval(this.con, this.app.app_id, this.aah_id, action, this.login_prof);
            } else if( functName.equalsIgnoreCase(FUNC_TO_NEXT_APPROVERS) ) {
                aeQueueManager qm = new aeQueueManager();
                returnValue = qm.toNextApprovers(this.con, this.app.app_id, this.aah_id, this.app.app_itm_id, this.app.app_ent_id, this.login_prof);
            } else if( functName.equalsIgnoreCase(FUNC_AUTO_ENROLL ) ) {
                AcQueueManager acqm = new AcQueueManager(con);
                CommonLog.debug("aeworkflowevent.auto_enroll_ind= "+auto_enroll_ind);
                acSite site = new acSite();
				site.ste_ent_id = this.login_prof.root_ent_id;
                returnValue = (auto_enroll_ind && (acqm.hasAdminPrivilege(this.login_prof.usr_ent_id, this.login_prof.current_role) || this.login_prof.usr_ent_id == site.getSiteSysEntId(con))) ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
                CommonLog.debug("--------First check auto enroll: " + returnValue);
            } else if( functName.equalsIgnoreCase(FUNC_REQUIRE_APPROVAL ) ) {
                aeItem itm = new aeItem();
                itm.itm_id = this.app.app_itm_id;
                itm.get(con);
                returnValue = (itm.itm_app_approval_type != null) ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
                CommonLog.debug("--------Third check require approval: " + returnValue);
            } else if( functName.equalsIgnoreCase(FUNC_AUTO_APPROVE ) ) {
                AcQueueManager acqm = new AcQueueManager(con);
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = app.app_ent_id;
                returnValue = (usr.isManager(con)
                              || acqm.hasAdminPrivilege(this.login_prof.usr_ent_id, this.login_prof.current_role))
                              ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
            } else if( functName.equalsIgnoreCase(INS_JI_RECIPIENT ) ) {
            	if (aeItem.getRunInd(con, app.app_itm_id)){
//            		prof.my_top_tc_id = ViewTrainingCenter.getTopTc(con, prof.usr_ent_id, false);
	                aeItemMessage.insNotifyForJI(con, prof, app.app_ent_id, app.app_itm_id, app.app_id);
            	}
            } else if( functName.equalsIgnoreCase(RM_JI_RECIPIENT ) ) {
                aeItemMessage.removeNotifyForJI(con, app.app_itm_id, app.app_ent_id, app.app_id,"JI,REMINDER");
            }else if( functName.equalsIgnoreCase(FUNC_INIT_ATTENDANCE ) ) {
                //System.out.println("Trigger init_attendance");
                int ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, "PROGRESS");
                aeItem itm = new aeItem();
                itm.itm_id = app.app_itm_id;
                itm.getRunInd(con);
                long cos_id = itm.getResId(con);
                aeAttendance.saveStatus(con, app.app_id, app.app_itm_id, cos_id, app.app_ent_id,
                                        false, ats_id, prof.usr_id);
                //System.out.println("attendance saved");
            } else if( functName.equalsIgnoreCase(FUNC_MARK_ATTENDANCE_INCOMPLETE ) ) {
                //System.out.println("Trigger make_attendance_incomplete");
                aeAttendance att = new aeAttendance();
                att.att_app_id = app.app_id;
                att.att_itm_id = app.app_itm_id;
                String cur_ats_type = att.getAttStatus(con);
                if(cur_ats_type == null
                    || !cur_ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_ATTEND)) {
                    int ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, "INCOMPLETE");
                    aeItem itm = new aeItem();
                    itm.itm_id = app.app_itm_id;
                    itm.getRunInd(con);
                    long cos_id = itm.getResId(con);
                    aeAttendance.saveStatus(con, app.app_id, app.app_itm_id, cos_id, app.app_ent_id,
                                            false, ats_id, prof.usr_id);
                    //System.out.println("attendance updated to incomplete");
                }
            } else if( functName.equalsIgnoreCase(FUNC_MARK_ATTENDANCE_WITHDRAWN ) ) {
                //System.out.println("Trigger make_attendance_incomplete");
                aeAttendance att = new aeAttendance();
                att.att_app_id = app.app_id;
                att.att_itm_id = app.app_itm_id;
                String cur_ats_type = att.getAttStatus(con);
                if(cur_ats_type == null
                    || !cur_ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_ATTEND)) {
                    int ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, "WITHDRAWN");
                    aeItem itm = new aeItem();
                    itm.itm_id = app.app_itm_id;
                    //itm.getRunInd(con);
                    long cos_id = itm.getResId(con);
                    aeAttendance.saveStatus(con, app.app_id, app.app_itm_id, cos_id, app.app_ent_id,
                                            false, ats_id, prof.usr_id);
                }
            } else if( functName.equalsIgnoreCase(FUNC_SEND_NOTIFY) && send_mail_ind  ) {
            	aeItem itm = new aeItem();
            	itm.itm_id = app.app_itm_id;
            	itm.getSendMailInd(con);
	            if (itm.itm_send_enroll_email_ind > 0 
	            		&& MessageTemplate.isActive(con, login_prof.my_top_tc_id, (String)param.get("template_type"))) {
	            	CommonLog.debug("SEND_NOTIFY aeWorkFlowEvent.Line560............."+param.get("template_type")+"...........");
	                String DELIMITER = ";";
	                long[] rec_ent_id = null;
	                long[] cc_ent_id = null;
	                long[] bcc_ent_id = null;
	                long[] reply_ent_id = null;
	                long[] cc_sup_ent_ids = null;
	                String sender_usr_id = null;
	                String from = (String)param.get("from");
	                String to = (String)param.get("to");
	                String appr_role = (String)param.get("appr_role");
	                String cc = (String)param.get("cc");
	                String cc_role = (String)param.get("cc_role");
	                StringTokenizer stk_cc_role = (cc_role == null) ? null
	                                                                : new StringTokenizer(cc_role, DELIMITER);
	                String cc_list = (String) param.get("cc_list");
	                String cc_role_list = (String) param.get("cc_role_list");
	                
	                String to_role = (String)param.get("to_role");
	                StringTokenizer stk_to_role = (to_role == null) ? null
	                                                                : new StringTokenizer(to_role, DELIMITER);
	                String from_role = (String)param.get("from_role");
	                String from_ent_id = (String)param.get("from_ent_id");

	                dbRegUser sender = null;
	                if( from != null ) {
	                    sender = new dbRegUser();
	                    try {
	                        if( from.equalsIgnoreCase("prof") ) {
	                                sender.usr_ent_id = this.login_prof.usr_ent_id;
	                                sender.usr_id = this.login_prof.usr_id;
	                                sender_usr_id = this.login_prof.usr_id;
	                                sender.get(con);
	                        }
	                        else if( from.equalsIgnoreCase("applicant") ) {
	                                sender.usr_ent_id = this.app.app_ent_id;
	                                sender.get(con);
	                                sender_usr_id = sender.usr_id;
	                            }
	                        else if( from.equalsIgnoreCase("sysadmin") ) {
	                        	acSite site = new acSite();
	        					site.ste_ent_id = prof.root_ent_id;
	        					sender.usr_ent_id = site.getSiteSysEntId(con);
	        					itm.itm_notify_email = itm.getNotifyEmail(con);
	        					sender.get(con);
	        					if(!"".equals(itm.itm_notify_email)) {
	        						sender.usr_display_bil = "Course support";
	        						sender.getUsrID(con);
	        					}
	        					sender_usr_id = sender.usr_id;
	                        }
	                        else if( from.equalsIgnoreCase("item_access") ) {
	                            Vector v_iac = aeItemAccess.getItemAccessByItem(con, this.app.app_itm_id);
	                                if(v_iac == null || v_iac.size() == 0) {
	                                //if no item access record found
	                                //check it if it is a run and get the parent's item access
	                                    v_iac = aeItemAccess.getParentItemAccessByItem(con, this.app.app_itm_id);
	                                }
	                            Vector v_ent_id = new Vector();
	                            AccessControlWZB acl = new AccessControlWZB();
	                            Hashtable h_role_map = acl.getAllRoleUid(con, this.prof.root_ent_id, "rol_ste_uid");
	                            String from_role_2 = (String) h_role_map.get(from_role);
	                            if(from_role_2 == null) {
	                                from_role_2 = "";
	                            }
	                            for(int i=0; i < v_iac.size(); i++) {
	                                aeItemAccess iac = (aeItemAccess) v_iac.elementAt(i);
	                                if((iac.iac_access_type).equals(aeItemAccess.ACCESS_TYPE_ROLE)
	                                    && (from_role.equalsIgnoreCase(iac.iac_access_id)
	                                    || from_role_2.equalsIgnoreCase(iac.iac_access_id))) {
	                                    v_ent_id.addElement(new Long(iac.iac_ent_id));
	                                }
	                            }
	                            sender.usr_ent_id = ((Long)v_ent_id.elementAt(0)).longValue();
	                            sender.get(con);
	                            sender_usr_id = sender.usr_id;
	                        }
	                        else if( from.equalsIgnoreCase("ent_id") ) {
	                            sender.usr_ent_id = (new Long(from_ent_id)).longValue();
	                            sender.get(con);
	                            sender_usr_id = sender.usr_id;
	                        }
	                    } catch (qdbException e) {
	                            throw new cwException(e.getMessage());
	                    }
	                }
	                if(sender.usr_status != null
		                    /*&& !sender.usr_status.equalsIgnoreCase(dbRegUser.USR_STATUS_SYS)*/) {
		                //if sender is not SYS user, try to send the mail
		                if( to != null ) {
		                    rec_ent_id = get_to_ent_ids(con, to, stk_to_role, appr_role);
		                }
		                //Either use cc or cc_list
		                if( cc != null ) {
		                    Vector v = get_cc_entVec(con, cc, stk_cc_role);
		                    cc_ent_id = aeUtils.vec2longArray(v);
		                }else if (cc_list != null && cc_list.length() > 0) {
		                    String[] cc_array = cwUtils.splitToString(cc_list, DEFAULT_DELIMITER);
		                    String[] cc_role_array = cwUtils.splitToString(cc_role_list, DEFAULT_DELIMITER);
		                    if (cc_array.length != cc_role_array.length) {
		                    	CommonLog.info("cc_list doesn't match with cc_role_list.");
		                    }
		                    Vector v = new Vector();
		                    for (int i=0;i<cc_array.length;i++) {
		                        StringTokenizer stk_cc_role_tmp = (cc_role_array[i] == null) ? null
		                                                                : new StringTokenizer(cc_role_array[i], DELIMITER);
		                        Vector v_tmp = get_cc_entVec(con, cc_array[i], stk_cc_role_tmp);
		                        for (int j=0;j<v_tmp.size();j++) {
		                            Long id_tmp = (Long) v_tmp.elementAt(j);
		                            if (!v.contains(id_tmp)) {
		                                v.addElement(id_tmp);
		                            }
		                        }

		                    }
		                    cc_ent_id = aeUtils.vec2longArray(v);
		                }
		                
		                Timestamp sendTime = cwSQL.getTime(con);
		                aeXMessage aeXmsg = new aeXMessage();
		                param.put("itm_id", new Long(app.app_itm_id));
		                param.put("app_id", new Long(app.app_id));
		                param.put("ent_id", new Long(app.app_ent_id));
		                param.put("action_taker_ent_id", new Long(this.cur_prof.usr_ent_id));
		                if(this.ach_content != null)
		                    param.put("body", this.ach_content);
		                
		                if(rec_ent_id != null && rec_ent_id.length > 0) {
		                    //aeXmsg.insNotify(con, prof, sender_usr_id, rec_ent_id, cc_ent_id, bcc_ent_id, sendTime, param, reply_ent_id);

			                //插入邮件及邮件内容
			                MessageService msgService = new MessageService();
			                String mtp_type = (String)param.get("template_type");
			                
			        		MessageTemplate mtp = new MessageTemplate();
			        		mtp.setMtp_tcr_id(login_prof.my_top_tc_id);
			        		mtp.setMtp_type(mtp_type);
			        		mtp.getByTcr(con);
			        		
			                String[] contents = msgService.getMsgContent(con, mtp, app.app_ent_id, this.login_prof.usr_ent_id, app.app_itm_id, app.app_id,rec_ent_id);
			                
			                if(itm.itm_send_enroll_email_ind == 2) {
			                	// 抄送至直属领导
			                	aeQueueManager qm = new aeQueueManager();
			                	List<Long> supervisors = qm.getMySupervise(con, app.app_ent_id);
			                	if(supervisors != null && supervisors.size() > 0) {
			                		cc_sup_ent_ids = new long[supervisors.size()];
			                		for(int i=0; i<supervisors.size(); i++) {
			                			cc_sup_ent_ids[i] = supervisors.get(i);
			                		}
			                	}
			                }
			                
			                if(cc_sup_ent_ids != null && cc_sup_ent_ids.length > 0) {
			                	msgService.insMessage(con, mtp, sender_usr_id, rec_ent_id, cc_sup_ent_ids, sendTime, contents,app.app_itm_id);
			                } else {
			                	msgService.insMessage(con, mtp, sender_usr_id, rec_ent_id, cc_ent_id, sendTime, contents,app.app_itm_id);
			                }
		                }else {
		                	CommonLog.info("No recipients.");
		                }
	                }
	            } else {
	            	CommonLog.info("not send notifications");
	            }
            } else if( functName.equalsIgnoreCase(FUNC_ENROL) ) {
            	CommonLog.debug("trigger enrol course");
                aeQueueManager qm = new aeQueueManager();
                qm.enrolCos(con, app.app_id, app.app_itm_id, app.app_ent_id, prof, app.app_tkh_id);
                // ind certification
                CFCertificate cf = new CFCertificate(con);
                cf.insCertificationItem((int)app.app_itm_id,(int)app.app_ent_id,0,"Not Certified",(int)prof.root_ent_id,prof.usr_id);
                CommonLog.debug("trigger enrol course DONE");
            } else if( functName.equalsIgnoreCase(FUNC_UNENROL) ) {
//                System.out.println("trigger unenrol course");
                aeQueueManager qm = new aeQueueManager();
                qm.unenrolCos(this.con, this.app.app_id, this.app.app_itm_id, this.app.app_ent_id, prof, this.app.app_id);

//                System.out.println("trigger unenrol course DONE");
            } else if( functName.equalsIgnoreCase(FUNC_RM_APPN) ) {
            	CommonLog.debug("trigger remove application");
                this.app.del(con);
                // del certification
                CFCertificate cf = new CFCertificate(con);
                cf.delCertificationItem((int)app.app_itm_id,(int)app.app_ent_id, (int)prof.root_ent_id);
                //删除该报名的积分
                Credit.deleteOldCreditsByCos(con, app.app_itm_id, app.app_id, app.app_ent_id);
            } else if( functName.equalsIgnoreCase(FUNC_RM_APPN_JI) ){
            	CommonLog.debug("trigget remove applicant ji & reminder");
                Message msg = new Message();
                msg.removeItemRecipient(con, this.app.app_itm_id, this.app.app_ent_id);
            } else if( functName.equalsIgnoreCase(FUNC_FROM_TRIGGER_ACTION) ) {
                //System.out.println("trigger is trigger action");
                returnValue = this.fromTriggerAction ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
            } else if( functName.equalsIgnoreCase(FUNC_CHECK_ACTION_ID) ) {
                //System.out.println("trigger check action id");
                long action_id = Long.parseLong((String)param.get("action_id"));
                returnValue = (action_id == this.action_id) ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
            } else if( functName.equalsIgnoreCase(FUNC_CAPTURE_USR_PROFILE) ) {
            //add for capture user profile as app_usr_prof_xml when the is admitted
            	CommonLog.debug("Trigger capture user profile");
                app.updateProf(con,app.app_id,app.app_ent_id,prof,app.app_upd_timestamp);

            }

            CommonLog.info("returnValue = " + returnValue);

            NodeList eventFunctNodelist = eventFunctElement.getChildNodes();
            NodeList functReturnNodelist = null;
            Element functReturnElement;
            if(functName.equalsIgnoreCase(FUNC_CHECK_CAPACITY))
            	CommonLog.info("------After check capacity, the eventFunctNodelist size: " + eventFunctNodelist.getLength());
            for(int i=0; i<eventFunctNodelist.getLength(); i++) {

                if( ((eventFunctNodelist.item(i)).getNodeName()).equalsIgnoreCase(FUNCTRETURN_TAG) ) {

                    functReturnElement = (Element)eventFunctNodelist.item(i);
                    if(returnValue != null) {
                        if(returnValue.equals(FUNC_RETURN_TRUE)) {

                            if( (functReturnElement.getAttribute(ATTR_VALUE)).equals(FUNC_RETURN_TRUE) ) {
                                functReturnNodelist = functReturnElement.getChildNodes();
                                if(functName.equalsIgnoreCase(FUNC_CHECK_CAPACITY)){
                                	if(functReturnNodelist==null) CommonLog.info("------After check capacity, the functReturnNodelist is null ");
                                	else CommonLog.info("------After check capacity, the functReturnNodelist size: " + functReturnNodelist.getLength());
                                }
                                break;
                            }

                        } else if(returnValue.equals(FUNC_RETURN_FALSE)){

                            if( (functReturnElement.getAttribute(ATTR_VALUE)).equals(FUNC_RETURN_FALSE) ) {

                                if( (functReturnElement.getAttribute(ATTR_ROLLBACK)).equalsIgnoreCase("yes") )
                                    throw new cwSysMessage(aeQueueManager.QM_INVALID_ACTION);

                                functReturnNodelist = functReturnElement.getChildNodes();
                                break;
                            }

                        } else if(returnValue.equals(FUNC_RETURN_OK)){
                        	CommonLog.debug(FUNC_RETURN_OK);
                            if( (functReturnElement.getAttribute(ATTR_VALUE)).equals(FUNC_RETURN_OK) ) {
                                functReturnNodelist = functReturnElement.getChildNodes();
                                break;
                            }

                        } else if(returnValue.equals(FUNC_RETURN_NO_SUPERVISOR)){
                        	CommonLog.debug(FUNC_RETURN_NO_SUPERVISOR);

                            if( (functReturnElement.getAttribute(ATTR_VALUE)).equals(FUNC_RETURN_NO_SUPERVISOR) ) {
                                functReturnNodelist = functReturnElement.getChildNodes();
                                break;
                            }

                        } else if(returnValue.equals(FUNC_RETURN_END_OF_APPROVAL)){
                        	CommonLog.debug(FUNC_RETURN_END_OF_APPROVAL);

                            if( (functReturnElement.getAttribute(ATTR_VALUE)).equals(FUNC_RETURN_END_OF_APPROVAL) ) {
                                functReturnNodelist = functReturnElement.getChildNodes();
                                break;
                            }

                        }
                    }
                }
            }

                if( functReturnNodelist!= null ) {
                    Node node;
                    String nodeName;
                    for(int j=0; j<functReturnNodelist.getLength(); j++) {
                        nodeName = (functReturnNodelist.item(j)).getNodeName();
                        node = functReturnNodelist.item(j);
                        if( nodeName.equalsIgnoreCase(ACTION_TAG) ) {
//                        	System.out.println("tow in ACTION_TAG");

                            processAction((Element)node);


                        } else if( nodeName.equalsIgnoreCase(EVENTFUNCT_TAG) ) {
//                        	System.out.println("tow in EVENTFUNCT_TAG");
                        	if(functName.equalsIgnoreCase(FUNC_CHECK_CAPACITY))
                        		CommonLog.info("------After check capacity, call processFunct: " + ((Element)node).getAttribute(ATTR_NAME));

                            processFunct((Element)node);

                        }
                    }
                }

            return;
        }

    //call the queue manager to ins action
    public void processAction(Element eventActionElement)
        throws SQLException, IOException, cwSysMessage, cwException {

//System.out.println("Process ACTION ...............................................");
            String process_id = eventActionElement.getAttribute(ATTR_PROCESS_ID);
            String status_id = eventActionElement.getAttribute(ATTR_STATUS_ID);
            String action_id = eventActionElement.getAttribute(ATTR_ID);


            if( process_id != null && status_id != null && action_id != null ) {

                NodeList workFlowNodelist;
                NodeList processNodelist;
                NodeList statusNodelist;
                Element processElement;
                Element statusElement;
                Element actionElement;
                String fr = null;
                String to = null;
                String verb = null;
                String next_status = null;


                workFlowNodelist = workFlowNode.getChildNodes();
                loop:
                for(int i=0; i<workFlowNodelist.getLength(); i++) {

                    if( ((workFlowNodelist.item(i)).getNodeName()).equalsIgnoreCase(PROCESS_TAG) ) {

                        processElement = (Element)workFlowNodelist.item(i);
                        if( (processElement.getAttribute(ATTR_ID)).equalsIgnoreCase(process_id) ) {
//System.out.println("ProcessElement at item = " + i);
                        processNodelist = processElement.getChildNodes();
                        for(int j=0; j<processNodelist.getLength(); j++) {

                            if( ((processNodelist.item(j)).getNodeName()).equalsIgnoreCase(STATUS_TAG) ) {

                                statusElement = (Element)processNodelist.item(j);
                                if((statusElement.getAttribute(ATTR_ID)).equalsIgnoreCase(status_id) ) {
//System.out.println("Status Element at item = " + j);

                                    statusNodelist = statusElement.getChildNodes();
                                    for(int k=0; k<statusNodelist.getLength(); k++) {

                                        if( ((statusNodelist.item(k)).getNodeName()).equalsIgnoreCase(ACTION_TAG) ) {

                                            actionElement = (Element)statusNodelist.item(k);
                                            if( (actionElement.getAttribute(ATTR_ID)).equalsIgnoreCase(action_id) ) {
//System.out.println("Action element at item = " + k);
                                                fr = statusElement.getAttribute(ATTR_NAME);
                                                verb = actionElement.getAttribute(ATTR_VERB);
                                                next_status = actionElement.getAttribute(ATTR_NEXT_STATUS);
                                                NodeList _nodelist = processElement.getChildNodes();
                                                Element _element;
                                                for(int l=0; l<_nodelist.getLength(); l++) {
                                                    if( ((_nodelist.item(l)).getNodeName()).equalsIgnoreCase(STATUS_TAG) ) {
                                                        _element = (Element)_nodelist.item(l);
                                                        if( (_element.getAttribute(ATTR_ID)).equalsIgnoreCase(next_status) ) {
//System.out.println("Next status at item = " + l);
                                                            to = _element.getAttribute(ATTR_NAME);
                                                            break loop;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        }

                    }


                }


                if( fr != null && to != null && verb != null ) {
//System.out.println("Call Queue Manager ins action ........................... ");

                    //Long appn_id = (Long)sess.getAttribute(aeQueueManager.APPN_HISTORY_ID);
                    //Vector appn_action = (Vector)sess.getAttribute(aeQueueManager.APPN_HISTORY_ACTION);
                    //sess.removeAttribute(aeQueueManager.APPN_HISTORY_ID);
                    //sess.removeAttribute(aeQueueManager.APPN_HISTORY_ACTION);
                    aeQueueManager qm = new aeQueueManager();
                    qm.send_mail_ind = send_mail_ind;
                    try {
                        //System.out.println("QM Action,");
                        //System.out.println(process_id + " " + status_id + " " + action_id);


                        qm.doAppnActn(con, app.app_id, Long.parseLong(process_id),
                                        fr, to, verb, Long.parseLong(action_id),
                                        Long.parseLong(status_id), /*cur_prof*/prof, null,
                                        null, null, true, this.login_prof);
                        //qm.doAppnAction();
                        /*
                        qm.insAppnAction(con, sess, prof.usr_id, app.app_id,
                                        Long.parseLong(process_id), fr, to,
                                        verb, Long.parseLong(action_id),
                                        Long.parseLong(status_id), prof.usr_ent_id,
                                        prof.current_role);
                        qm.insHistory2DB(con, sess, prof, app.app_id,
                                        "EVENT TRIGGER", null, true);
                        */
                    }
                    catch(qdbException e) {
                        throw new cwException(e.getMessage());
                    }
                    /*
                    if(appn_id != null)
                        sess.setAttribute(aeQueueManager.APPN_HISTORY_ID, appn_id);
                    if(appn_action == null)
                        sess.setAttribute(aeQueueManager.APPN_HISTORY_ACTION, appn_action);
                    */
                } else {
                    // no matched action find in Template
                    throw new cwException("Event Trigger a wrong action");
                }

                return;
            }

        }

        /*
        Get the entity
        */
        protected Vector get_app_approver_ids(Connection con, StringTokenizer stk_role) throws cwException, SQLException{
            String role = new String();
            Vector v = new Vector();
            Vector v_temp = null;
            while(stk_role.hasMoreTokens()) {
                role = stk_role.nextToken();
                v_temp = aeAppnTargetEntity.getAppnTargetEntIds(con, this.app.app_id, role);
                for(int i=0; i<v_temp.size(); i++) {
                    v.addElement((Long)v_temp.elementAt(i));
                }
            }
            return v;
        }

        protected Vector get_item_access_ids(Connection con, StringTokenizer stk_role) throws SQLException, cwException {

            Vector v_iac = aeItemAccess.getItemAccessByItem(con, this.app.app_itm_id);
            if(v_iac == null || v_iac.size() == 0) {
            //if no item access record found
            //check it if it is a run and get the parent's item access
                v_iac = aeItemAccess.getParentItemAccessByItem(con, this.app.app_itm_id);
            }

            Vector v_ent_id = new Vector();
            Vector v_role = new Vector();
            AccessControlWZB acl = new AccessControlWZB();
            Hashtable h_role_map = acl.getAllRoleUid(con, this.prof.root_ent_id, "rol_ste_uid");
            while(stk_role.hasMoreTokens()) {
                String this_role_uid = stk_role.nextToken();
                String this_role = (String) h_role_map.get(this_role_uid);
                if(this_role != null) {
                    v_role.addElement(this_role);
                } else {
                    v_role.addElement(this_role_uid);
                }
            }
            for(int i=0; i < v_iac.size(); i++) {
                aeItemAccess iac = (aeItemAccess) v_iac.elementAt(i);
                if((iac.iac_access_type).equals(aeItemAccess.ACCESS_TYPE_ROLE)
                    && v_role.contains(iac.iac_access_id)) {
                    v_ent_id.addElement(new Long(iac.iac_ent_id));
                }
            }
            return v_ent_id;
        }

        protected long[] get_to_ent_ids(Connection con, String to, StringTokenizer stk_to_role, String appr_role) throws SQLException, cwException {
            long[] ent_id = null;
            if( to.equalsIgnoreCase("applicant") ) {
                ent_id = new long[1];
                ent_id[0] = app.app_ent_id;
            }
            else if( to.equalsIgnoreCase("item_access") ) {
                Vector v_ent_id = get_item_access_ids(con, stk_to_role);
                ent_id = aeUtils.vec2longArray(v_ent_id);
            // Send to the user who create the application record
            }else if ( to.equalsIgnoreCase("initiator") ) {
                ent_id = new long[1];
                ent_id[0] = app.getCreateUsrEntId(con);

            } else {
                Vector v = new Vector();
                if (to.indexOf("current_approvers") > -1){
                    Vector vTemp = DbAppnApprovalList.getCurrentApprovers(con, this.app.app_id);
                    v = aeUtils.unionVectors(v, vTemp);
                }
                if (to.indexOf("previous_approvers") > -1){
                    Vector vTemp = DbAppnApprovalList.getPreviousApprovers(con, this.app.app_id);
                    v = aeUtils.unionVectors(v, vTemp);
                }
                if (to.indexOf("participated_approvers") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedApprovers(con, this.app.app_id);
                    v = aeUtils.unionVectors(v, vTemp);
                }
                if (to.indexOf("participated_tadm") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedTADM(con, this.app.app_id);
                    v = aeUtils.unionVectors(v, vTemp);
                }
                if (to.indexOf("participated_ds") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedDirectSupervise(con, this.app.app_id);
                    v = aeUtils.unionVectors(v, vTemp);
                }
                if (to.indexOf("participated_gs") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedSupervise(con, this.app.app_id);
                    v = aeUtils.unionVectors(v, vTemp);
                }
                ent_id = aeUtils.vec2longArray(v);
            }
            return ent_id;
        }

        protected Vector get_cc_entVec(Connection con, String cc, StringTokenizer stk_cc_role) throws cwException, SQLException {

            Vector entVec = new Vector();
            if( cc.equalsIgnoreCase("applicant") ) {
                entVec.addElement(new Long(this.app.app_ent_id));
            }
            // Approver on that application
            else if( cc.equalsIgnoreCase("app_approver") ) {
                entVec = get_app_approver_ids(con, stk_cc_role);
            }
            else if (cc.equalsIgnoreCase("item_access")) {
                entVec = get_item_access_ids(con, stk_cc_role);
            }
            else if (cc.equalsIgnoreCase("initiator")) {
                entVec.addElement(new Long(app.getCreateUsrEntId(con)));
            }
            else if (cc.equalsIgnoreCase("prof")){
                entVec.addElement(new Long(this.login_prof.usr_ent_id));
            }
            else {
                if (cc.indexOf("current_approvers") > -1){
                    Vector vTemp = DbAppnApprovalList.getCurrentApprovers(con, this.app.app_id);
                    entVec = aeUtils.unionVectors(entVec, vTemp);
                }
                if (cc.indexOf("previous_approvers") > -1){
                    Vector vTemp = DbAppnApprovalList.getPreviousApprovers(con, this.app.app_id);
                    entVec = aeUtils.unionVectors(entVec, vTemp);
                }
                if (cc.indexOf("participated_approvers") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedApprovers(con, this.app.app_id);
                    entVec = aeUtils.unionVectors(entVec, vTemp);
                }
                if (cc.indexOf("participated_tadm") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedTADM(con, this.app.app_id);
                    entVec = aeUtils.unionVectors(entVec, vTemp);
                }
                if (cc.indexOf("participated_ds") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedDirectSupervise(con, this.app.app_id);
                    entVec = aeUtils.unionVectors(entVec, vTemp);
                }
                if (cc.indexOf("participated_gs") > -1){
                    Vector vTemp = DbAppnApprovalList.getParticipatedSupervise(con, this.app.app_id);
                    entVec = aeUtils.unionVectors(entVec, vTemp);
                }
            }

            return entVec;
        }

		public aeApplication getApp() {
			return app;
		}

		public void setApp(aeApplication app) {
			this.app = app;
		}
}