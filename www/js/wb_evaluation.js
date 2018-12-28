// ------------------ WizBank Evaluation Object ---------------------
// Convention:
//   public functions : use "wbEvaluation" prefix 
//   private functions: use "_wbEvaluation" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js	 
// ------------------------------------------------------------ 
/* constructor */
function wbEvaluation(){	
	this.evn_lst = wbEvaluationLst;
	this.evn_opn = wbEvaluationOpen
	this.maintain_evn = wbEvaluationMaintainEval;
	this.ins_evn_prep = wbEvaluationInsertEvalPrep;
	this.ins_evn_exec = wbEvaluationInsertEvalExec;
	this.upd_evn_prep = wbEvaluationUpdateEvalPrep;
	this.upd_evn_exec = wbEvaluationUpdateEvalExec;
	this.del_evn = wbEvaluationDeleteEval;
	this.del_evn_sng = wbEvaluationDeleteEvalSingle;
	this.export_result = wbEvaluationExportResult;
	this.upd_tst_prep = wbEvaluationUpdTestPrep;	
	this.search_result_prep = wbEvaluationSearchResultPrep;
	this.search_result_exec = wbEvaluationSearchResultExec;
	this.show_mgt_content = wbEvaluationShowMgtContent;
	this.show_content = wbEvaluationShowContent;
	this.search_result_survey = wbEvaluationSearchResultSurvey;
}
// ------------------------------------------------------------ 
function wbEvaluationLst(){
	url = wbEvaluationLstUrl()
	parent.location.href = url;
}

function wbEvaluationLstUrl(){
	url = wb_utils_invoke_disp_servlet(
		'module','content.EvaluationModule',
		'cmd','get_public_eval_lst',
		'filter','true',
		'stylesheet','evn_lst.xsl'
	);
	return url;
}

function wbEvaluationMaintainEval(){
	url = wbEvaluationMaintainEvalUrl()
	parent.location.href = url;
}

function wbEvaluationMaintainEvalUrl(){
	url = wb_utils_invoke_disp_servlet(
		'module','content.EvaluationModule',
		'cmd','get_public_eval_lst',
		'stylesheet','evn_maintain_lst.xsl',
		'tcr_id','0'
	);
	return url;
}

function wbEvaluationOpen(mod_type,mod_id,tpl_use,cos_id){
	str_feature = 'toolbar='	+ 'no'
	+ ',width=' 				+ '1100'
	+ ',height=' 				+ '420'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'no'
		
	if(document.all) {str_feature += ',top=' + '10' + ',left=' + '10';}
	else {str_feature += ',screenX=' + '10' + ',screenY=' + '10';}
		
	cmd = 'get_tst'	
	wb_utils_set_cookie('isWizpack', 'false')
	
	url = wb_utils_invoke_servlet(
		'cmd','get_mod_status',
		'mod_id',mod_id,
		'mod_type',mod_type,
		'tpl_use', tpl_use,
		'cos_id',cos_id,
		'url_failure','../htm/close_window.htm',
		'stylesheet','start_module.xsl'
	)
	evn_test_player = wbUtilsOpenWin(url, 'evn_test_player', false, str_feature);
	evn_test_player.focus();	
}

function wbEvaluationInsertEvalPrep(tcr_id){
	if(tcr_id == null) tcr_id = 0;
	url = wb_utils_invoke_servlet(
		'cmd','get_tpl', 'tpl_type',
		'FOR','tpl_subtype', 'EVN','dpo_view',
		'IST_EDIT','stylesheet', 'evn_ins.xsl',
		'tcr_id', tcr_id
	)
	parent.location.href = url;
}

function wbEvaluationInsertEvalExec(frm,lang){
	frm.end_hour.value='23';
	frm.end_min.value='59';
	if(_validateForm(frm,lang)){
				
		frm.method = "post";
		frm.action = wb_utils_servlet_url;
		frm.mod_type.value = 'EVN';
		frm.url_success.value = wbEvaluationMaintainEvalUrl()
		frm.url_failure.value = parent.location.href;
		
		if(frm.mod_eff_start_datetime) {
			frm.mod_eff_start_datetime.value += frm.start_yy.value + "-" + frm.start_mm.value + "-" + frm.start_dd.value + " 00:00:00.00";
		}
		if (frm.mod_eff_end_datetime) {
			frm.mod_eff_end_datetime.value += frm.end_yy.value + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " 23:59:59.00";
		}
		if(frm.usr_ent_id_lst){
			frm.usr_ent_id_lst.value = _wbGetSelectionIdLst(frm,frm.usr_id_lst);
		}
		//tcr_id
		if (frm.mtc_tcr_id) {
			frm.mtc_tcr_id.value = frm.tcr_id_lst.options[0].value;
		}
		frm.submit();
	}
}

