package com.cw.wizbank.batch.CourseNotification;

import java.sql.*;
import java.util.Vector;
import java.util.Calendar;

import com.cw.wizbank.util.*;
import com.cw.wizbank.batch.batchUtil.*;
import com.cw.wizbank.db.DbCourseModuleCriteria;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.view.ViewCourseModuleCriteria;
//import qdb.dbModuleEvaluation;
//import qdb.qdbEnv;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbCourse;
//import qdb.dbAiccPath;

import com.cw.wizbank.message.Message;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.db.DbMgRecipient;
import com.cw.wizbank.db.DbMgView;
import com.cw.wizbank.db.DbMgItmSelectedMessage;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.accesscontrol.acSite;
import com.cwn.wizbank.utils.CommonLog;

public class CourseNotification {
    
    public final static String STATIC           =   "STATIC";
    public final static String MSG_TPL_COURSE_NOTIFY = "COURSE_LATE_ATTEND_REMINDER";


    public static void main(String argv[]) {
        try{
            Vector site = null;
            boolean reSend = false;
            for (int i = 0; i < argv.length; i++) {
//                System.out.println(argv[i]);
                // if it is an option argument
                if (argv[i].charAt(0) == '-') {
                    if (argv[i].equals("-s")) 
                        site = cwUtils.splitToVec(argv[++i], "~");
                    if (argv[i].equals("-r")) 
                        reSend = true;
                    
                }else{
                	Connection con = BatchUtils.openDB(argv[i++]);//this argv must be the app root
                    String iniFile = argv[i++];
                    cwIniFile ini = new cwIniFile(iniFile);
                    
                    String subject = ini.getValue("COURSE_NOTIFY_SUBJECT");
                    String sender = ini.getValue("COURSE_NOTIFY_SENDER");
//                    System.out.println("("+sender+")");
//                    System.out.println(subject);
                    
                    if (site == null){
                        site = new Vector();
                        PreparedStatement stmt = acSite.getSite(con);
                        ResultSet rs = stmt.executeQuery();
                        while(rs.next()){
                            site.addElement(new Long(rs.getLong("ste_ent_id")));    
                        }
                    }
                    for (int k=0; k<site.size(); k++){
                        long siteId = ((Long)site.elementAt(k)).longValue();
                        loginProfile prof = BatchUtils.getProf(con, siteId, iniFile);
                        dbRegUser usr = new dbRegUser();
                        usr.usr_ent_id = dbRegUser.getEntId(con, sender, siteId);
                        String sender_usr_id = usr.getUserId(con);
                        sendNotification(con, siteId, reSend, prof, subject, sender_usr_id);
                    }
                    con.commit();
                }
            }
        }catch(Exception e){
            System.err.println("Error:" + e.getMessage());    
        }
    }        
    
