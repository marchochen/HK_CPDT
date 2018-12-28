/*
 * for auto-enrol, override method(s) in parent class 
 */
package com.cw.wizbank.ae;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cw.wizbank.accesscontrol.AcQueueManager;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
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

public class aeWorkFlowEventAutoEnrol extends aeWorkFlowEvent {
    //class variables
    public Connection con = null;
    private aeApplication app = null;
    public boolean fromTriggerAction = false;  //indicate if this event comes from a trigger action
    private long aah_id; //aah_id of the application action
    public long action_id; //action id of the action triggered this event
    public String ach_content; //appn action comment
    public static final String DEFAULT_DELIMITER = "~";

    /*
    Constructor of aeWorkFlowEventAutoEnrol, parse the string to a node
    */
    public aeWorkFlowEventAutoEnrol(Connection con, String actionXML, long tplId) throws IOException, cwException{
    	super(con, actionXML, tplId);
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
    public void eventTrigger(Connection con, aeApplication app,
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
                return;
            }
            //scan the childs, find a matched event
            else {
                this.con = con;
                this.app = app;
                if(this.prof == null) {
                    this.prof = dbRegUser.getSiteDefaultSysProfile(con, cur_prof);
                }
                if(this.cur_prof == null || cur_prof.usr_ent_id != this.prof.usr_ent_id) {
                    this.cur_prof = cur_prof;
                }
                if( this.login_prof == null ){
                    this.login_prof = login_prof;
                    this.loginUser = new dbRegUser();
                    this.loginUser.usr_ent_id = login_prof.usr_ent_id;
                    try{
                        this.loginUser.get(con);
                    }catch(qdbException e){
                        throw new cwException(e.getMessage());
                    }
                }
                this.fromTriggerAction = fromTriggerAction;
                this.action_id = action_id;
                this.ach_content = ach_content;
                CommonLog.debug("Attributes match process_id, fr, to, action id: " + process_id + "," + fr + "," + to + "," + action_id);
                Node node = aeWorkFlowCache.getEventNodeFromCachedEventTrig(app.workFlowTplId, process_id, fr, to);
                if (node != null) {
                	processEvent(node);
                }
            }
            return;
        }
    
    // process the event
    // if the event is perform a action, call queue manager to ins action
    // if the event is perform a function, call the function
    public void processEvent(Node eventNode) throws SQLException, IOException, cwSysMessage, cwException ,qdbException{
        NodeList nodelist = eventNode.getChildNodes();
        Node node;
        String nodeName;
        for(int i=0; i<nodelist.getLength(); i++) {
            node = nodelist.item(i);
            nodeName = node.getNodeName();
            if( nodeName.equalsIgnoreCase(EVENTFUNCT_TAG) ) {
                processFunct((Element)node);
            } else if ( nodeName.equalsIgnoreCase(ACTION_TAG) ) {
                processAction((Element)node);
            }
        }
        return;
    }

