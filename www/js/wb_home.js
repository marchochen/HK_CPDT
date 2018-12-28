// ------------------ wizBank Home object ------------------- 
// Convention:
//   public functions : use "wbHome" prefix 
//   private functions: use "_wbHome" prefix
// ------------------------------------------------------------ 
/* constructor */
function wbHome(){
	this.itm_lst = new wbHomeItem;
	this.usr = new wbHomeUserGroup
	this.app = new wbHomeApplication
	this.ann = new wbHomeAnnouncement
	this.cpty = new wbHomeCompetency	
}


// *** wbHomeItem ***
function wbHomeItem(){
	this.select_add_item_type_prep = wbHomeItemSelectAddItemTypePrep

	this.get_item_list = wbHomeItemGetItemList
	this.search_item_prep = wbHomeItemSearchItemPrep
	this.search_cos_main_has_run = wbHomeItemSearchCosMainHasRun
	this.get_item_detail = wbHomeItemGetItemDetail
	this.get_item_run_detail = wbHomeItemGetItemRunDetail
	this.get_item_lrn_detail = wbHomeItemGetItemLrnDetail
	this.search_cos_main_no_run = wbHomeItemSearchCosMainNoRun
	this.search_enr_list_has_run = wbHomeItemSearchEnrListHasRun
	this.search_coming_list_has_run = wbHomeItemSearchComingListHasRun
	this.search_coming_list_no_run = wbHomeItemSearchComingListNoRun

	this.get_cos_approval_lst = wbHomeItemGetCosApprovalLst
}

function wbHomeItemGetCosApprovalLst(){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'all_ind', 'true', 'exact', 'false', 'tvw_id', 'LIST_VIEW', 'stylesheet', 'itm_approval_lst.xsl', 'show_respon', 'true', 'filter_retire', 'true', 'type', '', 'orderby', 'p_itm_submit_timestamp', 'sortorder', 'desc', 'approval_status', 'PENDING_APPROVAL~PENDING_REAPPROVAL')
	window.location.href = url;
}

function wbHomeItemSelectAddItemTypePrep(){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_all_ity_form', 'stylesheet', 'ls_add_form_prep.xsl')
	window.location.href = url;
}

function wbHomeItemGetItemList(type){
	url_failure = 'javascript:wb_utils_gen_home()'

	if(type){
		if(type == "CLASSROOM") url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'all_ind', 'true', 'exact', 'false', 'type', type, 'stylesheet', 'itm_lst.xsl', 'tvw_id', '', 'show_run_ind', 'false', 'orderby', 'r_itm_upd_timestamp', 'sortorder', 'desc');
		else url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'all_ind', 'true', 'exact', 'false', 'type', type, 'stylesheet', 'itm_lst.xsl', 'tvw_id', 'LIST_VIEW', 'orderby', 'r_itm_upd_timestamp', 'sortorder', 'desc')
	}else{
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'all_ind', 'true', 'exact', 'false', 'type', '', 'stylesheet', 'itm_lst.xsl', 'tvw_id', 'LIST_VIEW', 'filter_retire', 'true', 'orderby', 'r_itm_upd_timestamp', 'sortorder', 'desc')
	}

	url += '&show_respon=true'
	window.location.href = url;
}

function wbHomeItemSearchItemPrep(tnd_id){
	if(tnd_id == null){
		tnd_id = '';
	}

	url = wb_utils_invoke_ae_servlet('cmd', 'get_itm_ref_data', 'stylesheet', 'itm_search.xsl', 'tnd_id', tnd_id)
	window.location.href = url;
}

function wbHomeItemSearchCosMainHasRun(curTime, itmType, showRunInd){
	if(showRunInd == null){
		showRunInd = 'true';
	}

	url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_no_run', 'true', 'show_orphan', 'true', 'show_respon', 'true', 'type', itmType, 'eff_from', curTime, 'eff_from_operator', ">=", 'orderby', 'r_itm_eff_start_datetime', 'sortorder', 'asc', 'page_size', '10', 'itm_life_status_not_equal_lst', 'CANCELLED', 'allow_null_datetime', 'true', 'show_run_ind', showRunInd);
	window.location.href = url;
}

