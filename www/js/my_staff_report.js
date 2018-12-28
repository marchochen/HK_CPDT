var formRpt = null;
var mystaff = new wbMyStaff;
var goldenman = new wbGoldenMan;
var mgt_rpt = new wbManagementReport;

var gldmanParam = 'goldenman_mystaff_user,goldenman_mystaff_item,goldenman_mystaff_treenode';
var content_lst_value = 'att_create_timestamp~att_status~att_timestamp~cov_commence_datetime~cov_last_acc_datetime~total_attempt~cov_total_time~cov_score';
var itm_content_lst_value = 'field01~field02~itm_type~catalog~training_center';
var usr_content_lst_value = 'usr_id~usr_display_bil~USR_PARENT_USG~USR_CURRENT_UGR~usr_email~usr_tel_1';

var colModel = [ {
	name : 'rsp_id',
	display : getLabel('79'),
	format : function(data) {
		//alert(data.rsp_title.length);
		var sublength = 15;		//页面显示长度
		var title = (data.rsp_title && data.rsp_title.length > sublength) ? (data.rsp_title.substring(0,sublength)+"...") : data.rsp_title;
		p = {
			rsp_id : data.rsp_id,
			rsp_title : title,
			rsp_full_title : data.rsp_title.length>sublength ? data.rsp_title : ""
		};
		return $('#titleTemplate').render(p);
	}
}, {
	name : 'rsp_upd_timestamp',
	display : getLabel('275'),
	width : 120,
	format : function(data) {
		return '<span style=\"white-space: nowrap;\" class=\'wzb-common-text\'>' + Wzb.displayTime(data.rsp_upd_timestamp) + '</span>';
	}
}, {
	name : 'rsp_upd_timestamp',
	display : '',
	format : function(data) {
		return renderViewRptBtn(data);
	}
} ];

function getSpecId(id_values) {
	var len = id_values.length;
	var str = '';
	for ( var j = 0; j < len; j++) {
		str += id_values[j].id;
		if (j !== len - 1) {
			str += d_v;
		}
	}
	return str;
}

function renderViewRptBtn(data) {
	var spec = new Spec();
	var spec_name = [ 'ent_id', 'tnd_id', 'itm_id', 's_usg_ent_id_lst', 'att_create_start_datetime', 'att_create_end_datetime', 'ats_id', 'is_my_staff', 'usr_content_lst',
			'itm_content_lst', 'content_lst' ];

	for ( var i = 0; i < spec_name.length; i++) {
		spec.name += spec_name[i] + d_f;
		var tmp_value = '';
		switch (spec_name[i]) {
		case 'ent_id':
			var id_tmp = data.gmUsrOption;
			if (id_tmp !== undefined && id_tmp !== '') {
				tmp_value = getSpecId(id_tmp['value']);
			}
			break;
		case 'tnd_id':
			var id_tmp = data.gmTndOption;
			if (id_tmp !== undefined && id_tmp !== '') {
				tmp_value = getSpecId(id_tmp['value']);
			}
			break;
		case 'itm_id':
			var id_tmp = data.gmItmOption;
			if (id_tmp !== undefined && id_tmp !== '') {
				tmp_value = getSpecId(id_tmp['value']);
			}
			break;
		case 's_usg_ent_id_lst':
			tmp_value = data.s_usg_ent_id_lst;
			break;
		case 'att_create_start_datetime':
			tmp_value = data.att_create_start_datetime;
			if (tmp_value === Wzb.min_timestamp) {
				tmp_value = '';
			}
			break;
		case 'att_create_end_datetime':
			tmp_value = data.att_create_end_datetime;
			if (tmp_value === Wzb.max_timestamp) {
				tmp_value = '';
			}
			break;
		case 'ats_id':
			tmp_value = data.ats_id;
			break;
		case 'is_my_staff':
			tmp_value = 'true';
			break;
		case 'usr_content_lst':
			tmp_value = usr_content_lst_value;
			break;
		case 'itm_content_lst':
			tmp_value = itm_content_lst_value;
			break;
		case 'content_lst':
			tmp_value = content_lst_value;
			break;
		}
		spec.value += tmp_value + d_f;
	}
	var handler = 'viewSpecifiedRpt(\'' + data.rte_exe_xsl + '\', ' + 1 + ',\'' + spec.name + '\', \'' + spec.value + '\');';
	p = {
		handler : handler,
		title : getLabel('588')
	};
	return $('#buttonTemplate').render(p);
}

