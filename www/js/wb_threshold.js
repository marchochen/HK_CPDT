/**
  * ------------------ wizBank Configure threshold -------------------
  * 2008-02-26
  * harvey
  * ------------------------------------------------------------
  */

function wbThreshold() {
	this.upd_sys_setting = wbUpdSysSetting
	this.get_cur_act_user = wbGetCurActUser
	this.get_thd_syn_log_prep = wbGetThdSynLogPrep
	this.get_action_log = wbGetActionLog
	this.get_threshold_Log = wbGetThresholdLog
}

function wbUpdSysSetting(frm) {

	//warning threshold
	if(frm.warn_value[0].checked){
		frm.threshold_warn.value = '';
	} else {
		frm.thr_warn_value.value = wbUtilsTrimString(frm.thr_warn_value.value);
		if(!wbUtilsValidateNonZeroPositiveInteger(frm.thr_warn_value, frm.lab_threshold_warn.value)){
			return;
		}
		frm.threshold_warn.value = frm.thr_warn_value.value;
	}
	//blocking threshold
	if(frm.block_value[0].checked){
		frm.threshold_block.value = '';
	} else {
		frm.thr_block_value.value = wbUtilsTrimString(frm.thr_block_value.value);
		if(!wbUtilsValidateNonZeroPositiveInteger(frm.thr_block_value, frm.lab_threshold_block.value)){
			return;
		}
		frm.threshold_block.value = frm.thr_block_value.value;
	}
	//support email
	if(frm.sup_email[0].checked){
		frm.support_email.value = '';
	} else {
		frm.sup_email_value.value = wbUtilsTrimString(frm.sup_email_value.value);
		if(!wbUtilsValidateEmail(frm.sup_email_value, frm.lab_support_email.value)){
			return;
		}
		frm.support_email.value = frm.sup_email_value.value;
	}
	//warning threshold value can't big than blocking threshold value
	if(frm.threshold_warn.value != '' && frm.threshold_block.value != ''){
		if(Number(frm.threshold_warn.value) >= Number(frm.threshold_block.value)){
			alert(frm.lab_threshold_warn.value + wb_sys_smaller_than + frm.lab_threshold_block.value);
			frm.thr_warn_value.focus();
			return;
		}
	}

	frm.cmd.value = 'upd_sys_setting';
	frm.url_success.value = self.location.href;
	frm.url_failure.value = self.location.href;
	frm.method = "post";
	frm.action = wb_utils_servlet_url;
	frm.submit();

}

function wbGetCurActUser(){
	url = wb_utils_invoke_servlet("cmd", "get_cur_act_user", "stylesheet", 'threshold_cur_active_user.xsl');
	window.location.href = url;
}

function wbGetThdSynLogPrep(logType){
	url = wb_utils_invoke_servlet("cmd", "get_thd_syn_log", "stylesheet", 'threshold_syn_log_prep.xsl', 'log_type', logType);
	window.location.href = url;
}

function wbGetActionLog(){
	url = wb_utils_invoke_servlet("cmd", "GET_PROF", "stylesheet", 'threshold_action_log.xsl');
	window.location.href = url;
}

function wbGetThresholdLog(frm,lang){
	//if(frm.radio_log_type[1].checked){
		//frm.log_type.value=frm.radio_log_type[1].value;
	//}else{
		frm.log_type.value=frm.radio_log_type.value;
	//}
	var select_all = false;
	if(frm.radio_time[3].checked){
		frm.select_all.value='all';
		select_all = true;
	}else{
		frm.select_all.value='';
		if(frm.radio_time[0].checked){
			frm.last_days.value=7;
		}else if(frm.radio_time[1].checked){

			var val = wbUtilsTrimString(frm.days.value);
			if(val.length <= 0) {
				alert(wb_msg_usr_please_specify_value + wb_msg_usr_export_time);
				return false;
			}
			
			if(val.search(/[^0-9]/) != -1){
				alert(wb_msg_usr_enter_positive_integer + wb_msg_usr_export_time);
				frm.days.focus();
				return false;
			}
			
			if(val > 9999){
				alert(wb_msg_usr_export_time + wb_msg_usr_enter_smaller_number_1);
				frm.days.focus();
				return false;
			}
			frm.last_days.value=frm.days.value
		}else if(frm.radio_time[2].checked){
			frm.last_days.value=0;
			if(frm.start_mm.value.length==1){
				frm.start_mm.value='0'+frm.start_mm.value;
				}
			if(frm.start_dd.value.length==1){
				frm.start_dd.value='0'+frm.start_dd.value;
				}
			if(frm.end_mm.value.length==1){
				frm.end_mm.value='0'+frm.end_mm.value;
				}
			if(frm.end_dd.value.length==1){
				frm.end_dd.value='0'+frm.end_dd.value;
				}
			if(frm.start_yy.value=="" && frm.start_mm.value=="" && frm.start_dd.value==""){
				frm.sys_log_start_date.value="";
			}else{
				frm.sys_log_start_date.value=frm.start_yy.value +"-"+frm.start_mm.value+"-"+frm.start_dd.value+" 00:00:00";
			}
			if(frm.end_yy.value=="" && frm.end_mm.value=="" && frm.end_dd.value==""){
				frm.sys_log_end_date.value="";
			}else{
				frm.sys_log_end_date.value=frm.end_yy.value +"-"+frm.end_mm.value+"-"+frm.end_dd.value+" 23:59:59";
			}

			if(frm.sys_log_start_date.value=="" && frm.sys_log_end_date.value !=""){
				if(!(wbUtilsValidateDate('frm.end',eval('wb_msg_'+lang+'_end_date')))){
					return ;
				}
			}else if(frm.sys_log_start_date.value !="" && frm.sys_log_end_date.value==""){
				if(!(wbUtilsValidateDate('frm.start',eval('wb_msg_'+lang+'_start_date')))){
					return;
				}
			}else{
				if(!(wbUtilsValidateDate('frm.start',eval('wb_msg_'+lang+'_start_date')))){
					return;
				}
				if(!(wbUtilsValidateDate('frm.end',eval('wb_msg_'+lang+'_end_date')))){
					return ;
				}

			}

			if((frm.sys_log_start_date.value !="" && frm.sys_log_end_date.value !="")){
				if(!wb_utils_validate_date_compare({
					frm : 'document.' + frm.name,
					start_obj : 'start',
					end_obj : 'end'
					})) {
					return false;
				}
		}

	}
	}

	var type = $("input[name='radio_log_type']:checked").val();
	//if(type != 'Perf_Warning_Log'){
			layer.load();
			var lastdays = frm.last_days.value;
			var starttime = frm.sys_log_start_date.value;
			var endtime = frm.sys_log_end_date.value;
			if(!frm.radio_time[2].checked){
				starttime = null;
				endtime = null;
			}
			if(frm.radio_time[3].checked){
				lastdays = -1;
			}
		   	$.ajax({
		           url : "/app/admin/system/exporOperationOrLoginLog",
		           type : 'POST',
				   data: {
					   "type" : type,
					   "lastdays" : lastdays,
					   "starttime" : starttime,
					   "endtime" : endtime,
				   },
		           dataType : 'json',
		           traditional : true,
		           success : function(data) {
		        	layer.closeAll('loading');
		           	window.location.href = data.fileUri;
		           }
		      });
	/*}else{
		frm.cmd.value = 'export_thd_syn_log';
	
		frm.url_success.value = self.location.href;
		frm.url_failure.value = self.location.href;
		frm.method = "post";
		frm.action = wb_utils_servlet_url;
		frm.submit();
	}*/

}