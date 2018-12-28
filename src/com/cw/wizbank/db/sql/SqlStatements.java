package com.cw.wizbank.db.sql;
// added for oracle migration!
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.report.Report;
import com.cw.wizbank.util.cwSQL;

public class SqlStatements {
	/**
	Database Vendor : Microsft SQL Server
	*/
	private static final String DBVENDOR_MSSQL    = "MSSQL";
	/**
	Database Vendor : Oracle
	*/
	private static final String DBVENDOR_ORACLE   = "ORACLE";
	/**
	Database Vendor : DB2
	*/
	private static final String DBVENDOR_DB2      = "DB2";

	/**
	JDBC product name : oracle
	*/
	private static final String ProductName_ORACLE = "oracle";
	/**
	JDBC product name : microsoft sql server
	*/
	private static final String ProductName_MSSQL = "microsoft sql server";
	/**
	JDBC product name : db2
	*/
	private static final String ProductName_DB2 = "db2";

    // for DbCtReference
    public static String sql_reference_upd = "UPDATE ctReference SET ref_title = ?, ref_url = ?, ref_update_usr_id = ?, ref_update_timestamp = ? where ref_id = ?";
    public static String sql_reference_del = "DELETE from ctReference where ref_id = ?";
    public static String sql_reference_get = "SELECT "
                                            + " ref_res_id,"
                                            + " ref_type,"
                                            + " ref_title,"
                                            + " ref_description,"
                                            + " ref_url,"
                                            + " ref_create_usr_id,"
                                            + " ref_create_timestamp,"
                                            + " ref_update_usr_id,"
                                            + " ref_update_timestamp"
                                            + " from ctReference"
                                            + " where ref_id = ?";
    
    public static String sql_reference_get_mod_ref_list = "SELECT ref_id from ctReference where ref_res_id = ?";

    // define the SQLs here
    public static final String sql_get_unit_ = "SELECT itm_title, itm_ext1 FROM aeItem WHERE itm_id = ? AND itm_status = ? AND itm_type = ? ";

    // for dbUserGrade
    public static final String sql_ins_usergrade =
		"INSERT INTO UserGrade (ugr_ent_id, ugr_display_bil, ugr_ent_id_root, ugr_type, ugr_seq_no, ugr_default_ind, ugr_tcr_id, ugr_code) values (?, ?, ?, ?, ?, ?, ?, ?) ";

    public static final String sql_upd_desc_usergrade =
        "UPDATE UserGrade SET ugr_display_bil = ?,ugr_code = ? WHERE ugr_ent_id = ? ";

    public static final String sql_get_usergrade_by_ste_uid =
        "SELECT ugr_ent_id, ugr_display_bil, ugr_ent_id_root, ugr_type FROM UserGrade, Entity WHERE ent_id = ugr_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL AND ent_ste_uid = ?  AND ugr_ent_id_root = ? ";

    // for user classsification group
    public static final String sql_ins_user_classification =
        "INSERT INTO UserClassification (ucf_ent_id, ucf_display_bil, ucf_ent_id_root, ucf_type, ucf_seq_no) values (?, ?, ?, ?, ? ) ";

    public static final String sql_get_user_classification_by_ste_uid_n_type =
        "SELECT ucf_ent_id, ucf_display_bil, ucf_type, ucf_seq_no FROM UserClassification, Entity WHERE ent_id = ucf_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL AND ent_ste_uid = ?  AND ent_type = ? AND ucf_ent_id_root = ? ";

    public static final String sql_get_all_user_classification_type_in_org =
        "SELECT ent_type, ent_id FROM UserClassification, Entity WHERE ucf_ent_id = ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL AND ucf_ent_id_root = ? and ucf_type = ? ";
        
    public static final String sql_upd_desc_user_classification =
        "UPDATE UserClassification SET ucf_display_bil = ? WHERE ucf_ent_id = ? ";

    // for industry code
    public static final String sql_get_indusrty_by_ste_uid =
        "SELECT idc_ent_id, idc_display_bil, idc_ent_id_root, idc_type FROM IndustryCode, Entity WHERE ent_id = idc_ent_id AND ent_delete_usr_id IS NULL AND ent_delete_timestamp IS NULL AND ent_ste_uid = ?  AND idc_ent_id_root = ? ";

    public static final String sql_get_usergrade_by_display =
		"SELECT ugr_ent_id, ugr_display_bil, ugr_ent_id_root, ugr_type FROM UserGrade WHERE lower(ugr_display_bil) like ?  AND ugr_ent_id_root = ? ";

    public static final String sql_get_grade_root =
        "SELECT ugr_ent_id  FROM UserGrade WHERE ugr_ent_id_root = ? and ugr_TYPE = 'ROOT'";

    // for DbBookmark
    public static final String sql_ins_bookmark =
        "INSERT INTO plBookmark (boo_ent_id, boo_res_id, boo_title, boo_url, boo_create_timestamp) values (?, ?, ?, ?, ?) ";

    public static final String sql_get_all_bookmark =
        "SELECT boo_id, boo_ent_id, boo_res_id, boo_title, boo_url, boo_create_timestamp FROM plBookmark WHERE boo_ent_id = ? ORDER BY boo_create_timestamp desc";

    public static final String sql_del_multi_bookmark =
        "DELETE From plBookmark where boo_ent_id = ? and boo_id in ";

    public static final String sql_get_granted_homepage_privilege =
        " Select ftn_ext_id, ftn_xml From acFunction "+ cwSQL.noLockTable() + ", acHomePage "+ cwSQL.noLockTable() +
        " Where ac_hom_rol_ext_id = ? And ac_hom_ftn_ext_id = ftn_ext_id And ftn_type = ? " +
        " order by ftn_ext_id";


    public static final String sql_has_homepage_privilege =
        " Select ac_hom_rol_ext_id AS e " +
        " From acHomePage " +
        " Where ac_hom_rol_ext_id = ? " +
        " And ac_hom_ftn_ext_id = ? " +
        " Union " +
        " Select ac_hom_rol_ext_id AS e " +
        " From acHomePage " +
        " Where ac_hom_rol_ext_id is null " +
        " And ac_hom_ftn_ext_id = ? ";

    public static final String sql_get_root_idc =
        " Select idc_ent_id, idc_display_bil " +
        " From IndustryCode " +
        " Where idc_ent_id_root = ? " +
        " And idc_type = ? ";

    public static final String sql_ins_idc =
        "Insert into IndustryCode " +
        " (idc_ent_id, idc_display_bil, idc_ent_id_root) " +
        " Values (?, ?, ?) ";

    public static final String sql_upd_idc =
        "Update IndustryCode Set " +
        "idc_display_bil = ? " +
        "Where idc_ent_id = ? ";

    public static final String sql_del_idc =
        "Delete From IndustryCode " +
        "Where idc_ent_id = ? ";

    public static final String sql_get_idc =
        "Select idc_display_bil, idc_ent_id_root, idc_type " +
        " From IndustryCode " +
        " Where idc_ent_id = ? ";

    public static final String sql_search_idc =
        "Select idc_ent_id, idc_display_bil " +
        "From IndustryCode " +
        "Where idc_ent_id_root = ? " +
		"AND lower(idc_display_bil) like ? ";

    public static final String sql_get_CourseCriteria =
    " SELECT ccr_id, ccr_type, ccr_itm_id,  ccr_upd_method, ccr_duration, ccr_attendance_rate, ccr_pass_score, ccr_pass_ind, ccr_all_cond_ind, ccr_offline_condition, ccr_upd_timestamp, ccr_ccr_id_parent FROM CourseCriteria WHERE ccr_id = ? ";

    public static final String sql_get_ccr_id_by_itm_n_type =
    " SELECT ccr_id from CourseCriteria where ccr_itm_id = ? and ccr_type = ? ";

    public static final String sql_get_ccr_id_by_itm =
    " SELECT ccr_id from CourseCriteria where ccr_itm_id = ? ";
    
    public static final String sql_get_module_criteria_lst =
    " SELECT res_title, mod_type, res_src_type , mod_res_id, mod_max_score, cmr_id, cmr_ccr_id, cmr_status , cmr_contri_rate , cmr_is_contri_by_score, cmr_upd_timestamp FROM CourseModuleCriteria, Module, Resources WHERE cmr_res_id = res_id AND mod_res_id = res_id AND cmr_ccr_id = ? AND cmr_del_timestamp is null ORDER BY cmr_upd_timestamp";
    
    public static final String sql_get_module_criteria_lst_by_date =
    " SELECT res_title, mod_type, res_src_type , mod_res_id, mod_max_score, cmr_id, cmr_ccr_id, cmr_status , cmr_contri_rate , cmr_is_contri_by_score, cmr_upd_timestamp FROM CourseModuleCriteria, Module, Resources WHERE cmr_res_id = res_id AND mod_res_id = res_id AND cmr_ccr_id = ? AND cmr_create_timestamp <= ? AND (cmr_del_timestamp >= ? OR cmr_del_timestamp is null) ORDER BY cmr_upd_timestamp";
    
    public static final String sql_get_CourseModuleCriteria_by_ccr_id =
    " SELECT cmr_id, cmr_res_id, cmr_ccr_id, cmr_status, cmr_contri_rate , cmr_is_contri_by_score, cmr_status_desc_option, cmr_cmr_id_parent FROM CourseModuleCriteria WHERE cmr_ccr_id = ? AND cmr_del_timestamp is null ";
    
    public static final String sql_get_OnlineModuleCriteria_by_ccr_id = 
    "select cmr_id, cmr_res_id, cmr_ccr_id, cmr_status, cmr_contri_rate , cmr_is_contri_by_score from courseMeasurement,CourseModuleCriteria "+
    " where cmt_cmr_id = cmr_id and cmt_ccr_id = ? and cmr_res_id = ? and cmr_del_timestamp is null  and cmr_status is not null ";
    
    public static final String sql_del_CourseCriteria =
    " DELETE From CourseCriteria WHERE ccr_ccr_id_parent = ? or ccr_id = ? ";

    public static final String sql_del_CourseModuleCriteria =
    " DELETE From CourseModuleCriteria WHERE cmr_cmr_id_parent = ? or cmr_id = ? ";

    public static final String sql_del_CourseModuleCriteria_by_mod_id =
    " DELETE From CourseModuleCriteria WHERE cmr_res_id = ? ";

    public static final String sql_del_CourseModuleCriteria_by_ccr_id =
    " DELETE From CourseModuleCriteria WHERE cmr_ccr_id = ? ";

    public static final String sql_ins_CourseModuleCriteria =
    " INSERT INTO CourseModuleCriteria (cmr_ccr_id, cmr_res_id, cmr_status, cmr_contri_rate, cmr_is_contri_by_score, cmr_create_timestamp, cmr_create_usr_id, cmr_upd_timestamp, cmr_upd_usr_id, cmr_status_desc_option,cmr_cmr_id_parent ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    public static final String sql_ins_CourseCriteria =
    " INSERT INTO CourseCriteria (ccr_itm_id, ccr_type, ccr_upd_method, ccr_duration, ccr_attendance_rate, ccr_pass_score, ccr_pass_ind, ccr_all_cond_ind, ccr_create_timestamp, ccr_create_usr_id, ccr_upd_timestamp, ccr_upd_usr_id, ccr_offline_condition,ccr_ccr_id_parent) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    public static final String sql_upd_CourseCriteria =
    " UPDATE CourseCriteria SET ccr_upd_method = ? , ccr_duration = ? , ccr_attendance_rate = ? , ccr_pass_score = ? , ccr_pass_ind = ? , ccr_all_cond_ind = ? , ccr_upd_timestamp = ? , ccr_upd_usr_id = ? , ccr_offline_condition = ? WHERE ccr_id = ? ";

    public static final String sql_upd_CourseModuleCriteria =
    " UPDATE CourseModuleCriteria SET cmr_status = ? , cmr_contri_rate = ? , cmr_is_contri_by_score = ?, cmr_upd_timestamp = ? , cmr_upd_usr_id = ? WHERE cmr_id = ? ";

	public static final String sql_get_cmr_id =
		" select * from CourseModuleCriteria where cmr_res_id = ? and cmr_ccr_id = ? and  cmr_del_timestamp is null ";
	public static final String sql_get_by_cmr_id =
		" select * from CourseModuleCriteria where cmr_id = ? and cmr_del_timestamp is null ";
    public static final String sql_soft_del_CourseModuleCriteria =
    " UPDATE CourseModuleCriteria SET cmr_upd_timestamp = ? , cmr_del_timestamp = ? , cmr_upd_usr_id = ? WHERE cmr_id = ? ";

    public static final String sql_is_active_criteria =
    " SELECT COUNT(*) cnt FROM CourseModuleCriteria WHERE cmr_res_id = ? AND cmr_del_timestamp IS NULL ";

    public static final String sql_get_my_grade =
    	" SELECT ern_ancestor_ent_id FROM EntityRelation " + cwSQL.noLockTable() + " WHERE ern_child_ent_id = ? AND ern_type= ? AND ern_parent_ind = ? ";

    public static final String sql_get_my_industry_code =
    " SELECT ern_ancestor_ent_id FROM EntityRelation WHERE ern_child_ent_id = ? AND ern_type= ? AND ern_parent_ind = ? ";

    public static final String sql_get_my_user_group =
    " SELECT ern_ancestor_ent_id FROM EntityRelation WHERE ern_child_ent_id = ? AND ern_type= ? AND ern_parent_ind = ? ";

    public static final String sql_get_peer_ent_id =
    "SELECT gm2.ern_child_ent_id, ugr_display_bil FROM EntityRelation gm1, EntityRelation gm2, UserGrade " +
    "WHERE gm1.ern_child_ent_id = ? AND gm1.ern_parent_ind = ? " + 
    "AND gm1.ern_ancestor_ent_id = gm2.ern_ancestor_ent_id AND ugr_ent_id = gm2.ern_child_ent_id " +
    "AND gm2.ern_parent_ind = ? " +
    "order by ugr_display_bil";

    /** 
                    start date          end date            notification date
    ------------------------------------------------------------------------------------------
    content_end     content_start       content_end         content_start + notify duration          
    has value
    ------------------------------------------------------------------------------------------
    content         att_create_date     att_create_date     att_create_date + notify duration
    duration has value                  + content duration    
    ------------------------------------------------------------------------------------------
    unlimit period  att_create_date     --                  att_create_date + notify duration
    ------------------------------------------------------------------------------------------

        a course is inactive, while:
        1)  content period is limited (either content_end or content_duration has value) AND 
        2)  curTime > content_end if content_end has value  OR
        3)  curTime > content_duration + app confirm date if content_duration has value 
        
        get all active course
        a course is active, while:
        1)   course period is unlimited        OR
        1)   content_start < curTime < content_end if content_end has value OR
        2)   curTime < content_duration + app confirm date if content has value 
        
    */

