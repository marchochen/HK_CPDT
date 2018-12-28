// ------------------ wizBank Course Wizard object ------------------- 
// Convention:
//   public functions : use "wbCosWizard" prefix 
//   private functions: use "_wbCosWizard" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
//   urlParam.js
// ------------------------------------------------------------ 
/*
/* constructor */
function wbCosWizard(){

	this.import_aicc_cos = wbCosWizardImportAiccCosExec
	this.import_scorm_cos = wbCosWizardImportScormCosExec
	
	this.ins_res_scorm_prep = wbCosWizardInsResourceScormPrep 			// insert scorm as a resource
	this.ins_res_scorm_exec = wbCosWizardInsResourceScormExec			//insert scorm as a resource
	this.upd_res_scorm		= wbCosWizardUpdResourceScorm
 	this.upd_res_scorm_exec = wbCosWizardUpdResourceScormExec
 	this.edit_in_search = wbCosWizardUpdResourceScormInSearch
	
	this.set_cos_id = wbCosWizardSetCosId
	this.get_cos_id = wbCosWizardGetCosId
	
	this.set_cos_title = wbCosWizardSetCosTitle
	this.get_cos_title = wbCosWizardGetCosTitle

	this.next_step = wbCosWizardNextStep
	this.cancel = wbCosWizardCancel
	this.add_scorm = _wbCosWizardImportScormCos 
	
	this.import_offline_pkg_prep = wbCosWizardImportOfflinePkgPrep
	this.import_offline_pkg_exec = wbCosWizardImportOfflinePkgExec
	
}
// public functions
function wbCosWizardSetCosId (cos_id) {
	_wbCosWizardSetCookie('cos_id', cos_id)	
}
function wbCosWizardGetCosId () {
	return _wbCosWizardGetCookie('cos_id')	
}

function wbCosWizardSetCosTitle (cos_title) {
	_wbCosWizardSetCookie('cos_title', cos_title)	
}
function wbCosWizardGetCosTitle () {
	return _wbCosWizardGetCookie('cos_title')	
}

function wbCosWizardImportAiccCosExec(frm,lang){
	cos_id = wbCosWizardGetCosId ()
	frm.course_id.value = cos_id
	frm.cmd.value = 'import_cos'
	if(_wbCosWizardCheckAICCFile(frm,lang))
	{
	 frm.submit();
	}
}

function wbCosWizardImportOfflinePkgPrep(cos_id){
	wbCosWizardSetCosId(cos_id);
	wbCosWizardSetCosTitle(wb_utils_get_cookie('lrn_soln_itm_title'));
	_wbCosWizardSetCookie('selected_action',1)
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','cos_import_offline_pkg.xsl')
	applet_width = 790;
	applet_height = 420;
	str_feature = 'toolbar=' + 'no' + ',width=' + applet_width + ',height=' + applet_height + ',scrollbars=' + 'auto' + ',resizable=' + 'no' + ',screenX=' + '0' + ',screenY=' + '10' + ',left=' + '0' + ',top=' + '10'
	window.open(url, 'course_content', str_feature);
}
function wbCosWizardImportOfflinePkgExec(frm,lang){
	cos_id = wbCosWizardGetCosId();
	frm.cos_id.value = cos_id;
	frm.rename.value = 'no';
	frm.method = 'post'
	
	if(!_wbCosWizardCheckScormFileEmpty(frm.aicc_zip,'cp',lang) || !_wbCosWizardCheckScormExtension(frm.aicc_zip,'cp',lang)){
		return false;
	}
	
	if(startStatusCheck(frm,lang,false)){
		frm.action = wb_utils_invoke_disp_servlet('upload_listener','true');
		frm.target = 'target_upload';
		frm.cmd.value = 'upload_offline_pkg';
		frm.module.value = 'JsonMod.courseware.CoursewareModule';
	 	frm.submit();
	}
}

