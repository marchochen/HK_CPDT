// ------------------ WizBank Application Object ---------------------
// Convention:
//   public functions : use "wbApplication" prefix
//   private functions: use "_wbApplication" prefix
// ------------------------------------------------------------
/* constructor */
function wbApplication(){

	//learner enrol couse
	this.get_application_form = wbApplicationGetForm
	this.get_app_form_url = wbApplicationGetFormUrl
	this.confirm_form = wbApplicationConfirmForm
	this.submit_application_form = wbApplicationSubmitForm
	this.get_application_status = wbApplicationGetStatus

	//CM/PC maintain approval
	this.get_application_list = wbApplicationGetApplicationList

	//WorkFlow Action
	this.process_application = wbApplicationProcessApplication
	this.process_lrn_application = wbApplicationProcessLrnApplication

	this.ins_comment_prep = wbApplicationAddCommentPrep
	this.ins_comment_exec = wbApplicationAddCommentExec
	this.ins_history_exec = wbApplicationInsHistoryExec
	this.cancel_ins_history_exec = wbApplicationCancelInsHistoryExec
	this.lrn_ins_history_exec = wbApplicationLrnInsHistoryExec
	this.lrn_ins_reason_exec = wbApplicationLrnInsReasonExec
	this.lrn_cancel_ins_history_exec = wbApplicationLrnCancelInsHistoryExec
	this.lrn_cancel_ins_reason_exec = wbApplicationLrnCancelInsReasonExec

	this.action_exec = wbApplicationActionExec
	this.lrn_action_exec = wbApplicationLrnActionExec

	this.multi_action_exec = wbApplicationMultiActionExec
	this.single_multi_action_exec = wbApplicationSingleMultiActionExec
	this.lrn_single_multi_action_exec = wbApplicationLrnSingleMultiActionExec
	this.dl_application_rpt = wbApplicationDownloadApplicationReport

	//Remove Application
	this.remove_multi_application = wbApplicationRemoveMultiApplication
	this.new_enrollment_exec = wbApplicationNewEnrollmentExec

	//Enrollment Assignment
	this.enrol_assignment = new EnrolAssignment()

	//new workflow
	this.get_workflow_lst = wbApplicationGetWorkflowList

	this.enrol_approval = new EnrolApproval()

	this.get_ent_id_lst = wbApplicationGetEntIdLst
	this.get_display_name_lst = wbApplicationGetDisplayNameLst

	//Edit comment histroy
	this.get_comment = wbApplicationEditCommentGet;
	this.upd_comment = wbApplicationEditCommentExe;
	//auto_enroll_ind
	this.auto_enroll_ind = wbAutoEnrollInd;
	//application conflict
	this.back_to_confirm =  wbBackToConfirm;
	this.confirm_cancel = wbConfirmCancel;

	//Filter Application
	this.adv_filter_prep = wbAdvFilterPrep;
	this.set_appn_simple_filter = wbSetAppnSimpleFilter;
	this.set_appn_adv_filter = wbSetAppnAdvFilter;
	this.set_appn_status_filter = wbAppnStatusFilter;
	this.appn_clear_filter = wbAppnClearFilter;
}

//new EnrolAssignment
function EnrolAssignment(){
	this.get_lrn_lst = wbApplicationEnrolAssignmentGetLrnLst
	this.adv_search_prep = wbApplicationEnrolAssignmentAdvSearchPrep
	this.adv_search_exec = wbApplicationEnrolAssignmentAdvSearchExec
	this.lrn_cos_srh_prep = wbApplicationEnrolAssignmentLrnCosSrhPrep
	this.lrn_cos_srh_exec = wbApplicationEnrolAssignmentLrnCosSrhExec
	this.assign_cos_exec = wbApplicationEnrolAssignmentAssignCosExec
}

//For Enrol Approval
function EnrolApproval(){
	this.get_lrn_info = wbApplicationEnrolApprovalGetLrnInfo
	this.get_lrn_history_info = wbApplicationEnrolApprovalGetLrnHistoryInfo
	this.process_enrol = wbApplicationEnrolApprovalProcessEnrol
	this.get_my_appn_approval_list = wbApplicationApprovalList;
}


// ------------------------------------------------------------
function wbApplicationEnrolApprovalGetLrnHistoryInfo(app_id){
	url = wb_utils_invoke_ae_servlet(
		'cmd', 'ae_process_appn',
		'app_id', app_id,
		'tvw_id', 'APPLY_VIEW',
		'app_tvw_id', 'DETAIL_VIEW',
		'frmAppr' ,'false',
		'stylesheet' , 'enrol_approval_history.xsl'
	)
	window.location.href = url;
	return;
}
function wbApplicationEnrolApprovalGetLrnInfo(app_id){
	url = wb_utils_invoke_ae_servlet(
		'cmd', 'ae_process_appn',
		'app_id', app_id,
		'tvw_id', 'APPLY_VIEW',
		'app_tvw_id', 'DETAIL_VIEW',
		'frmAppr' ,'false',
		'stylesheet' , 'enrol_approval.xsl'
	)
	window.location.href = url;
	return;
}
function wbApplicationEnrolApprovalProcessEnrol(frm, lang, app_id, upd_timestamp, process_id, status_id, action_id, fr, to, verb, require_action){

	if( require_action != 'true' ) {
		if(confirm (eval('wb_msg_' + lang + '_confirm')) ) {
			frm.method = 'post'
			frm.action = wb_utils_ae_servlet_url
			frm.cmd.value = 'ae_make_multi_actn'
			frm.app_id_lst.value = app_id
			frm.app_upd_timestamp_lst.value = upd_timestamp
			frm.process_id.value = process_id
			frm.status_id.value = status_id
			frm.action_id.value = action_id
			frm.fr.value = fr
			frm.to.value = to
			frm.verb.value = verb
			frm.url_success.value = wb_utils_invoke_ae_servlet(
							'cmd','ae_get_my_appn_approval_lst',
							'cur_page', '1',
							'aal_status' , 'PENDING',
							'stylesheet', 'enrollment_list.xsl'
						)
			frm.url_failure.value = window.location.href
			frm.submit()
			return;
		}
	} else {
		frm.url_success.value = wb_utils_invoke_ae_servlet(
									'cmd','ae_process_appn',
									'app_id',app_id,
									'tvw_id','APPLY_VIEW',
									'app_tvw_id','DETAIL_VIEW',
									'stylesheet', 'application_give_reason.xsl'
								)
		frm.url_failure.value = window.location.href
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_ins_actn'
		frm.app_id.value = app_id
		frm.process_id.value = process_id
		frm.action_id.value = action_id
		frm.status_id.value = status_id
		frm.fr.value = fr
		frm.to.value = to
		frm.verb.value = verb
		frm.method = "post"
		frm.submit();
		return;
	}

	return;
}


