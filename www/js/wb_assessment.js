// ------------------ wizBank Resource object ------------------- 
// Convention:
//   public functions : use "wbResource" prefix 
//   private functions: use "_wbResource" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 




/* constructor */
function wbAssessment() {			
	this.ins_prep = wbAsmInsPrep					
	this.sendFrm = wbAsmSendForm
	this.upd_prep = wbAsmUpdatePrep
	this.upd_prep_no_failure = wbAsmUpdatePrepNoFailure
	this.updFrm = wbAsmUpd
	this.copy = wbAsmCopy
	this.paste = wbAsmPaste
	this.get = wbAsmGet
	this.read = wbAsmGetInSelfWin
	this.get_in_search = wbAsmGetInSearch
	this.edit_in_search = wbAsmEditInSearch
	this.read_in_select_res = wbAsmReadInSelectRes
	this.asm_q = wbAsmQuestion
	this.asm_q_in_self_win = wbAsmQuestionInSelfWin
	this.add_criteria = wbAsmAddCriteria
	this.get_criteria = wbAsmGetCriteria
	this.edit_criteria = wbAsmEditCriteria
	this.del_criteria = wbAsmDeleteCriteria
	this.validate = wbAsmValidateCriteria
	this.del_obj = wbAsmDeleteObjective
	this.del_q = wbAsmDeleteQuestion
	this.preview = wbAsmPreview
	this.preview_learning_res = wbPreviewLearningResource
	this.export_exec = wbAsmExportExec
	this.export_evn = wbEvaluationExportExec
}

function wbAsmValidateCriteria (res_id) {
	var url_success = window.parent.location.href;
	url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','validate_dynamic_assessment','res_id',res_id,'url_success', url_success,'isExcludes',true);

	window.parent.location.href = url;
	
}

function wbAsmAddCriteria(frm,lang) {
	if (_wbAsmValidateEditCriteriaForm(frm,lang) ) {
			
		frm.method = "get"
		frm.action = wb_utils_disp_servlet_url
		frm.res_id.value = wb_utils_get_cookie('container_res_id');
		frm.url_success.value = 'javascript:asm = new wbAssessment; asm.asm_q_in_self_win(' + wb_utils_get_cookie('container_res_id') + ');'
		
		url = window.parent.location.href
		if ( url.indexOf('stylesheet=qFra.xsl') != -1 )
		{	url = setUrlParam('res_privilege',frm.res_privilege.value, url);
			frm.url_success.value = "javascript:window.parent.location.href='" + url + "';document.write('');"
			wb_utils_set_cookie('prev_res_privilege',frm.res_privilege.value);
			wb_utils_set_cookie('prev_res_difficulty',frm.res_difficulty.value);
			wb_utils_set_cookie('prev_res_lan',frm.res_lan.value);
		}
		frm.submit()
	}
}

function wbAsmGetCriteria(res_id,qcs_id) {
	url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_crit_spec','res_id',res_id,'qcs_id',qcs_id,'res_type','ASM','res_subtype','DAS','stylesheet','asm_edit_criteria.xsl','url_failure',self.location.href);
	window.location.href = url;
}

function wbAsmDeleteCriteria(res_id,qcs_id, res_type) {
	if (confirm(wb_msg_confirm_remove_que)) {
	url_success = wbAsmQuestionURL(res_id, res_type)
	url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','del_crit_spec','res_id',res_id,'qcs_id',qcs_id,'res_type','ASM','res_subtype','DAS','url_success',url_success,"isExcludes",true);
	window.parent.location.href = url;
	}
}

function wbAsmDeleteObjective(res_id,obj_id,lang) {
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {	
		url_success = 'javascript:window.parent.location.reload()';
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','del_que_container_obj','res_id',res_id,'obj_id',obj_id,'url_success',url_success);
		window.location.href = url;
	}
}

function wbAsmDeleteQuestion(que_res_id) {
	url_success = window.location.href;
	url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','del_que','que_id_lst',que_res_id,'url_success',url_success);
	window.location.href = url;
}

