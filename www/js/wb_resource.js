// ------------------ wizBank Resource object -------------------
// Convention:
//   public functions : use "wbResource" prefix
//   private functions: use "_wbResource" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------


/* constructor */
function wbResource() {
	this.get = wbResourceGet
	this.get_in_search = wbResourceGetInSearch
	this.ins_prep = wbResourceInsPrep						// insert resource only
	this.ins_exec = wbResourceInsExec						// insert resource only
	this.ins_res_prep = wbResourceInsResourcePrep 			// insert resource (include question)
	this.ins_res_prep_url = wbResourceInsResourcePrepURL 	// insert resource (include question) instruction
	this.ins_netg_prep = wbResourceInsNetgPrep
	this.ins_presentation_prep = wbResourceInsPresentationPrep
	this.del_res = wbResourceDelResource					// del list of resource (include question)
	this.del = wbResourceDel							// del one resource only
	this.del_in_search = wbResourceDelInSearch

	this.copy_lst = wbResourceCopyLst
	this.pasteLst = wbResourcePasteLst

	this.change_status = wbResourceChangeStatus
	this.download = wbResourcesDownload
	this.pick_res = wbResourcePick							// for insert gen module
	this.pick_res_frame_url = wbResourcePickResourceFrameURL
	this.upd_prep = wbResourceUpdatePrep
	this.upd_exec = wbResourceUpdateExec
	this.read_in_select_res	= wbResourceReadInSelectQuesiton
	this.edit_in_search = wbResourceEditInSearch
	this.trash_res = wbResourceTrashResource	//Physical Delete
	this.trash = wbResourceTrash	//Physical Delete
	this.trash_res_prep_url = wbResourceTrashResourcePrepURL
	this.sendFrm = wbResourceSendForm
	this.perm_del = wbResourcePermanentDel					// for premenent del a resource , admin only
	this.cut = wbResourceCut								// for cut a resource from recycle bin
	this.read = wbResourceReadInSearch
}



function wbResourceEditInSearch (res_id, res_subtype) {
	var stylesheet = '';
	switch(res_subtype) {
		case 'RES_SCO' : stylesheet='upd_res_sco.xsl';break;
		case 'RES_NETG_COK' : stylesheet='res_srh_netg_upd.xsl';break;
		default : stylesheet = 'res_srh_res_upd.xsl';break;
	}
	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet',stylesheet)
	window.location.href = url
}



