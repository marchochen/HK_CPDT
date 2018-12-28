function wbpfs() {	
	this.pfs_home_url = wbpfsHomeUrl
	this.pfs_top_nav_url = wbpfsTopNavUrl
	this.pfs_nav_url = wbpfsNavUrl
	
	this.pfs_url = wbpfsUrl
	
	this.pfs_ins_prep_url = wbpfsInsPrepUrl
	this.pfs_ins_upd_exec = wbpfsInsUpdExec
	
	this.pfs_del = wbpfsDel
}

function wbpfsHomeUrl() {
	cmd = "pfs_info"
	stylesheet = "pfs_info.xsl"
	url =  wb_utils_invoke_disp_servlet("module", "profession.ProfessionModule", 'cmd', cmd, "stylesheet", stylesheet)
	return url;
}

function wbpfsTopNavUrl() {
	cmd = "pfs_top_nav"
	stylesheet = "pfs_top_nav.xsl"
	url =  wb_utils_invoke_disp_servlet("module", "profession.ProfessionModule", 'cmd', cmd, "stylesheet", stylesheet)
	return url;
}

function wbpfsNavUrl() {
	cmd = "pfs_nav"
	stylesheet = "pfs_navigation.xsl"
	url =  wb_utils_invoke_disp_servlet("module", "profession.ProfessionModule", 'cmd', cmd, "stylesheet", stylesheet)
	return url;
}

function wbpfsUrl(pfs_ent_id) {
	cmd = "get_pfs"
	stylesheet = "pfs.xsl"
	url =  wb_utils_invoke_disp_servlet("module", "profession.ProfessionModule", 'cmd', cmd, "pfs_ent_id", pfs_ent_id, "stylesheet", stylesheet)
	return url;
}

function wbpfsInsPrepUrl(pfs_ent_id) {
	cmd = "pfs_ins_prep"
	stylesheet = "pfs_ins.xsl"
	url =  wb_utils_invoke_disp_servlet("module", "profession.ProfessionModule", 'cmd', cmd, "pfs_ent_id", pfs_ent_id, "stylesheet", stylesheet)
	return url;
}