function wbEvaluationUpdateEvalPrep(mod_id){
	url = wb_utils_invoke_servlet(
		'cmd','get_mod',
		'course_id','',
		'mod_id',mod_id,
		'stylesheet','evn_upd.xsl',
		'dpo_view','IST_EDIT'
	)
	parent.location.href = url;
}

function wbEvaluationUpdateEvalExec(frm,lang){
	
	if(_validateForm(frm,lang)){
		if(frm.mod_eff_start_datetime) {
			frm.mod_eff_start_datetime.value += frm.start_yy.value + "-" + frm.start_mm.value + "-" + frm.start_dd.value + " 00:00:00.00";
		}
		if (frm.mod_eff_end_datetime) {
			frm.mod_eff_end_datetime.value += frm.end_yy.value + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " 23:59:59.00";
		}

		for(i=0;i<frm.mod_status_ind.length;i++){
			if(frm.mod_status_ind[i].checked){frm.mod_status.value = frm.mod_status_ind[i].value;}
		}
		if(frm.usr_ent_id_lst){
					frm.usr_ent_id_lst.value = _wbGetSelectionIdLst(frm,frm.usr_id_lst);
		}		
		//tcr_id
		if (frm.mtc_tcr_id) {
			    frm.mtc_tcr_id.value = frm.tcr_id_lst.options[0].value;
		}
		frm.url_success.value = wbEvaluationMaintainEvalUrl()
		frm.url_failure.value = parent.location.href;
		frm.method = "post"			
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

function wbEvaluationDeleteEval(frm,lang){
	mod_id = "";
	timestamp = "";
	if(frm.mod_id.length>1){
		for(i=0;i<frm.mod_id.length;i++){
			if(frm.mod_id[i].checked){
				mod_id += frm.mod_id[i].value + "~";
				timestamp += frm.mod_timestamp[i].value + "~";
			}
		}
	}else if(frm.mod_id.checked){
		mod_id = frm.mod_id.value;
		timestamp = frm.mod_timestamp.value;
	}
	
	if(mod_id==""){
		alert(eval('wb_msg_' + lang + '_sel_del_itm'));
		return;
	}
	
	if(!confirm(eval('wb_msg_' + lang + '_confirm'))) {return;}
	if(mod_id.indexOf("~")>-1)
		mod_id = mod_id.substr(0,mod_id.length-1);
	timestamp = timestamp.substr(0,timestamp.length-1);
	url = wb_utils_invoke_servlet(
		'cmd','del_mod_lst',
		'res_id_lst',mod_id,
		'res_timestamp_lst',timestamp,
		'url_success', wbEvaluationMaintainEvalUrl(),
		'url_failure', wbEvaluationMaintainEvalUrl()
	)
	parent.location.href = url;
}

function wbEvaluationDeleteEvalSingle(mod_id,timestamp,lang){
	if(!confirm(wb_public_eval_delete_confirm)) {return;}
	url = wb_utils_invoke_servlet(
		'cmd','del_mod_lst',
		'res_id_lst',mod_id,
		'res_timestamp_lst',timestamp,
		'url_success', wbEvaluationMaintainEvalUrl(),
		'url_failure', wbEvaluationMaintainEvalUrl()
	)
	parent.location.href = url;
}

function wbEvaluationExportResult(mod_id){
	url = wb_utils_invoke_disp_servlet(
		'module','content.EvaluationModule',
		'cmd','dl_eval_rpt',
		'mod_id',mod_id,
		'stylesheet','evn_rpt_csv.xsl'
	)
	parent.location.href = url;
}

function wbEvaluationUpdTestPrep(id,course_id,usr_id, status, attempted, mod_type,lang){
		
		wb_utils_set_cookie('mod_type',mod_type)
		
		if ( status == 'ON' ){
			if (confirm(eval('wb_msg_' + lang + '_confirm_change_offline'))) {
					url_success = parent.location.href
					timestamp = wb_utils_get_cookie('mod_timestamp')
					this.upd_prep(id,course_id,usr_id,status,attempted,mod_type,lang)
					return;
			}else{
				return;
			}			
		}

		url = wb_utils_invoke_servlet('cmd','get_prof','course_id',course_id,'mod_id',id,'attempted',attempted,'stylesheet','tst_info_frame.xsl','usr_id',usr_id)
	
		str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
		wbUtilsOpenWin(url, 'tst_player', false, str_feature);	

}

function wbEvaluationSearchResultPrep(mod_id, title){
	url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_tpl","mod_id",mod_id,"rpt_type","SURVEY_COS_GRP","stylesheet","evn_view_submission_srh.xsl");
	var str_feature = 'width=' 				+ '780'
		+ ',height=' 				+ '500'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',toolbar='				+ 'yes'
		+ ',screenX='				+ '10'
		+ ',screenY='				+ '10'
		+ ',status='				+ 'yes';
	wbUtilsOpenWin(url,'',false,str_feature);
}

function wbEvaluationSearchResultExec(mod_id,rsp_id){
	var cmd = 'get_rpt'
	var spec_name = 'mod_id' + ':_:_:';
	var spec_value = mod_id + ':_:_:' ;

	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd",cmd,"rsp_id",rsp_id,"rpt_type","SURVEY_IND","stylesheet","rpt_svy_ind_res.xsl", 
						"spec_name", spec_name, "spec_value", spec_value);
	window.parent.location.href = url
}


function wbEvaluationChangeStatus(mod_id,status){

		res_id_lst = mod_id	
		url = wb_utils_invoke_servlet(
			'cmd','upd_res_sts',
			'res_status',status,
			'res_id_lst',res_id_lst,
			'url_success',parent.location.href
		)			
		window.parent.location.href = url

}

// validate function ----------------------------------------------- 
function _validateForm(frm,lang){
	if(frm.mod_title){
		frm.mod_title.value = wbUtilsTrimString(frm.mod_title.value);
		if (!gen_validate_empty_field(frm.mod_title, eval('wb_msg_'+ lang + '_title'),lang)){
			return false;
		}
		if(getChars(frm.mod_title.value) > 80) {
			alert(eval('wb_msg_usr_title_too_long') );
			return false;
		}
	}
	
	//description
	if(frm.mod_desc){
		frm.mod_desc.value = wbUtilsTrimString(frm.mod_desc.value);
		if (!gen_validate_empty_field(frm.mod_desc, frm.lab_mod_desc.value ,lang)){
			return false;
		}
		if(getChars(frm.mod_desc.value) > 400) {
			alert(wb_msg_usr_desc_too_long);
			return false;
		}
	}
	
	//tcr_id
	if (frm.mtc_tcr_id) {
    if (frm.tcr_id_lst.options[0].value == '' || frm.tcr_id_lst.options[0].text == '') {
	    alert(wb_msg_pls_input_tcr);
      return false;
    } 
	}
	//target user
	if (frm.usr_id_lst) {
    if (frm.usr_id_lst.options[0] == null || frm.usr_id_lst.options[0].value == '' || frmXml.tcr_id_lst.options[0].text == '') {
    	var label_publish_object;
    	if(frm.lab_user && frm.lab_user.value != '') {
    			label_publish_object = frm.lab_user.value;
    	} else {
    			label_publish_object = 'Published Object';
    	}
	    alert(eval("ae_msg_" + lang + "_select") + label_publish_object);
      return false;
    } 
	}
	
	//start_date	
		if(!wbUtilsValidateDate("document." + frm.name + ".start",frm.lab_publish_date.value)){
			return false;
		}			

	//end_date
		if(!wbUtilsValidateDate("document." + frm.name + ".end",frm.lab_publish_date.value)){
			return false;
		}			
	
	//comparison
	if(!wb_utils_validate_date_compare({
		frm : 'document.' + frm.name, 
		start_obj : 'start', 
		end_obj : 'end'
		})) {
		return false;
	}
	return true;
}

function _wbGetSelectionIdLst(frm,obj){
	var str, i, n
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

function wbEvaluationShowMgtContent(tcr_id) {
  url = wb_utils_invoke_disp_servlet("module","content.EvaluationModule","cmd", 'get_public_eval_lst', 'stylesheet', 'evn_maintain_lst.xsl', 'tcr_id', tcr_id);
	window.location.href = url;
}

function wbEvaluationShowContent(tcr_id) {
  url = wb_utils_invoke_disp_servlet("module","content.EvaluationModule","cmd", 'get_public_eval_lst', 'stylesheet', 'evn_lst.xsl', 'tcr_id', tcr_id);
	window.location.href = url;
}

function wbEvaluationSearchResultSurvey(lang){
	var title_code = wbUtilsTrimString($('#title_code').val());
	/*if(title_code.length < 1){
		alert(eval('wb_msg_' + lang + '_search_field'));
		$('#title_code').focus();
		return;
	}*/
	var cmd = 'get_public_eval_lst'

	var url = wb_utils_invoke_disp_servlet("env","wizb","module","content.EvaluationModule","cmd",cmd,"tcr_id","0","stylesheet","evn_maintain_lst.xsl","title_code",title_code);
	window.location.href = url
}

