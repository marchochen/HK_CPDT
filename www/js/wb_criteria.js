// ------------------ wizBank Criteria object ------------------- 
// Convention:
//   public functions : use "wbMote" prefix 
//   private functions: use "_wbMote" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbCriteria() {
	this.get_criteria_detail = wbCriteriaGetCriteriaDetail
	this.get_reminder_mod_lst = wbCriteriaGetReminderModuleLst
	this.mod_lst = wbCriteriaModuleLst
	this.mod_ins = wbCriteriaModuleIns	
	this.mod_lst_url = wbCriteriaModuleLstUrl	
//for old reminder criteria & completion criteria
	this.mod_ins_exec = wbCriteriaModuleInsExec
	this.mod_remove_exec = wbCriteriaModuleRemoveExec
	this.mod_del_exec = wbCriteriaModuleDelExec

//	this.upd = wbCriteriaUpdate
	this.upd_exec = wbCriteriaUpdateExec
	this.dur_upd = wbCriteriaDurationUpdate
	this.dur_upd_exec = wbCriteriaDurationUpdateExec

	this.mod_status_upd = wbCriteriaModuleStatusUpdate
	this.mod_status_upd_exec = wbCriteriaModuleStatusUpdateExec	
	
//for new completion criteria
	this.mod_manage_exec = wbCriteriaModuleManageExec
	this.mod_manage_exec_url = wbCriteriaModuleManageExecURL
	this.mod_manage_upd_exec = wbCriteriaModuleManageUpdateExec
	this.mod_manage_remove_exec = wbCriteriaModuleManageRemoveExec
//for instructor
	this.instr_upd_exec=wbInstrCriteriaUpdateExec;
}

function wbCriteriaModuleManageRemoveExec(frm,itm_id,ccr_id,ccr_type,del_cmr_id,lang){
if(confirm(eval('wb_msg_'+lang+'_confirm_remove_itm'))){	
		frm.cmr_lst.value = del_cmr_id	
		frm.type.value = ccr_type;
		frm.cmd.value = 'soft_del_criteria_module'
		frm.module.value = 'course.CourseCriteriaModule'
		frm.url_success.value = wbCriteriaModuleLstUrl(itm_id,ccr_id,'',ccr_type)
		frm.url_failure.value = wbCriteriaModuleLstUrl(itm_id,ccr_id,'',ccr_type)
		frm.itm_id.value = itm_id
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.submit()		
	}	
}

function wbCriteriaModuleManageExec(itm_id,ccr_id,ccr_type,del_cmr_id){
	url = wbCriteriaModuleManageExecURL(itm_id,ccr_id,ccr_type,del_cmr_id)
	window.location.href = url
}

function wbCriteriaModuleManageExecURL(itm_id,ccr_id,ccr_type,del_cmr_id){
//http://man.wizq.net/servlet/Dispatcher?env=wizb&cmd=get_criteria_lst&module=course.CourseCriteriaModule&ccr_id=4&type=completion&itm_id=139&stylesheet=crit_mod_status_upd_lst.xsl&ins_mod_id=21
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'del_cmr_id', del_cmr_id,
		'stylesheet','crit_manage_mod_status.xsl'
	)
	return url
}

function wbCriteriaGetCriteriaDetail(itm_id,ccr_id,ccr_type){
	if (ccr_id == null) { ccr_id = '';}
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet','crit_detail_upd.xsl'
	)
	window.location.href = url;
}

/* public functions */
function wbCriteriaGetReminderModuleLst(itm_id,ccr_id,target_window,ccr_type){
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',status='				+ 'yes';
	if (ccr_id == null) { ccr_id = '';}
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet','crit_lst.xsl'
	)
	if (target_window == null || target_window == ''){
		window.location.href = url;
	}else{
		wbUtilsOpenWin(url,'course_criteria', false, str_feature);
	}	
}

function wbCriteriaModuleLst(itm_id,ccr_id,target_window,ccr_type){
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',status='				+ 'yes';
			
	if (ccr_id == null) { ccr_id = '';}
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet','crit_mod_lst.xsl'
	)
	if (target_window == null || target_window == ''){
		window.location.href = url;
	}else{
		wbUtilsOpenWin(url,'course_criteria', false, str_feature);
	}	
}

