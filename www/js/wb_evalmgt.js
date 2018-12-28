function wbEvalManagement (){
	this.get_scoring_item_marking_lst = wbGetScrItmMrkingLst;
	this.edit_marking_item = wbEditMarkingItm;
	this.upd_mark = wbUpdMark;
	this.reset_mark = wbResetMark;
	this.get_marking_lst_by_cmt_id = wbGetMarkingLstByCmtId;
	this.eval_export_mark = wbEvalExportMark;
	this.eval_import_mark_prep = wbImportMarkPrep;
	this.eval_import_mark_prep_export_template = wbImportMarkPrepExportTemplate;
	this.eval_import_preview = wbEvalProcessMarkImportPreview;
	this.exec_import_mark = wbEvalImportMark;
}

function wbGetScrItmMrkingLst(frm){
	var url = wb_utils_invoke_disp_servlet('module','course.EvalManagementModule','cmd','get_scoring_itm_marking_lst','itm_id','', 'cmt_id', frm.cmt_id.value, 'stylesheet','marking_item_lst.xsl');
	window.location.href = url;
}
function wbGetMarkingLstByCmtId(itm_id,cmt_id){
	var url = wb_utils_invoke_disp_servlet('module','course.EvalManagementModule','cmd','get_scoring_itm_marking_lst','itm_id',itm_id, 'cmt_id', cmt_id, 'stylesheet','marking_item_lst.xsl');
	window.location.href = url;
}
function wbEditMarkingItm(frm,app_id,cmt_id,lrn_ent_id,cmt_tkh_id,lrn_name,cmt_score,cmt_max_score){
	frm.action = wb_utils_disp_servlet_url;
	frm.module.value = 'course.EvalManagementModule';
	frm.cmd.value = 'edit';
	frm.stylesheet.value= 'eval_edit_mark.xsl';
	frm.app_id.value = app_id;
	frm.cmt_id.value = cmt_id;
	frm.lrn_ent_id.value = lrn_ent_id;
	frm.lrn_name.value = lrn_name;
	frm.cmt_tkh_id.value = cmt_tkh_id;
	if(cmt_score){
		frm.cmt_score.value = cmt_score;
	}
//	frm.url-success.value = wb_utils_invoke_disp_servlet('module',frm.module.value,'cmd','get_scoring_itm_marking_lst', 'cmt_id', cmt_id, 'stylesheet','marking_item_lst.xsl');
//	frm.url-failure.value = wb_utils_invoke_disp_servlet('module',frm.module.value,'cmd','get_scoring_itm_marking_lst', 'cmt_id', cmt_id, 'stylesheet','marking_item_lst.xsl');
	frm.method = 'get';
	frm.submit();
//	var url = wb_utils_invoke_disp_servlet('module','course.EvalManagementModule','cmd','edit', 'cmt_id', cmt_id, 'stylesheet','eval_edit_mark.xsl');
}
function wbResetMark(frm,cmt_id,lrn_ent_id,cmt_tkh_id){
	if(confirm(wb_msg_confirm)){
		frm.action = wb_utils_disp_servlet_url;
		frm.module.value = 'course.EvalManagementModule';
		frm.cmd.value = 'reset';
		frm.cmt_id.value = cmt_id;
		frm.lrn_ent_id.value = lrn_ent_id;
		frm.cmt_tkh_id.value = cmt_tkh_id;
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','course.EvalManagementModule','cmd','get_scoring_itm_marking_lst', 'cmt_id', cmt_id, 'stylesheet','marking_item_lst.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','course.EvalManagementModule','cmd','get_scoring_itm_marking_lst', 'cmt_id', cmt_id, 'stylesheet','marking_item_lst.xsl');
		frm.method = 'get';
		frm.submit();
	}else{
		return;
	}
}

function feedParam(frm){
	frm.lrn_ent_id.value = getUrlParam('lrn_ent_id');
	if(frm.lrn_name){
		frm.lrn_name.value = getUrlParam('lrn_name');
	}
	if(frm.cmt_id){
		frm.cmt_id.value = getUrlParam('cmt_id');	
	}
	if(frm.cmt_tkh_id){
	frm.cmt_tkh_id.value = getUrlParam('cmt_tkh_id');
	}
	if(frm.app_id){
		frm.app_id.value = getUrlParam('app_id');
	}
	if(frm.cmt_score){
	    var int_score = getUrlParam('cmt_score');
	    if (int_score == '') {
	        int_score = '';
	    } else {
	        int_score = parseInt(int_score);
	    }
		frm.cmt_score.value = int_score;
	}
	if(frm.cmt_max_score){
		frm.cmt_max_score.value = getUrlParam('cmt_max_score');
	}
}

