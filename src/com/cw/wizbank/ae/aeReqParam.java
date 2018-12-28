package com.cw.wizbank.ae;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.db.DbFigure;
import com.cw.wizbank.ae.db.DbItemCost;
import com.cw.wizbank.ae.db.DbItemRating;
import com.cw.wizbank.codetable.CodeTable;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.mote.Mote;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import com.oreilly.servlet.MultipartRequest;
import com.oroinc.text.perl.Perl5Util;
        
// public class for processing any parameters at the request URL
// constructor requires a servlet request object
// method "common()" for the set of shared parameters
// add your own method for your own set of parameters
public class aeReqParam {
    private static final boolean XSL_DISPLAY = true;
    public static final String INVALID_TIME = "AERP01";
    
    private ServletRequest req;
    private boolean bMultiPart = false;
    private MultipartRequest multi = null;
    public cwPagination cwPage;    
    public String cmd;
    public String stylesheet;
    public String url_success;
    public String url_failure;
    public Timestamp cur_timestamp; //current time of database, user to perform concurrence control
//    public String usr_id;
    public long cat_id;
    public long node_id;

    // queue management var
    // borrowed by attendance
    public long app_id;
    public long ent_id;
    public long itm_id;
    public long tcr_id;
    public long process_id;
    public long action_id;
    public long aah_id;
    public long status_id;
    public int page;
    public String sort_by;
    public String order_by;
    public String[] order_bys;
    public String[] sort_bys;
    public String queue_type;
    public String fr;
    public String to;
    public String action_verb;
    public String content;
    public String app_xml;
    public boolean history;
    public Timestamp upd_timestamp;
    public String msg_subject_1;
    public String msg_subject_2;
    public String msg_subject_3;
    public String show_process_status;
    public String aal_status;
    public String filter_value;
    public String filter_type;
    public boolean clear_session;
    public Timestamp appn_upd_fr;
    public Timestamp appn_upd_to;
    public int app_lst_page;
    public int app_lst_page_size;
    public String user_code;
    Vector vFilterColName = new Vector();
    Vector vFilterColType = new Vector();
    Vector vFilterColValue = new Vector();
    //public String q_type;
    public long[] app_id_lst;
    public String[] comment_lst;
    public String comment;
    public Timestamp[] app_upd_timestamp_lst;
    public Hashtable app_lst_param;
    public String app_process_status;
    public String[] app_process_status_lst;
    public String app_tvw_id;
    public long[] ent_id_lst;
    public boolean download;
    public String[] app_priority_lst;

    public int att_status;
    public int pageSize;
	public int total;
    public String remark;

    //messging var
    public String ent_ids;
    public String url_redirect_param;

    //Catalog var
    public aeCatalog cat;
    public Timestamp cat_in_upd_timestamp;
    public long[] cat_acc_ent_id_list;
    public String[] ity_id_lst;
    public long[] cat_id_lst;
    public Timestamp[] cat_in_upd_timestamp_lst;

    //TreeNode var
    public aeTreeNode tnd;
    public long[] tnd_id_lst;
    public String[] tnd_id_lst_value;
    public Timestamp tnd_in_upd_timestamp;
    public Timestamp[] tnd_in_upd_timestamp_lst;
    public String ctb_type;
    public String list;

    //TreeNodeSearch var
    public String phrase;
    public String code;
    public String status;
    public boolean exact, all_ind;
    public String[] types;
    public Timestamp appn_from;
    public Timestamp appn_to;
    public Timestamp eff_from;
    public Timestamp eff_to;
    public Timestamp search_timestamp;
    public Hashtable searchItemParam;

    //Item var
    public String training_type;
    public aeItem itm;
    public aeItemExtension itmExtension;
    public long tnd_parent_tnd_id;
    public long tnd_id;
    public long itm_tpl_id;
    public long wrk_tpl_id;
    public long app_tpl_id;
    public String tpl_type;
    public Timestamp itm_in_upd_timestamp;
    public String[] itm_mobile_ind_lst;
    public long[] itm_id_lst;
    public Timestamp[] itm_in_upd_timestamp_lst;
    public String[] itm_status_lst;
    public long cur_tpl_id;
    public long plan_id;
    public String ity_id;
    public String tvw_id;
    public String[] iac_id_lst;
    public String[] target_ent_group_lst;
    public String[] comp_target_ent_group_lst;
    public String[] r_target_ent_group_lst;
    public boolean prev_version_ind;
    public String[] cm_lst;
    public boolean show_run_ind;
    public boolean show_session_ind;
    public boolean show_attendance_ind;
	public boolean show_respon_run_ind;
    public long old_itm_id;
    public int get_last;
    public boolean input_itm_apply_ind;
    public boolean input_itm_code_ind;
    public boolean input_itm_status_ind;
    public Vector vColName;
    public Vector vColType;
    public Vector vColValue;
    public Vector vExtensionColName;
    public Vector vExtensionColType;
    public Vector vExtensionColValue;
    public Vector vClobColName;
    public Vector vClobColValue;
    public Vector vFileName;
    public Timestamp rsv_upd_timestamp;
    public boolean apply_now_ind;
    public boolean adv_filter;
    public String apply_method;
    public String approval_action;
    public boolean upd_itm_content_ind;
    public boolean show_sys_msg;
    public boolean training_plan;
    public Timestamp tpn_update_timestamp;
    public Hashtable entIdRole;
    public boolean tpn_create_run_ind;
    //Item var
    public aeItemAccess iac;
    public String[] iac_acc_id_lst;
    public String[] iac_nist_lst;
    public String[] iac_ecdn_lst;
    public String[] iac_ncdn_lst;
    public String[] cost_center_group_lst;
    //批量修改课程目录
    public long[] tnd_ids_change_lst;

    //for application status change report
    public Hashtable appStatusChangeParam;

    //For search item
    public aeSearch aes;

    //For Transaction
    public aeAccountTransaction axn;
    public String acn_type;

    //Course var
    public String cos_lic_key;
    public String cos_desc;
//    public Timestamp cos_eff_start_datetime;
//    public Timestamp cos_eff_end_datetime;
    public float res_duration;

    //Code Table var
    public CodeTable ctb;
    public Timestamp ctb_in_upd_timestamp;
    public String orderBy, sortOrder;
    public String[] ctb_id_lst;
    public Timestamp[] ctb_upd_timestamp_lst;


    //Search Book_Slot var
    public Hashtable searchBook_SlotParam;

    //calendar var
    public Timestamp cal_start_datetime;
    public Timestamp cal_end_datetime;

    //navigation center var
    public int nav_num_of_mod;

    //announcement var
    public String msg_type;
    public Timestamp msg_begin_date;

    //resource content list var
    public int cal_d, cal_m, cal_y;
    public String dpo_view;
    public long res_id;
    public long tkh_id;
    public boolean qr_ind;
    public String location;
    
    // book slot
    public int[] slot_item_id;
    public Timestamp[] slot_start_time;
    public int[] slot_parent_node_id;
    public String app_ext1;
    public String app_ext3;

    //aeTemplate var
    public aeTemplate tpl;
    public String itm_type;
    public String ttp_title;
    public String tpl_view_type;
    //auto_enroll_id
    public String auto_enroll_ind;
	public String url_failure1;
	public String back_confirm;
    
    public long cos_res_id;
    /**
     * added for the application list of approver
     * Emily, 20020830
     */
    public int tpl_id;

    //var for getting navigator in ae_get_prof
    public String nav_type;
    public long nav_id;

    // var for itemRating ie itmmod
    public DbItemRating dbTargetRating;
    public DbItemRating dbActualRating;

    public long mod_id;

    public boolean flag;

    //var for system password;
    public String password;

    // for itemMote in insItem

    public Mote mote;

    // for Learning Solution
    public Vector v_soln_type;
    public Vector v_itm_type;
    public Vector v_itm_lst;
    public Vector v_period_lst;
    public long usr_ent_id;
    public long period_id;
    public long pgm_id;
    public long pgm_run_id;
    public String[] targeted_itm_apply_method_lst;
    public boolean include_targeted_itm;
    //ADD BY Tim
    public long viewer_ent_id;
    public String viewer_role;

    public float rate;
    public String rate_range_xml;
    public String rate_q_xml;

    public DbFigure dbFig;
    
    public String[] credit_type;
    public String[] credit_subtype;

    //add for att_remark
    public String[] att_remark_lst;
    public Timestamp att_update_timestamp;
    public Timestamp[] att_update_timestamp_lst;
    public long[] ict_id_list;
    public String[] icv_value_list;    
    //for item cancellation message
    public long sender_ent_id;
    public boolean cc_to_approver_ind = false;
    public String[] cc_to_approver_rol_ext_id;
    public String cc_to_approver_rol_ext_ids;
    public String bcc_to;
    
    //for JI message
    public long ji_msg_id;
    public Timestamp ji_target_datetime;
    public long ji_reminder_msg_id;
    public Timestamp ji_reminder_target_datetime;
    public String reminder_msg_subject;
    public String ji_no_change = "";
    public String reminder_no_change = "";
    
    public String ji_value="";
    
    public String reminder_value="";

    public boolean show_approval_ent_only = false;

    // for aeItemRequirement & related
    public long itrItmId;
    public long itrOrder;
	public String itrRequirementType;
	public String itrRequirementSubtype;
	public String itrRequirementRestriction;
	public Timestamp itrRequirementDueDate;
	public int itrAppnFootnoteInd;
	public String itrConditionType;
	public String itrConditionRule;
	public Timestamp itrProcExecuteTimestamp;
	public long[] reqItmId;
	public String[] reqEntLst;
	public String reqOperator;
	public Timestamp lastUpdTime;

    // for aeItemAction & related
	public String posIatType;
   	public String posIaaToAttStatus;
	public String negIatType;
   	public String negIaaToAttStatus;
    
    // fro aeItemLesson of Course
    public String ils_act_type;
    public long ils_id;
    public aeItemLesson itmLessonReq;
    public Hashtable ils_day_date_Req;
	
    public long appn_wait_count;
    public aeAppnCommHistory ach;
    
	public Timestamp att_timestamp;
    public dbUserGroup dbUsg;
    
    public long active_user;
    public long warning_user;
    public long blocking_user;
    public Timestamp gen_time;
    
    public boolean tpn_itm_run_ind;
    //for tree_frame return
    public boolean show_all = false;
    public boolean from_core5 = false;
    
    public Vector goldenManName;
    
    public long s_cmt_id;
    public String sns_type;
    public String itm_status;
    public String itm_access_type;
    public long itm_tcr_id;
    
    public aeReqParam(ServletRequest inReq, boolean bMultiPart, MultipartRequest multi) {
        this.req = inReq;
        this.bMultiPart = bMultiPart;
        this.multi = multi;

        common();
    }

