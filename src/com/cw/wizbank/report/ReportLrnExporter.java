
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport.Data;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.ReportByCosExporter.resultCosData;
import com.cw.wizbank.report.ReportByCosExporter.resultData;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

/**
 * @author jackyx
 * @date 2007-04-10
 */
public  class ReportLrnExporter extends ReportExporter {
   
    
    public ReportLrnExporter(Connection incon, ExportController inController) {
    		super(incon, inController);
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
    public String DEFAULT_SORT_COL_ORDER = "usr_ste_usr_id";
    public String DEFAULT_SORT_COL_ORDER_2 = "usr_ent_id";
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
        public long app_id;
        public Timestamp app_create_timestamp;
        public String app_status;
        public String app_process_status;
        public long app_tkh_id;
        public String t_title;
        public float t_unit;
        public String t_code;
        public String t_type;
        public String itm_dummy_type;
        public Timestamp t_eff_start_datetime;
        public Timestamp t_eff_end_datetime;
        public String t_apply_method;
        public long itm_id;
        public String itm_title;
        public String catNav;
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
        public String tcr_title;
    }
    class resultLrnData {
    	public String usr_display_bil;
    	public String usr_ste_usr_id;
    	public int total_cos;
    	public int total_lrn;
    	public int total_enroll;
    	public float total_time;
    	public float total_sroce;
    	public int total_attempt;
    	public int attempts_user;
    	public String averge_sroce;
    	public String times;
    	public Vector ats_id_vec;
    	public int[] ats_id_lst;
    	public int[] ats_id_value;
    	public String[] ats_id_per;
    	