//added to get the workflow list
function wbApplicationGetWorkflowList() {
	var url_failure =  window.location.href;
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_workflow_lst', 'url_failure', url_failure, 'stylesheet', 'application_workflow_lst.xsl');
	window.location.href = url;
}

function wbApplicationNewEnrollmentExec(frm,lang,ent_id_lst,functionName,itm_id,run_itm_id,processEndFunction,auto_enroll_ind,isExclude){

	if (auto_enroll_ind == null || auto_enroll_ind == '') {
		auto_enroll_ind = 'false';
	}
	if (itm_id == null) {
		itm_id = getUrlParam("itm_id");
	}
	if (run_itm_id == null) {run_itm_id = getUrlParam("run_id");}

	if (ent_id_lst != null && ent_id_lst != "") {
		frm.ent_id_lst.value = ent_id_lst;
	}else {
		frm.ent_id_lst.value = _wbApplicationGetUserLst(frm,frm.ent_ids_lst);
	}

	if (frm.ent_id_lst.value == ''){
		alert(eval('wb_msg_' + lang + '_sel_usr'))
	}else{
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '500'
			+ ',height=' 				+ '300'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'yes';
	wb_utils_set_cookie("appn_usr_name","");
	wb_utils_set_cookie("current","");
	wb_utils_set_cookie("total","");
	wb_utils_set_cookie("lang",lang);
	url = "../htm/application_frame_window.htm?itm_id="+itm_id+"&lang="+lang+"&run_id="+run_itm_id+"&functionName="+functionName+"&processEndFunction="+processEndFunction+"&auto_enroll_ind="+auto_enroll_ind;
	if(isExcludes==true){
		url += '&isExcludes=true'
	}
	wbUtilsOpenWin(url,'application_new_enrollment_status', false, str_feature);
	}
}