    public static final String sql_get_set_attend_list(boolean expiredOnly, boolean activeOnly, long app_itm_id, long app_id){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT cos_res_id, ccr_id, ccr_duration, ccr_pass_score , ccr_pass_ind, ccr_all_cond_ind, ccr_attendance_rate, ccr_itm_id, ccr_type, ccr_upd_method, itm_content_eff_duration, att_create_timestamp , itm_content_eff_start_datetime,itm_content_eff_end_datetime, itm_id app_itm_id, itm_create_session_ind, app_id, cov_status, cov_score, cov_ent_id , cov_tkh_id ");
        sql.append(" FROM aeItem , CourseCriteria , Course, aeAttendance, aeApplication, CourseEvaluation   ");
        sql.append(" WHERE cov_final_ind = ? ");                  // FINAL = 0
//        sql.append(" AND ccr_upd_method = ? ");        // not 'manual'
        sql.append(" AND itm_owner_ent_id  =  ? AND ccr_type =  ? ");        // itm_owner_ent_id = root_ent_id , ccr_type = 'completion'
        sql.append(" AND itm_id = cos_itm_id ");
        sql.append(" AND itm_id = ccr_itm_id   ");
        sql.append(" AND att_itm_id = itm_id ");
        sql.append(" AND cov_cos_id = cos_res_id ");  
        sql.append(" AND app_ent_id = cov_ent_id ");
        sql.append(" AND cov_tkh_id = app_tkh_id ");
        sql.append(" AND att_app_id = app_id ");
        if (app_itm_id != 0){
            sql.append(" AND itm_id = " + app_itm_id);
            //sql.append(" AND itm_id = 255");
        }
        if (app_id != 0){
            sql.append(" AND app_id = " + app_id);
        }

        if (expiredOnly){
            sql.append(" AND ((itm_content_eff_start_datetime is not null and itm_content_eff_end_datetime is not null ) or (itm_content_eff_duration is not null and itm_content_eff_duration <> ? )) "); // duration != 0
            sql.append(" AND ( ? > itm_content_eff_end_datetime  or (?  > att_create_timestamp + itm_content_eff_duration and itm_content_eff_duration <> ? ))  ");     // curtime, curtime, 0
        }
        if (activeOnly){
            sql.append(" AND (((itm_content_eff_start_datetime is null and itm_content_eff_end_datetime is null ) and (itm_content_eff_duration is null or itm_content_eff_duration = ?) ) ");
            sql.append("  or ( itm_content_eff_start_datetime < ? and ? < itm_content_eff_end_datetime ) ");
            sql.append(" or (?  < att_create_timestamp + itm_content_eff_duration and itm_content_eff_duration <> ? ))  ");
        }
        sql.append(" UNION ");
        sql.append(" SELECT cos_res_id, ccr_id, ccr_duration, ccr_pass_score , ccr_pass_ind, ccr_all_cond_ind, ccr_attendance_rate, ccr_itm_id, ccr_type, ccr_upd_method, r.itm_content_eff_duration, att_create_timestamp , r.itm_content_eff_start_datetime, r.itm_content_eff_end_datetime, r.itm_id app_itm_id, r.itm_create_session_ind, app_id, cov_status, cov_score, cov_ent_id, cov_tkh_id  ");
        sql.append(" FROM aeItem p, aeitem r, aeItemRelation, courseCriteria , course, aeAttendance, aeApplication, courseEvaluation   ");
        sql.append(" WHERE cov_final_ind = ? ");      // final_ind = 0
//        sql.append(" AND ccr_upd_method = ? ");        
        sql.append(" AND r.itm_run_ind = ?  ");     // run = 1, 
        sql.append(" AND r.itm_id = ire_child_itm_id and p.itm_id = ire_parent_itm_id  ");
        sql.append(" AND p.itm_owner_ent_id  = ? and ccr_type = ? ");       // root_ent_id = 1 , 'completion'
        sql.append(" AND p.itm_id = cos_itm_id   ");
        sql.append(" AND p.itm_id = ccr_itm_id   ");
        sql.append(" AND att_itm_id = r.itm_id ");
        sql.append(" AND cov_cos_id = cos_res_id   ");
        sql.append(" AND app_ent_id = cov_ent_id ");
        sql.append(" AND cov_tkh_id = app_tkh_id ");
        sql.append(" AND att_app_id = app_id ");
        if (app_itm_id != 0){
            sql.append(" AND r.itm_id = " + app_itm_id);
			//sql.append(" AND itm_id = 255");
        }
        if (app_id != 0){
            sql.append(" AND app_id = " + app_id);
        }
       
        if (expiredOnly){
            sql.append(" AND r.itm_content_eff_start_datetime is not null and r.itm_content_eff_end_datetime is not null  ");
            sql.append(" AND ? > r.itm_content_eff_end_datetime   ");       // curtime
        }
        if (activeOnly){
            sql.append(" AND ((r.itm_content_eff_start_datetime is null and r.itm_content_eff_end_datetime is null )");
            sql.append(" or ( r.itm_content_eff_start_datetime < ? and ? < r.itm_content_eff_end_datetime  ) )");
        }
        
        return sql.toString();
    }
    
    
    public static String sql_get_lrnr_value(Connection con, boolean expiredOnly, boolean activeOnly, long app_itm_id, long app_id) throws SQLException{
		        StringBuffer sql = new StringBuffer();
				sql.append(" SELECT cos_res_id, ccr_id, ccr_duration, ccr_pass_score , ccr_pass_ind, ccr_all_cond_ind, ccr_attendance_rate, ccr_itm_id, ccr_type, ccr_upd_method, itm_content_eff_duration, att_create_timestamp , itm_content_eff_start_datetime,itm_content_eff_end_datetime, itm_id app_itm_id, itm_create_session_ind, app_id, cov_status, cov_score, cov_ent_id , cov_tkh_id, itm_mark_buffer_day ");
				sql.append(" FROM aeItem , CourseCriteria , Course, aeAttendance, aeApplication, CourseEvaluation   ");
				sql.append(" WHERE cov_final_ind = ? ");                  // FINAL = 0
//				  sql.append(" AND ccr_upd_method = ? ");        // not 'manual'
				sql.append(" AND itm_owner_ent_id  =  ? AND ccr_type =  ? ");        // itm_owner_ent_id = root_ent_id , ccr_type = 'completion'
				sql.append(" AND itm_id = cos_itm_id ");
				sql.append(" AND itm_id = ccr_itm_id   ");
				sql.append(" AND att_itm_id = itm_id ");
				sql.append(" AND cov_cos_id = cos_res_id ");  
				sql.append(" AND app_ent_id = cov_ent_id ");
				sql.append(" AND cov_tkh_id = app_tkh_id ");
				sql.append(" AND att_app_id = app_id ");
				sql.append(" AND itm_integrated_ind = ? ");
				if (app_itm_id != 0){
					sql.append(" AND itm_id = " + app_itm_id);
				}
				if (app_id != 0){
					sql.append(" AND app_id = " + app_id);
				}

				if (expiredOnly){
					sql.append(" AND ((itm_content_eff_start_datetime is not null and itm_content_eff_end_datetime is not null ) or (itm_content_eff_duration is not null and itm_content_eff_duration <> ? and itm_blend_ind = ?) or (itm_eff_start_datetime is not null and itm_eff_end_datetime is not null and itm_blend_ind = ?)) "); // duration != 0
                    sql.append(" AND ((")
                    .append(" (? > itm_content_eff_end_datetime + ").append(cwSQL.replaceNull("itm_mark_buffer_day", "0")).append(" ").append(cwSQL.getDayUnit()).append(" and itm_blend_ind = ?) ")
                    .append(" OR (")
                    .append(" ? > itm_content_eff_end_datetime + ").append(cwSQL.replaceNull("itm_mark_buffer_day", "0")).append(" ").append(cwSQL.getDayUnit())
                    .append(" AND ? > itm_eff_end_datetime + ").append(cwSQL.replaceNull("itm_mark_buffer_day", "0")).append(" ").append(cwSQL.getDayUnit())
                    .append(" AND itm_blend_ind = ? )) ")
                    .append(" OR (")
                    .append(" (? > att_create_timestamp + itm_content_eff_duration + ").append(cwSQL.replaceNull("itm_mark_buffer_day", "0")).append(" ").append(cwSQL.getDayUnit()).append(" ) ")
                    .append(" AND itm_content_eff_duration <> ? AND itm_blend_ind = ?))");
				}
				if (activeOnly){
					sql.append(" AND (((itm_content_eff_start_datetime is null and itm_content_eff_end_datetime is null ) and (itm_content_eff_duration is null or itm_content_eff_duration = ?) ) ");
					sql.append("  or ( itm_content_eff_start_datetime < ? and ? < itm_content_eff_end_datetime ) ");
					sql.append(" or (?  < att_create_timestamp + itm_content_eff_duration and itm_content_eff_duration <> ? ))  ");
				}
				return sql.toString();
    }

    // kim:todo
    /*
    public static final String sql_get_no_completion_criteria_active_course(){
        StringBuffer sql = new StringBuffer();
        sql.append(" select cos_res_id from course , aeitem ");
        sql.append(" where  ");
        sql.append(" itm_id = cos_itm_id  ");
        sql.append(" and cos_eff_start_datetime < ? ");
        sql.append(" and cos_eff_end_datetime > ?  ");


        sql.append(" and itm_owner_ent_id  =  ? ");
        sql.append(" and cos_res_id not in  ");
        sql.append(" (select ccr_cos_id from  courseCriteria where ccr_type = ?) ");
        return sql.toString();
    }
    */
    // expired method in completion criteria, to set attend or score during course eff period
    /*
    public static final String sql_get_active_list(){
        StringBuffer sql = new StringBuffer();
        sql.append(" select res_title, itm_id, cos_res_id, ccr_id, ccr_pass_score , ccr_max_score , ccr_pass_ind, ccr_all_cond_ind, ccr_duration, enr_create_timestamp , cos_eff_start_datetime, cos_eff_end_datetime, cov_status, cov_ent_id, cov_final_ind ");
        sql.append(" from aeItem, course , courseCriteria , resources , enrolment , courseEvaluation  ");
        sql.append(" where  ");
        sql.append(" itm_id = cos_itm_id  ");
        sql.append(" and cos_res_id = res_id ");
        sql.append(" and cos_res_id = ccr_cos_id  ");
        sql.append(" and cos_res_id = enr_res_id  ");
        sql.append(" and cov_cos_id = cos_res_id  ");
        sql.append(" and cov_ent_id = enr_ent_id  ");
        sql.append(" and itm_owner_ent_id  =  ? and ccr_type =  ?  ");
        sql.append(" and (? < cos_eff_end_datetime OR cos_eff_end_datetime is null) AND (? > cos_eff_start_datetime OR cos_eff_start_datetime is null)  ");
        sql.append(" AND ( ");
        sql.append(" ((enr_create_timestamp > cos_eff_start_datetime or cos_eff_start_datetime is null) and ? < enr_create_timestamp + ccr_duration and ccr_duration != 0) ");
        sql.append(" or  ");
        sql.append(" (enr_create_timestamp < cos_eff_start_datetime and ? < cos_eff_start_datetime + ccr_duration and ccr_duration != 0) ");
        sql.append(" or  ");
        sql.append(" ccr_duration = 0 ");
        sql.append(" ) ");
        return sql.toString();
    }
    */
// kim:todo
/*
    public static final String sql_get_notification_active_list(){
        StringBuffer sql = new StringBuffer();
        sql.append(" select res_title, itm_id, cos_res_id, ccr_id, ccr_pass_score , ccr_max_score , ccr_pass_ind, ccr_all_cond_ind, ccr_duration, enr_create_timestamp , cos_eff_start_datetime, cos_eff_end_datetime, cov_status, cov_ent_id, cov_final_ind ");
        sql.append(" from aeItem, course , courseCriteria , resources , enrolment , courseEvaluation  ");
        sql.append(" where  ");
        sql.append(" itm_id = cos_itm_id  ");
        sql.append(" and cos_res_id = res_id ");
        sql.append(" and cos_res_id = ccr_cos_id  ");
        sql.append(" and cos_res_id = enr_res_id  ");
        sql.append(" and cov_cos_id = cos_res_id  ");
        sql.append(" and cov_ent_id = enr_ent_id  ");
        sql.append(" and itm_owner_ent_id  =  ? and ccr_type =  ?  ");
        sql.append(" and (? < cos_eff_end_datetime OR cos_eff_end_datetime is null) AND (? > cos_eff_start_datetime OR cos_eff_start_datetime is null)  ");
        sql.append(" AND ( ");
        // notification is active while the duration is expired
        sql.append(" ((enr_create_timestamp > cos_eff_start_datetime or cos_eff_start_datetime is null) and ? > enr_create_timestamp + ccr_duration and ccr_duration != 0) ");
        sql.append(" or  ");
        sql.append(" (enr_create_timestamp < cos_eff_start_datetime and ? > cos_eff_start_datetime + ccr_duration and ccr_duration != 0) ");
        sql.append(" or  ");
        sql.append(" ccr_duration = 0 ");
        sql.append(" ) ");
        return sql.toString();
    }
*/
    public static final String SQL_GET_RTE_TYPE = 
        " Select rte_type From ReportTemplate " +
        " Where rte_id in ";

    public static final String sql_ins_role_target_entity =
        "INSERT INTO usrRoleTargetEntity (rte_usr_ent_id, rte_rol_ext_id, " +
        " rte_group_id, rte_ent_id, rte_syn_timestamp, rte_eff_start_datetime, rte_eff_end_datetime, rte_create_timestamp, rte_create_usr_id) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    public static final String sql_get_max_role_target_entity_group_id =
        "SELECT MAX(rte_group_id) AS m FROM usrRoleTargetEntity " +
        "WHERE rte_usr_ent_id = ? " +
        "AND rte_rol_ext_id = ? ";
    public static final String sql_get_role_target_entity_ids =
        "SELECT rte_ent_id FROM usrRoleTargetEntity " +
        "WHERE rte_usr_ent_id = ? " +
        "AND rte_rol_ext_id = ? " +
        "AND (rte_eff_start_datetime IS NULL OR rte_eff_start_datetime <= ?) " +
        "AND (rte_eff_end_datetime IS NULL OR rte_eff_end_datetime >= ? )";
    // check effective period by adding filtering criteria at the end of following sql.
    public static final String sql_check_role_target_entity =
        "SELECT rte_ent_id FROM usrRoleTargetEntity " +
        "WHERE rte_usr_ent_id = ? AND rte_rol_ext_id = ? AND rte_ent_id = ? ";
    public static final String upd_role_target_entity_syn_timestamp =
        "UPDATE usrRoleTargetEntity SET rte_syn_timestamp = ? " + 
        "WHERE rte_usr_ent_id = ? AND rte_rol_ext_id = ? AND rte_ent_id = ? ";

    public static final String sql_ins_assessment_evaluation =
        "INSERT INTO AssessmentEvaluation (asv_ent_id , asv_mod_id , asv_correct_cnt , asv_incorrect_cnt , asv_giveup_cnt ) values (?,?,?,?,?) ";

    public static final String sql_upd_assessment_evaluation =
        "UPDATE AssessmentEvaluation SET asv_correct_cnt = ? , asv_incorrect_cnt = ? , asv_giveup_cnt = ? WHERE asv_ent_id = ? AND asv_mod_id = ? ";

    public static final String sql_get_assessment_evaluation =
        "SELECT asv_correct_cnt , asv_incorrect_cnt , asv_giveup_cnt FROM AssessmentEvaluation WHERE asv_ent_id = ? AND asv_mod_id = ? ";