function wbHomeItemGetItemDetail(itm_id){ wbHomeItemGenGetItemDetailUrl(itm_id, 'DETAIL_VIEW', 'itm_details.xsl', '', '', false, false) }

function wbHomeItemGetItemRunDetail(itm_id, progress){
	if(progress == 'CANCELLED'){
		wbHomeItemGenGetItemDetailUrl(itm_id, 'CANCELLED_VIEW', 'itm_run_view_cancel.xsl', '', '', false, false)
	}else if(progress == '' || progress == null){
		wbHomeItemGenGetItemDetailUrl(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false)
	}else{
		wbHomeItemGenGetItemDetailUrl(itm_id, 'DETAIL_VIEW', 'itm_run_view.xsl', '', '', false, false)
	}
}

function wbHomeItemGetItemLrnDetail(itm_id){ wbHomeItemGenGetItemDetailUrl(itm_id, 'LRN_VIEW', 'itm_lrn_details.xsl', '', '', false, true) }

function wbHomeItemGenGetItemDetailUrl(itm_id, tvw_id, stylesheet, url_failure, tnd_id, prev_version_ind, show_run_ind){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', itm_id, 'tvw_id', tvw_id, 'stylesheet', stylesheet, 'url_failure', url_failure, 'tnd_id', tnd_id, 'prev_version_ind', prev_version_ind, 'show_run_ind', show_run_ind);
	window.location.href = url;
}

function wbHomeItemSearchCosMainNoRun(curTime, itmType){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_respon', 'true', 'show_orphan', 'true', 'type', itmType, 'orderby', 'r_itm_id', 'sortorder', 'DESC', 'page_size', '10', 'allow_null_datetime', 'true');
	window.location.href = url;
}

function wbHomeItemSearchEnrListHasRun(curTime, itmType){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_no_run', 'false', 'show_respon', 'true', 'show_respon', 'true', 'type', itmType, 'appn_to', curTime, 'appn_to_operator', "<", 'filter_retire_or_in_process_att', 'true', 'orderby', 'r_itm_eff_end_datetime', 'page_size', '10', 'itm_life_status_not_equal_lst', 'CANCELLED', 'allow_null_datetime', 'true');
	window.location.href = url;
}

function wbHomeItemSearchComingListHasRun(curTime, itmType){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_no_run', 'true', 'show_orphan', 'true', 'show_respon', 'true', 'type', itmType, 'appn_to', curTime, 'appn_to_operator', ">=", 'orderby', 'r_itm_appn_end_datetime', 'page_size', '10', 'itm_life_status_not_equal_lst', 'CANCELLED', 'allow_null_datetime', 'true');
	window.location.href = url;
}

function wbHomeItemSearchComingListNoRun(curTime, itmType){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_lookup_itm', 'stylesheet', 'itm_search_result.xsl', 'tvw_id', 'LIST_VIEW', 'url_failure', window.location.href, 'all_ind', 'true', 'show_orphan', 'true', 'show_respon', 'true', 'type', itmType, 'orderby', 'r_itm_id', 'sortorder', 'desc', 'page_size', '10', 'allow_null_datetime', 'true');
	window.location.href = url;
}

//*** wbHomeUserGroup ***
function wbHomeUserGroup(){
	this.edit_usr_prep = wbHomeUserGroupEditUserPrep

	this.reg_usr_approval_lst = wbUserRegisterUserApprovalLst
	this.reg_usr_approval = wbUserRegisterUserApproval
}

function wbHomeUserGroupEditUserPrep(usr_id, stylesheet, ent_id, stype, stimestamp, lang){
	if(stylesheet == null || stylesheet == ''){
		stylesheet = 'usr_detail_upd.xsl';
	}

	if(stype != null) url = wb_utils_invoke_servlet('cmd', 'get_usr', 'ent_id', ent_id, 'stype', stype, 'stimestamp', stimestamp, 'stylesheet', stylesheet, 'usr_ent_id', usr_id)
	else url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', stylesheet, 'usr_ent_id', usr_id)

	window.location.href = url
}

