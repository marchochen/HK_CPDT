package com.cw.wizbank.ae.db.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.entity.AcRole;

public class SqlStatements {
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

    // define the SQLs here

    // for DbItemMote
    public static final String sql_ins_itemMote =
        "INSERT INTO aeItemMote " +
            " (imt_itm_id, imt_budget_cmt, imt_participant_cmt, imt_rating_cmt, imt_pos_cmt, imt_neg_cmt, imt_ist_cmt, imt_suggestion, imt_status,  " +
            " imt_create_timestamp, imt_create_usr_id, imt_upd_timestamp, imt_upd_usr_id) " +
            " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    public static final String sql_get_itemMote =
        "SELECT imt_itm_id, imt_budget_cmt, imt_participant_cmt, imt_rating_cmt, imt_pos_cmt, imt_neg_cmt, imt_ist_cmt, imt_suggestion, imt_status, " +
            " imt_create_timestamp, imt_create_usr_id, imt_upd_timestamp, imt_upd_usr_id " +
            " FROM aeItemMote " +
            " WHERE imt_itm_id = ? ";

    public static final String sql_upd_itemMote =
        "UPDATE aeItemMote SET imt_budget_cmt = ? , imt_participant_cmt = ? , imt_rating_cmt = ? , imt_pos_cmt = ? , imt_neg_cmt = ? , imt_ist_cmt = ? , imt_suggestion = ? , imt_status= ? , " +
            " imt_upd_timestamp = ? , imt_upd_usr_id = ? " +
            " WHERE imt_itm_id = ? ";

    // for itemBudget
    public static final String sql_ins_itemBudget =
        "INSERT INTO aeitemBudget " +
            " (ibd_itm_id, ibd_target, ibd_actual, ibd_create_timestamp, ibd_create_usr_id, ibd_update_timestamp, ibd_update_usr_id " +
            " values (?, ?, ?, ?, ?, ?, ? ) ";

    public static final String sql_get_itemBudget =
        "SELECT ibd_itm_id, ibd_target, ibd_actual, ibd_create_timestamp, ibd_create_usr_id, ibd_update_timestamp, ibd_update_usr_id " +
            " FROM aeitemBudget " +
            " WHERE ibd_itm_id = ? ";

    public static final String sql_upd_itemBudget =
        "UPDATE aeitemBudget SET ibd_target = ? , ibd_actual = ? , ibd_update_timestamp = ? , ibd_update_usr_id  = ? " +
            " WHERE ibd_itm_id = ? ";

    public static final String sql_get_run_capacity =
        "SELECT itm_capacity FROM aeItemRelation, aeItem WHERE ire_child_itm_id = itm_id AND ire_parent_itm_id = ? ";

    public static final String sql_get_cos_rating_lst =
        "SELECT irt_itm_id, irt_rate FROM aeItemRating WHERE irt_itm_id = ? AND irt_type = ? " +
        " UNION " +
        " SELECT irt_itm_id, irt_rate FROM aeitemrating, aeItemRelation WHERE irt_itm_id = ire_child_itm_id AND ire_parent_itm_id = ? AND irt_type = ? ";

    public static final String sql_ins_itemResources =
        "INSERT INTO aeItemResources " +
            " (ire_itm_id, ire_res_id, ire_type) " +
            " values (?, ?, ? ) ";

    public static final String sql_get_res_id_itemResources =
        "SELECT ire_res_id FROM aeItemResources " +
            " WHERE ire_itm_id = ? AND ire_type = ? " ;

    public static final String sql_get_itm_id_itemResources =
        "SELECT ire_itm_id FROM aeItemResources " +
            " WHERE ire_res_id = ? AND ire_type = ? " ;

    public static final String sql_del_itemResources =
        "DELETE From aeItemResources " +
            " WHERE ire_itm_id = ? AND ire_res_id = ? AND ire_type = ? ";

    public static final String sql_get_run_lst =
        " SELECT itm_id, itm_title FROM aeItem, aeItemRelation WHERE itm_id = ire_child_itm_id AND ire_parent_itm_id = ? ";

    public static final String sql_get_run_lst_evn =
        "SELECT itm_id, itm_title, count(*) evn_count FROM aeItem, aeItemRelation, aeItemResources WHERE itm_id = ire_child_itm_id AND ire_parent_itm_id = ? and ire_itm_id = itm_id and ire_type = 'EVN' group by itm_id, itm_title ";

/* dymanic
    public static final String sql_get_itm_res_lst =
        "SELECT res_id, res_title, mod_eff_start_datetime, mod_eff_end_datetime FROM aeItem, aeItemResources, Resources , Module " +
        " WHERE ire_itm_id = itm_id and ire_res_id = res_id and mod_res_id = res_id " +
        " and itm_id = ? and mod_type = ? ";
*/
/* dymanic
    public static final String sql_get_res_lst =
        "SELECT res_id, res_title, res_usr_id_owner, mod_eff_start_datetime, mod_eff_end_datetime FROM Resources , Module " +
        " WHERE mod_res_id = res_id AND mod_type = ? ";
*/
    public static final String sql_get_itemRating =
        "SELECT irt_itm_id, irt_ent_id, irt_type, irt_rate, irt_create_timestamp, irt_create_usr_id, irt_update_timestamp, irt_update_usr_id FROM aeItemRating WHERE irt_itm_id = ? AND irt_type = ? ";

    public static final String sql_get_avg_itemRating =
        "SELECT AVG(irt_rate) AS AVG_RATING FROM aeItemRating WHERE irt_itm_id = ? AND irt_type = ? ";

    public static final String sql_ins_itemRating =
        "INSERT INTO aeItemRating (irt_itm_id, irt_ent_id, irt_type, irt_rate, irt_create_timestamp, irt_create_usr_id, irt_update_timestamp, irt_update_usr_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String sql_upd_itemRating =
        "UPDATE aeitemRating SET irt_rate = ? , irt_update_timestamp = ? , irt_update_usr_id  = ? " +
            " WHERE irt_itm_id = ? AND irt_type = ? ";

    public static final String SQL_GET_APPLICABLE_ITY =
        " select distinct(a.ity_id), b.ity_seq_id from aeITemType a, aeItemType b " +
        " where a.ity_owner_ent_id = ? " +
        " and a.ity_apply_ind = ? " +
        " and a.ity_id = b.ity_id " +
        " and a.ity_owner_ent_id = b.ity_owner_ent_id " +
        " and b.ity_seq_id = (Select min(c.ity_seq_id) from aeItemType c where c.ity_id = b.ity_id and c.ity_owner_ent_id = b.ity_owner_ent_id) " +
        " order by b.ity_seq_id ";

    public static final String sql_get_all_itemType_in_org =
        "SELECT ity_owner_ent_id, ity_id, ity_seq_id, ity_title_xml, ity_icon_url, ity_create_timestamp, ity_create_usr_id, ity_init_life_status, ity_create_run_ind, ity_create_session_ind, ity_has_attendance_ind, ity_ji_ind, ity_completion_criteria_ind , ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind , ity_reminder_criteria_ind, ity_tkh_method, ity_exam_ind ,ity_blend_ind ,ity_ref_ind,ity_integ_ind FROM aeItemType " +
            "WHERE ity_owner_ent_id = ? AND ity_run_ind = ? AND ity_session_ind = ? ORDER BY ity_seq_id asc";

    public static final String sql_get_itemType =
        "SELECT ity_owner_ent_id, ity_id, ity_seq_id, ity_title_xml, ity_icon_url, ity_create_timestamp, ity_create_usr_id, ity_init_life_status, ity_create_run_ind, ity_create_session_ind, ity_has_attendance_ind, ity_ji_ind, ity_completion_criteria_ind , ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind , ity_reminder_criteria_ind, ity_tkh_method,ity_blend_ind,ity_exam_ind,ity_ref_ind FROM aeItemType " +
            "WHERE ity_owner_ent_id = ? AND ity_id = ? AND ity_run_ind = ? AND ity_session_ind = ? and ity_blend_ind = ? and ity_exam_ind = ? and ity_ref_ind = ?";

    public static final String sql_get_itemTypeId_by_seqId =
        "SELECT ity_id FROM aeItemType " +
            "WHERE ity_seq_id = ? AND ity_owner_ent_id = ? ";

    public static final String sql_get_templateType_of_itemType =
        "SELECT DISTINCT ttp_id, ttp_title FROM aeItemType, aeItemTypeTemplate, aeTemplateType " +
            "WHERE ity_owner_ent_id = ? AND ity_id = ? " +
            "AND itt_ity_owner_ent_id = ity_owner_ent_id AND itt_ity_id = ity_id " +
            "AND itt_ttp_id = ttp_id AND itt_run_tpl_ind = ? AND itt_session_tpl_ind = ? " +
            "AND ity_run_ind = itt_run_tpl_ind " +
            "AND ity_session_ind = itt_session_tpl_ind " ;

