// ------------------ wizBank AICC object ------------------- 
// Convention:
//   public functions : use "wbAICC" prefix 
//   private functions: use "_wbAICC" prefix
// ------------------------------------------------------------ 

/* constructor */
function wbAicc() {			
	this.ins_prep = wbAiccInsPrep					
	this.sendFrm = wbAiccSendForm
	this.upd_prep = wbAiccUpdatePrep
	this.copy = wbAiccCopy
	this.read = wbAiccGetInSearch
	this.edit_in_search = wbAiccEditInSearch
}


function wbAiccEditInSearch (res_id) {

	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','res_srh_aicc_upd.xsl')
	window.location.href = url
}

function wbAiccGetInSearch(res_id){

	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','res_srh_aicc_ind.xsl','url_failure',self.location.href)	
	window.location.href = url;

}

function wbAiccInsPrep(sub_type,que_obj_id){
	url = wb_utils_invoke_servlet('cmd','get_prof','sub_type',sub_type,'stylesheet','aicc_ins.xsl')
	if(que_obj_id!=null && que_obj_id!=""){
		url += "&obj_id=" + que_obj_id;
	}
	window.location.href = url
}


function wbAiccSendForm(frm,lang,flag) {
	if (_wbAiccValidateForm(frm,lang) ) {
		frm.obj_id.value = wb_utils_get_cookie('que_obj_id_ass')
		if(frm.obj_id.value==""){
			frm.obj_id.value = getUrlParam("obj_id");
		}
		frm.method = "post"
		frm.action = wb_utils_servlet_url
		
		url = window.location.href;
		if ( url.indexOf('stylesheet=res_fra.xsl') != -1 )
		{	url = setUrlParam('res_privilege',frm.res_privilege.value, url);
			frm.url_success.value = url;
			//wb_utils_set_cookie('prev_res_privilege',frm.res_privilege.value);
			//wb_utils_set_cookie('prev_res_difficulty',frm.res_difficulty.value);
			//wb_utils_set_cookie('prev_res_lan',frm.res_lan.value);
		}
		if (frm.res_type.value == 'AICC') {
			frm.url_success.vaule = url;
			if(flag == true) {
				frm.url_success.vaule = gen_get_cookie('url_success');
			}
			frm.url_failure.vaule = url;
			if (frm.res_format != null && (frm.res_format.length > 1 && frm.res_format[1].checked)){
				if(startStatusCheck(frm,lang,false)){
					frm.action = '/servlet/Dispatcher?upload_listener=true';
					frm.target = 'target_upload';
					if (frm.cmd.value == "ins_res") {
						frm.cmd.value = 'ins_res_aicc';
					} else if (frm.cmd.value = "upd_res") {
						frm.cmd.value = 'upd_res_aicc';
					}
					frm.module.value = 'JsonMod.courseware.CoursewareModule';
				 	frm.submit();
				}
				return;
			}
		}
		frm.submit();
	}
}


