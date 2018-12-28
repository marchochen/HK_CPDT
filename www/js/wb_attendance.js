// ------------------ wizBank Attendance object ------------------- 
// Convention:
//   public functions : use "wbAttendance" prefix 
//   private functions: use "_wbAttendance" prefix
// ------------------------------------------------------------ 

/* constructor */
function wbAttendance(){
	this.usr_lst = wbAttendanceUserLst
	this.get_status = wbAttendanceGetStatus
	this.chg_status_exec = wbAttendanceChangeStatusExec
	this.chg_usr_status = wbAttendanceChangeUserStatus
	this.usr_status = wbAttendanceUserStatus
	this.chg_usr_status_exec = wbAttendanceChangeUserStatusExec
	this.upd_remark = wbAttendanceUpdRemark
	this.auto_update = wbAttendanceAutoUpdate
	this.export_usr_lst = wbAttendanceExportUserLst
	this.cmt_maintain = wbCourseMeasurement
	this.dl_att_rate_lst = wbAttendanceExportAttendanceRateLst
	this.chg_attd_rate_prep = wbAttendanceChangeAttdRatePrep
	this.chg_attd_rate_exec = wbAttendanceChangeAttdRateExec
	this.get_attd_rate_lst = wbAttendanceGetAttdRateList
	this.get_grad_record = wbAttendanceGradRecord
	this.get_finished_record = wbAttendanceFinishedRecord
	this.get_report_card =wbAttendanceGetReportCard
	this.chg_completion_date =wbAttendanceChangeCompletionDateExec
	this.search_user_list=wbAttendanceSearchUser
	this.search_user_list_rate=wbAttendanceSearchUserForRate
	this.get_qiandao_Lst = wbAttendanceGetQiandaoLst
}


function wbAttendanceGetQiandaoLst(itm_id, ils_id){
	var url = wb_utils_invoke_disp_servlet('cmd','ae_get_qiandao_lst','module','course.EvalManagementModule','stylesheet','attendance_qiandao_lst.xsl','itm_id',itm_id,'ils_id',ils_id);
	window.location.href = url;
}

/* public functions */
function wbAttendanceUserLst(itm_id){
	/*var str_feature = 'toolbar=' + 'no' + ',width=' + '780' + ',height=' + '420' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',status=' + 'yes';

	if(sort_col == null || sort_col == '') sort_col = 'user';

	if(sort_order == null || sort_order == '') sort_order = 'ASC';

	if(cur_page == null || cur_page == '') cur_page = '0';

	if(timestamp == null) timestamp = '';

	if(page_size == null || page_size == '') page_size = '100';

	if(att_status == null || page_size == '') att_status = '';

	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_maintain_attendance', 'itm_id', itm_id, 'att_status', att_status, 'sort_col', sort_col, 'sort_order', sort_order, 'cur_page', cur_page, 'timestamp', timestamp, 'page_size', page_size, 'stylesheet', 'attendance_lst.xsl')
	cos_lrn_lst = window.open(url, 'cos_lrn_lst', str_feature);*/
	var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_cmt_maintain', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_maintain.xsl');
	window.location.href = url;
}


function wbAttendanceExportUserLst(frm, itm_id, att_status, timestamp,lang, sort_col, sort_order){
	if(sort_order == 'ASC'){
		sort_order = 'DESC';
	} else if(sort_order == 'DESC'){
		sort_order = 'ASC';
	}
	if(timestamp == null) timestamp = '';
	frm.app_id_lst.value = _wbAttendanceGetAppIdCheckLst(frm);
	if(frm.app_id_lst.value == ''){
		alert(eval('wb_msg_' + lang + '_sel_usr'))
	}else{
		var url = wb_utils_invoke_ae_servlet('cmd', 'ae_maintain_attendance', 'itm_id', itm_id, 'att_status', att_status, 'download', 'true', 'sort_col', sort_col, 'sort_order', sort_order, 'cur_page', '0', 'timestamp', timestamp, 'stylesheet', 'attendance_dl_report.xsl')
		frm.method = 'POST';
		frm.action = url;
		frm.submit();
	}
}

function wbAttendanceExportAttendanceRateLst(itm_id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_maintain_attendance', 'itm_id', itm_id, 'att_status', '-1', 'download', 'true', 'sort_col', 'user', 'sort_order', 'ASC', 'cur_page', '0','stylesheet', 'attendance_rate_dl_report.xsl')
	self.location.href = url;
}


