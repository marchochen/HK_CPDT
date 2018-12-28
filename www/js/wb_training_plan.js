// ------------------ wizBank TrainingPlan object ------------------- 
// Convention:
//   public functions : use "wbTrainingPlan" prefix 
//   private functions: use "_wbTrainingPlan" prefix
function wbTrainingPlan(){
	this.impl_plan = wbTpplanImpl;
	this.get_plan_lst = wbGetPlanList;
	this.get_plan_by_code = wbGetPlanByCode;
	this.add_makeup_pre = wbAddMakeUpPreview;
	this.add_makeup = wbAddMakeUp;
	this.upd_makeup = wbUpdMakeUp;
	this.edit_plan_prep = wbEditPlanPrep;
	this.get_plan_detail = wbGetPlanDetail;
	this.refer_makeup_plan = weReferMakeupPlan;
	this.del_makeup_plan = weDeleteMakeupPlan;
	this.get_plan_config = wbGetPlanConfig;
	this.save_plan_config = wbSavePlanConfig;
	this.submit_year_plan = wbSubmitYearPlan;
	this.del_year_plan = wbDeleteYearPlan;
	
	this.Import = new wbTpPlanImport;	
	this.get_year_plan = wbGetYearPlan;
	this.get_out_plan_lst = wbGetOutPlanList;
	this.get_year_plan_lst = wbGetYearPlanList;
	this.upd_status_exec = wbUpdateStatusExec;
 	this.upd_multi_status_exec = wbUpdateMultiStatusExec;
	
	this.implement_plan_prep = wbImplementPlanPrep;	
  this.implement_plan_exec = wbImplementPlanExec;
}

function wbTpplanImpl(frm){
		var temp_url="";
		var entrance= getUrlParam('entrance');
		var tcr_id ;
		if(frm.tcr_id){
			tcr_id=frm.tcr_id.value
		}
		var url_failure= wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan','tpn_id', frm.plan_id.value,'entrance', 'FTN_AMD_PLAN_CARRY_OUT', 'stylesheet', 'training_plan_detail.xsl','tcr_id',tcr_id);
    if (frm.impl_type_new.checked) {
        var hasSelect = false;
        var ity_id = null;
        var dummy_type = null;
        if (frm.ity_id.checked) {
            frm.ity_id.value;
            frm.dummy_type.value;
        }else {
            for (var i = 0; i < frm.ity_id.length; i++) {
                var rad = frm.ity_id[i];
                if (rad.checked) {
                    hasSelect = true;
                    ity_id = rad.value;
                    //dummy_type = rad.dummy_type;
                    dummy_type = rad.getAttribute('dummy_type');
                    break;
                }
            }
        }
        if (!hasSelect) {
            alert(frm.lab_msg_no_type_sel.value);
            return;
        }
			frm.itm_dummy_type.value=dummy_type;
			if (ity_id !== undefined && ity_id != 'INTEGRATED') {
				frm.itm_integrated_ind.value = false;
			}
    		frm.cmd.value = 'ae_form_ins_itm';
			frm.action = wb_utils_ae_servlet_url;
			frm.method = 'get';
			frm.tvw_id.value = 'DETAIL_VIEW';
			frm.stylesheet.value = 'itm_add.xsl';
			
			frm.url_failure.value=url_failure;	
			frm.submit();
        
    }else if (frm.impl_type_add.checked) {
        if (frm.itm_id) {
            var itm_id = frm.itm_id.options[0].value;
            if (itm_id == '' || itm_id <= 0) {
                alert(frm.lab_msg_no_name_spec.value);
                return;
            }
           var url = wb_utils_invoke_ae_servlet('cmd', 'ae_form_ins_itm', 'stylesheet', 'itm_add_run.xsl', 'ity_id', 'CLASSROOM', 'tvw_id', 'DETAIL_VIEW', 'itm_id', itm_id, 'training_plan',true, 'plan_id',frm.plan_id.value,'tpn_update_timestamp',frm.tpn_update_timestamp.value,'url_failure',url_failure);
					window.location.href = url;
        }
    }
}

