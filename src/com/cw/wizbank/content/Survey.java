package com.cw.wizbank.content;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

import javax.servlet.http.*;

import com.cw.wizbank.db.*;
import com.cw.wizbank.ae.db.view.ViewSurvey;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.config.WizbiniLoader;

// to be replace
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbUtils;



// for tna, cos Evn
public class Survey extends dbModule{
    
    public Survey() {
        super();
        vtOrder = new Vector();
    }  
    public Survey(dbModule dbmod) {
        
        mod_res_id = dbmod.mod_res_id;
        mod_type = dbmod.mod_type;
        mod_instruct = dbmod.mod_instruct;
        mod_in_eff_start_datetime = dbmod.mod_in_eff_start_datetime;
        mod_in_eff_end_datetime = dbmod.mod_in_eff_end_datetime;
        mod_eff_start_datetime = dbmod.mod_eff_start_datetime;
        mod_eff_end_datetime = dbmod.mod_eff_end_datetime;
 
        res_id = dbmod.res_id;
        res_lan = dbmod.res_lan;
        res_title = dbmod.res_title;
        res_desc = dbmod.res_desc;

        res_type = dbmod.res_type; 
        res_subtype = dbmod.res_subtype; 
        res_privilege = dbmod.res_privilege;
        res_usr_id_owner = dbmod.res_usr_id_owner;
        res_tpl_name = dbmod.res_tpl_name;
        res_mod_res_id_test = dbmod.res_mod_res_id_test;
        res_status = dbmod.res_status;
        res_upd_user = dbmod.res_upd_user;
        res_upd_date = dbmod.res_upd_date;
        res_src_type = dbmod.res_src_type;
        res_src_link = dbmod.res_src_link;
        res_instructor_name = dbmod.res_instructor_name;
        res_instructor_organization = dbmod.res_instructor_organization;        
        vtOrder = new Vector();
    }
    
    public Vector vtOrder;
    
/*
    public class order{
        public String res_title;
        public String usr_display_bil;
        public String mod_eff_start_datetime;
        public String mod_eff_end_datetime;
    }
    */
    
    public static String buildOrderSQL(Vector vtOrder){
        StringBuffer SQL = new StringBuffer();
        if (vtOrder !=null){
            if (vtOrder.size() != 0){
                SQL.append(" ORDER BY ");
                OrderObj thisObj = null;
                for (int i=0; i<vtOrder.size(); i++){
                    thisObj = (OrderObj) vtOrder.elementAt(i);                        
                    SQL.append(thisObj.colName).append(" ").append(thisObj.order);
                    if (i+1<vtOrder.size()){
                        SQL.append(", ");   
                    }
                }
            }
        }
        return SQL.toString();
    } 
    /*
    public void getExtendXML(Connection con) throws qdbException, cwException, SQLException{
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<extension>");
        Vector resLst = new Vector();
        Vector usrId = new Vector();
        
        
        if (res_id ==0){
            res_id = mod_res_id;   
        }
        Long Long_res_id = new Long(res_id); 
        resLst.addElement(Long_res_id);
        usrId.addElement(res_usr_id_owner);
            
        Hashtable htAssignedUsrCnt = dbProgress.assignedNum(con, resLst);
        Hashtable htAttemptUsrCnt = dbProgress.attemptNum(con, resLst);
        Hashtable htUsrAndItsGroup = dbRegUser.getUsrAndItsGroup(con, usrId);
    
        xmlBuf.append("<assign_count>");
        xmlBuf.append(htAssignedUsrCnt.get(Long_res_id));
        xmlBuf.append("</assign_count>");
        xmlBuf.append("<attempt_count>");
        xmlBuf.append(htAttemptUsrCnt.get(Long_res_id));
        xmlBuf.append("</attempt_count>");
        xmlBuf.append("<last_notify_date>");
        xmlBuf.append(DbMgView.getLatestDate(con, res_id));            
        xmlBuf.append("</last_notify_date>");
        xmlBuf.append("<owner>");
        dbRegUser usr = (dbRegUser) htUsrAndItsGroup.get(res_usr_id_owner);
        xmlBuf.append(usr.usrAndItsGroupAsXML());
        xmlBuf.append("</owner>");

        xmlBuf.append("</extension>");
        
        extension = xmlBuf.toString();
                    
    }
    */
    
    // add page
    public void getNotifyLstAsXML(Connection con, loginProfile prof, String usrOrder, int curPage) throws cwException, qdbException, SQLException{
        int pageSize = 10;
        long[] msgIds = DbMgTnaSelectedMessage.getMessageIds(con, mod_res_id);
        Hashtable htNotifyDate = DbMgView.getRecipientNotificationDate(con, msgIds, "RECIPIENT", null, 0);
        
        dbProgress dbpgr = new dbProgress();
        dbpgr.pgr_res_id = mod_res_id;

        Vector usrAllPgr = getUsrId(con, usrOrder);

        Vector usrLatestPgr = new Vector();

        Vector usrIds = new Vector();
        dbProgress thisPgr = null;
        
        // build the usr lst first!! batch for get user infos at once
        // by the way, filter other attempts
        for (int i=0; i<usrAllPgr.size(); i++){
            thisPgr = (dbProgress) usrAllPgr.elementAt(i);
            if (!usrIds.contains(thisPgr.pgr_usr_id)){
                usrIds.addElement(thisPgr.pgr_usr_id); 
                usrLatestPgr.addElement(thisPgr);
            }else{
                // this is not last attempt, let's filter them out..    
            }
        }
        
        Hashtable htUsrAndItsGroup = dbRegUser.getUsrAndItsGroup(con, usrIds);
        
        // build xml
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<extension>");
        xmlBuf.append("<notify_count>").append(usrLatestPgr.size()).append("</notify_count>");        
        xmlBuf.append("<page_size>").append(pageSize).append("</page_size>");        

        thisPgr = null;
        int[] startEnd = calcVectorPos(usrLatestPgr, pageSize, curPage);

        for (int i=startEnd[0]; i<=startEnd[1]; i++){
            thisPgr = (dbProgress) usrLatestPgr.elementAt(i);
            xmlBuf.append("<notified_usr>").append(cwUtils.NEWL);
            dbRegUser usr = (dbRegUser) htUsrAndItsGroup.get(thisPgr.pgr_usr_id);
            xmlBuf.append(usr.usrAndItsGroupAsXML());
            xmlBuf.append("<last_notify_date>").append(cwUtils.NEWL);
            xmlBuf.append(htNotifyDate.get(new Long(usr.usr_ent_id)));
            xmlBuf.append("</last_notify_date>").append(cwUtils.NEWL);
            xmlBuf.append(thisPgr.attemptAsXML());
            xmlBuf.append("</notified_usr>").append(cwUtils.NEWL);
        }
        xmlBuf.append("</extension>");
        
        extension = xmlBuf.toString();
    }