function _wbResourceValidateForm(frm,lang) {
	frm.res_duration.value = wbUtilsTrimString(frm.res_duration.value);
	if (!gen_validate_float(frm.res_duration, eval('wb_msg_'+lang+'_duration'),lang)) {
		return false
	}
	if (frm.res_title) {
		frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
		if ( !gen_validate_empty_field(frm.res_title, eval('wb_msg_'+lang+'_title'),lang) ) {
			return false
		}
	}

	if (frm.res_desc) {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
		if(frm.res_desc.value.length > 1000 ) {
			alert(eval('wb_msg_'+lang+'_desc_too_long'))
			frm.res_desc.focus()
			return false
		}
	}
	// valid length of annotation
	if (frm.res_annotation) {
		frm.res_annotation.value = wbUtilsTrimString(frm.res_annotation.value);
		if (frm.res_annotation.value.length > 1000){
			alert(eval('wb_msg_'+lang+'_annonation_too_long'))
			frm.res_annotation.focus()
			return false
		}
	}

	//Validate URL
	if ( frm.res_format[0].checked ){
		_url = frm.res_url.value.toLowerCase()
		if( !gen_validate_empty_field(frm.res_url,eval('wb_msg_'+lang+'_url'),lang ) )
			return false
		if( _url == 'http://'){
			alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + eval('wb_msg_' + lang + '_url') + '\"')
			frm.res_url.focus()
			return false;
		}
		if ( _url.substring(0,7) != "http://"){
			alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + eval('wb_msg_' + lang + '_url') + '\"')
			frm.res_url.focus()
			return false;
		}
	}

	//Validate File
	if ( frm.res_format[1].checked )
		if( !gen_validate_empty_field(frm.upload_file, eval('wb_msg_'+lang+'_filename'),lang) )
			return false

	//Validate Zip File
	if( frm.res_format[2].checked ) {
		if( !gen_validate_empty_field(frm.upload_zipfile, eval('wb_msg_'+lang+'_filename'),lang) )
			return false
		if ( frm.cmd.value == 'upd_res' )
			frm.res_zipfile.value = frm.res_zipfile1.value;
		if( !gen_validate_empty_field(frm.res_zipfile, eval('wb_msg_'+lang+'_filename'),lang))
			return false
		if(!wb_utils_check_chinese_char(frm.res_zipfile.value))
			return false

			wizpack = wbMediaGetFileName(frm.upload_zipfile.value);
		if ( wizpack.substring(wizpack.lastIndexOf('.')+1).toLowerCase() != 'zip' ){
			alert(eval('wb_msg_' + lang + '_upload_valid_zipfile'))
			return false;
		}
	}
	
	
	//Validte Keep current zip file
	if(frm.res_format[3] && frm.res_format[3].checked && frm.res_zipfile2){
		if( !gen_validate_empty_field(frm.res_zipfile2, eval('wb_msg_'+lang+'_filename'),lang) )
			return false;
	}

	//Validate Upload Source identical with User selection
	/*Web Content Source*/
	/*if (frm.res_subtype.value == 'WCT' && frm.upload_file.value != "" && frm.res_format[1].checked){
		//not yet validate WCT

	}*/
	/*Document Source*/
	/*if (frm.res_subtype.value == 'NAR' && frm.upload_file.value != "" && frm.res_format[1].checked){
		//not yet validate NAR

	}*/
	/*Image Source*/
	/*if (frm.res_subtype.value == 'FIG' && frm.upload_file.value != "" && frm.res_format[1].checked){
		_file_type = wbGetImgType(frm.upload_file.value)
		if (_file_type == 'unknown'){
			alert(eval('wb_msg_' + lang + '_img_not_support'))
			frm.upload_file.focus();
			return false;
		}
	}*/
	/*VDO Source*/
	/*if (frm.res_subtype.value == 'VDO' && frm.upload_file.value != "" && frm.res_format[1].checked){
		_file_type = wbGetVdoType(frm.upload_file.value)
		if (_file_type == 'unknown'){
			alert(eval('wb_msg_' + lang + '_vdo_not_support'))
			frm.upload_file.focus();
			return false;
		}
	}*/
	/*ADO Source*/
	/*if (frm.res_subtype.value == 'ADO' && frm.upload_file.value != "" && frm.res_format[1].checked){
		_file_type = wbGetAdoType(frm.upload_file.value)
		if (_file_type == 'unknown'){
			alert(eval('wb_msg_' + lang + '_ado_not_support'))
			frm.upload_file.focus();
			return false;
		}
	}*/

	/*
	if(frm.res_privilege && frm.res_privilege.options[frm.res_privilege.selectedIndex].value == ''){
		alert(eval('wb_msg_' + lang + '_select_permission'))
		frm.res_privilege.focus();
		return false;
	}
	*/

	return true;
}


function wbGetType(filename) {
	s = filename.lastIndexOf(".");
	if (s == -1)
		return MEDIA_TYPE_UNKNOWN;

	l = filename.length;
	suffx = filename.substr(s+1,l);

	if (suffx.toLowerCase() == "doc")
		return 'doc';
	else if (suffx.toLowerCase() == "ppt")
		return 'ppt';
	else if (suffx.toLowerCase() == "txt")
		return 'txt';
	else if (suffx.toLowerCase() == "htm")
		return 'htm';
	else
		return 'unknown';
}

function wbGetImgType(filename) {
	s = filename.lastIndexOf(".");
	if (s == -1)
		return MEDIA_TYPE_UNKNOWN;

	l = filename.length;
	suffx = filename.substr(s+1,l);

	if (suffx.toLowerCase() == "gif")
		return 'gif';
	else if (suffx.toLowerCase() == "jpg")
		return 'jpg';
	else if (suffx.toLowerCase() == "jpeg")
		return 'jpeg';
	else if (suffx.toLowerCase() == "bmp")
		return 'bmp';
	else if (suffx.toLowerCase() == "swf")
		return 'swf';
	else
		return 'unknown';
}

