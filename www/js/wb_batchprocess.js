// ------------------ wizBank Data Import and Export object ------------------- 
// Convention:
//   public functions : use "wbBatchProcess" prefix 
//   private functions: use "_wbBatchProcess" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

function wbBatchProcess(){
	this.User = new wbBatchProcessUser;
	this.Enrol = new wbBatchProcessEnrol;
	this.Catalog = new wbBatchProcessCatalog;
	this.Res = new wbBatchProcessRes;
	this.get_list = wbBatchProcessGetList;
	this.AttdRate = new wbBatchProcessAttdRate;
	this.Credit = new wbBatchProcessCredit
}
//------------------------------------------------------------------ 
function wbBatchProcessGetList(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_lst.xsl');
	window.location.href = url;
}

function wbBatchProcessUser(){
	this.Import = new wbBatchProcessUserImport;
	this.Log = new wbBatchProcessUserLog
}

function wbBatchProcessEnrol(){
	this.Import = new wbBatchProcessEnrolImport;
	this.Log = new wbBatchProcessEnrolLog
}
function wbBatchProcessCatalog(){
	//
}

function wbBatchProcessRes(){
	this.Import = new wbBatchProcessResImport;
	this.Export = new wbBatchProcessResExport;
	this.Log = new wbBatchProcessResLog
}
function wbBatchProcessAttdRate(){
	this.Import = new wbBatchProcessAttdRateImport
}
//------------------------------------------------------------------ 
function wbBatchProcessUserLog(){
	this.get_log = wbBatchProcessUserLogGetLog;
	this.get_del_log = wbBatchProcessUserDelLogGetLog
}

function wbBatchProcessEnrolLog(){
	this.get_log = wbBatchProcessEnrolLogGetLog
}

function wbBatchProcessResLog(){
	this.get_log = wbBatchProcessResLogGetLog
}

function wbBatchProcessUserImport(){
	this.prep = wbBatchProcessUserImportPrep;
	this.del= wbBatchProcessUserDeletePrep;
	this.exec = wbBatchProcessUserImportExec;
	this.del_exec = wbBatchProcessUserDelExec;
	this.confirm = wbBatchProcessUserImportConfirm;
	this.del_confirm = wbBatchProcessUserDelConfirm;
	this.confirm2 = wbBatchProcessUserImportConfirm2;
	this.cancel = wbBatchProcessUserImportCancel;
	this.preview = wbBatchProcessUserImportPreviewAll;
	this.get_instr = wbBatchProcessUserImportGetInstr;
	this.get_del_instr = wbBatchProcessUserDelGetInstr;
	this.get_tpl = wbBatchProcessUserImportGetTpl;
	this.get_del_tpl = wbBatchProcessUserDelGetTpl;
	this.get_source=wbBatchProcessUserImportGetSourceFile;
	this.export_user = wbBatchProcessUserInfoExportPrep
}

function wbBatchProcessEnrolImport(){
	this.prep = wbBatchProcessEnrolImportPrep;
	this.exec = wbBatchProcessEnrolImportExec;
	this.confirm = wbBatchProcessEnrolImportConfirm;
	this.viewall = wbBatchProcessEnrolImportViewAll;
	this.get_instr = wbBatchProcessEnrolImportGetInstr;
	this.get_tpl = wbBatchProcessEnrolImportGetTpl
}

function wbBatchProcessResImport(){
	this.prep = wbBatchProcessResImportPrep;
	this.exec = wbBatchProcessResImportExec;
	this.cancel = wbBatchProcessResImportCancel;
	this.confirm = wbBatchProcessResImportConfirm;
	this.prep_page = wbBatchProcessResImportPrepPage;
	this.get_template = wbBatchProcessResImportTemplate;
	this.get_instr = wbBatchProcessResImportInstr;
	this.execRewriting = wbBatchProcessResImportExecRewriting
}


function wbBatchProcessResExport(){
	this.prep = wbBatchProcessResExportPrep;
	this.export_que_exe = wbBatchProcessResExportExec
}


function wbBatchProcessAttdRateImport(){
	this.exec = wbBatchProcessAttdRateImportExec;
	this.prep = wbBatchProcessAttdRateImportPrep;
	this.confirm =wbBatchProcessAttdRateImportConfirm
}

