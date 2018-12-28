// ------------------ WizBank Item Object ---------------------
// Convention:
//   public functions : use "wbItem" prefix 
//   private functions: use "_wbItem" prefix 
// ------------------------------------------------------------ 
/* constructor */
function wbItem(){
	this.exam = new wbItemExam;
	
	//insert item caching	
	this.ins_itm_caching = wbItemInsCaching;
	this.asn_itm_caching = wbItemAssignCaching;

	//add ItemType
	this.select_add_item_type_prep = wbItemSelectAddItemTypePrep;
	this.select_add_item_type_exec = wbItemSelectAddItemTypeExec;
	this.cancel_add_itm_type_exec = wbItemSelectCancelAddItmTypeExec;
	
	// add to skip the Learning Solution types selection page when the User Role is Learner
	this.skip_add_item_type_exec = wbItemSkipAddItemTypeExec;

	//Item Ins/Upd
	this.ins_simple_item_exec = wbItemSimpleInsItemExec;
	this.ins_item_exec = wbItemInsItemExec;
	this.upd_item_prep = wbItemUpdPrep;
	this.upd_item_exec = wbItemUpdExec;
	this.del_item_exec = wbItemDelExec;
	this.del_item_lst_exec = wbItemDelItemLstExec;
	this.upd_item_workflow_prep = wbItemUpdWorkflowPrep;
	this.upd_item_workflow_exec = wbItemUpdWorkflowExec;
	this.upd_item_prep_batch = wbItemUpdPrepBatch;
	this.upd_item_batch_exec = wbItemUpdBatchExec;
	//Item Detail
	this.get_item_detail = wbItemGetItemDetail;
	this.get_res_detail = wbItemGetResourceDetailWin;
	this.get_item_run_detail = wbItemGetItemRunDetail;
	this.get_item_lrn_detail = wbItemGetItemLrnDetail;
	
	//Item List
	this.get_item_list = wbItemGetItemList;
	this.get_item_run_list = wbItemGetItemRunList;
	this.sort_item_list = wbItemSortItemList;

	//New Version
	this.new_version_prep = wbItemNewItemVersionPrep;
	this.new_version_exec = wbItemNewItemVersionExec;

	//Runs
	this.ins_item_run_prep = wbItemInsItemRunPrep;
	this.ins_item_run_exec = wbItemInsItemRunExec;

	//Cancel Runs
	this.cancel_item_prep = wbItemCancelItemPrep;
	this.cancel_item_exec = wbItemCancelItemExec;

	//Search
	this.search_item_prep = wbItemSearchItemPrep;
	this.search_item_exec = wbItemSearchItemExec;
	this.simple_search_item_exec = wbItemSearchSimpleExec;

	//Class Search
	this.search_run_prep = wbItemSearchRunPrep;
	this.search_run_exec = wbItemSearchRunExec;
	this.simple_search_run_exec = wbItemSearchRunSimpleExec;
	this.search_item_add_run_prep = wbItemSearchItemAddRunPrep;
	this.search_item_add_run_exec = wbItemSearchItemAddRunExec;

	// search from homepage
	this.search_cos_main_has_run = wbItemSearchCosMainHasRun;
	this.search_cos_main_no_run = wbItemSearchCosMainNoRun;

	this.search_enr_list_has_run = wbItemSearchEnrListHasRun;

	this.search_coming_list_has_run = wbItemSearchComingListHasRun;
	this.search_coming_list_no_run = wbItemSearchComingListNoRun;

	//Update item Status
	this.upd_itm_status = wbItemUpdItemStatus;
	this.upd_itm_status_on = wbItemUpdItemStatusOn;

	//Auto Enrol WZBCOURSE
	this.auto_enrol_cos = wbItemAutoEnrolCos;

	//Get Orphan item List
	this.get_orphan_item = wbItemOrphanItemLst;

	//Session
	this.session = new wbSession;

	//for online content
	this.ae_upd_itm = wbItemOnlineContentUpd;
	this.ae_get_content_sched = wbItemContentSched;
	this.ae_get_setting_info = wbItemContentInfo;
	this.ae_get_online_content_info = wbItemContentInfo;
	this.ae_get_online_performance_info = wbItemPerformanceInfo;
	this.ae_get_online_content_info_url = wbItemContentInfoURL;

	//for JI
	this.itm_ji_upd = wbItemJIUpd;
	this.itm_ji_msg = wbItemJIMsg;

	//  approval  action
	this.make_approval_action_exec = wbItemMakeApprovalActionExec;
	this.itm_view_popup = wbItemViewPopup;

	//for instructor
	this.get_itm_instr_view = wbItemGetItemInstrView;
	this.get_itm_content_instr_view = wbItemGetItemContentInstrView;
	this.get_itm_performance_instr_view = wbItemGetItemPerformanceInstrView;
	this.my_teaching_cos = wbItemMyTeachingCourse;
	
	//for instructor
	this.ae_get_instr_online_content_info=wbInstrItemContentInfo;

	//for CourseLesson
	this.ae_get_course_lesson = wbItemGetCourseLessonInfo;
	this.ae_upd_course_lesson = wbItemUpdCourseLesson;
	this.ae_get_run_lesson = wbItemGetRunLessonInfo;
	this.ae_upd_run_lesson = wbItemUpdRunLesson;

	//item_cost
	this.get_item_cost = wbItemCostInfo;
	this.commit_change = wbCommitItemCostChange;
	this.assign_instr = wbAssignInstr;
	this.remove_instr = wbRemoveInstr;
	this.validNum = validPageNum;
	
	//for module Prerequisite
	this.upd_mod_pre=UpdatePrerequisite;

	this.get_mod_pre = wbItemGetModPre;
	
	this.get_target_rule = wbItemGetTargetRule;
	this.start_target_rule_prev = wbItemStartTargetRulePrev;
	this.set_target_rule_prev_url = wbItemSetTargetRulePrevUrl;
	this.get_preview_target_url = getPreviewTargetRuleUrl;
	this.set_target_rule_exec = wbItemSetTargetRuleExec;
	this.target_rule_list_preview = wbItemTargetRuleLstPreview;
	this.target_lrn_export = wbItemTargetLrnExport;
	this.del_target_rule = wbItemDelTargetRule;
	this.sort_target_user = wbItemSortTargetUser;
	this.change_tarenrol_rule = wbItemChangeTarEnrolRule;
	this.get_item_publish = wbGetItemPublish;
	this.get_item_auto_enrol = wbGetItemAutoEnrol;
	this.upd_item_target_ref = wbUpdItemTargetLrnRef;
	
	this.get_course_list = wbGetCourseList;
	this.set_condition_prev = wbSetConditionPrev;
	this.set_condition_exec = wbSetConditionExec;
	this.del_condition = wbDelCondition;
	this.set_criteria = wbSetCriteria;
	this.share_itm = wbItemShare;
	
	this.get_itm_comment_lst = wbGetItmCommentLst;
	this.set_itm_cpd_gourp_hour = wbSetItmCPDGourpHours;
	
	this.itm_evaluation_report = wbItemEvaluationReport;
	this.itm_cpd_hours_award_record = wbItemCpdHoursAwardRecord;
}

//SubClass wbItemExam
function wbItemExam() {
	this.sort_exam_list = wbItemExamSortExamList;
	this.get_exam_online_list = wbItemExamGetExamOnlineList;
	this.sent_msg_to_exam_learner_prep = wbItemExamSentMsgPrep;
	this.sent_msg_to_exam_learner = wbItemExamSentMsg;
	this.terminate_exam = wbItemExamTerminate;
	this.pause_exam = wbItemExamPause;
	this.release_pause_exam = wbItemExamReleasePause;
}

/* 学员课程评论列表 */
function wbGetItmCommentLst(itm_id) {
	applet_width = 1100;
		applet_height = 772;
		str_feature = 'toolbar=' + 'no' + ',width=' + applet_width + ',height=' + applet_height + ',scrollbars=' + 'auto' + ',resizable=' + 'NO' + ',screenX=' + '0' + ',screenY=' + '10' + ',left=' + '0' + ',top=' + '10';
		url = wb_utils_controller_base +"course/admin/mgt_comments/"+itm_id;
		//wbUtilsOpenWin(url, 'course_Comment', false,str_feature);
	
	self.location.href=url;
}

/* 设置课程cpt时数*/
function wbSetItmCPDGourpHours(itm_id) {
	applet_width = 1100;
		applet_height = 772;
		str_feature = 'toolbar=' + 'no' + ',width=' + applet_width + ',height=' + applet_height + ',scrollbars=' + 'auto' + ',resizable=' + 'NO' + ',screenX=' + '0' + ',screenY=' + '10' + ',left=' + '0' + ',top=' + '10';
		url = wb_utils_controller_base +"course/admin/mgt_cpd_set_hours/"+itm_id;
		//wbUtilsOpenWin(url, 'course_Comment', false,str_feature);
	
	self.location.href=url;
}
/* cpt获得时数*/
function wbItemCpdHoursAwardRecord(itm_id){
	url = wb_utils_controller_base +"course/admin/mgt_cpd_hours_award/"+itm_id;
    self.location.href=url;
}

function wbItemCostInfo(itm_id){
    _wbGenItemCostInfo(itm_id,"ae_item_fee_mgt.xsl","");
}

function wbCommitItemCostChange(frm,itm_id, itm_type){
	var flag = validateFrm(frm);
	var url_success;
	if(itm_type == 'CLASSROOM') {
		url_success = wbReturnItemGetItemRunDetailUrl(itm_id);
	}else{
		url_success = wbReturnItemGetItemDetailUrl(itm_id);
	}
	if(flag == 0){
		alert(eval("wb_msg_specify_decimal_2"));
		return ;
	}
    frm.itm_id.value = itm_id;
	frm.cmd.value = "commit_item_cost";
	frm.url_success.value = url_success;
	frm.url_failure.value = window.location.href;
	frm.action = wb_utils_ae_servlet_url;
	frm.method = "post";
	frm.submit();

}

function validateFrm(frm){
	var name;
	var num;
	var typeValue;
	for(var i=1;i<=frm.cost_type_num.value;i++){
	   typeValue = eval("frm.type_value"+i+".value");
       name = eval("frm.budget"+typeValue);
	   num = name.value;
	   if(validNum(num)==0){
		   name.focus();
		   return 0;
	   }
	   name = eval("frm.actual"+typeValue);
	   num = name.value;
	   if(validNum(num)==0){
		   name.focus();
		   return 0;
	   }
	}
	return 1;
}

function validNum(num){
	var str = "0123456789.";
	var cha;
    var n=num.length;
    var status = 0;
	if (num.charAt(n-1)=='.'){
         return 0;
    }
	for (j=0;j<n;j++){
		  cha = num.charAt(j);
		  if(str.indexOf(cha)==-1){
			  return 0;
		  }
          if (num.charAt(j)=='.'){
             status++;
			 if(n-1-j>2){
				 return 0;
			 }
           }
	}
	if(status > 1){
			return 0;
	}
	return 1;
}

function validPageNum(num){
	var str = "0123456789.";
	var cha;
    var n=num.length;
    var status = 0;
	for (j=0;j<n;j++){
		  cha = num.charAt(j);
		  if(str.indexOf(cha)==-1){
			  return 0;
		  }
	}
	if(status > 1){
			return 0;
	}
	return 1;
}

function wbSession(){
	this.get_session_list = wbSessionGetSessionList;
	this.ins_session_prep = wbSessionInsSessionPrep
}

// ------------------------------------------------------------ 
function wbItemViewPopup(itm_id){
	wbItemViewPopup(itm_id, 0)
}

function wbItemViewPopup(itm_id, usr_ent_id) {
	var url;
	if (!usr_ent_id || usr_ent_id==0) {
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', 'LRN_VIEW', 'stylesheet', 'itm_view_popup.xsl', 'show_run_ind', 'true', 'show_session_ind', 'true')
	}
	else {
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', 'LRN_VIEW', 'stylesheet', 'itm_view_popup.xsl', 'show_run_ind', 'true', 'show_session_ind', 'true', 'usr_ent_id', usr_ent_id)
	}
	var str_feature = ',width=' + '720' + ',height=' + '500' + ',scrollbars=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10' + ',status=' + 'yes';
	wbUtilsOpenWin(url, "itmWin", false, str_feature);
}

function wbItemMyTeachingCourse() {
	var url = wb_utils_invoke_ae_servlet('cmd', 'my_responsible_itm', 'stylesheet', 'tch_cos_list.xsl');
	self.location.href = url;
}

function wbItemGetItemInstrView(itm_id) {
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', 'LRN_VIEW', 'show_respon_run_ind', 'true', 'stylesheet', 'itm_instr_details.xsl');
	self.location.href = url;
}

function wbItemGetItemContentInstrView(itm_id) {
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_instr_content_info.xsl');
	self.location.href = url;
}

function wbItemGetItemPerformanceInstrView(itm_id) {
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_instr_performance_info.xsl');
	self.location.href = url;
}
/**
 * training_type(需要创建的课程类型)
 * training_plan
 * tcr_id
 * plan_id
 * tpn_update_timestamp
 * plan_type
 */