function wbGetPlanList(tcr_id, year, month, tpn_status,search_tcr_id){
	url=_wbGetPlanListURL(tcr_id, year, month, tpn_status,search_tcr_id);
	window.location.href = url;
}
function wbGetPlanByCode(frm, tpn_tcr_id, year, month, tpn_status, tpn_type, tpn_status_lst, is_makeup) {
	var stylesheet = frm.stylesheet.value;
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 
	'cmd', 'get_training_plan_lst',
	'tcr_id', tpn_tcr_id,
	'tpn_type',tpn_type,
	'tpn_date_year', year,
	'tpn_date_month', month, 
	'is_makeup', is_makeup,
	'tpn_status', tpn_status,
	'tpn_status_lst', tpn_status_lst,
	'stylesheet', stylesheet);
	
	window.location.href = url;
	return;
}


function wbEditPlanPrep(tpn_id, tcr_id, entrance) {
	if (entrance =='' || entrance==null){
		entrance = ''
	}
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule','cmd','get_training_plan','stylesheet', 'training_plan_add_upd.xsl','tpn_id', tpn_id,'tcr_id', tcr_id,'entrance',entrance);
	window.location.href = url;
}
function wbAddMakeUpPreview(testr) {
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule','cmd','add_training_plan_pre','stylesheet', 'training_plan_add_upd.xsl');
	window.location.href = url;
}

function wbUpdMakeUp(frm, tcr_id,tpn_id, lang, entrance) {
	if(_makeUpValidated(frm, lang)) {
		frm.module.value = "tpplan.tpPlanModule";
		frm.cmd.value = "upd_training_plan_exe";
		url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan','tcr_id', tcr_id, 'tpn_id', tpn_id, 'stylesheet', 'training_plan_detail.xsl', 'entrance', entrance)
		frm.url_success.value = url;   
		frm.url_failure.value = url;   
		frm.tcr_id.value = tcr_id;
		frm.action = wb_utils_disp_servlet_url;
		frm.method = "post";
		frm.submit();
		return;
	}
	return;
}
function wbAddMakeUp(frm, tcr_id, lang) {
	if(_makeUpValidated(frm, lang)) {
		frm.module.value = "tpplan.tpPlanModule";
		frm.cmd.value = "add_training_plan_exe";
		frm.tpn_status.value = 'PREPARED';
		frm.tpn_type.value='MAKEUP'
		frm.url_success.value = _wbGetMakeUpPlanListURL();
		frm.url_failure.value = _wbGetMakeUpPlanListURL();
		frm.action = wb_utils_disp_servlet_url;
		frm.method = "post";
		frm.submit();
		return;
	}
	return;
}
function _wbGetMakeUpPlanListURL(){
		url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan_lst', 'tpn_status', 'ALL', 'tpn_status_lst','PREPARED~PENDING~APPROVED~DECLINED~IMPLEMENTED', 'stylesheet', 'training_plan_adhoc_lst.xsl','tpn_type', 'MAKEUP');
		return url;
}