function wbUserRegisterUserApprovalLst(cur_page, page_size, sort_col, sort_order, timestamp){
	if(sort_col == null || sort_col == ''){
		sort_col = 'usr_signup_date';
	}

	if(sort_order == null || sort_order == ''){
		sort_order = 'ASC';
	}

	if(cur_page == null || cur_page == ''){
		cur_page = '1';
	}

	if(page_size == null || page_size == ''){
		page_size = '10';
	}

	if(timestamp == null || timestamp == ''){
		timestamp = '';
	}

	var url
	url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 's_sort_by', sort_col, 's_order_by', sort_order, 's_usg_ent_id_lst', 'my_approval', 's_status', 'PENDING', 'stylesheet', 'usr_reg_approval_lst.xsl')
	window.location.href = url;
}

function wbUserRegisterUserApproval(usr_ent_id){
	var url

	url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', usr_ent_id, 'stylesheet', 'usr_reg_approval.xsl')
	window.location.href = url;
}

// *** wbHomeApplication ***
function wbHomeApplication(){
	this.process_application = wbHomeApplicationProcessApplication
	// added to new enrol assignment
	this.get_enrol_assignment_lrn_lst = wbHomeApplicationEnrolAssignmentGetLrnLst
	// added to get the workflow list
	this.get_workflow_list = wbHomeApplicationGetWorkflowList
	//enrollment approval by courses
	this.get_appr_cos_lst = wbHomeApplicationCourseList
	//approval list
	this.get_my_appn_approval_list = wbHomeApplicationApprovalList
	this.appn_approval_get_lrn_info = wbHomeApplicationEnrolApprovalGetLrnInfo
}

function wbHomeApplicationEnrolApprovalGetLrnInfo(app_id){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_process_appn', 'app_id', app_id, 'tvw_id', 'APPLY_VIEW', 'app_tvw_id', 'DETAIL_VIEW', 'frmAppr', 'false', 'stylesheet', 'enrol_approval.xsl')

	window.location.href = url;
	return;
}

function wbHomeApplicationApprovalList(type){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_my_appn_approval_lst', 'cur_page', '1', 'aal_status', type, 'stylesheet', 'enrollment_list.xsl')
	window.location.href = url;
}

function wbHomeApplicationCourseList(){
	var url_failure = ''

	url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_approver_appn_cos_lst', 'cur_page', '1', 'page_size', '15', 'sort_col', 'parent_itm_title', 'sort_order', 'ASC', 'url_failure', url_failure, 'stylesheet', 'appr_cos_lst.xsl')
	window.location.href = url;
}

/*** added to get the workflow list ***/
function wbHomeApplicationGetWorkflowList(){
	var url_failure = wb_utils_gen_home_url();

	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_workflow_lst', 'url_failure', url_failure, 'stylesheet', 'application_workflow_lst.xsl');
	window.location.href = url;
}

function wbHomeApplicationEnrolAssignmentGetLrnLst(ent_id){
	var url = wb_utils_invoke_servlet("cmd", "search_ent_lst", "ent_id", ent_id, "s_role_types", "", "stylesheet", 'enrol_assignment_lrn_lst.xsl', "s_usr_id", "", "s_usr_first_name_bil", "", "s_usr_last_name_bil", "", "s_usr_display_bil", "", "s_grade_lst_single", "", "s_grade", "", "s_usg_ent_id_lst", "my_approval");
	parent.location.href = url;
}

function wbHomeApplicationProcessApplication(app_id, frmAppr){
	url = wbHomeApplicationProcessApplicationURL(app_id, frmAppr)
	wb_utils_set_cookie('page', getUrlParam('page'));
	wb_utils_set_cookie('sort_by', getUrlParam('sort_by'));
	wb_utils_set_cookie('order_by', getUrlParam('order_by'));
	window.location.href = url;
}

function wbHomeApplicationProcessApplicationURL(app_id, frmAppr){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_process_appn', 'app_id', app_id, 'tvw_id', 'APPLY_VIEW', 'app_tvw_id', 'DETAIL_VIEW', 'stylesheet', 'application_process.xsl')

	if(frmAppr != null){
		url += "&frmAppr=" + frmAppr;
	}
	return url;
}

// *** wbHomeAnnouncement ***
function wbHomeAnnouncement(){
	this.sys_lst = wbHomeAnnouncementSystemAnnLst
	this.get_ann_dtl = wbHomeAnnouncementGetAnnDetail
	this.add_sys_ann_lst = wbHomeAnnouncementAddSysAnnLst
	this.upd_sys_ann_lst = wbHomeAnnouncementUpdSysAnnLst
	this.del_sys_ann_lst = wbHomeAnnouncementDelSystemAnnLst
}

