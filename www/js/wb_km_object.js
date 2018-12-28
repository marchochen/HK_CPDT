// ------------------ wizBank Knowledge Management Object ------------------- 
// Convention:
//   public functions : use "wbKMObject" prefix 
//   private functions: use "_wbKMObject" prefix
// ------------------------------------------------------------ 
var wbKMLibModule = 'km.library.KMLibraryModule'
var wbKMModule = 'km.KMModule'
//KM XSL List
//Since KM /KM Lib Share same JS library, the XSL name use JS function to concat.
//KM
wb_km_xsl_obj_add = 'km_obj_add.xsl';
wb_km_xsl_obj_update = 'km_obj_upd.xsl';
wb_km_xsl_obj_adm_details = 'km_obj_adm_details.xsl';
wb_km_xsl_obj_details = 'km_obj_details.xsl';
wb_km_xsl_obj_checkout_return = 'km_obj_checkout_return.xsl';
wb_km_xsl_obj_history = 'km_obj_history.xsl';
wb_km_xsl_obj_publish = 'km_obj_publish.xsl';
wb_km_xsl_obj_add_domain ='km_obj_add_domain.xsl';
wb_km_xsl_obj_add_folder = 'km_obj_add_folder.xsl';
wb_km_xsl_domain_node_lst = 'km_domain_node_lst.xsl'


//KM Lib
wb_km_xsl_lib_obj_add = 'km_lib_obj_add.xsl';
wb_km_xsl_lib_obj_update = 'km_lib_obj_edit.xsl';
wb_km_xsl_lib_obj_adm_details = 'km_lib_obj_adm_details.xsl';
wb_km_xsl_lib_obj_details = 'km_lib_obj_details.xsl';
wb_km_xsl_lib_obj_checkout_return = 'km_lib_obj_checkout_return.xsl';
wb_km_xsl_lib_obj_history = 'km_lib_obj_history.xsl';
wb_km_xsl_lib_obj_publish = 'km_lib_obj_publish.xsl';
wb_km_xsl_lib_obj_add_domain = 'km_lib_obj_add_domain.xsl';
wb_km_xsl_lib_obj_add_folder = 'km_lib_obj_add_folder.xsl';
wb_km_xsl_lib_domain_node_lst = 'km_lib_domain_node_lst.xsl'



/* constructor */
function wbKMObject(){
	this.ins_prep = wbKMObjectInsPrep
	this.ins_exec = wbKMObjectInsExec	
	this.details = wbKMObjectDetails	
	this.checkOut = wbKMObjectCheckOutExec
	this.checkIn_prep = wbKMObjectCheckInPrep
	this.checkIn_exec = wbKMObjectCheckInExec
	this.del_exec = wbKMObjectDeleteExec
	this.history = wbKMObjectHistory
	this.publish_prep = wbKMObjectPublishPrep
	this.publish_exec = wbKMObjectPublishExec
	this.obj_attch = wbKMObjectAttachment
	this.read_user_prof = wbKMReadUserProfile
	this.read_user_prof_by_usrid = wbKMReadUserProfileByUsrId
	this.download_file = wbKMObjectDownloadFile
	this.mark_del_exec = wbKMObjectMarkDeleteExec
	this.add_domain_prep = wbKMObjectAddDomainPrep
	this.add_domain_exec = wbKMObjectAddDomainExec
	this.add_folder_prep = wbKMObjectAddFolderPrep
	this.add_folder_exec = wbKMObjectAddFolderExec
	this.edit_exec = wbKMObjectEditExec
	this.edit_prep = wbKMObjectEditPrep
	this.popup_search_prep = wbKMObjectPopUpSearchPrep
	this.popup_search_prep_go = wbKMObjectPopUpSearchPrepGo
	this.popup_search_exec = wbKMObjectPopUpSearchExec
	this.popup_search_return_value = wbMKObjectPopUpSearchReturnValue
	this.search_prep = wbKMObjectSearchPrep
	this.search_exec = wbKMObjectSearchExec
	this.checkall = wbKMCheckall
	this.uncheckall = wbKMUncheckall
	this.select_checkall=wbKMSelectCheckall
	this.details_frame = wbKMObjectGetItemDetailsFrame
	this.popup_details = wbKMObjectPopUpGetItemDetails
	this.review_edit=wbKMObjectBookReviewEdit
	this.review_ins=wbKMObjectBookReviewIns
	this.add_review_exec=wbKMObjectBookReviewExec
	this.copy = new wbKMObjectCopy
	this.holding = new wbKMObjectHolding
	this.lib_inquiry_item_search = wbKMObjectInquriyItemSearch
}

function wbKMObjectCopy(){
	this.copies_info = wbKMObjectCopyCopiesInfo
	this.details = wbKMObjectCopyDetails
	this.ins_prep = wbKMObjectCopyInsPrep
	this.ins_exec = wbKMObjectCopyInsExec
	this.upd_prep = wbKMObjectCopyUpdPrep
	this.upd_exec = wbKMObjectCopyUpdExec
	this.del_exec = wbKMObjectCopyDelExec
}
function wbKMObjectHolding(){
	this.details = wbKMObjectHoldingDetails
	this.ins_prep = wbKMObjectHoldingInsPrep
    this.ins_exec = wbKMObjectHoldingInsExec
	this.upd_prep = wbKMObjectHoldingUpdPrep
    this.upd_exec = wbKMObjectHoldingUpdExec
}	  
/* declare subsystem suffix */
function _wbKMObjectGetSysType(sys_type){
	if(sys_type == 'lib'){
		return '_lib'; 
	}else{
		return '';
	}
}
function wbKMObjectBookReviewExec(frm,lang){
	if(frm.rating.value!=''){
	var rating = parseInt(frm.rating.value)
		if(rating<1 || rating>5||isNaN(rating)){
			alert(eval('wb_km_'+lang+'_review_rating'))
			frm.rating.focus();
			return false;
		}
		}
	var url=wb_utils_invoke_disp_servlet(
		'module',wbKMModule
			)
	frm.url_success.value =wbKMObjectDetailsUrl('', frm.obj_nod_id.value,'','LRN_VIEW','','km_obj_item_details_frame.xsl')
	frm.action=url
	frm.submit();
}
/* public functions */