function wbCriteriaModuleLstUrl(itm_id,ccr_id,target_window,ccr_type){
	if(ccr_type == 'completion' || ccr_type == 'COMPLETION'){
		var stylesheet = 'crit_mod_lst.xsl'
	}else{
		var stylesheet = 'crit_lst.xsl'
	}
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet',stylesheet
	)
	return url;	
}

function wbCriteriaDetailUrl(itm_id,ccr_id,target_window,ccr_type){
	
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet','crit_detail_upd.xsl'
	)
	return url;	
}
/*
function wbCriteriaUpdate(itm_id,ccr_id,ccr_type){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet','crit_detail_upd.xsl'
	)
	window.location.href = url;
}
*/
function wbCriteriaUpdateExec(frm,itm_id,ccr_id,lang,ccr_type){
	
	if (_wbCriteriaValidateUpdDetail(frm,lang)){
		frm.cmd.value = 'upd_criteria'
		frm.module.value = 'course.CourseCriteriaModule'
		frm.ccr_id.value = ccr_id
		frm.type.value = ccr_type
		frm.itm_id.value = itm_id
		frm.url_success.value = wb_utils_invoke_ae_servlet('env','wizb','cmd','ae_get_content_info','itm_id',itm_id,'stylesheet', 'ae_get_content_info.xsl');
		frm.must_meet_all_cond.value = 'false'
		frm.attendance_rate.value = frm.att_val.value
		frm.pass_score.value = frm.grade_val.value
		if (frm.pass_score.value>0){
			frm.must_pass.value = 'true'	
		}else{
			frm.must_pass.value = 'false'	
		}
		frm.action = wb_utils_disp_servlet_url;
		frm.method = 'get'
		frm.submit()
	}		
}

function wbCriteriaDurationUpdate(itm_id,ccr_id,ccr_type){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet','crit_dur_upd.xsl'
	)
	window.location.href = url;
}

function wbCriteriaDurationUpdateExec(frm,itm_id,ccr_id,lang,ccr_type){
	
	if (_wbCriteriaValidateDuration(frm,lang)){
//		if (frm.dur_sel[0].checked) {frm.duration.value = 0;}
		frm.duration.value = frm.dur_val.value;
		url = wb_utils_invoke_disp_servlet(
			'cmd','upd_criteria',
			'module','course.CourseCriteriaModule',
			'duration',frm.duration.value,
			'ccr_id',ccr_id,
			'type',ccr_type,
			'itm_id',itm_id,
			'url_success',wbCriteriaModuleLstUrl(itm_id,ccr_id,'',ccr_type)
		)		
		window.location.href = url;
	}		
}


function wbCriteriaModuleIns(course_id,ccr_type){
	
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '234'
			+ ',height=' 				+ '326'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'no';
			
	url = wb_utils_invoke_servlet(
		'cmd','get_cos',
		'course_id',course_id,
		'type',ccr_type,
		'stylesheet','gen_pick_cos_module.xsl',
		'url_failure','../htm/close_window.htm'
	)
	wbUtilsOpenWin(url, 'course_module_picker', false, str_feature);
}