    // common parameters needed in all commands
    public void common() {
        String var;
        String prmNm;
        // command
        prmNm = "cmd";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0)
            cmd = var;
        else
            cmd = null;
		prmNm = "back_confirm";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0)
		back_confirm = var;
		else
		back_confirm = null;            
        // stylesheet filename
        prmNm = "stylesheet";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0)
            stylesheet = var;
        else
            stylesheet = null;
        if (this.XSL_DISPLAY && stylesheet != null)
        	CommonLog.debug("current stylesheet:\t\t" + stylesheet);

        try {
        // url success
        prmNm = "url_success";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
           // url_success = cwUtils.getRealPath((HttpServletRequest)req, var);
            url_success = cwUtils.esc4JS(cwUtils.getRealPath((HttpServletRequest)req, cwUtils.getUrlByisPhishing(var)),true);
            
        }
        else
            url_success = null;
        }
        catch (cwException e) {
            url_success = null;
        }

        try{
        // url failure
        prmNm = "url_failure";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            url_failure = cwUtils.esc4JS(cwUtils.getRealPath((HttpServletRequest)req, cwUtils.getUrlByisPhishing(var)),true);;
        }
        else
            url_failure = null;
        }
        catch (cwException e) {
            url_failure = null;
        }
		try{
		// url failure
		prmNm = "url_failure1";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			url_failure1 = cwUtils.getRealPath((HttpServletRequest)req, var);

		}
		else
			url_failure1 = null;
		}
		catch (cwException e) {
			url_failure1 = null;
		}
        
        //auto_enroll_ind
		prmNm = "auto_enroll_ind";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0)
			auto_enroll_ind = var;
		else
			auto_enroll_ind = null;     
		
		//itm_tcr_id
		prmNm = "itm_tcr_id";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0)
			itm_tcr_id = Long.parseLong(var);
		else
			itm_tcr_id = 0;     
    }

    public void pagination(){
        cwPage = new cwPagination();

        String var;

        var = req.getParameter("cur_page");
        if (var != null && var.length() > 0) {
            cwPage.curPage = Integer.parseInt(var);
        }
        else
            cwPage.curPage = 0;

        var = req.getParameter("page_size");
        if (var != null && var.length() > 0) {
            cwPage.pageSize = Integer.parseInt(var);
        }
        else
            cwPage.pageSize = 0;

        var = req.getParameter("sort_col");
        if (var != null && var.length() > 0) {
            cwPage.sortCol = cwPagination.esc4SortSql(var);
        }

        var = req.getParameter("sort_order");
        if (var != null && var.length() > 0) {
            cwPage.sortOrder = cwPagination.esc4SortSql(var);
        }

        var = req.getParameter("timestamp");
        if (var != null && var.length() > 0) {
            cwPage.ts = Timestamp.valueOf(var);
        }
        else
            cwPage.ts = null;
    }

    public void system() {
        String var;

        var = req.getParameter("password");
        if (var != null && var.length() > 0) {
                password = var;
        }
    }


    //parameters needed in message
    public void message() {
        String var;

        var = req.getParameter("msg_type");
        if (var != null && var.length() > 0) {
                msg_type = var;
        }
    }

    public void resContentList() {
        String var;

        var = req.getParameter("res_id");
        if (var != null && var.length() > 0) {
                res_id = Long.parseLong(var);
        }

        var = req.getParameter("tkh_id");
        if (var != null && var.length() > 0) {
                tkh_id = Long.parseLong(var);
        }

        var = req.getParameter("qr_ind");
        if (var != null && var.length() > 0) {
            qr_ind = (new Boolean(var)).booleanValue();
        }else {
            qr_ind = false;
        }
        
        var = req.getParameter("location");
        if (var != null && var.length() > 0) {
                location = var;
        }

        var = req.getParameter("dpo_view");
        if (var != null && var.length() > 0) {
                dpo_view = var;
        }

        var = req.getParameter("cal_d");
        if (var != null && var.length() > 0) {
                cal_d = Integer.parseInt(var);
        }
        else
                cal_d = -100;

        var = req.getParameter("cal_m");
        if (var != null && var.length() > 0) {
                cal_m = Integer.parseInt(var);
        }
        else
                cal_m = -100;

        var = req.getParameter("cal_y");
        if (var != null && var.length() > 0) {
                cal_y = Integer.parseInt(var);
        }
        else
                cal_y = -100;
    }

    //parameters needed in getting navigator in ae_get_prof
    public void navigator() {
        String var;

        var = req.getParameter("nav_type");
        if (var != null && var.length() > 0)
            nav_type = var;
        else
            nav_type = null;

        var = req.getParameter("nav_id");
        if (var != null && var.length() > 0) {
                nav_id = Long.parseLong(var);
        }
        else
            nav_id = 0;
    }

    //parameters needed in navigator center
    public void nav_center() {
        String var;

        var = req.getParameter("nav_num_of_mod");
        if (var != null && var.length() > 0) {
                nav_num_of_mod = Integer.parseInt(var);
        }

        // message begin date
        var = req.getParameter("msg_begin_date");
        if (var != null && var.length() > 0) {
            msg_begin_date = Timestamp.valueOf(var);
        }
        else
            msg_begin_date = null;
    }

    //parameters needed in calendar
    public void calendar() {
        String var;

        // calendar start datetime
        var = req.getParameter("start_datetime");
        if (var != null && var.length() > 0) {
            cal_start_datetime = Timestamp.valueOf(var);
        }
        else
            cal_end_datetime = null;

        //code table upd timestamp
        var = req.getParameter("end_datetime");
        if (var != null && var.length() > 0) {
            cal_end_datetime = Timestamp.valueOf(var);
        }
        else
            cal_end_datetime = null;
    }


    //parameters needed in searching book slots
    public void searchBook_Slot(String clientEnc, String env_encoding)
      throws UnsupportedEncodingException, cwSysMessage {
        ctb = new CodeTable();
        String var;
        int temp;
        Boolean temp_b;
        boolean allNull=true; //indicate no input fields are input
        boolean validTime=true;  //indicate if the start/end time are valid
        searchBook_SlotParam = new Hashtable();

        var = req.getParameter("exact");
        if (var != null) {
            allNull = false;
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchBook_SlotParam.put(aeItem.EXACT, temp_b);

        //code table type
        var = req.getParameter("ctb_type");
        if (var != null)
            ctb_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("begin_tnd_title");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.BEGIN_TND_TITLE, var);

        var = req.getParameter("tnd_title");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.TND_TITLE, var);

        var = req.getParameter("itm_title");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.ITM_TITLE, var);

        var = req.getParameter("ext1");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.TND_EXT1, var);

        var = req.getParameter("ext2");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.TND_EXT2, var);

        var = req.getParameter("ext3");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.TND_EXT3, var);

        var = req.getParameter("ext5");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.TND_EXT5, var);

        var = req.getParameter("status");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.STATUS, var);

        var = req.getParameter("orderby");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.ORDERBY, var);

        var = req.getParameter("sortorder");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.SORTORDER, var);

        var = req.getParameter("child_itm_type");
        if (var != null) {
            allNull = false;
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchBook_SlotParam.put(aeItem.CHILD_ITEM_TYPE, var);

/*
        temp = 0;
        var = req.getParameter("start_week");
        if (var != null) {
            allNull = false;
            try {
                temp = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                validTime = false;
            }
        } else {
            validTime = false;
        }
        searchBook_SlotParam.put(aeItem.START_WEEK, new Integer(temp));
        if(temp<1 || temp >5)
            validTime = false;
        temp = 0;

        var = req.getParameter("start_month");
        if (var != null) {
            allNull = false;
            try {
                temp = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                validTime = false;
            }
        } else {
            validTime = false;
        }
        searchBook_SlotParam.put(aeItem.START_MONTH, new Integer(temp));
        if(temp<0 || temp >11)
            validTime = false;
        temp = 0;

        var = req.getParameter("start_year");
        if (var != null) {
            allNull = false;
            try {
                temp = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                validTime = false;
            }
        } else {
                validTime = false;
        }
        searchBook_SlotParam.put(aeItem.START_YEAR, new Integer(temp));
        if(temp<0)
            validTime = false;
        temp = 0;

        var = req.getParameter("end_week");
        if (var != null) {
            allNull = false;
            try {
                temp = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                validTime = false;
            }
        } else {
            validTime = false;
        }
        searchBook_SlotParam.put(aeItem.END_WEEK, new Integer(temp));
        if(temp<1 || temp >5)
            validTime = false;
        temp = 0;

        var = req.getParameter("end_month");
        if (var != null) {
            allNull = false;
            try {
                temp = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                validTime = false;
            }
        } else {
            validTime = false;
        }
        searchBook_SlotParam.put(aeItem.END_MONTH, new Integer(temp));
        if(temp<0 || temp >11)
            validTime = false;
        temp = 0;

        var = req.getParameter("end_year");
        if (var != null) {
            allNull = false;
            try {
                temp = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                validTime = false;
            }
        } else {
            validTime = false;
        }
        searchBook_SlotParam.put(aeItem.END_YEAR, new Integer(temp));
        if(temp<0)
            validTime = false;
*/

        //start date
        Timestamp temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("start_date");
        if (var != null) {
            allNull = false;
            try {
                if(var.length() != 0)
                    temp_date = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                validTime = false;
            }
        } else {
            validTime = false;
        }
        searchBook_SlotParam.put(aeItem.START_DATE, temp_date);

        //end date
        temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("end_date");
        if (var != null) {
            allNull = false;
            try {
                if(var.length() != 0)
                    temp_date = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                validTime = false;
            }
        } else {
            validTime = false;
        }
        searchBook_SlotParam.put(aeItem.END_DATE, temp_date);


        temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("timestamp");
        if (var != null) {
            allNull = false;
            try {
                if(var.length() != 0) {
                    temp_date = Timestamp.valueOf(var);
                    validTime = true;  //don't need to check start_date and end_date
                }
            } catch (IllegalArgumentException e) {
            }
        }
        searchBook_SlotParam.put(aeItem.ITM_PARAM_TIMESTAMP, temp_date);

        searchBook_SlotParam.put(aeItem.ALLNULL, new Boolean(allNull));

        if(!(allNull) && !(validTime)) {
            throw new cwSysMessage(INVALID_TIME);
        }
    }

    //parameters needed in searching book slots
    public void searchItem(String clientEnc, String env_encoding)
      throws UnsupportedEncodingException, cwSysMessage {
        String var;
        Long temp_l;
        Boolean temp_b;
        searchItemParam = new Hashtable();

        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            temp_l = Long.valueOf(var);
        }
        else
            temp_l = new Long(0);
        searchItemParam.put(aeItem.TND_ID, temp_l);

        var = req.getParameter("life_status");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.ITM_LIFE_STATUS, var);

        var = req.getParameter("status");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.ITM_STATUS, var);

        var = req.getParameter("approval_status");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.ITM_APPROVAL_STATUS, var);

        var = req.getParameter("title");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.ITM_TITLE, var);

        var = req.getParameter("code");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.ITM_CODE, var);

        var = req.getParameter("title_code");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.ITM_TITLE_CODE, var);

        var = req.getParameter("r_title");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.R_ITM_TITLE, var);

        var = req.getParameter("r_code");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.R_ITM_CODE, var);

        var = req.getParameter("r_title_code");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.R_ITM_TITLE_CODE, var);

        var = req.getParameter("all_ind");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ALL_IND, temp_b);
        
        var = req.getParameter("adv_srh_ind");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ADV_SRH_IND, temp_b);

        var = req.getParameter("cat_public_ind");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.CAT_PUBLIC_IND, temp_b);

        var = req.getParameter("exact");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.EXACT, temp_b);

        var = req.getParameter("r_exact");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.R_EXACT, temp_b);

        var = req.getParameter("filter_retire");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.FILTER_RETIRE, temp_b);

        var = req.getParameter("filter_retire_or_in_process_att");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ITM_FILTER_RETIRE_OR_EXIST_IN_PROGRESS_ATTENDANCE, temp_b);
        
        var = req.getParameter("in_process_att");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ITM_EXIST_IN_PROGRESS_ATTENDANCE, temp_b);        

        var = req.getParameter("show_no_run");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(true);
        searchItemParam.put(aeItem.ITM_SHOW_NO_RUN_PARENT, temp_b);
        
        var = req.getParameter("show_run_only");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(true);
        searchItemParam.put(aeItem.ITM_SHOW_RUN_ONLY, temp_b);


        var = req.getParameter("allow_null_datetime");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ITM_ALLOW_NULL_DATETIME, temp_b);

        var = req.getParameter("show_run_ind");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ITM_SHOW_RUN_IND, temp_b);

        var = req.getParameter("show_attendance");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ITM_SHOW_ATTENDANCE, temp_b);

        var = req.getParameter("show_orphan");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(true);
        searchItemParam.put(aeItem.SHOW_ORPHAN, temp_b);

        var = req.getParameter("show_off_itm");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(true);
        searchItemParam.put(aeItem.SHOW_OFF_ITM, temp_b);

        var = req.getParameter("show_pre_approve_itm");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(true);
        searchItemParam.put(aeItem.SHOW_PRE_APPROVE_ITM, temp_b);

        var = req.getParameter("appn_from_flag");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_APPN_FROM_FLAG, var);
        }

        var = req.getParameter("appn_to_flag");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_APPN_TO_FLAG, var);
        }

        var = req.getParameter("eff_from_flag");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_EFF_FROM_FLAG, var);
        }

        var = req.getParameter("eff_to_flag");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_EFF_TO_FLAG, var);
        }

        Timestamp temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("appn_from");
        if (var != null) {
            try {
                if(var.length() != 0) {
                    if(var.equalsIgnoreCase(aeItem.NOW)) {
                        temp_date = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                    }
                    else {
                        temp_date = Timestamp.valueOf(var);
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
        searchItemParam.put(aeItem.ITM_APPN_FROM, temp_date);

        temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("appn_to");
        if (var != null) {
            try {
                if(var.length() != 0) {
                    if(var.equalsIgnoreCase(aeItem.NOW)) {
                        temp_date = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                    }
                    else {
                        temp_date = Timestamp.valueOf(var);
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
        searchItemParam.put(aeItem.ITM_APPN_TO, temp_date);

        temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("eff_from");
        if (var != null) {
            try {
                if(var.length() != 0) {
                    if(var.equalsIgnoreCase(aeItem.NOW)) {
                        temp_date = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                    }
                    else {
                        temp_date = Timestamp.valueOf(var);
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
        searchItemParam.put(aeItem.ITM_EFF_FROM, temp_date);

        temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("eff_to");
        if (var != null) {
            try {
                if(var.length() != 0) {
                    if(var.equalsIgnoreCase(aeItem.NOW)) {
                        temp_date = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                    }
                    else {
                        temp_date = Timestamp.valueOf(var);
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
        searchItemParam.put(aeItem.ITM_EFF_TO, temp_date);

        temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("timestamp");
        if (var != null) {
            try {
                if(var.length() != 0) {
                    if(var.equalsIgnoreCase(aeItem.NOW)) {
                        temp_date = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                    }
                    else {
                        temp_date = Timestamp.valueOf(var);
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
        searchItemParam.put(aeItem.ITM_PARAM_TIMESTAMP, temp_date);

        String var_lst = req.getParameter("type");
        String[] types;
        if (var_lst != null && var_lst.length() > 0) {
                types = split(var_lst, "#");
                searchItemParam.put(aeItem.ITM_TYPES, types);
        }

        var = req.getParameter("appn_from_operator");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_APPN_FROM_OPERATOR, var);
        }
        var = req.getParameter("appn_to_operator");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_APPN_TO_OPERATOR, var);
        }
        var = req.getParameter("eff_from_operator");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_EFF_FROM_OPERATOR, var);
        }
        var = req.getParameter("eff_to_operator");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_EFF_TO_OPERATOR, var);
        }

        var = req.getParameter("orderby");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.ORDERBY, var);

        var = req.getParameter("sortorder");
        if (var != null) {
            var = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }
        else
            var = "";
        searchItemParam.put(aeItem.SORTORDER, var);

        var = req.getParameter("page");
        if (var != null) {
            temp_l = Long.valueOf(var);
        }
        else
            temp_l = new Long(0);
        searchItemParam.put(aeItem.PAGE, temp_l);

        var = req.getParameter("page_size");
        if (var != null) {
            temp_l = Long.valueOf(var);
        }
        else
            temp_l = new Long(0);
        searchItemParam.put(aeItem.PAGE_SIZE, temp_l);

        var = req.getParameter("comp_target_ent_group_lst");
        if ( var!= null) {
            comp_target_ent_group_lst = split(var, "~");
            searchItemParam.put(aeItem.ITM_COMP_TARGET_ENT_GROUP, comp_target_ent_group_lst);
        }

        //target entity group id list for run e.g. 1,2,3~22,33,44~123
        var = req.getParameter("r_target_ent_group_lst");
        if ( var!= null) {
            r_target_ent_group_lst = split(var, "~");
            searchItemParam.put(aeItem.ITM_R_TARGET_ENT_GROUP, r_target_ent_group_lst);
        }
        
        //skill id list e.g. 256~128
        var = req.getParameter("skill_id_lst");
        if ( var!= null) {
            searchItemParam.put(aeItem.ITM_SKILL_ID_VECTOR, cwUtils.splitToVec(var, "~"));
        }

        long[] temp_longArray;
        var = req.getParameter("tnd_id_lst");
        if (var != null) {
            temp_longArray = String2long(split(var, "~"));
        }
        else
            temp_longArray = new long[0];
        searchItemParam.put(aeItem.TND_ID_LIST, temp_longArray);

        var = req.getParameter("tcr_id_lst");
        if (var != null) {
            temp_longArray = String2long(split(var, "~"));
        }
        else
            temp_longArray = new long[0];
        searchItemParam.put(aeItem.TCR_ID_LIST, temp_longArray);

        var = req.getParameter("r_tcr_id_lst");
        if (var != null) {
            temp_longArray = String2long(split(var, "~"));
        }
        else
            temp_longArray = new long[0];
        searchItemParam.put(aeItem.R_TCR_ID_LIST, temp_longArray);

        var = req.getParameter("show_respon");
        if (var != null) {
            temp_b = Boolean.valueOf(var);
        }
        else
            temp_b = new Boolean(false);
        searchItemParam.put(aeItem.ITM_SHOW_RESPON, temp_b);

        var = req.getParameter("tvw_id");
        if (var != null) {
            searchItemParam.put(aeItem.ITM_LIST_VIEW_ID, var);
        }

        var = req.getParameter("itm_life_status_equal_lst");
        if (var != null && var.length() > 0) {
            searchItemParam.put(aeItem.ITM_LIFE_STATUS_EQUAL_LST, split(var, "~"));
        }

        var = req.getParameter("itm_life_status_not_equal_lst");
        if (var != null && var.length() > 0) {
            searchItemParam.put(aeItem.ITM_LIFE_STATUS_NOT_EQUAL_LST, split(var, "~"));
        }
        
        var = req.getParameter("itm_only_open_enrol_now");
        if( var != null ) {
            temp_b = Boolean.valueOf(var);
        } else {
            temp_b = new Boolean(false);
        }
        searchItemParam.put(aeItem.ITM_ONLY_OPEN_ENROL_NOW, temp_b);
        
        var = req.getParameter("itm_only_open_enrol_quota_now");
        if( var != null ) {
            temp_b = Boolean.valueOf(var);
        } else {
            temp_b = new Boolean(false);
        }
        searchItemParam.put(aeItem.ITM_ONLY_OPEN_ENROL_QUOTA_NOW, temp_b);
        
        var = req.getParameter("training_type");
        if (var != null && var.length() > 0) {
        	searchItemParam.put(aeItem.TRAINING_TYPE, var);
        }
        var = req.getParameter("dummy_type");
        if (var != null && var.length() > 0) {
        	searchItemParam.put(aeItem.DUMMY_TYPE, var);
        }

        var = req.getParameter("training_plan");
        if( var != null ) {
            temp_b = Boolean.valueOf(var);
        } else {
            temp_b = new Boolean(false);
        }
        searchItemParam.put(aeItem.TRAINING_PLAN, temp_b);
        
        var = req.getParameter("plan_id");
        long plan_id=0;
        if( var != null ) {
        	plan_id =Long.parseLong(var);
        }
        searchItemParam.put(aeItem.PLAN_ID, new Long(plan_id));
        
        var = req.getParameter("tcr_id");
        long tpn_tcr_id=0;
        if( var != null ) {
        	tpn_tcr_id =Long.parseLong(var);
        }
        searchItemParam.put(aeItem.TPN_TCR_ID, new Long(tpn_tcr_id));
        
        
        temp_date=aeUtils.EMPTY_DATE;
        var = req.getParameter("tpn_update_timestamp");
        if (var != null) {
            try {
                if(var.length() != 0) {
                    if(var.equalsIgnoreCase(aeItem.NOW)) {
                        temp_date = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                    }
                    else {
                        temp_date = Timestamp.valueOf(var);
                    }
                }
            } catch (IllegalArgumentException e) {
            }
        }
        searchItemParam.put(aeItem.TPN_UPDATE_TIMESTAMP, temp_date);
        
        var =req.getParameter("entrance");
        if (var != null && var.length()>0) {
        	searchItemParam.put(aeItem.TPN_ENTRANCE, var);
        }
        
    }


    //parameters needed in code table related commands
    public void codeTable(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        ctb = new CodeTable();
        String var;

        //for code table search
        var = req.getParameter("exact");
        if (var != null && var.length() > 0) {
            exact = Boolean.valueOf(var).booleanValue();
        } else {
            exact = false;
        }

        //code table type
        var = req.getParameter("ctb_type");
        if (var != null)
            ctb.ctb_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table id
        var = req.getParameter("ctb_id");
        if (var != null)
            ctb.ctb_id = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table id list
        var = req.getParameter("ctb_id_lst");
        if ( var!= null && var.length()!= 0 )
            ctb_id_lst = split(var, "~");
        else
            ctb_id_lst = new String[0];

        //code table title
        var = req.getParameter("ctb_title");
        if (var != null)
            ctb.ctb_title = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table title
        var = req.getParameter("ctb_xml");
        if (var != null && var.length() > 0)
            ctb.ctb_xml = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table upd timestamp
        var = req.getParameter("ctb_upd_timestamp");
        if (var != null && var.length() > 0) {
            ctb_in_upd_timestamp = Timestamp.valueOf(var);
        }
        else
            ctb_in_upd_timestamp = null;

        //code table upd timestamp list
        var = req.getParameter("ctb_upd_timestamp_lst");
        if ( var!= null && var.length()!= 0 )
            ctb_upd_timestamp_lst = String2Timestamp(split(var, "~"));
        else
            ctb_upd_timestamp_lst = new Timestamp[0];

        //code table look up order
        var = req.getParameter("orderby");
        if (var != null && var.length() > 0)
            orderBy = var;

        //code table look up sort order
        var = req.getParameter("sortorder");
        if (var != null && var.length() > 0)
            sortOrder = var;

        var = req.getParameter("search_timestamp");
        if (var != null && var.length() > 0)
            search_timestamp = Timestamp.valueOf(var);
        else
            search_timestamp = null;

        var = req.getParameter("page");
        if (var != null && var.length() > 0) {
            try {
                page = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                page = 0;
            }
        } else {
            page = 0;
        }

    }

    //parameters needed in aeTemplate(only ae_get_tpl will call)
    public void template() {
        tpl = new aeTemplate();
        String var;

        var = req.getParameter("itm_type");
        if (var != null && var.length() > 0) {
                itm_type = var;
        }
        else
            itm_type = "";

        var = req.getParameter("ttp_title");
        if (var != null && var.length() > 0) {
                ttp_title = var;
        }
        else
            ttp_title = "";

        var = req.getParameter("orderby");
        if (var != null && var.length() > 0)
            orderBy = var;
        else
            orderBy = "";

        var = req.getParameter("sortorder");
        if (var != null && var.length() > 0)
            sortOrder = var;
        else
            sortOrder = "";

    }

    //parameters needed in item access related commands
    public void itemAccess() {
        iac = new aeItemAccess();

        String var;

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
                iac.iac_itm_id = Long.parseLong(var);
        }

        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
                tnd_id = Long.parseLong(var);
        }

        var = req.getParameter("access_type");
        if (var != null && var.length() > 0) {
                iac.iac_access_type = var;
        }

        var = req.getParameter("access_id");
        if (var != null && var.length() > 0) {
                iac.iac_access_id = var;
        }

        var = req.getParameter("acc_id_lst");
        if (var != null && var.length() > 0) {
                iac_acc_id_lst = split(var, "~");
        }


        var = req.getParameter("nist_lst");
        if (var != null && var.length() > 0) {
                iac_nist_lst = split(var, "~");
        }

        var = req.getParameter("ecdn_lst");
        if (var != null && var.length() > 0) {
                iac_ecdn_lst = split(var, "~");
        }

        var = req.getParameter("ncdn_lst");
        if (var != null && var.length() > 0) {
                iac_ncdn_lst = split(var, "~");
        }

    }

    public void updItmRsv() {

        //get aeItem column values
        this.vClobColName = new Vector();
        this.vClobColValue = new Vector();
        this.vColName = new Vector();
        this.vColType = new Vector();
        this.vColValue = new Vector();
        String prmNm;
        String var;
        itm = new aeItem();

        //item id
        prmNm = "itm_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try {
                itm.itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm.itm_id = 0;
            }
        } else {
            itm.itm_id = 0;
        }

        //item reservation id
        prmNm = "rsv_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itm.itm_rsv_id = Long.parseLong(var);
            this.vColName.addElement("itm_rsv_id");
            this.vColType.addElement(DbTable.COL_TYPE_LONG);
            this.vColValue.addElement(new Long(itm.itm_rsv_id));
        }

        //item reservation start datetime
        /*
        prmNm = "rsv_start_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_eff_start_datetime = Timestamp.valueOf(var);
            else
                itm.itm_eff_start_datetime = null;
            
            this.vColName.addElement("itm_eff_start_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_eff_start_datetime);
        }

        //item reservation end datetime
        prmNm = "rsv_end_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            if (var.length() > 0)
                itm.itm_eff_end_datetime = Timestamp.valueOf(var);
            else
                itm.itm_eff_end_datetime = null;
            
            this.vColName.addElement("itm_eff_end_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_eff_end_datetime);
        }
        */
    }

    public void get_item_plan() {
    	itm = new aeItem();
        String prmNm;
        String var;
        prmNm = "training_plan";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	training_plan = (new Boolean(var)).booleanValue();
        }
        
        prmNm = "training_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	training_type = var;
        }
        
        prmNm = "tpn_update_timestamp";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	tpn_update_timestamp = Timestamp.valueOf(var);
        }
        
        prmNm = "tpn_create_run_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	tpn_create_run_ind = (new Boolean(var)).booleanValue();
        }
        prmNm = "tnd_parent_tnd_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("tnd_parent_tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd_parent_tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd_parent_tnd_id = 0;
            }
        } else {
            tnd_parent_tnd_id = 0;
        }
        
    }
    boolean ji_view = false;
    Vector fgt_id_vec;
    Vector fig_val_vec;
    //parameters needed in treenode related commands
    public void item(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        itm = new aeItem();
        itmExtension = new aeItemExtension();
        
        String var;
        String prmNm;
        
        var = req.getParameter("itm_content_def");
        if (var != null && var.length() > 0) {
                itm.itm_content_def = var;
        }
        
        var = req.getParameter("cur_page");
        if (var != null && var.length() > 0) {
            try {
                page = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                page = 0;
            }
        } else {
            page = 0;
        }
        
        var = req.getParameter("page_size");
        if (var != null && var.length() > 0) {
            try {
                pageSize = Integer.parseInt(var);
            } catch (NumberFormatException e) {
            	pageSize = 0;
            }
        } else {
        	pageSize = 0;
        }
        
        prmNm = "usr_ent_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            try {
                usr_ent_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                usr_ent_id = 0;
            }
        } else {
            usr_ent_id = 0;
        }

        prmNm = "apply_now_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
            try{
                apply_now_ind = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
                apply_now_ind = false;
            }
        }
        
        prmNm = "adv_filter";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        adv_filter = Boolean.valueOf(var).booleanValue();
          
		prmNm = "upd_itm_content_ind";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if( var != null && var.length() > 0) {
			try{
				upd_itm_content_ind = (new Boolean(var)).booleanValue();
			}catch( ClassCastException e ) {
				upd_itm_content_ind = false;
			}
		}

        //for ins ji, get paretn detail
        prmNm = "ji_view";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
            try{
                ji_view = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
                ji_view = false;
            }
        }

        //批量修改课程目录
        prmNm = "tnd_ids_change_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_id_lst");
        if ( var!= null && var.length()!= 0 )
        	tnd_ids_change_lst = String2long(split(var, "~"));
        else
        	tnd_ids_change_lst = new long[0];
        
        
        //item id list
        prmNm = "itm_id_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_id_lst");
        if ( var!= null && var.length()!= 0 )
            itm_id_lst = String2long(split(var, "~"));
        else
            itm_id_lst = new long[0];


        //item tree node parent id
        prmNm = "tnd_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd_id = 0;
            }
        } else {
            tnd_id = 0;
        }

        //item tree node parent id
        prmNm = "tnd_parent_tnd_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("tnd_parent_tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd_parent_tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd_parent_tnd_id = 0;
            }
        } else {
            tnd_parent_tnd_id = 0;
        }

        //current template id selected
        prmNm = "cur_tpl_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("cur_tpl_id");
        if (var != null && var.length() > 0) {
            try {
                cur_tpl_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                cur_tpl_id = 0;
            }
        } else {
            cur_tpl_id = 0;
        }

        //item template id
        prmNm = "itm_tpl_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_tpl_id");
        if (var != null && var.length() > 0) {
            try {
                itm_tpl_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm_tpl_id = 0;
            }
        } else {
            itm_tpl_id = 0;
        }

        //workflow template id
        prmNm = "wrk_tpl_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("wrk_tpl_id");
        if (var != null && var.length() > 0) {
            try {
                wrk_tpl_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                wrk_tpl_id = 0;
            }
        } else {
            wrk_tpl_id = 0;
        }

        //appnform template id
        prmNm = "app_tpl_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("app_tpl_id");
        if (var != null && var.length() > 0) {
            try {
                app_tpl_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                app_tpl_id = 0;
            }
        } else {
            app_tpl_id = 0;
        }

        //template type
        prmNm = "tpl_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("tpl_type");
        if (var != null && var.length() > 0) {
            tpl_type = var;
            itm.tpl_type = var;
        } else {
            tpl_type = "";
            itm.tpl_type = "";
        }

        //item id
        prmNm = "itm_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try {
                itm.itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm.itm_id = 0;
            }
        } else {
            itm.itm_id = 0;
        }

        //old item id
        prmNm = "old_itm_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            try {
                old_itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                old_itm_id = 0;
            }
        } else {
            old_itm_id = 0;
        }

        //get aeItem column values
        this.vClobColName = new Vector();
        this.vClobColValue = new Vector();
        this.vColName = new Vector();
        this.vColType = new Vector();
        this.vColValue = new Vector();
        this.vFileName = new Vector();
        this.vExtensionColName = new Vector();
        this.vExtensionColType = new Vector();
        this.vExtensionColValue = new Vector();
        

		prmNm = "itm_id_lst";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null) {
			if (var.length() > 0) {
				itm.itm_id_lst = cwUtils.splitToLong(var, "~");
			} else {
				itm.itm_id_lst = null;
			}
		}
		

        //item capacity
        prmNm = "itm_capacity";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itm.itm_capacity = Integer.parseInt(var);
            this.vColName.addElement("itm_capacity");
            this.vColType.addElement(DbTable.COL_TYPE_LONG);
            this.vColValue.addElement(new Long(itm.itm_capacity));
        }

        //item min capacity
        prmNm = "itm_min_capacity";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itm.itm_min_capacity = Integer.parseInt(var);
            this.vColName.addElement("itm_min_capacity");
            this.vColType.addElement(DbTable.COL_TYPE_LONG);
            this.vColValue.addElement(new Long(itm.itm_min_capacity));
        }

        //item unit
        prmNm = "itm_unit";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_unit");
        if (var != null && var.length() > 0) {
//            itm.itm_unit = Integer.parseInt(var);
            itm.itm_unit = Float.parseFloat(var);
            this.vColName.addElement("itm_unit");
            this.vColType.addElement(DbTable.COL_TYPE_FLOAT);
            this.vColValue.addElement(new Float(itm.itm_unit));
        }

        //item unit
        prmNm = "itm_can_qr_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_unit");
        if (var != null && var.length() > 0) {
//            itm.itm_unit = Integer.parseInt(var);
            itm.itm_can_qr_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_can_qr_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_can_qr_ind));
        }

        //item xml
        prmNm = "itm_xml";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            String xml = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            Perl5Util perl = new Perl5Util();
            itm.itm_xml = perl.substitute("s#&(?!(amp;|gt;|lt;|\\#))#&amp;#ig", xml);
