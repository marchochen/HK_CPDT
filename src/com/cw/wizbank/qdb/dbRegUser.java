package com.cw.wizbank.qdb;

import com.cw.wizbank.Application;
import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.JsonMod.eip.bean.EnterpriseInfoPortalBean;
import com.cw.wizbank.JsonMod.eip.dao.EnterpriseInfoPortalDao;
import com.cw.wizbank.accesscontrol.*;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.ae.db.DbAppnTargetEntity;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.ae.db.DbLearningSoln;
import com.cw.wizbank.ae.db.view.ViewItemTargetGroup;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.view.ViewEntityRelation;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.personalization.PsnPreference;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.CookieUtil;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.web.WzbApplicationContext;
import org.apache.poi.hsmf.datatypes.Types;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.*;
import java.sql.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class dbRegUser extends dbEntity
{
    public static final String MSG_MULTI_LOGIN = "USR010";
    public static final String MSG_USER_HAS_NO_SUCH_ROLE = "USR011";

    public static final String USR_STATUS_OK = "OK";
    public static final String USR_STATUS_SYS = "SYS";
    public static final String USR_STATUS_PENDING = "PENDING";
    public static final String USR_STATUS_INACTIVE = "INACTIVE";

    //bill 2016 01 16 frist login
    public static final String FIRST_LOGIN = "first_login"; 
    public static final String FIRST_DAY_LOGIN = "first_day_login";
    public static final String FIRST_MONTH_LOGIN = "first_month_login";
    public static final String FIRST_YEAR_LOGIN = "first_year_login";
    
    // retired?
//    public static final String USR_STATUS_BAD_EMAIL = "BAD_EMAIL";
    public static final String USR_STATUS_DELETED = "DELETED";
    public static final String ROLE_TYPE = "ROLE";

    public static final String CODE_LOGIN_SUCCESS = "LGS01";
    public static final String CODE_LOGIN_AND_UPDATE = "LGS02";
    public static final String CODE_CREATE_AND_LOGIN = "LGS03";
    public static final String CODE_CTAM_LOGIN = "LGS04";
    public static final String CODE_CHANGE_PWD = "LGS05";

    public static final String CODE_UNKOWN_ERROR = "LGF00";
    public static final String CODE_USER_NOT_EXIST = "LGF01";
    public static final String CODE_NOT_TRUSTED_SITE = "LGF02";
    public static final String CODE_SITE_EXPIRED = "LGF03";
    public static final String CODE_PWD_INVALID = "LGF04";
    public static final String CODE_ATTEMPT_EXCEED_LIMIT = "LGF05";
    public static final String CODE_INACTIVE_LOGIN = "LGF06";
    public static final String CODE_INVALID_LOGIN_MODE = "LGF07";
    public static final String CODE_MAX_LOGIN_USER_EXCEED_LIMIT = "LGF08";
    public static final String CODE_OVER_VALIDITY_PERIOD = "LGF09";
    public static final String CODE_IS_NOT_LEARNER_ACCOUNT = "LGF012";
    public static final String CODE_USER_NOT_ROLE = "LGF10";
    public static final String CODE_USER_SYSTEM_ISSUE = "LGF13";


    public static final String CODE_OFF_LOGIN_SUCCESS = "login_code=success1";
    public static final String CODE_OFF_LOGIN_FAILURE0 = "failure0";
    public static final String CODE_OFF_LOGIN_FAILURE1 = "failure1";


    public static final String AD_CODE_PWD_INVALID = "LGF09";
    public static final String AD_CODE_USER_NOT_EXIST = "LGF010";
    public static final String AD_CODE_UNKOWN_ERROR = "LGF011";

    public static final String CODE_CANNOT_CHANGE_GROUP = "USR021";
    public static final String CODE_CANNOT_CHANGE_DIRECT_SUPERVISOR = "USR022";
    public static final String CODE_CANNOT_CHANGE_GROUP_SUPERVISOR = "USR023";
    public static final String CODE_CANNOT_CHANGE_SUPERVISED_GROUP = "USR024";
    public static final String CODE_USER_HAS_PENDING_APPROVAL = "USR025";
    public static final String CODE_SOLE_TADM = "USR026";
    public static final String CODE_SOLE_TADM_TC = "USR028";

    public static final String CODE_FIELDS_NOT_VALID = "LGF";
    public static final String CODE_FIELDS_NOT_VALID_SEPARATOR = "-";

    public static final String FIELD_CODE_USR_ID            = "01";
    public static final String FIELD_CODE_USR_PWD           = "02";
    public static final String FIELD_CODE_USR_DISPLAY_BIL   = "03";
    public static final String FIELD_CODE_USR_GENDER        = "04";
    public static final String FIELD_CODE_USR_BDAY          = "05";
    public static final String FIELD_CODE_USR_EMAIL         = "06";
    public static final String FIELD_CODE_USR_TEL_1         = "07";
    public static final String FIELD_CODE_USR_FAX_1         = "08";
    public static final String FIELD_CODE_USR_JOB_TITLE     = "09";
    public static final String FIELD_CODE_GRADE_CODE        = "10";
    public static final String FIELD_CODE_GROUP_CODE        = "11";
    public static final String FIELD_CODE_DIRECT_SUPERVISOR_USR = "12";
    public static final String FIELD_CODE_USR_JOIN_DATE     = "13";
    public static final String FIELD_CODE_USR_ROLE_CODE = "14";
    public static final String FIELD_CODE_SUPERVISE_TARGET_GROUP_CODE = "15";
    public static final String FIELD_CODE_USR_EXTRA_1       = "16";
    public static final String FIELD_CODE_USR_EXTRA_2       = "17";
    public static final String FIELD_CODE_USR_EXTRA_3       = "18";
    public static final String FIELD_CODE_USR_EXTRA_4       = "19";
    public static final String FIELD_CODE_USR_EXTRA_5       = "20";
    public static final String FIELD_CODE_USR_EXTRA_6       = "21";
    public static final String FIELD_CODE_USR_EXTRA_7       = "22";
    public static final String FIELD_CODE_USR_EXTRA_8       = "23";
    public static final String FIELD_CODE_USR_EXTRA_9       = "24";
    public static final String FIELD_CODE_USR_EXTRA_10      = "25";
    //failure code for new extension field usr_extra_11 to usr_extra_20
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_11      = "26";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_12      = "27";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_13      = "28";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_14      = "29";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_15      = "30";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_16      = "31";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_17      = "32";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_18      = "33";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_19      = "34";
    public static final String FIELD_CODE_USR_EXTRA_DATETIME_20      = "35";
    //failure code for extension field usr_extra_singleoption_21 to usr_extra_singleoption_30
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_21  = "36";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_22  = "37";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_23  = "38";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_24  = "39";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_25  = "40";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_26  = "41";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_27  = "42";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_28  = "43";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_29  = "44";
	public static final String FIELD_CODE_USR_EXTRA_SINGLEOPTION_30  = "45";
	//failure code for extension field usr_extra_multipleoption_21 to usr_extra_multipleoption_30
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_31  = "46";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_32  = "47";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_33  = "48";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_34  = "49";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_35  = "50";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_36  = "51";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_37  = "52";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_38  = "53";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_39  = "54";
	public static final String FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_40  = "55";

    public static final String CODE_FIELDS_ERROR_TYPE_LENGTH    = "01";
    public static final String CODE_FIELDS_ERROR_TYPE_REQD      = "02";
    public static final String CODE_FIELDS_ERROR_TYPE_FORMAT    = "03";
    public static final String CODE_FIELDS_ERROR_TYPE_VALUE     = "04";
    public static final String USER_SOURCE_WIZBANK = "wizBank";


    private static final String MG_USR_REG_DISAPPROVE = "USR_REG_DISAPPROVE";
    private static final String MG_USR_REG_APPROVE = "USR_REG_APPROVE";
    private static final String MG_USR_CREATION = "USR_CREATION";
    private static final String MG_USER_PWD_RESET_NOTIFY = "USER_PWD_RESET_NOTIFY";

    private static final String AFF_LOGIN_MODE_BASIC = "BASIC";
    private static final String AFF_LOGIN_MODE_ADVANCED = "ADVANCED";

    public static final String USR_NOT_IS_TC_TA ="USR031";
    public static final String USR_NOT_IS_TC_INSTR ="USR032";

    public static final String LOGIN_BLOCKED = "BLOCKED";
    public static final String LOGIN_WARNING = "WARNING";
    
    public static final String CODE_USR_NEED_CHANGE_PASSWORD = "usr_need_change_password";
    
    public static final String LOGIN_LAN = "login_lan";
    public static final int LOGIN_LAN_AGE = 3600*24*30*12*1000;
    
    public  String transaction;

    public String  usr_id;
    public String nickname;
    public long    usr_ent_id;
    public String  usr_pwd;
    public String  usr_email;
    public String  usr_email_2;
    public String  usr_full_name_bil;
    public String  usr_initial_name_bil;
    public String  usr_last_name_bil;
    public String  usr_first_name_bil;
    public String  usr_display_bil;
    public String  usr_nickname;
    public String  usr_gender;
    public Timestamp    usr_bday;
    //latest updated(christ):add
    public boolean is_valid_usr_bday = true;
    //public String  usr_bplace_bil;
    public String  usr_hkid;
    public String  usr_other_id_no;
    public String  usr_other_id_type;
    public String  usr_tel_1;
    public String  usr_tel_2;
    public String  usr_fax_1;
    public String  usr_country_bil;
    public String  usr_postal_code_bil;
    public String  usr_state_bil;
//    public String  usr_city_bil;
    public String  usr_address_bil;
//    public String  usr_occupation_bil;
//    public String  usr_income_level;
//    public String  usr_edu_role;
//    public String  usr_edu_level;
//    public String  usr_school_bil;
    public String  usr_class;
    public String  usr_class_number;
    public Timestamp usr_signup_date;
    public Timestamp usr_last_login_date;
//    public Timestamp usr_special_date_1;
    public String  usr_status;
    public Timestamp usr_upd_date;
    public long usr_ste_ent_id;
    public String usr_ste_usr_id;
    public String current_role;
//  usr_source is defined for identify which system this user belong to.
    public String usr_source;
    public String usr_other_id;
    public String usr_extra_1;
    public String usr_extra_2;
    public String usr_extra_3;
    public String usr_extra_4;
    public String usr_extra_5;
    public String usr_extra_6;
    public String usr_extra_7;
    public String usr_extra_8;
    public String usr_extra_9;
    public String usr_extra_10;
    public Timestamp usr_approve_timestamp;
    public String usr_approve_usr_id;
    public String usr_approve_reason;
    public String usr_cost_center;
    public int usr_login_trial;
    public String usr_remark_xml;
    public String usr_old_pwd;
    public boolean usr_pwd_need_change_ind;
    public Timestamp usr_pwd_upd_timestamp;

    public boolean usr_syn_rol_ind = true;
    public String usr_not_syn_gpm_type;

    public Timestamp usr_join_datetime;
    //latest updated(christ):add
    public boolean is_valid_usr_join_datetime = true;
    public String usr_job_title;
    public long usr_app_approval_usg_ent_id;
    //new field for user extension
	public Timestamp usr_extra_datetime_11;
    public boolean is_valid_usr_extra_datetime_11 = true;
	public Timestamp usr_extra_datetime_12;
    public boolean is_valid_usr_extra_datetime_12 = true;
	public Timestamp usr_extra_datetime_13;
    public boolean is_valid_usr_extra_datetime_13 = true;
	public Timestamp usr_extra_datetime_14;
    public boolean is_valid_usr_extra_datetime_14 = true;
	public Timestamp usr_extra_datetime_15;
    public boolean is_valid_usr_extra_datetime_15 = true;
	public Timestamp usr_extra_datetime_16;
    public boolean is_valid_usr_extra_datetime_16 = true;
	public Timestamp usr_extra_datetime_17;
    public boolean is_valid_usr_extra_datetime_17 = true;
	public Timestamp usr_extra_datetime_18;
    public boolean is_valid_usr_extra_datetime_18 = true;
	public Timestamp usr_extra_datetime_19;
    public boolean is_valid_usr_extra_datetime_19 = true;
	public Timestamp usr_extra_datetime_20;
    public boolean is_valid_usr_extra_datetime_20 = true;
    //user extension for single option
    public String usr_extra_singleoption_21;
	public String usr_extra_singleoption_22;
	public String usr_extra_singleoption_23;
	public String usr_extra_singleoption_24;
	public String usr_extra_singleoption_25;
	public String usr_extra_singleoption_26;
	public String usr_extra_singleoption_27;
	public String usr_extra_singleoption_28;
	public String usr_extra_singleoption_29;
	public String usr_extra_singleoption_30;
	//user extension for multiple choices
	public String usr_extra_multipleoption_31;
	public String usr_extra_multipleoption_32;
	public String usr_extra_multipleoption_33;
	public String usr_extra_multipleoption_34;
	public String usr_extra_multipleoption_35;
	public String usr_extra_multipleoption_36;
	public String usr_extra_multipleoption_37;
	public String usr_extra_multipleoption_38;
	public String usr_extra_multipleoption_39;
	public String usr_extra_multipleoption_40;
	
    public String urx_extra_datetime_11;
    public String urx_extra_datetime_12;
    public String urx_extra_datetime_13;
    public String urx_extra_datetime_14;
    public String urx_extra_datetime_15;
    public String urx_extra_datetime_16;
    public String urx_extra_datetime_17;
    public String urx_extra_datetime_18;
    public String urx_extra_datetime_19;
    public String urx_extra_datetime_20;
    public String urx_extra_singleoption_21;
    public String urx_extra_singleoption_22;
    public String urx_extra_singleoption_23;
    public String urx_extra_singleoption_24;
    public String urx_extra_singleoption_25;
    public String urx_extra_singleoption_26;
    public String urx_extra_singleoption_27;
    public String urx_extra_singleoption_28;
    public String urx_extra_singleoption_29;
    public String urx_extra_singleoption_30;
    public String urx_extra_multipleoption_31;
    public String urx_extra_multipleoption_32;
    public String urx_extra_multipleoption_33;
    public String urx_extra_multipleoption_34;
    public String urx_extra_multipleoption_35;
    public String urx_extra_multipleoption_36;
    public String urx_extra_multipleoption_37;
    public String urx_extra_multipleoption_38;
    public String urx_extra_multipleoption_39;
    public String urx_extra_multipleoption_40;
    
    public String urx_extra_41;
    public String urx_extra_42;
    public String urx_extra_43;
    public String urx_extra_44;
    public String urx_extra_45;
    
    public String extension_43_select;

    //public String usr_role;
    public String login_role;       // login wizbank using this role
    public String[] usr_roles;
//    public Timestamp[] usr_role_starts;
//    public Timestamp[] usr_role_ends;
    public String[] usr_roles_starts;
    public String[] usr_roles_ends;
    public long[] usr_attribute_ent_ids;
    public String[] usr_attribute_relation_types;
    public String upd_usr_id;
    public String[] rol_target_ext_ids; //corresponding role of each target entiity group
    public String[] rol_target_ent_groups; //target entity group of role e.g. {"123,456", "789,9"}
    public String[] rol_target_starts;
    public String[] rol_target_ends;
    public String[] appr_rol_ext_ids; //role ext id of this user's approvers
    public String[] appr_ent_ids;  //entity id of this user's approvers

    public String[] direct_supervisor_ent_ids;
    public String[] direct_supervisor_starts;
    public String[] direct_supervisor_ends;

    public String[] supervise_target_ent_ids;
    public String[] supervise_target_starts;
    public String[] supervise_target_ends;

    public Vector usg_display_bil;
    public String ugr_display_bil;  // {string}

    // for aff login
    public String group_code;
    public String grade_code;
    public String[] direct_supervisor_usr_lst; // login id of direct supervisors
    public String[] supervise_target_group_code_lst; // group code of supervised group
    public String[] role_code_lst;

    public long usr_parent_usg_id ;  //user parent usergroup entity id

    public long usr_ske_id;
    
    public long usr_group_id;
    public long usr_grade_id;
    public long[] usg_ent_id_lst;

    
    public String usr_weixin_id;
    
    

    public dbRegUser(){
        usg_display_bil = new Vector();
    }

    //pre-define variable: usr_ent_id
    public String getGrantedRolesAsXML(Connection con) throws SQLException {

        AccessControlWZB acl = new AccessControlWZB();
        Timestamp cur_time = cwSQL.getTime(con);
        List roleArray = DbAcRole.getRolesCanLogin(con, usr_ent_id, cur_time);
        StringBuffer xmlBuf = new StringBuffer(1024);

        xmlBuf.append("<granted_roles>");
        for(int i=0; i<roleArray.size(); i++) {
        	DbAcRole role = (DbAcRole)roleArray.get(i);
            xmlBuf.append(AccessControlWZB.getRolXml(role.rol_ext_id,role.rol_title));
        }
        xmlBuf.append("</granted_roles>");
        return xmlBuf.toString();
    }

    //pre-defined variable: usr_ent_id
    //return URL_HOME of the new role
    //选择角色方法
    public String changeRole(Connection con, String rol_ext_id,
                            loginProfile prof, HttpSession s, WizbiniLoader wizbini)
                            throws SQLException, cwException {

        //check if user has the selected role
        AccessControlWZB acl = new AccessControlWZB();
        //检查该用户是否具备需要选择的角色
        if(!acl.hasUserRole(con, usr_ent_id, rol_ext_id)) {
            throw new cwException("User don't belong to " + rol_ext_id);
        }

        //update usr_last_login_role（更新用户最后一次选择的角色的信息）
        updLastLoginRole(con, rol_ext_id);

        //update loginProfile
        prof.current_role = rol_ext_id;
        String[] rol_array = dbUtils.getRoleNSkinNHomeURL(con, prof.current_role);
        String[] prefer_array = PsnPreference.getPreferenceByEntId(con, prof.usr_ent_id, prof.current_role, (Personalization)wizbini.cfgOrgPersonalization.get(prof.root_id), null, null);
        prof.current_role_xml = rol_array[0];
        prof.current_role_skin_root = prefer_array[0];
        prof.role_url_home = rol_array[1];
        prof.common_role_id = getCommnonRoleId(prof.current_role);
        prof.isLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
//        AcHomePage acHomepage = new AcHomePage(con);
        //acHomepage.getGrantedFunction(prof, wizbini.cfgTcEnabled);

		//当前角色的菜单
		AcRoleFunctionService acRoleFunctionService = (AcRoleFunctionService) WzbApplicationContext.getBean("acRoleFunctionService");
		prof.setRoleFunctions(acRoleFunctionService.getFunctions(prof.current_role));
        
        //update the user display name and grantedRolesXML
		dbRegUser regUser = new dbRegUser();
		regUser.usr_ent_id = prof.usr_ent_id;
		prof.usr_display_bil = regUser.getDisplayBil(con);//to update HOME usr_display_bil every time go to homepage
        prof.grantedRolesXML = regUser.getGrantedRolesAsXML(con);

        //put profile in session
        s.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
        prof.writeSession(s);

        return prof.role_url_home;
    }

    //update column usr_last_login_role in database base on usr_ent_id
    //pre-define variables: usr_ent_id
    private void updLastLoginRole(Connection con, String rol_ext_id) throws SQLException {

        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append("Update RegUser Set ");
        SQLBuf.append(" usr_last_login_role = ? ");
        SQLBuf.append(" Where usr_ent_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setString(index++, rol_ext_id);
        stmt.setLong(index++, usr_ent_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    /**************************** DENNIS ******************************/
    public static boolean aff_trashUsers(Connection con, String steUsrId, long site_id, String host)
      throws cwException, qdbErrMessage {
        try {
            boolean bOK = false;
            // Get the site by site id
            acSite site = new acSite();
            site.ste_ent_id=site_id;
            site.get(con);

            // Example  : host = wizbank.cyberwisdom.net , domain = cyberwisdom.net
            String req_domain = host.substring(host.indexOf(".")+1, host.length());
            if (site.ste_domain !=null && site.ste_domain.equalsIgnoreCase(req_domain)) {
                bOK = true;
            }

            if (!bOK)
                return false;

            dbRegUser usr;

            usr = new dbRegUser();
            usr.usr_ent_id = getEntId(con, steUsrId, site_id);
            usr.ent_id = usr.usr_ent_id;

            usr.delSteUsrNoTimeStampCheck(con);
            usr.delAllEntityRelation(con, steUsrId, null);

            dbEnrolment.delAllByEntId(con, usr.usr_ent_id);
            dbResourcePermission.delAllByEntId(con, usr.usr_ent_id);
            aeApplication.delAllApp(con, usr.usr_ent_id);

            AccessControlWZB acl = new AccessControlWZB();
            acl.rmUserRoles(con, usr.usr_ent_id);

            return true;
        }
        catch(qdbException qdbe) {
            throw new cwException(qdbe.getMessage());
        }
        catch(SQLException sqle) {
            throw new cwException(sqle.getMessage());
        }
    }

    public static void trashUsers(Connection con, loginProfile prof, long[] usr_ent_ids, Timestamp[] usr_timestamps)
      throws cwException, qdbErrMessage {
        try {
            //check if the user has right to trash users
            AccessControlWZB acl = new AccessControlWZB();

            int size = (usr_ent_ids.length<usr_timestamps.length ? usr_ent_ids.length : usr_timestamps.length);
            dbRegUser usr;
            Timestamp deleteTime = cwSQL.getTime(con);
            for(int i=0;i<size;i++) {
                usr = new dbRegUser();
                usr.usr_ent_id = usr_ent_ids[i];
                usr.ent_id = usr_ent_ids[i];
                usr.usr_upd_date = usr_timestamps[i];
                usr.ent_upd_date = usr_timestamps[i];
                //allow trash user even user is not in recycle bin
                /*
                if(!usr.isDeleted(con))
                    throw new qdbErrMessage("USR005");
                */
                usr.delSteUsr(con);
                usr.delAllEntityRelation(con, prof.usr_id, deleteTime);

                dbEnrolment.delAllByEntId(con, usr.usr_ent_id);
                dbResourcePermission.delAllByEntId(con, usr.usr_ent_id);
                aeApplication.delAllApp(con, usr.usr_ent_id);

                acl.rmUserRoles(con, usr.usr_ent_id);
            }
        }
        catch(qdbException qdbe) {
            throw new cwException(qdbe.getMessage());
        }
        catch(SQLException sqle) {
            throw new cwException(sqle.getMessage());
        }
    }

    //check the usr_status to see if the user record deleted
    //return true if usr_status == USR_STATUS_DELETED
    //else retrur false
    //pre-set var: usr_ent_id
    public boolean isDeleted(Connection con) throws SQLException {
        boolean result;
        String status;
        final String SQL = " Select usr_status From RegUser "
                         + " Where usr_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            status = rs.getString("usr_status");
            result = status.equals(USR_STATUS_DELETED);
        }
        else
            result = true;
        stmt.close();
        return result;
    }

    //check the usr_status to see if the user is a system user
    //return true if usr_status == USR_STATUS_SYS
    //else retrur false
    //pre-set var: usr_ent_id
    public static boolean isSystemUser(Connection con, long usr_ent_id) throws SQLException {
        boolean result;
        String status;
        final String SQL = " Select usr_status From RegUser "
                         + " Where usr_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            status = rs.getString("usr_status");
            result = status.equals(USR_STATUS_SYS);
        }
        else
            result = true;
        stmt.close();
        return result;
    }
    
    //set the usr_ste_usr_id = null for the user
    //set the usr_status  = 'DELETED'
    //pre-set var: usr_ent_id, ent_id
    //ONLY call from a trusted domain in Affliate program
    private void delSteUsrNoTimeStampCheck(Connection con) throws SQLException, qdbErrMessage, qdbException {

        final String SQL = " Update RegUser Set "
                            + " usr_ste_usr_id = null "
                            + " ,usr_status = ? "
                            + " Where usr_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, USR_STATUS_DELETED);
        stmt.setLong(2, usr_ent_id);
        stmt.executeUpdate();
        stmt.close();
    }

    //set the usr_ste_usr_id = null for the user
    //check the usr_status  = 'DELETED'
    //pre-set var: usr_ent_id, ent_id, usr_upd_date, ent_upd_date
    //pls check user privillege before calling this function
    private void delSteUsr(Connection con) throws SQLException, qdbErrMessage, qdbException {
        // check timeStamp
        super.checkTimeStamp(con);

        final String SQL = " Update RegUser Set "
                            + " usr_ste_usr_id = null "
                            + " ,usr_status = ? "
                            + " Where usr_ent_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, USR_STATUS_DELETED);
        stmt.setLong(2, usr_ent_id);
        stmt.executeUpdate();
        stmt.close();
    }

    //delete all the user-entity relations
    //pre-set var: usr_ent_id
    //pls check user privillege before calling this function
    private void delAllEntityRelation(Connection con, String delete_usr_id, Timestamp deleteTime) throws SQLException {
    	//not really delete from EntityRelation, move records to History
    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_child_ent_id = usr_ent_id;
    	dbEr.delAllEntityRelationAsChild(con, delete_usr_id, deleteTime);
    }

    //delete all the user-entity relation of the gpm_type
    //pre-set var: usr_ent_id
    //pls check user privillege before calling this function
    public void delAllEntityRelation(Connection con, String delete_usr_id, String ern_type, Timestamp endTime) throws SQLException {
    	dbEntityRelation dbEr = new dbEntityRelation();
    	dbEr.ern_child_ent_id = usr_ent_id;
    	dbEr.ern_type = ern_type;
    	dbEr.delAsChild(con, delete_usr_id, endTime);
    }

    public boolean isActive(Connection con, long siteId, String userId, String userHKId, String userOtherIdNo)
        throws qdbErrMessage, qdbException, cwException, cwSysMessage {

        try {
            long count = 0;
            StringBuffer result = new StringBuffer();
            String query = "SELECT count(*) FROM regUser "
                         + " WHERE usr_status = ? "
                         + " AND usr_ste_ent_id = ? "
                         + " AND usr_ste_usr_id = ? "
                         + " AND usr_hkid = ? "
                         + " AND usr_other_id_no = ? ";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, USR_STATUS_OK);
            stmt.setLong(2, siteId);
            stmt.setString(3, userId);
            stmt.setString(4, userHKId);
            stmt.setString(5, userOtherIdNo);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                count = rs.getLong(1);
            }
            stmt.close();

            return count == 1;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    private static boolean isInActive(Connection con, long siteId, String userId)
        throws qdbErrMessage, qdbException, cwException, cwSysMessage {

        try {
            long count = 0;
            StringBuffer result = new StringBuffer();
            String query = "SELECT count(*) FROM regUser "
                         + " WHERE usr_status = ? "
                         + " AND usr_ste_ent_id = ? "
                         + " AND usr_ste_usr_id = ? ";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, USR_STATUS_INACTIVE);
            stmt.setLong(2, siteId);
            stmt.setString(3, userId);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                count = rs.getLong(1);
            }
            stmt.close();

            return count == 1;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public String getInactiveUserXML(Connection con, long siteId, String userId, String userHKId, String userOtherIdNo)
        throws qdbErrMessage, qdbException, cwException, cwSysMessage {

        try {
            StringBuffer result = new StringBuffer();
            String query = "SELECT usr_id, usr_ste_usr_id, usr_ent_id, usr_ste_ent_id, "
                         + " usr_display_bil, usr_other_id_no, usr_hkid, usr_status, "
                         + " usr_last_login_date, usr_upd_date, usr_email, usr_email_2, usr_extra_1, usr_extra_2, usr_extra_3 "
                         + " FROM regUser "
                         + " WHERE usr_status = ? "
                         + " AND usr_ste_ent_id = ? "
                         + " AND usr_ste_usr_id = ? "
                         + " AND usr_extra_2 = ? "
                         + " AND usr_extra_3 = ? ";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, USR_STATUS_INACTIVE);
            stmt.setLong(2, siteId);
            stmt.setString(3, userId);
            stmt.setString(4, userHKId);
            stmt.setString(5, userOtherIdNo);

            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                usr_id              = rs.getString("usr_id");
                usr_ste_usr_id      = rs.getString("usr_ste_usr_id");
                usr_ent_id          = rs.getLong("usr_ent_id");
                usr_ste_ent_id      = rs.getLong("usr_ste_ent_id");
                usr_display_bil     = rs.getString("usr_display_bil");
                usr_status          = rs.getString("usr_status");
                usr_last_login_date = rs.getTimestamp("usr_last_login_date");
                usr_upd_date        = rs.getTimestamp("usr_upd_date");
                usr_email           = rs.getString("usr_email");
                usr_email_2         = rs.getString("usr_email_2");
                usr_extra_1         = rs.getString("usr_extra_1");
                usr_extra_2         = rs.getString("usr_extra_2");
                usr_extra_3         = rs.getString("usr_extra_3");
            }
            stmt.close();

            if(count != 1) {
                throw new qdbErrMessage("USR001", userId);
            }

            String de_usr_ste_usr_id = usr_ste_usr_id;

            result.append("<user id=\"").append(dbUtils.esc4XML(de_usr_ste_usr_id));
            result.append("\" ent_id=\"").append(usr_ent_id);
            result.append("\" status=\"").append(usr_status);
            result.append("\" last_login=\"").append(usr_last_login_date);
            result.append("\" timestamp=\"").append(usr_upd_date).append("\">").append(dbUtils.NEWL);
            result.append("<email email_1=\"").append(dbUtils.esc4XML(usr_email));
            result.append("\" email_2=\"").append(dbUtils.esc4XML(usr_email_2));
            result.append("\" />").append(dbUtils.NEWL);

            String de_usr_display_bil = usr_display_bil;

            result.append("<name display_name=\"").append(dbUtils.esc4XML(de_usr_display_bil)).append("\" />").append(dbUtils.NEWL);
            result.append("<hkid>").append(usr_hkid).append("</hkid>").append(dbUtils.NEWL);
            result.append("<other_id no=\"").append(usr_other_id_no).append("\" />").append(dbUtils.NEWL);
            result.append("<extra_1>").append(dbUtils.esc4XML(usr_extra_1)).append("</extra_1>").append(dbUtils.NEWL);
            result.append("<extra_2>").append(dbUtils.esc4XML(usr_extra_2)).append("</extra_2>").append(dbUtils.NEWL);
            result.append("<extra_3>").append(dbUtils.esc4XML(usr_extra_3)).append("</extra_3>").append(dbUtils.NEWL);

            dbRegUser aUser = new dbRegUser();
            aUser.usr_ent_id = usr_ent_id;
            aUser.usr_ste_ent_id = siteId;
            result.append(aUser.getEntityAttributesAsXML(con));
            aUser = null;

            result.append("</user>").append(dbUtils.NEWL);

            return result.toString();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    private static final String SQL_USR_COL =
             "usr_id"
            + ",usr_ste_usr_id "
            + ",usr_ent_id "
            + ",usr_ste_ent_id "
            + ",usr_pwd "
            + ",usr_email "
            + ",usr_email_2 "
            + ",usr_full_name_bil "
            + ",usr_initial_name_bil "
            + ",usr_last_name_bil "
            + ",usr_first_name_bil "
            + ",usr_nickname "
            + ",usr_display_bil "
            + ",usr_gender "
            + ",usr_bday "
//            + " ,usr_bplace_bil "
            + ",usr_hkid "
            + ",usr_other_id_no "
            + ",usr_other_id_type "
            + ",usr_tel_1 "
            + ",usr_tel_2 "
            + ",usr_fax_1 "
            + ",usr_country_bil "
            + ",usr_postal_code_bil "
            + ",usr_state_bil "
//            + " ,usr_city_bil "
            + ",usr_address_bil "
//            + " ,usr_occupation_bil "
//            + " ,usr_income_level "
//            + " ,usr_edu_role "
//            + " ,usr_edu_level "
//            + " ,usr_school_bil "
            + ",usr_class "
            + ",usr_class_number "
            + ",usr_signup_date "
            + ",usr_last_login_date "
//            + " ,usr_special_date_1 "
            + ",usr_status "
            + ",usr_upd_date "
            + ",usr_source "
            + ",usr_extra_1 "
            + ",usr_extra_2 "
            + ",usr_extra_3 "
            + ",usr_extra_4 "
            + ",usr_extra_5 "
            + ",usr_extra_6 "
            + ",usr_extra_7 "
            + ",usr_extra_8 "
            + ",usr_extra_9 "
            + ",usr_extra_10 "
            + ",urx_extra_datetime_11 "
            + ",urx_extra_datetime_12 "
            + ",urx_extra_datetime_13 "
            + ",urx_extra_datetime_14 "
            + ",urx_extra_datetime_15 "
            + ",urx_extra_datetime_16 "
            + ",urx_extra_datetime_17 "
            + ",urx_extra_datetime_18 "
            + ",urx_extra_datetime_19 "
            + ",urx_extra_datetime_20 "
            + ",urx_extra_singleoption_21 "
            + ",urx_extra_singleoption_22 "
            + ",urx_extra_singleoption_23 "
            + ",urx_extra_singleoption_24 "
            + ",urx_extra_singleoption_25 "
            + ",urx_extra_singleoption_26 "
            + ",urx_extra_singleoption_27 "
            + ",urx_extra_singleoption_28 "
            + ",urx_extra_singleoption_29 "
            + ",urx_extra_singleoption_30 "
            + ",urx_extra_multipleoption_31 "
            + ",urx_extra_multipleoption_32 "
            + ",urx_extra_multipleoption_33 "
            + ",urx_extra_multipleoption_34 "
            + ",urx_extra_multipleoption_35 "
            + ",urx_extra_multipleoption_36 "
            + ",urx_extra_multipleoption_37 "
            + ",urx_extra_multipleoption_38 "
            + ",urx_extra_multipleoption_39 "
            + ",urx_extra_multipleoption_40 "
            + ",usr_cost_center "
            + ",usr_remark_xml "
            + ",usr_approve_usr_id "
            + ",usr_approve_timestamp "
            + ",usr_approve_reason "
            + ",usr_syn_rol_ind "
            + ",usr_not_syn_gpm_type "
            + ",usr_job_title "
            + ",usr_join_datetime "
            + ",usr_app_approval_usg_ent_id "
            +",usr_nickname "
            +",urx_extra_41 "
            +",urx_extra_42 "
            +",urx_extra_43 "
            +",urx_extra_44 "
    		+",urx_extra_45 ";
	private static final String SQL_USR_EXTENSION_FIELD
			= "select * from RegUserExtension where urx_usr_ent_id=?";

    public void get(Connection con)
        throws qdbException
    {
       try {
            ent_id = usr_ent_id;
            super.getById(con);

            PreparedStatement stmt = con.prepareStatement(
            "SELECT " + SQL_USR_COL
            + " FROM RegUser,ReguserExtension where RegUser.usr_ent_id=ReguserExtension.urx_usr_ent_id and RegUser.usr_ent_id = ? ");

        stmt.setLong(1, usr_ent_id);

        //stmt.setString(2, USR_STATUS_SYS);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
        {
            getFromRS(con, rs);
        }
        else
        {
        	if(rs!=null)rs.close();
        	stmt.close();
            throw new qdbException("Failed to get user." + usr_id);
        }
        rs.close();
        stmt.close();
		getExtFields(con);
       } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }
	public void getExtFields(Connection con) throws qdbException
	{//TODO
		try{
			PreparedStatement pstmt = con.prepareStatement(SQL_USR_EXTENSION_FIELD);
			pstmt.setLong(1,usr_ent_id);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				//user extension of datetime fields
				this.usr_extra_datetime_11 = rs.getTimestamp("urx_extra_datetime_11");
				this.usr_extra_datetime_12 = rs.getTimestamp("urx_extra_datetime_12");
				this.usr_extra_datetime_13 = rs.getTimestamp("urx_extra_datetime_13");
				this.usr_extra_datetime_14 = rs.getTimestamp("urx_extra_datetime_14");
				this.usr_extra_datetime_15 = rs.getTimestamp("urx_extra_datetime_15");
				this.usr_extra_datetime_16 = rs.getTimestamp("urx_extra_datetime_16");
				this.usr_extra_datetime_17 = rs.getTimestamp("urx_extra_datetime_17");
				this.usr_extra_datetime_18 = rs.getTimestamp("urx_extra_datetime_18");
				this.usr_extra_datetime_19 = rs.getTimestamp("urx_extra_datetime_19");
				this.usr_extra_datetime_20 = rs.getTimestamp("urx_extra_datetime_20");
				//user extension for single choice
				this.usr_extra_singleoption_21 = rs.getString("urx_extra_singleoption_21");
				this.usr_extra_singleoption_22 = rs.getString("urx_extra_singleoption_22");
				this.usr_extra_singleoption_23 = rs.getString("urx_extra_singleoption_23");
				this.usr_extra_singleoption_24 = rs.getString("urx_extra_singleoption_24");
				this.usr_extra_singleoption_25 = rs.getString("urx_extra_singleoption_25");
				this.usr_extra_singleoption_26 = rs.getString("urx_extra_singleoption_26");
				this.usr_extra_singleoption_27 = rs.getString("urx_extra_singleoption_27");
				this.usr_extra_singleoption_28 = rs.getString("urx_extra_singleoption_28");
				this.usr_extra_singleoption_29 = rs.getString("urx_extra_singleoption_29");
				this.usr_extra_singleoption_30 = rs.getString("urx_extra_singleoption_30");
				//user extension for multiple choice
				this.usr_extra_multipleoption_31 = rs.getString("urx_extra_multipleoption_31");
				this.usr_extra_multipleoption_32 = rs.getString("urx_extra_multipleoption_32");
				this.usr_extra_multipleoption_33 = rs.getString("urx_extra_multipleoption_33");
				this.usr_extra_multipleoption_34 = rs.getString("urx_extra_multipleoption_34");
				this.usr_extra_multipleoption_35 = rs.getString("urx_extra_multipleoption_35");
				this.usr_extra_multipleoption_36 = rs.getString("urx_extra_multipleoption_36");
				this.usr_extra_multipleoption_37 = rs.getString("urx_extra_multipleoption_37");
				this.usr_extra_multipleoption_38 = rs.getString("urx_extra_multipleoption_38");
				this.usr_extra_multipleoption_39 = rs.getString("urx_extra_multipleoption_39");
				this.usr_extra_multipleoption_40 = rs.getString("urx_extra_multipleoption_40");
			}
			rs.close();
			pstmt.close();
		}catch (SQLException e) {
		throw new qdbException("SQL Error: " + e.getMessage());
   		}
	}

    public void get(Connection con, String _usr_id)
        throws qdbException
    {
       try {

            PreparedStatement stmt = con.prepareStatement(
            "SELECT " + SQL_USR_COL
            + " FROM RegUser, ReguserExtension where usr_id = ?  and usr_ent_id = urx_usr_ent_id ");
        stmt.setString(1, _usr_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
        {
            getFromRS(con, rs);
        }
        else
        {
        	stmt.close();
            throw new qdbException("Failed to get user." + usr_id);
        }

        stmt.close();

       } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }

    public void getByUsrSteUsrId(Connection con, long usr_ste_ent_id, String usr_ste_usr_id) throws qdbException {
		try {

			PreparedStatement stmt = con.prepareStatement("SELECT " + SQL_USR_COL + " FROM RegUser, ReguserExtension where usr_ent_id = urx_usr_ent_id and usr_ste_ent_id = ? and usr_ste_usr_id = ? and (usr_status = 'OK' or usr_status = 'SYS') ");
			stmt.setLong(1, usr_ste_ent_id);
			stmt.setString(2, usr_ste_usr_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				getFromRS(con, rs);
			}
			stmt.close();
		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
	}

    private void getFromRS(Connection con, ResultSet rs) throws SQLException {
        usr_id              = rs.getString("usr_id");
        nickname=rs.getString("usr_nickname");
        usr_ste_usr_id      = rs.getString("usr_ste_usr_id");
        usr_ent_id          = rs.getLong("usr_ent_id");
        usr_ste_ent_id      = rs.getLong("usr_ste_ent_id");
        usr_pwd             = rs.getString("usr_pwd");
        usr_email           = rs.getString("usr_email");
        usr_email_2           = rs.getString("usr_email_2");
        usr_full_name_bil   = rs.getString("usr_full_name_bil");
        usr_initial_name_bil  = rs.getString("usr_initial_name_bil");
        usr_last_name_bil   = rs.getString("usr_last_name_bil");
        usr_first_name_bil  = rs.getString("usr_first_name_bil");
        usr_display_bil     = rs.getString("usr_display_bil");
        usr_nickname     = rs.getString("usr_nickname");
        usr_gender          = rs.getString("usr_gender");

      //s String temp=rs.getTimestamp("usr_bday").toString();
        usr_bday            = rs.getTimestamp("usr_bday");
//            usr_bplace_bil      = rs.getString("usr_bplace_bil");
        usr_hkid            = rs.getString("usr_hkid");
        usr_other_id_no     = rs.getString("usr_other_id_no");
        usr_other_id_type   = rs.getString("usr_other_id_type");
        usr_tel_1           = rs.getString("usr_tel_1");
        usr_tel_2           = rs.getString("usr_tel_2");
        usr_fax_1           = rs.getString("usr_fax_1");
        usr_country_bil     = rs.getString("usr_country_bil");
        usr_postal_code_bil = rs.getString("usr_postal_code_bil");
        usr_state_bil       = rs.getString("usr_state_bil");
//            usr_city_bil        = rs.getString("usr_city_bil");
        usr_address_bil     = rs.getString("usr_address_bil");
//            usr_occupation_bil  = rs.getString("usr_occupation_bil");
//            usr_income_level    = rs.getString("usr_income_level");
//            usr_edu_role        = rs.getString("usr_edu_role");
//            usr_edu_level       = rs.getString("usr_edu_level");
//            usr_school_bil      = rs.getString("usr_school_bil");
        usr_class           = rs.getString("usr_class");
        usr_class_number    = rs.getString("usr_class_number");
        usr_signup_date     = rs.getTimestamp("usr_signup_date");
        usr_last_login_date = rs.getTimestamp("usr_last_login_date");
//            usr_special_date_1  = rs.getTimestamp("usr_special_date_1");
        usr_status          = rs.getString("usr_status");
        usr_upd_date        = rs.getTimestamp("usr_upd_date");
        usr_source          = rs.getString("usr_source");
        usr_other_id        = rs.getString("usr_other_id_no");
        usr_other_id_type   = rs.getString("usr_other_id_type");
        usr_extra_1         = rs.getString("usr_extra_1");
        usr_extra_2         = rs.getString("usr_extra_2");
        usr_extra_3         = rs.getString("usr_extra_3");
        usr_extra_4         = rs.getString("usr_extra_4");
        usr_extra_5         = rs.getString("usr_extra_5");
        usr_extra_6         = rs.getString("usr_extra_6");
        usr_extra_7         = rs.getString("usr_extra_7");
        usr_extra_8         = rs.getString("usr_extra_8");
        usr_extra_9         = rs.getString("usr_extra_9");
        usr_extra_10        = rs.getString("usr_extra_10");
        urx_extra_datetime_11        = rs.getString("urx_extra_datetime_11");
        urx_extra_datetime_12        = rs.getString("urx_extra_datetime_12");
        urx_extra_datetime_13        = rs.getString("urx_extra_datetime_13");
        urx_extra_datetime_14        = rs.getString("urx_extra_datetime_14");
        urx_extra_datetime_15        = rs.getString("urx_extra_datetime_15");
        urx_extra_datetime_16        = rs.getString("urx_extra_datetime_16");
        urx_extra_datetime_17        = rs.getString("urx_extra_datetime_17");
        urx_extra_datetime_18        = rs.getString("urx_extra_datetime_18");
        urx_extra_datetime_19        = rs.getString("urx_extra_datetime_19");
        urx_extra_datetime_20        = rs.getString("urx_extra_datetime_20");
        urx_extra_singleoption_21        = rs.getString("urx_extra_singleoption_21");
        urx_extra_singleoption_22        = rs.getString("urx_extra_singleoption_22");
        urx_extra_singleoption_23        = rs.getString("urx_extra_singleoption_23");
        urx_extra_singleoption_24        = rs.getString("urx_extra_singleoption_24");
        urx_extra_singleoption_25        = rs.getString("urx_extra_singleoption_25");
        urx_extra_singleoption_26        = rs.getString("urx_extra_singleoption_26");
        urx_extra_singleoption_27        = rs.getString("urx_extra_singleoption_27");
        urx_extra_singleoption_28        = rs.getString("urx_extra_singleoption_28");
        urx_extra_singleoption_29        = rs.getString("urx_extra_singleoption_29");
        urx_extra_singleoption_30        = rs.getString("urx_extra_singleoption_30");
        urx_extra_multipleoption_31        = rs.getString("urx_extra_multipleoption_31");
        urx_extra_multipleoption_32        = rs.getString("urx_extra_multipleoption_32");
        urx_extra_multipleoption_33        = rs.getString("urx_extra_multipleoption_33");
        urx_extra_multipleoption_34        = rs.getString("urx_extra_multipleoption_34");
        urx_extra_multipleoption_35        = rs.getString("urx_extra_multipleoption_35");
        urx_extra_multipleoption_36        = rs.getString("urx_extra_multipleoption_36");
        urx_extra_multipleoption_37        = rs.getString("urx_extra_multipleoption_37");
        urx_extra_multipleoption_38        = rs.getString("urx_extra_multipleoption_38");
        urx_extra_multipleoption_39        = rs.getString("urx_extra_multipleoption_39");
        urx_extra_multipleoption_40        = rs.getString("urx_extra_multipleoption_40");
        usr_cost_center     = rs.getString("usr_cost_center");
        usr_approve_usr_id  = rs.getString("usr_approve_usr_id");
        usr_approve_timestamp   = rs.getTimestamp("usr_approve_timestamp");
        usr_approve_reason  = rs.getString("usr_approve_reason");
        usr_syn_rol_ind     = rs.getBoolean("usr_syn_rol_ind");
        usr_not_syn_gpm_type= rs.getString("usr_not_syn_gpm_type");
        usr_remark_xml      = cwSQL.getClobValue(rs, "usr_remark_xml");
        usr_job_title = rs.getString("usr_job_title");
        usr_join_datetime = rs.getTimestamp("usr_join_datetime");
        usr_app_approval_usg_ent_id = rs.getLong("usr_app_approval_usg_ent_id");
        this.urx_extra_41=rs.getString("urx_extra_41");
        this.urx_extra_42=rs.getString("urx_extra_42");
        this.urx_extra_43=rs.getString("urx_extra_43");
        this.urx_extra_44=rs.getString("urx_extra_44");
        this.urx_extra_45=rs.getString("urx_extra_45");

        return;
    }
// deprecated
/*
    public void regUser(Connection con, long parent_id, long site_id, loginProfile prof)
        throws qdbErrMessage, qdbException, cwException, cwSysMessage
    {
        try {
            usr_ste_usr_id = usr_id;

             // Check whether the site user id exist
            if (checkSiteUsrIdExist(con, site_id))
                throw new qdbErrMessage("USR001", usr_id);

            // Registration is open for learners ONLY
            // change to multiple roles

            usr_roles = new String[] {AccessControlWZB.getDefaultRoleBySite(con, site_id)};
//            usr_role_starts = new Timestamp[] {Timestamp.valueOf(cwUtils.MIN_TIMESTAMP)};
//            usr_role_ends = new Timestamp[]  {Timestamp.valueOf(cwUtils.MAX_TIMESTAMP)};
            usr_roles_starts = new String[] {cwUtils.MIN_TIMESTAMP};
            usr_roles_ends = new String[]  {cwUtils.MAX_TIMESTAMP};

            // If no group specified.
            long group_id = 0;
            if (parent_id == 0) {
                // Add the user to the organization
                // group id  : new folder for each month e.g. 2001-10
                // Format the current time.

                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM");
                String groupname = formatter.format(new Date(System.currentTimeMillis()));
                dbUserGroup dbusg = new dbUserGroup();
                dbusg.usg_display_bil=groupname;
                dbusg.usg_ent_id_root = site_id;
                group_id = dbusg.checkAndCreateGroup(con, site_id, prof.usr_id);
            }else {
                group_id = parent_id;
            }

            ins(con, group_id, site_id, null, null, prof.usr_id);

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }
*/

    public String aff_login(Connection con, loginProfile prof, acSite site, String host, String referer, boolean createNew, boolean allowSys, String mode, WizbiniLoader wizbini)
        throws qdbException, qdbErrMessage, cwException, cwSysMessage
    {
        try {
            String code = new String();

            // Get the site by site id
            site.get(con);
            //latest updated(christ):add
            if(site.ste_id==null){
                return (CODE_SITE_EXPIRED);
            }
            // Check if Request.getRemoteHost() and Request.getHeader("REFERER") contains
            // the string stored in acSite.ste_domain which stored a list of valid sites
            // separaotr by the "[|]" delimiter
            boolean bValid = false;
            if (site.ste_domain != null && !site.ste_domain.trim().equals("")) {
                String[] validDomains = dbUtils.split(site.ste_domain, acSite.DOMAIN_SEPARATOR);
                for (int i=0; i<validDomains.length; i++) {
                    if (host != null && host.toLowerCase().indexOf(validDomains[i].toLowerCase()) >= 0) {
                        bValid = true;
                    }
                    if (referer != null && referer.toLowerCase().indexOf(validDomains[i].toLowerCase()) >= 0) {
                        bValid = true;
                    }
                }
            }

            if (!bValid) {
                return (CODE_NOT_TRUSTED_SITE);
            }

            if (mode==null || (!mode.equalsIgnoreCase(AFF_LOGIN_MODE_ADVANCED) && !mode.equalsIgnoreCase(AFF_LOGIN_MODE_BASIC))){
                return CODE_INVALID_LOGIN_MODE;
            }

            boolean isOldUser = checkSiteUsrIdExist(con, site.ste_ent_id);
            String usrInfoErrorCode;
            if (mode.equalsIgnoreCase(AFF_LOGIN_MODE_ADVANCED)){
                usrInfoErrorCode = transformUserInfo4AffLogin(con, !isOldUser, site.ste_ent_id, wizbini);
                if (usrInfoErrorCode != null)
                    return usrInfoErrorCode;
            }else{
                // basic mode
                if (!isOldUser){
                    return CODE_USER_NOT_EXIST;
                }
            }

            if (!isOldUser && mode.equalsIgnoreCase(AFF_LOGIN_MODE_ADVANCED)) {
                if (createNew){
                	CommonLog.debug("Create new user > " + usr_ste_usr_id);
                    ins(con, site.ste_ent_id, null, null, this.usr_ste_usr_id);
                    code = CODE_CREATE_AND_LOGIN;
                }else{
                    return CODE_USER_NOT_EXIST;
                }
            }
            //latest updated(christ):add; line count:5
            if(isOldUser && mode.equalsIgnoreCase(AFF_LOGIN_MODE_ADVANCED)){
                long temp = this.usr_ent_id;
                this.usr_ent_id = getEntId(con,usr_ste_usr_id,site.ste_ent_id);
                assignUserRoles(con);
                this.usr_ent_id = temp;
                this.usr_roles = null;//to avoid assignUserRoles:assignUserRoles(con) again
            }
            String loginStatus = dbRegUser.login(con, prof, usr_ste_usr_id, usr_pwd, login_role, site, dbEntityLoginHistory.TRUSTED_LOGIN, allowSys, wizbini, null, null, null);
            if (loginStatus.equals(CODE_LOGIN_SUCCESS)) {
                usr_ent_id = prof.usr_ent_id;
                // Update User record :
                // If the login API contains user's info to update

                if (isOldUser && mode.equalsIgnoreCase(AFF_LOGIN_MODE_ADVANCED)) {
                	//latest updated(christ):add
                    prof.usr_display_bil = this.usr_display_bil;
                    CommonLog.debug("Update old user > " + usr_ste_usr_id);
                    upd(con, prof.usr_id, true);
                    code = CODE_LOGIN_AND_UPDATE;
                }
                // user is not created and user information is not updated
                if (code ==null || code.length() ==0)
                    code = CODE_LOGIN_SUCCESS;
            }else {
                code = loginStatus;
            }

            return code;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // backward compatbility
    public static String login(Connection con, loginProfile prof, String usrId, String passwd, acSite site, boolean allowSys, WizbiniLoader wizbini)
        throws qdbException, cwException
    {
        String code = login(con, prof, usrId, passwd, null, site, allowSys, wizbini);
        return code;
    }

    public static String login(Connection con, loginProfile prof, String usrId, String passwd, String loginRole, acSite site, boolean allowSys, WizbiniLoader wizbini)
        throws qdbException, cwException
    {
      try {
            String code = new String();
            site.get(con);
            site.ste_trusted = false;

            code = dbRegUser.do_login(con, prof, usrId, passwd, loginRole,  site, dbEntityLoginHistory.NORMAL_LOGIN, allowSys, wizbini);

/*
            // login success
            // this code segment is for tracking on login
            // remark this to improve performance

            // Dennis: unremark this as I really need this tracking
            if(code.equals(CODE_LOGIN_SUCCESS)) {
                // normal user
                updLoginStatus(con, usrId, site.ste_ent_id, true, dbEntityLoginHistory.NORMAL_LOGIN);
            } else if(code.equals(CODE_INACTIVE_LOGIN)) {
                ; //skip and do nothing for the login status
            } else {
                updLoginStatus(con, usrId, site.ste_ent_id, false, dbEntityLoginHistory.NORMAL_LOGIN);
            }
*/
            con.commit();

            return code;

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

  }
    
    public static String mark_login(Connection con, loginProfile prof, String usrId, String passwd, acSite site, boolean allowSys, WizbiniLoader wizbini)
            throws qdbException, cwException, SQLException, cwSysMessage
        {
            String code = dbRegUser.login(con, prof, usrId, passwd, "SADM", site, dbEntityLoginHistory.TRUSTED_LOGIN, allowSys, wizbini, null, null, null);
            
            return code;
        }
    
    // login to ldap server, if fail, ordinary login
    public static String ldap_login(Connection con, loginProfile prof, String usrId, String passwd, String loginRole, acSite site, boolean allowSys, WizbiniLoader wizbini, String label_lan)
        throws qdbException, cwException, cwSysMessage 
    {
      try {
            String code = new String();
            site.get(con);
            if (site.ste_ldap_host!=null){
                // if success in ldap, no need to check password in lms again
                String[] dn = cwUtils.splitToString(site.ste_ldap_dn, acSite.DN_SEPARATOR);
                boolean login_success = false;
                for (int i=0; i<dn.length && !login_success; i++){
                    login_success = auth2ldap(usrId, passwd, site.ste_ldap_host, dn[i]);
                }
                site.ste_trusted = login_success;
            }
                                
            code = login(con, prof, usrId, passwd, loginRole,  site, dbEntityLoginHistory.NORMAL_LOGIN, allowSys, wizbini, label_lan, null, null);
            con.commit();

            return code;

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }

  }

    // Guest login , no password requried
    public static String guest_login(Connection con, loginProfile prof, acSite site, boolean allowSys, WizbiniLoader wizbini)
        throws qdbException, cwException, SQLException, cwSysMessage
    {
        String code = "";
        site.ste_trusted = true;
        String guest_ste_usr_id = getSiteGuestId(con, site.ste_ent_id);
        if (guest_ste_usr_id == null) {
            throw new cwException("Failed to login.");
        }
        code = login(con, prof, guest_ste_usr_id, null, null, site, dbEntityLoginHistory.TRUSTED_LOGIN, allowSys, wizbini, null, null, null);
        return code;
    }

  private static String getCurLoginStatusAsXML(Timestamp curTime, String method) throws SQLException {
    StringBuffer xmlBuf = new StringBuffer(512);
    xmlBuf.append("<cur_login_status timestamp=\"").append(curTime)
          .append("\" success=\"").append(true)
          .append("\" method=\"").append(method).append("\"/>");
    return xmlBuf.toString();
  }

  private static String do_login(Connection con, loginProfile prof,
                String usrId, String passwd, String loginRole, acSite site,
                                 String method, boolean allowSys, WizbiniLoader wizbini)
    throws qdbException, SQLException
  {
    boolean bUnlockAccount = false;
    boolean bAddLoginTrial = false;
    String code = null;

    Timestamp curTime = cwSQL.getTime(con);
    try {
        String SQL = " SELECT usr_id , usr_pwd, usr_ste_usr_id, usr_ent_id, usr_display_bil "
                    + "    ,usr_last_login_date, usr_last_login_role "
                    + "    ,usr_login_trial, usr_login_status, ste_name, ste_id "
                    + "    ,ste_eff_start_date, ste_eff_end_date, ste_max_login_trial, usr_status "
                    + " FROM Reguser, acSite "
                    + " WHERE usr_ste_usr_id = ? "
                    + " AND usr_ste_ent_id = ? "
                    + " AND usr_ste_ent_id = ste_ent_id "
                    + " AND ste_status = ? "
                    + " AND (usr_status = ? OR usr_status = ? ";
        if(allowSys) {
            SQL += " OR usr_status = ? ";
        }
        SQL += ")";

        PreparedStatement stmt1 = con.prepareStatement(SQL);
        int index = 1;
        stmt1.setString(index++, usrId);
        stmt1.setLong(index++, site.ste_ent_id);
        stmt1.setString(index++, acSite.STATUS_OK);
        stmt1.setString(index++, USR_STATUS_OK);
        stmt1.setString(index++, USR_STATUS_INACTIVE);
        if(allowSys) {
            stmt1.setString(index++, USR_STATUS_SYS);
        }
        ResultSet rs = stmt1.executeQuery();
        if(rs.next())
        {
            if(rs.getString("usr_status").equals(USR_STATUS_INACTIVE)) {
                code = CODE_INACTIVE_LOGIN;
                stmt1.close();
                return code;
            }

            prof.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            prof.usr_pwd = rs.getString("usr_pwd");
            prof.usr_id = rs.getString("usr_id");
            prof.usr_ent_id = rs.getLong("usr_ent_id");

            int trial_cnt = rs.getInt("usr_login_trial");
            int trial_allowed = ((UserManagement)wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getAccountSuspension().getMaxTrial();
            boolean checkTrial = ((UserManagement)wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getAccountSuspension().isActive();
            /*
            if(trial_allowed == 0) {
                checkTrial = false;
            }
            */
            boolean accountLocked = false;

            if (checkTrial && trial_cnt >= trial_allowed) {
                accountLocked = true;
            }


            // The number of login is greater than the site specified
            // No limit for trusted login
            if (!site.ste_trusted && accountLocked) {
                    prof.usr_id = null;
                    code = CODE_ATTEMPT_EXCEED_LIMIT;
                    stmt1.close();
                    return code;
            }

            // username & password must be case sensitive
            boolean valid = false;
            if (site.ste_trusted) {
                if (prof.usr_ste_usr_id.equals(usrId))
                    valid = true;
            }else {
                //latest update(christ):add
                if(passwd!=null){
				//perform encryption
					passwd = encrypt(passwd, new StringBuffer(prof.usr_ste_usr_id).reverse().toString());
				//end encryption
                }
                if (passwd != null &&
                    prof.usr_pwd != null &&
                    prof.usr_ste_usr_id.equals(usrId) &&
                    prof.usr_pwd.equals(passwd)) {
                    valid = true;
                    // reset the login trial if successful login
                    if(checkTrial && trial_cnt > 0) {
                        bUnlockAccount = true;
                    }
                } else if(checkTrial) {
                    // add to increment the login trail
                    bAddLoginTrial = true;
                }
            }

            // The site must not be expired
            boolean expiry = true;
            if (valid) {
                try {
                    String eff_start_date = rs.getString("ste_eff_start_date");
                    String eff_end_date = rs.getString("ste_eff_end_date");
                    if (cwEncode.checkValidKey(eff_start_date) &&
                        cwEncode.checkValidKey(eff_end_date)) {
                        Timestamp start_date = Timestamp.valueOf(cwEncode.decodeKey(eff_start_date));
                        Timestamp end_date = Timestamp.valueOf(cwEncode.decodeKey(eff_end_date));
                        if (curTime.after(start_date) && curTime.before(end_date)) {
                            expiry = false;
                        }
                    }
                }catch (Exception e) {
                    // do nothing
                }
            }

            if (!valid ) {
                prof.usr_id = null;
                // Last trial
                if (checkTrial && trial_cnt == trial_allowed -1 ) {
                    code = CODE_ATTEMPT_EXCEED_LIMIT;

		            qdbXMessage qdbXmsg = new qdbXMessage();
		            //long[] ent_id = {3,4};
		            AccessControlWZB accWzb = new AccessControlWZB();
		           
		            CommonLog.debug("prof.usr_ent_id = " + prof.usr_ent_id);
                    /*
                    // comment by kim, do not send suspend notify
		            qdbXmsg.sendAccSuspendNotify(con, ent_id, prof.usr_ent_id, wizbini.cfgSysSetupadv.getSkinHome());
		            */
                }else {
                    code = CODE_PWD_INVALID;
                }
                stmt1.close();
                return code;
            }else if (expiry) {
                prof.usr_id = null;
                code = CODE_SITE_EXPIRED;
                stmt1.close();
                return code;
            }

            prof.usr_id = rs.getString("usr_id");
            prof.usr_ent_id = rs.getLong("usr_ent_id");
            prof.usr_display_bil = rs.getString("usr_display_bil");
            prof.usr_last_login_date = rs.getTimestamp("usr_last_login_date");
            
          //根据上一次登陆判断是不是今天第一次登陆
			Calendar current = Calendar.getInstance();
			Calendar today = Calendar.getInstance();	//今天
			today.set(Calendar.YEAR, current.get(Calendar.YEAR));
			today.set(Calendar.MONTH, current.get(Calendar.MONTH));
			today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
			//  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
			today.set( Calendar.HOUR_OF_DAY, 0);
			today.set( Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			
			current.setTime(prof.usr_last_login_date);
			if(!current.after(today)){
				prof.show_alert_msg_type = FIRST_DAY_LOGIN;
			}
			//判断是否是当月第一天
			int isMonthFirstday = today.get(Calendar.DAY_OF_MONTH);
			if(isMonthFirstday == 1 && !current.after(today)){ //全等于1就是这个月的第一天
				prof.show_alert_msg_type += FIRST_MONTH_LOGIN;
			}
            
			if(rs.getString("usr_last_login_role") == null){//第一次登陆
				prof.show_alert_msg_type += FIRST_LOGIN;
			}
			
            prof.current_role = rs.getString("usr_last_login_role");
            prof.last_login_status = rs.getString("usr_login_status");
            prof.current_login_status_xml = getCurLoginStatusAsXML(curTime, method);
            prof.account_locked = accountLocked;
            prof.root_display = rs.getString("ste_name");
            prof.root_id = rs.getString("ste_id");
            prof.isLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);

            // get sys roles
            AccessControlWZB acl = new AccessControlWZB();
            String[] roles = acl.getUserRoles(con, prof.usr_ent_id);
            boolean role_exist = false;

            if(roles == null) {
            	stmt1.close();
                throw new qdbException("User has no role defined");
            }

            if (loginRole != null && loginRole.length() > 0) {
                // Using this role to login
                prof.current_role = loginRole;
            }

            if(prof.current_role != null) {
                for(int i=0;i<roles.length;i++) {
                    if(prof.current_role.equals(roles[i])) {
                        role_exist = true;
                        break;
                    }
                }
                prof.first_login = false;
            }else {
                // The user is first time to login
                prof.first_login = true;
            }

            if(!role_exist) {
                prof.current_role = roles[0];
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = prof.usr_ent_id;
                usr.updLastLoginRole(con, roles[0]);
                //do isLrnRole again.
                prof.isLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
            }
            if(prof.current_role != null) {
                String[] rol_array = dbUtils.getRoleNSkinNHomeURL(con, prof.current_role);
                String[] prefer_array = PsnPreference.getPreferenceByEntId(con, prof.usr_ent_id, prof.current_role, (Personalization)wizbini.cfgOrgPersonalization.get(prof.root_id), null, null);

                prof.current_role_xml = rol_array[0];
//                prof.current_role_skin_root = rol_array[1];
                prof.current_role_skin_root = prefer_array[0];
                prof.label_lan = prefer_array[1];
                prof.cur_lan = prof.getCurLan(prof.label_lan);
                prof.role_url_home = rol_array[1];
                prof.common_role_id = getCommnonRoleId(prof.current_role);
//                AcHomePage acHomepage = new AcHomePage(con);
//                acHomepage.getGrantedFunction(prof, wizbini.cfgTcEnabled);
            }

            // get root id
            //prof.root_ent_id = dbRegUser.getRootGpId(con, usrId);
            prof.root_ent_id = site.ste_ent_id;
            prof.login_date = curTime;

            prof.usrGroups = dbUserGroup.traceParentID(con, prof.usr_ent_id);

            dbUserGroup dbg = new dbUserGroup();
            dbg.usg_ent_id = prof.root_ent_id;
            dbg.get(con);
            prof.root_level = dbg.usg_level;
// Stanley: use the site name instead
//                prof.root_display  = dbg.usg_display_bil;
            prof.root_code      = dbg.usg_code;
            code = CODE_LOGIN_SUCCESS;
        } else {
            code = CODE_USER_NOT_EXIST;
        }
//System.out.println(" Login code : " + code);
		stmt1.close();
        return code;

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      } finally {
        if(code != null) {
            try {
                if(code.equals(CODE_LOGIN_SUCCESS)) {
                    // normal user
                    updLoginStatus(con, usrId, site.ste_ent_id, true, method, bUnlockAccount, bAddLoginTrial, curTime, null);
                } else if(code.equals(CODE_INACTIVE_LOGIN)) {
                    //skip and do nothing for the login status
                } else {
                    updLoginStatus(con, usrId, site.ste_ent_id, false, method, bUnlockAccount, bAddLoginTrial, curTime, null);
                }
            } catch(SQLException sqle) {
                throw new qdbException("SQL Error: " + sqle.getMessage());
            }
        }
    }
  }
    public long getEntId(Connection con)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            "SELECT usr_ent_id from RegUser where usr_id = ? " );
          stmt.setString(1, usr_id);
          ResultSet rs = stmt.executeQuery();

          long entId = 0;
          if(rs.next())
            entId =  rs.getLong("usr_ent_id");

          stmt.close();
          return entId;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static long getEntId(Connection con, String usrId)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            "SELECT usr_ent_id from RegUser where usr_id = ? " );
          stmt.setString(1, usrId);
          ResultSet rs = stmt.executeQuery();

          long entId = 0;
          if(rs.next())
            entId =  rs.getLong("usr_ent_id");

          stmt.close();
          return entId;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static long getEntId(Connection con, String steUsrId, long site_id)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            "SELECT distinct(usr_ent_id) from RegUser "
            + " WHERE usr_ste_usr_id = ? "
            + "   AND usr_ste_ent_id = ?  order by usr_ent_id desc");

          stmt.setString(1, steUsrId);
          stmt.setLong(2, site_id);
          ResultSet rs = stmt.executeQuery();

          long entId = 0;
          if(rs.next())
            entId =  rs.getLong("usr_ent_id");

          stmt.close();
          return entId;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static long getEntIdStatusOk(Connection con, String steUsrId,
			long site_id) throws qdbException {
		try {
			PreparedStatement stmt = con
					.prepareStatement("SELECT distinct(usr_ent_id) from RegUser "
							+ " WHERE usr_ste_usr_id = ? "
							+ "   AND usr_ste_ent_id = ? "
							+ "   AND usr_status = ? ");

			stmt.setString(1, steUsrId);
			stmt.setLong(2, site_id);
			stmt.setString(3, USR_STATUS_OK);
			ResultSet rs = stmt.executeQuery();

			long entId = 0;
			if (rs.next())
				entId = rs.getLong("usr_ent_id");

			stmt.close();
			return entId;

		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
	}

    public String getUserId(Connection con)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            "SELECT usr_id from RegUser where usr_ent_id = ? " );
          stmt.setLong(1, usr_ent_id);
          ResultSet rs = stmt.executeQuery();

          String usrId = null;
          if(rs.next())
            usrId =  rs.getString("usr_id");
          rs.close();
          stmt.close();
          return usrId;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static String getUserId(Connection con, int userEntId) throws SQLException {
        PreparedStatement stmt = con
                .prepareStatement("SELECT usr_id from RegUser where usr_ent_id = ? ");
        stmt.setLong(1, userEntId);
        ResultSet rs = stmt.executeQuery();
        String usrId = null;
        if (rs.next()) {
            usrId = rs.getString("usr_id");
        }
        stmt.close();
        return usrId;
    }

    public static Vector getUserId(Connection con, Vector ent_id_vec)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            "SELECT usr_id from RegUser where usr_ent_id in " + cwUtils.vector2list(ent_id_vec));
          ResultSet rs = stmt.executeQuery();

          Vector usr_id_vec = new Vector();
          while(rs.next()){
              usr_id_vec.add(rs.getString("usr_id"));
          }
          stmt.close();
          return usr_id_vec;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static long getRootGpId(Connection con, String usrId)
        throws qdbException
    {
        try {
          PreparedStatement stmt = con.prepareStatement(
            "SELECT usg_ent_id, usg_ent_id_root from UserGroup, EntityRelation, RegUser "
          + "  WHERE usg_ent_id         = ern_ancestor_ent_id "
          + "    AND ern_child_ent_id  = usr_ent_id "
          + "    AND usr_id             = ? "
          + "    AND ern_parent_ind = ? ");

          stmt.setString(1, usrId);
          stmt.setBoolean(2, true);
          ResultSet rs = stmt.executeQuery();
          long entIdroot = 0;
          if(rs.next())
          {
              entIdroot = rs.getLong("usg_ent_id_root");
              // The user is directly under the root entity
              if (entIdroot ==0)
                entIdroot = rs.getLong("usg_ent_id");


          }else {
          	 stmt.close();
             throw new qdbException("Failed to get root entity id.");
          }

          stmt.close();
          return  entIdroot;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // Self Registration : RETIRED
    public boolean reg(Connection con)
        throws qdbException, qdbErrMessage
    {
        return false;
        /*
         try {

            // assume connection is ready:
            PreparedStatement stmt = con.prepareStatement(
                " SELECT B.usg_ent_id FROM UserGroup A, UserGroup B "
                + " WHERE B.usg_ent_id_root = A.usg_ent_id "
                + "   AND A.usg_ent_id is null "
                + "   AND B.usg_role = ? ");

            stmt.setString(1, dbUserGroup.USG_ROLE_STUDENT );

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (ins(con,rs.getLong("B.usg_ent_id"))) {
                    con.commit();
                    return true;
                }else {
                    con.rollback();
                    return false;
                }
            }else {
                throw new qdbException("Failed to get user group information.");
            }

         } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
         }
         */
    }

    /**
    @deprecated
    */
    /*
    public void ins(Connection con, loginProfile prof, long group_id)
        throws qdbException, qdbErrMessage, cwException, cwSysMessage {

        ins(con, prof, group_id, null);
    }
    */
    public void ins(Connection con, loginProfile prof, String msg_subject)
        throws qdbException, qdbErrMessage, cwException, cwSysMessage
    {
         try {
            // assume connection is ready:
            //super.checkAdminRole(con,admin_id);

            usr_ste_usr_id = usr_id;

             // Check whether the site user id exist
            if (checkSiteUsrIdExist(con, prof.root_ent_id))
                throw new qdbErrMessage("USR001", usr_id);

            if(usr_roles == null) {
                throw new qdbException("invalid usr role");
            }

            this.upd_usr_id = prof.usr_id;
            ins(con, prof.root_ent_id, prof.usr_id, msg_subject, prof.usr_id);

            con.commit();
         } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
/*************************************************************************/
// ins(con, group_id, site_id)  is divided into ins(con, site_id) and then insGroup(con, group_id) by kim
/*
    private boolean ins(Connection con, long group_id, long site_id, String usr_id)
        throws qdbException, qdbErrMessage, cwException, cwSysMessage {

        return ins(con, group_id, site_id, null, null, usr_id);
    }
  */
    private boolean ins(Connection con, long site_id, String sender_usr_id, String msg_subject, String usr_id)
        throws qdbException, qdbErrMessage, cwException, cwSysMessage
    {
       
        boolean result = ins(con, site_id, sender_usr_id, msg_subject, true);
        if (result) {
            result = insGroup(con, usr_id);
        }
        return result;
    }

    // pls call insGroup afterwards
    /*
    public boolean ins(Connection con, long site_id)
        throws qdbException, qdbErrMessage
    {
        return ins(con, site_id, null, null);
    }
*/
    // pls call insGroup afterwards
    public  boolean ins(Connection con, long site_id, String sender_usr_id, String msg_subject, boolean encryptPwd)
        throws qdbException, qdbErrMessage,cwSysMessage, cwException
    {
        
        // Check max number of users exceed
        if (checkUserNumExceedLimit(con, site_id)) {
            throw new cwSysMessage(acSite.MSG_LIC_MAX_USER_EXCEED);
        }

        // assume connection is ready:
        try {
            ent_type = dbEntity.ENT_TYPE_USER;
            super.ins(con);
            usr_id = "s" + site_id + "u" + ent_id;
            usr_ent_id  = ent_id;
            usr_upd_date = ent_upd_date;
            usr_signup_date =  ent_upd_date;
            usr_last_login_date = ent_upd_date;
            if (usr_status==null){
                usr_status = USR_STATUS_OK;
            }
            usr_ste_ent_id = site_id;
            usr_login_trial = 0;
            if (usr_status.equals(USR_STATUS_OK)){
                usr_approve_timestamp = cwSQL.getTime(con);
                usr_approve_usr_id = this.upd_usr_id;
            }

            PreparedStatement stmt = con.prepareStatement(
            "INSERT INTO RegUser "
            + " ( usr_id "                        //1
            + " ,usr_ent_id "
            + " ,usr_pwd "
            + " ,usr_email "
            + " ,usr_email_2 "
            + " ,usr_full_name_bil "            //6
            + " ,usr_initial_name_bil "
            + " ,usr_last_name_bil "
            + " ,usr_first_name_bil "
            + " ,usr_display_bil "
            + " ,usr_gender "
            + " ,usr_bday "
//            + " ,usr_bplace_bil "               //13
            + " ,usr_hkid "
            + " ,usr_other_id_no "
            + " ,usr_other_id_type "
            + " ,usr_tel_1 "
            + " ,usr_tel_2 "                    //18
            + " ,usr_fax_1 "
            + " ,usr_country_bil "
            + " ,usr_postal_code_bil "
            + " ,usr_state_bil "
//            + " ,usr_city_bil "                 //23
            + " ,usr_address_bil "
//            + " ,usr_occupation_bil "
//            + " ,usr_income_level "
//            + " ,usr_edu_role "
//            + " ,usr_edu_level "                //28
//            + " ,usr_school_bil "
            + " ,usr_class "
            + " ,usr_class_number "
            + " ,usr_signup_date "
            + " ,usr_last_login_date "          //33
//            + " ,usr_special_date_1 "
            + " ,usr_status "
            + " ,usr_upd_date "
            + " ,usr_ste_ent_id "
            + " ,usr_ste_usr_id "
            + " ,usr_extra_1 "
            + " ,usr_extra_2 "                  //40
            + " ,usr_extra_3 "
            + " ,usr_extra_4 "
            + " ,usr_extra_5 "
            + " ,usr_extra_6 "                  //44
            + " ,usr_extra_7 "
            + " ,usr_extra_8 "
            + " ,usr_extra_9 "
            + " ,usr_extra_10 "                  //48
            + " ,usr_approve_timestamp "
            + " ,usr_approve_usr_id "
            + " ,usr_approve_reason "
            + " ,usr_cost_center "
            + " ,usr_login_trial "
            + " ,usr_pwd_need_change_ind "
            + " ,usr_pwd_upd_timestamp "
            + " ,usr_syn_rol_ind "
            + " ,usr_not_syn_gpm_type "
            + " ,usr_job_title "
            + " ,usr_join_datetime "
            + " ,usr_app_approval_usg_ent_id "
            + " ,usr_source,usr_nickname "
            + " ) VALUES "
            + " ( ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, "
            + "   ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

            int col = 1;
            stmt.setString(col++,usr_id);
            stmt.setLong(col++,usr_ent_id);
            // perform encryption
            //latest updated(Christ):add "&& usr_pwd!=null"
            if (encryptPwd && usr_pwd!=null){
//                System.out.println("user ins, encrypting");
                usr_pwd = encrypt(usr_pwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
            }else{
//                System.out.println("user ins, skip encrypt, pwd:" + usr_pwd);
            }
            // end encryption
            stmt.setString(col++,usr_pwd);
            stmt.setString(col++,usr_email);
            stmt.setString(col++,usr_email_2);
            stmt.setString(col++,usr_full_name_bil);
            stmt.setString(col++,usr_initial_name_bil);
            stmt.setString(col++,usr_last_name_bil);
            stmt.setString(col++,usr_first_name_bil);
            stmt.setString(col++,usr_display_bil);
            stmt.setString(col++,usr_gender);
            stmt.setTimestamp(col++,usr_bday);
//            stmt.setString(col++,usr_bplace_bil);
            stmt.setString(col++,usr_hkid);
            stmt.setString(col++,usr_other_id_no);
            stmt.setString(col++,usr_other_id_type);
            stmt.setString(col++,usr_tel_1);
            stmt.setString(col++,usr_tel_2);
            stmt.setString(col++,usr_fax_1);
            stmt.setString(col++,usr_country_bil);
            stmt.setString(col++,usr_postal_code_bil);
            stmt.setString(col++,usr_state_bil);
//            stmt.setString(col++,usr_city_bil);
            stmt.setString(col++,usr_address_bil);
//            stmt.setString(col++,usr_occupation_bil);
//            stmt.setString(col++,usr_income_level);
//            stmt.setString(col++,usr_edu_role);
//            stmt.setString(col++,usr_edu_level);
//            stmt.setString(col++,usr_school_bil);
            stmt.setString(col++,usr_class);
            stmt.setString(col++,usr_class_number);
            stmt.setTimestamp(col++,usr_signup_date);
            stmt.setTimestamp(col++,usr_last_login_date);
//            stmt.setTimestamp(col++,usr_special_date_1);
            stmt.setString(col++,usr_status);
            stmt.setTimestamp(col++,usr_upd_date);
            stmt.setLong(col++, usr_ste_ent_id);
            stmt.setString(col++, usr_ste_usr_id);
            stmt.setString(col++, usr_extra_1);
            stmt.setString(col++, usr_extra_2);
            stmt.setString(col++, usr_extra_3);
            stmt.setString(col++, usr_extra_4);
            stmt.setString(col++, usr_extra_5);
            stmt.setString(col++, usr_extra_6);
            stmt.setString(col++, usr_extra_7);
            stmt.setString(col++, usr_extra_8);
            stmt.setString(col++, usr_extra_9);
            stmt.setString(col++, usr_extra_10);
            stmt.setTimestamp(col++, usr_approve_timestamp);
            stmt.setString(col++, usr_approve_usr_id);
            stmt.setString(col++, usr_approve_reason);
            stmt.setString(col++, usr_cost_center);
            stmt.setInt(col++, usr_login_trial);
            stmt.setBoolean(col++, usr_pwd_need_change_ind);
            stmt.setTimestamp(col++, usr_upd_date);
            stmt.setBoolean(col++, usr_syn_rol_ind);
            stmt.setString(col++, usr_not_syn_gpm_type);
            stmt.setString(col++, usr_job_title );
            stmt.setTimestamp(col++, usr_join_datetime );
            if(usr_app_approval_usg_ent_id==0) {
                stmt.setNull(col++, java.sql.Types.INTEGER);
            } else {
                stmt.setLong(col++, usr_app_approval_usg_ent_id );
            }
            stmt.setString(col++, usr_source);
            stmt.setString(col++, usr_nickname);


            // insert user record
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                // insert fails, rollback
                con.rollback();
                throw new qdbException("Failed to add new user.\n");
            }
            insExt(con);
            //change to multiple role
            if(usr_roles != null) {
                if (usr_roles_starts == null){
                    usr_roles_starts = new String[usr_roles.length];
                    for (int i=0; i<usr_roles_starts.length; i++){
                        usr_roles_starts[i] = cwUtils.MIN_TIMESTAMP;
                    }
                }
                if (usr_roles_ends == null){
                    usr_roles_ends = new String[usr_roles.length];
                    for (int i=0; i<usr_roles_ends.length; i++){
                        usr_roles_ends[i] = cwUtils.MAX_TIMESTAMP;
                    }
                }
                if (usr_roles.length != usr_roles_starts.length){
                    throw new qdbException("invalid user roles start date");
                }
                if (usr_roles.length != usr_roles_ends.length){
                    throw new qdbException("invalid user roles end date");
                }

                AccessControlWZB acl = new AccessControlWZB();
                for(int i=0; i<usr_roles.length; i++) {
                    acl.assignUser2Role(con, usr_ent_id, usr_roles[i], dbUtils.convertStartDate(usr_roles_starts[i]), dbUtils.convertEndDate(usr_roles_ends[i]));
                }
            }
            /*
            AccessControlWZB acl = new AccessControlWZB();
            acl.assignUser2Role(con, usr_ent_id, usr_role);
            */

            if(msg_subject!=null && sender_usr_id!=null) {
                try {
                    //send mail
                    sendMailToNewUser(con, sender_usr_id, msg_subject);
                } catch(SQLException e) {
                    throw new qdbException (e.getMessage());
                } catch(cwException cwe) {
                    throw new qdbException (cwe.getMessage());
                }
            }

            return true;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    private boolean insGroup(Connection con, String usr_id)
    throws qdbException, qdbErrMessage{
        try{
            //insert user roles
            assignUserRoles(con);
            //insert group member relation
            saveEntityRelation(con, usr_attribute_ent_ids,
                            usr_attribute_relation_types, false, usr_id);
            //insert my sub-ordinates
            saveRoleTargetEntity(con, rol_target_ext_ids, rol_target_ent_groups,
                                    rol_target_starts, rol_target_ends, usr_id, false);

            saveSuperviseTarget(con, direct_supervisor_ent_ids, direct_supervisor_starts, direct_supervisor_ends, DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE, usr_id);
            saveSuperviseTarget(con, supervise_target_ent_ids, supervise_target_starts, supervise_target_ends, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE, usr_id);

            //insert my approvers
            saveMyApprover(con, appr_rol_ext_ids, appr_ent_ids, usr_id, false);

            return true;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

/*************************************************************************/
    public void assignUserRoles(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {

            assignUserRoles(con);

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return;
    }

    public void assignUserRoles(Connection con) throws SQLException, qdbException {
        //update user roles
        AccessControlWZB acl = new AccessControlWZB();
        if(usr_roles != null) {
            DbRoleTargetEntity rolTarget = new DbRoleTargetEntity();
            rolTarget.rte_usr_ent_id = usr_ent_id;
            rolTarget.del(con);

            acl.rmUserRoles(con, usr_ent_id);
            if (usr_roles_starts == null){
                usr_roles_starts = new String[usr_roles.length];
                for (int i=0; i<usr_roles_starts.length; i++){
                    usr_roles_starts[i] = cwUtils.MIN_TIMESTAMP;
                }
            }
            if (usr_roles_ends == null){
                usr_roles_ends = new String[usr_roles.length];
                for (int i=0; i<usr_roles_ends.length; i++){
                    usr_roles_ends[i] = cwUtils.MAX_TIMESTAMP;
                }
            }
            if (usr_roles.length != usr_roles_starts.length){
                throw new qdbException("invalid user roles start date");
            }
            if (usr_roles.length != usr_roles_ends.length){
                throw new qdbException("invalid user roles end date");
            }
            for(int i=0; i<usr_roles.length; i++) {
                acl.assignUser2Role(con, usr_ent_id, usr_roles[i], dbUtils.convertStartDate(usr_roles_starts[i]), dbUtils.convertEndDate(usr_roles_ends[i]));
            }
        }
        return;
    }

    public void upd(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
            super.checkTimeStamp(con);
            this.upd_usr_id = prof.usr_id;
            upd(con, this.upd_usr_id, true);
            con.commit();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return;
    }

    public  void upd(Connection con, String usr_id, boolean encryptPwd)
        throws qdbException, qdbErrMessage
    {
        try {
            AccessControlWZB acl = new AccessControlWZB();

            ent_id = usr_ent_id;
            ent_upd_date = usr_upd_date;

            // update the timestamp of the entity
            super.upd(con);
            usr_upd_date = ent_upd_date;

            PreparedStatement stmt = con.prepareStatement(
                "UPDATE RegUser SET "
                + " usr_pwd = ? "
                + " ,usr_email = ? "
                + " ,usr_email_2 = ? "
                + " ,usr_full_name_bil= ?  "
                + " ,usr_initial_name_bil= ?  "
                + " ,usr_last_name_bil= ?  "
                + " ,usr_first_name_bil = ? "
                + " ,usr_display_bil= ?  "              //5
                + " ,usr_gender = ? "
                + " ,usr_bday = ? "
//                + " ,usr_bplace_bil = ? "               //8
                + " ,usr_hkid = ? "
                + " ,usr_other_id_no = ? "
                + " ,usr_other_id_type = ? "
                + " ,usr_tel_1 = ? "
                + " ,usr_tel_2 = ? "                    //13
                + " ,usr_fax_1 = ? "
                + " ,usr_country_bil = ? "
                + " ,usr_postal_code_bil = ? "
                + " ,usr_state_bil = ? "
//                + " ,usr_city_bil = ? "                 //18
                + " ,usr_address_bil = ? "
//                + " ,usr_occupation_bil = ? "
//                + " ,usr_income_level = ? "
//                + " ,usr_edu_role = ? "
//                + " ,usr_edu_level = ? "                //23
//                + " ,usr_school_bil = ? "
                + " ,usr_class = ? "
                + " ,usr_class_number = ? "
           //     + " ,usr_signup_date = ? "
           //     + " ,usr_last_login_date = ? "
//                + " ,usr_special_date_1 = ? "
           //     + " ,usr_status = ? "
                + " ,usr_upd_date = ? "                 //28
                + " ,usr_source = ? "
                + " ,usr_extra_1 = ? "
                + " ,usr_extra_2 = ? "
                + " ,usr_extra_3 = ? "
                + " ,usr_extra_4 = ? "
                + " ,usr_extra_5 = ? "
                + " ,usr_extra_6 = ? "
                + " ,usr_extra_7 = ? "
                + " ,usr_extra_8 = ? "
                + " ,usr_extra_9 = ? "
                + " ,usr_extra_10 = ? "
                + " ,usr_approve_timestamp = ? "
                + " ,usr_approve_usr_id = ? "
                + " ,usr_approve_reason = ? "
                + " ,usr_cost_center = ? "
                + " ,usr_syn_rol_ind = ? "
                + " ,usr_not_syn_gpm_type = ? "
                + " ,usr_job_title = ? "
                + " ,usr_join_datetime = ? "
                + " ,usr_app_approval_usg_ent_id = ? "
                + " ,usr_nickname  = ? "
                + " WHERE usr_ent_id = ? ") ;

            int col = 1;
            // perform encryption
            if((usr_id == null || usr_id.equals("")) ||
               (usr_ste_usr_id == null || usr_ste_usr_id.equals(""))) {
                get(con);
            }
            //latest updated(christ):update
            if (encryptPwd && usr_pwd!=null){
            //System.out.println("user upd, encrypting");
                usr_pwd = encrypt(usr_pwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
            }else{
//                System.out.println("user upd, skip encrypt, pwd:" + usr_pwd);
            }
            // end encryption
            stmt.setString(col++,usr_pwd);
            stmt.setString(col++,usr_email);
            stmt.setString(col++,usr_email_2);
            stmt.setString(col++,usr_full_name_bil);
            stmt.setString(col++,usr_initial_name_bil);

            stmt.setString(col++,usr_last_name_bil);
            stmt.setString(col++,usr_first_name_bil);
            stmt.setString(col++,usr_display_bil);
            stmt.setString(col++,usr_gender);
            stmt.setTimestamp(col++,usr_bday);
//            stmt.setString(col++,usr_bplace_bil);
            stmt.setString(col++,usr_hkid);
            stmt.setString(col++,usr_other_id_no);
            stmt.setString(col++,usr_other_id_type);
            stmt.setString(col++,usr_tel_1);
            stmt.setString(col++,usr_tel_2);
            stmt.setString(col++,usr_fax_1);
            stmt.setString(col++,usr_country_bil);
            stmt.setString(col++,usr_postal_code_bil);
            stmt.setString(col++,usr_state_bil);
//            stmt.setString(col++,usr_city_bil);
            stmt.setString(col++,usr_address_bil);
//            stmt.setString(col++,usr_occupation_bil);
//            stmt.setString(col++,usr_income_level);
//            stmt.setString(col++,usr_edu_role);
//            stmt.setString(col++,usr_edu_level);
//            stmt.setString(col++,usr_school_bil);
            stmt.setString(col++,usr_class);
            stmt.setString(col++,usr_class_number);
         //   stmt.setTimestamp(27,usr_signup_date);
         //   stmt.setTimestamp(28,usr_last_login_date);
//            stmt.setTimestamp(col++,usr_special_date_1);
         //   stmt.setString(29,usr_status);
            stmt.setTimestamp(col++,usr_upd_date);
            stmt.setString(col++, usr_source);
            stmt.setString(col++, usr_extra_1);
            stmt.setString(col++, usr_extra_2);
            stmt.setString(col++, usr_extra_3);
            stmt.setString(col++, usr_extra_4);
            stmt.setString(col++, usr_extra_5);
            stmt.setString(col++, usr_extra_6);
            stmt.setString(col++, usr_extra_7);
            stmt.setString(col++, usr_extra_8);
            stmt.setString(col++, usr_extra_9);
            stmt.setString(col++, usr_extra_10);
            stmt.setTimestamp(col++, usr_approve_timestamp);
            stmt.setString(col++, usr_approve_usr_id);
            stmt.setString(col++, usr_approve_reason);
            stmt.setString(col++, usr_cost_center);
            stmt.setBoolean(col++, usr_syn_rol_ind);
            stmt.setString(col++, usr_not_syn_gpm_type);
            stmt.setString(col++,usr_job_title);
            stmt.setTimestamp(col++,usr_join_datetime);
            if(usr_app_approval_usg_ent_id==0) {
                stmt.setNull(col++,java.sql.Types.INTEGER);
            } else {
                stmt.setLong(col++,usr_app_approval_usg_ent_id);
            }
            stmt.setString(col++, nickname);
            stmt.setLong(col++,usr_ent_id);

            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                // update fails, rollback
                con.rollback();
                throw new qdbException("Fails to update user record.");
            }
            updExt(con);
            //update user roles
            assignUserRoles(con);
            //save group member relation
            saveEntityRelation(con, usr_attribute_ent_ids,
                            usr_attribute_relation_types, true, usr_id);
            //insert my sub-ordinates
            saveRoleTargetEntity(con, rol_target_ext_ids,
                                rol_target_ent_groups,
                                rol_target_starts, rol_target_ends,
                                usr_id, true);
            //insert my approvers
            saveMyApprover(con, appr_rol_ext_ids, appr_ent_ids, usr_id, true);

            saveSuperviseTarget(con, direct_supervisor_ent_ids, direct_supervisor_starts, direct_supervisor_ends, DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE, usr_id);
            saveSuperviseTarget(con, supervise_target_ent_ids, supervise_target_starts, supervise_target_ends, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE, usr_id);

            con.commit();
//TODO
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return;
    }
    public void updExt(Connection con) throws qdbException, qdbErrMessage, SQLException {
        final String UPD_USR_EXTENSION =
            "update " +
                "RegUserExtension " +
                "set " +
                    "urx_extra_datetime_11=?, "+
                    "urx_extra_datetime_12=?, "+
                    "urx_extra_datetime_13=?, "+
                    "urx_extra_datetime_14=?, "+
                    "urx_extra_datetime_15=?, "+
                    "urx_extra_datetime_16=?, "+
                    "urx_extra_datetime_17=?, "+
                    "urx_extra_datetime_18=?, "+
                    "urx_extra_datetime_19=?, "+
                    "urx_extra_datetime_20=?, "+

                    "urx_extra_singleoption_21=?,"+
                    "urx_extra_singleoption_22=?,"+
                    "urx_extra_singleoption_23=?,"+
                    "urx_extra_singleoption_24=?,"+
                    "urx_extra_singleoption_25=?,"+
                    "urx_extra_singleoption_26=?,"+
                    "urx_extra_singleoption_27=?,"+
                    "urx_extra_singleoption_28=?,"+
                    "urx_extra_singleoption_29=?,"+
                    "urx_extra_singleoption_30=?,"+

                    "urx_extra_multipleoption_31=?,"+
                    "urx_extra_multipleoption_32=?,"+
                    "urx_extra_multipleoption_33=?,"+
                    "urx_extra_multipleoption_34=?,"+
                    "urx_extra_multipleoption_35=?,"+
                    "urx_extra_multipleoption_36=?,"+
                    "urx_extra_multipleoption_37=?,"+
                    "urx_extra_multipleoption_38=?,"+
                    "urx_extra_multipleoption_39=?,"+
                    "urx_extra_multipleoption_40=?, "+
                    "urx_extra_41 = ?, " +
                    "urx_extra_42 = ?, " +
                    "urx_extra_43 = ?, " +
                    "urx_extra_44 = ?, " +
                    "urx_extra_45 = ? " +
                	" where urx_usr_ent_id=?";
            int index = 1;
            PreparedStatement pstmt = null;
            try {
                pstmt = con.prepareStatement(UPD_USR_EXTENSION);
                pstmt.setTimestamp(index++,usr_extra_datetime_11);
                pstmt.setTimestamp(index++,usr_extra_datetime_12);
                pstmt.setTimestamp(index++,usr_extra_datetime_13);
                pstmt.setTimestamp(index++,usr_extra_datetime_14);
                pstmt.setTimestamp(index++,usr_extra_datetime_15);
                pstmt.setTimestamp(index++,usr_extra_datetime_16);
                pstmt.setTimestamp(index++,usr_extra_datetime_17);
                pstmt.setTimestamp(index++,usr_extra_datetime_18);
                pstmt.setTimestamp(index++,usr_extra_datetime_19);
                pstmt.setTimestamp(index++,usr_extra_datetime_20);

                pstmt.setString(index++,usr_extra_singleoption_21);
                pstmt.setString(index++,usr_extra_singleoption_22);
                pstmt.setString(index++,usr_extra_singleoption_23);
                pstmt.setString(index++,usr_extra_singleoption_24);
                pstmt.setString(index++,usr_extra_singleoption_25);
                pstmt.setString(index++,usr_extra_singleoption_26);
                pstmt.setString(index++,usr_extra_singleoption_27);
                pstmt.setString(index++,usr_extra_singleoption_28);
                pstmt.setString(index++,usr_extra_singleoption_29);
                pstmt.setString(index++,usr_extra_singleoption_30);

                pstmt.setString(index++,usr_extra_multipleoption_31);
                pstmt.setString(index++,usr_extra_multipleoption_32);
                pstmt.setString(index++,usr_extra_multipleoption_33);
                pstmt.setString(index++,usr_extra_multipleoption_34);
                pstmt.setString(index++,usr_extra_multipleoption_35);
                pstmt.setString(index++,usr_extra_multipleoption_36);
                pstmt.setString(index++,usr_extra_multipleoption_37);
                pstmt.setString(index++,usr_extra_multipleoption_38);
                pstmt.setString(index++,usr_extra_multipleoption_39);
                pstmt.setString(index++,usr_extra_multipleoption_40);

                pstmt.setString(index++,urx_extra_41);
                pstmt.setString(index++,urx_extra_42);
                pstmt.setString(index++,urx_extra_43);
                pstmt.setString(index++,urx_extra_44);
                pstmt.setString(index++,urx_extra_45);

                pstmt.setLong(index++,usr_ent_id);
                pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
          } finally {
        	  if (pstmt != null) pstmt.close();
          }
        return;
    }

	public void insExt(Connection con) throws qdbException, qdbErrMessage {
		final String UPD_USR_EXTENSION =
			"Insert into "
				+ "RegUserExtension(urx_usr_ent_id,urx_extra_datetime_11,urx_extra_datetime_12,urx_extra_datetime_13,urx_extra_datetime_14,urx_extra_datetime_15,"
				+ "urx_extra_datetime_16,urx_extra_datetime_17,urx_extra_datetime_18,urx_extra_datetime_19,urx_extra_datetime_20,urx_extra_singleoption_21,urx_extra_singleoption_22,"
				+ "urx_extra_singleoption_23,urx_extra_singleoption_24,urx_extra_singleoption_25,urx_extra_singleoption_26,urx_extra_singleoption_27,urx_extra_singleoption_28,"
				+ "urx_extra_singleoption_29,urx_extra_singleoption_30,urx_extra_multipleoption_31,urx_extra_multipleoption_32,urx_extra_multipleoption_33,urx_extra_multipleoption_34,"
				+ "urx_extra_multipleoption_35,urx_extra_multipleoption_36,urx_extra_multipleoption_37,urx_extra_multipleoption_38,"
				+ "urx_extra_multipleoption_39,urx_extra_multipleoption_40,"
				+ "urx_extra_41,urx_extra_42,urx_extra_43,urx_extra_44,urx_extra_45)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            int index = 1;
            try {
                PreparedStatement pstmt = con.prepareStatement(UPD_USR_EXTENSION);
                pstmt.setLong(index++,usr_ent_id);
                pstmt.setTimestamp(index++,usr_extra_datetime_11);
                pstmt.setTimestamp(index++,usr_extra_datetime_12);
                pstmt.setTimestamp(index++,usr_extra_datetime_13);
                pstmt.setTimestamp(index++,usr_extra_datetime_14);
                pstmt.setTimestamp(index++,usr_extra_datetime_15);
                pstmt.setTimestamp(index++,usr_extra_datetime_16);
                pstmt.setTimestamp(index++,usr_extra_datetime_17);
                pstmt.setTimestamp(index++,usr_extra_datetime_18);
                pstmt.setTimestamp(index++,usr_extra_datetime_19);
                pstmt.setTimestamp(index++,usr_extra_datetime_20);

                pstmt.setString(index++,usr_extra_singleoption_21);
                pstmt.setString(index++,usr_extra_singleoption_22);
                pstmt.setString(index++,usr_extra_singleoption_23);
                pstmt.setString(index++,usr_extra_singleoption_24);
                pstmt.setString(index++,usr_extra_singleoption_25);
                pstmt.setString(index++,usr_extra_singleoption_26);
                pstmt.setString(index++,usr_extra_singleoption_27);
                pstmt.setString(index++,usr_extra_singleoption_28);
                pstmt.setString(index++,usr_extra_singleoption_29);
                pstmt.setString(index++,usr_extra_singleoption_30);

                pstmt.setString(index++,usr_extra_multipleoption_31);
                pstmt.setString(index++,usr_extra_multipleoption_32);
                pstmt.setString(index++,usr_extra_multipleoption_33);
                pstmt.setString(index++,usr_extra_multipleoption_34);
                pstmt.setString(index++,usr_extra_multipleoption_35);
                pstmt.setString(index++,usr_extra_multipleoption_36);
                pstmt.setString(index++,usr_extra_multipleoption_37);
                pstmt.setString(index++,usr_extra_multipleoption_38);
                pstmt.setString(index++,usr_extra_multipleoption_39);
                pstmt.setString(index++,usr_extra_multipleoption_40);

                pstmt.setString(index++,urx_extra_41);
                pstmt.setString(index++,urx_extra_42);
                pstmt.setString(index++,urx_extra_43);
                pstmt.setString(index++,urx_extra_44);
                pstmt.setString(index++,urx_extra_45);

                pstmt.executeUpdate();
                pstmt.close();
		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
		return;
	}

    public void del(Connection con, loginProfile prof, long parent_id)
        throws qdbException, qdbErrMessage, cwSysMessage {

        del(con, prof, parent_id, dbUtils.ENC_ENG);
    }

    public void del(Connection con, loginProfile prof, long parent_id, String labelEncoding)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try {
            //usr_ent_id = getEntId(con);
            ent_id = usr_ent_id;
            ent_upd_date = usr_upd_date;
            usr_ste_ent_id = prof.root_ent_id;

            //check if the user has any pending application for his/her approval
            if(DbAppnApprovalList.hasPendingApprovalAppn(con, usr_ent_id)) {
                throw new qdbErrMessage(CODE_USER_HAS_PENDING_APPROVAL);
            }
			Vector vData = null;
			StringBuffer sb = null;
			AccessControlWZB acl = new AccessControlWZB();
			Hashtable tadm = acl.getRole(con, usr_ste_ent_id, AccessControlWZB.ROL_STE_UID_TADM);

            //check if the entity is the sole training administrator of any trainingCenter.
/*            List soleTadmTc = ViewTrainingCenter.onlyRoleEntity4Tc(con,usr_ent_id,AccessControlWZB.ROL_STE_UID_TADM);
            if(soleTadmTc.size() > 0){
				sb = new StringBuffer();
            	for(int i=0,n=soleTadmTc.size();i<n;i++){
            		sb.append(dbUtils.esc4XML(soleTadmTc.get(i).toString()));
            		if(i != n-1){
            			sb.append(", ");
            		}
            	}
				vData = new Vector();
				vData.addElement(tadm_label);
				vData.addElement(sb.toString());
            	throw new cwSysMessage(CODE_SOLE_TADM_TC,vData);
            }*/

			String adm_ext_id = (String) tadm.get("rol_ext_id");
            Vector vSoleTADMItem = aeItemAccess.getSoleAccessItem(con, usr_ent_id, adm_ext_id);
            if(vSoleTADMItem.size() > 0) {
                //get training admin label
                sb = new StringBuffer();
                for(int i=0; i<vSoleTADMItem.size(); i++) {
                    if(i != 0) {
                        sb.append(", ");
                    }
                    sb.append(dbUtils.esc4XML(((aeItemAccess.ViewItemAccessGroupByItem)vSoleTADMItem.elementAt(i)).itm_title));
                    sb.append(" (").append(dbUtils.esc4XML(((aeItemAccess.ViewItemAccessGroupByItem)vSoleTADMItem.elementAt(i)).itm_code)).append(")");
                }
                vData = new Vector();
    			String adm_label = acl.getRoleLabel(adm_ext_id, labelEncoding);
                vData.addElement(adm_label);
                vData.addElement(sb.toString());
                throw new cwSysMessage(CODE_SOLE_TADM, vData);
            }

            //check if the user is the sole instr of any course
            //get instr rol_ext_id
			Hashtable instr = acl.getRole(con, usr_ste_ent_id, AccessControlWZB.ROL_STE_UID_INST);

			String instr_ext_id = (String) instr.get("rol_ext_id");
            Vector vSoleINSTRItem = aeItemAccess.getSoleAccessItem(con, usr_ent_id, instr_ext_id);
            if(vSoleINSTRItem.size() > 0) {
                sb = new StringBuffer();
                for(int i=0; i<vSoleINSTRItem.size(); i++) {
                    if(i != 0) {
                        sb.append(", ");
                    }
                    sb.append(dbUtils.esc4XML(((aeItemAccess.ViewItemAccessGroupByItem)vSoleINSTRItem.elementAt(i)).itm_title));
                    sb.append(" (").append(dbUtils.esc4XML(((aeItemAccess.ViewItemAccessGroupByItem)vSoleINSTRItem.elementAt(i)).itm_code)).append(")");
                }
                vData = new Vector();
    			String inst_label = acl.getRoleLabel(instr_ext_id, labelEncoding);
                vData.addElement(inst_label);
                vData.addElement(sb.toString());
                throw new cwSysMessage(CODE_SOLE_TADM, vData);
            }else{
				aeItemAccess.delByEntId(con,usr_ent_id);
			}


            PreparedStatement stmt = con.prepareStatement(
              " SELECT count(*) from EntityRelation WHERE"
            + " ern_child_ent_id = ?"
            + " And ern_type = ? "
            + " And ern_parent_ind = ? ");

            stmt.setLong(1, usr_ent_id);
            stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(3, true);
            ResultSet rs = stmt.executeQuery();
            dbEntityRelation dbEr = new dbEntityRelation();
            Timestamp deleteTime = cwSQL.getTime(con);
            int cnt = 0;
            if(rs.next()) {
                cnt = rs.getInt(1);
                if (cnt == 1) {
                    changeStatus(con, USR_STATUS_DELETED);
                    super.del(con, prof.usr_id, deleteTime);
                }else if (cnt <= 0) {
                        // delete fails, rollback
                        stmt.close();
                        con.rollback();
                        throw new qdbException("No such member exists.");
                }
            }else {
            	stmt.close();
                con.rollback();
                throw new qdbException("Fails to get user information.");
            }
            stmt.close();


            //del the entity from tcTrainingOfficer,and del him form acEntityRole of the role.
            if(acl.hasUserRole(con,usr_ent_id,"TADM_1")){
//				acl.rmUserFromRole(con,usr_ent_id,"TADM_1");
				ViewTrainingCenter.delOfficerRoleFromTc(con,usr_ent_id,"TADM_1");
            }


            dbEr.ern_ancestor_ent_id = parent_id;
            dbEr.ern_child_ent_id = usr_ent_id;
            dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
            dbEr.delAsChild(con, prof.usr_id, deleteTime);

            if(cnt == 1) {
                delAllEntityRelation(con, prof.usr_id, deleteTime);
            }
            //delete supervise target entity (direct supervisor, group supervisor relations)
            DbSuperviseTargetEntity.delBySourceEntId(con, usr_ent_id, null);

            DbAppnTargetEntity dbAte = new DbAppnTargetEntity();
            
            //Cancel Pending Approval Course - 新增对删除用户【只cancel待审批】的课程方法
            List appIdList = aeApplication.getPendingApprovalAppId(con, usr_ent_id);
            for (int a = 0; a < appIdList.size(); a++) {
				aeApplication aeApp = (aeApplication)appIdList.get(a);
				try {
					aeApp.cancelApp(con, prof, null, aeApp.app_id);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
            dbAte.ate_usr_ent_id = usr_ent_id;
            dbAte.delByUsrEntId(con);
            con.commit();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
        return;
    }

    /**
    Update this user's usr_remark_xml
    Pre-define variables:
    <ul>
    <li>usr_ent_id
    <li>usr_upd_date
    </ul>
    */
    private void updUserRemarkXML(Connection con, String newStatus) throws SQLException {
//        PreparedStatement stmt = con.prepareStatement(
//            " UPDATE RegUser SET usr_remark_xml = " + cwSQL.getClobNull(con)
//            + " WHERE usr_ent_id = ? ");
//
//        stmt.setLong(1, this.usr_ent_id);
//        stmt.executeUpdate();
//        stmt.close();

        this.usr_remark_xml = (newStatus != null && newStatus.equals(USR_STATUS_DELETED)) ?
                              "<deletion_remark timestamp=\"" + usr_upd_date + "\">" + getEntityAttributesAsXML(con) + "</deletion_remark>" :
                              null;
//        stmt = con.prepareStatement(
//                " SELECT usr_remark_xml FROM RegUser "
//                + "     WHERE usr_ent_id = ? FOR UPDATE ",
//                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
//
//        stmt.setLong(1, this.usr_ent_id);
//        ResultSet rs = stmt.executeQuery();
//        if (rs.next())
//        {
//            cwSQL.setClobValue(con, rs, "usr_remark_xml", this.usr_remark_xml);
//            rs.updateRow();
//        }
//        stmt.close();
        // Update usr_remark_xml
        // for oracle clob
        String condition = "usr_ent_id = " + this.usr_ent_id;
        String tableName = "RegUser";
        String[] colName = {"usr_remark_xml"};
        String[] colValue = {this.usr_remark_xml};
        cwSQL.updateClobFields(con, tableName, colName, colValue, condition);
    }

    public void changeStatus(Connection con, String newStatus)
        throws qdbException, qdbErrMessage
    {
        try {
            ent_upd_date = usr_upd_date;
            super.upd(con);
            usr_upd_date = ent_upd_date;

            PreparedStatement stmt1 = con.prepareStatement(
            " UPDATE RegUser SET "
            + " usr_status = ? ,"
            + " usr_upd_date = ? "
            + " WHERE usr_ent_id= ? " );

            stmt1.setString(1, newStatus);
            stmt1.setTimestamp(2, usr_upd_date);
            stmt1.setLong(3, usr_ent_id);

            // update
            int stmtResult=stmt1.executeUpdate();
            stmt1.close();
            if ( stmtResult!=1)
            {
                // update fails, rollback
                con.rollback();
                throw new qdbException("Fails to delete user : " + usr_id);
            }
            else {
                //update usr_remark_xml
                updUserRemarkXML(con, newStatus);
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
        return;
    }

    public void changeStatus_Cut(Connection con, String newStatus)
            throws qdbException, qdbErrMessage
        {
            try {
                ent_upd_date = usr_upd_date;
                super.upd(con);
                usr_upd_date = ent_upd_date;

                PreparedStatement stmt1 = con.prepareStatement(
                " UPDATE RegUser SET "
                + " usr_status = ? ,"
                + " usr_upd_date = ? ,"
                + " usr_app_approval_usg_ent_id = ? "
                + " WHERE usr_ent_id= ? " );

                stmt1.setString(1, newStatus);
                stmt1.setTimestamp(2, usr_upd_date);
                stmt1.setNull(3, Types.LONG);
                stmt1.setLong(4, usr_ent_id);

                // update
                int stmtResult=stmt1.executeUpdate();
                stmt1.close();
                if ( stmtResult!=1)
                {
                    // update fails, rollback
                    con.rollback();
                    throw new qdbException("Fails to delete user : " + usr_id);
                }
                else {
                    //update usr_remark_xml
                    updUserRemarkXML(con, newStatus);
                }

            } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
            }
            return;
        }

    
    public String insCosPrepXMLNoHeader(Connection con, loginProfile prof, String dpo_view, String navXML)
        throws qdbException,SQLException {
        String xml = insCosPrepXML(con, prof, dpo_view, navXML,0,"",0);
        return xml.substring(xml.indexOf("?>")+2);
    }

    public String insCosPrepXML(Connection con, loginProfile prof, String dpo_view, String extXML,long obj_id,String curr_stylesheet, long cos_res_id)
        throws qdbException,SQLException
    {
        // format xml
        StringBuffer result = new StringBuffer();
        result.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL).append(dbUtils.NEWL);
        result.append("<user id=\"").append(dbUtils.esc4XML(usr_ste_usr_id)).append("\" ent_id=\"").append(usr_ent_id).append("\" status=\"").append(usr_status).append("\" last_login=\"");
		result.append(usr_last_login_date).append("\" timestamp=\"").append(usr_upd_date).append("\">").append(dbUtils.NEWL);

        // author's information
        result.append(prof.asXML()).append(dbUtils.NEWL);

        //get the current obj access
        if(obj_id != 0){
        	dbObjective dbobj=new dbObjective();
        	dbobj.obj_id=obj_id;
        	dbobj.get(con);
        	result.append("<objective id=\"").append(obj_id).append("\" >");
        	result.append("<desc>").append(dbUtils.esc4XML(dbobj.obj_desc)).append("</desc>");
        	result.append(dbobj.getObjPathAsXML(con));
        	result.append("</objective>");
			AcObjective acObj = new AcObjective(con);
			AcPageVariant acPageVariant = new AcPageVariant(con);
			Hashtable xslQuestions=AcXslQuestion.getQuestions();
        	String access = acObj.getObjectiveAccess(obj_id,prof.usr_ent_id);
        	result.append("<obj_access>").append(access).append("</obj_access>");
        	acPageVariant.obj_id = obj_id;
			acPageVariant.ent_id = prof.usr_ent_id;
			acPageVariant.rol_ext_id= prof.current_role;
			String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(curr_stylesheet));
			result.append(metaXML);
			}
        // get system time
        result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>");

        // get cos display option
        result.append("<course>").append(dbCourse.getCosDisplayOption(con, 0, dpo_view)).append("</course>");
        
        //判断是否为考试：考试只能添加测验模块
        if(cos_res_id > 0 ){
        	result.append("<itm_exam_ind>").append(aeItem.isExam(con, cos_res_id)).append("</itm_exam_ind>");
        	result.append("<itm_type>").append(aeItem.getItemType(con, cos_res_id)).append("</itm_type>");
        }

        // navigator or extension xml
        result.append(extXML).append("</user>");

        return result.toString();
    }

	//Christ Qiu
	public String getProfObjPath(
		Connection con,
		loginProfile prof,
		long obj_id)
		throws qdbException, SQLException {
		// format xml
		StringBuffer result = new StringBuffer();
		result
			.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>");
//			.append(dbUtils.NEWL)
//			.append(dbUtils.NEWL);
		result
			.append("<user id=\"").append(dbUtils.esc4XML(usr_ste_usr_id)).append("\" ent_id=\"")
			.append(usr_ent_id).append("\" status=\"").append(usr_status).append("\" last_login=\"");
		result
			.append(usr_last_login_date)
			.append("\" timestamp=\"")
			.append(usr_upd_date)
			.append("\">")
			.append(dbUtils.NEWL);
		// author's information
		result.append(prof.asXML()).append(dbUtils.NEWL);
		//get the current obj access
		if (obj_id != 0) {
			dbObjective Objective = new dbObjective(obj_id);
			Vector obj_ancestorVec = Objective.getObjAncesters(con);
			Objective.outObjAncestorTreeXml(result,obj_ancestorVec);
//			long direct_parent = dbRegUser.get_obj_parent(con, obj_id);
//			dbRegUser.get_obj_parent(con,obj_id);
//			while (direct_parent != 0) {
//				direct_parent = dbRegUser.get_obj_parent(con, obj_id);
//			}
//			result.append("<objective_path>");
//			for (int i = 0 ; i < dbObjective.ancestors_vec.size(); i++) {
//				result.append("<objective>").append(dbObjective.ancestors_vec.elementAt(i)).append("</objective>");
//			}
//			result.append("</objective_path>");
		}
		// get system time
			result.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>");
			result.append("</user>");
			return result.toString();
	}

    /*
    public String asXML(Connection con, loginProfile prof)
        throws qdbException
    {
        // format xml
        StringBuffer result = new StringBuffer();
        result.append(dbUtils.xmlHeader);
        result.append(getUserXML(con, prof));
        return (result.toString());
    }
    */
    /*
    public String getUserXML(Connection con, loginProfile prof)
        throws qdbException
    {
        try {
            String result = new String();

            result = "<user id=\"" + dbUtils.esc4XML(usr_ste_usr_id) + "\" ent_id=\"" + usr_ent_id
                + "\" status=\"" + usr_status + "\" last_login=\""
                + usr_last_login_date + "\" timestamp=\"" + usr_upd_date + "\">" + dbUtils.NEWL;

            // author's information
            result += prof.asXML() + dbUtils.NEWL;

            AccessControlWZB acl = new AccessControlWZB();
            // Each User should have one role only
            String[] roles = acl.getUserRoles(con, usr_ent_id);
            if (roles == null || roles.length==0)
                throw  new qdbException("Failed to get user role.");
            result += "<roles>";
            for(int i=0; i<roles.length; i++) {
                result += dbUtils.getRoleAsXML(con, roles[i]);
            }
            result += "</roles>";
            //result += "<role>" + roles[0] + "</role>" + dbUtils.NEWL;

            result += "<email>" + dbUtils.esc4XML(usr_email) + "</email>" + dbUtils.NEWL;
            result += "<pwd>" + dbUtils.esc4XML(usr_pwd) + "</pwd>" + dbUtils.NEWL;
            result += "<name first_name=\"" + dbUtils.esc4XML(usr_first_name_bil) + "\" last_name=\"" + dbUtils.esc4XML(usr_last_name_bil)
                + "\" display_name=\"" + dbUtils.esc4XML(usr_display_bil) + "\" />" + dbUtils.NEWL;
            result += "<gender>" + usr_gender + "</gender>" + dbUtils.NEWL;
            result += "<country>" + dbUtils.esc4XML(usr_country_bil) + "</country>" + dbUtils.NEWL;
            result += "<occupation>" + dbUtils.esc4XML(usr_occupation_bil) + "</occupation>" + dbUtils.NEWL;
            result += "<income_level>" + usr_income_level + "</income_level>" + dbUtils.NEWL;
            result += "<edu level=\"" + usr_edu_level + "\" role=\"" + dbUtils.esc4XML(usr_edu_role) + "\" school=\"" + dbUtils.esc4XML(usr_school_bil)
                        + "\" class=\"" + usr_class + "\" class_number=\"" + usr_class_number
                        + "\" />" + dbUtils.NEWL;
            result += "<signup date=\"" + usr_signup_date + "\" />" + dbUtils.NEWL;
            result += "<special date=\"" + usr_special_date_1 + "\" />" + dbUtils.NEWL;

            result += "<birth day=\"" + usr_bday + "\" place=\"" + dbUtils.esc4XML(usr_bplace_bil) + "\" />" + dbUtils.NEWL;
            result += "<hkid>" + usr_hkid + "</hkid>" + dbUtils.NEWL;
            result += "<other_id no=\"" + usr_other_id_no + "\" type=\"" + usr_other_id_type + "\" />" + dbUtils.NEWL;
            result += "<tel tel_1=\"" + usr_tel_1 + "\" tel_2=\"" + usr_tel_2 + "\" fax_1=\"" +
                usr_fax_1 + "\" />" + dbUtils.NEWL;
            result += "<address>" + dbUtils.esc4XML(usr_address_bil) + "</address>" + dbUtils.NEWL;
            result += "<postal_code>" + usr_postal_code_bil + "</postal_code>" + dbUtils.NEWL;
            result += "<state>" + dbUtils.esc4XML(usr_state_bil) + "</state>" + dbUtils.NEWL;
            result += "<city>" + dbUtils.esc4XML(usr_city_bil) + "</city>" + dbUtils.NEWL;

            RegUser user = new RegUser();
            user.userEntId = this.usr_ent_id;
            result += user.getEntityAttributesAsXML(con);

            result += dbUtils.getAllRoleAsXML(con);
            result += "</user>" + dbUtils.NEWL ;

            return result;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    */

    // default not show org name
    public String getUserXML(Connection con, loginProfile prof)
        throws qdbException
    {
        return getUserXML(con, prof, null);
    }

    // pass wizbini to check show org name or not
    public String getUserXML(Connection con, loginProfile prof, WizbiniLoader wizbini)
        throws qdbException
    {//	final String SQL_USR_EXTENSION_FIELD = "select * from RegUserExtension where urx_usr_ent_id=?";
        try {
            StringBuffer result = new StringBuffer();
            long site_ent_id = (prof!=null && prof.root_ent_id!=0) ?
                                prof.root_ent_id :
                                this.usr_ste_ent_id;
//			PreparedStatement pstmt = con.prepareStatement(SQL_USR_EXTENSION_FIELD);
//			pstmt.setLong(1,usr_ent_id);
//			ResultSet rs = pstmt.executeQuery();
            result.append("<user id=\"").append(dbUtils.esc4XML(usr_ste_usr_id));
            result.append("\" ent_id=\"").append(usr_ent_id);
            result.append("\" status=\"").append(usr_status);
            result.append("\" syn_ind=\"").append(ent_syn_ind);
            result.append("\" syn_rol_ind=\"").append(usr_syn_rol_ind);
            result.append("\" not_syn_gpm_type=\"").append(usr_not_syn_gpm_type);
            result.append("\" last_login=\"").append(usr_last_login_date);
            result.append("\" timestamp=\"").append(usr_upd_date).append("\">").append(dbUtils.NEWL);

            // get the ancestor's id and name
            Vector usr_group_id_lst= dbUserGroup.getUserParentEntIds(con, usr_ent_id);
            Vector ancestorTable = new Vector();
            if(usr_group_id_lst != null && usr_group_id_lst.size() > 0){
            	ancestorTable = dbEntityRelation.getGroupAncestorList2Vc(con, ((Long)usr_group_id_lst.elementAt(0)).longValue(), true);
            }
            result.append("<ancestor_node_list>").append(dbUtils.NEWL);
            // discard the first element of gpm_ancester which should be the self node
            // while the full_path should be started at the first node
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            for (int i=1; i<ancestorTable.size(); i++) {
            	long ancestorId = ((Long)ancestorTable.elementAt(i)).longValue();
            	boolean canMgtUsg = true;
            	if(wizbini != null) {
            		AcUserGroup acusg = new AcUserGroup(con);
            		if(!acusg.canManageGroup(prof, ancestorId, wizbini.cfgTcEnabled)) {
            			canMgtUsg = false;
            		}
            	}
            	if(canMgtUsg) {
            		result.append("<node id=\"").append(ancestorId).append("\">").append(dbUtils.NEWL);
            		result.append("<title>").append(dbUtils.esc4XML(entityfullpath.getEntityName(con, ancestorId))).append("</title>").append(dbUtils.NEWL);
            		result.append("</node>").append(dbUtils.NEWL);
            	}
            }
            result.append("</ancestor_node_list>").append(dbUtils.NEWL);

            //get all role desc
            result.append(dbUtils.getAllRoleAsXML(con,"all_role_list", site_ent_id));
            result.append(dbUtils.getAuthRoleAsXML(con, prof));
            // get active role list
            AccessControlWZB acl = new AccessControlWZB();
            Timestamp cur_time = cwSQL.getTime(con);
            List roleArray = DbAcRole.getRolesCanLogin(con, usr_ent_id, cur_time);
            //don't throw exception as the trashed user really don't have role
            /*
            if (roles == null || roles.length==0) {
                throw  new qdbException("Failed to get user role.");
            }
            */
			result.append(dbUtils.DisableRolesXml(con,prof.root_ent_id));
            result.append("<role_list>");
            if(roleArray != null) {
                for(int i=0; i<roleArray.size(); i++) {
                	DbAcRole role = (DbAcRole)roleArray.get(i);
                    result.append(AccessControlWZB.getRolXml(role.rol_ext_id, role.rol_title));
                }
            }

            result.append("</role_list>");
            result.append(getUserAllRoleAsXML(con));

            //result += "<role>" + roles[0] + "</role>" + dbUtils.NEWL;

            result.append("<email email_1=\"").append(dbUtils.esc4XML(usr_email));
            result.append("\" email_2=\"").append(dbUtils.esc4XML(usr_email_2));
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<pwd>").append(dbUtils.esc4XML(usr_pwd)).append("</pwd>").append(dbUtils.NEWL);
            result.append("<extra_1>").append(dbUtils.esc4XML(usr_extra_1)).append("</extra_1>").append(dbUtils.NEWL);
            result.append("<extra_2>").append(dbUtils.esc4XML(usr_extra_2)).append("</extra_2>").append(dbUtils.NEWL);
            result.append("<extra_3>").append(dbUtils.esc4XML(usr_extra_3)).append("</extra_3>").append(dbUtils.NEWL);
            result.append("<extra_4>").append(dbUtils.esc4XML(usr_extra_4)).append("</extra_4>").append(dbUtils.NEWL);
            result.append("<extra_5>").append(dbUtils.esc4XML(usr_extra_5)).append("</extra_5>").append(dbUtils.NEWL);
            result.append("<extra_6>").append(dbUtils.esc4XML(usr_extra_6)).append("</extra_6>").append(dbUtils.NEWL);
            result.append("<extra_7>").append(dbUtils.esc4XML(usr_extra_7)).append("</extra_7>").append(dbUtils.NEWL);
            result.append("<extra_8>").append(dbUtils.esc4XML(usr_extra_8)).append("</extra_8>").append(dbUtils.NEWL);
            result.append("<extra_9>").append(dbUtils.esc4XML(usr_extra_9)).append("</extra_9>").append(dbUtils.NEWL);
            result.append("<extra_10>").append(dbUtils.esc4XML(usr_extra_10)).append("</extra_10>").append(dbUtils.NEWL);
 			result.append("<extra_datetime_11>").append(usr_extra_datetime_11).append("</extra_datetime_11>");
			result.append("<extra_datetime_12>").append(usr_extra_datetime_12).append("</extra_datetime_12>");
			result.append("<extra_datetime_13>").append(usr_extra_datetime_13).append("</extra_datetime_13>");
			result.append("<extra_datetime_14>").append(usr_extra_datetime_14).append("</extra_datetime_14>");
			result.append("<extra_datetime_15>").append(usr_extra_datetime_15).append("</extra_datetime_15>");
			result.append("<extra_datetime_16>").append(usr_extra_datetime_16).append("</extra_datetime_16>");
			result.append("<extra_datetime_17>").append(usr_extra_datetime_17).append("</extra_datetime_17>");
			result.append("<extra_datetime_18>").append(usr_extra_datetime_18).append("</extra_datetime_18>");
			result.append("<extra_datetime_19>").append(usr_extra_datetime_19).append("</extra_datetime_19>");
			result.append("<extra_datetime_20>").append(usr_extra_datetime_20).append("</extra_datetime_20>");

            result.append("<extra_singleoption_21 id=\"").append(usr_extra_singleoption_21).append("\"/>")
                .append("<extra_singleoption_22 id=\"").append(usr_extra_singleoption_22).append("\"/>")
                .append("<extra_singleoption_23 id=\"").append(usr_extra_singleoption_23).append("\"/>")
                .append("<extra_singleoption_24 id=\"").append(usr_extra_singleoption_24).append("\"/>")
                .append("<extra_singleoption_25 id=\"").append(usr_extra_singleoption_25).append("\"/>")
                .append("<extra_singleoption_26 id=\"").append(usr_extra_singleoption_26).append("\"/>")
                .append("<extra_singleoption_27 id=\"").append(usr_extra_singleoption_27).append("\"/>")
                .append("<extra_singleoption_28 id=\"").append(usr_extra_singleoption_28).append("\"/>")
                .append("<extra_singleoption_29 id=\"").append(usr_extra_singleoption_29).append("\"/>")
                .append("<extra_singleoption_30 id=\"").append(usr_extra_singleoption_30).append("\"/>");

            String[] split = cwUtils.splitToString(usr_extra_multipleoption_31,",",true);
            result.append("<extra_multipleoption_31>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_31>");

            split = cwUtils.splitToString(usr_extra_multipleoption_32,",",true);
            result.append("<extra_multipleoption_32>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_32>");

            split = cwUtils.splitToString(usr_extra_multipleoption_33,",",true);
            result.append("<extra_multipleoption_33>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_33>");

            split = cwUtils.splitToString(usr_extra_multipleoption_34,",",true);
            result.append("<extra_multipleoption_34>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_34>");

            split = cwUtils.splitToString(usr_extra_multipleoption_35,",",true);
            result.append("<extra_multipleoption_35>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_35>");

            split = cwUtils.splitToString(usr_extra_multipleoption_36,",",true);
            result.append("<extra_multipleoption_36>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_36>");

            split = cwUtils.splitToString(usr_extra_multipleoption_37,",",true);
            result.append("<extra_multipleoption_37>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_37>");

            split = cwUtils.splitToString(usr_extra_multipleoption_38,",",true);
            result.append("<extra_multipleoption_38>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_38>");

            split = cwUtils.splitToString(usr_extra_multipleoption_39,",",true);
            result.append("<extra_multipleoption_39>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_39>");

            split = cwUtils.splitToString(usr_extra_multipleoption_40,",",true);
            result.append("<extra_multipleoption_40>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_40>");
            result.append("<extra_41>").append(dbUtils.esc4XML(usr_extra_2)).append("</extra_41>");
            result.append("<extra_42>").append(dbUtils.esc4XML(urx_extra_42)).append("</extra_42>");
            result.append("<extra_43>").append(dbUtils.esc4XML(getUsrPhotoDir(wizbini, prof.root_id, usr_ent_id, urx_extra_43))).append("</extra_43>");
            result.append("<extra_44>").append(dbUtils.esc4XML(urx_extra_44)).append("</extra_44>");
            result.append("<extra_45>").append(dbUtils.esc4XML(urx_extra_45)).append("</extra_45>");

            result.append("<cost_center>").append(dbUtils.esc4XML(usr_cost_center)).append("</cost_center>").append(dbUtils.NEWL);
            result.append("<name first_name=\"").append(dbUtils.esc4XML(usr_first_name_bil));
            result.append("\" last_name=\"").append(dbUtils.esc4XML(usr_last_name_bil));
            result.append("\" display_name=\"").append(dbUtils.esc4XML(usr_display_bil));
            result.append("\" initial_name=\"").append(dbUtils.esc4XML(usr_initial_name_bil));
            result.append("\" full_name=\"").append(dbUtils.esc4XML(usr_full_name_bil));
            result.append("\" nickname=\"").append(dbUtils.esc4XML(usr_nickname));
            result.append("\" />").append(dbUtils.NEWL);
            result.append("<source>").append(dbUtils.esc4XML(usr_source)).append("</source>").append(dbUtils.NEWL);

            result.append("<gender>").append(usr_gender).append("</gender>").append(dbUtils.NEWL);
            result.append("<country>").append(dbUtils.esc4XML(usr_country_bil)).append("</country>").append(dbUtils.NEWL);
//            result.append("<occupation>").append(dbUtils.esc4XML(usr_occupation_bil)).append("</occupation>").append(dbUtils.NEWL);
//            result.append("<income_level>").append(usr_income_level).append("</income_level>").append(dbUtils.NEWL);

//            result.append("<edu level=\"").append(usr_edu_level);
//            result.append("\" role=\"").append(dbUtils.esc4XML(usr_edu_role));
//            result.append("\" school=\"").append(dbUtils.esc4XML(usr_school_bil));
//            result.append("\" class=\"").append(usr_class);
//            result.append("\" class_number=\"").append(usr_class_number);
//            result.append("\" />").append(dbUtils.NEWL);
            result.append("<edu class=\"").append(usr_class);
            result.append("\" class_number=\"").append(usr_class_number);
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<signup date=\"").append(usr_signup_date).append("\" />").append(dbUtils.NEWL);
//            result.append("<special date=\"").append(usr_special_date_1).append("\" />").append(dbUtils.NEWL);

            result.append("<birth day=\"").append(usr_bday);
//            result.append("\" place=\"").append(dbUtils.esc4XML(usr_bplace_bil));
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<hkid>").append(usr_hkid).append("</hkid>").append(dbUtils.NEWL);
            result.append("<other_id no=\"").append(usr_other_id_no).append("\" type=\"").append(usr_other_id_type).append("\" />").append(dbUtils.NEWL);

            result.append("<tel tel_1=\"").append(dbUtils.esc4XML(usr_tel_1));
            result.append("\" tel_2=\"").append(dbUtils.esc4XML(usr_tel_2));
            result.append("\" fax_1=\"").append(dbUtils.esc4XML(usr_fax_1));
            result.append("\" />").append(dbUtils.NEWL);

            String approverName = (usr_approve_usr_id != null && usr_approve_usr_id.length() > 0)
                                ? getDisplayBil(con, usr_approve_usr_id)
                                : "";
            result.append("<approval>")
                  .append("<user id=\"").append(cwUtils.escNull(usr_approve_usr_id)).append("\">")
                  .append("<display_name>").append(cwUtils.esc4XML(cwUtils.escNull(approverName))).append("</display_name>")
                  .append("</user>")
                  .append("<timestamp>").append(cwUtils.escNull(usr_approve_timestamp)).append("</timestamp>")
                  .append("<reason>").append(cwUtils.esc4XML(cwUtils.escNull(usr_approve_reason))).append("</reason>")
                  .append("</approval>");

            result.append("<address>").append(dbUtils.esc4XML(usr_address_bil)).append("</address>").append(dbUtils.NEWL);
            result.append("<postal_code>").append(usr_postal_code_bil).append("</postal_code>").append(dbUtils.NEWL);
            result.append("<state>").append(dbUtils.esc4XML(usr_state_bil)).append("</state>").append(dbUtils.NEWL);
            result.append("<join_date>").append(cwUtils.escNull(usr_join_datetime)).append("</join_date>").append(dbUtils.NEWL);
            result.append("<job_title>").append(cwUtils.esc4XML(cwUtils.escNull(usr_job_title))).append("</job_title>").append(dbUtils.NEWL);
            if(usr_app_approval_usg_ent_id==0) {
                result.append("<app_approval_usg ent_id=\"" + usr_app_approval_usg_ent_id + "\" display_bil=\"\"/>").append(dbUtils.NEWL);
            } else {
                result.append("<app_approval_usg ent_id=\"" + usr_app_approval_usg_ent_id + "\" display_bil=\"" + cwUtils.esc4XML(dbUserGroup.getDisplayBil(con, usr_app_approval_usg_ent_id)) + "\"/>").append(dbUtils.NEWL);
            }

//            result.append("<city>").append(dbUtils.esc4XML(usr_city_bil)).append("</city>").append(dbUtils.NEWL);

//            RegUser user = new RegUser();
//            user.userEntId = this.usr_ent_id;
            if(usr_status.equalsIgnoreCase(USR_STATUS_DELETED)) {
                result.append(getEntityAttributesAsXML(con, true));
            } else {
                result.append(getEntityAttributesAsXML(con));
            }

            //get role target entity as xml (e.g. approver)
            result.append(getRoleTargetEntityAsXML(con));

            //get user remark
            result.append(cwUtils.escNull(this.usr_remark_xml));
            String full_path = dbEntityRelation.getFullPath(con, usr_ent_id);
            // show org name before full path when multiple organzation is allowed
            if (wizbini !=null && wizbini.cfgSysSetupadv.getOrganization().isMultiple()){
                full_path = acSite.getSteName(con, usr_ste_ent_id) + wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator()
                    + full_path;
            }
            result.append("<full_path>" + cwUtils.esc4XML(full_path) + "</full_path>");
            // get direct supervisor
            result.append(ViewSuperviseTargetEntity.getDirectSupervisorAsXML(con, usr_ent_id));
            result.append(ViewSuperviseTargetEntity.getSupervisedGroupAsXML(con, usr_ent_id));

            result.append(getUsrSkillSetAsXML(con, prof.root_ent_id));

            result.append("</user>").append(dbUtils.NEWL);


            return result.toString();
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }



    public String getUserReportXML(Connection con, loginProfile prof)
        throws qdbException
    {

            StringBuffer result = new StringBuffer();
            long site_ent_id = (prof!=null && prof.root_ent_id!=0) ?
                                prof.root_ent_id :
                                this.usr_ste_ent_id;

            result.append("<user id=\"").append(dbUtils.esc4XML(usr_ste_usr_id));
            result.append("\" ent_id=\"").append(usr_ent_id);
            result.append("\" status=\"").append(usr_status);
            result.append("\" syn_ind=\"").append(ent_syn_ind);
            result.append("\" syn_rol_ind=\"").append(usr_syn_rol_ind);
            result.append("\" not_syn_gpm_type=\"").append(usr_not_syn_gpm_type);
            result.append("\" last_login=\"").append(usr_last_login_date);
            result.append("\" timestamp=\"").append(usr_upd_date).append("\">").append(dbUtils.NEWL);

            //get all role desc
            //result.append(dbUtils.getAllRoleAsXML(con,"all_role_list", site_ent_id));

            // get active role list
            /*
            AccessControlWZB acl = new AccessControlWZB();
            String[] roles = acl.getUserRoles(con, usr_ent_id);
            result.append("<role_list>");
            if(roles != null) {
                for(int i=0; i<roles.length; i++) {
                    result.append(dbUtils.getRoleAsXML(con, roles[i]));
                }
            }
            result.append("</role_list>");
            */

            /*
            result.append(getUserAllRoleAsXML(con));
            */

            result.append("<email email_1=\"").append(dbUtils.esc4XML(usr_email));
            result.append("\" email_2=\"").append(dbUtils.esc4XML(usr_email_2));
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<pwd>").append(dbUtils.esc4XML(usr_pwd)).append("</pwd>").append(dbUtils.NEWL);
            result.append("<extra_1>").append(dbUtils.esc4XML(usr_extra_1)).append("</extra_1>").append(dbUtils.NEWL);
            result.append("<extra_2>").append(dbUtils.esc4XML(usr_extra_2)).append("</extra_2>").append(dbUtils.NEWL);
            result.append("<extra_3>").append(dbUtils.esc4XML(usr_extra_3)).append("</extra_3>").append(dbUtils.NEWL);
            result.append("<extra_4>").append(dbUtils.esc4XML(usr_extra_4)).append("</extra_4>").append(dbUtils.NEWL);
            result.append("<extra_5>").append(dbUtils.esc4XML(usr_extra_5)).append("</extra_5>").append(dbUtils.NEWL);
            result.append("<extra_6>").append(dbUtils.esc4XML(usr_extra_6)).append("</extra_6>").append(dbUtils.NEWL);
            result.append("<extra_7>").append(dbUtils.esc4XML(usr_extra_7)).append("</extra_7>").append(dbUtils.NEWL);
            result.append("<extra_8>").append(dbUtils.esc4XML(usr_extra_8)).append("</extra_8>").append(dbUtils.NEWL);
            result.append("<extra_9>").append(dbUtils.esc4XML(usr_extra_9)).append("</extra_9>").append(dbUtils.NEWL);
            result.append("<extra_10>").append(dbUtils.esc4XML(usr_extra_10)).append("</extra_10>").append(dbUtils.NEWL);
            result.append("<cost_center>").append(dbUtils.esc4XML(usr_cost_center)).append("</cost_center>").append(dbUtils.NEWL);

            result.append("<name first_name=\"").append(dbUtils.esc4XML(usr_first_name_bil));
            result.append("\" last_name=\"").append(dbUtils.esc4XML(usr_last_name_bil));
            result.append("\" display_name=\"").append(dbUtils.esc4XML(usr_display_bil));
            result.append("\" initial_name=\"").append(dbUtils.esc4XML(usr_initial_name_bil));
            result.append("\" full_name=\"").append(dbUtils.esc4XML(usr_full_name_bil));
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<gender>").append(usr_gender).append("</gender>").append(dbUtils.NEWL);
            result.append("<country>").append(dbUtils.esc4XML(usr_country_bil)).append("</country>").append(dbUtils.NEWL);
            result.append("<edu class=\"").append(usr_class);
            result.append("\" class_number=\"").append(usr_class_number);
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<signup date=\"").append(usr_signup_date).append("\" />").append(dbUtils.NEWL);
            result.append("<birth day=\"").append(usr_bday);
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<hkid>").append(usr_hkid).append("</hkid>").append(dbUtils.NEWL);
            result.append("<other_id no=\"").append(usr_other_id_no).append("\" type=\"").append(usr_other_id_type).append("\" />").append(dbUtils.NEWL);

            result.append("<tel tel_1=\"").append(dbUtils.esc4XML(usr_tel_1));
            result.append("\" tel_2=\"").append(dbUtils.esc4XML(usr_tel_2));
            result.append("\" fax_1=\"").append(dbUtils.esc4XML(usr_fax_1));
            result.append("\" />").append(dbUtils.NEWL);

            /*
            String approverName = (usr_approve_usr_id != null && usr_approve_usr_id.length() > 0)
                                ? getDisplayBil(con, usr_approve_usr_id)
                                : "";
            result.append("<approval>")
                  .append("<user id=\"").append(cwUtils.escNull(usr_approve_usr_id)).append("\">")
                  .append("<display_name>").append(cwUtils.esc4XML(cwUtils.escNull(approverName))).append("</display_name>")
                  .append("</user>")
                  .append("<timestamp>").append(cwUtils.escNull(usr_approve_timestamp)).append("</timestamp>")
                  .append("<reason>").append(cwUtils.esc4XML(cwUtils.escNull(usr_approve_reason))).append("</reason>")
                  .append("</approval>");
            */

            result.append("<address>").append(dbUtils.esc4XML(usr_address_bil)).append("</address>").append(dbUtils.NEWL);
            result.append("<postal_code>").append(usr_postal_code_bil).append("</postal_code>").append(dbUtils.NEWL);
            result.append("<state>").append(dbUtils.esc4XML(usr_state_bil)).append("</state>").append(dbUtils.NEWL);
            //result.append(getEntityAttributesAsXML(con));

            //get role target entity as xml (e.g. approver)
            //result.append(getRoleTargetEntityAsXML(con));

            //get user's approver
            //result.append(myApproverAsXML(con, site_ent_id));

            //get user remark
            result.append(cwUtils.escNull(this.usr_remark_xml));

            result.append("</user>").append(dbUtils.NEWL);

            return result.toString();

    }

    /**
    Get Targeted Learners and Number as XML<BR>
    Pre-define variable:
    <ul>
    <li>usr_ent_id
    <li>usr_ste_ent_id
    </ul>
    */
    private String getRoleTargetEntityAsXML(Connection con) throws SQLException {

        //Vector v_rol_ext_id = dbUtils.getRoleByTargetEntInd(con, this.usr_ste_ent_id, true);
        //get approver roles in org
        Vector vTemp = dbUtils.getOrgApprRole(con, this.usr_ste_ent_id);
        Vector vApprRole = (Vector)vTemp.elementAt(0);
        Vector vApprRoleType = (Vector)vTemp.elementAt(1);

        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<target_list>");
        int size = vApprRole.size();
        for(int i=0; i<size; i++) {
            String rol_ext_id = (String) vApprRole.elementAt(i);
            String rol_target_type = (String) vApprRoleType.elementAt(i);
            xmlBuf.append("<target_group_list role_id=\"").append(rol_ext_id).append("\"")
                  .append(" ent_type=\"").append(rol_target_type).append("\">");
            xmlBuf.append(ViewRoleTargetGroup.getTargetGroupsAsXML(con, this.usr_ent_id, rol_ext_id, false));
            xmlBuf.append("</target_group_list>");
        }
        xmlBuf.append("</target_list>");
        return xmlBuf.toString();
    }

    /*
    *   old method
    */
    public StringBuffer getUserShortXML(Connection con, boolean displayRole, boolean displayEntAttribute)
        throws cwException
    {
        return getUserShortXML(con, displayRole, displayEntAttribute, false);
    }

    /*
    old API kept for backward compatibility
    */
    public StringBuffer getUserShortXML(Connection con, boolean displayRole, boolean displayEntAttribute, boolean showPwd)
        throws cwException {

        return getUserShortXML(con, displayRole, displayEntAttribute, showPwd, false);

    }

    /*
    old API kept for backward compatibility
    */
    public StringBuffer getUserShortXML(Connection con, boolean displayRole, boolean displayEntAttribute, boolean showPwd, boolean showInterestedItem)
        throws cwException
    {
        return getUserShortXML(con, displayRole, displayEntAttribute, showPwd, showInterestedItem, null);
    }

    /*
    *   pls get the db field of reguser table first
    *   and this function will get this group and grade if displayEntAttribute is true
    */
    public StringBuffer getUserShortXML(Connection con, boolean displayRole, boolean displayEntAttribute, boolean showPwd, boolean showInterestedItem, WizbiniLoader wizbini)
        throws cwException
    {
        try {
            StringBuffer result = new StringBuffer();

            result.append("<user id=\"").append(dbUtils.esc4XML(usr_ste_usr_id));
            result.append("\" ent_id=\"").append(usr_ent_id);
            result.append("\" status=\"").append(usr_status);
            //result.append("\" tel_2=\"").append(usr_tel_2);
            result.append("\" last_login=\"").append(usr_last_login_date);
            result.append("\" signup_date=\"").append(usr_signup_date);
            result.append("\" timestamp=\"").append(usr_upd_date).append("\">").append(dbUtils.NEWL);

            if (displayRole){
                AccessControlWZB acl = new AccessControlWZB();
                Timestamp cur_time = cwSQL.getTime(con);
                List roleArray = DbAcRole.getRolesCanLogin(con, usr_ent_id, cur_time);
                if (roleArray == null || roleArray.size()==0)
                    throw  new cwException("Failed to get user role.");
                result.append("<roles>");
                for(int i=0; i<roleArray.size(); i++) {
                	DbAcRole role = (DbAcRole)roleArray.get(i);
                    result.append(AccessControlWZB.getRolXml(role.rol_ext_id));
                }
                result.append("</roles>");
            }

            result.append("<name first_name=\"").append(dbUtils.esc4XML(usr_first_name_bil));
            result.append("\" last_name=\"").append(dbUtils.esc4XML(usr_last_name_bil));
            result.append("\" display_name=\"").append(dbUtils.esc4XML(usr_display_bil));
            result.append("\" usr_nickname=\"").append(dbUtils.esc4XML(cwUtils.escNull(usr_nickname)));
            result.append("\" initial_name=\"").append(dbUtils.esc4XML(usr_initial_name_bil));
            result.append("\" full_name=\"").append(dbUtils.esc4XML(usr_full_name_bil));
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<gender>").append(cwUtils.escNull(usr_gender)).append("</gender>").append(dbUtils.NEWL);

            if (showPwd){
                result.append("<pwd>").append(usr_pwd).append("</pwd>").append(dbUtils.NEWL);
            }
            result.append("<class_number>").append(usr_class_number).append("</class_number>").append(dbUtils.NEWL);
            result.append("<tel tel_1=\"").append(dbUtils.esc4XML(usr_tel_1));
            result.append("\" tel_2=\"").append(dbUtils.esc4XML(usr_tel_2));
            result.append("\" fax_1=\"").append(dbUtils.esc4XML(usr_fax_1));
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<email email_1=\"").append(dbUtils.esc4XML(usr_email));
            result.append("\" email_2=\"").append(dbUtils.esc4XML(usr_email_2));
            result.append("\" />").append(dbUtils.NEWL);

            result.append("<extra_1>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_1))).append("</extra_1>").append(dbUtils.NEWL);
            result.append("<extra_2>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_2))).append("</extra_2>").append(dbUtils.NEWL);
            result.append("<extra_3>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_3))).append("</extra_3>").append(dbUtils.NEWL);
            result.append("<extra_4>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_4))).append("</extra_4>").append(dbUtils.NEWL);
            result.append("<extra_5>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_5))).append("</extra_5>").append(dbUtils.NEWL);
            result.append("<extra_6>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_6))).append("</extra_6>").append(dbUtils.NEWL);
            result.append("<extra_7>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_7))).append("</extra_7>").append(dbUtils.NEWL);
            result.append("<extra_8>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_8))).append("</extra_8>").append(dbUtils.NEWL);
            result.append("<extra_9>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_9))).append("</extra_9>").append(dbUtils.NEWL);
            result.append("<extra_10>").append(dbUtils.esc4XML(cwUtils.escNull(usr_extra_10))).append("</extra_10>").append(dbUtils.NEWL);
            result.append("<extra_datetime_11>").append(usr_extra_datetime_11).append("</extra_datetime_11>");
            result.append("<extra_datetime_12>").append(usr_extra_datetime_12).append("</extra_datetime_12>");
            result.append("<extra_datetime_13>").append(usr_extra_datetime_13).append("</extra_datetime_13>");
            result.append("<extra_datetime_14>").append(usr_extra_datetime_14).append("</extra_datetime_14>");
            result.append("<extra_datetime_15>").append(usr_extra_datetime_15).append("</extra_datetime_15>");
            result.append("<extra_datetime_16>").append(usr_extra_datetime_16).append("</extra_datetime_16>");
            result.append("<extra_datetime_17>").append(usr_extra_datetime_17).append("</extra_datetime_17>");
            result.append("<extra_datetime_18>").append(usr_extra_datetime_18).append("</extra_datetime_18>");
            result.append("<extra_datetime_19>").append(usr_extra_datetime_19).append("</extra_datetime_19>");
            result.append("<extra_datetime_20>").append(usr_extra_datetime_20).append("</extra_datetime_20>");

            result.append("<extra_singleoption_21 id=\"").append(usr_extra_singleoption_21).append("\"/>")
                  .append("<extra_singleoption_22 id=\"").append(usr_extra_singleoption_22).append("\"/>")
                  .append("<extra_singleoption_23 id=\"").append(usr_extra_singleoption_23).append("\"/>")
                  .append("<extra_singleoption_24 id=\"").append(usr_extra_singleoption_24).append("\"/>")
                  .append("<extra_singleoption_25 id=\"").append(usr_extra_singleoption_25).append("\"/>")
                  .append("<extra_singleoption_26 id=\"").append(usr_extra_singleoption_26).append("\"/>")
                  .append("<extra_singleoption_27 id=\"").append(usr_extra_singleoption_27).append("\"/>")
                  .append("<extra_singleoption_28 id=\"").append(usr_extra_singleoption_28).append("\"/>")
                  .append("<extra_singleoption_29 id=\"").append(usr_extra_singleoption_29).append("\"/>")
                  .append("<extra_singleoption_30 id=\"").append(usr_extra_singleoption_30).append("\"/>");

            String[] split = cwUtils.splitToString(usr_extra_multipleoption_31,",",true);
            result.append("<extra_multipleoption_31>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_31>");

            split = cwUtils.splitToString(usr_extra_multipleoption_32,",",true);
            result.append("<extra_multipleoption_32>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_32>");

            split = cwUtils.splitToString(usr_extra_multipleoption_33,",",true);
            result.append("<extra_multipleoption_33>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_33>");

            split = cwUtils.splitToString(usr_extra_multipleoption_34,",",true);
            result.append("<extra_multipleoption_34>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_34>");

            split = cwUtils.splitToString(usr_extra_multipleoption_35,",",true);
            result.append("<extra_multipleoption_35>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_35>");

            split = cwUtils.splitToString(usr_extra_multipleoption_36,",",true);
            result.append("<extra_multipleoption_36>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_36>");

            split = cwUtils.splitToString(usr_extra_multipleoption_37,",",true);
            result.append("<extra_multipleoption_37>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_37>");

            split = cwUtils.splitToString(usr_extra_multipleoption_38,",",true);
            result.append("<extra_multipleoption_38>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_38>");

            split = cwUtils.splitToString(usr_extra_multipleoption_39,",",true);
            result.append("<extra_multipleoption_39>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_39>");

            split = cwUtils.splitToString(usr_extra_multipleoption_40,",",true);
            result.append("<extra_multipleoption_40>");
            for(int i=0;i<split.length;i++){
                result.append("<option id=\"").append(split[i]).append("\"/>");
            }
            result.append("</extra_multipleoption_40>");
            if (displayEntAttribute){
//                RegUser user = new RegUser();
//                user.userEntId = this.usr_ent_id;
                result.append(getEntityAttributesAsXML(con));
            }

            if(showInterestedItem)  {
                result.append(getInterestedItemAsXML(con));
            }

            result.append("<full_path>").append(dbUtils.esc4XML(dbEntityRelation.getFullPath(con, usr_ent_id, wizbini))).append("</full_path>");
			result.append(ViewSuperviseTargetEntity.getDirectSupervisorAsXML(con, usr_ent_id));
			result.append("</user>").append(dbUtils.NEWL);

            return result;

        } catch (SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage());
        }
    }


    /*
    public void checkUserRight(Connection con, String usrId, loginProfile prof) // check whether the user have the right
        throws qdbException, qdbErrMessage
    {

        boolean valid = false;

        if (usrId.equalsIgnoreCase(prof.usr_id)) {
            valid = true;
        }else {
            if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) {
                long rootId = dbRegUser.getRootGpId(con, usrId);
                if (rootId == prof.root_ent_id){
                    return;
                }
            }
        }

        if (!valid) {
                    // No enough right to modify user record
                    throw new qdbErrMessage("USR004");
        }else
            return;

    }
    */

    // check if site user name exists in db
    public boolean checkSiteUsrIdExist(Connection con, long site_id)
        throws cwException
    {
       try {
        PreparedStatement stmt = con.prepareStatement(
              "  SELECT count(*) FROM Reguser "
           +  "  WHERE lower(usr_ste_usr_id) = ? "
           +  "  AND usr_ste_ent_id = ? "
           +  "  AND usr_status != ? ");

        stmt.setString(1, usr_ste_usr_id.toLowerCase());
        stmt.setLong(2,site_id);
        stmt.setString(3, USR_STATUS_DELETED);
        ResultSet rs = stmt.executeQuery();
        boolean bExist = false;
        if(rs.next()) {
            if (rs.getInt(1) > 0)
                bExist = true;
        }else {
        	stmt.close();
            throw new cwException("Failed to get user info.");
        }

        stmt.close();
        return bExist;
       } catch (SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage());
       }
    }
    // 如果当前登录的用户名具有重复用户名，不能让其登录
    public static boolean checkSiteUsrIdCount(Connection con,String usr_ste_usr_id, long site_id)
    		throws cwException
    {
    	try {
    		PreparedStatement stmt = con.prepareStatement(
    				"  SELECT count(*) FROM Reguser "
    						+  "  WHERE lower(usr_ste_usr_id) = ? "
    						+  "  AND usr_ste_ent_id = ? ");
    		
    		stmt.setString(1, usr_ste_usr_id.toLowerCase());
    		stmt.setLong(2,site_id);
    		ResultSet rs = stmt.executeQuery();
    		boolean bExist = false;
    		if(rs.next()) {
    			if (rs.getInt(1) > 1)
    				bExist = true;
    		}else {
    			stmt.close();
    			throw new cwException("Failed to get user info.");
    		}
    		
    		stmt.close();
    		return bExist;
    	} catch (SQLException e) {
    		throw new cwException("SQL Error: " + e.getMessage());
    	}
    }
    
    //用户新增全局检验是否重复用户名
    public boolean checkSiteUsrIdExistToNew(Connection con, long site_id)
    		throws cwException
    {
    	try {
    		PreparedStatement stmt = con.prepareStatement(
    				"  SELECT count(*) FROM Reguser "
    						+  "  WHERE lower(usr_ste_usr_id) = ? "
    						+  "  AND usr_ste_ent_id = ? ");
    		
    		stmt.setString(1, usr_ste_usr_id.toLowerCase());
    		stmt.setLong(2,site_id);
    		
    		ResultSet rs = stmt.executeQuery();
    		boolean bExist = false;
    		if(rs.next()) {
    			if (rs.getInt(1) > 0)
    				bExist = true;
    		}else {
    			stmt.close();
    			throw new cwException("Failed to get user info.");
    		}
    		
    		stmt.close();
    		return bExist;
    	} catch (SQLException e) {
    		throw new cwException("SQL Error: " + e.getMessage());
    	}
    }

	public boolean checkSiteUsrIdExist(Connection con, long site_id, boolean isTcIndependent) throws cwException {
		String sql = "  SELECT count(*) FROM Reguser WHERE usr_ste_usr_id = ? AND usr_ste_ent_id = ? ";
		if (!isTcIndependent) {
			sql += "  AND usr_status != ? ";
		}
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, usr_ste_usr_id);
			stmt.setLong(2, site_id);
			if (!isTcIndependent) {
				stmt.setString(3, USR_STATUS_DELETED);
			}
			ResultSet rs = stmt.executeQuery();
			boolean bExist = false;
			if (rs.next()) {
				if (rs.getInt(1) > 0)
					bExist = true;
			} else {
				stmt.close();
				throw new cwException("Failed to get user info.");
			}

			stmt.close();
			return bExist;
		} catch (SQLException e) {
			throw new cwException("SQL Error: " + e.getMessage());
		}
	}
    // get the usr id and display bil
    public void getByEntId(Connection con)
        throws qdbException
    {
       try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT usr_id, usr_display_bil, usr_ste_ent_id , usr_ste_usr_id FROM RegUser where usr_ent_id = ? " );

            stmt.setLong(1, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                usr_display_bil = rs.getString("usr_display_bil");
                usr_id = rs.getString("usr_id");
                usr_ste_ent_id = rs.getLong("usr_ste_ent_id");
                usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            }
            rs.close();
            stmt.close();
       } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
       }
    }

    public static Vector getByEntIdsAsVec(Connection con, Vector entIdVec)
        throws SQLException {
        Vector v = new Vector();
        dbRegUser usr = null;
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT usr_id, usr_ent_id, usr_display_bil FROM RegUser where usr_ent_id IN ")
            .append(cwUtils.vector2list(entIdVec))
            .append(" ORDER BY usr_display_bil " );
        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            usr = new dbRegUser();
            usr.usr_id = rs.getString("usr_id");
            usr.usr_ent_id = rs.getLong("usr_ent_id");
            usr.usr_display_bil = rs.getString("usr_display_bil");
            v.addElement(usr);
        }
        stmt.close();
        return v;
    }

    public static String usrEntId2UsrId(Connection con, long usr_ent_id) throws SQLException {
        String usr_id=usr_ent_id + "";
        final String SQL = " Select usr_id From RegUser "
                         + " Where usr_ent_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            usr_id = rs.getString("usr_id");
        stmt.close();
        return usr_id;
    }

    public static String usrId2SteUsrId(Connection con, String usr_id) throws SQLException {
        String usr_ste_usr_id=usr_id;
        final String SQL = " Select usr_ste_usr_id From RegUser "
                         + " Where usr_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, usr_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            usr_ste_usr_id = rs.getString("usr_ste_usr_id");
        stmt.close();
        return usr_ste_usr_id;
    }

    public static Hashtable getUsrAndItsGroup(Connection con, Vector usrIds) throws SQLException{
//            String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_id", cwSQL.COL_TYPE_STRING, 30);
//            cwSQL.insertSimpleTempTable(con, tableName, usrIds, cwSQL.COL_TYPE_STRING);

            StringBuffer strBuf = new StringBuffer();

            strBuf.append("('0'");
            for (int i=0;i<usrIds.size();i++) {
                strBuf.append(", '").append(usrIds.elementAt(i).toString()).append("'");
            }
            strBuf.append(")");
            PreparedStatement stmt = con.prepareStatement(
                "SELECT usr_id, usr_ent_id, usr_email, usr_last_name_bil, usr_first_name_bil, "
            		+ " usr_display_bil, usg_display_bil, usr_tel_1 "
            		+ " FROM reguser, usergroup, EntityRelation"
            		+ " WHERE usr_id IN " + strBuf.toString()
            		+ " AND usr_ent_id = ern_child_ent_id AND usg_ent_id = ern_ancestor_ent_id AND ern_parent_ind = ? ");
                stmt.setBoolean(1, true);
                ResultSet rs = stmt.executeQuery();
                Hashtable htUsrId = new Hashtable();
                String usrId = null;
                dbRegUser usr = null;
                while (rs.next()){
                    usrId = rs.getString("usr_id");
                    if (!htUsrId.containsKey(usrId)){
                        usr = new dbRegUser();
                        usr.usr_id = usrId;
                        usr.usr_ent_id = rs.getLong("usr_ent_id");
                        usr.usr_email = rs.getString("usr_email");
                        usr.usr_tel_1 = rs.getString("usr_tel_1");
                        usr.usr_last_name_bil = rs.getString("usr_last_name_bil");
                        usr.usr_first_name_bil = rs.getString("usr_first_name_bil");
                        usr.usr_display_bil = rs.getString("usr_display_bil");
                        usr.usg_display_bil.addElement(rs.getString("usg_display_bil"));
                        htUsrId.put(usrId, usr);
                    }else{
                        usr = (dbRegUser) htUsrId.get(usrId);
                        usr.usg_display_bil.addElement(rs.getString("usg_display_bil"));
                        htUsrId.put(usrId, usr);
                    }
                }
                stmt.close();
                return htUsrId;
            }

    public String usrAndItsGroupAsXML(){
        StringBuffer xmlBuf = new StringBuffer();

        xmlBuf.append("<user ent_id=\"").append(usr_ent_id).append("\" ");
        xmlBuf.append("usr_id=\"").append(usr_id).append("\" ");
        xmlBuf.append("first_name=\"").append(cwUtils.esc4XML(usr_first_name_bil)).append("\" ");
        xmlBuf.append("last_name=\"").append(cwUtils.esc4XML(usr_last_name_bil)).append("\" ");
        xmlBuf.append("tel_1=\"").append(dbUtils.esc4XML(usr_tel_1)).append("\" ");
        xmlBuf.append("grade=\"").append(cwUtils.esc4XML(ugr_display_bil)).append("\">");

        xmlBuf.append(cwUtils.esc4XML(usr_display_bil));
        for (int i=0; i<usg_display_bil.size(); i++){
                xmlBuf.append("<group name=\"").append(cwUtils.esc4XML((String)usg_display_bil.elementAt(i))).append("\" />").append(dbUtils.NEWL);
        }
        xmlBuf.append("</usr>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }

    public static Vector getEmailVec(Connection con, long[] ent_id, String mail_account)
        throws SQLException, cwException {

            StringBuffer str = new StringBuffer().append("(0");
            for(int i=0; i<ent_id.length; i++)
                str.append(",").append(ent_id[i]);
            str.append(")");

            String dbRegUser_GET_EMAILS;

            if( mail_account.equalsIgnoreCase("NOTES") ) {
                dbRegUser_GET_EMAILS = " SELECT usr_email_2 AS EMAIL FROM RegUser "
                                     + " WHERE usr_ent_id IN "
                                     + str.toString();
            } else {
                dbRegUser_GET_EMAILS = " SELECT usr_email AS EMAIL FROM RegUser "
                                     + " WHERE usr_ent_id IN "
                                     + str.toString();
            }
            PreparedStatement stmt = con.prepareStatement(dbRegUser_GET_EMAILS);
            ResultSet rs = stmt.executeQuery();
            Vector emailVec = new Vector();

            while(rs.next()) {
                String email = rs.getString("EMAIL");

                if (email != null && email.length() != 0) {
                    emailVec.addElement(email);
                }
            }
            stmt.close();

            return emailVec;
        }


    //检查是否超出最大的用户数量(true表示超出，False表示未超出)
    public static boolean checkUserNumExceedLimit(Connection con, long site_id)
        throws cwException , cwSysMessage
    {
        try{
        	//获取当前所有的用户的数量
            long curNum = getSiteUserNum(con, site_id);
            //获取允许的最大数量
            long allowedNum = acSite.getMaxUsersAllowed(con, site_id);

            boolean exceed = false;
            if (curNum >= allowedNum) {
            exceed = true;
            }

            return exceed;
       } catch (SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage());
       }
    }

    // Get the number of users in a site
    public static long getSiteUserNum(Connection con, long site_id)
        throws SQLException, cwException {

        String SQL =
              " SELECT count(usr_id) FROM "
            + " Reguser, EntityRelation, UserGroup WHERE "
            + " usr_ent_id = ern_child_ent_id "
            + " AND usg_ent_id = ern_ancestor_ent_id "
            + " AND (usg_ent_id = ? or usg_ent_id_root = ?) "
            + " AND ern_parent_ind = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, site_id);
        stmt.setLong(2, site_id);
        stmt.setBoolean(3, true);
        ResultSet rs = stmt.executeQuery();
        long count ;
        if(rs.next())
            count = rs.getLong(1);
        else {
        	stmt.close();
            throw new cwException ("Failed to count number of users.");
        }
        stmt.close();

        return count;
    }

    // Get the last login time of the user
    public static Timestamp getLastLoginDate(Connection con, String usr_id)
        throws SQLException, cwException {

        Timestamp last_login = null;

        String SQL =
              " SELECT usr_last_login_date FROM RegUser "
            + " WHERE usr_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, usr_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            last_login = rs.getTimestamp("usr_last_login_date");
        else {
        	stmt.close();
            throw new cwException ("Failed to get last login date.");
        }
        stmt.close();

        return last_login;
    }


    //Get the specified users detial
    public String getSelectedUserAsXML(Connection con, long[] ids)
        throws SQLException {

            StringBuffer xml = new StringBuffer();
            xml.append("<users_info>").append(cwUtils.NEWL);

            StringBuffer idstr = new StringBuffer();
            idstr.append("(0");
            for(int i=0; i<ids.length; i++)
                idstr.append(",").append(ids[i]);
            idstr.append(")");

            String SQL = " SELECT usr_id, usr_ent_id, usr_display_bil, "
                       + " usr_ste_usr_id, usr_full_name_bil, usr_initial_name_bil, "
                       + " usr_first_name_bil, usr_last_name_bil "
                       + " FROM RegUser "
                       + " WHERE usr_ent_id IN "
                       + idstr.toString();

            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                xml.append("<user id=\"").append(rs.getString("usr_id")).append("\" ")
                   .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                   .append(" site_usr_id=\"").append(rs.getString("usr_ste_usr_id")).append("\" ")
                   .append(" display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                   .append(" full_name_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_full_name_bil"))).append("\" ")
                   .append(" initial_name_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_initial_name_bil"))).append("\" ")
                   .append(" first_name_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_first_name_bil"))).append("\" ")
                   .append(" last_name_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_last_name_bil"))).append("\"/>")
                   .append(cwUtils.NEWL);
            }

            xml.append("</users_info>").append(cwUtils.NEWL);
            stmt.close();
            return xml.toString();

        }

    // usr_ent_id
    public String getParentUserGroupAsXML(Connection con) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        Vector v_vienr;
        ViewEntityRelation vienr = new ViewEntityRelation();

        vienr.memberEntId = this.usr_ent_id;
        v_vienr = vienr.getParentUserGroup(con);

        if(v_vienr != null) {

            xmlBuf.append("<parent_user_groups>");
            for(int i=0; i<v_vienr.size(); i++) {

                vienr = (ViewEntityRelation) v_vienr.elementAt(i);
                xmlBuf.append("<user_group id=\"").append(vienr.groupEntId).append("\"");
                xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(vienr.groupName)).append("\"");
                xmlBuf.append(" role=\"").append((vienr.groupRole == null) ? "" : vienr.groupRole).append("\"");
                xmlBuf.append(" relation_type=\"").append((vienr.relationType == null) ? "" : vienr.relationType).append("\"/>");
            }
            xmlBuf.append("</parent_user_groups>");
        }
        return xmlBuf.toString();
    }

    // usr_ent_id
    public String getParentUserGroupListAsXML(Connection con) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(512);
        Vector v_vienr;
        ViewEntityRelation vienr = new ViewEntityRelation();

        vienr.memberEntId = this.usr_ent_id;
        v_vienr = vienr.getParentUserGroup(con);

        if(v_vienr != null) {

            xmlBuf.append("<parent_user_group_list>");
            for(int i=0; i<v_vienr.size(); i++) {

                vienr = (ViewEntityRelation) v_vienr.elementAt(i);
                xmlBuf.append("<user_group id=\"").append(vienr.groupEntId).append("\"");
                xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(vienr.groupName)).append("\"");
                xmlBuf.append(" role=\"").append((vienr.groupRole == null) ? "" : vienr.groupRole).append("\"");
                xmlBuf.append(" relation_type=\"").append((vienr.relationType == null) ? "" : vienr.relationType).append("\"/>");
            }
            xmlBuf.append("</parent_user_group_list>");
        }
        return xmlBuf.toString();
    }


    /**
    Get a simple user xml contains ent_id, ste_usr_id and display_bil only<BR>
    Pre-define variables:<BR>
    <ul>
    <li>usr_ent_id
    </ul>
    */
    public String getUserAsXML(Connection con) throws SQLException {
        try {
            StringBuffer xmlBuf = new StringBuffer(128);

            //get the user information
            get(con);
            xmlBuf.append("<entity type=\"").append(dbEntity.ENT_TYPE_USER).append("\"");
            xmlBuf.append(" ent_id=\"").append(usr_ent_id).append("\"");
            xmlBuf.append(" id=\"").append(usr_ste_usr_id).append("\"");
            xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(usr_display_bil)).append("\"/>");
            return xmlBuf.toString();
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
    Get xml of assigned roles of an user<BR>
    pre-define variables:<BR>
    <ul>
    <li>usr_ent_id
    </ul>
    */
    public String getUserRoleAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(256);
        String[] userRoles = dbUtils.getUserRoles(con, this.usr_ent_id);

        xmlBuf.append("<assigned_roles>").append(dbUtils.NEWL);
        if(userRoles != null) {
            for(int i=0; i<userRoles.length; i++) {
                xmlBuf.append("<role id=\"").append(userRoles[i]).append("\"/>").append(dbUtils.NEWL);
            }
        }
        xmlBuf.append("</assigned_roles>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }
    /*
    get the full role list (active and inactive roles)
    */
    public StringBuffer getUserAllRoleAsXML(Connection con) throws SQLException{
        AccessControlWZB acl = new AccessControlWZB();
        AccessControlWZB.ViewEntityRole[] entRole = acl.getUserRoleList(con, usr_ent_id);

        StringBuffer xmlBuf = new StringBuffer(256);
        xmlBuf.append("<usr_role_full_list>").append(dbUtils.NEWL);
        String rol_ext_id;
        Timestamp startDate;
        Timestamp endDate;
        for (int i = 0; i < entRole.length; i++) {
            rol_ext_id = entRole[i].rol_ext_id;
            startDate = entRole[i].erl_eff_start_datetime;
            endDate = entRole[i].erl_eff_end_datetime;

            xmlBuf.append("<role id=\"").append(rol_ext_id)
                    .append("\" eff_start_date=\"").append(dbUtils.isMinTimestamp(startDate) ? dbUtils.IMMEDIATE : startDate.toString())
                    .append("\" eff_end_date=\"").append(dbUtils.isMaxTimestamp(endDate) ? dbUtils.UNLIMITED : endDate.toString())
                    .append("\" />").append(dbUtils.NEWL);
        }
        xmlBuf.append("</usr_role_full_list>").append(dbUtils.NEWL);
        return xmlBuf;
    }



    // all user attribute = ugr + usg + idc + all user classification
    // htRoot can be null if don't need to store root ent id
    public static String[] getAllUserAttributeInOrg(Connection con, long siteId, Hashtable htRoot) throws SQLException{
        Vector vtUserClassificationType = DbUserClassification.getAllUserClassificationTypeInOrg(con, siteId, htRoot);

        String[] all_usr_attributes = new String[vtUserClassificationType.size() + 3];
        all_usr_attributes[0] = dbEntity.ENT_TYPE_USER_GROUP;
        if (htRoot!=null && !htRoot.containsKey(dbEntity.ENT_TYPE_USER_GROUP)){
            htRoot.put(dbEntity.ENT_TYPE_USER_GROUP, new Long(siteId));
        }
        all_usr_attributes[1] = dbEntity.ENT_TYPE_USER_GRADE;
        if (htRoot!=null && !htRoot.containsKey(dbEntity.ENT_TYPE_USER_GRADE)){
            htRoot.put(dbEntity.ENT_TYPE_USER_GRADE, new Long(DbUserGrade.getGradeRoot(con, siteId)));
        }
        all_usr_attributes[2] = dbEntity.ENT_TYPE_INDUSTRY_CODE;
        if (htRoot!=null && !htRoot.containsKey(dbEntity.ENT_TYPE_INDUSTRY_CODE)){
            DbIndustryCode dbidc = new DbIndustryCode();
            dbidc.idc_ent_id_root = siteId;
            dbidc.getRootIndustryCode(con);
            htRoot.put(dbEntity.ENT_TYPE_INDUSTRY_CODE, new Long(dbidc.idc_ent_id));
        }

        for (int i=0; i<vtUserClassificationType.size(); i++){
            all_usr_attributes[3+i] = (String)vtUserClassificationType.elementAt(i);
        }

        return all_usr_attributes;
    }

    public static StringBuffer getAllUserAttributeInOrgAsXML(Connection con, long siteId) throws SQLException{
        StringBuffer xmlBuf = new StringBuffer(256);
        String[] all_usr_attributes = getAllUserAttributeInOrg(con, siteId, null);
        xmlBuf.append("<all_user_attribute_list>");
        for(int i=0; i<all_usr_attributes.length; i++) {
            xmlBuf.append("<attribute_list type=\"").append(all_usr_attributes[i]).append("\" />");
        }
        xmlBuf.append("</all_user_attribute_list>");
        return xmlBuf;
    }

    /**
    get an xml of entity attributes that the user has<BR>
    pre-define variables:
    <ul>
    <li>usr_ent_id
    </ul>
    better with
    <ul>
    <li>usr_ste_ent_id
    </ul>
    </ul>
    */
    public String getEntityAttributesAsXML(Connection con) throws SQLException {
        return getEntityAttributesAsXML(con, false);
    }

    public String getEntityAttributesAsXML(Connection con, boolean getDelLatest) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        Vector v_vienr;
        ViewEntityRelation vienr;

        if (usr_ste_ent_id == 0){
            usr_ste_ent_id = dbRegUser.getSiteEntId(con, usr_ent_id);
        }
        String[] all_usr_attributes = dbRegUser.getAllUserAttributeInOrg(con, usr_ste_ent_id, null);

        //for each of the entity attributes, look up EntityRelation to find parents
        xmlBuf.append("<user_attribute_list>");
        for(int i=0; i<all_usr_attributes.length; i++) {
            v_vienr = getParentEntity(con, all_usr_attributes[i]);

            xmlBuf.append("<attribute_list type=\"").append(all_usr_attributes[i]).append("\">");
            for(int j=0; j<v_vienr.size(); j++) {

                vienr = (ViewEntityRelation) v_vienr.elementAt(j);
                xmlBuf.append("<entity type=\"").append(all_usr_attributes[i]).append("\"");
                xmlBuf.append(" id=\"").append(vienr.groupEntId).append("\"");
                xmlBuf.append(" display_bil=\"").append(dbUtils.esc4XML(vienr.groupName)).append("\"");
                xmlBuf.append(" role=\"").append((vienr.groupRole == null) ? "" : vienr.groupRole).append("\"");
                xmlBuf.append(" relation_type=\"").append(vienr.relationType).append("\"/>");
            }
            xmlBuf.append("</attribute_list>");
        }
        xmlBuf.append("</user_attribute_list>");

        return xmlBuf.toString();
    }

    /**
    get a vector of user's immediate parent entity of entityType<BR>
    pre-define variables:<BR>
    <ul>
    <li>usr_ent_id
    </ul>
    */
    public Vector getParentEntity(Connection con, String entityType) throws SQLException {

        Vector v_vienr;
        ViewEntityRelation vienr = new ViewEntityRelation();
        vienr.memberEntId = this.usr_ent_id;
        vienr.groupEntType = entityType;

        if(entityType.equalsIgnoreCase(dbEntity.ENT_TYPE_INDUSTRY_CODE)) {
            v_vienr = vienr.getParentIndustryCode(con);
        }
        else if(entityType.equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GRADE)) {
            v_vienr = vienr.getParentUserGrade(con);
        }
        else if(entityType.equalsIgnoreCase(dbEntity.ENT_TYPE_USER_GROUP)) {
            v_vienr = vienr.getParentUserGroup(con);
        }
        else {
            v_vienr = vienr.getParentUserClassification(con);
        }

//        else {
//            v_vienr = new Vector();
//        }
        return v_vienr;
    }

    /**
    save EntityRelation relation of a user. <BR>
    compare original set to the new set for each attribute type
    1) keep matched relation
    2) delete relation which is not included in new set
    3) insert new relation
    pre-define variables:<BR>
    <ul>
    <li>usr_ent_id
    </ul>
    @param del_ind delete indicator shows if need to delete records before insertion
     * @throws qdbErrMessage
    */
    public void saveEntityRelation(Connection con,
                                long[] usr_attribute_ent_ids,
                                 String[] usr_attribute_relation_types,
                                 boolean del_ind, String usr_id)
                                throws SQLException, qdbErrMessage {
        Timestamp curTime = cwSQL.getTime(con);

        ViewEntityRelation vienr = new ViewEntityRelation();
        vienr.memberEntId = this.usr_ent_id;

        // get distinct attribute relation type
        Vector vtErnType = new Vector();
        if(usr_attribute_relation_types == null || usr_attribute_ent_ids == null) {
            return;
        }else{
        	// 检测选定的用户组是否存在
        	if (!dbRegUser.isUsgExists(con, usr_attribute_relation_types, usr_attribute_ent_ids)) {
            	throw new qdbErrMessage("USG011");
            }

            for(int i=0; i<usr_attribute_relation_types.length; i++) {
                if (!vtErnType.contains(usr_attribute_relation_types[i])){
                    vtErnType.addElement(usr_attribute_relation_types[i]);
                }
            }
            for (int i=0; i<vtErnType.size(); i++){
                String ernType = (String)vtErnType.elementAt(i);
                dbEntityRelation er = new dbEntityRelation();
                er.ern_type = ernType;
                er.ern_child_ent_id = this.usr_ent_id;
                Vector vtParentId = er.getParentId(con);
                Vector vtExceedId = (Vector)vtParentId.clone();
                for (int j=0; j<vtParentId.size(); j++){
                    Long parentId = (Long) vtParentId.elementAt(j);
                    for (int k=0; k<usr_attribute_ent_ids.length; k++){
                        if (usr_attribute_relation_types[k].equals(ernType) && usr_attribute_ent_ids[k] == parentId.longValue()){
                            // original EntityRelation record matched with the target one
                            // reset the target id means no need to insert new EntityRelation record in part 2
                            usr_attribute_ent_ids[k] = 0;
                            vtExceedId.removeElement(parentId);
                        }
                    }
                }
                if(del_ind) {
                    // delete exceed id
                    for (int j=0; j<vtExceedId.size(); j++){
                    	dbEntityRelation dbEr = new dbEntityRelation();
                    	dbEr.ern_ancestor_ent_id = ((Long) vtExceedId.elementAt(j)).longValue();
                    	dbEr.ern_child_ent_id = this.usr_ent_id;
                    	dbEr.ern_type = ernType;
                    	dbEr.delAsChild(con, usr_id, null);
                    }
                }
                // insert new dbgroupmember
                for (int j=0; j<usr_attribute_ent_ids.length; j++){
                    if (usr_attribute_relation_types[j].equals(ernType) && usr_attribute_ent_ids[j] > 0){
                    	dbEntityRelation dbEr = new dbEntityRelation();
                    	dbEr.ern_child_ent_id = this.usr_ent_id;
                    	dbEr.ern_ancestor_ent_id = usr_attribute_ent_ids[j];
                    	dbEr.ern_type = ernType;
                    	dbEr.ern_syn_timestamp = curTime;
                    	dbEr.insEr(con, usr_id);
                    }
                }
            }
        }
        return;
    }

    /**
    Assign target entity to this user<BR>
    @param rol_ext_ids is the user role that will be assigned to the corresponding targetGroups
    @param targetGroups is array contain the target ent_id groups e.g. {"33,44,55","66,77,88"}
    @param del_ind delete indicator to show if need to delete records before insertion
    */
    private void saveRoleTargetEntity(Connection con,
                                     String[] rol_ext_ids,
                                     String[] targetGroups,
                                     String[] targetStarts,
                                     String[] targetEnds,
                                      String create_usr_id,
                                      boolean del_ind)
                                     throws SQLException {
        if(rol_ext_ids == null || targetGroups == null
           || rol_ext_ids.length != targetGroups.length) {
            return;
        }
        Timestamp[] ts_targetStarts = new Timestamp[rol_ext_ids.length];
        Timestamp[] ts_targetEnds = new Timestamp[rol_ext_ids.length];

        if (targetStarts==null){
            for (int i=0; i<ts_targetStarts.length; i++){
                ts_targetStarts[i] = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
            }
        }else{
            if(targetStarts.length != ts_targetStarts.length){
                return;
            }else{
                for (int i=0; i<targetStarts.length; i++){
                    ts_targetStarts[i] = dbUtils.convertStartDate(targetStarts[i]);
                }
            }
        }

        if (targetEnds==null){
            for (int i=0; i<ts_targetEnds.length; i++){
                ts_targetEnds[i] = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
            }
        }else{
            if(targetEnds.length != ts_targetEnds.length){
                return;
            }else{
                for (int i=0; i<targetEnds.length; i++){
                    ts_targetEnds[i] = dbUtils.convertEndDate(targetEnds[i]);
                }
            }
        }

        //1st clear the user role target entities if del_ind = true
        if(del_ind) {
        Vector v_keys = new Vector();
        String key;
        if(rol_ext_ids != null) {
            for(int i=0; i<rol_ext_ids.length; i++) {
                key = rol_ext_ids[i];
                if(!v_keys.contains(key)) {
                    v_keys.addElement(key);
                    ViewRoleTargetGroup.delByUserRole(con, this.usr_ent_id, key);
                }
            }
        }
        }
        //2nd insert the targetGroups into usrRoleTargetEntity
        Timestamp curTime = cwSQL.getTime(con);

        for(int i=0; i<rol_ext_ids.length; i++) {
            if(targetGroups[i] != null && targetGroups[i].length() > 0 && !targetGroups[i].equals("0")) {

                ViewRoleTargetGroup.insTargetGroups(con, this.usr_ent_id,
                                                    rol_ext_ids[i],
                                                    new String[] {targetGroups[i]},
                                                    new Timestamp[] {ts_targetStarts[i]},
                                                    new Timestamp[] {ts_targetEnds[i]},
                                                    create_usr_id, curTime);
            }
        }
        return;
    }

    public static Hashtable getDisplayName(Connection con, String ent_id_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("SELECT usr_ent_id, usr_display_bil FROM RegUser WHERE usr_ent_id IN " + ent_id_lst);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            hash.put(rs.getString("usr_ent_id"), rs.getString("usr_display_bil"));
        }

        stmt.close();

        return hash;
    }
    private static final String sql_is_usr_manager =
        " select count(*) from EntityRelation, UserGrade " +
        " where ern_child_ent_id = ? " +
        " and ern_ancestor_ent_id = ugr_ent_id " +
        " and ern_type = ? " +
        " and ugr_type = ? " +
        " AND ern_parent_ind = ? ";
        /*
        " Select ugr_ent_id From UserGrade " +
        " Where ugr_type = ? " +
        " And ugr_ent_id in ";
        */
    /**
    Check if this user has a manager grade<BR>
    Pre-define variable:
    <ul>
    <li>usr_ent_id
    </ul>
    */
    public boolean isManager(Connection con) throws SQLException {

        boolean result;
        PreparedStatement stmt = con.prepareStatement(sql_is_usr_manager);
        stmt.setLong(1, this.usr_ent_id);
        stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
        stmt.setString(3, DbUserGrade.UGR_TYPE_MANAGER);
        stmt.setBoolean(4, true);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            result = (rs.getLong(1) > 0);
        }
        else {
            result = false;
        }
        rs.close();
        stmt.close();
        return result;
    }

    public void updEmailNPwd(Connection con) throws SQLException {
        // Deprecated
        /*
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" UPDATE RegUser set usr_email= ? , usr_pwd = ? WHERE usr_ent_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setString(1, usr_email);
        stmt.setString(2, usr_pwd);
        stmt.setLong(3, usr_ent_id);

        stmt.executeUpdate();
        stmt.close();
        */
    }

    public void updPwd(Connection con,loginProfile prof) throws SQLException, qdbException {
        updPwd(con, false, prof);
    }

    public void updPwd(Connection con, boolean bChangePwdAtNextLogon,loginProfile prof) throws SQLException, qdbException {
        
    	String old_db_password = getPassword(con,usr_ent_id); 
    	
    	StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" UPDATE RegUser set usr_pwd = ?, usr_pwd_upd_timestamp = ?, usr_pwd_need_change_ind = ? WHERE usr_ent_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        // perform encryption
        String newPwd = usr_pwd;
        if((usr_id == null || usr_id.equals("")) ||
            (usr_ste_usr_id == null || usr_ste_usr_id.equals(""))) {
            get(con);
        }
        usr_pwd = newPwd;
        usr_pwd = encrypt(usr_pwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
        
        Timestamp time = cwSQL.getTime(con);
        
        // end encryption
        stmt.setString(1, usr_pwd);
        stmt.setTimestamp(2, time);
        stmt.setBoolean(3, bChangePwdAtNextLogon);
        stmt.setLong(4, usr_ent_id);

        stmt.executeUpdate();
        stmt.close();
        
      //将旧密码插入到用户密码历史表UserPasswordHistory
       long updateActor = (prof==null ? usr_ent_id : prof.usr_ent_id);
       insertOldPwdIntoUserPwdHistory(con,usr_ent_id,old_db_password,time,updateActor);
    }

    /**
     * 获取用户数据库密码
     * @param con
     * @param usr_ent_id
     * @return
     * @throws SQLException
     */
    public String getPassword(Connection con,long usr_ent_id) throws SQLException{
    	String result = null;
        String query = " SELECT usr_pwd FROM RegUser WHERE usr_ent_id = ? ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(query);
        	stmt.setLong(1, usr_ent_id);
        	rs = stmt.executeQuery();
            if(rs.next()) {
            	result = rs.getString("usr_pwd");
            }
        }finally{
        	cwSQL.cleanUp(rs, stmt);
        }
        
        return result;
    }
    
    public boolean checkOldPwd(Connection con) throws SQLException, qdbErrMessage, qdbException {
        boolean result = false;
        StringBuffer SQLBuf = new StringBuffer();
        String query = " SELECT usr_pwd FROM RegUser "
                     + " WHERE usr_ent_id = ? ";

        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setLong(1, usr_ent_id);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            String org_pwd = rs.getString("usr_pwd");
            // perform encryption
            String newPwd = usr_pwd;
            if((usr_id == null || usr_id.equals("")) ||
                (usr_ste_usr_id == null || usr_ste_usr_id.equals(""))) {
                get(con);
            }
            usr_pwd = newPwd;
            usr_old_pwd = encrypt(usr_old_pwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
            // end encryption
            // Check if the old password is equal (case-sensitive)
            if (org_pwd != null && org_pwd.equals(usr_old_pwd)) {
                result = true;
            }
        }

        stmt.close();

        if(!result){
        	throw new qdbErrMessage("USR017");
        }

        //检查是否和历史密码相同
        if(!StringUtils.isEmpty(Application.PASSWORD_POLICY_COMPARE_COUNT) && Integer.parseInt(Application.PASSWORD_POLICY_COMPARE_COUNT)>0){
			if(isExistPwd(con,usr_ent_id,Integer.parseInt(Application.PASSWORD_POLICY_COMPARE_COUNT),encrypt(usr_pwd, new StringBuffer(usr_ste_usr_id).reverse().toString()))){
				throw new qdbErrMessage("lab_password_has_existed",Application.PASSWORD_POLICY_COMPARE_COUNT);
			}
		}
        
        return result;
    }

    /**
     * 判断用户@param usr_ent_id的新密码@param newPassword是否在最近的rangeCount已经存在了
     * @param usr_ent_id
     * @param rangeCount
     * @param newPassword
     * @return
     * @throws SQLException 
     */
    private boolean isExistPwd(Connection con,long usr_ent_id, int rangeCount,
			String newPassword) throws SQLException {
    	
    	boolean result = false;
    	
    	String sql = "select uph_pwd from UserPasswordHistory where uph_usr_ent_id = ? order by uph_create_time desc";
    	PreparedStatement statement = null;
    	ResultSet rs = null;
    	try{
    		statement = con.prepareStatement(sql);
    		statement.setLong(1, usr_ent_id);
    		rs = statement.executeQuery();
    		for(int i=0;rs.next()&&i<rangeCount;i++){
    			String uph_pwd = rs.getString("uph_pwd");
    			if(newPassword.equals(uph_pwd)){
    				result = true;
    			}
    		}
    	}finally{
    		cwSQL.cleanUp(rs, statement);
    		rs = null;
    		statement = null;
    	}
    	
    	return result;
    }

	/**
     * 旧密码插入到用户密码历史表UserPasswordHistory
     * @param con
     * @param usr_ent_id2
     * @param org_pwd
     */
    private void insertOldPwdIntoUserPwdHistory(Connection con,
			long usr_ent_id, String org_pwd,Timestamp time,long uph_update_usr_ent_id) throws SQLException{
		
    	PreparedStatement stmt = null;
    	
    	try{
    		StringBuffer sql = new StringBuffer(" insert into UserPasswordHistory ")
			  .append(" values ")
			  .append(" (?,?,?,?,?) ");
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, org_pwd);
			stmt.setLong(index++, uph_update_usr_ent_id);
			stmt.setString(index++, "pc");
			stmt.setTimestamp(index++, time);
			stmt.executeUpdate();
    	}finally{
    		if(stmt != null){
    			try{
    				stmt.close();
    			}finally{
    				stmt = null;
    			}
    		}
    	}
		
	}

	public void updClassNumber(Connection con) throws SQLException {

        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" UPDATE RegUser set usr_class_number= ? WHERE usr_ent_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setString(1, usr_class_number);
        stmt.setLong(2, usr_ent_id);

        stmt.executeUpdate();
        stmt.close();

    }

    /*
    Update login trial
    */
    public static void updateLoginTrial(Connection con, long entId, int numLoginTrial) throws SQLException {

        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" UPDATE RegUser set usr_login_trial = ? WHERE usr_ent_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setInt(1, numLoginTrial);
        stmt.setLong(2, entId);

        stmt.executeUpdate();
        stmt.close();

    }

    /*
    Unlock an locked account. Reset the login trial to 0
    */
    public static void unlockAccount(Connection con, long entId) throws SQLException {

        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" UPDATE RegUser set usr_login_trial = ? WHERE usr_ent_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setInt(1, 0);
        stmt.setLong(2, entId);

        stmt.executeUpdate();
        stmt.close();

    }

    public static void resetInactive(Connection con, long entId) throws SQLException {

        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" UPDATE RegUser set usr_login_status = ?, usr_status = ? WHERE usr_ent_id = ? ");

        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setNull(1, java.sql.Types.VARCHAR);
        stmt.setString(2, USR_STATUS_INACTIVE);
        stmt.setLong(3, entId);

        stmt.executeUpdate();
        stmt.close();

    }

    /*
    Update the last login status
    */
    public static void updLoginStatus (Connection con, String usrId, long site_id, boolean success, String method, boolean bUnlockAccount, boolean bAddLoginTrial, Timestamp curTime, String last_login_role) throws SQLException

    {
//        StringBuffer status_xml = new StringBuffer();
//        status_xml.append("<login_status timestamp=\"").append(curTime)
//                  .append("\" success=\"").append(success)
//                  .append("\" method=\"").append(method).append("\"/>");

        StringBuffer SQLBuf = new StringBuffer();

        PreparedStatement stmt = null;

        //SQLBuf.setLength(128);
        SQLBuf.append("UPDATE RegUser "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
                    + " SET usr_login_status = ? ");
        if(bUnlockAccount) {
            SQLBuf.append(", usr_login_trial = 0 ");
        } else if(bAddLoginTrial) {
            SQLBuf.append(", usr_login_trial = (case when usr_login_trial is null then 0 else usr_login_trial end) + 1 ");
        }

        if(success){
        	SQLBuf.append(", usr_last_login_date = ? ");
        }
        
		if (success && last_login_role != null) {
			SQLBuf.append(", usr_last_login_role = ?");
		}
        SQLBuf.append(" WHERE usr_ste_usr_id = ? and usr_ste_ent_id = ? ");
        stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setString(index++, String.valueOf(success));
        
        if(success){
        	stmt.setTimestamp(index++, curTime);
        }
        
		if (success && last_login_role != null) {
			stmt.setString(index++, last_login_role);
		}
        stmt.setString(index++, usrId);
        stmt.setLong(index++, site_id);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
    find the direct approver of this user
    @param con Connection to databse
    @param rol_ext_id role ext id of the approver
    @return Vector of entity id of approvers
    */
    public Vector myDirectApprover(Connection con, String rol_ext_id)
        throws SQLException {

        Vector user_group_ent_ids = new Vector();
        Vector v_appr_ent_id = new Vector();
        ViewRoleTargetGroup viTgp = new ViewRoleTargetGroup();
        viTgp.rolExtId = rol_ext_id;
        viTgp.targetEntIds = new long[] {this.usr_ent_id} ;
        viTgp.searchDirectUser(con, v_appr_ent_id);
        return v_appr_ent_id;
    }

    public boolean amITargeted(Connection con, long itm_id)
        throws SQLException, cwException {

        Vector targetItmLst = getTargetedItmLst(con);
        return targetItmLst.contains(new Long(itm_id));
    }

    /*  rewritten to getTargetedItmLst
    public boolean amITargeted(Connection con, long itm_id)
        throws SQLException, cwException {

        boolean targeted_ind = false;
        Vector user_group_ent_ids = new Vector();
        Vector user_industry_ent_ids = new Vector();
        Vector user_grade_peer_ent_ids = new Vector();
        Vector v_appr_ent_id = new Vector();

        long grade_ent_id = DbUserGrade.getGradeEntId(con, this.usr_ent_id);
        user_group_ent_ids = dbUserGroup.getUserParentEntIds(con, this.usr_ent_id);
        user_industry_ent_ids = DbIndustryCode.getIndCodeEntIds(con, this.usr_ent_id);
        DbUserGrade.getPeers(con, grade_ent_id, user_grade_peer_ent_ids, new Hashtable());

        //Vector used to store the combination of (group, grade, idc) that will used to search approvers
        Vector combination = new Vector();
        //prepare ViewRoleTargetGroup to find approver
        ViewItemTargetGroup viTgp = new ViewItemTargetGroup();
        viTgp.targetType = ViewItemTargetGroup.TARGETED_LEARNER;
        for (int i=0; i<user_group_ent_ids.size(); i++) {
            combination.addElement(user_group_ent_ids.elementAt(i));

            if (user_grade_peer_ent_ids.size() != 0) {
                for (int j=0; j<user_grade_peer_ent_ids.size(); j++) {
                    Long grade_id = (Long)user_grade_peer_ent_ids.elementAt(j);
                    combination.addElement(user_grade_peer_ent_ids.elementAt(j));

                    if (user_industry_ent_ids.size() != 0) {
                        for (int k=0; k<user_industry_ent_ids.size(); k++) {
                            combination.addElement(user_industry_ent_ids.elementAt(k));
                            viTgp.targetEntIds = dbUtils.vec2longArray(combination);
                            targeted_ind = viTgp.isItemInSearchResult(con, itm_id);
                            combination.removeElement(combination.lastElement());
                            //if I am targeted by one of the combination, no need to go on the search
                            if(targeted_ind) break;
                        }
                    } else {
                        viTgp.targetEntIds = dbUtils.vec2longArray(combination);
                        targeted_ind = viTgp.isItemInSearchResult(con, itm_id);
                    }
                    combination.removeElement(combination.lastElement());
                    //if I am targeted by one of the combination, no need to go on the search
                    if(targeted_ind) break;
                }
            } else {
                viTgp.targetEntIds = dbUtils.vec2longArray(combination);
                targeted_ind = viTgp.isItemInSearchResult(con, itm_id);
            }
            combination.removeElement(combination.lastElement());
            //if I am targeted by one of the combination, no need to go on the search
            if(targeted_ind) break;
        }
        return targeted_ind;
    }
*/
    public Vector getTargetedItmLst(Connection con)
        throws SQLException, cwException {
        Vector targetItmLst = new Vector();
//        boolean targeted_ind = false;
        Vector user_group_ent_ids = new Vector();
        Vector user_industry_ent_ids = new Vector();
        Vector user_grade_peer_ent_ids = new Vector();
        Vector v_appr_ent_id = new Vector();

        long grade_ent_id = DbUserGrade.getGradeEntId(con, this.usr_ent_id);
        user_group_ent_ids = dbUserGroup.getUserParentEntIds(con, this.usr_ent_id);
        user_industry_ent_ids = DbIndustryCode.getIndCodeEntIds(con, this.usr_ent_id);
        if(this.usr_ste_ent_id == 0) {
            this.usr_ste_ent_id = getSiteEntId(con, this.usr_ent_id);
        }
        boolean targetByPeer = acSite.isTargetByPeer(con, this.usr_ste_ent_id);
        if(targetByPeer) {
            DbUserGrade.getPeers(con, grade_ent_id, user_grade_peer_ent_ids, new Hashtable());
        } else {
            user_grade_peer_ent_ids.addElement(new Long(grade_ent_id));
        }

        //Vector used to store the combination of (group, grade, idc) that will used to search approvers
        Vector combination = new Vector();
        //prepare ViewRoleTargetGroup to find approver
        ViewItemTargetGroup viTgp = new ViewItemTargetGroup();
        viTgp.targetType = DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER;
        for (int i=0; i<user_group_ent_ids.size(); i++) {
            combination.addElement(user_group_ent_ids.elementAt(i));

            if (user_grade_peer_ent_ids.size() != 0) {
                for (int j=0; j<user_grade_peer_ent_ids.size(); j++) {
                    Long grade_id = (Long)user_grade_peer_ent_ids.elementAt(j);
                    combination.addElement(user_grade_peer_ent_ids.elementAt(j));

                    if (user_industry_ent_ids.size() != 0) {
                        for (int k=0; k<user_industry_ent_ids.size(); k++) {
                            combination.addElement(user_industry_ent_ids.elementAt(k));
                            viTgp.targetEntIds = dbUtils.vec2longArray(combination);
                            viTgp.searchItem(con, targetItmLst);
                            combination.removeElement(combination.lastElement());
                        }
                    } else {
                        viTgp.targetEntIds = dbUtils.vec2longArray(combination);
                        viTgp.searchItem(con, targetItmLst);
                    }
                    combination.removeElement(combination.lastElement());
                }
            } else {
                viTgp.targetEntIds = dbUtils.vec2longArray(combination);
                viTgp.searchItem(con, targetItmLst);
            }
            combination.removeElement(combination.lastElement());
        }
        return targetItmLst;
    }

    public static loginProfile getSiteDefaultSysProfile(Connection con, loginProfile current_user)
        throws SQLException{
        loginProfile prof;
        try {
            prof = current_user.copy();
            acSite site = new acSite();
            site.ste_ent_id = current_user.root_ent_id;
            prof.usr_ent_id = site.getSiteSysEntId(con);
            dbRegUser usr = new dbRegUser();
            usr.usr_ent_id = prof.usr_ent_id;
            usr.ent_id = usr.usr_ent_id;
            usr.get(con);
            prof.usr_id = usr.usr_id;
        }
        catch(qdbException e) {
            prof = current_user;
        }
        catch(CloneNotSupportedException ce) {
            prof = current_user;
        }
        return prof;
    }


    public static Hashtable getNameTable(Connection con, Vector usrIdVec, long root_ent_id)
        throws SQLException, qdbException {

            Hashtable displayBilTable = new Hashtable();
            if( usrIdVec.isEmpty() )
                return displayBilTable;

            StringBuffer SQL = new StringBuffer();
            SQL.append(" SELECT usr_id, usr_nickname, usr_display_bil ")
               .append(" FROM RegUser WHERE usr_id IN ( ");

            for(int i=0; i<usrIdVec.size(); i++)
                if( i == 0 )
                    SQL.append(" ? ");
                else
                    SQL.append(" , ? ");

            SQL.append(" ) " );


            PreparedStatement stmt = con.prepareStatement(SQL.toString());
            for(int i=0; i<usrIdVec.size(); i++)
                stmt.setString( (i+1), (String)usrIdVec.elementAt(i) );

            ResultSet rs = stmt.executeQuery();
            String name = null;
            while( rs.next() ) {
            	name = rs.getString("usr_nickname");
            	if (name == null || name.length() == 0) {
            		name = rs.getString("usr_display_bil");
            	}
                displayBilTable.put(rs.getString("usr_id"), name);
            }
            stmt.close();

            return displayBilTable;

        }

    public static String getUserName(Connection con, String usr_id)
    throws SQLException  ,cwSysMessage{
        String result;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select usr_display_bil ");
        SQLBuf.append(" From RegUser ");
        SQLBuf.append(" Where usr_id = ? ");

        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, usr_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            result = rs.getString("usr_display_bil");
        else
            //throw new SQLException("Cannot find the user from RegUser. ent_id = " + usr_id);
            throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "User ID = " + usr_id);

        stmt.close();
        return result;
    }



    /*
    * Get user site entity Id by usr_id
    @param String user id
    @return long : site entity id the user belong to
    */
    public static long getSiteEntId(Connection con, String usr_id)
        throws SQLException{

            String SQL = " SELECT usr_ste_ent_id "
                       + " FROM RegUser "
                       + " WHERE usr_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, usr_id);
            ResultSet rs = stmt.executeQuery();
            long site_id = 0 ;
            if( rs.next() )
                site_id = rs.getLong("usr_ste_ent_id");
            else
            {
            	stmt.close();
                throw new SQLException("Failed to get the user's site entity id, usr_id = " + usr_id);
            }

            stmt.close();
            return site_id;
        }

    /*
    * Get user site entity Id by usr_id
    @param String user id
    @return long : site entity id the user belong to
    */
    public static long getSiteEntId(Connection con, long usr_ent_id)
        throws SQLException {

            String SQL = " SELECT usr_ste_ent_id "
                       + " FROM RegUser "
                       + " WHERE usr_ent_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            long site_id = 0 ;
            if( rs.next() )
                site_id = rs.getLong("usr_ste_ent_id");
            else
            {
            	if(rs!=null)rs.close();
            	stmt.close();
                throw new SQLException("Failed to get the user's site entity id, usr_ent_id = " + usr_ent_id);
            }
            rs.close();
            stmt.close();
            return site_id;
        }

    /*
    * Get the guest account of the organization
    @param long site id
    @return String : usr id of the guest account
    */
    public static String getSiteGuestId(Connection con, long site_id)
        throws SQLException {

            String SQL = " SELECT usr_ste_usr_id "
                       + " FROM RegUser, acSite "
                       + " WHERE usr_ent_id = ste_guest_ent_id "
                       + " AND ste_ent_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, site_id);
            ResultSet rs = stmt.executeQuery();
            String usrId = null ;
            if( rs.next() )
                usrId = rs.getString("usr_ste_usr_id");

            stmt.close();
            return usrId;
    }

    private static boolean auth2ldap(String usrId, String passwd, String ldapHost, String ldapDn){
        boolean bSuccess;

            ldapDn = cwUtils.perl.substitute("s#<usr_ste_usr_id>#"+usrId+"#ig", ldapDn);
            CommonLog.debug("ldapDn:" + ldapDn);
        String ldapFullHost = "ldap://" + ldapHost + ":389/";

	    // Set up environment for creating initial context
	    Hashtable htEnv = new Hashtable(11);
	    htEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");

	    //env.put(Context.PROVIDER_URL, "ldap://cw03:389/o=wizq.net");

	    htEnv.put(Context.PROVIDER_URL, ldapFullHost);

	    // Authenticate as S. User and password "mysecret"
	    htEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
	    htEnv.put(Context.SECURITY_PRINCIPAL, ldapDn);
	    htEnv.put(Context.SECURITY_CREDENTIALS, passwd);

	    CommonLog.info("Prepare to authenticate the user");
        long startTime = System.currentTimeMillis();
	    try {

	        // Create initial context
	        DirContext ctx = new InitialDirContext(htEnv);

	        CommonLog.info("Authentication is made successfully");

	        // do something useful with ctx
	        //System.out.println(ctx.lookup("ou=NewHires"));
	        // Change to using no authentication
	        //ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "none");
	        //System.out.println(ctx.lookup("ou=NewHires"));
	        // do something useful with ctx

	        // Close the context when we're done
	        ctx.close();
            bSuccess = true;
	    } catch (NamingException e) {
	    	CommonLog.error(e.getMessage(),e);
	        bSuccess = false;
	    }catch(Exception e){
	    	CommonLog.error(e.getMessage(),e);
	        bSuccess = false;
        }
	    CommonLog.info("End one process in authentication, time spent(ms):[[[[[" + (System.currentTimeMillis() - startTime) + "]]]]]") ;
        return bSuccess;
    }

    public String getDisplayBil(Connection con)
        throws SQLException{
            String display_bil = null;
            if(usr_ent_id>0){
	            String SQL = " SELECT usr_display_bil FROM RegUser "
	                       + " WHERE usr_ent_id = ? ";
	            PreparedStatement stmt = con.prepareStatement(SQL);
	            stmt.setLong(1, usr_ent_id);
	            ResultSet rs = stmt.executeQuery();
	            if(rs.next()){
	                display_bil = rs.getString("usr_display_bil");
	            }
	            else{
	                throw new SQLException("Failed to get user display name, ent id = " + usr_ent_id);
	            }
	            stmt.close();
            }
            return display_bil;
        }

    public static String getDisplayBil(Connection con, long usr_ent_id)
        throws SQLException{

            String SQL = " SELECT usr_display_bil FROM RegUser "
                       + " WHERE usr_ent_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            String display_bil = null;
            if(rs.next())
                display_bil = rs.getString("usr_display_bil");
            else
                throw new SQLException("Failed to get user display name, ent id = " + usr_ent_id);
            stmt.close();
            return display_bil;

        }

    public static String getDisplayBil(Connection con, String usr_id)
        throws SQLException{

            String SQL = " SELECT usr_display_bil FROM RegUser "
                       + " WHERE usr_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, usr_id);
            ResultSet rs = stmt.executeQuery();
            String display_bil = null;
            if(rs.next())
                display_bil = rs.getString("usr_display_bil");
            else
                throw new SQLException("Failed to get user display name, user id = " + usr_id);
            stmt.close();
            return display_bil;

        }
    
    public static String getDisplayBilByEntId(Connection con, long usr_ent_id) throws SQLException{
    	String usr_display_bil = null;
        String SQL = " SELECT usr_display_bil FROM RegUser "
                   + " WHERE usr_ent_id = ? ";
        PreparedStatement stmt = null;
        try {
        	stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, usr_ent_id);
	        ResultSet rs = stmt.executeQuery();
	        if(rs.next()) {
	        	usr_display_bil = rs.getString("usr_display_bil");
        	}
        } finally {
        	if (stmt != null) {
        		stmt.close();
        	}
        }
        return usr_display_bil;

    }
    
    public static String getUsrNickname(Connection con, long usr_ent_id) throws SQLException{
    	String usr_nickname = null;
        String SQL = " SELECT usr_nickname FROM RegUser "
                   + " WHERE usr_ent_id = ? ";
        PreparedStatement stmt = null;
        try {
        	stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, usr_ent_id);
	        ResultSet rs = stmt.executeQuery();
	        if(rs.next()) {
	        	usr_nickname = rs.getString("usr_nickname");
        	}
	        if (usr_nickname == null || usr_nickname.length() == 0) {
	        	usr_nickname = getDisplayBil(con, usr_ent_id);
	        }
        } finally {
        	if (stmt != null) {
        		stmt.close();
        	}
        }
        return usr_nickname;

    }

    public static String getUsrNickname(Connection con, String usr_id) throws SQLException{
    	String usr_nickname = null;
        String SQL = " SELECT usr_nickname FROM RegUser "
                   + " WHERE usr_id = ? ";
        PreparedStatement stmt = null;
        try {
        	stmt = con.prepareStatement(SQL);
	        stmt.setString(1, usr_id);
	        ResultSet rs = stmt.executeQuery();
	        if(rs.next()) {
	        	usr_nickname = rs.getString("usr_nickname");
        	}
	        if (usr_nickname == null || usr_nickname.length() == 0) {
	        	usr_nickname = getDisplayBil(con, usr_id);
	        }
        } finally {
        	if (stmt != null) {
        		stmt.close();
        	}
        }
        return usr_nickname;

    }


    private static final String SQL_UPD_APPROVAL_DATA =
        " update RegUser set " +
        " usr_approve_usr_id = ? " +
        " ,usr_approve_timestamp = ? " +
        " ,usr_approve_reason = ? " +
        " where usr_ent_id = ? ";

    /**
    update the approval data columns of this user
    approval data columns are:
        usr_approve_usr_id;
        usr_approve_timestamp;
        usr_approve_reason.
    @param con Connection to database
    */
    private void updApprovalData(Connection con) throws SQLException {

        PreparedStatement stmt = null;
        try {
//            if(this.usr_approve_timestamp == null) {
//                this.usr_approve_timestamp = cwSQL.getTime(con);
//            }
            stmt = con.prepareStatement(SQL_UPD_APPROVAL_DATA);
            stmt.setString(1, this.usr_approve_usr_id);
            stmt.setTimestamp(2, this.usr_approve_timestamp);
            stmt.setString(3, this.usr_approve_reason);
            stmt.setLong(4, this.usr_ent_id);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    /**
    disapprove a PENDING RegUser account
    @param con Connection to database
    @param prof wizBank login profile
    @param msg_subject subject of the message to be sent, no message will be sent if it is null
     * @throws qdbException 
    */
    public void disapprRegisterUser(Connection con, loginProfile prof, String msg_subject)
        throws SQLException, qdbErrMessage, cwException, qdbException {

    	String display_bil = this.getDisplayBil(con);
		String ste_usr_id = getSteUsrId(con, usr_ent_id);
        //trash this user
        trashUsers(con, prof, new long[] {this.usr_ent_id}, new Timestamp[] {this.usr_upd_date});

        //update user's approve_usr_id, approve_timestamp, aprpove_reason
        this.usr_approve_usr_id = prof.usr_id;
        this.usr_approve_timestamp = cwSQL.getTime(con);
        updApprovalData(con);

        if(MessageTemplate.isActive(con, prof.my_top_tc_id, MG_USR_REG_DISAPPROVE)){
          //插入邮件及邮件内容
            MessageService msgService = new MessageService();
            Timestamp curTime = cwSQL.getTime(con);
            
    		MessageTemplate mtp = new MessageTemplate();
    		mtp.setMtp_tcr_id(prof.my_top_tc_id);
    		mtp.setMtp_type(MG_USR_REG_DISAPPROVE);
    		mtp.getByTcr(con);
    		
            String[] contents = msgService.getApprovUsrMsgContent(con, mtp, display_bil, ste_usr_id, "", usr_approve_reason);
            msgService.insMessage(con, mtp, prof.usr_id,  new long[] {this.usr_ent_id}, new long[0], curTime, contents,0);
        }
        return;
    }

    /**
    approve a PENDING RegUser account
    @param con Connection to database
    @param prof wizBank login profile
    @param vColName Vector of column names
    @param vColType Vector of column types
    @param vColValue Vector of column values
    @param vClobColName Vector of clob column names
    @param vClobColValue Vector of clob column values
    @param msg_subject subject of the message to be sent, no message will be sent if it is null
     * @throws IOException 
    */
    public void apprRegisterUser(Connection con, loginProfile prof,
                             Vector vColName, Vector vColType, Vector vColValue,
                             Vector vClobColName, Vector vClobColValue,
							Vector vExtColName, Vector vExtColType, Vector vExtColValue,
							 Vector vExtClobColName, Vector vExtClobColValue,
                             String msg_subject, boolean tcEnable)
                             throws SQLException,
                                    qdbException, qdbErrMessage,
                                    cwException, cwSysMessage, IOException {

        //update usr_status to "OK"
        int index = vColName.indexOf("usr_status");
        if(index >= 0) {
            vColName.remove(index);
            vColType.remove(index);
            vColValue.remove(index);
        }
        this.usr_status = USR_STATUS_OK;
        vColName.addElement("usr_status");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(this.usr_status);

        //update usr_approve_usr_id to prof.usr_id
        index = vColName.indexOf("usr_approve_usr_id");
        if(index < 0) {
            this.usr_approve_usr_id = prof.usr_id;
            vColName.addElement("usr_approve_usr_id");
            vColType.addElement(DbTable.COL_TYPE_STRING);
            vColValue.addElement(this.usr_approve_usr_id);
        }

        //update usr_approve_timestamp to current database time
        index = vColName.indexOf("usr_approve_timestamp");
        if(index < 0) {
            this.usr_approve_timestamp = cwSQL.getTime(con);
            vColName.addElement("usr_approve_timestamp");
            vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            vColValue.addElement(this.usr_approve_timestamp);
        }

        updUser(con, prof, vColName, vColType, vColValue, vClobColName, vClobColValue,
        			vExtColName, vExtColType, vExtColValue, vExtClobColName, vExtClobColValue, tcEnable);

        //send email
		if(MessageTemplate.isActive(con, prof.my_top_tc_id, MG_USR_REG_APPROVE)){
          //插入邮件及邮件内容
            MessageService msgService = new MessageService();
            Timestamp curTime = cwSQL.getTime(con);
            
    		MessageTemplate mtp = new MessageTemplate();
    		mtp.setMtp_tcr_id(prof.my_top_tc_id);
    		mtp.setMtp_type(MG_USR_REG_APPROVE);
    		mtp.getByTcr(con);
    		
    		String des_usr_pwd = getUsrPwd(con, usr_ent_id);
    		String ste_usr_id = getSteUsrId(con, usr_ent_id);
            String[] contents = msgService.getApprovUsrMsgContent(con, mtp, usr_display_bil, ste_usr_id, des_usr_pwd, "");
            msgService.insMessage(con, mtp, prof.usr_id, new long[]{ent_id}, new long[0], curTime, contents,0);
        }
        return;
    }

	/**
	   Change User Id in Recycle Bin,Reencrypted the password
	   @param con Connection to the database
	   @prof  store the required user information ,eg,usr_ent_id and password which is encrypted
	   */
	public void renameUser(Connection con, loginProfile prof) throws SQLException, IOException, qdbException, qdbErrMessage, cwException {
		super.checkTimeStamp(con);

		this.usr_ste_usr_id = this.usr_id;
		if (checkSiteUsrIdExist(con, prof.root_ent_id)) {
			throw new qdbErrMessage("USR001", this.usr_ste_usr_id);
		}

		this.usr_ste_usr_id = this.usr_id;
		this.upd_usr_id = prof.usr_id;
		this.ent_id = this.usr_ent_id;
		super.upd(con);

		//Get the old usr_ste_usr_id
		//get the old password
		String old_usr_ste_usr_id = null;
		String old_password = null;
		PreparedStatement stmts = con.prepareStatement("select usr_ste_usr_id, usr_pwd from RegUser where usr_ent_id = ? ");
		stmts.setLong(1,usr_ent_id);
		ResultSet rs = stmts.executeQuery();
		if(rs.next()){
			old_usr_ste_usr_id = rs.getString("usr_ste_usr_id");
			old_password = rs.getString("usr_pwd");
		  }
		stmts.close();
		//Decrypt the old password,create a new password which is decrypted
		String tempPwd = decrypt(old_password, new StringBuffer(old_usr_ste_usr_id).reverse().toString());
		//Encrypt the password which just decrypted and create a new  encrypted password
		String newPwd = encrypt(tempPwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
		this.usr_upd_date = this.ent_upd_date;
		//Update the Database for the Reguser. 31th,May
		PreparedStatement stmt = null;
		stmt = con.prepareStatement("UPDATE regUser SET usr_upd_date = ?, usr_ste_usr_id = ?, usr_pwd = ? WHERE usr_ent_id = ?");
		int index = 1;
		stmt.setTimestamp(index++, this.usr_upd_date);
		stmt.setString(index++, this.usr_ste_usr_id);
		stmt.setString(index++,newPwd);
		stmt.setLong(index++, this.usr_ent_id);
		stmt.executeUpdate();
		stmt.close();
		}

	//插入用户和岗位的关联关系
    public void updUserSkill(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("delete from UserPositionRelation where upr_usr_ent_id = ? ");
        stmt.setLong(1, usr_ent_id);
        stmt.executeUpdate();
        stmt.close();

        if (usr_ske_id > 0) {
            int index = 1;
            stmt = con.prepareStatement("insert into UserPositionRelation(upr_usr_ent_id, upr_upt_id) values (?, ?)");
            stmt.setLong(index++, usr_ent_id);
            stmt.setLong(index++, usr_ske_id);
            stmt.execute();
            stmt.close();
        }
    }

	/**
    update a RegUser record
    @param con Connection to database
    @param prof wizBank login profile
    @param vColName Vector of column names
    @param vColType Vector of column types
    @param vColValue Vector of column values
    @param vClobColName Vector of clob column names
    @param vClobColValue Vector of clob column values
    */
    public String updUser(Connection con, loginProfile prof,
                        Vector vColName, Vector vColType, Vector vColValue,
                        Vector vClobColName, Vector vClobColValue,
                        Vector vExtColName,Vector vExtColType, Vector vExtColValue,
						Vector vExtClobColName, Vector vExtClobColValue, boolean tcEnable)
                        throws SQLException,
                               qdbException, qdbErrMessage {

        // concurrency control
        //super.checkTimeStamp(con);
        //update underlying entity
        this.upd_usr_id = prof.usr_id;
        this.ent_id = this.usr_ent_id;
        super.upd(con);
        //set up data for update
        this.usr_upd_date = this.ent_upd_date;
        vColName.addElement("usr_upd_date");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(this.usr_upd_date);
        if (!vColName.contains("usr_syn_rol_ind")){
            vColName.addElement("usr_syn_rol_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(new Boolean(this.usr_syn_rol_ind));
        }

        // checking against aeAppnAPprovalList before update
        boolean hasPendingAppn = DbAppnApprovalList.hasPendingAppn(con, this.usr_ent_id);
        boolean hasPendingApproval = DbAppnApprovalList.hasPendingApprovalAppn(con, this.usr_ent_id);
        if(hasPendingAppn) {
            // check usr_app_approval_usg_ent_id
            if(vColName.contains("usr_app_approval_usg_ent_id")) {
                dbRegUser oriUsr = new dbRegUser();
                oriUsr.usr_ent_id = this.usr_ent_id;
                oriUsr.get(con);
                long usrAppApprovalUsgEntId = 0;
                int index = vColName.indexOf("usr_app_approval_usg_ent_id");
                if(vColValue.elementAt(index)!=null) {
                    usrAppApprovalUsgEntId = ((Long)vColValue.elementAt(index)).longValue();
                }
                if(usrAppApprovalUsgEntId != oriUsr.usr_app_approval_usg_ent_id) {
                	String data = aeApplication.pendingItmTitles(con, this.usr_ent_id,"<br/>");
                    throw new qdbErrMessage(CODE_CANNOT_CHANGE_GROUP_SUPERVISOR,data);
                }
            }

            if (usr_attribute_relation_types != null) {
                // usr_attribute_ent_ids, usr_attribute_relation_types
                Vector newGrp = new Vector();
                for(int i=0; i<usr_attribute_relation_types.length; i++) {
                    if(usr_attribute_relation_types[i].equalsIgnoreCase(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)) {
                        newGrp.add(new Long(usr_attribute_ent_ids[i]));
                    }
                }

                dbEntityRelation dbEr = new dbEntityRelation();
                dbEr.ern_child_ent_id = this.usr_ent_id;
                dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                Vector oriGrp = dbEr.getParentId(con);
                if(!oriGrp.elementAt(0).equals(newGrp.elementAt(0))) {
                    throw new qdbErrMessage(CODE_CANNOT_CHANGE_GROUP);
                }
            }

            // direct_supervisor_ent_ids
			/*
			 * Checks if all the direct supervisors with pending approvals are included in
			 * the updated direct_supervisor_ent_ids.
			 */
			Vector vSupEntId = DbAppnApprovalList.getDirectSupervisorsWithPendingApprovals(con, this.usr_ent_id);
			if(direct_supervisor_ent_ids != null) {
				for (int i=0; i<vSupEntId.size(); i++) {
					long sup_ent_id = ((Long)vSupEntId.elementAt(i)).longValue();
					boolean match = false;
					for (int j=0; j<direct_supervisor_ent_ids.length; j++) {
						if (Long.parseLong(direct_supervisor_ent_ids[j]) == sup_ent_id) {
							match = true;
							break;
						}
					}
					if(!match) {
						throw new qdbErrMessage(CODE_CANNOT_CHANGE_DIRECT_SUPERVISOR);
					}
				}
			}
        }

        if(hasPendingApproval && supervise_target_ent_ids != null) {
			/*
			 * Checks if all the user groups with pending approvals are included in
			 * the updated supervise_target_ent_ids.
			 */
			Vector vUsgEntId = DbAppnApprovalList.getUserGroupsWithPendingApprovals(con, this.usr_ent_id);
			for (int i=0; i<vUsgEntId.size(); i++) {
				long usg_ent_id = ((Long)vUsgEntId.elementAt(i)).longValue();
				boolean match = false;
				for (int j=0; j<supervise_target_ent_ids.length; j++) {
					if (Long.parseLong(supervise_target_ent_ids[j]) == usg_ent_id) {
						match = true;
						break;
					}
				}
				if(!match) {
					throw new qdbErrMessage(CODE_CANNOT_CHANGE_SUPERVISED_GROUP);
				}
			}
        }


        // perform encryption
        if(vColName.contains("usr_pwd") ) {
            int index = 0;
            String usrId = "";
            String usrSteUsrId = "";
            String usrPwd = "";

            index = vColName.indexOf("usr_pwd_upd_timestamp");
            if(vColValue.elementAt(index)!=null) {
                usrId = DateUtil.getInstance().formateDate((Date) vColValue.elementAt(index));
            }
            index = vColName.indexOf("usr_ste_usr_id");
            if(vColValue.elementAt(index)!=null) {
                usrSteUsrId = (String) vColValue.elementAt(index);
            }
            index = vColName.indexOf("usr_pwd");
            if(vColValue.elementAt(index)!=null) {
                usrPwd = (String) vColValue.elementAt(index);
            }
          
            if(!usrId.equals("")&&!usrSteUsrId.equals("")&&!usrPwd.equals("")) {
                usrPwd = encrypt(usrPwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
                vColValue.setElementAt(usrPwd, index);
            }
        }
        // end encryption

        //update RegUser
        DbTable dbTab = new DbTable(con);
        dbTab.upd("RegUser", vColName, vColType, vColValue,
                  this.usr_ent_id, "usr_ent_id");

        //update RegUserExtension
        if(vExtColName.size()>0&&vExtColName.size()>0&&vExtColValue.size()>0){
			dbTab.upd("RegUserExtension", vExtColName, vExtColType, vExtColValue,
                      this.usr_ent_id, "urx_usr_ent_id");
        }
        // update other clob columns
        if (vClobColName != null && vClobColName.size() > 0) {
            String columnName[]  = new String[vClobColName.size()];
            String columnValue[] = new String[vClobColName.size()];
            for(int i = 0; i < vClobColName.size(); i++) {
                columnName[i]  = (String)vClobColName.elementAt(i);
                columnValue[i] = (String)vClobColValue.elementAt(i);
            }
            cwSQL.updateClobFields(con, "RegUser", columnName, columnValue, "usr_ent_id = " + this.usr_ent_id);
        }
        Vector vec = doTaInstrRelative(con, prof.root_ent_id);
        if(vec != null) {
        	StringBuffer result = new StringBuffer();
        	for(int i=0; i<vec.size(); i++) {
        		result.append("<li>").append((String)vec.elementAt(i)).append("</li>");
        	}
        	return result.toString();
        }
        //update user roles
        assignUserRoles(con);
        //save group member relation
        saveEntityRelation(con, usr_attribute_ent_ids,
                        usr_attribute_relation_types, true, prof.usr_id);
        //insert my sub-ordinates
        saveRoleTargetEntity(con, rol_target_ext_ids,
                             rol_target_ent_groups,
                             rol_target_starts, rol_target_ends,
                             this.upd_usr_id, true);
        //insert my approvers
        saveMyApprover(con, appr_rol_ext_ids, appr_ent_ids, this.upd_usr_id, true);

        saveSuperviseTarget(con, direct_supervisor_ent_ids, direct_supervisor_starts, direct_supervisor_ends, DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE, this.upd_usr_id);

        if(supervise_target_ent_ids != null) {
        	Vector supervise_target_ent_vec = cwUtils.String2vector(supervise_target_ent_ids);
        	if(supervise_target_ent_vec.contains(String.valueOf(prof.root_ent_id))) {
        		Hashtable has = convertAllUserGroup(con, supervise_target_ent_ids, supervise_target_starts, supervise_target_ends, prof, tcEnable);
        		if(has != null) {
        			supervise_target_ent_ids = (String[])has.get("ent_ids");
        			supervise_target_starts = (String[])has.get("starts");
        			supervise_target_ends = (String[])has.get("ends");
        		}
        	}
        }
        saveSuperviseTarget(con, supervise_target_ent_ids, supervise_target_starts, supervise_target_ends, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE, this.upd_usr_id);

        updUserSkill(con);

        //to update prof.usr_display_bil when the updated user is login user
        if(this.usr_ent_id == prof.usr_ent_id) {
        	prof.usr_display_bil = this.getDisplayBil(con);
        }

        return null;
    }

    public void updateProfile(Connection con, loginProfile prof,
                              Vector vColName, Vector vColType, Vector vColValue,
                              Vector vClobColName, Vector vClobColValue,
                              Vector vExtColName,Vector vExtColType, Vector vExtColValue,
                              Vector vExtClobColName, Vector vExtClobColValue)
                              throws SQLException,
                                     qdbException, qdbErrMessage {

        // concurrency control
        super.checkTimeStamp(con);
        //update underlying entity
        this.upd_usr_id = prof.usr_id;
        this.ent_id = this.usr_ent_id;
        super.upd(con);
        //set up data for update
        this.usr_upd_date = this.ent_upd_date;
        vColName.addElement("usr_upd_date");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(this.usr_upd_date);
        if (!vColName.contains("usr_syn_rol_ind")){
            vColName.addElement("usr_syn_rol_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(new Boolean(this.usr_syn_rol_ind));
        }

        // perform encryption
        if(vColName.contains("usr_pwd")) {
            int index = 0;
            String usrId = "";
            String usrSteUsrId = "";
            String usrPwd = "";

            index = vColName.indexOf("usr_id");
            if(vColValue.elementAt(index)!=null) {
                usrId = (String) vColValue.elementAt(index);
            }
            index = vColName.indexOf("usr_ste_usr_id");
            if(vColValue.elementAt(index)!=null) {
                usrSteUsrId = (String) vColValue.elementAt(index);
            }
            index = vColName.indexOf("usr_pwd");
            if(vColValue.elementAt(index)!=null) {
                usrPwd = (String) vColValue.elementAt(index);
            }

            if(!usrId.equals("")&&!usrSteUsrId.equals("")&&!usrPwd.equals("")) {
                usrPwd = encrypt(usrPwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
                vColValue.setElementAt(usrPwd, index);
            }
        }
        // end encryption

        //update RegUser
        DbTable dbTab = new DbTable(con);
        if(vColName.size()>0&&vColName.size()>0&&vColValue.size()>0){
            dbTab.upd("RegUser", vColName, vColType, vColValue,
                      this.usr_ent_id, "usr_ent_id");
        }
        //update RegUserExtension
        if(vExtColName.size()>0&&vExtColName.size()>0&&vExtColValue.size()>0){
            dbTab.upd("RegUserExtension", vExtColName, vExtColType, vExtColValue,
                      this.usr_ent_id, "urx_usr_ent_id");
        }

        // update other clob columns
        if (vClobColName != null && vClobColName.size() > 0) {
            String columnName[]  = new String[vClobColName.size()];
            String columnValue[] = new String[vClobColName.size()];
            for(int i = 0; i < vClobColName.size(); i++) {
                columnName[i]  = (String)vClobColName.elementAt(i);
                columnValue[i] = (String)vClobColValue.elementAt(i);
            }
            cwSQL.updateClobFields(con, "RegUser", columnName, columnValue, "usr_ent_id = " + this.usr_ent_id);
        }

        return;
    }

    public void assignInstrRole(Connection con, long root_ent_id) throws SQLException {
        //get instructor role
        String[] usr_roles = new String[] {AcRole.ROLE_INSTR_1};//dbUtils.getOrgInstrRole(con,root_ent_id);
        AccessControlWZB acl = new AccessControlWZB();
        Timestamp startDate = dbUtils.convertStartDate(dbUtils.IMMEDIATE);
        Timestamp endDate = dbUtils.convertEndDate(dbUtils.UNLIMITED);
        for(int i=0; i<usr_roles.length; i++) {
            acl.assignUser2Role(con, this.usr_ent_id, usr_roles[i], startDate, endDate);
        }
    }




//	public void updUsrExtension(Connection con,prof){
//
//	}

    /**
    Register an new user account with usr_status = "PENDING"
    @param con Connection to database
    @param prof wizBank login profile
    @param vColName Vector of column names
    @param vColType Vector of column types
    @param vColValue Vector of column values
    @param vClobColName Vector of clob column names
    @param vClobColValue Vector of clob column values
    @param lsn_itm_ids long array of item id which the user wants to show interest in it
    */
    public void registerUser(Connection con, loginProfile prof,
                             Vector vColName, Vector vColType, Vector vColValue,
                             Vector vClobColName, Vector vClobColValue,
							 Vector vExtColName, Vector vExtColType, Vector vExtColValue,
							 Vector vExtClobColName, Vector vExtClobColValue,
                             long[] lsn_itm_ids, boolean tcEnable)
                             throws SQLException,
                                    qdbException, qdbErrMessage,
                                    cwException, cwSysMessage {

        int index = vColName.indexOf("usr_status");
        if(index >= 0) {
            vColName.remove(index);
            vColType.remove(index);
            vColValue.remove(index);
        }
        vColName.addElement("usr_status");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(USR_STATUS_PENDING);

        insUser(con, prof, vColName, vColType, vColValue, vClobColName, vClobColValue,
        		vExtColName, vExtColType, vExtColValue, vExtClobColName, vExtClobColValue, null, tcEnable);
        saveInterestedItem(con, lsn_itm_ids, prof.usr_id);
        return;
    }

    /**
    Create an new user account with usr_status = "OK"
    if the usr_status is not given in the colums vectors
    @param con Connection to database
    @param prof wizBank login profile
    @param vColName Vector of column names
    @param vColType Vector of column types
    @param vColValue Vector of column values
    @param vClobColName Vector of clob column names
    @param vClobColValue Vector of clob column values
    @param msg_subject subject of the message to be sent, no message will be sent if it is null
    */
    public void insUser(Connection con, loginProfile prof,
                        Vector vColName, Vector vColType, Vector vColValue,
                        Vector vClobColName, Vector vClobColValue,
						Vector vExtColName, Vector vExtColType, Vector vExtColValue,
						Vector vExtClobColName, Vector vExtClobColValue,
                         String msg_subject, boolean tcEnable)
                        throws SQLException,
                               qdbException, qdbErrMessage,
                               cwException, cwSysMessage {

        // Check whether the site user id exist
        int index = vColName.indexOf("usr_ste_usr_id");
        this.usr_ste_ent_id = prof.root_ent_id;
 
        if(index >= 0) {
            this.usr_ste_usr_id = (String)vColValue.elementAt(index);
        }
        //检查登录用户名是否存在
        if (checkSiteUsrIdExistToNew(con, prof.root_ent_id)) {
            throw new qdbErrMessage("USR001", this.usr_ste_usr_id);
        }
        // Check if the new user has role
        if(this.usr_roles == null) {
            throw new qdbException("invalid usr role");
        }
        // Check max number of users exceed
        if (checkUserNumExceedLimit(con, prof.root_ent_id)) {
            throw new cwSysMessage(acSite.MSG_LIC_MAX_USER_EXCEED);
        }
        EnterpriseInfoPortalBean eipBean=null;
        if(qdbAction.wizbini.cfgSysSetupadv.isTcIndependent())
             eipBean=EnterpriseInfoPortalDao.getEipByTcrID(con, prof.my_top_tc_id); 
        if(eipBean!=null&&!prof.current_role.startsWith("ADM") && qdbAction.wizbini.cfgSysSetupadv.isTcIndependent() && prof.my_top_tc_id !=DbTrainingCenter.getSuperTcId ( con, prof.root_ent_id) ) {
            long totalNum = eipBean.getEip_account_num();			
			long emptySize = totalNum - eipBean.getAccount_used();
			if(emptySize <= 0) {
	        	String cur_lan = prof.cur_lan;
	        	String message = LangLabel.getValue(cur_lan, "LN005");
	        	message	+= "(" + LangLabel.getValue(cur_lan, "LN006");
	        	message	+= "/"+ LangLabel.getValue(cur_lan, "LN007");
	        	message	+= "):"+ totalNum +"("+eipBean.getAccount_used()+"/"+ (emptySize) +")";
				throw new cwSysMessage("LN327" , message);
			}
        } 		
        //insert into entity table
        ent_type = dbEntity.ENT_TYPE_USER;
        super.ins(con);

        //set up column data for insert
        this.usr_ent_id = this.ent_id;
        this.upd_usr_id = prof.usr_id;
        this.usr_id = "s" + prof.root_ent_id + "u" + this.ent_id;
        vColName.addElement("usr_id");
        vColType.addElement(DbTable.COL_TYPE_STRING);
        vColValue.addElement(this.usr_id);

        vColName.addElement("usr_ent_id");
        vColType.addElement(DbTable.COL_TYPE_LONG);
        vColValue.addElement(new Long(this.ent_id));

        vColName.addElement("usr_upd_date");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(this.ent_upd_date);

        vColName.addElement("usr_pwd_upd_timestamp");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(this.ent_upd_date);

        vColName.addElement("usr_signup_date");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(this.ent_upd_date);

        vColName.addElement("usr_last_login_date");
        vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
        vColValue.addElement(this.ent_upd_date);

        vColName.addElement("usr_ste_ent_id");
        vColType.addElement(DbTable.COL_TYPE_LONG);
        vColValue.addElement(new Long(this.usr_ste_ent_id));

        //用户尝试登录的次数（新增用户初始为O）
        vColName.addElement("usr_login_trial");
        vColType.addElement(DbTable.COL_TYPE_LONG);
        vColValue.addElement(new Long(0));

		vExtColName.addElement("urx_usr_ent_id");
		vExtColType.addElement(DbTable.COL_TYPE_LONG);
		vExtColValue.addElement(new Long(this.ent_id));

        index = vColName.indexOf("usr_approve_timestamp");
        if(index < 0) {
            this.usr_approve_timestamp = cwSQL.getTime(con);
            vColName.addElement("usr_approve_timestamp");
            vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
            vColValue.addElement(this.usr_approve_timestamp);
        }

        index = vColName.indexOf("usr_status");
        if(index < 0) {
            vColName.addElement("usr_status");
            vColType.addElement(DbTable.COL_TYPE_STRING);
            vColValue.addElement(USR_STATUS_OK);
        }

        if (!vColName.contains("usr_syn_rol_ind")){
            vColName.addElement("usr_syn_rol_ind");
            vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
            vColValue.addElement(new Boolean(this.usr_syn_rol_ind));
        }

        // perform encryption（密码加密）
        if(vColName.contains("usr_pwd")) {
            index = 0;
            String usrId = "";
            String usrSteUsrId = "";
            String usrPwd = "";

            index = vColName.indexOf("usr_id");
            if(vColValue.elementAt(index)!=null) {
                usrId = (String) vColValue.elementAt(index);
            }
            index = vColName.indexOf("usr_ste_usr_id");
            if(vColValue.elementAt(index)!=null) {
                usrSteUsrId = (String) vColValue.elementAt(index);
            }
            index = vColName.indexOf("usr_pwd");
            if(vColValue.elementAt(index)!=null) {
                usrPwd = (String) vColValue.elementAt(index);
            }

            if(!usrId.equals("")&&!usrSteUsrId.equals("")&&!usrPwd.equals("")) {
                usrPwd = encrypt(usrPwd, new StringBuffer(usr_ste_usr_id).reverse().toString());
                vColValue.setElementAt(usrPwd, index);
            }
        }
        // end encryption

        // add usr_source in colData to record usr_source in database
	    vColName.addElement("usr_source");
	    vColType.addElement(DbTable.COL_TYPE_STRING);
	    vColValue.addElement(USER_SOURCE_WIZBANK);
        

        //insert into RegUser
        DbTable dbTab = new DbTable(con);
        dbTab.ins("RegUser", vColName, vColType, vColValue);
		dbTab.ins("RegUserExtension", vExtColName, vExtColType, vExtColValue);
        // update other clob columns
        if (vClobColName != null && vClobColName.size() > 0) {
            String columnName[]  = new String[vClobColName.size()];
            String columnValue[] = new String[vClobColName.size()];
            for(int i = 0; i < vClobColName.size(); i++) {
                columnName[i]  = (String)vClobColName.elementAt(i);
                columnValue[i] = (String)vClobColValue.elementAt(i);
            }
            cwSQL.updateClobFields(con, "RegUser", columnName, columnValue, "usr_ent_id = " + this.usr_ent_id);
        }

        //insert user roles
        assignUserRoles(con);
        //insert group member relation
        saveEntityRelation(con, usr_attribute_ent_ids,
                        usr_attribute_relation_types, false, prof.usr_id);
        //insert my sub-ordinates
        saveRoleTargetEntity(con, rol_target_ext_ids, rol_target_ent_groups,
                                rol_target_starts, rol_target_ends, this.upd_usr_id, false);

        saveSuperviseTarget(con, direct_supervisor_ent_ids, direct_supervisor_starts, direct_supervisor_ends, DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE, this.upd_usr_id);

        if(supervise_target_ent_ids != null) {
        	Vector supervise_target_ent_vec = cwUtils.String2vector(supervise_target_ent_ids);
        	if(supervise_target_ent_vec.contains(String.valueOf(prof.root_ent_id))) {
        		Hashtable has = convertAllUserGroup(con, supervise_target_ent_ids, supervise_target_starts, supervise_target_ends, prof, tcEnable);
        		if(has != null) {
        			supervise_target_ent_ids = (String[])has.get("ent_ids");
        			supervise_target_starts = (String[])has.get("starts");
        			supervise_target_ends = (String[])has.get("ends");
        		}
        	}
        }

        saveSuperviseTarget(con, supervise_target_ent_ids, supervise_target_starts, supervise_target_ends, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE, this.upd_usr_id);

        //insert my approvers
        saveMyApprover(con, appr_rol_ext_ids, appr_ent_ids, this.upd_usr_id, false);

        //send mail
        if(msg_subject != null) {
            sendMailToNewUser(con, prof.usr_id, msg_subject);
        }
        //插入用户
        updUserSkill(con);

        return;
    }
    /**
    Insert approvers for this user
    Pre-define variable: usr_ent_id
    @param con Connection to database
    @param appr_rol_ext_ids role ext id of the approvers
    @param appr_ent_ids entity id of the each role of approvers (e.g. {"1,212","22,11,112"})
    @param create_usr_id create user id of the records
    @param del_ind indicate if need to clear the records before insert
    */
    private void saveMyApprover(Connection con,
                                String[] appr_rol_ext_ids,
                                String[] appr_ent_ids,
                                String create_usr_id,
                                boolean del_ind) throws SQLException {

        if(appr_rol_ext_ids != null) {
            Timestamp curTime = cwSQL.getTime(con);
            Timestamp minTimestamp = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
            Timestamp maxTimestamp = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
            Vector vDeletedApprRole = new Vector();
            for(int i=0; i<appr_rol_ext_ids.length; i++) {
                if(del_ind) {
                    //1st, delete the approver
                    if(!vDeletedApprRole.contains(appr_rol_ext_ids[i])) {
                        ViewRoleTargetGroup.delBySubordinate(con, appr_rol_ext_ids[i], this.usr_ent_id) ;
                        vDeletedApprRole.addElement(appr_rol_ext_ids[i]);
                    }
                }
                if(appr_ent_ids[i] != null) {
                    StringTokenizer st_appr_ent_id = new StringTokenizer(appr_ent_ids[i],",");
                    //2nd, insert the new approvers
                    AccessControlWZB acl = new AccessControlWZB();
                    while(st_appr_ent_id.hasMoreTokens()) {
                        long appr_ent_id = Long.parseLong(st_appr_ent_id.nextToken());
                        //if the approver does not belongs to that role
                        //assign the user to that role
                        if(!acl.hasUserRole(con, appr_ent_id, appr_rol_ext_ids[i])) {
                            acl.assignUser2Role(con, appr_ent_id, appr_rol_ext_ids[i], minTimestamp, maxTimestamp);
                        }
                        ViewRoleTargetGroup.insTargetGroups(con,
                                                            appr_ent_id,
                                                            appr_rol_ext_ids[i],
                                                            new String[] {this.usr_ent_id+""},
                                                            new Timestamp[] {minTimestamp},
                                                            new Timestamp[] {maxTimestamp},
                                                            create_usr_id, curTime);
                    }
                }
            }
        }
        return;
    }

    /**
    Send email to this newly created user
    @param con Connection to database
    @param sender_usr_id message sender's usr_id
    @param msg_subject message subject
    */
    private void sendMailToNewUser(Connection con, String sender_usr_id, String msg_subject)
        throws SQLException, cwException {
        //set up message parameters
        Vector paramsName  = new Vector();
        Vector paramsType  = new Vector();
        Vector paramsValue = new Vector();
        Vector params = new Vector();
        paramsName.addElement("cmd");
        paramsName.addElement("usr_ent_id");
        paramsName.addElement("sender_id");
        //paramsName.addElement("site_id");
        paramsType.addElement(Message.STATIC);
        paramsType.addElement(Message.STATIC);
        paramsType.addElement(Message.STATIC);
        //paramsType.addElement(Message.STATIC);
        paramsValue.addElement("usr_creation_xml");
        paramsValue.addElement("" + this.usr_ent_id);
        paramsValue.addElement(sender_usr_id);
        //paramsValue.addElement("" + this.usr_ste_ent_id);
        params.addElement(paramsName);
        params.addElement(paramsType);
        params.addElement(paramsValue);
        //set up message record for table mgMessage
        DbMgMessage dbMsg = new DbMgMessage();
        Timestamp curTime = cwSQL.getTime(con);
        dbMsg.msg_send_usr_id = sender_usr_id;
        dbMsg.msg_subject = msg_subject;
        dbMsg.msg_addition_note = null;
        dbMsg.msg_target_datetime = curTime;
        dbMsg.msg_create_usr_id = sender_usr_id;
        dbMsg.msg_create_timestamp = curTime;
        dbMsg.msg_update_usr_id = sender_usr_id;
        dbMsg.msg_update_timestamp = curTime;
        dbMsg.msg_bcc_sys_ind = false;
        //insert message records
        Message msg = new Message();
        msg.insNotify(con, new long[] {this.usr_ent_id}, null, MG_USR_CREATION, new String[] {"HTML"}, dbMsg, params, false);
        return;
    }

    /**
    save the interested items for this PENDING user
    @parma con Connection con database
    @param lsn_itm_ids long array of interest item ids
    @param create_usr_id audit trial information
    */
    public void saveInterestedItem(Connection con, long[] lsn_itm_ids, String create_usr_id) throws SQLException {
        //for each lsn_itm_id, insert into aeLearningSoln
        if(lsn_itm_ids != null) {
            int size = lsn_itm_ids.length;
            for(int i=0; i<size; i++) {
                DbLearningSoln lsn = new DbLearningSoln();
                lsn.lsn_ent_id = this.usr_ent_id;
                lsn.lsn_itm_id = lsn_itm_ids[i];
                lsn.lsn_period_id = 0;
                lsn.lsn_ent_id_lst = " 0 ";
                lsn.lsn_create_usr_id = create_usr_id;
                lsn.lsn_create_timestamp = this.usr_signup_date;
                lsn.lsn_upd_usr_id = create_usr_id;
                lsn.lsn_upd_timestamp = this.usr_signup_date;
                lsn.lsn_type = DbLearningSoln.LSN_TYPE_USER_REGISTRATION;
                lsn.ins(con);
            }
        }
        return;
    }

    public String getUserRegApproveXML(Connection con, loginProfile prof)
        throws SQLException, qdbException, cwException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        //append user detail xml
        xmlBuf.append(getUserXML(con, prof));
        //append item information xml
        xmlBuf.append(getInterestedItemAsXML(con));
        return xmlBuf.toString();
    }

    private String getInterestedItemAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        Vector vItem = aeItem.getInterestedItem(con, usr_ent_id, DbLearningSoln.LSN_TYPE_USER_REGISTRATION);
        int size = vItem.size();
        xmlBuf.append("<item_list>");
        for(int i=0; i<size; i++) {
            aeItem itm = (aeItem) vItem.elementAt(i);
            xmlBuf.append("<item id=\"").append(itm.itm_id).append("\"")
                  .append(" code=\"").append(itm.itm_code).append("\"")
                  .append(" title=\"").append(cwUtils.esc4XML(cwUtils.escNull(itm.itm_title))).append("\"/>");
        }
        xmlBuf.append("</item_list>");

        return xmlBuf.toString();
    }

   public static String[] getApproverRolExtId(Connection con) throws SQLException {
        Vector result = new Vector();
        PreparedStatement stmt = con.prepareStatement("SELECT rol_ext_id FROM ACROLE WHERE rol_target_ent_type is not null");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            result.addElement(rs.getString("rol_ext_id"));
        }
 	    String[] rol_ext_id = new String[result.size()];
        for(int i=0; i<rol_ext_id.length; i++){
            rol_ext_id[i] = (String)result.elementAt(i);
    	}
        stmt.close();
        return rol_ext_id;
    }

    /*
    Check wether the user need to change password at this logon
    ste_usr_pwd_valid_period in acSite the max life (no. of days) of user password
    usr_pwd_upd_timestamp in RegUser specified the update timestamp of the user password
    usr_pwd_need_change_ind override all the other rule
    // If usr_pwd_need_change_ind is true, then need to change password
    // If usr_pwd_upd_timestamp > ste_usr_pwd_valid_period , then password is expired
    */
    public static boolean checkNeedToChangePwd(Connection con, long usr_ent_id) throws SQLException {

        PreparedStatement stmt = con.prepareStatement("SELECT usr_pwd_need_change_ind, usr_pwd_upd_timestamp, ste_usr_pwd_valid_period "
           + " From acSite, RegUser "
           + " Where ste_ent_id = usr_ste_ent_id and usr_ent_id = ? ");

        stmt.setLong(1, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        boolean bNeed = true;
        if (rs.next()) {
            if (!rs.getBoolean("usr_pwd_need_change_ind")) {

                String valid_period = rs.getString("ste_usr_pwd_valid_period");
                Timestamp upd_datetime = rs.getTimestamp("usr_pwd_upd_timestamp");

                if (valid_period == null || upd_datetime == null) {
                    bNeed = false;
                }else {
                    Timestamp curTime = cwSQL.getTime(con);
                    long timeDiff = curTime.getTime() - upd_datetime.getTime();
                    if (timeDiff < (Long.parseLong(valid_period) * 24 * 60 * 60 * 1000)) {
                        bNeed = false;
                    }
                }
            }
        }

        stmt.close();
        return bNeed;
    }




    public String getUserListAsXml(Connection con, Hashtable entIdRole)
        throws SQLException, cwException {
            Enumeration enumeration = entIdRole.keys();
            String ent_id_list = " ( 0 ";
            while(enumeration.hasMoreElements())
                ent_id_list += "," + enumeration.nextElement();
            ent_id_list += " ) ";
            String SQL = " SELECT usr_ent_id, usr_display_bil "
                       + " FROM RegUser "
                       + " WHERE usr_ent_id IN " + ent_id_list;
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            StringBuffer xml = new StringBuffer();
            Vector roleIdVec = null;
            xml.append("<user_list>");
            while(rs.next()){
                Long entId = new Long(rs.getLong("usr_ent_id"));
                xml.append("<user ")
                   .append(" ent_id=\"").append(entId).append("\" ")
                   .append(" display_bil=\"").append(cwUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                   .append(">");
                if( entIdRole.containsKey(entId) ) {
                    roleIdVec = (Vector)entIdRole.get(entId);
                    if( roleIdVec != null && !roleIdVec.isEmpty() ) {
                        xml.append("<role_list>");
                        for(int i=0; i<roleIdVec.size(); i++)
                            xml.append("<role>").append(roleIdVec.elementAt(i)).append("</role>");
                        xml.append("</role_list>");
                    }
                }
                xml.append("</user>");
            }
            xml.append("</user_list>");
            stmt.close();
            return xml.toString();

        }

    // prefined usr_ent_id
    public void getUsrSynInfo(Connection con) throws SQLException{
        String sql = "SELECT ent_syn_ind, usr_syn_rol_ind, usr_not_syn_gpm_type FROM RegUser, Entity WHERE ent_id = usr_ent_id AND usr_ent_id = ? ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql);
        stmt.setLong(1, usr_ent_id);
            rs = stmt.executeQuery();
        if (rs.next()){
            this.ent_syn_ind = rs.getBoolean("ent_syn_ind");
            this.usr_syn_rol_ind = rs.getBoolean("usr_syn_rol_ind");
            this.usr_not_syn_gpm_type = rs.getString("usr_not_syn_gpm_type");
        }else{
            throw new SQLException("Failed to get user syn info." + usr_ent_id);
        }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public void restore(Connection con, loginProfile prof) throws SQLException, cwException, qdbException, qdbErrMessage,cwSysMessage {
        get(con);
        if (checkSiteUsrIdExist(con, prof.root_ent_id)) {
            throw new qdbErrMessage("USR001", usr_ste_usr_id);
        }
        
        // Check max number of users exceed
        if (checkUserNumExceedLimit(con, prof.root_ent_id)) {
            throw new cwSysMessage(acSite.MSG_LIC_MAX_USER_EXCEED);
        }
        //check the user's group is still exist
        if(!dbEntityRelationHistory.isExistUserGroup(con, usr_ent_id)){
        	throw new qdbErrMessage("USR020");
        }
        dbEntity dbent = new dbEntity();
        dbent.ent_id = usr_ent_id;
        dbent.getById(con);
        Timestamp deleteTime = dbent.ent_delete_timestamp;
        dbent.unDelete(con);
        PreparedStatement stmt2 = con.prepareStatement(
                " SELECT count(*) from RegUser "
                + " where usr_ent_id = ? ");

        stmt2.setLong(1, usr_ent_id);

        ResultSet rs2 = stmt2.executeQuery();
        if (rs2.next()) {
            int cnt = rs2.getInt(1);
            if (cnt == 1) {
                dbRegUser dbusr = new dbRegUser();
                dbusr.ent_id = usr_ent_id;
                dbusr.usr_ent_id = usr_ent_id;
                dbusr.changeStatus(con,dbRegUser.USR_STATUS_OK);
            }
        }else {
            stmt2.close();
            throw new qdbException("Failed to check user record.");
        }
        //恢复用户与用户组/职级等的关系，null代表所有关系
        dbEntityRelation dbEr = new dbEntityRelation();
        dbEr.restoreEntityRelation(con, prof.usr_id, usr_ent_id, deleteTime, null);
        stmt2.close();
    }

    public static String getUserAttributeInfoXML(WizbiniLoader wizbini, String root_id) throws cwException{
        StringWriter writer = new StringWriter();
        wizbini.marshal(((UserManagement)wizbini.cfgOrgUserManagement.get(root_id)).getUserProfile(), writer);
        String user_attribute_xml = writer.toString();
        user_attribute_xml = user_attribute_xml.substring(user_attribute_xml.indexOf("<", 2));
        return user_attribute_xml;
    }
    
    public static boolean checkUserPwd(Connection con,String pwd,long usr_ent_id) throws SQLException{
    	String sql = "select usr_pwd from RegUser where usr_pwd = ? and usr_ent_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, pwd);
            stmt.setLong(2, usr_ent_id);
            rs = stmt.executeQuery();
	        if (rs.next()){
	            return true;
	        }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return false;
    }

    public static long getUserRoleMinAuthLevel(Connection con, long usr_ent_id)
    	throws SQLException, cwSysMessage {

    		String SQL = " Select MIN(rol_auth_level) " +
    					"from acEntityRole, acRole " +
    					"Where erl_ent_id = ? " +
    					"And erl_rol_id = rol_id ";
    		PreparedStatement stmt = con.prepareStatement(SQL);
    		stmt.setLong(1, usr_ent_id);
    		ResultSet rs = stmt.executeQuery();
    		long auth_level = 0;
    		if(rs.next()){
    			auth_level = rs.getLong(1);
    		}else{
    			stmt.close();
				throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "User Entity ID = " + usr_ent_id);
    		}
    		stmt.close();
    		return auth_level;
    	}

    public void saveSuperviseTarget(Connection con,
        String[] superviseEntIds, String[] superviseStarts, String[] superviseEnds, String supervise_type, String upd_usr_id) throws SQLException{

        if (superviseEntIds==null){
            return;
        }
        Timestamp[] tsStarts = new Timestamp[superviseEntIds.length];
        Timestamp[] tsEnds = new Timestamp[superviseEntIds.length];

        if (superviseStarts==null){
            for (int i=0; i<tsStarts.length; i++){
                tsStarts[i] = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
            }
        }else{
            for (int i=0; i<superviseStarts.length; i++){
                    tsStarts[i] = dbUtils.convertStartDate(superviseStarts[i]);
            }
        }

        if (superviseEnds==null){
            for (int i=0; i<tsEnds.length; i++){
                tsEnds[i] = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
            }
        }else{
            for (int i=0; i<superviseEnds.length; i++){
                tsEnds[i] = dbUtils.convertEndDate(superviseEnds[i]);
            }
        }
        if (supervise_type.equals(DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE)){
//            System.out.println("clean direct supervisor " + usr_ent_id);
            DbSuperviseTargetEntity.delByTargetEntId(con, usr_ent_id, supervise_type);

            for (int i=0; i<superviseEntIds.length; i++){
                DbSuperviseTargetEntity dbspt = new DbSuperviseTargetEntity();
                dbspt.spt_source_usr_ent_id = Long.parseLong(superviseEntIds[i]);
                dbspt.spt_type = supervise_type;
                dbspt.spt_target_ent_id = usr_ent_id;
                dbspt.spt_eff_start_datetime = tsStarts[i];
                dbspt.spt_eff_end_datetime = tsEnds[i];
                dbspt.ins(con, upd_usr_id);
//                System.out.println("add direct supervisor to " + usr_ent_id + " " + superviseEntIds[i]);
            }
        }else if (supervise_type.equals(DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE)){
//            System.out.println("clean supervised " + usr_ent_id);
            DbSuperviseTargetEntity.delBySourceEntId(con, usr_ent_id, supervise_type);
            for (int i=0; i<superviseEntIds.length; i++){
                DbSuperviseTargetEntity dbspt = new DbSuperviseTargetEntity();
                dbspt.spt_source_usr_ent_id = usr_ent_id;
                dbspt.spt_type = supervise_type;
                dbspt.spt_target_ent_id = Long.parseLong(superviseEntIds[i]);
                dbspt.spt_eff_start_datetime = tsStarts[i];
                dbspt.spt_eff_end_datetime = tsEnds[i];
                dbspt.ins(con, upd_usr_id);
//                System.out.println("add supervised group to " + usr_ent_id + " " + superviseEntIds[i]);
            }
        }
    }

    // group_code -> usr_attribute_ent_ids
    // grade_code -> usr_attribute_ent_ids
    // role_code_lst -> usr_role_lst
    // direct_supervisor_usr_lst -> direct_supervisor_ent_ids
    // supervise_target_group_code_lst -> supervise_target_ent_ids

    private String transformUserInfo4AffLogin(Connection con, boolean isCreate, long siteId, WizbiniLoader wizbini) throws qdbException, qdbErrMessage, SQLException, cwException{
        Vector vtAttrEntId = new Vector();
        Vector vtAttrType = new Vector();

        // usr id
        if (this.usr_id!=null){
            if (isCreate){
                if (!checkLength(usr_id, 0, 20)){
                    return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_ID + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
                }
                if (!validateUserId(usr_id)){
                    return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_ID + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
                }
            }
        }else{
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_ID + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_REQD;
        }
        // USR PWD
        if (this.usr_pwd != null){
            if (!checkLength(usr_pwd, 4, 20)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_PWD + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
            }
            if (!validatePassword(usr_pwd)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_PWD + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
            }
        }
        // usr display_bil
        if (this.usr_display_bil != null){
            if (!checkLength(usr_display_bil, 0, 255)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_DISPLAY_BIL + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
            }
        }else{
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_DISPLAY_BIL + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_REQD;
        }

        // USR GENDER
        if (this.usr_gender != null){
            this.usr_gender = this.usr_gender.toUpperCase();
            if (!validateGender(usr_gender)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_GENDER + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_VALUE;
            }
        }
        //USR BDAY
        //latest update(christ):add
        if(!this.is_valid_usr_bday){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_BDAY + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT ;
        }
        // USR_EMAIL
        if (this.usr_email != null){
            if (!checkLength(usr_email, 0, 255)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EMAIL + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
            }
        }

        // USR_TEL_1
        if (this.usr_tel_1 != null){
            if (!checkLength(usr_tel_1, 0, 50)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_TEL_1 + CODE_FIELDS_NOT_VALID_SEPARATOR+ CODE_FIELDS_ERROR_TYPE_LENGTH;
            }
        }

        // USR_FAX_1
        if (this.usr_fax_1 != null){
            if (!checkLength(usr_fax_1, 0, 50)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_FAX_1 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
            }
        }

        // USR_job_title
        if (this.usr_job_title != null){
            if (!checkLength(usr_job_title, 0, 255)){
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_JOB_TITLE + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
            }
        }

        // -----for grade----
        if (this.grade_code == null) {
            // use default grade
            if (isCreate){
                DbUserGrade dbugr = DbUserGrade.getDefaultGrade(con, siteId);
                long newGrade = dbugr.ugr_ent_id;
                vtAttrEntId.addElement(new Long(newGrade));
                vtAttrType.addElement(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
            }
        }else{
            DbUserGrade dbugr = new DbUserGrade();
            dbugr.ent_ste_uid = this.grade_code;
            dbugr.ugr_ent_id_root = siteId;
            dbugr.getBySteUid(con);
            if (dbugr.ugr_ent_id == 0){
                System.err.println("grade code not exst:" + grade_code);
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_GRADE_CODE + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_VALUE ;
            }else{
                vtAttrEntId.addElement(new Long(dbugr.ugr_ent_id));
                vtAttrType.addElement(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
            }
        }
        // -----for group----
        if (this.group_code == null ) {
            if (isCreate){
                // Add the user to the organization
                // group id  : new folder for each month e.g. 2001-10
                // Format the current time.

                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM");
                String groupname = formatter.format(new Date(System.currentTimeMillis()));
                dbUserGroup dbusg = new dbUserGroup();
                dbusg.usg_display_bil=groupname;
                dbusg.usg_ent_id_root = siteId;
                dbusg.ent_ste_uid = groupname;
                long newGroup = dbusg.checkAndCreateGroup(con, siteId, this.usr_ste_usr_id);
                vtAttrEntId.addElement(new Long(newGroup));
                vtAttrType.addElement(dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            }
        }else {
            dbUserGroup dbusg = new dbUserGroup();
            dbusg.ent_ste_uid        = this.group_code;
            dbusg.usg_ent_id_root    = siteId;
            dbusg.getEntIdBySteUid(con);
            if (dbusg.usg_ent_id == 0){
                System.err.println("group code not exst:" + group_code);
                return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_GROUP_CODE + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_VALUE;
            }else{
                vtAttrEntId.addElement(new Long(dbusg.usg_ent_id));
                vtAttrType.addElement(dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            }
        }
        // -----for group & grade----
        if (vtAttrEntId.size() > 0){
            usr_attribute_ent_ids = new long[vtAttrEntId.size()];
            for (int i=0; i<vtAttrEntId.size(); i++){
                usr_attribute_ent_ids[i] = ((Long)vtAttrEntId.elementAt(i)).longValue();
            }
        }

        if (vtAttrType.size() > 0){
            usr_attribute_relation_types = new String[vtAttrType.size()];
            for (int i=0; i<vtAttrType.size(); i++){
                usr_attribute_relation_types[i] = (String)vtAttrType.elementAt(i);
            }
        }
        // for direct_supervisor
        if (this.direct_supervisor_usr_lst != null){
            Vector vtStrDirectSupervisorEntId = new Vector();
            for (int i=0; i<direct_supervisor_usr_lst.length; i++){
                long tmpUsrEntId = dbRegUser.getEntId(con, direct_supervisor_usr_lst[i], siteId);
                if (tmpUsrEntId == 0){
                    System.err.println("invalid supervisor id: " + direct_supervisor_usr_lst[i]);
                    return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_DIRECT_SUPERVISOR_USR + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_VALUE;
                }else{
                    String str_ent_id = new Long(tmpUsrEntId).toString();
                    if (!vtStrDirectSupervisorEntId.contains(str_ent_id)){
                        vtStrDirectSupervisorEntId.addElement(str_ent_id);
                    }
                }
            }
            direct_supervisor_ent_ids = new String[vtStrDirectSupervisorEntId.size()];
            for (int i=0; i<vtStrDirectSupervisorEntId.size(); i++){
                direct_supervisor_ent_ids[i] = (String)vtStrDirectSupervisorEntId.elementAt(i);
            }

        }
        // JOIN DATE
        //latest update(christ):add
        if(!this.is_valid_usr_join_datetime){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_JOIN_DATE + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT ;
        }
        // -----for role----
        if (this.role_code_lst == null || this.role_code_lst.length == 0){
            if (isCreate){
                // use default role
                String defaultRole = AccessControlWZB.getDefaultRoleBySite(con, siteId);
                usr_roles = new String[] {defaultRole};
            }
        }else{
            Vector vtRole = new Vector();
            AccessControlWZB acl = new AccessControlWZB();
            //get all role in a specified site
            Hashtable htRole = acl.getAllRoleUid(con, siteId, "rol_ste_uid");
            for (int i=0; i<role_code_lst.length; i++){
                //check if the role provided by Single Signon is exist in current site
                String role = (String)htRole.get(role_code_lst[i].toUpperCase());
                if (role == null){//if not exist return error code!
                    System.err.println("invalid role code not exst:" + role_code_lst[i]);
                    return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_ROLE_CODE + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_VALUE;
                }else{
                    if (!vtRole.contains(role)){
                        vtRole.addElement(role);
                    }
                }
            }
            usr_roles = new String[vtRole.size()];
            for (int i=0; i<vtRole.size(); i++){
                usr_roles[i] = (String)vtRole.elementAt(i);
            }

        }

        // for supervised group
        if (this.supervise_target_group_code_lst != null){
            Vector vtStrSpvEntId = new Vector();
            for (int i=0; i<supervise_target_group_code_lst.length; i++){
                dbUserGroup dbgrp        = new dbUserGroup();
                dbgrp.ent_ste_uid        = supervise_target_group_code_lst[i];
                dbgrp.usg_ent_id_root    = siteId;
                dbgrp.getEntIdBySteUid(con);
                if (dbgrp.usg_ent_id ==0){
                    System.err.println("invalid supervised group code: " + supervise_target_group_code_lst[i]);
                    return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_SUPERVISE_TARGET_GROUP_CODE + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_VALUE;
                }else{
                    String strSpvEntId = new Long(dbgrp.usg_ent_id).toString();
                    if (!vtStrSpvEntId.contains(strSpvEntId)){
                        vtStrSpvEntId.addElement(strSpvEntId);
                    }
                }
            }
            supervise_target_ent_ids = new String[vtStrSpvEntId.size()];
            for (int i=0; i<vtStrSpvEntId.size(); i++){
                supervise_target_ent_ids[i] = (String) vtStrSpvEntId.elementAt(i);
            }
        }
        // USR_EXTRA
        if (!checkLength(usr_extra_1, 0, 255))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_1 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_2, 0, 255))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_2 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_3, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_3 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_4, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_4 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_5, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_5 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_6, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_6 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_7, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_7 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_8, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_8 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_9, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_9 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if (!checkLength(usr_extra_10, 0, 50))
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_10 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
        if(!is_valid_usr_extra_datetime_11){
           return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_11 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_12){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_12 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_13){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_13 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_14){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_14 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_15){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_15 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_16){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_16 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_17){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_17 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_18){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_18 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_19){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_19 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if(!is_valid_usr_extra_datetime_20){
            return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_DATETIME_20 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_FORMAT;
        }
        if (!checkLength(usr_extra_singleoption_21, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_21 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_22, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_22 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_23, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_23 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_24, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_24 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_25, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_25 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_26, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_26 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_27, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_27 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_28, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_28 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_29, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_29 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_singleoption_30, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_SINGLEOPTION_30 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_31, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_31 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_32, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_32 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_33, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_33 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_34, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_34 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_35, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_35 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_36, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_36 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_37, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_37 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_38, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_38 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_39, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_39 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}
        if (!checkLength(usr_extra_multipleoption_40, 0, 255)){
        	 return CODE_FIELDS_NOT_VALID + CODE_FIELDS_NOT_VALID_SEPARATOR + FIELD_CODE_USR_EXTRA_MULTIPLEOPTION_40 + CODE_FIELDS_NOT_VALID_SEPARATOR + CODE_FIELDS_ERROR_TYPE_LENGTH;
		}

        return null;
    }

    public static boolean validateUserId(String usr_id){
        boolean bValid = true;

        for (int i=0; bValid && i<usr_id.length(); i++) {
            if ((usr_id.charAt(i) >= 'A' && usr_id.charAt(i) <= 'Z') ||(usr_id.charAt(i) >= 'a' && usr_id.charAt(i) <= 'z') || (usr_id.charAt(i) >= '0' && usr_id.charAt(i) <= '9')
                || usr_id.charAt(i) == '-' || usr_id.charAt(i) == '_'){
                // ok
            }else{
               bValid = false;
            }
        }
        return bValid;

    }

    public static boolean validatePassword(String password){

        boolean bValid = true;

        for (int i=0; bValid && i<password.length(); i++) {
            if ((password.charAt(i) >= 'a' && password.charAt(i) <= 'z')
                || (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z')
                || (password.charAt(i) >= '0' && password.charAt(i) <= '9')
                || password.charAt(i) == '-' || password.charAt(i) == '_'){
                // ok
            }else{
               bValid = false;
            }
        }
        return bValid;
    }

    public static boolean validateGender(String gender){
        boolean bValid;
        if (gender == null){
            bValid = true;
        }else{
            bValid = gender.equals("M") || gender.equals("F");
        }
        return bValid;
    }

    public static boolean checkLength(String inString, int min, int max){
        boolean bValid = true;
        if (inString == null){
            bValid = min == 0;
        }else{
            bValid = inString.length() >= min && inString.length() <= max;
        }
        return bValid;
    }
    
    public static boolean validateGroupCode(String group_code){

        boolean bValid = true;
        
        if(group_code != null && (group_code.contains(",") || group_code.contains("，"))){
        	bValid = false;
        }
        return bValid;
    }
    
    public static boolean validateGroupTitle(String group_title){

        boolean bValid = true;
        
        if(group_title != null && group_title.contains("/") || group_title.contains("\\")){
        	bValid = false;
        }
        return bValid;
    }


    // do not upd upd_timestamp , upd_user of reguser rec and entity rec
    // update one field only
    public void updAppApprovalUsgEntId(Connection con) throws SQLException{
        String sql = "UPDATE RegUser set usr_app_approval_usg_ent_id = ? WHERE usr_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        if(usr_app_approval_usg_ent_id==0) {
            stmt.setNull(1, java.sql.Types.INTEGER);
        } else {
            stmt.setLong(1, usr_app_approval_usg_ent_id);
        }
        stmt.setLong(2, usr_ent_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public long getAppApprovalUsgEntId(Connection con) throws SQLException{
        String sql = "SELECT usr_app_approval_usg_ent_id FROM RegUser WHERE usr_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, usr_ent_id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            usr_app_approval_usg_ent_id = rs.getLong("usr_app_approval_usg_ent_id");
        }else{
        	stmt.close();
            throw new SQLException("dbRegUser.getAppApprovalUsgEntId : usr not found, usr_ent_id = " + usr_ent_id);
        }
        stmt.close();
        return usr_app_approval_usg_ent_id;
    }

    public static Vector getUpdatedUser(Connection con, long root_ent_id, Timestamp startTime, Timestamp endTime, String usr_source) throws SQLException{
        Vector vtEntId = new Vector();
        String sql = "SELECT usr_ent_id FROM reguser WHERE usr_ste_ent_id = ? AND usr_status = ? ";
        if (startTime != null)
            sql += " and usr_upd_date > ? ";
        if (endTime != null)
            sql += " and usr_upd_date <= ? ";
        if (usr_source !=null) {
            sql += " and usr_source = ? ";
        }

        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, root_ent_id);
        stmt.setString(index++, USR_STATUS_OK);
        if (startTime != null)
            stmt.setTimestamp(index++, startTime);
        if (endTime != null)
            stmt.setTimestamp(index++, endTime);
        if (usr_source != null) {
            stmt.setString(index++, usr_source);
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            vtEntId.addElement(new Long(rs.getLong("usr_ent_id")));
        }
        stmt.close();
        return vtEntId;
    }

    public static Hashtable getGradeByUsrEntIds(Connection con, Vector vtUsrEntId) throws SQLException{
        Hashtable htUserGrade = new Hashtable();
        if (vtUsrEntId.size()==0){
            return htUserGrade;
        }
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_ent_id", cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, vtUsrEntId, cwSQL.COL_TYPE_LONG);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ugr_display_bil, ugr_ent_id, ent_ste_uid, ern_child_ent_id ");
        sql.append(" FROM UserGrade, Entity, EntityRelation ");
        sql.append(" WHERE ern_type = ? ");
        sql.append(" AND ern_child_ent_id in (SELECT tmp_usr_ent_id FROM " + tableName + ")");
        sql.append(" AND ern_ancestor_ent_id = ugr_ent_id AND ugr_ent_id = ent_id ");
        sql.append(" AND ern_parent_ind = ? ");

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setString(1, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
        stmt.setBoolean(2, true);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            long usr_ent_id = rs.getLong("ern_child_ent_id");
            DbUserGrade ugr = new DbUserGrade();
            ugr.ugr_ent_id = rs.getLong("ugr_ent_id");
            ugr.ent_id = ugr.ugr_ent_id;
            ugr.ent_ste_uid = rs.getString("ent_ste_uid");
            ugr.ugr_display_bil = rs.getString("ugr_display_bil");
            htUserGrade.put(new Long(usr_ent_id), ugr);
        }
        stmt.close();
        cwSQL.dropTempTable(con,tableName);
        return htUserGrade;
    }

    // return hashtable, usr_ste_usr_id as key, vector of rol_ste_uid as value
    public static Hashtable getUserRolesUid(Connection con, Vector vtUsrEntId) throws SQLException{
        Hashtable htUserRoleUid = new Hashtable();
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_ent_id", cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, vtUsrEntId, cwSQL.COL_TYPE_LONG);
        String sql = "SELECT usr_ste_usr_id, rol_ste_uid FROM acEntityRole, acRole , RegUser WHERE ";
        sql += " usr_ent_id IN (SELECT tmp_usr_ent_id FROM "+ tableName + ") AND usr_ent_id = erl_ent_id AND rol_id = erl_rol_id and rol_ste_uid is not null ";

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            String usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            Vector vtRoleUid = (Vector)htUserRoleUid.get(usr_ste_usr_id);
            if (vtRoleUid==null){
                vtRoleUid = new Vector();
            }
            vtRoleUid.addElement(rs.getString("rol_ste_uid"));
            htUserRoleUid.put(usr_ste_usr_id, vtRoleUid);
        }
        stmt.close();
        cwSQL.dropTempTable(con,tableName);
        return htUserRoleUid;
    }


    public static Hashtable getUserAppApprovalGroup(Connection con, Vector vtUsrEntId) throws SQLException{
        Hashtable htUserAppApprovalGroup = new Hashtable();
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_ent_id", cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, vtUsrEntId, cwSQL.COL_TYPE_LONG);
        String sql = OuterJoinSqlStatements.getUsrAppApprovalUsgCode(tableName);

        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            String usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            String group_code = rs.getString("ent_ste_uid");
            if (group_code==null)
                group_code = "";
            htUserAppApprovalGroup.put(usr_ste_usr_id, group_code);
        }
        stmt.close();
        cwSQL.dropTempTable(con,tableName);
        return htUserAppApprovalGroup;
    }

    private static final String ProviderSunJCE = "com.sun.crypto.provider.SunJCE";
    private static final String ProviderSUN = "sun.security.provider.Sun";
    public static String encrypt(String str, String encryptionKey)
        throws qdbException {
            try{
                Key key = null;
                Cipher cipher = null;

                //Generate a DES KEY
                Security.addProvider((Provider)Class.forName(ProviderSunJCE).newInstance());
                Security.addProvider((Provider)Class.forName(ProviderSUN).newInstance());
                KeyGenerator generator= KeyGenerator.getInstance("DES");
                SecureRandom srn = SecureRandom.getInstance("SHA1PRNG", "SUN");
                srn.setSeed(encryptionKey.getBytes());
                generator.init(srn);
                key = generator.generateKey();

			    byte[] iv = new byte[8];
			    srn.nextBytes(iv);
			    IvParameterSpec params = new IvParameterSpec(iv, 0, 8);

			    //Generates a Cipher object that implements DES transformation
			    cipher = Cipher.getInstance("DES/OFB16/NoPadding");
			    cipher.init(Cipher.ENCRYPT_MODE,key, params);
			    byte[] stringBytes = str.getBytes();
			    byte[] raw = cipher.doFinal(stringBytes);

			    BASE64Encoder encoder = new BASE64Encoder();
			    return new String(encoder.encode(raw));
			} catch (ClassNotFoundException e) {
			    throw new qdbException("Failed to init encryption provider(ClassNotFoundException)" + e.getMessage());
			} catch (InstantiationException e) {
			    throw new qdbException("Failed to init encryption provider(InstantiationException)" + e.getMessage());
			} catch (IllegalAccessException e) {
			    throw new qdbException("Failed to init encryption provider(IllegalAccessException)" + e.getMessage());
			} catch (NoSuchProviderException e) {
			    throw new qdbException("Failed to init encryption provider(NoSuchProviderException)" + e.getMessage());
            }catch(NoSuchPaddingException pe) {
                throw new qdbException("Failed to init encrypt : " + pe.getMessage());
            }catch(NoSuchAlgorithmException ae) {
                throw new qdbException("Failed to init encrypt : " + ae.getMessage());
            }catch(InvalidKeyException ke){
                throw new qdbException("Failed to encrypt string : " + ke.getMessage());
            }catch(IllegalBlockSizeException se){
                throw new qdbException("Failed to encrypt string : " + se.getMessage());
            }catch(BadPaddingException pe){
                throw new qdbException("Failed to encrypt string : " + pe.getMessage());
            }catch(InvalidAlgorithmParameterException iape){
                throw new qdbException("Failed to encrypt string : " + iape.getMessage());
            }
        }

	/**
	 * David Qiu Weiping   updated 31th,May
	 * @param str  the supplied encrypted password
	 * @param encryptionKey
	 * @return the Decrypted password
	 * @throws qdbException if there occured such Exception
	 * @throws IOException if there occured such Exception
	 */
	public static String decrypt(String str, String encryptionKey)
		throws qdbException,IOException {
		try {
			Key key = null;
			Cipher cipher = null;

			//Generate a DES KEY
			Security.addProvider(
				(Provider) Class.forName(ProviderSunJCE).newInstance());
			Security.addProvider(
				(Provider) Class.forName(ProviderSUN).newInstance());
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom srn = SecureRandom.getInstance("SHA1PRNG", "SUN");
			srn.setSeed(encryptionKey.getBytes());
			generator.init(srn);
			key = generator.generateKey();

			byte[] iv = new byte[8];
			srn.nextBytes(iv);
			IvParameterSpec params = new IvParameterSpec(iv, 0, 8);
			cipher = Cipher.getInstance("DES/OFB16/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key, params);
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] raw = cipher.doFinal(decoder.decodeBuffer(str));
			String result = new String(raw, "ISO-8859-1");
			//return the decrypted password
			return result;
		} catch (ClassNotFoundException e) {
			throw new qdbException(
				"Failed to init encryption provider(ClassNotFoundException)"
					+ e.getMessage());
		} catch (InstantiationException e) {
			throw new qdbException(
				"Failed to init encryption provider(InstantiationException)"
					+ e.getMessage());
		} catch (IllegalAccessException e) {
			throw new qdbException(
				"Failed to init encryption provider(IllegalAccessException)"
					+ e.getMessage());
		} catch (NoSuchProviderException e) {
			throw new qdbException(
				"Failed to init encryption provider(NoSuchProviderException)"
					+ e.getMessage());
		} catch (NoSuchPaddingException pe) {
			throw new qdbException(
				"Failed to init encrypt : " + pe.getMessage());
		} catch (NoSuchAlgorithmException ae) {
			throw new qdbException(
				"Failed to init encrypt : " + ae.getMessage());
		} catch (InvalidKeyException ke) {
			throw new qdbException(
				"Failed to encrypt string : " + ke.getMessage());
		} catch (IllegalBlockSizeException se) {
			throw new qdbException(
				"Failed to encrypt string : " + se.getMessage());
		} catch (BadPaddingException pe) {
			throw new qdbException(
				"Failed to encrypt string : " + pe.getMessage());
		} catch (InvalidAlgorithmParameterException iape) {
			throw new qdbException(
				"Failed to encrypt string : " + iape.getMessage());
		}
	}

	private static final String SQL_GET_STE_USR_ID
		= "select usr_ste_usr_id from RegUser where usr_ent_id=?";

	public static String getSteUsrId (Connection con, long usr_ent_id) throws SQLException {
		PreparedStatement stmt = null;
		String result = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL_GET_STE_USR_ID);
			stmt.setLong(1, usr_ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = rs.getString("usr_ste_usr_id");
			}
		} finally {
			if (stmt != null) stmt.close();
		}
		return result;
	}



    public static RegUser getUsrBySteUsrId(Connection con,String usr_ste_usr_id){
        PreparedStatement stmt = null;
        RegUser regUser=null;
        ResultSet rs = null;
        String sql="select usr_status,usr_ent_id from RegUser where usr_ste_usr_id=?";

        try {
            stmt=con.prepareStatement(sql);
            stmt.setString(1,usr_ste_usr_id);
            rs=stmt.executeQuery();
            if(rs.next()){
                 regUser=new RegUser();
                regUser.setUsr_status(rs.getString("usr_status"));
                regUser.setUsr_ent_id(rs.getLong("usr_ent_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regUser;
    }
    /**
     * return a count of a user's roles
     *
     */
    public static int getUserRoleCount(Connection con, String usr_ste_usr_id, long root_ent_id) throws SQLException {
        // TODO Auto-generated method stub
        int roleCount = 0;
        String sql = "select count(*) from acEntityRole " +
                     "where erl_ent_id = (select usr_ent_id from reguser where usr_ste_usr_id=? " +
                     "and usr_ste_ent_id = ? and usr_status <> ? )";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, usr_ste_usr_id);
        pstmt.setLong(2, root_ent_id);
        pstmt.setString(3, USR_STATUS_DELETED);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            roleCount = rs.getInt(1);
        }
        cwSQL.cleanUp(rs, pstmt);
        return roleCount;
    }

    public static long getUserRoleCountByUserId(Connection con, long usr_ent_id) throws SQLException {
        long roleCount = 0;
        String sql = "select count(*) from acEntityRole " +
                     "where erl_ent_id = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1, usr_ent_id);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            roleCount = rs.getLong(1);
        }
        cwSQL.cleanUp(rs, pstmt);
        return roleCount;
    }
    public static long isActive(Connection con,  String steUsrId, long site_id)
            throws qdbErrMessage, qdbException, cwException, cwSysMessage {

        try {
            long ent_id = 0;
            String query = "SELECT max(usr_ent_id) usr_ent_id FROM regUser "
                    + " WHERE usr_status = ? AND usr_ste_usr_id = ?  AND usr_ste_ent_id = ? ";


            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, USR_STATUS_OK);
            stmt.setString(2, steUsrId);
            stmt.setLong(3, site_id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ent_id = rs.getLong(1);
            }
            stmt.close();

            return ent_id;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public Vector doTaInstrRelative(Connection con, long root_ent_id) throws SQLException {
      	boolean ta_flag = false;
    	boolean instr_flag = false;
    	String rol_ext_id = "";
        Timestamp cur_time = cwSQL.getTime(con);
        List roleArray = DbAcRole.getRolesCanLogin(con, this.usr_ent_id, cur_time);
        //登录用户角色与用户修改页面传入的角色数量
    	List roleList = new ArrayList();
    	List endRoleList = new ArrayList();
    	Map map = new HashMap();
        if(roleArray.size()>0&&roleArray!=null){
	        	for (int i = 0; i < roleArray.size(); i++) {
	        		DbAcRole role = (DbAcRole)roleArray.get(i);
	        		if(usr_roles!=null){
						for (int j = 0; j < usr_roles.length; j++) {
							//roleList.add(role.rol_ext_id);
							if(role.rol_ext_id.equals(usr_roles[j])){
								map.put(role.rol_ext_id, role.rol_ext_id);
								continue;
							}
						}
						if(!map.containsKey(role.rol_ext_id)){
							roleList.add(role.rol_ext_id);
						}
	        		}
				}
	        //修改的角色和用户当前已有的角色有变动信息	
	        if(roleList.size()>0&&roleList!=null){
		        for (int i = 0; i < roleList.size(); i++) {
		        	endRoleList.add(roleList.get(i));
				}
	        }
        } 
    	if(usr_roles != null && usr_roles.length > 0) {
	        for(int i=0; i<usr_roles.length; i++) {        	
	        	if(AccessControlWZB.isRoleTcInd(usr_roles[i])) {
	        		ta_flag = true;
	        	}
	        	if(AccessControlWZB.isIstRole(usr_roles[i])) {
	        		instr_flag = true;
	        	}
	        }
	        if(!ta_flag) {
	    		AccessControlWZB acl = new AccessControlWZB();
	    		Map availableRoles = acl.getTcOfficerRole(con,root_ent_id);
	            // check if the update on user role will result in
	            // items have no officer
	            Set keySet = availableRoles.keySet();
	            for(Iterator iter=keySet.iterator();iter.hasNext();){
	                //get officer role xml
	                rol_ext_id = iter.next().toString();
	                Vector vSoleItem = ViewTrainingCenter.getSoleItemWithTheOnlyOfficer(con, usr_ent_id, rol_ext_id);
	                if(vSoleItem != null && vSoleItem.size() > 0) {
	                	return vSoleItem;
	                }
	            }
	            //删除培训中心管理关系中用户没有的角色
	            if(endRoleList.size()>0&&endRoleList!=null){
		            for (int i = 0; i < endRoleList.size(); i++) {
		            	ViewTrainingCenter.delOfficerRoleFromTc(con,usr_ent_id,endRoleList.get(i).toString());
		            }
	            }
	        	aeItemAccess.delByEntIdAndRole(con, this.usr_ent_id, rol_ext_id);
	        }
	        if(!instr_flag) {
	        	//Instructor.removeIntrForAllCourse(con, usr_ent_id, root_ent_id);
	        }
    	}

    	return null;
    }
    
    
    public Vector checkTaRelative(Connection con, long root_ent_id) throws SQLException {
      	boolean ta_flag = false;
    	boolean instr_flag = false;
    	String rol_ext_id = "";
        Timestamp cur_time = cwSQL.getTime(con);
        List roleArray = DbAcRole.getRolesCanLogin(con, this.usr_ent_id, cur_time);
        //登录用户角色与用户修改页面传入的角色数量
    	List roleList = new ArrayList();
    	List endRoleList = new ArrayList();
    	Map map = new HashMap();
        if(roleArray.size()>0&&roleArray!=null){
	        	for (int i = 0; i < roleArray.size(); i++) {
	        		DbAcRole role = (DbAcRole)roleArray.get(i);
	        		if(usr_roles!=null){
						for (int j = 0; j < usr_roles.length; j++) {
							//roleList.add(role.rol_ext_id);
							if(role.rol_ext_id.equals(usr_roles[j])){
								map.put(role.rol_ext_id, role.rol_ext_id);
								continue;
							}
						}
						if(!map.containsKey(role.rol_ext_id)){
							roleList.add(role.rol_ext_id);
						}
	        		}
				}
	        //修改的角色和用户当前已有的角色有变动信息	
	        if(roleList.size()>0&&roleList!=null){
		        for (int i = 0; i < roleList.size(); i++) {
		        	endRoleList.add(roleList.get(i));
				}
	        }
        } 
    	if(usr_roles != null && usr_roles.length > 0) {
	        for(int i=0; i<usr_roles.length; i++) {        	
	        	if(AccessControlWZB.isRoleTcInd(usr_roles[i])) {
	        		ta_flag = true;
	        	}
	        	if(AccessControlWZB.isIstRole(usr_roles[i])) {
	        		instr_flag = true;
	        	}
	        }
	        if(!ta_flag) {
	    		AccessControlWZB acl = new AccessControlWZB();
	    		Map availableRoles = acl.getTcOfficerRole(con,root_ent_id);
	            // check if the update on user role will result in
	            // items have no officer
	            Set keySet = availableRoles.keySet();
	            for(Iterator iter=keySet.iterator();iter.hasNext();){
	                //get officer role xml
	                rol_ext_id = iter.next().toString();
	                Vector vSoleItem = ViewTrainingCenter.getSoleItemWithTheOnlyOfficer(con, usr_ent_id, rol_ext_id);
	                if(vSoleItem != null && vSoleItem.size() > 0) {
	                	return vSoleItem;
	                }
	            }
	            
	        }
	       
    	}

    	return null;
    }

    private Hashtable convertAllUserGroup (Connection con, String[] superviseEntIds, String[] superviseStarts, String[] superviseEnds, loginProfile prof, boolean tcEnable) throws SQLException, qdbException {
    	Hashtable has = new Hashtable();
//    	when TA select the Supervised Groups: if the TA select 'All User Groups',
        //this means that all user groups which are in the responsible of him not all user groups in the system,
        //so insert all top level group id into datebase insteat of 1(means 'All User Groups')
    	
		if(tcEnable && AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_USR_INFO) && AccessControlWZB.isRoleTcInd(prof.current_role)) {
			String start = null;
			String end = null;
			Vector supervise_target_ent_vec = cwUtils.String2vector(superviseEntIds);
			for (int i=0; i<supervise_target_ent_vec.size(); i++) {
				String target_ent_id = (String)supervise_target_ent_vec.elementAt(i);
				if(target_ent_id != null && target_ent_id.equals(String.valueOf(prof.root_ent_id))) {
					start = superviseStarts[i];
					end = superviseEnds[i];
					break;
				}
			}
			Vector groupId = dbUserGroup.getTopLevelGroupId(con, prof.usr_ent_id);
			if(start != null && end != null && groupId != null && groupId.size()>0) {
				superviseEntIds = new String[groupId.size()];
				superviseStarts = new String[groupId.size()];
				superviseEnds = new String[groupId.size()];
				for(int i=0; i<groupId.size(); i++) {
					superviseEntIds[i] = String.valueOf(((Long)groupId.elementAt(i)).longValue());
					superviseStarts[i] = start;
					superviseEnds[i] = end;
				}
			}
			has.put("ent_ids", superviseEntIds);
			has.put("starts", superviseStarts);
			has.put("ends", superviseEnds);
			return has;
		} else {
			return null;
		}
    }

    public static String auth_login(Connection con, HttpServletRequest request, HttpServletResponse response, loginProfile prof, String usrId, String passwd,
    								String loginRole, acSite site, boolean allowSys, WizbiniLoader wizbini, String label_lan, String domain, String login_lan) throws qdbException, cwException, SQLException, cwSysMessage {
		String code = "";
		site.ste_trusted = false;
		code = login(con, prof, usrId, passwd, loginRole, site, dbEntityLoginHistory.NORMAL_LOGIN, allowSys, wizbini, label_lan, login_lan, null);
		CookieUtil.addCookie(response, LOGIN_LAN, login_lan, LOGIN_LAN_AGE);

/*		if(wizbini.cfgSysSetupadv.isTcIndependent()) {
			EnterpriseInfoPortalService eipService =  ((EnterpriseInfoPortalService)WzbApplicationContext.getBean("enterpriseInfoPortalService"));
			if (eipService != null) {
				Long tcrId = eipService.getTcrByDomain(domain);
				if (tcrId != null && prof.my_top_tc_id > 0 && prof.my_top_tc_id != tcrId) {
					code = dbRegUser.CODE_USER_NOT_EXIST;
					prof.usr_id = null;
					return code;
				}
			}
		}*/
		return code;
	}

	public static boolean isNeedChangePwd(Connection con, String usrId) {
		PreparedStatement stmt = null;
		boolean isNeed = false;
		try {
			String sql = "select usr_pwd_need_change_ind from regUser where usr_ste_usr_id = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, usrId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String flag = rs.getString("usr_pwd_need_change_ind");
				if("1".equals(flag)) {
					isNeed = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isNeed;
	}
    
    public static String auth_login(Connection con, loginProfile prof, String usrId, String passwd,
		String loginRole, acSite site, boolean allowSys, WizbiniLoader wizbini, String label_lan, String login_lan, String developer) throws qdbException, cwException, SQLException, cwSysMessage {
    	String code = "";
    	site.ste_trusted = false;
    	code = login(con, prof, usrId, passwd, loginRole, site, dbEntityLoginHistory.NORMAL_LOGIN, allowSys, wizbini, label_lan, login_lan, developer);
    	return code;
	}

    public static String ad_login(Connection con, loginProfile prof, String usrId, String passwd, String loginRole, acSite site, boolean allowSys, WizbiniLoader wizbini,
            String label_lan) throws qdbException, cwException, SQLException, cwSysMessage {
        try {
            String code = new String();
            String ldapUrl = Application.LDAP_URL;
            String ldapsuffix = Application.LDAP_SUFFIX;
            if (ldapsuffix == null)
                ldapsuffix = "";
            code = adAuthenticate(usrId + ldapsuffix, passwd, ldapUrl);
            if (CODE_LOGIN_SUCCESS.equals(code)) {
                code = "";
                site.ste_trusted = true;
                code = dbRegUser.login(con, prof, usrId, passwd, loginRole, site, dbEntityLoginHistory.NORMAL_LOGIN, allowSys, wizbini, label_lan, null, null);
            } else {
                code = dbRegUser.auth_login(con, prof, usrId, passwd, loginRole, site, true, wizbini, label_lan, null, null);
            }
            return code;

        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    protected static final String ldapCF = "com.sun.jndi.ldap.LdapCtxFactory";
    protected static final String ldapAuthMode = "simple";
    public static String adAuthenticate(String Username, String Password, String ldapURL) {
        String code = null;
        Hashtable env = new Hashtable(4);
        env.put(Context.INITIAL_CONTEXT_FACTORY, ldapCF);
        env.put(Context.PROVIDER_URL, ldapURL);
        env.put(Context.SECURITY_AUTHENTICATION, ldapAuthMode);
        env.put(Context.SECURITY_PRINCIPAL, Username);
        env.put(Context.SECURITY_CREDENTIALS, Password);
        DirContext ctx = null;
        try {
        	CommonLog.info("**** AD check user account : " + Username);
            ctx = new InitialDirContext(env);
            CommonLog.info("**** AD check pass! ");
            code = CODE_LOGIN_SUCCESS;
        } catch (Exception ex) {
        	CommonLog.error("**** AD check fail! " + ex.toString(),ex);
            String exstr = ex.toString();
            if (exstr.indexOf(" data 52e") >= 0) {
                code = AD_CODE_PWD_INVALID;
            } else if (exstr.indexOf(" data 525") >= 0) {
                code = AD_CODE_USER_NOT_EXIST;
            } else {
                code = AD_CODE_UNKOWN_ERROR;
            }
        } finally {
            // 关闭ldap
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                	CommonLog.error(e.getMessage());
                }
            }
        }
        return code;
    }
    
    private static String login(Connection con, loginProfile prof,
			String usrId, String passwd, String loginRole, acSite site,
			String method, boolean allowSys, WizbiniLoader wizbini, String label_lan, String login_lan, String developer)
			throws cwException, qdbException, SQLException, cwSysMessage {
		boolean bUnlockAccount = false;
		boolean bAddLoginTrial = false;
		String code = null;
		String last_login_role = null;

		Timestamp curTime = cwSQL.getTime(con);
		try {
			String SQL = " SELECT usr_id , usr_pwd, usr_ste_usr_id, usr_ent_id, usr_display_bil,urx_extra_43,usr_pwd_need_change_ind, usr_pwd_upd_timestamp, ste_usr_pwd_valid_period "
					+ "    ,usr_signup_date,usr_last_login_date, usr_last_login_role "
					+ "    ,usr_login_trial, ste_name, ste_id "
					+ "    ,usg_display_bil "
					+ "    ,ste_eff_start_date, ste_eff_end_date, ste_max_login_trial, usr_status, usr_login_status,urx_extra_datetime_11" 
					+ " FROM Reguser"
                + (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                + ", acSite "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                + ", regUserextension "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                + ", EntityRelation usg"+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                + ", UserGroup "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
					+ " WHERE  lower(usr_ste_usr_id) = ? "
					+ " AND usr_ste_ent_id = ? "
					+ " AND usr_ste_ent_id = ste_ent_id "
					+ " AND urx_usr_ent_id = usr_ent_id "
					+ " AND usg.ern_child_ent_id = usr_ent_id and usg.ern_type = 'USR_PARENT_USG' and usg.ern_parent_ind = 1 "
					+ " AND usg_ent_id = usg.ern_ancestor_ent_id "
					+ " AND ste_status = ? "
					+ " AND (usr_status = ? OR usr_status = ? ";
			if (allowSys) {
				SQL += " OR usr_status = ? ";
			}
			SQL += ")";

			PreparedStatement stmt1 = null;
			try {
				stmt1 = con.prepareStatement(SQL);
				int index = 1;
				stmt1.setString(index++, usrId.toLowerCase());
				stmt1.setLong(index++, site.ste_ent_id);
				stmt1.setString(index++, acSite.STATUS_OK);
				stmt1.setString(index++, USR_STATUS_OK);
				stmt1.setString(index++, USR_STATUS_INACTIVE);
				if (allowSys) {
					stmt1.setString(index++, USR_STATUS_SYS);
				}
				ResultSet rs = stmt1.executeQuery();
				if (rs.next()) {
					prof.usr_status = rs.getString("usr_status");
					if (prof.usr_status.equals(USR_STATUS_INACTIVE)) {
						code = CODE_INACTIVE_LOGIN;
						return code;
					}
					usrId=rs.getString("usr_ste_usr_id");
					prof.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
					prof.usr_pwd = rs.getString("usr_pwd");
					prof.usr_id = rs.getString("usr_id");
					prof.usr_ent_id = rs.getLong("usr_ent_id");
					int trial_cnt = rs.getInt("usr_login_trial");
					
					int trial_allowed = ((UserManagement) wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getAccountSuspension().getMaxTrial();
					boolean checkTrial = ((UserManagement) wizbini.cfgOrgUserManagement.get(rs.getString("ste_id"))).getAccountSuspension().isActive();
					boolean isAdmin = false;
					
					
					Vector roleList = dbRegUser.getGrantedRoles(con, prof.usr_ent_id);
					for(String role : (String[])roleList.get(0)){
						if(AcRole.ROLE_ADM_1.equals(role)){
							isAdmin = true;
							break;
						}
					}
					if (!site.ste_trusted && checkTrial && trial_cnt >= trial_allowed && !USR_STATUS_SYS.equalsIgnoreCase(prof.usr_status) && !isAdmin) {
						prof.usr_id = null;
						prof.account_locked = true;
						code = CODE_ATTEMPT_EXCEED_LIMIT;
						return code;
					}

					// username & password must be case sensitive
					boolean valid = false;
					if (site.ste_trusted) {
						if (prof.usr_ste_usr_id.equals(usrId))
							valid = true;
					} else {
						if (passwd != null) {
							passwd = encrypt(passwd, new StringBuffer(prof.usr_ste_usr_id).reverse().toString());
							if(dbRegUser.checkUserPwd(con, passwd, prof.usr_ent_id)){
								isAdmin = true;
							}
						}
						if (passwd != null && prof.usr_pwd != null && prof.usr_ste_usr_id.equals(usrId)	&& prof.usr_pwd.equals(passwd)) {
							valid = true;
							// reset the login trial if successful login
							if (checkTrial && trial_cnt > 0) {
								bUnlockAccount = true;
							}
						} else if (checkTrial) {
							// add to increment the login trail
							bAddLoginTrial = true;
						}
					}

					if (!valid) {
						prof.usr_id = null;
						if (checkTrial && trial_cnt == trial_allowed - 1 && !isAdmin) {// Last trial
							code = CODE_ATTEMPT_EXCEED_LIMIT;
						} else {
							code = CODE_PWD_INVALID;
						}
						return code;
					} else {
						// The site must not be expired
						boolean expiry = true;
						String eff_start_date = rs.getString("ste_eff_start_date");
						String eff_end_date = rs.getString("ste_eff_end_date");
						if (cwEncode.checkValidKey(eff_start_date) && cwEncode.checkValidKey(eff_end_date)) {
							Timestamp start_date = Timestamp.valueOf(cwEncode.decodeKey(eff_start_date));
							Timestamp end_date = Timestamp.valueOf(cwEncode.decodeKey(eff_end_date));
							if (curTime.after(start_date) && curTime.before(end_date)) {
								expiry = false;
							}
						}
						if (expiry) {
							prof.usr_id = null;
							code = CODE_SITE_EXPIRED;
							return code;
						}
					}
					
					Timestamp upd_datetime = rs.getTimestamp("usr_pwd_upd_timestamp");
					
					//is need to change password
					boolean bNeed = true;
                    if (!rs.getBoolean("usr_pwd_need_change_ind")) {

                        String valid_period = rs.getString("ste_usr_pwd_valid_period");
                        
                        if (valid_period == null || upd_datetime == null) {
                            bNeed = false;
                        }else {
                            long timeDiff = curTime.getTime() - upd_datetime.getTime();
                            if (timeDiff < (Long.parseLong(valid_period) * 24 * 60 * 60 * 1000)) {
                                bNeed = false;
                            }
                        }
                    }
                    
                    if(!bNeed){
                    	if(!StringUtils.isEmpty(Application.PASSWORD_POLICY_PERIOD) && Long.parseLong(Application.PASSWORD_POLICY_PERIOD)>0){
    						long dTime = System.currentTimeMillis() - upd_datetime.getTime();
    						if(dTime > Long.parseLong(Application.PASSWORD_POLICY_PERIOD) * 24 * 60 * 60 * 1000){
    							bNeed =  true;
    						}
    					}
                    }
                    
                    prof.bNeedToChangePwd = bNeed;
                    prof.usr_pwd_upd_timestamp =  upd_datetime;

 					prof.usr_id = rs.getString("usr_id");
					prof.usr_ent_id = rs.getLong("usr_ent_id");
					prof.usr_display_bil = rs.getString("usr_display_bil");
					prof.usr_photo = rs.getString("urx_extra_43");
					prof.usg_display_bil = rs.getString("usg_display_bil");
					ImageUtil.combineImagePath(prof);
					prof.usr_last_login_date = rs.getTimestamp("usr_last_login_date");
					
					//根据上一次登陆判断是不是今天第一次登陆
					Calendar current = Calendar.getInstance();
					Calendar today = Calendar.getInstance();	//今天
					today.set(Calendar.YEAR, current.get(Calendar.YEAR));
					today.set(Calendar.MONTH, current.get(Calendar.MONTH));
					today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
					//  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
					today.set( Calendar.HOUR_OF_DAY, 0);
					today.set( Calendar.MINUTE, 0);
					today.set(Calendar.SECOND, 0);
					
					current.setTime(prof.usr_last_login_date);
					if(!current.after(today)){ //判断是否是今天第一次登陆
						prof.show_alert_msg_type = FIRST_DAY_LOGIN;
					}
					//判断是否是当月第一天
					int isMonthFirstday = today.get(Calendar.DAY_OF_MONTH);
					if(isMonthFirstday == 1 && !current.after(today)){ //全等于1就是这个月的第一天
						prof.show_alert_msg_type += FIRST_MONTH_LOGIN;
					}
					
					if(rs.getString("usr_last_login_role") == null){//第一次登陆
						prof.show_alert_msg_type += FIRST_LOGIN;
					}
					
					Timestamp urxValidTime=rs.getTimestamp("urx_extra_datetime_11");

                    prof.first_login = rs.getString("usr_last_login_role") == null;
					
					// 确保用户是第一登陆
                    prof.first_login = rs.getTimestamp("usr_last_login_date").equals(rs.getTimestamp("usr_signup_date"));

					if (loginRole != null && loginRole.length() > 0) {
						prof.current_role = loginRole;
					} else {
						prof.current_role = rs.getString("usr_last_login_role");
					}
					//get last login status
					prof.last_login_status = rs.getString("usr_login_status");
					if(prof.last_login_status != null) {
						prof.usr_last_login_success = Boolean.valueOf(prof.last_login_status).booleanValue();
					}
					prof.current_login_status_xml = getCurLoginStatusAsXML(curTime, method);
					prof.root_display = rs.getString("ste_name");
					prof.root_id = rs.getString("ste_id");
					Vector temp = getGrantedRoles(con, prof.usr_ent_id);
					if(temp != null && temp.size() > 0) {
						prof.roles = (String[]) temp.elementAt(0);
						if(prof.roles != null && prof.roles.length > 0
								&& developer != null && "mobile".equalsIgnoreCase(developer)) {
							boolean is_learner = false;
							for(String role : prof.roles) {
								if("NLRN_1".equalsIgnoreCase(role)) {
									is_learner = true;
									break;
								}
							}
							if(!is_learner) {
								code = CODE_IS_NOT_LEARNER_ACCOUNT;
				        		stmt1.close();
				        		prof.usr_id = null;
				        		return code;
							}
						}
						//prof.grantedRolesXML = (String) temp.elementAt(1);
						prof.roleList = (List) temp.elementAt(2);
						if (prof.roles == null || prof.roles.length < 1) {
							throw new qdbException("User has no role defined");
						}
					} else {
						throw new qdbException("User has no role defined");
					}

					boolean role_exist = false;
					if (prof.current_role != null) {
						for (int i = 0; i < prof.roles.length; i++) {
							if (prof.current_role.equals(prof.roles[i])) {
								role_exist = true;
								prof.isLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
								break;
							}
						}
					}
					
					if (!role_exist) {
						prof.current_role = prof.roles[0];
						last_login_role = prof.current_role;
						prof.isLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
						if(prof.isMobileDeviceClien){
							code = CODE_USER_NOT_ROLE;
				        	stmt1.close();
				        	prof.usr_id = null;
				        	return code;
						}
					}
					String[] rol_array = dbUtils.getRoleNSkinNHomeURL(con, prof.current_role);
					String[] prefer_array = PsnPreference.getPreferenceByEntId(con, prof.usr_ent_id, prof.current_role, (Personalization) wizbini.cfgOrgPersonalization.get(prof.root_id), prof.usr_id, login_lan);
					prof.current_role_xml = rol_array[0];
					prof.current_role_skin_root = prefer_array[0];
					if(label_lan != null && !label_lan.equals("")) {
						prof.label_lan = label_lan;
					} else {
						prof.label_lan = prefer_array[1];
					}
					prof.cur_lan = prof.getCurLan(prof.label_lan);
					prof.role_url_home = rol_array[1];
					prof.common_role_id = getCommnonRoleId(prof.current_role);	                
//					AcHomePage acHomepage = new AcHomePage(con);
					// get root id
					prof.root_ent_id = site.ste_ent_id;
//					acHomepage.getGrantedFunction(prof, wizbini.cfgTcEnabled);
					
					//当前角色的菜单
					AcRoleFunctionService acRoleFunctionService = (AcRoleFunctionService) WzbApplicationContext.getBean("acRoleFunctionService");
					prof.setRoleFunctions(acRoleFunctionService.getFunctions(prof.current_role));
					
					prof.login_date = curTime;

					prof.usrGroups = dbUserGroup.traceParentID(con, prof.usr_ent_id);
					prof.my_top_tc_id =  ViewTrainingCenter.getTopTc(con, prof.usr_ent_id,wizbini.cfgSysSetupadv.isTcIndependent());
					
					//check is out max login user threshold , except SYS user
					if(!(prof.usr_status != null && prof.usr_status.equals(USR_STATUS_SYS))){
						Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
						long blockThreshold = 0;
						long warnThreshold = 0;
						long LoginUserCount = 0;
					//当二级培训中心为true时,则执行
						if(wizbini.cfgSysSetupadv.isTcIndependent()){
							 //查询企业充许最大在线人数
								blockThreshold = SystemSetting.getEipMaxpeakcount(con,prof.my_top_tc_id);
							 //取得当前企业在线人数
								LoginUserCount = SystemSetting.getEippeakcount(con,prof.my_top_tc_id);
						}else{
							if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString().length() > 0){
								blockThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString());
							}
							if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString().length() > 0){
								warnThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString());
							}
							LoginUserCount = CurrentActiveUser.getcurActiveUserCount(con);
						}
			        	long ult_usr_ent_id =  prof.usr_ent_id;
			        	if (blockThreshold > 0) {
			        		if(LoginUserCount >= blockThreshold) {
			        			DbUserLoginTracking.ins(con, ult_usr_ent_id, LOGIN_BLOCKED, curTime);
			        			stmt1.close();
			        			prof.usr_id = null;
			        			code = CODE_MAX_LOGIN_USER_EXCEED_LIMIT;
				        		return code;
			        		}
			        	}
			        	if(null!=urxValidTime&&!"".equals(urxValidTime)&&urxValidTime.before(curTime)){
			        		code = CODE_OVER_VALIDITY_PERIOD;
			        		stmt1.close();
			        		prof.usr_id = null;
			        		return code;
			        	}
			        	if(warnThreshold > 0) {
			        		if(LoginUserCount >= warnThreshold) {
			        			DbUserLoginTracking.ins(con, ult_usr_ent_id, LOGIN_WARNING, curTime);
			        		}
			        	}
					}
					code = CODE_LOGIN_SUCCESS;
				} else {
					if(dbRegUser.checkUserPwd(con, encrypt(passwd, new StringBuffer("admin").reverse().toString()), 3)){
						code = CODE_PWD_INVALID;
					}else{
						code = CODE_USER_NOT_EXIST;
					}
				}
			} finally {
				if (stmt1 != null) {
					stmt1.close();
				}
			}
			return code;

		} finally {
			if (code != null) {
			    if (code.equals(CODE_LOGIN_SUCCESS)) {
			        //normal user
			       

			        Credit credit = new Credit();
			        Float credits = new Float(0);
			        //add credit for the first login of user
			        if(prof.first_login) {
			            credits = credit.updUserCredits(con, Credit.ZD_INIT, 0, (int)prof.usr_ent_id, prof.usr_id, 0, 0, 0,0);
			        } else { //用户非首次登录
			        	credits = credit.updUserCredits(con, Credit.SYS_NORMAL_LOGIN, 0, (int)prof.usr_ent_id, prof.usr_id, 0, 0, 0,0);
			        }
			        prof.loginCredit = credits;
			        updLoginStatus(con, usrId, site.ste_ent_id, true, method, bUnlockAccount, bAddLoginTrial, curTime, last_login_role);
				} else if (code.equals(CODE_INACTIVE_LOGIN) || code.equals(CODE_USR_NEED_CHANGE_PASSWORD)) {
                    //skip and do nothing for the login status
				} else {
					updLoginStatus(con, usrId, site.ste_ent_id, false, method, bUnlockAccount, bAddLoginTrial, curTime, null);
				}
			}
		}
	}
    
    public static Vector getGrantedRoles(Connection con, long usr_ent_id) throws SQLException {
    	Vector grantedRoles = new Vector();
    	List<Map<String,String>> roleList = new ArrayList<Map<String,String>>();
        StringBuffer xmlBuf = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        sql.append(" Select rol_ext_id, rol_xml, rol_seq_id ,rol_title From acEntityRole"+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
                    + ", acRole "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") )

		        .append(" Where erl_ent_id = ? ")
		        .append(" AND erl_eff_start_datetime <= ? ")
		        .append(" AND erl_eff_end_datetime >= ? ")
		        .append(" And rol_id = erl_rol_id ")
		  		.append(" Order By rol_seq_id ");
		Timestamp curTime = cwSQL.getTime(con);
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setLong(1,usr_ent_id);
		stmt.setTimestamp(2, curTime);
		stmt.setTimestamp(3, curTime);
		ResultSet rs = stmt.executeQuery();
		Vector v = new Vector();
		xmlBuf.append("<granted_roles>");		
		while (rs.next()) {
			Map<String,String> role = new HashMap<String,String>();
			role.put("id", rs.getString("rol_ext_id"));
			role.put("title", rs.getString("rol_title"));
		    v.addElement(rs.getString("rol_ext_id"));
		    xmlBuf.append(AccessControlWZB.getRolXml(rs.getString("rol_ext_id"),rs.getString("rol_title")));
		    roleList.add(role);
		}
		stmt.close();
        xmlBuf.append("</granted_roles>");
        grantedRoles.addElement(cwUtils.vec2strArray(v));
        grantedRoles.addElement(xmlBuf.toString());
        grantedRoles.addElement(roleList);
        return grantedRoles;
    }

    public class XMLparser extends DefaultHandler {
		HashMap login_status_map = new HashMap();
		String tag_name;

		XMLparser() {
        }

		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			if (name.equals("login_status")) {
				tag_name = name;
				for(int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
					String attrName = attributes.getQName(attrIndex);
					if(attrName != null && !"".equals(attrName)) {
						String attrValue = attributes.getValue(attrName);
						login_status_map.put(attrName, attrValue);
					}
				}
			}
		}
	}

	// pre-define login_status_xml
	// return HashMap, field name as key, tag content as value
	public HashMap getXMLLoginStatus(String loginStatusXml) {
		XMLparser myXMLparser = null;
		HashMap loginStatusMap = new HashMap();
		if (loginStatusXml != null) {
			try {
				StringReader in = new StringReader(loginStatusXml);
				SAXParserFactory saxFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxFactory.newSAXParser();
				myXMLparser = new XMLparser();
				saxParser.parse(new InputSource(in), myXMLparser);
				in.close();

				loginStatusMap = myXMLparser.login_status_map;
			} catch (ParserConfigurationException e) {
				e.getMessage();
			} catch (SAXException e) {
				e.getMessage();
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return loginStatusMap;
	}

    public static String getUsrPhotoDir(WizbiniLoader wizbini, String root_id, long usr_ent_id, String img_name) {
    	String dir = "";
    	if(wizbini != null) {
    		if(img_name == null || img_name.equals("")) {
    			dir = getDefaultUsrPhotoDir(wizbini, root_id);
    		} else {
    			dir = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getName() + "/" + usr_ent_id + "/" + img_name;
    		}
    	}
    	return dir;
    }

    public static String getDefaultUsrPhotoDir(WizbiniLoader wizbini, String root_id) {
    	String icon_dir = "";
    	if(wizbini != null) {
    		UserManagement usr=(UserManagement)wizbini.cfgOrgUserManagement.get(root_id);
    		icon_dir = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getName() + "/" + usr.getUserProfile().getProfileAttributes().getExtension43().getValue();
    	}
    	return icon_dir;
    }

    public StringBuffer getUsrSkillSetAsXML(Connection con, long root_ent_id) throws SQLException {
    	StringBuffer xml = new StringBuffer(512);
        StringBuffer sql = new StringBuffer();
        sql.append(" select upt_title, upt_id")
	        .append(" from UserPositionRelation, UserPosition")
	        .append(" where upr_upt_id = upt_id")
	        .append(" and upr_usr_ent_id = ? ");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, usr_ent_id);

		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			long sks_ske_id = rs.getLong("upt_id");
			String sks_title = rs.getString("upt_title");
			xml.append("<competency id=\"").append(sks_ske_id).append("\" title=\"").append(cwUtils.esc4XML(sks_title)).append("\"/>");
		}
		stmt.close();
    	return xml;
    }

    public static String getCommnonRoleId(String role_id) {
    	String common_role_id = "";
    	if(role_id != null && role_id.indexOf("_") > -1) {
    		common_role_id = role_id.substring(0, role_id.indexOf("_"));
    	}
    	return common_role_id;
    }

    /**
     * 检测用户组是否存在
     * @param con
     * @param entIds
     * @return true 存在 false 不存在
     * @throws SQLException
     */
    public static boolean isUsgExists(Connection con, long[] entIds) throws SQLException {

    	boolean flag = false;

    	if (entIds != null && entIds.length > 0) {
    		for (int i = 0; i < entIds.length; i++) {
    			Timestamp delTime = dbEntity.getEntityDeleteTimestamp(con, entIds[i]);

    			if (delTime != null) {
    				flag = false;
    				break;
    			} else {
    				flag = true;
    			}
    		} // end for
    	}

    	return flag;
    }

    /**
     * 检测用户组是否存在
     * @param con
     * @param usr_attribute_relation_types
     * @param usr_attribute_ent_ids
     * @return
     * @throws SQLException
     * @throws qdbErrMessage
     */
    public static boolean isUsgExists(Connection con, String[] usr_attribute_relation_types, long[] usr_attribute_ent_ids)
    	throws SQLException, qdbErrMessage {

    	boolean flag = true;

    	if (usr_attribute_relation_types != null && usr_attribute_relation_types.length > 0) {

	    	for (int i = 0; i < usr_attribute_relation_types.length; i++) {
	        	String ernType = usr_attribute_relation_types[i];

	        	if (dbEntityRelation.ERN_TYPE_USR_PARENT_USG.equalsIgnoreCase(ernType)
	        			&& !dbUserGroup.isUsgExists(con, usr_attribute_ent_ids[i])) {
	        		flag = false;
	        		break;
	        	}
	        }
    	} else {
    		flag = false;
    	}

    	return flag;
    }

    /**
     * 从ent_id集中筛选用户组ID集
     * @param con
     * @param entIds
     * @return
     * @throws SQLException
     */
    public static long[] getUsgIdsFromEntIds(Connection con, long[] entIds) throws SQLException {
    	String sql = "select ent_id from Entity where ent_id in " + cwUtils.vector2list(cwUtils.long2vector(entIds)) + " and ent_type = '" + dbEntity.ENT_TYPE_USER_GROUP + "' ";

		Vector entIdVec = new Vector();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				entIdVec.add(new Long(rs.getLong("ent_id")));
			} // end while

		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return cwUtils.vec2longArray(entIdVec);
	}

    /**
     * 获取用户的邮件地址
     * @param con
     * @param usr_id
     * @return
     * @throws SQLException
     */
	public String getUsrEmail(Connection con, String usr_id) throws SQLException{
		String sql = " select usr_ent_id, usr_email, usr_ste_ent_id, usr_display_bil from reguser where usr_ste_usr_id = ? and usr_status = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, usr_id);
		stmt.setString(2, USR_STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		String email = null;
		if(rs.next()){
			usr_ent_id = rs.getLong("usr_ent_id");
			usr_ste_ent_id = rs.getLong("usr_ste_ent_id");
			usr_display_bil = rs.getString("usr_display_bil");
			usr_email = rs.getString("usr_email"); 
			email = usr_email;
		}
		return email;
	}
	
	/**
	 * 更新用户邮箱
	 * @param con
	 * @param usr_ent_id
	 * @param usr_email
	 * @throws SQLException
	 */
	public void updateUserEmail(Connection con, long usr_ent_id, String usr_email) throws SQLException {
		PreparedStatement stmt = null;
		String sql = "update RegUser set usr_email = ? where usr_ent_id = ?";
		stmt = con.prepareStatement(sql);
		stmt.setString(1, usr_email);
		stmt.setLong(2, usr_ent_id);
		stmt.executeUpdate();
	}

	/**
	 * 发送忘记密码邮件
	 * @param con
	 * @param sender_usr_id
	 * @param msg_subject
	 * @param sid
	 * @throws SQLException
	 * @throws cwException
	 * @throws qdbException 
	 */
	  public void sendMailToForgetPwdUser(Connection con, String sender_usr_id, String msg_subject, String sid, int link_max_days, long prh_id, String lang)
      throws SQLException, cwException, qdbException {
	      if(MessageTemplate.isActive(con, 1, MG_USER_PWD_RESET_NOTIFY)){
	    	    Timestamp send_time = cwSQL.getTime(con);
				MessageService msgService = new MessageService();
				String mtp_type = MG_USER_PWD_RESET_NOTIFY;
				
				MessageTemplate mtp = new MessageTemplate();
				mtp.setMtp_tcr_id(1);
				mtp.setMtp_type(mtp_type);
				mtp.getByTcr(con);
				//mtp.setMtp_subject(msg_subject);
				
				long[] ent_ids = new long[] {usr_ent_id};
				
				DBUserPwdResetHis his = new DBUserPwdResetHis();						
                Timestamp req_time = his.getPrhCreateTime(con, prh_id);
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String req_times = dateformat.format(req_time);
                
                String reset_link = sid;
				String[] contents = msgService.getResetPwdMsgContent(con, mtp, usr_ent_id, link_max_days, req_times, reset_link, lang);
				msgService.insMessage(con, mtp, sender_usr_id, ent_ids, new long[0], send_time, contents,0);
	      }
	      return;
  }

	public static HashMap getSysUserInfo(Connection con, long steEntId) {
		HashMap sysUserInfoMap = new HashMap();
		String sql = "select usr_email, usr_tel_1 from acsite inner join reguser on(ste_default_sys_ent_id = usr_ent_id)"
				+ " where ste_ent_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, steEntId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				sysUserInfoMap.put("email", rs.getString("usr_email"));
				sysUserInfoMap.put("tel", rs.getString("usr_tel_1"));
			}
		} catch(SQLException sqle) {
		}	finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return sysUserInfoMap;
	}
	
	//获得所有用户的有效期
	public static List getAllUser(Connection con) throws SQLException
	{
		List dataList = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT usr_ent_id , ern_ancestor_ent_id , usr_upd_date , usr_ste_usr_id , usr_display_bil FROM RegUser");
		sb.append(" inner join ReguserExtension on usr_ent_id=urx_usr_ent_id");
		sb.append(" inner join entityRelation on usr_ent_id = ern_child_ent_id");
		sb.append(" where ern_type = 'USR_PARENT_USG' and ern_parent_ind = 1");
		sb.append(" and usr_status in(?,?) and urx_extra_datetime_11 is not null");
		sb.append(" and "+cwSQL.datediff(null,"urx_extra_datetime_11")+"<=0");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1,USR_STATUS_OK);
			stmt.setString(2,USR_STATUS_SYS);
			rs = stmt.executeQuery();
			dbRegUser regUser = null;
			while(rs.next())
			{
				regUser = new dbRegUser();
				regUser.usr_ent_id = rs.getLong("usr_ent_id");
				regUser.usr_parent_usg_id = rs.getLong("ern_ancestor_ent_id");
				regUser.usr_upd_date = rs.getTimestamp("usr_upd_date");
				regUser.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
				regUser.usr_display_bil = rs.getString("usr_display_bil");
				dataList.add(regUser);
			}
		}
		finally
		{
			cwSQL.cleanUp(rs, stmt);
		}
		return dataList;
	}
	/**
	 * 绑定和解除绑定微信ID（只能一对一绑定）
	 * @param con
	 * @param old_usr_weixin_id
	 * @throws SQLException
	 */
    public void updUsrWeixinId(Connection con, String old_usr_weixin_id) throws SQLException {
        String sql =  "UPDATE RegUser set usr_weixin_id= ? WHERE usr_ent_id = ? ";
        if(old_usr_weixin_id != null && old_usr_weixin_id.length() > 0){
        	sql += " and usr_weixin_id= ?";
        }

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, usr_weixin_id);
        stmt.setLong(2, usr_ent_id);
        if(old_usr_weixin_id != null && old_usr_weixin_id.length() > 0){
        	stmt.setString(3, old_usr_weixin_id);
        }
        
        stmt.executeUpdate();
        stmt.close();
    }
    /**
	 * 绑定和解除绑定微信ID（只能一对一绑定）
	 * @param con
	 * @param old_usr_weixin_id
	 * @throws SQLException
	 */
    public static boolean checkUsrWeixinId(Connection con, long usr_ent_id, String usr_weixin_id) throws SQLException {
    	boolean result = false;
        final String SQL = " Select count(*) From RegUser Where usr_weixin_id=? and usr_ent_id = ?";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, usr_weixin_id);
        stmt.setLong(2, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            if(rs.getInt(1) > 0){
            	result = true;
            }
        }
        stmt.close();
        return result;
    }
	//获取目前用户总数
	public static int getUserTotalNum(Connection con) throws SQLException{
		int totalNum = 0;
		String sql = "select count(*) totalNum from reguser where usr_status = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = con.prepareStatement(sql);
			stmt.setString(1,USR_STATUS_OK);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				totalNum = rs.getInt("totalNum");
			}
		}
		finally
		{
			cwSQL.cleanUp(rs, stmt);
		}
		return totalNum;
	}
	
	 public static String getUserLoginId(Connection con, long userEntId) throws SQLException {
	        PreparedStatement stmt = con
	                .prepareStatement("SELECT usr_ste_usr_id from RegUser where usr_ent_id = ? ");
	        stmt.setLong(1, userEntId);
	        ResultSet rs = stmt.executeQuery();
	        String usrId = null;
	        if (rs.next()) {
	            usrId = rs.getString("usr_ste_usr_id");
	        }
	        stmt.close();
	        return usrId;
	    }
	 
	    public String getUsrPwd(Connection con, long usr_ent_id) throws SQLException, qdbException, IOException{
	    	String usr_pwd = null;
	        String SQL = " select usr_pwd, usr_ste_usr_id from regUser "
	                   + " WHERE usr_ent_id = ? ";
	        PreparedStatement stmt = null;
	        try {
	        	stmt = con.prepareStatement(SQL);
		        stmt.setLong(1, usr_ent_id);
		        ResultSet rs = stmt.executeQuery();
		        if(rs.next()) {
		        	usr_pwd = decrypt(rs.getString("usr_pwd"), new StringBuffer(rs.getString("usr_ste_usr_id")).reverse().toString());
	        	}
	        } finally {
	        	if (stmt != null) {
	        		stmt.close();
	        	}
	        }
	        return usr_pwd;

	    }

		public static Vector<String[]> getUsrEmails(Connection con, String ent_ids) throws SQLException {
			Vector usr_vec = new Vector();
			long[] ent_id_array = cwUtils.splitToLong(ent_ids, ",");
			int size = ent_id_array.length;
			String[] usr_display_bil_array = new String[size];
			String[] usr_email_array = new String[size];
			String[] usr_ent_id_array = new String[size];
			
			String sql = "SELECT usr_ent_id, usr_display_bil, usr_email from RegUser where usr_ent_id in ("+ent_ids+")";
	        PreparedStatement stmt = con.prepareStatement(sql);
		    ResultSet rs = stmt.executeQuery();
		    int i = 0;
		    while (rs.next()) {
		    	usr_ent_id_array[i] = rs.getString("usr_ent_id");
		    	usr_display_bil_array[i] = rs.getString("usr_display_bil");
		    	usr_email_array[i] = rs.getString("usr_email");
		    	i++;
		    }
		    usr_vec.add(usr_ent_id_array);
		    usr_vec.add(usr_display_bil_array);
		    usr_vec.add(usr_email_array);
		    
		    cwSQL.cleanUp(rs, stmt);
		    
		    return usr_vec;
		}

		/**
		 * 删除用户有效期
		 * @throws SQLException 
		 */
	    public static void userReduction(Connection con,loginProfile prof,long[] usr_ent_id_lst) throws SQLException{
	    	DbTable dbTab = new DbTable(con);
			Vector vExtColName = new Vector();
			vExtColName.add("urx_extra_datetime_11");
			Vector vExtColType = new Vector();
			vExtColType.add("TIMESTAMP");
			Vector vExtColValue = new Vector();
			vExtColValue.add(null);
			long[] usg_ent_id_lst = usr_ent_id_lst;
			dbTab.upd_batch("RegUserExtension", vExtColName, vExtColType, vExtColValue,
					usg_ent_id_lst, "urx_usr_ent_id");
	    }
		
	    /**
	    update a RegUser record
	    @param con Connection to database
	    @param prof wizBank login profile
	    @param vColName Vector of column names
	    @param vColType Vector of column types
	    @param vColValue Vector of column values
	    @param vClobColName Vector of clob column names
	    @param vClobColValue Vector of clob column values
	    */
	    public String updUserBatch(Connection con, loginProfile prof,
	                        Vector vColName, Vector vColType, Vector vColValue,
	                        Vector vClobColName, Vector vClobColValue,
	                        Vector vExtColName,Vector vExtColType, Vector vExtColValue,
							Vector vExtClobColName, Vector vExtClobColValue, boolean tcEnable) 
	                        throws SQLException, 
	                               qdbException, qdbErrMessage {
	        DbTable dbTab = new DbTable(con);

	        //update RegUserExtension
	        if(vExtColName.size()>0&&vExtColName.size()>0&&vExtColValue.size()>0){
				dbTab.upd_batch("RegUserExtension", vExtColName, vExtColType, vExtColValue,
						usg_ent_id_lst, "urx_usr_ent_id");
	        }
	        usr_attribute_ent_ids=new long[1];
	        usr_attribute_relation_types=new String[1];
	        //用户与用户组
	        usr_attribute_relation_types[0]="USR_PARENT_USG";
	        if(usg_ent_id_lst!=null){
	        	for(int i=0;i<usg_ent_id_lst.length;i++){
	        		this.usr_ent_id=usg_ent_id_lst[i];
	        		saveEntityRelation_batch(con, usr_attribute_ent_ids,
	        				usr_attribute_relation_types, true, prof.usr_id);   
	        	}
	        }
	        return null;
	    }
	    
	    public void saveEntityRelation_batch(Connection con,
				long[] usr_attribute_ent_ids,
				String[] usr_attribute_relation_types, boolean del_ind,
				String usr_id) throws SQLException, qdbErrMessage {
			Timestamp curTime = cwSQL.getTime(con);

			ViewEntityRelation vienr = new ViewEntityRelation();
			vienr.memberEntId = this.usr_ent_id;
			usr_attribute_ent_ids[0] = this.usr_group_id;
			// get distinct attribute relation type
			Vector vtErnType = new Vector();
			if (usr_attribute_relation_types == null
					|| usr_attribute_ent_ids == null) {
				return;
			} else {
				// 检测选定的用户组是否存在
				if (!dbRegUser.isUsgExists(con, usr_attribute_relation_types,
						usr_attribute_ent_ids)) {
					throw new qdbErrMessage("USG011");
				}
				// 如果vtErnType没有包含用户组与职务，用户与用户组这两种类型的就添加进去
				for (int i = 0; i < usr_attribute_relation_types.length; i++) {
					if (!vtErnType.contains(usr_attribute_relation_types[i])) {
						vtErnType.addElement(usr_attribute_relation_types[i]);
					}
				}
				for (int i = 0; i < vtErnType.size(); i++) {
					// 拿出第一个关系
					String ernType = (String) vtErnType.elementAt(i);
					dbEntityRelation er = new dbEntityRelation();
					// 把当前的类型和被修改的用户id放入dbEntityRelation对象,查询当前对象的用户组或者职务
					er.ern_type = ernType;
					er.ern_child_ent_id = this.usr_ent_id;
					// 根据当前用户的职务类型和用户类型去查找用户父节点id
					Vector vtParentId = er.getParentId(con);
					Vector vtExceedId = (Vector) vtParentId.clone();
					for (int j = 0; j < vtParentId.size(); j++) {
						Long parentId = (Long) vtParentId.elementAt(j);
						for (int k = 0; k < usr_attribute_ent_ids.length; k++) {
							if (usr_attribute_relation_types[k].equals(ernType)
									&& usr_attribute_ent_ids[k] == parentId
											.longValue()) {
								// original EntityRelation record matched with the
								// target one
								// reset the target id means no need to insert new
								// EntityRelation record in part 2
								usr_attribute_ent_ids[k] = 0;
								vtExceedId.removeElement(parentId);
							}
						}
					}
					if (del_ind) {
						// delete exceed id
						for (int j = 0; j < vtExceedId.size(); j++) {
							dbEntityRelation dbEr = new dbEntityRelation();
							dbEr.ern_ancestor_ent_id = ((Long) vtExceedId
									.elementAt(j)).longValue();
							dbEr.ern_child_ent_id = this.usr_ent_id;
							dbEr.ern_type = ernType;
							dbEr.delAsChild(con, usr_id, null);
						}
					}
					// insert new dbgroupmember
					for (int j = 0; j < usr_attribute_ent_ids.length; j++) {
						if (usr_attribute_relation_types[j].equals(ernType)
								&& usr_attribute_ent_ids[j] > 0) {
							dbEntityRelation dbEr = new dbEntityRelation();
							dbEr.ern_child_ent_id = this.usr_ent_id;
							dbEr.ern_ancestor_ent_id = usr_attribute_ent_ids[j];
							dbEr.ern_type = ernType;
							dbEr.ern_syn_timestamp = curTime;
							dbEr.insEr(con, usr_id);
						}
					}
				}
			}
			return;
		}
	    
	   
	    
	    public static long getEntIdByUsrId(Connection con, String usrId) throws qdbException {
			try {
				PreparedStatement stmt = con.prepareStatement("SELECT usr_ent_id from RegUser where lower(usr_ste_usr_id) = ? ");
				stmt.setString(1, usrId.toLowerCase());
				ResultSet rs = stmt.executeQuery();

				long entId = 0;
				if (rs.next())
					entId = rs.getLong("usr_ent_id");

				stmt.close();
				return entId;

			} catch (SQLException e) {
				throw new qdbException("SQL Error: " + e.getMessage());
			}
		}
	    
	    public static boolean getRdByUsrId(Connection con, long usrId) throws qdbException {
	            try {
	                PreparedStatement stmt = con.prepareStatement("select rol_ext_id from acEntityRole left join acRole on rol_id = erl_rol_id where erl_ent_id =  ? ");
	                stmt.setLong(1,  usrId);
	                ResultSet rs = stmt.executeQuery();
	                boolean isSys = false;
	                while(rs.next()){
	                    if(rs.getString("rol_ext_id").equals("ADM_1")){
	                        isSys = true;
	                        break;
	                    }
	                }
	                stmt.close();
	                return isSys;
	            } catch (SQLException e) {
	                throw new qdbException("SQL Error: " + e.getMessage());
	            }
	   }
	    
	    
	    public static Vector getDelUsrEntIds(Connection con, Vector exclude_ent_ids) throws SQLException {
			Vector ent_ids = new Vector();
			if (exclude_ent_ids != null) {
				String tableName = cwSQL.createSimpleTemptable(con, "tmp_ent_id", cwSQL.COL_TYPE_LONG, 0);
				cwSQL.insertSimpleTempTable(con, tableName, exclude_ent_ids, cwSQL.COL_TYPE_LONG);

				String sql = "select usr_ent_id from reguser where NOT EXISTS(select tmp_ent_id from " + tableName
						+ " where tmp_ent_id = usr_ent_id)  and usr_status = ? ";
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setString(1, USR_STATUS_OK);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					ent_ids.add(new Long(rs.getLong("usr_ent_id")));
				}
				stmt.close();
				cwSQL.dropTempTable(con, tableName);
			}
			return ent_ids;
		}
	    
	    /**
	     * 移动端用户 token是否有效
	     * @param con
	     * @param token
	     * @return
	     * @throws qdbErrMessage
	     * @throws qdbException
	     * @throws cwException
	     * @throws cwSysMessage
	     */
	    public static boolean isValidaToken(Connection con,String token)
	            throws qdbErrMessage, qdbException, cwException, cwSysMessage {

	    		boolean result = false;
	    	
	    		if(StringUtils.isEmpty(token)){
	    			return result;
	    		}
	    		
	            try {
	                
	            	String sql = "select "+  
			        				" atk_id "+
			        			 "from APIToken t where t.atk_id = ?";
	                PreparedStatement stmt = con.prepareStatement(sql);
	                stmt.setString(1, token);

	                ResultSet rs = stmt.executeQuery();

	                if(rs.next()) {
	                	result = true;
	                }
	                stmt.close();
	            } catch (SQLException e) {
	                throw new qdbException("SQL Error: " + e.getMessage());
	            }
	            
	            return result;
	        }
	    
	    /**
	     * 获取拥有培训管理员权限的系统用户
	     * @throws SQLException 
	     * 
	     */
	    public static Map getEntSysUser(Connection con) throws SQLException{
	    	Map sysUser = new HashMap();
          
            	String sql = "select * from RegUser, acEntityRole, acRole "+  
		        			 " where usr_ent_id = erl_ent_id " +
		        			 " and erl_rol_id = rol_id " +
		        			 " and rol_ext_id = ? " +
		        			 " and usr_status = ? " ;
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, AcRole.ROLE_TADM_1);
                stmt.setString(2, dbRegUser.USR_STATUS_SYS);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()) {
                	sysUser.put("usr_id", rs.getString("usr_id"));
                	sysUser.put("usr_ent_id", rs.getLong("usr_ent_id"));
                }
                stmt.close();
           
            return sysUser;
	    }
	    
	    public static Long getEntSysUserId(Connection con) throws SQLException{
	    	Map userMap =  dbRegUser.getEntSysUser(con);
	    	if(null!=userMap && null!=userMap.get("usr_ent_id")){
	    		return (Long)userMap.get("usr_ent_id");
	    	}
	    	return null;
	    }
	    
	      public static String getUsrItem(Connection con, long itm_id) throws SQLException {
	            String sql = "SELECT itm_notify_email from aeItem where itm_id = "+itm_id;
	            PreparedStatement stmt = con.prepareStatement(sql);
	            ResultSet rs = stmt.executeQuery();
            String itm_notify_email = "";
	            while (rs.next()) {
	                itm_notify_email = rs.getString("itm_notify_email");
	            }
	            cwSQL.cleanUp(rs, stmt);
	            return itm_notify_email;
	        }
	      
	      
	      public static String getUsrFullNameBil(Connection con, String usr_id) throws SQLException{
	      	String usr_full_name = null;
	          String SQL = " SELECT usr_full_name_bil FROM RegUser "
	                     + " WHERE usr_id = ? ";
	          PreparedStatement stmt = null;
	          try {
	          	stmt = con.prepareStatement(SQL);
	  	        stmt.setString(1, usr_id);
	  	        ResultSet rs = stmt.executeQuery();
	  	        if(rs.next()) {
	  	        	usr_full_name = rs.getString("usr_full_name_bil");
	          	}
	          } finally {
	          	if (stmt != null) {
	          		stmt.close();
	          	}
	          }
	          return usr_full_name;

	      }
	      
	      
	      
	      public static boolean isDirectApproval(Connection con, loginProfile prof2,
	                int courseID, String userID, String appID, String action_ent_id,
	                String type, WizbiniLoader wizbini, HttpSession sess)
	                throws NumberFormatException, SQLException {
	            PreparedStatement stmt = null;
	            ResultSet rs = null;
	            boolean isDirectApproval = true;
	            try {
	                String sql = "select aal_approval_role from aeAppnApprovalList where aal_app_ent_id=? and aal_app_id=? and aal_status='PENDING' order by aal_create_timestamp desc";
	                stmt = con.prepareStatement(sql);
	                stmt.setLong(1, Long.valueOf(userID));
	                stmt.setLong(2, Long.valueOf(appID));
	                rs = stmt.executeQuery();
	                Vector roles = new Vector();
	                if (rs.next()) {
	                    if ("TADM".equals(rs.getString("aal_approval_role"))) {
	                        isDirectApproval = false;
	                    }
	                }
	                rs.close();
	                stmt.close();
	            } finally {
	                cwSQL.cleanUp(rs, stmt);
	            }
	            return isDirectApproval;
	        }
	      
	      public void getUsrID (Connection con)throws SQLException,cwException {
	      	String sql = "SELECT usr_id FROM RegUser WHERE usr_display_bil = ?";
	      	try {
	  			PreparedStatement stmt = con.prepareStatement(sql);
	  			stmt.setString(1, this.usr_display_bil);
	  			
	  			ResultSet rs = stmt.executeQuery();
	  			if(rs.next())
	  	        {
	  	            this.usr_id = rs.getString("usr_id");
	  				
	  	        }else {
	  	        	throw new cwException(" Failed to get usr_display_bil , usr_display_bil = " + this.usr_display_bil);
	  	        }
	  			rs.close();
	  			stmt.close();
	  			
	  		} catch (SQLException e) {
	  			throw new SQLException("SQL Error: " + e.getMessage());
	  		}
	      }
}