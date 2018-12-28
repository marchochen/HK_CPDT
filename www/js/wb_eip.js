function wbEIP() {
	this.ins_upd_eip_prep = wbEnterpriseInfoPortalInsUpdPrep
	this.sort_eip_list = wbEnterpriseInfoPortalSort
	this.ins_upd_eip_exec = wbEnterpriseInfoPortalInsUpdExec
	this.get_eip = wbEnterpriseInfoPortalGet
	this.set_status = wbEnterpriseInfoPortalSetStatus
	this.del_eip = wbEnterpriseInfoPortalDelete
	this.set_login_bg_prep = wbEnterpriseInfoPortalSetLoginBgPrep
	this.set_login_bg_exec = wbEnterpriseInfoPortalSetLoginBgExec
	this.get_dynamicpri =wbDynamicPriGet
	this.set_dynamicpri =wbDynamicPriSet
	this.empty_data = wbEnterpriseInfoPortalEmptyData
}

function wbEnterpriseInfoPortalGetListUrl() {
	var url = wb_utils_invoke_disp_servlet('module','JsonMod.eip.EIPModule','cmd', 'get_eip_list','stylesheet', 'eip_list.xsl');
	return url;
}


function wbEnterpriseInfoPortalInsUpdPrep(eip_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'ins_upd_eip_prep', 'stylesheet', 'eip_ins_upd.xsl', 'eip_id', eip_id);
	window.location.href = url;
}

function wbEnterpriseInfoPortalGet(eip_id) {
	var url = wbEnterpriseInfoPortalGetUrl(eip_id);
	window.location.href = url;
}

function wbEnterpriseInfoPortalGetUrl(eip_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'ins_upd_eip_prep', 'stylesheet', 'eip_detail.xsl', 'eip_id', eip_id);
	return url;
}

function wbEnterpriseInfoPortalSetLoginBgPrep(eip_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'ins_upd_eip_prep', 'stylesheet', 'eip_login_bg.xsl', 'eip_id', eip_id);
	window.location.href = url;
}

function wbEnterpriseInfoPortalSort(sort_col, sort_order) {
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'get_eip_list', 'stylesheet', 'eip_list.xsl', 'sort_col', sort_col, 'sort_order', sort_order);
	window.location.href = url;
}