//            itm.itm_xml = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            this.vClobColName.addElement("itm_xml");
            this.vClobColValue.addElement(itm.itm_xml);
        }

        //item title
        prmNm = "itm_title";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_title");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_title = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_title = null;
            
            this.vColName.addElement("itm_title");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_title);
        }

        //item type
        prmNm = "itm_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_type");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_type = var;
            else
                itm.itm_type = null;
            
            this.vColName.addElement("itm_type");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_type);
        }

        //item type
        prmNm = "itm_dummy_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_type");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_dummy_type = var;
            else
                itm.itm_dummy_type = null;
        }

        //item code
        prmNm = "itm_code";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_code");
        if (var != null) {
            input_itm_code_ind = true;
            if (var.length() > 0)
                itm.itm_code = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_code = null;
            
            this.vColName.addElement("itm_code");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_code);
        }
        /*
        else {
            input_itm_code_ind = false;
        }
        */

        prmNm = "itm_run_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_run_ind");
        if( var != null && var.length() > 0) {
            itm.itm_run_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_run_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_run_ind));
        }
        
        prmNm = "tpn_itm_run_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_run_ind");
        if( var != null && var.length() > 0) {
            tpn_itm_run_ind = (new Boolean(var)).booleanValue();
        }
        
        prmNm = "itm_blend_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_run_ind");
        if( var != null && var.length() > 0) {
            itm.itm_blend_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_blend_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_blend_ind));
        }
        
        prmNm = "itm_exam_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_run_ind");
        if( var != null && var.length() > 0) {
            itm.itm_exam_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_exam_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_exam_ind));
        }
        
        prmNm = "itm_ref_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_run_ind");
        if( var != null && var.length() > 0) {
            itm.itm_ref_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_ref_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_ref_ind));
        }
        //item code
        prmNm = "itm_status";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null ) {
            input_itm_status_ind = true;
            if (var.length() > 0){
            	itm.itm_status = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            	if(!itm.itm_status.equalsIgnoreCase(aeItem.ITM_STATUS_OFF)) {
            		if(!itm.itm_run_ind) {
            			itm.itm_access_type = itm.itm_status;
            			this.vColName.addElement("itm_access_type");
            			this.vColType.addElement(DbTable.COL_TYPE_STRING);
            			this.vColValue.addElement(itm.itm_access_type);
            		}
        			
            		itm.itm_status = aeItem.ITM_STATUS_ON;
            	}
            }
            else
                itm.itm_status = null;
            
            this.vColName.addElement("itm_status");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_status);

        }
        
        //appn start datetime
        prmNm = "itm_appn_start_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_appn_start_datetime");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_appn_start_datetime = Timestamp.valueOf(var);
            else
                itm.itm_appn_start_datetime = null;
            
            this.vColName.addElement("itm_appn_start_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_appn_start_datetime);
        }
        
        
        prmNm = "content_eff_start_end";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null ) {
            try {
                int temp = Integer.parseInt(var);
                itm.itm_content_eff_duration = temp;
                itm.itm_content_eff_start_datetime = null;
                itm.itm_content_eff_end_datetime = null;
            } catch (NumberFormatException nfe) {
                try {
                    itm.itm_content_eff_end_datetime = Timestamp.valueOf(var);
                    itm.itm_content_eff_start_datetime = itm.itm_appn_start_datetime;//aeUtils.getTimeAfter(itm.itm_content_eff_end_datetime, Calendar.YEAR, 0);
                    itm.itm_content_eff_duration = 0;
                } catch (IllegalArgumentException iae) {
                    itm.itm_content_eff_start_datetime = null;
                    itm.itm_content_eff_end_datetime = null;
                    itm.itm_content_eff_duration = 0;
                }
            } finally {
                this.vColName.addElement("itm_content_eff_start_datetime");
                this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
                this.vColValue.addElement(itm.itm_content_eff_start_datetime);

                this.vColName.addElement("itm_content_eff_end_datetime");
                this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
                this.vColValue.addElement(itm.itm_content_eff_end_datetime);

                this.vColName.addElement("itm_content_eff_duration");
                this.vColType.addElement(DbTable.COL_TYPE_INT);
                this.vColValue.addElement(new Integer(itm.itm_content_eff_duration));
            }
        }

        //appn end datetime
        prmNm = "itm_appn_end_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_appn_end_datetime");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_appn_end_datetime = Timestamp.valueOf(var);
            else
                itm.itm_appn_end_datetime = null;
            
            this.vColName.addElement("itm_appn_end_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_appn_end_datetime);
        }

        //item update timestamp
        prmNm = "itm_upd_timestamp";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_upd_timestamp");
        if (var != null && var.length() > 0) {
            itm_in_upd_timestamp = Timestamp.valueOf(var);
            //this.vColName.addElement("itm_upd_timestamp");
            //this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            //this.vColValue.addElement(itm.itm_upd_timestamp);
        }

        //item eff start datetime
        prmNm = "itm_eff_start_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_eff_start_datetime");
        if (var != null) {
            if (var.length() > 0)
                itm.itm_eff_start_datetime = Timestamp.valueOf(var);
            else
                itm.itm_eff_start_datetime = null;
            
            this.vColName.addElement("itm_eff_start_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_eff_start_datetime);
        }

        //item eff end datetime
        prmNm = "itm_eff_end_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_eff_end_datetime");
        if (var != null) {
            if (var.length() > 0)
                itm.itm_eff_end_datetime = Timestamp.valueOf(var);
            else
                itm.itm_eff_end_datetime = null;
            
            this.vColName.addElement("itm_eff_end_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_eff_end_datetime);
        }

        //item upd timestamp list
        prmNm = "itm_upd_timestamp_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_upd_timestamp_lst");
        if ( var!= null && var.length()!= 0 )
            itm_in_upd_timestamp_lst = String2Timestamp(split(var, "~"));
        else
            itm_in_upd_timestamp_lst = new Timestamp[0];

        //item status list
        prmNm = "itm_status_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_status_lst");
        if ( var!= null && var.length()!= 0 )
            itm_status_lst = split(var, "~");
        else
            itm_status_lst = new String[0];

        //item mobile ind list default [0]
        prmNm = "itm_mobile_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if ( var!= null && var.length()!= 0 ){
        	itm_mobile_ind_lst = new String[]{var};
        }
        else{
        	itm_mobile_ind_lst = new String[]{"no"};
        }
        
        //itm fee
        prmNm = "itm_fee";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_fee");
        if (var != null && var.length() > 0) {
            itm.itm_fee = Float.parseFloat(var);
            this.vColName.addElement("itm_fee");
            this.vColType.addElement(DbTable.COL_TYPE_FLOAT);
            this.vColValue.addElement(new Float(itm.itm_fee));
        }
        //item fee ccy
        prmNm = "itm_fee_ccy";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_fee_ccy");
        if (var != null) {
            if (var.length() > 0)
                itm.itm_fee_ccy = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_fee_ccy = null;
            
            this.vColName.addElement("itm_fee_ccy");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_fee_ccy);
        }
        
        //item icon
        boolean upd_ind= false;
        prmNm = "itm_icon_del_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
        	upd_ind = (new Boolean(var)).booleanValue();
        }
        prmNm = "itm_icon";
        var = (bMultiPart) ? multi.getFilesystemName("itm_icon") : null;
        //var = req.getParameter("itm_title");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_icon = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_icon = null;
        }
        
        prmNm = "field99__select";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if("use_default_image".equalsIgnoreCase(var)){
        	prmNm = "default_image";
        	var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        	if(var != null){
        		if (var.length() > 0){
        			itm.itm_icon = var;
        		} else {
        			itm.itm_icon = null;
        		}
        	}
        }
        
        if(upd_ind){
	        this.vColName.addElement("itm_icon");
	        this.vColType.addElement(DbTable.COL_TYPE_STRING);
	        //选择默认文件夹的图片时，itm_ico会把一个文件夹名也一起带过来。把文件夹过滤掉
	        if(itm.itm_icon.indexOf("/") > -1 && itm.itm_icon.length() > itm.itm_icon.indexOf("/")+1){
	        	itm.itm_icon = itm.itm_icon.substring(itm.itm_icon.lastIndexOf("/")+1);
	        }
	        this.vColValue.addElement(itm.itm_icon);
        }
        
        //item plan code
        prmNm = "itm_plan_code";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_title");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_plan_code = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_plan_code = null;
            
            this.vColName.addElement("itm_plan_code");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_plan_code);
        }
        
        //item plan code
        prmNm = "itm_desc";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_title");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_desc = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_desc = null;
            
            this.vColName.addElement("itm_desc");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_desc);
        }
        
        
        
        
        
        prmNm = "itm_inst_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_title");
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_inst_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_inst_type = null;
            
            this.vColName.addElement("itm_inst_type");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_inst_type);
        }
        
        
        //course desc
        prmNm = "cos_desc";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0)
            cos_desc = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);

        //course license key
        prmNm = "cos_lic_key";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0)
            cos_lic_key = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);

        //ext1
        prmNm = "ext1";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("ext1");
        if (var != null && var.length() > 0)
            itm.itm_ext1 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);

        //order by of get_all_itm
        prmNm = "orderby";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("orderby");
        if (var != null) {
            orderBy = var;
        }
        else
            orderBy = "";

        prmNm = "sortorder";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("sortorder");
        if (var != null) {
            sortOrder = var;
        }
        else
            sortOrder = "";


        //item details option
        prmNm = "dtl_opt";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("dtl_opt");
        if (var != null && var.length() > 0) {
            try {
                itm.details_option = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm.details_option = 0;
            }
        } else {
            itm.details_option = 0;
        }

        // type of item access id
        prmNm = "access_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("access_type");
        if (var != null && var.length() > 0) {
            itm.acc_type = var;
        } else {
            itm.acc_type = "";
        }

        // item access id
        prmNm = "acc_id_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("acc_id_lst");
        if (var != null && var.length() > 0) {
            itm.acc_id = split(var, "~");
        } else {
            itm.acc_id = new String[0];
        }

        // type of view of the template
        prmNm = "view_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("view_type");
        if (var != null && var.length() > 0) {
            tpl_view_type = var;
        } else {
            tpl_view_type = "";
        }

        //item type
        prmNm = "ity_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("ity_id");
        if (var != null && var.length() > 0) {
            ity_id = var;
        } else {
            ity_id = "";
        }

        //template view id
        prmNm = "tvw_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("tvw_id");
        if (var != null && var.length() > 0) {
            tvw_id = var;
        } else {
            tvw_id = "";
        }

        prmNm = "itm_create_run_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_create_run_ind");
        if( var != null && var.length() > 0) {
            itm.itm_create_run_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_create_run_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_create_run_ind));
        }

        prmNm = "itm_create_session_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_create_run_ind");
        if( var != null && var.length() > 0) {
            itm.itm_create_session_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_create_session_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_create_session_ind));
        }

        prmNm = "itm_session_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_run_ind");
        if( var != null && var.length() > 0) {
            itm.itm_session_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_session_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_session_ind));
        }

        prmNm = "itm_apply_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_apply_ind");
        if( var != null && var.length() > 0) {
            itm.itm_apply_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_apply_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_apply_ind));
        }

        prmNm = "itm_qdb_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_qdb_ind");
        if( var != null && var.length() > 0) {
            itm.itm_qdb_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_qdb_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_qdb_ind));
        }

        prmNm = "itm_auto_enrol_qdb_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_qdb_ind");
        if( var != null && var.length() > 0) {
            itm.itm_auto_enrol_qdb_ind = (new Boolean(var)).booleanValue();
            this.vColName.addElement("itm_auto_enrol_qdb_ind");
            this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            this.vColValue.addElement(new Boolean(itm.itm_auto_enrol_qdb_ind));
        }

        prmNm = "itm_version_code";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null ) {
            if (var.length() > 0)
                itm.itm_version_code = var;
            else
                itm.itm_version_code = null;
            
            this.vColName.addElement("itm_version_code");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_version_code);
        }

        prmNm = "itm_person_in_charge";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null) {
            if (var.length() > 0)
                itm.itm_person_in_charge = var;
            else
                itm.itm_person_in_charge = null;
            
            this.vColName.addElement("itm_person_in_charge");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_person_in_charge);
        }

        prmNm = "itm_apply_method";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_apply_method");
        if( var != null) {
            if (var.length() > 0)
                itm.itm_apply_method = var;
            else
                itm.itm_apply_method = null;
            
            this.vColName.addElement("itm_apply_method");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_apply_method);
        }

        prmNm = "itm_life_status";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null) {
            if (var.length() > 0)
                itm.itm_life_status = var;
            else
                itm.itm_life_status = null;
            
            this.vColName.addElement("itm_life_status");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_life_status);
        }

        //online content eff start datetime
        prmNm = "itm_content_eff_start_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            if (var.length() > 0)
                itm.itm_content_eff_start_datetime = Timestamp.valueOf(var);
            else
                itm.itm_content_eff_start_datetime = null;
            
            this.vColName.addElement("itm_content_eff_start_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_content_eff_start_datetime);
        }

        prmNm = "itm_content_eff_end_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            if (var.length() > 0)
                itm.itm_content_eff_end_datetime = Timestamp.valueOf(var);
            else
                itm.itm_content_eff_end_datetime = null;
            
            this.vColName.addElement("itm_content_eff_end_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_content_eff_end_datetime);
        }

        prmNm = "cos_eff_start_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            if (var.length() > 0)
                itm.itm_content_eff_start_datetime = Timestamp.valueOf(var);
            else
                itm.itm_content_eff_start_datetime = null;
            
            this.vColName.addElement("itm_content_eff_start_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_content_eff_start_datetime);
        }

        //System.out.println("input cos_eff_start_datetime = " + var);
        //System.out.println("cos_eff_start_datetime = " + cos_eff_start_datetime);

        //online content eff end datetime
        prmNm = "cos_eff_end_datetime";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            if (var.length() > 0)
                itm.itm_content_eff_end_datetime = Timestamp.valueOf(var);
            else
                itm.itm_content_eff_end_datetime = null;
            
            this.vColName.addElement("itm_content_eff_end_datetime");
            this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            this.vColValue.addElement(itm.itm_content_eff_end_datetime);
        }

        prmNm = "itm_content_eff_duration";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            if (var.length() > 0)
                itm.itm_content_eff_duration = Integer.parseInt(var);
            else
                itm.itm_content_eff_duration = 0;
            
            this.vColName.addElement("itm_content_eff_duration");
            this.vColType.addElement(DbTable.COL_TYPE_INT);
            this.vColValue.addElement(new Integer(itm.itm_content_eff_duration));
        }

        //online content duration
        prmNm = "res_duration";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            res_duration = Float.parseFloat(var);
        }

        //catalog attachment tnd_id
        prmNm = "tnd_id_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("tnd_id_lst");
        if ( var!= null)
            tnd_id_lst = String2long(split(var, "~"));

		prmNm = "tnd_id_lst_value";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		//var = req.getParameter("tnd_id_lst");
		if ( var!= null)
			tnd_id_lst_value = split(dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart), "~");

        //target entity group id list e.g. 1,2,3~22,33,44~123
        prmNm = "target_ent_group_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if ( var!= null)
            target_ent_group_lst = split(var, "~");

        prmNm = "comp_target_ent_group_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if ( var!= null)
            comp_target_ent_group_lst = split(var, "~");

       

        //item access list e.g. CMAN~128~256
        prmNm = "iac_id_lst";
        iac_id_lst = (bMultiPart) ? multi.getParameterValues(prmNm) : req.getParameterValues(prmNm);

        //competency level list e.g. 256,1~128,1
        prmNm = "cm_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if ( var!= null)
            cm_lst = split(var, "~");

        var = req.getParameter("prev_version_ind");
        if (var != null) {
            prev_version_ind = Boolean.valueOf(var).booleanValue();
        }
        else
            prev_version_ind = false;

        prmNm = "show_run_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_create_run_ind");
        if( var != null && var.length() > 0) {
            try{
                show_run_ind = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
                show_run_ind = false;
            }
        }
        prmNm = "show_session_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_create_run_ind");
        if( var != null && var.length() > 0) {
            try{
                show_session_ind = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
                show_session_ind = false;
            }
        }
        prmNm = "show_attendance_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("itm_create_run_ind");
        if( var != null && var.length() > 0) {
            try{
                show_attendance_ind = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
                show_attendance_ind = false;
            }
        }
		prmNm = "show_respon_run_ind";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		//var = req.getParameter("itm_create_run_ind");
		if( var != null && var.length() > 0) {
			try{
				show_respon_run_ind = (new Boolean(var)).booleanValue();
			}catch( ClassCastException e ) {
				show_respon_run_ind = false;
			}
		}

        //ext1
        prmNm = "itm_ext1";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null ) {
            if (var.length() > 0)
                itm.itm_ext1 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_ext1 = null;
            
            this.vColName.addElement("itm_ext1");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_ext1);
        }
        //item cancellation reason
        prmNm = "itm_cancellation_reason";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            if (var.length() > 0)
                itm.itm_cancellation_reason = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
            else
                itm.itm_cancellation_reason = null;
            
            this.vColName.addElement("itm_cancellation_reason");
            this.vColType.addElement(DbTable.COL_TYPE_STRING);
            this.vColValue.addElement(itm.itm_cancellation_reason);
        }
        //item cancel type
        prmNm = "rsv_cancel_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itm.itm_cancellation_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
        }
        
         

        //item cancel message sender
        prmNm = "sender_ent_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.sender_ent_id = Integer.parseInt(var);
        }
        //item cancel message -- cc to approver indicator
        prmNm = "cc_to_approver_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(var.equals("true"))
                this.cc_to_approver_ind = true;
        }
        //item cancel message approver rol_ext_id into array
        prmNm = "cc_to_approver_rol_ext_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.cc_to_approver_rol_ext_id = split(var, "~");
        }
        
        //item cancel message approver rol_ext_ids
        prmNm = "cc_to_approver_rol_ext_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.cc_to_approver_rol_ext_ids = var;
        }
        
        //item cancel message bcc list
        prmNm = "bcc_to";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            this.bcc_to = var;
        }
        
        //rsv update timestamp
        prmNm = "rsv_upd_timestamp";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            rsv_upd_timestamp = Timestamp.valueOf(var);
        }

        //get lastest version indicator
        prmNm = "get_last";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            try {
                get_last = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                get_last = 0;
            }
        } else {
            get_last = 0;
        }


        //message subject of item cancellation
        msg_subject = null;
        prmNm = "msg_subject";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            msg_subject = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
        }

        // cost center usergroup list e.g. 1~2~3~4
        prmNm = "cost_center_group_lst";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var!= null)
            cost_center_group_lst = split(var, "~");
            
        prmNm = "apply_method";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        //var = req.getParameter("tpl_type");
        if (var != null && var.length() > 0) {
            apply_method = var;
        } else {
            apply_method = "";
        }
        
        prmNm = "itm_retake_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0 ){
			itm.itm_retake_ind = Boolean.valueOf(var).booleanValue();
			this.vColName.addElement("itm_retake_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_retake_ind));
        }
        
        fgt_id_vec = new Vector();
        prmNm = "fgt_id_list";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);        
        if( var != null && var.length() != 0 )
            fgt_id_vec = cwUtils.splitToVec(var, "~");

        fig_val_vec = new Vector();
        prmNm = "fig_val_list";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);        
        Vector vec = new Vector();
        if( var != null && var.length() != 0 )
            vec = cwUtils.splitToVecString(var, "~");
        for(int i=0; i<vec.size(); i++)
            fig_val_vec.addElement(new Float((String)vec.elementAt(i)));
            
        prmNm = "approval_action";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0 ){
            approval_action = var;
        }

        //item application approval type
        prmNm = "itm_app_approval_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itm.itm_app_approval_type = var;
        } else {
            itm.itm_app_approval_type = null;
        }

        //itm_tcr_id
        prmNm = "itm_tcr_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            try {
                itm.itm_tcr_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm.itm_tcr_id = 0;
            }
        } else {
            itm.itm_tcr_id = 0;
        }
        
        prmNm = "itm_send_enroll_email_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0 ){
        	//此项逻辑用于处理LearnNow Enhancement 20180307.docx 第1点功能的旧数据
        	if("true".equals(var)){
        		itm.itm_send_enroll_email_ind = 1;
        	} else if("false".equals(var)){
        		itm.itm_send_enroll_email_ind = 0;
        	} else {        		
        		itm.itm_send_enroll_email_ind = Long.parseLong(var);
        	}        	
			this.vColName.addElement("itm_send_enroll_email_ind");
			this.vColType.addElement(DbTable.COL_TYPE_LONG);
			this.vColValue.addElement(itm.itm_send_enroll_email_ind);
        }
        
        prmNm = "itm_enroll_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null){
        	if (var.length() == 0)
        		var = null;
			itm.itm_enroll_type = var;
			this.vColName.addElement("itm_enroll_type");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_enroll_type);
        }
        prmNm = "itm_access_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0 ){
			itm.itm_access_type = var;
			this.vColName.addElement("itm_access_type");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_access_type);
        }
        
        prmNm = "itm_mark_buffer_day";
        itm.itm_mark_buffer_day = 0;
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itm.itm_mark_buffer_day = Integer.parseInt(var);
        }
        this.vColName.addElement("itm_mark_buffer_day");
        this.vColType.addElement(DbTable.COL_TYPE_INT);
        this.vColValue.addElement(new Integer(itm.itm_mark_buffer_day));
		
		prmNm = "itm_notify_days";
		itm.itm_notify_days = 0;
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itm.itm_notify_days = Integer.parseInt(var);
		}
		this.vColName.addElement("itm_notify_days");
		this.vColType.addElement(DbTable.COL_TYPE_INT);
		this.vColValue.addElement(new Integer(itm.itm_notify_days));
		
		prmNm = "itm_notify_email";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if( var != null){
			itm.itm_notify_email = var;
			this.vColName.addElement("itm_notify_email");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_notify_email);
		}

        prmNm = "waitlst_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
            itm.itm_not_allow_waitlist_ind = (new Boolean(var)).booleanValue();
			this.vColName.addElement("itm_not_allow_waitlist_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_not_allow_waitlist_ind));
        }
        
        prmNm = "show_sys_msg";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
            show_sys_msg = (new Boolean(var)).booleanValue();
        }
        
        prmNm = "plan_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            try {
            	plan_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
            	plan_id = 0;
            }
        } else {
        	plan_id = 0;
        }
        
        

        
        
        prmNm = "training_plan";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	training_plan = (new Boolean(var)).booleanValue();
        }
        
        prmNm = "training_type";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	training_type = var;
        }
        
        prmNm = "tpn_update_timestamp";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	tpn_update_timestamp = Timestamp.valueOf(var);
        }
        
        prmNm = "tpn_create_run_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	tpn_create_run_ind = (new Boolean(var)).booleanValue();
        }
        
        prmNm = "file1_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file2_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file3_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file4_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file5_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file001_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file002_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file003_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file004_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file005_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file006_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file007_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file008_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file009_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "file010_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        prmNm = "itm_icon_name";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if( var != null && var.length() > 0) {
        	this.vFileName.addElement(var);
        }
        
        //itm_bonus_ind
        prmNm = "itm_bonus_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
        	if (var.length() > 0) {
        		itm.itm_bonus_ind =  (new Boolean(var)).booleanValue();
        	} else {
        		itm.itm_bonus_ind = false;
        	}
        	this.vColName.addElement("itm_bonus_ind");
        	this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
        	this.vColValue.addElement(new Boolean(itm.itm_bonus_ind));
        }
        
        //itm_diff_factor
        prmNm = "itm_diff_factor";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itm.itm_diff_factor = Float.parseFloat(var);
            this.vColName.addElement("itm_diff_factor");
            this.vColType.addElement(DbTable.COL_TYPE_FLOAT);
            this.vColValue.addElement(new Float(itm.itm_diff_factor));
        }
        
        prmNm = "itm_integrated_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
        	if (var.length() > 0) {
        		itm.itm_integrated_ind =  (new Boolean(var)).booleanValue();
        	} else {
        		itm.itm_integrated_ind = false;
        	}
        	this.vColName.addElement("itm_integrated_ind");
        	this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
        	this.vColValue.addElement(new Boolean(itm.itm_integrated_ind));
        }
        
        prmNm = "itm_share_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null) {
            itm.itm_share_ind = Boolean.valueOf(var).booleanValue(); 
        }
        
        prmNm = "itm_cfc_id";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            try {
                itm.itm_cfc_id = Long.parseLong(var);
                this.vColName.addElement("itm_cfc_id");
                this.vColType.addElement(DbTable.COL_TYPE_LONG);
                this.vColValue.addElement(new Long(itm.itm_cfc_id));
            } catch (NumberFormatException e) {
                itm.itm_cfc_id = 0;
                this.vColName.addElement("itm_cfc_id");
                this.vColType.addElement(DbTable.COL_TYPE_LONG);
                this.vColValue.addElement(new Long(itm.itm_cfc_id));
            }
        } else {
            itm.itm_cfc_id = 0;
            this.vColName.addElement("itm_cfc_id");
            this.vColType.addElement(DbTable.COL_TYPE_LONG);
            this.vColValue.addElement(new Long(itm.itm_cfc_id));
        }

		// aeItemExtension
		prmNm = "ies_lang";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_lang = var;
		}
		this.vExtensionColName.addElement("ies_lang");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_lang);

		prmNm = "ies_objective";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_objective = var;
		}
		this.vExtensionColName.addElement("ies_objective");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_objective);

		prmNm = "ies_contents";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_contents = var;
		}
		this.vExtensionColName.addElement("ies_contents");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_contents);

		prmNm = "ies_duration";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_duration = var;
		}
		this.vExtensionColName.addElement("ies_duration");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_duration);

		prmNm = "ies_audience";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_audience = var;
		}
		this.vExtensionColName.addElement("ies_audience");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_audience);

		prmNm = "ies_prerequisites";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_prerequisites = var;
		}
		this.vExtensionColName.addElement("ies_prerequisites");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_prerequisites);

		prmNm = "ies_exemptions";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_exemptions = var;
		}
		this.vExtensionColName.addElement("ies_exemptions");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_exemptions);

		prmNm = "ies_remarks";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_remarks = var;
		}
		this.vExtensionColName.addElement("ies_remarks");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_remarks);

		prmNm = "ies_enroll_confirm_remarks";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_enroll_confirm_remarks = var;
		}
		this.vExtensionColName.addElement("ies_enroll_confirm_remarks");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_enroll_confirm_remarks);

		prmNm = "ies_schedule";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_schedule = var;
		}
		this.vExtensionColName.addElement("ies_schedule");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_schedule);

		prmNm = "url1";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_itm_ref_url_1 = var;
		}
		this.vExtensionColName.addElement("ies_itm_ref_url_1");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_1);

		prmNm = "url2";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_itm_ref_url_2 = var;
		}
		this.vExtensionColName.addElement("ies_itm_ref_url_2");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_2);
		
		prmNm = "url3";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_itm_ref_url_3 = var;
		}
		this.vExtensionColName.addElement("ies_itm_ref_url_3");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_3);

		prmNm = "url4";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_itm_ref_url_4 = var;
		}
		this.vExtensionColName.addElement("ies_itm_ref_url_4");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_4);

		prmNm = "url5";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			itmExtension.ies_itm_ref_url_5 = var;
		}
		this.vExtensionColName.addElement("ies_itm_ref_url_5");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
		this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_5);

		if (isUploadFileModify("file1_name", "file1", clientEnc, env_encoding)) {
			itmExtension.ies_itm_ref_materials_1 = getUploadFileParam("file1_name", "file1", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_ref_materials_1");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_1);
		}

		if (isUploadFileModify("file2_name", "file2", clientEnc, env_encoding)) {
			itmExtension.ies_itm_ref_materials_2 = getUploadFileParam("file2_name", "file2", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_ref_materials_2");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_2);
		}

		if (isUploadFileModify("file3_name", "file3", clientEnc, env_encoding)) {
			itmExtension.ies_itm_ref_materials_3 = getUploadFileParam("file3_name", "file3", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_ref_materials_3");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_3);
		}

		if (isUploadFileModify("file4_name", "file4", clientEnc, env_encoding)) {
			itmExtension.ies_itm_ref_materials_4 = getUploadFileParam("file4_name", "file4", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_ref_materials_4");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_4);
		}

		if (isUploadFileModify("file5_name", "file5", clientEnc, env_encoding)) {
			itmExtension.ies_itm_ref_materials_5 = getUploadFileParam("file5_name", "file5", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_ref_materials_5");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_5);
		}

		if (isUploadFileModify("file001_name", "file001", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_1 = getUploadFileParam("file001_name", "file001", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_1");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_1);
		}

		if (isUploadFileModify("file002_name", "file002", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_2 = getUploadFileParam("file002_name", "file002", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_2");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_2);
		}

		if (isUploadFileModify("file003_name", "file003", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_3 = getUploadFileParam("file003_name", "file003", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_3");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_3);
		}

		if (isUploadFileModify("file004_name", "file004", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_4 = getUploadFileParam("file004_name", "file004", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_4");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_4);
		}

		if (isUploadFileModify("file005_name", "file005", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_5 = getUploadFileParam("file005_name", "file005", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_5");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_5);
		}

		if (isUploadFileModify("file006_name", "file006", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_6 = getUploadFileParam("file006_name", "file006", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_6");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_6);
		}

		if (isUploadFileModify("file007_name", "file007", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_7 = getUploadFileParam("file007_name", "file007", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_7");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_7);
		}

		if (isUploadFileModify("file008_name", "file008", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_8 = getUploadFileParam("file008_name", "file008", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_8");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_8);
		}

		if (isUploadFileModify("file009_name", "file009", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_9 = getUploadFileParam("file009_name", "file009", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_9");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_9);
		}

		if (isUploadFileModify("file010_name", "file010", clientEnc, env_encoding)) {
			itmExtension.ies_itm_rel_materials_10 = getUploadFileParam("file010_name", "file010", clientEnc, env_encoding);
			this.vExtensionColName.addElement("ies_itm_rel_materials_10");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_10);
		}

		prmNm = "ies_top_ind";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null && var.length() > 0) {
			try {
				itmExtension.ies_top_ind = (new Boolean(var)).booleanValue();
			} catch (ClassCastException e) {
				itmExtension.ies_top_ind = false;
			}
		}
		this.vExtensionColName.addElement("ies_top_ind");
		this.vExtensionColType.addElement(DbTable.COL_TYPE_BOOLEAN);
		this.vExtensionColValue.addElement(itmExtension.ies_top_ind);
		
		boolean ies_top_icon_upd_ind = false;
		prmNm = "ies_top_icon_del_ind";
		var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
		if (var != null) {
			ies_top_icon_upd_ind = (new Boolean(var)).booleanValue();
		}

		prmNm = "ies_top_icon";
		var = (bMultiPart) ? multi.getFilesystemName("ies_top_icon") : null;
		if (var != null) {
			if (var.length() > 0)
				itmExtension.ies_top_icon = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
			else
				itmExtension.ies_top_icon = null;
		}
		if (ies_top_icon_upd_ind) {
			this.vExtensionColName.addElement("ies_top_icon");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_top_icon);
		}
		
		prmNm = "ies_credit";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            itmExtension.ies_credit = Float.parseFloat(var);            
        }
        if(itmExtension.ies_credit!=0){
            itm.itm_diff_factor = itmExtension.ies_credit;
        }
        this.vExtensionColName.addElement("ies_credit");
        this.vExtensionColType.addElement(DbTable.COL_TYPE_FLOAT);
        this.vExtensionColValue.addElement(itmExtension.ies_credit);
        
        prmNm = "is_complete_del";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
        	itm.is_complete_del = Boolean.valueOf(var).booleanValue();
        } else {
        	itm.is_complete_del = false;
        }
        
        prmNm = "is_new_cos";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
        	itm.is_new_cos = Boolean.valueOf(var).booleanValue();
        } else {
        	itm.is_new_cos = false;
        }
        
        /*this.vColName.addElement("itm_plan_code");
        this.vColType.addElement(DbTable.COL_TYPE_STRING);
        this.vColValue.addElement(Long.toString(plan_id));*/

	}

	private String getUploadFileParam(String delKey, String key, String clientEnc, String env_encoding) throws UnsupportedEncodingException {
		String result = null;
		String var = (bMultiPart) ? multi.getParameter(key) : req.getParameter(key);
		if (var != null && var.length() > 0) {
			result = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
		}
		return result;
	}

	private boolean isUploadFileModify(String delKey, String key, String clientEnc, String env_encoding) throws UnsupportedEncodingException {
		boolean result = false;

		String var = (bMultiPart) ? multi.getParameter(delKey) : req.getParameter(delKey);
		if (var != null && var.length() > 0) {
			result = true;
		} else {
			var = (bMultiPart) ? multi.getParameter(key) : req.getParameter(key);
			if (var != null && var.length() > 0) {
				result = true;
			}
		}
		return result;
    }

    //parameters needed in treenode related commands
    public void treenode(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        tnd = new aeTreeNode();
        String var;

        //user list or item list
        var = req.getParameter("list");
        if ( var!= null && var.length()!= 0 )
            list = var;
        else
            list = "";

        //item list order
        var = req.getParameter("orderby");
        if ( var!= null && var.length()!= 0 )
            orderBy = var;
        else
            orderBy = "";

        //item list sort order
        var = req.getParameter("sortorder");
        if ( var!= null && var.length()!= 0 )
            sortOrder = var;
        else
            sortOrder = "";

        //tree node id list
        var = req.getParameter("tnd_id_lst");
        if ( var!= null && var.length()!= 0 )
            tnd_id_lst = String2long(split(var, "~"));
        else
            tnd_id_lst = new long[0];

        //tree node upd timestamp list
        var = req.getParameter("tnd_upd_timestamp_lst");
        if ( var!= null && var.length()!= 0 )
            tnd_in_upd_timestamp_lst = String2Timestamp(split(var, "~"));
        else
            tnd_in_upd_timestamp_lst = new Timestamp[0];

        //tree node id
        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd.tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd.tnd_id = 0;
            }
        } else {
            tnd.tnd_id = 0;
        }

        //tree node title
        var = req.getParameter("tnd_title");
        if (var != null && var.length() > 0)
            tnd.tnd_title = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //tree node desc
        var = req.getParameter("tnd_desc");
        if (var != null && var.length() > 0)
            tnd.tnd_desc = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //tree node status
        var = req.getParameter("tnd_status");
        if (var != null && var.length() > 0)
            tnd.tnd_status = var;


        //tree node catalog id
        var = req.getParameter("tnd_cat_id");
        if (var != null && var.length() > 0) {
            try {
                tnd.tnd_cat_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd.tnd_cat_id = 0;
            }
        } else {
            tnd.tnd_cat_id = 0;
        }


        //tree node parent id
        var = req.getParameter("tnd_parent_tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd.tnd_parent_tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd.tnd_parent_tnd_id = 0;
            }
        } else {
            tnd.tnd_parent_tnd_id = 0;
        }


        //tree node link id
        var = req.getParameter("tnd_link_tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd.tnd_link_tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd.tnd_link_tnd_id = 0;
            }
        } else {
            tnd.tnd_link_tnd_id = 0;
        }


        //tree node item id
        var = req.getParameter("tnd_itm_id");
        if (var != null && var.length() > 0) {
            try {
                tnd.tnd_itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd.tnd_itm_id = 0;
            }
        } else {
            tnd.tnd_itm_id = 0;
        }


        //update timestamp
        var = req.getParameter("tnd_upd_timestamp");
        if (var != null && var.length() > 0)
            tnd_in_upd_timestamp = Timestamp.valueOf(var);
        else
            tnd_in_upd_timestamp = null;

        //ext1
        var = req.getParameter("ext1");
        if (var != null && var.length() > 0)
            tnd.tnd_ext1 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //ext2
        var = req.getParameter("ext2");
        if (var != null && var.length() > 0)
            tnd.tnd_ext2 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //ext3
        var = req.getParameter("ext3");
        if (var != null && var.length() > 0)
            tnd.tnd_ext3 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //ext4
        var = req.getParameter("ext4");
        if (var != null && var.length() > 0)
            tnd.tnd_ext4 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //ext5
        var = req.getParameter("ext5");
        if (var != null && var.length() > 0)
            tnd.tnd_ext5 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table type
        var = req.getParameter("ctb_type");
        if (var != null)
            ctb_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("show_all");
        if( var != null && var.length() > 0) {
            try{
            	show_all = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
            	show_all = false;
            }
        }

    }

    public void treeNodeSearch(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        String var;

        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                tnd_id = 0;
            }
        } else {
            tnd_id = 0;
        }

        var = req.getParameter("status");
        if (var != null && var.length() > 0)
            status = var;
        else
            status = null;

        var = req.getParameter("phrase");
        if (var != null && var.length() > 0)
            phrase = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            phrase = null;

        var = req.getParameter("all_ind");
        if (var != null && var.length() > 0) {
            all_ind = Boolean.valueOf(var).booleanValue();
        } else {
            all_ind = false;
        }

        var = req.getParameter("exact");
        if (var != null && var.length() > 0) {
            exact = Boolean.valueOf(var).booleanValue();
        } else {
            exact = false;
        }

        var = req.getParameter("code");
        if (var != null && var.length() > 0)
            code = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            code = null;

        String[] var_lst = req.getParameterValues("type");
        if (var_lst != null && var_lst.length > 0) {
                types = var_lst;
        } else {
            types = null;
        }

        var = req.getParameter("appn_from");
        if (var != null && var.length() > 0)
            appn_from = Timestamp.valueOf(var);
        else
            appn_from = null;

        var = req.getParameter("appn_to");
        if (var != null && var.length() > 0)
            appn_to = Timestamp.valueOf(var);
        else
            appn_to = null;

        var = req.getParameter("eff_from");
        if (var != null && var.length() > 0)
            eff_from = Timestamp.valueOf(var);
        else
            eff_from = null;

        var = req.getParameter("eff_to");
        if (var != null && var.length() > 0)
            eff_to = Timestamp.valueOf(var);
        else
            eff_to = null;

        var = req.getParameter("search_timestamp");
        if (var != null && var.length() > 0)
            search_timestamp = Timestamp.valueOf(var);
        else
            search_timestamp = null;

        var = req.getParameter("page");
        if (var != null && var.length() > 0) {
            try {
                page = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                page = 0;
            }
        } else {
            page = 0;
        }

        var = req.getParameter("orderby");
        if (var != null && var.length() > 0) {
            orderBy = var;
        } else {
            orderBy = null;
        }

        var = req.getParameter("sortorder");
        if (var != null && var.length() > 0) {
            sortOrder = var;
        } else {
            sortOrder = null;
        }
    }

    // parameters needed in catalog related commands
    public void catalog(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        cat = new aeCatalog();
        String var;
        // catolog id
        var = req.getParameter("cat_id");
        if (var != null && var.length() > 0) {
            try {
                cat.cat_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                cat.cat_id = 0;
            }
        } else {
            cat.cat_id = -1;
        }

        //catalog title
        var = req.getParameter("cat_title");
        if (var != null && var.length() > 0)
            cat.cat_title = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //catalog public indicator
        var = req.getParameter("cat_public_ind");
        if (var != null && var.length() > 0)
            cat.cat_public_ind = Boolean.valueOf(var).booleanValue();
        else
            cat.cat_public_ind = false;
        
        //catalog mobile indicator
        var = req.getParameter("cat_mobile_ind");
        if (var != null && var.length() > 0)
        	cat.cat_mobile_ind = Boolean.valueOf(var).booleanValue();
        else
        	cat.cat_mobile_ind = false;

        //catalog status
        var = req.getParameter("cat_status");
        if (var != null && var.length() > 0)
            cat.cat_status = var;

        //catalog status
        var = req.getParameter("cat_desc");
        if (var != null && var.length() > 0)
            cat.cat_desc = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //catalog access entity id list
        var = req.getParameter("cat_acc_ent_id_list");
        if ( var!= null && var.length()!= 0 )
            cat_acc_ent_id_list = String2long(split(var, "~"));
        else
            cat_acc_ent_id_list = new long[0];

        //catalog access entity id list
        var = req.getParameter("ity_id_lst");
        if ( var!= null && var.length()!= 0 )
            ity_id_lst = split(var, "~");
        else
            ity_id_lst = new String[0];

        //cat_tcr_id
        var = req.getParameter("cat_tcr_id");
        if (var != null && var.length() > 0) {
            try {
                cat.cat_tcr_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                cat.cat_tcr_id = 1;
            }
        } else {
            cat.cat_tcr_id = -1;
        }

        var = req.getParameter("cat_show_all");
        if (var != null && var.length() > 0)
            cat.cat_show_all = Boolean.valueOf(var).booleanValue();
        else
            cat.cat_show_all = false;
/*
        //catalog read indicator
        var = req.getParameter("cac_read_ind_lst");
        if ( var!= null && var.length()!= 0 )
            cac_read_ind_lst = String2boolean(split(var, "~"));
        else
            cac_read_ind_lst = new boolean[0];

        //catalog write indicator
        var = req.getParameter("cac_write_ind_lst");
        if ( var!= null && var.length()!= 0 )
            cac_write_ind_lst = String2boolean(split(var, "~"));
        else
            cac_write_ind_lst = new boolean[0];
*/
        //catalog update timestamp
        var = req.getParameter("cat_upd_timestamp");
        if (var != null && var.length() > 0)
            cat_in_upd_timestamp = Timestamp.valueOf(var);
        else
            cat_in_upd_timestamp = null;
        
        //for multi del catalog
        var = req.getParameter("cat_id_lst");
        if ( var!= null && var.length()!= 0 )
            cat_id_lst = String2long(split(var, "~"));
        else
        	cat_id_lst = new long[0];

        //tree node upd timestamp list
        var = req.getParameter("cat_upd_timestamp_lst");
        if ( var!= null && var.length()!= 0 )
            cat_in_upd_timestamp_lst = String2Timestamp(split(var, "~"));
        else
        	cat_in_upd_timestamp_lst = new Timestamp[0];
        
        var = req.getParameter("training_type");
        if (var != null && var.length() > 0) {
        	training_type=var;
        }


    }

    public void applicationList() {
        app_lst_param = new Hashtable();
        Timestamp tempTime;
        String tempStr;
        String var;

        var = req.getParameter("app_create_date_begin");
        if (var != null && var.length() > 0) {
            try {
                tempTime = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                tempTime = aeUtils.EMPTY_DATE;
            }
        } else {
            tempTime = aeUtils.EMPTY_DATE;
        }
        app_lst_param.put(aeQueueManager.QM_APPN_CREATE_DATE_BEGIN, tempTime);

        var = req.getParameter("app_create_date_end");
        if (var != null && var.length() > 0) {
            try {
                tempTime = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                tempTime = aeUtils.EMPTY_DATE;
            }
        } else {
            tempTime = aeUtils.EMPTY_DATE;
        }
        app_lst_param.put(aeQueueManager.QM_APPN_CREATE_DATE_END, tempTime);

        var = req.getParameter("app_upd_date_begin");
        if (var != null && var.length() > 0) {
            try {
                tempTime = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                tempTime = aeUtils.EMPTY_DATE;
            }
        } else {
            tempTime = aeUtils.EMPTY_DATE;
        }
        app_lst_param.put(aeQueueManager.QM_APPN_UPD_DATE_BEGIN, tempTime);

        var = req.getParameter("app_upd_date_end");
        if (var != null && var.length() > 0) {
            try {
                tempTime = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                tempTime = aeUtils.EMPTY_DATE;
            }
        } else {
            tempTime = aeUtils.EMPTY_DATE;
        }
        app_lst_param.put(aeQueueManager.QM_APPN_UPD_DATE_END, tempTime);

        var = req.getParameter("slot_start_date");
        if (var != null && var.length() > 0) {
            try {
                tempTime = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                tempTime = aeUtils.EMPTY_DATE;
            }
        } else {
            tempTime = aeUtils.EMPTY_DATE;
        }
        app_lst_param.put(aeQueueManager.QM_SLOT_START_DATE, tempTime);

        var = req.getParameter("slot_end_date");
        if (var != null && var.length() > 0) {
            try {
                tempTime = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                tempTime = aeUtils.EMPTY_DATE;
            }
        } else {
            tempTime = aeUtils.EMPTY_DATE;
        }
        app_lst_param.put(aeQueueManager.QM_SLOT_END_DATE, tempTime);

        var = req.getParameter("timestamp");
        if (var != null && var.length() > 0) {
            try {
                tempTime = Timestamp.valueOf(var);
            } catch (IllegalArgumentException e) {
                tempTime = aeUtils.EMPTY_DATE;
            }
        } else {
            tempTime = aeUtils.EMPTY_DATE;
        }
        app_lst_param.put(aeQueueManager.QM_APPN_PARAM_TIMESTAMP, tempTime);

        var = req.getParameter("usr_display_bil");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_USR_DISPLAY_BIL, tempStr);

        var = req.getParameter("app_ext1");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_APPN_EXT1, tempStr);

        var = req.getParameter("tnd_title");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_TND_TITLE, tempStr);

        var = req.getParameter("itm_title");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_ITM_TITLE, tempStr);

        var = req.getParameter("app_ext3");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_APPN_EXT3, tempStr);

        var = req.getParameter("queue_type");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_QUEUE_TYPE, tempStr);

        var = req.getParameter("orderby");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_ORDER_BY, tempStr);

        var = req.getParameter("sortorder");
        if (var != null && var.length() > 0) {
            tempStr = var;
        } else {
            tempStr = "";
        }
        app_lst_param.put(aeQueueManager.QM_SORT_ORDER, tempStr);

    }

    public void aeGetComm() {
        String var;
        ach = new aeAppnCommHistory();
        
        var = req.getParameter("ach_id");
        if (var != null && var.length() > 0) {
            try {
                ach.ach_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                ach.ach_id = 0;
            }
        } else {
            ach.ach_id = 0;
        }

        var = req.getParameter("ach_aah_id");
        if (var != null && var.length() > 0) {
            try {
                ach.ach_aah_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                ach.ach_aah_id = 0;
            }
        } else {
            ach.ach_aah_id = 0;
        }
        
    }

    public void aeUpdComm(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        String var;
        ach = new aeAppnCommHistory();
        
        var = req.getParameter("ach_id");
        if (var != null && var.length() > 0) {
            try {
                ach.ach_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                ach.ach_id = 0;
            }
        } else {
            ach.ach_id = 0;
        }
        
        var = req.getParameter("ach_aah_id");
        if (var != null && var.length() > 0) {
             try {
                ach.ach_aah_id = Long.parseLong(var);
             } catch (NumberFormatException e) {
                ach.ach_aah_id = 0;
              }
        } else {
           ach.ach_aah_id = 0;
         }
        
        var = req.getParameter("app_id"); 
        if (var != null && var.length() > 0) {
            try {
                ach.ach_app_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                ach.ach_app_id = 0;
            }
        } else {
            ach.ach_app_id = 0;
        }
        
        var = req.getParameter("ach_content");
        if (var != null &&var.length() >= 0) {
            ach.ach_content = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        } else {
            ach.ach_content = null;
        }
        
        /*var = req.getParameter("cur_usr_id");
        if (var != null && var.length() > 0) {
            ach.ach_create_usr_id = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        } else {
            ach.ach_create_usr_id = null;
        }*/
        
        var = req.getParameter("ach_upd_timestamp");
        if (var != null && var.length() > 0) {
            ach.ach_upd_timestamp = Timestamp.valueOf(var);
        } else {
            ach.ach_upd_timestamp = null;
        }

        /*var = req.getParameter("ach_create_timestamp");
        if (var != null && var.length() > 0) {
            ach.ach_create_timestamp = Timestamp.valueOf(var);
        } else {
            ach.ach_create_timestamp = null;
        }*/

    }

    // parameters needed in queue management related commands
    public void queueManagement(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        String var;

/*        Enumeration enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = (String)enumeration.nextElement();
            System.out.println(name + "=" + (String)req.getParameter(name));
        }*/

        
        var = req.getParameter("app_id");
        if (var != null && var.length() > 0) {
            try {
                app_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                app_id = 0;
            }
        } else {
            app_id = 0;
        }

        var = req.getParameter("ent_id");
        if (var != null && var.length() > 0) {
            try {
                ent_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                ent_id = 0;
            }
        } else {
            ent_id = 0;
        }

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try {
                itm_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                itm_id = 0;
            }
        } else {
            itm_id = 0;
        }

        var = req.getParameter("page");
        if (var != null && var.length() > 0) {
            try {
                page = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                page = 0;
            }
        } else {
            page = 0;
        }

        var = req.getParameter("page_size");
        if (var != null && var.length() > 0) {
            try {
                page_size = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                page_size = 0;
            }
        } else {
            page_size = 0;
        }

        var = req.getParameter("app_process_status");
        if (var != null && var.length() > 0)
            app_process_status = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            app_process_status = null;

        var = req.getParameter("app_process_status_lst");
        if ( var!= null && var.length()!= 0 )
            app_process_status_lst = split(var, "~");
        else
            app_process_status_lst = new String[0];

        var = req.getParameter("sort_by");
        if (var != null && var.length() > 0)
            sort_by = var;

        var = req.getParameter("order_by");
        if (var != null && var.length() > 0)
            order_by = var;

        var = req.getParameter("sortCol");
        if (var != null && var.length() > 0)
            order_by = var;

        var = req.getParameter("sortOrder");
        if (var != null && var.length() > 0)
            sort_by = var;

        var = req.getParameter("queue_type");
        if (var != null && var.length() > 0)
            queue_type = var;
        else
            queue_type = null;

/*        var = req.getParameter("usr_id");
        if (var != null && var.length() > 0)
            usr_id = var;
        else
            usr_id = null;
*/
        var = req.getParameter("process_id");
        if (var != null && var.length() > 0) {
            try {
                process_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                process_id = 0;
            }
        } else {
            process_id = 0;
        }

        var = req.getParameter("action_id");
        if (var != null && var.length() > 0) {
            try {
                action_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                action_id = 0;
            }
        } else {
            action_id = 0;
        }

        var = req.getParameter("status_id");
        if (var != null && var.length() > 0) {
            try {
                status_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                status_id = 0;
            }
        } else {
            status_id = 0;
        }

        var = req.getParameter("fr");
        if (var != null && var.length() > 0)
            fr = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            fr = null;

        var = req.getParameter("to");
        if (var != null && var.length() > 0)
            to = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            to = null;

        var = req.getParameter("verb");
        if (var != null && var.length() > 0)
            action_verb = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            action_verb = null;

        var = req.getParameter("aah_id");
        if (var != null && var.length() > 0) {
            try {
                aah_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                aah_id = 0;
            }
        } else {
            aah_id = 0;
        }

        var = req.getParameter("content");
        if (var != null && var.length() > 0)
            content = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            content = null;

        var = req.getParameter("upd_timestamp");
        if (var != null && var.length() > 0)
            upd_timestamp = Timestamp.valueOf(var);
        else
            upd_timestamp = null;

        var = req.getParameter("app_xml");
        if (var != null && var.length() > 0)
            app_xml = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            app_xml = null;

        var = req.getParameter("history");
        if (var != null && var.length() > 0) {
                history = Boolean.valueOf(var).booleanValue();
        } else {
            history = false;
        }

        var = req.getParameter("download");
        if (var != null && var.length() > 0) {
                download = Boolean.valueOf(var).booleanValue();
        } else {
            download = false;
        }

        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                tnd_id = 0;
            }
        } else {
            tnd_id = 0;
        }

        var = req.getParameter("app_id_lst");
        if ( var!= null && var.length()!= 0 )
            app_id_lst = String2long(split(var, "~"));
        else
            app_id_lst = null;

        var = req.getParameter("ent_id_lst");
        if ( var!= null && var.length()!= 0 )
            ent_id_lst = String2long(split(var, "~"));
        else
            ent_id_lst = null;

        var = req.getParameter("itm_id_lst");
        if ( var!= null && var.length()!= 0 )
            itm_id_lst = String2long(split(var, "~"));
        else
            itm_id_lst = null;

        var = req.getParameter("comment_lst");
        if ( var!= null && var.length()!= 0 )
            comment_lst = split(var, "~");
        else
            comment_lst = null;

        var = req.getParameter("comment");
        if (var != null && var.length() > 0)
            comment = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            comment = null;

        var = req.getParameter("app_upd_timestamp_lst");
        if ( var!= null && var.length()!= 0 )
            app_upd_timestamp_lst = String2Timestamp(split(var, "~"));
        else
            app_upd_timestamp_lst = null;

        var = req.getParameter("app_priority_lst");
        if ( var!= null && var.length()!= 0 )
            app_priority_lst = split(var, "~");
        else
            app_priority_lst = null;

        var = req.getParameter("tvw_id");
        if (var != null) {
            tvw_id = var;
        }

        var = req.getParameter("app_tvw_id");
        if (var != null) {
            app_tvw_id = var;
        }

        var = req.getParameter("timestamp");
        if (var != null && var.length() > 0) {
            search_timestamp = Timestamp.valueOf(var);
        }

        /**
         * added for the application list of approver
         * Emily, 20020830
         */
        var = req.getParameter("tpl_id");
        if (var != null && var.length() > 0) {
            this.tpl_id = Integer.parseInt(var);
        }

        var = req.getParameter("show_approval_ent");
        if (var != null && var.length() > 0) {
            show_approval_ent_only = Boolean.valueOf(var).booleanValue();
        } else {
            show_approval_ent_only = false;
        }
        
        var = req.getParameter("usr_ent_n_rol_id_lst");
        //val = 3[|]APPR_1[|]LRN_1~3[|]APPR_1[|]LRN_1
        // ~ as a delimiter of each approver and his/her rol_ext_id selected
        //first element is approver entity id and the following is the rol_ext_id of the approver
        if ( var!= null && var.length()!= 0 ) {
            String[] usr_ent_n_rol_id_lst = split(var, "~");
            entIdRole = new Hashtable();
            for(int i=0; i<usr_ent_n_rol_id_lst.length; i++) {
                String[] token = dbUtils.split( usr_ent_n_rol_id_lst[i], "[|]" );
                Vector roleVec = null;
                if( token == null || token.length == 0 )
                    continue;
                else{
                    if( entIdRole.contains(new Long(token[0])) ) {
                        roleVec = (Vector)entIdRole.get(new Long(token[0]));
                    } else {
                        roleVec = new Vector();
                    }
                    for(int j=1; j<token.length; j++)
                        roleVec.addElement(token[j]);
                    entIdRole.put(new Long(token[0]), roleVec);
                }
            }
        }        

        var = req.getParameter("show_process_status");
        if (var != null && var.length() > 0)
            show_process_status = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            show_process_status = null;
            
        var = req.getParameter("aal_status");            
        if (var != null && var.length() > 0)
            aal_status = var;
        else
            aal_status = "";
            
        //var = req.getParameter("filter_value"); 
        String prmNm = "filter_value";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() != 0)
            filter_value = dbUtils.unicodeFrom(var, clientEnc, env_encoding, bMultiPart);
        else
            filter_value = "";
            
        var = req.getParameter("filter_type");            
        if (var != null && var.length() > 0)
            filter_type = var;
            
        var = req.getParameter("clear_session");
        if (var != null && var.length() > 0) {
            clear_session = Boolean.valueOf(var).booleanValue();
        } else {
            clear_session = false;
        }
        
        var = req.getParameter("upddate_fr");            
        if (var != null && var.length() > 0){
            appn_upd_fr = Timestamp.valueOf(var);
        }
        
        var = req.getParameter("upddate_to");            
        if (var != null && var.length() > 0){
            appn_upd_to = Timestamp.valueOf(var);
        }

        var = req.getParameter("app_lst_page");
        if (var != null && var.length() > 0) {
            try {
                app_lst_page = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                app_lst_page = 0;
            }
        } else {
                app_lst_page = 0;
        }

        var = req.getParameter("app_lst_page_size");
        if (var != null && var.length() > 0) {
            try {
                app_lst_page_size = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                app_lst_page_size = 0;
            }
        } else {
            app_lst_page_size = 0;
        }
        
        var = req.getParameter("from_core5");
        if (var != null && var.length() > 0) {
        	try {
        		from_core5 = Boolean.valueOf(var).booleanValue();
			} catch (Exception e) {
				from_core5 = false;
			}
            
        } else {
        	from_core5 = false;
        }

    }

