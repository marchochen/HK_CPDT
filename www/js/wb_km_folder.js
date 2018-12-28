// ------------------ wizBank Knowledge Management Folder object ------------------- 
// Convention:
//   public functions : use "wbKMFolder" prefix 
//   private functions: use "_wbKMFolder" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 
//KM XSL List
//Since KM /KM Lib Share same JS library, the XSL name use JS function to concat.
//KM
wb_km_xsl_domain_lst = 'km_domain_lst.xsl'
wb_km_xsl_domain_node_lst = 'km_domain_node_lst.xsl'
wb_km_xsl_domain_ins = 'km_domain_ins.xsl'
wb_km_xsl_domain_upd = 'km_domain_upd.xsl'
wb_km_xsl_work_lst = 'km_work_lst.xsl'
wb_km_xsl_work_node_lst = 'km_work_node_lst.xsl'
wb_km_xsl_work_ins = 'km_work_ins.xsl'
wb_km_xsl_work_upd = 'km_work_upd.xsl'
//KM Lib
wb_km_xsl_lib_domain_node_lst = 'km_lib_domain_node_lst.xsl'
wb_km_xsl_lib_domain_ins = 'km_lib_domain_ins.xsl'
wb_km_xsl_lib_domain_upd = 'km_lib_domain_upd.xsl'
wb_km_xsl_lib_domain_lst = 'km_lib_domain_lst.xsl'
wb_km_xsl_lib_work_lst = 'km_lib_work_lst.xsl'
wb_km_xsl_lib_work_node_lst = 'km_lib_work_node_lst.xsl'
wb_km_xsl_lib_work_ins = 'km_lib_work_ins.xsl'
wb_km_xsl_lib_work_upd = 'km_lib_work_upd.xsl'

/* constructor */
function wbKMFolder(){	
	this.folder_lst = wbKMFolderSubFolderLst
	this.ins_prep = wbKMFolderInsPrep
	this.ins_exec = wbKMFolderInsExec
	this.edit_prep = wbKMFolderEditPrep
	this.edit_exec = wbKMFolderEditExec
	this.del_exec = wbKMFolderDelExec
	this.folder_main = wbKMFolderMain
	this.add_to_my_workplace = wbKMFolderAddToMyWorkplace
	this.assign_workplace_prep = wbKMFolderAssignWorkplacePrep
	this.assign_workplace_exec = wbKMFolderAssignWorkplaceExec
	this.remove_from_workplace = wbKMFolderRemove
}
/* declare subsystem suffix */
function _wbKMFolderGetSysType(sys_type){
	if(sys_type == 'lib'){
		return '_lib'; 
	}else{
		return '';
	}
}
/* public functions */

function wbKMFolderRemove(nod_id){
	var url = wb_utils_invoke_disp_servlet(		
		'cmd','remove_folder_from_workplace',
		'module','km.KMModule',
		'nod_id', nod_id,
		'url_success', self.location.href
	)
	window.location.href = url;	
}

function wbKMFolderAssignWorkplacePrep(sys_type,nod_id) {
	var stylesheet = 'km_assign_workplace.xsl';
	var url = wb_utils_invoke_disp_servlet(		
		'cmd','get_folder_assigned_workplace',
		'module','km.KMModule',
		'nod_id', nod_id,
		'stylesheet', stylesheet
	)
	window.location.href = url;	

}

function wbKMFolderAssignWorkplaceExec(sys_type, frm, type, nod_id) {

	frm.cmd.value = 'assign_folder_workplace'
	frm.module.value = 'km.KMModule'
	frm.nod_id.value = nod_id
	frm.url_success.value = wbKMFolderSubFolderLstUrl(sys_type, type, nod_id/*,sys_type*/)
	frm.action = wb_utils_disp_servlet_url
	frm.method = 'post'
	frm.assign_ent_lst.value = _wbKMFolderGetSelectionIdLst(frm,frm.assign_ent_id_lst)

	frm.submit()
	
}

function wbKMFolderAddToMyWorkplace(sys_type, nod_id){

	var url = wb_utils_invoke_disp_servlet(		
		'cmd','add_folder_to_workplace',
		'module','km.KMModule',
		'nod_id', nod_id,
		'url_success', self.location.href
	)
	window.location.href = url;	

}

function wbKMFolderMain(sys_type,type){
	var url = _wbKMFolderLstURL(sys_type,type)
	window.location.href = url;	
}

function _wbKMFolderLstURL(sys_type,type){	
	var stylesheet = eval('wb_km_xsl' + _wbKMFolderGetSysType(sys_type)  + '_' + type.toLowerCase() + '_lst')
	
	var url = wb_utils_invoke_disp_servlet(
		'cmd','get_folder_main',
		'module','km.KMModule',
		'type',type,
		'stylesheet',stylesheet
	)
	return url;	
}