    /**
     * added for facility management
     */
    // for facility type
    public static final String sql_get_parent_type  = "SELECT ftp_parent_ftp_id FROM fmFacilityType WHERE ftp_id = ?";
    public static final String sql_get_type_name    = "SELECT ftp_title_xml FROM fmFacilityType WHERE ftp_id = ?";
    public static final String sql_get_type_class   = "SELECT ftp_class_name FROM fmFacilityType WHERE ftp_id = ?";
    // for facility
    public static final String sql_check_duplicate  = "SELECT fac_id FROM fmFacility WHERE fac_owner_ent_id = ? AND fac_title = ? AND fac_status <> 'DELETED'";
    // << BEGIN for oracle migration!
    //public static final String sql_ins_facility       = "INSERT INTO fmFacility (fac_ftp_id, fac_title, fac_desc, fac_remarks, fac_url, fac_url_type, fac_add_xml, fac_status, fac_loc_id, fac_order, fac_owner_ent_id, fac_create_timestamp, fac_create_usr_id, fac_upd_timestamp, fac_upd_usr_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String sql_ins_facility     = "INSERT INTO fmFacility (fac_ftp_id, fac_title, fac_desc, fac_remarks, fac_url, fac_url_type, fac_status, fac_loc_id, fac_order, fac_owner_ent_id, fac_create_timestamp, fac_create_usr_id, fac_upd_timestamp, fac_upd_usr_id, fac_fee) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    // >> END
    public static final String sql_get_facility_id  = "SELECT MAX(fac_id) FROM fmFacility";
    // << BEGIN for oracle migration!
    //public static final String sql_upd_facility       = "UPDATE fmFacility SET fac_title = ?, fac_desc = ?, fac_remarks = ?, fac_url = ?, fac_url_type = ?, fac_add_xml = ?, fac_status = ?, fac_loc_id = ?, fac_order = ?, fac_upd_timestamp = ?, fac_upd_usr_id = ? WHERE fac_id = ? AND fac_upd_timestamp = ?";
    // >> END
    public static final String sql_get_facility     = "SELECT fac_ftp_id, fac_title, fac_desc, fac_status, fac_loc_id, fac_order FROM fmFacility WHERE fac_id = ?";
    public static final String sql_upd_facility_status      = "UPDATE fmFacility SET fac_status = 'DELETED', fac_upd_timestamp = ?, fac_upd_usr_id = ? WHERE fac_id = ? AND fac_upd_timestamp = ?";
    public static final String sql_check_facility   = "SELECT fsh_rsv_id FROM fmFacilitySchedule WHERE fsh_fac_id = ?";
    public static final String sql_del_facility     = "DELETE FROM fmFacility WHERE fac_id = ? AND fac_upd_timestamp = ?";
    public static final String sql_get_facility_type= "SELECT fac_ftp_id FROM fmFacility WHERE fac_id = ?";
    //public static final String sql_get_all_type     = "SELECT type.ftp_id type_id, type.ftp_title_xml type_title_xml, type.ftp_main_indc, type.ftp_xsl_prefix, subType.ftp_id sub_type_id, subType.ftp_title_xml sub_type_title_xml FROM fmFacilityType type, fmFacilityType subType WHERE type.ftp_owner_ent_id = ? AND type.ftp_id = subType.ftp_parent_ftp_id ORDER BY type_id, sub_type_id";
    public static final String sql_get_all_type     = "SELECT type.ftp_id type_id, type.ftp_title_xml type_title_xml, type.ftp_main_indc, type.ftp_xsl_prefix, subType.ftp_id sub_type_id, subType.ftp_title_xml sub_type_title_xml FROM fmFacilityType type, fmFacilityType subType WHERE type.ftp_owner_ent_id = ? AND type.ftp_owner_ent_id = subType.ftp_owner_ent_id AND type.ftp_id = subType.ftp_parent_ftp_id ORDER BY type_id, sub_type_id";

    public static final String sql_get_all_facility         = "SELECT fac_id, fac_status, fac_title, fac_ftp_id, rom_ftp_id sub_type_id FROM fmRoom, fmFacility WHERE fac_owner_ent_id = ? AND fac_status <> 'DELETED' AND fac_id = rom_fac_id UNION SELECT fac_id, fac_status, fac_title, fac_ftp_id, eqm_ftp_id sub_type_id FROM fmEquipment, fmFacility WHERE fac_owner_ent_id = ? AND fac_status <> 'DELETED' AND fac_id = eqm_fac_id ORDER BY fac_ftp_id, sub_type_id, fac_title";
    public static final String sql_get_all_facility_by_type = "SELECT fac_id, fac_status, fac_title, fac_ftp_id, rom_ftp_id sub_type_id FROM fmRoom, fmFacility WHERE fac_owner_ent_id = ? AND fac_ftp_id = ? AND fac_status <> 'DELETED' AND fac_id = rom_fac_id UNION SELECT fac_id, fac_status, fac_title, fac_ftp_id, eqm_ftp_id sub_type_id FROM fmEquipment, fmFacility WHERE fac_owner_ent_id = ? AND fac_ftp_id = ? AND fac_status <> 'DELETED' AND fac_id = eqm_fac_id ORDER BY fac_ftp_id, sub_type_id, fac_title";
    // for room
    public static final String sql_get_room     = "SELECT rom_fac_id, rom_ftp_id, ftp_title_xml, ftp_xsl_prefix, rom_capacity, fac_ftp_id, fac_title, fac_desc, fac_remarks, fac_url, fac_url_type, fac_status, fac_add_xml, fac_owner_ent_id, fac_create_timestamp, fac_create_usr_id, fac_upd_timestamp, fac_upd_usr_id, fac_loc_id, fac_order, fac_fee FROM fmFacility, fmRoom, fmFacilityType WHERE rom_fac_id = ? AND fac_id = rom_fac_id AND fac_ftp_id = ftp_id";
    public static final String sql_ins_room     = "INSERT INTO fmRoom (rom_fac_id, rom_ftp_id) VALUES (?, ?)";
//  public static final String sql_upd_room     = "UPDATE fmRoom SET rom_ftp_id = ?, rom_capacity = ? WHERE rom_fac_id = ?";
    public static final String sql_del_room     = "DELETE FROM fmRoom WHERE rom_fac_id = ?";
    // for equipment
    public static final String sql_get_equipment    = "SELECT eqm_fac_id, eqm_ftp_id, ftp_title_xml, ftp_xsl_prefix, fac_ftp_id, fac_title, fac_desc, fac_remarks, fac_url, fac_url_type, fac_status, fac_add_xml, fac_owner_ent_id, fac_create_timestamp, fac_create_usr_id, fac_upd_timestamp, fac_upd_usr_id, fac_loc_id, fac_order, fac_fee FROM fmFacility, fmEquipment, fmFacilityType WHERE eqm_fac_id = ? AND fac_id = eqm_fac_id AND fac_ftp_id = ftp_id";
    public static final String sql_ins_equipment    = "INSERT INTO fmEquipment (eqm_fac_id, eqm_ftp_id) VALUES (?, ?)";
//  public static final String sql_upd_equipment    = "UPDATE fmEquipment SET eqm_ftp_id = ? WHERE eqm_fac_id = ?";
    public static final String sql_del_equipment    = "DELETE FROM fmEquipment WHERE eqm_fac_id = ?";
    // for calendar
    public static final String sql_get_slot         = "SELECT tsl_id, tsl_start_time, tsl_end_time FROM fmTimeSlot WHERE tsl_owner_ent_id = ? ORDER BY tsl_id";
    public static final String sql_get_limit_time   = "SELECT ste_rsv_min_gap, ste_rsv_min_len FROM acSite WHERE ste_ent_id = ?";
    // for retrieving the user name
    public static final String sql_get_user_name    = "SELECT usr_first_name_bil, usr_last_name_bil, usr_display_bil FROM RegUser WHERE (usr_ent_id = ?)";
    //--------------------------------------------------------------------------
    // get facility Type ;ftp_parent_ftp_id is null ,root_ent_id,order by ftp_id
    public static final String sql_get_ftpParentIsNull = "select ftp_id,ftp_title_xml,ftp_main_indc,ftp_xsl_prefix from fmFacilityType where ftp_owner_ent_id = ? and ftp_parent_ftp_id is null order by ftp_id";

    // get reservation ;by rsv_id ,root_ent_id,
    //public static final String sql_get_rsv_RsvId = "select rsv_id,rsv_status,rsv_main_fac_id,rsv_purpose,rsv_desc,rsv_ent_id,rsv_participant_no,rsv_cancel_usr_id,rsv_cancel_timestamp,rsv_cancel_type,rsv_cancel_reason,rsv_create_usr_id,rsv_create_timestamp,rsv_upd_usr_id,rsv_upd_timestamp,usr_last_name_bil ,usr_first_name_bil,usr_display_bil from fmReservation,RegUser where usr_ent_id = rsv_ent_id and rsv_id = ? and rsv_owner_ent_id = ? ";
    public static final String sql_get_rsv_RsvId = 
        "select rsv_id,rsv_status,rsv_main_fac_id,rsv_purpose,rsv_desc,rsv_ent_id,rsv_participant_no,rsv_cancel_usr_id, " + 
        "rsv_cancel_timestamp,rsv_cancel_type,rsv_cancel_reason,rsv_create_usr_id,rsv_create_timestamp,rsv_upd_usr_id, " +
        "rsv_upd_timestamp,reserveFor.usr_last_name_bil,reserveFor.usr_first_name_bil,reserveFor.usr_display_bil, " +
        "lastModify.usr_last_name_bil upd_usr_last_name_bil,lastModify.usr_first_name_bil upd_usr_first_name_bil,lastModify.usr_display_bil upd_usr_display_bil " +
        "from fmReservation,RegUser reserveFor,RegUser lastModify " +
        "where rsv_id = ? and rsv_owner_ent_id = ? and reserveFor.usr_ent_id = rsv_ent_id and lastModify.usr_id = rsv_upd_usr_id ";        
    
    // get facilitySchedule facility facilityType ; by rsv_id join three tables order by fac_ftp_id,fac_title
    //DENNIS: changed to join fmRoom and fmEquipment to make order on Facility sub_type
    //public static final String sql_get_fsh_RsvId = "select fac_id,fac_ftp_id,fac_title,fac_desc,fsh_date,fsh_start_time,fsh_end_time,fsh_status,fsh_upd_usr_id,fsh_upd_timestamp,fsh_cancel_usr_id,fsh_cancel_timestamp,fsh_cancel_type,fsh_cancel_reason from fmFacilitySchedule,fmFacility,fmFacilityType where ftp_id = fac_ftp_id and fac_id = fsh_fac_id and fsh_rsv_id = ? and fsh_owner_ent_id = ? order by fac_ftp_id,fac_title,fsh_date,fsh_start_time";
    public static final String sql_get_fsh_RsvId =
        "select fac_id,fac_ftp_id,fac_title,fac_desc,fsh_date,fsh_start_time,fsh_end_time,fsh_status,fsh_upd_usr_id,fsh_upd_timestamp,fsh_cancel_usr_id,fsh_cancel_timestamp,fsh_cancel_type,fsh_cancel_reason,rom_ftp_id as sub_type_id "
        + "from fmFacilitySchedule,fmFacility,fmFacilityType,fmRoom "
        + "where ftp_id = fac_ftp_id and fac_id = fsh_fac_id and fsh_rsv_id = ? and fsh_owner_ent_id = ? and fac_id = rom_fac_id "
        + "union "
        + "select fac_id,fac_ftp_id,fac_title,fac_desc,fsh_date,fsh_start_time,fsh_end_time,fsh_status,fsh_upd_usr_id,fsh_upd_timestamp,fsh_cancel_usr_id,fsh_cancel_timestamp,fsh_cancel_type,fsh_cancel_reason,eqm_ftp_id as sub_type_id "
        + "from fmFacilitySchedule,fmFacility,fmFacilityType,fmEquipment "
        + "where ftp_id = fac_ftp_id and fac_id = fsh_fac_id and fsh_rsv_id = ? and fsh_owner_ent_id = ? and fac_id = eqm_fac_id "
        + "order by fac_ftp_id,fsh_date,fsh_start_time,sub_type_id,fac_title ";
    // get fscilitySchedule ; by rsv_id is null ,fsh_create_usr_id,root_ent_id,
    //public static final String sql_get_fsh_cart = "select fac_id,fac_ftp_id,fac_title,fac_desc,fsh_date,fsh_start_time,fsh_end_time,fsh_status,fsh_upd_usr_id,fsh_upd_timestamp,fsh_cancel_usr_id,fsh_cancel_timestamp,fsh_cancel_type,fsh_cancel_reason from fmFacilitySchedule,fmFacility,fmFacilityType where ftp_id = fac_ftp_id and fac_id = fsh_fac_id and fsh_rsv_id is null and fsh_create_usr_id = ? and fsh_owner_ent_id = ? order by fac_ftp_id,fac_title,fsh_date,fsh_start_time";
    public static final String sql_get_fsh_cart = 
        " select fac_id,fac_ftp_id,fac_title,fac_desc,fsh_date,fsh_start_time,fsh_end_time,fsh_status,fsh_upd_usr_id,fsh_upd_timestamp,fsh_cancel_usr_id,fsh_cancel_timestamp,fsh_cancel_type,fsh_cancel_reason, rom_ftp_id as ftp_sub_id " + 
        " from fmFacilitySchedule,fmFacility,fmFacilityType,fmRoom where " + 
        " ftp_id = fac_ftp_id and fac_id = fsh_fac_id and fsh_rsv_id is null and fac_id = rom_fac_id and fsh_create_usr_id = ? and fsh_owner_ent_id = ? " + 
        " UNION " + 
        " select fac_id,fac_ftp_id,fac_title,fac_desc,fsh_date,fsh_start_time,fsh_end_time,fsh_status,fsh_upd_usr_id,fsh_upd_timestamp,fsh_cancel_usr_id,fsh_cancel_timestamp,fsh_cancel_type,fsh_cancel_reason, eqm_ftp_id as ftp_sub_id " + 
        " from fmFacilitySchedule,fmFacility,fmFacilityType,fmEquipment where " + 
        " ftp_id = fac_ftp_id and fac_id = fsh_fac_id and fsh_rsv_id is null and fac_id = eqm_fac_id and fsh_create_usr_id = ? and fsh_owner_ent_id = ? " + 
        " order by fsh_date, fsh_start_time, ftp_sub_id, fac_title ";
    
    // delete facilitySchedule(s) from cart
    public static final String sql_del_fsh_cart = "delete from fmFacilitySchedule WHERE fsh_rsv_id is null";
    // prepare cancel facilitySchedule(s)
    public static final String sql_cancel_fsh_prep = "select fac_id,fac_ftp_id,fac_title,fac_desc,fsh_date,fsh_start_time,fsh_end_time,fsh_status,fsh_upd_usr_id,fsh_upd_timestamp,fsh_cancel_usr_id,fsh_cancel_timestamp,fsh_cancel_type,fsh_cancel_reason from fmFacilitySchedule,fmFacility,fmFacilityType where ftp_id = fac_ftp_id and fac_id = fsh_fac_id and fsh_rsv_id = ? and fsh_owner_ent_id = ?";
    // cancel facilitySchedule(s)
    public static final String sql_cancel_fsh = "update fmFacilitySchedule set fsh_status = ? ,fsh_upd_timestamp = ? ,fsh_upd_usr_id = ? ,fsh_cancel_timestamp = ?,fsh_cancel_usr_id = ?,fsh_cancel_type = ? , fsh_cancel_reason = ? WHERE fsh_rsv_id = ? and fsh_status <> 'CANCELLED'";