public String no_result_stylesheet;

public void getSearchItem(String clientEnc, String env_encoding)
    throws UnsupportedEncodingException {
        String var;
        String[] varArray;
        aes = new aeSearch();

        var = req.getParameter("usr_id");
        if (var != null && var.length() > 0)
            aes.usr_id = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("usr_id_type");
        if( var != null && var.length() > 0 )
            aes.usr_id_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("usr_last_name");
        if (var != null && var.length() > 0)
            aes.usr_last_name = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("usr_last_name_type");
        if (var != null && var.length() > 0)
            aes.usr_last_name_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("usr_first_name");
        if (var != null && var.length() > 0)
            aes.usr_first_name = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("usr_first_name_type");
        if (var != null && var.length() > 0)
            aes.usr_first_name_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("usr_email");
        if (var != null && var.length() > 0)
            aes.usr_email = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("usr_email_type");
        if (var != null && var.length() > 0)
            aes.usr_email_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("oi_id");
        if (var != null && var.length() > 0) {
            try {
                aes.oi_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                aes.oi_id = 0;
            }
        } else {
            aes.oi_id = 0;
        }

        var = req.getParameter("oi_due_from");
        if (var != null && var.length() > 0)
            aes.oi_due_from = Timestamp.valueOf(var);
        else
            aes.oi_due_from = null;

        var = req.getParameter("oi_due_to");
        if (var != null && var.length() > 0)
            aes.oi_due_to = Timestamp.valueOf(var);
        else
            aes.oi_due_to = null;

        var = req.getParameter("oi_desc");
        if (var != null && var.length() > 0)
            aes.oi_desc = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("oi_desc_type");
        if (var != null && var.length() > 0)
            aes.oi_desc_type = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        var = req.getParameter("acn_type");
        if ( var != null && var.length() > 0 )
            aes.acn_type = var.toUpperCase();

        var = req.getParameter("no_result_stylesheet");
        if (var != null && var.length() > 0)
            no_result_stylesheet = var;

        var = req.getParameter("page_num");
        if(var != null && var.length() > 0)
        try {
                aes.page_num = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                aes.page_num = 1;
            }
        else
            aes.page_num = 1;


        var = req.getParameter("item_per_page");
        if(var != null && var.length() > 0)
        try {
                aes.item_per_page = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                aes.item_per_page = 1;
            }
        else
            aes.item_per_page = 1;

        var = req.getParameter("order_by");
        if(var != null && var.length() > 0)
            aes.order_by = var;

        var = req.getParameter("first_time");
        if(var != null && var.length() > 0) {
            if(var.equalsIgnoreCase("Y"))
                aes.first_time = true;
        }
        else
            aes.first_time = false;

        var = req.getParameter("sort_by");
        if(var != null && var.length() > 0)
            aes.sort_by = var;

        varArray = req.getParameterValues("oi_status");
        if (varArray != null && varArray.length > 0)
            aes.oi_status = varArray;


        return;
    }

