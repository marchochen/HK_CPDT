/*
 * for auto-enrol, override method(s) in parent class 
 */
package com.cw.wizbank.ae;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.AcWorkFlow;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

public class aeQueueManagerAutoEnrol extends aeQueueManager {

	public aeApplication insApplication(Connection con, String app_xml,	long ent_id, long itm_id, loginProfile prof, long aer_app_id,
				long process_id, long status_id, long action_id, String to,	String verb, boolean checkDate, long tkh_id, String comment, String from) 
			throws SQLException, qdbException, IOException, cwSysMessage, cwException {
		aeApplication app = new aeApplication();
		aeItem item = new aeItem();
		Timestamp cur_time = dbUtils.getTime(con);
		item.itm_id = itm_id;
		item.getItem(con);
		if (checkDate) {
			if (item.itm_appn_start_datetime != null
					&& cur_time.before(item.itm_appn_start_datetime)) {
				throw new cwSysMessage("AEQM03");
			}
			if (item.itm_appn_end_datetime != null
					&& cur_time.after(item.itm_appn_end_datetime)) {
				throw new cwSysMessage("AEQM04");
			}
		}
		// init the application to get app_process_xml, app_status and
		// app_process_status
		app = initAppnProcess(con, item, process_id, status_id, action_id, to);

		app.app_itm_id = itm_id;
		app.app_ent_id = ent_id;
		app.app_xml = app_xml;
		app.app_create_usr_id = prof.usr_id;
		app.app_upd_usr_id = prof.usr_id;
		app.app_tkh_id = tkh_id;
		insAppn2DB(con, app, cur_time, prof, aer_app_id, process_id, action_id, to, verb, comment, from);
		return app;
	}

	private aeApplication initAppnProcess(Connection con, aeItem item,	long process_id, long status_id, long action_id, String to)
			throws SQLException, qdbException, IOException, cwSysMessage, cwException {
		aeTemplate tpl = new aeTemplate();
		aeApplication app = new aeApplication();
		tpl.tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
		tpl.get(con);
		if (to != null && (action_id == 0 || status_id == 0)) {
			// hardcode mapping of the enrollment status in xml to the
			// corresponding action id in workflow xml
			// such that it is language independent (just a temporary hardcode)
			// 2004-11-11 kawai
			status_id = 1;
			if (to.equals("Pending Approval")) {
				action_id = 1;
			} else if (to.equals("Waitlisted")) {
				action_id = 6;
			} else if (to.equals("Enrolled")) {
				action_id = 7;
			} else if (to.equals("Not Approved")) {
				action_id = 2;
			} else if (to.equals("Cancelled")) {
				action_id = 3;
			} else {
				throw new cwException("Unknown Enrollment Status:" + to);
			}
		}
		app.app_process_xml = aeWorkFlowCache.getProXmlFromCached(tpl.tpl_id, 1, 1, 1);
		app.app_status = aeWorkFlowCache.getStatusFromQueueRule(tpl.tpl_id, 1, 1);
		app.app_process_status = aeWorkFlowCache.getProcessStatusFromCached(tpl.tpl_id, 1, 1);
		app.workFlowTpl = tpl.tpl_xml;
		app.workFlowTplId = tpl.tpl_id;
		return app;
	}

	private void insAppn2DB(Connection con, aeApplication app,	Timestamp cur_time, loginProfile prof, long aer_app_id,
							long process_id, long action_id, String to, String verb, String comment, String from) 
			throws SQLException, IOException,qdbException, cwException, cwSysMessage {
		aeAppnActnHistory actn = new aeAppnActnHistory();
		aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
		if (cur_time == null) {
			cur_time = cwSQL.getTime(con);
		}
		app.canBeAdmitted = isAppCanBeAdmitted(con, auto_enroll_ind, app.app_itm_id);
		if(!app.canBeAdmitted) {
			//insert into aeApplication
			app.ins(con);
		}
		// insert an application history
		actn.aah_app_id = app.app_id;
		actn.aah_process_id = process_id;
		actn.aah_fr = null;
		if (to != null) {
			actn.aah_to = to;
		} else {
			actn.aah_to = "Applied";
		}
		// DENNIS: 2002-08-01, change the verb to capital letters
		/* actn.aah_verb = "applied"; */
		if (verb != null) {
			actn.aah_verb = verb;
		} else {
			actn.aah_verb = "Applied";
		}
		actn.aah_action_id = action_id;
		actn.aah_create_usr_id = app.app_create_usr_id;
		actn.aah_create_timestamp = cur_time;
		actn.aah_upd_usr_id = app.app_create_usr_id;
		actn.aah_upd_timestamp = cur_time;
		actn.aah_actn_type = from;
		if(app.canBeAdmitted) {
			app.actn1 = actn;
		} else {
			actn.ins(con);
		}

		// Perform event trigger
		Hashtable process_status = workFlow.getCurProcessStatus(app.app_process_xml);
		Enumeration key = process_status.keys();
		aeWorkFlowEventAutoEnrol wfEvent = new aeWorkFlowEventAutoEnrol(con, app.workFlowTpl, app.workFlowTplId);
		wfEvent.auto_enroll_ind = auto_enroll_ind;
		wfEvent.send_mail_ind = send_mail_ind;

		while (key.hasMoreElements()) {
			String event_process_id = (String) key.nextElement();
			String event_status_name = (String) process_status.get(event_process_id);
			wfEvent.eventTrigger(con, app, prof, prof, Long.parseLong(event_process_id), "", event_status_name, 0, null, false);
		}

		return;
	}