function viewSpecifiedRpt(stylesheet, rowIndex, spec_name, spec_value) {
	var dateVar = new Date();
	var window_name = 'rpt_win' + dateVar.getTime();
	var url_success = Wzb.getDisUrl('module', 'report.ReportModule', 'cmd', 'get_rpt', 'rpt_type', 'LEARNER', 'stylesheet', stylesheet, 'download', 0, 'window_name', window_name);
	var url = Wzb.getDisUrl('module', 'report.ReportModule', 'cmd', 'save_sess_spec', 'window_name', window_name, 'spec_name', spec_name, 'spec_value', spec_value, 'url_success',
			url_success);
	var str_feature = 'width=' + '780' + ',height=' + '500' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',toolbar=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10'
			+ ',status=' + 'yes';
	wbUtilsOpenWin(url, 'win' + rowIndex, false, str_feature);
}

function run_rpt() {
	mgt_rpt.get_rpt_adv(formRpt, 'LEARNER', '', 'rpt_lrn_res.xsl', 'false', 1, wb_label_lan);
}

function run_rpt_new() {
	mgt_rpt.get_rpt_adv(formRpt, 'LEARNER', '', 'rpt_lrn_res_new.xsl', 'false', 1, wb_label_lan);
}

function del_rpt() {
	if (formRpt.rsp_id !== '') {
		if (confirm(getLabel('625'))) {
			window.location.href = getDeleteTplUrl(formRpt.rsp_id.value);
		}
	}
}

function getDeleteTplUrl(rsp_id) {
	var url = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'del_staff_rpt_spec', 'rsp_id', rsp_id, 'url_success', location.href, 'url_failure',
			location.href);
	return url;
}

function save_rpt() {
	var spec = new Spec();
	if (_wbMgtRptGetFormSpec(formRpt, wb_label_lan, spec, 'yes') === true) {
		p = {
			rsp_id : formRpt.rsp_id.value,
			rsp_tpl_value : formRpt.rsp_tpl_value.value
		}
		var formHtml = $('#formTemplate').render(p);
		
		new Boxy(formHtml, {
			title : getLabel('594'),
			closeText : getLabel('772')
		});
		$(".boxy-wrapper").bgiframe();
	}
}

function save_rpt_exec() {
	var text = $('#s_rsp_title').val();
	if (text.trim() === '') {
		alert(getLabel('183'));
		return;
	}
	var rsp_id = $('#s_rsp_id').val();

	formRpt.method = 'post';
	formRpt.action = Wzb.uri_dis;
	formRpt.cmd.value = 'ins_staff_rpt_spec';
	formRpt.module.value = 'JsonMod.supervise.SuperviseModule';
	formRpt.target = '_self';
	if (rsp_id != '') {
		formRpt.cmd.value = 'upd_staff_rpt_spec';
		formRpt.rsp_id.value = rsp_id;
	}
	formRpt.rspTitle.value = text;
	if (formRpt.enroll_start_date.value === '') {
		formRpt.att_create_start_datetime.value = Wzb.min_timestamp;
	} else {
		formRpt.att_create_start_datetime.value = formRpt.enroll_start_date.value + ' 00:00:00.000';
	}
	if (formRpt.enroll_end_date.value === '') {
		formRpt.att_create_end_datetime.value = Wzb.max_timestamp;
	} else {
		formRpt.att_create_end_datetime.value = formRpt.enroll_end_date.value + ' 23:59:59.000';
	}

	formRpt.usr_name_str.value = getGoldenmanTextStr(formRpt.ent_id_lst);
	formRpt.itm_title_str.value = getGoldenmanTextStr(formRpt.itm_id_lst);
	formRpt.tnd_title_str.value = getGoldenmanTextStr(formRpt.tnd_id);
	formRpt.ent_id_str.value = getGoldenmanIdStr(formRpt.ent_id_lst);
	formRpt.itm_id_str.value = getGoldenmanIdStr(formRpt.itm_id_lst);
	formRpt.tnd_id_str.value = getGoldenmanIdStr(formRpt.tnd_id);

	formRpt.url_success.value = wb_utils_invoke_controller('subordinate/subordinateReport');
	formRpt.url_failure.value = wb_utils_invoke_controller('subordinate/subordinateReport');
	formRpt.submit();
}