function wbItemSelectAddItemTypePrep(training_type, itm_type, flag){
	if(flag == undefined) flag = false;
	itm_dummy_type = itm_type + "|-|" + training_type;
	itm_integrated_ind = 'false';
	if(training_type=='REF'){
	}else if(training_type=='INTEGRATED'){
		//如果是创建公开课类型
		itm_integrated_ind = true;
		itm_dummy_type = training_type;
		
	}
	
	if(flag) {
		var itm_tcr_id = document.getElementById("sel_tcr_id").value;
		url= wb_utils_invoke_ae_servlet('ity_id', itm_type, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_add.xsl', 'cmd', 'ae_form_ins_itm', 'itm_dummy_type', itm_dummy_type,'training_type',training_type,'itm_integrated_ind',itm_integrated_ind, 'itm_tcr_id', itm_tcr_id);
	} else {
		url= wb_utils_invoke_ae_servlet('ity_id', itm_type, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_add.xsl', 'cmd', 'ae_form_ins_itm', 'itm_dummy_type', itm_dummy_type,'training_type',training_type,'itm_integrated_ind',itm_integrated_ind);
	}
	
	
	window.location.href = url;
}

function _wbItemSelectAddItemTypeURL(training_type,training_plan,tcr_id,plan_id,tpn_update_timestamp,plan_type){
	var url = '';
	if(training_plan =='true' || training_plan == true){
		url=wb_utils_invoke_ae_servlet('cmd', 'ae_get_all_ity_form', 'stylesheet', 'training_plan_impl.xsl','training_plan',training_plan,'tcr_id',tcr_id,'plan_id',plan_id,'tpn_update_timestamp',tpn_update_timestamp,'entrance',plan_type);
	}else{
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_all_ity_form', 'stylesheet', 'ls_add_form_prep.xsl','training_type',training_type);
	}
	return url;
}

function wbItemSelectAddItemTypeExec(frm, lang){
	var i;
	var index = null;
	var sort=null;
	if(frm.ity_id.length == null){
		index = 0;
		var ity_id=frm.ity_id.id;
		
		sort=ity_id.substring(7,ity_id.length);	
	}else{
		for (i = 0; i < frm.ity_id.length; i++){
			if(frm.ity_id[i].checked == true){
				index = i;
				var ity_id=frm.ity_id[i].id;
				
			  sort=ity_id.substring(7,ity_id.length);
			  alert(sort);
			}
		}
	}
	if(index != null){
		//frm.itm_dummy_type.value = eval('document.getElementById("dummy_type_'+(sort)+'").value' );
		frm.itm_dummy_type.value=document.getElementById('dummy_type_'+sort).getAttribute('value');
		if(frm.itm_dummy_type.value === 'INTEGRATED') {
			frm.itm_integrated_ind.value = 'true';
		}
		if(_wbValidateSelectAddItemTypeExec(frm, lang)){
			frm.cmd.value = 'ae_form_ins_itm';
			frm.action = wb_utils_ae_servlet_url;
			frm.method = 'get';
			frm.tvw_id.value = 'DETAIL_VIEW';
			frm.stylesheet.value = 'itm_add.xsl';
			frm.submit();
		}
	}else{
		alert(eval('wb_msg_' + lang + '_select_add_item_type'));
	}
}

function wbItemSelectCancelAddItmTypeExec (training_type){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'all_ind', 'true', 'training_type', training_type, 'tcr_id_lst', -1);
	window.location.href = url;
}

function _wbValidateSelectAddItemTypeExec(frm, lang){
	if(frm.wrk_tpl_id.length == null){
		index = 0;
	}else{
		for (i = 0; i < frm.wrk_tpl_id.length; i++){
			if(frm.wrk_tpl_id[i].checked){
				index = i;
			}
		}
	}

	tpl_id = frm.wrk_tpl_id[index].value;

	if(tpl_id == null || tpl_id == ''){
		frm.itm_app_approval_type.value = '';
	}else if(frm.wrk_tpl_id[index].id == 'wrk_tpl_id_' + tpl_id + '_no_approval'){
		frm.itm_app_approval_type.value = '';
	}else if(frm.wrk_tpl_id[index].id == 'wrk_tpl_id_' + tpl_id + '_approval'){
		chk_supervise = eval('frm.chk_' + tpl_id + '_supervise');

		chk_tadm = eval('frm.chk_' + tpl_id + '_tadm');

		if(!chk_supervise.checked && !chk_tadm.checked){
			alert(eval('wb_msg_' + lang + '_select_app_approval_type'));
			return false;
		}else if(chk_supervise.checked){
			rdo_ds = eval('frm.rdo_' + tpl_id + '[0]');

			rdo_ds_gs = eval('frm.rdo_' + tpl_id + '[1]');
			if(!rdo_ds.checked && !rdo_ds_gs.checked){
				alert(eval('wb_msg_' + lang + '_select_app_approval_type'));
				return false;
			}else if(rdo_ds.checked){
				frm.itm_app_approval_type.value = 'DIRECT_SUPERVISE_';
			}else if(rdo_ds_gs.checked){
				frm.itm_app_approval_type.value = 'DIRECT_SUPERVISE_SUPERVISE_';
			}
		}
		if(chk_tadm.checked){
			frm.itm_app_approval_type.value = frm.itm_app_approval_type.value + 'TADM';
		}else{
			frm.itm_app_approval_type.value = frm.itm_app_approval_type.value.substring(0, frm.itm_app_approval_type.value.length - 1);
		}
	}
	return true;
}

function wbItemSkipAddItemTypeExec(ity_id, lang){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_form_ins_itm', 'ity_id', ity_id, 'tvw_id', 'LRN_VIEW', 'stylesheet', 'itm_add.xsl');
	window.location.href = url;
}

function wbItemInsItemExec(frm, itm_type){
	if(ValidateFrm(frm)){
		feed_param_value(frm);
		GenerateXML(frm);
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		frm.cmd.value = "ae_ins_itm";
		//frm.stylesheet.value = "wb_createlrnsol_msg_box.xsl"
		frm.url_failure.value = self.location.href;
		if(frm.training_plan.value =='true'&&frm.tpn_create_run_ind.value=='true'){
				var plan_id;
				var plan_tcr_id;
				if(frm.plan_id) {
					plan_id = frm.plan_id.value;
				}
				if(frm.plan_tcr_id){
					plan_tcr_id = frm.plan_tcr_id.value;
				}
				var url_failure = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan','tcr_id', plan_tcr_id,'tpn_id', plan_id,'entrance', 'FTN_AMD_PLAN_CARRY_OUT', 'stylesheet', 'training_plan_detail.xsl');
				var url = wb_utils_invoke_ae_servlet('cmd', 'ae_form_ins_itm', 'stylesheet', 'itm_add_run.xsl', 'ity_id','CLASSROOM', 'tvw_id', 'DETAIL_VIEW','plan_id',frm.plan_id.value,'training_plan',true,'tpn_update_timestamp',frm.tpn_update_timestamp.value,'tpn_itm_run_ind',true, 'url_failure', url_failure);
				frm.url_success.value = url; 
		} else if(frm.training_plan.value =='true') {
				var plan_id;
				var plan_tcr_id;
				if(frm.plan_id) {
					plan_id = frm.plan_id.value;
				}
				if(frm.plan_tcr_id){
					plan_tcr_id = frm.plan_tcr_id.value;
				}
				url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan','tcr_id', plan_tcr_id,'tpn_id', plan_id,'entrance', 'FTN_AMD_PLAN_CARRY_OUT', 'stylesheet', 'training_plan_detail.xsl');
				frm.url_failure.value = url;
				frm.url_success.value = url;
		}else{
			
				frm.url_success.value = _wbItemGetItemDetailURL('$id');
		}	
		if(submit_trigger == 'true'){
			submit_trigger = 'false';
			frm.submit();
			document.getElementById("submit_btn").disabled = true;
		}
	}
}

function wbItemSimpleInsItemExec(frm, itm_type){
	if(ValidateFrm(frm)){
		feed_param_value(frm);
		GenerateXML(frm);
		
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		frm.cmd.value = "ae_simple_ins_itm";
		frm.url_failure.value = self.location.href;
		var url_success;
		if(itm_type == 'CLASSROOM' && frm.itm_run_ind.value != 'true'){
			//url_success = wbItemContentInfoURL('$id', true);
			url_success = _wbItemGetItemDetailURL('$id');
		}else if(itm_type == 'CLASSROOM'){
			url_success = _wbItemGenGetItemDetailURL('$id', 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false);
		}else if(itm_type == 'INTEGRATED'){
			url_success = _wbItemGenGetItemDetailURL('$id', 'DETAIL_VIEW', 'itm_details.xsl', '', '', false, false);
		}else{
			url_success = _wbItemGetItemDetailURL('$id');
			//url_success = wb_utils_invoke_servlet('cmd', 'get_cos', 'course_id', '$id', 'cos_type', frm.itm_type.value, 'stylesheet', 'course_authoring_ns.xsl', 'is_new_cos', true);
		}
		frm.url_success.value = url_success;
		frm.submit();
	}
}


function wbItemUpdPrep(itm_id, isReqAppr){
	
	if(isReqAppr == true){
		stylesheet = 'itm_upd_req_appr.xsl';
	}else{
		stylesheet = 'itm_upd.xsl';
	}

	if(isReqAppr == true){
		wb_utils_set_cookie('course_approval_request_url', self.location.href);
	}
	_wbItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', stylesheet, '', '', false, false, false);
}

function wbItemGetItemDetail(itm_id){
	_wbItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_details.xsl', '', '', false, false, false);
}
function wbItemUpdPrepBatch(itm_exam_ind) {
	stylesheet = 'itm_upd_batch.xsl';
	_wbItemGenGetItemDetailBatch(0, 'DETAIL_VIEW', stylesheet, '', '', false, false, false,itm_exam_ind);
}

function _wbItemGenGetItemDetailBatch(itm_id, tvw_id, stylesheet, url_failure, tnd_id, prev_version_ind, show_run_ind, show_session_ind,itm_exam_ind) {
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm_batch', 'itm_id', itm_id, 'tvw_id', tvw_id, 'stylesheet', stylesheet, 'url_failure', url_failure, 'tnd_id', tnd_id, 'prev_version_ind', prev_version_ind, 'show_run_ind', show_run_ind, 'show_session_ind', show_session_ind,'itm_exam_ind',itm_exam_ind);
	window.location.href = url;
}

function wbReturnItemGetItemDetailUrl(itm_id){
	return _wbReturnItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_details.xsl', '', '', false, false, false);
}

function wbReturnItemGetItemRunDetailUrl(itm_id){
	return _wbReturnItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false, false);
}

function wbItemGetItemRunDetail(itm_id, progress){
	if(progress == 'CANCELLED'){
		_wbItemGenGetItemDetail(itm_id, 'CANCELLED_VIEW', 'itm_run_view_cancel.xsl', '', '', false, false, false);
	}else if(progress == '' || progress == null){
		_wbItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false, false);
	}else{
		_wbItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false, false);
	}
}

function wbItemGetItemLrnDetail(itm_id){
	var url_failure = window.location.href;
	_wbItemGenGetItemDetail(itm_id, 'LRN_VIEW', 'itm_lrn_details.xsl', url_failure, '', false, true, true);
}

function _tcrChanged(org_id, new_id) {
    if (org_id && new_id) {
        if (org_id != new_id) {
            return true;
        }
    }
}