    //process the function and following action if necessary
    public void processFunct(Element eventFunctElement)
        throws SQLException, cwException, IOException, cwSysMessage ,qdbException{

            String functName = eventFunctElement.getAttribute(ATTR_NAME);
            String returnValue = null;
            Hashtable param = getFunctParam(eventFunctElement);
			if( functName.equalsIgnoreCase(FUNC_CHECK_CAPACITY ) ) {
				returnValue = FUNC_RETURN_TRUE;
			} else if( functName.equalsIgnoreCase(FUNC_END_APPROVAL) ) {
                String action = (String)param.get("action");
                aeQueueManager qm = new aeQueueManager();
                qm.send_mail_ind = send_mail_ind;
                qm.endApproval(this.con, this.app.app_id, this.app.actn2.aah_id, action, this.login_prof);
            } else if( functName.equalsIgnoreCase(FUNC_AUTO_ENROLL ) ) {
                AcQueueManager acqm = new AcQueueManager(con);
                CommonLog.debug("aeworkflowevent.auto_enroll_ind= "+auto_enroll_ind);
                acSite site = new acSite();
				site.ste_ent_id = this.login_prof.root_ent_id;
                returnValue = (auto_enroll_ind && (acqm.hasAdminPrivilege(this.login_prof.usr_ent_id, this.login_prof.current_role) || this.login_prof.usr_ent_id == site.getSiteSysEntId(con))) ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
            } else if( functName.equalsIgnoreCase(INS_JI_RECIPIENT ) ) {
            	if (aeItem.getRunInd(con, app.app_itm_id)){
//	                prof.my_top_tc_id = ViewTrainingCenter.getTopTc(con, prof.usr_ent_id, false);
            		aeItemMessage.insNotifyForJI(con, prof, app.app_ent_id, app.app_itm_id, app.app_id);
            	}
            } else if( functName.equalsIgnoreCase(FUNC_FROM_TRIGGER_ACTION) ) {
                returnValue = this.fromTriggerAction ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
            } else if( functName.equalsIgnoreCase(FUNC_REQUIRE_APPROVAL ) ) {
                aeItem itm = new aeItem();
                itm.itm_id = this.app.app_itm_id;
                itm.get(con);
                returnValue = (itm.itm_app_approval_type != null) ? FUNC_RETURN_TRUE : FUNC_RETURN_FALSE;
            } else if( functName.equalsIgnoreCase(FUNC_ENROL) ) {
            	CommonLog.debug("trigger enrol course");
                aeQueueManagerAutoEnrol qm = new aeQueueManagerAutoEnrol();
                qm.enrolCos(con, app.app_id, app.app_itm_id, app.app_ent_id, prof, app.app_tkh_id, app);
                CommonLog.debug("trigger enrol course DONE");
            } else if( functName.equalsIgnoreCase(FUNC_TO_NEXT_APPROVERS) ) {
                aeQueueManager qm = new aeQueueManager();
                returnValue = qm.toNextApprovers(this.con, this.app.app_id, this.aah_id, this.app.app_itm_id, this.app.app_ent_id, this.login_prof);
            } else if( functName.equalsIgnoreCase(FUNC_SEND_NOTIFY) && send_mail_ind //for enrollment import 
            		) {
            	aeItem itm = new aeItem();
            	itm.itm_id = app.app_itm_id;
            	itm.getSendMailInd(con);
	            if (itm.itm_send_enroll_email_ind > 0 
	            		&& MessageTemplate.isActive(con, this.login_prof.my_top_tc_id, (String)param.get("template_type"))) {
	            	CommonLog.debug("SEND_NOTIFY aeWorkFlowEventAutoEnrol.Line176............."+param.get("template_type")+"...........");
	                String DELIMITER = ";";
	                long[] ent_id = null;
	                long[] cc_ent_id = null;
	                long[] bcc_ent_id = null;
	                long[] reply_ent_id = null;
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
	                            site.ste_ent_id = this.prof.root_ent_id;
	                            sender.usr_ent_id = site.getSiteSysEntId(con);
	                            sender.get(con);
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
		                    ent_id = get_to_ent_ids(con, to, stk_to_role, appr_role);
		                }
		                //Either use cc or cc_list
		                if( cc != null ) {
		                    Vector v = get_cc_entVec(con, cc, stk_cc_role);
		                    cc_ent_id = aeUtils.vec2longArray(v);
		                }else if (cc_list != null && cc_list.length() > 0) {
		                    String[] cc_array = cwUtils.splitToString(cc_list, DEFAULT_DELIMITER);
		                    String[] cc_role_array = cwUtils.splitToString(cc_role_list, DEFAULT_DELIMITER);
		                    if (cc_array.length != cc_role_array.length) {
		                    	CommonLog.debug("cc_list doesn't match with cc_role_list.");
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

		                if(ent_id != null && ent_id.length > 0) {
		//                    aeXmsg.insNotify(con, prof, sender_usr_id, ent_id, cc_ent_id, sendTime, param);
		                   // aeXmsg.insNotify(con, prof, sender_usr_id, ent_id, cc_ent_id, bcc_ent_id, sendTime, param, reply_ent_id);
		                  //插入邮件及邮件内容
		                    MessageService msgService = new MessageService();
		                    String mtp_type = (String)param.get("template_type");
		                    
		            		MessageTemplate mtp = new MessageTemplate();
		            		mtp.setMtp_tcr_id(prof.my_top_tc_id);
		            		mtp.setMtp_type(mtp_type);
		            		mtp.getByTcr(con);
		            		
		                    String[] contents = msgService.getMsgContent(con, mtp, app.app_ent_id, this.cur_prof.usr_ent_id, app.app_itm_id, app.app_id,ent_id);
		    	            msgService.insMessage(con, mtp, sender_usr_id, ent_id, cc_ent_id, sendTime, contents,0);
		                }else {
		                	CommonLog.info("No recipients.");
		                }
	                }
	            } else {
	            	CommonLog.info("not send notifications");
	            }
            } 

			CommonLog.info("returnValue = " + returnValue);
            NodeList eventFunctNodelist = eventFunctElement.getChildNodes();
            NodeList functReturnNodelist = null;
            Element functReturnElement;
            for(int i=0; i<eventFunctNodelist.getLength(); i++) {
                if( ((eventFunctNodelist.item(i)).getNodeName()).equalsIgnoreCase(FUNCTRETURN_TAG) ) {
                    functReturnElement = (Element)eventFunctNodelist.item(i);
                    if(returnValue != null) {
                        if(returnValue.equals(FUNC_RETURN_TRUE)) {
                            if( (functReturnElement.getAttribute(ATTR_VALUE)).equals(FUNC_RETURN_TRUE) ) {
                                functReturnNodelist = functReturnElement.getChildNodes();
                                break;
                            }
                        } else if(returnValue.equals(FUNC_RETURN_FALSE)){
                            if( (functReturnElement.getAttribute(ATTR_VALUE)).equals(FUNC_RETURN_FALSE) ) {
                                if( (functReturnElement.getAttribute(ATTR_ROLLBACK)).equalsIgnoreCase("yes") ) {
                                	throw new cwSysMessage(aeQueueManager.QM_INVALID_ACTION);
                                }
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
                        processAction((Element)node);
                    } else if( nodeName.equalsIgnoreCase(EVENTFUNCT_TAG) ) {
                        processFunct((Element)node);
                    }
                }
            }
            return;
        }
    
    //call the queue manager to ins action
    public void processAction(Element eventActionElement) throws SQLException, IOException, cwSysMessage, cwException {
        String process_id = eventActionElement.getAttribute(ATTR_PROCESS_ID);
        String status_id = eventActionElement.getAttribute(ATTR_STATUS_ID);
        String action_id = eventActionElement.getAttribute(ATTR_ID);
        if( process_id != null && status_id != null && action_id != null ) {
            String fr = null;
            String to = null;
            String verb = null;
            String next_status = null;
            Hashtable action_temp = aeWorkFlowCache.getProActionFromCached(app.workFlowTplId, process_id, status_id, action_id);
            fr = aeWorkFlowCache.getProcessStatusFromCached(app.workFlowTplId, Long.parseLong(process_id), Long.parseLong(status_id));
            verb = (String)action_temp.get(ATTR_VERB);
            next_status = (String)action_temp.get(ATTR_NEXT_STATUS);
            to = aeWorkFlowCache.getProcessStatusFromCached(app.workFlowTplId, Long.parseLong(process_id), Long.parseLong(next_status));
            if( fr != null && to != null && verb != null ) {
                aeQueueManagerAutoEnrol qm = new aeQueueManagerAutoEnrol();
                qm.send_mail_ind = send_mail_ind;
                try {
                    qm.doAppnActn(con, app.app_id, Long.parseLong(process_id),
                                    fr, to, verb, Long.parseLong(action_id),
                                    Long.parseLong(status_id), prof, null,
                                    null, null, true, this.login_prof, null, this.app);
                }
                catch(qdbException e) {
                    throw new cwException(e.getMessage());
                }
            } else {
                // no matched action find in Template
                throw new cwException("Event Trigger a wrong action");
            }
            return;
        }
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


    
}