function wbKMObjectPopUpGetItemDetails(obj_nod_id,parent_nod_id,view_type,obj_version){
	var str_feature = 'toolbar='		+ 'no'
				+ ',width=' 				+ '780'
				+ ',height=' 				+ '420'
				+ ',scrollbars='				+ 'yes'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'yes';
	if (obj_version == null) {obj_version = '';}
	if (view_type == null || view_type == '') {view_type = 'LRN_VIEW';}	
	var url = wbKMObjectDetailsUrl('', obj_nod_id,parent_nod_id,view_type,obj_version,'km_obj_item_details_frame.xsl')
	
	obj_detail = window.open(url,'obj_detail',str_feature)	
}


function wbKMObjectBookReviewEdit(node_id,fmg_id){
	var url=wb_utils_invoke_disp_servlet(
		'cmd','edit_review_prep',
		'module',wbKMModule,
		'obj_nod_id',node_id,
		'msg_id',fmg_id,
		'stylesheet','km_book_review_edit.xsl'
			)
			
	window.location.href=url;
}
function wbKMObjectBookReviewIns(node_id){
	var url=wb_utils_invoke_disp_servlet(
		'cmd','ins_review_prep',
		'module',wbKMModule,
		'obj_nod_id',node_id,
		'stylesheet','km_book_review_ins.xsl'
			)
		window.location.href=url;
}
function wbKMReadUserProfileByUsrId(sys_type,usr_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','get_usr',
		'module',wbKMModule,	
		'usr_id',usr_id,
		'stylesheet','km_read_user_profile.xsl'
	)	
	window.location.href = url;
	
}
function wbKMReadUserProfile(sys_type,usr_ent_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','get_usr',
		'module',wbKMModule,	
		'usr_ent_id',usr_ent_id,
		'stylesheet','km_read_user_profile.xsl'
	)	
	window.location.href = url;
	
}
function wbKMObjectInsPrep(sys_type,parent_nod_id,nature){
	var url =  wbKMObjectInsPrepUrl(sys_type,parent_nod_id,nature);
	wb_utils_set_cookie('url_prev',self.location.href)
	window.location.href = url;
}

function wbKMObjectInsPrepUrl(sys_type,parent_nod_id,nature){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','prep_ins_obj',
		'module',wbKMModule,		
		'tvw_id','CREATE_VIEW',
		'parent_nod_id',parent_nod_id,
		'nature',nature,
		'is_new','true',
		'stylesheet',eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) +'_obj_add')	
	)	
	return url;
}

function wbKMObjectInsExec(sys_type,frm,parent_nod_id) {
	if(ValidateFrm(frm)){		
		feed_param_value(frm)
		GenerateXML(frm)
		frm.obj_xml.value = frm.itm_xml.value
		frm.nod_parent_nod_id.value = parent_nod_id
		if(getUrlParam('nature')=='LIBRARY'){
			frm.module.value = wbKMLibModule
		}else{
			frm.module.value = wbKMModule	
		}	
		frm.cmd.value = "ins_obj"		
		frm.url_failure.value = wbKMObjectInsPrepUrl(sys_type, parent_nod_id, getUrlParam('nature'));
		frm.url_success.value = wbKMObjectDetailsUrl('lib', '$id', parent_nod_id, 'READ_VIEW', 'LATEST');
		frm.action = wb_utils_disp_servlet_url		
		frm.method = 'post'			
		//alert(frm.module.value)			
		frm.submit()
	}	
}

function wbKMObjectDetails(sys_type,obj_nod_id,parent_nod_id,view_type,obj_version){
	if (obj_version == null) {obj_version = '';}
	if (view_type == null || view_type == '') {view_type = 'READ_VIEW';}	
	var url = wbKMObjectDetailsUrl(sys_type,obj_nod_id,parent_nod_id,view_type,obj_version,'')
	window.location.href = url;
}

function wbKMObjectDetailsUrl(sys_type, obj_nod_id, parent_nod_id, view_type, obj_version, stylesheet) {
	if (obj_version == null) {
		obj_version = '';
	}
	if (view_type == null || view_type == '') {
		view_type = 'READ_VIEW';
	}

	if (stylesheet != null && stylesheet != '') {

	} else if (view_type == 'UPDATE_VIEW' && obj_version == 'LATEST') {
		stylesheet = eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_obj_update');
	} else if (view_type == 'READ_VIEW' && (obj_version != '' && obj_version != null)) {
		stylesheet = eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_obj_adm_details');
	} else {
		stylesheet = eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_obj_details');
	}

	var url = wb_utils_invoke_disp_servlet(
		'cmd', 'get_obj', 
		'module', wbKMModule, 
		'obj_nod_id', obj_nod_id, 
		'parent_nod_id', parent_nod_id, 
		'tvw_id', view_type, 
		'obj_version', obj_version, 
		'stylesheet', stylesheet
	)
	return url;
}