function wbUpdMark(frm,lang){
	frm.cmt_score.value = wbUtilsTrimString(frm.cmt_score.value);
	if(frm.cmt_score.value == '' || frm.cmt_score.value == 'NaN'){
		alert(frm.cmt_score.title + label_tm.label_core_training_management_291);
		return;
	}
	var val = wbUtilsTrimString(frm.cmt_score.value);
	if ( val.search(/[^0-9]/) != -1 || Number(val) <= 0 ){
		Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + frm.cmt_score.title + '"' + wb_msg_pls_enter_positive_integer_2);
		frm.cmt_score.focus();
		return false;
	}
	frm.cmt_max_score.value = wbUtilsTrimString(frm.cmt_max_score.value);
	if(parseInt(frm.cmt_score.value) > parseInt(frm.cmt_max_score.value)){
		alert(wb_msg_score_lt_max_score + '('+ parseInt(frm.cmt_max_score.value) +')');
		return;
	}
	frm.action = wb_utils_disp_servlet_url;
	frm.module.value = 'course.EvalManagementModule';
	frm.cmd.value = 'upd_score';
	frm.method = 'get';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','course.EvalManagementModule','cmd','get_scoring_itm_marking_lst', 'cmt_id', frm.cmt_id.value, 'stylesheet','marking_item_lst.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','course.EvalManagementModule','cmd','get_scoring_itm_marking_lst', 'cmt_id', frm.cmt_id.value, 'stylesheet','marking_item_lst.xsl');
	frm.submit();
}
function wbEvalExportMark(frm){
	frm.action = wb_utils_disp_servlet_url;
	frm.module.value = 'course.EvalManagementModule';
	frm.cmd.value = 'export_mark';
	frm.stylesheet.value = 'eval_export_mark.xsl';
	frm.method = 'post';
	frm.submit();
}
function wbImportMarkPrep(frm){
	var Now = new Date();
	var import_win = Now.getTime();
	//open a blank window with name = current system time
	var rpt_win = wbUtilsOpenWin ('', import_win, false, 'height=500, width=800, top=0,left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=yes')
	//var rpt_win = window.open("../htm/loading.htm?lang=en", "winName");
	frm.action = wb_utils_disp_servlet_url;
	frm.module.value = 'course.CourseCriteriaModule';
	frm.cmd.value = 'get_cmt_lst';
	frm.stylesheet.value = 'eval_import_mark_prep.xsl';
	frm.method = 'get';
	frm.target =  import_win;//form target is the name of a window
	frm.submit();
}

function wbImportMarkPrepExportTemplate(frm){
	frm.action = wb_utils_disp_servlet_url;
	frm.module.value = 'course.EvalManagementModule';
	frm.cmd.value = 'export_mark';
	frm.stylesheet.value = 'eval_import_mark_prep_export_template.xsl';
	frm.method = 'post';
	frm.submit();
}
function wbEvalProcessMarkImportPreview(frm,lang){
//	var Now = new Date();
//	var import_win = Now.getTime();
	//open a blank window with name = current system time
//	var rpt_win = window.open ('', import_win, 'height=500, width=800, top=0,left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no')
	_txtFileName = wb_utils_get_filename_from_path(frm.src_filename_path.value)
	if(_txtFileName == '' || _txtFileName == undefined){
		Dialog.alert(eval('wb_msg_select_uploaded_file'))
	}else if (_txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'txt')	{
		Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_usr'))
	} else if (_txtFileName.length > 100)	{
		Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else{
		frm.action = wb_utils_disp_servlet_url+"?isExcludes=true"
		frm.module.value = 'course.EvalManagementModule'
		frm.method = 'post'
		frm.cmd.value = 'import_mark_preview'
		frm.stylesheet.value = 'eval_import_mark_preview.xsl'
		frm.src_filename.value = _txtFileName		
		frm.url_failure.value = self.location;
//		frm.target = import_win;
		frm.submit()
	}
}
function wbEvalImportMark(frm,isExcludes){
	frm.action = wb_utils_disp_servlet_url;
	if(isExcludes)
	{
		frm.action+="?isExcludes=true";
	}
	frm.module.value = 'course.EvalManagementModule';
	frm.cmd.value = 'exec_import_mark';
	frm.url_failure.value = parent.location.href;
	frm.url_success.value = "../htm/close_window.htm";
	frm.method = 'post';
	frm.submit();
}