function wbAsmEditCriteria(frm,lang) {
	if (_wbAsmValidateEditCriteriaForm(frm,lang) ) {
			
		frm.method = "post"
		frm.action = wb_utils_disp_servlet_url
		
		url = window.parent.location.href
		if ( url.indexOf('stylesheet=qFra.xsl') != -1 )
		{	url = setUrlParam('res_privilege',frm.res_privilege.value, url);
			frm.url_success.value = "javascript:window.parent.location.href='" + url + "';document.write('');"
			wb_utils_set_cookie('prev_res_privilege',frm.res_privilege.value);
			wb_utils_set_cookie('prev_res_difficulty',frm.res_difficulty.value);
			wb_utils_set_cookie('prev_res_lan',frm.res_lan.value);
		}
		frm.submit()
	}
}

function wbAsmQuestionURL(res_id, res_subtype, mode){
	if (!mode){
		mode = '';
	}
	if (res_subtype == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','tst_info_frame.xsl', 'mode', mode)
	}else{
		var url_failure = self.location.href;
		url_failure = setUrlParam('url_success','', url_failure);
		url_failure = setUrlParam('url_failure','', url_failure);
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','tst_info_frame.xsl','url_failure',url_failure, 'mode', mode)
	}	
	return url;
}
function wbAsmQuestion (res_id, res_subtype, mode) {
	if (res_subtype == 'FAS') {
		url_success = parent.location.href;
		timestamp = wb_utils_get_cookie('mod_timestamp');												
		url = wbAsmQuestionURL(res_id, res_subtype, mode);
//		'tst_info_frame.xsl')
	}
	else {
		url = wbAsmQuestionURL(res_id, res_subtype, mode);
	}

	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '620'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

	wbUtilsOpenWin(url, 'asm_q', false, str_feature);
}

function wbAsmQuestionInSelfWin (res_id, res_subtype) {
	if (res_subtype == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','asm_criteria_lst.xsl','url_failure',self.location.href)
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','asm_criteria_lst.xsl','url_failure',self.location.href)
	}

	window.location.href = url
}

function wbAsmEditInSearch (res_id, asm_type) {
	if (asm_type == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','res_srh_fas_upd.xsl','url_failure',self.location.href)
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','res_srh_das_upd.xsl','url_failure',self.location.href)
	}
	window.location.href = url;
}

function wbAsmGet(res_id, res_subtype){
	window.location.href = wbAsmGetURL(res_id, res_subtype);
}

function wbAsmGetInSearch(res_id, res_subtype){
	if (res_subtype == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','res_srh_fas_ind.xsl','url_failure',self.location.href)
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','res_srh_das_ind.xsl','url_failure',self.location.href)
	}
	window.location.href = url;

}

function wbAsmGetURL(res_id, res_subtype){
	if (res_subtype == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','asm_fixed_get.xsl','url_failure',self.location.href)
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','asm_get.xsl','url_failure',self.location.href)
	}
	return url;
}


function wbAsmGetInSelfWin(res_id, res_subtype){
	if (res_subtype == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_fixed_assessment','res_id',res_id,'stylesheet','asm_get.xsl','url_failure',self.location.href)
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','asm_get.xsl','url_failure',self.location.href)
	}
	window.location.href = url;
}


function wbAsmReadInSelectRes(res_id, res_subtype){
	if (res_subtype == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','select_res_asm_fixed.xsl','url_failure',self.location.href)
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','select_res_asm.xsl','url_failure',self.location.href)
	}
	window.parent.content.location.href = url;
}

function wbAsmInsPrep(res_subtype, obj_id){

	if (res_subtype == 'FAS') {
		url = wb_utils_invoke_servlet('cmd','get_prof','sub_type',res_subtype,'stylesheet','asm_fixed_ins.xsl','obj_id',obj_id)
	}
	else {
		url = wb_utils_invoke_servlet('cmd','get_prof','sub_type',res_subtype,'stylesheet','asm_ins.xsl','obj_id',obj_id)
	}
	window.location.href = url
}


function wbAsmSendForm(frm,lang) {
	if (_wbAsmValidateForm(frm,lang) ) {
		frm.obj_id.value = getParentUrlParam('obj_id');
		frm.method = "post";
		frm.action = wb_utils_disp_servlet_url;
		var url = wb_utils_get_cookie('mod_url');
		frm.url_success.value = url;
		frm.url_failure.value = url;
		frm.submit();
	}
}

function wbAsmUpd(frm,lang) {
	if (_wbAsmValidateForm(frm,lang) ) {			
		frm.method = "post"
		frm.action = wb_utils_disp_servlet_url
		var url = wbAsmGetURL(frm.res_id.value, frm.res_subtype.value);
		frm.url_success.value = "javascript:window.parent.location.href='" + url + "';"
		frm.submit();
	}
}