function _wbApplicationGetUserLst(frm,obj){
	var str, ele, i, n
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

function _wbApplicationGetUserNmLst(frm,obj){
	var str, ele, i, n
	str = ''
	n = obj.options.length
	for (i=0; i < n; i++){
		if (obj.options[i].value != ""){
			str += obj.options[i].text + '~'
		}
	}
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}

function wbApplicationInsPopupSrchUsrLst(fld_name,rawIdLst,rawNameLst){
	var i, args, args2 , idLst, nameLst
	idLst = new Array()
	nameLst = new Array()

	args = new String(rawIdLst)
	args2 = new String(rawNameLst)
	idLst = args.split("~%~")
	nameLst = args2.split("~%~")

	for (i=0; i<idLst.length; i++){
		var j, tmpOpt, can_add, OptCnt
		tmpOpt = new Array();
		can_add = true;
		OptCnt = eval('document.frmXml.' + fld_name + '.options.length');

		tmpOpt[i] = new Option
		tmpOpt[i].text =  nameLst[i]
		tmpOpt[i].value = idLst[i]

		for (j=0; j<OptCnt; j++){
			if (eval('document.frmXml.' + fld_name + '.options[' + j + '].value') == tmpOpt[i].value){
				can_add = false
				break;
			}
		}
		if (can_add == true) {addOption(eval('document.frmXml.' + fld_name), tmpOpt[i]);}
	}
}
function wbApplicationRemoveMultiApplication(frm,lang){
	var app_id_lst = _wbApplicationGetMultiActionLst(frm)
	if(app_id_lst == ''){
		alert(eval("wb_msg_"+ lang +"_select_applicant"));
		return;
	}else{
		frm.app_id_lst.value = app_id_lst
		frm.app_upd_timestamp_lst.value = _wbApplicationGetMultiActionTimeStampeLst(frm)
		//
		url = window.location.href;
		index = url.indexOf('enl_psrh_lst_srhTime',0)
		if(index > 0){
			url = url.substring(0,index)
		}

		frm.url_success.value = url
		frm.url_failure.value = url
		frm.cmd.value = 'ae_rm_appn';
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		if(confirm(wb_msg_remove_enrollment_confirm)){
			frm.submit();
		}
	}
}

function wbApplicationGetForm(itm_id,ent_id,confirmMethod){
	if (confirmMethod == null || confirmMethod == "") {confirmMethod = '1';}
	url = wbApplicationGetFormUrl(itm_id,ent_id,confirmMethod)
	window.location.href = url;
}

function wbApplicationGetFormUrl(itm_id,ent_id,from_core5){

	stylesheet = 'application_simple_form.xsl';

	var url_failure = window.location.href
	url = wb_utils_invoke_ae_servlet(
		'cmd','ae_get_appn_form',
		'tvw_id','APPLY_VIEW',
		'app_tvw_id','APPLY_VIEW',
		'itm_id',itm_id,
		'ent_id',ent_id,
		'stylesheet',stylesheet,
		'url_failure',url_failure,
		'from_core5',from_core5
	)
	return url;
}

function wbApplicationConfirmForm(frm,lang,itm_id,hasLearningSolnFcn,p_itm_id){
	var url_uccess, url_failure

	url_failure = wb_utils_invoke_ae_servlet(
		'cmd','ae_get_appn_form',
		'tvw_id','APPLY_VIEW',
		'app_tvw_id','DETAIL_VIEW',
		'itm_id',itm_id,
		'ent_id',frm.ent_id.value,
		'stylesheet','application_simple_form.xsl'
	)

	if(ValidateFrm(frm)){

		frm.itm_id.value = itm_id
		frm.action = wb_utils_ae_servlet_url
		frm.method = 'post'
		frm.cmd.value = 'ae_direct_ins_appn'
		frm.tvw_id.value = 'APPLY_VIEW'
		frm.app_tvw_id.value = 'DETAIL_VIEW'
		frm.url_failure.value = "javascript:history.go(-2)"
		feed_param_value(frm)
		frm.app_xml.value = GenerateXML(frm)
		if(frm.from_core5){
			frm.from_core5.value = getUrlParam('from_core5');
			if(frm.from_core5.value == 'true'){
				frm.url_success.value = "javascript:window.opener.location.href = window.opener.location.href;self.close();"
			}
		}
		frm.submit();
	}
}

function wbApplicationSubmitForm(frm,lang,ent_id,url_success_page_variant){
		var url_success = ''
		switch(url_success_page_variant){
			case 1 :
				url_success = wb_utils_invoke_ae_servlet(
					'cmd','ae_lrn_soln',
					'usr_ent_id',ent_id,
					'item_type','CLASSROOM~SELFSTUDY~ODE~EDE~VIDEO',
					'stylesheet','lrn_soln_view_cos.xsl',
					'targeted_item_apply_method_lst',
					'compulsory~elective'
				)
				break;
			default :
				url_success = wb_utils_invoke_ae_servlet(
					'cmd','ae_get_itm',
					'itm_id',frm.itm_id.value,
					'tvw_id','LRN_VIEW',
					'stylesheet','itm_lrn_details.xsl',
					'url_failure','',
					'tnd_id','',
					'prev_version_ind',false,
					'show_run_ind',true,
					'show_session_ind',true
				);
				break;
		}
		frm.action = wb_utils_ae_servlet_url
		frm.method = 'post'
		frm.cmd.value = 'ae_ins_appn'
		frm.url_success.value = url_success
		frm.url_failure.value = wb_utils_invoke_ae_servlet(
			'cmd','ae_get_itm',
			'itm_id',frm.itm_id.value,
			'tvw_id','LRN_VIEW',
			'url_success',url_success,
			'url_failure','',
			'prev_version_ind','false',
			'show_run_ind','true',
			'show_session_ind','true',
			'stylesheet','itm_lrn_details.xsl'
		)
		frm.submit();
}


function wbApplicationGetStatus(app_id){
	url = _wbApplicationGetStatusUrl(app_id)
	parent.location.href = url;
}

function _wbApplicationGetStatusUrl(app_id){
	var url_failure = parent.location.href;
	url = wb_utils_invoke_ae_servlet("cmd","ae_get_appn_status","app_id",app_id,"stylesheet",'lrn_application_process_status.xsl');
	return url;
}


function wbApplicationGetApplicationList(app_process_status,itm_id,page,page_size,sort_by,order_by, clear_session){
	if (page != null) {
		wb_utils_set_cookie("page",page);
	}
	if (page_size != null) {
		wb_utils_set_cookie("page_size",page_size);
	}

	if (sort_by != null) {
		wb_utils_set_cookie("sort_by",sort_by);
	}
	if (order_by != null) {
		wb_utils_set_cookie("order_by",order_by);
	}
	if(clear_session == null){
		clear_session = false;
	}
	url = _wbApplicationGetApplicationListURL(app_process_status,itm_id,page,page_size,sort_by,order_by, clear_session)
	window.location.href = url;
}

function _wbApplicationGetApplicationListURL(app_process_status,itm_id,page,page_size,sort_by,order_by, clear_session){
	var url_failure =  wb_utils_gen_home_url()
	if(page_size == null || page_size == '') {
		if(wb_utils_get_cookie('page_size') != ""){
			page_size = wb_utils_get_cookie('page_size');
		} else {
			page_size = 100;
		}
	}
	if(page == null || page == '') {
		if(wb_utils_get_cookie('page') != ""){
			page = wb_utils_get_cookie('page');
		} else {
			page = 1;
		}
	}
	if(sort_by == null || sort_by == "") {
		if(wb_utils_get_cookie("sort_by") != ""){
			sort_by = wb_utils_get_cookie("sort_by");
		} else {
			sort_by= '';
		}
	}
	if(order_by == null || order_by == "") {
		if(wb_utils_get_cookie("order_by") != ""){
			order_by = wb_utils_get_cookie("order_by");
		} else {
 			order_by= '';
		}
	}
	if(app_process_status == null){app_process_status= '';}
	url = wb_utils_invoke_ae_servlet(
		'cmd','ae_get_appn_ps_lst',
		'itm_id',itm_id,
		'app_lst_page',page,
		'app_lst_page_size',page_size,
		'sort_by',sort_by,
		'order_by',order_by,
		'url_failure',url_failure,
		'clear_session', clear_session,
		'stylesheet','application_lst.xsl'
	)
	url += "&app_process_status=" + app_process_status;
	return url;
}

function wbApplicationProcessApplication(app_id,frmAppr){;
	url = _wbApplicationProcessApplicationURL(app_id,frmAppr)
	wb_utils_set_cookie('page',getUrlParam('page'));
	wb_utils_set_cookie('sort_by',getUrlParam('sort_by'));
	wb_utils_set_cookie('order_by',getUrlParam('order_by'));
	window.location.href = url;
}

function _wbApplicationProcessApplicationURL(app_id, frmAppr){

	url = wb_utils_invoke_ae_servlet(
		'cmd','ae_process_appn',
		'app_id',app_id,
		'tvw_id','APPLY_VIEW',
		'app_tvw_id','DETAIL_VIEW',
		'stylesheet', 'application_process.xsl'
	)
	if(frmAppr!=null) {url += "&frmAppr=" + frmAppr;}
	return url;
}

function wbApplicationProcessLrnApplication(app_id){
	url = _wbApplicationProcessLrnApplicationURL(app_id)
	window.location.href = url
}

function _wbApplicationProcessLrnApplicationURL(app_id){
	url = wb_utils_invoke_ae_servlet(
		'cmd','ae_process_appn',
		'app_id',app_id,
		'tvw_id','APPLY_VIEW',
		'app_tvw_id','DETAIL_VIEW',
		'stylesheet','lrn_application_process.xsl'
	)
	return url;
}

function wbApplicationEditCommentGet(ach_id, ach_aah_id){
	if (!ach_id){ach_id=''}
	if (!ach_aah_id){ach_aah_id=''}
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_comm','ach_id',ach_id,'ach_aah_id',ach_aah_id,'stylesheet','application_edit_comment.xsl');
	window.location.href = url;
}

function wbApplicationEditCommentExe(frm) {
	/*if (frm.ach_content.value.length > 2000) {
		alert(wb_msg_comment_too_long);
		frm.ach_content.focus();
	}
	else if (frm.ach_content.value.length == 0){
		alert(wb_msg_enter_remark);
	}*/
	if (getChars(frm.ach_content.value) > 400){
		Dialog.alert(eval('text_label_old.course_remarks')+eval('text_label_old.label_title_length_warn_400'));
		return;
	}
	else {
		frm.cmd.value = 'ae_upd_comm';
		//alert(frm.cmd.value+frm.ach_id.value+frm.ach_aah_id.value+frm.ach_content.value+frm.ach_upd_timestamp.value);
		frm.url_success.value = _wbApplicationProcessApplicationURL(frm.app_id.value);
		frm.url_failure.value = wb_utils_invoke_ae_servlet('cmd','ae_get_comm','ach_id',frm.ach_id.value,'ach_aah_id',frm.ach_aah_id.value,'stylesheet','application_edit_comment.xsl');
		frm.action = wb_utils_ae_servlet_url;
		frm.method = "post";
		frm.submit();
	}
}

function wbApplicationAddCommentPrep(app_id,app_update_timestamp){
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '500'
			+ ',height=' 				+ '500'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'yes'

	url = wb_utils_invoke_ae_servlet('cmd','ae_get_prof','app_id',app_id,'stylesheet','application_ins_comment.xsl','upd_timestamp',app_update_timestamp)
	wbUtilsOpenWin(url, 'ins_comment', false, str_feature);
}

function wbApplicationAddCommentExec(frm,lang){
	if (frm.content.value.length == 0){
		Dialog.alert(wb_msg_enter_remark);
		return;
	}
	if (getChars(frm.content.value) > 400){
		Dialog.alert(eval('text_label_old.course_remarks')+eval('text_label_old.label_title_length_warn_400'));
		return;
	}
	frm.upd_timestamp.value = getUrlParam('upd_timestamp')
	frm.app_id.value = getUrlParam('app_id')
	frm.cmd.value = 'ae_ins_comm'
	frm.url_success.value = '../htm/close_window.htm'
	frm.action = wb_utils_ae_servlet_url
	frm.method = "post"
	frm.submit()
	window.opener.location.href = window.opener.location.href;
}

function wbApplicationActionExec(frm,app_id,process_id,status_id,action_id,org_status,to_status,verb,frmAppr,next_status){
	if(next_status == 0){
		frm.url_success.value = _wbApplicationGetApplicationListURL('',frm.itm_id.value)
	}else{
		frm.url_success.value = _wbApplicationProcessApplicationURL(app_id,frmAppr)
	}
	frm.url_failure.value = _wbApplicationProcessApplicationURL(app_id,frmAppr)
	frm.action = wb_utils_ae_servlet_url
	frm.cmd.value = 'ae_make_multi_actn'
	frm.app_id_lst.value = app_id
	frm.process_id.value = process_id
	frm.action_id.value = action_id
	frm.status_id.value = status_id
	frm.fr.value = org_status
	frm.to.value = to_status
	frm.verb.value = verb
	frm.method = "post"
	frm.submit();
}

function wbApplicationLrnActionExec(frm,app_id,process_id,status_id,action_id,org_status,to_status,verb,lang, opennew){
	if(verb=="withdrawn")
		if(!confirm(eval("wb_msg_"+lang+"_confirm")))
			return;
	frm.url_success.value = _wbApplicationProcessLrnApplicationURL(app_id)
	frm.url_failure.value = _wbApplicationProcessLrnApplicationURL(app_id)
	frm.action = wb_utils_ae_servlet_url
	frm.cmd.value = 'ae_ins_actn'
	frm.app_id.value = app_id
	frm.process_id.value = process_id
	frm.action_id.value = action_id
	frm.status_id.value = status_id
	frm.fr.value = org_status
	frm.to.value = to_status
	frm.verb.value = verb
	frm.method = "post"
	if(opennew === true) {
		var win_name = app_id + "_" + process_id + "_" + status_id + "_" + action_id
		var str_feature = 'toolbar=' + 'no' + ',width=' + '780' + ',height=' + '400' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes';
		wbUtilsOpenWin("about:blank", win_name, false, str_feature);
		frm.target = win_name;
	}
	frm.submit();
}

function wbApplicationInsHistoryExec(frm,itm_id,frmAppr, lang){
	var _appr_app_process_status

	if (frm.content){
		if (frm.content.value.length > 2000){
			alert(eval("wb_msg_" + lang + "_enter_valid") + " \"" + eval("wb_msg_" + lang + "_app_comment_content") + "\"" );
			frm.content.focus();
			return false;
		}
	}
	frm.cmd.value = 'ae_ins_history'

	if (wb_utils_get_cookie('appr_app_process_status') != '') {
	_appr_app_process_status = wb_utils_get_cookie('appr_app_process_status');
	}else {
	_appr_app_process_status = '';
	}
	frm.url_success.value = _wbApplicationGetApplicationListURL(_appr_app_process_status,itm_id);

	frm.action = wb_utils_ae_servlet_url
	frm.method = "post"
	frm.submit();
}

function wbApplicationCancelInsHistoryExec(frm,frmAppr){
	var url = wb_utils_invoke_ae_servlet('cmd','ae_cancel_history','url_success',_wbApplicationProcessApplicationURL(frm.app_id.value,frmAppr))
	window.location.href = url;
}

function wbApplicationLrnCancelInsHistoryExec(frm){
	var url_success = "../htm/course_detail.htm?itm_id=" + frm.itm_id.value;
	var url = wb_utils_invoke_ae_servlet('cmd','ae_cancel_history','url_success',url_success)
	window.location.href = url;
	self.close();
}

function wbApplicationLrnCancelInsReasonExec(frm){
	url_success = wb_utils_invoke_ae_servlet(
						'cmd', 'ae_process_appn',
						'app_id', frm.app_id.value,
						'tvw_id', 'APPLY_VIEW',
						'app_tvw_id', 'DETAIL_VIEW',
						'stylesheet' , 'enrol_approval.xsl'
					)
	var url = wb_utils_invoke_ae_servlet('cmd','ae_cancel_history','url_success', url_success)
	window.location.href = url;
}

function _wbApplicationGenGetItemDetailURL(itm_id,tvw_id,stylesheet,url_failure,tnd_id,prev_version_ind,show_run_ind, show_session_ind){
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm','itm_id',itm_id,'tvw_id',tvw_id,'stylesheet',stylesheet,'url_failure',url_failure,'tnd_id',tnd_id,'prev_version_ind',prev_version_ind,'show_run_ind',show_run_ind,'show_session_ind',show_session_ind);
	return url;
}

function wbApplicationLrnInsReasonExec(frm) {
	if (frm.content) {
		frm.content.value = wbUtilsTrimString(frm.content.value);
	}
	frm.cmd.value = 'ae_ins_history'
	frm.url_success.value = wb_utils_invoke_ae_servlet(
					'cmd','ae_get_my_appn_approval_lst',
					'cur_page', '1',
					'aal_status' , 'PENDING',
					'stylesheet', 'enrollment_list.xsl'
				)
	frm.action = wb_utils_ae_servlet_url
	frm.method = "post"
	frm.submit();
}

function wbApplicationApprovalList(type){

	url = wb_utils_invoke_ae_servlet(
		'cmd','ae_get_my_appn_approval_lst',
		'cur_page', '1',
		'aal_status' , type,
		'stylesheet', 'enrollment_list.xsl'
	)
	window.location.href = url;
}

function wbApplicationLrnInsHistoryExec(frm){
	var success_itm_id = (frm.p_itm_id.value !== null && frm.p_itm_id.value !== "") ? frm.p_itm_id.value : frm.itm_id.value;
	frm.cmd.value = 'ae_ins_history';
	frm.url_success.value = _wbApplicationProcessLrnApplicationURL(frm.app_id.value)
	frm.action = wb_utils_ae_servlet_url;

	var url = $(frm).attr('action');
	$.post(url, $(frm).serialize(), function() {
		window.opener.location.href = window.opener.location.href;
		self.close();
	});
}

function wbApplicationMultiActionExec(frm,process_id,status_id,action_id,fr,to,verb,lang,isApprover,itm_id){
	frm.app_id_lst.value = _wbApplicationGetMultiActionLst(frm)
	if(frm.app_id_lst.value == ''){
		alert(eval("wb_msg_"+ lang +"_select_applicant"))
	}else{
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '450'
			+ ',height=' 				+ '200'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'yes';
	wb_utils_set_cookie("appn_usr_name","");
	wb_utils_set_cookie("current","");
	wb_utils_set_cookie("total","");

	frmAction.verb.value=verb
	frmAction.fr.value=fr
	frmAction.to.value=to
	url = "../htm/application_frame_window.htm?run_id="+itm_id+"&lang="+lang+"&process_id="+process_id+"&status_id="+status_id+"&action_id="+action_id+"&fr="+fr+"&to="+to+"&verb="+verb+"&functionName=doFeedParam&processEndFunction=reload_me";
	wbUtilsOpenWin(url,'application_enrollment_status', false, str_feature);
	}
}

function wbApplicationSingleMultiActionExec(frm,process_id,status_id,action_id,fr,to,verb,lang,isApprover,itm_id){
	if(confirm (eval('wb_msg_' + lang + '_confirm')) ) {
		frm.app_id_lst.value = frm.app_id.value
		if(frm.app_id_lst.value == ''){
			alert(eval("wb_msg_"+ lang +"_select_applicant"))
		}else{
			if(verb=="removed")
				if(!confirm(eval("wb_msg_"+lang+"_confirm")))
					return;
			frm.method = 'post'
			frm.action = wb_utils_ae_servlet_url
			frm.cmd.value = 'ae_make_multi_actn'
			frm.app_upd_timestamp_lst.value = frm.upd_timestamp.value
			frm.process_id.value = process_id
			frm.status_id.value = status_id
			frm.action_id.value = action_id
			frm.fr.value = fr
			frm.to.value = to
			frm.verb.value = verb
			frm.url_success.value =	_wbApplicationGetApplicationListURL('',itm_id)
			frm.url_failure.value =	_wbApplicationProcessApplicationURL(frm.app_id.value)
			frm.submit();
		}
	}
}

function wbApplicationLrnSingleMultiActionExec(frm,process_id,status_id,action_id,fr,to,verb,lang,isApprover,itm_id){
	frm.app_id_lst.value = frm.app_id.value
	if(frm.app_id_lst.value == ''){
		alert(eval("wb_msg_"+ lang +"_select_applicant"))
	}else{
		if(verb=="withdrawn")
			if(!confirm(eval("wb_msg_"+lang+"_confirm")))
				return;
		frm.method = 'post'
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_make_multi_actn'
		frm.app_upd_timestamp_lst.value = frm.upd_timestamp.value
		frm.process_id.value = process_id
		frm.status_id.value = status_id
		frm.action_id.value = action_id
		frm.fr.value = fr
		frm.to.value = to
		frm.verb.value = verb
		frm.url_success.value =	wb_utils_invoke_ae_servlet('cmd','ae_get_itm','itm_id',itm_id,'tvw_id','LRN_VIEW','stylesheet','itm_lrn_details.xsl','url_failure','','tnd_id','','prev_version_ind','false','show_run_ind','true', 'show_session_ind','true');
		frm.url_failure.value =	_wbApplicationProcessLrnApplicationURL(frm.app_id.value)
		frm.submit();
	}
}

function wbApplicationDownloadApplicationReport(frm,app_process_status,itm_id,wb_lang){
	var url_failure = parent.location.href
	var ent_id_lst = wbApplicationGetEntIdLst(frm);
	var app_id_lst = wbApplicationGetAppIdLst(frm);
	if( ent_id_lst ==""){
		alert(eval("wb_msg_"+ wb_lang +"_select_applicant"));
		return;
	}
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_appn_ps_lst','app_process_status',app_process_status,'itm_id',itm_id,'download','true','url_failure',url_failure,'stylesheet','application_dl_report.xsl')
	//url = wb_utils_invoke_ae_servlet('cmd','ae_get_appn_ps_lst','app_process_status',app_process_status,'ent_id_lst',ent_id_lst,'app_id_lst',app_id_lst,'itm_id',itm_id,'download','true','url_failure',url_failure,'stylesheet','application_dl_report.xsl')
	frm.app_id_lst.value = app_id_lst;
	frm.ent_id_lst.value = ent_id_lst;
	frm.method = 'POST';
	frm.action = url;
	frm.submit();
	//window.location.href = url;
}

function wbApplicationGetAppIdLst(frm) {
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='app_id') {
			if (ele.value !=""){
				str = str + ele.value + '~';
			}
		}
	}
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}

