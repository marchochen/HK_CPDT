
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.ReportCosExporter.resultData;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

/**
 * @author jackyx
 * @date 2007-04-10
 */
public  class ReportByCosExporter extends ReportExporter  {
//    Connection con;
//    ExportController controller;
//    public static final int fetchsize = 10000;
    
    public ReportByCosExporter(Connection incon, ExportController inController) {
    		super(incon, inController);
//        con = incon;
//        controller = inController;
    }


    static final String SPEC_KEY_ENT_ID = "ent_id";
    static final String SPEC_KEY_USR_ENT_ID = "usr_ent_id";
    static final String SPEC_KEY_USG_ENT_ID = "usg_ent_id";
    static final String SPEC_KEY_S_USG_ENT_ID_LST = "s_usg_ent_id_lst";
    static final String SPEC_KEY_ITM_ID = "itm_id";
    static final String SPEC_KEY_TND_ID = "tnd_id";

    static final String SPEC_KEY_CONTENT_LST_TYPE = "type";

    static final String SPEC_KEY_ATS_ID = "ats_id";

    static final String TEMP_COL = "app_id";
    static final String SPEC_KEY_ATT_CREATE_START_DATETIME = "att_create_start_datetime";
    static final String SPEC_KEY_ATT_CREATE_END_DATETIME = "att_create_end_datetime";
    //progress controller
    public static final String EXPORT_CONTROLLER = "RptController";
    static final String FLAG_GET_COUNT = "get_count";
    static final String LAB_ALL_MY_STAFF = "lab_all_my_staff";
    static final String LAB_MY_DIRECT_STAFF = "lab_my_direct_staff";
    
    static final String DEFAULT_SORT_ORDER = "ASC";
    public String DEFAULT_SORT_COL_ORDER = "t_title";
    public String DEFAULT_SORT_COL_ORDER_2 = "t_id";

    public boolean export_stat_only;
    public boolean is_realTime_view_rpt;
    class resultData {
        public resultData() { ; }
        public long t_id;
        public long usr_ent_id;
        public String usr_ste_usr_id;
        public String usr_first_name_bil;
        public String usr_last_name_bil;
        public String usr_display_bil;
        public String usr_email;
        public String usr_tel_1;
        public String group_name;
        public String grade_name;
        public long app_id;
        public Timestamp app_create_timestamp;
        public String app_status;
        public String app_process_status;
        public long app_tkh_id;
        public String t_title;
        public float t_unit;
        public String t_code;
        public String t_type;
        public Timestamp t_eff_start_datetime;
        public Timestamp t_eff_end_datetime;
        public String t_apply_method;
        public long itm_id;
        public String itm_title;
        public Timestamp itm_eff_start_datetime;
        public Timestamp itm_eff_end_datetime;
        public Timestamp att_timestamp;
        public Timestamp att_create_timestamp;
        public long att_ats_id;
        public String att_remark;
        public String att_rate;
        public long cos_res_id;
        public Timestamp cov_last_acc_datetime;
        public Timestamp cov_commence_datetime;
        public float cov_total_time;
        public String cov_score;
        public String cov_max_score;
        public String cov_status;
        public String cov_comment;
        public Timestamp cov_complete_datetime;
        public int totalAttempt;
        public String tc_title;
    }
    class resultCosData {
    	public long att_ats_id ;
    	public String itm_title;
    	public String itm_code;
    	public int total_cos;
    	public int total_lrn;
    	public int total_enroll;
    	public float total_time;
    	public float total_sroce;
    	public int total_attempt;
    	public String averge_sroce;
    	public String times;
    	public Vector ats_id_vec;
    	public int[] ats_id_lst;
    	public int[] ats_id_value;
    	public String[] ats_id_per;
    	public int attempts_user;
    }
    
   
    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
    	//get usr_ent_id
    	Vector usr_ent_ids = getUsrEntIds(con, specData, wizbini.cfgTcEnabled); 
        //get all itm include classroom
    	Vector itm_id_vec = getAllTypeItemId(con, prof, specData, wizbini.cfgTcEnabled);
        //get ats_lst
        Vector ats_lst = getAtsList(specData);
        
		if (specData.sortOrder == null) {
			specData.sortOrder = DEFAULT_SORT_ORDER;
		}
		if (specData.sortCol == null) {
			specData.sortCol = DEFAULT_SORT_COL_ORDER;
		}
		StringBuffer col_order = new StringBuffer();
		col_order.append(" ORDER BY ");
		col_order.append(specData.sortCol).append(" ").append(specData.sortOrder).append(" , ").append(DEFAULT_SORT_COL_ORDER_2).append(" ").append(specData.sortOrder);
        