function _wbAsmValidateForm(frm,lang) {
	if (frm.res_title) {
		frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
		if (!gen_validate_empty_field(frm.res_title, eval('wb_msg_' + lang + '_title'), lang)) {
			return false
		}
		if(getChars(frm.res_title.value) > 80){
			alert(eval('wb_msg_' + lang + '_title_not_longer'));
			frm.res_title.focus();
			return false;
		}
	}

	if (frm.res_desc.value != undefined && frm.res_desc.value != '') {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
		if(getChars(frm.res_desc.value) > 400){
			alert(eval('wb_msg_usr_desc_too_long'));
			frm.res_desc.focus();
			return false;
		}
	}
	
	return true;
}

function _wbAsmValidateEditCriteriaForm(frm,lang) {

	if (!my_validate_positive_integer(frm.qcs_score, eval('wb_msg_'+lang+'_score_que'),lang)){
		frm.qcs_score.focus()
		return false;
	}
	
	if (!my_validate_positive_integer(frm.qcs_qcount, eval('wb_msg_'+lang+'_ttl_que'),lang)){
		frm.qcs_qcount.focus()
		return false;
	}
	
	return true;
}

function my_validate_positive_integer(fld, txtFldName,lang) {
	val = wbUtilsTrimString(fld.value)
	if (val.length == 0 || isNaN(Number(val)) || Number(val) <= 0){
		if ( lang == ''|| lang == 'en' )
			alert('Please enter positive integer in "' + txtFldName + '"');
		else if ( lang == 'gb' ){
			alert('Please enter positive integer in "' + txtFldName + '"');
			}
		else if (lang == 'ch' ){
			alert('Please enter positive integer in "' + txtFldName + '"');
		}
	
		fld.focus();
		return false;
	}

	return true;
}

function _wbModuleCheckAsmExtension(obj,ext,lang){
	req_ext = '.' + ext
	req_ext_length = req_ext.length
	file_str = obj.value
	file_str_length = obj.value.length
	file_ext = file_str.substring((file_str_length - req_ext_length),file_str_length)
	file_ext = file_ext.toLowerCase()
	
	if( file_ext != req_ext)
	{
		alert(eval('wb_msg_' + lang + '_Asm_file_not_support') + req_ext)
		obj.focus()
		return false;
	}
	else{
		return true;
	}
	
}
function _wbModuleGetAsmFilename(obj){
	file_str = obj.value
	file_str_length = obj.value.length
	file_str_start = file_str.lastIndexOf('\\')
	filename = file_str.substring(++file_str_start,file_str_length)
	return filename;
}

function _wbGetFilename(str) {
	file_str_length = str.lastIndexOf('.')
	file_str = str.substring(0,file_str_length)
	//alert(file_str)
	return file_str
}

function _wbModuleCheckFileEmpty(obj,ext,lang){
	if (obj.value == ''){
		alert(eval('wb_msg_' + lang + '_Asm_file_not_input') + ' (' + ext + ')')
		
		obj.focus()
		return false
	}else
	{
		return true;
	}
}


function wbAsmUpdatePrep(res_id, asm_type){
	if (asm_type == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','asm_fixed_upd.xsl','url_failure',self.location.href)
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','asm_upd.xsl','url_failure',self.location.href)
	}
	window.location.href = url;
}

function wbAsmUpdatePrepNoFailure(res_id, asm_type){
	if (asm_type == 'FAS') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','asm_fixed_upd.xsl')
	}
	else {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',res_id,'stylesheet','asm_upd.xsl')
	}
	window.location.href = url;
}

function wbAsmCopy(res_id,res_type,lang){

	wb_utils_set_cookie('res_from','obj')
	wb_utils_set_cookie('res_id_copy',res_id)
	wb_utils_set_cookie('res_type_copy',res_type)
	alert(eval('wb_msg_' + lang + '_copy_res'))
}