function wbKMObjectCheckOutExec(sys_type, obj_nod_id,parent_nod_id,obj_update_timestamp){
	if (confirm(wb_msg_km_obj_checkout_confirm)){
		var url = wb_utils_invoke_disp_servlet(
			'cmd','check_out_obj',
			'module',wbKMModule,
			'obj_nod_id',obj_nod_id,
			'obj_update_timestamp',obj_update_timestamp,
			'url_success',wbKMObjectCheckOutReturnUrl(sys_type, obj_nod_id,parent_nod_id),
			'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
		)	
		window.location.href = url;
	}
}

function wbKMObjectCheckOutReturnUrl(sys_type, obj_nod_id,parent_nod_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','get_obj_att',
		'module',wbKMModule,
		'obj_nod_id',obj_nod_id,		
		'stylesheet',eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_obj_checkout_return'),
		'obj_version','LATEST',
		'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
	)
	return url;
}

function wbKMObjectCheckInPrep(sys_type, obj_nod_id,parent_nod_id,view_type,obj_version){
	if (obj_version == null) {obj_version = 'LATEST';}
	if (view_type == null || view_type == '') {view_type = 'UPDATE_VIEW';}	
	var url = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,view_type,obj_version)
	window.location.href = url;	
}

function wbKMObjectCheckInExec(sys_type, frm,obj_nod_id,parent_nod_id,obj_update_timestamp){
	if(ValidateFrm(frm)){		
		feed_param_value(frm)
		GenerateXML(frm)
		frm.obj_xml.value = frm.itm_xml.value
		frm.obj_nod_id.value = obj_nod_id
		frm.obj_update_timestamp.value = obj_update_timestamp
		frm.module.value = wbKMModule			
		frm.cmd.value = "check_in_obj"		
		frm.url_failure.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
		frm.url_success.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
		frm.action = wb_utils_disp_servlet_url		
		frm.method = 'post'	
		//alert(frm.obj_xml.value)	
		frm.submit()
	}	
}

function wbKMObjectDeleteExec(sys_type,obj_nod_id,parent_nod_id,obj_update_timestamp){	
	if (confirm(wb_msg_confirm)){
		var url = wb_utils_invoke_disp_servlet(
			'cmd','del_obj',
			'module',wbKMModule,
			'obj_nod_id',obj_nod_id,
			'obj_update_timestamp',obj_update_timestamp,
			'url_success',_wbKMObjectFolderNodeLstURL(sys_type,'WORK',parent_nod_id),
			'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
		)
		window.location.href = url;
	}	
}

function wbKMObjectHistory(sys_type,obj_nod_id,parent_nod_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','get_obj_hist',
		'module',wbKMModule,
		'obj_nod_id',obj_nod_id,
		'stylesheet',eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_obj_history'),
		'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
	)
	window.location.href = url;
}

function wbKMObjectPublishPrep(sys_type, obj_nod_id,parent_nod_id){
	var url = wbKMObjectPublishPrepUrl(sys_type, obj_nod_id,parent_nod_id)
	window.location.href = url;
}

function wbKMObjectPublishPrepUrl(sys_type, obj_nod_id,parent_nod_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','prep_publish_obj',
		'module',wbKMModule,
		'obj_nod_id',obj_nod_id,
		'stylesheet',eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_obj_publish'),
		'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
	)
	return url;
}

function wbKMObjectPublishExec(sys_type, frm,obj_nod_id,parent_nod_id,obj_update_timestamp){
	frm.domain_id_list.value = _wbKMObjectGetGoldenmanObjLst(frm,frm.domain_id)	
	if (_wbKMObjectValidatePublish(frm)){
		frm.obj_nod_id.value = obj_nod_id
		frm.obj_update_timestamp.value = obj_update_timestamp
		frm.cmd.value = 'publish_obj'
		frm.module.value = wbKMModule
		frm.url_success.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
		frm.url_failure.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
		frm.action = wb_utils_disp_servlet_url		
		frm.method = 'post'
		frm.submit()
	}
}

/* file is in the format xxx/yyy/abc.ext where xxx/yyy is the path relative to "/" */
function wbKMObjectDownloadFile(sys_type, obj_node_id,filename,version) {

	var url = '../cw/skin1/jsp/download.jsp?file=' + 'object/' + obj_node_id + '/' + version + '/' + filename
	window.location.href= url;
}


function wbKMObjectAttachment(sys_type, obj_node_id,filename,version){
	var url = '/object/' + obj_node_id + '/' + version + '/' + filename
	var str_feature = 'toolbar='		+ 'no'
				//+ ',width=' 				+ '400'
				//+ ',height=' 				+ '180'
				+ ',scrollbars='				+ 'no'
				+ ',resizable='				+ 'no'
				+ ',status='				+ 'yes';
	
	obj_atth = window.open(url,'obj_atth',str_feature)
}

/* validate function */
function _wbKMObjectValidatePublish(frm){
	if (frm.domain_id && frm.domain_id_list){
		if (frm.domain_id_list.value == ''){
			alert(wb_msg_km_obj_publish_select_folder)
			return false;
		}
	}
	return true;
}

/* private function */
function _wbKMObjectGetGoldenmanObjLst(frm,obj){
	var i,n,str,ele
	str = ''
	
	n = obj.options.length
	for(i = 0;i < n; i++){
		ele = obj.options[i]
		if (ele.value != '') {str = str + ele.value + "~";}		
	}
	
	if (str != "") {str = str.substring(0, str.length-1)}			
	return str;	
}