    public static final String sql_get_templateId_of_itemTypeTemplate =
        "SELECT itt_tpl_id FROM aeItemType, aeItemTypeTemplate, aeTemplateType " +
            "WHERE ity_owner_ent_id = ? AND ity_id = ? AND ttp_title = ? " +
            "AND itt_ity_owner_ent_id = ity_owner_ent_id AND itt_ity_id = ity_id " +
            "AND itt_ttp_id = ttp_id AND itt_run_tpl_ind = ? AND itt_session_tpl_ind = ? " +
            "AND ity_run_ind = itt_run_tpl_ind " +
            "AND ity_session_ind = itt_session_tpl_ind " +
            "ORDER BY itt_seq_id ";

    public static final String sql_get_templateView =
        "SELECT tvw_tpl_id, tvw_id, tvw_xml, " +
            "tvw_create_timestamp, tvw_create_usr_id, " +
            "tvw_cat_ind, tvw_target_ind, tvw_cm_ind, " +
            "tvw_mote_ind, tvw_itm_acc_ind, " +
            "tvw_res_ind, tvw_rsv_ind, tvw_wrk_tpl_ind, " +
            "tvw_cost_center_ind, tvw_ctb_ind, " +
            "tvw_filesize_ind, tvw_km_published_version_ind, tvw_km_domain_ind, " +
            "tvw_tcr_ind " +
            "FROM aeTemplateView " +
            "WHERE tvw_tpl_id = ? AND tvw_id = ? ";
   
    public static final String sql_get_tnd_no_child_and_type_info =
        "select parent.tnd_id, parent.tnd_type from aeTreeNode parent where parent.tnd_id in ? AND " +
        " NOT EXISTS (Select child.tnd_id from aeTreeNode child where child.tnd_parent_tnd_id = parent.tnd_id)";

    public static final String sql_del_itm_all_tpl =
        " Delete From aeItemTemplate Where itp_itm_id = ? ";

	public static final String sql_ins_view_item_template =
				" Insert into aeItemTemplate "
			+ " (itp_ttp_id, itp_itm_id, itp_tpl_id, itp_create_timestamp, itp_create_usr_id) "
			+ " VALUES (?, ?, ?, ?, ?)";

	public static final String sql_select_view_item_template =
			" Select ttp_id From aeTemplateType Where ttp_title = ? ";

    public static final String sql_ins_item_template =
        " Insert into aeItemTemplate " +
        " (itp_itm_id, itp_tpl_id, itp_ttp_id, itp_create_usr_id, itp_create_timestamp) " +
        " Values (?, ?, ?, ?, ?) ";


    //Item Competency
    public static final String sql_ins_item_competency =
            " INSERT INTO aeItemCompetency "
            + " ( itc_itm_id, itc_skl_skb_id, itc_skl_level, itc_create_usr_id, itc_create_timestamp ) "
            + " VALUES( ?, ?, ?, ?, ? ) " ;


    public static final String sql_get_item_skills =
            " SELECT itc_skl_skb_id, itc_skl_level "
            + " FROM aeItemCompetency WHERE itc_itm_id = ? ";


    public static final String sql_del_item_skills_relation =
            " DELETE From aeItemCompetency WHERE itc_itm_id = ? ";

    public static final String sql_del_item_skill_relation_by_skill =
            " DELETE From aeItemCompetency WHERE itc_skl_skb_id = ? ";

    public static final String sql_get_item_by_skills_id =
            " SELECT DISTINCT itc_itm_id FROM aeItemCompetency WHERE ";

    public static final String sql_get_item_skill_relation =
            " SELECT itc_itm_id, itc_skl_skb_id, itc_skl_level "
          + " FROM aeItemCompetency WHERE itc_skl_skb_id IN ";

    public static final String sql_ins_cit =
        " Insert into aeCatalogItemType " +
        " (cit_cat_id, cit_ity_owner_ent_id, cit_ity_id, cit_create_usr_id, cit_create_timestamp) " +
        " Values (?, ?, ?, ?, ?) ";

    public static final String sql_del_cit_by_cat =
        " Delete From aeCatalogItemType Where cit_cat_id = ? ";

    public static final String sql_get_cit_by_cat =
        " Select cit_ity_id From aeCatalogItemType Where cit_cat_id = ? ";


    // learning soln
    public static final String sql_get_period_xml =
        " SELECT snt_xml FROM aeLearningSolnTemplate WHERE snt_owner_ent_id = ?";

    public static final String sql_get_my_training_needs =
        " SELECT itm_id, skl_skb_id, avg(ssc_level) as average " +
        " FROM aeItem, aeItemCompetency, cmAssessment, cmAssessmentUnit, cmSkillSetCoverage, cmSkill " +
        " WHERE asm_id = asu_asm_id " +
        " AND asu_sks_skb_id = ssc_sks_skb_id " +
        " AND ssc_skb_id = skl_skb_id " +
        " AND asm_status = ? " +
        " AND asu_type = ? " +
        " AND itm_id = itc_itm_id " +
        " AND itc_skl_skb_id = skl_skb_id " +
        " AND asm_ent_id = ? " +
        " GROUP BY itm_id, skl_skb_id ";
/*
    public static final String sql_get_my_learning_soln =
        " SELECT lsn_itm_id, item2.itm_id, lsn_period_id " +
        " FROM aeLearningSoln, aeItem item1, aeItem item2 " +
        " WHERE lsn_ent_id = ? " +
        " AND lsn_ent_id_lst like ? " +
        " AND item1.itm_id = lsn_itm_id " +
        " AND item1.itm_code = item2.itm_code " +
        " AND lsn_type = ? " +
        " AND lsn_status_ind = 1 " +
        " AND item2.itm_deprecated_ind = 0 ";

    public static final String sql_get_my_learning_soln_non_peer =
        " SELECT lsn_itm_id, item2.itm_id, lsn_period_id "
      + " FROM aeLearningSoln, aeItem item1, aeItem item2 "
      + " WHERE lsn_ent_id = ? "
      + " AND item1.itm_id = lsn_itm_id "
      + " AND item1.itm_code = item2.itm_code "
      + " AND lsn_type = ? "
      + " AND lsn_status_ind = 1 "
      + " AND item2.itm_deprecated_ind = 0 ";
*/

	public static String getMyLearningSoln(Connection con, String groupAncester, String gradeAncester) throws SQLException{
		String sqlStr =
		" SELECT lsn_itm_id, itm_id, lsn_period_id " +
		" FROM aeLearningSoln, aeItem, aeItemTargetRuleDetail " +
		" WHERE lsn_ent_id = ? " +
		" AND lsn_ent_id_lst like ? " +
		" AND itm_id = lsn_itm_id " +
		" AND lsn_type = ? " +
		" AND lsn_status_ind = 1 " +
		" AND itm_deprecated_ind = 0 " +
		" AND itm_access_type = ird_type "+
		" AND itm_id = ird_itm_id " + 
		" AND ird_group_id in (" + groupAncester + " )" + 
		" AND ird_grade_id in (" + gradeAncester + " )" +
		" UNION "+
		" SELECT lsn_itm_id, itm_id, lsn_period_id FROM aeLearningSoln, aeItem "+
		" WHERE lsn_ent_id = ? " +
		" AND lsn_ent_id_lst like ? " +
		" AND itm_id = lsn_itm_id " +
		" AND lsn_type = ? " +
		" AND lsn_status_ind = 1 " +
		" AND itm_deprecated_ind = 0 " +
		" AND (itm_access_type = 'ALL' OR itm_access_type is NULL) "
		;
		return sqlStr;
	}

	public static String getMyLearningSolnNonPeer(Connection con, String groupAncester, String gradeAncester) throws SQLException{
		String sqlStr =
		" SELECT lsn_itm_id, itm_id, lsn_period_id " +
		" FROM aeLearningSoln, aeItem, aeItemTargetRuleDetail " +
		" WHERE lsn_ent_id = ? " +
		" AND itm_id = lsn_itm_id " +
		" AND lsn_type = ? " +
		" AND lsn_status_ind = 1 " +
		" AND itm_deprecated_ind = 0 " +
		" AND itm_access_type = ird_type "+
		" AND itm_id = ird_itm_id " + 
		" AND ird_group_id in (" + groupAncester + ")" +
		" AND ird_grade_id in (" + gradeAncester + ")" +
		" UNION "+
		" SELECT lsn_itm_id, itm_id, lsn_period_id FROM aeLearningSoln, aeItem "+
		" WHERE lsn_ent_id = ? " +
		" AND itm_id = lsn_itm_id " +
		" AND lsn_type = ? " +
		" AND lsn_status_ind = 1 " +
		" AND itm_deprecated_ind = 0 " +
		" AND (itm_access_type = 'ALL' OR itm_access_type is NULL) ";
		return sqlStr;
	}

