function wbTcMgt() {
	this.tc_detail = wbTcDetail;
	this.add_tc_prep = wbTcInsPrep;
	this.add_upd_tc_exe = wbTcInsUpdExe;
	this.upd_tc_prep = wbTcUpdPrep;
	this.del_tc_exe = wbTcDelExe;
	this.all_tc_lst = wbTcAllLst;
	this.eff_tc_lst = wbTcEffLst;
	this.tc_lst = wbTcLst;
	this.set_ta_prep = wbTcSetTaPrep;
	this.set_ta_exec = wbTcSetTaExec;
	this.set_tc_style_prep = wbSetTcStylePrep;
	this.set_tc_style_exec = wbSetTcStyleExec;
}

function wbTcLst() {
	var url = _wbReturnTcGenTcPrep("tc_lst", 0, "trainingcenter_list.xsl");
	window.location.href = url;
}

function wbTcAllLst(frm) {
	var url = _wbNomalUrl('tc_all_lst', frm.stylesheet.value, js_name);
	window.location.href = url;
}

function wbTcEffLst(frm) {
	var url = _wbNomalUrl('tc_lst', frm.stylesheet.value, js_name);
	window.location.href = url;
}

function wbTcDetail(id) {
	var url = _wbReturnTcGenGetTcDetail('tc_details', id, "tc_details.xsl", "", wb_utils_gen_home_url());
	window.location.href = url;
}

function wbTcInsPrep(id) {
	var url = _wbReturnTcGenTcPrep("add_tc_prep", id, "tc_ins_upd.xsl");
	window.location.href = url;
}

function wbTcInsUpdExe(frm, id, is_ins_ind, parent_tcr_id) {
	var flag = validate(frm);
	var name_field_post = "_usr_ent_id_lst";
	var value_post = "_usr_lst";
	var tmp_lst = "";
	var tmp_value;
	var tmp_obj;
	if (!flag) {
		return;
	}
	 // get the list of officers
	for (i = 1; i <= frm.role_num.value; i++) {
		tmp_value = eval("frm.role_" + i + ".value");
		tmp_obj = eval("frm." + tmp_value + name_field_post);
		if (tmp_obj) {
			/*
			 * if(tmp_obj.options.length == 0){
			 * alert(eval("wb_msg_tc_"+tmp_value)); return; }
			 */
			for (j = 0; j < tmp_obj.options.length; j++) {
				tmp_lst += tmp_obj.options[j].value;
				if (j != tmp_obj.options.length - 1) {
					tmp_lst += "~";
				}
			}
			// for example: frm.TADM_1_usr_lst 's value will give Java.
			tmp_obj = eval("frm." + tmp_value + value_post);
			tmp_obj.value = tmp_lst;
		}
		tmp_lst = "";
	}
	 // get the list of target group
	/*
	 * if(frm.usg_ent_id_lst.options.length == 0){
	 * alert(eval("wb_msg_tc_target_group")); return; }
	 */
	if (frm.usg_ent_id_lst) {
		for (i = 0; i < frm.usg_ent_id_lst.options.length; i++) {
			tmp_lst += frm.usg_ent_id_lst.options[i].value;
			if (i != frm.usg_ent_id_lst.options.length - 1) {
				tmp_lst += "~";
			}
			frm.usg_lst.value = tmp_lst;
			// alert(frm.usg_lst.value);
		}
	}
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	if (is_ins_ind == 'true') {
		frm.cmd.value = "add_tc_exe";
		frm.parent_tcr_id.value = id;
		frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_details', 'stylesheet', 'tc_details.xsl', 'tcr_id', id);
	} else {
		frm.cmd.value = "upd_tc_exe";
		frm.parent_tcr_id.value = parent_tcr_id;
		frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_details', 'stylesheet', 'tc_details.xsl', 'tcr_id', id);
	}
	frm.module.value = "trainingcenter.TrainingCenterModule";

	frm.url_failure.value = window.location.href;
	frm.url_failure1.value = wb_utils_gen_home_url();
	frm.stylesheet.value = 'tc_upd_msg_box.xsl'
	frm.submit();
}

function wbTcUpdPrep(id) {
	var url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'upd_tc_prep', 'tcr_id', id, 'stylesheet', 'tc_ins_upd.xsl', 'is_tc_upd', true);
	window.location.href = url;
}