    // prepare cancel reservation
    //public static final String sql_cancel_rsv_prep = "select rsv_id,rsv_status,rsv_main_fac_id,rsv_purpose,rsv_desc,rsv_ent_id,rsv_participant_no,rsv_cancel_usr_id,rsv_cancel_timestamp,rsv_cancel_type,rsv_cancel_reason,rsv_create_usr_id,rsv_create_timestamp,rsv_upd_usr_id,rsv_upd_timestamp,usr_last_name_bil ,usr_first_name_bil,usr_display_bil from fmReservation,RegUser where usr_ent_id = rsv_ent_id and rsv_id = ? and rsv_owner_ent_id = ? and rsv_upd_timestamp = ?";
    public static final String sql_cancel_rsv_prep = 
        " select rsv_id,rsv_status,rsv_main_fac_id,rsv_purpose,rsv_desc,rsv_ent_id,rsv_participant_no,rsv_cancel_usr_id,rsv_cancel_timestamp,rsv_cancel_type,rsv_cancel_reason,rsv_create_usr_id,rsv_create_timestamp,rsv_upd_usr_id,rsv_upd_timestamp, " +
        " reserveFor.usr_last_name_bil,reserveFor.usr_first_name_bil,reserveFor.usr_display_bil, " +
        " lastModify.usr_last_name_bil upd_usr_last_name_bil,lastModify.usr_first_name_bil upd_usr_first_name_bil,lastModify.usr_display_bil upd_usr_display_bil " +
        " from fmReservation,RegUser reserveFor,RegUser lastModify where reserveFor.usr_ent_id = rsv_ent_id and lastModify.usr_id = rsv_upd_usr_id and rsv_id = ? and rsv_owner_ent_id = ? ";//and rsv_upd_timestamp = ?";

    //cancel reservation
    public static final String sql_cancel_rsv = "update fmReservation set rsv_status = ? ,rsv_upd_timestamp = ?,rsv_upd_usr_id = ?,rsv_cancel_timestamp = ?,rsv_cancel_usr_id= ?,rsv_cancel_type = ? , rsv_cancel_reason = ? WHERE rsv_id = ? and rsv_status <> 'CANCEL'"; //and rsv_upd_timestamp = ?
    // cancel fsh check rsv_main_fac_id ;reset to null
    public static final String sql_reset_rsv_main_fac_id_get = "select fsh_fac_id from fmReservation,fmFacilityschedule where rsv_main_fac_id = fsh_fac_id and rsv_id = fsh_rsv_id and fsh_status <> 'CANCELLED' and rsv_id = ?";
    public static final String sql_reset_rsv_main_fac_id = "update fmReservation set rsv_main_fac_id = null,rsv_desc=null where rsv_id = ?";
    // multi part:sql_upd_fsh_multi ,
    // for upd facilitySchedule(s),include delete,cancel,update,and other update cases...
    public static final String sql_upd_fsh_multi = " (fsh_fac_id = ? and fsh_start_time = ? and fsh_upd_timestamp = ?)";
    public static final String sql_where = " where";
    public static final String sql_and = " and";
    public static final String sql_or = " or";
    public static final String sql_orderBy = " order by fac_ftp_id,fac_title,fsh_date,fsh_start_time";
    public static final String sql_cancel_fshRsvId = " fsh_rsv_id = ?";

    public static final String sql_fsh_audit_trail = " fsh_create_usr_id = ? and fsh_owner_ent_id = ?";
    // for ae_fm_linkage
    public static final String sql_get_site_rsv_link = "select ste_rsv_link from acSite where ste_ent_id = ?";
    // for access control checking in fm
    // get the create_usr_id and owner_ent_id of a reservation record
    // Vector getRsvCreateUserID(rsv_id);
    /* DENNIS BEGIN */
    public static final String sql_get_RsvCreateUserID = "select rsv_create_usr_id,rsv_owner_ent_id,rsv_upd_timestamp from fmReservation where rsv_id = ?";
    //public static final String sql_get_RsvCreateUserID = "select rsv_create_usr_id,rsv_owner_ent_id from fmReservation where rsv_id = ?";
    /* DENNIS END */
    // and rsv_root_ent_id = ?";
    // get the create_usr_id and owner_ent_id of a facility schedule
    // Vector getFshCreateUserID(fac_id, start_time);
    public static final String sql_get_FshCreateUserID = "select fsh_create_usr_id,fsh_owner_ent_id from fmFacilitySchedule where fsh_fac_id = ? and fsh_start_time = ?";
    public static final String sql_get_FacCreateUserID = "select fac_create_usr_id,fac_owner_ent_id from fmFacility where fac_id = ?";


    /* DENNIS FM BEGIN */
    //get conflicting facility schedule
    public static final String get_conflict_fsh_select =
        " Select rsv_id, rsv_purpose, rsv_status, rsv_participant_no, " +
        " fsh_date, fsh_start_time, fsh_end_time, fsh_status, " +
        " usr_ent_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, " +
        " fac_id, fac_title ";
    public static final String get_conflict_fsh_from =
        " From fmFacilitySchedule, fmFacility, RegUser ";
    public static final String get_conflict_fsh_where =
        " Where  " +
        " fac_id = fsh_fac_id " +
        " And usr_id = fsh_create_usr_id " +
        " And fac_id = ? " +
        " And fsh_status <> ? " +
        " And fsh_end_time > ? " +
        " And fsh_start_time < ? ";

    //lock table fmFacilitySchedule
    public static final String sql_lock_fsh =
        " Update fmFacilitySchedule Set fsh_fac_id = fsh_fac_id ";

    //insert facility schedule into a shopping cart
    public static final String sql_add_fsh_2_cart =
        " Insert into fmFacilitySchedule " +
        " (fsh_fac_id, fsh_date, fsh_start_time, fsh_end_time, fsh_status, " +
        " fsh_owner_ent_id, fsh_create_timestamp, fsh_create_usr_id, " +
        " fsh_upd_timestamp, fsh_upd_usr_id)" +
        " Values " +
        " (?,?,?,?,?,?,?,?,?,?) ";

    //insert facility schedule into a shopping cart
    public static final String sql_add_fsh_2_rsv =
        " Insert into fmFacilitySchedule " +
        " (fsh_fac_id, fsh_date, fsh_start_time, fsh_end_time, fsh_status, " +
        " fsh_owner_ent_id, fsh_create_timestamp, fsh_create_usr_id, " +
        " fsh_upd_timestamp, fsh_upd_usr_id, fsh_rsv_id)" +
        " Values " +
        " (?,?,?,?,?,?,?,?,?,?,?) ";

    //insert reservetion
    public static final String sql_ins_rsv =
        " Insert into fmReservation " +
        " (rsv_purpose, rsv_desc, rsv_ent_id, rsv_participant_no, rsv_main_fac_id, " +
        " rsv_status, rsv_owner_ent_id, rsv_create_timestamp, rsv_create_usr_id, " +
        " rsv_upd_timestamp, rsv_upd_usr_id) " +
        " Values " +
        " (?,?,?,?,?,?,?,?,?,?,?) ";

    //update reservation
    public static final String sql_upd_rsv =
        " Update fmReservation Set " +
        " rsv_purpose = ? " +
        " ,rsv_desc = ? " +
        " ,rsv_ent_id = ? " +
        " ,rsv_participant_no = ? " +
        " ,rsv_main_fac_id = ? " +
        " ,rsv_upd_timestamp = ? " +
        " ,rsv_upd_usr_id = ? " +
        " Where rsv_id = ? ";

    //get reservation start, end time
    public static final String sql_get_rsv_start_end_time =
        " Select min(fsh_start_time) as rsv_start_time , " +
        " max(fsh_end_time) as rsv_end_time " +
        " From fmFacilitySchedule " +
        " Where fsh_rsv_id = ? " +
        " And fsh_status <> ? ";
    /* DENNIS FM END */

	public static String updRsvPurposeForItemWithCourseLevelId() {
		StringBuffer SQL = new StringBuffer(1024);

		SQL.append(" Update fmReservation set rsv_purpose = " + 
		" (Select parent.itm_title " + cwSQL.getConcatOperator() + " ' - ' " + cwSQL.getConcatOperator() + " child.itm_title from aeItem parent, aeItem child, aeItemRelation where ire_parent_itm_id = ? and parent.itm_id = ire_parent_itm_id and ire_child_itm_id = child.itm_id and child.itm_rsv_id = rsv_id) " + 
		" where rsv_id " + 
		" in (select itm_rsv_id from aeItem child, aeItemRelation where ire_parent_itm_id = ? and ire_child_itm_id = child.itm_id) "); 

		return SQL.toString();
	}

//	Deprecated by prev function	
//	   public static final String upd_rsv_purpose_for_item_with_course_level_id = 
//		   " Update fmReservation set rsv_purpose = " + 
//		   " (Select parent.itm_title + ' - ' + child.itm_title from aeItem parent, aeItem child, aeItemRelation where ire_parent_itm_id = ? and parent.itm_id = ire_parent_itm_id and ire_child_itm_id = child.itm_id and child.itm_rsv_id = rsv_id) " + 
//		   " where rsv_id " + 
//		   " in (select itm_rsv_id from aeItem child, aeItemRelation where ire_parent_itm_id = ? and ire_child_itm_id = child.itm_id) "; 

	public static String updRsvPurposeForItemWithRunLevelId() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer SQL = new StringBuffer(1024);

		if (dbproduct.indexOf(ProductName_DB2) >= 0 || dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQL.append(" Update fmReservation set rsv_purpose = " + 
			" (select parent.itm_title || ' - ' || child.itm_title from aeItem parent, aeItem child, aeItemRelation where child.itm_id = ? and ire_child_itm_id = child.itm_id and ire_parent_itm_id = parent.itm_id and child.itm_rsv_id = rsv_id) " +
			" where rsv_id = " + 
			" (select itm_rsv_id from aeItem where itm_id = ?) ");
		} else {
			SQL.append(" Update fmReservation set rsv_purpose = " + 
			" (select parent.itm_title + ' - ' + child.itm_title from aeItem parent, aeItem child, aeItemRelation where child.itm_id = ? and ire_child_itm_id = child.itm_id and ire_parent_itm_id = parent.itm_id and child.itm_rsv_id = rsv_id) " +
			" where rsv_id = " + 
			" (select itm_rsv_id from aeItem where itm_id = ?) ");
		}
		return SQL.toString();
	}

// Deprecate by prev function
//	  public static final String upd_rsv_purpose_for_item_with_run_level_id = 
//		  " Update fmReservation set rsv_purpose = " + 
//		  " (select parent.itm_title + ' - ' + child.itm_title from aeItem parent, aeItem child, aeItemRelation where child.itm_id = ? and ire_child_itm_id = child.itm_id and ire_parent_itm_id = parent.itm_id and child.itm_rsv_id = rsv_id) " +
//		  " where rsv_id = " + 
//		  " (select itm_rsv_id from aeItem where itm_id = ?) ";
    
    /**
     * added for BJMU
     *
     */
     // for certificate
    public static final String sql_get_certificateLst =
        "select ctf_id,ctf_title, ctf_status, cfn_qualification_ind, cfn_status " +
        "from cfCertificate,cfCertification " +
        "where ctf_id = cfn_ctf_id and ctf_owner_ent_id = cfn_owner_ent_id and cfn_owner_ent_id= ? " +
        //"and cfn_status = ? " +
        "order by ctf_title,ctf_status,cfn_status ";
    public static final String sql_get_cfCertificateStatus =
        "select ctb_id,ctb_title from CodeTable " +
        "where ctb_type = ? " +
        "order by ctb_id ";
    //
    // sql_get_ctf_status_on_num
    public static final String sql_get_ctf_status_on_num = "select count(ctf_id) count_cf from cfCertificate where ctf_status = 'ON' and ctf_owner_ent_id = ? ";
    // sql_get_ctf_status_off_num
    public static final String sql_get_ctf_status_off_num = "select count(ctf_id) count_cf from cfCertificate where ctf_status = 'OFF' and ctf_owner_ent_id = ? ";
    // sql_get_ctf_applied_num
//  public static final String sql_get_ctf_applied_num = "select count(cfn_ctf_id) count_applied,cfn_ctf_id,ctf_title,ctf_status,ctb_id " +
//         "from cfCertification, cfCertificate,CodeTable " +
//         "where ctf_id = cfn_ctf_id and cfn_owner_ent_id = ? and ctf_status = ctb_title ";
    public static final String sql_get_ctf_applied_num_groupby = " group by ctf_title,ctf_id,ctf_status,ctb_id order by ctf_title ";
