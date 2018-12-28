// ------------------ wizBank Score Scheme object ------------------- 
// Convention:
//   public functions : use "wbScoreScheme" prefix 
//   private functions: use "_wbScoreScheme" prefix
// ------------------------------------------------------------ 

/* constructor */
function wbScoreScheme(){
	this.get_score_scheme_list = wbScoreSchemeGetList;
	this.add_new_cmt = wbScoreSchemeNewItem;
	this.add_new_cmt_exec = wbScoreSchemeNewItemExec;
	this.add_new_cmt_final = wbScoreSchemeNewItemFinalExec;
	this.mod_percent = wbScoreSchemeModPercent;
	this.get_scoring_itm_lst = wbScoringItmList;
	this.upd_cmt = wbScoreSchemeUpd;
	this.del_cmt = wbScoreSchemeDel;
	this.upd_exec = wbScoreSchemeUpdExec;
	this.get_criteria = wbScoreSchemeCompletionCriteria;
}

/* public functions */
function wbScoreSchemeGetList(itm_id){
	var url = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');
	window.location.href = url;
}

function wbScoreSchemeNewItem(itm_id,cos_res_id){
	var url = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'cos_res_id', cos_res_id, 'stylesheet', 'ae_add_new_cmt.xsl');
	window.location.href = url;
}

function wbScoreSchemeNewItemExec(frm,ccr_id,itm_id,lang,cos_res_id,content_def){
	var is_status_on = true;
	var is_offline = false;
	if ( frm.cmt_method != null ) {
		if ( frm.cmt_method.type != 'radio' ){
					if ( frm.cmt_method[1].checked ) {
						is_offline=true;
					}
			}
	}
	var is_parent = false;
	if(content_def){
		if(content_def =='PARENT'){
			is_parent=true;
		}	
	}
	if(frm.selected_mod_status && is_offline && !is_parent) {
		is_status_on = check_mod_status(frm.selected_mod_status);
	}
	if(is_status_on && _validateForm(frm,lang)){
		frm.method = 'get';
		frm.ccr_id.value = ccr_id;
		frm.stylesheet.value = 'ae_mod_per.xsl';
		frm.action = wb_utils_disp_servlet_url;
		if ( frm.cmt_method != null ) {
			if ( frm.cmt_method.type != 'radio' ){
				if ( frm.cmt_method[1].checked ) {
					frm.mod_res_id.value = frm.online_module.options[frm.online_module.selectedIndex].value;
				} else frm.mod_res_id.value = 0;
			} else frm.mod_res_id.value = 0;
		}
		frm.itm_id.value = itm_id;
		frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');		
		frm.submit();
	}
}

function wbScoreSchemeNewItemFinalExec(frm,ccr_id,itm_id,lang,mod_cnt){
	if(_validatePercent(frm,lang,mod_cnt)){
		frm.method = 'post';
		frm.ccr_id.value = ccr_id;
		if ( mod_cnt == 0){
			frm.cmt_id_list.value = frm.cmt_id_del.value;
		} else if ( mod_cnt == 1 ){
			if(frm.cmt_id_del.value == null || frm.cmt_id_del.value == 0)
				frm.cmt_id_list.value = 0;
			else{
				frm.cmt_id_list.value = frm.cmt_contri_rate.id;
			} 
			frm.cmt_id_percent_list.value = 100;
		} else {
			for(i=0;i<frm.cmt_contri_rate.length;i++){
				if ( i == 0 ){
					frm.cmt_id_list.value = frm.cmt_contri_rate[i].id;
					frm.cmt_id_percent_list.value = frm.cmt_contri_rate[i].value;
				} else {
					frm.cmt_id_list.value = frm.cmt_id_list.value + "~" + frm.cmt_contri_rate[i].id;
					frm.cmt_id_percent_list.value = frm.cmt_id_percent_list.value + "~" + frm.cmt_contri_rate[i].value;
				}
			}
		}
		if(frm.re_evaluate){
			if(frm.re_evaluate[0].checked)
				frm.re_evaluate_ind.value = false;
			else if(frm.re_evaluate[1].checked){
				if(frm.upd_date.checked)
					frm.upd_comp_date.value = true;
				frm.re_evaluate_ind.value = true;
			}
		}
		frm.action = wb_utils_disp_servlet_url;
		frm.itm_id.value = itm_id;
		frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');
		if(frm.re_evaluate_ind.value == 'true')
			frm.url_success.value = wb_utils_invoke_disp_servlet('module','course.CourseCriteriaModule','cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');		
		frm.submit();
	}
}