function wbKMObjectMarkDeleteExec(sys_type,obj_nod_id,parent_nod_id,obj_update_timestamp,nature){	
	if(nature == 'LIBRARY'){
		var _module = wbKMLibModule
	}else{
		var _module = wbKMModule
	}
	if (confirm(wb_msg_confirm)){
		var url = wb_utils_invoke_disp_servlet(
			'cmd','mark_del_obj',
			'module',_module,
			'obj_nod_id',obj_nod_id,
			'obj_update_timestamp',obj_update_timestamp,
			
			'url_success',_wbKMObjectFolderNodeLstURL(sys_type,'WORK',parent_nod_id),
			'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
		)
		window.location.href = url;
	}	
}

function wbKMObjectAddDomainPrep(sys_type,obj_nod_id,parent_nod_id){
	//For Elib ONLY
	if(sys_type == 'lib'){
	
	var url = wb_utils_invoke_disp_servlet(
		'cmd','prep_add_domain',
		'module',wbKMLibModule,
		'obj_nod_id',obj_nod_id,
		'stylesheet',eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_obj_add_domain'),
		'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
	)
	window.location.href = url;
	}else{
		//alert('This function for elib only')
	}
}


function wbKMObjectAddDomainExec(sys_type,frm,obj_nod_id,parent_nod_id,obj_update_timestamp){
	//For Elib ONLY
	if(sys_type == 'lib'){
		frm.domain_id_list.value = _wbKMObjectGetGoldenmanObjLst(frm,frm.domain_id)	
			frm.obj_nod_id.value = obj_nod_id
			frm.obj_update_timestamp.value = obj_update_timestamp
			frm.cmd.value = 'add_domain'
			frm.module.value = wbKMLibModule
			frm.url_success.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
			frm.url_failure.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
			frm.action = wb_utils_disp_servlet_url		
			frm.method = 'post'
			frm.submit()
	}else{
		//alert('This function for elib only')
	}
}

function wbKMObjectAddFolderPrep(sys_type,obj_nod_id,parent_nod_id){
	//For Elib ONLY
	if(sys_type == 'lib'){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','prep_add_domain',
		'module',wbKMLibModule,
		'obj_nod_id',obj_nod_id,
		'stylesheet',eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type)+'_obj_add_folder'),
		'url_failure',wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
	)
	window.location.href = url;
	}else{
		//alert('This function for elib only')
	}	
}


function wbKMObjectAddFolderExec(sys_type,frm,obj_nod_id,parent_nod_id,obj_update_timestamp){
	//For Elib ONLY
	if(sys_type == 'lib'){
	var folder_nod_id = _wbKMObjectGetGoldenmanObjLst(frm,frm.folder_nod_id);
	if (folder_nod_id == '') {
		alert(wb_msg_km_obj_select_folder);
		return false;
	} else if (folder_nod_id == 0) {
		alert(wb_msg_km_obj_cannot_select_root_folder);
		return false;
	}
			
	frm.obj_nod_id.value = obj_nod_id
	frm.parent_nod_id.value = folder_nod_id;
	frm.obj_update_timestamp.value = obj_update_timestamp
	frm.cmd.value = 'move_obj'
	frm.module.value = wbKMModule
	frm.url_success.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,folder_nod_id,'READ_VIEW','LATEST')
	frm.url_failure.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id,parent_nod_id,'READ_VIEW','LATEST')
	frm.action = wb_utils_disp_servlet_url		
	frm.method = 'post'
	frm.submit()
	}else{
		//alert('This function for elib only')
	}		
}

function wbKMObjectEditExec(sys_type, frm, obj_nod_id, parent_nod_id, obj_update_timestamp) {
	if (ValidateFrm(frm)) {
		feed_param_value(frm)
		GenerateXML(frm)
		frm.obj_xml.value = frm.itm_xml.value
		frm.obj_nod_id.value = obj_nod_id
		frm.obj_update_timestamp.value = obj_update_timestamp
		if (frm.nature.value == 'LIBRARY') {
			frm.module.value = wbKMLibModule
		} else {
			// frm.module.value = wbKMLibModule
			frm.module.value = wbKMModule
		}
		frm.cmd.value = "edit_obj"
		frm.url_failure.value = _wbKMObjectEditPrepURL(sys_type, obj_nod_id, parent_nod_id, 'UPDATE_VIEW', 'LATEST')
		frm.url_success.value = wbKMObjectDetailsUrl(sys_type, obj_nod_id, parent_nod_id, 'READ_VIEW', 'LATEST')
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		// alert(frm.obj_xml.value)
		// alert(frm.module.value)
		frm.submit()
	}
}

function wbKMObjectEditPrep(sys_type, obj_nod_id, parent_nod_id, view_type, obj_version, nature){
    window.location.href = _wbKMObjectEditPrepURL(sys_type, obj_nod_id, parent_nod_id, view_type, obj_version, nature);
}

function _wbKMObjectEditPrepURL(sys_type, obj_nod_id, parent_nod_id, view_type, obj_version, nature) {
	var stylesheet, _module
	if (obj_version == null) {
		obj_version = '';
	}
	if (view_type == null || view_type == '') {
		view_type = 'READ_VIEW';
	}
	if (nature == 'LIBRARY') {
		_module = wbKMLibModule
	} else {
		_module = wbKMModule
	}

	var url = wb_utils_invoke_disp_servlet(
		'cmd', 'get_obj', 
		'module', _module, 
		'obj_nod_id', obj_nod_id, 
		'parent_nod_id', parent_nod_id, 
		'tvw_id', view_type, 
		'obj_version', obj_version, 
		'stylesheet', 'km_lib_obj_edit.xsl'
	);
	return url;
}