var updating = false;//防止重复提交，开始为false
function wbItemUpdExec(frm, itm_id, isRun, isReqAppr, isSession){

	if(updating){
		return;
	}
	
	updating = true;
	
	if(ValidateFrm(frm)){
	    //if tcr_id changed,pop a js warn
        var new_tcr_id;
        if (frm.training_center__box_single) {
            new_tcr_id = frm.training_center__box.options[0].value;
        }
        if (_tcrChanged(frm.org_tcr_id.value, new_tcr_id)) {
            if (confirm(wb_msg_perp_clean)) {
                //do nothing
            } else {
                return;
            }
        }
	    
		feed_param_value(frm);
		
		var spiltIndex = 0;
		if(frm.itm_icon && frm.itm_icon.value.length > 0){
			spiltIndex = frm.itm_icon.value.lastIndexOf('\\');
			var itm_icon_name = frm.itm_icon.value.substring(spiltIndex+1);
			if(itm_icon_name && getChars(itm_icon_name) > 200){
				alert(label_tm.label_core_training_management_309);
				updating = false;
				return;
			}
		}
		
		GenerateXML(frm);
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		frm.cmd.value = "ae_upd_itm";

		if(isRun == true){
			frm.url_failure.value = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false);
			frm.url_success.value = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false);
		}else if(isSession == true){
			frm.url_failure.value = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false);
			frm.url_success.value = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false);
		}else{
			if(isReqAppr == true){
				frm.url_failure.value = _wbItemGetItemDetailURL(itm_id);
				frm.url_success.value = wb_utils_invoke_ae_servlet('cmd', 'init_msg', 'msg_type', 'course_approval_request', 'stylesheet', 'msg_notify.xsl', 'id', itm_id, 'id_type', 'item');
			}else{
				frm.url_failure.value = _wbItemGetItemDetailURL(itm_id);
				frm.url_success.value = _wbItemGetItemDetailURL(itm_id);
			}
		}
		frm.tvw_id.value = 'DETAIL_VIEW';
		frm.submit();
	}else{
		updating = false;
	}
}
function wbItemUpdBatchExec(frm, lang, itm_exam_ind) {
	if (validationFrom_batch(frm)) {
		if(frm.itm_bonus_ind_radio[0].checked==true) {
			if (!gen_validate_empty_field(frm.itm_diff_factor,frm.lab_itm_diff_factor.value,lang)) {
				frm.itm_diff_factor.focus();
				return false;
			}
			if(wbUtilsTrimString(frm.itm_diff_factor.value) == 0){
				alert(wb_msg_pls_enter_positive_integer_1 + '"' + frm.lab_itm_diff_factor.value + '"' + wb_msg_pls_enter_positive_integer_2);
				frm.itm_diff_factor.focus();
				return false;
			}	
			if (!wbUtilsValidateAllInteger(frm.itm_diff_factor, frm.lab_itm_diff_factor.value)){
				frm.itm_diff_factor.focus();
				return false
			}
		} else {
			frm.itm_diff_factor.value = '';
		}
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		frm.cmd.value = "ae_upd_itm_batch";
		var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm_batch', 'stylesheet', 'itm_upd_batch.xsl','itm_exam_ind',itm_exam_ind);
		frm.url_failure.value = url;
		frm.url_success.value = '/app/admin/course';
		frm.tvw_id.value = 'DETAIL_VIEW';
		if (frm.iscatalog_ind[0].checked) {
			var len = frm.itm_id_lsts.length;
			// 获取选择的id，并且组装成为字符串
			for ( var i = 0; i < len; i++) {
				if (i == 0) {
					frm.itm_id_lst.value = frm.itm_id_lsts[i].value
				} else {
					frm.itm_id_lst.value = frm.itm_id_lst.value + '~' + frm.itm_id_lsts[i].value
				}

			}
			frm.iscatalog.value = 'false';
		} else {
			len = frm.tnd_ids.length;
			// 获取选择的id，并且组装成为字符串
			for ( var i = 0; i < len; i++) {
				if (i == 0) {
					frm.tnd_id_lst.value = frm.tnd_ids[i].value
				} else {
					frm.tnd_id_lst.value = frm.tnd_id_lst.value + '~' + frm.tnd_ids[i].value
				}
			}
			frm.itm_id_lst.value = frm.tnd_id_lst.value;
			frm.iscatalog.value = 'true';
		}
		
		if(frm.iscatalog_change_ind[0].checked){
			// 获取选择的id，并且组装成为字符串
			for ( var i = 0; i < frm.tnd_ids_change.length; i++) {
				if (i == 0) {
					frm.tnd_ids_change_lst.value = frm.tnd_ids_change[i].value
				} else {
					frm.tnd_ids_change_lst.value = frm.tnd_ids_change_lst.value + '~' + frm.tnd_ids_change[i].value
				}
			}
		}else{
			frm.tnd_ids_change_lst.value = "";
		}
		// 如果没有选择课程或者目录就不能提交
		if (len != 0) {
			frm.submit();
		} else {
			if(frm.iscatalog_ind && frm.iscatalog_ind[0].checked == true) {
				alert(frm.alert_msg.value);
			} else if (frm.iscatalog_ind && frm.iscatalog_ind[1].checked == true) {
				alert(wb_msg_gb_sel_catalog);
			}
		}
	}
}

function validationFrom_batch(frm) {
	// Verify the start time period
	if (frm.itm_appn_start_datetime_yy.value != '' && frm.itm_appn_start_datetime_mm.value != '' && frm.itm_appn_start_datetime_dd.value != '') {
		if (wbUtilsValidateDate('document.' + frm.name + '.itm_appn_start_datetime', '报名期限', '', 'ymdhm')) {
			frm.itm_appn_start_datetime.value = frm.itm_appn_start_datetime_yy.value + '-' + frm.itm_appn_start_datetime_mm.value + '-' + frm.itm_appn_start_datetime_dd.value + ' '
					+ frm.itm_appn_start_datetime_hour.value + ':' + frm.itm_appn_start_datetime_min.value + ':00.000'
		} else {
			return false;
		}
	}

	if (frm.itm_appn_end_datetime_radio[0].checked) {
		frm.itm_appn_end_datetime.value = '9999-12-31 23:59:59.0';
	} else if (frm.itm_appn_end_datetime_radio[1].checked) {
		if (frm.itm_appn_end_datetime_temp_yy.value != '' && frm.itm_appn_end_datetime_temp_mm.value != '' && frm.itm_appn_end_datetime_temp_dd.value != '') {
			if (wbUtilsValidateDate('document.' + frm.name + '.itm_appn_end_datetime_temp', '报名期限', '', 'ymdhm')) {
				if (!wb_utils_validate_date_compare({
					frm : 'document.' + frm.name,
					start_obj : 'itm_appn_start_datetime',
					end_obj : 'itm_appn_end_datetime_temp'
				})) {
					return false;
				}
				frm.itm_appn_end_datetime.value = frm.itm_appn_end_datetime_temp_yy.value + '-' + frm.itm_appn_end_datetime_temp_mm.value + '-' + frm.itm_appn_end_datetime_temp_dd.value + ' '
						+ frm.itm_appn_end_datetime_temp_hour.value + ':' + frm.itm_appn_end_datetime_temp_min.value + ':59.000'
			} else {
				return false;
			}

		}
	}

	if (frm.itm_content_eff_end_datetime_radio[0].checked) {
		frm.itm_content_eff_end_datetime.value = '9999-12-31 23:59:59.0';
	} else if (frm.itm_content_eff_end_datetime_radio[1].checked) {
		if (frm.itm_content_eff_end_datetime_yy.value != '' && frm.itm_content_eff_end_datetime_mm.value != '' && frm.itm_content_eff_end_datetime_dd.value != '') {
			if (wbUtilsValidateDate('document.' + frm.name + '.itm_content_eff_end_datetime', '报名期限', '', 'ymd')) {
				frm.itm_content_eff_end_datetime.value = frm.itm_content_eff_end_datetime_yy.value + '-' + frm.itm_content_eff_end_datetime_mm.value + '-' + frm.itm_content_eff_end_datetime_dd.value + ' '
						+ frm.itm_content_eff_end_datetime_hour.value + ':' + frm.itm_content_eff_end_datetime_min.value + ':59.000'
			} else {

				return false;
			}

		}
	} else if (frm.itm_content_eff_end_datetime_radio[2].checked) {
		if (frm.itm_content_eff_duration.value <= 0) {
			return false;
		}
	}
	if (frm.itm_bonus_ind_radio[0].checked) {
		frm.itm_bonus_ind.value = "true";
	}
	if (frm.itm_bonus_ind_radio[1].checked) {
		frm.itm_bonus_ind.value = "false";
	}
	if (frm.itm_retake_ind_radio[0].checked) {
		frm.itm_retake_ind.value = "true";
	}
	if (frm.itm_retake_ind_radio[1].checked) {
		frm.itm_retake_ind.value = "fasle";
	}
	return true;
}

function wbItemNewItemVersionPrep(itm_id){
	_wbItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_new_ver.xsl', '', '', false, false, false);
}

function wbItemNewItemVersionExec(frm, itm_id){
	if(ValidateFrm(frm)){
		feed_param_value(frm);
		GenerateXML(frm);
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		frm.cmd.value = "ae_ins_new_itm";
		frm.url_failure.value = _wbItemGetItemDetailURL(itm_id);
		frm.url_success.value = _wbItemGetItemDetailURL(itm_id);
		frm.url_success.value += '&get_last=1';
		frm.submit();
	}
}

function wbItemDelExec(itm_id, timestamp, lang,type, parent_itm_id,dummy_type, training_plan, is_complete_del){
	var temp_url = '';
	if(type == 'session'){
		temp_url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_child_lst', 'itm_id', parent_itm_id, 'stylesheet', 'itm_session_lst.xsl', 'show_attendance_ind', 'true');
	}else if(type == 'run'){
		temp_url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_run_lst', 'itm_id', parent_itm_id, 'stylesheet', 'itm_run_lst.xsl', 'show_attendance_ind', 'true');
	}else{
		if(dummy_type == 'REF'){
			temp_url = wb_utils_controller_base + "admin/course?type=open"
		} else if(dummy_type == 'EXAM'){
			temp_url = wb_utils_controller_base + "admin/course?type=exam"
		}else {
			temp_url = wb_utils_controller_base + "admin/course"
		}
	}

	if (is_complete_del === undefined) {
		is_complete_del = false;
	}
	
	var url_success = temp_url;
	var url_failure = window.location.href;
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_del_itm', 'itm_id_lst', itm_id, 'itm_upd_timestamp_lst', timestamp, 'is_complete_del',is_complete_del ,'url_success', url_success, 'url_failure', url_failure);
	if(training_plan=='true') {
		if(confirm(eval('wb_msg_confirm_del_itm_tp'))){
			window.location.href = url;
		}
	} else {
		if(confirm(eval('wb_msg_' + lang + '_confirm_del_itm'))){
			window.location.href = url;
		}
	}
	
}

function wbItemDelItemLstExec(frm, lang){
	var itm_id_lst = _wbItemDelItemLst(frm);
	if(itm_id_lst == ''){
		alert(eval('wb_msg_' + lang + '_sel_del_itm'))
	}else if(confirm(eval('wb_msg_' + lang + '_confirm_del_itm'))){
		itm_upd_timestamp_lst = _wbItemDelGetTimestampLst(frm);
		frm.itm_id_lst.value = itm_id_lst;
		frm.url_failure.value = self.location.href;
		frm.url_success.value = self.location.href;
		frm.itm_upd_timestamp_lst.value = itm_upd_timestamp_lst;
		frm.cmd.value = 'ae_del_itm';
		frm.method = 'post';
		frm.action = wb_utils_ae_servlet_url;
		frm.submit();
	}
}

function wbItemUpdWorkflowPrep(itm_id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'itm_upd_workflow.xsl');
	window.location.href = url;
}

function wbItemUpdWorkflowExec(frm, lang){
	if(frm.wrk_tpl_id[1].checked == true){
		if(frm.chk_supervise.checked == true && frm.chk_tadm.checked == true){
			if(frm.rdo[0].checked){
				frm.itm_app_approval_type.value = "DIRECT_SUPERVISE_TADM";
			}else if(frm.rdo[1].checked){
				frm.itm_app_approval_type.value = "DIRECT_SUPERVISE_SUPERVISE_TADM";
			}else{
				alert(wb_msg_select_supervisor_approval);
				return;
			}
		}else if(frm.chk_supervise.checked == true){
			if(frm.rdo[0].checked){
				frm.itm_app_approval_type.value = "DIRECT_SUPERVISE";
			}else if(frm.rdo[1].checked){
				frm.itm_app_approval_type.value = "DIRECT_SUPERVISE_SUPERVISE";
			}else{
				alert(wb_msg_select_supervisor_approval);
				return;
			}
		}else if(frm.chk_tadm.checked == true){
			frm.itm_app_approval_type.value = "TADM";
		}else{
			alert(eval('wb_msg_' + lang + '_select_app_approval_type'));
			return;
		}
	}else{
		frm.itm_app_approval_type.value = "";
	}

	frm.action = wb_utils_ae_servlet_url;
	frm.method = 'post';
	frm.cmd.value = "ae_upd_itm_workflow";
	frm.url_success.value = _wbItemGetItemDetailURL(frm.itm_id.value);
	frm.url_failure.value = window.location.href;
	frm.submit();
}

function wbItemGetItemList(type){
	var url = _wbItemGetItemListURL(type);
	window.location.href = url;
}

function wbItemGetItemRunList(itm_id){
	var url_failure = _wbItemGetItemDetailURL(itm_id);
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_run_lst', 'itm_id', itm_id, 'url_failure', url_failure, 'stylesheet', 'itm_run_lst.xsl');
	window.location.href = url;
}

function wbItemSortItemList(order_by, sort_order){
	wb_utils_nav_get_urlparam('orderby', order_by, 'sortorder', sort_order);
}