function wbBatchProcessCredit() {
	this.exec = wbBatchProcessCreditImportExec;
	this.get_instr = wbBatchProcessCreditImportGetInstr;
	this.get_tpl = wbBatchProcessCreditImportGetTpl;
	this.get_log = wbBatchProcessCreditLogGetLog;
}

// - Enrollment Log --------------------------------------------------------
function wbBatchProcessEnrolLogGetLog(itm_id){
	url = wb_utils_invoke_disp_servlet('cmd','get_log_history','module','upload.UploadModule','log_type','ENROLLMENT','stylesheet','bp_enrol_log_get_log.xsl', 'itm_id', itm_id);
	window.location.href = url;

}

// - User Log --------------------------------------------------------------
function wbBatchProcessUserLogGetLog(){
	url = wb_utils_invoke_disp_servlet('cmd','get_log_history','module','upload.UploadModule','log_type','USER','stylesheet','bp_usr_log_get_log.xsl');
	window.location.href = url;
}
function wbBatchProcessUserDelLogGetLog() {
	url = wb_utils_invoke_disp_servlet('cmd', 'get_del_log_history', 'module', 'upload.UploadModule', 'log_type', 'DEL', 'stylesheet', 'bp_del_usr_log_get_log.xsl');
	window.location.href = url;

}
// - Learning Resource Log -------------------------------------------------
function wbBatchProcessResLogGetLog(){
	url = wb_utils_invoke_disp_servlet('cmd','get_que_log_history','module','upload.UploadModule','stylesheet','bp_res_log_get_log.xsl');
	window.location.href = url;
}

// - User Import ----------------------------------------------------------- 
function wbBatchProcessUserImportPrep(){
	url = wb_utils_invoke_disp_servlet('cmd','upload_user_prep','module','upload.UploadModule','stylesheet','bp_usr_import_prep.xsl');
	window.location.href = url;
}

function wbBatchProcessUserDeletePrep(){
	url = wb_utils_invoke_disp_servlet('cmd','upload_del_user_prep','module','upload.UploadModule','stylesheet','bp_usr_del_prep.xsl');
	window.location.href = url;
}
// - User Import ----------------------------------------------------------- 
wb_utils_controller_base = wb_utils_app_base + 'app/';
function wbBatchProcessUserInfoExportPrep(){
	var url = wb_utils_controller_base + 'admin/user/exportUserInfo';
    window.parent.location.href =  url;
}

function wbBatchProcessUserImportPrepURL(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_usr_import_prep.xsl');
	return url
}

function wbBatchProcessUserDelPrepURL(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_usr_del_prep.xsl');
	return url
}

function wbBatchProcessUserImportGetSourceFile(url){
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 				+ '600'
	+ ',height=' 				+ '400'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'yes';
	wbUtilsOpenWin(url,'batchprocess_preview_all_users', false, str_feature);
}

function wbBatchProcessUserImportExec(frm,lang){
	_txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
	if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls')	{
		alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
	} else if (_txtFileName.length > 100)	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else if (getChars(frm.upload_desc.value) > 2000)	{
		alert(eval('wb_msg_'+lang+'_desc_too_long'));
		frm.upload_desc.focus()
	}else{
		frm.src_filename.value = _txtFileName;
		/*
		if (confirm('xml?')) {
			frm.cmd.value = 'upload_user_xml'
		}
		else {
		*/
		frm.cmd.value = 'upload_user';
		//}
		frm.module.value = 'upload.UploadModule';
		frm.stylesheet.value = 'bp_usr_import_confirm.xsl';
		frm.url_failure.value = parent.location.href;
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit()
	}
}

function wbBatchProcessUserDelExec(frm,lang){
	_txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
	if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls')	{
		alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
	} else if (_txtFileName.length > 100)	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else if (getChars(frm.upload_desc.value) > 2000)	{
		alert(eval('wb_msg_'+lang+'_desc_too_long'));
		frm.upload_desc.focus()
	}else{
		frm.src_filename.value = _txtFileName;
		/*
		 if (confirm('xml?')) {
		 frm.cmd.value = 'upload_user_xml'
		 }
		 else {
		 */
		frm.cmd.value = 'upload_del_user';
		//}
		frm.module.value = 'upload.UploadModule';
		frm.stylesheet.value = 'bp_usr_delete_confirm.xsl';
		frm.url_failure.value = parent.location.href;
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit()
	}
}
function wbBatchProcessUserImportPreviewAll(){
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '600'
			+ ',height=' 				+ '400'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',status='				+ 'yes';
	url = wb_utils_invoke_disp_servlet('module','upload.UploadModule','cmd','preview_all_user','stylesheet','bp_usr_import_preview.xsl');
	wbUtilsOpenWin(url,'batchprocess_preview_all_users', false, str_feature);
}