    public static final String sql_get_learning_soln =
        " SELECT item1.itm_id, lsn_itm_id " +
        " FROM aeLearningSoln, aeItem item1, aeItem item2 " +
        " WHERE item1.itm_id = ? " + 
        " AND item1.itm_code = item2.itm_code " + 
        " AND item2.itm_id = lsn_itm_id " + 
        " AND lsn_ent_id = ? " + 
        " AND lsn_type = ? " +
        " AND lsn_ent_id_lst like ? " +
        " AND lsn_status_ind = 1 ";

    public static final String sql_get_learning_soln_non_peer =
        " SELECT item1.itm_id, lsn_itm_id " 
      + " FROM aeLearningSoln, aeItem item1, aeItem item2 "
      + " WHERE item1.itm_id = ? "
      + " AND item1.itm_code = item2.itm_code " 
      + " AND item2.itm_id = lsn_itm_id "
      + " AND lsn_ent_id = ? "
      + " AND lsn_type = ? "
      + " AND lsn_status_ind = 1 ";


    public static final String sql_ins_learning_soln =
        " INSERT INTO aeLearningSoln (lsn_ent_id, lsn_itm_id, lsn_period_id, lsn_ent_id_lst, lsn_create_usr_id, lsn_create_timestamp, lsn_upd_usr_id, lsn_upd_timestamp, lsn_type, lsn_status_ind) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    public static final String sql_upd_learning_soln =
        " UPDATE aeLearningSoln " +
        " SET lsn_period_id = ?, lsn_upd_usr_id = ?, lsn_upd_timestamp = ? " +
        " WHERE lsn_ent_id = ? " +
        " AND lsn_itm_id = ? " +
        " AND lsn_ent_id_lst LIKE ? " +
        " AND lsn_type = ? " + 
        " AND lsn_status_ind = 1 ";

    public static final String sql_upd_learning_soln_non_peer =
        " UPDATE aeLearningSoln " +
        " SET lsn_period_id = ?, lsn_upd_usr_id = ?, lsn_upd_timestamp = ? " +
        " WHERE lsn_ent_id = ? " + 
        " AND lsn_itm_id = ? " + 
        " AND lsn_type = ? " +
        " AND lsn_status_ind = 1 ";

    public static final String sql_del_learning_soln =
        " DELETE From aeLearningSoln " + 
        " WHERE lsn_ent_id = ? " +
        " AND lsn_itm_id = ? " +
        " AND lsn_ent_id_lst LIKE ? " +
        " AND lsn_type = ? " +
        " AND lsn_status_ind = 1 ";

    public static final String sql_del_learning_soln_non_peer =
        " DELETE FROM aeLearningSoln " + 
        " WHERE lsn_ent_id = ? " + 
        " AND lsn_itm_id = ? " +
        " AND lsn_type = ? " +
        " AND lsn_status_ind = 1 ";

    public static final String sql_disable_learning_soln =
        " UPDATE aeLearningSoln " + 
        " SET lsn_status_ind = 0, lsn_upd_timestamp = ? " +
        " WHERE lsn_ent_id = ? " +
        " AND lsn_itm_id = ? " +
        " AND lsn_ent_id_lst LIKE ? " +
        " AND lsn_type = ? " +
        " AND (lsn_status_ind = 1 or lsn_upd_timestamp = ?) ";

    public static final String sql_disable_learning_soln_non_peer =
        " UPDATE aeLearningSoln " + 
        " SET lsn_status_ind = 0, lsn_upd_timestamp = ? " +
        " WHERE lsn_ent_id = ? " + 
        " AND lsn_itm_id = ? " +
        " AND lsn_type = ? " +
        " AND (lsn_status_ind = 1 or lsn_upd_timestamp = ?) ";

    public static final String sql_enable_learning_soln =
        " UPDATE aeLearningSoln " + 
        " SET lsn_status_ind = 1, lsn_upd_timestamp = ? " +
        " WHERE lsn_ent_id = ? " +
        " AND lsn_itm_id = ? " +
        " AND lsn_ent_id_lst LIKE ? " +
        " AND lsn_type = ? " +
        " AND lsn_upd_timestamp = ? ";

    public static final String sql_enable_learning_soln_non_peer =
        " UPDATE aeLearningSoln " + 
        " SET lsn_status_ind = 1, lsn_upd_timestamp = ? " +
        " WHERE lsn_ent_id = ? " + 
        " AND lsn_itm_id = ? " +
        " AND lsn_type = ? " +
        " AND lsn_upd_timestamp = ? ";
        
    public static final String sql_del_learning_soln_by_item =
        " DELETE From aeLearningSoln WHERE lsn_itm_id = ? ";

    public static final String sql_get_itm_rate_def_by_site = " SELECT ird_id FROM aeItemRatingDefination, acSite WHERE ste_ird_id = ird_id AND ste_ent_id = ? ";

    public static final String sql_ins_itm_rate_def = "INSERT INTO aeItemRatingDefination (ird_range_xml,  ird_default_ind, ird_update_timestamp, ird_update_usr_id) values ( ?, ?, ?, ?)";
    public static final String sql_upd_itm_rate_def = "UPDATE aeItemRatingDefination SET ird_range_xml = ? ,  ird_update_timestamp = ? , ird_update_usr_id = ? WHERE ird_id = ? ";
    public static final String sql_get_by_upd_ts = "SELECT ird_id FROM aeItemRatingDefination WHERE ird_update_timestamp = ? AND ird_update_usr_id = ? ";
    public static final String sql_get_parent_itm_id = "select ire_parent_itm_id from aeItemRelation where ire_child_itm_id = ?";
    public static final String sql_get_pgm_itm_ids = "SELECT pdt_itm_id, pdt_core_ind FROM aeProgramDetails, aeItem WHERE pdt_pgm_itm_id = ? AND pdt_itm_id = itm_id ORDER BY itm_title";

    public static final String sql_ins_aer =
        " Insert into aeAppnEnrolRelation (aer_app_id, aer_ent_id, aer_res_id, " +
        " aer_status, aer_create_usr_id, aer_create_timestamp, " +
        " aer_upd_usr_id, aer_upd_timestamp) " +
        " Values (?, ?, ?, ?, ? ,? ,?, ?) ";

    public static final String sql_is_aer_exist =
        " Select * from aeAppnEnrolRelation " +
        " Where aer_app_id = ? " +
        " And aer_ent_id = ? " +
        " And aer_res_id = ? ";

    public static final String sql_set_aer_status_by_enrol =
        " Update aeAppnEnrolRelation set aer_status = ? " +
        " ,aer_upd_usr_id = ? " +
        " ,aer_upd_timestamp = ? " +
        " Where aer_ent_id = ? " +
        " And aer_res_id = ? ";

    public static final String sql_del_aer =
        " Delete From aeAppnEnrolRelation " +
        " Where aer_app_id = ? " +
        " And aer_ent_id = ? " +
        " And aer_res_id = ? ";

    public static final String sql_del_aer_by_appn =
        " Delete From aeAppnEnrolRelation " +
        " Where aer_app_id = ? ";

    public static final String sql_get_aer_cnt_by_enrol =
        " Select count(*) from aeAppnEnrolRelation " +
        " Where aer_ent_id = ? " +
        " And aer_res_id = ? ";

    public static final String sql_get_ity_init_life_status =
        " Select ity_init_life_status From aeItemType " +
        " Where ity_id = ? " +
        " And ity_owner_ent_id = ? " +
        " And ity_run_ind = ? " +
        " And ity_session_ind = ? ";

    public static final String sql_get_applicable_itemType_in_org =
        " select ity_owner_ent_id, ity_id, ity_seq_id, ity_title_xml, ity_icon_url, ity_create_timestamp, ity_create_usr_id, ity_init_life_status, ity_create_run_ind, ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind , ity_reminder_criteria_ind, ity_tkh_method from aeItemType a " +
        " where a.ity_owner_ent_id = ? " +
        " and ((a.ity_apply_ind = ? and a.ity_run_ind = ?) " +
        " or (a.ity_apply_ind = ? and a.ity_create_run_ind = ? " +
        " and exists (select b.ity_id from aeItemType b where b.ity_apply_ind = ? and b.ity_id = ity_id and b.ity_run_ind = ? and b.ity_owner_ent_id = a.ity_owner_ent_id))) " +
        " order by ity_seq_id ";

    public static final String sql_get_has_qdb_itemType_in_org =
        " select ity_owner_ent_id, ity_id, ity_seq_id, ity_title_xml, ity_icon_url, ity_create_timestamp, ity_create_usr_id, ity_init_life_status, ity_create_run_ind, ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind , ity_reminder_criteria_ind, ity_tkh_method,ity_blend_ind,ity_exam_ind,ity_ref_ind from aeItemType " +
        " where ity_owner_ent_id = ? " +
        " and ity_qdb_ind = ? and ity_run_ind = ?" +
        " order by ity_seq_id ";

