package com.cw.wizbank.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

public class LearnerByCosStatisticInfo {
	public static final long PAGE_SIZE_SHOW_STAT_ONLY = 100;
	public static final long PAGE_SIZE_TOTAL_GROUP = 5;
	public static final long PAGE_SIZE_RECORD_PER_GROUP = 20;

	public static HashMap getStatisticInfo(Connection con,
			String[] ent_id_lst,
			Vector itm_lst_vec,
			Vector ats_lst, 
			Timestamp att_create_start_datetime,
			Timestamp att_create_end_datetime, 
			String col_order,
			long usr_ent_id,
			long root_ent_id, 
			boolean include_no_record,
			boolean show_stat_only,
			boolean tc_enabled,
			String lrn_scope_sql,long v_itm_id,boolean is_realTime_view_rpt)
			throws SQLException, cwException {
        String entIdTableName = null;
        String entIdColName = null;
        Vector usr_ent_ids = null;
        long cur_time= System.currentTimeMillis();
		String tableName = null;
		String tableName1 = null;
		String physicalTableName = null;
		String physicalTableName1 = null;
		String physicalEntIdTableName = null;
        MYSQLDbHelper mysqlDbHelper = null;
		boolean isMysql = false;
		if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
			mysqlDbHelper = new MYSQLDbHelper();
			isMysql = true;
		}
		