function wbItemSearchItemPrep(tnd_id, cat_public_ind){
	if(tnd_id == null){
		tnd_id = '';
	}

	//Man: for Public Catalog
	if(cat_public_ind == null){
		var temp_cat_public_ind = getUrlParam("cat_public_ind");
		if(temp_cat_public_ind != ""){
			cat_public_ind = temp_cat_public_ind;
		}else{
			cat_public_ind = false;
		}
	}
	
	var training_type=getUrlParam("training_type");

	// get the current value of "show_respon" and pass it to the next page
	// (because need to pass the value to subsequent search result pages)
	// (2003-07-09 kawai)
	var cur_show_respon_value = getUrlParam("show_respon");

	if(cur_show_respon_value != 'true'){
		cur_show_respon_value = 'false';
	}

	var url = wb_utils_invoke_ae_servlet('cmd', 'get_itm_ref_data', 'stylesheet', 'itm_search.xsl', 'tnd_id', tnd_id, 'cat_public_ind', cat_public_ind, 'show_respon', cur_show_respon_value,'training_type',training_type);
	window.location.href = url;
}

function wbItemSearchItemExec(frm, lang){
	if(_wbItemSearchValidate(frm, lang)){
		if(frm.catalog_checkbox_ind){
			//When have catalog
			if(frm.cat_rdo && frm.cat_rdo[0] && frm.cat_rdo[0].checked){
				frm.all_ind.value = 'true';
			}else{
				if(frm.tnd_id.length == 0){
					alert(eval('wb_msg_' + lang + '_sel_catalog'));
					return;
				}

				frm.all_ind.value = 'false';
				_wbItemGetCatId(frm);
			}
		}else{
			//No Catalog Case
			frm.all_ind.value = 'true';
		}
		//if no spec training center
		if (frm.tcr_id_lst_box) {
		    if (frm.tcr_sel_all[1].checked && frm.tcr_id_lst_box.length==0) {
		        alert(wb_msg_pls_input_tcr);
		        return;
		    }
		}

//		if(frm.itm_only_open_enrol_now.checked){
//			frm.itm_only_open_enrol_now.value = true;
//		}else{
//			frm.itm_only_open_enrol_now.value = false;
//		}
//		
//		if(frm.itm_only_open_enrol_quota_now.checked){
//			frm.itm_only_open_enrol_quota_now.value = true;
//		}else{
//			frm.itm_only_open_enrol_quota_now.value = false;
//		}

		if(frm.status_chk && frm.status){
			if(frm.status_chk[0].checked && frm.status_chk[1].checked){
				frm.status.value = ""
			}else if(frm.status_chk[0].checked){
				frm.status.value = frm.status_chk[0].value
			}else if(frm.status_chk[1].checked){
				frm.status.value = frm.status_chk[1].value
			}
		}

		if(frm.exact.checked){
			frm.exact.value = "true";
		}

		//Man: url_failure removed to prevent URL too long	
		//frm.url_failure.value = window.location.href
		frm.method = 'get';
		frm.action = wb_utils_ae_servlet_url;
		frm.cmd.value = 'ae_lookup_itm';

		if(frm.orderby){
			frm.orderby.value = 'r_itm_title';
		}

		if(frm.sortorder){
			frm.sortorder.value = 'asc';
		}

		frm.tvw_id.value = 'LIST_VIEW';

		// get the current value of "show_respon" and pass it to the next page
		// (because need to pass the value to subsequent search result pages)
		// (2003-07-09 kawai)
		if(frm.show_respon){
			var cur_show_respon_value = getUrlParam("show_respon");

			if(cur_show_respon_value != 'true'){
				cur_show_respon_value = 'false';
			}
			frm.show_respon.value = cur_show_respon_value;
		}
		//Man:cat_public_ind for Public Catalog
		if(frm.cat_public_ind){
			frm.cat_public_ind.value = getUrlParam("cat_public_ind");
		}

		if(frm.tcr_id_lst_box && frm.tcr_id_lst) {
			_wbItemGetTcrId(frm);
		}

		frm.stylesheet.value = 'itm_search_result.xsl';
		frm.submit();
	}
}

function wbItemSearchRunPrep(tnd_id, cat_public_ind){
	//Man: for Public Catalog
	if(cat_public_ind == null){
		var temp_cat_public_ind = getUrlParam("cat_public_ind");
		if(temp_cat_public_ind != ""){
			cat_public_ind = temp_cat_public_ind;
		}else{
			cat_public_ind = false;
		}
	}
	
	var training_type = getUrlParam("training_type");

	var url = wb_utils_invoke_ae_servlet('cmd', 'get_run_ref_data', 'stylesheet', 'run_search_main.xsl', 'cat_public_ind', cat_public_ind ,'training_type' ,training_type);
	window.location.href = url;
}

function wbItemSearchRunExec(frm, lang){
	if(_wbItemSearchValidate(frm, lang) && _wbItemSearchRunValidate(frm, lang)){
		frm.all_ind.value = 'true';
		frm.show_run_ind.value = 'true';
		frm.show_no_run.value = 'false';

		//concat targeted learner value
		if (frm.r_target_ent_group_lst_box){
			if(frm.r_target_ent_group_lst_box.options.length != 0){
				var tl_list = '';
				var tl_idx = 0;
				for (tl_idx = 0; tl_idx < frm.r_target_ent_group_lst_box.options.length;tl_idx++){
					tl_set = [];
					tl_set = frm.r_target_ent_group_lst_box.options[tl_idx].value.split("~");
					var j=0;
					temp_str ='';
					for(j=0;j<tl_set.length;j++){
						if(tl_set[j] != '0'){
						 temp_str += tl_set[j] + ","
						}
					}
					temp_str = temp_str.substring(0,temp_str.length -1);
					tl_list += temp_str;
					if(tl_idx != (frm.r_target_ent_group_lst_box.options.length -1)){
						tl_list += '~'
					}
				}
				frm.r_target_ent_group_lst.value = tl_list
			}
		}
		if(frm.itm_only_open_enrol_now.checked){
			frm.itm_only_open_enrol_now.value = true;
		}else{
			frm.itm_only_open_enrol_now.value = false;
		}

		if(frm.exact.checked){
			frm.exact.value = "true";
		}

		if(frm.r_exact.checked){
			frm.r_exact.value = "true";
		}

		if(frm.include_cancel_chk && frm.include_cancel_chk.checked) {
			frm.itm_life_status_equal_lst.value = 'NULL~CANCELLED'
		} else {
			frm.itm_life_status_equal_lst.value = 'NULL'
		}

		frm.method = 'get';
		frm.action = wb_utils_ae_servlet_url;
		frm.cmd.value = 'ae_lookup_itm';

		if(frm.orderby){
			frm.orderby.value = 'r_itm_title';
		}

		if(frm.sortorder){
			frm.sortorder.value = 'asc';
		}

		frm.tvw_id.value = 'LIST_VIEW';

		//Man:cat_public_ind for Public Catalog
		if(frm.cat_public_ind){
			frm.cat_public_ind.value = getUrlParam("cat_public_ind");
		}

		if(frm.r_tcr_id_lst_box && frm.r_tcr_id_lst) {
			_wbItemGetTcrId(frm);
		}

		frm.stylesheet.value = 'run_search_result_main.xsl';
		frm.submit();
	}
}

function wbItemSearchItemAddRunPrep(){

	var url = wb_utils_invoke_ae_servlet('cmd', 'get_itm_ref_data', 'stylesheet', 'itm_search_add_run.xsl');
	window.location.href = url;
}

function wbItemSearchItemAddRunExec(frm){
	frm.code.value = wbUtilsTrimString(frm.code.value);
	frm.title.value = wbUtilsTrimString(frm.title.value);

	frm.all_ind.value = 'true';

	if(frm.exact.checked){
		frm.exact.value = "true";
	}

	//Man: url_failure removed to prevent URL too long	
	//frm.url_failure.value = window.location.href
	frm.method = 'get';
	frm.action = wb_utils_ae_servlet_url;
	frm.cmd.value = 'ae_lookup_itm';

	if(frm.orderby){
		frm.orderby.value = 'r_itm_title';
	}

	if(frm.sortorder){
		frm.sortorder.value = 'asc';
	}

	//Man:cat_public_ind for Public Catalog
	if(frm.cat_public_ind){
		frm.cat_public_ind.value = getUrlParam("cat_public_ind");
	}
	frm.stylesheet.value = 'itm_search_add_run_result.xsl';
	frm.submit();
}

function _wbItemConcateTreeNodeId(frm){
	// concat tree node id
	var i, n, ele;
	temp_lst = '';
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "checkbox" && ele.name == "treenode_id" && ele.checked){
			if(ele.value != "") temp_lst += ele.value + '~';
		}
	}
	return temp_lst;
}

function _wbItemConcateLocodeId(frm){
	// concat tree node id
	var i, n, ele;
	temp_lst = '';
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "checkbox" && ele.name == "locnode_id" && ele.checked){
			if(ele.value != "") temp_lst += ele.value + '~';
		}
	}
	return temp_lst;
}

function wbItemSearchCosMainHasRun(curTime, itmType, showRunInd){
	if(showRunInd == null){
		showRunInd = 'true';
	}

	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_no_run', 'true', 'show_orphan', 'true', 'show_respon', 'true', 'type', itmType, 'eff_from', curTime, 'eff_from_operator', ">=", 'orderby', 'r_itm_eff_start_datetime', 'sortorder', 'asc', 'page_size', '10', 'itm_life_status_not_equal_lst', 'CANCELLED', 'allow_null_datetime', 'true', 'show_run_ind', showRunInd);
	window.location.href = url;
}

function wbItemSearchCosMainNoRun(curTime, itmType){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_respon', 'true', 'show_orphan', 'true', 'type', itmType, 'orderby', 'r_itm_id', 'sortorder', 'DESC', 'page_size', '10', 'allow_null_datetime', 'true');
	window.location.href = url;
}

function wbItemSearchEnrListHasRun(curTime, itmType){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_no_run', 'false', 'show_respon', 'true', 'show_respon', 'true', 'type', itmType, 'appn_to', curTime, 'appn_to_operator', "<", 'filter_retire_or_in_process_att', 'true', 'orderby', 'r_itm_eff_end_datetime', 'page_size', '10', 'itm_life_status_not_equal_lst', 'CANCELLED', 'allow_null_datetime', 'true');
	window.location.href = url;
}

function wbItemSearchComingListHasRun(curTime, itmType){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_no_run', 'true', 'show_orphan', 'true', 'show_respon', 'true', 'type', itmType, 'appn_to', curTime, 'appn_to_operator', ">=", 'orderby', 'r_itm_appn_end_datetime', 'page_size', '10', 'itm_life_status_not_equal_lst', 'CANCELLED', 'allow_null_datetime', 'true');
	window.location.href = url;
}

function wbItemSearchComingListNoRun(curTime, itmType){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_orphan', 'true', 'show_respon', 'true', 'type', itmType, 'orderby', 'r_itm_id', 'sortorder', 'desc', 'page_size', '10', 'allow_null_datetime', 'true');
	window.location.href = url;
}

function wbItemSearchSimpleExec(frm, lang, orderby, sortorder){
	frm.title_code.value = wbUtilsTrimString(frm.title_code.value);
	if(!gen_validate_empty_field(frm.title_code, eval('wb_msg_' + lang + '_search_field'), lang)){
		frm.title_code.focus();
		return;
	}
	frm.orderby.value = (orderby != null) ? orderby : 'r_itm_title';
	frm.sortorder.value = (sortorder != null) ? sortorder : 'asc';

	// get the current value of "show_respon" and pass it to the next page
	// (because need to pass the value to subsequent search result pages)
	// (2003-07-09 kawai)
	if(frm.show_respon){
		var cur_show_respon_value = getUrlParam("show_respon");

		if(cur_show_respon_value != 'true'){
			cur_show_respon_value = 'false';
		}
		frm.show_respon.value = cur_show_respon_value;
	}

	//Man: Public Catalog
	if(frm.cat_public_ind){
		if(frm.cat_public_ind.value == ""){
			frm.cat_public_ind.value = getUrlParam("cat_public_ind");
		}
	}

	frm.cmd.value = 'ae_lookup_itm';
	frm.stylesheet.value = 'itm_search_result.xsl';
	frm.action = wb_utils_ae_servlet_url;
	frm.tvw_id.value = 'DETAIL_VIEW';
	frm.method = 'get';
	frm.submit();
}

function wbItemSearchRunSimpleExec(frm, lang, orderby, sortorder){
	frm.r_title_code.value = wbUtilsTrimString(frm.r_title_code.value);
	if(!gen_validate_empty_field(frm.r_title_code, eval('wb_msg_' + lang + '_search_field'), lang)){
		frm.r_title_code.focus();
		return;
	}

	frm.orderby.value = (orderby != null) ? orderby : 'r_itm_title';
	frm.sortorder.value = (sortorder != null) ? sortorder : 'asc';

	//Man: Public Catalog
	if(frm.cat_public_ind){
		if(frm.cat_public_ind.value == ""){
			frm.cat_public_ind.value = getUrlParam("cat_public_ind");
		}
	}

	frm.cmd.value = 'ae_lookup_itm';
	frm.stylesheet.value = 'run_search_result_main.xsl';
	frm.action = wb_utils_ae_servlet_url;
	frm.method = 'get';
	frm.submit();
}