    public static final String sql_get_ity_ind =
        " Select ity_create_run_ind, ity_create_session_ind, ity_has_attendance_ind, ity_apply_ind, ity_qdb_ind, ity_auto_enrol_qdb_ind, ity_ji_ind, ity_completion_criteria_ind, ity_can_cancel_ind , ity_reminder_criteria_ind, ity_blend_ind,ity_exam_ind,ity_ref_ind" +
        " From aeItemType " +
        " Where ity_owner_ent_id = ? " +
        " And ity_id = ? " +
        " And ity_run_ind = ? " +
        " And ity_session_ind = ? and ity_blend_ind = ? and ity_exam_ind = ? and ity_ref_ind = ?";

    public static final String SQL_GET_TPL_BY_ITY =
        " select ttp_title, a.itt_tpl_id " +
        " from aeItemTypeTemplate a, aeTemplateType " +
        " where a.itt_ity_owner_ent_id = ? " +
        " and a.itt_ity_id = ? " +
        " and a.itt_run_tpl_ind = ? " +
        " and a.itt_session_tpl_ind = ? " +
        " and a.itt_ttp_id = ttp_id " +
        " and a.itt_seq_id = (select min(b.itt_seq_id) from aeItemTypeTemplate b " +
        "        where b.itt_ity_owner_ent_id = a.itt_ity_owner_ent_id " +
        "        and b.itt_run_tpl_ind = a.itt_run_tpl_ind " +
        "        and b.itt_session_tpl_ind = a.itt_session_tpl_ind " +
        "        and b.itt_ttp_id = a.itt_ttp_id)    ";

    // report
    public static final String sql_get_rpt_spec = "SELECT rte_id, rte_title_xml, rte_type, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url, rsp_id, rsp_rte_id, rsp_ent_id, rsp_title, rsp_xml, rsp_create_usr_id, rsp_create_timestamp, rsp_upd_usr_id, rsp_upd_timestamp FROM ReportTemplate, ReportSpec WHERE rsp_id = ? AND rsp_rte_id = rte_id";
    public static final String sql_upd_rpt_spec = "UPDATE ReportSpec SET rsp_ent_id = ?, rsp_title = ?, rsp_upd_usr_id = ?, rsp_upd_timestamp = ? WHERE rsp_id = ?";
    public static final String sql_del_rpt_spec = "DELETE FROM ReportSpec WHERE rsp_id = ?";
    public static final String sql_get_rpt_rsp_xml_1 = "SELECT rsp_xml FROM ReportSpec WHERE rsp_rte_id = ? AND rsp_ent_id = ? AND rsp_create_usr_id = ? AND rsp_create_timestamp = ? FOR UPDATE";
    public static final String sql_get_rpt_rsp_xml_2 = "SELECT rsp_xml FROM ReportSpec WHERE rsp_id = ? FOR UPDATE";
    public static final String sql_get_rpt_tpl = "SELECT rte_id, rte_type, rte_title_xml, rte_get_xsl, rte_exe_xsl, rte_dl_xsl, rte_meta_data_url FROM ReportTemplate WHERE rte_id = ?";
    public static final String sql_get_rpt_id = "SELECT rte_id FROM ReportTemplate WHERE rte_owner_ent_id = ? AND rte_type = ?";
    public SqlStatements() {
    }


    public static String getEnrollmentInfo(String itm_lst, boolean hasOffReadPrivilege) {

        StringBuffer result = new StringBuffer();

        result.append("SELECT itm_id AS COS_ID, 0 AS CLASS_ID, itm_appn_start_datetime, itm_appn_end_datetime, itm_eff_start_datetime, itm_eff_end_datetime FROM aeItem WHERE itm_apply_ind= ? and itm_id IN ");
        result.append(itm_lst);
        if (!hasOffReadPrivilege)
            result.append(" AND itm_status = 'ON' ");
        result.append(" UNION ");
        result.append("SELECT item1.itm_id AS COS_ID, item2.itm_id AS CLASS_ID, item2.itm_appn_start_datetime, item2.itm_appn_end_datetime, item2.itm_eff_start_datetime, item2.itm_eff_end_datetime FROM aeItem item1, aeItem item2, aeItemRelation WHERE item1.itm_id IN ");
        result.append(itm_lst);
        result.append(" AND item1.itm_apply_ind=?  and item1.itm_create_run_ind=? and item1.itm_id = ire_parent_itm_id and item2.itm_id = ire_child_itm_id ");
        if (!hasOffReadPrivilege)
            result.append("  AND item2.itm_status = 'ON' ");

        result.append(" ORDER by COS_ID, itm_appn_start_datetime ");
        return result.toString();
    }

    public static final String sql_get_learning_soln_created_by = 
     " SELECT item2.itm_id, usr_ent_id " +
     " FROM aeLearningSoln, aeItem item1, aeItem item2 , RegUser " +
     " WHERE lsn_create_usr_id = usr_id " +
     " AND lsn_status_ind = 1 " + 
     " AND lsn_ent_id = ? " + 
     " AND lsn_ent_id_lst like ? " +
     " AND item1.itm_id = lsn_itm_id " + 
     " AND item1.itm_code = item2.itm_code " + 
     " AND item2.itm_deprecated_ind = ? " +
     " AND lsn_type = ? ";

    public static final String sql_get_learning_soln_created_by_non_peer = 
      " SELECT item2.itm_id, usr_ent_id " +
      " FROM aeLearningSoln, aeItem item1, aeItem item2 , RegUser " +
      " WHERE lsn_create_usr_id = usr_id "  +
      " AND lsn_status_ind = 1 " + 
      " AND lsn_ent_id = ? " +
      " AND item1.itm_id = lsn_itm_id " +
      " AND item1.itm_code = item2.itm_code " +
      " AND item2.itm_deprecated_ind = ? " +
      " AND lsn_type = ? ";


    public static final String sql_get_item_selected_workflow_template_id =
        " SELECT itp_tpl_id FROM aeItemTemplate , aeTemplateType "
       + " WHERE itp_itm_id = ? AND ttp_title = ? AND ttp_id = itp_ttp_id ";

    public static final String sql_get_item_approval_ind =
        " SELECT tpl_approval_ind FROM aeItemTemplate , aeTemplateType , aeTemplate "
       + " WHERE itp_itm_id = ? AND ttp_title = ? AND ttp_id = itp_ttp_id AND tpl_id = itp_tpl_id ";

    public static final String sql_get_item_suppored_template_id =
        " Select itt_tpl_id FROM aeItem, aeItemTypeTemplate, aeTemplateType "
      + " where itm_type = itt_ity_id and itm_id = ? and itt_ttp_id = ttp_id "
      + " and ttp_title = ? and itt_ity_owner_ent_id = ? ";


    public static final String sql_upd_multiple_item_template =
      " Update aeItemTemplate Set itp_tpl_id = ? "
    + " Where itp_ttp_id = ( Select ttp_id From aeTemplateType Where ttp_title = ? ) AND itp_itm_id IN ";

     public static final String sql_upd_att_remark =
            "update aeAttendance set att_remark = ? ,att_update_usr_id = ? ,att_update_timestamp = ? where att_app_id = ? and att_update_timestamp = ?";
            
    public static final String SQL_GET_LSN_BY_ENT_ID_N_TYPE = 
        " SELECT lsn_itm_id, lsn_period_id, lsn_ent_id_lst " + 
        " ,lsn_create_usr_id, lsn_create_timestamp, lsn_upd_usr_id, lsn_upd_timestamp " +
        " FROM aeLearningSoln " +
        " WHERE lsn_ent_id = ? " +
        " AND lsn_type = ? " +
        " AND lsn_status_ind = 1 ";

     public static final String sql_upd_att_rate =
            "update aeAttendance set att_rate = ? where att_app_id = ? and att_itm_id = ? ";

    public static final String sql_get_session_attendance = "SELECT att_ats_id  FROM aeAttendance , aeItem WHERE att_itm_id = itm_id and itm_session_ind = ? AND att_app_id = ? ";

     // get all workflow template
     public static final String sql_get_workflow_template = "SELECT tpl_id, tpl_xml, tpl_title, ttp_title " +
                                                            "FROM aeTemplate, aeTemplateType " +
                                                            "WHERE ttp_title LIKE '%WORKFLOW%' AND tpl_ttp_id = ttp_id";
     // get template title, type, ... by specified owner
     public static final String sql_get_workflow_template_cond_owner = " AND tpl_owner_ent_id = ?";

     // get template title, type, ... by specified tpl_id
     public static final String sql_get_workflow_template_cond_id = " AND tpl_id = ?";

     // get approver's servant
     public static String getApproverApplication(String[] statusList, String curProcessStatus) {
        return (SqlStatements.getApproverApplication(statusList,curProcessStatus, 0));
     }

