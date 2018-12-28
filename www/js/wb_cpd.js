function wbCpd() {
	this.del_cpd = wbCpdDel;
	this.ins_cpd_prep = wbCpdIns;// add prep
	this.upd_cpd_prep = wbCpdUpd;// 
	this.ins_upd_cpd_exec = wbCpdExec;// submit(check form)
	this.show_license_info = wbShowLicenseInfo;
	this.declaration_upload = wbDeclarationUpload;
	this.declaration_upload_preview = wbDeclarationUploadPreview;
	this.declaration_generate_rpt = wbDeclarationGenerateRpt;
	this.declaration_generate_form = wbDeclarationGenerateForm;
	this.declaration_send_form = wbDeclarationSendForm;
	this.declaration_step = wbDeclarationStep;
	this.enter_rpt_period = wbEnterRptPeriod;
	this.rpt_list = wbRptList;

	this.ins_rpt_period = wbInsRptPeriod;
	this.upd_rpt_period = wbUpdRptPeriod;
	this.ins_upd_rpt_period_exec = wbInsUpdRptPeriodExec;
	this.del_rpt_period = wbDelRptPeriod;

	this.ins_rpt = wbInsRpt;
	this.upd_rpt = wbUpdRpt;
	this.ins_upd_rpt_exec = wbInsUpdRptExec;
	this.del_rpt = wbDelRpt;

	this.enter_rpt = wbEnterRpt;

	this.del_file = wbDelFile
	this.add_file_location = wbAddFileLocation
	this.remove_file_location = wbRemoveFileLocation
	this.change_cdf_file = wbChangeCdfFile
}

function wbShowLicenseInfo(type) {
	if (type == 0) {
		document.getElementById("mi_div").style.display = "none";
		document.getElementById("ii_div").style.display = "none";
		document.getElementById("ri_div").style.display = "none";
		document.getElementById("requirement_ri_div").style.display = "none";
		document.getElementById("requirement_mi_ii_div").style.display = "none";

	} else if (type == "RI") {
		if (document.getElementById("ri_div").style.display == "none") {
			document.getElementById("ri_div").style.display = "";// 显示
			document.getElementById("mi_div").style.display = "none";
			document.getElementById("ii_div").style.display = "none";

			document.getElementById("requirement_mi_ii_div").style.display = "";
			document.getElementById("requirement_ri_div").style.display = "";

			document.getElementById("mi_ii_core_unit_div").style.display = "none";
			document.getElementById("ri_core_unit_div").style.display = "";
			document.getElementById("mi_ii_non_core_unit_div").style.display = "none";
			document.getElementById("ri_non_core_unit_div").style.display = "";
		}
	} else if (type == "MI") {
		if (document.getElementById("mi_div").style.display == "none") {
			document.getElementById("mi_div").style.display = "";// 显示
			document.getElementById("ii_div").style.display = "none";
			document.getElementById("ri_div").style.display = "none";

			document.getElementById("requirement_ri_div").style.display = "none";
			document.getElementById("requirement_mi_ii_div").style.display = "";

			document.getElementById("mi_ii_core_unit_div").style.display = "";
			document.getElementById("ri_core_unit_div").style.display = "none";
			document.getElementById("mi_ii_non_core_unit_div").style.display = "";
			document.getElementById("ri_non_core_unit_div").style.display = "none";
		}
	} else if (type == "II") {
		if (document.getElementById("ii_div").style.display == "none") {
			document.getElementById("ii_div").style.display = "";// 显示
			document.getElementById("mi_div").style.display = "none";
			document.getElementById("ri_div").style.display = "none";

			document.getElementById("requirement_ri_div").style.display = "none";
			document.getElementById("requirement_mi_ii_div").style.display = "";

			document.getElementById("mi_ii_core_unit_div").style.display = "";
			document.getElementById("ri_core_unit_div").style.display = "none";
			document.getElementById("mi_ii_non_core_unit_div").style.display = "";
			document.getElementById("ri_non_core_unit_div").style.display = "none";
		}
	}
}
function wbCpdIns() {
	url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'ins_cpd_prep', 'stylesheet', 'cpd_form.xsl');
	parent.location.href = url;
}
function wbCpdUpd(cpd_id) {
	url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'upd_cpd_prep', 'stylesheet', 'cpd_edit_form.xsl', 'cpd_id', cpd_id);
	parent.location.href = url;
}
function wbCpdDel(cpd_id, lang) {
	if (!confirm(wb_cpd_delete_confirm)) {
		return;
	}

	url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'del_cpd', 'cpd_id', cpd_id, 'url_success', wbCpdListUrl(), 'url_failure', wbCpdListUrl());
	parent.location.href = url;
}
function wbCpdListUrl() {
	return wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'get_cpd_list', 'stylesheet', 'cpd_list.xsl')
}