/*function wbItemGetApprovedVersionDetail(itm_id){
	var temp_url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'all_ind', 'true', 'exact', 'false', 'type', '', 'stylesheet', 'itm_lst.xsl', 'tvw_id', 'LIST_VIEW')
	var url_failure = temp_url
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_approved_version', 'itm_id', itm_id, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_approved_detail.xsl', 'url_failure', url_failure, 'tnd_id', '', 'prev_version_ind', 'true');
	window.location.href = url;
}

function wbItemGetVersionDetail(itm_id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_approved_version', 'itm_id', itm_id, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_approved_detail.xsl', 'url_failure', '', 'tnd_id', '', 'prev_version_ind', true, 'show_run_ind', false);
	window.location.href = url;
}*/

function wbItemInsItemRunPrep(itm_id, ity_id, itm_tcr_id){
	if(itm_tcr_id == null)	itm_tcr_id = '';
	var url_failure = _wbItemGetItemDetailURL(itm_id);
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_form_ins_itm', 'stylesheet', 'itm_add_run.xsl', 'ity_id', ity_id, 'tvw_id', 'DETAIL_VIEW', 'itm_id', itm_id, 'itm_tcr_id', itm_tcr_id, 'url_failure', url_failure);
	window.location.href = url;
}

function wbItemInsItemRunExec(frm){
	var itm_id = getUrlParam('itm_id');
	var itm_session_ind = getUrlParam('itm_session_ind');
	var training_plan;
	if(frm.training_plan) {
		training_plan = frm.training_plan.value;
	} else {
		training_plan = false;
	}

	
	if(ValidateFrm(frm)){
		feed_param_value(frm);
		GenerateXML(frm);
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		frm.cmd.value = "ae_ins_itm";
		if(training_plan =='true') {
			var plan_id;
			var plan_tcr_id;
			if(frm.plan_id) {
				plan_id = frm.plan_id.value;
			}
			if(frm.plan_tcr_id){
				plan_tcr_id = frm.plan_tcr_id.value;
			}
			url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan','tcr_id', plan_tcr_id,'tpn_id', plan_id,'entrance', 'FTN_AMD_PLAN_CARRY_OUT', 'stylesheet', 'training_plan_detail.xsl');
			frm.url_failure.value = self.location.href;
			frm.url_success.value = url;
		} else {
			frm.url_failure.value = self.location.href;
			frm.url_success.value = _wbItemGenGetItemDetailURL('$id', 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false);		
		}

		if(submit_trigger == 'true'){
			submit_trigger = 'false';
			frm.submit();
		}
	}
}

function wbItemGetResourceDetailWin(res_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_res', 'res_id', res_id, 'stylesheet', 'res_srh_res_ind.xsl', 'url_failure', self.location.href);
	var feature = 'toolbar=' + 'no' + ',location=' + 'no' + ',width=' + 800 + ',height=' + 400 + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',screenX=' + '100' + ',screenY=' + '100' + ',left=' + '100' + ',top=' + '100' + ',status=' + 'no';
	wbUtilsOpenWin(url, 'win', false, feature);
}

function wbItemCancelItemPrep(itm_id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_prep_cancel_itm', 'stylesheet', 'itm_cancel.xsl', 'itm_id', itm_id);
	window.location.href = url;
}

function wbItemCancelItemExec(frm, lang, itemType){
	if(_wbItemCancelItemValidate(frm, lang)){
		var itm_id = frm.itm_id.value;

		frm.cmd.value = 'ae_cancel_itm';
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';

		if(itemType == 'course'){
			frm.url_failure.value = _wbItemGetItemDetailURL(itm_id);
			frm.url_success.value = _wbItemGetItemDetailURL(itm_id);
		}else{
			frm.url_success.value = _wbItemGenGetItemDetailURL(itm_id, 'CANCELLED_VIEW', 'itm_run_view_cancel.xsl', '', '', false, false);
			frm.url_failure.value = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false);
		}
		frm.submit();
	}
}

function wbItemUpdItemStatus(itm_id_lst, itm_status_lst, itm_upd_timestamp_lst, run_ind){
	var url;
	if(itm_status_lst == 'ON' && run_ind != 'true') {
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_itm_publish_target', 'stylesheet', 'itm_publish_target_ind.xsl', 'itm_id_lst', itm_id_lst, 'itm_upd_timestamp_lst', itm_upd_timestamp_lst, 'itm_status_lst', itm_status_lst);
		var str_feature = 'toolbar=' + 'no' + ',width=' + '480' + ',height=' + '200' + ',scrollbars=' + 'yes' + ',resizable=' + 'false' + ',status=' + 'no';
		if(document.all){
			str_feature += ',top=' + '100';
			str_feature += ',left=' + '100';
 		}
		wbUtilsOpenWin(url,'', false, str_feature);

	} else {
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_upd_itm_status', 'url_success', self.location.href, 'itm_id_lst', itm_id_lst, 'itm_status_lst', itm_status_lst, 'itm_upd_timestamp_lst', itm_upd_timestamp_lst, 'url_failure', self.location.href);
		window.location.href = url;
	}
}

function wbItemUpdItemStatusOn(frm){
    frm.cmd.value = "ae_upd_itm_status";
    frm.method = "post";
    frm.action = wb_utils_ae_servlet_url;
    frm.url_success.value = _wbItemGetItemDetailURL(frm.itm_id_lst.value);
    frm.url_failure.value = _wbItemGetItemDetailURL(frm.itm_id_lst.value);
    var url = $(frm).attr('action');
    $.post(url, $(frm).serialize(), function(data) {
        if(parent.window.opener) {
            parent.window.opener.location.reload();
            window.close();
        }
    });
}

function wbItemAutoEnrolCos(itm_id, cos_id){
	var url_success = _wbItemAutoEnrolCourseStartURL(cos_id);
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_auto_enrol_cos', 'itm_id', itm_id, 'dpo_view', 'LRN_READ', 'nav_num_of_mod', 10, 'msg_type', 'res', 'url_success', url_success);
	window.location.href = url;
}

function wbItemOrphanItemLst(order_by, sort_order){
	order_by = (order_by == null) ? 'itm_code' : order_by;
	sort_order = (sort_order == null) ? 'asc' : sort_order;
  var tcr_id = getUrlParam("cat_tcr_id");
	if (tcr_id == ''){
		tcr_id = -1
	}
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_orphan_itm', 'orderby', order_by, 'sortorder', sort_order, 'stylesheet', 'orphan_itm_lst.xsl', 'tcr_id', tcr_id);
	window.location.href = url;
}

// -Private Functions--------------------------------------------- 
function _wbGenItemCostInfo(itm_id, stylesheet, url_failure){
   var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm_cost', 'itm_id', itm_id,'stylesheet', stylesheet, 'url_failure', url_failure);
	window.location.href = url;
}

function _wbItemAutoEnrolCourseStartURL(id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_enrol_cos', 'res_id', id, 'dpo_view', 'LRN_READ', 'stylesheet', 'lrn_tst_lst.xsl', 'nav_num_of_mod', '10', 'msg_type', 'RES');
	wb_utils_set_cookie('course_id', id);
	return url;
}

function _wbItemGetItemDetailURL(itm_id){
	var url = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_details.xsl', '', '', false, false);
	return url;
}

function _wbItemSelectAddItemTypeURL(training_type,training_plan,tcr_id,plan_id,tpn_update_timestamp,plan_type){
	var url = '';
	if(training_plan =='true' || training_plan == true){
		url=wb_utils_invoke_ae_servlet('cmd', 'ae_get_all_ity_form', 'stylesheet', 'training_plan_impl.xsl','training_plan',training_plan,'tcr_id',tcr_id,'plan_id',plan_id,'tpn_update_timestamp',tpn_update_timestamp,'entrance',plan_type);
	}else{
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_all_ity_form', 'stylesheet', 'ls_add_form_prep.xsl','training_type',training_type);
	}
	return url;
}

function _wbItemGenGetItemDetail(itm_id, tvw_id, stylesheet, url_failure, tnd_id, prev_version_ind, show_run_ind, show_session_ind){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', tvw_id, 'stylesheet', stylesheet, 'url_failure', url_failure, 'tnd_id', tnd_id, 'prev_version_ind', prev_version_ind, 'show_run_ind', show_run_ind, 'show_session_ind', show_session_ind);
	window.location.href = url;
}

function _wbReturnItemGenGetItemDetail(itm_id, tvw_id, stylesheet, url_failure, tnd_id, prev_version_ind, show_run_ind, show_session_ind){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', tvw_id, 'stylesheet', stylesheet, 'url_failure', url_failure, 'tnd_id', tnd_id, 'prev_version_ind', prev_version_ind, 'show_run_ind', show_run_ind, 'show_session_ind', show_session_ind);
    return url;
}

function _wbItemGenGetItemDetailURL(itm_id, tvw_id, stylesheet, url_failure, tnd_id, prev_version_ind, show_run_ind){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', tvw_id, 'stylesheet', stylesheet, 'url_failure', url_failure, 'tnd_id', tnd_id, 'prev_version_ind', prev_version_ind, 'show_run_ind', show_run_ind);
	return url;
}

function _wbItemUpdPrepURL(itm_id){
	var url = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_upd.xsl', '', '', false, false);
	return url;
}

function _wbItemSearchValidate(frm, lang){
	frm.code.value = wbUtilsTrimString(frm.code.value);
	frm.title.value = wbUtilsTrimString(frm.title.value);

	if(frm.eff_from_yy.value.length != 0 || frm.eff_from_mm.value.length != 0 || frm.eff_from_dd.value.length != 0){
		if(wbUtilsValidateDate('document.frmXml.eff_from', eval('wb_msg_' + lang + '_class_date'))){
			frm.eff_from.value = frm.eff_from_yy.value + '-' + frm.eff_from_mm.value + '-' + frm.eff_from_dd.value + ' 00:00:00.0'
		}else{
			return false;
		}
	}

	if(frm.eff_to_yy.value.length != 0 || frm.eff_to_mm.value.length != 0 || frm.eff_to_dd.value.length != 0){
		if(wbUtilsValidateDate('document.frmXml.eff_to', eval('wb_msg_' + lang + '_class_date'))){
			frm.eff_to.value = frm.eff_to_yy.value + '-' + frm.eff_to_mm.value + '-' + frm.eff_to_dd.value + ' 23:59:59.0'
		}else{
			return false;
		}
	}

	if(frm.eff_from_yy.value.length != 0 && frm.eff_to_yy.value.length != 0) {
		if(!wb_utils_validate_date_compare({
			frm : 'document.' + frm.name, 
			start_obj : 'eff_from', 
			end_obj : 'eff_to' 
			}))	{
			return false;	
		}
	}
	
	return true;
}

function _wbItemSearchRunValidate(frm, lang){
	frm.r_code.value = wbUtilsTrimString(frm.r_code.value);
	frm.r_title.value = wbUtilsTrimString(frm.r_title.value);
	return true;
}

function _wbItemCancelItemValidate(frm, lang){
	if(frm.rsv_cancel_type[frm.rsv_cancel_type.selectedIndex].value == ''){
		alert(eval('wb_msg_' + lang + '_sel_cancellation_type'));
		return false;
	}

	if(!gen_validate_empty_field(frm.itm_cancellation_reason, eval('wb_msg_' + lang + '_reason'), lang)){
		frm.itm_cancellation_reason.focus();
		return false;
	}
	return true
}

//================================================================
function _wbItemLstGetAddLnkItemLst(){
	var i, n, str;

	str = "";

	frm = arguments[0];

	for(j = 1; j < arguments.length; j++){
		ele = eval('frm.' + arguments[j]);
		if(ele.length > 0){
			for (i = 0; i < ele.length; i++){
				if(ele.options[i].value != ""){
					str += ele.options[i].value.replace("~", ",") + "~"
				}
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbItemLstGetAddItemTypeCount(frm){
	var i, n, ele, str;

	str = 0;
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "checkbox" && ele.name == "item_type_id" && ele.checked){
			if(ele.value != "") str = str + 1
		}
	}
	return str;
}

function _wbItemLstGetAddItemTypeID(frm){
	var i, n, ele, str;

	str = 0;
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "checkbox" && ele.name == "item_type_id" && ele.checked){
			if(ele.value != "") str = ele.value
		}
	}
	return str;
}