function wbApplicationGetEntIdLst(frm) {
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='app_id') {
			if (ele.value !=""){
				str = str + eval('frm.ent_id_' + ele.value +'.value') + '~';
			}
		}
	}
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}

function wbApplicationGetDisplayNameLst(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='app_id') {
			if (ele.value !=""){
				str = str + eval('frm.display_name_' + ele.value +'.value') + ', ';
			}
		}
	}
	if (str != "") {str = str.substring(0, str.length-2);}
	if (frm.appn_display_name){
		str = frm.appn_display_name.value;
	}
	return str;

}

function wbApplicationEnrolAssignmentGetLrnLst(ent_id,frm,lang){
		var s_usr_display_bil = "";
		if(frm != null){
			if(!gen_validate_empty_field(frm.s_usr_display_bil,eval('wb_msg_'+lang+'_search_field'),lang)){
				frm.s_usr_display_bil.focus();
				return;
			}
			s_usr_display_bil = frm.s_usr_display_bil.value;
		}
		var url = wb_utils_invoke_servlet("cmd","search_ent_lst","ent_id",ent_id,"s_role_types","","stylesheet",'enrol_assignment_lrn_lst.xsl',"s_usr_id","","s_usr_first_name_bil","","s_usr_last_name_bil","","s_usr_display_bil",s_usr_display_bil,"s_grade_lst_single","","s_grade","","s_usg_ent_id_lst","my_approval");
		parent.location.href = url;
}