function wbCpdExec(frm, lang, cpd_id) {
	// 检查License Type
	var obj = frm.cpd_license;
	var type;
	if (cpd_id > 0) {
		type = frm.cpd_license.value;
		frm.cpd_id.value = cpd_id;
	} else {
		type = obj.options[obj.selectedIndex].value;
	}

	if (type == 0) {
		alert(wb_cpd_select_cpd_type);
		return false;
	}
	frm.cpd_license.value = type;
	// 检查Effective Date
	if (frm.eff_date_yy && frm.eff_date_mm && frm.eff_date_dd && frm.cpd_effective_date) {
		var lab_effective_date = frm.lab_effective_date ? frm.lab_effective_date.value : 'Effective Date(sys)'
		if (frm.eff_date_dd.value == "" && frm.eff_date_mm.value == "" && frm.eff_date_yy.value == "") {
			wbUtilsValidateDate('document.' + frm.name.toString() + '.eff_date', lab_effective_date, '')
			return false;
		} else {
			if (!wbUtilsValidateDate('document.' + frm.name.toString() + '.eff_date', lab_effective_date, '')) {
				return false;
			} else {
				frm.cpd_effective_date.value = frm.eff_date_yy.value + "-" + frm.eff_date_mm.value + "-" + frm.eff_date_dd.value + " " + "00:00:00";
			}
		}
	}
	// 检查Assessment Period (Month)
	var month = /^[0-9]*[1-9][0-9]*$/;// 正整数
	if (!month.test(frm.cpd_period.value)) {
		alert(wb_cpd_period_enter_int_value);
		frm.cpd_period.focus();
		return false;
	}
	var count = 0;
	frm.cdi_months_hours_str.value = '';
	if (type == 'MI') {
		// 检查Requirement
		if (!gen_validate_float(frm.cpd_core_hours.value, wb_cpd_requirement_enter_float_value)) {
			frm.cpd_core_hours.focus();
			return false;
		}
		if (!gen_validate_float(frm.cpd_non_core_hours.value, wb_cpd_requirement_enter_float_value)) {
			frm.cpd_non_core_hours.focus();
			return false;
		}
		// 合并字符串
		count = 12;
		for (i = 1; i <= count; i++) {
			var core = document.getElementById('MI_' + i + '_cdi_core_hours').value;
			var non_core = document.getElementById('MI_' + i + '_cdi_non_core_hours').value;
			var core_2 = document.getElementById('MI_' + i + '_cdi_core_hours');
			var non_core_2 = document.getElementById('MI_' + i + '_cdi_non_core_hours');
			var cdiId = core_2.getAttribute('cdi_id');
			
			if(i >= frm.cpd_period.value){
				if(core <= 0 || non_core <= 0){
					if(!window.confirm(wb_cpd_core_not_null_by_month.replace("[month]",i))){
						return false;
					}
				}
			}
			if (Trim(core) == '') {
				alert(wb_cpd_calculation_enter_core_non_null + '(' + i + wb_cpd_calculation_enter_core_month + ')');
				core_2.focus();
				return false;
			}
			if (core != '') {
				if (!gen_validate_float(core, wb_cpd_core_pls_enter_float_value + '(' + i + wb_cpd_calculation_enter_core_month + ')')) {
					core_2.focus();
					return false;
				}
//				if (Number(core) > Number(frm.cpd_core_hours.value)) {
//					alert(wb_cpd_calculation_enter_check_core_size + frm.cpd_core_hours.value + '(' + i + wb_cpd_calculation_enter_core_month + ')');
//					core_2.select();
//					return false;
//				}
			}
			if (Trim(non_core) == '') {
				alert(wb_cpd_calculation_enter_non_core_non_null + '(' + i + wb_cpd_calculation_enter_core_month + ')');
				non_core_2.focus();
				return false;
			}
			if (non_core != '') {
				if (!gen_validate_float(non_core, wb_cpd_non_core_pls_enter_float_value + '(' + i + wb_cpd_calculation_enter_core_month + ')')) {
					non_core_2.focus();
					return false;
				}
//				if (Number(non_core) > Number(frm.cpd_non_core_hours.value)) {
//					alert(wb_cpd_calculation_enter_check_non_core_size + frm.cpd_non_core_hours.value + '(' + i + wb_cpd_calculation_enter_core_month + ')');
//					non_core_2.select();
//					return false;
//				}
			}
			frm.cdi_months_hours_str.value += i + '#' + core + '#' + non_core + '#' + cdiId;
			frm.cdi_months_hours_str.value += ",";
		}
	} else if (type == 'II') {
		// 检查Requirement
		if (!gen_validate_float(frm.cpd_core_hours.value, wb_cpd_requirement_enter_float_value)) {
			frm.cpd_core_hours.focus();
			return false;
		}
		if (!gen_validate_float(frm.cpd_non_core_hours.value, wb_cpd_requirement_enter_float_value)) {
			frm.cpd_non_core_hours.focus();
			return false;
		}
		count = 24;
		for (i = 1; i <= count; i++) {
			var core = document.getElementById('II_' + i + '_cdi_core_hours').value;
			var non_core = document.getElementById('II_' + i + '_cdi_non_core_hours').value;
			var core_2 = document.getElementById('II_' + i + '_cdi_core_hours');
			var non_core_2 = document.getElementById('II_' + i + '_cdi_non_core_hours');
			var cdiId = core_2.getAttribute('cdi_id');
			
			if(i >= frm.cpd_period.value){
				if(core <= 0 || non_core <= 0){
					if(!window.confirm(wb_cpd_core_not_null_by_month.replace("[month]",i))){
						return false;
					}
				}
			}
			if (Trim(core) == '') {
				alert(wb_cpd_calculation_enter_core_non_null + '(' + i + wb_cpd_calculation_enter_core_month + ')');
				core_2.focus();
				return false;
			}
			if (core != '') {
				if (!gen_validate_float(core, wb_cpd_core_pls_enter_float_value + '(' + i + wb_cpd_calculation_enter_core_month + ')')) {
					core_2.focus();
					return false;
				}
//				if (Number(core) > Number(frm.cpd_core_hours.value)) {
//					alert(wb_cpd_calculation_enter_check_core_size + frm.cpd_core_hours.value + '(' + i + wb_cpd_calculation_enter_core_month + ')');
//					core_2.select();
//					return false;
//				}
			}
			if (Trim(non_core) == '') {
				alert(wb_cpd_calculation_enter_non_core_non_null + '(' + i + wb_cpd_calculation_enter_core_month + ')');
				non_core_2.focus();
				return false;
			}
			if (non_core != '') {
				if (!gen_validate_float(non_core, wb_cpd_non_core_pls_enter_float_value + '(' + i + wb_cpd_calculation_enter_core_month + ')')) {
					non_core_2.focus();
					return false;
				}
//				if (Number(non_core) > Number(frm.cpd_non_core_hours.value)) {
//					alert(wb_cpd_calculation_enter_check_non_core_size + frm.cpd_non_core_hours.value + '(' + i + wb_cpd_calculation_enter_core_month + ')');
//					non_core_2.select();
//					return false;
//				}
			}
			frm.cdi_months_hours_str.value += i + '#' + core + '#' + non_core + '#' + cdiId;
			frm.cdi_months_hours_str.value += ",";
		}
	} else if (type = "RI") {
		// 检查Requirement
		if (!gen_validate_float(frm.cpd_core_hours.value, wb_cpd_requirement_enter_float_value)) {
			frm.cpd_core_hours.focus();
			return false;
		}
		if (!gen_validate_float(frm.cpd_non_core_hours.value, wb_cpd_requirement_enter_float_value)) {
			frm.cpd_non_core_hours.focus();
			return false;
		}
		/*
		 * if(!gen_validate_float(frm.ri_requirement_hours.value,wb_cpd_requirement_enter_float_value)){
		 * frm.ri_requirement_hours.focus(); return false; } else{
		 * frm.cpd_core_hours.value = frm.ri_requirement_hours.value; }
		 */
	}

	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = wbCpdListUrl();
	frm.url_failure.value = wbCpdListUrl();
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}
function Trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function gen_validate_float(fld, msg) {
	var val = wbUtilsTrimString(fld);
	if ((val.search(/^\d{1,9}$/) == -1 && val.search(/^\d{1,9}\.\d{1,2}$/) == -1)) {
		alert(msg);
		return false;
	}
	return true;
}

