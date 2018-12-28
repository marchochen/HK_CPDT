package com.cw.wizbank.ae.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.report.EvnSurveyQueReportXls;
import com.cw.wizbank.report.SurveyQueData;

public class ViewEvnSurveyQueReport {

	private Connection con = null;
	
	public class HeadData{
		public String mod_title;
	}
	
	public ViewEvnSurveyQueReport(Connection con) {
		this.con = con;
	}
	
	private static String getTargetUserCountByModSql() {
		String sql = " select count(usr_ent_id) as target_usr_count from regUser" +
				" where usr_ent_id in (" +
				" 	select ent_id from entity" +
				" 	inner join evalAccess on (eac_target_ent_id = ent_id and eac_res_id = ?)" +
				" 	where ent_type = ? and ent_delete_timestamp is null" +
				" 	union " +
				" 	select usr_ent.ent_id from entity usg_ent" +
				" 	inner join evalAccess on (eac_target_ent_id = usg_ent.ent_id and eac_res_id = ?)" +
				" 	inner join entityRelation on (ern_ancestor_ent_id = usg_ent.ent_id and ern_type = ?)" +
				" 	inner join entity usr_ent on (ern_child_ent_id = usr_ent.ent_id)" +
				" 	where usg_ent.ent_type = ? and usg_ent.ent_delete_timestamp is null" +
				"	and usr_ent.ent_type = ? and usr_ent.ent_delete_timestamp is null" +
				" )";
		return sql;
	}
	
	public long getTargetUserCountByMod(long modId) throws SQLException {
		long targetUserCount = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			stmt = con.prepareStatement(getTargetUserCountByModSql());
			int index = 1;
			stmt.setLong(index++, modId);
			stmt.setString(index++, dbEntity.ENT_TYPE_USER);
			stmt.setLong(index++, modId);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setString(index++, dbEntity.ENT_TYPE_USER_GROUP);
			stmt.setString(index++, dbEntity.ENT_TYPE_USER);
			rs = stmt.executeQuery();
			if(rs.next()) {
				targetUserCount = rs.getLong("target_usr_count");
			}
			
		} finally {
			if(stmt != null) {
				stmt.close();
			}
		}
		
		return targetUserCount;
	}
	
	public long getReponseCount(long modId) throws SQLException {
		long reponseCount = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try{
			stmt = con.prepareStatement(SqlStatements.getModProgressStatSQL(new StringBuffer(" and usr_status <> ?")));
			int index = 1;
			stmt.setString(index++, "OK");
			stmt.setLong(index++, modId);
			stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
			rs = stmt.executeQuery();
			if(rs.next()) {
				reponseCount = rs.getLong("TOTAL_CNT");
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
		}
		
		return reponseCount;
		
	}
	
	private String getDetailSql(String conditionSql) {
		String sql = " select atm_order, atm_response_bil, count(*) response_count" +
				" from ProgressAttempt" +
				" inner join Progress on (atm_pgr_usr_id = pgr_usr_id and atm_pgr_res_id = pgr_res_id and atm_pgr_attempt_nbr = pgr_attempt_nbr and atm_tkh_id = pgr_tkh_id and pgr_status = ?)" +
				" inner join Module on (mod_res_id = atm_pgr_res_id and mod_res_id = ?)" +
				" inner join RegUser  on (atm_pgr_usr_id=usr_id and usr_status <> ?)"+
				" where atm_order in (" +
				" 	select rcn_order" +
				" 	from ResourceContent" +
				" 	inner join Resources on (res_id = rcn_res_id and res_id = ?)" +
				" 	inner join Question on (que_res_id = rcn_res_id_content)" +
				" 	inner join Interaction on (int_res_id = que_res_id))";
		if(conditionSql != null) {
			sql += conditionSql;
		}
		sql += " group by atm_order, atm_response_bil order by atm_order";
		return sql;
	}
	
	public void getDetail(ArrayList tempList,long modId, long responseCount, boolean isShowAllFBAns) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(getDetailSql(null));
			int index = 1;
			stmt.setString(index++, "OK");
			stmt.setLong(index++, modId);
			stmt.setString(index++,dbRegUser.USR_STATUS_DELETED);
			stmt.setLong(index++, modId);
			rs = stmt.executeQuery();
			
			int preOrder = 0;
			SurveyQueData gx = null;
			while(rs.next()) {
				int atm_order = rs.getInt("atm_order");
	            String atm_response_bil = rs.getString("atm_response_bil");
	            int response_count = rs.getInt("response_count");

	            gx = (SurveyQueData)tempList.get(atm_order - 1);
	            
	            gx.countResponse(atm_response_bil, response_count, false, isShowAllFBAns);

	            if (atm_order != preOrder && preOrder != 0) {
	                gx = (SurveyQueData)tempList.get(preOrder - 1);
	                gx.calculateStat(responseCount);
	            }
	            preOrder = atm_order;
			}
			if (preOrder!=0){
	            gx = (SurveyQueData)tempList.get(preOrder - 1);
	            gx.calculateStat(responseCount);
	        }  
		} finally {
			if(stmt != null) {
				stmt.close();
			}
		}
	}
	
	public HeadData getRptHead(long modId) throws SQLException {
		HeadData headDate = new HeadData();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// get mod_title starts here
		StringBuffer getModTitle = new StringBuffer();
		getModTitle.append("select res_title from Resources where res_id = ").append(modId); 
		stmt = con.prepareStatement(getModTitle.toString());
		rs = stmt.executeQuery();
		while (rs.next()) {
			headDate.mod_title = rs.getString("res_title");
		}
		stmt.close();

		return headDate;
	}
	
	public int getXslFBAns(long modId, long queId, EvnSurveyQueReportXls evnSurveyQueRptXls) 
		throws SQLException, IOException, RowsExceededException, WriteException {
		//the count of answer
		int ansCount = 0;

		StringBuffer conditionSql = new StringBuffer(" and atm_order = ?");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(getDetailSql(conditionSql.toString()));
			int index = 1;
			stmt.setString(index++, "OK");
			stmt.setLong(index++, modId);
			stmt.setString(index++, dbRegUser.USR_STATUS_DELETED);
			stmt.setLong(index++, modId);
			stmt.setLong(index++, queId);

			rs = stmt.executeQuery();
			int rowNum = 1;
			while (rs.next()) {
				if (rs.getString("atm_response_bil") != null) {
					int response_count = rs.getInt("response_count");
					String response_bil = rs.getString("atm_response_bil");
					for (int i = 0; i < response_count; i++) {
						if (rowNum == 1 && i == 0) {
							evnSurveyQueRptXls.setFBAns(response_bil, true);
						} else {
							evnSurveyQueRptXls.setFBAns(response_bil, false);
						}
					}
					ansCount += response_count;
					rowNum++;
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return ansCount;
	}
}