        // create temp table name of itm_id & ent_id
        if (ent_id_lst != null && ent_id_lst.length > 0) {
            Vector in_ent_ids = new Vector();
            for (int i = 0;i < ent_id_lst.length;i++) {
                in_ent_ids.addElement(new Long(ent_id_lst[i]));
            }
            Vector ent_id_vec = new Vector();
            usr_ent_ids = LearnerRptExporter.getUserEntId(con, ent_id_vec, in_ent_ids);
            entIdColName = "usr_ent_ids";
            entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);
        }
		if (itm_lst_vec != null) {
			tableName = cwSQL.createSimpleTemptable(con, "tmp_app_itm_id", cwSQL.COL_TYPE_LONG, 0);
		}
		tableName1 = cwSQL.createSimpleTemptable(con, "tmp_app_itm_ids" , cwSQL.COL_TYPE_LONG, 0);
		if(entIdColName !=null && entIdTableName != null) {
			cwSQL.insertSimpleTempTable(con, entIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
		}
		if (entIdTableName == null && lrn_scope_sql != null) {
		    entIdColName = "usr_ent_ids";
		    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0, lrn_scope_sql);
		}
		if(isMysql){
			physicalEntIdTableName = mysqlDbHelper.tempTable2Physical(con, entIdTableName);
		}
		if(itm_lst_vec != null){
			cwSQL.insertSimpleTempTable(con, tableName, itm_lst_vec, cwSQL.COL_TYPE_LONG);
			if(isMysql){
				physicalTableName = mysqlDbHelper.tempTable2Physical(con, tableName);
			}
		}
		List runnableItem = null;
		if(tc_enabled) {
			if(isMysql){
				runnableItem = aeItemRelation.getAllRunnableItemId(con, physicalTableName, root_ent_id);
			}else{
				runnableItem = aeItemRelation.getAllRunnableItemId(con, tableName, root_ent_id);
			}
		} else {
			if(isMysql){
				runnableItem = aeItemRelation.getRunnableItemId(con, physicalTableName, root_ent_id);
			}else{
				runnableItem = aeItemRelation.getRunnableItemId(con, tableName, root_ent_id);
			}
		}
		
		cwSQL.insertSimpleTempTable(con, tableName1, (Vector)runnableItem, cwSQL.COL_TYPE_LONG);
		if(isMysql){
			physicalTableName1 = mysqlDbHelper.tempTable2Physical(con, tableName1);
		}
		//System.out.println("开始: " + (System.currentTimeMillis() - cur_time));
		cur_time= System.currentTimeMillis();
		
		//Summary
		StatisticBean reportSummary = new StatisticBean();
		if(v_itm_id <=0){
			if(isMysql){
				reportSummary = getSummary(con, entIdColName,physicalEntIdTableName,itm_lst_vec, ats_lst, att_create_start_datetime, att_create_end_datetime, 
				    	root_ent_id, include_no_record, physicalTableName, physicalTableName1,is_realTime_view_rpt);
			}else{
				reportSummary = getSummary(con, entIdColName,entIdTableName,itm_lst_vec, ats_lst, att_create_start_datetime, att_create_end_datetime, 
				    	root_ent_id, include_no_record, tableName, tableName1,is_realTime_view_rpt);
			}

		}
		
		//System.out.println("getSummary: " + (System.currentTimeMillis() - cur_time));
		cur_time= System.currentTimeMillis();
		HashMap hs = null;
		if(isMysql){
			hs = getCosStatistic(con, entIdColName,  physicalEntIdTableName, itm_lst_vec, ats_lst, att_create_start_datetime, att_create_end_datetime, 
			    	usr_ent_id, root_ent_id, include_no_record,col_order,show_stat_only,true, tc_enabled, physicalTableName, physicalTableName1,is_realTime_view_rpt);
		}else{
			hs = getCosStatistic(con, entIdColName,  entIdTableName, itm_lst_vec, ats_lst, att_create_start_datetime, att_create_end_datetime, 
			    	usr_ent_id, root_ent_id, include_no_record,col_order,show_stat_only,true, tc_enabled, tableName, tableName1,is_realTime_view_rpt);
		}
		//System.out.println("getCosStatistic: " + (System.currentTimeMillis() - cur_time));
		cur_time= System.currentTimeMillis();
		
		//get each record
		if(v_itm_id > 0) {
			if(isMysql){
				hs = getCosRecord(con, entIdColName,  physicalEntIdTableName, ats_lst,att_create_start_datetime, att_create_end_datetime,
						col_order, usr_ent_id, root_ent_id, include_no_record,tc_enabled, hs,is_realTime_view_rpt);
			}else{
				hs = getCosRecord(con, entIdColName,  entIdTableName, ats_lst,att_create_start_datetime, att_create_end_datetime,
					col_order, usr_ent_id, root_ent_id, include_no_record,tc_enabled, hs,is_realTime_view_rpt);
			}
		}
		
        if(tableName != null){
			cwSQL.dropTempTable(con, tableName);
        }
        cwSQL.dropTempTable(con, tableName1);
        if(entIdTableName != null) {
        	cwSQL.dropTempTable(con, entIdTableName);	
        }
        if(isMysql){
        	mysqlDbHelper.dropTable(con, physicalTableName);
        	mysqlDbHelper.dropTable(con, physicalTableName1);
        	mysqlDbHelper.dropTable(con, physicalEntIdTableName);
        }
        hs.put("reportSummary", reportSummary);
        //System.out.println("getCosRecord: " + (System.currentTimeMillis() - cur_time));
		cur_time= System.currentTimeMillis();
		return hs;
	}
	
	private static HashMap handleTransData(Connection con, ResultSet rs, long rootId, boolean is_view, long count) throws SQLException, cwException  {
		HashMap hs = new HashMap();
		Vector keyVec = new Vector();
		Vector status = aeAttendanceStatus.getAllByRoot(con, rootId);
		Long dataKey = null;
		
		while(rs.next()) {
			StatisticBean sb = null;
			dataKey = new Long(rs.getLong("t_id"));
			long att_ats_id = rs.getLong("att_ats_id");
			float total_time = rs.getFloat("total_time");
			String total_score = rs.getString("total_score");
			int total_enroll = rs.getInt("total_enroll");
			int attempt = rs.getInt("total_attempt");
			int attempts_user = rs.getInt("attempts_user");
			String t_code = rs.getString("t_code");
			String t_title = rs.getString("t_title");
			
			ArrayList workList = null;
			if (hs.containsKey(dataKey)&&(att_ats_id !=0 && total_enroll != 0)) {
				workList = (ArrayList) hs.get(dataKey);
				sb = (StatisticBean) workList.get(0);
				if (sb.getHasValue() == 0) {
					sb.setHasValue(1);
				}
			} 
			// ignore it if the item is a rubbish data
			if(hs.containsKey(dataKey) && (att_ats_id ==0 || total_enroll == 0)){
				continue; 
			}
			if (!hs.containsKey(dataKey)) {
				keyVec.addElement(dataKey);
				workList = new ArrayList();
				sb = new StatisticBean(status);
				if (att_ats_id == 0 || total_enroll == 0)
					sb.setHasValue(0);
				else
					sb.setHasValue(1);
				sb.t_code = t_code;
				sb.t_title = t_title;
				hs.put(dataKey, workList);
				workList.add(sb);
			}
			if(sb.getHasValue()==1){
				sb.addTotalEnroll(total_enroll);
				sb.addAttemptUsers(attempts_user);
				sb.setStatus(att_ats_id, sb.getStatus(att_ats_id) + total_enroll);
				sb.addTimeSpent(total_time);
				sb.addScore(total_score);
				sb.addAttempts(attempt);
			}
			if(is_view && keyVec.size() == count+1) {
				hs.remove(dataKey);
				keyVec.remove(dataKey);
				break;
			}
		}
		rs.close();
		hs.put("keyVec", keyVec);
		hs.put("status", status);
		return hs;
	}
	public static HashMap transCosRecordData(HashMap hs, ResultSet rs, Connection con) throws SQLException {
		Long dataKey = null;
		 Vector<Long> usr_ent_vec = new Vector<Long>();
		ViewLearnerReport view = new ViewLearnerReport();
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		while(rs.next()) {
	        ViewLearnerReport.Data d = view.new Data();
            d.usr_ent_id = rs.getLong("usr_ent_id");
            d.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            d.usr_display_bil = rs.getString("usr_display_bil");
            d.app_id = rs.getLong("app_id");
            d.app_tkh_id = rs.getLong("app_tkh_id");
            d.t_id = rs.getLong("t_id");
            d.t_title = rs.getString("t_title");            
            d.t_code = rs.getString("t_code");
            d.t_type = rs.getString("t_type");
            d.itm_id = rs.getLong("itm_id");
            d.att_timestamp = rs.getTimestamp("att_timestamp");
            d.att_create_timestamp = rs.getTimestamp("att_create_timestamp");
            d.att_ats_id = rs.getLong("att_ats_id");
            d.cov_score = rs.getString("cov_score");
            d.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
            d.cov_commence_datetime = rs.getTimestamp("cov_commence_datetime");
            d.cov_total_time = rs.getFloat("cov_total_time");
            d.totalAttempt = rs.getInt("total");
			StatisticBean sb = null;
			dataKey = new Long(d.t_id );
			ArrayList workList = null;
			if (hs.containsKey(dataKey)&&(d.usr_ent_id!=0 && d.att_ats_id!=0 && d.att_create_timestamp!=null)) {
				workList = (ArrayList) hs.get(dataKey);
				sb = (StatisticBean) workList.get(0);
				if(sb.getHasValue()==0){
					sb.setHasValue(1);
				}			
				if(workList.size() < (PAGE_SIZE_RECORD_PER_GROUP +1)) {
					workList.add(d);	
				}
				hs.put(dataKey, workList);
			}
			if (!usr_ent_vec.contains(new Long(d.usr_ent_id))) {
				usr_ent_vec.addElement(new Long(d.usr_ent_id));
			}
		}
		hs.put("usrVec", usr_ent_vec);
		rs.close();
		return hs;
	}
	
	public static StatisticBean getSummary(Connection con,String entIdColName, String entIdTableName,Vector itm_lst_vec,
			 Vector ats_lst,Timestamp att_create_start_datetime,Timestamp att_create_end_datetime,
			 long root_ent_id,boolean include_no_record,String tableName, String tableName1,boolean is_realTime_view_rpt) throws SQLException, cwException{
		
		Vector status = aeAttendanceStatus.getAllByRoot(con, root_ent_id);
		StatisticBean reportSummary = new StatisticBean(status);
		String sql = OuterJoinSqlStatements.getTotalStatisticCos(con, entIdColName, entIdTableName, ats_lst, att_create_start_datetime, att_create_end_datetime, tableName, tableName1, include_no_record,is_realTime_view_rpt);
		
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		if (att_create_start_datetime != null) {
			stmt.setTimestamp(index++, att_create_start_datetime);
		}
		if (att_create_end_datetime != null) {
			stmt.setTimestamp(index++, att_create_end_datetime);
		}
		long time1 = System.currentTimeMillis();
		//System.out.println("sql = " +sql);
		ResultSet rs = stmt.executeQuery();
		//System.out.println("getSummary 1:"+(System.currentTimeMillis()-time1));
		time1 = System.currentTimeMillis();
		int user_total = 0;
		int item_total = 0;
		int total_enroll  = 0;
		String total_scroe = "0.0";
		float total_time = 0.0f;
		int attempt_user = 0;
		int total_attempt = 0;
		if (rs.next()) {
			user_total = rs.getInt("user_total");
			item_total = rs.getInt("item_total");
			total_enroll = rs.getInt("total_enroll");
			total_scroe = rs.getString("total_score");
			total_time = rs.getFloat("total_time");
			attempt_user = rs.getInt("attempts_user");
			total_attempt = rs.getInt("total_attempt");
		}
		cwSQL.cleanUp(rs, stmt);
		reportSummary.addScore(total_scroe);
		reportSummary.addTimeSpent(total_time);	
		reportSummary.setTotalLrn(user_total);
		reportSummary.setTotalCos(item_total);
		reportSummary.addTotalEnroll(total_enroll);
		reportSummary.addAttemptUsers(attempt_user);
		reportSummary.addAttempts(total_attempt);
		String sql_search = OuterJoinSqlStatements.getTatoalRecordGroupByAttID(con, ats_lst, entIdColName, entIdTableName,itm_lst_vec,att_create_start_datetime,att_create_end_datetime,tableName);
		if(!is_realTime_view_rpt){
			sql_search = OuterJoinSqlStatements.getTatoalRecordGroupByAttID2(con, ats_lst, entIdColName, entIdTableName,itm_lst_vec,att_create_start_datetime,att_create_end_datetime,tableName);
		}
		stmt = con.prepareStatement(sql_search);
		index = 1;
		if (att_create_start_datetime != null) {
			stmt.setTimestamp(index++, att_create_start_datetime);
		}
		if (att_create_end_datetime != null) {
			stmt.setTimestamp(index++, att_create_end_datetime);
		}
		stmt.setLong(index++, root_ent_id);
		time1 = System.currentTimeMillis();
		//System.out.println("sql_search = " +sql_search);
		rs = stmt.executeQuery();
		//System.out.println("getSummary 2:"+(System.currentTimeMillis()-time1));
		while(rs.next()) {
			long atsId = rs.getLong("att_ats_id");
			int record = rs.getInt("record");
			reportSummary.addStatus(atsId, record);
		}
		cwSQL.cleanUp(rs, stmt);
		return reportSummary;
	}

	public static HashMap getCosStatistic(Connection con, String entIdColName,String entIdTableName,
			Vector itm_lst_vec, Vector ats_lst,
			Timestamp att_create_start_datetime,
			Timestamp att_create_end_datetime,
		    long usr_ent_id, long root_ent_id, boolean include_no_record,
		    String col_order,boolean show_stat_only,boolean is_view,boolean tc_enabled,
			String itmtableName, String itmtableName1,boolean is_realTime_view_rpt) throws SQLException, cwException {
		long count = 0;
		if(show_stat_only) {
			count = PAGE_SIZE_SHOW_STAT_ONLY;
		} else {
			count = PAGE_SIZE_TOTAL_GROUP;
		}
		String sql = OuterJoinSqlStatements.lrnRptSearchCosStatistic(entIdColName, entIdTableName, ats_lst, att_create_start_datetime, att_create_end_datetime, include_no_record, itmtableName, itmtableName1,col_order,is_realTime_view_rpt);
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		if (att_create_start_datetime != null) {
			stmt.setTimestamp(index++, att_create_start_datetime);
		}
		if (att_create_end_datetime != null) {
			stmt.setTimestamp(index++, att_create_end_datetime);
		}
		long cur_time= System.currentTimeMillis();
		ResultSet rs = stmt.executeQuery();
		//System.out.println("lrnRptSearchCosStatistic: " + (System.currentTimeMillis() - cur_time));
		cur_time= System.currentTimeMillis();
		HashMap hs = handleTransData(con, rs, root_ent_id, is_view, count);
		stmt.close();
		return hs;
	}
	public static HashMap getCosRecord(
			  Connection con,
			  String entIdColName,
			  String entIdTableName,
			  Vector ats_lst,
			  Timestamp att_create_start_datetime,
			  Timestamp att_create_end_datetime,
			  String col_order,
			  long usr_ent_id, long root_ent_id,
              boolean include_no_record,
              boolean tc_enabled,
              HashMap hs,boolean is_realTime_view_rpt) throws SQLException {
		Vector itm_parent = (Vector)hs.get("keyVec");
		Vector itm_id_vec = null;
		if(itm_parent != null) {
			Vector child_id_vec = aeItemRelation.getItemIdByParent(con, itm_parent);
			itm_id_vec = new Vector();
			for(int i = 0; i<itm_parent.size(); i++) {
				itm_id_vec.addElement(itm_parent.get(i));
			}
			if(child_id_vec != null) {
				for(int i = 0; i<child_id_vec.size(); i++) {
					itm_id_vec.addElement(child_id_vec.get(i));
				}					
			}
		}
		String tableName2 = null;
		String tableName3 = null;
		if(itm_parent != null){
			tableName2 = cwSQL.createSimpleTemptable(con, "tmp_key_itm_ids" , cwSQL.COL_TYPE_LONG, 0);
			tableName3 = cwSQL.createSimpleTemptable(con, "tmp_par_itm_ids" , cwSQL.COL_TYPE_LONG, 0);
			cwSQL.insertSimpleTempTable(con, tableName2, itm_id_vec, cwSQL.COL_TYPE_LONG);
			cwSQL.insertSimpleTempTable(con, tableName3, itm_parent, cwSQL.COL_TYPE_LONG);
		}
		
		String sql_search = OuterJoinSqlStatements.lrnRptSearchCos(con, entIdColName, entIdTableName, ats_lst, att_create_start_datetime, 
				att_create_end_datetime, tableName2, tableName3, include_no_record,col_order,is_realTime_view_rpt);
		
		PreparedStatement stmt = con.prepareStatement(sql_search);
		int index = 1;
		if (att_create_start_datetime != null) {
			stmt.setTimestamp(index++, att_create_start_datetime);
		}
		if (att_create_end_datetime != null) {
			stmt.setTimestamp(index++, att_create_end_datetime);
		}
		stmt.setFetchSize(ReportExporter.fetchsize);
		long cur_time= System.currentTimeMillis();
		ResultSet rs = stmt.executeQuery();
		//System.out.println("lrnRptSearchCos: " + (System.currentTimeMillis() - cur_time));
		hs = transCosRecordData(hs, rs, con);
		if(itm_parent != null){
			cwSQL.dropTempTable(con,tableName2);
			cwSQL.dropTempTable(con, tableName3);
		}
		stmt.close();
		return hs;
	}
}