function wbApplicationEnrolAssignmentAdvSearchPrep(){
	url = wb_utils_invoke_servlet('cmd','get_ent_lst','stylesheet','enrol_assignment_lrn_lst_srh.xsl')
	window.location.href = url;
}
function wbApplicationEnrolAssignmentAdvSearchExec(frm,lang){
	if (_wbApplicationEnrolAssignValidateSearchFrm(frm,lang)) {

		if (frm.ent_id && frm.s_group_lst) {
			if (frm.s_group_lst.options[0]) {frm.ent_id.value = frm.s_group_lst.options[0].value;}
		}
		if (frm.s_grade && frm.s_grade_lst.options[0]) {frm.s_grade.value = frm.s_grade_lst.options[0].value;}

		frm.action = wb_utils_servlet_url
		frm.stylesheet.value = 'enrol_assignment_lrn_lst.xsl'
		frm.cmd.value = 'search_ent_lst'
		frm.s_usg_ent_id_lst.value = "my_approval";
		frm.frmHP.value = "false";
		frm.method = "get"
		frm.submit()
	}
}

function wbApplicationEnrolAssignmentLrnCosSrhPrep(frm,lang){
	if(frm !=null && lang!=null && !_wbApplicationEnrolAssignValidateSearchFrm(frm,lang))
		return;
	var url = wb_utils_invoke_ae_servlet('cmd','get_itm_ref_data','stylesheet','enrol_assignment_lrn_cos_srh.xsl');
	var str_feature = ',width=' 				+ '760'
							+ ',height=' 				+ '500'
							+ ',scrollbars='			+ 'yes'
							+ ',screenX='				+ '10'
							+ ',screenY='				+ '10'
							+ ',status='					+ 'yes'
	wbUtilsOpenWin(url,"srhWin",false,str_feature);
}