		String tableName = null;
		String tableName1 = null;
        String entIdTableName = null;
        String entIdColName = null;
		String physicalTableName = null;
		String physicalTableName1 = null;
		String physicalEntIdTableName = null;
        MYSQLDbHelper mysqlDbHelper = null;
		boolean isMysql = false;
		if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
			mysqlDbHelper = new MYSQLDbHelper();
			isMysql = true;
		}
        //crate Temp table for search
		if (itm_id_vec != null) {
			tableName = cwSQL.createSimpleTemptable(con, "tmp_app_itm_id", cwSQL.COL_TYPE_LONG, 0);
		}
		tableName1 = cwSQL.createSimpleTemptable(con, "tmp_app_itm_ids" , cwSQL.COL_TYPE_LONG, 0);
		if(usr_ent_ids !=null && usr_ent_ids.size() > 0) {
	        entIdColName = "usr_ent_ids";
			entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);
		}
		if(itm_id_vec != null){
			cwSQL.insertSimpleTempTable(con, tableName, itm_id_vec, cwSQL.COL_TYPE_LONG);
			if(isMysql){
				physicalTableName = mysqlDbHelper.tempTable2Physical(con, tableName);
			}
		}
		List runnableItem = null;
		if(wizbini.cfgTcEnabled) {
			if(isMysql){
				runnableItem = aeItemRelation.getAllRunnableItemId(con, physicalTableName, prof.root_ent_id);	
			}else{
				runnableItem = aeItemRelation.getAllRunnableItemId(con, tableName, prof.root_ent_id);	
			}
		} else {
			if(isMysql){
				runnableItem = aeItemRelation.getRunnableItemId(con, physicalTableName, prof.root_ent_id);
			}else{
				runnableItem = aeItemRelation.getRunnableItemId(con, tableName, prof.root_ent_id);
			}
		}
		cwSQL.insertSimpleTempTable(con, tableName1, (Vector)runnableItem, cwSQL.COL_TYPE_LONG);
		if(isMysql){
			physicalTableName1 = mysqlDbHelper.tempTable2Physical(con, tableName1);
		}
		
		if(entIdColName !=null && entIdTableName != null) {
			cwSQL.insertSimpleTempTable(con, entIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
		}
		if (entIdTableName == null && specData.lrn_scope_sql != null) {
		    entIdColName = "usr_ent_ids";
		    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0, specData.lrn_scope_sql);
		}
		if(isMysql){
			physicalEntIdTableName = mysqlDbHelper.tempTable2Physical(con, entIdTableName);
		}
		//Summary
		StatisticBean reportSummary = null;
		if(isMysql){
			 reportSummary = LearnerByCosStatisticInfo.getSummary(con, entIdColName,physicalEntIdTableName,itm_id_vec, ats_lst, specData.att_create_start_datetime, specData.att_create_end_datetime, 
					prof.root_ent_id, specData.include_no_record, physicalTableName, physicalTableName1,is_realTime_view_rpt); 	
		}else{
			 reportSummary = LearnerByCosStatisticInfo.getSummary(con, entIdColName,entIdTableName,itm_id_vec, ats_lst, specData.att_create_start_datetime, specData.att_create_end_datetime, 
					prof.root_ent_id, specData.include_no_record, tableName, tableName1,is_realTime_view_rpt); 	
		}

        //set summary data
		resultCosData rsSumData= new resultCosData(); 
		rsSumData.ats_id_vec = aeAttendanceStatus.getAllStatusIdByRoot(con, prof.root_ent_id);
		rsSumData.ats_id_lst = new int[rsSumData.ats_id_vec.size()]; 
		rsSumData.ats_id_value = new int[rsSumData.ats_id_vec.size()];
		rsSumData.ats_id_per = new String[rsSumData.ats_id_vec.size()];
		rsSumData.total_cos = reportSummary.getTotalCos();
		rsSumData.total_lrn = reportSummary.getTotalLrn();
		rsSumData = getResCosData(reportSummary, rsSumData);
		
		//get each cos ats_id 
		resultCosData rsCosData= new resultCosData(); 
		rsCosData.ats_id_vec = rsSumData.ats_id_vec;
		rsCosData.ats_id_lst = new int[rsCosData.ats_id_vec.size()]; 
		rsCosData.ats_id_value = new int[rsCosData.ats_id_vec.size()];
		rsCosData.ats_id_per = new String[rsCosData.ats_id_vec.size()];
		
		//show all information 
		PreparedStatement stmt_show_all = null;
		ResultSet rs_show_all = null;
		String sql_search = null;
		if(isMysql){
			 sql_search = OuterJoinSqlStatements.lrnRptSearchCos(con, entIdColName, physicalEntIdTableName, ats_lst, specData.att_create_start_datetime, specData.att_create_end_datetime, tableName, tableName1, specData.include_no_record,col_order.toString(),is_realTime_view_rpt);
		}else{
			 sql_search = OuterJoinSqlStatements.lrnRptSearchCos(con, entIdColName, entIdTableName, ats_lst, specData.att_create_start_datetime, specData.att_create_end_datetime, tableName, tableName1, specData.include_no_record,col_order.toString(),is_realTime_view_rpt);
		}
		stmt_show_all = con.prepareStatement(sql_search);
		int index = 1;
		if (specData.att_create_start_datetime != null) {
			stmt_show_all.setTimestamp(index++, specData.att_create_start_datetime);
		}
		if (specData.att_create_end_datetime != null) {
			stmt_show_all.setTimestamp(index++, specData.att_create_end_datetime);
		}
		stmt_show_all.setFetchSize(fetchsize);
		rs_show_all = stmt_show_all.executeQuery();
		
		//set the total row
		int rpt_count = 0;
		rpt_count = reportSummary.getTotalEnroll();
    	if(rpt_count == 0) {
    		rpt_count = reportSummary.getTotalCos();
    	}
		
		ReportByCosRptExporter rptBuilder = new ReportByCosRptExporter(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit,wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getUrl());
		rptBuilder.writeCondition(con, specData);
		//write report summary
		rptBuilder.writeSummaryData(rsSumData, specData.cur_lang);
		if(export_stat_only) {
			rptBuilder.writeCosTableHead(rsSumData.ats_id_lst , specData.cur_lang);
		}
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		Vector status = aeAttendanceStatus.getAllByRoot(con, prof.root_ent_id);
		
		HashMap<Long, Vector<resultData>> itm_app_map = new HashMap<Long, Vector<resultData>>();
		HashMap<Long, Vector<resultCosData>> itm_sum_map = new HashMap<Long, Vector<resultCosData>>();
		Vector<Long> order_vec = new Vector<Long>();
		Vector<Long> usrVec = new Vector<Long>();
		while(rs_show_all.next()) {
			long itm_id = rs_show_all.getLong("itm_id");
			long usr_ent_id = rs_show_all.getLong("usr_ent_id");
			if(!usrVec.contains(usr_ent_id)){
				usrVec.addElement(usr_ent_id); 
			}
			resultData usr_data = getRecordData(rs_show_all, entityfullpath);
			if (!itm_app_map.containsKey(itm_id)) {
				Vector<resultData> usr_vec = new Vector<resultData>();
				itm_app_map.put(itm_id, usr_vec);
				order_vec.add(itm_id);
			}
			Vector<resultData> usr_vec = itm_app_map.get(itm_id);
			usr_vec.add(usr_data);
			
			resultCosData sum_data = getResCosAppData(rs_show_all);
			if(!itm_sum_map.containsKey(itm_id)){
				Vector<resultCosData> sum_vec = new Vector<resultCosData>();
				itm_sum_map.put(itm_id, sum_vec);
			}
			Vector<resultCosData> sum_vec = itm_sum_map.get(itm_id);
			sum_vec.add(sum_data);
		}

		if(stmt_show_all != null) {
			stmt_show_all.close();	
		}
        if(tableName != null){
			cwSQL.dropTempTable(con,tableName);
        }
        cwSQL.dropTempTable(con,tableName1);
        if(entIdTableName != null) {
        	cwSQL.dropTempTable(con, entIdTableName);
        }
        if(isMysql){
        	mysqlDbHelper.dropTable(con, physicalTableName);
        	mysqlDbHelper.dropTable(con, physicalTableName1);
        	mysqlDbHelper.dropTable(con, physicalEntIdTableName);
        }
        controller.setTotalRow(order_vec.size());
        
        Hashtable usr_group_hash = null;
        Hashtable usr_grade_hash = null;
        if(usrVec != null && usrVec.size() > 0){
        	 String colName = "tmp_usr_ent_id";
     		String usrTableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
     		cwSQL.insertSimpleTempTable(con, usrTableName, usrVec, cwSQL.COL_TYPE_LONG);
     		usr_group_hash = dbEntityRelation.getGroupEntIdRelation(con, usrTableName, colName);
     		usr_grade_hash = dbEntityRelation.getGradeRelation(con, usrTableName, colName);
     		cwSQL.dropTempTable(con, usrTableName);
        }
		EntityFullPath full_path = EntityFullPath.getInstance(con);
		for (int i = 0; i < order_vec.size(); i++) {
			long itm_id = (Long)order_vec.elementAt(i);
			if(itm_sum_map.get(itm_id) != null && itm_app_map.get(itm_id) != null){
				Vector<resultCosData> sum_vec = itm_sum_map.get(itm_id);
				int total_enroll = rptBuilder.writeCosDataHead(sum_vec, specData.cur_lang,status,export_stat_only);
				if(total_enroll > 0 && !export_stat_only){
					rptBuilder.writeTableHead(specData.rpt_content, specData.cur_lang, um);
					Vector<resultData> vec_usr = itm_app_map.get(itm_id);
					if(vec_usr != null){
						for (int j = 0; j < vec_usr.size(); j++) {
							resultData resData = vec_usr.elementAt(j);
							long usg_ent_id = (Long)usr_group_hash.get(resData.usr_ent_id);
			            	resData.group_name = full_path.getFullPath(con, usg_ent_id);
			            	resData.grade_name = (String)usr_grade_hash.get(resData.usr_ent_id);
							rptBuilder.writeData(resData, specData.rpt_content, specData.cur_lang);
						}
					}
					
				}
			}
			controller.next();
		}
        
        if (!controller.isCancelled()) {
            controller.setFile(rptBuilder.finalizeFile());
        }
        
    }
    private resultCosData getResCosData(StatisticBean sb, resultCosData rsCosData) throws cwException {
		rsCosData.itm_code = sb.t_code;
		rsCosData.itm_title = sb.t_title;
		rsCosData.total_enroll = sb.getTotalEnroll();
		rsCosData.total_attempt = sb.getTotalAttemp();
		rsCosData.times = sb.transTime();
		rsCosData.averge_sroce = sb.getAverScore(sb.getTotalEnroll());
		for(int j= 0; j < rsCosData.ats_id_vec.size(); j ++) {
			rsCosData.ats_id_lst[j] = ((Integer)rsCosData.ats_id_vec.get(j)).intValue();
			rsCosData.ats_id_value[j] = sb.getStatus((long)rsCosData.ats_id_lst[j]);
			rsCosData.ats_id_per[j] = sb.getEnrolledPerc(rsCosData.ats_id_lst[j]);
		}
		return rsCosData;
    }
    private resultCosData getResCosAppData(ResultSet rs) throws SQLException{
    	resultCosData resCosAppData = new resultCosData();
    	resCosAppData.att_ats_id = rs.getLong("att_ats_id");
    	resCosAppData.itm_code = rs.getString("t_code");
    	resCosAppData.itm_title = rs.getString("t_title");
    	resCosAppData.total_enroll = 1;
    	resCosAppData.total_time = rs.getFloat("cov_total_time");
    	resCosAppData.total_sroce = rs.getFloat("cov_score");
    	resCosAppData.total_attempt = rs.getInt("total");
    	resCosAppData.attempts_user = rs.getInt("attempts_user");
    	return resCosAppData;
    }
    
    private resultData getRecordData(ResultSet rs_show_all, EntityFullPath entityfullpath) throws SQLException {
        resultData resData = new resultData();
//        long group_id = rs_show_all.getLong("group_id");
//        long grade_id = rs_show_all.getLong("grade_id");
        resData.usr_ent_id = rs_show_all.getLong("usr_ent_id");
        resData.usr_ste_usr_id = rs_show_all.getString("usr_ste_usr_id");
//        resData.group_name = entityfullpath.getFullPath(con, group_id);
        resData.usr_display_bil = rs_show_all.getString("usr_display_bil");
        resData.usr_email = rs_show_all.getString("usr_email");
        resData.usr_tel_1 = rs_show_all.getString("usr_tel_1");
//        resData.grade_name = entityfullpath.getEntityName(con, grade_id);
        resData.app_id = rs_show_all.getLong("app_id");
        resData.app_tkh_id = rs_show_all.getLong("app_tkh_id");
        resData.t_id = rs_show_all.getLong("t_id");
        resData.t_title = rs_show_all.getString("t_title");            
        resData.t_code = rs_show_all.getString("t_code");
        resData.t_type = rs_show_all.getString("t_type");
        resData.itm_id = rs_show_all.getLong("itm_id");
        resData.att_timestamp = rs_show_all.getTimestamp("att_timestamp");
        resData.att_create_timestamp = rs_show_all.getTimestamp("att_create_timestamp");
        resData.att_ats_id = rs_show_all.getLong("att_ats_id");
        resData.cov_score = rs_show_all.getString("cov_score");
        resData.cov_last_acc_datetime = rs_show_all.getTimestamp("cov_last_acc_datetime");
        resData.cov_commence_datetime = rs_show_all.getTimestamp("cov_commence_datetime");
        resData.cov_total_time = rs_show_all.getFloat("cov_total_time");
        resData.totalAttempt = rs_show_all.getInt("total");
        return resData;
    }
}
