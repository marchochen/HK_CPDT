package com.cw.wizbank.db.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSQL;

public class OuterJoinSqlStatements {
    public static final char IDENTIFIER = '#';
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
    public static final String ProductName_ORACLE = "oracle";
    /**
    JDBC product name : microsoft sql server
    */
    private static final String ProductName_MSSQL = "microsoft sql server";
    /**
    JDBC product name : db2
    */
    public static final String ProductName_DB2 = "db2";

    // deprecated, tracking history not supported
    /*
    public static String getNavCentre(Connection con, String res_id_list, long course_id, boolean checkStatus) throws SQLException {
        return getNavCentre(con, res_id_list, course_id, checkStatus, 0);
    }
    */

    public static String getNavCentre(String res_id_list, long course_id, boolean checkStatus, long tkh_id) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer SQL = new StringBuffer(1024);

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            SQL.append(" Select tpl_stylesheet AS RTPLSTYLESHEET, ");
            SQL.append(" cos_res_id COSID, mod_res_id MODID, mod_type MTYPE, ");
            SQL.append(" RES1.res_tpl_name RTPLNAME, mov_last_acc_datetime ACCTIME, ");
            SQL.append(" RES1.res_title MTITLE, RES2.res_title CTITLE, ");
            SQL.append(" mod_web_launch MWEBLAUNCH, RES1.res_src_type MSRCTYPE, RES1.res_src_link MSRCLINK ");
            SQL.append(" From Module, Course, ModuleEvaluation, ");
            SQL.append(" Resources RES1, Resources RES2, Template ");
            SQL.append(" Where mov_cos_id = cos_res_id AND mov_mod_id = mod_res_id ");
            SQL.append(" AND tpl_name (+) = RES1.res_tpl_name ");
            SQL.append(" AND tpl_lan (+) = ? ");
            SQL.append(" AND mod_eff_start_datetime <= ? ");
            SQL.append(" AND mod_eff_end_datetime >= ? ");
            SQL.append(" AND cos_res_id IN ").append(res_id_list);
            SQL.append(" AND mod_res_id = RES1.res_id ");
            SQL.append(" AND cos_res_id = RES2.res_id ");
            SQL.append(" AND mov_ent_id = ? ");
            if(course_id > 0){
                SQL.append(" AND RES2.res_id = ? ");
                SQL.append(" AND mov_tkh_id = ? ");
            }
            if(checkStatus)
                SQL.append(" AND RES1.res_status <> ? ");
            SQL.append(" order by mov_last_acc_datetime desc ");

        } else {
            SQL.append(" Select tpl_stylesheet AS RTPLSTYLESHEET, ");
            SQL.append(" cos_res_id COSID, mod_res_id MODID, mod_type MTYPE, ");
            SQL.append(" RES1.res_tpl_name RTPLNAME, mov_last_acc_datetime ACCTIME, ");
            SQL.append(" RES1.res_title MTITLE, RES2.res_title CTITLE, ");
            SQL.append(" mod_web_launch MWEBLAUNCH, RES1.res_src_type MSRCTYPE, RES1.res_src_link MSRCLINK ");
            SQL.append(" From Module, Course, ModuleEvaluation, ");
            SQL.append(" Resources RES2, Resources RES1 ");
            SQL.append(" Left Join Template on ");
            SQL.append(" (tpl_name = RES1.res_tpl_name AND tpl_lan = ?) ");
            SQL.append(" Where mov_cos_id = cos_res_id AND mov_mod_id = mod_res_id ");
            SQL.append(" AND mod_eff_start_datetime <= ? ");
            SQL.append(" AND mod_eff_end_datetime >= ? ");
            SQL.append(" AND cos_res_id IN ").append(res_id_list);
            SQL.append(" AND mod_res_id = RES1.res_id ");
            SQL.append(" AND cos_res_id = RES2.res_id ");
            SQL.append(" AND mov_ent_id = ? ");
            if(course_id > 0)
                SQL.append(" AND RES2.res_id = ? ");
                SQL.append(" AND mov_tkh_id = ? ");
            if(checkStatus)
                SQL.append(" AND RES1.res_status <> ? ");
            SQL.append(" order by mov_last_acc_datetime desc ");
        }

        return SQL.toString();
    }

    // added for facility management - calendar
    public static final String getFacilitySchedule() throws SQLException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT");
        sqlStr.append(" fac_id, fac_title, fac_status, fsh_date, fsh_start_time, fsh_end_time ");
        sqlStr.append("FROM");
        sqlStr.append(" fmFacility");
        sqlStr.append(" LEFT JOIN fmFacilitySchedule ON");
        sqlStr.append("  fac_id = fsh_fac_id AND fsh_owner_ent_id = ? AND");
        sqlStr.append("  fsh_date >= ? AND fsh_date <= ? AND fsh_status IN (?, ?) ");
        sqlStr.append("WHERE");
        sqlStr.append(" fac_id IN ");
        
        sqlStr.append(OuterJoinSqlStatements.IDENTIFIER);
        sqlStr.append(" ORDER BY fac_id, fsh_start_time");
        return sqlStr.toString();
    }
    // added for facility management - reservation record list
    public static final String getRecordList() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT");
        sqlStr.append(" fsh_status, fsh_date, fsh_start_time, fsh_end_time,");
        sqlStr.append(" fsh_cancel_usr_id, fsh_cancel_timestamp, fsh_cancel_type, fsh_cancel_reason,");
        sqlStr.append(" fsh_create_usr_id, fsh_create_timestamp, fsh_upd_usr_id, fsh_upd_timestamp,");
        sqlStr.append(" rsv_id, rsv_status, rsv_purpose, rsv_ent_id, rsv_participant_no,");
        sqlStr.append(" fac_id, fac_title, fac_fee, usr_first_name_bil, usr_last_name_bil, usr_display_bil ");
        sqlStr.append("FROM");
        sqlStr.append(" fmReservation");
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(", fmFacilitySchedule, RegUser, fmFacility, fmRoom, fmEquipment");
            sqlStr.append(" WHERE");
            sqlStr.append(" fsh_fac_id = fac_id AND");
            sqlStr.append(" rsv_id(+) = fsh_rsv_id AND");
            sqlStr.append(" rsv_ent_id = usr_ent_id(+) AND");
            sqlStr.append(" fac_id = rom_fac_id(+) AND fac_id = eqm_fac_id(+) AND");
            sqlStr.append(" fsh_owner_ent_id = ? AND fsh_date >= ? AND fsh_date <= ?");
        } else {
            sqlStr.append(" RIGHT JOIN fmFacilitySchedule ON fsh_rsv_id = rsv_id");
			sqlStr.append(" INNER JOIN fmFacility ON fsh_fac_id = fac_id ");
			sqlStr.append(" LEFT JOIN RegUser ON rsv_ent_id = usr_ent_id ");
			sqlStr.append(" LEFT JOIN fmRoom ON fac_id = rom_fac_id ");
			sqlStr.append(" LEFT JOIN fmEquipment ON fac_id = eqm_fac_id ");
            sqlStr.append("WHERE");
            sqlStr.append(" fsh_owner_ent_id = ? AND fsh_date >= ? AND fsh_date <= ?");
        }
        return sqlStr.toString();
    }
    public static final String sql_get_record_list_status   = " AND fsh_status IN ";
    public static final String sql_get_record_list_fac_id   = " AND fac_id IN ";
    public static final String sql_get_record_list_created  = " AND fsh_create_usr_id = ?";
    public static final String sql_get_record_list_reserved = " AND rsv_ent_id = ?";
    //public static final String sql_get_record_list_join     = " AND fsh_fac_id = fac_id ORDER BY fsh_date, fsh_start_time, fac_title";
    public static final String sql_get_record_list_join		= " ORDER BY fsh_date, fsh_start_time, eqm_fac_id, rom_ftp_id, fac_title";
    /* DENNIS FM BEGIN */
    /*
    private static final String sql_get_conflict_fsh_oracle =
        " Select rsv_id, rsv_purpose, rsv_status, rsv_participant_no, " +
        " fsh_date, fsh_start_time, fsh_end_time, fsh_status, " +
        " usr_ent_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, " +
        " fac_id, fac_title " +
        " From fmFacility, RegUser, fmFacilitySchedule, fmReservation " +
        " Where  " +
        " rsv_id (+) = fsh_rsv_id " +
        " And fac_id = fsh_fac_id " +
        " And usr_id = fsh_create_usr_id " +
        " And fac_id = ? " +
        " And fsh_status <> ? " +
        " And fsh_end_time > ? " +
        " And fsh_start_time < ? ";
    private static final String sql_get_conflict_fsh_else =
        " Select rsv_id, rsv_purpose, rsv_status, rsv_participant_no, " +
        " fsh_date, fsh_start_time, fsh_end_time, fsh_status, " +
        " usr_ent_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, " +
        " fac_id, fac_title " +
        " From fmFacility, RegUser, fmFacilitySchedule Left Join fmReservation " +
        " on (rsv_id = fsh_rsv_id) " +
        " Where  " +
        " fac_id = fsh_fac_id " +
        " And usr_id = fsh_create_usr_id " +
        " And fac_id = ? " +
        " And fsh_status <> ? " +
        " And fsh_end_time > ? " +
        " And fsh_start_time < ? ";
    /**
    Use Left Join to find conflicting facility schedule
    */
    /*
    public static String getConflictFsh(Connection con) throws SQLException {
        String dbproduct = cwSQL.getDbProductName();
        String SQL;
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            SQL = sql_get_conflict_fsh_oracle;
        } else {
            SQL = sql_get_conflict_fsh_else;
        }
        return SQL;
    }
    /* DENNIS FM END */

    public static final String searchEnrolledEndIds(long itm_id) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" SELECT app_ent_id FROM aeApplication, aeAttendance, aeAttendanceStatus WHERE app_id = att_app_id(+) AND app_itm_id = att_itm_id(+) AND att_ats_id = ats_id(+) ")
                  .append(" AND (app_itm_id = ").append(itm_id).append(" OR app_itm_id IN ( ")
                  .append(" SELECT b.ire_child_itm_id FROM aeItemRelation a, aeItemRelation b ")
                  .append(" WHERE a.ire_child_itm_id = ").append(itm_id)
                  .append(" AND a.ire_parent_itm_id = b.ire_parent_itm_id ").append(" )) ")
                  .append(" AND (app_status NOT IN ('REJECTED', 'WITHDRAWN')) ")
                  .append(" AND (ats_type is null OR ats_type = 'PROGRESS') ");
        } else {
            sqlStr.append(" SELECT app_ent_id FROM aeApplication LEFT JOIN aeAttendance ON (app_id = att_app_id AND app_itm_id = att_itm_id) LEFT JOIN aeAttendanceStatus ON (att_ats_id = ats_id) ")
                  .append(" WHERE (app_itm_id = ").append(itm_id).append(" OR app_itm_id IN ( ")
                  .append(" SELECT b.ire_child_itm_id FROM aeItemRelation a, aeItemRelation b ")
                  .append(" WHERE a.ire_child_itm_id = ").append(itm_id)
                  .append(" AND a.ire_parent_itm_id = b.ire_parent_itm_id ").append(" )) ")
                  .append(" AND (app_status NOT IN ('REJECTED', 'WITHDRAWN')) ")
                  .append(" AND (ats_type is null OR ats_type = 'PROGRESS') ");
        }
        return sqlStr.toString();
    }
    // kim: todo
    // << BEGIN for oracle migration
    // from sqlStatements back to sqlStatements
    /*
    public static final String sql_get_course_start_end_date(Connection con) throws SQLException {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT");
        sql.append(" ccr_duration, enr_create_timestamp,");
        sql.append(" cos_eff_start_datetime, cos_eff_end_datetime ");
        sql.append("FROM");

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sql.append(" Enrolment, Course, CourseCriteria ");
            sql.append("WHERE");
            sql.append(" ccr_type(+) = ? AND enr_ent_id = ? AND cos_res_id = ?");
            sql.append(" AND enr_res_id = cos_res_id AND cos_res_id = ccr_cos_id(+)");
        } else {
            sql.append(" Enrolment");
            sql.append(" INNER JOIN Course ON (enr_res_id = cos_res_id)");
            sql.append(" LEFT JOIN CourseCriteria ON (cos_res_id = ccr_cos_id AND ccr_type = ?) ");
            sql.append("WHERE");
            sql.append(" enr_ent_id = ? AND cos_res_id = ?");
        }
        return sql.toString();
    }
    */
    // >> END
    // added for get certificate list ,dtl num
    public static final String getCtfDtlList() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sqlStr.append("SELECT");
			sqlStr.append(" ctf_id,ctf_title, cfn_status,");
			sqlStr.append(" count(cfn_ent_id) lrn_num,sum(cfn_qualification_ind) cfn_qualified_num");
			sqlStr.append(" FROM");
			sqlStr.append(" cfCertificate,cfCertification");
			sqlStr.append(" WHERE");
			sqlStr.append(" ctf_id = cfn_ctf_id(+)");
			sqlStr.append(" and ctf_owner_ent_id = ?");
		} else {
			sqlStr.append("SELECT");
			sqlStr.append(" ctf_id,ctf_title, cfn_status,");
			sqlStr.append(" count(cfn_ent_id) lrn_num,sum(cfn_qualification_ind) cfn_qualified_num");
			sqlStr.append(" FROM");
			sqlStr.append(" cfCertificate LEFT JOIN cfCertification");
			sqlStr.append(" ON (ctf_id = cfn_ctf_id) ");
			sqlStr.append(" WHERE");
			sqlStr.append(" ctf_owner_ent_id = ?");
		}
        return sqlStr.toString();
    }
    // added for get certificate list ,applied num
    public static final String getCtfAppliedNum() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT");
        sqlStr.append(" count(cfn_ctf_id) count_applied,ctf_id,ctf_title,ctf_status,ctb_id");
        sqlStr.append(" FROM");
        sqlStr.append(" cfCertification, cfCertificate,CodeTable");
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" WHERE");
            sqlStr.append(" ctf_id = cfn_ctf_id(+)");