function _makeUpValidated(frm, lang) {
	if(frm.tpn_date){
		if((frm.tpn_date_yy.value!=""&&frm.tpn_date_yy.value!=null)||(frm.tpn_date_mm.value!=""&&frm.tpn_date_mm.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".tpn_date", frm.tpn_date_label.value,'YY','ym') || !wbUtilsValidateDate("document." + frm.name + ".tpn_date", frm.tpn_date_label.value,'MM','ym')){
				return false;
			}
			frm.tpn_date.value = frm.tpn_date_yy.value + "-" + frm.tpn_date_mm.value + "-" + "01" + " 00:00:00.00";
		}else{
			alert(wb_msg_usr_please_specify_value + '"' + frm.tpn_date_label.value + '"' );
			frm.tpn_date_yy.focus();
			return false;
		}
	}
  /*
	if(!wbUtilsValidateEmptyField(frm.tpn_code, frm.tpn_code_label.value)) {
		return false;
	}
  */
 	frm.tpn_name.value = wbUtilsTrimString(frm.tpn_name.value);
	if(!wbUtilsValidateEmptyField(frm.tpn_name, frm.tpn_name_label.value)) {
		return false;
	}
	frm.tpn_cos_type.value = wbUtilsTrimString(frm.tpn_cos_type.value);
	if(!wbUtilsValidateEmptyField(frm.tpn_cos_type, frm.tpn_cos_type_label.value)) {
		return false;
	}
	if(frm.tpn_tnd_title) {
		frm.tpn_tnd_title.value = wbUtilsTrimString(frm.tpn_tnd_title.value);
		if(getChars(frm.tpn_tnd_title.value) > 100 ){
			alert(wb_msg_tnd_title_too_longer);
			frm.tpn_tnd_title.focus();
			return false;
		}
	}
	if(frm.tpn_introduction) {
		frm.tpn_introduction.value = wbUtilsTrimString(frm.tpn_introduction.value);
		if(getChars(frm.tpn_introduction.value) > 400)
		{
			alert(wb_msg_introduction_too_longer);
			frm.tpn_introduction.focus();
			return false;
		}
	}
	if(frm.tpn_aim) {
		frm.tpn_aim.value = wbUtilsTrimString(frm.tpn_aim.value);
		if(getChars(frm.tpn_aim.value) > 2000 ){
			alert(wb_msg_purpose_too_longer);
			frm.tpn_aim.focus();
			return false;
		}
	}
	if(frm.tpn_target) {
		frm.tpn_target.value = wbUtilsTrimString(frm.tpn_target.value);
		if(getChars(frm.tpn_target.value) > 2000 ){
			alert(wb_msg_target_too_longer);
			frm.tpn_target.focus();
			return false;
		}

	}
	
	if(frm.tpn_responser) {
		frm.tpn_responser.value = wbUtilsTrimString(frm.tpn_responser.value);
 		if(!wbUtilsValidateEmptyField(frm.tpn_responser, frm.tpn_responser_label.value)) {
			return false;
		}
		if(getChars(frm.tpn_responser.value) > 2000 ){
			alert(wb_msg_responser_too_longer);
			frm.tpn_responser.focus();
			return false;
		}

	}
	frm.tpn_duration.value = wbUtilsTrimString(frm.tpn_duration.value);
	if(frm.tpn_duration.value !="" && frm.tpn_duration.value !=null){
		if(getChars(frm.tpn_duration.value) > 2000 ){
			alert(wb_msg_duration_too_longer);
			frm.tpn_duration.focus();
			return false;
		}
		
	}
	if(frm.tpn_ftf_start_date){
		if((frm.tpn_ftf_start_datetime_yy.value!=""&&frm.tpn_ftf_start_datetime_yy.value!=null)
		  ||(frm.tpn_ftf_start_datetime_mm.value!=""&&frm.tpn_ftf_start_datetime_mm.value!=null)
		  ||(frm.tpn_ftf_start_datetime_dd.value!=""&&frm.tpn_ftf_start_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".tpn_ftf_start_datetime", frm.tpn_ftf_start_date_label.value)){
				return false;
			}
			frm.tpn_ftf_start_date.value = frm.tpn_ftf_start_datetime_yy.value + "-" + frm.tpn_ftf_start_datetime_mm.value + "-" + frm.tpn_ftf_start_datetime_dd.value + " 00:00:00.00";
		} else {
			frm.tpn_ftf_start_date.value = '';
		}
	}
	if(frm.tpn_ftf_end_date){
		if((frm.tpn_ftf_end_datetime_yy.value!=""&&frm.tpn_ftf_end_datetime_yy.value!=null)
		  ||(frm.tpn_ftf_end_datetime_mm.value!=""&&frm.tpn_ftf_end_datetime_mm.value!=null)
		  ||(frm.tpn_ftf_end_datetime_dd.value!=""&&frm.tpn_ftf_end_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".tpn_ftf_end_datetime", frm.tpn_ftf_end_date_label.value)){
				return false;
			}
			frm.tpn_ftf_end_date.value = frm.tpn_ftf_end_datetime_yy.value + "-" + frm.tpn_ftf_end_datetime_mm.value + "-" + frm.tpn_ftf_end_datetime_dd.value + " 00:00:00.00";
		} else {
			frm.tpn_ftf_end_date.value = '';
		}
	}
	
	if(frm.tpn_ftf_start_date && frm.tpn_ftf_end_date) {
		if(frm.tpn_ftf_start_datetime_yy.value !='' && frm.tpn_ftf_end_datetime_yy.value !='') {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'tpn_ftf_start_datetime', 
				end_obj : 'tpn_ftf_end_datetime', 
				start_nm : frm.lab_start_date.value, 
				end_nm : frm.lab_end_date.value
				})) {
				return false;
			}	
		}
	}
	
	if(frm.tpn_wb_start_date){
		if((frm.tpn_wb_start_datetime_yy.value!=""&&frm.tpn_wb_start_datetime_yy.value!=null)
		  ||(frm.tpn_wb_start_datetime_mm.value!=""&&frm.tpn_wb_start_datetime_mm.value!=null)
		  ||(frm.tpn_wb_start_datetime_dd.value!=""&&frm.tpn_wb_start_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".tpn_wb_start_datetime", frm.tpn_wb_start_date_label.value)){
				return false;
			}
			frm.tpn_wb_start_date.value = frm.tpn_wb_start_datetime_yy.value + "-" + frm.tpn_wb_start_datetime_mm.value + "-" + frm.tpn_wb_start_datetime_dd.value + " 00:00:00.00";
		} else {
			frm.tpn_wb_start_date.value = '';
		}
	}	
	if(frm.tpn_wb_end_date){
		if((frm.tpn_wb_end_datetime_yy.value!=""&&frm.tpn_wb_end_datetime_yy.value!=null)
		  ||(frm.tpn_wb_end_datetime_mm.value!=""&&frm.tpn_wb_end_datetime_mm.value!=null)
		  ||(frm.tpn_wb_end_datetime_dd.value!=""&&frm.tpn_wb_end_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".tpn_wb_end_datetime", frm.tpn_wb_end_date_label.value)){
				return false;
			}
			frm.tpn_wb_end_date.value = frm.tpn_wb_end_datetime_yy.value + "-" + frm.tpn_wb_end_datetime_mm.value + "-" + frm.tpn_wb_end_datetime_dd.value + " 00:00:00.00";
		} else {
			frm.tpn_wb_end_date.value = '';
		}
	}
	if(frm.tpn_wb_start_date && frm.tpn_wb_end_date) {
		if(frm.tpn_wb_start_datetime_yy.value !='' && frm.tpn_wb_end_datetime_yy.value !='') {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'tpn_wb_start_datetime', 
				end_obj : 'tpn_wb_end_datetime', 
				start_nm : frm.lab_start_date.value, 
				end_nm : frm.lab_end_date.value
				})) {
				return false;
			}	
		}
	}
	frm.tpn_lrn_count.value = wbUtilsTrimString(frm.tpn_lrn_count.value);
	if(frm.tpn_lrn_count.value !="" && frm.tpn_lrn_count.value !=null){
		if(!wbUtilsValidateInteger(frm.tpn_lrn_count, frm.tpn_lrn_count_label.value)) {
			return false;
		}	
	}
	frm.tpn_fee.value = wbUtilsTrimString(frm.tpn_fee.value);
	if(frm.tpn_fee.value !=""  && frm.tpn_fee.value !=null) {
		if(!gen_validate_float(frm.tpn_fee, frm.tpn_fee_label.value)) {
			return false;
		}
	}
	
	if(frm.tpn_remark) {
		frm.tpn_remark.value = wbUtilsTrimString(frm.tpn_remark.value);
		if(getChars(frm.tpn_remark.value) > 4000 ){
			alert(wb_msg_remark_too_longer);
			frm.tpn_remark.focus();
			return false;
		}
	}
	return true;
}