function wbKMFolderSubFolderLst(sys_type,type,id,sort_col,sort_order,cur_page,page_size,timestamp){
	if (sort_col == null || sort_col == '') {sort_col = 'title';}
	if (sort_order == null || sort_order == '') {sort_order = 'ASC';}
	if (cur_page == null || cur_page == '') {cur_page = '1';}
	if (page_size == null || page_size == '') {page_size = '10';}
	if (timestamp == null || timestamp == '') {timestamp = '';}
	
	var url = wbKMFolderSubFolderLstUrl(sys_type,type,id,sort_col,sort_order,cur_page,page_size,timestamp)
	window.location.href = url;
}

function wbKMFolderSubFolderLstUrl(sys_type,type,id,sort_col,sort_order,cur_page,page_size,timestamp){
	if (sort_col == null || sort_col == '') {sort_col = 'title';}
	if (sort_order == null || sort_order == '') {sort_order = 'ASC';}
	if (cur_page == null || cur_page == '') {cur_page = '1';}
	if (page_size == null || page_size == '') {page_size = '10';}
	if (timestamp == null || timestamp == '') {timestamp = '';}
	var stylesheet = eval('wb_km_xsl'+ _wbKMFolderGetSysType(sys_type)  +'_' + type.toLowerCase() + '_node_lst')
		
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

function wbKMFolderInsPrep(sys_type,type,id){
	var url = wbKMFolderInsPrepUrl(sys_type,type,id)
	wb_utils_set_cookie('url_prev',self.location.href)
	window.location.href = url;
}

function wbKMFolderInsPrepUrl(sys_type,type,id){
	var stylesheet = eval('wb_km_xsl'+  _wbKMFolderGetSysType(sys_type) +'_' + type.toLowerCase() + '_ins')
	
	if (id != null && id != ''){	
		var url = wb_utils_invoke_disp_servlet(
			'cmd','get_folder',
			'module','km.KMModule',
			'nod_id',id,
			'stylesheet',stylesheet
		)
	}else{
		var url = wb_utils_invoke_disp_servlet(			
			'cmd','get_prof',
			'module','km.KMModule',
			'stylesheet',stylesheet
		)
	}
	return url;
}

function wbKMFolderInsExec(sys_type,type,id,frm,lang){	
	if (_wbKMFolderInsEditValidateFrm(frm,lang)) {
		if(frm.nod_display_option_ind) {
			if(_wbKMFolderGetRadioObjectCheckedValue(frm, frm.nod_display_option_ind)==2) {
				frm.display_option_ind.value = 1;
			} else {
				frm.display_option_ind.value = _wbKMFolderGetRadioObjectCheckedValue(frm, frm.nod_display_option_ind);
			}
		}
		frm.cmd.value = 'ins_folder'		
		frm.module.value = 'km.KMModule'
		frm.type.value = type
		frm.parent_nod_id.value = id
		frm.url_success.value = wb_utils_get_cookie('url_prev')
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'get'
		frm.submit()
	}
}

function wbKMFolderEditPrep(sys_type,type,id){
	var url = wbKMFolderEditPrepUrl(sys_type,type,id)
	window.location.href = url;
}

function wbKMFolderEditPrepUrl(sys_type,type,id){
	var stylesheet = eval('wb_km_xsl' +  _wbKMFolderGetSysType(sys_type) + '_' + type.toLowerCase() + '_upd')
	
	var url = wb_utils_invoke_disp_servlet(
		'cmd','get_folder',
		'module','km.KMModule',
		'nod_id',id,
		'stylesheet',stylesheet
	)		
	return url;
}

function wbKMFolderEditExec(sys_type,type,id,frm,lang){
	if (_wbKMFolderInsEditValidateFrm(frm,lang)) {
		if(frm.nod_display_option_ind) {
			if(_wbKMFolderGetRadioObjectCheckedValue(frm, frm.nod_display_option_ind)==2) {
				frm.display_option_ind.value = 1;
			} else {
				frm.display_option_ind.value = _wbKMFolderGetRadioObjectCheckedValue(frm, frm.nod_display_option_ind);
			}
		}
		frm.cmd.value = 'upd_folder'		
		frm.module.value = 'km.KMModule'
		//frm.type.value = type
		frm.nod_id.value = id		
		frm.url_success.value = wbKMFolderSubFolderLstUrl(sys_type,type,id)
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'		
		frm.submit()
	}
}

function wbKMFolderDelExec(sys_type,type,id,timestamp,parent_id){
	var url_success
	
	if (parent_id == null || parent_id == '') {url_success = _wbKMFolderLstURL(sys_type,type);}
	else {url_success = wbKMFolderSubFolderLstUrl(sys_type,type,parent_id);}		
	
	if (confirm(eval('wb_msg_confirm'))) {
		var url = wb_utils_invoke_disp_servlet(
			'cmd','del_folder',
			'module','km.KMModule',
			'nod_id',id,
			'update_timestamp',timestamp,
			'url_success', url_success,
			'url_failure',wbKMFolderSubFolderLstUrl(sys_type,type,id)
		)
		window.location.href = url;
	}	
}

/* validate function */
function _wbKMFolderInsEditValidateFrm(frm,lang){
	//title
	if (frm.title){
		frm.title.value = wbUtilsTrimString(frm.title.value);
		if (!gen_validate_empty_field(frm.title, eval('wb_msg_'+ lang + '_title'),lang)) {return false;};
	}
	
	//desc
	if (frm.desc){
		frm.desc.value = wbUtilsTrimString(frm.desc.value);
		//if (!gen_validate_empty_field(frm.desc, eval('wb_msg_'+ lang + '_desc'),lang)) {return false;};
		if (frm.desc.value.length > 200) {
			alert(wb_msg_km_folder_desc_too_long);
			return false;
		}
	}
		
	//inherit_ind
	if (frm.inherit_ind){
		if (frm.inherit_ind_sel){			
			if (frm.inherit_ind_sel.checked == true) {frm.inherit_ind.value = 'true'}
			else {frm.inherit_ind.value = 'false';}
		}else{
			frm.inherit_ind.value = 'false';
		}		
	}	

	//reader_ent_lst
	if (frm.reader_ent_lst){	
		if(frm.nod_display_option_ind) {
			if (_wbKMFolderGetRadioObjectCheckedValue(frm, frm.nod_display_option_ind)==1 && frm.reader_ent_id_lst && frm.reader_ent_id_lst.length == 0 /*&& frm.inherit_ind.value == 'false'*/){
				alert(wb_msg_km_folder_select_reader_ent_lst)
				frm.reader_ent_id_lst.focus();
				return false;
			}else{
				frm.reader_ent_lst.value = _wbKMFolderGetSelectionIdLst(frm,frm.reader_ent_id_lst)
			}		
		} else {
			if (frm.reader_ent_id_lst && frm.reader_ent_id_lst.length == 0 && frm.inherit_ind.value == 'false'){
				alert(wb_msg_km_folder_select_reader_ent_lst)
				frm.reader_ent_id_lst.focus();
				return false;
			}else{
				frm.reader_ent_id_lst.value = _wbKMFolderGetSelectionIdLst(frm,frm.reader_ent_id_lst)
			}		
		}
	}
	
	//author_ent_lst
	if (frm.author_ent_lst){	
		if (frm.author_ent_id_lst && frm.author_ent_id_lst.length == 0 && frm.inherit_ind.value == 'false'){
			alert(wb_msg_km_folder_select_author_ent_lst)
			frm.author_ent_id_lst.focus();
			return false;
		}else{
			frm.author_ent_lst.value = _wbKMFolderGetSelectionIdLst(frm,frm.author_ent_id_lst)
		}		
	}
	
	//owner_ent_lst
	if (frm.owner_ent_lst){		
		if (frm.owner_ent_id_lst && frm.owner_ent_id_lst.length == 0 /*&& frm.inherit_ind.value == 'false'*/){
			alert(wb_msg_km_folder_select_owner_ent_lst)
			frm.owner_ent_id_lst.focus();
			return false;
		}else{
			frm.owner_ent_lst.value = _wbKMFolderGetSelectionIdLst(frm,frm.owner_ent_id_lst)
		}		
	}
	return true;	
}

/* private function */
function _wbKMFolderGetRadioObjectCheckedValue(frm,obj){
	var i, n, ele, str
	str = ""
	n = obj.length;	
	for (i = 0; i < n; i++) {
		ele = obj[i]		
		if (ele.type == "radio" && ele.checked){
			if (ele.value != "") {str = ele.value;}
		}
	}			
	return str;	
}

function _wbKMFolderGetCheckboxObjectCheckedValue(frm,obj){
	var i, n, ele, str
	str = ""
	n = obj.length;	
	for (i = 0; i < n; i++) {
		ele = obj[i]		
		if (ele.type == "checkbox" && ele.checked){
			if (ele.value != "") {str = str + ele.value + '~';}
		}
	}			
	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;	
}

function _wbKMFolderGetSelectionIdLst(frm,obj){
	var str, ele, i, n
	str = ''
	n = obj.options.length
	for (i=0; i < n; i++){
		if (obj.options[i].value != ""){
			str += obj.options[i].value + '~'
		}		
	}
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}