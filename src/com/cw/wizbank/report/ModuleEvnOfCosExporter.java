package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import jxl.write.WriteException;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

public class ModuleEvnOfCosExporter extends ReportExporter {
	
	public class ResultData {
		public String usr_display_bil;
		public long usg_ent_id;
		public String ugr_display_bil;
		public String mod_title;
		public String mod_type;
		public float mod_max_score;
		public Timestamp mov_last_acc_datetime;
		public float mov_total_time;
		public String mov_status;
		public float mov_score;
		public String pgr_grade;
	}
	
	public ModuleEvnOfCosExporter(Connection incon, ExportController inController) {
		super(incon, inController);
	}

	public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException,
			WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
		Vector modEvnVec = getModEvnOfCosList(con, prof, specData);

		controller.setTotalRow(modEvnVec.size());
		
		// set exporter
		ModuleEvnOfCosRptExporter rptBuilder = new ModuleEvnOfCosRptExporter(specData.tempDir, specData.relativeTempDirName, 
				specData.window_name, specData.encoding, specData.process_unit,wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getUrl());
		
		rptBuilder.writeTableHead(specData.cur_lang, um);
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		
		for (int modIndex = 0; !controller.isCancelled() && modIndex < modEvnVec.size(); modIndex++) {
			ModuleEvnOfCosExporter.ResultData rsData = (ResultData) modEvnVec.elementAt(modIndex);
			rptBuilder.writeData(con, rsData, entityfullpath, specData.cur_lang);
			controller.next();
		}
		