function _wbGetPlanListURL(tpn_type, tpn_status_lst){
	url= wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan_lst', 'tpn_status', 'ALL', 'tpn_status_lst', tpn_status_lst, 'stylesheet', 'training_plan_adhoc_lst.xsl','tpn_type', tpn_type);
	return url;
}
function wbGetPlanDetail(tcr_id, tpn_id, entrance){
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 
	'cmd', 'get_training_plan',
	'tcr_id', tcr_id, 
	'tpn_id', tpn_id,
	'entrance', entrance,
	'stylesheet', 'training_plan_detail.xsl');
	
	window.location.href = url;
}


function weReferMakeupPlan(tpn_id, tcr_id, tpn_update_timestamp){
	if(confirm(wb_msg_confirm)){
		var url_failure = self.location.href;
		var url_success = self.location.href;
		url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 
		'cmd', 'refer_markup_plan',
		'tpn_id', tpn_id,
		'tcr_id', tcr_id,
		'tpn_update_timestamp', tpn_update_timestamp,
		'url_success', url_success,
		'url_failure', url_failure);
		
		window.location.href = url;
	}
}

function weDeleteMakeupPlan(tcr_id, tpn_id, tpn_update_timestamp){
	if(confirm(wb_msg_confirm)){
		var url_failure = _wbGetMakeUpPlanListURL();
		var url_success = _wbGetMakeUpPlanListURL();
		url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 
		'cmd', 'del_markup_plan',
		'tpn_id', tpn_id,
		'tcr_id', tcr_id,
		'tpn_update_timestamp', tpn_update_timestamp,
		'url_success', url_success,
		'url_failure', url_failure)
		
		window.location.href = url;
	}
}