    public static void sendNotification(Connection con, long root_ent_id, boolean reSend, loginProfile prof, String subject, String sender_usr_id) throws SQLException, cwException, cwSysMessage{
        // get user that is still active in course
        ViewCourseModuleCriteria.ViewAttendDate[] modCrit = ViewCourseModuleCriteria.getActiveList(con, root_ent_id, DbCourseCriteria.TYPE_NOTIFICATION);
        // next step is to check the notification date 
        Timestamp curTime = cwSQL.getTime(con);
        for (int i = 0; i < modCrit.length; i++) {
            Timestamp content_start = modCrit[i].itm_content_eff_start_datetime;
            Timestamp content_end = modCrit[i].itm_content_eff_end_datetime;
            long content_duration = modCrit[i].itm_content_eff_duration;
            Timestamp att_create_date = modCrit[i].att_create_timestamp;  
            long notification_duration = modCrit[i].ccr_duration;
            Timestamp course_start = null;
            Timestamp course_end = null;
            Timestamp notification_date = null;
            if (content_start!=null){
                course_start = content_start;
                course_end = content_end;
                notification_date = new Timestamp(content_start.getTime() + notification_duration*24*60*60*1000);
            }else if (content_duration!=0){
                course_start = att_create_date;
                course_end = new Timestamp(att_create_date.getTime() + content_duration*24*60*60*1000);
                notification_date = new Timestamp(att_create_date.getTime() + notification_duration*24*60*60*1000);
            }else{
                course_start = att_create_date;
                course_end = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
                notification_date = new Timestamp(att_create_date.getTime() + notification_duration*24*60*60*1000);
            } 
            // send reminder after buffer period
            if (curTime.before(notification_date)){
                // next record
            	CommonLog.info("notification buffer not end yet");
            	CommonLog.info("course start: " + course_start + " course_end: " + course_end + " notification_date:" + notification_date );
                continue;
            }
                        
            Vector vtCmr = DbCourseModuleCriteria.getByCcrId(con, modCrit[i].ccr_id);
            long cos_id = modCrit[i].cos_res_id;
            long itm_id = modCrit[i].app_itm_id;
            String cos_title = dbCourse.getCosTitle(con, cos_id);
            long usr_ent_id = modCrit[i].cov_ent_id;
            if (vtCmr.size() == 0){
                continue;    
            }
            Calendar rangeStart = null;
            rangeStart = Calendar.getInstance();
            rangeStart.setTime(course_start);
            Timestamp end = curTime;
            Calendar rangeEnd = null;
            rangeEnd = Calendar.getInstance();
            rangeEnd.setTime(end);

            // user not fulfill either one module
            if (!CourseCriteria.checkOrStatus(con, vtCmr, rangeStart, rangeEnd, usr_ent_id, modCrit[i].cov_tkh_id)){
                // if allow resend , no need to check
                if (!reSend){
                        if (DbMgView.checkItmEntExists(con, itm_id, usr_ent_id, MSG_TPL_COURSE_NOTIFY, DbMgRecipient.RECIPIENT)){
                            // go to next record
                        	CommonLog.info("itm: " + itm_id + ", usr: " + usr_ent_id + ",  reminder sent before");
                            continue;    
                        }else{
                            // send notify    
                        }
                }
//                String course_end_str = course_end.toString();
//                if (course_end.equals(Timestamp.valueOf(cwUtils.MAX_TIMESTAMP))){
//                    course_end_str = cwUtils.UNLIMITED;
//                }else{
//                    System.out.println("no equal");
//                }
                String[] value = {"get_notify_xml", usr_ent_id+"", sender_usr_id, cos_title, course_start.toString(), course_end.toString()};
                insNotify(con, prof, sender_usr_id, usr_ent_id, subject, value, itm_id);
                CommonLog.info("itm: " + itm_id + ", usr: " + usr_ent_id + ",  reminder sending");
            }else{
                CommonLog.info("itm: " + itm_id + ", usr: " + usr_ent_id + ",  criteria fulfilled");
            }
//            System.out.println("THE END");
        }
    }
    
    static final String name[] = {"cmd", "ent_id", "sender_id", "title", "appn_date", "last_date"};
    static final String type[] = {STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};

    public static void insNotify(Connection con, loginProfile prof, String sender_usr_id, long ent_id, String subject, String[] value, long itm_id)
        throws SQLException, cwException, cwSysMessage {
            
            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_send_usr_id = sender_usr_id;
            dbMsg.msg_create_usr_id = prof.usr_id;                        
            dbMsg.msg_create_timestamp = cwSQL.getTime(con);
            dbMsg.msg_target_datetime = cwSQL.getTime(con);
            dbMsg.msg_subject = subject;
            
            Vector vec = new Vector();

            String[] xtp_subtype = {"HTML"};
            long[] ent_ids = {ent_id};
            vec = notifyParams(name, type, value);
            
            Message msg = new Message();
            msg.insNotify(con, ent_ids, null, MSG_TPL_COURSE_NOTIFY, xtp_subtype, dbMsg, vec);
            DbMgItmSelectedMessage dbMgItm = new DbMgItmSelectedMessage();
            dbMgItm.ism_itm_id = itm_id;
            dbMgItm.ism_msg_id = msg.msg_id;
            dbMgItm.ism_type = MSG_TPL_COURSE_NOTIFY;
            dbMgItm.ins(con);
                
            return;
        }

    public static Vector notifyParams(String[] name, String[] type, String[] value) {
                
        Vector params = new Vector();
        Vector paramsName = new Vector();
        Vector paramsType = new Vector();
        Vector paramsValue = new Vector();
                
        for(int i=0; i<name.length; i++) {
            paramsName.addElement(name[i]);
            paramsType.addElement(type[i]);
            paramsValue.addElement(value[i]);
        }

        params.addElement(paramsName);
        params.addElement(paramsType);
        params.addElement(paramsValue);

        return params;
        
    }
}