function wbGetVdoType(filename) {
	s = filename.lastIndexOf(".");
	if (s == -1)
		return MEDIA_TYPE_UNKNOWN;

	l = filename.length;
	suffx = filename.substr(s+1,l);

	if (suffx.toLowerCase() == "ra")
		return 'ra';
	else if (suffx.toLowerCase() == "rm")
		return 'rm';
	else if (suffx.toLowerCase() == "asf")
		return 'asf';
	else if (suffx.toLowerCase() == "mov")
		return 'mov';
	else if (suffx.toLowerCase() == "swf")
		return 'swf';
	else
		return 'unknown';
}

function wbGetAdoType(filename) {
	s = filename.lastIndexOf(".");
	if (s == -1)
		return MEDIA_TYPE_UNKNOWN;

	l = filename.length;
	suffx = filename.substr(s+1,l);

	if (suffx.toLowerCase() == "ra")
		return 'ra';
	else if (suffx.toLowerCase() == "rm")
		return 'rm';
	else if (suffx.toLowerCase() == "asf")
		return 'asf';
	else if (suffx.toLowerCase() == "mov")
		return 'mov';
	else if (suffx.toLowerCase() == "wav")
		return 'wav';
	else
		return 'unknown';
}

/* private functions */
function _wbResourceGetList(frm,delimiter, checkASM) {
		if(delimiter == null){
			delimiter = '~'
		}
		var i, n, ele, str
		var fail_count = 0
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.name != 'sel_all_checkbox' && ele.checked) {
				str = str + ele.value + delimiter
				if(frm.elements[i+1].type == 'hidden' && frm.elements[i+1].value == 'ASM'){
					fail_count++
				} 
			}
		}

		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		if (checkASM == 'true' && fail_count != 0){
			str = 'que_type_not_support';
		}
		return str;
}

function _wbResourceGenFrm(frm,lang) {

	if (frm.asHTML.checked)
		frm.annotation_html.value = "Y";
	else
		frm.annotation_html.value = "N";

	if ( frm.res_format[0].checked == true ) {
		 frm.res_src_type.value = 'URL';
		 frm.res_src_link.value = frm.res_url.value;
		 frm.upload_file.value = '';
		 frm.upload_zipfile.value = '';
		 frm.zip_filename.value = '';
	}
	if ( frm.res_format[1].checked == true )
	{	 frm.res_src_type.value = 'FILE';
		 frm.res_src_link.value = wbMediaGetFileName(frm.upload_file.value);
		 frm.upload_zipfile.value = '';
		 frm.zip_filename.value = '';
	}
	if ( frm.res_format[2].checked == true )
	{	frm.res_src_type.value = 'ZIPFILE';
		frm.res_src_link.value = frm.res_zipfile.value;
		frm.upload_file.value = '';
		frm.zip_filename.value = wbMediaGetFileName(frm.upload_zipfile.value)
	}
	if(frm.res_format[3]){
		if ( frm.cmd.value == 'upd_res' && frm.res_src_type.value == 'ZIPFILE' && frm.res_format[3].checked == true )
		{
	
			frm.res_src_type.value = 'ZIPFILE';
			frm.res_src_link.value = frm.res_zipfile2.value;
			frm.upload_file.value = '';
			frm.zip_filename.value = wbMediaGetFileName(frm.upload_zipfile.value)
		}
	}
}

/* public functions */
function wbResourceGet(res_id){

	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','res_get.xsl','url_failure',self.location.href)
	window.location.href = url;
}

function wbResourceGetInSearch(res_id){

	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','res_srh_res_ind.xsl','url_failure',self.location.href)
	window.location.href = url;

}

function wbResourceInsPrep(sub_type) {
	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'sub_type', sub_type, 'stylesheet', 'res_ins.xsl')
	window.location.href = url
}

function wbResourceInsNetgPrep(sub_type) {
	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'sub_type', sub_type, 'stylesheet', 'ins_res_netg.xsl')
	window.location.href = url
}

