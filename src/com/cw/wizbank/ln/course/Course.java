package com.cw.wizbank.ln.course;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.ae.aeAction;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.view.ViewItemTemplate;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class Course {
	public static String getCourseXml(Connection con) throws SQLException {
		Vector<TCBean> vec = ViewTrainingCenter.getTopLevelTrainingCenterList(con);

		StringBuffer result = new StringBuffer("");
		result.append("<tc_list>");
		if (vec != null && vec.size() > 0) {
			for (TCBean tc : vec) {
				Long index = 0l;
				result.append("<training_center index=\""+index+"\" id=\"" + tc.getTcr_id() + "\" code=\"" + cwUtils.esc4XML(tc.getTcr_code()) + "\" name=\"" + cwUtils.esc4XML(tc.getTcr_title()) + "\">");
				recursionTrainingCenter(con, tc.getTcr_id(), result, ++index );	//递归入口
				result.append("</training_center>");
			}
		}
		result.append("</tc_list>");
		
		//System.out.print(result.toString());
		return result.toString();
	}
	
	/**
	 * 递归获取所有培训中心  by leon.li
	 * @param con
	 * @param tc_id
	 * @param result
	 * @throws SQLException
	 */
	public static String recursionTrainingCenter(Connection con, long tc_id, StringBuffer result ,Long index) throws SQLException {
		
		Vector<TCBean> tclist = ViewTrainingCenter.getSubLevelTrainingCenterList(con, tc_id);
		
		if (tclist != null && tclist.size() > 0) {
			int count = 0;
			for (TCBean tc : tclist) {
				result.append("<training_center index=\""+index+"\"  id=\"" + tc.getTcr_id() + "\" code=\"" + cwUtils.esc4XML(tc.getTcr_code()) + "\" name=\"" + cwUtils.esc4XML(tc.getTcr_title()) + "\">");
				if(tclist.size() == ++count) {
					++index;
				}
				recursionTrainingCenter(con, tc.getTcr_id(), result, index);
				result.append("</training_center>");
			}
		}
		return result.toString();
	}

	public static boolean copyCourse(Connection con, WizbiniLoader wizbini, loginProfile prof, long[] itm_id_lsts, long[] tc_id_lsts) throws SQLException, cwSysMessage, qdbException, cwException {
		if (itm_id_lsts == null || itm_id_lsts.length == 0) {
			throw new cwSysMessage("LN050");
		}
		if (tc_id_lsts == null || tc_id_lsts.length == 0) {
			throw new cwSysMessage("LN049");
		}

		for (long itm_id : itm_id_lsts) {
			// 源课程的信息
			aeItem fromItm = new aeItem();
			fromItm.itm_id = itm_id;
			fromItm.get(con);

			// 课程模板
			ViewItemTemplate viItmTpl = new ViewItemTemplate();
			viItmTpl.ownerEntId = prof.root_ent_id;
			viItmTpl.itemType = fromItm.itm_type;
			viItmTpl.runInd = fromItm.itm_run_ind;
			viItmTpl.sessionInd = fromItm.itm_session_ind;
			String[] templateTypes = viItmTpl.getItemTypeTemplates(con);
			long[] tpl_ids = aeAction.getTplIds(con, null, fromItm.itm_type, templateTypes, fromItm.itm_session_ind, prof.root_ent_id);

			for (long tcr_id : tc_id_lsts) {
				// 查询新培训中心培训管理员
				String[] iac_id_lst = getTrainingCenterOfficer(con, tcr_id);

				// 如果目标培训中心没有培训管理员，报错，不给指派
				if (iac_id_lst == null || iac_id_lst.length == 0) {
					throw new cwSysMessage("LN054");
				}

				// 培训中心管理员
				dbRegUser tcAdmin = getTrainingCenterOfficerByTcrId(con, tcr_id);
				loginProfile tcProf = new loginProfile();
				tcProf.usr_display_bil = tcAdmin.usr_display_bil;
				tcProf.usr_id = tcAdmin.usr_id;
				tcProf.usr_ent_id = tcAdmin.usr_ent_id;
				tcProf.root_ent_id = tcAdmin.usr_ste_ent_id;
				tcProf.label_lan = prof.label_lan;
				tcProf.current_role = prof.current_role;

				DbTrainingCenter tc = DbTrainingCenter.getInstance(con, tcr_id);
				CommonLog.info("Copy Course [" + fromItm.itm_title + "] to TrainingCenter[" + tc.tcr_title + "]");

				if (isCopied(con, itm_id, tcr_id)) {
					CommonLog.info("Course [itm_id=" + itm_id + "] have bean copied to TrainingCenter[" + tc.tcr_title + "]. skip.");
					continue;
				}

				Vector<Object> vColName = new Vector<Object>();
				Vector<Object> vColType = new Vector<Object>();
				Vector<Object> vColValue = new Vector<Object>();

				Vector<Object> vExtensionColName = new Vector<Object>();
				Vector<Object> vExtensionColType = new Vector<Object>();
				Vector<Object> vExtensionColValue = new Vector<Object>();

				Vector<Object> vClobColName = new Vector<Object>();
				Vector<Object> vClobColValue = new Vector<Object>();

				aeItem itm = getItem(con, fromItm, vColName, vColType, vColValue, vExtensionColName, vExtensionColType, vExtensionColValue, vClobColName, vClobColValue);

				itm.itm_tcr_id = tcr_id;
				addColumnAndValueToVector(vColName, vColType, vColValue, "itm_tcr_id", DbTable.COL_TYPE_LONG, Long.valueOf(itm.itm_tcr_id));

				dbCourse cos = new dbCourse();
				cos.res_lan = tcProf.label_lan;
				cos.res_title = fromItm.itm_title;
				cos.res_usr_id_owner = tcProf.usr_id;
				cos.res_upd_user = tcProf.usr_id;

				itm.insWZBCourse(con, tcProf, cos, 0, templateTypes, tpl_ids, vColName, vColType, vColValue, vClobColName, vClobColValue);

				// 处理课程目录
				long[] tnd_id_lst = null;
				String[] tnd_id_lst_value = null;
				Vector<Long> tndIdVec = new Vector<Long>();
				Vector<String> tndTitleVec = new Vector<String>();
				getCourseCatalog(con, prof, fromItm, tcr_id, tndIdVec, tndTitleVec);
				if (tndIdVec.size() > 0) {
					tnd_id_lst = cwUtils.vec2longArray(tndIdVec);
				}
				if (tndTitleVec.size() > 0) {
					tnd_id_lst_value = cwUtils.vec2strArray(tndTitleVec);
				}

				itm.insExternalInfo(con, tnd_id_lst, tnd_id_lst_value, null, null, null, iac_id_lst, null, null, null, null, tcProf.usr_id, tcProf.usr_ent_id, tcProf.root_ent_id, wizbini.cfgSysSetupadv.getDefaultTaId(),true);

				copyCourseContent(con, wizbini, tcProf, fromItm, itm);
				copyAeItemextension(con, fromItm.itm_id, itm.itm_id);

				CommonLog.info("Copy Course [" + fromItm.itm_title + "] to TrainingCenter[" + tc.tcr_title + "] finish.");
			}
		}
		return true;
	}

	private static void copyCourseContent(Connection con, WizbiniLoader wizbini, loginProfile prof, aeItem fromItm, aeItem targetItm) throws SQLException, cwException {
		String sql = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			// 取得当前时间
			Timestamp curTime = cwSQL.getTime(con);

			int index = 1;

			// 查询源课程中的模块信息
			String cos_structure_xml = null;
			String cos_import_xml = "";
			Timestamp cos_import_datetime = null;
			String cos_max_normal = "";
			String cos_aicc_version = null, cos_vendor = null;
			String cos_structure_json = null;
			sql = " select cos_structure_xml, cos_import_xml, cos_import_datetime, cos_max_normal, cos_aicc_version, cos_vendor, cos_structure_json from Course where cos_itm_id = ? ";
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, fromItm.itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cos_structure_xml = rs.getString("cos_structure_xml");
				cos_import_xml = rs.getString("cos_import_xml");
				cos_import_datetime = rs.getTimestamp("cos_import_datetime");
				cos_max_normal = rs.getString("cos_max_normal");
				cos_aicc_version = rs.getString("cos_aicc_version");
				cos_vendor = rs.getString("cos_vendor");
				//cos_structure_json = rs.getString("cos_structure_json");
			}

			// 查询新课程中的模块信息
			long res_id = 0;
			sql = " select cos_res_id from Course where cos_itm_id = ? ";
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, targetItm.itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				res_id = rs.getLong("cos_res_id");
			}

			// Resources
			StringBuffer resSql = new StringBuffer();
			resSql.append(" insert into Resources  ( ");
			resSql.append(" res_lan, res_title, res_type, res_subtype, res_format, res_difficulty, res_duration, res_privilege, ");
			resSql.append(" res_usr_id_owner, res_tpl_name, res_res_id_root, res_mod_res_id_test, res_status, res_create_date, res_upd_user, ");
			resSql.append(" res_upd_date, res_src_type, res_src_link, res_code, res_instructor_name, res_instructor_organization, res_annotation, ");
			resSql.append(" res_sco_version, res_vod_duration, res_img_link, res_vod_main, res_first_res_id ,");
			resSql.append(" res_long_desc, res_desc ");
			resSql.append(" )  ");
			resSql.append(" select res_lan, res_title, res_type, res_subtype, res_format, res_difficulty, res_duration, res_privilege, ");
			resSql.append(" res_usr_id_owner, res_tpl_name, res_res_id_root, ?, res_status, res_create_date, res_upd_user, ");
			resSql.append(" res_upd_date, res_src_type, res_src_link, res_code, res_instructor_name, res_instructor_organization, res_annotation, ");
			resSql.append(" res_sco_version, res_vod_duration, res_img_link, res_vod_main, res_first_res_id, ");
			resSql.append(" res_long_desc, res_desc from Resources where res_id = ? ");

			// Module
			StringBuffer modSql = new StringBuffer();
			modSql.append("insert into Module (mod_res_id, mod_type, mod_max_score, mod_pass_score, mod_instruct, mod_max_attempt, mod_max_usr_attempt, ");
			modSql.append(" mod_score_ind, mod_score_reset, mod_logic, mod_eff_start_datetime, mod_eff_end_datetime, mod_usr_id_instructor,  ");
			modSql.append(" mod_core_vendor, mod_password, mod_import_xml, mod_import_datetime, mod_time_limit_action,  ");
			modSql.append(" mod_web_launch, mod_vendor, mod_aicc_version, mod_has_rate_q, mod_is_public, mod_public_need_enrol, ");
			modSql.append(" mod_mod_id_root, mod_show_answer_ind, mod_sub_after_passed_ind, mod_mod_res_id_parent, mod_auto_save_ind,mod_download_ind,mod_copy_ind,mod_mobile_ind,mod_test_style ,mod_required_time");
			modSql.append(" ) ");
			modSql.append(" select ?, mod_type, mod_max_score, mod_pass_score, mod_instruct, mod_max_attempt, mod_max_usr_attempt, ");
			modSql.append(" mod_score_ind, mod_score_reset, mod_logic, mod_eff_start_datetime, mod_eff_end_datetime, mod_usr_id_instructor, ");
			modSql.append(" mod_core_vendor, mod_password, mod_import_xml, mod_import_datetime, mod_time_limit_action,  ");
			modSql.append(" mod_web_launch, mod_vendor, mod_aicc_version, mod_has_rate_q, mod_is_public, mod_public_need_enrol, ");
			modSql.append(" mod_mod_id_root, mod_show_answer_ind, mod_sub_after_passed_ind, mod_mod_res_id_parent, mod_auto_save_ind,mod_download_ind, 1, mod_mobile_ind ,mod_test_style,mod_required_time");
			modSql.append(" from Module ");
			modSql.append(" where mod_res_id = ?");

			// ResourceContent
			StringBuffer rcnSql = new StringBuffer();
			rcnSql.append(" insert into ResourceContent (rcn_res_id, rcn_sub_nbr, rcn_desc, rcn_order, rcn_res_id_content, rcn_obj_id_content, ");
			rcnSql.append(" rcn_score_multiplier, rcn_rcn_res_id_parent, rcn_rcn_sub_nbr_parent, rcn_tkh_id) ");
			rcnSql.append(" select ?, rcn_sub_nbr, rcn_desc, rcn_order, ?, rcn_obj_id_content, ");
			rcnSql.append(" rcn_score_multiplier, rcn_rcn_res_id_parent, rcn_rcn_sub_nbr_parent, rcn_tkh_id ");
			rcnSql.append(" from ResourceContent where rcn_res_id in (select distinct cos_res_id from Course where cos_itm_id = ?) and rcn_res_id_content = ?  and rcn_tkh_id = -1 ");

			// assignment
			StringBuffer assSql = new StringBuffer();
			assSql.append(" insert into assignment ( ");
			assSql.append(" ass_res_id, ass_max_upload, ass_email, ass_notify_ind, ass_due_datetime, ass_files_desc_xml, ass_due_date_day, ass_submission)  ");
			assSql.append(" select ?, ass_max_upload, ass_email, ass_notify_ind, ass_due_datetime, ass_files_desc_xml, ass_due_date_day, ass_submission  ");
			assSql.append(" from assignment where ass_res_id = ?");

			// ctReference
			StringBuffer ctrSql = new StringBuffer();
			ctrSql.append(" insert into ctReference (ref_res_id, ref_type, ref_title, ref_description, ref_url, ref_create_usr_id, ");
			ctrSql.append(" ref_create_timestamp, ref_update_usr_id, ref_update_timestamp)  ");
			ctrSql.append(" select ?, ref_type, ref_title, ref_description, ref_url, ref_create_usr_id,  ");
			ctrSql.append(" ref_create_timestamp, ref_update_usr_id, ref_update_timestamp from ctReference where ref_res_id = ? ");

			// ctGlossary
			StringBuffer ctgSql = new StringBuffer();
			ctgSql.append(" INSERT INTO ctGlossary(glo_res_id, glo_keyword, glo_definition, glo_create_usr_id, glo_create_timestamp,  ");
			ctgSql.append(" glo_update_usr_id, glo_update_timestamp)  ");
			ctgSql.append(" select ?, glo_keyword, glo_definition, glo_create_usr_id, glo_create_timestamp, glo_update_usr_id, glo_update_timestamp  ");
			ctgSql.append(" from ctGlossary ");
			ctgSql.append(" where glo_res_id = ? ");

			// forumtopic
			StringBuffer ftoSql = new StringBuffer();
			ftoSql.append(" insert into forumtopic (fto_res_id, fto_title, fto_usr_id, fto_create_datetime, fto_last_post_datetime) ");
			ftoSql.append(" select ?, fto_title, fto_usr_id, fto_create_datetime, null  ");
			ftoSql.append(" from forumtopic where fto_res_id = ? ");

			// ResourcePermission
			StringBuffer rpmSql = new StringBuffer();
			rpmSql.append(" insert into ResourcePermission ( ");
			rpmSql.append(" rpm_res_id, rpm_ent_id, rpm_rol_ext_id, rpm_read, rpm_write, rpm_execute) ");
			rpmSql.append(" SELECT ?, ?, rpm_rol_ext_id, rpm_read, rpm_write, rpm_execute  ");
			rpmSql.append(" FROM ResourcePermission where rpm_res_id = ? ");

			// modulespec
			StringBuffer mspSql = new StringBuffer();
			mspSql.append(" insert into modulespec (msp_res_id, msp_obj_id, msp_type, msp_score, msp_difficulty, msp_privilege, ");
			mspSql.append(" msp_duration, msp_qcount, msp_algorithm ) ");
			mspSql.append(" select ?, msp_obj_id, msp_type, msp_score, msp_difficulty, msp_privilege,  ");
			mspSql.append(" msp_duration, msp_qcount, msp_algorithm  ");
			mspSql.append(" from modulespec where msp_res_id = ? ");

			// ResourceContent
			StringBuffer rcSql = new StringBuffer();
			rcSql.append(" insert into ResourceContent (rcn_res_id, rcn_sub_nbr, rcn_desc, rcn_order, rcn_res_id_content, rcn_obj_id_content, ");
			rcSql.append(" rcn_score_multiplier, rcn_rcn_res_id_parent, rcn_rcn_sub_nbr_parent, rcn_tkh_id) ");
			rcSql.append(" select ?, rcn_sub_nbr, rcn_desc, rcn_order, ?, rcn_obj_id_content, ");
			rcSql.append(" rcn_score_multiplier, rcn_rcn_res_id_parent, rcn_rcn_sub_nbr_parent, rcn_tkh_id ");
			rcSql.append(" from ResourceContent where rcn_res_id = ? and rcn_res_id_content = ?  and rcn_tkh_id = -1 ");

			// RCQ
			String rcqSql = " select rcn_res_id, rcn_res_id_content from ResourceContent where rcn_res_id = ? and rcn_tkh_id = -1 ";

			StringBuffer queSql = new StringBuffer();
			queSql.append("insert into question (que_res_id, que_xml, que_score, que_type, que_int_count, que_prog_lang, que_media_ind, que_submit_file_ind) ");
			queSql.append(" select ?, que_xml, que_score, que_type, que_int_count, que_prog_lang, que_media_ind, que_submit_file_ind ");
			queSql.append(" from question where que_res_id = ? ");

			// INI
			StringBuffer iniSql = new StringBuffer();
			iniSql.append(" insert into Interaction (int_res_id, int_order, int_label, int_xml_outcome, int_xml_explain, int_res_id_explain, int_res_id_refer) ");
			iniSql.append(" select ?, int_order, int_label, int_xml_outcome, int_xml_explain, int_res_id_explain, int_res_id_refer ");
			iniSql.append(" from Interaction where int_res_id = ? ");

			// ROB
			String robSql = " select rob_res_id, rob_obj_id from ResourceObjective, resources where res_id = rob_res_id and res_subtype not in ('TST') and rob_res_id = ? ";

			String insRobSql = " insert into ResourceObjective (rob_res_id, rob_obj_id) values (?, ?) ";

			StringBuffer objSql = new StringBuffer();
			objSql.append(" insert into Objective ( ");
			objSql.append(" obj_id, obj_syl_id, obj_type, obj_desc, obj_obj_id_parent, obj_title, obj_developer_id, ");
			objSql.append(" obj_import_xml, obj_ancester, obj_status, obj_tcr_id ");
			objSql.append(" ) ");
			objSql.append(" select ?, obj_syl_id, obj_type, obj_desc, obj_obj_id_parent, obj_title, obj_developer_id, obj_import_xml, obj_ancester, ");
			objSql.append(" obj_status, obj_tcr_id from Objective where obj_id = ? ");

			StringBuffer robSql2 = new StringBuffer();
			robSql2.append(" insert into ResourceObjective (rob_res_id, rob_obj_id) ");
			robSql2.append(" select ?, rob_obj_id from ResourceObjective, resources ");
			robSql2.append(" where res_id = rob_res_id and res_subtype in ('TST') and rob_res_id = ? ");

			StringBuffer robSql3 = new StringBuffer();
			robSql3.append(" insert into ResourceObjective (rob_res_id, rob_obj_id) ");
			robSql3.append(" select ?, rob_obj_id from ResourceObjective, resources ");
			robSql3.append(" where res_id = rob_res_id and rob_res_id = ? ");

			// ResourceContent
			StringBuffer rcnSubSql = new StringBuffer();
			rcnSubSql.append(" insert into ResourceContent (rcn_res_id, rcn_sub_nbr, rcn_desc, rcn_order, rcn_res_id_content, rcn_obj_id_content, ");
			rcnSubSql.append(" rcn_score_multiplier, rcn_rcn_res_id_parent, rcn_rcn_sub_nbr_parent, rcn_tkh_id) ");
			rcnSubSql.append(" select ?, rcn_sub_nbr, rcn_desc, rcn_order, ?, rcn_obj_id_content, ");
			rcnSubSql.append(" rcn_score_multiplier, rcn_rcn_res_id_parent, rcn_rcn_sub_nbr_parent, rcn_tkh_id ");
			rcnSubSql.append(" from ResourceContent where rcn_res_id = ?  and rcn_tkh_id = -1 ");

			StringBuffer QueContainerSql = new StringBuffer();
			QueContainerSql.append(" insert into QueContainer (qct_res_id, qct_select_logic, qct_allow_shuffle_ind) ");
			QueContainerSql.append(" select ?, qct_select_logic, qct_allow_shuffle_ind ");
			QueContainerSql.append(" from QueContainer where qct_res_id = ? ");

			StringBuffer QueContainerSpecSql = new StringBuffer();
			QueContainerSpecSql.append(" insert into QueContainerSpec ( ");
			QueContainerSpecSql.append(" qcs_res_id, qcs_obj_id, qcs_type, qcs_score, qcs_difficulty, qcs_privilege, qcs_duration, ");
			QueContainerSpecSql.append(" qcs_qcount, qcs_create_timestamp, qcs_create_usr_id, qcs_update_timestamp, qcs_update_usr_id) ");
			QueContainerSpecSql.append(" SELECT ?, qcs_obj_id, qcs_type, qcs_score, qcs_difficulty, qcs_privilege, qcs_duration,  ");
			QueContainerSpecSql.append(" qcs_qcount, qcs_create_timestamp, qcs_create_usr_id, qcs_update_timestamp, qcs_update_usr_id ");
			QueContainerSpecSql.append(" FROM QueContainerSpec where qcs_res_id = ? ");

			// 因为模块信息保存在cos_structure_xml中，每个模块的ID是identifierref属性里
			// 所以复制模块的时候，先取出每个模块的ID，再复制到新的课程里面得到一个心的ID，再替换原来的ID，然后重新构造cos_structure_xml
			StringBuffer result = new StringBuffer();
			Map<Long, Long> res_id_map = new HashMap<Long, Long>();
			if (cos_structure_xml != null && !cos_structure_xml.equals("")) {
				String m = "identifierref=\"";
				while (cos_structure_xml.indexOf(m) >= 0) {
					int i = cos_structure_xml.indexOf(m);

					if (cos_structure_xml.indexOf(m) >= 0) {
						result.append(cos_structure_xml.substring(0, i + m.length()));

						String temp = cos_structure_xml.substring(i + m.length());
						int j = temp.indexOf("\"");
						if (j >= 0) {
							String id = temp.substring(0, j);

							long newResId = 0;
							long oldResId = Long.valueOf(id.trim()).longValue();

							// 复制Resource表记录，返回一个新的模块res_id
							stmt = con.prepareStatement(resSql.toString());
							stmt.setObject(1, null);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							
							cwSQL.cleanUp(null, stmt);
							newResId = cwSQL.getMaxId(con, "Resources", "res_id");

							// 复制模块所上传的文件
							String oldDir = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + oldResId;
							String newDir = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + newResId;
							copyFolder(oldDir, newDir);

							// 复制Module表记录
							stmt = con.prepareStatement(modSql.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							// 复制作业
							stmt = con.prepareStatement(assSql.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							stmt = con.prepareStatement(ctrSql.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							stmt = con.prepareStatement(ctgSql.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							// resourcecontent
							stmt = con.prepareStatement(rcnSql.toString());
							index = 1;
							stmt.setLong(index++, res_id);
							stmt.setLong(index++, newResId);
							stmt.setLong(index++, fromItm.itm_id);
							stmt.setLong(index++, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							// resource content
							stmt = con.prepareStatement(rcqSql);
							stmt.setLong(1, oldResId);
							rs = stmt.executeQuery();
							while (rs.next()) {
								long nId = 0;
								long rId = rs.getLong("rcn_res_id_content");

								stmt1 = con.prepareStatement(resSql.toString());
								stmt1.setLong(1, newResId);
								stmt1.setLong(2, rId);
								stmt1.executeUpdate();
								
								cwSQL.cleanUp(null, stmt1);
								nId = cwSQL.getMaxId(con, "Resources", "res_id");
								
								String oldDir1 = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + rId;
								String newDir1 = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + nId;
								copyFolder(oldDir1, newDir1);

								stmt1 = con.prepareStatement(modSql.toString());
								stmt1.setLong(1, nId);
								stmt1.setLong(2, rId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);

								stmt1 = con.prepareStatement(rcSql.toString());
								index = 1;
								stmt1.setLong(index++, newResId);
								stmt1.setLong(index++, nId);
								stmt1.setLong(index++, oldResId);
								stmt1.setLong(index++, rId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);

								stmt1 = con.prepareStatement(queSql.toString());
								stmt1.setLong(1, nId);
								stmt1.setLong(2, rId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);

								stmt1 = con.prepareStatement(iniSql.toString());
								stmt1.setLong(1, nId);
								stmt1.setLong(2, rId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);

								// resourcecontent
								stmt1 = con.prepareStatement(rcqSql.toString());
								stmt1.setLong(1, rId);
								rs1 = stmt1.executeQuery();
								while (rs1.next()) {
									long subNewId = 0;
									long subOldId = rs1.getLong("rcn_res_id_content");

									stmt2 = con.prepareStatement(resSql.toString());
									stmt2.setLong(1, nId);
									stmt2.setLong(2, subOldId);
									stmt2.executeUpdate();
									
									cwSQL.cleanUp(null, stmt2);
									subNewId = cwSQL.getMaxId(con, "Resources", "res_id");

									String oldDir2 = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + subOldId;
									String newDir2 = wizbini.getFileUploadResDirAbs() + cwUtils.SLASH + subNewId;
									copyFolder(oldDir2, newDir2);

									stmt2 = con.prepareStatement(modSql.toString());
									stmt2.setLong(1, subNewId);
									stmt2.setLong(2, subOldId);
									stmt2.executeUpdate();
									cwSQL.cleanUp(null, stmt2);

									stmt2 = con.prepareStatement(rcSql.toString());
									index = 1;
									stmt2.setLong(index++, nId);
									stmt2.setLong(index++, subNewId);
									stmt2.setLong(index++, rId);
									stmt2.setLong(index++, subOldId);
									stmt2.executeUpdate();
									cwSQL.cleanUp(null, stmt2);

									stmt2 = con.prepareStatement(queSql.toString());
									stmt2.setLong(1, subNewId);
									stmt2.setLong(2, subOldId);
									stmt2.executeUpdate();
									cwSQL.cleanUp(null, stmt2);

									stmt2 = con.prepareStatement(iniSql.toString());
									stmt2.setLong(1, subNewId);
									stmt2.setLong(2, subOldId);
									stmt2.executeUpdate();
									cwSQL.cleanUp(null, stmt2);

									stmt2 = con.prepareStatement(robSql3.toString());
									stmt2.setLong(1, subNewId);
									stmt2.setLong(2, subOldId);
									stmt2.executeUpdate();
									cwSQL.cleanUp(null, stmt2);
								}
								cwSQL.cleanUp(null, stmt1);

								stmt1 = con.prepareStatement(QueContainerSql.toString());
								stmt1.setLong(1, nId);
								stmt1.setLong(2, rId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);

								stmt1 = con.prepareStatement(QueContainerSpecSql.toString());
								stmt1.setLong(1, nId);
								stmt1.setLong(2, rId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);

								stmt1 = con.prepareStatement(robSql3.toString());
								stmt1.setLong(1, nId);
								stmt1.setLong(2, rId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);
							}
							cwSQL.cleanUp(null, stmt);

							stmt = con.prepareStatement(robSql2.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							stmt = con.prepareStatement(robSql);
							stmt.setLong(1, oldResId);
							rs = stmt.executeQuery();
							while (rs.next()) {
								long objId = rs.getLong("rob_obj_id");

								long max_obj_id = dbObjective.getMaxId(con) + 1;
								stmt1 = con.prepareStatement(objSql.toString());
								stmt1.setLong(1, max_obj_id);
								stmt1.setLong(2, objId);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);

								stmt1 = con.prepareStatement(insRobSql);
								stmt1.setLong(1, newResId);
								stmt1.setLong(2, max_obj_id);
								stmt1.executeUpdate();
								cwSQL.cleanUp(null, stmt1);
							}
							cwSQL.cleanUp(rs, stmt);

							// module spec
							stmt = con.prepareStatement(mspSql.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							// Forum and FAQ
							stmt = con.prepareStatement(ftoSql.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							// ResourcePermission
							stmt = con.prepareStatement(rpmSql.toString());
							stmt.setLong(1, newResId);
							stmt.setLong(2, prof.usr_ent_id);
							stmt.setLong(3, oldResId);
							stmt.executeUpdate();
							cwSQL.cleanUp(null, stmt);

							result.append(newResId);
							temp = temp.substring(j);

							res_id_map.put(new Long(oldResId), new Long(newResId));
						}
						cos_structure_xml = temp;
					}
				}
				result.append(cos_structure_xml);
			}

			StringBuffer updSql = new StringBuffer(" ");
			updSql.append(" update Course set cos_structure_xml = ?, cos_import_xml = ?, cos_import_datetime = ?, ");
			updSql.append(" cos_max_normal = ?, cos_aicc_version = ?, cos_vendor = ?, cos_structure_json = ? where cos_itm_id = ? ");
			stmt = con.prepareStatement(updSql.toString());
			index = 1;
			
		    if(result != null) {
	            cos_structure_json = qdbAction.static_env.transformXML(result.toString().replaceAll("&quot;", " "), "cos_structure_json_js.xsl",null);
	        }
			
			stmt.setString(index++, (cos_structure_xml == null || cos_structure_xml.equals("")) ? null : result.toString());
			stmt.setString(index++, cos_import_xml);
			stmt.setTimestamp(index++, cos_import_datetime);
			stmt.setString(index++, cos_max_normal);
			stmt.setString(index++, cos_aicc_version);
			stmt.setString(index++, cos_vendor);
			stmt.setString(index++, cos_structure_json);
			stmt.setLong(index++, targetItm.itm_id);
			stmt.executeUpdate();
			cwSQL.cleanUp(rs, stmt);

			/* 复制完成准则 ---------------------- */
			long ccr_id = 0;
			sql = " select ccr_id from CourseCriteria where ccr_itm_id = ? ";
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, targetItm.itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ccr_id = rs.getLong("ccr_id");
			}
			cwSQL.cleanUp(rs, stmt);

			// update CourseCriteria
			float ccr_pass_score = 0;
			float ccr_attendance_rate = 0;
			String ccr_offline_condition = "";
			sql = " select ccr_pass_score, ccr_attendance_rate, ccr_offline_condition from CourseCriteria where ccr_itm_id = ? ";
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, fromItm.itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ccr_pass_score = rs.getFloat("ccr_pass_score");
				ccr_attendance_rate = rs.getFloat("ccr_attendance_rate");
				ccr_offline_condition = rs.getString("ccr_offline_condition");
			}
			cwSQL.cleanUp(null, stmt);

			sql = " update CourseCriteria set ccr_pass_score = ?, ccr_attendance_rate = ?, ccr_offline_condition = ? where ccr_itm_id = ? ";
			stmt = con.prepareStatement(sql.toString());
			index = 1;
			stmt.setFloat(index++, ccr_pass_score);
			stmt.setFloat(index++, ccr_attendance_rate);
			stmt.setString(index++, ccr_offline_condition);
			stmt.setLong(index++, targetItm.itm_id);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			// CourseMeasurement
			sql = "";
			sql += " insert into CourseMeasurement (cmt_title, cmt_ccr_id, cmt_cmr_id, cmt_max_score, cmt_status, cmt_contri_rate, ";
			sql += " 	cmt_is_contri_by_score, cmt_create_timestamp, cmt_create_usr_id, cmt_update_timestamp, ";
			sql += " 	cmt_update_usr_id, cmt_delete_timestamp, cmt_pass_score, cmt_status_desc_option, cmt_order, cmt_cmt_id_parent, ";
			sql += " 	cmt_duration, cmt_place) ";
			sql += " select cmt_title, ?, cmt_cmr_id, cmt_max_score, cmt_status, cmt_contri_rate, cmt_is_contri_by_score, ";
			sql += " 	?, ?, ?, ?, cmt_delete_timestamp, cmt_pass_score, cmt_status_desc_option, cmt_order, cmt_cmt_id_parent,  ";
			sql += " 	cmt_duration, cmt_place ";
			sql += " from CourseMeasurement where cmt_ccr_id in (select distinct(ccr_id) from CourseCriteria where ccr_itm_id = ?) ";
			sql += " and cmt_cmr_id is null AND cmt_delete_timestamp IS NULL ";
			stmt = con.prepareStatement(sql.toString());
			index = 1;
			stmt.setLong(index++, ccr_id);
			stmt.setTimestamp(index++, curTime);
			stmt.setString(index++, prof.usr_id);
			stmt.setTimestamp(index++, curTime);
			stmt.setString(index++, prof.usr_id);
			stmt.setLong(index++, fromItm.itm_id);
			stmt.executeUpdate();
			cwSQL.cleanUp(null, stmt);

			// CourseMeasurement and CourseModuleCriteria
			long cmr_id = 0;
			long new_cmr_id = 0;

			sql = " select cmr_id from CourseModuleCriteria where cmr_ccr_id = ( ";
			sql += " select distinct(ccr_id) from CourseCriteria where ccr_itm_id = ?) AND cmr_del_timestamp IS NULL";
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, fromItm.itm_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				cmr_id = rs.getLong("cmr_id");

				// CourseModuleCriteria
				long cmr_res_id = 0;
				sql = " select cmr_res_id from CourseModuleCriteria where cmr_id = ? ";
				stmt1 = con.prepareStatement(sql);
				index = 1;
				stmt1.setLong(index++, cmr_id);
				rs1 = stmt1.executeQuery();
				if (rs1.next()) {
					cmr_res_id = rs1.getLong("cmr_res_id");
				}
				cwSQL.cleanUp(rs1, stmt1);

				long new_res_id = ((Long) res_id_map.get(new Long(cmr_res_id))).longValue();

				sql = "";
				sql += " insert into CourseModuleCriteria (cmr_ccr_id, cmr_res_id, cmr_status, cmr_contri_rate, cmr_is_contri_by_score, cmr_is_contri_on_status, ";
				sql += " cmr_create_timestamp, cmr_create_usr_id, cmr_upd_timestamp, cmr_upd_usr_id, ";
				sql += " cmr_del_timestamp, cmr_status_desc_option, cmr_cmr_id_parent)";
				sql += " select ?, ?, cmr_status, cmr_contri_rate, cmr_is_contri_by_score, cmr_is_contri_on_status, ";
				sql += " ?, ?, ?, ?, cmr_del_timestamp, cmr_status_desc_option, cmr_cmr_id_parent ";
				sql += " from CourseModuleCriteria where cmr_id = ? ";
				stmt1 = con.prepareStatement(sql);
				index = 1;
				stmt1.setLong(index++, ccr_id);
				stmt1.setLong(index++, new_res_id);
				stmt1.setTimestamp(index++, curTime);
				stmt1.setString(index++, prof.usr_id);
				stmt1.setTimestamp(index++, curTime);
				stmt1.setString(index++, prof.usr_id);
				stmt1.setLong(index++, cmr_id);
				stmt1.executeUpdate();

				
				cwSQL.cleanUp(rs1, stmt1);
				new_cmr_id = cwSQL.getMaxId(con, "CourseModuleCriteria", "cmr_id");

				// CourseMeasurement
				sql = "";
				sql += " insert into CourseMeasurement (cmt_title, cmt_ccr_id, cmt_cmr_id, cmt_max_score, cmt_status, cmt_contri_rate, ";
				sql += " 	cmt_is_contri_by_score, cmt_create_timestamp, cmt_create_usr_id, cmt_update_timestamp, ";
				sql += " 	cmt_update_usr_id, cmt_delete_timestamp, cmt_pass_score, cmt_status_desc_option, cmt_order, cmt_cmt_id_parent, ";
				sql += " 	cmt_duration, cmt_place) ";
				sql += " select cmt_title, ?, ?, cmt_max_score, cmt_status, cmt_contri_rate, ";
				sql += " 	cmt_is_contri_by_score, ?, ?, ?, ?, cmt_delete_timestamp, cmt_pass_score, cmt_status_desc_option, cmt_order, cmt_cmt_id_parent, ";
				sql += " 	cmt_duration, cmt_place ";
				sql += " from CourseMeasurement where cmt_ccr_id in (select distinct(ccr_id) from CourseCriteria where ccr_itm_id = ?) ";
				sql += " and cmt_cmr_id = ? ";
				stmt1 = con.prepareStatement(sql);
				index = 1;
				stmt1.setLong(index++, ccr_id);
				stmt1.setLong(index++, new_cmr_id);
				stmt1.setTimestamp(index++, curTime);
				stmt1.setString(index++, prof.usr_id);
				stmt1.setTimestamp(index++, curTime);
				stmt1.setString(index++, prof.usr_id);
				stmt1.setLong(index++, fromItm.itm_id);
				stmt1.setLong(index++, cmr_id);
				stmt1.executeUpdate();
				cwSQL.cleanUp(rs1, stmt1);
			}

			// 复制课程资源文件
			String oldDir = wizbini.getFileUploadItmDirAbs() + cwUtils.SLASH + fromItm.itm_id;
			String newDir = wizbini.getFileUploadItmDirAbs() + cwUtils.SLASH + targetItm.itm_id;
			copyFolder(oldDir, newDir);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public static void copyFolder(String oldPath, String newPath) {
		try {
			if (!new File(newPath).exists()) {
				new File(newPath).mkdirs();
			}

			File a = new File(oldPath);
			String[] files = a.list();
			File temp = null;
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (oldPath.endsWith(File.separator)) {
						temp = new File(oldPath + files[i]);
					} else {
						temp = new File(oldPath + File.separator + files[i]);
					}

					if (temp.isFile()) {
						FileInputStream input = new FileInputStream(temp);
						FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
						byte[] b = new byte[1024 * 5];
						int len;
						while ((len = input.read(b)) != -1) {
							output.write(b, 0, len);
						}
						output.flush();
						output.close();
						input.close();
					}
					if (temp.isDirectory()) {
						copyFolder(oldPath + cwUtils.SLASH + files[i], newPath + cwUtils.SLASH + files[i]);
					}
				}
			}
		} catch (Exception e) {
			CommonLog.error("copy folder error - " + e.getMessage(),e);
		}
	}

	private static void getCourseCatalog(Connection con, loginProfile prof, aeItem itm, long tcr_id, Vector<Long> tndIdVec, Vector<String> tndTitleVec) throws SQLException, cwSysMessage, cwException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			Vector<Long> vec = new Vector<Long>();

			String sql = " select tnd_parent_tnd_id from aeTreeNode where tnd_itm_id = ? and tnd_type = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm.itm_id);
			stmt.setString(2, aeTreeNode.TND_TYPE_ITEM);

			rs = stmt.executeQuery();
			while (rs.next()) {
				vec.add(rs.getLong("tnd_parent_tnd_id"));
			}
			cwSQL.cleanUp(rs, stmt);

			for (long itm_tnd_id : vec) {
				Vector<aeTreeNode> tndVec = new Vector<aeTreeNode>();
				getCourseCatalog(con, itm_tnd_id, tndVec);

				if (tndVec != null && tndVec.size() > 0) {
					int size = tndVec.size();
					long parent_id = tcr_id;

					for (int i = (size - 1); i >= 0; i--) {
						aeTreeNode tnd = tndVec.elementAt(i);

						long tnd_id = createTndOrCat(con, prof, tcr_id, parent_id, tnd);
						if (i == 0) {
							tndIdVec.add(tnd_id);
							tndTitleVec.add(tnd.tnd_title);
						}
						parent_id = tnd_id;
					}
				}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	private static void getCourseCatalog(Connection con, long tnd_id, Vector<aeTreeNode> tndVec) throws SQLException, cwSysMessage {
		aeTreeNode tnd = new aeTreeNode();
		tnd.tnd_id = tnd_id;
		tnd.get(con);

		if (cwUtils.notEmpty(tnd.tnd_type) && tnd.tnd_type.equals(aeTreeNode.TND_TYPE_NORMAL)) { // 普通的二级目录，递归查询上级目录
			tndVec.add(tnd);

			getCourseCatalog(con, tnd.tnd_parent_tnd_id, tndVec);
		} else if (cwUtils.notEmpty(tnd.tnd_type) && tnd.tnd_type.equals(aeTreeNode.TND_TYPE_CAT)) { // 课程目录，直接加入返回
			tndVec.add(tnd);
		}
	}

	private static long createTndOrCat(Connection con, loginProfile prof, long tcr_id, long parent_id, aeTreeNode tnd) throws SQLException, cwSysMessage, cwException {
		dbRegUser tadm = getTrainingCenterOfficerByTcrId(con, tcr_id);
		long tadm_ent_id = tadm != null ? tadm.usr_ent_id : prof.usr_ent_id;
		long root_ent_id = tadm != null ? tadm.usr_ste_ent_id : prof.root_ent_id;
		String tadm_usr_id = tadm != null ? tadm.usr_id : prof.usr_id;

		long tnd_id = 0;
		if (cwUtils.notEmpty(tnd.tnd_type) && tnd.tnd_type.equals(aeTreeNode.TND_TYPE_NORMAL)) {
			tnd_id = getTndIdByTitle(con, tnd.tnd_title, parent_id);
			if (tnd_id <= 0) {
				aeTreeNode newTnd = new aeTreeNode();
				newTnd.tnd_title = tnd.tnd_title;
				newTnd.tnd_desc = tnd.tnd_desc;
				newTnd.tnd_owner_ent_id = tadm_ent_id;
				newTnd.tnd_parent_tnd_id = parent_id;
				newTnd.tnd_status = aeTreeNode.TND_STATUS_ON;
				newTnd.ins(con, root_ent_id, tadm_usr_id);

				tnd_id = newTnd.tnd_id;
			}
		} else if (cwUtils.notEmpty(tnd.tnd_type) && tnd.tnd_type.equals(aeTreeNode.TND_TYPE_CAT)) {
			tnd_id = getCatalogTndIdByTitle(con, tnd.tnd_title, parent_id);
			if (tnd_id <= 0) {
				aeCatalog cat = new aeCatalog();
				cat.cat_title = tnd.tnd_title;
				cat.cat_desc = tnd.tnd_desc;
				cat.cat_owner_ent_id = tadm_ent_id;
				cat.cat_tcr_id = parent_id;
				cat.cat_status = aeCatalog.CAT_STATUS_ON;
				cat.ins(con, root_ent_id, tadm_usr_id, new long[] { root_ent_id }, new String[] { "ALL_TYPES" });

				tnd_id = cat.cat_treenode.tnd_id;
			}
		}
		return tnd_id;
	}

	private static long getCatalogTndIdByTitle(Connection con, String cat_title, long tcr_id) throws SQLException, cwSysMessage {
		long tnd_id = 0;

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " select tnd_id from aeCatalog, aeTreeNode where tnd_cat_id = cat_id and tnd_type = ? and cat_tcr_id = ? and cat_title = ?  ";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, aeTreeNode.TND_TYPE_CAT);
			stmt.setLong(2, tcr_id);
			stmt.setString(3, cat_title);
			rs = stmt.executeQuery();
			while (rs.next()) {
				tnd_id = rs.getLong("tnd_id");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return tnd_id;
	}

	private static long getTndIdByTitle(Connection con, String tnd_title, long parent_id) throws SQLException, cwSysMessage {
		long tnd_id = 0;

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " select tnd_id from aeTreeNode where tnd_parent_tnd_id = ? and tnd_title = ?  ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, parent_id);
			stmt.setString(2, tnd_title);
			rs = stmt.executeQuery();
			while (rs.next()) {
				tnd_id = rs.getLong("tnd_id");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return tnd_id;
	}

	private static boolean isCopied(Connection con, long itm_id, long tcr_id) throws SQLException, cwSysMessage {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " select * from aeItem where itm_parent_id = ? and itm_tcr_id = ?  ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			stmt.setLong(2, tcr_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return false;
	}

	private static aeItem getItem(Connection con, aeItem fromItm, Vector<Object> vColName, Vector<Object> vColType, Vector<Object> vColValue, Vector<Object> vExtensionColName, Vector<Object> vExtensionColType,
			Vector<Object> vExtensionColValue, Vector<Object> vClobColName, Vector<Object> vClobColValue) throws SQLException, cwSysMessage {
		aeItem itm = new aeItem();

		itm.itm_app_approval_type = fromItm.itm_app_approval_type;

		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_parent_id", DbTable.COL_TYPE_LONG, Long.valueOf(fromItm.itm_id));

		itm.itm_type = fromItm.itm_type;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_type", DbTable.COL_TYPE_STRING, itm.itm_type);
		
		itm.itm_bonus_ind = fromItm.itm_bonus_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_bonus_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_bonus_ind));

		itm.itm_create_run_ind = fromItm.itm_create_run_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_create_run_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_create_run_ind));

		itm.itm_apply_ind = fromItm.itm_apply_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_apply_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_apply_ind));

		itm.itm_qdb_ind = fromItm.itm_qdb_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_qdb_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_qdb_ind));

		itm.itm_deprecated_ind = fromItm.itm_deprecated_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_deprecated_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_deprecated_ind));

		itm.itm_auto_enrol_qdb_ind = fromItm.itm_auto_enrol_qdb_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_auto_enrol_qdb_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_auto_enrol_qdb_ind));

		itm.itm_run_ind = fromItm.itm_run_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_run_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_run_ind));

		itm.itm_blend_ind = fromItm.itm_blend_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_blend_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_blend_ind));

		itm.itm_exam_ind = fromItm.itm_exam_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_exam_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_exam_ind));

		itm.itm_ref_ind = fromItm.itm_ref_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_ref_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_ref_ind));

		itm.itm_session_ind = fromItm.itm_session_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_session_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_session_ind));

		itm.itm_create_session_ind = fromItm.itm_create_session_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_create_session_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_create_session_ind));

		itm.itm_integrated_ind = fromItm.itm_integrated_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_integrated_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_integrated_ind));

		itm.itm_has_attendance_ind = fromItm.itm_has_attendance_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_has_attendance_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_has_attendance_ind));

		itm.itm_ji_ind = fromItm.itm_ji_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_ji_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_ji_ind));

		itm.itm_completion_criteria_ind = fromItm.itm_completion_criteria_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_completion_criteria_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_completion_criteria_ind));

		itm.itm_can_cancel_ind = fromItm.itm_can_cancel_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_can_cancel_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_can_cancel_ind));

		itm.itm_retake_ind = fromItm.itm_retake_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_retake_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_retake_ind));

		itm.itm_can_qr_ind = fromItm.itm_can_qr_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_can_qr_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_can_qr_ind));

		itm.itm_send_enroll_email_ind = fromItm.itm_send_enroll_email_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_send_enroll_email_ind", DbTable.COL_TYPE_LONG, Long.valueOf(itm.itm_send_enroll_email_ind));
		
		itm.itm_ji_send_datetime = fromItm.itm_ji_send_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_ji_send_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_ji_send_datetime);
		
		itm.itm_reminder_send_datetime = fromItm.itm_reminder_send_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_reminder_send_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_reminder_send_datetime);

		itm.itm_inst_type = fromItm.itm_inst_type;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_inst_type", DbTable.COL_TYPE_STRING, itm.itm_inst_type);
		
		itm.itm_not_allow_waitlist_ind = fromItm.itm_not_allow_waitlist_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_not_allow_waitlist_ind", DbTable.COL_TYPE_BOOLEAN, Boolean.valueOf(itm.itm_not_allow_waitlist_ind));

		itm.itm_code = cwSQL.getAutoCode(con, "COS"); // 自动生成编号
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_code", DbTable.COL_TYPE_STRING, itm.itm_code);

		itm.itm_title = fromItm.itm_title;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_title", DbTable.COL_TYPE_STRING, itm.itm_title);

		itm.itm_status = fromItm.itm_status;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_status", DbTable.COL_TYPE_STRING, itm.itm_status);

		itm.itm_srh_content = fromItm.itm_srh_content;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_srh_content", DbTable.COL_TYPE_STRING, itm.itm_srh_content);

		itm.itm_desc = fromItm.itm_desc;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_desc", DbTable.COL_TYPE_STRING, itm.itm_desc);

		itm.itm_icon = fromItm.itm_icon;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_icon", DbTable.COL_TYPE_STRING, itm.itm_icon);

		itm.itm_access_type = fromItm.itm_access_type;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_access_type", DbTable.COL_TYPE_STRING, itm.itm_access_type);

		itm.itm_capacity = fromItm.itm_capacity;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_capacity", DbTable.COL_TYPE_LONG, Long.valueOf(itm.itm_capacity));

		itm.itm_mark_buffer_day = fromItm.itm_mark_buffer_day;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_mark_buffer_day", DbTable.COL_TYPE_INT, Integer.valueOf(itm.itm_mark_buffer_day));

		itm.itm_notify_days = fromItm.itm_notify_days;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_notify_days", DbTable.COL_TYPE_INT, Integer.valueOf(itm.itm_notify_days));

		itm.itm_fee = fromItm.itm_fee;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_fee", DbTable.COL_TYPE_FLOAT, Float.valueOf(itm.itm_fee));

		itm.itm_appn_start_datetime = fromItm.itm_appn_start_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_appn_start_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_appn_start_datetime);

		itm.itm_appn_end_datetime = fromItm.itm_appn_end_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_appn_end_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_appn_end_datetime);

		itm.itm_eff_start_datetime = fromItm.itm_eff_start_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_eff_start_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_eff_start_datetime);

		itm.itm_eff_end_datetime = fromItm.itm_eff_end_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_eff_end_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_eff_end_datetime);

		itm.itm_content_eff_start_datetime = fromItm.itm_content_eff_start_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_content_eff_start_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_content_eff_start_datetime);

		itm.itm_content_eff_end_datetime = fromItm.itm_content_eff_end_datetime;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_content_eff_end_datetime", DbTable.COL_TYPE_TIMESTAMP, itm.itm_content_eff_end_datetime);

		itm.itm_content_eff_duration = fromItm.itm_content_eff_duration;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_content_eff_duration", DbTable.COL_TYPE_INT, itm.itm_content_eff_duration);
		
		itm.itm_mobile_ind = fromItm.itm_mobile_ind;
		addColumnAndValueToVector(vColName, vColType, vColValue, "itm_mobile_ind", DbTable.COL_TYPE_STRING, itm.itm_mobile_ind);

		itm.itm_xml = fromItm.itm_xml;
		itm.itm_xml = itm.itm_xml.replaceAll("<field01>.+</field01>", "<field01>" + itm.itm_code + "</field01>"); // 替换课程编号为新的自动编号

		addColumnAndValueToVector(vClobColName, null, vClobColValue, "itm_xml", DbTable.COL_TYPE_STRING, itm.itm_xml);

		return itm;
	}

	private static void copyAeItemextension(Connection con, long source_itm_id, long target_itm_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " insert into aeItemextension (ies_itm_id, ies_lang, ies_objective, ies_contents, ies_duration, ies_audience, ";
			sql += " ies_prerequisites, ies_exemptions, ies_remarks, ies_enroll_confirm_remarks, ies_schedule, ";
			sql += " ies_itm_ref_materials_1, ies_itm_ref_materials_2, ies_itm_ref_materials_3, ies_itm_ref_materials_4, ies_itm_ref_materials_5, ";
			sql += " ies_itm_ref_url_1, ies_itm_ref_url_2, ies_itm_ref_url_3, ies_itm_ref_url_4, ies_itm_ref_url_5, ";
			sql += " ies_itm_rel_materials_1, ies_itm_rel_materials_2, ies_itm_rel_materials_3, ies_itm_rel_materials_4, ies_itm_rel_materials_5, ";
			sql += " ies_itm_rel_materials_6, ies_itm_rel_materials_7, ies_itm_rel_materials_8, ies_itm_rel_materials_9, ies_itm_rel_materials_10, ";
			sql += " ies_top_ind, ies_top_icon) ";
			sql += " select ?, ies_lang, ies_objective, ies_contents, ies_duration, ies_audience, ";
			sql += " ies_prerequisites, ies_exemptions, ies_remarks, ies_enroll_confirm_remarks, ies_schedule, ";
			sql += " ies_itm_ref_materials_1, ies_itm_ref_materials_2, ies_itm_ref_materials_3, ies_itm_ref_materials_4, ies_itm_ref_materials_5, ";
			sql += " ies_itm_ref_url_1, ies_itm_ref_url_2, ies_itm_ref_url_3, ies_itm_ref_url_4, ies_itm_ref_url_5, ";
			sql += " ies_itm_rel_materials_1, ies_itm_rel_materials_2, ies_itm_rel_materials_3, ies_itm_rel_materials_4, ies_itm_rel_materials_5, ";
			sql += " ies_itm_rel_materials_6, ies_itm_rel_materials_7, ies_itm_rel_materials_8, ies_itm_rel_materials_9, ies_itm_rel_materials_10, ";
			sql += " ies_top_ind, ies_top_icon ";
			sql += " from aeItemextension where ies_itm_id = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, target_itm_id);
			stmt.setLong(2, source_itm_id);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	private static String[] getTrainingCenterOfficer(Connection con, long tcr_id) throws SQLException {
		String[] iac_list = null;

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " select tco_rol_ext_id, tco_usr_ent_id from tcTrainingCenterOfficer, acRole where tco_rol_ext_id = rol_ext_id and rol_ste_uid = 'TADM' and tco_tcr_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, tcr_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String iac = rs.getString("tco_rol_ext_id") + "~" + rs.getString("tco_usr_ent_id");
				iac_list = new String[] { iac };
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return iac_list;
	}

	private static dbRegUser getTrainingCenterOfficerByTcrId(Connection con, long tcr_id) throws SQLException {
		dbRegUser usr = null;
		long usr_ent_id = 0;

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " select tco_rol_ext_id, tco_usr_ent_id from tcTrainingCenterOfficer, acRole where tco_rol_ext_id = rol_ext_id and rol_ste_uid = 'TADM' and tco_tcr_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, tcr_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				usr_ent_id = rs.getLong("tco_usr_ent_id");
			}

			if (usr_ent_id > 0) {
				usr = new dbRegUser();
				usr.usr_ent_id = usr_ent_id;
				try {
					usr.get(con);
				} catch (qdbException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return usr;
	}

	private static void addColumnAndValueToVector(Vector<Object> vColName, Vector<Object> vColType, Vector<Object> vColValue, String key, String type, Object val) {
		if (vColName != null)
			vColName.addElement(key);
		if (vColType != null)
			vColType.addElement(type);
		if (vColValue != null)
			vColValue.addElement(val);
	}

}