function wbAttendanceGetStatus(status_id){
	var url = self.location.href;
	url = setUrlParam('sort_col', 'att_timestamp', url)
	url = setUrlParam('sort_order', 'DESC', url)
	url = setUrlParam('att_status', status_id, url)
	url = setUrlParam('cur_page', '1', url);
	self.location.href = url;
}

function wbAttendanceChangeStatusExec(frm, lang, att_status){
	frm.app_id_lst.value = _wbAttendanceGetAppIdCheckLst(frm)
	if(frm.app_id_lst.value == ''){
		alert(eval('wb_msg_' + lang + '_sel_usr'))
	}else{
		frm.att_status.value = att_status
		if(frm.att_status.selectedIndex != 0){
			var str_feature = 'toolbar=' + 'no' + ',width=' + '400' + ',height=' + '180' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'yes';

			wb_utils_set_cookie("appn_usr_name", "");
			wb_utils_set_cookie("current", "");
			wb_utils_set_cookie("total", "");
			var url = "../htm/application_frame_window.htm?lang=" + lang + "&run_id=" + frm.itm_id.value + "&processEndFunction=" + "reload_me";
			wbUtilsOpenWin(url, 'attendance_status'+frm.itm_id.value, false, str_feature);
		}
	}
}

function wbAttendanceChangeUserStatus(app_id, itm_id, att_status){
	var str_feature= 'toolbar=' + 'no' + ',width=' + '500' + ',height=' + '500' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'no';
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_att', 'app_id', app_id, 'stylesheet', 'attendance_usr_rmk.xsl', 'att_status', att_status, 'itm_id', itm_id);
	wbUtilsOpenWin(url, 'chg_usr_status', false, str_feature);
}

function wbAttendanceUserStatus(app_id, itm_id, att_status){
	var str_feature= 'toolbar=' + 'no' + ',width=' + '500' + ',height=' + '150' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'no';
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_att', 'app_id', app_id, 'stylesheet', 'attendance_usr_status.xsl', 'att_status', att_status, 'itm_id', itm_id)
	wbUtilsOpenWin(url, 'usr_status', false, str_feature);
}

function wbAttendanceUpdRemark(frm, lang){
	frm.app_id_lst.value = _wbAttendanceGetAppIdLst(frm);
	frm.att_remark_lst.value = _wbAttendanceGetAppRemarkLst(frm);
	frm.att_update_timestamp_lst.value = _wbAttendanceGetAppUpdTimestampLst(frm);
	frm.url_failure.value = self.location.href;
	frm.url_success.value = self.location.href;
	frm.cmd.value = 'ae_upd_att_remark';
	frm.action = wb_utils_ae_servlet_url;
	frm.method = 'post';
	frm.submit();
}

function wbAttendanceChangeUserStatusExec(frm, lang){
	if(frm.remark.value.length > 200){
		alert(eval('wb_msg_' + lang + '_attd_remark'))
		frm.remark.focus();
	}else{
		if(frm.ict_id_list && frm.ict_id){
			frm.ict_id_list.value = _wbAttendanceGetChangeUserStatusIctId(frm, lang);
		}

		if(frm.icv_value_list){
			if(!_wbAttendanceValidateCreditValue(frm, lang)) return;
			frm.icv_value_list.value = _wbAttendanceGetChangeUserStatusIcvValue(frm, lang);
		}

		frm.url_failure.value = self.location.href;
		frm.url_success.value = '../htm/close_window.htm';
		frm.cmd.value = 'ae_upd_att';
		frm.action = wb_utils_ae_servlet_url;
		frm.method = 'post';
		frm.submit();
	}
}

function p_time(s) {
	if(s.length==2)
	{
		return s;
	}
    return s < 10 ? '0' + s: s;
}

function wbAttendanceChangeCompletionDateExec(frm, lang){
	var yy=frm.completion_date_yy.value;
	var mm=frm.completion_date_mm.value;
	var dd=frm.completion_date_dd.value;
	var hour=frm.completion_date_hour.value;
	var min=frm.completion_date_min.value;
	txtFldName = wb_msg_ils_orderdate_required;
	
	if(yy=='' && mm=='' && dd=='' && hour=='' && min==''){
		frm.att_timestamp.value ='';
	}else{
		if(!wbUtilsValidateDate('document.frmXml.completion_date',txtFldName,'','ymdhm')){
			return;
		}
		frm.att_timestamp.value = yy+"-"+p_time(mm)+"-"+p_time(dd)+" "+hour+":"+min+":00.0000";
	}
	
	frm.remark.value = wbUtilsTrimString(frm.remark.value);
	if(getChars(frm.remark.value) > 400){
		Dialog.alert(fetchLabel("course_remarks") + fetchLabel("label_title_length_warn_400"),function(){
			frm.remark.focus();
		});
		return;
	}
	frm.url_failure.value = '../htm/close_and_reload_window.htm';
	frm.url_success.value = '../htm/close_and_reload_window.htm';
	frm.cmd.value = 'ae_upd_att_timestamp';
	frm.action = wb_utils_ae_servlet_url;
	frm.method = 'post';
	frm.submit();
}