function _wbItemDelItemLst(frm){
	var i, n, ele, str;

	str = "";
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "checkbox" && ele.checked && ele.name != 'select_all'){
			if(ele.value != "") str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbItemDelGetTimestampLst(frm){
	var i, n, ele, str;

	str = "";
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "hidden" && ele.name.indexOf('timestamp') != -1 && ele.name != 'itm_upd_timestamp_lst'){
			ele_nm = 'frm.' + ele.name.substring(0, ele.name.length - 10) + '.checked';
			if(ele.value != "" && eval(ele_nm)) str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbItemLstPickItemIdLst(frm){
	var i, n, ele, str;

	str = "";
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "checkbox" && ele.checked){
			if(ele.value != "") str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbItemPickProgItemCoreLst(frm){
	var i, n, ele, str;

	str = "";
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "radio" && ele.checked){
			if(ele.value != "") str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbItemPickProgItemIdLst(frm){
	var i, n, ele, str;

	str = "";
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "radio" && ele.checked){
			ele_nm = 'frm.' + ele.name + '_id.value';
			if(eval(ele_nm) != "") str = str + eval(ele_nm) + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbItemGetItemListURL(type){
	var url_failure = 'javascript:wb_utils_gen_home()';

	if(type){
		var index = type.indexOf("CLASSROOM");
		var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'all_ind', 'true', 'exact', 'false', 'type', type, 'stylesheet', 'itm_lst.xsl', 'tvw_id', 'LIST_VIEW', 'orderby', 'r_itm_upd_timestamp', 'sortorder', 'desc')
	}else{
		var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'all_ind', 'true', 'exact', 'false', 'type', '', 'stylesheet', 'itm_lst.xsl', 'tvw_id', 'LIST_VIEW', 'orderby', 'r_itm_upd_timestamp', 'sortorder', 'desc')
	}

	url += '&show_respon=true';
	return url;
}

function wbItemAssignCaching(frm){
	var i, n, ele, str, type_val, token_val, itm_cache_type;

	str = '';
	n = frm.elements.length;
	itm_cache_type = _wbItemGetCookie('itm_cache_type');
	if(frm.itm_cache_type.value == itm_cache_type){
		for (i = 0; i < n; i++){
			ele = frm.elements[i];

			token_val = _wbItemGetCookie(ele.name);
			if(token_val != '' && token_val != null){
				type_val = token_val.substring(0, token_val.indexOf('~%~'));
				if(type_val == ele.type && ele.value == ''){
					ele.value = token_val.substring(token_val.lastIndexOf('~%~') + 3, token_val.length)
				}
			}
		}
	}
}

function wbItemInsCaching(frm, obj){
	var str, token_val_len, cookie_remain_len, nowDay, nowMonth, nowYear, expireDate, nowDate;
	var cookie_remain_len = 2120 - gen_get_cookie('wb_item').length;
	var token_val_len = obj.value.length;
	var str = "";
	var nowDay = new Date();
	var expireDay = new Date(nowDay.getFullYear(), nowDay.getMonth(), nowDay.getDate(), nowDay.getHours() + 8, nowDay.getMinutes(), nowDay.getSeconds());

	_wbItemSetCookie('itm_cache_type', frm.itm_cache_type.value);

	if(obj.type == 'text' || obj.type == 'textarea'){
		if(obj.value != ''){
			str += obj.type + '~%~' + obj.value;
		}else{
			_wbItemSetCookie(obj.name, obj.value, expireDay)
		}
	}
	if((str != '') && (cookie_remain_len > token_val_len)){
		_wbItemSetCookie(obj.name, str, expireDay);
	}
}

function _wbItemSetCookie(token_nm, token_val, expire){
	gen_set_cookie_token('wb_item', token_nm, token_val, expire);
}

function _wbItemGetCookie(token_nm){
	return gen_get_cookie_token('wb_item', token_nm);
}

// function for wbSession
function wbSessionGetSessionList(itm_id){
	var url_failure = _wbItemGetItemDetailURL(itm_id);
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_child_lst', 'itm_id', itm_id, 'url_failure', url_failure, 'stylesheet', 'itm_session_lst.xsl', 'show_attendance_ind', 'true');
	window.location.href = url;
}

function wbSessionInsSessionPrep(itm_id, ity_id){
	var url_failure = _wbItemGetItemDetailURL(itm_id);
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_form_ins_itm', 'stylesheet', 'itm_add_run.xsl', 'ity_id', ity_id, 'tvw_id', 'DETAIL_VIEW', 'itm_id', itm_id, 'url_failure', url_failure, 'itm_session_ind', 'true');
	window.location.href = url;
}

function wbItemOnlineContentUpd(frm, itm_id, lang){
	if(frm.date_format[1].checked){
		if(frm.date_format[1].value != 'unlimited'){
			if(!gen_validate_positive_integer(frm.itm_content_eff_duration, eval('wb_msg_' + lang + '_no_of_days'), lang)){
				//wb_msg_en_greater_zero
				frm.itm_content_eff_duration.focus();
				return false
			}

			if(frm.itm_content_eff_duration.value == 0){
				alert(eval('wb_msg_' + lang + '_greater_zero'));

				frm.itm_content_eff_duration.focus();
				return false
			}

			frm.itm_content_eff_start_datetime.value = "";
			frm.itm_content_eff_end_datetime.value = "";
		}else{
			frm.itm_content_eff_start_datetime.value = "";
			frm.itm_content_eff_end_datetime.value = "";
		}
	}else if(frm.date_format[0].checked){
		if(frm.start_dd && frm.start_mm && start_yy){
			if(!gen_validate_date('document.frmXml.start', eval('wb_msg_' + lang + '_start_time'), lang)) return false;
		}

		if(frm.end_dd && frm.end_mm && frm.end_yy){
			if(!gen_validate_date('document.frmXml.end', eval('wb_msg_' + lang + '_end_time'), lang)) return false;
		}

		if(frm.start_dd && frm.start_mm && frm.start_yy && frm.end_dd && frm.end_mm && frm.end_yy){
			if(!gen_validate_date_compare_larger(frm, 'start', 'end', eval('wb_msg_' + lang + '_start_date'), eval('wb_msg_' + lang + '_end_date'), lang)){
				return false;
			}
		}

		frm.itm_content_eff_duration.value = 0;

		if(frm.start_dd && frm.start_mm && frm.start_yy){
			frm.itm_content_eff_start_datetime.value = frm.start_yy.value + "-" + frm.start_mm.value + "-" + frm.start_dd.value + " " + 00 + ":" + 00 + ":00"
		}else{
			frm.itm_content_eff_start_datetime.value = (frm.end_yy.value - 1) + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " " + 00 + ":" + 00 + ":00"
		}
		frm.itm_content_eff_end_datetime.value = frm.end_yy.value + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " " + 23 + ":" + 59 + ":59"
	}else{
		frm.itm_content_eff_start_datetime.value = "";

		frm.itm_content_eff_end_datetime.value = "";
		frm.itm_content_eff_duration.value = 0;
	}

	frm.url_success.value = "../htm/close_window.htm";
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_itm', 'itm_id', itm_id, 'itm_content_eff_start_datetime', frm.itm_content_eff_start_datetime.value, 'itm_content_eff_end_datetime', frm.itm_content_eff_end_datetime.value, 'itm_type', frm.itm_type.value, 'itm_upd_timestamp', frm.itm_upd_timestamp.value, 'url_success', frm.url_success.value, 'itm_content_eff_duration', frm.itm_content_eff_duration.value, 'upd_itm_content_ind', 'true');
	window.location.href = url;
}

function wbItemContentInfo(itm_id){
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_content_info.xsl');
	window.location.href = url;
}

function wbItemPerformanceInfo(itm_id){
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_performance_info.xsl');
	window.location.href = url;
}

function wbItemContentInfoURL(itm_id, is_new_cos){
	if(is_new_cos == null || is_new_cos==undefined){
		is_new_cos = false;
	}
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_content_info.xsl', 'is_new_cos', is_new_cos);
	return url;
}

function wbItemContentSched(itm_id){
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_content_sch_info.xsl');
	var applet_width = 790;
	var applet_height = 420;
	var str_feature = 'toolbar=' + 'no' + ',width=' + applet_width + ',height=' + applet_height + ',scrollbars=' + 'auto' + ',resizable=' + 'no' + ',screenX=' + '0' + ',screenY=' + '10' + ',left=' + '0' + ',top=' + '10';

	wbUtilsOpenWin(url, 'content_sched', false, str_feature);
}

function wbItemJIUpd(frm, itm_id, lang){
	var now_date = new Date();
	var cur_date = now_date.getDate();
	var cur_month = now_date.getMonth();
	var cur_year = now_date.getYear();
	var current_date = new Date(cur_year, cur_month, cur_date);
	frm.ji_no_change.value=$('input[name="ji_value"]:checked').val();
	frm.reminder_no_change.value=$('input[name="reminder_value"]:checked').val();
	if($('input[name="ji_value"]:checked').val() == 'new'){
		if(!wbUtilsValidateDate('document.frmXml.ji', eval('wb_msg_' + lang + '_ji_date'), '','ymdhm')) return false;
		if(!gen_validate_equal_and_greater_cur_time('document.frmXml.ji', eval('wb_msg_' + lang + '_ji_date'), lang)) return false;
		frm.ji_target_datetime.value = frm.ji_yy.value + "-" + frm.ji_mm.value + "-" + frm.ji_dd.value + " " + frm.ji_hour.value + ":" + frm.ji_min.value + ":59"
	}

	if($('input[name="reminder_value"]:checked').val() == 'new'){
		if(!wbUtilsValidateDate('document.frmXml.reminder', eval('wb_msg_' + lang + '_ji_reminder_date'), '','ymdhm')) return false;
		if(!gen_validate_equal_and_greater_cur_time('document.frmXml.reminder', eval('wb_msg_' + lang + '_ji_reminder_date'), lang)) return false;
		frm.ji_reminder_target_datetime.value = frm.reminder_yy.value + "-" + frm.reminder_mm.value + "-" + frm.reminder_dd.value + " " + frm.reminder_hour.value + ":" + frm.reminder_min.value + ":59"
	}

	if(frm.run_ind.value == 'true'){
		frm.url_success.value = _wbItemGenGetItemDetailURL(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false)
	}else{
		frm.url_success.value = _wbItemGetItemDetailURL(itm_id)
	}

	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_ji', 'itm_id', itm_id, 'ji_msg_id', frm.ji_msg_id.value, 'ji_target_datetime', frm.ji_target_datetime.value, 'ji_reminder_msg_id', frm.ji_reminder_msg_id.value, 'ji_reminder_target_datetime', frm.ji_reminder_target_datetime.value, 'url_success', frm.url_success.value, 'sender_ent_id', frm.sender_ent_id.value, 'cc_to_approver_ind', frm.cc_to_approver_ind.value, 'cc_to_approver_rol_ext_id', frm.cc_to_approver_rol_ext_id.value, 'msg_subject', frm.msg_subject.value, 'bcc_to', frm.bcc_to.value, 'reminder_msg_subject', frm.reminder_msg_subject.value, 'reminder_no_change', frm.reminder_no_change.value, 'ji_no_change', frm.ji_no_change.value);
	window.location.href = url;
}

function wbItemJIMsg(itm_id){
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_ji_info', 'itm_id', itm_id, 'stylesheet', 'itm_ji_msg.xsl');
	window.location.href = url;
}

function wbItemMakeApprovalActionExec(itm_id, action){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_make_itm_approval_actn', 'itm_id', itm_id, 'approval_action', action, 'url_success', self.location.href);
	window.location.href = url;
}
function wbInstrItemContentInfo(itm_id){
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_instr_content_info.xsl');
	window.location.href = url;
}

function wbItemGetCourseLessonInfo(itm_id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_lesson_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_course_lesson_info.xsl');
	window.location.href = url;
}

function wbItemGetRunLessonInfo(itm_id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_lesson_info', 'itm_id', itm_id, 'stylesheet', 'ae_get_run_lesson_info.xsl');
	window.location.href = url;
}

function wbItemUpdCourseLesson(frm, id, act_type){
	var url = "";
	var url_ = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_lesson_info', 'itm_id', frm.itm_id.value, 'stylesheet', 'ae_get_course_lesson_info.xsl');
	var method = "";
	if(act_type == "new"){
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_act_type', act_type ,'stylesheet', 'ae_upd_course_lesson.xsl');
	} else if(act_type == "edit") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type, 'stylesheet', 'ae_upd_course_lesson.xsl');
	} else if(act_type == "save") {
		var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type, 'stylesheet', 'ae_upd_course_lesson_info.xsl');
		frm.url_success.value = url_;
		frm.url_failure.value = url_;
		method="post";
	} else if(act_type == "delete") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type, 'stylesheet', 'ae_upd_course_lesson_info.xsl','url_success', window.location.href, 'url_failure', window.location.href, 'upd_timestamp', frm.upd_timestamp.value);
	}
	if(url != null && url != "" && method==""){
		window.location.href = url;
	}else{
		frm.action = url;
		frm.method = "post";
		frm.submit();
	}
}