      // get approver's servant
     public static String getApproverApplication(String[] statusList, String curProcessStatus, long itm_id) {
         StringBuffer sql_get_appr_appn = new StringBuffer();
         sql_get_appr_appn.append("SELECT app_id, app_ent_id ");
         sql_get_appr_appn.append("FROM aeApplication, aeItem, aeItemTemplate ");
         sql_get_appr_appn.append("WHERE itp_tpl_id = ?");

         if (curProcessStatus != null && curProcessStatus.length() > 0) {
            if(curProcessStatus.startsWith("(") && curProcessStatus.endsWith(")")) {
                sql_get_appr_appn.append(" AND ? is not null"); // dummy to keep same number of param
                sql_get_appr_appn.append(" AND app_process_status IN " + curProcessStatus + " ");
            } else {
                sql_get_appr_appn.append(" AND app_process_status = ? ");
            }
         } else {
             sql_get_appr_appn.append(" AND app_process_status IN (''");
             for (int i = 0; i < statusList.length; i++) {
                 sql_get_appr_appn.append(", ?");
             }
             sql_get_appr_appn.append(")");
         }
         sql_get_appr_appn.append(" AND itm_id = app_itm_id");
         sql_get_appr_appn.append(" AND app_id not in (SELECT ate_app_id from aeAppnTargetEntity WHERE ate_rol_ext_id = ? and ate_usr_ent_id != ?)");
        
         if(itm_id != 0)
                sql_get_appr_appn.append(" AND itm_id = ").append(itm_id);
         sql_get_appr_appn.append(" AND itm_id = itp_itm_id AND app_ent_id IN ");

         return sql_get_appr_appn.toString();
     }

          // get approver's servant
     public static String getApproverApplicationCnt(String[] statusList, long itm_id, String targetGroupLrnSql) {
         StringBuffer sql_get_appr_appn = new StringBuffer();
         sql_get_appr_appn.append("SELECT app_process_status , count(app_id) as cnt ");
         sql_get_appr_appn.append("FROM aeApplication, aeItem, aeItemTemplate ");
         sql_get_appr_appn.append("WHERE itp_tpl_id = ?");

         sql_get_appr_appn.append(" AND app_process_status IN (''");
         for (int i = 0; i < statusList.length; i++) {
            sql_get_appr_appn.append(", ?");
         }
         sql_get_appr_appn.append(")");
         
         sql_get_appr_appn.append(" AND itm_id = app_itm_id");
         sql_get_appr_appn.append(" AND app_id not in (SELECT ate_app_id from aeAppnTargetEntity WHERE ate_rol_ext_id = ? and ate_usr_ent_id != ?)");
        
         if(itm_id != 0)
                sql_get_appr_appn.append(" AND itm_id = ").append(itm_id);
         sql_get_appr_appn.append(" AND itm_id = itp_itm_id AND app_ent_id IN ").append(targetGroupLrnSql);
          sql_get_appr_appn.append(" group by app_process_status");
         return sql_get_appr_appn.toString();
     }

    public static String getApproverNomApplication(String processStatusLstSql, long itm_id, String targetGroupLrnSql, String sortCol, String sortOrder) {
        StringBuffer sql = new StringBuffer();

        sql.append("select self.usr_display_bil as self_display_bil, app_id, app_ent_id, app_process_status, app_upd_timestamp, appr.usr_ent_id as approver_ent_id, appr.usr_display_bil as approver_display_bil from aeApplication, aeAppnTargetEntity, RegUser appr, RegUser self where ");
 
        if (itm_id != 0) {
            sql.append("app_itm_id = ").append(itm_id).append(" and ");
        }
        
        sql.append(" app_ent_id in ").append(targetGroupLrnSql).append(" and ");

        if (processStatusLstSql != null) {
            sql.append(" app_process_status in ").append(processStatusLstSql).append(" and ");
        }
        
        sql.append(" app_id not in (select ate_app_id from aeAppnTargetEntity where ate_usr_ent_id = ? and ate_rol_ext_id = ?) and ");
        sql.append(" app_ent_id = self.usr_ent_id and ");
        sql.append(" app_id = ate_app_id and ate_usr_ent_id = appr.usr_ent_id ");
        
        if (sortCol != null) {
            sql.append(" order by ").append(sortCol);

            if (sortOrder != null) {
                sql.append(" ").append(sortOrder);   
            }        
        }
        
        return sql.toString();
    }

     // get approver's servant
     public static String getApproverApplicationCourse(String[] statusList, String usrSql) {
        StringBuffer sql_get_appr_appn = new StringBuffer();
        sql_get_appr_appn.append("select distinct itm_id, itm_title from aeItemTemplate, aeAppnTargetEntity, aeApplication, aeItem where ");        
        sql_get_appr_appn.append(" itm_id = app_itm_id and ate_usr_ent_id = ? and ate_rol_ext_id = ?");
        sql_get_appr_appn.append(" and itp_tpl_id = ? and itp_ttp_id = ? and itm_id = itp_itm_id");
            
        sql_get_appr_appn.append(" AND app_process_status IN (''");
        for (int i = 0; i < statusList.length; i++) {
            sql_get_appr_appn.append(", ?");
        }
        sql_get_appr_appn.append(")");
                 
        sql_get_appr_appn.append(" AND ate_app_id = app_id AND app_ent_id IN ");
        sql_get_appr_appn.append(usrSql);
             
        sql_get_appr_appn.append(" union select distinct itm_id, itm_title from aeItemTemplate, aeApplication, aeItem where ");        
        sql_get_appr_appn.append(" not exists (select ate_app_id from aeAppnTargetEntity where ate_app_id = app_id and ate_rol_ext_id = ?) ");
        sql_get_appr_appn.append(" and itp_tpl_id = ? and itp_ttp_id = ? and itm_id = itp_itm_id");
        
        sql_get_appr_appn.append(" AND app_process_status IN (''");
        for (int i = 0; i < statusList.length; i++) {
            sql_get_appr_appn.append(", ?");
        }
        sql_get_appr_appn.append(")");
        
        sql_get_appr_appn.append(" and app_itm_id = itm_id AND app_ent_id IN ");
        sql_get_appr_appn.append(usrSql);
        return sql_get_appr_appn.toString();
        /*
         select distinct itm_id, itm_title from aeAppnTargetEntity, aeApplication, aeItem where 
app_ent_id in (65, 56, 57, 58)
and app_process_status in ('Applied')
and ate_rol_ext_id = 'SUP_1' and ate_usr_ent_id = 66
and app_itm_id = itm_id and ate_app_id = app_id 
union 
select distinct itm_id, itm_title from aeApplication, aeItem where 
app_ent_id in (65, 56, 57, 58)
and app_process_status in ('Applied')
and not exists (select ate_app_id from aeAppnTargetEntity where ate_app_id = app_id and ate_rol_ext_id = 'SUP_1')
and app_itm_id = itm_id*/
     }

     /**
      * add to get the application by app_id
      * @param con
      * @return
      * @throws SQLException
      */
     public static String getApplicationList(Connection con,
                                             String appIdLst,
                                             String processStatus,
                                             long itmId,
                                             String entIdLst) throws SQLException {
         String sql_null_string = cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING);
         StringBuffer sqlStr = new StringBuffer(1024);

         /**
          * get the application list of the item with "classroom" type
          */
         sqlStr.append("SELECT");
         sqlStr.append(" app_id, app_ent_id, app_itm_id, app_status, app_process_status, ");
         sqlStr.append(" app_create_timestamp, app_create_usr_id, ");
         sqlStr.append(" app_upd_timestamp, app_upd_usr_id, app_ext1, app_ext2, app_ext3, ");
         sqlStr.append(" app_notify_status, app_notify_datetime, app_priority, ");
         sqlStr.append(" usr_display_bil, usr_tel_1, ");
         sqlStr.append(" run.itm_title AS r_itm_title, ");
         sqlStr.append(cwSQL.replaceNull("run.itm_eff_start_datetime", "?")).append(" AS r_itm_eff_start_datetime, ");
         sqlStr.append(cwSQL.replaceNull("run.itm_eff_end_datetime", "?")).append(" AS r_itm_eff_end_datetime, ");
         sqlStr.append(cwSQL.replaceNull("run.itm_appn_start_datetime", "?")).append(" AS r_itm_appn_start_datetime, ");
         sqlStr.append(cwSQL.replaceNull("run.itm_appn_end_datetime", "?")).append(" AS r_itm_appn_end_datetime, ");
         sqlStr.append(" parent.itm_title AS p_itm_title, ");
         sqlStr.append(" parent.itm_type AS p_itm_type, ");
         sqlStr.append(" parent.itm_apply_method AS p_itm_apply_method, ste_name, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id ");
         sqlStr.append("FROM");
         sqlStr.append(" aeApplication");
         sqlStr.append(" inner join aeItem run on (run.itm_id = app_itm_id)");
         sqlStr.append(" inner join RegUser on (usr_ent_id = app_ent_id)");
         sqlStr.append(" inner join Entity on (ent_id = usr_ent_id)");
         sqlStr.append(" inner join acSite on (usr_ste_ent_id = ste_ent_id)");
         sqlStr.append(" inner join aeItemRelation on (run.itm_id = ire_child_itm_id)");
         sqlStr.append(" inner join aeItem parent on (parent.itm_id = ire_parent_itm_id)");
         sqlStr.append(" left join EntityRelation on (usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)");
         sqlStr.append(" left join EntityRelationHistory on (usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)");
         sqlStr.append(" WHERE run.itm_run_ind = ? ");
         // condition
         if (appIdLst != null && appIdLst.length() != 0) {
             sqlStr.append(" AND app_id IN ").append(appIdLst);
         }
         if (appIdLst == null) {
             sqlStr.append(" AND run.itm_owner_ent_id = ?");
             if (entIdLst != null && entIdLst.length() != 0) {
                 sqlStr.append(" AND usr_ent_id IN ").append(entIdLst);
             }
             if (processStatus != null && processStatus.length() > 0) {
                 sqlStr.append(" AND app_process_status = ?");
             }
             if (itmId != 0) {
                 sqlStr.append(" AND run.itm_id = ?");
             }
         }