    	public long att_ats_id ;
    }

    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
    	//get usr_ent_id
    	Vector usr_ent_ids = getUsrEntIds(con, specData, wizbini.cfgTcEnabled); 
        //get all itm include classroom
    	Vector itm_id_vec = getAllTypeItemId(con, prof, specData,wizbini.cfgTcEnabled);
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
		if(entIdTableName == null && specData.lrn_scope_sql != null) {
		    entIdColName = "usr_ent_ids";
		    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0, specData.lrn_scope_sql);
		}
		if(isMysql){
			physicalEntIdTableName = mysqlDbHelper.tempTable2Physical(con, entIdTableName);
		}
		
		//Summary
		StatisticBean reportSummary = null;
		if(isMysql){
			 reportSummary = LearnerLrnStatisticInfo.getSummary(con, entIdColName, physicalEntIdTableName, itm_id_vec, ats_lst, specData.att_create_start_datetime, specData.att_create_end_datetime, 
					prof.root_ent_id, specData.include_no_record,physicalTableName, physicalTableName1,is_realTime_view_rpt);
		}else{
			 reportSummary = LearnerLrnStatisticInfo.getSummary(con, entIdColName, entIdTableName, itm_id_vec, ats_lst, specData.att_create_start_datetime, specData.att_create_end_datetime, 
					prof.root_ent_id, specData.include_no_record,tableName, tableName1,is_realTime_view_rpt);
		}

		resultLrnData rsSumData= new resultLrnData(); 
		rsSumData.ats_id_vec = aeAttendanceStatus.getAllStatusIdByRoot(con, prof.root_ent_id);
		rsSumData.ats_id_lst = new int[rsSumData.ats_id_vec.size()]; 
		rsSumData.ats_id_value = new int[rsSumData.ats_id_vec.size()];
		rsSumData.ats_id_per = new String[rsSumData.ats_id_vec.size()];
		rsSumData.total_cos = reportSummary.getTotalCos();
		rsSumData.total_lrn = reportSummary.getTotalLrn();
		rsSumData = getResLrnData(reportSummary, rsSumData);
		
		resultLrnData rsLrnData= new resultLrnData(); 
		rsLrnData.ats_id_vec = rsSumData.ats_id_vec;
		rsLrnData.ats_id_lst = new int[rsLrnData.ats_id_vec.size()]; 
		rsLrnData.ats_id_value = new int[rsLrnData.ats_id_vec.size()];
		rsLrnData.ats_id_per = new String[rsLrnData.ats_id_vec.size()];
		
		//get all record information 
		PreparedStatement stmt_show_all = null;
		ResultSet rs_show_all = null;
		String sql_search = null;
		if(isMysql){
			 sql_search = OuterJoinSqlStatements.lrnRptSearchLrn(entIdColName, physicalEntIdTableName, ats_lst, specData.att_create_start_datetime, 
					specData.att_create_end_datetime, physicalTableName, physicalTableName1, specData.include_no_record, col_order.toString(),is_realTime_view_rpt);
		}else{
			 sql_search = OuterJoinSqlStatements.lrnRptSearchLrn(entIdColName, entIdTableName, ats_lst, specData.att_create_start_datetime, 
					specData.att_create_end_datetime, tableName, tableName1, specData.include_no_record, col_order.toString(),is_realTime_view_rpt);
		}
			stmt_show_all = con.prepareStatement(sql_search);
			int index_all = 1;
			if (specData.att_create_start_datetime != null) {
				stmt_show_all.setTimestamp(index_all++, specData.att_create_start_datetime);
			}
			if (specData.att_create_end_datetime != null) {
				stmt_show_all.setTimestamp(index_all++, specData.att_create_end_datetime);
			}
			stmt_show_all.setFetchSize(ReportCosExporter.fetchsize);
			rs_show_all = stmt_show_all.executeQuery();
    	
		int rpt_count = 0;
		rpt_count = reportSummary.getTotalEnroll();
		if(rpt_count == 0) {
			rpt_count = reportSummary.getTotalLrn();
		}
		
		ReportLrnRptExporter rptBuilder = new ReportLrnRptExporter(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit);
		rptBuilder.writeCondition(con, specData);
    
		rptBuilder.writeSummaryData(rsSumData, specData.cur_lang);
		//if show statistic only, write the header
		if(export_stat_only) {
			rptBuilder.writeLrnTableHead(rsSumData.ats_id_lst , specData.cur_lang);
		}
		
		HashMap<Long, Vector<resultData>> usr_app_map = new HashMap<Long, Vector<resultData>>();
		HashMap<Long, Vector<resultLrnData>> usr_sum_map = new HashMap<Long, Vector<resultLrnData>>();
		Vector<Long> order_vec = new Vector<Long>();
		while(rs_show_all.next()) {
			long usr_ent_id = rs_show_all.getLong("usr_ent_id");
			
			resultData usr_data = getRecordData(rs_show_all);
			if (!usr_app_map.containsKey(usr_ent_id)) {
				Vector<resultData> usr_vec = new Vector<resultData>();
				usr_app_map.put(usr_ent_id, usr_vec);
				order_vec.add(usr_ent_id);
			}
			Vector<resultData> usr_vec = usr_app_map.get(usr_ent_id);
			usr_vec.add(usr_data);
			
			resultLrnData sum_data = getResCosAppData(rs_show_all);
			if(!usr_sum_map.containsKey(usr_ent_id)){
				Vector<resultLrnData> sum_vec = new Vector<resultLrnData>();
				usr_sum_map.put(usr_ent_id, sum_vec);
			}
			Vector<resultLrnData> sum_vec = usr_sum_map.get(usr_ent_id);
			sum_vec.add(sum_data);
		}
		
		rs_show_all.close();
		stmt_show_all.close();
		
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
		
        for (int i = 0; i < order_vec.size(); i++) {
			long usr_ent_id = (Long)order_vec.elementAt(i);
			if(usr_app_map.get(usr_ent_id) != null && usr_app_map.get(usr_ent_id) != null){
				Vector<resultLrnData> sum_vec = usr_sum_map.get(usr_ent_id);
					int total_enroll = rptBuilder.writeLrnDataHead(sum_vec, specData.cur_lang,export_stat_only);
					if(total_enroll > 0 && !export_stat_only){
						rptBuilder.writeTableHead(specData.cur_lang);
						Vector<resultData> vec_usr = usr_app_map.get(usr_ent_id);
						rptBuilder.writeData(vec_usr,specData.cur_lang);
					}
				}
			controller.next();
		}
        
        if (!controller.isCancelled()) {
            controller.setFile(rptBuilder.finalizeFile());
        }
        
    }
    private resultLrnData getResLrnData(StatisticBean sb, resultLrnData rsLrnData) throws cwException {
		rsLrnData.usr_display_bil = sb.u_display;
		rsLrnData.usr_ste_usr_id = sb.u_login_id;
		rsLrnData.total_enroll = sb.getTotalEnroll();
		rsLrnData.total_attempt = sb.getTotalAttemp();
		rsLrnData.times = sb.transTime();
		rsLrnData.averge_sroce = sb.getAverScore(sb.getAttemptUsers());
		for(int j= 0; j < rsLrnData.ats_id_vec.size(); j ++) {
			rsLrnData.ats_id_lst[j] = ((Integer)rsLrnData.ats_id_vec.get(j)).intValue();
			rsLrnData.ats_id_value[j] = sb.getStatus((long)rsLrnData.ats_id_lst[j]);
			rsLrnData.ats_id_per[j] = sb.getEnrolledPerc(rsLrnData.ats_id_lst[j]);
		}
		return rsLrnData;
    }
    private resultLrnData getResCosAppData(ResultSet rs) throws SQLException{
    	resultLrnData resLrnAppData = new resultLrnData();
    	resLrnAppData.att_ats_id = rs.getLong("att_ats_id");
    	resLrnAppData.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
    	resLrnAppData.usr_display_bil = rs.getString("usr_display_bil");
    	resLrnAppData.total_enroll = 1;
    	resLrnAppData.total_time = rs.getFloat("cov_total_time");
    	resLrnAppData.total_sroce = rs.getFloat("cov_score");
    	resLrnAppData.total_attempt = rs.getInt("total");
    	resLrnAppData.attempts_user = rs.getInt("attempts_user");
    	
    	return resLrnAppData;
    }
    
    private resultData getRecordData(ResultSet rs_show_all) throws SQLException, cwSysMessage {
        resultData resData = new resultData();
        resData.usr_ent_id = rs_show_all.getLong("usr_ent_id");
        resData.usr_ste_usr_id = rs_show_all.getString("usr_ste_usr_id");
        resData.usr_display_bil = rs_show_all.getString("usr_display_bil");
        resData.app_id = rs_show_all.getLong("app_id");
        resData.app_tkh_id = rs_show_all.getLong("app_tkh_id");
        resData.t_id = rs_show_all.getLong("t_id");
        resData.t_title = rs_show_all.getString("t_title");            
        resData.t_code = rs_show_all.getString("t_code");
        resData.t_type = rs_show_all.getString("t_type");
        resData.itm_dummy_type = aeItemDummyType.getDummyItemType(resData.t_type, rs_show_all.getBoolean("itm_blend_ind"), rs_show_all.getBoolean("itm_exam_ind"), rs_show_all.getBoolean("itm_ref_ind"));
        resData.att_timestamp = rs_show_all.getTimestamp("att_timestamp");
        resData.att_create_timestamp = rs_show_all.getTimestamp("att_create_timestamp");
        resData.att_ats_id = rs_show_all.getLong("att_ats_id");
        resData.cov_score = rs_show_all.getString("cov_score");
        resData.cov_last_acc_datetime = rs_show_all.getTimestamp("cov_last_acc_datetime");
        resData.cov_commence_datetime = rs_show_all.getTimestamp("cov_commence_datetime");
        resData.cov_total_time = rs_show_all.getFloat("cov_total_time");
        resData.totalAttempt = rs_show_all.getInt("total");
        resData.catNav = getCatNav(resData.t_id);
        resData.tcr_title = rs_show_all.getString("tcr_title");	
        
        return resData;
    }

    //get dir nav
    public String getCatNav(long itm_id) throws SQLException, cwSysMessage {
        Vector tempCatNavStr = new Vector();
        String[] catNav = null;
        String sql = "SELECT CATA.tnd_id AS CATID from aeTreeNode CATA, aeTreeNode ITM WHERE CATA.tnd_id = ITM.tnd_parent_tnd_id AND ITM.tnd_itm_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            aeTreeNode node = new aeTreeNode();
            node.tnd_id = rs.getLong("CATID");
            node.get(con);
            aeTreeNode[] treeNodes = node.getNavigatorTreeNode(con);
            tempCatNavStr.add(getCatNavStr(treeNodes));
        }
        StringBuffer catNavStr = new StringBuffer();
        for (int i = 0; i < tempCatNavStr.size(); i++) {
            catNavStr.append(tempCatNavStr.get(i));
            if (i != tempCatNavStr.size() - 1) {
                catNavStr.append("\n");
            }
        }
        stmt.close();
        return catNavStr.toString();
    }
    private String getCatNavStr(aeTreeNode[] treeNodes) {
        StringBuffer catNavStr = new StringBuffer();
        for (int i = 0;i < treeNodes.length; i++) {
            catNavStr.append(treeNodes[i].tnd_title);
            if (i != treeNodes.length - 1) {
                catNavStr.append(" > ");
            }
        }
        return catNavStr.toString();
    }
}