function wbSubmitYearPlan(tcr_id, ypn_year, ypn_update_timestamp){
	if(confirm(wb_msg_confirm)){
		var url_failure = self.location.href;
		var url_success = self.location.href;
		url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 
		'cmd', 'submit_year_plan',
		'tcr_id', tcr_id,
		'year', ypn_year,
		'ypn_tcr_id', tcr_id,
		'ypn_update_timestamp', ypn_update_timestamp,
		'url_success', url_success,
		'url_failure', url_failure
		);
		window.location.href = url;
	}
}

function wbDeleteYearPlan(tcr_id, ypn_year, ypn_update_timestamp){
	if(confirm(wb_msg_confirm)){
		var url_failure = self.location.href;
		var url_success = self.location.href;
		url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 
		'cmd', 'del_year_plan',
		'tcr_id', tcr_id,
		'year', ypn_year,
		'ypn_tcr_id', tcr_id,
		'ypn_update_timestamp', ypn_update_timestamp,
		'url_success', url_success,
		'url_failure', url_failure)
		
		window.location.href = url;
	}
}

function wbImplementPlanPrep(tcr_id, tpn_id, tpn_update_timestamp,entrance){
	var url_failure= wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan','tcr_id', tcr_id,'tpn_id', tpn_id,'entrance', 'FTN_AMD_PLAN_CARRY_OUT', 'stylesheet', 'training_plan_detail.xsl');
	var url=wb_utils_invoke_ae_servlet('cmd','ae_get_all_ity_form','stylesheet','training_plan_impl.xsl','training_plan',true,'tcr_id',tcr_id,'plan_id',tpn_id,'tpn_update_timestamp',tpn_update_timestamp,'entrance',entrance,'url_failure',url_failure);
		window.location.href = url;
}

function wbGetPlanConfig(year, tcr_id) {
	if(year == null || year == '') {
		year = 0;	
	}
	if(tcr_id == null || tcr_id == '') {
		tcr_id = 0;	
	}
	var url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'set_year_config_prepare', 'stylesheet', 'training_plan_year_setting.xsl', 'year', year, 'tcr_id', tcr_id);
	window.location.href = url;
}

function wbSavePlanConfig(frm, lang) {
	if(_wbSavePlanConfigValidate(frm, lang)) {
		frm.module.value = "tpplan.tpPlanModule";
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.cmd.value = 'save_year_config';
		frm.url_success.value = self.location;
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'set_year_config_prepare', 'stylesheet', 'training_plan_year_setting.xsl','year', 0);
		frm.submit();
	}
}

