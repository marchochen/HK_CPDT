/**
  * ------------------ wizBank wbUgr MailGroup object ------------------- 
  * Convention:
  * public functions : use "wbUgr" prefix 
  * private functions: use "_wbUgr" prefix
  * Dependencies:
  * - gen_utils.js
  * - wb_utils.js
  * ------------------------------------------------------------ 
  */


/** constructor */
function wbUgr() {	
	this.ugr_home_url = wbUgrHomeUrl
	this.ugr_top_nav_url = wbUgrTopNavUrl
	this.ugr_nav_url = wbUgrNavUrl
	
	this.ugr_url = wbUgrUrl
	
	this.ugr_ins_prep_url = wbUgrInsPrepUrl
	this.ugr_ins_exec = wbUgrInsExec
	
	this.ugr_upd_prep_url = wbUgrUpdPrepUrl
	this.ugr_upd_exec = wbUgrUpdExec
	
	this.ugr_del_url = wbUgrDelUrl
	this.ugr_del_exec = wbUgrDelExec
}

function wbUgrHomeUrl() {
	cmd = "ugr_info"
	stylesheet = "ugr_info.xsl"
	url = wb_utils_invoke_servlet('cmd', cmd, "stylesheet", stylesheet)
	return url;
}

function wbUgrTopNavUrl() {
	cmd = "ugr_top_nav"
	stylesheet = "ugr_top_nav.xsl"
	url = wb_utils_invoke_servlet('cmd', cmd, "stylesheet", stylesheet)
	return url;
}

function wbUgrNavUrl() {
	cmd = "ugr_nav"
	stylesheet = "ugr_navigation.xsl"
	url = wb_utils_invoke_servlet('cmd', cmd, "stylesheet", stylesheet)
	return url;
}

function wbUgrUrl(ugr_ent_id) {
	cmd = "get_ugr"
	stylesheet = "ugr.xsl"
	url = wb_utils_invoke_servlet('cmd', cmd, "ugr_ent_id", ugr_ent_id, "stylesheet", stylesheet)
	return url;
}

function wbUgrInsPrepUrl(ugr_ent_id) {
	cmd = "ugr_ins_prep"
	stylesheet = "ugr_ins.xsl"
	url = wb_utils_invoke_servlet('cmd', cmd, "ugr_ent_id", ugr_ent_id, "stylesheet", stylesheet)
	return url;
}