function wbBatchProcessUserImportGetInstr(){
	url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_instr', 'instr_type', 'user', 'stylesheet', 'bp_import_instr.xsl');
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessUserDelGetInstr(){
	url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_del_instr', 'instr_type', 'user', 'stylesheet', 'bp_delete_instr.xsl');
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessUserImportGetTpl(lang, site){
	url = '../htm/import_template/' + site +  '/wb_import_user_profile_template-' + lang + '.xls';
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessUserDelGetTpl(lang, site){
	url = '../htm/import_template/' + site +  '/wb_delete_user_profile_template-' + lang + '.xls';
	wbUtilsOpenWin(url, '');
}
function wbBatchProcessUserImportConfirm(frm,lang) {
	_wbBatchProcessUserImportConfirm(frm,lang,'bp_usr_import_success.xsl');
}
function wbBatchProcessUserDelConfirm(frm,lang) {
	_wbBatchProcessUserDeleteConfirm(frm,lang,'bp_usr_del_success.xsl');
}

function wbBatchProcessUserImportConfirm2(frm,lang) {
	_wbBatchProcessUserImportConfirm(frm,lang,'bp_usr_import_processing.xsl');
}

function _wbBatchProcessUserImportConfirm(frm,lang,stylesheet){
	/*
	if (confirm('xml?')) {
		frm.cmd.value = 'cook_user_xml'
	}
	else {
	*/
	debugger;
	frm.cmd.value = 'cook_user';
	//}
	frm.module.value = 'upload.UploadModule';
	frm.url_success.value = wbBatchProcessUserImportPrepURL();
	frm.url_failure.value = url = wb_utils_invoke_disp_servlet('cmd','upload_user_prep','module','upload.UploadModule','stylesheet','bp_usr_import_prep.xsl');
	frm.stylesheet.value = stylesheet;
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit()
}

function _wbBatchProcessUserDeleteConfirm(frm,lang,stylesheet){
	/*
	 if (confirm('xml?')) {
	 frm.cmd.value = 'cook_user_xml'
	 }
	 else {
	 */
	debugger;
	frm.cmd.value = 'del_user';
	//}
	frm.module.value = 'upload.UploadModule';
	frm.url_success.value = wbBatchProcessUserDelPrepURL();
	frm.url_failure.value = url = wb_utils_invoke_disp_servlet('cmd','upload_del_user_prep','module','upload.UploadModule','stylesheet','bp_usr_del_prep.xsl');
	frm.stylesheet.value = stylesheet;
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit()
}

function wbBatchProcessUserImportCancel(){
	window.history.back()
}

function _wbBatchProcessUserImportPreviewUrl(){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_upload_usr_info',
		'module','upload.UploadModule',
		'stylesheet','bp_usr_import_confirm.xsl'
	);
	return url;
}
// - Enrolment Import ----------------------------------------------------------- 
function wbBatchProcessEnrolImportPrep(itm_id){
	//url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_enrol_import_prep.xsl', 'itm_id', itm_id)	
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_content_info','stylesheet','bp_enrol_import_prep.xsl', 'itm_id', itm_id);
		
	window.location.href = url;
} 

function wbBatchProcessEnrolImportPrepURL(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_enrol_import_prep.xsl');
	return url
} 

function wbBatchProcessEnrolImportExec(frm,lang){
	_txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
	if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls' && _txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xlsx')	{
		alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
	} else if (_txtFileName.length > 100)	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else if (getChars(frm.upload_desc.value) > 2000)	{
		alert(eval('wb_msg_'+lang+'_desc_too_long'));
		frm.upload_desc.focus()
	}else{
		frm.src_filename.value = _txtFileName;
		frm.cmd.value = 'upload_enrollment';
		frm.module.value = 'upload.UploadModule';
		frm.stylesheet.value = 'bp_enrol_import_confirm.xsl';
		frm.url_failure.value = 'javascript:history.back()';
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit()
	}
}
	
function wbBatchProcessEnrolImportViewAll(itm_id){
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '600'
			+ ',height=' 				+ '400'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',status='				+ 'yes';
	url = wb_utils_invoke_disp_servlet('module','upload.UploadModule','cmd','preview_all_enrollment','stylesheet','bp_enrol_import_preview.xsl','itm_id',itm_id,'isExcludes','true');
	wbUtilsOpenWin(url,'batchprocess_preview_all_users', false, str_feature);

}

function wbBatchProcessEnrolImportGetInstr(){
	url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_instr', 'instr_type', 'enrol', 'stylesheet', 'bp_import_instr.xsl');
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessEnrolImportGetTpl(lang, site){
	url = '../htm/import_template/' + site + '/wb_import_enrollment_template-' + lang + '.xls';
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessEnrolImportConfirm(frm,lang){
	frm.cmd.value = 'cook_enrollment';
	frm.module.value = 'upload.UploadModule';
	frm.url_success.value = wbBatchProcessEnrolImportPrepURL();
	frm.url_failure.value = wbBatchProcessEnrolImportPrepURL();
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.stylesheet.value = 'bp_enrol_import_success.xsl';
	frm.submit()
}
// - Learning Resource Import ----------------------------------------------
function wbBatchProcessResImportPrep(que_type){
	if (que_type == 'MC') {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_mc_import_prep.xsl')
	}
	else if (que_type == 'FB') {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_fb_import_prep.xsl')		
	}
	else if (que_type == 'ES') {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_es_import_prep.xsl')		
	}
	else if (que_type == 'TF') {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_tf_import_prep.xsl')		
	}	
	else if (que_type == 'MT') {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_mt_import_prep.xsl')		
	}
	window.location.href = url;
}

function wbBatchProcessResImportPrepPage() {
    url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_upload_res_prep', 'stylesheet','que_import_prep.xsl');	
	window.location.href = url;
}

function wbBatchProcessResImportTemplate(frm, lang) {
	if (!wbUtilsValidateEmptyField(frm.que_type[1], frm.lab_que_type.value)) {
		return;
    }
	var que_type = frm.que_type[1].value;
	if( que_type == 'MC' ) {
		url = '../htm/import_template/wb_import_que_mc_template-' + lang + '.xls';
	} else if( que_type == 'FB' ) {
		url = '../htm/import_template/wb_import_que_fb_template-' + lang + '.xls';
	} else if( que_type == 'ES' ) {
		url = '../htm/import_template/wb_import_que_es_template-' + lang + '.xls';
	} else if( que_type == 'MT' ) {
		url = '../htm/import_template/wb_import_que_mt_template-' + lang + '.xls';
	} else if( que_type == 'TF' ) {
		url = '../htm/import_template/wb_import_que_tf_template-' + lang + '.xls';
	} else if(que_type == 'FSC') {
		url = '../htm/import_template/wb_import_que_fsc_template-' + lang + '.xls';
	} else if(que_type == 'DSC') {
		url = '../htm/import_template/wb_import_que_dsc_template-' + lang + '.xls';
	}
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessResImportInstr(frm, fldName) {
	if (!wbUtilsValidateEmptyField(frm.que_type[1], frm.lab_que_type.value)) {
		return;
    }
    instrType = frm.que_type[1].value;
	url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_instr', 'instr_type', instrType, 'stylesheet', 'bp_import_instr.xsl');
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessResExportPrep() {
    url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','que_export_prep.xsl');	
	window.location.href = url;    
}

function _wbExportValidateFrm(frm,lang){
	
	if (frm.search_id.value != '' && !gen_validate_integer(frm.search_id,eval('wb_msg_'+lang+'_res_id'),lang)) {
			return false
	}

	if (!_wbSearchValidateCompareField(frm,'search_id',eval('wb_msg_'+lang+'_res_id'),lang)) {
			return false
	}
	
	if (!_wbExportValidateTwoDateField(frm,'c',eval('wb_msg_'+lang+'_create_time'),lang))
			return false;
	
	if (!_wbExportValidateTwoDateField(frm,'u',eval('wb_msg_'+lang+'_upd_time'),lang))
			return false;
	
		
	return true
		
}

function _wbExportValidateTwoDateField(frm,fldName,txtfldName,lang){
		
		var before = 0;
		var	after = 0;
				
		fld_pre = 'frm.' + fldName;
		
	
		if ( eval(fld_pre + '_a_yy').value.length != 0 || eval(fld_pre + '_a_mm').value.length != 0 || eval(fld_pre + '_a_dd').value.length != 0 ) {
			before = 1;
			if ( !wbUtilsValidateDate('document.frmXml.'+ fldName + '_a',txtfldName)){
				return false		
			}
		}

	
		if ( eval(fld_pre + '_b_yy').value.length != 0 || eval(fld_pre + '_b_mm').value.length != 0 || eval(fld_pre + '_b_dd').value.length != 0  ) {
			after = 1;
			if ( !wbUtilsValidateDate('document.frmXml.'+fldName + '_b',txtfldName))
				return false
		}

		// compare the dates
		if(eval(fld_pre + '_a_yy').value.length != 0 && eval(fld_pre + '_b_yy').value.length != 0) {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : fldName + '_a', 
				end_obj : fldName + '_b' 
				})) {
				return false;
			}
		}
			
			
	return true

}

function wbBatchProcessResExportExec(frm, lang) {
	if (_wbExportValidateFrm(frm,lang)){
        frm.s_que_type.value = frm.que_type.value;
        if (!wbUtilsValidateEmptyField(frm.que_type, frm.type_fld_name.value)) {
            return false;
        }
        
        if (frm.include_sub[0].checked) {
            frm.s_include_sub.value = 'Y';
        } else if (frm.include_sub[1].checked) {
            frm.s_include_sub.value = 'N';
        }
        
        frm.search_id.value = frm.search_id.value;
        
    	if ( frm.c_a_yy.value.length != 0 )
    		frm.search_create_time_after.value = frm.c_a_yy.value+ '-' + frm.c_a_mm.value + '-' + frm.c_a_dd.value + ' 00:00:00';
    	else
    		frm.search_create_time_after.value = "";
    	if ( frm.c_b_yy.value.length != 0 )
    		frm.search_create_time_before.value = frm.c_b_yy.value + '-' + frm.c_b_mm.value + '-' + frm.c_b_dd.value + ' 23:59:59';
    	else
    		frm.search_create_time_before.value = "";
    	if ( frm.u_a_yy.value.length != 0 )
    		frm.search_update_time_after.value = frm.u_a_yy.value + '-' + frm.u_a_mm.value + '-' + frm.u_a_dd.value + ' 00:00:00';
    	else
    		frm.search_update_time_after.value = "";
    	if ( frm.u_b_yy.value.length != 0 )
    		frm.search_update_time_before.value = frm.u_b_yy.value + '-' + frm.u_b_mm.value + '-' + frm.u_b_dd.value + ' 23:59:59';
    	else
    		frm.search_update_time_before.value = "";
    
    	if(frm.search_diff_lst) {
    		frm.search_diff_lst.value = "";
    		var delimiter = "";
    		if(frm.chk_diff_easy.checked) {
    			frm.search_diff_lst.value += '1';
    			delimiter = "~";
    		}
    		if(frm.chk_diff_normal.checked) {
    			frm.search_diff_lst.value += delimiter + '2';
    			delimiter = "~";
    		}
    		if(frm.chk_diff_hard.checked) {
    			frm.search_diff_lst.value += delimiter + '3';
    		}
    		if (frm.search_diff_lst.value == '1~2~3'){
    			frm.search_diff_lst.value = '';
    		}
    	}
    	
    	tnd_id_lst = "";
    	for(i=0;i<frm.res_tnd_id.options.length;i++)
    		tnd_id_lst += frm.res_tnd_id.options[i].value + "~";
    		
        frm.res_tnd_id_lst.value = tnd_id_lst;
        
        newUrl = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','export_progress.xsl');
    	
    	wb_utils_open_win(newUrl, '', 450, 150);
    }
}

function wbBatchProcessResImportPrepURL(que_type){
	if( que_type == 'MC' ) {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_mc_import_prep.xsl');
	}
	else if( que_type == 'DSC' ) {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_sc_import_prep.xsl');
	}
	else if( que_type == 'FSC' ) {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_sc_import_prep.xsl');
	}	
	else if( que_type == 'FB' ) {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_fb_import_prep.xsl');
	}	
	else if( que_type == 'ES' ) {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_es_import_prep.xsl');
	}
	else if( que_type == 'MT' ) {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_mt_import_prep.xsl');
	}
	else if( que_type == 'TF' ) {
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'bp_tf_import_prep.xsl');
	}		
	return url
} 

function wbBatchProcessResImportExec(frm,lang, fldName){
    obj = frm.que_type[1];
    que_type = obj.value;
    
    if (!wbUtilsValidateEmptyField(obj, fldName)) {
        return false;
    }
    _txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
    if ((que_type != 'FSC' && que_type != 'DSC')
        && _txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'txt') {
    		alert(eval('wb_msg_' + lang + '_upload_valid_usr'))
    } else if ((que_type == 'FSC' || que_type == 'DSC')
        && _txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls') {
		alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
	} else if (_txtFileName.length > 100)	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else if (frm.ulg_desc.value.length > 1000)	{
		alert(eval('wb_msg_'+lang+'_desc_too_long'));
		frm.ulg_desc.focus()
	}else{
		frm.src_filename.value = _txtFileName;
		frm.cmd.value = 'upload_que';
		frm.module.value = 'upload.UploadModule';
		if( que_type == 'MC' ) {
			frm.stylesheet.value = 'bp_mc_import_confirm.xsl';
		} else if( que_type == 'FB' ) {
			frm.stylesheet.value = 'bp_fb_import_confirm.xsl';
		} else if( que_type == 'ES' ) {
			frm.stylesheet.value = 'bp_es_import_confirm.xsl';
		} else if( que_type == 'MT' ) {
			frm.stylesheet.value = 'bp_mt_import_confirm.xsl';
		} else if( que_type == 'TF' ) {
			frm.stylesheet.value = 'bp_tf_import_confirm.xsl';
		} else if( que_type == 'FSC' ) {
			frm.stylesheet.value = 'bp_sc_import_confirm.xsl';
		} else if( que_type == 'DSC' ) {
			frm.stylesheet.value = 'bp_sc_import_confirm.xsl';
		}
		frm.url_failure.value = 'javascript:history.back()';
		frm.que_type.value = que_type;
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit()		
	}
}

/**
 * 因wbBatchProcessResImportExec函数耦合关系，故重新复制一份作为
 * 培训管理->资源管理->导入题目资源 - 第一步：文件上载，使用。
 */
function wbBatchProcessResImportExecRewriting(frm,lang, fldName){
	
    obj = frm.que_type[1];
    que_type = obj.value;
    
    if (!wbUtilsValidateEmptyField(obj, fldName)) {
        return false;
    }
    _txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
    if (_txtFileName == null || _txtFileName.trim().length == 0) {
    		alert(eval('wb_msg_' + lang + '_upload_valid_usr'));
    } else if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'txt') {
		alert(eval('wb_msg_' + lang + '_upload_valid_txt_enro'));
	} else if (_txtFileName.length > 100)	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else if (frm.ulg_desc.value.length > 1000)	{
		alert(eval('wb_msg_'+lang+'_desc_too_long'));
		frm.ulg_desc.focus()
	}else{
		frm.src_filename.value = _txtFileName;
		frm.cmd.value = 'upload_que';
		frm.module.value = 'upload.UploadModule';
		if( que_type == 'MC' ) {
			frm.stylesheet.value = 'bp_mc_import_confirm.xsl';
		} else if( que_type == 'FB' ) {
			frm.stylesheet.value = 'bp_fb_import_confirm.xsl';
		} else if( que_type == 'ES' ) {
			frm.stylesheet.value = 'bp_es_import_confirm.xsl';
		} else if( que_type == 'MT' ) {
			frm.stylesheet.value = 'bp_mt_import_confirm.xsl';
		} else if( que_type == 'TF' ) {
			frm.stylesheet.value = 'bp_tf_import_confirm.xsl';
		} else if( que_type == 'FSC' ) {
			frm.stylesheet.value = 'bp_sc_import_confirm.xsl';
		} else if( que_type == 'DSC' ) {
			frm.stylesheet.value = 'bp_sc_import_confirm.xsl';
		}
		frm.url_failure.value = 'javascript:history.back()';
		frm.que_type.value = que_type;
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit()		
	}
}