function wbGetYearPlan(tcr_id) {
	if(tcr_id == null || tcr_id == '') {
		tcr_id = 0;	
	}
	var url_fail = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan_lst', 'tpn_status', 'ALL', 'stylesheet', 'training_plan_lst.xsl');
	var url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_year_plan_lst', 'stylesheet', 'training_plan_year_lst.xsl', 'tcr_id', tcr_id, 'url_failure', url_fail);
	window.location.href = url;
}

function _wbSavePlanConfigValidate(frm, lang){
	var before = 0;
	var	after = 0;
	if(frm.submit_from_yy && frm.submit_from_mm && frm.submit_from_dd){
        if (!wbUtilsValidateDate(frm.name + ".submit_from", frm.lab_start_time.value)) {
            return false;
        }
        before = 1;
        frm.submit_from.value = frm.submit_from_yy.value + "-" + frm.submit_from_mm.value + "-" + frm.submit_from_dd.value + " 00:00:00.00";
	}

	if(frm.submit_to_yy && frm.submit_to_mm && frm.submit_to_dd){
        if (!wbUtilsValidateDate(frm.name + ".submit_to", frm.lab_end_time.value)) {
            return false;
        }
	    after = 1;
	    frm.submit_to.value = frm.submit_to_yy.value + "-" + frm.submit_to_mm.value + "-" + frm.submit_to_dd.value + " 23:59:59.00";
	}	
	
	if(!wb_utils_validate_date_compare({
		frm : 'document.' + frm.name, 
		start_obj : 'submit_from', 
		end_obj : 'submit_to', 
		start_nm : frm.lab_start_date.value, 
		end_nm : frm.lab_end_date.value
		})) {
		return false;	
	}
	if ( before == after == 1){
		var invalid = false;
		if (frm.submit_from_yy.value < frm.submit_to_yy.value ){
			invalid = true;
		} else if (frm.submit_from_yy.value == frm.submit_to_yy.value){
			if (frm.submit_from_mm.value < frm.submit_to_mm.value ){
				invalid = true;
			} else if (frm.submit_from_mm.value == frm.submit_to_mm.value) {
				if (frm.submit_from_dd.value <= frm.submit_to_dd.value){
					invalid = true;
				}	
			}
		}	
		if (!invalid){
			alert(eval('wb_msg_'+lang+'_start_end_time'));
			return false;
		}
	}
	
	var i, n, ele
	n = frm.elements.length
	frm.ysg_child_tcr_id_lst.value = '';
	for (i = 0; i < n; i++){
		ele = frm.elements[i];
		if (ele.type == "checkbox"){
			if(ele.checked && ele.name != 'sel_all_checkbox') {
				frm.ysg_child_tcr_id_lst.value += ' ' + ele.value + ' ' + ',';;
			}
		}
	}
	
	if(frm.ysg_child_tcr_id_lst.value == null || frm.ysg_child_tcr_id_lst.value == '') {
		alert(eval('wb_msg_sel_child_tcr_id'));
		return false;	
	}
	
	return true;
}

function wbTpPlanImport(){
	this.prep = wbTpPlanImportPrep;
	this.prep_sup = wbTpPlanImportPrepSup;
	this.exec = wbTpPlanImportExec;
	this.confirm = wbTpPlanImportConfirm;
	this.cancel = wbTpPlanImportCancel;
	this.get_template = wbBatchProcessResImportTemplate;
	this.get_instr = wbBatchProcessResImportInstr
}
function wbBatchProcessResImportTemplate(lang) {
	url = '../htm/import_template/wb_import_tp_plan_template-' + lang + '.xls';
	wbUtilsOpenWin(url, '');
}
function wbBatchProcessResImportInstr() {
	url = wb_utils_invoke_disp_servlet('module', 'upload.UploadModule', 'cmd','get_instr', 'instr_type', 'tpplan', 'stylesheet', 'training_plan_import_instr.xsl');
	wbUtilsOpenWin(url, '');
}
function wbTpPlanImportExec(frm) {
	frm.cmd.value = 'upload_year_plan_exec'
	frm.module.value = 'tpplan.tpPlanModule'
	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true"
	frm.submit()
}

