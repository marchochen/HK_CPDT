// ------------------ wizBank Knowledge Management Folder object ------------------- 
// Convention:
//   public functions : use "wbKMSubscribe" prefix 
//   private functions: use "_wbKMSubscribe" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbKMSubscribe(){	
	this.sub_lst = wbKMSubscribeLst
	this.del_exec = wbKMSubscribeDeleteExec
	this.clear_exec = wbKMSubscribeClearExec
	this.folder_prep = wbKMSubscribeFolderPrep
	this.folder_exec = wbKMSubscribeFolderExec
	

}
/* public functions */
function wbKMSubscribeLst(){
	var url = wbKMSubscribeLstUrl()
	window.location.href = url;
}

function wbKMSubscribeLstUrl(){
	var url = wb_utils_invoke_disp_servlet(	
		'cmd', 'get_sub_lst',
		'module','km.KMModule',
		'stylesheet','km_subscribe_lst.xsl'
	)
	return url;
}

function wbKMSubscribeDeleteExec(id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','del_sub',		
		'nod_id_lst',id,
		'module','km.KMModule',
		'url_success',wbKMSubscribeLstUrl(),
		'url_failure',wbKMSubscribeLstUrl()
	)
	window.location.href = url;
}

function wbKMSubscribeClearExec(id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','clear_sub',		
		'nod_id_lst',id,
		'module','km.KMModule',		
		'url_success',wbKMSubscribeLstUrl(),
		'url_failure',wbKMSubscribeLstUrl()
	)	
	window.location.href = url;	
}

function wbKMSubscribeFolderPrep(sys_type,type,id){
	var url = wbKMSubscribeFolderPrepUrl(type,id)
	wb_utils_set_cookie('url_prev',self.location.href)	
	window.location.href = url;
}

function wbKMSubscribeFolderPrepUrl(type,id){
	var stylesheet = 'km_subscribe_folder.xsl'
	
	var url = wb_utils_invoke_disp_servlet(
		'cmd','get_folder',
		'module','km.KMModule',
		'nod_id',id,
		'stylesheet',stylesheet
	)	
	return url;
}

function wbKMSubscribeFolderExec(frm,type,id){
	if (frm.email_send_chk.checked == true){
			frm.email_send_type.value = frm.email_send_type_sel.value;
	}else {
		frm.email_send_type.value = 'NONE';
	}
	
	frm.cmd.value = 'ins_sub'
	frm.module.value = 'km.KMModule'
	frm.nod_id.value = id
	frm.type.value = type
	frm.url_success.value = _wbKMSubscribeLstURL()
	frm.url_failure.value = wb_utils_get_cookie('url_prev')
	frm.action = wb_utils_disp_servlet_url
	frm.method = 'get'
	frm.submit()
}

/* validate function */
function _wbKMSubscribeLstURL(){
	var url = wb_utils_invoke_disp_servlet(	
		'cmd', 'get_sub_lst',
		'module','km.KMModule',
		'stylesheet','km_subscribe_lst.xsl'
	)
	return url;
}
/* private function */