function wbBatchProcessResImportConfirm(frm, que_type){
	frm.cmd.value = 'cook_que';
	frm.module.value = 'upload.UploadModule';
	frm.url_success.value = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','que_import_prep.xsl');
	frm.url_failure.value = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','que_import_prep.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.stylesheet.value = 'bp_import_success.xsl';
	frm.submit()
}

function wbBatchProcessResImportCancel(ulg_id, que_type){
    var url_success = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_upload_res_prep', 'stylesheet','que_import_prep.xsl');	
	var url = wb_utils_invoke_disp_servlet('cmd','cancel_cook_que','module','upload.UploadModule','ulg_id',ulg_id,'url_success',url_success);
	window.location.href = url;
}

function wbBatchProcessAttdRateImportPrep(itm_id){
	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 			+ '760'
		+ ',height=' 			+ '400'
		+ ',scrollbars='		+ 'yes'
		+ ',resizable='			+ 'yes';
	url = wb_utils_invoke_disp_servlet('cmd','upload_attdrate_prep','module','course.EvalManagementModule','stylesheet','bp_attdrate_import_prep.xsl','itm_id',itm_id);
	wbUtilsOpenWin(url,'', false,str_feature);
}



function wbBatchProcessAttdRateImportExec(frm,itm_id,lang)
{
	_txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
	if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'txt')	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr'))
	} else if (_txtFileName.length > 100)	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else{
		frm.src_filename.value = _txtFileName;
		frm.cmd.value = 'upload_AttdRate';
		frm.module.value = 'course.EvalManagementModule';
		frm.itm_id.value = itm_id;
		frm.stylesheet.value = 'bp_attdrate_import_confirm.xsl';
		frm.url_failure.value = 'javascript:history.back()';
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url + '?isExcludes=true';
		frm.submit()	
	}	
}