function wbCosWizardImportScormCosExec(frm,lang,isExcludes){
	if(isExcludes==undefined||isExcludes=='')
	{
		isExcludes=false;
	}
	if (frm.src_type) {
		if (frm.src_type[src_type_pick_aicc_res_id].checked) {
			res.pick_res(tpl_type,'',cookie_course_id)
			return;
		}
	}
	cos_id = wbCosWizardGetCosId ()
	frm.rename.value = 'no'
	frm.cos_id.value = cos_id
	frm.method = 'post'
	if(frm.src_type && frm.src_type.length>2 && frm.src_type[2].checked){
		if(!_wbCosWizardCheckScormFileEmpty(frm.aicc_zip,'zip',lang) || !_wbCosWizardCheckScormExtension(frm.aicc_zip,'zip',lang)){
			return false;
		}
		frm.url_success.value = "javascript:parent.location.reload();";
		frm.url_failure.value = "javascript:parent.location.reload();";
		if(startStatusCheck(frm,lang,isExcludes)){
			frm.action = wb_utils_invoke_disp_servlet('upload_listener','true');
			frm.target = 'target_upload';
			frm.cmd.value = 'ins_mod_scorm';
			frm.module.value = 'JsonMod.courseware.CoursewareModule';
		 	frm.submit();
		}
	}else{
		frm.module.value = 'importcos.ImportCosModule'
		if (frm.sco_ver && frm.sco_ver[1].checked) {
			frm.cmd.value = 'IMPORT_SCORM_2004';
		} else {
			frm.cmd.value = 'IMPORT_SCORM_1_2';
		}
		frm.env.value = 'wizb'
		frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";
		if(_wbCosWizardCheckScormFile(frm,lang)){
			/*alert(
				'rename: ' + frm.rename.value + '\n' +
				'env: ' + frm.env.value + '\n' +
				'module: ' + frm.module.value + '\n' +
				'cmd: ' + frm.cmd.value + '\n' +
				'cos_id: ' + frm.cos_id.value + '\n' +
				'cos_url_prefix' + frm.cos_url_prefix.value + '\n' +
				'imsmanifest_file_name: ' + frm.imsmanifest_file_name.value + '\n' +
				'aicc_crs: ' + frm.aicc_crs.value + '\n'
			)*/
			frm.submit();
		}
	}
	
}

function wbCosWizardNextStep(frm,lang) {
	if (_wbCosWizardSetLessonCount(frm,lang,true)){
		_wbCosWizardGoModuleType()
	}
}

function wbCosWizardCancel() {
	window.top.close();
}

function wbCosWizardInsResourceScormPrep(sub_type, que_obj_id) {
	url = wb_utils_invoke_servlet('cmd','get_prof','sub_type',sub_type,'stylesheet','ins_res_sco_prep.xsl')
	if(que_obj_id!=null && que_obj_id!=""){
		url += "&obj_id=" + que_obj_id;
	}
	window.location.href = url;
}

function wbCosWizardInsResourceScormExec(frm, lang){
	if( !gen_validate_empty_field(frm.res_title, eval('wb_msg_'+lang+'_title'),lang) ) {
		return false
	}
	
	if(getChars(frm.res_title.value) > 80){
		Dialog.alert(eval('wb_msg_usr_title_too_long'))
		return false
	}	
	
	if (frm.src_type && frm.src_type.length > 1) {
		if (frm.src_type[1].checked) {
			if(!_wbCosWizardCheckScormFileEmpty(frm.aicc_zip,'zip',lang) || !_wbCosWizardCheckScormExtension(frm.aicc_zip,'zip',lang)){
				return false;
			}
		}
	}
	
	
	if (frm.res_desc) {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
		if(getChars(frm.res_desc.value) > 800){
			Dialog.alert(eval('wb_msg_usr_desc_too_long'))
			return false
		}
	}
	cos_id = wbCosWizardGetCosId ()
	frm.rename.value = 'no';
	frm.cos_id.value = cos_id;
	frm.obj_id.value = getParentUrlParam('obj_id')
	frm.env.value = 'wizb';	
	frm.cmd.value= 'INS_RES';
	frm.method = 'post';
	if(frm.src_type && frm.src_type.length>1 && frm.src_type[1].checked){
		if(startStatusCheck(frm,lang,false)){
			frm.action = wb_utils_invoke_disp_servlet('upload_listener','true');
			frm.cmd.value = 'ins_res_scorm';
			frm.module.value = 'JsonMod.courseware.CoursewareModule';
			frm.target = 'target_upload';
			frm.submit();
		}
	}else{
		if(!_wbCosWizardCheckScormFile(frm,lang)){
			return false;
		}	
		
	//	frm.module.value = 'importcos.ImportCosModule'	
	//	frm.cmd.value = 'IMPORT_SCORM_1_2'
		
		//frm.action = wb_utils_disp_servlet_url;
		frm.action = wb_utils_servlet_url;
		frm.submit();
	}
}

function wbCosWizardUpdResourceScorm(res_id) {
	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','upd_res_sco.xsl','url_failure',self.location.href)
	window.location.href = url;
}