function wbKMObjectPopUpSearchPrep(){
	var url = wb_utils_invoke_disp_servlet('cmd','get_folder_main','module',wbKMModule,'stylesheet','km_obj_popup_search.xsl','type','DOMAIN')
	var str_feature = ',width=' 				+ '570'
					+ ',height=' 				+ '260'
					+ ',scrollbars='			+ 'yes'
					+ ',screenX='				+ '10'
					+ ',screenY='				+ '10'
					+ ',status='				+ 'yes'
	gen_open_win(url,"usrWin",570,420,str_feature);
}
function wbKMObjectPopUpSearchPrepGo(){
	var url = wb_utils_invoke_disp_servlet('cmd','get_folder_main','module',wbKMModule,'stylesheet','km_obj_popup_search.xsl','type','DOMAIN')
	window.location.href = url
}

function wbKMObjectPopUpSearchExec(frm,lang){
	var i = 0,all_node='',checked_node='', isSearchinChecked=false, isSearchbyChecked=false;
	for(i=0;i<frm.search_by.length;i++) {
		if(frm.search_by[i].checked){
			isSearchbyChecked = true;
			if(frm.search_by[i].value == 'title') {
				frm.obj_title.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'subtitle') {
				frm.obj_sub_title.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'desc') {
				frm.obj_desc.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'author') {
				frm.obj_author.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'isbn') {
				frm.obj_isbn.value = frm.search_for.value;
			}
		}
	}
	
//	if(frm.nod_id.length) {
//		for(i=0;i<frm.nod_id.length;i++){
//			if(frm.nod_id[i].checked){
//				isSearchinChecked = true
//				checked_node += frm.nod_id[i].value + '~'
//			}
//		}
//	} else {
//		if(frm.nod_id.checked) {
//			checked_node = frm.nod_id.value;
//			isSearchinChecked = true
//		}
//	}
	if(frm.catalog.length){
		for(i=0;i<frm.catalog.length;i++){
			if(frm.catalog[i].selected){
				isSearchinChecked = true
				checked_node += frm.catalog[i].value + '~'
			}
		}
	}
	if(frm.search_for.value == '') {
		alert(eval('wb_msg_' + lang + '_select_search_for'));
		frm.search_for.focus()
		return false;
	}
	if(isSearchbyChecked == false) {
		alert(eval('wb_msg_' + lang + '_select_search_by'));
		return false;
	}
	if(isSearchinChecked == false) {
		alert(eval('wb_msg_' + lang + '_select_search_in'));
		return false;
	}

	frm.nod_id_lst.value = checked_node.substring(0,checked_node.length -1)
	frm.action = wb_utils_disp_servlet_url	
	frm.method = 'get'
	frm.cmd.value = 'adv_search'
	frm.sort_order.value = 'ASC'
	frm.module.value = wbKMLibModule
	frm.page_size.value = 10
	frm.cur_page.value = 1
	frm.stylesheet.value = 'km_obj_popup_search_result.xsl'
	frm.submit();
}


function wbKMObjectInquriyItemSearch(frm,lang){
	var i = 0,all_node='',checked_node='', isSearchinChecked=false, isSearchbyChecked=false, isCompetencyChecked=false;
	for(i=0;i<frm.search_by.length;i++) {
		if(frm.search_by[i].checked){
			isSearchbyChecked = true;
			if(frm.search_by[i].value == 'callnum') {
				frm.obj_callnum.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'title') {
				frm.obj_title.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'desc') {
				frm.obj_desc.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'author') {
				frm.obj_author.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'pub') {
				frm.obj_pub.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'isbn') {
				frm.obj_isbn.value = frm.search_for.value;
			}
		}
	}

	//get competency value 
	var competency = ''
	for (j = 0; j < frm.competencys.length; j++) {
		if (frm.competencys[j].checked) {
			isCompetencyChecked = true;
			competency += frm.competencys[j].value + "~";
		}
	}
	frm.competency.value = competency;
	
	
	/*if(frm.catalog.length){
		for(i=0;i<frm.catalog.length;i++){
			if(frm.catalog[i].selected){
				isSearchinChecked = true
				checked_node += frm.catalog[i].value + '~'
			}
		}
	}*/
	if(frm.catalog && frm.catalog[0].checked){
		isCatalogValue = false;
	}else{
		isCatalogValue = true;
	}
	
	if (frm.search_for.value == '') {
		alert(eval('wb_msg_' + lang + '_select_search_for'));
		frm.search_for.focus()
		return false;
	}
	if (isSearchbyChecked == false) {
		alert(eval('wb_msg_' + lang + '_select_search_by'));
		return false;
	}
	if (isCompetencyChecked == false) {
		alert(eval('wb_msg_' + lang + '_select_competency'));
		return false;
	}
	/*if(isSearchinChecked == false) {
		alert(eval('wb_msg_' + lang + '_select_search_in'));
		return false;
	}*/

	if(isCatalogValue == true){
		if(frm.cat_id_lst.length<=0){
			alert(eval('wb_msg_' + lang + '_select_search_in'));
			return false;
		}else{
			for(i=0;i<frm.cat_id_lst.length;i++){
				checked_node += frm.cat_id_lst[i].value + '~'
			}
		}
	}
	frm.nod_id_lst.value = checked_node.substring(0,checked_node.length -1)
	frm.action = wb_utils_disp_servlet_url	
	frm.method = 'get'
	frm.cmd.value = 'adv_search'
	frm.sort_order.value = 'ASC'
	frm.module.value = wbKMLibModule
	frm.page_size.value = 10
	frm.cur_page.value = 1
	//frm.stylesheet.value = 'km_obj_popup_search_result.xsl'
	frm.stylesheet.value = 'km_lib_inquiry_item_search_result.xsl'
	frm.submit();
}