function wbAttendanceAutoUpdate(itm_id){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_auto_upd_attendance', 'itm_id', itm_id, 'url_success', self.location.href)
	window.location.href = url;
}

function wbAttendanceChangeAttdRatePrep(frm,lang,att_rate_status){
	frm.app_id_lst.value = _wbAttendanceGetAppIdCheckLst(frm)
	if(frm.app_id_lst.value == ''){
		alert(eval('wb_msg_' + lang + '_sel_usr'))
	}else{
		var str_feature = 'toolbar=' + 'no' + ',width=' + '600' + ',height=' + '430' + ',scrollbars=' + 'yes' + ',resizable=' + 'no' + ',status=' + 'yes';
		var url = wb_utils_invoke_disp_servlet('cmd','prep_upd_multi_att_rate','module','course.EvalManagementModule','stylesheet','attendance_rate_remark.xsl','itm_id',frm.itm_id.value,'att_rate_status',att_rate_status)
		wbUtilsOpenWin(url, 'attendance_rate_remark', false, str_feature);
		
	}
}

function wbAttendanceChangeAttdRateExec(frm,lang){
	var str_feature = 'toolbar=' + 'no' + ',width=' + '600' + ',height=' + '200' + ',scrollbars=' + 'yes' + ',resizable=' + 'no' + ',status=' + 'yes';
	wb_utils_set_cookie("appn_usr_name", "");
	wb_utils_set_cookie("type","update");
	wb_utils_set_cookie("current", "");
	wb_utils_set_cookie("total", "");
	var url = "../htm/application_frame_window.htm?lang=" + lang + "&run_id=" + frm.itm_id.value + "&processEndFunction=" + "reload_me";
	wbUtilsOpenWin(url, 'attendance_rate', false, str_feature);
}

function wbAttendanceGetAttdRateList(itm_id){
	var url = wb_utils_invoke_disp_servlet('cmd','attd_rate_lst','module','course.EvalManagementModule','stylesheet','attendance_rate_lst.xsl','itm_id',itm_id,'att_status','-1');
	window.location.href = url;
}

function wbAttendanceGradRecord(itm_id,att_status,sort_col,sort_order,cur_page,timestamp,page_size){
	if(sort_col == null || sort_col == '') sort_col = 'user';

	if(sort_order == null || sort_order == '') sort_order = 'ASC';

	if(cur_page == null || cur_page == '') cur_page = '0';

	if(timestamp == null) timestamp = '';

	if(page_size == null || page_size == '') page_size = '100';

	if(att_status == null || page_size == '') att_status = '';
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_maintain_attendance', 'itm_id', itm_id, 'att_status', att_status, 'sort_col', sort_col, 'sort_order', sort_order, 'cur_page', cur_page, 'timestamp', timestamp, 'page_size', page_size, 'stylesheet', 'attendance_lst.xsl')
	window.location.href = url;
}

function wbAttendanceFinishedRecord(itm_id,att_status,sort_col,sort_order,cur_page,timestamp,page_size){

	if(sort_col == null || sort_col == '') sort_col = 'att_timestamp';

	if(sort_order == null || sort_order == '') sort_order = 'ASC';

	if(cur_page == null || cur_page == '') cur_page = '0';

	if(timestamp == null) timestamp = '';

	if(page_size == null || page_size == '') page_size = '100';

	if(att_status == null || page_size == '') att_status = '';
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_maintain_attendance', 'itm_id', itm_id, 'att_status', att_status, 'sort_col', sort_col, 'sort_order', sort_order, 'cur_page', cur_page, 'timestamp', timestamp, 'page_size', page_size, 'stylesheet', 'attendance_lst.xsl')
	window.location.href = url;
}