    public void pickUsr(Connection con, long[] entIds) throws qdbException, SQLException{

        if (res_id ==0){
            res_id = mod_res_id;    
        }
        
        Vector usrIds = new Vector(); 
        Vector tempVec = new Vector();
        // assume size of entIds < 10 
        for (int i=0; i<entIds.length; i++){
            tempVec = dbUserGroup.getUserVec(con, entIds[i]); 
            dbUtils.appendVec(usrIds, tempVec);
        }
        
        Timestamp curTime = cwSQL.getTime(con);
        String usr_id = null;
        for (int i=0; i<usrIds.size(); i++){
            usr_id = (String) usrIds.elementAt(i);

            if (dbProgress.usrAttemptNum(con, res_id, usr_id) ==0){
                dbProgress pgr = new dbProgress();
                pgr.pgr_usr_id = usr_id;
                pgr.pgr_res_id = res_id;
                pgr.pgr_start_datetime = curTime;
                pgr.pgr_status = "PENDING";
                pgr.pgr_attempt_nbr = 0;
                pgr.ins(con, 0);
             
            }else{
                // for usr who have attempted this survey, no need to add record attempt_nbr = 0 for it    
            }
        } // end for
    }
    
        // dup from genTestAsXML
    public String svyAsXML(Connection con, loginProfile prof, String uploadDir, Vector resIdVec)
        throws cwException, cwSysMessage, SQLException
    {
        try{
            String qList = "";
            
            // pass the resIdVec as reference and store all the queId in the test
            // return to qdbAction for access control
            Hashtable queOrder = new Hashtable();
            qList = getSuqAsXML(con, prof, resIdVec, queOrder); 
            
            Timestamp curTime = cwSQL.getTime(con);
                
            StringBuffer result = new StringBuffer();
            result.append("<?xml version=\"1.0\" encoding=\"").append(cwUtils.ENC_UTF);
            result.append("\" standalone=\"no\" ?>").append(cwUtils.NEWL);
            result.append("<quiz id=\"").append(res_id);
            result.append("\" language=\"").append(res_lan);
            result.append("\" timestamp=\"").append(res_upd_date); 
            result.append("\" start_time=\"").append(curTime);
            result.append("\" size=\"").append(mod_size).append("\">").append(cwUtils.NEWL);
            result.append("<key>").append(Math.round(Math.random()*99+1)).append("</key>").append(cwUtils.NEWL);
            
            // author's information
            result.append(prof.asXML()).append(cwUtils.NEWL);
            result.append(getModHeader(con, prof)).append(cwUtils.NEWL);
            
            result.append(qList).append(cwUtils.NEWL);
            result.append("</quiz>").append(cwUtils.NEWL); 

            return result.toString(); 
        } catch(qdbException e) {
            throw new cwException(e.toString());
        }

    }
    
    public String getSuqAsXML(Connection con, loginProfile prof, Vector resIdVec, Hashtable queOrder)
        throws cwException , cwSysMessage
    {
        try{
            // Get the list of question from the test; 
            Vector qArray = dbResourceContent.getChildAss(con,mod_res_id);
        
            StringBuffer qList = new StringBuffer();
            dbQuestion dbq = new dbQuestion();
            dbResourceContent rcn = new dbResourceContent();
            long order;
            int i;
            
            mod_size = qArray.size(); 
            
            for (i=0;i < qArray.size();i++) {
                    rcn = (dbResourceContent) qArray.elementAt(i);                                        
                    dbq.que_res_id = rcn.rcn_res_id_content;
                    dbq.res_id = dbq.que_res_id;
                    dbq.get(con);
                    order = rcn.rcn_order;
//                    queOrder.put(new Long(order), dbq);                    
                    qList.append(dbq.asXML(con, order));
                    resIdVec.addElement(new Long(dbq.que_res_id));
            }
//            if (hasRatingQ){
//                qList.append(ViewItemRatingDefination.getCol(con, prof.root_ent_id, ViewItemRatingDefination.Q_COL));
//            }
            return qList.toString(); 
        } catch(qdbException e) {
            throw new cwException(e.toString());
//        } catch(SQLException e) {
//            throw new cwException(e.toString());
        }
    }

   /*
   Default : only get one of the progress result on the test for each user
   */
   public String getSurveyReport(Connection con, loginProfile prof, Timestamp rpt_eff_start_date, Timestamp rpt_eff_end_date, Timestamp course_start_date, Timestamp course_end_date)
            throws cwException, cwSysMessage, SQLException, qdbException
    {
        return (getSurveyReport(con, prof, rpt_eff_start_date, rpt_eff_end_date, course_start_date, course_end_date, true, null));
    }

    /*
   public String getSurveyReport(Connection con, loginProfile prof, Timestamp rpt_eff_start_date, Timestamp rpt_eff_end_date, Timestamp course_start_date, Timestamp course_end_date, boolean distinctUser, long[] ent_id_lst)
            throws cwException, cwSysMessage, SQLException, qdbException
    {
        return getSurveyReport(con, prof, rpt_eff_start_date, rpt_eff_end_date, course_start_date, course_end_date, distinctUser, ent_id_lst, null);
    }
    */

