// ------------------ wizBank Resource object ------------------- 
// Convention:
//   public functions : use "wbResource" prefix 
//   private functions: use "_wbResource" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 




/* constructor */
function wbScControl() {
    this.get = wbScGet
    this.read = wbScRead
    this.get_Que_Content = getQueContent;
	this.add_que_prep = addQuePrep;
	this.add_dsc_spec_prep = addDynamicSpecPrep;
	this.add_dsc_spec = addDynamicSpec;
	this.edit_dsc_spec = editDynamicSpec;
	this.upd_dsc_spec = updDynamicSpec;
	this.del_dsc_spec = delDynamicSpec;
}

function wbScGet(res_id, res_subtype){
	if (res_subtype == 'FSC') {
		url = wb_utils_invoke_servlet('cmd','get_q','que_id',res_id,'stylesheet','que_get.xsl');
	}
	else {
		url = wb_utils_invoke_servlet('cmd','get_q','que_id',res_id,'stylesheet','que_get.xsl')
	}
	window.location.href = url;
	//document.getElementById("content").src = url;
}

function wbScRead(res_id, res_subtype){
		url = wb_utils_invoke_servlet('cmd','get_q','que_id',res_id,'stylesheet','res_srh_que_ind.xsl');
    window.location.href = url;
}

function getQueContent(id, obj_id, que_type) {
	url_failure = '../htm/close_window.htm'
    url = wb_utils_invoke_disp_servlet('env','wizb','module','quebank.QueBankModule','cmd','get_fsc_content','res_id', id, 'obj_id', obj_id, 'stylesheet','tst_info_frame.xsl','url_failure',url_failure)
	
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '620'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

	 wbUtilsOpenWin(url,'dsc_que_content',false, str_feature);
}

function addQuePrep(id, mode){
    url_success = 'javascript:parent.location.reload()';
	url_failure = '../htm/close_window.htm'
	url = wb_utils_invoke_servlet('cmd','get_que_frm','que_type','MC', 'mod_type', mode, 'container_res_id', id, 'stylesheet','res_sc_que_ins.xsl', 'url_success', url_success, 'url_failure',url_failure)
	
/*	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ 600
			+ ',height=' 				+ 600
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'no'	
			+ ',screenX='				+ '0'
			+ ',screenY='				+ '10'
			+ ',left='					+ '0'
			+ ',top='					+ '10'
*/
	 window.parent.content.location.href = url;
}

function addDynamicSpecPrep (id) {
	url = wb_utils_invoke_servlet('env','wizb','cmd','get_prof', 'res_id', id, 'stylesheet','res_sc_criteria.xsl',"isExcludes",'true');
	window.parent.content.location.href = url;
}

function addDynamicSpec(frm,continer_res_id,lang){
	if (frm.qcs_score) {
		frm.qcs_score.value = wbUtilsTrimString(frm.qcs_score.value);
	}
	if (frm.qcs_qcount) {
		frm.qcs_qcount.value = wbUtilsTrimString(frm.qcs_qcount.value);
	}
    if (!wbUtilsValidateEmptyField(frm.qcs_score,frm.qcs_score_label.value)) {
        return false;
    }else if (!wbUtilsValidateEmptyField(frm.qcs_qcount,frm.qcs_qcount_label.value)) {
        return false;
    }
    if (!wbUtilsValidateInteger(frm.qcs_score,frm.qcs_score_label.value)) {
        return false;
    }else if (!wbUtilsValidateInteger(frm.qcs_qcount,frm.qcs_qcount_label.value)) {
        return false;
    }
	frm.method = "post";
	frm.module.value = 'quebank.QueBankModule'
	frm.cmd.value = 'ADD_DSC_CRIT_SPEC';
	frm.res_id.value = continer_res_id;
	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";
	frm.qcs_obj_id.value = '';
	frm.qcs_type.value = 'DSC';
	frm.qcs_difficulty.value = 0;
	frm.qcs_id.value = '';
	
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dsc_content','res_id',continer_res_id,'stylesheet','res_sc_info_content.xsl', 'mode', 'DSC',"isExcludes",'true')
	frm.url_failure.value = 'javascript:parent.location.reload()'
	frm.submit();
}

function editDynamicSpec(que_id, spec_id) {
	window.location.href = getEditUrl(que_id, spec_id);
}

function getEditUrl(que_id, spec_id) {
	url_success = 'javascript:parent.location.reload()'
	url_failure = 'javascript:parent.location.reload()'
    url = wb_utils_invoke_disp_servlet('env','wizb','module','quebank.QueBankModule','cmd','GET_DSC_CRIT_SPEC','res_id', que_id,'qcs_id',spec_id, 'stylesheet','res_sc_criteria.xsl','url_success', url_success, 'url_failure',url_failure,"isExcludes",'true')    
	return url;
}

function updDynamicSpec(frm,container_res_id,lang){
	if (frm.qcs_score) {
		frm.qcs_score.value = wbUtilsTrimString(frm.qcs_score.value);
	}
	if (frm.qcs_qcount) {
		frm.qcs_qcount.value = wbUtilsTrimString(frm.qcs_qcount.value);
	}
	
	frm.method = "get";
	frm.module.value = 'quebank.QueBankModule'
	frm.cmd.value = 'UPD_DSC_CRIT_SPEC';
	frm.res_id.value = container_res_id;
	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";
	frm.qcs_obj_id.value = '';
	frm.qcs_type.value = 'DSC';
	frm.qcs_difficulty.value = 0;
	
	frm.url_failure.value = 'javascript:parent.location.reload()';
	frm.url_success.value = wbInfoContentURL(container_res_id);
	frm.submit();
}

function wbInfoContentURL(container_id){
	url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dsc_content','res_id',container_id,'stylesheet','res_sc_info_content.xsl','url_failure',self.location.href,"isExcludes",'true');
	return url;
}

function delDynamicSpec(continer_res_id, qcs_id, lang) {
    if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
        var url_failure = 'javascript:window.history.back()';
        url = wb_utils_invoke_disp_servlet('env','wizb','module','quebank.QueBankModule','cmd','DEL_DSC_CRIT_SPEC','res_id', continer_res_id,'qcs_id',qcs_id,'url_success', self.location.href, 'url_failure',url_failure,"isExcludes",'true')    
    	window.parent.content.location.href = url;
    }
}