function wbMKObjectPopUpSearchReturnValue(frm,lang){
	var i=0,isSelected=false,call_num='',call_num_title='',nod_id='',nod_pos='';
	if(frm.obj_radio.length){
		for(i=0;i<frm.obj_radio.length;i++){
			if(frm.obj_radio[i].checked){
				isSelected = true;
				nod_id = frm.obj_radio[i].value;
				nod_pos = frm.obj_pos[i].value;
				break;
			}
		}
		if(isSelected == false){
			alert(wb_msg_km_select_item)
			return;
		}
	}else{
		nod_id = frm.obj_radio.value;
		nod_pos = frm.obj_pos.value;
	}
	call_num  =  eval('frm.call_num_'+ nod_id + '_' + nod_pos + '.value')
	call_num_title = eval('frm.call_num_'+ nod_id +  '_' + nod_pos + '_title.value')
	//alert(call_num + '' + call_num_title)
	if(window.opener){
		if(window.opener.getPopupItemLst){
			window.opener.getPopupItemLst(nod_id,call_num,call_num_title);
		}
	}
	window.close()
}

function wbKMObjectSearchPrep(target){
	var url = wb_utils_invoke_disp_servlet('module',wbKMModule,'cmd','get_folder_main','type','DOMAIN','stylesheet','km_obj_lrn_search.xsl')
	if(target == null || target == ''){
		target=window;
	}
	target.location.href= url;
}

function wbKMObjectSearchExec(frm, lang){
	var i = 0,all_node='',checked_node='', isSearchinChecked=false, isSearchbyChecked=false;isCatalogValue = false;
	for(i=0;i<frm.search_by.length;i++) {
		if(frm.search_by[i].checked){
			isSearchbyChecked = true;
			if(frm.search_by[i].value == 'title') {
				frm.obj_title.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'desc') {
				frm.obj_desc.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'author') {
				frm.obj_author.value = frm.search_for.value;
			} else if(frm.search_by[i].value == 'pub') {
				frm.obj_pub.value = frm.search_for.value;
			} else if(frm.search_by[i].value =='isbn'){
				frm.obj_isbn.value = frm.search_for.value;
			}
		}
	}
	//get competency value 
	var competency=''
	for(j=0;j<frm.competencys.length;j++){
		if(frm.competencys[j].checked){
			competency += frm.competencys[j].value + "~"; 
		}
	}
	frm.competency.value = competency;
	
	/*
	if(frm.nod_id.length) {
		for(i=0;i<frm.nod_id.length;i++){
			if(frm.nod_id[i].checked){
				isSearchinChecked = true
				checked_node += frm.nod_id[i].value + '~'
			}
		}
	} else {
		if(frm.nod_id.checked) {
			checked_node = frm.nod_id.value;
			isSearchinChecked = true
		}
	}*/
	
	/*if(frm.catalog.length){
		for(i=0;i<frm.catalog.length;i++){
			if(frm.catalog[i].selected){
				isSearchinChecked = true
				checked_node += frm.catalog[i].value + '~'
			}
		}
	}*/
	if(frm.catalog && frm.catalog[0].checked){
		isCatalogValue = false;
	}else{
		isCatalogValue = true;
	}
	if(frm.search_for.value == '') {
		alert(eval('wb_msg_' + lang + '_select_search_for'));
		frm.search_for.focus()
		return false;
	}
	if(isSearchbyChecked == false) {
		alert(eval('wb_msg_' + lang + '_select_search_by'));
		return false;
	}
	/*if(isSearchinChecked == false) {
		alert(eval('wb_msg_' + lang + '_select_search_in'));
		return false;
	}*/
	
	if(isCatalogValue == true){
		if(frm.cat_id_lst.length<=0){
			alert(eval('wb_msg_' + lang + '_select_search_in'));
			return false;
		}else{
			for(i=0;i<frm.cat_id_lst.length;i++){
				checked_node += frm.cat_id_lst[i].value + '~'
			}
		}
	}
	
	frm.nod_id_lst.value = checked_node.substring(0,checked_node.length -1)
	frm.action = wb_utils_disp_servlet_url	
	frm.method = 'get'
	frm.cmd.value = 'adv_search'
	frm.sort_order.value = 'ASC'
	frm.module.value = wbKMLibModule
	frm.page_size.value = 10
	frm.cur_page.value = 1
	frm.stylesheet.value = 'km_obj_lrn_search_result.xsl'
	frm.is_lrn.value = true;
	frm.submit();
}

function wbKMObjectGetItemDetailsFrame(obj_nod_id,parent_nod_id,view_type,obj_version){
	if (obj_version == null) {obj_version = '';}
	if (view_type == null || view_type == '') {view_type = 'LRN_VIEW';}	
	var url = wbKMObjectDetailsUrl('', obj_nod_id,parent_nod_id,view_type,obj_version,'km_obj_item_details_frame.xsl')
	window.location.href = url;
}


//for wbKMObjectCopy
function wbKMObjectCopyCopiesInfo(sys_type, obj_nod_id,loc_id){
	var url = wbKMObjectCopyCopiesInfoURL(sys_type, obj_nod_id,loc_id)
	window.location.href = url;
}