function wbBatchProcessAttdRateImportConfirm(frm,itm_id,lang){
	frm.cmd.value = 'cook_attdrate';
	frm.module.value = 'course.EvalManagementModule';
	frm.itm_id.value = itm_id;
	frm.url_success.value = "../htm/close_and_reload_window.htm";
	frm.url_failure.value ="../htm/close_window.htm";
	frm.submit()
}

function wbBatchProcessCreditImportGetTpl(frm, lang) {
	frm.cmd.value = 'get_credit_tpl';
	frm.module.value = 'upload.UploadModule';
	frm.template_url.value = 'htm/import_template/wb_import_credits_template-' + lang + '.xls';
	frm.submit()
}

function wbBatchProcessCreditImportGetInstr(){

	var url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_instr', 'instr_type', 'credit', 'stylesheet', 'bp_import_instr.xsl');
	wbUtilsOpenWin(url, '');
}

function wbBatchProcessCreditImportExec(frm,lang){
	_txtFileName = _wbBatchProcessUserImportGetFileName(frm.src_filename_path.value);
	if(_txtFileName == "" || _txtFileName == undefined || _txtFileName == null){
		alert(eval('wb_msg_'+ lang +'_upload_file'));
	}else if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls')	{
		alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
	} else if (_txtFileName.length > 100)	{
		alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else if (getChars(frm.upload_desc.value) > 2000)	{
		alert(eval('wb_msg_'+lang+'_desc_too_long'));
		frm.upload_desc.focus()
	}else{
		frm.src_filename.value = _txtFileName;

		frm.cmd.value = 'upload_credit';
		//}		
		frm.module.value = 'upload.UploadModule';
		frm.stylesheet.value = 'bp_usr_credit_import_confirm.xsl';
		frm.url_failure.value = parent.location.href;
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit()
	}
}

function wbBatchProcessCreditLogGetLog(){
	url = wb_utils_invoke_disp_servlet('cmd','get_log_history','module','upload.UploadModule','log_type','CREDIT','stylesheet','bp_credit_log_get_log.xsl');
	window.location.href = url;
}

// - Private Function------------------------------------------------------- 

function _wbBatchProcessUserImportGetFileName(pathname) {
	s = pathname.lastIndexOf("\\");
	if (s == -1) {s = pathname.lastIndexOf("/");}
	if (s == -1) {return pathname;}	
	l = pathname.length - s;
	return pathname.substr(s+1,l);
}