function wbResourceInsPresentationPrep(sub_type) {
	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'sub_type', sub_type, 'stylesheet', 'ins_res_presentation.xsl')
	window.location.href = url
}

function wbResourceInsExec(frm,lang){

	if (_wbResourceValidateForm(frm,lang)) {

		frm.url_success.value = 'javascript:parent.location.reload()'
		frm.url_failure.value = 'javascript:parent.location.reload()'

			frm.action = wb_utils_servlet_url
			frm.submit()
		}


}

function wbResourceInsResourcePrep(){

	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ins_res_prep.xsl')
	window.location.href = url;

}


function wbResourceInsResourcePrepURL(objId){
    //alert(objId);
	url = wb_utils_invoke_servlet('cmd','get_prof','obj_id',objId,'stylesheet','res_inst.xsl')
	return url;

}
function wbResourceTrashResourcePrepURL(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','res_trash_prep_inst.xsl')
	return url;
}

function wbResourceDelResource(frm,lang,obj_id){

		res_id_lst = _wbResourceGetList(frm)
		if (res_id_lst == "") {
			alert(wb_msg_select_res)
			return
		}


		if (confirm(eval('wb_msg_' + lang + '_confirm'))){
			frm.method = 'post'
			frm.cmd.value = 'del_obj_res'
			frm.res_id_lst.value = res_id_lst
			frm.obj_id.value = obj_id
			frm.url_success.value = window.parent.location.href
			frm.url_failure.value = window.parent.location.href
			//alert(window.parent.location.href)
			frm.target = '_parent'
			frm.action = wb_utils_servlet_url
			frm.submit()

			//url = wb_utils_invoke_servlet('cmd','del_obj_res','res_id_lst',res_id_lst,'obj_id',obj_id,'url_success','javascript:parent.location.reload()','url_failure','parent.location.href.reload()')
			//document.write(url)
			//window.parent.content.location.href = url
		}
}
function wbResourceDel(res_id,obj_id,lang){

	if (confirm(eval('wb_msg_' + lang + '_confirm'))){
			url = wb_utils_invoke_servlet('cmd','del_obj_res','res_id_lst',res_id,'obj_id',obj_id,'url_success',"javascript:obj.show_obj_lst('','"+obj_id+"','','','false');",'url_failure',"javascript:obj.show_obj_lst('','"+obj_id+"','','','false');")
			window.location.href = url
	}


}

function wbResourceDelInSearch(res_id,obj_id,lang){

	if (confirm(eval('wb_msg_' + lang + '_confirm'))){
		url = wb_utils_invoke_servlet('cmd','del_obj_res','res_id_lst',res_id,'obj_id',obj_id,'url_success',wb_utils_get_cookie('search_result_url'),'url_failure',wb_utils_get_cookie('search_result_url'))
		window.location.href = url
	}

}

function wbResourceCopyLst(frm,lang){
	var _idLst = ""
	//  Man: Reason of Using "." as delimiter
	//	The length of escaped "~"(%7E) is 3 , the length of escaped "." is 1
 	//	ids MUST be integer, we can use "." to replace "~" delimiter ,to create shorter cookie length
	_idLst = _wbResourceGetList(frm,'.', 'true')

	if (_idLst == ''){
		alert(wb_msg_select_res);
	}else if (_idLst == 'que_type_not_support'){
		alert(wb_msg_que_type_not_support);
	}else if(escape(_idLst).length > 2000){
		alert(eval('wb_msg_' + lang + '_sel_too_many_que'))
	}else {
		gen_set_cookie('res_id_copy',_idLst);
		alert(eval('wb_msg_' + lang + '_copy_res'))
	}
}