function wbKMObjectCopyCopiesInfoURL(sys_type, obj_nod_id,loc_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','obj_copy_lst',
		'module',wbKMLibModule,
		'obj_nod_id',obj_nod_id,
		'stylesheet','km_lib_obj_copies_info.xsl'
		)
	if(loc_id != null || loc_id != ''){
		url+= '&loc_id=' + loc_id
	}
	return url;
	
}

function wbKMObjectCopyDetails(sys_type, obj_nod_id,loc_id){
	var url = wbKMObjectCopyDetailsURL(sys_type, obj_nod_id,loc_id)
	window.location.href = url;
}

function wbKMObjectCopyDetailsURL(sys_type, obj_nod_id,loc_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','obj_copy_lst',
		'module',wbKMLibModule,
		'obj_nod_id',obj_nod_id,
		'loc_id',loc_id,
		'stylesheet', 'km_lib_obj_copy_details.xsl'
		)
	return url;
}

function wbKMObjectCopyInsPrep(sys_type, obj_nod_id){
	var url = wbKMObjectCopyInsPrepURL(sys_type, obj_nod_id)
	window.location.href = url;	
}

function wbKMObjectCopyInsPrepURL(sys_type, obj_nod_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','prep_ins_obj_copy',
		'module',wbKMLibModule,
		'obj_nod_id',obj_nod_id,
		'stylesheet','km_lib_obj_copy_ins.xsl'
		)
	return url;
}