function _wbAiccValidateForm(frm,lang) {

	if (frm.res_title) {
		frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
		if ( !gen_validate_empty_field(frm.res_title, eval('wb_msg_'+lang+'_title'),lang) ) {		
			return false
		}
	}
	
	if(getChars(frm.res_title.value) > 80){
		Dialog.alert(eval('wb_msg_usr_title_too_long'))
		frm.res_title.focus()
		return false
	}
	
	if (frm.res_desc) {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
		if(getChars(frm.res_desc.value) > 800){
			Dialog.alert(eval('wb_msg_usr_desc_too_long'))
			frm.res_desc.focus()
			return false
		}
	}
	if(frm.res_format == null || (frm.res_format != null && frm.res_format[0].checked)) {
		if( _wbModuleCheckFileEmpty(frm.aicc_crs,'crs',lang) && _wbModuleCheckAICCExtension(frm.aicc_crs,'crs',lang)){
			frm.aicc_crs_filename.value = _wbModuleGetAICCFilename(frm.aicc_crs)		
		}else{
			return false;
		}
		
		if(_wbModuleCheckFileEmpty(frm.aicc_cst,'cst',lang) && _wbModuleCheckAICCExtension(frm.aicc_cst,'cst',lang) ){
			frm.aicc_cst_filename.value = _wbModuleGetAICCFilename(frm.aicc_cst);	
		}else{
			return false;
		}
	
		if(_wbModuleCheckFileEmpty(frm.aicc_des,'des',lang) && _wbModuleCheckAICCExtension(frm.aicc_des,'des',lang) ){
			frm.aicc_des_filename.value = _wbModuleGetAICCFilename(frm.aicc_des);
		}else{
			return false;
		}
	
		if(_wbModuleCheckFileEmpty(frm.aicc_au,'au',lang) && _wbModuleCheckAICCExtension(frm.aicc_au,'au',lang) ){
			frm.aicc_au_filename.value = _wbModuleGetAICCFilename(frm.aicc_au);
		}else{
			return false;
		}
		
		if(frm.aicc_ort.value != '')
		{
			if(frm.aicc_ort.value != '' && _wbModuleCheckAICCExtension(frm.aicc_ort,'ort',lang)){
				frm.aicc_ort_filename.value = _wbModuleGetAICCFilename(frm.aicc_ort);
			}else{
				return false;
			}
		}
		
		if( !aicc_filename(frm,lang) )
			return false
		else{	
			return true;
		}
	}
	if (frm.res_format != null && (frm.res_format.length > 1 && frm.res_format[1].checked)){
		if(!_wbModuleCheckFileEmpty(frm.aicc_zip,'zip',lang) || !_wbModuleCheckAICCExtension(frm.aicc_zip, 'zip', lang)) {
			return false;
		}
	}
	return true;
}


function _wbModuleCheckAICCExtension(obj,ext,lang){
	req_ext = '.' + ext
	req_ext_length = req_ext.length
	file_str = obj.value
	file_str_length = obj.value.length
	file_ext = file_str.substring((file_str_length - req_ext_length),file_str_length)
	file_ext = file_ext.toLowerCase()
	
	if( file_ext != req_ext)
	{
		alert(eval('wb_msg_' + lang + '_aicc_file_not_support') + req_ext)
		obj.focus()
		return false;
	}
	else{
		return true;
	}
	
}
function _wbModuleGetAICCFilename(obj){
	file_str = obj.value
	file_str_length = obj.value.length
	file_str_start = file_str.lastIndexOf('\\')
	filename = file_str.substring(++file_str_start,file_str_length)
	return filename;
}

function _wbGetFilename(str) {
	file_str_length = str.lastIndexOf('.')
	file_str = str.substring(0,file_str_length)
	return file_str
}

function _wbModuleCheckFileEmpty(obj,ext,lang){
	if (obj.value == ''){
		alert(eval('wb_msg_' + lang + '_aicc_file_not_input') + ' (' + ext + ')')
		
		obj.focus()
		return false
	}else
	{
		return true;
	}
}

function wbAiccUpdatePrep(res_id){
	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','aicc_upd.xsl')
	window.location.href = url;
}

function wbAiccCopy(res_id,res_type,lang){

	wb_utils_set_cookie('res_from','obj')
	wb_utils_set_cookie('res_id_copy',res_id)
	wb_utils_set_cookie('res_type_copy',res_type)
	alert(eval('wb_msg_' + lang + '_copy_res'))
}

function aicc_filename(frm,lang) {
	
	crs_name = _wbGetFilename(_wbModuleGetAICCFilename(frm.aicc_crs))
	cst_name = _wbGetFilename(_wbModuleGetAICCFilename(frm.aicc_cst))
	des_name = _wbGetFilename(_wbModuleGetAICCFilename(frm.aicc_des))
	au_name = _wbGetFilename(_wbModuleGetAICCFilename(frm.aicc_au))
	
	if(frm.aicc_ort.value != '') {
		ort_name = _wbGetFilename(_wbModuleGetAICCFilename(frm.aicc_ort))
		if( crs_name == cst_name && crs_name == des_name && crs_name == au_name && crs_name== ort_name ) {			
			return true	
		}
	}
	else {
		if( crs_name == cst_name && crs_name == des_name && crs_name == au_name ) {			
			return true
		}
	}
	alert(eval('wb_msg_' + lang + '_aicc_same_filename'))
	frm.aicc_crs.focus()
	return false
}