function wbHomeAnnouncementSystemAnnLst(ann_type, msg_type, res_id, cur_page, page_size, sort_col, sort_order, timestamp, openWin, readonly){
	if(sort_col == null || sort_col == ''){
		sort_col = 'msg_begin_date';
	}

	if(sort_order == null || sort_order == ''){
		sort_order = 'DESC';
	}

	if(cur_page == null || cur_page == ''){
		cur_page = '1';
	}

	if(page_size == null || page_size == ''){
		page_size = '10';
	}

	if(timestamp == null || timestamp == ''){
		timestamp = '';
	}

	if(msg_type == null || msg_type == ''){
		msg_type = 'SYS';
	}

	if(openWin != null && openWin){
		str_feature = 'toolbar=' + 'no' + ',width=' + '780' + ',height=' + '400' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes'
	}
	
	if (!readonly) {
	    readonly = 'true';
	}

	if(openWin == null || openWin == '' || !openWin){
		url = wbHomeAnnouncementSystemAnnLstUrl(ann_type, msg_type, res_id, cur_page, page_size, sort_col, sort_order, timestamp, '', readonly)
		window.location.href = url;
	}else{
		url = wbHomeAnnouncementSystemAnnLstUrl(ann_type, msg_type, res_id, cur_page, page_size, sort_col, sort_order, timestamp, 'announ_lst_popup.xsl', readonly)
		wbUtilsOpenWin(url, "ann_lst", false, str_feature);
	}
}

function wbHomeAnnouncementGetAnnDetail(msg_id, msg_type, read_only){
	if(msg_type == null || msg_type == ''){
		msg_type = 'SYS';
	}

	str_feature = 'toolbar=' + 'no' + ',width=' + '480' + ',height=' + '200' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',status=' + 'no'
	url = wb_utils_invoke_servlet('cmd', 'get_msg', 'msg_type', msg_type, 'msg_id', msg_id, 'msg_readonly',read_only, 'stylesheet', 'announ_dtl.xsl')
	wbUtilsOpenWin(url, 'announcement', false, str_feature);
}

function wbHomeAnnouncementSystemAnnLstUrl(ann_type, msg_type, res_id, cur_page, page_size, sort_col, sort_order, timestamp, stylesheet){
	if(sort_col == null || sort_col == ''){
		sort_col = 'msg_begin_date';
	}

	if(sort_order == null || sort_order == ''){
		sort_order = 'DESC';
	}

	if(cur_page == null || cur_page == ''){
		cur_page = '1';
	}

	if(page_size == null || page_size == ''){
		page_size = '10';
	}

	if(timestamp == null || timestamp == ''){
		timestamp = '';
	}

	if(msg_type == null || msg_type == ''){
		msg_type = 'SYS';
	}

	if(stylesheet == null || stylesheet == ''){
		stylesheet = 'announ_lst.xsl';
	}

	if(ann_type == 'all'){
		url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', stylesheet, 'msg_type', msg_type, 'cur_page', cur_page,'sortCol', sort_col, 'sortOrder', sort_order, 'timestamp', timestamp)
		if(msg_type == 'RES' && res_id != null && res_id != ''){
			url += '&res_id=' + res_id;
		}
	} else if (ann_type == 'tcr_ann') {
		url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', stylesheet, 'msg_type', msg_type, 'cur_page', cur_page,'sortCol', sort_col, 'sortOrder', sort_order, 'timestamp', timestamp, 'msg_readonly', 'true', 'msg_show_all', 'true');
	} else{
		url = wb_utils_invoke_servlet('cmd', 'get_msg', 'stylesheet', stylesheet, 'msg_type', msg_type, 'cur_page', cur_page,'sortCol', sort_col, 'sortOrder', sort_order, 'timestamp', timestamp)
		if(msg_type == 'RES' && res_id != null && res_id != ''){
			url += '&res_id=' + res_id;
		}
	}
	return url;
}

function wbHomeAnnouncementAddSysAnnLst(msg_type, res_id, popup){
	wb_utils_set_cookie('url_prev', self.location.href)

	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'msg_type', msg_type, 'stylesheet', 'announ_ins_rte.xsl')

	if(msg_type == 'RES'){
		url += '&res_id=' + res_id
	}

	if(popup != null && popup){
		url += '&popup=true'
	}
	window.location.href = url
}

