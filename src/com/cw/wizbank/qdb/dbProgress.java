package com.cw.wizbank.qdb;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cwn.wizbank.utils.CommonLog;

public class dbProgress
{
    
    public static final String MOD_TYPE_DXT     = "DXT";
    public static final String MOD_TYPE_STX     = "STX";
    
    public static final String PGR_STATUS_NOT_GRADED = "NOT GRADED";
    public static final String PGR_STATUS_GRADED = "GRADED";
    public static final String PGR_STATUS_OK = "OK";
    public static final String PGR_STATUS_PENDING = "PENDING";      // SAVED RECORD
    public static final String NEW_ATTEMPT_SUBMITTED_MSG = "PGR010"; 
    public static final String ESSAY_GRADING_MSG = "PGR011"; 
    public static final String COMPLETION_STATUS_PASSED = "P";
    public static final String COMPLETION_STATUS_FAILED = "F";
    public static final String COMPLETION_STATUS_NOT_GRADED = "N";
    public static final String COMPLETION_STATUS_IN_PROGRESS = "I";



    public String       pgr_usr_id;
    public long         pgr_res_id;
    public long         pgr_attempt_nbr;
    public float        pgr_score;
    public String       pgr_grade;
    public float        pgr_max_score;
    public String       pgr_rank_bil;
    public Timestamp    pgr_schedule_datetime;
    public Timestamp    pgr_start_datetime;
    public Timestamp    pgr_complete_datetime;
    public Timestamp    pgr_last_acc_datetime;
    public String       pgr_status;
    public long         pgr_tkh_id;
    public String       pgr_completion_status;
    public long 		pgr_correct_cnt;
    public long 		pgr_incorrect_cnt;
    public long 		pgr_not_score_cnt;
    
    public dbProgress() {;}
    