function wbCriteriaModuleInsExec(frm,itm_id,ccr_id,mod_id,mod_type,lang,ccr_type,isFirst){
	
	var status
	status = ''

	if (_wbCriteriaValidateModule(frm,mod_id,lang)){
		if (mod_type == 'GAG' || mod_type == 'LCT' || mod_type == 'TUT' || mod_type == 'RDG' || mod_type == 'EXP' || mod_type == 'VOD'){status = 'IFCP';}
		else if (mod_type == 'TST' || mod_type == 'DXT' || mod_type == 'SXT'){status = 'P';}
		else if (mod_type == 'EXC'){status = 'IFCP';}
		else if (mod_type == 'ASS'){status = 'CP';}
		else if (mod_type == 'EAS'){status = 'P';}
		else if (mod_type == 'VCR' || mod_type == 'FOR' || mod_type == 'CHT' || mod_type == 'FAQ'){status = 'IFCP';}
		else if (mod_type == 'VST' || mod_type == 'EXM' || mod_type == 'ORI' || mod_type == 'REF' || mod_type == 'GLO'){status = 'IFCP';}
		else if (mod_type == 'AICC_AU'){status = 'P';}
		else if (mod_type == 'SCO'){status = 'P';}
		else if (mod_type == 'NETG_COK'){status = '';}
		else if (mod_type == 'SVY' || mod_type == 'TNA' || mod_type == 'EVN'){status = 'CP';}
		else {status = 'IFCP';}
		frm.type.value = ccr_type;
		frm.cmd.value = 'pick_module'
		frm.module.value = 'course.CourseCriteriaModule'
		frm.itm_id.value = itm_id
		frm.mod_id.value = mod_id
		frm.ccr_id.value = ccr_id
		frm.status.value = status
		if(isFirst == 'true'){
			frm.rate.value = "100"
		}else{
			frm.rate.value = "0"
		}
		 if (mod_type == 'NETG_COK'){
		 	frm.is_contri_by_score.value = "true"
		 }else{
			frm.is_contri_by_score.value = "false"
		}
		
			frm.url_success.value = wbCriteriaModuleLstUrl(itm_id,ccr_id,'',ccr_type)
			frm.url_failure.value = wbCriteriaModuleLstUrl(itm_id,ccr_id,'',ccr_type)
		
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'get'
		frm.submit()
	}

}

function wbCriteriaModuleRemoveExec(frm,lang,itm_id,ccr_type){
	frm.cmr_lst.value = _wbCriteriaGetDelModuleLst(frm)
	if (frm.cmr_lst.value == ''){
		alert(eval('wb_msg_' + lang + '_sel_remove_itm'))
	}else if(confirm(eval('wb_msg_'+lang+'_confirm_remove_itm'))){		
		frm.type.value = ccr_type;
		frm.cmd.value = 'soft_del_criteria_module'
		frm.module.value = 'course.CourseCriteriaModule'
		frm.url_success.value = self.location.href
		frm.url_failure.value = self.location.href
		frm.itm_id.value = itm_id
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.submit()		
	}	
}

function wbCriteriaModuleDelExec(frm,lang,itm_id,ccr_type){
	frm.cmr_lst.value = _wbCriteriaGetDelModuleLst(frm)
	if (frm.cmr_lst.value == ''){
		alert(eval('wb_msg_' + lang + '_sel_del_itm'))
	}else if(confirm(eval('wb_msg_'+lang+'_confirm_del_itm'))){		
		frm.type.value = ccr_type;
		frm.cmd.value = 'del_criteria_module'
		frm.module.value = 'course.CourseCriteriaModule'
		frm.url_success.value = self.location.href
		frm.url_failure.value = self.location.href
		frm.itm_id.value = itm_id
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.submit()		
	}	
}

function wbCriteriaModuleStatusUpdate(itm_id,ccr_id,ccr_type){
	
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_criteria_lst',
		'module','course.CourseCriteriaModule',
		'ccr_id',ccr_id,
		'type',ccr_type,
		'itm_id',itm_id,
		'stylesheet','crit_mod_status_upd_lst.xsl'
	)
	window.location.href = url;
}

function wbCriteriaModuleStatusUpdateExec(frm,itm_id,ccr_id,ccr_type){
	frm.cmr_lst.value = _wbCriteriaGetUpdCModuleLst(frm)
	frm.status_lst.value = _wbCriteriaGetUpdModuleStatusLst(frm)
	frm.cmd.value = 'upd_multi_status'
	frm.itm_id.value = itm_id
	frm.type.value = ccr_type;
	frm.module.value = 'course.CourseCriteriaModule'
	frm.url_success.value = wbCriteriaModuleLstUrl(itm_id,ccr_id,'',ccr_type)
	frm.url_failure.value = self.location.href
	frm.action = wb_utils_disp_servlet_url
	frm.method = 'post'	
	frm.submit()
}