    public void doAppnActn(Connection con, long app_id, long process_id, String fr, String to, String verb, long action_id, long status_id,
			loginProfile prof, String content, Timestamp time, Timestamp curTime, boolean triggerAction, loginProfile loginUser, String app_priority, aeApplication app) 
    		throws SQLException, IOException, cwSysMessage, qdbException, cwException {
		aeAppnActnHistory actn2 = new aeAppnActnHistory();
		Vector appn_history = new Vector();
		if (app == null) {
			app = new aeApplication();
			// get application details
			app.app_id = app_id;
			app.app_priority = app_priority;
			app.getWithItem(con);
		}
		if (curTime == null)
			curTime = cwSQL.getTime(con);

		// format the ActionHistory Object
		app.actn2 = actn2;
		actn2.aah_app_id = app.app_id;
		actn2.aah_process_id = process_id;
		actn2.aah_fr = fr;
		actn2.aah_to = to;
		actn2.status_id = status_id;
		actn2.aah_verb = verb;
		actn2.aah_action_id = action_id;
		actn2.aah_create_usr_id = prof.usr_id;
		actn2.aah_create_timestamp = curTime;
		actn2.aah_upd_usr_id = prof.usr_id;
		actn2.aah_upd_timestamp = curTime;
		// get action access control and get the new app_process_xml
		if (!triggerAction) {
			actn2.status = aeWorkFlowCache.getProXmlFromCached(app.workFlowTplId, process_id, status_id, action_id);
			if (actn2.status == null || actn2.status.equals("")) {
				throw new cwSysMessage(QM_INVALID_ACTION);
			}
		} else {
			actn2.status = aeWorkFlowCache.getProXmlFromCached(app.workFlowTplId, process_id, status_id, action_id);
		}
		appn_history.addElement(actn2);
		if (process_id == 0) {
			app.app_status = actn2.aah_to;
		} else {
			app.app_process_xml = actn2.status;
			Hashtable action_temp = aeWorkFlowCache.getProActionFromCached(app.workFlowTplId, String.valueOf(process_id), String.valueOf(status_id), String.valueOf(action_id));
			String next_status_id = (String) action_temp.get(aeWorkFlowEvent.ATTR_NEXT_STATUS);
			app.app_status = aeWorkFlowCache.getStatusFromQueueRule(app.workFlowTplId, process_id, Long.parseLong(next_status_id));
		}
		// save the action into database
		saveAppnActn(con, app, content, triggerAction, appn_history, time, prof, loginUser);
	}
    