function wbHomeAnnouncementUpdSysAnnLst(msg_type, msg_id, res_id, popup){
	wb_utils_set_cookie('url_prev', self.location.href)
	url = wb_utils_invoke_servlet('cmd', 'get_msg', 'msg_type', msg_type, 'msg_id', msg_id, 'stylesheet', 'announ_upd_rte.xsl');
	if(msg_type == 'RES'){
		url += '&res_id=' + res_id
	}

	if(popup != null && popup){
		url += '&popup=true'
	}
	window.location.href = url
}

function wbHomeAnnouncementDelSystemAnnLst(msg_id, msg_type, res_id, msg_timestamp, lang){
	if(confirm(eval('wb_msg_' + lang + '_confirm'))){
		url = wb_utils_invoke_servlet('cmd', 'del_msg', 'msg_id', msg_id, 'msg_type', msg_type, 'res_id', res_id, 'msg_timestamp', msg_timestamp, 'url_success', self.location.href, 'url_failure', self.location.href)
		window.location.href = url;
	}
}

// *** wbHomeCompetency ***
function wbHomeCompetency(){
	this.get_ass_lst = wbHomeCompetencyGetAssessmentList;
	this.submit_ass_prep = wbHomeCompetencySubmitAssessmentPrep;
	this.get_skill_grp_lst = wbHomeCompetencyGetSkillGroupList;
	this.sk_gap_ana_prep = wbHomeCompetencySkillGapAnalysisPrep;
	this.submit_mod_ass_prep = wbHomeCompetencySubmitModuleAssessmentPrep;
	this.get_ass_res = wbCompetencyGetAssessmentResult;
	this.get_to_do_list = wbHomeCompGetAssignedAssessmentList;
}

function wbCompetencyGetAssessmentResult(asm_id, lang){
	var url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "get_ass_result", "asm_id", asm_id, "stylesheet", 'comp_ass_get_res.xsl', "from", "homepage");
	parent.location.href = url;
}

function wbHomeCompetencySubmitModuleAssessmentPrep(mod_type, mod_id, tpl_use, cos_id, lang, url_success, cookieNm){
	str_feature = 'toolbar=' + 'no' + ',width=' + '780' + ',height=' + '420' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',status=' + 'no'

	if(document.all){
		str_feature += ',top=' + '10' + ',left=' + '10'
	}else{
		str_feature += ',screenX=' + '10' + ',screenY=' + '10'
	}

	cmd = 'get_tst'
	wb_utils_set_cookie('isWizpack', 'false')
	gen_del_cookie(cookieNm);
	gen_del_cookie("quiz_time_left");
	gen_del_cookie("quiz_que_flg");
	gen_del_cookie("quiz_interaction");
	url = wb_utils_invoke_servlet('cmd', 'get_mod_status', 'mod_id', mod_id, 'mod_type', mod_type, 'tpl_use', tpl_use, 'cos_id', cos_id, 'url_failure', '../htm/close_window.htm', 'stylesheet', 'start_module.xsl', 'url_success', url_success)
	test_player = wbUtilsOpenWin('', 'test_player', false, str_feature);
	test_player.document.write('<font size="2" face="Arial">' + eval('wb_msg_' + lang + '_loading_msg') + '</font>');
	test_player.location.href = url;
	test_player.focus();
}

function wbHomeCompetencyGetAssessmentList(lang, prepared, notified, collected, resolved, timestamp){
	var url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "refresh_ass_list", "prepared", prepared, "collected", collected, "notified", notified, "resolved", resolved, "pagesize", "10", "cur_page", "1", "sort_by", "DESC", "order_by", "asm_eff_start_datetime", "stylesheet", 'comp_ass_get_list.xsl');

	if(timestamp != null && timestamp != "") url = setUrlParam("timestamp", timestamp, url);

	parent.location.href = url;
}

function wbHomeCompetencySubmitAssessmentPrep(asm_id, usr_ent_id){
	var url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "get_assessor_result", "asm_id", asm_id, "usr_ent_id", usr_ent_id, "stylesheet", 'comp_ass_submit_ass.xsl', "from", "homepage");
	parent.location.href = url;
}