function wbScoreSchemeModPercent(frm,ccr_id,itm_id,lang,cos_res_id,mod_cnt,lang){
	if(mod_cnt == 0 ){
		return;
	}
	if (mod_cnt == 1){
		alert(eval('wb_msg_'+lang+'_only_one'));
		return;
	} else {
		wbScoreSchemeNewItemExec(frm,ccr_id,itm_id,lang,cos_res_id);
	}
}

function wbScoreSchemeDel(frm,ccr_id,cmt_id_del,lang,itm_id){
	if(confirm(eval('wb_msg_'+lang+'_confirm'))){
		frm.ccr_id.value = ccr_id;
		frm.stylesheet.value = 'ae_mod_per.xsl';
		frm.action = wb_utils_disp_servlet_url;
		frm.cmt_id_del.value = cmt_id_del;
		frm.itm_id.value = itm_id;
		frm.submit();
	}else return;
}

function wbScoreSchemeUpd(frm,cmt_id,itm_id,ccr_id,cos_res_id){
	var url = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'upd_cmt', 'cmt_id', cmt_id, 'ccr_id', ccr_id, 'cos_res_id', cos_res_id, 'itm_id', itm_id, 'stylesheet', 'upd_cmt.xsl');		
	window.location.href = url;	
}

function wbScoreSchemeUpdExec(frm,cmt_id,itm_id,ccr_id,lang,content_def){
	
	var is_status_on = true;
	var is_offline = false;
	if ( frm.cmt_method != null ) {
		if ( frm.cmt_method.type != 'radio' ){
					if ( frm.cmt_method[1].checked ) {
						is_offline=true;
					}
			}
	}
	var is_parent = false;
	if(content_def){
		if(content_def =='PARENT'){
			is_parent=true;
		}	
	}
	if(frm.selected_mod_status && is_offline && !is_parent) {
		is_status_on = check_mod_status(frm.selected_mod_status);
	}
	if(is_status_on && _validateForm(frm,lang)){
		if ( frm.cmt_method != null ) {
			if ( frm.cmt_method.type != 'radio' ){
				if ( frm.cmt_method[1].checked ) {
					frm.mod_res_id.value = frm.online_module.options[frm.online_module.selectedIndex].value;
				} else frm.mod_res_id.value = 0;
			} else frm.mod_res_id.value = 0;
		}
		frm.method = 'post';
		frm.itm_id.value = itm_id;
		frm.ccr_id.value = ccr_id;
		frm.cmt_id.value = cmt_id;	
		if(frm.re_evaluate){
			if(frm.re_evaluate[0].checked)
				frm.re_evaluate_ind.value = false;
			else if(frm.re_evaluate[1].checked){
				if(frm.upd_date.checked)
					frm.upd_comp_date.value = true;
				frm.re_evaluate_ind.value = true;
			}
		}	
		frm.action = wb_utils_disp_servlet_url;
		frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');
		if(frm.re_evaluate_ind.value == 'true')
			frm.url_success.value = wb_utils_invoke_disp_servlet('module','course.CourseCriteriaModule','cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_lst.xsl');
		frm.submit();
	} else return;
}
	
function _validateForm(frm,lang){
	if(frm.cmt_title){
		frm.cmt_title.value = wbUtilsTrimString(frm.cmt_title.value);
		if (!gen_validate_empty_field(frm.cmt_title, frm.cmt_title.title,lang)){
			return false;
		}else if(getChars(frm.cmt_title.value)>80){
			alert(eval('wb_msg_'+lang+'_title_not_longer'));
			return false;
		} else if(frm.cmt_max_score){
			if(frm.cmt_max_score.disabled == false){
				frm.cmt_max_score.value = wbUtilsTrimString(frm.cmt_max_score.value);
				if (!gen_validate_empty_field(frm.cmt_max_score, frm.cmt_max_score.title,lang)){
					return false;
				}
				frm.cmt_pass_score.value = wbUtilsTrimString(frm.cmt_pass_score.value);
				if (!gen_validate_empty_field(frm.cmt_pass_score, frm.cmt_pass_score.title,lang)){
					return false;
				}
				if(!gen_validate_max_integer_diy_value(frm.cmt_max_score, frm.cmt_max_score.title, lang, 99999, true)){
					return false;
				}
				var val = wbUtilsTrimString(frm.cmt_pass_score.value);
				if (val.search(/[^0-9]/) != -1 || Number(val) <= 0){
					Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + frm.cmt_pass_score.title + '"' + wb_msg_pls_enter_positive_integer_2);
					frm.cmt_pass_score.focus();
					return false;
				}
				if(parseInt(frm.cmt_max_score.value) < parseInt(frm.cmt_pass_score.value)){
					alert(eval('wb_msg_'+lang+'_max_pass_score'));
					frm.cmt_pass_score.focus();
					return false;
				}
				return true;
			}
			return true;
		}
	} else return true;
}