    private void saveAppnActn(Connection con, aeApplication app,
			String content, boolean triggerAction, Vector appn_action,
			Timestamp time, loginProfile prof, loginProfile loginUser)
			throws SQLException, IOException, cwSysMessage, qdbException,
			cwException {
		String lastest_status = null;
		aeAppnActnHistory actn2;
		// for each action history, insert into aeAppnActnHistory
		if (appn_action != null && appn_action.size() != 0) {
			for (int i = 0; i < appn_action.size(); i++) {
				actn2 = (aeAppnActnHistory) appn_action.elementAt(i);
				if (app.app_id == actn2.aah_app_id) {
					// access control on each action
					if (!triggerAction) {
						AcWorkFlow acWorkFlow = new AcWorkFlow(con,	app.workFlowTpl);
						if (!acWorkFlow.checkPrivilege(prof.usr_ent_id,
								prof.current_role, actn2.aah_process_id,
								actn2.status_id, actn2.aah_action_id, app,
								triggerAction)) {
							throw new cwSysMessage(QM_INVALID_ACTION);
						}
					}
					if(!app.canBeAdmitted) {
						actn2.aah_id = actn2.ins(con);
					}
					lastest_status = actn2.aah_to;
				}
			}
			// cannot support multiple action
			actn2 = (aeAppnActnHistory) appn_action.elementAt(appn_action.size() - 1);
			if (actn2.aah_process_id == 0) {
				// ??
				app.app_status = actn2.aah_to;
			} else {
				app.app_process_xml = actn2.status;
			}
			if (triggerAction || time == null || app.checkUpdTimestamp(time)) {
				app.app_upd_usr_id = prof.usr_id;
				app.app_upd_timestamp = time;
				app.app_process_status = lastest_status;
				if(!app.canBeAdmitted) {
					if (triggerAction) {
						app.updNoTime(con);
					} else {
						app.upd(con);
					}
				}
			} else {
				throw new cwSysMessage("AEQM02");
			}
		}
		// event trigger
		for (int i = 0; i < appn_action.size(); i++) {
			actn2 = (aeAppnActnHistory) appn_action.elementAt(i);
			if (app.app_id == actn2.aah_app_id) {
				aeWorkFlowEventAutoEnrol wfEvent = new aeWorkFlowEventAutoEnrol(con, app.workFlowTpl, app.workFlowTplId);
				// transfer the send mail indicator
				wfEvent.send_mail_ind = send_mail_ind;
				// wfEvent.auto_enroll_ind = auto_enroll_ind;
				wfEvent.eventTrigger(con, app, prof, loginUser, actn2.aah_process_id, actn2.aah_fr, actn2.aah_to,
										actn2.aah_action_id, content, triggerAction);
			}
		}
	}

    /**
    Enrol into a course<Br>
    Insert into ResourcePermission and Enrollment<BR>
    Active aeAppnEnrolRelation by (ent_id, res_id)<BR>
     * @throws qdbException 
    */
    public void enrolCos(Connection con, long app_id, long itm_id, long ent_id, loginProfile prof, long tkh_id, aeApplication app)
        throws SQLException, cwException, cwSysMessage, qdbException {
        dbCourse cos = new dbCourse();
        cos.cos_res_id = getResId(con, itm_id);
        cos.cos_itm_id = itm_id;
        if(cos.cos_res_id != 0) {
            //save attendance
            int ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, "PROGRESS");
            aeAttendanceAutoEnrol.saveStatus(con, app_id, itm_id , cos.cos_res_id, ent_id, false, ats_id, prof.usr_id, tkh_id, app);
            //update enrolment
            cos.enroll(con, ent_id, prof);
			long temp_ach_aah_id = 0;
            if(app != null) {
            	if(app.actn1 != null) {
            		app.actn1.aah_app_id = app.app_id;
            		temp_ach_aah_id = app.actn1.ins(con);
            		app.actn1.aah_id = temp_ach_aah_id;
            	}
            	if(app.actn2 != null && app.actn2.aah_id == 0) {
            		app.actn2.aah_app_id = app.app_id;
            		temp_ach_aah_id = app.actn2.ins(con);
            		app.actn2.aah_id = temp_ach_aah_id;
            	}
            	if (app.comm != null) {
            		app.comm.ach_app_id = app.app_id;
            		app.comm.ach_aah_id = temp_ach_aah_id;
            		app.comm.ins(con);
            	}
            }
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            itm.getItem(con);
            if (itm.itm_create_session_ind){
                // check if the session has attendance
                DbItemType dbIty = new DbItemType();
                dbIty.ity_id = itm.itm_type;
                dbIty.ity_owner_ent_id = itm.itm_owner_ent_id;
                dbIty.ity_run_ind = false;
                dbIty.ity_session_ind = true;
                Hashtable h_ity_inds = dbIty.getInd(con);
                if (((Boolean)h_ity_inds.get("ity_has_attendance_ind")).booleanValue()){
                    Vector v_app_id = new Vector();
                    v_app_id.addElement(new Long(app_id));
                    aeSession.insSessionAttendance(con, itm_id, null, v_app_id, ats_id, prof.usr_id, prof.root_ent_id);
                }
            }
        }
        return;
    }
    
    private boolean isAppCanBeAdmitted(Connection con, boolean auto_enroll_ind, long itm_id) throws SQLException, cwSysMessage, cwException, qdbException {
    	boolean result = false;
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		itm.get(con);
    	result = auto_enroll_ind || (itm.itm_app_approval_type == null);
    	return result;
    }
}