function wbCriteriaModuleManageUpdateExec(frm,itm_id,ccr_id,ccr_type,lang){
	if(frm.contribute_total){
		frm.contribute_total.value = wbUtilsTrimString(frm.contribute_total.value);
	if(frm.contribute_total.value != 100){
		alert(eval('wb_msg_'+lang+'_criteria_not_valid'))
	return;
	}
	}
	_wbCriteriaGetValue(frm)
	// = _wbCriteriaGetUpdModuleStatusLst(frm)
	//alert(frm.status_lst.value)
	frm.cmd.value = 'upd_multi_status'
	frm.itm_id.value = itm_id
	frm.type.value = ccr_type;
	frm.module.value = 'course.CourseCriteriaModule'
	frm.url_success.value = wbCriteriaModuleLstUrl(itm_id,ccr_id,'',ccr_type)
	frm.url_failure.value = self.location.href
	frm.action = wb_utils_disp_servlet_url
	frm.method = 'post'	
	frm.submit()
}
/* private functions */
function _wbCriteriaGetValue(frm){
	var i, n, j,ele, str, tmp1, tmp2
	var cmr_id_lst = ""
	var contri_rate_lst = ""
	var is_contri_by_score_lst = ""
	var sts_lst = ""
	n = frm.elements.length;
	for (i=0; i<n; i++){
		ele = frm.elements[i]
		if(ele.name.indexOf('cmr_id_value')!= -1){
			var cmr_id = ele.value
			//get cmr_id_lst
			cmr_id_lst += cmr_id + '~'
			//get contri_rate_lst
			var rate_ele = eval('frm.contribute_rate_' + cmr_id)			
			contri_rate_lst += rate_ele.value + '~'			
			//get is_contri_by_score_lst		   
			var is_contri_by_score_ele = eval('frm.is_contri_by_score_' + cmr_id)
			is_contri_by_score_lst += is_contri_by_score_ele.value  + '~'
			//get sts_lst
			var cmr_rdo = eval('frm.cmr_rdo_' + cmr_id)
			if(cmr_rdo.length){
				for(j=0;j<cmr_rdo.length;j++){
					if(cmr_rdo[j].checked == true){
						sts_lst+= cmr_rdo[j].value  + '~'
						break;
					}
				}		
			}else{
				sts_lst += cmr_rdo.value + '~'
			}	

		}  
		
	}	
	if (cmr_id_lst != "") {
		cmr_id_lst = cmr_id_lst.substring(0, cmr_id_lst.length-1);
		contri_rate_lst = contri_rate_lst.substring(0, contri_rate_lst.length-1);
		is_contri_by_score_lst = is_contri_by_score_lst.substring(0, is_contri_by_score_lst.length-1);
	}
	frm.status_lst.value = sts_lst
	frm.cmr_lst.value = cmr_id_lst
	frm.contri_rate_lst.value = contri_rate_lst
	frm.is_contri_by_score_lst.value = is_contri_by_score_lst
	/*
	alert(frm.status_lst.value)
	alert(frm.cmr_lst.value)
	alert(frm.contri_rate_lst.value)
	alert(frm.is_contri_by_score_lst.value)		
	*/	

}


function _wbCriteriaGetUpdModuleStatusLst(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i=0; i<n; i++){
		ele = frm.elements[i]
		//alert(ele.type)
		if (ele.type == "select-one"){
			if (ele.value != "")				
				str = str + ele.value + "~"			
		}
	}	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;	
}

function _wbCriteriaGetUpdCModuleLst(frm){
	var i, n, ele, str, tmp1, tmp2
	str = ""
	tmp1 = ""
	tmp2 = ""
	n = frm.elements.length;
	for (i=0; i<n; i++){
		ele = frm.elements[i]
		ele2 = frm.elements[i+1]
		if (ele.type == "select-one"){
			if (ele2.value != ""){			
				str = str + ele2.value + "~"			
				tmp1 += "0" + "~"
				tmp2 += "false" + "~"
			}
		}
	}	
	if (str != "") {
		str = str.substring(0, str.length-1);
		tmp1 = tmp1.substring(0, tmp1.length-1);
		tmp2 = tmp2.substring(0, tmp2.length-1);
	}
	frm.contri_rate_lst.value = tmp1;
	frm.is_contri_by_score_lst.value = tmp2;
	return str;	
}

function _wbCriteriaGetDelModuleLst(frm) {
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name!='sel_all_checkbox') {
			if (ele.value !="")
				str = str + ele.value + "~"
		}
	}
	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;	
}