         sqlStr.append(" UNION ");
         /**
          * get the application list of the item with non-"classroom" type
          */
         sqlStr.append("SELECT app_id, app_ent_id, app_itm_id, app_status, app_process_status, ");
         sqlStr.append(" app_create_timestamp, app_create_usr_id, ");
         sqlStr.append(" app_upd_timestamp, app_upd_usr_id, app_ext1, app_ext2, app_ext3, ");
         sqlStr.append(" app_notify_status, app_notify_datetime, app_priority, ");
         sqlStr.append(" usr_display_bil, usr_tel_1, ");
         sqlStr.append(sql_null_string).append(" AS r_itm_title, ");
         sqlStr.append(cwSQL.replaceNull("itm_eff_start_datetime", "?")).append(" AS r_itm_eff_start_datetime, ");
         sqlStr.append(cwSQL.replaceNull("itm_eff_end_datetime", "?")).append(" AS r_itm_eff_end_datetime, ");
         sqlStr.append(cwSQL.replaceNull("itm_appn_start_datetime", "?")).append(" AS r_itm_appn_start_datetime, ");
         sqlStr.append(cwSQL.replaceNull("itm_appn_end_datetime", "?")).append(" AS r_itm_appn_end_datetime, ");
         sqlStr.append(" itm_title AS p_itm_title, ");
         sqlStr.append(" itm_type AS p_itm_type, ");
         sqlStr.append(" itm_apply_method AS p_itm_apply_method, ste_name, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id ");
         sqlStr.append("FROM");
         sqlStr.append(" aeApplication");
         sqlStr.append(" inner join aeItem run on (itm_id = app_itm_id)");
         sqlStr.append(" inner join RegUser on (usr_ent_id = app_ent_id)");
         sqlStr.append(" inner join acSite on (usr_ste_ent_id = ste_ent_id)");
         sqlStr.append(" left join EntityRelation on (usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)");
         sqlStr.append(" left join EntityRelationHistory on (usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)");
         sqlStr.append(" WHERE itm_run_ind = ?");
         // condition
         if (appIdLst != null && appIdLst.length() != 0) {
             sqlStr.append(" AND app_id IN ").append(appIdLst);
         }
         if (appIdLst == null) {
             sqlStr.append(" AND itm_owner_ent_id = ?");
             if (entIdLst != null && entIdLst.length() != 0) {
                 sqlStr.append(" AND usr_ent_id IN ").append(entIdLst);
             }
             if (processStatus != null && processStatus.length() > 0) {
                 sqlStr.append(" AND app_process_status = ?");
             }
             if (itmId != 0) {
                 sqlStr.append(" AND itm_id = ?");
             }
         }

