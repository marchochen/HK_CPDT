/**
 * @author Christ Qiu
 * Core Business Logic of Instructor Management
 * to operate on Instructors
 */
package com.cw.wizbank.course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbCourseMeasurement;
import com.cw.wizbank.db.DbCourseModuleCriteria;
import com.cw.wizbank.db.DbMeasurementEvaluation;
import com.cw.wizbank.db.DbRegUser;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;


public class EvalManagement {
	/**
	 * @author Christ
	 * Param:con is db connection 
	 * to get mark of learner 
	 * scoring item information
	 * and return as XML
	 */
    
    private long ccr_id;
    
	protected String scoringItmDetail2XML(
		Connection con,
		EvalManagementReqParam urlp)
		throws SQLException, cwException, qdbException, cwSysMessage {
		StringBuffer result = new StringBuffer();
		StringBuffer tempBuf = new StringBuffer();
		if (urlp.cwPage.sortCol == null || urlp.cwPage.sortCol.length() == 0) {
			urlp.cwPage.sortCol = "usr_display_bil";
		}
		if (urlp.cwPage.sortOrder == null
			|| urlp.cwPage.sortOrder.length() == 0) {
			urlp.cwPage.sortOrder = "desc";
		}
		if (urlp.cwPage.curPage == 0) {
			urlp.cwPage.curPage = 1;
		}
        if (urlp.cwPage.pageSize == 0) {
            urlp.cwPage.pageSize = 10;
        }
		PreparedStatement pstmt = null;
		String course_type = DbCourseMeasurement.getCourseTypeByCmtId(con,urlp.cmt_id);
        long itm_id = DbCourseCriteria.getCcrItmIdByCmtId(con,urlp.cmt_id);
		if (course_type.equals("online")) {
			pstmt = con.prepareStatement(OuterJoinSqlStatements.getOnlineEvalItem(itm_id));
			//pstmt.setString();
		} else {
			pstmt = con.prepareStatement(OuterJoinSqlStatements.getOfflineEvalItem(itm_id));
			//pstmt.setString      
		}
		pstmt.setLong(1, urlp.cmt_id);
		ResultSet rs = pstmt.executeQuery();
        
		int total_record = 0;
        //pagination
		int start = urlp.cwPage.pageSize * (urlp.cwPage.curPage - 1);
        
		while (rs.next()) {
			if (total_record >= start && total_record < start + urlp.cwPage.pageSize) {
				tempBuf
					.append("<eval_item app_id=\"")
                    .append(rs.getLong(1))
                    .append("\" ")                    .append("lrn_name=\"")
					.append(cwUtils.esc4XML(rs.getString(2)))
					.append("\" ")
					.append("lrn_ent_id=\"")
					.append(rs.getLong(3))
					.append("\" ")
                    .append("cmt_score=\"");
              float cmt_score = rs.getFloat(4);
                    if(!rs.wasNull()){
                        tempBuf.append(cmt_score);
                    }
                    
                tempBuf.append("\" ")
                       .append("cmt_tkh_id=\"")
                       .append(cwUtils.escZero(rs.getLong(5)))
                       .append("\" ")
                       .append("cov_score=\"");
                float cov_score = rs.getFloat("cov_score");
                if(!rs.wasNull()) {
                    tempBuf.append(cov_score);
                }
                tempBuf.append("\"/>");
    	}
        if(!urlp.cmd.equalsIgnoreCase("export_mark")){//for export mark:don't control the record number
    		total_record++;
        }
		}
		pstmt.close();
		urlp.cwPage.setTotalRec(total_record);
		result
			.append("<scoring_item itm_id=\"")
			.append(itm_id)
			.append("\" ")
			.append("run_ind=\"")
			.append(aeItem.getRunInd(con,itm_id))
			.append("\" ")
			.append("session_ind=\"")
			.append(aeItem.getSessionInd(con,itm_id))
			.append("\" ")
			.append("itm_title=\"");
		try {
			result.append(cwUtils.esc4XML(aeItem.getItemTitle(con, itm_id)));
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (cwSysMessage e) {
			CommonLog.error(e.getMessage(),e);
		}
        float cmt_max_score = 0;
        DbCourseMeasurement dbcmt = new DbCourseMeasurement();
        DbCourseModuleCriteria dbcmr = new DbCourseModuleCriteria();
        dbModule dbm = new dbModule();
        dbcmt = dbcmt.getByCmtId(con,urlp.cmt_id);
        if(dbcmt.getCmt_cmr_id() != 0){
           
            dbcmr.getByCmrId(con, dbcmt.getCmt_cmr_id());
            dbcmt.setCmt_contri_rate(dbcmr.cmr_contri_rate);
            dbm.mod_res_id = dbcmr.cmr_res_id;
            dbm.get(con);
            cmt_max_score = dbm.mod_max_score;
        }else{
            cmt_max_score = DbCourseMeasurement.getCmtMaxScore(con,urlp.cmt_id);
        }

        result

			.append("\" ")
			.append("cmt_id=\"")
			.append(urlp.cmt_id)
            .append("\" ")
            .append("cmt_max_score=\"")
            .append(cmt_max_score)
            .append("\" ")
			.append("type=\"")
			.append(course_type)
		//.append(course_type)
		.append("\" ")
		.append("cmt_title=\"")
		.append(cwUtils.esc4XML(DbCourseMeasurement.getCmtTitleByCmtId(con, urlp.cmt_id)))
		.append("\">");
		result.append(aeItem.getNavAsXML(con,itm_id));
		result.append(urlp.cwPage.asXML()).append(tempBuf.toString()).append("</scoring_item>");
		return result.toString();
	}
	/**
	 * @param import_Cols
	 * @return
	 */
	public String genPreviewXml(Vector import_Cols) {
		// TODO Auto-generated method stub
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<eval_item_lst>");
        String[] colName = (String[])import_Cols.elementAt(0);
        String[] colValue = null;
        for(int i=0;i<colName.length;i++){
            xmlBuf.append("<col_name name=\"").append(dbUtils.esc4XML(colName[i])).append("\"/>");
        }
        for(int i=1;i<import_Cols.size();i++){
            if(import_Cols.elementAt(i)!=null){
                colValue = (String[])import_Cols.elementAt(i); 
                xmlBuf.append("<eval_item>");
                for(int j=0;j<colValue.length;j++){
                  xmlBuf.append("<col_value value=\"").append(dbUtils.esc4XML(colValue[j])).append("\"/>");
                }
                xmlBuf.append("</eval_item>");
            }
        }
        xmlBuf.append("</eval_item_lst>");
		return xmlBuf.toString();
	}

	/**
	 * @param import_Cols
	 */
	public void validateRecords(Connection con,Vector import_Cols) throws SQLException, cwSysMessage {
        Vector errorMessage = new Vector();
        String[] colName = (String[])import_Cols.elementAt(0);
        String[] colValue =	null;
        boolean hasCcrIdAlready = false;
        float[] cmt_max_score = new float[colName.length];
        String[] invalidColumn;
//        HashMap invalidRecords = new HashMap();//to store error message of each record in the form of(row,cloumn)
        long app_id = 0;
	    for(int i=2;i<colName.length;i++){
		  String itm_title = colName[i];
		  if(itm_title!=null && !itm_title.equals("")){
			for(int j=1;j<import_Cols.size();j++){//get ccr_id for getCmtIdByCmtTitle to get cmt_id
				if (hasCcrIdAlready){
					break;
				}
				if (import_Cols.elementAt(j)!= null) {
					colValue = (String[]) import_Cols.elementAt(j);
					  if(colValue[0]!=null){
						  app_id = Long.valueOf(colValue[0]).longValue();
						  ccr_id = DbCourseCriteria.getCcrIdByAppId(con,app_id);
						  hasCcrIdAlready = true;
					  }
				  }
			}
			  long cur_cmt_id = DbCourseMeasurement.getCmtIdByCmtTitle(con,itm_title,ccr_id);
			  if(!(cur_cmt_id>0)){
				  throw new cwSysMessage("CND005",errorMessage);
			  }
			  String type = DbCourseMeasurement.getCourseTypeByCmtId(con,cur_cmt_id);
			  if(!type.equals("offline")){
				  errorMessage.removeAllElements();
				  errorMessage.add(itm_title);
				  errorMessage.add(String.valueOf(i+1));
				  throw new cwSysMessage("CND006",errorMessage); 
			  }
			  cmt_max_score[i] = DbCourseMeasurement.getCmtMaxScore(con,cur_cmt_id);
		  }
	  }
	  
	  for(int i=1;i<import_Cols.size();i++){
		  if (import_Cols.elementAt(i) != null) {
			  colValue = (String[]) import_Cols.elementAt(i);
			  //app_id = Long.parseLong(colValue[0]);
			  if(colValue[0]!=null && !colValue[0].equals("")){
				  app_id = Long.parseLong(colValue[0]);
			  } else{
				  throw new cwSysMessage("CND007" ,String.valueOf(i+1));
			  }
			  if (!aeApplication.isExistAppId(con, app_id)) {
				  throw new cwSysMessage("CND007" ,String.valueOf(i+1));
			  }
			  if (!aeApplication.isExistTkhId(con, app_id)) {
				  throw new cwSysMessage("CND008" ,String.valueOf(i+1));
			  }
			  if ((DbRegUser.getUserNameByAppId(con, app_id) == null)) {
				  throw new cwSysMessage("CND009" ,String.valueOf(i+1));
			  }
			  for(int j=2;j<colValue.length;j++){
				  if(colValue[j]!=null && !colValue[j].equals("--") 
					  && !colValue[j].equals("-") && !colValue[j].equals("")){
					  try {
						  Float.parseFloat(colValue[j]);
					  } catch (NumberFormatException e) {
						  throw new cwSysMessage("1076", errorMessage);
					  }
					  if(Float.parseFloat(colValue[j]) > cmt_max_score[j]){
						  errorMessage.removeAllElements();
						  errorMessage.add(String.valueOf(cmt_max_score[j]));
						  errorMessage.add(String.valueOf(i+1));
						  errorMessage.add(String.valueOf(j+1));
						  throw new cwSysMessage("CND010" ,errorMessage);
					  }
					  if(Float.parseFloat(colValue[j]) < 0){
						  errorMessage.removeAllElements();
						  errorMessage.add(String.valueOf(0));
						  errorMessage.add(String.valueOf(i+1));
						  errorMessage.add(String.valueOf(j+1));
						  throw new cwSysMessage("CND011" ,errorMessage);
					  }
				  }
			  }
		  }
	}
	  
	}

    public void importRecords(Connection con, Vector import_Cols, loginProfile prof) throws SQLException, qdbException, cwSysMessage, cwException, qdbErrMessage {
        importRecords(con, import_Cols, prof.usr_id);
        CourseCriteria Ccr = new CourseCriteria();
        Ccr.setFromMarkingSchema(con, prof, ccr_id, true, false, false, false);
    }
	/**
	 * @param con
	 * @param import_Cols
	 */
	public void importRecords(Connection con, Vector import_Cols,String usr_id) throws SQLException, qdbException {
		// TODO Auto-generated method stub
        String[] colName = (String[])import_Cols.elementAt(0);
        String[] colValue = null;
        DbMeasurementEvaluation MeasurementEval = new DbMeasurementEvaluation();
        boolean has_score = true;//indicate if the learner has score
        boolean hasCcrIdAlready = false;
        long app_id = 0;
//		long ccr_id = 0;        
        long mtv_cos_id = 0;
        long mtv_ent_id = 0;
        long mtv_cmt_id = 0;
        long mtv_tkh_id = 0;
        Timestamp mtv_create_timestamp = dbUtils.getTime(con);
        Timestamp mtv_update_timestamp = mtv_create_timestamp;
        float mtv_score = 0;
		int size=import_Cols.size();
        for(int i=1;i<import_Cols.size();i++){
            colValue = (String[])import_Cols.elementAt(i);
            if(colValue[0]!=null && !colValue[0].equals("")){
				app_id = Long.parseLong(colValue[0]);
				if(!hasCcrIdAlready){
					ccr_id = DbCourseCriteria.getCcrIdByAppId(con,app_id);
				}
            }
            mtv_ent_id = DbRegUser.getUsrEntIdByAppId(con,app_id);
            MeasurementEval.setMtv_ent_id(mtv_ent_id);
            mtv_tkh_id = aeApplication.getTkhId(con,app_id);//get the tkh_id from aeApplication

//          mtv_tkh_id = DbMeasurementEvaluation.getMtvTkhIdByAppId(con,app_id);
            MeasurementEval.setMtv_tkh_id(mtv_tkh_id);
            for(int j=2;j<colValue.length;j++){
                mtv_cmt_id = DbCourseMeasurement.getCmtIdByCmtTitle(con,colName[j],ccr_id);
                MeasurementEval.setMtv_cmt_id(mtv_cmt_id);
                String cur_value = colValue[j];
                if(!cur_value.equals("--")&&!cur_value.equals("-")&&!cur_value.equals("")){
                    mtv_score = Float.parseFloat(cur_value);
                    MeasurementEval.setMtv_score(mtv_score); 
                    has_score = true;
                }else{
                    has_score = false;
                }
                boolean exist_mtv = DbMeasurementEvaluation.exist(con,mtv_ent_id,mtv_cmt_id,mtv_tkh_id);
                if(exist_mtv){//find corresponding record in MTV:update the record
                    MeasurementEval.setMtv_update_usr_id(usr_id);
                    MeasurementEval.setMtv_update_timestamp(mtv_update_timestamp);
                    MeasurementEval.updScoreByTkhId(con,has_score);
                    MeasurementEval.updStatus(con,has_score);
                }else{//no corresponding record in MTV:insert a record
                 	mtv_cos_id = dbCourse.getCosResId(con,DbCourseCriteria.getCcrItmIdByCmtId(con,mtv_cmt_id));
//                  mtv_cos_id = DbCourseModuleCriteria.getCmrResIdByCmtId(con,mtv_cmt_id);
                    MeasurementEval.setMtv_cos_id(mtv_cos_id);                    
                    MeasurementEval.setMtv_score(mtv_score);
                    MeasurementEval.setMtv_create_timestamp(mtv_create_timestamp);
                    MeasurementEval.setMtv_update_timestamp(mtv_update_timestamp);
                    MeasurementEval.setMtv_update_usr_id(usr_id);
                    MeasurementEval.setMtv_create_usr_id(usr_id);
                    MeasurementEval.setMtv_tkh_id(mtv_tkh_id);
                    MeasurementEval.ins(con,has_score);
                    MeasurementEval.updStatus(con,has_score);
                }
            }
		//ccr_id = DbCourseCriteria.getCcrIdByCmtId(con,mtv_cmt_id);
	   }
    }

	public static final String sql_get_status_from_mtv ="select mtv_status from MeasurementEvaluation,aeApplication where app_id=? and mtv_ent_id =app_ent_id and mtv_tkh_id=app_tkh_id and mtv_cmt_id =?";
	public static final String sql_get_status_from_mov ="select mov_status from ModuleEvaluation,aeApplication where app_id=? and mov_ent_id=app_ent_id and mov_tkh_id =app_tkh_id and mov_mod_id=?";
	
	public static String getStatusFromMtvMov(Connection con,long app_id,long cmt_id,long cmr_id,long res_id)throws SQLException{
		String strStatus = new String();
		if (cmr_id == -1){
			PreparedStatement stmt = con.prepareStatement(sql_get_status_from_mtv);	
			stmt.setLong(1,app_id);
			stmt.setLong(2,cmt_id);
			ResultSet rs=stmt.executeQuery();
			if(rs.next()){
			strStatus=rs.getString("mtv_status");
			}
			rs.close();
			stmt.close();
		}else{
			PreparedStatement stmt = con.prepareStatement(sql_get_status_from_mov);	
			stmt.setLong(1,app_id);
			stmt.setLong(2,res_id);
			ResultSet rs=stmt.executeQuery();
			if(rs.next()){
				strStatus=rs.getString("mov_status");
			}
			rs.close();
			stmt.close();
		}
		return strStatus;
	}
	public static boolean bFulfillCriteria(Connection con,long app_id,long cmt_id,long cmr_id,long res_id,String status)throws SQLException{
	
		String learnerStatus =getStatusFromMtvMov(con,app_id,cmt_id,cmr_id,res_id);
		if(status==null||status.length()==0){
			return true;
		}
		else if(learnerStatus==null||learnerStatus.length()==0){
			return false;
		}
		else if(status.indexOf(learnerStatus)==-1) return false;
		else
		 return true;
	}
	public static String fulfillOtherCriteriaAsXML(Connection con,long app_id,long itm_id) throws SQLException, cwSysMessage{
		CourseMeasurement cm = new CourseMeasurement();
		Vector onlineModuleVt=cm.getOnlineModuleAsVt(con,itm_id);
		Vector scoringItemVt=cm.getScoringItemAsVt(con,itm_id);
		StringBuffer result = new StringBuffer("");
		result.append("<fulfill_other_criteria>");
		result.append("<online_module>");
		for(int i=0;i<onlineModuleVt.size();i++){
			CourseMeasurement.CmtCmr online =(CourseMeasurement.CmtCmr)onlineModuleVt.get(i);
			boolean bFulfill = bFulfillCriteria(con,app_id,online.cmt_id,online.cmr_id,online.res_id,online.status);
			result.append("<item ccr_id=\"").append(online.ccr_id);
			result.append("\" cmt_id=\"").append(online.cmt_id);
			result.append("\" cmr_id=\"").append(online.cmr_id);
			result.append("\" res_id=\"").append(online.res_id);
			result.append("\" desc_option=\"").append(online.status_desc_option);
			result.append("\" status=\"").append(online.status);
			result.append("\" subtype=\"").append(online.subtype);
			result.append("\">");
			result.append("<title>").append(cwUtils.esc4XML(online.title)).append("</title>");
			result.append("<fulfill_the_criteria>").append(bFulfill);
			result.append("</fulfill_the_criteria>");
			result.append("</item>");
		}
		result.append("</online_module>");
		result.append("<scoring_item>");
		for(int i=0;i<scoringItemVt.size();i++){
			CourseMeasurement.CmtCmr scoringItem =(CourseMeasurement.CmtCmr)scoringItemVt.get(i);
			boolean bFulfill = bFulfillCriteria(con,app_id,scoringItem.cmt_id,scoringItem.cmr_id,scoringItem.res_id,scoringItem.status);
			result.append("<item ccr_id=\"").append(scoringItem.ccr_id);
			result.append("\" cmt_id=\"").append(scoringItem.cmt_id);
			result.append("\" cmr_id=\"").append(scoringItem.cmr_id);
			result.append("\" res_id=\"").append(scoringItem.res_id);
			result.append("\" desc_option=\"").append(scoringItem.status_desc_option);
			result.append("\">");
			result.append("<title>").append(cwUtils.esc4XML(scoringItem.title)).append("</title>");
			result.append("<fulfill_the_criteria>").append(bFulfill);
			result.append("</fulfill_the_criteria>");
			result.append("</item>");
		}
		result.append("</scoring_item>");	
		result.append("</fulfill_other_criteria>");
		return result.toString();
	}
}