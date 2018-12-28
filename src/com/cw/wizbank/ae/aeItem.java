package com.cw.wizbank.ae;

import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.bean.CosCommentBean;
import com.cw.wizbank.JsonMod.Course.bean.CourseBean;
import com.cw.wizbank.accesscontrol.*;
import com.cw.wizbank.ae.db.*;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.ae.db.view.ViewItemRequirement;
import com.cw.wizbank.ae.db.view.ViewItemTargetGroup;
import com.cw.wizbank.ae.db.view.ViewItemTemplate;
import com.cw.wizbank.ae.db.view.ViewItemType;
import com.cw.wizbank.cert.Certificate;
import com.cw.wizbank.cf.CFCertificate;
import com.cw.wizbank.cf.CFCertificateLink;
import com.cw.wizbank.codetable.CodeTable;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.exportcols.ColType;
import com.cw.wizbank.config.organization.exportcols.ExportCols;
import com.cw.wizbank.config.organization.itemcostmgt.ItemCostManagement;
import com.cw.wizbank.config.organization.itemcostmgt.ItemCostType;
import com.cw.wizbank.config.organization.itemcostmgt.Labels;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.course.loadTargetLrnCacheAndCourseEnrollScheduler;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.fm.FMAuditTrail;
import com.cw.wizbank.fm.FMReservationManager;
import com.cw.wizbank.instructor.InstructorDao;
import com.cw.wizbank.integratedlrn.IntegratedLrn;
import com.cw.wizbank.itemtarget.ManageItemTarget;
import com.cw.wizbank.message.xmlObj;
import com.cw.wizbank.mote.Mote;
import com.cw.wizbank.mote.MoteDefault;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.tpplan.db.dbTpTrainingPlan;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.entity.CourseTabsRemind;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.services.CourseTabsRemindService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.web.WzbApplicationContext;
import com.oroinc.text.perl.Perl5Util;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
//>>add for bjmu


public class aeItem implements CFCertificateLink {
    private Connection con;

    private static final int CONTENT_EFF_END_BUFFER = 0;

    //constant for itm_app_approval_type
    public static final String APP_APPROVAL_TYPE_TADM = "TADM";
    public static final String APP_APPROVAL_TYPE_DS = "DIRECT_SUPERVISE";
    public static final String APP_APPROVAL_TYPE_DS_GS = "DIRECT_SUPERVISE_SUPERVISE";
    public static final String APP_APPROVAL_TYPE_DS_TADM = "DIRECT_SUPERVISE_TADM";
    public static final String APP_APPROVAL_TYPE_DS_GS_TADM = "DIRECT_SUPERVISE_SUPERVISE_TADM";

    //constant for itm_life_status
    public static final String ITM_LIFE_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String ITM_LIFE_STATUS_REQUEST_APPROVAL = "REQUEST_APPROVAL";
    public static final String ITM_LIFE_STATUS_APPROVED = "APPROVED";
    public static final String ITM_LIFE_STATUS_CANCELLED = "CANCELLED";
    public static final String ITM_LIFE_STATUS_DISCONTINUE = "DISCONTINUE";
    public static final String ITM_LIFE_STATUS_NULL = null;
    private static final String ITM_LIFE_STATUS_COMPLETED = "COMPLETED";  //Not potential database value

    //constant for itm_apply_method
    public static final String ITM_APPLY_METHOD_COMPULSORY = "COMPULSORY";
    public static final String ITM_APPLY_METHOD_ELECTIVE = "ELECTIVE";

    // constant for itm_approval_status
    public static final String ITM_APPROVAL_STATUS_PREAPPROVE = "PREAPPROVE";
    public static final String ITM_APPROVAL_STATUS_PENDING_APPROVAL = "PENDING_APPROVAL";
    public static final String ITM_APPROVAL_STATUS_PENDING_REAPPROVAL = "PENDING_REAPPROVAL";
    public static final String ITM_APPROVAL_STATUS_APPROVED = "APPROVED";
    public static final String ITM_APPROVAL_STATUS_APPROVED_OFF = "APPROVED_OFF";

    public static final String ITM_APPROVAL_ACTION_REQ_APPR = "REQ_APPR";
    public static final String ITM_APPROVAL_ACTION_CANCEL_REQ_APPR = "CANCEL_REQ_APPR";
    public static final String ITM_APPROVAL_ACTION_APPR_PUB = "APPR_PUB";
    public static final String ITM_APPROVAL_ACTION_DECLINE_APPR_PUB = "DECLINE_APPR_PUB";
    public static final String ITM_APPROVAL_ACTION_PUB = "PUB";
    public static final String ITM_APPROVAL_ACTION_UNPUB = "UNPUB";
    
    public static final String PARENT = "PARENT";
	public static final String CHILD = "CHILD";

   //const to determine to list orphan, non-orphan or all items
    public static final int ITM_ALL = 0;
    public static final int ITM_ORPHAN = 1;
    public static final int ITM_NON_ORPHAN = 2;

    //const for search item
    public static final String BEGIN_TND_TITLE = "BEGIN_TND_TITLE";
    public static final String TND_TITLE = "TND_TITLE";
    public static final String ITM_TITLE = "ITM_TITLE";
    public static final String R_ITM_TITLE = "R_ITM_TITLE";
    public static final String TND_EXT1 = "TND_EXT1";
    public static final String TND_EXT2 = "TND_EXT2";
    public static final String TND_EXT3 = "TND_EXT3";
    public static final String TND_EXT5 = "TND_EXT5";
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String STATUS = "STATUS";
    public static final String ORDERBY = "ORDERBY";
    public static final String SORTORDER = "SORTORDER";
    public static final String ALLNULL = "ALLNULL";
    public static final String CHILD_ITEM_TYPE = "CHILD_ITEM_TYPE";
    public static final String ITM_SESS_TIMESTAMP = "ITM_SESS_TIEMSTAMP";
    public static final String ITM_PARAM_TIMESTAMP = "ITM_PARAM_TIMESTAMP";
    public static final String ITM_SEARCH_SLOT_RESULT = "ITM_SEARCH_SLOT_RESULT";
    public static final String ITM_SEARCH_SLOT_PARAM = "ITM_SEARCH_SLOT_PARAM";
    public static final String ALL_IND = "ITM_ALL_IND";
    public static final String ADV_SRH_IND = "ADV_SRH_IND";
    public static final String TND_ID = "TND_ID";
    public static final String TND_ID_LIST = "TND_ID_LIST";
    public static final String TCR_ID_LIST = "TCR_ID_LIST";
    public static final String R_TCR_ID_LIST = "R_TCR_ID_LIST";
    public static final String ITM_STATUS = "ITM_STATUS";
    public static final String ITM_CODE = "ITM_CODE";
    public static final String ITM_TITLE_CODE = "ITM_TITLE_CODE";
    public static final String R_ITM_CODE = "R_ITM_CODE";
    public static final String R_ITM_TITLE_CODE = "R_ITM_TITLE_CODE";
    public static final String ITM_APPN_FROM = "ITM_APPN_FROM";
    public static final String ITM_APPN_TO = "ITM_APPN_TO";
    public static final String ITM_EFF_FROM = "ITM_EFF_FROM";
    public static final String ITM_EFF_TO = "ITM_EFF_TO";
    public static final String ITM_APPN_FROM_FLAG = "ITM_APPN_FROM_FLAG";
    public static final String ITM_APPN_TO_FLAG = "ITM_APPN_TO_FLAG";
    public static final String ITM_EFF_FROM_FLAG = "ITM_EFF_FROM_FLAG";
    public static final String ITM_EFF_TO_FLAG = "ITM_EFF_TO_FLAG";
    public static final String ITM_TYPES = "ITM_TYPES";
    public static final String EXACT = "EXACT";
    public static final String R_EXACT = "R_EXACT";
    public static final String ITM_SEARCH_ITM_PARAM = "ITM_SEARCH_ITM_PARAM";
    public static final String ITM_SEARCH_ITM_RESULT = "ITM_SEARCH_ITM_RESULT";
    public static final String PAGE = "PAGE";
    public static final String PAGE_SIZE = "PAGE_SIZE";
    public static final String FILTER_RETIRE = "FILTER_RETIRE";
    public static final String SHOW_ORPHAN = "SHOW_ORPHAN";
    public static final String ITM_TARGET_ENT_GROUP = "ITM_TARGET_ENT_GROUP";
    public static final String ITM_COMP_TARGET_ENT_GROUP = "ITM_COMP_TARGET_ENT_GROUP";
    public static final String ITM_R_TARGET_ENT_GROUP = "ITM_R_TARGET_ENT_GROUP";
    public static final String ITM_SKILL_ID_VECTOR = "ITM_SKILL_ID_VECTOR";
    public static final String ITM_SHOW_RESPON = "ITM_SHOW_RESPON";
    public static final String ITM_LIST_VIEW_ID = "ITM_LIST_VIEW_ID";
    public static final String ITM_SHOW_NO_RUN_PARENT = "ITM_SHOW_NO_RUN_PARENT";
    public static final String ITM_SHOW_RUN_ONLY = "ITM_SHOW_RUN_ONLY";  
    public static final String ITM_APPN_FROM_OPERATOR = "ITM_APPN_FROM_OPERATOR";
    public static final String ITM_APPN_TO_OPERATOR = "ITM_APPN_TO_OPERATOR";
    public static final String ITM_EFF_FROM_OPERATOR = "ITM_EFF_FROM_OPERATOR";
    public static final String ITM_EFF_TO_OPERATOR = "ITM_EFF_TO_OPERATOR";
    public static final String ITM_SHOW_ATTENDANCE = "ITM_SHOW_ATTENDANCE";
    public static final String ITM_FILTER_RETIRE_OR_EXIST_IN_PROGRESS_ATTENDANCE = "ITM_FILTER_RETIRE_OR_EXIST_IN_PROGRESS_ATTENDANCE";
    public static final String NOW = "NOW";
    public static final String ITM_EXIST_IN_PROGRESS_ATTENDANCE = "ITM_EXIST_IN_PROGRESS_ATTENDANCE";
    public static final String ITM_LIFE_STATUS = "ITM_LIFE_STATUS";
    public static final String SEARCH_NULL_LIFE_STATUS = "NULL_LIFE_STATUS";
    public static final String ITM_SHOW_RUN_IND = "ITM_SHOW_RUN_IND";
    public static final String ITM_LIFE_STATUS_EQUAL_LST = "ITM_LIFE_STATUS_EQUAL_LST";
    public static final String ITM_LIFE_STATUS_NOT_EQUAL_LST = "ITM_LIFE_STATUS_NOT_EQUAL_LST";
    public static final String ITM_ALLOW_NULL_DATETIME = "ITM_ALLOW_NULL_DATETIME";
    public static final String SHOW_OFF_ITM = "SHOW_OFF_ITM";
    public static final String SHOW_PRE_APPROVE_ITM = "SHOW_PRE_APPROVE_ITM";
    public static final String ITM_APPLICABLE_TYPE = "APPLICABLE";
    private static final String ALL_ITM_TYPES = "ALL_ITM_TYPES";
    private static final String ITM_CREATE_USR_ID = "ITM_CREATE_USR_ID";
    public static final String ITM_ONLY_OPEN_ENROL_NOW = "ITM_ONLY_OPEN_ENROL_NOW";
    public static final String ITM_ONLY_OPEN_ENROL_QUOTA_NOW = "ITM_ONLY_OPEN_ENROL_QUOTA_NOW";
    public static final String TRAINING_TYPE="training_type";
    public static final String DUMMY_TYPE="dummy_type";
    public static final String CAT_PUBLIC_IND = "CAT_PUBLIC_IND";
    public static final String ITM_APPROVAL_STATUS = "ITM_APPROVAL_STATUS";
    public static final String TRAINING_PLAN="TRAINING_PLAN";
    public static final String PLAN_ID="PLAN_ID";
    public static final String TPN_TCR_ID="TPN_TCR_ID";
    public static final String TPN_UPDATE_TIMESTAMP="TPN_UPDATE_TIMESTAMP";
    public static final String TPN_ENTRANCE="TPN_ENTRANCE";
    
    //slot status
    public static final String SLOT_STATUS_CONFIRMED = "CONFIRMED";
    public static final String SLOT_STATUS_PENDING = "PENDING";
    public static final String SLOT_STATUS_AVAILABLE = "AVAILABLE";
    public static final String SLOT_STATUS_ALL = "";

    public static final String UNKNOWN_ITM_TYPE = "AEIT01";
    public static final String ITM_OFFLINE_MSG = "AEIT02";
    public static final String HAS_APPLICATION_MSG = "AEIT03";
    public static final String NO_ENROL_RECORD_MSG = "AEIT04";
    public static final String NO_ONLINE_CONTENT_MSG = "AEIT05";
    public static final String ITM_CODE_EXIST_MSG = "AEIT06";
    public static final String ITM_EXAM_EXIST_MSG = "AEIT30";
    public static final String ITM_ADD_OK = "AEIT07";
    public static final String ITM_ALREADY_APPROVED = "AEIT08";
    public static final String ITM_RUN_NO_NEW_VERSION = "AEIT09";
    public static final String ITM_NOT_COMPLETE = "AEIT10";
    public static final String ITM_ALREADY_CANCELLED = "AEIT11";
    public static final String ITM_UPDATE_OK = "AEIT12";
    public static final String ITM_NEW_VERSION_OK = "AEIT13";
    public static final String ITM_NOT_COMPLETE_FOR_DISCONTINUE = "AEIT14";
    public static final String ITM_TITLE_EXIST_MSG = "AEIT15";
    public static final String ITM_SESSION_NO_NEW_VERSION = "AEIT16";
    public static final String ITM_QR_UPDATE_OK = "AEIT17";
    public static final String ITM_DEL_HAS_ENROLLMENT = "AEIT18";
	public static final String ITM_INVALID_TIMESTAMP ="AEIT19";
    public static final String ITM_CHANGE_WRK_CLASS_LEVEL = "AEIT20";
    public static final String ITM_CHANGE_WRK_INCORRECT_ITM_TYPE = "AEIT21";
    public static final String ITM_CHANGE_WRK_PENDING_APPN = "AEIT22";
    public static final String ITM_CHANGE_WRK_OK = "AEIT23";
    public static final String ITM_UPDATE_OK2 = "AEIT24";
    public static final String CERT_NOT_EXIST = "938";
    
    public static final String ITM_NOT_STARTED = "NOT_STARTED";
    public static final String ITM_IN_PROGRESS = "IN_PROGRESS";
    public static final String ITM_FINISHED = "FINISHED";

    public static final String ITM_STATUS_ON = "ON";
    public static final String ITM_STATUS_ALL = "ALL";
    public static final String ITM_STATUS_TARGET = "TARGET";
    public static final String ITM_STATUS_OFF = "OFF";

    public static final String ITM_AVAILABLE = "AVAILABLE";
    public static final String ITM_EMPTY = "EMPTY";
    public static final String ITM_FULL = "FULL";
    public static final String ITM_UNLIMITED = "UNLIMITED";

    public static final String ITM_TKH_METHOD_SEPARATED = "SEPARATED";
    public static final String ITM_TKH_METHOD_COMBINED = "COMBINED";

    public static final String ITM_AUTO_ENROLL_TYPE_AUTO_CONFIRM = "TARGET_AUTO_CONFIRM";
    public static final String ITM_AUTO_ENROLL_TYPE_AUTO_ENROLL = "TARGET_AUTO_ENROLL";
		public static final String IS_REQ = "AEIT25";
	
	public static final String ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER = "TARGET_LEARNER";
	public static final String ITM_TARGET_ENROLL_TYPE_ASSIGNED = "ASSIGNED";
	
    public static final int ITM_SEARCH_PAGE_SIZE = 10;
    
    //item type 
    public static final String ITM_TYPE_SELFSTUDY = "SELFSTUDY"; //网上课程
    public static final String ITM_TYPE_CLASSROOM = "CLASSROOM"; //离线课程
    public static final String ITM_TYPE_INTEGRATED = "INTEGRATED";  //项目培训
    public static final String ITM_TYPE_VIDEO = "VIDEO"; 
    public static final String ITM_TYPE_MOBILE = "MOBILE";
    public static final String ITM_TYPE_AUDIOVIDEO="AUDIOVIDEO";
    public static Hashtable usrTargetItmLstHS = new Hashtable();


    public long itm_id;
    public String itm_title;

    /*  not implement yet */
    public String itm_desc;

    public String itm_type;
    public String itm_dummy_type;
    public Timestamp itm_appn_start_datetime;
    public Timestamp itm_appn_end_datetime;
    public long itm_capacity;
    public float itm_unit;
    public String itm_fee_ccy;
    public float itm_fee;
    public float itm_score;
    public String itm_xml;
    public String itm_srh_content;
    public String itm_progress_status;
    public String itm_status;
    public long itm_owner_ent_id;
    public Timestamp itm_create_timestamp;
    public String itm_create_usr_id;
    public Timestamp itm_upd_timestamp;
    public String itm_upd_usr_id;
    public Timestamp itm_eff_start_datetime, itm_eff_end_datetime;
    public String itm_code;
    public aeTreeNode myTreeNode;
    public String app_status;
    public String app_rol_ext_id;
    public long cos_res_id;
//    public Timestamp cos_eff_start_datetime;    // depreciate, replaced by itm_content_eff_start_datetime
//    public Timestamp cos_eff_end_datetime;      // depreciate, replaced by itm_content_eff_end_datetime
    public Timestamp itm_content_eff_start_datetime;
    public Timestamp itm_content_eff_end_datetime;
    public int itm_content_eff_duration;        // replace ccr_duration
    public String itm_ext1;
    public long tnd_id; //tnd_id of this item
    // new attributes for 3.5
    public boolean itm_create_run_ind;  // indicates if this item can create run
    public boolean itm_run_ind;         // indicates if this item is a run
    public boolean itm_apply_ind;       // indicates if this item can be applied
    public boolean itm_deprecated_ind;  // indicates if this item is deprecated
    public String itm_life_status;       // the life status of this item (NULL, IN_PROGRESS, RQUEST_APPROVAL, APPROVED, CANCEL)
    public String itm_apply_method;     // the method of application
    public boolean itm_qdb_ind;         // indicates if this item has a qdb course
    public long itm_imd_id;             // mote default id
    public boolean itm_auto_enrol_qdb_ind;   // indicates if this item will auto enrol into WZBCourse
    public boolean itm_can_cancel_ind; //indicates if this item can be cancelled
    public String itm_version_code;
    public String itm_person_in_charge;
    public long itm_min_capacity;
    public String itm_cancellation_reason;
    public String itm_cancellation_type;
    public long itm_rsv_id;
    public long itm_tcr_id;
    public String itm_app_approval_type; //stores the application approval type of the Item
    public String itm_content_def;
    public String itm_inst_type;
    
    // complete end date notification email
    public int itm_notify_days;
    public String itm_notify_email;
    
    public Hashtable h_ity_inds;  // stores the indicators from aeItemType as Boolean object, keys are those column names of aeItemType

    public boolean itm_create_session_ind;  // indicates if this item can create session, can either create run or session. not both
    public boolean itm_session_ind;         // indicates if this item is a session, can be either run or session, not both
    public boolean itm_has_attendance_ind;
    public boolean itm_ji_ind;
    public boolean itm_completion_criteria_ind;
    public boolean itm_can_qr_ind;
    public boolean itm_retake_ind;
    public boolean itm_blend_ind;
    public boolean itm_exam_ind;
    public boolean itm_ref_ind;

    public long itm_send_enroll_email_ind; // indicates if the item send enrollmetn email to leanrers.
    public String itm_enroll_type;
    public String itm_access_type;
    public String itm_approval_status;
    public String itm_approval_action;
    public String itm_approve_usr_id;
    public Timestamp itm_approve_timestamp;
    public String itm_submit_action;
    public String itm_submit_usr_id;
    public Timestamp itm_submit_timestamp;
	private Timestamp itm_publish_timestamp;
    public boolean itm_not_allow_waitlist_ind;
    public String itm_target_enrol_type;
    public long itm_cfc_id;    
    public String itm_offline_pkg;
    public String itm_offline_pkg_file;

    public String itm_icon;
    public String itm_plan_code;
    public long auto_enroll_interval = loadTargetLrnCacheAndCourseEnrollScheduler.Auto_Enroll_Interval;
    //indicates if current usr can qr the item
    // calc by itm_can_qr_ind and qr_settings
    public boolean usr_can_qr_ind;
    public boolean itm_share_ind;
    
    public Timestamp itm_ji_send_datetime;
    
    public String itm_mobile_ind;
    
    public Timestamp itm_reminder_send_datetime;

    public static final String[] ORDER = {"code", "name", "type", "appn_end", "eff_start"};
    public static final String[] ORDER_DB = {"itm_code", "itm_title", "itm_type", "itm_appn_end_datetime", "itm_eff_start_datetime"};

    // control which parts of item details will be included in the output
    public int details_option;
    // details option constants (maximum 32 kinds of details)
    private static final int DTL_DEF       = 0;     // default, only template details
    private static final int DTL_TPL_VALUE = 1;     // template details
    private static final int DTL_RUN_ITEM  = 2;     // run items
    private static final int DTL_NODE_ASS  = 4;     // assigned nodes
    private static final int DTL_ACL_RULE  = 8;     // access control rules
    private static final int DTL_RES       = 16;    // related resources
    private static final int DTL_COMPET    = 32;    // competence
    private static final int DTL_PARENT    = 64;    // item details of the parent item
    private static final int DTL_TPL_VIEW  = 128;   // list of template views

    // item access parameters
    public String acc_type;
    public String[] acc_id;
    // template parameters
    public String tpl_type;
    // parent item id if it is a run
    public long parent_itm_id;

    public int itm_mark_buffer_day;
    // for cost center group list
    public String targetType = null;

	// for tcr_title;
	public String itm_tcr_title;
	public DbFigure[] itm_credit_values;

	public aeItem child_item;
	public long enrolled;
	public boolean itm_bonus_ind;
	public float itm_diff_factor;
	public boolean itm_integrated_ind;
	public long[] itm_id_lst;
	
    public boolean is_complete_del;
    public boolean is_new_cos;

	public static class ViewNotifyUser {
		public String usr_id;
		public long usr_ent_id;
		public String usr_display_bil;
		public String usr_email_2;
	}

    public aeItem() {
        // do nothing
    }

    public aeItem(aeItem itm) {
        this.itm_title = itm.itm_title;
        this.itm_type = itm.itm_type;
        this.itm_appn_start_datetime = itm.itm_appn_start_datetime;
        this.itm_appn_end_datetime = itm.itm_appn_end_datetime;
        this.itm_capacity = itm.itm_capacity;
        //this.itm_ext2 = itm.itm_ext2;
        this.itm_unit = itm.itm_unit;
        this.itm_fee_ccy = itm.itm_fee_ccy;
        this.itm_fee = itm.itm_fee;
        this.itm_xml = itm.itm_xml;
        this.itm_progress_status = itm.itm_progress_status;
        this.itm_status = itm.itm_status;
        this.itm_owner_ent_id = itm.itm_owner_ent_id;
        this.itm_create_timestamp = itm.itm_create_timestamp;
        this.itm_create_usr_id = itm.itm_create_usr_id;
        this.itm_upd_timestamp = itm.itm_upd_timestamp;
        this.itm_upd_usr_id = itm.itm_upd_usr_id;
        this.itm_eff_start_datetime = itm.itm_eff_start_datetime;
        this.itm_eff_end_datetime = itm.itm_eff_end_datetime;
        this.itm_code = itm.itm_code;
        this.itm_ext1 = itm.itm_ext1;
        this.itm_create_run_ind = itm.itm_create_run_ind;
        this.itm_create_session_ind = itm.itm_create_session_ind;
        this.itm_run_ind = itm.itm_run_ind;
        this.itm_has_attendance_ind = itm.itm_has_attendance_ind;
        this.itm_ji_ind = itm.itm_ji_ind;
        this.itm_completion_criteria_ind = itm.itm_completion_criteria_ind;
        this.itm_apply_ind = itm.itm_apply_ind;
        this.itm_deprecated_ind = itm.itm_deprecated_ind;
        this.itm_life_status = itm.itm_life_status;
        this.itm_apply_method = itm.itm_apply_method;
        this.itm_qdb_ind = itm.itm_qdb_ind;
        this.itm_auto_enrol_qdb_ind = itm.itm_auto_enrol_qdb_ind;
        this.itm_version_code = itm.itm_version_code;
        this.itm_min_capacity = itm.itm_min_capacity;
        this.itm_person_in_charge = itm.itm_person_in_charge;
        this.itm_cancellation_reason = itm.itm_cancellation_reason;
        this.itm_blend_ind = itm.itm_blend_ind;
        this.itm_exam_ind = itm.itm_exam_ind;
        this.itm_ref_ind = itm.itm_ref_ind;
        this.itm_dummy_type = itm.itm_dummy_type;
        this.itm_session_ind = true;
		this.itm_content_def = itm.itm_content_def;
		this.itm_bonus_ind = itm.itm_bonus_ind;
		this.itm_diff_factor = itm.itm_diff_factor;
    }

    public aeItem(Connection con) {
        this.con = con;
    }

    public void setConnection(Connection con) {
        this.con = con;
    }
    public Connection getConnection() {
        return this.con;
    }

    //get raw template of an item
    /* REPLACED BY ANOTHER getRawTemplate
    public String getRawTemplate(Connection con, String tpl_type) throws SQLException {

        aeTemplate tpl = new aeTemplate();
        tpl.tpl_id = getTemplateId(con, tpl_type);
        return tpl.getRawTemplate(con);
    }
    */

    private String getItemInfoAsExtXML(Connection con) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append("<item_status>");
        SQLBuf.append("<status value=\"").append(this.itm_status).append("\"/>");
        SQLBuf.append("</item_status>");
        SQLBuf.append("<item_life_status>");
        SQLBuf.append("<status value=\"");

        if (this.itm_life_status != null) {
            SQLBuf.append(this.itm_life_status);
        } else {
            Timestamp cur_time = cwSQL.getTime(con);

            if (this.itm_eff_end_datetime != null && this.itm_eff_end_datetime.before(cur_time)) {
                SQLBuf.append(ITM_LIFE_STATUS_COMPLETED);
            } else {
                SQLBuf.append(ITM_LIFE_STATUS_IN_PROGRESS);
            }
        }

        SQLBuf.append("\"/>");
        //SQLBuf.append("<reason value=\"").append(aeUtils.escNull(this.itm_ext1)).append("\"/>");
        SQLBuf.append("</item_life_status>");
        if(this.itm_life_status != null && this.itm_life_status.equals(ITM_LIFE_STATUS_CANCELLED)) {
            SQLBuf.append("<item_cancel_reason>");
            SQLBuf.append("<reason value=\"").append(dbUtils.esc4XML(aeUtils.escNull(this.itm_cancellation_reason))).append("\"/>");
            SQLBuf.append("</item_cancel_reason>");

            SQLBuf.append("<item_cancel_type>");
            SQLBuf.append("<type value=\"").append(dbUtils.esc4XML(aeUtils.escNull(this.itm_cancellation_type))).append("\"/>");
            SQLBuf.append("</item_cancel_type>");

        }
        
        if(itm_apply_ind && !itm_create_run_ind) {
            String eff_value = null;
            if(itm_content_eff_end_datetime != null) {
                eff_value = itm_content_eff_end_datetime.toString();
            } else if (itm_content_eff_duration > 0) {
                eff_value = String.valueOf(itm_content_eff_duration);
            } else {
                eff_value = "";
            }
            SQLBuf.append("<content_eff>")
            	  .append("<content_eff_start_end value=\"").append(eff_value).append("\"/>")
                  .append("</content_eff>"); 
        }
        
        return SQLBuf.toString();
    }

    // << BEGIN for oracle migration!
    private final static String sql_has_toc =
        " Select cos_res_id, cos_structure_xml from Course " +
        " Where cos_itm_id = ? ";
        //" And cos_structure_xml is not null ";
    public boolean hasToc(Connection con) throws SQLException {
        boolean result = false;
        PreparedStatement stmt = con.prepareStatement(sql_has_toc);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        //result = (rs.next()) ? true : false;
        while (!result && rs.next()) {
            String tmpStr = cwSQL.getClobValue(rs, "cos_structure_xml");
            if (tmpStr != null && tmpStr.length() > 0) {
                result = true;
            }
        }
        stmt.close();
        return result;
    }
    // >> END

    private String getLinkCriteriaAsXML(Connection con, Timestamp cur_time, boolean cos_mgt_ind)
                                        throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append("<link_criteria>");
        //only non-approved version will call this method
        SQLBuf.append("<approved_version_ind value=\"").append(false).append("\"/>");
        SQLBuf.append("<item_life_status value=\"").append(aeUtils.escNull(this.itm_life_status)).append("\"/>");
        if(this.itm_qdb_ind) {
            SQLBuf.append("<tob_existence value=\"").append(hasToc(con)).append("\"/>");
        }
        SQLBuf.append("<item_apply_ind value=\"").append(this.itm_apply_ind).append("\"/>");
        if((this.itm_appn_start_datetime == null || this.itm_appn_start_datetime.before(cur_time))
          && (this.itm_appn_end_datetime == null || this.itm_appn_end_datetime.after(cur_time))) {
            SQLBuf.append("<item_within_enrollment_ind value=\"").append(true).append("\"/>");
        }
        SQLBuf.append("<item_deprecated_ind value=\"").append(this.itm_deprecated_ind).append("\"/>");
        //<item_management> is the management privilege on underlying qdbCourse
        //keep <item_management> name for backward compatibility
        SQLBuf.append("<item_management value=\"").append(cos_mgt_ind).append("\"/>");
        SQLBuf.append("</link_criteria>");
        return SQLBuf.toString();
    }

    private boolean isWithInAppnPeriod(Connection con, Timestamp cur_time) throws SQLException  {

        boolean result;
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        result = (this.itm_appn_start_datetime == null || this.itm_appn_start_datetime.before(cur_time))
                && (this.itm_appn_end_datetime == null || this.itm_appn_end_datetime.after(cur_time));
        return result;
    }

    // meaning as ItemDetailByChildAsXML
    public String ItemDetailByRunAsXML(Connection con, qdbEnv inEnv,
                                    boolean checkStatus, boolean prev_version_ind,
                                    String tvw_id, long usr_ent_id,
                                    boolean cos_mgt_ind, boolean show_attendance_ind)
                                    throws SQLException, cwException ,cwSysMessage {
        return ItemDetailByRunAsXML(con, inEnv, checkStatus, prev_version_ind, tvw_id, usr_ent_id,
                                    cos_mgt_ind, show_attendance_ind, false);
    }

    public String ItemDetailByRunAsXML(Connection con, qdbEnv inEnv,
                                    boolean checkStatus, boolean prev_version_ind,
                                    String tvw_id, long usr_ent_id,
                                    boolean cos_mgt_ind, boolean show_attendance_ind, boolean show_session_ind)
                                    throws SQLException, cwException ,cwSysMessage {
        String xml;
        aeItem parent = new aeItem();
        aeItemRelation ire = new aeItemRelation();
        ire.ire_child_itm_id = this.itm_id;
        parent.itm_id = ire.getParentItemId(con);
        if(parent.itm_id != 0) {
            parent.getItem(con);
            xml = parent.ItemDetailAsXML(con, inEnv, checkStatus, prev_version_ind,
                                            tvw_id, true, show_session_ind, this.itm_id, usr_ent_id,
                                            cos_mgt_ind, show_attendance_ind,false,null, null, false);
        } else {
            /*getItem(con);*/
            xml = ItemDetailAsXML(con, inEnv, checkStatus, prev_version_ind,
                                   tvw_id, false, show_session_ind, 0, usr_ent_id,
                                   cos_mgt_ind, show_attendance_ind,false,null, null, false);
        }
        return xml;
    }
    /*
    @deprecated
    */

    public String ItemDetailAsXML(Connection con, qdbEnv inEnv,
                                    boolean checkStatus, boolean prev_version_ind,
                                    String tvw_id, boolean show_run_ind, long in_run_itm_id,
                                    long usr_ent_id, boolean cos_mgt_ind,
                                    boolean show_attendance_ind)
                                    throws SQLException, cwException ,cwSysMessage
    {
        return ItemDetailAsXML(con, inEnv, checkStatus, prev_version_ind, tvw_id, show_run_ind, false, in_run_itm_id,
                                    usr_ent_id, cos_mgt_ind,show_attendance_ind,false,null, null, false);
    }

    public String ItemDetailAsXML(Connection con, qdbEnv inEnv,
                                    boolean checkStatus, boolean prev_version_ind,
                                    String tvw_id, boolean show_run_ind, boolean show_session_ind, long in_run_itm_id,
                                    long usr_ent_id, boolean cos_mgt_ind,
                                    boolean show_attendance_ind,
                                    boolean show_respon_run_ind, String rol_ext_id, loginProfile prof, boolean tcEnabled)
                                    throws SQLException, cwException ,cwSysMessage {
//                                        System.out.println("ItemDetailAsXML");
        StringBuffer xmlBuf = new StringBuffer(2500);
        if(myTreeNode != null) {
            xmlBuf.append(myTreeNode.contentAsXML(con));
        }
        Timestamp cur_time = cwSQL.getTime(con);
        
        //fot sso itm end date check
//        boolean isEnrolled = false;
//        if (itm_content_eff_end_datetime != null) {
//            if (dayDiff(cur_time, itm_content_eff_end_datetime) >= 0 && checkEnrolled(con, usr_ent_id)) {
//                isEnrolled = true;
//            }
//        } else if (checkEnrolled(con, usr_ent_id)){
//            isEnrolled = true;
//        }
        
        //get course res_id
        dbCourse cos = new dbCourse();
        cos.cos_itm_id = this.itm_id;
//        long ccr_id = 0;
        if(this.itm_qdb_ind) {
            cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
//            ccr_id = dbCourse.getCosCriteriaId(con, cos.cos_res_id);
        }

        ViewItemTemplate viItmTpl = new ViewItemTemplate();
        viItmTpl.itemId = itm_id;
        int enrol_cnt = aeApplication.countItemAppn(con, this.itm_id, show_attendance_ind);
        String itmStatus = "";
        if(itm_status.equalsIgnoreCase(ITM_STATUS_OFF)) {
        	itmStatus = itm_status;
        } else {
        	itmStatus = itm_access_type;
        }
        // item attributes
        if(itm_dummy_type!=null){
        	xmlBuf.append("<training_type>").append(convertTrainType(itm_dummy_type)).append("</training_type>");
        }
        xmlBuf.append("<item")
                .append(" id=\"").append(itm_id).append("\"")
                //.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("\"")
                .append(" type=\"").append(itm_type).append("\"");
                if(itm_dummy_type!=null){
          xmlBuf.append(" dummy_type=\"").append(itm_dummy_type).append("\"");
                }
          xmlBuf.append(" class=\"").append(itm_run_ind).append("\"");
          xmlBuf.append(" cos_res_id=\"").append(aeUtils.escZero(cos.cos_res_id)).append("\"")
  //              .append(" ccr_id=\"").append(aeUtils.escZero(ccr_id)).append("\"")
                .append(" status=\"").append(itmStatus).append("\"")
                .append(" itm_mobile_ind=\"").append(itm_mobile_ind).append("\"")
                .append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"")
                .append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"")
                .append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"")
                .append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"")
                .append(" appn_start_datetime=\"").append(aeUtils.escNull(itm_appn_start_datetime)).append("\"")
                .append(" appn_end_datetime=\"").append(aeUtils.escNull(itm_appn_end_datetime)).append("\"")
                .append(" eff_start_days_to=\"").append(dayDiff(cur_time, itm_eff_start_datetime)).append("\"")
                .append(" eff_end_days_to=\"").append(dayDiff(cur_time, itm_eff_end_datetime)).append("\"")
                .append(" appn_start_days_to=\"").append(dayDiff(cur_time, itm_appn_start_datetime)).append("\"")
                .append(" appn_end_days_to=\"").append(dayDiff(cur_time, itm_appn_end_datetime)).append("\"")
                .append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"")
                .append(" apply_method=\"").append(aeUtils.escNull(itm_apply_method)).append("\"")
                .append(" capacity=\"").append(itm_capacity).append("\"")
                .append(" min_capacity=\"").append(itm_min_capacity).append("\"")
                .append(" unit=\"").append(itm_unit).append("\"")
                .append(" version_code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_version_code))).append("\"")
                .append(" person_in_charge=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_person_in_charge))).append("\"")
                .append(" cancellation_reason=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_reason))).append("\"")
                .append(" cancellation_type=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_type))).append("\"")
                .append(" create_run_ind=\"").append(itm_create_run_ind).append("\"")
                .append(" create_session_ind=\"").append(itm_create_session_ind).append("\"")
                .append(" qdb_ind=\"").append(itm_qdb_ind).append("\"")
                .append(" auto_enrol_qdb_ind=\"").append(itm_auto_enrol_qdb_ind).append("\"")
                .append(" run_ind=\"").append(itm_run_ind).append("\"")
                .append(" session_ind=\"").append(itm_session_ind).append("\"")
                .append(" has_attendance_ind=\"").append(itm_has_attendance_ind).append("\"")
                .append(" ji_ind=\"").append(itm_ji_ind).append("\"")
                .append(" completion_criteria_ind=\"").append(itm_completion_criteria_ind).append("\"")
                .append(" itm_enroll_type=\"").append(aeUtils.escNull(itm_enroll_type)).append("\"")
                .append(" apply_ind=\"").append(itm_apply_ind).append("\"")
                .append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"")
                .append(" can_qr_ind=\"").append(itm_can_qr_ind).append("\"")
                .append(" usr_can_qr_ind=\"").append(usr_can_qr_ind).append("\"")
                .append(" imd_id=\"").append(aeUtils.escZero(itm_imd_id)).append("\"")
                .append(" rsv_id=\"").append(aeUtils.escZero(itm_rsv_id)).append("\"")
                .append(" tcr_id=\"").append(aeUtils.escZero(itm_tcr_id)).append("\"")
                .append(" appn_started_ind=\"").append(this.itm_appn_start_datetime == null || this.itm_appn_start_datetime.before(cur_time)).append("\"")
                .append(" appn_closed_ind=\"").append(this.itm_appn_end_datetime != null && this.itm_appn_end_datetime.before(cur_time)).append("\"")
                .append(" content_started_ind=\"").append(this.itm_content_eff_start_datetime == null || this.itm_content_eff_start_datetime.before(cur_time)).append("\"")
                .append(" content_closed_ind=\"").append(this.itm_content_eff_end_datetime != null && this.itm_content_eff_end_datetime.before(cur_time)).append("\"")
                .append(" enrol_cnt=\"").append(enrol_cnt).append("\"")
                .append(" itm_upd_timestamp=\"").append(aeUtils.escNull(itm_upd_timestamp)).append("\"")
                .append(" itm_not_allow_waitlist_ind=\"").append(itm_not_allow_waitlist_ind).append("\"")
                //.append(" cos_res_id=\"").append(getResId(con)).append("\"")
                .append(" notify_days=\"").append(aeUtils.escZero(itm_notify_days)).append("\"")
                .append(" notify_email=\"").append(cwUtils.esc4XML(cwUtils.escNull(itm_notify_email))).append("\"")
                .append(" plan_code=\"").append(cwUtils.esc4XML(cwUtils.escNull(itm_plan_code))).append("\"")
                .append(" bonus_ind=\"").append(itm_bonus_ind).append("\"")
                .append(" diff_factor=\"").append(aeUtils.escZero(itm_diff_factor)).append("\"")
                .append(" itm_integrated_ind=\"").append(itm_integrated_ind).append("\"")
                .append(" itm_share_ind=\"").append(itm_share_ind).append("\"")
                .append(" itm_cfc_id=\"").append(itm_cfc_id).append("\"")
                .append(" offline_pkg=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_offline_pkg))).append("\"")
                .append(" offline_pkg_file=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_offline_pkg_file))).append("\"")
            ;
        if(itm_not_allow_waitlist_ind && (itm_capacity != 0 && itm_capacity <= enrol_cnt)) {
        	xmlBuf.append(" refuse_exceeded_quato_enrol=\"").append(true).append("\"");
        }
            if(this.itm_run_ind || this.itm_session_ind) {
                aeItemRelation aeIre = new aeItemRelation();
                aeIre.ire_child_itm_id = this.itm_id;
                aeItem ireParentItm = aeIre.getParentInfo(con);
                if (ireParentItm != null) {
                    xmlBuf.append(" parent_itm_id=\"").append(ireParentItm.itm_id).append("\"");
                    xmlBuf.append(" parent_title=\"").append(cwUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"");
                }
            }
            xmlBuf.append(">");
    		if(prof != null) {
    			String itemAcXML = null;
    			String[] str=new String[]{"hasItmEditBtn"};
    			AcPageVariant acPageVariant = new AcPageVariant(con);
    			acPageVariant.prof = prof;
    			acPageVariant.instance_id = itm_id;
    			acPageVariant.ent_owner_ent_id = prof.root_ent_id;
    			acPageVariant.ent_id = prof.usr_ent_id;
    			acPageVariant.rol_ext_id = prof.current_role;
    			acPageVariant.root_id = prof.root_id;
    			acPageVariant.tc_enable_ind = tcEnabled;
	    		//	acPageVariant.setWizbiniLoader(wizbini);
	    		itemAcXML=acPageVariant.answerPageVariantAsXML(str);
	    		xmlBuf.append(itemAcXML);
    		}
            // use root level approval_status
            if(this.itm_run_ind || this.itm_session_ind) {
                long rootItmId = aeItem.getRootItemId(con, itm_id);
                itm_approval_status = aeItem.getApprovalStatus(con, rootItmId);
            }

            if (itm_approval_status != null){
                xmlBuf.append("<approval_status>").append(itm_approval_status).append("</approval_status>").append(dbUtils.NEWL);
            }
            
//            //for sso
//            if (isEnrolled) {
//                xmlBuf.append("<cur_usr_enrolled>").append(isEnrolled).append("</cur_usr_enrolled>");
//            }

            xmlBuf.append(getNavAsXML(con, this.itm_id));
            //get application count by app_status
            xmlBuf.append(aeApplication.getAppnCountByItemAsXML(con, this.itm_id));
            xmlBuf.append("<workflow_template approval_ind=\"").append(this.itm_app_approval_type!=null).append("\" />");


            if(usr_ent_id != 0) {
                xmlBuf.append(getAppAsXML(con, usr_ent_id));
                if( usr_ent_id != 0 && this.app_rol_ext_id != null ){
                	xmlBuf.append(getAppWrkTplAsXML(con, usr_ent_id));
                }
                if (!this.itm_run_ind || isTargetedLearner( con, usr_ent_id, itm_id, false)){
                     xmlBuf.append("<applicable>").append(isEnrollable(con, usr_ent_id)).append("</applicable>");
                }
            }
            //item type xml
            xmlBuf.append(aeUtils.escNull(getItemTypeTitle(con)));
            
            //item title
            xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>").append(dbUtils.NEWL);

            if(prev_version_ind) {

                xmlBuf.append("<family>");
                Vector v_family = getItemFamily(con);
                for(int i=0; i<v_family.size(); i++) {
                    xmlBuf.append(((aeItem)v_family.elementAt(i)).itemAttributeAsXML());
                }
                xmlBuf.append("</family>");
            }
            //get item template
            aeTemplate tpl = getItemTemplate(con);

            //get item template view
            int cnt = 0;
            if(tvw_id == null || tvw_id.length() == 0) {
                tvw_id = "DETAIL_VIEW";
            }
            // add by Tim: check if the item is cancelled, change to "CANCELLED VIEW"
            if(itm_life_status !=null && itm_life_status.equals("CANCELLED"))
                tvw_id = "CANCELLED_VIEW";
            DbTemplateView dbTplVi = new DbTemplateView();
            do {
                if(cnt > 1) {
                    break;
                }
                if(cnt > 0) {
                    tvw_id = "DETAIL_VIEW";
                }
                dbTplVi.tvw_tpl_id = tpl.tpl_id;
                dbTplVi.tvw_id = tvw_id;
                dbTplVi.get(con);
                cnt ++;
            } while(dbTplVi.tvw_xml == null);
            // template details
            xmlBuf.append(getValuedTemplate(con,prof,tpl,dbTplVi,cos_mgt_ind,cur_time,inEnv, usr_ent_id));
            Vector itm_lst = new Vector();
            itm_lst.addElement(new Long(itm_id));
            Hashtable item_evaluation = dbCourseEvaluation.getCourseEvaluation(con, usr_ent_id, itm_lst);
            if(item_evaluation != null) {
                dbCourseEvaluation dbCosEval = (dbCourseEvaluation)item_evaluation.get(new Long(itm_id));
                if(dbCosEval!=null) {
                    xmlBuf.append("<aicc_data course_id=\"").append(dbCosEval.cov_cos_id)
                        .append("\" student_id=\"").append(dbCosEval.cov_ent_id)
                        .append("\" tkh_id=\"").append(dbCosEval.cov_tkh_id)
                        .append("\" last_acc_datetime=\"").append(aeUtils.escNull(dbCosEval.cov_last_acc_datetime))
                        .append("\" status=\"").append(aeUtils.escNull(dbCosEval.cov_status))
                        .append("\" score=\"").append(aeUtils.escNull(dbCosEval.cov_score))
                        .append("\" comment=\"").append(aeUtils.escNull(dbCosEval.cov_comment))
                        .append("\"/>").append(cwUtils.NEWL);
                }
            }

            xmlBuf.append("<last_updated usr_id=\"").append(itm_upd_usr_id).append("\"");
            xmlBuf.append(" timestamp=\"").append(itm_upd_timestamp).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_upd_usr_id)));
            xmlBuf.append("</last_updated>");

            xmlBuf.append("<creator usr_id=\"").append(itm_create_usr_id).append("\"");
            xmlBuf.append(" timestamp=\"").append(itm_create_timestamp).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_create_usr_id)));
            xmlBuf.append("</creator>");

            if (itm_approval_action!=null){
                xmlBuf.append("<approver usr_id=\"").append(itm_approve_usr_id);
                xmlBuf.append("\" timestamp=\"").append(itm_approve_timestamp);
                xmlBuf.append("\" action=\"").append(itm_approval_action);
                xmlBuf.append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_approve_usr_id)));
                xmlBuf.append("</approver>");
            }

            if (itm_submit_action!=null){
                xmlBuf.append("<submitted usr_id=\"").append(itm_submit_usr_id);
                xmlBuf.append("\" timestamp=\"").append(itm_submit_timestamp);
                xmlBuf.append("\" action=\"").append(itm_submit_action);
                xmlBuf.append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_submit_usr_id)));
                xmlBuf.append("</submitted>");
            }
            xmlBuf.append(dbUtils.NEWL);

            // xml for run infomation
            if(show_respon_run_ind && this.itm_create_run_ind) {
				xmlBuf.append("<run_item_list>");
				Vector v_run = getResponseChildItemAsVector(con, usr_ent_id, rol_ext_id, checkStatus, null);
				for(int i=0; i<v_run.size(); i++) {
					aeItem runItm = (aeItem)v_run.elementAt(i);
					runItm.app_rol_ext_id = this.app_rol_ext_id;
					runItm.itm_retake_ind = this.itm_retake_ind;
					runItm.parent_itm_id = this.itm_id;
					xmlBuf.append(runItm.ItemDetailAsXML(con, inEnv, checkStatus, false, tvw_id, false, show_session_ind, 0, usr_ent_id, cos_mgt_ind, show_attendance_ind,false,null, prof, tcEnabled));
				}
				xmlBuf.append("</run_item_list>");
            }
            if(show_run_ind && this.itm_create_run_ind) {
	            //if has applied run, only get the applied run
	            if(in_run_itm_id == 0) {
	                long run_itm_id = getAppliedRunId(con, usr_ent_id);
	                xmlBuf.append("<run_item_list");
	                if(run_itm_id != 0) {
	                    xmlBuf.append(" applied_run_item_id=\"").append(run_itm_id).append("\"");
	                }
	                xmlBuf.append(">");
                    Vector v_run = getChildItemAsVector(con, checkStatus, true, null);
                    for(int i=0; i<v_run.size(); i++) {
                    	aeItem runItm = (aeItem)v_run.elementAt(i);
                    	runItm.app_rol_ext_id = this.app_rol_ext_id;
                    	runItm.itm_retake_ind = this.itm_retake_ind;
                    	runItm.parent_itm_id = this.itm_id;
                        xmlBuf.append(runItm.ItemDetailAsXML(con, inEnv, checkStatus, false, tvw_id, false, show_session_ind, 0, usr_ent_id, cos_mgt_ind, show_attendance_ind,false,null, prof, tcEnabled));
                    }
	            } else {
	                xmlBuf.append("<run_item_list selected_run_itm_id=\"").append(in_run_itm_id).append("\">");
	                aeItem runItem = new aeItem();
	                runItem.itm_id = in_run_itm_id;
	                runItem.getItem(con);
	                runItem.app_rol_ext_id = this.app_rol_ext_id;
	                runItem.itm_retake_ind = this.itm_retake_ind;
	                runItem.parent_itm_id = this.itm_id;
	                xmlBuf.append(runItem.ItemDetailAsXML(con, inEnv, checkStatus, false, tvw_id, false, show_session_ind, 0, usr_ent_id, cos_mgt_ind, show_attendance_ind,false,null, prof, tcEnabled));
	            }
	            xmlBuf.append("</run_item_list>");
            }
            if (show_session_ind && this.itm_create_session_ind){
                xmlBuf.append("<session_item_list>");
                Vector v_session = getChildItemAsVector(con, checkStatus, true, null);
                for(int i=0; i<v_session.size(); i++) {
                    xmlBuf.append(((aeItem)v_session.elementAt(i)).ItemDetailAsXML(con, inEnv, checkStatus, false, tvw_id, false, show_session_ind, 0, usr_ent_id, cos_mgt_ind, show_attendance_ind,false,null, prof, tcEnabled));
                }
                xmlBuf.append("</session_item_list>");
            }
            //show attendance here
            if(show_attendance_ind && this.itm_has_attendance_ind) {
                aeAttendanceStatus currentStatus = new aeAttendanceStatus();
                xmlBuf.append(aeAttendance.stateAsXML(con, itm_owner_ent_id, itm_id, -1, currentStatus));
            }
            xmlBuf.append("<auto_enroll_interval>" + auto_enroll_interval + "</auto_enroll_interval>");
            if(itm_integrated_ind) {
            	xmlBuf.append("<forum id=\"").append(getIntgForumModId(con)).append("\"/>");
            }
            xmlBuf.append("</item>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }
    
    /**
     * @param con
     * @param usr_ent_id
     * @return
     */
    public boolean checkEnrolled(Connection con, long usr_ent_id,aeApplication app) throws SQLException {
        boolean isEnrolled = false;
        String sql = "select * from aeAttendance, aeApplication, aeAttendanceStatus " +
                "   where att_app_id = app_id " +
                    " and att_ats_id = ats_id " +
                    " and app_ent_id = ? " +
                    " and app_itm_id = ?" +
                    " and ats_type = ?  order by app_id desc";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setLong(1, usr_ent_id);
        ps.setLong(2, itm_id);
        ps.setString(3, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
        
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                isEnrolled = true;
                if(app != null){
                    app.app_id = rs.getLong("app_id");
                    app.app_tkh_id = rs.getLong("app_tkh_id");
                    app.app_itm_id = rs.getLong("app_itm_id");
                }
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return isEnrolled;
    }

    public String object2Xml(Timestamp cur_time)throws SQLException{
    	StringBuffer xmlBuf = new StringBuffer(512);
		xmlBuf.append("<item")
						.append(" id=\"").append(itm_id).append("\"")
						//.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("\"")
						.append(" type=\"").append(itm_type).append("\"")
						///.append(" cos_res_id=\"").append(aeUtils.escZero(cos.cos_res_id)).append("\"")
		                //.append(" ccr_id=\"").append(aeUtils.escZero(ccr_id)).append("\"")
						.append(" status=\"").append(itm_status).append("\"")
						.append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"")
						.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"")
						.append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"")
						.append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"")
						.append(" appn_start_datetime=\"").append(aeUtils.escNull(itm_appn_start_datetime)).append("\"")
						.append(" appn_end_datetime=\"").append(aeUtils.escNull(itm_appn_end_datetime)).append("\"")
						.append(" eff_start_days_to=\"").append(dayDiff(cur_time, itm_eff_start_datetime)).append("\"")
						.append(" eff_end_days_to=\"").append(dayDiff(cur_time, itm_eff_end_datetime)).append("\"")
						.append(" appn_start_days_to=\"").append(dayDiff(cur_time, itm_appn_start_datetime)).append("\"")
						.append(" appn_end_days_to=\"").append(dayDiff(cur_time, itm_appn_end_datetime)).append("\"")
						.append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"")
						.append(" apply_method=\"").append(aeUtils.escNull(itm_apply_method)).append("\"")
						.append(" capacity=\"").append(itm_capacity).append("\"")
						.append(" min_capacity=\"").append(itm_min_capacity).append("\"")
						.append(" unit=\"").append(itm_unit).append("\"")
						.append(" version_code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_version_code))).append("\"")
						.append(" person_in_charge=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_person_in_charge))).append("\"")
						.append(" cancellation_reason=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_reason))).append("\"")
						.append(" cancellation_type=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_type))).append("\"")
						.append(" create_run_ind=\"").append(itm_create_run_ind).append("\"")
						.append(" create_session_ind=\"").append(itm_create_session_ind).append("\"")
						.append(" qdb_ind=\"").append(itm_qdb_ind).append("\"")
						.append(" auto_enrol_qdb_ind=\"").append(itm_auto_enrol_qdb_ind).append("\"")
						.append(" run_ind=\"").append(itm_run_ind).append("\"")
						.append(" session_ind=\"").append(itm_session_ind).append("\"")
						.append(" has_attendance_ind=\"").append(itm_has_attendance_ind).append("\"")
						.append(" ji_ind=\"").append(itm_ji_ind).append("\"")
						.append(" completion_criteria_ind=\"").append(itm_completion_criteria_ind).append("\"")
						.append(" apply_ind=\"").append(itm_apply_ind).append("\"")
						.append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"")
						.append(" can_qr_ind=\"").append(itm_can_qr_ind).append("\"")
						.append(" usr_can_qr_ind=\"").append(usr_can_qr_ind).append("\"")
						.append(" imd_id=\"").append(aeUtils.escZero(itm_imd_id)).append("\"")
						.append(" rsv_id=\"").append(aeUtils.escZero(itm_rsv_id)).append("\"")
                        .append(" tcr_id=\"").append(aeUtils.escZero(itm_tcr_id)).append("\"")
						//.append(" appn_started_ind=\"").append(this.itm_appn_start_datetime == null || this.itm_appn_start_datetime.before(cur_time)).append("\"")
						//.append(" appn_closed_ind=\"").append(this.itm_appn_end_datetime != null && this.itm_appn_end_datetime.before(cur_time)).append("\"")
						//.append(" content_started_ind=\"").append(this.itm_content_eff_start_datetime == null || this.itm_content_eff_start_datetime.before(cur_time)).append("\"")
						//.append(" content_closed_ind=\"").append(this.itm_content_eff_end_datetime != null && this.itm_content_eff_end_datetime.before(cur_time)).append("\"")
						//.append(" enrol_cnt=\"").append(aeApplication.countItemAppn(con, this.itm_id, show_attendance_ind)).append("\"")
						.append(" itm_upd_timestamp=\"").append(aeUtils.escNull(itm_upd_timestamp)).append("\"")
						.append(" bonus_ind=\"").append(itm_bonus_ind).append("\"")
						.append(" diff_factor=\"").append(aeUtils.escZero(itm_diff_factor)).append("\"")
						.append(" offline_pkg=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_offline_pkg))).append("\"")
                        .append(" offline_pkg_file=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_offline_pkg_file)))
						.append("\">");
						//.append(" cos_res_id=\"").append(getResId(con)).append("\"")
				return xmlBuf.toString();
    }

    public String ItemPreviewAsXML(Connection con)
                                    throws SQLException, cwException ,cwSysMessage {

        StringBuffer xmlBuf = new StringBuffer(2500);
        if(myTreeNode != null) {
            xmlBuf.append(myTreeNode.contentAsXML(con));
        }
        Timestamp cur_time = cwSQL.getTime(con);

        dbCourse cos = new dbCourse();
        cos.cos_itm_id = this.itm_id;

        if(this.itm_qdb_ind) {
            cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
        }
        // item attributes
        xmlBuf.append("<item")
                .append(" id=\"").append(itm_id).append("\"")
                .append(" type=\"").append(itm_type).append("\"")
                .append(" cos_res_id=\"").append(aeUtils.escZero(cos.cos_res_id)).append("\"")
                .append(" status=\"").append(itm_status).append("\"")
                .append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"")
                .append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"")
                .append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"")
                .append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"")
                .append(" appn_start_datetime=\"").append(aeUtils.escNull(itm_appn_start_datetime)).append("\"")
                .append(" appn_end_datetime=\"").append(aeUtils.escNull(itm_appn_end_datetime)).append("\"")
                .append(" eff_start_days_to=\"").append(dayDiff(cur_time, itm_eff_start_datetime)).append("\"")
                .append(" eff_end_days_to=\"").append(dayDiff(cur_time, itm_eff_end_datetime)).append("\"")
                .append(" appn_start_days_to=\"").append(dayDiff(cur_time, itm_appn_start_datetime)).append("\"")
                .append(" appn_end_days_to=\"").append(dayDiff(cur_time, itm_appn_end_datetime)).append("\"")
                .append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"")
                .append(" apply_method=\"").append(aeUtils.escNull(itm_apply_method)).append("\"")
                .append(" capacity=\"").append(itm_capacity).append("\"")
                .append(" min_capacity=\"").append(itm_min_capacity).append("\"")
                .append(" unit=\"").append(itm_unit).append("\"")
                .append(" version_code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_version_code))).append("\"")
                .append(" person_in_charge=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_person_in_charge))).append("\"")
                .append(" cancellation_reason=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_reason))).append("\"")
                .append(" cancellation_type=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_type))).append("\"")
                .append(" create_run_ind=\"").append(itm_create_run_ind).append("\"")
                .append(" create_session_ind=\"").append(itm_create_session_ind).append("\"")
                .append(" qdb_ind=\"").append(itm_qdb_ind).append("\"")
                .append(" auto_enrol_qdb_ind=\"").append(itm_auto_enrol_qdb_ind).append("\"")
                .append(" run_ind=\"").append(itm_run_ind).append("\"")
                .append(" session_ind=\"").append(itm_session_ind).append("\"")
                .append(" has_attendance_ind=\"").append(itm_has_attendance_ind).append("\"")
                .append(" ji_ind=\"").append(itm_ji_ind).append("\"")
                .append(" completion_criteria_ind=\"").append(itm_completion_criteria_ind).append("\"")
                .append(" apply_ind=\"").append(itm_apply_ind).append("\"")
                .append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"")
                .append(" can_qr_ind=\"").append(itm_can_qr_ind).append("\"")
                .append(" usr_can_qr_ind=\"").append(usr_can_qr_ind).append("\"")
                .append(" imd_id=\"").append(aeUtils.escZero(itm_imd_id)).append("\"")
                .append(" rsv_id=\"").append(aeUtils.escZero(itm_rsv_id)).append("\"")
                .append(" tcr_id=\"").append(aeUtils.escZero(itm_tcr_id)).append("\"")
                .append(" appn_started_ind=\"").append(this.itm_appn_start_datetime == null || this.itm_appn_start_datetime.before(cur_time)).append("\"")
                .append(" appn_closed_ind=\"").append(this.itm_appn_end_datetime != null && this.itm_appn_end_datetime.before(cur_time)).append("\"")
                .append(" content_started_ind=\"").append(this.itm_content_eff_start_datetime == null || this.itm_content_eff_start_datetime.before(cur_time)).append("\"")
                .append(" content_closed_ind=\"").append(this.itm_content_eff_end_datetime != null && this.itm_content_eff_end_datetime.before(cur_time)).append("\"")
                .append(" enrol_cnt=\"").append(aeApplication.countItemAppn(con, this.itm_id, true)).append("\"")
                .append(" itm_upd_timestamp=\"").append(aeUtils.escNull(itm_upd_timestamp)).append("\"")
                .append(" bonus_ind=\"").append(itm_bonus_ind).append("\"")
                .append(" offline_pkg=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_offline_pkg))).append("\"")
                .append(" offline_pkg_file=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_offline_pkg_file))).append("\"")
                .append(" diff_factor=\"").append(aeUtils.escZero(itm_diff_factor)).append("\"");

            if(this.itm_run_ind || this.itm_session_ind) {
                aeItemRelation aeIre = new aeItemRelation();
                aeIre.ire_child_itm_id = this.itm_id;
                aeItem ireParentItm = aeIre.getParentInfo(con);
                if (ireParentItm != null) {
                    xmlBuf.append(" parent_itm_id=\"").append(ireParentItm.itm_id).append("\"");
                    xmlBuf.append(" parent_title=\"").append(cwUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"");
                }
            }
            xmlBuf.append(">");
            xmlBuf.append(getNavAsXML(con, this.itm_id));

            //item type xml
            xmlBuf.append(aeUtils.escNull(getItemTypeTitle(con)));
            //item title
            xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>").append(dbUtils.NEWL);
            xmlBuf.append("<preview>");
            xmlBuf.append(getTargetPreviewAsXML(con));
            xmlBuf.append("</preview>");
            xmlBuf.append("<last_updated usr_id=\"").append(itm_upd_usr_id).append("\"");
            xmlBuf.append(" timestamp=\"").append(itm_upd_timestamp).append("\">");
            xmlBuf.append("</last_updated>");
            xmlBuf.append(dbUtils.NEWL);

            xmlBuf.append("</item>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }
	public static String getSysEmail(Connection con, loginProfile prof)
		throws SQLException {
		String email = "";
		if (prof != null) {
			email = getSysEmail(con, prof.root_ent_id);
		}
		return email;
	}
    
    public static String getSysEmail(Connection con, long root_ent_id)
		throws SQLException {
		String sysEmail = "";
		try {
			sysEmail = getSysEmailAddress(con, root_ent_id);
		} catch (qdbException e) {
			CommonLog.error(e.getMessage(),e);
			//e.printStackTrace();
		}
		
	    StringBuffer xmlBuf = new StringBuffer();
	    xmlBuf.append("<field24><sys_email>");
	    xmlBuf.append(sysEmail);
	    xmlBuf.append("</sys_email></field24>");
	    xmlBuf.append("<field23><sys_email>");
	    xmlBuf.append(sysEmail);
	    xmlBuf.append("</sys_email></field23>");
	    return xmlBuf.toString();
	}
    
	public static String getSysEmailAddress(Connection con, long root_ent_id)
		throws SQLException, qdbException {
		String email = null;
		if (root_ent_id == 0) {
			email = "";
		} else {
			acSite site = new acSite();
			site.ste_ent_id = root_ent_id;
			DbRegUser sysadmin = new DbRegUser();
			sysadmin.usr_ent_id = site.getSiteSysEntId(con);
			sysadmin.get(con);  	
			email = sysadmin.usr_email;
		}
		return email;
    }
    
    public String getValuedTemplate(Connection con, aeTemplate tpl,
                                     DbTemplateView dbTplVi,
                                     boolean cos_mgt_ind,
                                     Timestamp cur_time,
                                     qdbEnv inEnv,
                                     long usr_ent_id)
    throws SQLException, cwException, cwSysMessage {
    		return getValuedTemplate( con, null, tpl, dbTplVi, cos_mgt_ind, cur_time, inEnv, usr_ent_id);
    }
    
    public String getValuedTemplate(Connection con, loginProfile prof, aeTemplate tpl,
            DbTemplateView dbTplVi, boolean cos_mgt_ind, Timestamp cur_time, qdbEnv inEnv, long usr_ent_id)
        throws SQLException, cwException, cwSysMessage {
         
        // template details
        StringBuffer tplBuf = new StringBuffer(2000);
        tplBuf.append("<applyeasy>").append(dbUtils.NEWL);
        //view template
        tplBuf.append(dbTplVi.tvw_xml).append(dbUtils.NEWL);
        //item template
        tplBuf.append(tpl.tpl_xml).append(dbUtils.NEWL);
        //item detail
        tplBuf.append(itm_xml).append(dbUtils.NEWL);
        //if this item is not an approved version, get the external xml
        //if this item is an approved version, itm_xml is the snapshot, no need to get external xml
        if(this.itm_life_status == null || !this.itm_life_status.equals(ITM_LIFE_STATUS_APPROVED)) {
            //get extra info
            //generate item type list
            tplBuf.append(getAllItemTypeTitleInOrg(con, itm_owner_ent_id));
            // generate item info as external xml
            tplBuf.append(getItemInfoAsExtXML(con));
            // generate link criteria as xml
            tplBuf.append(getLinkCriteriaAsXML(con, cur_time, cos_mgt_ind));
            if(dbTplVi.tvw_cat_ind) {
                // generate assigned nodes xml
                tplBuf.append(getAssignedNodeAsXML(con, usr_ent_id));
            }

            if (dbTplVi.tvw_itm_acc_ind) {
                // generate access control rules xml
                tplBuf.append(getAccessAsXML(con, 0));
            }
            if (dbTplVi.tvw_cm_ind) {
                // generate related competence info xml
                aeItemCompetency aeItc = new aeItemCompetency();
                tplBuf.append(aeItc.asXML(con, this.itm_id, "ASC", "skb_title"));
            }
            if (dbTplVi.tvw_mote_ind) {
                // generate related mote info xml
                tplBuf.append(getMoteAsXML(con));
            }
            if (dbTplVi.tvw_res_ind) {
                // generate related resource info xml
                tplBuf.append(getOnlineContentAsXML(con));
            }
            if (dbTplVi.tvw_rsv_ind) {
                // generate related resource info xml
                tplBuf.append(getRsvAsXML(con));
            }
            if (this.itm_run_ind) {
                tplBuf.append(getCascadeUpdInfoAsXML(con));
            }
            if( dbTplVi.tvw_wrk_tpl_ind ) {
                tplBuf.append(getWorkflowTplIdAsXML(con));
            }
            if(dbTplVi.tvw_cost_center_ind) {
                // generate cost center group xml
                tplBuf.append(this.getCostCenterGroupAsXML(con));
            }
            if (this.itm_run_ind) {
                tplBuf.append(aeItemFigure.getItemRunFigureXML(con, this.itm_id));
            } else {
                tplBuf.append(aeItemFigure.getItemFigureXML(con, this.itm_id, itm_owner_ent_id));
            }
            if(dbTplVi.tvw_ctb_ind) {
                //generate code table xml
                try {
                    tplBuf.append(getCodeTableAsXML(con, tpl));
                } catch(IOException ioe) {
                    throw new cwException ("getValuedTemplate: IOException at getCodeTableAsXML: " + ioe.getMessage());
                }
            }
            if(dbTplVi.tvw_tcr_ind) {
            	getCourseTcrId(con);
                tplBuf.append(getTrainingCenterXML(con));
            }
//            tplBuf.append()
        }
        // get sys email
        tplBuf.append(getSysEmail(con, prof));
        tplBuf.append("</applyeasy>");
        return cwUtils.escCrLfForXml(aeUtils.transformXML(tplBuf.toString(), inEnv.INI_XSL_VALTPL, inEnv, null));

    }

    private void getCourseTcrId(Connection con) {
    	PreparedStatement stmt = null;
        StringBuffer xmlBuf = new StringBuffer();
        try {
        	String sql = "select itm_tcr_id from aeItem"
        			+ " inner join aeItemRelation on itm_id = ire_parent_itm_id"
        			+ " where ire_child_itm_id = ?";
			stmt = con.prepareStatement(sql);
            stmt.setLong(1, this.itm_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
            	this.itm_tcr_id = rs.getLong("itm_tcr_id");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
	}

	//    private static final String sql_get_rsv_4_itm =
//        " Select rsv_desc From fmReservation where rsv_id = ? ";
    /**
    Get reservation details for booking system
    Pre-define variables:
    <ul>
    <li>itm_rsv_id
    <li>itm_eff_start_datetime
    <li>itm_eff_end_datetime
    <ul>
    */
    private String getRsvAsXML(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.sql_get_rsv_4_itm());
        stmt.setLong(1, this.itm_rsv_id);
        ResultSet rs = stmt.executeQuery();
        String rsv_venue = null;
        String fac_title = null;

        if (rs.next()) {
            rsv_venue = rs.getString("rsv_desc");
            fac_title = rs.getString("fac_title");
        }

        stmt.close();
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<rsv_start_datetime>")
              .append("<datetime value=\"").append(this.itm_eff_start_datetime).append("\"/>")
              .append("</rsv_start_datetime>");
        xmlBuf.append("<rsv_end_datetime>")
              .append("<datetime value=\"").append(this.itm_eff_end_datetime).append("\"/>")
              .append("</rsv_end_datetime>");
        xmlBuf.append("<rsv_venue>")
              .append("<venue value=\"").append(cwUtils.esc4XML(aeUtils.escNull(rsv_venue))).append("\"/>")
              .append("</rsv_venue>");
        xmlBuf.append("<rsv_main_room>")
              .append("<main_room value=\"").append(cwUtils.esc4XML(aeUtils.escNull(fac_title))).append("\"/>")
              .append("</rsv_main_room>");
        return xmlBuf.toString();
    }

    public aeTemplate getItemTemplate(Connection con) throws SQLException {
        String xml;
        aeTemplate tpl = new aeTemplate();
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tpl_xml, tpl_id From aeItemTemplate, aeTemplateType, aeTemplate ");
        SQLBuf.append(" Where itp_itm_id = ? ");
        SQLBuf.append(" And itp_ttp_id = ttp_id ");
        SQLBuf.append(" And itp_tpl_id = tpl_id ");
        SQLBuf.append(" And ttp_title = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.setString(2, aeTemplate.ITEM);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            tpl.tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
            tpl.tpl_id = rs.getLong("tpl_id");
        }
        else
            throw new SQLException("Cannot find item template for item, itm_id = " + itm_id);

        stmt.close();
        return tpl;
    }

    public long getTemplateId(Connection con, String tpl_type) throws SQLException {
        /*
        aeItemRelation ire = new aeItemRelation();
        ire.ire_child_itm_id = itm_id;
        long temp_itm_id = itm_id;
        itm_id = ire.getParentItemId(con);
        if(itm_id == 0)
            itm_id = temp_itm_id;
        */
        long tpl_id;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select itp_tpl_id From aeItemTemplate, aeTemplateType ");
        SQLBuf.append(" Where itp_ttp_id = ttp_id ");
        SQLBuf.append(" And itp_itm_id = ? ");
        SQLBuf.append(" And ttp_title = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.setString(2, tpl_type);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            tpl_id = rs.getLong("itp_tpl_id");
        else
            tpl_id = 0;
            //throw new SQLException("Cannot find template " + tpl_type + " for item " + itm_id);
        rs.close();
        stmt.close();
        //itm_id = temp_itm_id;
        return tpl_id;
    }

    public String getRawTemplate(Connection con, String tpl_type) throws SQLException {
        String tpl_xml;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tpl_xml From aeItemTemplate, aeTemplateType, aeTemplate ");
        SQLBuf.append(" Where itp_ttp_id = ttp_id ");
        SQLBuf.append(" And itp_itm_id = ? ");
        SQLBuf.append(" And ttp_title = ? ");
        SQLBuf.append(" And tpl_id = itp_tpl_id ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.setString(2, tpl_type);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
        else
            tpl_xml = null;

        stmt.close();
        return tpl_xml;
    }

    public boolean getItemDeprecatedInd(Connection con) throws SQLException ,cwSysMessage {
        String title;
        final String SQL = " Select itm_deprecated_ind From aeItem "
                         + " Where itm_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            this.itm_deprecated_ind = rs.getBoolean("itm_deprecated_ind");
        else{
            stmt.close();
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        }

        stmt.close();
        return this.itm_deprecated_ind;
    }

    public String getItemType(Connection con) throws SQLException ,cwSysMessage {
        String title;
        final String SQL = " Select itm_type From aeItem "
                         + " Where itm_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            itm_type = rs.getString("itm_type");
        else{
            stmt.close();
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        }

        stmt.close();
        return itm_type;
    }
    
    public static String getItemTypeByCosId(Connection con, long cos_id) throws SQLException  {
        String itm_type = "";
        final String SQL = " Select itm_type From aeItem,course "
                         + " Where itm_id = cos_itm_id and cos_res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()){
            itm_type = rs.getString("itm_type");
        }

        stmt.close();
        return itm_type;
    }

    public static String getItemType(Connection con, long cos_id) throws SQLException {
    	String itm_type = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " SELECT itm_type FROM Course, aeItem WHERE cos_itm_id = itm_id AND cos_res_id = ? ";
		try {
			int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(index++, cos_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				itm_type = rs.getString("itm_type");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return itm_type;
    }

    // delete WZBCourse
    public void delWZBCourse(Connection con, loginProfile prof, dbCourse cos, Timestamp upd_timestamp)
        throws qdbException, cwSysMessage, SQLException, cwException
    {
        if(!isLastUpd(con, upd_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        //System.out.println("check timestamp OK");
        delWZBCourse(con, prof, cos);
    }

	// delete WZBCourse
	public void delWZBCourse(Connection con, loginProfile prof, dbCourse cos) 
        throws qdbException, cwSysMessage, SQLException, cwException
    {
		try {
			if (isIntegratedItem(con, itm_id)) {
				IntegratedLrn.del(con, itm_id);
			}

			String title = isRefByIntegratedItem(con, itm_id);
			if (cwUtils.notEmpty(title)) {
				throw new cwSysMessage("COS004", title);
			}
			
            
            if(hasApplication(con))
                throw new cwSysMessage(aeItem.ITM_DEL_HAS_ENROLLMENT);
            
			
			//check whether the item (course) has enrollment records (TrackingHistory)
			//if yes, do not allow to delete this course
            
			List chril_itm_lst = getChItemIDList(con, cos.cos_itm_id);
			// del child itm
			if (chril_itm_lst != null) {
				aeItem chril_itm = new aeItem();
				dbCourse chril_cos = new dbCourse();
				for (int i = 0; i < chril_itm_lst.size(); i++) {
					chril_itm.itm_id = ((Long) chril_itm_lst.get(i)).longValue();
					chril_cos.cos_itm_id = chril_itm.itm_id;
                    chril_cos.is_complete_del = is_complete_del;
					if (!is_complete_del && chril_itm.getEnrollmentCount(con) > 0) {
						throw new cwSysMessage(ITM_DEL_HAS_ENROLLMENT);
					}
					chril_cos.cos_res_id = dbCourse.getCosResId(con, chril_cos.cos_itm_id);
					 if (is_complete_del) {
	                    	itemCompleteDelete.delOtherRecord(con, chril_cos.cos_res_id, is_complete_del);
	                    }
					chril_cos.aeDel(con, prof);
					chril_itm.del(con, prof.root_ent_id, prof, false);
				}
                
			}

			if (!is_complete_del && getEnrollmentCount(con) > 0) {
				throw new cwSysMessage(ITM_DEL_HAS_ENROLLMENT);
			}
			
			cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
            if (is_complete_del) {
            	itemCompleteDelete.delOtherRecord(con, cos.cos_res_id, is_complete_del);
            }
				
			delEnrolHistory(con); // 删除残留的学习记录
			delCriteria(con); // 删除模块前，先删除完成准则，这样积分项目引用模块的情况下就可以正常删除模块
			
			cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
			cos.aeDel(con, prof);
			//System.out.println("cos.aeDel OK");
			del(con, prof.root_ent_id, prof, false);
			//System.out.println("del OK");
        }
        catch(qdbErrMessage e) {
			throw new cwSysMessage(e.getId());
		}
	}

    //del an item with unkown itm_type
    public void delItem(Connection con, long owner_ent_id, Timestamp upd_timestamp, loginProfile prof)
      throws SQLException, cwSysMessage, qdbException, cwException {
        /*
        if(!hasUpdPrivilege(con, owner_ent_id))
            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
        */
        if(!isLastUpd(con, upd_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

        del(con, owner_ent_id, prof, false);
    }
    /*
        depreciated, call delItemChild instead.
    */
    //delete the run items of this item
    public void delItemRun(Connection con, long owner_ent_id, loginProfile prof)
        throws SQLException, cwSysMessage, qdbException, cwException {
        delItemChild(con, owner_ent_id, prof);
    }
    //delete the child items of this item
    public void delItemChild(Connection con, long owner_ent_id, loginProfile prof)
        throws SQLException, cwSysMessage, qdbException, cwException {
        //System.out.println("Enter del Run: " + this.itm_id);

        Vector v_child_itm_id = aeItemRelation.getChildItemId(con, this.itm_id);
        for(int i=0; i<v_child_itm_id.size(); i++) {
            aeItem childItem = new aeItem();
            childItem.itm_id = ((Long)v_child_itm_id.elementAt(i)).longValue();
            if(childItem.getQdbInd(con)) {
                dbCourse cos = new dbCourse();
                cos.cos_itm_id = childItem.itm_id;
                childItem.delWZBCourse(con, prof, cos);
            }
            else {
                childItem.del(con, owner_ent_id, prof, false);
            }
        }
        //System.out.println("leave del Run: " + this.itm_id);
        return;
    }


    //del aeItem
    public void del(Connection con, long owner_ent_id, loginProfile prof, boolean del_deprecated)
      throws SQLException, cwSysMessage, qdbException, cwException {
        final String SQL = " Delete From aeItem "
                         + " Where itm_id = ? ";
        //check the item requirement first!
        List list = ViewItemRequirement.isWhoseReq(con,itm_id);
        if(list.size()>0){
			List vData = new Vector();
			StringBuffer s = new StringBuffer();
        	for(int i=0,n=list.size();i<n;i++){
        		s.append(list.get(i));
        		if(i!=n-1){
        			s.append(", ");
        		}
        	}
        	vData.add(s.toString());
			throw new cwSysMessage(IS_REQ,(Vector)vData);
        }
        aeItemRequirement req = new aeItemRequirement();
        req.del(con,itm_id);
        
        // get ity_certificate_ind
        String sqlTmp1 = "select ity_certificate_ind from aeItemType,aeItem where ity_id = itm_type and itm_id = ?";
        PreparedStatement stmt1 = con.prepareStatement(sqlTmp1);
        stmt1.setLong(1,itm_id);
        ResultSet rsTmp1 = stmt1.executeQuery();
        int ity_certificate_ind = -1;
        while (rsTmp1.next()) {
            ity_certificate_ind = rsTmp1.getInt("ity_certificate_ind");
        }
        stmt1.close();

        if (ity_certificate_ind == 1) {
            // get ctf_id
            String sqlTmp2 = "select itm_ctf_id from aeItem where itm_id = ?";
            PreparedStatement stmt2 = con.prepareStatement(sqlTmp2);
            stmt2.setLong(1,itm_id);
            ResultSet rsTmp2 = stmt2.executeQuery();
            int ctf_id = -1;
            while (rsTmp2.next()) {
                ctf_id = rsTmp2.getInt("itm_ctf_id");
            }
            stmt2.close();
            //del ctf
            CFCertificate cf = new CFCertificate(con);
            cf.deleteCertificate(ctf_id, (int)owner_ent_id);
        }
        //<<add for bjmu

        //delete all application on item
        aeAttendance.delByItem(con, this.itm_id);
        aeApplication.delByItem(con, this.itm_id);

        // Update the Overall Attendance Rate (if necessary)
        aeItem tempItm = new aeItem();
        tempItm.itm_id = this.itm_id;
        tempItm.get(con);

        //get itm_imd_id
        getMoteId(con);

        //del ItemTemplate
        DbItemTemplate.delByItem(con, itm_id);
        //detach the item from tree nodes
        attachTreeNodes(con, new long[0], null, prof.usr_id, prof.usr_ent_id, owner_ent_id);

        //del ItemAccess
        aeItemAccess.delByItemId(con, itm_id);
        //del cm
        delCompetency(con);
        delCriteria(con);
        //del itemlesson
        delItemLesson(con);
        //del ItemRelation
        if(getRunInd(con) || getSessionInd(con)) {
            //if run, delete aeItemRelation record
            aeItemRelation.delByChild(con, itm_id);
        }
        getCreateRunNSessionInd(con);
        if (itm_create_run_ind || itm_create_session_ind)
            delItemChild(con, owner_ent_id, prof);

        //del the item family if this item is a non-deprecated version
        if(!getItemDeprecatedInd(con) && del_deprecated) {
            Vector v_family = getItemFamily(con);
            for(int i=0; i<v_family.size(); i++) {
                aeItem famItem = (aeItem)v_family.elementAt(i);
                if(famItem.itm_id != this.itm_id) {
                    famItem.del(con, owner_ent_id, prof, del_deprecated);
                }
            }
        }

        //del Learning Solution
        aeLearningSoln.delByItem(con, itm_id);

        //del JI item message
        aeItemMessage.delByItem(con, this.itm_id);


        //del item accreditation
        aeItemFigure aeIfg = new aeItemFigure();
        aeIfg.delAll(con, itm_id);

        //del user accreditation
        aeUserFigure aeUfg = new aeUserFigure();
        aeUfg.delAll(con, itm_id);      

        //del item extension
        aeItemExtension itmExtension = new aeItemExtension();
        itmExtension.del(con, itm_id);
        
        //del item itemTargetLrnDetail  
        InstructorDao.delitemTargetLrnDetailByItmId(con, itm_id);
        
        //del item aeItemTargetRuleDetail  
        InstructorDao.delaeItemTargetRuleDetailByItmId(con, itm_id);
        
        //del item aeItemTargetRule  
        InstructorDao.delAeItemTargetRuleByItmId(con, itm_id);
       
        //检查是否与培训实施计划关联  
        String plan_code = getItemPlanCode(con);
        if (plan_code != null) {
        	dbTpTrainingPlan dbTp = new dbTpTrainingPlan();
        	dbTp.tpn_code = plan_code;
        	long tpn_id = dbTp.getTpPlanIdByCode(con);
        	dbTp.tpn_id = tpn_id;
        	dbTp.get(con);
        	dbTp.tpn_status = dbTpTrainingPlan.TPN_STATUS_APPROVED;
        	dbTp.tpn_update_timestamp = cwSQL.getTime(con);
        	dbTp.tpn_update_usr_id = prof.usr_id;
        	dbTp.upd_status(con);
        }
        
        
        //删除课程相关讲师评论
        InstructorDao.delCommentByItmId(con, itm_id, null);
        
        //删除课程相关赞、动态和评论
        dbSns.deleteCourseLikeDoingComment(con, itm_id);
        
        //del item
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        int row = stmt.executeUpdate();
        stmt.close();
        if(row == 0) {
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        } else if(row > 1) {
            throw new SQLException("More than 1 record in aeItem have itm_id = " + itm_id);
        }

        //del mote
        delMote(con);
    }

    public void getRunNSessionInd(Connection con) throws SQLException {
        final String sql_get_run_session_ind = "Select itm_run_ind, itm_session_ind from aeItem where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_run_session_ind);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_run_ind = rs.getBoolean("itm_run_ind");
            this.itm_session_ind = rs.getBoolean("itm_session_ind");
        }
        rs.close();
        stmt.close();
    }

    public boolean getRunInd(Connection con) throws SQLException {
        final String sql_get_run_ind = "Select itm_run_ind from aeItem where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_run_ind);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_run_ind = rs.getBoolean("itm_run_ind");
        }
        rs.close();
        stmt.close();
        return this.itm_run_ind;
    }

	public static boolean getRunInd(Connection con,long itm_id) throws SQLException {
		final String sql_get_run_ind = "Select itm_run_ind from aeItem where itm_id = ? ";
		boolean run_ind = false;
		PreparedStatement stmt = con.prepareStatement(sql_get_run_ind);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			run_ind = rs.getBoolean("itm_run_ind");
		}
		stmt.close();
		return run_ind;
	}
	public static boolean getSessionInd(Connection con,long itm_id) throws SQLException {
		final String sql_get_session_ind = "Select itm_session_ind from aeItem where itm_id = ? ";
		boolean session_ind = false;
		PreparedStatement stmt = con.prepareStatement(sql_get_session_ind);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			session_ind = rs.getBoolean("itm_session_ind");
		}
		stmt.close();
		return session_ind;
	}

    
    public boolean getSessionInd(Connection con) throws SQLException {
        final String sql_get_session_ind = "Select itm_session_ind from aeItem where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_session_ind);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_session_ind = rs.getBoolean("itm_session_ind");
        }
        rs.close();
        stmt.close();
        return this.itm_session_ind;
    }

    public void getCreateRunNSessionInd(Connection con) throws SQLException {
        final String sql_get_create_run_n_session_ind = "Select itm_create_run_ind , itm_create_session_ind  from aeItem where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_create_run_n_session_ind);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
            itm_create_session_ind = rs.getBoolean("itm_create_session_ind");
        }
        stmt.close();
        return;
    }


    public boolean getCreateRunInd(Connection con) throws SQLException {

        final String sql_get_create_run_ind = "Select itm_create_run_ind from aeItem where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_create_run_ind);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
        }
        rs.close();
        stmt.close();
        return this.itm_create_run_ind;
    }

    public boolean getCreateSessionInd(Connection con) throws SQLException {

        final String sql_get_create_session_ind = "Select itm_create_session_ind from aeItem where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql_get_create_session_ind);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_create_session_ind = rs.getBoolean("itm_create_session_ind");
        }
        stmt.close();
        return this.itm_create_session_ind;
    }

    private static final String sql_get_qdb_ind = "Select itm_qdb_ind from aeItem where itm_id = ? ";
    public boolean getQdbInd(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(sql_get_qdb_ind);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_qdb_ind = rs.getBoolean("itm_qdb_ind");
        }
        stmt.close();
        return this.itm_qdb_ind;
    }

    private static final String sql_check_auto_enrol =
        " Select itm_auto_enrol_qdb_ind, cos_res_id From aeItem, Course " +
        " Where itm_id = cos_itm_id and itm_id = ? ";

    public void autoEnrolWZBCourse(Connection con, long usr_ent_id, String create_usr_id, long owner_ent_id)
        throws SQLException, cwSysMessage, qdbException {

        PreparedStatement stmt = con.prepareStatement(sql_check_auto_enrol);
        int index = 1;
        stmt.setLong(index++, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_auto_enrol_qdb_ind = rs.getBoolean("itm_auto_enrol_qdb_ind");
            this.cos_res_id = rs.getLong("cos_res_id");
            if(this.itm_auto_enrol_qdb_ind) {
                dbEnrolment enr = new dbEnrolment();
                enr.enr_ent_id = usr_ent_id;
                enr.enr_res_id = this.cos_res_id;
                enr.enr_status = dbCourse.COS_ENROLL_OK;
                enr.ins(con, create_usr_id, null, owner_ent_id);
            }
            else {
                stmt.close();
                throw new cwSysMessage(NO_ENROL_RECORD_MSG);
            }
        }
        else {
            stmt.close();
            throw new cwSysMessage(NO_ONLINE_CONTENT_MSG);
        }
        stmt.close();
        return;
    }

    //upd a course
    public void updWZBCourse(Connection con, loginProfile prof, dbCourse cos, Timestamp upd_timestamp,
                             Vector vColName, Vector vColType, Vector vColValue,
                             Vector vClobColName, Vector vClobColValue)
      throws qdbException, cwSysMessage, SQLException,cwException {
            cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
            cos.res_id = cos.cos_res_id;
            cos.res_type = dbResource.RES_TYPE_COS;
            cos.res_upd_date = upd(con, prof.root_ent_id, prof.usr_id, upd_timestamp, vColName, vColType, vColValue, vClobColName, vClobColValue);
            if(cos.res_title != null) {
                cos.aeUpd(con, prof);
            }
    }

    //upd a course, without checking on last update timestamp
    public void updWZBCourseWithoutTimestampChecking(Connection con, loginProfile prof, dbCourse cos,
                                                    Vector vColName, Vector vColType, Vector vColValue,
                                                    Vector vClobColName, Vector vClobColValue)
      throws qdbException, cwSysMessage, SQLException,cwException {
            cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
            cos.res_id = cos.cos_res_id;
            cos.res_type = dbResource.RES_TYPE_COS;
            cos.res_upd_date = updWithoutTimestampChecking(con, prof.root_ent_id, prof.usr_id, vColName, vColType, vColValue, vClobColName, vClobColValue);
            if(cos.res_title != null) {
                cos.aeUpd(con, prof);
            }
    }

    // upd an item
    public Timestamp upd(Connection con, long owner_ent_id, String usr_id, Timestamp upd_timestamp,
                         Vector vColName, Vector vColType, Vector vColValue,
                         Vector vClobColName, Vector vClobColValue)
      throws SQLException, cwSysMessage,cwException {
            if(!isLastUpd(con, upd_timestamp))
                throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

            return updWithoutTimestampChecking(con, owner_ent_id, usr_id,
                                               vColName, vColType, vColValue,
                                               vClobColName, vClobColValue);
    }

    //upd an item, without checking on last update timestamp
    public Timestamp updWithoutTimestampChecking(Connection con, long owner_ent_id, String usr_id,
                         Vector vColName, Vector vColType, Vector vColValue,
                         Vector vClobColName, Vector vClobColValue)
        throws SQLException, cwSysMessage, cwException {
        try {
            if (isItemCodeExist(con, itm_id)){
            	if(Boolean.parseBoolean(vColValue.get(vColName.indexOf("itm_exam_ind")).toString())){//是否为考试
                    throw new cwSysMessage(ITM_EXAM_EXIST_MSG,null,"itm_code");
            	}
                throw new cwSysMessage(ITM_CODE_EXIST_MSG, null, "itm_code");
            }
            Certificate cert =  new Certificate();
            if (itm_cfc_id > 0 && !cert.isICertExist(con,itm_cfc_id)){
                throw new cwSysMessage(CERT_NOT_EXIST);
            }
            
            Timestamp curTime = dbUtils.getTime(con);
            itm_owner_ent_id = owner_ent_id;
            itm_create_usr_id = usr_id;
            itm_create_timestamp = curTime;
            itm_upd_usr_id = usr_id;
            itm_upd_timestamp = curTime;

            //set item static indicators
            DbItemType dbIty = new DbItemType();
            dbIty.ity_id = this.itm_type;
            dbIty.ity_owner_ent_id = this.itm_owner_ent_id;
            dbIty.ity_run_ind = this.itm_run_ind;
            dbIty.ity_session_ind = this.itm_session_ind;
            dbIty.ity_blend_ind = this.itm_blend_ind;
            dbIty.ity_exam_ind = this.itm_exam_ind;
            dbIty.ity_ref_ind = this.itm_ref_ind;


            this.h_ity_inds = dbIty.getInd(con);

            this.itm_create_run_ind = ((Boolean)this.h_ity_inds.get("ity_create_run_ind")).booleanValue();
            this.itm_run_ind = ((Boolean)this.h_ity_inds.get("ity_run_ind")).booleanValue();
            this.itm_create_session_ind = ((Boolean)this.h_ity_inds.get("ity_create_session_ind")).booleanValue();
            this.itm_session_ind = ((Boolean)this.h_ity_inds.get("ity_session_ind")).booleanValue();
            this.itm_has_attendance_ind = ((Boolean)this.h_ity_inds.get("ity_has_attendance_ind")).booleanValue();
            this.itm_ji_ind = ((Boolean)this.h_ity_inds.get("ity_ji_ind")).booleanValue();
            this.itm_completion_criteria_ind = ((Boolean)this.h_ity_inds.get("ity_completion_criteria_ind")).booleanValue();
            this.itm_qdb_ind = ((Boolean)this.h_ity_inds.get("ity_qdb_ind")).booleanValue();
            this.itm_auto_enrol_qdb_ind = ((Boolean)this.h_ity_inds.get("ity_auto_enrol_qdb_ind")).booleanValue();

            //update aeItem status and treenode count
            if(this.itm_status != null && this.itm_status.length() > 0) {
                updItemStatus(con);
            }

            //update aeItem
            updItem(con, vColName, vColType, vColValue, vClobColName, vClobColValue);

            //update aeTreeNode
            if (itm_title != null ){
                aeTreeNode.updItemNodeTitle(con, itm_id, itm_title);
            }
            if (itm_credit_values!=null){
               aeItemFigure ifg = new aeItemFigure();
                // delete all item credits before ins new set of item credits
                 ifg.delAll(con, this.itm_id);
                for (int i=0; i<itm_credit_values.length; i++){
                     ifg.ins(con, usr_id, this.itm_id, itm_credit_values[i]);
                }
            }

            //update fmReservation (rsv_purpose)
            FMReservationManager.updRsvPurpose4Itm(con, this.itm_id, this.itm_create_run_ind);
/*
            // trigger upd attendance when criteria marking date changed
            if (this.itm_completion_criteria_ind && 
                (vColName.contains("itm_content_eff_start_datetime") || vColName.contains("itm_content_eff_end_datetime") || vColName.contains("itm_content_eff_duration") )){
                if(this.itm_create_run_ind){
                    //run_lst
                    Vector v_child_itm_id = aeItemRelation.getChildItemId(con, this.itm_id);
                    for(int i=0; i<v_child_itm_id.size(); i++) {
                        aeAttendance.autoUpdateAttendance(con, usr_id, owner_ent_id, this.itm_id, 0, true, false, false); 
                    }
                } else{
                    aeAttendance.autoUpdateAttendance(con, usr_id, owner_ent_id, this.itm_id, 0, true, false, false); 
                }
            }
*/
            return curTime;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    private static final String sql_upd_itm_can_qr_ind =
        " Update aeItem set itm_can_qr_ind = ? where itm_id = ? ";
    //update itm_apply_ind
    public void updItemCanQrInd(Connection con, Timestamp upd_timestamp) throws SQLException, cwSysMessage {
        if(!isLastUpd(con, upd_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        PreparedStatement stmt = con.prepareStatement(sql_upd_itm_can_qr_ind);
        stmt.setBoolean(1, this.itm_can_qr_ind);
        stmt.setLong(2, this.itm_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    private static final String sql_upd_itm_apply_ind =
        " Update aeItem set itm_apply_ind = ? where itm_id = ? ";
    //update itm_apply_ind
    public void updItemApplyInd(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_upd_itm_apply_ind);
        stmt.setBoolean(1, this.itm_apply_ind);
        stmt.setLong(2, this.itm_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    private static final String sql_upd_itm_code =
        " Update aeItem set itm_code = ? where itm_id = ? ";
    //update itm_code
    public void updItemCode(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_upd_itm_code);
        stmt.setString(1, this.itm_code);
        stmt.setLong(2, this.itm_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    //upd aeItem
    /*
    private void updItem(Connection con, String usr_id, Timestamp curTime)
      throws SQLException, cwSysMessage {
        final String SQL = " Update aeItem Set "
                         //+ " itm_code = ? "
                         + " itm_title = ? "
                         + " ,itm_appn_start_datetime = ? "
                         + " ,itm_appn_end_datetime = ? "
                         + " ,itm_capacity = ? "
                         + " ,itm_unit = ? "
                         + " ,itm_xml = " + cwSQL.getClobNull(con)
                         + " ,itm_upd_timestamp = ? "
                         + " ,itm_upd_usr_id = ? "
                         + " ,itm_eff_start_datetime = ? "
                         + " ,itm_eff_end_datetime = ? "
                         + " ,itm_fee = ? "
                         + " ,itm_fee_ccy = ? "
                         + " ,itm_apply_method = ? "
                         + " ,itm_version_code = ? "
                         + " ,itm_min_capacity = ? "
                         + " ,itm_person_in_charge = ? "
                         + " ,itm_cancellation_reason = ? "
                         + " Where itm_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        //stmt.setString(index++, itm_code);
        stmt.setString(index++, itm_title);
        stmt.setTimestamp(index++, itm_appn_start_datetime);
        stmt.setTimestamp(index++, itm_appn_end_datetime);
        stmt.setLong(index++, itm_capacity);
        //stmt.setString(index++, itm_ext2);
        stmt.setFloat(index++, itm_unit);
        stmt.setTimestamp(index++, curTime);
        stmt.setString(index++, usr_id);
        stmt.setTimestamp(index++, itm_eff_start_datetime);
        stmt.setTimestamp(index++, itm_eff_end_datetime);
        stmt.setFloat(index++, itm_fee);
        stmt.setString(index++, itm_fee_ccy);
        stmt.setString(index++, itm_apply_method);
        stmt.setString(index++, itm_version_code);
        stmt.setLong(index++, itm_min_capacity);
        stmt.setString(index++, itm_person_in_charge);
        stmt.setString(index++, itm_cancellation_reason);
        stmt.setLong(index++, itm_id);

        int row = stmt.executeUpdate();
        stmt.close();

        // Update itm_xml
        stmt = con.prepareStatement(
                " SELECT itm_xml FROM aeItem  "
               + "    Where itm_id = ?  FOR UPDATE ",
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
        {
            cwSQL.setClobValue(con, rs, "itm_xml", itm_xml);
            rs.updateRow();
        }
        stmt.close();

        if(row == 0)
            //throw new SQLException("Cannot find Item. itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        if(row > 1)
            throw new SQLException("More than 1 Item have itm_id = " + itm_id);
    }
    */
    //upd aeItem
    //pre-define variable: itm_id, itm_upd_usr_id, itm_upd_timestamp
    private void updItem(Connection con, Vector vColName, Vector vColType, Vector vColValue,
                         Vector vClobColName, Vector vClobColValue)
      throws SQLException, cwSysMessage,cwException {
        //<<add for bjmu
        // get ity_certificate_ind
        String sqlTmp1 = "select ity_certificate_ind from aeItemType,aeItem "
                    + " where ity_id = itm_type and ity_run_ind = itm_run_ind and ity_session_ind = itm_session_ind and itm_id = ? ";
        PreparedStatement stmt1 = con.prepareStatement(sqlTmp1);
        stmt1.setLong(1,itm_id);
        ResultSet rsTmp1 = stmt1.executeQuery();
        int ity_certificate_ind = -1;
        while (rsTmp1.next()) {
            ity_certificate_ind = rsTmp1.getInt("ity_certificate_ind");
        }
        stmt1.close();

        // get ctf_id
        if (ity_certificate_ind == 1) {
            // get itm_id
//          int itm_id = -1;
//          if (vColName.contains("itm_id")) {
//              int indexId = vColName.indexOf("itm_id");
//              itm_id = Integer.parseInt(vColValue.elementAt(indexId).toString());
//          }
//          // get ctf_id
            String sqlTmp2 = "select itm_ctf_id from aeItem where itm_id = ?";
            PreparedStatement stmt2 = con.prepareStatement(sqlTmp2);
            stmt2.setLong(1,this.itm_id);
            ResultSet rsTmp2 = stmt2.executeQuery();
            int ctf_id = -1;
            while (rsTmp2.next()) {
                ctf_id = rsTmp2.getInt("itm_ctf_id");
            }
            stmt2.close();
            // get itm_title
            String itm_title = "";
            if (vColName.contains("itm_title")) {
                int indexOfId = vColName.indexOf("itm_title");
                itm_title = vColValue.elementAt(indexOfId).toString();
            }
            //upd ctf

            CFCertificate cf = new CFCertificate(con);
            cf.setTitle(itm_title);
            cf.setCtfID(ctf_id);
            cf.updateCertificate(cf,(int)itm_owner_ent_id,itm_upd_usr_id);
        }
        //>>add for bjmu
        vColName.addElement("itm_upd_usr_id");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(this.itm_upd_usr_id);

        vColName.addElement("itm_upd_timestamp");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(this.itm_upd_timestamp);

        if(itm_run_ind) {
            if(!vColName.contains("itm_content_eff_start_datetime") && vColName.contains("itm_eff_start_datetime")) {
                vColName.addElement("itm_content_eff_start_datetime");
                vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
                this.itm_content_eff_start_datetime = (Timestamp) vColValue.elementAt(vColName.indexOf("itm_eff_start_datetime"));
                vColValue.addElement(this.itm_content_eff_start_datetime);
            }

            if(!vColName.contains("itm_content_eff_end_datetime") && vColName.contains("itm_eff_end_datetime")) {
                vColName.addElement("itm_content_eff_end_datetime");
                vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
               // this.itm_content_eff_end_datetime = aeUtils.getTimeAfter((Timestamp) vColValue.elementAt(vColName.indexOf("itm_eff_end_datetime")),Calendar.DATE,CONTENT_EFF_END_BUFFER);
                this.itm_content_eff_end_datetime = (Timestamp) vColValue.elementAt(vColName.indexOf("itm_eff_end_datetime"));
                vColValue.addElement(this.itm_content_eff_end_datetime);
            }
        }

        DbTable dbTab = new DbTable(con);
        //upd Item
        dbTab.upd("aeItem", vColName, vColType, vColValue,
            this.itm_id, "itm_id");

        updChildItemCfcId(con,this.itm_id,itm_cfc_id);
        
        updOterClobColumns(con, vClobColName, vClobColValue);
    }

    /**
     * 课程修改证书时，同时修改班级的证书
     * @param con
     * @param itm_id 课程id
     * @param itm_cfc_id  证书id
     * @throws SQLException 
     */
    private void updChildItemCfcId(Connection con, long itm_id, long itm_cfc_id) {
            String SQL = " update aeItem set itm_cfc_id = ? "
                            + " where itm_id in ( select ire_child_itm_id from   "
                            + " aeItemRelation where ire_parent_itm_id = ? )";
            
                        PreparedStatement stmt;
						try {
							stmt = con.prepareStatement(SQL);
							int index = 1;
	                        stmt.setLong(index++, itm_cfc_id);
	                        stmt.setLong(index++, itm_id);
	                        stmt.executeUpdate();
						} catch (SQLException e) {
							//e.printStackTrace();
						}
    }
    
    
    private static final String sql_upd_itm_life_status =
        " Update aeItem Set itm_life_status = ?, itm_ext1 = ?, " +
        " itm_upd_timestamp = ? , itm_upd_usr_id = ? " +
        " Where itm_id = ? " +
        " And itm_upd_timestamp = ? ";
    public void updItemLifeStatus(Connection con, String userId, Timestamp updTime, Timestamp curTime)
        throws SQLException, cwSysMessage {

        if(curTime == null) {
            curTime = cwSQL.getTime(con);
        }
        PreparedStatement stmt = con.prepareStatement(sql_upd_itm_life_status);
        stmt.setString(1, this.itm_life_status);
        stmt.setString(2, this.itm_ext1);
        stmt.setTimestamp(3, curTime);
        stmt.setString(4, userId);
        stmt.setLong(5, this.itm_id);
        stmt.setTimestamp(6, updTime);
        int rc = stmt.executeUpdate();
        stmt.close();
        if(rc != 1) {
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        }
        return;
    }

    //upd Item Status, multi records
    public void updItemStatus(Connection con, long[] itm_id_lst, String[] itm_status_lst,
                                  Timestamp[] itm_upd_timestamp_lst, long owner_ent_id, String usr_id)
      throws SQLException, cwSysMessage,cwException{
        try {
            Timestamp curTime = dbUtils.getTime(con);
            for(int i=0;i<itm_id_lst.length;i++) {
                itm_id = itm_id_lst[i];
                aeItem itm = new aeItem();
                itm.itm_id=itm_id;
                itm.get(con);
                updItemStatus(con, itm_id_lst[i], itm_status_lst[i], itm_upd_timestamp_lst[i], owner_ent_id, usr_id, curTime);
                if(itm.itm_run_ind){
            		aeItemRelation relation =new aeItemRelation();
            		relation.ire_child_itm_id =itm_id;
            		long parent_itm_id=relation.getParentItemId(con);
            		if(itm_status_lst[i].equalsIgnoreCase(ITM_STATUS_ON)){
            			updItmPublishTime(con, parent_itm_id, curTime);
                	}else{
                		aeItem parent =new aeItem();
                		parent.itm_id=parent_itm_id;
                		Vector childLst=parent.getChildItemAsVector(con, true, false, null);
                		Timestamp lastTime = null ;
                		if(childLst !=null && !childLst.isEmpty()){
	                		for(int j=0; j< childLst.size(); j++){
	                			aeItem child =  (aeItem)childLst.elementAt(j);
	                			if(j==0 ||child.itm_publish_timestamp.after(lastTime)){
	                				lastTime =child.itm_publish_timestamp;
	                			}
	                		}
                		}
                		updItmPublishTime(con, parent_itm_id, lastTime);
                	}
                	
                }
            }
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    
    //upd Item Status, multi records
    public void updItemStatus(Connection con, long[] itm_id_lst, String[] itm_status_lst,
                                  Timestamp[] itm_upd_timestamp_lst, long owner_ent_id, String usr_id,long usr_ent_id,String[] itm_mobile_ind)
      throws SQLException, cwSysMessage,cwException{
        try {
            Timestamp curTime = dbUtils.getTime(con);
            for(int i=0;i<itm_id_lst.length;i++) {
                itm_id = itm_id_lst[i];
                aeItem itm = new aeItem();
                itm.itm_id=itm_id;
                itm.get(con);
                updItemStatus(con, itm_id_lst[i], itm_status_lst[i], itm_upd_timestamp_lst[i], owner_ent_id, usr_id, curTime,usr_ent_id,itm_mobile_ind[i]);
            }
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }


    //get itm_status from database
    //but it will not update this.itm_status
    public String getItemStatus(Connection con) throws SQLException, cwSysMessage {
        String status;
        final String SQL = " Select itm_status from aeItem "
                         + " Where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            status = rs.getString("itm_status");
        }
        else{
            stmt.close();
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        }
        stmt.close();
        return status;
    }

    //upd Item Status
    public void updItemStatus(Connection con, long itm_id, String itm_status
                                , Timestamp itm_upd_timestamp, long owner_ent_id, String usr_id, Timestamp curTime)
      throws SQLException, cwSysMessage,qdbException,cwException {
        try {
            String SQL = " Update aeItem Set "
                            + " itm_status = ? "
                            + " ,itm_publish_timestamp=?"
                            + " ,itm_upd_timestamp = ? "
                            + " ,itm_upd_usr_id = ? ";
            if(itm_status.equalsIgnoreCase(ITM_STATUS_ON) && itm_access_type != null) {
            	SQL += " ,itm_access_type = ?";
            }
            SQL += " Where itm_id = ? ";
            if(!isLastUpd(con, itm_upd_timestamp))
                throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

            this.itm_id = itm_id;
            String prev_status = getItemStatus(con);
            String new_status = itm_status;
            if (itm_status.equalsIgnoreCase(ITM_STATUS_ON)){
                    if (this.isActiveItemCntExceedLimit(con, owner_ent_id)) {
                        throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
                    } else {
                        if(getQdbInd(con)) {
                            dbCourse cos = new dbCourse();
                            //cos.cos_itm_id = itm_id;
                            cos.res_id = dbCourse.getCosResId(con, itm_id);
                            cos.res_status = itm_status;
                            cos.res_upd_user = usr_id;
                            cos.res_upd_date = curTime;
                            cos.aeUpdStatus(con);
                        }

                        PreparedStatement stmt = con.prepareStatement(SQL);
                        int index = 1;
                        stmt.setString(index++, itm_status);
                        stmt.setTimestamp(index++, curTime);
                        stmt.setTimestamp(index++, curTime);
                        stmt.setString(index++, usr_id);
                        if(itm_access_type != null) {
                        	stmt.setString(index++, itm_access_type);
                        }
                        stmt.setLong(index++, itm_id);
                        int row = stmt.executeUpdate();
                        if(row == 0)
                            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
                            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
                        else if(row > 1)
                            throw new SQLException("More than 1 record in aeItem have itm_id = " + itm_id );
                        stmt.close();
                        //update tnd_on_itm_cnt of treenodes
                        if(prev_status != null && new_status != null && !new_status.equals(prev_status)) {
                            if(new_status.equals(ITM_STATUS_ON))
                                cascadeUpdTndOnCnt(con, 1);
                            else if(new_status.equals(ITM_STATUS_OFF))
                                cascadeUpdTndOnCnt(con, -1);
                        }
                        this.itm_status = new_status;
                    }
            }else {
                  if(getQdbInd(con)) {
                        dbCourse cos = new dbCourse();
                        //cos.cos_itm_id = itm_id;
                        cos.res_id = dbCourse.getCosResId(con, itm_id);
                        cos.res_status = itm_status;
                        cos.res_upd_user = usr_id;
                        cos.res_upd_date = curTime;
                        cos.aeUpdStatus(con);
                    }

                    PreparedStatement stmt = con.prepareStatement(SQL);
                    int index=1;
                    stmt.setString(index++, itm_status);
                    stmt.setTimestamp(index++, null);
                    stmt.setTimestamp(index++, curTime);
                    stmt.setString(index++, usr_id);
                    stmt.setLong(index++, itm_id);
                    int row = stmt.executeUpdate();
                    if(row == 0)
                        //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
                        throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
                    else if(row > 1)
                        throw new SQLException("More than 1 record in aeItem have itm_id = " + itm_id );
                    stmt.close();
                    //update tnd_on_itm_cnt of treenodes
                    if(prev_status != null && new_status != null && !new_status.equals(prev_status)) {
                        if(new_status.equals(ITM_STATUS_ON))
                            cascadeUpdTndOnCnt(con, 1);
                        else if(new_status.equals(ITM_STATUS_OFF))
                            cascadeUpdTndOnCnt(con, -1);
                    }
                    this.itm_status = new_status;
                }
        }
        catch(qdbErrMessage e) {
            throw new cwSysMessage(e.getMessage());
        }
    }
    
    //upd Item Status
    public void updItemStatus(Connection con, long itm_id, String itm_status
                                , Timestamp itm_upd_timestamp, long owner_ent_id, String usr_id, Timestamp curTime,long usr_ent_id,String itm_mobile_ind)
      throws SQLException, cwSysMessage,qdbException,cwException {
        try {
            String SQL = " Update aeItem Set "
                            + " itm_status = ? "
                            + " ,itm_publish_timestamp=?"
                            + " ,itm_upd_timestamp = ? "
                            + " ,itm_upd_usr_id = ? "
            				+ " ,itm_mobile_ind = ? ";
            if(itm_status.equalsIgnoreCase(ITM_STATUS_ON) && itm_access_type != null) {
            	SQL += " ,itm_access_type = ?";
            }
            SQL += " Where itm_id = ? ";
            if(!isLastUpd(con, itm_upd_timestamp))
                throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

            this.itm_id = itm_id;
            String prev_status = getItemStatus(con);
            String new_status = itm_status;
            if (itm_status.equalsIgnoreCase(ITM_STATUS_ON)){
                    if (this.isActiveItemCntExceedLimit(con, owner_ent_id)) {
                        throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
                    } else {
                        if(getQdbInd(con)) {
                            dbCourse cos = new dbCourse();
                            //cos.cos_itm_id = itm_id;
                            cos.res_id = dbCourse.getCosResId(con, itm_id);
                            cos.res_status = itm_status;
                            cos.res_upd_user = usr_id;
                            cos.res_upd_date = curTime;
                            cos.aeUpdStatus(con);
                        }

                        PreparedStatement stmt = con.prepareStatement(SQL);
                        int index = 1;
                        stmt.setString(index++, itm_status);
                        stmt.setTimestamp(index++, curTime);
                        stmt.setTimestamp(index++, curTime);
                        stmt.setString(index++, usr_id);
                        stmt.setString(index++, itm_mobile_ind);
                        if(itm_access_type != null) {
                        	stmt.setString(index++, itm_access_type);
                        }
                        stmt.setLong(index++, itm_id);
                        int row = stmt.executeUpdate();
                        if(row == 0)
                            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
                            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
                        else if(row > 1)
                            throw new SQLException("More than 1 record in aeItem have itm_id = " + itm_id );

                        //update tnd_on_itm_cnt of treenodes
                        if(prev_status != null && new_status != null && !new_status.equals(prev_status)) {
                            if(new_status.equals(ITM_STATUS_ON))
                                cascadeUpdTndOnCnt(con, 1);
                            else if(new_status.equals(ITM_STATUS_OFF))
                                cascadeUpdTndOnCnt(con, -1);
                        }
                        this.itm_status = new_status;
                    }
                    
                    if(itm_access_type!=null&&itm_access_type.equalsIgnoreCase(ITM_STATUS_ALL)){
                    	if(usrTargetItmLstHS.get(new Long (usr_ent_id)) != null){
                    		Iterator it = usrTargetItmLstHS.entrySet().iterator() ;
                    		while(it.hasNext()){
                    			 Entry entry = (Entry) it.next();
                    			 long uid = ((Long) entry.getKey()).longValue() ;
                    			 Vector targetItmLst = (Vector) entry.getValue() ;
                    			 targetItmLst.add(itm_id) ;

                    			 CommonLog.info("add>>>>>>>>>>>>            "+targetItmLst.size()) ;
                    		}	
                		}
                    }else{
                    	Iterator it = usrTargetItmLstHS.entrySet().iterator() ;
                		while(it.hasNext()){
                			Entry entry = (Entry) it.next();
                			long uid = ((Long) entry.getKey()).longValue() ;
                			Vector targetItmLst = (Vector) entry.getValue() ;
                			targetItmLst.remove(itm_id) ;
                			CommonLog.info("del>>>>>>>>>>>>            "+targetItmLst.size()) ;
                		}
                    	
                    }
                    
                    //发布课程时,更新课程目标学员的中间表
                    ManageItemTarget.setTargetCache( con,  itm_id, true);
            }else {
                  if(getQdbInd(con)) {
                        dbCourse cos = new dbCourse();
                        //cos.cos_itm_id = itm_id;
                        cos.res_id = dbCourse.getCosResId(con, itm_id);
                        cos.res_status = itm_status;
                        cos.res_upd_user = usr_id;
                        cos.res_upd_date = curTime;
                        cos.aeUpdStatus(con);
                    }

                    PreparedStatement stmt = con.prepareStatement(SQL);
                    int index=1;
                    stmt.setString(index++, itm_status);
                    stmt.setTimestamp(index++, null);
                    stmt.setTimestamp(index++, curTime);
                    stmt.setString(index++, usr_id);
                    stmt.setString(index++, itm_mobile_ind);
                    stmt.setLong(index++, itm_id);
                    int row = stmt.executeUpdate();
                    if(row == 0)
                        //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
                        throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
                    else if(row > 1)
                        throw new SQLException("More than 1 record in aeItem have itm_id = " + itm_id );

                    //update tnd_on_itm_cnt of treenodes
                    if(prev_status != null && new_status != null && !new_status.equals(prev_status)) {
                        if(new_status.equals(ITM_STATUS_ON))
                            cascadeUpdTndOnCnt(con, 1);
                        else if(new_status.equals(ITM_STATUS_OFF))
                            cascadeUpdTndOnCnt(con, -1);
                    }
                    this.itm_status = new_status;
                }
        }
        catch(qdbErrMessage e) {
            throw new cwSysMessage(e.getMessage());
        }
    }
    
    public void updAutoEnrolType(Connection con, Timestamp itm_upd_timestamp, String usr_id) throws SQLException, cwSysMessage{
		if(!isLastUpd(con, itm_upd_timestamp)) {
			throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
		}
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		itm.getItem(con);
		if(itm.itm_create_run_ind) {
			throw new cwSysMessage("XMG002");
		}
		Timestamp curTime = cwSQL.getTime(con);
		String SQL = " Update aeItem Set "
			+ " itm_enroll_type = ? "
			+ " ,itm_upd_timestamp = ? "
			+ " ,itm_upd_usr_id = ? ";
		SQL += " Where itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, itm_enroll_type);
        stmt.setTimestamp(2, curTime);
        stmt.setString(3, usr_id);
        stmt.setLong(4, itm_id);
        stmt.executeUpdate();
        stmt.close();
    }


    //upd Item Status, don't check and update upd_timestamp
    void updItemStatus(Connection con)
      throws SQLException, cwSysMessage,qdbException,cwException {
        try {
            final String SQL = " Update aeItem Set "
                            + " itm_status = ? "
                            + " Where itm_id = ? ";

            String prev_status = getItemStatus(con);
            String new_status = itm_status;
            if (itm_status.equalsIgnoreCase(ITM_STATUS_ON)){
                if (isActiveItemCntExceedLimit(con, this.itm_owner_ent_id)) {
                    throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
                } else {
                    if(prev_status != null && new_status != null && !new_status.equals(prev_status)) {
                        if(getQdbInd(con)) {
                            dbCourse cos = new dbCourse();
                            //cos.cos_itm_id = itm_id;
                            cos.res_id = dbCourse.getCosResId(con, itm_id);
                            cos.res_status = itm_status;
                            cos.aeUpdStatusNoTimestamp(con);
                        }

                        PreparedStatement stmt = con.prepareStatement(SQL);
                        stmt.setString(1, itm_status);
                        stmt.setLong(2, itm_id);
                        int row = stmt.executeUpdate();
                        stmt.close();
                        if(row == 0)
                            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
                        else if(row > 1)
                            throw new SQLException("More than 1 record in aeItem have itm_id = " + itm_id );

                        //update tnd_on_itm_cnt of treenodes
                        if(new_status.equals(ITM_STATUS_ON))
                            cascadeUpdTndOnCnt(con, 1);
                        else if(new_status.equals(ITM_STATUS_OFF))
                            cascadeUpdTndOnCnt(con, -1);
                    }
                }
            } else {
                if(prev_status != null && new_status != null && !new_status.equals(prev_status)) {
                    if(getQdbInd(con)) {
                        dbCourse cos = new dbCourse();
                        //cos.cos_itm_id = itm_id;
                        cos.res_id = dbCourse.getCosResId(con, itm_id);
                        cos.res_status = itm_status;
                        cos.aeUpdStatusNoTimestamp(con);
                    }

                    PreparedStatement stmt = con.prepareStatement(SQL);
                    stmt.setString(1, itm_status);
                    stmt.setLong(2, itm_id);
                    int row = stmt.executeUpdate();
                    stmt.close();
                    if(row == 0)
                        throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
                    else if(row > 1)
                        throw new SQLException("More than 1 record in aeItem have itm_id = " + itm_id );

                    //update tnd_on_itm_cnt of treenodes
                    if(new_status.equals(ITM_STATUS_ON))
                        cascadeUpdTndOnCnt(con, 1);
                    else if(new_status.equals(ITM_STATUS_OFF))
                        cascadeUpdTndOnCnt(con, -1);
                }
            }
        }
        catch(qdbErrMessage e) {
            throw new cwSysMessage(e.getMessage());
        }
    }

    //cascade update all the item treenodes count attached to this item
    //this will update both tnd_cnt and tnd_on_cnt
    private void cascadeUpdTndCnt(Connection con, long amt)
        throws SQLException, qdbException, cwSysMessage {
        final String SQL = " Select r.tnd_id AS r_tnd_id, r.tnd_parent_tnd_id AS r_tnd_parent_tnd_id "
                         + " From aeTreeNode d, aeTreeNode r "
                         + " Where d.tnd_itm_id = ? "
                         + " And d.tnd_type = ? "
                         + " And d.tnd_parent_tnd_id = r.tnd_id ";

        String status = (this.itm_status == null) ? getItemStatus(con) : this.itm_status;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.setString(2, aeTreeNode.TND_TYPE_ITEM);
        ResultSet rs = stmt.executeQuery();
        aeTreeNode tnd;
        while(rs.next()) {
            tnd = new aeTreeNode();
            tnd.tnd_id = rs.getLong("r_tnd_id");
            tnd.tnd_parent_tnd_id = rs.getLong("r_tnd_parent_tnd_id");
            tnd.cascadeUpdItmCntBy(con, amt);
            if(status != null && status.equals(ITM_STATUS_ON)) {
                tnd.cascadeUpdOnItmCntBy(con, amt);
            }
        }
        stmt.close();
    }

    //when turn on/off the status of an item, cascade update all the item treenodes attached to it
    private void cascadeUpdTndOnCnt(Connection con, long amt)
        throws SQLException, qdbException, cwSysMessage {
        final String SQL = " Select r.tnd_id AS r_tnd_id, r.tnd_parent_tnd_id AS r_tnd_parent_tnd_id "
                         + " From aeTreeNode d, aeTreeNode r "
                         + " Where d.tnd_itm_id = ? "
                         + " And d.tnd_type = ? "
                         + " And d.tnd_parent_tnd_id = r.tnd_id ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.setString(2, aeTreeNode.TND_TYPE_ITEM);
        ResultSet rs = stmt.executeQuery();
        aeTreeNode tnd;
        while(rs.next()) {
            tnd = new aeTreeNode();
            tnd.tnd_id = rs.getLong("r_tnd_id");
            tnd.tnd_parent_tnd_id = rs.getLong("r_tnd_parent_tnd_id");
            tnd.cascadeUpdOnItmCntBy(con, amt);
        }
        stmt.close();
    }

    //ins a course
    public void insWZBCourse(Connection con, loginProfile prof, dbCourse cos, long tnd_parent_tnd_id
                     ,String[] templateTypes, long[] tpl_ids,
                     Vector vColName, Vector vColType, Vector vColValue,
                     Vector vClobColName, Vector vClobColValue)
        throws qdbException, cwSysMessage, SQLException, cwException {
            //ins into aeItm
            cos.cos_itm_id = ins(con, prof.root_ent_id, prof.usr_id, tnd_parent_tnd_id, templateTypes, tpl_ids, prof, vColName, vColType, vColValue, vClobColName, vClobColValue);
            cos.res_create_date = itm_create_timestamp;
            cos.res_upd_date = itm_upd_timestamp;
            cos.res_status = itm_status;
            cos.ins(con, prof);
            
            if(itm_integrated_ind) {
                dbModule mod = new dbModule();
                mod.mod_type = dbModule.MOD_TYPE_FOR;
                mod.res_title = itm_title;
                
                mod.res_status = dbResource.RES_STATUS_ON;
                mod.res_subtype = dbModule.MOD_TYPE_FOR;
                mod.res_type = dbResource.RES_TYPE_MOD;
                mod.res_privilege = dbResource.RES_PRIV_AUTHOR;
                mod.res_upd_user = prof.usr_id;
                mod.res_usr_id_owner = prof.usr_id;
                mod.mod_in_eff_start_datetime = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                mod.mod_in_eff_end_datetime = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
                
                cos.insModule(con, mod, "", prof);
                cos.cos_structure_xml ="<tableofcontents identifier=\"TOC1\" title=\"$itm_title_\"><item identifier=\"ITEM1\" identifierref=\"$mod_id_\" title=\"$mod_title_\"><itemtype>MOD</itemtype><restype>$mod_type_</restype></item></tableofcontents>";
                cos.cos_structure_xml = dbUtils.subsitute(cos.cos_structure_xml, "$itm_title_" , dbUtils.esc4XML(itm_title));
                cos.cos_structure_xml = dbUtils.subsitute(cos.cos_structure_xml, "$mod_id_" , Long.toString(mod.mod_res_id));
                cos.cos_structure_xml = dbUtils.subsitute(cos.cos_structure_xml, "$mod_title_" , dbUtils.esc4XML(mod.res_title));
                cos.cos_structure_xml = dbUtils.subsitute(cos.cos_structure_xml, "$mod_type_" , mod.mod_type);
                cos.updCosStructure(con);
            }

    }

    private static final String sql_auto_gen_itm_code =
        " Update aeItem Set itm_code = ? where itm_id = ? ";
    private static final String sql_auto_gen_run_itm_code =
        " Update aeItem Set itm_code = " +
        " (Select parent.itm_code From aeItem parent, aeItemRelation, aeItem run " +
        " Where parent.itm_id = ire_parent_itm_id " +
        " And run.itm_id = ire_child_itm_id " +
        " And run.itm_id = ? ) " +
        " Where itm_id = ? ";
    /**
    Auto generate itm_code<Br>
    If it is not a run: itm_code = "s" + this.itm_owner_ent_id + "i" + this.itm_id<BR>
    If it is a run: itm_code = parent item's itm_code<BR>
    @return itm_code generated
    */
    public String autoGenItemCode(Connection con) throws SQLException {
        PreparedStatement stmt;
        stmt = con.prepareStatement(sql_auto_gen_itm_code);
        this.itm_code = "s" + this.itm_owner_ent_id + "i" + this.itm_id;
        stmt.setString(1, this.itm_code);
        stmt.setLong(2, this.itm_id);
        stmt.executeUpdate();
        stmt.close();
        /*
        if(this.itm_run_ind) {
            stmt = con.prepareStatement(sql_auto_gen_run_itm_code);
            stmt.setLong(1, this.itm_id);
            stmt.setLong(2, this.itm_id);
            stmt.executeUpdate();
            stmt.close();
            this.itm_code = getItemCode(con);
        }
        else {
            stmt = con.prepareStatement(sql_auto_gen_itm_code);
            this.itm_code = "s" + this.itm_owner_ent_id + "i" + this.itm_id;
            stmt.setString(1, this.itm_code);
            stmt.setLong(2, this.itm_id);
            stmt.executeUpdate();
            stmt.close();
        }
        */
        return this.itm_code;
    }

    //ins an item node
    //itm_code need to be unique in organization if itm_code != null
    public long ins(Connection con, long owner_ent_id, String usr_id, long tnd_parent_tnd_id,
                    String[] templateTypes, long[] tpl_ids, loginProfile prof,
                    Vector vColName, Vector vColType, Vector vColValue,
                    Vector vClobColName, Vector vClobColValue)
      throws SQLException, cwException, cwSysMessage {
        // check for create item restriction (2002.03.26 kawai)
        if (isActiveItemCntExceedLimit(con, owner_ent_id)) {
            throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
        }
        try {
        	 if (itm_code!=null && isItemCodeExist(con, 0)){
        		if(Boolean.parseBoolean(vColValue.get(vColName.indexOf("itm_exam_ind")).toString())){//是否为考试
        			throw new cwSysMessage(ITM_EXAM_EXIST_MSG,null,"itm_code");
        		}
 				throw new cwSysMessage(ITM_CODE_EXIST_MSG, null, "itm_code");
             }
            
            Certificate cert =  new Certificate();
            if (itm_cfc_id > 0 && !cert.isICertExist(con,itm_cfc_id)){
                throw new cwSysMessage(CERT_NOT_EXIST);
            }
            
            aeTreeNode parentNode=null;
            Timestamp curTime = dbUtils.getTime(con);

            itm_owner_ent_id = owner_ent_id;
            itm_create_usr_id = usr_id;
            itm_create_timestamp = curTime;
            itm_upd_usr_id = usr_id;
            itm_upd_timestamp = curTime;

            String valStr = vClobColValue.get(0).toString();
            if(itm_icon == null && valStr.indexOf("<field99>") > 0){
                String icon=valStr.substring(valStr.indexOf("<field99>") + 9, valStr.indexOf("</field99>"));
                itm_icon=icon;
            }
            //ins into aeItem
            insItem(con, vColName, vColType, vColValue);
            
            //ins into aeItemTemplate
            ViewItemTemplate viItmTpl = new ViewItemTemplate();
            viItmTpl.itemId = itm_id;
            for(int i=0; i<templateTypes.length; i++) {
                viItmTpl.templateType = templateTypes[i];
                viItmTpl.templateId = tpl_ids[i];
                viItmTpl.insItemTemplate(con, usr_id, curTime);
            }
            // for other clob columns
            updOterClobColumns(con, vClobColName, vClobColValue);
            
            // insert this item as a run if run_ind is true
            // what if for item type of "PROGRAM"????
            //System.out.println("itm_run_ind = " + this.itm_run_ind);
            //System.out.println("parent_itm_id = " + this.parent_itm_id);
            if ((this.itm_run_ind || this.itm_session_ind ) && this.parent_itm_id != 0) {
                //System.out.println("attach Parent");
                attachParent(con, this.parent_itm_id);
            }
            if (this.itm_run_ind && this.parent_itm_id != 0 && itm_credit_values==null){
                aeItemFigure aeIfg = new aeItemFigure();
                aeIfg.insParentFigure(con, prof, this.parent_itm_id, this.itm_id);
            }
            if (this.itm_session_ind && this.itm_has_attendance_ind){
                Vector v_session_itm_id = new Vector();
                v_session_itm_id.addElement(new Long(this.itm_id));
                aeSession.insSessionAttendance(con, this.parent_itm_id, v_session_itm_id, null, 0, this.itm_upd_usr_id, itm_owner_ent_id);
            }
            //auto-generate itm_code Or
            //fill it with itm_code from parent item if it is a run
            if(this.itm_code == null) {
                autoGenItemCode(con);
            }
            //ins into aeTreeNode
            if(tnd_parent_tnd_id != 0) {
                myTreeNode = new aeTreeNode();
                myTreeNode.tnd_cat_id = parentNode.tnd_cat_id;
                myTreeNode.tnd_parent_tnd_id = parentNode.tnd_id;
                myTreeNode.tnd_itm_id = itm_id;
                myTreeNode.tnd_create_usr_id = itm_create_usr_id;
                myTreeNode.tnd_create_timestamp = itm_create_timestamp;
                myTreeNode.tnd_upd_usr_id = itm_upd_usr_id;
                myTreeNode.tnd_upd_timestamp = itm_upd_timestamp;
                myTreeNode.tnd_id = myTreeNode.insItmNode(con, owner_ent_id);
            }
            // ins credit value to this item only (not carry to run level)
            if (itm_credit_values!=null){
                aeItemFigure ifg = new aeItemFigure();
                // delete all itemcredit before ins new set of item credit
                 ifg.delAll(con, this.itm_id);
                for (int i=0; i<itm_credit_values.length; i++){
                     ifg.ins(con, usr_id, this.itm_id, itm_credit_values[i]);
                }
            }

            // insert CourseCriteria
            if (this.itm_completion_criteria_ind && !this.itm_run_ind) {
                //CourseCriteria ccr = new CourseCriteria();
				CourseCriteria.insCriteria(con, itm_id, DbCourseCriteria.TYPE_COMPLETION, DbCourseCriteria.UPD_METHOD_AUTO, 0,
                    0, false, true, prof.usr_id,null, false);
            }
            
            if(itm_integrated_ind) {
            	DbIntegCourseCriteria dbIcc = new DbIntegCourseCriteria();
            	dbIcc.icc_itm_id = itm_id;
            	dbIcc.icc_create_timestamp = curTime;
            	dbIcc.icc_create_usr_id = prof.usr_id;
            	dbIcc.icc_update_timestamp = curTime;
            	dbIcc.icc_update_usr_id = prof.usr_id;
            	dbIcc.ins(con);
            }

            return itm_id;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    public void updOterClobColumns(Connection con, Vector vClobColName, Vector vClobColValue) throws SQLException {
    	if (vClobColName != null && vClobColName.size() > 0) {
        	if (vClobColName.contains("itm_xml") && this.itm_xml != null) {
        		try {
					Hashtable srhContentFieldHt = this.getSrhContentField(con);
	        		this.itm_srh_content = new SimpleXmlToTextParser().parseXml2Text(this.itm_xml, srhContentFieldHt);
	        		vClobColName.addElement("itm_srh_content");
	        		vClobColValue.addElement(this.itm_srh_content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					CommonLog.error(e.getMessage(),e);
				} catch (cwException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					CommonLog.error(e.getMessage(),e);
				}
        	}
            String columnName[]  = new String[vClobColName.size()];
            String columnValue[] = new String[vClobColName.size()];
            for(int i = 0; i < vClobColName.size(); i++) {
                columnName[i]  = (String)vClobColName.elementAt(i);
                columnValue[i] = (String)vClobColValue.elementAt(i);
            }
            String tableName = "aeItem";
            String condition = "itm_id = " + this.itm_id;
            cwSQL.updateClobFields(con, tableName, columnName, columnValue, condition);
        }
    }
    /**
    Get the initial itm_life_status from aeItemType
    If this.itm_run_ind = true, hardcoded itm_life_status = null
    Pre-define variables:
    <ul>
    <li>itm_owner_ent_id
    <li>itm_run_ind
    <li>itm_session
    </ul>
    */
    private String getInitItemLifeStatus(Connection con) throws SQLException, cwSysMessage {
        DbItemType dbIty = new DbItemType();
        dbIty.ity_id = this.itm_type;
        dbIty.ity_owner_ent_id = this.itm_owner_ent_id;
        dbIty.ity_run_ind = this.itm_run_ind;
        dbIty.ity_session_ind = this.itm_session_ind;
        this.itm_life_status = dbIty.getItemTypeInitLifeStatus(con);
        return this.itm_life_status;
    }
// predefine itm_run_ind, itm_session_ind
    private void insItem(Connection con, Vector vColName, Vector vColType, Vector vColValue) throws SQLException, cwSysMessage {
        vColName.addElement("itm_owner_ent_id");
        vColType.addElement(DbTable.COL_TYPE_LONG);
        vColValue.addElement(new Long(itm_owner_ent_id));

        vColName.addElement("itm_create_usr_id");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_create_usr_id);

        vColName.addElement("itm_create_timestamp");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(itm_create_timestamp);

        vColName.addElement("itm_upd_usr_id");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_upd_usr_id);

        vColName.addElement("itm_upd_timestamp");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(itm_upd_timestamp);
        
        if(!vColName.contains("itm_icon")) {
	        vColName.addElement("itm_icon");
	        vColType.addElement(DbTable.COL_TYPE_STRING);
	        vColValue.addElement(itm_icon);
        }
        
        //itm_deprecated_ind should be false by default
        if(!vColName.contains("itm_deprecated_ind")) {
            vColName.addElement("itm_deprecated_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(new Boolean(false));
            this.itm_deprecated_ind = false;
        }

        //itm_status should be OFF by default
        if(!vColName.contains("itm_status")) {
            vColName.addElement("itm_status");
            vColType.addElement(DbTable.COL_TYPE_STRING);
            vColValue.addElement("OFF");
            this.itm_status = "OFF";
        }
        
        if (this.itm_status.equals(ITM_STATUS_ON)) {
			vColName.addElement("itm_publish_timestamp");
	        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
	        vColValue.addElement(itm_create_timestamp);
		}
        
        //get init itm_life_status
        if(!vColName.contains("itm_life_status")) {
            vColName.addElement("itm_life_status");
            vColType.addElement(DbTable.COL_TYPE_STRING);

            this.itm_life_status = getInitItemLifeStatus(con);
            vColValue.addElement(this.itm_life_status);
        }

        if(!vColName.contains("itm_can_qr_ind")) {
            vColName.addElement("itm_can_qr_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(new Boolean(false));
            this.itm_can_qr_ind = false;
        }

        if(itm_run_ind) {
            if(!vColName.contains("itm_content_eff_start_datetime") && vColName.contains("itm_eff_start_datetime")) {
                vColName.addElement("itm_content_eff_start_datetime");
                vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
                this.itm_content_eff_start_datetime = (Timestamp) vColValue.elementAt(vColName.indexOf("itm_eff_start_datetime"));
                vColValue.addElement(this.itm_content_eff_start_datetime);
            }

            if(!vColName.contains("itm_content_eff_end_datetime") && vColName.contains("itm_eff_end_datetime")) {
                vColName.addElement("itm_content_eff_end_datetime");
                vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
               // this.itm_content_eff_end_datetime = aeUtils.getTimeAfter((Timestamp) vColValue.elementAt(vColName.indexOf("itm_eff_end_datetime")),Calendar.DATE,CONTENT_EFF_END_BUFFER);
                this.itm_content_eff_end_datetime = (Timestamp) vColValue.elementAt(vColName.indexOf("itm_eff_end_datetime"));
                vColValue.addElement(this.itm_content_eff_end_datetime);
            }
        }


        //initialize item indicators
        //1.get indicators from aeItemType and put into this.h_ity_inds
        //2.put the indicators back to Vectors [TODO]
        DbItemType dbIty = new DbItemType();
        dbIty.ity_id = this.itm_type;
        dbIty.ity_owner_ent_id = this.itm_owner_ent_id;
        dbIty.ity_run_ind = this.itm_run_ind;
        dbIty.ity_session_ind = this.itm_session_ind;
        dbIty.ity_blend_ind = this.itm_blend_ind;
        dbIty.ity_exam_ind = this.itm_exam_ind;
        dbIty.ity_ref_ind = this.itm_ref_ind;
        this.h_ity_inds = dbIty.getInd(con);

        //itm_can_cancel_ind
        if(!vColName.contains("itm_can_cancel_ind")) {
            vColName.addElement("itm_can_cancel_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_can_cancel_ind"));
            this.itm_can_cancel_ind = ((Boolean)this.h_ity_inds.get("ity_can_cancel_ind")).booleanValue();
            CommonLog.debug("Add Item: itm_can_cancel_ind" + itm_can_cancel_ind);
        }
        //apply_ind
        if(!vColName.contains("itm_apply_ind")) {
            vColName.addElement("itm_apply_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_apply_ind"));
            this.itm_apply_ind = ((Boolean)this.h_ity_inds.get("ity_apply_ind")).booleanValue();
        }
        //create_run_ind
        if(!vColName.contains("itm_create_run_ind")) {
            vColName.addElement("itm_create_run_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_create_run_ind"));
            this.itm_create_run_ind = ((Boolean)this.h_ity_inds.get("ity_create_run_ind")).booleanValue();
        }
        //create_session_ind
        if(!vColName.contains("itm_create_session_ind")) {
            vColName.addElement("itm_create_session_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_create_session_ind"));
            this.itm_create_session_ind = ((Boolean)this.h_ity_inds.get("ity_create_session_ind")).booleanValue();
        }
        //has_attendance_ind
        if(!vColName.contains("itm_has_attendance_ind")) {
            vColName.addElement("itm_has_attendance_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_has_attendance_ind"));
            this.itm_has_attendance_ind = ((Boolean)this.h_ity_inds.get("ity_has_attendance_ind")).booleanValue();
        }
        if(!vColName.contains("itm_ji_ind")) {
            vColName.addElement("itm_ji_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_ji_ind"));
            this.itm_ji_ind = ((Boolean)this.h_ity_inds.get("ity_ji_ind")).booleanValue();
        }
        if(!vColName.contains("itm_completion_criteria_ind")) {
            vColName.addElement("itm_completion_criteria_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_completion_criteria_ind"));
            this.itm_completion_criteria_ind = ((Boolean)this.h_ity_inds.get("ity_completion_criteria_ind")).booleanValue();
        }
        //qdb_ind
        if(!vColName.contains("itm_qdb_ind")) {
            vColName.addElement("itm_qdb_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_qdb_ind"));
            this.itm_qdb_ind = ((Boolean)this.h_ity_inds.get("ity_qdb_ind")).booleanValue();
        }
        //auto_enrol_qdb_ind
        if(!vColName.contains("itm_auto_enrol_qdb_ind")) {
            vColName.addElement("itm_auto_enrol_qdb_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_auto_enrol_qdb_ind"));
            this.itm_auto_enrol_qdb_ind = ((Boolean)this.h_ity_inds.get("ity_auto_enrol_qdb_ind")).booleanValue();
        }
        //run_ind
        if(!vColName.contains("itm_run_ind")) {
            vColName.addElement("itm_run_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_run_ind"));
            this.itm_run_ind = ((Boolean)this.h_ity_inds.get("ity_run_ind")).booleanValue();
        }
        //session_ind
        if(!vColName.contains("itm_session_ind")) {
            vColName.addElement("itm_session_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(this.h_ity_inds.get("ity_session_ind"));
            this.itm_session_ind = ((Boolean)this.h_ity_inds.get("ity_session_ind")).booleanValue();
        }

        //<<add for bjmu--------------------------------------------------------
        // get itm_type

        if (vColName.contains("itm_type")) {
            String itm_type = "";
            int indexID = vColName.indexOf("itm_type");
            itm_type = vColValue.elementAt(indexID).toString();
            // get ity_certificate_ind
            String sqlTmp1 = "select ity_certificate_ind from aeItemType where ity_id = ? and ity_run_ind = ? and ity_session_ind = ? ";
            PreparedStatement stmt1 = con.prepareStatement(sqlTmp1);
            stmt1.setString(1,itm_type);
            stmt1.setBoolean(2,itm_run_ind);
            stmt1.setBoolean(3,itm_session_ind);

            ResultSet rsTmp1 = stmt1.executeQuery();
            int ity_certificate_ind = -1;

            while (rsTmp1.next()) {
                ity_certificate_ind = rsTmp1.getInt("ity_certificate_ind");
            }
            stmt1.close();

            // get itm_title
            String itm_title = "";
            if (vColName.contains("itm_title")) {
                int indexOfId = vColName.indexOf("itm_title");
                itm_title = vColValue.elementAt(indexOfId).toString();
            }


            //<<add for bjmu
//          System.out.println("ity_certificate_ind = "+ity_certificate_ind);
//          System.out.println("itm_type="+itm_type);
//          System.out.println("itm_title="+itm_title);

            if (ity_certificate_ind == 1) {
                // insert certificate
                CFCertificate cf = new CFCertificate(con);
                cf.setTitle(itm_title);
                cf.setStatus("ON");
                cf.setCertificateLink("com.cw.wizbank.ae.aeItem");
                // get max(ctf_id)
                int ctf_max_id =(int)cf.insertCertificate(cf, (int)itm_owner_ent_id, itm_create_usr_id);
                if ((!(vColName.contains("itm_ctf_id")))&&(ctf_max_id > 0)) {
                    vColName.addElement("itm_ctf_id");
                    vColType.addElement(DbTable.COL_TYPE_INT);
                    vColValue.addElement(new Integer(ctf_max_id));
                }

            }
        }

        //>>add for bjmu--------------------------------------------------------

        //add itm_app_approval_type to Objects
        if(itm_app_approval_type != null) {
            vColName.addElement("itm_app_approval_type");
            vColType.addElement(DbTable.COL_TYPE_STRING);
            vColValue.addElement(itm_app_approval_type);
        }

        DbTable dbTab = new DbTable(con);
        PreparedStatement stmt = dbTab.ins4AutoId("aeItem", vColName, vColType, vColValue);
        this.itm_id = cwSQL.getAutoId(con, stmt, "aeItem", "itm_id");
        if (stmt != null) {
        	stmt.close();
        }
    }

    //ins into aeItem
    public void insItem(Connection con) throws SQLException ,cwSysMessage, cwException {
        //
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Insert into aeItem ");
        SQLBuf.append(" (itm_title, itm_type "); //2
        SQLBuf.append(" ,itm_appn_start_datetime, itm_appn_end_datetime "); //4
        SQLBuf.append(" ,itm_capacity, itm_unit "); //6
        SQLBuf.append(" ,itm_xml, itm_status ");
        SQLBuf.append(" ,itm_owner_ent_id, itm_create_timestamp ");
        SQLBuf.append(" ,itm_create_usr_id, itm_upd_timestamp ");
        SQLBuf.append(" , itm_upd_usr_id,itm_eff_start_datetime ");
        SQLBuf.append(" , itm_eff_end_datetime ,itm_code ");
        SQLBuf.append(" , itm_fee, itm_fee_ccy ");
        SQLBuf.append(" , itm_ext1 ,itm_run_ind ");
        SQLBuf.append(" , itm_session_ind, itm_apply_ind ");
        SQLBuf.append(" ,itm_deprecated_ind, itm_life_status, itm_apply_method ");
        SQLBuf.append(" ,itm_create_run_ind, itm_create_session_ind, itm_has_attendance_ind, itm_ji_ind, itm_completion_criteria_ind, itm_qdb_ind, itm_auto_enrol_qdb_ind, itm_version_code ");

        SQLBuf.append(" ,itm_min_capacity, itm_person_in_charge, itm_cancellation_reason, itm_retake_ind ");
        SQLBuf.append(" ,itm_approval_status ");
        SQLBuf.append(" , itm_approval_action, itm_approve_usr_id, itm_approve_timestamp ");
        SQLBuf.append(" , itm_submit_action, itm_submit_usr_id, itm_submit_timestamp ");
        SQLBuf.append(" ) Values ");
        //SQLBuf.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        // insert NULL / EMPTY_CLOB() to DB first
        SQLBuf.append(" (?, ?, ?, ?, ?, ?, ").append(cwSQL.getClobNull());
        SQLBuf.append(", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
        //add for bjmu
        SQLBuf.append(", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?) ");

        String SQL = new String(SQLBuf);

        if(itm_status == null)
            itm_status = aeItem.ITM_STATUS_OFF;
        PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
        int index = 1;
        stmt.setString(index++, itm_title);
        stmt.setString(index++, itm_type);
        stmt.setTimestamp(index++, itm_appn_start_datetime);
        stmt.setTimestamp(index++, itm_appn_end_datetime);
        stmt.setLong(index++, itm_capacity);
        //stmt.setString(index++, itm_ext2);
        stmt.setFloat(index++, itm_unit);
        stmt.setString(index++, itm_status);
        stmt.setLong(index++, itm_owner_ent_id);
        stmt.setTimestamp(index++, itm_create_timestamp);
        stmt.setString(index++, itm_create_usr_id);
        stmt.setTimestamp(index++, itm_upd_timestamp);
        stmt.setString(index++, itm_upd_usr_id);
        stmt.setTimestamp(index++, itm_eff_start_datetime);
        stmt.setTimestamp(index++, itm_eff_end_datetime);
        stmt.setString(index++, itm_code);
        stmt.setFloat(index++, itm_fee);
        stmt.setString(index++, itm_fee_ccy);
        stmt.setString(index++, itm_ext1);
        stmt.setBoolean(index++, itm_run_ind);
        stmt.setBoolean(index++, itm_session_ind);
        stmt.setBoolean(index++, itm_apply_ind);
        stmt.setBoolean(index++, false);
        stmt.setString(index++, itm_life_status);
        stmt.setString(index++, itm_apply_method);
        stmt.setBoolean(index++, itm_create_run_ind);
        stmt.setBoolean(index++, itm_create_session_ind);
        stmt.setBoolean(index++, itm_has_attendance_ind);
        stmt.setBoolean(index++, itm_ji_ind);
        stmt.setBoolean(index++, itm_completion_criteria_ind);
        stmt.setBoolean(index++, itm_qdb_ind);
        stmt.setBoolean(index++, itm_auto_enrol_qdb_ind);
        stmt.setString(index++, itm_version_code);
        stmt.setLong(index++, itm_min_capacity);
        stmt.setString(index++, itm_person_in_charge);
        stmt.setString(index++, itm_cancellation_reason);
        stmt.setBoolean(index++, itm_retake_ind);
        stmt.setString(index++, itm_approval_status);
        stmt.setString(index++, itm_approval_action);
        stmt.setString(index++, itm_approve_usr_id);
        stmt.setTimestamp(index++, itm_approve_timestamp);
        stmt.setString(index++, itm_submit_action);
        stmt.setString(index++, itm_submit_usr_id);
        stmt.setTimestamp(index++, itm_submit_timestamp);

        stmt.executeUpdate();

        itm_id = cwSQL.getAutoId(con, stmt, "aeItem", "itm_id");
        stmt.close();

/*
        // Update itm_xml
        stmt = con.prepareStatement(
                " SELECT itm_xml FROM aeItem  "
            + "    Where itm_id = ?  FOR UPDATE ",
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
        {
            cwSQL.setClobValue(con, rs, "itm_xml", itm_xml);
            rs.updateRow();
        }

        stmt.close();
*/
        //update itm_xml for Oracle
        String columnName[]={"itm_xml"};
        String columnValue[]={itm_xml};
        String condition = "itm_id= " + itm_id;
        cwSQL.updateClobFields(con, "aeItem",columnName,columnValue, condition);
    }

    //private

    public String shortInfoAsXML(Connection con) throws SQLException, cwSysMessage {
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append(" <item ");
        xmlBuf.append(" id=\"").append(aeUtils.escZero(itm_id)).append("\"");
        xmlBuf.append(" res_id=\"").append(aeUtils.escZero(getResId(con))).append("\"");
        xmlBuf.append(" type=\"").append(aeUtils.escNull(itm_type)).append("\"");
        xmlBuf.append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"");
        xmlBuf.append(" status=\"").append(aeUtils.escNull(itm_status)).append("\"");
        xmlBuf.append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"");
        xmlBuf.append(" timestamp=\"").append(itm_upd_timestamp).append("\">");
        xmlBuf.append(dbUtils.NEWL);
        if(this.itm_rsv_id > 0) {
            FMAuditTrail auditor = new FMAuditTrail(con);
            auditor.setRsvAuditTrail((int)this.itm_rsv_id);
            Timestamp rsv_upd_timestamp = auditor.getRsvUpdTimestamp();
            xmlBuf.append("<reservation id=\"").append(this.itm_rsv_id).append("\"")
                  .append(" timestamp=\"").append(rsv_upd_timestamp).append("\"/>");

        }
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append(getItemTypeTitle(con));
        xmlBuf.append(getNavAsXML(con, itm_id));
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append("</item>");
        xmlBuf.append(dbUtils.NEWL);
        String xml = new String(xmlBuf);
        return xml;
    }
    /** retired
    */
    public String TreeViewContentAsXML(Connection con, long tnd_id,
                                       String create_usr_id, Timestamp create_timestamp,
                                       String upd_usr_id, Timestamp upd_timestamp,
                                       String elementXML)
                          throws SQLException, cwSysMessage, qdbException {

        return TreeViewContentAsXML(con, tnd_id, create_usr_id, create_timestamp, upd_usr_id, upd_timestamp, elementXML, false);

    }

    public String TreeViewContentAsXML(Connection con, long tnd_id,
                                       String create_usr_id, Timestamp create_timestamp,
                                       String upd_usr_id, Timestamp upd_timestamp,
                                       String elementXML, boolean show_attendance_ind)
                          throws SQLException, cwSysMessage, qdbException {

        aeTimeField itm_eff_start=null;
        if(itm_eff_start_datetime!=null)
            itm_eff_start = new aeTimeField(itm_eff_start_datetime);

        boolean within_appn_period=false;
        if(itm_appn_start_datetime != null && itm_appn_end_datetime != null)
            within_appn_period = checkWithinAppnPeriod(con);

        StringBuffer xmlBuf = new StringBuffer(2500);

        if(create_usr_id == null)
            create_usr_id = itm_create_usr_id;
        if(create_timestamp == null)
            create_timestamp = itm_create_timestamp;
        if(upd_usr_id == null)
            upd_usr_id = itm_upd_usr_id;
        if(upd_timestamp == null)
            upd_timestamp = itm_upd_timestamp;

        Timestamp cur_time = dbUtils.getTime(con);
        xmlBuf.append(" <item ");
        if(tnd_id != 0)
            xmlBuf.append(" node_id=\"").append(aeUtils.escZero(tnd_id)).append("\"");
        xmlBuf.append(" item_id=\"").append(aeUtils.escZero(itm_id)).append("\"");
        xmlBuf.append(" item_type=\"").append(aeUtils.escNull(itm_type)).append("\"");
        xmlBuf.append(" dummy_type=\"").append(aeUtils.escNull(aeItemDummyType.getDummyItemType(itm_type, itm_blend_ind, itm_exam_ind, itm_ref_ind))).append("\"");
        xmlBuf.append(" item_code=\"").append(cwUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"");
        xmlBuf.append(" progress_status=\"").append(aeUtils.escNull(itm_progress_status)).append("\"");
        xmlBuf.append(" status=\"").append(aeUtils.escNull(itm_status)).append("\"");
        xmlBuf.append(" appn_start_datetime=\"").append(aeUtils.escNull(itm_appn_start_datetime)).append("\"");
        xmlBuf.append(" appn_end_datetime=\"").append(aeUtils.escNull(itm_appn_end_datetime)).append("\"");
        xmlBuf.append(" capacity=\"").append(aeUtils.escZero(itm_capacity)).append("\"");
        xmlBuf.append(" unit=\"").append(aeUtils.escZero(itm_unit)).append("\"");
        xmlBuf.append(" fee=\"").append(aeUtils.escZero(itm_fee)).append("\"");
        xmlBuf.append(" fee_ccy=\"").append(aeUtils.escNull(itm_fee_ccy)).append("\"");
        xmlBuf.append(" within_appn_period=\"").append(within_appn_period).append("\"");
        xmlBuf.append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"");
        xmlBuf.append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"");
        xmlBuf.append(" eff_start_days_to=\"");
        xmlBuf.append(dayDiff(cur_time, itm_eff_start_datetime)).append("\"");
        xmlBuf.append(" appn_start_days_to=\"");
        xmlBuf.append(dayDiff(cur_time, itm_appn_start_datetime)).append("\"");
        xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"");
        xmlBuf.append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"");
        if(itm_eff_end_datetime != null) {
            xmlBuf.append(" eff_end_pass_ind=\"").append(itm_eff_end_datetime.before(cur_time)).append("\"");
        }
        try {
            if(this.itm_id > 0) {
                xmlBuf.append(" enrol_cnt=\"").append(aeApplication.countItemAppn(con, itm_id, false)).append("\"");
            }
        }
        catch(cwException e) {
            throw new qdbException (e.getMessage());
        }
        xmlBuf.append(">");
        if(this.itm_id > 0 ) {
            //get application count by app_status
            xmlBuf.append(aeApplication.getAppnCountByItemAsXML(con, this.itm_id));
        }
        if (this.itm_has_attendance_ind && show_attendance_ind){
//            xmlBuf.append("<attendance_count_list><count status_id=\"1\">1</count><count status_id=\"2\">6</count><count status_id=\"3\">0</count><count status_id=\"4\">1</count></attendance_count_list>");
            xmlBuf.append(aeAttendance.getAttendanceCountByItemAsXML(con, this.itm_id, this.itm_owner_ent_id, null));
        }
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append("<title>").append(aeUtils.escNull(dbUtils.esc4XML(itm_title))).append("</title>");
        xmlBuf.append(dbUtils.NEWL);

        xmlBuf.append("<creator usr_id=\"").append(create_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(create_timestamp).append("\">");
        xmlBuf.append(dbUtils.esc4XML(aeCatalog.getUserName(con, create_usr_id)));
        xmlBuf.append("</creator>");
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append("<last_updated usr_id=\"").append(upd_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(upd_timestamp).append("\">");
        xmlBuf.append(dbUtils.esc4XML(aeCatalog.getUserName(con, upd_usr_id)));
        xmlBuf.append("</last_updated>");
        xmlBuf.append(dbUtils.NEWL);
        if(itm_eff_start != null)
            xmlBuf.append(itm_eff_start.asXML("eff_start_datetime"));
        if(elementXML != null) {
            xmlBuf.append(elementXML);
        }

        aeItemFigure aeIfg = new aeItemFigure();
        xmlBuf.append(aeIfg.getDetailAsXML(con, itm_id, null, null, null, null));
        aeItemRelation aeIre = new aeItemRelation();
        aeIre.ire_parent_itm_id = itm_id;
        xmlBuf.append("<run count=\"").append(aeIre.getChildCount(con)).append("\"/>").append(cwUtils.NEWL);

        xmlBuf.append("</item>");
        xmlBuf.append(dbUtils.NEWL);

        String xml = new String(xmlBuf);
        return xml;
    }

    public String ProgramDetailContentAsXML(Connection con, boolean core_ind, boolean pick_ind)
    throws SQLException ,cwSysMessage {

        aeTimeField itm_eff_start=null;
        if(itm_eff_start_datetime != null)
            itm_eff_start = new aeTimeField(itm_eff_start_datetime);

        boolean within_appn_period=false;
        if(itm_appn_start_datetime != null && itm_appn_end_datetime != null)
            within_appn_period = checkWithinAppnPeriod(con);

        StringBuffer xmlBuf = new StringBuffer(2500);

        xmlBuf.append(" <child ");
        xmlBuf.append(" item_id=\"").append(itm_id).append("\"");
        xmlBuf.append(" item_type=\"").append(itm_type).append("\"");
        xmlBuf.append(" item_code=\"").append(cwUtils.esc4XML(itm_code)).append("\"");
        xmlBuf.append(" status=\"").append(itm_status).append("\"");
        xmlBuf.append(" appn_start_datetime=\"").append(itm_appn_start_datetime).append("\"");
        xmlBuf.append(" appn_end_datetime=\"").append(itm_appn_end_datetime).append("\"");
        xmlBuf.append(" capacity=\"").append(itm_capacity).append("\"");
        xmlBuf.append(" unit=\"").append(itm_unit).append("\"");
        xmlBuf.append(" core_ind=\"").append(core_ind).append("\"");
        xmlBuf.append(" pick_ind=\"").append(pick_ind).append("\"");
        xmlBuf.append(" fee=\"").append(aeUtils.escZero(itm_fee)).append("\"");
        xmlBuf.append(" fee_ccy=\"").append(aeUtils.escNull(itm_fee_ccy)).append("\"");
        xmlBuf.append(" within_appn_period=\"").append(within_appn_period).append("\"");
        xmlBuf.append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"");
        xmlBuf.append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"");
//        xmlBuf.append(" cos_eff_start_datetime=\"").append(aeUtils.escNull(cos_eff_start_datetime)).append("\"");
//        xmlBuf.append(" cos_eff_end_datetime=\"").append(aeUtils.escNull(cos_eff_end_datetime)).append("\">");
        xmlBuf.append(" cos_eff_start_datetime=\"").append(aeUtils.escNull(itm_content_eff_start_datetime)).append("\"");
        xmlBuf.append(" cos_eff_end_datetime=\"").append(aeUtils.escNull(itm_content_eff_start_datetime)).append("\">");
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append("<title>").append(dbUtils.esc4XML(itm_title)).append("</title>");
        xmlBuf.append(dbUtils.NEWL);


        xmlBuf.append("<creator usr_id=\"").append(aeUtils.escNull(itm_create_usr_id)).append("\"");
        xmlBuf.append(" timestamp=\"").append(aeUtils.escNull(itm_create_timestamp)).append("\">");
        // comment for performance tuning! - kawai
        //if(itm_create_usr_id != null)
        //    xmlBuf.append(dbUtils.esc4XML(aeCatalog.getUserName(con, itm_create_usr_id)));
        xmlBuf.append("</creator>");
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append("<last_updated usr_id=\"").append(aeUtils.escNull(itm_upd_usr_id)).append("\"");
        xmlBuf.append(" timestamp=\"").append(aeUtils.escNull(itm_upd_timestamp)).append("\">");
        // comment for performance tuning! - kawai
        //if(itm_upd_usr_id != null)
        //    xmlBuf.append(dbUtils.esc4XML(aeCatalog.getUserName(con, itm_upd_usr_id)));
        xmlBuf.append("</last_updated>");
        xmlBuf.append(dbUtils.NEWL);

        if(itm_eff_start != null)
            xmlBuf.append(itm_eff_start.asXML("eff_start_datetime"));
        xmlBuf.append("</child>");
        xmlBuf.append(dbUtils.NEWL);

        String xml = new String(xmlBuf);
        return xml;
    }

    //get the parent treenode nav xml of this item before it is added into database
    //so need to add a dummy node in the nav xml to show this item's nav XML
    private String getTreeNodeNavAsXMLForIns(Connection con, aeTreeNode tnd)
        throws SQLException  ,cwSysMessage{

        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<nav>");
        xmlBuf.append(dbUtils.NEWL);

        if(tnd.isNodeExist(con)) {
            aeTreeNode[] navTreeNode = tnd.getNavigatorTreeNode(con);
            for(int i=0; i<navTreeNode.length; i++) {
                xmlBuf.append("<node node_id=\"").append(navTreeNode[i].tnd_id).append("\">");
                xmlBuf.append(dbUtils.NEWL);
                xmlBuf.append("<title>").append(dbUtils.esc4XML(navTreeNode[i].tnd_title)).append("</title>");
                xmlBuf.append(dbUtils.NEWL);
                xmlBuf.append("</node>");
                xmlBuf.append(dbUtils.NEWL);
            }
        }
        //add a dummy node
        xmlBuf.append("<node>").append("</node>");
        xmlBuf.append("</nav>");
        String xml = new String(xmlBuf);
        return xml;
    }

  
    public String prep_ins_form(Connection con, long tnd_id, long usr_ent_id,
                                String rol_ext_id, long owner_ent_id, long itm_tpl_id,
                                String tvw_id, qdbEnv inEnv, long wrk_tpl_id, long my_top_tc_id, long itm_tcr_id)
      throws SQLException, cwSysMessage, cwException, qdbException {
//System.out.println("wrk tpl id = " + wrk_tpl_id);

        // check for create item restriction (2002.03.26 kawai)
        if (isActiveItemCntExceedLimit(con, owner_ent_id)) {
            throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
        }

        StringBuffer xmlBuf = new StringBuffer(2500);
        this.itm_owner_ent_id = owner_ent_id;
        aeTreeNode parentNode = new aeTreeNode();
        parentNode.tnd_id = tnd_id;
        StringBuffer catalogXML = new StringBuffer(1024);
        if(parentNode.tnd_id != 0) {
            parentNode.get(con);
            catalogXML.append("<catalog>");
            catalogXML.append("<node_list>");
            catalogXML.append("<node>");
            catalogXML.append(getTreeNodeNavAsXMLForIns(con, parentNode));
            catalogXML.append("</node>");
            catalogXML.append("</node_list>");
            catalogXML.append("</catalog>");
        }
        if (tnd_id != 0) {
            aeTreeNode myTreeNode = new aeTreeNode();
            myTreeNode.tnd_parent_tnd_id = parentNode.tnd_id;
            xmlBuf.append(myTreeNode.contentHeaderAsXML());
            xmlBuf.append(parentNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
            xmlBuf.append("</node>");
        }
        //run item indicator will be stored in item template
        xmlBuf.append("<item");

        if(itm_tcr_id > 0) {
        	xmlBuf.append(" tcr_id=\"").append(itm_tcr_id).append("\"");
        }
        if((itm_run_ind || itm_session_ind )&& itm_id != 0) {
        	aeItem pitm = new aeItem();
        	pitm.itm_id = itm_id;
        	pitm.getItemSimpleInfo(con, itm_id);
            xmlBuf.append(" parent_itm_id=\"").append(itm_id).append("\"");
            xmlBuf.append(" parent_title=\"").append(cwUtils.esc4XML(pitm.itm_title)).append("\"");
            itm_type = pitm.itm_type;
            itm_blend_ind = pitm.itm_blend_ind;
            itm_exam_ind = pitm.itm_exam_ind;
            itm_ref_ind = pitm.itm_ref_ind;
        } else {
        	xmlBuf.append(" class=\"false\"");
        }
        xmlBuf.append(">").append(dbUtils.NEWL);
        if (itm_id!=0){
            xmlBuf.append(getNavAsXML(con, itm_id));
        }
        xmlBuf.append(getItemTypeTitle(con));
        //get item template
        aeTemplate tpl = new aeTemplate();
        tpl.tpl_id = itm_tpl_id;
        tpl.get(con);
        //get view template
        DbTemplateView dbTplVi = new DbTemplateView();
        dbTplVi.tvw_tpl_id = itm_tpl_id;
        dbTplVi.tvw_id = tvw_id;
        dbTplVi.get(con);
        StringBuffer tplBuf = new StringBuffer(2048);
        tplBuf.append("<applyeasy>").append(dbUtils.NEWL);
        StringBuffer tmp_xml = new StringBuffer();
        if(dbTplVi.tvw_xml.indexOf("</center>") == -1) {
        	tmp_xml.append(dbTplVi.tvw_xml.substring(0, dbTplVi.tvw_xml.indexOf("</training_center>")))
        	.append("<center id=\"").append(itm_tcr_id).append("\">");
        	
        	String tcr_title = getTcrTitleById(con, itm_tcr_id);
        	tcr_title = tcr_title == null ? tcr_title : com.cwn.wizbank.utils.StringUtils.replaceUtils(tcr_title);
        	
        	tmp_xml.append(tcr_title).append("</center>")
        	.append(dbTplVi.tvw_xml.substring(dbTplVi.tvw_xml.indexOf("</training_center>")));
        	dbTplVi.tvw_xml = tmp_xml.toString();
        }
        tplBuf.append(aeUtils.escNull(dbTplVi.tvw_xml)).append(dbUtils.NEWL);
        tplBuf.append(tpl.tpl_xml).append(dbUtils.NEWL);
        //store the parent item id
        long pItemId = this.itm_id;
        this.itm_id = 0;
        /*
        if(dbTplVi.tvw_itm_acc_ind) {
            // generate access control rules xml
            if(this.itm_run_ind || this.itm_session_ind) {
                tplBuf.append(getAccessAsXML(con, pItemId));
            }
            else {
                //generate the item access tag for user who wants to create this item
                tplBuf.append(getCreateItemAccess(con, usr_ent_id, rol_ext_id, owner_ent_id));
            }
        }
        */
        if(dbTplVi.tvw_itm_acc_ind) {
            //generate the item access tag for user who wants to create this item
            tplBuf.append(getCreateItemAccess(con, usr_ent_id, rol_ext_id, owner_ent_id));
        }

        if(dbTplVi.tvw_tcr_ind) {
        	//constuct the prof
        	loginProfile prof = new loginProfile();
        	prof.usr_ent_id = usr_ent_id;
        	prof.root_ent_id = owner_ent_id;
        	prof.current_role = rol_ext_id;
        	prof.my_top_tc_id  = my_top_tc_id;
        	//get the default training center for the login user
        	tplBuf.append(getCourseTrainingCenterXML(con,pItemId));
            //tplBuf.append(getDefaultTrainingCenterXML(con, prof));
        }
        if(this.itm_run_ind || this.itm_session_ind) {
            //get parent itm_xml
            tplBuf.append("<parent_item>");
            aeItem pItm = new aeItem();
            pItm.itm_id = pItemId;
            pItm.getItemXML(con);
            tplBuf.append(pItm.itm_xml);
            tplBuf.append("</parent_item>");
            //get parent's targeted learner info
            if(dbTplVi.tvw_target_ind) {
                pItm.itm_create_run_ind = true;

            }
            //get parent's itm_capacity, itm_min_capacity
            tplBuf.append(pItm.getCascadeUpdInfoAsXML(con));
            //set initial run status to IN_PROGRESS
            tplBuf.append("<item_life_status>");
            tplBuf.append("<status value=\"IN_PROGRESS\"/>");
            tplBuf.append("</item_life_status>");
        }
        tplBuf.append(getAllItemTypeTitleInOrg(con, owner_ent_id));
        tplBuf.append(catalogXML);
        if( wrk_tpl_id != 0 ) {
            tplBuf.append("<workflow_template>")
                  .append("<id value=\"").append(wrk_tpl_id).append("\"/>")
                  .append("</workflow_template>");
        }
        if(dbTplVi.tvw_ctb_ind) {
            //generate code table xml
            try {
                tplBuf.append(getCodeTableAsXML(con, tpl));
            } catch(IOException ioe) {
                throw new cwException ("getValuedTemplate: IOException at getCodeTableAsXML: " + ioe.getMessage());
            }
        }

        if( (itm_run_ind || itm_session_ind) && pItemId != 0 )
            tplBuf.append(aeItemFigure.getItemRunFigureXML(con, pItemId));
        else
            tplBuf.append(aeItemFigure.getItemFigureXML(con, 0, owner_ent_id));

        tplBuf.append("</applyeasy>");
        //System.out.print(tplBuf.toString());
        if( wrk_tpl_id != 0 ) {
            aeTemplate aetpl = new aeTemplate();
            aetpl.tpl_id = wrk_tpl_id;
            aetpl.get(con);

            xmlBuf.append("<workflow_template id=\"")
                    .append(wrk_tpl_id)
                    .append("\" approval_ind=\"")
                    .append(this.itm_app_approval_type!=null)
                    .append("\" />");
        }

        xmlBuf.append(aeUtils.transformXML(tplBuf.toString(), inEnv.INI_XSL_VALTPL, inEnv, null)).append(dbUtils.NEWL);
        xmlBuf.append("<auto_enroll_interval>" + auto_enroll_interval + "</auto_enroll_interval>");
        xmlBuf.append("<sys_email>").append(getSysEmailAddress(con, owner_ent_id)).append("</sys_email>");
        xmlBuf.append("</item>").append(dbUtils.NEWL);

        //if(itm_type.equalsIgnoreCase(ITM_TYPE_SELFSTUDY)){
            Certificate cert =  new Certificate();
            xmlBuf.append(cert.getAllCertXml(con, 0)).append(dbUtils.NEWL);
       // }
        
        String xml = new String(xmlBuf);
        return xml;
    }

    public String getTcrTitleById(Connection con, long tcr_id) {
    	PreparedStatement stmt = null;
    	String tcr_title = null;
    	try {
    		String sql = "select tcr_title from tcTrainingCenter where tcr_id = ?";
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, tcr_id);
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next()) {
    			tcr_title = rs.getString("tcr_title");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return tcr_title;
    }

    public String prep_ins_simple_form(Connection con, long tnd_id, long usr_ent_id,
                                String rol_ext_id, long owner_ent_id, long itm_tpl_id,
                                String tvw_id, qdbEnv inEnv, long wrk_tpl_id)
      throws SQLException, cwSysMessage, cwException, qdbException {
  //System.out.println("wrk tpl id = " + wrk_tpl_id);

          // check for create item restriction (2002.03.26 kawai)
          if (isActiveItemCntExceedLimit(con, owner_ent_id)) {
              throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
          }

          StringBuffer xmlBuf = new StringBuffer(2500);
          this.itm_owner_ent_id = owner_ent_id;
          aeTreeNode parentNode = new aeTreeNode();
          parentNode.tnd_id = tnd_id;
          StringBuffer catalogXML = new StringBuffer(1024);
          if(parentNode.tnd_id != 0) {
              parentNode.get(con);
              catalogXML.append("<catalog>");
              catalogXML.append("<node_list>");
              catalogXML.append("<node>");
              catalogXML.append(getTreeNodeNavAsXMLForIns(con, parentNode));
              catalogXML.append("</node>");
              catalogXML.append("</node_list>");
              catalogXML.append("</catalog>");
          }
          if (tnd_id != 0) {
              aeTreeNode myTreeNode = new aeTreeNode();
              myTreeNode.tnd_parent_tnd_id = parentNode.tnd_id;
              xmlBuf.append(myTreeNode.contentHeaderAsXML());
              xmlBuf.append(parentNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
              xmlBuf.append("</node>");
          }
          //run item indicator will be stored in item template
          xmlBuf.append("<item");

          if((itm_run_ind || itm_session_ind )&& itm_id != 0) {
          	aeItem pitm = new aeItem();
          	pitm.itm_id = itm_id;
          	pitm.getItemSimpleInfo(con, itm_id);
              xmlBuf.append(" parent_itm_id=\"").append(itm_id).append("\"");
              xmlBuf.append(" parent_title=\"").append(cwUtils.esc4XML(pitm.itm_title)).append("\"");
              xmlBuf.append(" content_def=\"").append(cwUtils.esc4XML(pitm.itm_content_def)).append("\"");
              itm_type = pitm.itm_type;
              itm_blend_ind = pitm.itm_blend_ind;
              itm_exam_ind = pitm.itm_exam_ind;
              itm_ref_ind = pitm.itm_ref_ind;
          }
          xmlBuf.append(">").append(dbUtils.NEWL);
          if (itm_id!=0){
              xmlBuf.append(getNavAsXML(con, itm_id));
          }
          xmlBuf.append(getItemTypeTitle(con));
          if(itm_type.equalsIgnoreCase(ITM_TYPE_INTEGRATED) || itm_type.equalsIgnoreCase(ITM_TYPE_AUDIOVIDEO)){
        	  xmlBuf.append("<item_type id=\"" + cwUtils.escNull(itm_type) +"\"" + " dummy_type=\"" + itm_type + "\"/>");
          }
          if( wrk_tpl_id != 0 ) {
              aeTemplate aetpl = new aeTemplate();
              aetpl.tpl_id = wrk_tpl_id;
              aetpl.get(con);

              xmlBuf.append("<workflow_template id=\"")
                      .append(wrk_tpl_id)
                      .append("\" approval_ind=\"")
                      .append(this.itm_app_approval_type!=null)
                      .append("\" />");
          }

          xmlBuf.append("<auto_enroll_interval>" + auto_enroll_interval + "</auto_enroll_interval>");
          xmlBuf.append("<sys_email>").append(getSysEmailAddress(con, owner_ent_id)).append("</sys_email>");
          xmlBuf.append("</item>").append(dbUtils.NEWL);

          //if(itm_type.equalsIgnoreCase(ITM_TYPE_SELFSTUDY)){
              Certificate cert =  new Certificate();
              xmlBuf.append(cert.getAllCertXml(con, 0)).append(dbUtils.NEWL);
         // }
          
          String xml = new String(xmlBuf);
          return xml;
    }
    /**
    Get XML of item access when wants to create an item
    */
    private static String getCreateItemAccess(Connection con, long usr_ent_id, String rol_ext_id, long owner_ent_id) throws SQLException, cwException {
        StringBuffer tplBuf = new StringBuffer(1024);
        tplBuf.append("<item_access>");
        tplBuf.append(dbUtils.getAllRoleAsXML(con,"role_list", owner_ent_id));
        tplBuf.append(aeItemAccess.getCreateItemAccess(con, usr_ent_id, rol_ext_id));
        tplBuf.append("</item_access>");
        return tplBuf.toString();
    }

    /**
    get the itm_xml of this item into this.itm_xml
    */
    private static final String sql_get_itm_xml =
        " Select itm_xml From aeItem Where itm_id = ? ";
    public void getItemXML(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(sql_get_itm_xml);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            //System.out.println("rs NOT NULL");
            this.itm_xml = cwSQL.getClobValue(rs, "itm_xml");
        }
        else {
            //System.out.println("rs NULL");
            this.itm_xml = null;
        }
        stmt.close();
        return;
    }

    public static String prep_ins(Connection con, long tnd_id, long owner_ent_id)
                                  /*,long itm_tpl_id, long wrk_tpl_id, long app_tpl_id)*/
      throws SQLException, qdbException, cwSysMessage {

        aeTreeNode parentNode = new aeTreeNode();
        parentNode.tnd_id = tnd_id;
        String itemTypeXML = "";
        if(parentNode.tnd_id != 0) {
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            parentNode.tnd_cat_id = parentNode.getCatalogId(con);
            itemTypeXML = getAllItemTypeTitleInOrg(con, owner_ent_id) +
                                    DbCatalogItemType.getCatalogItemTypeAsXML(con, parentNode.tnd_cat_id);
            /*
            if(!parentNode.hasUpdPrivilege(con, owner_ent_id))
                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
            */
        }

        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<item parent_tnd_id=\"").append(aeUtils.escZero(parentNode.tnd_id)).append("\">").append(dbUtils.NEWL);
        xmlBuf.append(itemTypeXML);
        xmlBuf.append(parentNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);
        xmlBuf.append("</item>");
        String xml = new String(xmlBuf);
        return xml;
    }


    public static long[] getQueue(Connection con) throws SQLException {
        // sort by code
        long[] queue;
        Vector v = new Vector();
        Long id;
        String SQL= " Select itm_id From aeItem Order By itm_code asc ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            id = new Long(rs.getLong("itm_id"));
            v.addElement(id);
        }

        stmt.close();
        queue = Vector2long(v);
        return queue;
    }

    public static long[] getQueue(Connection con, long root_ent_id, String sort_by, String order_by, boolean showHistory) throws SQLException, qdbException {
        long[] queue;
        Vector v = new Vector();
        Long id;
        StringBuffer SQL= new StringBuffer();
        StringBuffer orderSQL = new StringBuffer();

        SQL.append(" Select itm_id From aeItem WHERE itm_owner_ent_id = ? AND itm_eff_end_datetime ");

        if (showHistory) {
            SQL.append(" < ? ");
        } else {
            SQL.append(" >= ? ");
        }

        SQL.append(" Order By ");

        for (int i=0;i<ORDER.length; i++) {
            if (ORDER[i].equalsIgnoreCase(sort_by)) {
                SQL.append(ORDER_DB[i]);

                if (order_by.equalsIgnoreCase("DESC")) {
                    SQL.append(" DESC, ");
                } else {
                    SQL.append(" ASC, ");
                }
            } else {
                orderSQL.append(ORDER_DB[i]);

                if (order_by.equalsIgnoreCase("DESC")) {
                    orderSQL.append(" DESC, ");
                } else {
                    orderSQL.append(" ASC, ");
                }
            }
        }

        SQL.append(orderSQL.toString());
        SQL.append("itm_unit");

        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(1, root_ent_id);
        stmt.setTimestamp(2, dbUtils.getTime(con));
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            id = new Long(rs.getLong("itm_id"));
            v.addElement(id);
        }

        stmt.close();
        queue = Vector2long(v);
        return queue;
    }

    public static long[] Vector2long(Vector v) {
        if (v == null) {
            return null;
        }

        int size = v.size();
        long[] l = new long[size];
        for(int i=0;i<size;i++)
            l[i] = ((Long)v.elementAt(i)).longValue();

        return l;
    }

    public static String getOwnerItems(Connection con, long owner_ent_id, boolean checkStatus
            ,int orphan_idx, String orderBy, String sortOrder, boolean tc_enabled, loginProfile prof) throws SQLException, cwSysMessage, qdbException {
    	return getOwnerItems(con, owner_ent_id, checkStatus, orphan_idx, orderBy, sortOrder, tc_enabled, prof, 0);
    }
    
    //get items for the organization(owner)
    //this method will skip aeCatalogAccess
    //if orphan_idx is ITM_ORPHAN, only get the orphan items
    //if orphan_idx is ITM_NON_ORPHAN, only get the non orphan items
    //if orphan_idx is ITM_ALL, get all the items
    public static String getOwnerItems(Connection con, long owner_ent_id, boolean checkStatus
                                      ,int orphan_idx, String orderBy, String sortOrder, boolean tc_enabled, loginProfile prof, long tcr_id)
        throws SQLException, cwSysMessage, qdbException{

        aeItem itm;
        StringBuffer SQLBuf = new StringBuffer(300);
        StringBuffer xmlBuf = new StringBuffer(2500);

        SQLBuf.append(" Select * From aeItem ");
        SQLBuf.append(" Where itm_owner_ent_id = ? ");
        SQLBuf.append(" And itm_deprecated_ind = ? ");
        SQLBuf.append(" And itm_run_ind = ? ");
        SQLBuf.append(" And itm_session_ind = ? ");
        if(checkStatus)
            SQLBuf.append( " And itm_status <> '").append(aeItem.ITM_STATUS_OFF).append("' ");
        switch(orphan_idx) {
            case ITM_ORPHAN:
                SQLBuf.append(" And Not Exists (Select tnd_id From aeTreeNode Where tnd_itm_id = itm_id) ");
                break;
            case ITM_NON_ORPHAN:
                SQLBuf.append(" And Exists (Select tnd_id From aeTreeNode Where tnd_itm_id = itm_id) ");
                break;
            case ITM_ALL:
                // don't need to check orphan or not
        }
        if (tcr_id == -1) {
	    	if (tc_enabled && !prof.isLrnRole) {
	    		tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
	    	} else if (tc_enabled && prof.isLrnRole) {
	    		tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
	    	}
        }
        if (tcr_id > 0) {
        	SQLBuf.append(" and itm_tcr_id = ? ");
        }
        SQLBuf.append(" Order by ");
        if(sortOrder == null || sortOrder.length() == 0)
            sortOrder = "asc";
        if(orderBy == null || orderBy.length() == 0)
            orderBy = "itm_code";
        SQLBuf.append(orderBy).append(" ").append(sortOrder);
        //SQLBuf.append(" itm_code asc, itm_title asc ");

        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, owner_ent_id);
        stmt.setBoolean(2, false);
        stmt.setBoolean(3, false);
        stmt.setBoolean(4, false);
        if (tcr_id > 0) {
        	stmt.setLong(5, tcr_id);
        }
        ResultSet rs = stmt.executeQuery();

        xmlBuf.append("<items orderby=\"").append(orderBy).append("\"");
        xmlBuf.append(" sortorder=\"").append(sortOrder).append("\">").append(dbUtils.NEWL);
        xmlBuf.append(getAllItemTypeTitleInOrg(con, owner_ent_id));
        while(rs.next()) {
            itm = new aeItem();
            itm.itm_id = rs.getLong("itm_id");
            itm.itm_title = rs.getString("itm_title");
            itm.itm_type = rs.getString("itm_type");
            itm.itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
            itm.itm_appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
            itm.itm_capacity = rs.getLong("itm_capacity");
            //itm.itm_ext2 = rs.getString("itm_ext2");
            itm.itm_unit = rs.getFloat("itm_unit");
            //itm.itm_xml = rs.getString("itm_xml");
            itm.itm_xml = cwSQL.getClobValue(rs, "itm_xml");

            itm.itm_status = rs.getString("itm_status");
            itm.itm_owner_ent_id = rs.getLong("itm_owner_ent_id");
            itm.itm_create_timestamp = rs.getTimestamp("itm_create_timestamp");
            itm.itm_create_usr_id = rs.getString("itm_create_usr_id");
            itm.itm_upd_timestamp = rs.getTimestamp("itm_upd_timestamp");
            itm.itm_upd_usr_id = rs.getString("itm_upd_usr_id");
            itm.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            itm.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            itm.itm_code = rs.getString("itm_code");
            itm.itm_blend_ind = rs.getBoolean("itm_blend_ind");
            itm.itm_exam_ind = rs.getBoolean("itm_exam_ind");
            itm.itm_ref_ind = rs.getBoolean("itm_ref_ind");
            xmlBuf.append(itm.TreeViewContentAsXML(con, 0, null, null, null, null, null, false));
        }

        stmt.close();
        xmlBuf.append("</items>");
        String xml = new String(xmlBuf);

        return xml;
    }


    public boolean checkWithinAppnPeriod(Connection con) throws SQLException {
        try{
            boolean result;
            //getDate
            Timestamp curTime = dbUtils.getTime(con);
            result = (itm_appn_end_datetime == null || curTime.before(itm_appn_end_datetime))
                    && (itm_appn_start_datetime == null || curTime.after(itm_appn_start_datetime));

            return result;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    /*
    public static boolean isItemOff(Connection con, long itm_id) throws SQLException, cwSysMessage {
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        return itm.isItemOff(con);
    }
    */
    public boolean isItemOff(Connection con) throws SQLException ,cwSysMessage {
        boolean result;
        String status;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select itm_status From aeItem ");
        SQLBuf.append(" Where itm_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            status = rs.getString("itm_status");
        else
            //throw new SQLException("Cannot find Item. itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);

        result = status.equals(aeItem.ITM_STATUS_OFF);

        stmt.close();
        return result;
    }

    private boolean isItemExist(Connection con) throws SQLException {
        boolean result;
        long count=0;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select count(*) From aeItem ");
        SQLBuf.append(" Where itm_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            count = rs.getLong(1);

        result = count > 0;

        stmt.close();
        return result;
    }

    public void get(Connection con, long tnd_id)
      throws SQLException, cwSysMessage {

        if(tnd_id != 0) {
            myTreeNode = new aeTreeNode();
            myTreeNode.tnd_id = tnd_id;
            myTreeNode.get(con);
        }
        getItem(con);
    }

    public void get(Connection con)
      throws SQLException, cwSysMessage {

        getItem(con);
    }

    public boolean isOrphan(Connection con) throws SQLException {
        final String SQL = " Select count(*) from aeTreeNode "
                         + " Where tnd_itm_id = ? ";

        long count = 0;
        boolean result;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            count = rs.getLong(1);
        }
        stmt.close();

        result = count == 0;
        return result;
    }

    /**
     * alternative api for getItem(), using the object's private connection
     * 2001.08.06 wai
     */
    public void getItem() throws SQLException, cwSysMessage {
        getItem(this.con);
    }
    public void getItem(Connection con) throws SQLException ,cwSysMessage {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select itm_title, itm_type ");
        SQLBuf.append(" ,itm_appn_start_datetime, itm_appn_end_datetime ");
        SQLBuf.append(" ,itm_capacity, itm_unit ");
        SQLBuf.append(" ,itm_xml, itm_status ");
        SQLBuf.append(" ,itm_owner_ent_id ");
        SQLBuf.append(" ,itm_create_timestamp, itm_create_usr_id ");
        SQLBuf.append(" ,itm_upd_timestamp, itm_upd_usr_id ");
        SQLBuf.append(" ,itm_eff_start_datetime, itm_eff_end_datetime ");
        SQLBuf.append(" ,itm_code, itm_fee, itm_fee_ccy, itm_ext1 ");
        SQLBuf.append(" ,itm_run_ind, itm_session_ind, itm_apply_ind ");
        SQLBuf.append(" ,itm_deprecated_ind, itm_life_status, itm_apply_method ");
        SQLBuf.append(" ,itm_create_run_ind, itm_create_session_ind, itm_has_attendance_ind, itm_ji_ind, itm_completion_criteria_ind, itm_qdb_ind, itm_imd_id , itm_auto_enrol_qdb_ind, itm_version_code ");
        SQLBuf.append(" ,itm_min_capacity, itm_person_in_charge, itm_cancellation_reason, itm_cancellation_type ");
        SQLBuf.append(" ,itm_rsv_id, itm_tcr_id, itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration, itm_can_cancel_ind, itm_can_qr_ind, itm_retake_ind, itm_app_approval_type ");
        SQLBuf.append(" ,itm_approval_status");
        SQLBuf.append(" ,itm_approval_action, itm_approve_usr_id, itm_approve_timestamp");
        SQLBuf.append(" ,itm_submit_action, itm_submit_usr_id, itm_submit_timestamp ");
        SQLBuf.append(" ,itm_app_approval_type ");
        SQLBuf.append(" ,itm_content_def ");
        SQLBuf.append(" ,itm_enroll_type");
        SQLBuf.append(" ,itm_mark_buffer_day");
        SQLBuf.append(" ,itm_notify_days");
        SQLBuf.append(" ,itm_notify_email");
        SQLBuf.append(" ,itm_not_allow_waitlist_ind");
        SQLBuf.append(" ,itm_access_type");
        SQLBuf.append(" ,itm_target_enrol_type");
        SQLBuf.append(" ,itm_blend_ind");
        SQLBuf.append(" ,itm_exam_ind");
        SQLBuf.append(" ,itm_ref_ind");
        SQLBuf.append(" ,itm_icon");
        SQLBuf.append(" ,itm_plan_code");
        SQLBuf.append(" ,itm_bonus_ind");
        SQLBuf.append(" ,itm_diff_factor");
        SQLBuf.append(" ,itm_integrated_ind");
        SQLBuf.append(" ,itm_share_ind");
        SQLBuf.append(" ,itm_offline_pkg");
        SQLBuf.append(" ,itm_offline_pkg_file");
        SQLBuf.append(" ,itm_cfc_id");
        SQLBuf.append(" ,itm_inst_type");
        SQLBuf.append(" ,itm_desc");
        SQLBuf.append(" ,itm_send_enroll_email_ind");
        SQLBuf.append(" ,itm_reminder_send_datetime");
        SQLBuf.append(" ,itm_ji_send_datetime");
        SQLBuf.append(" ,itm_mobile_ind");
        SQLBuf.append(" From aeItem ");
        SQLBuf.append(" Where itm_id = ? ");

        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);  //set the itm_id = this.itm_id
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            itm_title = rs.getString("itm_title");
            itm_type = rs.getString("itm_type");
            itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
            itm_appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
            itm_capacity = rs.getLong("itm_capacity");
            //itm_ext2 = rs.getString("itm_ext2");
            itm_unit = rs.getFloat("itm_unit");

            //itm_xml = rs.getString("itm_xml");
            itm_xml = cwSQL.getClobValue(rs, "itm_xml");
            itm_desc = rs.getString("itm_desc");

            itm_status = rs.getString("itm_status");
            itm_owner_ent_id = rs.getLong("itm_owner_ent_id");
            itm_create_timestamp = rs.getTimestamp("itm_create_timestamp");
            itm_create_usr_id = rs.getString("itm_create_usr_id");
            itm_upd_timestamp = rs.getTimestamp("itm_upd_timestamp");
            itm_upd_usr_id = rs.getString("itm_upd_usr_id");
            itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
            itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
            itm_code = rs.getString("itm_code");
            itm_fee = rs.getFloat("itm_fee");
            itm_fee_ccy = rs.getString("itm_fee_ccy");
            itm_ext1 = rs.getString("itm_ext1");
            itm_run_ind = rs.getBoolean("itm_run_ind");
            itm_session_ind = rs.getBoolean("itm_session_ind");
            itm_apply_ind = rs.getBoolean("itm_apply_ind");
            itm_deprecated_ind = rs.getBoolean("itm_deprecated_ind");
            itm_life_status = rs.getString("itm_life_status");
            itm_apply_method = rs.getString("itm_apply_method");
            itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
            itm_create_session_ind = rs.getBoolean("itm_create_session_ind");
            itm_has_attendance_ind = rs.getBoolean("itm_has_attendance_ind");
            itm_ji_ind = rs.getBoolean("itm_ji_ind");
            itm_completion_criteria_ind = rs.getBoolean("itm_completion_criteria_ind");
            itm_qdb_ind = rs.getBoolean("itm_qdb_ind");
            itm_imd_id = rs.getLong("itm_imd_id");
            itm_auto_enrol_qdb_ind = rs.getBoolean("itm_auto_enrol_qdb_ind");
            itm_version_code = rs.getString("itm_version_code");
            itm_min_capacity = rs.getLong("itm_min_capacity");
            itm_person_in_charge = rs.getString("itm_person_in_charge");
            itm_cancellation_reason = rs.getString("itm_cancellation_reason");
            itm_cancellation_type = rs.getString("itm_cancellation_type");
            itm_rsv_id = rs.getLong("itm_rsv_id");
            itm_tcr_id = rs.getLong("itm_tcr_id");
            itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
            itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
            itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
            itm_can_cancel_ind = rs.getBoolean("itm_can_cancel_ind");
            itm_can_qr_ind = rs.getBoolean("itm_can_qr_ind");
            itm_retake_ind = rs.getBoolean("itm_retake_ind");
            itm_app_approval_type = rs.getString("itm_app_approval_type");
            itm_approval_status = rs.getString("itm_approval_status");
            itm_approval_action = rs.getString("itm_approval_action");
            itm_approve_usr_id = rs.getString("itm_approve_usr_id");
            itm_approve_timestamp = rs.getTimestamp("itm_approve_timestamp");
            itm_submit_action = rs.getString("itm_submit_action");
            itm_submit_usr_id = rs.getString("itm_submit_usr_id");
            itm_submit_timestamp = rs.getTimestamp("itm_submit_timestamp");
            //itm_app_approval_type = rs.getString("itm_app_approval_type");
			itm_content_def = rs.getString("itm_content_def");
			itm_enroll_type = rs.getString("itm_enroll_type");
			itm_target_enrol_type = rs.getString("itm_target_enrol_type");
            itm_mark_buffer_day = rs.getInt("itm_mark_buffer_day");
            itm_notify_days = rs.getInt("itm_notify_days");
            itm_notify_email = rs.getString("itm_notify_email");
            itm_not_allow_waitlist_ind = rs.getBoolean("itm_not_allow_waitlist_ind");
            itm_access_type = rs.getString("itm_access_type");
            itm_target_enrol_type = rs.getString("itm_target_enrol_type");
            itm_blend_ind = rs.getBoolean("itm_blend_ind");
            itm_exam_ind = rs.getBoolean("itm_exam_ind");
            itm_ref_ind = rs.getBoolean("itm_ref_ind");
            itm_dummy_type = aeItemDummyType.getDummyItemType(itm_type, itm_blend_ind, itm_exam_ind, itm_ref_ind);
            itm_icon = rs.getString("itm_icon");
            itm_plan_code = rs.getString("itm_plan_code");
            itm_bonus_ind = rs.getBoolean("itm_bonus_ind");
            itm_diff_factor = rs.getInt("itm_diff_factor");
            itm_integrated_ind = rs.getBoolean("itm_integrated_ind");
            itm_share_ind = rs.getBoolean("itm_share_ind");
            itm_offline_pkg = rs.getString("itm_offline_pkg");
        	itm_offline_pkg_file = rs.getString("itm_offline_pkg_file");
        	itm_cfc_id = rs.getLong("itm_cfc_id");
        	itm_inst_type = rs.getString("itm_inst_type");
        	itm_send_enroll_email_ind = rs.getLong("itm_send_enroll_email_ind");
            itm_reminder_send_datetime = rs.getTimestamp("itm_reminder_send_datetime");
            itm_ji_send_datetime = rs.getTimestamp("itm_ji_send_datetime");
            itm_mobile_ind = rs.getString("itm_mobile_ind");
        }
        else{
        	if(rs!=null)rs.close();
            stmt.close();
            //throw new SQLException("Cannot find Item. itm_id = " + itm_id);
            throw new cwSysMessage ("AEIT27");
        }

        // target learner type
        this.targetType = (this.itm_run_ind) ? DbItemTargetRuleDetail.IRD_TYPE_TARGET_ENROLLMENT
                                             : DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER;
        rs.close();
        stmt.close();
    }
    
    public String getItemCostAttribute(WizbiniLoader wizbini,String root_id)throws cwException{
		StringWriter writer = new StringWriter(); 
		wizbini.marshal(wizbini.cfgOrgItemCostMgt.get(root_id), writer);
		String attri_xml = writer.toString();
		attri_xml = attri_xml.substring(attri_xml.indexOf("<", 2));
    	return attri_xml;
    }
    
    public List getItemCostAttriList(WizbiniLoader wizbini,String root_id)throws cwException{
    	List list = ((ItemCostManagement)wizbini.cfgOrgItemCostMgt.get(root_id)).getItemCost();
    	return list;
    }
    
    public List getCostTypes(ItemCostType costType){
    	return costType.getCost();
    }
    
    public String getCostTypesClause(ItemCostType costType){
    	List types = getCostTypes(costType);
    	StringBuffer clause = new StringBuffer(" ( ");
    	for(int i=0,n=types.size();i<n;i++){
    		clause.append(((Labels)types.get(i)).getType()+", ");
    	}
    	clause.append(" -1 )");
    	return clause.toString();
    }


    // calc the usr_can_qr_ind w/ itm_can_qr & quick-reference  
    // must perform get(con) first
    public void setQRInd(Connection con, loginProfile prof) throws SQLException, qdbException {
        Vector targetPreviewVec = ViewItemTargetGroup.getTargetGroupsLrn(con, this.itm_id, ViewItemTargetGroup.TARGETED_PREVIEW);
        if(itm_can_qr_ind) {
            usr_can_qr_ind = !(targetPreviewVec != null && targetPreviewVec.size() > 0 && !targetPreviewVec.contains(new Long(prof.usr_ent_id)));
        } else {
            usr_can_qr_ind = false;
        }
    }

    public String contentAsXML(Connection con, Timestamp cur_time) throws SQLException, qdbException{
    	return contentAsXML(con, cur_time, 0);
    }

    public String contentAsXML(Connection con, Timestamp cur_time, long root_ent_id)
        throws SQLException, qdbException
    {
        StringBuffer result = new StringBuffer();
        int numOFAdmitted = 0;
        int count;

//change capacity_status to pending_count
/*
        if (itm_capacity == 0) {
            capacity_status = ITM_UNLIMITED;
        } else {
            count = aeApplication.countQueue(con, itm_id, aeApplication.ADMITTED);

            if (count == 0) {
                capacity_status = ITM_EMPTY;
            } else if (itm_capacity > count) {
                capacity_status = ITM_AVAILABLE;
            } else {
                capacity_status = ITM_FULL;
            }
        }
*/
        int pending_count = aeApplication.countQueue(con, itm_id, aeApplication.PENDING);
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }

        ViewItemTemplate viItmTpl = new ViewItemTemplate();
        viItmTpl.itemId = itm_id;

        result.append("<item id=\"");
        result.append(itm_id);
        result.append("\" code=\"");
        result.append(cwUtils.esc4XML(itm_code));
        result.append("\" type=\"");
        result.append(itm_type);
        result.append("\" app_start_days_to=\"");
        result.append(dayDiff(cur_time, itm_appn_start_datetime));
        result.append("\" app_start_datetime=\"");
        result.append(aeUtils.escNull(itm_appn_start_datetime));
        result.append("\" app_end_days_to=\"");
        result.append(dayDiff(cur_time, itm_appn_end_datetime));
        result.append("\" app_end_datetime=\"");
        result.append(aeUtils.escNull(itm_appn_end_datetime));
        result.append("\" capacity=\"");
        result.append(aeUtils.escZero(itm_capacity));
        result.append("\" min_capacity=\"");
        result.append(aeUtils.escZero(itm_min_capacity));
        result.append("\" person_in_charge=\"");
        result.append(aeUtils.escNull(dbUtils.esc4XML(itm_person_in_charge)));
        result.append("\" version_code=\"");
        result.append(aeUtils.escNull(dbUtils.esc4XML(itm_version_code)));
        result.append("\" cancellation_reason=\"");
        result.append(aeUtils.escNull(dbUtils.esc4XML(itm_cancellation_reason)));
        result.append("\" pending_count=\"");
        result.append(pending_count);
        result.append("\" notify_days=\"").append(aeUtils.escZero(itm_notify_days));
        result .append("\" notify_email=\"");
        if (itm_notify_email == null || itm_notify_email.equals("")) {
        	result.append(getSysEmailAddress(con, root_ent_id));
        } else {
        	result.append(cwUtils.esc4XML(cwUtils.escNull(itm_notify_email)));
        }
        result.append("\" status=\"");
        result.append(itm_status);
        result.append("\" apply_method=\"");
        result.append(aeUtils.escNull(itm_apply_method));
        result.append("\" eff_start_days_to=\"");
        result.append(dayDiff(cur_time, itm_eff_start_datetime));
        result.append("\" eff_start_datetime=\"");
        result.append(aeUtils.escNull(itm_eff_start_datetime));
        result.append("\" eff_end_days_to=\"");
        result.append(dayDiff(cur_time, itm_eff_end_datetime));
        result.append("\" eff_end_datetime=\"");
        result.append(aeUtils.escNull(itm_eff_end_datetime));
        result.append("\" run_ind=\"");
        result.append(itm_run_ind);
        result.append("\" session_ind=\"");
        result.append(itm_session_ind);
        result.append("\" create_run_ind=\"");
        result.append(itm_create_run_ind);
        result.append("\" create_session_ind=\"");
        result.append(itm_create_session_ind);
        result.append("\" has_attendance_ind=\"");
        result.append(itm_has_attendance_ind);
        result.append("\" ji_ind=\"");
        result.append(itm_ji_ind);
        result.append("\" completion_criteria_ind=\"");
        result.append(itm_completion_criteria_ind);
        result.append("\" itm_integrated_ind=\"");
        result.append(itm_integrated_ind);
        result.append("\">");
        result.append(dbUtils.NEWL);
        result.append(getItemTypeTitle(con));
        result.append("<title>");
        result.append(dbUtils.esc4XML(aeUtils.escNull(itm_title)));
        result.append("</title>");
        result.append(dbUtils.NEWL);
        //get parent info (id and title) for run item
        if(this.itm_run_ind || this.itm_session_ind) {
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = this.itm_id;
            aeItem ireParentItm = ire.getParentInfo(con);
            if (ireParentItm != null) {
                result.append("<parent id=\"").append(ireParentItm.itm_id).append("\"");
                result.append(" type=\"").append(ireParentItm.itm_type).append("\"");
                result.append(" apply_method=\"").append(aeUtils.escNull(ireParentItm.itm_apply_method)).append("\">");
                result.append("<title>");
                result.append(dbUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title)));
                result.append("</title>");
                result.append(dbUtils.NEWL);
                result.append("</parent>");
                result.append(dbUtils.NEWL);
            }
        }
        result.append("</item>");
        result.append(dbUtils.NEWL);
        return result.toString();
    }

    public boolean isLastUpdBefore(Connection con, Timestamp cur_timestamp) throws SQLException, cwSysMessage {
        boolean result;
        final String SQL = " Select itm_upd_timestamp From aeItem "
                         + " Where itm_id = ? ";

         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setLong(1, itm_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next())
            itm_upd_timestamp = rs.getTimestamp("itm_upd_timestamp");
         else
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);

         if(cur_timestamp == null || itm_upd_timestamp == null)
            result = false;
         else {
             result = itm_upd_timestamp.before(cur_timestamp);
         }
         stmt.close();
         return result;
    }


    public boolean isLastUpd(Connection con, Timestamp upd_timestamp) throws SQLException ,cwSysMessage {
        boolean result;
        final String SQL = " Select itm_upd_timestamp From aeItem "
                         + " Where itm_id = ? ";

         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setLong(1, itm_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next())
            itm_upd_timestamp = rs.getTimestamp("itm_upd_timestamp");
         else
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);

         if(upd_timestamp == null || itm_upd_timestamp == null)
            result = false;
         else {
            //upd_timestamp.setNanos(tnd_upd_timestamp.getNanos());
             result = upd_timestamp.equals(itm_upd_timestamp);
         }
         stmt.close();
         return result;
    }


    public boolean hasApplication(Connection con) throws SQLException {
        boolean result;
        long count;
        final String SQL = " Select count(*) From aeApplication "
                         + " Where app_itm_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            count = rs.getLong(1);
        else
            throw new SQLException("Cannot select count(*) from aeApplication");

        result = count > 0;

        stmt.close();
        return result;
    }

    /**
     * check if this item has any runs by counting the number of runs this item has
     * 2001.08.06 wai
     */
    private final String sql_get_run_count =
        "SELECT count(*) FROM aeItemRelation, aeItem WHERE ire_parent_itm_id = ? AND itm_id = ire_child_itm_id AND itm_run_ind = ? ";
    public boolean hasRun(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        boolean result;

        // column variable
        int runCnt = 0;
        // get the number of run items of this item
        stmt = con.prepareStatement(sql_get_run_count);
        col = 1;
        stmt.setLong(col++, itm_id);
        stmt.setBoolean(col++, true);

        rs = stmt.executeQuery();
        if (rs.next()) {
            col = 1;
            runCnt = rs.getInt(col++);
        } else {
            runCnt = 0;
        }
        stmt.close();

        result = runCnt > 0;

        return result;
    }

    public boolean hasUpdPrivilege(Connection con, long owner_ent_id) throws SQLException ,cwSysMessage {
         boolean result;
         StringBuffer SQLBuf = new StringBuffer(300);
         SQLBuf.append(" Select itm_owner_ent_id From aeItem ");
         SQLBuf.append(" Where itm_id = ? ");
         String SQL = new String(SQLBuf);

         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setLong(1, itm_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next())
            itm_owner_ent_id = rs.getLong("itm_owner_ent_id");
         else
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);

        result = itm_owner_ent_id == owner_ent_id;

         stmt.close();
         return result;
    }


    public static String getItemTitle(Connection con, long itm_id)
    throws SQLException, cwSysMessage {
        String title;
        StringBuffer SQLBuf = new StringBuffer(200);
        SQLBuf.append(" Select itm_title From aeItem ");
        SQLBuf.append(" Where itm_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            title = rs.getString("itm_title");
        else
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        rs.close();
        stmt.close();
        return title;
    }
    
    private static final String sql_get_itm_simple_info = " Select itm_title,itm_type,itm_blend_ind,itm_exam_ind,itm_ref_ind, itm_content_def From aeItem Where itm_id = ?";
    public void getItemSimpleInfo(Connection con, long itm_id)
    throws SQLException, cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_simple_info);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
        	itm_title = rs.getString("itm_title");
        	itm_type = rs.getString("itm_type");
        	itm_blend_ind = rs.getBoolean("itm_blend_ind");
        	itm_exam_ind = rs.getBoolean("itm_exam_ind");
        	itm_ref_ind = rs.getBoolean("itm_ref_ind");
        	itm_content_def = rs.getString("itm_content_def");
        	itm_dummy_type = aeItemDummyType.getDummyItemType(itm_type, itm_blend_ind, itm_exam_ind, itm_ref_ind);
        } else
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        rs.close();
        stmt.close();
    }

    public static String getItemCode(Connection con, long itm_id)
    throws SQLException, qdbException ,cwSysMessage {
        String itm_code;
        StringBuffer SQLBuf = new StringBuffer(200);
        SQLBuf.append(" Select itm_code From aeItem ");
        SQLBuf.append(" Where itm_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            itm_code = rs.getString("itm_code");
        else{
        	if(rs!=null)rs.close();
            stmt.close();
            con.rollback();
            //throw new SQLException("Cannot find Item, itm_id = " + itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        }
        rs.close();
        stmt.close();
        return itm_code;
    }


    /**
     * @deprecated (2003-07-29 kawai)
     * please modify to not returning a ResultSet if this method is to be reused.
     */
    /**
    Get items title
    @param vector containing the items id
    @param sort by (ASC/DESC)
    @return result set (itm_id, itm_title)
    */
    /*
    public ResultSet getItemsTitle(Connection con, Vector itmIdVec, String sort_by)
        throws SQLException {

            String sql_get_items_title = " SELECT itm_id, itm_title FROM aeItem "
                                        + " WHERE itm_id IN "
                                        + cwUtils.vector2list(itmIdVec)
                                        + "  ORDER BY itm_id  "
                                        + sort_by;

            PreparedStatement stmt = con.prepareStatement(sql_get_items_title);
            ResultSet rs = stmt.executeQuery();

            return rs;

        }
    */

    private long dayDiff(Timestamp time_from, Timestamp time_to)
        throws SQLException
    {
        long days_to = 0;

        if (time_to != null && time_from != null) {
                Calendar date1 = Calendar.getInstance();
                Calendar date2 = Calendar.getInstance();
                Date d1;
                Date d2;
                long mini1;
                long mini2;

                date1.setTime(time_from);
                date1.set(Calendar.HOUR_OF_DAY, 0);
                date1.set(Calendar.MINUTE, 0);
                date1.set(Calendar.SECOND, 0);
                date1.set(Calendar.MILLISECOND, 0);
                d1 = date1.getTime();
                mini1 = d1.getTime();

                date2.setTime(time_to);
                date2.set(Calendar.HOUR_OF_DAY, 0);
                date2.set(Calendar.MINUTE, 0);
                date2.set(Calendar.SECOND, 0);
                date2.set(Calendar.MILLISECOND, 0);
                d2 = date2.getTime();
                mini2 = d2.getTime();

                days_to = (mini2-mini1)/(24*60*60*1000);
        }

        return days_to;
    }

    public static long getMaxItemId(Connection con) throws SQLException {
        long id;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select max(itm_id) From aeItem ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            id = rs.getLong(1);
        else
            throw new SQLException("Cannot get the max itm_id from aeItem");

        stmt.close();
        return id;
    }

    String searchRunViewAsXML(Connection con, long p_itm_id, String p_itm_title, long p_itm_capacity, long p_itm_min_capacity, String p_itm_life_status, boolean show_attendance_ind, String p_itm_code,loginProfile prof, String stylesheet)
      throws SQLException, cwException {
        Timestamp cur_time = cwSQL.getTime(con);
        StringBuffer xmlBuf = new StringBuffer(1000);
        xmlBuf.append("<item id=\"").append(itm_id).append("\"");
        xmlBuf.append(" parent_id=\"").append(p_itm_id).append("\"");
        xmlBuf.append(" parent_title=\"").append(dbUtils.esc4XML(p_itm_title)).append("\"");
        xmlBuf.append(" parent_capacity=\"").append(p_itm_capacity).append("\"");
        xmlBuf.append(" parent_min_capacity=\"").append(p_itm_min_capacity).append("\"");
        xmlBuf.append(" parent_life_status=\"").append(cwUtils.escNull(p_itm_life_status)).append("\"");
        if(p_itm_code != null) {
            xmlBuf.append(" parent_code=\"").append(cwUtils.esc4XML(p_itm_code)).append("\"");
        }
        xmlBuf.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("\"");
        xmlBuf.append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"");
        xmlBuf.append(" status=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_status))).append("\"");
        xmlBuf.append(" type=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_type))).append("\"");
        xmlBuf.append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"");
        xmlBuf.append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"");
        xmlBuf.append(" appn_start_datetime=\"").append(aeUtils.escNull(itm_appn_start_datetime)).append("\"");
        xmlBuf.append(" appn_end_datetime=\"").append(aeUtils.escNull(itm_appn_end_datetime)).append("\"");
        xmlBuf.append(" eff_start_days_to=\"").append(dayDiff(cur_time, itm_eff_start_datetime)).append("\"");
        xmlBuf.append(" eff_end_days_to=\"").append(dayDiff(cur_time, itm_eff_end_datetime)).append("\"");
        xmlBuf.append(" appn_start_days_to=\"").append(dayDiff(cur_time, itm_appn_start_datetime)).append("\"");
        xmlBuf.append(" appn_end_days_to=\"").append(dayDiff(cur_time, itm_appn_end_datetime)).append("\"");
        xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"");
        xmlBuf.append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"");
        xmlBuf.append(" apply_method=\"").append(itm_apply_method).append("\"");
        xmlBuf.append(" capacity=\"").append(itm_capacity).append("\"");
        xmlBuf.append(" min_capacity=\"").append(itm_min_capacity).append("\"");
        xmlBuf.append(" unit=\"").append(itm_unit).append("\"");
        xmlBuf.append(" version_code=\"").append(dbUtils.esc4XML(itm_version_code)).append("\"");
        xmlBuf.append(" person_in_charge=\"").append(dbUtils.esc4XML(itm_person_in_charge)).append("\"");
        xmlBuf.append(" cancellation_reason=\"").append(dbUtils.esc4XML(itm_cancellation_reason)).append("\"");
        xmlBuf.append(" create_run_ind=\"").append(itm_create_run_ind).append("\"");
        xmlBuf.append(" create_session_ind=\"").append(itm_create_session_ind).append("\"");
        xmlBuf.append(" qdb_ind=\"").append(itm_qdb_ind).append("\"");
        xmlBuf.append(" auto_enrol_qdb_ind=\"").append(itm_auto_enrol_qdb_ind).append("\"");
        xmlBuf.append(" run_ind=\"").append(itm_run_ind).append("\"");
        xmlBuf.append(" session_ind=\"").append(itm_session_ind).append("\"");
        xmlBuf.append(" has_attendance_ind=\"").append(itm_has_attendance_ind).append("\"");
        xmlBuf.append(" ji_ind=\"").append(itm_ji_ind).append("\"");
        xmlBuf.append(" completion_criteria_ind=\"").append(itm_completion_criteria_ind).append("\"");
        xmlBuf.append(" apply_ind=\"").append(itm_apply_ind).append("\"");
        xmlBuf.append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"");
        xmlBuf.append(" imd_id=\"").append(aeUtils.escZero(itm_imd_id)).append("\"");
        xmlBuf.append(" rsv_id=\"").append(aeUtils.escZero(itm_rsv_id)).append("\"");
        xmlBuf.append(" tcr_id=\"").append(aeUtils.escZero(itm_tcr_id)).append("\"");
        xmlBuf.append(" itm_upd_timestamp=\"").append(aeUtils.escNull(itm_upd_timestamp)).append("\"");
        if(this.itm_id != 0) {
            xmlBuf.append(" enrol_cnt=\"").append(aeApplication.countItemAppn(con, this.itm_id, show_attendance_ind)).append("\"");
        }
        xmlBuf.append(">");
        if(stylesheet != null) {
			Hashtable xslQuestions = AcXslQuestion.getQuestions();
			String[] str = (String[]) xslQuestions.get(stylesheet);
			if(str != null && str.length > 0) {
				String itemAcXML = null;
				if(prof != null) {
					AcPageVariant acPageVariant = new AcPageVariant(con);
					acPageVariant.prof = prof;
					acPageVariant.instance_id = itm_id;
					acPageVariant.ent_owner_ent_id = prof.root_ent_id;
					acPageVariant.ent_id = prof.usr_ent_id;
					acPageVariant.rol_ext_id = prof.current_role;
					acPageVariant.root_id = prof.root_id;
	                itemAcXML=acPageVariant.answerPageVariantAsXML(str);
				}
				xmlBuf.append(itemAcXML);
			}
        }
        //get application count by app_status
        xmlBuf.append(aeApplication.getAppnCountByItemAsXML(con, this.itm_id));
        //show attendance here
        if(show_attendance_ind) {
            aeAttendanceStatus currentStatus = new aeAttendanceStatus();
            xmlBuf.append(aeAttendance.stateAsXML(con, itm_owner_ent_id, itm_id, -1, currentStatus));
        }
        xmlBuf.append("</item>");
        return xmlBuf.toString();
    }

    public String searchViewAsXML(Connection con, boolean show_attendance_ind,loginProfile prof, boolean tcEnabled, Timestamp curTime)
      throws SQLException, cwException {
        //Timestamp cur_time = cwSQL.getTime(con);
        StringBuffer xmlBuf = new StringBuffer(1000);
        xmlBuf.append("<item id=\"").append(itm_id).append("\">");
        xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>");
        xmlBuf.append("<code>").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("</code>");
        xmlBuf.append("<status>").append(dbUtils.esc4XML(aeUtils.escNull(itm_status))).append("</status>");
        xmlBuf.append("<type>").append(dbUtils.esc4XML(aeUtils.escNull(itm_type))).append("</type>");
        xmlBuf.append("<dummy_type>").append(dbUtils.esc4XML(aeUtils.escNull(itm_dummy_type))).append("</dummy_type>");
        xmlBuf.append("<eff_start_datetime>").append(aeUtils.escNull(itm_eff_start_datetime)).append("</eff_start_datetime>");
        xmlBuf.append("<eff_end_datetime>").append(aeUtils.escNull(itm_eff_end_datetime)).append("</eff_end_datetime>");
        xmlBuf.append("<appn_start_datetime>").append(aeUtils.escNull(itm_appn_start_datetime)).append("</appn_start_datetime>");
        xmlBuf.append("<appn_end_datetime>").append(aeUtils.escNull(itm_appn_end_datetime)).append("</appn_end_datetime>");
        xmlBuf.append("<eff_start_days_to>").append(dayDiff(curTime, itm_eff_start_datetime)).append("</eff_start_days_to>");
        xmlBuf.append("<eff_end_days_to>").append(dayDiff(curTime, itm_eff_end_datetime)).append("</eff_end_days_to>");
        xmlBuf.append("<appn_start_days_to>").append(dayDiff(curTime, itm_appn_start_datetime)).append("</appn_start_days_to>");
        xmlBuf.append("<appn_end_days_to>").append(dayDiff(curTime, itm_appn_end_datetime)).append("</appn_end_days_to>");
        xmlBuf.append("<ext1>").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("</ext1>");
        //xmlBuf.append("<sks_id>").append(itm_sks_skb_id).append("</>");
        xmlBuf.append("<life_status>").append(aeUtils.escNull(itm_life_status)).append("</life_status>");
        xmlBuf.append("<apply_method>").append(itm_apply_method).append("</apply_method>");
        xmlBuf.append("<capacity>").append(itm_capacity).append("</capacity>");
        xmlBuf.append("<min_capacity>").append(itm_min_capacity).append("</min_capacity>");
        xmlBuf.append("<unit>").append(itm_unit).append("</unit>");
        xmlBuf.append("<version_code>").append(dbUtils.esc4XML(itm_version_code)).append("</version_code>");
        xmlBuf.append("<person_in_charge>").append(dbUtils.esc4XML(itm_person_in_charge)).append("</person_in_charge>");
        xmlBuf.append("<cancellation_reason>").append(dbUtils.esc4XML(itm_cancellation_reason)).append("</cancellation_reason>");
        xmlBuf.append("<create_run_ind>").append(itm_create_run_ind).append("</create_run_ind>");
        xmlBuf.append("<create_session_ind>").append(itm_create_session_ind).append("</create_session_ind>");
        xmlBuf.append("<qdb_ind>").append(itm_qdb_ind).append("</qdb_ind>");
        xmlBuf.append("<auto_enrol_qdb_ind>").append(itm_auto_enrol_qdb_ind).append("</auto_enrol_qdb_ind>");
        xmlBuf.append("<run_ind>").append(itm_run_ind).append("</run_ind>");
        xmlBuf.append("<session_ind>").append(itm_session_ind).append("</session_ind>");
        xmlBuf.append("<has_attendance_ind>").append(itm_has_attendance_ind).append("</has_attendance_ind>");
        xmlBuf.append("<ji_ind>").append(itm_ji_ind).append("</ji_ind>");
        xmlBuf.append("<completion_criteria_ind>").append(itm_completion_criteria_ind).append("</completion_criteria_ind>");
        xmlBuf.append("<apply_ind>").append(itm_apply_ind).append("</apply_ind>");
        xmlBuf.append("<deprecated_ind>").append(itm_deprecated_ind).append("</deprecated_ind>");
        //xmlBuf.append("<enrol_cnt>").append(aeApplication.countItemAppn(con, this.itm_id, show_attendance_ind)).append("</enrol_cnt>");--20130111简化
        xmlBuf.append("<upd_timestamp>").append(aeUtils.escNull(itm_upd_timestamp)).append("</upd_timestamp>");
        xmlBuf.append("<imd_id>").append(aeUtils.escZero(itm_imd_id)).append("</imd_id>");
        xmlBuf.append("<enrolled>").append(enrolled).append("</enrolled>");
        //xmlBuf.append("<create_usr_id>").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_create_usr_id))).append("</create_usr_id>");--20130111简化
        if(itm_tcr_title != null && !itm_tcr_title.equals("")) {
        	xmlBuf.append("<tcr_title>").append(cwUtils.esc4XML(itm_tcr_title)).append("</tcr_title>");
        }
       
        if(itm_create_run_ind && child_item != null && child_item.itm_id > 0) {
        	xmlBuf.append("<class id=\"").append(child_item.itm_id).append("\">");
			xmlBuf.append("<c_title>").append(dbUtils.esc4XML(aeUtils.escNull(child_item.itm_title))).append("</c_title>");
		    xmlBuf.append("<c_code>").append(dbUtils.esc4XML(aeUtils.escNull(child_item.itm_code))).append("</c_code>");
		    xmlBuf.append("<c_type>").append(dbUtils.esc4XML(aeUtils.escNull(child_item.itm_type))).append("</c_type>");
		    xmlBuf.append("</class>");
        }
		String[] str=new String[]{"hasItmEditBtn"};
		String itemAcXML = null;
		if(prof != null) {
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.prof = prof;
			acPageVariant.instance_id = itm_id;
			acPageVariant.ent_owner_ent_id = prof.root_ent_id;
			acPageVariant.ent_id = prof.usr_ent_id;
			acPageVariant.rol_ext_id = prof.current_role;
			acPageVariant.root_id = prof.root_id;
			acPageVariant.tc_enable_ind = tcEnabled;
		//	acPageVariant.setWizbiniLoader(wizbini);
		itemAcXML=acPageVariant.answerPageVariantAsXML(str);
						}
        xmlBuf.append(itemAcXML);
        if (itm_approval_status != null){
            xmlBuf.append("<approval_status>").append(itm_approval_status).append("</approval_status>").append(dbUtils.NEWL);
        }
        /*--20130111简化
        if(this.itm_id > 0) {
            //get application count by app_status
            xmlBuf.append(aeApplication.getAppnCountByItemAsXML(con, this.itm_id));
        }
        */
        //show attendance here
        if(show_attendance_ind) {
            aeAttendanceStatus currentStatus = new aeAttendanceStatus();
            xmlBuf.append(aeAttendance.stateAsXML(con, itm_owner_ent_id, itm_id, -1, currentStatus));
        }
        //item title
//        xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>").append(dbUtils.NEWL);

        /*--20130111简化
        xmlBuf.append("<last_updated usr_id=\"").append(itm_upd_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(itm_upd_timestamp).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_upd_usr_id)));
        xmlBuf.append("</last_updated>");

        xmlBuf.append("<creator usr_id=\"").append(itm_create_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(itm_create_timestamp).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_create_usr_id)));
        xmlBuf.append("</creator>");

        if (itm_approve_usr_id!=null){
            xmlBuf.append("<approver usr_id=\"").append(itm_approve_usr_id).append("\"");
            xmlBuf.append(" action=\"").append(itm_approval_action).append("\"");
            xmlBuf.append(" timestamp=\"").append(itm_approve_timestamp).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_approve_usr_id)));
            xmlBuf.append("</approver>");
        }

        if (itm_submit_usr_id!=null){
            xmlBuf.append("<submitted usr_id=\"").append(itm_submit_usr_id).append("\"");
            xmlBuf.append(" action=\"").append(itm_submit_action).append("\"");
            xmlBuf.append(" timestamp=\"").append(itm_submit_timestamp).append("\">").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, itm_submit_usr_id)));
            xmlBuf.append("</submitted>");
        }
		*/
        xmlBuf.append("</item>");
        xmlBuf.append(dbUtils.NEWL);
        return xmlBuf.toString();
    }

    public String bookSlotAsXML(long tnd_id, String tnd_title, String tnd_ext2, String tnd_ext3, String tnd_ext5) {
        StringBuffer xmlBuf = new StringBuffer(1000);
        xmlBuf.append("<item id=\"").append(itm_id).append("\"");
        xmlBuf.append(" type=\"").append(itm_type).append("\"");
        xmlBuf.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("\"");
        xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"");
        xmlBuf.append(" slot_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"");
        xmlBuf.append(" slot_status=\"").append(aeUtils.escNull(itm_status)).append("\">").append(dbUtils.NEWL);
        xmlBuf.append(" <parent_node id=\"").append(tnd_id).append("\"");
        xmlBuf.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_title))).append("\"");
        xmlBuf.append(" ext2=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext2))).append("\"");
        xmlBuf.append(" ext3=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext3))).append("\"");
        xmlBuf.append(" ext5=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext5))).append("\"/>").append(dbUtils.NEWL);
        xmlBuf.append("</item>");
        xmlBuf.append(dbUtils.NEWL);
        return xmlBuf.toString();
    }

    public static String inputParamAsXML(Hashtable param) {
        Timestamp start_date = (Timestamp) param.get(START_DATE);
        Timestamp end_date = (Timestamp) param.get(END_DATE);
        String tnd_title = (String) param.get(TND_TITLE);
        String tnd_ext1 = (String) param.get(TND_EXT1);
        String tnd_ext2 = (String) param.get(TND_EXT2);
        String tnd_ext3 = (String) param.get(TND_EXT3);
        String tnd_ext5 = (String) param.get(TND_EXT5);
        String begin_tnd_title = (String) param.get(BEGIN_TND_TITLE);
        String status = (String) param.get(STATUS);
        String child_itm_type = (String) param.get(CHILD_ITEM_TYPE);
        String orderBy = ((String) param.get(ORDERBY)).trim();
        String sortOrder = ((String) param.get(SORTORDER)).trim();

        StringBuffer xmlBuf = new StringBuffer(1000);
        xmlBuf.append("<input begin_tnd_title=\"").append(dbUtils.esc4XML(begin_tnd_title)).append("\"");
        xmlBuf.append(" tnd_title=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_title))).append("\"");
        xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext1))).append("\"");
        xmlBuf.append(" ext2=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext2))).append("\"");
        xmlBuf.append(" ext3=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext3))).append("\"");
        xmlBuf.append(" ext5=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext5))).append("\"");
        xmlBuf.append(" status=\"").append(aeUtils.escNull(status)).append("\"");
        xmlBuf.append(" child_itm_type=\"").append(aeUtils.escNull(child_itm_type)).append("\"");
        xmlBuf.append(" orderby=\"").append(orderBy).append("\"");
        xmlBuf.append(" sortorder=\"").append(sortOrder).append("\"");
        xmlBuf.append(" start_date=\"").append(aeUtils.escEmptyDate(start_date)).append("\"");
        xmlBuf.append(" end_date=\"").append(aeUtils.escEmptyDate(end_date)).append("\"/>").append(dbUtils.NEWL);
        String xml = new String(xmlBuf);
        return xml;
    }

    public static String searchItemParamAsXML(Hashtable param) {
        boolean all_ind = ((Boolean) param.get(ALL_IND)).booleanValue();
        long[] tnd_ids = (long[]) param.get(TND_ID_LIST);
        String itm_status = (String) param.get(ITM_STATUS);
        String itm_code = (String) param.get(ITM_CODE);
        String itm_title = (String) param.get(ITM_TITLE);
        boolean exact = ((Boolean) param.get(EXACT)).booleanValue();
        Timestamp itm_appn_from = (Timestamp) param.get(ITM_APPN_FROM);
        Timestamp itm_appn_to = (Timestamp) param.get(ITM_APPN_TO);
        Timestamp itm_eff_from = (Timestamp) param.get(ITM_EFF_FROM);
        Timestamp itm_eff_to = (Timestamp) param.get(ITM_EFF_TO);
        String[] itm_types = (String[]) param.get(ITM_TYPES);
        String orderBy = (String) param.get(ORDERBY);
        String sortOrder = (String) param.get(SORTORDER);
        boolean all_itm_types = ((Boolean) param.get(ALL_ITM_TYPES)).booleanValue();
        boolean show_run_ind = ((Boolean) param.get(ITM_SHOW_RUN_IND)).booleanValue();
        boolean itm_only_open_enrol_now = ((Boolean) param.get(ITM_ONLY_OPEN_ENROL_NOW)).booleanValue();
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<input all_ind=\"").append(all_ind).append("\"");
        xmlBuf.append(" status=\"").append(aeUtils.escNull(itm_status)).append("\"");
        xmlBuf.append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"");
        xmlBuf.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("\"");
        xmlBuf.append(" exact=\"").append(exact).append("\"");
        xmlBuf.append(" appn_from=\"").append(aeUtils.escEmptyDate(itm_appn_from)).append("\"");
        xmlBuf.append(" appn_to=\"").append(aeUtils.escEmptyDate(itm_appn_to)).append("\"");
        xmlBuf.append(" eff_from=\"").append(aeUtils.escEmptyDate(itm_eff_from)).append("\"");
        xmlBuf.append(" eff_to=\"").append(aeUtils.escEmptyDate(itm_eff_to)).append("\"");
        xmlBuf.append(" show_run_ind=\"").append(show_run_ind).append("\"");
        xmlBuf.append(" orderby=\"").append(orderBy).append("\"");
        xmlBuf.append(" only_open_enrol_now=\"").append(itm_only_open_enrol_now).append("\"");
        xmlBuf.append(" sortorder=\"").append(sortOrder).append("\">").append(dbUtils.NEWL);
        xmlBuf.append("<types>").append(dbUtils.NEWL);
        if(!all_itm_types) {
            for(int i=0;i<itm_types.length;i++)
                xmlBuf.append("<type value=\"").append(aeUtils.escNull(itm_types[i])).append("\"/>").append(dbUtils.NEWL);
        }
        xmlBuf.append("</types>");
        xmlBuf.append("<tnd_id_list>");
        if(tnd_ids != null) {
            for(int i=0;i<tnd_ids.length;i++)
                xmlBuf.append("<tnd_id value=\"").append(aeUtils.escZero(tnd_ids[i])).append("\"/>").append(dbUtils.NEWL);
        }
        xmlBuf.append("</tnd_id_list>");
        xmlBuf.append("</input>");
        String xml = new String(xmlBuf);
        return xml;
    }

    //serach for item nodes according to the input params
    //and return a 2 dimenional long array to store the tnd_id and itm_id of the BOOK_UNIT
    public static long[][] searchBook_Unit(Connection con, Hashtable param, boolean checkStatus)
      throws SQLException {
        String begin_tnd_title = (String) param.get(BEGIN_TND_TITLE);
        boolean allNull = ((Boolean) param.get(ALLNULL)).booleanValue();

        Vector v_itm_ids = new Vector();    //store the itm_id of the item node found
        Vector v_tnd_ids = new Vector();    //store the tnd_id of the found itme node belongs to

        //get the starting search points according to the input tnd_title and tnd_ext2
        long[] tnd_ids = aeTreeNode.getTndIDFromTitle(con, begin_tnd_title, allNull);
        for(int i=0;i<tnd_ids.length;i++)
            recurSearchBook_Unit(con, tnd_ids[i], checkStatus, param, v_tnd_ids, v_itm_ids);

        return aeUtils.vec2long2DimArray(v_tnd_ids, v_itm_ids);
    }

    //assmue the input tnd_id is a normal node
    //it will not follow the links if input tnd_id is a link node
    public static void recurSearchBook_Unit(Connection con, long tnd_id, boolean checkStatus
                                           ,Hashtable param, Vector v_tnd_ids, Vector v_itm_ids)
      throws SQLException {
        aeTreeNode tnd = new aeTreeNode();
        tnd.tnd_id = tnd_id;

        // search for child meet criteria
        String child_tnd_title = (String) param.get(TND_TITLE);
        String child_tnd_ext1 = (String) param.get(TND_EXT1);
        String child_tnd_ext2 = (String) param.get(TND_EXT2);
        String child_tnd_ext3 = (String) param.get(TND_EXT3);
        String child_tnd_ext5 = (String) param.get(TND_EXT5);
        long[] child_tnd_ids = tnd.searchChild(con, checkStatus, child_tnd_title, child_tnd_ext1
                                              ,child_tnd_ext2, child_tnd_ext3, child_tnd_ext5);

        // get the items for each child found above
        String itm_status = (String) param.get(ITM_STATUS);
        String itm_code = (String) param.get(ITM_CODE);
        String itm_title = (String) param.get(ITM_TITLE);
        Timestamp itm_appn_from = (Timestamp) param.get(ITM_APPN_FROM);
        Timestamp itm_appn_to = (Timestamp) param.get(ITM_APPN_TO);
        Timestamp itm_eff_from = (Timestamp) param.get(ITM_EFF_FROM);
        Timestamp itm_eff_to = (Timestamp) param.get(ITM_EFF_TO);
        String itm_appn_from_operator = (String) param.get(ITM_APPN_FROM_OPERATOR);
        String itm_appn_to_operator = (String) param.get(ITM_APPN_TO_OPERATOR);
        String itm_eff_from_operator = (String) param.get(ITM_EFF_FROM_OPERATOR);
        String itm_eff_to_operator = (String) param.get(ITM_EFF_TO_OPERATOR);
        if(itm_appn_from_operator == null || itm_appn_from_operator.length() == 0) {
            itm_appn_from_operator = ">=";
        }
        if(itm_appn_to_operator == null || itm_appn_to_operator.length() == 0) {
            itm_appn_to_operator = "<=";
        }
        if(itm_eff_from_operator == null || itm_eff_from_operator.length() == 0) {
            itm_eff_from_operator = ">=";
        }
        if(itm_eff_to_operator == null || itm_eff_to_operator.length() == 0) {
            itm_eff_to_operator = "<=";
        }
        String[] itm_types = (String[]) param.get(ITM_TYPES);
        boolean exact = ((Boolean) param.get(EXACT)).booleanValue();

        aeTreeNode child_tnd;
        for(int i=0;i<child_tnd_ids.length;i++) {
            child_tnd = new aeTreeNode();
            child_tnd.tnd_id = child_tnd_ids[i];

            long[][] result = child_tnd.getItemNodeId(con, checkStatus, itm_status
                                                     ,itm_code, itm_title
                                                     ,itm_appn_from, itm_appn_to
                                                     ,itm_eff_from, itm_eff_to
                                                     ,itm_types, exact
                                                     ,false, null);
            for(int j=0;j<result[0].length; j++) {
                v_tnd_ids.addElement(new Long(result[0][j]));
                v_itm_ids.addElement(new Long(result[1][j]));
            }
        }

        //recursive search the tree for children treenodes
        //child_tnd_ids = tnd.getChildNodeId(con, checkStatus, aeCatalogAccess.prepareList(child_tnd_ids));
        child_tnd_ids = tnd.getChildNodeId(con, checkStatus);
        for(int i=0;i<child_tnd_ids.length;i++)
            recurSearchBook_Unit(con, child_tnd_ids[i], checkStatus, param, v_tnd_ids, v_itm_ids);

        return;
    }

    public static Hashtable getSearchSessionHash(Connection con, HttpSession sess, Hashtable param) {
        Hashtable hash = null;
        boolean frSess;

        if(sess != null) {
            Timestamp sessTime = (Timestamp) sess.getAttribute(ITM_SESS_TIMESTAMP);
            Timestamp paramTime = (Timestamp) param.get(ITM_PARAM_TIMESTAMP);
            frSess = sessTime != null && paramTime != null && sessTime.equals(paramTime);
        }
        else
            frSess = false;

        if(frSess) {
            hash = (Hashtable) sess.getAttribute(ITM_SEARCH_ITM_PARAM);
        }

        return hash;
    }

    public static String searchItemAsXML(Connection con, HttpSession sess, Hashtable param, boolean checkStatus, boolean checkLifeStatus, boolean responAllItem,
            boolean checkIsTargetedItem, long[] usr_ent_ids, long owner_ent_id, qdbEnv inEnv, long usr_ent_id, String usr_id, String rol_ext_id, loginProfile prof,
            WizbiniLoader wizbini, String stylesheet, boolean tcEnabled) throws qdbException, SQLException, cwSysMessage, cwException {
    	
    	
        // override the checkStatus
        if (!checkStatus) {
            Boolean showOffItem = (Boolean)param.get(SHOW_OFF_ITM);

            if (showOffItem != null && !showOffItem.booleanValue()) {
                checkStatus = true;
            }
        }

        if (!checkLifeStatus) {
            Boolean showPreApproveItem = (Boolean)param.get(SHOW_PRE_APPROVE_ITM);

            if (showPreApproveItem != null && !showPreApproveItem.booleanValue()) {
                checkLifeStatus = true;
            }
        }

        boolean frSess;
        Timestamp sessTime,paramTime;
        Timestamp cur_time = cwSQL.getTime(con);
        if(sess != null) {
            sessTime = (Timestamp) sess.getAttribute(ITM_SESS_TIMESTAMP);
            paramTime = (Timestamp) param.get(ITM_PARAM_TIMESTAMP);
            frSess = sessTime != null && paramTime != null && sessTime.equals(paramTime);
        } else
            frSess = false;

        if(frSess) {
            //before restore the param for session, get the order by and sort order 1st
            String orderBy = (String) param.get(ORDERBY);
            String sortOrder = (String) param.get(SORTORDER);
            Long page = (Long) param.get(PAGE);
            Long page_size = (Long) param.get(PAGE_SIZE);
            param = (Hashtable) sess.getAttribute(ITM_SEARCH_ITM_PARAM);
            //put back the order by and sort order
            param.put(ORDERBY, orderBy);
            param.put(SORTORDER, sortOrder);
            param.put(PAGE, page);
            param.put(PAGE_SIZE, page_size);
        } else {
            //put usr_id into param which needed to search out items created by this user
            if(usr_id  !=  null)  {
                param.put(ITM_CREATE_USR_ID, usr_id);
            }
            //put default value into param that has not been put to avoid NullPointerException
            initSearchItemParam(param, cur_time);
            //init the param ITM_TYPES: if the itm_type is integer,
            //get the item type id (ity_id) from database by ity_seq_id
            //if itm_type is APPLICABLE, convert to organization's applicable item type
            initSearchItemTypesParam(con, param, owner_ent_id);
        }
        if(!frSess) {
            sessTime = cur_time;
            sess.setAttribute(ITM_SEARCH_ITM_PARAM, param);
            sess.setAttribute(ITM_SESS_TIMESTAMP, sessTime);
        }
        else
            sessTime = (Timestamp) sess.getAttribute(ITM_SESS_TIMESTAMP);

	    StringBuffer xmlBuf = new StringBuffer(2500);
	    
	    xmlBuf.append("<training_type>").append(param.get(TRAINING_TYPE)).append("</training_type>");
	    long[] tcr_id_lst = (long[]) param.get(TCR_ID_LIST);
	    if(tcr_id_lst !=null && tcr_id_lst.length==1 && tcr_id_lst[0]==-1){
            ViewTrainingCenter viewTCR = new ViewTrainingCenter();
        	List lTcr = viewTCR.getTrainingCenterByOfficer(con, usr_ent_id, rol_ext_id, false);
        	if(lTcr==null){
        		tcr_id_lst[0]=0;
        	}else{
        		Vector vec = new Vector();

				for (int i = 0; i < lTcr.size(); i++) {
					DbTrainingCenter tcr = (DbTrainingCenter) lTcr.get(i);
					if (!vec.contains(new Long(tcr.tcr_id))) {
						vec.add(new Long(tcr.tcr_id));
					}
					Vector childs = DbTrainingCenter.getChildTc(con, tcr.tcr_id);
					if (childs != null && childs.size() > 0) {
						for (int j = 0; j < childs.size(); j++) {
							if (!vec.contains(childs.elementAt(j))) {
								vec.add(childs.elementAt(j));
							}	
						}
					}
				}
				
				tcr_id_lst = new long[vec.size()];
				if (vec != null && vec.size() > 0) {
					for (int j = 0; j < vec.size(); j++) {
						tcr_id_lst[j] = ((Long) vec.elementAt(j)).longValue();
					}
				}
        	}
        	xmlBuf.append("<select_type>1</select_type>");
        }else if(tcr_id_lst ==null ||tcr_id_lst.length==0){
	    	xmlBuf.append("<select_type>0</select_type>");
	    }
	    StringBuffer tcrBuf = new StringBuffer();
	    for(int i=0; i<tcr_id_lst.length; i++){
	    	tcrBuf.append("<tcr id=\"").append(tcr_id_lst[i]).append("\"/>");
	    }
	    xmlBuf.append("<input_tcr_lst>").append(tcrBuf).append("</input_tcr_lst>");
	    
	    xmlBuf.append(getDefaultTCR4Search(con, usr_ent_id, rol_ext_id));
        xmlBuf.append(aeQueueManager.getExportColsXML(wizbini, prof.root_id, aeQueueManager.EXPORT_ITEM_SEARCH));
        xmlBuf.append(getColumnsLabel(con,wizbini, prof, aeQueueManager.EXPORT_ITEM_SEARCH));
        xmlBuf.append(searchItemResultAsXML(con, param, sessTime, checkStatus, inEnv, owner_ent_id,prof, tcEnabled));
        
        return xmlBuf.toString();
    }

    public static boolean hasTargetedLrnPrivilege(Connection con, long root_ent_id, long usr_ent_id, long itm_id)
    throws SQLException, cwException {
	
		Vector targetItmLst = new Vector();
		if(usrTargetItmLstHS.get(new Long (usr_ent_id)) != null){
			targetItmLst = (Vector)usrTargetItmLstHS.get(new Long (usr_ent_id));
		}else{
			targetItmLst = getUserTargetItem(con, root_ent_id, usr_ent_id);
			usrTargetItmLstHS.put(new Long (usr_ent_id),targetItmLst);
		}
        return targetItmLst.contains(new Long(itm_id));
    }
    
	public static Vector getUserTargetItem(Connection con, long root_ent_id, long usr_ent_id) throws SQLException, cwException {
		Vector targetItemVec =  new Vector();
		String[] acsite_targeted_entity = ViewEntityToTree.getTargetEntity(con, root_ent_id);
		int usr_dimension = aeLearningPlan.getUsrDimensionByUsrEntId(con, usr_ent_id);
		String SQL = "";
		PreparedStatement stmt = null;
		CallableStatement proc = null;
		ResultSet rs;

		if (acsite_targeted_entity.length <= usr_dimension) {
			if (cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())) {
				SQL = "{call getItemsForTargetUser(?,?)}";

			} else {
				// 对于db2数数据库的存储过程，暂时还没有做
				SQL = "exec getItemsForTargetUser " + usr_ent_id;
			}
		} else {
			SQL += " SELECT itm_id FROM aeItem WHERE (itm_access_type = 'ALL' or itm_access_type is NULL)";
		}
		if (acsite_targeted_entity.length <= usr_dimension && cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())) {
			proc = con.prepareCall(SQL);
			proc.setInt(1, (int)usr_ent_id);
			proc.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
			proc.execute();
			rs = (ResultSet) proc.getObject(2);
		} else {
			stmt = con.prepareStatement(SQL);
			rs = stmt.executeQuery();
		}

		while (rs.next()) {
			targetItemVec.add(rs.getLong("itm_id"));
		}
		if (stmt != null) {
			stmt.close();
		}
		if (proc != null) {
			proc.close();
		}
		return targetItemVec;
	}

    public static String getColumnsLabel(Connection con, WizbiniLoader wizbini, loginProfile prof, String export_type ) throws SQLException {
        ExportCols export_cols = (ExportCols)wizbini.cfgOrgExportCols.get(prof.root_id);
        List export_lst = export_cols.getItmSearchList().getColumns().getCol();
      
        StringBuffer labelXML = new StringBuffer();
        labelXML.append("<columns_label>");
        if(export_lst != null) {
        	Iterator it = export_lst.iterator();
        	while(it.hasNext()) {
        		ColType col = (ColType)it.next();
        		String id = col.getId();
        		String label = col.getLabel();
//        		System.out.println("label = " + label);
        		labelXML.append("<col id=\"").append(id).append("\"")
        		        .append(" label=\"").append(cwUtils.esc4XML(cwUtils.escNull(LangLabel.getValue(prof.cur_lan, label)))).append("\"")
        		        .append("/>");
        	}
        }
        labelXML.append("</columns_label>");

        return labelXML.toString();
    }

    /**
    Find out the run items of the item as xml<BR>
    @param checkStatus used to determine if need to check itm_status <> 'OFF'
    */

    public String getRunItemAsXML(Connection con, boolean checkStatus, boolean xmlInDetails, cwPagination page)
        throws SQLException, cwSysMessage, qdbException {
            return getChildItemAsXML(con, checkStatus, false, xmlInDetails, page, "run_item_list");
        }
    public String getChildItemAsXML(Connection con, boolean checkStatus, boolean show_attendance_ind, boolean xmlInDetails, cwPagination page, String boundListXMLTag)
        throws SQLException, cwSysMessage, qdbException {
        if (boundListXMLTag==null){
            boundListXMLTag = "child_item_list";
        }
        StringBuffer result = new StringBuffer(4000);
        result.append("<item id=\"").append(this.itm_id).append("\"");
        result.append(" tcr_id=\"").append(this.itm_tcr_id).append("\"");
        result.append(" type=\"").append(this.itm_type).append("\"");
        result.append(" dummy_type=\"").append(this.itm_dummy_type).append("\"");
        result.append(" title=\"").append(dbUtils.esc4XML(this.itm_title)).append("\"");
        result.append(" create_run_ind=\"").append(this.itm_create_run_ind).append("\"");
        result.append(" blend_ind=\"").append(this.itm_blend_ind).append("\"");
        result.append(" exam_ind=\"").append(this.itm_exam_ind).append("\"");
        result.append(" ref_ind=\"").append(this.itm_ref_ind).append("\"");
        result.append(" create_session_ind=\"").append(this.itm_create_session_ind).append("\"");
        result.append(" run_ind=\"").append(this.itm_run_ind).append("\"");
        result.append(" session_ind=\"").append(this.itm_session_ind).append("\"");
        result.append(" >");
        if (this.itm_id!=0){
            result.append(getNavAsXML(con, this.itm_id));
        }
        // ind for the child
        DbItemType dbIty = new DbItemType();
        dbIty.ity_id = this.itm_type;
        dbIty.ity_owner_ent_id = this.itm_owner_ent_id;
        dbIty.ity_run_ind = this.itm_create_run_ind;
        dbIty.ity_blend_ind = this.itm_blend_ind;
        dbIty.ity_exam_ind = this.itm_exam_ind;
        dbIty.ity_ref_ind = this.itm_ref_ind;
        dbIty.ity_session_ind = this.itm_create_session_ind;
        Hashtable h_ity_inds = dbIty.getInd(con);


        result.append("<item_type_reference_data id=\"").append(this.itm_type).append("\"");
        result.append(" create_run_ind=\"").append(h_ity_inds.get("ity_create_run_ind")).append("\"");
        result.append(" create_session_ind=\"").append(h_ity_inds.get("ity_create_session_ind")).append("\"");
        result.append(" run_ind=\"").append(h_ity_inds.get("ity_run_ind")).append("\"");
        result.append(" session_ind=\"").append(h_ity_inds.get("ity_session_ind")).append("\"");
        result.append(" apply_ind=\"").append(h_ity_inds.get("ity_apply_ind")).append("\"");
        result.append(" qdb_ind=\"").append(h_ity_inds.get("ity_qdb_ind")).append("\"");
        result.append(" blend_ind=\"").append(h_ity_inds.get("ity_qdb_ind")).append("\"");
        result.append(" exam_ind=\"").append(h_ity_inds.get("ity_qdb_ind")).append("\"");
        result.append(" ref_ind=\"").append(h_ity_inds.get("ity_qdb_ind")).append("\"");
        result.append(" auto_enrol_qdb_ind=\"").append(h_ity_inds.get("ity_auto_enrol_qdb_ind")).append("\"");
        result.append(" init_life_status=\"").append(dbIty.getItemTypeInitLifeStatus(con)).append("\"");
        result.append(" has_attendance_ind=\"").append(h_ity_inds.get("ity_has_attendance_ind")).append("\" />");

        result.append("<"+ boundListXMLTag + ">");
        Vector v_child = getChildItemAsVector(con, checkStatus, false, page);
        Timestamp cur_time = cwSQL.getTime(con);
        
        // Pagination
        page.totalRec = v_child.size();
        if (page.pageSize == 0) page.pageSize = 10;
        if (page.curPage == 0) page.curPage = 1;
        if (page.pageSize*(page.curPage-1) >= page.totalRec) {
            page.curPage = page.totalRec/page.pageSize;

            if (page.totalRec%page.pageSize > 0) {
                page.curPage++;
            }
        }
        long start = page.pageSize * (page.curPage-1);
                
        for(int i=0; i<v_child.size(); i++) {
            if (i >= start && i < start+page.pageSize) {
    
                aeItem childItem = (aeItem) v_child.elementAt(i);
                if(childItem.itm_life_status == null && childItem.itm_eff_end_datetime != null) {
                    if (childItem.itm_eff_end_datetime.before(cur_time)) {
                        childItem.itm_life_status = ITM_LIFE_STATUS_COMPLETED;
                    }
                }
                if (xmlInDetails){
                    result.append(childItem.TreeViewContentAsXML(con, 0, null, null, null, null, null, show_attendance_ind));
                }else{
                    result.append(childItem.shortInfoAsXML(con));
                }
            
            }
        }
        result.append("</" + boundListXMLTag + ">");

        if (page != null) {
            result.append(page.asXML());
        }

        result.append("</item>");
        return result.toString();
    }


    private final String sql_get_child_item =
        "SELECT itm_id, itm_title, itm_code, itm_type, itm_capacity, itm_unit, itm_fee_ccy, itm_fee, itm_status, "
      + "       itm_appn_start_datetime, itm_appn_end_datetime, itm_eff_start_datetime, itm_eff_end_datetime, "
      + "       itm_create_timestamp, itm_create_usr_id, itm_upd_timestamp, itm_upd_usr_id, "
      + "       itm_owner_ent_id, itm_xml, "
      + "       itm_ext1, itm_run_ind, itm_session_ind, itm_has_attendance_ind, itm_ji_ind, itm_completion_criteria_ind, itm_apply_ind, itm_deprecated_ind, itm_life_status, "
      + "       itm_apply_method, itm_create_run_ind, itm_create_session_ind, itm_qdb_ind, itm_auto_enrol_qdb_ind, "
      + "       itm_version_code, itm_person_in_charge, itm_min_capacity, itm_cancellation_reason, "
      + "       itm_rsv_id, itm_tcr_id , itm_not_allow_waitlist_ind , itm_publish_timestamp"
      + "  FROM aeItemRelation, aeItem WHERE ire_parent_itm_id = ? AND ire_child_itm_id = itm_id ";
    private final String sql_get_run_item = sql_get_child_item ;

    private final String sql_itm_status_not_equal_off = " AND itm_status <> '" + ITM_STATUS_OFF + "'";
    private final String sql_itm_eff_end_after_now = " AND (itm_eff_end_datetime > ? OR itm_eff_end_datetime IS NULL) ";
    //private final String sql_order_by_itm_id = " ORDER BY itm_id ";
    /**
    Find out the run items of the item<BR>
    @param checkStatus used to determine if need to check itm_status <> 'OFF'
    @param checkEndDate used to determine if need to check itm_eff_end_datetime > TODAY
    @return Vector of aeItem objects of the run items
    */
    public Vector getRunItemAsVector(Connection con, boolean checkStatus, boolean checkEndDate, cwPagination page)
    //     qdbException never throw, for backward compatible
        throws SQLException, cwSysMessage, qdbException {

        Vector vtRunItem = null;
        if (getCreateRunInd(con)){
            vtRunItem = getChildItemAsVector(con, checkStatus, checkEndDate, page);
        }else{
            vtRunItem = new Vector();
        }
        return vtRunItem;
    }

    /**
    Find out the session items of the item<BR>
    @param checkStatus used to determine if need to check itm_status <> 'OFF'
    @param checkEndDate used to determine if need to check itm_eff_end_datetime > TODAY
    @return Vector of aeItem objects of the run items
    */
    public Vector getSessionItemAsVector(Connection con, boolean checkStatus, boolean checkEndDate, cwPagination page)
        throws SQLException, cwSysMessage {

        Vector vtSessionItem = null;
        if (getCreateSessionInd(con)){
            vtSessionItem = getChildItemAsVector(con, checkStatus, checkEndDate, page);
        }else{
            vtSessionItem = new Vector();
        }
        return vtSessionItem;
    }

    public Vector getChildItemAsVector(Connection con, boolean checkStatus, boolean checkEndDate, cwPagination page)
        throws SQLException, cwSysMessage {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        Vector v_child = new Vector();

        // get all the items that are runs of this item
        StringBuffer SQLBuf = new StringBuffer(1024);
        SQLBuf.append(sql_get_child_item);
        if(checkStatus) {
            SQLBuf.append(sql_itm_status_not_equal_off);
        }
        if(checkEndDate) {
            SQLBuf.append(sql_itm_eff_end_after_now);
        }

        if (page == null) {
            page = new cwPagination();
        }
        if (page.sortCol == null || page.sortCol.length() == 0) {
            page.sortCol = "itm_eff_start_datetime";
        }
        SQLBuf.append("Order by ").append(page.sortCol).append(" ");
        if (page.sortOrder == null || page.sortOrder.length() == 0) {
            page.sortOrder = "ASC";
        }
        SQLBuf.append(page.sortOrder);

        stmt = con.prepareStatement(SQLBuf.toString());
        col = 1;
        stmt.setLong(col++, itm_id);
        if(checkEndDate){
            stmt.setTimestamp(col++, cwSQL.getTime(con));
        }
        rs = stmt.executeQuery();
        Timestamp cur_time = cwSQL.getTime(con);
        // for each item, get the details except the xml
        while (rs.next()) {
            aeItem childItem = new aeItem();
            col = 1;
            childItem.itm_id                  = rs.getLong("itm_id");
            childItem.itm_title               = rs.getString("itm_title");
            childItem.itm_code                = rs.getString("itm_code");
            childItem.itm_type                = rs.getString("itm_type");
            childItem.itm_capacity            = rs.getLong("itm_capacity");
            childItem.itm_unit                = rs.getFloat("itm_unit");
            childItem.itm_fee_ccy             = rs.getString("itm_fee_ccy");
            childItem.itm_fee                 = rs.getFloat("itm_fee");
            childItem.itm_status              = rs.getString("itm_status");
            childItem.itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
            childItem.itm_appn_end_datetime   = rs.getTimestamp("itm_appn_end_datetime");
            childItem.itm_eff_start_datetime  = rs.getTimestamp("itm_eff_start_datetime");
            childItem.itm_eff_end_datetime    = rs.getTimestamp("itm_eff_end_datetime");
            if (childItem.itm_eff_start_datetime != null && cur_time.before(childItem.itm_eff_start_datetime)) {
                 childItem.itm_progress_status = ITM_NOT_STARTED;
            }
            else if (childItem.itm_eff_end_datetime != null && cur_time.after(childItem.itm_eff_end_datetime)) {
                childItem.itm_progress_status = ITM_FINISHED;
            }
            else {
                childItem.itm_progress_status = ITM_IN_PROGRESS;
            }
            
            childItem.itm_create_timestamp    = rs.getTimestamp("itm_create_timestamp");
            childItem.itm_create_usr_id       = rs.getString("itm_create_usr_id");
            childItem.itm_upd_timestamp       = rs.getTimestamp("itm_upd_timestamp");
            childItem.itm_upd_usr_id          = rs.getString("itm_upd_usr_id");
            childItem.itm_owner_ent_id        = rs.getLong("itm_owner_ent_id");
            childItem.itm_ext1                = rs.getString("itm_ext1");
            childItem.itm_run_ind             = rs.getBoolean("itm_run_ind");
            childItem.itm_session_ind         = rs.getBoolean("itm_session_ind");
            childItem.itm_apply_ind           = rs.getBoolean("itm_apply_ind");
            childItem.itm_deprecated_ind      = rs.getBoolean("itm_deprecated_ind");
            childItem.itm_life_status          = rs.getString("itm_life_status");
            childItem.itm_apply_method        = rs.getString("itm_apply_method");
            childItem.itm_create_run_ind      = rs.getBoolean("itm_create_run_ind");
            childItem.itm_create_session_ind  = rs.getBoolean("itm_create_session_ind");
            childItem.itm_has_attendance_ind  = rs.getBoolean("itm_has_attendance_ind");
            childItem.itm_ji_ind  = rs.getBoolean("itm_ji_ind");
            childItem.itm_completion_criteria_ind = rs.getBoolean("itm_completion_criteria_ind");
            childItem.itm_qdb_ind             = rs.getBoolean("itm_qdb_ind");
            childItem.itm_auto_enrol_qdb_ind  = rs.getBoolean("itm_auto_enrol_qdb_ind");
            childItem.itm_version_code        = rs.getString("itm_version_code");
            childItem.itm_person_in_charge    = rs.getString("itm_person_in_charge");
            childItem.itm_cancellation_reason = rs.getString("itm_cancellation_reason");
            childItem.itm_min_capacity        = rs.getLong("itm_min_capacity");
            childItem.itm_rsv_id              = rs.getLong("itm_rsv_id");
            childItem.itm_tcr_id              = rs.getLong("itm_tcr_id");
            childItem.itm_xml                 = cwSQL.getClobValue(rs, "itm_xml");
            childItem.itm_not_allow_waitlist_ind  = rs.getBoolean("itm_not_allow_waitlist_ind");
            childItem.itm_publish_timestamp		= rs.getTimestamp("itm_publish_timestamp");
            v_child.addElement(childItem);
        }
        stmt.close();
        return v_child;
    }

	public Vector getResponseChildItemAsVector(Connection con, long usr_ent_id, String rol_ext_id, boolean checkStatus, cwPagination page)
		throws SQLException, cwSysMessage {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int col = 0;
		Vector v_child = new Vector();

		// get all the items that are runs of this item
		StringBuffer SQLBuf = new StringBuffer(1024);
		SQLBuf.append(sql_get_child_item);
		if(checkStatus) {
			SQLBuf.append(sql_itm_status_not_equal_off);
		}
		
		SQLBuf.append(" AND itm_id in (Select iac_itm_id from aeItemAccess where iac_ent_id = ? and iac_access_type = ? and iac_access_id = ? )");

		if (page == null) {
			page = new cwPagination();
		}
		if (page.sortCol == null || page.sortCol.length() == 0) {
			page.sortCol = "itm_eff_start_datetime";
		}
		SQLBuf.append("Order by ").append(page.sortCol).append(" ");
		if (page.sortOrder == null || page.sortOrder.length() == 0) {
			page.sortOrder = "ASC";
		}
		SQLBuf.append(page.sortOrder);

		stmt = con.prepareStatement(SQLBuf.toString());
		col = 1;
		stmt.setLong(col++, itm_id);
		stmt.setLong(col++, usr_ent_id);
		stmt.setString(col++, aeItemAccess.ACCESS_TYPE_ROLE);
		stmt.setString(col++, rol_ext_id);
		rs = stmt.executeQuery();
		// for each item, get the details except the xml
		while (rs.next()) {
			aeItem childItem = new aeItem();
			col = 1;
			childItem.itm_id                  = rs.getLong("itm_id");
			childItem.itm_title               = rs.getString("itm_title");
			childItem.itm_code                = rs.getString("itm_code");
			childItem.itm_type                = rs.getString("itm_type");
			childItem.itm_capacity            = rs.getLong("itm_capacity");
			childItem.itm_unit                = rs.getFloat("itm_unit");
			childItem.itm_fee_ccy             = rs.getString("itm_fee_ccy");
			childItem.itm_fee                 = rs.getFloat("itm_fee");
			childItem.itm_status              = rs.getString("itm_status");
			childItem.itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
			childItem.itm_appn_end_datetime   = rs.getTimestamp("itm_appn_end_datetime");
			childItem.itm_eff_start_datetime  = rs.getTimestamp("itm_eff_start_datetime");
			childItem.itm_eff_end_datetime    = rs.getTimestamp("itm_eff_end_datetime");
			childItem.itm_create_timestamp    = rs.getTimestamp("itm_create_timestamp");
			childItem.itm_create_usr_id       = rs.getString("itm_create_usr_id");
			childItem.itm_upd_timestamp       = rs.getTimestamp("itm_upd_timestamp");
			childItem.itm_upd_usr_id          = rs.getString("itm_upd_usr_id");
			childItem.itm_owner_ent_id        = rs.getLong("itm_owner_ent_id");
			childItem.itm_ext1                = rs.getString("itm_ext1");
			childItem.itm_run_ind             = rs.getBoolean("itm_run_ind");
			childItem.itm_session_ind         = rs.getBoolean("itm_session_ind");
			childItem.itm_apply_ind           = rs.getBoolean("itm_apply_ind");
			childItem.itm_deprecated_ind      = rs.getBoolean("itm_deprecated_ind");
			childItem.itm_life_status          = rs.getString("itm_life_status");
			childItem.itm_apply_method        = rs.getString("itm_apply_method");
			childItem.itm_create_run_ind      = rs.getBoolean("itm_create_run_ind");
			childItem.itm_create_session_ind  = rs.getBoolean("itm_create_session_ind");
			childItem.itm_has_attendance_ind  = rs.getBoolean("itm_has_attendance_ind");
			childItem.itm_ji_ind  = rs.getBoolean("itm_ji_ind");
			childItem.itm_completion_criteria_ind = rs.getBoolean("itm_completion_criteria_ind");
			childItem.itm_qdb_ind             = rs.getBoolean("itm_qdb_ind");
			childItem.itm_auto_enrol_qdb_ind  = rs.getBoolean("itm_auto_enrol_qdb_ind");
			childItem.itm_version_code        = rs.getString("itm_version_code");
			childItem.itm_person_in_charge    = rs.getString("itm_person_in_charge");
			childItem.itm_cancellation_reason = rs.getString("itm_cancellation_reason");
			childItem.itm_min_capacity        = rs.getLong("itm_min_capacity");
			childItem.itm_rsv_id              = rs.getLong("itm_rsv_id");
            childItem.itm_tcr_id              = rs.getLong("itm_tcr_id");
			childItem.itm_xml                 = cwSQL.getClobValue(rs, "itm_xml");
			childItem.itm_not_allow_waitlist_ind  = rs.getBoolean("itm_not_allow_waitlist_ind");
			v_child.addElement(childItem);
		}
		stmt.close();
		return v_child;
	}


    /**
     * get all the tree nodes this item has been assigned to
     * and find out the full nature path of the nodes
     * 2001.07.23 wai
     */
    private final String sql_get_assigned_node
        = " SELECT tnd_id, cat_public_ind "
        + " FROM aeTreeNode, aeCatalog "
        + " WHERE tnd_type = ? "
        + " AND tnd_itm_id = ? "
        + " AND tnd_cat_id = cat_id "
        + " ORDER BY tnd_create_timestamp ";
    private final String sql_get_assigned_node_2
        = " SELECT distinct tnd_id, cat_public_ind, tnd_create_timestamp "
        + " FROM aeTreeNode, aeCatalog, aeCatalogAccess "
        + " WHERE tnd_type = ? "
        + " AND tnd_itm_id = ? "
        + " AND tnd_cat_id = cat_id "
        + " AND cac_cat_id = cat_id "
        + " AND $ "
        + " ORDER BY tnd_create_timestamp ";

    public StringBuffer getAssignedNodeAsXML(Connection con) throws SQLException, cwSysMessage {
        return getAssignedNodeAsXML(con, 0);
    }
    public StringBuffer getAssignedNodeAsXML(Connection con, long usr_ent_id) throws SQLException, cwSysMessage {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        StringBuffer result = new StringBuffer(4000);
        result.append("<catalog>");
        result.append("<node_list>");

        // get all the nodes that are pointing to this item
        if(usr_ent_id == 0) {
            stmt = con.prepareStatement(sql_get_assigned_node);
            col = 1;
            stmt.setString(col++, aeTreeNode.TND_TYPE_ITEM);
            stmt.setLong(col++, itm_id);
        } else {
            StringBuffer SQLBuf = new StringBuffer(sql_get_assigned_node_2);
            int start = sql_get_assigned_node_2.indexOf('$');
            SQLBuf = SQLBuf.replace(start, start+1, "(cac_ent_id in ("+dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG)+") or cac_ent_id ="+usr_ent_id+")");
            stmt = con.prepareStatement(SQLBuf.toString());
            col = 1;
            stmt.setString(col++, aeTreeNode.TND_TYPE_ITEM);
            stmt.setLong(col++, itm_id);
        }
        rs = stmt.executeQuery();
        // for each node, get the details and navigation
        while (rs.next()) {
            aeTreeNode node = new aeTreeNode();
            col = 1;
            node.tnd_id = rs.getLong(col++);
            node.public_ind = rs.getBoolean(col++);
            node.get(con);
            result.append(node.contentAsXML(con));
        }
        stmt.close();

        result.append("</node_list>");
        result.append("</catalog>");
        return result;
    }

    /**
     * get all access control rules of this item
     * 2001.07.25 wai
     */
    public StringBuffer getAccessAsXML(Connection con, long pItemId) throws SQLException, cwException {

        StringBuffer result = new StringBuffer(4000);
        aeItemAccess itmAcc = new aeItemAccess();
        itmAcc.iac_itm_id = this.itm_id;
        String allRoleDesc = dbUtils.getAllRoleAsXML(con,"role_list", this.itm_owner_ent_id);
        if(this.itm_run_ind) {
            result.append("<run_item_access>");
            result.append(allRoleDesc);
            result.append(itmAcc.getChildAssignedRoleList(con, pItemId));
            result.append("</run_item_access>");
        }else if (this.itm_session_ind){
            result.append("<child_item_access>");
            result.append(allRoleDesc);
            result.append(itmAcc.getChildAssignedRoleList(con, pItemId));
            result.append("</child_item_access>");
        }
		result.append("<item_access>");
		result.append(allRoleDesc);
		result.append(itmAcc.getAssignedRoleList(con));
		result.append("</item_access>");

        return result;
    }

    /**
     * attach this item to the specified parent item
     * 2001.07.31 wai
     */
    public void attachParent(Connection con, long inParentID) throws SQLException {
        // check parent item id exist and accept runs?

        aeItemRelation rel = new aeItemRelation();

        rel.ire_parent_itm_id    = inParentID;
        rel.ire_child_itm_id     = this.itm_id;
        rel.ire_create_timestamp = this.itm_create_timestamp;
        rel.ire_create_usr_id    = this.itm_create_usr_id;

        rel.ins(con);
    }

    /**
     * get all the resources related to this item
     * 2001.08.01 wai
     */
    private StringBuffer getResourcesAsXML(Connection con) throws SQLException, cwException {
        StringBuffer result = new StringBuffer();
        result.append(com.cw.wizbank.content.Survey.getCleanItmModLstAsXML(con, this.itm_id, 0, "TNA"));
        return result;
    }

    private String getOnlineContentAsXML(Connection con)
        throws SQLException, cwException, cwSysMessage{
        try{
            StringBuffer xmlBuf = new StringBuffer(512);
            getItmContentInfo(con);
            long cos_id = getResId(con);
    //        dbCourse cos = new dbCourse();
    //        cos.cos_res_id = getResId(con);
    //        cos.res_id = cos.cos_res_id;
    //        try {
    //            cos.get(con);
    //        }
    //        catch(qdbException e) {
    //            throw new cwException(e.getMessage());
    //        }
            xmlBuf.append("<cos_eff_start_datetime>");
            xmlBuf.append("<course id=\"").append(cos_id).append("\"");
            xmlBuf.append(" eff_start_datetime=\"").append(itm_content_eff_start_datetime).append("\"/>");
            xmlBuf.append("</cos_eff_start_datetime>");
            xmlBuf.append("<cos_eff_end_datetime>");
            xmlBuf.append("<course id=\"").append(cos_id).append("\"");
            xmlBuf.append(" eff_end_datetime=\"").append(itm_content_eff_end_datetime).append("\"/>");
            xmlBuf.append("</cos_eff_end_datetime>");
            xmlBuf.append("<cos_desc>");
            xmlBuf.append("<course id=\"").append(cos_id).append("\"");
            xmlBuf.append(" desc=\"").append(cwUtils.esc4XML(cwUtils.escNull(dbResource.getResDesc(con, cos_id)))).append("\"/>");
            xmlBuf.append("</cos_desc>");
            return xmlBuf.toString();
        }catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
    }

    /**
    Insert this item as an new version
    */
    public void insAsNewVersion(Connection con, long old_itm_id, Timestamp lastUpdTime,
                                Timestamp curTime, String usr_id, String uploadPath,
                                dbCourse cos, loginProfile prof,
                                Vector vColName, Vector vColType, Vector vColValue,
                                Vector vClobColName, Vector vClobColValue)
                                throws SQLException, qdbException, cwException, cwSysMessage {

        if(curTime == null) {
            curTime = cwSQL.getTime(con);
        }
        aeItem oldItem = new aeItem();
        oldItem.itm_id = old_itm_id;
        oldItem.getItem(con);

        // check for create item restriction (2002.03.26 kawai)
        if (isActiveItemCntExceedLimit(con, oldItem.itm_owner_ent_id)) {
            throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
        }

        if(oldItem.itm_run_ind) {
            throw new cwSysMessage(ITM_RUN_NO_NEW_VERSION );
        }
        if(oldItem.itm_session_ind) {
            throw new cwSysMessage(ITM_SESSION_NO_NEW_VERSION );
        }

        if(!oldItem.isItemCompleted(con, curTime)) {
            throw new cwSysMessage(ITM_NOT_COMPLETE);
        }

//        !!! NEED TO CONSIDER UNIQUE CODE ISSUE FOR NEW VERSION

//        if (!isUniqueTitle(con, old_itm_id, parent_itm_id, itm_type, prof.root_ent_id, itm_run_ind, itm_session_ind, itm_title)) {
//            throw new cwSysMessage(ITM_TITLE_EXIST_MSG);
//        }

        // turn on the deprecated indicator of the original item only if it is not turned on yet
        if (oldItem.itm_deprecated_ind || lastUpdTime == null || !oldItem.itm_upd_timestamp.equals(lastUpdTime))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        else {
            oldItem.con = con;
            oldItem.setDeprecatedInd(true, curTime, usr_id);
        }
        //update tree nodes cnt and on_cnt that attached to the old item
        oldItem.cascadeUpdTndCnt(con, -1);
        //insert the new version
        //this.itm_deprecated_ind = false;
        this.itm_upd_timestamp = curTime;
        this.itm_create_timestamp = curTime;
        this.itm_create_usr_id = usr_id;
        this.itm_upd_usr_id = usr_id;
        this.itm_owner_ent_id = oldItem.itm_owner_ent_id;
        this.itm_code = oldItem.itm_code;
        if (!vColName.contains("itm_code")) {
	        vColName.addElement("itm_code");
	        vColType.addElement(DbTable.COL_TYPE_STRING);
	        vColValue.addElement(this.itm_code);
	}
        this.itm_life_status = null;
        if (!vColName.contains("itm_life_status")) {
	        vColName.addElement("itm_life_status");
	        vColType.addElement(DbTable.COL_TYPE_STRING);
	        vColValue.addElement(this.itm_life_status);
	}
        this.insItem(con, vColName, vColType, vColValue);
        //copy templates
        ViewItemTemplate newTplUsage = new ViewItemTemplate();
        newTplUsage.itemId = this.itm_id;
        ViewItemTemplate orgTplUsage = new ViewItemTemplate();
        orgTplUsage.itemId = oldItem.itm_id;
        Hashtable orgTplList = orgTplUsage.getUsage(con);
        Enumeration orgTplTypeList = orgTplList.keys();
        while (orgTplTypeList.hasMoreElements()) {
            String tplType = (String)orgTplTypeList.nextElement();
            Long tplID = (Long)orgTplList.get(tplType);
            newTplUsage.templateId = tplID.longValue();
            newTplUsage.templateType = tplType;
            newTplUsage.insItemTemplate(con, this.itm_create_usr_id, this.itm_create_timestamp);
        }
        // for other clob columns
        updOterClobColumns(con, vClobColName, vClobColValue);
        //online content
        if(this.itm_qdb_ind) {
            if(cos != null) {
                cos.cos_itm_id = this.itm_id;
                cos.res_create_date = this.itm_create_timestamp;
                cos.res_upd_date = this.itm_upd_timestamp;
                cos.res_status = this.itm_status;
            }
            else {
                cos = oldItem.getWZBCourse(con);
                cos.cos_itm_id = this.itm_id;
            }
            cos.cos_structure_xml = null;
            cos.ins(con, prof);
        }
        // copy uploaded files
        String orgDirPath = uploadPath + dbUtils.SLASH + oldItem.itm_id;
        String newDirPath = uploadPath + dbUtils.SLASH + this.itm_id;
        dbUtils.copyDir(orgDirPath, newDirPath);
        return;
    }


    /**
    Create new version of this item.
    */
    private aeItem newVersion(Connection con, Timestamp lastUpdTime,
                                Timestamp curTime, String usr_id,
                                String uploadPath, loginProfile prof)
                                throws SQLException, cwSysMessage, qdbException, cwException {

        // check for create item restriction (2002.03.26 kawai)
        if (isActiveItemCntExceedLimit(con, this.itm_owner_ent_id)) {
            throw new cwSysMessage(acSite.MSG_LIC_MAX_ACT_ITEM_EXCEED);
        }

        if(curTime == null) {
            curTime = cwSQL.getTime(con);
        }

        //get the item details from database
        if(this.itm_status == null) {
            getItem(con);
        }
        // turn on the deprecated indicator of the original item only if it is not turned on yet
        if (this.itm_deprecated_ind || !this.itm_upd_timestamp.equals(lastUpdTime))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        else {
            this.con = con;
            setDeprecatedInd(true, curTime, this.itm_upd_usr_id);
        }
        //create new version of this item
        aeItem newItem = new aeItem(this);
        newItem.itm_deprecated_ind = false;
        newItem.itm_upd_timestamp = curTime;
        newItem.itm_create_timestamp = curTime;
        newItem.itm_create_usr_id = usr_id;
        newItem.itm_upd_usr_id = usr_id;
        newItem.insItem(con);

        // copy item template usage
        /*
        aeItemTemplate newTplUsage = new aeItemTemplate(con, newItem.itm_id);
        aeItemTemplate orgTplUsage = new aeItemTemplate(con, this.itm_id);
        Hashtable orgTplList = orgTplUsage.getUsage();
        */
        ViewItemTemplate newTplUsage = new ViewItemTemplate();
        newTplUsage.itemId = newItem.itm_id;
        ViewItemTemplate orgTplUsage = new ViewItemTemplate();
        orgTplUsage.itemId = this.itm_id;
        Hashtable orgTplList = orgTplUsage.getUsage(con);
        Enumeration orgTplTypeList = orgTplList.keys();
        while (orgTplTypeList.hasMoreElements()) {
            String tplType = (String)orgTplTypeList.nextElement();
            Long tplID = (Long)orgTplList.get(tplType);
            newTplUsage.templateId = tplID.longValue();
            newTplUsage.templateType = tplType;
            newTplUsage.insItemTemplate(con, newItem.itm_create_usr_id, newItem.itm_create_timestamp);
        }

        // copy tree node assignment
        Vector v = getAssignedParentNodes(con);
        aeTreeNode node = new aeTreeNode();
        node.tnd_itm_id = newItem.itm_id;
        for(int i=0; i<v.size(); i++) {
            node.tnd_parent_tnd_id = ((Long)v.elementAt(i)).longValue();
            node.ins(con, this.itm_owner_ent_id, this.itm_create_usr_id);
        }

        //competency
        DbItemCompetency dbItc = new DbItemCompetency();
        dbItc.itc_itm_id = this.itm_id;
        Hashtable h_skill = dbItc.getByItemId(con);
        Enumeration enumeration = h_skill.keys();
        Long tempL;
        dbItc.itc_itm_id = newItem.itm_id;
        while( enumeration.hasMoreElements() ) {
            tempL = (Long)enumeration.nextElement();
            dbItc.itc_skl_skb_id = tempL.longValue();
            dbItc.itc_skl_level = ((Long)h_skill.get(tempL)).longValue();
            dbItc.itc_create_usr_id = newItem.itm_create_usr_id;
            dbItc.ins(con);
        }

        //MOTE
        if(this.itm_imd_id > 0) {
                Mote mote = new Mote();
                newItem.itm_imd_id = mote.cloneMoteDefaultNMote(con, this.itm_imd_id,
                                            this.itm_type, newItem.itm_create_usr_id);
                newItem.updItemMoteId(con);
        }

        //online content
        dbCourse cos = null;
        if(this.itm_qdb_ind) {
            cos = getWZBCourse(con);
            cos.cos_itm_id = newItem.itm_id;
            cos.res_upd_user = prof.usr_id;
            cos.res_upd_date = curTime;
            cos.res_create_date = curTime;
            cos.cos_structure_xml = null;
            cos.ins(con, prof);
        }
        //item access
        Vector v_iac = aeItemAccess.getItemAccessByItem(con, this.itm_id);
        aeItemAccess iac;
        for(int i=0; i<v_iac.size(); i++) {
            iac = (aeItemAccess) v_iac.elementAt(i);
            iac.iac_itm_id = newItem.itm_id;
            if(this.itm_qdb_ind) {
                iac.ins(con, cos.cos_res_id);
            }
            else {
                iac.ins(con);
            }
        }

        // copy uploaded files
        String orgDirPath = uploadPath + dbUtils.SLASH + this.itm_id;
        String newDirPath = uploadPath + dbUtils.SLASH + newItem.itm_id;
        dbUtils.copyDir(orgDirPath, newDirPath);
       
        return newItem;
    }

    private final String sql_get_assigned_parent_node =
        "SELECT tnd_parent_tnd_id FROM aeTreeNode WHERE tnd_type = ? AND tnd_itm_id = ? ORDER BY tnd_create_timestamp ";

    private Vector getAssignedParentNodes(Connection con) throws SQLException {

        Vector v = new Vector();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        // get all the nodes that are pointing to the original item
        stmt = con.prepareStatement(sql_get_assigned_parent_node);
        col = 1;
        stmt.setString(col++, aeTreeNode.TND_TYPE_ITEM);
        stmt.setLong(col++, this.itm_id);
        rs = stmt.executeQuery();
        while (rs.next()) {
            col = 1;
            v.addElement(new Long(rs.getLong("tnd_parent_tnd_id")));
        }
        stmt.close();
        return v;
    }


    /**
     * create a new version of an existing item
     * 2001.08.06 wai
     */
     /*
    public long insAsNewVersion(Connection con, long orgItemID, Timestamp newTime, Timestamp lastUpdTime) throws SQLException, cwSysMessage {
        // check access control
        // handle for different types of item
        // handle switches
        // handle all indicators
        aeItem org = new aeItem(con);
        org.itm_id = orgItemID;
        org.getItem();

        // turn on the deprecated indicator of the original item only if it is not turned on yet
        if (!org.itm_deprecated_ind && !org.itm_upd_timestamp.equals(lastUpdTime))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        else
            org.setDeprecatedInd(true, newTime, this.itm_upd_usr_id);

        // create this new item
        this.itm_create_timestamp = newTime;
        this.itm_upd_timestamp    = newTime;
        this.insItem(con);

        // copy item template usage
        ViewItemTemplate newTplUsage = new ViewItemTemplate();
        newTplUsage.itemId = this.itm_id;
        ViewItemTemplate orgTplUsage = new ViewItemTemplate();
        orgTplUsage.itemId = org.itm_id;
        Hashtable orgTplList = orgTplUsage.getUsage(con);
        Enumeration orgTplTypeList = orgTplList.keys();
        while (orgTplTypeList.hasMoreElements()) {
            String tplType = (String)orgTplTypeList.nextElement();
            Long tplID = (Long)orgTplList.get(tplType);
            newTplUsage.templateId = tplID.longValue();
            newTplUsage.templateType = tplType;
            newTplUsage.insItemTemplate(con, this.itm_create_usr_id, this.itm_create_timestamp);
        }

        // copy tree node assignment
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        // get all the nodes that are pointing to the original item
        stmt = con.prepareStatement(sql_get_assigned_node);
        col = 1;
        stmt.setString(col++, aeTreeNode.TND_TYPE_ITEM);
        stmt.setLong(col++, org.itm_id);
        rs = stmt.executeQuery();
        // for each node, assign the new item to it
        aeTreeNode node = new aeTreeNode();
        node.tnd_itm_id = this.itm_id;
        while (rs.next()) {
            col = 1;
            node.tnd_parent_tnd_id = rs.getLong(col++);
            node.ins(con, this.itm_owner_ent_id, this.itm_create_usr_id);
        }
        stmt.close();

        return this.itm_id;
    }
    */
    /**
     * set the deprecated indicator of this item to true or false
     * 2001.08.06 wai
     */
    private final String sql_upd_deprecated =
        "UPDATE aeItem "
      + "SET    itm_deprecated_ind = ?, itm_upd_timestamp = ?, itm_upd_usr_id = ? "
      + "WHERE  itm_id = ? ";
    private final String sql_upd_deprecated_sub =
        "AND itm_upd_timestamp = ? ";
    private void setDeprecatedInd(boolean ind, Timestamp newTime, String newUserID) throws SQLException, cwSysMessage {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int col = 0;
        int rc = 0;

        // check timestamp only if supplied
        StringBuffer sql = new StringBuffer(sql_upd_deprecated);
        if (this.itm_upd_timestamp != null) sql.append(sql_upd_deprecated_sub);

        // update the deprecated indicator
        stmt = con.prepareStatement(sql.toString());
        col = 1;
        stmt.setBoolean(col++, ind);
        stmt.setTimestamp(col++, newTime);
        stmt.setString(col++, newUserID);
        stmt.setLong(col++, this.itm_id);
        if (this.itm_upd_timestamp != null) stmt.setTimestamp(col++, this.itm_upd_timestamp);
        rc = stmt.executeUpdate();
        stmt.close();

        // if update failed, throw error message
        if (rc != 1) throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

        // update the values of this item
        this.itm_deprecated_ind = ind;
        this.itm_upd_timestamp  = newTime;
        this.itm_upd_usr_id     = newUserID;
    }
    /**
    Get all the item type title as xml in organization
    @return XML that contain all the ity_title_xml in the organization
    */
    public static String getAllItemTypeTitleInOrg(Connection con, long owner_ent_id)
    throws SQLException {

    StringBuffer xmlBuf = new StringBuffer(512);
    StringBuffer metaBuf = new StringBuffer(512);
    DbItemType[] dbItys = DbItemType.getAllItemTypeInOrg(con, owner_ent_id);

    xmlBuf.append("<item_type_list>");
    metaBuf.append("<item_type_meta_list>");
    for(int i=0; i<dbItys.length; i++) {
        xmlBuf.append(dbItys[i].ity_title_xml);
        metaBuf.append(dbItys[i].getMetaDataAsXML());
    }
    metaBuf.append("</item_type_meta_list>");
    xmlBuf.append("</item_type_list>");
    StringBuffer resultBuf = new StringBuffer(1024);
    resultBuf.append(xmlBuf.toString()).append(metaBuf.toString());
    return resultBuf.toString();
}
    
    
    public static String getAllItemTypeTitleInOrg(Connection con, long owner_ent_id, String training_type)
        throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        StringBuffer metaBuf = new StringBuffer(512);
        DbItemType[] dbItys = DbItemType.getAllItemTypeInOrg(con, owner_ent_id);

        xmlBuf.append("<item_type_list>");
        metaBuf.append("<item_type_meta_list>");
        String pre_training_type="";
        for(int i=0; i<dbItys.length; i++) {
        	String cur_ity=dbItys[i].item_type;
        	String cur_training_type = convertTrainType(cur_ity);
        	if(cur_training_type.equals(training_type)||"ALL".equalsIgnoreCase(training_type)){
	        	if(!cur_training_type.equals(pre_training_type)){
	        		xmlBuf.append("<item_type id=\"").append(cur_training_type).append("\" title=\"").append("ALL"+aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER+cur_training_type).append("\" group_ind=\"true").append("\"/>");
	        	}
	            xmlBuf.append("<item_type id=\"").append(cur_ity).append("\" title=\"").append(cur_ity).append("\"/>");
        	}
            metaBuf.append(dbItys[i].getMetaDataAsXML()); 	
            pre_training_type=cur_training_type;
        }
        metaBuf.append("</item_type_meta_list>");
        xmlBuf.append("</item_type_list>");
        StringBuffer resultBuf = new StringBuffer(1024);
        resultBuf.append(xmlBuf.toString()).append(metaBuf.toString());
        return resultBuf.toString();
    }
    
    public static String getItemTypeTitleByTrainType(Connection con, long owner_ent_id, String[] training_type)
	    throws SQLException {
	
	    StringBuffer xmlBuf = new StringBuffer(512);
	    StringBuffer metaBuf = new StringBuffer(512);
	    DbItemType[] dbItys = DbItemType.getAllItemTypeInOrg(con, owner_ent_id);
	
	    xmlBuf.append("<item_type_list>");
	    metaBuf.append("<item_type_meta_list>");
	    String pre_training_type = "";
	    String cur_ity = null;
	    String cur_training_type = null;
	    String train_type = null;
	    int i, j;
	    for (j = 0; j < training_type.length; j++) {
	    	train_type = training_type[j];
		    for(i = 0; i < dbItys.length; i++) {
		    	cur_ity=dbItys[i].item_type;
		    	cur_training_type = convertTrainType(cur_ity);
		    	if(cur_training_type.equals(train_type)){
		        	if(!cur_training_type.equals(pre_training_type)){
		        		xmlBuf.append("<item_type id=\"").append(cur_training_type).append("\" title=\"").append("ALL"+aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER+cur_training_type).append("\" group_ind=\"true").append("\"/>");
		        	}
		            xmlBuf.append("<item_type id=\"").append(cur_ity).append("\" title=\"").append(cur_ity).append("\"/>");
		    	}
		        metaBuf.append(dbItys[i].getMetaDataAsXML()); 	
		        pre_training_type=cur_training_type;
		    }
	    }
	    metaBuf.append("</item_type_meta_list>");
	    xmlBuf.append("</item_type_list>");
	    StringBuffer resultBuf = new StringBuffer(1024);
	    resultBuf.append(xmlBuf.toString()).append(metaBuf.toString());
	    return resultBuf.toString();
	}

    /**
    Get applicable item type title as xml in organization
    @return XML that contain ity_title_xml
    */
    public static String getApplicableItemTypeTitleInOrg(Connection con, long owner_ent_id)
        throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        //StringBuffer metaBuf = new StringBuffer(512);
        DbItemType[] dbItys = DbItemType.getApplicableItemTypeInOrg(con, owner_ent_id);

        xmlBuf.append("<item_type_list>");
        //metaBuf.append("<item_type_meta_list>");
        for(int i=0; i<dbItys.length; i++) {
            xmlBuf.append(dbItys[i].ity_title_xml);
            //metaBuf.append(dbItys[i].getMetaDataAsXML());
        }
        //metaBuf.append("</item_type_meta_list>");
        xmlBuf.append("</item_type_list>");
        //StringBuffer resultBuf = new StringBuffer(1024);
        //resultBuf.append(xmlBuf.toString()).append(metaBuf.toString());
        return xmlBuf.toString();
    }

    /**
    Get has qdb item type title as xml in organization
    @return XML that contain ity_title_xml
    */
    public static String getHasQdbItemTypeTitleInOrg(Connection con, long owner_ent_id)
        throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        //StringBuffer metaBuf = new StringBuffer(512);
        DbItemType[] dbItys = DbItemType.getHasQdbItemTypeInOrg(con, owner_ent_id);

        xmlBuf.append("<item_type_list>");
        for(int i=0; i<dbItys.length; i++) {
        	if(dbItys[i].ity_title_xml.indexOf("EXAM") == -1 && dbItys[i].ity_title_xml.indexOf("AUDIOVIDEO") == -1){
        		xmlBuf.append(dbItys[i].ity_title_xml);
        	}
        }
        xmlBuf.append("</item_type_list>");
        return xmlBuf.toString();
    }

    /**
    Get one item type title as xml in organization
    @return XML of ity_title_xml
    */
    public String getItemTypeTitle(Connection con)
        throws SQLException {

        DbItemType dbIty = new DbItemType();
        dbIty.ity_id = this.itm_type;
        dbIty.ity_owner_ent_id = this.itm_owner_ent_id;
        dbIty.ity_run_ind = this.itm_run_ind;
        dbIty.ity_session_ind = this.itm_session_ind;
        dbIty.ity_blend_ind = this.itm_blend_ind;
        dbIty.ity_exam_ind = this.itm_exam_ind;
        dbIty.ity_ref_ind = this.itm_ref_ind;
        dbIty.setItyTypeByDummyType(this.itm_dummy_type);
        dbIty.getItemType(con);
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append(dbIty.ity_title_xml);
        xmlBuf.append(dbIty.getMetaDataAsXML());
        return xmlBuf.toString();
    }

    /**
    Attach this item tree nodes<BR>
    Pre-define variable:
    <ul>
    <li>itm_id</li>
    </ul>
    */
    public void attachTreeNodes(Connection con, long[] tnd_ids, String usr_id, long owner_ent_id)
        throws SQLException, cwSysMessage, qdbException {
        attachTreeNodes(con, tnd_ids, null, usr_id, 0, owner_ent_id);
    }

    public void attachTreeNodes(Connection con, long[] tnd_ids, String[] tnd_title_lst, String usr_id, long usr_ent_id, long owner_ent_id)
        throws SQLException, cwSysMessage, qdbException {
//System.out.println("attach node = " + owner_ent_id);
        String sql_tnd_id_lst = aeUtils.prepareSQLList(tnd_ids);
        aeTreeNode tnd = new aeTreeNode();
        tnd.tnd_itm_id = this.itm_id;


        //remove all the ITEM node not in the list
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append("Select distinct tnd_parent_tnd_id, tnd_id, tnd_upd_timestamp ");
        SQLBuf.append("From aeTreeNode ");
        if(usr_ent_id > 0) {
            SQLBuf.append(", aeCatalogAccess ");
        }
        SQLBuf.append("Where tnd_type = ? ");
        SQLBuf.append("And tnd_itm_id = ? ");
        SQLBuf.append("And tnd_parent_tnd_id not in ").append(sql_tnd_id_lst);
        if(usr_ent_id > 0) {
            SQLBuf.append("And tnd_cat_id = cac_cat_id ");
            SQLBuf.append("And (cac_ent_id in ");
            SQLBuf.append("(").append(dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG)).append(") or cac_ent_id =").append(usr_ent_id).append(")");
        }
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setString(1, aeTreeNode.TND_TYPE_ITEM);
        stmt.setLong(2, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            tnd.tnd_id = rs.getLong("tnd_id");
            tnd.tnd_parent_tnd_id = rs.getLong("tnd_parent_tnd_id");
            tnd.tnd_upd_timestamp = rs.getTimestamp("tnd_upd_timestamp");
            aeTreeNodeRelation tnr= new aeTreeNodeRelation();
            tnr.delTnr(con, aeTreeNodeRelation.TNR_TYPE_ITEM_PARENT_TND,  tnd.tnd_id);
            tnd.del(con, 0, tnd.tnd_upd_timestamp);
        }
        stmt.close();

        //insert new item nodes
        //insItmNode() will prevent inserting duplicating item
        for(int i=0; i<tnd_ids.length; i++) {
            tnd.tnd_parent_tnd_id = tnd_ids[i];
            aeTreeNode parentTnd = new aeTreeNode();
            parentTnd.tnd_id = tnd_ids[i];
            if( tnd_title_lst != null && tnd_title_lst.length > i) {
				parentTnd.tnd_title = tnd_title_lst[i];
            }
            tnd.tnd_cat_id = parentTnd.getCatalogId(con);
            this.tnd_id=tnd.insItmNode(con, owner_ent_id, usr_id); //owner_ent_id is retired
            aeTreeNodeRelation tnr= new aeTreeNodeRelation();
            tnr.insTnr(con, usr_id, aeTreeNodeRelation.TNR_TYPE_ITEM_PARENT_TND, this.tnd_id, tnd.tnd_parent_tnd_id);
        }
        return;
    }

    /**
    Save item_access of this item
    @param iac_id_lst An array of item access ent id,
    e.g. {"CMAN~3~2","PCON~123~456"}
    the 1st element in each list is the role ext id
    */
    public void saveItemAccess(Connection con, String[] iac_id_lst, long root_ent_id, boolean tc_enabled)
        throws SQLException, cwException, cwSysMessage {
        aeItemAccess iac = new aeItemAccess();
        iac.iac_itm_id = this.itm_id;
        String rolExtId;
        long[] entIds;
        String delimiter = "~";
        for(int i=0; i<iac_id_lst.length; i++) {
            String in = iac_id_lst[i];
            Vector q = new Vector();
            int pos = 0;
            pos = in.indexOf(delimiter);
            if(pos < 0) {
                rolExtId = in;
                entIds = new long[0];
            }
            else {
                rolExtId = in.substring(0,pos);
                in = in.substring(pos + delimiter.length(), in.length());
                entIds = cwUtils.splitToLong(in, delimiter);
            }
            iac.iac_access_id = rolExtId;
            //checking the tcr_id is the role tc??????
            if(tc_enabled) {
//            	AcTrainingCenter acTc = new AcTrainingCenter(con);
//            	if(rolExtId != null &&  entIds.length > 0 && rolExtId.startsWith(AccessControlWZB.ROL_STE_UID_TADM)) {
//            		if(!acTc.hasTaInTc(root_ent_id, rolExtId, entIds, this.itm_tcr_id)) {
//            			throw new cwSysMessage(dbRegUser.USR_NOT_IS_TC_TA);
//            		}
//            	}
//            	else if (rolExtId != null && entIds.length > 0 && rolExtId.startsWith(AccessControlWZB.ROL_STE_UID_INST)) {
//            		if(!acTc.hasInstrInTc(root_ent_id, rolExtId, entIds, this.itm_tcr_id)) {
//            			throw new cwSysMessage(dbRegUser.USR_NOT_IS_TC_INSTR);
//            		}            		
//            	}
            }
            iac.saveByRole(con, entIds, this.itm_qdb_ind);
        }
        return;
    }

    /**
    Delete competency of this item<BR>
    Pre-defined variable:
    <ul>
    <li>itm_id
    </ul>
    */
    private void delCompetency(Connection con) throws SQLException {

        DbItemCompetency itc = new DbItemCompetency();
        itc.itc_itm_id = this.itm_id;
        itc.del(con);
        return;
    }

    /**
    Save(delete existing and insert those in cm_lst) competency of this item<BR>
    Pre-defined variable:
    <ul>
    <li>itm_id
    </ul>
    @param cm_lst list of competency and level e.g. {"123,1", "256,1"}
    */
    public void saveCompetency(Connection con, String[] cm_lst, String usr_id)
        throws SQLException {

        DbItemCompetency itc = new DbItemCompetency();
        long itc_skl_skb_id;
        long itc_skl_level;
        String in;
        String delimiter = ",";
        itc.itc_itm_id = this.itm_id;
        itc.itc_create_usr_id = usr_id;
        //del cm
        itc.del(con);
        for(int i=0; i<cm_lst.length; i++) {
            int pos = 0;
            in = cm_lst[i];
            pos = in.indexOf(delimiter);
            itc.itc_skl_skb_id = Long.parseLong(in.substring(0,pos));
            itc.itc_skl_level = Long.parseLong(in.substring(pos + delimiter.length(), in.length()));
            //save cm
            itc.ins(con);
        }
        return;
    }

    private String getTargetPreviewAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuffer = new StringBuffer();
        ViewItemTargetGroup[] viTgps = ViewItemTargetGroup.getTargetGroups(con, this.itm_id, true, ViewItemTargetGroup.TARGETED_PREVIEW);
        xmlBuffer.append(ViewItemTargetGroup.targetGroupsAsXML(viTgps));
        return xmlBuffer.toString();
    }

    /**
    Get Targeted Learners as XML<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    <li>itm_apply_ind
    <li>itm_create_run_ind
    <li>itm_run_ind
    <li>itm_capacity
    </ul>
    */
    public String getTargetLrnAsXML(Connection con) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        ViewItemTargetGroup[] viTgps = null;
        viTgps = ViewItemTargetGroup.getTargetGroups(con, this.itm_id, true, this.targetType);
        //targeted learner
        xmlBuf.append(ViewItemTargetGroup.targetGroupsAsXML(viTgps));

        return xmlBuf.toString();
    }

    private String getCompTargetLrnAsXML(Connection con) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);

        /*Vector v_targetLrn = ViewItemTargetGroup.getTargetGroupsLrn(con, viTgps);*/
        //targeted learner

       // System.out.println("getTargetLrnAsXML: xmlBuf:" + xmlBuf.toString());
/*        //targeted learner numbers
        long attended = getAttendedTargetLrnNum(con, v_targetLrn);
        long targeted = v_targetLrn.size();
        xmlBuf.append("<targeted_lrn_num>");
        if(this.itm_apply_ind || this.itm_create_run_ind || this.itm_run_ind) {
            xmlBuf.append("<num value=\"").append(targeted)
                .append(",").append(attended)
                .append(",").append(targeted - attended).append("\"/>");
        }
        else {
            xmlBuf.append("<num value=\"").append(targeted).append("\"/>");
        }
        xmlBuf.append("</targeted_lrn_num>").append(cwUtils.NEWL);*/

/*        //targeted number of run
        if(this.itm_create_run_ind) {
            xmlBuf.append("<targeted_run_num>");
            xmlBuf.append("<num value=\"");
            if(this.itm_capacity != 0) {
                xmlBuf.append((int)Math.ceil( (float)targeted / (float) this.itm_capacity));
            } else {
                xmlBuf.append("0");
            }
            xmlBuf.append("\"/>");
            xmlBuf.append("</targeted_run_num>");
        }*/
        return xmlBuf.toString();
    }

    /**
     * retrieve the cost center group and generate the xml
     * @param con
     * @return
     * @throws SQLException
     */
    private String getCostCenterGroupAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(512);
        ViewItemTargetGroup[] viTgps = ViewItemTargetGroup.getTargetGroups(con, this.itm_id, true, ViewItemTargetGroup.COST_CENTER);
        // cost center group
        xmlBuf.append("<cost_center>");
        xmlBuf.append(ViewItemTargetGroup.GroupListAsXML(viTgps));
        xmlBuf.append("</cost_center>");
//System.out.println(xmlBuf.toString());
        return xmlBuf.toString();
    }

    /**
    Get Number of Targeted Learners as XML<BR>
    And get the targeted number of run
    This function will not consider about Run
    Pre-define variable:
    <ul>
    <li>itm_id
    <li>itm_apply_ind
    <li>itm_create_run_ind
    <li>itm_run_ind
    <li>itm_capacity
    </ul>
    */
    public String getNumOfTargetLrnAsXML(Connection con, String applyMethod) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        ViewItemTargetGroup[] viTgps = ViewItemTargetGroup.getTargetGroups(con, this.itm_id, true, this.targetType);
        //ViewItemTargetGroup[] viTgps1 = ViewItemTargetGroup.getTargetGroups(con, this.itm_id, true, this.targetType);
        //System.out.println("viTgps :" + viTgps.length);
        //System.out.println("viTgps1 :" + viTgps1.length);

        Vector v_targetLrn = ViewItemTargetGroup.getTargetGroupsLrn(con, viTgps);
/*        //targeted learner
        xmlBuf.append(ViewItemTargetGroup.targetGroupsAsXML(viTgps));*/

        //targeted learner numbers
        long attended = getAttendedTargetLrnNum(con, v_targetLrn);
        long targeted = v_targetLrn.size();
        xmlBuf.append("<targeted_lrn_num>");
        if(this.itm_apply_ind || this.itm_create_run_ind || this.itm_run_ind) {
            xmlBuf.append("<num value=\"").append(targeted)
                .append(",").append(attended)
                .append(",").append(targeted - attended).append("\"/>");

        }
        else {
            xmlBuf.append("<num value=\"").append(targeted).append("\"/>");
        }

        xmlBuf.append("</targeted_lrn_num>").append(cwUtils.NEWL);
        xmlBuf.append("<capacity>").append(this.itm_capacity).append("</capacity>").append(cwUtils.NEWL);

/*        //targeted number of run
        if(this.itm_create_run_ind) {
            xmlBuf.append("<targeted_run_num>");
            xmlBuf.append("<num value=\"");
            if(this.itm_capacity != 0) {
                xmlBuf.append((int)Math.ceil( (float)targeted / (float) this.itm_capacity));
            } else {
                xmlBuf.append("0");
            }
            xmlBuf.append("\"/>");
            xmlBuf.append("</targeted_run_num>");
        }*/
        return xmlBuf.toString();
    }

    /**
    Get an xml that will count the number of actual targeted learners of this item<BR>
    including old versions and run's (if this item has runs)<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    private String getActualTargetLrnNumAsXML(long targeted, long attended)
        throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(256);
        xmlBuf.append("<actual_targeted_lrn_num>");
        xmlBuf.append("<num value=\"").append(targeted - attended).append("\"/>");
        xmlBuf.append("</actual_targeted_lrn_num>").append(cwUtils.NEWL);
        return xmlBuf.toString();
    }

    /**
    Get an xml that will count the number of attended targeted learners of this item<BR>
    including old versions and run's (if this item has runs)<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    private String getAttendedTargetLrnNumAsXML(Connection con, Vector v_targetLrn)
        throws SQLException {

        long cnt = getAttendedTargetLrnNum(con, v_targetLrn);
        StringBuffer xmlBuf = new StringBuffer(256);
        xmlBuf.append("<attended_targeted_lrn_num>");
        xmlBuf.append("<num value=\"").append(cnt).append("\"/>");
        xmlBuf.append("</attended_targeted_lrn_num>").append(cwUtils.NEWL);
        xmlBuf.append(getActualTargetLrnNumAsXML(v_targetLrn.size(), cnt));
        return xmlBuf.toString();
    }

    /**
    Count the number of attended targeted learners of this item, excluding the attendance without application<BR>
    including old versions and run's (if this item has runs)<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    private long getAttendedTargetLrnNum(Connection con, Vector v_targetLrn)
        throws SQLException {

        long cnt = 0;
        if(v_targetLrn == null) {
            v_targetLrn = getTargetLrn(con);
        }
        if(v_targetLrn.size() != 0) {
            String sql_target_ent_id = cwUtils.vector2list(v_targetLrn);
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(" Select count(distinct(app_ent_id)) as cnt from aeApplication, aeAttendance, aeAttendanceStatus ")
                  .append(" where app_ent_id in ")
                  .append(sql_target_ent_id)
                  .append(" and att_app_id = app_id and app_itm_id = att_itm_id and att_ats_id = ats_id and ats_attend_ind = ? ")
                  .append(" and (app_itm_id in ")
                  .append("(Select ire_child_itm_id From aeItem fam, aeItem cur, aeItemRelation ")
                  .append(" where cur.itm_id = ? and cur.itm_code = fam.itm_code ")
                  .append(" and ire_parent_itm_id = fam.itm_id) ")
                  .append(" OR app_itm_id in ")
                  .append(" (Select fam.itm_id From aeItem fam, aeItem cur ")
                  .append(" where cur.itm_id =  ? and cur.itm_code = fam.itm_code))");
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setBoolean(1, true);
            stmt.setLong(2, this.itm_id);
            stmt.setLong(3, this.itm_id);
            ResultSet rs = stmt.executeQuery();
            cnt = (rs.next()) ? rs.getLong("cnt") : 0;
            stmt.close();
        }
        return cnt;
    }

    /*
    *   itm id = not a run itm_id, excluding the attendance without application
    */
    public Hashtable getAttendedUser(Connection con, Vector ent_vec, long itm_id)
        throws SQLException {
        Hashtable hash = new Hashtable();
		String[] s = null;
		
        if(ent_vec != null && ent_vec.size() != 0) {
            StringBuffer SQLBuf = new StringBuffer(512);
			s = cwSQL.getSQLClause(con,"tmp_app_ent_id",cwSQL.COL_TYPE_LONG,ent_vec,0);
            SQLBuf.append(" Select app_ent_id, app_id from aeApplication, aeAttendance, aeAttendanceStatus ")
                  .append(" where app_ent_id in ")
                  //.append(cwUtils.vector2list(ent_vec))
                  .append(s[0])
                  .append(" and att_app_id = app_id and app_itm_id = att_itm_id and att_ats_id = ats_id ")
                  .append(" and ats_attend_ind in (?,?) ")//把所有状态的查询出来 - HKO Bug 16632 - 第一次報名是TA手動mark as fail, 然後第二次報名是TA import enrollment, 但沒顯示+號 (retake). 在邏輯上, 無論什麼情況, 多過1次報名都應該顯示retake
                  .append(" and (app_itm_id in ")
                  .append("(Select ire_child_itm_id From aeItem fam, aeItem cur, aeItemRelation ")
                  .append(" where cur.itm_id = ? and cur.itm_code = fam.itm_code ")
                  .append(" and ire_parent_itm_id = fam.itm_id) ")
                  .append(" OR app_itm_id in ")
                  .append(" (Select fam.itm_id From aeItem fam, aeItem cur ")
                  .append(" where cur.itm_id =  ? and cur.itm_code = fam.itm_code))");
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setBoolean(1, true);//把所有状态的查询出来 - HKO Bug 16632 - 第一次報名是TA手動mark as fail, 然後第二次報名是TA import enrollment, 但沒顯示+號 (retake). 在邏輯上, 無論什麼情況, 多過1次報名都應該顯示retake
            stmt.setBoolean(2, false);
            stmt.setLong(3, itm_id);
            stmt.setLong(4, itm_id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Long ent_id = new Long(rs.getLong("app_ent_id"));
                Long app_id = new Long(rs.getLong("app_id"));

                Vector vec = (Vector)hash.get(ent_id);

                if (vec == null) {
                    vec = new Vector();
                }

                vec.addElement(app_id);
                hash.put(ent_id, vec);
            }

            stmt.close();
			if(s!=null && s[1] != null){
			   cwSQL.dropTempTable(con,s[1]);
			}
        }
        return hash;
    }

    /**
    Get targeted learners of this item<BR>
    If this item is a run, find out it's parent and return parent's target learners
    Pre-defined variable:
    <ul>
    <li>itm_id<li>
    </ul>
    @return Vector of Long objects of usr_ent_id
    */
    public Vector getTargetLrn(Connection con) throws SQLException {
        Vector v_targetLrn;
        long rootItmId = getRootItemId(con, this.itm_id);
        v_targetLrn = ViewItemTargetGroup.getTargetGroupsLrn(con, rootItmId, this.targetType);
        return v_targetLrn;
    }

    public Vector getTargetLrn(Connection con, String applyMethod) throws SQLException {
        Vector v_targetLrn;
        long rootItmId = getRootItemId(con, this.itm_id);
        v_targetLrn = ViewItemTargetGroup.getTargetGroupsLrn(con, rootItmId, this.targetType, applyMethod);
        return v_targetLrn;
    }

    /**
    Get request approve info for vendoring the page for request approval<BR>
    Hardcoded to get "DIRR" users as default To: users<BR>
    Pre-defined variable:
    <!--
    <ul>
    <li>itm_id<li>
    <li>itm_title</li>
    </ul>
    -->
    */
//    public ViewNotifyUser[] requestApprovalInfo(Connection con, long owner_ent_id) throws SQLException {
//
//        StringBuffer SQLBuf = new StringBuffer(256);
//        AccessControlWZB acl = new AccessControlWZB();
//        SQLBuf.append(" Select usr_id, usr_ent_id, usr_display_bil, usr_email_2 From RegUser ");
//        SQLBuf.append(" Where usr_ent_id In ");
//        SQLBuf.append("(").append(acl.getEntityByFunctionSQL(con, AcItem.FTN_ITM_APPROVE)).append(")");
//        SQLBuf.append(" And usr_ste_ent_id = ? ");
//        SQLBuf.append(" order by usr_display_bil asc ");
//
//        int idx = 0;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Vector tempResult = null;
//        try {
//            idx = 1;
//            stmt = con.prepareStatement(SQLBuf.toString());
//            stmt.setLong(idx++, owner_ent_id);
//            rs = stmt.executeQuery();
//
//            tempResult = new Vector();
//            while (rs.next()) {
//                idx = 1;
//                ViewNotifyUser ntfyUser = new ViewNotifyUser();
//                ntfyUser.usr_id = rs.getString(idx++);
//                ntfyUser.usr_ent_id = rs.getLong(idx++);
//                ntfyUser.usr_display_bil = rs.getString(idx++);
//                ntfyUser.usr_email_2 = rs.getString(idx++);
//                tempResult.addElement(ntfyUser);
//            }
//        } finally {
//            if (stmt != null) {
//                stmt.close();
//            }
//        }
//        ViewNotifyUser result[] = new ViewNotifyUser[tempResult.size()];
//        result = (ViewNotifyUser[])tempResult.toArray(result);
//
//        return result;
//    }

    /**
    Update this item's life status<BR>
    Pre-defined variable:
    <ul>
    <li>itm_id<li>
    <li>itm_life_status</li>
    </ul>
    @exception cwSysMessage if this item is not last updated
    */
    public void setLifeStatus(Connection con, String usr_id, Timestamp upd_timestamp)
        throws SQLException, cwSysMessage {

        if(!isLastUpd(con, upd_timestamp)) {
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        }
        this.itm_upd_usr_id = usr_id;
        this.itm_upd_timestamp = cwSQL.getTime(con);
        setLifeStatus(con);
        return;
    }

    /**
    Update this item's life status but use a different method to check item's last update time<BR>
    Used by Messaging<BR>
    Pre-defined variable:
    <ul>
    <li>itm_id<li>
    <li>itm_life_status</li>
    </ul>
    @exception cwSysMessage if this item is not last updated
    */
    public void setLifeStatusByMsg(Connection con, String usr_id, Timestamp cur_timestamp)
        throws SQLException, cwSysMessage {

        if(!isLastUpdBefore(con, cur_timestamp)) {
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        }
        this.itm_upd_usr_id = usr_id;
        this.itm_upd_timestamp = cwSQL.getTime(con);
        setLifeStatus(con);
        return;
    }

    private static final String sql_set_life_status =
        " Update aeItem Set itm_life_status = ?, " +
        " itm_upd_usr_id = ?, itm_upd_timestamp = ? " +
        " Where itm_id = ? ";
    /**
    Update this item's life status<BR>
    Pre-defined variable:
    <ul>
    <li>itm_id<li>
    <li>itm_life_status</li>
    <li>itm_upd_timestamp</li>
    <li>itm_upd_usr_id</li>
    </ul>
    */
    private void setLifeStatus(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(sql_set_life_status);
        int index = 1;
        stmt.setString(index++, this.itm_life_status);
        stmt.setString(index++, this.itm_upd_usr_id);
        stmt.setTimestamp(index++, this.itm_upd_timestamp);
        stmt.setLong(index++, this.itm_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    /**
    insert the input Mote to this item<BR>
    Pre-defined variable:
    <ul>
    <li>itm_id<li>
    </ul>
    */
    public void insMote(Connection con, Mote mote, String usr_id)
        throws cwException, SQLException {
            this.itm_imd_id = mote.insMoteDefaultNMote(con, usr_id);
            updItemMoteId(con);
            return;
    }

    /*
    private static final String sql_get_imd_id =
        "Select itm_imd_id From aeItem where itm_id = ? ";

    private long getItemModeId(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_get_imd_id);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_imd_id = rs.getLong("itm_imd_id");
        }
        stmt.close();
        return this.itm_imd_id;
    }
    */

    /**
    Update the input Mote to this item<BR>
    Pre-defined variable:
    <ul>
    <li>itm_id<li>
    <li>itm_imd_id<li>
    </ul>
    */
    public void updMote(Connection con, Mote mote, String usr_id)
        throws cwException, SQLException {
            if(this.itm_imd_id == 0) {
                getMoteId(con);
            }
            //System.out.println("imd_id = " + this.itm_imd_id);
            mote.imd_id = this.itm_imd_id;
            mote.updMoteDefaultNMote(con, this.itm_id, this.itm_type, usr_id);
            return;

    }

    private static final String sql_get_imd_id =
        " Select itm_imd_id From aeItem where itm_id = ? ";
    public long getMoteId(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(sql_get_imd_id);
        int index = 1;
        stmt.setLong(index++, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_imd_id = rs.getLong("itm_imd_id");
        }
        else {
            this.itm_imd_id = 0;
        }
        stmt.close();
        return this.itm_imd_id;
    }

    /**
    Delete mote of this item
    Pre-defined variable:
    <ul>
    <li>itm_imd_id<li>
    </ul>
    */
    private void delMote(Connection con) throws SQLException {

        if(this.itm_imd_id != 0) {
            Mote mote = new Mote();
            mote.imd_id = this.itm_imd_id;
            mote.delMoteDefaultNMote(con);
        }
        return;
    }

    private static final String sql_upd_itm_imd_id =
        " Update aeItem Set itm_imd_id = ? Where itm_id = ? ";
    /**
    update this item's itm_imd_id<BR>
    Pre-defined variable:
    <ul>
    <li>itm_imd_id<li>
    <li>itm_id<li>
    </ul>
    */
    private void updItemMoteId(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_upd_itm_imd_id);
        int index = 1;
        stmt.setLong(index++, this.itm_imd_id);
        stmt.setLong(index++, this.itm_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    public String prepareCancelItem(Connection con) throws SQLException, cwSysMessage, cwException {
        if(this.itm_life_status != null && this.itm_life_status.equals(ITM_LIFE_STATUS_CANCELLED)) {
            throw new cwSysMessage(ITM_ALREADY_CANCELLED);
        }
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append(shortInfoAsXML(con));
        // get Cancel Type List AsXML
        String[] ctb_types = {FMReservationManager.FM_CANCEL_TYPE};
        // static method getAll(con ,ctb_types) in CodeTable
        xmlBuf.append("<cancel_type_list>");
        xmlBuf.append(CodeTable.getAll(con ,ctb_types));
        xmlBuf.append("</cancel_type_list>");
        return xmlBuf.toString();
    }

    private static final String sql_cancel_item =
        " Update aeItem Set itm_life_status = ?, " +
        " itm_upd_usr_id = ?, itm_upd_timestamp = ?, " +
        " itm_cancellation_reason = ?, " +
        " itm_cancellation_type = ?, " +
        " itm_status = ? " +
        " Where itm_id = ? ";
    /**
    Cancel this itme<BR>
    Set itm_life_status = 'CANCELLED' and save the reason to itm_ext1
    Pre-defined variable:
    <ul>
    <li>itm_id
    <li>itm_cancellation_reason
    </ul>
    */
    public void cancelItem(Connection con, loginProfile prof,
                            Timestamp upd_timestamp,
                            Timestamp rsv_upd_timestamp,
                            Timestamp curTime)
                            throws SQLException, cwSysMessage, cwException, IOException, qdbException {

        if(!isLastUpd(con, upd_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

        String life_status = getItemLifeStatus(con);
        if(life_status != null && life_status.equals(ITM_LIFE_STATUS_CANCELLED)) {
            throw new cwSysMessage(ITM_ALREADY_CANCELLED);
        }

        this.itm_upd_usr_id = prof.usr_id;
        if(curTime == null) {
            curTime = cwSQL.getTime(con);
        }
        this.itm_upd_timestamp = curTime;
        this.itm_life_status = ITM_LIFE_STATUS_CANCELLED;
        this.itm_status = ITM_STATUS_OFF;

        PreparedStatement stmt = con.prepareStatement(sql_cancel_item);
        int index = 1;
        stmt.setString(index++, this.itm_life_status);
        stmt.setString(index++, this.itm_upd_usr_id);
        stmt.setTimestamp(index++, this.itm_upd_timestamp);
        stmt.setString(index++, this.itm_cancellation_reason);
        stmt.setString(index++, this.itm_cancellation_type);
        stmt.setString(index++, this.itm_status);
        stmt.setLong(index++, this.itm_id);
        stmt.executeUpdate();
        stmt.close();

        //cancel all application
        /*
        aeAttendance.delByItem(con, this.itm_id);
        aeApplication.delByItem(con, this.itm_id);
        */
        aeQueueManager qm = new aeQueueManager();
        qm.makeItemCancelled(con, prof, itm_id);
        
        
        
        // Update the Overall Attendance Rate (if necessary)
        aeItem tempItm = new aeItem();
        tempItm.itm_id = this.itm_id;
        tempItm.get(con);
/*
        if (tempItm.itm_session_ind && tempItm.itm_has_attendance_ind) {
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itm_id;
            this.parent_itm_id = ire.getParentItemId(con);
            Vector v_app_id = aeApplication.getLatestItmAppnLstWAtt(con, this.parent_itm_id);

            for (int i=0; i<v_app_id.size(); i++) {
                aeAttendance att = new aeAttendance();
                att.autoUpdateAttendance(con, usr_id, tempItm.itm_owner_ent_id, this.parent_itm_id, ((Long)v_app_id.elementAt(i)).longValue(), false, true, true);
            }
        }
*/
        //cancel JI item message
        aeItemMessage.delByItem(con, this.itm_id);

        //cancel reservation
        getInfo4CancelRsv(con);
        if(this.itm_rsv_id > 0) {
             if(rsv_upd_timestamp == null){
                FMAuditTrail auditor = new FMAuditTrail(con);
                auditor.setRsvAuditTrail((int)this.itm_rsv_id);
                rsv_upd_timestamp = auditor.getRsvUpdTimestamp();
            }
            FMReservationManager rsvMgr = new FMReservationManager(con);
            rsvMgr.cancelRsv((int)this.itm_owner_ent_id, prof.usr_id, (int)this.itm_rsv_id, rsv_upd_timestamp, this.itm_cancellation_type, null/*this.itm_cancellation_reason*/, false);
        }

        //cancel lesson instructor
        aeItemLessonInstructor.delByItem(con, this.itm_id, null);

        return;
    }

    private static final String sql_get_itm_code =
        " Select itm_code From aeItem Where itm_id = ? ";
    /**
    Get the itm_code of this item
    @return itm_code of this item
    */
    public String getItemCode(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_code);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_code = rs.getString("itm_code");
        }
        else {
            this.itm_code = null;
        }
        rs.close();
        stmt.close();
        return this.itm_code;
    }
    /**
    Get the itm_code of this item
    @return itm_code of this item
    */
    public String getItemPlanCode(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("select itm_plan_code from aeItem where itm_id = ? ");
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_plan_code = rs.getString("itm_plan_code");
        }
        else {
            this.itm_plan_code = null;
        }
        rs.close();
        stmt.close();
        return this.itm_plan_code;
    }
    private static final String sql_is_itm_code_exist =
        " Select itm_code From aeItem Where itm_code = ? ";
    /**
    Check if the itm_code exists in the organization<BR>
    Pre-define variable:
    <ul>
    <li>itm_code</li>
    <li>itm_owner_ent_id</li>
    </ul>
    */
    public boolean isItemCodeExist(Connection con, long itm_id) throws SQLException {

        boolean result;
        String sql = sql_is_itm_code_exist;
        if (itm_id != 0) {
            sql += " and itm_id <> ? ";
        }

        PreparedStatement stmt = con.prepareStatement(sql);

        int index = 1;
        stmt.setString(index++, this.itm_code);
        if (itm_id != 0)
            stmt.setLong(index++, this.itm_id);

//        stmt.setBoolean(index++, false);
        ResultSet rs = stmt.executeQuery();
        result = rs.next();
        stmt.close();

        return result;
    }

    private static final String sql_get_appr_version_id =
        " select appr.itm_id from aeItem appr " +
        " where appr.itm_code = (Select a.itm_code From aeItem a Where a.itm_id = ?) " +
        " and appr.itm_owner_ent_id = (Select b.itm_owner_ent_id From aeItem b Where b.itm_id = ?) " +
        " and appr.itm_life_status = ? ";
    /**
    Get itm_id of the approved version item<BR>
    Pre-define variable:
    <ul>
    <li>itm_id</li>
    </ul>
    */
    private long getApprovedVersionId(Connection con) throws SQLException {

        long appr_itm_id;
        PreparedStatement stmt = con.prepareStatement(sql_get_appr_version_id);
        int index = 1;
        stmt.setLong(index++, this.itm_id);
        stmt.setLong(index++, this.itm_id);
        stmt.setString(index++, ITM_LIFE_STATUS_APPROVED);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            appr_itm_id = rs.getLong("itm_id");
        }
        else {
            appr_itm_id = 0;
        }
        stmt.close();
        return appr_itm_id;
    }

    /**
    Get detail xml of the approved version item<BR>
    Pre-define variable:
    <ul>
    <li>itm_id</li>
    </ul>
    */
    public String getApprovedVersionAsXML(Connection con, qdbEnv env, long tnd_id,
                                            boolean checkStatus, boolean prev_version_ind,
                                            String tvw_id)
                                            throws SQLException, cwException, cwSysMessage {
        aeItem apprItem = new aeItem();
        apprItem.itm_id = getApprovedVersionId(con);
        if( apprItem.itm_id == 0 ) {
            apprItem.itm_id = this.itm_id;
        }
        if(tnd_id == 0) {
            apprItem.getItem(con);
        }
        else {
            apprItem.get(con, tnd_id);
        }
        return apprItem.ItemDetailAsXML(con, env, checkStatus, prev_version_ind, tvw_id, false, false, 0, 0, false, false, false ,null, null, false);
    }

    private static final String sql_is_itm_approved =
        " select itm_id from aeItem " +
        " where (itm_life_status is not null and itm_life_status <> ?)" +
        " and itm_id in ";
    /**
    Check if this item has been approved before by looking at the old versions<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    public boolean isItemApproved(Connection con) throws SQLException, cwSysMessage {
        Vector v_fam_itm_id = getItemFamilyId(con);
        String sql_fam_itm_id = aeUtils.prepareSQLList(v_fam_itm_id);
        String SQL = sql_is_itm_approved + sql_fam_itm_id;
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ITM_LIFE_STATUS_IN_PROGRESS);
        ResultSet rs = stmt.executeQuery();
        boolean isApproved = rs.next();
        stmt.close();
        return isApproved;
    }

    private static final String sql_get_itm_family =
        " select fam.itm_id from aeItem fam " +
        " where fam.itm_code = (Select a.itm_code From aeItem a Where a.itm_id = ?) " +
        " and fam.itm_owner_ent_id = (Select b.itm_owner_ent_id From aeItem b Where b.itm_id = ?) " +
        " order by fam.itm_id desc ";
    /**
    Get the item family of this item<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    @return Vector contains objects of aeItem
    */
    private Vector getItemFamily(Connection con) throws SQLException, cwSysMessage {

        Vector v_family = new Vector();
        aeItem itm;
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_family);
        int index = 1;
        stmt.setLong(index++, this.itm_id);
        stmt.setLong(index++, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            itm = new aeItem();
            itm.itm_id = rs.getLong("itm_id");
            itm.getItem(con);
            v_family.addElement(itm);
        }
        stmt.close();
        return v_family;
    }

    /**
    Get the item family of this item<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    @return Vector contains Long of itm_id
    */
    private Vector getItemFamilyId(Connection con) throws SQLException, cwSysMessage {

        Vector v_family = new Vector();
        aeItem itm;
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_family);
        int index = 1;
        stmt.setLong(index++, this.itm_id);
        stmt.setLong(index++, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v_family.addElement(new Long(rs.getLong("itm_id")));
        }
        stmt.close();
        return v_family;
    }

    /**
    Generate simple XML only has itme attributes
    */
    public String itemAttributeAsXML() {
        StringBuffer xmlBuf = new StringBuffer(512);

        // item attributes
        xmlBuf.append("<item")
                .append(" id=\"").append(itm_id).append("\"")
                .append(" type=\"").append(itm_type).append("\"")
                .append(" status=\"").append(itm_status).append("\"")
                .append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"")
                .append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"")
                //.append(" sks_id=\"").append(itm_sks_skb_id).append("\"")
                .append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"")
                .append(" apply_method=\"").append(itm_apply_method).append("\"")
                .append(" itm_version_code=\"").append(dbUtils.esc4XML(itm_version_code)).append("\"")
                .append(" create_run_ind=\"").append(itm_create_run_ind).append("\"")
                .append(" create_session_ind=\"").append(itm_create_session_ind).append("\"")
                .append(" qdb_ind=\"").append(itm_qdb_ind).append("\"")
                .append(" auto_enrol_qdb_ind=\"").append(itm_auto_enrol_qdb_ind).append("\"")
                .append(" run_ind=\"").append(itm_run_ind).append("\"")
                .append(" session_ind=\"").append(itm_session_ind).append("\"")
                .append(" has_attendance_ind=\"").append(itm_has_attendance_ind).append("\"")
                .append(" ji_ind=\"").append(itm_ji_ind).append("\"")
                .append(" completion_criteria_ind=\"").append(itm_completion_criteria_ind).append("\"")
                .append(" apply_ind=\"").append(itm_apply_ind).append("\"")
                .append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"")
        ;
        xmlBuf.append(">");
        //item title
        xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>").append(dbUtils.NEWL);
        xmlBuf.append("</item>");
        return xmlBuf.toString();
    }

    /**
    Get XML for mote<BR>
    Pre-define variable:
    <ul>
    <li>itm_imd_id</li>
    </ul>
    */
    private String getMoteAsXML(Connection con) throws SQLException {

        MoteDefault moteD = new MoteDefault();
        moteD.imd_id = this.itm_imd_id;
        moteD.get(con);
        return((moteD.defaultValueAsXML()).toString());
    }

    // added "cost_center_group_lst" for cost center
    public void insExternalInfo(Connection con,
                                long[] tnd_id_lst,
								String[] tnd_title_lst,
                                String[] target_ent_group_lst,
                                String[] comp_target_ent_group_lst,
                                String[] cost_center_group_lst,
                                String[] iac_id_lst,
                                Mote mote,
                                String[] cm_lst,
                                Vector v_fgt_id,
                                Vector v_fig_val,
                                String usr_id,
                                long usr_ent_id,
                                long owner_ent_id,
                                long default_ta_ent_id,
                                boolean tc_enabled)
                                throws SQLException, cwException, cwSysMessage {
        String type = null;
        if(tnd_id_lst != null) {
        	if(tc_enabled) {
            	Vector vec = DbTrainingCenter.getChildTc(con, this.itm_tcr_id);
            	vec.add(new Long(this.itm_tcr_id));
            	for(int i=0; i < tnd_id_lst.length; i++) {
            		long tcr_id = aeTreeNode.getTcrIdByTndId(con, tnd_id_lst[i], owner_ent_id); 
            		if(!vec.contains(new Long(tcr_id))) {
            			throw new cwSysMessage(aeCatalog.CAT_NOT_IN_TC);
            		}
            	}
        	}

            try {
                attachTreeNodes(con, tnd_id_lst, tnd_title_lst, usr_id, usr_ent_id, owner_ent_id);
            }
            catch (qdbException e) {
                throw new cwException (e.getMessage());
            }
        }
        if(iac_id_lst != null) {
        	if (iac_id_lst[0] != null && iac_id_lst[0].length() > 0 && iac_id_lst[0].equals("TADM_" + owner_ent_id)) {
				iac_id_lst[0] += "~" + default_ta_ent_id;
			}
        	saveItemAccess(con, iac_id_lst, owner_ent_id, tc_enabled);
        }
        if(mote != null) {
            insMote(con, mote, usr_id);
        }
        if(cm_lst != null) {
            saveCompetency(con, cm_lst, usr_id);
        }

        aeItemFigure ifg = new aeItemFigure();
        loginProfile prof = new loginProfile();
        prof.usr_id = usr_id;
        ifg.updItemFigure(con, prof, this.itm_id, v_fgt_id, v_fig_val);

        //save training center linkage
        updateItmTcrId(con);

        return;
    }

    // added "cost_center_group_lst" for cost center
    public void saveExternalInfo(Connection con,
                                long[] tnd_id_lst,
                                String[] tnd_title_lst,
                                String[] target_ent_group_lst,
                                String[] comp_target_ent_group_lst,
                                String[] cost_center_group_lst,
                                String[] iac_id_lst,
                                Mote mote,
                                String[] cm_lst,
                                Vector v_fgt_id,
                                Vector v_fig_val,
                                String usr_id,
                                long usr_ent_id,
                                long owner_ent_id,
                                long default_ta_ent_id,
                                boolean tc_enabled)
                                throws SQLException, cwException, cwSysMessage {
        String type = null;
        if(tnd_id_lst != null) {
            try {
            	if(tc_enabled) {
                	Vector vec = DbTrainingCenter.getChildTc(con, this.itm_tcr_id);
                	vec.add(new Long(this.itm_tcr_id));
                	for(int i=0; i < tnd_id_lst.length; i++) {
                		long tcr_id = aeTreeNode.getTcrIdByTndId(con, tnd_id_lst[i], owner_ent_id); 
                		if(!vec.contains(new Long(tcr_id))) {
                			throw new cwSysMessage(aeCatalog.CAT_NOT_IN_TC);
                		}
                	}
            	}
                attachTreeNodes(con, tnd_id_lst, tnd_title_lst, usr_id, usr_ent_id, owner_ent_id);
            }
            catch (qdbException e) {
                throw new cwException (e.getMessage());
            }
        }
        if(iac_id_lst != null) {
			if (iac_id_lst[0] != null && iac_id_lst[0].length() > 0 && iac_id_lst[0].equals("TADM_" + owner_ent_id)) {
				iac_id_lst[0] += "~" + default_ta_ent_id;
			}
            saveItemAccess(con, iac_id_lst, owner_ent_id, tc_enabled);
        }
        if(mote != null) {
            //System.out.println("try to update Mote");
            updMote(con, mote, usr_id);
        }
        if(cm_lst != null) {
            saveCompetency(con, cm_lst, usr_id);
        }

        aeItemFigure ifg = new aeItemFigure();
        loginProfile prof = new loginProfile();
        prof.usr_id = usr_id;
        ifg.updItemFigure(con, prof, this.itm_id, v_fgt_id, v_fig_val);

        //save training center linkage
        updateItmTcrId(con);
        // save class training center
        updateChildClassItmTcrId(con);

        return;
    }

    public static long getBaseItemId(Connection con, long itm_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("select item2.itm_id FROM aeItem item1, aeItem item2 WHERE item1.itm_id = ? AND item1.itm_code = item2.itm_code AND item1.itm_owner_ent_id = item2.itm_owner_ent_id AND item2.itm_id = (select min(item3.itm_id) FROM aeItem item3 WHERE item3.itm_id = item1.itm_id AND item1.itm_code = item3.itm_code AND item1.itm_owner_ent_id = item3.itm_owner_ent_id)");
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        long base_itm_id = 0;

        if (rs.next()) {
            base_itm_id = rs.getLong("itm_id");
        }
        rs.close();
        stmt.close();

        return base_itm_id;
    }

    /**
    Get an XML of this item's targeted groups<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    public String getTargetGroupsAsXML(Connection con, String apply_method) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<item id=\"").append(this.itm_id).append("\">");
        xmlBuf.append(ViewItemTargetGroup.getTargetGroupsAsXML(con, this.itm_id, apply_method));
        xmlBuf.append("</item>");
        return xmlBuf.toString();
    }

    /**
    Generate a XML for inviting targeted learners<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    public ViewNotifyUser[] inviteMsgInfo(Connection con, String[] groups)
        throws SQLException, cwException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        //if(groups != null) {
            ViewItemTargetGroup viTgps[] = new ViewItemTargetGroup[groups.length];
            dbEntity ent = new dbEntity();

            //for each group, get the ent_types of those ent_id and put it in viTgps
            try {
                for(int i=0; i<viTgps.length; i++) {
                    viTgps[i] = new ViewItemTargetGroup();
                    viTgps[i].itemId = this.itm_id;
                    viTgps[i].targetEntIds = cwUtils.splitToLong(groups[i], ",");
                    viTgps[i].targetEntTypes = new String[viTgps[i].targetEntIds.length];
                    for(int j=0; j<viTgps[i].targetEntIds.length; j++) {
                        ent.ent_id = viTgps[i].targetEntIds[j];
                        ent.get(con);
                        viTgps[i].targetEntTypes[j] = ent.ent_type;
                    }
                }
            }
            catch(qdbException e) {
                throw new cwException (e.getMessage());
            }
            //get the targeted learners in groups
            Vector v_target_lrn = ViewItemTargetGroup.getTargetGroupsLrn(con, viTgps);
            v_target_lrn.addElement(new Long(0));


            /*
            //format xml for messaging
            xmlBuf.append("<item id=\"").append(this.itm_id).append("\">");
            xmlBuf.append("<notify dd=\"\" mm=\"\" yy=\"\">");
            xmlBuf.append("<subject/>");
            xmlBuf.append("<content/>");
            xmlBuf.append("<method notes=\"yes\" etray=\"no\"/>");
            xmlBuf.append("</notify>");
            xmlBuf.append("<picked_entity>");
            */
            //get usr_ent_id, usr_display_bil and usr_email_2 for message
            String list = cwUtils.vector2list(v_target_lrn);
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(" Select usr_id, usr_ent_id, usr_display_bil, usr_email_2 From RegUser ");
            SQLBuf.append(" Where usr_ent_id In ").append(list);
            SQLBuf.append(" order by usr_display_bil asc ");

            int idx = 0;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Vector tempResult = null;
            try {
                stmt = con.prepareStatement(SQLBuf.toString());
                rs = stmt.executeQuery();

                tempResult = new Vector();
                while (rs.next()) {
                    idx = 1;
                    ViewNotifyUser ntfyUser = new ViewNotifyUser();
                    ntfyUser.usr_id = rs.getString(idx++);
                    ntfyUser.usr_ent_id = rs.getLong(idx++);
                    ntfyUser.usr_display_bil = rs.getString(idx++);
                    ntfyUser.usr_email_2 = rs.getString(idx++);
                    tempResult.addElement(ntfyUser);
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
            ViewNotifyUser result[] = new ViewNotifyUser[tempResult.size()];
            result = (ViewNotifyUser[])tempResult.toArray(result);

            return result;
            /*
            while(rs.next()) {
                xmlBuf.append("<entity id=\"").append(rs.getLong("usr_ent_id")).append("\"");
                xmlBuf.append(" display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\"");
                xmlBuf.append(" email=\"").append(cwUtils.esc4XML(rs.getString("usr_email_2"))).append("\"/>");
            }
            stmt.close();
            xmlBuf.append("</picked_entity>");
            xmlBuf.append("<picked_cc/>");
            xmlBuf.append("</item>");
            */
        //}
        //return xmlBuf.toString();
    }

    private static final String sql_get_applied_run_id =
        " select ire_child_itm_id from aeApplication, aeItemRelation, aeItem " +
        " where ire_child_itm_id =  app_itm_id " +
        " and ire_child_itm_id = itm_id " +
        " and itm_run_ind = ?  " +
        " and ire_parent_itm_id = ? " +
        " and app_ent_id = ? " +
        " and app_status <> ? and app_status <> ? " +
        " AND (Exists (select att_app_id from aeAttendance, aeAttendanceStatus where att_app_id = app_id and app_itm_id = att_itm_id and att_ats_id = ats_id and ats_type = ?) " +
        " OR Not Exists (select att_app_id from aeAttendance where att_app_id = app_id and app_itm_id = att_itm_id )) ";
    /**
    Get the applied run itm_id of this item<BR>
    Application with app_status = Withdrawn and Rejected is not considered as applied
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    private long getAppliedRunId(Connection con, long ent_id) throws SQLException {

        long run_itm_id;
        PreparedStatement stmt = con.prepareStatement(sql_get_applied_run_id);
        int index = 1;
        stmt.setBoolean(index++, true);
        stmt.setLong(index++, this.itm_id);
        stmt.setLong(index++, ent_id);
        stmt.setString(index++, aeApplication.WITHDRAWN);
        stmt.setString(index++, aeApplication.REJECTED);
        stmt.setString(index++, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            run_itm_id = rs.getLong("ire_child_itm_id");
        }
        else {
            run_itm_id = 0;
        }
        stmt.close();
        return run_itm_id;
    }

	private static final String sql_get_child_latest_app_id =
	"Select Max(app_id) " +
	"From aeApplication , aeItemRelation " +
	"Where app_itm_id = ire_child_itm_id " +
	"And ire_parent_itm_id = ? " +
	"And app_ent_id = ?	";


	long getChildLatestAppId(Connection con, long parent_itm_id, long usr_ent_id) throws SQLException {

		PreparedStatement stmt = con.prepareStatement(sql_get_child_latest_app_id);
		stmt.setLong(1, parent_itm_id);
		stmt.setLong(2, usr_ent_id);
		ResultSet rs = stmt.executeQuery();
		long app_id = 0;
		if(rs.next()){
			app_id = rs.getLong(1);
		}
		stmt.close();
		return app_id;
	}


    private static final String sql_get_create_run_ind =
        " Select distinct(itm_create_run_ind) " +
        " From aeItem Where " +
        " itm_run_ind = ? " +
        " And itm_session_ind = ? ";
        //" Select top 1 itm_create_run_ind From aeItem Where itm_type = ? And itm_owner_ent_id = ? ";
    /**
    Get the create run ind of each item type and store in an String array<BR>
    If there is no item of an item type exists, set the create run ind to null<BR>
    @return String array of "true", "false" or null
    */
    private static String[] getCreateRunInds(Connection con, String[] item_types) throws SQLException {
        String[] create_run_inds;
        if(item_types != null) {
            create_run_inds = new String[item_types.length];
            for(int i=0; i<item_types.length; i++) {
            	String sql=sql_get_create_run_ind+aeItemDummyType.genSqlByItemDummyType(item_types[i], null, true);
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setBoolean(1, false);
                stmt.setBoolean(2, false);

                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    create_run_inds[i] =
                        (rs.getBoolean("itm_create_run_ind") ? "true" : "false");
                }
                else {
                    create_run_inds[i] = null;
                }
                stmt.close();
            }
        }
        else {
            create_run_inds = null;
        }
        return create_run_inds;
    }

    /**
    For HomePage
    @deprecated
    */
    public Vector getItemTypeByInd(Connection con, long root_ent_id) throws SQLException{
        Vector itemTypes = new Vector();
//        final String sql = "SELECT DISTINCT itm_type AS ITM_TYPES FROM aeItem, aeItemType WHERE ity_id = itm_type AND ity_owner_ent_id = ? AND itm_create_run_ind = ? AND itm_create_session_ind = ? AND itm_apply_ind = ? AND itm_run_ind = ? AND itm_session_ind = ? ";
        final String sql = "SELECT DISTINCT itm_type AS ITM_TYPES FROM aeItem, aeItemType WHERE ity_id = itm_type AND ity_owner_ent_id = ? AND itm_create_run_ind = ? AND itm_apply_ind = ? AND itm_run_ind = ? AND itm_session_ind = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, root_ent_id);
        stmt.setBoolean(index++, itm_create_run_ind);
//        stmt.setBoolean(index++, itm_create_session_ind);
        stmt.setBoolean(index++, itm_apply_ind);
        stmt.setBoolean(index++, itm_run_ind);
        stmt.setBoolean(index++, itm_session_ind);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            itemTypes.addElement(rs.getString("ITM_TYPES"));
        }
        stmt.close();
        return itemTypes;
    }



	public void getItemTypeInd(Connection con)
		throws SQLException {
			DbItemType itmType = new DbItemType();
			itmType.ity_owner_ent_id = this.itm_owner_ent_id;
			itmType.ity_id = this.itm_type;
			itmType.ity_run_ind = this.itm_run_ind;
			itmType.ity_session_ind = this.itm_session_ind;
			itmType.ity_ref_ind = this.itm_ref_ind;
			itmType.ity_blend_ind = this.itm_blend_ind;
			itmType.ity_exam_ind = this.itm_exam_ind;
			Hashtable h_ind = itmType.getInd(con);
			this.itm_create_run_ind = ((Boolean)h_ind.get("ity_create_run_ind")).booleanValue();
			//this.itm_session_ind = ((Boolean)h_ind.get("ity_create_session_ind")).booleanValue();
			this.itm_has_attendance_ind = ((Boolean)h_ind.get("ity_has_attendance_ind")).booleanValue();
			this.itm_ji_ind = ((Boolean)h_ind.get("ity_ji_ind")).booleanValue();
			this.itm_completion_criteria_ind = ((Boolean)h_ind.get("ity_completion_criteria_ind")).booleanValue();
			this.itm_apply_ind = ((Boolean)h_ind.get("ity_apply_ind")).booleanValue();
			this.itm_qdb_ind = ((Boolean)h_ind.get("ity_qdb_ind")).booleanValue();
			this.itm_auto_enrol_qdb_ind = ((Boolean)h_ind.get("ity_auto_enrol_qdb_ind")).booleanValue();
			//this.itm_run_ind = ((Boolean)h_ind.get("ity_run_ind")).booleanValue();
			//this.itm_session_ind = ((Boolean)h_ind.get("ity_session_ind")).booleanValue();
			this.itm_can_cancel_ind = ((Boolean)h_ind.get("ity_can_cancel_ind")).booleanValue();
			return;
		}

    /**
    For HomePage
    @deprecated
    */
    public static Vector getItemTypeByInd(Connection con, long root_ent_id, String create_run_ind, String apply_ind, String run_ind) throws SQLException{
        return getItemTypeByInd(con, root_ent_id, create_run_ind, null, apply_ind, run_ind, null, null, null, null);
    }
    /**
    For HomePage
    */
    public static Vector getItemTypeByInd(Connection con, long root_ent_id, String create_run_ind, String create_session_ind, String apply_ind, String run_ind, String session_ind, String has_attendance_ind, String ji_ind, String completion_criteria_ind) throws SQLException{
        Vector itemTypes = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
        int index = 1;
        SQLBuf.append(" SELECT DISTINCT itm_type AS ITM_TYPES ")
              .append(" FROM aeItem, aeItemType ")
              .append(" WHERE ity_id = itm_type ")
              .append(" AND ity_owner_ent_id = ? ");
        if(create_run_ind != null
            && (create_run_ind.equalsIgnoreCase("TRUE") || (create_run_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_create_run_ind = ? ");
        }
        if(create_session_ind != null
            && (create_session_ind.equalsIgnoreCase("TRUE") || (create_session_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_create_session_ind = ? ");
        }
        if(apply_ind != null
            && (apply_ind.equalsIgnoreCase("TRUE") || (apply_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_apply_ind = ? ");
        }
        if(run_ind != null
            && (run_ind.equalsIgnoreCase("TRUE") || (run_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_run_ind = ? ");
        }
        if(session_ind != null
            && (session_ind.equalsIgnoreCase("TRUE") || (session_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_session_ind = ? ");
        }
        if(has_attendance_ind != null
            && (has_attendance_ind.equalsIgnoreCase("TRUE") || (has_attendance_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_has_attendance_ind = ? ");
        }
        if(ji_ind != null
            && (ji_ind.equalsIgnoreCase("TRUE") || (ji_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_ji_ind = ? ");
        }
        if(completion_criteria_ind!= null
            && (completion_criteria_ind.equalsIgnoreCase("TRUE") || (completion_criteria_ind.equalsIgnoreCase("FALSE")))) {
            SQLBuf.append(" AND itm_completion_criteria_ind = ? ");
        }
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(index++, root_ent_id);
        if(create_run_ind != null
            && (create_run_ind.equalsIgnoreCase("TRUE") || (create_run_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(create_run_ind).booleanValue());
        }
        if(create_session_ind != null
            && (create_session_ind.equalsIgnoreCase("TRUE") || (create_session_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(create_session_ind).booleanValue());
        }
        if(apply_ind != null
            && (apply_ind.equalsIgnoreCase("TRUE") || (apply_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(apply_ind).booleanValue());
        }
        if(run_ind != null
            && (run_ind.equalsIgnoreCase("TRUE") || (run_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(run_ind).booleanValue());
        }
        if(session_ind != null
            && (session_ind.equalsIgnoreCase("TRUE") || (session_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(session_ind).booleanValue());
        }
        if(has_attendance_ind != null
            && (has_attendance_ind.equalsIgnoreCase("TRUE") || (has_attendance_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(has_attendance_ind).booleanValue());
        }
        if(ji_ind != null
            && (ji_ind.equalsIgnoreCase("TRUE") || (ji_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(ji_ind).booleanValue());
        }
        if(completion_criteria_ind != null
            && (completion_criteria_ind.equalsIgnoreCase("TRUE") || (completion_criteria_ind.equalsIgnoreCase("FALSE")))) {
            stmt.setBoolean(index++, Boolean.valueOf(completion_criteria_ind).booleanValue());
        }
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            itemTypes.addElement(rs.getString("ITM_TYPES"));
        }
        return itemTypes;
    }


    public dbCourse getWZBCourse(Connection con) throws SQLException, cwException, cwSysMessage {
        dbCourse cos = null;
        if(this.itm_qdb_ind) {
            cos = new dbCourse();
            cos.cos_itm_id = this.itm_id;
            cos.cos_res_id = dbCourse.getCosResId(con, this.itm_id);
            cos.res_id = cos.cos_res_id;
            cos.res_type = dbResource.RES_TYPE_COS;
            try {
                cos.get(con);
            }
            catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
        }
        return cos;
    }

    /**
    init the param ITM_TYPES for item search:
    if the itm_type is integer,
    get the item type id (ity_id) from database by ity_seq_id.
    if itm_type is APPLICABLE, convert to organization's applicable item type.
    */
    private static void initSearchItemTypesParam(Connection con, Hashtable param, long owner_ent_id)
        throws SQLException, cwSysMessage {
        String[] itm_types = (String[]) param.get(ITM_TYPES);
        Vector vItemType = null; //store itm_types[i], if itm_types[i] is APPLICABLE, convert to organization's applicable item types
        Vector vApplicableItemType = null;  //store the applicable item type in organization
        if(itm_types == null || itm_types.length == 0) {
            param.put(ALL_ITM_TYPES,new Boolean(true));
            itm_types = DbItemType.getAllItemTypeIdInOrg(con, owner_ent_id);
        }
        else {
	    vItemType = new Vector();
            param.put(ALL_ITM_TYPES,new Boolean(false));
            int ity_seq_id = 0;
            DbItemType dbIty = new DbItemType();
            for(int i=0; i< itm_types.length; i++) {
                try {
                    //if this type is integer
                    //get the ity_id from database by its ity_seq_id
                    ity_seq_id = Integer.parseInt(itm_types[i]);
                    dbIty.ity_seq_id = ity_seq_id;
                    dbIty.ity_owner_ent_id = owner_ent_id;
                    dbIty.getItemTypeIdBySeqId(con);
                    vItemType.addElement(dbIty.ity_id);
                    itm_types[i] = dbIty.ity_id;
                }
                catch(NumberFormatException e) {
                    //if this type is APPLICABLE
                    //try to get organization's applicable item type if not get before
                    if(itm_types[i].equalsIgnoreCase(ITM_APPLICABLE_TYPE) && vApplicableItemType == null) {
                        vApplicableItemType = DbItemType.getApplicableItemType(con, owner_ent_id);
                        //Since the Maintain Attendance link cannot display
                        //CLASSROOM and SELFSTUDY at the same time
                        //thus only display the first item type
                        if(vApplicableItemType.size() > 0) {
                            vItemType.addElement(vApplicableItemType.elementAt(0));
                        }
                        /*
                        for(int j=0; j<vApplicableItemType.size(); j++) {
                            vItemType.addElement((String)vApplicableItemType.elementAt(j));
                        }
                        */
                    } else {
                        vItemType.addElement(itm_types[i]);
                    }
                }
            }
        }
        //if vApplicableItemType is not null,
        //need to convert vItemType to itm_types as
        //vItemType consists vApplicableItemType while itm_types does not
        if(vApplicableItemType != null) {
            itm_types = aeUtils.vec2StringArray(vItemType);
        }
        param.put(ITM_TYPES, itm_types);
        return;
    }

    private static void initSearchItemParam(Hashtable param, Timestamp cur_time) {

        Boolean temp_b;
        temp_b = (Boolean) param.get(ALL_IND);
        if(temp_b == null) {
            param.put(ALL_IND, new Boolean(true));
        }
        temp_b = (Boolean) param.get(ITM_SHOW_RUN_IND);
        if(temp_b == null) {
            param.put(ITM_SHOW_RUN_IND, new Boolean(true));
        }
        temp_b = (Boolean) param.get(ITM_ALLOW_NULL_DATETIME);
        if(temp_b == null) {
            param.put(ITM_ALLOW_NULL_DATETIME, new Boolean(false));
        }
        temp_b = (Boolean) param.get(ITM_EXIST_IN_PROGRESS_ATTENDANCE);
        if(temp_b == null) {
            param.put(ITM_EXIST_IN_PROGRESS_ATTENDANCE, new Boolean(false));
        }
        temp_b = (Boolean) param.get(ITM_FILTER_RETIRE_OR_EXIST_IN_PROGRESS_ATTENDANCE);
        if(temp_b == null) {
            param.put(ITM_FILTER_RETIRE_OR_EXIST_IN_PROGRESS_ATTENDANCE, new Boolean(false));
        }
        temp_b = (Boolean) param.get(ITM_SHOW_ATTENDANCE);
        if(temp_b == null) {
            param.put(ITM_SHOW_ATTENDANCE, new Boolean(false));
        }
        temp_b = (Boolean) param.get(EXACT);
        if(temp_b == null) {
            param.put(EXACT, new Boolean(false));
        }
        temp_b = (Boolean) param.get(R_EXACT);
        if(temp_b == null) {
            param.put(R_EXACT, new Boolean(false));
        }
        temp_b = (Boolean) param.get(FILTER_RETIRE);
        if(temp_b == null) {
            param.put(FILTER_RETIRE, new Boolean(false));
        }
        temp_b = (Boolean) param.get(ITM_SHOW_NO_RUN_PARENT);
        if(temp_b == null) {
            param.put(ITM_SHOW_NO_RUN_PARENT, new Boolean(true));
        }
        temp_b = (Boolean) param.get(SHOW_ORPHAN);
        if(temp_b == null) {
            param.put(SHOW_ORPHAN, new Boolean(true));
        }
        temp_b = (Boolean) param.get(ITM_SHOW_RESPON);
        if(temp_b == null) {
            param.put(ITM_SHOW_RESPON, new Boolean(false));
        }
        temp_b = (Boolean) param.get(CAT_PUBLIC_IND);
        if(temp_b == null) {
            param.put(CAT_PUBLIC_IND, new Boolean(false));
        }
        Long temp_l;
        temp_l = (Long) param.get(TND_ID);
        if(temp_l == null) {
            param.put(TND_ID, new Long(0));
        }
        temp_l = (Long) param.get(PAGE);
        if(temp_l == null) {
            param.put(PAGE, new Long(0));
        }
        temp_l = (Long) param.get(PAGE_SIZE);
        if(temp_l == null) {
            param.put(PAGE_SIZE, new Long(0));
        }
        Timestamp temp_appn_from = (Timestamp) param.get(ITM_APPN_FROM);
        Timestamp min_timestamp = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
        if(temp_appn_from != null && temp_appn_from.equals(min_timestamp)) {
            temp_appn_from = cur_time;
            param.put(ITM_APPN_FROM, cur_time);
        }
        Timestamp temp_appn_to = (Timestamp) param.get(ITM_APPN_TO);
        if(temp_appn_to != null && temp_appn_to.equals(min_timestamp)) {
            temp_appn_to = cur_time;
            param.put(ITM_APPN_TO, cur_time);
        }
        Timestamp temp_eff_from = (Timestamp) param.get(ITM_EFF_FROM);
        if(temp_eff_from != null && temp_eff_from.equals(min_timestamp)) {
            temp_eff_from = cur_time;
            param.put(ITM_EFF_FROM, cur_time);
        }
        Timestamp temp_eff_to = (Timestamp) param.get(ITM_EFF_TO);
        if(temp_eff_to != null && temp_eff_to.equals(min_timestamp)) {
            temp_eff_to = cur_time;
            param.put(ITM_EFF_TO, cur_time);
        }

        temp_b = (Boolean) param.get(ITM_ONLY_OPEN_ENROL_NOW);
        if(temp_b == null) {
            param.put(ITM_ONLY_OPEN_ENROL_NOW, new Boolean(false));
        }

        return;
    }

    private static final String sql_get_itm_life_status =
        " Select itm_life_status From aeItem Where itm_id = ?";
    /**
    Get itm_life_status of an item<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    private String getItemLifeStatus(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(sql_get_itm_life_status);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_life_status = rs.getString("itm_life_status");
        }
        stmt.close();
        return this.itm_life_status;
    }



	private String getAppWrkTplAsXML(Connection con, long usr_ent_id)
		throws SQLException, cwException {

			String xml = "";
			long app_id = aeApplication.getAppId(con, this.itm_id, usr_ent_id, true);
			aeApplication app = new aeApplication();
			app.app_id = app_id;
			if(app_id != 0) {
				try {
					app.get(con);
				}
				catch(qdbException e) {
					//Do Nothing
				}
			}
			String app_process_status = app.app_process_status;
			ViewItemTemplate viewIt = new ViewItemTemplate();
			viewIt.itemId = this.itm_id;
			long tplId = viewIt.getItemSelectedWorkflowTemplateId(con);
			ProcessStatus[] ps = null;
			try{
				Hashtable h_template_list = (Hashtable) aeWorkFlowCache.cachedSelfStatusList.get(this.app_rol_ext_id);
				Template tpl = (Template) h_template_list.get(Long.toString(tplId));
				ps = tpl.getStatusList();
			} catch(Exception e){
				//e.printStackTrace(System.out);
				xml = "<application_process/>";
			}

			if( ps != null && ps.length > 0 ) {
				for(int i=0; i<ps.length; i++){
					if( ps[i].getStatusName().equalsIgnoreCase(app_process_status) ) {
						xml = ps[i].getActionXML();
						break;
					}
				}
			}
			return xml;

		}

    /**
    Get the application id and process status as XML of the item
    or this item's run
    */
    private String getAppAsXML(Connection con, long ent_id)
        throws SQLException, cwException {

        boolean hasApplied = false;
        long app_id = aeApplication.getAppId(con, this.itm_id, ent_id, true);
        aeApplication app = new aeApplication();
        app.app_id = app_id;
        boolean canCancel = false;
        if(app_id != 0) {
            try {
                hasApplied = true;
                app.get(con);
                canCancel = app.canCancel(con);
            }
            catch(qdbException e) {
                //Do Nothing
            }
        }
        else {
            hasApplied = aeApplication.isExist(con, this.itm_id, ent_id);
        }
        String app_process_status = app.app_process_status;
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<application id=\"").append(aeUtils.escZero(app_id)).append("\"");
        xmlBuf.append(" process_status=\"").append(aeUtils.escNull(app_process_status)).append("\"");
        xmlBuf.append(" has_applied=\"").append(hasApplied).append("\" ");
        xmlBuf.append(" can_cancel=\"").append(canCancel).append("\"/>");
        return xmlBuf.toString();
    }

    private static final String sql_get_res_id_non_run =
        "SELECT cos_res_id FROM Course WHERE cos_itm_id = ?";
    private static final String sql_get_res_id_run =
        "select cos_res_id from aeItemRelation, Course " +
        "where ire_child_itm_id = ? " +
        "and cos_itm_id = ire_parent_itm_id ";
    /**
    Get cos_res_id of this item<BR>
    If this item is a run, get the cos_res_id of it's parent<BR>
    Pre-define variable:
    <ul>
    <li>itm_id
    </ul>
    */
    //in new version of on_line content,no need to get the rootItemId of a class
    //the class itself has its corresponding on_line content
    public long getResId(Connection con)
        throws SQLException
    {
        PreparedStatement stmt;
        ResultSet rs;
        long res_id = 0;
        //long rootItmId = getRootItemId(con, itm_id);

        stmt = con.prepareStatement(sql_get_res_id_non_run);
        //stmt.setLong(1, rootItmId);
		stmt.setLong(1, itm_id);
        rs = stmt.executeQuery();

        if (rs.next()) {
            res_id = rs.getLong("cos_res_id");
        }
        rs.close();
        stmt.close();
        return res_id;
    }
    
    public long getClassResId(Connection con) throws SQLException{
    	PreparedStatement stmt;
    	ResultSet rs;
    	long res_id = 0;
    	stmt = con.prepareStatement(sql_get_res_id_non_run);
    	stmt.setLong(1,itm_id);
    	rs = stmt.executeQuery();
    	if(rs.next()){
    		res_id = rs.getLong("cos_res_id");
    	}
    	rs.close();
    	stmt.close();
    	return res_id;
    }
    
    public long getResIdByRunId(Connection con,long run_itm_id) throws SQLException{
    	PreparedStatement stmt;
    	ResultSet rs;
    	long res_id = 0;
    	stmt = con.prepareStatement(sql_get_res_id_run);
    	stmt.setLong(1,run_itm_id);
    	rs = stmt.executeQuery();
    	if(rs.next()){
    		res_id = rs.getLong("cos_res_id");
    	}
    	rs.close();
    	stmt.close();
    	return res_id;
    }

    /**
    Get a Vector of item type (String) that can create run<BR>
    Will only look up table aeItem
    */
    public static Vector getCreateRunItemType(Connection con) throws SQLException {

        return getItemTypeByCreateRunInd(con, true);
    }

    private static final String sql_get_ity_by_create_run_ind =
        "select itm_type from aeItem where itm_create_run_ind = ? group by itm_type";
    /**
    Get a Vector of item type (String) by create_run_ind<BR>
    Will only look up table aeItem
    */
    private static Vector getItemTypeByCreateRunInd(Connection con, boolean create_run_ind) throws SQLException {

        Vector v_ity = new Vector();
        PreparedStatement stmt = con.prepareStatement(sql_get_ity_by_create_run_ind);
        stmt.setBoolean(1, create_run_ind);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v_ity.addElement(rs.getString("itm_type"));
        }
        stmt.close();
        return v_ity;
    }

    public static long getLastVersion(Connection con, long itm_id) throws SQLException {
        long last_itm_id = 0;
        PreparedStatement stmt = con.prepareStatement("select item2.itm_id from aeItem item1, aeItem item2 where item1.itm_id = ? and item1.itm_code = item2.itm_code and item2.itm_deprecated_ind = 0");
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            last_itm_id = rs.getLong("itm_id");
        }

        stmt.close();

        return last_itm_id;
    }

    private boolean isItemEffEndPassed(Timestamp cur_time) {
        boolean result;
        if(this.itm_eff_end_datetime == null) {
            result = true;
        }
        else result = this.itm_eff_end_datetime.before(cur_time);
        return result;
    }

    /**
    Pre-define variables
    <ul>
    <li>itm_id
    <li>itm_create_run_ind
    </ul>
    */
    private boolean hasPendingAppn(Connection con) throws SQLException {
        return (aeApplication.hasPendingAppnByItem(con, this.itm_id,
                 this.itm_create_run_ind));
    }

    /**
    Pre-define variables
    <ul>
    <li>itm_id
    </ul>
    */
    private boolean isMoteCompleted(Connection con) throws SQLException {
        boolean completed = true;
        Mote mote = new Mote();
        Vector v_mote = mote.getMoteLstByItm(con, this.itm_id);
        for(int i=0; i<v_mote.size(); i++) {
            mote = (Mote) v_mote.elementAt(i);
            if(mote.status.equalsIgnoreCase(Mote.STATUS_PROGRESS)) {
                completed = false;
                break;
            }
        }
        return completed;
    }

    /**
    Pre-define variables
    <ul>
    <li>itm_id
    <li>itm_create_run_ind
    <li>itm_eff_end_datetime
    </ul>
    */
    private boolean isItemEnd(Connection con, Vector v_run_itm_id)
        throws SQLException, cwSysMessage {
        //check item eff end date
        boolean completed = true;
        Timestamp cur_time = cwSQL.getTime(con);
        if(!this.itm_create_run_ind) {
            completed = (isItemEffEndPassed(cur_time)
                        || (this.itm_life_status != null && this.itm_life_status.equalsIgnoreCase(ITM_LIFE_STATUS_CANCELLED)));
        }
        else {
            if(v_run_itm_id == null) {
                v_run_itm_id = aeItemRelation.getChildItemId(con, this.itm_id);
            }
            for(int i=0; i<v_run_itm_id.size(); i++) {
                aeItem run = new aeItem();
                run.itm_id = ((Long)v_run_itm_id.elementAt(i)).longValue();
                run.getItem(con);
                completed = (run.isItemEffEndPassed(cur_time)
                            || (run.itm_life_status != null && run.itm_life_status.equalsIgnoreCase(ITM_LIFE_STATUS_CANCELLED)));
                if(completed == false) break;
            }
        }
        return completed;
    }

    /**
    Pre-define variables
    <ul>
    <li>itm_id
    <li>itm_create_run_ind
    <li>itm_owner_ent_id
    </ul>
    */
    // nobody use?

    private boolean isAttendanceCompleted(Connection con, Vector v_run_itm_id)
        throws SQLException, cwException, cwSysMessage {
        boolean completed = true;
        //check item attendance
        int ats_id = aeAttendanceStatus.getIdByType(con, this.itm_owner_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
        if(!this.itm_create_run_ind) {

            completed = (aeAttendance.countStatus(con, this.itm_id, ats_id) == 0);
        }
        else {
            if(v_run_itm_id == null) {
                v_run_itm_id = aeItemRelation.getChildItemId(con, this.itm_id);
            }
            for(int i=0; i<v_run_itm_id.size(); i++) {
                aeItem run = new aeItem();
                run.itm_id = ((Long)v_run_itm_id.elementAt(i)).longValue();
                run.getItem(con);
                completed = (aeAttendance.countStatus(con, run.itm_id, ats_id) == 0);
                if(completed == false) break;
            }
        }
        return completed;
    }

    private boolean isItemCompleted(Connection con, Timestamp cur_time)
        throws SQLException, cwException, cwSysMessage {
        //getItem(con);
        boolean completed = true;
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        //MOTE complete
        Mote mote = new Mote();
        Vector v_mote = mote.getMoteLstByItm(con, this.itm_id);
        for(int i=0; i<v_mote.size(); i++) {
            mote = (Mote) v_mote.elementAt(i);
            if(mote.status.equalsIgnoreCase(Mote.STATUS_PROGRESS)) {
                completed = false;
                break;
            }
        }
        if(completed == true) {
            //check item eff end date && in progress attendance
            int ats_id = aeAttendanceStatus.getIdByType(con, this.itm_owner_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
            if(!this.itm_create_run_ind) {

                completed = (isItemEffEndPassed(cur_time) || (this.itm_life_status != null && this.itm_life_status.equalsIgnoreCase(ITM_LIFE_STATUS_CANCELLED))) && (aeAttendance.countStatus(con, this.itm_id, ats_id) == 0);
            }
            else {
                Vector v_run_itm_id = aeItemRelation.getChildItemId(con, this.itm_id);
                for(int i=0; i<v_run_itm_id.size(); i++) {
                    aeItem run = new aeItem();
                    run.itm_id = ((Long)v_run_itm_id.elementAt(i)).longValue();
                    run.getItem(con);
                    completed = (run.isItemEffEndPassed(cur_time)
                                || (run.itm_life_status != null && run.itm_life_status.equalsIgnoreCase(ITM_LIFE_STATUS_CANCELLED)))
                                && (aeAttendance.countStatus(con, run.itm_id, ats_id) == 0);
                    if(completed == false) break;
                }
            }
        }
        //pending application
        if(completed == true) {
            completed = !(aeApplication.hasPendingAppnByItem(con, this.itm_id,
                                                        this.itm_create_run_ind));
        }
        return completed;
    }

    // the list includes the item that contains pending application ONLY
    public static String getJIStatusAsXML(Connection con, Vector itm_vec) throws SQLException {
        StringBuffer result = new StringBuffer();

        result.append("<ji_not_completed_list>");

        if (itm_vec != null && itm_vec.size() != 0) {
            PreparedStatement stmt = con.prepareStatement("select distinct app_itm_id from aeApplication where app_itm_id in " + cwUtils.vector2list(itm_vec) + " and app_status = 'Admitted' ");
            ResultSet rs = stmt.executeQuery();
            Vector admitted_itm_vec = new Vector();

            while (rs.next()) {
                admitted_itm_vec.addElement(new Long(rs.getLong("app_itm_id")));
            }

            stmt.close();

            for (int i=0; i<itm_vec.size(); i++) {
                Long itm_id = (Long)itm_vec.elementAt(i);

                if (!admitted_itm_vec.contains(itm_id)) {
                    result.append("<itm id=\"").append(itm_id.longValue()).append("\"/>");
                }
            }

            if (admitted_itm_vec != null && admitted_itm_vec.size() != 0) {
                stmt = con.prepareStatement("select distinct app_itm_id from aeApplication where app_itm_id in " + cwUtils.vector2list(admitted_itm_vec) + " and app_notify_status = 0 and app_status = 'Admitted' ");
                rs = stmt.executeQuery();
//System.out.println("select app_itm_id, count(app_id) as cnt from aeApplication where app_itm_id in " + cwUtils.vector2list(itm_vec) + " and app_notify_status = 0 and app_status = 'Admitted' group by app_itm_id ");

                while (rs.next()) {
                    result.append("<itm id=\"").append(rs.getLong("app_itm_id")).append("\"/>");
                }

                stmt.close();
            }
        }

        result.append("</ji_not_completed_list>");

        return result.toString();
    }

    private static final String sql_get_run_info_4_enrol_assign =
        " Select itm_id, itm_title, itm_capacity, itm_eff_start_datetime, itm_appn_end_datetime, " +
        " itm_appn_start_datetime, itm_apply_ind " +
        " From aeItem Where itm_id in ";
    public static String getRunInfo4EnrolAssignmentAsXML(Connection con, Vector v_itm_id, Timestamp cur_time)
        throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        xmlBuf.append("<run_item_list>");
        if(v_itm_id != null && v_itm_id.size() > 0) {
            String sql_itm_id = cwUtils.vector2list(v_itm_id);
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Hashtable itmId_appnCnt_pair = new Hashtable();

            stmt = con.prepareStatement("SELECT app_itm_id, COUNT(*) AS CNT FROM aeApplication WHERE app_itm_id in " + sql_itm_id + " and app_status not in (?, ?) GROUP BY app_itm_id ");
            stmt.setString(1, aeApplication.REJECTED);
            stmt.setString(2, aeApplication.WITHDRAWN);
            rs = stmt.executeQuery();

            while(rs.next()) {
                Long itm_id = new Long(rs.getLong("app_itm_id"));
                Long appn_cnt = new Long(rs.getLong("cnt"));
                itmId_appnCnt_pair.put(itm_id, appn_cnt);
            }

            stmt.close();
            stmt = con.prepareStatement(sql_get_run_info_4_enrol_assign + sql_itm_id);
            rs = stmt.executeQuery();

            while(rs.next()) {
                Timestamp appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
                Timestamp appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
                xmlBuf.append("<run_item id=\"").append(rs.getLong("itm_id")).append("\"")
                      .append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("itm_title")))).append("\"")
                      .append(" capacity=\"").append(rs.getLong("itm_capacity")).append("\"")
                      .append(" eff_start_datetime=\"").append(rs.getTimestamp("itm_eff_start_datetime")).append("\"")
                      .append(" apply_ind=\"").append(rs.getBoolean("itm_apply_ind")).append("\"")
                      .append(" appn_started_ind=\"").append(appn_start_datetime == null || appn_start_datetime.before(cur_time)).append("\"")
                      .append(" appn_closed_ind=\"").append(appn_end_datetime != null && appn_end_datetime.before(cur_time)).append("\"")
                      .append(" enrollment=\"");

                Long appnCnt = (Long)itmId_appnCnt_pair.get(new Long(rs.getLong("itm_id")));

                if (appnCnt != null) {
                    xmlBuf.append(appnCnt.longValue());
                } else {
                    xmlBuf.append("0");
                }

                xmlBuf.append("\"/>");
            }

            stmt.close();
        }
        xmlBuf.append("</run_item_list>");
        return xmlBuf.toString();
    }

    private static final String sql_unlink_rsv =
        " Update aeItem set itm_rsv_id = null"+
       //" itm_eff_start_datetime = null, " +
       // " itm_eff_end_datetime = null " +
        " Where itm_rsv_id = ? ";
    /**
    Interface function for FM.
    Used to unlink reservation from item
    @return number of records updated
    */
    public int unlinkRsv(int rsv_id) throws SQLException {
        PreparedStatement stmt = this.con.prepareStatement(sql_unlink_rsv);
        stmt.setInt(1, rsv_id);
        int cnt = stmt.executeUpdate();
        stmt.close();
        return cnt;
    }
/*
    private static final String sql_refresh_rsv =
        " Update aeItem set " +
        " itm_eff_start_datetime = ?, " +
        " itm_eff_end_datetime = ? " +
        " Where itm_rsv_id = ? ";
  */
      /**
    Interface function for FM.
    Used to update redundant data in aeItem
    @return number of records updated
    */
    public int refreshRsv(int rsv_id, Timestamp start_date, Timestamp end_date) throws SQLException {
     /* 
       PreparedStatement stmt = this.con.prepareStatement(sql_refresh_rsv);
        if(start_date != null) {
            stmt.setTimestamp(1, start_date);
        } else {
            stmt.setNull(1, Types.TIMESTAMP);
        }
        if(end_date != null) {
            stmt.setTimestamp(2, end_date);
        } else {
            stmt.setNull(2, Types.TIMESTAMP);
        }
        stmt.setInt(3, rsv_id);
        int cnt = stmt.executeUpdate();
        stmt.close();
        return cnt;
        */
        return 1;
    }

    private static final String sql_get_info_4_cancel_rsv =
        "Select itm_rsv_id, itm_owner_ent_id from aeItem where itm_id = ? ";
    private void getInfo4CancelRsv(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_get_info_4_cancel_rsv);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_rsv_id = rs.getLong("itm_rsv_id");
            this.itm_owner_ent_id = rs.getLong("itm_owner_ent_id");
        }
        stmt.close();
        return;
    }

    private Vector getCascadeUpdInfo(Connection con) throws SQLException {
        Vector v_info = new Vector();
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" Select ").append(cwSQL.replaceNull("itm_capacity", "-100")).append(" as itm_capacity, ")
              .append(cwSQL.replaceNull("itm_min_capacity", "-100")).append(" as itm_min_capacity, ")
              .append(cwSQL.replaceNull("itm_unit", "-100")).append(" as itm_unit ")
              .append(" From aeItem ")
              .append(" Where itm_id = ? ");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            v_info.addElement(new Long(rs.getLong("itm_capacity")));
            v_info.addElement(new Long(rs.getLong("itm_min_capacity")));
            v_info.addElement(new Float(rs.getFloat("itm_unit")));
        }
        stmt.close();
        return v_info;
    }

    private String getCascadeUpdInfoAsXML(Connection con) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        Vector v_info = getCascadeUpdInfo(con);
        if(v_info.size() > 0) {
            long capacity = ((Long)v_info.elementAt(0)).longValue();
            long min_capacity = ((Long)v_info.elementAt(1)).longValue();
            float unit = ((Float)v_info.elementAt(2)).floatValue();
            xmlBuf.append("<capacity>")
                    .append("<num value=\"").append((capacity == -100) ? "" : capacity+"").append("\"/>")
                    .append("</capacity>");
            xmlBuf.append("<min_capacity>")
                    .append("<num value=\"").append((min_capacity == -100) ? "" : min_capacity+"").append("\"/>")
                    .append("</min_capacity>");
            xmlBuf.append("<unit>")
                    .append("<num value=\"").append((unit == -100) ? "" : unit+"").append("\"/>")
                    .append("</unit>");
        }
        return xmlBuf.toString();
    }


    private String getWorkflowTplIdAsXML(Connection con) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(256);
        ViewItemTemplate viewIt = new ViewItemTemplate();
        viewIt.itemId = this.itm_id;
        xmlBuf.append("<workflow_template>")
              .append("<id value=\"").append(viewIt.getItemSelectedWorkflowTemplateId(con)).append("\" />")
              .append("</workflow_template>");
        return xmlBuf.toString();
    }


    private static final String sql_upd_successor_capacity =
        " Update aeItem Set itm_capacity = ?, itm_min_capacity = ?, itm_unit = ? " +
        " Where itm_id in ";

    private void updRunCapacity(Connection con) throws SQLException{
        updSuccessorCapacity(con);
    }
    private void updSuccessorCapacity(Connection con) throws SQLException {
        Vector v_successor_itm_id = getSuccessorItemId(con, itm_id);

        if(v_successor_itm_id != null && v_successor_itm_id.size() > 0) {
            String sql_sucessor_itm_id = cwUtils.vector2list(v_successor_itm_id);
            Vector v_info = getCascadeUpdInfo(con);
            long capacity = ((Long)v_info.elementAt(0)).longValue();
            long min_capacity = ((Long)v_info.elementAt(1)).longValue();
            float unit = ((Float)v_info.elementAt(2)).floatValue();
            String SQL = sql_upd_successor_capacity + sql_sucessor_itm_id;
            PreparedStatement stmt = con.prepareStatement(SQL);
            if(capacity == -100) {
                stmt.setNull(1, Types.DOUBLE);
            } else {
                stmt.setLong(1, capacity);
            }
            if(min_capacity == -100) {
                stmt.setNull(2, Types.DOUBLE);
            } else {
                stmt.setLong(2, min_capacity);
            }
            if(unit == -100) {
                stmt.setNull(3, Types.FLOAT);
            } else {
                stmt.setFloat(3, unit);
            }
            stmt.executeUpdate();
            stmt.close();
        }
        return;
    }

    private static final String sql_has_rsv_propriety =
    " select count(*) " +
    " from aeItem, aeItemTemplate, aeTemplate, aeTemplateView, aeTemplateType " +
    " where itm_id = ? " +
    " and itp_itm_id = itm_id " +
    " and itp_ttp_id = ttp_id " +
    " and ttp_title = ? " +
    " and itp_tpl_id = tpl_id " +
    " and tvw_tpl_id = tpl_id " +
    " and tvw_rsv_ind = ? ";
    /**
    Check if this item has Facility Management propriety by looking at tvw_rsv_ind from aeTemplateView
    */
    public boolean hasRsvPropriety(Connection con) throws SQLException {

        boolean result;
        PreparedStatement stmt = con.prepareStatement(sql_has_rsv_propriety);
        stmt.setLong(1, this.itm_id);
        stmt.setString(2, aeTemplate.ITEM);
        stmt.setBoolean(3, true);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            result = (rs.getLong(1) > 0);
        } else {
            result = false;
        }
        stmt.close();
        return result;
    }

    public void discontinueVersion(Connection con, String usr_id, Timestamp lastUpdTime) throws SQLException, cwException, cwSysMessage {
        Timestamp curTime = cwSQL.getTime(con);

        if (!this.itm_upd_timestamp.equals(lastUpdTime)) {
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        }

        if(!isItemCompleted(con, curTime)) {
            throw new cwSysMessage(ITM_NOT_COMPLETE_FOR_DISCONTINUE);
        }

        //update tree nodes cnt and on_cnt that attached to the this item
        try {
            cascadeUpdTndCnt(con, -1);
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }

        PreparedStatement stmt = con.prepareStatement("update aeItem set itm_deprecated_ind = ?, itm_status = ?, itm_life_status = ?, itm_upd_usr_id = ?, itm_upd_timestamp = ? where itm_id = ?");
        stmt.setBoolean(1, true);
        stmt.setString(2, ITM_STATUS_OFF);
        stmt.setString(3, ITM_LIFE_STATUS_DISCONTINUE);
        stmt.setString(4, usr_id);
        stmt.setTimestamp(5, curTime);
        stmt.setLong(6, itm_id);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Checks if the total number of "active" items
     * exceeded the maximum number of items allowed in this organization
     *
     * 1. The restriction is applied to "active" items only:
     * Physically deleted items, cancelled items (i.e. cancelled runs),
     * deprecated items (i.e. those generated by new version) and items
     * that can create runs, will NOT be counted in the sum.
     *
     * 2. The restriction is applied to each organization individually:
     * There can be at most N "active" items within a single organization,
     * but total number of "active" items within the whole system
     * CAN exceed N if there are more than one organization.
     *
     * @author  kawai
     * @version 2002.03.26
     * @param   inCon    the Connection object used by this method
     * @param   inSiteID the entity ID of the organization to be queried
     * @return  <i>true</i> indicates if the current total number of items has reached the limit; <i>false</i> otherwise
     * @exception   SQLException    if an error occured during sql operation
     * @exception   cwException     if a never expected error occured
     * @exception   cwSysMessage    if a value validation violation occured
     */
    private static final String sql_get_active_item_count =
        "SELECT count(*) FROM aeItem WHERE "
      + "itm_owner_ent_id = ? "
      + "AND itm_create_run_ind = 0 "
      + "AND itm_deprecated_ind = 0 "
      + "AND (itm_life_status <> ? OR itm_life_status IS NULL) "
      // added by richard
      + "AND itm_status = ? ";
    public boolean isActiveItemCntExceedLimit(Connection inCon, long inSiteID)
        throws SQLException, cwException, cwSysMessage
    {
        // get the current total number of items
        int cur_itm_cnt = 0;
        int col = 1;
        PreparedStatement stmt = inCon.prepareStatement(sql_get_active_item_count);
        stmt.setLong(col++, inSiteID);
        stmt.setString(col++, ITM_LIFE_STATUS_CANCELLED);
        stmt.setString(col++, ITM_STATUS_ON);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            col = 1;
            cur_itm_cnt = rs.getInt(col++);
        } else {
            throw new cwException("Failed to get the total number of items for restriction checking.(Site ID:" + inSiteID + ")");
        }
        stmt.close();

        // get the maximum number of items allowed in this organization
        int max_itm_cnt = acSite.getMaxItemsAllowed(inCon, inSiteID);
        // return true if current total number equals to
        // or more than maximum number of items allowed
        boolean exceeded = false;
        exceeded = cur_itm_cnt >= max_itm_cnt;

        return exceeded;
    }

    // for message level code
    public static final String MESSAGE_LEVEL = "MessageLevel";

    public String getMsgLevelLst(Connection con, loginProfile prof)
        throws SQLException, cwSysMessage {
        // get level list as XML
        String[] ctb_types = { MESSAGE_LEVEL };
        StringBuffer xmlBufLevelList = new StringBuffer(1024);
        // static method getAll(con ,ctb_types) in CodeTable
        xmlBufLevelList.append("<level_list>");
        xmlBufLevelList.append(CodeTable.getAll(con ,ctb_types));
        xmlBufLevelList.append("</level_list>");

        return xmlBufLevelList.toString();
    }

    /**
     * get the certificate id of the item
     * @param con
     * @return
     * @throws SQLException
     * @throws cwException
     */
    public int getCertificateId(Connection con) throws SQLException, cwException {
        String sqlStr = "SELECT itm_ctf_id FROM aeItem WHERE itm_id = ?";
        PreparedStatement stmt = con.prepareStatement(sqlStr);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        int ctf_id = 0;
        if (rs.next()) {
            ctf_id = rs.getInt("itm_ctf_id");
        } else {
            throw new cwException("Failed to get the certificate id of the item. (itm_id: )" + itm_id);
        }
        stmt.close();

        return ctf_id;
    }
    public boolean isQualified(int ctf_id, int ent_id) throws SQLException {
        //
        //
        String sqlStr = "select ats_type from aeAttendance, aeAttendanceStatus,aeApplication,aeItem " +
               "where ats_id = att_ats_id and att_app_id = app_id and app_itm_id = att_itm_id and itm_id = app_itm_id and itm_ctf_id = ? and app_ent_id = ?";
        PreparedStatement stmt = con.prepareStatement(sqlStr);
        int count = 0;
        stmt.setInt(++count, ctf_id);
        stmt.setInt(++count, ent_id);
        ResultSet rs = stmt.executeQuery();
        boolean qualification = false;
        while (rs.next()) {
            qualification = (rs.getString("ats_type").equals("ATTEND"))? true:false;
        }
        stmt.close();

        return qualification;
    }

    // add for skipping payment event - dennis
    private static final String sql_get_itm_fee =
    " Select itm_fee, itm_fee_ccy From aeItem where itm_id = ? ";
    /**
    Get this itm_fee, itm_fee_ccy into instance variable and return itm_fee
    */
    public float getItemFee(Connection con) throws SQLException {
    PreparedStatement stmt = con.prepareStatement(sql_get_itm_fee);
    stmt.setLong(1, this.itm_id);
    ResultSet rs = stmt.executeQuery();
    if(rs.next()) {
        this.itm_fee = rs.getFloat("itm_fee");
        this.itm_fee_ccy = rs.getString("itm_fee_ccy");
    } else {
        this.itm_fee = 0;
        this.itm_fee_ccy = null;
    }
    rs.close();
    stmt.close();
    return this.itm_fee;
    }

    /**
     * get all the catalog/Category this item has been attached to
     * 2002.05.06 chris
     */

    public static Hashtable getCatalogXMLHash(Connection con, Vector itmVec) throws SQLException, cwSysMessage {
        String tableName = null;
        try {
            String colName = "tmp_itm_id";
            tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, itmVec, cwSQL.COL_TYPE_LONG);
            String sql_get_catalog =
            " SELECT CATA.tnd_id AS CATID, CATA.tnd_title AS CATTITLE, ITM.tnd_itm_id AS ITMID from aeTreeNode CATA, aeTreeNode ITM, "
            + tableName
            + " WHERE CATA.tnd_id = ITM.tnd_parent_tnd_id "
            + " AND ITM.tnd_itm_id = " + colName
            + " ORDER BY ITM.tnd_itm_id, CATA.tnd_title ";

            PreparedStatement stmt = null;
            ResultSet rs = null;

            // get all the nodes that are pointing to this item
            stmt = con.prepareStatement(sql_get_catalog);
            rs = stmt.executeQuery();

            long prv_itm_id = 0;
            long cur_itm_id = 0;
            Hashtable itmCatalogXMLHash = new Hashtable();

            StringBuffer result = new StringBuffer();
            while (rs.next()) {
                cur_itm_id = rs.getLong("ITMID");
                if (cur_itm_id != prv_itm_id) {
                    if (prv_itm_id > 0) {
                        result.append("</node_list>");
                        result.append("</catalog>");
                        itmCatalogXMLHash.put(new Long(prv_itm_id), result.toString());
                    }
                    result.setLength(0);
                    result.append("<catalog>");
                    result.append("<node_list>");
                }

                result.append("<node id=\"").append(rs.getLong("CATID")).append("\">").append(cwUtils.NEWL)
                    .append("<title>").append(cwUtils.esc4XML(rs.getString("CATTITLE"))).append("</title>").append(cwUtils.NEWL)
                    .append("</node>");

                prv_itm_id = cur_itm_id;
            }

            if (prv_itm_id > 0) {
                result.append("</node_list>");
                result.append("</catalog>");
                itmCatalogXMLHash.put(new Long(prv_itm_id), result.toString());
            }

            stmt.close();
            return itmCatalogXMLHash;
        }
        finally {
            if(tableName != null) {
                cwSQL.dropTempTable(con, tableName);
            }
        }
    }
    /** retired
    */
    private static boolean isUniqueTitle(Connection con, long itm_id, long parent_itm_id, String itm_type, long owner_ent_id, boolean itm_run_ind, String itm_title) throws SQLException {
        return isUniqueTitle(con, itm_id, parent_itm_id, itm_type, owner_ent_id, itm_run_ind, false, itm_title);
    }
    private static boolean isUniqueTitle(Connection con, long itm_id, long parent_itm_id, String itm_type, long owner_ent_id, boolean itm_run_ind, boolean itm_session_ind, String itm_title) throws SQLException {
/*System.out.println(">>>>>>>>>>>>>> run ind = " + itm_run_ind);
System.out.println(">>>>>>>>>>>>>> type = " + itm_type);
System.out.println(">>>>>>>>>>>>>> itm_id = " + itm_id);
System.out.println(">>>>>>>>>>>>>> parent = " + parent_itm_id);
System.out.println(">>>>>>>>>>>>>> title = " + itm_title);*/
        StringBuffer SQLBuf = new StringBuffer();
        boolean isUniqueTitle = true;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        if (itm_run_ind || itm_session_ind) {
            if (parent_itm_id == 0) {
                SQLBuf.append("select itm_title from aeItem, aeItemRelation r1, aeItemRelation r2 where r1.ire_child_itm_id = ? and r2.ire_parent_itm_id = r1.ire_parent_itm_id and r2.ire_child_itm_id = itm_id and itm_life_status is null and itm_title = ?");
            } else {
                SQLBuf.append("select itm_title from aeItem, aeItemRelation where ire_parent_itm_id = ? and ire_child_itm_id = itm_id and itm_life_status is null and itm_title = ?");
            }

            if (itm_id != 0) {
                SQLBuf.append(" and itm_id <> ").append(itm_id);
            }

            int index = 1;
            stmt = con.prepareStatement(SQLBuf.toString());

            if (parent_itm_id == 0) {
                stmt.setLong(index++, itm_id);
            } else {
                stmt.setLong(index++, parent_itm_id);
            }

            stmt.setString(index++, itm_title);
            rs = stmt.executeQuery();

            if (rs.next()) {
                isUniqueTitle = false;
            }
        } else {
            SQLBuf.append("select itm_title cnt from aeItem where itm_deprecated_ind = 0 and itm_run_ind = ? and itm_session_ind = ? and itm_type = ? and itm_owner_ent_id = ? and itm_title = ?");

            if (itm_id != 0) {
                SQLBuf.append(" and itm_id <> ").append(itm_id);
            }

            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setBoolean(1, false);
            stmt.setBoolean(2, false);
            stmt.setString(3, itm_type);
            stmt.setLong(4, owner_ent_id);
            stmt.setString(5, itm_title);
            rs = stmt.executeQuery();

            if (rs.next()) {
                isUniqueTitle = false;
            }

        }

        stmt.close();

        return isUniqueTitle;
    }


    public Vector getItemListByType(Connection con, String[] item_type)
        throws SQLException{

            String SQL = " SELECT itm_id, itm_code FROM aeItem ";
            if( item_type != null && item_type.length >= 0 ) {
                SQL += " WHERE itm_type IN ( ? ";
                for(int i=1; i<item_type.length; i++)
                    SQL += " ,? ";
                SQL += " ) ";
            }

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            for(int i=0; i<item_type.length; i++)
                stmt.setString(index++, item_type[i]);

            ResultSet rs = stmt.executeQuery();
            aeItem aeItm = null;
            Vector itemVec = new Vector();
            while(rs.next()){
                aeItm = new aeItem();
                aeItm.itm_id = rs.getLong("itm_id");
                aeItm.itm_code = rs.getString("itm_code");
                itemVec.add(aeItm);
            }
            stmt.close();
            return itemVec;
        }

    private static final String SQL_GET_ITM_ID =
        "select itm_id from aeItem where itm_deprecated_ind = ? and itm_code = ? and itm_owner_ent_id = ? ";
    /**
    Get Item id of the latest version of item by itm_code and itm_owner_ent_id
    Predefine variable: itm_code, itm_owner_ent_id
    @return item id find, or 0 if no such item found
    */
    public long getItemId(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_ITM_ID);
            stmt.setBoolean(1, false);
            stmt.setString(2, this.itm_code);
            stmt.setLong(3, this.itm_owner_ent_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                this.itm_id = rs.getLong("itm_id");
            } else {
                this.itm_id = 0;
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return this.itm_id;
    }

    /**
    Generate an itm_xml for this item based on its item tempalte and argument data.
    Pre-defined variable: itm_id (0 if you want to generate itm_xml for a new item),
                          itm_owner_ent_id,
                          itm_type
                          itm_run_ind
                          itm_session_ind

    @param con Connnection to database
    @param data Hashtable contains data of this item needed to be constructed into itm_xml. e.g. ("itm_title", "I am Item Title")
    @return itm_xml generated
    */
    public String generateItemXML(Connection con, Hashtable data) throws SQLException, IOException, cwException {
        getItemXML(con);
        if(this.itm_xml == null || this.itm_xml.length() == 0) {
            this.itm_xml = "<detail></detail>";
        }
        String tpl_xml = null;
        aeTemplate tpl = null;
        if(this.itm_id != 0) {
            tpl = getItemTemplate(con);
        } else {
            tpl = new aeTemplate();
            ViewItemTemplate viItmTpl = new ViewItemTemplate();
            viItmTpl.ownerEntId = itm_owner_ent_id;
            viItmTpl.itemType = itm_type;
            viItmTpl.templateType = aeTemplate.ITEM;
            viItmTpl.runInd = itm_run_ind;
            viItmTpl.sessionInd = itm_session_ind;
            tpl.tpl_id = viItmTpl.getFirstTplId(con);
            tpl.get(con);
        }
        // get field options
        Hashtable htCheckbox = new Hashtable();
        Hashtable htRadio = new Hashtable();
        Hashtable htSelect = new Hashtable();

        tpl.getOptionFields_(con, tpl.tpl_xml, htCheckbox, htRadio, htSelect);

        Hashtable paramField = tpl.getParamNameFields();
        paramField.put("itm_retake_ind", "field16");
        String paramname;
        String fieldname;
        String fielddata;
        Enumeration key = paramField.keys();
        Perl5Util perl = new Perl5Util();
        String perlString = new String();
        while(key.hasMoreElements()) {
            paramname = (String) key.nextElement();
            fielddata = (String) data.get(paramname);
            fieldname = (String) paramField.get(paramname);
            StringBuffer xmlBuf = new StringBuffer(128);
            if(fieldname != null && fielddata != null) {
                if (htCheckbox.containsKey(fieldname) || htRadio.containsKey(fieldname)){
                    Hashtable htOption = null;
                    if (htCheckbox.containsKey(fieldname)){
                        htOption = (Hashtable)htCheckbox.get(fieldname);
                    }else if (htRadio.containsKey(fieldname)){
                        htOption = (Hashtable)htRadio.get(fieldname);
                    }else{
                        throw new cwException("ERROR IN aeItem.generateItemXML : INVALID OPTION TYPE");
                    }
                    xmlBuf.append("<").append(fieldname).append(">");
                    xmlBuf.append("<").append(fieldname)
                        .append(" id=\"")
                        .append(htOption.get(fielddata)).append("\">")
                        .append(fielddata)
                        .append("</").append(fieldname).append(">");
                    xmlBuf.append("</").append(fieldname).append(">");

                }else if (htSelect.containsKey(fieldname)){
                    Hashtable htOption = (Hashtable)htSelect.get(fieldname);

                    xmlBuf.append("<").append(fieldname)
                        .append(" id=\"")
                        .append(htOption.get(fielddata)).append("\">")
                        .append(fielddata)
                        .append("</").append(fieldname).append(">");
                }else{
                    xmlBuf.append("<").append(fieldname).append(">");
                    xmlBuf.append(cwUtils.esc4XML(fielddata));
                    xmlBuf.append("</").append(fieldname).append(">");
                }
            }else if(fieldname == "field03" || fieldname == "field53" || fieldname == "field55"){
            	xmlBuf.append("<").append(fieldname).append(">");
            	xmlBuf.append("<subfield_list><subfield id=\"1\">").append(data.get("itm_appn_start_datetime")).append("</subfield>");
                if(data.get("itm_appn_end_datetime") !=null && Timestamp.valueOf(cwUtils.MAX_TIMESTAMP).toString().equals(data.get("itm_appn_end_datetime").toString())){
                	xmlBuf.append("<subfield id=\"2\">").append(cwUtils.MAX_TIMESTAMP).append("</subfield></subfield_list>");
                } else {
                	xmlBuf.append("<subfield id=\"2\">").append(data.get("itm_appn_end_datetime")).append("</subfield></subfield_list>");
                }
                xmlBuf.append("</").append(fieldname).append(">");
            }else{
            	 xmlBuf.append("<").append(fieldname).append("/>");
            }
                        
            perlString = "#<" + fieldname + ">(\\w|\\W)*</" + fieldname + ">#i";
            if(perl.match(perlString, this.itm_xml)) {
                perlString = "s#<" + fieldname + ">(\\w|\\W)*</" + fieldname + ">#" + cwUtils.escHash(xmlBuf.toString()) + "#i";
            } else {
                perlString = "s#</detail>#" + cwUtils.escHash(xmlBuf.toString()) + "</detail>#i";
            }
            this.itm_xml = perl.substitute(perlString, this.itm_xml);
        }
        return this.itm_xml;
    }

    private static final String SQL_GET_NOT_SYN_ITM_ID_N_UPD_T =
        " select itm_id, itm_upd_timestamp from aeItem " +
        " where itm_owner_ent_id = ? " +
        " and itm_type = ? " +
        " and (itm_syn_timestamp < ? OR itm_syn_timestamp IS NULL )";
    /**
    Get the not-in-syn item id and last update timestamp from aeItem
    @param con Connection to database
    @param synTimestamp start syn timestamp
    @param owner_ent_id organization site id
    @param itm_type item type to look at
    @return Hashtable with itm_id as key and itm_upd_timestamp as value
    */
    public static Hashtable getNotInSynItemIdNUpdTimestamp(Connection con,
                                                           Timestamp synTimestamp,
                                                           long owner_ent_id,
                                                           String itm_type, Boolean isRun)
        throws SQLException {
        return getNotInSynItemIdNUpdTimestamp(con, synTimestamp, owner_ent_id, itm_type, isRun, new Boolean(false));
    }

    public static Hashtable getNotInSynItemIdNUpdTimestamp(Connection con,
                                                           Timestamp synTimestamp,
                                                           long owner_ent_id,
                                                           String itm_type, Boolean isRun, Boolean isSession)
        throws SQLException {
        PreparedStatement stmt = null;
        Hashtable h = new Hashtable();
        try {
            String sql = SQL_GET_NOT_SYN_ITM_ID_N_UPD_T;
            if (isRun!=null)    sql += " and itm_run_ind = ? ";
            if (isSession!=null)    sql += " and itm_session_ind = ? ";

            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, owner_ent_id);
            stmt.setString(index++, itm_type);
            stmt.setTimestamp(index++, synTimestamp);
            if (isRun!=null)    stmt.setBoolean(index++, isRun.booleanValue());
            if (isSession!=null)    stmt.setBoolean(index++, isSession.booleanValue());

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                h.put(new Long(rs.getLong("itm_id")), rs.getTimestamp("itm_upd_timestamp"));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return h;
    }

    public static boolean isItemTypeExist(Connection con, String itmType) throws SQLException {
        boolean result=false;
        long count=0;
        String err;
        String SQL = " Select count(*) from aeitemtype "
                   + " Where ity_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1,itmType);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            count = rs.getLong(1);
        }
        else {
            err = "Execute "
                + SQL + " itm_type = " + itmType
                + " return a null result set ";

            throw new SQLException(err);
        }
        result = count > 0;

        stmt.close();
        return result;
    }

    public static Vector getUpdatedItem(Connection con, String itemType, long siteId, Timestamp startDate, Timestamp endDate) throws SQLException{
        Vector itmLst = new Vector();
        String SQL = "SELECT itm_id FROM aeItem WHERE itm_type = ? AND itm_owner_ent_id = ? ";
        if( startDate != null )
            SQL += " AND itm_upd_timestamp > ? ";
        if( endDate != null )
            SQL += " AND itm_upd_timestamp < ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        int index=1;
        stmt.setString(index++, itemType);
        stmt.setLong(index++, siteId);
        if( startDate != null )
            stmt.setTimestamp(index++, startDate);
        if( endDate != null )
            stmt.setTimestamp(index++, endDate);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            itmLst.addElement(new Long(rs.getLong("itm_id")));
        }
        stmt.close();
        return itmLst;
    }

    public class XMLparser extends HandlerBase {
        Hashtable htContent = new Hashtable();
        String tag_name;

        XMLparser() {
        }

        public void startElement(String name, AttributeList inAttribList) throws SAXException {
            if (!name.equals("detail"))
                tag_name = name;
        }
        public void characters(char buf[], int offset, int len) throws SAXException {
            if (!tag_name.equals("detail")){
                String thisStr = new String(buf, offset, len);
                if (thisStr!=null && tag_name!=null){
                    htContent.put(tag_name, thisStr);
                }
            }
        }
    }
    // pre-define itm_xml
    // return hashtable , field name as key, tag content as value
    public Hashtable getXMLContent() {
        XMLparser myXMLparser = null;
        Hashtable htContent = new Hashtable();
        if (itm_xml!=null){
            try {
                StringReader in = new StringReader(itm_xml);
                SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                SAXParser saxParser  = saxFactory.newSAXParser();
                myXMLparser = new XMLparser();
                saxParser.parse(new InputSource(in), myXMLparser);
                in.close();

                htContent = myXMLparser.htContent;
            } catch (ParserConfigurationException e) {
                e.getMessage();
            } catch (SAXException e) {
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return htContent;
    }
    private static boolean chkTarget(Connection con, long usr_ent_id,	long itm_id, String type) throws SQLException {
		boolean ret = false;
		StringBuffer sql = new StringBuffer(512);
		sql.append(" SELECT itd_itm_id FROM itemTargetLrnDetail where itd_itm_id = ? and itd_usr_ent_id = ?")
		;
		
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, itm_id);
		stmt.setLong(index++, usr_ent_id);
		
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			
				ret = true;
		}
		rs.close();
		stmt.close();
		return ret;
	}
    
    private static boolean isTarget(Connection con, long usr_ent_id, long itm_id, String type) throws SQLException {
		boolean ret = false;
		String itm_target_enrol_type = getTargetEnolType(con, itm_id);
		if (type.equalsIgnoreCase(DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER)
			&& DbItemTargetRuleDetail.hasRecord(con, itm_id, type)) {
			ret = chkTarget(con, usr_ent_id, itm_id, type);
		} else {
			aeItemRelation ire = new aeItemRelation();
			ire.ire_child_itm_id = itm_id;
			ire.getParentItemId(con);
			long parent_itm_id = ire.ire_parent_itm_id;
			if (itm_target_enrol_type == null) {
				ret = false;
			} else if (	(itm_target_enrol_type.equalsIgnoreCase(ITM_TARGET_ENROLL_TYPE_ASSIGNED) && !DbItemTargetRuleDetail.hasRecord(con, itm_id, type))
							|| (itm_target_enrol_type.equalsIgnoreCase(ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER) && !DbItemTargetRuleDetail.hasRecord(con, parent_itm_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER)))
			{
				ret = true;
			} else if (itm_target_enrol_type.equalsIgnoreCase(ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER)) {
				ret = chkTarget(con, usr_ent_id, parent_itm_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
			} else {
				ret = chkTarget(con, usr_ent_id, itm_id, type);
			}
		}
		return ret;
	}
    public static String getTargetEnolType(Connection con, long itm_id) throws SQLException {
    	String sql = "select itm_target_enrol_type From  aeItem where itm_id = ?" ;
    	PreparedStatement stmt = con.prepareStatement(sql);
    	stmt.setLong(1, itm_id);
    	ResultSet rs = stmt.executeQuery();
    	String type = null;
    	if(rs.next()) {
    		type = rs.getString("itm_target_enrol_type");
    	}
    	rs.close();
    	stmt.close();
    	return type;
    }
	public static boolean isTargetedLearner(Connection con, long usr_ent_id, long itm_ent_id, boolean get_itm_root) throws SQLException {
		boolean ret = false;
		aeItem itm = new aeItem();
		itm.itm_id = itm_ent_id;
		itm.getRunInd(con);
		if (itm.itm_run_ind) {
			if (get_itm_root) {
				aeItemRelation ire = new aeItemRelation();
				ire.ire_child_itm_id = itm.itm_id;
				ire.getParentItemId(con);
				itm_ent_id = ire.ire_parent_itm_id;
				ret = isTarget(con, usr_ent_id, itm_ent_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
			} else {
				ret = isTarget(con, usr_ent_id, itm_ent_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_ENROLLMENT);
			}
		} else {
			ret = isTarget(con, usr_ent_id, itm_ent_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
		}
		return ret;
	}

    /*
        get the aptent still both itm_run_ind, itm_session_ind is false
    */
    public static long getRootItemId(Connection con, long itmId) throws SQLException{
        long rootItmId;
        aeItem itm = new aeItem();
        itm.itm_id = itmId;
        itm.getRunNSessionInd(con);
        long tmpId;

        if (!itm.itm_run_ind && !itm.itm_session_ind){
            rootItmId = itm.itm_id;
        }else{
            aeItemRelation aeIre = new aeItemRelation();
            aeIre.ire_child_itm_id = itm.itm_id;
            tmpId = aeIre.getParentItemId(con);
            if (tmpId != 0){
                rootItmId = getRootItemId(con, tmpId);
            }else{
                rootItmId = itm.itm_id;
//                throw new cwException("invalid item :" + itm.itm_id + " with no parent.");
            }
            rootItmId = itm.itm_id;
        }
        return rootItmId;
    }

    // include itself , order by root -> itself
    public static void getParentsInfo(Connection con, long itmId, Vector vtParentItm) throws SQLException, cwSysMessage{
        aeItem itm = new aeItem();
        itm.itm_id = itmId;
        itm.getItem(con);
        vtParentItm.add(0, itm);
//        itm.getRunNSessionInd(con);
        if (itm.itm_run_ind || itm.itm_session_ind){
            aeItemRelation aeIre = new aeItemRelation();
            aeIre.ire_child_itm_id = itmId;
            long tmpId = aeIre.getParentItemId(con);
            if (tmpId != 0){
                getParentsInfo(con, tmpId, vtParentItm);
            }
        }
    }

    public static StringBuffer getNavAsXML(Connection con, long itmId) throws SQLException, cwSysMessage{
//        try{
            StringBuffer result = new StringBuffer();
            result.append("<nav>").append(cwUtils.NEWL);
            Vector vtParentItm = new Vector();
            getParentsInfo(con, itmId, vtParentItm);
            aeItem itm;
            for (int i=0; i<vtParentItm.size();i++){
                itm = (aeItem)vtParentItm.elementAt(i);
                result.append("<item id=\"").append(itm.itm_id).append("\"")
                .append(" run_ind=\"").append(itm.itm_run_ind).append("\"")
                .append(" blend_ind=\"").append(itm.itm_blend_ind).append("\"")
                .append(" exam_ind=\"").append(itm.itm_exam_ind).append("\"")
                .append(" ref_ind=\"").append(itm.itm_ref_ind).append("\"")
                .append(" integrated_ind=\"").append(itm.itm_integrated_ind).append("\"")
                .append(" session_ind=\"").append(itm.itm_session_ind).append("\">");
                result.append("<title>");
                result.append(cwUtils.esc4XML(cwUtils.escNull(itm.itm_title)));
                result.append("</title>");
                result.append(cwUtils.NEWL);
                result.append("</item>");
            }
            result.append("</nav>");
            return result;
//        }catch(qdbException e){
//            throw new SQLException(e.getMessage());
//        }
    }

    public static Vector getSuccessorItemId(Connection con, long itmId) throws SQLException{
        Vector vtSuccessor = new Vector();
        Vector vtChild = aeItemRelation.getChildItemId(con, itmId);
        for (int i=0; i<vtChild.size(); i++){
            aeItem itm = new aeItem();
            itm.itm_id = ((Long)vtChild.elementAt(i)).longValue();
            itm.getCreateRunNSessionInd(con);
            if (itm.itm_create_run_ind || itm.itm_create_session_ind){
                Vector v_tmp = getSuccessorItemId(con, itm.itm_id);
                for (int j=0; j<v_tmp.size(); j++){
                    vtSuccessor.addElement(v_tmp.elementAt(j));
                }
            }
        }
        for (int i=0; i<vtChild.size(); i++){
            vtSuccessor.addElement(vtChild.elementAt(i));
        }
        return vtSuccessor;
    }


    //获取所有的课程模板信息
    public static String getAllItemTypeDetailInOrgAsXML(Connection con, long root_ent_id)
        throws SQLException, cwException {
            return getAllItemTypeDetailInOrgAsXML(con, root_ent_id, null);









        }
    public static String getAllItemTypeDetailInOrgAsXML(Connection con, long root_ent_id, String item_type)
        throws SQLException, cwException {

            StringBuffer xml = new StringBuffer();
            StringBuffer typeList = new StringBuffer();
            StringBuffer workflowList = new StringBuffer();
            StringBuffer metaList = new StringBuffer();

            ViewItemType vIty = new ViewItemType();
            Vector resultVec = vIty.getAllItemTypeDetailInOrgAsXML(con, root_ent_id, item_type);

            String curItemType = null;
            String prevItemType = null;
            String preTrainingType=null;
            Vector workflowId = new Vector();
            typeList.append("<item_type_list>");
            metaList.append("<item_type_meta_list>");
            int sort=1; 
            for(int i=0; i<resultVec.size(); i++){
                vIty = (ViewItemType)resultVec.elementAt(i);
                
                curItemType =vIty.item_type;
                //Save workflow template id;
                if( vIty.ttp_title.equalsIgnoreCase("WORKFLOW") ) {
                	//如果是报名工作流，检查该报名工作流是否存在，不存在则放入集合中
                    Long wfId = new Long(vIty.itt_tpl_id);
                    if( workflowId.indexOf(wfId) == -1 )
                        workflowId.addElement(wfId);
                }
                //只要课程类型不为空就加载
                if( !curItemType.equalsIgnoreCase(prevItemType)) {
                    metaList.append(vIty.getMetaDataAsXML());
                }
                String curTrainingType="";
                if(vIty.ity_id.endsWith(aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED)) {
                	curTrainingType = aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED;
                } else if(vIty.item_type.endsWith(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM)){
                	curTrainingType=aeItemDummyType.ITEM_DUMMY_TYPE_EXAM;
                }else if(vIty.item_type.endsWith(aeItemDummyType.ITEM_DUMMY_TYPE_COS)){
                	curTrainingType=aeItemDummyType.ITEM_DUMMY_TYPE_COS;
                }else{
                	curTrainingType=aeItemDummyType.ITEM_DUMMY_TYPE_REF;
                }
                if(!curTrainingType.equalsIgnoreCase(preTrainingType)){
                	 if(preTrainingType !=null){
                     	typeList.append("</training_type>");
                     }
                	typeList.append("<training_type id=\"").append(curTrainingType).append("\">");
                }
                typeList.append("<item id=\"").append(vIty.ity_id).append("\" ").append(" dummy_type=\"").append(vIty.item_type).append("\" ").append(" blend_ind=\"").append(vIty.ity_blend_ind).append("\" ")
                		.append("exam_ind=\"").append(vIty.ity_exam_ind).append("\" ").append(" ref_ind=\"").append(vIty.ity_ref_ind)
                		.append("\" ").append(" sort=\"").append(sort++)
                		.append("\">")
                        //.append(vIty.ity_title_xml)
                        .append("<template_type_list>");

                for(int j=i; j<resultVec.size(); j++, i++) {
                    ViewItemType _vIty = (ViewItemType)resultVec.elementAt(j);
                    //Save workflow template id;
                    if( _vIty.ttp_title.equalsIgnoreCase("WORKFLOW") ) {
                        Long wfId = new Long(_vIty.itt_tpl_id);
                        if( workflowId.indexOf(wfId) == -1 )
                            workflowId.addElement(wfId);
                    }

                    long curItemTplTypeId = _vIty.itt_ttp_id;
                    if( !_vIty.item_type.equalsIgnoreCase(curItemType) ){
                        i--;
                        break;
                    }
                    typeList.append("<template_type ")
                            .append(" id=\"").append(_vIty.itt_ttp_id).append("\" ")
                            .append(" title=\"").append(_vIty.ttp_title).append("\" ")
                            .append(">");
                    for(int k=j; k<resultVec.size(); k++, j++, i++){
                        ViewItemType __vIty = (ViewItemType)resultVec.elementAt(k);
                        //Save workflow template id;
                        if( __vIty.ttp_title.equalsIgnoreCase("WORKFLOW") ) {
                            Long wfId = new Long(__vIty.itt_tpl_id);
                            if( workflowId.indexOf(wfId) == -1 )
                                workflowId.addElement(wfId);
                        }

                        if( curItemTplTypeId != __vIty.itt_ttp_id || !curItemType.equalsIgnoreCase(__vIty.item_type) ){
                            j--;i--;
                            break;
                        }
                        typeList.append("<template ")
                                .append(" id=\"").append(__vIty.itt_tpl_id).append("\" ")
                                .append(" seq_id=\"").append(__vIty.itt_seq_id).append("\" ")
                                .append("/>");
                    }
                    typeList.append("</template_type>");
                }
                typeList.append("</template_type_list>")
                        .append("</item>");
               
                preTrainingType=curTrainingType;
                prevItemType = curItemType;
            }
            typeList.append("</training_type>");
            typeList.append("</item_type_list>");
            metaList.append("</item_type_meta_list>");


            long[] workflowIdArray = new long[workflowId.size()];
            for(int i=0; i<workflowId.size(); i++) {
                workflowIdArray[i] = ((Long)workflowId.elementAt(i)).longValue();
            }
            cwUtils.sort(workflowIdArray);
            workflowList.append("<workflow_list>");
            for(int i=0; i<workflowIdArray.length; i++) {
                workflowList.append("<workflow ")
                            .append(" tpl_id=\"").append(workflowIdArray[i]).append("\" ")
                            .append("/>");
            }
            workflowList.append("</workflow_list>");

            xml.append(typeList).append(workflowList).append(metaList);
            return xml.toString();
        }

    /**
    * Cascading Update item and it's child workflow template
     Pre-define variable:
     <ul>
     <li>itm_create_run_ind
    */
    public void updCascadeItemWrkTpl(Connection con, long wrk_tpl_id)
        throws SQLException{

            Vector v_itm_id = new Vector();
            v_itm_id.addElement(new Long(this.itm_id));
            ViewItemTemplate viewIt = new ViewItemTemplate();
            viewIt.templateId = wrk_tpl_id;
            viewIt.templateType = "WORKFLOW";
            if(this.itm_create_run_ind)
                v_itm_id.addAll( aeItemRelation.getChildItemId(con, this.itm_id) );

            viewIt.updItemTemplate(con, v_itm_id);
            return;
        }
  public Vector getItemByEnrolEndDate(Connection con, long site_id, long ttp_id, Timestamp periodStart, Timestamp periodEnd)
        throws SQLException, cwException {

            String SQL = " SELECT itm_id "
                       + " FROM aeItem, aeItemTemplate, aeTemplateType "
                       + " WHERE itm_id = itp_itm_id "
                       + " AND itp_ttp_id = ttp_id "
                       + " AND ttp_title = ? "
                       + " AND itp_tpl_id = ? "
                       + " AND itm_owner_ent_id = ? ";
            if( periodStart != null )
                SQL += " AND itm_appn_end_datetime >= ? ";
            if( periodEnd != null )
                SQL += " AND itm_appn_end_datetime <= ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, "WORKFLOW");
            stmt.setLong(index++, ttp_id);
            stmt.setLong(index++, site_id);
            if( periodStart != null )
                stmt.setTimestamp(index++, periodStart);
            if( periodEnd != null )
                stmt.setTimestamp(index++, periodEnd);
            ResultSet rs = stmt.executeQuery();
            Vector vec = new Vector();
            while(rs.next())
                vec.addElement(new Long(rs.getLong("itm_id")));
            stmt.close();
            return vec;

        }

    private static final String sql_get_itm_content_info =
        " Select itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration From aeItem Where itm_id = ? ";

    public void getItmContentInfo(Connection con) throws SQLException, cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_content_info);
        stmt.setLong(1, this.itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
            this.itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
            this.itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
        }
        else {
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + itm_id);
        }
        stmt.close();
        return;
    }

    public String contentInfoAsXML(Connection con) throws SQLException, cwSysMessage, cwException, qdbException{
        StringBuffer xmlBuf = new StringBuffer();
        dbCourse dbcos = new dbCourse();
        dbcos.cos_res_id = dbCourse.getCosResId(con, itm_id);
        dbcos.get(con);
        boolean has_mod;
        has_mod = dbcos.cos_structure_xml != null;
        xmlBuf.append("<item")
        .append(" id=\"").append(itm_id).append("\"")
        .append(" type=\"").append(itm_type).append("\"")
        .append(" cos_res_id=\"").append(aeUtils.escZero(dbcos.cos_res_id)).append("\"")
        .append(" status=\"").append(itm_status).append("\"")
        .append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"")
        .append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"")
        .append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"")
        .append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"")
        .append(" create_run_ind=\"").append(itm_create_run_ind).append("\"")
        .append(" create_session_ind=\"").append(itm_create_session_ind).append("\"")
        .append(" qdb_ind=\"").append(itm_qdb_ind).append("\"")
        .append(" can_qr_ind=\"").append(itm_can_qr_ind).append("\"")
        .append(" usr_can_qr_ind=\"").append(usr_can_qr_ind).append("\"")
        .append(" run_ind=\"").append(itm_run_ind).append("\"")
        .append(" exam_ind=\"").append(itm_exam_ind).append("\"")
        .append(" session_ind=\"").append(itm_session_ind).append("\"")
        .append(" apply_ind=\"").append(itm_apply_ind).append("\"")
        .append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"")
        .append(" imd_id=\"").append(aeUtils.escZero(itm_imd_id)).append("\"")
        .append(" rsv_id=\"").append(aeUtils.escZero(itm_rsv_id)).append("\"")
        .append(" tcr_id=\"").append(aeUtils.escZero(itm_tcr_id)).append("\"")
        .append(" content_eff_start_datetime=\"").append(aeUtils.escNull(itm_content_eff_start_datetime)).append("\"")
        .append(" content_eff_end_datetime=\"").append(aeUtils.escNull(itm_content_eff_end_datetime)).append("\"")
        .append(" content_eff_duration=\"").append(itm_content_eff_duration).append("\"")
        .append(" tob_existence=\"").append(hasToc(con)).append("\"")
        .append(" app_approval_type=\"").append(cwUtils.escNull(this.itm_app_approval_type)).append("\"")
        .append(" content_def=\"").append(cwUtils.escNull(itm_content_def)).append("\"")
        .append(" has_mod=\"").append(has_mod).append("\"")
        .append(" itm_inst_type=\"").append(itm_inst_type).append("\"")
        .append(" itm_not_allow_waitlist_ind=\"").append(itm_not_allow_waitlist_ind).append("\"")
        .append(">");

        xmlBuf.append(getNavAsXML(con, this.itm_id));

        xmlBuf.append(aeUtils.escNull(getItemTypeTitle(con)));
        xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>").append(dbUtils.NEWL);
        xmlBuf.append("</item>");
        xmlBuf.append("<last_updated usr_id=\"").append(itm_upd_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(itm_upd_timestamp).append("\">");
        xmlBuf.append("</last_updated>");
        xmlBuf.append(cwUtils.NEWL);
        return xmlBuf.toString();
    }

    public String JIInfoAsXML(Connection con) throws SQLException, cwSysMessage, cwException{
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<item")
        .append(" id=\"").append(itm_id).append("\"")
        .append(" type=\"").append(itm_type).append("\"")
        .append(" cos_res_id=\"").append(aeUtils.escZero(getResId(con))).append("\"")
        .append(" status=\"").append(itm_status).append("\"")
        .append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"")
        .append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"")
        .append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"")
        .append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"")
        .append(" create_run_ind=\"").append(itm_create_run_ind).append("\"")
        .append(" create_session_ind=\"").append(itm_create_session_ind).append("\"")
        .append(" qdb_ind=\"").append(itm_qdb_ind).append("\"")
        .append(" run_ind=\"").append(itm_run_ind).append("\"")
        .append(" session_ind=\"").append(itm_session_ind).append("\"")
        .append(" apply_ind=\"").append(itm_apply_ind).append("\"")
        .append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"")
        .append(" imd_id=\"").append(aeUtils.escZero(itm_imd_id)).append("\"")
        .append(" rsv_id=\"").append(aeUtils.escZero(itm_rsv_id)).append("\"")
        .append(" tcr_id=\"").append(aeUtils.escZero(itm_tcr_id)).append("\"")
        .append(" content_eff_start_datetime=\"").append(aeUtils.escNull(itm_content_eff_start_datetime)).append("\"")
        .append(" content_eff_end_datetime=\"").append(aeUtils.escNull(itm_content_eff_end_datetime)).append("\"")
        .append(" content_eff_duration=\"").append(itm_content_eff_duration).append("\"")
        .append(" tob_existence=\"").append(hasToc(con)).append("\"")
        .append(">");

        xmlBuf.append(getNavAsXML(con, this.itm_id));

       // xmlBuf.append(aeUtils.escNull(getItemTypeTitle(con)));
        xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>").append(dbUtils.NEWL);
        xmlBuf.append(aeItemMessage.getAeItemMessage(con, itm_id));
        xmlBuf.append("</item>");
        /*
        xmlBuf.append("<last_updated usr_id=\"").append(itm_upd_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(itm_upd_timestamp).append("\">");
        xmlBuf.append("</last_updated>");
        */
        xmlBuf.append(cwUtils.NEWL);
        return xmlBuf.toString();
    }

    private static final String sql_upd_itm_content_eff_duration =
        " Update aeItem set itm_content_eff_duration = ? , itm_upd_timestamp = ? , itm_upd_usr_id = ? where itm_id = ? ";
    // predefine itm_content_eff_duration, itm_id
    public void updItemContentEffDuration(Connection con, String usr_id)
      throws SQLException, cwSysMessage,qdbException,cwException {
        PreparedStatement stmt = con.prepareStatement(sql_upd_itm_content_eff_duration);
        stmt.setInt(1, this.itm_content_eff_duration);
        stmt.setTimestamp(2, cwSQL.getTime(con));
        stmt.setString(3, usr_id);
        stmt.setLong(4, this.itm_id);
        stmt.executeUpdate();
        stmt.close();
        return;

    }

    private void delCriteria(Connection con) throws SQLException{
        Vector ccrIds = DbCourseCriteria.getCcrIdByItmId(con, itm_id);
        DbCourseCriteria ccr = new DbCourseCriteria();
        for (int i=0; i<ccrIds.size(); i++){
            ccr.ccr_id = ((Long)ccrIds.elementAt(i)).longValue();
            ccr.del(con);
        }
    }
 private static final String SQL_GET_INTERESTED_ITM =
        " Select itm_id, itm_title, itm_type " +
        " ,itm_appn_start_datetime, itm_appn_end_datetime " +
        " ,itm_capacity, itm_unit " +
        " ,itm_xml, itm_status " +
        " ,itm_owner_ent_id " +
        " ,itm_create_timestamp, itm_create_usr_id " +
        " ,itm_upd_timestamp, itm_upd_usr_id " +
        " ,itm_eff_start_datetime, itm_eff_end_datetime " +
        " ,itm_code, itm_fee, itm_fee_ccy, itm_ext1 " +
        " ,itm_run_ind, itm_session_ind, itm_apply_ind" +
        " ,itm_deprecated_ind, itm_life_status, itm_apply_method " +
        " ,itm_create_run_ind, itm_create_session_ind, itm_has_attendance_ind, itm_ji_ind, itm_completion_criteria_ind, itm_qdb_ind, itm_imd_id , itm_auto_enrol_qdb_ind, itm_version_code " +
        " ,itm_min_capacity, itm_person_in_charge, itm_cancellation_reason, itm_cancellation_type " +
        " ,itm_rsv_id, itm_tcr_id " +
        " From aeItem, aeLearningSoln " +
        " Where itm_id = lsn_itm_id " +
        " And lsn_ent_id = ? " +
        " And lsn_type = ? ";

    /**
    Get the interested item, shown when registered, of the input user
    @param con Connection to database
    @param ent_id user entity id
    @param lsn_type plan type of learning solution
    @return Vector of aeItem interested
    */
    public static Vector getInterestedItem(Connection con, long ent_id, String lsn_type)
        throws SQLException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SQL_GET_INTERESTED_ITM);
            stmt.setLong(1, ent_id);
            stmt.setString(2, lsn_type);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                aeItem itm = new aeItem();
                itm.itm_id = rs.getLong("itm_id");
                itm.itm_title = rs.getString("itm_title");
                itm.itm_type = rs.getString("itm_type");
                itm.itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
                itm.itm_appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
                itm.itm_capacity = rs.getLong("itm_capacity");
                itm.itm_unit = rs.getFloat("itm_unit");
                itm.itm_xml = cwSQL.getClobValue(rs, "itm_xml");
                itm.itm_status = rs.getString("itm_status");
                itm.itm_owner_ent_id = rs.getLong("itm_owner_ent_id");
                itm.itm_create_timestamp = rs.getTimestamp("itm_create_timestamp");
                itm.itm_create_usr_id = rs.getString("itm_create_usr_id");
                itm.itm_upd_timestamp = rs.getTimestamp("itm_upd_timestamp");
                itm.itm_upd_usr_id = rs.getString("itm_upd_usr_id");
                itm.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
                itm.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
                itm.itm_code = rs.getString("itm_code");
                itm.itm_fee = rs.getFloat("itm_fee");
                itm.itm_fee_ccy = rs.getString("itm_fee_ccy");
                itm.itm_ext1 = rs.getString("itm_ext1");
                itm.itm_run_ind = rs.getBoolean("itm_run_ind");
                itm.itm_session_ind = rs.getBoolean("itm_session_ind");
                itm.itm_apply_ind = rs.getBoolean("itm_apply_ind");
                itm.itm_deprecated_ind = rs.getBoolean("itm_deprecated_ind");
                itm.itm_life_status = rs.getString("itm_life_status");
                itm.itm_apply_method = rs.getString("itm_apply_method");
                itm.itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
                itm.itm_create_session_ind = rs.getBoolean("itm_create_session_ind");
                itm.itm_has_attendance_ind = rs.getBoolean("itm_has_attendance_ind");
                itm.itm_ji_ind = rs.getBoolean("itm_ji_ind");
                itm.itm_completion_criteria_ind = rs.getBoolean("itm_completion_criteria_ind");
                itm.itm_qdb_ind = rs.getBoolean("itm_qdb_ind");
                itm.itm_imd_id = rs.getLong("itm_imd_id");
                itm.itm_auto_enrol_qdb_ind = rs.getBoolean("itm_auto_enrol_qdb_ind");
                itm.itm_version_code = rs.getString("itm_version_code");
                itm.itm_min_capacity = rs.getLong("itm_min_capacity");
                itm.itm_person_in_charge = rs.getString("itm_person_in_charge");
                itm.itm_cancellation_reason = rs.getString("itm_cancellation_reason");
                itm.itm_cancellation_type = rs.getString("itm_cancellation_type");
                itm.itm_rsv_id = rs.getLong("itm_rsv_id");
                itm.itm_tcr_id = rs.getLong("itm_tcr_id");
                v.addElement(itm);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

    private static final String sql_get_child_item_recursive =
        " SELECT * FROM aeitem WHERE itm_id IN (" +
        " SELECT DISTINCT ire_child_itm_id from aeItemRelation" +
        " WHERE ire_parent_itm_id = ? OR " +
        " ire_parent_itm_id IN " +
        " (SELECT ire_child_itm_id FROM dbo.aeItemRelation  " +
        " WHERE ire_parent_itm_id = ?)) ";

    // get all the child item recursively
    public Vector getChildItemRecursive(Connection con) throws SQLException {

        long run_itm_id;
        PreparedStatement stmt = con.prepareStatement(sql_get_child_item_recursive);
        int index = 1;

        stmt.setLong(index++, this.itm_id);
        stmt.setLong(index++, this.itm_id);

        ResultSet rs = stmt.executeQuery();
        Vector result = new Vector();
        while(rs.next()) {
            aeItem childItem = new aeItem();

            childItem.itm_id                  = rs.getLong("itm_id");
            childItem.itm_title               = rs.getString("itm_title");
            childItem.itm_code                = rs.getString("itm_code");
            childItem.itm_type                = rs.getString("itm_type");
            childItem.itm_capacity            = rs.getLong("itm_capacity");
            childItem.itm_unit                = rs.getFloat("itm_unit");
            childItem.itm_fee_ccy             = rs.getString("itm_fee_ccy");
            childItem.itm_fee                 = rs.getFloat("itm_fee");
            childItem.itm_status              = rs.getString("itm_status");
            childItem.itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
            childItem.itm_appn_end_datetime   = rs.getTimestamp("itm_appn_end_datetime");
            childItem.itm_eff_start_datetime  = rs.getTimestamp("itm_eff_start_datetime");
            childItem.itm_eff_end_datetime    = rs.getTimestamp("itm_eff_end_datetime");
            childItem.itm_create_timestamp    = rs.getTimestamp("itm_create_timestamp");
            childItem.itm_create_usr_id       = rs.getString("itm_create_usr_id");
            childItem.itm_upd_timestamp       = rs.getTimestamp("itm_upd_timestamp");
            childItem.itm_upd_usr_id          = rs.getString("itm_upd_usr_id");
            childItem.itm_owner_ent_id        = rs.getLong("itm_owner_ent_id");
            childItem.itm_ext1                = rs.getString("itm_ext1");
            childItem.itm_run_ind             = rs.getBoolean("itm_run_ind");
            childItem.itm_session_ind         = rs.getBoolean("itm_session_ind");
            childItem.itm_apply_ind           = rs.getBoolean("itm_apply_ind");
            childItem.itm_deprecated_ind      = rs.getBoolean("itm_deprecated_ind");
            childItem.itm_life_status          = rs.getString("itm_life_status");
            childItem.itm_apply_method        = rs.getString("itm_apply_method");
            childItem.itm_create_run_ind      = rs.getBoolean("itm_create_run_ind");
            childItem.itm_create_session_ind  = rs.getBoolean("itm_create_session_ind");
            childItem.itm_has_attendance_ind  = rs.getBoolean("itm_has_attendance_ind");
            childItem.itm_ji_ind  = rs.getBoolean("itm_ji_ind");
            childItem.itm_completion_criteria_ind = rs.getBoolean("itm_completion_criteria_ind");
            childItem.itm_qdb_ind             = rs.getBoolean("itm_qdb_ind");
            childItem.itm_auto_enrol_qdb_ind  = rs.getBoolean("itm_auto_enrol_qdb_ind");
            childItem.itm_version_code        = rs.getString("itm_version_code");
            childItem.itm_person_in_charge    = rs.getString("itm_person_in_charge");
            childItem.itm_cancellation_reason = rs.getString("itm_cancellation_reason");
            childItem.itm_min_capacity        = rs.getLong("itm_min_capacity");
            childItem.itm_rsv_id              = rs.getLong("itm_rsv_id");
            childItem.itm_tcr_id              = rs.getLong("itm_tcr_id");
            childItem.itm_xml                 = cwSQL.getClobValue(rs, "itm_xml");
            result.addElement(childItem);
        }

        stmt.close();
        return result;
    }


     public String JIItemDetailByRunAsXML(Connection con, qdbEnv inEnv,
                                    boolean checkStatus, boolean prev_version_ind,
                                    String tvw_id, long usr_ent_id,
                                    boolean cos_mgt_ind, boolean show_attendance_ind, boolean show_session_ind)
                                    throws SQLException, cwException ,cwSysMessage {
        String xml;
        aeItem parent = new aeItem();
        aeItemRelation ire = new aeItemRelation();
        ire.ire_child_itm_id = this.itm_id;
        parent.itm_id = ire.getParentItemId(con);
        if(parent.itm_id != 0) {
            parent.getItem(con);
            xml = parent.JIItemDetailAsXML(con, inEnv, checkStatus, prev_version_ind,
                                            tvw_id, true, show_session_ind, this.itm_id, usr_ent_id,
                                            cos_mgt_ind, show_attendance_ind);
        } else {
            /*getItem(con);*/
            xml = JIItemDetailAsXML(con, inEnv, checkStatus, prev_version_ind,
                                   tvw_id, false, show_session_ind, 0, usr_ent_id,
                                   cos_mgt_ind, show_attendance_ind);
        }
        return xml;
    }

     public String JIItemDetailAsXML(Connection con, qdbEnv inEnv,
                                    boolean checkStatus, boolean prev_version_ind,
                                    String tvw_id, boolean show_run_ind, boolean show_session_ind, long in_run_itm_id,
                                    long usr_ent_id, boolean cos_mgt_ind,
                                    boolean show_attendance_ind)
                                    throws SQLException, cwException ,cwSysMessage {
//                                        System.out.println("ItemDetailAsXML");
        StringBuffer xmlBuf = new StringBuffer(2500);
        if(myTreeNode != null) {
            xmlBuf.append(myTreeNode.contentAsXML(con));
        }
        Timestamp cur_time = cwSQL.getTime(con);
        //get course res_id
        dbCourse cos = new dbCourse();
        cos.cos_itm_id = this.itm_id;
//        long ccr_id = 0;
        if(this.itm_qdb_ind) {
            cos.cos_res_id = dbCourse.getCosResId(con, cos.cos_itm_id);
//            ccr_id = dbCourse.getCosCriteriaId(con, cos.cos_res_id);
        }
        // item attributes
        xmlBuf.append("<item")
                .append(" id=\"").append(itm_id).append("\"")
                //.append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("\"")
                .append(" type=\"").append(itm_type).append("\"")
                .append(" cos_res_id=\"").append(aeUtils.escZero(cos.cos_res_id)).append("\"")
  //              .append(" ccr_id=\"").append(aeUtils.escZero(ccr_id)).append("\"")
                .append(" status=\"").append(itm_status).append("\"")
                .append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_code))).append("\"")
                .append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_ext1))).append("\"")
                .append(" eff_start_datetime=\"").append(aeUtils.escNull(itm_eff_start_datetime)).append("\"")
                .append(" eff_end_datetime=\"").append(aeUtils.escNull(itm_eff_end_datetime)).append("\"")
                .append(" appn_start_datetime=\"").append(aeUtils.escNull(itm_appn_start_datetime)).append("\"")
                .append(" appn_end_datetime=\"").append(aeUtils.escNull(itm_appn_end_datetime)).append("\"")
                .append(" eff_start_days_to=\"").append(dayDiff(cur_time, itm_eff_start_datetime)).append("\"")
                .append(" eff_end_days_to=\"").append(dayDiff(cur_time, itm_eff_end_datetime)).append("\"")
                .append(" appn_start_days_to=\"").append(dayDiff(cur_time, itm_appn_start_datetime)).append("\"")
                .append(" appn_end_days_to=\"").append(dayDiff(cur_time, itm_appn_end_datetime)).append("\"")
                .append(" life_status=\"").append(aeUtils.escNull(itm_life_status)).append("\"")
                .append(" apply_method=\"").append(aeUtils.escNull(itm_apply_method)).append("\"")
                .append(" capacity=\"").append(itm_capacity).append("\"")
                .append(" min_capacity=\"").append(itm_min_capacity).append("\"")
                .append(" unit=\"").append(itm_unit).append("\"")
                .append(" version_code=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_version_code))).append("\"")
                .append(" person_in_charge=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_person_in_charge))).append("\"")
                .append(" cancellation_reason=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_reason))).append("\"")
                .append(" cancellation_type=\"").append(dbUtils.esc4XML(aeUtils.escNull(itm_cancellation_type))).append("\"")
                .append(" create_run_ind=\"").append(itm_create_run_ind).append("\"")
                .append(" create_session_ind=\"").append(itm_create_session_ind).append("\"")
                .append(" qdb_ind=\"").append(itm_qdb_ind).append("\"")
                .append(" auto_enrol_qdb_ind=\"").append(itm_auto_enrol_qdb_ind).append("\"")
                .append(" run_ind=\"").append(itm_run_ind).append("\"")
                .append(" session_ind=\"").append(itm_session_ind).append("\"")
                .append(" has_attendance_ind=\"").append(itm_has_attendance_ind).append("\"")
                .append(" ji_ind=\"").append(itm_ji_ind).append("\"")
                .append(" completion_criteria_ind=\"").append(itm_completion_criteria_ind).append("\"")
                .append(" apply_ind=\"").append(itm_apply_ind).append("\"")
                .append(" deprecated_ind=\"").append(itm_deprecated_ind).append("\"")
                .append(" imd_id=\"").append(aeUtils.escZero(itm_imd_id)).append("\"")
                .append(" rsv_id=\"").append(aeUtils.escZero(itm_rsv_id)).append("\"")
                .append(" tcr_id=\"").append(aeUtils.escZero(itm_tcr_id)).append("\"")
                .append(" appn_started_ind=\"").append(this.itm_appn_start_datetime == null || this.itm_appn_start_datetime.before(cur_time)).append("\"")
                .append(" appn_closed_ind=\"").append(this.itm_appn_end_datetime != null && this.itm_appn_end_datetime.before(cur_time)).append("\"")
                .append(" enrol_cnt=\"").append(aeApplication.countItemAppn(con, this.itm_id, show_attendance_ind)).append("\"")
                .append(" itm_upd_timestamp=\"").append(aeUtils.escNull(itm_upd_timestamp)).append("\"")
                //.append(" cos_res_id=\"").append(getResId(con)).append("\"")
            ;
            if(this.itm_run_ind || this.itm_session_ind) {
                aeItemRelation aeIre = new aeItemRelation();
                aeIre.ire_child_itm_id = this.itm_id;
                aeItem ireParentItm = aeIre.getParentInfo(con);
                if (ireParentItm != null) {
                    xmlBuf.append(" parent_itm_id=\"").append(ireParentItm.itm_id).append("\"");
                    xmlBuf.append(" parent_title=\"").append(cwUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"");
                }
            }
            xmlBuf.append(">");
            xmlBuf.append(getNavAsXML(con, this.itm_id));
            //get application count by app_status
            xmlBuf.append(aeApplication.getAppnCountByItemAsXML(con, this.itm_id));

            //application id and process status as xml
            if(usr_ent_id != 0) {
                xmlBuf.append(getAppAsXML(con, usr_ent_id));
            }
            //item type xml
            xmlBuf.append(aeUtils.escNull(getItemTypeTitle(con)));
            //item title
            xmlBuf.append("<title>").append(dbUtils.esc4XML(aeUtils.escNull(itm_title))).append("</title>").append(dbUtils.NEWL);

            if(prev_version_ind) {

                xmlBuf.append("<family>");
                Vector v_family = getItemFamily(con);
                for(int i=0; i<v_family.size(); i++) {
                    xmlBuf.append(((aeItem)v_family.elementAt(i)).itemAttributeAsXML());
                }
                xmlBuf.append("</family>");
            }
            //get item template
            aeTemplate tpl = getItemTemplate(con);

            //get item template view
            int cnt = 0;
            if(tvw_id == null || tvw_id.length() == 0) {
                tvw_id = "DETAIL_VIEW";
            }
            // add by Tim: check if the item is cancelled, change to "CANCELLED VIEW"
            if(itm_life_status !=null && itm_life_status.equals("CANCELLED"))
                tvw_id = "CANCELLED_VIEW";
            DbTemplateView dbTplVi = new DbTemplateView();
            do {
                if(cnt > 1) {
                    break;
                }
                if(cnt > 0) {
                    tvw_id = "DETAIL_VIEW";
                }
                dbTplVi.tvw_tpl_id = tpl.tpl_id;
                dbTplVi.tvw_id = tvw_id;
                dbTplVi.get(con);
                cnt ++;
            } while(dbTplVi.tvw_xml == null);
            // template details
            xmlBuf.append(getValuedTemplate(con,tpl,dbTplVi,cos_mgt_ind,cur_time,inEnv, usr_ent_id));
            xmlBuf.append("<last_updated usr_id=\"").append(itm_upd_usr_id).append("\"");
            xmlBuf.append(" timestamp=\"").append(itm_upd_timestamp).append("\">");
            xmlBuf.append("</last_updated>");
            xmlBuf.append(dbUtils.NEWL);

            // xml for run infomation
            if(show_run_ind && this.itm_create_run_ind) {
                    //if has applied run, only get the applied run
                    if(in_run_itm_id == 0) {
                        long run_itm_id = getAppliedRunId(con, usr_ent_id);
                        xmlBuf.append("<run_item_list");
                        if(run_itm_id != 0) {
                            xmlBuf.append(" applied_run_item_id=\"").append(run_itm_id).append("\"");
                        }
                        xmlBuf.append(">");
                            Vector v_run = getChildItemAsVector(con, checkStatus, true, null);
                            for(int i=0; i<v_run.size(); i++) {
                                xmlBuf.append(((aeItem)v_run.elementAt(i)).JIItemDetailAsXML(con, inEnv, checkStatus, false, tvw_id, false, show_session_ind, 0, usr_ent_id, cos_mgt_ind, show_attendance_ind));
                            }
                    }
                    else {
                        xmlBuf.append("<run_item_list selected_run_itm_id=\"").append(in_run_itm_id).append("\">");
                        aeItem runItem = new aeItem();
                        runItem.itm_id = in_run_itm_id;
                        runItem.getItem(con);
                        xmlBuf.append(runItem.JIItemDetailAsXML(con, inEnv, checkStatus, false, tvw_id, false, show_session_ind, 0, usr_ent_id, cos_mgt_ind, show_attendance_ind));
                    }
                    xmlBuf.append("</run_item_list>");
            }
            if (show_session_ind && this.itm_create_session_ind){
                xmlBuf.append("<session_item_list>");
                cwPagination page = new cwPagination();
                page.sortCol = "itm_title";
                Vector v_session = getChildItemAsVector(con, false, false, page);
                for(int i=0; i<v_session.size(); i++) {
                    xmlBuf.append(((aeItem)v_session.elementAt(i)).JIItemDetailAsXML(con, inEnv, checkStatus, false, tvw_id, false, show_session_ind, 0, usr_ent_id, cos_mgt_ind, show_attendance_ind));
                }
                xmlBuf.append("</session_item_list>");
            }
            //show attendance here
            if(show_attendance_ind && this.itm_has_attendance_ind) {
                aeAttendanceStatus currentStatus = new aeAttendanceStatus();
                xmlBuf.append(aeAttendance.stateAsXML(con, itm_owner_ent_id, itm_id, -1, currentStatus));
            }
            xmlBuf.append("</item>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }


     // get Application approver item list
   public static String getApproverApplicaitonItmLst(Connection con, cwPagination cwPage, String itmList, HttpSession sess, Hashtable h_tpl_id) throws SQLException {

            //StringBuffer xml = new StringBuffer();
            //HttpSession sess = request.getSession(true);
            Long itm_id;
            Hashtable data = null;
            boolean useSess = false;
            Timestamp sess_pagetime = null;
            String sess_order = null;
            String sess_sort = null;
            if (sess !=null) {
                data = (Hashtable) sess.getAttribute("APPN_COS_LIST");
                if ( data !=null ) {
                    sess_pagetime = (Timestamp) data.get("HASH_TIMESTAMP");
                    sess_order = (String) data.get("HASH_ORDERBY");
                    sess_sort = (String) data.get("HASH_SORTBY");
                    if ( sess_pagetime.equals(cwPage.ts) )
                        useSess = true;
                    CommonLog.debug("data is not null");
                } else {
                	CommonLog.debug("data is null ");
                    data = new Hashtable();
                }
            }

            int start = (cwPage.curPage-1) * cwPage.pageSize + 1;
            int end  = cwPage.curPage * cwPage.pageSize;

            Vector idVecFromSess = new Vector();
            Vector idVecFromDb = new Vector();
            //ResultSet rs = null;
            if( useSess ) {
                idVecFromSess = (Vector)data.get("HASH_ITM_ID_VEC");
                //change order by or sort by will goto page 1 of the serach result
                if( ( sess_order != null && !sess_order.equalsIgnoreCase(cwPage.sortCol) ) ||
                    ( sess_sort != null && !sess_sort.equalsIgnoreCase(cwPage.sortOrder) )   ) {
                        start = 1;
                        end = idVecFromSess.size();
                        cwPage.curPage = 1;
                    }
                long[] id = new long[end - start + 1];
                for(int i=start; i<=idVecFromSess.size() && i<=end; i++)
                    id[i-start] = ((Long)idVecFromSess.elementAt(i-1)).longValue();
                itmList = cwUtils.array2list(id);
            }


        String sql = "select " + cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG) +  " as parent_itm_id, itm_title as parent_itm_title, " +
            "itm_id, itm_title, itm_code, itm_status, itm_type, itm_eff_start_datetime, itm_eff_end_datetime, "+
            "itm_appn_start_datetime, itm_appn_end_datetime, itm_life_status "+
            "from aeItem where itm_id in "+ itmList +" and " +
            "not exists (select ire_parent_itm_id from aeItemRelation where ire_child_itm_id = itm_id) "+
            "union " +
            "select parent.itm_id as parent_itm_id, parent.itm_title as parent_itm_title, "+
            "self.itm_id, self.itm_title, self.itm_code, self.itm_status, self.itm_type, self.itm_eff_start_datetime, self.itm_eff_end_datetime, "+
            "self.itm_appn_start_datetime, self.itm_appn_end_datetime, self.itm_life_status "+
            "from aeItem self, aeItem parent, aeItemRelation where "+
            "self.itm_id in "+ itmList +" and ire_child_itm_id = self.itm_id and parent.itm_id = ire_parent_itm_id " +
            "ORDER BY " + cwPage.sortCol +
            " " + cwPage.sortOrder;

        //System.out.println("sql : " + sql);

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        Vector result = new Vector();
        StringBuffer xmlBuf = new StringBuffer();
        int count = 0;
        xmlBuf.append("<item_list>");
        Timestamp curTime = cwSQL.getTime(con);
        while(rs.next()) {
            /*
             System.out.println("parent_itm_id : " + rs.getLong("parent_itm_id"));
             System.out.println("parent_itm_title : " + rs.getString("parent_itm_title"));
             System.out.println("itm_id : " + rs.getLong("itm_id"));
             System.out.println("itm_title : " + rs.getString("itm_title"));*/
             count++;
             itm_id = new Long(rs.getLong("itm_id"));
             if( idVecFromDb.size() < cwPage.pageSize  ) {
                    Timestamp endTime = rs.getTimestamp("itm_eff_end_datetime");
                    if(endTime == null || (endTime!= null && endTime.after(curTime))) {
                    xmlBuf.append("<item")
                    .append(" parent_id=\"").append(rs.getLong("parent_itm_id")).append("\"")
                    .append(" parent_title=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("parent_itm_title")))).append("\"")
                    .append(" id=\"").append(rs.getLong("itm_id")).append("\"")
                    .append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("itm_title")))).append("\"")
                    .append(" type=\"").append(rs.getString("itm_type")).append("\"")
                    .append(" status=\"").append(rs.getString("itm_status")).append("\"")
                    .append(" code=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("itm_code")))).append("\"")
                    .append(" eff_start_datetime=\"").append(aeUtils.escNull(rs.getTimestamp("itm_eff_start_datetime"))).append("\"")
                    .append(" eff_end_datetime=\"").append(aeUtils.escNull(rs.getTimestamp("itm_eff_end_datetime"))).append("\"")
                    .append(" appn_start_datetime=\"").append(aeUtils.escNull(rs.getTimestamp("itm_appn_start_datetime"))).append("\"")
                    .append(" appn_end_datetime=\"").append(aeUtils.escNull(rs.getTimestamp("itm_appn_end_datetime"))).append("\"")
                    .append(" life_status=\"").append(dbUtils.esc4XML(aeUtils.escNull(rs.getString("itm_life_status")))).append("\"")
                    .append(" tpl_id=\"").append(h_tpl_id.get(new Long(rs.getLong("itm_id")))).append("\"")
                    .append("/>");
                    idVecFromDb.addElement(itm_id);
                    }
             } else if( idVecFromDb.indexOf(itm_id) == -1 )
                idVecFromDb.addElement(itm_id);

        }
        xmlBuf.append("</item_list>");
        stmt.close();

        cwPage.totalRec = 0;
        if( useSess ) {
            cwPage.totalRec = idVecFromSess.size();
            if( ( sess_order != null && !sess_order.equalsIgnoreCase(cwPage.sortCol) ) ||
                ( sess_sort != null && !sess_sort.equalsIgnoreCase(cwPage.sortOrder) ))
                    data.put("HASH_ENT_ID_VEC", idVecFromDb);
        } else {
            cwPage.totalRec = idVecFromDb.size();
            cwPage.ts = cwSQL.getTime(con);
//            System.out.println("cwPage.ts : " + cwPage.ts);
//             System.out.println("idVecFromDb : " + idVecFromDb);
            data.put("HASH_TIMESTAMP", cwPage.ts);

            data.put("HASH_ITM_ID_VEC", idVecFromDb);
        }
        cwPage.totalPage = (int)Math.ceil( (float)cwPage.totalRec / (float) cwPage.pageSize );
       //System.out.println("cwPage.sortCol : " + cwPage.sortCol);
       //System.out.println("cwPage.sortOrder : " + cwPage.sortOrder);
        data.put("HASH_ORDERBY", cwPage.sortCol);
        data.put("HASH_SORTBY", cwPage.sortOrder);
        sess.setAttribute("APPN_COS_LIST", data);
        xmlBuf.append(cwPage.asXML());
        /*
        xmlBuf.append("<pagination total_rec=\"").append(count).append("\"");
        xmlBuf.append(" page_size=\"").append(pageSize).append("\"");
        xmlBuf.append(" cur_page=\"").append(pageNum).append("\"");
        xmlBuf.append(" sort_col=\"").append(orderBy).append("\"");
        xmlBuf.append(" sort_order=\"").append(sortOrder).append("\"/>");*/
        return xmlBuf.toString();
    }

    /**
    Get CodeTable information for generating valued template
    @param con Connetion to database
    @param tpl aeTemplate of this item's item template
    @return XML showing CodeTable data
    */
    private String getCodeTableAsXML(Connection con, aeTemplate tpl)
        throws SQLException, IOException, cwException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        Hashtable hOptionSrc = tpl.getOptionSrc();
        Enumeration eOptionSrc = hOptionSrc.keys();
        int size = hOptionSrc.size();
        String[] option_src_array = new String[size];

        for (int i=0; i<size; i++) {
            String option_src = (String) eOptionSrc.nextElement();
            option_src_array[i] = option_src;
        }
        return CodeTable.getCtb4ItemTemplate(con, option_src_array);
    }

    public static String getItemApplyMethod(Connection con, long itm_id)
        throws SQLException, cwException {

            String SQL = " select ity_target_method from aeitem, aeitemtype where itm_type = ity_id and itm_run_ind = ity_run_ind and itm_session_ind = ity_session_ind  and itm_id = ?";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;

            stmt.setLong(index++, itm_id);

            String result = "";
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                result = rs.getString("ity_target_method");
            stmt.close();
            if(result == null || result.equals("NULL"))
                result = "";
            return result;
        }

    public static String getItemTkhMethod(Connection con, long itm_id)
        throws SQLException, cwException {

            String SQL = " select ity_tkh_method from aeitem, aeitemtype where itm_type = ity_id and itm_run_ind = ity_run_ind and itm_session_ind = ity_session_ind  and itm_id = ?";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;

            stmt.setLong(index++, itm_id);

            String result = "";
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                result = rs.getString("ity_tkh_method");
            }
            rs.close();
            stmt.close();
            return result;
        }

    public Vector getItemByType(Connection con, long root_ent_id, Vector v_itm_type, boolean run_ind)
        throws SQLException {
            if( v_itm_type == null || v_itm_type.isEmpty() )
                return new Vector();

            String SQL = " SELECT itm_id "
                       + " FROM aeItem "
                       + " WHERE itm_owner_ent_id = ? AND itm_run_ind = ? "
                       + " AND itm_type IN ( ? ";
                   for(int i=1; i<v_itm_type.size(); i++)
                        SQL += " ,? ";
                   SQL += " ) ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, root_ent_id);
            stmt.setBoolean(index++, run_ind);
            for(int i=0; i<v_itm_type.size(); i++)
                stmt.setString(index++, (String)v_itm_type.elementAt(i));
            ResultSet rs = stmt.executeQuery();
            Vector v_itm_id = new Vector();
            while(rs.next())
                v_itm_id.addElement(new Long(rs.getLong("itm_id")));
            stmt.close();
            return v_itm_id;
        }

    public static Hashtable getItemTitle(Connection con, Vector v_itm_id)
        throws SQLException {

            Hashtable h_itm_title = new Hashtable();
            if( v_itm_id == null || v_itm_id.isEmpty() )
                return h_itm_title;
            String SQL = " SELECT itm_id, itm_title "
                       + " FROM aeItem "
                       + " WHERE itm_id IN " + cwUtils.vector2list(v_itm_id);
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                h_itm_title.put(new Long(rs.getLong("itm_id")), rs.getString("itm_title"));
            stmt.close();
            return h_itm_title;
        }


	public static boolean isItemEnrollable(Connection con, long itm_id, long usr_ent_id)
		throws SQLException, cwException{

			aeItem aeItm = new aeItem();
			aeItm.itm_id = itm_id;
			try{
				aeItm.get(con);
			}catch(cwSysMessage e) {
				throw new SQLException(e.getMessage());
			}
			return aeItm.isEnrollable(con, usr_ent_id);

		}
	
	/*
	 * 返回值说明
	 * -1	不显示任何操作按钮
	 * 0 	显示购买
	 * 1 	显示开始学习（报自动报名后，直接开始学习）
	 * 2 	已报名，并可以开始学习，且可以取消报名
	 * 3 	已报名，并可以开始学习，但不可以取消报名，
	 * 4	显示“报名“按钮，因为课程需要审批”，
	 * 5           已报名，但状态为”等待审批，可以取消报名“
	 * 6           已报名，但状态为”等待队列，可以取消报名“，
	 */
	public static long itmButtonCon(Connection con, long itm_id, long usr_ent_id, CourseBean cb, long cru_app_id) throws SQLException, cwException {
		final long no_button_ind = -1;
		final long buy_ind = 0;
		final long apply_and_start_ind = 1;
		final long start_and_cancel_ind = 2;
		final long start_ind = 3;
		final long apply_ind = 4;
		final long pending_ind = 5;
		final long waiting_ind = 6;
		final long full_capacity = 7;
        final long full_capacity_wait = 8;
		Timestamp cur_time = cwSQL.getTime(con);
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		try {
			itm.get(con);
		} catch (cwSysMessage e) {
			throw new SQLException(e.getMessage());
		}
		if (itm.itm_create_run_ind) {
			// 暂时不支持离线课程
			return no_button_ind;
		} else {
			boolean isEnrolled = false;
            
            long max_app_id = aeApplication.getAppId(con, itm_id, usr_ent_id, true);
            if(cru_app_id ==0){
            	cru_app_id = max_app_id;
            }
			 
			aeApplication app = new aeApplication();
			app.app_id = max_app_id;
			if (max_app_id != 0) {
				try {
					isEnrolled = true;
					app.get(con);
				} catch (qdbException e) {
					//e.printStackTrace();
					CommonLog.error(e.getMessage(),e);
				}
			}

			if (isEnrolled) {
				if(cru_app_id == app.app_id){
					if (cb != null) {
						cb.setApp_id(app.app_id);
						cb.setApp_tkh_id(app.app_tkh_id);
						cb.setApp_cos_id(dbCourse.getCosResId(con, itm_id));
					}
					if (aeApplication.PENDING.equalsIgnoreCase(app.app_status)) {
						return pending_ind;
					}
					if (aeApplication.WAITING.equalsIgnoreCase(app.app_status)) {
						return waiting_ind;
					}
					boolean can_cancel = false;
					can_cancel = app.canCancel(con);
	
					if (can_cancel) {
						return start_and_cancel_ind;
					} else {
						return start_ind;
					}
				}else{
					app.app_id = cru_app_id;
					try {
						app.get(con);
					} catch (qdbException e) {
						//e.printStackTrace();
						CommonLog.error(e.getMessage(),e);
					}
					if (aeApplication.PENDING.equalsIgnoreCase(app.app_status)) {
						return pending_ind;
					}
					if (aeApplication.WAITING.equalsIgnoreCase(app.app_status)) {
						return waiting_ind;
					}
					boolean can_cancel = false;
					can_cancel = app.canCancel(con);
	
					if (can_cancel) {
						return start_and_cancel_ind;
					} else {
						return start_ind;
					}
					
				}

			} else {
				if (itm.isEnrollable(con, usr_ent_id)) {
					String[] process_status_lst = aeReqParam.split(LangLabel.getValue(LangLabel.Encoding_zh_cn, "process_status"), "~");
					app.itm_capacity = itm.itm_capacity;
					app.itm_id = itm.itm_id;
					
					//用户没报名，报名人数有限制并且已经满人，可以进入等待名单，按钮显示为：放上等待名單
                    if(app.itm_capacity <= aeApplication.countItemAppn(con, itm_id, true) && itm.itm_capacity > 0 && !itm.itm_not_allow_waitlist_ind ){
                        return full_capacity_wait;
                    }
                    

                    //用户没报名，报名人数有限制并且已经满人，可以进入等待名单，按钮显示为：放上等待名單（审批中的学员也算入名额）
                    if(app.itm_capacity <= aeApplication.countItemAppPending(con, itm_id) && itm.itm_capacity > 0 && !itm.itm_not_allow_waitlist_ind ){
                        return full_capacity_wait;
                    }

                    //判断报名各额。
                    if(app.itm_capacity <= aeApplication.countItemAppn(con, itm_id, true) && itm.itm_capacity > 0 && itm.itm_not_allow_waitlist_ind  ){
                        return full_capacity;
                    }
                    
                    //判断报名各额（审批中的学员也算入名额）
                    if(app.itm_capacity <= aeApplication.countItemAppPending(con, itm_id) && itm.itm_capacity > 0 && itm.itm_not_allow_waitlist_ind  ){
                        return full_capacity;
                    }
                    

					if (itm.itm_capacity < 1 || (itm.itm_capacity > 0 && (!itm.itm_not_allow_waitlist_ind || (!app.isItemCapacityExceed(con, process_status_lst, false))))) {
						if (itm.itm_app_approval_type != null && itm.itm_app_approval_type.trim().length() > 0) {
		                        return apply_ind;
						} else {
                                return apply_and_start_ind;
						}
					}
					//判断报名各额。
					if(app.itm_capacity <= aeApplication.countItemAppn(con, itm_id, true) && itm.itm_capacity >= 0){
						return full_capacity;
					}
				}
			}
		}
		return no_button_ind;
	}

	private boolean isEnrollable(Connection con, long usr_ent_id)
		throws SQLException, cwException {

			Timestamp cur_time = cwSQL.getTime(con);
			String xml = null;
			boolean enrollable = false;
			//Check item attribute
			if( ( this.itm_life_status == null || !this.itm_life_status.equalsIgnoreCase(ITM_LIFE_STATUS_CANCELLED) ) &&
				!this.itm_deprecated_ind &&
				this.itm_apply_ind &&
				( this.itm_appn_start_datetime != null && this.itm_appn_start_datetime.before(cur_time) ) &&
				( this.itm_appn_end_datetime != null && this.itm_appn_end_datetime.after(cur_time) )
			) {
                aeQueueManager qm = new aeQueueManager();
				List retake_itm_lst = qm.hasRetakeConflict(con,this.itm_id, usr_ent_id);
				if(!this.itm_retake_ind && retake_itm_lst.size() != 0) {                    //if has retake conflict, return false
                    enrollable = false;
                } else {
                    aeApplication app = new aeApplication();
                    app.app_id = aeApplication.getLatestApplicationId(con, this.itm_id, usr_ent_id);
                    if( app.app_id == 0 ) {
                        //learner has not applied this course yet
                        enrollable = true;
                    } else {
                        try{
                            app.get(con);
                        }catch(qdbException e){
                            throw new cwException(e.getMessage());
                        }

                        String ats_type = app.getAttandanceStatus(con);
                        if( ats_type == null ) {
                            //the applicaiton is cancelled
//the application is pending or waiting
                            enrollable = app.app_status.equalsIgnoreCase(aeApplication.REJECTED) || app.app_status.equalsIgnoreCase(aeApplication.WITHDRAWN);
                        } else {
                            enrollable = !ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_PROGRESS);
                        }
                    }
                }
            }
            
            if(enrollable && this.itm_run_ind) {
            	boolean itm_status_all = false;
            	if(null != this.itm_access_type && !this.itm_access_type.equals("")){
            		itm_status_all = this.itm_access_type.equalsIgnoreCase(aeItem.ITM_STATUS_ALL);
            	}
            
            	if(!itm_status_all){  //如果课程发布为 “所有学员”时,所有学员都可以报名 
            		enrollable = isTargetedLearner(con, usr_ent_id, this.itm_id, false);
            	}
            }
            
            return enrollable;
		}


    public static boolean isItemRetakable(Connection con, long itm_id)
        throws SQLException{
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        String SQL = null;
        if (itm.getRunInd(con)) {
           SQL = " Select itm_retake_ind From aeItem, aeItemRelation Where itm_id = ire_parent_itm_id And ire_child_itm_id =  "  +  itm_id;
        } else {
            SQL = " Select itm_retake_ind From aeItem Where itm_id =  "  +  itm_id;
        }
        PreparedStatement stmt = con.prepareStatement(SQL);
        //stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            itm.itm_retake_ind = rs.getBoolean("itm_retake_ind");
        }
        rs.close();
        stmt.close();
        return itm.itm_retake_ind;
        }

	private long getEnrollmentCount(Connection con) throws SQLException {
		String sql = "";
		sql += " select count(app_id) enrollmentCount from aeApplication where app_itm_id = ? ";

		ResultSet rs = null;
		PreparedStatement stmt = null;
		long count = 0l;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, this.itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getLong("enrollmentCount");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return count;
	}

    private static final String SQL_GET_ORG_RESPON_ITM_ID
        = " Select itm_id "
        + " From aeItem "
        + " Where itm_owner_ent_id = ? "
        + " And itm_deprecated_ind = ? ";

    // itm_id must be root level
    public static String getApprovalStatus(Connection con, long itm_id) throws SQLException{
        String approval_status = null;
        String SQL = "SELECT itm_approval_status FROM aeItem WHERE itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            approval_status = rs.getString("itm_approval_status");
        }
        stmt.close();
        return approval_status;
    }

    public void makeApprovalActn(Connection con, String action, String usr_id) throws SQLException, cwSysMessage, cwException{
        String cur_itm_approval_status = itm_approval_status;
        Timestamp curTime = cwSQL.getTime(con);
        if (cur_itm_approval_status==null){
            return;
        }
        if (action.equalsIgnoreCase(ITM_APPROVAL_ACTION_REQ_APPR)){
            if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_PREAPPROVE)){
                itm_approval_status = ITM_APPROVAL_STATUS_PENDING_APPROVAL;
                itm_submit_action = action;
                itm_submit_usr_id = usr_id;
                itm_submit_timestamp = curTime;
            }else if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_APPROVED_OFF)){
                itm_approval_status = ITM_APPROVAL_STATUS_PENDING_REAPPROVAL;
                itm_submit_action = action;
                itm_submit_usr_id = usr_id;
                itm_submit_timestamp = curTime;
            }
        }else if (action.equalsIgnoreCase(ITM_APPROVAL_ACTION_CANCEL_REQ_APPR)){
            if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_PENDING_APPROVAL)){
                itm_approval_status = ITM_APPROVAL_STATUS_PREAPPROVE;
                itm_submit_action = action;
                itm_submit_usr_id = usr_id;
                itm_submit_timestamp = curTime;
            }else if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_PENDING_REAPPROVAL)){
                itm_approval_status = ITM_APPROVAL_STATUS_APPROVED_OFF;
                itm_submit_action = action;
                itm_submit_usr_id = usr_id;
                itm_submit_timestamp = curTime;
            }
        }else if (action.equalsIgnoreCase(ITM_APPROVAL_ACTION_APPR_PUB)){
            if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_PENDING_APPROVAL) ||
                cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_PENDING_REAPPROVAL)){
                itm_approval_status = ITM_APPROVAL_STATUS_APPROVED;
                itm_status = ITM_STATUS_ON;
                itm_approval_action = action;
                itm_approve_usr_id = usr_id;
                itm_approve_timestamp = curTime;
            }
        }else if (action.equalsIgnoreCase(ITM_APPROVAL_ACTION_DECLINE_APPR_PUB)){
            if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_PENDING_APPROVAL)){
                itm_approval_status = ITM_APPROVAL_STATUS_PREAPPROVE;
                itm_approval_action = action;
                itm_approve_usr_id = usr_id;
                itm_approve_timestamp = curTime;
            }else if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_PENDING_REAPPROVAL)){
                itm_approval_status = ITM_APPROVAL_STATUS_APPROVED_OFF;
                itm_approval_action = action;
                itm_approve_usr_id = usr_id;
                itm_approve_timestamp = curTime;
            }
        }else if (action.equalsIgnoreCase(ITM_APPROVAL_ACTION_PUB)){
            if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_APPROVED_OFF)){
                itm_approval_status = ITM_APPROVAL_STATUS_APPROVED;
                itm_status = ITM_STATUS_ON;
            }
        }else if (action.equalsIgnoreCase(ITM_APPROVAL_ACTION_UNPUB)){
            if (cur_itm_approval_status.equals(ITM_APPROVAL_STATUS_APPROVED)){
                itm_approval_status = ITM_APPROVAL_STATUS_APPROVED_OFF;
                itm_status = ITM_STATUS_OFF;
            }
        }
        Vector vColName = new Vector();
        Vector vColType = new Vector();
        Vector vColValue = new Vector();

        vColName.addElement("itm_approval_status");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_approval_status);

        vColName.addElement("itm_submit_action");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_submit_action);

        vColName.addElement("itm_submit_usr_id");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_submit_usr_id);

        vColName.addElement("itm_submit_timestamp");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(itm_submit_timestamp);

        vColName.addElement("itm_approval_action");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_approval_action);

        vColName.addElement("itm_approve_usr_id");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_approve_usr_id);

        vColName.addElement("itm_approve_timestamp");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(itm_approve_timestamp);

        vColName.addElement("itm_status");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(itm_status);

        updItem(con, vColName, vColType, vColValue, null, null);
    }

    /**
    Update this item's application approval type.
    Application approval type can only be changed in course level (not run level)
    Pre-defined variables: itm_id
                           itm_app_approval_type
                           itm_upd_timestamp
    @parma con Connection to database
    */
    public void updAppApprovalType(Connection con) throws SQLException, qdbException, cwSysMessage {

        String in_itm_app_approval_type = this.itm_app_approval_type;
        Timestamp in_itm_upd_timestamp = this.itm_upd_timestamp;
        boolean itm_not_allow_waitlist_ind = this.itm_not_allow_waitlist_ind;

        //check if the item's last update timestamp
        if(!isLastUpd(con, in_itm_upd_timestamp)) {
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        }

        //get the item's details
        getItem(con);

        if(this.itm_run_ind || this.itm_session_ind) {
            //if the item is a run or a session,
            //the workflow should not be changed in this level
            throw new cwSysMessage(ITM_CHANGE_WRK_CLASS_LEVEL);
        } else {
            //check if the item has a workflow attached to it
            ViewItemTemplate viewIt = new ViewItemTemplate();
            viewIt.itemId = this.itm_id;
            if(viewIt.getItemSelectedWorkflowTemplateId(con) == 0) {
                throw new cwSysMessage(ITM_CHANGE_WRK_INCORRECT_ITM_TYPE);
            }
        }

        if(aeApplication.isItemAppnExist(con, this.itm_id, aeApplication.PENDING)) {
            //workflow cannot be changed if the item (and its runs) has pending application
            throw new cwSysMessage(ITM_CHANGE_WRK_PENDING_APPN);
        } else {
            //start to change workflow (both the item itself and its runs)
            StringBuffer SQLBuf = new StringBuffer();
            PreparedStatement stmt = null;
            SQLBuf.append(" Update aeItem Set itm_app_approval_type = ? ")
//            	  .append(" , itm_not_allow_waitlist_ind = ?")
                  .append(" Where (itm_id = ? ")
                  .append(" Or itm_id In ( ")
                  .append(" Select ire_child_itm_id ")
                  .append(" From aeItemRelation ")
                  .append(" Where ire_parent_itm_id = ? ))");

            try {
                stmt = con.prepareStatement(SQLBuf.toString());
                int index = 1;
                stmt.setString(index++, in_itm_app_approval_type);
//                stmt.setBoolean(index++, itm_not_allow_waitlist_ind);
                stmt.setLong(index++, this.itm_id);
                stmt.setLong(index++, this.itm_id);
                stmt.executeUpdate();
            } finally {
                if(stmt!=null) {stmt.close();}
            }
        }
        return;
    }
    
    public  HashMap transCosItmId(Connection con,Vector itmId) throws SQLException{
//    	Vector[] ids = new Vector[2];
    	Vector itms = new Vector();
//    	Vector oldItms = new Vector();
        HashMap hm = new HashMap();
		String sql = OuterJoinSqlStatements.transCosId(itmId);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs =null;
		try{
			rs = stmt.executeQuery();
			while(rs.next()){
				Long itmid = new Long(rs.getLong("new_itm_id"));
				Long oldid = new Long(rs.getLong("p_id"));
				itms.add(itmid);
				//oldItms.add(oldid);
				hm.put(itmid,oldid);
			}
		}catch(Exception e){
			throw new SQLException(e.getMessage());  
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
		}
		hm.put("keys",itms);
    	return hm;
    }
    
    private static final String SQL_GET_TCR_NAME
        = "Select tcr_title From tcTrainingCenter Where tcr_id = ? ";

    private String getTrainingCenterXML(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        StringBuffer xmlBuf = new StringBuffer();
        try {
            stmt = con.prepareStatement(SQL_GET_TCR_NAME);
            stmt.setLong(1, this.itm_tcr_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                xmlBuf.append("<training_center>");
                xmlBuf.append("<center id=\"").append(this.itm_tcr_id).append("\">")
                      .append(cwUtils.esc4XML(rs.getString("tcr_title"))).append("</center>");
                xmlBuf.append("</training_center>");
            }
        }finally {
            if(stmt!=null) stmt.close();
        }
        return xmlBuf.toString();
    }

    private static final String SQL_UPD_ITM_TCR_ID 
        = " Update aeItem Set itm_tcr_id = ? Where itm_id = ? ";

    private void updateItmTcrId(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
        	if(this.itm_tcr_id != -1) {
        		stmt = con.prepareStatement(SQL_UPD_ITM_TCR_ID);
        		if(this.itm_tcr_id == 0){
        			stmt.setNull(1, java.sql.Types.INTEGER);
        		} else{
        			stmt.setLong(1, this.itm_tcr_id);
        		}
        		stmt.setLong(2, this.itm_id);
        		stmt.executeUpdate();
        	}
        } finally {
            if(stmt!=null) stmt.close();
        }
        
    }
    
    private static final String SQL_UPD_CHILD_CLASS_ITM_TCR_ID
    	= "Update aeItem Set itm_tcr_id = ? Where itm_id in ("
    		+ " Select itm_id From aeItem"
    		+ " Inner join aeItemRelation on itm_id = ire_child_itm_id"
    		+ " Where ire_parent_itm_id = ?)";
    
    private void updateChildClassItmTcrId(Connection con) throws SQLException {
    	PreparedStatement stmt = null;
    	try {
    		if(this.itm_tcr_id != -1) {
    			stmt = con.prepareStatement(SQL_UPD_CHILD_CLASS_ITM_TCR_ID);
    			if(this.itm_tcr_id == 0){
    				stmt.setNull(1, java.sql.Types.INTEGER);
    			} else{
    				stmt.setLong(1, this.itm_tcr_id);
    			}
    			stmt.setLong(2, this.itm_id);
    			stmt.executeUpdate();
    		}
    	} finally {
    		if(stmt!=null) stmt.close();
    	}
    	
    }

    public String getDefaultTrainingCenterXML(Connection con, loginProfile prof) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer();
        long default_tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
        DbTrainingCenter tcr = DbTrainingCenter.getInstance(con, default_tcr_id);
        if(tcr != null) {
        	xmlBuf.append("<training_center>");
        	xmlBuf.append("<center id=\"").append(tcr.getTcr_id()).append("\">")
        	.append(cwUtils.esc4XML(tcr.getTcr_title())).append("</center>");
        	xmlBuf.append("</training_center>");
        }
        return xmlBuf.toString();
    }
    
    public String getCourseTrainingCenterXML(Connection con, long itm_id) throws SQLException {
    	StringBuffer xmlBuf = new StringBuffer();
    	long default_tcr_id = getTrainingCenterByItmId(con, itm_id);
    	DbTrainingCenter tcr = DbTrainingCenter.getInstance(con, default_tcr_id);
    	if(tcr != null) {
    		xmlBuf.append("<training_center>");
    		xmlBuf.append("<center id=\"").append(tcr.getTcr_id()).append("\">")
    		.append(cwUtils.esc4XML(tcr.getTcr_title())).append("</center>");
    		xmlBuf.append("</training_center>");
    	}
    	return xmlBuf.toString();
    }
    
    public Long getTrainingCenterByItmId(Connection con, long itm_id) {
    	long tcr_id = 0L;
    	PreparedStatement stmt = null;
    	try {
    		String sql = "select itm_tcr_id from aeItem where itm_id = ?";
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, itm_id);
    		ResultSet rs = stmt.executeQuery();
    		if(rs.next()) {
    			tcr_id = rs.getLong("itm_tcr_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return tcr_id;
    }

    public static String getDefaultTCR4Search(Connection con, long usr_ent_id, String rol_ext_id) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer();

        //first, test if the role is a TCR officer
        AccessControlWZB acl = new AccessControlWZB();
        ViewTrainingCenter viewTCR = new ViewTrainingCenter();
        List lTcr = null;
        if(acl.isTcOfficerRole(con, rol_ext_id)) {
            //get the officer's assigned training centers
            lTcr = viewTCR.getTrainingCenterByOfficer(con, usr_ent_id, rol_ext_id, false);
        } else {
            //get the training centers that is in charge of the user's group
            lTcr = viewTCR.getTrainingCenterByTargetUser(con, usr_ent_id);
        }
        if(lTcr != null) {
        	Vector vec = new Vector();

			for (int i = 0; i < lTcr.size(); i++) {
				DbTrainingCenter tcr = (DbTrainingCenter) lTcr.get(i);
				if (!vec.contains(new Long(tcr.tcr_id))) {
					vec.add(new Long(tcr.tcr_id));
				}
				Vector childs = DbTrainingCenter.getChildTc(con, tcr.tcr_id);
				if (childs != null && childs.size() > 0) {
					for (int j = 0; j < childs.size(); j++) {
						if (!vec.contains(childs.elementAt(j))) {
							vec.add(childs.elementAt(j));
						}	
					}
				}
			}
			
			long[] tcr_id_lst = new long[vec.size()];
			if (vec != null && vec.size() > 0) {
				for (int j = 0; j < vec.size(); j++) {
					tcr_id_lst[j] = ((Long) vec.elementAt(j)).longValue();
				}
			}
			
			for (int i = 0; i < tcr_id_lst.length; i++) {
				long tcr_id = tcr_id_lst[i];
				DbTrainingCenter tcr = new DbTrainingCenter();
				tcr.tcr_id = tcr_id;
				tcr.get(con);
				
				xmlBuf.append("<training_center_list>");
                xmlBuf.append("<center id=\"").append(tcr.getTcr_id()).append("\">")
                      .append(cwUtils.esc4XML(tcr.getTcr_title())).append("</center>");
                xmlBuf.append("</training_center_list>");    
			}
        }
        return xmlBuf.toString();
    }
    //insCriteria(Connection con, long itm_id, String type, String upd_method, int attendance_rate, 
   // int pass_score, boolean must_pass, boolean must_meet_all_cond, String usr_id)
    
    public void genCmtFromParent(Connection con, long parent_itm_id, long itm_id, loginProfile prof, boolean data_trans) throws SQLException, cwException, cwSysMessage {
        String sql = "select * from CourseCriteria where ccr_itm_id=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,parent_itm_id);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			String type = rs.getString("ccr_type");
			String upd_method = rs.getString("ccr_upd_method");
			int attendance_rate = rs.getInt("ccr_attendance_rate");
			int pass_score = rs.getInt("ccr_pass_score");
			boolean must_pass = rs.getBoolean("ccr_pass_ind") ;
			boolean must_meet_all_cond = rs.getBoolean("ccr_all_cond_ind");
			String offline_condition = rs.getString("ccr_offline_condition");
			CourseCriteria.insCriteria(con, itm_id, type, upd_method,  attendance_rate, pass_score, must_pass, must_meet_all_cond , prof.usr_id, offline_condition, data_trans);

		}
		pstmt.close();
	
        
    }
    /*
    public void genCmtFromParent(Connection con, long parent_itm_id, long itm_id, loginProfile prof) throws SQLException {
        String sql1 = "INSERT INTO CourseCriteria(ccr_pass_score, ccr_duration, ccr_pass_ind, ccr_all_cond_ind, ccr_create_timestamp, ccr_create_usr_id, ccr_upd_timestamp, ccr_upd_usr_id, ccr_type, ccr_upd_method, ccr_itm_id, ccr_attendance_rate, ccr_offline_condition, ccr_ccr_id_parent)"
                         + " (select ccr_pass_score, ccr_duration, ccr_pass_ind, ccr_all_cond_ind, ?, ?, ?, ?, ccr_type, ccr_upd_method, ?, ccr_attendance_rate, ccr_offline_condition, ccr_id from CourseCriteria where ccr_itm_id=?)";
        PreparedStatement stmt = con.prepareStatement(sql1);
        Timestamp upd_time = cwSQL.getTime(con);
        int index = 1;
        stmt.setTimestamp(index++, upd_time);
        stmt.setString(index++, prof.usr_id);
        stmt.setTimestamp(index++, upd_time);
        stmt.setString(index++, prof.usr_id);
        stmt.setLong(index++, itm_id);
        stmt.setLong(index++, parent_itm_id);
        int ss = stmt.executeUpdate();
        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
        ccr.getCcrIdByItmNType(con);
        DbCourseCriteria ccrp = new DbCourseCriteria();
        ccrp.ccr_itm_id = parent_itm_id;
        ccrp.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
        ccrp.getCcrIdByItmNType(con);
        String sql2 = "INSERT INTO CourseMeasurement(cmt_title, cmt_ccr_id, cmt_cmr_id, cmt_max_score, cmt_status, cmt_contri_rate, cmt_is_contri_by_score, cmt_create_timestamp, cmt_create_usr_id, cmt_update_timestamp, cmt_update_usr_id, cmt_delete_timestamp, cmt_pass_score, cmt_status_desc_option, cmt_order)"
                    + " (select cmt_title, ?, cmt_cmr_id,cmt_max_score, cmt_status, cmt_contri_rate, cmt_is_contri_by_score, ?, ?, ?, ?, cmt_delete_timestamp,cmt_pass_score,cmt_status_desc_option, cmt_order from CourseMeasurement"
                    + " where cmt_ccr_id=?)";
        PreparedStatement stmt2 = con.prepareStatement(sql2);
        index = 1;
        stmt2.setLong(index++, ccr.ccr_id);
        stmt2.setTimestamp(index++, upd_time);
        stmt2.setString(index++, prof.usr_id);
        stmt2.setTimestamp(index++, upd_time);
        stmt2.setString(index++, prof.usr_id);
        stmt2.setLong(index++, ccrp.ccr_id);
        stmt2.executeUpdate();
    }
    */
    
    public void genLessonFromParent(Connection con, long parent_itm_id, long itm_id, loginProfile prof) throws SQLException {
        /*String sql = "INSERT INTO aeItemLesson(ils_itm_id, ils_title, ils_day, ils_start_time, ils_end_time, ils_create_timestamp, ils_create_usr_id, ils_update_timestamp, ils_update_usr_id)"
                   + " select ?, ils_title, ils_day, ils_start_time, ils_end_time, ?, ?, ?, ? from aeItemLesson where ils_itm_id=?";
        */
    	String sql_sel = " select ils_title, ils_day, ils_start_time, ils_end_time, ils_place, " 
			    	+ "ils_date, ils_qiandao, ils_qiandao_chidao, ils_qiandao_queqin,"
					+ "ils_qiandao_youxiaoqi, ils_qiandao_chidao_time, ils_qiandao_queqin_time, ils_qiandao_youxiaoqi_time "
			    	+ "from aeItemLesson where ils_itm_id= ?";
    	String sql = "INSERT INTO aeItemLesson(ils_itm_id, ils_title, ils_day, ils_start_time, ils_end_time, ils_create_timestamp, ils_create_usr_id, ils_update_timestamp, ils_update_usr_id, " 
    			+ "ils_place , ils_date, ils_qiandao, ils_qiandao_chidao, ils_qiandao_queqin, ils_qiandao_youxiaoqi,"
				+ "ils_qiandao_chidao_time, ils_qiandao_queqin_time, ils_qiandao_youxiaoqi_time )"
    			+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	PreparedStatement stmt = null;
    	PreparedStatement stmt_sel = null;
    	ResultSet rs = null;
        try {
	        stmt_sel = con.prepareStatement(sql_sel);
	        stmt_sel.setLong(1, parent_itm_id);
	        rs = stmt_sel.executeQuery();
	        while(rs.next()) {
		        stmt = con.prepareStatement(sql);
		        int index = 1;
		        Timestamp upd_time = cwSQL.getTime(con);
		        stmt.setLong(index++, itm_id); 
		        stmt.setString(index++, rs.getString("ils_title"));
		        stmt.setInt(index++, rs.getInt("ils_day"));
		        stmt.setTimestamp(index++, rs.getTimestamp("ils_start_time"));
		        stmt.setTimestamp(index++, rs.getTimestamp("ils_end_time"));		        
		        stmt.setTimestamp(index++, upd_time);
		        stmt.setString(index++, prof.usr_id);
		        stmt.setTimestamp(index++, upd_time);
		        stmt.setString(index++, prof.usr_id);
		        stmt.setString(index++, rs.getString("ils_place"));
		        stmt.setTimestamp(index++, rs.getTimestamp("ils_date"));
		        stmt.setInt(index++, rs.getInt("ils_qiandao"));
		        stmt.setInt(index++, rs.getInt("ils_qiandao_chidao"));
		        stmt.setInt(index++, rs.getInt("ils_qiandao_queqin"));
		        stmt.setInt(index++, rs.getInt("ils_qiandao_youxiaoqi"));
		        stmt.setTimestamp(index++, rs.getTimestamp("ils_qiandao_chidao_time"));
		        stmt.setTimestamp(index++, rs.getTimestamp("ils_qiandao_queqin_time"));
		        stmt.setTimestamp(index++, rs.getTimestamp("ils_qiandao_youxiaoqi_time"));
		        stmt.executeUpdate();
	        }
        } finally {
        	if (stmt != null) stmt.close();
        	if (rs != null) rs.close();
        	if (stmt_sel != null) stmt_sel.close();
        }
    }
    
    public void delItemLesson(Connection con) throws SQLException {
        aeItemLessonInstructor.delByItem(con, itm_id, null);
        aeItemLesson.delByItemId(con, itm_id);
    }

    public void setContentDef(Connection con) throws SQLException, cwException {
        final String SQL =
            " update aeItem set itm_content_def = ? "
          + " where (itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id = ?) or itm_id = ?) "
          + " and itm_content_def is null";
        PreparedStatement stmt = con.prepareStatement(SQL);
        try {
            if (this.itm_content_def != null && (this.itm_content_def.equals("PARENT") || this.itm_content_def.equals("CHILD"))) {
                int idx = 1;
                stmt.setString(idx++, this.itm_content_def);
                stmt.setLong(idx++, this.itm_id);
                stmt.setLong(idx++, this.itm_id);
                int row = stmt.executeUpdate();
                if (row == 0) {
                    throw new SQLException("update failed. cannot update aeItem of itm_id = " + this.itm_id);
                }
            } else {
                throw new cwException("invalid itm_content_def:" + this.itm_content_def);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
	/*    public String getEnrollTypeAsXML(qdbEnv env) {
	    	StringBuffer str = new StringBuffer();
	    	str.append("<auto_enroll_target_learners value=\"").append(this.itm_enroll_type).append("\" ");
	//    	str.append("interval=\"").append(env.)
    }*/
    
    /**
     * Get indicates that if need to send enroll mail notification.
     Predefine variable: <code>itm_id</code>
     * @param con datatbase connection
     * @throws SQLException 
     */
    public void getSendMailInd(Connection con) throws SQLException {
    	String sql = "select itm_send_enroll_email_ind from aeItem where itm_id=?";
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	try {
    		pstmt = con.prepareStatement(sql);
    		pstmt.setLong(1, this.itm_id);
    		rs = pstmt.executeQuery();
    		if (rs.next()) {
    			this.itm_send_enroll_email_ind = rs.getLong(1);
    		}
    	} finally {
    		cwSQL.closeResultSet(rs);
    		cwSQL.closePreparedStatement(pstmt);
    	}
    }
    
    public List getChItemIDList(Connection con,long itm_id)throws SQLException{
    	List ch_id_list=new ArrayList();
    	String SQL = "SELECT ire_child_itm_id from aeitemrelation where ire_parent_itm_id=?";
    	PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
        	ch_id_list.add(new Long(rs.getLong("ire_child_itm_id")));
        }
        stmt.close();
        return ch_id_list;        
    }
 
    public static boolean isCosLevContentClass (Connection con,long itm_id) throws SQLException{
        boolean result = false;
        boolean create_run = false;
        boolean run = false;
        boolean apply = false;
        String content_def="";
        String SQL = "SELECT itm_create_run_ind, itm_run_ind,itm_apply_ind, itm_content_def FROM aeItem WHERE itm_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
        	create_run = rs.getBoolean("itm_create_run_ind");
        	run = rs.getBoolean("itm_run_ind");
        	apply = rs.getBoolean("itm_apply_ind");
        	content_def=rs.getString("itm_content_def");
        }
        if(!create_run && run && apply && content_def!=null && content_def.equalsIgnoreCase("PARENT")){
        	result=true;
        }
        stmt.close();
        return result;
    }
    
    public static String getItemXMLForNav (Connection con,long itm_id) throws SQLException,  cwSysMessage {
        StringBuffer appXML = new StringBuffer(1024);
        if(itm_id > 0){
           
            Timestamp cur_time = cwSQL.getTime(con);
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            itm.getItem(con);
    
            appXML.append("<item id=\"").append(itm.itm_id).append("\"")
                  .append(" title=\"").append(cwUtils.esc4XML(itm.itm_title)).append("\"")
                  .append(" run_ind=\"").append(itm.itm_run_ind).append("\"")
                  .append(" life_status=\"").append(cwUtils.esc4XML(itm.itm_life_status)).append("\"")
                  .append(" appn_eff_start_datetime=\"").append(itm.itm_appn_start_datetime).append("\"")
                  .append(" appn_eff_end_datetime=\"").append(itm.itm_appn_end_datetime).append("\"")
                  .append(" cur_time=\"").append(cur_time).append("\"")
                  .append(" app_approval_type=\"").append(cwUtils.escNull(itm.itm_app_approval_type)).append("\"")
                  .append(">");
            if(itm.itm_run_ind) {
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = itm.itm_id;
                aeItem ireParentItm = ire.getParentInfo(con);
                if (ireParentItm != null) {
                    itm.parent_itm_id = ireParentItm.itm_id;
                    
                    appXML.append("<parent id=\"").append(itm.parent_itm_id).append("\"")
                          .append(" title=\"").append(dbUtils.esc4XML(aeUtils.escNull(ireParentItm.itm_title))).append("\"/>");
                }
            }
            appXML.append("</item>");
        }
        return appXML.toString();
    }
    
    public static void updateItemQuotaExceedNotifyTimestamp(Connection con, long itm_id, Timestamp timestamp) throws SQLException {
    	String sql = "update aeItem set itm_qte_notify_timestamp = ? where itm_id=?";
    	PreparedStatement pstmt = con.prepareStatement(sql);
    	pstmt.setTimestamp(1, timestamp);
    	pstmt.setLong(2, itm_id);
    	pstmt.executeUpdate();
    	pstmt.close();
    }

	public static long getTcrId(Connection con, long in_itm_id, long root_ent_id) throws SQLException {
		String sql = "select itm_tcr_id from aeItem where itm_id = ? and itm_owner_ent_id = ?";
		long out_tcr_id = 0;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = con.prepareStatement(sql);
        	pstmt.setLong(1, in_itm_id);
        	pstmt.setLong(2, root_ent_id);
        	ResultSet rs = pstmt.executeQuery();
        	if (rs.next()) {
        		out_tcr_id = rs.getLong("itm_tcr_id");
        	}
    	} finally {
    		if (pstmt != null) {
    			pstmt.close();
    		}
    	}
		return out_tcr_id;
	}
	
	public static String getPublishTargetXML(aeReqParam urlp) {
		StringBuffer xml = new StringBuffer(512);
        xml.append("<item>");
        if(urlp.itm_id_lst != null && urlp.itm_id_lst.length != 0) {
        	xml.append("<id>").append(urlp.itm_id_lst[0]).append("</id>");
        }
        if(urlp.itm_in_upd_timestamp_lst != null && urlp.itm_in_upd_timestamp_lst.length != 0) {
        	xml.append("<timestamp>").append(urlp.itm_in_upd_timestamp_lst[0]).append("</timestamp>");
        }
        if(urlp.itm_status_lst != null && urlp.itm_status_lst.length != 0) {
        	xml.append("<status>").append(urlp.itm_status_lst[0]).append("</status>");
        }
        xml.append("</item>");
		return xml.toString();
	}
	
	public void updTargetEnrolType (Connection con, loginProfile prof) throws SQLException {
		Timestamp cur_time = cwSQL.getTime(con);
    	String sql = "update aeItem set itm_target_enrol_type = ?, itm_upd_timestamp = ?, itm_upd_usr_id = ? where itm_id=?";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setString(index++, itm_target_enrol_type);
    	stmt.setTimestamp(index++, cur_time);
    	stmt.setString(index++, prof.usr_id);
    	stmt.setLong(index++, itm_id);
    	stmt.executeUpdate();
    	stmt.close();
	}
	
	public void updItmPublishTime(Connection con, long itm_id ,Timestamp curTime) throws SQLException{		
		String sql="update aeItem set itm_publish_timestamp=? where itm_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setTimestamp(index++, curTime);
    	stmt.setLong(index++, itm_id);
    	stmt.executeUpdate();
    	stmt.close();
	}
	/*
	 * 取得当前类型课程中不需要添加到搜索内容中的字段
	 */
	public Hashtable getSrhContentField(Connection con) throws SQLException, IOException, cwException {
		Hashtable ht = new Hashtable();
		aeTemplate at = this.getItemTemplate(con);
		xmlObj xml = new xmlObj(at.tpl_xml);

		Element element = null;
		Element subElement = null;

		for (int i = 0; i < xml.elements.getLength(); i++) {
			element = (Element) xml.elements.item(i);
			if ((element.getTagName()).equals("template")) {
				NodeList nodelist = element.getChildNodes();
				for (int j = 0; j < nodelist.getLength(); j++) {
					if ((nodelist.item(j)).getNodeType() == Node.ELEMENT_NODE) {
						subElement = (Element) nodelist.item(j);
						if (subElement.getAttribute("addSrhContent") != null 
								&& subElement.getAttribute("addSrhContent").toString().equalsIgnoreCase("true")) {
							ht.put(subElement.getNodeName(), subElement.getAttribute("addSrhContent"));
						}
					}
				}
				break;
			}
		}
		return ht;
	}

	public static Hashtable getItemIdAndXmlMap(Connection con) throws SQLException {
		Hashtable itemIdAndXmlMap = new Hashtable();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement(SqlStatements.getItemIdAndXmlList());
			rs = stmt.executeQuery();
			while (rs.next()) {
				itemIdAndXmlMap.put(new Long(rs.getLong("itm_id")), rs.getString("itm_xml"));
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}

		return itemIdAndXmlMap;
	}
	
	public static boolean isExam(Connection con, long cos_id) throws SQLException {
		boolean itm_exam_ind = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " SELECT itm_exam_ind FROM Course, aeItem WHERE cos_itm_id = itm_id AND cos_res_id = ? ";
		try {
			int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(index++, cos_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				itm_exam_ind = rs.getBoolean("itm_exam_ind");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return itm_exam_ind;
	}
	
	//根据mod_id 判断是否为“网上考试”或者“混合式考试”的模块
	public static boolean isOnlineExam(aeItem itm) throws SQLException {
		boolean is_online_exam = false;
		if (itm.itm_exam_ind && (itm.itm_exam_ind || itm.itm_type.equals(aeItem.ITM_TYPE_SELFSTUDY) || itm.itm_type.equals(aeItem.ITM_TYPE_VIDEO))) {
			is_online_exam = true;
		}
		return is_online_exam;
	}
	
	//考试监考员获取所有自己可以管理的考试
	public static String getExamItemListAsXml(Connection con, long usr_ent_id, String rol_ext_id, cwPagination cwPage) throws SQLException {
		
		StringBuffer result = new StringBuffer();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
			cwPage.sortCol = "itm_code";
		}
		if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
			cwPage.sortOrder = "desc";
		}
		cwPage.setRec();
		
		String get_exam_lst_sql = 
			" SELECT itm_id, itm_code, itm_title, itm_status, tcr_title, usr_display_bil, itm_upd_timestamp "+
			" FROM aeItem, aeItemAccess, tcTrainingCenter, reguser "+
			" WHERE iac_access_type = ? "+
			"   AND iac_access_id = ? " +
			"   AND usr_id = itm_create_usr_id " +
			"   AND iac_ent_id = ? " +
			"   AND itm_id = iac_itm_id " +
			"   AND itm_id = iac_itm_id " +
//                    屏蔽考试
			"   AND itm_exam_ind != 1" +
			" ORDER BY " + cwPage.sortCol + " " + cwPage.sortOrder;
		
		try {
			
			stmt = con.prepareStatement(get_exam_lst_sql);
			int index = 1;
			stmt.setString(index++, aeItemAccess.ACCESS_TYPE_ROLE);
			stmt.setString(index++, rol_ext_id);
			stmt.setLong(index++, usr_ent_id);
			rs = stmt.executeQuery();
			
			result.append("<exam_itm_lst>");
			while (rs.next()) {
				cwPage.totalRec++;
				if (cwPage.totalRec >= cwPage.startRec && cwPage.totalRec < cwPage.endRec) {
					result.append("<exam_itm id=\"").append(rs.getLong("itm_id")).append("\">")
					      .append("<title>").append(cwUtils.esc4XML(rs.getString("itm_title"))).append("</title>")
					      .append("<code>").append(cwUtils.esc4XML(rs.getString("itm_code"))).append("</code>")
					      .append("<status>").append(rs.getString("itm_status")).append("</status>")
					      .append("<tcraining_center>").append(cwUtils.esc4XML(rs.getString("tcr_title"))).append("</tcraining_center>")
					      .append("<usr_display_bil>").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("</usr_display_bil>")
					      .append("<itm_upd_timestamp>").append(rs.getTimestamp("itm_upd_timestamp")).append("</itm_upd_timestamp>")
					      .append("</exam_itm>");
				}
			}
			result.append("</exam_itm_lst>");
			
			cwPage.setTotalPage();
			result.append(cwPage.asXML());
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result.toString();
	}
	
	public static aeItem getItemByContentMod(Connection inCon, long mod_res_id) throws SQLException {
		
		String sql = " SELECT itm_type, itm_id, itm_exam_ind, itm_blend_ind " +
					 " FROM aeItem, course, resourceContent " +
			         " WHERE cos_itm_id = itm_id " +
			         "   AND cos_res_id = rcn_res_id " +
			         "   AND rcn_res_id_content = ? ";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		aeItem item = null;
		try {
			int index = 1;
			stmt = inCon.prepareStatement(sql);
			stmt.setLong(index++, mod_res_id);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				item = new aeItem();
				item.itm_id = rs.getLong("itm_id");
				item.itm_exam_ind = rs.getBoolean("itm_exam_ind");
				item.itm_blend_ind = rs.getBoolean("itm_blend_ind");
				item.itm_type = rs.getString("itm_type");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return item;
	}
	
	/**
	 * 统计课程数(如果传进来的itm_id 有包含班级id的话，把该班级所属的课程也计算进去)
	 * @param itmIds
	 * @return 课程数
	 */
	public static int getCourseCnt(Connection con, List itmIds) throws SQLException {
		List itmLst = new ArrayList();
    	if (itmIds == null || itmIds.size() == 0) {
    		return 0;
    	}
    	String itmTableName = null;
    	String itmIdColName = "itm_ids";
    	
    	itmTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
		if(itmTableName != null) {
			cwSQL.insertSimpleTempTable(con, itmTableName, itmIds, cwSQL.COL_TYPE_LONG);
		}
		String sql = " SELECT cItm.itm_id c_itm_id, pItm.itm_id p_itm_id, cItm.itm_type c_itm_type "
				   + " FROM aeItem cItm "
				   + " LEFT JOIN aeItemRelation on (ire_child_itm_id = cItm.itm_id) "
				   + " LEFT JOIN aeItem pItm on (ire_parent_itm_id = pItm.itm_id) "
				   + " WHERE EXISTS (SELECT " + itmIdColName + " FROM " + itmTableName + " WHERE " + itmIdColName + " = cItm.itm_id) ";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			long itm_id;
			Long itmIdObj = null;
			String itm_type = null;
			boolean flag = false;
			while(rs.next()) {
				itm_id = rs.getLong("p_itm_id");
				//班级
				if (itm_id > 0) {
					flag = true;
				} else {
					itm_type = rs.getString("c_itm_type");
					//网上课程或者线下课程
					if (itm_type.equals(ITM_TYPE_SELFSTUDY) || itm_type.equals(ITM_TYPE_CLASSROOM) || itm_type.equals(ITM_TYPE_VIDEO)) {
						itm_id = rs.getLong("c_itm_id");
						flag = true;
					}
				}
				if (flag) {
					itmIdObj = new Long(itm_id);
					if (itmLst.indexOf(itmIdObj) == -1) {
						itmLst.add(itmIdObj);
					}
				}
				flag = false;
			}
		} finally {
			if (itmTableName != null) {
				cwSQL.dropTempTable(con, itmTableName);
			}
			cwSQL.cleanUp(rs, stmt);
		}
		return itmLst.size();
	}
	
	/**
	 * @return 所有的课程类型，不包括资料
	 */
	public static String[] getItemCosType() {
		return new String[]{ITM_TYPE_SELFSTUDY, ITM_TYPE_CLASSROOM,ITM_TYPE_VIDEO};
	}
	
	/**
     * Check if a item use bonus point by given item id.
     * @param con Db connection.
     * @param itm_id Item id.
     * @return True if the item use bonus point.
     * @throws SQLException
     */
    public static boolean getItmBonusIndByItmId(Connection con, long itm_id) throws SQLException {
    	String sql_get_itm_bnp_ind_by_itm_id = " select itm_bonus_ind from aeItem where itm_id = ? ";
    	boolean result = false;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql_get_itm_bnp_ind_by_itm_id);
    		stmt.setLong(1, itm_id);
    		rs = stmt.executeQuery();
    		if (rs.next())
    			result = rs.getBoolean(1);
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
        return result;
    }
    
    public static float getItmDiffFactorByItmId(Connection con, long itm_id) throws SQLException {
    	String sql = " select itm_diff_factor from aeItem where itm_id = ? ";
    	float result = 0;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, itm_id);
    		rs = stmt.executeQuery();
    		if (rs.next())
    			result = rs.getFloat(1);
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
        return result;
    }
    
    public static boolean isIntegratedItem(Connection con, long itm_id) throws SQLException {
    	String sql = " select itm_integrated_ind from aeItem where itm_id = ? ";
    	boolean result = false;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		stmt.setLong(1, itm_id);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			result = rs.getBoolean("itm_integrated_ind");
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return result;
    }
    
	public static String isRefByIntegratedItem(Connection con, long itm_id) throws SQLException {
		String sql = " ";
		sql += " select distinct itm_id, itm_title ";
		sql += "  from aeItem, IntegCourseCriteria, IntegCompleteCondition, IntegRelationItem ";
		sql += " where itm_id = icc_itm_id and icd_icc_id = icc_id and iri_icd_id = icd_id ";
		sql += "   and iri_relative_itm_id = ? ";

		String result = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result += (result.trim().length() == 0 ? "" : ", ") + rs.getString("itm_title");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}

    private long getIntgForumModId(Connection con) throws SQLException {
    	long mod_id = 0;
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select rcn_res_id_content")
    		.append(" from course")
    		.append(" inner join ResourceContent on (rcn_res_id = cos_res_id)")
    		.append(" inner join Resources on (res_id = rcn_res_id_content and res_type = ? and res_subtype = ?)")
    		.append(" where cos_itm_id = ?");
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		int index = 1;
    		stmt = con.prepareStatement(sql.toString());
    		stmt.setString(index++, dbResource.RES_TYPE_MOD);
    		stmt.setString(index++, dbModule.MOD_TYPE_FOR);
    		stmt.setLong(index++, itm_id);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			mod_id = rs.getLong("rcn_res_id_content");
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
        return mod_id;
    }
    
    private static ArrayList getBeIntegratedLrn(Connection con, long itm_id, long usr_ent_id) throws SQLException {
    	ArrayList intgLrn = new ArrayList();
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select distinct itm_id, itm_title")
    		.append(" from IntegRelationItem")
    		.append(" inner join IntegCompleteCondition on (icd_id = iri_icd_id)")
    		.append(" inner join IntegCourseCriteria on (icc_id = icd_icc_id)")
    		.append(" inner join aeApplication on (app_itm_id = icc_itm_id and app_ent_id = ?)")
    		.append(" inner join aeItem on (itm_id = icc_itm_id)")
    		.append(" where");
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
    	if(itm.getRunInd(con)) {
    		sql.append(" iri_relative_itm_id = (select ire_parent_itm_id from aeItemRelation where ire_child_itm_id = ?)");
    	} else {
    		sql.append(" iri_relative_itm_id = ?");
    	}
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		int index = 1;
    		stmt = con.prepareStatement(sql.toString());
    		stmt.setLong(index++, usr_ent_id);
    		stmt.setLong(index++, itm_id);
    		rs = stmt.executeQuery();
    		while (rs.next()) {
    			aeItem item = new aeItem();
    			item.itm_id = rs.getLong("itm_id");
    			item.itm_title = rs.getString("itm_title");
    			intgLrn.add(item);
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
        return intgLrn;
    }
    
    public static String getBeIntegratedLrnXml(Connection con, long itm_id, long usr_ent_id) throws SQLException {
    	StringBuffer result = new StringBuffer(500);
    	result.append("<be_integrated_lrn_lst>");
    	ArrayList intgLrn = getBeIntegratedLrn(con, itm_id, usr_ent_id);
    	for(int i=0; i<intgLrn.size(); i++) {
    		aeItem item = (aeItem)intgLrn.get(i);
    		result.append("<intg_lrn id=\"").append(item.itm_id).append("\">")
    			.append(cwUtils.esc4XML(item.itm_title))
    			.append("</intg_lrn>");
    	}
    	result.append("</be_integrated_lrn_lst>");
    	return result.toString();
    }
    
    private static String convertTrainType(String dummyType) {
    	if(dummyType.lastIndexOf(aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER) > 0) {
    		dummyType = dummyType.substring(dummyType.lastIndexOf(aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER) + aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER.length(), dummyType.length());
    	}
    	return dummyType;
    }
    
    private static final String sql_upd_itm_share_ind = "update aeItem set itm_share_ind = ? where itm_id = ?";
    public void setItemShareStatus(Connection con) throws SQLException, cwSysMessage {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql_upd_itm_share_ind);
            stmt.setBoolean(1, itm_share_ind);
            stmt.setLong(2, itm_id);
            if (stmt.executeUpdate() != 1) {
                throw new cwSysMessage("GEN000");
            }
        } finally {
            if (stmt != null) stmt.close();
        }
    }
    
    private static final String sql_get_itm_share_ind = "select itm_share_ind from aeItem where itm_id = ? or itm_id in (select ire_parent_itm_id from aeItemRelation where ire_child_itm_id = ?)";
    public static boolean isItemShared(Connection con, long _itm_id) throws SQLException {
        boolean shared = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql_get_itm_share_ind);
            stmt.setLong(1, _itm_id);
            stmt.setLong(2, _itm_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                shared = rs.getBoolean(1);
            }
            return shared;
        } finally {
            if (stmt != null) stmt.close();
        }
    }
    public void updateOfflinePkg(Connection con, long itm_id, String offline_pkg, String offline_pkg_file) throws SQLException {
		String sql = "update aeItem set itm_offline_pkg=?, itm_offline_pkg_file=? where itm_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, offline_pkg);
		stmt.setString(index++, offline_pkg_file);
		stmt.setLong(index++, itm_id);
		stmt.executeUpdate();
		stmt.close();
		return;
	}
    /**
     * InstructorCos 取出授课对象
     * @author kelvin.yan
     */
    public String getItemXML(Connection con ,long itm_id) throws SQLException {
    	String xml =null;
        PreparedStatement stmt = con.prepareStatement(sql_get_itm_xml);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
        	xml = cwSQL.getClobValue(rs, "itm_xml");
        }
        else {
            //System.out.println("rs NULL");
        	xml = null;
        }
        stmt.close();
        return xml;
    }

	/**
	 * 取课程的证书id
	 * 
	 * @author kelvin.yan
	 */
	private static final String sql_get_itm_cfc_id = " Select itm_cfc_id From aeItem Where itm_id = ? ";

	public long getItemcfcID(Connection con, long itm_id) throws SQLException {
		long cfc_id = 0;
		PreparedStatement stmt = con.prepareStatement(sql_get_itm_cfc_id);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			cfc_id = rs.getLong("itm_cfc_id");
		}

		stmt.close();
		return cfc_id;
	}
	
	// 批量修改网上课程 - start
	/**
	 * 根据课程id或者目录id批量更新课程想信息 该方法有三个重点：1 根据需要修改的字段拼装sql。2 根据当前目录或者课程id查找所有课程id，3 根据当前的id去修改课程（课程信息和xml信息）
	 * 
	 * @param conn
	 * @throws SQLException
	 * @throws cwSysMessage
	 * @throws qdbException
	 * @autho jun Connection 与数据库的连接
	 * @throws
	 * @date2012-07-29
	 */

	public void upd_batch(Connection con, loginProfile prof, String itm_bonus_ind, String itm_retake_ind, String iscatalog, 
			long[] tnd_ids_change_lst,String itm_status ,String itm_access_type) throws SQLException, cwSysMessage, qdbException {
		Vector itm_ids = new Vector();
		int identification = 0;		
		CommonLog.info("===================="+itm_id_lst);
		// 目录或者课程id不能为空
		if (itm_id_lst != null && itm_id_lst.length != 0) {
			// 更新课程信息sql
			String sql = "UPDATE aeItem set ";
			/* 报名时期限只有填写或者填写两种情况，如果填写了职则更新为当前值，如果是空的证明不做修改 */
			// 报名开始时间（报名开始时间只有写或者不写两种情况，写了就不为空，不写就是空）
			if (this.itm_appn_start_datetime != null) {
				sql += "itm_appn_start_datetime=? ";
				identification++;
			}
			// 报名结束时间(报名结束时间可以使手写的一个时间或者不限，不限也是一个时间，选择不修改的时候就是空)
			if (this.itm_appn_end_datetime != null) {
				if (identification == 0) {
					sql += "itm_appn_end_datetime=? ";
				} else {
					sql += ",itm_appn_end_datetime=? ";
				}
				identification++;
			}

			// 内容期限只有结束时间，如果为空则不做任何修改（如果是空标示不做修改，如果是不空标示为需要修改）
			if (this.itm_content_eff_end_datetime != null) {
				if (identification == 0) {
					sql += "itm_content_eff_end_datetime=? ";
				} else {
					sql += ",itm_content_eff_end_datetime=? ";
				}
				identification++;
			}
			
			//内容期限结束时间不为空，且从学员报名成功后多少天为0
			if (this.itm_content_eff_end_datetime != null && this.itm_content_eff_duration == 0) {
				if (identification == 0) {
					sql += "itm_content_eff_duration=? ";
				} else {
					sql += ",itm_content_eff_duration=? ";
				}
				identification++;
			}

			// 重读
			if (!"".equals(itm_retake_ind)) {
				if (identification == 0) {
					sql += "itm_retake_ind=? ";
				} else {
					sql += ",itm_retake_ind=? ";
				}
				identification++;
			}

			// 积分如果不做修改就直接，如果可以自动积分就修改按年度系数，如果不能自动积分就不修改难度系数
			if (!"".equals(itm_bonus_ind)) {
				if (identification == 0) {
					sql += "itm_bonus_ind=? ";
				} else {
					sql += ",itm_bonus_ind=? ";
				}
				// 如果可以自动积分就添加修改难度系数，不自动积分就不要修改难度系数
				if (this.itm_bonus_ind) {
					sql += ",itm_diff_factor=? ";
				}
				identification++;
			}
			// 从学员报名成功后多少天
			if (this.itm_content_eff_duration != 0) {
				if (identification == 0) {
					sql += "itm_content_eff_duration=? ";
				} else {
					sql += ",itm_content_eff_duration=? ";
				}
				identification++;
			}
			
			//内容期限结束时间为空，且从学员报名成功后多少天不为0
			if (this.itm_content_eff_end_datetime == null && this.itm_content_eff_duration != 0) {
				if (identification == 0) {
					sql += "itm_content_eff_end_datetime=? ";
				} else {
					sql += ",itm_content_eff_end_datetime=? ";
				}
				identification++;
			}
			
			if(!StringUtils.isEmpty(itm_access_type)){
				if(!StringUtils.isEmpty(itm_status)&&itm_status.equalsIgnoreCase(ITM_STATUS_ON)){
					if (identification == 0) {
						sql += " itm_status=? , itm_access_type=? , itm_publish_timestamp = ? ";
					}else{
						sql += " ,itm_status=? , itm_access_type=? , itm_publish_timestamp = ? ";
					}
				}else{
					if (identification == 0) {
						sql += " itm_status=? , itm_access_type='' , itm_publish_timestamp = null ";
					}else{
						sql += " ,itm_status=? , itm_access_type='' , itm_publish_timestamp = null ";
					}
				}
				identification++;
			}
			// 如果没有任何的数据修改则直接返回，初始化identification为0，只要有任何的数据修改都不会为0
			if (identification == 0 && (tnd_ids_change_lst == null || tnd_ids_change_lst.length < 1)) {
				return;
			}
			// 更新课程xml信息sql
			String sql_upd_xml = "UPDATE aeItem set itm_xml=? WHERE itm_id=? ";
			// 如果传递过来的是id可能是目录id也可能是课程id，不管哪种情况都由getItemIdsByCatalogId执行，如果是目录返回目录及其子目录的所有课程，如果是课程直接返回课程id。
			for (int i = 0; i < itm_id_lst.length; i++) {
				// 如果是目录就获取该目录以及该目录所有子目录的课程id，如果是课程id则直接把该id放入集合当中
				getItemIdsByCatalogId(con, itm_id_lst[i], prof, itm_ids, iscatalog);
			}
			String conditions = "";
			// 在课程id存在的时候把Vector变成附件的更新条件（如果传递过来的是目录，目录不一定有课程，因此要判断，如果没有课程的直接返回）
			if (itm_ids != null && itm_ids.size() != 0) {
				conditions = cwUtils.vector2list(itm_ids);
			} else {
				// 如果没有课程id标示没有任何的修改，然后直接返回。
				return;
			}

			sql += " WHERE itm_id in " + conditions;
			CommonLog.info("============================="+sql);
			PreparedStatement stmt = null;
			PreparedStatement stmt1 = null;
			int index = 1;
			try {
				if(identification > 0){
					stmt = con.prepareStatement(sql);
					// 报名期限
					if (this.itm_appn_start_datetime != null) {
						stmt.setTimestamp(index++, this.itm_appn_start_datetime);
					}
					if (this.itm_appn_end_datetime != null) {
						
						//if ("9999-12-31 23:59:59.0".equals(this.itm_appn_end_datetime.toString())) {
						//	this.itm_appn_end_datetime = null;
						//}
						stmt.setTimestamp(index++, this.itm_appn_end_datetime);
					}
	
					//是否为不限
					boolean isUnlimited = false;
					
					// 内容期限只有结束时间，如果为空则不做任何修改
					if (this.itm_content_eff_end_datetime != null) {
						/*
						 * 如果是内容期限为不限9999-12-31 23:59:59.0的时候直接为null标示为不限，网上内容期限时间本应该是null， 为了跟不做任何修改区别只能设定一个固定时间，在这里做判断
						 */
						if ("9999-12-31 23:59:59.0".equals(this.itm_content_eff_end_datetime.toString())) {
							isUnlimited = true;
							this.itm_content_eff_end_datetime = null;
						}
						stmt.setTimestamp(index++, this.itm_content_eff_end_datetime);
					}
					
					if (isUnlimited || (this.itm_content_eff_end_datetime != null && this.itm_content_eff_duration == 0)) {
						stmt.setLong(index++, 0);
					}
					
					// 重读
					if (!"".equals(itm_retake_ind)) {
						if (this.itm_retake_ind) {
							stmt.setLong(index++, 1);
						} else {
							stmt.setLong(index++, 0);
						}
					}
					// 积分如果不做修改就直接，如果可以自动积分就修改按年度系数，如果不能自动积分就不修改难度系数
					if (!"".equals(itm_bonus_ind)) {
						if (this.itm_bonus_ind) {
							stmt.setLong(index++, 1);
							stmt.setFloat(index++, this.itm_diff_factor);
							
						} else {
							stmt.setLong(index++, 0);
						}
					}
					if (this.itm_content_eff_duration != 0) {
						stmt.setLong(index++, this.itm_content_eff_duration);
					}
					
					if (this.itm_content_eff_end_datetime == null && this.itm_content_eff_duration != 0) {
						stmt.setTimestamp(index++, null);
					}
					
					if(!StringUtils.isEmpty(itm_access_type)){
						if(!StringUtils.isEmpty(itm_status)&&itm_status.equalsIgnoreCase(ITM_STATUS_ON)){
							stmt.setString(index++, itm_status);
							stmt.setString(index++, itm_access_type);
							stmt.setTimestamp(index++,  new java.sql.Timestamp((new Date()).getTime()));
						}else{
							stmt.setString(index++, itm_status);
						}
					}
					
					stmt.executeUpdate();
					// 按照当前的课程id更新各个课程的xml信息
					if (itm_ids != null && itm_ids.size() != 0) {
						for (int i = 0; i < itm_ids.size(); i++) {
							long temp_itm_id = (Long) itm_ids.get(i);
						    
							index = 1;
							String item_xml = upd_item_xml(con, temp_itm_id);
						    if(item_xml!=null){
						    	stmt1 = con.prepareStatement(sql_upd_xml);
						    	stmt1.setString(index++, item_xml);
						    	stmt1.setLong(index++, temp_itm_id);
						    	stmt1.executeUpdate();		
						    	stmt1.close();
						    }
							ObjectActionLog log = new ObjectActionLog(temp_itm_id, 
									aeItem.getItemCode(con, temp_itm_id),
									aeItem.getItemTitle(con, temp_itm_id),
									ObjectActionLog.OBJECT_TYPE_COS,
									ObjectActionLog.OBJECT_ACTION_UPD,
									ObjectActionLog.OBJECT_ACTION_TYPE_BATCH,
									prof.getUsr_ent_id(),
									prof.login_date,
									prof.ip
							);
							SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
						}
					}
				}
				
				if (itm_ids != null && itm_ids.size() != 0) {
					for (int i = 0; i < itm_ids.size(); i++) {
						long temp_itm_id = (Long) itm_ids.get(i);
					    if(tnd_ids_change_lst != null && tnd_ids_change_lst.length > 0){
					    	aeItem itm = new aeItem();
					    	itm.itm_id = temp_itm_id;
					    	itm.attachTreeNodes(con, tnd_ids_change_lst, null, prof.usr_id, prof.usr_ent_id, prof.root_ent_id);
					    }
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				CommonLog.error(e.getMessage(),e);
			} finally {
				cwSQL.closePreparedStatement(stmt1);
				cwSQL.closePreparedStatement(stmt);
			}
		}

	}

	/**
	 * @desc 把报名期限 ，网上课程内容，重读，自动积分和难度系数瓶装成为xml
	 * @param conn
	 *            itm_id
	 * @autho jun Connection 与数据库的连接
	 * @throws
	 * @date2012-07-29
	 */

	public String upd_item_xml(Connection con, long itm_id) throws SQLException, cwSysMessage {
		aeItem item = new aeItem();
		item.itm_id = itm_id;
		item.get(con);
		String head=null;
		String bottom=null;
		String result = null;
		
		// 自动积分		
		if(item.itm_xml!=null&&item.itm_xml.indexOf("<field25>")!=-1&&item.itm_xml.indexOf("</field25>")!=-1){
			head = item.itm_xml.substring(0, item.itm_xml.indexOf("<field25>"));
			bottom = item.itm_xml.substring(item.itm_xml.indexOf("</field25>"));			
			if (itm_bonus_ind) {
				result = head + "<field25><field25 id=\"1\">true" + bottom;
			} else {
				result = head + "<field25><field25 id=\"2\">false" + bottom;
			}
		}
		
		// 重读
		if(result!=null&&result.indexOf("<field16>")!=-1&&result.indexOf("</field16>")!=-1){
			head = result.substring(0, result.indexOf("<field16>"));
			bottom = result.substring(result.indexOf("</field16>"));
			if (itm_retake_ind) {
				result = head + "<field16><field16 id=\"1\">true" + bottom;
			} else {
				result = head + "<field16><field16 id=\"2\">false" + bottom;
			}
		}

		// 难度系数
		if(result!=null&&result.indexOf("<field26>")!=-1&&result.indexOf("</field26>")!=-1){
			head = result.substring(0, result.indexOf("<field26>"));
			bottom = result.substring(result.indexOf("</field26>"));
			if (itm_bonus_ind) {
				result = head + "<field26>" + (int) itm_diff_factor + bottom;
			} else {
				result = head + "<field26>" + bottom;
			}			
		}
		// 报名期限
		if(result!=null&&result.indexOf("<field03><subfield_list>")!=-1&&result.indexOf("</subfield_list></field03>")!=-1){
			head = result.substring(0, result.indexOf("<field03><subfield_list>"));
			bottom = result.substring(result.indexOf("</subfield_list></field03>"));
			String temp_xml = "";
			if (itm_appn_start_datetime != null) {
				temp_xml += "<field03><subfield_list><subfield id=\"1\">" + itm_appn_start_datetime + "</subfield>";
				if (itm_appn_end_datetime != null) {
					temp_xml += "<subfield id=\"2\">" + itm_appn_end_datetime + "</subfield>";
				} 
			} else {
				temp_xml += "<field03><subfield_list><subfield id=\"1\">" + item.itm_appn_start_datetime + "</subfield>";
				if (itm_appn_end_datetime != null) {
					temp_xml += "<subfield id=\"2\">" + itm_appn_end_datetime + "</subfield>";
				} 
			}
			result = head + temp_xml + bottom;
		}
		return result;
	}

	public void updItemInfo(Connection con, aeItem item) {
		String sql = "UPDATE aeItem set itm_capacity=?,itm_fee=?,itm_retake_ind=?,itm_bonus_ind=?,itm_notify_days=?,itm_send_enroll_email_ind=?,itm_content_eff_duration=? where itm_id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			int index = 1;
			pstmt.setLong(index++, item.itm_capacity);
			pstmt.setFloat(index++, item.itm_fee);
			pstmt.setLong(index++, item.itm_retake_ind == true ? 1 : 0);
			pstmt.setLong(index++, item.itm_bonus_ind == true ? 1 : 0);
			pstmt.setInt(index++, item.itm_notify_days);
			pstmt.setLong(index++, item.itm_send_enroll_email_ind);
			pstmt.setLong(index++, 0);
			pstmt.setLong(index++, item.itm_id);
			pstmt.executeUpdate();
		} catch (SQLException e) {

			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} finally {
			cwSQL.closePreparedStatement(pstmt);
		}
	}

	/**
	 * @desc 根据目录查询课程id，如果传递进来的是课程直接返回，如果是目录的查询该目录下的所有课程，包括子目录的课程id
	 * @param conn itm_id
	 * @throws cwSysMessage
	 * @throws qdbException
	 * @autho jun Connection 与数据库的连接
	 * @throws
	 * @date2012-07-29
	 */
	private void getItemIdsByCatalogId(Connection con, long catalog_id, loginProfile prof, Vector itm_ids, String iscatalog) throws SQLException, cwSysMessage, qdbException {
		// 如果传递过来的id为0直接返回
		if (catalog_id == 0) {
			itm_ids = null;
			return;
		}
		// 如果是课程的把当前的id放进结合直接返回
		if (!"true".equals(iscatalog)) {
			itm_ids.add(catalog_id);
			return;
		}
		// 如果id不为0并且不是课程，那就有可能是目录，这样就按照目录的id去查询课程id集合
		Course course = new Course();
		String sql = getViewCoursesOfOneCatalogSql(false);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, catalog_id);
			pstmt.setString(2, ITM_TYPE_SELFSTUDY);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				long itm_id = rs.getLong("itm_id");
				if (!itm_ids.contains(itm_id)) {
					itm_ids.add(itm_id);
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} finally {
			cwSQL.cleanUp(rs, pstmt);
		}
	}

	/**
	 * 查询某一目录下(包括其子孙目录)课程的SQL
	 * 
	 * @param isCalcAmount
	 *            是否要计算某一目录下(包括其子孙目录)的所有用户的可见课程的数量
	 * @return 某一目录下(包括其子孙目录)的所有用户的可见课程的SQL语句
	 */
	public String getViewCoursesOfOneCatalogSql(boolean isCalcAmount) {

		// 若不计算该目录下的可见课程的数量，则不能有treeNode.tnd_id，
		// 这是由于前台在课程列表栏显示某个目录(包括其子目录)下的课程时，不能出现重复的课程(即使是同一门课程发布该目录下的不同子目录中时的情况)
		String subSql = " select distinct ";

		// 若要计算该目录下的可见课程的数量
		// 可能出现的情况是，在某个目录下的多个子目录中发布有相同的课程，就不能注释掉treeNode.tnd_id
		if (isCalcAmount == true) {
			subSql = " select distinct treeNode.tnd_id, ";
		}

		// 查询某一目录下(包括其子孙目录)的所有用户的可见课程的SQL
		String sql = subSql + " citm.itm_id, citm.itm_title, citm.itm_type, citm.itm_icon, " // 课程所在结点树的ID(去掉)，课程ID，课程名，课程类型，课程图标
				+ " citm.itm_desc, citm.itm_comment_avg_score, " // 课程描述，课程评分
				+ " citm.itm_create_run_ind, citm.itm_exam_ind, citm.itm_blend_ind, citm.itm_ref_ind ,citm.itm_publish_timestamp" // 是否可以创建班级、是否是考试、是否是混合、是否是参考
				+ " from aeTreeNodeRelation treeNodeR " // 用来记录目录与目录、目录与课程的关系表
				+ " inner join aeTreeNode treeNode on treeNodeR.tnr_child_tnd_id=treeNode.tnd_id " // 课程与目录的关系结点树
				+ " inner join aeItem citm on treeNode.tnd_itm_id=citm.itm_id " // 课程(或班级)表
				// // 加入班级与离线课程的关系表，以方便查找离线课程对应的班级的开课时间
				// + " left join aeItemRelation citmR on citm.itm_id=citmR.ire_parent_itm_id "
				// + " left join aeItem citm2 on citm2.itm_id=citmR.ire_child_itm_id "
				// 加入顶层目录表
				+ " left join aeCatalog cat on treeNode.tnd_cat_id=cat.cat_id " + " left join aeTreeNode treeNode2 on ( " + " 		treeNode2.tnd_parent_tnd_id is null " + " 		and cat.cat_id = treeNode2.tnd_cat_id " + " ) "
				// 加入目录表
				+ " left join aeTreeNode treeNode3 on treeNodeR.tnr_ancestor_tnd_id = treeNode3.tnd_id " + " where treeNodeR.tnr_ancestor_tnd_id=? and itm_type = ?";// 传入：某一目录ID(aeTreeNode表的tnd_id字段)
		return sql;
	}


	
    public void convertItmData(Connection con) throws SQLException,qdbException, cwException {
        PreparedStatement stmt = con.prepareStatement("select cos_structure_xml, cos_itm_id, cos_import_datetime, cos_max_normal, cos_aicc_version, cos_vendor, cos_structure_json from Course where cos_structure_json is null");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String cos_structure_xml = rs.getString("cos_structure_xml");
            int cos_itm_id = rs.getInt("cos_itm_id");
		    if(cos_structure_xml != null) {
		    	String cos_structure_json = qdbAction.static_env.transformXML(cos_structure_xml.toString().replaceAll("&quot;", " "), "cos_structure_json_js.xsl",null);
	        
				StringBuffer updSql = new StringBuffer(" ");
				updSql.append(" update Course set cos_structure_json = ? where cos_itm_id = ? ");
				stmt = con.prepareStatement(updSql.toString());
				stmt.setString(1, cos_structure_json);
				stmt.setLong(2, cos_itm_id);
				stmt.executeUpdate();
		    }

        }
        rs.close();
        stmt.close();

    }
    
    public void arseQuesXmlToHash(String xml, Vector vExtensionColName, Vector vExtensionColType, Vector vExtensionColValue) throws qdbException {
        Hashtable bodyContent = new Hashtable();
        ArrayList optionList = new ArrayList();
        ArrayList orgOptionList = new ArrayList();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document docXml = null;
        try {
            builder = factory.newDocumentBuilder();
            if (xml != null && xml.length() > 0) {
                StringReader rd = new StringReader(xml);
                InputSource in = new InputSource(rd);
                docXml = builder.parse(in);
            }
        } catch (ParserConfigurationException e) {
           // e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        } catch (SAXException e) {
        	CommonLog.error(e.getMessage(),e);
            //e.printStackTrace();
            throw new qdbException(e.getMessage());
        } catch (IOException e) {
            //e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
            throw new qdbException(e.getMessage());
        }
        Element rootElement = docXml.getDocumentElement();
        NodeList nodeList = rootElement.getChildNodes();
//        System.out.println(xml);
        ArrayList que_list = new ArrayList();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nod = nodeList.item(i);
//            System.out.println(nod.getNodeName());
//            System.out.println(nod.getTextContent());

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field04")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_lang");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field05")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_objective");
            }
            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field07")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_contents");
            }
            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field08")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_duration");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field09")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_audience");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field10")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_prerequisites");
            }
            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field11")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_exemptions");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field12")) {

                getSubNodValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_itm_ref_materials");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field13")) {

                getSubNodValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_itm_ref_url");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field14")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_remarks");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field17")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_enroll_confirm_remarks");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field18")) {
                getSubNodValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_itm_rel_materials");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field54")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_schedule");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field56")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_remarks");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field58")) {
                getFieldValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_enroll_confirm_remarks");
            }

            if (nod.getNodeName() != null && nod.getNodeName().equalsIgnoreCase("field60")) {
                getSubNodValue(nod, vExtensionColName, vExtensionColType, vExtensionColValue, DbTable.COL_TYPE_STRING, "ies_itm_rel_materials");
            }
        }
    }

    private void getFieldValue(Node nod, Vector vExtensionColName, Vector vExtensionColType, Vector vExtensionColValue, String type, String Col) {
        if (nod.getTextContent() != null && nod.getTextContent().trim().length() > 0) {
            vExtensionColName.add(Col);
            vExtensionColType.add(type);
            vExtensionColValue.add(nod.getTextContent());
        }

    }
    
    private void getSubNodValue(Node nod, Vector vExtensionColName, Vector vExtensionColType, Vector vExtensionColValue, String type, String col_pre) {
    	
    	NodeList sub_nod_lst = nod.getChildNodes();
    	for (int j = 0; j < sub_nod_lst.getLength(); j++) {
    		Node nod_sub = sub_nod_lst.item(j);
    		if (nod_sub.getNodeName() != null && nod_sub.getNodeName().equalsIgnoreCase("subfield_list")) {
    			
    			NodeList sub_nod_lst_1 = nod_sub.getChildNodes();
    			for (int k = 0; k < sub_nod_lst_1.getLength(); k++) {
    				
    				Node nod_sub_1 = sub_nod_lst_1.item(k);
    				if (nod_sub_1.getNodeName() != null && nod_sub_1.getNodeName().equalsIgnoreCase("subfield")) {
    					String col = col_pre + "_" + (k + 1);
    					getFieldValue(nod_sub_1, vExtensionColName, vExtensionColType, vExtensionColValue, type, col);
    				}
    			}
    		}
    	}
    }
    
	// 强制删除残留的学习记录
	// 保证没有报名记录时，可以删除课程
	private void delEnrolHistory(Connection con) {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sql = null;
		try {
			// 查询当前课程或者班级下所有残留的
			Vector<Long> tkhIdVec = new Vector<Long>();

			sql = " select tkh_id from TrackingHistory, Course where tkh_cos_res_id = cos_res_id and cos_itm_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, this.itm_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				tkhIdVec.add(rs.getLong("tkh_id"));
			}
			cwSQL.cleanUp(rs, stmt);

			// 把tkh_id保存到临时表里
			String tkhIdTableName = null;
			String tkhIdColName = null;
			if (tkhIdVec.size() > 0) {
				tkhIdColName = "tmp_tkh_id";
				tkhIdTableName = cwSQL.createSimpleTemptable(con, tkhIdColName, cwSQL.COL_TYPE_LONG, 0);
			}
			if (tkhIdTableName != null) {
				cwSQL.insertSimpleTempTable(con, tkhIdTableName, tkhIdVec, cwSQL.COL_TYPE_LONG);
			}

			if (tkhIdTableName == null) {
				return;
			}

			String cond = " in ( select " + tkhIdColName + " from " + tkhIdTableName + ")";

			sql = " delete from ProgressAttemptSaveAnswer where psa_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from ProgressAttemptSave where pas_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from ProgressAttempt where atm_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from ProgressAttachment where pat_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from Progress where pgr_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from ModuleEvaluationHistory where mvh_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from ModuleEvaluation where mov_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from CourseEvaluation where cov_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from aeItemComments where ict_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from TrackingHistory where tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);
			
			sql = " delete from MeasurementEvaluation where mtv_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			sql = " delete from Accomplishment where apm_tkh_id " + cond;
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);
		} catch (Exception e) {
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	private static String searchItemResultAsXML(Connection con, Hashtable param, Timestamp curTime, boolean checkStatus, qdbEnv inEnv, long owner_ent_id,
            loginProfile prof, boolean tcEnabled) throws SQLException, qdbException, cwException, cwSysMessage {
        aeItem itm = null;
        String tvw_id = (String) param.get(ITM_LIST_VIEW_ID);
        String[] itm_types = (String[]) param.get(ITM_TYPES);
        boolean all_ind = ((Boolean) param.get(ALL_IND)).booleanValue();
        long[] tnd_ids = (long[])param.get(TND_ID_LIST);
        long[] tcr_id_lst = (long[]) param.get(TCR_ID_LIST);
        
        String itm_status = (String) param.get(ITM_STATUS);
        String itm_code = (String) param.get(ITM_CODE);
        String itm_title = (String) param.get(ITM_TITLE);
        String itm_title_code = (String) param.get(ITM_TITLE_CODE);
        boolean exact = ((Boolean) param.get(EXACT)).booleanValue();
        
        String orderBy = (String) param.get(ORDERBY);
        if (orderBy == null || orderBy.length() == 0) {
        	orderBy = null;
        }else if (orderBy.equalsIgnoreCase("r_itm_code")
        	|| orderBy.equalsIgnoreCase("r_itm_title")
        	|| orderBy.equalsIgnoreCase("r_itm_type")
        	|| orderBy.equalsIgnoreCase("r_itm_status")
        	|| orderBy.equalsIgnoreCase("r_itm_appn_end_datetime")
        	|| orderBy.equalsIgnoreCase("r_itm_eff_start_datetime")
        	|| orderBy.equalsIgnoreCase("r_itm_eff_end_datetime")
        	|| orderBy.equalsIgnoreCase("r_itm_upd_timestamp")
        	|| orderBy.equalsIgnoreCase("r_itm_appn_end_datetime")
        	
        ){
        		 
        }
        else{
        	orderBy = null;
        }
        
        String sortOrder = (String) param.get(SORTORDER);
        
        if (sortOrder == null || sortOrder.length() == 0) {
        	sortOrder = null;
        }else if (sortOrder.equalsIgnoreCase("asc")
        	|| sortOrder.equalsIgnoreCase("desc")
        ){
        }
        else{
        	sortOrder = null;
        }
        
        long page = ((Long) param.get(PAGE)).longValue();
        long page_size = ((Long) param.get(PAGE_SIZE)).longValue();
        Timestamp itm_eff_from = (Timestamp) param.get(ITM_EFF_FROM);
        Timestamp itm_eff_to = (Timestamp) param.get(ITM_EFF_TO);
        String itm_eff_from_operator = (String) param.get(ITM_EFF_FROM_OPERATOR);
        String itm_eff_to_operator = (String) param.get(ITM_EFF_TO_OPERATOR);
        boolean show_attendance_ind = ((Boolean) param.get(ITM_SHOW_ATTENDANCE)).booleanValue();
        String training_type = (String) param.get(TRAINING_TYPE);
        String dummy_type = (String) param.get(DUMMY_TYPE);

        if (dummy_type == null || dummy_type.length() == 0) {
            dummy_type = training_type;
        }

        if (itm_eff_from_operator == null || itm_eff_from_operator.length() == 0) {
            itm_eff_from_operator = ">=";
        }
        if (itm_eff_to_operator == null || itm_eff_to_operator.length() == 0) {
            itm_eff_to_operator = "<=";
        }
        if (page == 0)
            page = 1;
        if (page_size == 0)
            page_size = ITM_SEARCH_PAGE_SIZE;
        if (curTime == null) {
            curTime = cwSQL.getTime(con);
        }

        StringBuffer orderBySql = new StringBuffer();
        if (orderBy != null && orderBy.length() > 0) {
            Vector orderByVc = cwUtils.splitToVecString(orderBy, "~");
            orderBySql.append(" order by ");
            for (int i = 0; i < orderByVc.size(); i++) {
                orderBySql.append(" ").append(orderByVc.get(i)).append(" ").append(sortOrder).append(",");
            }
            orderBySql = orderBySql.deleteCharAt(orderBySql.length() - 1);
        }

        if (orderBy == null || orderBy.length() == 0) {
            orderBy = "r_itm_upd_timestamp";
            param.put(ORDERBY, orderBy);
        }
        if (sortOrder == null || sortOrder.length() == 0) {
            sortOrder = "desc";
            param.put(SORTORDER, sortOrder);
        }
        StringBuffer SQLBuf = new StringBuffer(300);
        String null_sql_string = cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING);
        String null_sql_long = cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG);

        SQLBuf.append(" Select ");
        SQLBuf.append(" i.itm_ext1 r_itm_ext1, i.itm_life_status r_itm_life_status, i.itm_apply_method r_itm_apply_method");
        SQLBuf.append(" ,i.itm_version_code r_itm_version_code ");
        SQLBuf.append(" ,i.itm_person_in_charge r_itm_person_in_charge ");
        SQLBuf.append(" ,i.itm_cancellation_reason r_itm_cancellation_reason ");
        SQLBuf.append(" ,i.itm_min_capacity r_itm_min_capacity ");
        SQLBuf.append(" ,i.itm_unit r_itm_unit ");
        SQLBuf
                .append(" ,i.itm_create_run_ind r_itm_create_run_ind, i.itm_create_session_ind r_itm_create_session_ind, i.itm_qdb_ind r_itm_qdb_ind, i.itm_run_ind r_itm_run_ind, i.itm_session_ind r_itm_session_ind, i.itm_has_attendance_ind r_itm_has_attendance_ind, i.itm_ji_ind r_itm_ji_ind, i.itm_completion_criteria_ind r_itm_completion_criteria_ind, i.itm_apply_ind r_itm_apply_ind");
        SQLBuf.append(" ,i.itm_deprecated_ind r_itm_deprecated_ind, i.itm_imd_id r_itm_imd_id, i.itm_auto_enrol_qdb_ind r_itm_auto_enrol_qdb_ind");
        SQLBuf.append(" ,i.itm_id r_itm_id, i.itm_title r_itm_title, i.itm_type r_itm_type");
        SQLBuf.append(" ,i.itm_create_usr_id r_itm_create_usr_id, i.itm_create_timestamp r_itm_create_timestamp ");
        SQLBuf.append(" ,i.itm_status r_itm_status, i.itm_code r_itm_code ");
        SQLBuf.append(" ,i.itm_owner_ent_id r_itm_owner_ent_id");
        SQLBuf.append(" ,i.itm_approval_status as p_itm_approval_status");
        SQLBuf.append(" ,i.itm_approval_action as p_itm_approval_action, i.itm_approve_usr_id as p_itm_approve_usr_id, i.itm_approve_timestamp as p_itm_approve_timestamp");
        SQLBuf
                .append(" ,i.itm_submit_action as p_itm_submit_action, i.itm_submit_usr_id as p_itm_submit_usr_id, i.itm_submit_timestamp as p_itm_submit_timestamp ,i.itm_exam_ind, i.itm_ref_ind, i.itm_blend_ind");
        SQLBuf.append(" ,i.itm_eff_start_datetime r_itm_eff_start_datetime, i.itm_content_eff_end_datetime r_itm_eff_end_datetime ");
        SQLBuf.append(" ,i.itm_appn_start_datetime r_itm_appn_start_datetime, i.itm_appn_end_datetime r_itm_appn_end_datetime ");
        SQLBuf.append(" ,i.itm_upd_usr_id r_itm_upd_usr_id, i.itm_upd_timestamp r_itm_upd_timestamp ");
        SQLBuf.append(" ,i.itm_capacity r_itm_capacity ");
  
        SQLBuf.append(" ,tcr_title as p_itm_tcr_title");
        SQLBuf.append(" ,enrolled r_enrolled ");
        SQLBuf.append(" From aeItem i ");
        SQLBuf.append(" Inner join tcTrainingCenter on (i.itm_tcr_id = tcr_id )");
        SQLBuf.append(" Left join (Select app_itm_id, Count(*) as enrolled From aeApplication Where app_status =? group by app_itm_id) app on (i.itm_id = app.app_itm_id)");
        SQLBuf.append(" Where i.itm_life_status is null ");
        SQLBuf.append(" AND i.itm_run_ind = ? ");
        SQLBuf.append(" AND tcr_status = ? ");

        if (checkStatus) {
            SQLBuf.append(" AND i.itm_status = ? ");
        }
        
        if(itm_status != null && itm_status.length() > 0) {
            SQLBuf.append( " AND i.itm_status = ? ");
        }

        if (itm_title != null && itm_title.length() > 0)
            SQLBuf.append(" AND lower(i.itm_title) LIKE ? ");

        if (itm_code != null && itm_code.length() > 0)
            SQLBuf.append(" AND lower(i.itm_code) LIKE ? ");

        if (itm_title_code != null && itm_title_code.length() > 0) {
            SQLBuf.append(" AND (lower(i.itm_title) LIKE ? OR lower(i.itm_code) LIKE ?)");
        }
        
        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE)) {
            SQLBuf.append(" AND Exists (select c.itm_id from aeItem c")
            	.append(" inner join aeItemRelation on ire_parent_itm_id = i.itm_id and c.itm_id = ire_child_itm_id")
            	.append(" where c.itm_eff_start_datetime ").append(itm_eff_from_operator).append(" ? )");
        }
        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE)) {
        	 SQLBuf.append(" AND Exists (select c.itm_id from aeItem c")
	         	.append(" inner join aeItemRelation on ire_parent_itm_id = i.itm_id and c.itm_id = ire_child_itm_id")
	         	.append(" where c.itm_eff_start_datetime ").append(itm_eff_to_operator).append(" ? )");
        }

        if (!"ALL".equalsIgnoreCase(dummy_type)) {
            SQLBuf.append(aeItemDummyType.genSqlByItemDummyType(dummy_type, "i", true));
        }
       
        if(!all_ind){
            SQLBuf.append(" and i.itm_id in(");
            SQLBuf.append(" select tnd_itm_id from aeTreenode,aeTreenoderelation where tnr_child_tnd_id = tnd_id and tnd_type = 'ITEM'");
            SQLBuf.append(" and tnr_ancestor_tnd_id in").append(aeUtils.prepareSQLList(tnd_ids));
            SQLBuf.append(" )");
        }
        
        if(itm_types != null && itm_types.length > 0){
            SQLBuf.append(" and i.itm_type in");
            SQLBuf.append("('0");
                for(int i=0;i<itm_types.length;i++) 
                    SQLBuf.append("','").append(itm_types[i]);
            SQLBuf.append("')");  
        }
        
        if(tcr_id_lst != null && tcr_id_lst.length > 0) {
        	if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                if(tcr_id_lst.length==1 && tcr_id_lst[0]==-1){
                    ViewTrainingCenter viewTCR = new ViewTrainingCenter();
                    List lTcr = viewTCR.getTrainingCenterByOfficer(con,  prof.usr_ent_id,prof.current_role, false);
                    tcr_id_lst = new long[lTcr.size()];
                    if (lTcr != null && lTcr.size() > 0) {
                        for (int j = 0; j < lTcr.size(); j++) {
                            tcr_id_lst[j] = ((DbTrainingCenter) lTcr.get(j)).getTcr_id();
                        }
                    }
                }
                SQLBuf.append(" and (i.itm_tcr_id in")
                	.append(aeUtils.prepareSQLList(tcr_id_lst));
                SQLBuf.append(" or itm_tcr_id in (")
    	            .append("select tcn_child_tcr_id from tcRelation where tcn_ancestor in")
    	            .append(aeUtils.prepareSQLList(tcr_id_lst))
    	            .append(" )")
    	            .append(" )");
        	}
        }

        if (orderBySql != null && orderBySql.length() > 0) {
            SQLBuf.append(orderBySql);
        } else {
            SQLBuf.append(" order by ").append(orderBy).append(" ").append(sortOrder);
        }
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        // System.out.println(" ================= SQL ================ ");
        // System.out.println(SQL);

        int index = 1;
        stmt.setString(index++, aeApplication.ADMITTED);
        stmt.setInt(index++, 0);
        stmt.setString(index++, DbTrainingCenter.STATUS_OK);

        if (checkStatus)
            stmt.setString(index++, aeItem.ITM_STATUS_ON);

        if(itm_status != null && itm_status.length() > 0) {
            stmt.setString(index++, itm_status);
        }

        if (itm_title != null && itm_title.length() > 0) {
            if (exact)
                stmt.setString(index++, itm_title.toLowerCase());
            else
                stmt.setString(index++, "%" + itm_title.toLowerCase() + "%");
        }

        if (itm_code != null && itm_code.length() > 0)
            stmt.setString(index++, "%" + itm_code.toLowerCase() + "%");

        if (itm_title_code != null && itm_title_code.length() > 0) {
            stmt.setString(index++, "%" + itm_title_code.toLowerCase() + "%");
            stmt.setString(index++, "%" + itm_title_code.toLowerCase() + "%");
        }

        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_from);

        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_to);

        // System.out.println("aeItem.searchItemResultUnionAsXML sql:\n" + SQL);
        ResultSet rs = stmt.executeQuery();

        StringBuffer xmlBuf = new StringBuffer(2500);
        StringBuffer eleBuf = new StringBuffer(2500);
        xmlBuf.append(searchItemParamAsXML(param));
        long start = page_size * (page - 1);
        long count = 0;
        /**
         * itm.itm_capacity; enrolled
         */

        while (rs.next()) {
            itm = new aeItem();
            itm.itm_capacity = rs.getLong("r_itm_capacity");
            itm.enrolled = rs.getLong("r_enrolled");

                itm.itm_create_run_ind = rs.getBoolean("r_itm_create_run_ind");
 
                if (count >= start && count < start + page_size) {
                    // itm.itm_xml = cwSQL.getClobValue(con, rs, "r_itm_xml");
                    itm.itm_ext1 = rs.getString("r_itm_ext1");
                    itm.itm_life_status = rs.getString("r_itm_life_status");
                    itm.itm_apply_method = rs.getString("r_itm_apply_method");
                    itm.itm_version_code = rs.getString("r_itm_version_code");
                    itm.itm_person_in_charge = rs.getString("r_itm_person_in_charge");
                    itm.itm_cancellation_reason = rs.getString("r_itm_cancellation_reason");
                    itm.itm_capacity = rs.getLong("r_itm_capacity");
                    itm.itm_min_capacity = rs.getLong("r_itm_min_capacity");
                    itm.itm_unit = rs.getFloat("r_itm_unit");

                    itm.itm_create_session_ind = rs.getBoolean("r_itm_create_session_ind");
                    itm.itm_qdb_ind = rs.getBoolean("r_itm_qdb_ind");
                    itm.itm_run_ind = rs.getBoolean("r_itm_run_ind");
                    itm.itm_session_ind = rs.getBoolean("r_itm_session_ind");
                    itm.itm_has_attendance_ind = rs.getBoolean("r_itm_has_attendance_ind");
                    itm.itm_ji_ind = rs.getBoolean("r_itm_ji_ind");
                    itm.itm_completion_criteria_ind = rs.getBoolean("r_itm_completion_criteria_ind");
                    itm.itm_apply_ind = rs.getBoolean("r_itm_apply_ind");
                    itm.itm_deprecated_ind = rs.getBoolean("r_itm_deprecated_ind");
                    itm.itm_imd_id = rs.getLong("r_itm_imd_id");
                    itm.itm_auto_enrol_qdb_ind = rs.getBoolean("r_itm_auto_enrol_qdb_ind");
                    itm.itm_title = rs.getString("r_itm_title");
                    itm.itm_id = rs.getLong("r_itm_id");
                    itm.itm_code = rs.getString("r_itm_code");
                    itm.itm_type = rs.getString("r_itm_type");
                    itm.itm_status = rs.getString("r_itm_status");
                    itm.itm_eff_start_datetime = rs.getTimestamp("r_itm_eff_start_datetime");
                    itm.itm_eff_end_datetime = rs.getTimestamp("r_itm_eff_end_datetime");
                    itm.itm_appn_start_datetime = rs.getTimestamp("r_itm_appn_start_datetime");
                    itm.itm_appn_end_datetime = rs.getTimestamp("r_itm_appn_end_datetime");
                    itm.itm_create_usr_id = rs.getString("r_itm_create_usr_id");
                    itm.itm_create_timestamp = rs.getTimestamp("r_itm_create_timestamp");
                    itm.itm_upd_usr_id = rs.getString("r_itm_upd_usr_id");
                    itm.itm_upd_timestamp = rs.getTimestamp("r_itm_upd_timestamp");
                    itm.itm_owner_ent_id = rs.getLong("r_itm_owner_ent_id");

                    itm.itm_approval_status = rs.getString("p_itm_approval_status");
                    itm.itm_approval_action = rs.getString("p_itm_approval_action");
                    itm.itm_approve_usr_id = rs.getString("p_itm_approve_usr_id");
                    itm.itm_approve_timestamp = rs.getTimestamp("p_itm_approve_timestamp");
                    itm.itm_submit_action = rs.getString("p_itm_submit_action");
                    itm.itm_submit_usr_id = rs.getString("p_itm_submit_usr_id");
                    itm.itm_submit_timestamp = rs.getTimestamp("p_itm_submit_timestamp");

                    itm.itm_tcr_title = rs.getString("p_itm_tcr_title");
                    itm.enrolled = rs.getLong("r_enrolled");
                    itm.itm_dummy_type = aeItemDummyType.getDummyItemType(itm.itm_type, rs.getBoolean("itm_blend_ind"), rs.getBoolean("itm_exam_ind"), rs.getBoolean("itm_ref_ind"));
                  
                    eleBuf.append(itm.searchViewAsXML(con, show_attendance_ind, prof, tcEnabled, curTime)).append(dbUtils.NEWL);

  
                }
                count++;
        }
        stmt.close();

        xmlBuf.append("<items timestamp=\"").append(curTime).append("\"");
        xmlBuf.append(" page_size=\"").append(page_size).append("\"");
        xmlBuf.append(" cur_page=\"").append(page).append("\"");
        xmlBuf.append(" search_code=\"").append(cwUtils.esc4XML(itm_title_code)).append("\"");
        xmlBuf.append(" total_search=\"").append(count).append("\">").append(dbUtils.NEWL);
        if (itm_types != null && itm_types.length == 1 && tvw_id != null && tvw_id.length() > 0) {
            long tpl_id;
            if (itm != null) {
                // assume the LIST_VIEW valued template are the same if have
                // same item type
                // so use the last template view to descript the column
                aeTemplate tpl = itm.getItemTemplate(con);
                tpl_id = tpl.tpl_id;
            } else {
                ViewItemTemplate viItmTpl = new ViewItemTemplate();
                viItmTpl.ownerEntId = owner_ent_id;
                viItmTpl.itemType = itm_types[0];
                viItmTpl.templateType = aeTemplate.ITEM;
                viItmTpl.runInd = false;
                viItmTpl.sessionInd = false;
                tpl_id = viItmTpl.getFirstTplId(con);
            }
            DbTemplateView dbTplVi = new DbTemplateView();
            dbTplVi.tvw_tpl_id = tpl_id;
            dbTplVi.tvw_id = tvw_id;
            dbTplVi.get(con);
            xmlBuf.append(dbTplVi.tvw_xml);
        }
        xmlBuf.append(eleBuf.toString());
        xmlBuf.append("</items>").append(dbUtils.NEWL);

        String xml = new String(xmlBuf);
        return xml;
    }

	/**
	 * 获取课程评分统计信息和评分列表数据组成的XML
	 * @param con
	 * @param urlp
	 * @return
	 * @throws SQLException
	 * @throws cwSysMessage
	 * @throws cwException
	 * @throws qdbException
	 */
	public static String getItemCommentLstPageXML(
			Connection con, aeReqParam urlp) throws SQLException, cwSysMessage, cwException, qdbException {
		StringBuffer strBuf = new StringBuffer(2000);
		aeItem itm = urlp.itm;
		Course cos = new Course();
		itm.get(con);
		strBuf.append(itm.shortInfoAsXML(con));
		
		int page = urlp.page;
		int pageSize = urlp.pageSize;
		
		if(page == 0) {
			page = 1;
			urlp.page = page;
		}
        if(pageSize == 0) {
        	pageSize = 10;
        	urlp.pageSize = 10;
        }
		
		
		Vector comLst = Course.getCosCommentLst(con, urlp);
		
		CosCommentBean cosComBean = cos.getCosCommentInfoByItmId(con, itm.itm_id);
		
		strBuf.append("<comments ")
			.append("total_score=\"").append(cosComBean.getTotal_score()).append("\" ")
			.append("total_count=\"").append(cosComBean.getTotal_count()).append("\" ")
			.append("avg_score=\"").append(cosComBean.getAvg_score()).append("\" ")
			.append("curr_page=\"").append(urlp.page).append("\" ")
			.append("page_size=\"").append(urlp.pageSize).append("\" ")
			.append("total=\"").append(urlp.total).append("\" ")
			.append(">");
		
		for (int i = 0; i < comLst.size(); i++) {
			CosCommentBean com = (CosCommentBean) comLst.get(i);
			strBuf.append("<comment id=\"").append(com.getIct_id()).append("\" ")
				.append("ent_id=\"").append(com.getIct_ent_id()).append("\" ")
				.append("ict_score=\"").append(com.getIct_score()).append("\" ")
				.append("ict_comment=\"").append(cwUtils.esc4XML(com.getIct_comment())).append("\" ")
				.append("ict_create_timestamp=\"").append(com.getIct_create_timestamp()).append("\" ")
				.append("ict_ent_name=\"").append(cwUtils.esc4XML(com.getIct_ent_name())).append("\" ")
				.append("type=\"").append(cwUtils.esc4XML(com.getType())).append("\" ")
				.append("/>");
		}
		
		strBuf.append("</comments>");
        
		return strBuf.toString();
	}
	public static boolean canNorminate(Connection con, loginProfile prof, long itm_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			boolean result = false;

			Timestamp cur_time = cwSQL.getTime(con);

			String sql = " select itm_id, itm_code, itm_title, itm_run_ind, itm_create_run_ind, ";
			sql += " itm_appn_start_datetime, itm_appn_end_datetime, ";
			sql += " itm_capacity, itm_not_allow_waitlist_ind ";
			sql += " from aeItem where itm_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);

			rs = stmt.executeQuery();
			if (rs.next()) {
				boolean itm_run_ind = rs.getBoolean("itm_run_ind");
				boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
				boolean itm_not_allow_waitlist_ind = rs.getBoolean("itm_not_allow_waitlist_ind");
				Timestamp itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
				Timestamp itm_appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
				int itm_capacity = rs.getInt("itm_capacity");

				if (itm_run_ind || (!itm_create_run_ind && !itm_run_ind)) {
					if (prof != null && prof.hasStaff) {
						if ((itm_appn_start_datetime != null && itm_appn_start_datetime.before(cur_time)) && (itm_appn_end_datetime != null && itm_appn_end_datetime.after(cur_time))) {
							result = true;
						}
					}
				}

				if (result && itm_not_allow_waitlist_ind) {
					String[] process_status_lst = aeReqParam.split(LangLabel.getValue(prof.cur_lan, "process_status"), "~");
					int enrol_cnt = aeApplication.countProcessStatus(con, itm_id, process_status_lst);

					if ((itm_capacity != 0 && itm_capacity <= enrol_cnt)) {
						result = false;
					}
				}
			}
			return result;
		} catch (qdbException e) {
			throw new SQLException(e.getMessage());
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	
	public static String getTitleByCode(Connection con, String itm_code) throws SQLException {
		String sql = "select itm_title from aeItem where itm_code = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, itm_code);
		ResultSet rs = stmt.executeQuery();
		String result = null;
		if(rs.next()) {
			result = rs.getString("itm_title");
		}
		rs.close();
		stmt.close();
		return result;
	}
	
	/**获取网上课程或班级的开始结束日期
	 * @param con
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
	public static Timestamp[] getItemStartEndDate(Connection con, long app_id) throws SQLException {
		Timestamp[] start_end_date = new Timestamp[2];
		String sql = "select itm_eff_start_datetime, itm_content_eff_end_datetime, att_create_timestamp, itm_content_eff_duration, itm_run_ind " +
				"     from aeItem, aeApplication, aeAttendance where itm_id = app_itm_id and app_id = att_app_id and app_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, app_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			if(rs.getBoolean("itm_run_ind")){
				start_end_date[0] = rs.getTimestamp("itm_eff_start_datetime");
			}else{
				start_end_date[0] = rs.getTimestamp("att_create_timestamp");
			}
			if(rs.getTimestamp("itm_content_eff_end_datetime") != null) {
				start_end_date[1] = rs.getTimestamp("itm_content_eff_end_datetime");
			}else{
				start_end_date[1] = cwUtils.dateAdd(rs.getTimestamp("att_create_timestamp"), rs.getInt("itm_content_eff_duration"));
			}
		}
		cwSQL.cleanUp(rs, stmt);
		return start_end_date;
	}
	
	/**获取班级JI,Reminder邮件发送时间
	 * @param con
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
	public static Timestamp[] getItmJiReminderSendTimestamp(Connection con, long itm_id) throws SQLException {
		Timestamp[] ji_reminder_send_datetime = new Timestamp[2];
		String sql = "select itm_ji_send_datetime, itm_reminder_send_datetime from aeItem where itm_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			ji_reminder_send_datetime[0] = rs.getTimestamp("itm_ji_send_datetime");
			ji_reminder_send_datetime[1] = rs.getTimestamp("itm_reminder_send_datetime");
		}
		cwSQL.cleanUp(rs, stmt);
		return ji_reminder_send_datetime;
	}
	
    public static void updateItemJiSendTimestamp(Connection con, long itm_id, Timestamp timestamp) throws SQLException {
    	String sql = "update aeItem set itm_ji_send_datetime = ? where itm_id=?";
    	PreparedStatement pstmt = con.prepareStatement(sql);
    	pstmt.setTimestamp(1, timestamp);
    	pstmt.setLong(2, itm_id);
    	pstmt.executeUpdate();
    	pstmt.close();
    }
    
    public static void updateItemReminderSendTimestamp(Connection con, long itm_id, Timestamp timestamp) throws SQLException {
    	String sql = "update aeItem set itm_reminder_send_datetime = ? where itm_id=?";
    	PreparedStatement pstmt = con.prepareStatement(sql);
    	pstmt.setTimestamp(1, timestamp);
    	pstmt.setLong(2, itm_id);
    	pstmt.executeUpdate();
    	pstmt.close();
    }
    public static String genItemActionNavXML(Connection con, long itm_id, loginProfile prof)throws SQLException, cwSysMessage {
    	return genItemActionNavXML( con,  itm_id,  prof, null, null) ;
    }

	public static String genItemActionNavXML(Connection con, long itm_id, loginProfile prof, String itm_type, aeReqParam urlp) throws SQLException, cwSysMessage {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<itm_action_nav ");
		boolean exam_ind = false;
		if(itm_id > 0){
			String sql = "select itm_id, itm_title, itm_type,itm_status, itm_run_ind, itm_ref_ind,"
					+ " itm_create_run_ind, itm_exam_ind, itm_content_def, itm_rsv_id,"
					+ " cos_res_id, cos_structure_xml,itm_content_eff_end_datetime,itm_eff_end_datetime"
					+ " from aeItem, Course where itm_id = cos_itm_id and itm_id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				long itm_run_ind = rs.getLong("itm_run_ind");
				long parent_itm_id = 0;
				if(itm_run_ind == 1){
					aeItemRelation ire = new aeItemRelation();
			        ire.ire_child_itm_id = rs.getLong("itm_id");
			        parent_itm_id = ire.getParentItemId(con); 
				}
				exam_ind = rs.getBoolean("itm_exam_ind");
				String itm_status = rs.getString("itm_status");
				Timestamp cur_time = cwSQL.getTime(con);
				Timestamp itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
				Timestamp itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
				boolean isStatusOff = false;
				if(itm_status == null || "".equals(itm_status) || "OFF".equals(itm_status)){
					if(itm_eff_end_datetime == null || cur_time.before(itm_eff_end_datetime)){
						if(itm_content_eff_end_datetime == null || cur_time.before(itm_content_eff_end_datetime)){
							isStatusOff = true;
						}
					}
				}
				boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
				if(itm_create_run_ind){
					aeItem itm = new aeItem();
					List chril_itm_lst = itm.getChItemIDList(con, itm_id);
					if(chril_itm_lst== null || chril_itm_lst.size() < 1){
						strBuf.append("has_run=\"").append(false).append("\" ");
					}else{
						strBuf.append("has_run=\"").append(true).append("\" ");
					}
				}
				
				if(itm_create_run_ind || itm_run_ind == 1){
					boolean hasLesson = false;
					aeItemLesson lesson = new aeItemLesson();
					long rowCount = lesson.getLessonCountByItmId(con ,itm_id);
					if(rowCount>0){
						hasLesson = true;
					}
					strBuf.append("has_lesson=\"").append(hasLesson).append("\" ");
				}
				
				strBuf.append("itm_id=\"").append(rs.getLong("itm_id")).append("\" ")
				
				.append("cos_res_id=\"").append(rs.getLong("cos_res_id")).append("\" ")
				.append("itm_type=\"").append(rs.getString("itm_type")).append("\" ")
				.append("itm_run_ind=\"").append(rs.getBoolean("itm_run_ind")).append("\" ")
				.append("itm_ref_ind=\"").append(rs.getBoolean("itm_ref_ind")).append("\" ")
				.append("itm_create_run_ind=\"").append(rs.getBoolean("itm_create_run_ind")).append("\" ")
				.append("itm_exam_ind=\"").append(rs.getBoolean("itm_exam_ind")).append("\" ")
				.append("itm_content_def=\"").append(rs.getString("itm_content_def")).append("\" ")
				.append("itm_rsv_id=\"").append(rs.getLong("itm_rsv_id")).append("\" ")
				.append("itm_status=\"").append(rs.getString("itm_status")).append("\" ")
				.append("itm_title=\"").append(cwUtils.esc4XML(rs.getString("itm_title"))).append("\" ")
				.append("isStatusOff=\"").append(isStatusOff).append("\" ");
				if(parent_itm_id >0){
					strBuf.append("parent_title=\"").append(cwUtils.esc4XML(aeItem.getItemTitle(con, itm_id))).append("\" ")
					.append("parent_itm_id=\"").append(parent_itm_id).append("\" ");
				}
				
		        boolean has_mod;
                has_mod = rs.getString("cos_structure_xml") != null;
		        strBuf.append("has_mod=\"").append(has_mod).append("\" ");
			}
		
			cwSQL.cleanUp(rs, stmt);
		}else{
			if(urlp != null && urlp.itm  != null){
				strBuf.append("itm_run_ind=\"").append(urlp.itm.itm_run_ind).append("\" ");
				strBuf.append("itm_create_run_ind=\"").append(urlp.itm.itm_create_run_ind).append("\" ");
				strBuf.append("itm_exam_ind=\"").append(urlp.itm.itm_exam_ind).append("\" ");
				exam_ind = urlp.itm.itm_exam_ind;
			}
				
			strBuf.append(" itm_type=\"").append(itm_type).append("\" ");
		}
		strBuf.append(">");
		AcItem acItem = new AcItem(con);
		boolean hasItmCosMain = false;
		boolean hasContentMain = false;
		boolean hasResultMain = false;
		boolean hasEnrollMain = false;
		boolean hasTeachingCourse = false;
		//是否开放CPT/D功能
		boolean hasCPD = AccessControlWZB.hasCPDFunction();
		
		//公开课程
		if(ITM_TYPE_AUDIOVIDEO.equalsIgnoreCase(itm_type)){
			hasItmCosMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasContentMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasResultMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasEnrollMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN );
			hasTeachingCourse = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_TEACHING_COURSE_LIST );
			//公开课程
		}else if(exam_ind){
			//考试
			hasItmCosMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_EXAM_MAIN_VIEW );
			hasContentMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_EXAM_MAIN_CONTENT );
			hasResultMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_EXAM_MAIN_PERFORMANCE );
			hasEnrollMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION );
			hasTeachingCourse = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_TEACHING_COURSE_LIST );
			
		}else{
			//课程
			hasItmCosMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW );
			hasContentMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT );
			hasResultMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_PERFORMANCE );
			hasEnrollMain = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION );
			hasTeachingCourse = AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_TEACHING_COURSE_LIST );
			
		}
		
		
	     strBuf.append("<hasItmCosMain>").append(hasItmCosMain).append("</hasItmCosMain>");
	     strBuf.append("<hasContentMain>").append(hasContentMain).append("</hasContentMain>");
	     strBuf.append("<hasResultMain>").append(hasResultMain).append("</hasResultMain>");
	     strBuf.append("<hasEnrollMain>").append(hasEnrollMain).append("</hasEnrollMain>");
	     strBuf.append("<hasTeachingCourse>").append(hasTeachingCourse).append("</hasTeachingCourse>");
	
	    strBuf.append("</itm_action_nav>");
	    //将用户当前角色摄入XML
	    strBuf.append("<current_role>").append(cwUtils.esc4XML(prof.current_role)).append("</current_role>");
	    //是否开放CPT/D功能
	    strBuf.append("<hasCPD>").append(hasCPD).append("</hasCPD>");
	    strBuf.append(getCourseTabsRemindXml(itm_id));
    	return strBuf.toString();
	}
	
	public static String getCourseTabsRemindXml(long itm_id){
		StringBuffer xml = new StringBuffer();
		CourseTabsRemindService courseTabsRemindService = ((CourseTabsRemindService) WzbApplicationContext.getBean("courseTabsRemindService"));
		CourseTabsRemind courseTabsRemind = courseTabsRemindService.getCourseTabsRemind(itm_id);
		xml.append("<courseTabsRemind>");
		xml.append("<rmdClassManagement>").append(courseTabsRemind.isRmdClassManagement()).append("</rmdClassManagement>");
		xml.append("<rmdCompletionCriteriaSettings>").append(courseTabsRemind.isRmdCompletionCriteriaSettings()).append("</rmdCompletionCriteriaSettings>");
		xml.append("<rmdCoursePackage>").append(courseTabsRemind.isRmdCoursePackage()).append("</rmdCoursePackage>");
		xml.append("<rmdCourseScoreSettings>").append(courseTabsRemind.isRmdCourseScoreSettings()).append("</rmdCourseScoreSettings>");
		xml.append("<rmdOnlineContent>").append(courseTabsRemind.isRmdOnlineContent()).append("</rmdOnlineContent>");
		xml.append("<rmdTargetLearner>").append(courseTabsRemind.isRmdTargetLearner()).append("</rmdTargetLearner>");
		xml.append("<rmdTimetable>").append(courseTabsRemind.isRmdTimetable()).append("</rmdTimetable>");
		xml.append("</courseTabsRemind>");
		return xml.toString();
	}
	
	public String getContentDefXml(Connection con, long itm_id){
		String sql = "select itm_type,itm_content_def,itm_run_ind from aeItem where itm_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String xml = "<content_def>";
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				xml += "<itm_type>" + rs.getString("itm_type") + "</itm_type>";
				xml += "<itm_content_def>" + rs.getString("itm_content_def") + "</itm_content_def>";
				xml += "<itm_run_ind>" + rs.getLong("itm_run_ind") + "</itm_run_ind>";
			}
		} catch(SQLException e){
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		xml += "</content_def>";
		return xml;
	}
	
	static public aeItem getCourseByClassId(Connection con, long itm_id){
		String sql = "select ire_parent_itm_id , itm_title  from aeItemRelation , aeItem where ire_parent_itm_id=itm_id and ire_child_itm_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		aeItem item = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			rs = stmt.executeQuery();
			if(rs.next()){
				item = new aeItem();
				item.itm_id = rs.getLong("ire_parent_itm_id");
				item.itm_title =  rs.getString("itm_title");
			}
		} catch(SQLException e){
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}

		return item;
	}
	
    public static aeItem getItemById(Connection con, long itm_id)
    throws SQLException, qdbException ,cwSysMessage {
    	aeItem itm = null;
        StringBuffer SQLBuf = new StringBuffer(200);
        SQLBuf.append(" Select * From aeItem ");
        SQLBuf.append(" Where itm_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        ResultSet rs = stmt.executeQuery();
        try{
        	 if(rs.next()){
             	itm = new aeItem();
             	itm.itm_id = rs.getLong("itm_id");
             	itm.itm_type= rs.getString("itm_type");
             	itm.itm_title =  rs.getString("itm_title");
             	itm.itm_code =  rs.getString("itm_code");
             	itm.itm_ref_ind =  rs.getBoolean("itm_ref_ind");
             	itm.itm_integrated_ind =  rs.getBoolean("itm_integrated_ind");
             	itm.itm_exam_ind = rs.getBoolean("itm_exam_ind");
             }
        }catch(Exception e){
        	CommonLog.error(e.getMessage(),e);
        } finally {
			cwSQL.cleanUp(rs, stmt);
		}
       
        return itm;
    }

    /**
     * 通过子tcrid获取所有上级培训中心id
     * @param con
     * @param tcr_id
     * @return
     * @throws SQLException
     */
    private static List<Long> getTcrParentId(Connection con, long tcr_id)  throws SQLException{
        String sql = "select * from tcRelation where tcn_child_tcr_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, tcr_id);
        ResultSet rs = stmt.executeQuery();
        List<Long> id_list=new ArrayList<Long>();
        while(rs.next()) {
            id_list.add(rs.getLong("tcn_ancestor")) ;
        }
        stmt.close();
        return id_list;
    }

    /**
     *  获取当前问卷的培训中心id，然后再获取所有上级培训中心id
     * @param con
     * @param mod_id
     * @return
     * @throws qdbException
     * @throws SQLException
     */
    private static List<Long> getTcrIdList(Connection con, Long mod_id) throws qdbException, SQLException {
        long mod_tcr_id=0;
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT mod_tcr_id  from Module where mod_res_id =?");
            stmt.setLong(1, mod_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mod_tcr_id= rs.getLong("mod_tcr_id");}
            else {
                stmt.close();
                throw new qdbException( "No data for module. id = " + mod_id );
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
        List<Long> tcr_id_list=new ArrayList<Long>();
        tcr_id_list.add(mod_tcr_id);
        List<Long> tcr_pid_list=getTcrParentId(con,mod_tcr_id);
        for(long tcr_id: tcr_pid_list){
            tcr_id_list.add(tcr_id);
        }
        return tcr_id_list;
    }

	public static List<Map<String, Object>> getReportByItmId(Connection con, Long[] itm_ids, Long[] cat_ids, String[] itm_types, Long mod_id,String usr_ste_usr_id) throws SQLException, qdbException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
        List<Long> tcrIdList =new ArrayList<Long>();
        //如果是admin不用加上培训中心条件
        if(!"admin".equals(usr_ste_usr_id)) {
          tcrIdList = getTcrIdList(con, mod_id);
         }
        try {
			StringBuffer sql = new StringBuffer();
			sql.append("select itm_id, itm_title, itm_code, itm_type, itm_tcr_id,")
				.append(" tcr_code, tcr_title, cat_id, cat_title, mod_res_id")
				.append(" from aeItem")
				.append(" inner join tctrainingCenter on itm_tcr_id = tcr_id")
				.append(" left join aeTreeNode on itm_id = tnd_itm_id")
				.append(" left join aeCatalog on cat_id = tnd_cat_id")
				.append(" inner join Course on itm_id = cos_itm_id")
				.append(" inner join ResourceContent on cos_res_id = rcn_res_id")
				.append(" inner join Module on mod_res_id = rcn_res_id_content")
				.append(" where 1 = 1");
			if(itm_ids != null && itm_ids.length > 0) {	// 课程标题
				sql.append(" and itm_id in (");
				for(int i=0; i<itm_ids.length; i++) {
					if(i != itm_ids.length - 1) {
						sql.append("?, ");
					} else{
						sql.append("?");
					}
				}
				sql.append(")");
			} else if(cat_ids != null && itm_types != null && cat_ids.length > 0 && itm_types.length > 0) {	//课程目录
				sql.append(" and tnd_parent_tnd_id in (");
				for(int i=0; i<cat_ids.length; i++) {
					if(i != cat_ids.length - 1) {
						sql.append("?, ");
					} else{
						sql.append("?");
					}
				}
				sql.append(") and itm_type in (");
				for(int i=0; i<itm_types.length; i++) {
					if(i != itm_types.length - 1) {
						sql.append("?, ");
					} else{
						sql.append("?");
					}
				}
				sql.append(")");
			} else if(itm_types != null && itm_types.length > 0) {
				sql.append(" and itm_type in (");
				for(int i=0; i<itm_types.length; i++) {
					if(i != itm_types.length - 1) {
						sql.append("?, ");
					} else{
						sql.append("?");
					}
				}
				sql.append(")");
			}
			sql.append(" and mod_mod_id_root = ?");
            if(tcrIdList.size()>0) {
                sql.append(" and itm_tcr_id in(");
                for (int i = 0; i < tcrIdList.size(); i++) {
                    if (i != tcrIdList.size() - 1) {
                        sql.append("?, ");
                    } else {
                        sql.append("?");
                    }
                }
                sql.append(")");
            }
            sql.append("order by itm_id");
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			if(itm_ids != null && itm_ids.length > 0) {	// 课程标题
				for(int j=0; j<itm_ids.length; j++) {
					stmt.setLong(index++, itm_ids[j]);
				}
			} else if(cat_ids != null && itm_types != null && cat_ids.length > 0 && itm_types.length > 0) {	//课程目录
				for(int m=0; m<cat_ids.length; m++) {
					stmt.setLong(index++, cat_ids[m]);
				}
				for(int n=0; n<itm_types.length; n++) {
					stmt.setString(index++, itm_types[n]);
				}
			} else if(itm_types != null && itm_types.length > 0) {
				for(int j=0; j<itm_types.length; j++) {
					stmt.setString(index++, itm_types[j]);
				}
			}
			stmt.setLong(index++, mod_id);
            if(tcrIdList.size()>0) {
                for (long tcr_id : tcrIdList) {
                    stmt.setLong(index++, tcr_id);
                }
            }
			rs = stmt.executeQuery();
			while(rs.next()) {
                long itm_id = rs.getLong("itm_id");
                String itm_title = rs.getString("itm_title");
                // 如果是itm_type:CLASSROOM(ps:面授课程)并且，cat_id不为空，跳过，因为该记录是面授课程不是班级课程
                if(rs.getString("itm_type").equals("CLASSROOM")) {
                    if(rs.getLong("cat_id") > 0){
                        continue;
                    } else {
                        // 查询父类课程名称
                        itm_title = aeItem.getParentByItmid(con, itm_id).itm_title;
                    }
                }
				Map<String, Object> params = new HashMap<String, Object>();
				aeItem item = new aeItem();
				TcTrainingCenter trainingCenter = new TcTrainingCenter();
				aeCatalog catalog = new aeCatalog();
                item.itm_id = itm_id;
                item.itm_title = itm_title;
				item.itm_code = rs.getString("itm_code");
				item.itm_type = rs.getString("itm_type");
				item.itm_tcr_id = rs.getLong("itm_tcr_id");
				trainingCenter.setTcr_id(rs.getLong("itm_tcr_id"));
				trainingCenter.setTcr_code(rs.getString("tcr_code"));
				trainingCenter.setTcr_title(rs.getString("tcr_title"));
				catalog.cat_id = rs.getLong("cat_id");
				catalog.cat_title = rs.getString("cat_title");
				params.put("item", item);
				params.put("trainingCenter", trainingCenter);
				params.put("catalog", catalog);
				params.put("res_id", rs.getLong("mod_res_id"));
				list.add(params);
			}
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}

    /**
     * 通过子类获取父类课程信息（暂用于导出保持和查看名称一样）
     * @param con
     * @param itm_id
     * @return
     * @throws SQLException
     */
    public static aeItem getParentByItmid(Connection con, long itm_id) {
        String sql = "SELECT itm_title FROM aeItem,aeItemRelation WHERE ire_parent_itm_id = itm_id AND ire_child_itm_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        aeItem aeItem = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, itm_id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                aeItem = new aeItem();
                aeItem.itm_title = rs.getString("itm_title");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cwSQL.cleanUp(rs, stmt);
        }
        return aeItem;
    }
    
    public String getNotifyEmail(Connection con)throws SQLException{
    	String SQL = "Select itm_notify_email from aeItem where itm_id = ? ";
    	PreparedStatement stmt1 = con.prepareStatement(SQL);
        stmt1.setLong(1, this.itm_id);
        
        ResultSet rs = stmt1.executeQuery();
        if(rs.next()) {
        	this.itm_notify_email = rs.getString("itm_notify_email");
        }
        return this.itm_notify_email;
    }

}