//          sqlStr.append(" and ctf_owner_ent_id = cfn_owner_ent_id(+)");
            sqlStr.append(" and ctf_status = ctb_title");
            sqlStr.append(" and ctf_owner_ent_id = ?");
        } else {
            sqlStr.append(" WHERE");
            sqlStr.append(" ctf_id *= cfn_ctf_id");
//          sqlStr.append(" and ctf_owner_ent_id *= cfn_owner_ent_id");
            sqlStr.append(" and ctf_status = ctb_title");
            sqlStr.append(" and ctf_owner_ent_id = ?");
        }
        return sqlStr.toString();
    }



    public static final String getPublishedObjVersionSet(String colList) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT ")
              .append(colList);
        sqlStr.append(" FROM ");
        sqlStr.append(" kmNode, kmObject ");
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(", kmObjectAttachment, Attachment ")
                  .append(" WHERE obj_publish_ind = ? ")
                  .append(" AND oat_obj_bob_nod_id (+)= obj_bob_nod_id ")
                  .append(" AND oat_obj_version (+)= obj_version ")
                  .append(" AND att_id (+)= oat_att_id ")
                  .append(" AND nod_id = obj_bob_nod_id ")
                  .append(" AND obj_bob_nod_id in ");
        } else {
            sqlStr.append(" LEFT JOIN kmObjectAttachment ON ")
                  .append(" (obj_bob_nod_id = oat_obj_bob_nod_id and obj_version = oat_obj_version ) ")
                  .append(" LEFT JOIN Attachment ON ")
                  .append(" (oat_att_id = att_id) ")
                  .append(" WHERE obj_publish_ind = ? ")
                  .append(" AND nod_id = obj_bob_nod_id ")
                  .append(" AND obj_bob_nod_id in ");
        }
        return sqlStr.toString();
    }


    public static final String getLatestObjVersionSet(String colList) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT ")
              .append(colList);
        sqlStr.append(" FROM ");
        sqlStr.append(" kmNode, kmObject ");
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(", kmObjectAttachment, Attachment ")
                  .append(" WHERE obj_latest_ind = ? ")
                  .append(" AND oat_obj_bob_nod_id (+)= obj_bob_nod_id ")
                  .append(" AND oat_obj_version (+)= obj_version ")
                  .append(" AND att_id (+)= oat_att_id ")
                  .append(" AND nod_id = obj_bob_nod_id ")
                  .append(" AND obj_bob_nod_id in ");
        } else {
            sqlStr.append(" LEFT JOIN kmObjectAttachment ON ")
                  .append(" (obj_bob_nod_id = oat_obj_bob_nod_id and obj_version = oat_obj_version ) ")
                  .append(" LEFT JOIN Attachment ON ")
                  .append(" (oat_att_id = att_id) ")
                  .append(" WHERE obj_latest_ind = ? ")
                  .append(" AND nod_id = obj_bob_nod_id ")
                  .append(" AND obj_bob_nod_id in ");
        }
        return sqlStr.toString();
    }


    public static final String getObjWithAtt(String colList) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT ")
              .append(colList);
        sqlStr.append(" FROM ");
        sqlStr.append(" kmNode, kmObject ");
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(", kmObjectAttachment, Attachment ")
                  .append(" WHERE ")
                  .append(" oat_obj_bob_nod_id (+)= obj_bob_nod_id ")
                  .append(" AND oat_obj_version (+)= obj_version ")
                  .append(" AND att_id (+)= oat_att_id ")
                  .append(" AND nod_id = obj_bob_nod_id ")
                  .append(" AND obj_bob_nod_id = ? ")
                  .append(" AND obj_version = ? ");
        } else {
            sqlStr.append(" LEFT JOIN kmObjectAttachment ON ")
                  .append(" (obj_bob_nod_id = oat_obj_bob_nod_id and obj_version = oat_obj_version ) ")
                  .append(" LEFT JOIN Attachment ON ")
                  .append(" (oat_att_id = att_id) ")
                  .append(" WHERE ")
                  .append(" nod_id = obj_bob_nod_id ")
                  .append(" AND obj_bob_nod_id = ? ")
                  .append(" AND obj_version = ? ");
        }
        return sqlStr.toString();
    }

    /*
        scope the user record in certain usergroup (ent_id_lst)and certain usergrade (ugr_ent_id_lst)
        ent_id_lst, ugr_ent_id_lst accept null
        not scope by enrollment ( by public module )
    */
    // the sql below has been changed to non-outer join statment,
    // will be moved to SQLStatements in the future.
    // (2003-07-02 kawai)
    private static final String sql_get_mov_mssql =
        "SELECT usr_ent_id, usr_id, usr_display_bil, "
        + "mov_status, mov_update_timestamp, mov_score, "
        + "mov_tkh_id AS tkh_id, pgr_grade, pgr_status, pgr_complete_datetime "
        + "FROM ModuleEvaluation "
        + "INNER JOIN RegUser ON (mov_ent_id = usr_ent_id) "
        + "INNER JOIN Progress ON (usr_id = pgr_usr_id AND mov_mod_id = pgr_res_id AND mov_tkh_id = pgr_tkh_id) "
        + "WHERE mov_mod_id = ? ";
    private static final String sql_get_mov_oracle =
        "SELECT usr_ent_id, usr_id, usr_display_bil, "
        + "mov_status, mov_update_timestamp, mov_score, "
        + "mov_tkh_id AS tkh_id, pgr_grade, pgr_status, pgr_complete_datetime "
        + "FROM ModuleEvaluation, RegUser, Progress "
        + "WHERE mov_ent_id = usr_ent_id "
        + "AND usr_id = pgr_usr_id "
        + "AND mov_mod_id = pgr_res_id "
        + "AND mov_tkh_id = pgr_tkh_id "
        + "AND mov_mod_id = ? ";
    public static String getModuleEvaluationUserListSQL() {
        String dbproduct = cwSQL.getDbProductName();
        String result = null;

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            result = sql_get_mov_oracle;
        } else {
            result = sql_get_mov_mssql;
        }

        return result;
    }

    // scope the user record in certain usergroup (ent_id_lst)and certain usergrade (ugr_ent_id_lst)
    // ent_id_lst, ugr_ent_id_lst accept null
    private static final String sql_get_mov_by_enrollment_mssql =
        "SELECT usr_ent_id, usr_id, usr_display_bil, "
        + "mov_status, mov_update_timestamp, mov_score, "
        + "tkh_id, pgr_grade, pgr_status, pgr_complete_datetime "
        + "FROM ResourceContent "
        + "INNER JOIN TrackingHistory ON (rcn_res_id = tkh_cos_res_id) "
        + "INNER JOIN RegUser ON (tkh_usr_ent_id = usr_ent_id) "
        + "LEFT JOIN ModuleEvaluation ON (tkh_id = mov_tkh_id and mov_mod_id = rcn_res_id_content) "
        + "LEFT JOIN Progress ON (tkh_id = pgr_tkh_id and pgr_res_id = rcn_res_id_content) "
        + "WHERE rcn_res_id_content = ? ";
    private static final String sql_get_mov_by_enrollment_oracle =
        "SELECT usr_ent_id, usr_id, usr_display_bil, "
        + "mov_status, mov_update_timestamp, mov_score, "
        + "tkh_id, pgr_grade, pgr_status, pgr_complete_datetime "
        + "FROM ResourceContent, TrackingHistory, RegUser, ModuleEvaluation, Progress "
        + "WHERE rcn_res_id_content = ? "
        + "AND rcn_res_id = tkh_cos_res_id "
        + "AND tkh_usr_ent_id = usr_ent_id "
        + "AND tkh_id = mov_tkh_id(+) "
        + "AND tkh_id = pgr_tkh_id(+) "
        + "AND (mov_mod_id = rcn_res_id_content OR mov_mod_id IS NULL) "
        + "AND (pgr_res_id = rcn_res_id_content OR pgr_res_id IS NULL) ";
    public static String getModuleEvaluationUserListByEnrollmentSQL(Connection con) throws SQLException {
        String result = null;
        result = sql_get_mov_by_enrollment_mssql;
        return result;
    }

    public static String getModuleEvaluationUserCountSQL(/*Vector itm_id,*/ Vector mov_status) {
            String dbproduct = cwSQL.getDbProductName();
            StringBuffer sqlStr = new StringBuffer();

            if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
                sqlStr.append(" SELECT COUNT(tkh_usr_ent_id) ")
                      .append(" FROM ResourceContent, TrackingHistory, ModuleEvaluation ")
                      .append(" WHERE tkh_usr_ent_id = mov_ent_id(+) ")
                      .append(" AND tkh_id = mov_tkh_id(+) ")
                      .append(" AND mov_mod_id(+) = ? ")
                      .append(" AND tkh_cos_res_id = rcn_res_id ")
                      .append(" AND rcn_res_id_content = ? ");
                if( mov_status != null && !mov_status.isEmpty() ){
                    sqlStr.append(" AND ( ");
                    if( mov_status.indexOf("N") != -1)
                        sqlStr.append(" mov_status is null or mov_status IN ( ? ");
                    else
                        sqlStr.append(" mov_status IN ( ? ");

                    for(int i=1; i<mov_status.size(); i++)
                        sqlStr.append(", ?");

                    sqlStr.append(" ) ) ");
                }

            } else {
                sqlStr.append(" SELECT COUNT(tkh_usr_ent_id) ")
                      .append(" FROM ResourceContent, TrackingHistory ")
                      .append(" LEFT JOIN ModuleEvaluation ON ( tkh_id = mov_tkh_id AND tkh_usr_ent_id = mov_ent_id and mov_mod_id = ?) ")
                      .append(" WHERE ")
                      .append(" tkh_cos_res_id = rcn_res_id ")
                      .append(" AND rcn_res_id_content = ? ");
                if( mov_status != null && !mov_status.isEmpty() ){
                    sqlStr.append(" AND ( ");
                    if( mov_status.indexOf("N") != -1 )
                        sqlStr.append(" mov_status is null or mov_status IN ( ? ");
                    else
                        sqlStr.append(" mov_status IN ( ? ");
                    for(int i=1; i<mov_status.size(); i++)
                        sqlStr.append(", ?");
                    sqlStr.append(" ) ) ");
                }
            }
            return sqlStr.toString();

        }

	public static String getModuleEvaluationUserCountSQL2(/*Vector itm_id,*/ Vector mov_status) {
						String dbproduct = cwSQL.getDbProductName();
			StringBuffer sqlStr = new StringBuffer();

			if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
				sqlStr.append(" SELECT DISTINCT tkh_id ")
					  .append(" FROM ResourceContent, TrackingHistory, ModuleEvaluation ")
					  .append(" WHERE tkh_usr_ent_id = mov_ent_id(+) ")
					  .append(" AND tkh_id = mov_tkh_id(+) ")
					  .append(" AND mov_mod_id(+) = ? ")
					  .append(" AND tkh_cos_res_id = rcn_res_id ")
					  .append(" AND rcn_res_id_content = ? ");
				if( mov_status != null && !mov_status.isEmpty() ){
					sqlStr.append(" AND ( ");
					if( mov_status.indexOf("N") != -1)
						sqlStr.append(" mov_status is null or mov_status IN ( ? ");
					else
						sqlStr.append(" mov_status IN ( ? ");

					for(int i=1; i<mov_status.size(); i++)
						sqlStr.append(", ?");

					sqlStr.append(" ) ) ");
				}

			} else {
				sqlStr.append(" SELECT DISTINCT tkh_id ")
					  .append(" FROM ResourceContent, TrackingHistory ")
					  .append(" LEFT JOIN ModuleEvaluation ON ( tkh_id = mov_tkh_id AND tkh_usr_ent_id = mov_ent_id and mov_mod_id = ?) ")
					  .append(" WHERE ")
					  .append(" tkh_cos_res_id = rcn_res_id ")
					  .append(" AND rcn_res_id_content = ? ");
				if( mov_status != null && !mov_status.isEmpty() ){
					sqlStr.append(" AND ( ");
					if( mov_status.indexOf("N") != -1 )
						sqlStr.append(" mov_status is null or mov_status IN ( ? ");
					else
						sqlStr.append(" mov_status IN ( ? ");
					for(int i=1; i<mov_status.size(); i++)
						sqlStr.append(", ?");
					sqlStr.append(" ) ) ");
				}
			}
			return sqlStr.toString();

		}


    public static final String geteLibisValidCheckoutUserSQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow, ")
                  .append(" (SELECT COUNT(lob_id) AS numOverdue, lob_usr_ent_id AS overdueUser FROM kmlibraryobjectborrow WHERE lob_status = ? AND ? > lob_due_timestamp AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) overdueQuery ")
                  .append(" WHERE lob_usr_ent_id = overdueUser ")
                  .append(" AND numOverdue <= ? or numOverdue is null ");
            sqlStr.append(" AND lob_usr_ent_id in ( ");
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow, ")
                  .append(" (SELECT COUNT(lob_id) AS numBorrow, lob_usr_ent_id AS borrowUser FROM kmlibraryobjectborrow WHERE lob_status = ?  AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) borrowQuery ")
                  .append(" WHERE lob_usr_ent_id = borrowUser ")
                  .append(" AND numBorrow <= ? or numBorrow is null ");
            sqlStr.append(" ) ");
            sqlStr.append(" AND lob_latest_ind = ? ")
                  .append(" AND lob_usr_ent_id = ? ");
        } else {
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow ")
                  .append(" LEFT JOIN (SELECT COUNT(lob_id) AS numOverdue, lob_usr_ent_id AS overdueUser FROM kmlibraryobjectborrow WHERE lob_status = ? AND ? > lob_due_timestamp AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) overdueQuery ")
                  .append(" ON lob_usr_ent_id = overdueUser ")
                  .append(" WHERE numOverdue <= ? or numOverdue is null ");
            sqlStr.append(" AND lob_usr_ent_id in ( ");
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow ")
                  .append(" LEFT JOIN (SELECT COUNT(lob_id) AS numBorrow, lob_usr_ent_id AS borrowUser FROM kmlibraryobjectborrow WHERE lob_status IN (?,?) AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) borrowQuery ")
                  .append(" ON lob_usr_ent_id = borrowUser ")
                  .append(" WHERE numBorrow <= ? or numBorrow is null ");
            sqlStr.append(" ) ");
            sqlStr.append(" AND lob_latest_ind = ? ")
                  .append(" AND lob_usr_ent_id = ? ");
        }
        return sqlStr.toString();
    }

    public static final String geteLibisValidUserSQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow, ")
                  .append(" (SELECT COUNT(lob_id) AS numOverdue, lob_usr_ent_id AS overdueUser FROM kmlibraryobjectborrow WHERE lob_status = ? AND ? > lob_due_timestamp AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) overdueQuery ")
                  .append(" WHERE lob_usr_ent_id = overdueUser ")
                  .append(" AND numOverdue < ? or numOverdue is null ");
            sqlStr.append(" AND lob_usr_ent_id in ( ");
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow, ")
                  .append(" (SELECT COUNT(lob_id) AS numBorrow, lob_usr_ent_id AS borrowUser FROM kmlibraryobjectborrow WHERE lob_status = ?  AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) borrowQuery ")
                  .append(" WHERE lob_usr_ent_id = borrowUser ")
                  .append(" AND numBorrow < ? or numBorrow is null ");
            sqlStr.append(" ) ");
            sqlStr.append(" AND lob_latest_ind = ? ");
            sqlStr.append(" UNION ")
                  .append(" SELECT lob_usr_ent_id FROM kmlibraryobjectborrow ")
                  .append(" WHERE lob_status = ? ")
                  .append(" AND lob_lio_bob_nod_id = ? ")
                  .append(" AND lob_latest_ind = ? ");

        } else {
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow ")
                  .append(" LEFT JOIN (SELECT COUNT(lob_id) AS numOverdue, lob_usr_ent_id AS overdueUser FROM kmlibraryobjectborrow WHERE lob_status = ? AND ? > lob_due_timestamp AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) overdueQuery ")
                  .append(" ON lob_usr_ent_id = overdueUser ")
                  .append(" WHERE numOverdue < ? or numOverdue is null ");
            sqlStr.append(" AND lob_usr_ent_id in ( ");
            sqlStr.append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow ")
                  .append(" LEFT JOIN (SELECT COUNT(lob_id) AS numBorrow, lob_usr_ent_id AS borrowUser FROM kmlibraryobjectborrow WHERE lob_status IN (?,?) AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) borrowQuery ")
                  .append(" ON lob_usr_ent_id = borrowUser ")
                  .append(" WHERE numBorrow < ? or numBorrow is null ");
            sqlStr.append(" ) ");
            sqlStr.append(" AND lob_latest_ind = ? ");
            sqlStr.append(" UNION ")
                  .append(" SELECT lob_usr_ent_id FROM kmlibraryobjectborrow ")
                  .append(" WHERE lob_status = ? ")
                  .append(" AND lob_lio_bob_nod_id = ? ")
                  .append(" AND lob_latest_ind = ? ");
        }
        return sqlStr.toString();
    }

    public static final String geteLibCheckOutListSQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" SELECT bob_nod_id, obj_title AS title, ")
                  .append(" bob_code AS call_number, count(lob_id) AS numRequest, lio_num_copy_in_stock AS stock, obj_type ")
                  .append(" FROM kmlibraryobjectborrow, kmLibraryObject, kmBaseObject, kmObject, ")
                  .append(" ( ")
                  .append(" SELECT lob_usr_ent_id as validUser, lob_lio_bob_nod_id as validBorrow FROM kmlibraryobjectborrow, ")
                  .append(" (SELECT COUNT(lob_id) AS numOverdue, lob_usr_ent_id AS overdueUser FROM kmlibraryobjectborrow WHERE lob_status = ? AND ? > lob_due_timestamp AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) overdueQuery ")
                  .append(" WHERE numOverdue < ? or numOverdue is null ")
                  .append(" AND lob_usr_ent_id = overdueUser (+) ")
                  .append(" AND lob_usr_ent_id in ( ")
                  .append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow, ")
                  .append(" (SELECT COUNT(lob_id) AS numBorrow, lob_usr_ent_id AS borrowUser FROM kmlibraryobjectborrow WHERE lob_status IN (?, ?) AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) borrowQuery ")
                  .append(" WHERE numBorrow < ? or numBorrow is null ")
                  .append(" AND lob_usr_ent_id = borrowUser (+) ")
                  .append(" ) ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" UNION ")
                  .append(" SELECT lob_usr_ent_id, lob_lio_bob_nod_id FROM kmlibraryobjectborrow ")
                  .append(" WHERE lob_status = ? ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" ) tempQuery ")
                  .append(" WHERE lob_status in (?, ?) ")
                  .append(" AND lob_usr_ent_id = validUser ")
                  .append(" AND lob_lio_bob_nod_id = validBorrow " )
                  .append(" AND lob_lio_bob_nod_id = lio_bob_nod_id ")
                  .append(" AND lob_lio_bob_nod_id = bob_nod_id ")
                  .append(" AND bob_nod_id = obj_bob_nod_id ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" AND lio_num_copy_in_stock > 0 ");
        } else {
            sqlStr.append(" SELECT bob_nod_id, obj_title AS title, ")
                  .append(" bob_code AS call_number, count(lob_id) AS numRequest, lio_num_copy_in_stock AS stock, obj_type ")
                  .append(" FROM kmlibraryobjectborrow, kmLibraryObject, kmBaseObject, kmObject, ")
                  .append(" ( ")
                  .append(" SELECT lob_usr_ent_id as validUser, lob_lio_bob_nod_id as validBorrow FROM kmlibraryobjectborrow ")
                  .append(" LEFT JOIN (SELECT COUNT(lob_id) AS numOverdue, lob_usr_ent_id AS overdueUser FROM kmlibraryobjectborrow WHERE lob_status = ? AND ? > lob_due_timestamp AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) overdueQuery ")
                  .append(" ON lob_usr_ent_id = overdueUser ")
                  .append(" WHERE numOverdue < ? or numOverdue is null ")
                  .append(" AND lob_usr_ent_id in ( ")
                  .append(" SELECT distinct lob_usr_ent_id FROM kmlibraryobjectborrow ")
                  .append(" LEFT JOIN (SELECT COUNT(lob_id) AS numBorrow, lob_usr_ent_id AS borrowUser FROM kmlibraryobjectborrow WHERE lob_status IN (?, ?) AND lob_latest_ind = ? GROUP BY lob_usr_ent_id) borrowQuery ")
                  .append(" ON lob_usr_ent_id = borrowUser ")
                  .append(" WHERE numBorrow < ? or numBorrow is null ")
                  .append(" ) ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" UNION ")
                  .append(" SELECT lob_usr_ent_id, lob_lio_bob_nod_id FROM kmlibraryobjectborrow ")
                  .append(" WHERE lob_status = ? ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" ) tempQuery ")
                  .append(" WHERE lob_status in (?, ?) ")
                  .append(" AND lob_usr_ent_id = validUser ")
                  .append(" AND lob_lio_bob_nod_id = validBorrow " )
                  .append(" AND lob_lio_bob_nod_id = lio_bob_nod_id ")
                  .append(" AND lob_lio_bob_nod_id = bob_nod_id ")
                  .append(" AND bob_nod_id = obj_bob_nod_id ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" AND lio_num_copy_in_stock > 0 ");
        }
        return sqlStr.toString();
    }

    public static final String geteLibItemRecSQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {

            sqlStr.append(" Select usr_ent_id, usr_display_bil, lob_id, lob_status, ")
                  .append(" loc_id, loc_copy, lob_renew_no, lob_due_timestamp, lob_create_timestamp ")
                  .append(" From RegUser, kmLibraryObjectBorrow, kmLibraryObjectCopy ")
                  .append(" Where usr_ent_id = lob_usr_ent_id ")
                  .append(" lob_loc_id = loc_id (+) ")
                  .append(" AND lob_lio_bob_nod_id = ? ")
                  .append(" AND lob_latest_ind = ? ");
        } else {

            sqlStr.append(" Select usr_ent_id, usr_display_bil, lob_id, lob_status, ")
                  .append(" loc_id, loc_copy, lob_renew_no, lob_due_timestamp, lob_create_timestamp ")
                  .append(" From RegUser, kmLibraryObjectBorrow ")
                  .append(" Left Join kmLibraryObjectCopy on ( lob_loc_id = loc_id ) " )
                  .append(" Where usr_ent_id = lob_usr_ent_id ")
                  .append(" AND lob_lio_bob_nod_id = ? ")
                  .append(" AND lob_latest_ind = ? ");

        }

        return sqlStr.toString();
    }


    public static final String geteLibUserRecSQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {

            sqlStr.append(" Select usr_ent_id, usr_display_bil, obj_type, bob_code, ")
                  .append(" loc_copy, obj_title, lob_renew_no, lob_due_timestamp, ")
                  .append(" lob_id, bob_nod_id, loc_id, lob_status, lio_num_copy_available, ")
                  .append(" lob_create_timestamp, lio_num_copy_in_stock ")
                  .append(" From RegUser, kmObject, kmBaseObject, kmLibraryObjectBorrow, ")
                  .append(" kmLibraryObject, kmLibraryObjectCopy ")
                  .append(" Where lob_usr_ent_id = usr_ent_id ")
                  .append(" And bob_nod_id = lob_lio_bob_nod_id ")
                  .append(" And bob_nod_id = obj_bob_nod_id ")
                  .append(" And bob_nod_id = lio_bob_nod_id ")
                  .append(" And lob_loc_id = loc_id (+) ")
                  .append(" And usr_ent_id = ? ")
                  .append(" And lob_latest_ind = ? ");


        } else {

            sqlStr.append(" Select usr_ent_id, usr_display_bil, obj_type, bob_code, ")
                  .append(" loc_copy, obj_title, lob_renew_no, lob_due_timestamp, ")
                  .append(" lob_id, bob_nod_id, loc_id, lob_status, lio_num_copy_available, ")
                  .append(" lob_create_timestamp, lio_num_copy_in_stock ")
                  .append(" From RegUser, kmObject, kmBaseObject, kmLibraryObject, ")
                  .append(" kmLibraryObjectBorrow Left Join kmLibraryObjectCopy ON ( lob_loc_id = loc_id )")
                  .append(" Where lob_usr_ent_id = usr_ent_id ")
                  .append(" And bob_nod_id = lob_lio_bob_nod_id ")
                  .append(" And bob_nod_id = obj_bob_nod_id ")
                  .append(" And bob_nod_id = lio_bob_nod_id ")
                  .append(" And usr_ent_id = ? ")
                  .append(" And lob_latest_ind = ? ");

        }
        return sqlStr.toString();
    }



    public static final String geteLibChildObjectsNStatus() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" Select bob_nod_id, lob_id, obj_type, bob_code, obj_title, obj_author, ")
                  .append(" lio_num_copy_available,lob_renew_no, lob_status, lio_num_copy  ")
                  .append(" From kmNode, kmBaseObject, kmObject, kmLibraryObject, kmLibraryObjectBorrow ")
                  .append(" WHERE lio_bob_nod_id = lob_lio_bob_nod_id (+) ")
                  .append(" And lnk_nod_id = nod_id And lnk_target_nod_id = bob_nod_id ")
                  .append(" And lob_usr_ent_id = ? And lob_latest_ind = ? ")
                  .append(" And lio_bob_nod_id = bob_nod_id And nod_id = bob_nod_id ")
                  .append(" And obj_bob_nod_id = bob_nod_id And obj_publish_ind = ? ")
                  .append(" And bob_nature = ? And nod_ancestor like ? ");
        } else {
            sqlStr.append(" Select bob_nod_id, lob_id, obj_type, bob_code, obj_title, obj_author, ")
                  .append(" lio_num_copy_available,lob_renew_no, lob_status, lio_num_copy  ")
                  .append(" From kmNode, kmBaseObject, kmObject, kmLink, kmLibraryObject ")
                  .append(" Left Join kmLibraryObjectBorrow ")
                  .append(" On ( lio_bob_nod_id = lob_lio_bob_nod_id ")
                  .append("      And lob_usr_ent_id = ? And lob_latest_ind = ? ) ")
                  .append(" Where lio_bob_nod_id = bob_nod_id ")
                  .append(" And lnk_target_nod_id = bob_nod_id ")
                  .append(" And lnk_nod_id = nod_id ")
                  .append(" And obj_bob_nod_id = bob_nod_id ")
                  .append(" And obj_publish_ind = ? ")
                  .append(" And bob_nature = ? ")
                  .append(" And nod_ancestor like ? ");
        }
        return sqlStr.toString();
    }


    public static final String geteLibUserHistorySQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" SELECT lob_id, lob_status, bob_nod_id, obj_title, bob_code, ")
                  .append(" loc_id, loc_copy, loc_desc, lob_renew_no, lob_due_timestamp, ")
                  .append(" processUser.usr_ent_id AS process_usr_ent_id, ")
                  .append(" lob_usr_ent_id AS usr_ent_id, ")
                  .append(" lob_create_timestamp as lob_update_timestamp, lob_latest_ind ")
                  .append(" FROM  kmBASeObject, kmObject, regUser ownUser, regUser processUser, kmlibraryObjectborrow, kmLibraryObjectcopy ")
                  .append(" WHERE lob_lio_bob_nod_id = bob_nod_id ")
                  .append(" AND lob_loc_id = loc_id (+) ")
                  .append(" AND obj_bob_nod_id = bob_nod_id ")
                  .append(" AND ownUser.usr_ent_id = lob_usr_ent_id ")
                  .append(" AND processUser.usr_id = lob_create_usr_id ")
                  .append(" AND lob_usr_ent_id = ? ")
                  .append(" ORDER BY lob_update_timestamp ");
        } else {
            sqlStr.append(" SELECT lob_id, lob_status, bob_nod_id, obj_title, bob_code, ")
                  .append(" loc_id, loc_copy, loc_desc, lob_renew_no, lob_due_timestamp, ")
                  .append(" processUser.usr_ent_id AS process_usr_ent_id, ")
                  .append(" lob_usr_ent_id AS usr_ent_id, ")
                  .append(" lob_create_timestamp as lob_update_timestamp, lob_latest_ind ")
                  .append(" FROM  kmBASeObject, kmObject, regUser ownUser, regUser processUser, ")
                  .append(" kmlibraryObjectborrow LEFT JOIN kmLibraryObjectcopy ")
                  .append(" ON (lob_loc_id = loc_id) ")
                  .append(" WHERE lob_lio_bob_nod_id = bob_nod_id ")
                  .append(" AND obj_bob_nod_id = bob_nod_id ")
                  .append(" AND ownUser.usr_ent_id = lob_usr_ent_id ")
                  .append(" AND processUser.usr_id = lob_create_usr_id ")
                  .append(" AND lob_usr_ent_id = ? ")
                  .append(" ORDER BY lob_update_timestamp ");
        }
        return sqlStr.toString();
    }

    public static final String geteLibItemReserveInfoSQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" SELECT bob_nod_id, obj_title, bob_code, lio_num_copy_available, ")
                  .append(" lio_num_copy_in_stock, num_reserve ")
                  .append(" FROM kmObject, kmBaseObject, kmlibraryObject, ")
                  .append(" ( ")
                  .append(" SELECT COUNT(lob_lio_bob_nod_id) AS num_reserve, ")
                  .append(" lob_lio_bob_nod_id AS reserve_nod_id ")
                  .append(" FROM kmlibraryobjectborrow ")
                  .append(" WHERE lob_status = ? ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" GROUP BY lob_lio_bob_nod_id ")
                  .append(" ) tempQuery ")
                  .append(" WHERE obj_bob_nod_id = bob_nod_id ")
                  .append(" AND lio_bob_nod_id = reserve_nod_id (+) ")
                  .append(" AND lio_bob_nod_id = bob_nod_id ")
                  .append(" AND bob_nod_id = ? ");
        } else {
            sqlStr.append(" SELECT bob_nod_id, obj_title, bob_code, lio_num_copy_available, ")
                  .append(" lio_num_copy_in_stock, num_reserve ")
                  .append(" FROM kmObject, kmBaseObject, kmlibraryObject LEFT JOIN ")
                  .append(" ( ")
                  .append(" SELECT COUNT(lob_lio_bob_nod_id) AS num_reserve, ")
                  .append(" lob_lio_bob_nod_id AS reserve_nod_id ")
                  .append(" FROM kmlibraryobjectborrow ")
                  .append(" WHERE lob_status = ? ")
                  .append(" AND lob_latest_ind = ? ")
                  .append(" GROUP BY lob_lio_bob_nod_id ")
                  .append(" ) tempQuery ON lio_bob_nod_id = reserve_nod_id ")
                  .append(" WHERE obj_bob_nod_id = bob_nod_id ")
                  .append(" AND lio_bob_nod_id = bob_nod_id ")
                  .append(" AND bob_nod_id = ? ");
        }
        return sqlStr.toString();
    }

      public static final String geteLibItemInfoSQL() {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sqlStr = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sqlStr.append(" SELECT lob_usr_ent_id, usr_display_bil, bob_nod_id, ")
                  .append(" obj_title, bob_code, loc_id, loc_copy, loc_desc, lob_due_timestamp, ")
                  .append(" lob_renew_no, lob_create_usr_id, lob_create_timestamp, ")
                  .append(" lob_update_usr_id, lob_update_timestamp ")
                  .append(" FROM regUser, kmObject, kmBaseObject, kmLibraryObjectBorrow, kmlibraryobjectcopy")
                  .append(" WHERE usr_ent_id = lob_usr_ent_id ")
                  .append(" AND lob_loc_id = loc_id (+) ")
                  .append(" AND obj_bob_nod_id = bob_nod_id ")
                  .append(" AND bob_nod_id = lob_lio_bob_nod_id ")
                  .append(" AND lob_id = ? ");
        } else {
            sqlStr.append(" SELECT lob_usr_ent_id, usr_display_bil, bob_nod_id, ")
                  .append(" obj_title, bob_code, loc_id, loc_copy, loc_desc, lob_due_timestamp, ")
                  .append(" lob_renew_no, lob_create_usr_id, lob_create_timestamp, ")
                  .append(" lob_update_usr_id, lob_update_timestamp ")
                  .append(" FROM regUser, kmObject, kmBaseObject, kmLibraryObjectBorrow LEFT JOIN ")
                  .append(" kmlibraryobjectcopy ON (lob_loc_id = loc_id) ")
                  .append(" WHERE usr_ent_id = lob_usr_ent_id ")
                  .append(" AND obj_bob_nod_id = bob_nod_id ")
                  .append(" AND bob_nod_id = lob_lio_bob_nod_id ")
                  .append(" AND lob_id = ? ");
        }
        return sqlStr.toString();
    }


    public static String getUsageRpt(String mod_id_lst, Timestamp startDateTime, Timestamp endDateTime) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer SQLBuf = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            SQLBuf.append(" SELECT ")
                  .append(" rcn_res_id_content mov_mod_id ")
                  .append(" , res_title ")
                  .append(" , res_subtype ")
                  .append(" , res_src_type ")
                  .append(" , sum(mvh_total_attempt) total_attempt ")
                  .append(" , max(mvh_last_acc_datetime) last_access ")
                  .append(" , sum(mvh_total_time) total_time ")
                  .append(" FROM ModuleEvaluationHistory, Resources, ResourceContent, TrackingHistory ")
                  .append(" WHERE ")
                  .append(" rcn_res_id = tkh_cos_res_id AND ");
            if(startDateTime != null) {
                SQLBuf.append(" mvh_create_timestamp > ? AND ");
            }
            if(endDateTime != null) {
                SQLBuf.append(" mvh_create_timestamp < ? AND ");
            }
            if(startDateTime != null) {
                SQLBuf.append(" mvh_last_acc_datetime > ? AND ");
            }
            if(endDateTime != null) {
                SQLBuf.append(" mvh_last_acc_datetime < ? AND ");
            }
            SQLBuf.append(" rcn_res_id = ? AND ")
                  .append(" res_id = rcn_res_id_content AND ")
                  .append(" mvh_ent_id (+)= tkh_usr_ent_id AND ")
                  //.append(" mvh_mod_id (+)= rcn_res_id_content AND ")
                  .append(" (mvh_mod_id = rcn_res_id_content OR mvh_mod_id is null) AND ")
                  .append(" mvh_tkh_id (+)= tkh_id AND " )
                  .append(" rcn_res_id_content IN ").append(mod_id_lst)
                  .append(" AND tkh_type = ? " )
                  .append(" GROUP BY rcn_res_id_content, res_title, res_src_type, res_subtype ")
                  .append(" ORDER BY res_title, rcn_res_id_content");
        } else {
            SQLBuf.append("SELECT rcn_res_id_content mov_mod_id  , res_title  , res_subtype  , res_src_type  , sum(mvh_total_attempt) total_attempt  , max(mvh_last_acc_datetime) last_access  ,sum(mvh_total_time) total_time  ")
                  .append("FROM TrackingHistory ")
                  .append("Inner Join ResourceContent on ( rcn_res_id = tkh_cos_res_id AND rcn_res_id_content IN ").append(mod_id_lst).append(") ")
                  .append("Inner Join Resources on (res_id = rcn_res_id_content) ")
                  .append("Left Join ModuleEvaluationHistory on ( mvh_ent_id  = tkh_usr_ent_id ")
                  .append("AND  mvh_mod_id  = rcn_res_id_content ")
                  .append("AND  mvh_tkh_id  = tkh_id ");
            if(startDateTime != null) {
                SQLBuf.append("AND mvh_create_timestamp > ? ");
            }
            if(endDateTime != null) {
                SQLBuf.append("AND mvh_create_timestamp < ? ");
            }
            if(startDateTime != null) {
                SQLBuf.append("AND mvh_last_acc_datetime > ? ");
            }
            if(endDateTime != null) {
                SQLBuf.append("AND mvh_last_acc_datetime < ? ");
            }
            SQLBuf.append(" ) ")
                  .append("WHERE  ")
                  .append("tkh_cos_res_id = ? ")
                  .append("AND tkh_type = ?  ")
                  .append("GROUP BY rcn_res_id_content, res_title, res_src_type, res_subtype  ")
                  .append("ORDER BY res_title, rcn_res_id_content ");
        }
        return SQLBuf.toString();
    }

    private static final String sql_get_ass_enrol_mssql =
        "SELECT usr_id, usr_ent_id, pgr_usr_id, pgr_status, usr_display_bil, tkh_id "
        + "FROM TrackingHistory "
        + "INNER JOIN RegUser ON (tkh_usr_ent_id = usr_ent_id) "
        + "LEFT JOIN Progress ON (tkh_id = pgr_tkh_id AND pgr_res_id = ? AND pgr_attempt_nbr = ?) "
        + "WHERE tkh_cos_res_id = ? "
        + "AND tkh_type = ? "
        + "ORDER BY usr_display_bil ";
    private static final String sql_get_ass_enrol_oracle =
        "SELECT usr_id, usr_ent_id, pgr_usr_id, pgr_status, usr_display_bil, tkh_id "
        + "FROM TrackingHistory, RegUser, Progress "
        + "WHERE tkh_usr_ent_id = usr_ent_id "
        + "AND tkh_id = pgr_tkh_id(+) AND pgr_res_id(+) = ? AND pgr_attempt_nbr(+) = ? "
        + "AND tkh_cos_res_id = ? "
        + "AND tkh_type = ? "
        + "ORDER BY usr_display_bil ";
    public static String getAssEnrolSQL() {
        String dbproduct = cwSQL.getDbProductName();
        String result = null;
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            result = sql_get_ass_enrol_oracle;
        } else {
            result = sql_get_ass_enrol_mssql;
        }
        return result;
    }
    
    public static String getUsrAppApprovalUsgCode(String tmpTableName){
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sql = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sql.append("SELECT usr_ste_usr_id, ent_ste_uid FROM RegUser , Entity ");
            sql.append("WHERE usr_app_approval_usg_ent_id = ent_id(+) AND usr_ent_id in ");
            sql.append(" (SELECT tmp_usr_ent_id FROM " + tmpTableName + ")");
        }else{
            sql.append("SELECT usr_ste_usr_id, ent_ste_uid FROM RegUser ");
            sql.append("LEFT JOIN Entity ON (usr_app_approval_usg_ent_id = ent_id) ");
            sql.append("WHERE usr_ent_id in ");
            sql.append(" (SELECT tmp_usr_ent_id FROM " + tmpTableName + ")");
        }
        return sql.toString();
    }
    
    public static String getUsrSupervisedGroupByUsrEntIds(String tmpTableName){
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sql = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sql.append("SELECT usg_ent_id , ent_ste_uid, usr_ent_id, usr_ste_usr_id FROM UserGroup, Entity , SuperviseTargetEntity , RegUser ");
            sql.append("WHERE usr_ent_id = spt_source_usr_ent_id(+) ");
            sql.append("AND spt_target_ent_id = usg_ent_id(+) ");
            sql.append("AND usg_ent_id = ent_id(+) ");
            sql.append("AND spt_type = ? ");
            sql.append(" AND usr_ent_id in ");
            sql.append(" (SELECT tmp_usr_ent_id FROM " + tmpTableName + ")");
        }else{
            sql.append("SELECT usg_ent_id , ent_ste_uid, usr_ent_id, usr_ste_usr_id FROM RegUser ");
            sql.append("LEFT JOIN SuperviseTargetEntity on (usr_ent_id = spt_source_usr_ent_id and spt_type = ? ) ");
            sql.append("LEFT JOIN UserGroup on (usg_ent_id = spt_target_ent_id )");
            sql.append("LEFT JOIN Entity on (usg_ent_id = ent_id )");
            sql.append("WHERE usr_ent_id in ");
            sql.append(" (SELECT tmp_usr_ent_id FROM " + tmpTableName + ")");
        }
        return sql.toString();
    }

    public static String getUsrDirectSupervisorByUsrEntIds(String tmpTableName){
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sql = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sql.append(" SELECT SUPERVISOR.usr_ste_usr_id supervisor_ste_usr_id, TARGET.usr_ste_usr_id target_ste_usr_id FROM RegUser SUPERVISOR, SuperviseTargetEntity , RegUser TARGET ");
            sql.append(" WHERE spt_source_usr_ent_id = SUPERVISOR.usr_ent_id(+) ");
            sql.append(" AND TARGET.usr_ent_id = spt_target_ent_id(+) ");
            sql.append("AND spt_type = ? ");
            sql.append("AND TARGET.usr_ent_id in ");
            sql.append(" (SELECT tmp_usr_ent_id FROM " + tmpTableName + ")");
        }else{
            sql.append("SELECT SUPERVISOR.usr_ste_usr_id supervisor_ste_usr_id, TARGET.usr_ste_usr_id target_ste_usr_id FROM RegUser TARGET ");
            sql.append("LEFT JOIN SuperviseTargetEntity on (TARGET.usr_ent_id = spt_target_ent_id and spt_type = ? ) ");
            sql.append("LEFT JOIN RegUser SUPERVISOR on (spt_source_usr_ent_id = SUPERVISOR.usr_ent_id )");
            sql.append("WHERE TARGET.usr_ent_id in ");
            sql.append(" (SELECT tmp_usr_ent_id FROM " + tmpTableName + ")");
        }
        return sql.toString();
    }

	public static String viewAssessmentGetAsmList(){
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT res_id, res_title, usr_display_bil, mod_eff_start_datetime, mod_eff_end_datetime, asm_mode, asm_type, tpl_stylesheet, mod_max_usr_attempt ");
			sql.append(" FROM Resources, Module, Assessment, RegUser, Template RIGHT JOIN Resources ");
			sql.append(" ON (tpl_name = res_tpl_name) ");
			sql.append(" WHERE asm_res_id = res_id AND asm_res_id = mod_res_id ");
			sql.append(" AND res_status = ? ");
			sql.append(" AND res_usr_id_owner = usr_id AND usr_ste_ent_id = ? ");
		}else{
			sql.append(" SELECT res_id, res_title, usr_display_bil, mod_eff_start_datetime, mod_eff_end_datetime, asm_mode, asm_type, tpl_stylesheet, mod_max_usr_attempt ");
			sql.append(" FROM Resources, Module, Assessment, RegUser, Template ");
			sql.append(" WHERE asm_res_id = res_id AND asm_res_id = mod_res_id ");
			sql.append(" AND tpl_name " + cwSQL.get_right_join() + " res_tpl_name ");
			sql.append(" AND res_status = ? ");
			sql.append(" AND res_usr_id_owner = usr_id AND usr_ste_ent_id = ? ");
		}
		return sql.toString();
	}

	public static String viewCMAssessmentAssList(boolean hasTc,String tcr_id_lst){
//		DatabaseMetaData conMD = con.getMetaData();
//		String dbproduct = conMD.getDatabaseProductName().toLowerCase();
		StringBuffer sql = new StringBuffer();
//		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT asm_id, COUNT(asu_asm_id) AS total, usr_display_bil, asm_status, ")
			   .append(" asm_title, asm_eff_start_datetime, asm_eff_end_datetime ")
			   .append(" FROM RegUser, cmSkillSet, cmAssessmentUnit RIGHT JOIN cmAssessment ");
				sql.append(" ON asu_asm_id = asm_id ");
				if(hasTc){
					sql.append("  ,entityrelation, tcTrainingCenterTargetEntity");
				}
			   sql.append(" WHERE asm_ent_id = usr_ent_id ")
			   .append(" AND asm_sks_skb_id = sks_skb_id ")
			   .append(" AND asu_type <> ? ")
			   .append(" AND asm_status <> ? ")
			   .append(" AND sks_owner_ent_id = ? ");
			   if(hasTc){
					sql.append("  and ern_type='USR_PARENT_USG' and ern_child_ent_id= usr_ent_id and ern_ancestor_ent_id=tce_ent_id and tce_tcr_id in ").append(tcr_id_lst);
				}
		/*}else{
			sql.append(" SELECT asm_id, COUNT(asu_asm_id) AS total, usr_display_bil, asm_status, ")
			   .append(" asm_title, asm_eff_start_datetime, asm_eff_end_datetime ")
			   .append(" FROM RegUser, cmAssessment, cmAssessmentUnit, cmSkillSet ");
			if(hasTc){
				sql.append("  ,entityrelation, tcTrainingCenterTargetEntity");
			}
			   
			sql.append(" WHERE asu_asm_id ").append(cwSQL.get_right_join(con)).append(" asm_id ")
			   .append(" AND asm_ent_id = usr_ent_id ")
			   .append(" AND asm_sks_skb_id = sks_skb_id ")
			   .append(" AND asu_type <> ? ")
			   .append(" AND asm_status <> ? ")
			   .append(" AND sks_owner_ent_id = ? ");
			if(hasTc){
				sql.append("  and ern_type='USR_PARENT_USG' and ern_child_ent_id= usr_ent_id  and ern_ancestor_ent_id=tce_ent_id and tce_tcr_id in ").append(tcr_id_lst);
			}
		}*/
		return sql.toString();
	}

	public static String viewCMAssessmentAssList2(boolean hasTc,String tcr_id_lst){
//		DatabaseMetaData conMD = con.getMetaData();
//		String dbproduct = conMD.getDatabaseProductName().toLowerCase();
		StringBuffer sql = new StringBuffer();
//		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT asm_id, COUNT(asu_asm_id) AS total, usr_display_bil, asm_status, ")
			   .append(" asm_title, asm_eff_start_datetime, asm_eff_end_datetime ")
			   .append(" FROM RegUser, cmSkillSet , cmAssessmentUnit RIGHT JOIN cmAssessment ")
			   .append(" ON asu_asm_id = asm_id ");
			if(hasTc){
				sql.append("  ,entityrelation, tcTrainingCenterTargetEntity");
			}
			sql.append(" WHERE asm_ent_id = usr_ent_id ")
			   .append(" AND asm_sks_skb_id = sks_skb_id ")
			   .append(" AND sks_owner_ent_id = ? ")
			   .append(" AND asu_type <> ? ");
			if(hasTc){
				sql.append("  and ern_type='USR_PARENT_USG' and ern_child_ent_id= usr_ent_id and ern_ancestor_ent_id=tce_ent_id and tce_tcr_id in ").append(tcr_id_lst);
			}
	/*	}else{
			sql.append(" SELECT asm_id, COUNT(asu_asm_id) AS total, usr_display_bil, asm_status, ")
			   .append(" asm_title, asm_eff_start_datetime, asm_eff_end_datetime ")
			   .append(" FROM RegUser, cmAssessment, cmAssessmentUnit, cmSkillSet ");
			if(hasTc){
				sql.append("  ,entityrelation, tcTrainingCenterTargetEntity");
			}
			sql .append(" WHERE asu_asm_id ").append(cwSQL.get_right_join(con)).append(" asm_id ")
			   .append(" AND asm_ent_id = usr_ent_id ")
			   .append(" AND asm_sks_skb_id = sks_skb_id ")
			   .append(" AND sks_owner_ent_id = ? ")
			   .append(" AND asu_type <> ? ");
			if(hasTc){
				sql.append("  and ern_type='USR_PARENT_USG' and ern_child_ent_id= usr_ent_id and ern_ancestor_ent_id=tce_ent_id and tce_tcr_id in ").append(tcr_id_lst);
			}
			
		}*/
		return sql.toString();
	}
	
	public static String viewCmSkillGetSkillsLevel() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT skb_id, skb_title, ")
			   .append(" skb_description, max(sle_level)total, skb_type ")
			   .append(" FROM cmSkillLevel RIGHT JOIN cmSkillBase ")
			   .append(" ON (sle_ssl_id = skb_ssl_id ) WHERE 1=1 ");
		}else{
			sql.append(" SELECT skb_id, skb_title, ")
			   .append(" skb_description, max(sle_level)total, skb_type ")
			   .append(" FROM cmSkillBase, cmSkillLevel ")
			   .append(" WHERE sle_ssl_id ").append(cwSQL.get_right_join()).append(" skb_ssl_id ");
		}
		return sql.toString();
	}
	
	public static String viewCmSkillCoverageGetSkillSetInfo() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT COUNT(ssc_sks_skb_id), sks_skb_id, sks_title ")
			   .append(" FROM cmSkillSetCoverage RIGHT JOIN cmSkillSet ")
			   .append(" ON (ssc_sks_skb_id = sks_skb_id) ")
			   .append(" WHERE sks_type = ? ");
		}else{
			sql.append(" SELECT COUNT(ssc_sks_skb_id), sks_skb_id, sks_title ")
			   .append(" FROM cmSkillSet , cmSkillSetCoverage ")
			   .append(" WHERE ssc_sks_skb_id ").append(cwSQL.get_right_join()).append(" sks_skb_id AND sks_type = ? ");
		}
		return sql.toString();
	}

	public static String dbCourseSubmissionAsXML() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		/*if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT DISTINCT usr_id, usr_ent_id, pgr_usr_id , pgr_res_id, tkh_id, ")
			   .append(" pgr_attempt_nbr, pgr_status, pgr_tkh_id, usr_display_bil ")
			   .append(" FROM Enrolment, TrackingHistory , Progress A RIGHT JOIN RegUser ")
			   .append(" ON (pgr_usr_id=usr_id  AND pgr_res_id = ?  and pgr_tkh_id = tkh_id) ")
			   .append(" WHERE usr_ent_id = enr_ent_id ")
			   .append(" AND enr_res_id = ? ")
                 .append(" AND enr_ent_id = tkh_usr_ent_id ")
                  .append(" AND enr_res_id = tkh_cos_res_id ")
			   .append("");// randy add :get last Enrolment
              //.append(" AND A.pgr_tkh_id = (SELECT MAX(pgr_tkh_id) from Progress B where A.pgr_res_id = B.pgr_res_id AND A.pgr_usr_id = B.pgr_usr_id) ");
		}else{
			sql.append(" SELECT DISTINCT usr_id, usr_ent_id, pgr_usr_id , pgr_res_id, tkh_id,")
			   .append(" pgr_attempt_nbr, pgr_status, pgr_tkh_id, usr_display_bil ")
			   .append(" FROM RegUser, Progress A , Enrolment,  TrackingHistory ")
			   .append(" WHERE usr_ent_id = enr_ent_id ")
			   
			   .append(" AND A.pgr_usr_id" + cwSQL.get_right_join(con) + "usr_id ")
			   .append(" AND A.pgr_res_id = ? ")// randy add :get last Enrolment
               .append(" AND A.pgr_tkh_id " + cwSQL.get_right_join(con) + "tkh_id")//(SELECT MAX(pgr_tkh_id) from Progress B where A.pgr_res_id = B.pgr_res_id AND A.pgr_usr_id = B.pgr_usr_id) ")
              .append(" AND enr_res_id = ? ")
               .append(" AND enr_ent_id = tkh_usr_ent_id ")
                  .append(" AND enr_res_id = tkh_cos_res_id ")
              ;
		}*/
		sql.append(" select tkh_id, usr_ent_id, usr_display_bil, pgr_usr_id, pgr_tkh_id, pgr_attempt_nbr, pgr_status, pgr_res_id ,usr_status")
		.append(" from ")
		.append(" TrackingHistory ")
		.append(" inner join Enrolment on (tkh_usr_ent_id = enr_ent_id ) ")
		.append(" inner join RegUser on (tkh_usr_ent_id = usr_ent_id) ")
		.append(" inner join aeApplication on (tkh_id = app_tkh_id)")
		.append(" left join Progress on (tkh_id = pgr_tkh_id and pgr_res_id = ?) ")
		.append(" where ")
		.append(" tkh_cos_res_id = enr_res_id ")
		.append(" and tkh_cos_res_id = ? ")
	    .append(" and  usr_display_bil  like ? ")
		.append(" order by usr_display_bil ");
		return sql.toString();
	}

	public static String dbCourseEvaluationGetSingleLearnerCourseRptAsXML() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sql.append(" SELECT cov_status, cov_score, res_title, cov_total_time, cov_last_acc_datetime, cov_comment , cov_update_timestamp ")
			   .append(" FROM CourseEvaluation, Resources ")
			   .append(" WHERE cov_cos_id " + cwSQL.get_right_join() + " res_id ")
			   .append(" AND res_id = ? ")
			   .append(" AND cov_ent_id = ? " );
		
		}else{
			sql.append(" SELECT cov_status, cov_score, res_title, cov_total_time, cov_last_acc_datetime, cov_comment , cov_update_timestamp ")
			   .append(" FROM CourseEvaluation RIGHT JOIN Resources ")
			   .append(" ON (cov_cos_id = res_id) ")
			   .append(" WHERE res_id = ? ")
			   .append(" AND cov_ent_id = ? " );
		}
			
		return sql.toString();
	}

	public static String dbCourseEvaluationGetGroupXML() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sql.append(" SELECT  count(usr_ent_id) as s ")
			   .append(" ,usg_ent_id ")
			   .append(" ,usg_display_bil ")
			   .append(" FROM CourseEvaluation, RegUser, EntityRelation, UserGroup, Enrolment ")
			   .append(" WHERE cov_ent_id " + cwSQL.get_right_join() + " enr_ent_id ")
			   .append(" AND cov_cos_id = ? ")
			   .append(" AND enr_res_id = ? ")
			   .append(" AND usr_ent_id = enr_ent_id ")
			   .append(" AND usr_ent_id = ern_child_ent_id ")
			   .append(" AND usg_ent_id = ern_ancestor_ent_id ")
			   .append(" AND ern_parent_ind = ? ")
			   .append(" Group by usg_ent_id, usg_display_bil ")
			   .append(" ORDER by usg_display_bil, usg_ent_id ");
			
		}else{
			sql.append(" SELECT  count(usr_ent_id) as s ")
			   .append(" ,usg_ent_id ")
			   .append(" ,usg_display_bil ")
			   .append(" FROM RegUser, EntityRelation, UserGroup, CourseEvaluation RIGHT JOIN Enrolment ")
			   .append(" ON (cov_ent_id = enr_ent_id) ")
			   .append(" WHERE cov_cos_id = ? ")
			   .append(" AND enr_res_id = ? ")
			   .append(" AND usr_ent_id = enr_ent_id ")
			   .append(" AND usr_ent_id = ern_child_ent_id ")
			   .append(" AND usg_ent_id = ern_ancestor_ent_id ")
			   .append(" AND ern_parent_ind = ? ")
			   .append(" Group by usg_ent_id, usg_display_bil ")
			   .append(" ORDER by usg_display_bil, usg_ent_id ");
		}
		return sql.toString();
	}

	public static String dbCourseEvaluationGetLearnerXML() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sql.append(" SELECT usg_ent_id, usg_display_bil, usr_ent_id, usr_display_bil, usr_id, usr_ste_usr_id, ")
			   .append(" cov_status, cov_score, cov_total_time, cov_last_acc_datetime, cov_update_timestamp ")
			   .append(" FROM CourseEvaluation, RegUser, EntityRelation, UserGroup, Enrolment ")
			   .append(" WHERE cov_ent_id " + cwSQL.get_right_join() + " enr_ent_id ")
			   .append(" AND cov_cos_id = ? ")
			   .append(" AND enr_res_id = ? ")
			   .append(" AND usr_ent_id = enr_ent_id ")
			   .append(" AND usr_ent_id = ern_child_ent_id ")
			   .append(" AND usg_ent_id = ern_ancestor_ent_id ")
			   .append(" AND usg_ent_id = ? ")
			   .append(" AND ern_parent_ind = ? ");
		}else{
			sql.append(" SELECT usg_ent_id, usg_display_bil, usr_ent_id, usr_display_bil, usr_id, usr_ste_usr_id, ")
			   .append(" cov_status, cov_score, cov_total_time, cov_last_acc_datetime, cov_update_timestamp ")
			   .append(" FROM RegUser, EntityRelation, UserGroup, CourseEvaluation RIGHT JOIN Enrolment ")
			   .append(" ON (cov_ent_id = enr_ent_id) ")
			   .append(" WHERE cov_cos_id = ? ")
			   .append(" AND enr_res_id = ? ")
			   .append(" AND usr_ent_id = enr_ent_id ")
			   .append(" AND usr_ent_id = ern_child_ent_id ")
			   .append(" AND usg_ent_id = ern_ancestor_ent_id ")
			   .append(" AND usg_ent_id = ? ")
			   .append(" AND ern_parent_ind = ? ");
			
		}
		return sql.toString();
	}

	public static String dbCourseEvaluationGetLearnerXML2() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sql.append(" SELECT tkh_id , usr_ent_id, usr_display_bil, usr_id, usr_ste_usr_id, ern_ancestor_ent_id, ")
			   .append(" ste_name, cov_status, cov_score, cov_total_time, cov_commence_datetime, ")
			   .append(" cov_last_acc_datetime, cov_complete_datetime, cov_update_timestamp ")
			   .append(" FROM CourseEvaluation, RegUser, TrackingHistory, EntityRelation , acSite, aeApplication ")
			   .append(" WHERE cov_tkh_id " + cwSQL.get_right_join() + " tkh_id ")
			   .append(" AND usr_ent_id = ern_child_ent_id ")
			   .append(" AND usr_ste_ent_id = ste_ent_id ")
			   .append(" AND ern_type = ? ")
			   .append(" AND ern_parent_ind = ? ")
			   .append(" AND tkh_cos_res_id = ? ")
			   .append(" AND tkh_usr_ent_id = usr_ent_id ")
			   .append(" AND tkh_type = ? ")
			   .append(" AND app_tkh_id = tkh_id");
		}else{
			
			sql.append(" SELECT tkh_id , usr_ent_id, usr_display_bil, usr_id, usr_ste_usr_id,  ern_ancestor_ent_id, ")
			   .append(" ste_name, cov_status, cov_score, cov_total_time, cov_commence_datetime, ")
			   .append(" cov_last_acc_datetime, cov_complete_datetime, cov_update_timestamp ")
			   .append(" FROM RegUser, EntityRelation, acSite, aeApplication, CourseEvaluation RIGHT JOIN TrackingHistory ")
			   .append(" ON (cov_tkh_id = tkh_id) ")
			   .append(" WHERE usr_ent_id = ern_child_ent_id ")
			   .append(" AND usr_ste_ent_id = ste_ent_id ")
			   .append(" AND ern_type = ? ")
			   .append(" AND ern_parent_ind = ? ")
			   .append(" AND tkh_cos_res_id = ? ")
			   .append(" AND tkh_usr_ent_id = usr_ent_id ")
			   .append(" AND tkh_type = ? ")
			   .append(" AND app_tkh_id = tkh_id");
			
		}
		return sql.toString();
	}

	public static String dbModuleGetInstructorName() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT res_instructor_name, usr_display_bil, mod_usr_id_instructor ")
			   .append(" FROM Resources, RegUser RIGHT JOIN Module")
			   .append(" ON (usr_id = mod_usr_id_instructor) ")
			   .append(" WHERE mod_res_id = res_id ")
			   .append("   AND mod_res_id = ? ");
		}else{
			sql.append(" SELECT res_instructor_name, usr_display_bil, mod_usr_id_instructor ")
			   .append(" FROM Resources, Module, RegUser ")
			   .append(" WHERE mod_res_id = res_id ")
			   .append("   AND mod_res_id = ? ")
			   .append("   AND usr_id " + cwSQL.get_right_join() + " mod_usr_id_instructor ");
		}
		return sql.toString();
	}
	
	public static String dbModuleEvaluationGetModule(Vector modIdVec) {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT count(*) AS result_count, rcn_res_id_content mov_mod_id, res_title, res_subtype, ")
			   .append(" res_src_type, mov_status, sum(mov_total_attempt) total_attempt, max(mov_last_acc_datetime) last_access, ")
			   .append(" sum(mov_total_time) total_time ")
			   .append(" FROM Resources, ModuleEvaluation RIGHT JOIN TrackingHistory ")
			   .append(" ON (mov_ent_id = tkh_usr_ent_id) ")
			   .append(" RIGHT JOIN ResourceContent ")
			   .append(" ON (mov_mod_id = rcn_res_id_content) ")
			   .append(" RIGHT JOIN TrackingHistory ")
			   .append(" ON (mov_tkh_id = tkh_id) ")
			   .append(" WHERE rcn_res_id = ? ")
			   .append(" AND rcn_res_id = tkh_cos_res_id ")
			   .append(" AND res_id = rcn_res_id_content ")
			   .append(" rcn_res_id_content IN " + dbUtils.vec2String(modIdVec))
			   .append(" AND tkh_type = ? ")
			   .append(" GROUP BY rcn_res_id_content, mov_status, res_title, res_src_type, res_subtype ")
			   .append(" ORDER BY res_title, rcn_res_id_content");
		}else{
			sql.append(" SELECT count(*) AS result_count, rcn_res_id_content mov_mod_id, res_title, res_subtype, ")
			   .append(" res_src_type, mov_status, sum(mov_total_attempt) total_attempt, max(mov_last_acc_datetime) last_access, ")
			   .append(" sum(mov_total_time) total_time ")
			   .append(" FROM ModuleEvaluation, Resources, ResourceContent, TrackingHistory ")
			   .append(" WHERE rcn_res_id = ? ")
			   .append(" AND rcn_res_id = tkh_cos_res_id ")
			   .append(" AND res_id = rcn_res_id_content ")
			   .append(" AND mov_ent_id " + cwSQL.get_right_join() + " tkh_usr_ent_id ")
			   .append(" AND mov_mod_id " + cwSQL.get_right_join() + " rcn_res_id_content ")
			   .append(" AND mov_tkh_id " + cwSQL.get_right_join() + " tkh_id AND ")
			   .append(" rcn_res_id_content IN " + dbUtils.vec2String(modIdVec))
			   .append(" AND tkh_type = ? ")
			   .append(" GROUP BY rcn_res_id_content, mov_status, res_title, res_src_type, res_subtype ")
			   .append(" ORDER BY res_title, rcn_res_id_content");
		}
		return sql.toString();
	}
	
	public static String dbModuleEvaluationGetLearnerModule() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sql.append(" SELECT rcn_res_id_content mov_mod_id, res_title, res_subtype, res_src_type, res_src_link, ")
			   .append(" mov_status, mov_score, mov_total_attempt total_attempt, mov_last_acc_datetime last_access, ")
			   .append(" mov_total_time total_time, mod_eff_start_datetime, mod_eff_end_datetime, mod_web_launch ")
			   .append(" FROM moduleEvaluation, resources, resourceContent, Module ")
			   .append(" WHERE rcn_res_id = ? ")
			   .append(" AND mov_ent_id = ? ")
			   .append(" AND mov_tkh_id = ? ")
			   .append(" AND ")
			   .append(" mov_mod_id " + cwSQL.get_right_join() + " rcn_res_id_content AND ")
			   .append(" res_id " + cwSQL.get_right_join() + " rcn_res_id_content AND")
			   .append(" mod_res_id " + cwSQL.get_right_join() + " rcn_res_id_content")
			   .append(" ORDER BY res_title, rcn_res_id_content");
			
		}else{
			
			
			sql.append(" SELECT rcn_res_id_content mov_mod_id, res_title, res_subtype, res_src_type, res_src_link, ")
			   .append(" mov_status, mov_score, mov_total_attempt total_attempt, mov_last_acc_datetime last_access, ")
			   .append(" mov_total_time total_time, mod_eff_start_datetime, mod_eff_end_datetime, mod_web_launch ")
			   .append(" FROM resourceContent LEFT JOIN moduleEvaluation")
			   .append(" ON (rcn_res_id_content = mov_mod_id) ")
			   .append(" LEFT JOIN resources ")
			   .append(" ON (rcn_res_id_content = res_id) ")
			   .append(" LEFT JOIN Module ")
			   .append(" ON (rcn_res_id_content = mod_res_id) ")
			   .append(" WHERE rcn_res_id = ? ")
			   .append(" AND mov_ent_id = ? ")
			   .append(" AND mov_tkh_id = ? ")
			   .append(" ORDER BY res_title, rcn_res_id_content");
		}
		return sql.toString();
	}

	public static String dbUserGroupGetUserVec() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT ent_type, usr_id ")
			   .append(" FROM RegUser RIGHT JOIN Entity ")
			   .append(" ON (usr_ent_id = ent_id) ")
			   .append(" WHERE ent_id = ? ")
			   .append(" AND ent_delete_usr_id IS NULL ")
			   .append(" AND ent_delete_timestamp IS NULL ");
		}else{
			sql.append(" SELECT ent_type, usr_id FROM Entity, RegUser ")
			   .append(" WHERE ent_id = ? ")
			   .append(" AND ent_delete_usr_id IS NULL ")
			   .append(" AND ent_delete_timestamp IS NULL ")
			   .append(" AND usr_ent_id " + cwSQL.get_right_join() + " ent_id ");
		}
		return sql.toString();
	}

	public static String dbUserGroupGetUserVec2() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT ent_type AS MEM_TYPE , ern_child_ent_id AS MEM_ID, usr_id AS USR_ID ")
			   .append(" FROM EntityRelation, RegUser RIGHT JOIN Entity ")
			   .append(" ON (usr_ent_id = ent_id) ")
			   .append(" WHERE ern_ancestor_ent_id = ? ")
			   .append(" AND  ern_child_ent_id = ent_id ")
			   .append(" AND ent_delete_usr_id IS NULL ")
			   .append(" AND ent_delete_timestamp IS NULL ")
			   .append(" AND ern_parent_ind = ? ")
			   .append(" UNION ")
			   .append(" SELECT ent_type AS MEM_TYPE , usr_ent_id AS MEM_ID, usr_id AS USR_ID ")
			   .append(" FROM Entity, RegUser ")
			   .append(" WHERE usr_ent_id = ? ")
			   .append(" AND usr_ent_id = ent_id ")
			   .append(" AND ent_delete_usr_id IS NULL ")
			   .append(" AND ent_delete_timestamp IS NULL ");
		}else{
			sql.append(" SELECT ent_type AS MEM_TYPE , ern_child_ent_id AS MEM_ID, usr_id AS USR_ID ")
			   .append(" FROM EntityRelation, Entity, RegUser ")
			   .append(" WHERE ern_ancestor_ent_id = ? ")
			   .append(" AND  ern_child_ent_id = ent_id ")
			   .append(" AND ent_delete_usr_id IS NULL ")
			   .append(" AND ent_delete_timestamp IS NULL ")
			   .append(" AND  usr_ent_id " + cwSQL.get_right_join() + " ent_id ")
			   .append(" AND ern_parent_ind = ? ")
			   .append(" UNION ")
			   .append(" SELECT ent_type AS MEM_TYPE , usr_ent_id AS MEM_ID, usr_id AS USR_ID ")
			   .append(" FROM Entity, RegUser ")
			   .append(" WHERE usr_ent_id = ? ")
			   .append(" AND usr_ent_id = ent_id ")
			   .append(" AND ent_delete_usr_id IS NULL ")
			   .append(" AND ent_delete_timestamp IS NULL ");
		}
		return sql.toString();
	}

	public static String qdbQuizGetQuizContentInXML() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT res_id, res_title, res_difficulty, res_usr_id_owner, usr_display_bil, res_status, ")
			   .append(" res_upd_date, que_xml, que_score, int_xml_outcome, int_xml_explain, qse_obj_id_ass, obj_desc ")
			   .append(" FROM  Question, Interaction, RegUser RIGHT JOIN Resources")
			   .append(" ON (usr_id=res_usr_id_owner), ")
			   .append(" Objective RIGHT JOIN QSequence ")
			   .append(" ON (obj_id=qse_obj_id_cat) ")
			   .append(" WHERE res_id = que_res_id ")
			   .append(" AND res_id = int_res_id ")
			   .append(" AND int_order = 1 ")
			   .append(" AND res_id = qse_que_res_id ")
			   .append(" AND qse_obj_id  = ? ")
			   .append(" AND qse_order   BETWEEN ? AND ? ");
		}else{
			sql.append(" SELECT res_id, res_title, res_difficulty, res_usr_id_owner, usr_display_bil, res_status, ")
			   .append(" res_upd_date, que_xml, que_score, int_xml_outcome, int_xml_explain, qse_obj_id_ass, obj_desc ")
			   .append(" FROM  Resources, Question, Interaction, QSequence, Objective, RegUser ")
			   .append(" WHERE res_id = que_res_id ")
			   .append(" AND res_id = int_res_id ")
			   .append(" AND usr_id " + cwSQL.get_right_join() + " res_usr_id_owner ")
			   .append(" AND int_order = 1 ")
			   .append(" AND res_id = qse_que_res_id ")
			   .append(" AND obj_id " + cwSQL.get_right_join() +  " qse_obj_id_cat ")
			   .append(" AND qse_obj_id  = ? ")
			   .append(" AND qse_order   BETWEEN ? AND ? ");
		}
		return sql.toString();
	}
	
	public static String dbResourceGetCosContentListAsXMLSQL(String tableName) {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer sql = new StringBuffer();
		if (dbproduct.indexOf(ProductName_DB2) >= 0) {
			sql.append(" SELECT mod_res_id, res_instructor_name, usr_display_bil, mod_usr_id_instructor ")
			   .append(" FROM Resources, RegUser RIGHT JOIN Module ")
			   .append(" ON (usr_id = mod_usr_id_instructor) ")
			   .append(" WHERE mod_res_id = res_id ")
			   .append(" AND mod_res_id in (select tmp_res_id from " + tableName + ") ");
		} else {
			sql.append("SELECT mod_res_id, res_instructor_name, usr_display_bil, mod_usr_id_instructor FROM Resources, Module, RegUser WHERE mod_res_id = res_id AND mod_res_id in (select tmp_res_id from " + tableName + ") AND usr_id " + cwSQL.get_right_join() + " mod_usr_id_instructor");
		}
		return sql.toString();
	}

    public static String getOfflineEvalItem(long itm_id) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer SQL = new StringBuffer(1024);
        
    	SQL.append(" select app_id,usr_display_bil,usr_ent_id,mtv_score,mtv_tkh_id,cov_score  ")
    	   .append(" from ")
    	   .append(" courseMeasurement ")
    	   .append(" inner join aeApplication on (app_itm_id = ")
		   .append(itm_id)
		   .append(" and app_tkh_id is not null ) ")
		   .append(" inner join RegUser on (app_ent_id = usr_ent_id) ")
		   .append(" inner join courseEvaluation on (cov_tkh_id = app_tkh_id) ")
		   .append(" left join measurementEvaluation on (mtv_cmt_id = cmt_id and mtv_ent_id = usr_ent_id and app_tkh_id = mtv_tkh_id ) ")
		   .append(" where cmt_id = ? ")
		   .append(" ORDER BY usr_display_bil ASC, app_id ");
        
        return SQL.toString();
    }
    public static String getOnlineEvalItem(long itm_id) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer SQL = new StringBuffer(1024);
        
        SQL.append(" select app_id,usr_display_bil,usr_ent_id,mov_score,mov_tkh_id,cov_score  ")
            .append(" from ")
            .append(" courseMeasurement ")
            .append(" inner join courseModuleCriteria on (cmt_cmr_id = cmr_id)")
            .append(" inner join aeApplication on (app_itm_id =")
            .append(itm_id) 
            .append(" and app_tkh_id is not null) ")
            .append(" inner join RegUser on (app_ent_id = usr_ent_id) ")
            .append(" inner join courseEvaluation on (cov_tkh_id = app_tkh_id) ")                
            .append(" left join moduleEvaluation on (mov_mod_id  = cmr_res_id and mov_ent_id = usr_ent_id and app_tkh_id = mov_tkh_id) ")
            .append(" where cmt_id = ? ")
            .append(" and cmt_cmr_id = cmr_id ")
            .append(" ORDER BY usr_display_bil ASC, app_id ");
        
        return SQL.toString();
    }

}



