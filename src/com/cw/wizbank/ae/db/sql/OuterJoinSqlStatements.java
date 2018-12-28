package com.cw.wizbank.ae.db.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.report.TrainFeeStatReport;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

public class OuterJoinSqlStatements {
	private static final int LIST_SIZE = 999;

	/**
	Database Vendor : Microsft SQL Server
	*/
	private static final String DBVENDOR_MSSQL = "MSSQL";
	/**
	Database Vendor : Oracle
	*/
	private static final String DBVENDOR_ORACLE = "ORACLE";
	/**
	Database Vendor : DB2
	*/
	private static final String DBVENDOR_DB2 = "DB2";

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

	public static String getApplicationAndAttendance(
		String itm_lst) {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer result = new StringBuffer();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			result.append(
				"SELECT item1.itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp ,att_ats_id FROM aeItem item1, aeItem item2, aeApplication, aeAttendance, aeAttendanceStatus WHERE item1.itm_id in ");
			result.append(itm_lst);
			result.append(
				" AND item1.itm_code = item2.itm_code AND item2.itm_id = app_itm_id AND app_ent_id = ? AND att_app_id (+)= app_id AND att_itm_id (+)= app_itm_id AND ats_id (+)= att_ats_id");
			result.append(" UNION ");
			result.append(
				"SELECT item1.itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp , att_ats_id FROM aeItem item1, aeItem item2, aeItemRelation, aeApplication, aeAttendance, aeAttendanceStatus WHERE item1.itm_id in ");
			result.append(itm_lst);
			result.append(
				" AND item1.itm_code = item2.itm_code AND item2.itm_id = ire_parent_itm_id AND ire_child_itm_id = app_itm_id AND app_ent_id = ? AND att_app_id (+)= app_id AND att_itm_id (+)= app_itm_id AND ats_id (+)= att_ats_id");
		} else {
			result.append(
				"SELECT item1.itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp, att_ats_id FROM aeItem item1 INNER JOIN aeItem item2 ON (item1.itm_id in ");
			result.append(itm_lst);
			result.append(
				" AND item1.itm_code = item2.itm_code) INNER JOIN aeApplication ON (item2.itm_id = app_itm_id) LEFT JOIN aeAttendance ON (att_app_id = app_id AND att_itm_id = app_itm_id ) LEFT JOIN aeAttendanceStatus ON (att_ats_id = ats_id) WHERE app_ent_id = ?");
			result.append(" UNION ");
			result.append(
				"SELECT item1.itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp , att_ats_id FROM aeItem item1 INNER JOIN aeItem item2 ON (item1.itm_id in ");
			result.append(itm_lst);
			result.append(
				" AND item1.itm_code = item2.itm_code) INNER JOIN aeItemRelation ON (item2.itm_id = ire_parent_itm_id) INNER JOIN aeApplication ON (ire_child_itm_id = app_itm_id) LEFT JOIN aeAttendance ON (att_app_id = app_id AND att_itm_id = app_itm_id ) LEFT JOIN aeAttendanceStatus ON (att_ats_id = ats_id) WHERE app_ent_id = ?");
		}