function wbApplicationEnrolAssignmentLrnCosSrhExec(frm,lang,doValidate){
	if(((doValidate==null)||(doValidate!=null&&doValidate))&&!_wbApplicationEnrolAssignValidateSearchFrm(frm,lang))
		return;
	if(frm.match && frm.match.checked)
		frm.exact.value = "true";
	else
		frm.exact.value = "false";
	if(frm.show_run.length){
		if(frm.show_run[0].checked == true){
			frm.orderby.value = 'itm_title'
		}else{
			frm.orderby.value = 'p_itm_title'
		}
	}else{
		frm.orderby.value = 'itm_title'
	}
	frm.cmd.value = "ae_lookup_itm";
	frm.tvw_id.value = "LIST_VIEW";
	frm.tnd_id_lst.value = frm.catalog.options[frm.catalog.selectedIndex].value;
	if(frm.tnd_id_lst.value=="")
		frm.all_ind.value = "true";
	frm.type.value = frm.type.options[frm.type.selectedIndex].value;
	frm.stylesheet.value = 'enrol_assignment_lrn_cos_srh_res.xsl';
	frm.url_failure.value = wb_utils_invoke_ae_servlet('cmd','get_itm_ref_data','stylesheet','enrol_assignment_lrn_cos_srh.xsl');
	frm.method = "get";
	frm.action = wb_utils_ae_servlet_url;
	frm.submit();
}

function wbApplicationEnrolAssignmentAssignCosExec(frm,lang){
	if(!_wbApplicationEnrolAssignValidateSearchFrm(frm,lang))
		return;
	var itm_id_lst = "";
	var usr_id_lst = "";
	var usr_nm_lst = "";
	var ent_id_lst = "";
	wb_utils_set_cookie("appn_usr_name","");
	wb_utils_set_cookie("current","");
	wb_utils_set_cookie("total","");

	var itm_id = frm.itm_id_lst.value.split("~");
	//get usr_ent_id_lst & usr_id array
	if(opener.document.frmXml.usr_ent_id){
		var usr_id_lst = _wbApplicationEnrolAssignSearchGetLst(opener.document.frmXml.usr_ent_id,true);
		var usr_id = usr_id_lst.split("~");
	}else{
		var usr_id = new Array();
		usr_id[0] = opener.document.frmXml.usr_ent_id_lst.value;
	}
	//get usr_nm_lst Array
	if(opener.document.frmXml.usr_ent_id){
		var list = "";
		if(opener.document.frmXml.usr_ent_id.length){
			//more than 1 user selected
			for(var i=0;i<opener.document.frmXml.usr_ent_id.length;i++){
				if(opener.document.frmXml.usr_ent_id[i].checked){
					list += opener.document.frmXml.usr_ent_nm[i].value + "~";
				}
			}
		}else{
			//only 1 user selected
			if(opener.document.frmXml.usr_ent_id.checked){
				list = opener.document.frmXml.usr_ent_nm.value;
			}
		}
		list = _wbApplicationCutLstTail(list,"~")
		var usr_nm = list.split("~");
	}else{
		var usr_nm = new Array();
		usr_nm[0] = opener.document.frmXml.usr_ent_nm_lst.value;
	}
	for(var i=0; i<usr_id.length; i++){
		for(var j=0; j<itm_id.length; j++){
			itm_id_lst += itm_id[j] + "~";
			ent_id_lst += usr_id[i] + "~";
			usr_nm_lst += usr_nm[i] + "~";
		}
	}
	//cut tail
	frm.ent_id_lst.value = _wbApplicationCutLstTail(ent_id_lst,"~");
	frm.usr_nm_lst.value = _wbApplicationCutLstTail(usr_nm_lst,"~");
	frm.itm_id_lst.value = _wbApplicationCutLstTail(itm_id_lst,"~");
	//do New Enrolment
	wbApplicationNewEnrollmentExec(frm,lang,frm.ent_id_lst.value,'doFeedParam','','','')
}

function _wbApplicationCutLstTail(list,tail){
	if(list.indexOf(tail)>-1){
		list = list.substr(0,list.length-1)
	}
	return list;
}

// Private Functions ============================

function _wbApplicationEnrolAssVailidate(frm,run_num,lang){
	for (i = 1; i <= run_num; i++){
		if ( eval("frm.run_" + i + "_total.value") < 0 ){
			alert(eval('wb_msg_' + lang + '_app_ass_full'))
			return;
		}
	}
	return true;
}

function _wbApplicationGetEnrolItemIdList(frm,app_num){
	var i, n, ele, str,j,name
	str = ""
	for(j=1;j<=app_num;j++){
		ele = eval('frm.s' + j)
		my_val = '0'
		if ( eval("frm.disable_s" + j + ".value") != 'false' ){
			str += eval("frm.disable_s" + j + ".value") + '~'
		}else{
			if(ele.length){
				for(i=0;i<ele.length;i++){
					if(ele[i].checked){
						my_val = ele[i].value
						break;
					}
				}
				str+=my_val + '~'

			}else{
				if(ele.checked){
					str+=ele.value + '~'
				}else{
					str+= '0~'
				}
			}
		}
	}
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}

function _wbApplicationGetMultiActionLst(frm) {
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='app_id') {
			if (ele.value !="")
				str = str + ele.value + "~"
		}
	}
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}

