package com.cw.wizbank.trunkinterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.DbPsnBiography;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwUtils;

@Component
@Transactional
public class UserBean  {
	
	
	/**获取学员详细信息
	 * @param con
	 * @param jsonResult
	 * @param prof
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void my_profile(Connection con, Map<String, String> valueMap, Map<String, Boolean> pubIndMap, loginProfile prof) throws SQLException, qdbException{
		dbRegUser regUser = new dbRegUser();
		regUser.usr_ent_id = prof.usr_ent_id;
		DbPsnBiography dbpbg = new DbPsnBiography();
		regUser.get(con);
		dbpbg.getBiographyByEntId(con, regUser.usr_ent_id);
		String options = dbpbg.pbg_option;
		setAttribute(prof, con, qdbAction.getWizbini(),  regUser, valueMap, pubIndMap, options);
	}


	
	private void setAttribute(loginProfile prof, Connection con, WizbiniLoader wizbini, dbRegUser regUser, Map<String, String> valueMap,
			Map<String, Boolean> pubIndMap, String options) throws SQLException{
		boolean ind = false;

		// 用户名
		valueMap.put("usr_id", regUser.usr_ste_usr_id);
		ind = false;
		if (options != null && options.indexOf("usr_ste_usr_id") != -1)
			ind = true;
		pubIndMap.put("usr_id", Boolean.valueOf(ind));

		// 全名
		valueMap.put("usr_display_bil", regUser.usr_display_bil != null ? regUser.usr_display_bil : "");
		ind = false;
		if (options != null && options.indexOf("usr_display_bil") != -1)
			ind = true;
		pubIndMap.put("usr_display_bil", Boolean.valueOf(ind));

		// 用户出身日期
		valueMap.put("usr_bday", regUser.usr_bday != null ? toJsonDateFormat(regUser.usr_bday) : "");
		ind = false;
		if (options != null && options.indexOf("usr_bday") != -1)
			ind = true;
		pubIndMap.put("usr_bday", Boolean.valueOf(ind));

		// 用户性别
		valueMap.put("usr_gender", regUser.usr_gender != null ? regUser.usr_gender : "");
		ind = false;
		if (options != null && options.indexOf("usr_gender") != -1)
			ind = true;
		pubIndMap.put("usr_gender", Boolean.valueOf(ind));

		// 用户Email
		valueMap.put("usr_email", regUser.usr_email != null ? regUser.usr_email : "");
		ind = false;
		if (options != null && options.indexOf("usr_email") != -1)
			ind = true;
		pubIndMap.put("usr_email", Boolean.valueOf(ind));

		// 用户电话
		valueMap.put("usr_tel_1", regUser.usr_tel_1 != null ? regUser.usr_tel_1 : "");
		ind = false;
		if (options != null && options.indexOf("usr_tel_1") != -1)
			ind = true;
		pubIndMap.put("usr_tel_1", Boolean.valueOf(ind));

		// 用户Fax
		valueMap.put("usr_fax_1", regUser.usr_fax_1 != null ? regUser.usr_fax_1 : "");
		ind = false;
		if (options != null && options.indexOf("usr_fax_1") != -1)
			ind = true;
		pubIndMap.put("usr_fax_1", Boolean.valueOf(ind));

		// 用户jobTitle
		valueMap.put("usr_job_title", regUser.usr_job_title != null ? regUser.usr_job_title : "");
		ind = false;
		if (options != null && options.indexOf("usr_job_title") != -1)
			ind = true;
		pubIndMap.put("usr_job_title", Boolean.valueOf(ind));

		// 用户join_date
		valueMap.put("usr_join_date", regUser.usr_join_datetime != null ? toJsonDateFormat(regUser.usr_join_datetime) : "");
		ind = false;
		if (options != null && options.indexOf("usr_join_date") != -1)
			ind = true;
		pubIndMap.put("usr_join_date", Boolean.valueOf(ind));

		// group
		valueMap.put("usr_group", dbEntityRelation.getFullPath(con, regUser.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG));
		ind = false;
		if (options != null && options.indexOf("usr_group") != -1)
			ind = true;
		pubIndMap.put("usr_group", Boolean.valueOf(ind));

		// grade
		valueMap.put("usr_grade", dbEntityRelation.getFullPath(con, regUser.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR));
		ind = false;
		if (options != null && options.indexOf("usr_grade") != -1)
			ind = true;
		pubIndMap.put("usr_grade", Boolean.valueOf(ind));

		// extension41
		valueMap.put("urx_extra_41", regUser.urx_extra_41 != null ? regUser.urx_extra_41 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_41") != -1)
			ind = true;
		pubIndMap.put("urx_extra_41", Boolean.valueOf(ind));

		// extension42
		valueMap.put("urx_extra_42", regUser.urx_extra_42 != null ? regUser.urx_extra_42 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_42") != -1)
			ind = true;
		pubIndMap.put("urx_extra_42", Boolean.valueOf(ind));

		// extension43
		if (regUser.urx_extra_43 == null || regUser.urx_extra_43.equalsIgnoreCase("")) {
			UserManagement usr = (UserManagement) wizbini.cfgOrgUserManagement.get(prof.root_id);
			String defaultUserPhoto = cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl())
					+ usr.getUserProfile().getProfileAttributes().getExtension43().getValue();
			valueMap.put("urx_extra_43", defaultUserPhoto);
		} else {
			String photopath = cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl()) + regUser.usr_ent_id + "/" + regUser.urx_extra_43;
			valueMap.put("urx_extra_43", photopath);
		}
		ind = false;
		if (options != null && options.indexOf("urx_extra_43") != -1)
			ind = true;
		pubIndMap.put("urx_extra_43", Boolean.valueOf(ind));

		// extension44
		valueMap.put("urx_extra_44", regUser.urx_extra_44 != null ? regUser.urx_extra_44 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_44") != -1)
			ind = true;
		pubIndMap.put("urx_extra_44", Boolean.valueOf(ind));

		// extension45
		valueMap.put("urx_extra_45", regUser.urx_extra_45 != null ? regUser.urx_extra_45 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_45") != -1)
			ind = true;
		pubIndMap.put("urx_extra_45", Boolean.valueOf(ind));

		// 昵称
		valueMap.put("usr_nickname", regUser.usr_nickname != null ? regUser.usr_nickname : "");
		ind = false;
		if (options != null && options.indexOf("usr_nickname") != -1)
			ind = true;
		pubIndMap.put("usr_nickname", Boolean.valueOf(ind));

		// 添加hkid
		valueMap.put("usr_hkid", regUser.usr_hkid != null ? regUser.usr_hkid : "");
		ind = false;
		if (options != null && options.indexOf("usr_hkid") != -1)
			ind = true;
		pubIndMap.put("usr_hkid", Boolean.valueOf(ind));

		// usr_source
		valueMap.put("usr_source", regUser.usr_source != null ? regUser.usr_source : "");
		ind = false;
		if (options != null && options.indexOf("usr_source") != -1)
			ind = true;
		pubIndMap.put("usr_source", Boolean.valueOf(ind));

		// extension_1
		valueMap.put("usr_extra_1", regUser.usr_extra_1 != null ? regUser.usr_extra_1 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_1") != -1)
			ind = true;
		pubIndMap.put("usr_extra_1", Boolean.valueOf(ind));

		// extension_2
		valueMap.put("usr_extra_2", regUser.usr_extra_2 != null ? regUser.usr_extra_2 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_2") != -1)
			ind = true;
		pubIndMap.put("usr_extra_2", Boolean.valueOf(ind));

		// extension_3
		valueMap.put("usr_extra_3", regUser.usr_extra_3 != null ? regUser.usr_extra_3 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_3") != -1)
			ind = true;
		pubIndMap.put("usr_extra_3", Boolean.valueOf(ind));

		// extension_4
		valueMap.put("usr_extra_4", regUser.usr_extra_4 != null ? regUser.usr_extra_4 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_4") != -1)
			ind = true;
		pubIndMap.put("usr_extra_4", Boolean.valueOf(ind));

		// extension_5
		valueMap.put("usr_extra_5", regUser.usr_extra_5 != null ? regUser.usr_extra_5 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_5") != -1)
			ind = true;
		pubIndMap.put("usr_extra_5", Boolean.valueOf(ind));

		// extension_6
		valueMap.put("usr_extra_6", regUser.usr_extra_6 != null ? regUser.usr_extra_6 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_6") != -1)
			ind = true;
		pubIndMap.put("usr_extra_6", Boolean.valueOf(ind));

		// extension_7
		valueMap.put("usr_extra_7", regUser.usr_extra_7 != null ? regUser.usr_extra_7 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_7") != -1)
			ind = true;
		pubIndMap.put("usr_extra_7", Boolean.valueOf(ind));

		// extension_8
		valueMap.put("usr_extra_8", regUser.usr_extra_8 != null ? regUser.usr_extra_8 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_8") != -1)
			ind = true;
		pubIndMap.put("usr_extra_8", Boolean.valueOf(ind));

		// extension_9
		valueMap.put("usr_extra_9", regUser.usr_extra_9 != null ? regUser.usr_extra_9 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_9") != -1)
			ind = true;
		pubIndMap.put("usr_extra_9", Boolean.valueOf(ind));

		// extension_10
		valueMap.put("usr_extra_10", regUser.usr_extra_10 != null ? regUser.usr_extra_10 : "");
		ind = false;
		if (options != null && options.indexOf("usr_extra_10") != -1)
			ind = true;
		pubIndMap.put("usr_extra_10", Boolean.valueOf(ind));

		// extension_11
		valueMap.put("urx_extra_datetime_11", regUser.urx_extra_datetime_11 != null ? regUser.urx_extra_datetime_11 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_11") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_11", Boolean.valueOf(ind));

		// extension_12
		valueMap.put("urx_extra_datetime_12", regUser.urx_extra_datetime_12 != null ? regUser.urx_extra_datetime_12 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_12") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_12", Boolean.valueOf(ind));

		// extension_13
		valueMap.put("urx_extra_datetime_13", regUser.urx_extra_datetime_13 != null ? regUser.urx_extra_datetime_13 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_13") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_13", Boolean.valueOf(ind));

		// extension_14
		valueMap.put("urx_extra_datetime_14", regUser.urx_extra_datetime_14 != null ? regUser.urx_extra_datetime_14 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_14") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_14", Boolean.valueOf(ind));

		// extension_15
		valueMap.put("urx_extra_datetime_15", regUser.urx_extra_datetime_15 != null ? regUser.urx_extra_datetime_15 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_15") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_15", Boolean.valueOf(ind));

		// extension_16
		valueMap.put("urx_extra_datetime_16", regUser.urx_extra_datetime_16 != null ? regUser.urx_extra_datetime_16 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_16") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_16", Boolean.valueOf(ind));

		// extension_17
		valueMap.put("urx_extra_datetime_17", regUser.urx_extra_datetime_17 != null ? regUser.urx_extra_datetime_17 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_17") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_17", Boolean.valueOf(ind));

		// extension_18
		valueMap.put("urx_extra_datetime_18", regUser.urx_extra_datetime_18 != null ? regUser.urx_extra_datetime_18 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_18") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_18", Boolean.valueOf(ind));

		// extension_19
		valueMap.put("urx_extra_datetime_19", regUser.urx_extra_datetime_19 != null ? regUser.urx_extra_datetime_19 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_19") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_19", Boolean.valueOf(ind));

		// extension_20
		valueMap.put("urx_extra_datetime_20", regUser.urx_extra_datetime_20 != null ? regUser.urx_extra_datetime_20 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_datetime_20") != -1)
			ind = true;
		pubIndMap.put("urx_extra_datetime_20", Boolean.valueOf(ind));

		// extension_21
		valueMap.put("urx_extra_singleoption_21", regUser.urx_extra_singleoption_21 != null ? regUser.urx_extra_singleoption_21 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_21") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_21", Boolean.valueOf(ind));

		// extension_22
		valueMap.put("urx_extra_singleoption_22", regUser.urx_extra_singleoption_22 != null ? regUser.urx_extra_singleoption_22 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_22") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_22", Boolean.valueOf(ind));

		// extension_23
		valueMap.put("urx_extra_singleoption_23", regUser.urx_extra_singleoption_23 != null ? regUser.urx_extra_singleoption_23 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_23") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_23", Boolean.valueOf(ind));

		// extension_24
		valueMap.put("urx_extra_singleoption_24", regUser.urx_extra_singleoption_24 != null ? regUser.urx_extra_singleoption_24 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_24") != -1)
			ind = true;
		pubIndMap.put("urx_extra__singleoption24", Boolean.valueOf(ind));

		// extension_25
		valueMap.put("urx_extra_singleoption_25", regUser.urx_extra_singleoption_25 != null ? regUser.urx_extra_singleoption_25 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra__singleoption25") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_25", Boolean.valueOf(ind));

		// extension_26
		valueMap.put("urx_extra_singleoption_26", regUser.urx_extra_singleoption_26 != null ? regUser.urx_extra_singleoption_26 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_26") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_26", Boolean.valueOf(ind));

		// extension_27
		valueMap.put("urx_extra_singleoption_27", regUser.urx_extra_singleoption_27 != null ? regUser.urx_extra_singleoption_27 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_27") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_27", Boolean.valueOf(ind));

		// extension_28
		valueMap.put("urx_extra_singleoption_28", regUser.urx_extra_singleoption_28 != null ? regUser.urx_extra_singleoption_28 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_28") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_28", Boolean.valueOf(ind));

		// extension_29
		valueMap.put("urx_extra_singleoption_29", regUser.urx_extra_singleoption_29 != null ? regUser.urx_extra_singleoption_29 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_29") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_29", Boolean.valueOf(ind));

		// extension_30
		valueMap.put("urx_extra_singleoption_30", regUser.urx_extra_singleoption_30 != null ? regUser.urx_extra_singleoption_30 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_singleoption_30") != -1)
			ind = true;
		pubIndMap.put("urx_extra_singleoption_30", Boolean.valueOf(ind));

		// extension_31
		valueMap.put("urx_extra_multipleoption_31", regUser.urx_extra_multipleoption_31 != null ? regUser.urx_extra_multipleoption_31 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_31") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_31", Boolean.valueOf(ind));

		// extension_32
		valueMap.put("urx_extra_multipleoption_32", regUser.urx_extra_multipleoption_32 != null ? regUser.urx_extra_multipleoption_32 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_32") != -1)
			ind = true;
		pubIndMap.put("urx_extra__multipleoption_32", Boolean.valueOf(ind));

		// extension_33
		valueMap.put("urx_extra_multipleoption_33", regUser.urx_extra_multipleoption_33 != null ? regUser.urx_extra_multipleoption_33 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_33") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_33", Boolean.valueOf(ind));

		// extension_34
		valueMap.put("urx_extra__multipleoption34", regUser.urx_extra_multipleoption_34 != null ? regUser.urx_extra_multipleoption_34 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_34") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_34", Boolean.valueOf(ind));

		// extension_35
		valueMap.put("urx_extra_multipleoption_35", regUser.urx_extra_multipleoption_35 != null ? regUser.urx_extra_multipleoption_35 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_35") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_35", Boolean.valueOf(ind));

		// extension_36
		valueMap.put("urx_extra_multipleoption_36", regUser.urx_extra_multipleoption_36 != null ? regUser.urx_extra_multipleoption_36 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_36") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_36", Boolean.valueOf(ind));

		// extension_37
		valueMap.put("urx_extra_multipleoption_37", regUser.urx_extra_multipleoption_37 != null ? regUser.urx_extra_multipleoption_37 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_37") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_37", Boolean.valueOf(ind));

		// extension_38
		valueMap.put("urx_extra_multipleoption_38", regUser.urx_extra_multipleoption_38 != null ? regUser.urx_extra_multipleoption_38 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_38") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_38", Boolean.valueOf(ind));

		// extension_39
		valueMap.put("urx_extra_multipleoption_39", regUser.urx_extra_multipleoption_39 != null ? regUser.urx_extra_multipleoption_39 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_39") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_39", Boolean.valueOf(ind));

		// extension_40
		valueMap.put("urx_extra_multipleoption_40", regUser.urx_extra_multipleoption_40 != null ? regUser.urx_extra_multipleoption_40 : "");
		ind = false;
		if (options != null && options.indexOf("urx_extra_multipleoption_40") != -1)
			ind = true;
		pubIndMap.put("urx_extra_multipleoption_40", Boolean.valueOf(ind));

		//我当前所在的培训中心
		valueMap.put("usr_template_tcr_id", prof.my_top_tc_id+"");

		valueMap.put("usr_desc_infor", LangLabel.getValue(prof.cur_lan, "lab_main_tcr_desc_content"));
	}

	private void getUserColumns(HttpServletRequest request, Vector<Object> vColName, Vector<Object> vColType, Vector<Object> vColValue, Vector<Object> vExtColName,
			Vector<Object> vExtColType, Vector<Object> vExtColValue) {
		Timestamp t = null;
		String param = null;
		String val = null;

		param = "usr_id";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_ste_usr_id");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_pwd";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_pwd");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_ste_usr_id";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_ste_usr_id");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_email";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_email");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_display_bil";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_display_bil");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);

		}

		param = "usr_gender";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_gender");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				vColValue.addElement(val);
			} else {
				vColValue.addElement(null);
			}
		}

		param = "usr_bday";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
			} catch (Exception e) {
				t = null;
			}
		}
		vColName.addElement("usr_bday");
		vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
		vColValue.addElement(t);

		param = "usr_tel_1";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_tel_1");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);

		}
		param = "usr_fax_1";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_fax_1");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_join_date";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
			} catch (Exception e) {
				t = null;
			}
		}
		vColName.addElement("usr_join_datetime");
		vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
		vColValue.addElement(t);
		
		param = "usr_job_title";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_job_title");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_source";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_source");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}
		// 扩展字段
		param = "urx_extra_41";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_41");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "urx_extra_42";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_42");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "urx_extra_43";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_43");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "urx_extra_44";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_44");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_45";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_45");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "usr_hkid";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_hkid");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}
		param = "usr_nickname";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_nickname");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}
		param = "usr_extra_1";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_1");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_2";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_2");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_3";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_3");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_4";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_4");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_5";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_5");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_6";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_6");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_7";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_7");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_8";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_8");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_9";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_9");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);
		}

		param = "usr_extra_10";
		val = request.getParameter(param);
		if (val != null) {
			vColName.addElement("usr_extra_10");
			vColType.addElement(DbTable.COL_TYPE_STRING);
			vColValue.addElement(val);

		}
		
		param = "urx_extra_datetime_11";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_11");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_12";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_12");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_13";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_13");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_14";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_14");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_15";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_15");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_16";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_16");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_17";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_17");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_18";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_18");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_19";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_19");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_datetime_20";
		val = request.getParameter(param);
		if (val != null) {
			try {
				t = cwUtils.parse(val, cwUtils.DEFAULT_DATE_FORMAT);
				vExtColName.addElement("urx_extra_datetime_20");
				vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
				vExtColValue.addElement(t);
			} catch (Exception e) {
				t = null;
			}
		}

		param = "urx_extra_singleoption_21";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_21");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_22";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_22");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_23";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_23");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_24";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_24");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_25";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_25");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_26";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_26");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_27";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_27");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_28";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_28");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_29";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_29");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_singleoption_30";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_singleoption_30");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "urx_extra_multipleoption_31";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_31");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_multipleoption_32";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_32");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_multipleoption_33";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_33");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_multipleoption_34";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_34");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_multipleoption_35";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_35");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_multipleoption_36";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_36");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}
		param = "urx_extra_multipleoption_37";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_37");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "urx_extra_multipleoption_38";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_38");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "urx_extra_multipleoption_39";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_39");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "urx_extra_multipleoption_40";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("urx_extra_multipleoption_40");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		// 其他字段
		param = "usr_edu_role";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("usr_edu_role");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "usr_competency";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("usr_competency");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "direct_supervisors";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("direct_supervisors");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "app_approval_usg_ent_id";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("app_approval_usg_ent_id");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

		param = "supervised_groups";
		val = request.getParameter(param);
		if (val != null) {
			vExtColName.addElement("supervised_groups");
			vExtColType.addElement(DbTable.COL_TYPE_STRING);
			vExtColValue.addElement(val);
		}

	}

	public static String toJsonDateFormat(Timestamp time) {
		if (time == null)
			return "";
		return time.toString().substring(0, 10);
	}


}