function wbResourcePasteLst(que_obj_id,lang,frm,res_privilege){

	if (res_privilege == null) {res_privilege = "";}

	var _idLst = gen_get_cookie('res_id_copy')
	_idLst = _idLst.replace(/\./g,"~")
	gen_set_cookie('res_id_copy','')
	if (_idLst == '') {alert(wb_msg_copy_res_first);}
	else {
		frm.res_id_lst.value = _idLst
		frm.to_obj_id.value = que_obj_id
		frm.cmd.value = 'cp_obj_res_lst'
		frm.url_success.value = parent.location.href
		frm.url_failure.value = parent.location.href
		frm.method = 'post'
		frm.target = '_parent'
		frm.action = wb_utils_servlet_url
		//alert(frm.res_id_lst.value)
		//alert(frm.to_obj_id.value)
		//wb_utils_set_cookie('res_id_copy','')
		frm.submit()
		/*url = wb_utils_invoke_servlet(
			'cmd','cp_obj_res_lst',
			'res_id_lst',_idLst,
			'to_obj_id',que_obj_id,
			'res_privilege',res_privilege,
			'url_success','javascript:parent.location.reload()',
			'url_failure','javascript:parent.location.reload()'
		)
		alert(url)

		window.parent.content.location.href = url;
	*/
	}
}


function wbResourceTrashResource(frm,lang){
		res_id_lst = _wbResourceGetList(frm)
		if (res_id_lst == "") {
			alert(wb_msg_select_res)
			return
		}


		if (confirm(eval('wb_msg_' + lang + '_confirm'))){
			frm.method = 'post'
			frm.cmd.value = 'trash_obj_res'
			//alert(res_id_lst)
			frm.res_id_lst.value = res_id_lst
			frm.url_success.value = window.parent.location.href
			frm.url_failure.value = window.parent.location.href
			frm.target = '_parent'
			frm.action = wb_utils_servlet_url
			frm.submit()
			//url = wb_utils_invoke_servlet('cmd','trash_obj_res','res_id_lst',res_id_lst,'url_success','javascript:parent.location.reload()','url_failure','javascript:parent.location.reload()')
			//window.parent.content.location.href = url
		}
}
function wbResourceTrash(res_id,lang){

	if (confirm(eval('wb_msg_' + lang + '_confirm'))){
			url = wb_utils_invoke_servlet('cmd','trash_obj_res','res_id_lst',res_id,'url_success','javascript:parent.location.reload()','url_failure','parent.location.href.reload()')
			window.parent.content.location.href = url
	}


}

function wbResourceCopy(res_id,res_type,lang){

		wb_utils_set_cookie('res_from','obj')
		wb_utils_set_cookie('res_id_copy',res_id)
		wb_utils_set_cookie('res_type_copy',res_type)
		alert(eval('wb_msg_' + lang + '_copy_res'))
}

function wbResourceChangeStatus(frm,status,lang){

		res_id_lst = _wbResourceGetList(frm);
		if (res_id_lst == "") {
			alert(wb_msg_select_res)
			return
		}

		/*url = frm.action
			+ "&cmd=upd_qlst_sts"
			+ "&que_status=" + status
			+ "&que_id_lst=" + escape(que_id_lst)*/
		frm.method = 'post'
		frm.action = wb_utils_servlet_url
		frm.cmd.value = 'upd_res_sts'
		frm.res_status.value = status
		frm.res_id_lst.value = res_id_lst
		frm.url_success.value = top.frames.location.href

		frm.target = '_top'
		frm.submit()
		//url = wb_utils_invoke_servlet('cmd','upd_res_sts','res_status',status,'res_id_lst',res_id_lst,'url_success',parent.location.href)
		//window.parent.location.href = url

}

function wbResourcesDownload(id,frm,lang){
		res_id_lst = _wbResourceGetList(frm);
		if (res_id_lst == "") {
			alert(wb_msg_select_res)
			return
		}
		url_success = '../resource/temp/' + id + '/resources.zip'
		//url = wb_utils_invoke_servlet('cmd','download_res','res_id_lst',res_id_lst,'url_success',url_success,'url_failure',parent.location.href)
		//window.parent.location.href = url

		frm.method = 'post'
		frm.action = wb_utils_servlet_url
		frm.cmd.value = 'download_res'
		frm.res_id_lst.value = res_id_lst
		frm.url_success.value = url_success
		frm.url_failure.value = parent.location.href
		frm.target = '_top'
		frm.submit()
}
function wbResourcePickResourceFrameURL(tpl_type){
	var url = wb_utils_invoke_servlet('cmd','get_tpl','tpl_type',tpl_type,'tpl_subtype',tpl_type,'dpo_view','IST_EDIT','stylesheet','res_pick_obj_hidden_frame.xsl')
	return url;
}