    // get the test report for a specified attempt number
    public void get(Connection con, long attemptNbr) 
        throws qdbException, qdbErrMessage
    {   
        try {
            pgr_attempt_nbr = attemptNbr;
            
            PreparedStatement stmt = con.prepareStatement(
            " SELECT "
            + "   pgr_score "
            + " , pgr_grade "
            + " , pgr_max_score "
            + " , pgr_rank_bil "
            + " , pgr_schedule_datetime "
            + " , pgr_start_datetime "
            + " , pgr_complete_datetime "
            + " , pgr_last_acc_datetime "
            + " , pgr_status "
            + " , pgr_completion_status "
            +   " from Progress "
            +   " where pgr_usr_id =? and pgr_res_id = ?  "
            +   "      and pgr_attempt_nbr = ? and pgr_tkh_id = ? "); 
            
            // set the values for prepared statements
            stmt.setString(1, pgr_usr_id);
            stmt.setLong(2, pgr_res_id);
            stmt.setLong(3, pgr_attempt_nbr);
            stmt.setLong(4, pgr_tkh_id);
CommonLog.debug("pgr_usr_id = " + pgr_usr_id);
CommonLog.debug("pgr_res_id = " + pgr_res_id);
CommonLog.debug("pgr_attempt_nbr = " + pgr_attempt_nbr);
CommonLog.debug("pgr_tkh_id = " + pgr_tkh_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                pgr_score = rs.getFloat("pgr_score");
                pgr_grade = rs.getString("pgr_grade");
                pgr_max_score = rs.getFloat("pgr_max_score");
                pgr_rank_bil = rs.getString("pgr_rank_bil");
                pgr_schedule_datetime = rs.getTimestamp("pgr_schedule_datetime");
                pgr_complete_datetime = rs.getTimestamp("pgr_complete_datetime");
                pgr_last_acc_datetime = rs.getTimestamp("pgr_last_acc_datetime");
                pgr_start_datetime = rs.getTimestamp("pgr_start_datetime");
                pgr_status = rs.getString("pgr_status");
                pgr_completion_status = rs.getString("pgr_completion_status");
            }
            else
            {
                stmt.close();
                /*
                 * 去掉抛出异常 fix bug Bug 10990 - 课程管理，进入网上课程，学习管理结果，测试结果，点击静态测试的“查看提交情况”按钮，页面提示“server error:null” 
                 */
               // throw new qdbErrMessage("PGR003");
            }
            
            stmt.close();        
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
        
    }
    
    public static long getLastAttemptNbr(Connection con, long res_id, long tkh_id)
        throws qdbErrMessage, SQLException{
            long attempt_nbr;
            PreparedStatement stmt = con.prepareStatement(
            " SELECT MAX(pgr_attempt_nbr) "
            +   " from Progress "
            +   " where pgr_res_id = ? and pgr_tkh_id = ? " ); 
            
            // set the values for prepared statements
            stmt.setLong(1, res_id);
            stmt.setLong(2, tkh_id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                attempt_nbr = rs.getLong(1);
            }
            else
            {
                stmt.close();
                throw new qdbErrMessage("PGR003");
            }
            stmt.close();        
            return attempt_nbr;
    }
    
    // get the lastest test report
    public void get(Connection con) 
        throws qdbException, qdbErrMessage
    {   
        try {
                pgr_attempt_nbr = getLastAttemptNbr(con, pgr_res_id, pgr_tkh_id);
                get(con,pgr_attempt_nbr);
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }                
    }    

    // return the status of the assignment for a specified attempt number
    public String getStatus(Connection con, long attemptNbr) 
        throws qdbException, qdbErrMessage
    {   
        try {
            pgr_attempt_nbr = attemptNbr;
            
            PreparedStatement stmt = con.prepareStatement(
            " SELECT "
            + " pgr_status, pgr_completion_status "
            +   " from Progress "
            +   " where pgr_usr_id =? and pgr_res_id = ?  "
            +   "      and pgr_attempt_nbr = ? and pgr_tkh_id = ? "); 
            
            // set the values for prepared statements
            stmt.setString(1, pgr_usr_id);
            stmt.setLong(2, pgr_res_id);
            stmt.setLong(3, pgr_attempt_nbr);
            stmt.setLong(4, pgr_tkh_id);
            
            ResultSet rs = stmt.executeQuery();
            String pgrStatus = null;
            if (rs.next()){
            	pgrStatus = rs.getString("pgr_status");
            	pgr_completion_status = rs.getString("pgr_completion_status");
            }
            stmt.close();
            return pgrStatus;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
        
    }    
    
    public void clearScore(Connection con)
        throws qdbException
    {
        try {
             PreparedStatement stmt = con.prepareStatement(
                  " DELETE From ProgressAttempt WHERE "
                + "     atm_pgr_usr_id = ? "
                + "   AND atm_pgr_res_id = ? ");

             stmt.setString(1,pgr_usr_id); 
             stmt.setLong(2,pgr_res_id); 
             stmt.executeUpdate(); 

             stmt =  con.prepareStatement(
                 " DELETE From Progress WHERE "
                + "     pgr_usr_id = ? "
                + "   AND pgr_res_id  = ? ");
             
             stmt.setString(3,pgr_usr_id); 
             stmt.setLong(4,pgr_res_id); 
             stmt.executeUpdate(); 
             stmt.close();
             
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        } 
    }
    
    public static long totalAttemptNum(Connection con, long modId)
        throws qdbException
    {
        try {
             PreparedStatement stmt = con.prepareStatement(
                  " SELECT count(pgr_res_id) FROM Progress " 
                + "   WHERE pgr_res_id = ? ");
            
            stmt.setLong(1,modId);
            ResultSet rs = stmt.executeQuery();
            long cnt = 0;
            if (rs.next()) {
                cnt = rs.getLong(1);
            }else {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to get progress info.");
            }
            
            stmt.close();
            return cnt;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        } 
    }
    
    public static Hashtable totalAttemptNum(Connection con, Vector vtModId)
        throws SQLException
    {
            Hashtable htAttemptNum = new Hashtable();
             PreparedStatement stmt = con.prepareStatement(
             
                  " SELECT count(*) As attempt_cnt, pgr_res_id  FROM Progress "
                    + " where pgr_res_id  in " 
                    + cwUtils.vector2list(vtModId)
                    + " group by pgr_res_id  ");
            
            ResultSet rs = stmt.executeQuery();
//            long cnt = 0;
            while (rs.next()) {
                Long cnt = new Long(rs.getLong("attempt_cnt"));
                Long modId = new Long(rs.getLong("pgr_res_id"));   
                htAttemptNum.put(modId, cnt);
            }
            
            stmt.close();
            return htAttemptNum;
    }
    /*
    public static Hashtable attemptNum(Connection con, Vector vtResId)
        throws cwException
    {
        try {
            String tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, vtResId, cwSQL.COL_TYPE_LONG);

            PreparedStatement stmt = con.prepareStatement(
                  " SELECT pgr_res_id res_id, count(distinct pgr_usr_id) usr_count FROM Progress " 
                + "   WHERE pgr_attempt_nbr != 0 and pgr_res_id IN ( SELECT tmp_res_id FROM " +  tableName + " ) " 
                + " GROUP BY pgr_res_id ");

            ResultSet rs = stmt.executeQuery();
            Hashtable htResId = new Hashtable();
            while (rs.next()){
                htResId.put(new Long(rs.getLong("res_id")), new Integer(rs.getInt("usr_count")));
            }
            stmt.close();
            return htResId;
            
        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
        } 
    }
*/
        // retired method
/*
    public static Hashtable assignedNum(Connection con, Vector vtResId)
        throws cwException  , SQLException 
    {
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
        try {
            cwSQL.insertSimpleTempTable(con, tableName, vtResId, cwSQL.COL_TYPE_LONG);

            PreparedStatement stmt = con.prepareStatement(
                  " SELECT pgr_res_id res_id, count(distinct pgr_usr_id) usr_count FROM Progress " 
                + "   WHERE pgr_res_id IN ( SELECT tmp_res_id FROM " +  tableName + " ) " 
                + " GROUP BY pgr_res_id ");

            ResultSet rs = stmt.executeQuery();
            Hashtable htResId = new Hashtable();
            while (rs.next()){
                htResId.put(new Long(rs.getLong("res_id")), new Integer(rs.getInt("usr_count")));
            }
            stmt.close();
            return htResId;
            
        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
        } finally{
            cwSQL.dropTempTable(con, tableName);
        }
    }
*/
    public static long attemptNum(Connection con, long modId)
        throws qdbException
    {
        try {
             PreparedStatement stmt = con.prepareStatement(
                  " SELECT count(distinct pgr_usr_id) FROM Progress " 
                + "   WHERE pgr_res_id = ? " );
            
            stmt.setLong(1,modId);
            ResultSet rs = stmt.executeQuery(); 
            long cnt = 0;
            if (rs.next()) {
                cnt = rs.getLong(1); 
            }else {
                stmt.close();
                con.rollback(); 
                throw new qdbException("Failed to get progress info."); 
            }
             
            stmt.close();
            return cnt;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        } 
    }

    public static Hashtable usrAttemptNum(Connection con, Vector modVec, String usrId)
        throws qdbException
    {
        try {
            Hashtable modAttempNumHash = new Hashtable();
            if (modVec == null || modVec.size() ==0) {
                return modAttempNumHash;
            }
            
            PreparedStatement stmt = con.prepareStatement(
                " SELECT max(pgr_attempt_nbr) AS CNT, pgr_res_id FROM Progress " 
            + "   WHERE pgr_usr_id = ? " 
            + "    AND  pgr_res_id IN " + cwUtils.vector2list(modVec)
            + "   GROUP BY pgr_res_id "); 
            
            stmt.setString(1,usrId); 
            
            ResultSet rs = stmt.executeQuery(); 
            while (rs.next()) {
                modAttempNumHash.put(new Long(rs.getLong("pgr_res_id")), new Long(rs.getLong("CNT")));
            }
            stmt.close();
            return modAttempNumHash;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        } 
    }
    
    public static long usrAttemptNum(Connection con, long modId, String usrId) throws qdbException{
        try{
            long tkhId = DbTrackingHistory.getAppTrackingIDByMod(con, modId, dbRegUser.getEntId(con, usrId));
            return usrAttemptNum(con, modId, usrId, tkhId);
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        } catch(cwException e) {
            throw new qdbException("CW Error: " + e.getMessage()); 
        } 

    }
    
    public static long usrAttemptNum(Connection con, long modId, String usrId, long tkhId)
        throws qdbException
    {
        try {
//            System.out.println("modified method usrAttemptNum");
//            System.out.println("!!!!!!get tracking id in static dbProgress.usrAttemptNum(Connection con, long modId, String usrId)");
//            long tkhId = DbTrackingHistory.getAppTrackingIDByMod(con, modId, dbRegUser.getEntId(con, usrId));

             PreparedStatement stmt = con.prepareStatement(
                  " SELECT max(pgr_attempt_nbr) FROM Progress " 
                + "   WHERE pgr_res_id = ? " 
                + "    AND  pgr_usr_id = ? "
                + "    AND  pgr_tkh_id = ? "
                + "   GROUP BY pgr_attempt_nbr");
            
            stmt.setLong(1,modId);
            stmt.setString(2,usrId); 
            stmt.setLong(3,tkhId);
            
            ResultSet rs = stmt.executeQuery(); 
            long cnt=0;
            if (rs.next()) {
               cnt = rs.getLong(1); 
            }else {
//                con.rollback(); 
//                throw new qdbException("Failed to get progress info."); 
            }
            stmt.close();
            return cnt;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    // Should be control by pagination and the size of usridVec is limited by pagesize
    public static String getResultGrpXML(Connection con, long modId, Vector usrIdVec)
        throws qdbException
    {
        try {
            String usr_xml = ""; 
            usr_xml = "<student_report mod_id=\"" + modId + "\">"; 
            
            // ** TEMP TABLE
//            String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_id", cwSQL.COL_TYPE_STRING, 30);
//            cwSQL.insertSimpleTempTable(con, tableName, usrIdVec, cwSQL.COL_TYPE_STRING);

            StringBuffer strBuf = new StringBuffer();

            strBuf.append("('0'");
            for (int i=0;i<usrIdVec.size();i++) {
                strBuf.append(", '").append(((String) usrIdVec.elementAt(i)).toString()).append("'");
            }
            strBuf.append(")");

            PreparedStatement stmt = con.prepareStatement(
            " SELECT "
            + "   pgr_usr_id "
            + " , pgr_res_id " 
            + " , pgr_attempt_nbr " 
            + " , pgr_score "
            + " , pgr_rank_bil "
            + " , pgr_schedule_datetime "
            + " , pgr_complete_datetime "
            + " , pgr_last_acc_datetime "
            + " , pgr_start_datetime "
            + " , pgr_status "
            +   " from Progress " 
            +   " where pgr_res_id = ?  "
            +   "     AND pgr_usr_id IN " + strBuf.toString() 
            +   "        order by pgr_usr_id, pgr_attempt_nbr ASC "); 
            
            // set the values for prepared statements
            stmt.setLong(1, modId);
            dbProgress pgr = new dbProgress(); 
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
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
                
                usr_xml += "<student id=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con, pgr.pgr_usr_id)) + "\">";  
                usr_xml += dbUtils.NEWL;
                    
                usr_xml += "<attempt number=\"" + pgr.pgr_attempt_nbr + "\" score=\"" ;
                usr_xml += pgr.pgr_score + "\" schedule_date=\"" + pgr.pgr_schedule_datetime ; 
                usr_xml += "\" complete_date=\"" + pgr.pgr_complete_datetime ;
                usr_xml += "\" start_date=\"" + pgr.pgr_start_datetime ;
                usr_xml += "\" last_acc_date=\"" + pgr.pgr_last_acc_datetime ;
                usr_xml += "\" status=\"" + pgr.pgr_status + "\">" ;
                usr_xml += "</attempt>" + dbUtils.NEWL;
                    
                usr_xml += "</student>" + dbUtils.NEWL;
            }
            
            usr_xml += "</student_report>"; 
            stmt.close();

//            cwSQL.dropTempTable(con, tableName);

            return usr_xml ; 
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
    
    public String rptUserAsXML(Connection con, String[] que_id_lst, loginProfile prof, String metaXML, qdbEnv static_env)
        throws qdbException, cwSysMessage, qdbErrMessage
    {
        try {
            StringBuffer result = new StringBuffer(1024); 
            dbRegUser  user = new dbRegUser();
            user.usr_id = pgr_usr_id;
            user.usr_ent_id = user.getEntId(con);
            user.get(con);

            result.append(dbUtils.xmlHeader);
            result.append("<student_report>").append(dbUtils.NEWL);
            result.append("<res_dir_url>").append(cwUtils.getFileURL(qdbAction.static_env.INI_RES_DIR_URL)).append("</res_dir_url>").append(dbUtils.NEWL);
            
            result.append(prof.asXML()).append(dbUtils.NEWL);
            if (metaXML !=null){
                result.append(metaXML).append(dbUtils.NEWL);
            }
            
            result.append(attemptInfoAsXML(con, pgr_usr_id)); 

            result.append("<student id=\"").append(user.usr_ste_usr_id).append("\" last_name=\"") ;
            result.append(dbUtils.esc4XML(user.usr_last_name_bil)).append("\" first_name=\"") ;
            result.append(dbUtils.esc4XML(user.usr_first_name_bil)).append("\" display_bil=\"") ;
            result.append(dbUtils.esc4XML(user.usr_display_bil)).append("\" email=\"").append(cwUtils.esc4XML(user.usr_email)).append("\"");
            result.append(" ent_id=\"").append(user.usr_ent_id).append("\">").append(dbUtils.NEWL);
            result.append("<test id=\"").append(pgr_res_id).append("\" tkh_id=\"").append(pgr_tkh_id);
            result.append("\" score=\"").append(pgr_score) ; 
            result.append("\" status=\"").append(pgr_status);
            result.append("\" completion_status=\"").append(pgr_completion_status);
            result.append("\" attempt=\"").append(pgr_attempt_nbr) ;
            result.append("\" schedule_date=\"").append(pgr_schedule_datetime) ; 
            result.append("\" complete_date=\"").append(pgr_complete_datetime) ;
            result.append("\" start_date=\"").append(pgr_start_datetime) ;
            result.append("\" last_acc_date=\"").append(pgr_last_acc_datetime).append("\">").append(dbUtils.NEWL) ;

            //result += QStat.modAvgByGroup(con, pgr_res_id, pgr_attempt_nbr, "('0')");  
//            result.append(QStat.modAvgByGroup(con, pgr_res_id, pgr_attempt_nbr, null));  

            result.append(dbUtils.NEWL);

            dbModule dbm = new dbModule();
            dbm.mod_res_id = pgr_res_id;
            dbm.res_id = dbm.mod_res_id;
            dbm.tkh_id = pgr_tkh_id;
            dbm.get(con);

            // get the test header
            result.append(dbm.getModHeader(con,prof));
            result.append("<essay_marked>" + isEssayMarked(con, pgr_res_id, pgr_usr_id, pgr_attempt_nbr,pgr_tkh_id) + "</essay_marked>");
            if(pgr_attempt_nbr == getLastAttemptNbr(con, pgr_res_id, pgr_tkh_id)) {
                result.append("<is_latest_attempt>true</is_latest_attempt>");
            } else {
                result.append("<is_latest_attempt>false</is_latest_attempt>");
            }

            result.append("<body>").append(dbUtils.NEWL);
                
            // Get the list of question in the test
            int i; 
            Vector qList = new Vector(); 
            dbResourceContent rcnObj = null; 
                
            if (Long.parseLong(que_id_lst[0]) == 0) {
                //qList = dbResourceContent.getChildAss(con,pgr_res_id);
            	qList = dbResourceContent.getChildAss(con,pgr_res_id,pgr_usr_id,pgr_attempt_nbr,pgr_tkh_id);
                // for dynamic test, qList returned will be empty (2005-12-13 kawai)
                if (qList.size() == 0) {
                    qList = dbProgressAttempt.getChildAss(con,pgr_usr_id,pgr_res_id,pgr_attempt_nbr,pgr_tkh_id); 
                } else {
                    // breakdown the qList for those scenario-type question
                    Vector revisedQList = new Vector();
                    for(i=0;i<qList.size();i++) {
                        dbResourceContent resCon = (dbResourceContent) qList.elementAt(i);
                        dbQuestion que = new dbQuestion();
                        que.que_res_id =  resCon.rcn_res_id_content; 
                        que.res_id = que.que_res_id;
                        que.get(con);
                        if( que.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) || que.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                            Vector childAss = dbResourceContent.getChildAss(con, que.que_res_id);
                            revisedQList.addAll(childAss);
                        } else {
                            revisedQList.addElement(resCon);
                        }
                    }
                    qList = revisedQList;
                }
            }else {
                for (i=0;i<que_id_lst.length;i++) {
                    rcnObj = new dbResourceContent();
                    rcnObj.rcn_res_id = pgr_res_id;
                    rcnObj.rcn_res_id_content = Long.parseLong(que_id_lst[i]); 
                    rcnObj.get(con); 
                    if( dbm.mod_type.equalsIgnoreCase(MOD_TYPE_DXT) || dbm.mod_type.equalsIgnoreCase(MOD_TYPE_STX) )
                        rcnObj.rcn_order = (i+1);
                    qList.addElement(rcnObj); 
                }
            }


            dbResourceContent rcn = null;
                    
            Hashtable qHash = new Hashtable();
            dbQuestion dbq = null;

            for (i=0; i<qList.size(); i++) {
                rcn = (dbResourceContent) qList.elementAt(i);
                dbq = new dbQuestion();
                dbq.que_res_id =  rcn.rcn_res_id_content; 
                dbq.res_id = dbq.que_res_id;
                dbq.get(con);
                qHash.put(new Long(dbq.que_res_id), dbq);
            }
            
            Hashtable usrAttemptHash = new Hashtable();
            dbProgressAttempt atm = new dbProgressAttempt();
            dbProgressAttempt atm2 = new dbProgressAttempt();
            Vector qAttempt = null;
            
            atm.atm_pgr_usr_id = pgr_usr_id;
            atm.atm_pgr_res_id = pgr_res_id;
            atm.atm_pgr_attempt_nbr = pgr_attempt_nbr;
            atm.atm_tkh_id = pgr_tkh_id;
            usrAttemptHash = atm.getQAttempt(con, qHash);

//            Hashtable queRespHash = new Hashtable();
//            queRespHash = QStat.modQueRespAvg(con, pgr_res_id, qHash, pgr_attempt_nbr, null);
            long parent_que_id = 0;
            Long queId = null;
            
            // write xml
            Vector containerVec = new Vector();
            StringBuffer continer_xml = new StringBuffer();
            //for get container obj id
            dbResourceObjective tempResObj = new dbResourceObjective();
            long container_obj_id = 0;
            
            continer_xml.append("<container_list>");
            for (i=0;i<qList.size();i++) {
                if(  !usrAttemptHash.containsKey(new  Long(((dbResourceContent)  qList.elementAt(i)).rcn_res_id_content)))
                                    continue;
                rcn = (dbResourceContent) qList.elementAt(i);
                queId = new Long(rcn.rcn_res_id_content);
                dbq = (dbQuestion) qHash.get(queId);
                //get container obj id
                parent_que_id = dbq.getContainerQueId(con);
                if (parent_que_id > 0) {
                    Vector obj_id = dbResourceObjective.getObjId(con, parent_que_id);
                    tempResObj = (dbResourceObjective)obj_id.get(0);
                    container_obj_id = tempResObj.rob_obj_id;
                } else {
                    container_obj_id = rcn.rcn_obj_id_content;
                }
                
                dbProgressAttempt pgrAtm = new dbProgressAttempt();
                pgrAtm.atm_pgr_usr_id = pgr_usr_id;
                pgrAtm.atm_pgr_res_id = pgr_res_id;
                pgrAtm.atm_int_res_id = dbq.que_res_id;
                pgrAtm.atm_pgr_attempt_nbr = pgr_attempt_nbr;
                pgrAtm.get(con,pgr_tkh_id);

                result.append(dbq.asXML(con, pgrAtm.atm_order));
                //System.out.println(result.toString());
                
                qAttempt =  (Vector) usrAttemptHash.get(queId);
                
                result.append("<result id=\"").append(dbq.que_res_id).append("\" obj_id=\"").append(container_obj_id).append("\">").append(dbUtils.NEWL); 
                for (int j=0; j<qAttempt.size();j++) {
                    atm2 = (dbProgressAttempt) qAttempt.elementAt(j);
                    result.append("<interaction order=\"").append(atm2.atm_int_order) ;
                    result.append("\" correct=\"").append(atm2.atm_correct_ind) ;
                    result.append("\" flag=\"").append(atm2.atm_flag_ind) ;
                    result.append("\" usr_score=\"").append(atm2.atm_score) ;
                    result.append("\">").append(dbUtils.NEWL);
                    // handle Matching response
                    String[] resps_ = dbUtils.split(atm2.atm_response_bil, dbProgressAttempt.RESPONSE_DELIMITER);
                    if (resps_ != null && resps_.length  > 0 && resps_[0] != null) {
                        for (int k=0; k < resps_.length; k++)
                            result.append("<response>").append(dbUtils.esc4XML(resps_[k])).append("</response>").append(dbUtils.NEWL);
                    }
                    result.append("</interaction>").append(dbUtils.NEWL);
                }
                
                StringBuffer tempContainer = new StringBuffer();
                if (parent_que_id > 0 && !containerVec.contains(new Long(parent_que_id))) {
                    dbQuestion parentQu = new dbQuestion();
                    parentQu.que_res_id = parent_que_id;
                    parentQu.get(con);
                    if (parentQu.que_type.equals(dbResource.RES_SUBTYPE_FSC)) {
                        FixedScenarioQue fsq = new FixedScenarioQue();
                        fsq.res_id = parentQu.que_res_id;
                        fsq.get(con);
                        continer_xml.append("<container id=\"").append(parentQu.que_res_id).append("\">")
                        .append(fsq.getQueXml()).append("</container>");
                        if (!containerVec.contains(new Long(parent_que_id))) {
                            tempContainer.append("<parent_container que_id=\"").append(dbq.que_res_id).append("\">").append(parent_que_id).append("</parent_container>");
                        }
                        containerVec.add(new Long(parent_que_id));
                    }else if (parentQu.que_type.equals(dbResource.RES_SUBTYPE_DSC)) {
                        DynamicScenarioQue dsq = new DynamicScenarioQue();
                        dsq.res_id = parentQu.que_res_id;
                        dsq.get(con);
                        continer_xml.append("<container id=\"").append(parentQu.que_res_id).append("\">")
                        .append(dsq.getQueXml()).append("</container>");
                        if (!containerVec.contains(new Long(parent_que_id))) {
                            tempContainer.append("<parent_container que_id=\"").append(dbq.que_res_id).append("\">").append(parent_que_id).append("</parent_container>");
                        }
                        containerVec.add(new Long(parent_que_id));
                    }
                }

                // Get the statistic of all students
//                result.append(queRespHash.get(queId));
                
                //result += QStat.modQueRespAvg(con, pgr_res_id, rcn.rcn_res_id_content, pgr_attempt_nbr, null);
                result.append("</result>").append(dbUtils.NEWL);
                result.append(tempContainer);
                if( dbq.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) || dbq.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
                    result.append(getFileSubmissionList(static_env, pgr_res_id, user.usr_ent_id, dbq.que_res_id, pgr_attempt_nbr));
                }                

            }
            continer_xml.append("</container_list>");
            // Get the score of the class grouped by objective
            // Get the statistic of all students
            //result += QStat.modAvgByObj(con,pgr_res_id, pgr_attempt_nbr, "('0')");
            result.append(continer_xml);
            //randy result.append(QStat.modAvgByObj(con,pgr_res_id, pgr_attempt_nbr, null));
//            result.append(QStat.modAvgByObj(con,pgr_res_id, pgr_attempt_nbr, null, dbm));
            result.append("</body>").append(dbUtils.NEWL);
            result.append("</test>").append(dbUtils.NEWL);
            result.append("</student>").append(dbUtils.NEWL);
            result.append("</student_report>").append(dbUtils.NEWL);
            return result.toString();

        }catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }
    
    public String rptUserAssAsXML(Connection con, loginProfile prof, String UPLOAD_PATH, String COMMENT_DIR)
        throws qdbException, cwSysMessage, qdbErrMessage
    {
        String result = "" ; 
            
        dbRegUser  user = new dbRegUser();
        user.usr_id = pgr_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);
            
        result += prof.asXML() + dbUtils.NEWL;
        
        String usr_lst = "('" + pgr_usr_id + "')";         
        //result += attemptInfoAsXML(con, usr_lst); 
        
        result += "<student id=\"" + user.usr_ste_usr_id + "\" last_name=\"" ;
        result += cwUtils.esc4XML(user.usr_last_name_bil) + "\" first_name=\"" ;
        result += cwUtils.esc4XML(user.usr_first_name_bil) + "\" display_bil=\"" ;
        result += cwUtils.esc4XML(user.usr_display_bil) + "\" email=\"" + cwUtils.esc4XML(user.usr_email) + "\"";
        result += " ent_id=\"" + user.usr_ent_id + "\">";
        result += dbUtils.NEWL;
            
        result += "<assignment id=\"" + pgr_res_id ;
        result += "\" tkh_id=\"" + pgr_tkh_id ;
        result += "\" score=\"" + pgr_score ; 
        result += "\" grade=\"" + pgr_grade ;
        result += "\" status=\"" + pgr_status + "\" attempt=\"" + pgr_attempt_nbr ;
        result += "\" schedule_date=\"" + pgr_schedule_datetime ; 
        result += "\" complete_date=\"" + pgr_complete_datetime ;
        result += "\" start_date=\"" + pgr_start_datetime ;
        result += "\" last_acc_date=\"" + pgr_last_acc_datetime + "\">" ;
        result += dbUtils.NEWL;

        //result += QStat.modAvgByGroup(con, pgr_res_id, pgr_attempt_nbr, "('0')");  
        result += QStat.modAvgByGroup(con, pgr_res_id, pgr_attempt_nbr, null);  
        result += dbUtils.NEWL;       
            
        dbAssignment dbass = new dbAssignment();
        dbass.ass_res_id = pgr_res_id;
        dbass.mod_res_id = dbass.ass_res_id;
        dbass.res_id = dbass.ass_res_id;
        dbass.get(con);
        // get the test header
        result += dbass.getAssHeader(con,prof.usr_id, prof.root_ent_id, pgr_tkh_id);
        result += "<body>" + dbUtils.NEWL;

//        String saveDirPath = UPLOAD_PATH + dbUtils.SLASH + pgr_res_id + dbUtils.SLASH + user.usr_id + dbUtils.SLASH;   
//        String commentDirPath = UPLOAD_PATH + dbUtils.SLASH + pgr_res_id + 
//                                dbUtils.SLASH + user.usr_id + dbUtils.SLASH + dbass.COMMENT_DIR + dbUtils.SLASH;

        String saveDirPath = dbAssignment.getAssPath(con, UPLOAD_PATH, pgr_res_id, user.usr_id, pgr_tkh_id) + dbUtils.SLASH;
        String commentDirPath = saveDirPath + dbass.COMMENT_DIR + dbUtils.SLASH;
            
        //result += "<uploadPath student=\"" + saveDirPath + "\" teacher=\"" + commentDirPath + "\">" + dbUtils.NEWL;
        result += "<uploadPath student=\"" + cwUtils.replaceSlashToHttp(saveDirPath) + "\" teacher=\"" + cwUtils.replaceSlashToHttp(commentDirPath) + "\">" + dbUtils.NEWL;
        
        dbProgressAttachment pgrAttach= new dbProgressAttachment(pgr_usr_id, pgr_res_id, pgr_attempt_nbr, pgr_tkh_id);
        Vector attLst = pgrAttach.get(con);
            
        for (int i=0; i<attLst.size(); i++) {
            dbAttachment attach = (dbAttachment) attLst.elementAt(i);
            String esc_filename = dbUtils.esc4XML(attach.att_filename);
            result += "<file id=\"" + attach.att_id + "\" name=\"" + esc_filename + 
                        "\" parentId=\"" + attach.att_att_id_parent + 
                        "\" desc=\"" + dbUtils.esc4XML(attach.att_desc) + 
                        "\" type=\"" + attach.att_type + "\">" + esc_filename + "</file>" + dbUtils.NEWL;
        }
            
        result += "</uploadPath>" + dbUtils.NEWL;
        result += "</body>"  + dbUtils.NEWL;
        result += "</assignment>" + dbUtils.NEWL;
        result += "</student>" + dbUtils.NEWL;
        return result;
    } 

    public String infoUserAssXML(Connection con)
        throws qdbException, qdbErrMessage 
    {
        String xmlBody = "";
        
        if (getStatus(con, 1) == null) {
            xmlBody += "<progress status=\"Not submitted yet\" />" + dbUtils.NEWL;
        } else {
            get(con);
            
            xmlBody = "<progress status=\"" + pgr_status +
                    "\" attempt_nbr=\"" + pgr_attempt_nbr +
                    "\" pgr_score=\"" + pgr_score +
                    "\" pgr_grade=\"" + pgr_grade +
                    "\" pgr_max_score=\"" + pgr_max_score +
                    "\" complete_datetime=\"" + pgr_complete_datetime + 
                    "\" />" + dbUtils.NEWL;
        }
                  
        return xmlBody;
    }
    
    public String rptTestAsXML(Connection con, loginProfile prof)
        throws qdbException, cwSysMessage
    {
        dbModule dbm = new dbModule();
        dbm.mod_res_id = pgr_res_id;
        dbm.res_id = dbm.mod_res_id; 
        dbm.get(con);
            
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<test_report>" + dbUtils.NEWL;
        result += "<module id=\""+ dbm.res_id + "\" language=\"" + dbm.res_lan + "\" timestamp=\"" + dbm.res_upd_date +"\">" + dbUtils.NEWL;
        
        result += prof.asXML() + dbUtils.NEWL;
        
        // Module Header 
        result += dbm.getModHeader(con,prof);
        result += "<body>" + dbUtils.NEWL;
            
        Vector usrLst = getUsrId(con); 
        dbProgress pgr = new dbProgress();
            
        dbRegUser  user = new dbRegUser();
        int i;
        String usr_xml = ""; 
            
        for (i=0;i<usrLst.size();i++) {
            pgr = (dbProgress) usrLst.elementAt(i);
                            
            user.usr_id = pgr.pgr_usr_id;
            user.usr_ent_id = user.getEntId(con);
            user.get(con);

            usr_xml += "<student id=\"" + dbUtils.esc4XML(user.usr_ste_usr_id) + "\" last_name=\"" ;
            usr_xml += user.usr_last_name_bil + "\" first_name=\"" ;
            usr_xml += user.usr_first_name_bil + "\" display_bil=\"" ;
            usr_xml += user.usr_display_bil + "\" email=\"" + cwUtils.esc4XML(user.usr_email) + "\">";

            usr_xml += dbUtils.NEWL;
                
            usr_xml += "<attempt number=\"" + pgr.pgr_attempt_nbr + "\" score=\"" ;
            usr_xml += pgr.pgr_score + "\" schedule_date=\"" + pgr.pgr_schedule_datetime ; 
            usr_xml += "\" complete_date=\"" + pgr.pgr_complete_datetime ;
            usr_xml += "\" start_date=\"" + pgr.pgr_start_datetime ;
            usr_xml += "\" last_acc_date=\"" + pgr.pgr_last_acc_datetime ;
            usr_xml += "\" status=\"" + pgr.pgr_status + "\">" ;
            usr_xml += "</attempt>" + dbUtils.NEWL;
                
            usr_xml += "</student>" + dbUtils.NEWL;
        }            
        
        result += usr_xml; 
        
        result += "</body>"  + dbUtils.NEWL;
        result += "</module>" + dbUtils.NEWL;
        result += "</test_report>" + dbUtils.NEWL;
        return result;
    }
    
    // NEW
    public String rptGroupListAsXML(Connection con, String[] que_id_lst, String[] rpt_group_lst, loginProfile prof)
        throws qdbException, SQLException, cwSysMessage, qdbErrMessage
    {
        
        String ent_id_lst = "";
        //String usr_lst = "";
        //Vector usrVec = new Vector();
        
        String temp = "";
        
        //usrVec = dbUserGroup.getUserVec(con, rpt_group_lst);
        
        for (int i = 0; i < rpt_group_lst.length; i++) {
            ent_id_lst += rpt_group_lst[i] + "~";
        }
            
        String result = "" ; 
            
        result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<group_report id=\"" + ent_id_lst + "\">" + dbUtils.NEWL;
        
        result += prof.asXML() + dbUtils.NEWL;
        
        result += "<test id=\"" + pgr_res_id + "\" attempt=\"" + pgr_attempt_nbr + "\">";
        result += dbUtils.NEWL;

        result += attemptInfoAsXML(con, rpt_group_lst);
        
        dbModule dbm = new dbModule();
        dbm.mod_res_id = pgr_res_id;
        dbm.res_id = dbm.mod_res_id;
        dbm.get(con);
        // get the test header
        result += dbm.getModHeader(con,prof);
        result += "<body>" + dbUtils.NEWL;

        // Get the list of question in the test
        int i; 
        Vector qList = new Vector(); 
        dbQuestion dbq = new dbQuestion(); 
        dbResourceContent rcnObj = null; 

        if (Long.parseLong(que_id_lst[0]) == 0) {
            qList = dbResourceContent.getChildAss(con,pgr_res_id);
            if (qList.size() == 0) 
                qList = dbProgressAttempt.getChildAss(con,rpt_group_lst,pgr_res_id,pgr_attempt_nbr); 
        
        }else {
            for (i=0;i<que_id_lst.length;i++) {
                rcnObj = new dbResourceContent();
                rcnObj.rcn_res_id = pgr_res_id;
                rcnObj.rcn_res_id_content = Long.parseLong(que_id_lst[i]); 
                rcnObj.get(con); 
                qList.addElement(rcnObj); 
            }
        }
                
        dbResourceContent rcn = null;
        for (i=0; i<qList.size(); i++) {
            
            rcn = (dbResourceContent) qList.elementAt(i);
            dbq.que_res_id =  rcn.rcn_res_id_content; 
            dbq.res_id = dbq.que_res_id;
            dbq.get(con);
            
            result += dbq.asXML(con, rcn.rcn_order); 
            long res_time = System.currentTimeMillis();

            result += "<result id=\"" + dbq.que_res_id + "\">" + dbUtils.NEWL ; 
            result += QStat.modQueRespAvg(con, pgr_res_id, rcn.rcn_res_id_content, pgr_attempt_nbr, rpt_group_lst);
            
            result += "</result>"; 
        
        }

        // Get the score average (percentage) of the class (grouped by usr_lst)
        result += QStat.modAvgByGroup(con, pgr_res_id, pgr_attempt_nbr, rpt_group_lst);  

        // Get the score of the class grouped by objective
        //randy result += QStat.modAvgByObj(con,pgr_res_id,pgr_attempt_nbr, rpt_group_lst);
        result += QStat.modAvgByObj(con,pgr_res_id,pgr_attempt_nbr, rpt_group_lst, dbm);

        result += "</body>"  + dbUtils.NEWL;
        result += "</test>" + dbUtils.NEWL;
        result += "</group_report>" + dbUtils.NEWL;
        return result;
    }
    
    public String rptGroupAsXML(Connection con, String[] que_id_lst, long entId, loginProfile prof)
        throws qdbException, cwSysMessage, qdbErrMessage
    {
        //try {
            
            String [] group_lst = new String[1];
            group_lst[0] = Long.toString(entId);
        
            String result = "" ; 
                
            result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<group_report id=\"" + entId + "\">" + dbUtils.NEWL;
            
            result += prof.asXML() + dbUtils.NEWL;
            
            result += "<test id=\"" + pgr_res_id + "\" attempt=\"" + pgr_attempt_nbr + "\">";
            result += dbUtils.NEWL;
            
            dbModule dbm = new dbModule();
            dbm.mod_res_id = pgr_res_id;
            dbm.res_id = dbm.mod_res_id;
            dbm.get(con);
            // get the test header
            result += dbm.getModHeader(con,prof);
            result += "<body>" + dbUtils.NEWL;
                
            // Get the list of question in the test
            int i; 
            Vector qList = new Vector(); 
            dbQuestion dbq = new dbQuestion(); 
            dbResourceContent rcnObj = null; 
            
            if (Long.parseLong(que_id_lst[0]) == 0) {
                qList = dbResourceContent.getChildAss(con,pgr_res_id);
                if (qList.size() == 0) 
                    qList = dbProgressAttempt.getChildAss(con,group_lst,pgr_res_id,pgr_attempt_nbr); 
            
            }else {
                for (i=0;i<que_id_lst.length;i++) {
                    rcnObj = new dbResourceContent();
                    rcnObj.rcn_res_id = pgr_res_id;
                    rcnObj.rcn_res_id_content = Long.parseLong(que_id_lst[i]); 
                    rcnObj.get(con); 
                    qList.addElement(rcnObj); 
                }
            }
            
            dbResourceContent rcn = null;

            for (i=0; i<qList.size(); i++) {
                
                rcn = (dbResourceContent) qList.elementAt(i);
                dbq.que_res_id =  rcn.rcn_res_id_content; 
                dbq.res_id = dbq.que_res_id;
                dbq.get(con);
                
                result += dbq.asXML(con, rcn.rcn_order); 
                long res_time = System.currentTimeMillis();

                result += "<result id=\"" + dbq.que_res_id + "\">" + dbUtils.NEWL ; 
                result += QStat.modQueRespAvg(con, pgr_res_id, rcn.rcn_res_id_content, pgr_attempt_nbr, group_lst);
                
                result += "</result>"; 
            
            }
            
            // Get the score average (percentage) of the class (grouped by usr_lst)
            result += QStat.modAvgByGroup(con, pgr_res_id, pgr_attempt_nbr, group_lst);  
            
            // Get the score of the class grouped by objective
            //randy result += QStat.modAvgByObj(con,pgr_res_id,pgr_attempt_nbr, group_lst);
            result += QStat.modAvgByObj(con,pgr_res_id,pgr_attempt_nbr, group_lst, dbm);
            
                
            result += "</body>"  + dbUtils.NEWL;
            result += "</test>" + dbUtils.NEWL;
            result += "</group_report>" + dbUtils.NEWL;
            return result;
        //}catch (SQLException e) {
        //    throw new qdbException(e.getMessage());
        //}
    }
    
    public void ins(Connection con, long used_time)
        throws qdbException
    {
        
       try {
        PreparedStatement stmt1 = con.prepareStatement(
            "SELECT max(pgr_attempt_nbr) NBR from Progress "
         +  " WHERE pgr_usr_id = ? "
         +  "   AND pgr_res_id = ? "
         +  "   AND pgr_tkh_id = ? ");
        
        PreparedStatement stmt2 = con.prepareStatement(
            "INSERT INTO Progress "
          + " ( pgr_usr_id "
          + "  ,pgr_res_id "
          + "  ,pgr_tkh_id "
          + "  ,pgr_attempt_nbr "
          + "  ,pgr_score "
          + "  ,pgr_max_score "
          + "  ,pgr_rank_bil "
          + "  ,pgr_schedule_datetime "
          + "  ,pgr_start_datetime "
          + "  ,pgr_complete_datetime "
          + "  ,pgr_last_acc_datetime "
          + "  ,pgr_status "
          + "  ,pgr_completion_status "
          + "  ,pgr_grade ) "          
          + " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"  );
        
        stmt1.setString(1, pgr_usr_id);
        stmt1.setLong(2, pgr_res_id);
        stmt1.setLong(3, pgr_tkh_id);
        ResultSet rs1 = stmt1.executeQuery();  
        pgr_attempt_nbr = 1;
        if(rs1.next())
            pgr_attempt_nbr = rs1.getLong("NBR") + 1;
        
        int index = 1;
        stmt2.setString(index++, pgr_usr_id);
        stmt2.setLong(index++, pgr_res_id);
        stmt2.setLong(index++, pgr_tkh_id);
        stmt2.setLong(index++, pgr_attempt_nbr);
        stmt2.setFloat(index++, pgr_score);
        stmt2.setFloat(index++, pgr_max_score);
        stmt2.setString(index++, pgr_rank_bil);
        stmt2.setTimestamp(index++, pgr_schedule_datetime);
        stmt2.setTimestamp(index++, pgr_start_datetime);
        
        long ctime_millsec = pgr_start_datetime.getTime() + used_time  * 60 * 1000 ;
        Timestamp complete_time = new Timestamp(ctime_millsec);
        stmt2.setTimestamp(index++, complete_time);
        
        stmt2.setTimestamp(index++, pgr_last_acc_datetime);
        stmt2.setString(index++, pgr_status);
        stmt2.setString(index++, pgr_completion_status);
        stmt2.setString(index++, pgr_grade);
        int stmtResult=stmt2.executeUpdate();
        stmt2.close();
        if ( stmtResult!=1)                            
        {        
            con.rollback();
            throw new qdbException("Failed to insert Progress.");
        }
        
        stmt1.close();
        
        
       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }                
    }
    /*
    *   retired and the table's meaning changed..   2001/12/12 kim
    */
    /*
    public void saveAttempt(Connection con)
        throws qdbException
    {
       try {
            boolean bExist = false;
            
            PreparedStatement stmt = con.prepareStatement(
                " SELECT pgr_usr_id from Progress WHERE pgr_usr_id = ? and pgr_res_id = ? ");
            
            stmt.setString(1, pgr_usr_id);
            stmt.setLong(  2, pgr_res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bExist = true;
            }
            
            String SQL = new String();
            
            if (bExist) {
                SQL = "   UPDATE Progress SET "
                    + "          pgr_las_acc_datetime = ? "
                    + "          ,pgr_attempt_nbr = pgr_attempt_nbr + 1 "
                    + "      WHERE pgr_usr_id = ? and pgr_res_id = ? "; 
                
                stmt = con.prepareStatement(SQL);
                stmt.setTimestamp(1, pgr_last_acc_datetime);
                stmt.setString(2, pgr_usr_id);
                stmt.setLong(3, pgr_res_id);
            
            } else {
                SQL = "   INSERT INTO Progress "
                    + "        ( pgr_usr_id " 
                    + "         ,pgr_res_id "
                    + "         ,pgr_attempt_nbr "
                    + "         ,pgr_score "
                    + "         ,pgr_max_score "
                    + "         ,pgr_start_datetime "
                    + "         ,pgr_last_acc_datetime "
                    + "         ,pgr_status ) "
                    + "      VALUES (?,?,?,?,?,?,?,?) ";
                
                stmt = con.prepareStatement(SQL);
                stmt.setString(1, pgr_usr_id);
                stmt.setLong(  2, pgr_res_id);
                stmt.setLong(  3, pgr_attempt_nbr);
                stmt.setFloat(  4, pgr_score);
                stmt.setFloat(  5, pgr_max_score);
                stmt.setTimestamp( 6, pgr_start_datetime);
                stmt.setTimestamp( 7, pgr_last_acc_datetime);
                stmt.setString(8, pgr_status);
                stmt.setTimestamp(9, pgr_last_acc_datetime);
                stmt.setString(10, pgr_usr_id);
                stmt.setLong(11, pgr_res_id);
            
            }
            
            if (stmt.executeUpdate() != 1) {
                con.rollback();
                throw new qdbException("Failed to save attempt.");
            }
        
            stmt.close();
       } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }                
    }
    */
    public Vector getUsrId(Connection con) throws qdbException{
        return getUsrId(con, "ASC", null);  
    }

    
    // give the list of users given a module id 
    public Vector getUsrId(Connection con, String attempt_order, String add_cond) 
        throws qdbException
    {   
        try {
            String SQL = " SELECT "
            + "   pgr_usr_id "
            + " , pgr_res_id " 
            + " , pgr_attempt_nbr " 
            + " , pgr_score "
            + " , pgr_rank_bil "
            + " , pgr_schedule_datetime "
            + " , pgr_complete_datetime "
            + " , pgr_last_acc_datetime "
            + " , pgr_start_datetime "
            + " , pgr_status "
            +   " from Progress "
            +   " where pgr_res_id = ? ";
            
            if (add_cond != null)
                SQL += add_cond;
                
            SQL += " order by pgr_usr_id, pgr_attempt_nbr " + attempt_order;
            

            PreparedStatement stmt = con.prepareStatement(SQL); 
            
            // set the values for prepared statements
            stmt.setLong(1, pgr_res_id);
            
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
    
    public String attemptInfoAsXML(Connection con, String usr_id)
        throws qdbException
    {
        try {
            String xml = ""; 
            
            PreparedStatement stmt = con.prepareStatement(
            " SELECT pgr_attempt_nbr, pgr_score, pgr_complete_datetime, pgr_status "
            +   " FROM Progress "
            +   " where pgr_res_id = ?  "
            +   "     AND pgr_usr_id = ? "
            +   "     AND pgr_tkh_id = ? "
            +   "       order by pgr_attempt_nbr ");  

            // set the values for prepared statements
            stmt.setLong(1, pgr_res_id);
            stmt.setString(2, usr_id);
            stmt.setLong(3, pgr_tkh_id);
            xml += "<attempt_list current=\"" + pgr_attempt_nbr + "\">" ;
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                long attempt_nbr = rs.getLong(1); 
                long max_score   = rs.getLong(2); 
                Timestamp complete_datetime = rs.getTimestamp(3); 
                xml += "<attempt id=\"" + attempt_nbr + "\" max_score=\"" ;
                xml += max_score + "\" complete_time=\"" + complete_datetime + "\" status=\"" + rs.getString("pgr_status") + "\">";  
                xml += "</attempt>" + dbUtils.NEWL;
            }
            
            xml += "</attempt_list>" + dbUtils.NEWL;
            
            stmt.close();

            return xml ; 

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

    
    public String attemptInfoAsXML(Connection con, String[] group_lst)
        throws qdbException
    {
        try {
            String xml = ""; 
            String SQL = " SELECT "
            + "  distinct pgr_attempt_nbr, MAX(pgr_score), MAX(pgr_complete_datetime) "
            +   " FROM Progress , EntityRelation, RegUser " 
            +   " where pgr_res_id = ?  "
            +   "     AND pgr_usr_id = usr_id "
            +   "     AND usr_ent_id = ern_child_ent_id "
            +   "     AND ern_parent_ind = ? ";
            
            if (group_lst != null && group_lst.length > 0) {
                SQL += " AND ern_ancestor_ent_id IN " + dbUtils.array2list(group_lst);
            }
            
            SQL += "       group by pgr_attempt_nbr ";
            SQL += "       order by pgr_attempt_nbr ";  
             
            PreparedStatement stmt = con.prepareStatement(SQL);

            // set the values for prepared statements
            stmt.setLong(1, pgr_res_id);
            stmt.setBoolean(2, true);
            
            xml += "<attempt_list current=\"" + pgr_attempt_nbr + "\">" ;
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                long attempt_nbr = rs.getLong(1); 
                long max_score   = rs.getLong(2); 
                Timestamp complete_datetime = rs.getTimestamp(3); 
                xml += "<attempt id=\"" + attempt_nbr + "\" max_score=\"" ;
                xml += max_score + "\" complete_time=\"" + complete_datetime + "\">"; 
                xml += "</attempt>" + dbUtils.NEWL;
            }
            
            xml += "</attempt_list>" + dbUtils.NEWL;
            
            stmt.close();

            return xml ; 

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

    // helper fcn for Assignment
    public void updResult(Connection con)
        throws qdbException
    {
        try {
            if(!isEssayMarked(con, pgr_res_id, pgr_usr_id, pgr_attempt_nbr, pgr_tkh_id)) {
                pgr_status = PGR_STATUS_NOT_GRADED;
                pgr_score = 0;
            }
            
            PreparedStatement stmt = con.prepareStatement(
             " UPDATE Progress SET pgr_score = ?, pgr_grade = ?, pgr_status = ?, pgr_complete_datetime = ? , pgr_last_acc_datetime = ?, pgr_completion_status = ? " +
             " WHERE pgr_usr_id = ? AND pgr_res_id = ? AND pgr_attempt_nbr = ? AND pgr_tkh_id = ? ");
          
            int col = 1;
            stmt.setFloat(col++, pgr_score);
            stmt.setString(col++, pgr_grade);
            stmt.setString(col++, pgr_status);
            stmt.setTimestamp(col++, pgr_complete_datetime);
            stmt.setTimestamp(col++, pgr_last_acc_datetime);
            stmt.setString(col++, pgr_completion_status);
            stmt.setString(col++, pgr_usr_id);
            stmt.setLong(col++, pgr_res_id);
            stmt.setLong(col++, pgr_attempt_nbr);
            stmt.setLong(col++, pgr_tkh_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                throw new qdbException("Failed to update Progress.");
            }
            
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    /**
    * Delete User progress on the specified resource
    */
    public void del(Connection con)
        throws SQLException{
            
            String SQL = " DELETE FROM Progress WHERE pgr_usr_id = ? AND pgr_res_id = ? AND pgr_tkh_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, this.pgr_usr_id);
            stmt.setLong(2, this.pgr_res_id);
            stmt.setLong(3, this.pgr_tkh_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }
    
    public static void delByResId(Connection con, long resId)
    	throws SQLException{
        
        String SQL = " DELETE FROM Progress WHERE pgr_res_id = ?";
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setLong(index++, resId);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    // helper fcn for Assignment
    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
//        String privilege=null;
        
        //if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) 
        //    privilege = dbResourcePermission.RIGHT_WRITE;
        //else if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_STUDENT)) 
        //    privilege = dbResourcePermission.RIGHT_EXECUTE;
            
        // check User Right
        /*
        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) {
            dbModule dbmod_ = new dbModule();
            dbmod_.mod_res_id = pgr_res_id;
            dbmod_.checkModifyPermission(con , prof);
        }else {// For Student
            privilege = dbResourcePermission.RIGHT_EXECUTE;
            long cosId = dbModule.getCosId(con, pgr_res_id);
            if (!dbResourcePermission.hasPermission(con, cosId, prof, privilege)) {
                throw new qdbErrMessage(privilege);
            }
        }
        */

/*      privilege = dbResourcePermission.RIGHT_EXECUTE;
        long cosId = dbModule.getCosId(con, pgr_res_id);
        if (!dbResourcePermission.hasPermission(con, cosId, prof, privilege)) {
            try {
                dbModule dbmod_ = new dbModule();
                dbmod_.mod_res_id = pgr_res_id;
                dbmod_.checkModifyPermission(con , prof);
            }
            catch(qdbErrMessage e) {
                throw new qdbErrMessage("ACL002");
            }
        }
*/        
        try {
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From Progress where pgr_usr_id = ? AND pgr_res_id = ? AND pgr_attempt_nbr = ?");
                    
            stmt.setString(1, pgr_usr_id);
            stmt.setLong(2, pgr_res_id);
            stmt.setLong(3, pgr_attempt_nbr);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void delAll(Connection con)
        throws qdbException, qdbErrMessage
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From Progress where pgr_res_id = ? AND pgr_attempt_nbr = ?");
                    
            stmt.setLong(1, pgr_res_id);
            stmt.setLong(2, pgr_attempt_nbr);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }    
    
    public void delAll(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        // check User Right
//        dbModule dbmod_ = new dbModule();
//        dbmod_.mod_res_id = pgr_res_id;
//        dbmod_.checkModifyPermission(con, prof);
        
        delAll(con);
    }    
/*    
    public static Hashtable getLastAttemptDate(Connection con, long res_id, Vector usrIds){
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_id", cwSQL.COL_TYPE_STRING, 30);
        cwSQL.insertSimpleTempTable(con, tableName, usrIds, cwSQL.COL_TYPE_STRING);

        PreparedStatement stmt = con.prepareStatement(
            "SELECT pgr_usr_id, max(pgr_start_datetime) last_attempt_date from Progress " + 
            "where pgr_res_id = ? and pgr_attempt_nbr != 0 and pgr_usr_id IN ( SELECT tmp_usr_id FROM " +  tableName + " ) " +
            "group by pgr_usr_id ");

        stmt.setLong(1, res_id);
        
        ResultSet rs = stmt.executeQuery();
        
        Hashtable htUsrId = new Hashtable();
        while (rs.next()){
                htUsrId.put(rs.getString("pgr_usr_id"), rs.getTimestamp("last_attempt_date"));
        }

        return htUsrId;
    }
    */
    
    public String attemptAsXML(){
        String xml = "<attempt number=\"" + pgr_attempt_nbr + "\" score=\"" ;
        xml += pgr_score + "\" schedule_date=\"" + pgr_schedule_datetime ; 
        xml += "\" complete_date=\"" + pgr_complete_datetime ;
        xml += "\" start_date=\"" + pgr_start_datetime ;
        xml += "\" last_acc_date=\"" + pgr_last_acc_datetime ;
        xml += "\" status=\"" + pgr_status + "\">" ;
        xml += "</attempt>" + dbUtils.NEWL;

        return xml;
    }            


    public Vector getAttemptedResult(Connection con, long mov_mod_id, String mov_status, String sort_col, String sort_order, String tkh_ids)
        throws SQLException{
            
            String SQL = null;
            PreparedStatement stmt = null;
            int index = 1;
            //if( mov_status == null || mov_status.equalsIgnoreCase("N") ){
                Vector v_itm_id = null;
                aeItem aeItm = new aeItem();
                aeItm.itm_id =  dbModule.getModuleItemId(con, mov_mod_id);             
                if( aeItm.getCreateRunInd(con) ) {
                    v_itm_id = aeItemRelation.getChildItemId(con, aeItm.itm_id);
                }else{
                    v_itm_id = new Vector();
                    v_itm_id.addElement(new Long(aeItm.itm_id));
                }
                
                SQL = getModuleEvaluationUserListSQL(con, mov_status, sort_col, sort_order, tkh_ids);
                stmt = con.prepareStatement(SQL);
                stmt.setLong(index++, mov_mod_id);
                if( mov_status != null && mov_status.length() > 0 )
                    stmt.setString(index++, mov_status);
                
            /*    
            }else {
                
                SQL = getModuleEvaluationUserListSQL(con, mov_status, sort_col, sort_order);
                stmt = con.prepareStatement(SQL);
                stmt.setLong(index++, mov_mod_id);
                stmt.setLong(index++, mov_mod_id);
                stmt.setString(index++, mov_status);
                
            }
            */
            Vector resultVec = new Vector();
            ResultSet rs = stmt.executeQuery();
            Vector traceProgressUsrVec = new Vector();
            dbProgress pgr_temp = new dbProgress();
            while(rs.next()){
                dbProgress pgr = new dbProgress();
                pgr.pgr_usr_id = rs.getString("usr_id");
                pgr.pgr_res_id = rs.getLong("pgr_res_id");
                pgr.pgr_tkh_id= rs.getLong("pgr_tkh_id");
                pgr.pgr_attempt_nbr = rs.getLong("pgr_attempt_nbr");
                pgr.pgr_score = rs.getFloat("pgr_score");
                pgr.pgr_rank_bil = rs.getString("pgr_rank_bil");
                pgr.pgr_schedule_datetime = rs.getTimestamp("pgr_schedule_datetime");
                pgr.pgr_complete_datetime = rs.getTimestamp("pgr_complete_datetime");
                pgr.pgr_last_acc_datetime = rs.getTimestamp("pgr_last_acc_datetime");
                pgr.pgr_start_datetime = rs.getTimestamp("pgr_start_datetime");
                pgr.pgr_status = rs.getString("pgr_status");
                if(traceProgressUsrVec.contains(pgr.pgr_usr_id)){
                    int index_temp = traceProgressUsrVec.lastIndexOf(pgr.pgr_usr_id);
                    pgr_temp = (dbProgress)resultVec.get(index_temp);
                    if(pgr.pgr_tkh_id > pgr_temp.pgr_tkh_id ||
                       (pgr.pgr_tkh_id > pgr_temp.pgr_tkh_id && pgr.pgr_attempt_nbr > pgr_temp.pgr_attempt_nbr)){
                        resultVec.remove(index_temp);
                        resultVec.add(index_temp,pgr);
                    }   
                }else{
                    resultVec.addElement(pgr);
                    traceProgressUsrVec.add(pgr.pgr_usr_id);
                }
                
            }
            stmt.close();
            return resultVec;

        }
    private static final String sql_get_mov_user_list =
        "SELECT usr_id, "
        + "pgr_res_id, pgr_attempt_nbr, pgr_tkh_id, "
        + "pgr_score, pgr_rank_bil, pgr_schedule_datetime, "
        + "pgr_complete_datetime, pgr_last_acc_datetime, "
        + "pgr_start_datetime, pgr_status "
        + "FROM ResourceContent, Enrolment, RegUser, ModuleEvaluation, Progress "
        + "WHERE rcn_res_id_content = ? "
        + "AND rcn_res_id = enr_res_id "
        + "AND enr_ent_id = usr_ent_id "
        + "AND enr_ent_id = mov_ent_id AND rcn_res_id_content = mov_mod_id "
        + "AND usr_id = pgr_usr_id AND mov_mod_id = pgr_res_id ";
    private static String getModuleEvaluationUserListSQL(Connection con, String mov_status, String sort_col, String sort_order, String tkh_ids)
        throws SQLException {
            StringBuffer sqlStr = new StringBuffer();
            
            sqlStr.append(sql_get_mov_user_list);
            if (mov_status != null && mov_status.length() > 0) {
                if( mov_status.equalsIgnoreCase("N") )
                    sqlStr.append(" AND ( mov_status = ? OR mov_status is null ) ");
                else
                    sqlStr.append(" AND ( mov_status = ? ) ");
            }
            if(tkh_ids != null && !"".equals(tkh_ids)){
            	 sqlStr.append(" AND pgr_tkh_id in ("+ tkh_ids +") ");
            }
            sqlStr.append(" ORDER BY ").append(sort_col).append(" ").append(sort_order);
            
            return sqlStr.toString();
        }

    public boolean isEssayMarked(Connection con, long modId, String usrId, long attemptNbr, long thk_id) throws qdbException {
        try {
            String xml = ""; 
            String SQL = " SELECT que_res_id, atm_score, usr_ent_id "
                    + " FROM question, progressAttempt, regUser "
                    + " WHERE atm_int_res_id = que_res_id "
                    + " AND usr_id = atm_pgr_usr_id "
                    + " AND atm_pgr_res_id = ? "
                    + " AND (que_type = ? or que_type = ?)"
                    + " AND atm_score = ? "
                    + " AND atm_pgr_usr_id = ? "
                    + " AND atm_pgr_attempt_nbr = ? ";
                   if(thk_id != 0)
                       SQL +=  " AND atm_tkh_id = ? ";
            int index = 1;
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, modId);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY_2);
            // -1 stand for not marked yet.
            stmt.setLong(index++, -1);
            stmt.setString(index++, usrId);
            stmt.setLong(index++, attemptNbr);
            if(thk_id != 0)
                stmt.setLong(index++, thk_id);
            
            ResultSet rs = stmt.executeQuery();
            boolean flag = true;
            if(rs.next()) {
            	flag = false;
            } else {
            	flag = true;
            }
            cwSQL.cleanUp(rs, stmt);
            return flag;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
    
    // getTotalScore will not count "score = -1"
    private float getTotalScore(Connection con, String usr_id, long res_id, long attempt_nbr,long tkh_id) throws qdbException {
        try {
            int index = 1;
            float totalScore = 0;
            String SQL = " SELECT sum(atm_score) as score FROM progressAttempt "
                    + " WHERE atm_pgr_usr_id = ? " 
                    + " AND atm_tkh_id = ? " 
                    + " AND atm_pgr_res_id = ? " 
                    + " AND atm_pgr_attempt_nbr = ? " 
                    + " AND atm_score != ? "
                    + " GROUP BY atm_pgr_usr_id, atm_pgr_res_id, atm_pgr_attempt_nbr ";
                               
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(index++, this.pgr_usr_id);
            stmt.setLong(index++, tkh_id);
            stmt.setLong(index++, this.pgr_res_id);
            stmt.setLong(index++, this.pgr_attempt_nbr);
            stmt.setLong(index++, -1);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                totalScore = rs.getFloat("score");
            }
            rs.close();
            stmt.close();
            
            return totalScore;
        } catch (SQLException e) {
            throw new qdbException ("SQLError: " + e.getMessage());
        }
    }

    public void updScore(Connection con, long queId, long score, loginProfile prof) throws qdbException, qdbErrMessage {
        try { 
            // write to dbProgressAttempt
            if(dbProgress.getLastAttemptNbr(con, this.pgr_res_id, this.pgr_tkh_id)!=this.pgr_attempt_nbr) {
                throw new qdbException("Failed to update Progress.");
            }
            dbProgressAttempt dbPgrAtm = new dbProgressAttempt();
            dbPgrAtm.atm_pgr_usr_id = this.pgr_usr_id;
            dbPgrAtm.atm_pgr_res_id = this.pgr_res_id;
            dbPgrAtm.atm_pgr_attempt_nbr = this.pgr_attempt_nbr;
            dbPgrAtm.atm_int_res_id = queId; 
            dbPgrAtm.atm_score = score;
            dbPgrAtm.atm_tkh_id = this.pgr_tkh_id;
            if(score==0) {
                dbPgrAtm.atm_correct_ind = false;
            } else {
                dbPgrAtm.atm_correct_ind = true;
            }
            dbPgrAtm.updateScore(con);
            
            if(isEssayMarked(con, this.pgr_res_id, this.pgr_usr_id, this.pgr_attempt_nbr, pgr_tkh_id)) {
                // write to progress
                // set the total score
                this.pgr_score = getTotalScore(con, this.pgr_usr_id, this.pgr_res_id, this.pgr_attempt_nbr, pgr_tkh_id);
                    
                // set status to 'OK'
                this.pgr_status=PGR_STATUS_OK;
                    
                dbModule dbmod = new dbModule();
                dbmod.mod_res_id = pgr_res_id;
                dbmod.get(con);

                if((pgr_score/dbmod.mod_max_score)*100 >= dbmod.mod_pass_score) {
                    this.pgr_completion_status = COMPLETION_STATUS_PASSED;
                } else {
                    this.pgr_completion_status = COMPLETION_STATUS_FAILED;
                }

                // perform update
                int index = 1;
                String SQL = " UPDATE progress SET pgr_status= ?, pgr_score = ?, pgr_completion_status = ? "
                           + " WHERE pgr_usr_id = ? "
                           + " AND pgr_res_id = ? "
                           + " AND pgr_tkh_id = ? "
                           + " AND pgr_attempt_nbr = ? ";
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setString(index++, this.pgr_status);
                stmt.setFloat(index++, this.pgr_score);
                stmt.setString(index++, this.pgr_completion_status);
                stmt.setString(index++, this.pgr_usr_id);
                stmt.setLong(index++, this.pgr_res_id);
                stmt.setLong(index++, this.pgr_tkh_id);
                stmt.setLong(index++, this.pgr_attempt_nbr);
                if(stmt.executeUpdate()!=1) {
                    con.rollback();
                    throw new qdbException("Failed to insert ProgressAttempt");
                }
                stmt.close();
                
                // write to moduleEvaluation
                dbModuleEvaluation dbmov = new dbModuleEvaluation();
                dbmov.mov_ent_id = Long.parseLong(pgr_usr_id.substring(pgr_usr_id.indexOf("u")+1));
                dbmov.mov_mod_id = pgr_res_id;
                dbmov.mov_tkh_id = pgr_tkh_id;
              
                dbmov.get(con);
                dbmov.mov_not_mark_ind = 0;
                dbmov.mov_score = pgr_score + "";
                
                if((Float.parseFloat(dbmov.mov_score)/dbmod.mod_max_score)*100 >= dbmod.mod_pass_score) {
                    dbmov.mov_status = dbAiccPath.STATUS_PASSED;
                } else {
                    dbmov.mov_status = dbAiccPath.STATUS_FAILED;
                }
                dbmov.upd(con);
                
                dbModuleEvaluationHistory dbModEvalHist = new dbModuleEvaluationHistory();
                dbModEvalHist.mvh_ent_id = Long.parseLong(pgr_usr_id.substring(pgr_usr_id.indexOf("u")+1));
                dbModEvalHist.mvh_mod_id = pgr_res_id;
                dbModEvalHist.mvh_tkh_id = pgr_tkh_id;
                dbModEvalHist.mvh_score = pgr_score + "";
                dbModEvalHist.mvh_status = dbmov.mov_status;
                dbModEvalHist.updScore(con);
                
                //for update the course score
                if (dbmov.mov_cos_id > 0) {
                    dbCourseEvaluation dbcov = new dbCourseEvaluation();
                    dbcov.cov_cos_id = dbmov.mov_cos_id;
                    dbcov.cov_ent_id = dbmov.mov_ent_id;
                    dbcov.cov_tkh_id = dbmov.mov_tkh_id;
//                    dbcov.save(con);
//                    CourseCriteria.setAttendFromModule(con, prof, dbmov.mov_mod_id, dbmov.mov_cos_id, dbmov.mov_ent_id, dbmov.mov_tkh_id, dbmov.mov_status);
                    CourseCriteria.setAttendFromModule( con,  prof, dbmov.mov_mod_id,  dbmov.mov_cos_id,  dbmov.mov_ent_id,  dbmov.mov_tkh_id,  dbmov.mov_status,0, cwSQL.getTime(con), null);
                }
            }
        } catch (SQLException e) { 
            throw new qdbException("SQLException: " + e.getMessage());
        } catch (cwSysMessage e) {
            throw new qdbException("cwSysMessage: " + e.getMessage());
        } catch (cwException e) {
            throw new qdbException("cwException: " + e.getMessage());
        }
    }

    public long numEssay(Connection con, long modId, String usrId, long attemptNbr) throws qdbException {
        try {
            long num = 0;
            String SQL = " SELECT count(que_res_id) as num "
                    + " FROM question, progressAttempt, regUser "
                    + " WHERE atm_int_res_id = que_res_id "
                    + " AND usr_id = atm_pgr_usr_id "
                    + " AND atm_pgr_res_id = ? "
                    + " AND (que_type = ? or que_type = ?) "
                    + " AND atm_pgr_usr_id = ? "
                    + " AND atm_pgr_attempt_nbr = ? ";
            int index = 1;
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, modId);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY_2);
            stmt.setString(index++, usrId);
            stmt.setLong(index++, attemptNbr);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                num = rs.getLong("num");
            } else {
                num = 0;
            }
            cwSQL.cleanUp(rs, stmt);
            return num;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

    public long numEssayNotMarked(Connection con, long modId, long tkh_id, long attemptNbr) throws qdbException {
        try {
            long num = 0;
            String SQL = " SELECT count(que_res_id) as num "
                    + " FROM question, progressAttempt, regUser "
                    + " WHERE atm_int_res_id = que_res_id "
                    + " AND usr_id = atm_pgr_usr_id "
                    + " AND atm_pgr_res_id = ? "
                    + " AND (que_type = ? or que_type = ?) "
                    + " AND atm_score = ? "
                    + " AND atm_tkh_id = ? "
                    + " AND atm_pgr_attempt_nbr = ? ";
            int index = 1;
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, modId);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY_2);
            stmt.setLong(index++, -1);
            stmt.setLong(index++, tkh_id);
            stmt.setLong(index++, attemptNbr);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                num = rs.getLong("num");
            } else {
                num = 0;
            }
            cwSQL.cleanUp(rs, stmt);
            return num;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

/*
    private static String getModuleEvaluationUserListSQL(Connection con, String mov_status, String sort_col, String sort_order)
        throws SQLException {
            String dbproduct = cwSQL.getDbProductName();            
            StringBuffer sqlStr = new StringBuffer(512);
            if (dbproduct.indexOf("oracle") >= 0) {

            sqlStr.append(" SELECT usr_id, pgr_res_id, pgr_attempt_nbr, ") 
                  .append(" pgr_score, pgr_rank_bil, pgr_schedule_datetime, ")
                  .append(" pgr_complete_datetime, pgr_last_acc_datetime, ")
                  .append(" pgr_start_datetime, pgr_status  ")            
                  .append(" FROM RegUser, ModuleEvaluation, Progress ")                  
                  .append(" WHERE mov_mod_id = ? ")
                  .append(" AND pgr_usr_id(+) = usr_id ")
                  .append(" AND pgr_res_id(+) = ? ")
                  .append(" AND usr_ent_id = mov_ent_id ")
                  .append(" AND mov_status = ? ")
                  .append(" AND ( pgr_res_id = mov_mod_id OR pgr_res_id is null ) ")
                  .append(" ORDER BY " ).append(sort_col).append(" ").append(sort_order);
            } else {
            sqlStr.append(" SELECT usr_id, pgr_res_id, pgr_attempt_nbr, ") 
                  .append(" pgr_score, pgr_rank_bil, pgr_schedule_datetime, ")
                  .append(" pgr_complete_datetime, pgr_last_acc_datetime, ")
                  .append(" pgr_start_datetime, pgr_status  ")
                  .append(" FROM ModuleEvaluation, RegUser ")
                  .append(" Left join Progress On ( pgr_usr_id = usr_id and pgr_res_id = ? ) ")
                  .append(" WHERE mov_mod_id = ? ")
                  .append(" AND usr_ent_id = mov_ent_id ")
                  .append(" AND mov_status = ? ")
                  .append(" AND ( pgr_res_id = mov_mod_id OR pgr_res_id is null ) ")
                  .append(" ORDER BY " ).append(sort_col).append(" ").append(sort_order);
            }
            return sqlStr.toString();
        }
*/


        private String getFileSubmissionList(
            qdbEnv static_env,
            long mod_res_id,
            long usr_ent_id,
            long que_res_id,
            long pgr_attempt_nbr)
            throws qdbException {
            StringBuffer xml = new StringBuffer();

            String filePath =
                static_env.INI_DIR_UPLOAD
                    + dbUtils.SLASH
                    + mod_res_id
                    + dbUtils.SLASH
                    + usr_ent_id
                    + dbUtils.SLASH
                    + que_res_id
                    + dbUtils.SLASH
                    + pgr_attempt_nbr
                    + dbUtils.SLASH;
            File path = new File(filePath);
            if (!path.exists() && !path.isDirectory()) {
                return "";
            }

            String[] file_list = path.list();
            xml.append("<file_list id=\"").append(que_res_id).append("\">");
            for (int i = 0; i < file_list.length; i++) {
                xml.append("<file data=\"").append(file_list[i]).append("\"/>");
            }
            xml.append("</file_list>");
            return xml.toString();
        }
    
        public long numEssayNotMarked_test(Connection con, long modId, long tkh_id) throws qdbException {
            try {
                long num = 0;
                String SQL = "SELECT count(que_res_id) as num "
                        + " FROM question, progressAttempt, regUser "
                        + " WHERE atm_int_res_id = que_res_id "
                        + " AND usr_id = atm_pgr_usr_id "
                        + " AND atm_tkh_id = ? "
                        + " AND atm_pgr_res_id = ? "
                        + " AND (que_type = ? or que_type = ?) "
                        + " AND atm_score = ? "
                        + " AND atm_pgr_attempt_nbr >0 "
                        + " AND atm_pgr_attempt_nbr = "
                        + " (SELECT max(pgr_attempt_nbr) FROM Progress " 
                        + "  WHERE pgr_res_id = ? " 
                        + "  AND  pgr_tkh_id = ? )";
                int index = 1;
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(index++, tkh_id);
                stmt.setLong(index++, modId);
                stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY);
                stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY_2);
                stmt.setLong(index++, -1);
                stmt.setLong(index++, modId);
                stmt.setLong(index++, tkh_id);
                
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    num = rs.getLong("num");
                } else {
                    num = 0;
                }
                stmt.close();
                return num;
            } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
            }        
        }
     
     public String rptUserAsXML_test(Connection con, String[] que_id_lst, loginProfile prof, String metaXML, qdbEnv static_env)
     throws qdbException, cwSysMessage, qdbErrMessage
     {
         try {
    
          //access control
             StringBuffer result = new StringBuffer(1024); 
             dbRegUser  user = new dbRegUser();
             user.usr_id = pgr_usr_id;
             user.usr_ent_id = prof.usr_ent_id;//user.getEntId(con);dob.cur_stylesheet
             user.get(con);
    
             //result.append(dbUtils.xmlHeader);

             result.append("<student_report>").append(dbUtils.NEWL);
             
             result.append(prof.asXML()).append(dbUtils.NEWL);
             if (metaXML !=null){
                 //result.append(metaXML).append(dbUtils.NEWL);
             }
             
             result.append(attemptInfoAsXML(con, pgr_usr_id)); 
    
             result.append("<student id=\"").append(user.usr_ste_usr_id).append("\" last_name=\"") ;
             result.append(dbUtils.esc4XML(user.usr_last_name_bil)).append("\" first_name=\"") ;
             result.append(dbUtils.esc4XML(user.usr_first_name_bil)).append("\" display_bil=\"") ;
             result.append(dbUtils.esc4XML(user.usr_display_bil)).append("\" email=\"").append(cwUtils.esc4XML(user.usr_email)).append("\"");
             result.append(" ent_id=\"").append(user.usr_ent_id).append("\">").append(dbUtils.NEWL);
             result.append("<test id=\"").append(pgr_res_id).append("\" tkh_id=\"").append(pgr_tkh_id);
             result.append("\" score=\"").append(pgr_score) ; 
             result.append("\" status=\"").append(pgr_status);
             result.append("\" completion_status=\"").append(pgr_completion_status);
             result.append("\" attempt=\"").append(pgr_attempt_nbr) ;
             result.append("\" schedule_date=\"").append(pgr_schedule_datetime) ; 
             result.append("\" complete_date=\"").append(pgr_complete_datetime) ;
             result.append("\" start_date=\"").append(pgr_start_datetime) ;
             result.append("\" last_acc_date=\"").append(pgr_last_acc_datetime).append("\">").append(dbUtils.NEWL) ;
    
             result.append(dbUtils.NEWL);
    
             dbModule dbm = new dbModule();
             dbm.mod_res_id = pgr_res_id;
             dbm.res_id = dbm.mod_res_id;
             dbm.tkh_id = pgr_tkh_id;
             dbm.get(con);
    
             // get the test header
             result.append(dbm.getModHeader_test(con,prof,0,true));
     
             result.append("<body>").append(dbUtils.NEWL);
                 
             // Get the list of question in the test
             int i; 
             Vector qList = new Vector(); 
             dbResourceContent rcnObj = null; 
                 
             if (Long.parseLong(que_id_lst[0]) == 0) {
                 qList = dbResourceContent.getChildAss(con,pgr_res_id);
                 if (qList.size() == 0) {
                     qList = dbProgressAttempt.getChildAss(con,pgr_usr_id,pgr_res_id,pgr_attempt_nbr,pgr_tkh_id); 
                 } else {
                     // breakdown the qList for those scenario-type question
                     Vector revisedQList = new Vector();
                     for(i=0;i<qList.size();i++) {
                         dbResourceContent resCon = (dbResourceContent) qList.elementAt(i);
                         dbQuestion que = new dbQuestion();
                         que.que_res_id =  resCon.rcn_res_id_content; 
                         que.res_id = que.que_res_id;
                         que.get(con);
                         if( que.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) || que.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                             Vector childAss = dbResourceContent.getChildAss(con, que.que_res_id);
                             revisedQList.addAll(childAss);
                         } else {
                             revisedQList.addElement(resCon);
                         }
                     }
                     qList = revisedQList;
                 }
             }else {
                 for (i=0;i<que_id_lst.length;i++) {
                     rcnObj = new dbResourceContent();
                     rcnObj.rcn_res_id = pgr_res_id;
                     rcnObj.rcn_res_id_content = Long.parseLong(que_id_lst[i]); 
                     rcnObj.get(con); 
                     if( dbm.mod_type.equalsIgnoreCase(MOD_TYPE_DXT) || dbm.mod_type.equalsIgnoreCase(MOD_TYPE_STX) )
                         rcnObj.rcn_order = (i+1);
                     qList.addElement(rcnObj); 
                 }
             }
             dbResourceContent rcn = null;
                     
             Hashtable qHash = new Hashtable();
             dbQuestion dbq = null;
             //rnady modify
             dbq = new dbQuestion();
             qHash = dbq.get_test(con,qList);
                 
             Hashtable usrAttemptHash = new Hashtable();
             dbProgressAttempt atm = new dbProgressAttempt();
             dbProgressAttempt atm2 = new dbProgressAttempt();
             Vector qAttempt = null;
             
             atm.atm_pgr_usr_id = pgr_usr_id;
             atm.atm_pgr_res_id = pgr_res_id;
             atm.atm_pgr_attempt_nbr = pgr_attempt_nbr;
             atm.atm_tkh_id = pgr_tkh_id;
             
             usrAttemptHash = atm.getQAttempt(con, qHash);
             
             long parent_que_id = 0;
             Long queId = null;
             Vector containerVec = new Vector();
             StringBuffer continer_xml = new StringBuffer();
             continer_xml.append("<container_list>");
             for (i=0;i<qList.size();i++) {
                 if(  !usrAttemptHash.containsKey(new  Long(((dbResourceContent)  qList.elementAt(i)).rcn_res_id_content)))
                     continue;
                 rcn = (dbResourceContent) qList.elementAt(i);
                 queId = new Long(rcn.rcn_res_id_content);
                 dbq = (dbQuestion) qHash.get(queId);
                 
                 parent_que_id = dbq.getContainerQueId(con);
              
                 dbProgressAttempt pgrAtm = new dbProgressAttempt();
                 pgrAtm.atm_pgr_usr_id = pgr_usr_id;
                 pgrAtm.atm_pgr_res_id = pgr_res_id;
                 pgrAtm.atm_int_res_id = dbq.que_res_id;
                 pgrAtm.atm_pgr_attempt_nbr = pgr_attempt_nbr;
                 pgrAtm.get(con,pgr_tkh_id);
                 CommonLog.debug("pgrAtm.atm_order = "+pgrAtm.atm_order +"    rcn.rcn_order="+rcn.rcn_order);
                 result.append(dbq.asXML_test(con, pgrAtm.atm_order,false, 0));
                 
                 qAttempt =  (Vector) usrAttemptHash.get(queId);
                 
                 result.append("<result id=\"").append(dbq.que_res_id).append("\" obj_id=\"").append(rcn.rcn_obj_id_content).append("\">").append(dbUtils.NEWL); 
                 for (int j=0; j<qAttempt.size();j++) {
                     atm2 = (dbProgressAttempt) qAttempt.elementAt(j);
                     result.append("<interaction order=\"").append(atm2.atm_int_order) ;
                     result.append("\" correct=\"").append(atm2.atm_correct_ind) ;
                     result.append("\" flag=\"").append(atm2.atm_flag_ind) ;
                     result.append("\" usr_score=\"").append(atm2.atm_score) ;
                     result.append("\">").append(dbUtils.NEWL);
                     // handle Matching response
                     String[] resps_ = dbUtils.split(atm2.atm_response_bil, dbProgressAttempt.RESPONSE_DELIMITER);
                     if (resps_ != null && resps_.length  > 0 && resps_[0] != null) {
                         for (int k=0; k < resps_.length; k++)
                             result.append("<response>").append(dbUtils.esc4XML(resps_[k])).append("</response>").append(dbUtils.NEWL);
                     }
                     result.append("</interaction>").append(dbUtils.NEWL);
                 }
                 
                 StringBuffer tempContainer = new StringBuffer();
                 if (parent_que_id > 0 && !containerVec.contains(new Long(parent_que_id))) {
                     dbQuestion parentQu = new dbQuestion();
                     parentQu.que_res_id = parent_que_id;
                     parentQu.get(con);
                     if (parentQu.que_type.equals(dbResource.RES_SUBTYPE_FSC)) {
                         FixedScenarioQue fsq = new FixedScenarioQue();
                         fsq.res_id = parentQu.que_res_id;
                         fsq.get(con);
                         continer_xml.append("<container id=\"").append(parentQu.que_res_id).append("\">")
                         .append(fsq.getQueXml()).append("</container>");
                         if (!containerVec.contains(new Long(parent_que_id))) {
                             tempContainer.append("<parent_container que_id=\"").append(dbq.que_res_id).append("\">").append(parent_que_id).append("</parent_container>");
                         }
                         containerVec.add(new Long(parent_que_id));
                     }else if (parentQu.que_type.equals(dbResource.RES_SUBTYPE_DSC)) {
                         DynamicScenarioQue dsq = new DynamicScenarioQue();
                         dsq.res_id = parentQu.que_res_id;
                         dsq.get(con);
                         continer_xml.append("<container id=\"").append(parentQu.que_res_id).append("\">")
                         .append(dsq.getQueXml()).append("</container>");
                         if (!containerVec.contains(new Long(parent_que_id))) {
                             tempContainer.append("<parent_container que_id=\"").append(dbq.que_res_id).append("\">").append(parent_que_id).append("</parent_container>");
                         }
                         containerVec.add(new Long(parent_que_id));
                     }
                 }

                 // Get the statistic of all students
                // result.append(queRespHash.get(queId));
                 //result += QStat.modQueRespAvg(con, pgr_res_id, rcn.rcn_res_id_content, pgr_attempt_nbr, null);
                 result.append("</result>").append(dbUtils.NEWL);
                 result.append(tempContainer);
                 if( dbq.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) || dbq.que_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
                     result.append(getFileSubmissionList(static_env, pgr_res_id, user.usr_ent_id, dbq.que_res_id, pgr_attempt_nbr));
                 }   
             }
             continer_xml.append("</container_list>");
             result.append(continer_xml);            
    
    
             result.append("</body>").append(dbUtils.NEWL);
             result.append("</test>").append(dbUtils.NEWL);
             result.append("</student>").append(dbUtils.NEWL);
             result.append("</student_report>").append(dbUtils.NEWL);
    
             return result.toString();
    
         }catch (SQLException e) {
             throw new qdbException(e.getMessage());
         }
     }

	public String getPgr_usr_id() {
		return pgr_usr_id;
	}

	public void setPgr_usr_id(String pgr_usr_id) {
		this.pgr_usr_id = pgr_usr_id;
	}

	public long getPgr_res_id() {
		return pgr_res_id;
	}

	public void setPgr_res_id(long pgr_res_id) {
		this.pgr_res_id = pgr_res_id;
	}

	public long getPgr_attempt_nbr() {
		return pgr_attempt_nbr;
	}

	public void setPgr_attempt_nbr(long pgr_attempt_nbr) {
		this.pgr_attempt_nbr = pgr_attempt_nbr;
	}

	public float getPgr_score() {
		return pgr_score;
	}

	public void setPgr_score(float pgr_score) {
		this.pgr_score = pgr_score;
	}

	public String getPgr_grade() {
		return pgr_grade;
	}

	public void setPgr_grade(String pgr_grade) {
		this.pgr_grade = pgr_grade;
	}

	public float getPgr_max_score() {
		return pgr_max_score;
	}

	public void setPgr_max_score(float pgr_max_score) {
		this.pgr_max_score = pgr_max_score;
	}

	public String getPgr_rank_bil() {
		return pgr_rank_bil;
	}

	public void setPgr_rank_bil(String pgr_rank_bil) {
		this.pgr_rank_bil = pgr_rank_bil;
	}

	public Timestamp getPgr_schedule_datetime() {
		return pgr_schedule_datetime;
	}

	public void setPgr_schedule_datetime(Timestamp pgr_schedule_datetime) {
		this.pgr_schedule_datetime = pgr_schedule_datetime;
	}

	public Timestamp getPgr_start_datetime() {
		return pgr_start_datetime;
	}

	public void setPgr_start_datetime(Timestamp pgr_start_datetime) {
		this.pgr_start_datetime = pgr_start_datetime;
	}

	public Timestamp getPgr_complete_datetime() {
		return pgr_complete_datetime;
	}

	public void setPgr_complete_datetime(Timestamp pgr_complete_datetime) {
		this.pgr_complete_datetime = pgr_complete_datetime;
	}

	public Timestamp getPgr_last_acc_datetime() {
		return pgr_last_acc_datetime;
	}

	public void setPgr_last_acc_datetime(Timestamp pgr_last_acc_datetime) {
		this.pgr_last_acc_datetime = pgr_last_acc_datetime;
	}

	public String getPgr_status() {
		return pgr_status;
	}

	public void setPgr_status(String pgr_status) {
		this.pgr_status = pgr_status;
	}

	public long getPgr_tkh_id() {
		return pgr_tkh_id;
	}

	public void setPgr_tkh_id(long pgr_tkh_id) {
		this.pgr_tkh_id = pgr_tkh_id;
	}

	public String getPgr_completion_status() {
		return pgr_completion_status;
	}

	public void setPgr_completion_status(String pgr_completion_status) {
		this.pgr_completion_status = pgr_completion_status;
	}

	public long getPgr_correct_cnt() {
		return pgr_correct_cnt;
	}

	public void setPgr_correct_cnt(long pgr_correct_cnt) {
		this.pgr_correct_cnt = pgr_correct_cnt;
	}

	public long getPgr_incorrect_cnt() {
		return pgr_incorrect_cnt;
	}

	public void setPgr_incorrect_cnt(long pgr_incorrect_cnt) {
		this.pgr_incorrect_cnt = pgr_incorrect_cnt;
	}

	public long getPgr_not_score_cnt() {
		return pgr_not_score_cnt;
	}

	public void setPgr_not_score_cnt(long pgr_not_score_cnt) {
		this.pgr_not_score_cnt = pgr_not_score_cnt;
	}
	
}