function wbpfsInsUpdExec(frm, lang) {
	
	if (frm.pfs_title) {
		frm.pfs_title.value = wbUtilsTrimString(frm.pfs_title.value);
		if (frm.pfs_title.value == '') {
			alert(wb_msg_pfs_title_empty)
			frm.pfs_title.focus()
			return;
		}
	}


	
	if(frm.pfs_title.value.search(/\//) != -1){
		alert(wb_msg_usr_enter_vaild + wb_msg_usr_title)
		frm.pfs_title.focus()
		return false;
	}
	
	var tmp_psi_itm_id_lst = '';
	var tmp_psi_ugr_id_lst = '';
	for(var i=0;i<frm.length;i++){
		var element=frm[i];
		if(element.name.indexOf('itm_id_lst') == 0){
			var tmp_lst = '';
			for (j = 0; j < element.options.length; j++) {
				tmp_lst += element.options[j].value;
				if (j != element.options.length - 1) {
					tmp_lst += " ~ ";
				}
			}
			if(tmp_lst != '') {
				tmp_psi_itm_id_lst += tmp_lst + "|";
				tmp_lst = '';
			}
		}
		if(element.name.indexOf('psi_ugr_id') == 0){
			tmp_ugr_id = element.value;
			if(tmp_ugr_id != 0) {
				tmp_psi_ugr_id_lst += tmp_ugr_id + '|';
			}
		}
	}
	frm.psi_itm_id_lst.value = tmp_psi_itm_id_lst.substring(0,tmp_psi_itm_id_lst.length-1);
	frm.psi_ugr_id_lst.value = tmp_psi_ugr_id_lst.substring(0,tmp_psi_ugr_id_lst.length-1);
	
	var tmp_itm_id_lst = frm.psi_itm_id_lst.value.split('|');
	var tmp_ugr_id_lst = frm.psi_ugr_id_lst.value.split('|');
	if(tmp_itm_id_lst.length != tmp_ugr_id_lst.length || (tmp_itm_id_lst == '' && tmp_ugr_id_lst != '') || (tmp_itm_id_lst != '' && tmp_ugr_id_lst == '')) {
		alert(wb_msg_pfs_err01);
		return;
	}
	
	frm.cmd.value = "pfs_ins_upd_exec";
	frm.action = wb_utils_disp_servlet_url + "?cmd=pfs_ins_upd_exec";
	frm.module.value = "profession.ProfessionModule";
	frm.method = 'post'

	frm.url_success.value = "javascript:wb_utils_nav_go('FTN_AMD_PROFESSION_MAIN', '${prof.usr_ent_id}', '${label_lan}')";
	
	frm.url_failure.value = self.location.href
	
	frm.submit()
}

function wbpfsUpdPrepUrl(pfs_ent_id) {
	cmd = "pfs_upd_prep"
	stylesheet = "pfs_upd.xsl"
	url =  wb_utils_invoke_disp_servlet("module", "profession.ProfessionModule", 'cmd', cmd, "pfs_ent_id", pfs_ent_id, "stylesheet", stylesheet)
	return url;
}

function wbpfsUpdExec(frm, lang) {
	if (frm.pfs_grade_code) {
		frm.pfs_grade_code.value = wbUtilsTrimString(frm.pfs_grade_code.value);
		if (frm.pfs_grade_code.value == '') {
			alert(wb_msg_pfs_grade_code_empty)
			frm.pfs_grade_code.focus()
			return;
		}
	}
	
	if (frm.pfs_display_bil) {
		frm.pfs_display_bil.value = wbUtilsTrimString(frm.pfs_display_bil.value);
		if (frm.pfs_display_bil.value == '') {
			alert(wb_msg_pfs_title_empty)
			frm.pfs_display_bil.focus()
			return;
		}
	}
	
	//tcr_id
	if (frm.pfs_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		  	alert(wb_msg_pls_input_tcr);
	    	return;
	    } else {
		    frm.pfs_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	
	/*
	if(frm.pfs_grade_code.value.search(/,/) != -1){

		alert(wb_msg_usr_enter_vaild + wb_msg_usr_code)
		frm.pfs_grade_code.focus()
		return false;
	}
*/
	if(frm.pfs_display_bil.value.search(/\//) != -1){
		alert(wb_msg_usr_enter_vaild + wb_msg_usr_title)
		frm.pfs_display_bil.focus()
		return false;
	}
	
	frm.cmd.value = "pfs_upd";
	frm.pfs_ent_id.value = getUrlParam('pfs_ent_id');
	frm.action = wb_utils_servlet_url
	frm.method = 'post'

	frm.url_success.value = '../htm/pfs_upd_staging.htm'+'?lang='+lang;
	frm.url_success.value += '&pfs_ent_id='+getUrlParam('pfs_ent_id')
	frm.url_success.value += '&pfs_display_bil='+escape(frm.pfs_display_bil.value)
	
	frm.url_failure.value = self.location.href
	
	frm.submit()
}

function wbpfsDel(pfs_id) {
	if(confirm(wb_msg_pfs_del_confirm)) {
		cmd = "pfs_del";
		url_success = "javascript:wb_utils_nav_go('FTN_AMD_PROFESSION_MAIN', '${prof.usr_ent_id}', '${label_lan}')";
		url_failure = "javascript:wb_utils_nav_go('FTN_AMD_PROFESSION_MAIN', '${prof.usr_ent_id}', '${label_lan}')";
		url =  wb_utils_invoke_disp_servlet("module", "profession.ProfessionModule", 'cmd', cmd, "pfs_id", pfs_id, "url_success", url_success, 'url_failure', url_failure)
		window.location.href = url;
	}
}