		if (!controller.isCancelled()) {
            controller.setFile(rptBuilder.finalizeFile());
        }
	}
	
	private String getRptSQL(SpecData specData) {
		String sql;
		if(specData.all_enrolled_lrn_ind) {
			sql = getAllModEvnByConditionSQL(true);
		} else if(specData.all_mod_ind) {
			sql = getAllModEvnByConditionSQL(false);
		} else {
			sql = _GET_MOD_EVN_OF_COS_BY_PERIOD;
			if(specData.start_datetime != null) {
	        	sql += " and mov_last_acc_datetime > ? ";
	        }
	        if(specData.end_datetime != null) {
	        	sql += " and mov_last_acc_datetime < ? ";
	        }
		}
        
        sql += " order by usr_display_bil, res_title, mov_last_acc_datetime, mov_create_timestamp";
		return sql;
	}
	
	private Vector getModEvnOfCosList(Connection con, loginProfile prof, SpecData specData) throws SQLException {
		Vector modEvnVec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.prepareStatement(getRptSQL(specData));
			setStmtParameters(specData, stmt);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				ResultData rsData = new ResultData();
				rsData.usr_display_bil = rs.getString("usr_display_bil");
				rsData.usg_ent_id = rs.getLong("usg_ent_id");
				rsData.ugr_display_bil = rs.getString("ugr_display_bil");
				rsData.mod_title = rs.getString("res_title");
				rsData.mod_type = rs.getString("mod_type");
				rsData.mod_max_score = rs.getFloat("mod_max_score");
				rsData.mov_last_acc_datetime = rs.getTimestamp("mov_last_acc_datetime");
				rsData.mov_total_time = rs.getFloat("mov_total_time");
				rsData.mov_status = rs.getString("mov_status");
				rsData.mov_score = rs.getFloat("mov_score");
				rsData.pgr_grade = rs.getString("pgr_grade");
				modEvnVec.addElement(rsData);
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		return modEvnVec;
	}

	private void setStmtParameters(SpecData specData, PreparedStatement stmt) throws SQLException {
		int index = 1;
		if(specData.all_enrolled_lrn_ind || specData.all_mod_ind) {
			stmt.setString(index++, dbRegUser.USR_STATUS_OK);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setBoolean(index++, true);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			stmt.setBoolean(index++, true);
			stmt.setLong(index++, specData.cos_id);
			stmt.setString(index++, DbTrackingHistory.TKH_TYPE_APPLICATION);
			stmt.setInt(index++, 1);
			stmt.setString(index++, aeApplication.ADMITTED);
		} else {
			stmt.setString(index++, DbTrackingHistory.TKH_TYPE_APPLICATION);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setBoolean(index++, true);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			stmt.setBoolean(index++, true);
			stmt.setLong(index++, specData.cos_id);
			stmt.setInt(index++, 1);
			if(specData.start_datetime != null) {
			    stmt.setTimestamp(index++, specData.start_datetime);
			} 
			if(specData.end_datetime != null) {
			    stmt.setTimestamp(index++, specData.end_datetime);
			}			
		}
	}

	private static final String _GET_MOD_EVN_OF_COS_BY_PERIOD = " select res_title, mod_type, mod_max_score, usr_display_bil, ugr_display_bil, "
			+ " mov_last_acc_datetime, mov_total_time, mov_status, mov_score, usg_ern.ern_ancestor_ent_id as usg_ent_id, pgr_grade"
			+ " from moduleEvaluation" 
			+ " inner join resources on (mov_mod_id = res_id)" + " inner join module on (mod_res_id = res_id)"
			+ " inner join trackingHistory on (mov_tkh_id = tkh_id and tkh_type = ?)" + " inner join regUser on (usr_ent_id = mov_ent_id)"
			+ " inner join EntityRelation usg_ern on (usr_ent_id = usg_ern.ern_child_ent_id and usg_ern.ern_type = ? and usg_ern.ern_parent_ind = ?)"
			+ " inner join EntityRelation ugr_ern on (usr_ent_id = ugr_ern.ern_child_ent_id and ugr_ern.ern_type = ? and ugr_ern.ern_parent_ind = ?)"
			+ " inner join userGrade on (ugr_ent_id = ugr_ern.ern_ancestor_ent_id)" + " inner join acSite on (ste_ent_id = usr_ste_ent_id)"
			+ " left join Progress on (mov_tkh_id = pgr_tkh_id and mov_mod_id = pgr_res_id and pgr_grade is not null)" + " where mov_cos_id = ? and mov_total_attempt >= ?";

	private String getAllModEvnByConditionSQL(boolean isIncludeAllEnrolledLrn) {
		String sql = " select usr_display_bil, ugr_display_bil, usg_ern.ern_ancestor_ent_id as usg_ent_id, "
				+ " res_title, mod_type, mod_max_score, mov_last_acc_datetime, mov_total_time, mov_status, mov_score, pgr_grade"
				+ " from aeApplication"
				+ " inner join regUser on (usr_ent_id = app_ent_id and usr_status = ?)"
				+ " inner join EntityRelation usg_ern on (usr_ent_id = usg_ern.ern_child_ent_id and usg_ern.ern_type = ? and usg_ern.ern_parent_ind = ?)"
				+ " inner join EntityRelation ugr_ern on (usr_ent_id = ugr_ern.ern_child_ent_id and ugr_ern.ern_type = ? and ugr_ern.ern_parent_ind = ?)"
				+ " inner join userGrade on (ugr_ent_id = ugr_ern.ern_ancestor_ent_id)"
				+ " inner join course on (app_itm_id = cos_itm_id and cos_res_id = ?)" 
				+ " inner join resourceContent on (rcn_res_id = cos_res_id)"
				+ " inner join resources on (res_id = rcn_res_id_content)" 
				+ " inner join module on (rcn_res_id_content = mod_res_id)";

		if (!isIncludeAllEnrolledLrn) {
			sql += " inner join courseEvaluation on (cov_cos_id = rcn_res_id and app_tkh_id = cov_tkh_id and cov_commence_datetime is not null)";
		}

		sql += " left join moduleEvaluation on (mov_mod_id = mod_res_id and mov_ent_id = app_ent_id)"
				+ " left join trackingHistory on (mov_tkh_id = tkh_id and tkh_type = ?)"
				+ " left join Progress on (mov_tkh_id = pgr_tkh_id and mov_mod_id = pgr_res_id and pgr_attempt_nbr = ?)"
				+ " where app_status = ?";

		return sql;
	}
}