function _validatePercent(frm,lang,mod_cnt){
	var sum = 0.0;
	if ( mod_cnt ==0){
		return true;
	} else if ( mod_cnt == 1 ) {
		if ( _validate_pencentage(frm.cmt_contri_rate,frm.cmt_contri_rate.lang,lang)){
			sum = frm.cmt_contri_rate.value;
		}		
		if ( sum != 100){
			alert(eval('wb_msg_'+lang+'_percent'));
			frm.cmt_contri_rate.focus();
			return false;
		} else return true;
	} else {
		if ( frm.cmt_contri_rate ) {
			for(i=0;i<frm.cmt_contri_rate.length;i++){
				if ( _validate_pencentage(frm.cmt_contri_rate[i],frm.cmt_contri_rate[i].lang,lang)){
					sum += parseFloat(frm.cmt_contri_rate[i].value);
				} else {
					return false;
					
					break;
				}
			}
			if ( sum != 100){
				alert(eval('wb_msg_'+lang+'_percent'));
				frm.cmt_contri_rate[0].focus();
				return false;
			}
		}
	}
	return true;
}

function _validate_pencentage(fld, txtFldName,lang) {
	val = wbUtilsTrimString(fld.value);
	if (fld.value.indexOf('.') != -1){
		alert(wb_msg_pls_enter_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_integer_2);
		fld.focus();
		return false;
	} else {		
		if (val.search(/[^0-9]/) != -1) {
			alert(wb_msg_pls_enter_0_to_100_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_0_to_100_2);
			fld.focus();
			return false;
		}else if (fld.value > 100 ){
			alert(wb_msg_pls_enter_0_to_100_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_0_to_100_2);
			fld.focus();
			return false;
		}
		else if(fld.value < 0 ){
			alert(wb_msg_pls_enter_0_to_100_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_0_to_100_2);
			fld.focus();
			return false;
		}	
		return true;
	}
}

function _validate_positive_integer(fld,txtFldName,lang){

	val = wbUtilsTrimString(fld.value)
	if ( val.search(/[^0-9]/) != -1 ){
		alert(wb_msg_pls_enter_positive_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}
	if (val <= 0){
		alert(wb_msg_pls_enter_positive_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}
	if (val > 999999999){
		alert(txtFldName + wb_msg_pls_enter_smaller_number);
		fld.focus();
		return false;

	}
	return true;
}

function wbScoringItmList(itm_id){
	var url = wb_utils_invoke_disp_servlet('module','course.CourseCriteriaModule','cmd', 'get_cmt_lst', 'itm_id', itm_id, 'stylesheet', 'scoring_item_lst.xsl');
	window.location.href = url;
}

function wbScoreSchemeCompletionCriteria(itm_id, is_new_cos){
	var redirect_url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_publish_new_cos.xsl');
	var url = wb_utils_invoke_disp_servlet('module','course.CourseCriteriaModule', 'cmd', 'get_criteria_cond_lst','itm_id',itm_id, 'stylesheet', 'completion_criteria_cond_run.xsl', 'is_new_cos', is_new_cos, 'redirect_url', redirect_url);
	document.location.href = url;
}

function check_mod_status(obj) {
	var is_status_on = true;
	if(obj.value === 'OFF' && !confirm(wb_msg_mod_not_public2)) {
		is_status_on = false;
	}
	return is_status_on;
}
function change_res_status(obj) {
	var is_public = document.getElementById('mod_public_'+obj.value).value;
	document.getElementById("selected_mod_status").value = is_public;
}
