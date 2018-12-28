package com.cw.wizbank.instructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class InstructorManager {
	public static final String Delimiter = "~|~";
	public static final String FTN_TEACHING_COURSE_LIST = "TEACHING_COURSE_LIST";

	public static String ITI_TYPE_REMARK_EXT = Instructor.TYPE_MARK_EXT;
	public static String ITI_TYPE_REMARK_IN = Instructor.TYPE_MARK_INT;

	public String getinstructorCommentAsXml(Connection con, long itm_id, long iti_ent_id, int page, int page_size) throws SQLException, cwSysMessage {
		StringBuffer result = new StringBuffer();
		InstructorDao insDao = new InstructorDao();
		InstructorComment insc = null;
		Vector<InstructorComment> ins_comment_v = insDao.getInstructorComment(con, itm_id, iti_ent_id);
		if (page == 0) {
			page = 1;
		}
		if (page_size == 0) {
			page_size = 10;
		}

		result.append("<instructorcomments >");
		int start = page_size * (page - 1);
		int count = 0;
		for (int i = 0; i < ins_comment_v.size(); i++) {
			if (count >= start && count < start + page_size) {
				insc = (InstructorComment) ins_comment_v.get(i);
				result.append("<instructorcomment id=\"").append(insc.getItc_id());
				result.append("\" itm_id=\"").append(insc.getItc_itm_id());
				result.append("\" ent_id=\"").append(insc.getItc_ent_id());
				result.append("\" iti_ent_id=\"").append(insc.getItc_iti_ent_id());
				result.append("\">");
				result.append("<name>").append(cwUtils.esc4XML(insc.getUsr_display_bil())).append("</name>");
				result.append("<style_score>").append(cwUtils.roundingFloat(insc.getItc_style_score(), 1)).append("</style_score>");
				result.append("<quality_score>").append(cwUtils.roundingFloat(insc.getItc_quality_score(), 1)).append("</quality_score>");
				result.append("<structure_score>").append(cwUtils.roundingFloat(insc.getItc_structure_score(), 1)).append("</structure_score>");
				result.append("<interaction_score>").append(cwUtils.roundingFloat(insc.getItc_interaction_score(), 1)).append("</interaction_score>");
				result.append("<score>").append(cwUtils.roundingFloat(insc.getItc_score(), 1)).append("</score>");
				result.append("<comment>").append(cwUtils.esc4XML(insc.getItc_comment())).append("</comment>");
				result.append("<upd_datetime>").append(insc.getItc_upd_datetime()).append("</upd_datetime>");
				result.append("</instructorcomment>");
			}
			count++;
		}
		result.append("</instructorcomments>");
		aeItem aeitem = new aeItem();
		String title =aeitem.getItemTitle(con, itm_id);
		String instructor=insDao.getInstructorName(con, iti_ent_id);
		if(instructor==null){
			instructor =dbRegUser.getDisplayBil(con, iti_ent_id);
		}
		if(title!=null && instructor !=null){
			result.append("<item title=\"").append(cwUtils.esc4XML(title));
			result.append("\" instructor=\"").append(cwUtils.esc4XML(instructor)).append("\"/>");
		}
		cwPagination pagn = new cwPagination();
		pagn.totalRec = count;
		pagn.totalPage = (int) Math.ceil((float) count / page_size);
		pagn.pageSize = page_size;
		pagn.curPage = page;
		pagn.sortCol = "";
		pagn.sortOrder = "";
		pagn.ts = null;
		result.append(pagn.asXML());

		return result.toString();
	}

	/**
	 * 授课评分
	 */
	public String getinstructorCourseAsXml(Connection con, long iti_ent_id, int page, int page_size) throws SQLException {
		StringBuffer result = new StringBuffer();
		InstructorDao insDao = new InstructorDao();
		aeItemAccess aeitem_acc = new aeItemAccess();
		aeItem aeitem = new aeItem();
		InstructorCos inscos = null;
		InstructorComment insc = null;
		Vector<InstructorCos> ins_course_v = aeitem_acc.getinstructorCourseAsXML(con, iti_ent_id);

		if (page == 0) {
			page = 1;
		}
		if (page_size == 0) {
			page_size = 10;
		}

		int start = page_size * (page - 1);
		int count = 0;
		for (int i = 0; i < ins_course_v.size(); i++) {
			if (count >= start && count < start + page_size) {
				inscos = ins_course_v.get(i);
				result.append("<item id=\"").append(inscos.getCos_itm_id()).append("\"").append(" type=\"").append(inscos.getItm_type()).append("\"").append(" itm_integrated_ind=\"")
						.append(inscos.isItm_integrated_ind()).append("\"").append(" dummy_type=\"")
						.append(aeItemDummyType.getDummyItemType(inscos.getItm_type(), inscos.isItm_blend_ind(), inscos.isItm_exam_ind(), inscos.isItm_ref_ind())).append("\">").append("<title>")
						.append(cwUtils.esc4XML(inscos.getCos_itm_title())).append("</title>");
				// 授课对象
				String itm_xml = aeitem.getItemXML(con, inscos.getCos_itm_id());
				String target = "";
				if (itm_xml != null) {
					int start_idx = itm_xml.indexOf("<field09>");
					int end_idx = itm_xml.indexOf("</field09>");
					if (start_idx > 0 && end_idx > (start_idx + 9)) {
						target = itm_xml.substring(start_idx + 9, end_idx);
					}
				}
				result.append("<target>").append(target).append("</target>");
				// 统计评分
				Vector<InstructorComment> ins_comment_v = null;
				if (inscos.getRun_itm_id() > 0) {
					ins_comment_v = insDao.getInstructorComment(con, inscos.getRun_itm_id(), iti_ent_id);
				} else {
					ins_comment_v = insDao.getInstructorComment(con, inscos.getCos_itm_id(), iti_ent_id);
				}

				float score = 0;
				for (int j = 0; j < ins_comment_v.size(); j++) {
					insc = ins_comment_v.get(j);
					score += insc.getItc_score();
				}
				if (ins_comment_v.size() > 0)
					score = score / (float) ins_comment_v.size();
				result.append("<score>").append(cwUtils.roundingFloat(score, 1)).append("</score>");
				if (inscos.getRun_itm_id() == 0) {
					result.append("<status>").append(inscos.getItm_status()).append("</status>").append("<eff_start_datetime>").append(cwUtils.escNull(inscos.getItm_eff_start_datetime())).append("</eff_start_datetime>")
							.append("<eff_end_datetime>").append(cwUtils.escNull(inscos.getItm_eff_end_datetime())).append("</eff_end_datetime>");
				} else {
					result.append("<child_item id=\"").append(inscos.getRun_itm_id()).append("\">").append("<title>").append(cwUtils.esc4XML(inscos.getRun_itm_title())).append("</title>").append("<status>")
							.append(inscos.getItm_status()).append("</status>").append("<eff_start_datetime>").append(cwUtils.escNull(inscos.getItm_eff_start_datetime())).append("</eff_start_datetime>")
							.append("<eff_end_datetime>").append(cwUtils.escNull(inscos.getItm_eff_end_datetime())).append("</eff_end_datetime>").append("</child_item>");
				}
				if (inscos.isItm_integrated_ind()) {
					result.append("<resourse id=\"").append(dbCourse.getCosResId(con, inscos.getCos_itm_id())).append("\"/>");
				}
				result.append("</item>");
			}
			count++;
		}

		cwPagination pagn = new cwPagination();
		pagn.totalRec = count;
		pagn.totalPage = (int) Math.ceil((float) count / page_size);
		pagn.pageSize = page_size;
		pagn.curPage = page;
		pagn.sortCol = "";
		pagn.sortOrder = "";
		pagn.ts = null;
		result.append(pagn.asXML());

		return result.toString();
	}


	/**
	 * 讲师搜索
	 * 
	 * @param con
	 * @param itm_id
	 * @param iti_ent_id
	 * @param page
	 * @param page_size
	 * @return
	 */
	public String instructorSearchAsXml(Connection con, InstructorReqParam param,Long top_tc_id,boolean lnMode) throws SQLException {
		StringBuffer result = new StringBuffer();
		InstructorDao insDao = new InstructorDao();
		Instructor inst = null;
		String iti_type_mark = param.getIti_type_mark();
		cwPagination cwPage = param.getCwPage();
		if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
			cwPage.sortCol = "iti_update_datetime";
		}
		if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
			cwPage.sortOrder = "desc";
		}
		if (cwPage.curPage == 0) {
			cwPage.curPage = 1;
		}
		if (cwPage.pageSize == 0) {
			cwPage.pageSize = 10;
		}

		Vector<Instructor> ins_v = insDao.search(con, param, iti_type_mark,top_tc_id,lnMode);

		result.append("<instructor_lst>").append(cwUtils.NEWL);
		for (int i = 0; i < ins_v.size(); i++) {
			inst = ins_v.get(i);
			result.append("<instructor id=\"").append(inst.iti_ent_id).append("\">").append(cwUtils.NEWL);
			result.append("<iti_name>").append(cwUtils.esc4XML(inst.iti_name)).append("</iti_name>").append(cwUtils.NEWL);
			result.append("<iti_gender>").append(cwUtils.esc4XML(inst.iti_gender)).append("</iti_gender>").append(cwUtils.NEWL);
			result.append("<iti_level>").append(cwUtils.esc4XML(inst.iti_level)).append("</iti_level>").append(cwUtils.NEWL);
			result.append("<iti_sks_title>").append(cwUtils.esc4XML(inst.iti_sks_title)).append("</iti_sks_title>").append(cwUtils.NEWL);
			result.append("<iti_main_course>").append(cwUtils.esc4XML(inst.iti_main_course)).append("</iti_main_course>").append(cwUtils.NEWL);
			result.append("<iti_mobile>").append(cwUtils.esc4XML(inst.iti_mobile)).append("</iti_mobile>").append(cwUtils.NEWL);
			result.append("<score>").append(cwUtils.formatNumber(inst.iti_score, 1)).append("</score>").append(cwUtils.NEWL);
			result.append("<iti_training_company>").append(cwUtils.esc4XML(inst.iti_training_company)).append("</iti_training_company>").append(cwUtils.NEWL);
			result.append("<iti_expertise_areas>").append(cwUtils.esc4XML(inst.iti_expertise_areas)).append("</iti_expertise_areas>").append(cwUtils.NEWL);
			result.append("<iti_type_mark>").append(cwUtils.esc4XML(inst.iti_type_mark)).append("</iti_type_mark>").append(cwUtils.NEWL);

			result.append("</instructor>").append(cwUtils.NEWL);
		}
		result.append("</instructor_lst>").append(cwUtils.NEWL);
		result.append("<searchCom>").append(cwUtils.NEWL);
		if (param.getIti_name() != null && param.getIti_name().trim().length() > 0) {
			result.append("<iti_name>").append(cwUtils.esc4XML(param.getIti_name().trim())).append("</iti_name>").append(cwUtils.NEWL);
		}
		if (param.getIti_level() != null && param.getIti_level().trim().length() > 0) {
			result.append("<iti_level>").append(cwUtils.esc4XML(param.getIti_level().trim())).append("</iti_level>").append(cwUtils.NEWL);
		}
		if (param.getIti_gw_str() != null && param.getIti_gw_str().trim().length() > 0) {
			result.append("<iti_gw_str>").append(cwUtils.esc4XML(param.getIti_gw_str().trim())).append("</iti_gw_str>").append(cwUtils.NEWL);
		}
		if (param.getIti_main_course() != null && param.getIti_main_course().trim().length() > 0) {
			result.append("<iti_main_course>").append(cwUtils.esc4XML(param.getIti_main_course().trim())).append("</iti_main_course>").append(cwUtils.NEWL);
		}
		if (param.getIti_score_from() > 0) {
			result.append("<iti_score_from>").append(param.getIti_score_from()).append("</iti_score_from>").append(cwUtils.NEWL);
		}
		if (param.getIti_score_to() > 0) {
			result.append("<iti_score_to>").append(param.getIti_score_to()).append("</iti_score_to>").append(cwUtils.NEWL);
		}
		if (param.getIti_expertise_areas() != null && param.getIti_expertise_areas().trim().length() > 0) {
			result.append("<iti_expertise_areas>").append(cwUtils.esc4XML(param.getIti_expertise_areas().trim())).append("</iti_expertise_areas>").append(cwUtils.NEWL);
		}
		if (param.getIti_training_company() != null && param.getIti_training_company().trim().length() > 0) {
			result.append("<iti_training_company>").append(cwUtils.esc4XML(param.getIti_training_company().trim())).append("</iti_training_company>").append(cwUtils.NEWL);
		}
		if (param.getJs_name() != null && param.getJs_name().trim().length() > 0) {
			result.append("<js_name>").append(param.getJs_name().trim()).append("</js_name>").append(cwUtils.NEWL);
		}
		if (param.getMax_select() > 0) {
			result.append("<max_select>").append(param.getMax_select()).append("</max_select>").append(cwUtils.NEWL);
		}
		if (param.getIs_poup() != null && param.getIs_poup().trim().length() > 0) {
			result.append("<is_poup>").append(param.getIs_poup().trim()).append("</is_poup>").append(cwUtils.NEWL);
		}
		if (param.getFor_time_table() != null && param.getFor_time_table().trim().length() > 0) {
			result.append("<for_time_table>").append(param.getFor_time_table().trim()).append("</for_time_table>").append(cwUtils.NEWL);
		}
		if (param.getIls_id() > 0) {
			result.append("<ils_id>").append(param.getIls_id()).append("</ils_id>").append(cwUtils.NEWL);
		}
		if(param.getSearch_text()!=null&&param.getSearch_text().trim().length()>0){
			result.append("<search_text>").append(cwUtils.esc4XML(param.getSearch_text())).append("</search_text>").append(cwUtils.NEWL);
		}
		result.append("<iti_type_mark>").append(iti_type_mark).append("</iti_type_mark>").append(cwUtils.NEWL);
		result.append("</searchCom>").append(cwUtils.NEWL);
		result.append(param.getCwPage().asXML());
		return result.toString();
	}

	public String getInstrXml(Connection con, WizbiniLoader wizbini, loginProfile prof, long iti_ent_id) throws qdbException, SQLException {
		Instructor instr = InstructorDao.get(con, iti_ent_id);
		Vector<InstructorCos> icsVec = InstructorDao.getInstrCos(con, iti_ent_id);

		String xml = "";
		xml += "<instructor>";
		xml += "<iti_ent_id>" + instr.getIti_ent_id() + "</iti_ent_id>";
		xml += "<iti_name>" + cwUtils.esc4XML(instr.getIti_name()) + "</iti_name>";
		xml += "<iti_gender>" + cwUtils.esc4XML(instr.getIti_gender()) + "</iti_gender>";
		xml += "<iti_bday>" + (instr.getIti_bday() != null ? cwUtils.format(instr.getIti_bday()) : "") + "</iti_bday>";
		xml += "<iti_mobile>" + cwUtils.esc4XML(instr.getIti_mobile()) + "</iti_mobile>";
		xml += "<iti_email>" + cwUtils.esc4XML(instr.getIti_email()) + "</iti_email>";
		xml += "<iti_img>" + cwUtils.esc4XML(dbRegUser.getUsrPhotoDir(wizbini, prof.root_id, instr.getIti_ent_id(), instr.getIti_img())) + "</iti_img>";
		xml += "<iti_introduction>" + cwUtils.esc4XML(instr.getIti_introduction()) + "</iti_introduction>";
		xml += "<iti_level>" + cwUtils.esc4XML(instr.getIti_level()) + "</iti_level>";
		xml += "<iti_cos_type>" + cwUtils.esc4XML(instr.getIti_cos_type()) + "</iti_cos_type>";
		xml += "<iti_main_course>" + cwUtils.esc4XML(instr.getIti_main_course()) + "</iti_main_course>";
		xml += "<iti_type>" + cwUtils.esc4XML(instr.getIti_type()) + "</iti_type>";
		xml += "<iti_property>" + cwUtils.esc4XML(instr.getIti_property()) + "</iti_property>";
		xml += "<iti_highest_educational>" + cwUtils.esc4XML(instr.getIti_highest_educational()) + "</iti_highest_educational>";
		xml += "<iti_graduate_institutions>" + cwUtils.esc4XML(instr.getIti_graduate_institutions()) + "</iti_graduate_institutions>";
		xml += "<iti_address>" + cwUtils.esc4XML(instr.getIti_address()) + "</iti_address>";
		xml += "<iti_work_experience>" + cwUtils.esc4XML(instr.getIti_work_experience()) + "</iti_work_experience>";
		xml += "<iti_education_experience>" + cwUtils.esc4XML(instr.getIti_education_experience()) + "</iti_education_experience>";
		xml += "<iti_training_experience>" + cwUtils.esc4XML(instr.getIti_training_experience()) + "</iti_training_experience>";
		xml += "<iti_expertise_areas>" + cwUtils.esc4XML(instr.getIti_expertise_areas()) + "</iti_expertise_areas>";
		xml += "<iti_good_industry>" + cwUtils.esc4XML(instr.getIti_good_industry()) + "</iti_good_industry>";
		xml += "<iti_training_company>" + cwUtils.esc4XML(instr.getIti_training_company()) + "</iti_training_company>";
		xml += "<iti_training_contacts>" + cwUtils.esc4XML(instr.getIti_training_contacts()) + "</iti_training_contacts>";
		xml += "<iti_training_tel>" + cwUtils.esc4XML(instr.getIti_training_tel()) + "</iti_training_tel>";
		xml += "<iti_training_email>" + cwUtils.esc4XML(instr.getIti_training_email()) + "</iti_training_email>";
		xml += "<iti_training_address>" + cwUtils.esc4XML(instr.getIti_training_address()) + "</iti_training_address>";
		xml += "<iti_status>" + cwUtils.esc4XML(instr.getIti_status()) + "</iti_status>";
		xml += "<iti_type_mark>" + cwUtils.esc4XML(instr.getIti_type_mark()) + "</iti_type_mark>";
		xml += "<iti_sks_title>" + cwUtils.esc4XML(instr.getIti_sks_title()) + "</iti_sks_title>";
		xml += "<iti_user_group>" + cwUtils.esc4XML(instr.getIti_user_group()) + "</iti_user_group>";
		xml += "<iti_user_grade>" + cwUtils.esc4XML(instr.getIti_user_grade()) + "</iti_user_grade>";
		xml += "<iti_join_datetime>" + instr.getIti_join_datetime() + "</iti_join_datetime>";
		xml += "<iti_score>" + cwUtils.formatNumber(instr.getIti_score(), 1) + "</iti_score>";
		xml += "<iti_create_datetime>" + cwUtils.format(instr.getIti_create_datetime()) + "</iti_create_datetime>";
		xml += "<iti_create_user_id>" + cwUtils.esc4XML(instr.getIti_create_usr_id()) + "</iti_create_user_id>";
		xml += "<iti_update_datetime>" + cwUtils.format(instr.getIti_upd_datetime()) + "</iti_update_datetime>";
		xml += "<iti_update_user_id>" + cwUtils.esc4XML(instr.getIti_upd_usr_id()) + "</iti_update_user_id>";
		xml += "<iti_recommend>" + instr.getIti_recommend() + "</iti_recommend>";
		if (icsVec.size() > 0) {
			xml += "<instr_cos>";
			for (InstructorCos ics : icsVec) {
				xml += "<cos>";
				xml += "<ics_title>" + cwUtils.esc4XML(ics.getIcs_title()) + "</ics_title>";
				xml += "<ics_target>" + cwUtils.esc4XML(ics.getIcs_target()) + "</ics_target>";
				xml += "<ics_content>" + cwUtils.esc4XML(ics.getIcs_content()) + "</ics_content>";
				xml += "<ics_fee>" + ics.getIcs_fee() + "</ics_fee>";
				xml += "<ics_hours>" + ics.getIcs_hours() + "</ics_hours>";
				xml += "</cos>";
			}
			xml += "</instr_cos>";
		}
		xml += "</instructor>";
		return xml;
	}

	public String getIntInstrInsXml(Connection con, WizbiniLoader wizbini, loginProfile prof, long ref_ent_id) throws qdbException {
		String xml = "";
		if (ref_ent_id > 0) {
			dbRegUser user = new dbRegUser();
			user.usr_ent_id = ref_ent_id;
			user.get(con);

			xml += "<ref_user>";
			xml += user.getUserXML(con, prof, wizbini);
			xml += "</ref_user>";
		}
		return xml;
	}

	public String getInstrPropertiesXml(Connection con, loginProfile prof) throws qdbException {
		String xml = "";
		xml += "<iti_property_options>";
		xml += "<option value=\"" + Instructor.PROPERTY_BRANCH + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1054")) + "</option>";
		xml += "<option value=\"" + Instructor.PROPERTY_HEAD + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1053")) + "</option>";
		xml += "</iti_property_options>";
		xml += "<iti_type_options>";
		xml += "<option value=\"" + Instructor.TYPE_PART_TIME + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1048")) + "</option>";
		xml += "<option value=\"" + Instructor.TYPE_FULL_TIME + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1047")) + "</option>";
		xml += "</iti_type_options>";
		xml += "<iti_level_options>";
		xml += "<option value=\"" + Instructor.LEVEL_JUNIOR + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1049")) + "</option>";
		xml += "<option value=\"" + Instructor.LEVEL_DEDIUM + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1050")) + "</option>";
		xml += "<option value=\"" + Instructor.LEVEL_SENIOR + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1051")) + "</option>";
		xml += "<option value=\"" + Instructor.LEVEL_DISTINGUISHED + "\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1052")) + "</option>";
		xml += "</iti_level_options>";
		xml += "<iti_gender_options>";
		xml += "<option value=\"M\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1043")) + "</option>";
		xml += "<option value=\"F\">" + cwUtils.esc4XML(LangLabel.getValue(prof.cur_lan, "1044")) + "</option>";
		xml += "</iti_gender_options>";
		return xml;
	}

	public long insOrUpdInstructor(Connection con, WizbiniLoader wizbini,loginProfile prof, InstructorReqParam param) throws qdbException, qdbErrMessage, SQLException {
		long iti_ent_id = 0;
		Timestamp curTime = cwSQL.getTime(con);

		Instructor instr = getInstructorFromModParam(param);
		if (instr.getIti_ent_id() > 0) {
			instr.iti_upd_datetime = curTime;
			instr.iti_upd_usr_id = prof.usr_id;
			instr.ent_id = instr.getIti_ent_id();

			InstructorDao.upd(con, instr);

			iti_ent_id = instr.getIti_ent_id();
		} else {
			instr.iti_create_datetime = curTime;
			instr.iti_create_usr_id = prof.usr_id;
			instr.iti_upd_datetime = curTime;
			instr.iti_upd_usr_id = prof.usr_id;
			if(param.getRef_ent_id() > 0){
				instr.setIti_tcr_id(ViewTrainingCenter.getTopTc(con, param.getRef_ent_id(),wizbini.cfgSysSetupadv.isTcIndependent()));
			}else{
				instr.setIti_tcr_id(prof.my_top_tc_id);
			}
			
			iti_ent_id = InstructorDao.ins(con, instr, param.getRef_ent_id());
		}

		// 保存讲师可提供课程
		Vector<InstructorCos> icsVec = getInstructorCosFromModParam(param);
		InstructorDao.updateInstructorCourse(con, iti_ent_id, icsVec);

		return iti_ent_id;
	}

	private Instructor getInstructorFromModParam(InstructorReqParam param) {
		Instructor instr = new Instructor();
		instr.setIti_ent_id(param.getIti_ent_id());
		instr.setIti_name(param.getIti_name());
		instr.setIti_gender(param.getIti_gender());
		instr.setIti_bday(param.getIti_bday());
		instr.setIti_mobile(param.getIti_mobile());
		instr.setIti_email(param.getIti_email());
		instr.setIti_img(param.getIti_img());
		instr.setIti_introduction(param.getIti_introduction());
		instr.setIti_level(param.getIti_level());
		instr.setIti_cos_type(param.getIti_cos_type());
		instr.setIti_main_course(param.getIti_main_course());
		instr.setIti_type(param.getIti_type());
		instr.setIti_property(param.getIti_property());
		instr.setIti_highest_educational(param.getIti_highest_educational());
		instr.setIti_graduate_institutions(param.getIti_graduate_institutions());
		instr.setIti_address(param.getIti_address());
		instr.setIti_work_experience(param.getIti_work_experience());
		instr.setIti_education_experience(param.getIti_education_experience());
		instr.setIti_training_experience(param.getIti_training_experience());
		instr.setIti_expertise_areas(param.getIti_expertise_areas());
		instr.setIti_good_industry(param.getIti_good_industry());
		instr.setIti_training_company(param.getIti_training_company());
		instr.setIti_training_contacts(param.getIti_training_contacts());
		instr.setIti_training_tel(param.getIti_training_tel());
		instr.setIti_training_email(param.getIti_training_email());
		instr.setIti_training_address(param.getIti_training_address());
		instr.setIti_status(param.getIti_status());
		instr.setIti_type_mark(param.getIti_type_mark());
		if("use_default_image".equalsIgnoreCase(param.getIti_img_select())){
			instr.setIti_img(param.getDefault_image());
		}
		return instr;
	}

	@SuppressWarnings("unchecked")
	private Vector<InstructorCos> getInstructorCosFromModParam(InstructorReqParam param) {
		String ics_title = param.getIcs_title();
		String ics_fee = param.getIcs_fee();
		String ics_hours = param.getIcs_hours();
		String ics_target = param.getIcs_target();
		String ics_content = param.getIcs_content();

		Vector<String> icsTitleVec = new Vector<String>();
		Vector<String> icsFeeVec = new Vector<String>();
		Vector<String> icsHoursVec = new Vector<String>();
		Vector<String> icsTargetVec = new Vector<String>();
		Vector<String> icsContentVec = new Vector<String>();
		if (cwUtils.notEmpty(ics_title)) {
			icsTitleVec = cwUtils.splitToVecString(ics_title, Delimiter);
		}
		if (cwUtils.notEmpty(ics_fee)) {
			icsFeeVec = cwUtils.splitToVecString(ics_fee, Delimiter);
		}
		if (cwUtils.notEmpty(ics_fee)) {
			icsHoursVec = cwUtils.splitToVecString(ics_hours, Delimiter);
		}
		if (cwUtils.notEmpty(ics_fee)) {
			icsTargetVec = cwUtils.splitToVecString(ics_target, Delimiter);
		}
		if (cwUtils.notEmpty(ics_fee)) {
			icsContentVec = cwUtils.splitToVecString(ics_content, Delimiter);
		}

		Vector<InstructorCos> vec = new Vector<InstructorCos>();
		for (int i = 0; i < icsTitleVec.size(); i++) {
			InstructorCos ics = new InstructorCos();

			ics.setIcs_title(icsTitleVec.elementAt(i));

			float fee = 0f;
			if (icsFeeVec.size() > i && cwUtils.notEmpty(icsFeeVec.elementAt(i))) {
				try {
					fee = Float.parseFloat(icsFeeVec.elementAt(i));
				} catch (Exception e) {
					fee = 0f;
				}
			}
			ics.setIcs_fee(fee);

			float hours = 0f;
			if (icsHoursVec.size() > i && cwUtils.notEmpty(icsHoursVec.elementAt(i))) {
				try {
					hours = Float.parseFloat(icsHoursVec.elementAt(i));
				} catch (Exception e) {
					hours = 0f;
				}
			}
			ics.setIcs_hours(hours);

			if (icsTargetVec.size() > i && cwUtils.notEmpty(icsTargetVec.elementAt(i))) {
				ics.setIcs_target(icsTargetVec.elementAt(i));
			}
			if (icsContentVec.size() > i && cwUtils.notEmpty(icsContentVec.elementAt(i))) {
				ics.setIcs_content(icsContentVec.elementAt(i));
			}

			vec.add(ics);
		}
		return vec;
	}
}