function wbTpPlanImportCancel(tcr_id, year, ypn_year, upd_timestamp) {
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'upload_year_plan_prep', 'year', year, 'tcr_id', tcr_id, 'ypn_year', ypn_year, 'upd_timestamp', upd_timestamp, 'stylesheet', 'training_plan_import_prep.xsl');
	window.location.href = url;
}

function wbTpPlanImportPrep(tcr_id, year, ypn_year, upd_timestamp) {
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 			+ '800'
	+ ',height=' 			+ '500'
	+ ',scrollbars='		+ 'yes'
	+ ',resizable='			+ 'yes';
	if(upd_timestamp==null || upd_timestamp ==''){
		upd_timestamp = '';
	}
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'upload_year_plan_prep', 'year', year, 'tcr_id', tcr_id, 'ypn_year', ypn_year, 'stylesheet', 'training_plan_import_prep.xsl','upd_timestamp',upd_timestamp);
	wbUtilsOpenWin(url,'', false, str_feature);
}

function wbTpPlanImportPrepSup(frm, tcr_id) {
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 			+ '800'
	+ ',height=' 			+ '500'
	+ ',scrollbars='		+ 'yes'
	+ ',resizable='			+ 'yes';
	if(upd_timestamp==null || upd_timestamp ==''){
		upd_timestamp = '';
	}
	var year = frm.ypn_year_sel[frm.ypn_year_sel.selectedIndex].value;
	var ypn_year = '';
	var upd_timestamp = '';
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'upload_year_plan_prep', 'year', year, 'tcr_id', tcr_id, 'ypn_year', ypn_year, 'stylesheet', 'training_plan_import_prep.xsl','upd_timestamp',upd_timestamp);
	wbUtilsOpenWin(url,'', false, str_feature);
}

function wbTpPlanImportConfirm(frm, lang){
    _txtFileName = _wbTpYearPlanImportGetFileName(frm.src_filename_path.value)
	if ( _txtFileName.substring(_txtFileName.lastIndexOf('.')+1).toLowerCase() != 'xls') {
	    Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_enro'))
	} else if (_txtFileName.length > 100)	{
		Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_usr_filename'))
	}else{
		frm.src_filename.value = _txtFileName		
		frm.cmd.value = 'upload_year_plan_confirm';
		frm.module.value = 'tpplan.tpPlanModule'
		frm.stylesheet.value = 'training_plan_import_confirm.xsl';
		frm.url_failure.value = 'javascript:history.back()'
		frm.method = 'post'
		frm.action = wb_utils_disp_servlet_url + "?cmd=upload_year_plan_confirm";
		frm.submit()		
	}
}

function _wbTpYearPlanImportGetFileName(pathname) {
	s = pathname.lastIndexOf("\\");
	if (s == -1) {s = pathname.lastIndexOf("/");}
	if (s == -1) {return pathname;}	
	l = pathname.length - s;
	return pathname.substr(s+1,l);
}

function wbGetOutPlanList(tpn_status, tcr_id){
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_out_training_plan_lst', 'status', tpn_status ,'tcr_id', tcr_id, 'page_size', '10', 'stylesheet', 'training_plan_adhoc_approval.xsl');
	window.location.href = url;
}

function wbPlanSearchSimpleExec(frm,type){
	if(frm.sear_code_name){
		frm.sear_code_name.value = wbUtilsTrimString(frm.sear_code_name.value);
	}
	frm.status.value = frm.status_sel_frm.options[frm.status_sel_frm.selectedIndex].value;
	frm.module.value = "tpplan.tpPlanModule"
	if (type == 'MAKEUP'){
		frm.cmd.value = 'get_out_training_plan_lst';
		frm.stylesheet.value = 'training_plan_adhoc_approval.xsl';
	}else{
		frm.cmd.value = 'get_auditing_yearPlan_lst';
		frm.stylesheet.value = 'training_plan_year_approval.xsl';
	}	
	frm.action = wb_utils_disp_servlet_url;
	frm.method = 'get';
	frm.submit();
}