function wbEnterpriseInfoPortalInsUpdExec(frm, lang) {
	var eip_max_peak_count = frm.eip_max_peak_count.value; 
    if(eip_max_peak_count !=null && isNaN(eip_max_peak_count))
    {
    	alert(fetchLabel("label_core_system_setting_215"));
    	frm.eip_max_peak_count.focus()
    	return false;
    }	
	frm.eip_code.value = wbUtilsTrimString(frm.eip_code.value);
	if(!wbUtilsValidateEmptyField(frm.eip_code, frm.lab_eip_code.value)){
		frm.eip_code.focus()
		return;
	}
	
	frm.eip_name.value = wbUtilsTrimString(frm.eip_name.value);
	if(!wbUtilsValidateEmptyField(frm.eip_name, frm.lab_eip_name.value)){
		frm.eip_name.focus()
		return;
	}
	
	//tcr_id
	if (frm.eip_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		  	alert(wb_msg_pls_input_tcr);
	    	return;
	    } else {
		    frm.eip_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	if(frm.eip_account_num.value > 1000000000){
		alert(wb_msg_pass_max_eip_account_num);
		return;
	}
	if(!wbUtilsValidateInteger(frm.eip_account_num, frm.lab_eip_account_num.value)){
		frm.eip_account_num.focus();
		return;
	}	
	var pattern = /^[0-9]\d*$/;
	if(frm.eip_max_peak_count != undefined && frm.eip_max_peak_count.value != undefined 
			&& frm.eip_max_peak_count.value != "" && !pattern.test(frm.eip_max_peak_count.value)){
		alert(fetchLabel("label_core_system_setting_215"));
		return false;
	}
	if(frm.eip_max_peak_count.value > 10000){
		alert(wb_msg_max_peak_count);
		return;
	}
	
	if(frm.eip_live_max_count != undefined && frm.eip_live_max_count.value != "" && !pattern.test(frm.eip_live_max_count.value)){
		alert(fetchLabel("label_core_system_setting_205"));
		frm.eip_live_max_count.focus();
    	return false;
    }else if(frm.eip_live_max_count.value > 10000){
		alert(fetchLabel("label_core_system_setting_216"));
		return false;
    }else if(frm.eip_live_max_count != undefined && frm.eip_live_max_count.value == ""){
    	frm.eip_live_max_count.value = 0;
    }
	
	/*
	var loginBgObj = frm.login_bg_radio;
	for(var i=0; i<loginBgObj.length; i++) {
		if(loginBgObj[i].checked) {
			frm.login_bg_type.value = loginBgObj[i].value;
			break;
		}
	}
	
	if(frm.login_bg_type.value == '2') {
		var field = frm.login_bg_file;
		if(frm.login_bg_file.value == '') {
			alert(wb_msg_usr_please_enter_the + frm.lab_eip_login_bg.value);
			return;
		} else if(field.value != null && field.value.length > 0){
			var img_url = field.value;
			var file_ext = img_url.substring( img_url.lastIndexOf('.')+1,img_url.length);
			file_ext =file_ext.toLowerCase();
			if(!(file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png' || file_ext == 'swf')){
					alert(eval('wb_msg_'+lang+'_img_not_support'));
					field.focus();
					return false;
			}
		}
	}
	*/
	
	var eip_live_modes = frm.eip_live_mode_checkbox;
	var eip_live_mode = "";
	for(var i=0;i<eip_live_modes.length;i++){
	    if(eip_live_modes[i].checked){
	    	eip_live_mode += eip_live_modes[i].value + ",";	    	
	    }
	}
	if(eip_live_mode && eip_live_mode.length > 0){
		frm.eip_live_mode.value = eip_live_mode.substring(0,eip_live_mode.length-1);
	}
	
	if(eip_live_mode && eip_live_mode.length > 0 && eip_live_mode.indexOf('QCLOUD') != -1){
		frm.eip_live_qcloud_secretid.value = wbUtilsTrimString(frm.eip_live_qcloud_secretid.value);
		if(!wbUtilsValidateEmptyField(frm.eip_live_qcloud_secretid, frm.lab_live_qcloud_secretid.value)){
			frm.eip_live_qcloud_secretid.focus()
			return;
		}
		
		frm.eip_live_qcloud_secretkey.value = wbUtilsTrimString(frm.eip_live_qcloud_secretkey.value);
		if(!wbUtilsValidateEmptyField(frm.eip_live_qcloud_secretkey, frm.lab_live_qcloud_secretkey.value)){
			frm.eip_live_qcloud_secretkey.focus()
			return;
		}
	}
	
	frm.eip_domain.value = wbUtilsTrimString(frm.eip_domain.value);
	
	frm.module.value = 'JsonMod.eip.EIPModule';
	frm.url_success.value = frm.url_failure.value = wbEnterpriseInfoPortalGetListUrl();
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function wbEnterpriseInfoPortalSetStatus(eip_id, eip_status, eip_update_timestamp) {
	var url_success = wbEnterpriseInfoPortalGetUrl(eip_id);
	var url_failure = url_success;
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'set_status', 'eip_id', eip_id, 'eip_status', eip_status, 'eip_update_timestamp', eip_update_timestamp, 'url_success', url_success, 'url_failure', url_failure);
	window.location.href = url;
}

function wbEnterpriseInfoPortalDelete(eip_id, eip_update_timestamp) {
	//����Ѿ�ʹ�õ��û������0�͵������û��Ѵ��ڲ���0ɾ����ʾ��Ȼ��ֱ�ӷ��ز�ִ�����´���
	if(document.getElementById("account_used").value>0){
		alert(eval('wb_msg_alert_del_eip'));
		return;
	}
	
	var url_success = wbEnterpriseInfoPortalGetListUrl();
	var url_failure = self.location.href;
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'del_eip', 'eip_id', eip_id, 'eip_update_timestamp', eip_update_timestamp, 'url_success', url_success, 'url_failure', url_failure);
	if(confirm(eval('wb_msg_confirm_del_eip'))){
			window.location.href = url;
	}
}