function wbAsmPaste(que_obj_id,lang){
				
	res_id = wb_utils_get_cookie('res_id_copy')
	
	if ( wb_utils_get_cookie('res_from') == 'recycle' ){
		url_success = "javascript:window.parent.location.reload()"
		url = wb_utils_invoke_servlet('cmd','mv_obj_res','res_id',res_id,'fr_obj_id',wb_utils_get_cookie('frm_obj_id'),'to_obj_id',que_obj_id,'url_success',url_success,'url_failure',url_success)
		window.parent.content.location.href = url
	}else if ( wb_utils_get_cookie('res_type_copy') == 'QUE' ){			
		url = wb_utils_invoke_servlet('cmd','get_q','que_id',res_id,'que_obj_id',que_obj_id,'stylesheet',wb_utils_xsl_paste_que)				
		window.parent.content.location.href = url
	}
	else if ( wb_utils_get_cookie('res_type_copy') == 'Asm' ){			
		url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'que_obj_id',que_obj_id,'stylesheet','asm_pst.xsl')
		window.parent.content.location.href = url
	}
	else if (res_id > 0 ){
		url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'que_obj_id',que_obj_id,'stylesheet',wb_utils_xsl_paste_res)
		window.parent.content.location.href = url
		
	     }else {				
			alert(eval('wb_msg_' + lang + '_copy_res_first'));
		}
}

function Asm_filename(frm,lang) {
	
	crs_name = _wbGetFilename(_wbModuleGetAsmFilename(frm.Asm_crs))
	cst_name = _wbGetFilename(_wbModuleGetAsmFilename(frm.Asm_cst))
	des_name = _wbGetFilename(_wbModuleGetAsmFilename(frm.Asm_des))
	au_name = _wbGetFilename(_wbModuleGetAsmFilename(frm.Asm_au))
	
	if(frm.Asm_ort.value != '') {
		ort_name = _wbGetFilename(_wbModuleGetAsmFilename(frm.Asm_ort))
		if( crs_name == cst_name && crs_name == des_name && crs_name == au_name && crs_name== ort_name ) {			
			return true	
		}
	}
	else {
		if( crs_name == cst_name && crs_name == des_name && crs_name == au_name ) {			
			return true
		}
	}
	alert(eval('wb_msg_' + lang + '_Asm_same_filename'))
	frm.Asm_crs.focus()
	return false
}

function wbAsmPreview(res_id){
	url = wb_utils_invoke_servlet('cmd','preview_asm','res_id',res_id,'mod_type','TST','mod_subtype','TST','mod_privilege','AUTHOR','stylesheet','tst_player1.xsl','url_failure','../htm/close_window.htm','url_success','../htm/close_window.htm','res_preview_ind','1');

	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 				+ '787'
	+ ',height=' 				+ '420'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'no';

	if(document.all){
		str_feature += ',top=' + '10' + ',left=' + '10';
	}else{
		str_feature += ',screenX=' + '10' + ',screenY=' + '10';
	}

	test_player = wbUtilsOpenWin(url, 'test_player' + res_id, false, str_feature);
	test_player.focus();
}

function wbPreviewLearningResource(res_id){
	url = wb_utils_invoke_servlet('cmd','preview_learning_res','res_id',res_id,'mod_type','TST','mod_subtype','TST','mod_privilege','AUTHOR','stylesheet','tst_player1.xsl','url_failure','../htm/close_window.htm','url_success','../htm/close_window.htm','res_preview_ind','1');
	
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 				+ '787'
	+ ',height=' 				+ '420'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'no';
	
	if(document.all){
		str_feature += ',top=' + '10' + ',left=' + '10';
	}else{
		str_feature += ',screenX=' + '10' + ',screenY=' + '10';
	}
	
	test_player = wbUtilsOpenWin(url, 'test_player' + res_id, false, str_feature);
	test_player.focus();
}

function wbAsmExportExec(res_id){
	var url = wb_utils_invoke_servlet('cmd','export_tst','res_id',res_id,'mod_type','TST','mod_subtype','TST','mod_privilege','AUTHOR','stylesheet','export_tst.xsl', 'url_success', '../htm/close_window.htm', 'url_failure', '../htm/close_window.htm');
	test_player = wbUtilsOpenWin(url, 'test_player' + res_id);
	test_player.focus();
}

function wbEvaluationExportExec(res_id){
	var url = wb_utils_invoke_servlet('cmd','export_evn','res_id',res_id,'mod_privilege','AUTHOR','stylesheet','export_tst.xsl', 'url_success', '../htm/close_window.htm', 'url_failure', '../htm/close_window.htm');
	test_player = wbUtilsOpenWin(url, 'test_player' + res_id);
	test_player.focus();
}