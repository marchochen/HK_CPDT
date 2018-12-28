
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
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

/**
 * @author jackyx
 * @date 2007-04-10
 */
public  class ReportCosExporter extends ReportExporter  {
//    Connection con;
//    ExportController controller;
//    public static final int fetchsize = 10000;
    
    public ReportCosExporter(Connection incon, ExportController inController) {
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


    //a inner class for tran spec value
    /*
    class SpecData {
        public int process_unit;
        String[] ent_id = null;
        String[] ent_id_lst = null;
        String[] usg_ent_id_lst = null;
        String[] s_usg_ent_id_lst = null;
        String[] tnd_id_lst = null;
        long[] itm_id_lst = null;
        String staff = null;

        String rsp_title = null;
        String[] ats_id_lst = null;

        Timestamp att_create_start_datetime = null;
        Timestamp att_create_end_datetime = null;

        Vector content_vec = null;
        Vector usr_content_vec = null;
        Vector itm_content_vec = null;
        Vector rpt_content = null;
        
        boolean include_no_record;
        boolean show_stat_only;
        
        public String sortCol;
        public String sortOrder;
        
        public String window_name = null;
        public String tempDir = null;
        public String relativeTempDirName = null;
        public String cur_lang = null;
        public String encoding = null;
    }
 */
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
    	public String itm_title;
    	public String itm_code;
    	public int total_cos;
    	public int total_lrn;
    	public int total_enroll;
    	public float total_time;
    	public int total_attempt;
    	public String averge_sroce;
    	public String times;
    	public Vector ats_id_vec;
    	public int[] ats_id_lst;
    	public int[] ats_id_value;
    	public String[] ats_id_per;
    }
    /*
//get param
    public void getReportXlsBySpec(String[] spec_name, String[] spec_value, long rte_id, loginProfile prof, WizbiniLoader wizbini, String window_name) throws SQLException, cwException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);
        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;
        ReportTemplate rt = new ReportTemplate();
        Hashtable spec_pairs = rt.getSpecPairs(data.rsp_xml);
        SpecData specData = getSpecData(spec_pairs, prof, wizbini, window_name);
        specData.process_unit = wizbini.cfgSysSetupadv.getRptProcessUnit();
        try {
            getReportXls(prof, specData, (UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id));
        }
        catch (Exception e) {
            controller.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
    }

    public void getReportXlsByRsp(long rsp_id, loginProfile prof, WizbiniLoader wizbini, String window_name) throws SQLException, cwException {
        // get the spec name and value pair by rsp_id
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
        ReportTemplate rt = new ReportTemplate();
        Hashtable spec_pairs = rt.getSpecPairs(data.rsp_xml);
        SpecData specData = getSpecData(spec_pairs, prof, wizbini, window_name);
        specData.rsp_title = data.rsp_title;
        specData.process_unit = wizbini.cfgSysSetupadv.getRptProcessUnit();
        try {
            getReportXls(prof, specData, (UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id));
        }
        catch (Exception e) {
            controller.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
    }
    */
    /*
//get paramer values
    public SpecData getSpecData(Hashtable spec_pairs, loginProfile prof, WizbiniLoader wizbini, String window_name) throws SQLException, cwException {
        SpecData specData = new SpecData();
        //spec values
        Vector spec_values;

        if ((spec_values = (Vector)spec_pairs.get("include_no_record")) != null) {
        	specData.include_no_record = new Boolean((String)spec_values.get(0)).booleanValue();
        }
        if ((spec_values = (Vector)spec_pairs.get("show_stat_only")) != null) {
        	specData.show_stat_only = new Boolean((String)spec_values.get(0)).booleanValue();
        }
        //get ent_id from spec
        if (spec_pairs.containsKey(SPEC_KEY_ENT_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ENT_ID);

            specData.ent_id_lst= new String[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                specData.ent_id_lst[i] = (String)spec_values.elementAt(i);
            }
        }
        
        //for moduleReport 
        if (spec_pairs.containsKey(SPEC_KEY_USR_ENT_ID) || spec_pairs.containsKey(SPEC_KEY_USG_ENT_ID)) {
			if((spec_values = (Vector)spec_pairs.get(SPEC_KEY_USR_ENT_ID))==null){
				spec_values = (Vector)spec_pairs.get(SPEC_KEY_USG_ENT_ID);
			 }
            specData.ent_id_lst = new String[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                specData.ent_id_lst[i] = (String)spec_values.elementAt(i);
            }
			if((spec_values = (Vector)spec_pairs.get(SPEC_KEY_USR_ENT_ID))==null){
				specData.usg_ent_id_lst = specData.ent_id_lst;
			} else {
				specData.ent_id = specData.ent_id_lst;
			}
        }

        //get usg_ent_id from spec
        if (spec_pairs.containsKey(SPEC_KEY_USG_ENT_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_USG_ENT_ID);

            specData.usg_ent_id_lst = new String[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                specData.usg_ent_id_lst[i] = (String)spec_values.elementAt(i);
            }
        }

        //get the index of report
        specData.window_name = window_name;
        specData.tempDir = wizbini.getFileUploadTmpDirAbs();
        
        // get the relative path of xls file
        specData.relativeTempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
        if (prof.label_lan.equalsIgnoreCase("ISO-8859-1")) {
            specData.cur_lang = "en-us";
        }else if (prof.label_lan.equalsIgnoreCase("GB2312")) {
            specData.cur_lang = "zh-cn";
        }else if (prof.label_lan.equalsIgnoreCase("BIG5")) {
            specData.cur_lang = "zh-hk";
        }
        specData.encoding = wizbini.cfgSysSetupadv.getEncoding();
        
        //get staff from spec for staff
        if (spec_pairs.containsKey(SPEC_KEY_S_USG_ENT_ID_LST)) {
            spec_values = (Vector)spec_pairs.get("s_usg_ent_id_lst");

            specData.s_usg_ent_id_lst = new String[spec_values.size()];
            Supervisor sup = new Supervisor(con, prof.usr_ent_id);
            Vector v_ent_id = new Vector();
            v_ent_id.add(new Long(0));

            for (int i = 0; i < spec_values.size(); i++) {
                specData.s_usg_ent_id_lst[i] = (String)spec_values.elementAt(i);
                if (specData.s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_STAFF)) {
                    specData.staff = LAB_ALL_MY_STAFF;
                    Vector vStaff = sup.getStaffEntIdVector(con);
                    for (int k = 0; k < vStaff.size(); k++) {
                        v_ent_id.addElement(vStaff.elementAt(k));
                    }
                }
                else if (specData.s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_DIRECT_STAFF)) {
                    Vector vStaff = sup.getDirectStaffEntIdVector(con);
                    specData.staff = LAB_MY_DIRECT_STAFF;
                    for (int k = 0; k < vStaff.size(); k++) {
                        v_ent_id.addElement(vStaff.elementAt(k));
                    }
                }
                else {
                    v_ent_id.addElement(Long.valueOf(specData.s_usg_ent_id_lst[i]));
                }
            }
            specData.ent_id_lst = new String[v_ent_id.size()];
            for (int i = 0; i < v_ent_id.size(); i++) {
                specData.ent_id_lst[i] = (v_ent_id.elementAt(i).toString());
            }
        }
        
        //get dir spec
        if (spec_pairs.containsKey(SPEC_KEY_TND_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_TND_ID);

            specData.tnd_id_lst = new String[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                specData.tnd_id_lst[i] = (String)spec_values.elementAt(i);
            }
        }

        //get item id
        if (spec_pairs.containsKey(SPEC_KEY_ITM_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_ID);
            specData.itm_id_lst = new long[spec_values.size()];
            for (int i = 0; i < spec_values.size(); i++) {
                specData.itm_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
            }
        }
        
        //get the complate status in spec
        if (spec_pairs.containsKey(SPEC_KEY_ATS_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATS_ID);
            if (((String)spec_values.elementAt(0)).equals("0")) {
				   Vector vID = aeAttendanceStatus.getAllStatusIdByRoot(con, prof.root_ent_id);
				   specData.ats_id_lst = new String[vID.size()];
				   try{
					  for(int i=0;i<vID.size();i++){
						  specData.ats_id_lst[i]=((Integer)vID.get(i)).toString();;
					  }
				   }catch(NumberFormatException  e){
					   e.printStackTrace();
				   }
			   }
            else {
                specData.ats_id_lst = new String[spec_values.size()];
                for (int i = 0; i < spec_values.size(); i++) {
                    specData.ats_id_lst[i] = (String)spec_values.elementAt(i);
                }
            }
        }
        //get attent start time
        if (spec_pairs.containsKey(SPEC_KEY_ATT_CREATE_START_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATT_CREATE_START_DATETIME);
            specData.att_create_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }
        if (spec_pairs.containsKey(SPEC_KEY_ATT_CREATE_END_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATT_CREATE_END_DATETIME);
            specData.att_create_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        Vector v = null;
        if ((v = (Vector)spec_pairs.get("sort_col")) != null) {
            String col = (String)v.elementAt(0);
            if (col.equals("itm_code") || col.equals("itm_title")) {
                col = "t" + col.substring(col.indexOf("_"));
            }
            specData.sortCol = col;
            v = null;
        }
        if ((v = (Vector)spec_pairs.get("sort_order")) != null) {
            specData.sortOrder = (String)v.elementAt(0);
            v = null;
        }
        specData.content_vec = (Vector)spec_pairs.get("content_lst");
        specData.usr_content_vec = (Vector)spec_pairs.get("usr_content_lst");
        specData.itm_content_vec = (Vector)spec_pairs.get("itm_content_lst");
        specData.rpt_content = new Vector();
        if (specData.usr_content_vec != null) {
        	specData.rpt_content.addAll(specData.usr_content_vec);
        }
        if (specData.itm_content_vec != null) {
        	specData.rpt_content.addAll(specData.itm_content_vec);
        }
        if (specData.content_vec != null) {
        	specData.rpt_content.addAll(specData.content_vec);
        }
        return specData;
    }
    */
    