function wbKMObjectCopyInsExec(sys_type, frm,lang){    
    if (frm.loc_notes.value.length > 1000) {
        alert(frm.lab_notes.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_notes.focus()
        return false;
    }
	if (frm.loc_desc.value.length > 1000) {
        alert(frm.lab_description.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_desc.focus()
        return false;
    }
    if (frm.loc_copy && !gen_validate_empty_field(frm.loc_copy, wb_msg_km_call_no, lang)) {
        return false;
    } 
    frm.method = 'post'
    frm.action = wb_utils_disp_servlet_url
    frm.cmd.value = 'ins_obj_copy'
    frm.module.value = wbKMLibModule
	
	var url = wb_utils_invoke_disp_servlet(
		'cmd', 'obj_copy_lst',
		'module', wbKMLibModule,
		'obj_nod_id', frm.obj_nod_id.value,
		'loc_id', '$id',
		'stylesheet', 'km_lib_obj_copy_details.xsl'
	);
    frm.url_success.value = url;
    frm.url_failure.value = wbKMObjectCopyInsPrepURL(sys_type, frm.obj_nod_id.value)
    frm.submit();
}

function wbKMObjectCopyUpdPrep(sys_type, obj_nod_id,loc_id){
	var url = wb_utils_invoke_disp_servlet(
		'cmd','obj_copy_lst',
		'module',wbKMLibModule,
		'obj_nod_id',obj_nod_id,
		'loc_id',loc_id,
		'stylesheet','km_lib_obj_copy_upd.xsl'
		)
	window.location.href = url;
}

function wbKMObjectCopyUpdExec(sys_type, frm,lang){ 
    if (frm.loc_notes.value.length > 1000) {
        alert(frm.lab_notes.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_notes.focus()
        return false;
    }
	if (frm.loc_desc.value.length > 1000) {
        alert(frm.lab_description.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_desc.focus()
        return false;
    }
    if (frm.loc_copy && !gen_validate_empty_field(frm.loc_copy, wb_msg_km_call_no, lang)) {
        return false;
    } 
	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'upd_obj_copy'
	frm.module.value = wbKMLibModule
	frm.url_success.value = wbKMObjectCopyDetailsURL(sys_type, frm.obj_nod_id.value,frm.loc_id.value)
	
    var url = wb_utils_invoke_disp_servlet('cmd', 'obj_copy_lst', 'module', wbKMLibModule, 'obj_nod_id', frm.obj_nod_id.value, 'loc_id', frm.loc_id.value, 'stylesheet', 'km_lib_obj_copy_upd.xsl')
	frm.url_failure.value = url
	frm.submit();
}

function wbKMObjectCopyDelExec(sys_type, frm,lang){
	if(confirm(wb_msg_confirm)){
	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'del_obj_copy'
	frm.module.value = wbKMLibModule
	frm.url_success.value = wbKMObjectCopyCopiesInfoURL(sys_type, frm.obj_nod_id.value,0)
	frm.url_failure.value = wbKMObjectCopyDetailsURL(sys_type, frm.obj_nod_id.value,frm.loc_id.value)
	frm.submit();
	}
}  

// Holding
function wbKMObjectHoldingInsPrep(sys_type, obj_nod_id){
	var url = wb_utils_invoke_disp_servlet('cmd','prep_ins_obj_copy','module',wbKMLibModule,'obj_nod_id',obj_nod_id,'stylesheet','km_lib_obj_holding_ins.xsl');
	window.location.href = url;	
}
 
function wbKMObjectHoldingInsExec(sys_type, frm,lang){   
    if (frm.loc_notes.value.length > 1000) {
        alert(frm.lab_notes.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_notes.focus()
        return false;
    }
	if (frm.loc_desc.value.length > 1000) {
        alert(frm.lab_description.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_desc.focus()
        return false;
    } 
    if (frm.loc_copy && !gen_validate_empty_field(frm.loc_copy, wb_msg_km_call_no, lang)) {
        return false;
    } 
	if (frm.loc_volume_no && !gen_validate_empty_field(frm.loc_volume_no, wb_msg_km_copy_vol_no, lang)) {
        return false;
    }
	if (frm.loc_issue_no && !gen_validate_empty_field(frm.loc_issue_no, wb_msg_km_copy_issue_no, lang)) {
        return false;
    }
    frm.method = 'post'
    frm.action = wb_utils_disp_servlet_url
    frm.cmd.value = 'ins_obj_copy'
    frm.module.value = wbKMLibModule
    var url = wb_utils_invoke_disp_servlet('cmd', 'prep_ins_obj_copy', 'module', wbKMLibModule, 'obj_nod_id', frm.obj_nod_id.value, 'stylesheet', 'km_lib_obj_holding_ins.xsl');
    frm.url_success.value = wb_utils_invoke_disp_servlet(
		'cmd', 'obj_copy_lst',
		'module', wbKMLibModule,
		'obj_nod_id', frm.obj_nod_id.value,
		'loc_id', '$id',
		'stylesheet', 'km_lib_obj_holding_details.xsl'
	);
    frm.url_failure.value = url
    frm.submit();
}

function wbKMObjectHoldingDetails(sys_type, obj_nod_id,loc_id){
	var url = wb_utils_invoke_disp_servlet('cmd','obj_copy_lst','module',wbKMLibModule,'obj_nod_id',obj_nod_id,'loc_id',loc_id,'stylesheet', 'km_lib_obj_holding_details.xsl');
	window.location.href = url;
}

function wbKMObjectHoldingUpdPrep(sys_type, obj_nod_id,loc_id){
	var url = wb_utils_invoke_disp_servlet('cmd','obj_copy_lst','module',wbKMLibModule,'obj_nod_id',obj_nod_id,'loc_id',loc_id,'stylesheet','km_lib_obj_holding_upd.xsl');
	window.location.href = url;
}	

function wbKMObjectHoldingUpdExec(sys_type, frm, lang){
	if (frm.loc_notes.value.length > 1000) {
        alert(frm.lab_notes.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_notes.focus()
        return false;
    }
	if (frm.loc_desc.value.length > 1000) {
        alert(frm.lab_description.value + wb_msg_en_too_long + ', ' + wb_msg_en_word_limit + ' 1000')
        frm.loc_desc.focus()
        return false;
    }
    if (frm.loc_copy && !gen_validate_empty_field(frm.loc_copy, wb_msg_km_call_no, lang)) {
        return false;
    }
	if (frm.loc_volume_no && !gen_validate_empty_field(frm.loc_volume_no, wb_msg_km_copy_vol_no, lang)) {
        return false;
    }
	if (frm.loc_issue_no && !gen_validate_empty_field(frm.loc_issue_no, wb_msg_km_copy_issue_no, lang)) {
        return false;
    }

	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'upd_obj_copy'
	frm.module.value = wbKMLibModule
    var detailsUrl = wb_utils_invoke_disp_servlet('cmd', 'obj_copy_lst', 'module', wbKMLibModule, 'obj_nod_id', frm.obj_nod_id.value, 'loc_id', frm.loc_id.value, 'stylesheet', 'km_lib_obj_holding_details.xsl');
	frm.url_success.value = detailsUrl
	
	var url = wb_utils_invoke_disp_servlet('cmd','obj_copy_lst','module',wbKMLibModule,'obj_nod_id',frm.obj_nod_id.value,'loc_id',frm.loc_id.value,'stylesheet','km_lib_obj_holding_upd.xsl');
	frm.url_failure.value = url
	frm.submit();
}


function wbKMCheckall(frm, elementName){
	
	var i, n, ele
	n = frm.elements.length
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
		if (ele.name == elementName && ele.type == "checkbox" && ele.checked == false){
			ele.checked = true;
		}
	}	
}

function wbKMUncheckall(frm, elementName){
	
	var i, n, ele
	n = frm.elements.length
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
		if (ele.name == elementName && ele.type == "checkbox" && ele.checked == true){
			ele.checked = false;
		}
	}	
}
function wbKMSelectCheckall(frm,elementName,checkall){
	var i, n, ele
	n = frm.elements.length
	
					for (i=0; i<n; i++){
					ele=frm.elements[i]
					if (ele.name == elementName && ele.type == "select-multiple"){
					for(j=0;j<ele.length;j++){
						if(checkall=='yes'){
							ele[j].selected=true;
							
						}else{
							ele[j].selected=false;
						}
						}
					}
			}
		}
//==============================================
function _wbKMObjectFolderNodeLstURL(sys_type,type,id,sort_col,sort_order,cur_page,page_size,timestamp){
	if (sort_col == null || sort_col == '') {sort_col = 'title';}
	if (sort_order == null || sort_order == '') {sort_order = 'ASC';}
	if (cur_page == null || cur_page == '') {cur_page = '1';}
	if (page_size == null || page_size == '') {page_size = '10';}
	if (timestamp == null || timestamp == '') {timestamp = '';}
	var stylesheet = eval('wb_km_xsl' + _wbKMObjectGetSysType(sys_type) + '_' + type.toLowerCase() + '_node_lst')
		
	var url = wb_utils_invoke_disp_servlet(		
		'cmd','get_folder_lst',
		'module','km.KMModule',
		'nod_id',id,
		'sort_col',sort_col,
		'sort_order',sort_order,
		'cur_page',cur_page,
		'page_size',page_size,
		'timestamp',timestamp,
		'stylesheet',stylesheet
	)
	return url;	
}

