// ------------------ wizBank Data Import and Export object ------------------- 
// Convention:
//   public functions : use "wbBatchProcess" prefix 
//   private functions: use "_wbBatchProcess" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

function wbContent(){
	this.ImpTemplate =  wbContentImportTemplate
	this.ImpInstr = wbContentImportInstr
	this.ImpExec = wbContentImportExec
	this.ImpConfirm = wbContentResImportConfirm
	this.prep_page = wbContentResPrepPage
}
//------------------------------------------------------------------ 




function wbContentImportTemplate(frm, lang) {
	if (!wbUtilsValidateEmptyField(frm.que_type[1], frm.lab_que_type.value)) {
		return;
    }
	var que_type = frm.que_type[1].value;
	if( que_type == 'MC' ) {
		url = '../htm/import_contentque/wb_import_contentque_mc_template-' + lang + '.xls';
	}  else if( que_type == 'ES' ) {
		url = '../htm/import_contentque/wb_import_contentque_es_template-' + lang + '.xls';
	}
	wbUtilsOpenWin(url, '');
}

function wbContentImportInstr(frm, mod_type) {
	if (!wbUtilsValidateEmptyField(frm.que_type[1], frm.lab_que_type.value)) {
		return;
    }
    instrType = frm.que_type[1].value;
    if(instrType =='ES'){
    	instrType ='CES';
    }else{
    	instrType ='CMC';
    }
	url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_instr', 'instr_type', instrType, 'stylesheet', 'bp_import_instr.xsl','mod_type',mod_type);
	wbUtilsOpenWin(url, '');
}

function wbContentImportExec(frm,lang, fldName){
    obj = frm.que_type[1];
    que_type = obj.value;
    if (!wbUtilsValidateEmptyField(obj, fldName)) {
        return false;
    }
    _txtFileName = _wbContentUserImportGetFileName(frm.src_filename_path.value)
    if ((que_type != 'FSC' && que_type != 'DSC')
        && _txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'txt') {
    		Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_usr'))
    } else if ((que_type == 'FSC' || que_type == 'DSC')
        && _txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls') {
    	Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
	} else if (_txtFileName.length > 100)	{
		Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else if (frm.ulg_desc.value.length > 1000)	{
		Dialog.alert(eval('wb_msg_'+lang+'_desc_too_long'))
		frm.ulg_desc.focus()
	}else{
		frm.src_filename.value = _txtFileName		
		frm.cmd.value = 'upload_que'
		frm.module.value = 'content.ConQueImpModule'
		if( que_type == 'MC' ) {
			frm.stylesheet.value = 'bp_mc_contentimport_confirm.xsl';
		} else if( que_type == 'ES' ) {
			frm.stylesheet.value = 'bp_es_contentimport_confirm.xsl';
		} 
		frm.url_failure.value = 'javascript:history.back()'
		frm.que_type.value = que_type
		frm.method = 'post'
		frm.action = wb_utils_disp_servlet_url + '?cmd=upload_que';	
		frm.submit()		
	}
}
function _wbContentUserImportGetFileName(pathname) {
	s = pathname.lastIndexOf("\\");
	if (s == -1) {s = pathname.lastIndexOf("/");}
	if (s == -1) {return pathname;}	
	l = pathname.length - s;
	return pathname.substr(s+1,l);
}
function wbContentUserImportConfirm(frm,lang) {
	_wbContentUserImportConfirm(frm,lang,'bp_usr_import_success.xsl');
}



function _wbContentUserImportConfirm(frm,lang,stylesheet){
	/*
	if (confirm('xml?')) {
		frm.cmd.value = 'cook_user_xml'
	}
	else {
	*/
	frm.cmd.value = 'cook_user'
	//}
	frm.module.value = 'content.ConQueImpModule'
	frm.url_success.value = wbContentUserImportPrepURL()
	frm.url_failure.value = wbContentUserImportPrepURL()
	frm.stylesheet.value = stylesheet
	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.submit()
}

function wbContentUserImportPrepURL(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','bp_usr_import_prep.xsl')		
	return url
} 

function wbContentResImportConfirm(frm, que_type){
	frm.cmd.value = 'cook_que'
	frm.module.value = 'content.ConQueImpModule'
	url = window.parent.location.href
	frm.url_success.value = 'javascript:window.parent.location.href=\'' + url + '\';document.write(\'\');';
	frm.url_failure.value = 'javascript:window.parent.location.href=\'' + url + '\';document.write(\'\');';
	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true"
	frm.stylesheet.value = 'bp_contentimport_success.xsl';
	frm.submit()
}
function wbContentResPrepPage(){
	var url = window.parent.location.href;
	window.parent.location.href=url;
	
}
