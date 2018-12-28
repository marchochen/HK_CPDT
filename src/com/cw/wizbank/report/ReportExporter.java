/*
 * Created on 2005-9-15
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.utils.CommonLog;

import jxl.write.WriteException;

/**
 * @author dixson
 */
public abstract class ReportExporter {
    Connection con;
    ExportController controller;
    public static final int fetchsize = 10000;
    public ReportExporter(Connection incon, ExportController inController) {
        con = incon;
        controller = inController;
    }


    static final String SPEC_KEY_ENT_ID = "ent_id";
    static final String SPEC_KEY_USR_ENT_ID = "usr_ent_id";
    static final String SPEC_KEY_MOD_ID = "mod_id";
    static final String SPEC_KEY_USG_ENT_ID = "usg_ent_id";
    static final String SPEC_KEY_ITM_ID = "itm_id";
    static final String SPEC_KEY_TND_ID = "tnd_id";
    static final String SPEC_KEY_COS_ID = "cos_id";
    static final String SPEC_KEY_TCR_ID = "tcr_id";

    static final String SPEC_KEY_CONTENT_LST_VERSION = "version";
    static final String SPEC_KEY_CONTENT_LST_CATALOG = "catalog";
    static final String SPEC_KEY_CONTENT_LST_TYPE = "type";
    static final String SPEC_KEY_ITM_TYPE = "itm_type";
    static final String SPEC_KEY_TRAIN_SCOPE = "train_scope";
    static final String SPEC_KEY_FAC_TYPE = "fac_type";

    static final String SPEC_KEY_ATS_ID = "ats_id";
    static final String SPEC_KEY_CONTENT_LST_INSTRUCTOR = "instructor";
    static final String SPEC_KEY_CONTENT_LST_COS_MANAGER = "cos_manager";
    static final String SPEC_KEY_CONTENT_LST_PROJ_CONST = "proj_const";

    static final String TEMP_COL = "app_id";
    static final String SPEC_KEY_ATT_CREATE_START_DATETIME = "att_create_start_datetime";
    static final String SPEC_KEY_ATT_CREATE_END_DATETIME = "att_create_end_datetime";
    static final String SPEC_KEY_ATT_START_DATETIME = "att_start_datetime";
    static final String SPEC_KEY_ATT_END_DATETIME = "att_end_datetime";
    static final String SPEC_KEY_START_DATETIME = "start_datetime";
    static final String SPEC_KEY_END_DATETIME = "end_datetime";
    //progress controller
    public static final String EXPORT_CONTROLLER = "RptController";
    static final String FLAG_GET_COUNT = "get_count";
    static final String LAB_ALL_MY_STAFF = "lab_all_my_staff";
    static final String LAB_MY_DIRECT_STAFF = "lab_my_direct_staff";

    public String[] DEFAULT_SORT_COL_ORDER = {"att_timestamp",  "usr_ste_usr_id", "usr_display_bil", "att_create_timestamp", "att_ats_id","t_code","t_title"};


    //a inner class for tran spec value
    class SpecData {
        public int process_unit;
        String[] ent_id = null;
        String[] ent_id_lst = null;
        String[] mod_id_lst = null;
        String[] usg_ent_id_lst = null;
        String[] s_usg_ent_id_lst = null;
        String[] tnd_id_lst = null;
        long[] itm_id_lst = null;
        String staff = null;
        public long cos_id;
        long tcr_id;
        String[] dummy_type;
        String train_scope;
        int fac_type;
        
        boolean all_user_ind = false;
        boolean all_cos_ind = false;
        boolean answer_for_lrn = false;
        boolean answer_for_course_lrn = false;
        boolean answer_for_course = false;
        boolean answer_for_lrn_course = false;
        //for detail report of course->score->tracking report
        public boolean all_mod_ind;
        public boolean all_enrolled_lrn_ind;
        
        String rsp_title = null;
        String[] ats_id_lst = null;