function wbCosWizardUpdResourceScormExec(frm, lang) {
	if(frm.res_format[0].checked == true) {
		if(!_wbCosWizardCheckScormFile(frm,lang)){
			return false;
		}
		frm.aicc_zip.outerHTML = frm.aicc_zip.outerHTML;
	}
	frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
	if ( !gen_validate_empty_field(frm.res_title, eval('wb_msg_'+lang+'_title'),lang) ) {
		return false
	}
	
	if (frm.res_desc) {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
	}
	cos_id = wbCosWizardGetCosId ()
	frm.rename.value = 'no';
	frm.cos_id.value = cos_id;
	frm.obj_id.value = getParentUrlParam('obj_id')
	frm.env.value = 'wizb';	
	frm.method = 'post';
	frm.cmd.value= 'UPD_RES';
	if(frm.res_format.length>2 && frm.res_format[1].checked){
		if(!_wbCosWizardCheckScormFileEmpty(frm.aicc_zip,'zip',lang) || !_wbCosWizardCheckScormExtension(frm.aicc_zip,'zip',lang)){
			return false;
		}
		if(startStatusCheck(frm,lang,false)){
			frm.aicc_crs.outerHTML = frm.aicc_crs.outerHTML;
			frm.action = wb_utils_invoke_disp_servlet('upload_listener','true');
			frm.target = 'target_upload';
			frm.cmd.value = 'upd_res_scorm';
			frm.module.value = 'JsonMod.courseware.CoursewareModule';
			frm.submit();
		}
	}else{
		if(frm.res_format.length>2 && frm.res_format[2].checked){
			frm.aicc_crs.outerHTML = frm.aicc_crs.outerHTML;
			frm.aicc_zip.outerHTML = frm.aicc_zip.outerHTML;	
		}
//	frm.module.value = 'importcos.ImportCosModule'	
//	frm.cmd.value = 'IMPORT_SCORM_1_2'
	//frm.action = wb_utils_disp_servlet_url;
		frm.action = wb_utils_servlet_url;
		frm.submit();
	}
}

function wbCosWizardUpdResourceScormInSearch(frm, lang) {
		if(frm.res_format[0].checked == true) {
		if(!_wbCosWizardCheckScormFile(frm,lang)){
			return false;
		}
	}
	if ( !gen_validate_empty_field(frm.res_title, eval('wb_msg_'+lang+'_title'),lang) ) {
		return false
	}

	cos_id = wbCosWizardGetCosId ()
	frm.rename.value = 'no';
	frm.cos_id.value = cos_id;
	frm.obj_id.value = getParentUrlParam('obj_id')
	frm.cmd.value= 'UPD_RES';
//	frm.module.value = 'importcos.ImportCosModule'	
//	frm.cmd.value = 'IMPORT_SCORM_1_2'
	frm.env.value = 'wizb';	
	//frm.action = wb_utils_disp_servlet_url;
	frm.action = wb_utils_servlet_url;
	frm.method = 'post';
	frm.submit();
}
// ----------------- save user inputs ----------------- //
function _wbCosWizardSetLessonCount(frm,lang,bln_validate){
	cos_id = getUrlParam('res_id')
	if (frm.select_type[0].checked){
		_wbCosWizardSetCookie('selected_action',0)
		_wbCosWizardImportAICCCos(cos_id)
		return false;
	}else if (frm.select_type[1].checked){		
		_wbCosWizardSetCookie('selected_action',1)
		_wbCosWizardImportScormCos(cos_id)
		return false;
	}else if (frm.select_type[2].checked){			
		_wbCosWizardSetCookie('selected_action',2)
		_wbCosWizardStartEditCourse(cos_id)
		return false;
	}
	alert(eval('wb_msg_' + lang + '_select_itm'))
	return false;
}

function _wbCosWizardImportAICCCos(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','cos_wizard_import_aicc_cos.xsl')
	window.location.href = url;	
}

function _wbCosWizardImportScormCos(is_inner){
	if (is_inner == 'true'){
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','cos_wizard_import_scorm_cos.xsl','is_inner',is_inner);	
	}else{
		url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','cos_wizard_import_scorm_cos.xsl')			
	}

	window.location.href = url;
}

function _wbCosWizardStartEditCourse(id){
	url = wb_utils_invoke_servlet('cmd','get_cos','course_id',id,'stylesheet','course_authoring_ns.xsl')
	applet_width = 790;
	applet_height = 420;
	
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ applet_width
			+ ',height=' 				+ applet_height
			+ ',scrollbars='			+ 'auto'
			+ ',resizable='				+ 'no'	
			+',screenX='				+ '0'
			+',screenY='				+ '10'
			+',left='					+ '0'
			+',top='					+ '10'					
			
	 wbUtilsOpenWin(url,'course_content',false,str_feature);
}

// Private functions 
function _wbCosWizardSetCookie(token_nm,token_val){
	gen_set_cookie_token('wiz', token_nm, token_val, '')
}

function _wbCosWizardGetCookie(token_nm){
	return gen_get_cookie_token('wiz', token_nm)
}