function _wbCriteriaValidateModule(frm,mod_id,lang){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "hidden" && ele.name == 'app_mod_id') {			
			if (ele.value == mod_id)
				str = str + ele.value + "~"
		}
	}	
	if (str != "") {
		alert(eval('wb_msg_' + lang + '_crit_mod_added'))
		return false;
	}else{ return true;}
}

/* validate functions */
function _wbCriteriaValidateUpdDetail(frm,lang){
/*	for(i=0;i<frm.upd_method.length;i++){
		if(frm.upd_method[i].checked == true){
			index = i
		}
	}
	if (index==null){
		alert(eval('wb_msg_' + lang + '_crit_select_upd_method'))
	}
	*/
	// todo	
	if (frm.att_sel !=null){
		if (frm.att_sel[0].checked == true){
			frm.att_val.value = wbUtilsTrimString(frm.att_val.value);
			if (!gen_validate_empty_field(frm.att_val, eval('wb_msg_'+ lang + '_crit_attendance'), lang)){
				return false;
			}
			if (frm.att_val.value == '0'){
				alert(eval('wb_msg_' + lang + '_crit_valid_attendance'))
				return false;
			}
			if (!gen_validate_positive_integer(frm.att_val, eval('wb_msg_' + lang + '_crit_attendance'), lang)){
				return false;
			}
			if (!gen_validate_pencentage(frm.att_val, eval('wb_msg_' + lang + '_crit_attendance'), lang)){
				return false;
			}
		}else{
			frm.att_val.value = '0';		
		}
	}
	if (frm.grade_sel !=null){
		if (frm.grade_sel[1].checked == true){
			frm.grade_val.value = wbUtilsTrimString(frm.grade_val.value);
			if (!gen_validate_empty_field(frm.grade_val, eval('wb_msg_'+ lang + '_crit_grade'), lang)){
				return false;
			}
			if (frm.grade_val.value == '0'){
				alert(eval('wb_msg_' + lang + '_crit_valid_grade'))
				return false;
			}
			if (!gen_validate_positive_integer(frm.grade_val, eval('wb_msg_' + lang + '_crit_grade'), lang)){
				return false;
			}
			if (!gen_validate_pencentage(frm.grade_val, eval('wb_msg_' + lang + '_crit_grade'), lang)){
				return false;
			}
		}else{
			frm.grade_val.value = '0';	
		}
	}
	
	return true;
}

function _wbCriteriaValidateDuration(frm,lang){
		
//	if (frm.dur_sel[1].checked == true){
		frm.dur_val.value = wbUtilsTrimString(frm.dur_val.value);
		if (!gen_validate_empty_field(frm.dur_val, eval('wb_msg_'+ lang + '_duration'), lang)){
			return false;
		}
		if (!gen_validate_positive_integer(frm.dur_val, eval('wb_msg_' + lang + '_duration'), lang)){
			return false;
		}
		if (frm.dur_val.value == '0'){
			alert(eval('wb_msg_' + lang + '_crit_valid_dur'))
			return false;
		}
//	}
	return true;
}

function _wbItemGetItemDetail(itm_id){
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm','itm_id',itm_id,'tvw_id','DETAIL_VIEW','stylesheet','itm_details.xsl','url_failure','','tnd_id','','prev_version_ind',false,'show_run_ind',false);
	return url;
}
//for instructor
function wbInstrCriteriaUpdateExec(frm,itm_id,ccr_id,lang,ccr_type){
	if (_wbCriteriaValidateUpdDetail(frm,lang)){
		frm.cmd.value = 'upd_criteria'
		frm.module.value = 'course.CourseCriteriaModule'
		frm.ccr_id.value = ccr_id
		frm.type.value = ccr_type
		frm.itm_id.value = itm_id
		frm.url_success.value = wb_utils_invoke_ae_servlet('env','wizb','cmd','ae_get_content_info','itm_id',itm_id,'stylesheet', 'ae_get_instr_content_info.xsl');
		frm.must_meet_all_cond.value = 'false'
		frm.attendance_rate.value = frm.att_val.value
		frm.pass_score.value = frm.grade_val.value
		if (frm.pass_score.value>0){
			frm.must_pass.value = 'true'	
		}else{
			frm.must_pass.value = 'false'	
		}
		frm.action = wb_utils_disp_servlet_url;
		frm.method = 'get'
		frm.submit()
	}		
}