function wbResourcePick(tpl_type, res_type, cos_id){
	if(tpl_type == 'AICC_AU'){
		var res_type = 'AICC'
	} else if (tpl_type == 'SCORM') {
		var res_type = 'SCORM'
	} else if (tpl_type == 'NETG_COK') {
		var res_type = 'NETGCOK'
	} else {
		if(res_type == null || res_type == ''){
			var res_type = 'GEN'	
		}
	}

	var url = wb_utils_invoke_servlet('cmd','get_tpl','tpl_type',tpl_type,'tpl_subtype',tpl_type,'dpo_view','IST_EDIT','stylesheet','res_pick_obj_frame.xsl','res_type',res_type, 'course_id',cos_id)
	var str_feature = 'toolbar='			+ 'no'
			+ ',titlebar='				+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '680'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'NO'
	wbUtilsOpenWin(url, 'pick_resource', false, str_feature);
}


function wbResourceUpdatePrep(res_id){

		// url =../servlet/qdb.qdbAction?env=qdb_dev&cmd=get_q&que_id=4710&charset=ISO-8859-1&stylesheet=que_edit_fb.xsl
		url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','res_upd.xsl')
//		return url;
		window.location.href = url;

}


function wbResourceUpdateExec(frm,lang){


}

function wbResourceSendForm(frm,lang) {
	if (_wbResourceValidateForm(frm,lang)) {
		_wbResourceGenFrm(frm,lang)
		frm.obj_id.value = getParentUrlParam('obj_id')

		frm.method = "post"
		frm.action = wb_utils_servlet_url


		url = window.parent.location.href
		if ( url.indexOf('stylesheet=res_fra.xsl') != -1 )
		{		url = setUrlParam('res_privilege',frm.res_privilege.value, url);
				frm.url_success.value = "javascript:window.parent.location.href='" + url + "';document.write('');"
				wb_utils_set_cookie('prev_res_privilege',frm.res_privilege.value);
				wb_utils_set_cookie('prev_res_difficulty',frm.res_difficulty.value);
				wb_utils_set_cookie('prev_res_lan',frm.res_lan.value);
		}

		if(!(frm.res_format && frm.res_format[0] && frm.res_format[0].checked))
			if (window.parent.content != null){
				wb_utils_preloading();
			}else{
				wb_utils_preloading();
			}
		frm.submit();
	}

}

function wbResourceReadInSelectQuesiton(res_id, res_type){
	// url = cmd=get_q&que_id=4711&charset=ISO-8859-1&stylesheet=tst_info_content_que.xsl
	if (res_type == 'FAS' || res_type == 'DAS'){
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',res_id,'stylesheet','cm_asm_get_ins.xsl')
	}else{
		url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','cm_res_get_ins.xsl')
	}
	window.parent.content.location.href = url
//	return url;

}


function wbResourcePermanentDel(res_id,lang){

	if (confirm(eval('wb_msg_' + lang + '_confirm'))){
			url = wb_utils_invoke_servlet('cmd','trash_obj_res','res_id_lst',res_id,'url_success','javascript:parent.location.reload()','url_failure','javascript:parent.location.reload()')
			window.parent.content.location.href = url
	}


}

function wbResourceCut(res_id,obj_id,lang){

	wb_utils_set_cookie('res_from','recycle')
	wb_utils_set_cookie('res_id_copy',res_id)
	wb_utils_set_cookie('frm_obj_id',obj_id)
	alert(eval('wb_msg_' + lang + '_cut_res'))

}

function wbResourceReadInSearch(res_id, res_subtype){
	var stylesheet = "";
	switch(res_subtype) {
		case 'RES_SCO' : stylesheet = 'res_srh_sco_ind.xsl';break;
		case 'RES_NETG_COK' : stylesheet = 'res_srh_netg_ind.xsl';break;
		default : stylesheet = 'res_srh_res_ind.xsl';break;
	}
	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet', stylesheet)
	window.location.href = url
}