public void getFilterAppn(String clientEnc, String env_encoding) 
    throws UnsupportedEncodingException{

    // user search
    String prmNm;
    String val;
    dbUsg = new dbUserGroup();
    dbUsg.s_ext_col_names = new Vector();
    dbUsg.s_ext_col_types = new Vector();
    dbUsg.s_ext_col_values = new Vector();
    
    prmNm = "ent_id";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if ( val!= null && val.length()!= 0 )
        dbUsg.usg_ent_id = Long.parseLong(val);;

        
    prmNm = "s_usr_id";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_id = val;
    else
        dbUsg.s_usr_id = null;
        
    prmNm = "s_usr_job_title";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_job_title = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_job_title = null;

    prmNm = "s_usr_email";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_email = val;
    else
        dbUsg.s_usr_email = null;

    prmNm = "s_usr_email_2";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_email_2 = val;
    else
        dbUsg.s_usr_email_2 = null;
        
    prmNm = "s_usr_initial_name_bil";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_initial_name_bil = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_initial_name_bil = null;
        
    prmNm = "s_usr_last_name_bil";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_last_name_bil = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_last_name_bil = null;

    prmNm = "s_usr_first_name_bil";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_first_name_bil = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_first_name_bil = null;

    prmNm = "s_usr_display_bil";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_display_bil = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_display_bil = null;

    prmNm = "s_usr_id_display_bil";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_id_display_bil = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_id_display_bil = null;

    prmNm = "s_usr_gender";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_gender = val;
    else
        dbUsg.s_usr_gender = null;

      prmNm = "s_usr_bday_fr";
      val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
      if ( val!= null && val.trim().length()!= 0 )
          dbUsg.s_usr_bday_fr = Timestamp.valueOf(val);
      else
          dbUsg.s_usr_bday_fr = null;
          
      prmNm = "s_usr_bday_to";
      val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
      if ( val!= null && val.trim().length()!= 0 )
          dbUsg.s_usr_bday_to = Timestamp.valueOf(val);
      else
          dbUsg.s_usr_bday_to = null;


      prmNm = "s_usr_jday_fr";
      val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
      if ( val!= null && val.trim().length()!= 0 )
          dbUsg.s_usr_jday_fr = Timestamp.valueOf(val);
      else
          dbUsg.s_usr_jday_fr = null;

      prmNm = "s_usr_jday_to";
      val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
      if ( val!= null && val.trim().length()!= 0 )
          dbUsg.s_usr_jday_to = Timestamp.valueOf(val);
      else
          dbUsg.s_usr_jday_to = null;

    prmNm = "s_usr_hkid";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_hkid = val;
    else
        dbUsg.s_usr_hkid = null;

    prmNm = "s_usr_tel";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_tel = val;
    else
        dbUsg.s_usr_tel = null;

    prmNm = "s_usr_fax";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_fax = val;
    else
        dbUsg.s_usr_fax = null;

    prmNm = "s_usr_address_bil";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_address_bil = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_address_bil = null;

    prmNm = "s_usr_postal_code_bil";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_usr_postal_code_bil = dbUtils.unicodeFrom(val, clientEnc, env_encoding, bMultiPart);
    else
        dbUsg.s_usr_postal_code_bil = null;

    prmNm = "s_grade";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_grade = Long.parseLong(val);

    prmNm = "s_idc_int";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_idc_int = Long.parseLong(val);

    prmNm = "s_idc_fcs";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0)
        dbUsg.s_idc_fcs = Long.parseLong(val);

    prmNm = "s_usg_ent_id_lst";
    val = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
    if (val != null && val.length() != 0) {
        dbUsg.s_usg_ent_id_lst = dbUtils.split(val, "~");
    }

    Enumeration attr_names = req.getParameterNames();
    while(attr_names.hasMoreElements()) {
        String att_name = (String)attr_names.nextElement();
        if (att_name.startsWith("s_ext_")) {
                dbUsg.s_ext_col_names.addElement(att_name);
            if (att_name.endsWith("_text")) {
                dbUsg.s_ext_col_types.addElement(DbTable.COL_TYPE_STRING);
                dbUsg.s_ext_col_values.addElement(dbUtils.unicodeFrom(req.getParameterValues(att_name)[0], clientEnc, env_encoding, bMultiPart));
            }
            if (att_name.endsWith("_fr")) {
                dbUsg.s_ext_col_types.addElement(DbTable.COL_TYPE_TIMESTAMP);
                if (req.getParameterValues(att_name)[0].length() > 0) {
                    try{
                        dbUsg.s_ext_col_values.addElement(Timestamp.valueOf(req.getParameterValues(att_name)[0]));
                    }catch(NumberFormatException e){
                        dbUsg.s_ext_col_values.addElement("");
                    }
                    
                } else {
                    dbUsg.s_ext_col_values.addElement("");
                }
            }
            if (att_name.endsWith("_to")) {
                dbUsg.s_ext_col_types.addElement(DbTable.COL_TYPE_TIMESTAMP);
                if (req.getParameterValues(att_name)[0].length() > 0) {
                    try{
                        dbUsg.s_ext_col_values.addElement(Timestamp.valueOf(req.getParameterValues(att_name)[0]));
                    }catch(NumberFormatException e){
                        dbUsg.s_ext_col_values.addElement("");
                    }
                } else {
                    dbUsg.s_ext_col_values.addElement("");
                }
            }
            if (att_name.endsWith("_select")) {
                dbUsg.s_ext_col_types.addElement(DbTable.COL_TYPE_STRING);
                dbUsg.s_ext_col_values.addElement(req.getParameterValues(att_name)[0]);
            }
            if (att_name.endsWith("_check")) {
                dbUsg.s_ext_col_types.addElement(DbTable.COL_TYPE_STRING);
                String[] checkValue = req.getParameterValues(att_name);
                dbUsg.s_ext_col_values.addElement(checkValue);
            }
        }
    }
}
    public void acnTransaction(String clientEnc, String env_encoding)
        throws UnsupportedEncodingException {
            String var;
            axn = new aeAccountTransaction();

            var = req.getParameter("stylesheet_fail");
            if (var != null && var.length() > 0)
                stylesheet_fail = var;

            var = req.getParameter("stylesheet_success");
            if (var != null && var.length() > 0)
                stylesheet_success = var;


            var = req.getParameter("acn_id");
            if(var != null && var.length() >0)
                axn.axn_acn_id = Integer.parseInt(var);

            var = req.getParameter("axn_ccy");
            if(var != null && var.length() >0)
                axn.axn_ccy = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

            var = req.getParameter("axn_amt");
            if( var != null && var.length() > 0 )
                axn.axn_amt = to2decPt(var);


            var = req.getParameter("axn_type");
            if( var != null && var.length() > 0 )
                axn.axn_type = var.toUpperCase();

            var = req.getParameter("axn_desc");
            if(var != null && var.length() > 0 )
                axn.axn_desc = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);

            var = req.getParameter("axn_method");
            if( var != null && var.length() > 0 )
                axn.axn_method = var.toUpperCase();

            var = req.getParameter("axn_ref");
            if( var != null && var.length() > 0 )
                axn.axn_ref = var;

            var = req.getParameter("axn_create_usr_id");
            if( var != null && var.length() > 0 )
                axn.axn_create_usr_id = var;

            var = req.getParameter("axn_create_usr_ent_id");
            if( var != null && var.length() > 0 )
                axn.axn_create_usr_id = var;

            var = req.getParameter("ref_no");
            if( var != null && var.length() > 0 )
                axn.ref_no = var;

            var = req.getParameter("card_type");
            if( var != null && var.length() > 0 )
                axn.card_type = var;

            var = req.getParameter("holder");
            if( var != null && var.length() > 0 )
                axn.holder = var;

            var = req.getParameter("exp_month");
            if( var != null && var.length() > 0 )
                axn.exp_month = var;

            var = req.getParameter("exp_year");
            if( var != null && var.length() > 0 )
                axn.exp_year = var;

            var = req.getParameter("usr_ent_id");
            if( var != null && var.length() > 0 ) {
                axn.ent_id = Long.parseLong(var);
                ent_id = Long.parseLong(var);
            }
            var = req.getParameter("acn_type");
            if( var != null && var.length() > 0 ) {
                axn.acn_type = var.toUpperCase();
                acn_type = var.toUpperCase();
            }

            var = req.getParameter("item_amount");
            if( var != null && var.length() > 0 )
                axn.amount_assigned = Stringto2decPtfloat(split(var,"~"));

            var = req.getParameter("item_id");
            if( var != null && var.length() > 0 )
                axn.item_assigned = String2int(split(var,"~"));

        }

    // online payment
    boolean Succeed;
    String BillNo;
    String Amount;
    String Date;
    String Msg;
    String Signature;
    String stylesheet_fail;
    String stylesheet_success;
    int axn_id;
    public void onlinePayment(String clientEnc, String env_encoding)
        throws UnsupportedEncodingException {
            String var;

            var = req.getParameter("stylesheet_fail");
            if (var != null && var.length() > 0)
                stylesheet_fail = var;

            var = req.getParameter("stylesheet_success");
            if (var != null && var.length() > 0)
                stylesheet_success = var;

            var = req.getParameter("BillNo");
            if( var != null && var.length() > 0 )
                BillNo = var;//Integer.parseInt(var);

            var = req.getParameter("axn_id");
            if( var != null && var.length() > 0 )
                axn_id = Integer.parseInt(var);


            var = req.getParameter("Amount");
            if( var != null && var.length() > 0 )
                Amount = var;//to2decPt(var);

            var = req.getParameter("Date");
            if( var != null && var.length() > 0 )
                Date = var;

            var = req.getParameter("Msg");
            if( var != null && var.length() > 0 )
                Msg = var;

            var = req.getParameter("?Succeed");
            if( var != null && var.length() > 0 )
                if(var.equalsIgnoreCase("Y"))
                    Succeed = true;
                else
                    Succeed = false;

            var = req.getParameter("Signature");
            if( var != null && var.length() > 0 )
                Signature = var;


            return;
        }

    // parameters for booking slots
    public void bookSlot(String clientEnc, String env_encoding) {
        String var = null;
        String var1[], var2[], var3[];
        /*
        var1 = req.getParameterValues("itm_id");
        if (var1 == null) var1 = new String[0];
        var2 = req.getParameterValues("start_time");
        if (var2 == null) var2 = new String[0];
        var3 = req.getParameterValues("tnd_parent_tnd_id");
        if (var3 == null) var3 = new String[0];
        */
        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0)
            var1 = split(var, "~");
        else
            var1 = new String[0];
        var = req.getParameter("start_time");
        if (var != null && var.length() > 0)
            var2 = split(var, "~");
        else
            var2 = new String[0];
        var = req.getParameter("tnd_parent_tnd_id");
        if (var != null && var.length() > 0)
            var3 = split(var, "~");
        else
            var3 = new String[0];

        slot_item_id = new int[var1.length];
        for (int i = 0; i < var1.length; i++) {
            try {
                slot_item_id[i] = Integer.parseInt(var1[i]);
            } catch (NumberFormatException e) {
                slot_item_id[i] = 0;
            }
        }

        slot_start_time = new Timestamp[var2.length];
        for (int i = 0; i < var2.length; i++) {
            try {
                slot_start_time[i] = Timestamp.valueOf(var2[i]);
            } catch (IllegalArgumentException e) {
                slot_start_time[i] = null;
            }
        }

        slot_parent_node_id = new int[var3.length];
        for (int i = 0; i < var3.length; i++) {
            try {
                slot_parent_node_id[i] = Integer.parseInt(var3[i]);
            } catch (NumberFormatException e) {
                slot_parent_node_id[i] = 0;
            }
        }

        try {
            var = req.getParameter("app_xml");
            if (var != null && var.length() > 0)
                app_xml = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            else
                app_xml = null;
        } catch (UnsupportedEncodingException e) {
            app_xml = null;
        }

        try {
            var = req.getParameter("app_ext1");
            if (var != null && var.length() > 0)
                app_ext1 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            else
                app_ext1 = null;
        } catch (UnsupportedEncodingException e) {
            app_ext1 = null;
        }
        try {
            var = req.getParameter("app_ext3");
            if (var != null && var.length() > 0)
                app_ext3 = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            else
                app_ext3 = null;
        } catch (UnsupportedEncodingException e) {
            app_ext3 = null;
        }
    }

    public String imt_status;
    //DbItemMote itemMote = null;
    /*
    public void Mote(String clientEnc, String env_encoding) {

        String var = null;
        itemMote = new DbItemMote();

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try {
                itm_id = Long.parseLong(var);
                itemMote.imt_itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm_id = 0;
                itemMote.imt_itm_id = 0;
            }
        } else {
            itm_id = 0;
            itemMote.imt_itm_id = 0;
        }

        var = req.getParameter("imt_status");
        if (var != null && var.length() > 0) {
            imt_status = var;
            itemMote.imt_status = var;
        }

        var = req.getParameter("imt_budget_cmt");
        if (var != null && var.length() > 0) {
            itemMote.imt_budget_cmt = var;
        }
        var = req.getParameter("imt_participant_cmt");
        if (var != null && var.length() > 0) {
            itemMote.imt_participant_cmt = var;
        }
        var = req.getParameter("imt_rating_cmt");
        if (var != null && var.length() > 0) {
            itemMote.imt_rating_cmt = var;
        }
        var = req.getParameter("imt_pos_cmt");
        if (var != null && var.length() > 0) {
            itemMote.imt_pos_cmt = var;
        }
        var = req.getParameter("imt_neg_cmt");
        if (var != null && var.length() > 0) {
            itemMote.imt_neg_cmt = var;
        }
        var = req.getParameter("imt_ist_cmt");
        if (var != null && var.length() > 0) {
            itemMote.imt_ist_cmt = var;
        }
        var = req.getParameter("imt_suggestion");
        if (var != null && var.length() > 0) {
            itemMote.imt_suggestion = var;
        }

    }
      */
    public void ItmMod(String clientEnc, String env_encoding) {

        dbTargetRating = new DbItemRating();
        dbActualRating = new DbItemRating();;

        dbTargetRating.irt_type = DbItemRating.TARGET_OVERALL_RATING;
        dbActualRating.irt_type = DbItemRating.ACTUAL_OVERALL_RATING;

        String var = null;

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try {
                itm_id = Long.parseLong(var);
                dbTargetRating.irt_itm_id = Integer.parseInt(var);
                dbActualRating.irt_itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm_id = 0;
                dbTargetRating.irt_itm_id = 0;
                dbActualRating.irt_itm_id = 0;
            }
        } else {
            itm_id = 0;
            dbTargetRating.irt_itm_id = 0;
            dbActualRating.irt_itm_id = 0;
        }

        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                tnd_id = 0;
            }
        } else {
            tnd_id = 0;
        }

        var = req.getParameter("target_rate");
        if (var != null && var.length() > 0) {
            dbTargetRating.irt_rate = Float.parseFloat(var);;
        }

        var = req.getParameter("actual_rate");
        if (var != null && var.length() > 0) {
            dbActualRating.irt_rate = Float.parseFloat(var);;
        }
        var = req.getParameter("mod_id");
        if (var != null && var.length() > 0) {
            try {
                mod_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                mod_id = 0;
            }
        } else {
            mod_id = 0;
        }

    }

    public static String[] split(String var, String delim) {
        StringTokenizer st = new StringTokenizer(var, delim);
        String[] result = new String[st.countTokens()];


        for(int i=0;i<result.length;i++)
            result[i] = st.nextToken();

        return result;
    }

    public static boolean[] String2boolean(String[] var) {

        boolean[] result = new boolean[var.length];

        for(int i=0;i<result.length;i++)
            result[i] = Boolean.valueOf(var[i]).booleanValue();

        return result;
    }

    public static long[] String2long(String[] var) {

        long[] result = new long[var.length];

        for(int i=0;i<result.length;i++)
            result[i] = Long.parseLong(var[i]);

        return result;
    }

    public static int[] String2int(String[] var) {

        int[] result = new int[var.length];

        for(int i=0;i<result.length;i++)
            result[i] = Integer.parseInt(var[i]);

        return result;
    }

    public static float[] Stringto2decPtfloat(String[] var) {

        float[] result = new float[var.length];

        for(int i=0;i<result.length;i++)
            result[i] = to2decPt(var[i]);

        return result;
    }


    public static Timestamp[] String2Timestamp(String var[]) {
        Timestamp[] result = new Timestamp[var.length];

        for(int i=0;i<result.length;i++) {
            if(var[i] == null || var[i].equalsIgnoreCase("null")) {
                result[i] = null;
            } else {
                result[i] = Timestamp.valueOf(var[i]);
            }
        }
        return result;

    }


    public String sender_id;    //sender user id

    public String rec_ent_ids;
    public String cc_ent_ids;
    public String cc_email_address;
    public String bcc_ent_ids;
    public int notify_status;
    public Timestamp msg_datetime;
    public String url_redirect;

    public void send_notify(String clientEnc, String env_encoding)
        throws UnsupportedEncodingException {

        String var = null;

        var = req.getParameter("url_redirect");
        if (var != null && var.length() > 0) {
            url_redirect = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }


        var = req.getParameter("ent_ids");
        if (var != null && var.length() > 0) {
            rec_ent_ids = var;
        }

        var = req.getParameter("cc_ent_ids");
        if (var != null && var.length() > 0) {
            cc_ent_ids = var;
        }

        var = req.getParameter("bcc_ent_ids");
        if (var != null && var.length() > 0) {
            bcc_ent_ids = var;
        }

        try{
            var = req.getParameter("ji_status");
            if( var != null && var.length() > 0 )
                notify_status = Integer.parseInt(var);
        } catch( NumberFormatException e ) {
            notify_status = 0;
        }

        try{
            var = req.getParameter("itm_id");
            if( var != null && var.length() > 0 )
                itm_id = Integer.parseInt(var);
        } catch( NumberFormatException e ) {
            itm_id = 0;
        }

        try{
            var = req.getParameter("msg_datetime");
            if( var != null && var.length() > 0 )
                msg_datetime = Timestamp.valueOf(var);
        } catch( IllegalArgumentException  e ) {
                msg_datetime = null;
        }

        return;
    }


    public void get_notify(String clientEnc, String env_encoding) {

        String var = null;
        aeXmsg = new aeXMessage();
        itm = new aeItem();

        var = req.getParameter("itm_id");
        if( var != null && var.length() != 0 ) {
            itm.itm_id = Long.parseLong(var);
        } else {
            itm.itm_id = 0;
        }

        
        var = req.getParameter("ent_ids");
        if( var != null && var.length() != 0 ) {
            aeXmsg.ent_id = Long.parseLong(var);
        } else
            aeXmsg.ent_id = 0;
        
        var = req.getParameter("sender_id");
        if( var != null && var.length() != 0 )
            aeXmsg.sender_id = var;

        var = req.getParameter("tvw_id");
        if( var != null && var.length() != 0 )
            tvw_id = var;
        
        return;

    }

    public void get_quota_exceed_notify(String clientEnc, String env_encoding) {

        String var = null;
        itm = new aeItem();

        var = req.getParameter("itm_id");
        if( var != null && var.length() != 0 ) {
            itm.itm_id = Long.parseLong(var);
        } else {
            itm.itm_id = 0;
        }
        
        var = req.getParameter("appn_wait_count");
        if( var != null && var.length() != 0 )
            appn_wait_count = Long.parseLong(var);
        else
        	appn_wait_count = 0;
        
        var = req.getParameter("sender_id");
        if( var != null && var.length() != 0 )
        	sender_id = var;
        else
        	sender_id = null;
        
        var = req.getParameter("ent_ids");
        if( var != null && var.length() != 0 )
        	rec_ent_ids = var;
        else
        	rec_ent_ids = null;        
        return;

    }
    
    public void get_sys_performance_notify(String clientEnc, String env_encoding) {

        String var = null;
        itm = new aeItem();

        var = req.getParameter("active_user");
        if( var != null && var.length() != 0 ) {
        	active_user = Long.parseLong(var);
        } else {
        	active_user = 0;
        }
        
        var = req.getParameter("warning_user");
        if( var != null && var.length() != 0 )
            warning_user = Long.parseLong(var);
        else
        	warning_user = 0;
        
        var = req.getParameter("blocking_user");
        if( var != null && var.length() != 0 )
        	blocking_user = Long.parseLong(var);
        else
        	blocking_user = 0;
        
        var = req.getParameter("sender_id");
        if( var != null && var.length() != 0 )
        	sender_id = var;
        else
        	sender_id = null;

        var = req.getParameter("gen_time");
        if( var != null && var.length() != 0 )
        	gen_time = Timestamp.valueOf(var);
        else
        	gen_time = null;
        
        return;

    }
    
    public void ji_get_notify(String clientEnc, String env_encoding) {

        String var = null;
        aeXmsg = new aeXMessage();
        itm = new aeItem();

        var = req.getParameter("itm_id");
        if( var != null && var.length() != 0 ) {
            itm.itm_id = Long.parseLong(var);
        } else {
            itm.itm_id = 0;
        }

        var = req.getParameter("ent_ids");
        if( var != null && var.length() != 0 )
            ent_ids = var;    

        var = req.getParameter("sender_id");
        if( var != null && var.length() != 0 )
            aeXmsg.sender_id = var;

        var = req.getParameter("tvw_id");
        if( var != null && var.length() != 0 )
            tvw_id = var;

        var = req.getParameter("cc_to_approver_ind");
        if (var != null && var.length() > 0) {
            if(var.equals("true"))
                this.cc_to_approver_ind = true;
        }
       
        var = req.getParameter("cc_to_approver_ex_id");
        if (var != null && var.length() > 0) {
            this.cc_to_approver_rol_ext_id = split(var, "~");
        }
        
        var = req.getParameter("bcc_ent_ids");
        if (var != null && var.length() > 0) {
            this.bcc_ent_ids = var;
        }

        return;

    }

    boolean refresh;
    public void notify_page(String clientEnc, String env_encoding) {

        String var = null;
        var = req.getParameter("refresh");
        if( var != null && var.length() > 0)
            try{
                refresh = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
                refresh = false;
            }

        return;
    }

    int ji_status = 0;
    int rem_status = 0;
    Timestamp send_date = null;
    public void notify_user(String clientEnc, String env_encoding) {

        String var = null;
        var = req.getParameter("flag");
        if( var != null && var.length() > 0)
            try{
                flag = (new Boolean(var)).booleanValue();
            }catch( ClassCastException e ) {
                flag = false;
            }

        var = req.getParameter("JI_STATUS");
        if( var != null && var.length() > 0)
            try{
                ji_status = Integer.parseInt(var);
            }catch( NumberFormatException e ) {
                ji_status = 0 ;
            }

        var = req.getParameter("REM_STATUS");
        if( var != null && var.length() > 0)
            try{
                rem_status = Integer.parseInt(var);
            }catch( NumberFormatException e ) {
                rem_status = 0 ;
            }

        var = req.getParameter("ITM_ID");
        if( var != null && var.length() > 0)
            try{
                itm_id = Integer.parseInt(var);
            }catch( NumberFormatException e ) {
                itm_id = 0 ;
            }


        var = req.getParameter("rec_ent_ids");
        if (var != null && var.length() > 0) {
            rec_ent_ids = var;
        }

        var = req.getParameter("cc_ent_ids");
        if (var != null && var.length() > 0) {
            cc_ent_ids = var;
        }

        var = req.getParameter("send_date");
        if( var != null && var.length() > 0)
            send_date = Timestamp.valueOf(var);

        return;
    }

    public void pick_recip(String clientEnc, String env_encoding) {

        String var = null;
        var = req.getParameter("rec_ent_ids");
        if (var != null && var.length() > 0) {
            rec_ent_ids = var;
        }

        return;
    }


    int cur_page;
    int pagesize;
    Timestamp timestamp;
    public void get_ent_lst(String clientEnc, String env_encoding) {

        String var = null;
        try{
            var = req.getParameter("cur_page");
            if( var != null && var.length() > 0 )
                cur_page = Integer.parseInt(var);
        } catch( NumberFormatException e ) {
            cur_page = 1;
        }

        try{
            var = req.getParameter("pagesize");
            if( var != null && var.length() > 0 )
                pagesize = Integer.parseInt(var);
        } catch( NumberFormatException e ) {
            pagesize = 10;
        }

        dbUsg = new dbUserGroup ();
        try{
            var = req.getParameter("ent_id");
            if( var != null && var.length() > 0 )
                dbUsg.usg_ent_id = Long.parseLong(var);
        } catch( NumberFormatException e ) {
            dbUsg.usg_ent_id = 0;
        }

        try{
            var = req.getParameter("tnd_id");
            if( var != null && var.length() > 0 )
                tnd_id = Long.parseLong(var);
        } catch( NumberFormatException e ) {
            tnd_id = 0;
        }

        try{
            var = req.getParameter("timestamp");
            if( var != null && var.length() > 0 )
                timestamp = Timestamp.valueOf(var);
        } catch( NumberFormatException e ) {
            timestamp = null;
        }
    }

    public void attendance(String clientEnc, String env_encoding) {
        String var;
        var = req.getParameter("itm_id");
        if( var != null && var.length() > 0){
            try{
                itm_id = Integer.parseInt(var);
            }catch( NumberFormatException e ) {
                itm_id = 0 ;
            }
        }
		var = req.getParameter("cos_res_id");
		if (var != null && var.length() > 0) {
			try {
				cos_res_id = Integer.parseInt(var);
			} catch (NumberFormatException e) {
				cos_res_id = 0;
			}
		}
        var = req.getParameter("att_status");
        if( var != null && var.length() > 0){
            try{
                att_status = Integer.parseInt(var);
            }catch( NumberFormatException e ) {
                att_status = -1 ;
            }
        } else {
            att_status = 0;
        }

        var = req.getParameter("app_id");
        if (var != null && var.length() > 0) {
            try {
                app_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                app_id = 0;
            }
        } else {
            app_id = 0;
        }
        var = req.getParameter("app_id_lst");
        if ( var!= null && var.length()!= 0 )
            app_id_lst = String2long(split(var, "~"));
        else
            app_id_lst = null;

        try{
            var = req.getParameter("remark");
            if( var != null && var.length() > 0){
                remark = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
        } catch (UnsupportedEncodingException e) {
            remark = null;
        }
        //add for remark
        try{
            var = req.getParameter("att_remark_lst");
            if( var != null && var.length() > 0){
                att_remark_lst = split(dbUtils.unicodeFrom(var, clientEnc, env_encoding, false), "~");
            }
        } catch (UnsupportedEncodingException e) {
            remark = null;
        }
        var = req.getParameter("att_update_timestamp_lst");
        if (var != null && var.length() > 0) {
            att_update_timestamp_lst = String2Timestamp(split(var,"~"));
        }
        else
            att_update_timestamp = null;
        var = req.getParameter("att_update_timestamp");
        if (var != null && var.length() > 0) {
            att_update_timestamp = Timestamp.valueOf(var);
        }
        else
            att_update_timestamp = null;


		var = req.getParameter("att_timestamp");
			 if (var != null && var.length() > 0) {
				 att_timestamp = Timestamp.valueOf(var);
			 }
			 else
				 att_timestamp = null;
        
        var = req.getParameter("first_no_show_subject");
        if( var != null && var.length() != 0 )
            msg_subject_1 = var;

        var = req.getParameter("second_no_show_subject");
        if( var != null && var.length() != 0 )
            msg_subject_2 = var;

        var = req.getParameter("no_show_subject");
        if( var != null && var.length() != 0 )
            msg_subject_3 = var;

        var = req.getParameter("show_approval_ent");
        if (var != null && var.length() > 0) {
            show_approval_ent_only = Boolean.valueOf(var).booleanValue();
        } else {
            show_approval_ent_only = false;
        }

        /* Used in item accreditation */
        var = req.getParameter("usr_ent_id");
        if( var != null && var.length() > 0){
            try{
                usr_ent_id = Integer.parseInt(var);
            }catch( NumberFormatException e ) {
                usr_ent_id = 0 ;                
            }
        }
        var = req.getParameter("ict_id_list");
        if( var != null && var.length() != 0 )
            ict_id_list = String2long(split(var, "~"));
        else
            ict_id_list = null;
            
        var = req.getParameter("icv_value_list");
        if( var != null && var.length() != 0 )
            icv_value_list = split(var, "~");
        else
            icv_value_list = null;        

        var = req.getParameter("download");
        if( var != null && var.length() != 0 )
            download = Boolean.valueOf(var).booleanValue();
        else
            download = false;
        
        var =req.getParameter("user_code");
        if(var!=null&&var.length()!=0)
        	user_code=var;
        else
        	user_code=null;        
        return;
        

    }

     public void mote(String clientEnc, String env_encoding){
        mote = null;
        String var;
        String prmNm;

        prmNm = "due_date";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.due_date = Timestamp.valueOf(var);
        }

        prmNm = "level1_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.level1_ind = Integer.parseInt(var);
        }

        prmNm = "level2_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.level2_ind = Integer.parseInt(var);
        }

        prmNm = "level3_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.level3_ind = Integer.parseInt(var);
        }

        prmNm = "level4_ind";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.level4_ind = Integer.parseInt(var);
        }

        prmNm = "participant_target";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.participant_target = var;
        }

        prmNm = "rating_target";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.rating_target = var;
        }

        prmNm = "cost_target";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.cost_target = var;
        }

        prmNm = "time_target";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            mote.time_target = var;
        }

        String plan_xml_resource = "";

        prmNm = "plan_xml";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {
            if(mote == null) {
                mote = new Mote();
            }
            try {
                plan_xml_resource = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
            catch (cwException e) {
                plan_xml_resource = "<mote_plan><resource_list></resource_list></mote_plan>";
            }
        }

        String plan_desc_xml = "";

        prmNm = "plan_desc_xml";
        var = (bMultiPart) ? multi.getParameter(prmNm) : req.getParameter(prmNm);
        if (var != null && var.length() > 0) {

            if(mote == null) {
                mote = new Mote();
            }
            try {
                plan_desc_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }
            catch (cwException e) {
                plan_desc_xml = "<mote_plan_desc></mote_plan_desc>";
            }
        }

        if (mote != null) {
            Perl5Util perl = new Perl5Util();
            mote.plan_xml = perl.substitute("s#&(?!(amp;|gt;|lt;|\\#))#&amp;#ig", plan_desc_xml + plan_xml_resource);
        }
    }

    public void messaging(String clientEnc, String env_encoding)
        throws UnsupportedEncodingException {

        String var = null;
        var = req.getParameter("ent_ids");
        if (var != null && var.length() > 0) {
            ent_ids = var;
        }

        var = req.getParameter("cc_ent_ids");
        if (var != null && var.length() > 0) {
            cc_ent_ids = var;
        }

        var = req.getParameter("url_redirect_param");
        if (var != null && var.length() > 0) {
            url_redirect_param = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        }

        return;
    }

    public static float to2decPt(String var) {
        if(var.indexOf(".")!= -1) {
            if(var.length() > var.indexOf(".") + 3)
                return (Float.valueOf(var.substring(0, var.indexOf(".")+3))).floatValue();
        }

        return (Float.valueOf(var)).floatValue();
    }

    public void LearningSoln() {
        String var;

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            itm_id = Integer.parseInt(var);
        }
        else
            itm_id = 0;

        var = req.getParameter("usr_ent_id");
        if (var != null && var.length() > 0) {
            usr_ent_id = Integer.parseInt(var);
        }
        else
            usr_ent_id = 0;

        var = req.getParameter("period_id");
        if (var != null && var.length() > 0) {
            period_id = Integer.parseInt(var);
        }
        else
            period_id = 0;

        var = req.getParameter("soln_type");
        if (var != null && var.length() > 0) {
            v_soln_type = cwUtils.splitToVecString(var, "~");
        } else {
            v_soln_type = null;
        }

        var = req.getParameter("item_type");
        if (var != null && var.length() > 0) {
            v_itm_type = cwUtils.splitToVecString(var, "~");
        } else {
            v_itm_type = null;
        }

        var = req.getParameter("item_lst");
        if (var != null && var.length() > 0) {
            v_itm_lst = cwUtils.splitToVec(var, "~");
        } else {
            v_itm_lst = null;
        }

        var = req.getParameter("period_lst");
        if (var != null && var.length() > 0) {
            v_period_lst = cwUtils.splitToVec(var, "~");
        } else {
            v_period_lst = null;
        }

        var = req.getParameter("pgm_id");
        if (var != null && var.length() > 0) {
            pgm_id = Integer.parseInt(var);
        }
        else
            pgm_id = 0;

        var = req.getParameter("pgm_run_id");
        if (var != null && var.length() > 0) {
            pgm_run_id = Integer.parseInt(var);
        }
        else
            pgm_run_id = 0;

        var = req.getParameter("targeted_item_apply_method_lst");
        if (var != null && var.length() > 0) {
            targeted_itm_apply_method_lst = split(var, "~");
        } else {
            targeted_itm_apply_method_lst = null;
        }

        // [2002-07-31 : Chris]
        var = req.getParameter("all_ind");
        if (var != null && var.length() > 0) {
            all_ind = Boolean.valueOf(var).booleanValue();
        } else {
            all_ind = false;
        }
        
        var = req.getParameter("include_targeted_itm");
        if (var != null && var.length() > 0) {
            include_targeted_itm = Boolean.valueOf(var).booleanValue();
        } else {
            include_targeted_itm = false;
        }
        
        //add by Tim
        var = req.getParameter("viewer_ent_id");
        if (var != null && var.length() > 0) {
            viewer_ent_id = Integer.parseInt(var);
        }
        else
            viewer_ent_id = 0;

        var = req.getParameter("viewer_role");
        if (var != null && var.length() > 0) {
            viewer_role = var;
        }

        var = req.getParameter("order_by");
        if ( var!= null) {
            order_bys = split(var, "~");
        }

        var = req.getParameter("sort_by");
        if (var != null && var.length() > 0) {
            sort_bys = split(var, "~");
        }
        
    }

    public void rating(String clientEnc, String env_encoding){
        String var = null;

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            itm_id = Integer.parseInt(var);
        }
        else
            itm_id = 0;

        var = req.getParameter("mod_id");
        if (var != null && var.length() > 0) {
            mod_id = Integer.parseInt(var);
        }
        else{
            mod_id = 0;
        }

        var = req.getParameter("rate");
        if ( var!= null && var.length()!= 0 ){
            try{
                rate = Float.valueOf(var).floatValue();
            } catch (NumberFormatException e) {
                rate = 0;
            }
        }else{
            rate = 0;
        }

        var = req.getParameter("rate_range_xml");
        if ( var!= null && var.length()!= 0 ){
            try{
                rate_range_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }catch (cwException e){
                rate_range_xml = null;
            }
        }
        var = req.getParameter("rate_q_xml");
        if ( var!= null && var.length()!= 0 ){
            try{
                rate_q_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            }catch (cwException e){
                rate_q_xml = null;
            }
        }
        
        var = req.getParameter("ent_id");
        if( var != null && var.length() > 0 )
            ent_id = Long.parseLong(var);        
    }

    Timestamp pagetime;
    int page_size;
    public void notifyStatus() {
        String var;
        /*
        var = req.getParameter("sort_by");
        if (var != null && var.length() > 0)
            sort_by = var;
        else
            sort_by = null;

        var = req.getParameter("order_by");
        if (var != null && var.length() > 0)
            order_by = var;
        else
            order_by = null;

        var = req.getParameter("pagetime");
        if (var != null && var.length() > 0) {
            pagetime = Timestamp.valueOf(var);
        }
        else
            pagetime = null;


        var = req.getParameter("page_size");
        if (var != null && var.length() > 0) {
            page_size = Integer.parseInt(var);
        }
        else
            page_size = 0;

        var = req.getParameter("cur_page");
        if (var != null && var.length() > 0) {
            cur_page = Integer.parseInt(var);
        }
        else
            cur_page = 0;
        */

        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            itm_id = Long.parseLong(var);
        }
        else
            itm_id = 0;

    }

    aeXMessage aeXmsg;
    String[] target_group;
    String msg_subject;
    String msg_body;
    boolean url_link_ind;
    boolean show_prereq_ind;
    
	public void course_notify(String clientEnc, String env_encoding)
		throws UnsupportedEncodingException { 
		aeXmsg = new aeXMessage();
		
		String var = null;
		var = req.getParameter("app_id");
		if( var != null && var.length() != 0 ) {
			aeXmsg.app_id = Long.parseLong(var);
		} else {
			aeXmsg.app_id = 0;
		}
	}

    public void notify(String clientEnc, String env_encoding)
        throws UnsupportedEncodingException {

        String var = null;

        var = req.getParameter("msg_subject");
        if( var != null && var.length() > 0 )
            msg_subject = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);//var;

        var = req.getParameter("msg_body");
        if( var != null && var.length() > 0 )
            msg_body = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);//var;


        aeXmsg = new aeXMessage();

        String _group;
        var = req.getParameter("target_group");
        if( var != null && var.length() > 0 ) {
            _group = var;
            target_group = cwUtils.splitToString(_group, "~");
        }

        var = req.getParameter("cur_timestamp");
        if (var != null && var.length() > 0) {
            cur_timestamp = Timestamp.valueOf(var);
        }
        else
            cur_timestamp = null;

        var = req.getParameter("id");
        if (var != null && var.length() > 0) {
            aeXmsg.id = Long.parseLong(var);
        } else {
            aeXmsg.id = 0;
        }

        var = req.getParameter("id_type");
        if( var != null && var.length() != 0 ) {
            aeXmsg.id_type = var;
        }


        var = req.getParameter("ent_id");
        if( var != null && var.length() != 0 ) {
            aeXmsg.ent_id = Long.parseLong(var);
        } else
            aeXmsg.ent_id = 0;

        var = req.getParameter("action_taker_ent_id");
        if( var != null && var.length() != 0 ) {
            aeXmsg.action_taker_ent_id = Long.parseLong(var);
        } else
            aeXmsg.action_taker_ent_id = 0;

        var = req.getParameter("sender_id");
        if( var != null && var.length() != 0 )
            aeXmsg.sender_id = var;


        var = req.getParameter("ent_ids");
        if( var != null && var.length() != 0 )
            ent_ids = var;

        var = req.getParameter("cc_ent_ids");
        if( var != null && var.length() != 0 )
            cc_ent_ids = var;
        
        var = req.getParameter("cc_email_address");
        if( var != null && var.length() != 0 )
        	cc_email_address = var;

        var = req.getParameter("bcc_ent_ids");
        if (var != null && var.length() > 0) {
            bcc_ent_ids = var;
        }

        var = req.getParameter("app_id");
        if( var != null && var.length() != 0 ) {
            aeXmsg.app_id = Long.parseLong(var);
        } else
            aeXmsg.app_id = 0;

        var = req.getParameter("url_redirect");
        if( var != null && var.length() != 0 )
            url_redirect = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);


        var = req.getParameter("msg_type");
        if( var != null && var.length() != 0 )
            aeXmsg.msg_type = var;

        var = req.getParameter("reply_to");
        if( var != null && var.length() > 0 )
            aeXmsg.reply_to = var;
            
        var = req.getParameter("app_process_status");
        if (var != null && var.length() > 0)
            app_process_status = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
        else
            app_process_status = null;

        var = req.getParameter("url_link_ind");
        if (var != null && var.equalsIgnoreCase("FALSE")) {
            url_link_ind = false;
        }else {
            url_link_ind = true;
        }
        
        var = req.getParameter("show_prereq_ind");
        if (var != null && var.equalsIgnoreCase("TRUE")) {
            show_prereq_ind = true;
        }else {
            show_prereq_ind = false;
        }
        
        var = req.getParameter("ent_id_lst");
        if ( var!= null && var.length()!= 0 ) {
            ent_id_lst = String2long(split(var, "~"));
        } else {
            ent_id_lst = null;
        }

        return;
    }
    
    
    
    public void itmCredit(String clientEnc, String env_encoding)
        throws UnsupportedEncodingException {
            
            String var = null;
            
            var = req.getParameter("itm_id");
            if( var != null && var.length() != 0 ) {
                itm_id = Long.parseLong(var);
            } else 
                itm_id = 0;
            
            var = req.getParameter("type");
            if( var != null && var.length() != 0 ) 
                credit_type = split(var, "~");
            
            var = req.getParameter("subtype");
            if( var != null && var.length() != 0 ) 
                credit_subtype = split(var, "~");
            
            var = req.getParameter("sort_col");
            if( var != null && var.length() != 0 )
                sort_by = var;

            var = req.getParameter("sort_order");
            if( var != null && var.length() != 0 )
                order_by = var;
                
            return;
            
        }
    
    
    Vector icv_id_vec;
    Vector ict_id_vec;
    Vector icv_val_vec;
   
    public void itmCreditValue(String clientEnc, String env_encoding)
        throws UnsupportedEncodingException {
            
            String var = null;
            dbFig = new DbFigure();
            
            var = req.getParameter("itm_id");
            if( var != null && var.length() != 0 ) {
                itm_id = Long.parseLong(var);
            } else 
                itm_id = 0;

            
            var = req.getParameter("icv_ict_id");
            if( var != null && var.length() != 0 ) {
                dbFig.fig_fgt_id = Long.parseLong(var);
            } else 
                dbFig.fig_fgt_id = 0;

            
            var = req.getParameter("icv_id");
            if( var != null && var.length() != 0 ) {
                dbFig.fig_id = Long.parseLong(var);
            } else 
                dbFig.fig_id = 0;
                        
            
            var = req.getParameter("icv_value");
            if( var != null && var.length() != 0 ) {
                dbFig.fig_value = Float.parseFloat(var);
            } else 
                dbFig.fig_value = 0;
            /*
            var = req.getParameter("icv_eff_start_datetime");
            if( var != null && var.length() != 0 ) 
                dbIcv.icv_eff_start_datetime = Timestamp.valueOf(var);
            else
                dbIcv.icv_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);

            var = req.getParameter("icv_eff_end_datetime");
            if( var != null && var.length() != 0 ) 
                dbIcv.icv_eff_end_datetime = Timestamp.valueOf(var);
            else
                dbIcv.icv_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
            */
            icv_id_vec = new Vector();
            var = req.getParameter("icv_id_lst");
            if( var != null && var.length() != 0 )
                icv_id_vec = cwUtils.splitToVec(var, "~");
/*
            var = req.getParameter("icv_code");
            if( var != null && var.length() != 0 )
                dbIcv.icv_code = var;
*/

            ict_id_vec = new Vector();
            var = req.getParameter("ict_id_lst");
            if( var != null && var.length() != 0 )
                ict_id_vec = cwUtils.splitToVec(var, "~");

            icv_val_vec = new Vector();
            var = req.getParameter("icv_value_lst");
            Vector vec = new Vector();
            if( var != null && var.length() != 0 )
                vec = cwUtils.splitToVecString(var, "~");
            for(int i=0; i<vec.size(); i++)
                icv_val_vec.addElement(new Float((String)vec.elementAt(i)));
            return;

        }
        
    //parameters needed in setup JI
    public void jiMsg() {
        String var;

        var = req.getParameter("ji_msg_id");
        if (var != null && var.length() > 0) {
                ji_msg_id = Long.parseLong(var);
        }
        var = req.getParameter("ji_reminder_msg_id");
        if (var != null && var.length() > 0) {
                ji_reminder_msg_id = Long.parseLong(var);
        }
        var = req.getParameter("ji_target_datetime");
        if (var != null && var.length() > 0) {
                ji_target_datetime = Timestamp.valueOf(var);
        }
        var = req.getParameter("ji_reminder_target_datetime");
        if (var != null && var.length() > 0) {
                ji_reminder_target_datetime = Timestamp.valueOf(var);
        }
        
        var = req.getParameter("reminder_msg_subject");
        if (var != null && var.length() > 0) {
                reminder_msg_subject = var;
        }
        
        var = req.getParameter("reminder_no_change");
        if (var != null && var.length() > 0) {
               reminder_no_change = var;
        }
        
        var = req.getParameter("ji_no_change");
        if (var != null && var.length() > 0) {
               ji_no_change = var;
        }
    }
    
    public void ilsRequest(String clientEnc, String env_encoding) throws UnsupportedEncodingException {
        String var = null;
        Long varl;
        int vari;
        
        var = this.getParam("ils_act_type");
        if(var != null && var.length() > 0){
            ils_act_type = var;
        }
        
        var = this.getParam("ils_id");
        if(var != null && var.length() > 0){
            try {
                ils_id = Long.parseLong(var);
            }catch(NumberFormatException e){
                ils_id = 0;
            }
        } else
            ils_id = 0;
        
        var = this.getParam("itm_id");
        if(var != null && var.length() > 0){
            try {
                itm_id = Long.parseLong(var);
            }catch(NumberFormatException e){
                itm_id = 0;
            }
        } else
            itm_id = 0;
            
        itmLessonReq = new aeItemLesson();
        itmLessonReq.ils_itm_id = itm_id;
        itmLessonReq.ils_id = ils_id;
        
        if(ils_act_type.equalsIgnoreCase("set_date_save")){
            var = this.getParam("day_seq");
            String day_seq;
            String tmp_str;
            int pl;
            if(var != null && var.length() > 0){
                day_seq = var.trim();
            }else{
                day_seq = "";
            }
            
            if(day_seq.length() > 0){
                String tmpDay, ipt_name;
                
                var = this.getParam("ipt_name_content");
                if(var != null && var.length() > 0){
                    ipt_name = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
                }else{
                    ipt_name = "";
                }
                ils_day_date_Req = new Hashtable();
                if (day_seq.length() > 0){
                	String[] days = cwUtils.splitToString(day_seq, ",");
                	for (int i = 0; i < days.length; i++) {
	                    var = this.getParam(ipt_name + days[i]);
	                    if(var != null && var.length() > 0){
	                        ils_day_date_Req.put(days[i], var);
	                    }else{
	                        ils_day_date_Req.put(days[i], "");
	                    }
                	}
                }
            }
        }
        
        if(ils_act_type.equalsIgnoreCase("delete")){
            var = this.getParam("upd_timestamp");
            if(var != null && var.length() > 0){
                try{
                    itmLessonReq.ils_update_timestamp = Timestamp.valueOf(var);
                }catch(IllegalArgumentException e){
                    itmLessonReq.ils_update_timestamp = null;
                }
            }else
            itmLessonReq.ils_update_timestamp = null;
        }

        if(ils_act_type.equalsIgnoreCase("save")){
            var = this.getParam("ipt_day");
            if(var != null && var.length() > 0){
                try {
                    vari = Integer.parseInt(var);            
                }catch(NumberFormatException e){
                    vari = 0;
                }                               
            }else
                vari = 0;
            itmLessonReq.ils_day = vari;
            
            var = this.getParam("ipt_start_h");
            if(var != null && var.length() > 0){
                try {
                    vari = Integer.parseInt(var);
                }catch(NumberFormatException e){
                    vari = 0;
                }               
            }else
                vari = 0;
            itmLessonReq.start_h = vari;
            
            var = this.getParam("ipt_start_m");
            if(var != null && var.length() > 0){
                try{
                    vari = Integer.parseInt(var);
                }catch(NumberFormatException e){
                    vari = 0;
                }
            }else
                vari = 0;
            itmLessonReq.start_m = vari;
                
            var = this.getParam("ipt_end_h");
            if(var != null && var.length() > 0){
                try{
                    vari = Integer.parseInt(var);
                }catch(NumberFormatException e){
                    vari = 0;
                }
            }else
                vari = 0;
            itmLessonReq.end_h = vari;
                        
            var = this.getParam("ipt_end_m");
            if(var != null && var.length() > 0){
                try{
                    vari = Integer.parseInt(var);
                }catch(NumberFormatException e){
                    vari = 0;
                }
            }else
                vari = 0;
            itmLessonReq.end_m = vari;
            
            var = this.getParam("ipt_lesson_place");
            if(var != null && var.length() > 0){
                ;
            }else
                var = "";   
            itmLessonReq.ils_place =dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            
            var = this.getParam("ipt_lesson_name");
            if(var != null && var.length() > 0){
                ;
            }else
                var = "";     
            itmLessonReq.ils_title = dbUtils.unicodeFrom(var, clientEnc, env_encoding, false);
            
            var = this.getParam("upd_timestamp");
            if(var != null && var.length() > 0){
                try{
                    itmLessonReq.ils_update_timestamp = Timestamp.valueOf(var);
                }catch(IllegalArgumentException e){
                    itmLessonReq.ils_update_timestamp = null;
                }
            }else
            itmLessonReq.ils_update_timestamp = null;
            
            var = this.getParam("ils_date");
            if(var != null && var.length() > 0){
                try{
                    itmLessonReq.ils_date = cwUtils.parse(var.toString());
                }catch(ParseException e){
                    itmLessonReq.ils_date = null;
                }
            }
            
            var = this.getParam("ils_qiandao");
            if(var != null && var.length() > 0){
                try {
                    vari = Integer.parseInt(var);            
                }catch(NumberFormatException e){
                    vari = 0;
                }                               
            }else{
                vari = 0;
            }
        	itmLessonReq.ils_qiandao = vari;
        	
            var = this.getParam("ils_qiandao_chidao");
            if(var != null && var.length() > 0){
                try {
                    vari = Integer.parseInt(var);            
                }catch(NumberFormatException e){
                    vari = 0;
                }                               
            }else{
                vari = 0;
            }
        	itmLessonReq.ils_qiandao_chidao = vari;
            
            var = this.getParam("ils_qiandao_queqin");
            if(var != null && var.length() > 0){
                try {
                    vari = Integer.parseInt(var);            
                }catch(NumberFormatException e){
                    vari = 0;
                }                               
            }else{
                vari = 0;
            }
        	itmLessonReq.ils_qiandao_queqin = vari;
            
            var = this.getParam("ils_qiandao_youxiaoqi");
            if(var != null && var.length() > 0){
                try {
                    vari = Integer.parseInt(var);            
                }catch(NumberFormatException e){
                    vari = 0;
                }                               
            }else{
                vari = 0;
            }
        	itmLessonReq.ils_qiandao_youxiaoqi = vari;
            	
            
            
        }
    }
    public void itmRequirement(String clientEnc, String env_encoding) {
        String var = null;

        var = req.getParameter("itm_id");
        if( var != null && var.length() > 0) {
            try {
                itrItmId = Long.parseLong(var);
            } catch (NumberFormatException e) {
                itrItmId = 0;
            }
        }

        var = req.getParameter("itr_order");
        if(var != null && var.length() > 0) {
            try {
                itrOrder = Long.parseLong(var);
            } catch (NumberFormatException e) {
                itrOrder = 0;
            }
        }
        
        var = req.getParameter("upd_time");
        if(var !=  null && var.length()>0){
        	lastUpdTime = Timestamp.valueOf(var);
        }
        
        var = req.getParameter("req_type");
        if (var != null && var.length() > 0) {
                itrRequirementType = var;
        }

        var = req.getParameter("req_subtype");
        if (var != null && var.length() > 0) {
                itrRequirementSubtype = var;
        }

        var = req.getParameter("req_restriction");
        if (var != null && var.length() > 0) {
                itrRequirementRestriction = var;
        }

        var = req.getParameter("req_due_date");
        if( var != null && var.length() > 0) {
            itrRequirementDueDate = Timestamp.valueOf(var);
        }

        var = req.getParameter("appn_footnote_ind");
        if(var != null && var.length() > 0) {
            try {
                itrAppnFootnoteInd = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itrAppnFootnoteInd = 0;
            }
        }
        
        var = req.getParameter("cond_type");
        if (var != null && var.length() > 0) {
                itrConditionType = var;
        }

        var = req.getParameter("cond_rule");
        if (var != null && var.length() > 0) {
                itrConditionRule = var;
        }

        var = req.getParameter("appn_footnote_ind");
        if(var != null && var.length() > 0) {
            try {
                itrAppnFootnoteInd = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itrAppnFootnoteInd = 0;
            }
        }
        
        var = req.getParameter("proc_exec_datetime");
        if (var != null && var.length() > 0) {
            itrProcExecuteTimestamp = Timestamp.valueOf(var);
        }

        var = req.getParameter("req_itm_id_lst");
        if (var!= null && var.length()!= 0 )
            reqItmId = String2long(split(var, "~"));
        else
            reqItmId = new long[0];

        var = req.getParameter("req_ent_lst");
        if (var!= null && var.length()!= 0 )
            reqEntLst = split(var, "~");

        var = req.getParameter("req_operator");
        if (var != null && var.length() > 0) {
            reqOperator = var;
        }
        
        var = req.getParameter("pos_actn_type");
        if (var != null && var.length() > 0) {
            posIatType = var;
        }
        
        var = req.getParameter("pos_attn_status");
        if (var != null && var.length() > 0) {
            posIaaToAttStatus = var;
        }

        var = req.getParameter("neg_actn_type");
        if (var != null && var.length() > 0) {
            negIatType = var;
        }
        
        var = req.getParameter("neg_attn_status");
        if (var != null && var.length() > 0) {
            negIaaToAttStatus = var;
        }
        return;
    }
	
	public void saveGetItemSubmittedParams(String clientEnc, String env_encoding) 
		throws UnsupportedEncodingException, cwException {
			HttpServletRequest httpReq = (HttpServletRequest) req;
			HttpSession sess = httpReq.getSession();
			//获取提交的参数名称
			Enumeration enumeration = (bMultiPart) ? multi.getParameterNames() : httpReq.getParameterNames();//有两种方式的提交，一种的直接表单提交，一种上传文件的表单提交
			Hashtable h_params = new Hashtable();
			String param_name = null;
			String param_value[] = null;
			while(enumeration.hasMoreElements()){
				param_name = (String) enumeration.nextElement();
				param_value = (bMultiPart) ? multi.getParameterValues(param_name) : httpReq.getParameterValues(param_name);
				for(int i=0; i<param_value.length; i++) {
					param_value[i] = cwUtils.unicodeFrom(param_value[i], clientEnc, env_encoding, false);
				}
				h_params.put(param_name, param_value);
			}
			//将参数设置到Session中
			sess.setAttribute("GET_ITEM_SUBMITTED_PARAMS", h_params);
			return;
		}
	public void saveGetItemSubmittedParams(String clientEnc, String env_encoding,String sessKey)  
	throws UnsupportedEncodingException, cwException {
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession sess = httpReq.getSession();
		Enumeration enumeration = (bMultiPart) ? multi.getParameterNames() : httpReq.getParameterNames();
		Hashtable h_params = new Hashtable();
		String param_name = null;
		String param_value[] = null;
		while(enumeration.hasMoreElements()){
			param_name = (String) enumeration.nextElement();
			param_value = (bMultiPart) ? multi.getParameterValues(param_name) : httpReq.getParameterValues(param_name);
			for(int i=0; i<param_value.length; i++) {
				param_value[i] =param_value[i]; 
			}
			h_params.put(param_name, param_value);
		}
		sess.setAttribute(sessKey, h_params);
		return;
	}
	public void clearGetItemSubmittedParams(){
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession sess = httpReq.getSession();
		sess.removeAttribute("GET_ITEM_SUBMITTED_PARAMS");
		return;
	}
	
	public void getGetItemSubmittedParams(String sessKey) {
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession sess = httpReq.getSession();
		if(sessKey ==null ||sessKey.length()==0){
			sessKey="GET_ITEM_SUBMITTED_PARAMS";
		}
		Hashtable h_params = (Hashtable) sess.getAttribute(sessKey);
		if( h_params == null ) {
			return; 
		}
		
		itm = new aeItem();
		itmExtension = new aeItemExtension();
		String[] var;
		String prmNm;

		prmNm = "apply_now_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
			if( var[0] != null && var[0].length() > 0) {
				try{
					apply_now_ind = (new Boolean(var[0])).booleanValue();
				}catch( ClassCastException e ) {
					apply_now_ind = false;
				}
			}
		}
		
		prmNm = "ji_view";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			try{
				ji_view = (new Boolean(var[0])).booleanValue();
			}catch( ClassCastException e ) {
				ji_view = false;
			}
		}
		}
		
		//item id list
		prmNm = "itm_id_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if ( var[0]!= null && var[0].length()!= 0 )
			itm_id_lst = String2long(split(var[0], "~"));
		else
			itm_id_lst = new long[0];
		}

		//item tree node parent id
		prmNm = "tnd_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				tnd_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				tnd_id = 0;
			}
		} else {
			tnd_id = 0;
		}
		}
		
		//item tree node parent id
		prmNm = "tnd_parent_tnd_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				tnd_parent_tnd_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				tnd_parent_tnd_id = 0;
			}
		} else {
			tnd_parent_tnd_id = 0;
		}
		}
		
		//current template id selected
		prmNm = "cur_tpl_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				cur_tpl_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				cur_tpl_id = 0;
			}
		} else {
			cur_tpl_id = 0;
		}
		}
		
		//item template id
		prmNm = "itm_tpl_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				itm_tpl_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				itm_tpl_id = 0;
			}
		} else {
			itm_tpl_id = 0;
		}
		}
		
		//workflow template id
		prmNm = "wrk_tpl_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				wrk_tpl_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				wrk_tpl_id = 0;
			}
		} else {
			wrk_tpl_id = 0;
		}
		}
		
		//appnform template id
		prmNm = "app_tpl_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				app_tpl_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				app_tpl_id = 0;
			}
		} else {
			app_tpl_id = 0;
		}
		}
		
		//template type
		prmNm = "tpl_type";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			tpl_type = var[0];
			itm.tpl_type = var[0];
		} else {
			tpl_type = "";
			itm.tpl_type = "";
		}
		}
		
		//item id
		prmNm = "itm_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				itm.itm_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				itm.itm_id = 0;
			}
		} else {
			itm.itm_id = 0;
		}
		}
		
		//old item id
		prmNm = "old_itm_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				old_itm_id = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				old_itm_id = 0;
			}
		} else {
			old_itm_id = 0;
		}
		}
		//get aeItem column values
		this.vClobColName = new Vector();
		this.vClobColValue = new Vector();
		this.vColName = new Vector();
		this.vColType = new Vector();
		this.vColValue = new Vector();
		this.vExtensionColName = new Vector();
        this.vExtensionColType = new Vector();
        this.vExtensionColValue = new Vector();

		//item capacity
		prmNm = "itm_capacity";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm.itm_capacity = Integer.parseInt(var[0]);
			this.vColName.addElement("itm_capacity");
			this.vColType.addElement(DbTable.COL_TYPE_LONG);
			this.vColValue.addElement(new Long(itm.itm_capacity));
		}
		}
		
		//item min capacity
		prmNm = "itm_min_capacity";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm.itm_min_capacity = Integer.parseInt(var[0]);
			this.vColName.addElement("itm_min_capacity");
			this.vColType.addElement(DbTable.COL_TYPE_LONG);
			this.vColValue.addElement(new Long(itm.itm_min_capacity));
		}
		}
		//item unit
		prmNm = "itm_unit";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
