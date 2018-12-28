package com.cw.wizbank.qdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.db.ViewEvnSurveyQueReport;
import com.cw.wizbank.cache.wizbCacheManager;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.content.EvalAccess;
import com.cw.wizbank.content.ModuleTrainingCenter;
import com.cw.wizbank.db.DbCourseModuleCriteria;
import com.cw.wizbank.db.DbCtGlossary;
import com.cw.wizbank.db.DbCtReference;
import com.cw.wizbank.db.DbQueContainerSpec;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.quebank.quecontainer.DynamicQueContainer;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedQueContainer;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class dbModule extends dbResource
{
    public static final String MOD_TYPE_TST     = "TST";
    public static final String MOD_TYPE_DXT     = "DXT";
    public static final String MOD_TYPE_STX     = "STX";
    public static final String MOD_TYPE_EXC     = "EXC";
    public static final String MOD_TYPE_GEN     = "GEN";
    public static final String MOD_TYPE_ASS     = "ASS";
    public static final String MOD_TYPE_CHAT    = "CHT";   //Type : Chatroom
    public static final String MOD_TYPE_VCR     = "VCR";
    public static final String MOD_TYPE_FOR     = "FOR";
    public static final String MOD_TYPE_FAQ     = "FAQ";   //Type : Chatroom
    public static final String MOD_TYPE_SVY     = "SVY";
    public static final String MOD_TYPE_VOT     = "VOT";
    public static final String MOD_TYPE_ASM     = "ASM";    // quiz room
    public static final String MOD_TYPE_EAS     = "EAS";    // EAS module
    public static final String MOD_TYPE_EVN     = "EVN";    // Evaluation module
    
    public static final String MOD_TYPE_RDG     = "RDG";
    public static final String MOD_TYPE_REF     = "REF";   
    public static final String MOD_TYPE_GLO     = "GLO";
    public static final String MOD_TYPE_VOD     = "VOD";
    public static final String MOD_TYPE_AICC_U   = "AICC_AU";   
    public static final String MOD_TYPE_NETG_COK     = "NETG_COK";   
    public static final String MOD_TYPE_SCO     = "SCO";    
	
//SQL for get all online module with score
	static final String GET_ONLINE_MODULE_WITH_SCORE_SQL = "select distinct(mod_res_id),mod_type,res_title,mod_max_score,mod_pass_score, res_status from resourcecontent,module,resources "
														  +"where rcn_res_id = ?   and mod_res_id = rcn_res_id_content and res_id = mod_res_id and (" 
														  +"( mod_type IN ('TST','DXT') ) "
														  +"  or( mod_type IN ('AICC_AU','NETG_COK','SCO') and mod_max_score > 0) "
														  +" or (mod_type = 'ASS' and mod_max_score > 0 )"
														  +") "
														  +" and (mod_res_id not in (select mod_res_id from module,coursemodulecriteria where mod_res_id = cmr_res_id and cmr_ccr_id = ? and cmr_del_timestamp is null))";
	static final String GET_ALL_ONLINE_MODULE_WITH_SCORE_SQL = "select distinct(mod_res_id),res_title,mod_max_score,mod_pass_score from resourcecontent,module,resources "
														  +"where (( rcn_res_id = ? and mod_type IN ('TST','DXT','AICC_AU','NETG_COK','SCO') and mod_res_id = rcn_res_id_content and res_id = mod_res_id ) "
														  +" or (rcn_res_id = ? and mod_type = 'ASS' and mod_max_score > 0 and mod_res_id = rcn_res_id_content and res_id = mod_res_id)) ";
	
	static final String GET_ALL_AVAILABLE_ONLINE_MODULE_SQL = "select distinct(mod_res_id),res_title,mod_required_time, mod_max_score,mod_pass_score,res_status, mod_type from resourcecontent,module,resources "
		  +"where rcn_res_id = ? and mod_res_id = rcn_res_id_content and res_id = mod_res_id "
		  +" and (mod_res_id not in (select mod_res_id from module,coursemodulecriteria where mod_res_id = cmr_res_id and cmr_ccr_id = ? and cmr_del_timestamp is null and cmr_is_contri_by_score=1))";
	
	static final String GET_BY_RES_ID_SQL = "select mod_res_id, mod_max_score, mod_pass_score from module where mod_res_id = ? ";
//    public static final String MOD_TYPE_TNA     = "TNA";
	static final String GET_ONLINE_MODULE_BY_RES_ID = "select mod_res_id, res_title, mod_type,mod_max_score, mod_pass_score, res_status from module,resources where mod_res_id = ? and res_id = mod_res_id ";
    String SQL_getIsEnrollmentRelatedAsXML =
    "select itm_run_ind from ResourceContent, Course, aeItem where rcn_res_id_content = ? and rcn_res_id = cos_res_id and cos_itm_id = itm_id and itm_type = 'CLASSROOM' and itm_content_def = 'PARENT'";
// AICC AU
    public static final String MOD_TYPE_AICC_AU = "AICC_AU";

    public static final String FCN_MOD_MGT_IN_COS = "MOD_MGT_IN_COS";
//    public static final String MOD_TYPE_LCT     = "LCT";
//    public static final String MOD_TYPE_MMD     = "MMD";
    public static final String MOD_TYPE_ALL     = "ALL";

    public static final String MOD_LOGIC_ADT = "ADT";
    public static final String MOD_LOGIC_RND = "RND";   

    public int mod_mod_id_root = -1;
    public long mod_res_id;
    public String mod_type;
    public float  mod_max_score;
    public float  mod_pass_score;
    public String mod_instruct;
    public long mod_max_attempt;
    public long mod_max_usr_attempt;
    public boolean mod_score_ind;
    public long mod_score_reset;
    public String mod_logic;
    public Timestamp mod_eff_start_datetime;
    public Timestamp mod_eff_end_datetime;
    public Timestamp mod_in_eff_start_datetime;
    public Timestamp mod_in_eff_end_datetime;
    public String mod_usr_id_instructor;
    public boolean mod_has_rate_q;
    public boolean mod_is_public;   // not attach to course
    public boolean mod_public_need_enrol;   //public module need to enrol before access
    // 0 or 1, -1 indicate this field is not applicable for the mod_type 
    public int mod_show_answer_ind = -1; 
    // 0 or 1, -1 indicate this field is not applicable for the mod_type 
    public int mod_sub_after_passed_ind = -1; 
    public int mod_show_save_and_suspend_ind = -1;
    public long mod_mod_res_id_parent;
    public long mod_show_answer_after_passed_ind;

    public String mod_tshost;   //Data of the TSSERVER
    public int mod_tsport;
    public int mod_wwwport;

    public int mod_managed_ind;
    public int mod_started_ind;
    public String mod_test_style;
    // Data for AICC
    public String mod_web_launch;
    public String mod_core_vendor;
    public String mod_password;
    public String mod_time_limit_action;
    public String mod_vendor;
    public String mod_aicc_version;
    public String mod_import_xml;
    
    public boolean attempt_add = true;
    

    public long     mod_size;
    // for resource permission of all instructors
//    public String[] mod_instructor_ent_id_lst;
    public String extension = "";
    // not a db field
    public long[] mod_instructor_ent_id_lst;
    public long usr_ent_id;//view svy submission,to get other user svy result
    public long tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;
    
    //for public evaluation
    public long tcr_id;
    public String tcr_title;
    
    public String mod_res_title;
    public long mod_tcr_id;
    
    public int mod_required_time;	// 移动课程视频点播的必修时长，以分钟为单位的整数
    public int mod_download_ind;	// 视频点播是否允许下载
    public int mod_mobile_ind;    // 模块是否发布到移动端

    public boolean mod_intranet_ip_limit_ind;
    public String mod_intranet_start_ip;
    public String mod_intranet_end_ip;
    public boolean mod_multi_question_ind;
    
    //为了同步锁而创建的静态变量
    public static Object obj = new Object();
    
    public dbModule() {
        super.SQL_getIsEnrollmentRelatedAsXML = SQL_getIsEnrollmentRelatedAsXML;
    }
    
    
    
    public dbModule(long inResID) {
        super();
        this.res_id = inResID;
        this.mod_res_id = inResID;
        super.SQL_getIsEnrollmentRelatedAsXML = SQL_getIsEnrollmentRelatedAsXML;
    }
    
    public Object clone() throws CloneNotSupportedException {
        dbModule newMod = (dbModule) super.clone();
        newMod.mod_mod_id_root = this.mod_mod_id_root;
        newMod.mod_res_id = this.mod_res_id;
        newMod.mod_type = this.mod_type;
        newMod.mod_max_score = this.mod_max_score;
        newMod.mod_pass_score = this.mod_pass_score;
        newMod.mod_instruct = this.mod_instruct;
        newMod.mod_max_attempt = this.mod_max_attempt;
        newMod.mod_max_usr_attempt = this.mod_max_usr_attempt;
        newMod.mod_score_ind = this.mod_score_ind;
        newMod.mod_score_reset = this.mod_score_reset;
        newMod.mod_logic = this.mod_logic;
        newMod.mod_eff_start_datetime = this.mod_eff_start_datetime;
        newMod.mod_eff_end_datetime = this.mod_eff_end_datetime;
        newMod.mod_in_eff_start_datetime = this.mod_in_eff_start_datetime;
        newMod.mod_in_eff_end_datetime = this.mod_in_eff_end_datetime;
        newMod.mod_usr_id_instructor = this.mod_usr_id_instructor;
        newMod.mod_has_rate_q = this.mod_has_rate_q;
        newMod.mod_is_public = this.mod_is_public;
        newMod.mod_public_need_enrol = this.mod_public_need_enrol;
        newMod.mod_show_answer_ind = this.mod_show_answer_ind;
        newMod.mod_sub_after_passed_ind = this.mod_sub_after_passed_ind;
        newMod.mod_mod_res_id_parent = this.mod_mod_res_id_parent;
        newMod.mod_tshost = this.mod_tshost;
        newMod.mod_tsport = this.mod_tsport;
        newMod.mod_wwwport = this.mod_wwwport;
        newMod.mod_web_launch = this.mod_web_launch;
        newMod.mod_core_vendor = this.mod_core_vendor;
        newMod.mod_password = this.mod_password;
        newMod.mod_time_limit_action = this.mod_time_limit_action;
        newMod.mod_vendor = this.mod_vendor;
        newMod.mod_aicc_version = this.mod_aicc_version;
        newMod.mod_import_xml = this.mod_import_xml;
        newMod.mod_size = this.mod_size;
        newMod.extension = this.extension;
        newMod.mod_instructor_ent_id_lst = this.mod_instructor_ent_id_lst;
        newMod.usr_ent_id = this.usr_ent_id;
        newMod.tkh_id = this.tkh_id;
        
        return newMod;
    }
    
    public void ins(Connection con, loginProfile prof)
        throws qdbException
    {
        try {
            res_type = RES_TYPE_MOD;

            //for a standard test or dynamic test
            //the status must be offline because
            //the test must contains no question/criterion 
            //in it at the time of creation
            if(mod_type.equalsIgnoreCase(MOD_TYPE_TST) || mod_type.equalsIgnoreCase(MOD_TYPE_DXT)) {
                res_status = RES_STATUS_OFF;
            }

            // calls dbResource.ins()
            super.ins(con);

            // if ok.
            mod_res_id = res_id;

			if (!res_subtype.equals(MOD_TYPE_ASS)
					&& !res_subtype.equals(MOD_TYPE_AICC_AU)
					&& !res_subtype.equals(MOD_TYPE_SCO)
					&& !res_subtype.equals(MOD_TYPE_EAS)
                    && !res_subtype.equals(MOD_TYPE_TST)
                    && !res_subtype.equals(MOD_TYPE_DXT)) {
                mod_max_score = 0;
            }
            // only survey might has the rating question
            if (!mod_type.equals(MOD_TYPE_SVY)) {
                mod_has_rate_q = false;
            }

            PreparedStatement stmt = con.prepareStatement(
                " INSERT INTO Module "
                + " ( mod_res_id "
                + " , mod_type "
                + " , mod_max_score "
                + " , mod_pass_score "
                + " , mod_instruct "
                + " , mod_max_attempt "
                + " , mod_max_usr_attempt "
                + " , mod_score_ind "
                + " , mod_score_reset "
                + " , mod_logic "
                + " , mod_usr_id_instructor "
                + " , mod_has_rate_q "
                + " , mod_is_public "
                + " , mod_public_need_enrol "
                + " , mod_mod_id_root "
                + " , mod_show_answer_ind "
                + " , mod_sub_after_passed_ind "
                + " , mod_mod_res_id_parent " 
                + " , mod_auto_save_ind" 
                + " , mod_managed_ind "
                + " , mod_show_A_A_passed_ind "
                + " , mod_tcr_id "
                + " , mod_required_time " 
                + " , mod_download_ind, mod_mobile_ind"
                + " , mod_test_style) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            PreparedStatement stmt1 = con.prepareStatement(
                " UPDATE Resources SET "
                + " res_tpl_name = ? WHERE "
                + " res_id = ? ");

            int idx = 1;
            stmt.setLong(idx++, mod_res_id);
            stmt.setString(idx++, mod_type);
            stmt.setFloat(idx++, mod_max_score);
            stmt.setFloat(idx++, mod_pass_score);
            stmt.setString(idx++, mod_instruct);
            stmt.setLong(idx++, mod_max_attempt);
            stmt.setLong(idx++, mod_max_usr_attempt);
            stmt.setBoolean(idx++, mod_score_ind);
            stmt.setLong(idx++,   mod_score_reset);
            stmt.setString(idx++,  mod_logic);
            stmt.setString(idx++, mod_usr_id_instructor);
            stmt.setBoolean(idx++, mod_has_rate_q);
            stmt.setBoolean(idx++, mod_is_public);
            stmt.setBoolean(idx++, mod_public_need_enrol);
            if (mod_mod_id_root > 0) {
                stmt.setLong(idx++, mod_mod_id_root);
            } else {
                stmt.setNull(idx++, java.sql.Types.INTEGER);
            }
            stmt.setInt(idx++, mod_show_answer_ind);
            stmt.setInt(idx++, mod_sub_after_passed_ind);
            if (mod_mod_res_id_parent > 0) {
                stmt.setLong(idx++, mod_mod_res_id_parent);
            } else {
                stmt.setNull(idx++, java.sql.Types.INTEGER);
            }
            stmt.setInt(idx++, mod_show_save_and_suspend_ind);
            stmt.setInt(idx++, mod_managed_ind);
            stmt.setLong(idx++, mod_show_answer_after_passed_ind);
            stmt.setLong(idx++, mod_tcr_id);
            stmt.setInt(idx++, mod_required_time);
            stmt.setInt(idx++, mod_download_ind);
            stmt.setInt(idx++, mod_mobile_ind);
            stmt.setString(idx++, mod_test_style);
            
            stmt1.setString(1, res_tpl_name);
            stmt1.setLong(2, mod_res_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to insert Module.");
            }else {
                stmtResult=stmt1.executeUpdate();
                stmt1.close();
                if ( stmtResult!=1)
                {
                    con.rollback();
                    throw new qdbException("Failed to insert Module.");
                }

                AcResources acres = new AcResources(con);
                boolean read = true;
                boolean write = true;
                boolean exec = true;
                dbResourcePermission.save(con,mod_res_id,prof.usr_ent_id,prof.current_role,read,write,exec);
                if( mod_is_public && !mod_public_need_enrol )
                    dbResourcePermission.save(con,mod_res_id,prof.root_ent_id,null,true,false,true);

                //dbResourcePermission.save(con,mod_res_id,prof.usr_ent_id,true,true,false);

                // Insert write permission for the instructor
                if (mod_instructor_ent_id_lst != null){
                    for (int i=0; i<mod_instructor_ent_id_lst.length; i++){
                        dbResourcePermission.save(con,mod_res_id,mod_instructor_ent_id_lst[i],null,true,true,false);
                    }
                }

//                // for resource permission of all instructors
//                if (mod_instructor_ent_id_lst != null && mod_instructor_ent_id_lst.length >0) {
//                    for (int i=0;i<mod_instructor_ent_id_lst.length;i++) {
//                        dbResourcePermission.save(con,mod_res_id,Integer.parseInt(mod_instructor_ent_id_lst[i]),true,true,false);
//                    }
//                }
//                if (mod_usr_id_instructor !=null && mod_usr_id_instructor.length() > 0) {
//                    long instructorId = dbRegUser.getEntId(con, mod_usr_id_instructor);
//                    dbResourcePermission.save(con,mod_res_id,instructorId,null,true,true,false);
                    /*
                    String[] ist_role_lst = dbUtils.getUserIstRole(con, prof.usr_ent_id);
                    for(int i=0; i<ist_role_lst.length; i++) {
                        String ist_role = (String) ist_role_lst[i];
                        read = acres.hasResPermissionRead(ist_role);
                        write = acres.hasResPermissionWrite(ist_role);
                        exec = acres.hasResPermissionExec(ist_role);
                        dbResourcePermission.save(con,mod_res_id,instructorId,ist_role,read,write,exec);
                    }
                    */
                    //dbResourcePermission.save(con,mod_res_id,instructorId,"NIST",true,true,false);
//                }
                //Dennis, 2000-12-13, impl release control
                //If the new status == DATE, update the eff_start/end_datetime in Module
                //if(res_status.equalsIgnoreCase(RES_STATUS_DATE))
                //2001-01-05, all status will have eff datetime
                updateEffDatetime(con);
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // for AICC import
    // by cliff, 2001/4/19
    public void updAiccLongText(Connection con, String mod_import_xml, String mod_core_vendor, String mod_desc, String mod_web_launch)
        throws qdbException
    {
        try {
            /* commented for using the new update function for clob (2002.04.22 kawai)
            // update mod_import_xml
            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Module SET mod_import_xml= " + cwSQL.getClobNull(con)
                + " WHERE mod_res_id = ? ");

            stmt.setLong(1, mod_res_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement(
                      " SELECT mod_import_xml FROM Module "
                    + "     WHERE mod_res_id = ? FOR UPDATE "
                    ,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
                stmt.setLong(1, mod_res_id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    cwSQL.setClobValue(con, rs, "mod_import_xml", mod_import_xml);
                    rs.updateRow();
                }
            stmt.close();

            // update mod_core_vendor
            stmt = con.prepareStatement(
                " UPDATE Module SET mod_core_vendor= " + cwSQL.getClobNull(con)
                + " WHERE mod_res_id = ? ");

            stmt.setLong(1, mod_res_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = con.prepareStatement(
                      " SELECT mod_core_vendor FROM Module "
                    + "     WHERE mod_res_id = ? FOR UPDATE "
                    ,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );
                stmt.setLong(1, mod_res_id);
                rs = stmt.executeQuery();
                if (rs.next())
                {
                    cwSQL.setClobValue(con, rs, "mod_core_vendor", mod_core_vendor);
                    rs.updateRow();
                }
            stmt.close();
            */
            //--- use new clob update method to update clob columns (2002.04.22 kawai)
            // construct the condition
            String condition = "mod_res_id = " + mod_res_id;
            // construct the column & value
            String[] columnName = new String[3];
            String[] columnValue = new String[3];
            columnName[0] = "mod_import_xml";
            columnValue[0] = mod_import_xml;
            columnName[1] = "mod_core_vendor";
            columnValue[1] = mod_core_vendor;
            columnName[2] = "mod_web_launch";
            columnValue[2] = mod_web_launch;
            cwSQL.updateClobFields(con, "Module", columnName, columnValue, condition);

            // update res desc
            if(mod_desc != null && mod_desc.length() > RES_DESC_LENGTH) {
                mod_desc = mod_desc.substring(0,RES_DESC_LENGTH);
            }
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(
                    " UPDATE Resources SET res_desc = ? "
                    + " WHERE res_id = ? ");

                stmt.setString(1, mod_desc);
                stmt.setLong(2, mod_res_id);
                stmt.executeUpdate();
            } finally {
                if(stmt!=null) stmt.close();
            }
            super.updateTimeStamp(con);
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    // by cliff, 8/6/2001
    // for updating an module with aicc type
    public boolean updAiccAu(Connection con, String domain, loginProfile prof, String aicc_crs_filename, String aicc_cst_filename, String aicc_des_filename, String aicc_au_filename, String aicc_ort_filename)
        throws IOException, SQLException, qdbException, qdbErrMessage, cwSysMessage {
//System.out.println("mod_in_eff_start_datetime:" + mod_in_eff_start_datetime);
//System.out.println("mod_in_eff_end_datetime:" + mod_in_eff_end_datetime);
//System.out.println("mod_max_score:" + mod_max_score);
//System.out.println("res_subtype:" + res_subtype);
//System.out.println("res_src_type:" + res_src_type);
//System.out.println("res_src_link:" + res_src_link);
        // only used for calling aicc methods
        dbCourse dbCos = new dbCourse();
        dbCos.cos_res_id = getCosId(con, mod_res_id);

        try {
            String mod_import_xml = null;

            Vector vtCosDescriptor = null;
            Vector vtCosStructure = null;
            Vector vtObjectiveRelation = null;

            Vector vtTemp = null;

            crsIniFile iniFileCRS = null;
            try {
                iniFileCRS = new crsIniFile(aicc_crs_filename);
            } catch(IOException e) {
            	CommonLog.error("Error in loading the iniFileCRS:" + e.toString());
                throw new IOException(e.toString());
            }

            vtCosDescriptor = dbCos.buildCosDescriptorVector(aicc_des_filename);
            vtCosStructure = dbCos.getCosStructureVector(aicc_cst_filename);
            if (aicc_ort_filename != null) {
                vtObjectiveRelation = dbCos.getCosObjectiveVector(aicc_ort_filename);
            }
            Hashtable htBlockElements = dbCos.buildAiccBlockElements(dbCos.getMemberRelationLst(aicc_cst_filename));

            Hashtable htAuModID = new Hashtable();
            Hashtable htObjObjID = new Hashtable();

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(aicc_au_filename))));
            } catch(Exception e) {
                throw new IOException(e.toString());
            }
            String line = null;
            String systemID = null;
            String type = null;
            String command_line = null;
            String file_name = null;
            String max_score = null;
            String mastery_score = null;
            String max_time_allowed = null;
            String time_limit_action = null;
            String system_vendor = null;
            String core_vendor = null;
            String web_launch = null;
            String au_password = null;

            String tempStr = null;

            Hashtable htAuFieldOrder = null;
            Hashtable htAuFieldValue = null;

            StringTokenizer st = null;
            // the first line is used to determine the field order
            for (;;) {
                try {
                    line = in.readLine();
                } catch(Exception e) {
                    throw new IOException(e.toString());
                }
                if (line == null) {
                    break;
                }
                else if (line.trim().length() > 0 && line.trim().equalsIgnoreCase("") == false) {
                    htAuFieldOrder = new Hashtable();

                    int index = 1;

                    Vector vtRecord = dbCos.buildTableRecord(line);
                    String recordElement = null;
                    for (int i=0; i<vtRecord.size(); i++) {
                        recordElement = (String)vtRecord.elementAt(i);
                        htAuFieldOrder.put(recordElement.toLowerCase(), new Integer(index));
                        index++;
                    }

                    break;
                }
            }

            //actually there should be only one AU record in the .au file for skillsoft course
            for (;;) {
                String mod_desc = null;

                try {
                    line = in.readLine();
                } catch(Exception e) {
                    throw new IOException(e.toString());
                }
                if (line == null) {
                    break;
                }
                else if (line.trim().length() == 0 || line.trim().equalsIgnoreCase("") == true) {
                    continue;
                }
                else {
                    htAuFieldValue = new Hashtable();

                    int index = 1;

                    Vector vtRecord = dbCos.buildTableRecord(line);
                    String recordElement = null;
                    for (int i=0; i<vtRecord.size(); i++) {
                        recordElement = (String)vtRecord.elementAt(i);
                        htAuFieldValue.put(new Integer(index), recordElement);
                        index++;
                    }

                    Integer fieldIndex = null;

                    fieldIndex = (Integer)htAuFieldOrder.get("system_id");
                    systemID = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("type");
                    type = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("command_line");
                    command_line = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("file_name");
                    file_name = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("max_score");
                    max_score = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("mastery_score");
                    mastery_score = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("max_time_allowed");
                    max_time_allowed = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("time_limit_action");
                    time_limit_action = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("system_vendor");
                    system_vendor = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("core_vendor");
                    core_vendor = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("web_launch");
                    web_launch = (String)htAuFieldValue.get(fieldIndex);

                    fieldIndex = (Integer)htAuFieldOrder.get("au_password");
                    au_password = (String)htAuFieldValue.get(fieldIndex);

                    vtTemp = null;
                    for (int i=0; i<vtCosDescriptor.size(); i++) {
                        vtTemp = (Vector)vtCosDescriptor.elementAt(i);
                        if (systemID.equalsIgnoreCase((String)vtTemp.elementAt(0)) == true) {
                            break;
                        }
                        else {
                            vtTemp = null;
                        }
                    }

                    mod_import_xml = "<assignable_unit system_id=\"";
                    mod_import_xml += dbUtils.esc4XML(systemID);
                    mod_import_xml += "\" type=\"";
                    mod_import_xml += dbUtils.esc4XML(type);
                    mod_import_xml += "\" command_line=\"";
                    mod_import_xml += dbUtils.esc4XML(command_line);
                    mod_import_xml += "\" file_name=\"";
                    mod_import_xml += dbUtils.esc4XML(file_name);
                    mod_import_xml += "\" max_score=\"";
                    mod_import_xml += dbUtils.esc4XML(max_score);
                    mod_import_xml += "\" mastery_score=\"";
                    mod_import_xml += dbUtils.esc4XML(mastery_score);
                    mod_import_xml += "\" max_time_allowed=\"";
                    mod_import_xml += dbUtils.esc4XML(max_time_allowed);
                    mod_import_xml += "\" time_limit_action=\"";
                    mod_import_xml += dbUtils.esc4XML(time_limit_action);
                    mod_import_xml += "\" system_vendor=\"";
                    mod_import_xml += dbUtils.esc4XML(system_vendor);
                    mod_import_xml += "\" core_vendor=\"";
                    mod_import_xml += dbUtils.esc4XML(core_vendor);
                    mod_import_xml += "\" web_launch=\"";
                    mod_import_xml += web_launch;
                    mod_import_xml += "\" au_password=\"";
                    mod_import_xml += au_password;

                    mod_import_xml += "\" />";

                    if (max_score.length() > 0) {
                        mod_max_score = Float.parseFloat(max_score);;
                    }
                    else {
                    }

                    if (mastery_score.length() > 0) {
                        mod_pass_score = Float.parseFloat(mastery_score);
                    }
                    else {
                    }

                    if (max_time_allowed.equalsIgnoreCase("") == true || max_time_allowed.length() == 0) {
                    }
                    else {
                        int hr = 0;
                        int min = 0;
                        int sec = 0;
                        String strTime = null;
                        StringTokenizer stTime = new StringTokenizer(max_time_allowed,":");

                        strTime = stTime.nextToken();
                        strTime = strTime.trim();
                        hr = Integer.parseInt(strTime);

                        strTime = stTime.nextToken();
                        strTime = strTime.trim();
                        min = Integer.parseInt(strTime);

                        strTime = stTime.nextToken();
                        strTime = strTime.trim();
                        sec = Integer.parseInt(strTime);

                        res_duration = hr*60 + min + sec/60;
                    }

                    res_type = "MOD";
//                    res_lan = "ISO-8859-1";
                    res_subtype = "AICC_AU";
                    res_src_type = "AICC_FILES";
                    res_src_link = file_name;
//                    mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
//                    mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);

                    // truncate the length of the description
                    //if (res_desc.length() > 200) {
                        //res_desc = res_desc.substring(0, 200);
                    //}

                    upd(con, prof);
                    updAicc(con, prof, core_vendor, au_password, mod_import_xml, time_limit_action, web_launch, iniFileCRS.getValue("Course_Creator"), res_desc, iniFileCRS.getValue("Version"));

                    htAuModID.put(systemID, new Long(mod_res_id));
                }

            }

            in.close();

            Vector vtElement = null;

            // input AICC objectives into DB
            for (int i=0; i<vtCosDescriptor.size(); i++) {
                String objSystemID = null;
                String objDeveloperID = null;
                String objTitle = null;
                String objDesc = null;
                dbObjective dbObj = null;

                vtElement = (Vector)vtCosDescriptor.elementAt(i);
                objSystemID = (String)vtElement.elementAt(0);
                if (objSystemID.toLowerCase().startsWith("j") == true) {
                    objDeveloperID = (String)vtElement.elementAt(1);
                    objTitle = (String)vtElement.elementAt(2);
                    objDesc = (String)vtElement.elementAt(3);

                    dbObj = new dbObjective();
                    dbObj.obj_type = "AICC";
                    dbObj.obj_desc = dbCos.trimObjDesc(dbUtils.esc4XML(objDesc));
                    dbObj.obj_title = objTitle;
                    dbObj.obj_developer_id = objDeveloperID;
                    dbObj.obj_import_xml = "<objective developer_id=\"";
                    dbObj.obj_import_xml += objDeveloperID;
                    dbObj.obj_import_xml += "\" />";
                    int obj_id = (int)dbObj.insAicc(con, prof);
                    htObjObjID.put(objSystemID, new Integer(obj_id));
                }
            }

            if (vtObjectiveRelation != null) {
                vtElement = null;
                for (int i=0; i<vtObjectiveRelation.size(); i++) {
                    vtElement = (Vector)vtObjectiveRelation.elementAt(i);
                    String parentSystemID = (String)vtElement.elementAt(0);

                    Vector vtObjID = new Vector();
                    String childSystemID = null;
                    for (int j=1; j<vtElement.size(); j++) {
                        childSystemID = (String)vtElement.elementAt(j);
                        vtObjID.addElement((Integer)htObjObjID.get(childSystemID));
                    }

                    long[] intObjID = new long[vtObjID.size()];
                    for (int k=0; k<vtObjID.size(); k++) {
                        intObjID[k] = ((Integer) vtObjID.elementAt(k)).longValue();
                    }

                    long[] int_parent_mod_id_lst = null;
                    if (parentSystemID.startsWith("A") == true || parentSystemID.startsWith("a") == true) {
                        int_parent_mod_id_lst = new long[] { ((Long)htAuModID.get(parentSystemID)).longValue() };
                    }
                    // collect all AU's mod_id in that AICC Block
                    else if (parentSystemID.startsWith("B") == true || parentSystemID.startsWith("b") == true) {
                        Vector vtBlockElements = (Vector)htBlockElements.get(parentSystemID);
                        int_parent_mod_id_lst = new long[vtBlockElements.size()];
                        String tempSystemID = null;
                        for (int m=0; m<vtBlockElements.size(); m++) {
                            tempSystemID = (String)vtBlockElements.elementAt(m);
                            int_parent_mod_id_lst[m] = ((Long)htAuModID.get(tempSystemID)).longValue();
                        }
                    }
                    dbResourceObjective dbResObj = new dbResourceObjective();
                    // remove the previous objective relations
                    dbResObj.removeResObj(con, int_parent_mod_id_lst);
                    // add new objective relations
                    dbResObj.insResObj(con, int_parent_mod_id_lst, intObjID);

                }
            }

            // update the cos corresponding fields, or reset the fields that is previously set
            // during importing an AICC course which should be reset when inserting/updating an AICC AU
            dbCos.updAiccCos(con, prof, null, null, null, -1, false, false);

            return true;

        } catch(qdbErrMessage e) {
            // rollback at qdbAction
            //con.rollback();
            throw new qdbErrMessage(e.toString());
        }
    }

    public void saveNETgSpecificInfo(Connection con, loginProfile prof, String cookie_name) throws qdbException {
      try {
        String upd_mSQL = "UPDATE Module SET "
            + " mod_core_vendor = ? "
            + " where mod_res_id = ? " ;

        PreparedStatement stmt = con.prepareStatement(upd_mSQL);

        stmt.setString(1, cookie_name);
        stmt.setLong(2, mod_res_id);
        int stmtResult=stmt.executeUpdate();
        stmt.close();
        if ( stmtResult!=1)
        {
            // rollback at qdbAction
            //con.rollback();
            throw new qdbException("Failed to update Module.");
        }

        return;

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }
    }

    public void updAicc(Connection con, loginProfile prof, String mod_core_vendor, String mod_password, String mod_import_xml, String mod_time_limit_action, String web_launch, String mod_vendor, String mod_desc, String mod_aicc_version)
        throws qdbException, qdbErrMessage
    {
      try {
        updAiccLongText(con, mod_import_xml, mod_core_vendor, mod_desc, web_launch);

        Timestamp curTime = dbUtils.getTime(con);


        res_id = mod_res_id;

        res_type = RES_TYPE_MOD;
        res_upd_user = prof.usr_id;

        String upd_mSQL = "UPDATE Module SET "
            + " mod_password = ? "
            + " , mod_time_limit_action = ? "
            + " , mod_import_datetime = ? "
//            + " , mod_web_launch = ? "
            + " , mod_vendor = ? "
            + " , mod_aicc_version = ? "
            + " where mod_res_id = ? " ;

        PreparedStatement stmt = con.prepareStatement(upd_mSQL);

        int index = 1;
        stmt.setString(index++, mod_password);
        stmt.setString(index++, mod_time_limit_action);
        stmt.setTimestamp(index++, curTime);
//        stmt.setString(4, web_launch);
        stmt.setString(index++, mod_vendor);
        stmt.setString(index++, mod_aicc_version);
        stmt.setLong(index++, mod_res_id);
        int stmtResult=stmt.executeUpdate();
        stmt.close();
        if ( stmtResult!=1)
        {
            // rollback at qdbAction
            //con.rollback();
            throw new qdbException("Failed to update Module.");
        }

        return;

      } catch(SQLException e) {
    	  CommonLog.error(e.getMessage(),e);
        throw new qdbException("SQL Error: " + e.getMessage());
      }
    }

    public String getModImportXML(Connection con)
        throws IOException, SQLException, qdbException, qdbErrMessage ,cwSysMessage {
        String xml = new String("");

        try {
            PreparedStatement stmt = con.prepareStatement(
            " SELECT  mod_import_xml FROM Module "
            + " where mod_res_id = ? ");

            // set the values for prepared statements
            stmt.setLong(1, mod_res_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                xml = cwSQL.getClobValue(rs, "mod_import_xml");
            }
            else
            {
            	stmt.close();
                //throw new qdbException( "No data for course. id = " + mod_res_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Module ID = " + mod_res_id );
            }

            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

        return xml;
    }

    // end of AICC specific methods


	public void updBasicInfo(Connection con, loginProfile prof, boolean isChangeDate) throws qdbException, qdbErrMessage, cwSysMessage {
		try {
			res_id = mod_res_id;

			res_type = RES_TYPE_MOD;
			res_upd_user = prof.usr_id;

			// check User Right

			// checkModifyPermission(con, prof);
			// if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
			// dbResourcePermission.RIGHT_WRITE)) {
			// throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
			// }

			// check timeStamp

			super.checkTimeStamp(con);

			super.updBasicInfo(con, isChangeDate);

			// tricky! upd instructor only when has instructor inputted
			if (mod_instructor_ent_id_lst != null) {
				dbResourcePermission.delUserRoleIsNull(con, mod_res_id);
				/*
				 * if( mod_is_public && !mod_public_need_enrol )
				 * dbResourcePermission
				 * .save(con,mod_res_id,prof.root_ent_id,null,true,false,true);
				 */
				for (int i = 0; i < mod_instructor_ent_id_lst.length; i++) {
					dbResourcePermission.save(con, mod_res_id,
							mod_instructor_ent_id_lst[i], null, true, true,
							false);
				}
			}

			String SQL;

			if (mod_in_eff_start_datetime != null && dbUtils.isMinTimestamp(mod_in_eff_start_datetime)) {
				mod_in_eff_start_datetime = dbUtils.getTime(con);
			}

			SQL = "update Module set mod_eff_start_datetime = ?, "

			+ "mod_eff_end_datetime = ? " + " , mod_pass_score = ? "
					+ " , mod_max_score = ? " + "where mod_res_id = ? ";
			PreparedStatement stmt = con.prepareStatement(SQL);
			int index = 1;
			stmt.setTimestamp(index++, mod_in_eff_start_datetime);
			stmt.setTimestamp(index++, mod_in_eff_end_datetime);
			stmt.setFloat(index++, mod_pass_score);
			stmt.setFloat(index++, mod_max_score);
			stmt.setLong(index++, mod_res_id);
			int stmtResult = stmt.executeUpdate();
			stmt.close();
			if (stmtResult != 1) {
				con.rollback();
				throw new qdbException("Failed to update status.");
			}

			return;

		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
	}

    public void upd(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        upd(con, prof, true);
    }
    public void upd(Connection con, loginProfile prof, boolean isChangeDate)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
      try {
        res_id = mod_res_id;

        res_type = RES_TYPE_MOD;
        res_upd_user = prof.usr_id;

        // check User Right

//        checkModifyPermission(con, prof);
        //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}

        // check timeStamp

            super.checkTimeStamp(con);

        // check whether the course was attempted.
        // 30-01-20001 : basic information can be modified.
        //checkStat(con);

        //Dennis, impl release date control

        if (res_status.equalsIgnoreCase(RES_STATUS_ON)
            || res_status.equalsIgnoreCase(RES_STATUS_DATE))
           {
                // Check if the question is ordered in 1,2,3....
                if (!checkQorder(con)) {
                    //Questions are not in the correct order.
                    throw new qdbErrMessage("MOD001");
                }
        }

        //if the module is standard test or dynamic test
        //make sure that you can only turn the module online
        //if the test has question/criteria defined in it
        if(res_status.equalsIgnoreCase(RES_STATUS_ON)) {
            if(mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {
                if(dbResourceContent.getResourceContentCount(con, mod_res_id) == 0) {
                    res_status = RES_STATUS_OFF;
                }
            } else if(mod_type.equalsIgnoreCase(MOD_TYPE_DXT)) {
                if(dbModuleSpec.getModuleSpecCount(con, mod_res_id) == 0) {
                    res_status = RES_STATUS_OFF;
                }
            }
        }

        // calls dbResource.upd()
        super.upd(con, isChangeDate);

        // Update ResourcePermission of the instructor
        // Get the current instructor

        /*
        String instructorId = dbModule.getInstructorId(con, mod_res_id);
        if (instructorId != null && instructorId.length() > 0 ) {
            String[] ist_role_lst = dbUtils.getUserIstRole(con, prof.usr_ent_id);
            for(int i=0; i<ist_role_lst.length; i++) {
                dbResourcePermission.del(con,mod_res_id,instructorId,ist_role_lst[i]);
            }
            //dbResourcePermission.del(con,mod_res_id,instructorId,"NIST");
        }
        */

        // permission records have to be cleared no matter mod_instructor is specified or not
        dbResourcePermission.delUserRoleIsNull(con, mod_res_id);
        /*
        if( mod_is_public && !mod_public_need_enrol )
            dbResourcePermission.save(con,mod_res_id,prof.root_ent_id,null,true,false,true);
        */
        if (mod_instructor_ent_id_lst != null){
            for (int i=0; i<mod_instructor_ent_id_lst.length; i++){
                dbResourcePermission.save(con,mod_res_id,mod_instructor_ent_id_lst[i],null,true,true,false);
            }
        }


/*        if (mod_usr_id_instructor !=null && mod_usr_id_instructor.length() > 0) {
            String[] ist_role_lst = dbUtils.getUserIstRole(con, prof.usr_ent_id);
            AcResources acres = new AcResources(con);
            for(int i=0; i<ist_role_lst.length; i++) {
                String ist_role = ist_role_lst[i];
                boolean read = acres.hasResPermissionRead(ist_role);
                boolean write = acres.hasResPermissionWrite(ist_role);
                boolean exec = acres.hasResPermissionExec(ist_role);
                dbResourcePermission.save(con,mod_res_id,instructorId,ist_role,read,write,exec);
            }
            //dbResourcePermission.save(con,mod_res_id,instructorId,"NIST",true,true,false);
        }
        */

        String upd_mSQL = "UPDATE Module SET "
            + " mod_res_id = ? "
            + " , mod_type = ? ";

            if (mod_type.equalsIgnoreCase("SCO") || mod_type.equalsIgnoreCase("ASS") || mod_type.equalsIgnoreCase("EAS")) {
                upd_mSQL += " , mod_max_score = ? ";
            }

            upd_mSQL +=
              " , mod_pass_score = ? "
            + " , mod_instruct = ? "
            + " , mod_max_attempt = ? "
            + " , mod_max_usr_attempt = ? "
            + " , mod_score_ind = ? "
            + " , mod_score_reset = ? "
            + " , mod_logic = ? "
            + " , mod_usr_id_instructor = ? "
            + " , mod_has_rate_q = ? "
            + " , mod_show_answer_ind = ? "
            + " , mod_sub_after_passed_ind = ? "
            + " , mod_auto_save_ind = ? "
            + " , mod_managed_ind = ? "
            + " , mod_tcr_id = ? "
            + " , mod_required_time = ? "
            + " , mod_download_ind = ? "
            + " , mod_mobile_ind = ? "
            + " , mod_show_A_A_passed_ind = ? "
            + " , mod_test_style = ?"
            + " where mod_res_id = ? " ;
        String upd_mRec =
             " UPDATE Resources SET "
            + " res_tpl_name = ? WHERE "
            + " res_id = ? ";

        PreparedStatement stmt = con.prepareStatement(upd_mSQL);
        int index = 1;

        stmt.setLong(index++, mod_res_id);
        stmt.setString(index++, mod_type);

        if (mod_type.equalsIgnoreCase("SCO") || mod_type.equalsIgnoreCase("ASS") || mod_type.equalsIgnoreCase("EAS")) {
            stmt.setFloat(index++, mod_max_score);
        }

        stmt.setFloat(index++, mod_pass_score);
        stmt.setString(index++, mod_instruct);

        stmt.setLong(index++, mod_max_attempt);
        stmt.setLong(index++, mod_max_usr_attempt);
        stmt.setBoolean(index++, mod_score_ind);
        stmt.setLong(index++, mod_score_reset);
        stmt.setString(index++, mod_logic);
        stmt.setString(index++, mod_usr_id_instructor);
        stmt.setBoolean(index++, mod_has_rate_q);
        stmt.setInt(index++, mod_show_answer_ind);
        stmt.setInt(index++, mod_sub_after_passed_ind);
        stmt.setInt(index++, mod_show_save_and_suspend_ind);
        stmt.setInt(index++, mod_managed_ind);
        stmt.setLong(index++, mod_tcr_id);
        stmt.setLong(index++, mod_required_time);
        stmt.setInt(index++, mod_download_ind);
        stmt.setInt(index++, mod_mobile_ind);
        stmt.setLong(index++, mod_show_answer_after_passed_ind);
        stmt.setString(index++, mod_test_style);
        stmt.setLong(index++, mod_res_id);
        int stmtResult=stmt.executeUpdate();
        stmt.close();
        if ( stmtResult!=1)
        {
            con.rollback();
            throw new qdbException("Failed to update Module.");
        }

        stmt = con.prepareStatement(upd_mRec);
        stmt.setString(1, res_tpl_name);
        stmt.setLong(2, mod_res_id);
        stmtResult=stmt.executeUpdate();
        stmt.close();
        if ( stmtResult!=1)
        {
            con.rollback();
            throw new qdbException("Failed to update Template.");
        }

        //Dennis, 2000-12-13, impl release control
        //If the new status == DATE, update the eff_start/end_datetime in Module
        //if(res_status.equalsIgnoreCase(RES_STATUS_DATE))
        //2001-01-05, all status will have eff datetime
        if (isChangeDate) {
            updateEffDatetime(con);
        }

        return;

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }
    }
    
    /**
     * this is cloned from the original upd method.
     * specially modified for class module of a course using common content
     */
    public void upd2(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, SQLException {
        res_id = mod_res_id;
        res_type = RES_TYPE_MOD;
        res_upd_user = prof.usr_id;

        super.checkTimeStamp(con);

        if (res_status.equalsIgnoreCase(RES_STATUS_ON) || res_status.equalsIgnoreCase(RES_STATUS_DATE)) {
            // Check if the question is ordered in 1,2,3....
            if (!checkQorder(con)) {
                //Questions are not in the correct order.
                throw new qdbErrMessage("MOD001");
            }
        }

        //if the module is standard test or dynamic test
        //make sure that you can only turn the module online
        //if the test has question/criteria defined in it
        if (res_status.equalsIgnoreCase(RES_STATUS_ON)) {
            if (mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {
                if (dbResourceContent.getResourceContentCount(con, mod_res_id) == 0) {
                    res_status = RES_STATUS_OFF;
                }
            } else if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT)) {
                if (dbModuleSpec.getModuleSpecCount(con, mod_res_id) == 0) {
                    res_status = RES_STATUS_OFF;
                }
            }
        }

        super.updateStatus(con);

        // permission records have to be cleared no matter mod_instructor is specified or not
        dbResourcePermission.delUserRoleIsNull(con, mod_res_id);
        if (mod_instructor_ent_id_lst != null) {
            for (int i = 0; i < mod_instructor_ent_id_lst.length; i++) {
                dbResourcePermission.save(con, mod_res_id, mod_instructor_ent_id_lst[i], null, true, true, false);
            }
        }

        //Dennis, 2000-12-13, impl release control
        //If the new status == DATE, update the eff_start/end_datetime in Module
        //if(res_status.equalsIgnoreCase(RES_STATUS_DATE))
        //2001-01-05, all status will have eff datetime
        updateEffDatetime(con);
    }

    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {

            // check User Right
//            checkModifyPermission(con, prof);
            //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}
            del(con);
    }

    public void del(Connection con) throws qdbErrMessage, qdbException ,cwSysMessage {
        try {
            super.checkTimeStamp(con);

            if(!MOD_TYPE_EVN.equals(this.res_subtype)) {
	            long cnt = dbProgress.attemptNum(con, mod_res_id);
	
	            if(cnt > 0) {
	                //The test had been attempted
	                throw new qdbErrMessage("PGR002");
	            }
	
	            // dbAiccPath Checking
	
	            cnt = dbModuleEvaluation.attemptNum(con, mod_res_id);
	            boolean exitsHis = dbModuleEvaluationHistory.existHis(con, mod_res_id);
	            if (cnt > 0 || exitsHis) {
	                throw new qdbErrMessage("PGR002");
	            }
	
	            cnt = dbAiccPath.attemptNum(con, mod_res_id);
	            if (cnt > 0) {
	                throw new qdbErrMessage("PGR002");
	            }
            } 
            
            // delete Modulecriteria
            if (DbCourseModuleCriteria.isActiveCriteria(con, mod_res_id)){
                throw new qdbErrMessage("MOD011");
            }
            
            if (isPublicUsed(con)){
                throw new qdbErrMessage("MOD010");
            }
            // delete all inActive Modulecriteria
            DbCourseModuleCriteria.delByModId(con, mod_res_id);

            //whether attempted resource can be deleted. 
            boolean deletedAttemptedQueInd = false;
            if(MOD_TYPE_EVN.equals(this.res_subtype)) {
            	deletedAttemptedQueInd = true;
            }
            
            // delete all the question first
            Vector queLst = new Vector();
            dbResourceContent resCon = new dbResourceContent();
            queLst = dbResourceContent.getChildAss(con , mod_res_id);
            for(int i=0;i<queLst.size();i++) {
                resCon = (dbResourceContent) queLst.elementAt(i);
                // do not delete the question if this resource content is only a link to the question
                // for a module in a class of a classroom that is using common content
                if (resCon.rcn_rcn_res_id_parent == 0) {
                    dbQuestion dbque = new dbQuestion();
                    dbque.deleted_attempted_que_ind = deletedAttemptedQueInd;
                    dbque.que_res_id = resCon.rcn_res_id_content;
                    dbque.del(con);
                }
            }

            // delete the resource content record
            PreparedStatement stmt2 = con.prepareStatement(
                " DELETE From ResourceContent where rcn_res_id_content = ?");

            stmt2.setLong(1, mod_res_id);
            stmt2.executeUpdate();
            stmt2.close();

            stmt2 = con.prepareStatement(
                " DELETE From ResourceContent where rcn_res_id = ? ");
            stmt2.setLong(1, mod_res_id);
            stmt2.executeUpdate();
            stmt2.close();

            stmt2 = con.prepareStatement(
                  " DELETE From ModuleSpec where msp_res_id = ? ");
            stmt2.setLong(1, mod_res_id);
            stmt2.executeUpdate();
            stmt2.close();

            stmt2 = con.prepareStatement("DELETE From ResourceObjective where rob_res_id = ?");
            stmt2.setLong(1, mod_res_id);
            stmt2.executeUpdate();
            stmt2.close();

            // delete access control list
            dbResourcePermission.delAll(con,mod_res_id);

            //delete messages
            dbMessage.delAllMsg(con,mod_res_id);
            
            if(deletedAttemptedQueInd) {
	            //delete relation moduleTrainingCenter
	            ModuleTrainingCenter.delete(con, mod_res_id);
	            
	            //delete relation dbModuleEvaluationHistory
	            dbModuleEvaluationHistory.delAllHistory(con, mod_res_id);
	            
	            //delete relation moduleEvaluation
	            dbModuleEvaluation.delByMod(con, mod_res_id);
	            
	            //delete dbProgress 
	            dbProgress.delByResId(con, mod_res_id);
            }

            try {
                DbCtGlossary ctGlossary = new DbCtGlossary();
                ctGlossary.glo_res_id = mod_res_id;
                ctGlossary.delByResId(con);
                
                DbCtReference ctReference = new DbCtReference();
                ctReference.ref_res_id = Long.valueOf(mod_res_id).intValue();
				ctReference.delByResId(con);
			} catch (cwException e) {
				CommonLog.error(e.getMessage(),e);
			}
			
			PreparedStatement stmt = con.prepareStatement(
               "update  Resources set res_mod_res_id_test= null where res_mod_res_id_test=? " );           
                stmt.setLong(1, mod_res_id);
                //stmt.setLong(2, mod_res_id);
               stmt.executeUpdate();
                stmt.close();
            
            stmt = con.prepareStatement(
                "DELETE From Module where mod_res_id=? " );           
            stmt.setLong(1, mod_res_id);
            //stmt.setLong(2, mod_res_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to delete Module. No such record.");
            }
            else
            {

                // calls dbResource.del()
                super.del(con);
                // if ok.
                // commit() at qdbAction.java
                return;
            }

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }


    public void get(Connection con)
            throws qdbException, cwSysMessage
    {
        try {
            res_id = mod_res_id;
            super.get(con);
            PreparedStatement stmt = con.prepareStatement(
            "SELECT  mod_type, "
            + " mod_max_score, "
            + " mod_pass_score, "
            + " mod_instruct, "
            + " mod_max_attempt, "
            + " mod_max_usr_attempt, "
            + " mod_score_ind, "
            + " mod_score_reset, "
            + " mod_logic, "
            + " mod_eff_start_datetime, "
            + " mod_eff_end_datetime, "
            + " mod_usr_id_instructor, "
            + " mod_has_rate_q, "
            + " mod_is_public, "
            + " mod_mod_id_root, "
            + " mod_show_answer_ind, "
            + " mod_sub_after_passed_ind, "
            + " mod_mod_res_id_parent, "
            + " mod_auto_save_ind, "
            + " mod_managed_ind,"
            + " mod_started_ind,"
            + " mod_tcr_id,"
            + " mod_required_time,"
            + " mod_download_ind," 
            + " mod_mobile_ind,"
            + " mod_show_A_A_passed_ind,"
            + " mod_test_style"
            + " from Module "
            + " where mod_res_id =?");

            // set the values for prepared statements
            stmt.setLong(1, mod_res_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                mod_type    = rs.getString("mod_type");
                mod_max_score = rs.getFloat("mod_max_score");
                mod_pass_score = rs.getFloat("mod_pass_score");
                mod_instruct = rs.getString("mod_instruct");
                mod_max_attempt = rs.getLong("mod_max_attempt");
                mod_max_usr_attempt = rs.getLong("mod_max_usr_attempt");
                mod_score_ind = rs.getBoolean("mod_score_ind");
                mod_score_reset  = rs.getLong("mod_score_reset");
                mod_logic = rs.getString("mod_logic");
                mod_eff_start_datetime = rs.getTimestamp("mod_eff_start_datetime");
                mod_eff_end_datetime = rs.getTimestamp("mod_eff_end_datetime");
                mod_usr_id_instructor = rs.getString("mod_usr_id_instructor");
                mod_has_rate_q = rs.getBoolean("mod_has_rate_q");
                mod_is_public = rs.getBoolean("mod_is_public");
                mod_show_answer_ind = rs.getInt("mod_show_answer_ind");
                mod_sub_after_passed_ind = rs.getInt("mod_sub_after_passed_ind");
                this.mod_mod_id_root = rs.getInt("mod_mod_id_root");
                mod_mod_res_id_parent = rs.getLong("mod_mod_res_id_parent");
                mod_show_save_and_suspend_ind = rs.getInt("mod_auto_save_ind");
                mod_managed_ind = rs.getInt("mod_managed_ind");
                mod_started_ind = rs.getInt("mod_started_ind");
                mod_tcr_id = rs.getLong("mod_tcr_id");
                mod_required_time = rs.getInt("mod_required_time");
                mod_download_ind = rs.getInt("mod_download_ind");
                mod_mobile_ind = rs.getInt("mod_mobile_ind");
                mod_show_answer_after_passed_ind=rs.getLong("mod_show_A_A_passed_ind");
                mod_test_style = rs.getString("mod_test_style");
            }
            else
            {
            	stmt.close();
                throw new qdbException( "No data for module. id = " + res_id );
            }

            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
	public void getScoreByModResId(Connection con) throws qdbException, cwSysMessage, SQLException{
		PreparedStatement stmt = con.prepareStatement(GET_BY_RES_ID_SQL);
		stmt.setLong(1,this.mod_res_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()){
			this.mod_max_score = rs.getFloat("mod_max_score");
			this.mod_pass_score = rs.getFloat("mod_pass_score");
		}
		cwSQL.cleanUp(rs, stmt);
	}
    // for AICC data
    public void getAicc(Connection con)
            throws qdbException, cwSysMessage
    {
        try {
            res_id = mod_res_id;

            super.get(con);

            PreparedStatement stmt = con.prepareStatement(
            "SELECT  mod_core_vendor, "
            + " mod_web_launch, "
            + " mod_password, "
            + " mod_time_limit_action, "
            + " mod_vendor, "
            + " mod_aicc_version, "
            + " mod_import_xml "
            + " from Module "
            + " where mod_res_id =?");

            // set the values for prepared statements
            stmt.setLong(1, mod_res_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                //--- modification for using clob specific methods (2002.04.22 kawai)
                //mod_core_vendor = rs.getString("mod_core_vendor");
                mod_core_vendor = cwSQL.getClobValue(rs, "mod_core_vendor");
                mod_web_launch = rs.getString("mod_web_launch");
                mod_password = rs.getString("mod_password");
                mod_time_limit_action = rs.getString("mod_time_limit_action");
                mod_vendor = rs.getString("mod_vendor");
                mod_aicc_version = rs.getString("mod_aicc_version");
                mod_import_xml = cwSQL.getClobValue(rs, "mod_import_xml");
            }
            else
            {
            	stmt.close();
                throw new qdbException( "No data for module. id = " + res_id );
            }

            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    /**
     * 获取包括AICC的所有信息
     */
    public void getAllAicc(Connection con)
    		throws qdbException, cwSysMessage {
    	
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
            res_id = mod_res_id;
            super.get(con);
            
            stmt = con.prepareStatement(
            "SELECT  mod_type, "
            + " mod_max_score, "
            + " mod_pass_score, "
            + " mod_instruct, "
            + " mod_max_attempt, "
            + " mod_max_usr_attempt, "
            + " mod_score_ind, "
            + " mod_score_reset, "
            + " mod_logic, "
            + " mod_eff_start_datetime, "
            + " mod_eff_end_datetime, "
            + " mod_usr_id_instructor, "
            + " mod_has_rate_q, "
            + " mod_is_public, "
            + " mod_mod_id_root, "
            + " mod_show_answer_ind, "
            + " mod_sub_after_passed_ind, "
            + " mod_mod_res_id_parent, "
            + " mod_auto_save_ind, "
            + " mod_core_vendor, "
            + " mod_web_launch, "
            + " mod_password, "
            + " mod_time_limit_action, "
            + " mod_vendor, "
            + " mod_aicc_version, "
            + " mod_test_style, "
            + " mod_import_xml,"
            + " mod_mobile_ind "
            + " from Module "
            + " where mod_res_id =?");

            // set the values for prepared statements
            stmt.setLong(1, mod_res_id);

            rs = stmt.executeQuery();
            if (rs.next()) {
                mod_type    = rs.getString("mod_type");
                mod_max_score = rs.getFloat("mod_max_score");
                mod_pass_score = rs.getFloat("mod_pass_score");
                mod_instruct = rs.getString("mod_instruct");
                mod_max_attempt = rs.getLong("mod_max_attempt");
                mod_max_usr_attempt = rs.getLong("mod_max_usr_attempt");
                mod_score_ind = rs.getBoolean("mod_score_ind");
                mod_score_reset  = rs.getLong("mod_score_reset");
                mod_logic = rs.getString("mod_logic");
                mod_eff_start_datetime = rs.getTimestamp("mod_eff_start_datetime");
                mod_eff_end_datetime = rs.getTimestamp("mod_eff_end_datetime");
                mod_usr_id_instructor = rs.getString("mod_usr_id_instructor");
                mod_has_rate_q = rs.getBoolean("mod_has_rate_q");
                mod_is_public = rs.getBoolean("mod_is_public");
                mod_show_answer_ind = rs.getInt("mod_show_answer_ind");
                mod_sub_after_passed_ind = rs.getInt("mod_sub_after_passed_ind");
                this.mod_mod_id_root = rs.getInt("mod_mod_id_root");
                mod_mod_res_id_parent = rs.getLong("mod_mod_res_id_parent");
                mod_show_save_and_suspend_ind = rs.getInt("mod_auto_save_ind");
                //AICC information
                mod_core_vendor = cwSQL.getClobValue(rs, "mod_core_vendor");
                mod_web_launch = rs.getString("mod_web_launch");
                mod_password = rs.getString("mod_password");
                mod_time_limit_action = rs.getString("mod_time_limit_action");
                mod_vendor = rs.getString("mod_vendor");
                mod_aicc_version = rs.getString("mod_aicc_version");
                mod_import_xml = cwSQL.getClobValue(rs, "mod_import_xml");
                mod_test_style =  rs.getString("mod_test_style");
                mod_mobile_ind = rs.getInt("mod_mobile_ind");
            } else {
                throw new qdbException( "No data for module. id = " + res_id );
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        } finally {
        	cwSQL.closePreparedStatement(stmt);
        }
    }

    public String getUserReport(Connection con, String usrId, String[] que_id_lst, long attempt_nbr, loginProfile prof, String metaXML, qdbEnv static_env, long ur_tkh_id)
            throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException
    {
/*        if ((!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) &&
            (!prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) &&
            (!prof.usr_id.equals(usrId))) {
*/

    	if(ur_tkh_id > 0){
    		tkh_id = ur_tkh_id;
    	}else{
    		if (tkh_id==DbTrackingHistory.TKH_ID_UNDEFINED){
    			CommonLog.info("!!!!!!get tracking id in dbModule.getUserReport, with role:" + prof.current_role);
    			tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, res_id, prof.usr_ent_id);
    		}
    	}
        
        String result ;
        dbProgress pgr = new dbProgress();
        pgr.pgr_usr_id = usrId;
        pgr.pgr_res_id = mod_res_id;
        pgr.pgr_tkh_id = tkh_id;
        if (attempt_nbr == 0 )
            pgr.get(con);
        else
            pgr.get(con,attempt_nbr);

        result = pgr.rptUserAsXML(con, que_id_lst, prof, metaXML, static_env);

        get(con);

        if (mod_score_reset != 0 && pgr.pgr_attempt_nbr >= mod_score_reset) {
            try {
                pgr.clearScore(con);
                con.commit();
            } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
            }
        }

        return result;

    }

    // NEW
    public String getGroupReportFromList(Connection con, String[] que_id_lst, String[] rpt_group_lst, long attempt_nbr, loginProfile prof)
            throws qdbException, qdbErrMessage, SQLException, cwSysMessage
    {
        String result = "";
/*
        if ((!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) &&
            (!prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER))) {
*/

        dbProgress pgr = new dbProgress();
        pgr.pgr_res_id = mod_res_id;
        pgr.pgr_attempt_nbr = attempt_nbr;

        result = pgr.rptGroupListAsXML(con, que_id_lst, rpt_group_lst, prof);

        return result;
    }

    public String getGroupReport(Connection con, String[] que_id_lst, long entId, long attempt_nbr, loginProfile prof)
            throws qdbException, qdbErrMessage, cwSysMessage
    {
        String result ;
        dbProgress pgr = new dbProgress();
        pgr.pgr_res_id = mod_res_id;
        pgr.pgr_attempt_nbr = attempt_nbr;
        /*
        if ((!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) &&
            (!prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER))) {
        */
        result = pgr.rptGroupAsXML(con, que_id_lst, entId, prof);

        return result;

    }

    public String getTestReport(Connection con, loginProfile prof)
            throws qdbException, cwSysMessage
    {
        /*
        if ((!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) &&
            (!prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER))) {
        */
        String result ;
        dbProgress pgr = new dbProgress();
        pgr.pgr_res_id = mod_res_id;
        result = pgr.rptTestAsXML(con, prof);

        return result;

    }

    public void insObj(Connection con, long objId, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        res_upd_user = prof.usr_id ;

        // check whether the course was attempted.
        checkStat(con);
        mod_res_id = res_id;

        get(con);


        if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT)) {
            dbModuleSpec dbmsp = new dbModuleSpec();
            dbmsp.msp_res_id = res_id;
            dbmsp.msp_obj_id = objId;
            dbmsp.save(con);

        }else {
            String aobjs = ""; // (1, 2, 34)

            /*
            CL (17 Dec 2001) : No Assessment Objective

            aobjs = dbObjective.getChildAssId(con,objId);
            */
            aobjs = " (" + objId + ") ";

            if (aobjs == "") {
                    throw new qdbException( "No resource objective selected. ");
            }
                String SQL =
                        " INSERT INTO ResourceObjective "
                    +   " SELECT " + res_id + ", obj_id "
                    +   "  FROM Objective "
                    +   " WHERE obj_id IN " + aobjs
                    +   "   AND obj_id NOT IN (SELECT rob_obj_id from ResourceObjective where "
                    +   "                             rob_res_id = " + res_id + ") ";


            try {
                    PreparedStatement stmt = con.prepareStatement(SQL);
                    stmt.executeUpdate();
                    super.updateTimeStamp(con);
                    stmt.close();
                    // commit() at qdbAction.java;
            } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
            }
        }
    }

    public void delObj(Connection con, String objIds[], loginProfile prof, boolean isParent)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
      try {
        res_upd_user = prof.usr_id ;

        // check User Right
//        checkModifyPermission(con, prof);
        //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}

        // check whether the module was attempted.
        checkStat(con);

        // check timeStamp
        super.checkTimeStamp(con);
        get(con);

        String objLst = "(0 " ;
        for (int i=0; i<objIds.length;i++) {
            objLst += ", " + objIds[i];
        }
        objLst += ")";

        PreparedStatement stmt = con.prepareStatement(
            " DELETE From ResourceObjective "
          + "    where rob_res_id = ? and rob_obj_id IN " + objLst );

        stmt.setLong(1, mod_res_id);
        stmt.executeUpdate();
        stmt.close();

//        stmt = con.prepareStatement(
//            " DELETE From ModuleSpec "
//          + "    where msp_res_id = ? and msp_obj_id IN " + objLst );

//        stmt.setLong(1, mod_res_id);
//        stmt.executeUpdate();
        stmt.close();

        /* delete */

        // delete all the question with this objective
        PreparedStatement stmt1 = con.prepareStatement(
        " SELECT rcn_res_id_content FROM ResourceContent "
        + " where rcn_res_id = ? and rcn_obj_id_content IN " + objLst);

        stmt1.setLong(1, mod_res_id);

        ResultSet rs1 = stmt1.executeQuery();
        dbResourceContent resCon = new dbResourceContent();

        while (rs1.next())  {
            resCon.rcn_res_id = mod_res_id;
            resCon.rcn_res_id_content = rs1.getLong("rcn_res_id_content");

            // check whether statistics exists in Progress Attempt
            if (dbProgressAttempt.checkQStatExist(con, resCon.rcn_res_id, resCon.rcn_res_id_content)) {
                //String  mesg = "Failed to delete the objective\n";
                //        mesg += dbUtils.NEWL;
                //        mesg += "\nReason : Question(s) under this objective had been attempted.";
                stmt1.close();
                throw new qdbErrMessage("MOD003");

            } else {
                resCon.get(con);
                resCon.del(con);
                if (isParent) {
                    dbResource myDbResource = new dbResource();
                    myDbResource.res_id = resCon.rcn_res_id_content;
                    myDbResource.get(con);
                    if (myDbResource.res_subtype.equalsIgnoreCase(RES_SUBTYPE_FSC)) {
                        FixedScenarioQue myFixedScenarioQue = new FixedScenarioQue();
                        myFixedScenarioQue.res_id = resCon.rcn_res_id_content;
                        myFixedScenarioQue.del(con, prof);
                    } else if (myDbResource.res_subtype.equalsIgnoreCase(RES_SUBTYPE_DSC)) {
                        DynamicScenarioQue myDynamicScenarioQue = new DynamicScenarioQue();
                        myDynamicScenarioQue.res_id = resCon.rcn_res_id_content;
                        myDynamicScenarioQue.del(con, prof);
                    } else {
                        // TDO : Delete the question from the database
                        dbQuestion dbque = new dbQuestion();
                        dbque.que_res_id = resCon.rcn_res_id_content;
                        dbque.del(con);
                    }
                }
            }
        }
        stmt1.close();

        updMaxScore(con);
        fixQueOrder(con);
        super.updateTimeStamp(con);
        // con.commit() at qdbAction.java

      } catch(SQLException e) {
        throw new qdbException("SQL Error: " + e.getMessage());
      }
    }


	public void delMsp(Connection con, String mspIds[], loginProfile prof)
		throws qdbException, qdbErrMessage, cwSysMessage
	{
	  try {
		res_upd_user = prof.usr_id ; 
        
		// check whether the module was attempted.
		checkStat(con);
        
		// check timeStamp 
		super.checkTimeStamp(con);
		get(con); 
        
		String mspLst = "(0 " ; 
		for (int i=0; i<mspIds.length;i++) {
			mspLst += ", " + mspIds[i]; 
		}
		mspLst += ")";
        
		PreparedStatement stmt = con.prepareStatement(
			" DELETE From ModuleSpec " 
		  + "    where msp_id IN " + mspLst );
        
		stmt.executeUpdate(); 
		cwSQL.cleanUp(null, stmt);
		updMaxScore(con);
		super.updateTimeStamp(con);
		// con.commit() at qdbAction.java
        
	  } catch(SQLException e) {
		throw new qdbException("SQL Error: " + e.getMessage()); 
	  }
	}

	public String asXML(Connection con, loginProfile prof, String dpo_view, String ssoXml) throws qdbException, cwSysMessage, SQLException {
		return asXML(con, prof, dpo_view, ssoXml, false);
	}

    public String asXML(Connection con, loginProfile prof, String dpo_view, String ssoXml, boolean tcEnabled)
        throws qdbException, cwSysMessage, SQLException
    {
    	    StringBuffer result = new StringBuffer();
            result.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL);
            result.append("<module id=\"").append(res_id).append("\" res_status=\"").append(res_status).append("\" language=\"")
            	  .append(res_lan).append("\" timestamp=\"").append(res_upd_date).append("\" mod_required_time=\"").append(mod_required_time).append("\">").append(dbUtils.NEWL);

            if (this.mod_mod_id_root > 0) {
                dbResource tmpResource = new dbResource();
                tmpResource.res_id = this.mod_mod_id_root;
                tmpResource.get(con);
                result.append("<survey_template id=\"").append(this.mod_mod_id_root).append("\" title=\"").append(cwUtils.esc4XML(tmpResource.res_title)).append("\"/>").append(dbUtils.NEWL);
            }

            // author's information
            result.append(prof.asXML()).append(dbUtils.NEWL);
            
            //tc_enabled
            if(tcEnabled) {
            	result.append("<tc_enabled>").append(tcEnabled).append("</tc_enabled>").append(dbUtils.NEWL);
            }
            
            result.append(ssoXml);
            
            result.append(dbResourcePermission.aclAsXML(con,res_id,prof));
            // Module Header
            result.append(getModHeader(con, prof));
            res_id = mod_res_id;
            result.append(dbModuleEvaluation.getModuleEvalAsXML(con, mod_res_id, prof.usr_ent_id, tkh_id));
            
            //get default tc and tc of module
            if(MOD_TYPE_EVN.equalsIgnoreCase(mod_type)) {
            	//default tc
            	long tcr_id = 0;
            	if(tcEnabled) {
            		tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
                } else {
                	tcr_id =DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
                }
            	DbTrainingCenter tcr = DbTrainingCenter.getInstance(con, tcr_id);
            	if(tcr != null) {
                	StringBuffer xmlBuf = new StringBuffer();
                    xmlBuf.append("<default_training_center id =\"").append(tcr.getTcr_id()).append("\">");
                    xmlBuf.append("<title>").append(cwUtils.esc4XML(tcr.getTcr_title())).append("</title>");
                    xmlBuf.append("</default_training_center>");
                    result.append(xmlBuf.toString());
                }
            	
            	//tc of module
            	ModuleTrainingCenter modTrainingCenter = new ModuleTrainingCenter();
            	modTrainingCenter.mtc_mod_id = mod_res_id;
            	HashMap tcMap = modTrainingCenter.getTcByModId(con);
            	if(tcMap != null) {
            		result.append("<training_center id=\"").append(tcMap.get(ModuleTrainingCenter.KEY_TCR_ID)).append("\">").append(dbUtils.NEWL);
            		result.append("<title>").append(cwUtils.esc4XML((String)tcMap.get(ModuleTrainingCenter.KEY_TCR_TITLE))).append("</title>");
            		result.append("</training_center>");
            	}
            	result.append(String.format("<isMobile>%s</isMobile>", modTrainingCenter.isMobile(con)));
            }

            result.append(getDisplayOption(con, dpo_view));
            result.append(getIsEnrollmentRelatedAsXML(con));
            result.append("<res_vod_main>").append(dbUtils.NEWL);
            if(res_vod_main != null && res_vod_main.length() > 0){
            	result.append(dbUtils.esc4XML(res_vod_main));
            }
            result.append(dbUtils.NEWL).append("</res_vod_main>");
            result.append("<body>").append(dbUtils.NEWL);

            if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT) || mod_type.equalsIgnoreCase(MOD_TYPE_STX))
                result.append(dynObjLstAsXML(con, res_id));
            else
                result.append(tstObjLstAsXML(con, res_id));

            result.append(extension).append(dbUtils.NEWL).append("</body>").append(dbUtils.NEWL).append("</module>");
            
            return result.toString();
    }

    public static String tstObjLstAsXML(Connection con, long modId)
        throws qdbException
    {
        try {
            StringBuffer result = new StringBuffer();

			dbResource myDbResource = new dbResource();
			myDbResource.res_id = modId;
			try {
				myDbResource.get(con);
			} catch(cwSysMessage e) {
				throw new qdbException(e.toString());
			}

            // objective list
             Vector objIdVec = new Vector();
			if (myDbResource.res_type.equalsIgnoreCase(dbResource.RES_TYPE_ASM)) {
				objIdVec = dbResourceObjective.getAsmObjId(con, modId);
			}
			else {
             objIdVec = dbResourceObjective.getObjId(con, modId);
			}            
            
            /*new container for new requirement
              HashMap objMap = new HashMap();
              objMap = dbResourceObjective.getObj(con,modId);
              objIdVec = (Vector)objMap.get("keyVec"); */
            dbObjective obj = new dbObjective();
            dbResourceObjective rob = new dbResourceObjective();

            int i;
			StringBuffer objPathBuf = new StringBuffer();

            for (i=0;i<objIdVec.size();i++)
            {
                rob =(dbResourceObjective) objIdVec.elementAt(i);
                obj.obj_id = rob.rob_obj_id;
                obj.get(con);

                PreparedStatement stmt = con.prepareStatement(
                "SELECT rcn_res_id_content, rcn_score_multiplier, que_score, res_duration "
                + " from ResourceContent, Question, Resources "
                + " where rcn_res_id =? and que_res_id = rcn_res_id_content "
                + "      and res_id=rcn_res_id_content and rcn_obj_id_content = ? "
                + "         order by rcn_order " );

                stmt.setLong(1, modId);
                stmt.setLong(2, rob.rob_obj_id);

                int count = 0;
                float duration = 0;
                long  score = 0;

                ResultSet rs = stmt.executeQuery();
                while(rs.next())
                {
                    count ++;
                    duration += rs.getLong("res_duration");
                    score += rs.getLong("rcn_score_multiplier") * rs.getInt("que_score");
                }
                stmt.close();
                Vector obj_ancestors_vec = obj.getObjAncesters(con);
		        result.append("<objective id=\"").append(obj.obj_id).append("\" type=\"").append(obj.obj_type).append("\" status=\"").append(obj.obj_status);
		        if(obj_ancestors_vec.size()!=0){
		        objPathBuf.append("<objective_path id=\"").append(obj.obj_id).append("\">");
				for(int j=0; j<obj_ancestors_vec.size();j++){
					objPathBuf.append("<objective>").append(cwUtils.esc4XML((String)obj_ancestors_vec.elementAt(j))).append("</objective>");
				}
				objPathBuf.append("</objective_path>");
		        }
		        result.append("\" q_count=\"").append(count).append("\" q_score=\"" ).append(score);
                result.append("\" duration=\"").append(duration);
                result.append("\">").append(dbUtils.esc4XML(obj.obj_desc)).append("</objective>").append(dbUtils.NEWL);
            }
				result.append(objPathBuf);
                return result.toString() ;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static String dynObjLstAsXML(Connection con, long modId)
        throws qdbException, SQLException
    {
        StringBuffer xmlBuf = new StringBuffer(256);

        // objective list
        Vector objIdVec = new Vector();
        objIdVec = dbModuleSpec.getModuleSpecs(con, modId);

        dbObjective obj = new dbObjective();
        dbModuleSpec dbmsp = new dbModuleSpec();

        int i;
        for (i=0;i<objIdVec.size();i++)
        {
            dbmsp =(dbModuleSpec) objIdVec.elementAt(i);
            obj.obj_id = dbmsp.msp_obj_id;
            obj.get(con);
            String[] qType = null;
            if(dbmsp.msp_type != null) {
                qType = cwUtils.splitToString(dbmsp.msp_type, "~");
            }   

            xmlBuf.append("<objective id=\"").append(obj.obj_id).append("\" type=\"").append(obj.obj_type);
            xmlBuf.append("\" msp_id=\"").append(dbmsp.msp_id);
            xmlBuf.append("\" q_type=\"").append(dbmsp.msp_type).append("\" status=\"").append(obj.obj_status);
            xmlBuf.append("\" q_score=\"" + dbmsp.msp_score);
            xmlBuf.append("\" difficulty=\"").append(dbmsp.msp_difficulty);
            xmlBuf.append("\" privilege=\"").append(dbmsp.msp_privilege);
            xmlBuf.append("\" duration=\"").append(dbmsp.msp_duration * dbmsp.msp_qcount);
            xmlBuf.append("\" q_count=\"").append(dbmsp.msp_qcount);
            xmlBuf.append("\" algorithm=\"").append(dbmsp.msp_algorithm);
            xmlBuf.append("\">");

            xmlBuf.append("<type_list>");
            if(qType != null && qType.length > 0) {
                for(int j=0; j<qType.length; j++) {
                    xmlBuf.append("<type>").append(qType[j]).append("</type>");
                }
            }
            xmlBuf.append("</type_list>");
            xmlBuf.append("<desc>").append(dbUtils.esc4XML(obj.obj_desc)).append("</desc>");
            xmlBuf.append("</objective>").append(dbUtils.NEWL);
        }

        return xmlBuf.toString() ;
    }

    public long addQ(Connection con, dbQuestion que, String[] robs, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try {
            res_upd_user = prof.usr_id ;

            // check User Right
//            checkModifyPermission(con, prof);
            //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            // check whether the course was attempted.
            checkStat(con);

            dbQuestion dbque = new dbQuestion();
            dbque = que;
            dbque.res_upd_user = prof.usr_id;
            dbque.res_usr_id_owner = prof.usr_id;
            dbque.ins(con, robs, dbResource.RES_TYPE_QUE);

            PreparedStatement stmt = con.prepareStatement(
                " UPDATE Resources "
            +  "  SET res_mod_res_id_test = ? "
            +  "  WHERE res_id  = ? " );

            stmt.setLong(1,mod_res_id);
            stmt.setLong(2,dbque.que_res_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to set mod_res_id_test.");
            }

            dbResourceContent resCon = new dbResourceContent();
            resCon.rcn_res_id = mod_res_id;
            resCon.rcn_res_id_content = dbque.que_res_id;

            if (robs[0] != null )
                resCon.rcn_obj_id_content = Long.parseLong(robs[0]);
            resCon.ins(con);

            // Calculate the max score after adding the question to the test
            updMaxScore(con);
            super.updateTimeStamp(con);

            return dbque.que_res_id;

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public void insChild(Connection con, String [] files)
        throws qdbException
    {
        dbResource childRes;  //store the child res
        dbResourceContent resContent;
        int i;

        for(i=0;i<files.length;i++) {
            childRes = new dbResource();
            childRes.res_type = mod_type;
            childRes.res_subtype = RES_SUBTYPE_FILE;

            childRes.res_src_type = dbResource.SRC_TYPE_FILE;
            childRes.res_src_link = files[i];
            //childRes.res_filename = files[i];

            //insert into Resource(the child)
            childRes.ins(con);

            //insert into ResourceContent
            resContent = new dbResourceContent();
            resContent.rcn_res_id = mod_res_id;
            resContent.rcn_res_id_content=childRes.res_id;
            resContent.ins(con);
        }
    }

    public void insQ(Connection con, String[] que_id_lst, String uploadDir , loginProfile prof, Vector vtClassModId)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try {
            res_upd_user = prof.usr_id ;

            // check User Right
//            checkModifyPermission(con, prof);
            //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            // check timeStamp
            //super.checkTimeStamp(con);

            // check whether the course was attempted.
            checkStat(con);

            get(con);

            int i;
            dbResourceContent resObj = new dbResourceContent();
            Vector failList = new Vector();
            long newId = 0;

            Vector idobjVec = dbResourceObjective.getResObj(con, que_id_lst);
            Vector idVec = (Vector) idobjVec.elementAt(0);
            Vector objVec = (Vector) idobjVec.elementAt(1);

            for(i =0;i< idVec.size();i++) {
                resObj.rcn_res_id = mod_res_id;
                long tmpId = ((Long) idVec.elementAt(i)).longValue();
                long objId = ((Long) objVec.elementAt(i)).longValue();
                //  duplicate the question
				newId = dbQuestion.insTestQ(con, mod_res_id, tmpId, objId, res_upd_user, uploadDir, prof, true);
                if (newId <=0) {
                    failList.addElement(new Long(tmpId));
                }else {
                    resObj.rcn_res_id_content = newId;
                    resObj.rcn_obj_id_content = objId;
                    resObj.rcn_rcn_res_id_parent = 0;
                    resObj.rcn_rcn_sub_nbr_parent = 0;
                    resObj.ins(con);

                    dbUtils.copyMediaFrom(uploadDir, tmpId, newId);
                    
                    for (int j = 0; vtClassModId != null && j < vtClassModId.size(); j++) {
                        resObj.rcn_res_id = ((dbModule) vtClassModId.get(j)).mod_res_id;
                        resObj.rcn_res_id_content = newId;
                        resObj.rcn_obj_id_content = objId;
                        resObj.rcn_rcn_res_id_parent = mod_res_id;
                        resObj.rcn_rcn_sub_nbr_parent = resObj.rcn_sub_nbr;
                        resObj.ins(con);
                    }
                }
            }
            // Calculate the max score after adding the question to the test
            updMaxScore(con);
            updMaxScoreForChild(con);
            super.updateTimeStamp(con);
            con.commit();

            if (failList.size() > 0) {
                //String mesg = "The following question(s) are failed to insert into the test :\n";
                //mesg += dbUtils.NEWL;

                String mesg = new String();
                for (i=0;i<failList.size();i++) {
                    if (i!=0)
                        mesg += ", ";

                    mesg += ((Long) failList.elementAt(i)).longValue();
                }

                //mesg += "\nReason : Question(s) exist in the test.";

                throw new qdbErrMessage("MOD002", mesg);
            }

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }

    }
/*
    public void delChildLst(Connection con, loginProfile prof, String [] rcn_res_id_content)
        throws qdbException, qdbErrMessage {

        int i;
        try {
            for(i=0;i<rcn_res_id_content.length;i++)
                delChild(con, prof, Integer.parseInt(rcn_res_id_content[i]));

            res_upd_user = prof.usr_id;
            res_id = mod_res_id;
            updateTimeStamp(con);
        }
        catch(NumberFormatException e) {
            throw new qdbException(e.getMessage());
        }
    }
*/
    /*
    public void updChild(Connection con, String[] files)
      throws qdbException {
        delChild(con);
        insChild(con, files);
    }

    public void delChild(Connection con)
      throws qdbException {
        try {
            Vector children = new Vector();
            dbResourceContent childRes;
            String idLst="";
            PreparedStatement stmt;
            String SQL;

            //get the children id list
            children = dbResourceContent.getChildAss(con , mod_res_id);
            for(int i=0;i<children.size();i++) {
                childRes = (dbResourceContent) children.elementAt(i);
                idLst += childRes.rcn_res_id_content + ",";
            }

            idLst = idLst.substring(0, idLst.length() - 1);  //remove the last ","
            idLst = "(" + idLst + ")";

            //delete the children in ResourceContent
            SQL = "Delete from ResourceContent where rcn_res_id_content in " + idLst;
            stmt = con.prepareStatement(SQL);
            //stmt.setString(1, idLst);
            stmt.executeUpdate();

            //delete the children in Resource
            SQL = "Delete from Resources where res_id in " + idLst ;
            stmt = con.prepareStatement(SQL);
            //stmt.setString(1, idLst);
            stmt.executeUpdate(SQL);
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }
    */

    public void delQ(Connection con, String[] que_id_lst, loginProfile prof, boolean isParent)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try{
            res_upd_user = prof.usr_id ;

            // check User Right
//            checkModifyPermission(con, prof);

            //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            // check timeStamp
            super.checkTimeStamp(con);

            // check whether the course was attempted.
            checkStat(con);

            get(con);

            int i;
            Vector failList = new Vector();
            Long idObj = null;

            for (i=0;i<que_id_lst.length; i++) {

                dbResourceContent resObj = new dbResourceContent();

                resObj.rcn_res_id = mod_res_id;
                long tmpId = Long.parseLong(que_id_lst[i]) ;
                resObj.rcn_res_id_content = tmpId;

                // check whether statistics exists in Progress Attempt
                if (dbProgressAttempt.checkQStatExist(con, resObj.rcn_res_id, resObj.rcn_res_id_content)) {
                    idObj = new Long(tmpId);
                    failList.addElement(idObj);
                }else {
                    resObj.get(con);
                    resObj.del(con);
                    if (isParent) {
                        dbResource myDbResource = new dbResource();
                        myDbResource.res_id = resObj.rcn_res_id_content;
                        myDbResource.get(con);

                        if (myDbResource.res_subtype.equalsIgnoreCase(RES_SUBTYPE_FSC)) {
                            FixedScenarioQue myFixedScenarioQue = new FixedScenarioQue();
                            myFixedScenarioQue.res_id = resObj.rcn_res_id_content;
                            myFixedScenarioQue.del(con, prof);
                        } else if (myDbResource.res_subtype.equalsIgnoreCase(RES_SUBTYPE_DSC)) {
                            DynamicScenarioQue myDynamicScenarioQue = new DynamicScenarioQue();
                            myDynamicScenarioQue.res_id = resObj.rcn_res_id_content;
                            myDynamicScenarioQue.del(con, prof);
                        } else {
                            dbQuestion dbque = new dbQuestion();
                            dbque.que_res_id = resObj.rcn_res_id_content;
                            dbque.del(con);
                        }
                    }
                }
            }

            // calculate the max score of the test after deleting the question(s).
            updMaxScore(con);
			fixQueOrder(con);
            super.updateTimeStamp(con);
            con.commit();

            if (failList.size() > 0) {
                //String mesg = "The following question(s) are failed to delete from the module :\n";
                //mesg += dbUtils.NEWL;

                String mesg = new String();
                for (i=0;i<failList.size();i++) {
                    idObj = (Long) failList.elementAt(i);
                    if (i!=0)
                        mesg += ", ";

                    mesg += idObj.longValue() ;
                }

                //mesg += "\nReason : The question(s) had been attempted.";

                throw new qdbErrMessage("MOD003", mesg);

            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public boolean checkQorder(Connection con)
        throws qdbException, qdbErrMessage
    {

        // The selected question is already ordered.
        Vector resCon = dbResourceContent.getChildAss(con,mod_res_id);
        dbResourceContent rcn = new dbResourceContent();

        for(int i=1;i<=resCon.size();i++) {
            rcn = (dbResourceContent) resCon.elementAt(i-1);
            if (rcn.rcn_order != i)
                return false;
        }

        return true;
    }



    public void reorderQ(Connection con, String[] que_id_lst, String[] que_order_lst, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        //try {
            res_upd_user = prof.usr_id ;

            super.checkTimeStamp(con);

            // check User Right
//            checkModifyPermission(con, prof);
            //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}
            // check whether the course was attempted.
            checkStat(con);

            int i;
            for (i=0;i<que_id_lst.length; i++) {
                dbResourceContent resObj = new dbResourceContent();
                resObj.rcn_res_id = mod_res_id;
                long tmpId = Long.parseLong(que_id_lst[i]) ;
                resObj.rcn_res_id_content = tmpId;
                long tmpOrder = Long.parseLong(que_order_lst[i]);
                resObj.rcn_order = tmpOrder;
                resObj.updOrder(con);
            }

            super.updateTimeStamp(con);
            // commit() at qdbAction.java

        //} catch(SQLException e) {
        //    throw new qdbException("SQL Error: " + e.getMessage());
        //}
    }

    public void assignScore(Connection con, String[] que_id_lst, String[] que_multiplier_lst, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        //try {

            res_upd_user = prof.usr_id ;

            super.checkTimeStamp(con);

            // check User Right
//            checkModifyPermission(con, prof);
            //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            int i;
            for (i=0;i<que_id_lst.length; i++) {

                dbResourceContent resObj = new dbResourceContent();
                resObj.rcn_res_id = mod_res_id;
                long tmpId = Long.parseLong(que_id_lst[i]) ;
                resObj.rcn_res_id_content = tmpId;
                long tmpMultiplier = Long.parseLong(que_multiplier_lst[i]);
                resObj.rcn_score_multiplier = tmpMultiplier;

                int oldScore = resObj.getScore(con);
                resObj.updMultiplier(con);
                int newScore = resObj.getScore(con);

            }

            updMaxScore(con);
            super.updateTimeStamp(con);
            // commit() at qdbAction.java

        //} catch(SQLException e) {
        //    throw new qdbException("SQL Error: " + e.getMessage());
        //}
    }

    public void saveSpec(Connection con,  dbModuleSpec dbmsp , loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        res_upd_user = prof.usr_id ;

        // check User Right
//            checkModifyPermission(con, prof);
        //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}
        // check whether the course was attempted.
        checkStat(con);
        // check timeStamp
        //super.checkTimeStamp(con);

        dbmsp.save(con);
        super.updateTimeStamp(con);
    }

/*
    public String getSuqAsXML(Connection con, Vector resIdVec)
        throws qdbException, cwSysMessage
    {
        // Get the list of question from the test;
        Vector qArray = dbResourceContent.getChildAss(con,mod_res_id);

        String qList = "";
        dbQuestion dbq = new dbQuestion();
        dbResourceContent rcn = new dbResourceContent();
        long order;
        int i;

        mod_size = qArray.size();

        for (i=0;i < qArray.size();i++) {
                rcn = (dbResourceContent) qArray.elementAt(i);
                dbq.que_res_id = rcn.rcn_res_id_content;
                dbq.res_id = dbq.que_res_id;
                dbq.get(con);
                order = rcn.rcn_order;
                qList += dbq.asXML(con, order);
                resIdVec.addElement(new Long(dbq.que_res_id));
        }
        return qList;
    }
*/

    public String genTestAsXML(Connection con, loginProfile prof, dbModuleSpec dbmsp, String[] robs, float time_limit, String uploadDir, Vector resIdVec, long tkh_id)
        throws qdbException, qdbErrMessage, cwSysMessage, SQLException, cwException
    {
        return genTestAsXML(con, prof, dbmsp, robs, time_limit, uploadDir, resIdVec, false, tkh_id, false, true);
    }

    // isShuffleMCQue: indicate if the MC question in this test needs to be shuffled
    public String genTestAsXML(Connection con, loginProfile prof, dbModuleSpec dbmsp, String[] robs, float time_limit, String uploadDir, Vector resIdVec, boolean showAnswer, long tkh_id, boolean boolPreviewAssessment, boolean isShuffleMCQue)
        throws qdbException, qdbErrMessage, cwSysMessage, SQLException, cwException
    {

        String qList = "";

        if (mod_type.equalsIgnoreCase(MOD_TYPE_STX))
            res_duration = time_limit;

        // pass the resIdVec as reference and store all the queId in the test
        // return to qdbAction for access control
        if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT) ||
                mod_type.equalsIgnoreCase(MOD_TYPE_STX)) {
			qList = getDynamicQueAsXML(con, prof.usr_id, dbmsp, robs, uploadDir, resIdVec, boolPreviewAssessment, isShuffleMCQue); 
        }else {
            qList = getStaticQueAsXML(con, resIdVec, isShuffleMCQue);
        }

        Timestamp curTime = dbUtils.getTime(con);
		int scenarioQueCount = dbResource.countScenarioQueInList(con, resIdVec);

        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<quiz id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date ;
		result += "\" start_time=\"" + curTime + "\" size=\"" + (resIdVec.size()-scenarioQueCount) 
			   + "\" total_score=\"" + (int)mod_max_score + "\">" + dbUtils.NEWL;
        result += "<key>" +  Math.round(Math.random()*99+1) + "</key>" + dbUtils.NEWL;

        // author's information
        //result += prof.asXML() + dbUtils.NEWL;
        if (this.usr_ent_id==0){
            this.usr_ent_id = prof.usr_ent_id;
        }

        result += getModHeader(con, prof)+ dbUtils.NEWL;
        result += dbModuleEvaluation.getModuleEvalAsXML(con, mod_res_id, this.usr_ent_id, tkh_id);
        result += dbResourcePermission.aclAsXML(con,res_id,prof);

        dbRegUser usr = new dbRegUser();
        usr.usr_ent_id = this.usr_ent_id;
        usr.ent_id = this.usr_ent_id;
        usr.get(con);

        result  += usr.getUserShortXML(con, false, true, false);

        result += qList + dbUtils.NEWL;
        if( showAnswer ) {
            dbProgressAttempt dbAtm = new dbProgressAttempt();
            CommonLog.debug("getUserAttemptedResultAsXML:" + tkh_id);
            result += dbAtm.getUserAttemptedResultAsXML(con, mod_res_id, this.usr_ent_id, tkh_id);
        }
        result += "</quiz>"+ dbUtils.NEWL;

        return result;

    }
    
    public String genLearningResAsXML(Connection con, loginProfile prof, float time_limit, Vector resIdVec, long res_id, boolean showAnswer, boolean isShuffleMCQue)
    		throws qdbException, qdbErrMessage, cwSysMessage, SQLException, cwException
    {
    	String qList = "";
    	if (mod_type.equalsIgnoreCase(MOD_TYPE_STX))
    		res_duration = time_limit;
    	
    	// pass the resIdVec as reference and store all the queId in the test
    	// return to qdbAction for access control
    	qList = getStaticQueAsXML(con, resIdVec, isShuffleMCQue, res_id);
    	
    	Timestamp curTime = dbUtils.getTime(con);
    	int scenarioQueCount = dbResource.countScenarioQueInList(con, resIdVec);
    	
    	String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
    	result += "<quiz id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date ;
    	result += "\" start_time=\"" + curTime + "\" size=\"" + (resIdVec.size()-scenarioQueCount) 
    			+ "\" total_score=\"" + (int)mod_max_score + "\">" + dbUtils.NEWL;
    	result += "<key>" +  Math.round(Math.random()*99+1) + "</key>" + dbUtils.NEWL;
    	
    	// author's information
    	//result += prof.asXML() + dbUtils.NEWL;
    	if (this.usr_ent_id==0){
    		this.usr_ent_id = prof.usr_ent_id;
    	}
    	
    	result += getModHeader(con, prof)+ dbUtils.NEWL;
    	result += dbModuleEvaluation.getModuleEvalAsXML(con, mod_res_id, this.usr_ent_id, tkh_id);
    	result += dbResourcePermission.aclAsXML(con,res_id,prof);
    	
    	dbRegUser usr = new dbRegUser();
    	usr.usr_ent_id = this.usr_ent_id;
    	usr.ent_id = this.usr_ent_id;
    	usr.get(con);
    	
    	result  += usr.getUserShortXML(con, false, true, false);
    	
    	result += qList + dbUtils.NEWL;
    	if( showAnswer ) {
    		dbProgressAttempt dbAtm = new dbProgressAttempt();
    		CommonLog.debug("getUserAttemptedResultAsXML:" + tkh_id);
    		result += dbAtm.getUserAttemptedResultAsXML(con, mod_res_id, this.usr_ent_id, tkh_id);
    	}
    	result += "</quiz>"+ dbUtils.NEWL;
    	
    	return result;
    }
    
    public String getStaticQueAsXML(Connection con, Vector resIdVec, boolean isShuffleMCQue, long resId)
            throws qdbErrMessage, qdbException, cwSysMessage, cwException
    {
        String qList = "";
        dbQuestion dbq = new dbQuestion();
        long order = 1;

        dbq.que_res_id = resId;
        dbq.res_id = resId;
        dbq.get(con);
	    if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) ) {
	          FixedScenarioQue fsq = new FixedScenarioQue();
	          fsq.res_id = dbq.res_id;
	          fsq.get(con);
	          try{
	              Vector v_que_id = fsq.getChildQueId(con);
	              if (v_que_id.size() == 0) {
	                  throw new qdbErrMessage("MSP002");
	              }
	              if( fsq.qct_allow_shuffle_ind == 1 ) {
	                  v_que_id = cwUtils.randomVec(v_que_id);
	              }
	              qList += fsq.asXMLinTest(con, order++, v_que_id, isShuffleMCQue);
	              resIdVec.addAll(v_que_id);
	          }catch(SQLException e){
	              throw new qdbException(e.getMessage());
	          }
	      } else if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
	          DynamicScenarioQue dsq = new DynamicScenarioQue();
	          dsq.res_id = dbq.res_id;
	          dsq.get(con);
	          try{
	              Vector v_que_id = dsq.getChildQueId(con);
	              if (v_que_id.size() == 0) {
	                  throw new qdbErrMessage("MSP002");
	              }
	              qList += dsq.asXMLinTest(con, order++, v_que_id, isShuffleMCQue);
	              resIdVec.addAll(v_que_id);
	          }catch(SQLException e){
	              throw new qdbException(e.getMessage());
	          }
	      } else {
	          qList += dbq.asXML(con, order, isShuffleMCQue);
	      }
        
        resIdVec.addElement(new Long(dbq.que_res_id));

        return qList;
    }

    public String getStaticQueAsXML(Connection con, Vector resIdVec, boolean isShuffleMCQue)
        throws qdbErrMessage, qdbException, cwSysMessage, cwException
    {
        // Get the list of question from the test;
        Vector qArray = dbResourceContent.getChildAss(con,mod_res_id);
		if( this.mod_logic != null && this.mod_logic.equalsIgnoreCase(dbModule.MOD_LOGIC_RND) ){
			qArray = cwUtils.randomVec(qArray);
		}
		
        if(qArray.size() == 0) {
            //if no question is drawn, 
            //throw system message and do not launch the test player
        	CommonLog.info("Empty question for Standard Test");
            throw new qdbErrMessage("MOD008");
        }

        String qList = "";
        dbQuestion dbq = new dbQuestion();
        dbResourceContent rcn = new dbResourceContent();
        long order = 1;
        int i;

        mod_size = qArray.size();

        for (i=0;i < qArray.size();i++) {
                rcn = (dbResourceContent) qArray.elementAt(i);
                dbq.que_res_id = rcn.rcn_res_id_content;
                dbq.res_id = dbq.que_res_id;
                dbq.get(con);
                order = rcn.rcn_order;
//          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                  if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) ) {
                      FixedScenarioQue fsq = new FixedScenarioQue();
                      fsq.res_id = dbq.res_id;
                      fsq.get(con);
                      try{
                          Vector v_que_id = fsq.getChildQueId(con);
                          if (v_que_id.size() == 0) {
                              throw new qdbErrMessage("MSP002");
                          }
                          if( fsq.qct_allow_shuffle_ind == 1 ) {
                              v_que_id = cwUtils.randomVec(v_que_id);
                          }
                          qList += fsq.asXMLinTest(con, order++, v_que_id, isShuffleMCQue);
                          resIdVec.addAll(v_que_id);
                      }catch(SQLException e){
                          throw new qdbException(e.getMessage());
                      }
                  } else if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                      DynamicScenarioQue dsq = new DynamicScenarioQue();
                      dsq.res_id = dbq.res_id;
                      dsq.get(con);
                      try{
                          Vector v_que_id = dsq.getChildQueId(con);
                          if (v_que_id.size() == 0) {
                              throw new qdbErrMessage("MSP002");
                          }
                          qList += dsq.asXMLinTest(con, order++, v_que_id, isShuffleMCQue);
                          resIdVec.addAll(v_que_id);
                      }catch(SQLException e){
                          throw new qdbException(e.getMessage());
                      }
                  } else {
                      qList += dbq.asXML(con, order, isShuffleMCQue);
                  }
        