function _wbApplicationGetMultiActionNameLst(frm) {
	var i, n, ele, str
	str = ""

	n = frm.app_id.length;
	if(n == 0) {
		if(frm.app_id.checked) {
			str = str + app_nm[0] + '~';
		}
	} else {
		for(i = 0; i < n; i++) {
			if(frm.app_id[i].checked) {
				str = str + app_nm[i-1] + '~';
			}
		}
	}
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}

function _wbApplicationGetMultiActionTimeStampeLst(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='app_id') {
			if (ele.value !="")
				str = str + eval("frm.app_upd_timestamp_"+ele.value+".value") + "~"
		}
	}

	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}

function _wbApplicationEnrolAssignSearchGetUserSStatusLst(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.name == "s_status_rad" && ele.checked) {
			if ( ele.value != "")
				str += ele.value + "~"
		}
	}
	if (str != "") {str = str.substring(0, str.length-1)}
	return str;
}


function _wbApplicationEnrolAssignValidateSearchFrm(frm,lang){
	if(frm.title){
		if (frm.title.value.length>50){
			alert("wb_msg_"+lang+"_title_not_longer");
			return false;
		}
	}

	if (frm.s_usr_id){
		if (frm.s_usr_id.value != ''){
			if (!gen_validate_usr_id(frm.s_usr_id,lang)) {return false;}
		}
	}


	if (frm.s_status) {
		frm.s_status.value = _wbApplicationEnrolAssignSearchGetUserSStatusLst(frm)
		if (frm.s_status.value == ""){
			alert(eval('wb_msg_'  + lang + '_sel_usr_s_status'))
			return false;
		}
	}
	if(frm.usr_ent_id_lst){
		if(frm.usr_ent_id) {
			frm.usr_ent_id_lst.value = _wbApplicationEnrolAssignSearchGetLst(frm.usr_ent_id,true);
			if(frm.usr_ent_id_lst.value == ""){
				alert(eval("wb_msg_"+lang+"_sel_usr"));
				return false;
			}
		} else {
			return false;
		}
	}
	if(frm.itm_id){
		frm.itm_id_lst.value = _wbApplicationEnrolAssignSearchGetLst(frm.itm_id,true);
		if(frm.itm_id_lst.value == ""){
			alert(eval("wb_msg_"+lang+"_select_course"));
			return false;
		}
	}
	return true;

}

function _wbApplicationEnrolAssignSearchGetLst(ele,checked){
	var list = "";
	if(ele.length){
		for(var i=0;i<ele.length;i++){
			if(checked==null || (checked && ele[i].checked) || (!checked && !ele[i].checked)){
				list += ele[i].value + "~";
			}
		}
	}else{
		if(checked==null || (checked && ele.checked) || (!checked && !ele.checked)){
			list = ele.value;
		}
	}
	if(list.indexOf("~")>-1){
		list = list.substr(0,list.length-1);
	}
	return list;
}


//auto_enroll_ind
function wbAutoEnrollInd(){
	url = wb_utils_invoke_ae_servlet('cmd', 'auto_enroll_ind', 'stylesheet', 'auto_enroll_ind.xsl');
	var str_feature = 'toolbar=' + 'no' + ',width=' + '480' + ',height=' + '200' + ',scrollbars=' + 'yes' + ',resizable=' + 'false' + ',status=' + 'no'
	if(document.all){
				str_feature += ',top=' + '100';
				str_feature += ',left=' + '100';
	 		}
	 wbUtilsOpenWin(url,'ind', false, str_feature);
	 return;
}
//application conflict
function wbBackToConfirm(frm) {
	lang = 	wb_utils_get_cookie("lang");
	frm.back_confirm.value = 'true';
	window.location.href = '../htm/application_submit_frame.htm?lang='+lang+'&back_confirm='+frm.back_confirm.value;
	return;
}
function wbConfirmCancel(frm) {
	frm.back_confirm.value = 'false';
	lang = 	wb_utils_get_cookie("lang");
	window.location.href = '../htm/application_submit_frame.htm?lang='+lang+'&back_confirm='+frm.back_confirm.value;
	return;
}

function wbAdvFilterPrep(itm_id){
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', itm_id, 'adv_filter', true, 'stylesheet', 'application_filter.xsl');
	window.location.href = url;
}

function wbSetAppnSimpleFilter(itm_id, frm){
	frm.filter_value.value = wbUtilsTrimString(frm.filter.value);
	frm.cmd.value = 'ae_set_appn_filter';
	frm.url_success.value = _wbApplicationGetApplicationListURL('',itm_id, '1');
	frm.filter_type.value = 'simple_filter';
	frm.action = wb_utils_ae_servlet_url;
	frm.method = 'post';
	frm.submit();
}

function wbAppnStatusFilter(status, itm_id){
	url_success = _wbApplicationGetApplicationListURL('',itm_id, '1');
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_set_appn_filter', 'filter_type', 'status_filter', 'app_process_status', status, 'url_success', url_success);
	window.location.href = url;
}


function wbAppnClearFilter(itm_id){
	url_success = _wbApplicationGetApplicationListURL('',itm_id, '1');
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_set_appn_filter', 'filter_type', 'clear_filter', 'url_success', url_success);
	window.location.href = url;
}