function wbTcDelExe(id, parent_tcr_id) {
	var url_suc = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_details', 'stylesheet', 'tc_details.xsl', 'tcr_id', parent_tcr_id);
	var url_fail = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_details', 'stylesheet', 'tc_details.xsl', 'tcr_id', id);
	var url = _wbReturnTcGenGetTcDetail("tc_del", id, "", url_suc, url_fail);
	if (confirm(wb_msg_tc_del_confirm)) {
		window.location.href = url;
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function validate(frm) {
	if (frm.tc_code) {
		frm.tc_code.value = wbUtilsTrimString(frm.tc_code.value);
		if (!wbUtilsValidateEmptyField(frm.tc_code, eval("wb_msg_tc_code_name"))) {
			return false;
		}
		if(getChars(frm.tc_code.value) > 20)
		{
			alert(wb_cert_core_length);
			return false;
		}
	}
	if (frm.tc_name) {
		frm.tc_name.value = wbUtilsTrimString(frm.tc_name.value);
		if (!wbUtilsValidateEmptyField(frm.tc_name, eval("wb_msg_tc_title_name"))) {
			return false;
		}
		if(getChars(frm.tc_name.value) > 80)
		{
			alert(wb_msg_gb_title_length);
			return false;
		}
	}
	return true;
}

function _wbNomalUrl(cmd, stylesheet, value1) {
	var url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', cmd, 'stylesheet', stylesheet, 'js_name', value1);
	return url;
}

function _wbReturnTcGenGetTcDetail(cmd, tcr_id, stylesheet, url_suc, url_fail) {
	var url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', cmd, 'tcr_id', tcr_id, 'stylesheet', stylesheet, 'url_success', url_suc, 'url_failure', url_fail);
	return url;
}

function _wbReturnTcGenTcPrep(cmd, id, stylesheet) {
	var url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', cmd, 'tcr_id', id, 'stylesheet', stylesheet);
	return url;
}

function wbTcSetTaPrep(id) {
	var url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'set_ta_prep', 'tcr_id', id, 'stylesheet', 'tc_set_ta.xsl');
	window.location.href = url;
}

function wbTcSetTaExec(frm, id) {
	var name_field_post = "_usr_ent_id_lst";
	var value_post = "_usr_lst";
	var tmp_lst = "";
	var tmp_value;
	var tmp_obj;
	// get the list of officers
	for (i = 1; i <= frm.role_num.value; i++) {
		tmp_value = eval("frm.role_" + i + ".value");
		tmp_obj = eval("frm." + tmp_value + name_field_post);
		if (tmp_obj) {
			for (j = 0; j < tmp_obj.options.length; j++) {
				tmp_lst += tmp_obj.options[j].value;
				if (j != tmp_obj.options.length - 1) {
					tmp_lst += "~";
				}
			}
			// for example: frm.TADM_1_usr_lst 's value will give Java.
			tmp_obj = eval("frm." + tmp_value + value_post);
			tmp_obj.value = tmp_lst;
		}
		tmp_lst = "";
	}
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.cmd.value = "set_ta_exec";
	frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_details', 'tcr_id', id, 'stylesheet', 'tc_details.xsl');
	frm.module.value = "trainingcenter.TrainingCenterModule";
	frm.url_failure.value = window.location.href;
	frm.url_failure1.value = wb_utils_gen_home_url();
	frm.stylesheet.value = 'tc_upd_msg_box.xsl'
	frm.submit();
}

function wbSetTcStylePrep(id, lang) {
	var url = _getTcStyleUrl(id, lang);
	window.location.href = url;
}

function wbSetTcStyleExec(frm, lang) {
	var bannerObj = frm.banner_image_radio;
	for ( var i = 0; i < bannerObj.length; i++) {
		if (bannerObj[i].checked) {
			frm.banner_image.value = bannerObj[i].value;
			break;
		}
	}

	if (frm.banner_image.value == '2') {
		if (frm.banner_image_file.value == '') {
			alert(wb_msg_usr_please_enter_the + frm.lab_884.value);
			return;
		} else if (!wbUtilsValidateImgType(frm.banner_image_file, lang)) {
			return;
		}
	}
	if(frm.footer_image_radio){
		var footerObj = frm.footer_image_radio;
		for(var i=0; i<footerObj.length; i++) {
			if(footerObj[i].checked) {
				frm.footer_image.value = footerObj[i].value;
				break;
			}
		}
	}
	
	if(frm.footer_image){
		if (frm.footer_image.value == '2') {
			if (frm.footer_image_file.value == '') {
				alert(wb_msg_usr_please_enter_the + frm.lab_887.value);
				return;
			} else if (!wbUtilsValidateImgType(frm.footer_image_file, lang)) {
				return;
			}
		}
	}
	frm.method = 'post';
	frm.module.value = "trainingcenter.TrainingCenterModule";
	frm.cmd.value = "set_tc_style_exec";
	frm.url_success.value = _getTcStyleUrl(frm.tcr_id.value, frm.lang.value);
	frm.url_failure.value = _getTcStyleUrl(frm.tcr_id.value, frm.lang.value);
	frm.submit();
}

function _getTcStyleUrl(id, lang) {
	return wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'set_tc_style_prep', 'tcr_id', id, 'stylesheet', 'tc_set_style.xsl', 'lang', lang);
}