    /*
    public static Vector getUsrEntIds(Connection con, SpecData specData) throws SQLException {
    	Vector usr_ent_ids = null;
        if (specData.ent_id_lst != null && specData.ent_id_lst.length > 0) {
            Vector empty_vec = new Vector();
            for (int i = 0;i < specData.ent_id_lst.length;i++) {
                empty_vec.addElement(new Long(specData.ent_id_lst[i]));
            }
            Vector ent_id_vec = new Vector();
            usr_ent_ids = ReportExporter.getUserEntId(con, ent_id_vec, empty_vec);
        }
        return usr_ent_ids;
    }
   
    public static Vector getAllTypeItemId(Connection con, loginProfile prof, SpecData specData) throws SQLException {
        Vector itm_id_vec = null;
        //get all itm include classroom
        if (specData.itm_id_lst != null) {
            itm_id_vec = new Vector();
            aeItem itm = null;
            Vector childItm;
            for (int i = 0; i < specData.itm_id_lst.length; i++) {
                itm = new aeItem();
                itm.itm_id = specData.itm_id_lst[i];
                if (itm.getCreateRunInd(con)) {
                    childItm = aeItemRelation.getChildItemId(con, specData.itm_id_lst[i]);
                    for (int k = 0; k < childItm.size(); k++) {
                        itm_id_vec.addElement((Long)childItm.elementAt(k));
                    }
                }
                itm_id_vec.addElement(new Long(specData.itm_id_lst[i]));
            }
        }
        else if (specData.tnd_id_lst != null) {
            itm_id_vec = new Vector();
            for (int i = 0; i < specData.tnd_id_lst.length; i++) {
                Hashtable cat_item_hash = new Hashtable();
                aeTreeNode.getItemsFromNode(con, prof, (new Long(specData.tnd_id_lst[i])).longValue(), cat_item_hash, itm_id_vec, false);
            }
        }
        return itm_id_vec;
    }
    public static Vector getAtsList(SpecData specData) {
        Vector ats_lst = null;
        if (specData.ats_id_lst != null) {
            ats_lst = new Vector();
            for (int i = 0; i < specData.ats_id_lst.length; i++) {
                ats_lst.addElement(specData.ats_id_lst[i]);
            }
        }
        return ats_lst;
    }
    */
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
		}
		List runnableItem = null;
		if(wizbini.cfgTcEnabled) {
			runnableItem = aeItemRelation.getAllRunnableItemId(con, tableName, prof.root_ent_id);	
		} else {
			runnableItem = aeItemRelation.getRunnableItemId(con, tableName, prof.root_ent_id);
		}
		cwSQL.insertSimpleTempTable(con, tableName1, (Vector)runnableItem, cwSQL.COL_TYPE_LONG);
		
		if(entIdColName !=null && entIdTableName != null) {
			cwSQL.insertSimpleTempTable(con, entIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
		}
		if (entIdTableName == null && specData.lrn_scope_sql != null) {
		    entIdColName = "usr_ent_ids";
		    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0, specData.lrn_scope_sql);
		}
		//Summary
		StatisticBean reportSummary = LearnerCosStatisticInfo.getSummary(con, entIdColName,entIdTableName, itm_id_vec, ats_lst, specData.att_create_start_datetime, specData.att_create_end_datetime, 
				prof.usr_ent_id, prof.root_ent_id, specData.include_no_record, wizbini.cfgTcEnabled, tableName, tableName1); 	
        //set summary data
		resultCosData rsSumData= new resultCosData(); 
		rsSumData.ats_id_vec = aeAttendanceStatus.getAllStatusIdByRoot(con, prof.root_ent_id);
		rsSumData.ats_id_lst = new int[rsSumData.ats_id_vec.size()]; 
		rsSumData.ats_id_value = new int[rsSumData.ats_id_vec.size()];
		rsSumData.ats_id_per = new String[rsSumData.ats_id_vec.size()];
		rsSumData.total_cos = reportSummary.getTotalCos();
		rsSumData.total_lrn = reportSummary.getTotalLrn();
		rsSumData = getResCosData(reportSummary, rsSumData);
		
		//get course the statistic information
		String sql_stat = OuterJoinSqlStatements.learnerReportSearchCosStatistic(con,
				entIdColName, entIdTableName, itm_id_vec, ats_lst, specData.att_create_start_datetime,
				specData.att_create_end_datetime, tableName, tableName1,col_order.toString(),
				prof.usr_ent_id, prof.root_ent_id, specData.include_no_record, wizbini.cfgTcEnabled);
		PreparedStatement stmt_stat = con.prepareStatement(sql_stat);
		int index = 1;
		stmt_stat.setLong(index++, prof.root_ent_id);
		if (specData.att_create_start_datetime != null) {
			stmt_stat.setTimestamp(index++, specData.att_create_start_datetime);
		}
		if (specData.att_create_end_datetime != null) {
			stmt_stat.setTimestamp(index++, specData.att_create_end_datetime);
		}
		if(wizbini.cfgTcEnabled) {
			stmt_stat.setLong(index++, prof.root_ent_id);
			stmt_stat.setString(index++, DbTrainingCenter.STATUS_OK);
		}
		stmt_stat.setLong(index++, prof.root_ent_id);
		ResultSet rs_stat = stmt_stat.executeQuery();
		
		//get each cos ats_id 
		resultCosData rsCosData= new resultCosData(); 
		rsCosData.ats_id_vec = rsSumData.ats_id_vec;
		rsCosData.ats_id_lst = new int[rsCosData.ats_id_vec.size()]; 
		rsCosData.ats_id_value = new int[rsCosData.ats_id_vec.size()];
		rsCosData.ats_id_per = new String[rsCosData.ats_id_vec.size()];
		
		//show all information 
		PreparedStatement stmt_show_all = null;
		ResultSet rs_show_all = null;
		if(!specData.show_stat_only){
			String sql_search = OuterJoinSqlStatements.learnerReportSearchCos(con,
					entIdColName, entIdTableName, itm_id_vec, 
					ats_lst,
					specData.att_create_start_datetime,
					specData.att_create_end_datetime,
					col_order.toString(), tableName, tableName1,
					prof.usr_ent_id, prof.root_ent_id,
					specData.include_no_record, wizbini.cfgTcEnabled);
			stmt_show_all = con.prepareStatement(sql_search);
			int index2 = 1;
			stmt_show_all.setLong(index2++, prof.root_ent_id);
			if (specData.att_create_start_datetime != null) {
				stmt_show_all.setTimestamp(index2++, specData.att_create_start_datetime);
			}
			if (specData.att_create_end_datetime != null) {
				stmt_show_all.setTimestamp(index2++, specData.att_create_end_datetime);
			}
			if(wizbini.cfgTcEnabled) {
				stmt_show_all.setLong(index2++, prof.root_ent_id);
				stmt_show_all.setString(index2++, DbTrainingCenter.STATUS_OK);
			}
			stmt_show_all.setBoolean(index2++, true);
			stmt_show_all.setBoolean(index2++, true);
			stmt_show_all.setLong(index2++, prof.root_ent_id);
			stmt_show_all.setFetchSize(fetchsize);
			rs_show_all = stmt_show_all.executeQuery();
		}
		
		//set the total row
		int rpt_count = 0;
		if(specData.show_stat_only) {
			rpt_count = reportSummary.getTotalCos();
		} else {
        	rpt_count = reportSummary.getTotalEnroll();
        	if(rpt_count == 0) {
        		rpt_count = reportSummary.getTotalCos();
        	}
        }
		controller.setTotalRow(rpt_count);
		
		ReportCosRptExporter rptBuilder = new ReportCosRptExporter(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit,wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getUrl());
		rptBuilder.writeCondition(con, specData);
		//write report summary
		rptBuilder.writeSummaryData(rsSumData, specData.cur_lang);
		//if show statistic only, write the header
		if(specData.show_stat_only) {
			rptBuilder.writeCosTableHead(rsSumData.ats_id_lst , specData.cur_lang);
		}
		
		Long dataNext = null;
		Long dataPre = null;
		StatisticBean sbNext = null;
		StatisticBean sbPre = null;
		resultData resData  = null;
		Vector status = aeAttendanceStatus.getAllByRoot(con, prof.root_ent_id);
		while(rs_stat.next()&& !controller.isCancelled()) {
			dataNext = new Long(rs_stat.getLong("t_id"));
			long att_ats_id = rs_stat.getLong("att_ats_id");
			float total_time = rs_stat.getFloat("total_time");
			String total_score = rs_stat.getString("total_score");
			int total_enroll = rs_stat.getInt("total_enroll");
			int attempt = rs_stat.getInt("total_attempt");
			int attempts_user = rs_stat.getInt("attempts_user");
			String t_code = rs_stat.getString("t_code");
			String t_title = rs_stat.getString("t_title");
			//if  next  be equals  previous, continue  statistic data 
			if(dataPre !=null && dataPre.equals(dataNext)) {
				if(att_ats_id!=0 && total_enroll != 0) {
					sbNext.setHasValue(1);
				} else {
					continue;
				}
			} else {
				//if the item have statistics, will write to excel.
				if(dataPre !=null) {
					resData = writeExcelData(rsCosData, resData, rptBuilder,
							reportSummary, sbPre, rs_show_all, dataPre,
							 specData, um);
				}
				
				sbNext = new StatisticBean(status);
				if (att_ats_id == 0 || total_enroll == 0)
					sbNext.setHasValue(0);
				else
					sbNext.setHasValue(1);
				sbNext.t_code = t_code;
				sbNext.t_title = t_title;
			}
			if(sbNext.getHasValue()==1){
				sbNext.addTotalEnroll(total_enroll);
				sbNext.addAttemptUsers(attempts_user);
				sbNext.setStatus(att_ats_id, sbNext.getStatus(att_ats_id) + total_enroll);
				sbNext.addTimeSpent(total_time);
				sbNext.addScore(total_score);
				sbNext.addAttempts(attempt);
			}
			dataPre = dataNext;
			sbPre = sbNext;
		}
		//write the last data 
		if(dataPre != null && sbPre != null) {
			resData = writeExcelData(rsCosData, resData, rptBuilder, reportSummary,
					sbPre, rs_show_all, dataPre,  specData, um);
		}
		stmt_stat.close();
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
		rsCosData.averge_sroce = sb.getAverScore(sb.getAttemptUsers());
		for(int j= 0; j < rsCosData.ats_id_vec.size(); j ++) {
			rsCosData.ats_id_lst[j] = ((Integer)rsCosData.ats_id_vec.get(j)).intValue();
			rsCosData.ats_id_value[j] = sb.getStatus((long)rsCosData.ats_id_lst[j]);
			rsCosData.ats_id_per[j] = sb.getEnrolledPerc(rsCosData.ats_id_lst[j]);
		}
		return rsCosData;
    }
    
    private resultData getRecordData(ResultSet rs_show_all, EntityFullPath entityfullpath) throws SQLException {
        resultData resData = new resultData();
        long group_id = rs_show_all.getLong("group_id");
        long grade_id = rs_show_all.getLong("grade_id");
        resData.usr_ent_id = rs_show_all.getLong("usr_ent_id");
        resData.usr_ste_usr_id = rs_show_all.getString("usr_ste_usr_id");
        resData.group_name = entityfullpath.getFullPath(con, group_id);
        resData.usr_display_bil = rs_show_all.getString("usr_display_bil");
        resData.usr_email = rs_show_all.getString("usr_email");
        resData.usr_tel_1 = rs_show_all.getString("usr_tel_1");
        resData.grade_name = entityfullpath.getEntityName(con, grade_id);
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
	private void writeRecordData(Long dataKey, resultData resData,
			ReportCosRptExporter rptBuilder, StatisticBean reportSummary,
			Hashtable hasWriteHead, Hashtable hasWriteData, 
			SpecData specData, UserManagement um) throws WriteException {
	  	
		if(!hasWriteHead.containsKey(dataKey)){
			if(resData.usr_ent_id!=0 && resData.att_ats_id!=0 && resData.att_create_timestamp!=null) {
				rptBuilder.writeTableHead(specData.rpt_content, specData.cur_lang, um);
			}
			if(reportSummary.getTotalEnroll() == 0) {
				controller.next();
			}
			hasWriteHead.put(dataKey, new Boolean(true));						
		}
		if(resData.usr_ent_id!=0 && resData.att_ats_id!=0 && resData.att_create_timestamp!=null){
			rptBuilder.writeData(resData, specData.rpt_content, specData.cur_lang);
			controller.next();
			hasWriteData.put(new Long(resData.app_tkh_id), new Boolean(true));
		}
	}
	private resultData writeExcelData(resultCosData rsCosData, resultData resData,
			ReportCosRptExporter rptBuilder, StatisticBean reportSummary,
			StatisticBean sbPre,ResultSet rs_show_all, Long dataPre,
			SpecData specData, UserManagement um) throws WriteException, cwException, SQLException {
		//write data to excel
		if(specData.show_stat_only) {
			rsCosData = getResCosData(sbPre, rsCosData);
			rptBuilder.writeCosData(rsCosData, specData.cur_lang);
			controller.next();		
			return resData;
		} else {
			rsCosData = getResCosData(sbPre, rsCosData);
			rptBuilder.writeCosDataHead(rsCosData, specData.cur_lang);
			Long dataKey = null;
			Hashtable hasWriteHead = new Hashtable();
			Hashtable hasWriteData = new Hashtable();
			// checked the previous  record
			if(resData != null && !controller.isCancelled()) {
				dataKey = new Long(resData.t_id);
				if(dataPre.equals(dataKey)) {
					writeRecordData(dataKey,resData,rptBuilder,reportSummary, hasWriteHead,hasWriteData,specData,um);					
				} else {
					throw new cwException("no learning record for the course: " + dataPre.longValue()+ " ,please try again.");
				}
			}
			EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
			while(rs_show_all.next() && !controller.isCancelled()) {
				resData = getRecordData(rs_show_all, entityfullpath);
				dataKey = new Long(resData.t_id);
				if (dataPre.equals(dataKey)) {
					writeRecordData(dataKey,resData,rptBuilder, reportSummary, hasWriteHead,hasWriteData,specData,um);
				} else {
					Boolean flag = (Boolean)hasWriteHead.get(dataPre);
					if(flag == null) {
						throw new cwException("no learning record for the course: " + dataPre.longValue()+ " ,please try again.");
					} else {
						return resData;
					}
				}
			}
			return resData;
		}
	}
}