function wbItemUpdRunLesson(frm, id, act_type,itm_id){
	var url_ = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_lesson_info', 'itm_id', frm.itm_id.value, 'stylesheet', 'ae_get_run_lesson_info.xsl');
	var url = "";
	var method = "";
	if (act_type == "new") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_act_type', act_type ,'stylesheet', 'ae_upd_course_lesson.xsl');
	} else if(act_type == "edit") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type, 'stylesheet', 'ae_upd_course_lesson.xsl');
	} else if(act_type == "save") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type, 'stylesheet', 'ae_upd_course_lesson.xsl');
		frm.url_success.value = url_;
		frm.url_failure.value = url_;
		method="post";
	} else if (act_type == "delete") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type,'url_success', url_, 'url_failure', url_, 'upd_timestamp', frm.upd_timestamp.value);
	} else if (act_type == "set_date") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type, 'stylesheet', 'ae_upd_lesson_date.xsl','url_success', url_, 'url_failure', url_);
	} else if (act_type == "set_date_save") {
		url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_upd_lesson', 'itm_id', frm.itm_id.value, 'ils_id', id, 'ils_act_type', act_type, 'stylesheet', 'ae_upd_lesson_date.xsl','url_success', url_, 'url_failure', url_);
		frm.url_success.value = url_;
		frm.url_failure.value = url_;
		method="post";
	} else if(act_type == "add_teacher"){
		var ils_id = id;
		url = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'instr_adv_search.xsl', 'ils_id', ils_id, 'itm_id',frm.itm_id.value,'s_tcr_id', frm.itm_tcr_id.value);
	}else if(act_type == "del_teacher"){
		var ils_id = id;
		url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 'stylesheet', 'ils_instr_lst.xsl', 'ils_id', ils_id,'remove_ils_instr','true', 'itm_id', frm.itm_id.value);
	}else if(act_type == "export_lesson"){
		var window_name = (new Date()).getTime();
		url = wb_utils_invoke_disp_servlet('cmd', 'GET_RPT', 'module','report.ReportModule', 'rpt_type', 'EXP_AEITEM_LESSON', 'rpt_name', 'aeItemLesson', 'ils_id', id,'ils_itm_id',itm_id, 'download', 4, 'window_name', window_name);
		window.location.href = url;
		return;
	}
	if(url != null && url != "" && method==""){
		window.location.href = url;
	}else{
		frm.action = url;
		frm.method = "post";
		frm.submit();
	}
}
function wbAssignInstr(frm){
	frm.action = wb_utils_ae_servlet_url;
	frm.cmd.value = 'setting_instr';
	frm.url_success.value = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_lesson_info', 'itm_id', frm.itm_id.value, 'stylesheet', 'ae_get_run_lesson_info.xsl');
	frm.url_failure.value = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_lesson_info', 'itm_id', frm.itm_id.value, 'stylesheet', 'ae_get_run_lesson_info.xsl');
	if(frm.ili_usr_ent_id_lst.type=='hidden'){
		frm.ili_usr_ent_id_lst.value=frm.checkbox_group.value;
	}else{
		for (var i=0;i<frm.checkbox_group.length;i++){
			if(frm.checkbox_group[i].checked){
				frm.ili_usr_ent_id_lst[i].value=frm.checkbox_group[i].value;
			}
		}
	}
	frm.method='get';
	frm.submit();
}
function wbRemoveInstr(frm){
	frm.action = wb_utils_ae_servlet_url;
	frm.cmd.value = 'remove_instr';
	frm.url_success.value = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_lesson_info', 'itm_id', frm.itm_id.value, 'stylesheet', 'ae_get_run_lesson_info.xsl');
	frm.url_failure.value = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_lesson_info', 'itm_id', frm.itm_id.value, 'stylesheet', 'ae_get_run_lesson_info.xsl');
	if(frm.ili_usr_ent_id_lst.type=='hidden'){
		frm.ili_usr_ent_id_lst.value=frm.checkbox_group.value;
	}else{
		for (var i=0;i<frm.checkbox_group.length;i++){
			if(frm.checkbox_group[i].checked){
				frm.ili_usr_ent_id_lst[i].value=frm.checkbox_group[i].value;
			}
		}
	}
	frm.method='get';
	frm.submit();
}

function _wbItemGetTcrId(frm) {
	var list = '';
	var idx = 0;
	var box_ele;
	var lst_ele;
	if(frm.tcr_id_lst_box && frm.tcr_id_lst) {
		box_ele = frm.tcr_id_lst_box;
		lst_ele = frm.tcr_id_lst
	} else if(frm.r_tcr_id_lst_box && frm.r_tcr_id_lst) {
		box_ele = frm.r_tcr_id_lst_box;
		lst_ele = frm.r_tcr_id_lst
	}
	for (idx = 0; idx < box_ele.options.length;idx++) {
		list += box_ele.options[idx].value;
		if(idx != (box_ele.length -1)){
			list += '~'
		}
	}
	lst_ele.value = list
}

function _wbItemGetCatId(frm) {
	var list = '';
	var idx = 0;
	var box_ele;
	var lst_ele;
	if(frm.tnd_id && frm.tnd_id_lst) {
		box_ele = frm.tnd_id;
		lst_ele = frm.tnd_id_lst
	}
	for (idx = 0; idx < box_ele.options.length;idx++) {
		list += box_ele.options[idx].value;
		if(idx != (box_ele.length -1)){
			list += '~'
		}
	}
	lst_ele.value = list
}

function UpdatePrerequisite(frm){
	frm.action = wb_utils_disp_servlet_url;
	frm.cmd.value = 'upd';
	frm.module.value = 'course.ModulePrerequisiteModule';
	frm.url_success.value = _wbItemGetItemDetailURL(frm.itm_id.value);
	//wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', frm.itm_id.value, 'stylesheet', 'ae_get_content_info.xsl');
	//frm.url_failure.value = wb_utils_invoke_disp_servlet('cmd', 'get', 'stylesheet', 'itm_mod_pre.xsl', 'itm_id', frm.itm_id.value,'module','course.ModulePrerequisiteModule' );

	
	frm.method='get';
	frm.submit();
	
}


function wbItemGetModPre(itm_id){
	url = wb_utils_invoke_disp_servlet('cmd', 'get', 'stylesheet', 'itm_mod_pre.xsl', 'itm_id', itm_id, 'module','course.ModulePrerequisiteModule');
	window.location.href = url;
}

function wbItemGetTargetRule(itm_id, itm_target_type, is_new_cos) {
	if(is_new_cos==null || is_new_cos==undefined){
		is_new_cos = 'false';
	}
	url = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'get_rule_lst', 'stylesheet', 'ae_item_target_rule_lst.xsl', 'itm_id', itm_id, 'itm_target_type', itm_target_type, 'is_new_cos', is_new_cos);
	window.parent.location.href = url;
}

function wbItemStartTargetRulePrev(itm_id, itm_target_type, rule_id, is_new_cos) {
	var url = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'set_rule_prev', 'stylesheet', 'ae_item_target_rule_content.xsl', 'itm_id', itm_id, 'itm_target_type', itm_target_type, 'rule_id', rule_id, 'is_new_cos', is_new_cos);
	window.location.href = url;
}

function wbItemSetTargetRulePrevUrl(itm_id, itm_target_type, rule_id, stylesheet, is_new_cos) {
	url = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'set_rule_prev', 'stylesheet', stylesheet, 'itm_id', itm_id, 'itm_target_type', itm_target_type, 'rule_id', rule_id, 'is_new_cos', is_new_cos);
	return url;
}

function wbItemSetTargetRuleExec(frm) {
	frm.action = wb_utils_disp_servlet_url;
	frm.cmd.value = 'set_rule_exec';
	frm.target = '_parent';
	frm.module.value = 'itemtarget.ItemTargetModule';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'get_rule_lst', 'stylesheet', 'ae_item_target_rule_lst.xsl', 'itm_id', frm.itm_id.value, 'itm_target_type', frm.itm_target_type.value, 'is_new_cos', frm.is_new_cos.value);
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'set_rule_prev', 'stylesheet','ae_item_target_rule_content.xsl', 'itm_id', frm.itm_id.value, 'itm_target_type', frm.itm_target_type.value, 'rule_id', frm.rule_id.value, 'is_new_cos', frm.is_new_cos.value);
	frm.method = 'post';
	var tmp_lst = "";
	if(frm.target_group) {
		for(var i=0; i<frm.target_group.options.length; i++){
			tmp_lst += frm.target_group.options[i].value;
			if(i != frm.target_group.options.length - 1){
					tmp_lst += ",";
			}
		}
		frm.target_group_lst.value = tmp_lst;
		tmp_lst = "";
		if(frm.target_group_lst.value == "") {
			window.top.Dialog.alert(wb_msg_usr_please_select_a + frm.lab_group.value);
			return false;
		}
		if(frm.group_ind){
			if(frm.group_ind.checked){
				frm.itr_group_ind.value = 1;
			}else{
				frm.itr_group_ind.value = 0;
			}
		}
	}
	if(frm.target_grade) {
		for(var i=0; i<frm.target_grade.options.length; i++){
			tmp_lst += frm.target_grade.options[i].value;
			if(i != frm.target_grade.options.length - 1){
					tmp_lst += ",";
			}
		}
		frm.target_grade_lst.value = tmp_lst;
		tmp_lst = "";
		if(frm.target_grade_lst.value == "") {
			window.top.Dialog.alert(wb_msg_usr_please_select_a + frm.lab_grade.value);
			return false;
		}
		
		if(frm.grade_ind){
			if(frm.grade_ind.checked){
				frm.itr_grade_ind.value = 1;
			}else{
				frm.itr_grade_ind.value = 0;
			}
		}

	}
	if(frm.skill){
		for(var i=0; i<frm.skill.options.length; i++){
			tmp_lst += frm.skill.options[i].value;
			if(i != frm.skill.options.length - 1){
					tmp_lst += ",";
			}
		}
		frm.target_skill_lst.value = tmp_lst;
		tmp_lst = "";
		if(frm.skill_ind){
			if(frm.skill_ind.checked){
				frm.itr_skill_ind.value = 1;
			}else{
				frm.itr_skill_ind.value = 0;
			}
		}
		
	}
	
	
	frm.submit();
}

function wbItemTargetRuleLstPreview(frm) {
	var tmp_lst = "";
	if(frm.target_group) {
		for(var i=0; i<frm.target_group.options.length; i++){
			tmp_lst += frm.target_group.options[i].value;
			if(i != frm.target_group.options.length - 1){
					tmp_lst += ",";
			}
		}
		frm.target_group_lst.value = tmp_lst;
		tmp_lst = "";
		if(frm.target_group_lst.value == "") {
			window.top.Dialog.alert(wb_msg_usr_please_select_a + frm.lab_group.value);
			return false;
		}
	}
	if(frm.target_grade) {
		for(var i=0; i<frm.target_grade.options.length; i++){
			tmp_lst += frm.target_grade.options[i].value;
			if(i != frm.target_grade.options.length - 1){
					tmp_lst += ",";
			}
		}
		frm.target_grade_lst.value = tmp_lst;
		tmp_lst = "";
		if(frm.target_grade_lst.value == "") {
			window.top.Dialog.alert(wb_msg_usr_please_select_a + frm.lab_grade.value);
			return false;
		}
	}
	if(frm.skill){
		for(var i=0; i<frm.skill.options.length; i++){
			tmp_lst += frm.skill.options[i].value;
			if(i != frm.skill.options.length - 1){
					tmp_lst += ",";
			}
		}
		frm.target_skill_lst.value = tmp_lst;
		tmp_lst = "";
	}

	var doc = parent.frames[1].document;
	for(var i = 0; i < doc.getElementsByTagName("div").length; i++){
		doc.getElementsByTagName("div")[i].style.visibility = 'hidden';
    }

	parent.document.getElementById("prview_target_rule").src = getPreviewTargetRuleUrl(frm.target_group_lst.value, frm.target_grade_lst.value,	frm.target_skill_lst.value);
}

function getPreviewTargetRuleUrl(target_group_lst, target_grade_lst, target_skill_lst) {
	url = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'preview_rule', 'stylesheet', 'ae_item_target_user_lst.xsl', 'target_group_lst', target_group_lst, 'target_grade_lst', target_grade_lst,'target_skill_lst',target_skill_lst);
	return url;
}

function wbItemDelTargetRule(itm_id, itm_target_type, rule_id, upd_timestamp) {
	if(confirm(wb_msg_confirm)) {
		var url_success = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'get_rule_lst', 'stylesheet', 'ae_item_target_rule_lst.xsl', 'itm_id', itm_id, 'itm_target_type', itm_target_type);
		var url_failure = url_success;
		url = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'del_rule', 'itm_id', itm_id, 'rule_id', rule_id, 'upd_timestamp', upd_timestamp, 'url_success', url_success, 'url_failure', url_failure);
		window.location.href = url;
	}
}

function wbItemSortTargetUser(order_by, sort_order){
	wb_utils_nav_get_urlparam('sort_col', order_by, 'sort_order', sort_order);
}

