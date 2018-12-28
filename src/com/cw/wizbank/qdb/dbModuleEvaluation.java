package com.cw.wizbank.qdb;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.JsonMod.exam.ExamController;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.view.ViewModuleEvaluation;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class dbModuleEvaluation
{
    public final static String SESS_PAGINATION_TIMESTAMP        =   "SESS_PAGINATION_TIMESTAMP";
    public final static String SESS_PAGINATION_SORT_COL         =   "SESS_PAGINATION_SORT_COL";
    public final static String SESS_PAGINATION_SORT_ORDER       =   "SESS_PAGINATION_SORT_ORDER";
    public final static String SESS_PAGINATION_USER_LIST_RESULT =   "SESS_PAGINATION_USER_LIST_RESULT";
    public String    INI_DIR_UPLOAD;
    public String    tmpUploadDir;
    public boolean bFileUpload = false;
    public String RES_FOLDER;
    public static final String COMMENT_DIR = "comment";
    public static final String TEMP = "_temp";
    public static final String STATUS_PASSED = "P";
    public static final String STATUS_FAILED = "F";
    public static final String STATUS_NOT_GRADED = "N";
    public static final String STATUS_IN_PROGRESS = "I";

    public long mov_cos_id;
    public long mov_ent_id;
    public long mov_mod_id;
    public Timestamp mov_last_acc_datetime;
    public String mov_ele_loc;
    public float mov_total_time;
    public long mov_total_attempt;
    public String mov_status;
    public String mov_score;
    public String mov_ele_time;
    public boolean attempt_counted = false;
    public Hashtable avg_score = new Hashtable();
    public float mod_time;
    public float score_delta;
    public int mov_not_mark_ind;

    public String mov_create_usr_id;
    public Timestamp mov_create_timestamp;
    public String mov_update_usr_id;
    public Timestamp mov_update_timestamp;
    public Timestamp mov_start_time;
    public String mov_encrypted_start_time;
    public long mov_tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;

    public dbModuleEvaluation() {;}

    private class ModuleReport extends dbModuleEvaluation{
        String mod_title;
        String mod_type;
        String mod_src_type;
        String mod_web_launch;
        String mod_src_link;

        // for AICC only
        String mod_vendor;

        int count;
        // c-complete, i-incomplete, n-notattempt
        int p_count=0;
        int c_count=0;
        int f_count=0;
        int i_count=0;
        int b_count=0;
        int n_count=0;
    }

    public String getParamAsXML(Connection con, loginProfile prof) throws qdbException{
        String xml = new String(dbUtils.xmlHeader);
        xml += "<student_path>" + dbUtils.NEWL;
        xml += prof.asXML() + dbUtils.NEWL;
        xml += getModuleEvalAsXML(con, mov_mod_id, mov_ent_id, mov_tkh_id);
        xml += "</student_path>" + dbUtils.NEWL;
        return xml;
    }
    
    

    // deprecated, do not use this anymore 
    public void save(Connection con)
        throws qdbException, cwSysMessage, qdbErrMessage
    {
        save(con, null);
    }
    
    //core 4.6
    public void save(Connection con, loginProfile prof)
    	throws qdbException, cwSysMessage, qdbErrMessage
    {
    	save(con, prof, false);
    }
    
    public void save(Connection con, loginProfile prof, boolean isNotMark)
    throws qdbException, cwSysMessage, qdbErrMessage
{
    try {
        
        if (mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
        	CommonLog.info("!!!! get tracking id in dbModuleEvaluation.save(con, prof)");
            mov_tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, mov_mod_id, mov_ent_id);
        }
        
        boolean statusChanged;
        boolean scoreChange = true;
        Timestamp curTime = cwSQL.getTime(con);
        
        if  (mod_time  ==  0){
            if (mov_ele_time != null){
                mod_time = dbAiccPath.convert2Second(mov_ele_time);
            }else{
                mod_time = 0;
            }
        }
        if(mov_last_acc_datetime == null) {
            mov_last_acc_datetime = curTime;
        }
        // Correct the error when using other tkh_id to save module without course
        if (mov_cos_id == 0 && mov_tkh_id != 0) {
        	CommonLog.info("Error : mov_cos_id=" +  mov_cos_id + ",mov_tkh_id=" + mov_tkh_id);
            // Auto Correct the problem
            mov_tkh_id = 0;
        }else if (mov_cos_id != 0 && mov_tkh_id ==0) {
            throw new cwException("Failed to save moduleevluation, mov_cos_id=" +  mov_cos_id + ",mov_tkh_id=" + mov_tkh_id);
        }
        
        //get the maxScore from ModuleEvaluationHistory.
//      String everMaxScore = dbModuleEvaluationHistory.getEverMaxScore(con,mov_mod_id,mov_tkh_id);
//      String lastStatus = dbModuleEvaluationHistory.everStatus(con,mov_mod_id,mov_tkh_id,dbAiccPath.STATUS_PASSED,dbAiccPath.STATUS_COMPLETE);
//      boolean hisExist = dbModuleEvaluationHistory.existHis(con,mov_mod_id,mov_tkh_id);
        
        boolean isRestore;
        dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
        dbpas.pasTkhId = mov_tkh_id;
        dbpas.pasResId = mov_mod_id;
        if (dbpas.chkforExist(con)) {
            isRestore = true;
        } else {
            isRestore = false;
        }
        
        // Check if the  record already exists, do update or insert
        String SQL = " SELECT mov_mod_id ,mov_total_attempt, mov_total_time, mov_score, mov_status,mov_last_acc_datetime FROM ModuleEvaluation " + cwSQL.noLockTable() 
              + "    WHERE mov_ent_id = ? "
              + "      AND mov_mod_id = ? "
              + "      AND mov_tkh_id = ? ";
        if (mov_cos_id != 0){
              SQL += " AND mov_cos_id = ? ";
        }

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, mov_ent_id);
        stmt.setLong(2, mov_mod_id);
        stmt.setLong(3, mov_tkh_id);
        if (mov_cos_id != 0){
            stmt.setLong(4, mov_cos_id);
        }
        ResultSet rs = stmt.executeQuery();

        String mvh_score = null;
        String tmp_score = null;
        if (rs.next()) {
            if (attempt_counted || isRestore){
                mov_total_attempt = rs.getLong("mov_total_attempt");
            }else{
                mov_total_attempt = rs.getLong("mov_total_attempt") + 1;
            }
            
            mov_total_time = rs.getFloat("mov_total_time") + mod_time;            
            // if score have not pass in
            tmp_score = rs.getString("mov_score");
            String everMaxScore = tmp_score;
            
            String mod_type = dbResource.getResSubType(con, mov_mod_id);
            if(mod_type!=null && mod_type.equals(dbModule.MOD_TYPE_ASS) && mov_score==null){
                mvh_score = tmp_score;
                mov_score = tmp_score;
            }else{
                mvh_score = mov_score;
                if(everMaxScore != null){
                    mov_score = getMaxScore(mov_score,tmp_score,Float.valueOf(everMaxScore).floatValue());
                }
            }
                //离线助手上传的时间有可能比现有时间更早，不需要更新
				Timestamp old_mov_last_acc_datetime = rs.getTimestamp("mov_last_acc_datetime");
				if(old_mov_last_acc_datetime != null){
					if(mov_last_acc_datetime != null && mov_last_acc_datetime.before(old_mov_last_acc_datetime)){
						mov_last_acc_datetime = old_mov_last_acc_datetime;
					}
				}
            if (score_delta != 0){
                scoreChange = true;
                if (mov_score == null){
                    mov_score = score_delta + "";
                }else{
                    mov_score = (Float.valueOf(mov_score).floatValue() + score_delta) + "";
                }
            }
            String oldStatus = rs.getString("mov_status");
//            if (oldStatus.equals(mov_status)){
//                statusChanged = false;
//            }else{
//                statusChanged = true; 
//            }
            if(dbAiccPath.STATUS_COMPLETE.equalsIgnoreCase(oldStatus) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(oldStatus)){
                mov_status = oldStatus;
            }else{
                if((mov_status == null || mov_status.equalsIgnoreCase(dbAiccPath.STATUS_INCOMPLETE)) && (oldStatus != null && dbAiccPath.STATUS_FAILED.equalsIgnoreCase(oldStatus) )){
                    mov_status = oldStatus; 
                }
                if(mov_status == null){
                    mov_status = dbAiccPath.STATUS_INCOMPLETE;
                }
                
                //添加移动端的学习时长判断
                dbModule module = new dbModule();
                module.mod_res_id = mov_mod_id;
                module.get(con);
               
                if(("VOD".equals(module.mod_type) || "RDG".equals(module.mod_type) || "REF".equals(module.mod_type)) && ((module.mod_required_time > 0 && (mov_total_time / 60) > module.mod_required_time) || module.mod_required_time == 0)){
                    mov_status = dbAiccPath.STATUS_COMPLETE;  
                }
            }
           
            upd(con);
        }else {
            statusChanged = true;
            mov_total_attempt = 1;
            mov_total_time = mod_time;
            if (score_delta != 0){
                mov_score = score_delta + "";
            }
            mvh_score = mov_score;
            //添加移动端的学习时长判断
            dbModule module = new dbModule();
            module.mod_res_id = mov_mod_id;
            module.get(con);
            
            if(("VOD".equals(module.mod_type) || "RDG".equals(module.mod_type) || "REF".equals(module.mod_type)) && ((module.mod_required_time > 0 && (mov_total_time / 60) > module.mod_required_time) || module.mod_required_time == 0)){
                mov_status = dbAiccPath.STATUS_COMPLETE;  
            }
            
            // 结训条件为已查阅的话，则按结训条件为准
            /*
            if(this.queryEndTrainingStatus(con, "IFCP", module.mod_res_id)){
            	mov_status = dbAiccPath.STATUS_INCOMPLETE;
            }
            */
            ins(con);
            
            if (prof.isLrnRole) {
                dbModule dbMod =new dbModule();
                dbMod.mod_res_id = mov_mod_id;
                dbMod.get(con);
                if (dbMod.mod_type != null && dbMod.mod_type.equals(dbModule.MOD_TYPE_EVN)) {
                    Credit credit = new Credit();
                    credit.updUserCredits(con, Credit.SYS_SUBMIT_SVY, 0, (int)prof.usr_ent_id, prof.usr_id, 0, 0, 0,0);
                }
            }
        }
        stmt.close();
        // insert module evaluation history
        dbModuleEvaluationHistory dbModEvalHist = new dbModuleEvaluationHistory();
        dbModEvalHist.mvh_ent_id = this.mov_ent_id;
        dbModEvalHist.mvh_mod_id = this.mov_mod_id;
        dbModEvalHist.mvh_last_acc_datetime = this.mov_last_acc_datetime;
        dbModEvalHist.mvh_tkh_id = this.mov_tkh_id;
        dbModEvalHist.mvh_ele_loc = this.mov_ele_loc;
        dbModEvalHist.mvh_total_time = mod_time;
        dbModEvalHist.mvh_status = this.mov_status;
        if(mvh_score != null) {
            dbModEvalHist.mvh_score = mvh_score;
        }
        if(attempt_counted || isRestore) {
            dbModEvalHist.mvh_total_attempt = 0;
        }
        dbModEvalHist.mvh_create_usr_id = prof.usr_id;
        dbModEvalHist.mvh_create_timestamp = curTime;
        dbModEvalHist.ins(con);

        if (mov_cos_id != 0){
            dbCourseEvaluation dbcov = new dbCourseEvaluation();
            dbcov.cov_cos_id = mov_cos_id;
            dbcov.cov_ent_id = mov_ent_id;
            dbcov.cov_tkh_id = mov_tkh_id;
//            dbcov.save(con);
                // trigger course criteria on every user hit, no matter the status or score changed or not
//            if (statusChanged || scoreChange){
//                CourseCriteria.setAttendFromModule(con, prof, mov_mod_id, mov_cos_id, mov_ent_id, mov_tkh_id, mov_status);
            
                CourseCriteria.setAttendFromModule( con,  prof, mov_mod_id,  mov_cos_id,  mov_ent_id,  mov_tkh_id,  mov_status,mod_time, curTime, curTime);
//            }
        }
        //isNotMark如果为true表明该测验有问答题正在评分把mov_not_mark_ind设为1
        updateNotMark(con, isNotMark);
    } catch(SQLException e) {
        CommonLog.error(e.getMessage(),e);
        throw new qdbException("SQL Error: " + e.getMessage());
    } catch(cwException e) {
    	CommonLog.error(e.getMessage(),e);
        throw new qdbException("cwError: " + e.getMessage());
    }

}
    
    /**
     * 结训条件是否是已查阅
     * @param con
     * @param cmr_status 状态条件
     * @param cmr_res_id 模块ID 
     * @return true/false
     * @throws qdbException
     */
    private boolean queryEndTrainingStatus(Connection con,String cmr_status,long cmr_res_id) throws qdbException {
    	PreparedStatement stmt = null;
          try {
              String SQL = "select count(1) from CourseModuleCriteria where cmr_res_id= ? and cmr_status = ?";
              stmt = con.prepareStatement(SQL);
              stmt.setLong(1, cmr_res_id);
              stmt.setString(2, cmr_status);
              ResultSet rs = stmt.executeQuery();
              if(rs.next()) {
            	  return true;
              }else{
            	  return false;
              }
         } catch(SQLException e) {
              throw new qdbException(e.getMessage());
         } finally {
        	 try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
         }
    }

    
    public static String getMaxScore(String mov_score,String tmp_score,float everMaxScore){
		if(tmp_score == null){
			if(mov_score == null){
				mov_score = new Float(everMaxScore).toString();
			}else{
				mov_score = calMaxScore(mov_score,"0",everMaxScore);
			}
		}else{
			if (mov_score == null || mov_score.equalsIgnoreCase("NaN")){
				mov_score = calMaxScore("0",tmp_score,everMaxScore);
			}else{
				mov_score = calMaxScore(mov_score,tmp_score,everMaxScore);
			}
		}
		return mov_score;
    }
    
    private static String calMaxScore(String cur,String old,float everMax){
    	float curs = new Float(cur).floatValue();
    	float olds = new Float(old).floatValue();
		if(curs < olds){
			if(olds < everMax){
				return  new Float(everMax).toString(); 
			}else{
				return old;
			}
		}else{
			if(curs < everMax){
				return new Float(old).toString(); 
			}
		}
		return cur;
    }

    /*
    public void save(Connection con)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT acp_acc_datetime, acp_ele_time "
                  + "   FROM AiccPath "
                  + "    WHERE acp_cos_id = ? "
                  + "      AND acp_ent_id = ? "
                  + "      AND acp_mod_id = ? ");

            stmt.setLong(1, mov_cos_id);
            stmt.setLong(2, mov_ent_id);
            stmt.setLong(3, mov_mod_id);

            // Check if any record exist
            // if not, the course has not been attempted yet

            mov_total_time = 0;
            mov_last_acc_datetime = new Timestamp(0);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                mov_total_time += rs.getFloat("acp_ele_time");

                if ( rs.getTimestamp("acp_acc_datetime").after(mov_last_acc_datetime))
                    mov_last_acc_datetime = rs.getTimestamp("acp_acc_datetime");

            }
            stmt.close();

            // Check if the  record already exists, do update or insert
            stmt = con.prepareStatement(
                    " SELECT mov_mod_id ,mov_total_attempt FROM ModuleEvaluation "
                  + "    WHERE mov_cos_id = ? "
                  + "      AND mov_ent_id = ? "
                  + "      AND mov_mod_id = ? " );

            stmt.setLong(1, mov_cos_id);
            stmt.setLong(2, mov_ent_id);
            stmt.setLong(3, mov_mod_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                mov_total_attempt += rs.getLong("mov_total_attempt");
                if (unload)
                    mov_total_attempt += 1;
                upd(con);
            }else {
                mov_total_attempt = 1;
                ins(con);
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    */

    public void ins(Connection con)
        throws qdbException
    {
        try {
            String SQL = " INSERT INTO ModuleEvaluation "
                    + " (mov_cos_id "
                    + " ,mov_ent_id "
                    + " ,mov_mod_id "
                    + " ,mov_last_acc_datetime "
                    + " ,mov_ele_loc "
                    + " ,mov_total_time "
                    + " ,mov_total_attempt "
                    + " ,mov_status "
                    + " ,mov_score "
                    + " ,mov_tkh_id ";
            if( this.mov_create_usr_id != null && this.mov_create_usr_id.length() > 0 ) {
                SQL += " ,mov_create_usr_id ,mov_create_timestamp ,mov_update_usr_id ,mov_update_timestamp ";
            }
            SQL += " ) "
                + " VALUES (?,?,?,?,?,?,?,?,?,? ";
            if( this.mov_create_usr_id != null && this.mov_create_usr_id.length() > 0 ) {
                SQL += " ,? ,? ,? ,? ";
            }
            SQL += ") ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            if (mov_cos_id != 0){
                stmt.setLong(index++, mov_cos_id);
            }else{
                stmt.setNull(index++, java.sql.Types.INTEGER);
            }

            stmt.setLong(index++, mov_ent_id);
            stmt.setLong(index++, mov_mod_id);
            stmt.setTimestamp(index++, mov_last_acc_datetime);
            stmt.setString(index++, mov_ele_loc);
            stmt.setFloat(index++, mov_total_time);
            stmt.setLong(index++,mov_total_attempt);
            stmt.setString(index++, mov_status);
            if (mov_score == null){
//                System.out.println("mov_score:" + mov_score);
                stmt.setNull(index++, java.sql.Types.DECIMAL);
            }else{
//                System.out.println("mov_score:" + mov_score);
                stmt.setString(index++, mov_score);
            }
            stmt.setLong(index++, mov_tkh_id);
            if( this.mov_create_usr_id != null && this.mov_create_usr_id.length() > 0 ) {
                Timestamp cur_timestamp = null;
                if( this.mov_create_timestamp == null )
                    cur_timestamp = cwSQL.getTime(con);

                stmt.setString(index++, this.mov_create_usr_id);
                if( this.mov_create_timestamp == null )
                    stmt.setTimestamp(index++, cur_timestamp);
                else
                    stmt.setTimestamp(index++, this.mov_create_timestamp);

                stmt.setString(index++, this.mov_create_usr_id);
                if( this.mov_create_timestamp == null )
                    stmt.setTimestamp(index++, cur_timestamp);
                else
                    stmt.setTimestamp(index++, this.mov_create_timestamp);

            }
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to insert module evaluation .");
            }


        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }



    public void upd(Connection con)
        throws qdbException
    {
        try {
            String SQL =
                    " UPDATE ModuleEvaluation SET "
                    + " mov_last_acc_datetime = ? "
                    + " ,mov_total_time = ? "
                    + " ,mov_total_attempt = ? "
                    + " ,mov_ele_loc = ? "
                    + " ,mov_score = ? "
                    + " ,mov_status = ? " 
                    + " ,mov_not_mark_ind = ?";
                //For backward compatible, if update user not defined, no need to update it
                if( this.mov_update_usr_id != null && this.mov_update_usr_id.length() > 0 ){
                    SQL += " , mov_update_usr_id = ? , mov_update_timestamp = ? ";
                }
                SQL += " WHERE "
                    + "       mov_ent_id = ? "
                    + "   AND mov_mod_id = ? " 
                    + "   AND mov_tkh_id = ? " ;

            if (mov_cos_id != 0){
                  SQL += " AND mov_cos_id = ? ";
            }

            PreparedStatement stmt = con.prepareStatement(SQL);

            int index = 1;
            stmt.setTimestamp(index++, mov_last_acc_datetime);
            stmt.setFloat(index++, mov_total_time);
            stmt.setLong(index++, mov_total_attempt);
            stmt.setString(index++, mov_ele_loc);  
            if (mov_score == null){
                stmt.setNull(index++, java.sql.Types.DECIMAL);
            }else
                stmt.setString(index++, mov_score);

            stmt.setString(index++, mov_status);
            stmt.setInt(index++, mov_not_mark_ind);

            if( this.mov_update_usr_id != null && this.mov_update_usr_id.length() > 0 ){
                stmt.setString(index++, this.mov_update_usr_id );
                if( this.mov_update_timestamp == null )
                    stmt.setTimestamp(index++, cwSQL.getTime(con));
                else
                    stmt.setTimestamp(index++, this.mov_update_timestamp);
            }
            stmt.setLong(index++, mov_ent_id);
            stmt.setLong(index++, mov_mod_id);
            stmt.setLong(index++, mov_tkh_id);
            if (mov_cos_id != 0){
                stmt.setLong(index++, mov_cos_id);
            }
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to update module evaluation .");
            }


        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static String getModuleEvalAsXML(Connection con, long modId, long entId)
        throws qdbException
    {
        try{
        	CommonLog.info("!!!!!!! get tracking id in dbModuleEvaluation.getModuleEvalAsXML()");
            long tkhId = DbTrackingHistory.getAppTrackingIDByMod(con, modId, entId);
            return (getModuleEvalAsXML(con, modId, entId, tkhId));
        }catch(cwException e){
            throw new qdbException("CW ERROR: " + e.getMessage());
        }catch(SQLException e){
            throw new qdbException("SQL ERROR: " + e.getMessage());
        }
    }

    public static String getModuleEvalAsXML(Connection con, long modId, long entId, long tkhId)
        throws qdbException
    {
        Vector modVec = new Vector();

        modVec.addElement(new Long(modId));

        return (getModuleEvalAsXML(con, modVec, entId, tkhId));
    }


    public static String getModuleEvalAsXML(Connection con, Vector modVec, long entId, long tkhId)
        throws qdbException
    {
        try {
            
            String xml = new String();

            //String modLst = dbUtils.vec2list(modVec);
            String tableName = cwSQL.createSimpleTemptable(con, "tmp_res_id", cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, modVec, cwSQL.COL_TYPE_LONG);

            PreparedStatement stmt = con.prepareStatement(
                    " SELECT  mov_cos_id "
                    + "      ,mov_mod_id "
                    + "      ,mov_ent_id "
                    + "      ,mov_tkh_id "
                    + "      ,mov_last_acc_datetime "
                    + "      ,mov_total_time "
                    + "      ,mov_total_attempt "
                    + "      ,mov_status "
                    + "      ,mov_score "
                    + "      ,mov_ele_loc "
                    + "      ,mov_update_timestamp "
                    + "  FROM ModuleEvaluation "
                    + " WHERE "
                    + "   mov_ent_id = ? "
                    + "   AND mov_tkh_id = ? "
                    + "   AND mov_mod_id IN "
                    + " (SELECT tmp_res_id FROM " + tableName + ")");

            stmt.setLong(1, entId);
            stmt.setLong(2, tkhId);
            ResultSet rs = stmt.executeQuery();

            xml = generateXML(rs, tkhId);

            stmt.close();

            cwSQL.dropTempTable(con,tableName);

            return xml;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    /*
    public static String getModuleEvalFromCosAsXML(Connection con, long cosId, long entId)
        throws qdbException
    {
        return getModuleEvalFromCosAsXML(con, cosId, entId, 0);
    } 
    */

    public static String getModuleEvalFromCosAsXML(Connection con, long cosId, long entId, long tkhId)
        throws qdbException
    {
        try {
            String xml = new String();

            PreparedStatement stmt = con.prepareStatement(
                    " SELECT  mov_cos_id "
                    + "      ,mov_mod_id "
                    + "      ,mov_ent_id "
                    + "      ,mov_tkh_id "
                    + "      ,mov_last_acc_datetime "
                    + "      ,mov_total_attempt "
                    + "      ,mov_total_time "
                    + "      ,mov_status "
                    + "      ,mov_score "
                    + "      ,mov_ele_loc "
                    + "      ,mov_update_timestamp "
                    + "  FROM ModuleEvaluation "
                    + " WHERE "
                    + "   mov_ent_id = ? "
                    + "   AND mov_tkh_id = ? "
                    + "   AND mov_mod_id IN "
                    + "  ( SELECT rcn_res_id_content FROM ResourceContent "
                    + "  WHERE rcn_res_id = ? )");

            stmt.setLong(1, entId);
            stmt.setLong(2, tkhId);
            stmt.setLong(3, cosId);

            ResultSet rs = stmt.executeQuery();

            xml = generateXML(rs, tkhId);

            stmt.close();

            return xml;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    private static String generateXML(ResultSet rs, long tkh_id)
        throws qdbException
    {
        try {
            StringBuffer xmlBuf = new StringBuffer(2048);
            xmlBuf.append("<aicc_data");
            if(tkh_id>0){
                xmlBuf.append(" tkh_id=\"").append(tkh_id).append("\"");
            }
            xmlBuf.append(">").append(dbUtils.NEWL);

            while (rs.next()) {
                String score_s  =  null;
                if (rs.getString("mov_score")!= null){
                    score_s = convertScore(rs.getFloat("mov_score"));
                }else   score_s  =  "";
                xmlBuf.append("<attempt course_id=\"").append(rs.getLong("mov_cos_id"));
                xmlBuf.append("\" student_id=\"").append(rs.getLong("mov_ent_id")).append("\" lesson_id=\"").append(rs.getLong("mov_mod_id"));
                xmlBuf.append("\" last_acc_datetime=\"").append(rs.getTimestamp("mov_last_acc_datetime"));
                try{
                    xmlBuf.append("\" last_update_timestamp=\"").append(cwUtils.escNull(rs.getTimestamp("mov_update_timestamp")));
                }catch(SQLException e){
                    //For backward compatible, the resultset passed to the function may not contain mov_update_timestamp column
                }
                xmlBuf.append("\" used_time=\"").append(dbAiccPath.getTime(rs.getFloat("mov_total_time")));
                xmlBuf.append("\" number=\"").append(rs.getString("mov_total_attempt"));
                xmlBuf.append("\" status=\"").append(rs.getString("mov_status"));
                xmlBuf.append("\" score=\"").append(score_s);
                xmlBuf.append("\" location=\"").append(dbUtils.esc4XML(rs.getString("mov_ele_loc")));
                xmlBuf.append("\"/>").append(dbUtils.NEWL);
            }

            xmlBuf.append("</aicc_data>").append(dbUtils.NEWL);

            return xmlBuf.toString();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    public static String getAiccDataXML(ResultSet rs, long tkh_id) throws SQLException{

        StringBuffer xmlBuf = new StringBuffer(2048);
        xmlBuf.append("<aicc_data");
        if(tkh_id>0){
            xmlBuf.append(" tkh_id=\"").append(tkh_id).append("\"");
        }
        xmlBuf.append(">").append(dbUtils.NEWL);
        String score_s  =  null;
        if (rs.getString("mov_score")!= null){
            score_s = convertScore(rs.getFloat("mov_score"));
        }else   score_s  =  "";
        xmlBuf.append("<attempt course_id=\"").append(rs.getLong("mov_cos_id"));
        xmlBuf.append("\" student_id=\"").append(rs.getLong("mov_ent_id")).append("\" lesson_id=\"").append(rs.getLong("mov_mod_id"));
        xmlBuf.append("\" last_acc_datetime=\"").append(rs.getTimestamp("mov_last_acc_datetime"));
        try{
            xmlBuf.append("\" last_update_timestamp=\"").append(cwUtils.escNull(rs.getTimestamp("mov_update_timestamp")));
        }catch(SQLException e){
            //For backward compatible, the resultset passed to the function may not contain mov_update_timestamp column
        }
        xmlBuf.append("\" used_time=\"").append(dbAiccPath.getTime(rs.getFloat("mov_total_time")));
        xmlBuf.append("\" number=\"").append(rs.getString("mov_total_attempt"));
        xmlBuf.append("\" status=\"").append(rs.getString("mov_status"));
        xmlBuf.append("\" score=\"").append(score_s);
        xmlBuf.append("\" location=\"").append(dbUtils.esc4XML(rs.getString("mov_ele_loc")));
        xmlBuf.append("\"/>").append(dbUtils.NEWL);
        xmlBuf.append("</aicc_data>").append(dbUtils.NEWL);
        
        return xmlBuf.toString();
    }

    // called by qdbAction
    public String getModuleRptAsXML(Connection con, loginProfile prof, String cur_node, String tkh_type) throws qdbException, SQLException{
        try{
            String cosStructXML = getResourceAsXML(con);
            long totalUser = DbTrackingHistory.getAttemptCntFromCos(con, mov_cos_id, tkh_type);
            
            String xml = new String(dbUtils.xmlHeader);
            xml += "<report cur_node=\"" + cur_node + "\">" + dbUtils.NEWL;
            xml += prof.asXML() + dbUtils.NEWL;
            xml += start_resource_tag;
            //Vector temp = getModule(con, memberTableName);
            Vector modIdVec = dbResource.getNodeModuleIds(cosStructXML, cur_node);

            Vector temp = getModule(con, modIdVec, tkh_type);
            temp = parseVtModule(temp);
            getAvgScore(con);
            xml += getModuleXML(con, temp, totalUser);
            xml += end_resource_tag;
            xml += "</report>";

            return xml;
        }catch (qdbErrMessage e){
            throw new qdbException("Error: " + e.getMessage());
        }
    }

    // called by qdbAction with date range
    public String getModuleRptAsXML(Connection con, loginProfile prof, String cur_node, String tkh_type, Timestamp startDateTime, Timestamp endDateTime, long cos_res_id) throws qdbException, SQLException, cwSysMessage{
        try{
            String cosStructXML = getResourceAsXML(con);
            //long totalUser = DbTrackingHistory.getAttemptCntFromCos(con, mov_cos_id, tkh_type, startDateTime, endDateTime);
            
            String xml = new String(dbUtils.xmlHeader);
            xml += "<report cur_node=\"" + cur_node + "\">" + dbUtils.NEWL;
            xml += prof.asXML() + dbUtils.NEWL;
            xml += start_resource_tag;

            Vector modIdVec = dbResource.getNodeModuleIds(cosStructXML, cur_node);

            /*
            Vector temp = getModule(con, modIdVec, tkh_type, startDateTime, endDateTime);
            temp = parseVtModule(temp);
            */
            
            Hashtable hModuleReport = getModuleReport(con, modIdVec, tkh_type, startDateTime, endDateTime);
            getAvgScore(con, startDateTime, endDateTime);

            xml += getModuleInDateXML(con, hModuleReport);
            xml += end_resource_tag;
            
            long itm_id = dbCourse.getCosItemId(con, cos_res_id);
            xml += aeItem.genItemActionNavXML(con, itm_id, prof);
            xml+="<current_role>"+prof.current_role+"</current_role>";
            xml += "</report>";

            return xml;
        }catch (qdbErrMessage e){
            throw new qdbException("Error: " + e.getMessage());
        }
    }
    
    public String getLearnerModuleRptAsXML(Connection con, loginProfile prof)
        throws qdbException, cwSysMessage, cwException{
        try{
            if (mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
                mov_tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, mov_cos_id, mov_ent_id);
            }
            // no need to parse vtModule
            getResourceAsXML(con);
            StringBuffer xml = new StringBuffer();
            xml.append(dbUtils.xmlHeader);
            xml.append("<report>").append(dbUtils.NEWL);
            xml.append(prof.asXML()).append(dbUtils.NEWL);

            dbRegUser dbRU = new dbRegUser();
            dbRU.usr_ent_id = mov_ent_id;
            dbRU.getByEntId(con);
            xml.append("<learner usr_id=\"").append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,dbRU.usr_id)));
            xml.append("\" ent_id=\"").append(mov_ent_id);
            xml.append("\" name=\"").append(dbUtils.esc4XML(dbRU.usr_display_bil));
            xml.append("\" />").append(dbUtils.NEWL);
            xml.append(start_resource_tag);
            Vector temp = getLearnerModule(con, 0, prof.root_ent_id);
            xml.append("<module_list tkh_id=\"").append(mov_tkh_id).append("\">");
            xml.append(getLearnerModuleXML(con, temp, prof));
            xml.append("</module_list>");
            xml.append(end_resource_tag);
            xml.append("</report>");
            return xml.toString();
        }catch (qdbErrMessage e){
            throw new qdbException("Error: " + e.getMessage());
        }
        catch (SQLException sqle) {
            throw new qdbException(sqle.getMessage());
        }
    }

    public String getLearnerSkillsoftRptAsXML(Connection con, loginProfile prof) throws qdbException, cwException, SQLException{
        try{
            if (mov_tkh_id <= 0) {
                mov_tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, mov_mod_id, mov_ent_id);
            }
            
            // no need to parse vtModule
            getResourceAsXML(con);
            String xml = new String(dbUtils.xmlHeader);
            xml += "<report>" + dbUtils.NEWL;
            xml += prof.asXML() + dbUtils.NEWL;

            dbRegUser dbRU = new dbRegUser();
            dbRU.usr_ent_id = mov_ent_id;
            dbRU.getByEntId(con);
            xml += "<learner usr_id=\"" + dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,dbRU.usr_id)) + "\" ent_id=\"" + mov_ent_id + "\" name=\"" + dbUtils.esc4XML(dbRU.usr_display_bil) + "\" />" + dbUtils.NEWL;
            xml += start_resource_tag;
//            Vector temp = getLearnerModule(con);
//            xml += getLearnerModuleXML(temp);
            xml += "<module id=\"" + mov_mod_id + "\">" + dbUtils.esc4XML(dbResource.getResTitle(con, mov_mod_id)) + "</module>";
            xml += getSkillsoftRptBody(con);
            xml += end_resource_tag;
            xml += "</report>";
            return xml;
        }catch (qdbErrMessage e){
            throw new qdbException("Error: " + e.getMessage());
        }
    }

    public String getLearnerAiccRptAsXML(Connection con, loginProfile prof) throws cwException, cwSysMessage{
        try{
            long cosId = dbModule.getCosId(con, mov_mod_id);
            if (cosId > 0 && mov_tkh_id <= 0) {
                mov_tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, cosId, mov_ent_id);
            }
            
            String cosTitle = dbResource.getResTitle(con, cosId);
            Vector vtModule = getLearnerModule(con, mov_mod_id, prof.root_ent_id);
            String modTitle = null;

            if (vtModule.size() > 0) {
                ModuleReport mr = (ModuleReport)vtModule.elementAt(0);
                modTitle = mr.mod_title;
            } else {
                modTitle = dbResource.getResTitle(con, mov_mod_id);
            }

            StringBuffer xml = new StringBuffer(1500);
            xml.append(dbUtils.xmlHeader);
            xml.append("<report>").append(dbUtils.NEWL);
            xml.append(prof.asXML()).append(dbUtils.NEWL);

            dbRegUser dbRU = new dbRegUser();
            dbRU.usr_ent_id = mov_ent_id;
            dbRU.getByEntId(con);
            xml.append("<aicc_au_report>");
            xml.append("<learner usr_id=\"").append(dbUtils.esc4XML(dbRegUser.usrId2SteUsrId(con,dbRU.usr_id)));
            xml.append("\" ent_id=\"").append(mov_ent_id);
            xml.append("\" name=\"").append(dbUtils.esc4XML(dbRU.usr_display_bil));
            xml.append("\" tkh_id=\"").append(mov_tkh_id);
            xml.append("\" />").append(dbUtils.NEWL);
            xml.append("<course id=\"").append(cosId).append("\" >");
            xml.append("<title>").append(dbUtils.esc4XML(cosTitle)).append("</title>");
            xml.append("<module id=\"").append(mov_mod_id).append("\" title=\"").append(cwUtils.esc4XML(cwUtils.escNull(modTitle))).append("\"/>");
            xml.append(getLearnerModuleXML(vtModule));
            xml.append(getAiccRptBodyAsXML(con));
            xml.append(end_resource_tag);
            xml.append("</course>");
            xml.append("</aicc_au_report>");
            xml.append("</report>");
            return xml.toString();
        }catch (qdbException e){
            throw new cwException(e.getMessage());
        }catch (SQLException e){
            throw new cwException(e.getMessage());
        }
    }

    private String start_resource_tag = "";
    private String end_resource_tag = "";

    public String getResourceAsXML(Connection con) throws qdbErrMessage, qdbException{
        try{
            String course_title = null;
            String course_struct = null;
            String cos_vendor = null;
            long cos_itm_id = 0;
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT "
                    + " res_id, res_title, cos_structure_xml, cos_vendor, cos_itm_id "
                    + " FROM Resources, Course WHERE "
                    + " res_id = cos_res_id AND "
                    + " res_id = ? ");

            stmt.setLong(1, mov_cos_id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                course_title = rs.getString("res_title");
                //course_struct = rs.getString("cos_structure_xml");
                course_struct = cwSQL.getClobValue(rs, "cos_structure_xml");
                // for AICC only
                cos_vendor = rs.getString("cos_vendor");
                if (cos_vendor == null) {
                    cos_vendor = "";
                }
                cos_itm_id = rs.getLong("cos_itm_id");
            }
            stmt.close();

            start_resource_tag = "<course id=\"" + mov_cos_id + "\" itm_id=\"" + cos_itm_id + "\" title=\"" + dbUtils.esc4XML(course_title) + "\" cos_vendor=\"" + dbUtils.esc4XML(cos_vendor) + "\" >" + dbUtils.NEWL;
            start_resource_tag += course_struct + dbUtils.NEWL;
            //end_resource_tag = "</course>" + dbUtils.NEWL;
			try{
				 String s = aeItem.getNavAsXML(con,cos_itm_id).toString();
				 end_resource_tag = s + "</course>" + dbUtils.NEWL; 
			}catch(Exception e){
							throw new qdbException (e.getMessage());     
			}

            return course_struct;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


    //private Vector getModule(Connection con, String memberTableName) throws qdbException{
    // don't use temp table, get the enrolment list from Enrolment table
    private Vector getModule(Connection con, Vector modIdVec, String tkh_type) throws qdbException{
        // ent_id is a key, so no need to group by ent_id
        try{
            Vector vtModuleReport = new Vector();

            if (modIdVec == null ||  modIdVec.size() == 0) {
                return vtModuleReport;
            }

			String SQL = OuterJoinSqlStatements.dbModuleEvaluationGetModule(modIdVec);
//            String SQL = " SELECT count(*) AS result_count "
//               + " , rcn_res_id_content mov_mod_id "
//               + " , res_title "
//               + " , res_subtype "
//               + " , res_src_type "
//               + " , mov_status "
//               + " , sum(mov_total_attempt) total_attempt "
//               + " , max(mov_last_acc_datetime) last_access "
//               + " , sum(mov_total_time) total_time "
//               + " FROM ModuleEvaluation, Resources, ResourceContent, TrackingHistory "
//               + " WHERE "
//               + " rcn_res_id = ? AND "
//               + " rcn_res_id = tkh_cos_res_id AND "
//               + " res_id = rcn_res_id_content AND "
//               + " mov_ent_id " + cwSQL.get_right_join(con) + " tkh_usr_ent_id AND "
//               + " mov_mod_id " + cwSQL.get_right_join(con) + " rcn_res_id_content AND "
//               + " mov_tkh_id " + cwSQL.get_right_join(con) + " tkh_id AND " 
//               + " rcn_res_id_content IN " + dbUtils.vec2String(modIdVec)
//               + " AND tkh_type = ? "
//               + " GROUP BY rcn_res_id_content, mov_status, res_title, res_src_type, res_subtype "
//               + " ORDER BY res_title, rcn_res_id_content";

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, mov_cos_id);
            stmt.setString(2, tkh_type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                ModuleReport mr = new ModuleReport();
                mr.count = rs.getInt("result_count");
                mr.mov_mod_id = rs.getLong("mov_mod_id");
                mr.mod_title = rs.getString("res_title");
                mr.mod_type = rs.getString("res_subtype");
                mr.mod_src_type = rs.getString("res_src_type");
                mr.mov_status = rs.getString("mov_status");
                mr.mov_total_attempt = rs.getInt("total_attempt");
                mr.mov_last_acc_datetime = rs.getTimestamp("last_access");
                mr.mov_total_time = rs.getFloat("total_time");
                vtModuleReport.addElement(mr);
            }
            stmt.close();
            return vtModuleReport;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    private Hashtable getModuleReport(Connection con, Vector modIdVec, String tkh_type, Timestamp startDateTime, Timestamp endDateTime) throws SQLException{
        PreparedStatement stmt = null;
        Hashtable h = new Hashtable();

        if (modIdVec == null ||  modIdVec.size() == 0) {
            return h;
        }
        try{
            //Get Module Report details for each module, e.g. total time spent, last access datetime...
            String SQL = OuterJoinSqlStatements.getUsageRpt(dbUtils.vec2String(modIdVec), startDateTime, endDateTime);
            int index = 1;
            stmt = con.prepareStatement(SQL);
            
            if(startDateTime != null) {
                stmt.setTimestamp(index++, startDateTime);
            }
            if(endDateTime != null) {
                stmt.setTimestamp(index++, endDateTime);
            }
            if(startDateTime != null) {
                stmt.setTimestamp(index++, startDateTime);
            }
            if(endDateTime != null) {
                stmt.setTimestamp(index++, endDateTime);
            }
            stmt.setLong(index++, mov_cos_id);
            stmt.setString(index++, tkh_type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                ModuleReport mr = new ModuleReport();
                mr.mov_mod_id = rs.getLong("mov_mod_id");
                mr.mod_title = rs.getString("res_title");
                mr.mod_type = rs.getString("res_subtype");
                mr.mod_src_type = rs.getString("res_src_type");
                mr.mov_total_attempt = rs.getInt("total_attempt");
                mr.mov_last_acc_datetime = rs.getTimestamp("last_access");
                mr.mov_total_time = rs.getFloat("total_time");
                h.put(new Long(mr.mov_mod_id),mr);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        // Get AICC status count for each module
        getModuleAICCStatusCount(con, h, modIdVec, startDateTime, endDateTime);
        return h;
    }
    
    //Get the AICC Status count of the input ModuleReport which is inside the Hashtable
    private void getModuleAICCStatusCount(Connection con, Hashtable hModuleReport, Vector modIdVec, Timestamp startDateTime, Timestamp endDateTime) throws SQLException {
        PreparedStatement stmt = null;
        if(hModuleReport != null && hModuleReport.size() > 0) {
            try {
                dbUtils.vec2String(modIdVec);
                StringBuffer SQLBuf = new StringBuffer(512);
                SQLBuf.append(" select count(*) cnt, a.mvh_mod_id, a.mvh_status ")
                      .append(" from moduleEvaluationHistory a ")
                      .append(" where a.mvh_mod_id in ").append(dbUtils.vec2String(modIdVec))
                      .append(" and a.mvh_status is not null ")
                      .append(" and a.mvh_create_timestamp = ")
                      .append(" (Select max(b.mvh_create_timestamp) ")
                      .append(" from ModuleEvaluationHistory b ")
				      .append(" Where b.mvh_mod_id = a.mvh_mod_id ")
				      .append(" And b.mvh_ent_id = a.mvh_ent_id ")
				      .append(" And b.mvh_tkh_id = a.mvh_tkh_id ");
                if(startDateTime != null) {
                    SQLBuf.append("AND b.mvh_last_acc_datetime > ? ");
                }
                if(endDateTime != null) {
                    SQLBuf.append("AND b.mvh_last_acc_datetime < ? ");
                }
                SQLBuf.append(") group by a.mvh_mod_id, a.mvh_status ");


                stmt = con.prepareStatement(SQLBuf.toString());
                if(startDateTime != null) {
                    stmt.setTimestamp(1, startDateTime);
                }
                if(endDateTime != null) {
                    stmt.setTimestamp(2, endDateTime);
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int count = rs.getInt("cnt");
                    long mod_id = rs.getLong("mvh_mod_id");
                    Long L_mod_id = new Long(mod_id);
                    String status = rs.getString("mvh_status");
                    ModuleReport mr = (ModuleReport)hModuleReport.get(L_mod_id);
                    if(mr != null) {
                        if(status.equals(dbAiccPath.STATUS_COMPLETE)) {
                            mr.c_count = count;
                        } else if(status.equals(dbAiccPath.STATUS_PASSED)) {
                            mr.p_count = count;
                        } else if(status.equals(dbAiccPath.STATUS_FAILED)) {
                            mr.f_count = count;
                        } else if(status.equals(dbAiccPath.STATUS_INCOMPLETE)) {
                            mr.i_count = count;
                        } else if(status.equals(dbAiccPath.STATUS_BROWSED)) {
                            mr.b_count = count;
                        }
                        hModuleReport.put(L_mod_id, mr);
                    }
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return;
    }
    
    // if modId != 0
    // that mean specialified for one module only
    // the vtModule -> one element in the vector
    /****** new ****/
    private Vector getLearnerModule(Connection con, long modId, long root_ent_id) throws qdbException, cwSysMessage{
        // ent_id is a key, so no need to group by ent_id
        // one record in moduleEvaluation for one module
        try{
            PreparedStatement stmt = null;
            ResultSet rs = null;
//            Vector valid_period_vec = new Vector();
            Timestamp enrol_start_timestamp = null;
            Timestamp enrol_end_timestamp = null;

            if (modId == 0) {
                int ats_progress_id = aeAttendanceStatus.getIdByType(con, root_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
                StringBuffer SQLBuf = new StringBuffer(256);
                SQLBuf.append(" select tkh_create_timestamp, att_timestamp, att_ats_id from TrackingHistory , Course , aeApplication , aeAttendance ")
                    .append(" WHERE tkh_usr_ent_id = ? AND tkh_cos_res_id = ? AND tkh_id = ? AND tkh_type = ? ")
                    .append(" AND tkh_cos_res_id = cos_res_id ")
                    .append(" AND cos_itm_id = app_itm_id ")
                    .append(" AND app_ent_id = tkh_usr_ent_id ")
                    .append(" AND app_id = att_app_id ")
                    .append(" AND att_itm_id = app_itm_id ")
                    .append(" AND tkh_id = app_tkh_id ")
                    .append(" UNION ")
                    .append(" SELECT tkh_create_timestamp, att_timestamp, att_ats_id from TrackingHistory, Course, aeItemRelation, aeApplication, aeAttendance ")
                    .append(" WHERE tkh_usr_ent_id = ? and tkh_cos_res_id = ? AND tkh_id = ? AND tkh_type = ? ")
                    .append(" and tkh_cos_res_id = cos_res_id ")
                    .append(" and cos_itm_id = ire_parent_itm_id ")
                    .append(" and ire_child_itm_id = app_itm_id ")
                    .append(" and app_ent_id = tkh_usr_ent_id ")
                    .append(" and app_id = att_app_id ")
                    .append(" AND att_itm_id = app_itm_id ")
                    .append(" AND tkh_id = app_tkh_id ")
                    .append(" UNION ")
                    .append(" SELECT tkh_create_timestamp,")
                    .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP)).append(" as att_timestamp, ")
                    .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER)).append(" as att_ats_id")
                    .append(" from TrackingHistory ")
                    .append(" WHERE tkh_usr_ent_id = ? and tkh_cos_res_id = ? AND tkh_id = ? AND tkh_type = ? ")
                    .append(" UNION ")
                    .append(" SELECT tkh_create_timestamp,")
                    .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP)).append(" as att_timestamp, ")
                    .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER)).append(" as att_ats_id")
                    .append(" from TrackingHistory, Course ")
                    .append(" WHERE tkh_usr_ent_id = ? and tkh_cos_res_id = ? AND tkh_id = ? AND tkh_type = ? AND cos_res_id = tkh_cos_res_id AND tkh_id NOT IN (SELECT app_tkh_id FROM aeApplication WHERE app_itm_id = cos_itm_id)");

                int col=1;
                stmt = con.prepareStatement(SQLBuf.toString()); 
                stmt.setLong(col++, mov_ent_id);
                stmt.setLong(col++, mov_cos_id);
                stmt.setLong(col++, mov_tkh_id);
                stmt.setString(col++, DbTrackingHistory.TKH_TYPE_APPLICATION);
                stmt.setLong(col++, mov_ent_id);
                stmt.setLong(col++, mov_cos_id);
                stmt.setLong(col++, mov_tkh_id);
                stmt.setString(col++, DbTrackingHistory.TKH_TYPE_APPLICATION);
                stmt.setLong(col++, mov_ent_id);
                stmt.setLong(col++, mov_cos_id);
                stmt.setLong(col++, mov_tkh_id);
                stmt.setString(col++, DbTrackingHistory.TKH_TYPE_QUICK_REFERENCE);
                stmt.setLong(col++, mov_ent_id);
                stmt.setLong(col++, mov_cos_id);
                stmt.setLong(col++, mov_tkh_id);
                stmt.setString(col++, DbTrackingHistory.TKH_TYPE_APPLICATION);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int att_ats_id = rs.getInt("att_ats_id");
                    enrol_start_timestamp = rs.getTimestamp("tkh_create_timestamp");

                    if (att_ats_id != ats_progress_id) {
                        Timestamp temp_ts = rs.getTimestamp("att_timestamp");
                        
                        if (enrol_end_timestamp == null || temp_ts.after(enrol_end_timestamp)) {
                            enrol_end_timestamp = rs.getTimestamp("att_timestamp");
                        }
                    } else {
                        enrol_end_timestamp = null;
                        break;
                    }

                }

                stmt.close();
            }

            Vector vtModuleReport = new Vector();

			String SQL = "";
			if(modId!=0) {
				SQL = " SELECT "
               + " rcn_res_id_content mov_mod_id "
               + " , res_title "
               + " , res_subtype "
               + " , res_src_type "
               + " , res_src_link "
               + " , mov_status "
               + " , mov_score "
               + " , mov_total_attempt total_attempt "
               + " , mov_last_acc_datetime last_access "
               + " , mov_total_time total_time "
               + " , mod_eff_start_datetime "
               + " , mod_eff_end_datetime "
               + " , mod_web_launch "
               + " FROM moduleEvaluation, resources, resourceContent, Module "
               + " WHERE "
               + " rcn_res_id = ? AND "  // set cos_id
               + " mov_ent_id = ? AND "
               + " mov_tkh_id = ? AND ";
               
    SQL += " mov_mod_id = " + modId + " AND ";
    SQL += " mov_mod_id = rcn_res_id_content AND ";
    SQL += " res_id = rcn_res_id_content AND ";
    SQL += " mod_res_id = res_id";
    SQL += " ORDER BY res_title, rcn_res_id_content";
			   } else {
					SQL = OuterJoinSqlStatements.dbModuleEvaluationGetLearnerModule();
               }

            stmt = con.prepareStatement(SQL);

            stmt.setLong(1, mov_cos_id);
            stmt.setLong(2, mov_ent_id);
            stmt.setLong(3, mov_tkh_id);

            rs = stmt.executeQuery();

            while (rs.next())
            {
                ModuleReport mr = new ModuleReport();
                mr.mov_mod_id = rs.getLong("mov_mod_id");
                mr.mod_title = rs.getString("res_title");
                mr.mod_type = rs.getString("res_subtype");
                mr.mod_src_type = rs.getString("res_src_type");
                mr.mod_src_link = rs.getString("res_src_link");
                mr.mod_web_launch = rs.getString("mod_web_launch");
                mr.mov_status = rs.getString("mov_status");
                mr.mov_score = rs.getString("mov_score");
                mr.mov_total_attempt = rs.getInt("total_attempt");
                mr.mov_last_acc_datetime = rs.getTimestamp("last_access");
                mr.mov_total_time =(int) rs.getFloat("total_time");
              
                // for AICC only
                dbModule dbmod = new dbModule();
                dbmod.mod_res_id = mr.mov_mod_id;
                dbmod.getAicc(con);

                mr.mod_vendor = dbmod.mod_vendor;

                if (modId != 0) {
                    vtModuleReport.addElement(mr);
                } else {
                    Timestamp mod_eff_start_timestamp = rs.getTimestamp("mod_eff_start_datetime");
                    Timestamp mod_eff_end_timestamp = rs.getTimestamp("mod_eff_end_datetime");

                    if ((enrol_end_timestamp == null || enrol_end_timestamp.after(mod_eff_start_timestamp)) &&
                        enrol_start_timestamp.before(mod_eff_end_timestamp)) {
                        vtModuleReport.addElement(mr);
                    }
                }
            }

            stmt.close();
            return vtModuleReport;

        } catch(SQLException e) {
        	CommonLog.error(e.getMessage(),e);
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }




    private ModuleReport accumModule(ModuleReport module, ModuleReport sum){
        sum.mov_mod_id = module.mov_mod_id;
        sum.mod_title = module.mod_title;
        sum.mod_type = module.mod_type;
        sum.mod_src_type = module.mod_src_type;
        sum.mov_total_attempt += module.mov_total_attempt;
        sum.mov_total_time += module.mov_total_time;
        if (module.mov_last_acc_datetime != null){
            if (sum.mov_last_acc_datetime != null){
                if (sum.mov_last_acc_datetime.before(module.mov_last_acc_datetime))
                    sum.mov_last_acc_datetime = module.mov_last_acc_datetime;
            }else{
                sum.mov_last_acc_datetime = module.mov_last_acc_datetime;
            }
        }
        if (module.mov_status != null){
            if (module.mov_status.equals(dbAiccPath.STATUS_PASSED))
                sum.p_count = module.count;
            else if (module.mov_status.equals(dbAiccPath.STATUS_COMPLETE))
                sum.c_count = module.count;
            else if (module.mov_status.equals(dbAiccPath.STATUS_FAILED))
                sum.f_count = module.count;
            else if (module.mov_status.equals(dbAiccPath.STATUS_INCOMPLETE))
                sum.i_count = module.count;
            else if (module.mov_status.equals(dbAiccPath.STATUS_BROWSED))
                sum.b_count = module.count;
        }
        return sum;
    }

    // group the modId (with diff status) into the same object
    private Vector parseVtModule(Vector vtModuleReport) throws qdbErrMessage, qdbException{
        String xml = "";
        Vector vtFinalModule = new Vector();
        for (int i=0;i<vtModuleReport.size();i++){
            // for new mod
            ModuleReport sum = new ModuleReport();
            sum = accumModule((ModuleReport) vtModuleReport.elementAt(i), sum);
            while (i<vtModuleReport.size()-1){
                // if same loc, accumulate to sum and i++
                // else, break the while loop
                if (((ModuleReport)vtModuleReport.elementAt(i+1)).mov_mod_id == (sum.mov_mod_id)){
                    sum = accumModule((ModuleReport) vtModuleReport.elementAt(i+1), sum);
                    i++;
                }
                else
                    break;
            }
            vtFinalModule.addElement(sum);
        }
        return vtFinalModule;
    }
    // must call getAvgScore before call this
    public String getModuleXML(Connection con, Vector vtModuleReport, long noOfEnrolment) throws qdbErrMessage, qdbException{
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<module_list>");
        for (int i=0;i<vtModuleReport.size();i++) {
                    ModuleReport mr = (ModuleReport) vtModuleReport.elementAt(i);
                    int no_of_attentant = mr.p_count + mr.c_count + mr.f_count + mr.i_count + mr.b_count;
                    xmlBuf.append("<module id=\"").append(mr.mov_mod_id);
                    xmlBuf.append("\" title=\"").append(dbUtils.esc4XML(mr.mod_title));
                    xmlBuf.append("\" type=\"").append(mr.mod_type);
                    xmlBuf.append("\" avg_time_per_learner=\"").append(dbAiccPath.getTime(mr.mov_total_time / no_of_attentant));
                    xmlBuf.append("\" hits=\"").append(mr.mov_total_attempt);
                    xmlBuf.append("\" avg_time_per_hit=\"").append(dbAiccPath.getTime(mr.mov_total_time / mr.mov_total_attempt));
                    xmlBuf.append("\" last_access=\"").append(mr.mov_last_acc_datetime);
                    Long mov_id = new Long(mr.mov_mod_id);
                    String score = (String)avg_score.get(mov_id);
                    xmlBuf.append("\" score=\"").append(score);
                    xmlBuf.append("\" src_type=\"").append(mr.mod_src_type);
                    xmlBuf.append("\" >").append(dbUtils.NEWL);
                    xmlBuf.append("<status p=\"").append(mr.p_count);
                    xmlBuf.append("\" c=\"").append(mr.c_count);
                    xmlBuf.append("\" f=\"").append(mr.f_count);
                    xmlBuf.append("\" i=\"").append(mr.i_count);
                    xmlBuf.append("\" b=\"").append(mr.b_count);
                    xmlBuf.append("\" n=\"");
                    xmlBuf.append((noOfEnrolment - no_of_attentant));
                    xmlBuf.append("\" />").append(dbUtils.NEWL);
                    xmlBuf.append("</module>");
        }
        xmlBuf.append("</module_list>");
        return xmlBuf.toString();
    }

    // must call getAvgScore before call this
    private String getModuleInDateXML(Connection con, Hashtable hModuleReport) throws qdbErrMessage, qdbException{
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<module_list>");
        Enumeration eModuleReport = hModuleReport.elements();
        while(eModuleReport.hasMoreElements()) {
            ModuleReport mr = (ModuleReport) eModuleReport.nextElement();
            int no_of_attentant = mr.p_count + mr.c_count + mr.f_count + mr.i_count + mr.b_count;
            String ATTEMPTED = dbResource.RES_ATTEMPTED_FALSE;
            // Check if the module was attempted
            long cnt = dbProgress.attemptNum(con , mr.mov_mod_id);
            if (cnt>0)
                ATTEMPTED = dbResource.RES_ATTEMPTED_TRUE;
            
            xmlBuf.append("<module id=\"").append(mr.mov_mod_id);
            xmlBuf.append("\" title=\"").append(dbUtils.esc4XML(mr.mod_title));
            xmlBuf.append("\" type=\"").append(mr.mod_type);
            xmlBuf.append("\" avg_time_per_learner=\"").append(dbAiccPath.getTime(mr.mov_total_time / no_of_attentant));
            xmlBuf.append("\" hits=\"").append(mr.mov_total_attempt);
            xmlBuf.append("\" avg_time_per_hit=\"").append(dbAiccPath.getTime(mr.mov_total_time / mr.mov_total_attempt));
            xmlBuf.append("\" last_access=\"").append(mr.mov_last_acc_datetime);
            Long mov_id = new Long(mr.mov_mod_id);
            String score = (String)avg_score.get(mov_id);
            xmlBuf.append("\" score=\"").append(score);
            xmlBuf.append("\" src_type=\"").append(mr.mod_src_type);
            xmlBuf.append("\" attempted=\"").append(ATTEMPTED);
            xmlBuf.append("\" >").append(dbUtils.NEWL);
            xmlBuf.append("<status p=\"").append(mr.p_count);
            xmlBuf.append("\" c=\"").append(mr.c_count);
            xmlBuf.append("\" f=\"").append(mr.f_count);
            xmlBuf.append("\" i=\"").append(mr.i_count);
            xmlBuf.append("\" b=\"").append(mr.b_count);
            xmlBuf.append("\" />").append(dbUtils.NEWL);
            xmlBuf.append("</module>");
        }
        xmlBuf.append("</module_list>");
        return xmlBuf.toString();
    }

    public void getAvgScore(Connection con) throws qdbException{
        try{
/*
            String SQL = " SELECT "
               + " mov_mod_id "
               + " , avg(mov_score) as avg_mov_score"
               + " FROM moduleEvaluation "
               + " WHERE "
               + " mov_cos_id = ? AND "
               + " mov_score is not null "
               + " GROUP BY mov_mod_id ";

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, mov_cos_id);
*/
			String SQL = " SELECT mov_mod_id, avg(mov_score) as avg_mov_score " +
				" FROM moduleEvaluation, Module " +
				" WHERE mov_cos_id = ? " +
				" And mov_mod_id = mod_res_id " +
				" And mov_score Is Not Null " +
				" And ( ( mod_type = ? Or mod_type = ?) And mov_status <> ? ) " +
				" GROUP BY mov_mod_id ";
			PreparedStatement stmt = con.prepareStatement(SQL);
			int index = 1;
			stmt.setLong(index++, mov_cos_id);
			stmt.setString(index++, dbModule.MOD_TYPE_DXT);
			stmt.setString(index++, dbModule.MOD_TYPE_TST);
			stmt.setString(index++, "I");
			ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Long mov_id= new Long(rs.getLong("mov_mod_id"));
//                String score = new Float(rs.getString("avg_mov_score"));
                CommonLog.debug("mov_id = " + mov_id + " vs " + convertScore(rs.getFloat("avg_mov_score")));
                avg_score.put(mov_id, convertScore(rs.getFloat("avg_mov_score")));
            }
            stmt.close();
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void getAvgScore(Connection con, Timestamp startDateTime, Timestamp endDateTime) throws qdbException{
        try{

            String SQL = " SELECT "
               + " mov_mod_id "
               + " , avg(mov_score) as avg_mov_score"
               + " FROM ModuleEvaluation, ModuleEvaluationHistory meh1, Module "
               + " WHERE "
               + " ( mod_type = ? Or mod_type = ?) AND "
               + " mov_status <> ? AND "
               + " mov_cos_id = ? AND "
			   + " mov_mod_id = mod_res_id And "
               + " mov_score is not null AND "
               + " mov_mod_id = mvh_mod_id AND "
               + " mov_tkh_id = mvh_tkh_id AND "
               + " mvh_last_acc_datetime = "
               + " (select max(meh2.mvh_last_acc_datetime) "
               + " from ModuleEvaluationHistory meh2 "
               + " where meh2.mvh_ent_id = meh1.mvh_ent_id "
               + " and meh2.mvh_mod_id = meh1.mvh_mod_id "
               + " and meh2.mvh_tkh_id = meh1.mvh_tkh_id ";
               if(startDateTime != null) {
                    SQL += " AND meh2.mvh_create_timestamp > ? ";
               }
               if(endDateTime != null) {
                    SQL += " AND meh2.mvh_create_timestamp < ? ";
               }
               if(startDateTime != null) {
                    SQL += " AND meh2.mvh_last_acc_datetime > ? ";
               }
               if(endDateTime != null) {
                    SQL += " AND meh2.mvh_last_acc_datetime < ? ";
               }
               SQL += ") ";

               SQL += " AND mvh_create_timestamp = "
               + " (select max(meh3.mvh_create_timestamp) "
               + " from ModuleEvaluationHistory meh3 "
               + " where meh3.mvh_ent_id = meh1.mvh_ent_id "
               + " and meh3.mvh_mod_id = meh1.mvh_mod_id "
               + " and meh3.mvh_tkh_id = meh1.mvh_tkh_id ";
               if(startDateTime != null) {
                    SQL += " AND meh3.mvh_create_timestamp > ? ";
               }
               if(endDateTime != null) {
                    SQL += " AND meh3.mvh_create_timestamp < ? ";
               }
               SQL += " AND meh3.mvh_last_acc_datetime = meh1.mvh_last_acc_datetime ";
               SQL += ") ";

/*
            if(startDateTime != null) {
                SQL += " AND mvh_create_timestamp > ? ";
            }
            if(endDateTime != null) {
                SQL += " AND mvh_create_timestamp < ? ";
            }
            if(startDateTime != null) {
                SQL += " AND mvh_last_acc_datetime > ? ";
            }
            if(endDateTime != null) {
                SQL += " AND mvh_last_acc_datetime < ? ";
            }
*/
            SQL += " GROUP BY mov_mod_id ";

            int index = 1;
            PreparedStatement stmt = con.prepareStatement(SQL);
			stmt.setString(index++, dbModule.MOD_TYPE_DXT);
			stmt.setString(index++, dbModule.MOD_TYPE_TST);
			stmt.setString(index++, "I");
            stmt.setLong(index++, mov_cos_id);
            
            if(startDateTime != null) {
                stmt.setTimestamp(index++, startDateTime);
            }
            if(endDateTime != null) {
                stmt.setTimestamp(index++, endDateTime);
            }

            if(startDateTime != null) {
                stmt.setTimestamp(index++, startDateTime);
            }
            if(endDateTime != null) {
                stmt.setTimestamp(index++, endDateTime);
            }

            if(startDateTime != null) {
                stmt.setTimestamp(index++, startDateTime);
            }
            if(endDateTime != null) {
                stmt.setTimestamp(index++, endDateTime);
            }
            /*
            if(startDateTime != null) {
                stmt.setTimestamp(index++, startDateTime);
            }
            if(endDateTime != null) {
                stmt.setTimestamp(index++, endDateTime);
            }
            */
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Long mov_id= new Long(rs.getLong("mov_mod_id"));
//                String score = new Float(rs.getString("avg_mov_score"));
                avg_score.put(mov_id, convertScore(rs.getFloat("avg_mov_score")));
            }
            stmt.close();
        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String getLearnerModuleXML(Vector vtModuleReport) throws qdbException{
        return getLearnerModuleXML(null, vtModuleReport, null);
    }
    public String getLearnerModuleXML(Connection con, Vector vtModuleReport, loginProfile prof) throws qdbException{
        
        StringBuffer xmlBuf = new StringBuffer();
        for (int i=0;i<vtModuleReport.size();i++) {
            ModuleReport mr = (ModuleReport) vtModuleReport.elementAt(i);
            xmlBuf.append("<module id=\"").append(mr.mov_mod_id);
            xmlBuf.append("\" type=\"").append(mr.mod_type);
            xmlBuf.append("\" title=\"").append(dbUtils.esc4XML(mr.mod_title));
            xmlBuf.append("\" status=\"").append(mr.mov_status);
            if (mr.mov_score != null){
                xmlBuf.append("\" score=\"").append(convertScore(Float.valueOf(mr.mov_score).floatValue()));
            }
            xmlBuf.append("\" hits=\"").append(mr.mov_total_attempt);
            xmlBuf.append("\" total_time=\"").append(dbAiccPath.getTime(mr.mov_total_time));
            xmlBuf.append("\" last_access=\"" ).append(mr.mov_last_acc_datetime);
            xmlBuf.append("\" src_type=\"").append(mr.mod_src_type);
            xmlBuf.append("\" src_link=\"").append(dbUtils.esc4XML(mr.mod_src_link));
            xmlBuf.append("\" mod_web_launch=\"").append(dbUtils.esc4XML(mr.mod_web_launch));
            
            if (mr.mod_vendor != null){
                xmlBuf.append("\" mod_vendor=\"").append(dbUtils.esc4XML(mr.mod_vendor));
            }
            else {
                xmlBuf.append("\" mod_vendor=\"").append("");
            }
            xmlBuf.append("\">").append(dbUtils.NEWL);
            if(prof != null && con != null && mr.mov_mod_id != 0) {
                dbModule mod = new dbModule();
                mod.mod_res_id = mr.mov_mod_id;
                mod.res_id = mr.mov_mod_id;
                try { 
                    mod.get(con);  
                    xmlBuf.append(mod.getModHeader(con, prof));
                } catch(cwSysMessage e) {
                    throw new qdbException("Cannot get module header from dbModuleEvaluation.getLEarnerModuleXML");
                }
            }
            xmlBuf.append("</module>");
        }
        return xmlBuf.toString();
    }


    public static long attemptNum(Connection con, long modId)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    " SELECT  count(mov_mod_id) AS CNT "
                    + "  FROM ModuleEvaluation "
                    + " WHERE "
                    + "  mov_mod_id = ? ");

            stmt.setLong(1, modId);
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

    public static String convertScore(float score){
        String result = null;
        if (score <= -100){
            if (score >= -101.5)
                result = "A+";
            else if (score >= -102.5)
                result = "A";
            else if (score >= -103.5)
                result = "A-";
            else if (score >= -104.5)
                result = "B+";
            else if (score >= -105.5)
                result = "B";
            else if (score >= -106.5)
                result = "B-";
            else if (score >= -107.5)
                result = "C+";
            else if (score >= -108.5)
                result = "C";
            else if (score >= -109.5)
                result = "C-";
            else if (score >= -110.5)
                result = "D";
            else if (score >= -111.5)
                result = "F";
            else
                result = "?";

            return result;
        }else return ("" + score);

    }

    private StringBuffer getAiccRptBodyAsXML(Connection con) throws SQLException{
        StringBuffer xml = new StringBuffer();
        StringBuffer sqlBuf = new StringBuffer();
        Vector vtObjId = new Vector();

        sqlBuf.append("select obj_desc, apm_score, apm_status, obj_id, apm_id  from Accomplishment, objective ");
        sqlBuf.append("WHERE apm_ent_id = ? AND obj_id = apm_obj_id AND apm_tkh_id = ? ");
        sqlBuf.append("AND obj_id IN ");
        sqlBuf.append("(SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ");
        sqlBuf.append("union ");
        sqlBuf.append("select obj_desc, 0 apm_score, N'N' apm_status, obj_id, 0 apm_id from objective ");
        sqlBuf.append("where obj_id IN ");
        sqlBuf.append("(SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ");
        sqlBuf.append("ORDER BY obj_id, apm_id desc");
        PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
        stmt.setLong(1, mov_ent_id);
        stmt.setLong(2, mov_tkh_id);
        stmt.setLong(3, mov_mod_id);
        stmt.setLong(4, mov_mod_id);
        ResultSet rs = stmt.executeQuery();

        String last_score  = null;
        String max_score  = null;
        String min_score  = null;
        long cur_obj_id =0;
        long prv_obj_id =0;
        String obj_desc = null;
        String status = null;
        
        while (rs.next()){
            cur_obj_id = rs.getLong("obj_id");
            if (cur_obj_id != prv_obj_id) {
                if (prv_obj_id > 0) {
                    xml.append("<objective id=\"");
                    xml.append(prv_obj_id);
                    xml.append("\" title=\"");
                    xml.append(dbUtils.esc4XML(obj_desc));
                    xml.append("\" status=\"");
                    xml.append(status);
                    xml.append("\" score=\"");
                    xml.append(last_score);
                    xml.append("\" max_score=\"");
                    xml.append(max_score);
                    xml.append("\" min_score=\"");
                    xml.append(min_score);
                    xml.append("\" />").append(dbUtils.NEWL);
                }
                obj_desc = rs.getString("obj_desc");
                status = rs.getString("apm_status");
                last_score = rs.getString("apm_score");
                max_score = last_score;
                min_score = last_score;
                prv_obj_id = cur_obj_id;

            }else {
                String cur_score = rs.getString("apm_score");
                if (cur_score != null && cur_score.length() > 0) {
                    Float f_cur_score = new Float(cur_score);
                    if (max_score == null ||
                        f_cur_score.floatValue() > (new Float(max_score)).floatValue()) {
                        max_score = cur_score;
                    }
                    if (min_score == null ||
                        f_cur_score.floatValue() < (new Float(min_score)).floatValue()) {
                        min_score = cur_score;
                    }
                }
            }
        }

        if (prv_obj_id > 0) {
            xml.append("<objective id=\"");
            xml.append(prv_obj_id);
            xml.append("\" title=\"");
            xml.append(dbUtils.esc4XML(obj_desc));
            xml.append("\" status=\"");
            xml.append(status);
            xml.append("\" score=\"");
            xml.append(last_score);
            xml.append("\" max_score=\"");
            xml.append(max_score);
            xml.append("\" min_score=\"");
            xml.append(min_score);
            xml.append("\" />").append(dbUtils.NEWL);
        }
        stmt.close();            
        return xml;
    }

    private String getSkillsoftRptBody(Connection con) throws SQLException{
        StringBuffer body = new StringBuffer();
        StringBuffer result = new StringBuffer();

        PreparedStatement stmt = con.prepareStatement("SELECT obj_developer_id, apm_score FROM Objective, Accomplishment WHERE apm_ent_id = ? AND obj_id = apm_obj_id AND apm_tkh_id = ? AND obj_developer_id IS NOT NULL AND obj_id IN (SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ORDER BY obj_developer_id,  apm_id");
        stmt.setLong(1, mov_ent_id);
        stmt.setLong(2, mov_tkh_id);
        stmt.setLong(3, mov_mod_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable rpt = new Hashtable();

        while (rs.next()) {
            String developer_id = rs.getString("obj_developer_id");
            int len = 0;

            if ((len = developer_id.length()) >= 16) {
                //String sc_cos_id = developer_id.substring(0, 8);
                String sc_les_id = developer_id.substring(len-16, len-13);
                String sc_top_id = developer_id.substring(len-13, len-10);
                String sc_pag_id = developer_id.substring(len-10, len-7);

                String score = rs.getString("apm_score");

                if (score != null && score.length() > 0) {
                    Hashtable les = (Hashtable)rpt.get(sc_les_id);
                    Hashtable topic = null;
                    Vector v_score = null;

                    if (les == null) {
                        les = new Hashtable();
                        topic = new Hashtable();
                        v_score = new Vector();
                    } else {
                        topic = (Hashtable)les.get(sc_top_id);

                        if (topic == null) {
                            topic = new Hashtable();
                            v_score = new Vector();
                        } else {
                            v_score = (Vector)topic.get(sc_pag_id);

                            if (v_score == null) {
                                v_score = new Vector();
                            }
                        }
                    }

                    v_score.addElement(score);
                    topic.put(sc_pag_id, v_score);
                    les.put(sc_top_id, topic);
                    rpt.put(sc_les_id, les);
                }
            }
        }
        stmt.close();

        stmt = con.prepareStatement("SELECT obj_developer_id FROM Objective WHERE (obj_developer_id LIKE ? OR obj_developer_id LIKE ?) AND obj_id IN (SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ORDER BY obj_developer_id");
        stmt.setString(1, "%PXX%");
        stmt.setString(2, "%MXX%");
        stmt.setLong(3, mov_mod_id);
        rs = stmt.executeQuery();
        Vector all_lesson_with_assessment = new Vector();

        while (rs.next()) {
            String developer_id = rs.getString("obj_developer_id");
            int len = 0;

            if ((len = developer_id.length()) >= 16) {
                String sc_les_id = developer_id.substring(len-16, len-13);

                if (! all_lesson_with_assessment.contains(sc_les_id)) {
                    all_lesson_with_assessment.addElement(sc_les_id);
                }
            }
        }
        stmt.close();
//System.out.println("###" + all_lesson_with_assessment);
        for (int x=0; x<all_lesson_with_assessment.size(); x++) {
            String sc_les_id = (String)all_lesson_with_assessment.elementAt(x);
            int total_que = 0;

            if (rpt != null && rpt.containsKey(sc_les_id)) {
//System.out.println("========================");
                Hashtable les = (Hashtable)rpt.get(sc_les_id);
//System.out.println("<><>" + sc_les_id);
                if (les != null) {
                    Enumeration topic_enumeration = les.keys();

                    if (topic_enumeration != null) {
                        Vector set = new Vector();
                        Vector type = new Vector();

                        while (topic_enumeration.hasMoreElements()) {
                            String sc_top_id = (String)topic_enumeration.nextElement();
                            Hashtable topic = (Hashtable)les.get(sc_top_id);

                            if (topic != null) {
                                Enumeration page_enumeration = topic.keys();

                                if (page_enumeration != null) {
                                    while (page_enumeration.hasMoreElements()) {
                                        String sc_pag_id = (String)page_enumeration.nextElement();
                                        Vector scores = (Vector)topic.get(sc_pag_id);
//System.out.print(sc_top_id + " | " + sc_pag_id + "   ");
                                        if (scores != null && scores.size() != 0) {
                                            total_que++;

                                            for (int i=0; i<scores.size(); i++) {
                                                String score = (String)scores.elementAt(i);
                                                int cur_correct = 0;
//System.out.print(score);
                                                if (i > set.size()-1) {
//System.out.print(" !!! ");
//System.out.print("(" + i + ")" + " (" + set.size() + ")");
                                                    set.addElement(new Integer(0));

                                                    if (score.startsWith("0") || score.startsWith("100")) {
                                                        type.addElement("m");
//System.out.print("M");
                                                    } else {
                                                        type.addElement("p");
//System.out.print("P");
                                                    }
                                                } else {
                                                    cur_correct = ((Integer)set.elementAt(i)).intValue();
//System.out.print("<><>" + cur_correct + "<><>");
                                                }

                                                if (score.startsWith("100") || score.startsWith("-101")) {
                                                    cur_correct++;
                                                    set.setElementAt(new Integer(cur_correct), i);
//System.out.print(" &&& ");
                                                }
                                            }
                                        }
//System.out.println(" $");
                                    }
                                }
                            }
                        }

//System.out.println("Question size=" + total_que);
//System.out.println("Set=" + set);
//System.out.println("Type=" + type);

                        int preassessment = -1;
                        int mastery_highest = -1;
                        int mastery_lastest = -1;
                        int score = -1;

                        for (int i=0; i<set.size(); i++) {
                            String les_type = (String)type.elementAt(i);
                            score = (((Integer)set.elementAt(i)).intValue()*100)/total_que;

                            if (les_type.equals("p")) {
                                preassessment = score;

                                if (mastery_lastest == -1) {
                                    mastery_lastest = score;
                                }
                            } else {
                                mastery_lastest = score;
                            }

                            if (score > mastery_highest) {
                                mastery_highest = score;
                            }
                        }

                        body.append("<lesson name=\"").append(new Long(sc_les_id)).append("\" preassessment=\"").append(preassessment).append("\" mastery_highest=\"").append(mastery_highest).append("\" mastery_lastest=\"").append(mastery_lastest).append("\"/>").append(dbUtils.NEWL);
//System.out.println("========================");
                    }
                }
            } else {
                body.append("<lesson name=\"").append(new Long(sc_les_id)).append("\" preassessment=\"-1\" mastery_highest=\"-1\" mastery_lastest=\"-1\"/>").append(dbUtils.NEWL);
            }
        }


        result.append("<skillsoft_rpt id=\"").append(mov_mod_id).append("\">");
        result.append(body.toString());
        result.append("</skillsoft_rpt>");

        return result.toString();
    }

/*    private String getSkillsoftRptBody(Connection con) throws SQLException{
        StringBuffer body = new StringBuffer();
        StringBuffer result = new StringBuffer();

        PreparedStatement stmt = con.prepareStatement("SELECT obj_developer_id, apm_score FROM Objective, Accomplishment WHERE apm_ent_id = ? AND obj_id = apm_obj_id AND obj_developer_id IS NOT NULL AND obj_id IN (SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ORDER BY obj_developer_id, apm_id");
        stmt.setLong(1, mov_ent_id);
        stmt.setLong(2, mov_mod_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable rpt = new Hashtable();

        while (rs.next()) {
            String developer_id = rs.getString("obj_developer_id");

            if (developer_id.length() != 0) {
                String sc_cos_id = developer_id.substring(0, 8);
                String sc_les_id = developer_id.substring(8, 11);
                String sc_top_id = developer_id.substring(11, 14);
//System.out.println("cos_id=" + sc_cos_id);
//System.out.println("les_id=" + sc_les_id);
//System.out.println("top_id=" + sc_top_id);
//System.out.println();

                String score = rs.getString("apm_score");

                if (score != null && score.length() > 0) {
                    Hashtable obj = (Hashtable)rpt.get(sc_les_id);
                    Vector topic = null;

                    if (obj == null) {
                        obj = new Hashtable();
                        topic = new Vector();
                    } else {
                        topic = (Vector)obj.get(sc_top_id);

                        if (topic == null) {
                            topic = new Vector();
                        }
                    }

                    topic.addElement(score);
                    obj.put(sc_top_id, topic);
                    rpt.put(sc_les_id, obj);

//                    if (! v_rpt.contains(sc_les_id)) {
//                        v_rpt.addElement(sc_les_id);
//                    }
                }
            }
        }

        stmt = con.prepareStatement("SELECT obj_developer_id FROM Objective WHERE (obj_developer_id LIKE ? OR obj_developer_id LIKE ?) AND obj_id IN (SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ORDER BY obj_developer_id");
        stmt.setString(1, "%PXX%");
        stmt.setString(2, "%MXX%");
        stmt.setLong(3, mov_mod_id);
        rs = stmt.executeQuery();
        Vector all_lesson_with_assessment = new Vector();

        while (rs.next()) {
            String developer_id = rs.getString("obj_developer_id");

            if (developer_id.length() != 0) {
                String sc_les_id = developer_id.substring(8, 11);

                if (! all_lesson_with_assessment.contains(sc_les_id)) {
                    all_lesson_with_assessment.addElement(sc_les_id);
                }
            }
        }
//System.out.println("###" + all_lesson_with_assessment);
        for (int x=0; x<all_lesson_with_assessment.size(); x++) {
            String sc_les_id = (String)all_lesson_with_assessment.elementAt(x);

            if (rpt != null && rpt.containsKey(sc_les_id)) {

//        if (v_rpt != null) {
//            for (int temp=0; temp<v_rpt.size(); temp++) {
//System.out.println("========================");
//                String sc_les_id = (String)v_rpt.elementAt(temp);
                Hashtable les = (Hashtable)rpt.get(sc_les_id);
//System.out.println("<><>" + sc_les_id);
                if (les != null) {
                    Enumeration topic_enumeration = les.keys();

                    if (topic_enumeration != null) {
                        Vector set = new Vector();
                        Vector type = new Vector();

                        while (topic_enumeration.hasMoreElements()) {
                            String sc_top_id = (String)topic_enumeration.nextElement();
                            Vector scores = (Vector)les.get(sc_top_id);
//System.out.print(sc_top_id + "   ");
                            if (scores != null) {
                                for (int i=0; i<scores.size(); i++) {
                                    String score = (String)scores.elementAt(i);
                                    int cur_correct = 0;
//System.out.print(score);
                                    if (i > set.size()-1) {
//System.out.print(" !!! ");
//System.out.print("(" + i + ")" + " (" + set.size() + ")");
                                        set.addElement(new Integer(0));

                                        if (score.startsWith("0") || score.startsWith("100")) {
                                            type.addElement("m");
//System.out.print("M");
                                        } else {
                                            type.addElement("p");
//System.out.print("P");
                                        }
                                    } else {
                                        cur_correct = ((Integer)set.elementAt(i)).intValue();
//System.out.print("<><>" + cur_correct + "<><>");
                                    }

                                    if (score.startsWith("100") || score.startsWith("-101")) {
                                        cur_correct++;
                                        set.setElementAt(new Integer(cur_correct), i);
//System.out.print(" &&& ");
                                    }
                                }
                            }
//System.out.println(" $");
                        }

                        int obj_size = les.size();
//System.out.println("Question size=" + obj_size);
//System.out.println("Set=" + set);
//System.out.println("Type=" + type);

                        int preassessment = -1;
                        int mastery_highest = -1;
                        int mastery_lastest = -1;
                        int score = -1;

                        for (int i=0; i<set.size(); i++) {
                            String les_type = (String)type.elementAt(i);
                            score = (((Integer)set.elementAt(i)).intValue()*100)/obj_size;

                            if (les_type.equals("p")) {
                                preassessment = score;

                                if (mastery_lastest == -1) {
                                    mastery_lastest = score;
                                }
                            } else {
                                mastery_lastest = score;
                            }

                            if (score > mastery_highest) {
                                mastery_highest = score;
                            }
                        }

                        body.append("<lesson name=\"").append(sc_les_id).append("\" preassessment=\"").append(preassessment).append("\" mastery_highest=\"").append(mastery_highest).append("\" mastery_lastest=\"").append(mastery_lastest).append("\"/>").append(dbUtils.NEWL);
//System.out.println("========================");
                    }
                }
            } else {
                body.append("<lesson name=\"").append(sc_les_id).append("\" preassessment=\"-1\" mastery_highest=\"-1\" mastery_lastest=\"-1\"/>").append(dbUtils.NEWL);
            }
        }

        stmt.close();

        result.append("<skillsoft_rpt id=\"").append(mov_mod_id).append("\">");
        result.append(body.toString());
        result.append("</skillsoft_rpt>");

        return result.toString();
    }*/

    public boolean get(Connection con) throws SQLException{
    	boolean bFlag=false;
        String SQL = " SELECT mov_cos_id, mov_ent_id, mov_mod_id, mov_last_acc_datetime, mov_ele_loc, mov_total_time, mov_total_attempt, mov_status, mov_score,mov_not_mark_ind FROM ModuleEvaluation "
                  + "    WHERE mov_ent_id = ? "
                  + "      AND mov_mod_id = ? " 
                  + "      AND mov_tkh_id = ? " ;

            if (mov_cos_id != 0){
                  SQL += " AND mov_cos_id = ? ";
            }

        PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(1, mov_ent_id);
            stmt.setLong(2, mov_mod_id);
            stmt.setLong(3, mov_tkh_id);

            if (mov_cos_id != 0){
                stmt.setLong(4, mov_cos_id);
            }

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                mov_last_acc_datetime = rs.getTimestamp("mov_last_acc_datetime");
                mov_ele_loc = rs.getString("mov_ele_loc");
                mov_total_time = rs.getFloat("mov_total_time");
                mov_total_attempt = rs.getLong("mov_total_attempt");
                mov_status = rs.getString("mov_status");
                mov_score = rs.getString("mov_score");
                mov_cos_id = rs.getLong("mov_cos_id");
                mov_not_mark_ind =rs.getInt("mov_not_mark_ind");
                bFlag= true;
            }else{
                bFlag=false;
            }
            stmt.close();
            return bFlag;

    }

    public Timestamp getLastAccess(Connection con) throws SQLException{
        String SQL = " SELECT mov_last_acc_datetime FROM ModuleEvaluation "
                  + "    WHERE mov_ent_id = ? "
                  + "      AND mov_mod_id = ? " 
                  + "      AND mov_tkh_id = ? " ;

        if (mov_cos_id != 0){
                SQL += " AND mov_cos_id = ? ";
        }

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL);

            stmt.setLong(1, mov_ent_id);
            stmt.setLong(2, mov_mod_id);
            stmt.setLong(3, mov_tkh_id);
            if (mov_cos_id != 0){
                stmt.setLong(4, mov_cos_id);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mov_last_acc_datetime = rs.getTimestamp("mov_last_acc_datetime");
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return mov_last_acc_datetime;
    }



    public float getSmartForceScore(Connection con)
        throws SQLException {

            float score;
            String SQL = " SELECT mov_score FROM ModuleEvaluation "
                       + " WHERE mov_mod_id = ? AND mov_ent_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mov_mod_id);
            stmt.setLong(2, mov_ent_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                score = rs.getFloat("mov_score");
            else
                score = -1.0f;
            stmt.close();
            return score;
        }

    public float[] getSkillsoftScore(Connection con) throws SQLException{

        PreparedStatement stmt = con.prepareStatement("SELECT obj_developer_id, apm_score FROM Objective, Accomplishment WHERE apm_ent_id = ? AND obj_id = apm_obj_id AND obj_developer_id IS NOT NULL AND obj_id IN (SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ORDER BY obj_developer_id, apm_id");
        stmt.setLong(1, mov_ent_id);
        stmt.setLong(2, mov_mod_id);
        ResultSet rs = stmt.executeQuery();
        Hashtable rpt = new Hashtable();

        while (rs.next()) {
            String developer_id = rs.getString("obj_developer_id");
            int len = 0;

            if ((len = developer_id.length()) >= 16) {
                String sc_les_id = developer_id.substring(len-16, len-13);
                String sc_top_id = developer_id.substring(len-13, len-10);
                String sc_pag_id = developer_id.substring(len-10, len-7);

                String score = rs.getString("apm_score");

                if (score != null && score.length() > 0) {
                    Hashtable les = (Hashtable)rpt.get(sc_les_id);
                    Hashtable topic = null;
                    Vector v_score = null;

                    if (les == null) {
                        les = new Hashtable();
                        topic = new Hashtable();
                        v_score = new Vector();
                    } else {
                        topic = (Hashtable)les.get(sc_top_id);

                        if (topic == null) {
                            topic = new Hashtable();
                            v_score = new Vector();
                        } else {
                            v_score = (Vector)topic.get(sc_pag_id);

                            if (v_score == null) {
                                v_score = new Vector();
                            }
                        }
                    }

                    v_score.addElement(score);
                    topic.put(sc_pag_id, v_score);
                    les.put(sc_top_id, topic);
                    rpt.put(sc_les_id, les);
                }
            }
        }
        stmt.close();

        stmt = con.prepareStatement("SELECT obj_developer_id FROM Objective WHERE (obj_developer_id LIKE ? OR obj_developer_id LIKE ?) AND obj_id IN (SELECT rob_obj_id FROM ResourceObjective WHERE rob_res_id = ?) ORDER BY obj_developer_id");
        stmt.setString(1, "%PXX%");
        stmt.setString(2, "%MXX%");
        stmt.setLong(3, mov_mod_id);
        rs = stmt.executeQuery();
        Vector all_lesson_with_assessment = new Vector();

        while (rs.next()) {
            String developer_id = rs.getString("obj_developer_id");
            int len = 0;

            if ((len = developer_id.length()) >= 16) {
                String sc_les_id = developer_id.substring(len-16, len-13);

                if (! all_lesson_with_assessment.contains(sc_les_id)) {
                    all_lesson_with_assessment.addElement(sc_les_id);
                }
            }
        }
        stmt.close();

        boolean attempt = false;
        float[] ave_score = { 0.0f, 0.0f, 0.0f };
        int[] count = { 0, 0, 0 };

        for (int x=0; x<all_lesson_with_assessment.size(); x++) {
            String sc_les_id = (String)all_lesson_with_assessment.elementAt(x);
            int total_que = 0;
            if (rpt != null && rpt.containsKey(sc_les_id)) {

                attempt = true;
                Hashtable les = (Hashtable)rpt.get(sc_les_id);

                if (les != null) {
                    Enumeration topic_enumeration = les.keys();

                    if (topic_enumeration != null) {
                        Vector set = new Vector();
                        Vector type = new Vector();

                        while (topic_enumeration.hasMoreElements()) {
                            String sc_top_id = (String)topic_enumeration.nextElement();
                            Hashtable topic = (Hashtable)les.get(sc_top_id);

                            if (topic != null) {
                                Enumeration page_enumeration = topic.keys();

                                if (page_enumeration != null) {
                                    while (page_enumeration.hasMoreElements()) {
                                        String sc_pag_id = (String)page_enumeration.nextElement();
                                        Vector scores = (Vector)topic.get(sc_pag_id);

                                        if (scores != null && scores.size() != 0) {
                                            total_que++;

                                            for (int i=0; i<scores.size(); i++) {
                                                String score = (String)scores.elementAt(i);
                                                int cur_correct = 0;

                                                if (i > set.size()-1) {
                                                    set.addElement(new Integer(0));

                                                    if (score.startsWith("0") || score.startsWith("100")) {
                                                        type.addElement("m");

                                                    } else {
                                                        type.addElement("p");

                                                    }
                                                } else {
                                                    cur_correct = ((Integer)set.elementAt(i)).intValue();

                                                }

                                                if (score.startsWith("100") || score.startsWith("-101")) {
                                                    cur_correct++;
                                                    set.setElementAt(new Integer(cur_correct), i);

                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        int preassessment = -1;
                        int mastery_highest = -1;
                        int mastery_lastest = -1;
                        int score = -1;

                        for (int i=0; i<set.size(); i++) {
                            String les_type = (String)type.elementAt(i);
                            score = (((Integer)set.elementAt(i)).intValue()*100)/total_que;

                            if (les_type.equals("p")) {
                                preassessment = score;
                                ave_score[0] += preassessment;
                                count[0]++;

                                if (mastery_lastest == -1) {
                                    mastery_lastest = score;
                                    ave_score[2] += mastery_lastest;
                                    count[2]++;
                                }
                            } else {
                                mastery_lastest = score;
                                ave_score[2] += mastery_lastest;
                                count[2]++;
                            }

                            if (score > mastery_highest) {
                                mastery_highest = score;
                                ave_score[1] += mastery_highest;
                                count[1]++;
                            }
                        }
                        /*
                        ave_score[0] += preassessment;
                        ave_score[1] += mastery_highest;
                        ave_score[2] += mastery_lastest;
                        */
                        //body.append("<lesson name=\"").append(new Long(sc_les_id)).append("\" preassessment=\"").append(preassessment).append("\" mastery_highest=\"").append(mastery_highest).append("\" mastery_lastest=\"").append(mastery_lastest).append("\"/>").append(dbUtils.NEWL);

                    }
                }
            }
            /*
            else {
                ave_score[0] = -1.0;
                ave_score[1] = -1.0;
                ave_score[2] = -1.0;
                //body.append("<lesson name=\"").append(new Long(sc_les_id)).append("\" preassessment=\"-1\" mastery_highest=\"-1\" mastery_lastest=\"-1\"/>").append(dbUtils.NEWL);
            }
            */
        }


        if( attempt ) {
            if( count[0] > 0 ) {
                ave_score[0] /= count[0];
            } else {
                ave_score[0] = -1.0f;
            }
            if( count[1] > 0 ) {
                ave_score[1] /= count[1];
            } else {
                ave_score[1] = -1.0f;
            }
            if( count[2] > 0 ) {
                ave_score[2] /= count[2];
            } else {
                ave_score[2] = -1.0f;
            }
        } else {
            ave_score[0] = -1.0f;
            ave_score[1] = -1.0f;
            ave_score[2] = -1.0f;
        }
        return ave_score;
    }

    public static String getStatus(Connection con, loginProfile prof, long mod_res_id, long course_id) throws SQLException, qdbException{
        return getStatus(con, prof, mod_res_id, DbTrackingHistory.TKH_ID_UNDEFINED, course_id);
    }

    public static String getStatus(Connection con, loginProfile prof, long mod_res_id, long tkh_id, long course_id) throws SQLException, qdbException{
    	Timestamp cur_time = cwSQL.getTime(con);
    	String encryptedTime = cwUtils.hash(String.valueOf(cur_time));
        StringBuffer result = new StringBuffer();
        dbModuleEvaluation dbmov = new dbModuleEvaluation();
        dbmov.mov_ent_id = prof.usr_ent_id;
        dbmov.mov_mod_id = mod_res_id;
        dbmov.mov_tkh_id = tkh_id;
        String status = dbmov.getStatus(con);      
        
        result.append(dbUtils.xmlHeader);
        result.append("<module id=\"").append(mod_res_id).append("\" tkh_id=\"").append(dbmov.mov_tkh_id).append("\" course_id=\"").append(course_id)
        	.append("\" start_time=\"").append(cur_time).append("\" encrypted_start_time=\"").append(encryptedTime).append("\">");
        result.append(prof.asXML());
        result.append("<aicc_data tkh_id=\"").append(dbmov.mov_tkh_id).append("\">");
        result.append("<attempt status=\"").append(status).append("\"/>");
        result.append("</aicc_data>");
        if (course_id > 0) {
        	boolean isOnlineExam = aeItem.isOnlineExam(aeItem.getItemByContentMod(con, mod_res_id));
        	result.append("<isOnlineExam>").append(isOnlineExam).append("</isOnlineExam>");
        	if (isOnlineExam) {
        		long itm_id = dbCourse.getCosItemId(con, course_id);
        		result.append("<isPause>").append(ExamController.isPause(itm_id, prof.usr_ent_id)).append("</isPause>");
        	}
        }
        result.append("</module>");

        return result.toString();
    }

    public static long getTotalAttempt(Connection con, long mod_id, long ent_id) throws SQLException, cwException {
        return getTotalAttempt(con, mod_id, ent_id, DbTrackingHistory.getAppTrackingIDByMod(con, mod_id, ent_id));
    }

    public static long getTotalAttempt(Connection con, long mod_id, long ent_id, long tkh_id) throws SQLException{
        long total_attempt = 0;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("SELECT mov_total_attempt FROM moduleEvaluation WHERE mov_ent_id = ? AND mov_mod_id = ? AND mov_tkh_id = ? ");
            stmt.setLong(1, ent_id);
            stmt.setLong(2, mod_id);
            stmt.setLong(3, tkh_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                total_attempt = rs.getLong("mov_total_attempt");
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return total_attempt;
    }

    private static final String sql_get_cnt_by_usr_n_cos =
        " Select count(*) as cnt from ModuleEvaluation " +
        " Where mov_ent_id = ? " +
        " And mov_cos_id = ? " +
        " And mov_tkh_id = ? ";

    static long getCountByUsrNCos(Connection con, long usr_ent_id, long cos_res_id, long tkh_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_get_cnt_by_usr_n_cos);
        stmt.setLong(1, usr_ent_id);
        stmt.setLong(2, cos_res_id);
        stmt.setLong(3, tkh_id);
        ResultSet rs = stmt.executeQuery();
        long count;
        if(rs.next()) {
            count = rs.getLong("cnt");
        } else {
            count = 0;
        }
        rs.close();
        stmt.close();
        return count;
    }

	private static final String sql_del_by_mod = "DELETE FROM ModuleEvaluation WHERE mov_mod_id = ? ";
    public static void delByMod(Connection con, long mod_id) throws qdbException{
        try{
            PreparedStatement stmt = con.prepareStatement(sql_del_by_mod);
            stmt.setLong(1, mod_id);
            stmt.executeUpdate();
            stmt.close();
        }catch(SQLException e){
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    /**
    * Check the module evaluation last update timestamp
    * If last update timestamp is equal to the timestamp passed into the function
    * then return true otherwise return false
    Pre-define variables:
    <ul>
    <li>mov_mod_id
    <li>mov_ent_id
    <ul>
    */
    public boolean checkUpdateTimestamp(Connection con, Timestamp upd_timestamp)
        throws SQLException, cwException {
            if( upd_timestamp == null )
                return false;

            String SQL = " SELECT mov_update_timestamp FROM ModuleEvaluation "
                       + " WHERE mov_mod_id = ? AND mov_ent_id = ? AND mov_tkh_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.mov_mod_id);
            stmt.setLong(2, this.mov_ent_id);
            stmt.setLong(3, this.mov_tkh_id);
            ResultSet rs = stmt.executeQuery();
            Timestamp last_upd_timestamp = null;
            if(rs.next())
                last_upd_timestamp = rs.getTimestamp("mov_update_timestamp");
            else
                throw new cwException("Failed to get the module evaluation record, mod_id = " + this.mov_mod_id + " , mov_ent_id = " + this.mov_ent_id + " , mov_tkh_id = " + this.mov_tkh_id);
            stmt.close();
            if(last_upd_timestamp == null || last_upd_timestamp.equals(upd_timestamp))
                return true;
            else
                return false;
        }


    /**
    * Delete the module evaluation record
    */
    public void del(Connection con)
        throws SQLException, cwException{

            String SQL = " DELETE FROM ModuleEvaluation "
                       + " WHERE mov_mod_id = ? AND mov_ent_id = ? AND mov_tkh_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.mov_mod_id);
            stmt.setLong(2, this.mov_ent_id);
            stmt.setLong(3, this.mov_tkh_id);
            stmt.executeUpdate();
            stmt.close();
            return ;
        }

    /**
    *Change user module evaluation status
    Pre-define variables:
    <ul>
    <li>mov_mod_id
    <li>mov_status
    <li>mov_ent_id
    <ul>
     * @throws qdbErrMessage 
    */
    public void changeUserStatus(Connection con, loginProfile prof)
        throws SQLException, cwSysMessage, qdbException, qdbErrMessage {

            String cur_status = getStatus(con);
            //If status no change, only update the module evaluation record
            if( cur_status != null && !cur_status.equalsIgnoreCase(this.mov_status) ) {
                String usr_id = dbRegUser.usrEntId2UsrId(con, this.mov_ent_id);
                dbProgressAttempt dbAtm = new dbProgressAttempt();
                dbAtm.atm_pgr_usr_id = usr_id;
                dbAtm.atm_pgr_res_id = this.mov_mod_id;
                dbAtm.atm_tkh_id = this.mov_tkh_id;
                dbAtm.del(con);

                dbProgress dbPgr = new dbProgress();
                dbPgr.pgr_usr_id = usr_id;
                dbPgr.pgr_res_id = this.mov_mod_id;
                dbPgr.pgr_tkh_id = this.mov_tkh_id;
                dbPgr.del(con);
            }
            this.mov_cos_id = dbModule.getCosId(con, this.mov_mod_id);
            this.mov_last_acc_datetime = cwSQL.getTime(con);
            this.mov_total_time = 0;

            this.mov_create_usr_id = prof.usr_id;
            this.mov_create_timestamp = this.mov_last_acc_datetime;
            this.mov_update_usr_id = prof.usr_id;
            this.mov_update_timestamp = this.mov_last_acc_datetime;
            this.save(con, prof);
            return;

        }

    /**
    * Get user module evaluation status
    Pre-define variables:
    <ul>
    <li>mov_mod_id
    <li>mov_ent_id
    <li>mov_tkh_id
    <ul>
    */
    public String getStatus(Connection con)
        throws SQLException, qdbException {

            if (mov_tkh_id <= 0) {
                try {
                    mov_tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, mov_mod_id, mov_ent_id);
                }catch(cwException e) {
                    throw new qdbException(e.getMessage());
                }
            }
            
            String SQL = " SELECT mov_status FROM ModuleEvaluation "
                       + " WHERE mov_mod_id = ? AND mov_ent_id = ? and mov_tkh_id = ?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.mov_mod_id);
            stmt.setLong(2, this.mov_ent_id);
            stmt.setLong(3, this.mov_tkh_id);
            ResultSet rs = stmt.executeQuery();
            String status = null;
            if(rs.next())
                status = rs.getString("mov_status");
            stmt.close();
            return status;
        }

    public static int getStatusCountByModule(Connection con, long mod_id, String mov_status)
        throws SQLException{
            Vector v_mov_status = new Vector();
            v_mov_status.addElement(mov_status);
            return ViewModuleEvaluation.getStatusCountByModule(con, mod_id, v_mov_status);
    }


    public static int getStatusCountByModule(Connection con, long mod_id, Vector v_mov_status)
        throws SQLException{

            return ViewModuleEvaluation.getStatusCountByModule(con, mod_id, v_mov_status);
    }


    public static String getByModuleAsXML(Connection con, HttpSession sess, loginProfile prof, long mod_id, cwPagination cwPage, String mov_status) throws SQLException, cwException,  qdbException,  cwSysMessage {
        return getByModuleAsXML(con, sess, prof, mod_id, cwPage, mov_status, null, null, null, null, true);
    }    

    public static String getByModuleAsXML(Connection con, HttpSession sess, loginProfile prof, long mod_id, cwPagination cwPage, String mov_status, Timestamp startDate, Timestamp endDate, long[] ent_id_lst, long[] ugr_ent_id_lst, boolean byEnrollment)
        throws SQLException, cwException,  qdbException,  cwSysMessage {
        return getByModuleAsXML(con, sess, prof, mod_id, cwPage, mov_status, startDate, endDate, ent_id_lst, ugr_ent_id_lst, byEnrollment, null);
    }   

    public static String getByModuleAsXML(Connection con, HttpSession sess, loginProfile prof, long mod_id, cwPagination cwPage, String mov_status, Timestamp startDate, Timestamp endDate, long[] ent_id_lst, long[] ugr_ent_id_lst, boolean byEnrollment, WizbiniLoader wizbini)
        throws SQLException, cwException,  qdbException,  cwSysMessage {

            if( cwPage.curPage == 0 )
                cwPage.curPage = 1;
            if( cwPage.pageSize == 0 )
                cwPage.pageSize = 1000;
            if( cwPage.sortCol == null || cwPage.sortCol.length() == 0 )
                cwPage.sortCol = "usr_display_bil";
            if( cwPage.sortOrder == null || cwPage.sortOrder.length() == 0 )
                cwPage.sortOrder = "ASC";

            Timestamp ts = (Timestamp)sess.getAttribute(SESS_PAGINATION_TIMESTAMP);
            String sort_col = (String)sess.getAttribute(SESS_PAGINATION_SORT_COL);
            String sort_order = (String)sess.getAttribute(SESS_PAGINATION_SORT_ORDER);
            Vector resultVec = null;

            if( ts != null && ts.equals(cwPage.ts) &&
                sort_col != null && sort_col.equalsIgnoreCase(cwPage.sortCol) &&
                sort_order != null && sort_order.equalsIgnoreCase(cwPage.sortOrder) ) {
CommonLog.debug(" ------------------ Use Session ------------------ ");
                resultVec = (Vector)sess.getAttribute( SESS_PAGINATION_USER_LIST_RESULT );
            } else {
                //timestamp not correct or sort other column/order, search again
                cwPage.ts = cwSQL.getTime(con);
                sess.setAttribute(SESS_PAGINATION_TIMESTAMP, cwPage.ts);
                sess.setAttribute(SESS_PAGINATION_SORT_COL, cwPage.sortCol);
                sess.setAttribute(SESS_PAGINATION_SORT_ORDER, cwPage.sortOrder);
                ViewModuleEvaluation viewMov = new ViewModuleEvaluation();
                resultVec = viewMov.getByModuleAsVector(con, mod_id, cwPage, mov_status, ent_id_lst, ugr_ent_id_lst, startDate, endDate, byEnrollment);
                sess.setAttribute(SESS_PAGINATION_USER_LIST_RESULT, resultVec);
            }

            cwPage.totalRec = resultVec.size();
            cwPage.totalPage = (int)Math.ceil( ((double)resultVec.size())/cwPage.pageSize );

            StringBuffer buf = new StringBuffer();
            buf.append("<module_submission>");

            buf.append("<module_type_reference_data>")
               .append((String)dbModuleType.moduleType2StatusXML.get(dbModuleType.MOD_TYPE_SVY))
               .append("</module_type_reference_data>");

            dbModule dbMod = new dbModule();
            dbMod.mod_res_id = mod_id;
            dbMod.get(con);
            buf.append("<module_info>")
               .append(dbMod.getModHeader(con, prof))
               .append("</module_info>");

            Vector moduleStatus = dbModuleType.getStatusVector(dbModuleType.MOD_TYPE_SVY);
            Hashtable statusCount = new Hashtable();
            int total = 0;
            for(int i=0; i<moduleStatus.size(); i++){
                int count = getStatusCountByModule(con, mod_id, (String)moduleStatus.elementAt(i));
                total += count;
                statusCount.put( (String)moduleStatus.elementAt(i), new Integer(count));
            }
            buf.append("<status_count_list total=\"").append(total).append("\">");
            for(int i=0; i<moduleStatus.size(); i++){
                buf.append("<status ")
                   .append(" id=\"").append(moduleStatus.elementAt(i)).append("\" ")
                   .append(" count=\"").append(statusCount.get((String)moduleStatus.elementAt(i))).append("\" ")
                   .append("/>");
            }
            buf.append("</status_count_list>");

            buf.append("<submission_list ")
               .append(" cur_status=\"").append(cwUtils.escNull(mov_status)).append("\" ")
               .append(">");

            //eg. 1 - 100, start = 1, end = 100
            int start = (cwPage.curPage-1) * cwPage.pageSize + 1;
            int end = start + cwPage.pageSize - 1;
            start -= 1; end -= 1; //Vector start from 0
            dbRegUser dbUsr = new dbRegUser();
            for(int i=start; i<=end && i<resultVec.size(); i++){
                ViewModuleEvaluation viewMov = (ViewModuleEvaluation)resultVec.elementAt(i);
                buf.append("<submission ")
                   .append(" status=\"").append(viewMov.mov_status).append("\" ")
                   .append(" tkh_id=\"").append(cwUtils.escZero(viewMov.tkh_id)).append("\" ")
                   .append(" last_update_timestamp=\"").append(viewMov.mov_update_timestamp).append("\" ")
                   .append(" submission_timestamp=\"").append(viewMov.pgr_complete_datetime).append("\" ")
                   .append(" >");

                dbUsr.usr_ent_id = viewMov.usr_ent_id;
                dbUsr.get(con);
                buf.append( (dbUsr.getUserShortXML(con, false, true, false, false, wizbini)).toString() );

                buf.append("</submission>");
            }
            buf.append("</submission_list>");
            buf.append(cwPage.asXML().toString());

            buf.append("</module_submission>");
            return buf.toString();
        }


 public static String getModuleByNameAsXML(String muduleName,Connection con, HttpSession sess, loginProfile prof, long mod_id, cwPagination cwPage, String mov_status, String mode)
        throws SQLException, cwException,  qdbException,  cwSysMessage {

            if( cwPage.curPage == 0 )
                cwPage.curPage = 1;
            if( cwPage.pageSize == 0 )
                cwPage.pageSize = 1000;
            if( cwPage.sortCol == null || cwPage.sortCol.length() == 0 )
                cwPage.sortCol = "usr_display_bil";
            if( cwPage.sortOrder == null || cwPage.sortOrder.length() == 0 )
                cwPage.sortOrder = "ASC";

            Timestamp ts = (Timestamp)sess.getAttribute(SESS_PAGINATION_TIMESTAMP);
            String sort_col = (String)sess.getAttribute(SESS_PAGINATION_SORT_COL);
            String sort_order = (String)sess.getAttribute(SESS_PAGINATION_SORT_ORDER);
            Vector resultVec = null;

            if( ts != null && ts.equals(cwPage.ts) &&
                sort_col != null && sort_col.equalsIgnoreCase(cwPage.sortCol) &&
                sort_order != null && sort_order.equalsIgnoreCase(cwPage.sortOrder) ) {
            	CommonLog.info(" ------------------ Use Session ------------------ ");
                resultVec = (Vector)sess.getAttribute( SESS_PAGINATION_USER_LIST_RESULT );
            } else {
                //timestamp not correct or sort other column/order, search again
                cwPage.ts = cwSQL.getTime(con);
                sess.setAttribute(SESS_PAGINATION_TIMESTAMP, cwPage.ts);
                sess.setAttribute(SESS_PAGINATION_SORT_COL, cwPage.sortCol);
                sess.setAttribute(SESS_PAGINATION_SORT_ORDER, cwPage.sortOrder);
                ViewModuleEvaluation viewMov = new ViewModuleEvaluation();
                resultVec = viewMov.getByModuleAsVector(con, mod_id, cwPage, mov_status, null, null, null,null, true);
                sess.setAttribute(SESS_PAGINATION_USER_LIST_RESULT, resultVec);
            }

            cwPage.totalRec = resultVec.size();
            cwPage.totalPage = (int)Math.ceil( ((double)resultVec.size())/cwPage.pageSize );

            StringBuffer buf = new StringBuffer();
            buf.append("<module_submission>");

            buf.append("<module_type_reference_data>")
               .append((String)dbModuleType.moduleType2StatusXML.get(muduleName))
               .append("</module_type_reference_data>");

            dbModule dbMod = new dbModule();
            dbMod.mod_res_id = mod_id;
            dbMod.get(con);
            buf.append("<module_info>")
               .append(dbMod.getModHeader(con, prof))
               .append("</module_info>");

            Vector moduleStatus = dbModuleType.getStatusVector(muduleName);
            Hashtable statusCount = new Hashtable();
            int total = 0;
            for(int i=0; i<moduleStatus.size(); i++){
                int count = getStatusCountByModule(con, mod_id, (String)moduleStatus.elementAt(i));
                total += count;
                statusCount.put( (String)moduleStatus.elementAt(i), new Integer(count));
            }
            buf.append("<status_count_list total=\"").append(total).append("\">");
            for(int i=0; i<moduleStatus.size(); i++){
                buf.append("<status ")
                   .append(" id=\"").append(moduleStatus.elementAt(i)).append("\" ")
                   .append(" count=\"").append(statusCount.get((String)moduleStatus.elementAt(i))).append("\" ")
                   .append("/>");
            }
            buf.append("</status_count_list>");

            buf.append("<submission_list ")
               .append(" cur_status=\"").append(cwUtils.escNull(mov_status)).append("\" ")
               .append(" mode=\"").append(cwUtils.escNull(mode)).append("\" ")
               .append(">");

            //eg. 1 - 100, start = 1, end = 100
            int start = (cwPage.curPage-1) * cwPage.pageSize + 1;
            int end = start + cwPage.pageSize - 1;
            start -= 1; end -= 1; //Vector start from 0
            dbRegUser dbUsr = new dbRegUser();
            for(int i=start; i<=end && i<resultVec.size(); i++){
                ViewModuleEvaluation viewMov = (ViewModuleEvaluation)resultVec.elementAt(i);
                buf.append("<submission ")
                   .append(" status=\"").append(viewMov.mov_status).append("\" ")
                   .append(" tkh_id=\"").append(cwUtils.escZero(viewMov.tkh_id)).append("\" ")
                   .append(" score=\"").append(viewMov.score).append("\" ")
                   .append(" grade=\"").append(viewMov.grade).append("\" ")
                   .append(" last_update_timestamp=\"").append(viewMov.mov_update_timestamp).append("\" ")
                   .append(" submission_timestamp=\"").append(viewMov.pgr_complete_datetime).append("\" ")
                   .append(" >");

                dbUsr.usr_ent_id = viewMov.usr_ent_id;
                dbUsr.get(con);
                buf.append( (dbUsr.getUserShortXML(con, false, true, false)).toString() );

                buf.append("</submission>");
            }
            buf.append("</submission_list>");
            buf.append(cwPage.asXML().toString());

            buf.append("</module_submission>");
            return buf.toString();
        }


  public String getEASResultByIndividualAsXML(Connection con, long mov_mod_id, long mov_ent_id, long tkh_id, loginProfile prof) throws qdbException{
        String xml = "";

        xml += "<module_submission>" + dbUtils.NEWL;
        //xml += prof.asXML() + dbUtils.NEWL;
        xml += getEASModuleEvalAsXML(con, mov_mod_id, mov_ent_id, tkh_id, prof);
        xml += "</module_submission>" + dbUtils.NEWL;
        return xml;

  }

  public String getEASModuleEvalAsXML(Connection con, long modId, long entId , long tkhId, loginProfile prof)
    throws qdbException{

        try {
            String xml = new String();
            StringBuffer xmlBuf = new StringBuffer(2048);
            dbModule dbMod = new dbModule();
            dbMod.mod_res_id = modId;
            dbMod.get(con);
            xmlBuf.append("<module_info>")
            .append(dbMod.getModHeader(con, prof))
            .append("</module_info>");


            String sql = "SELECT * FROM ModuleEvaluation, progress"
               + " WHERE mov_ent_id = ? AND mov_mod_id = ? AND mov_tkh_id = ? AND pgr_res_id = mov_mod_id AND pgr_tkh_id = mov_tkh_id AND "
               + " pgr_usr_id =(SELECT usr_id FROM Reguser WHERE usr_ent_id = mov_ent_id) ";
            PreparedStatement stmt = con.prepareStatement(sql);
//            System.out.println("sql: " + sql);
//            System.out.println("entId: " + entId);
//            System.out.println("modId: " + modId);
            stmt.setLong(1, entId);
            stmt.setLong(2, modId);
            stmt.setLong(3, tkhId);
            ResultSet rs = stmt.executeQuery();

            //xml = generateXML(con, rs, entId);
            String usrId = "";
            int attemptNo = 0;


            if (rs.next()) {
                String score_s  =  null;
                if (rs.getString("mov_score")!= null){
                    score_s = convertScore(rs.getFloat("mov_score"));
                }else   score_s  =  "";
                xmlBuf.append("<submission status=\"").append(rs.getString("mov_status"));
                xmlBuf.append("\" tkh_id=\"").append(tkhId);
                xmlBuf.append("\" score=\"").append(score_s).append("\" grade=\"").append(rs.getString("pgr_grade"));
                xmlBuf.append("\" last_acc_datetime=\"").append(rs.getTimestamp("mov_last_acc_datetime"));
                try{
                    xmlBuf.append("\" last_update_timestamp=\"").append(cwUtils.escNull(rs.getTimestamp("mov_update_timestamp")));
                }catch(SQLException e){
                    //For backward compatible, the resultset passed to the function may not contain mov_update_timestamp column
                }
                usrId = rs.getString("pgr_usr_id");
                attemptNo = rs.getInt("pgr_attempt_nbr");
            }
            else{
                xmlBuf.append("<submission status=\"");
                xmlBuf.append("\" tkh_id=\"").append(tkhId);
                xmlBuf.append("\" score=\"").append("\" grade=\"");
                xmlBuf.append("\" last_acc_datetime=\"");
                xmlBuf.append("\" last_update_timestamp=\"");
            }
            xmlBuf.append("\">").append(dbUtils.NEWL);
            dbRegUser dbUsr = new dbRegUser();
            dbUsr.usr_ent_id = entId;
            dbUsr.get(con);
            xmlBuf.append( (dbUsr.getUserShortXML(con, false, true, false)).toString() );

            // for file list
            dbProgressAttachment pgrAttach= new dbProgressAttachment(usrId, modId, attemptNo, tkhId);
            Vector attLst = pgrAttach.get(con);
            //String commentDirPath = INI_DIR_UPLOAD + dbUtils.SLASH + modId + dbUtils.SLASH
            //                    + usrId + dbUtils.SLASH + COMMENT_DIR + dbUtils.SLASH;
           String commentDirPath = RES_FOLDER + "/" + modId + "/" + usrId + "/" + COMMENT_DIR + "/";
            for (int i=0; i<attLst.size(); i++) {
                dbAttachment attach = (dbAttachment) attLst.elementAt(i);
                xmlBuf.append("<file id=\"").append(attach.att_id).append("\" name=\"").append(attach.att_filename);
                xmlBuf.append("\" parentId=\"").append(attach.att_att_id_parent);
                xmlBuf.append("\" type=\"").append(attach.att_type).append("\">");
                xmlBuf.append(attach.att_desc).append("</file>").append(dbUtils.NEWL);
            }
            xmlBuf.append("<upload_path teacher=\"").append(commentDirPath).append("\">");
            xmlBuf.append("</upload_path>").append(dbUtils.NEWL);
            xmlBuf.append("</submission>").append(dbUtils.NEWL);

            xml = xmlBuf.toString();
            rs.close();
            stmt.close();

            return xml;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        } catch(cwException e) {
            throw new qdbException("CW Error: " + e.getMessage());
        } catch(cwSysMessage e) {
            throw new qdbException("cwSysMessage Error: " + e.getMessage());
        }
    }

    // predefine 
    // mov_ent_id,  mov_mod_id, mov_tkh_id
    public void updateEASStatusWithComment(Connection con, loginProfile prof, dbProgress dbpgr, String filename, String comment, float maxScore, float passScore)
           throws qdbException, cwSysMessage
       {
           try{
           String status = "";
           String grade = dbpgr.pgr_grade;
           float score = dbpgr.pgr_score;
           dbRegUser usr = new dbRegUser();
           usr.usr_id = dbpgr.pgr_usr_id;
           usr.usr_ent_id = usr.getEntId(con);
           usr.get(con);

           String commentDirPath = INI_DIR_UPLOAD + dbUtils.SLASH + dbpgr.pgr_res_id + dbUtils.SLASH
                                 + usr.usr_id + dbUtils.SLASH + COMMENT_DIR;
           
           CommonLog.info("commentDirPath " + commentDirPath);
            
           if (bFileUpload && filename != null && filename.length() != 0) {
        	   CommonLog.info("process upload");
               // Copy all the files to the permanet directory

               dbUtils.moveDir(tmpUploadDir, commentDirPath);

               // Remove un-necessary uploaded files
               File dir = new File(commentDirPath);
               String[] fList = dir.list();
               File fh = null;

               if (fList != null) {
                   for (int i = 0; i < fList.length; i++) {
                       if (! filename.equals(fList[i])) {
                           fh = new File(dir, fList[i]);
                           fh.delete();
                       }
                   }
               }

           }
           
           //get status
            if(maxScore == -1){
                if(grade.equals("F"))
                    status = STATUS_FAILED;
                else
                    status = STATUS_PASSED;
           }else{
                if(score >= (passScore * maxScore / 100))
                    status = STATUS_PASSED;
                else
                    status = STATUS_FAILED;
           }
            CommonLog.info("status " + status);
           //dbpgr.pgr_res_id = ass_res_id;
           dbpgr.pgr_attempt_nbr = 1;
           //dbpgr.pgr_attempt_nbr = attempt_nbr;

           // get the pgr_complete_datetime
           try{
                
                dbpgr.get(con);
                dbpgr.pgr_grade = grade;
                dbpgr.pgr_score = score;
                dbpgr.pgr_status = status;
                dbpgr.pgr_completion_status = status;
                dbpgr.updResult(con);
           }   catch(Exception em){
        	   	CommonLog.error(em.getMessage());
                CommonLog.error("update progress : no progress record ");
                dbpgr.pgr_grade = grade;
                dbpgr.pgr_score = score;
                dbpgr.pgr_status = status;
                dbpgr.pgr_max_score = maxScore;
                dbpgr.pgr_start_datetime = new Timestamp(0);
                dbpgr.ins(con, 0);
                CommonLog.error("update progress : ins progress record ");
           }
         

           dbProgressAttachment pgrAttach = new dbProgressAttachment(dbpgr.pgr_usr_id, dbpgr.pgr_res_id, 1, dbpgr.pgr_tkh_id);
           pgrAttach.delAllTechAtt(con, prof);

           Vector attLst = new Vector();
           dbAttachment attach = new dbAttachment();
            
           attach.att_type = "TEACHER";
           attach.att_filename = filename;
           attach.att_desc = comment;

           attLst.addElement(attach);
           pgrAttach.ins(con, prof, attLst);

           // add by kim
           
           dbModuleEvaluation dbmov = new dbModuleEvaluation();
           dbmov.mov_cos_id = dbModule.getCosId(con, this.mov_mod_id);
           dbmov.mov_ent_id = this.mov_ent_id;
           dbmov.mov_mod_id = this.mov_mod_id;
           dbmov.mov_tkh_id = this.mov_tkh_id;
           dbmov.mov_status = status;
           dbmov.mov_update_timestamp = new Timestamp(System.currentTimeMillis());
           dbmov.mov_update_usr_id = prof.usr_id;
           dbmov.mov_create_timestamp = new Timestamp(System.currentTimeMillis());
           dbmov.mov_create_usr_id = prof.usr_id;
        
           if (dbpgr.pgr_grade == null){
               dbmov.mov_score = "" + dbpgr.pgr_score;
           }
           else{
               if (dbpgr.pgr_grade.equals("A+"))
                   dbmov.mov_score = "" + (-101);
               else if (dbpgr.pgr_grade.equals("A"))
                   dbmov.mov_score = "" + (-102);
               else if (dbpgr.pgr_grade.equals("A-"))
                   dbmov.mov_score = "" + (-103);
               else if (dbpgr.pgr_grade.equals("B+"))
                   dbmov.mov_score = "" + (-104);
               else if (dbpgr.pgr_grade.equals("B"))
                   dbmov.mov_score = "" + (-105);
               else if (dbpgr.pgr_grade.equals("B-"))
                   dbmov.mov_score = "" + (-106);
               else if (dbpgr.pgr_grade.equals("C+"))
                   dbmov.mov_score = "" + (-107);
               else if (dbpgr.pgr_grade.equals("C"))
                   dbmov.mov_score = "" + (-108);
               else if (dbpgr.pgr_grade.equals("C-"))
                   dbmov.mov_score = "" + (-109);
               else if (dbpgr.pgr_grade.equals("D"))
                   dbmov.mov_score = "" + (-110);
               else if (dbpgr.pgr_grade.equals("F"))
                   dbmov.mov_score = "" + (-111);
           }
           dbmov.save(con, prof);
          
         }catch(qdbErrMessage em){
               throw new cwSysMessage(em.getMessage());
         }
    }
    
    // predefine mov_ent_id, mov_mod_id, mov_tkh_id    
   public void updateEASStatus(Connection con, loginProfile prof, dbProgress dbpgr, String filename, String comment, float maxScore, float passScore)
           throws qdbException, cwSysMessage, qdbErrMessage
       {
           try{
           String status = "";
           String grade = dbpgr.pgr_grade;
           float score = dbpgr.pgr_score;
           dbRegUser usr = new dbRegUser();
           usr.usr_id = dbpgr.pgr_usr_id;
           usr.usr_ent_id = usr.getEntId(con);
           usr.get(con);
          
           //get status
            if(maxScore == -1){
                if(grade.equals("F"))
                    status = STATUS_FAILED;
                else
                    status = STATUS_PASSED;
           }else{
                if(score >= (passScore * maxScore / 100))
                    status = STATUS_PASSED;
                else
                    status = STATUS_FAILED;
           }
           //dbpgr.pgr_res_id = ass_res_id;
           dbpgr.pgr_attempt_nbr = 1;
           //dbpgr.pgr_attempt_nbr = attempt_nbr;

           // get the pgr_complete_datetime
           try{
                
                dbpgr.get(con);
                dbpgr.pgr_grade = grade;
                dbpgr.pgr_score = score;
                dbpgr.pgr_status = status;
                dbpgr.updResult(con);
           }   catch(Exception em){
        	   	CommonLog.error("update progress : no progress record in tkh_id: " + dbpgr.pgr_tkh_id);
                dbpgr.pgr_grade = grade;
                dbpgr.pgr_score = score;
                dbpgr.pgr_status = status;
                dbpgr.pgr_max_score = maxScore;
                dbpgr.pgr_start_datetime = new Timestamp(0);
                dbpgr.pgr_completion_status = status;
                dbpgr.ins(con, 0);
                CommonLog.error("update progress : ins progress record ");
           }
         

           // add by kim
           
           dbModuleEvaluation dbmov = new dbModuleEvaluation();
           dbmov.mov_cos_id = dbModule.getCosId(con, this.mov_mod_id);
           dbmov.mov_ent_id = this.mov_ent_id;
           dbmov.mov_mod_id = this.mov_mod_id;
           dbmov.mov_tkh_id = this.mov_tkh_id;
           dbmov.mov_status = status;
           dbmov.mov_update_timestamp = new Timestamp(System.currentTimeMillis());
           dbmov.mov_update_usr_id = prof.usr_id;
           dbmov.mov_create_timestamp = new Timestamp(System.currentTimeMillis());
           dbmov.mov_create_usr_id = prof.usr_id;
        
           if (dbpgr.pgr_grade == null){
               dbmov.mov_score = "" + dbpgr.pgr_score;
           }
           else{
               if (dbpgr.pgr_grade.equals("A+"))
                   dbmov.mov_score = "" + (-101);
               else if (dbpgr.pgr_grade.equals("A"))
                   dbmov.mov_score = "" + (-102);
               else if (dbpgr.pgr_grade.equals("A-"))
                   dbmov.mov_score = "" + (-103);
               else if (dbpgr.pgr_grade.equals("B+"))
                   dbmov.mov_score = "" + (-104);
               else if (dbpgr.pgr_grade.equals("B"))
                   dbmov.mov_score = "" + (-105);
               else if (dbpgr.pgr_grade.equals("B-"))
                   dbmov.mov_score = "" + (-106);
               else if (dbpgr.pgr_grade.equals("C+"))
                   dbmov.mov_score = "" + (-107);
               else if (dbpgr.pgr_grade.equals("C"))
                   dbmov.mov_score = "" + (-108);
               else if (dbpgr.pgr_grade.equals("C-"))
                   dbmov.mov_score = "" + (-109);
               else if (dbpgr.pgr_grade.equals("D"))
                   dbmov.mov_score = "" + (-110);
               else if (dbpgr.pgr_grade.equals("F"))
                   dbmov.mov_score = "" + (-111);
           }
           dbmov.save(con, prof);
           
         }catch(qdbException em){
               throw new qdbException(em.getMessage());
               //qdbErrMessage
         }
    }


   public void delEAS(Connection con, loginProfile prof)
           throws qdbException, qdbErrMessage, cwException, cwSysMessage
       {
           try {
               
               dbRegUser usr = new dbRegUser();
               usr.usr_ent_id  = mov_ent_id;
               usr.getByEntId(con);
               
               //delete progressAttachemnt anbd Attachment
               dbProgressAttachment pgrAttach = new dbProgressAttachment(usr.usr_id, mov_mod_id, 1, mov_tkh_id);
               pgrAttach.delAllTechAtt(con, prof);

               dbProgress dbpgr = new dbProgress();
               dbpgr.pgr_res_id = mov_mod_id;
               dbpgr.pgr_usr_id = usr.usr_id;
               dbpgr.pgr_tkh_id = mov_tkh_id;
               dbpgr.del(con);

               // delete dbModuleEvaluation
               del(con);
               String commentDirPath = INI_DIR_UPLOAD + dbUtils.SLASH + dbpgr.pgr_res_id + dbUtils.SLASH
                                    + usr.usr_id;
              
               CommonLog.info("commentDirPath " + commentDirPath);

              //String saveDirPath = INI_DIR_UPLOAD + dbUtils.SLASH + ass_res_id;
               dbUtils.delDir(commentDirPath);
               // trigger course criteria
//               CourseCriteria.setAttendFromModule(con, prof, mov_mod_id, dbModule.getCosId(con, mov_mod_id), mov_ent_id,mov_tkh_id,""); 
               CourseCriteria.setAttendFromModule( con,  prof, mov_mod_id,  dbModule.getCosId(con, mov_mod_id),  mov_ent_id,  mov_tkh_id,  "",0, cwSQL.getTime(con), null);
           } catch(SQLException e) {
               throw new qdbException("SQL Error: " + e.getMessage());
           } 
       }
    
    public static int getTotalAttemptByTkhId(Connection con, long tkh_id) throws SQLException{
        int attempt;
        String sql = "SELECT SUM(mov_total_attempt) AS total_attempt FROM ModuleEvaluation WHERE mov_tkh_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, tkh_id);
        
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            attempt = rs.getInt("total_attempt");
        }else{
            attempt = 0;
        }
        stmt.close();
        return attempt;
    }
    
	/**@author Christ Qiu
		 Get the total attempt number of all selected module
		 @param con Connection to database
		 @param tkh_id_ary: array of all tkh_id selected
		 @param tkh_ary_length:length of array "tkh_id_ary" 
		 @return number of attempts (hits) of the module
		 */

		public static int getTotalAttempts(Connection con, Vector tkh_id_vec,String tableName)
			throws SQLException {
			int totalattempts = 0;
			if(tkh_id_vec.size()!=0){
			StringBuffer sql = new StringBuffer("");
			sql.append("SELECT SUM(mov_total_attempt) AS total_attempt FROM ModuleEvaluation WHERE mov_tkh_id in(select * FROM ")
			   .append(tableName).append(")");
	/*		for (int i = 0; i < tkh_id_vec.size() - 1; i++) {
				sql.append("?,");
			}
			sql.append("?)");
			PreparedStatement stmt = con.prepareStatement(sql.toString());
			for (int i = 0; i < tkh_id_ary.length; i++) {
				System.out.println(i + " ok");
				stmt.setLong(i + 1, tkh_id_ary[i]);
			}
	*/		
			PreparedStatement stmt = con.prepareStatement(sql.toString());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				totalattempts = rs.getInt("total_attempt");
			}
			stmt.close();

			}
			return totalattempts;
		}
			/**@author Christ Qiu
			 Get the total users with attempts 
			 @param con Connection to database
			 @param tkh_id_ary: array of all tkh_id selected
			 @param tkh_ary_length:length of array "tkh_id_ary" 
			 @return number of users with attempts
			 */
		public static int getAttemptUsers(Connection con, Vector tkh_id_vec,String tableName)
			throws SQLException {
			int attemptusers = 0;
			if(tkh_id_vec.size()!=0){
			StringBuffer sql = new StringBuffer("");
			sql.append("SELECT count(distinct mov_tkh_id) as with_attempts FROM ModuleEvaluation WHERE mov_tkh_id in(select * FROM ")
				.append(tableName).append(")")
				.append("and mov_total_attempt is not null and mov_total_attempt>0");
	/*
			for (int i = 0; i < tkh_id_vec.size() - 1; i++) {
				sql.append("?,");
			}
			sql.append("?");
			sql.append(")");
	*/
			PreparedStatement stmt = con.prepareStatement(sql.toString());
	/*		for (int i = 0; i < tkh_id_vec.size(); i++) {
				//			   System.out.println(i+"ok");
				stmt.setLong(i + 1, tkh_id_vec[i]);
			}
	*/
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				//get learners number whose attempts not zero!
				attemptusers = rs.getInt("with_attempts");
			} else {
				attemptusers = 0;
			}
			stmt.close();
		
			}
			return attemptusers;

		}
    
		/**@author Christ Qiu
			 Get the total users with attempts 
			 @param con Connection to database
			 @param tkh_id_ary: array of all tkh_id selected
			 @param tkh_ary_length:length of array "tkh_id_ary" 
			 @return number of users with attempts
			 */
		public static int getAttemptUsers(Connection con, long[] tkh_id_ary)
			throws SQLException {
			int attemptusers = 0;
			if(tkh_id_ary.length!=0){
			StringBuffer sql = new StringBuffer("");
			sql.append(
				"SELECT count(distinct mov_tkh_id) as with_attempts FROM ModuleEvaluation WHERE mov_tkh_id in(");
			for (int i = 0; i < tkh_id_ary.length - 1; i++) {
				sql.append("?,");
			}
			sql.append("?");
			sql.append(")");
			sql.append(
				"and mov_total_attempt is not null and mov_total_attempt>0");
			PreparedStatement stmt = con.prepareStatement(sql.toString());
			for (int i = 0; i < tkh_id_ary.length; i++) {
				//			   System.out.println(i+"ok");
				stmt.setLong(i + 1, tkh_id_ary[i]);
			}

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				//get learners number whose attempts not zero!
				attemptusers = rs.getInt("with_attempts");
			} else {
				attemptusers = 0;
			}
			stmt.close();
			}
			return attemptusers;

		}

    private static final String SQL_GET_MOD_ATTEMPT_COUNT = " select sum(mov_total_attempt) as cnt "
                                                          + " from ModuleEvaluation "
                                                          + " where mov_mod_id = ? ";
    /**
    Get the total attempt number of the input module
    @param con Connection to database
    @param mod_id resource id of the module
    @return number of attempts (hits) of the module
    */
    public static int getModuleAttemptCount(Connection con, long mod_id) throws SQLException {
        PreparedStatement stmt = null;
        int count = 0;
        try {
            stmt = con.prepareStatement(SQL_GET_MOD_ATTEMPT_COUNT);
            stmt.setLong(1, mod_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                count = rs.getInt("cnt");
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return count;
    }
    
    private void updateNotMark(Connection con, boolean isNotMark) throws SQLException {
    	if (isNotMark) {
    		mov_not_mark_ind = 1;
    	} else {
    		mov_not_mark_ind = 0;
    	}
    	String sql = "update ModuleEvaluation set mov_not_mark_ind = ? where mov_cos_id = ? and mov_ent_id = ? " +
    			"and mov_mod_id = ? and mov_tkh_id = ?";
    	PreparedStatement stmt = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		int index = 1;
    		stmt.setInt(index++, mov_not_mark_ind);
    		stmt.setLong(index++, mov_cos_id);
    		stmt.setLong(index++, mov_ent_id);
    		stmt.setLong(index++, mov_mod_id);
    		stmt.setLong(index++, mov_tkh_id);
    		stmt.executeUpdate();
    	} finally {
    		if (stmt != null) {
    			stmt.close();
    		}
    	}
    }
    
    public static String getItmEvaluationReportList(Connection con, long course_id, String mod_type, long page_size, long page_num,
            String orderBy, String sortOrder) throws SQLException, qdbException, cwSysMessage{
    	
        StringBuffer result = new StringBuffer();
        ResultSet rset = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String res_subtype = null;
        long res_id = 0;
        long total_num = 0;
        Vector validAppTkhIds = null;
        long tst_count = 0;
        long svy_count = 0;
        long ass_count = 0;
        long res_count = 0;
        String res_type = "";
        
        //查询每种类型的个数
        String sql = "select res_subtype,COUNT(res_subtype) res_subtype_count from (SELECT res.res_id ,res.res_subtype FROM Resources res INNER JOIN ResourceContent on (res.res_id = rcn_res_id_content) WHERE res.res_status = 'ON' and rcn_res_id = ? and res_subtype IN ('DXT','TST','ASS','SVY')) s group by res_subtype";
        stmt = con.prepareStatement(sql);
        stmt.setLong(1, course_id);
        rs = stmt.executeQuery();
        
        while(rs.next()){
        	res_count = rs.getLong("res_subtype_count");
        	res_type = rs.getString("res_subtype");
        	//计算当前类型的总条数
        	if(mod_type.equalsIgnoreCase(res_type)){
        		total_num += res_count;
        	}
        	//每种类型的数量
        	if(dbModule.MOD_TYPE_DXT.equalsIgnoreCase(res_type)){
        		//把动态测验的条数加在TST上
        		if(dbModule.MOD_TYPE_TST.equalsIgnoreCase(mod_type)){
        			total_num += res_count;
        		}
        		tst_count += res_count;
        	}
        	if(dbModule.MOD_TYPE_TST.equalsIgnoreCase(res_type)){
        		tst_count += res_count;
        	}
        	if(dbModule.MOD_TYPE_ASS.equalsIgnoreCase(res_type)){
        		ass_count += res_count;
        	}
        	if(dbModule.MOD_TYPE_SVY.equalsIgnoreCase(res_type)){
        		svy_count += res_count;
        	}
        }
        
        //当没有默认类型的没有数据的时候，更换类型
        if(tst_count <= 0 && ass_count > 0 && dbModule.MOD_TYPE_TST.equalsIgnoreCase(mod_type)){
        	mod_type = dbModule.MOD_TYPE_ASS;
        	//总条数
        	total_num = ass_count;
        }else if(tst_count <= 0 && ass_count <= 0 && svy_count > 0 && (dbModule.MOD_TYPE_ASS.equalsIgnoreCase(mod_type) || dbModule.MOD_TYPE_TST.equalsIgnoreCase(mod_type))){
        	mod_type = dbModule.MOD_TYPE_SVY;
        	//总条数
        	total_num = svy_count;
        }
        result.append("<mod_type>").append(mod_type).append("</mod_type>");
        //每种类型的个数（TST类型包括动态（DXT）与静态（TST））
        result.append("<module_count TST=\"").append(tst_count).append("\" SVY=\"").append(svy_count).append("\" ASS=\"").append(ass_count).append("\" ></module_count>").append(dbUtils.NEWL);
        
        result.append("<module_list>").append(dbUtils.NEWL);
        
        //排序用的（暂时不用）
        if(orderBy == null || orderBy.length() == 0) {
            orderBy = "rcn_order";
        }
        if(sortOrder == null || sortOrder.length() == 0) {
            sortOrder = "ASC";
        }
        
        //查询出当前类型的列表
        sql = "SELECT res.res_id , res.res_type , res.res_subtype ,  rcn_sub_nbr , rcn_order , res.res_status , res.res_title FROM Resources res  INNER JOIN ResourceContent   On( res.res_id = rcn_res_id_content) WHERE res.res_status = 'ON' and rcn_res_id = ? ";
        if(mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_TST)){
        	sql += " and res_subtype in ('"+ dbModule.MOD_TYPE_TST +"','"+ dbModule.MOD_TYPE_DXT +"') ";
        }else{
        	sql += " and res_subtype = '"+ mod_type +"' ";
        }
        sql += " order by rcn_order ";
        
        stmt = con.prepareStatement(sql);

        stmt.setLong(1, course_id);
        
        rs = stmt.executeQuery();
        
        if(mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_ASS)){
        	sql = "SELECT ";
			if (cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				sql += "IFNULL(pgr_res_id,?) pgr_res_id,";
			}else if (cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())) {
				sql += "NVL(pgr_res_id,?) pgr_res_id,";
			}else {
				sql += "isnull(pgr_res_id,?) pgr_res_id,";
			}
        	sql += "pgr_status,tkh_id,tkh_usr_ent_id FROM TrackingHistory LEFT JOIN Progress ON (tkh_id = pgr_tkh_id AND pgr_res_id = ? AND pgr_attempt_nbr = 1) WHERE tkh_cos_res_id = ? AND tkh_type = ?";
        }else if(mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)){
        	sql = "SELECT ";
	        	if (cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())) {
	        		sql +=  " IFNULL(pgr_res_id,?) pgr_res_id, ";
				}else if (cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())) {
					sql +=  " NVL(pgr_res_id,?) pgr_res_id, ";
				}else {
					sql +=  " isnull(pgr_res_id,?) pgr_res_id, ";
				}
        			
        	sql +=  " tkh_id, pgr_status,tkh_usr_ent_id FROM ResourceContent INNER JOIN TrackingHistory ON (rcn_res_id = tkh_cos_res_id) "+
        			" LEFT JOIN ModuleEvaluation ON (tkh_id = mov_tkh_id and mov_mod_id = rcn_res_id_content)  LEFT JOIN Progress ON (tkh_id = pgr_tkh_id and pgr_res_id = rcn_res_id_content) WHERE rcn_res_id_content = ? ";
        }else{
        	sql = "select ";
        	if (cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())) {
        		sql += "IFNULL(pgr_res_id,?) pgr_res_id,";
			}else if (cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())) {
				sql += "NVL(pgr_res_id,?) pgr_res_id,";
			}else {
				sql += "isnull(pgr_res_id,?) pgr_res_id,";
			}	
        	sql +=  "pgr_status,tkh_usr_ent_id from  TrackingHistory inner join Enrolment  on (tkh_usr_ent_id = enr_ent_id )   " +
        			" inner join aeApplication  on (tkh_id = app_tkh_id) left join Progress on (tkh_id = pgr_tkh_id and pgr_res_id = ?) where tkh_cos_res_id = enr_res_id  and tkh_cos_res_id = ?";
        }
        
    	//page control
        if (page_size == 0) {
            page_size = 10;
        }
        if (page_num == 0) {
            page_num = 1;
        }
        long i = 0;
        if (page_num > 1 && page_size * (page_num - 1) >= total_num) {
            page_num = total_num / page_size;

            if (total_num % page_size > 0) {
                page_num++;
            }
        }
        
        long cur_page_end_num = page_num * page_size;
        long cur_page_start_num = (page_num - 1) * page_size;
        
        while(rs.next()){
        	i++;
        	if (i <=cur_page_end_num && i >= cur_page_start_num + 1) {
	        	res_id = rs.getLong("res_id");
	        	stmt = con.prepareStatement(sql);
	        	stmt.setLong(1, res_id);
	        	stmt.setLong(2, res_id);
	        	if(!mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)){
	        		stmt.setLong(3, course_id);
	        	}
	        	if(mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_ASS)){
	        		stmt.setString(4, DbTrackingHistory.TKH_TYPE_APPLICATION);
	        	}
	        	if(mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_ASS) || mod_type.equals(dbModule.MOD_TYPE_SVY)){
	        		validAppTkhIds = ViewModuleEvaluation.getValidAppTkhIds(con, res_id);
	        	}
	        	rset = stmt.executeQuery();

	        	//测验、作业、评估问卷主页面统计
	        	int pgr_status_not_graded = 0;//未評分
	            int pgr_status_graded = 0;//已評分
	            int pgr_status_ok = 0;//已評分
	            int pgr_status_not_submit = 0;//尚未提交
	            while(rset.next()){
	            	String status = rset.getString("pgr_status");

	            	Long ent_id = rset.getLong("tkh_usr_ent_id");
	            	String userStatusSql = "select usr_status from RegUser where usr_ent_id = ?";
                    PreparedStatement stmt_user = con.prepareStatement(userStatusSql);
	            	stmt_user.setLong(1,ent_id);
                    ResultSet rset_user = stmt_user.executeQuery();
                    String usr_status = null;
                    while (rset_user.next()){
                        usr_status = rset_user.getString("usr_status");
                    }
                    rset_user.close();
                    stmt_user.close();
                    //过滤掉回收站用户
                    if(usr_status.equals("OK")){
                        if(mod_type.equals(dbModule.MOD_TYPE_ASS) || mod_type.equals(dbModule.MOD_TYPE_SVY)){
                            long tkhId = rset.getLong("tkh_id");
                            if(validAppTkhIds.contains(new Long(tkhId))) {
                                if(dbProgress.PGR_STATUS_NOT_GRADED.equalsIgnoreCase(status)){
                                    pgr_status_not_graded++;
                                }else if(dbProgress.PGR_STATUS_GRADED.equalsIgnoreCase(status)){
                                    pgr_status_graded++;
                                }else if(dbProgress.PGR_STATUS_OK.equalsIgnoreCase(status)){
                                    pgr_status_ok++;
                                }else if(status == null){
                                    pgr_status_not_submit++;
                                }
                            }
                        }else{
                            if(dbProgress.PGR_STATUS_NOT_GRADED.equalsIgnoreCase(status)){
                                pgr_status_not_graded++;
                            }else if(dbProgress.PGR_STATUS_GRADED.equalsIgnoreCase(status)){
                                pgr_status_graded++;
                            }else if(dbProgress.PGR_STATUS_OK.equalsIgnoreCase(status)){
                                pgr_status_ok++;
                            }else if(status == null){
                                pgr_status_not_submit++;
                            }
                        }
                    }

	        	}
	        	result.append("<module id=\"").append(course_id).append("\" mod_res_id=\"").append(res_id)
	        	.append("\" res_title=\"").append( com.cwn.wizbank.utils.StringUtils.replaceUtils(rs.getString("res_title")))
	        	.append("\" res_status=\"").append(rs.getString("res_status"))
	        	.append("\" pgr_status_not_graded=\"").append(pgr_status_not_graded)
	        	.append("\" pgr_status_graded=\"").append(pgr_status_graded)
	        	.append("\" pgr_status_not_submit=\"").append(pgr_status_not_submit)
	        	.append("\" pgr_status_ok=\"").append(pgr_status_ok)
	        	.append("\" pgr_status_submit=\"").append(pgr_status_ok + pgr_status_not_graded + pgr_status_graded)
	        	.append("\" pgr_status_all=\"").append(pgr_status_ok + pgr_status_not_graded + pgr_status_not_submit + pgr_status_graded)
	        	.append("\">");
	        	result.append("</module>").append(dbUtils.NEWL);
        	}
        }
        result.append("</module_list>").append(dbUtils.NEWL);
        if(stmt != null){
        	stmt.close();
        }
        if(rset != null){
        	rset.close();
        }
        if(rs != null){
        	rs.close();
        }
        result.append("<pagination total_rec=\"").append(total_num).append("\"");
        result.append(" page_size=\"").append(page_size).append("\"");
        result.append(" cur_page=\"").append(page_num).append("\"");
        result.append(" sort_col=\"").append(orderBy).append("\"");
        result.append(" sort_order=\"").append(sortOrder).append("\"/>");
        
        return result.toString();
    }

}