function getGoldenmanTextStr(ele) {
	var str = '';
	if (ele && ele.options && ele.options.length > 0) {
		var len = ele.options.length;
		for (i = 0; i < len; i++) {
			str += ele.options[i].text;
			if (i !== len - 1) {
				str += '~';
			}
		}
	}
	return str;
}

function getGoldenmanIdStr(ele) {
	var str = '';
	if (ele && ele.options && ele.options.length > 0) {
		var len = ele.options.length;
		for (i = 0; i < len; i++) {
			str += ele.options[i].value;
			if (i !== len - 1) {
				str += '~';
			}
		}
	}
	return str;
}

function newRptTpl() {
	$.ajax({
		url : getRptLstUrl(),
		dataType : "json",
		type : 'GET'
	}).done(function(data) {
		setRptTplPanel(data)
	});
}

function getRptTpl(tpl_id) {
	var url = getSpecifiedTplUrl(tpl_id);
	$.ajax({
		url : url,
		dataType : "json",
		type : 'GET',
		success : function(data){
			$("li[name='custom_report']").click();
			setRptTplPanel(data);
		}
	});
}

function setRptTplPanel(data) {
	var rptData = data['staff_rpt'];

	// Panel title
	var title = '';
	var tplValue = '';
	var isSpecifiedTpl = (rptData['rsp_id'] !== undefined);
	if (isSpecifiedTpl) {
		title = rptData['rsp_title'];
		tplValue = rptData['rsp_title_noescape'];

		formRpt.rsp_id.value = rptData['rsp_id'];
		formRpt.rsp_tpl_value.value = tplValue;
	} else {
		title = getLabel('274')
	}
	$('#rpt_panel > div.panel-header > span').text(title);

	// Completion Status
	var ats_value = 0;
	if (rptData['ats_id'] !== undefined) {
		ats_value = rptData['ats_id'];
	}
	$('input[name=ats_id]', $('#formRpt')).each(function() {
		if ($(this).val() == ats_value) {
			this.checked = true;
		}
	});

	// Learner/Group
	var s_usg_ent_id_lst_value = 'my_staff';
	if (rptData['s_usg_ent_id_lst'] !== undefined) {
		s_usg_ent_id_lst_value = rptData['s_usg_ent_id_lst'];
	}
	$('input[name=s_usg_ent_id_lst]', $('#formRpt')).each(function() {
		if ($(this).val() == s_usg_ent_id_lst_value) {
			this.checked = true;
		}
	});

	// Course
	var course_sel_type_value = function() {
		if (rptData['gmItmOption'] !== undefined) {
			return 0;
		} else if (rptData['gmTndOption'] !== undefined) {
			return 1;
		} else {
			return 2;
		}
	}();
	$('input[name=course_sel_type]', $('#formRpt')).each(function() {
		if ($(this).val() == course_sel_type_value) {
			this.checked = true;
		}
	});

	// Course
	var eDate = '';
	if (rptData['att_create_start_datetime'] !== undefined) {
		sDate = rptData['att_create_start_datetime'];
		sDate = Wzb.formatTimeStr(sDate);
		if (sDate === Wzb.min_timestamp) {
			sDate = '';
		} else {
			sDate = sDate.slice(0, 10);
		}
	}
	$('#enroll_start_date').val(sDate);

	var sDate = '';
	if (rptData['att_create_end_datetime'] !== undefined) {
		eDate = rptData['att_create_end_datetime'];
		eDate = Wzb.formatTimeStr(eDate);
		if (eDate === Wzb.max_timestamp) {
			eDate = '';
		} else {
			eDate = eDate.slice(0, 10);
		}
	}
	$('#enroll_end_date').val(eDate);
	if (rptData['rsp_id'] !== undefined) {
		formRpt.rsp_id.value = rptData['rsp_id'];

		$('#del_btn_div').show();
	}else{
		$('#del_btn_div').hide();
	}

	init();
	Wzb.goldenMan.setOptions(data, formRpt.ent_id_lst, 'goldenman_mystaff_user');
	Wzb.goldenMan.setOptions(data, formRpt.itm_id_lst, 'goldenman_mystaff_item');
	Wzb.goldenMan.setOptions(data, formRpt.tnd_id, 'goldenman_mystaff_treenode');

}