function wbSetAppnAdvFilter(itm_id, frm, lang){
	if(frm.s_usr_id){
		frm.s_usr_id.value = wbUtilsTrimString(frm.s_usr_id.value);
	}
	if(frm.s_usr_display_bil){
		frm.s_usr_display_bil.value = wbUtilsTrimString(frm.s_usr_display_bil.value);
	}

	if(frm.s_usr_gender) {
	    if (frm.usr_gender[0].checked) {
    		frm.s_usr_gender.value = frm.usr_gender[0].value;
	    }
	    if (frm.usr_gender[1].checked) {
    		frm.s_usr_gender.value = frm.usr_gender[1].value;
	    }
	}

	if(frm.s_usr_email){
		frm.s_usr_email.value = wbUtilsTrimString(frm.s_usr_email.value);
	}
	if(frm.s_usr_tel){
		frm.s_usr_tel.value = wbUtilsTrimString(frm.s_usr_tel.value);
	}
	//
	if(_wbUserGroupValidateSearchFrm(frm, lang)){
		if(frm.bday_from_yy && frm.bday_from_mm && frm.bday_from_dd){
			if(frm.bday_from_yy.value != '' && frm.bday_from_mm.value != '' && frm.bday_from_dd.value != '') frm.s_usr_bday_fr.value = frm.bday_from_yy.value + "-" + frm.bday_from_mm.value + "-" + frm.bday_from_dd.value + " 00:00:00";
		}

		if(frm.bday_to_yy && frm.bday_to_mm && frm.bday_to_dd){
			if(frm.bday_to_yy.value != '' && frm.bday_to_mm.value != '' && frm.bday_to_dd.value != '') frm.s_usr_bday_to.value = frm.bday_to_yy.value + "-" + frm.bday_to_mm.value + "-" + frm.bday_to_dd.value + " 23:59:59.0";
		}

		if(frm.jday_from_yy && frm.jday_from_mm && frm.jday_from_dd){
			if(frm.jday_from_yy.value != '' && frm.jday_from_mm.value != '' && frm.jday_from_dd.value != '') frm.s_usr_jday_fr.value = frm.jday_from_yy.value + "-" + frm.jday_from_mm.value + "-" + frm.jday_from_dd.value + " 00:00:00";
		}

		if(frm.jday_to_yy && frm.jday_to_mm && frm.jday_to_dd){
			if(frm.jday_to_yy.value != '' && frm.jday_to_mm.value != '' && frm.jday_to_dd.value != '') frm.s_usr_jday_to.value = frm.jday_to_yy.value + "-" + frm.jday_to_mm.value + "-" + frm.jday_to_dd.value + " 23:59:59.0";
		}

		for(i=0;i<=40;i++) {
		    var ex_yy = eval('frm.extension_' + i + '_from_yy');
		    var ex_mm = eval('frm.extension_' + i + '_from_mm');
		    var ex_dd = eval('frm.extension_' + i + '_from_dd');
		    if (ex_yy && ex_mm && ex_dd) {
    		    if(ex_yy.value != '' && ex_mm.value != '' && ex_dd.value !='') {
    		        var s_ext_fr = eval('frm.s_ext_extension_' + i + '_fr');
    		        s_ext_fr.value = ex_yy.value + "-" + ex_mm.value + "-" + ex_dd.value + " 00:00:00";
    		    }
		    }
		}

		for(i=0;i<=40;i++) {
		    var ex_yy = eval('frm.extension_' + i + '_to_yy');
		    var ex_mm = eval('frm.extension_' + i + '_to_mm');
		    var ex_dd = eval('frm.extension_' + i + '_to_dd');
		    if (ex_yy && ex_mm && ex_dd) {
    		    if(ex_yy.value != '' && ex_mm.value != '' && ex_dd.value !='') {
    		        var s_ext_to = eval('frm.s_ext_extension_' + i + '_to');
    		        s_ext_to.value = ex_yy.value + "-" + ex_mm.value + "-" + ex_dd.value + " 23:59:59.0";
    		    }
		    }
		}

		if(frm.ent_id && frm.s_group_lst){
			if(frm.s_group_lst.options[0]){
				frm.ent_id.value = frm.s_group_lst.options[0].value;
			}
		}

		if(frm.s_grade && frm.s_grade_lst.options[0]){
			frm.s_grade.value = frm.s_grade_lst.options[0].value;
		}

		if(frm.usr_group_lst && frm.usr_group_lst.options[0]){
			frm.ent_id.value = frm.usr_group_lst.options[0].value;
		}

		if(frm.s_idc_fcs && frm.s_idc_fcs_lst.options[0]){
			frm.s_idc_fcs.value = frm.s_idc_fcs_lst.options[0].value;
		}

		if(frm.s_idc_int && frm.s_idc_int_lst.options[0]){
			frm.s_idc_int.value = frm.s_idc_int_lst.options[0].value;
		}

		if (_wbAppnValidateFilterDate(frm, lang)){
			if(frm.upddate_from_yy && frm.upddate_from_mm && frm.upddate_from_dd){
				if(frm.upddate_from_yy.value != '' && frm.upddate_from_mm.value != '' && frm.upddate_from_dd.value != '')
					frm.upddate_fr.value = frm.upddate_from_yy.value + "-" + frm.upddate_from_mm.value + "-" + frm.upddate_from_dd.value + " 00:00:00";
			}

			if(frm.upddate_to_yy && frm.upddate_to_mm && frm.upddate_to_dd) {
				if(frm.upddate_to_yy.value != '' && frm.upddate_to_mm.value != '' && frm.upddate_to_dd.value != '')
					frm.upddate_to.value = frm.upddate_to_yy.value + "-" + frm.upddate_to_mm.value + "-" + frm.upddate_to_dd.value + " 23:59:59.0";
			}
		}else{
			return false;
		}

		var sort_by = frm.appn_sort_by.options[frm.appn_sort_by.selectedIndex].value
		var order_by = frm.appn_order_by.options[frm.appn_order_by.selectedIndex].value
		wb_utils_set_cookie("sort_by",sort_by);
		wb_utils_set_cookie("order_by",order_by);

		frm.app_process_status.value = frm.type_sel_frm.options[frm.type_sel_frm.selectedIndex].value;
		frm.filter_type.value = 'advanced_filter';
		frm.url_success.value = _wbApplicationGetApplicationListURL('',itm_id,'1','', sort_by, order_by);
		frm.url_failure.value = frm.url_success.value;
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_set_appn_filter'
		frm.method = "post"
		frm.submit()
	}
}

function _wbAppnValidateFilterDate(frm, lang){
	if( frm.upddate_from_yy && frm.upddate_from_mm && frm.upddate_from_dd ) {
		if(( frm.upddate_from_yy.value != "" && frm.upddate_from_yy.value != null)||
		   ( frm.upddate_from_mm.value != "" && frm.upddate_from_mm.value != null)||
		   ( frm.upddate_from_dd.value != "" && frm.upddate_from_dd.value != null )) {
			if(!wbUtilsValidateDate( 'frmXml.upddate_from',eval('wb_msg_upddate'))) {
				return false;
			}
		}
	}

	if( frm.upddate_to_yy && frm.upddate_to_mm && frm.upddate_to_dd ) {
		if(( frm.upddate_to_yy.value != "" && frm.upddate_to_yy.value != null )||
		   ( frm.upddate_to_mm.value != "" && frm.upddate_to_mm.value != null )||
		   ( frm.upddate_to_dd.value != "" && frm.upddate_to_dd.value != null )) {
			if(!wbUtilsValidateDate('frmXml.upddate_to',eval('wb_msg_upddate'))) {
				return false;
			}
		}
	}

	if(frm.upddate_from_yy && frm.upddate_from_mm && frm.upddate_from_dd && frm.upddate_to_yy && frm.upddate_to_mm && frm.upddate_to_dd && frm.upddate_from_yy.value !== '' && frm.upddate_to_yy.value !== '') {
		if(!wb_utils_validate_date_compare({
			frm : 'document.' + frm.name,
			start_obj : 'upddate_from',
			end_obj : 'upddate_to'
			})) {
			return false;
		}
	}
	return true;
}