function wbDeclarationUploadPreview(frm, lang, cdt_id) {
	if (frm.src_file && frm.src_file.length) {
		var length = frm.src_file.length;
		for (var i = 0; i < length; i++) {
			if (!_wbValidFile(lang, frm.src_file[i], frm.src_filename[i])) {
				return false;
			}
		}
	} else {
		if (frm.src_file && !_wbValidFile(lang, frm.src_file, frm.src_filename)) {
			return false;
		}
	}

	frm.stylesheet.value = 'cpd_hkfi_rpt.xsl';
	frm.cmd.value = 'hkfi_rpt_upload_preview';
	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = wbDeclarationGetURL(cdt_id, 2);
	frm.url_failure.value = wbDeclarationGetURL(cdt_id, 1);
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function _wbValidFile(lang, inputFile, inputFilename) {
	var fileName = _wbGetDeclarationFileName(inputFile.value);
	if (_wbValidXlsFile(lang, fileName, inputFile, inputFilename)) {
		return true;
	} else {
		return false;
	}
}

function _wbValidXlsFile(lang, fileName, inputFile, inputFilename) {
	if (fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase() != 'xls') {
		alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
		return false;
	} else {
		inputFilename.value = fileName;
		return true;
	}
}

function wbDeclarationUpload(frm, lang, id) {
	frm.cmd.value = 'hkfi_rpt_upload';
	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = wbDeclarationGetURL(id, 2);
	frm.url_failure.value = wbDeclarationGetURL(id, 1);
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function wbDeclarationGenerateRpt(frm, lang, id) {
	frm.cmd.value = 'hkfi_rpt_generate_rpt_xml';
	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = wbDeclarationGetURL(id, 3);
	frm.url_failure.value = wbDeclarationGetURL(id, 2);
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;

	$(frm).ajaxSubmit({
		url : Wzb.uri_dis,
		dataType : "xml",
		success : function(resultXml) {
			onChange(resultXml);
		},
		error : function(req, options) {
			alert("There was a problem retrieving the XML data:\n");
		},
		method : 'POST'
	});
	$('#descidv').show();
}

function wbDeclarationGenerateForm(frm, lang, id) {
	frm.cmd.value = 'hkfi_rpt_generate_form_xml';
	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = wbDeclarationGetURL(id, 5);
	frm.url_failure.value = wbDeclarationGetURL(id, 4);
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	$(frm).ajaxSubmit({
		url : Wzb.uri_dis,
		success : function(resultXml) {
			onChange(resultXml);
			return;
		},
		error : function(req, options) {
			alert("There was a problem retrieving the XML data:\n");
		},
		method : 'POST'
	});
	$('#formdiv').hide();
	$('#descidv').show();
}

function onChange(resultXml) {
	if (resultXml.getElementsByTagName('error')[0] == null) {
		var success = resultXml.getElementsByTagName('success')[0].firstChild.nodeValue;
		if (success == 'true') {
			document.location = frm.url_success.value;
		}
		return;
	} else {
		var error_msg = resultXml.getElementsByTagName('error')[0].firstChild.nodeValue;
		alert(error_msg);
		document.location = frm.url_failure.value;
	}
}

function wbDeclarationSendForm(frm, lang, id) {
	if (frm.cdt_form_sent_opt[0].checked) {
		if ((frm.form_deadline_yy.value != "" && frm.form_deadline_yy.value != null) 
				|| (frm.form_deadline_mm.value != "" && frm.form_deadline_mm.value != null)
				|| (frm.form_deadline_dd.value != "" && frm.form_deadline_dd.value != null)) {
			if (!wbUtilsValidateDate("document." + frm.name + ".form_deadline", frm.lab_cdt_form_deadline.value)) {
				return false;
			}
			frm.cdt_form_deadline.value += frm.form_deadline_yy.value + "-" + frm.form_deadline_mm.value + "-" + frm.form_deadline_dd.value + " 00:00:00.000";
		} 
		if (!wbUtilsValidateEmptyField(frm.cdt_form_deadline, frm.lab_cdt_form_deadline.value)) {
			return;
		}
	}
	
	frm.cmd.value = 'hkfi_rpt_send_form';
	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = wbDeclarationGetURL(id, 6);
	frm.url_failure.value = wbDeclarationGetURL(id, 6);
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function wbDeclarationStep(id, step) {
	window.location = wbDeclarationGetURL(id, step);
}

function wbInsRptPeriod() {
	var url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_period_ins_upd', 'stylesheet', 'cpd_hkfi_rpt_period_ins_upd.xsl')
	window.location = url;
}

function wbUpdRptPeriod(cdp_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_period_ins_upd', 'stylesheet', 'cpd_hkfi_rpt_period_ins_upd.xsl', 'cdp_id', cdp_id)
	window.location = url;
}

function wbDelRptPeriod(id, lang) {
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
		var url_success = wbDeclarationPeriodGetURL();
		var url_failure = wbDeclarationPeriodGetURL();
		var url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_period_del', 'id', id, 'url_success', url_success, 'url_failure', url_failure)
		window.location.href = url
	}
}

function wbInsUpdRptPeriodExec(frm, lang) {
	if (frm.cdp_start_date_yy && frm.cdp_start_date_mm && frm.cdp_start_date_dd && frm.cdp_start_date) {
		var lab_rpt_period = frm.lab_rpt_period ? frm.lab_rpt_period.value : 'Report Period(sys)'
		if (frm.cdp_start_date_dd.value == "" && frm.cdp_start_date_mm.value == "" && frm.cdp_start_date_yy.value == "") {
			wbUtilsValidateDate('document.' + frm.name.toString() + '.cdp_start_date', lab_rpt_period, '')
			return false;
		} else {
			if (!wbUtilsValidateDate('document.' + frm.name.toString() + '.cdp_start_date', lab_rpt_period, '')) {
				return false;
			} else {
				frm.cdp_start_date.value = frm.cdp_start_date_yy.value + "-" + frm.cdp_start_date_mm.value + "-" + frm.cdp_start_date_dd.value + " " + "00:00:00";
			}
		}
	}
	if (frm.cdp_end_date_yy && frm.cdp_end_date_mm && frm.cdp_end_date_dd && frm.cdp_end_date) {
		var lab_rpt_period = frm.lab_rpt_period ? frm.lab_rpt_period.value : 'Report Period(sys)'
		if (frm.cdp_end_date_dd.value == "" && frm.cdp_end_date_mm.value == "" && frm.cdp_end_date_yy.value == "") {
			wbUtilsValidateDate('document.' + frm.name.toString() + '.cdp_end_date', lab_rpt_period, '')
			return false;
		} else {
			if (!wbUtilsValidateDate('document.' + frm.name.toString() + '.cdp_end_date', lab_rpt_period, '')) {
				return false;
			} else {
				frm.cdp_end_date.value = frm.cdp_end_date_yy.value + "-" + frm.cdp_end_date_mm.value + "-" + frm.cdp_end_date_dd.value + " " + "23:59:59";
			}
		}
	}

	if (frm.cdp_start_date_yy && frm.cdp_start_date_yy.value.length > 0 && frm.cdp_end_date_yy.value.length > 0) {
		if (!wb_utils_validate_date_compare({
					frm : 'document.' + frm.name,
					start_obj : 'cdp_start_date',
					end_obj : 'cdp_end_date'
				})) {
			return false;
		}
	}

	frm.cmd.value = 'hkfi_rpt_period_ins_upd_exec';
	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = wbDeclarationPeriodGetURL();
	frm.url_failure.value = wbDeclarationPeriodGetURL();
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function _wbGetDeclarationFileName(pathname) {
	s = pathname.lastIndexOf("\\");
	if (s == -1) {
		s = pathname.lastIndexOf("/");
	}
	if (s == -1) {
		return pathname;
	}
	l = pathname.length - s;
	return pathname.substr(s + 1, l);
}

function wbDeclarationGetURL(cdt_id, step) {
	return wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt', 'stylesheet', 'cpd_hkfi_rpt.xsl', 'step', step, 'cdt_id', cdt_id)
}

function wbDeclarationPeriodGetURL(id, step) {
	return wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_period', 'stylesheet', 'cpd_hkfi_rpt_period_list.xsl', 'step', step, 'id', id)
}

function wbEnterRptPeriod(frm, lang) {
	var field = getRadioSelect(frm.rpt_period_id_lst, frm.lab_specify_a_period.value);
	if (field == null) {
		return;
	}
	var id = field.value;
	window.location = _wbRptListURL(id);
}

function wbRptList(id) {
	window.location = _wbRptListURL(id);
}

function _wbRptListURL(id) {
	return wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_list', 'stylesheet', 'cpd_hkfi_rpt_list.xsl', 'cdp_id', id)
}

function wbInsRpt(cdp_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_ins_upd', 'stylesheet', 'cpd_hkfi_rpt_ins_upd.xsl', 'cdp_id', cdp_id)
	window.location = url;
}

function wbUpdRpt(cdp_id, cdt_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_ins_upd', 'stylesheet', 'cpd_hkfi_rpt_ins_upd.xsl', 'cdp_id', cdp_id, 'cdt_id', cdt_id)
	window.location = url;
}

function wbDelRpt(cdt_id, cdp_id, lang) {
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
		var url_success = _wbRptListURL(cdp_id);
		var url_failure = _wbRptListURL(cdp_id);
		var url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_rpt_del', 'id', cdt_id, 'url_success', url_success, 'url_failure', url_failure)
		window.location.href = url;
	}
}

function wbInsUpdRptExec(frm, lang, cdp_id) {
	frm.cmd.value = 'hkfi_rpt_ins_upd_exec';
	frm.module.value = 'cpd.CpdModule';
	frm.url_success.value = _wbRptListURL(cdp_id);
	frm.url_failure.value = _wbRptListURL(cdp_id);
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function wbEnterRpt(frm, lang) {
	var field = getRadioSelect(frm.rpt_id_lst, frm.lab_specify_a_hkfi_rpt.value);
	if (field == null) {
		return;
	}
	var id = field.value;
	wbDeclarationStep(id, 1);
}

function wbDelFile(cdt_id, cdf_id, lang) {
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
		var url_success = wbDeclarationGetURL(cdt_id, 1);
		var url_failure = wbDeclarationGetURL(cdt_id, 1);
		var url = wb_utils_invoke_disp_servlet('module', 'cpd.CpdModule', 'cmd', 'hkfi_file_del', 'id', cdf_id, 'url_success', url_success, 'url_failure', url_failure)
		window.location.href = url;
	}
}

function getRadioSelect(field, label) {
	var obj = null;
	if (field) {
		if (field.length) {
			for (var i = 0; i < field.length; i++) {
				var temp = field[i];
				if (temp.checked) {
					obj = temp;
					break;
				}
			}
		} else {
			if (field.checked) {
				obj = field;
			}
		}
	}
	if (obj == null) {
		alert(label);
	}
	return obj;
}

function wbAddFileLocation(frm) {
	var lab = frm.lab_file_location.value;
	var lab_remove = frm.lab_btn_remove.value;
	var html = '';
	html += '<tr class="removable">';
	html += '   <td width="20%" align="right">';
	html += '       <span class="TitleText">*' + lab + ':</span>';
	html += '   </td>';
	html += '   <td>';
	html += '<input type="file" name="src_file" class="InputFrm" style="width: 400;" />';
	html += '<input type="hidden" name="src_filename" />';
	html += '&nbsp;<span class="remove_btn_span">';
	html += '<input type="button" name="frmSubmitBtn" value="' + lab_remove + '" class="Btn" ';
	html += 'onclick="javascript: cpd.remove_file_location(document.frmXml);">&nbsp;</span>';
	html += '</td>';
	html += '</tr>';
	$('#file_upload_tbody').append(html);

	$('#file_upload_tbody tr td span[class=remove_btn_span]').hide();
	$('#file_upload_tbody tr:last-child td span[class=remove_btn_span]').show();
}

function wbRemoveFileLocation(frm) {
	$('#file_upload_tbody tr:last-child[class=removable]').remove();
	$('#file_upload_tbody tr:last-child td span[class=remove_btn_span]').show();
}

function wbChangeCdfFile(id, value) {
	$('#' + id).attr('value', value);
}