function _wbCosWizardCheckAICCFile(frm,lang){
	if( _wbCosWizardCheckFileEmpty(frm.aicc_crs,'crs',lang) && _wbCosWizardCheckAICCExtension(frm.aicc_crs,'crs',lang)){
		frm.aicc_crs_filename.value = _wbCosWizardGetAICCFilename(frm.aicc_crs)
	}else{
		return false;
	}
	 if(_wbCosWizardCheckFileEmpty(frm.aicc_cst,'cst',lang) && _wbCosWizardCheckAICCExtension(frm.aicc_cst,'cst',lang) ){
		frm.aicc_cst_filename.value = _wbCosWizardGetAICCFilename(frm.aicc_cst);	
	}else{
		return false;
	}
	if(_wbCosWizardCheckFileEmpty(frm.aicc_des,'des',lang) && _wbCosWizardCheckAICCExtension(frm.aicc_des,'des',lang) ){
		frm.aicc_des_filename.value = _wbCosWizardGetAICCFilename(frm.aicc_des);
	}else{
		return false;
	}
	if(_wbCosWizardCheckFileEmpty(frm.aicc_au,'au',lang) && _wbCosWizardCheckAICCExtension(frm.aicc_au,'au',lang) ){
		frm.aicc_au_filename.value = _wbCosWizardGetAICCFilename(frm.aicc_au);
	}else{
		return false;
	}
	if(frm.aicc_ort.value != ''){
		if(frm.aicc_ort.value != '' && _wbCosWizardCheckAICCExtension(frm.aicc_ort,'ort',lang)){
			frm.aicc_ort_filename.value = _wbCosWizardGetAICCFilename(frm.aicc_ort);
		}else{
			return false;
		}
	}else{
		return true;
	}
	return true
}

function _wbCosWizardCheckScormFile(frm,lang){
	if( _wbCosWizardCheckScormFileEmpty(frm.aicc_crs,'xml',lang) && _wbCosWizardCheckScormExtension(frm.aicc_crs,'xml',lang)){
		frm.imsmanifest_file_name.value = _wbCosWizardGetScormFilename(frm.aicc_crs)
		if (frm.imsmanifest_file_name.value != 'imsmanifest.xml'){
			Dialog.alert(eval('wb_msg_' + lang + '_scorm_file_not_input'))
			return false;	
		}
	}else{
		return false;
	}
	return true;
}

function _wbCosWizardCheckScormZipFile(frm,lang){
	if( _wbCosWizardCheckScormFileEmpty(frm.aicc_zip,'zip',lang) && _wbCosWizardCheckScormExtension(frm.aicc_zip,'zip',lang)){
		return true;
	}else{
		return false;
	}
}

function _wbCosWizardCheckAICCExtension(obj,ext,lang){
	req_ext = '.' + ext
	req_ext_length = req_ext.length
	file_str = obj.value
	file_str_length = obj.value.length
	file_ext = file_str.substring((file_str_length - req_ext_length),file_str_length)
	file_ext = file_ext.toLowerCase()
	
	if( file_ext != req_ext){
		alert(eval('wb_msg_' + lang + '_aicc_file_not_support') + req_ext)
		return false;
	}else{
		return true;
	}
	
}

function _wbCosWizardCheckScormExtension(obj,ext,lang){
	req_ext = '.' + ext
	req_ext_length = req_ext.length
	file_str = obj.value
	file_str_length = obj.value.length
	file_ext = file_str.substring((file_str_length - req_ext_length),file_str_length)
	file_ext = file_ext.toLowerCase()
	
	if( file_ext != req_ext){
		Dialog.alert(eval('wb_msg_' + lang + '_scorm_file_not_support'))		
		return false;
	}else{
		return true;
	}
	
}

function _wbCosWizardGetAICCFilename(obj){
	file_str = obj.value
	file_str_length = obj.value.length
	file_str_start = file_str.lastIndexOf('\\')
	filename = file_str.substring(++file_str_start,file_str_length)
	return filename;
}

function _wbCosWizardGetScormFilename(obj){
	file_str = obj.value
	file_str_length = obj.value.length
	file_str_start = file_str.lastIndexOf('\\')
	filename = file_str.substring(++file_str_start,file_str_length)
	return filename;
}

function _wbCosWizardCheckFileEmpty(obj,ext,lang){
	if (obj.value == ''){
		alert(eval('wb_msg_' + lang + '_aicc_file_not_input') + ' (' + ext + ')')
		obj.focus()
		return false
	}else
	{
		return true;
	}
}

function _wbCosWizardCheckScormFileEmpty(obj,ext,lang){
	if (obj.value == ''){
		var lab = eval('wb_msg_' + lang + '_scorm_file_not_input')
		Dialog.alert(lab)	
		obj.focus()
		return false;
	}else{
		return true;
	}
}