function wbEnterpriseInfoPortalSetLoginBgExec(frm, lang) {
	
	var loginBgObj = frm.login_bg_radio;
	for(var i=0; i<loginBgObj.length; i++) {
		if(loginBgObj[i].checked) {
			frm.login_bg_type.value = loginBgObj[i].value;
			break;
		}
	}
	if(frm.login_bg_type.value == '2') {
		var field = frm.login_bg_file;
		if(frm.login_bg_file.value == '') {
			alert(wb_msg_usr_please_enter_the + frm.lab_eip_login_bg.value);
			return;
		} else if(field.value != null && field.value.length > 0){
			var img_url = field.value;
			var file_ext = img_url.substring( img_url.lastIndexOf('.')+1,img_url.length);
			file_ext =file_ext.toLowerCase();
			if(!(file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png' || file_ext == 'swf')){
					alert(eval('wb_msg_'+lang+'_img_not_support'));
					field.focus();
					return false;
			}
		}
	}
	
	loginBgObj = frm.mobile_bg_radio;
	for(var i=0; i<loginBgObj.length; i++) {
		if(loginBgObj[i].checked) {
			frm.mobile_login_bg_type.value = loginBgObj[i].value;
			break;
		}
	}
	if(frm.mobile_login_bg_type.value == '2') {
		var field = frm.mobile_login_bg_file;
		if(frm.mobile_login_bg_file.value == '') {
			alert(wb_msg_usr_please_enter_the + frm.lab_eip_login_bg.value);
			return;
		} else if(field.value != null && field.value.length > 0){
			var img_url = field.value;
			var file_ext = img_url.substring( img_url.lastIndexOf('.')+1,img_url.length);
			file_ext = file_ext.toLowerCase();
			if(!(file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png' || file_ext == 'swf')){
					alert(eval('wb_msg_'+lang+'_img_not_support'));
					field.focus();
					return false;
			}
		}
	}
	
	frm.module.value = 'JsonMod.eip.EIPModule';
	frm.url_success.value = frm.url_failure.value = self.location.href;
	frm.cmd.value = 'set_login_bg';
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}


function wbDynamicPriGet(tcr_id) {
	var url = wb_utils_invoke_disp_servlet('module','JsonMod.eip.EIPModule','cmd', 'get_dynamicpri','stylesheet', 'eip_dynamicpri_set.xsl',"eip_tcr_id",tcr_id);
	window.location.href = url;
}
function wbDynamicPriGetUrl(tcr_id) {
	var url = wb_utils_invoke_disp_servlet('module','JsonMod.eip.EIPModule','cmd', 'get_dynamicpri','stylesheet', 'eip_dynamicpri_set.xsl',"eip_tcr_id",tcr_id);
	return url
}

function wbDynamicPriSet(frm, lang) {
	if(frm.share_usr_inf_ind[0].checked)
		frm.eip_dps_share_usr_inf_ind.value ="0";
	else
		frm.eip_dps_share_usr_inf_ind.value ="1";
	frm.module.value = 'JsonMod.eip.EIPModule';
	frm.url_success.value = frm.url_failure.value = wbDynamicPriGetUrl(frm.eip_tcr_id.value);
	frm.method = 'post';
	frm.cmd.value ="ins_upd_dynamicpri";
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function wbEnterpriseInfoPortalEmptyData(eip_id, tcr_id){
	var url_success = self.location.href;
	var url_failure = self.location.href;
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'empty_data', 'eip_id', eip_id, 'eip_tcr_id', tcr_id, 'url_success', url_success, 'url_failure', url_failure);
	if(confirm(eval('wb_msg_confirm_del_eip'))){
		window.location.href = url;
	}
}