function wbUpdateMultiStatusExec(frm, status, lang, type){
	if (!frm.planId_chkbox){
		alert(wb_msg_select_plan);
		return;

	}
	if(_wbGetCheckedtrainingPlanLst(frm)[0] == ""){
		alert(wb_msg_select_plan);
		return;
	}else{
		var sel_tpn_id = _wbGetCheckedtrainingPlanLst(frm)[0];
		var sel_tcr_id = _wbGetCheckedtrainingPlanLst(frm)[1];
		var update_timestamp = _wbGetCheckedtrainingPlanLst(frm)[2];
	}
	if(confirm(wb_msg_confirm)){
		var url_success = self.location.href;
		var url_failure = self.location.href;
		var url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'upd_status_exec', 'sel_tpn_id', sel_tpn_id, 'status', status, 'sel_update_timestamp', update_timestamp, 'type', type, 'sel_tcr_id', sel_tcr_id, 'url_success', url_success, 'url_failure', url_failure)
		window.location.href = url
	}
}

function _wbGetCheckedtrainingPlanLst(frm){
	var id = "";
	var tcr_id = "";
	var sel_value = "";
	var list = "";
	var list_tcr_id = "";
	var list_time = "";
	if(frm.planId_chkbox.length){
		for (var i = 0; i < frm.planId_chkbox.length; i++){
			if(frm.planId_chkbox[i].checked){
				sel_value = frm.planId_chkbox[i].value;
				id = sel_value.substr(0,sel_value.indexOf("~"));
				tcr_id = sel_value.substr(sel_value.indexOf("~")+1);
				list += id + "~";
				list_tcr_id += tcr_id + "~";
				list_time += frm.planId_chkbox[i].id + "~";
			}
		}
	}else{
		if(frm.planId_chkbox.checked){
			sel_value = frm.planId_chkbox.value;
			list = sel_value.substr(0,sel_value.indexOf("~"));
			list_tcr_id = sel_value.substr(sel_value.indexOf("~")+1);
			list_time = frm.planId_chkbox.id;
		}
	}

	if(list.indexOf("~") > -1){
		list = list.substr(0, list.length - 1);
	}
	if(list_time.indexOf("~") > -1){
		list_tcr_id = list_tcr_id.substr(0, list_tcr_id.length - 1);
	}
	if(list_time.indexOf("~") > -1){
		list_time = list_time.substr(0, list_time.length - 1);
	}
	var arr = [list,list_tcr_id,list_time];
	return arr;
}

function wbGetYearPlanList(status,tcr_id){
	url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_auditing_yearPlan_lst', 'status', status, 'tcr_id', tcr_id, 'page_size', '10', 'stylesheet', 'training_plan_year_approval.xsl');
	window.location.href = url;
}

function wbUpdateStatusExec(tpn_id,status, update_timestamp, tcr_id, type){
	if(confirm(wb_msg_confirm)) {
		var url_failure = self.location.href;
		var url_success = self.location.href;
		var url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'upd_status_exec', 'sel_tpn_id', tpn_id, 'status', status, 'sel_update_timestamp', update_timestamp, 'type', type, 'sel_tcr_id', tcr_id, 'tcr_id', tcr_id, 'url_success', url_success, 'url_failure', url_failure)	
		window.location.href = url
	}
	
}

function wbImplementPlanExec(frm, tcr_id, tpn_id, lang, action) {
	if(_classValidated(frm, lang, action)) {
		_wbGetClassUsgEntId(frm);
		_wbMasterAssistantIDLst(frm);
		frm.module.value = "tpplan.tpPlanModule";
		frm.cmd.value = "implement_plan_exe";
		frm.url_success.value = _wbGetPlanListURL(tcr_id);
		frm.url_failure.value = _wbGetPlanListURL(tcr_id);
		frm.action = wb_utils_disp_servlet_url;
		frm.method = "post";
		frm.submit();
	}
	return;
}