   public String getSurveyReport(Connection con, loginProfile prof, Timestamp rpt_eff_start_date, Timestamp rpt_eff_end_date, Timestamp course_start_date, Timestamp course_end_date, boolean distinctUser, long[] ent_id_lst)
            throws cwException, cwSysMessage, SQLException, qdbException
    {
//        System.out.println("aa");
        StringBuffer xmlBuf = new StringBuffer(); 
        
        xmlBuf.append("<survey id=\"").append(mod_res_id);
        
        if (course_start_date != null){
            xmlBuf.append("\" course_start_date=\"").append(course_start_date);
        }
        if (course_end_date != null){
            xmlBuf.append("\" course_end_date=\"").append(course_end_date);
        }
        xmlBuf.append("\" rpt_eff_start_date=\"").append(rpt_eff_start_date);
        xmlBuf.append("\" rpt_eff_end_date=\"").append(rpt_eff_end_date);
        xmlBuf.append("\">");
//        System.out.println("bb");
        
        long cosId = dbModule.getCosId(con, mod_res_id);
//        System.out.println("cc");

        xmlBuf.append("<course id=\"").append(cosId).append("\" >");        
        xmlBuf.append("<title>").append(cwUtils.esc4XML(dbCourse.getCosTitle(con, cosId))).append("</title>");
        xmlBuf.append("</course>");
//        System.out.println("dd");

        xmlBuf.append(cwUtils.NEWL);

        get(con);
//                System.out.println("ee");

        // get the test header
        xmlBuf.append(getModHeader(con,prof));
//        System.out.println("ff");

            
        Vector resIdVec = new Vector();    
        Hashtable queOrder = new Hashtable();
//                System.out.println("gg");

        xmlBuf.append(getSuqAsXML(con, prof, resIdVec, queOrder)); 
//                System.out.println("hh");

        dbProgress modPgr = new dbProgress();
        modPgr.pgr_res_id = mod_res_id; 
            
        String add_cond = "";
        if (rpt_eff_start_date != null && rpt_eff_end_date != null){
            add_cond += " AND pgr_complete_datetime between '" + rpt_eff_start_date + "' AND '" + rpt_eff_end_date + "' ";
        }
        if (ent_id_lst!=null && ent_id_lst.length>0){
            add_cond += " AND pgr_usr_id IN ( select usr_id from reguser where usr_ent_id in (";
            add_cond += com.cw.wizbank.db.sql.SqlStatements.getUserInGroupSQL(ent_id_lst, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            add_cond += " ))";
        }
        Vector usrIds = new Vector();

        Vector vtProgress = modPgr.getUsrId(con, "DESC", add_cond);
//                System.out.println("jj");

        dbProgress aUsrPgr = null;
        for (int i=0; i<vtProgress.size(); i++){
//                            System.out.println("kk");

            aUsrPgr = (dbProgress) vtProgress.elementAt(i);
            if (!usrIds.contains(aUsrPgr.pgr_usr_id)){
                usrIds.addElement(aUsrPgr.pgr_usr_id);
            }
        }
        // for user info xml
        // for later usage
//                        System.out.println("ll");

        Hashtable htUsrId = dbRegUser.getUsrAndItsGroup(con, usrIds);      
//                        System.out.println("mm");

        Vector traceProgressUsrVec = new Vector();
        
        for (int j=0; j<vtProgress.size();j++) {
//                        System.out.println("nn");
            aUsrPgr = (dbProgress) vtProgress.elementAt(j); 
//                            System.out.println("00");

            // for each user attempted the game
             if (distinctUser && traceProgressUsrVec.contains(aUsrPgr.pgr_usr_id)){
                // skip... nothing to do    
            }else{
//                                System.out.println("pp");

                traceProgressUsrVec.addElement(aUsrPgr.pgr_usr_id);
//                System.out.println("qq");

                dbRegUser usr = (dbRegUser) htUsrId.get(aUsrPgr.pgr_usr_id);
                if (usr!= null){
//                                System.out.println("rr");

                // user info
                    xmlBuf.append("<attempt_usr usr_id=\"").append(usr.usr_id);
                    xmlBuf.append("\" ent_id=\"").append(usr.usr_ent_id).append("\" ");
                    xmlBuf.append("first_name=\"").append(cwUtils.esc4XML(usr.usr_first_name_bil)).append("\" ");
                    xmlBuf.append("last_name=\"").append(cwUtils.esc4XML(usr.usr_last_name_bil)).append("\">").append(cwUtils.esc4XML(usr.usr_display_bil));
                    for (int i=0; usr.usg_display_bil != null && i<usr.usg_display_bil.size(); i++){
//                                        System.out.println("ss");

                        xmlBuf.append("<group name=\"").append(cwUtils.esc4XML((String)usr.usg_display_bil.elementAt(i))).append("\" />").append(dbUtils.NEWL);
                    }
                    
//                                    System.out.println("tt");

                    for(int i=0; i<resIdVec.size(); i++){
//                                        System.out.println("uu");

                        long que_id = ((Long) resIdVec.elementAt(i)).longValue() ;
//                                        System.out.println("vv");

                        if (que_id != 0){
//                                            System.out.println("ww");

                            dbProgressAttempt atm = new dbProgressAttempt();    
                            atm.atm_pgr_usr_id = aUsrPgr.pgr_usr_id;
                            atm.atm_pgr_res_id = mod_res_id;
                            atm.atm_pgr_attempt_nbr = aUsrPgr.pgr_attempt_nbr;
                            atm.atm_int_res_id = que_id;
//                                                        System.out.println("xx");

                            Vector vtAtm = atm.getQattempt(con);    
//                                            System.out.println("yy");

                            if (vtAtm.size()!=0){
//                                                System.out.println("zz");

                                xmlBuf.append("<attempt que_id=\"").append(atm.atm_int_res_id).append("\" >");
                                dbProgressAttempt atm2 = null;
                                for(int n=0; n<vtAtm.size(); n++){
//                                                    System.out.println("AA");

                                    atm2 = (dbProgressAttempt) vtAtm.elementAt(n);
                                    xmlBuf.append("<interaction order=\"").append(atm2.atm_int_order);
                                    xmlBuf.append("\" correct=\"").append(atm2.atm_correct_ind);
                                    xmlBuf.append("\" flag=\"").append(atm2.atm_flag_ind);
                                    xmlBuf.append("\" usr_score=\"").append(atm2.atm_score);
                                    xmlBuf.append("\">").append(dbUtils.NEWL);
//                                                    System.out.println("BB");

                                    // handle Matching response
                                    String[] resps_ = dbUtils.split(atm2.atm_response_bil, dbProgressAttempt.RESPONSE_DELIMITER);
                                    if (resps_ != null && resps_.length  > 0 && resps_[0] != null) {
                                        for (int k=0; k < resps_.length; k++)
                                            xmlBuf.append("<response>").append(cwUtils.esc4XML(resps_[k])).append("</response>").append(dbUtils.NEWL);
                                    }
//                                                    System.out.println("CC");

                                    xmlBuf.append("</interaction>").append(cwUtils.NEWL);
                                }    
                                xmlBuf.append("</attempt>").append(cwUtils.NEWL);
                            }else{
                            }
                        }else{
                        }
                    }
                    xmlBuf.append("</attempt_usr>").append(cwUtils.NEWL);                    
                }
            }
        }        
        xmlBuf.append("</survey>").append(cwUtils.NEWL);
        return xmlBuf.toString(); 
    }


   public String getSurveyReport(Connection con, loginProfile prof, String status) 
    throws cwException, cwSysMessage, SQLException, qdbException {
        return getSurveyReport(con, prof, status, null);
   }

   public String getSurveyReport(Connection con, loginProfile prof, String status, WizbiniLoader wizbini) throws cwException, cwSysMessage, SQLException, qdbException{
	   return getSurveyReport(con, prof, status, wizbini, "");
   }
   
   public String getSurveyReport(Connection con, loginProfile prof, String status, WizbiniLoader wizbini, String tkh_ids)
    throws cwException, cwSysMessage, SQLException, qdbException {

        StringBuffer xmlBuf = new StringBuffer(); 
        
        xmlBuf.append("<survey id=\"").append(mod_res_id);     
        xmlBuf.append("\">");
       
        long cosId = dbModule.getCosId(con, mod_res_id);
        xmlBuf.append("<course id=\"").append(cosId).append("\" >");
        xmlBuf.append("<title>").append(cwUtils.esc4XML(dbCourse.getCosTitle(con, cosId))).append("</title>");
        xmlBuf.append("</course>");
        xmlBuf.append(cwUtils.NEWL);

        get(con);
        // get the test header
        xmlBuf.append(getModHeader(con,prof));

        Vector resIdVec = new Vector();
        Hashtable queOrder = new Hashtable();
        xmlBuf.append(getSuqAsXML(con, prof, resIdVec, queOrder)); 
        dbProgress modPgr = new dbProgress();
        modPgr.pgr_res_id = mod_res_id; 

        String add_cond = null;
        // a list of user with mod_id, out with pgr_attempt_nbr
        Vector usrIds = new Vector();

        Vector vtProgress = modPgr.getAttemptedResult(con, mod_res_id, status, "usr_display_bil", "ASC" , tkh_ids);
        dbProgress aUsrPgr = null;
        for (int i=0; i<vtProgress.size(); i++){
            aUsrPgr = (dbProgress) vtProgress.elementAt(i);
            if (!usrIds.contains(aUsrPgr.pgr_usr_id)){
                usrIds.addElement(aUsrPgr.pgr_usr_id);
            }
        }
        // for user info xml
        // for later usage
        Hashtable htUsrId = dbRegUser.getUsrAndItsGroup(con, usrIds);      
        Vector traceProgressUsrVec = new Vector();
        
        for (int j=0; j<vtProgress.size();j++) {
            aUsrPgr = (dbProgress) vtProgress.elementAt(j); 
            // for each user attempted the game
            //if (!traceProgressUsrVec.contains(aUsrPgr.pgr_usr_id)){
                //traceProgressUsrVec.addElement(aUsrPgr.pgr_usr_id);
                dbRegUser usr = (dbRegUser) htUsrId.get(aUsrPgr.pgr_usr_id);
                if (usr!= null){
                    // user info
                    xmlBuf.append("<attempt_usr usr_id=\"").append(usr.usr_id);
                    xmlBuf.append("\" ent_id=\"").append(usr.usr_ent_id).append("\" ");
                    xmlBuf.append("first_name=\"").append(cwUtils.esc4XML(usr.usr_first_name_bil)).append("\" ");
                    xmlBuf.append("last_name=\"").append(cwUtils.esc4XML(usr.usr_last_name_bil)).append("\">").append(cwUtils.esc4XML(usr.usr_display_bil));
                    for (int i=0; usr.usg_display_bil != null && i<usr.usg_display_bil.size(); i++){
                        xmlBuf.append("<group name=\"").append(cwUtils.esc4XML((String)usr.usg_display_bil.elementAt(i))).append("\" />").append(dbUtils.NEWL);
                    }
                    xmlBuf.append("<full_path>").append(cwUtils.esc4XML(dbEntityRelation.getFullPath(con, usr.usr_ent_id,wizbini))).append("</full_path>");
                    if( aUsrPgr.pgr_res_id != 0 ){
                        for(int i=0; i<resIdVec.size(); i++){
                            long que_id = ((Long) resIdVec.elementAt(i)).longValue() ;
                            if (que_id != 0){
                                dbProgressAttempt atm = new dbProgressAttempt();    
                                atm.atm_pgr_usr_id = aUsrPgr.pgr_usr_id;
                                atm.atm_pgr_res_id = mod_res_id;
                                atm.atm_pgr_attempt_nbr = aUsrPgr.pgr_attempt_nbr;
                                atm.atm_int_res_id = que_id;
                                atm.atm_tkh_id= aUsrPgr.pgr_tkh_id;
                                Vector vtAtm = atm.getQattempt(con);    
                                if (vtAtm.size()!=0){
                                    xmlBuf.append("<attempt que_id=\"").append(atm.atm_int_res_id).append("\" >");
                                    dbProgressAttempt atm2 = null;
                                    for(int n=0; n<vtAtm.size(); n++){
                                        atm2 = (dbProgressAttempt) vtAtm.elementAt(n);
                                        xmlBuf.append("<interaction order=\"").append(atm2.atm_int_order);
                                        xmlBuf.append("\" correct=\"").append(atm2.atm_correct_ind);
                                        xmlBuf.append("\" flag=\"").append(atm2.atm_flag_ind);
                                        xmlBuf.append("\" usr_score=\"").append(atm2.atm_score);
                                        xmlBuf.append("\">").append(dbUtils.NEWL);
                                        // handle Matching response
                                        String[] resps_ = dbUtils.split(atm2.atm_response_bil, dbProgressAttempt.RESPONSE_DELIMITER);
                                        if (resps_ != null && resps_.length  > 0 && resps_[0] != null) {
                                            for (int k=0; k < resps_.length; k++)
                                            	xmlBuf.append("<response id =\""+cwUtils.esc4XML(resps_[k])+"\">").append(cwUtils.esc4XML(resps_[k])).append("</response>").append(dbUtils.NEWL);
                                        }
                                        xmlBuf.append("</interaction>").append(cwUtils.NEWL);
                                    }    
                                    xmlBuf.append("</attempt>").append(cwUtils.NEWL);
                                }
                            }
                        }
                    }
                    xmlBuf.append("</attempt_usr>").append(cwUtils.NEWL);                    
                }
            //}
        }        
        xmlBuf.append("</survey>").append(cwUtils.NEWL);
        return xmlBuf.toString(); 
    }



    // get the evaluation belongs to this item
    // or get all tna
    private final int BYATTEMPT = 1;
    private final int BYASSIGN = 2;
    private final int BYTABLE = 3;
    
    public static int[] calcVectorPos(Vector thisVector, int pageSize, int curPage){
        int[] result = new int[2];
        
        int start = 0;
        int end = 0;
        if (thisVector != null){
            if (curPage == 0 || curPage == 1){
                start = 0;    
            }else{
                start = (curPage - 1) * pageSize;
            }
            end = start + pageSize - 1;
        }
        start = checkVectorSize(thisVector, start);
        end = checkVectorSize(thisVector, end);
        result[0] = start;
        result[1] = end;
        return result;
    }
    
    public static int checkVectorSize(Vector thisVector, int position){
        if (position>=thisVector.size()){
            position = thisVector.size() -1;
        }
//        System.out.println("vector: " + thisVector.size() + "pos:"+ position);
        return position;
    }
    /*
    public static String getItmModLstAsXML(Connection con, long itm_id, long mod_id, String mod_type, Vector vtOrder, int curPage) throws cwException, SQLException{
        int pageSize = 10;           
        String orderSQL = buildOrderSQL(vtOrder);
        
//        int mainOrder = 0;
//        if (vtOrder.size() != 0){
//            mainOrder = BYTABLE;
//        }else if (assOrder != null){
//            mainOrder = BYASSIGN;
//        }else if (attOrder != null){
//            mainOrder = BYATTEMPT;
//        }else{
//            mainOrder = BYTABLE;    
//        }
                    

        Vector modContentLst  = null;
        // to collect userId for find their groups
        Vector usrId = new Vector();
        
        // a vector of dbModule
        System.out.println(orderSQL);
        if (mod_type.equalsIgnoreCase("TNA")){
            modContentLst = ViewSurvey.getItmModLst(con, usrId, orderSQL);    
        }else{
            modContentLst = ViewSurvey.getItmModLst(con, itm_id, mod_id, mod_type, usrId, orderSQL);    
        }
        if (modContentLst.size() == 0){
            return "";    
        }

        Vector resLst = new Vector();
        dbModule myMod = null;

        Hashtable htModContent = new Hashtable();

        int[] startEnd = calcVectorPos(modContentLst, pageSize, curPage);

        for (int i=startEnd[0]; i<=startEnd[1]; i++){
            myMod = (dbModule) modContentLst.elementAt(i);            
            resLst.addElement(new Long(myMod.res_id));
            htModContent.put(new Long(myMod.res_id), myMod);
        }
//        Hashtable htAssignedUsrCnt = new Hashtable();
//        Hashtable htAttemptUsrCnt = new Hashtable();
        
//        Vector vtAssModLst = new Vector();
//        Vector vtAttModLst = new Vector();
        
        // attempt nbr = 0
//        getProgressCnt(con, 0, resLst, htAssignedUsrCnt, vtAssModLst, assOrder);
//        getProgressCnt(con, 1, resLst, htAttemptUsrCnt, vtAttModLst, attOrder);


        Hashtable htAssignedUsrCnt = dbProgress.assignedNum(con, resLst);
        Hashtable htAttemptUsrCnt = dbProgress.attemptNum(con, resLst);


//        Hashtable htAttemptUsrCnt = dbProgress.attemptNum(con, resLst);
        Hashtable htUsrAndItsGroup = dbRegUser.getUsrAndItsGroup(con, usrId);
        Hashtable htLatestDate = Message.getLastestDates(con, resLst);

        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<total_count>").append(modContentLst.size()).append("</total_count>");
        xmlBuf.append("<page_size>").append(pageSize).append("</page_size>");        

        dbRegUser usr = null;
        String tmp_eff_end = null;
        
        for (int i=startEnd[0]; i<=startEnd[1]; i++){
            myMod = (dbModule) modContentLst.elementAt(i);            
            Long Long_mod_id = new Long(myMod.res_id);
            xmlBuf.append("<survey id=\"");
            xmlBuf.append(myMod.res_id);
            xmlBuf.append("\" title=\"");
            xmlBuf.append(cwUtils.esc4XML(myMod.res_title));
            xmlBuf.append("\" eff_start_datetime=\"");
            xmlBuf.append(myMod.mod_eff_start_datetime);
            xmlBuf.append("\" eff_end_datetime=\"");
            
            //check if the end_datetime need to be converted to "UNLIMITED"
            if(myMod.mod_eff_end_datetime != null){
                if(dbUtils.isMaxTimestamp(myMod.mod_eff_end_datetime) == true){
                    tmp_eff_end = cwUtils.UNLIMITED; //convert to String to "UNLIMITED"
                }
                else{
                    tmp_eff_end = myMod.mod_eff_end_datetime.toString();
                }
            }
            xmlBuf.append(tmp_eff_end);
            xmlBuf.append("\" assign_cnt=\"");
            xmlBuf.append(htAssignedUsrCnt.get(Long_mod_id));
            xmlBuf.append("\" attempt_cnt=\"");
            xmlBuf.append(htAttemptUsrCnt.get(Long_mod_id));
            xmlBuf.append("\" last_notify_date=\"");
            xmlBuf.append(htLatestDate.get(Long_mod_id));
            xmlBuf.append("\" >");
            xmlBuf.append("<owner>");
            
            usr = (dbRegUser) htUsrAndItsGroup.get(myMod.res_usr_id_owner);
            xmlBuf.append(usr.usrAndItsGroupAsXML());
            
            xmlBuf.append("</owner>");
            xmlBuf.append("</survey>");
        }
                
        return xmlBuf.toString();
    }
    */
    /*
    public static String getSurveyLstXML(int mainOrder, Hashtable htModContent, Vector modContentLst, Hashtable htAssignedUsrCnt, Vector vtAssModLst, Hashtable htAttemptUsrCnt, Vector vtAttModLst){
        dbModule myMod = null;
        CntOrder myCntOrder = null;
        for (int i=0; i<modContentLst.size(); i++){
            if (mainOrder == BYTABLE){
                myMod = (dbModule) modContentLst.elementAt(i);            
            }else if(mainOrder == BYATTEMPT) {
                myCntOrder = (CntOrder) vtAssModLst.elementAt(); 
                myMod = (dbModule) modContentLst.elementAt(i);            
            }            vtAssModLst
            
            xmlBuf.append("<survey id=\"");
            xmlBuf.append(myMod.res_id);
            xmlBuf.append("\" title=\"");
            xmlBuf.append(myMod.res_title);
            xmlBuf.append("\" eff_start_datetime=\"");
            xmlBuf.append(myMod.mod_eff_start_datetime);
            xmlBuf.append("\" eff_end_datetime=\"");
            
            //check if the end_datetime need to be converted to "UNLIMITED"
            if(myMod.mod_eff_end_datetime != null){
                if(dbUtils.isMaxTimestamp(myMod.mod_eff_end_datetime) == true){
                    tmp_eff_end = cwUtils.UNLIMITED; //convert to String to "UNLIMITED"
                }
                else{
                    tmp_eff_end = myMod.mod_eff_end_datetime.toString();
                }
            }
            xmlBuf.append(tmp_eff_end);
            xmlBuf.append("\" assign_cnt=\"");
            xmlBuf.append(htAssignedUsrCnt.get(Long_mod_id));
            xmlBuf.append("\" attempt_cnt=\"");
            xmlBuf.append(htAttemptUsrCnt.get(Long_mod_id));
            xmlBuf.append("\" last_notify_date=\"");
            xmlBuf.append(htLatestDate.get(Long_mod_id));
            xmlBuf.append("\" >");
            xmlBuf.append("<owner>");
            
            usr = (dbRegUser) htUsrAndItsGroup.get(myMod.res_usr_id_owner);
            xmlBuf.append(usr.usrAndItsGroupAsXML());
            
            xmlBuf.append("</owner>");
            xmlBuf.append("</survey>");
        }
    }
    */
    /*
    public static String ItmRateAsXML(Connection con, long itm_id, long mod_id) throws qdbException, cwSysMessage, cwException {
        try{
            StringBuffer xmlBuf = new StringBuffer();
//            aeItem cos = new aeItem();
            
//            cos.itm_id = itm_id;
//            cos.getItem(con);
            
//            String cosXML = cos.contentAsXML(con);
//            String targetXML = getCosRatingAsXML(con, itm_id, ViewMoteRpt.TARGET_OVERALL_RATING);
//            String actualXML = getCosRatingAsXML(con, itm_id, ViewMoteRpt.ACTUAL_OVERALL_RATING);

//            xmlBuf.append(cosXML).append(cwUtils.NEWL);
//            xmlBuf.append("<run_lst>").append(cwUtils.NEWL);
//            xmlBuf.append(getRunLstAsXML(con, itm_id)).append(cwUtils.NEWL);
//            xmlBuf.append("</run_lst>").append(cwUtils.NEWL);
//            xmlBuf.append(targetXML).append(cwUtils.NEWL);
//            xmlBuf.append(actualXML).append(cwUtils.NEWL);

            xmlBuf.append(getItmModLstAsXML(con, itm_id, mod_id, dbModule.MOD_TYPE_EVN, null, 0));

            return cwUtils.escNull(xmlBuf.toString());   
        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
        }                  
    }
*/


    //Get the XML to insert a TNA notify
    public static String insNotifyXML(Connection con, HttpSession sess, long mod_id, boolean refresh)
        throws SQLException, cwException {

                StringBuffer xml        = new StringBuffer();
                String msg_method       = null;
                String msg_subject      = null;
                String msg_body         = null;
                String msg_rec_ent_id   = null;
                String msg_cc_ent_id    = null;
                //Timestamp msg_sendtime  = null;
                String dd               = null;
                String yy               = null;
                String mm               = null;
                
                Vector methodVec        = new Vector();
                Vector pickedRecVec     = new Vector();
                Vector pickedCcVec      = new Vector();
                if(refresh) {   //refresh value in the session
                    sess.setAttribute("Survey_MSG_METHOD", "");
                    sess.setAttribute("Survey_MSG_SUBJECT", "");
                    msg_subject = "";           //  prevent show null in xml
                    sess.setAttribute("Survey_MSG_BODY", "");
                    msg_body = "";              //  prevent show null in xml
                    mm = "";
                    dd = "";
                    yy = "";
                    //sess.setAttribute("Survey_MSG_SENDTIME", "");
                    sess.setAttribute("Survey_MSG_TIME_MM", "");
                    sess.setAttribute("Survey_MSG_TIME_DD", "");
                    sess.setAttribute("Survey_MSG_TIME_YY", "");
                    sess.setAttribute("Survey_MSG_REC_ENT_ID", "");
                    sess.setAttribute("Survey_MSG_CC_ENT_ID", "");
                    sess.setAttribute("Survey_MSG_MOD_ID", new Long(mod_id));
                } else {        // get value from session
                    msg_method      = (String)sess.getAttribute("Survey_MSG_METHOD");
                    msg_subject     = (String)sess.getAttribute("Survey_MSG_SUBJECT");
                    msg_body        = (String)sess.getAttribute("Survey_MSG_BODY");
                    msg_rec_ent_id  = (String)sess.getAttribute("Survey_MSG_REC_ENT_ID");
                    msg_cc_ent_id   = (String)sess.getAttribute("Survey_MSG_CC_ENT_ID");
                    dd              = (String)sess.getAttribute("Survey_MSG_TIME_DD");
                    yy              = (String)sess.getAttribute("Survey_MSG_TIME_DD");
                    mm              = (String)sess.getAttribute("Survey_MSG_TIME_MM");
                    methodVec       = dbUtils.convert2Vec(msg_method+"~", "~");
                    pickedRecVec    = dbUtils.convert2Vec(msg_rec_ent_id+"~", "~");
                    pickedCcVec     = dbUtils.convert2Vec(msg_cc_ent_id+"~", "~");
                    
                    /*
                    try{
                        msg_sendtime    = (Timestamp)sess.getAttribute("Survey_MSG_SENDTIME");
                    }catch(ClassCastException e) {
                        msg_sendtime    = null;
                    }
                    */
                }
                mod_id = ((Long)sess.getAttribute("Survey_MSG_MOD_ID")).longValue();
            // construct xml for frontend to insert a TNA notify            
            xml.append("<module id=\"").append(mod_id).append("\">").append(dbUtils.NEWL);
            xml.append("<notify dd=\"").append(dd).append("\" mm=\"").append(mm).append("\" yy=\"").append(yy).append("\">").append(dbUtils.NEWL);
            xml.append("<subject>").append(dbUtils.esc4XML(msg_subject)).append("</subject>").append(dbUtils.NEWL);            
            xml.append("<content>").append(dbUtils.esc4XML(msg_body)).append("</content>").append(dbUtils.NEWL);
            xml.append("<method notes=\"");
            if( methodVec.indexOf("notes") == -1 )
                xml.append("no");
            else
                xml.append("yes");
                
            xml.append("\" etray=\"");
            if( methodVec.indexOf("etray") == -1 )
                xml.append("no");
            else
                xml.append("yes");
            xml.append("\"/>").append(dbUtils.NEWL);
            xml.append("</notify>").append(dbUtils.NEWL);
            xml.append("<picked_entity>").append(dbUtils.NEWL);
                xml.append(dbUserGroup.getEntityXml(con, pickedRecVec));
            xml.append("</picked_entity>").append(dbUtils.NEWL);
            xml.append("<picked_cc>").append(dbUtils.NEWL);
                xml.append(dbUserGroup.getEntityXml(con, pickedCcVec));
            xml.append("</picked_cc>").append(dbUtils.NEWL);            
            xml.append("</module>");
                
            return xml.toString();
        }
    
    // construct xml of the entity id list
    public static String pickEntityList(Connection con, HttpSession sess, loginProfile prof,  String type, int cur_page, int pagesize, Timestamp timestamp, long usg_ent_id)
        throws SQLException, cwException {
            /*
            // put value into session
            if( method == null ) method = "";
            sess.setAttribute("Survey_MSG_METHOD", method);
            
            if( subject == null )   subject ="";
            sess.setAttribute("Survey_MSG_SUBJECT", subject);
            
            if( body == null ) body ="";
            sess.setAttribute("Survey_MSG_BODY", body);
            
            if( send_time == null )
                sess.setAttribute("Survey_MSG_SENDTIME", "");
            else
                sess.setAttribute("Survey_MSG_SENDTIME", send_time);
            */
            String rec_ent_id_str;
            if( type.equalsIgnoreCase("REC") )
                rec_ent_id_str = (String)sess.getAttribute("Survey_MSG_REC_ENT_ID");
            else
                rec_ent_id_str = (String)sess.getAttribute("Survey_MSG_CC_ENT_ID");
            
            Vector pickedVec = new Vector();
            if( rec_ent_id_str != null && (rec_ent_id_str.trim()).length() > 0 )
                pickedVec = dbUtils.convert2Vec(rec_ent_id_str+"~","~");
            
            dbUserGroup dbUsg = new dbUserGroup();
            StringBuffer result = new StringBuffer();            
            try{
                dbUsg.usg_ent_id = usg_ent_id;
                result.append(dbUsg.getMemberListXML(con, sess, prof, cur_page, pagesize, timestamp));
            }catch( qdbException e) {
                throw new cwException("Failed to get the entity id list : " + e);
            }

            // attach the picked entity id list into the all entity id list xml
            result.append(dbUtils.NEWL).append("<picked_entity type=\"").append(type).append("\">").append(dbUtils.NEWL);
            for(int i=0; i<pickedVec.size(); i++)
                result.append("<entity ent_id=\"").append(pickedVec.elementAt(i)).append("\"/>").append(dbUtils.NEWL);        
            result.append("</picked_entity>").append(dbUtils.NEWL);

                
            return result.toString();
        }

        
    // construct xml of the picked entity id list
    public static String pickedEntityList(Connection con, HttpSession sess, loginProfile prof, String subject, String body, String method, Timestamp send_time, String type)
        throws SQLException, cwException {
            // put value into session
            if( method == null ) method = "";
            sess.setAttribute("Survey_MSG_METHOD", method);
            
            if( subject == null )   subject ="";
            sess.setAttribute("Survey_MSG_SUBJECT", subject);
            
            if( body == null ) body ="";
            sess.setAttribute("Survey_MSG_BODY", body);
            
            if( send_time == null )
                sess.setAttribute("Survey_MSG_SENDTIME", "");
            else
                sess.setAttribute("Survey_MSG_SENDTIME", send_time);
            
            String rec_ent_id_str;
            if( type.equalsIgnoreCase("REC") )
                rec_ent_id_str = (String)sess.getAttribute("Survey_MSG_REC_ENT_ID");
            else
                rec_ent_id_str = (String)sess.getAttribute("Survey_MSG_CC_ENT_ID");
            Vector pickedVec = new Vector();
            if( rec_ent_id_str != null && (rec_ent_id_str.trim()).length() > 0 )
                pickedVec = dbUtils.convert2Vec(rec_ent_id_str+"~","~");
            
            StringBuffer xml = new StringBuffer();
            xml.append("<picked_entity type=\"").append(type).append("\">").append(dbUtils.NEWL);
                xml.append(dbUserGroup.getEntityXml(con, pickedVec));
            xml.append("</picked_entity>").append(dbUtils.NEWL);

            return xml.toString();
        }
        
    public static String getCleanItmModLstAsXML(Connection con, long itm_id, long mod_id, String mod_type) throws cwException, SQLException{
        Vector modLst  = null;
        Vector usrId = new Vector();

        modLst = ViewSurvey.getItmModLst(con, itm_id, mod_id, mod_type, usrId, null);    
        
        if (modLst.size() == 0){
            return "";    
        }

        Vector resLst = new Vector();
        dbModule myMod = null;

        for (int i=0; i<modLst.size(); i++){
            myMod = (dbModule) modLst.elementAt(i);            
            resLst.addElement(new Long(myMod.res_id));
        }

        StringBuffer xmlBuf = new StringBuffer();
        String tmp_eff_end = null;
        xmlBuf.append("<survey_list>");
        for (int i=0; i<modLst.size(); i++){
            myMod = (dbModule) modLst.elementAt(i);            
            xmlBuf.append("<survey id=\"");
            xmlBuf.append(myMod.res_id);
            xmlBuf.append("\" title=\"");
            xmlBuf.append(cwUtils.esc4XML(myMod.res_title));
            xmlBuf.append("\" eff_start_datetime=\"");
            xmlBuf.append(myMod.mod_eff_start_datetime);
            xmlBuf.append("\" eff_end_datetime=\"");
            
            //check if the end_datetime need to be converted to "UNLIMITED"
            if(myMod.mod_eff_end_datetime != null){
                if(dbUtils.isMaxTimestamp(myMod.mod_eff_end_datetime) == true){
                    tmp_eff_end = cwUtils.UNLIMITED; //convert to String to "UNLIMITED"
                }
                else{
                    tmp_eff_end = myMod.mod_eff_end_datetime.toString();
                }
            }
            xmlBuf.append(tmp_eff_end);
            xmlBuf.append("\" />");
        }
        xmlBuf.append("</survey_list>");
                
        return xmlBuf.toString();
    }
/*
    public class CntObj{
            long mod_id;
            int count;
    }
  */  
    /*
    public static void getProgressCnt(Connection con, int pgr_attempt_nbr, Vector resLst, Hashtable htResId, Vector vtModLst, String cntOrder)
        throws cwException
    {
        try {
            String sqlCond = "";
            if (pgr_attempt_nbr > 0){
                sqlCond = " AND pgr_attempt_nbr > 0 ";
            }
            
            String tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, vtResId, cwSQL.COL_TYPE_LONG);
            
            PreparedStatement stmt = con.prepareStatement(
                  " SELECT pgr_res_id res_id, count(distinct pgr_usr_id) usr_count FROM Progress " 
                + "   WHERE pgr_res_id IN ( SELECT tmp_res_id FROM " +  tableName + " ) " 
                + sqlCond
                + " GROUP BY pgr_res_id "
                + " ORDER BY usr_count" + cntOrder);

            ResultSet rs = stmt.executeQuery();
            CntObj myCntObj = null;

            while (rs.next()){
                htResId.put(new Long(rs.getLong("res_id")), new Integer(rs.getInt("usr_count")));
                myCntObj = new CntObj();
                myCntObj.mod_id = rs.getLong("res_id");
                myCntObj.count = rs.getInt("usr_count");
                vtModLst.addElement(myCntObj);
            }
            stmt.close();
            
        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
        } 
    }
*/

    public Vector getUsrId(Connection con, String usrOrder) 
        throws qdbException
    {   
        try {
            String sqlOrder = "";
            if (usrOrder != null){
                sqlOrder = " order by usr_display_bil " + usrOrder + " pgr_usr_id, pgr_attempt_nbr DESC "; 
            }else{
                sqlOrder = " order by pgr_complete_datetime DESC, pgr_usr_id, pgr_attempt_nbr DESC "; 
            }
            PreparedStatement stmt = con.prepareStatement(
            " SELECT "
            + " usr_display_bil "
            + " , pgr_usr_id "
            + " , pgr_res_id " 
            + " , pgr_attempt_nbr " 
            + " , pgr_score "
            + " , pgr_rank_bil "
            + " , pgr_schedule_datetime "
            + " , pgr_complete_datetime "
            + " , pgr_last_acc_datetime "
            + " , pgr_start_datetime "
            + " , pgr_status "
            +   " from Progress , Reguser "
            +   " where pgr_usr_id = usr_id and pgr_res_id = ? " 
            + sqlOrder); 
            
            // set the values for prepared statements
            stmt.setLong(1, mod_res_id);
            
            Vector usrLst = new Vector();
            
            ResultSet rs = stmt.executeQuery();
            dbProgress pgr = null;
            while (rs.next())
            {
                pgr = new dbProgress();
                pgr.pgr_usr_id = rs.getString("pgr_usr_id");
                pgr.pgr_res_id = rs.getLong("pgr_res_id");
                pgr.pgr_attempt_nbr = rs.getLong("pgr_attempt_nbr");
                pgr.pgr_score = rs.getFloat("pgr_score");
                pgr.pgr_rank_bil = rs.getString("pgr_rank_bil");
                pgr.pgr_schedule_datetime = rs.getTimestamp("pgr_schedule_datetime");
                pgr.pgr_complete_datetime = rs.getTimestamp("pgr_complete_datetime");
                pgr.pgr_last_acc_datetime = rs.getTimestamp("pgr_last_acc_datetime");
                pgr.pgr_start_datetime = rs.getTimestamp("pgr_start_datetime");
                pgr.pgr_status = rs.getString("pgr_status");
                
                usrLst.addElement(pgr);
            }
            
            stmt.close();
            return usrLst; 
                    
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
        
    }


/*        
    public static String getRunLstAsXML(Connection con, long itm_id) throws cwException, SQLException{
        Vector runLst = ViewSurvey.getRunLstEvn(con, itm_id);        
        aeItem myItem = null;
        StringBuffer xmlBuf = new StringBuffer();
        
        for (int i=0; i<runLst.size(); i++){
            myItem = (aeItem) runLst.elementAt(i);            
            xmlBuf.append("<item itm_id=\"");
            xmlBuf.append(myItem.itm_id);
            xmlBuf.append("\" itm_title=\"");
            xmlBuf.append(myItem.itm_title);
            xmlBuf.append("\" evn_count=\"");
//            xmlBuf.append(myItem.count);
            xmlBuf.append("\" />");
        }
                
        return xmlBuf.toString();
    }
  */      
}