function wbUgrInsExec(frm, lang) {
	if (frm.ugr_grade_code) {
		frm.ugr_grade_code.value = wbUtilsTrimString(frm.ugr_grade_code.value);
		if (frm.ugr_grade_code.value=='') {
			Dialog.alert(wb_msg_ugr_grade_code_empty)
			frm.ugr_grade_code.focus()
			return;
		}
	}
	
	if (frm.ugr_display_bil) {
		frm.ugr_display_bil.value = wbUtilsTrimString(frm.ugr_display_bil.value);
		if (frm.ugr_display_bil.value == '') {
			Dialog.alert(wb_msg_ugr_title_empty)
			frm.ugr_display_bil.focus()
			return;
		}
		if(getChars(frm.ugr_display_bil.value) > 80){
			Dialog.alert(wb_msg_usr_title_too_long)
			frm.ugr_display_bil.focus()
			return;
		}
	}

	
	//tcr_id
	if (frm.ugr_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
	    	Dialog.alert(wb_msg_pls_input_tcr);
	    	return;
	    } else {
		    frm.ugr_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	
	
	if(frm.ugr_grade_code.value.search(/,|，/) != -1){

		Dialog.alert(wb_msg_usr_enter_vaild + wb_msg_usr_code)
		frm.ugr_grade_code.focus()
		return false;
	}

	if(frm.ugr_display_bil.value.search(/\/|\\/) != -1){
		Dialog.alert(wb_msg_usr_enter_vaild + wb_msg_usr_title)
		frm.ugr_display_bil.focus()
		return false;
	}
	
	frm.cmd.value = "ugr_ins";
	frm.ugr_ent_id.value = getUrlParam('ugr_ent_id');
	frm.action = wb_utils_servlet_url+"?isExcludes=true";
	frm.method = 'post'

	frm.url_success.value = '../htm/ugr_ins_staging.htm'+'?lang='+lang;
	frm.url_success.value += '&ugr_display_bil='+escape(frm.ugr_display_bil.value)
	frm.url_failure.value = self.location.href
	frm.submit()
}

function wbUgrUpdPrepUrl(ugr_ent_id) {
	cmd = "ugr_upd_prep"
	stylesheet = "ugr_upd.xsl"
	url = wb_utils_invoke_servlet('cmd', cmd, "ugr_ent_id", ugr_ent_id, "stylesheet", stylesheet)
	return url;
}

function wbUgrUpdExec(frm, lang) {
	if (frm.ugr_grade_code) {
		frm.ugr_grade_code.value = wbUtilsTrimString(frm.ugr_grade_code.value);
		if (frm.ugr_grade_code.value == '') {
			Dialog.alert(wb_msg_ugr_grade_code_empty);
			frm.ugr_grade_code.focus()
			return;
		}
	}
	
	if (frm.ugr_display_bil) {
		frm.ugr_display_bil.value = wbUtilsTrimString(frm.ugr_display_bil.value);
		if (frm.ugr_display_bil.value == '') {
			Dialog.alert(wb_msg_ugr_title_empty);
			frm.ugr_display_bil.focus();
			return;
		}
		if(getChars(frm.ugr_display_bil.value) > 80){
			Dialog.alert(wb_msg_usr_title_too_long)
			frm.ugr_display_bil.focus()
			return;
		}
	}
	
	//tcr_id
	if (frm.ugr_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		  	Dialog.alert(wb_msg_pls_input_tcr);
	    	return;
	    } else {
		    frm.ugr_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	
	
	if(frm.ugr_grade_code.value.search(/,/) != -1){

		Dialog.alert(wb_msg_usr_enter_vaild + wb_msg_usr_code)
		frm.ugr_grade_code.focus()
		return false;
	}

	if(frm.ugr_display_bil.value.search(/\//) != -1){
		Dialog.alert(wb_msg_usr_enter_vaild + wb_msg_usr_title);
		frm.ugr_display_bil
		frm.ugr_display_bil.focus()
		return false;
	}
	
	frm.cmd.value = "ugr_upd";
	frm.ugr_ent_id.value = getUrlParam('ugr_ent_id');
	frm.action = wb_utils_servlet_url+"?isExcludes=true"
	frm.method = 'post'

	frm.url_success.value = '../htm/ugr_upd_staging.htm'+'?lang='+lang;
	frm.url_success.value += '&ugr_ent_id='+getUrlParam('ugr_ent_id')
	frm.url_success.value += '&ugr_display_bil='+escape(frm.ugr_display_bil.value)
	
	frm.url_failure.value = self.location.href
	
	frm.submit()
}

function wbUgrDelUrl(lang, ugr_ent_id) {
	cmd = "ugr_del"
	stylesheet = "ugr_del.xsl"
	url = wb_utils_invoke_servlet('cmd', cmd, "ugr_ent_id", ugr_ent_id, "stylesheet", stylesheet)
	return url;
}

function wbUgrDelExec(frm, lang) {
	cmd = "ugr_del_confirm";
	ugr_ent_id  = getUrlParam('ugr_ent_id');
	url_success = '../htm/ugr_del_staging.htm';
	url_failure = 'javascript: top.location.reload()'
	ugr_timestamp = frm.ugr_timestamp.value
	url = wb_utils_invoke_servlet('cmd', cmd, "ugr_ent_id", ugr_ent_id, "url_success", url_success, 'url_failure', url_failure, "ugr_timestamp", ugr_timestamp)
	window.location = url
}