//          ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                resIdVec.addElement(new Long(dbq.que_res_id));
        }

        return qList;

    }

	public String getDynamicQueAsXML(Connection con, String usrId, dbModuleSpec dbmsp, String[] robs, String uploadDir, Vector resIdVec, boolean boolPreviewAssessment, boolean isShuffleMCQue)
        throws qdbErrMessage, SQLException, qdbException, cwSysMessage, cwException
    {
        String qList = "";

        Vector qArray = new Vector();
        dbModule dbmod = new dbModule();
        dbmod.mod_res_id = mod_res_id;
        dbmod.res_id = mod_res_id;
        dbmod.get(con);
		if (dbmod.res_subtype.equalsIgnoreCase(MOD_TYPE_DXT) || dbmod.res_subtype.equalsIgnoreCase(MOD_TYPE_STX)) {
            qArray = dbModuleSpec.genDynQue(con , dbmod, res_usr_id_owner, usrId);
        }
        /*else if (dbmod.res_subtype.equalsIgnoreCase(MOD_TYPE_STX)) {
            qArray = dbModuleSpec.genSTXDynQue(con , dbmod, dbmsp, robs, res_usr_id_owner, usrId);
        }
        */
        if(qArray.size() == 0) {
            //if no question is drawn, 
            //throw system message and do not launch the test player
        	CommonLog.info("Empty question for Dynamic Test");
            throw new qdbErrMessage("MOD008");
        }
        
        dbQuestion dbq = new dbQuestion();
        dbResourceContent rcn = new dbResourceContent();
        long tmpId;

        mod_size = qArray.size();

		for (int nowPos =0; nowPos < mod_size; nowPos++) {
			Vector isNewVec = new Vector();
            tmpId = ((Long) qArray.elementAt(nowPos)).longValue();
			if (boolPreviewAssessment == true) {
				// do not duplicate the question when previewing the assessment only
				dbq.que_res_id = tmpId;
				if( isNewVec != null){
					isNewVec.addElement(new Boolean(false)); 
				}
				dbq.res_id = dbq.que_res_id;
	            dbq.get(con);
			}
			else {
				dbq.que_res_id = dbQuestion.duplicateDynQ(con, tmpId, uploadDir, isNewVec, 0, true);
				dbq.res_id = dbq.que_res_id;
	            dbq.get(con);
				if (dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
					Vector dqc = DbQueContainerSpec.getQueContainerSpecs(con, dbq.que_res_id);
					if (dqc.size() == 0) {
						dqc = DbQueContainerSpec.getQueContainerSpecs(con, tmpId);
						DbQueContainerSpec qcs = (DbQueContainerSpec)dqc.get(0);
	                    qcs.qcs_res_id = dbq.res_id;
	                    qcs.ins(con, dbq.que_type, usrId);
					}				
				} else if (dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)) {				
					 ViewQueContainer vqc = new ViewQueContainer();
                     vqc.res_id = tmpId;
                     boolean exist = vqc.checkExist(con);
                     if (!exist) {
                    	 vqc.res_id = dbq.que_res_id;
                         vqc.ins(con);
                     }       
				}
			}
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) ) {
                FixedScenarioQue fsq = new FixedScenarioQue();
				Vector v_que_id = null; 
                fsq.res_id = dbq.res_id;
                fsq.get(con);
				if( ((Boolean)isNewVec.elementAt(0)).booleanValue() ){
					Vector _v_que_id = new Vector();
					FixedScenarioQue _fsq = new FixedScenarioQue();
					_fsq.res_id = tmpId;
					v_que_id = _fsq.getChildQueId(con);
                    if (v_que_id.size() == 0) {
                        throw new qdbErrMessage("MSP002");
                    }
                    if( fsq.qct_allow_shuffle_ind == 1 ) {
                        v_que_id = cwUtils.randomVec(v_que_id);
                    }
					for(int j=0; j<v_que_id.size(); j++){
						long _que_res_id = 0;
						if (boolPreviewAssessment == true) {
							_que_res_id = ((Long) v_que_id.elementAt(j)).longValue();
						}
						else {
							_que_res_id = dbQuestion.duplicateDynQ(con, ((Long) v_que_id.elementAt(j)).longValue(), uploadDir, null, fsq.res_id, false);
						}						
						_v_que_id.addElement(new Long(_que_res_id));
						dbResourceContent _rcn = new dbResourceContent();
						_rcn.rcn_res_id = fsq.res_id;
						_rcn.rcn_res_id_content = _que_res_id;
						_rcn.rcn_order = (j+1);
						_rcn.rcn_sub_nbr = rcn.rcn_order;
						_rcn.rcn_score_multiplier = 1;
						_rcn.ins(con);
					}
					v_que_id = _v_que_id;
				} else {
					v_que_id = fsq.getChildQueId(con);
                    if (v_que_id.size() == 0) {
                        throw new qdbErrMessage("MSP002");
                    }
                }
				FixedQueContainer fqc = new FixedQueContainer();
				fqc.res_id = tmpId;
				fqc.get(con);
				if( fqc.qct_allow_shuffle_ind == 1 ) {
					v_que_id = cwUtils.randomVec(v_que_id);					
				}
				qList += fsq.asXMLinTest(con, (nowPos+1), v_que_id, isShuffleMCQue);
                resIdVec.addAll(v_que_id);
			} else if( dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                DynamicScenarioQue dsq = new DynamicScenarioQue();
                DynamicScenarioQue _dsq = new DynamicScenarioQue();
                Vector v_que_id = null;
                dsq.res_id = dbq.res_id;
                dsq.get(con);

                //get DSC content que ids
                _dsq.res_id = tmpId;                    
                v_que_id = _dsq.getChildQueId(con);

                if (v_que_id.size() == 0) {
                    throw new qdbErrMessage("MSP002");
                }
                
                Vector _v_que_id = new Vector();
                Hashtable h_contained_que = dbResourceContent.getChildQueRoot(con, dsq.res_id);
                for(int j=0; j<v_que_id.size(); j++){
                    if( !h_contained_que.containsKey((Long)v_que_id.elementAt(j))) {
                        long _que_res_id = 0;
                        if (boolPreviewAssessment == true) {
                            _que_res_id = ((Long) v_que_id.elementAt(j)).longValue();
                        }
                        else {
                            _que_res_id = dbQuestion.duplicateDynQ(con, ((Long) v_que_id.elementAt(j)).longValue(), uploadDir, null, dsq.res_id, false);
                        }
                        _v_que_id.addElement(new Long(_que_res_id));
                        dbResourceContent _rcn = new dbResourceContent();
                        _rcn.rcn_res_id = dsq.res_id;
                        _rcn.rcn_res_id_content = _que_res_id;
                        _rcn.rcn_order = (j+1);
                        _rcn.rcn_sub_nbr = rcn.rcn_order;
                        _rcn.rcn_score_multiplier = 1;
                        _rcn.ins(con);
                        //_v_que_id.addElement(new Long(_que_res_id));
                    } else {
                        _v_que_id.addElement(h_contained_que.get((Long)v_que_id.elementAt(j)));
                    }
                }
                v_que_id = _v_que_id;

                DynamicQueContainer dqc = new DynamicQueContainer();
                dqc.res_id = tmpId;
                dqc.get(con);
                if( dqc.qct_allow_shuffle_ind == 1 ) {
                    v_que_id = cwUtils.randomVec(v_que_id);                 
                }
                qList += dsq.asXMLinTest(con, nowPos+1, v_que_id, isShuffleMCQue);
                resIdVec.addAll(v_que_id);
            } else {
                qList += dbq.asXML(con, nowPos+1, isShuffleMCQue);
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            resIdVec.addElement(new Long(dbq.que_res_id));
        }

        return qList;

    }