        Timestamp itm_start_datetime = null;
        Timestamp itm_end_datetime = null;
        Timestamp att_create_start_datetime = null;
        Timestamp att_create_end_datetime = null;
        Timestamp att_start_datetime = null;
        Timestamp att_end_datetime = null;
        Timestamp start_datetime = null;
        Timestamp end_datetime = null;

        Vector content_vec = null;
        Vector usr_content_vec = null;
        Vector itm_content_vec = null;
        Vector usr_ent_id = null;
        Vector usr_ent_ids = null;
        Vector rpt_content = null;
        
        boolean include_no_record;
        boolean show_stat_only;
        boolean isMyStaff;
        public String sortCol;
        public String sortCol1;
        public String sortOrder;
        
        public String window_name = null;
        public String tempDir = null;
        public String relativeTempDirName = null;
        public String cur_lang = null;
        public String encoding = null;
        
        //for jifenreport
        boolean include_del_usr;//是否包含已删除用户
        boolean show_usg_only;//包含用户与用户组
        boolean isDetail;//是否是明细
        boolean all_usg_ind;
        
        //used for replace the vector contains all users in ta or admin's scope
        String lrn_scope_sql;
        
        long ils_itm_id;
        long ils_id;
    }

    class resultData {
        public String catNav;
        public int att_times;
        public String itm_type;
        public String itm_dummy_type;
        public String itm_code;
        public String grade_name;
        public String group_name;
        public String usr_tel_1;
        public String usr_extra_2;
        public String usr_email;
        public long usr_ent_id;
        public String usr_ste_usr_id;
        public String usr_display_bil;
        public Timestamp app_create_timestamp;
        public long app_tkh_id;
        public long itm_id;
        public String itm_title;
        public Timestamp att_timestamp;
        public Timestamp att_create_timestamp;
        public String att_ats_id;
        public long cos_res_id;
        public Timestamp cov_last_acc_datetime;
        public Timestamp cov_commence_datetime;
        public float cov_total_time;
        public double cov_score;
        public String cov_max_score;
        public Timestamp cov_complete_datetime;
        public int totalAttempt;
        public String tcr_title;
        Vector vtmod = new Vector();
    }

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
            getReportXls(prof, specData, (UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id), wizbini);
        }
        catch (Exception e) {
            controller.setErrorMsg(e.getMessage());
            CommonLog.error(e.getMessage(),e);
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
            getReportXls(prof, specData, (UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id), wizbini);
        }
        catch (Exception e) {
            controller.setErrorMsg(e.getMessage());
            CommonLog.error(e.getMessage(),e);
        }
    }
    
    public void getReportXlsByRsp2ils(long ils_id,long ils_itm_id , loginProfile prof, WizbiniLoader wizbini, String window_name) throws SQLException, cwException {
        // get the spec name and value pair by rsp_id
        SpecData specData = new SpecData();
        specData.ils_id = ils_id;
        specData.ils_itm_id=ils_itm_id;
        specData.cur_lang =prof.cur_lan;

        specData.window_name = window_name;
        specData.relativeTempDirName = specData.relativeTempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
        specData.tempDir = wizbini.getFileUploadTmpDirAbs();
		specData.encoding = wizbini.cfgSysSetupadv.getEncoding();
		specData.process_unit = wizbini.cfgSysSetupadv.getRptProcessUnit();
        try {
            getReportXls(prof, specData, (UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id), wizbini);
        }
        catch (Exception e) {
            controller.setErrorMsg(e.getMessage());
            CommonLog.error(e.getMessage(),e);
        }
    }

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
        if ((spec_values = (Vector)spec_pairs.get("is_my_staff")) != null) {
        	specData.isMyStaff = new Boolean((String)spec_values.get(0)).booleanValue();
        }
        
        if(wizbini.cfgTcEnabled) {
        	if(spec_pairs.containsKey("all_user_ind")) {
        		spec_values = (Vector)spec_pairs.get("all_user_ind");
        		if(((String)spec_values.get(0)).equals("1")) {
        			specData.all_user_ind = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_lrn")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_lrn");
        		if(((String)spec_values.get(0)).equals("1")) {
        			specData.answer_for_lrn = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_course_lrn")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_course_lrn");
        		if(((String)spec_values.get(0)).equals("1")) {
        			specData.answer_for_course_lrn = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_course")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_course");
        		if(((String)spec_values.get(0)).equals("1")) {
        			specData.answer_for_course = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_lrn_course")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_lrn_course");
        		if(((String)spec_values.get(0)).equals("1")) {
        			specData.answer_for_lrn_course = true;
        		}
        	}
        	if(specData.all_user_ind && spec_pairs.containsKey("answer_for_lrn") && spec_pairs.containsKey("answer_for_course_lrn")) {
//        		Vector vec = new Vector();
        		if((specData.answer_for_lrn && specData.answer_for_course_lrn )||(!specData.answer_for_lrn && !specData.answer_for_course_lrn )) {
//        			vec = ViewLearnerReport.getAllLrn(con, prof.usr_ent_id, prof.root_ent_id);
        			specData.lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
        		} else if (specData.answer_for_lrn && !specData.answer_for_course_lrn) {
//        			vec = ViewLearnerReport.getMyRspLrn(con, prof.usr_ent_id, prof.root_ent_id);
        		    specData.lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
        		} else if (!specData.answer_for_lrn && specData.answer_for_course_lrn) {
//        			vec = ViewLearnerReport.getMyRspCosEnrollLrn(con, prof.usr_ent_id, prof.root_ent_id);
        		    specData.lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
        		}
/*        		if(vec.size() == 0) {
        			specData.ent_id_lst = new String[1];
        			specData.ent_id_lst[0] = "0";
        		} else {
        			specData.ent_id_lst = new String[vec.size()];
        			for (int i=0; i<vec.size(); i++) {
        				Long ent_id = (Long)vec.elementAt(i);
        				specData.ent_id_lst[i] = ent_id.longValue() + "";
        			}
        		}*/
        	}
        	if(!spec_pairs.containsKey("tnd_id") && !spec_pairs.containsKey("itm_id")) {
        		specData.all_cos_ind = true;
        	}
        	if(specData.all_cos_ind &&spec_pairs.containsKey("answer_for_course")&&spec_pairs.containsKey("answer_for_lrn_course")) {
        		Vector vec = new Vector();
        		if((specData.answer_for_course && specData.answer_for_lrn_course )||(!specData.answer_for_course && !specData.answer_for_lrn_course )) {
        			vec = ViewLearnerReport.getAllCos(con, prof.usr_ent_id, prof.root_ent_id);
        		} else if (specData.answer_for_course && !specData.answer_for_lrn_course) {
        			vec = ViewLearnerReport.getMyRspCos(con, prof.usr_ent_id, prof.root_ent_id);
        		} else if (!specData.answer_for_course && specData.answer_for_lrn_course) {
        			vec = ViewLearnerReport.getMyRspLrnEnrollCos(con, prof.usr_ent_id, prof.root_ent_id);
        		}
        		if(vec.size() == 0) {
        			specData.itm_id_lst = new long[1];
        			specData.itm_id_lst[0] = 0;
        		} else {
        			specData.itm_id_lst = new long[vec.size()];
        			for(int i=0; i<vec.size(); i++) {
        				specData.itm_id_lst[i] = ((Long)vec.elementAt(i)).longValue();
        			}
        		}
        	}
        }
        //get ent_id from spec
        if (spec_pairs.containsKey(SPEC_KEY_ENT_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ENT_ID);

            specData.ent_id_lst = new String[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                specData.ent_id_lst[i] = (String)spec_values.elementAt(i);
            }
            specData.usr_ent_id = spec_values;
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

        //get mod_id from spec
        if (spec_pairs.containsKey(SPEC_KEY_MOD_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_MOD_ID);

            specData.mod_id_lst = new String[spec_values.size()];

            for (int i = 0; i < spec_values.size(); i++) {
                specData.mod_id_lst[i] = (String)spec_values.elementAt(i);
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
        if (spec_pairs.containsKey("s_usg_ent_id_lst")) {
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
					   CommonLog.error(e.getMessage(),e);
				   }
            }
            else {
                specData.ats_id_lst = new String[spec_values.size()];
                for (int i = 0; i < spec_values.size(); i++) {
                    specData.ats_id_lst[i] = (String)spec_values.elementAt(i);
                }
            }
        }
        
        //for jifen report
        if (spec_pairs.containsKey("is_detail_ind")) {
        	int ind = Integer.parseInt((String)((Vector) spec_pairs.get("is_detail_ind")).get(0));
        	if(ind == 1) specData.isDetail = true;
		}
		if (spec_pairs.containsKey("include_del_usr_ind")) {
			int ind = Integer.parseInt((String)((Vector) spec_pairs.get("include_del_usr_ind")).get(0));
        	if(ind == 1) specData.include_del_usr = true;
		}
		if (spec_pairs.containsKey("show_usg_only")) {
			specData.show_usg_only = new Boolean((String)((Vector) spec_pairs.get("show_usg_only")).get(0)).booleanValue();
		}
		if (spec_pairs.containsKey("all_usg_ind")) {
        	int ind = Integer.parseInt((String)((Vector) spec_pairs.get("all_usg_ind")).get(0));
        	if(ind == 1) specData.all_usg_ind = true;
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
        if (spec_pairs.containsKey(SPEC_KEY_ATT_START_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATT_START_DATETIME);
            specData.att_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        if (spec_pairs.containsKey(SPEC_KEY_ATT_END_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATT_END_DATETIME);
            specData.att_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }
        
        //get res_id
        if (spec_pairs.containsKey(SPEC_KEY_COS_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_COS_ID);
            specData.cos_id = Long.parseLong((String)spec_values.elementAt(0));
        }
        
        //get start time and end time
        if (spec_pairs.containsKey(SPEC_KEY_START_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_START_DATETIME);
            specData.start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }
        if (spec_pairs.containsKey(SPEC_KEY_END_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_END_DATETIME);
            specData.end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }
        
        if(spec_pairs.containsKey("all_mod_ind")) {
    		spec_values = (Vector)spec_pairs.get("all_mod_ind");
    		if(((String)spec_values.get(0)).equals("1")) {
    			specData.all_mod_ind = true;
    		}
    	}
        
        if(spec_pairs.containsKey("all_enrolled_lrn_ind")) {
    		spec_values = (Vector)spec_pairs.get("all_enrolled_lrn_ind");
    		if(((String)spec_values.get(0)).equals("1")) {
    			specData.all_enrolled_lrn_ind = true;
    		}
    	}
        
        if(spec_pairs.containsKey(SPEC_KEY_TCR_ID)) {
    		spec_values = (Vector)spec_pairs.get(SPEC_KEY_TCR_ID);
    		specData.tcr_id = Long.parseLong((String)spec_values.elementAt(0));
    	}
        if(spec_pairs.containsKey(SPEC_KEY_ITM_TYPE)) {
    		spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_TYPE);
    		specData.dummy_type = new String[spec_values.size()];
 		    for(int i = 0; i < spec_values.size(); i++) {
 		    	specData.dummy_type[i] = (String)spec_values.elementAt(i);
 		    }
    	}
        if(spec_pairs.containsKey(SPEC_KEY_TRAIN_SCOPE)) {
    		spec_values = (Vector)spec_pairs.get(SPEC_KEY_TRAIN_SCOPE);
    		specData.train_scope = (String)spec_values.elementAt(0);
    	}
    	
    	if(spec_pairs.containsKey(SPEC_KEY_FAC_TYPE)) {
    		spec_values = (Vector)spec_pairs.get(SPEC_KEY_FAC_TYPE);
    		specData.fac_type = Integer.valueOf((String)spec_values.elementAt(0)).intValue();
    	}

        //      System.out.println("-------------Start processing the page parameter----------");
        Vector v = null;
        if ((v = (Vector)spec_pairs.get("sort_col")) != null) {
            String col = (String)v.elementAt(0);
            if (col.equals("itm_code") || col.equals("itm_title")) {
                col = "t" + col.substring(col.indexOf("_"));
            }
            specData.sortCol = cwPagination.esc4SortSql(col);
            //System.out.println("===============sortCol=="+page.sortCol);
            v = null;
        }
        if ((v = (Vector)spec_pairs.get("sort_col1")) != null) {
            String col = (String)v.elementAt(0);
            if (col.equals("itm_code") || col.equals("itm_title")) {
                col = "t" + col.substring(col.indexOf("_"));
            }
            specData.sortCol1 = cwPagination.esc4SortSql(col);
            //System.out.println("================sortCol1=="+page.sortCol1);
            v = null;
        }
        if ((v = (Vector)spec_pairs.get("sort_order")) != null) {
            specData.sortOrder = cwPagination.esc4SortSql((String)v.elementAt(0));
            //System.out.println("===============sortOrder="+page.sortOrder);
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
    
    public abstract void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini)
         throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException;

    //get the user group
    public String[] getApprGroups(loginProfile prof) throws SQLException {
        //get prof's approval groups
        ViewRoleTargetGroup[] apprGroups = 
            ViewRoleTargetGroup.getTargetGroups(con, prof.usr_ent_id, prof.current_role, true, true);
        String[] appr_group = new String[apprGroups.length];
        for(int i=0; i < appr_group.length; i++) {
            appr_group[i] = "" + apprGroups[i].targetEntIds[0];
        }
        return appr_group;
    }

    /**
     * @param con
     * @param ent_ids
     * @return ent_names
     * @throws SQLException
     * 
     * return ent_names,no matter user or group
     */
    public static String getEntName(Connection con, String[] strings) throws SQLException {
        Vector entIds = new Vector();
        String tableName = null;
        String colName = null;
        for (int i = 0; i < strings.length; i++) {
            entIds.add(Long.valueOf(strings[i]));
        }
        colName = "ent_ids";
        tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, entIds, cwSQL.COL_TYPE_LONG);

        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sql = new StringBuffer();
        sql.append("select")
           .append(cwSQL.replaceNull("usr_display_bil", "usg_display_bil"))
           .append(" as ent_name");
        if (dbproduct.indexOf(OuterJoinSqlStatements.ProductName_ORACLE) >= 0) {
            sql.append(" from Entity, RegUser, UserGroup where ent_id = usr_ent_id(+) and ent_id = usg_ent_id(+) and");
        }
        else {
            sql.append(" from Entity left join RegUser on (ent_id = usr_ent_id) left join UserGroup on (ent_id = usg_ent_id) where");
        }
        sql.append(" ent_id in (select ").append(colName).append(" from ").append(tableName).append(")");

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();
        StringBuffer entNameResult = new StringBuffer();
        boolean isFirst = true;
        while (rs.next()) {
            String entName = rs.getString("ent_name");
            if (!rs.wasNull()) {
                if (!isFirst) {
                    entNameResult.append(", ");
                }
                entNameResult.append(entName);
            }
            isFirst = false;
        }
        stmt.close();
        return entNameResult.toString();
    }

    //get dir title
    public static String getTntName(Connection con, String[] strings) throws SQLException {
        Vector tndIds = new Vector();
        String tableName = null;
        String colName = null;
        for(int i = 0;i <strings.length;i++) {
            tndIds.add(new Long(strings[i]));
        }
        colName = "tnd_ids";
        tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, tndIds, cwSQL.COL_TYPE_LONG);
        StringBuffer sql = new StringBuffer();
        
        sql.append("select tnd_title from aeTreeNode")
        .append(" where")
        .append(" tnd_id in (").append("select ").append(colName).append(" from ").append(tableName).append(")");
        
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        ResultSet rs = stmt.executeQuery();
        StringBuffer tndNameResult = new StringBuffer();
        boolean isFirst = true;
        while(rs.next()) {
            String tndName = rs.getString("tnd_title");
            if (!rs.wasNull()) {
                if (!isFirst) {
                tndNameResult.append(", ");
            }
                tndNameResult.append(tndName);
            }
            isFirst = false;
        }
        stmt.close();
        return tndNameResult.toString();
    }
    
    public static Vector getUserEntId(Connection con, Vector user_ent_ids, List in_ent_ids) throws SQLException {
        String ent_type = null;
        long ent_id_long;
//a temp table for in_ent_ids
        String tableName = null;
        String colName = "ent_id";
        tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, in_ent_ids, cwSQL.COL_TYPE_LONG);
        StringBuffer sql_buf = new StringBuffer();
        sql_buf.append("select ent_id, ent_type ")
        .append("from Entity ")
        .append("where ent_id in (")
        .append("select ").append(colName).append(" from ").append(tableName).append(")");

        PreparedStatement stmt = con.prepareStatement(sql_buf.toString());
        ResultSet rs = stmt.executeQuery();
        Vector user_group_ids = new Vector();
        user_ent_ids.add(new Long(0));
        while (rs.next()) {
            ent_type = rs.getString("ent_type");
            ent_id_long = rs.getLong("ent_id");
            if (ent_type.equals(dbEntity.ENT_TYPE_USER)) {
                if (!user_ent_ids.contains(new Long(ent_id_long))){
                    user_ent_ids.add(new Long(ent_id_long));
                }
            } else if (ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GROUP)) {
                user_group_ids.add(new Long(ent_id_long));
            }
        }
        cwSQL.dropTempTable(con, tableName);
        if (user_group_ids.size() > 0) {
            String groupTableName = null;
            String groupColName = "group_name";

            groupTableName = cwSQL.createSimpleTemptable(con, groupColName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, groupTableName, user_group_ids, cwSQL.COL_TYPE_LONG);

            sql_buf.setLength(0);
            sql_buf.append("select DISTINCT ern_child_ent_id ")
            .append("from EntityRelation ")
            .append("where ern_ancestor_ent_id in ( ")
            .append("select ").append(groupColName).append(" from ").append(groupTableName).append(") ")
            .append("AND ern_parent_ind = ? ");
            
            stmt = con.prepareStatement(sql_buf.toString());
            stmt.setBoolean(1, true);
            rs = stmt.executeQuery();
            Vector temp_vec = new Vector();
            while (rs.next()) {
                temp_vec.add(new Long(rs.getLong("ern_child_ent_id")));
            }
            cwSQL.dropTempTable(con, groupTableName);
            if (temp_vec.size() > 0) {
                getUserEntId(con, user_ent_ids, temp_vec);
            }
        }
        stmt.close();
        return user_ent_ids;
    }
    public static Vector getUsrEntIds(Connection con, SpecData specData, boolean tc_enabled) throws SQLException {
    	Vector usr_ent_ids = null;
        if (specData.ent_id_lst != null && specData.ent_id_lst.length > 0) {
            Vector empty_vec = new Vector();
            for (int i = 0;i < specData.ent_id_lst.length;i++) {
                empty_vec.addElement(new Long(specData.ent_id_lst[i]));
            }
        	if(specData.all_user_ind && tc_enabled) {
        		usr_ent_ids =empty_vec;
        	} else {
        		Vector ent_id_vec = new Vector();
        		usr_ent_ids = LearnerRptExporter.getUserEntId(con, ent_id_vec, empty_vec);	
        	}
            
        }
        return usr_ent_ids;
    }
    public static Vector getAllTypeItemId(Connection con, loginProfile prof, SpecData specData, boolean tc_enabled) throws SQLException {
        Vector itm_id_vec = null;
        //get all itm include classroom
        if (specData.itm_id_lst != null) {
            itm_id_vec = new Vector();
        	if(specData.all_cos_ind && tc_enabled) {
                for (int i = 0; i < specData.itm_id_lst.length; i++) {
                    itm_id_vec.addElement(new Long(specData.itm_id_lst[i]));
                }        		
        	} else {
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
        } else if (specData.tnd_id_lst != null) {
            itm_id_vec = new Vector();
            aeTreeNode.getItemsFromNode(con, specData.tnd_id_lst, itm_id_vec);
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
}