function wbItemChangeTarEnrolRule(frm, target_enrol_type, isClearAll) {
	if(frm.last_target_enrol_type.value != target_enrol_type || isClearAll) {
		var msg;
		if(target_enrol_type == 'TARGET_LEARNER') {
			msg = wb_change_to_targetlrn_rules;	
		} else {
			if(isClearAll) {
				msg = wb_del_all_custom_rules;
			} else {
				msg = wb_change_to_custom_rules;
			}
		}
		if(confirm(msg)) {
			var url_success = wb_utils_invoke_disp_servlet('module', 'itemtarget.ItemTargetModule', 'cmd', 'get_rule_lst', 'stylesheet', 'ae_item_target_rule_lst.xsl', 'itm_id', frm.itm_id.value, 'itm_target_type', frm.itm_target_type.value, 'target_enrol_type', frm.target_enrol_type.value);
			var url_failure = url_success;
			if(isClearAll) {
				frm.is_del_all.value = 'true';
			}
			frm.target_enrol_type.value = target_enrol_type;
			frm.action = wb_utils_disp_servlet_url;
			frm.module.value = 'itemtarget.ItemTargetModule';
			frm.cmd.value = 'change_target_enroll_type';
			frm.url_success.value = url_success;
			frm.url_failure.value = url_success;
			frm.method = 'post';
			frm.submit();		
		} else if(!isClearAll){
			var by_self = document.getElementById("by_self");
			var same_as_tarlrn = document.getElementById("same_as_tarlrn");
			if(by_self && same_as_tarlrn) {
				var change_first = by_self.checked ? by_self : same_as_tarlrn;
				var change_second = by_self.checked ? same_as_tarlrn : by_self;
				change_first.checked = !change_first.checked;
				change_second.checked = !change_second.checked;
			}
		}
	}
}

function wbGetItemPublish(itm_id, is_new_cos){
		if(is_new_cos == 'true'){
			stylesheet = 'itm_publish_new_cos.xsl';
		}else{
			stylesheet = 'itm_publish.xsl';
		}
	_wbItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_publish.xsl', '', '', false, false, false);
}


function wbGetItemAutoEnrol(itm_id){
	_wbItemGenGetItemDetail(itm_id, 'DETAIL_VIEW', 'itm_auto_enrol.xsl', '', '', false, false, false);
}

function wbUpdItemTargetLrnRef(frm, ref, is_new_cos,run_ind) {
	frm.action = wb_utils_ae_servlet_url;
	frm.method = 'post';
	var stylesheet;
	if(ref == 'item_status') {
		
		if(run_ind ='true'){
			stylesheet = 'itm_run_view.xsl';
		}else{
			stylesheet = 'itm_details.xsl';
		}
		
		
		frm.cmd.value = "ae_upd_itm_status";
	} else {
		stylesheet = 'itm_auto_enrol.xsl';
		frm.cmd.value = "ae_upd_auto_enrol_type";
	}
	frm.url_success.value = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', frm.itm_id.value, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', stylesheet, 'url_failure', '', 'tnd_id', '', 'prev_version_ind', false, 'show_run_ind', false, 'show_session_ind', false);
	frm.url_failure.value = frm.url_success.value;
	if(frm.itm_status) {
		if(!frm.itm_status.value){//如果沒有默认值
			for(var i=0; frm.item_status_ && i<frm.item_status_.length; i++) {
				if(frm.item_status_[i].checked){
					frm.itm_status.value = frm.item_status_[i].value;
					break;
				}
			}
		}
		if(frm.itm_status.value != 'OFF') {
			frm.itm_status_lst.value = 'ON';
		} else {
			frm.itm_status_lst.value = 'OFF';
		}
	}
	if(frm.itm_enroll_type) {
		for(var i=0; i<frm.itm_enroll_type.length; i++) {
			if(frm.itm_enroll_type[i].checked){
				frm.itm_enroll_type.value = frm.itm_enroll_type[i].value;
				break;
			}
		}
	}
	frm.submit();
}

function wbItemExamSortExamList(sort_col, sort_order){
	wb_utils_nav_get_urlparam('sort_col', sort_col, 'sort_order', sort_order);
}

function wbItemExamGetExamOnlineList(frm, itm_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'get_exam_online_lst', 
				'stylesheet', 'exam_online_app_lst.xsl', 'itm_id', itm_id);
	window.location.href = url;
}

function wbItemExamSentMsgPrep (lang, frm, isPause, isTerminate, itm_id) {
	var id_lst = _wbItemExamGetCheckBoxValue(lang, frm, 'exam_box');
	
	if (id_lst.length === 0) {
		alert(eval('wb_msg_' + lang + '_sel_lrn'));
        return;
	}
	
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'get_online_user_info', 'stylesheet', 'exam_send_msg_prep.xsl', 'itm_id', itm_id, 'ent_id_lst', id_lst, 'isPause', isPause, 'isTerminate', isTerminate,"isExcludes", "true");
	var str_feature = 'toolbar=' + 'no' 
					+ ',titlebar=' + 'no' 
					+ ',width=' + '520' 
					+ ',height=' + '360' 
					+ ',resizable=' + 'yes' 
					+ ',scrollbars=' + 'yes'
					+ ',status=' + 'no';
	wbUtilsOpenWin(url, 'examWin', false, str_feature);
}

function wbItemExamSentMsg(frm, txtFldName) {
    frm.url_success.value = location.href+'&isColse=true';
    								
    frm.msg_body.value = wbUtilsTrimString(frm.msg_body.value);
    if(!wbUtilsValidateEmptyField(frm.msg_body, txtFldName)) {
        return;
    }
    
    if(!_wbItemExamValidateMessage(frm.msg_body)) {
        return;
    }
    
    frm.isPause.value = 'false';
    frm.isTerminate.value = 'false';
	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";
	frm.cmd.value = 'send_msg_to_learner';
	frm.module.value = 'JsonMod.exam.ExamModule';
	frm.method='POST';
	frm.submit();
}

function wbItemExamTerminate(frm, txtFldName) {
    frm.url_success.value = '../htm/close_window.htm';
    								
    frm.msg_body.value = wbUtilsTrimString(frm.msg_body.value);
    if(!wbUtilsValidateEmptyField(frm.msg_body, txtFldName)) {
        return;
    }
    
    if(!_wbItemExamValidateMessage(frm.msg_body)) {
        return;
    }
    
    frm.isPause.value = 'false';
    frm.isTerminate.value = 'true';
    if(frm.markAsZero) {
        if(frm.markAsZero[0].checked) {
            frm.isMarkAsZero.value = 'true';
        } else {
            frm.isMarkAsZero.value = 'false';
        }
    }

	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";
	frm.cmd.value = 'submit_exam';
	frm.module.value = 'JsonMod.exam.ExamModule';
	frm.method='POST';
	frm.submit();
}

function _wbItemExamValidateMessage(msg_obj) {
	if (msg_obj && msg_obj.value.length > 1000) {
		alert(wb_msg_message_too_long);
		msg_obj.focus();
		return false;
	}
	return true;
}

function wbItemExamPause(frm, txtFldName) {
	frm.url_success.value = '../htm/close_window.htm';
    								
    frm.msg_body.value = wbUtilsTrimString(frm.msg_body.value);
    
    if(!wbUtilsValidateEmptyField(frm.msg_body, txtFldName)) {
        return;
    }
    
    if(!_wbItemExamValidateMessage(frm.msg_body)) {
        return;
    }
    
    frm.isPause.value = 'true';
    frm.isTerminate.value = 'false';

	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";
	frm.cmd.value = 'pause_exam';
	frm.module.value = 'JsonMod.exam.ExamModule';
	frm.method='POST';
	frm.submit();
}

function wbItemExamReleasePause (lang, frm, itm_id) {
	var id_lst = _wbItemExamGetCheckBoxValue(lang, frm, 'pause_box');
	
	if (id_lst.length === 0) {
		alert(eval('wb_msg_' + lang + '_sel_lrn'));
        return;
	}
	
	if (!confirm(wb_msg_confirm)) {
		return;
	}
	
	var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'release_pause_exam', 'itm_id', itm_id, 
					'ent_id_lst', id_lst, 'url_success', window.location.href);
	window.location = url;
	
}

function _wbItemExamGetCheckBoxValue(lang, frm, box_name) {
	var id_lst = '';
	var box_list = eval('frm.' + box_name);
	if (box_list && box_list.length === undefined) {
		box_list = [box_list];
	}
	
	for (var i = 0, box_obj; box_obj = box_list[i]; i++) {
		if (box_obj.checked) {
			if (id_lst.length > 0) {
				id_lst += '~'
			}
			id_lst += box_obj.value;
		}
	}

	return id_lst;
}

function _getCourseListUrl(itm_id) {
	return wb_utils_invoke_disp_servlet('module', 'integratedlrn.IntegratedLrnModule', 'cmd', 'get_course_list', 'stylesheet', 'integrated_course_lst.xsl', 'itm_id', itm_id);	
}

function _getConPreUrl(icd_type, itm_id, icc_id, icd_id) {
	return wb_utils_invoke_disp_servlet('module', 'integratedlrn.IntegratedLrnModule', 'cmd', 'set_consisted_course_prev', 'stylesheet', 'integrated_course_set.xsl', 'icd_type', icd_type, 'itm_id', itm_id, 'icc_id', icc_id, 'icd_id', icd_id);
}

function wbGetCourseList(itm_id) {
	var url = _getCourseListUrl(itm_id);
	window.location.href = url;
}

function wbSetConditionPrev(icd_type, itm_id, icc_id, icd_id) {
	var url = _getConPreUrl(icd_type, itm_id, icc_id, icd_id);
	window.location.href = url;
}

function wbSetConditionExec(frm) {
	var obj = frm.icd_type_radio;
	for(var i=0; i<obj.length; i++) {
		if(obj[i].checked) {
			frm.icd_type.value = obj[i].value;
			break;
		}
	}

	var tmp_value = "";
	var len = frm.itm_id_lst.length;
	for(var i=0; i<len; i++) {
		tmp_value += frm.itm_id_lst[i].value;
		if(i !== len - 1) {
			tmp_value += "~";
		}
	}
	frm.itm_condition_list.value = tmp_value;

	frm.icd_completed_item_count.value = wbUtilsTrimString(frm.icd_completed_item_count.value);
	if(wbUtilsValidateInteger(frm.icd_completed_item_count, frm.lab_878.value, frm.lab_870.value)) {
		if(len > 0) {
			if(Number(frm.icd_completed_item_count.value) > len) {
				alert(wb_msg_pls_enter_positive_integer_1 + frm.lab_878.value + wb_msg_enter_less_cnt1 + frm.lab_870.value + wb_msg_enter_less_cnt2);
				frm.icd_completed_item_count.focus();
			} else {
				frm.module.value = 'integratedlrn.IntegratedLrnModule';
				frm.cmd.value = 'set_consisted_course_exec';
				frm.url_failure.value = _getConPreUrl(frm.icd_type.value, frm.itm_id.value, frm.icc_id.value, frm.icd_id.value);
				frm.url_success.value = _getCourseListUrl(frm.itm_id.value);
				frm.submit();
			}
		} else {
			alert(wb_msg_please_specify + frm.lab_870.value);
		}
	}
}

function wbDelCondition(frm, icd_id) {
	if(confirm(wb_msg_confirm)) {
		frm.icd_id.value = icd_id;
		frm.module.value = 'integratedlrn.IntegratedLrnModule';
		frm.cmd.value = 'del_condition';
		frm.url_success.value = _getCourseListUrl(frm.itm_id.value);
		frm.url_failure.value = frm.url_success.value;
		frm.submit();
	}
}

function wbSetCriteria(frm) {
	if(wbUtilsValidatePositiveInteger(frm.icc_completed_elective_count, frm.lab_868.value)) {
		if(Number(frm.icc_completed_elective_count.value) > Number(frm.elective_total_cnt.value)) {
			alert(wb_msg_less_completed_cnt);
			frm.icc_completed_elective_count.focus();
		} else {
			frm.module.value = 'integratedlrn.IntegratedLrnModule';
			frm.cmd.value = 'set_criteria';
			frm.url_failure.value = _getCourseListUrl(frm.itm_id.value);
			frm.url_success.value = _getCourseListUrl(frm.itm_id.value);
			frm.submit();
		}
	}
}

function wbItemTargetLrnExport(itm_id) {

	  var dateVar = new Date();
		var window_name ="rpt_win"+dateVar.getTime();
		
		var spec_name = 'itm_id';
		var spec_value = itm_id ;
		var newurl = wb_utils_invoke_servlet('cmd', 'get_prof', 'rpt_type', 'EXP_TARGET_LRN',  'stylesheet', 'target_lrn_dl_progress.xsl', 'window_name', window_name, 'download', '4','spec_name', spec_name,'spec_value',spec_value);
		wb_utils_open_win(newurl, window_name+'export', 450, 150);
}

function wbItemShare(itm_id, flag) {
	var url = wb_utils_invoke_ae_servlet('cmd','share_itm','itm_id',itm_id,'itm_share_ind',flag,'url_success',document.location.href,'url_failure',document.location.href);
	//wb_utils_open_win(url,'', 450,150);
	window.location.href = url;
}

function wbItemEvaluationReport(res_id,mod_type){
	if(mod_type == undefined || mod_type == ''){
		mod_type = 'TST';
	}
	var url = wb_utils_invoke_servlet('cmd', 'itm_evaluation_report', 'stylesheet', 'itm_evaluation_results_list.xsl',
			'cur_page','1','pagesize','10','sortOrder','ASC','res_id', res_id,'mod_type',mod_type);
	window.location = url;
}