//			  itm.itm_unit = Integer.parseInt(var[0]);
			itm.itm_unit = Float.parseFloat(var[0]);
			this.vColName.addElement("itm_unit");
			this.vColType.addElement(DbTable.COL_TYPE_FLOAT);
			this.vColValue.addElement(new Float(itm.itm_unit));
		}
		}
		
		//item unit
		prmNm = "itm_can_qr_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
//			  itm.itm_unit = Integer.parseInt(var[0]);
			itm.itm_can_qr_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_can_qr_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_can_qr_ind));
		}
		}
		//item xml
		prmNm = "itm_xml";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			String xml = var[0];
			Perl5Util perl = new Perl5Util();
			itm.itm_xml = perl.substitute("s#&(?!(amp;|gt;|lt;|\\#))#&amp;#ig", xml);
			this.vClobColName.addElement("itm_xml");
			this.vClobColValue.addElement(itm.itm_xml);
		}
		}
		//item title
		prmNm = "itm_title";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null ) {
			if (var[0].length() > 0)
				itm.itm_title = var[0];
			else
				itm.itm_title = null;
            
			this.vColName.addElement("itm_title");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_title);
		}
		}
		//item type
		prmNm = "itm_type";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null ) {
			if (var[0].length() > 0)
				itm.itm_type = var[0];
			else
				itm.itm_type = null;
            
			this.vColName.addElement("itm_type");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_type);
		}
		}
		
		//item dummy type
		prmNm = "itm_dummy_type";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
			if (var[0] != null ) {
				if (var[0].length() > 0)
					itm.itm_dummy_type = var[0];
				else
					itm.itm_dummy_type = null;
			}
		}
		//item code
		prmNm = "itm_code";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			input_itm_code_ind = true;
			if (var[0].length() > 0)
				itm.itm_code = var[0];
			else
				itm.itm_code = null;
            
			this.vColName.addElement("itm_code");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_code);
		}
		}
        
        prmNm = "itm_blend_ind";
		var = ((String[]) h_params.get(prmNm));
        //var = req.getParameter("itm_run_ind");
		if( var != null && var.length > 0 ) {
			if(var[0] != null) {
				itm.itm_blend_ind = (new Boolean(var[0])).booleanValue();
				this.vColName.addElement("itm_blend_ind");
				this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
				this.vColValue.addElement(new Boolean(itm.itm_blend_ind));
			}
		}
        
        prmNm = "itm_exam_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
			if(var[0] != null) {
				itm.itm_exam_ind = (new Boolean(var[0])).booleanValue();
				this.vColName.addElement("itm_exam_ind");
				this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
				this.vColValue.addElement(new Boolean(itm.itm_exam_ind));
			}
		}
        
        prmNm = "itm_ref_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
			if(var[0] != null) {
				itm.itm_ref_ind = (new Boolean(var[0])).booleanValue();
				this.vColName.addElement("itm_ref_ind");
				this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
				this.vColValue.addElement(new Boolean(itm.itm_ref_ind));
			}
		}

        prmNm = "itm_integrated_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
			if(var[0] != null) {
				itm.itm_integrated_ind = (new Boolean(var[0])).booleanValue();
				this.vColName.addElement("itm_integrated_ind");
				this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
				this.vColValue.addElement(new Boolean(itm.itm_integrated_ind));
			}
		}
		
		//item code
		prmNm = "itm_status";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null ) {
			input_itm_status_ind = true;
			if (var[0].length() > 0)
				itm.itm_status = var[0];
			else
				itm.itm_status = null;
            
			this.vColName.addElement("itm_status");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_status);

		}
		}

		//appn start datetime
		prmNm = "itm_appn_start_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null ) {
			if (var[0].length() > 0)
				itm.itm_appn_start_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_appn_start_datetime = null;
            
			this.vColName.addElement("itm_appn_start_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_appn_start_datetime);
		}
		}
		//appn end datetime
		prmNm = "itm_appn_end_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null ) {
			if (var[0].length() > 0)
				itm.itm_appn_end_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_appn_end_datetime = null;
            
			this.vColName.addElement("itm_appn_end_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_appn_end_datetime);
		}
		}
		
		//item update timestamp
		prmNm = "itm_upd_timestamp";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm_in_upd_timestamp = Timestamp.valueOf(var[0]);
			//this.vColName.addElement("itm_upd_timestamp");
			//this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			//this.vColValue.addElement(itm.itm_upd_timestamp);
		}
		}
		
		//item eff start datetime
		prmNm = "itm_eff_start_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_eff_start_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_eff_start_datetime = null;
            
			this.vColName.addElement("itm_eff_start_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_eff_start_datetime);
		}
		}
		
		//item eff end datetime
		prmNm = "itm_eff_end_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_eff_end_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_eff_end_datetime = null;
            
			this.vColName.addElement("itm_eff_end_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_eff_end_datetime);
		}
		}
		
		//item upd timestamp list
		prmNm = "itm_upd_timestamp_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if ( var[0]!= null && var[0].length()!= 0 )
			itm_in_upd_timestamp_lst = String2Timestamp(split(var[0], "~"));
		else
			itm_in_upd_timestamp_lst = new Timestamp[0];
		}
		
		
		//item status list
		prmNm = "itm_status_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if ( var[0]!= null && var[0].length()!= 0 )
			itm_status_lst = split(var[0], "~");
		else
			itm_status_lst = new String[0];
		}
		
		//itm fee
		prmNm = "itm_fee";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm.itm_fee = Float.parseFloat(var[0]);
			this.vColName.addElement("itm_fee");
			this.vColType.addElement(DbTable.COL_TYPE_FLOAT);
			this.vColValue.addElement(new Float(itm.itm_fee));
		}
		}
		
		//item fee ccy
		prmNm = "itm_fee_ccy";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_fee_ccy = var[0];
			else
				itm.itm_fee_ccy = null;
            
			this.vColName.addElement("itm_fee_ccy");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_fee_ccy);
		}
		}
		
		//course desc
		prmNm = "cos_desc";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0)
			cos_desc = var[0];
		}
		
		//course license key
		prmNm = "cos_lic_key";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0)
			cos_lic_key = var[0];
		}
		
		//ext1
		prmNm = "ext1";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0)
			itm.itm_ext1 = var[0];
		}
		
		//order by of get_all_itm
		prmNm = "orderby";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			orderBy = var[0];
		}
		else
			orderBy = "";
		}
		
		prmNm = "sortorder";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			sortOrder = var[0];
		}
		else
			sortOrder = "";
		}

		//item details option
		prmNm = "dtl_opt";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				itm.details_option = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				itm.details_option = 0;
			}
		} else {
			itm.details_option = 0;
		}
		}
		
		// type of item access id
		prmNm = "access_type";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm.acc_type = var[0];
		} else {
			itm.acc_type = "";
		}
		}
		
		// item access id
		prmNm = "acc_id_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm.acc_id = split(var[0], "~");
		} else {
			itm.acc_id = new String[0];
		}
		}
		
		// type of view of the template
		prmNm = "view_type";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			tpl_view_type = var[0];
		} else {
			tpl_view_type = "";
		}
		}
		
		
		//item type
		prmNm = "ity_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			ity_id = var[0];
		} else {
			ity_id = "";
		}
		}
		
		
		//template view id
		prmNm = "tvw_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			tvw_id = var[0];
		} else {
			tvw_id = "";
		}
		}
		
		
		prmNm = "itm_create_run_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			itm.itm_create_run_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_create_run_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_create_run_ind));
		}
		}
		
		
		prmNm = "itm_create_session_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			itm.itm_create_session_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_create_session_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_create_session_ind));
		}
		}
		
		prmNm = "itm_run_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			itm.itm_run_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_run_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_run_ind));
		}
		}
		
		prmNm = "itm_session_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			itm.itm_session_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_session_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_session_ind));
		}
		}
		
		prmNm = "itm_apply_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			itm.itm_apply_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_apply_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_apply_ind));
		}
		}
		
		prmNm = "itm_qdb_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			itm.itm_qdb_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_qdb_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_qdb_ind));
		}
		}
		
		prmNm = "itm_auto_enrol_qdb_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			itm.itm_auto_enrol_qdb_ind = (new Boolean(var[0])).booleanValue();
			this.vColName.addElement("itm_auto_enrol_qdb_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_auto_enrol_qdb_ind));
		}
		}
		
		prmNm = "itm_version_code";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null ) {
			if (var[0].length() > 0)
				itm.itm_version_code = var[0];
			else
				itm.itm_version_code = null;
            
			this.vColName.addElement("itm_version_code");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_version_code);
		}
		}
		
		prmNm = "itm_person_in_charge";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_person_in_charge = var[0];
			else
				itm.itm_person_in_charge = null;
            
			this.vColName.addElement("itm_person_in_charge");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_person_in_charge);
		}
		}
		
		prmNm = "itm_apply_method";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_apply_method = var[0];
			else
				itm.itm_apply_method = null;
            
			this.vColName.addElement("itm_apply_method");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_apply_method);
		}
		}
		
		prmNm = "itm_life_status";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_life_status = var[0];
			else
				itm.itm_life_status = null;
            
			this.vColName.addElement("itm_life_status");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_life_status);
		}
		}
		
		//online content eff start datetime
		prmNm = "itm_content_eff_start_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_content_eff_start_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_content_eff_start_datetime = null;
            
			this.vColName.addElement("itm_content_eff_start_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_content_eff_start_datetime);
		}
		}
		
		prmNm = "itm_content_eff_end_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_content_eff_end_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_content_eff_end_datetime = null;
            
			this.vColName.addElement("itm_content_eff_end_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_content_eff_end_datetime);
		}
		}
		
		prmNm = "cos_eff_start_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_content_eff_start_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_content_eff_start_datetime = null;
            
			this.vColName.addElement("itm_content_eff_start_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_content_eff_start_datetime);
		}
		}
		

		//online content eff end datetime
		prmNm = "cos_eff_end_datetime";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_content_eff_end_datetime = Timestamp.valueOf(var[0]);
			else
				itm.itm_content_eff_end_datetime = null;
            
			this.vColName.addElement("itm_content_eff_end_datetime");
			this.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			this.vColValue.addElement(itm.itm_content_eff_end_datetime);
		}
		}
		
		prmNm = "itm_content_eff_duration";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_content_eff_duration = Integer.parseInt(var[0]);
			else
				itm.itm_content_eff_duration = 0;
            
			this.vColName.addElement("itm_content_eff_duration");
			this.vColType.addElement(DbTable.COL_TYPE_INT);
			this.vColValue.addElement(new Integer(itm.itm_content_eff_duration));
		}
		}
		
		//online content duration
		prmNm = "res_duration";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			res_duration = Float.parseFloat(var[0]);
		}
		}
		
		//catalog attachment tnd_id
		prmNm = "tnd_id_lst";
		var = ((String[]) h_params.get(prmNm));
		//var[0] = req.getParameter("tnd_id_lst");
		if( var != null && var.length > 0 ) {
		if ( var[0]!= null)
			tnd_id_lst = String2long(split(var[0], "~"));
		}
		
		//target entity group id list e.g. 1,2,3~22,33,44~123
		prmNm = "target_ent_group_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if ( var[0]!= null)
			target_ent_group_lst = split(var[0], "~");
		}
		
		prmNm = "comp_target_ent_group_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if ( var[0]!= null)
			comp_target_ent_group_lst = split(var[0], "~");
		}
       

		//item access list e.g. CMAN~128~256
		prmNm = "iac_id_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
			iac_id_lst = var;
		}


		prmNm = "cm_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if ( var[0]!= null)
			cm_lst = split(var[0], "~");
		}

		prmNm = "prev_version_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			prev_version_ind = Boolean.valueOf(var[0]).booleanValue();
		}
		else
			prev_version_ind = false;
		}
		
		prmNm = "show_run_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			try{
				show_run_ind = (new Boolean(var[0])).booleanValue();
			}catch( ClassCastException e ) {
				show_run_ind = false;
			}
		}
		}
		
		
		prmNm = "show_session_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			try{
				show_session_ind = (new Boolean(var[0])).booleanValue();
			}catch( ClassCastException e ) {
				show_session_ind = false;
			}
		}
		}
		
		
		prmNm = "show_attendance_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			try{
				show_attendance_ind = (new Boolean(var[0])).booleanValue();
			}catch( ClassCastException e ) {
				show_attendance_ind = false;
			}
		}
		}

		prmNm = "show_respon_run_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0) {
			try{
				show_respon_run_ind = (new Boolean(var[0])).booleanValue();
			}catch( ClassCastException e ) {
				show_respon_run_ind = false;
			}
		}
		}

		
		//ext1
		prmNm = "itm_ext1";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null ) {
			if (var[0].length() > 0)
				itm.itm_ext1 = var[0];
			else
				itm.itm_ext1 = null;
            
			this.vColName.addElement("itm_ext1");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_ext1);
		}
		}
		
		//item cancellation reason
		prmNm = "itm_cancellation_reason";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null) {
			if (var[0].length() > 0)
				itm.itm_cancellation_reason = var[0];
			else
				itm.itm_cancellation_reason = null;
            
			this.vColName.addElement("itm_cancellation_reason");
			this.vColType.addElement(DbTable.COL_TYPE_STRING);
			this.vColValue.addElement(itm.itm_cancellation_reason);
		}
		}
		
		//item cancel type
		prmNm = "rsv_cancel_type";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm.itm_cancellation_type = var[0];
		}
		}
         

		//item cancel message sender
		prmNm = "sender_ent_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			this.sender_ent_id = Integer.parseInt(var[0]);
		}
		}
		
		//item cancel message -- cc to approver indicator
		prmNm = "cc_to_approver_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			if(var[0].equals("true"))
				this.cc_to_approver_ind = true;
		}
		}
		
		//item cancel message approver rol_ext_id into array
		prmNm = "cc_to_approver_rol_ext_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			this.cc_to_approver_rol_ext_id = split(var[0], "~");
		}
		}
		
		//item cancel message approver rol_ext_ids
		prmNm = "cc_to_approver_rol_ext_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			this.cc_to_approver_rol_ext_ids = var[0];
		}
		}
		
		//item cancel message bcc list
		prmNm = "bcc_to";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			this.bcc_to = var[0];
		}
		}
		
		
		//rsv update timestamp
		prmNm = "rsv_upd_timestamp";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			rsv_upd_timestamp = Timestamp.valueOf(var[0]);
		}
		}
		
		//get lastest version indicator
		prmNm = "get_last";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			try {
				get_last = Integer.parseInt(var[0]);
			} catch (NumberFormatException e) {
				get_last = 0;
			}
		} else {
			get_last = 0;
		}
		}
		

		//message subject of item cancellation
		msg_subject = null;
		prmNm = "msg_subject";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			msg_subject = var[0];
		}
		}
		
		// cost center usergroup list e.g. 1~2~3~4
		prmNm = "cost_center_group_lst";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0]!= null)
			cost_center_group_lst = split(var[0], "~");
		}
		
		prmNm = "apply_method";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			apply_method = var[0];
		} else {
			apply_method = "";
		}
		}
		
		prmNm = "itm_retake_ind";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0 ){
			itm.itm_retake_ind = Boolean.valueOf(var[0]).booleanValue();
			this.vColName.addElement("itm_retake_ind");
			this.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vColValue.addElement(new Boolean(itm.itm_retake_ind));
		}
		}
		
		fgt_id_vec = new Vector();
		prmNm = "fgt_id_list";
		var = ((String[]) h_params.get(prmNm));        
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() != 0 )
			fgt_id_vec = cwUtils.splitToVec(var[0], "~");
		}
		
		fig_val_vec = new Vector();
		prmNm = "fig_val_list";
		var = ((String[]) h_params.get(prmNm));        
		if( var != null && var.length > 0 ) {
		Vector vec = new Vector();
		if( var[0] != null && var[0].length() != 0 )
			vec = cwUtils.splitToVecString(var[0], "~");
		for(int i=0; i<vec.size(); i++)
			fig_val_vec.addElement(new Float((String)vec.elementAt(i)));
		}
		
		prmNm = "approval_action";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if( var[0] != null && var[0].length() > 0 ){
			approval_action = var[0];
		}
		}
		
		//item application approvar type
		prmNm = "itm_app_approval_type";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			itm.itm_app_approval_type = var[0];
		} else {
			itm.itm_app_approval_type = null;
		}
		}

		prmNm = "stylesheet";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
		if (var[0] != null && var[0].length() > 0) {
			stylesheet = var[0];
		}else{
			stylesheet = null;
		}
		}
        //itm_tcr_id
        prmNm = "itm_tcr_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
	        if (var[0] != null && var[0].length() > 0) {
	            try {
	                itm.itm_tcr_id = Integer.parseInt(var[0]);
	            } catch (NumberFormatException e) {
	                itm.itm_tcr_id = 0;
	            }
	        } else{
	        	 itm.itm_tcr_id = 0;
	        }
		}else {
            itm.itm_tcr_id = 0;
        }
		
        prmNm = "plan_id";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
	        if (var[0] != null && var[0].length() > 0) {
	            try {
	                plan_id = Integer.parseInt(var[0]);
	            } catch (NumberFormatException e) {
	                plan_id = 0;
	            }
	        } else {
	            plan_id = 0;
	        }
		}else{
			plan_id=0;
		}
		//rsv update timestamp
		prmNm = "tpn_update_timestamp";
		var = ((String[]) h_params.get(prmNm));
		if( var != null && var.length > 0 ) {
			if (var[0] != null && var[0].length() > 0) {
				tpn_update_timestamp = Timestamp.valueOf(var[0]);
			}
		}
		
		 prmNm = "itm_desc";
	        var = ((String[]) h_params.get(prmNm));
	        //var = req.getParameter("itm_title");
	        if (var != null ) {
	            if (var.length > 0)
	                itm.itm_desc = var[0];
	            else
	                itm.itm_desc = null;
	            
	            this.vColName.addElement("itm_desc");
	            this.vColType.addElement(DbTable.COL_TYPE_STRING);
	            this.vColValue.addElement(itm.itm_desc);
	        }
		
		
		
		 prmNm = "itm_cfc_id";
	        var = ((String[]) h_params.get(prmNm));
	        if (var != null && var.length > 0) {
	            try {
	                itm.itm_cfc_id = Long.parseLong(var[0]);
	                this.vColName.addElement("itm_cfc_id");
	                this.vColType.addElement(DbTable.COL_TYPE_LONG);
	                this.vColValue.addElement(new Long(itm.itm_cfc_id));
	            } catch (NumberFormatException e) {
	                itm.itm_cfc_id = 0;
	                this.vColName.addElement("itm_cfc_id");
	                this.vColType.addElement(DbTable.COL_TYPE_LONG);
	                this.vColValue.addElement(new Long(itm.itm_cfc_id));
	            }
	        } else {
	            itm.itm_cfc_id = 0;
	            this.vColName.addElement("itm_cfc_id");
	            this.vColType.addElement(DbTable.COL_TYPE_LONG);
	            this.vColValue.addElement(new Long(itm.itm_cfc_id));
	        }

			// aeItemExtension
			prmNm = "ies_lang";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_lang = var[0];
			}
			this.vExtensionColName.addElement("ies_lang");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_lang);

			prmNm = "ies_objective";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_objective = var[0];
			}
			this.vExtensionColName.addElement("ies_objective");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_objective);

			prmNm = "ies_contents";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_contents = var[0];
			}
			this.vExtensionColName.addElement("ies_contents");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_contents);

			prmNm = "ies_duration";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_duration = var[0];
			}
			this.vExtensionColName.addElement("ies_duration");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_duration);

			prmNm = "ies_audience";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_audience = var[0];
			}
			this.vExtensionColName.addElement("ies_audience");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_audience);

			prmNm = "ies_prerequisites";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_prerequisites = var[0];
			}
			this.vExtensionColName.addElement("ies_prerequisites");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_prerequisites);

			prmNm = "ies_exemptions";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_exemptions = var[0];
			}
			this.vExtensionColName.addElement("ies_exemptions");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_exemptions);

			prmNm = "ies_remarks";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_remarks = var[0];
			}
			this.vExtensionColName.addElement("ies_remarks");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_remarks);

			prmNm = "ies_enroll_confirm_remarks";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_enroll_confirm_remarks = var[0];
			}
			this.vExtensionColName.addElement("ies_enroll_confirm_remarks");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_enroll_confirm_remarks);

			prmNm = "ies_schedule";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_schedule = var[0];
			}
			this.vExtensionColName.addElement("ies_schedule");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_schedule);

			prmNm = "url1";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_itm_ref_url_1 = var[0];
			}
			this.vExtensionColName.addElement("ies_itm_ref_url_1");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_1);

			prmNm = "url2";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_itm_ref_url_2 = var[0];
			}
			this.vExtensionColName.addElement("ies_itm_ref_url_2");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_2);
			
			prmNm = "url3";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_itm_ref_url_3 = var[0];
			}
			this.vExtensionColName.addElement("ies_itm_ref_url_3");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_3);

			prmNm = "url4";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_itm_ref_url_4 = var[0];
			}
			this.vExtensionColName.addElement("ies_itm_ref_url_4");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_4);

			prmNm = "url5";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				itmExtension.ies_itm_ref_url_5 = var[0];
			}
			this.vExtensionColName.addElement("ies_itm_ref_url_5");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
			this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_url_5);