/*
    // Get the display option of a module
    public String getDisplayOption(Connection con, String view)
            throws qdbException
    {
        String xml = "";
        dbDisplayOption dpo = new dbDisplayOption();
        dpo.dpo_res_id = mod_res_id;
        dpo.dpo_res_type = res_type;
        dpo.dpo_res_subtype = res_subtype;
        dpo.dpo_view = view;
        //xml = dpo.allViewAsXML(con);
        xml = dpo.getViewAsXML(con);
        return xml;

    }
*/
/*
    public String getDisplayOption(Connection con)
            throws qdbException
    {
        String xml = "";
        dbDisplayOption dpo = new dbDisplayOption();
        dpo.dpo_res_id = mod_res_id;
        dpo.dpo_res_type = res_type;
        dpo.dpo_res_subtype = res_subtype;
        xml = dpo.allViewAsXML(con);
        return xml;

    }
*/


    public String getModHeader(Connection con, loginProfile prof)
            throws qdbException, cwSysMessage
    {
        String result;
        String tmp_end_datetime = null;  //Dennis, store the output end datetime as it may == "UNLIMITED"
        Timestamp cur_time = dbUtils.getTime(con);

        String ATTEMPTED = RES_ATTEMPTED_FALSE;
        // Check if the module was attempted
        long cnt = dbProgress.attemptNum(con , mod_res_id);
        if (cnt>0)
            ATTEMPTED = RES_ATTEMPTED_TRUE;

        //check if the end_datetime need to be converted to "UNLIMITED"
        if(mod_eff_end_datetime != null){
            if(dbUtils.isMaxTimestamp(mod_eff_end_datetime) == true){
                tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
            }
            else{
                tmp_end_datetime = mod_eff_end_datetime.toString();
            }
        }

        //Dennis, display option, get the attributes that needed to be displayed
        Timestamp PSTART = null;
        Timestamp PCOMPLETE = null;
        Timestamp PLASTACC = null;
        long PATTEMPTNBR = 0;

        dbProgress pgr = new dbProgress();
        pgr.pgr_usr_id = prof.usr_id;
        pgr.pgr_res_id = mod_res_id;

        if (tkh_id==DbTrackingHistory.TKH_ID_UNDEFINED){
        		CommonLog.info("!!!!!!get tracking id in dbModule.getModHeader, with role:" + prof.current_role);
            try{
                tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, res_id, prof.usr_ent_id);
                CommonLog.debug("pgr.pgr_tkh_id: " + tkh_id);
            }catch (SQLException e){
                throw new qdbException ("SQL Error: " + e.getMessage());
            }catch (cwException e){
                throw new qdbException ("CW Error: " + e.getMessage());
            }
       }        
       
       pgr.pgr_tkh_id = tkh_id;
       PATTEMPTNBR = pgr.usrAttemptNum(con,mod_res_id,prof.usr_id, pgr.pgr_tkh_id);
        if(PATTEMPTNBR > 0) {
            try {
                pgr.get(con,1);
                PSTART = pgr.pgr_start_datetime;

                pgr.get(con);
                PCOMPLETE = pgr.pgr_complete_datetime;
                PLASTACC = pgr.pgr_last_acc_datetime;
            }
            catch(qdbErrMessage e) {
                throw new qdbException (e.getMessage());
            }
        }

        //Dennis, get the Event body
        dbEvent dbevt;
        String evtBody = "";
        if(dbEvent.isEventType(res_subtype)) {
            dbevt = new dbEvent();
            dbevt.evt_res_id = mod_res_id;
            evtBody = dbevt.getEventAsXML(con);
        }

        // Course id
        long cosId = dbModule.getCosId(con, mod_res_id);
        String cosTitle = null;
        String itmContentDef = null;
        long tcr_id = 0;
        try {
            itmContentDef = getItmContentDef(con,cosId);
            cosTitle = dbCourse.getCosTitle(con, cosId);
            tcr_id = dbCourse.getCosItmTcrId(con, cosId);
        } catch(SQLException sqle) {
            throw new qdbException(sqle.getMessage());
        }
        result = "<header course_id=\"" + cosId + "\" course_title=\"" + dbUtils.esc4XML(cosTitle) + "\" difficulty=\"" + res_difficulty ;
        result +=       "\" mod_id=\"" + mod_res_id;
        result +=		"\" test_style=\"" + mod_test_style;
        result +=       "\" duration=\"" + res_duration ;
        result +=       "\" time_limit=\"" + res_duration ;
        result +=       "\" time_left=\"" + res_duration * 60;
        result +=       "\" suggested_time=\"" + res_duration ;
        result +=       "\" max_score=\"" + mod_max_score ;
        result +=       "\" pass_score=\"" + (int)mod_pass_score + "\" privilege=\"" ;
                                                             // should be change to res_type
        result +=       res_privilege + "\" status=\"" + res_status + "\" type=\"" + res_type ;
        result +=       "\" subtype=\"" + res_subtype ;
        result +=       "\" max_attempt=\"" + mod_max_attempt + "\" max_usr_attempt=\"" + mod_max_usr_attempt ;
        result +=       "\" score_ind=\"" + mod_score_ind;
        result +=       "\" score_reset=\"" + mod_score_reset;
        result +=       "\" logic=\"" + mod_logic ;
        if( res_subtype!=null && ( res_subtype.equals("CHT") || res_subtype.equals("VCR") ) ){
            result +=   "\" ts_host=\"" + mod_tshost;
            result +=   "\" ts_port=\"" + mod_tsport;
            result +=   "\" www_port=\"" + mod_wwwport;
        }
        if(res_subtype != null && (res_subtype.equals("VOD") || res_subtype.equals("RDG") || res_subtype.equals("REF"))){
        	result +=   "\" mod_required_time=\"" + mod_required_time;
        	result +=   "\" mod_download_ind=\"" + mod_download_ind;
        	result +=   "\" itmContentDef=\"" + itmContentDef;
        }
        result +=   "\" mod_mobile_ind=\"" + mod_mobile_ind;
        result +=   "\" mod_test_style=\"" + mod_test_style;
        result +=       "\" eff_start_datetime=\"" + mod_eff_start_datetime;
        result +=       "\" eff_end_datetime=\"" + tmp_end_datetime;
        result +=       "\" cur_time=\"" + cur_time;
        result +=       "\" attempted=\"" + ATTEMPTED;
        result +=       "\" attempt_nbr=\"" + PATTEMPTNBR;
        result +=       "\" pgr_start=\"" + PSTART;
        result +=       "\" pgr_complete=\"" + PCOMPLETE;
        result +=       "\" has_rate_q=\"" + mod_has_rate_q;
        if(res_subtype.equalsIgnoreCase(MOD_TYPE_TST)) {
			if(numEssay(con) > 0) {
				result +=       "\" has_essay=\"true";
			} else {
				result +=       "\" has_essay=\"false";
			}
        } else if(res_subtype.equalsIgnoreCase(MOD_TYPE_DXT)) {
        	boolean hasEssay = false;
        	Vector dbMspVec = new Vector();
			try {
				dbMspVec = dbModuleSpec.getModuleSpecs(con, mod_res_id);
				if(dbMspVec.size() == 0){
					if(mod_mod_res_id_parent != 0){
						dbMspVec = dbModuleSpec.getModuleSpecs(con, mod_mod_res_id_parent);
					}
				}
			} catch (SQLException e) {
				throw new qdbException(e.getMessage());
			}
        	for(int k=0; k<dbMspVec.size(); k++) {
        		dbModuleSpec dbMsp = (dbModuleSpec) dbMspVec.elementAt(k);
        		if(dbMsp.msp_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) || dbMsp.msp_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
        			hasEssay = true;
        			break;
        		}
        	}
			result +=       "\" has_essay=\"" + hasEssay;
        }
        result +=       "\" is_public=\"" + mod_is_public;
        if(mod_show_answer_ind != -1) {
            result += "\" show_answer_ind=\"" + mod_show_answer_ind;
        }
        if(mod_show_answer_after_passed_ind != -1) {
            result += "\" mod_show_answer_after_passed_ind=\"" + mod_show_answer_after_passed_ind;
        }
        if(mod_sub_after_passed_ind != -1) {
            result += "\" sub_after_passed_ind=\"" + mod_sub_after_passed_ind;
        }     
        if(mod_show_save_and_suspend_ind != -1) {
            result += "\" show_save_and_suspend_ind=\"" + mod_show_save_and_suspend_ind;
        }
        if (mod_managed_ind != -1) {
            result += "\" managed_ind=\"" + mod_managed_ind;
        }
        if (mod_started_ind != -1) {
            result += "\" started_ind=\"" + mod_started_ind;
        }
        /**
         * check:
         * 1. if current module is created from a public module;
         * 2. if current module is used by other module
         */
        try {
        	result +=       "\" public_used=\"" + this.isPublicUsed(con);
        } catch (SQLException sqlEx) {
        	throw new qdbException(sqlEx.getMessage());
        }

        // for AICC only
        this.getAicc(con);
        if (mod_vendor != null) {
            result += "\" mod_vendor=\"" + dbUtils.esc4XML(mod_vendor);
        }
        else {
            result += "\" mod_vendor=\"" + "";
        }

        if (mod_web_launch != null) {
            result += "\" mod_web_launch=\"" + dbUtils.esc4XML(mod_web_launch);
        }
        else {
            result += "\" mod_web_launch=\"" + "";
        }

        if (res_src_type != null) {
            result += "\" res_src_type=\"" + res_src_type;
        }
        else {
            result += "\" res_src_type=\"" + "";
        }

        if (res_src_link != null) {
            result += "\" res_src_link=\"" + dbUtils.esc4XML(res_src_link);
        }
        else {
            result += "\" res_src_link=\"" + "";
        }
        
        if (res_sco_version != null) {
            result += "\" res_sco_version=\"" + res_sco_version;
        }
        else {
            result += "\" res_sco_version=\"" + "";
        }
        //

        result +=       "\" pgr_last_acc=\"" + PLASTACC + "\">";
        //Dennis, display option here
        result += dbUtils.NEWL;
        if(res_src_type != null && res_src_type.indexOf("ONLINEVIDEO_") != -1 && res_src_online_link != null) {
        	result += "<src link=\"" + res_src_online_link.replaceAll("&", "&amp;") + "\" />";
        	result += dbUtils.NEWL;
        }
        result += "<source type=\"" + res_src_type + "\">"+ dbUtils.esc4XML(res_src_link) + "</source>" + dbUtils.NEWL;
        //result += "<url>" + dbUtils.esc4XML(res_url) + "</url>" + dbUtils.NEWL;
        //result += "<filename>" + dbUtils.esc4XML(res_filename) + "</filename>" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(res_title) + "</title>" + dbUtils.NEWL;
        result += "<desc>" + dbUtils.esc4XML(res_desc) + "</desc>" + dbUtils.NEWL;
        result += "<vod_duration>" + res_vod_duration + "</vod_duration>" + dbUtils.NEWL;
        result += "<img_link>" + dbUtils.esc4XML(res_img_link) + "</img_link>" + dbUtils.NEWL;
        result += "<annotation>" + res_annotation + "</annotation>" + dbUtils.NEWL;
        result += "<instruction>" + dbUtils.esc4XML(mod_instruct) + "</instruction>" + dbUtils.NEWL;
        result += "<instructor>" + dbUtils.esc4XML(res_instructor_name) + "</instructor>" + dbUtils.NEWL;

        result += dbCourse.getInstructorList(con, cosId, mod_usr_id_instructor, prof.root_ent_id);

        //result += "<moderator>" + dbUtils.esc4XML(getOwnerName(con)) + "</moderator>" + dbUtils.NEWL;
        result += "<organization>" + dbUtils.esc4XML(res_instructor_organization) + "</organization>" + dbUtils.NEWL;

        if(dbEvent.isEventType(res_subtype))
            result += evtBody;

        result += "<template_list cur_tpl=\"" + res_tpl_name + "\">" + dbUtils.NEWL;

        String tpl_type = "";
            tpl_type = mod_type;

        result += dbTemplate.tplListContentXML(con, prof, tpl_type);
        result += "</template_list>" + dbUtils.NEWL;
        result += "<training_center id=\"" + tcr_id +"\"/>" + dbUtils.NEWL;
        try {
			String tcr_title = DbTrainingCenter.getTcrTitle(con, mod_tcr_id);
	        result += "<mod_tcr id=\"" + mod_tcr_id + "\" title=\"" + tcr_title + "\" />";
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		}
        result += "</header>" + dbUtils.NEWL;

        return result;
    }

     public String getItmContentDef(Connection con,long cosId){
        String itmContentDef = null;
        String sql = "SELECT itm_content_def FROM aeItem WHERE itm_id = (SELECT cos_itm_id FROM Course WHERE cos_res_id = ?)";
         PreparedStatement stmt = null;
         try {
             stmt = con.prepareStatement(sql);
             stmt.setLong(1, cosId);
             ResultSet rs = stmt.executeQuery();
             while(rs.next()){
                 itmContentDef = rs.getString("itm_content_def");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }finally {
             if (stmt != null) {
                 try {
                     stmt.close();
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         }
         return itmContentDef;
     }

    public void checkStat(Connection con)
        throws qdbException, qdbErrMessage
    {
        try {
            if(dbModuleEvaluation.getModuleAttemptCount(con,mod_res_id) > 0
                || getClassModuleAttemptCount(con, mod_res_id) > 0 ) {
                throw new qdbErrMessage("MOD004");
            }
        } catch (SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    public int getClassModuleAttemptCount(Connection con, long mod_res_id) throws SQLException {
        String sql = "SELECT mod_res_id from Module where mod_mod_res_id_parent = ? ";
        PreparedStatement stmt = null;
        int mod_class_att_count = 0;
        try {
            int idx = 1;
            stmt = con.prepareStatement(sql);
            stmt.setLong(idx++, mod_res_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mod_class_att_count += dbModuleEvaluation.getModuleAttemptCount(con, rs.getLong("mod_res_id"));
            }
        }
        finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return mod_class_att_count;
    }
    
    public void updMaxScore(Connection con, long que_res_id)
        throws qdbException, cwSysMessage
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT rcn_res_id FROM ResourceContent "
                + " where rcn_res_id_content = ?  ");

            stmt.setLong(1, que_res_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                mod_res_id = rs.getLong("rcn_res_id");
                updMaxScore(con);
            }else {
            	stmt.close();
                con.rollback();
                throw new qdbException("Failed to get the module if.");
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }

    }

    public void updMaxScore(Connection con)
        throws qdbException, cwSysMessage
    {
        try {
            get(con);
            String SQL = "";

            if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT) ||
                mod_type.equalsIgnoreCase(MOD_TYPE_STX))
                SQL = " SELECT msp_score, msp_qcount from ModuleSpec where msp_res_id = ? " ;
            else
                SQL = " SELECT que_score,rcn_score_multiplier FROM ResourceContent,Question "
                + " where rcn_res_id = ? and que_res_id=rcn_res_id_content ";


            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = null;
            if(mod_type.equalsIgnoreCase(MOD_TYPE_DXT) && (mod_mod_res_id_parent > 0)){
            	stmt.setLong(1, mod_mod_res_id_parent);
            } else {
	            stmt.setLong(1, mod_res_id);
            }
            rs = stmt.executeQuery();
            long maxScore = 0 ;
            while (rs.next()) {
				if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT) || mod_type.equalsIgnoreCase(MOD_TYPE_STX)) 
                    maxScore += rs.getLong("msp_score") * rs.getLong("msp_qcount");
//                else if (mod_type.equalsIgnoreCase(MOD_TYPE_STX))
                    // NO MAX SCORE for STX type of module
//                    maxScore = 0;
                else
                    maxScore += rs.getLong("rcn_score_multiplier") * rs.getInt("que_score");
            }
            stmt.close();

            PreparedStatement stmt1 = con.prepareStatement(
                "UPDATE Module SET "
                + " mod_max_score = ? "
                + " where mod_res_id = ?");

            stmt1.setLong(1, maxScore);
            stmt1.setLong(2, mod_res_id);
            int stmtResult=stmt1.executeUpdate();
            if ( stmtResult!=1)
            {
            	stmt1.close();
                con.rollback();
                throw new qdbException("Failed to modify the max score.");
            }
			mod_max_score = maxScore;
            stmt1.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public void updMaxScoreAndPassScore(Connection con) throws qdbException {
    	try {
    		PreparedStatement stmt1 = con.prepareStatement(
                "UPDATE Module SET "
                + " mod_max_score = ? "
                + " ,mod_pass_score = ? "
                + " where mod_res_id = ?");
    		
    		int index = 1;
            stmt1.setFloat(index++, mod_max_score);
            stmt1.setFloat(index++, mod_pass_score);
            stmt1.setLong(index++, mod_res_id);
            int stmtResult=stmt1.executeUpdate();
            if ( stmtResult!=1)
            {
            	stmt1.close();
                con.rollback();
                throw new qdbException("Failed to modify the max score and pass score.");
            }
            stmt1.close();
    	  } catch(SQLException e) {
              throw new qdbException("SQL Error: " + e.getMessage());
          }
	}

    public void genQ(Connection con, dbModuleSpec dbmsp, String usrId, String uploadDir, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try {
            get(con);

            res_upd_user = prof.usr_id ;

            super.checkTimeStamp(con);

            // check User Right
//            checkModifyPermission(con, prof);

            //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}

            // check whether the course was attempted.
            checkStat(con);

            String aobjs = ""; // (1, 2, 34)
            // CL : 2001-01-04 , add the immediate objective to standard test only
            // aobjs = dbObjective.getSelfAndChildsObjIdLst(con,dbmsp.msp_obj_id);
            aobjs = "( " + dbmsp.msp_obj_id + " ) ";

            if (aobjs == "") {
                    throw new qdbException( "No resource objective selected. ");
            }
                String SQL =
                        " INSERT INTO ResourceObjective "
                    +   " SELECT " + res_id + ", obj_id "
                    +   "  FROM Objective "
                    +   " WHERE obj_id IN " + aobjs
                    +   "   AND obj_id NOT IN (SELECT rob_obj_id from ResourceObjective where "
                    +   "                             rob_res_id = " + res_id + ") ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();

            Vector qArray = new Vector();
            //CL : 2001-01-04
            //qArray = dbmsp.genQue(con, usrId, qArray);
            qArray = dbmsp.genQue(con, usrId, qArray, aobjs);

            int i;
            long newId = 0 ;
            dbResourceContent resCn = new dbResourceContent();
            dbResourceObjective resObj = new dbResourceObjective();
            for(i =0;i< qArray.size();i++) {
                resCn.rcn_res_id = mod_res_id;
                long tmpId = ((Long) qArray.elementAt(i)).longValue();

                resObj = (dbResourceObjective ) dbResourceObjective.getObjId(con,tmpId).elementAt(0);
                long objId = resObj.rob_obj_id;
                //  duplicate the question
                newId = dbQuestion.insTestQ(con, mod_res_id, tmpId, objId, res_upd_user);

                if (newId > 0) {
                    resCn.rcn_res_id_content = newId;
                    resCn.rcn_obj_id_content = objId;
                    resCn.ins(con);

                    dbUtils.copyMediaFrom(uploadDir, tmpId, newId);
                }
            }
            // Calculate the max score after adding the question to the test
            updMaxScore(con);
            super.updateTimeStamp(con);
            con.commit();

            stmt.close();

        } catch(SQLException e) {
                throw new qdbException("SQL Error: " + e.getMessage());
        }


    }

    public void updateStatus(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage, SQLException
    {

        super.checkTimeStamp(con);

        // check User Right
//        checkModifyPermission(con, prof);
        //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}

        // get the test informatin
        String newStatus = res_status;
        get(con);
        res_status = newStatus;

        //Dennis, impl release date
        if (res_status.equalsIgnoreCase(RES_STATUS_ON)
            || res_status.equalsIgnoreCase(RES_STATUS_DATE))
        {
            // Check if the question is ordered in 1,2,3....
            if (!checkQorder(con)) {
                throw new qdbErrMessage("MOD001");
            }

        }
        
//		if the module is standard test or dynamic test
			  //make sure that you can only turn the module online
			  //if the test has question/criteria defined in it
		if(res_status.equalsIgnoreCase(RES_STATUS_ON)) {
			if(mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {
				if(dbResourceContent.getResourceContentCount(con, mod_res_id) == 0) {
					res_status = RES_STATUS_OFF;
				}
			} else if(mod_type.equalsIgnoreCase(MOD_TYPE_DXT)) {
				if(dbModuleSpec.getModuleSpecCount(con, mod_res_id) == 0) {
					res_status = RES_STATUS_OFF;
				}
			}
		}
			  
        super.updateStatus(con);


    }

    //Dennis, 2000-12-13, impl release control
    //it updates the mod_eff_start/end_datetime in Module
    //with the non-static class valiables mod_in_eff_start/end_datetime
    public void updateEffDatetime(Connection con)
        throws qdbException
    {
        try {
            String SQL;

            if(mod_in_eff_start_datetime != null)
                if(dbUtils.isMinTimestamp(mod_in_eff_start_datetime))
                    mod_in_eff_start_datetime = dbUtils.getTime(con);

            SQL = "update Module set mod_eff_start_datetime = ?, "
                + "mod_eff_end_datetime = ? "
                + "where mod_res_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setTimestamp(1, mod_in_eff_start_datetime);
            stmt.setTimestamp(2, mod_in_eff_end_datetime);
            stmt.setLong(3,mod_res_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {
                con.rollback();
                throw new qdbException("Failed to update status.");
            }
        }
        catch(SQLException e) {
            throw new qdbException(e.getMessage());
        }
    }

    //Dennis, 2000-12-13, impl release control
    //it gets the mod_eff_start_datetime from Module
    public static Timestamp getModStart(Connection con, long id)
        throws qdbException {
            try {
                String SQL;
                Timestamp curTime = null;

                SQL = "Select mod_eff_start_datetime "
                    + "From Module "
                    + "Where mod_res_id = ?";

                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next() == true)
                    curTime =  rs.getTimestamp(1);
                else
                {
                	stmt.close();
                    throw new qdbException("Cannot find the Module: " + id);
                }

                stmt.close();
                return curTime;
            }
            catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
    }

    //Dennis, 2000-12-13, impl release control
    //it gets the mod_eff_end_datetime from Module
    public static Timestamp getModEnd(Connection con, long id)
        throws qdbException {
            try {
                String SQL;
                Timestamp curTime = null;

                SQL = "Select mod_eff_end_datetime "
                    + "From Module "
                    + "Where mod_res_id = ?";

                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next() == true)
                    curTime = rs.getTimestamp(1);
                else
                {
                	stmt.close();
                    throw new qdbException("Cannot find the Module: " + id);
                }

                stmt.close();
                return curTime;
            }
            catch (SQLException e) {
                throw new qdbException(e.getMessage());
            }
    }


    public static long getCosId(Connection con, long modId)
        throws qdbException
    {
        try {
            long cosId = 0;
            String SQL =  " SELECT rcn_res_id FROM ResourceContent "
                      +   " WHERE rcn_res_id_content =  ?" ;
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,modId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cosId = rs.getLong("rcn_res_id");
            }

            stmt.close();
            return cosId;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

 public static Vector getModPermissionLst(Connection con, Vector modId, loginProfile prof)
    throws qdbException, qdbErrMessage
 {
        Vector CombineLst = new Vector();
        Vector CosIdLst = new Vector();
        Vector PermissionLst = new Vector();
        Vector CosNameLst = new Vector();
        String gpIds = prof.usrGroupsList();
        String StrModLst = "(0";
        for ( int i = 0; i < modId.size(); i++ )
            StrModLst += ", " + ((Long) modId.elementAt(i)).toString();
        StrModLst += ")";

        String SQL = "";

        try {
            SQL = " SELECT DISTINCT mod_res_id MODID , rcn_res_id COSID , res_title COSTITLE"
                + " FROM Module, ResourceContent, ResourcePermission , Resources"
                + " WHERE mod_res_id = rcn_res_id_content AND rcn_res_id = rpm_res_id "
                + " AND rpm_ent_id IN " + gpIds + " AND "
                + " (rpm_read = 1 OR rpm_write = 1 OR rpm_execute = 1) AND "
                + " res_id = rcn_res_id AND mod_res_id IN " + StrModLst ;

            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Long MODID = new Long(rs.getLong("MODID"));
                Long COSID = new Long(rs.getLong("COSID"));
                String COSTITLE = new String(rs.getString("COSTITLE"));
                //PermissionLst+= ((Long)RESID).toString() + ",";
                PermissionLst.addElement(MODID);
                CosIdLst.addElement(COSID);
                CosNameLst.addElement(COSTITLE);
            }
            stmt.close();
            CombineLst.addElement(PermissionLst);
            CombineLst.addElement(CosIdLst);
            CombineLst.addElement(CosNameLst);
            return CombineLst;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
   }
   // Check wether a user has right to modify a course according to ACL
   // Case 1 : User has write permission in the resource permission
   // Case 2 : User has COS_MGT_IN_ORG right in ACL
   /*
   public void checkModifyPermission(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
   {
        try {
            AcModule acMod = new AcModule(con);
                if (!acMod.checkModifyPermission(prof, mod_res_id)){
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            }
        }catch (SQLException e) {
            throw new qdbException("SQL Error : " + e.getMessage());
        }catch (cwSysMessage e) {
            throw new qdbErrMessage(e.getId());
        }
   }*/

 /*
   // Check wether a user has right to modify a module
   // Case 1 : User has write permission in the resource permission
   // Case 2 : User are Expert Course Designers and have write permission on the course
   public void checkModifyPermission(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
   {
        try {

            boolean hasRight = false;
            // User has write permission on the module
            if (dbResourcePermission.hasPermission(con, mod_res_id, prof,
                                            dbResourcePermission.RIGHT_WRITE)) {

                hasRight = true;
            }else {

                long cosId = dbModule.getCosId(con, mod_res_id);
                // User has write permission on the same course
                if (dbResourcePermission.hasPermission(con, cosId, prof,
                                                dbResourcePermission.RIGHT_WRITE)) {

                    AccessControlWZB acl = new AccessControlWZB();
                    String[] roles = acl.getUserRoles(con, prof.usr_ent_id);
                    // User is an expert course designer of the course

                    if (dbUtils.strArrayContains(roles, dbRegUser.ROLE_USR_ECDN))
                        hasRight = true;
                }
            }

            if (!hasRight){
                throw new qdbErrMessage("MOD005");
            }

        }catch (SQLException e) {
            throw new qdbException("SQL Error : " + e.getMessage());
        }
   }
*/
   // Check wether a user has right to read a module
   // Case 1 : User has read permission in the resource permission
   // Case 2 : User are Expert Course Designers and have read permission on the course
    /*
   public void checkReadPermission(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
   {
        //try {
            boolean hasRight = false;
            // User has write permission on the module
            if (dbResourcePermission.hasPermission(con, mod_res_id, prof,
                                            dbResourcePermission.RIGHT_READ)) {
                hasRight = true;
            }else {
                long cosId = dbModule.getCosId(con, mod_res_id);
                // User has read permission on the course
                if (dbResourcePermission.hasPermission(con, cosId, prof,
                                                dbResourcePermission.RIGHT_READ)) {
                    hasRight = true;
                }
            }

            if (!hasRight){

                throw new qdbErrMessage("RPM001");
            }

        //}catch (SQLException e) {
        //    throw new qdbException("SQL Error : " + e.getMessage());
        //}
   }
   */

   // Check wether a user has right to modify a course according to ACL
   // Case 1 : has read permission in Course
   public void checkReadPermission(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
   {
//        boolean hasRight;
//        long cosId = dbModule.getCosId(con, mod_res_id);
//        dbCourse cos = new dbCourse();
//        cos.cos_res_id = cosId;
//        try {
//            cos.checkReadPermission(con, prof);
//            hasRight = true;
//        }
//        catch(qdbErrMessage ee) {
//            hasRight = false;
//        }
//
//        if (!hasRight){
//            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
//        }
   }

   
   public static String getInstructorName(Connection con, long modId)
        throws qdbException, SQLException
   {
            StringBuffer xml = new StringBuffer();
			String SQL = OuterJoinSqlStatements.dbModuleGetInstructorName();
//            String SQL = " SELECT res_instructor_name, usr_display_bil, mod_usr_id_instructor "
//                + "    FROM Resources, Module, RegUser "
//                + " WHERE mod_res_id = res_id "
//                + "   AND mod_res_id = ? "
//                + "   AND usr_id " + cwSQL.get_right_join(con) + " mod_usr_id_instructor ";


            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,modId);

            ResultSet rs = stmt.executeQuery();
            xml.append("<instructor id=\"");
            if(rs.next()) {
                xml.append(rs.getString("mod_usr_id_instructor")).append("\">");

                String name = rs.getString("usr_display_bil");
                // if a instructor is assign to a module
                if ( name != null && name.length() > 0)
                    xml.append(name);
                else
                    xml.append(rs.getString("res_instructor_name"));
            }else {
            	stmt.close();
                throw new qdbException("Failed to get instructor information.");
            }

            xml.append("</instructor>").append(dbUtils.NEWL);
            stmt.close();

            return xml.toString();
   }

   public static String getInstructorId(Connection con, long modId)
        throws qdbException, SQLException
   {
            String instructorId ;

            String SQL = " SELECT mod_usr_id_instructor IST_ID FROM Module "
                + " WHERE mod_res_id = ? " ;

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,modId);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                instructorId = rs.getString("IST_ID");
            }else {
            	stmt.close();
                throw new qdbException("Failed to get instructor information.");
            }
            stmt.close();

            return instructorId;
   }
   /*
   public String getSurveyReport(Connection con, loginProfile prof)
            throws qdbException, cwSysMessage
    {
        if ((!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) &&
            (!prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER))) {

            throw new qdbException("Permission denied.");
        }

        StringBuffer xmlBuf = new StringBuffer();

        xmlBuf.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL);
        xmlBuf.append("<survey_report>").append(dbUtils.NEWL);
        xmlBuf.append(prof.asXML()).append(dbUtils.NEWL);
        xmlBuf.append("<test id=\"").append(mod_res_id).append("\">");
        xmlBuf.append(dbUtils.NEWL);

        get(con);
        // get the test header
        xmlBuf.append(getModHeader(con,prof));

        Vector resIdVec = new Vector();
        xmlBuf.append(getSuqAsXML(con, resIdVec));

        Vector memberlist = dbUserGroup.getMemberListFromCos(con, getCosId(con, mod_res_id));

        Vector usrVec = new Vector();
        dbRegUser user = null;
        for (int i=0; i<memberlist.size(); i++){
            user = new dbRegUser();
            user.usr_ent_id = ((Long)memberlist.elementAt(i)).longValue();
            usrVec.addElement(user.getUserId(con));
        }

        Vector qAttempt = new Vector();
        dbProgressAttempt atm = new dbProgressAttempt();

        qAttempt = atm.getTstAss(con, usrVec, mod_res_id, 1);
        Hashtable htUsrId = dbRegUser.getUsrAndItsGroup(con, usrVec);
        dbRegUser usr = null;
        xmlBuf.append("<result>").append(dbUtils.NEWL);
        dbProgressAttempt atm2 = new dbProgressAttempt();
        for (int j=0; j<qAttempt.size();j++) {
            atm2 = (dbProgressAttempt) qAttempt.elementAt(j);
            usr = (dbRegUser) htUsrId.get(atm2.atm_pgr_usr_id);
            xmlBuf.append("<attempt que_id=\"").append(atm2.atm_int_res_id).append("\" usr_id=\"").append(usr.usr_id).append("\" >");

            xmlBuf.append("<entity ent_id=\"").append(usr.usr_ent_id).append("\" ");
            xmlBuf.append("first_name=\"").append(cwUtils.esc4XML(usr.usr_first_name_bil)).append("\" ");
            xmlBuf.append("last_name=\"").append(cwUtils.esc4XML(usr.usr_last_name_bil)).append("\">");
            xmlBuf.append(cwUtils.esc4XML(usr.usr_display_bil)).append("</entity>").append(dbUtils.NEWL);

            for (int i=0; i<usr.usg_display_bil.size(); i++){
                xmlBuf.append("<group name=\"").append(cwUtils.esc4XML((String)usr.usg_display_bil.elementAt(i))).append("\" />").append(dbUtils.NEWL);
            }
            xmlBuf.append("<interaction order=\"").append(atm2.atm_int_order);
            xmlBuf.append("\" correct=\"").append(atm2.atm_correct_ind);
            xmlBuf.append("\" flag=\"").append(atm2.atm_flag_ind);
            xmlBuf.append("\" usr_score=\"").append(atm2.atm_score);
            xmlBuf.append("\">").append(dbUtils.NEWL);

            // handle Matching response
            String[] resps_ = dbUtils.split(atm2.atm_response_bil, dbProgressAttempt.RESPONSE_DELIMITER);
            if (resps_ != null && resps_.length  > 0 && resps_[0] != null) {
                for (int k=0; k < resps_.length; k++)
                    xmlBuf.append("<response>").append(dbUtils.esc4XML(resps_[k])).append("</response>").append(dbUtils.NEWL);
            }
            xmlBuf.append("</interaction>").append(dbUtils.NEWL);
            xmlBuf.append("</attempt>").append(dbUtils.NEWL);
        }

        xmlBuf.append("</result>").append(dbUtils.NEWL);
        xmlBuf.append("</test>").append(dbUtils.NEWL);
        xmlBuf.append("</survey_report>").append(dbUtils.NEWL);
        return xmlBuf.toString();

    }
    */

   // Check if the module is  attempted, the res_src_type and src_link cannot be modified
   public boolean updSourceInfo(Connection con) throws SQLException, qdbException {

        boolean useNewFile = true;
        long cnt  = dbModuleEvaluation.attemptNum(con, mod_res_id);
        if (cnt > 0) {
            useNewFile = false;
            String SQL = " SELECT res_src_type, res_src_link, res_tpl_name FROM Resources "
                + " WHERE res_id = ? " ;

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1,mod_res_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                res_src_type = rs.getString("res_src_type");
                res_src_link = rs.getString("res_src_link");
                res_tpl_name = rs.getString("res_tpl_name");
            }else {
            	stmt.close();
                throw new qdbException("Failed to get the module.");
            }
            stmt.close();
        }

        return useNewFile;
   }
 
    public static Vector getPublicModLst(Connection con, long root_ent_id, String mod_type, boolean checkStatus,boolean isStudyGroupMod, long sgp_id, Vector modTcrIds) throws SQLException{
        Vector vtMod = new Vector();
        Timestamp curTime = cwSQL.getTime(con);
        String sql = "SELECT mod_res_id, res_title, mod_eff_start_datetime , mod_eff_end_datetime , res_status , mod_usr_id_instructor, res_upd_date, mod_tcr_id " +
        		"FROM module" +
        		" inner join resources on( mod_res_id = res_id )" +
        		" inner join  reguser  on(res_usr_id_owner = usr_id)" ;
        if(isStudyGroupMod){ 
        	sql=sql+" inner join studygrouprelation on (sgr_ent_id=mod_res_id)";
        }
        sql=sql+" WHERE  mod_type = ? AND usr_ste_ent_id = ? AND mod_is_public = ? ";
        if (checkStatus){
            sql += " AND ? between mod_eff_start_datetime AND mod_eff_end_datetime AND res_status = ?";
        }
        if(isStudyGroupMod){
        	sql=sql+" and sgr_sgp_id=?" +
        			" and sgr_type='SGP_DISCUSS'";
        }
        sql += "  and mod_sgp_ind=?";
    	if(modTcrIds != null && modTcrIds.size() > 0){
    		String tcrIds = cwUtils.vector2list(modTcrIds);
    		sql += " AND mod_tcr_id IN " + tcrIds;
    	}
        sql += " ORDER BY mod_res_id DESC ";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index=1;
        stmt.setString(index++, mod_type);
        stmt.setLong(index++, root_ent_id);
        stmt.setBoolean(index++, true);

        if (checkStatus){
            stmt.setTimestamp(index++, curTime);
            stmt.setString(index++, "ON");
        }
        if(isStudyGroupMod){
        	stmt.setLong(index++, sgp_id);
        }
        stmt.setBoolean(index++, isStudyGroupMod);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            dbModule dbmod = new dbModule();
            dbmod.mod_res_id = rs.getLong("mod_res_id");
            dbmod.res_id = dbmod.mod_res_id;
            dbmod.mod_type = mod_type;
            dbmod.res_subtype = mod_type;
            dbmod.res_type = dbResource.RES_TYPE_MOD;
            dbmod.res_title = rs.getString("res_title");
            dbmod.res_upd_date = rs.getTimestamp("res_upd_date");
            dbmod.mod_eff_start_datetime = rs.getTimestamp("mod_eff_start_datetime");
            dbmod.mod_eff_end_datetime = rs.getTimestamp("mod_eff_end_datetime");
            dbmod.res_status = rs.getString("res_status");
            vtMod.addElement(dbmod);
        }
        stmt.close();
        return vtMod;
    }
    
    public Vector getAllEvalModuleOfLearner(Connection con, long rootEntId, boolean isCheckStatus, long curUserEntId, cwPagination page, WizbiniLoader wizbini) throws SQLException {
    	Vector evalModuleVec = null;
    	
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	Timestamp curTime = cwSQL.getTime(con);
    	try{
    		stmt = con.prepareStatement(SqlStatements.getAllEvalModuleOfLearnerByTcrId(isCheckStatus,  wizbini));
    		int index = 1;
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		stmt.setString(index++, mod_type);
            stmt.setLong(index++, rootEntId);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, curUserEntId);
        	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            if(isCheckStatus) {
            	stmt.setTimestamp(index++, curTime);
                stmt.setString(index++, "ON");
                stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                stmt.setLong(index++,curUserEntId);
            }
            rs = stmt.executeQuery();
            //get module list from ResultSet
            evalModuleVec = getEvalModuleListByResultSet(rs, page);
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    	
    	return evalModuleVec;
    }
    
    public Vector getAllEvalModuleOfTrainer(Connection con, long rootEntId, boolean isCheckStatus, long curUserEntId, cwPagination page, String title_code) throws SQLException {
		Vector evalModuleVec = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Timestamp curTime = cwSQL.getTime(con);
		try {
			stmt = con.prepareStatement(SqlStatements.getAllEvalModuleOfTrainer(isCheckStatus));
			int index = 1;
			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
			stmt.setString(index++, mod_type);
			stmt.setLong(index++, rootEntId);
			stmt.setBoolean(index++, true);
			stmt.setLong(index++, curUserEntId);
			if (isCheckStatus) {
				stmt.setTimestamp(index++, curTime);
				stmt.setString(index++, "ON");
				stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
				stmt.setLong(index++, curUserEntId);
			}
			stmt.setString(index++, "%"+title_code+"%");
			stmt.setString(index++, "%"+title_code+"%");
			rs = stmt.executeQuery();
			// get module list from ResultSet
			evalModuleVec = getEvalModuleListByResultSet(rs,page);
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}

		return evalModuleVec;
    }
    
    public Vector getEvalModuleOfTrainerByTcrId(Connection con, long rootEntId, boolean isCheckStatus, long curUserEntId,cwPagination page, String title_code) throws SQLException {
		Vector evalModuleVec = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Timestamp curTime = cwSQL.getTime(con);
		try {
			stmt = con.prepareStatement(SqlStatements.getEvalModuleOfTrainerByTcrId(isCheckStatus));
			int index = 1;
			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
			stmt.setString(index++, mod_type);
			stmt.setLong(index++, rootEntId);
			stmt.setBoolean(index++, true);
			stmt.setLong(index++, tcr_id);
			if (isCheckStatus) {
				stmt.setTimestamp(index++, curTime);
				stmt.setString(index++, "ON");
				stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
				stmt.setLong(index++, curUserEntId);
			}
			stmt.setString(index++, "%"+title_code+"%");
			stmt.setString(index++, "%"+title_code+"%");
			rs = stmt.executeQuery();
			// get module list from ResultSet
			evalModuleVec = getEvalModuleListByResultSet(rs,page);
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}

		return evalModuleVec;
    }
    
    private Vector getEvalModuleListByResultSet(ResultSet rs, cwPagination page) throws SQLException {
    	Vector evaluationModuleVec = new Vector();
    	if (page != null){
			if (page.pageSize <= 0) {
				page.pageSize = 10;
			}
			if (page.curPage <= 0) {
				page.curPage = 1;
			}			
		}
    	int count=1;
    	while (rs.next()){
    		if ((count > (page.curPage - 1) * page.pageSize) && (count <= page.curPage * page.pageSize)) {
	            dbModule dbmod = new dbModule();
	            dbmod.mod_res_id = rs.getLong("mod_res_id");
	            dbmod.res_id = dbmod.mod_res_id;
	            dbmod.mod_type = mod_type;
	            dbmod.res_subtype = mod_type;
	            dbmod.res_type = dbResource.RES_TYPE_MOD;
	            dbmod.res_title = rs.getString("res_title");
	            dbmod.res_upd_date = rs.getTimestamp("res_upd_date");
	            dbmod.mod_eff_start_datetime = rs.getTimestamp("mod_eff_start_datetime");
	            dbmod.mod_eff_end_datetime = rs.getTimestamp("mod_eff_end_datetime");
	            dbmod.res_status = rs.getString("res_status");
	            dbmod.res_desc = rs.getString("res_desc");
	            dbmod.tcr_title = rs.getString("tcr_title");
	            evaluationModuleVec.addElement(dbmod);
    		}
    		page.totalRec++;
			count++;
        }
    	if(evaluationModuleVec.size() == 0) {
    		evaluationModuleVec = null;
    	}
    	return evaluationModuleVec;
    }
    
    private static String getPublidEvaluationListSql(String joinSql, String conditionSql, boolean isMobaile) {
    	StringBuffer sqlBuffer = new StringBuffer();
    	sqlBuffer.append(" SELECT mod_res_id, res_title, mod_eff_start_datetime , mod_eff_end_datetime , res_status , mod_usr_id_instructor, res_upd_date, res_desc");
    	sqlBuffer.append(" from module")
    			.append(" inner join resources on (mod_res_id = res_id )")
    			.append(" inner join reguser  on (res_usr_id_owner = usr_id)");
    	if(joinSql != null) {
    		sqlBuffer.append(" ").append(joinSql);
    	}
    	sqlBuffer.append(" where mod_type = ? and usr_ste_ent_id = ? and mod_is_public = ? and mod_sgp_ind = ?");
    	if(isMobaile){
    		sqlBuffer.append("and mtc_mobile_ind = 1 ");
    	}
    	if(conditionSql != null) {
    		sqlBuffer.append(" ").append(conditionSql);
    	}
    	sqlBuffer.append(" order by res_upd_date desc");
    	
    	return sqlBuffer.toString();
    }
    public static Vector getPublicEvaluationList(Connection con,
			long rootEntId, long userEntId, boolean checkStatus,
			boolean isByTrainingCenter, long tcrId, boolean checkNoAttempted, WizbiniLoader wizbini)
			throws SQLException {
    	return getPublicEvaluationList(con, rootEntId, userEntId, checkStatus,
    			 isByTrainingCenter, tcrId, checkNoAttempted, false,  wizbini);
    }
    
	public static Vector getPublicEvaluationList(Connection con,
			long rootEntId, long userEntId, boolean checkStatus,
			boolean isByTrainingCenter, long tcrId, boolean checkNoAttempted, boolean isMobile, WizbiniLoader wizbini)
			throws SQLException {
    	Vector publicEvaluationVec = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
    	String conditionSql = "";
    	//for access by training center or get all public evaluation. 
		String joinSql = " inner join ModuleTrainingCenter on ( mtc_mod_id = mod_res_id";

		if(isByTrainingCenter) {
			conditionSql += " and (mtc_tcr_id = ? or mtc_tcr_id is null)";
		} else {
			conditionSql += " and (mtc_tcr_id in (" + ViewTrainingCenter.getLrnFliter( wizbini) + ") or mtc_tcr_id is null)";
		}
		joinSql +=	" ) inner join tcTrainingCenter on ( mtc_tcr_id = tcr_id and tcr_status = ? )";
		
		//check the evaluation has not been attempted.
		if(checkNoAttempted) {
			joinSql += " left join moduleEvaluation on ( mov_ent_id = " + userEntId + " and mov_mod_id = res_id and mov_tkh_id = 0)";
			conditionSql += " and (mov_total_attempt is null or mod_max_usr_attempt <= 0 or (mod_max_usr_attempt > 0 and mov_total_attempt < mod_max_usr_attempt))";			
		}
		//check status 
		if (checkStatus){
			conditionSql += " and ? between mod_eff_start_datetime AND mod_eff_end_datetime AND res_status = ? ";
			conditionSql += " and mod_res_id in (select eac_res_id from EvalAccess" +
            		" inner join entityRelation on ((ern_ancestor_ent_id = eac_target_ent_id or ern_child_ent_id = eac_target_ent_id) and ern_type = ?) " +
            		" where ern_child_ent_id = ?)";
        }
		
		Timestamp curTime = cwSQL.getTime(con);
    	
    	try{
    		stmt = con.prepareStatement(getPublidEvaluationListSql(joinSql, conditionSql, isMobile));
    		int index = 1;
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		
    		stmt.setString(index++, MOD_TYPE_EVN);
            stmt.setLong(index++, rootEntId);
            stmt.setBoolean(index++, true);
            stmt.setBoolean(index++, false);
            if(isByTrainingCenter) {
            	stmt.setLong(index++, tcrId);
            } else {
            	stmt.setLong(index++, userEntId);
            	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            }
    		if (checkStatus){
                stmt.setTimestamp(index++, curTime);
                stmt.setString(index++, "ON");
                stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                stmt.setLong(index++, userEntId);
            }
            
            rs = stmt.executeQuery();
            while (rs.next()){
            	if(publicEvaluationVec == null) {
            		publicEvaluationVec = new Vector();
            	}
                dbModule dbmod = new dbModule();
                dbmod.mod_res_id = rs.getLong("mod_res_id");
                dbmod.res_id = dbmod.mod_res_id;
                dbmod.mod_type = MOD_TYPE_EVN;
                dbmod.res_subtype = MOD_TYPE_EVN;
                dbmod.res_type = dbResource.RES_TYPE_MOD;
                dbmod.res_title = rs.getString("res_title");
                dbmod.res_upd_date = rs.getTimestamp("res_upd_date");
                dbmod.mod_eff_start_datetime = rs.getTimestamp("mod_eff_start_datetime");
                dbmod.mod_eff_end_datetime = rs.getTimestamp("mod_eff_end_datetime");
                dbmod.res_status = rs.getString("res_status");
                dbmod.res_desc = rs.getString("res_desc");
                publicEvaluationVec.addElement(dbmod);
            }
    	} finally {
    		if(stmt != null) {
    			stmt.close();
    		}
    	}
    	
    	return publicEvaluationVec;
    }    
    
    public static String getModLstAsXML(Connection con, Vector vtMod, Hashtable htAttemptNum, int page, int pagesize) throws cwException, SQLException /*, qdbException */{
    	return getModLstAsXML(con, vtMod, htAttemptNum, false,page,pagesize);
    }
    
    /*
    public static String getModLstAsXML(Connection con, Vector vtMod) throws cwException, SQLException, qdbException{
        return getModLstAsXML(con, vtMod, null);
    }
    */
    public static String getModLstAsXML(Connection con, Vector vtMod, Hashtable htAttemptNum, boolean isGetDetail,int page,int pagesize) throws cwException, SQLException /*, qdbException */{
        StringBuffer result = new StringBuffer();
        boolean ispage = true;
        if (pagesize == 0) {
        	ispage = false;
            pagesize = 10;
        }
        if (page == 0) {
        	ispage = false;
            page = 1;
        }
        int start = ((page-1) * pagesize) + 1;
        int end = page * pagesize;
        
        result.append("<module_list>").append(cwUtils.NEWL);
        
        if(vtMod != null) {
	    	ViewEvnSurveyQueReport viewEvnSurveyQueRpt = new ViewEvnSurveyQueReport(con);
	    	EvalAccess evalAccess = new EvalAccess();
	        for (int i=0; i<vtMod.size();i++){
	            dbModule dbmod = (dbModule) vtMod.elementAt(i);
	            if (ispage && i >= start-1 && i <= end-1) {
		            result.append("<module id=\"").append(dbmod.mod_res_id);
		            result.append("\" eff_start_datetime=\"").append(dbmod.mod_eff_start_datetime);
		            result.append("\" eff_end_datetime=\"").append(dbmod.mod_eff_end_datetime);
		            result.append("\" status=\"").append(dbmod.res_status);
		            result.append("\" timestamp=\"").append(dbmod.res_upd_date);
		            result.append("\" type=\"").append(dbmod.mod_type);
		            result.append("\" public_used=\"").append(dbmod.isPublicUsed(con));
		            result.append("\" >").append(cwUtils.NEWL);
		
		            result.append("<title>").append(cwUtils.esc4XML(dbmod.res_title)).append("</title>");
		            if (htAttemptNum!=null){
		                Long modId = new Long(dbmod.mod_res_id);
		                Long attemptNbr = (Long)htAttemptNum.get(modId);
		                result.append("<attempt_nbr>").append(cwUtils.escNull(attemptNbr)).append("</attempt_nbr>");
		            }
		            
		            //public evaluation
		            if(dbmod.tcr_title != null) {
		            	result.append("<tcr_title>").append(cwUtils.esc4XML(dbmod.tcr_title)).append("</tcr_title>");
		            }
		            
		            //add for the detail information of module
		            if(isGetDetail) {
		            	result.append("<desc>").append(cwUtils.esc4XML(dbmod.res_desc)).append("</desc>");
		            	//target object
		            	evalAccess.eac_res_id = dbmod.mod_res_id;
		            	String targetObject = evalAccess.getTargetDisplayByRes_IDASXML(con);
		            	result.append(targetObject);
		            	//reponse count
		            	long reponseCount = viewEvnSurveyQueRpt.getReponseCount(dbmod.mod_res_id);
		            	result.append("<response_count>").append(reponseCount).append("</response_count>");
		            }
	
		            result.append("</module>");
	            }else if(!ispage){
	            	result.append("<module id=\"").append(dbmod.mod_res_id);
		            result.append("\" eff_start_datetime=\"").append(dbmod.mod_eff_start_datetime);
		            result.append("\" eff_end_datetime=\"").append(dbmod.mod_eff_end_datetime);
		            result.append("\" status=\"").append(dbmod.res_status);
		            result.append("\" timestamp=\"").append(dbmod.res_upd_date);
		            result.append("\" type=\"").append(dbmod.mod_type);
		            result.append("\" public_used=\"").append(dbmod.isPublicUsed(con));
		            result.append("\" >").append(cwUtils.NEWL);
		
		            result.append("<title>").append(cwUtils.esc4XML(dbmod.res_title)).append("</title>");
		            if (htAttemptNum!=null){
		                Long modId = new Long(dbmod.mod_res_id);
		                Long attemptNbr = (Long)htAttemptNum.get(modId);
		                result.append("<attempt_nbr>").append(cwUtils.escNull(attemptNbr)).append("</attempt_nbr>");
		            }
		            
		            //public evaluation
		            if(dbmod.tcr_title != null) {
		            	result.append("<tcr_title>").append(cwUtils.esc4XML(dbmod.tcr_title)).append("</tcr_title>");
		            }
		            
		            //add for the detail information of module
		            if(isGetDetail) {
		            	result.append("<desc>").append(cwUtils.esc4XML(dbmod.res_desc)).append("</desc>");
		            	//target object
		            	evalAccess.eac_res_id = dbmod.mod_res_id;
		            	String targetObject = evalAccess.getTargetDisplayByRes_IDASXML(con);
		            	result.append(targetObject);
		            	//reponse count
		            	long reponseCount = viewEvnSurveyQueRpt.getReponseCount(dbmod.mod_res_id);
		            	result.append("<response_count>").append(reponseCount).append("</response_count>");
		            }
	
		            result.append("</module>");
	            }
	        }
        }
        result.append("</module_list>");
        result.append("<cwPage page=\"").append(page).append("\" page_size=\"")
        		.append(pagesize).append("\" total=\"").append(vtMod==null ? 0 : vtMod.size()).append("\" />");
        return result.toString();
    }

    public static boolean checkModRootIdUniqueInCos(Connection con, long cos_id, long mod_mod_id_root) throws SQLException{
        boolean hasRec;
        String sql = "SELECT mod_res_id FROM resourceContent , module WHERE rcn_res_id = ? AND mod_mod_id_root = ? and mod_res_id = rcn_res_id_content ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, cos_id);
        stmt.setLong(2, mod_mod_id_root);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            hasRec = true;
        }else{
            hasRec = false;
        }
        cwSQL.cleanUp(rs, stmt);
        return !hasRec;
    }
    //
    public static Vector dumpModNQ(Connection con, long mod_id, String mod_title, String uploadDir, loginProfile prof, Vector vtClsId, boolean isFormTemplet) throws cwException, cwSysMessage, SQLException{
        try{
            Vector oldChildAss = dbResourceContent.getChildAss(con, mod_id);
            dbModule newMod = new dbModule();
            newMod.mod_res_id = mod_id;
            newMod.res_id = mod_id;
            newMod.get(con);
            Timestamp curTime = cwSQL.getTime(con);
            newMod.res_create_date = curTime;
            newMod.res_upd_date = curTime;
            newMod.res_upd_user = prof.usr_id;
            newMod.mod_is_public = false;
            if (mod_title == null) {
                newMod.mod_in_eff_start_datetime = newMod.mod_eff_start_datetime;
                newMod.mod_in_eff_end_datetime = newMod.mod_eff_end_datetime;
            } else {
                newMod.res_title = mod_title;
                newMod.mod_in_eff_start_datetime = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
                newMod.mod_in_eff_end_datetime = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
            }
            if (isFormTemplet) {
                newMod.mod_mod_id_root = (int) mod_id;
            }
            newMod.res_status = RES_STATUS_OFF;
            newMod.mod_mod_res_id_parent = 0;
            newMod.ins(con, prof);
            long newModId = newMod.mod_res_id;
//          make new mod_res_id vector                     
            Vector vtModId = new Vector();  
            vtModId.addElement(new Long(newModId));  
            if (vtClsId != null) {
                for (int i = 0; i < vtClsId.size(); i++) {
                    newMod.mod_mod_res_id_parent = newModId;
                    newMod.ins(con, prof);
                    vtModId.addElement(new Long(newMod.mod_res_id));
                }
            }
            newMod.mod_res_id = newModId;

            if (newMod.mod_res_id == mod_id){
                throw new cwException("Error: cannot ins a new module!");
            }
//            String[] que_id_lst = new String[oldChildAss.size()];
//            for (int i=0; i<oldChildAss.size(); i++){
//                que_id_lst[i] = "" + ((dbResourceContent) oldChildAss.elementAt(i)).rcn_res_id_content ;
//            }

//            newMod.insQ(con, que_id_lst, uploadDir, prof);

            int i;
            dbResourceContent resObj = new dbResourceContent();
            Vector failList = new Vector();
            long newId = 0;

//            Vector idobjVec = dbResourceObjective.getResObj(con, que_id_lst);
//            Vector idVec = (Vector) idobjVec.elementAt(0);
//            Vector objVec = (Vector) idobjVec.elementAt(1);

            for(i =0;i< oldChildAss.size();i++) {
                dbResourceContent resCon = (dbResourceContent) oldChildAss.elementAt(i);
                long tmpId = resCon.rcn_res_id_content;
                //  duplicate the question
                newId = dbQuestion.insTestQ(con, newModId, tmpId, 0, prof.usr_id);
                if (newId <=0) {
                    failList.addElement(new Long(tmpId));
                }else {
                    resCon.rcn_res_id_content = newId;
                    resCon.rcn_res_id = newModId;
                    resCon.rcn_rcn_res_id_parent = 0;
                    resCon.rcn_rcn_sub_nbr_parent = 0;
                    resCon.ins(con);

                    dbUtils.copyMediaFrom(uploadDir, tmpId, newId);
                    for (int j = 1; vtModId != null && j < vtModId.size(); j++) {
                        dbResourceContent resobj = new dbResourceContent();
                        resObj.rcn_res_id = ((Long) vtModId.get(j)).longValue();
                        resObj.rcn_res_id_content = newId;
                        resObj.rcn_rcn_res_id_parent = newModId;
                        resObj.rcn_rcn_sub_nbr_parent = resCon.rcn_sub_nbr;
                        resObj.ins(con);
                    }
                }
            }

            if (failList.size() > 0) {
                //String mesg = "The following question(s) are failed to insert into the test :\n";
                //mesg += dbUtils.NEWL;

                String mesg = new String();
                for (i=0;i<failList.size();i++) {
                    if (i!=0)
                        mesg += ", ";

                    mesg += ((Long) failList.elementAt(i)).longValue();
                }

                //mesg += "\nReason : Question(s) exist in the test.";

                throw new qdbErrMessage("MOD002", mesg);
            }else{
                return vtModId;
            }
        }catch(qdbErrMessage e){
            throw new cwSysMessage(e.getMessage());
        }catch(qdbException e){
            throw new cwException(e.getMessage());
        }
    }

    public static long getMaxUsrAttempt(Connection con, long mod_id) throws SQLException{
        long max_usr_attempt;
        PreparedStatement stmt = con.prepareStatement("SELECT mod_max_usr_attempt FROM module WHERE mod_res_id = ? ");
        stmt.setLong(1, mod_id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            max_usr_attempt = rs.getLong("mod_max_usr_attempt");
        }else{
        	stmt.close();
            throw new SQLException("no this module:" + mod_id);
        }
        stmt.close();
        return max_usr_attempt;
    }


    /**
    * Update Assessment Related Filed
    * @param database connection
    * @param user profile
    */
    public void updAsmRelated(Connection con, loginProfile prof)
        throws SQLException, cwException {

            super.updAsmReleated(con, prof);

            String SQL = " UPDATE Module "
                       + " SET mod_eff_start_datetime = ?, mod_eff_end_datetime = ?, "
                       + " mod_max_usr_attempt = ?, mod_pass_score = ? "
                       + " WHERE mod_res_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setTimestamp(1, mod_eff_start_datetime);
            stmt.setTimestamp(2, mod_eff_end_datetime);
            stmt.setLong(3, mod_max_usr_attempt);
            stmt.setFloat(4, mod_pass_score);
            stmt.setLong(5, mod_res_id);
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
                throw new cwException(" Failed to Update Module, id = " + mod_res_id);
            return;

        }



    public static long getModuleItemId(Connection con, long mod_id)
        throws SQLException {

        String SQL = " SELECT cos_itm_id "
                   + " FROM ResourceContent, Course "
                   + " WHERE rcn_res_id_content = ? "
                   + " AND rcn_res_id = cos_res_id ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, mod_id);
        ResultSet rs = stmt.executeQuery();
        long itm_id = 0;
        if(rs.next())
            itm_id = rs.getLong("cos_itm_id");
        else
            throw new SQLException("Failed to get module item id, mod_id = " + mod_id);
        stmt.close();
        return itm_id;

    }

    private boolean isPublicUsed(Connection con) throws SQLException {
        boolean result = false;

        String sqlStr = " SELECT mod_res_id FROM Module WHERE mod_mod_id_root = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sqlStr);
            stmt.setLong(1, this.mod_res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                result = true;
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw sqlEx;
        }

        return result;
    }

    /**
    When a module is launch, save the progress tracking data for this attempt
    @param con Connection to database
    @param prof loginProfile of the learner who launch this module
    @param tkh_id TrackingHistory id of the attempt
     * @throws qdbErrMessage 
    */
    
    public void saveTrackingDataAtLaunch(Connection con, loginProfile prof, long tkh_id, long cos_id) 
        throws SQLException, cwException, cwSysMessage, qdbException, qdbErrMessage {
        //get the module's course id
    	if(cos_id == 0){
    		cos_id = getCosId(con, mod_res_id);
    	}

        //get the Tracking History id if it is not defined
        if (tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
            tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, mod_res_id, prof.usr_ent_id);
        }

        //save the tracking data to ModuleEvaluation, ModuleEvaluationHistory and CourseEvaluation
        dbModuleEvaluation mov = new dbModuleEvaluation();
        mov.mov_cos_id = cos_id;
        mov.mov_ent_id = prof.usr_ent_id;
        mov.mov_tkh_id = tkh_id;
        mov.mov_mod_id = mod_res_id;
        if(attempt_add){
            mov.attempt_counted = false;
	    }else{
	        mov.attempt_counted = true;
	    }
        mov.mod_time = 0f;          //time consumed is 0 as the module is just launch
        mov.score_delta = 0f;       //no score changes as the module is just launch
        mov.mov_score = "0";       //keep the score unchange as the module is just launch
        mov.mov_status = dbModuleEvaluation.STATUS_IN_PROGRESS; //the status is changed to in progress
        mov.save(con, prof);
        return;
    }

    /**
    Fix the questions' order. The order should be in 1,2,3,...,n,n+1
    Only Standard Test (TST) needs to fix its questions' order
    @parm con Connection to database
    */
    private void fixQueOrder(Connection con) throws SQLException, qdbException {
        
        if(mod_type == null || mod_type.equalsIgnoreCase(MOD_TYPE_TST)|| mod_type.equals(MOD_TYPE_SVY) || mod_type.equals(MOD_TYPE_EVN)) {
            //only standard test (TST) needs to fix que order bt its nature
            //for other module type, nothing should have been fixed
            //therefore try to fix the que order if the module tpye is unknown
            PreparedStatement stmt = null;
            try {
                //get the module's questions
                Vector vQue = dbResourceContent.getChildAss(con, mod_res_id);
                for(int i=0; i<vQue.size(); i++) {
                    dbResourceContent rcn = (dbResourceContent) vQue.elementAt(i);
                    if(rcn.rcn_order != i+1) {
                        //if the que is in order, update it
                        rcn.rcn_order = i+1;
                        rcn.updOrder(con);
                    }
                }
            } finally {
                if(stmt!=null) {stmt.close();}
            }
        }
        return;
    }

    public long numEssay(Connection con) throws qdbException {
        try {
            long num = 0;
            String SQL = " SELECT count(que_res_id) as num "
                    + " FROM question, resourceContent "
                    + " WHERE rcn_res_id = ? "
                    + " AND que_res_id = rcn_res_id_content "
                    + " AND (que_type = ? or que_type = ?) ";
            int index = 1;
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, this.mod_res_id);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY);
            stmt.setString(index++, dbQuestion.QUE_TYPE_ESSAY_2);
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                num = rs.getLong("num");
            } else {
                num = 0;
            }
            cwSQL.cleanUp(rs, stmt);
            return num;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
	
	public boolean hasEssay(Connection con) throws qdbException {
		boolean hasEssay = false;
		if(res_subtype.equalsIgnoreCase(MOD_TYPE_TST)) {
			if(numEssay(con) > 0) {
				hasEssay = true;
			} else {
				hasEssay = false;
			}
		} else if(res_subtype.equalsIgnoreCase(MOD_TYPE_DXT)) {
			Vector dbMspVec = new Vector();
			try {
				dbMspVec = dbModuleSpec.getModuleSpecs(con, mod_res_id);
			} catch (SQLException e) {
				throw new qdbException(e.getMessage());
			}
			for(int k=0; k<dbMspVec.size(); k++) {
				dbModuleSpec dbMsp = (dbModuleSpec) dbMspVec.elementAt(k);
				if(dbMsp.msp_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY) || dbMsp.msp_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
					hasEssay = true;
					break;
				}
			}
		}
		
		return hasEssay;
	}
	public String getAvailableOnLineWithScoreListAsXml(Connection con, long cos_id, long ccr_id) throws SQLException , cwSysMessage{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer xmlBuf = new StringBuffer(2000);
		xmlBuf.append("<online_module>").append(cwUtils.NEWL);
		stmt = con.prepareStatement(GET_ONLINE_MODULE_WITH_SCORE_SQL);
		stmt.setLong(1, cos_id);
		stmt.setLong(2, ccr_id);		
		rs = stmt.executeQuery();
		while (rs.next()){
		    String mod_type = rs.getString("mod_type");
		    Float mod_pass_score = rs.getFloat("mod_pass_score");
		    Float mod_max_score = rs.getFloat("mod_max_score");
		    if("TST".equalsIgnoreCase(mod_type) || "DXT".equalsIgnoreCase(mod_type) || "ASS".equalsIgnoreCase(mod_type)){
		        mod_pass_score = mod_max_score * (mod_pass_score/100);
		    }
			xmlBuf.append("<module id=\"").append(rs.getLong("mod_res_id")).append("\">").append(cwUtils.NEWL);
			xmlBuf.append("<title>").append(cwUtils.esc4XML(rs.getString("res_title"))).append("</title>").append(cwUtils.NEWL);
			xmlBuf.append("<max_score>").append(rs.getFloat("mod_max_score")).append("</max_score>").append(cwUtils.NEWL);
			xmlBuf.append("<pass_score>").append(cwUtils.formatNumber(mod_pass_score, 1)).append("</pass_score>").append(cwUtils.NEWL);
			xmlBuf.append("<status>").append(rs.getString("res_status")).append("</status>");
			xmlBuf.append("</module>").append(cwUtils.NEWL);			
		}
		cwSQL.cleanUp(rs, stmt);
		xmlBuf.append("</online_module>").append(cwUtils.NEWL);
		return xmlBuf.toString();
	}
    
	public Vector getAvailableOnLineWithScoreVec(Connection con, long cos_id, long ccr_id) throws SQLException , cwSysMessage{
		Vector onlineScoreMod = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		stmt = con.prepareStatement(GET_ONLINE_MODULE_WITH_SCORE_SQL);
		stmt.setLong(1, cos_id);
		stmt.setLong(2, ccr_id);		
		rs = stmt.executeQuery();
		dbModule mod = null;
		while (rs.next()){
			mod = new dbModule();
			mod.mod_type = rs.getString("mod_type");
		    mod.mod_pass_score = rs.getFloat("mod_pass_score");
		    mod.mod_max_score = rs.getFloat("mod_max_score");
		    if("TST".equalsIgnoreCase(mod_type) || "DXT".equalsIgnoreCase(mod_type) || "ASS".equalsIgnoreCase(mod_type)){
		    	mod.mod_pass_score = mod.mod_max_score * (mod.mod_pass_score/100);
		    }
		    mod.mod_res_id = rs.getLong("mod_res_id");
		    mod.mod_res_title = rs.getString("res_title");
		    onlineScoreMod.add(mod);
		}
		cwSQL.cleanUp(rs, stmt);
		return onlineScoreMod;
	}
	
	public String getAllOnLineWithScoreListAsXml(Connection con, long cos_id, long ccr_id) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer xmlBuf = new StringBuffer(2000);
		xmlBuf.append("<online_module>").append(cwUtils.NEWL);
		stmt = con.prepareStatement(GET_ALL_ONLINE_MODULE_WITH_SCORE_SQL);
		stmt.setLong(1, cos_id);
		stmt.setLong(2, cos_id);	
		rs = stmt.executeQuery();
		while (rs.next()){
			xmlBuf.append("<module id=\"").append(rs.getLong("mod_res_id")).append("\">").append(cwUtils.NEWL);
			xmlBuf.append("<title>").append(rs.getString("res_title")).append("</title>").append(cwUtils.NEWL);
			xmlBuf.append("<max_score>").append(rs.getFloat("mod_max_score")).append("</max_score>").append(cwUtils.NEWL);
			xmlBuf.append("<pass_score>").append(rs.getFloat("mod_pass_score")).append("</pass_score>").append(cwUtils.NEWL);
			xmlBuf.append("</module>").append(cwUtils.NEWL);			
		}
		cwSQL.cleanUp(rs, stmt);
		xmlBuf.append("</online_module>").append(cwUtils.NEWL);
		return xmlBuf.toString();
	}
    
	public String getAllOnlineModuleAsXml(Connection con, long cos_id, long ccr_id) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer xmlBuf = new StringBuffer(2000);
		xmlBuf.append("<online_module>").append(cwUtils.NEWL);
		stmt = con.prepareStatement(GET_ALL_AVAILABLE_ONLINE_MODULE_SQL);
		stmt.setLong(1, cos_id);
		stmt.setLong(2, ccr_id);
		rs = stmt.executeQuery();
		while (rs.next()){
			xmlBuf.append("<module id=\"").append(rs.getLong("mod_res_id")).append("\">").append(cwUtils.NEWL);
			xmlBuf.append("<title>").append(cwUtils.esc4XML(rs.getString("res_title"))).append("</title>").append(cwUtils.NEWL);
			xmlBuf.append("<res_status>").append(rs.getString("res_status")).append("</res_status>").append(cwUtils.NEWL);
			xmlBuf.append("<type>").append(dbUtils.esc4XML(rs.getString("mod_type").toLowerCase())).append("</type>");
			xmlBuf.append("<duration>").append(rs.getInt("mod_required_time")).append("</duration>");
			
			
			//xmlBuf.append("<max_score>").append(rs.getFloat("mod_max_score")).append("</max_score>").append(cwUtils.NEWL);
			//xmlBuf.append("<pass_score>").append(rs.getFloat("mod_pass_score")).append("</pass_score>").append(cwUtils.NEWL);
			xmlBuf.append("</module>").append(cwUtils.NEWL);			
		}
		cwSQL.cleanUp(rs, stmt);
		xmlBuf.append("</online_module>").append(cwUtils.NEWL);
		return xmlBuf.toString();
	}
	public String getModuleAsXmlByCmrResId(Connection con, long cmr_res_id) throws SQLException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer xmlBuf = new StringBuffer(2000);
		xmlBuf.append("<online_module>").append(cwUtils.NEWL);
		stmt = con.prepareStatement(GET_ONLINE_MODULE_BY_RES_ID);
		stmt.setLong(1,cmr_res_id);
		rs = stmt.executeQuery();
		if (rs.next()){
		    mod_pass_score = rs.getFloat("mod_pass_score");
		    String mod_type = rs.getString("mod_type");
            if("TST".equalsIgnoreCase(mod_type) || "DXT".equalsIgnoreCase(mod_type) || "ASS".equalsIgnoreCase(mod_type)){
                mod_pass_score = rs.getFloat("mod_max_score") * (mod_pass_score/100);
            }
			xmlBuf.append("<module id=\"").append(rs.getLong("mod_res_id")).append("\">").append(cwUtils.NEWL);
			xmlBuf.append("<title>").append(cwUtils.esc4XML(rs.getString("res_title"))).append("</title>").append(cwUtils.NEWL);
			xmlBuf.append("<max_score>").append(rs.getFloat("mod_max_score")).append("</max_score>").append(cwUtils.NEWL);
			xmlBuf.append("<pass_score>").append(cwUtils.formatNumber(mod_pass_score, 1)).append("</pass_score>").append(cwUtils.NEWL);
			xmlBuf.append("<status>").append(rs.getString("res_status")).append("</status>");
			xmlBuf.append("</module>").append(cwUtils.NEWL);
		}
		cwSQL.cleanUp(rs, stmt);
		xmlBuf.append("</online_module>").append(cwUtils.NEWL);
		return xmlBuf.toString();
	}
    
    public static float getPassingScore(Connection con, long mod_id)
            throws SQLException {

            String SQL = " Select mod_pass_score From Module Where mod_res_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mod_id);
            ResultSet rs = stmt.executeQuery();
            float pass_score = 0;
            if (rs.next()) {
                pass_score = rs.getFloat("mod_pass_score");
            }
            stmt.close();
            return pass_score;
        }

        public void selectAssessment(
            Connection con,
            loginProfile prof,
            long asm_res_id,
            String asm_type,
            String uploadDir,
            Vector vtClassModId)
            throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
            selectAssessment(con, prof, asm_res_id, asm_type, uploadDir, false, vtClassModId);
        }

        public void selectAssessment(
            Connection con,
            loginProfile prof,
            long asm_res_id,
            String asm_type,
            String uploadDir,
            boolean boolPreviewAssessment,
            Vector vtClassModId)
            throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
            this.get(con);

            // for dynamic assessment
            if (asm_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DAS)) {
                Vector vtDbQueContainerSpec =
                    DbQueContainerSpec.getQueContainerSpecs(con, asm_res_id);
                //          if (vtDbQueContainerSpec.size() <= 0) {
                //              throw new qdbErrMessage("ASM004");
                //          }           
                for (int i = 0; i < vtDbQueContainerSpec.size(); i++) {
                    DbQueContainerSpec myDbQueContainerSpec =
                        (DbQueContainerSpec)vtDbQueContainerSpec.elementAt(i);

                    dbModuleSpec myDbModuleSpec = new dbModuleSpec();
                    myDbModuleSpec.msp_res_id = mod_res_id;
                    myDbModuleSpec.msp_obj_id = myDbQueContainerSpec.qcs_obj_id;
                    myDbModuleSpec.msp_type = myDbQueContainerSpec.qcs_type;
                    myDbModuleSpec.msp_score = myDbQueContainerSpec.qcs_score;
                    myDbModuleSpec.msp_difficulty =
                        (int)myDbQueContainerSpec.qcs_difficulty;
                    myDbModuleSpec.msp_privilege =
                        myDbQueContainerSpec.qcs_privilege;
                    myDbModuleSpec.msp_duration = myDbQueContainerSpec.qcs_duration;
                    myDbModuleSpec.msp_qcount = myDbQueContainerSpec.qcs_qcount;

                    myDbModuleSpec.save(con);
                }
            }
            // for fixed assessment
            else {
                Vector vtDbResourceContent =
                    dbResourceContent.getChildAss(con, asm_res_id);
                String[] que_id_lst = new String[vtDbResourceContent.size()];
                for (int i = 0; i < vtDbResourceContent.size(); i++) {
                    dbResourceContent myDbResourceContent =
                        (dbResourceContent)vtDbResourceContent.elementAt(i);
                    que_id_lst[i] =
                        Long.toString(myDbResourceContent.rcn_res_id_content);
                }

                //          if (que_id_lst.length <= 0) {
                //              throw new qdbErrMessage("ASM008");
                //          }           

                if (boolPreviewAssessment == false) {
                    FixedQueContainer myFixedQueContainer = new FixedQueContainer();
                    myFixedQueContainer.res_id = mod_res_id;
                    myFixedQueContainer.res_type = dbResource.RES_TYPE_ASM;
                    myFixedQueContainer.res_subtype = asm_type;
                    myFixedQueContainer.addQuestion(
                        con,
                        que_id_lst,
                        uploadDir,
                        prof,
                        true,
                        vtClassModId);

                    Vector vtQueContainerSpecs =
                        DbQueContainerSpec.getQueContainerSpecs(con, asm_res_id);
                    long[] lstResID = new long[vtQueContainerSpecs.size()];
                    long[] lstObjID = new long[vtQueContainerSpecs.size()];
                    for (int i = 0; i < vtQueContainerSpecs.size(); i++) {
                        DbQueContainerSpec myDbQueContainerSpec =
                            (DbQueContainerSpec)vtQueContainerSpecs.elementAt(i);
                        lstResID[i] = mod_res_id;
                        lstObjID[i] = myDbQueContainerSpec.qcs_obj_id;
                    }
                    dbResourceObjective myDbResourceObjective =
                        new dbResourceObjective();
                    myDbResourceObjective.insResObj(con, lstResID, lstObjID);
                    
                    if (vtClassModId != null) {
                        long[] classModId = new long[vtClassModId.size()];
                        for (int i = 0; i < vtClassModId.size(); i++) {
                            classModId[i] = ((Long)vtClassModId.get(i)).longValue();
                        }
                        myDbResourceObjective.insResObj(con, classModId, lstObjID);
                    }
                }
                // do not clone question for the case of Assessment Preview
                else {
                    for (int i = 0; i < que_id_lst.length; i++) {
                        dbResourceContent rcn = new dbResourceContent();
                        rcn.rcn_res_id = mod_res_id;
                        rcn.rcn_res_id_content = Long.parseLong(que_id_lst[i]);
                        rcn.rcn_score_multiplier = 1;
                        rcn.ins(con);
                    }
                }
            }

            // update the module's max. score and duration
            this.updMaxScore(con);
            updMaxScoreForChild(con);
        }   
        public static long getChidltmModID(Connection con, long parent_mod_id, long child_itm_id) throws SQLException {
        long mod_id = 0;
        String sql = "select mod_res_id from Module,ResourceContent,course "
                + " where cos_itm_id=? and cos_res_id=rcn_res_id and mod_res_id=rcn_res_id_content and mod_mod_res_id_parent=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1, child_itm_id);
        pstmt.setLong(2, parent_mod_id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            mod_id = rs.getLong("mod_res_id");
        }
        pstmt.close();
        return mod_id;

    }

    //return a module is publish or not
    public String getModStatus(Connection con) throws SQLException {
        String sql = "select res_status from resources where res_id = ? ";
        String res_status = null;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1, mod_res_id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            res_status = rs.getString("res_status");
        }
        pstmt.close();
        return res_status;
    }
    
    public String getSSOXML(Connection con, loginProfile prof, String sso_refer, String sso_lms_url) throws qdbException, qdbErrMessage, SQLException, cwException, cwSysMessage {
        StringBuffer resultXML = new StringBuffer();
        long usr_id = prof.usr_ent_id;
        long last_att_no = 0;
        
        if (tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
            tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, res_id, prof.usr_ent_id);
        }
        
        dbModuleEvaluation dbMov = new dbModuleEvaluation();
        dbMov.mov_ent_id = usr_id;
        dbMov.mov_tkh_id = tkh_id;
        dbMov.mov_mod_id = mod_res_id;
        dbMov.get(con);

        long cnt = dbProgress.usrAttemptNum(con, res_id, prof.usr_id, tkh_id);
        //if type is svy and had attempted,throw a message .
        if (mod_type.equals(MOD_TYPE_SVY) && cnt > 0) {
            throw new cwSysMessage("MOD016");
        }
        
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        resultXML.append(result);
        
        resultXML.append("<SSO_module_content>")
        .append(prof.asXML()).append("<module id=\"").append(mod_res_id).append("\" ");
        if (isDiffDomain(sso_refer, sso_lms_url)) {
        	resultXML.append("diff_domain=\"").append("true").append("\" ");
        } else {
        	resultXML.append("diff_domain=\"").append("false").append("\" ");
        }
        resultXML.append("type=\"").append(mod_type).append("\" ")
        .append("usr_id=\"").append(prof.usr_id).append("\" ")
        .append("cos_id=\"").append(getCosId(con, mod_res_id)).append("\" ")
        .append("mod_web_launch=\"");
        if (mod_web_launch != null) {
            resultXML.append(dbUtils.esc4XML(mod_web_launch));
        }
        resultXML.append("\" ")
        .append("mod_vendor=\"");
        if (mod_vendor != null) {
            resultXML.append(dbUtils.esc4XML(mod_vendor));
        }
        resultXML.append("\" ")
        .append("mod_status=\"").append(dbMov.mov_status).append("\" ");
        if (res_src_link != null) {
            resultXML.append("res_src_link=\"").append(dbUtils.esc4XML(res_src_link)).append("\" ");
        }
        else {
            resultXML.append("res_src_link=\"\" ");
        }
        resultXML.append("sub_after_passed_ind=\"").append(mod_sub_after_passed_ind).append("\" ")
        .append("usr_max_attempt=\"").append(mod_max_attempt).append("\" ");
        
        if (mod_type.equals(MOD_TYPE_AICC_U) || mod_type.equals(MOD_TYPE_ASS)
            || mod_type.equals(MOD_TYPE_TST) || mod_type.equals(MOD_TYPE_DXT)
            || mod_type.equals(MOD_TYPE_SVY)) {
            dbProgress pgr = new dbProgress();

            if (cnt > 0) {
                resultXML.append("attempt_nbr=\"").append(cnt).append("\" ");
            }
            resultXML.append("/>");
            long numEssay = pgr.numEssay(con, mod_res_id, prof.usr_id, cnt);
            boolean isEssayMarked = pgr.isEssayMarked(con, mod_res_id, prof.usr_id, cnt, tkh_id);
            long numEssayNotMarked = pgr.numEssayNotMarked(con, mod_res_id, tkh_id, cnt);
            if(numEssay>0) {
                if(isEssayMarked) {
                    resultXML.append("<essay_grade_status>Graded</essay_grade_status>");
                } else if(numEssay == numEssayNotMarked) {
                    resultXML.append("<essay_grade_status>Not Graded</essay_grade_status>");
                } else if(numEssay > numEssayNotMarked) {
                    resultXML.append("<essay_grade_status>Being Graded</essay_grade_status>");
                }
            }
        } else {
            resultXML.append("/>");
        }
        //get module tpl_name
        String tpl_name = null;
        tpl_name = getModTplName(con, mod_res_id, prof);
        if (tpl_name != null && tpl_name.length() > 0) {
            resultXML.append("<stylesheet>").append(tpl_name).append("</stylesheet>");
        }
        
        resultXML.append("<tkh_info tkh_id=\"").append(tkh_id).append("\"/>")
        .append("</SSO_module_content>");
        return resultXML.toString();
    }

    private boolean isDiffDomain(String sso_refer, String sso_lms_url) {
    	boolean is_sso_diff_domain = true;
		if (sso_refer != null && sso_refer.length() > 0) {
			int temp_pos = sso_refer.indexOf("//") + 2;
			int temp_sec_pos = sso_refer.indexOf("/", temp_pos);
			String temp = sso_refer.substring(temp_pos, temp_sec_pos);
			temp_pos = sso_lms_url.indexOf("//") + 2;
			temp_sec_pos = sso_lms_url.indexOf("/", temp_pos);
			String temp_lms_url = sso_lms_url.substring(temp_pos, temp_sec_pos);
			if (temp.equalsIgnoreCase(temp_lms_url)) {
				is_sso_diff_domain = false;
			}
		} else {
			return true;
		}
		return is_sso_diff_domain;
	}



	private String getModTplName(Connection con, long mod_res_id, loginProfile prof) throws SQLException, qdbException {
        String sql = "select res_tpl_name from resources where res_id = ? ";
        String tpl_name = null;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1, mod_res_id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            tpl_name = rs.getString("res_tpl_name");
        }
        
        if (tpl_name !=null && tpl_name.length() > 0) {
            dbTemplate dbtpl = new dbTemplate();
            dbtpl.tpl_name = tpl_name;
            dbtpl.tpl_lan = prof.label_lan;
            dbtpl.get(con);
            tpl_name = dbtpl.tpl_stylesheet;
        }
        pstmt.close();
        return tpl_name;
    }

    public Vector getChildModResId(Connection con) throws SQLException {
        Vector clsModId = new Vector();
        String SQL = "select mod_res_id from Module where mod_mod_res_id_parent = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mod_res_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clsModId.add(new dbModule(rs.getLong(1)));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return clsModId;
    }

    /**
     * determine if the current module resides in a learning solution of type classroom that is not a class
     * if so, start date, end date, status cannot be modified 
     */
    public boolean isChangeDate(Connection con) throws SQLException {
        String SQL = "select itm_id from aeItem where itm_type = 'CLASSROOM' and itm_run_ind = 0 and itm_id in "
                   + "(select cos_itm_id from course where cos_res_id in "
                   + "(select rcn_res_id from resourcecontent where rcn_res_id_content = ?))";
        boolean changeDate = true;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mod_res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                changeDate = false;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return changeDate;
    }

    public void updMaxScoreForChild(Connection con) throws SQLException {
    	String SQL = " select m.mod_max_score from module m where mod_res_id = ? ";
    	PreparedStatement stmt = con.prepareStatement(SQL);
    	long mod_max_score = 0 ;
    	try{
    		stmt.setLong(1, mod_res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	mod_max_score = rs.getLong("mod_max_score");
            }
    	}finally{
            if (stmt != null) {
                stmt.close();
            }
    	}
    	

    	if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
            SQL = "update Module m1 INNER JOIN Module m2 ON m1.mod_res_id = m2.mod_res_id AND m2.mod_mod_res_id_parent = ? "
                    + " set m1.mod_max_score = ? ";
    	}else{
        	SQL = "update Module set mod_max_score = (select mod_max_score from Module where mod_res_id = ?) "
                    + "where mod_res_id in (select mod_res_id from Module where mod_mod_res_id_parent = ?)";
    	}


        stmt = con.prepareStatement(SQL);
        try {
        	if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
        		stmt.setLong(1, mod_res_id);
                stmt.setLong(2, mod_max_score);
        	}else{
        		stmt.setLong(1, mod_res_id);
                stmt.setLong(2, mod_res_id);
        	}
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    public void initialize(dbModule dbmod){
        this.res_title = dbmod.res_title;
        this.res_desc = dbmod.res_desc;
        this.res_tpl_name = dbmod.res_tpl_name;
        this.mod_logic = dbmod.mod_logic;
        this.mod_pass_score = dbmod.mod_pass_score;
        this.res_duration = dbmod.res_duration;
        this.mod_instruct = dbmod.mod_instruct;
        this.mod_max_attempt = dbmod.mod_max_attempt;
        this.mod_max_usr_attempt = dbmod.mod_max_usr_attempt;
        this.mod_sub_after_passed_ind = dbmod.mod_sub_after_passed_ind;
        this.mod_show_answer_ind = dbmod.mod_show_answer_ind;
        this.res_src_link = dbmod.res_src_link;
        this.res_src_type = dbmod.res_src_type;
        this.res_annotation = dbmod.res_annotation;
        this.mod_instruct = dbmod.mod_instruct;
        this.mod_show_save_and_suspend_ind = dbmod.mod_show_save_and_suspend_ind;
        this.mod_managed_ind = dbmod.mod_managed_ind;
        this.res_vod_duration = dbmod.res_vod_duration;
        this.res_img_link= dbmod.res_img_link;
        this.res_vod_main = dbmod.res_vod_main;
        this.mod_test_style = dbmod.mod_test_style;
        this.mod_mobile_ind = dbmod.mod_mobile_ind;
        this.mod_download_ind = dbmod.mod_download_ind;
    }

    public boolean isEffTime(Connection con) throws SQLException, qdbException {
        boolean isEffTime = false;
        Timestamp curTime = dbUtils.getTime(con);
        String sql = "select * from Module where mod_res_id = ? " +
            "and ? between mod_eff_start_datetime and mod_eff_end_datetime";
            
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1, mod_res_id);
        pstmt.setTimestamp(2, curTime);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            isEffTime = true;
        }
        pstmt.close();
        return isEffTime;
    }
    
   public String getModHeader_test(Connection con, loginProfile prof, long pasTimeLeft, boolean flag)
           throws qdbException, cwSysMessage, SQLException {
       String result;
//在上一层活动pasTimeLeft的值,如果flag为true，则需要从数据库中取得pasTimeLeft
       if(flag) {
    	   dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
    	   dbpas.pasTkhId = tkh_id;
    	   dbpas.pasResId = mod_res_id;
    	   try {
    		   dbpas.get(con);
    	   } catch (SQLException e) {
    		   throw new qdbException(e.getLocalizedMessage());
    	   }
    	   pasTimeLeft = dbpas.pasTimeLeft;
       }
       
       //如果考试有结束时间时，会计算出当前时间和结束时间所相差秒差，与考试的限制时间和保存时间做比较，以少的为准
       Timestamp curDate = cwSQL.getTime(con);
       long end_time = (mod_eff_end_datetime.getTime() - curDate.getTime())/1000;
       if(end_time <= 0){
    	  // throw new cwSysMessage("PGR013");
       }
       if(end_time < res_duration * 60 && (pasTimeLeft == 0 || (pasTimeLeft > 0 && end_time < pasTimeLeft))){
    	   pasTimeLeft = end_time;
       }

       result = "<header " ;   
       result += " mod_id=\"" + mod_res_id;
       result += "\" duration=\"" + res_duration;
       result += "\" time_limit=\"" + res_duration;
       result += "\" time_left=\"" + pasTimeLeft;
       result +=  "\" pass_score=\"" + mod_pass_score;
       if(mod_show_answer_ind != -1) {
           result += "\" show_answer_ind=\"" + mod_show_answer_ind;
       }
       result += "\" show_save_and_suspend_ind=\"" + mod_show_save_and_suspend_ind;
       result += "\" mod_show_answer_after_passed_ind=\"" + mod_show_answer_after_passed_ind;
       result += "\" managed_ind=\"" + mod_managed_ind;
       result += "\" subtype=\"" + res_subtype + "\">";
      
       result += "<title>" + dbUtils.esc4XML(res_title) + "</title>"
               + dbUtils.NEWL;
   
       result += "</header>" + dbUtils.NEWL;

       return result;
   }
    
    public String getDynamicQueAsXML_test(Connection con, String usrId,
           dbModuleSpec dbmsp, String[] robs, String uploadDir, Vector resIdVec, boolean boolPreviewAssessment, boolean isShuffleMCQue, long tkh_id, ExportController controller)
           throws qdbException, qdbErrMessage, cwSysMessage, SQLException {
       String qList = "";
       Vector qArray = new Vector();

       if (res_subtype.equalsIgnoreCase(MOD_TYPE_DXT)) {
           qArray = dbModuleSpec
                   .genDynQue(con, this, res_usr_id_owner, usrId);
       } else if (res_subtype.equalsIgnoreCase(MOD_TYPE_STX)) {
           qArray = dbModuleSpec.genSTXDynQue(con, this, dbmsp, robs,
                   res_usr_id_owner, usrId);
       }
       if (qArray.size() == 0) {
           // if no question is drawn,
           // throw system message and do not launch the test player
    	   CommonLog.info("Empty question for Dynamic Test");
           throw new qdbErrMessage("MOD008");
       }
       mod_size = qArray.size();
        qList += dbQuestion.getQueAsXML_test(con, uploadDir, qArray,  resIdVec,mod_type, isShuffleMCQue, tkh_id, mod_res_id, controller);
       return qList;

   }
    
    public String getUserReport_test(Connection con, String usrId, String[] que_id_lst, long attempt_nbr, loginProfile prof, String metaXML, qdbEnv static_env)
    throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException
       {
        String result ;
        dbProgress pgr = new dbProgress();
        pgr.pgr_usr_id = usrId;
        pgr.pgr_res_id = mod_res_id;
        pgr.pgr_tkh_id = tkh_id;
        if (attempt_nbr == 0 )
            pgr.get(con);
        else
            pgr.get(con,attempt_nbr);

        result = pgr.rptUserAsXML_test(con, que_id_lst, prof, metaXML, static_env);
       
        return result;
       
       }
    
    public String genTestAsXML_test(Connection con, loginProfile prof, dbModuleSpec dbmsp, String[] robs, 
    		float time_limit, String uploadDir, Vector resIdVec, long tkh_id, boolean boolPreviewAssessment, 
    		boolean isShuffleMCQue, ExportController controller, HashMap memory, HttpSession sess ,int level)
    throws qdbException, qdbErrMessage, cwSysMessage, SQLException, cwException
     {	
    	    Hashtable tests_memory = new Hashtable();
    	    Vector orderId = new Vector();
            String qList = "";
            boolean restoreQue = false; //check if the dynamic que need to be restored
			dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
			dbpas.pasTkhId = tkh_id;
			dbpas.pasResId = res_id;
			if (dbpas.chkforExist(con)) {
				restoreQue = true;
			}
			Long mod_id = new Long(this.mod_res_id);
			controller.setTotalRow(100);
			controller.currentRow = 1;
            // pass the resIdVec as reference and store all the queId in the test
            // return to qdbAction for access control
            if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT) && restoreQue) {
        		//restore the dynamic test here
                /*Vector qArray = dbResourceContent.getChildAssForDyn(con, mod_res_id, tkh_id);
                Vector qArray_id = new Vector();
                for (int i=0;i < qArray.size();i++) {
                    dbResourceContent rcn = (dbResourceContent) qArray.elementAt(i);
                    qArray_id.add(new Long(rcn.rcn_res_id_content)); 
                } 
                qList += dbQuestion.getQueAsXMLforRestoredDyn(con, null, qArray_id,  resIdVec, mod_type, 
                		isShuffleMCQue, tkh_id, mod_res_id, controller);*/
            	TestMemory tm = new TestMemory();

            	
//            	Vector vec_test_score = dbQuestion.getQueForRestore_test(con, mod_type, this.mod_res_id, tkh_id);
            	
            	Vector vec_res_id_lst = new Vector();
            	 Vector vec_test_score = dbQuestion.getQueForRestore_test(con, mod_type, this.mod_res_id, tkh_id, vec_res_id_lst);
                if(vec_res_id_lst.size() < 1){
                  //delete the information of auto-save
                    dbpas.pasTkhId = tkh_id;
                    dbpas.pasResId = mod_res_id;
                    dbpas.del(con);
                    dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
                    dbpsa.psaTkhId = tkh_id;
                    dbpsa.psaPgrResId = mod_res_id;
                    dbpsa.delAll(con);
                    dbResourceContent.DelForDxt(con, mod_res_id, tkh_id);
                    attempt_add = false;
                    level = level + 1;
                    return genTestAsXML_test( con,  prof,  dbmsp, robs, 
                             time_limit,  uploadDir,  resIdVec,  tkh_id,  boolPreviewAssessment, 
                             isShuffleMCQue,  controller,  memory,  sess, level);
                    
                }
            	tm.hs_tests_score.put(new Integer(1), vec_test_score);
            	tests_memory.put(mod_id, tm);
        	} else if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT) && 
        			mod_logic.equalsIgnoreCase(dbModule.MOD_LOGIC_ADT)) {
        		TestMemory tm = new TestMemory();
        		tm.setTest(this, prof.usr_id, uploadDir, con, controller);
        		tests_memory.put(mod_id, tm);
        		//qList = getDynamicQueAsXML_test(con, prof.usr_id, dbmsp, robs, uploadDir, resIdVec, boolPreviewAssessment, isShuffleMCQue, tkh_id, controller);
        	} else {
        		if (!memory.containsKey(mod_id)) {
        			synchronized (obj) {
        				//得到同步锁之后再判断一次，预防等待同步锁时别人已经为Hashtable赋值了
        				if (!memory.containsKey(mod_id)) {
        					memory.put(mod_id, new TestMemory());
        				}
        			}
        		}
        		TestMemory testMemory = (TestMemory)memory.get(mod_id);
        		if (testMemory.hs_tests_score.size() == 0) {
        			testMemory.beginSetTest(this, prof.usr_id, uploadDir, con,controller);
        		}
        		dbResource dbr = new dbResource();
    			dbr.res_id = this.mod_res_id;
    			Timestamp updateTimeStamp = dbr.getUpdateTimeStamp(con);
    			//判断测验的修改时间检查当前测验是否被修改过
        		if (updateTimeStamp != null && !updateTimeStamp.equals(testMemory.updateTimeStamp)) {
        			testMemory.reSetTest(this, updateTimeStamp, prof.usr_id, uploadDir, con,controller);    			
        		}
        		tests_memory.putAll(memory);       	
        		//qList = getStaticQueAsXML_test(con, resIdVec, isShuffleMCQue, tkh_id, controller);		
            }
            
            qList = getTestQueXML(tests_memory, this.mod_res_id, this.mod_type, tkh_id, uploadDir, con, 
    				restoreQue, isShuffleMCQue, resIdVec, sess, orderId, controller);
            
            Timestamp curTime = dbUtils.getTime(con);
            //把Resources中的不变的值放到缓存中
            HashMap test_res_cache = wizbCacheManager.getInstance().getCachedHashmap("TEST_RES_CACHE", true);
            int scenarioQueCount = 0;
            boolean isOnlineExam   = false;
            if(test_res_cache.get(new Long(res_id))==null){
            	Object[] res_cache_object ={new Long(0), new Boolean(false)};
            	scenarioQueCount = dbResource.countScenarioQueInList(con, resIdVec);
            	isOnlineExam = aeItem.isOnlineExam(aeItem.getItemByContentMod(con, mod_res_id));
            	res_cache_object[0] = new Long(scenarioQueCount);
            	res_cache_object[1] = new Boolean(isOnlineExam);
            	test_res_cache.put(new Long(res_id), res_cache_object);
            } else {
            	Object[] res_cache_object = (Object[])test_res_cache.get(new Long(res_id));
            	scenarioQueCount = ((Long)res_cache_object[0]).intValue();
            	isOnlineExam = ((Boolean)res_cache_object[1]).booleanValue();
            }
            String result = "";
            result += "<quiz id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date ;
            result += "\" start_time=\"" + curTime + "\" size=\"" + (resIdVec.size()-scenarioQueCount) 
            	   +  "\" total_score=\"" + (int)mod_max_score + "\">" + dbUtils.NEWL;
            result += "<key>" +  Math.round(Math.random()*99+1) + "</key>" + dbUtils.NEWL;
            
           
            result += "<isOnlineExam>" +  isOnlineExam + "</isOnlineExam>" + dbUtils.NEWL;
            

            if (this.usr_ent_id==0){
                this.usr_ent_id = prof.usr_ent_id;
            }
       dbpas.get(con);
       result += getModHeader_test(con, prof, dbpas.pasTimeLeft, false)+ dbUtils.NEWL;

       result +=  "<aicc_data";
       if(tkh_id>0){
        result +=  " tkh_id=\"" + tkh_id + "\"";
       }
       result +=  "/>";

	       	result += "<db_flag_cookie>";
			if (dbpas.pasFlag != null && !dbpas.pasFlag.equals("")) {
				int last_pos = dbpas.pasFlag.lastIndexOf("\f");
				String temp = dbpas.pasFlag.substring(1, last_pos);
				String [] flagTemp = cwUtils.splitToString(temp, "\f\f");
				StringBuffer flag_cookie = new StringBuffer();
				for (int i=0; i<flagTemp.length; i++) {
					if (i==0) {
						flag_cookie.append("\\f");
					}else {
						flag_cookie.append("\\f").append("\\f");
					}
					flag_cookie.append(flagTemp[i]);
				}
				flag_cookie.append("\\f");
				result += flag_cookie;
			} else {
				result += "blank";
			}
	       	result += "</db_flag_cookie>";
  
            result += qList + dbUtils.NEWL;
            
            result += "</quiz>"+ dbUtils.NEWL;
            //如果是动态测验
            if(mod_type.equalsIgnoreCase(MOD_TYPE_DXT) && (resIdVec.size() == 0 ||  getModuleSpec(con, this.mod_mod_res_id_parent>0? this.mod_mod_res_id_parent:this.mod_res_id) != resIdVec.size())){
            	if(level < 2){
            		level = level + 1;
	            	resIdVec.clear();
	            	//先清空之前自动保存的记录
	            	dbpas.pasTkhId = tkh_id;
	    			dbpas.pasResId = res_id;
	    			dbpas.del(con);
	    			dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
	    			dbpsa.psaTkhId = tkh_id;
	    			dbpsa.psaPgrResId = res_id;
	    			dbpsa.delAll(con);
	    			dbResourceContent.DelForDxt(con, res_id,tkh_id);
	            	result =genTestAsXML_test(con, prof, dbmsp, robs, time_limit, uploadDir, resIdVec,
	    					tkh_id, boolPreviewAssessment, isShuffleMCQue, controller, memory,sess, level);	
            	}else{
            		throw new cwSysMessage("ASM006");	
            	}
            }
            
            
            
            return result;
       
    }
    public String getStaticQueAsXML_test(Connection con, Vector resIdVec,boolean isShuffleMCQue, long tkh_id, ExportController controller)
    throws SQLException,qdbException ,cwSysMessage,qdbErrMessage,qdbErrMessage
    {
        // Get the list of question from the test;
        Vector qArray = dbResourceContent.getChildAss(con,mod_res_id);
        Vector qArray_id = new Vector();

        if (qArray.size() == 0) {
            //if no question is drawn, 
            //throw system message and do not launch the test player
        	CommonLog.info("Empty question for Standard Test");
            throw new cwSysMessage("MOD008");
        }
        for (int i=0;i < qArray.size();i++) {
            dbResourceContent rcn = (dbResourceContent) qArray.elementAt(i);
            qArray_id.add(new Long(rcn.rcn_res_id_content)); 
        }
   
        String qList = "";

        mod_size = qArray.size();
        qList += dbQuestion.getQueAsXML_test(con, null, qArray_id,  resIdVec, mod_type, isShuffleMCQue, tkh_id, mod_res_id, controller);

        return qList;
   
    }
    
    public static boolean clsStaIsOn(Connection con, Vector id)throws qdbException, cwSysMessage{
        String sql = "select res_status from resources where res_id in (";
        for (int i= 0; i< id.size(); i++){
            dbModule dbmod = ((dbModule) id.get(i));
            if(dbResource.getResStatus(con,dbmod.res_id).equalsIgnoreCase("ON")){
                return true;
            }
        }
        return false;
    }  
    
    public Vector getQue_test(Connection con, Vector qArray, Random random) throws SQLException, cwSysMessage, qdbException, cwException {
        Vector vec_test_score = new Vector();
        //存放所有题目
        Hashtable HS_Ques = new Hashtable();
        //用来存放所有题目的顺序 － 用sql语句查出来的顺序来存放
        Vector queOrder = new Vector();
        //存放选项
        Hashtable hs_int_score = new Hashtable();
        Hashtable subQues = null;
        PreparedStatement stmt = null;
        long QueId;
        long ScenarioQueId;
        
        StringBuffer temp_sql = new StringBuffer();
        if (qArray != null) {
            temp_sql.append("(");
            for(int i = 0; i < qArray.size(); i++){
                temp_sql.append(qArray.get(i).toString()).append(",");
            }
            temp_sql.append("0)");
        }

        dbQuestion dbq;
        StringBuffer sql = new StringBuffer();
        sql.append("select p_res.res_id as p_res_id , p_res.res_lan as p_res_lan, ");
        if (mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {
            sql.append(" p_rc.rcn_order as p_rcn_order, ");
        }
        sql.append("p_res.res_title as p_res_title, p_res.res_desc as p_res_desc, p_res.res_type as p_res_type, ")
           .append("p_res.res_subtype as p_res_subtype, p_res.res_annotation as p_res_annotation, ")
           .append("p_res.res_format as p_res_format, p_res.res_difficulty as p_res_difficulty, ")
           .append("p_res.res_duration as p_res_duration,p_res.res_privilege as p_res_privilege, ")
           .append("p_res.res_status as p_res_status, p_res.res_usr_id_owner as p_res_usr_id_owner, ")
           .append("p_res.res_create_date as p_res_create_date, p_res.res_tpl_name as p_res_tpl_name, ")
           .append("p_res.res_res_id_root as p_res_res_id_root, p_res.res_mod_res_id_test as p_res_mod_res_id_test, ")
           .append("p_res.res_upd_user as p_res_upd_user,p_res.res_upd_date as p_res_upd_date, ")
           .append("p_res.res_src_type as p_res_src_type, p_res.res_src_link as p_res_src_link, ")
           .append("p_res.res_instructor_name as p_res_instructor_name, ")
           .append("p_res.res_instructor_organization as p_res_instructor_organization, p_que.que_xml as p_que_xml, ")
           .append("p_que.que_score as p_que_score, p_que.que_type as p_que_type, ")
           .append("p_que.que_int_count as p_que_int_count, p_que.que_prog_lang as p_que_prog_lang, ")
           .append("p_que.que_media_ind as p_que_media_ind, p_que.que_submit_file_ind as p_que_submit_file_ind, ")
           .append("p_int.int_res_id as p_int_res_id, p_int.int_label as p_int_label, p_int.int_order as p_int_order, ")
           .append("p_int.int_xml_outcome as p_int_xml_outcome,  p_int.int_xml_explain as p_int_xml_explain, ")
           .append("p_int.int_res_id_explain as p_int_res_id_explain, p_int.int_res_id_refer as p_int_res_id_refer, ");
        sql.append("c_res.res_id as c_res_id , c_rc.rcn_order as c_rcn_order, c_res.res_lan as c_res_lan, ")
           .append("c_res.res_title as c_res_title, c_res.res_desc as c_res_desc, c_res.res_type as c_res_type, ")
           .append("c_res.res_subtype as c_res_subtype, c_res.res_annotation as c_res_annotation, ")
           .append("c_res.res_format as c_res_format, c_res.res_difficulty as c_res_difficulty, ")
           .append("c_res.res_duration as c_res_duration,c_res.res_privilege as c_res_privilege, ")
           .append("c_res.res_status as c_res_status, c_res.res_usr_id_owner as c_res_usr_id_owner, ")
           .append("c_res.res_create_date as c_res_create_date, c_res.res_tpl_name as c_res_tpl_name, ")
           .append("c_res.res_res_id_root as c_res_res_id_root, c_res.res_mod_res_id_test as c_res_mod_res_id_test, ")
           .append("c_res.res_upd_user as c_res_upd_user,c_res.res_upd_date as c_res_upd_date, ")
           .append("c_res.res_src_type as c_res_src_type, c_res.res_src_link as c_res_src_link, ")
           .append("c_res.res_instructor_name as c_res_instructor_name, c_res.res_instructor_organization as c_res_instructor_organization, ")
           .append("c_que.que_xml as c_que_xml, c_que.que_score as c_que_score, c_que.que_type as c_que_type, ")
           .append("c_que.que_int_count as c_que_int_count, c_que.que_prog_lang as c_que_prog_lang, ")
           .append("c_que.que_media_ind as c_que_media_ind, c_que.que_submit_file_ind as c_que_submit_file_ind, ")
           .append("c_int.int_res_id as c_int_res_id, c_int.int_label as c_int_label, c_int.int_order as c_int_order, ")
           .append("c_int.int_xml_outcome as c_int_xml_outcome,  c_int.int_xml_explain as c_int_xml_explain, ")
           .append("c_int.int_res_id_explain as c_int_res_id_explain, c_int.int_res_id_refer as c_int_res_id_refer, ")
           .append("qct_allow_shuffle_ind, qcs_qcount, qcs_score ")
           .append("FROM Resources p_res ");
        
        if (mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {
            sql.append("inner join ResourceContent p_rc on (p_rc.rcn_res_id = ? and p_rc.rcn_tkh_id = -1 and p_res.res_id = p_rc.rcn_res_id_content) ");
        }
           
        sql.append("inner join Question p_que on (p_res.res_id = p_que.que_res_id) ")
           .append("left join Interaction p_int on (p_res.res_id = p_int.int_res_id) ")
           .append("left join QueContainer on (qct_res_id = p_res.res_id) ")
           .append("left join QueContainerSpec on (qcs_res_id = p_res.res_id) ")
           .append("left join ResourceContent c_rc on (c_rc.rcn_res_id = p_res.res_id and (p_que.que_type = 'FSC' and c_rc.rcn_tkh_id = -1)) ")
           .append("left join Resources c_res on (c_res.res_id = c_rc.rcn_res_id_content and c_res.res_status = 'ON') ")
           .append("left join Question c_que on (c_que.que_res_id = c_res.res_id) ")
           .append("left join Interaction c_int on (c_int.int_res_id = c_res.res_id) ")
           .append("where p_res.res_status = 'ON' ");
       if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT)) {
           sql.append(" and p_res.res_id in ").append(temp_sql.toString());
       }       
       if (mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {
           sql.append("order by p_rc.rcn_order, c_rc.rcn_order ");
       }
       try {
           stmt = con.prepareStatement(sql.toString());   
           int index = 1;
           if (mod_type.equalsIgnoreCase(MOD_TYPE_TST)) {  
               stmt.setLong(index++, this.mod_res_id);
           }
           ResultSet rs = stmt.executeQuery();
           while (rs.next()) {
               QueId = rs.getLong("p_res_id");              
               Long QueId_obj = new Long(QueId);
               if(!HS_Ques.containsKey(QueId_obj)){
                   queOrder.add(QueId_obj);
                   dbq = new dbQuestion();
                   HS_Ques.put(QueId_obj, dbq);
                   dbq.res_id = QueId;
                   dbq.que_res_id = QueId;
                   dbq.res_lan = rs.getString("p_res_lan");
                   dbq.res_title = rs.getString("p_res_title");
                   dbq.res_desc = rs.getString("p_res_desc");
                   dbq.res_type = rs.getString("p_res_type");
                   dbq.res_subtype = rs.getString("p_res_subtype");
                   dbq.res_annotation = rs.getString("p_res_annotation");
                   dbq.res_format = rs.getString("p_res_format");
                   dbq.res_difficulty = rs.getInt("p_res_difficulty");
                   dbq.res_duration = rs.getFloat("p_res_duration");
                   dbq.res_privilege = rs.getString("p_res_privilege");
                   dbq.res_status = rs.getString("p_res_status");
                   dbq.res_usr_id_owner = rs.getString("p_res_usr_id_owner");
                   dbq.res_create_date = rs.getTimestamp("p_res_create_date");
                   dbq.res_tpl_name = rs.getString("p_res_tpl_name");
                   dbq.res_res_id_root = rs.getLong("p_res_res_id_root");
                   dbq.res_mod_res_id_test = this.mod_res_id;
                   dbq.res_upd_user = rs.getString("p_res_upd_user");
                   dbq.res_upd_date = rs.getTimestamp("p_res_upd_date");
                   dbq.res_src_type = rs.getString("p_res_src_type");
                   dbq.res_src_link = rs.getString("p_res_src_link");
                   dbq.res_instructor_name = rs.getString("p_res_instructor_name");
                   dbq.res_instructor_organization = rs.getString("p_res_instructor_organization");
                   dbq.que_xml = cwSQL.getClobValue(rs, "p_que_xml");
                   dbq.que_score = rs.getInt("p_que_score");
                   dbq.que_type = rs.getString("p_que_type");
                   dbq.que_int_count = rs.getInt("p_que_int_count");
                   dbq.que_prog_lang = rs.getString("p_que_prog_lang");
                   dbq.que_media_ind = rs.getBoolean("p_que_media_ind");
                   dbq.que_submit_file_ind = rs.getBoolean("p_que_submit_file_ind");
                   dbq.qct_allow_shuffle_ind = rs.getInt("qct_allow_shuffle_ind");
                   dbq.qcs_qcount = rs.getInt("qcs_qcount");
                   dbq.qcs_score = rs.getInt("qcs_score");
               }else{
                   dbq = (dbQuestion)HS_Ques.get(QueId_obj);
               }
               if( !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) && !dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC) ) {
                   dbInteraction intObj = new dbInteraction();
                   intObj.int_res_id = rs.getLong("p_int_res_id");
                   intObj.int_label = rs.getString("p_int_label");
                   intObj.int_order = rs.getInt("p_int_order");
                   intObj.int_xml_outcome = cwSQL.getClobValue(rs, "p_int_xml_outcome");
                   intObj.int_xml_explain = cwSQL.getClobValue(rs, "p_int_xml_explain");
                   intObj.int_res_id_explain = rs.getLong("p_int_res_id_explain");
                   intObj.int_res_id_refer = rs.getLong("p_int_res_id_refer");
                   dbq.ints.add(intObj);
                   setIntScore(intObj, hs_int_score);
               }
               
               ScenarioQueId = rs.getLong("c_res_id");  
               Long scenQueId_obj = new Long(ScenarioQueId);
               if(dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC) || 
                       dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                   if (dbq.sub_que_vec.size() > 0) {
                       subQues = (Hashtable)dbq.sub_que_vec.get(0);   
                   } else {
                       subQues = new Hashtable();
                       Vector subQueOrder = new Vector();
                       //题目顺序
                       subQues.put("QUE_ORDER", subQueOrder);
                       dbq.sub_que_vec.add(subQues);
                   }
                   if (dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)) {
                       dbQuestion subQbq = null;
                       if (ScenarioQueId == 0) {
                           throw new cwSysMessage("MSP002");
                       }else {
                           if(!subQues.containsKey(scenQueId_obj)){
                               ((Vector)subQues.get("QUE_ORDER")).add(scenQueId_obj);
                               subQbq = new dbQuestion();
                               subQues.put(scenQueId_obj, subQbq);
                               subQbq.res_id = ScenarioQueId;
                               subQbq.que_res_id = ScenarioQueId;
                               subQbq.res_lan = rs.getString("c_res_lan");
                               subQbq.res_title = rs.getString("c_res_title");
                               subQbq.res_desc = rs.getString("c_res_desc");
                               subQbq.res_type = rs.getString("c_res_type");
                               subQbq.res_subtype = rs.getString("c_res_subtype");
                               subQbq.res_annotation = rs.getString("c_res_annotation");
                               subQbq.res_format = rs.getString("c_res_format");
                               subQbq.res_difficulty = rs.getInt("c_res_difficulty");
                               subQbq.res_duration = rs.getFloat("c_res_duration");
                               subQbq.res_privilege = rs.getString("c_res_privilege");
                               subQbq.res_status = rs.getString("c_res_status");
                               subQbq.res_usr_id_owner = rs.getString("c_res_usr_id_owner");
                               subQbq.res_create_date = rs.getTimestamp("c_res_create_date");
                               subQbq.res_tpl_name = rs.getString("c_res_tpl_name");
                               subQbq.res_res_id_root = rs.getLong("c_res_res_id_root");
                               subQbq.res_mod_res_id_test = this.mod_res_id;
                               subQbq.res_upd_user = rs.getString("c_res_upd_user");
                               subQbq.res_upd_date = rs.getTimestamp("c_res_upd_date");
                               subQbq.res_src_type = rs.getString("c_res_src_type");
                               subQbq.res_src_link = rs.getString("c_res_src_link");
                               subQbq.res_instructor_name = rs.getString("c_res_instructor_name");
                               subQbq.res_instructor_organization = rs.getString("c_res_instructor_organization");
                               subQbq.que_xml = cwSQL.getClobValue(rs, "c_que_xml");
                               subQbq.que_score = rs.getInt("c_que_score");
                               subQbq.que_type = rs.getString("c_que_type");
                               subQbq.que_int_count = rs.getInt("c_que_int_count");
                               subQbq.que_prog_lang = rs.getString("c_que_prog_lang");
                               subQbq.que_media_ind = rs.getBoolean("c_que_media_ind");
                               subQbq.que_submit_file_ind = rs.getBoolean("c_que_submit_file_ind");
                               subQbq.qct_allow_shuffle_ind = rs.getInt("qct_allow_shuffle_ind");
                               subQbq.qcs_qcount = rs.getInt("qcs_qcount");
                               subQbq.qcs_score = rs.getInt("qcs_score");
                           } else {
                               subQbq = (dbQuestion)subQues.get(scenQueId_obj);
                           }         
                           dbInteraction subIntObj = new dbInteraction();
                           subIntObj.int_res_id = rs.getLong("c_int_res_id");
                           subIntObj.int_label = rs.getString("c_int_label");
                           subIntObj.int_order = rs.getInt("c_int_order");
                           subIntObj.int_xml_outcome = cwSQL.getClobValue(rs, "c_int_xml_outcome");
                           subIntObj.int_xml_explain = cwSQL.getClobValue(rs, "c_int_xml_explain");
                           subIntObj.int_res_id_explain = rs.getLong("c_int_res_id_explain");
                           subIntObj.int_res_id_refer = rs.getLong("c_int_res_id_refer");
                           subQbq.ints.add(subIntObj);
                           setIntScore(subIntObj, hs_int_score);
                       }
                   } else {
                       getDynScenQue(con, QueId, rs.getInt("qcs_score"), rs.getInt("qcs_qcount"), hs_int_score, subQues);
                   }
               }  
                 if (dbq != null) {
                    qdbAction.Ques_memory.put(new Long(dbq.res_id), dbq);
                    if (dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)|| dbq.que_type.equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC)) {
                        if (dbq.sub_que_vec.get(0) != null&& ((Hashtable) dbq.sub_que_vec.get(0)).size() > 0) {
                            Hashtable subQues_temp = (Hashtable) dbq.sub_que_vec.get(0);
                            Enumeration ids = subQues_temp.keys();
                            while (ids.hasMoreElements()) {
                                Object key = ids.nextElement();
                                if(key == null || key.toString().equals("QUE_ORDER")){
                                }else{
                                    dbQuestion sub_q = (dbQuestion) subQues_temp.get(key);
                                    qdbAction.Ques_memory.put(new Long(sub_q.res_id), sub_q);
                                }
                                
                            }
                        }
                    }
                }
            }
       } catch(SQLException e) {
           throw new qdbException("SQL Error: " + e.getMessage());
       } finally {
           if(stmt != null) {
               stmt.close();
           }
       }
       
       if (mod_type.equalsIgnoreCase(MOD_TYPE_DXT)) {
           //打乱题目顺序
           queOrder = cwUtils.randomDrawFromVec(queOrder, queOrder.size(), random);
       }
       
       //题目顺序
       HS_Ques.put("QUE_ORDER", queOrder);
       vec_test_score.add(HS_Ques);
       vec_test_score.add(hs_int_score);
       return vec_test_score;
    }
    
    public void getDynScenQue(Connection con, long res_id, int qcs_score, int qcs_qcount, Hashtable hs_int_score, Hashtable subQues) 
    	throws SQLException, qdbException, cwSysMessage {
    	Vector queOrder = new Vector();
    	List subScenQue = new ArrayList();
    	//抽题数
    	subScenQue.add(new Integer(qcs_qcount));
    	//符合抽题条件的所有题目id
    	subScenQue.add(queOrder);
    	
    	PreparedStatement stmt = null;
    	long QueId;
    	StringBuffer sql = new StringBuffer();
    	sql.append("select res_id , res_lan, res_title, res_desc, res_type, res_subtype , res_annotation, ")
    	   .append("res_format, res_difficulty, res_duration, res_privilege, res_status, res_usr_id_owner, ")
    	   .append("res_create_date, res_tpl_name, res_res_id_root, res_mod_res_id_test, res_upd_user, ")
    	   .append("res_upd_date, res_src_type, res_src_link, res_instructor_name, res_instructor_organization, ") 
    	   .append("que_xml, que_score, que_type, que_int_count, que_prog_lang, que_media_ind, que_submit_file_ind, ")
    	   .append("int_res_id, int_label, int_order, int_xml_outcome, int_xml_explain, int_res_id_explain, int_res_id_refer ");
    	sql.append("FROM ResourceContent, Resources left join Interaction on (res_id = int_res_id), Question ");
    	sql.append("WHERE rcn_res_id = ? and rcn_res_id_content = res_id and res_status = ? ")
    	   .append("and res_id = que_res_id and que_score = ? and rcn_tkh_id = ?");
    	try {
	    	stmt = con.prepareStatement(sql.toString());
	 	    int index = 1;
	 	    stmt.setLong(index++, res_id);
	 	    stmt.setString(index++, "ON");
	 	    stmt.setInt(index++, qcs_score);
	 	    stmt.setInt(index++, -1);
	 	    ResultSet rs = stmt.executeQuery();
	 	    
	 	    int count = 0;
	 	    dbQuestion subQbq = null;
	 	    while (rs.next()) {
	 	    	QueId = rs.getLong("res_id");				
			    Long QueId_obj = new Long(QueId);
			    if(!subQues.containsKey(QueId_obj)){
			    	count++;
			    	queOrder.add(QueId_obj);
				    subQbq = new dbQuestion();
				    subQues.put(QueId_obj, subQbq);
					subQbq.res_id = QueId;
					subQbq.que_res_id = QueId;
					subQbq.res_lan = rs.getString("res_lan");
					subQbq.res_title = rs.getString("res_title");
					subQbq.res_desc = rs.getString("res_desc");
					subQbq.res_type = rs.getString("res_type");
					subQbq.res_subtype = rs.getString("res_subtype");
					subQbq.res_annotation = rs.getString("res_annotation");
					subQbq.res_format = rs.getString("res_format");
					subQbq.res_difficulty = rs.getInt("res_difficulty");
					subQbq.res_duration = rs.getFloat("res_duration");
					subQbq.res_privilege = rs.getString("res_privilege");
					subQbq.res_status = rs.getString("res_status");
					subQbq.res_usr_id_owner = rs.getString("res_usr_id_owner");
					subQbq.res_create_date = rs.getTimestamp("res_create_date");
					subQbq.res_tpl_name = rs.getString("res_tpl_name");
					subQbq.res_res_id_root = rs.getLong("res_res_id_root");
					subQbq.res_mod_res_id_test = this.mod_res_id;
					subQbq.res_upd_user = rs.getString("res_upd_user");
					subQbq.res_upd_date = rs.getTimestamp("res_upd_date");
					subQbq.res_src_type = rs.getString("res_src_type");
					subQbq.res_src_link = rs.getString("res_src_link");
					subQbq.res_instructor_name = rs.getString("res_instructor_name");
					subQbq.res_instructor_organization = rs.getString("res_instructor_organization");
					subQbq.que_xml = cwSQL.getClobValue(rs, "que_xml");
					subQbq.que_score = rs.getInt("que_score");
					subQbq.que_type = rs.getString("que_type");
					subQbq.que_int_count = rs.getInt("que_int_count");
					subQbq.que_prog_lang = rs.getString("que_prog_lang");
					subQbq.que_media_ind = rs.getBoolean("que_media_ind");
					subQbq.que_submit_file_ind = rs.getBoolean("que_submit_file_ind");
			    } else {
			    	subQbq = (dbQuestion)subQues.get(QueId_obj);
			    }
			    
	            dbInteraction subIntObj = new dbInteraction();
	            subIntObj.int_res_id = rs.getLong("int_res_id");
	            subIntObj.int_label = rs.getString("int_label");
	            subIntObj.int_order = rs.getInt("int_order");
	            subIntObj.int_xml_outcome = cwSQL.getClobValue(rs, "int_xml_outcome");
	            subIntObj.int_xml_explain = cwSQL.getClobValue(rs, "int_xml_explain");
	            subIntObj.int_res_id_explain = rs.getLong("int_res_id_explain");
	            subIntObj.int_res_id_refer = rs.getLong("int_res_id_refer");
	            subQbq.ints.add(subIntObj);
	            setIntScore(subIntObj, hs_int_score);
	 	    }
	 	    if (count < qcs_qcount) {
	 	    	throw new cwSysMessage("MSP002");
	 	    }
    	} finally {
            if(stmt != null) {
         	   stmt.close();
            }
        }
    	
    	((Vector)subQues.get("QUE_ORDER")).add(subScenQue);
    }
    
    public static void setIntScore(dbInteraction intObj, Hashtable hs_int_score) throws qdbException {
    	Long int_id_obj = new Long(intObj.int_res_id);
    	Hashtable temp = new Hashtable();
    	if (!hs_int_score.containsKey(int_id_obj)) {
    		hs_int_score.put(int_id_obj + "_" + intObj.int_order, temp);
    	}
    	//获取int_type
    	String int_type = intObj.getIntType();
    	temp.put("int_type", int_type);
    	//获取mc_logic
        String mc_logic = dbUtils.perl.substitute("s#.*logic\\s*=\\s*\"(\\w+)\"\\s*.*>#$1#i", intObj.int_xml_outcome);
        temp.put("mc_logic", mc_logic);
    	//获取int_score
    	int int_score  = 0;
    	try {
            int_score =
              Integer.parseInt(dbUtils.perl.substitute("s#<outcome\\s*order\\s*=\\s*\"([0-9]+)\"\\s*type\\s*=\\s*\"([A-Z]+)\"\\s*score\\s*=\\s*\"([0-9]+)\".*>#$3#i", intObj.int_xml_outcome));
        } catch (NumberFormatException e) {
            int_score = 0;
        }
        temp.put("int_score", new Integer(int_score));
        //获取feedback
        Vector feedback = intObj.getFeedback();
        temp.put("feedback", feedback);
    }
    
    public String getTestQueXML(Hashtable tests_memory, long mod_res_id, String mod_type, long tkh_id, 
    		String uploadDir, Connection con, boolean restoreQue, boolean isShuffleMCQue, Vector resIdVec, 
    		HttpSession sess, Vector orderId, ExportController controller) 
    	throws qdbException, cwSysMessage, SQLException, cwException 
    {
    	Vector vec_test_score = null;
    	TestMemory testMemory = (TestMemory)tests_memory.get(new Long(mod_res_id));
    	if (testMemory.hs_tests_score.size() > 1) {
    		//在所有的动态测验题里随机抽取1份题
        	int randomId = new Random().nextInt(testMemory.hs_tests_score.size() - 1) + 1;
        	vec_test_score = (Vector)testMemory.hs_tests_score.get(new Integer(randomId));
    	} else {
    		//静态测验 | 适应性的动态测验 | 还原测验
    		vec_test_score = (Vector)testMemory.hs_tests_score.get(new Integer(1));
    	}
    	if(sess!= null){
    	    sess.setAttribute("test_data", vec_test_score);
    	}
    	
    	Hashtable HS_Ques = (Hashtable)vec_test_score.get(0);
    	//拿出所有的id
    	Vector testId = (Vector)HS_Ques.get("QUE_ORDER");
    	
    	String queXml = dbQuestion.getQueXML_test(HS_Ques, testId, mod_res_id, mod_type, tkh_id, uploadDir, con, 
    			restoreQue, isShuffleMCQue, resIdVec, false, controller);	
    	return queXml;
    }
    /**
     * 设置论坛为学习小组论坛
     * @param con
     * @param mod_res_id
     * @throws SQLException
     */
    public void setSgpDisMod(Connection con,long mod_res_id) throws SQLException{
    	String sql="update Module set mod_sgp_ind=? where mod_res_id=?";
    	PreparedStatement stmt = null; 
	    stmt = con.prepareStatement(sql);
	    stmt.setBoolean(1, true);
	    stmt.setLong(2, mod_res_id);
	    stmt.executeUpdate();
	    if(stmt !=null)stmt.close();
    }
    public boolean isModrelationCom(Connection con,long res_id)throws SQLException{
    	String sql="select ccr_id from CourseCriteria, CourseModuleCriteria" +
    			" where ccr_id = cmr_ccr_id" +
    			" and cmr_res_id=?" +
    			" and cmr_del_timestamp is null";
    	boolean result= false;
    	PreparedStatement stmt = null; 
	    stmt = con.prepareStatement(sql);
	    stmt.setLong(1, res_id);
	    ResultSet rs = stmt.executeQuery();
	    if(rs.next()){
	    	result= true;
	    }
	    cwSQL.cleanUp(rs, stmt);
	    return result;
    }

    private static final String sql_get_mod_started_status = "select mod_started_ind from module where mod_res_id = ?";
    public boolean isTstStarted(Connection con) throws SQLException {
        boolean result = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql_get_mod_started_status);
            stmt.setLong(1, mod_res_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1) == 1 ? true : false;
            }
        } finally {
            if (stmt != null) stmt.close();
        }
        return result;
    }
    
    private static final String sql_upd_mod_started_status = "update module set mod_started_ind = ? where mod_res_id = ?";
    /**
     * 
     * @param con
     * @param status 1 for started, 0 for stopped.
     * @throws IOException
     * @throws SQLException
     * @throws cwSysMessage
     */
    public void setTstStartedStatus(Connection con, int status) throws SQLException, cwSysMessage {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql_upd_mod_started_status);
            stmt.setInt(1, status);
            stmt.setLong(2, mod_res_id);
            if (stmt.executeUpdate() != 1) {
                throw new cwSysMessage("GEN000");
            }
        } finally {
            if (stmt != null) stmt.close();
        }
    }
    
    public String getCrmModType(Connection con, long cmr_id) throws SQLException {
        PreparedStatement stmt = null;
        String mod_type = "";
        try {
           
            stmt = con.prepareStatement("select mod_type from module,coursemodulecriteria where mod_res_id = cmr_res_id and cmr_id = ?");
            stmt.setLong(1, cmr_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mod_type = rs.getString("mod_type");
            }
        } finally {
            if (stmt != null) stmt.close();
        }
        return mod_type;
        
    }
    
    public String getTextStyle(Connection con ,int mod_res_id) throws SQLException{
		String sql="  select mod_test_style from module where mod_res_id = ?  ";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		ptmt = con.prepareStatement(sql);
		ptmt.setInt(1, mod_res_id);
		rs = ptmt.executeQuery();
		if(rs.next()){
			return rs.getString("mod_test_style");
		}
		return "many";
    }
    
    public void updateMobileInd(Connection con) throws qdbException{
            try {
                String SQL = "update Module set mod_mobile_ind = ?, mod_test_style= ? "
                    + "where mod_res_id = ? ";
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setInt(1, mod_mobile_ind);
                stmt.setString(2, mod_test_style);
                stmt.setLong(3,mod_res_id);
                int stmtResult=stmt.executeUpdate();
                stmt.close();
                if ( stmtResult!=1)
                {
                    con.rollback();
                    throw new qdbException("Failed to update status.");
                }
            }
            catch(SQLException e) {
                throw new qdbException(e.getMessage());
            }
        }
    
    public long getModuleSpec(Connection con, long res_id) throws qdbException, SQLException {
		long msp_qcount = 0;
		PreparedStatement stmt = con.prepareStatement(" SELECT msp_score, msp_qcount from ModuleSpec where msp_res_id = ?");
		stmt.setLong(1, res_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			msp_qcount += rs.getLong("msp_qcount");

		} 
		stmt.close();
		return msp_qcount;
	}



	public int getMod_mod_id_root() {
		return mod_mod_id_root;
	}
	public void setMod_mod_id_root(int mod_mod_id_root) {
		this.mod_mod_id_root = mod_mod_id_root;
	}
	public long getMod_res_id() {
		return mod_res_id;
	}
	public void setMod_res_id(long mod_res_id) {
		this.mod_res_id = mod_res_id;
	}
	public String getMod_type() {
		return mod_type;
	}
	public void setMod_type(String mod_type) {
		this.mod_type = mod_type;
	}
	public float getMod_max_score() {
		return mod_max_score;
	}
	public void setMod_max_score(float mod_max_score) {
		this.mod_max_score = mod_max_score;
	}
	public float getMod_pass_score() {
		return mod_pass_score;
	}
	public void setMod_pass_score(float mod_pass_score) {
		this.mod_pass_score = mod_pass_score;
	}
	public String getMod_instruct() {
		return mod_instruct;
	}
	public void setMod_instruct(String mod_instruct) {
		this.mod_instruct = mod_instruct;
	}
	public long getMod_max_attempt() {
		return mod_max_attempt;
	}
	public void setMod_max_attempt(long mod_max_attempt) {
		this.mod_max_attempt = mod_max_attempt;
	}
	public long getMod_max_usr_attempt() {
		return mod_max_usr_attempt;
	}
	public void setMod_max_usr_attempt(long mod_max_usr_attempt) {
		this.mod_max_usr_attempt = mod_max_usr_attempt;
	}
	public boolean isMod_score_ind() {
		return mod_score_ind;
	}
	public void setMod_score_ind(boolean mod_score_ind) {
		this.mod_score_ind = mod_score_ind;
	}
	public long getMod_score_reset() {
		return mod_score_reset;
	}
	public void setMod_score_reset(long mod_score_reset) {
		this.mod_score_reset = mod_score_reset;
	}
	public String getMod_logic() {
		return mod_logic;
	}
	public void setMod_logic(String mod_logic) {
		this.mod_logic = mod_logic;
	}
	public Timestamp getMod_eff_start_datetime() {
		return mod_eff_start_datetime;
	}
	public void setMod_eff_start_datetime(Timestamp mod_eff_start_datetime) {
		this.mod_eff_start_datetime = mod_eff_start_datetime;
	}
	public Timestamp getMod_eff_end_datetime() {
		return mod_eff_end_datetime;
	}
	public void setMod_eff_end_datetime(Timestamp mod_eff_end_datetime) {
		this.mod_eff_end_datetime = mod_eff_end_datetime;
	}
	public Timestamp getMod_in_eff_start_datetime() {
		return mod_in_eff_start_datetime;
	}
	public void setMod_in_eff_start_datetime(Timestamp mod_in_eff_start_datetime) {
		this.mod_in_eff_start_datetime = mod_in_eff_start_datetime;
	}
	public Timestamp getMod_in_eff_end_datetime() {
		return mod_in_eff_end_datetime;
	}
	public void setMod_in_eff_end_datetime(Timestamp mod_in_eff_end_datetime) {
		this.mod_in_eff_end_datetime = mod_in_eff_end_datetime;
	}
	public String getMod_usr_id_instructor() {
		return mod_usr_id_instructor;
	}
	public void setMod_usr_id_instructor(String mod_usr_id_instructor) {
		this.mod_usr_id_instructor = mod_usr_id_instructor;
	}
	public boolean isMod_has_rate_q() {
		return mod_has_rate_q;
	}
	public void setMod_has_rate_q(boolean mod_has_rate_q) {
		this.mod_has_rate_q = mod_has_rate_q;
	}
	public boolean isMod_is_public() {
		return mod_is_public;
	}
	public void setMod_is_public(boolean mod_is_public) {
		this.mod_is_public = mod_is_public;
	}
	public boolean isMod_public_need_enrol() {
		return mod_public_need_enrol;
	}
	public void setMod_public_need_enrol(boolean mod_public_need_enrol) {
		this.mod_public_need_enrol = mod_public_need_enrol;
	}
	public int getMod_show_answer_ind() {
		return mod_show_answer_ind;
	}
	public void setMod_show_answer_ind(int mod_show_answer_ind) {
		this.mod_show_answer_ind = mod_show_answer_ind;
	}
	public int getMod_sub_after_passed_ind() {
		return mod_sub_after_passed_ind;
	}
	public void setMod_sub_after_passed_ind(int mod_sub_after_passed_ind) {
		this.mod_sub_after_passed_ind = mod_sub_after_passed_ind;
	}
	public int getMod_show_save_and_suspend_ind() {
		return mod_show_save_and_suspend_ind;
	}
	public void setMod_show_save_and_suspend_ind(int mod_show_save_and_suspend_ind) {
		this.mod_show_save_and_suspend_ind = mod_show_save_and_suspend_ind;
	}
	public long getMod_mod_res_id_parent() {
		return mod_mod_res_id_parent;
	}
	public void setMod_mod_res_id_parent(long mod_mod_res_id_parent) {
		this.mod_mod_res_id_parent = mod_mod_res_id_parent;
	}
	public long getMod_show_answer_after_passed_ind() {
		return mod_show_answer_after_passed_ind;
	}
	public void setMod_show_answer_after_passed_ind(
			long mod_show_answer_after_passed_ind) {
		this.mod_show_answer_after_passed_ind = mod_show_answer_after_passed_ind;
	}
	public String getMod_tshost() {
		return mod_tshost;
	}
	public void setMod_tshost(String mod_tshost) {
		this.mod_tshost = mod_tshost;
	}
	public int getMod_tsport() {
		return mod_tsport;
	}
	public void setMod_tsport(int mod_tsport) {
		this.mod_tsport = mod_tsport;
	}
	public int getMod_wwwport() {
		return mod_wwwport;
	}
	public void setMod_wwwport(int mod_wwwport) {
		this.mod_wwwport = mod_wwwport;
	}
	public int getMod_managed_ind() {
		return mod_managed_ind;
	}
	public void setMod_managed_ind(int mod_managed_ind) {
		this.mod_managed_ind = mod_managed_ind;
	}
	public int getMod_started_ind() {
		return mod_started_ind;
	}
	public void setMod_started_ind(int mod_started_ind) {
		this.mod_started_ind = mod_started_ind;
	}
	public String getMod_test_style() {
		return mod_test_style;
	}
	public void setMod_test_style(String mod_test_style) {
		this.mod_test_style = mod_test_style;
	}
	public String getMod_web_launch() {
		return mod_web_launch;
	}
	public void setMod_web_launch(String mod_web_launch) {
		this.mod_web_launch = mod_web_launch;
	}
	public String getMod_core_vendor() {
		return mod_core_vendor;
	}
	public void setMod_core_vendor(String mod_core_vendor) {
		this.mod_core_vendor = mod_core_vendor;
	}
	public String getMod_password() {
		return mod_password;
	}
	public void setMod_password(String mod_password) {
		this.mod_password = mod_password;
	}
	public String getMod_time_limit_action() {
		return mod_time_limit_action;
	}
	public void setMod_time_limit_action(String mod_time_limit_action) {
		this.mod_time_limit_action = mod_time_limit_action;
	}
	public String getMod_vendor() {
		return mod_vendor;
	}
	public void setMod_vendor(String mod_vendor) {
		this.mod_vendor = mod_vendor;
	}
	public String getMod_aicc_version() {
		return mod_aicc_version;
	}
	public void setMod_aicc_version(String mod_aicc_version) {
		this.mod_aicc_version = mod_aicc_version;
	}
	public String getMod_import_xml() {
		return mod_import_xml;
	}
	public void setMod_import_xml(String mod_import_xml) {
		this.mod_import_xml = mod_import_xml;
	}
	public long getMod_size() {
		return mod_size;
	}
	public void setMod_size(long mod_size) {
		this.mod_size = mod_size;
	}
	public long[] getMod_instructor_ent_id_lst() {
		return mod_instructor_ent_id_lst;
	}
	public void setMod_instructor_ent_id_lst(long[] mod_instructor_ent_id_lst) {
		this.mod_instructor_ent_id_lst = mod_instructor_ent_id_lst;
	}
	public long getMod_tcr_id() {
		return mod_tcr_id;
	}
	public void setMod_tcr_id(long mod_tcr_id) {
		this.mod_tcr_id = mod_tcr_id;
	}
	public int getMod_required_time() {
		return mod_required_time;
	}
	public void setMod_required_time(int mod_required_time) {
		this.mod_required_time = mod_required_time;
	}
	public int getMod_download_ind() {
		return mod_download_ind;
	}
	public void setMod_download_ind(int mod_download_ind) {
		this.mod_download_ind = mod_download_ind;
	}
	public int getMod_mobile_ind() {
		return mod_mobile_ind;
	}
	public void setMod_mobile_ind(int mod_mobile_ind) {
		this.mod_mobile_ind = mod_mobile_ind;
	}
	public boolean isMod_intranet_ip_limit_ind() {
		return mod_intranet_ip_limit_ind;
	}
	public void setMod_intranet_ip_limit_ind(boolean mod_intranet_ip_limit_ind) {
		this.mod_intranet_ip_limit_ind = mod_intranet_ip_limit_ind;
	}
	public String getMod_intranet_start_ip() {
		return mod_intranet_start_ip;
	}
	public void setMod_intranet_start_ip(String mod_intranet_start_ip) {
		this.mod_intranet_start_ip = mod_intranet_start_ip;
	}
	public String getMod_intranet_end_ip() {
		return mod_intranet_end_ip;
	}
	public void setMod_intranet_end_ip(String mod_intranet_end_ip) {
		this.mod_intranet_end_ip = mod_intranet_end_ip;
	}
	public boolean isMod_multi_question_ind() {
		return mod_multi_question_ind;
	}
	public void setMod_multi_question_ind(boolean mod_multi_question_ind) {
		this.mod_multi_question_ind = mod_multi_question_ind;
	}
    
  }
