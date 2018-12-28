package com.cw.wizbank.qdb;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.*;
import com.cw.wizbank.ae.db.DbLearningSoln;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.view.ViewCourseMeasurement;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.utils.CommonLog;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class dbCourseEvaluation
{
    public String CUR_GRP_ID_ALL = "ALL";
    public String CUR_GRP_ID_NONE = "";
    
    public long cov_cos_id;
    public long cov_ent_id;
    public Timestamp cov_last_acc_datetime;
    public Timestamp cov_commence_datetime;
    public float cov_total_time;
    public String cov_status;
    public String cov_score;
    public boolean cov_final_ind;
    public Timestamp cov_complete_datetime;
    public String cov_max_score;
    public String cov_comment;
    public String display_bil;
    public int cov_status_ovrdn_ind;
    public String usr_id, usr_ste_usr_id;
    public Timestamp cov_update_timestamp;
    public long cov_tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;
    
    //store the group info of the user
    public long usg_ent_id;
    public String usg_display_bil;
    public int usg_count;
    
    public float cov_progress;
    
    private static final String SESS_COV_LRN_RPT = "cov_lrn_rpt_";
    private static final String HASH_COS_ID      = "cos_id_";
    private static final String HASH_GROUP_ID    = "group_id_";
    private static final String HASH_ENT_ID_VEC  = "ent_id_vector_";
    
    public final static String SESS_PAGINATION_TIMESTAMP        =   "SESS_PAGINATION_TIMESTAMP";
    public final static String SESS_PAGINATION_USER_TK_RESULT   =   "SESS_PAGINATION_USER_TK_RESULT";
    
    public final static String DATE_RANGE_LAST_ACC = "LAST_ACC";
    public final static String DATE_RANGE_COMMENCE = "COMMENCE";
    public final static String DATE_RANGE_COMPLETE = "COMPLETE";
    public final static String DATE_RANGE_ANY      = "ANY";
    public final static String DATE_RANGE_NOT_SPECIFIED = "NOT_SPECIFIED";
    
    public dbCourseEvaluation() {;		//$$ dbForumTopic1.move(0,0);
}

    public String getSingleLearnerCourseRptAsXML(Connection con, loginProfile prof) throws qdbException{
        try{
            String course_title = null;
			PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.dbCourseEvaluationGetSingleLearnerCourseRptAsXML());
//            PreparedStatement stmt = con.prepareStatement(
//                    " SELECT cov_status, cov_score, res_title, cov_total_time, cov_last_acc_datetime, cov_comment , cov_update_timestamp FROM CourseEvaluation, Resources "
//                  + "    WHERE cov_cos_id " + cwSQL.get_right_join(con) + " res_id AND "
//                  + "       res_id = ? AND"
//                  + "       cov_ent_id = ? " );
            
            stmt.setLong(1, cov_cos_id);
            stmt.setLong(2, cov_ent_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                course_title = rs.getString("res_title");
                cov_score = rs.getString("cov_score");
                cov_status = rs.getString("cov_status");
                cov_total_time = rs.getFloat("cov_total_time");
                cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
                cov_comment = rs.getString("cov_comment"); 
                cov_update_timestamp = rs.getTimestamp("cov_update_timestamp");
            }

            dbRegUser dbRU = new dbRegUser();
            dbRU.usr_ent_id = cov_ent_id;
            dbRU.getByEntId(con);
            display_bil = dbRU.usr_display_bil;
            usr_id = dbRU.usr_id;

            String xml = new String(dbUtils.xmlHeader);
            xml += "<report>" + dbUtils.NEWL;
            xml += prof.asXML() + dbUtils.NEWL;
            xml += "<course id=\"" + cov_cos_id + "\" title=\"" + dbUtils.esc4XML(course_title) + "\" >";
            xml += "<learner_list>";
            xml += "<learner id=\"" + cov_ent_id 
                        + "\" name=\"" + dbUtils.esc4XML(display_bil)
                        + "\" usr_id =\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,usr_id))
                        + "\" status=\"" + cov_status 
                        + "\" score=\"" + cov_score
                        + "\" total_time=\"" + dbAiccPath.getTime(cov_total_time)
                        + "\" last_access=\""  +  cov_last_acc_datetime
                        + "\" comment=\"" + dbUtils.esc4XML(cov_comment)
                        + "\" timestamp=\"" + cov_update_timestamp
                        + "\" />" + dbUtils.NEWL;
            xml += "</learner_list>";
            xml += "</course>";
            xml += "</report>";
            stmt.close();
            return xml;
        }catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }  
    }
    // call by qdbAction
    public String getLearnerCourseRptAsXML(Connection con, HttpSession sess, loginProfile prof, String cur_grp_id, int cur_page, int pagesize) throws qdbException{
        try{

            // no need to parse vtModule
            getResourceAsXML(con);
            
            String xmlBody = new String();
            if(cur_grp_id == null || cur_grp_id.length() == 0) {
                xmlBody = getGroupXML(con);
            }else {
                long group_id = Long.parseLong(cur_grp_id);
                xmlBody = getLearnerXML(con, sess, group_id, cur_page, pagesize); 
            }                
                
            StringBuffer xml = new StringBuffer(2048);
            xml.append(dbUtils.xmlHeader);
            xml.append("<report cur_grp_id=\"").append(cur_grp_id)
               .append("\" cur_page=\"").append(cur_page)
               .append("\" pagesize=\"").append(pagesize).append("\">").append(dbUtils.NEWL);
            xml.append(prof.asXML()).append(dbUtils.NEWL);
            xml.append(start_resource_tag);
            xml.append(xmlBody); 
            xml.append(end_resource_tag);
            xml.append("</report>");

            return xml.toString();
        }catch (qdbErrMessage e){
            throw new qdbException("Error: " + e.getMessage()); 
        }
    }

    private String start_resource_tag = "";
    private String end_resource_tag = "";
    
    public void getResourceAsXML(Connection con) throws qdbErrMessage, qdbException{
        try{
            String course_title = null;
            long cos_itm_id = 0;
            PreparedStatement stmt = con.prepareStatement(                      
                    " SELECT "
                    + " res_id, res_title, cos_itm_id "
                    + " FROM Resources, Course WHERE " 
                    + " res_id = ? AND cos_res_id = res_id ");

            stmt.setLong(1, cov_cos_id);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next())
            {
                cos_itm_id = rs.getLong("cos_itm_id");
                course_title = rs.getString("res_title");
            }
    
            start_resource_tag = "<course id=\"" + cov_cos_id + "\" itm_id=\"" + cos_itm_id + "\" title=\"" + dbUtils.esc4XML(course_title) + "\" >" + dbUtils.NEWL;
            try{
               String s = aeItem.getNavAsXML(con,cos_itm_id).toString();
               end_resource_tag = s + "</course>" + dbUtils.NEWL; 
            }catch(Exception e){
            	throw new qdbException (e.getMessage());     
            }
            stmt.close();

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }  
    }
    
    private String getGroupXML(Connection con) throws qdbErrMessage, qdbException{
        try{
			String SQL  = OuterJoinSqlStatements.dbCourseEvaluationGetGroupXML();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, cov_cos_id);
            stmt.setLong(2, cov_cos_id);
            stmt.setBoolean(3, true);

            ResultSet rs = stmt.executeQuery();
            
            StringBuffer xmlBuf = new StringBuffer(1024);
            
            while (rs.next())
            {
                xmlBuf.append("<group id=\"").append(rs.getLong("usg_ent_id")).append("\"");
                xmlBuf.append(" title=\"").append(dbUtils.esc4XML(rs.getString("usg_display_bil"))).append("\"");
                xmlBuf.append(" count=\"").append(rs.getInt("s")).append("\">").append(dbUtils.NEWL);
                xmlBuf.append("</group>").append(dbUtils.NEWL);
            }
            stmt.close();
            
            return xmlBuf.toString();
            
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }      
    } 

    private String getLearnerXML(Connection con, HttpSession sess, long group_id, int cur_page, int pagesize) throws qdbErrMessage, qdbException{
        try{
            Vector vtLearnerReport = new Vector(); 
            Hashtable data = null;
            boolean useSess = false;
            if (sess !=null) {
                data = (Hashtable) sess.getAttribute(SESS_COV_LRN_RPT);
                if (data !=null) {
                    long groupID = ((Long) data.get(HASH_GROUP_ID)).longValue();
                    long cosID = ((Long) data.get(HASH_COS_ID)).longValue();
                    if (cosID==cov_cos_id && groupID == group_id) {
                        useSess = true;
                    }
                }
            }

            int start = ((cur_page-1)*pagesize) + 1; 
            int end = cur_page * pagesize;
            
            String SESS_SQL = new String();
            Vector sessIdVec = null;
            if (useSess) {
                String ent_id_lst = new String();
                sessIdVec = (Vector)data.get(HASH_ENT_ID_VEC);
                for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
                    if (i!=start) {
                        ent_id_lst += ",";
                    }
                    ent_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
                }
                
                if (ent_id_lst.length()==0) 
                    ent_id_lst = "-1";
                    
                SESS_SQL = " AND enr_ent_id IN (" + ent_id_lst + ")";
            }
              
			String SQL = OuterJoinSqlStatements.dbCourseEvaluationGetLearnerXML();
            if (useSess) {
                    SQL += SESS_SQL;
            }                    
                        
            SQL += " ORDER BY usr_display_bil, usr_ste_usr_id ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);   

            stmt.setLong(1, cov_cos_id);
            stmt.setLong(2, cov_cos_id);
            stmt.setLong(3, group_id);
            stmt.setBoolean(4, true);
            
            ResultSet rs = stmt.executeQuery();
            
            Vector curIdVec = new Vector();
            StringBuffer learnerBuf = new StringBuffer();
            int cnt = 0;
            while (rs.next())
            {
                cnt ++;
                if (useSess || (cnt >= start && cnt <= end)) {
                    dbCourseEvaluation cov = new dbCourseEvaluation();
                    cov.cov_cos_id = cov_cos_id;
                    cov.cov_ent_id = rs.getLong("usr_ent_id");
                    cov.display_bil = rs.getString("usr_display_bil");
                    cov.usr_id = rs.getString("usr_id");
                    cov.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                    cov.cov_status = rs.getString("cov_status");
                    cov.cov_score = rs.getString("cov_score");
                    cov.cov_total_time = rs.getFloat("cov_total_time");
                    cov.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");                
                    cov.cov_update_timestamp = rs.getTimestamp("cov_update_timestamp");
                    usg_display_bil = rs.getString("usg_display_bil");

                    learnerBuf.append("<user id=\"").append(cov.cov_ent_id); 
                    learnerBuf.append("\" name=\"").append(dbUtils.esc4XML(cov.display_bil)); 
                    learnerBuf.append("\" usr_id=\"").append(dbUtils.esc4XML(cov.usr_ste_usr_id)); 
                    learnerBuf.append("\" status=\"").append(cov.cov_status); 
                    learnerBuf.append("\" score=\"").append(cov.cov_score);
                    learnerBuf.append("\" total_time=\"").append(dbAiccPath.getTime(cov.cov_total_time));
                    learnerBuf.append("\" last_access=\"").append(cov.cov_last_acc_datetime); 
                    learnerBuf.append("\" timestamp=\"").append(cov.cov_update_timestamp); 
                    learnerBuf.append("\" />").append(dbUtils.NEWL);

                }
                curIdVec.addElement(new Long(rs.getLong("usr_ent_id")));
            }
            
            int count =0;
            if (useSess) {
                count = sessIdVec.size();
            }else {
                count = curIdVec.size();
                Hashtable curData = new Hashtable();
                curData.put(HASH_COS_ID, new Long(cov_cos_id));
                curData.put(HASH_GROUP_ID, new Long(group_id));
                curData.put(HASH_ENT_ID_VEC, curIdVec);
                sess.setAttribute(SESS_COV_LRN_RPT, curData);
            }

            StringBuffer xmlBuf = new StringBuffer();
            xmlBuf.append("<group id=\"").append(group_id).append("\" title=\"")
                  .append(dbUtils.esc4XML(usg_display_bil)).append("\" count=\"").append(count)
                  .append("\">").append(dbUtils.NEWL);
            xmlBuf.append(learnerBuf.toString());
            xmlBuf.append("</group>");
            
            stmt.close();
            
            return xmlBuf.toString();
            
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }      
    } 

    public void save(Connection con) throws cwException, cwSysMessage, SQLException{
    	get(con);
        save(con, null, false, null,false,this.cov_progress);        
    }
    // Get the module list pf a course and then check if all the modules are completed
    // Three status for Course -- COMPLETE, IN COMPLETE, NOT ATTEMPTED
    // change total time. last_acc_time
    public void save(Connection con, String inStatus, boolean changeAttendance, String update_usr_id,boolean setAttDate, float cov_progress)
        throws cwException, cwSysMessage
    {
        try {
            // Get the module list from ResourceContent
            boolean hasRecord = get(con);
            Vector rcnVec = dbResourceContent.getChildAss(con, cov_cos_id);
            
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT mov_mod_id,  mov_total_time, mov_last_acc_datetime "
                  + "        ,mov_status, mov_score  FROM ModuleEvaluation "
                  + "    WHERE mov_cos_id = ? "
                  + "      AND mov_ent_id = ? " 
                  + "      AND mov_tkh_id = ? " );
            
            stmt.setLong(1, cov_cos_id);
            stmt.setLong(2, cov_ent_id);
            stmt.setLong(3, cov_tkh_id);
            // Check if any record exist
            // if not, the course has not been attempted yet
            int cnt = 0;

            cov_total_time = 0;
            cov_last_acc_datetime = new Timestamp(0);
            Vector completedMod = new Vector();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cnt ++;
                if (rs.getString("mov_status").equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE))
                    completedMod.addElement(new Long(rs.getLong("mov_mod_id")));

                cov_total_time += rs.getFloat("mov_total_time");
                
                if ( rs.getTimestamp("mov_last_acc_datetime").after(cov_last_acc_datetime)) 
                    cov_last_acc_datetime = rs.getTimestamp("mov_last_acc_datetime");
                
            }
            
            stmt.close();
            
            // if cov_status not passed in
            // assum the course was completed

        	this.cov_progress=cov_progress;        	   
 
            if (cnt == 0) {
                cov_commence_datetime = null;
                cov_last_acc_datetime = null;
            }else {
                for (int i=0;i<rcnVec.size();i++) {
                    dbResourceContent rcn = (dbResourceContent) rcnVec.elementAt(i);
                    if (!completedMod.contains(new Long(rcn.rcn_res_id_content))) { 
                        break;
                    }
                }
                if (cov_commence_datetime == null){
                    cov_commence_datetime = cwSQL.getTime(con);
                }
            }
            //    three level: inStatus ....originalStatus.....incomplete
            String originalStatus = cov_status;
            if (inStatus != null){
                cov_status = inStatus;
            }else if (originalStatus != null){
                cov_status = originalStatus;
            }else {
                cov_status = dbAiccPath.STATUS_INCOMPLETE;    
            }
            if (cov_status.equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE)){
                if (cov_complete_datetime == null){
                    cov_complete_datetime = getCompleteTime(con);
                }
            }
            // if 
            if (changeAttendance){
                if (update_usr_id==null){
                    throw new cwException("empty update_usr_id in updAttendace from courseEvaluation.");    
                }
                aeAttendance att = new aeAttendance();
                
                att.changeProgressStatus(con, dbCourse.getCosItemId(con, cov_cos_id), cov_ent_id, cov_status, update_usr_id,setAttDate, cov_tkh_id);
            }
            // Check if the  record already exists, do update or insert
            if (hasRecord) {
                upd(con);
            }else {
                ins(con);
            }
        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
        } catch(qdbException e) {
            throw new cwException("qdb Error: " + e.getMessage()); 
        }        
    }

    public void ins(Connection con)
        throws qdbException
    {
        try {
            Timestamp curTime = cwSQL.getTime(con);
            
            cov_update_timestamp = cwSQL.getTime(con);

            PreparedStatement stmt = con.prepareStatement(                      
                    " INSERT INTO CourseEvaluation "
                    + " (cov_cos_id "
                    + " ,cov_ent_id "
                    + " ,cov_commence_datetime "
                    + " ,cov_last_acc_datetime "
                    + " ,cov_total_time "
                    + " ,cov_status "
                    + " ,cov_score "
                    + " ,cov_status_ovrdn_ind "
                    + " , cov_comment "
                    + " , cov_final_ind "
                    + " , cov_complete_datetime "
                    + " , cov_update_timestamp "
                    + " , cov_tkh_id "
                    + " ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) "); 

            stmt.setLong(1, cov_cos_id);
            stmt.setLong(2, cov_ent_id);
            stmt.setTimestamp(3, cov_commence_datetime);
            stmt.setTimestamp(4, cov_last_acc_datetime);
            stmt.setFloat(5, cov_total_time);
            stmt.setString(6, cov_status);
            stmt.setString(7, cov_score);
            stmt.setInt(8, cov_status_ovrdn_ind);
            stmt.setString(9, cov_comment);
            stmt.setBoolean(10, cov_final_ind);
            stmt.setTimestamp(11, cov_complete_datetime);
            stmt.setTimestamp(12, cov_update_timestamp);
            stmt.setLong(13, cov_tkh_id);
            if ( stmt.executeUpdate()!=1)
            {
                stmt.close();
                con.rollback(); 
                throw new qdbException("Failed to insert course evaluation ."); 
            }
            
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

    public void upd(Connection con)
        throws qdbException
    {
        try {
            cov_update_timestamp = cwSQL.getTime(con);

            PreparedStatement stmt = con.prepareStatement(                      
                    " UPDATE CourseEvaluation SET "
                    + " cov_commence_datetime = ? "
                    + " ,cov_last_acc_datetime = ? "
                    + " ,cov_total_time = ? "
                    + " ,cov_status = ? " 
                    + " ,cov_score = ? " 
                    + " ,cov_status_ovrdn_ind = ? " 
                    + " ,cov_comment = ? " 
                    + " ,cov_final_ind = ? " 
                    + " ,cov_complete_datetime = ? "
                    + " ,cov_update_timestamp = ? "
                    + " ,cov_progress=?"
                    + " WHERE " 
                    + " cov_cos_id = ? AND " 
                    + " cov_ent_id = ? AND "
                    + " cov_tkh_id = ? " );

            stmt.setTimestamp(1, cov_commence_datetime);
            stmt.setTimestamp(2, cov_last_acc_datetime);
            stmt.setFloat(3, cov_total_time);
            stmt.setString(4, cov_status);
            stmt.setString(5, cov_score);
            stmt.setInt(6, cov_status_ovrdn_ind);
            stmt.setString(7, cov_comment);
            stmt.setBoolean(8, cov_final_ind);
            stmt.setTimestamp(9, cov_complete_datetime);
            stmt.setTimestamp(10, cov_update_timestamp);
            stmt.setFloat(11, cov_progress);
            stmt.setLong(12, cov_cos_id);
            stmt.setLong(13, cov_ent_id);
            stmt.setLong(14, cov_tkh_id);
            if ( stmt.executeUpdate()!=1)
            {
                stmt.close();
                con.rollback(); 
                throw new qdbException("Failed to update course evaluation ."); 
            }
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
    
   /*
   public void updCommenceDate(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("UPDATE courseEvaluation set cov_commence_datetime = ? where cov_cos_id = ? and cov_ent_id = ? ");
        stmt.setTimestamp(1, cov_commence_datetime);
        stmt.setLong(2, cov_cos_id);
        stmt.setLong(3, cov_ent_id);
        
        if (stmt.executeUpdate()!=1)
            {
                con.rollback(); 
                throw new SQLException("Failed to update course commence datetime."); 
            }
   }
   */
   public static String getProgressStatus(Connection con, long entId ,Vector cosVec)
        throws qdbException
    {
        try {
            StringBuffer xmlBuf = new StringBuffer();
            xmlBuf.append("<aicc_data>").append(dbUtils.NEWL);
            
            String cosLst = dbUtils.vec2list(cosVec);
            
            PreparedStatement stmt = con.prepareStatement(                      
                    " SELECT  cov_cos_id "
                    + "      ,cov_last_acc_datetime "
                    + "      ,cov_update_timestamp "
                    + "      ,cov_total_time "
                    + "      ,cov_status " 
                    + "      ,cov_score " 
                    + "      ,cov_comment " 
                    + "  FROM CourseEvaluation "
                    + " WHERE " 
                    + "   cov_ent_id = ? "
                    + "   AND cov_cos_id IN " + cosLst );

            stmt.setLong(1, entId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int col = 1;
                xmlBuf.append("<attempt course_id=\"").append(rs.getLong(col++));
                xmlBuf.append("\" student_id=\"").append(entId); 
                xmlBuf.append("\" last_acc_datetime=\"");
                
                Timestamp temp = rs.getTimestamp(col++);
                if (!rs.wasNull())
                    xmlBuf.append(temp);
                    
                xmlBuf.append("\" timestamp=\"").append(rs.getTimestamp(col++));
                xmlBuf.append("\" used_time=\"");
                rs.getFloat(col);
                if (!rs.wasNull()) 
                    xmlBuf.append(dbAiccPath.getTime(rs.getFloat(col)));
                
                col++;    
                
                xmlBuf.append("\" status=\"");
                rs.getString(col);
                if (!rs.wasNull()) 
                    xmlBuf.append(rs.getString(col));
                    
                col++;

                xmlBuf.append("\" score=\"");
                rs.getString(col);
                if (!rs.wasNull()) 
                    xmlBuf.append(rs.getString(col));
                
                col++;
                
                xmlBuf.append("\" comment=\"");
                rs.getString(col);
                if (!rs.wasNull()) 
                    xmlBuf.append(dbUtils.esc4XML(rs.getString(col)));
                  
                xmlBuf.append("\"/>").append(dbUtils.NEWL);
            }

            
            xmlBuf.append("</aicc_data>").append(dbUtils.NEWL);
            stmt.close();
            return xmlBuf.toString();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static long attemptNum(Connection con, long cosId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(                      
                    " SELECT  count(cov_cos_id) AS CNT "
                    + "  FROM CourseEvaluation "
                    + " WHERE " 
                    + "  cov_cos_id = ? ");

            stmt.setLong(1, cosId);
            ResultSet rs = stmt.executeQuery();
            
            long cnt = 0;
            if (rs.next()) {
                cnt = rs.getLong("CNT");
            }
            stmt.close();
            
            return cnt;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static Hashtable getCourseEvaluation(Connection con, long ent_id, Vector itm_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer lst = new StringBuffer();
        StringBuffer sql = new StringBuffer();
		aeItem item = new aeItem();
		HashMap itmMap = null;
		Vector new_itm_ids = new Vector();
		if(itm_lst!=null && itm_lst.size()!=0){
		  itmMap = item.transCosItmId(con,itm_lst);
		  new_itm_ids = (Vector)itmMap.get("keys");
		}
       
        lst.append("(0");
        
        for (int i=0,n=new_itm_ids.size(); i<n; i++) {
            lst.append(", " + new_itm_ids.elementAt(i));
        }

        lst.append(")");

        sql.append("SELECT cos_itm_id, cov_cos_id, cov_ent_id, cov_tkh_id, cov_last_acc_datetime, cov_total_time, cov_status, cov_score, cov_comment, cov_update_timestamp FROM CourseEvaluation, Course , TrackingHistory ");
        sql.append(" WHERE cov_ent_id = ? ");
        sql.append(" AND tkh_type = ? ");
        sql.append(" AND cos_itm_id in ");
        sql.append(lst.toString());
        sql.append(" AND cos_res_id = cov_cos_id ");
        sql.append(" AND tkh_id = cov_tkh_id ");
        sql.append(" ORDER BY cov_tkh_id desc, cos_itm_id  ");

        stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, ent_id);
        stmt.setString(2, DbTrackingHistory.TKH_TYPE_APPLICATION);
        
        rs = stmt.executeQuery();
		Long old_itm_id = new Long(0);
        while (rs.next()) {
            dbCourseEvaluation cv = new dbCourseEvaluation();
            Long itm_id = new Long(rs.getLong("cos_itm_id"));
            cv.cov_cos_id = rs.getLong("cov_cos_id");
            cv.cov_ent_id = rs.getLong("cov_ent_id");
            cv.cov_tkh_id = rs.getLong("cov_tkh_id");
            cv.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
            cv.cov_total_time = rs.getFloat("cov_total_time");
            cv.cov_status = rs.getString("cov_status");
            cv.cov_score = rs.getString("cov_score");
            cv.cov_comment = rs.getString("cov_comment");
            cv.cov_update_timestamp  = rs.getTimestamp("cov_update_timestamp");
            //do not remember the duplicate data
			old_itm_id = (Long)itmMap.get(itm_id);
		    if (hash.get(old_itm_id) == null){
			  hash.put(old_itm_id, cv);
		    }
        }
        
        stmt.close();
        
        return hash;
    }
    
	public static Hashtable getCourseEvaluation4Plan(Connection con, long ent_id, Vector itm_lst) throws SQLException {
			Hashtable hash = new Hashtable();
			Hashtable appHash = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			StringBuffer lst = new StringBuffer();
			StringBuffer sql = new StringBuffer();
			aeItem item = new aeItem();
            HashMap itmMap = null;
			Vector new_itm_ids = new Vector();
			if(itm_lst!=null && itm_lst.size()!=0){
			  itmMap = item.transCosItmId(con,itm_lst);
			  new_itm_ids = (Vector)itmMap.get("keys");
			}
			lst.append("(0");
        
			for (int i=0,n=new_itm_ids.size(); i<n; i++) {
				lst.append(", " + new_itm_ids.elementAt(i));
			}

			lst.append(")");

			sql.append("SELECT cos_itm_id, cov_cos_id, cov_ent_id, cov_tkh_id, cov_last_acc_datetime, cov_total_time, cov_status, cov_score, cov_comment, cov_update_timestamp FROM CourseEvaluation, Course , TrackingHistory ");
			sql.append(" WHERE cov_ent_id = ? ");
			sql.append(" AND tkh_type = ? ");
			sql.append(" AND cos_itm_id in ");
			sql.append(lst.toString());
			sql.append(" AND cos_res_id = cov_cos_id ");
			sql.append(" AND tkh_id = cov_tkh_id ");
			sql.append(" ORDER BY cov_tkh_id desc, cos_itm_id  ");

			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, ent_id);
			stmt.setString(2, DbTrackingHistory.TKH_TYPE_APPLICATION);
        
			rs = stmt.executeQuery();
			Long old_itm_id = new Long(0);
			while (rs.next()) {
				dbCourseEvaluation cv = new dbCourseEvaluation();
				//Long itm_id = new Long(rs.getLong("cos_itm_id"));
				Long tkh_id = new Long(rs.getLong("cov_tkh_id"));
                cv.cov_cos_id = rs.getLong("cov_cos_id");
				cv.cov_ent_id = rs.getLong("cov_ent_id");
				cv.cov_tkh_id = rs.getLong("cov_tkh_id");
				cv.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
				cv.cov_total_time = rs.getFloat("cov_total_time");
				cv.cov_status = rs.getString("cov_status");
				cv.cov_score = rs.getString("cov_score");
				cv.cov_comment = rs.getString("cov_comment");
				cv.cov_update_timestamp  = rs.getTimestamp("cov_update_timestamp");
                hash.put(tkh_id, cv);
				//do not remember the duplicate data
                /*
				old_itm_id = (Long)itmMap.get(itm_id);
				if ((appHash=(Hashtable)hash.get(old_itm_id)) == null){
				   appHash = new Hashtable();
				   appHash.put(itm_id,cv);
				   hash.put(old_itm_id, appHash);
				}else{
					appHash.put(itm_id,cv);
				}
                */
			}
        
			stmt.close();
        
			return hash;
		}

    public long getTkhId(Connection con) throws SQLException{
        String SQL = "SELECT max(cov_tkh_id) tkh_id FROM courseEvaluation WHERE cov_cos_id = ? AND cov_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cov_cos_id);
        stmt.setLong(2, cov_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()){
            cov_tkh_id = rs.getLong("tkh_id");
        }else{
            cov_tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;
        }
        stmt.close();
        return cov_tkh_id; 
    }
    
    public boolean get(Connection con) throws SQLException{
        String SQL = "SELECT cov_cos_id,  cov_ent_id,  cov_commence_datetime, cov_last_acc_datetime, cov_total_time, cov_status, cov_status_ovrdn_ind, cov_score, cov_max_score, cov_comment, cov_final_ind, cov_complete_datetime, cov_update_timestamp,cov_progress FROM courseEvaluation WHERE cov_cos_id = ? AND cov_ent_id = ? AND cov_tkh_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cov_cos_id);
        stmt.setLong(2, cov_ent_id);
        stmt.setLong(3, cov_tkh_id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()){
            cov_commence_datetime = rs.getTimestamp("cov_commence_datetime");
            cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
            cov_total_time = rs.getFloat("cov_total_time");
            cov_status = rs.getString("cov_status");
            cov_status_ovrdn_ind = rs.getInt("cov_status_ovrdn_ind");
            cov_score = rs.getString("cov_score");
            cov_max_score = rs.getString("cov_max_score");
            cov_comment = rs.getString("cov_comment");
            cov_final_ind = rs.getBoolean("cov_final_ind");
            cov_complete_datetime = rs.getTimestamp("cov_complete_datetime");
            cov_update_timestamp = rs.getTimestamp("cov_update_timestamp");
            cov_progress = rs.getFloat("cov_progress");
            rs.close();
            stmt.close();
            return true;    
        }else{
        	if(rs!=null)
        	rs.close();
            stmt.close();
            return false;    
        }
    }
    
    public Timestamp getCompleteTime(Connection con) throws SQLException{

        Timestamp complete_datetime = null;
        String SQL = "select max(mov_last_acc_datetime) as complete_datetime from courseModuleCriteria, moduleEvaluation where mov_mod_id = cmr_res_id " 
            + " and mov_cos_id = ? and mov_ent_id = ? AND mov_tkh_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cov_cos_id);
        stmt.setLong(2, cov_ent_id);
        stmt.setLong(3, cov_tkh_id);

        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()){
            complete_datetime = rs.getTimestamp("complete_datetime");
        }
        
        if (complete_datetime == null){
            complete_datetime = cwSQL.getTime(con);                
        }
        rs.close();
        stmt.close();
        return complete_datetime;
    }
 
    private static final String sql_del =
        " Delete From CourseEvaluation Where cov_cos_id = ? and cov_ent_id = ? and cov_tkh_id = ? ";
    /**
    Delete one CourseEvaluation record
    */
    public void del(Connection con) throws SQLException {
    	CommonLog.debug("cos: " + cov_cos_id);
    	CommonLog.debug("ent: " + cov_ent_id);
    	CommonLog.debug("tkh: " + cov_tkh_id);
        PreparedStatement stmt = con.prepareStatement(sql_del);
        stmt.setLong(1, cov_cos_id);
        stmt.setLong(2, cov_ent_id);
        stmt.setLong(3, cov_tkh_id);
        int tmp = stmt.executeUpdate();
        
        CommonLog.debug("in del, del count:" + tmp);
        stmt.close();
        return;
    }
 
    private static final String sql_del_by_cos =
        " Delete From CourseEvaluation Where cov_cos_id = ? ";
    /**
    Delete all CourseEvaluation records of a Course
    */
    public static void delByCos(Connection con, long cos_id) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(sql_del_by_cos);
        stmt.setLong(1, cos_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    // Enhancement
    public String getUserTrackingRptAsXML(Connection con, HttpSession sess, String rpt_type, String date_type, Timestamp start_datetime, Timestamp end_datetime, String rpt_search_full_name,cwPagination cwPage, WizbiniLoader wizbini) throws qdbException{
        try{
            // no need to parse vtModule
            getResourceAsXML(con);
            String xmlBody = getLearnerXML(con, sess, rpt_type, date_type, start_datetime, end_datetime, rpt_search_full_name, cwPage, wizbini); 
            StringBuffer xml = new StringBuffer(2048);
            xml.append(start_resource_tag);
            xml.append(xmlBody); 
            xml.append(end_resource_tag);
            return xml.toString();
        }catch (qdbErrMessage e){
            throw new qdbException("Error: " + e.getMessage()); 
        }
    }
    
    private String getLearnerXML(Connection con, HttpSession sess, String rpt_type, String date_type, Timestamp start_datetime, Timestamp end_datetime,String rpt_search_full_name, cwPagination cwPage, WizbiniLoader wizbini) throws qdbErrMessage, qdbException{
        try{
            Vector sessIdVec = null;
            boolean useSess = false;
            Timestamp ts = (Timestamp)sess.getAttribute(SESS_PAGINATION_TIMESTAMP);
            Vector resultVec = null;
            if (cwPage.ts == null) {
                cwPage.ts = cwSQL.getTime(con);
            }
            if (cwPage.curPage == 0) {
                cwPage.curPage = 1;
            }
            if (cwPage.pageSize == 0) {
                cwPage.pageSize = 10;
            }
            
            if( ts != null && ts.equals(cwPage.ts)) {
                sessIdVec = (Vector)sess.getAttribute(SESS_PAGINATION_USER_TK_RESULT);
                if (sessIdVec != null) {
                    useSess = true;
                }
            } 
            
            int start = ((cwPage.curPage-1)*cwPage.pageSize) + 1; 
            int end = cwPage.curPage * cwPage.pageSize;
            
            String SESS_SQL = new String();
            String tkh_id_lst = new String();
            if (useSess) {
                for (int i=start ; i<= sessIdVec.size() && (i <= end);i++) {
                    if (i!=start) {
                        tkh_id_lst += ",";
                    }
                    tkh_id_lst += ((Long) sessIdVec.elementAt(i-1)).longValue();
                }
                
                if (tkh_id_lst.length()==0) 
                    tkh_id_lst = "-1";
                    
                SESS_SQL = " AND tkh_id IN (" + tkh_id_lst + ")";
            }

			String SQL = OuterJoinSqlStatements.dbCourseEvaluationGetLearnerXML2();

            if (useSess) {
                    SQL += SESS_SQL;
            }
            
            if(rpt_search_full_name!=null && rpt_search_full_name.length()>0) {
            	rpt_search_full_name = " '%" + rpt_search_full_name.trim().toLowerCase() +"%' ";
            	SQL += " AND usr_display_bil like " + rpt_search_full_name;
            }
            
            if (cwPage.sortCol==null && cwPage.sortOrder==null) {
                SQL += " ORDER BY usr_display_bil, tkh_id ";            
            }
            else 
            if (cwPage.sortCol!=null && cwPage.sortCol.equals("r_name")) {
                SQL += " ORDER BY usr_display_bil ";
                if (cwPage.sortOrder!=null) {
                    SQL += cwPage.sortOrder;
                }
            }
            /*
            else
            if (cwPage.sortCol!=null && cwPage.sortCol.equals("r_group")) {
                SQL += " ORDER BY gpm_group_name ";
                if (cwPage.sortOrder!=null) {
                    SQL += cwPage.sortOrder;
                } 
            }
            */
            else
            if (cwPage.sortCol!=null && cwPage.sortCol.equals("r_status")) {
                SQL += " ORDER BY cov_status ";
                if (cwPage.sortOrder!=null) {
                    SQL += cwPage.sortOrder;
                }
            }
            else
            if (cwPage.sortCol!=null && cwPage.sortCol.equals("r_total_time")) {
                SQL += " ORDER BY cov_total_time ";
                if (cwPage.sortOrder!=null) {
                    SQL += cwPage.sortOrder;
                }
            }
            else
            if (cwPage.sortCol!=null && cwPage.sortCol.equals("r_last_access")) {
                SQL += " ORDER BY cov_last_acc_datetime ";
                if (cwPage.sortOrder!=null) {
                    SQL += cwPage.sortOrder;
                }
            }
            else
            if (cwPage.sortCol!=null && cwPage.sortCol.equals("r_score")) {
                SQL += " ORDER BY cov_score ";
                if (cwPage.sortOrder!=null) {
                    SQL += cwPage.sortOrder;
                }
            }

            int index = 1;
            Timestamp curTime = cwSQL.getTime(con);
            PreparedStatement stmt = con.prepareStatement(SQL);   
            stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, cov_cos_id);
            stmt.setString(index++, rpt_type);
            
            ResultSet rs = stmt.executeQuery();
            
            Vector curIdVec = new Vector();
            StringBuffer learnerBuf = new StringBuffer();
            int cnt = 0;
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            while (rs.next())
            {
            	long usrEntId = rs.getLong("usr_ent_id");
                boolean validRec = false;
                boolean validCommence = true;
                boolean validLastAcc = true;
                boolean validComplete = true;
                
                dbCourseEvaluation cov = new dbCourseEvaluation();
                cov.cov_cos_id = cov_cos_id;
                cov.cov_commence_datetime = rs.getTimestamp("cov_commence_datetime");
                cov.cov_complete_datetime = rs.getTimestamp("cov_complete_datetime");
                cov.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
                
                if (useSess || date_type.equalsIgnoreCase(DATE_RANGE_NOT_SPECIFIED)){
                    validRec = true;
                }else {
                    if (date_type.equalsIgnoreCase(DATE_RANGE_ANY) ||
                        date_type.equalsIgnoreCase(DATE_RANGE_COMMENCE)) {
                        if (cov.cov_commence_datetime != null) {
                            if ((start_datetime != null && cov.cov_commence_datetime.before(start_datetime)) ||
                                (end_datetime != null && cov.cov_commence_datetime.after(end_datetime))
                                ) {
                                validCommence = false;
                            }
                        }else {
                            validCommence = false;
                        }
                        
                        if (date_type.equalsIgnoreCase(DATE_RANGE_COMMENCE)) {
                            validRec = validCommence;                    
                        }
                    }

                    if (date_type.equalsIgnoreCase(DATE_RANGE_ANY) ||
                        date_type.equalsIgnoreCase(DATE_RANGE_LAST_ACC)) {
                        if (cov.cov_last_acc_datetime != null) {
                            if ((start_datetime != null && cov.cov_last_acc_datetime.before(start_datetime)) ||
                                (end_datetime != null && cov.cov_last_acc_datetime.after(end_datetime))){
                                validLastAcc = false;
                            }
                        }else {
                            validLastAcc = false;
                        }
                        
                        if (date_type.equalsIgnoreCase(DATE_RANGE_LAST_ACC)) {
                            validRec = validLastAcc;                    
                        }
                    }
                    
                    if (date_type.equalsIgnoreCase(DATE_RANGE_ANY) ||
                        date_type.equalsIgnoreCase(DATE_RANGE_COMPLETE)) {
                        if (cov.cov_complete_datetime != null) {
                            if ((start_datetime != null && cov.cov_complete_datetime.before(start_datetime)) ||
                                (end_datetime != null && cov.cov_complete_datetime.after(end_datetime))) {
                                validComplete = false;
                            }
                        }else {
                            validComplete = false;
                        }
                        
                        if (date_type.equalsIgnoreCase(DATE_RANGE_COMPLETE)) {
                            validRec = validComplete;                    
                        }
                    }

                    if (date_type.equalsIgnoreCase(DATE_RANGE_ANY) &&
                        (validCommence || validLastAcc || validComplete)) {
                        validRec = true; 
                    }
                }
                
                String usr_full_path;
                // show org name before full path when multiple organzation is allowed
                boolean bShowOrgName = wizbini.cfgSysSetupadv.getOrganization().isMultiple();
                String fullPathSeparator = wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator();
                if (validRec) {
                    cnt ++;
                    if (useSess || (cnt >= start && cnt <= end)) {
                        cov.cov_ent_id = rs.getLong("usr_ent_id");
                        cov.cov_tkh_id = rs.getLong("tkh_id");
                        cov.display_bil = rs.getString("usr_display_bil");
                        cov.usr_id = rs.getString("usr_id");
                        cov.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                        cov.cov_status = rs.getString("cov_status");
                        cov.cov_score = rs.getString("cov_score");
                        cov.cov_total_time = rs.getFloat("cov_total_time");
                        cov.cov_update_timestamp = rs.getTimestamp("cov_update_timestamp");
                        long ancestor_id = rs.getLong("ern_ancestor_ent_id");
                        String groupName = entityfullpath.getEntityName(con, ancestor_id);
                        learnerBuf.append("<user id=\"").append(cov.cov_ent_id); 
                        
                        usr_full_path = dbEntityRelation.getFullPath(con, cov.cov_ent_id);
                        if (bShowOrgName){
                            usr_full_path = rs.getString("ste_name") + fullPathSeparator + usr_full_path;
                        }

                        learnerBuf.append("\" full_path=\"").append(dbUtils.esc4XML(usr_full_path));
                        learnerBuf.append("\" name=\"").append(dbUtils.esc4XML(cov.display_bil)); 
                        learnerBuf.append("\" usg_display_bil=\"").append(dbUtils.esc4XML(groupName));
                        learnerBuf.append("\" usr_id=\"").append(dbUtils.esc4XML(cov.usr_ste_usr_id)); 
                        learnerBuf.append("\" status=\"").append(cov.cov_status); 
                        learnerBuf.append("\" score=\"").append(cov.cov_score);
                        learnerBuf.append("\" total_time=\"").append(dbAiccPath.getTime(cov.cov_total_time));
                        learnerBuf.append("\" commence_datetime=\"").append(cov.cov_commence_datetime); 
                        learnerBuf.append("\" last_access=\"").append(cov.cov_last_acc_datetime);
                        learnerBuf.append("\" complete_datetime=\"").append(cov.cov_complete_datetime); 
                        learnerBuf.append("\" timestamp=\"").append(cov.cov_update_timestamp); 
                        learnerBuf.append("\" tkh_id=\"").append(cov.cov_tkh_id); 
                        learnerBuf.append("\" />").append(dbUtils.NEWL);

                    }
                    curIdVec.addElement(new Long(rs.getLong("tkh_id")));
                }
            }
            stmt.close();
            
            if (useSess) {
                cwPage.totalRec = sessIdVec.size();
            }else {
                cwPage.totalRec = curIdVec.size();
                sess.setAttribute(SESS_PAGINATION_TIMESTAMP, cwPage.ts);
                sess.setAttribute(SESS_PAGINATION_USER_TK_RESULT, curIdVec);
            }
            cwPage.totalPage = (int)Math.ceil( ((double)cwPage.totalRec)/cwPage.pageSize );
            StringBuffer xmlBuf = new StringBuffer();
            xmlBuf.append(cwPage.asXML().toString());
            xmlBuf.append("<user_list report_type=\"").append(rpt_type)
                  .append("\" date_range=\"").append(date_type)
                  .append("\" start_datetime=\"").append(start_datetime)
                  .append("\" end_datetime=\"").append(end_datetime)
                  .append("\">");
            xmlBuf.append(learnerBuf.toString());
            xmlBuf.append("</user_list>");
            
            return xmlBuf.toString();
            
        } catch(SQLException e) {
        	CommonLog.error(e.getMessage(),e);
                throw new qdbException("SQL Error: " + e.getMessage()); 
        }      
    } 

    /**
    Save the last access time for this CourseEvaluation record.
    If the record does not exist, create a new one.
    Pre-define variables:
        cov_cos_id
        cov_ent_id
        cov_tkh_id
    @param con Connection to database
    */
    /*
    public void saveLastAccessTime(Connection con, Timestamp time) throws SQLException, qdbException {
        
        //check if the record exists
        boolean hasRecord = get(con);

        cov_last_acc_datetime = time;
        cov_update_timestamp = time;
        if(hasRecord) {
            if(cov_commence_datetime == null) {
                cov_commence_datetime = time;
            }
            upd(con);
        } else {
            cov_commence_datetime = time;
            ins(con);
        }
        return;
    }
    */
    
    public static Float getCosScoreByItmId(Connection con,long itm_id) throws SQLException{
        Float cov_score = null;
        String sql = "select cov_score from CourseEvaluation where cov_cos_id = " +
            "(select cos_res_id from Course where  cos_itm_id = ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,itm_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
             float f = rs.getFloat(1);
             if(rs.wasNull()){
                 cov_score = null;
             } else {
                 cov_score = new Float(f);
             }
        }
        pstmt.close();
        return cov_score;
    }
    
    public static String getCovStatus(Connection con, long tkh_id) throws SQLException{
    	String sql="select cov_status from CourseEvaluation where cov_tkh_id=?";
    	PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,tkh_id);
        String cov_status=null;
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
        	cov_status = rs.getString("cov_status");
        }
        if(pstmt !=null) pstmt.close();
        return cov_status;
    }
    
    public static void updateCosProgress(Connection con, long covCosId, long covEntId, long covTkhId, float covProgress) throws SQLException {
    	PreparedStatement stmt = null;
    	try{
    		stmt = con.prepareStatement(SqlStatements.updateCovProgress());
    		int index = 1;
    		stmt.setFloat(index++, covProgress);
    		stmt.setLong(index++, covCosId);
    		stmt.setLong(index++, covEntId);
    		stmt.setLong(index++, covTkhId);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    }

    public static void updCourseEvaluation(Connection con, loginProfile prof, long itm_id, long app_id) throws SQLException, qdbException, qdbErrMessage, cwSysMessage, cwException {
        long cos_id = dbCourse.getCosResId(con, itm_id);
        long tkh_id = aeApplication.getTkhId(con, app_id);
        long usr_ent_id = prof.usr_ent_id;
        Timestamp cur_time = cwSQL.getTime(con);
        Timestamp last_acc_time = cur_time;

        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
        ccr.getCcrIdByItmNType(con);
        ccr.get(con);

        dbCourseEvaluation cov = new dbCourseEvaluation();
        String cov_status_old = null;
        cov.cov_cos_id = cos_id;
        cov.cov_ent_id = usr_ent_id;
        cov.cov_tkh_id = tkh_id;
        boolean hasACovRecord = cov.get(con);
        if (hasACovRecord) {
            cov_status_old = cov.cov_status;
        }

        aeAttendance att = new aeAttendance();
        att.att_itm_id = itm_id;
        att.att_app_id = app_id;
        boolean hasAttRecord = att.get(con);

        List cmtLst = ViewCourseMeasurement.getRelateMeasurement(con, ccr.ccr_id);

        boolean hasCriteriaOrScore = true;

        // 如果没有设置完成准则或积分项目，则跳过完成准则检查和计算分数项目
        if (cmtLst.size() == 0 && ccr.ccr_attendance_rate == 0 && ccr.ccr_pass_score == 0) {
            hasCriteriaOrScore = false;
        }
        if (hasCriteriaOrScore) {
            // 与结训条件相关,则更检查完成准则
            if (cov.cov_status == null || cov.cov_status.equals(dbAiccPath.STATUS_INCOMPLETE)) {
                boolean pass_score = true;
                boolean pass_att = true;
                boolean pass_cond = true;
                if (ccr.ccr_pass_ind) {
                    if (cov.cov_score == null || (new Float(cov.cov_score).floatValue() < ccr.ccr_pass_score)) {
                        pass_score = false;
                    }
                }
                if (ccr.ccr_attendance_rate > 0) {
                    if (att.att_rate == null || new Float(att.att_rate).floatValue() < ccr.ccr_attendance_rate) {
                        pass_att = false;
                    }
                }
                if (ccr.ccr_all_cond_ind) {
                    if (!CourseCriteria.checkAllMeasurement(con, ccr, cmtLst, usr_ent_id, tkh_id)) {
                        pass_cond = false;
                    }
                }

                if (pass_score && pass_att && pass_cond) {
                    cov.cov_status = dbAiccPath.STATUS_COMPLETE;
                } else {
                    if (cov.cov_status == null) {
                        cov.cov_status = dbAiccPath.STATUS_INCOMPLETE;
                    }
                }

                float cur_score = 0;
                float cur_rate = 0;
                if (cov.cov_score != null && cov.cov_score.length() > 0) {
                    cur_score = new Float(cov.cov_score).floatValue();
                }
                if (att.att_rate != null && att.att_rate.length() > 0) {
                    cur_rate = new Float(att.att_rate).floatValue();
                }

                float cov_progress = CourseCriteria.getCovProgress(ccr.ccr_pass_ind, ccr.ccr_attendance_rate, cur_score, ccr.ccr_pass_score_f, cur_rate, ccr.ccr_attendance_rate, ccr.passCondCnt, ccr.allCondCnt);
                cov.cov_progress = cov_progress;
            }
        }

        if (cov.cov_commence_datetime == null) {
            cov.cov_commence_datetime = cur_time;
        }

        if(last_acc_time != null){
            if(cov.cov_last_acc_datetime == null){
                cov.cov_last_acc_datetime = last_acc_time;
            }else if(last_acc_time.after(cov.cov_last_acc_datetime)){
                cov.cov_last_acc_datetime = last_acc_time;
            }
        }
        // three level: inStatus ....originalStatus.....incomplete
        if (cov.cov_status == null) {
            cov.cov_status = dbAiccPath.STATUS_INCOMPLETE;
        }
        if (cov.cov_status.equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE)) {
            if (cov.cov_complete_datetime == null) {
                cov.cov_complete_datetime = cur_time;

            }
        }
        if (cov_status_old != null && !cov.cov_status.equals(cov_status_old) || !hasACovRecord) {

            if (cov.cov_status.equals(dbAiccPath.STATUS_COMPLETE) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(cov.cov_status)) {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_ATTEND);
            } else if (dbAiccPath.STATUS_INCOMPLETE.equalsIgnoreCase(cov.cov_status)) {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
            } else {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_INCOMPLETE);
            }
            if (cov.cov_status.equals(dbAiccPath.STATUS_COMPLETE) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(cov.cov_status)) {
                DbLearningSoln soln = new DbLearningSoln();
                soln.lsn_ent_id = usr_ent_id;
                aeItemRelation myAeItemRelation = new aeItemRelation();
                myAeItemRelation.ire_child_itm_id = itm_id;
                long parentID = myAeItemRelation.getParentItemId(con);

                if (parentID > 0) {
                    soln.lsn_itm_id = parentID;
                } else {
                    soln.lsn_itm_id = att.att_itm_id;
                }

                if (DbLearningSoln.isExist(con, soln.lsn_itm_id, usr_ent_id)) {
                    long grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
                    soln.disable(con, grade_ent_id, -1, cur_time, att.att_update_timestamp);
                }
            }

            att.att_update_usr_id = prof.usr_id;
            if (cov.cov_status.equals(dbAiccPath.STATUS_INCOMPLETE)) {
                att.att_timestamp = null;
            } else {
                att.att_timestamp = att.getAttTimestamp(con);
            }
            att.att_update_timestamp = cur_time;
            if (hasAttRecord) {
                att.updStatus(con);
            } else {
                att.ins(con);
            }
        }

        // Check if the record already exists, do update or insert
        if (hasACovRecord) {
            cov.upd(con);
        } else {
            cov.ins(con);
        }

        //如果开启了CPD模式
        if(AccessControlWZB.hasCPDFunction()){
            //学员完成课程添加获得时数
            if(dbAiccPath.STATUS_COMPLETE.equals(cov.cov_status)){
                CpdUtilService cpdUtilService = new CpdUtilService();
                aeItem itm = aeItem.getItemById(con, itm_id);
                AeItemCPDItem aeItemCPDItem = AeItemCPDItemService.getByItmIdForOld(con, itm_id);
                if(null!=aeItemCPDItem){
                    Long itm_ref_ind = 0l;
                    if(itm.itm_ref_ind){
                        itm_ref_ind =1l;
                    }
                    cpdUtilService.calAwardHoursForOld(itm_id,itm_ref_ind,itm.itm_type, app_id,usr_ent_id,
                            aeItemCPDItem.getAci_hours_end_date(), cov.cov_total_time,
                            att.att_timestamp, CpdUtils.AWARD_HOURS_ACTION_LRN_AW, prof.getUsr_ent_id(), con);
                }

            }
        }
        // 根据完成状态的判断，更新课程相关的积分
        Credit.autoUpdUserCreditsByCos(con, cov_status_old, itm_id, app_id, usr_ent_id, prof.usr_id, cov);
    }
}