/*
			if (isUploadFileModify("file1_name", "file1", clientEnc, env_encoding)) {
				itmExtension.ies_itm_ref_materials_1 = getUploadFileParam("file1_name", "file1", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_ref_materials_1");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_1);
			}

			if (isUploadFileModify("file2_name", "file2", clientEnc, env_encoding)) {
				itmExtension.ies_itm_ref_materials_2 = getUploadFileParam("file2_name", "file2", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_ref_materials_2");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_2);
			}

			if (isUploadFileModify("file3_name", "file3", clientEnc, env_encoding)) {
				itmExtension.ies_itm_ref_materials_3 = getUploadFileParam("file3_name", "file3", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_ref_materials_3");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_3);
			}

			if (isUploadFileModify("file4_name", "file4", clientEnc, env_encoding)) {
				itmExtension.ies_itm_ref_materials_4 = getUploadFileParam("file4_name", "file4", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_ref_materials_4");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_4);
			}

			if (isUploadFileModify("file5_name", "file5", clientEnc, env_encoding)) {
				itmExtension.ies_itm_ref_materials_5 = getUploadFileParam("file5_name", "file5", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_ref_materials_5");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_ref_materials_5);
			}

			if (isUploadFileModify("file001_name", "file001", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_1 = getUploadFileParam("file001_name", "file001", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_1");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_1);
			}

			if (isUploadFileModify("file002_name", "file002", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_2 = getUploadFileParam("file002_name", "file002", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_2");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_2);
			}

			if (isUploadFileModify("file003_name", "file003", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_3 = getUploadFileParam("file003_name", "file003", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_3");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_3);
			}

			if (isUploadFileModify("file004_name", "file004", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_4 = getUploadFileParam("file004_name", "file004", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_4");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_4);
			}

			if (isUploadFileModify("file005_name", "file005", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_5 = getUploadFileParam("file005_name", "file005", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_5");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_5);
			}

			if (isUploadFileModify("file006_name", "file006", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_6 = getUploadFileParam("file006_name", "file006", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_6");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_6);
			}

			if (isUploadFileModify("file007_name", "file007", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_7 = getUploadFileParam("file007_name", "file007", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_7");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_7);
			}

			if (isUploadFileModify("file008_name", "file008", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_8 = getUploadFileParam("file008_name", "file008", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_8");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_8);
			}

			if (isUploadFileModify("file009_name", "file009", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_9 = getUploadFileParam("file009_name", "file009", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_9");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_9);
			}

			if (isUploadFileModify("file010_name", "file010", clientEnc, env_encoding)) {
				itmExtension.ies_itm_rel_materials_10 = getUploadFileParam("file010_name", "file010", clientEnc, env_encoding);
				this.vExtensionColName.addElement("ies_itm_rel_materials_10");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_itm_rel_materials_10);
			}
*/
			prmNm = "ies_top_ind";
			var = ((String[]) h_params.get(prmNm));
			if (var != null && var.length > 0) {
				try {
					itmExtension.ies_top_ind = (new Boolean(var[0])).booleanValue();
				} catch (ClassCastException e) {
					itmExtension.ies_top_ind = false;
				}
			}
			this.vExtensionColName.addElement("ies_top_ind");
			this.vExtensionColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			this.vExtensionColValue.addElement(itmExtension.ies_top_ind);
			
			boolean ies_top_icon_upd_ind = false;
			prmNm = "ies_top_icon_del_ind";
			var = ((String[]) h_params.get(prmNm));
			if (var != null) {
				ies_top_icon_upd_ind = (new Boolean(var[0])).booleanValue();
			}

			prmNm = "ies_top_icon";
			var =((String[]) h_params.get(prmNm));
			if (var != null) {
				if (var.length > 0)
					itmExtension.ies_top_icon = var[0];
				else
					itmExtension.ies_top_icon = null;
			}
			if (ies_top_icon_upd_ind) {
				this.vExtensionColName.addElement("ies_top_icon");
				this.vExtensionColType.addElement(DbTable.COL_TYPE_STRING);
				this.vExtensionColValue.addElement(itmExtension.ies_top_icon);
			}
			
			prmNm = "ies_credit";
	        var = ((String[]) h_params.get(prmNm));
	        if (var != null && var.length > 0) {
	            itmExtension.ies_credit = Float.parseFloat(var[0]);            
	        }
	        if(itmExtension.ies_credit!=0){
	            itm.itm_diff_factor = itmExtension.ies_credit;
	        }
	        this.vExtensionColName.addElement("ies_credit");
	        this.vExtensionColType.addElement(DbTable.COL_TYPE_FLOAT);
	        this.vExtensionColValue.addElement(itmExtension.ies_credit);
	        
	        prmNm = "is_complete_del";
	        var = ((String[]) h_params.get(prmNm));
	        if (var != null && var.length > 0) {
	        	itm.is_complete_del = Boolean.valueOf(var[0]).booleanValue();
	        } else {
	        	itm.is_complete_del = false;
	        }
	        
	        prmNm = "is_new_cos";
	        var = ((String[]) h_params.get(prmNm));
	        if (var != null && var.length > 0) {
	        	itm.is_new_cos = Boolean.valueOf(var[0]).booleanValue();
	        } else {
	        	itm.is_new_cos = false;
	        }
		return;
	}


	public String getSubmittedParamsListXML(){
		Enumeration enumeration = (bMultiPart) ? multi.getParameterNames() : req.getParameterNames();
		String param_name = null;
		String[] param_value = null;
		StringBuffer xml = new StringBuffer(); 
		xml.append("<submitted_params_list>");
		while(enumeration.hasMoreElements()){
			param_name = (String) enumeration.nextElement();
			xml.append("<param name=\"").append(param_name).append("\">");
			param_value = (bMultiPart) ? multi.getParameterValues(param_name) : req.getParameterValues(param_name);
			for(int i=0; i<param_value.length; i++){
				xml.append("<value>")
							.append(cwUtils.esc4XML(param_value[i]))
							.append("</value>");
			}
			xml.append("</param>");
		}
		xml.append("</submitted_params_list>");		
		return xml.toString();
	}
	
	public String getParam(String prmName){
		String value = (bMultiPart) ? multi.getParameter(prmName) : req.getParameter(prmName);
		return value;
	}
	
	public Object getAttribute(String prmName){
		return req.getAttribute(prmName);
	}
	
	public List getParamWithItemCost(){
		List list = new ArrayList();
		DbItemCost obj = null;
		String prmName;
		String var;
		String budgetPre = "budget";
		String actualPre = "actual";
		String changtypePre = "change_type";
		String updtimePre = "updated_timestamp";
		String typeValue = "type_value";
		BigDecimal tmp_fee = null;
		long cost_type_num = 0;
		long itm_id = 0;
		int change_type = 0;
		int type_value = 0;
		
		prmName = "cost_type_num";
		var = getParam(prmName);
		if( var != null && var.length() > 0) {
			cost_type_num = Long.parseLong(var);
		}
		
		prmName = "itm_id";
		var = getParam(prmName);
		if( var != null && var.length() > 0) {
				   itm_id = Long.parseLong(var);
		}
		
		for(long i=1;i<=cost_type_num;i++){
			prmName = typeValue+i;
			var = getParam(prmName);
			if(var != null && var.length() > 0){
				type_value = Integer.parseInt(var);
			}
			
			obj = new DbItemCost(itm_id,type_value);
			prmName = budgetPre+type_value;
			var = getParam(prmName);
			if( var != null && var.length() > 0) {
				tmp_fee = new BigDecimal(var);
				obj.setIto_budget(tmp_fee);
			}
			//tmp_fee = null;
			prmName = actualPre+type_value;
			var = getParam(prmName);
			if( var != null && var.length() > 0) {
				tmp_fee = new BigDecimal(var);
				obj.setIto_actual(tmp_fee);
			}
			
			prmName = updtimePre+type_value;
			var = getParam(prmName);
			if(var != null && var.length() > 0){
				obj.setIto_update_timestamp(Timestamp.valueOf(var));
			}
			
			prmName = changtypePre+type_value;
			var = getParam(prmName);
			if( var != null && var.length() > 0) {
				change_type = Integer.parseInt(var);
			}
			obj.setChang_type(change_type);
			
			list.add(obj);
		}
		
		return list;
	}
    public int[] getInstrLst4aeItemLesson(){
        String[] temp = req.getParameterValues("ili_usr_ent_id_lst");
        if(temp == null || temp.length < 1){
            String var = getParam("ili_usr_ent_id_lst_str");
            if( var != null && var.length() > 0 ) {
                
                temp = cwUtils.splitToString(var, "~");
            }
        }
        int[] ili_usr_ent_id_lst = new int[temp.length];
        int j = 0;
        for(int i=0;i<temp.length;i++){
            if(!temp[i].equals("")){
                ili_usr_ent_id_lst[j] = Integer.parseInt(temp[i]); 
                j++;
            }
        }
        return ili_usr_ent_id_lst;
    }
    public aeItemLessonInstructor getParam2aeItemLessonInstructor(Connection con) throws qdbException{
        aeItemLessonInstructor ili = new aeItemLessonInstructor();
        String temp = getParam("ils_id");
        if(temp!=null && !temp.equals("")){
            ili.ili_ils_id = Integer.parseInt(temp);
        }
        temp = getParam("ili_usr_ent_id");
        if(temp!=null && !temp.equals("")){
            ili.ili_usr_ent_id = Integer.parseInt(temp);
        }
        ili.ili_create_timestamp =  dbUtils.getTime(con);
        ili.ili_create_usr_id = getParam("cur_usr_id");
        return ili;
    }
    public String getParam4RemoveIlsInstr(Connection con) throws qdbException{
        String ils_id = getParam("ils_id");
        return ils_id;
    }

    public void contentDef() {
        itm = new aeItem();
        String var;
        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            itm.itm_id = Integer.parseInt(var);
        }
        var = req.getParameter("content_def");
        if (var != null && var.length() > 0) {
                itm.itm_content_def = var;
        }
    }

    /**
     * 
     */
    public void getSsoParam() {
        String var;
        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            this.tnd.tnd_id = (Long.valueOf(var)).longValue();
        }

        //for itm_detail
        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            this.itm.itm_id = (Long.valueOf(var)).longValue();
        }

        
    }
    
    public void getTcrId() {
        String var;
        var = req.getParameter("tcr_id");
        if (var != null && var.length() > 0) {
            this.tcr_id = (Long.valueOf(var)).longValue();
        }
    }
    
    public void goldenManTest() {
    	String var;
        var = getParam("goldenman_param");
        if(var != null && var.length() > 0){
        	goldenManName = cwUtils.splitToVecString(var, ",");
        }
    	
    }
    public void getSnsParam(){
    	 String var;
         var = req.getParameter("s_cmt_id");
         if (var != null && var.length() > 0) {
             this.s_cmt_id = (Long.valueOf(var)).longValue();
         }
         var = req.getParameter("sns_type");
         if (var != null && var.length() > 0) {
                 this.sns_type = var;
         }
         var = req.getParameter("ent_id");
         if (var != null && var.length() > 0) {
             this.ent_id = (Long.valueOf(var)).longValue();
         }
    }
}