//  // sql_get_ctf_dtl_num                            select ctf_title,cfn_ctf_id,cfn_status,count(cfn_qualification_ind) cfn_qualified_num
//  public static final String sql_get_ctf_dtl_num = "select ctf_id,ctf_title, cfn_status,count(cfn_ent_id) lrn_num,sum(cfn_qualification_ind) cfn_qualified_num " +
//         " from cfCertificate,cfCertification " +
//         "where ctf_id=cfn_ctf_id and cfn_owner_ent_id = ? ";
    public static final String sql_ctf_status_on = " and ctf_status = 'ON' ";
    public static final String sql_ctf_status_off = " and ctf_status = 'OFF' ";
    public static final String sql_get_ctf_dtl_num_groupby = " group by ctf_id,ctf_title,cfn_status order by ctf_title ";
    // sql for sql_get_ctf_usr_count_certified
    public static final String sql_get_ctf_usr_count_certified = "select count(cfn_ctf_id) count_cf ,ctf_title " +
           "from cfCertificate,cfCertification " +
           "where ctf_id = cfn_ctf_id and cfn_status = 'Certified' and cfn_owner_ent_id = ? and ctf_owner_ent_id = ? and ctf_id = ? " +
           "group by ctf_title ";
    // sql for sql_get_ctf_usr_count_not_certified
    public static final String sql_get_ctf_usr_count_not_certified = "select count(cfn_ctf_id) count_cf ,ctf_title "+
           "from cfCertificate,cfCertification " +
           "where ctf_id = cfn_ctf_id and cfn_status = 'Not Certified' and cfn_owner_ent_id = ? and ctf_owner_ent_id = ? and ctf_id = ? " +
           "group by ctf_title";
    // sql for sql_get_ctf_usr_lst
    public static final String sql_get_ctf_usr_lst = "select ctf_id,ctf_title,ctf_status,cfn_status,cfn_qualification_ind,cfn_upd_timestamp ,usr_ent_id " +
           "from cfCertificate,cfCertification,RegUser,Entity " +
           "where ctf_id = cfn_ctf_id and cfn_ent_id = ent_id and usr_ent_id = ent_id and ctf_id = ? " +
    	   "and ent_delete_usr_id IS NULL and ent_delete_timestamp IS NULL ";
    public static final String sql_get_ctf_ust_lst_status_cert = " and cfn_status = 'Certified' ";
    public static final String sql_get_ctf_ust_lst_status_not_cert = " and cfn_status = 'Not Certified' ";
    public static final String sql_get_ctf_ust_lst_order = " order by usr_display_bil";
    public static final String sql_upd_ctf_status = "update cfCertificate " +
           "set ctf_status = ? , ctf_upd_timestamp = ? , ctf_upd_usr_id = ? " +
           "where ctf_owner_ent_id = ? and ctf_status <> ? and ctf_id in ";
    public static final String sql_del_ctf = "delete from cfCertificate where ctf_id = ? and ctf_owner_ent_id = ?";
    public static final String sql_ins_ctf = "INSERT INTO cfCertificate " +
           "(ctf_title, ctf_status, ctf_link,ctf_owner_ent_id,ctf_create_timestamp,ctf_create_usr_id,ctf_upd_timestamp,ctf_upd_usr_id) " +
           "values (?, ?, ?, ?, ?, ?, ?, ?) ";
    public static final String sql_upd_ctf = "UPDATE cfCertificate " +
           "set ctf_title = ? , ctf_upd_timestamp = ? , ctf_upd_usr_id = ? " +
           "where ctf_owner_ent_id = ? and ctf_id = ? ";
    public static final String sql_upd_cfn_status = "update cfCertification " +
           "set cfn_status = ? , cfn_upd_timestamp = ? , cfn_upd_usr_id = ? " +
           "where cfn_owner_ent_id = ? and cfn_status <> ? and cfn_ctf_id in " ;
    public static final String sql_get_cfn_usr_status = "select cfn_status from cfCertification " +
           "where cfn_ctf_id = ? and cfn_ent_id = ? ";
    public static final String sql_ins_cfn = "insert into cfCertification " +
           "(cfn_ctf_id,cfn_ent_id,cfn_qualification_ind,cfn_status,cfn_owner_ent_id,cfn_create_timestamp,cfn_create_usr_id,cfn_upd_timestamp,cfn_upd_usr_id) " +
           "values (?,?,?,?,?,?,?,?,?) ";
    public static final String sql_del_cfn = "delete from cfCertification where cfn_ctf_id = ? and cfn_ent_id = ? and cfn_owner_ent_id = ? ";
    public static final String sql_del_cfn_ctf_id = "delete from cfCertification where cfn_ctf_id = ? and cfn_owner_ent_id = ? ";
    // added for aeLearningPlan
    public static final String sql_get_user_certification =
        "SELECT ctf_id, ctf_title, ctf_status, cfn_qualification_ind, cfn_status " +
        "FROM cfCertificate, cfCertification " +
        "WHERE ctf_id = ? AND cfn_ent_id = ? AND ctf_id = cfn_ctf_id";
    public static final String sql_get_ctf_title =
           "select ctf_title from cfCertificate where ctf_id = ?";
    public static final String sql_get_ctf_link =
           "select ctf_link from cfCertificate where ctf_id = ?";
    // add for certification aeItem linkage
    public static final String sql_get_ity_ctf_ind =
           "select ity_certificate_ind,itm_ctf_id from aeItemType,aeItem where ity_id = itm_type and itm_id = ?";
    public static final String sql_get_ity_ctf_ind_appID =
           "select ity_certificate_ind,itm_ctf_id,app_ent_id from aeItemType,aeItem,aeApplication where ity_id = itm_type and app_itm_id = itm_id and app_id = ?";
    // add for cf QulifyBatch
    public static final String sql_get_qualify_batch =
           "select cfn_ctf_id,cfn_ent_id from cfCertificate,cfCertification,RegUser where ctf_id = cfn_ctf_id and ctf_status = ? and cfn_qualification_ind = 0 and cfn_ent_id = usr_ent_id";
    public static final String sql_get_qualify_ind =
           "select cfn_qualification_ind from cfCertification where cfn_ctf_id = ? and cfn_ent_id = ?";
    public static final String sql_upd_qualify_ind =
           "update cfCertification set cfn_qualification_ind = ? where cfn_ctf_id = ? and cfn_ent_id = ?";

	/* NETg SQL by Cliff */
	public static final String sql_get_netg_cookie_name =
	    "Select mod_core_vendor " +
	    "From Module " +
	    "Where mod_res_id = ? ";
	
	public static final String sql_get_netg_cookie =
	    "Select mov_core_lesson " + 
	    "From ModuleEvaluation " +
	    "Where mov_cos_id = ? " +
	    "And mov_mod_id = ? " +
	    "And mov_ent_id = ? " +
	    "And mov_tkh_id = ? ";

	public static final String sql_get_netg_status =
	    "Select mov_status " + 
	    "From ModuleEvaluation " +
	    "Where mov_cos_id = ? " +
	    "And mov_mod_id = ? " +
	    "And mov_ent_id = ? " +
	    "And mov_tkh_id = ? ";

	public static final String sql_get_netg_score =
	    "Select mov_score " + 
	    "From ModuleEvaluation " +
	    "Where mov_cos_id = ? " +
	    "And mov_mod_id = ? " +
	    "And mov_ent_id = ? " +
	    "And mov_tkh_id = ? ";
	    
	public static final String sql_upd_netg_cookie =
	    "Update ModuleEvaluation Set " +
	    "mov_total_attempt = mov_total_attempt + 1, " + 
	    "mov_core_lesson = ?, " +
	    "mov_last_acc_datetime = ? " +
	    "Where mov_mod_id = ? " +
	    "And mov_ent_id = ? " +
	    "And mov_cos_id = ? " +
	    "And mov_tkh_id = ? ";

	public static final String sql_upd_netg_tracking =
	    "Update ModuleEvaluation Set " +
	    "mov_score = ?, " +
	    "mov_status = ?, " +
	    "mov_total_time = mov_total_time + ?, " +
	    "mov_last_acc_datetime = ? " +
	    "Where mov_mod_id = ? " +
	    "And mov_ent_id = ? " +
	    "And mov_cos_id = ? " +
	    "And mov_tkh_id = ? ";

	public static final String sql_upd_netg_score =
	    "Update ModuleEvaluation Set " +
	    "mov_score = ?, " +
	    "mov_total_time = mov_total_time + ?, " +
	    "mov_last_acc_datetime = ? " +
	    "Where mov_mod_id = ? " +
	    "And mov_ent_id = ? " + 
	    "And mov_cos_id = ? " +
	    "And mov_tkh_id = ? ";

	public static final String sql_upd_netg_status =
	    "Update ModuleEvaluation Set " +
	    "mov_status = ?, " +
	    "mov_total_time = mov_total_time + ?, " +
	    "mov_last_acc_datetime = ? " +
	    "Where mov_mod_id = ? " +
	    "And mov_ent_id = ? " + 
	    "And mov_cos_id = ? " +
        "And mov_tkh_id = ? ";
        
	public static final String sql_upd_netg_duration =
	    "Update ModuleEvaluation Set " +
	    "mov_total_time = mov_total_time + ?, " +
	    "mov_last_acc_datetime = ? " +
	    "Where mov_mod_id = ? " +
	    "And mov_ent_id = ? " +
	    "And mov_cos_id = ? " +
        "And mov_tkh_id = ? ";
	    
    public static final String sql_ins_netg_obj_tracking =	    
	    "INSERT INTO Accomplishment (apm_ent_id, apm_obj_id, apm_score, apm_status, apm_tkh_id) " +
	    "VALUES (?, ?, ?, ?, ?)";
	    
	public static final String sql_get_netg_obj_id = 
	    "Select obj_id " + 
	    "From Objective, ResourceObjective " +
	    "Where obj_id = rob_obj_id " +
	    "And rob_res_id = ? " +
	    "And obj_developer_id = ? ";

    public static final String sql_count_netg_obj_attempt =	    
	    "Select count(apm_ent_id) " + 
	    "From Accomplishment " +
	    "Where apm_ent_id = ? " +
	    "And apm_obj_id = ? " +
	    "And apm_tkh_id = ? ";

	public static final String sql_get_netg_obj_and_developer_id = 
	    "Select obj_id, obj_developer_id " + 
	    "From Objective, ResourceObjective " +
	    "Where obj_id = rob_obj_id " +
	    "And rob_res_id = ? ";

	public static final String sql_get_netg_attempted_obj_count = 
	    "Select count(distinct apm_obj_id) " +
	    "From Accomplishment " +
	    "Where apm_ent_id = ? and apm_tkh_id = ? and apm_obj_id in ";
	    
	/* NETg SQL by Cliff END*/

    public static final String sql_upd_entity_syn_date = 
        "UPDATE Entity SET ent_syn_date = ? WHERE ent_id = ? ";

    public static final String sql_get_not_syn_usr_ent_id_n_ste_usr_id =         
        "SELECT usr_ste_usr_id, usr_ent_id " + 
        " FROM RegUser, entity  " + 
        " WHERE usr_ste_ent_id = ? " +
        " AND ent_syn_ind = ? " + 
        " AND (ent_syn_date < ?  OR ent_syn_date is null) " + 
        " AND (usr_status = ? OR usr_status = ? ) " + 
        " AND usr_ent_id = ent_id " +
        " AND usr_source = ? " +
        " AND ent_delete_usr_id IS NULL " + 
        " AND ent_delete_timestamp IS NULL ";
 
    public static final String sql_get_not_syn_usr_relation =    
        " SELECT ENTITY_GROUP.ent_ste_uid as GROUP_UID, usr_ste_usr_id as MEMBER_UID, ern_ancestor_ent_id, ern_child_ent_id " +
        " FROM EntityRelation, RegUser , Entity ENTITY_GROUP, Entity ENTITY_MEMBER " +
        " WHERE usr_ste_ent_id = ? " +
        " AND (usr_status = ? OR usr_status = ? )" +
        " AND (usr_not_syn_gpm_type NOT LIKE ? or usr_not_syn_gpm_type IS NULL)" + 
        " AND (ern_syn_timestamp < ? OR ern_syn_timestamp is null)  " +
        " AND ern_type = ? " +
        " AND ern_remain_on_syn = ? " +
        /*
        //  not delete user relation when user did not syn in and syn_ind = false
        " AND ( ENTITY_MEMBER.ent_syn_date >= ? OR ENTITY_MEMBER.ent_syn_ind = ? )" + 
        */
        //  delete user relation only when user just syn in , for upload
        " AND ENTITY_MEMBER.ent_syn_date >= ? " + 
        //  not delete user relation that GROUP did not syn in and GROUP syn_ind = false
        " AND ( ENTITY_GROUP.ent_syn_date >= ? OR ENTITY_GROUP.ent_syn_ind = ? ) " +
        " AND ern_child_ent_id = usr_ent_id " +
        " AND ern_ancestor_ent_id = ENTITY_GROUP.ent_id " + 
        " AND ern_child_ent_id = ENTITY_MEMBER.ent_id " +
        " AND ENTITY_GROUP.ent_delete_usr_id IS NULL " +
        " AND ENTITY_GROUP.ent_delete_timestamp IS NULL " +
        " AND ENTITY_MEMBER.ent_delete_usr_id IS NULL " +
        " AND ENTITY_MEMBER.ent_delete_timestamp IS NULL " +
        " AND ern_parent_ind = ? ";

    public static final String sql_get_not_syn_usergroup_relation = 
        " SELECT ENTITY_GROUP.ent_ste_uid as GROUP_UID, ENTITY_MEMBER.ent_ste_uid as MEMBER_UID, ern_ancestor_ent_id, ern_child_ent_id " + 
        " FROM EntityRelation, UserGroup, Entity ENTITY_GROUP, Entity ENTITY_MEMBER " + 
        " WHERE usg_ent_id_root = ? " + 
        " AND (ern_syn_timestamp < ? OR ern_syn_timestamp is null) " + 
        " and ern_type = ? " + 
        " AND ern_remain_on_syn = ? " + 
        //  not delete user relation when user did not syn in and syn_ind = false
        " AND ( ENTITY_MEMBER.ent_syn_date > ? OR ENTITY_MEMBER.ent_syn_ind = ? )" + 
        //  not delete user relation that GROUP did not syn in and GROUP syn_ind = false
        " AND ( ENTITY_GROUP.ent_syn_date > ? OR ENTITY_GROUP.ent_syn_ind = ? ) " +
        " AND ern_ancestor_ent_id = usg_ent_id " + 
        " AND ern_child_ent_id = ENTITY_MEMBER.ent_id " +
        " AND ern_ancestor_ent_id = ENTITY_GROUP.ent_id " +
        " AND ENTITY_GROUP.ent_delete_usr_id IS NULL " +
        " AND ENTITY_GROUP.ent_delete_timestamp IS NULL " +
        " AND ENTITY_MEMBER.ent_delete_usr_id IS NULL " +
        " AND ENTITY_MEMBER.ent_delete_timestamp IS NULL " + 
        " AND ern_parent_ind = ? ";

    public static final String sql_get_not_syn_usergroup =         
        "SELECT ent_ste_uid, ent_id " + 
        " FROM UserGroup, entity  " + 
        " WHERE usg_ent_id_root = ? " +
        " AND ent_syn_ind = ? " +
        " AND (ent_syn_date < ?  OR ent_syn_date is null) " + 
        " AND usg_role is null " + 
        " AND usg_ent_id = ent_id " +
        " AND ent_delete_usr_id IS NULL " +
        " AND ent_delete_timestamp IS NULL  order by ent_id desc ";

    public static final String sql_get_not_syn_role_target_entity =         
        "SELECT usr_ste_usr_id as USER_UID, ENTITY_GROUP.ent_ste_uid as GROUP_UID, usr_ent_id, ENTITY_GROUP.ent_id ent_id, rte_rol_ext_id, rte_group_id, rte_eff_start_datetime, rte_eff_end_datetime " + 
        " FROM RegUser, Entity ENTITY_USER, UsrRoleTargetEntity , Entity ENTITY_GROUP " + 
        " WHERE usr_ste_ent_id = ? " + 
        " AND (usr_status = ? OR usr_status = ? ) " +
        " AND ( ENTITY_USER.ent_syn_date > ? OR ENTITY_USER.ent_syn_ind = ? )" + 
        " AND usr_syn_rol_ind = ? " +
        " AND (rte_syn_timestamp < ?  OR rte_syn_timestamp is null) " + 
        " AND rte_rol_ext_id = ? " +
        " AND rte_usr_ent_id = usr_ent_id " +
        " AND rte_ent_id = ENTITY_GROUP.ent_id "+
        " AND ENTITY_USER.ent_id = usr_ent_id " +
        " AND ENTITY_GROUP.ent_delete_usr_id IS NULL " +
        " AND ENTITY_GROUP.ent_delete_timestamp IS NULL " +
        " AND ENTITY_USER.ent_delete_usr_id IS NULL " +
        " AND ENTITY_USER.ent_delete_timestamp IS NULL ";
        
    public static final String sql_get_orphan_user = 
        "SELECT usr_ste_usr_id, usr_ent_id " +      
        " FROM RegUser WHERE usr_ste_ent_id = ? " + 
        " AND usr_ent_id NOT IN " + 
        " (SELECT ern_child_ent_id FROM EntityRelation WHERE ern_type = ? AND ern_parent_ind = ? ) " + 
        " AND usr_status = ? ";

    public static final String sql_get_orphan_usg = 
        "SELECT ent_ste_uid, ent_id " +      
        " FROM Entity , UserGroup WHERE usg_ent_id_root = ? " + 
        " AND usg_ent_id = ent_id " + 
        " AND usg_role IS NULL " + 
        " AND usg_ent_id NOT IN " + 
        " (SELECT ern_child_ent_id FROM EntityRelation WHERE ern_type = ? AND ern_parent_ind = ? ) " +
        " AND ent_delete_usr_id IS NULL " +
        " AND ent_delete_timestamp IS NULL ";

    public static final String SQL_GET_OJV = 
        " SELECT ojv_create_usr_id, ojv_create_timestamp " +
        " ,ojv_update_usr_id, ojv_update_timestamp " +
        " ,ojv_option_xml " + 
        " FROM ObjectView " + 
        " WHERE ojv_owner_ent_id = ? " +
        " AND ojv_type = ? " +
        " AND ojv_subtype = ? ";
        
    public static final String sql_get_course_start_end_date = 
        "SELECT itm_content_eff_duration, itm_content_eff_start_datetime, itm_content_eff_end_datetime , att_create_timestamp "
        + " FROM aeitem , aeApplication, aeAttendance "
        + " WHERE itm_id = att_itm_id AND itm_id = app_itm_id AND app_id = att_app_id "
        + " AND app_ent_id = ? AND itm_id = ? "
        + " UNION "
        + " SELECT r.itm_content_eff_duration, r.itm_content_eff_start_datetime, r.itm_content_eff_end_datetime , att_create_timestamp "
        + " FROM aeItem p, aeitem r, aeItemRelation, aeApplication, aeAttendance "
        + " WHERE att_itm_id = r.itm_id AND r.itm_id = app_itm_id AND app_id = att_app_id "
        + " AND r.itm_run_ind = ? "
        + " AND r.itm_id = ire_child_itm_id "
        + " AND p.itm_id = ire_parent_itm_id "
        + " AND app_ent_id = ? AND p.itm_id = ? ";

    public static final String SQL_GET_TARGET_USER_GROUP = 
        " Select rte_usr_ent_id, rte_rol_ext_id, rte_group_id, rte_ent_id " +
        " ,rte_create_timestamp, rte_create_usr_id " +
        " From usrRoleTargetEntity, UserGroup " +
        " Where rte_usr_ent_id = ? " + 
        " And rte_rol_ext_id = ? " + 
        " And usg_ent_id = rte_ent_id " +
        " Order by usg_display_bil asc ";

    public static final String SQL_GET_TARGET_USER = 
        " Select rte_usr_ent_id, rte_rol_ext_id, rte_group_id, rte_ent_id " +
        " ,rte_create_timestamp, rte_create_usr_id " +
        " From usrRoleTargetEntity, RegUser " +
        " Where rte_usr_ent_id = ? " + 
        " And rte_rol_ext_id = ? " + 
        " And usr_ent_id = rte_ent_id " +
        " Order by usr_display_bil asc ";

    // sql for a search a list of user in certain usergroup/usergrade
    public static StringBuffer getUserInGroupSQL(long[] ent_id_lst, String ern_type){
        StringBuffer sql = new StringBuffer();
        
//            sql.append(" AND ").append(usr_column_name).append(" IN (");
        if (ent_id_lst != null && ent_id_lst.length > 0) {
            sql.append(" SELECT ern_child_ent_id FROM EntityRelation WHERE ern_parent_ind = ? AND ern_type = '").append(ern_type).append("' AND ( ");
            for (int i=0; i<ent_id_lst.length; i++) {
                if (i != 0) {
                    sql.append(" OR ");
                }
                sql.append(" ern_ancestor_ent_id = " + ent_id_lst[i] + " OR ern_child_ent_id = " + ent_id_lst[i] + " ");
            }
            sql.append(")");
        }
        //sql.append(" )) ");
        return sql;
    }
    
    
    
    
    public static final String SQL_CM_SKILL_BASE_INS = 
		" Insert Into cmSkillBase " +
    	    "( skb_type, " +
    		"skb_title, " +
    		"skb_description, " +
    		"skb_owner_ent_id, " +
    		"skb_parent_skb_id, " +
    		"skb_ancestor, " +
    		"skb_ssl_id, " +
    		"skb_order, " +
    		"skb_create_usr_id, " +
    		"skb_create_timestamp, " +
    		"skb_update_usr_id, " +
    		"skb_update_timestamp, " +
    		"skb_delete_usr_id, " +
    		"skb_delete_timestamp, " +
    		"skb_ske_id ) " +
    		"Values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?) ";
    
    
	public static final String SQL_CM_SKILL_BASE_UPD = 
			"Update cmSkillBase Set " +
			"skb_type = ?, " +
			"skb_title = ?, " +
			"skb_description = ?, " +
			"skb_owner_ent_id = ?, " +
			"skb_parent_skb_id = ?, " +
			"skb_ancestor = ?, " +
			"skb_ssl_id = ?, " +
			"skb_order = ?, " +
			"skb_update_usr_id = ?, " +
			"skb_update_timestamp = ? " +
			"Where skb_id = ? ";
    
	public static final String SQL_CM_SKILL_BASE_GET_ALL = 
		"Select " +
		"skb_id, " +
		"skb_type, " +
		"skb_title, " +
		"skb_description, " +
		"skb_owner_ent_id, " +
		"skb_parent_skb_id, " +
		"skb_ancestor, " +
		"skb_ssl_id, " +
		"skb_order, " +
		"skb_create_usr_id, " +
		"skb_create_timestamp, " +
		"skb_update_usr_id, " +
		"skb_update_timestamp, " +
		"skb_delete_usr_id, " +
		"skb_delete_timestamp " +
		"From cmSkillBase " +
		"Where skb_id = ? ";
	
	public static final String SQL_CM_SKILL_TREE_NODE_INS = 
		"Insert Into cmSkillTreeNode ( stn_skb_id ) Values ( ? )";
	
	public static final String SQL_CM_SKILL_TREE_NODE_GET_ALL = 
		"Select stn_skb_id From cmSkillTreeNode Where stn_skb_id = ? ";
		
	public static final String SQL_CM_SKILL_BASE_GET_ANCESTOR = 
		"Select skb_ancestor From cmSkillBase Where skb_id = ? ";
	
	public static final String SQL_CM_SKILL_BASE_UPDATE_ANCESTOR =
		"Update cmSkillBase Set skb_ancestor = ? Where skb_id = ? ";
				
	public static final String SQL_CM_SKILL_BASE_GET_CHILD_ID =
		"Select skb_id, skb_delete_usr_id " +
		"From cmSkillBase " +
		"Where skb_parent_skb_id = ? " +
		"And skb_delete_usr_id Is Null " +
		"And skb_delete_timestamp Is Null ";
		
	public static final String SQL_CM_SKILL_BASE_SOFT_DEL = 
		"Update cmSkillBase Set " +
		"skb_delete_usr_id = ?, " +
		"skb_delete_timestamp = ?, " +
		"skb_ssl_id = ? " +
		"Where skb_id = ? ";
	
	public static final String SQL_CM_SKILL_BASE_DEL = 
		"Delete FROM cmSkillBase Where skb_id = ? ";
	
	public static final String SQL_CM_SKILL_TREE_NODE_DEL = 
		"Delete FROM cmSkillTreeNode Where stn_skb_id = ? ";
		
	public static final String SQL_CM_SKILL_BASE_GET_SCALE_ID =
		"Select skb_ssl_id From cmSkillBase Where skb_id = ? ";		
		
		
	public static final String SQL_CM_SKILL_BASE_GET_SKILL_ID_BY_SCALE_ID = 
		"SELECT skb_id " +
		"From cmSkillBase " +
		"Where skb_ssl_id = ? " +
		"And skb_delete_usr_id Is Null ";
		
	public static final String SQL_CM_SKILL_BASE_GET_SOFT_DELETED_SKILL_ID_BY_SCALE_ID = 
		"SELECT skb_id " +
		"From cmSkillBase " +
		"Where skb_ssl_id = ? " +
		"And skb_delete_usr_id Is Not Null ";
		
	public static final String SQL_CM_SKILL_BASE_GET_BY_ID_VEC =
		"Select skb_id, " +
		"skb_title, " +
		"skb_description, " +
		"skb_type, " +
		"skb_create_usr_id, " +
		"skb_create_timestamp, " +
		"skb_update_usr_id, " +
		"skb_update_timestamp " +
		"From cmSkillBase " +
		"Where skb_id In ";
	

	public static final String SQL_CM_SKILL_BASE_GET_SKILL_LIST_TITLE =
		"Select skb_id, skb_title From cmSkillBase Where skb_id In ";


	public static final String SQL_CM_SKILL_BASE_CHECK_EXISTENCE = 
		"SELECT skb_id FROM cmSkillBase " +
		"WHERE skb_id = ? AND skb_delete_usr_id Is Null ";
		
	public static final String SQL_CM_SKILL_BASE_GET_UPDATE_TIMESTAMP = 
		"Select skb_update_timestamp From cmSkillBase Where skb_id = ? ";		
		
		
	public static final String SQL_CM_SKILL_GET_DETAIL = 
		"SELECT skb_id, " +
		"skb_title, " +
		"skb_description, " +
		"skb_ssl_id, " +
		"skb_parent_skb_id, " +
		"skl_xml, " +
		"skb_type, " +
		"skb_ancestor, " +
		"skl_derive_rule, " +
		"skb_owner_ent_id, " +
		"skb_create_usr_id, " +
		"skb_create_timestamp, " +
		"skb_update_usr_id, " +
		"skb_update_timestamp, " +
		"skb_delete_usr_id, " +
		"skb_delete_timestamp, " +
		"skl_rating_ind " +
		"FROM cmSkillBase, cmSkill " +
		"Where skb_id = skl_skb_id ";
		
		
	public static final String SQL_CM_SKILL_GET_CHILD_SKILL_ID_BY_TYPE = 
		"Select skb_id " +
		"From cmSkillBase " +
		"Where skb_parent_skb_id = ? " +
		"And skb_type = ? " +
		"And skb_delete_usr_id Is Null"; 
 		
	public static final String SQL_CM_SKILL_GET_CHILD_SKILL_ID_BY_RATING_IND = 
		"Select skb_id " +
		"From cmSkillBase, cmSkill " +
		"Where skb_parent_skb_id = ? " +
		"And skb_id = skl_skb_id " +
		"And skl_rating_ind = ? " +
		"And skb_delete_usr_id Is Null"; 
		
		
	public static final String SQL_CM_SKILL_BASE_UPD_SUCCESSOR_SCALE =  
		"Update cmSkillBase Set skb_ssl_id = ? " +
		"WHERE skb_ancestor Like ? And skb_delete_usr_id Is Null";
	
	
	public static final String SQL_CM_SKILL_BASE_CHECK_TITLE_AND_TYPE =
		"Select COUNT(skb_title) " +
		"From cmSkillBase " +
		"Where skb_title = ? " +
		"And skb_type = ? " +
		"And skb_owner_ent_id = ? " +
		"And skb_delete_usr_id Is Null"; 
	
	
	public static final String SQL_CM_SKILL_TREE_NODE_GET_TITLE_LIST = 
		"Select skb_id, skb_title " +
		"From cmSkillBase " +
		"Where skb_type = ? " +
		"And skb_owner_ent_id = ? " +
		"And skb_parent_skb_id Is Null " +
		"And skb_delete_usr_id Is Null " +
		"Order By skb_title Asc";


	public static final String SQL_CM_TREE_NODE_GET_CHILD_SKILL_BASE =
		"Select skb_id, " +
		"skb_type, " +
		"skb_title, " +
		"skb_description, " +
		"skb_ssl_id, " +
		"skl_xml, " +
		"skl_derive_rule, " +
		"skl_rating_ind " +
		"From cmSkillBase, cmSkill " +
		"Where skb_id = skl_skb_id " +
		"And skb_parent_skb_id = ? " +
		"And skb_delete_usr_id Is Null " +
		"And skb_type = ? ";

	public static final String SQL_CM_SKILL_BASE_SOFT_DELETE_CHILD = 
		"Update cmSkillBase " +
		"Set skb_delete_usr_id = ?, " +
		"skb_delete_timestamp = ?, " +
		"skb_ssl_id = ? " +
		"Where skb_parent_skb_id = ? ";


	public static final String SQL_CM_SKILL_BASE_UPD_COMPETENCY =
		"Update cmSkillBase " +
		"Set skb_title = ?, " +
		"skb_description = ?, " +
		"skb_update_usr_id = ?, " +
		"skb_update_timestamp = ? " +
		"Where skb_id = ?";


	public static final String SQL_CM_SKILL_BASE_UPD_BEHAVIOR =
		"Update cmSkillBase " +
		"Set skb_title = ?, " +
		"skb_description = ?, " +
		"skb_order = ?, " +
		"skb_update_usr_id = ?, " +
		"skb_update_timestamp = ? " +
		"Where skb_id = ?";
		
	public static final String SQL_CM_SKILL_UPD_RATING_IND = 
		"Update cmSkill Set skl_rating_ind = ? Where skl_skb_id = ?";		
		
	public static final String SQL_CM_SKILL_BASE_GET_ANCESTOR_ID_LIST =
		"Select skb_ancestor From cmSkillBase Where skb_id = ? ";
		
	
	public static final String SQL_CM_SKILL_GET_CHILD_DESC = 
		"Select skb_id, skb_description From cmSkillBase Where skb_parent_skb_id = ? Order By skb_order Asc";
	
	
	public static final String SQL_CM_SKILL_BASE_GET_TYPE_LIST = 
		"Select skb_id, skb_type From cmSkillBase Where skb_id IN ";

    public static final String SQL_SPT_GET_APPR_GROUP = 
        " select ent_id, ent_type, ent_upd_date, ent_syn_date, " +
        " ent_ste_uid, ent_syn_ind, " +
        " usg_ent_id, usg_code, usg_name, usg_display_bil, usg_level, " +
        " usg_usr_id_admin, usg_role, usg_ent_id_root, usg_desc " +
        " from Entity, UserGroup, SuperviseTargetEntity " +
        " where spt_source_usr_ent_id = ? " +
        " and spt_target_ent_id = usg_ent_id " +
        " and usg_ent_id = ent_id " +
        " and spt_eff_start_datetime <= ? " +
        " and spt_eff_end_datetime >= ? " +
        " and ent_delete_usr_id is null ";

    public static final String SQL_GET_DEFAULT_UGR = 
        " SELECT ugr_ent_id, ugr_display_bil, ugr_ent_id_root, ugr_type FROM UserGrade " +
        " WHERE ugr_default_ind = ? and ugr_ent_id_root = ? ";
        
    // for copy organization
    public static final String sql_get_site_ent_id = "Select ste_ent_id from acSite Where ste_id = ?";
    public static final String sql_clone_entity = "Insert into Entity (ent_type, ent_upd_date, ent_syn_date, ent_ste_uid, ent_syn_ind, ent_delete_usr_id, ent_delete_timestamp) "
                                                    + "Select ent_type, ent_upd_date, ent_syn_date, ent_ste_uid, ent_syn_ind, ent_delete_usr_id, ent_delete_timestamp "
                                                    + "From Entity "
                                                    + "Where ent_id = ?";
    public static final String sql_get_latest_ent_id = "Select max(ent_id) from Entity";
    public static final String sql_get_syl_id = "Select syl_id from Syllabus Where syl_ent_id_root = ?";
    public static final String sql_get_acrole = "Select "
                                        + "rol_id, rol_ext_id, rol_seq_id, "
                                        + "rol_ste_ent_id, rol_url_home, rol_creation_timestamp, "
                                        + "rol_xml, rol_ste_default_ind, rol_report_ind, "
                                        + "rol_skin_root, rol_status, rol_ste_uid, "
                                        + "rol_target_ent_type, rol_auth_level "
                                        + "from acRole Where rol_ste_ent_id = ?";
    public static final String sql_upd_acrole = "Update acRole set rol_ext_id = ?, rol_xml = ? Where rol_id = ?";
    public static final String sql_get_ac_hom_ftn_ext_id = "select ac_hom_ftn_ext_id from acRole, acHomepage where rol_ext_id = ac_hom_rol_ext_id and rol_ext_id = ?";
    public static final String sql_get_site_guest_ent_id = "Select ste_guest_ent_id from acSite Where ste_ent_id = ?";
    public static final String sql_get_sys_user = "Select usr_ent_id, usr_ste_usr_id from RegUser Where usr_ste_ent_id = ? and usr_status = ? order by usr_ste_usr_id";
    public static final String sql_get_usr_id = "Select usr_id from RegUser where usr_ent_id = ?";
    public static final String sql_upd_acsite_guest_ent_id = "Update acSite set ste_guest_ent_id = ? where ste_ent_id = ?";
    public static final String sql_get_user_grade = "Select ugr_ent_id, ugr_display_bil, ugr_ent_id_root, ugr_type, ugr_seq_no From UserGrade Where ugr_ent_id_root = ? and (ugr_type = ? or ugr_defalut_ind = ?)";
    //<Christ>
    public static final String sql_upd_acsite_ste_default_sys_ent_id = "Update acSite set ste_default_sys_ent_id = ? where ste_ent_id = ?";
    public static final String sql_get_user_grade_id = "select ugr_ent_id from UserGrade where ugr_ent_id_root = ?";
    
    public static final String sql_max_usg_ent_id = "select max(usg_ent_id) from UserGrade";
    public static final String sql_get_grade_group_member_frm_member_id = "Select ern_ancestor_ent_id From EntityRelation where ern_child_ent_id = ? and ern_type = ?";
    //</Christ>
    public static final String sql_get_group_member_frm_member_id = "Select * From EntityRelation where ern_child_ent_id = ?";
    
    public static final String sql_get_aetemplate = "Select "
                                            + "tpl_id, tpl_ttp_id, tpl_title, "
                                            + "tpl_xml, tpl_owner_ent_id, tpl_create_timestamp, "
                                            + "tpl_create_usr_id, tpl_upd_timestamp, tpl_upd_usr_id, "
                                            + "tpl_approval_ind "
                                            + "From aeTemplate Where tpl_owner_ent_id = ?";
    public static final String sql_get_latest_aetemplate_id = "Select max(tpl_id) from aeTemplate";
    public static final String sql_get_latest_role_id = "Select max(rol_id) from acRole";
    public static final String sql_get_entity_role_id = "Select erl_rol_id from acEntityRole where erl_ent_id = ?";
    public static final String sql_get_report_template = "Select rte_id from ReportTemplate where rte_owner_ent_id = ?";
    public static final String sql_get_ac_report_template = "Select "
                                                    + "ac_rte_id, ac_rte_ent_id, ac_rte_rol_ext_id, "
                                                    + "ac_rte_ftn_ext_id, ac_rte_owner_ind, ac_rte_create_usr_id, "
                                                    + "ac_rte_create_timestamp "
                                                    + "From acReportTemplate where ac_rte_id = ?";
    public static final String sql_get_role_ftn_ext_id = "Select ftn_ext_id from acRole, acRoleFunction, acFunction where rfn_rol_id = rol_id and rfn_ftn_id = ftn_id and rol_ext_id = ?";
    public static final String sql_get_latest_report_template_id = "Select max(rte_id) from ReportTemplate";
    public static final String sql_get_report_spec = "Select rsp_id, rsp_rte_id from ReportSpec where rsp_ent_id is Null and rsp_rte_id in ";
    
    public static final String SQL_GET_GROUP_SUPERVISORS 
        = " SELECT usr_ent_id, usr_ste_usr_id, usr_display_bil, "
        + " usr_status, spt_eff_start_datetime, spt_eff_end_datetime "
        + " FROM SuperviseTargetEntity, RegUser "
        + " WHERE spt_target_ent_id = ? "
        + " AND spt_eff_start_datetime < ? "
        + " AND spt_eff_end_datetime > ? "
        + " AND spt_source_usr_ent_id = usr_ent_id "
        + " AND spt_type = ? "
        + " ORDER BY usr_display_bil ASC ";

    //for ObjectiveAccess
    public static final String SQL_INS_OAC = " INSERT INTO ObjectiveAccess (oac_obj_id, oac_access_type, oac_ent_id) VALUES (?, ?, ?) ";
    public static final String SQL_GET_OAC = " SELECT oac_obj_id , oac_access_type, oac_ent_id FROM ObjectiveAccess where oac_obj_id = ? and oac_access_type = ?";
    public static final String SQL_DEL_OAC = "DELETE FROM ObjectiveAccess WHERE oac_obj_id = ? ";

    //********** Blueprint Begin
    public static final String sql_get_blp = "SELECT blp_ste_ent_id, blp_src_type, blp_source, blp_path,"
            + " blp_create_timestamp, blp_create_usr_id,"
            + " blp_update_timestamp, blp_update_usr_id, usr_display_bil"
            + " FROM bpBlueprint,reguser"
            + " where blp_update_usr_id=usr_id and blp_ste_ent_id = ? ";

    public static final String sql_ins_blp = "INSERT INTO bpBlueprint(blp_ste_ent_id, blp_src_type, blp_source, blp_path, blp_create_timestamp, blp_create_usr_id, blp_update_timestamp, blp_update_usr_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String sql_upd_blp = "UPDATE bpBlueprint SET blp_src_type=?, blp_source=?, blp_path=?, blp_update_timestamp=?, blp_update_usr_id=? WHERE blp_ste_ent_id=?";

    public static final String sql_del_blp = "DELETE FROM bpBlueprint WHERE blp_ste_ent_id=?";

    public static final String sql_get_updateTimestamp = "SELECT blp_update_timestamp from bpBlueprint WHERE blp_ste_ent_id=?";
    //*********** Blueprint End

    public static final String SQL_GET_TCR_BY_OFFICER 
        = " select tcr_id, tcr_title, tcr_ste_ent_id, tcr_status, tcr_create_timestamp, tcr_create_usr_id, tcr_update_usr_id, tcr_update_timestamp "
        + " from tcTrainingCenterOfficer, tcTrainingCenter "
        + " where tco_usr_ent_id = ? "
        + " and tco_rol_ext_id = ? "
        + " and tcr_status = ? "
        + " and tco_tcr_id = tcr_id "
        + " order by tcr_title ";

    public static final String SQL_GET_TCR_BY_OFFICER_WITH_SUBTC 
	    = " select t.tcr_id, t.tcr_title, t.tcr_ste_ent_id, t.tcr_status, t.tcr_create_timestamp, t.tcr_create_usr_id, t.tcr_update_usr_id, t.tcr_update_timestamp "
	    + " from tcTrainingCenter t"
	    + " where exists ( select * from tcTrainingCenter ancestor " 
	    + " inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) " 
	    + " left join tcRelation on (tcn_ancestor = ancestor.tcr_id) "
	    + " inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id)" 
	    + " where tco_usr_ent_id =  ? "
	    + " and child.tcr_id = t.tcr_id "
	    + " and tco_rol_ext_id = ? ) and t.tcr_status = ? "
	    + " order by t.tcr_title ";
    
    public static final String SQL_GET_TCR_BY_TARGET_USR
        = " select distinct tcr_id, tcr_title, tcr_ste_ent_id, tcr_status, tcr_create_timestamp, tcr_create_usr_id, tcr_update_usr_id, tcr_update_timestamp,tcr_parent_tcr_id "
        + " from EntityRelation, tcTrainingCenterTargetEntity, tcTrainingCenter "
        + " where ern_ancestor_ent_id = tce_ent_id "
        + " and ern_child_ent_id = ? "
        + " and ern_type = ? "
        + " and tce_tcr_id = tcr_id";

    public static final String sql_get_dynamic_que_container_score =
        " SELECT qcs_score, qcs_qcount from QueContainerSpec where qcs_res_id = ? ";

    public static final String sql_get_fixed_que_container_score =
        " SELECT que_score,rcn_score_multiplier FROM ResourceContent,Question "
            + " where rcn_res_id = ? and que_res_id=rcn_res_id_content ";

    /* Question Bank */
    public static final String sql_get_que_container_spec =
        " SELECT "
            + "         qcs_type "
            + "         , qcs_score "
            + "         , qcs_difficulty "
            + "         , qcs_privilege "
            + "         , qcs_duration "
            + "         , qcs_qcount "
            + "         , qcs_res_id "
            + "         , qcs_obj_id "
            + "         , qcs_create_timestamp "
            + "         , qcs_create_usr_id "
            + "         , qcs_update_timestamp "
            + "         , qcs_update_usr_id "
            + " FROM QueContainerSpec WHERE qcs_id = ? ";

    public static final String sql_get_que_container_spec_fr_res_obj_id =
        " SELECT "
            + "         qcs_type "
            + "         , qcs_score "
            + "         , qcs_difficulty "
            + "         , qcs_privilege "
            + "         , qcs_duration "
            + "         , qcs_qcount "
            + "         , qcs_id "
            + "         , qcs_create_timestamp "
            + "         , qcs_create_usr_id "
            + "         , qcs_update_timestamp "
            + "         , qcs_update_usr_id "
            + " FROM QueContainerSpec WHERE qcs_res_id = ? and qcs_obj_id = ?";

    public static final String sql_get_que_container_specs =
        " SELECT qcs_id "
            + " , qcs_obj_id "
            + " , qcs_type "
            + " , qcs_score "
            + " , qcs_difficulty "
            + " , qcs_privilege "
            + " , qcs_duration "
            + " , qcs_qcount "
            + "         , qcs_create_timestamp "
            + "         , qcs_create_usr_id "
            + "         , qcs_update_timestamp "
            + "         , qcs_update_usr_id "
            + " FROM QueContainerSpec "
            + " WHERE qcs_res_id = ? ";
    public static final String sql_ins_que_container_spec =
        " INSERT INTO QueContainerSpec "
            + "         ( qcs_type "
            + "         , qcs_score "
            + "         , qcs_difficulty "
            + "         , qcs_privilege "
            + "         , qcs_duration "
            + "         , qcs_qcount "
            + "         , qcs_res_id "
            + "         , qcs_obj_id "
            + "         , qcs_create_timestamp "
            + "         , qcs_create_usr_id "
            + "         , qcs_update_timestamp "
            + "         , qcs_update_usr_id ) "
            + "      VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ";

    public static final String sql_upd_que_container_spec =
        " UPDATE QueContainerSpec SET "
            + "         qcs_type = ? "
            + "         , qcs_score = ? "
            + "         , qcs_difficulty = ? "
            + "         , qcs_privilege = ? "
            + "         , qcs_duration = ? "
            + "         , qcs_qcount = ? "
            + "         , qcs_update_timestamp = ? "
            + "         , qcs_update_usr_id = ? "
            + "     WHERE qcs_id = ? ";

    public static final String sql_del_que_container_spec =
        " DELETE from QueContainerSpec " + "     WHERE qcs_id = ? ";

    public static final String sql_del_que_container_spec_frm_res_id =
        " DELETE from QueContainerSpec " + "     WHERE qcs_res_id = ? ";

    public static final String sql_del_que_container_spec_frm_res_obj_id =
        " DELETE from QueContainerSpec "
            + "     WHERE qcs_res_id = ? and qcs_obj_id = ? ";

    public static final String sql_get_dynamic_scenario_child_que_id =
        "Select res_id "
            + "From ResourceContent, Resources, Question "
            + "Where rcn_res_id = ? "
            + "And rcn_res_id_content = res_id "
            + "And res_status = ? "
            + "And res_id = que_res_id "
            + "And que_score = ? ";

    public static final String sql_get_child_que_id =
        "Select res_id "
            + "From ResourceContent, Resources "
            + "Where rcn_res_id = ? "
            + "And rcn_res_id_content = res_id "
            + "And res_status = ? "
            + "And rcn_tkh_id = -1 "
            + "ORDER BY rcn_order ASC, res_title ASC";

    public static final String sql_get_container_questions =
        " SELECT que_res_id, que_xml, que_score, que_type "
            + " ,que_int_count, que_prog_lang, que_media_ind "
            + " ,res_id, res_desc, res_title, res_lan, res_type, res_subtype "
            + " ,res_annotation, res_format, res_difficulty, res_duration "
            + " ,res_privilege, res_usr_id_owner, res_tpl_name, res_res_id_root "
            + " ,res_mod_res_id_test, res_status, res_create_date, res_upd_user "
            + " ,res_upd_date, res_src_type, res_src_link, res_code "
            + " ,res_instructor_name, res_instructor_organization "
            + " FROM Question, ResourceContent, Resources "
            + " WHERE que_res_id = rcn_res_id_content "
            + " AND rcn_res_id = ? "
            + " AND que_res_id = res_id ";

    public static final String sql_upd_qct_shuffle_ind =
        " UPDATE QueContainer "
            + " SET qct_allow_shuffle_ind = ? "
            + " WHERE qct_res_id = ? ";

    public static final String sql_get_container_specified_questions =
        " SELECT que_res_id, que_xml, que_score, que_type "
            + " ,que_int_count, que_prog_lang, que_media_ind "
            + " ,res_id, res_desc, res_title, res_lan, res_type, res_subtype "
            + " ,res_annotation, res_format, res_difficulty, res_duration "
            + " ,res_privilege, res_usr_id_owner, res_tpl_name, res_res_id_root "
            + " ,res_mod_res_id_test, res_status, res_create_date, res_upd_user "
            + " ,res_upd_date, res_src_type, res_src_link, res_code "
            + " ,res_instructor_name, res_instructor_organization "
            + " FROM Question, Resources "
            + " WHERE que_res_id = res_id And que_res_id IN ";

    public static final String SQL_VALID_NEW_DSC_SPEC =
        " Select count(*) From QueContainerSpec "
            + " Where qcs_res_id = ? "
            + " And qcs_score = ? ";

    public static final String SQL_VALID_EXIST_DSC_SPEC =
        " Select count(*) From QueContainerSpec "
            + " Where qcs_res_id = ? "
            + " And qcs_score = ? "
            + " And qcs_id <> ? ";

    public static final String SQL_DEL_DSC_ALL_SPEC =
         " Delete From QueContainerSpec "
             + " Where qcs_res_id = ? "
             + " And qcs_type = ? ";
    
    public static final String SQL_GET_TCR_BY_ID_SPEC =
    	     " select t.tcr_id, t.tcr_title, t.tcr_ste_ent_id, t.tcr_status, t.tcr_create_timestamp, t.tcr_create_usr_id, t.tcr_update_usr_id, t.tcr_update_timestamp "
    	    	    + " from tcTrainingCenter t"
    	    	    + " where t.tcr_id = ? and tcr_status =? " ;

    public static String SQL_MOD_RPT(Connection con, loginProfile prof, boolean isCount, Hashtable spec_pairs, Vector vtChildModId, Vector entId_lst, boolean tc_enabled) throws SQLException {
        StringBuffer mod_rpt_sql = new StringBuffer();
        Vector content_lst = new Vector();
        Vector usr_content_lst = new Vector();
        Vector mod_id_lst = new Vector();
        Vector ent_id_lst = new Vector();
        String tmp_modId_table = "";
        String tmp_usrId_table = "";
        String lrn_scope_sql = null;
        if(tc_enabled) {
        	boolean all_user_ind = false;
        	boolean answer_for_lrn = false;
        	boolean answer_for_course_lrn = false;
        	Vector spec_values = new Vector();
        	if(spec_pairs.containsKey("all_user_ind")) {
        		spec_values = (Vector)spec_pairs.get("all_user_ind");
        		if(((String)spec_values.get(0)).equals("1")) {
        			all_user_ind = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_lrn")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_lrn");
         		if(((String)spec_values.get(0)).equals("1")) {
         			answer_for_lrn = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_course_lrn")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_course_lrn");
         		if(((String)spec_values.get(0)).equals("1")) {
         			answer_for_course_lrn = true;
        		}
        	}
        	if(all_user_ind) {
//        		Vector vec = new Vector();
        		if((answer_for_lrn && answer_for_course_lrn )||(!answer_for_lrn && !answer_for_course_lrn )) {
//        			ent_id_lst = ViewLearnerReport.getAllLrn(con, prof.usr_ent_id, prof.root_ent_id);
        			lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
        		} else if (answer_for_lrn && !answer_for_course_lrn) {
//        			ent_id_lst = ViewLearnerReport.getMyRspLrn(con, prof.usr_ent_id, prof.root_ent_id);
        			lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
        		} else if (!answer_for_lrn && answer_for_course_lrn) {
//        			ent_id_lst = ViewLearnerReport.getMyRspCosEnrollLrn(con, prof.usr_ent_id, prof.root_ent_id);
        			lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
        		}
/*        		if(ent_id_lst.size() ==0){
        			ent_id_lst.add(new Long(0));
        		}*/
        	}
        }
        if (entId_lst.size() > 0) {
            ent_id_lst = entId_lst;
        }
        if (spec_pairs.containsKey("usg_ent_id")) {
            Vector vt = (Vector) spec_pairs.get("usg_ent_id");
            for (int i = 0; i < vt.size(); i++) {
                ent_id_lst.addElement(new Long((String) vt.get(i)));
            }
        }
        if (spec_pairs.containsKey("usr_ent_id")) {
            Vector vt = (Vector) spec_pairs.get("usr_ent_id");
            for (int i = 0; i < vt.size(); i++) {
                ent_id_lst.addElement(new Long((String) vt.get(i)));
            }
        }
        //frist create table 
        if(ent_id_lst != null && ent_id_lst.size() > 0 ) {
            String colName = "tmp_usr_id";
            tmp_usrId_table = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        }
        if (spec_pairs.containsKey("mod_id")) {
            Vector vt = (Vector) spec_pairs.get("mod_id");
            for (int i = 0; i < vt.size(); i++) {
                mod_id_lst.addElement(new Long((String) vt.get(i)));
            }
            //如果选择的是统一内容的课程模块，加入所选模块的子模块
            if (vtChildModId.size() > 0) {
                mod_id_lst.addAll(vtChildModId);
            }
            String colName = "tmp_mod_id";
            tmp_modId_table = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tmp_modId_table, mod_id_lst, cwSQL.COL_TYPE_LONG);
        }

        if(ent_id_lst != null && ent_id_lst.size() > 0 ) {
            String colName = "tmp_usr_id";
            cwSQL.insertSimpleTempTable(con, tmp_usrId_table, ent_id_lst, cwSQL.COL_TYPE_LONG);
        }
        if (tmp_usrId_table.equals("") && lrn_scope_sql != null) {
            tmp_usrId_table = cwSQL.createSimpleTemptable(con, "tmp_usr_id", cwSQL.COL_TYPE_LONG, 0, lrn_scope_sql);
        }
        if (isCount) {
            mod_rpt_sql.append("select count(*) as rpt_count ");
        } else {
            mod_rpt_sql.append("select usr_ent_id");
            if (spec_pairs.containsKey("usr_content_lst")) {
                usr_content_lst = (Vector) spec_pairs.get("usr_content_lst");
            }
            if (spec_pairs.containsKey("content_lst")) {
                content_lst = (Vector) spec_pairs.get("content_lst");
            }
            for (int i = 0; i < usr_content_lst.size(); i++) {
                if (((String) usr_content_lst.get(i)).equals("USR_PARENT_USG")) {
                    mod_rpt_sql.append(", ErnUsg.ern_ancestor_ent_id ern_usg_id, ErhUsg.erh_ancestor_ent_id erh_usg_id");
                } else if (((String) usr_content_lst.get(i)).equals("USR_CURRENT_UGR")) {
                    mod_rpt_sql.append(", ErnUgr.ern_ancestor_ent_id ern_ugr_id, ErhUgr.erh_ancestor_ent_id erh_ugr_id");
                } else if (((String) usr_content_lst.get(i)).equals("usr_id")) {
                    mod_rpt_sql.append(", usr_ste_usr_id");
                } else {
                    mod_rpt_sql.append(", " + usr_content_lst.get(i));
                }
            }
            for (int j = 0; j < content_lst.size(); j++) {
                if (((String) content_lst.get(j)).equals("att_status")) {
                    mod_rpt_sql.append(", att_ats_id ");
                } else if (((String) content_lst.get(j)).equals("att_create_timestamp")) {
                    mod_rpt_sql.append(", att_create_timestamp");
                } else {
                    mod_rpt_sql.append(", " + content_lst.get(j));
                }
            }
            mod_rpt_sql.append(", mod_mod_res_id_parent, mod_type, mod_max_score");
            mod_rpt_sql.append(", mov_status, mov_score, mov_mod_id, mov_tkh_id");
            mod_rpt_sql.append(", pgr_grade");

        }
        mod_rpt_sql.append(" from ModuleEvaluation");
        mod_rpt_sql.append(" inner join Module on (mov_mod_id = mod_res_id) ");
        mod_rpt_sql.append("inner join RegUser on (mov_ent_id = usr_ent_id and usr_ste_ent_id =? )");
        mod_rpt_sql.append("inner join Entity on (ent_id = usr_ent_id)");
        mod_rpt_sql.append("inner join aeApplication on (mov_tkh_id = app_tkh_id)");
        mod_rpt_sql.append("inner join aeAttendance on (app_id = att_app_id ");
        if (spec_pairs.containsKey("att_create_start_datetime")) {
            mod_rpt_sql.append("and att_create_timestamp >= ? ");
        }
        if (spec_pairs.containsKey("att_create_end_datetime")) {
            mod_rpt_sql.append(" and att_create_timestamp <= ? ");
        }
        if (spec_pairs.containsKey("ats_id")) {
            Vector vt = (Vector) spec_pairs.get("ats_id");
            String s = (String) vt.get(0);
            if(!s.equals("0")){
            mod_rpt_sql.append(" and att_ats_id in ( ");
            for(int i=0;i<vt.size();i++){
            	if(i==0){
            		mod_rpt_sql.append(vt.get(i));
            	}else{
            		mod_rpt_sql.append(" , " + vt.get(i));
            		
            	}
            }
            mod_rpt_sql.append(" ) ");
            }
//            if (!s.equals("0")) {
//                mod_rpt_sql.append(" and att_ats_id =" + s);
//            }
        }
        mod_rpt_sql.append(")");
        mod_rpt_sql.append(" left join EntityRelation ErnUsg on ");
        mod_rpt_sql.append(" (mov_ent_id = ErnUsg.ern_child_ent_id and ErnUsg.ern_type = 'USR_PARENT_USG' and ErnUsg.ern_parent_ind=1) ");
        mod_rpt_sql.append(" left join EntityRelationHistory ErhUsg on ");
        mod_rpt_sql.append(" (mov_ent_id = ErhUsg.erh_child_ent_id and ErhUsg.erh_type = 'USR_PARENT_USG' and ErhUsg.erh_parent_ind=1 and ErhUsg.erh_end_timestamp = ent_delete_timestamp)");
        mod_rpt_sql.append(" left join EntityRelation ErnUgr on ");
        mod_rpt_sql.append(" (mov_ent_id = ErnUgr.ern_child_ent_id and ErnUgr.ern_type = 'USR_CURRENT_UGR' and ErnUgr.ern_parent_ind=1) ");
        mod_rpt_sql.append(" left join EntityRelationHistory ErhUgr on ");
        mod_rpt_sql.append(" (mov_ent_id = ErhUgr.erh_child_ent_id and ErhUgr.erh_type = 'USR_CURRENT_UGR' and ErhUgr.erh_parent_ind = 1 and ErhUgr.erh_end_timestamp = ent_delete_timestamp)");
        mod_rpt_sql.append(" left join Progress on (mov_tkh_id = pgr_tkh_id and mov_mod_id = pgr_res_id)");
        mod_rpt_sql.append(" where 1=1 ");
        if (!tmp_modId_table.equals("")) {
            mod_rpt_sql.append(" and mov_mod_id in (select tmp_mod_id from " + tmp_modId_table + ")");
        }
        if (!tmp_usrId_table.equals("")) {
            mod_rpt_sql.append(" and usr_ent_id in ( select tmp_usr_id from " + tmp_usrId_table + ")");
        }
        if (!isCount) {
            String sort_col = (String) ((Vector) spec_pairs.get("sort_col")).get(0);
            if (sort_col.equals("att_status")) {
                sort_col = "att_ats_id";
            }
            if (sort_col.equals("usr_id")) {
                sort_col = "usr_ste_usr_id";
            }
            mod_rpt_sql.append("order by ").append(sort_col).append(" ").append((String) ((Vector) spec_pairs.get("sort_order")).get(0)).append(", mov_tkh_id ");
        }
        return mod_rpt_sql.toString();
    }
 
    public static String SQLCOSIDBYCLSID = "select ire_parent_itm_id from aeItemRelation where ire_child_itm_id = ? ";
    
    public static String get_reference_lst_sql(Connection con) throws SQLException {
    	String sql = "SELECT "
            + " ref_id,"
            + " ref_res_id,"
            + " ref_type,"
            + " ref_title,"
            + " ref_description,"
            + " ref_url,"
            + " ref_create_usr_id,"
            + " ref_create_timestamp,"
            + " ref_update_usr_id,"
            + " ref_update_timestamp"
            + " from ctReference, Module"
            + " where ref_res_id = " + cwSQL.replaceNull("mod_mod_res_id_parent", "mod_res_id") + " and mod_res_id = ?"
            + " order by ref_title ASC";
    	return sql;
    }
    
    /**
     * to get base SQL statement of evaluation module according to training center
     * @param joinSQL
     * @param conditionSQL
     * @param orderSQL
     * @return
     */
    private static String getEvaluationModuleByTcrId(String joinSQL, String conditionSQL, String orderSQL) {
    	StringBuffer sqlBuffer = new StringBuffer();
    	sqlBuffer.append("select mod_res_id, res_title, mod_eff_start_datetime, mod_eff_end_datetime")
    			.append(" , res_status, mod_usr_id_instructor, res_upd_date, res_desc, tcr_title")
    			.append(" from module")
    			.append(" inner join resources on (mod_res_id = res_id)")
    			.append(" inner join reguser on (res_usr_id_owner = usr_id)")
    			.append(" inner join ModuleTrainingCenter on (mtc_mod_id = mod_res_id)")
    			.append(" inner join tcTrainingCenter on (mtc_tcr_id = tcr_id and tcr_status = ?)");
    	//join sql
    	if(joinSQL != null) {
    		sqlBuffer.append(" ").append(joinSQL).append(" ");
    	}
    	
    	//condition sql
    	sqlBuffer.append(" where mod_type = ? and usr_ste_ent_id = ? and mod_is_public = ? ");
    	if(conditionSQL != null) {
    		sqlBuffer.append(" ").append(conditionSQL).append(" ");
    	}
    	
    	//search sql
    	sqlBuffer.append(" and (res_title like ?  or res_desc like ?) ");
    	
    	//order sql
    	if(orderSQL == null) {
    		sqlBuffer.append(" order by res_upd_date desc ");
    	} else {
    		sqlBuffer.append(" ").append(orderSQL).append(" ");
    	}
    	
    	return sqlBuffer.toString();
    }
    
    private static String getEvalModuleCheckStatus() {
    	StringBuffer sqlBuffer = new StringBuffer();
    	sqlBuffer.append(" and ? between mod_eff_start_datetime and mod_eff_end_datetime and res_status = ?")
    		.append(" and mod_res_id in (select eac_res_id from EvalAccess")
    		.append(" inner join entityRelation on ((ern_ancestor_ent_id = eac_target_ent_id or ern_child_ent_id = eac_target_ent_id) and ern_type = ?)")
    		.append(" where ern_child_ent_id = ?)");
    	
    	return sqlBuffer.toString();
    }
   
    public static String getAllEvalModuleOfLearnerByTcrId(boolean isCheckStatus, WizbiniLoader wizbini) {
    	//set condition sql
    	StringBuffer conditionSQL = new StringBuffer();
    	conditionSQL.append(" and mtc_tcr_id in (").append(ViewTrainingCenter.getLrnFliter( wizbini)).append(")");
    	conditionSQL.append(" and mod_sgp_ind = 0");
    	if(isCheckStatus) {
    		conditionSQL.append(getEvalModuleCheckStatus());
    	}
    	
    	StringBuffer sqlBuffer = new StringBuffer();
    	sqlBuffer.append(getEvaluationModuleByTcrId(null, conditionSQL.toString(), null));
    	
    	return sqlBuffer.toString();
    }
    
    public static String getAllEvalModuleOfTrainer(boolean isCheckStatus) {
    	//set condition sql
    	StringBuffer conditionSQL = new StringBuffer();
    	conditionSQL.append(" and mtc_tcr_id in (").append(ViewTrainingCenter.ta_fliter).append(")");
    	conditionSQL.append(" and mod_sgp_ind = 0");
    	if(isCheckStatus) {
    		conditionSQL.append(getEvalModuleCheckStatus());
    	}
    	
    	StringBuffer sqlBuffer = new StringBuffer();
    	sqlBuffer.append(getEvaluationModuleByTcrId(null, conditionSQL.toString(), null));
    	
    	return sqlBuffer.toString();
    }
    
    public static String getEvalModuleOfTrainerByTcrId(boolean isCheckStatus) {
    	//set condition sql
    	StringBuffer conditionSQL = new StringBuffer();
    	conditionSQL.append(" and mtc_tcr_id = ?");
    	conditionSQL.append(" and mod_sgp_ind = 0");
    	if(isCheckStatus) {
    		conditionSQL.append(getEvalModuleCheckStatus());
    	}
    	
    	StringBuffer sqlBuffer = new StringBuffer();
    	sqlBuffer.append(getEvaluationModuleByTcrId(null, conditionSQL.toString(), null));
    	
    	return sqlBuffer.toString();
    }
    
    public static String getUserSelfDescOfPsn() {
    	return "select pbg_ent_id, pbg_self_desc from psnBiography where pbg_self_desc is not null";
    }
    
    public static String updateUserSelfDesc() {
    	return "update reguserExtension set urx_extra_44 = ? where urx_usr_ent_id = ?";
    }
    
}
