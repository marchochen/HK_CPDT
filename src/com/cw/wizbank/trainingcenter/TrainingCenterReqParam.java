/*
 * Created on 2004-9-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.trainingcenter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TrainingCenterReqParam extends ReqParam {
	private DbTrainingCenter obj = null;
	private String url_suc = null;
	private String prmNm = null;
	public static final String deli = "~";
	private Map officers = null;
	private long[] target_group;
	private String rol;
	private Hashtable params = null;
	public boolean is_from_homepage = false;
	public boolean is_tc_upd = false;
	public long tcr_id = 0;
	public String url_failure1;
	public String style_lang;
	public String main_color;
	public String main_border_color;
	public String main_font_color;
	public String sub_color;
	public String sub_border_color;
	public String sub_font_color;
	public String font_family;
	public int banner_image;
	public String banner_image_dir;
	public int footer_image;
	public String footer_image_dir;

	public TrainingCenterReqParam() {
	}
	
	public TrainingCenterReqParam(ServletRequest inReq, String clientEnc_, String encoding_) throws cwException {
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		common();
		return;
	}

	public void oneCenterId(loginProfile prof) throws cwException {
		String var;

		prmNm = "tcr_id";
		var = getParam(prmNm);
		if (var.length() > 0) {
			tcr_id = Long.parseLong(var);
			obj = new DbTrainingCenter(tcr_id);
		}

		prmNm = "s_role_types";
		var = getParam(prmNm);
		if (var.length() > 0) {
			rol = var;
		}

		if (obj != null) {
			obj.setTcr_ste_ent_id(prof.root_ent_id);
		}
		prmNm = "url_success";
		var = getParam(prmNm);
		if (var.length() > 0) {
			url_suc = var;
		}

		prmNm = "is_tc_upd";
		is_tc_upd = getBooleanParameter(prmNm);

		prmNm = "lang";
		style_lang = getStringParameter(prmNm);

		prmNm = "main_color";
		main_color = getStringParameter(prmNm);

		prmNm = "main_border_color";
		main_border_color = getStringParameter(prmNm);

		prmNm = "main_font_color";
		main_font_color = getStringParameter(prmNm);

		prmNm = "sub_color";
		sub_color = getStringParameter(prmNm);

		prmNm = "sub_border_color";
		sub_border_color = getStringParameter(prmNm);

		prmNm = "sub_font_color";
		sub_font_color = getStringParameter(prmNm);

		prmNm = "font_family";
		font_family = getStringParameter(prmNm);

		prmNm = "banner_image";
		banner_image = getIntParameter(prmNm);

		prmNm = "footer_image";
		footer_image = getIntParameter(prmNm);

		prmNm = "banner_image_file";
		banner_image_dir = (bMultipart) ? multi.getFilesystemName("banner_image_file") : null;

		prmNm = "footer_image_file";
		footer_image_dir = (bMultipart) ? multi.getFilesystemName("footer_image_file") : null;

	}
	 
	public DbTrainingCenter addUpdNewTc(loginProfile prof, boolean isUpd) throws cwException {
		String var;
		String name_pre = "role_";
		int role_num = 0;
		params = new Hashtable();

		prmNm = "tc_id";
		var = getParam(prmNm);
		if (isUpd) {
			obj = new DbTrainingCenter(Long.parseLong(var));
		} else {
			obj = new DbTrainingCenter(0);
		}
		params.put(prmNm, var);

		obj.setTcr_ste_ent_id(prof.root_ent_id);
		obj.setTcr_create_usr_id(prof.usr_id);
		obj.setTcr_update_usr_id(prof.usr_id);

		prmNm = "tc_code";
		var = unicode(getParam(prmNm));
		if (var.length() > 0) {
			obj.setTcr_code(var);
		}
		params.put(prmNm, var);

		prmNm = "tc_name";
		var = unicode(getParam(prmNm));
		if (var.length() > 0) {
			obj.setTcr_title(var);
		}
		params.put(prmNm, var);

		prmNm = "upd_time";
		var = getParam(prmNm);
		if (var.length() > 0) {
			obj.setTcr_update_timestamp(Timestamp.valueOf(var));
		}
		params.put(prmNm, var);

		prmNm = "role_num";
		var = getParam(prmNm);
		if (var.length() > 0) {
			role_num = Integer.parseInt(var);
		}
		params.put(prmNm, var);
		officers = getOfficerLst(role_num);

		prmNm = "usg_lst";
		var = getParam(prmNm);
		if (var.length() > 0) {
			target_group = cwUtils.splitToLong(var, deli);
		}
		params.put(prmNm, var);

		prmNm = "mgt_user_rdo";
		boolean user_mgt_ind = getBooleanParameter(prmNm);
		obj.setTcr_user_mgt_ind(user_mgt_ind);

		prmNm = "parent_tcr_id";
		long parent_tcr_id = getLongParameter(prmNm);
		obj.setParent_tcr_id(parent_tcr_id);

		params.put("url_success", url_success);

		prmNm = "url_failure1";
		url_failure1 = getParam(prmNm);
		
		return obj;
	}
	 
	private Map getOfficerLst(int n) {
		Map map = new HashMap();
		String key;
		String tmpValue;
		long[] tmpLst;
		for (int i = 1; i <= n; i++) {
			key = getParam("role_" + i);
			tmpValue = getParam(key + "_usr_lst");
			if (tmpValue.length() > 0) {
				tmpLst = cwUtils.splitToLong(tmpValue, deli);
				map.put(key, tmpLst);
			}
			params.put("role_" + i, key);
			params.put(key + "_usr_lst", tmpValue);
		}
		return map;
	}

	private String getParam(String name) {
		return req.getParameter(name) == null ? "" : req.getParameter(name);
	}

	public DbTrainingCenter getObj() {
		return obj;
	}

	public String getUrl_suc() {
		return url_suc;
	}

	public Map getOfficers() {
		return officers;
	}

	public long[] getTarget_group() {
		return target_group;
	}

	public String getRol() {
		if (rol == null) {
			rol = "";
		}
		return rol;
	}

	public Hashtable getParams() {
		return this.params;
	}

}