function getRptLstUrl() {
//	var url = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_staff_rpt_lst', 'goldenman_param', gldmanParam);
//	return url;
	nav_go(wb_utils_app_base + 'app/report/my_staff_report');
}

function getSpecifiedTplUrl(rsp_id) {
	var url = Wzb.getDisUrl('module', 'JsonMod.supervise.SuperviseModule', 'cmd', 'get_staff_rpt_tpl', 'goldenman_param', gldmanParam, 'rsp_id', rsp_id);
	return url;
}

function usr_change(obj) {
	var value = obj.value;
	if (value === undefined) {
		value = obj.getGroupValue();
	}
	if (value == "") {
		var pos = false;
	} else {
		var pos = true;
	}
	if (formRpt.ent_id_lst && formRpt.ent_id_lst.type == 'select-multiple') {
		formRpt.ent_id_lst.disabled = pos;
		if (formRpt.genaddent_id_lst) {
			formRpt.genaddent_id_lst.disabled = pos;
		}
		if (formRpt.genremoveent_id_lst) {
			formRpt.genremoveent_id_lst.disabled = pos;
		}
		if (formRpt.gensearchent_id_lst) {
			formRpt.gensearchent_id_lst.disabled = pos;
		}
		if (pos == true) {
			formRpt.ent_id_lst.options.length = 0
		}
	}
}

function course_change(obj) {
	var value = obj.value;
	if (value === undefined) {
		value = obj.getGroupValue();
	}
	if (value == 0) {
		var pos = true;
		var neg = false;
	} else if (value == 1) {
		var pos = false;
		var neg = true;
	} else {
		var pos = true;
		var neg = true;
	}
	if (formRpt.tnd_id && formRpt.tnd_id.type == 'select-multiple') {
		formRpt.tnd_id.disabled = pos;
		if (formRpt.genaddtnd_id) {
			formRpt.genaddtnd_id.disabled = pos;
		}
		if (formRpt.genremovetnd_id) {
			formRpt.genremovetnd_id.disabled = pos;
		}
		if (pos == true) {
			formRpt.tnd_id.options.length = 0
		}
	}
	if (formRpt.itm_id_lst && formRpt.itm_id_lst.type == 'select-multiple') {
		formRpt.itm_id_lst.disabled = neg;
		if (formRpt.genadditm_id_lst) {
			formRpt.genadditm_id_lst.disabled = neg;
		}
		if (formRpt.genremoveitm_id_lst) {
			formRpt.genremoveitm_id_lst.disabled = neg;
		}
		if (neg == true) {
			formRpt.itm_id_lst.options.length = 0
		}
	}
}

function init() {
	if (formRpt.course_sel_type && formRpt.course_sel_type[0].checked) {
		course_change(formRpt.course_sel_type[0]);
	} else if (formRpt.course_sel_type && formRpt.course_sel_type[1].checked) {
		course_change(formRpt.course_sel_type[1]);
	} else if (formRpt.course_sel_type && formRpt.course_sel_type[2].checked){
		course_change(formRpt.course_sel_type[2]);
	}

	if (formRpt.s_usg_ent_id_lst && formRpt.s_usg_ent_id_lst[0].checked) {
		usr_change(formRpt.s_usg_ent_id_lst[0]);
	} else if (formRpt.s_usg_ent_id_lst && formRpt.s_usg_ent_id_lst[1].checked) {
		usr_change(formRpt.s_usg_ent_id_lst[1]);
	} else if (formRpt.s_usg_ent_id_lst && formRpt.s_usg_ent_id_lst[2].checked) {
		usr_change(formRpt.s_usg_ent_id_lst[2]);
	}
}

function getPopupUsrLst(fld_name, id_lst, nm_lst, usr_argv) {
	ent_id_lst(usr_argv);
}