function wbHomeCompetencyGetSkillGroupList(type, lang, cur_page, pagesize){
	if(cur_page == null || cur_page == "") cur_page = "1";

	if(pagesize == null || pagesize == "") pagesize = "10";

	if(type == null || type == "" || type == "list") var stylesheet = "cpty_skill_grp_lst.xsl";
	else if (type == "deleted") var stylesheet = "cpty_recycle_bin.xsl";

	var url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "get_comp_skill_content", "cur_page", cur_page, "pagesize", pagesize, "stylesheet", stylesheet);

	parent.location.href = url;
}

function wbHomeCompetencySkillGapAnalysisPrep(lang){
	wb_utils_set_cookie("sess_url", "");

	var url = wb_utils_invoke_disp_servlet("module", "competency.SkillGapModule", "cmd", "view_skill_gap", "stylesheet", 'sk_ana_compare.xsl');
	parent.location.href = url;
}

// *** wbHomeCompetence ***
function wbHomeCompetence(){
	this.get_ass_list = wbHomeCompGetAssessmentList

	this.show_usr_comp = wbHomeCompShowUserCompetency;
	this.get_all_skillgp = wbHomeCompGetAllSkillGp;
	this.send_ass_prep = wbHomeCompSendAssessmentPrep;
}

function wbHomeCompGetAssignedAssessmentList(prepared, notified, collected, resolved){
	url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "get_assigned_ass", "stylesheet", 'comp_assigned_ass_list.xsl');
	window.location.href = url;
}

function wbHomeCompGetAssessmentList(prepared, notified, collected, resolved){
	url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "refresh_ass_list", "prepared", prepared, "collected", collected, "notified", notified, "resolved", resolved, "pagesize", "10", "cur_page", "1", "sort_by", "asc", "order_by", "asm_eff_start_datetime", "stylesheet", 'comp_ass_get_list.xsl');
	parent.location.href = url;
}

function wbHomeCompShowUserCompetency(usr_ent_id, frm, id_type){
	if(usr_ent_id == null || frm == null || id_type == null){
		url = wb_utils_invoke_disp_servlet("module", "competency.SkillGapModule", "cmd", "view_usr_skill", "stylesheet", 'sk_ana_show_comp.xsl');
	}else{
		if(id_type == "USR"){
			for (i = 0; i < frm.asm_type.length; i++)
				if (frm.asm_type[i].checked) url = wb_utils_invoke_disp_servlet("module", "competency.SkillGapModule", "cmd", "view_usr_skill", "usr_ent_id", usr_ent_id, frm.asm_type[i].value, eval("frm." + frm.asm_type[i].value + ".value"), "stylesheet", 'sk_ana_show_comp.xsl');
		}else{
			resolved_date = wbHomeCompGetDate(frm, "cur_date");
			url = wb_utils_invoke_disp_servlet("module", "competency.SkillGapModule", "cmd", "view_usr_skill", "usr_ent_id", usr_ent_id, "resolved_date", resolved_date, "stylesheet", 'sk_ana_show_comp.xsl');
		}
	}
	parent.location.href = url;
}

function wbHomeCompGetDate(frm, date_nm){
	cur_date_yy = eval("frm." + date_nm + "_yy.value");
	cur_date_mm = eval("frm." + date_nm + "_mm.value");
	cur_date_dd = eval("frm." + date_nm + "_dd.value");

	if(cur_date_yy == "" || cur_date_mm == "" || cur_date_dd == "") return eval("frm." + date_nm + ".value");

	cur_date = cur_date_yy + "-" + cur_date_mm + "-" + cur_date_dd + " 23:59:59.00";
	return cur_date;
}

function wbHomeCompGetAllSkillGp(cur_page, lang){
	url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "get_node_list", "cur_page", cur_page, "pagesize", "10", "stylesheet", 'all_comp.xsl');
	window.location.href = url;
}

function wbHomeCompSendAssessmentPrep(asm_id, usr_ent_id){
	url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "get_assessor_result", "asm_id", asm_id, "usr_ent_id", usr_ent_id, "stylesheet", 'comp_ass_submit_ass.xsl');
	wb_utils_set_cookie("url_prev", parent.location.href);
	parent.location.href = url;
}