		return result.toString();
	}

    public static String getApplicationAndAttendance(
        String itm_ext1,
        String itm_ext2) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer result = new StringBuffer();
        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            //result.append("SELECT ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp,att_ats_id, itm_eff_start_datetime, itm_eff_end_datetime, itm_title, itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration, app_tkh_id FROM aeApplication, aeItem, aeItemRelation, aeAttendance, aeAttendanceStatus WHERE app_itm_id = itm_id AND itm_deprecated_ind = 0 AND app_ent_id = ? AND ire_child_itm_id (+)= itm_id AND att_app_id (+)= app_id AND att_itm_id (+)= app_itm_id AND ats_id (+)= att_ats_id order by app_id DESC");
            result.append(
                "SELECT ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp,att_ats_id, child.itm_eff_start_datetime, child.itm_eff_end_datetime, child.itm_title, child.itm_content_eff_start_datetime, child.itm_content_eff_end_datetime, child.itm_content_eff_duration, app_tkh_id "
                    + "FROM aeApplication,aeItem child,aeItemRelation,aeItem parent,aeAttendance,aeAttendanceStatus  "
                    + "where app_itm_id = child.itm_id AND child.itm_deprecated_ind = 0 and child.itm_run_ind =1 "
                    + "and child.itm_id = ire_child_itm_id "
                    + "and ire_parent_itm_id = parent.itm_id "
                    + "and att_app_id(+) = app_id AND att_itm_id(+) = app_itm_id "
                    + "and att_ats_id = ats_id(+) "
                    + "and app_ent_id = ? "
                    + "and app_status in (?, ?, ?) ");
            if (itm_ext1 != null && !itm_ext1.equals("")) {
                result.append("and parent.itm_ext1 ='" + itm_ext1 + "' ");
            }
            if (itm_ext2 != null && !itm_ext2.equals("")) {
                result.append("and parent.itm_ext2 ='" + itm_ext2 + "' ");
            }

            result.append(" union ");

            result.append(
                "SELECT 0 as ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp,att_ats_id, itm_eff_start_datetime, itm_eff_end_datetime, itm_title, itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration, app_tkh_id "
                    + "FROM aeApplication, aeItem parent, aeAttendance, aeAttendanceStatus "
                    + "where app_itm_id = parent.itm_id AND parent.itm_deprecated_ind = 0 and parent.itm_run_ind = 0 "
                    + "and att_app_id(+) = app_id AND att_itm_id(+) = app_itm_id "
                    + "and att_ats_id = ats_id(+) "
                    + "and app_ent_id = ? "
                    + "and app_status in (?, ?, ?) ");

            if (itm_ext1 != null && !itm_ext1.equals("")) {
                result.append("and parent.itm_ext1 ='" + itm_ext1 + "' ");
            }
            if (itm_ext2 != null && !itm_ext2.equals("")) {
                result.append("and parent.itm_ext2 ='" + itm_ext2 + "' ");
            }
            result.append(" order by 15 ");
            //result.append("SELECT ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, att_update_timestamp, itm_eff_start_datetime, itm_eff_end_datetime, itm_title FROM aeApplication, aeItem, aeItemRelation, aeAttendance, aeAttendanceStatus WHERE app_itm_id = itm_id AND app_ent_id = ? AND ire_child_itm_id (+)= itm_id AND att_app_id (+)= app_id AND ats_id (+)= att_ats_id order by app_id DESC");
        } else {

            //result.append("SELECT ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp,att_ats_id, itm_eff_start_datetime, itm_eff_end_datetime, itm_title, itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration, app_tkh_id FROM aeApplication INNER JOIN aeItem ON (app_itm_id = itm_id AND itm_deprecated_ind = 0) LEFT JOIN aeItemRelation ON (itm_id = ire_child_itm_id) LEFT JOIN aeAttendance ON (att_app_id = app_id AND att_itm_id = app_itm_id ) LEFT JOIN aeAttendanceStatus ON (att_ats_id = ats_id) WHERE app_ent_id = ? order by app_id DESC");
            //result.append("SELECT ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, att_update_timestamp, itm_eff_start_datetime, itm_eff_end_datetime, itm_title FROM aeApplication INNER JOIN aeItem ON (app_itm_id = itm_id) LEFT JOIN aeItemRelation ON (itm_id = ire_child_itm_id) LEFT JOIN aeAttendance ON (att_app_id = app_id) LEFT JOIN aeAttendanceStatus ON (att_ats_id = ats_id) WHERE app_ent_id = ? order by app_id DESC");
            result.append(
                "SELECT ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp,att_ats_id, child.itm_eff_start_datetime, child.itm_eff_end_datetime, child.itm_title, child.itm_content_eff_start_datetime, child.itm_content_eff_end_datetime, child.itm_content_eff_duration, app_tkh_id "
                    + "FROM aeApplication  INNER JOIN aeItem child ON (app_itm_id = itm_id AND itm_deprecated_ind = 0 and itm_run_ind =1) "
                    + "INNER JOIN aeItemRelation ON (itm_id = ire_child_itm_id) "
                    + "INNER JOIN aeItem parent ON (ire_parent_itm_id = parent.itm_id) "
                    + "LEFT JOIN aeAttendance ON (att_app_id = app_id AND att_itm_id = app_itm_id ) "
                    + "LEFT JOIN aeAttendanceStatus ON (att_ats_id = ats_id) "
                    + "WHERE app_ent_id = ? "
                    + "AND app_status in (?, ?, ?) ");

            if (itm_ext1 != null && !itm_ext1.equals("")) {
                result.append("and parent.itm_ext1 ='" + itm_ext1 + "' ");
            }
            if (itm_ext2 != null && !itm_ext2.equals("")) {
                result.append("and parent.itm_ext2 ='" + itm_ext2 + "' ");
            }

            result.append("union all ");

            result.append(
                "SELECT 0 as ire_parent_itm_id, app_itm_id, app_id, app_status, app_process_status, app_create_timestamp, ats_attend_ind, ats_type, att_timestamp, att_create_timestamp,att_ats_id, itm_eff_start_datetime, itm_eff_end_datetime, itm_title, itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration, app_tkh_id "
                    + "FROM aeApplication  INNER JOIN aeItem parent ON (app_itm_id = itm_id AND itm_deprecated_ind = 0 and itm_run_ind = 0) "
                    + "LEFT JOIN aeAttendance ON (att_app_id = app_id AND att_itm_id = app_itm_id ) "
                    + "LEFT JOIN aeAttendanceStatus ON (att_ats_id = ats_id) "
                    + "WHERE app_ent_id = ? "
                    + "AND app_status in (?, ?, ?) ");

            if (itm_ext1 != null && !itm_ext1.equals("")) {
                result.append("and parent.itm_ext1 ='" + itm_ext1 + "' ");
            }
            if (itm_ext2 != null && !itm_ext2.equals("")) {
                result.append("and parent.itm_ext2 ='" + itm_ext2 + "' ");
            }
            result.append(" order by 15 ");
        }
        return result.toString();
    }

	public static String getItemInfo(String itm_lst) {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer result = new StringBuffer();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			result.append(
				"SELECT itm_id, itm_code, itm_title, itm_type, cos_res_id, itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration, itm_eff_start_datetime, itm_eff_end_datetime, itm_apply_method, itm_unit, itm_status, itm_create_run_ind, itm_run_ind, itm_life_status,itm_tcr_id, tcr_title  itm_tcr_title from aeItem, Course, tcTrainingCenter WHERE itm_tcr_id (+)= tcr_id and  cos_itm_id (+)= itm_id and itm_id in ");
		} else {
			result.append(
				"SELECT itm_id, itm_code, itm_title, itm_type, cos_res_id, itm_content_eff_start_datetime, itm_content_eff_end_datetime, itm_content_eff_duration, itm_eff_start_datetime, itm_eff_end_datetime, itm_apply_method, itm_unit, itm_status, itm_create_run_ind, itm_run_ind, itm_life_status,itm_tcr_id, tcr_title  itm_tcr_title  from aeItem LEFT JOIN tcTrainingCenter ON(itm_tcr_id = tcr_id ) LEFT JOIN Course ON (cos_itm_id = itm_id) where itm_id in ");
		}

		result.append(itm_lst.toString()).append(" order by itm_title ");

		return result.toString();
	}
	  /**get total staticstic Course
       *will staticstic the item total, user total, study record total, total time, total scroe 
      */
	  //sql1。课程总数，学员总数，学习记录总数，总时长，平均成绩
	  public static String getTotalStatisticCos(
			  Connection con,
			  String entIdColName, 
			  String entIdTableName,
			  Vector itm_lst_vec,
			  Vector ats_lst,
			  Timestamp att_create_start_datetime,
			  Timestamp att_create_end_datetime,
			  String tableName,
			  String tableName1,
			  long usr_ent_id, long root_ent_id,
              boolean include_no_record,
              boolean tc_enabled) throws SQLException{
		  boolean show_all_info = false;
		  String attempts_sql = getLeftModuleEvaluation();
		  String cos_sql = getCosColumnInfo(con, show_all_info, include_no_record, itm_lst_vec, ats_lst,att_create_start_datetime, att_create_end_datetime, tableName, entIdColName, entIdTableName,usr_ent_id, root_ent_id, tc_enabled);
		  String condition_sql = getCosConditionSql(tableName1, tc_enabled);
		  StringBuffer sql = new StringBuffer(512);		  
		  
		  sql.append(" SELECT COUNT(DISTINCT app_ent_id) AS user_total, ")
		     .append(" COUNT(DISTINCT item1.itm_id) AS item_total, ")
		     .append(" COUNT(DISTINCT app.app_tkh_id) AS total_enroll,SUM(cov_score) AS total_score, ")
		     .append(" SUM(cov_total_time) AS total_time, ")
		     .append(" SUM(attempt.with_attempts) AS attempts_user, ")
		     .append(" SUM(attempt.all_attempts) AS total_attempt ")
		     .append(" FROM aeItem item1 ");
		  sql.append(cos_sql).append(attempts_sql).append(condition_sql);
		  return sql.toString();
	  }
      //sql4。全部列，需要程序统计，只显示100条记录
	  public static String learnerReportSearchCosStatistic(
					  Connection con,
					  String entIdColName,
					  String entIdTableName,
					  Vector itm_lst_vec,
					  Vector ats_lst,
					  Timestamp att_create_start_datetime,
					  Timestamp att_create_end_datetime,
					  String tableName,
					  String tableName1, 
					  String col_order,
					  long usr_ent_id, long root_ent_id,
		              boolean include_no_record,
		              boolean tc_enabled) throws SQLException {
		  boolean show_all_info = false;
		  String cos_sql = getCosColumnInfo(con, show_all_info, include_no_record, itm_lst_vec, ats_lst,att_create_start_datetime, att_create_end_datetime, tableName, entIdColName, entIdTableName,usr_ent_id, root_ent_id, tc_enabled);
		  String attempts_sql = getLeftModuleEvaluation();
		  String condition_sql = getCosConditionSql(tableName1, tc_enabled);
		  
		  StringBuffer sql = new StringBuffer(512);
		  sql.append("SELECT item1.itm_id AS t_id,item1.itm_code AS t_code,item1.itm_title AS t_title, att_ats_id, ")
			 .append(" SUM(cov_score) AS total_score, SUM(cov_total_time) AS total_time, ")
			 .append(" COUNT(*) AS total_enroll, ")
			 .append(" SUM(attempt.with_attempts) AS attempts_user, ")
		     .append(" SUM(attempt.all_attempts) AS total_attempt ")
		     .append(" FROM aeItem item1 ");
		  sql.append(cos_sql).append(attempts_sql).append(condition_sql);
		  sql.append(" GROUP BY item1.itm_id ,item1.itm_code, item1.itm_title, att_ats_id ");
		  sql.append(col_order);
		  return sql.toString();
	  }
	  //sql5。全部学习记录，
	  public static String learnerReportSearchCos(
					  Connection con,
					  String entIdColName,
					  String entIdTableName,
					  Vector itm_lst_vec,
					  Vector ats_lst,
					  Timestamp att_create_start_datetime,
					  Timestamp att_create_end_datetime,
					  String col_order,
					  String tableName,
					  String tableName1,
					  long usr_ent_id, long root_ent_id,
                      boolean include_no_record,
                      boolean tc_enabled)throws SQLException {
						
					  boolean show_all_info = true;
					  String cos_sql = getCosColumnInfo(con, show_all_info, include_no_record, itm_lst_vec, ats_lst,att_create_start_datetime, att_create_end_datetime, tableName, entIdColName, entIdTableName,usr_ent_id, root_ent_id, tc_enabled);
					  String attempts_sql = getLeftModuleEvaluation();
					  String condition_sql = getCosConditionSql(tableName1, tc_enabled);
					  StringBuffer sql = new StringBuffer(512);		  
					  
					  sql.append("SELECT usr_ent_id, usr_ste_usr_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, usr_email, usr_tel_1,")
						 .append(" app_id, app_create_timestamp, app_status, app_process_status, app_tkh_id, ")
						 .append(" item1.itm_id t_id, item1.itm_title t_title, item1.itm_unit t_unit, item1.itm_code t_code, item1.itm_type t_type, item1.itm_eff_start_datetime t_eff_start_datetime, item1.itm_eff_end_datetime t_eff_end_datetime, item1.itm_apply_method t_apply_method, ")
						 .append(" item1.itm_id, item1.itm_title, item1.itm_eff_start_datetime, item1.itm_eff_end_datetime, ")
						 .append(" att_timestamp, att_create_timestamp, att_ats_id, att_remark, att_rate, ")
						 .append(" cov_score, cov_commence_datetime, cov_last_acc_datetime, cov_total_time, ")
						 .append(" attempt.all_attempts as total, usg.ern_ancestor_ent_id group_id, ugr.ern_ancestor_ent_id  grade_id ")
					     .append(" FROM aeItem item1 ");
					  sql.append(cos_sql).append(attempts_sql).append(condition_sql);
					  sql.append(col_order);
					  return sql.toString();
				  } 
	  
	public static String lrnRptSearchCos(Connection con, String entIdColName, String entIdTableName, Vector ats_lst, Timestamp att_create_start_datetime,
			Timestamp att_create_end_datetime, String tableName, String tableName1, boolean include_no_record,String col_order,boolean is_realTime_view_rpt) {
		  StringBuffer sql = new StringBuffer(512);	
		  sql.append(" select usr_ent_id, usr_ste_usr_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, usr_email, usr_tel_1, ");
		  sql.append(" lar_app_id app_id, app_create_timestamp, app_status, app_process_status, app_tkh_id, ");
		  sql.append(" item1.itm_id t_id, item1.itm_title t_title, item1.itm_unit t_unit, item1.itm_code t_code, item1.itm_type t_type, item1.itm_eff_start_datetime t_eff_start_datetime, item1.itm_eff_end_datetime t_eff_end_datetime, item1.itm_apply_method t_apply_method,  item1.itm_id, item1.itm_title, item1.itm_eff_start_datetime, item1.itm_eff_end_datetime,  ");
		  sql.append(" att_timestamp, att_create_timestamp, lar_att_ats_id att_ats_id, att_remark, att_rate, lar_cov_score cov_score, cov_commence_datetime, cov_last_acc_datetime, lar_cov_total_time cov_total_time,  ");
		  sql.append(" lar_total_attempt as total,lar_attempts_user attempts_user  ");
		  sql.append(" FROM aeItem item1 ");
		  if(include_no_record){
			  sql.append(" left join ");
			}else {
			  sql.append(" inner join ");
			}
		  sql.append(" (select lar_c_itm_id,lar_p_itm_id,lar_usr_ent_id,lar_app_id,lar_att_ats_id,lar_cov_score, lar_cov_total_time,lar_attempts_user,lar_total_attempt ");
		  sql.append(" ,usr_ent_id, usr_ste_usr_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, usr_email, usr_tel_1");
		  sql.append(" ,lar_app_create_timestamp app_create_timestamp, lar_app_status app_status, lar_app_process_status app_process_status, lar_tkh_id app_tkh_id,lar_att_timestamp att_timestamp,lar_att_create_timestamp att_create_timestamp, lar_att_remark att_remark, lar_att_rate att_rate,lar_cov_commence_datetime cov_commence_datetime, lar_cov_last_acc_datetime cov_last_acc_datetime");
		  sql.append(" from ");
		  if(is_realTime_view_rpt){
				 //实时数据
		  sql.append(" view_lrn_activity_group ");
		  }else {
			   ////线程定时保存的数据
			 sql.append(" lrnActivityReport ");
		  }
		  sql.append(" inner join regUser on (usr_ent_id = lar_usr_ent_id)");
		  sql.append(" where usr_status = 'OK' ").append(OuterJoinSqlStatements.getaeAttendanceAtsId(ats_lst,"lar_att_ats_id"))
		  .append(OuterJoinSqlStatements.getTimeInfo(att_create_start_datetime,att_create_end_datetime,"lar_att_create_timestamp"));
		  if(entIdTableName != null ){
			  sql.append(" and lar_usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")");
			}
		  if(tableName != null ){
			  sql.append( " and lar_c_itm_id in (select * from "+tableName+")");
			}
		  sql.append(" )tmp on (tmp.lar_p_itm_id = item1.itm_id)");
		  sql.append(" where item1.itm_owner_ent_id = 1 ");
		  if(tableName1 != null ){
			  sql.append( " and item1.itm_id in (select * from "+tableName1+")");
			}
		  sql.append(col_order);
		  return sql.toString();
    	  
      }
	  
	  public static String lrnRptSearchCosStatistic(String entIdColName,String entIdTableName, Vector ats_lst,Timestamp att_create_start_datetime,Timestamp att_create_end_datetime,
			   boolean include_no_record,String itmtableName, String itmtableName1,String col_order,boolean is_realTime_view_rpt){
		  
		  String sql = "select lar_usr_ent_id app_ent_id,itm_id t_id,itm_title t_title,itm_code t_code,lar_att_ats_id att_ats_id,sum(lar_cov_score) total_score,sum(lar_cov_total_time) total_time,count(DISTINCT lar_app_id) total_enroll,sum(lar_attempts_user) attempts_user, sum(lar_total_attempt) total_attempt";
			sql += " from aeItem";
			if(include_no_record){
				sql += " left join ";
			}else {
				sql += " inner join ";
			}
			sql += " (";
			sql += " 	 select lar_c_itm_id,lar_p_itm_id,lar_usr_ent_id,lar_app_id,lar_att_ats_id,lar_cov_score, lar_cov_total_time,lar_attempts_user,lar_total_attempt from ";
			 if(is_realTime_view_rpt){
				 //实时数据
				 sql+= " view_lrn_activity_group ";
			  }else {
				   ////线程定时保存的数据
				 sql += " lrnActivityReport ";
			  }
			sql += " 	 where 1 = 1 "+ OuterJoinSqlStatements.getaeAttendanceAtsId(ats_lst,"lar_att_ats_id") ;
			sql +=       OuterJoinSqlStatements.getTimeInfo(att_create_start_datetime,att_create_end_datetime,"lar_att_create_timestamp");
			if(entIdTableName != null ){
				sql += " and lar_usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")";
			}
			if(itmtableName != null ){
				sql += " and lar_c_itm_id in (select * from "+itmtableName+")";
			}
			sql += " ) tmp on (tmp.lar_p_itm_id = itm_id)";
			if(itmtableName1 != null ){
				sql += " where itm_id in (select * from "+itmtableName1+")";
			}
			
			sql += " GROUP BY itm_id,itm_title,itm_code,lar_att_ats_id,lar_usr_ent_id";
			sql += col_order;
		  
			return sql;
	  }
	  
	  public static String getTotalStatisticCos(Connection con, String entIdColName,String entIdTableName, Vector ats_lst, Timestamp att_create_start_datetime,
			  Timestamp att_create_end_datetime, String tableName, String tableName1, boolean include_no_record,boolean is_realTime_view_rpt) throws SQLException{
		  StringBuffer sql = new StringBuffer(512);		  
		  
		  sql.append(" select COUNT(DISTINCT lar_usr_ent_id) AS user_total,COUNT(DISTINCT item1.itm_id) AS item_total,COUNT(DISTINCT lar_app_id) AS total_enroll, ");
		  sql.append(" SUM(lar_cov_score) AS total_score, SUM(lar_cov_total_time) AS total_time,SUM(lar_attempts_user) AS attempts_user,SUM(lar_total_attempt) AS total_attempt");
		  sql.append("  from aeItem item1");
		  if (include_no_record) {
              sql.append(" left join (  ");
          }else {
              sql.append(" inner join (  ");
          }
	   sql.append("select lar_usr_ent_id,lar_p_itm_id,lar_app_id,lar_cov_score,lar_cov_total_time,lar_attempts_user,lar_total_attempt")
		  .append("	from  ");
	   if(is_realTime_view_rpt){
		 //实时数据
		   sql.append(" view_lrn_activity_group ");
	   }else {
		   ////线程定时保存的数据
		   sql.append(" lrnActivityReport ");
	   }
	   sql.append(" where 1 = 1 ").append(OuterJoinSqlStatements.getaeAttendanceAtsId(ats_lst,"lar_att_ats_id")).append(OuterJoinSqlStatements.getTimeInfo(att_create_start_datetime,att_create_end_datetime,"lar_att_create_timestamp"));
		  if(entIdTableName != null ){
				sql.append(" and lar_usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")");
			}
		  if(tableName != null ){
				sql.append(" and lar_c_itm_id in (select * from "+tableName+")");
		  }
		  if(tableName1 != null ){
				sql.append(" and lar_p_itm_id in (select * from "+tableName1+")");
		  }
		  sql.append(") app on (app.lar_p_itm_id = item1.itm_id)");
		  if(tableName1 != null ){
				sql.append(" where item1.itm_id in (select * from "+tableName1+")");
			}
		  return sql.toString();
	  }
	  
	  public static String lrnRptSearchLrnStatistic(String entIdColName,String entIdTableName, Vector ats_lst,Timestamp att_create_start_datetime,Timestamp att_create_end_datetime,
			   String itmtableName, String itmtableName1,String col_order,boolean include_no_record,boolean is_realTime_view_rpt){
		  
		  String sql = "select usr_ent_id,usr_ste_usr_id,usr_display_bil,lar_p_itm_id p_itm_id,lar_att_ats_id att_ats_id,sum(lar_cov_score) total_score,sum(lar_cov_total_time) total_time,count(DISTINCT lar_app_id) total_enroll,sum(lar_attempts_user) attempts_user, sum(lar_total_attempt) total_attempt";
			sql += " from regUser";
			if(include_no_record){
				sql += " left join ";
			}else {
				sql += " inner join ";
			}
			sql += " (";
			sql += " 	 select lar_c_itm_id,lar_p_itm_id,lar_usr_ent_id,lar_app_id,lar_att_ats_id,lar_cov_score, lar_cov_total_time,lar_attempts_user,lar_total_attempt from ";
			if(is_realTime_view_rpt){
				sql += " view_lrn_activity_group ";
			}else {
				sql += " lrnActivityReport ";
			}
			sql += " 	 where 1 = 1 "+ OuterJoinSqlStatements.getaeAttendanceAtsId(ats_lst,"lar_att_ats_id") ;
			sql +=       OuterJoinSqlStatements.getTimeInfo(att_create_start_datetime,att_create_end_datetime,"lar_att_create_timestamp");
			if(entIdTableName != null ){
				sql += " and lar_usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")";
			}
			if(itmtableName != null ){
				sql += " and lar_c_itm_id in (select * from "+itmtableName+")";
			}
			if(itmtableName1 != null ){
				sql += " and lar_p_itm_id in (select * from "+itmtableName1+")";
			}
			sql += " ) tmp on (tmp.lar_usr_ent_id = usr_ent_id)";
			sql += " where usr_status = 'OK'";
			if(entIdTableName != null ){
				sql += " and usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")";
			}
			sql += " GROUP BY usr_ent_id,lar_att_ats_id,lar_p_itm_id,usr_ste_usr_id,usr_display_bil";
			sql += col_order;
		  
			return sql;
	  }
	  
	  public static String lrnRptSearchLrn(String entIdColName, String entIdTableName, Vector ats_lst, Timestamp att_create_start_datetime,
				Timestamp att_create_end_datetime, String tableName, String tableName1, boolean include_no_record,String col_order,boolean is_realTime_view_rpt) {
			  StringBuffer sql = new StringBuffer(512);	
			  
			  sql.append(" select usr_ent_id, usr_ste_usr_id, usr_display_bil, ");
			  sql.append(" lar_c_itm_id t_id, t_title, t_code, t_type,t_apply_method,itm_blend_ind, itm_exam_ind, itm_ref_ind,tcr_title, ");
			  sql.append(" lar_app_id app_id, app_create_timestamp, app_status, app_process_status, app_tkh_id, ");
			  sql.append(" att_timestamp, att_create_timestamp, lar_att_ats_id att_ats_id, att_remark, att_rate, lar_cov_score cov_score, cov_commence_datetime, cov_last_acc_datetime, lar_cov_total_time cov_total_time,  ");
			  sql.append(" lar_total_attempt as total,lar_total_attempt attempts_user  ");
			  sql.append(" FROM RegUser  ");
			  if (include_no_record) {
	              sql.append(" left join (  ");
	          }else {
	              sql.append(" inner join (  ");
	          }
			  sql.append(" select lar_c_itm_id, lar_p_itm_id,lar_usr_ent_id,lar_app_id,lar_att_ats_id,lar_cov_score, lar_cov_total_time,lar_attempts_user,lar_total_attempt ");
			  sql.append(" ,lar_app_create_timestamp app_create_timestamp, lar_app_status app_status, lar_app_process_status app_process_status, lar_tkh_id app_tkh_id,lar_att_timestamp att_timestamp,lar_att_create_timestamp att_create_timestamp, lar_att_remark att_remark, lar_att_rate att_rate,lar_cov_commence_datetime cov_commence_datetime,lar_cov_last_acc_datetime cov_last_acc_datetime");
			  sql.append(" ,itm_id t_id, itm_title t_title, itm_code t_code, itm_type t_type,itm_apply_method t_apply_method,  itm_blend_ind, itm_exam_ind ,itm_ref_ind,tcr_title");
			  sql.append(" from ");
			  if(is_realTime_view_rpt){
				  sql.append(" view_lrn_activity_group ");
				}else {
					 sql.append(" lrnActivityReport ");
				}
			  sql.append(" inner join aeItem on (lar_p_itm_id = itm_id)");
			  sql.append(" inner join tcTrainingCenter on (itm_tcr_id = tcr_id) ");
			  sql.append(" where 1 = 1 ").append(OuterJoinSqlStatements.getaeAttendanceAtsId(ats_lst,"lar_att_ats_id"))
			  .append(OuterJoinSqlStatements.getTimeInfo(att_create_start_datetime,att_create_end_datetime,"lar_att_create_timestamp"));
			  if(entIdTableName != null ){
				  sql.append(" and lar_usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")");
				}
			  if(tableName != null ){
				  sql.append( " and lar_c_itm_id in (select * from "+tableName+")");
				}
			  sql.append(" )tmp on (tmp.lar_usr_ent_id = usr_ent_id)");
			  sql.append(" where usr_status = 'OK' ");
			  if(entIdTableName != null ){
				  sql.append(" and usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")");
				}
			  sql.append(col_order);
			  return sql.toString();
	    	  
	      }
	 
	  public static String getTotalStatisticLrn(Connection con,
			  String entIdColName,String entIdTableName,Vector ats_lst,Timestamp att_create_start_datetime, Timestamp att_create_end_datetime,
			  String tableName,String tableName1,boolean include_no_record,boolean is_realTime_view_rpt) throws SQLException{
		  StringBuffer sql = new StringBuffer(512);
		  
		  sql.append(" select COUNT(DISTINCT usr_ent_id) AS user_total, COUNT(distinct lar_p_itm_id)  AS item_total,COUNT(DISTINCT lar_app_id)  AS total_enroll,");
		  sql.append(" SUM(lar_cov_score) AS total_scroe,SUM(lar_cov_total_time) AS total_time, SUM(lar_attempts_user) AS attempts_user,SUM(lar_total_attempt) AS total_attempt ");
		  sql.append(" from reguser");
		  if (include_no_record) {
              sql.append(" left join (  ");
          }else {
              sql.append(" inner join (  ");
          }
		  sql.append("select lar_usr_ent_id,lar_p_itm_id,lar_app_id,lar_cov_score,lar_cov_total_time,lar_attempts_user,lar_total_attempt")
		  .append("	from ");
		  if(is_realTime_view_rpt){
			 //实时数据
			   sql.append(" view_lrn_activity_group ");
		   }else {
			   ////线程定时保存的数据
			   sql.append(" lrnActivityReport ");
		   }
		  sql.append(" where 1 = 1").append(OuterJoinSqlStatements.getaeAttendanceAtsId(ats_lst,"lar_att_ats_id"))
		  .append(OuterJoinSqlStatements.getTimeInfo(att_create_start_datetime,att_create_end_datetime,"lar_att_create_timestamp"));
		  if(entIdTableName != null ){
				sql.append(" and lar_usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")");
			}
		  if(tableName != null ){
				sql.append(" and lar_c_itm_id in (select * from "+tableName+")");
		  }
		  if(tableName1 != null ){
				sql.append(" and lar_p_itm_id in (select * from "+tableName1+")");
		  }
		  sql.append(") app on (app.lar_usr_ent_id = usr_ent_id)");
		  sql.append(" where usr_status = 'OK'");
		  if(entIdTableName != null ){
				sql.append(" and usr_ent_id in (select "+entIdColName+" from "+entIdTableName+")");
			}
	      return sql.toString();
	  }
	
	  //已完成，已报名
	  public static String getTatoalRecordGroupByAttID(Connection con,Vector ats_lst, String entIdColName,String entIdTableName,Vector itm_lst_vec, 
			  Timestamp att_create_start_datetime,Timestamp att_create_end_datetime,String tableName) throws SQLException {
		  StringBuffer sql = new StringBuffer();
		  sql.append(" SELECT att_ats_id, COUNT(att_app_id) AS record FROM  aeAttendance ")
		     .append(" INNER JOIN aeApplication ON ( att_app_id = app_id ) ")
		      .append(" inner join RegUser on (usr_ent_id = app_ent_id)")
		     .append(" INNER JOIN aeItem ON (app_itm_id = itm_id) ")
		     .append(" WHERE 1=1 ").append(getAttenInfo(ats_lst))
		     .append(getTimeInfo(att_create_start_datetime,att_create_end_datetime,"att_create_timestamp"))
		  	 .append(getUserInfo(entIdColName, entIdTableName, "app_ent_id"))
		     .append(getItemLst(itm_lst_vec,tableName," AND app_itm_id IN "))
		     .append(" AND itm_owner_ent_id  = ? ");
		  sql.append(" GROUP BY att_ats_id ");
		  return sql.toString();
	  }
	  public static String getTatoalRecordGroupByAttID2(Connection con,Vector ats_lst, String entIdColName,String entIdTableName,Vector itm_lst_vec, 
			  Timestamp att_create_start_datetime,Timestamp att_create_end_datetime,String tableName) throws SQLException {
		  StringBuffer sql = new StringBuffer();
		  sql.append(" SELECT att_ats_id, COUNT(att_app_id) AS record FROM  aeAttendance ")
		     .append(" INNER JOIN lrnActivityReport ON ( att_app_id = lar_app_id ) ")
		     .append(" inner join RegUser on (usr_ent_id = lar_usr_ent_id)")
		     .append(" INNER JOIN aeItem ON (lar_c_itm_id = itm_id) ")
		     .append(" WHERE 1=1 ").append(getAttenInfo(ats_lst))
		     .append(getTimeInfo(att_create_start_datetime,att_create_end_datetime,"lar_att_create_timestamp"))
		  	 .append(getUserInfo(entIdColName, entIdTableName, "lar_usr_ent_id"))
		     .append(getItemLst(itm_lst_vec,tableName," AND lar_c_itm_id IN "))
		     .append(" AND itm_owner_ent_id  = ? ");
		  sql.append(" GROUP BY att_ats_id ");
		  return sql.toString();
	  }
	  
	  //get cos condition sql
	  private static String getCosConditionSql(String tableName1, boolean tc_enabled){
		  StringBuffer sql = new StringBuffer();
		  sql.append(" WHERE 1=1 ");
		  if (tableName1 != null) {
			  sql.append(" AND item1.itm_id IN ( SELECT * FROM ").append(tableName1).append(" )");		 
		  }
		  sql.append(" AND item1.itm_owner_ent_id = ? ");
		  return sql.toString();
	  }
	  private static String getCosColumnInfo(Connection con,
				boolean show_all_info,
				boolean include_no_record, 
				Vector itm_lst_vec, Vector ats_lst, 
				Timestamp att_create_start_datetime, Timestamp att_create_end_datetime,
				String tableName, 
				String entIdColName, String entIdTableName,
				long usr_ent_id, long root_ent_id,
				boolean tc_enabled) throws SQLException {
			  StringBuffer sql = new StringBuffer();
	          if (include_no_record) {
	              sql.append(" LEFT JOIN (  ");
	          }else {
	              sql.append(" INNER JOIN (  ");
	          }
	          if(show_all_info) {
				  sql.append("SELECT ").append(cwSQL.replaceNull("parentItem.itm_id", "appItem.itm_id")).append("app_parent_itm_id,usr_ent_id, usr_ste_usr_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil,usr_email, usr_tel_1,")
					 .append("app_id, app_create_timestamp, app_status, app_process_status, app_tkh_id,att_timestamp, att_create_timestamp, att_ats_id, att_remark, att_rate, ")
					 .append("cov_score, cov_commence_datetime, cov_last_acc_datetime,cov_total_time ");
		             if(tc_enabled) {
		            	 sql.append(" , tcr_id, tcr_title ");
		             }
	          } else {
		           sql.append(" SELECT ").append(cwSQL.replaceNull("parentItem.itm_id", "appItem.itm_id")).append(" AS app_parent_itm_id,")
		 			  .append(" app_tkh_id, att_ats_id, app_ent_id, ")
		 			  .append(" cov_score, cov_total_time ");
	          }
			  sql.append(" FROM aeApplication INNER JOIN aeItem appItem ON (app_itm_id = appItem.itm_id ");
			  sql.append(" AND appItem.itm_owner_ent_id = ? )")
				 .append(" INNER JOIN aeAttendance ON (app_id = att_app_id  ")
			     .append(getAttenInfo(ats_lst))
			     .append(getTimeInfo(att_create_start_datetime,att_create_end_datetime));
			  sql.append(" ) ");
			  sql.append(" INNER JOIN RegUser ON ( app_ent_id = usr_ent_id ) ");
			  sql.append(" INNER JOIN CourseEvaluation ON ( app_tkh_id = cov_tkh_id ) ")
				 .append(" LEFT JOIN aeItemRelation ON ( app_itm_id  = ire_child_itm_id) ")
				 .append(" LEFT JOIN aeItem parentItem ON (ire_parent_itm_id = parentItem.itm_id) ");
			  if(tc_enabled) {
				  sql.append(" INNER JOIN tcTrainingCenter on (").append(cwSQL.replaceNull("parentItem.itm_tcr_id", "appItem.itm_tcr_id")).append("  = tcr_id )");
			  }
			  sql.append(" WHERE 1=1 ")
			  	 .append(getItemLst(itm_lst_vec,tableName," AND appItem.itm_id in "))
				 .append(getUserInfo(entIdColName, entIdTableName, "app_ent_id"));
			  if(tc_enabled) {
				  sql.append(" AND tcr_ste_ent_id = ?")
				     .append(" AND tcr_status = ? ");
//				  sql.append(ViewLearnerReport.getMyRspLrnAndMyRspCosStmt(con, "app_ent_id", "app_itm_id", usr_ent_id, root_ent_id));
			  }
			  sql.append(" ) ").append("app ON ( item1.itm_id = app_parent_itm_id )");

			  if(show_all_info) {
				  sql.append("LEFT JOIN EntityRelation usg on (usg.ern_type='USR_PARENT_USG' AND usg.ern_child_ent_id = usr_ent_id AND usg.ern_parent_ind = ?) ");
				  sql.append("LEFT JOIN EntityRelation ugr on (ugr.ern_type='USR_CURRENT_UGR' AND ugr.ern_child_ent_id = usr_ent_id AND ugr.ern_parent_ind = ?) ");
			  }
			  return sql.toString();
	  }
	  //get lrn column information
	  private static String getLrnColumnInfo(
			  Connection con,
			  boolean show_all_info,
			  boolean include_no_record,
			  Vector itm_lst_vec,
			  Vector ats_lst,
			  Timestamp att_create_start_datetime,
			  Timestamp att_create_end_datetime,
			  String tableName, String tableName1,
			  long usr_ent_id, long root_ent_id,
			  boolean tc_enabled) throws SQLException {
		  StringBuffer sql = new StringBuffer();
          if (include_no_record) {
              sql.append("LEFT JOIN ");
          }else {
              sql.append("INNER JOIN ");
          }
          if(show_all_info) {
              sql.append("( select  ")
				 .append(cwSQL.replaceNull("parentItem.itm_id", "appItem.itm_id")).append(" itm_id,")
				 .append(cwSQL.replaceNull("parentItem.itm_owner_ent_id", "appItem.itm_owner_ent_id")).append(" itm_owner_ent_id,")
				 .append(cwSQL.replaceNull("parentItem.itm_title", "appItem.itm_title")).append(" itm_title,")
				 .append(cwSQL.replaceNull("parentItem.itm_unit", "appItem.itm_unit")).append(" itm_unit,")
				 .append(cwSQL.replaceNull("parentItem.itm_code", "appItem.itm_code")).append(" itm_code,")
				 .append(cwSQL.replaceNull("parentItem.itm_type", "appItem.itm_type")).append(" itm_type,")
				 .append(cwSQL.replaceNull("parentItem.itm_eff_start_datetime", "appItem.itm_eff_start_datetime")).append(" itm_eff_start_datetime,")
				 .append(cwSQL.replaceNull("parentItem.itm_eff_end_datetime", "appItem.itm_eff_end_datetime")).append(" itm_eff_end_datetime,")
				 .append(cwSQL.replaceNull("parentItem.itm_apply_method", "appItem.itm_apply_method")).append(" itm_apply_method,")
				 .append(" parentItem.itm_blend_ind, parentItem.itm_exam_ind, parentItem.itm_ref_ind,")
				 .append("app_id, app_ent_id, app_create_timestamp, app_status, app_process_status, app_tkh_id,att_timestamp, att_create_timestamp, att_ats_id, att_remark, att_rate, ")
				 .append("cov_score, cov_commence_datetime, cov_last_acc_datetime,cov_total_time "); 
             if(tc_enabled) {
            	 sql.append(" ,tcr_id, tcr_title ");
             }
          } else {
              sql.append("( SELECT  ")
 		     .append(cwSQL.replaceNull("parentItem.itm_id", "appItem.itm_id")).append(" AS itm_id,")
 		     .append(" parentItem.itm_blend_ind, parentItem.itm_exam_ind, parentItem.itm_ref_ind,")
 		     .append("app_id, app_ent_id, app_tkh_id, att_ats_id, ")
 		     .append("cov_score,cov_total_time ");        	  
          }
		  sql.append("FROM aeApplication ")
		     .append(" INNER JOIN aeAttendance ON (app_id = att_app_id AND app_itm_id = att_itm_id ")
	         .append(getAttenInfo(ats_lst))
		     .append(getTimeInfo(att_create_start_datetime,att_create_end_datetime));
	      sql.append(" ) ");
		  sql.append("INNER JOIN CourseEvaluation ON ( app_tkh_id = cov_tkh_id ) ");
		  sql.append("INNER JOIN aeItem appItem ON (app_itm_id = appItem.itm_id ")
		     .append(" AND appItem.itm_owner_ent_id = ? )")
		     .append("LEFT JOIN aeItemRelation ON (app_itm_id = ire_child_itm_id) ")
		     .append("LEFT JOIN aeItem parentItem ON (ire_parent_itm_id = parentItem.itm_id) ");
		  if(tc_enabled) {
			  sql.append(" INNER JOIN tcTrainingCenter on (").append(cwSQL.replaceNull("parentItem.itm_tcr_id", "appItem.itm_tcr_id")).append("  = tcr_id )");
		  }
		  sql.append(" where 1 = 1 ")
  	         .append(getItemLst(itm_lst_vec,tableName," and appItem.itm_id in "));

	      if (tableName1 != null) {
	    	  sql.append(" and ").append(cwSQL.replaceNull("parentItem.itm_id", "appItem.itm_id")).append(" in ( select * from ").append(tableName1).append(" ) ");		 
			  sql.append(" and ").append(cwSQL.replaceNull("parentItem.itm_owner_ent_id", "appItem.itm_owner_ent_id")).append(" = ? ");
	      }
	      if(tc_enabled) {
	     	   sql.append(" and tcr_ste_ent_id =?  ")
	     	   	  .append(" and tcr_status = ? ");
//	     	   sql.append(ViewLearnerReport.getMyRspLrnAndMyRspCosStmt(con, "app_ent_id", "app_itm_id", usr_ent_id, root_ent_id));
	      }
	      sql.append(" ) ").append(" app on ( usr_ent_id = app_ent_id ) ");
	      return sql.toString();
	  }

	  //get total user attempts and total attempts
	  private static String getLeftModuleEvaluation(){
		  StringBuffer sql = new StringBuffer();
		  sql.append(" LEFT JOIN ( SELECT DISTINCT mov_tkh_id, 1 AS  with_attempts, SUM(mov_total_attempt) AS all_attempts FROM ModuleEvaluation WHERE mov_total_attempt IS NOT NULL AND mov_total_attempt>0 ")
		   	 .append(" Group By mov_tkh_id ")
		     .append(" ) attempt ON ( app.app_tkh_id= attempt.mov_tkh_id ) ");
		   	 
		  return sql.toString();
	  }

	  public static String getUserInfo(String entIdColName, String entIdTableName, String column) {
		  StringBuffer sql = new StringBuffer();
		  if(entIdColName != null && entIdTableName != null) {
			  sql.append(" AND ").append(column).append(" IN (SELECT ").append(entIdColName).append(" FROM ").append(entIdTableName).append(")");
		  } else {
			  sql.append("");
		  }
		  sql.append(" and usr_status='OK'");
		  return sql.toString();
	  }
	  public static String getTimeInfo(Timestamp st,Timestamp et){
		  StringBuffer sql = new StringBuffer(64);
		  if (st != null) {
			  sql.append(" AND att_create_timestamp >= ? ");
		  }
		  if (et != null) {
			  sql.append(" AND att_create_timestamp <= ? ");
		  }
		  return sql.toString();		
	  }
	  public static String getTimeInfo(Timestamp st,Timestamp et,String field){
		  StringBuffer sql = new StringBuffer(64);
		  if (st != null) {
			  sql.append(" AND "+field+" >= ? ");
		  }
		  if (et != null) {
			  sql.append(" AND "+field+" <= ? ");
		  }
		  return sql.toString();		
	  }
				
	  public static String getAttenInfo(Vector ats_lst){
		  StringBuffer sql = new StringBuffer(64);
		  if (ats_lst == null || ats_lst.size() == 0) {
				  sql.append(" AND att_ats_id <> 2 ");
		  } else {
				  sql.append(" AND att_ats_id IN ");
				  for (int i = 0; i < ats_lst.size(); i++) {
						  if (i == 0) {
							  sql.append("(").append(ats_lst.elementAt(i));
						  } else {
							if(ats_lst.elementAt(i)!=null) {
							  sql.append(",").append(ats_lst.elementAt(i));
							}
						  }
				  }
		   sql.append(") ");
		 }
		  return sql.toString();			
	  }
	
	  public static String getaeAttendanceAtsId(Vector ats_lst,String field){
		  StringBuffer sql = new StringBuffer(64);
		  if(ats_lst != null && ats_lst.size() > 0){
			  sql.append(" AND "+field+" IN ");
			  for (int i = 0; i < ats_lst.size(); i++) {
					  if (i == 0) {
						  sql.append("(").append(ats_lst.elementAt(i));
					  } else {
						if(ats_lst.elementAt(i)!=null) {
						  sql.append(",").append(ats_lst.elementAt(i));
						}
					  }
			  }
			  sql.append(") ");
		  }
		  return sql.toString();			
	  }
	  
	  public static void printData(Connection con,String tableName){
		  try{
			  PreparedStatement s = con.prepareStatement("select * from "+tableName);
			  ResultSet rs = s.executeQuery();
			  CommonLog.debug("=============="+tableName+"====================");
			  while(rs.next()){
				  CommonLog.debug("+++++++++++++++++++"+rs.getString(1));
			  }
			  s.close();
			  CommonLog.debug("===============================================");
		  }catch(Exception e){
			  CommonLog.error(e.getMessage(),e);
			 // e.printStackTrace();
		  }
	  }

	  private static String getItemLst(Vector lst,String tableName,String s){
		  StringBuffer sql = new StringBuffer(64);
		  if(lst != null)
			 sql.append(s).append(" ( select * from ").append(tableName).append(" )");
		  else
			 sql.append(s).append("(SELECT itm_id FROM aeItem WHERE ( itm_run_ind =1 OR (itm_run_ind =0 AND itm_create_run_ind =0) ))");
		  return sql.toString();
	  }
	  
	//Get Course Left Join Run SQL
	public static String getCosLeftJoinRun() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf.append(" From aeItem parent, aeItemRelation, aeItem run, tcTrainingCenter ");
			SQLBuf.append(" Where parent.itm_id = ire_parent_itm_id(+) ");
			SQLBuf.append(" And ire_child_itm_id = run.itm_id(+) ");
			SQLBuf.append(" And ");
		} else {
			SQLBuf.append(
				" From tcTrainingCenter, aeItem parent Left Join aeItemRelation on (parent.itm_id = ire_parent_itm_id) ");
			SQLBuf.append(
				"                    Left Join aeItem run on (run.itm_id = ire_child_itm_id) ");
			SQLBuf.append(" Where ");
		}
		return SQLBuf.toString();
	}

	public static String courseReportSearch(
		String tableName,
		String colName) {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer result = new StringBuffer();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			result
				.append("select itm_id, itm_title, itm_xml, itm_code, itm_version_code,")
				.append(" itm_type, itm_capacity, itm_min_capacity, itm_unit,")
				.append(" itm_eff_start_datetime, itm_eff_end_datetime,")
				.append(" itm_status, itm_life_status, itm_apply_method,")
				.append(" itm_blend_ind, itm_exam_ind, itm_ref_ind,")
				.append(" itm_person_in_charge, itm_imd_id, itm_run_ind, itm_create_run_ind,")
				.append(" cos_res_id from aeItem, Course ");

			if (tableName != null) {
				result
					.append(" , ")
					.append(tableName)
					.append(" where itm_id = ")
					.append(colName)
					.append(" and ");
			} else {
				result.append(" where ");
			}

			result.append(" itm_id = cos_itm_id(+) and ");
		} else {
			result
				.append("select itm_id, itm_title, itm_xml, itm_code, itm_version_code,")
				.append(" itm_type, itm_capacity, itm_min_capacity, itm_unit,")
				.append(" itm_eff_start_datetime, itm_eff_end_datetime,")
				.append(" itm_status, itm_life_status, itm_apply_method,")
				.append(" itm_blend_ind, itm_exam_ind, itm_ref_ind,")
				.append(" itm_person_in_charge, itm_imd_id, itm_run_ind, itm_create_run_ind,")
				.append(" cos_res_id from aeItem ");

			if (tableName != null) {
				result
					.append(" inner join ")
					.append(tableName)
					.append(" on (itm_id = ")
					.append(colName)
					.append(") ");
			}

			result.append(   
				" left outer join Course on (itm_id = cos_itm_id) WHERE ");
		}

		return result.toString();
	}

	//Get RegUser Left Join Appn Left Join Item Left Join ItemRelation
	public static String getUsrLeftJoinAppnNRun() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf
				.append(" From RegUser, aeApplication, aeItem, aeItemRelation ")
				.append(" Where usr_ent_id = app_ent_id(+) ")
				.append(" And app_itm_id = itm_id(+) ")
				.append(" And itm_id = ire_child_itm_id(+) ")
				.append(" And ");
		} else {
			SQLBuf
				.append(" From RegUser Left Join aeApplication on (usr_ent_id = app_ent_id) ")
				.append(" Left Join aeItem on (app_itm_id = itm_id ) ")
				.append(" Left Join aeItemRelation on (itm_id = ire_child_itm_id) ")
				.append(" Where ");
		}
		return SQLBuf.toString();
	}

	public static String sql_get_rsv_4_itm() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf.append(
				"select rsv_desc, fac_title from fmReservation, fmFacility where rsv_id = ? and rsv_main_fac_id = fac_id(+)");
		} else {
			SQLBuf.append(
				"select rsv_desc, fac_title from fmReservation left join fmFacility on (rsv_main_fac_id = fac_id) where rsv_id = ?");
		}

		return SQLBuf.toString();
	}

	//Get aeApplication Left Join aeAttendance Left Join aeAttendanceStatus
	public static String getAppnLeftJoinAtt() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf
				.append(" From aeItem, aeApplication a, aeAttendance, aeAttendanceStatus ")
				.append(" Where a.app_id = att_app_id (+) ")
				.append(" And a.app_itm_id = att_itm_id (+) ")
				.append(" And att_ats_id = ats_id (+) ")
				.append(" And ");
		} else {
			SQLBuf
				.append(" From aeItem, aeApplication a ")
				.append(" Left Join aeAttendance on (a.app_id = att_app_id AND a.app_itm_id = att_itm_id ) ")
				.append(" Left Join aeAttendanceStatus on (att_ats_id = ats_id) ")
				.append(" Where ");
		}

		return SQLBuf.toString();
	}

	public static String getItemRequirement() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		SQLBuf
			.append(" SELECT ")
			.append(" itr_requirement_type, itr_requirement_subtype, ")
			.append(" itr_requirement_restriction, ")
			.append(" ird_requirement_due_date as itr_requirement_due_date, ")
			.append(" itr_appn_footnote_ind, ")
			.append(" itr_condition_type, itr_condition_rule, ")
			.append(" itr_positive_iat_id, itr_negative_iat_id, ")
			.append(" itr_proc_execute_timestamp,itr_update_timestamp,itr_update_usr_id ");

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf
				.append(" FROM aeItemRequirement, aeItemReqDueDate ")
				.append(" WHERE itr_itm_id = ird_itr_itm_id (+) ")
				.append(" AND itr_order = ird_itr_order (+) ")
				.append(" AND itr_itm_id = ird_child_itm_id (+) ")
				.append(" AND itr_itm_id = ? ")
				.append(" AND itr_order = ? ");
		} else {
			SQLBuf
				.append(" FROM aeItemRequirement Left Join aeItemReqDueDate on ")
				.append(" (itr_itm_id = ird_itr_itm_id and itr_order = ird_itr_order and itr_itm_id = ird_child_itm_id) ")
				.append(" WHERE itr_itm_id = ? ")
				.append(" AND itr_order = ? ");
		}

		return SQLBuf.toString();
	}

	public static String getItemRequirementByItemId() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		SQLBuf
			.append(" SELECT ")
			.append(" itr_order, itr_requirement_type, itr_requirement_subtype, ")
			.append(" itr_requirement_restriction, ")
			.append(" ird_requirement_due_date as itr_requirement_due_date, ")
			.append(" itr_appn_footnote_ind, ")
			.append(" itr_condition_type, itr_condition_rule, ")
			.append(" itr_positive_iat_id, itr_negative_iat_id, ")
			.append(" itr_proc_execute_timestamp ,itr_update_timestamp,itr_update_usr_id");

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf
				.append(" FROM aeItemRequirement, aeItemReqDueDate ")
				.append(" WHERE itr_itm_id = ird_itr_itm_id (+) ")
				.append(" AND itr_order = ird_itr_order (+) ")
				.append(" AND itr_itm_id = ird_child_itm_id (+) ")
				.append(" AND itr_itm_id = ? ")
				.append(" ORDER BY itr_order ");

		} else {
			SQLBuf
				.append(" FROM aeItemRequirement Left Join aeItemReqDueDate on ")
				.append(" (itr_itm_id = ird_itr_itm_id and itr_order = ird_itr_order and itr_itm_id = ird_child_itm_id) ")
				.append(" WHERE itr_itm_id = ? ")
				.append(" ORDER BY itr_order ");
		}

		return SQLBuf.toString();
	}
   
	//----------------
	public static String getRunRequirement() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		SQLBuf
			.append(" SELECT ")
			.append(" itr_requirement_type, itr_requirement_subtype, ")
			.append(" itr_requirement_restriction, ")
			.append(" ird_requirement_due_date as itr_requirement_due_date, ")
			.append(" itr_appn_footnote_ind, ")
			.append(" itr_condition_type, itr_condition_rule, ")
			.append(" itr_positive_iat_id, itr_negative_iat_id, ")
			.append(" itr_proc_execute_timestamp,itr_update_timestamp,itr_update_usr_id ");

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf
				.append(" FROM aeItemRelation, aeItemRequirement, aeItemReqDueDate ")
				.append(" WHERE itr_itm_id = ird_itr_itm_id (+) ")
				.append(" AND itr_order = ird_itr_order (+) ")
				.append(" AND ire_child_itm_id = ird_child_itm_id (+) ")
				.append(" AND itr_itm_id = ire_parent_itm_id ")
				.append(" AND ire_child_itm_id = ? ")
				.append(" AND itr_itm_id = ? ")
				.append(" AND itr_order = ? ");
		} else {
			SQLBuf
				.append(" FROM aeItemRelation ")
				.append(" Inner Join aeItemRequirement on ")
				.append("     (itr_itm_id = ire_parent_itm_id and ire_child_itm_id = ?) ")
				.append(" Left Join aeItemReqDueDate on ")
				.append("     (itr_itm_id = ird_itr_itm_id and itr_order = ird_itr_order and ire_child_itm_id = ird_child_itm_id) ")
				.append(" WHERE itr_itm_id = ? ")
				.append(" AND itr_order = ? ");
		}

		return SQLBuf.toString();
	}

	public static String getRunRequirementByItemId() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		SQLBuf
			.append(" SELECT ")
			.append(" itr_order, itr_requirement_type, itr_requirement_subtype, ")
			.append(" itr_requirement_restriction, ")
			.append(" ird_requirement_due_date as itr_requirement_due_date, ")
			.append(" itr_appn_footnote_ind, ")
			.append(" itr_condition_type, itr_condition_rule, ")
			.append(" itr_positive_iat_id, itr_negative_iat_id, ")
			.append(" itr_proc_execute_timestamp ,itr_update_timestamp,itr_update_usr_id");

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf
				.append(" FROM aeItemRequirement,aeItemRelation, aeItemReqDueDate ")
				.append(" WHERE itr_itm_id = ird_itr_itm_id (+) ")
				.append(" AND itr_order = ird_itr_order (+) ")
				.append(" AND itr_itm_id = ire_parent_itm_id ")
				.append(" AND ire_child_itm_id = ? ")
				.append(" AND itr_itm_id = ? ")
				.append(" ORDER BY itr_order ");

		} else {
			SQLBuf
				.append(" FROM aeItemRelation ")
				.append(" Inner Join aeItemRequirement on ")
				.append("     (itr_itm_id = ire_parent_itm_id and ire_child_itm_id = ?) ")
				.append(" Left Join aeItemReqDueDate on ")
				.append("     (itr_itm_id = ird_itr_itm_id and itr_order = ird_itr_order and ire_child_itm_id = ird_child_itm_id) ")
				.append(" WHERE itr_itm_id = ? ")
				.append(" ORDER BY itr_order ");
		}

		return SQLBuf.toString();
	}

	public static String getOverdueItemReq() {
		StringBuffer SQLBuf = new StringBuffer(512);
		String dbproduct = cwSQL.getDbProductName();

		SQLBuf
			.append(" SELECT ird_child_itm_id AS itr_itm_id, itr_order ")
			.append(" ,ird_requirement_due_date AS itr_requirement_due_date ")
			.append(" ,itr_condition_type, itr_condition_rule ")
			.append(" ,itr_positive_iat_id, POS_ACTN.iat_type AS POSTYPE ")
			.append(" ,itr_negative_iat_id, NEG_ACTN.iat_type AS NEGTYPE ");

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQLBuf
				.append(" FROM aeItemReqDueDate, aeItemRequirement ")
				.append(", aeItemActn POS_ACTN, aeItemActn NEG_ACTN ")
				.append(" WHERE itr_positive_iat_id = POS_ACTN.iat_id (+) ")
				.append(" AND POS_ACTN.iat_type (+)= ? ")
				.append(" AND itr_negative_iat_id = NEG_ACTN.iat_id (+)")
				.append(" AND NEG_ACTN.iat_type (+)= ? ")
				.append(" AND ird_requirement_due_date < ? ")
				.append(" AND ird_itr_itm_id = itr_itm_id ")
				.append(" AND ird_itr_order = itr_order ")
				.append(" ORDER BY itr_itm_id ASC, itr_order ASC ");

		} else {

			SQLBuf
				.append(" FROM aeItemReqDueDate, aeItemRequirement ")
				.append(" Left Join aeItemActn POS_ACTN on ")
				.append("     (POS_ACTN.iat_id = itr_positive_iat_id And POS_ACTN.iat_type = ?) ")
				.append(" Left Join aeItemActn NEG_ACTN on ")
				.append("     (NEG_ACTN.iat_id = itr_negative_iat_id And NEG_ACTN.iat_type = ?) ")
				.append(" Where ird_requirement_due_date < ? ")
				.append(" And ird_itr_itm_id = itr_itm_id ")
				.append(" And ird_itr_order = itr_order ")
				.append(" ORDER BY itr_itm_id ASC, itr_order ASC ");

		}

		return SQLBuf.toString();
	}
	public static String getItemRelation() {
		StringBuffer SQL = new StringBuffer();
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQL
				.append(" Select c_itm.itm_id as C_ITM_ID, c_itm.itm_title as C_ITM_TITLE, p_itm.itm_title as P_ITM_TITLE ")
				.append(" From aeItem c_itm, aeItem p_itm, aeItemRelation ")
				.append(" Where c_itm.itm_id = ire_child_itm_id(+) ")
				.append(" And ire_parent_itm_id = p_itm.itm_id(+) ")
				.append(" And c_itm.itm_owner_ent_id = ? and c_itm.itm_code = ? ");

		} else {
			SQL
				.append(" Select c_itm.itm_id as C_ITM_ID, c_itm.itm_title as C_ITM_TITLE, p_itm.itm_title as P_ITM_TITLE ")
				.append(" From aeItem c_itm ")
				.append(" Left Join aeItemRelation ON ( c_itm.itm_id = ire_child_itm_id ) ")
				.append(" Left Join aeItem p_itm ON ( ire_parent_itm_id = p_itm.itm_id ) ")
				.append(" Where c_itm.itm_owner_ent_id = ? and c_itm.itm_code = ? ");
		}
		return SQL.toString();
	}

	public static String getAssignedSites() {
		StringBuffer SQL = new StringBuffer();
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQL
				.append(" select ste_ent_id, ste_name, cac_id ")
				.append(" from acSite, aeCatalogAccess ")
				.append(" where cac_ent_id (+) = ste_ent_id ")
				.append(" and cac_cat_id (+) = ? ");

		} else {
			SQL
				.append(" select ste_ent_id, ste_name, cac_id ")
				.append(" from acSite Left Join  aeCatalogAccess ")
				.append(" on (cac_ent_id = ste_ent_id and cac_cat_id = ?) ");
		}
		return SQL.toString();
	}

	public static final String getGlobalEnrollmentReport(
		String siteList,
		Timestamp start_date,
		Timestamp end_date) {
		StringBuffer SQL = new StringBuffer();
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQL
				.append(" select count(tkh_id) enrollmentCount, ste_ent_id, ste_name ")
				.append(" from acSite , ")
				.append(" (Select * From RegUser, TrackingHistory ")
				.append(" Where usr_ent_id = tkh_usr_ent_id ");
			if (start_date != null) {
				SQL.append(" and tkh_create_timestamp >= ? ");
			}
			if (end_date != null) {
				SQL.append(" and tkh_create_timestamp <= ? ");
			}
			SQL.append(" ) siteEnrollment ").append(
				" where ste_status = ? ").append(
				" and ste_ent_id = usr_ste_ent_id (+) ");
			if (siteList != null) {
				SQL.append(" and ste_ent_id in ").append(siteList);
			}
			SQL.append(" group by ste_ent_id, ste_name ").append(
				" order by ste_name, ste_ent_id ");
		} else {
			SQL
				.append(" select count(tkh_id) enrollmentCount, ste_ent_id, ste_name ")
				.append(" from acSite Left Join ")
				.append(" (RegUser Inner join TrackingHistory on ")
				.append(" (tkh_usr_ent_id = usr_ent_id ");
			if (start_date != null) {
				SQL.append(" and tkh_create_timestamp >= ? ");
			}
			if (end_date != null) {
				SQL.append(" and tkh_create_timestamp <= ? ");
			}
			SQL.append(" )) on (ste_ent_id = usr_ste_ent_id) ").append(
				" where ste_status = ? ");
			if (siteList != null) {
				SQL.append(" and ste_ent_id in ").append(siteList);
			}
			SQL.append(" group by ste_ent_id, ste_name ").append(
				" order by ste_name, ste_ent_id ");
		}
		return SQL.toString();
	}

	public static String getGlobalEnrollmentReportItemEnrollmentCount(
		String ste_list,
		Timestamp start_date,
		Timestamp end_date,
		String[] course_code) {
		StringBuffer SQL = new StringBuffer();
		String dbproduct = cwSQL.getDbProductName();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQL
				.append(" Select itm_owner_ent_id, itm_id, itm_code, itm_title, count(itm_id) enrollmentCount ")
				.append(" From aeItem, Course, TrackingHistory ")
				.append(" Where cos_itm_id = itm_id ")
				.append(" And cos_res_id = tkh_cos_res_id(+) ")
				.append(" And itm_owner_ent_id In ")
				.append(ste_list);
			if (start_date != null) {
				SQL.append(" And tkh_create_timestamp >= ? ");
			}
			if (end_date != null) {
				SQL.append(" And tkh_create_timestamp <= ? ");
			}
			if (course_code != null && course_code.length > 0) {
				SQL.append(" And ( lower(itm_code) like ? ");
				for (int i = 1; i < course_code.length; i++) {
					SQL.append(" Or lower(itm_code) like ? ");
				}
				SQL.append(" ) ");
			}
			SQL.append(
				" Group By itm_id, itm_owner_ent_id, itm_title, itm_code ");

		} else {
			SQL
				.append(" Select itm_owner_ent_id, itm_id, itm_code, itm_title, count(itm_id) enrollmentCount ")
				.append(" From aeItem, Course Left Join TrackingHistory On ( tkh_cos_res_id = cos_res_id ) ")
				.append(" Where cos_itm_id = itm_id ")
				.append(" And itm_owner_ent_id In ")
				.append(ste_list);
			if (start_date != null) {
				SQL.append(" And tkh_create_timestamp >= ? ");
			}
			if (end_date != null) {
				SQL.append(" And tkh_create_timestamp <= ? ");
			}
			if (course_code != null && course_code.length > 0) {
				SQL.append(" And ( lower(itm_code) like ? ");
				for (int i = 1; i < course_code.length; i++) {
					SQL.append(" Or lower(itm_code) like ? ");
				}
				SQL.append(" ) ");
			}
			SQL.append(
				" Group By itm_id, itm_owner_ent_id, itm_title, itm_code ");
		}

		return SQL.toString();
	}
	
	public static String transCosId(Vector itmId){
		StringBuffer sql=new StringBuffer();
		String dbproduct = cwSQL.getDbProductName();
		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sql.append("select NVL(ire_child_itm_id,itm_id) new_itm_id,itm_id p_id from aeItem,aeItemRelation ")
			   .append(" where itm_id = ire_parent_itm_id(+) and itm_id in (");
		}else{
			sql.append("select " + cwSQL.replaceNull("ire_child_itm_id", "itm_id") + " new_itm_id,itm_id p_id from aeItem ")
			   .append(" left join aeItemRelation on (itm_id = ire_parent_itm_id) where itm_id in (");
		}
		int i = 0;
		for(int n = itmId.size();i<n-1;i++){
			sql.append(((Long)itmId.elementAt(i)).longValue()).append(" , ");
		}
		sql.append(((Long)itmId.elementAt(i)).longValue()).append(" )");
		return sql.toString();
	}

	public static String aeQueueManagerGetEnrolledItems() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer SQL = new StringBuffer();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQL.append("SELECT itm_id, itm_code, itm_type, itm_title, itm_eff_start_datetime, itm_eff_end_datetime, app_status, cos_res_id FROM aeApplication, aeItem, Course WHERE app_ent_id = ? AND itm_id = app_itm_id AND itm_status = ? AND cos_itm_id ");
			SQL.append(cwSQL.get_right_join()).append(" app_itm_id ORDER BY itm_id");
		} else {
			SQL.append(" SELECT itm_id, itm_code, itm_type, itm_title, itm_eff_start_datetime, itm_eff_end_datetime, app_status, cos_res_id " );
			SQL.append(" FROM aeItem, Course RIGHT JOIN aeApplication ");
			SQL.append(" ON ( cos_itm_id = app_itm_id ) ");
			SQL.append(" WHERE app_ent_id = ? AND itm_id = app_itm_id AND itm_status = ? ");
			SQL.append(" ORDER BY itm_id");
		}
		return SQL.toString();
	}

	public static String aeQueueManagerGetEnrolledItemsFromPgm() {
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer SQL = new StringBuffer();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			SQL.append("SELECT itm_id, itm_xml, itm_type, itm_eff_start_datetime, itm_eff_end_datetime, app_status, cos_res_id FROM aeApplication, aeItem, Course WHERE app_ent_id = ? AND itm_id = app_itm_id AND itm_status = ? AND cos_itm_id ");
			SQL.append(cwSQL.get_right_join()).append(" app_itm_id ORDER BY itm_id");
		} else {
			SQL.append(" SELECT itm_id, itm_code, itm_type, itm_title, itm_eff_start_datetime, itm_eff_end_datetime, app_status, cos_res_id " );
			SQL.append(" FROM aeItem, Course RIGHT JOIN aeApplication ");
			SQL.append(" ON ( cos_itm_id = app_itm_id ) ");
			SQL.append(" WHERE app_ent_id = ? AND itm_id = app_itm_id AND itm_status = ? ");
			SQL.append(" ORDER BY itm_id");
		}
		return SQL.toString();
	}
	
	public static String sql_get_check_list() {
		StringBuffer sql = new StringBuffer();
		String dbproduct = cwSQL.getDbProductName();
		StringBuffer SQL = new StringBuffer();

		if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
			sql.append(" select app_id, pgm_itm_id, app_ent_id , pgv_status, pgv_unit , pgv_final_ind FROM aeProgram, aeitem , aeProgramEvaluation , aeApplication ");
			sql.append(" where itm_id = pgm_itm_id ");
			sql.append(" and pgv_pgm_itm_id =* pgm_itm_id ");
			sql.append(" and pgv_ent_id =* app_ent_id ");
			sql.append(" and app_itm_id = pgm_itm_id ");
		} else {
			sql.append(" select app_id, pgm_itm_id, app_ent_id , pgv_status, pgv_unit , pgv_final_ind ");
			sql.append(" FROM aeitem , aeProgramEvaluation RIGHT JOIN aeProgram ");
			sql.append(" ON (pgv_pgm_itm_id = pgm_itm_id) ");
			sql.append(" RIGHT JOIN aeApplication ");
			sql.append(" ON (pgv_ent_id = app_ent_id) ");
			sql.append(" where itm_id = pgm_itm_id ");
			sql.append(" and app_itm_id = pgm_itm_id ");
		}
		return sql.toString();
	}

    public static StringBuffer getNodeItemsSql(String[] itm_dummy_type_lst) {
        String dbproduct = cwSQL.getDbProductName();
        StringBuffer sql = new StringBuffer();

        if (dbproduct.indexOf(ProductName_ORACLE) >= 0) {
            sql.append("select itm_id, tnd_type, tnd_id from aeTreeNode, aeItem ")
               .append("where tnd_itm_id = itm_id(+) ")
               .append("and tnd_parent_tnd_id = ? ");
            sql.append(aeItemDummyType.genSqlByItemDummyType(itm_dummy_type_lst,null,true));
        } else {
            sql.append("select itm_id, tnd_type, tnd_id from aeTreeNode ")
               .append("left join aeItem on (tnd_itm_id = itm_id ");
            sql.append(aeItemDummyType.genSqlByItemDummyType(itm_dummy_type_lst,null,true));
            sql.append(")").append("where tnd_parent_tnd_id = ? ");
        }

        return sql;
    }

    public static String getMyRspLrnEnrollCosSQL(Connection con) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" Select distinct(app_itm_id) itm_id From aeApplication,aeitem ")
    	   .append(" Where app_itm_id = itm_id and itm_type != 'AUDIOVIDEO' and app_ent_id in ( ").append(getAllUsrByTaMgtTc(true)).append(" )");
    	return sql.toString();
    }
    public static String getMyRspCosEnrollLrnSQL(Connection con, boolean distinct )throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select ").append(distinct ? "distinct(app_ent_id)" : "app_ent_id").append(" usr_ent_id From aeApplication,aeitem ")
    	   .append(" where app_itm_id = itm_id and itm_type != 'AUDIOVIDEO' and app_itm_id in ( ").append(getAllItemByTaMgtTc()).append(" )");
    	return sql.toString();
    }
    
    public static String getMyRspCosEnrollLrnSQL(String tmp_col_name, boolean distinct,long usr_ent_id,long root_ent_id)throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ").append(distinct ? "distinct(app_ent_id)" : "app_ent_id").append(" ").append(tmp_col_name).append(" From aeApplication,aeitem ")
           .append(" where app_itm_id = itm_id and itm_type != 'AUDIOVIDEO' and app_itm_id in ( ").append(getAllItemByTaMgtTc(usr_ent_id,root_ent_id)).append(" )");
        return sql.toString();
    }
    
    public static String getAllUsrByTaMgtTc(boolean distinct) throws SQLException {
    	StringBuffer sql = new StringBuffer(); 
    	sql.append(" select ")
    	   .append(distinct ? "distinct(ern_child_ent_id)" : "ern_child_ent_id")
    	   .append(" usr_ent_id from tcTrainingCenterOfficer ")
	       .append(" inner join tcTrainingCenterTargetEntity on (tce_tcr_id = tco_tcr_id) ")
	       .append(" inner join EntityRelation on (ern_ancestor_ent_id = tce_ent_id) ")
	       .append(" inner join tcTrainingCenter on ( tcr_id = tco_tcr_id) ")
	       .append(" where tco_usr_ent_id = ? ")
	       .append(" and tcr_ste_ent_id =? ")
	       .append(" and tcr_status = ? ")
	       .append(" and ern_type = ? ");
    	return sql.toString();
    }
    
    public static String getAllUsrByTaMgtTc(String tmp_col_name,boolean distinct, long usr_ent_id, long root_ent_id) throws SQLException {
        StringBuffer sql = new StringBuffer(); 
        sql.append(" select ")
           .append(distinct ? "distinct(ern_child_ent_id)" : "ern_child_ent_id")
           .append(" ").append(tmp_col_name).append(" from tcTrainingCenterOfficer ")
           .append(" inner join tcTrainingCenterTargetEntity on (tce_tcr_id = tco_tcr_id) ")
           .append(" inner join EntityRelation on (ern_ancestor_ent_id = tce_ent_id) ")
           .append(" inner join tcTrainingCenter on ( tcr_id = tco_tcr_id) ")
           .append(" where tco_usr_ent_id = ").append(usr_ent_id)
           .append(" and tcr_ste_ent_id = ").append(root_ent_id)
           .append(" and tcr_status = '").append(DbTrainingCenter.STATUS_OK).append("'")
           .append(" and ern_type =  '").append(dbEntityRelation.ERN_TYPE_USR_PARENT_USG).append("'");
        return sql.toString();
    }
    
    public static String getAllItemByTaMgtTc() {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" select distinct(itm_id) from tcTrainingCenterOfficer ")
    	   .append(" inner join tcTrainingCenter on (tcr_id = tco_tcr_id) ")
    	   .append(" left join tcRelation on (tcr_id = tcn_ancestor) ")
    	   .append(" inner join aeItem on ( itm_tcr_id = tcn_child_tcr_id or itm_tcr_id = tcr_id) ")
    	   .append(" where tco_usr_ent_id =? ")
    	   .append(" and tcr_ste_ent_id = ? ")
    	   .append(" and tcr_status = ? ")
    	   .append(" and itm_owner_ent_id = ? ");
    	return sql.toString();
    }
    
    public static String getAllItemByTaMgtTc(long usr_ent_id, long root_ent_id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select distinct(itm_id) from tcTrainingCenterOfficer ")
           .append(" inner join tcTrainingCenter on (tcr_id = tco_tcr_id) ")
           .append(" left join tcRelation on (tcr_id = tcn_ancestor) ")
           .append(" inner join aeItem on ( itm_tcr_id = tcn_child_tcr_id or itm_tcr_id = tcr_id) ")
           .append(" where tco_usr_ent_id = ").append(usr_ent_id)
           .append(" and tcr_ste_ent_id = ").append(root_ent_id)
           .append(" and tcr_status = '").append(DbTrainingCenter.STATUS_OK).append("'")
           .append(" and itm_owner_ent_id = ").append(root_ent_id);
        return sql.toString();
    }  
    
    public static String getCourseCriteriaByType() {
    	StringBuffer sqlBuffer = new StringBuffer();
    	sqlBuffer.append("select app_id, app_ent_id, app_tkh_id, att_rate, ccr_id, ccr_pass_ind, ccr_pass_score, ccr_attendance_rate, cos_res_id, cov_score")
    		.append(" from aeApplication")
    		.append(" inner join CourseCriteria on (ccr_itm_id = app_itm_id and ccr_type = ?)")
    		.append(" inner join course on (cos_itm_id = app_itm_id)")
    		.append(" inner join CourseEvaluation on (cov_cos_id = cos_res_id and cov_ent_id = app_ent_id and cov_tkh_id = app_tkh_id)")
    		.append(" inner join aeAttendance on (att_app_id = app_id) ");
    	return sqlBuffer.toString();
    }
    
    public static String ExamPaperStatReportSearchTstData(
			  Connection con,
			  String entIdColName,
			  String entIdTableName,
			  List mod_id_lst,
			  Timestamp att_start_datetime,
			  Timestamp att_end_datetime,
			  String modIdColName,
			  String modTableName,
			  String col_order) throws SQLException {
    	
		StringBuffer sql = new StringBuffer(512);
		sql.append(" SELECT res_id, itm_id, itm_title t_title, res_title, res_subtype, mod_max_score, mod_pass_score, mov_ent_id, mov_score, mov_total_attempt, ")
		   .append("        min(mvh_last_acc_datetime) start_visit_time, max(mvh_last_acc_datetime) last_visit_time, mvh_tkh_id ")
		   .append(" FROM resources ")
		   .append(" INNER JOIN resourcecontent ON (rcn_res_id_content = res_id) ")
		   .append(" INNER JOIN Course ON (cos_res_id = rcn_res_id) ")
		   .append(" INNER JOIN aeItem ON (cos_itm_id = itm_id) ")
		   .append(" INNER JOIN module ON (mod_res_id = res_id) ")
		   .append(" LEFT JOIN ( SELECT mov_mod_id, mov_cos_id, mov_ent_id, mov_score, mov_total_attempt, mov_tkh_id FROM moduleEvaluation ");
		if (entIdTableName != null) {
			sql.append(" WHERE EXISTS (SELECT ").append(entIdColName).append(" FROM ").append(entIdTableName)
			                                  .append(" INNER JOIN reguser ON (usr_ent_id = ").append(entIdColName).append(" AND usr_status = ?) ")
							                  .append(" WHERE ").append(entIdColName).append(" = mov_ent_id ").append(" ) ");
		}
		sql.append(" ) movE ON (movE.mov_mod_id = res_id AND movE.mov_cos_id = cos_res_id) ");
		sql.append(" LEFT JOIN moduleEvaluationHistory on (mvh_ent_id = mov_ent_id AND mvh_mod_id = res_id AND mvh_tkh_id = mov_tkh_id) ");
		sql.append(" WHERE EXISTS (SELECT ").append(modIdColName).append(" FROM ").append(modTableName)
		   									.append(" WHERE ").append(modIdColName).append(" = res_id ").append(" ) ");
		if (att_start_datetime != null) {
			sql.append(" AND mod_eff_start_datetime >= ? ");
		}
		if (att_end_datetime != null) {
			sql.append(" AND mod_eff_start_datetime <= ? ");
		}
		sql.append(" GROUP BY res_id, itm_id, itm_title, res_title, res_subtype, mod_max_score, mod_pass_score, mov_ent_id, mov_score, mov_total_attempt, mvh_tkh_id ");
		sql.append(col_order);
		return sql.toString();
	}
    
    /**
     * 面授培训：itm_eff_end_datetime
     * 网上培训：itm_content_eff_end_datetime，如果itm_content_eff_end_datetime is null 则不受日期条件影响
     * 混合式培训：取 itm_eff_end_datetime 和 itm_content_eff_end_datetime 的最大值，
     *          如果 itm_eff_end_datetime 和 itm_content_eff_end_datetime 都是NULL则不受日期条件影响
     * @param isFirst 是否第一个条件
     * @return 培训结束日期的sql语句
     */
    public static String getReportTrainEndDateSql(Timestamp start_datetime, Timestamp end_datetime, boolean isFirst, String tName) {
    	StringBuffer sql = new StringBuffer();
    	if (start_datetime == null && end_datetime == null) {
    		return sql.toString();
    	}
    	if (tName == null || tName.length() == 0) {
    		tName = "";
    	} else {
    		tName += ".";
    	}
    	if (!isFirst) {
    		sql.append(" AND ");
    	}
    				//无限
    	sql.append("  ( (").append(tName).append("itm_eff_end_datetime IS NULL AND ").append(tName).append("itm_content_eff_end_datetime IS NULL)");
    	sql.append("   OR ( ");//面授日期 或 混合式课程的面授时间大于网上内容时间
    	sql.append("        (").append(tName).append("itm_content_eff_end_datetime IS NULL OR ").append(tName).append("itm_eff_end_datetime >= ")
    																							 .append(tName).append("itm_content_eff_end_datetime) ");
    	if (start_datetime != null) {
    		sql.append("     AND ").append(tName).append("itm_eff_end_datetime >= ? ");
    	}
    	if (end_datetime != null) {
    		sql.append("     AND ").append(tName).append("itm_eff_end_datetime <= ? ");
    	}
    	sql.append("      ) ");
    	sql.append("   OR ( ");//网上内容结束日期 或 混合式课程的网上内容时间大于面授时间
    	sql.append("        (").append(tName).append("itm_eff_end_datetime is null OR ").append(tName).append("itm_content_eff_end_datetime >= ")
    																								   .append(tName).append("itm_eff_end_datetime) ");
    	if (start_datetime != null) {
    		sql.append("     AND ").append(tName).append("itm_content_eff_end_datetime >= ? ");
    	}
    	if (end_datetime != null) {
    		sql.append("     AND ").append(tName).append("itm_content_eff_end_datetime <= ? ");
    	}
    	sql.append("      ) ) ");
    	return sql.toString();
    }
    
    public static String getTrainFeeStatReport(String trainScope, Timestamp start_datetime, Timestamp end_datetime, String[] dummy_type) {
    	StringBuffer sql = new StringBuffer(512);
    	sql.append(" SELECT cItm.itm_id, pItm.itm_code p_itm_code, cItm.itm_title t_title, cItm.itm_code c_itm_code, cItm.itm_title c_itm_title, ")
    	   .append("        cItm.itm_type, cItm.itm_blend_ind, cItm.itm_exam_ind, cItm.itm_ref_ind, ito_type, ito_budget, ito_actual ")
    	   .append(" FROM aeItem cItm ")
    	   .append(" LEFT JOIN aeItemCost ON (ito_itm_id = cItm.itm_id) ")
    	   .append(" LEFT JOIN aeItemRelation ON (ire_child_itm_id = cItm.itm_id) ")
    	   .append(" LEFT JOIN aeItem pItm ON (ire_parent_itm_id = pItm.itm_id) ");
    	if (TrainFeeStatReport.TRAIN_SCOPE_PLAN.endsWith(trainScope)) {
    		sql.append(" INNER JOIN tpTrainingPlan ON (tpn_tcr_id = ?) ")
    		   .append(" WHERE cItm.itm_plan_code = tpn_code ");
    	} else {
    		sql.append(" WHERE cItm.itm_tcr_id = ? AND cItm.itm_plan_code IS NULL ");
    	}
    	sql.append(" AND cItm.itm_create_run_ind = ? ");
    	sql.append(getReportTrainEndDateSql(start_datetime, end_datetime, false, "cItm"));
    	sql.append(aeItemDummyType.genSqlByItemDummyType(dummy_type, "cItm", true));
    	return sql.toString();
    }
}