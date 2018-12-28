// ------------------ WizBank Evaluation Object ---------------------
// Convention:
//   public functions : use "wbEvaluation" prefix 
//   private functions: use "_wbEvaluation" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js	 
// ------------------------------------------------------------ 
/* constructor */
function wbCosEvaluation(){	
//	this.evn_opn = wbEvaluationOpen
	this.maintain_cos_evn = wbCosEvaluationMaintainCosEval;
	this.ins_cos_evn_prep = wbCosEvaluationInsertCosEvalPrep;
	this.ins_cos_evn_exec = wbCosEvaluationInsertCosEvalExec;
	this.upd_cos_evn_prep = wbCosEvaluationUpdateCosEvalPrep;
	this.upd_cos_evn_exec = wbCosEvaluationUpdateCosEvalExec;
	this.del_cos_evn = wbCosEvaluationDeleteCosEval;
//	this.upd_tst_prep = wbEvaluationUpdTestPrep;	
}
// ------------------------------------------------------------ 

function wbCosEvaluationMaintainCosEval(){
	url = wbCosEvaluationMaintainCosEvalUrl()
	parent.location.href = url;
}

function wbCosEvaluationMaintainCosEvalUrl(cur_page,page_size){
	url = wb_utils_invoke_disp_servlet(
		'module','content.CosEvaluationModule',
		'cmd','get_cos_eval_lst',
		'stylesheet','cos_evn_form_maintain_lst.xsl'
	);
	if(cur_page != 0 && page_size != 0 && cur_page != '' && page_size != '' && cur_page != undefined && page_size != undefined){
		url = wb_utils_invoke_disp_servlet(
				'module','content.CosEvaluationModule',
				'cmd','get_cos_eval_lst',
				'stylesheet','cos_evn_form_maintain_lst.xsl',
				'cur_page',cur_page,
				'page_size',page_size
			);
	}
	return url;
}
function wbCosEvaluationInsertCosEvalPrep(tcr_id){
	if(tcr_id == null) tcr_id = 0;
	url = wb_utils_invoke_servlet(
		'cmd','get_tpl', 'tpl_type',
		'FOR','tpl_subtype', 'EVN','dpo_view',
		'IST_EDIT','stylesheet', 'cos_evn_form_ins.xsl', 'tcr_id', tcr_id
	)
	parent.location.href = url;
}

function wbCosEvaluationInsertCosEvalExec(frm,lang){	
	if(_validateForm(frm,lang)){
		if (frm.mod_tcr_id) {
			if (frm.tcr_id_lst.options[0].value == '' || frm.tcr_id_lst.options[0].text == '') {
			    alert(wb_msg_pls_input_tcr);
		      	return;
	    	}
			frm.mod_tcr_id.value = frm.tcr_id_lst.options[0].value;
		}
		for(i=0;i<frm.mod_status_ind.length;i++){
			if(frm.mod_status_ind[i].checked){frm.mod_status.value = frm.mod_status_ind[i].value;}
		}
				
		frm.method = "post";
		frm.action = wb_utils_servlet_url;
		frm.mod_type.value = 'SVY';
		frm.url_success.value = wbCosEvaluationMaintainCosEvalUrl()
		frm.url_failure.value = parent.location.href;
		
		frm.mod_eff_start_datetime.value = "IMMEDIATE";
		frm.mod_eff_end_datetime.value = "UNLIMITED";
		frm.submit();
	}
}

function wbCosEvaluationUpdateCosEvalPrep(mod_id){
	url = wb_utils_invoke_servlet(
		'cmd','get_mod',
		'course_id','',
		'mod_id',mod_id,
		'stylesheet','cos_evn_form_upd.xsl',
		'dpo_view','IST_EDIT'
	)
	parent.location.href = url;
}

function wbCosEvaluationUpdateCosEvalExec(frm,lang){
	
	if(_validateForm(frm,lang)){
		if (frm.mod_tcr_id) {
			if (frm.tcr_id_lst.options[0].value == '' || frm.tcr_id_lst.options[0].text == '') {
			    alert(wb_msg_pls_input_tcr);
		      	return;
	    	}
			frm.mod_tcr_id.value = frm.tcr_id_lst.options[0].value;
		}
		frm.mod_eff_start_datetime.value = "IMMEDIATE";
		frm.mod_eff_end_datetime.value = "UNLIMITED";

		for(i=0;i<frm.mod_status_ind.length;i++){
			if(frm.mod_status_ind[i].checked){frm.mod_status.value = frm.mod_status_ind[i].value;}
		}
			
		frm.url_success.value = wbCosEvaluationMaintainCosEvalUrl(1,10)
		frm.url_failure.value = parent.location.href;
		frm.method = "post"			
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

function wbCosEvaluationDeleteCosEval(mod_id,timestamp,lang){
	
	if(!confirm(eval('wb_msg_' + lang + '_confirm'))) {return;}

	url = wb_utils_invoke_servlet(
		'cmd','del_mod_lst',
		'res_id_lst',mod_id,
		'res_timestamp_lst',timestamp,
		'url_success', wbCosEvaluationMaintainCosEvalUrl(),
		'url_failure', wbCosEvaluationMaintainCosEvalUrl()
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
	url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_tpl","mod_id",mod_id,"rpt_type","SURVEY_IND","stylesheet","evn_view_submission_srh.xsl");
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
		if (getChars(frm.mod_title.value) > 80){
			alert(fetchLabel("label_core_training_management_292"));
			return false;
		}
	}
	
	return true;
}