         return sqlStr.toString();
     }

    public static String getApplicationListForEnroll(Connection con,
                                            String appIdLst,
                                            String processStatus,
                                            long itmId,
                                            String entIdLst,
                                            boolean isCount,
                                            String select_cols) throws SQLException {
        StringBuffer sqlStr = new StringBuffer(1024);
        if (isCount) {
            sqlStr.append(" select count(*) as ero_count ");
        } else {
            sqlStr.append("select distinct app_id, app_status, app_priority, ");
            sqlStr.append(" usr_ent_id, usr_ste_usr_id login_id, ste_name, usr_display_bil usr_name ");
            sqlStr.append(select_cols);
        }
        sqlStr.append(" from aeApplication inner join aeItem on (app_itm_id = itm_id and itm_id = ? and itm_owner_ent_id = ?)");
        sqlStr.append(" inner join RegUser on (app_ent_id = usr_ent_id)");
        sqlStr.append(" inner join Entity on (ent_id = usr_ent_id)");
        sqlStr.append(" inner join acSite on (usr_ste_ent_id = ste_ent_id)");
        sqlStr.append(" inner join RegUserExtension on (urx_usr_ent_id = usr_ent_id)");
        sqlStr.append(" left join ( ");
        sqlStr.append(" 	select distinct aal_app_id, aal_approval_role from aeAppnApprovalList where aal_status = ? ");
        sqlStr.append(" ) t on (t.aal_app_id = app_id) ");
        sqlStr.append(" where 1 = 1 ");
        
        if (appIdLst != null && appIdLst.length() != 0) {
            sqlStr.append(" AND app_id IN ").append(appIdLst);
        }
        if (appIdLst == null) {
            if (entIdLst != null && entIdLst.length() != 0) {
                sqlStr.append(" AND usr_ent_id IN ").append(entIdLst);
            }
            if (processStatus != null && processStatus.length() > 0) {
            	if(processStatus.equals("已报名") || processStatus.equals("Enrolled") || processStatus.equals("已報名")) {
            		sqlStr.append(" AND app_status ='").append(aeApplication.ADMITTED).append("'");
            	} else {
            		sqlStr.append(" AND app_process_status = ?");
            	}
            }
        }
        return sqlStr.toString();
    }
    
     //add for capture user profile as app_usr_prof_xml when the is admitted
     public static final String sql_update_app_usr_prof_xml =
             "update aeApplication set app_upd_timestamp = ? , app_upd_usr_id = ? where app_id = ?";
             
    public static String getModProgressStatSQL(StringBuffer additionalCond){
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT count(*) TOTAL_CNT, SUM(pgr_score) TOTAL_SCORE FROM Progress, RegUser ");
            sql.append(" WHERE pgr_status = ? ");
            sql.append(" AND pgr_res_id = ? ");
            if (additionalCond!=null){
                sql.append(additionalCond); 
            }
            sql.append(" AND pgr_usr_id = usr_id ");
            return sql.toString();   
        }

    public static String getModAttemptStatSQL(StringBuffer additionalCond, boolean answer_for_course, boolean answer_for_lrn_course){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT rcn_order, atm_int_res_id, atm_response_bil, count(*) R_COUNT, SUM(atm_score) SUM_SCORE "); 
        sql.append(" FROM ProgressAttempt, Progress , Question , Reguser , ResourceContent , ");
        sql.append(" EntityRelation , UserGroup , TcTrainingCenterTargetEntity , TcTrainingCenter ");
        sql.append(" WHERE pgr_status = ? ");
        sql.append(" AND pgr_res_id = ? ");
        sql.append(" AND que_type = ? ");
        if (additionalCond!=null){
            sql.append(additionalCond); 
        }
        sql.append(" AND pgr_res_id = atm_pgr_res_id ");
        sql.append(" AND pgr_attempt_nbr = atm_pgr_attempt_nbr ");
        sql.append(" AND pgr_tkh_id = atm_tkh_id ");
        sql.append(" AND pgr_usr_id = atm_pgr_usr_id ");
        sql.append(" AND atm_pgr_usr_id = usr_id ");
        sql.append(" AND atm_int_res_id = que_res_id ");
        sql.append(" AND atm_pgr_res_id = rcn_res_id ");
        sql.append(" AND atm_int_res_id = rcn_res_id_content ");
        if(!answer_for_course && answer_for_lrn_course) {
        	sql.append(" AND usr_ent_id = ern_child_ent_id ")
        		.append(" AND ern_ancestor_ent_id = usg_ent_id ")
        		.append(" AND usg_ent_id = tce_ent_id ")
        		.append(" AND tce_tcr_id = tcr_id")
        		.append(" AND tcr_id = (SELECT mod_tcr_id FROM module WHERE mod_res_id in ")
        		.append(" (SELECT mod_mod_id_root FROM module WHERE mod_res_id = ?)) ");
        }
        
        sql.append(" GROUP BY rcn_order , atm_int_res_id, atm_response_bil ");
        sql.append(" ORDER BY rcn_order ");
        
        return sql.toString();
    }
                     
        
	    public static final String  sql_get_child_mod_list_by_root_id =
	            " select mod_res_id from Module where mod_mod_id_root = ? ";
            
        public static final String sql_get_itm_by_mod_root = 
	            " SELECT mod_res_id , cos_itm_id FROM Module , ResourceContent , Course " 
	            + " WHERE  mod_res_id = rcn_res_id_content "
	            + " AND rcn_res_id = cos_res_id ";

        public static final String sql_get_confirmed_application_timestamp = 
                "select app_create_timestamp, itm_eff_start_datetime, itm_run_ind from aeApplication,aeItemRelation,aeItem where app_itm_id = itm_id and ((app_itm_id = ire_child_itm_id and ire_parent_itm_id = ?) or app_itm_id = ?) and app_status = ? and app_ent_id = ? order by app_create_timestamp desc";

	    public static final String SQL_GET_CURRENT_STEP_APPROVER_TYPE 
	        = " select distinct(aal_approval_role) from aeAppnApprovalList "
	        + " where aal_app_id = ? "
            + " and aal_create_timestamp = "
            + "     (select max(aal_create_timestamp) "
            + "      from aeAppnApprovalList "
            + "      where aal_app_id = ?)";

        public static final String SQL_INS_APPN_APPROVAL
            = " insert into aeAppnApprovalList "
            + " (aal_usr_ent_id, aal_app_id, aal_app_ent_id "
            + " ,aal_approval_role, aal_approval_usg_ent_id "
            + " ,aal_status, aal_create_timestamp "
            + " ,aal_action_taker_usr_ent_id, aal_action_taker_approval_role "
            + " ,aal_action_taken, aal_action_timestamp) "
            + " values "
            + " (?, ?, ? "
            + " ,?, ? "
            + " ,?, ? "
            + " ,?, ? "
            + " ,?, ?) ";

        public static final String SQL_MAKE_APP_APPROVAL_ACTION 
            = " update aeAppnApprovalList "
            + " set aal_status = ? "
            + " , aal_aah_id = ? "
            + " , aal_action_taker_usr_ent_id = ? "
            + " , aal_action_taker_approval_role = ? "
            + " , aal_action_taken = ? "
            + " , aal_action_timestamp = ? "
            + " where aal_status = ? "
            + " and aal_app_id = ? ";

	    public static final String SQL_GET_CURRENT_APPROVAL_ROLE 
            = " select aal_approval_role "
            + " from aeAppnApprovalList "
            + " where aal_app_id = ? "
            + " and aal_usr_ent_id = ? "
            + " and aal_status = ? ";

        public static final String SQL_GET_CURRENT_SUPERVISED_GROUP_ENT_ID 
            = " select distinct(aal_approval_usg_ent_id) "
            + " from aeAppnApprovalList "
            + " where aal_app_id = ? "
            + " and aal_status = ? "
            + " and aal_approval_role = ? ";

	    public static final String SQL_DEL_APPN_APPROVAL_LIST
	        = " Delete from aeAppnApprovalList "
	        + " Where aal_app_id = ? ";
    	
	    public static final String SQL_IS_CURRENT_APPROVER 
	        = " Select aal_id From aeAppnApprovalList "
	        + " Where aal_app_id = ? "
	        + " And aal_usr_ent_id = ? "
	        + " And aal_status = ? ";

	public static String getPendingApprovalListSQL(Connection con,String acRole) throws SQLException {
		StringBuffer SQL_APP_PENDING_APPROVAL_LIST = new StringBuffer();
		SQL_APP_PENDING_APPROVAL_LIST.append("SELECT aal_usr_ent_id, aal_approval_role, itm_id, itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, 0 as run_itm_id, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as run_itm_title ");
		SQL_APP_PENDING_APPROVAL_LIST.append(" FROM aeAppnApprovalList");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join reguser on (usr_ent_id = aal_app_ent_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join entity on (ent_id = usr_ent_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join aeApplication on (app_id = aal_app_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join aeItem on (app_itm_id = itm_id AND itm_run_ind = ?) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" left join EntityRelation on (usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)");
        SQL_APP_PENDING_APPROVAL_LIST.append(" left join EntityRelationHistory on (usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)");
        SQL_APP_PENDING_APPROVAL_LIST.append(" WHERE aal_status = ? and ");
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){ //讲师 角色
        	SQL_APP_PENDING_APPROVAL_LIST.append(" itm_id in (select iac_itm_id from aeItemAccess where iac_access_type = 'ROLE' and iac_access_id = ? and iac_ent_id = ? ) ");
        }else{
        	SQL_APP_PENDING_APPROVAL_LIST.append(" aal_usr_ent_id = ? ");
        }
        SQL_APP_PENDING_APPROVAL_LIST.append(" UNION ");
        SQL_APP_PENDING_APPROVAL_LIST.append("SELECT aal_usr_ent_id, aal_approval_role, course.itm_id, course.itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, run.itm_id as run_itm_id, run.itm_title as run_itm_title ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" FROM aeAppnApprovalList ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join reguser on (usr_ent_id = aal_app_ent_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join entity on (ent_id = usr_ent_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join aeApplication on (app_id = aal_app_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join aeItem run on (app_itm_id = run.itm_id AND run.itm_run_ind = ?) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join aeItemRelation on (run.itm_id = ire_child_itm_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" inner join aeItem course on (course.itm_id = ire_parent_itm_id) ");
        SQL_APP_PENDING_APPROVAL_LIST.append(" left join EntityRelation on (usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)");
        SQL_APP_PENDING_APPROVAL_LIST.append(" left join EntityRelationHistory on (usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)");
        SQL_APP_PENDING_APPROVAL_LIST.append(" WHERE aal_status = ?  and ");
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){ //讲师 角色
        	SQL_APP_PENDING_APPROVAL_LIST.append(" course.itm_id in (select iac_itm_id from aeItemAccess where iac_access_type = 'ROLE' and iac_access_id = ? and iac_ent_id = ? ) ");
        }else{
        	SQL_APP_PENDING_APPROVAL_LIST.append(" aal_usr_ent_id = ? ");
        }
		/*String SQL_APP_PENDING_APPROVAL_LIST = "SELECT aal_usr_ent_id, aal_approval_role, itm_id, itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, 0 as run_itm_id, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as run_itm_title "
            + " FROM aeAppnApprovalList"
            + " inner join reguser on (usr_ent_id = aal_app_ent_id) "
            + " inner join entity on (ent_id = usr_ent_id) "
            + " inner join aeApplication on (app_id = aal_app_id) "
            + " inner join aeItem on (app_itm_id = itm_id AND itm_run_ind = ?) "
            + " left join EntityRelation on (usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)"
            + " left join EntityRelationHistory on (usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)"
            + " WHERE aal_usr_ent_id = ? and aal_status = ? "
            + " UNION "
            + "SELECT aal_usr_ent_id, aal_approval_role, course.itm_id, course.itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, run.itm_id as run_itm_id, run.itm_title as run_itm_title "
            + " FROM aeAppnApprovalList "
            + " inner join reguser on (usr_ent_id = aal_app_ent_id) "
            + " inner join entity on (ent_id = usr_ent_id) "
            + " inner join aeApplication on (app_id = aal_app_id) "
            + " inner join aeItem run on (app_itm_id = run.itm_id AND run.itm_run_ind = ?) "
            + " inner join aeItemRelation on (run.itm_id = ire_child_itm_id) "
            + " inner join aeItem course on (course.itm_id = ire_parent_itm_id) "
            + " left join EntityRelation on (usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)"
            + " left join EntityRelationHistory on (usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)"
            + " WHERE aal_usr_ent_id = ? and aal_status = ? ";*/
		return SQL_APP_PENDING_APPROVAL_LIST.toString();
	}
            
            
	public static String getApprovalHistoryListSQL(Connection con, String acRole) throws SQLException {
		StringBuffer SQL_APP_APPROVAL_HISTORY_LIST = new StringBuffer();
		SQL_APP_APPROVAL_HISTORY_LIST.append("SELECT aal_usr_ent_id, aal_approval_role, itm_id, itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, APPT.usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, 0 as run_itm_id, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as run_itm_title ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" , aal_action_taken, aal_action_taker_usr_ent_id as actr_ent_id, ACTR.usr_display_bil as actr_display_bil, aal_action_taker_approval_role as actr_role, aal_action_timestamp ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" FROM aeAppnApprovalList");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join reguser APPT on (APPT.usr_ent_id = aal_app_ent_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join entity on (ent_id = APPT.usr_ent_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join aeApplication on (app_id = aal_app_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join aeItem on (app_itm_id = itm_id AND itm_run_ind = ?) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join reguser ACTR on (ACTR.usr_ent_id = aal_action_taker_usr_ent_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" left join EntityRelation on (APPT.usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" left join EntityRelationHistory on (APPT.usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" WHERE aal_status = ? and ");
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){ //讲师 角色
        	SQL_APP_APPROVAL_HISTORY_LIST.append(" itm_id in (select iac_itm_id from aeItemAccess where iac_access_type = 'ROLE' and iac_access_id = ? and iac_ent_id = ? ) ");
        }else{
        	SQL_APP_APPROVAL_HISTORY_LIST.append(" aal_usr_ent_id = ? ");
        }
        SQL_APP_APPROVAL_HISTORY_LIST.append(" Union ");
        SQL_APP_APPROVAL_HISTORY_LIST.append("SELECT aal_usr_ent_id, aal_approval_role, course.itm_id, course.itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, APPT.usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, run.itm_id as run_itm_id, run.itm_title as run_itm_title ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" , aal_action_taken, aal_action_taker_usr_ent_id as actr_ent_id, ACTR.usr_display_bil as actr_display_bil, aal_action_taker_approval_role as actr_role, aal_action_timestamp ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" FROM aeAppnApprovalList");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join reguser APPT on (APPT.usr_ent_id = aal_app_ent_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join entity on (ent_id = APPT.usr_ent_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join aeApplication on (app_id = aal_app_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join aeItem run on (app_itm_id = run.itm_id AND run.itm_run_ind = ?) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join aeItemRelation on (run.itm_id = ire_child_itm_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join aeItem course on (course.itm_id = ire_parent_itm_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" inner join reguser ACTR on (ACTR.usr_ent_id = aal_action_taker_usr_ent_id) ");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" left join EntityRelation on (APPT.usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" left join EntityRelationHistory on (APPT.usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)");
        SQL_APP_APPROVAL_HISTORY_LIST.append(" WHERE aal_status = ?  and ");
        if(null != acRole && acRole.equals(AcRole.ROLE_INSTR_1)){ //讲师 角色
        	SQL_APP_APPROVAL_HISTORY_LIST.append(" course.itm_id in (select iac_itm_id from aeItemAccess where iac_access_type = 'ROLE' and iac_access_id = ? and iac_ent_id = ? ) ");
        }else{
        	SQL_APP_APPROVAL_HISTORY_LIST.append(" aal_usr_ent_id = ? ");
        }
        /*String SQL_APP_APPROVAL_HISTORY_LIST
		= "SELECT aal_usr_ent_id, aal_approval_role, itm_id, itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, APPT.usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, 0 as run_itm_id, " + cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING) + " as run_itm_title "
        + " , aal_action_taken, aal_action_taker_usr_ent_id as actr_ent_id, ACTR.usr_display_bil as actr_display_bil, aal_action_taker_approval_role as actr_role, aal_action_timestamp "
        + " FROM aeAppnApprovalList"
        + " inner join reguser APPT on (APPT.usr_ent_id = aal_app_ent_id) "
        + " inner join entity on (ent_id = APPT.usr_ent_id) "
        + " inner join aeApplication on (app_id = aal_app_id) "
        + " inner join aeItem on (app_itm_id = itm_id AND itm_run_ind = ?) "
        + " inner join reguser ACTR on (ACTR.usr_ent_id = aal_action_taker_usr_ent_id) "
        + " left join EntityRelation on (APPT.usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?) "
        + " left join EntityRelationHistory on (APPT.usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp) "
        + " WHERE aal_usr_ent_id = ? and aal_status = ? "
        + " Union "
        + "SELECT aal_usr_ent_id, aal_approval_role, course.itm_id, course.itm_title, app_id, app_ent_id, app_process_status , app_upd_timestamp, APPT.usr_display_bil as appt_display_bil, ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, aal_create_timestamp, run.itm_id as run_itm_id, run.itm_title as run_itm_title "
        + " , aal_action_taken, aal_action_taker_usr_ent_id as actr_ent_id, ACTR.usr_display_bil as actr_display_bil, aal_action_taker_approval_role as actr_role, aal_action_timestamp "
        + " FROM aeAppnApprovalList"
        + " inner join reguser APPT on (APPT.usr_ent_id = aal_app_ent_id) "
        + " inner join entity on (ent_id = APPT.usr_ent_id) "
        + " inner join aeApplication on (app_id = aal_app_id) "
        + " inner join aeItem run on (app_itm_id = run.itm_id AND run.itm_run_ind = ?) "
        + " inner join aeItemRelation on (run.itm_id = ire_child_itm_id) "
        + " inner join aeItem course on (course.itm_id = ire_parent_itm_id) "
        + " inner join reguser ACTR on (ACTR.usr_ent_id = aal_action_taker_usr_ent_id) "
        + " left join EntityRelation on (APPT.usr_ent_id = ern_child_ent_id AND ern_type = ? AND ern_parent_ind = ?)"
        + " left join EntityRelationHistory on (APPT.usr_ent_id = erh_child_ent_id AND erh_type = ? AND erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)"
        + " WHERE aal_usr_ent_id = ? and aal_status = ? ";*/
		return SQL_APP_APPROVAL_HISTORY_LIST.toString();
	}
 
        public static final String SQL_HAS_APPROVAL_LIST
            = "SELECT COUNT(*) AS cnt FROM aeAppnApprovalList WHERE aal_usr_ent_id = ? ";

        public static final String SQL_HAS_PENDING_APPN
            = "SELECT COUNT(*) AS cnt FROM aeAppnApprovalList WHERE aal_app_ent_id = ? AND aal_status = ? ";

        public static final String SQL_IS_PREVIOUS_APPROVER = "SELECT count(*) from aeAppnApprovalList where aal_app_id = ? and aal_action_taker_usr_ent_id = ? and aal_status = ? and aal_action_taken = ? ";  
        
        
		/*
		 * Get the (direct) supervisors that have pending approvals for
		 * a source user (app_ent_id=?)  
		 */
		public static final String SQL_PENDING_APPROVAL_DIRECT_SUP
			= "SELECT distinct(aal_usr_ent_id) FROM aeAppnApprovalList "
			+ "WHERE aal_app_ent_id = ? AND "
			+ "aal_approval_role = ? AND "
			+ "aal_status = ?"; 
				
    
        public static final String SQL_HAS_PENDING_APPROVAL_APPN
            = "SELECT COUNT(*) AS cnt FROM aeAppnApprovalList WHERE aal_usr_ent_id = ? AND aal_status = ? ";

		/*
		 * Get the source user groups with approvals with status=? for 
		 * a particular user (usr_ent_id=?)
		 */ 
		public static final String SQL_PENDING_APPROVAL_USER_GROUPS
			= "SELECT distinct(aal_approval_usg_ent_id) FROM aeAppnApprovalList "
			+ "WHERE aal_usr_ent_id = ? AND "
			+ "aal_approval_role = ? AND "
			+ "aal_status = ?";

	    public static final String SQL_GET_CURRENT_APPROVERS
	        = " select aal_usr_ent_id "
            + " from aeAppnApprovalList "
            + " where aal_app_id = ? "
            + " and aal_status = ? ";

        public static final String SQL_GET_PREVIOUS_APPROVERS 
            = " select aal_usr_ent_id "
            + " from aeAppnApprovalList a "
            + " where a.aal_app_id = ? "
            + " and a.aal_status = ? "
            + " and a.aal_create_timestamp = "
            + " (select max(b.aal_create_timestamp) "
            + " from aeAppnApprovalList b "
            + " where b.aal_app_id = ? "
            + " and b.aal_status = ? ) ";

        public static final String SQL_GET_PARTICIPATED_APPROVERS
            = " select distinct(aal_action_taker_usr_ent_id) " 
            + " from aeAppnApprovalList "
            + " where aal_app_id = ? "
            + " and aal_status = ? "
            + " and aal_action_taker_approval_role <> ? ";

        public static final String SQL_GET_PARTICIPATED_ROLE_APPROVERS
            = " select distinct(aal_action_taker_usr_ent_id) " 
            + " from aeAppnApprovalList "
            + " where aal_app_id = ? "
            + " and aal_status = ? "
            + " and aal_action_taker_approval_role = ? ";

        public static final String SQL_GET_APPN_PENDING_APPROVAL_ROLE
            = " select distinct(aal_approval_role) "
            + " from aeAppnApprovalList "
            + " where aal_status = ? "
            + " and aal_app_id = ? ";

        public static final String SQL_GET_ACTN_APPROVAL_ROLE
            = " select distinct(aal_action_taker_approval_role) "
            + " from aeAppnApprovalList "
            + " where aal_aah_id = ? ";
        
        public static String getCosStructureXml() {
        	return "select cos_res_id, cos_structure_xml from course where cos_structure_xml is not null";
        }
        
        public static String getTreeNodeRelation() {
        	return "select tnd_id, tnd_parent_tnd_id from aeTreeNode where tnd_type = ? order by tnd_id asc";
        }
        
        public static String getItemIdAndXmlList() {
        	return "select itm_id, itm_xml from aeItem where itm_xml is not null";
        }
        
        public static String updateCovProgress() {
        	return "update CourseEvaluation set cov_progress = ? where cov_cos_id = ? and cov_ent_id = ? and cov_tkh_id = ?";
        }
}