function wbAttendanceGetReportCard(app_id, itm_id, att_status){
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_attendance_record', 'app_id', app_id, 'stylesheet', 'learner_attendance_details.xsl', 'att_status', att_status, 'itm_id', itm_id)
	window.location.href = url;
}
/* private functions */
function _wbAttendanceGetAppIdCheckLst(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.checked && ele.name != 'sel_all_checkbox'){
			if(ele.value != "") str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbAttendanceGetAppNmCheckLst(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.checked && ele.name != 'sel_all_checkbox'){
			if(ele.value != "") str = str + ele.id + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

//add for eastcom
function _wbAttendanceGetAppIdLst(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if((ele.type == "checkbox") && (ele.name == "app_id")){
			if(ele.value != "") str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbAttendanceGetAppRemarkLst(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "text" && ele.name == "remark"){
			if(ele.value != ""){
				str = str + ele.value + "~"
			}else{
				str = str + " " + "~"
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbAttendanceGetAppUpdTimestampLst(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "hidden" && ele.name == "att_update_timestamp"){
			if(ele.value != ""){
				str = str + ele.value + "~"
			}else{
				str = str + " " + "~"
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}
/* validate functions */
function _wbAttendanceValidateChangeUserStatusCredit(frm, lang){
	var i, n, ele

	n = frm.icv_value.length

	for(i = 0; i < n; i++){
		ele = frm.icv_value[i]
		if(ele.value != ''){
			if(!gen_validate_float(ele, eval('wb_msg_' + lang + '_credit_unit'), lang)){
				ele.focus()
				return false;
			}
		}
	}
	return true;
}

function _wbAttendanceGetChangeUserStatusIctId(frm, lang){
	var i, n, ele, str

	str = ""
	n = frm.ict_id.length;

	if(n >= 1){
		for (i = 0; i < n; i++){
			ele = frm.ict_id[i]
			if(ele.type == 'hidden' && ele.value != ''){
				str = str + ele.value + '~';
			}
		}
	}else{
		str = str + frm.ict_id.value + '~'
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbAttendanceGetChangeUserStatusIcvValue(frm, lang){
	var i, n1, ele, str

	str = ""

	if(frm.ict_id && frm.ict_id.length == null) n1 = 1; else if(frm.ict_id){
		n1 = frm.ict_id.length
	}else{
		n1 = 0;
	}

	if(n1 >= 1){
		for (i = 1; i <= n1; i++){
			ele = eval('frm.type_' + i);
			if(ele[0].checked) str += eval('frm.type_' + i + '_default_val.value') + '~';
			else if (ele[1].checked) str += eval('frm.type_' + i + '_value.value') + '~';
		}
	}
	return str;
}

function _wbAttendanceValidateCreditValue(frm, lang){
	var i, n, ele, ele2

	if(frm.ict_id && frm.ict_id.length == null){
		n = 1;
	}else if(frm.ict_id){
		n = frm.ict_id.length;
	}else{
		n = 0;
	}

	if(n >= 1){
		for (i = 1; i <= n; i++){
			ele = eval('frm.type_' + i + '_value');

			ele2 = eval('frm.type_' + i);
			if(ele2[1].checked){
				if(n == 1){
					if(!gen_validate_float(ele, frm.icv_name.value, lang)) return false
				}else{
					if(!gen_validate_float(ele, frm.icv_name[i - 1].value, lang)) return false
				}
			}
		}
	}
	return true
}
//CourseMeasurement
function wbCourseMeasurement(itm_id){
		var url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_content_info', 'itm_id', itm_id, 'stylesheet', 'ae_cmt_maintain.xsl');
		window.location.href = url;
}
//获取input内的字符串提交到
function wbAttendanceSearchUser(frm){
	frm.user_code.value=wbUtilsTrimString(frm.user_code.value);
	frm.method = 'get';
	frm.action = wb_utils_ae_servlet_url;
	frm.cmd.value = 'ae_maintain_attendance';
	if(frm.orderby){
		frm.orderby.value = 'r_itm_title';
	}

	if(frm.sortorder){
		frm.sortorder.value = 'asc';
	}
	frm.stylesheet.value = 'attendance_lst.xsl';
	frm.sort_col='user';
	frm.sort_order='ASC';
	frm.cur_page='0';
	frm.page_size=frm.sel_frm;
	frm.submit();
}
function wbAttendanceSearchUserForRate(frm){
	frm.user_code.value=wbUtilsTrimString(frm.user_code.value);
	frm.method = 'get';
	frm.action = wb_utils_disp_servlet_url;
	frm.cmd.value = 'attd_rate_lst';
	frm.stylesheet.value = 'attendance_rate_lst.xsl';
    frm.att_status.value = '-1';
    frm.module.value = 'course.EvalManagementModule';
	frm.submit();
}
