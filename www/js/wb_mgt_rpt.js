// ------------------ wizBank Course object ----------------------------------------
// Convention:
//   public functions : use "wbMgtRpt" prefix 
//   private functions: use "_wbMgtRpt" prefix
// Dependency:
//   wb_utils.js
// ---------------------------------------------------------------------------------
//Delimiter
d_f = ":_:_:"; //field
d_v = "~"; //value
// ------------- Constructor -------------
function wbManagementReport(){
	this.get_rpt_lst = wbMgtRptGetReportList;
	
	this.get_rpt = wbMgtRptGetReport;
	this.get_rpt_adv = wbMgtRptGetReportAdvanced;
	
	this.get_rpt_by_que = wbMgtRptGetFBViewAll;

	
	this.ins_rpt_prep_popup = wbMgtRptInsertReportPrepPopup;
	this.ins_rpt_prep_popup_jsp = wbMgtRptInsertReportPrepPopupJsp
	this.ins_rpt_exec_popup = wbMgtRptInsertReportExecPopup;	
	
	this.ins_rpt_res_prep_popup = wbMgtRptInsertReportResultPrepPopup;		

	this.ins_rpt_prep = wbMgtRptInsertReportPrep;
	
	this.upd_rpt_prep = wbMgtRptUpdateReportPrep;
	this.upd_rpt_exec = wbMgtRptUpdateReportExec;
    this.upd_rpt_exec_jsp = wbMgtRptUpdateReportExecJsp;
	
	this.del_rpt = wbMgtRptDeleteReport;
	
	this.dl_rpt = wbMgtRptDownloadReport;
	this.dl_rpt_adv = wbMgtRptDownloadReportAdvanced;
	
	this.rslt_dl_rpt_adv = wbMgtRptResultDownloadReportAdvanced;
	this.get_lrn_by_cos_view_cos_appr = wbGetLearningActivityByCosAppr;
	this.get_lrn_by_lrn_view_lrn_appr = wbGetLearningActivityByLrnAppr;
	//Course Tracking Report
	this.get_cos_track_rpt = wbMgtRptGetCourseTrackingReport;
	
	this.get_lrn_track_rpt = wbMgtRptGetLearnerTrackingReport;
	
	//Enrollment Report & Accrediation Report
	this.grp_enrolment_rpt_srh = wbMgtRptGroupEnrolmentSearch
	this.grp_enrolment_rpt_srh_exec = wbMgtRptGroupEnrolmentSearchExec
	
	//
	this.get_rpt_mod_dl = wbMgtRptDownloadRptModuleList
	// module submission
	this.get_rpt_mod_subn_lst = wbMgtRptGetRptModuleSubmissionList 
	
	// evaluation survey rpt
	this.get_evn_svy_rpt = wbMgtRptEvnSurvey;
	
}

function Spec(){
	this.name = "";
	this.value = "";	
}

// ------------ Public Method ------------
function wbMgtRptGetLearnerTrackingReport(course_id, student_id, tkh_id){
	var url = wb_utils_invoke_servlet('cmd','get_lrn_mod_rpt', 'course_id', course_id, 'student_id', student_id, 'stylesheet','ist_lrn_rpt_mod.xsl', 'tkh_id', tkh_id)
	str_feature = 'toolbar='		+ 'no'
				+ ',width=' 		+ '780'
				+ ',height=' 		+ '400'
				+ ',scrollbars='	+ 'yes'
				+ ',resizable='		+ 'yes'
				+ ',status='		+ 'yes';	
	wbUtilsOpenWin(Url, '', false, str_feature);
	return;
}


function wbMgtRptGroupEnrolmentSearch(creditOpt){
	var	url = wbMgtRptGroupEnrolmentSearchUrlPrep(creditOptt)
	window.location.href = url;
}

function wbMgtRptGroupEnrolmentSearchUrlPrep(creditOpt){
	var rpt_type
	if (creditOpt == null || creditOpt == '') {creditOpt = 0;}
	if (creditOpt == 1) {rpt_type = 'group_cpt';}
	if (creditOpt == 'adm') {rpt_type = 'admin';}
	else {rpt_type = 'group';}
	
	var	url = wb_utils_invoke_servlet(
		'cmd','get_prof',
		'rpt_type',rpt_type,
		'stylesheet','enrolment_grp_credit_search.xsl'
	)
	return url;
}

function wbMgtRptGroupEnrolmentSearchExec(frm,rpt_type,rte_id,stylesheet,isStudent){	
	var spec_name = ''
	var spec_value = ''
	var _ent_id_lst = _wbMgtRptGetGroupEnrolmentSearchEntIdLst(frm)
	var _tnd_id_lst = _wbMgtRptGetGroupEnrolmentSearchTndIdLst(frm)
	stylesheet = 'enrolment_grp_credit_status.xsl';
	
	if (rpt_type == 'group_cpt'){
		spec_name += 'app_status_lst' + ':_:_:'
		spec_value += 'Admitted' + ':_:_:'
		
	}else{
		spec_name += 'app_status_lst' + ':_:_:'
		spec_value += 'Pending~Admitted~Waiting~Rejected~Withdrawn' + ':_:_:'
	}
	
	spec_name += 'ent_id' + ':_:_:'
	spec_value += _ent_id_lst + ':_:_:'
	
	spec_name += 'tnd_id' + ':_:_:'
	spec_value += _tnd_id_lst + ':_:_:'
	
	spec_name += 'ats_id' + ':_:_:' 
	spec_value += '1~2~3~4'	+ ':_:_:'	
	
	spec_name += "itm_title" + ":_:_:";
	spec_value += frm.itm_title.value + ":_:_:";	
	
	if (rpt_type == 'admin'){
		spec_name += "show_itm_credit" + ":_:_:";
		spec_value += "true:_:_:";		
	}
	
	spec_name += "itm_title_partial_ind" + ":_:_:";
	if(frm.itm_title_partial.checked) {spec_value += "0:_:_:";}
	else {spec_value += "1:_:_:";}
	
	
	
	if((frm.att_start_datetime_yy.value!=""&&frm.att_start_datetime_yy.value!=null) || (frm.att_start_datetime_mm.value!=""&&frm.att_start_datetime_mm.value!=null) || (frm.att_start_datetime_dd.value!=""&&frm.att_start_datetime_dd.value!=null)){
		if(!wbUtilsValidateDate("document." + frm.name + ".att_start_datetime","period")) {return false;}
		spec_name += "itm_start_datetime" + ":_:_:";
		spec_value += frm.att_start_datetime_yy.value + "-" + frm.att_start_datetime_mm.value + "-" + frm.att_start_datetime_dd.value + " 00:00:00.00" + ":_:_:";
	}
	
	if((frm.att_end_datetime_yy.value!=""&&frm.att_end_datetime_yy.value!=null) || (frm.att_end_datetime_mm.value!=""&&frm.att_end_datetime_mm.value!=null) || (frm.att_end_datetime_dd.value!=""&&frm.att_end_datetime_dd.value!=null)){
		if(!wbUtilsValidateDate("document." + frm.name + ".att_end_datetime","period")) {return;}
		spec_name += "itm_end_datetime";
		spec_value += frm.att_end_datetime_yy.value + "-" + frm.att_end_datetime_mm.value + "-" + frm.att_end_datetime_dd.value + " 23:59:59.00";
	}
		
	
	frm.module.value = "report.ReportModule"
	frm.cmd.value = 'get_rpt'
	frm.download.value = '0'
	frm.rte_id.value = '2'
	
	frm.rpt_type.value = rpt_type
	frm.stylesheet.value = stylesheet
	
	frm.spec_name.value = spec_name	
	frm.spec_value.value = spec_value		
	
	frm.action = Wb_Utils_Disp_Servlet_Url
	frm.method = "get";
	
	
	frm.submit();
	
}

function wbMgtRptGetCourseTrackingReport(frm,rte_id,stylesheet){
	frm.cmd.value = "get_rpt";
	frm.module.value = "report.ReportModule";
	frm.rte_id.value = rte_id;
	frm.rpt_type.value = "COURSE";
	frm.stylesheet.value = stylesheet;
	// -- Course Catalog -- //
	tnd_id_lst = "";
	for(i=0;i<frm.tnd_id.options.length;i++)
		tnd_id_lst += frm.tnd_id.options[i].value + "~";
	frm.spec_name.value = "tnd_id";
	if(tnd_id_lst.indexOf("~")>-1)
		tnd_id_lst = tnd_id_lst.substr(0,tnd_id_lst.length-1);
	frm.spec_value.value = tnd_id_lst;
	// -- Course Title -- //
	frm.spec_name.value += ":_:_:" + "itm_title";
	frm.spec_value.value += ":_:_:" + frm.itm_title.value;
	// -- Course Title Partial Match -- //
	frm.spec_name.value += ":_:_:" + "itm_title_partial_ind";
	if(frm.itm_title_partial.checked)
		frm.spec_value.value += ":_:_:" + "0";
	else
		frm.spec_value.value += ":_:_:" + "1";
	// -- Course Type -- //
	frm.spec_name.value += ":_:_:" + "itm_type";
	frm.spec_value.value += ":_:_:";
	// -- Show Run -- //
	frm.spec_name.value += ":_:_:" + "show_run_ind";
	frm.spec_value.value += ":_:_:" + "0";
	// -- Version -- //
	frm.spec_name.value += ":_:_:" + "show_old_version";
	frm.spec_value.value += ":_:_:" + "0";
	// -- Period -- //
	frm.spec_name.value += ":_:_:" + "start_datetime";
	frm.spec_value.value += ":_:_:";
	frm.spec_name.value += ":_:_:" + "end_datetime";
	frm.spec_value.value += ":_:_:";
	// -- Content -- //
	frm.spec_name.value += ":_:_:" + "content_lst";
	frm.spec_value.value += ":_:_:" + "catalog" + "~" + "attendance";
	// -- Form Submit -- //
	frm.method = "get";
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}
function wbGetLearningActivityByCosAppr(itm_id){
	var	url = self.location.href;
	url = wb_utils_escape_url_anchor(url);
	var rpt_type;
	var rsp_id = 0;

	if (getUrl('rpt_type')) {
		rpt_type = getUrlParam('rpt_type');
	}
	if (getUrl('window_name')) {
			window_name = getUrlParam('window_name');
	}
	if(getUrl('rsp_id')){
		rsp_id = getUrlParam('rsp_id');
	}
	var window_name;
	var newurl;
	newurl = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt","rpt_type",rpt_type,"stylesheet",'rpt_lrn_by_cos_view_cos_res.xsl',"download",'0',"window_name",window_name,'itm_id', itm_id,'rsp_id',rsp_id);
	wb_utils_open_win(newurl, window_name+'view',1300,700);
	
}
function wbGetLearningActivityByLrnAppr(usr_ent_id){
	var	url = self.location.href;
	url = wb_utils_escape_url_anchor(url);
	var rpt_type;
	var rsp_id = 0;

	if (getUrl('rpt_type')) {
		rpt_type = getUrlParam('rpt_type');
	}
	if (getUrl('window_name')) {
			window_name = getUrlParam('window_name');
	}
	if(getUrl('rsp_id')){
		rsp_id = getUrlParam('rsp_id');
	}
	var window_name;
	var newurl;
	newurl = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt","rpt_type",rpt_type,"stylesheet",'rpt_lrn_lrn_res.xsl',"download",'0',"window_name",window_name,'usr_ent_id', usr_ent_id,'rsp_id',rsp_id);
	wb_utils_open_win(newurl, window_name+'view',1300,700);
	
}
function wbMgtRptResultDownloadReportAdvanced(stylesheet, rpt_name,export_stat_only){
	
	var	url = self.location.href;
	url = wb_utils_escape_url_anchor(url);
	var rpt_type;


	if (getUrl('rpt_type')) {
		rpt_type = getUrlParam('rpt_type');
	}

	if (rpt_type == 'LEARNER'|| rpt_type == 'LEARNING_MODULE' || rpt_type=='LEARNING_ACTIVITY_COS' || rpt_type=='LEARNING_ACTIVITY_BY_COS' ||rpt_type=='LEARNING_ACTIVITY_LRN' 
				|| rpt_type=='EXAM_PAPER_STAT' || rpt_type =='TRAIN_FEE_STAT' || rpt_type =='TRAIN_COST_STAT' || rpt_type =='FM_FEE' || rpt_type=='CREDIT') {
		var window_name;
		var rsp_id;
		var newurl;
		
		if (getUrl('window_name')) {
			window_name = getUrlParam('window_name');
			if(export_stat_only){
				window_name = getUrlParam('window_name')+"__AA__"+export_stat_only;
			}
		}
		if(getUrl('rsp_id')) {
			rsp_id = getUrlParam('rsp_id');
			var newurl = wb_utils_invoke_servlet('cmd', 'get_prof', 'rpt_type', rpt_type, 'rpt_name', rpt_name, 'stylesheet', 'rpt_dl_progress.xsl', 'window_name', window_name, 'download', '4', 'rsp_id', rsp_id);
		}else {
			var newurl = wb_utils_invoke_servlet('cmd', 'get_prof', 'rpt_type', rpt_type, 'rpt_name', rpt_name, 'stylesheet', 'rpt_dl_progress.xsl', 'window_name', window_name, 'download', '4','export_stat_only',export_stat_only);
		}
		newurl = encodeURI(newurl);
		wb_utils_open_win(newurl, window_name+'export', 450, 150);
	}else {
        if (rpt_type =='SURVEY_QUE_GRP' || rpt_type == 'ASSESSMENT_QUE_GRP' || rpt_type =='SURVEY_EVN_QUE_GRP'){
        	url=setUrlParam('download','4',url);
        }else {
        	url = setUrlParam('download','4',url);
        }
    	url = url + "&rpt_name=" + rpt_name;
    
    	if (getUrl('page_size')) {
    		url = setUrlParam('page_size','-1',url);
    	}else{
    		url = url + "&page_size=-1";
    	}
    	
    	if (getUrl('stylesheet')) {
    		url = setUrlParam('stylesheet',stylesheet,url)
    	}
		url = encodeURI(url);
		wbUtilsOpenWin(url,'');
	}
}	

function wbMgtRptGetReportList(lang,rpt_type_lst){
	var	url = _wbMgtRptGetReportListURL(rpt_type_lst);
	window.location.href = url;
}

function wbMgtRptGetReport(rsp_id,rpt_type,title,stylesheet){
	var cmd = 'get_rpt'
	var dateVar = new Date();
	var window_name ="rpt_win"+dateVar.getTime();
	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd",cmd,"rsp_id",rsp_id,"rpt_type",rpt_type,"stylesheet",stylesheet,'window_name', window_name);
	var str_feature = 'width=' 				+ '780'
			+ ',height=' 				+ '500'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',toolbar='				+ 'yes'
			+ ',screenX='				+ '10'
			+ ',screenY='				+ '10'
			+ ',status='				+ 'yes';
	wbUtilsOpenWin(url,'win'+current,false,str_feature);
}

function _wbMgtRptConcateTreeNodeId(frm){
	// concat tree node id
	var i, n, ele;
	var temp_lst = '';
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i];
		if (ele.type == "checkbox" && ele.name == "treenode_id" && ele.checked) {
			if ( ele.value != "")
				temp_lst += ele.value + '~';
		}
	}
	return temp_lst;	
}

function _wbMgtRptConcateAttendanceStatusId(frm){
	// concat attendance status id
	var i, n, ele;
	var temp_lst = '';
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i];
		if (ele.type == "checkbox" && ele.name == "attendance_status_id" && ele.checked) {
			if ( ele.value != "")
				temp_lst += ele.value + '~';
		}
	}
	return temp_lst;	
}

function wbMgtRptGetReportAdvanced(frm,rpt_type,rte_id,stylesheet,isStudent,current,lang){
	//检测选择的排序字段是否在Report Status中
	//Exist(frm);
	var i;
	if(frm.rsp_title){
		if(frm.rsp_title.value.length == 0){
			alert(eval("wb_msg_rpt_title_wrong"));
			return;
		}
	}
	
	if(frm.sort_col!=null){
		var sortValue=frm.sort_col.value;
		var showValue=transValue(sortValue);
	  
		if(rpt_type!='LEARNING_ACTIVITY_LRN'&&rpt_type!='LEARNING_ACTIVITY_COS' &&rpt_type!='LEARNING_ACTIVITY_BY_COS' && rpt_type != 'EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
			var flag=detectValue(frm,sortValue,showValue);
			if(flag==0)
				return;
		}
	}
	
	if(rpt_type=='LEARNING_ACTIVITY_COS' || rpt_type == 'EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
		var flag=detectValueCos(frm);
     	if(flag==0)
        	return;
	}
        
	if (rpt_type=='TRAIN_COST_STAT') {
    	var flag = detectValueTrainCostStat(frm);
    	if(flag==0) return;
    }    
        
	if(lang==null || lang==""){
		lang="en"
	}
	var download = "0";
	var spec = new Spec();

	var rpt_type_learner = rpt_type.indexOf("LEARNER");
	if(rpt_type_learner<0) {
		rpt_type_learner = rpt_type.indexOf("learner");
	}
	
	if(rpt_type == 'LEARNER' || rpt_type == 'learner' || rpt_type_learner>=0){
		var require = 'yes';
	}else{
		var require = 'no';
	}

	if(rpt_type=='ASSESSMENT_QUE_GRP'||rpt_type=='LEARNING_ACTIVITY_LRN'||rpt_type=='LEARNING_ACTIVITY_COS' || rpt_type=='LEARNING_ACTIVITY_BY_COS' || rpt_type == 'EXAM_PAPER_STAT'){
		if(_wbMgtRptGetFormSpec1(frm,lang,spec,require,rpt_type) == false){
			return;
		}
	} else {
		if(_wbMgtRptGetFormSpec(frm,lang,spec,require) == false){
			return;
		}
	}

	var dateVar = new Date();
	var rpt_win = wbUtilsOpenWin(wb_utils_app_base+"htm/loading.htm?lang=" +lang, "rpt_win"+dateVar.getTime());
	//datetime_restriction
	if((rpt_type == 'ASSESSMENT_QUE_GRP' || rpt_type == 'LEARNING_MODULE' || rpt_type == 'learning_module' || rpt_type == 'LEARNER' || rpt_type == 'learner' || rpt_type_learner>=0) && (isStudent || isStudent == 'true')){
		if(spec.name.indexOf("ent_id")<0) {
			spec.name+='ent_id' + ':_:_:'
			spec.value+=frm.usr_ent_id.value + ':_:_:'
		}
	}
	if(rpt_type == 'COURSE' || rpt_type == 'course') {
		spec.name+='datetime_restriction' + ':_:_:'
		spec.value+='att_create_timestamp'	 + ':_:_:'
	}

	if(rpt_type =='TARGET_LEARNER' || rpt_type == 'TARGET_LEARNER'){
		spec.name += 'sort_col'  + ':_:_:'
		spec.value += frm.sort_col.value + ':_:_:'
		
		spec.name += 'sort_order'
		spec.value += frm.sort_order.value
	}
	if(rpt_type == 'LEARNER' && frm.is_my_staff) {
		spec.name += 'is_my_staff'  + ':_:_:'
		spec.value += frm.is_my_staff.value + ':_:_:'		
	}

	frm.module.value ="report.ReportModule";
	frm.cmd.value = "save_sess_spec";
	frm.rpt_type.value = rpt_type;
	if(frm.rsp_title)
		frm.rpt_name.value = frm.rsp_title.value;
	frm.stylesheet.value = stylesheet;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value;
	if(isStudent == 'false' || !isStudent){
		frm.download.value = download;
		frm.rte_id.value = rte_id;
	}
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";

	frm.target = "rpt_win"+dateVar.getTime();
	frm.window_name.value ="rpt_win"+dateVar.getTime();
	if(frm.fromHP)
		frm.fromHP.value = "NO";
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt","rpt_type",rpt_type,"stylesheet",frm.stylesheet.value,"download",frm.download.value,"window_name",frm.window_name.value);
	frm.submit();

}

function wbMgtRptEvnSurvey(frm,mod_id,lang) {
	var dateVar = new Date();
	var rpt_win = wbUtilsOpenWin("../htm/loading.htm?lang=" +lang, "rpt_win"+dateVar.getTime());
	
	frm.module.value ="report.ReportModule";
	frm.cmd.value = "save_sess_spec";
	frm.rpt_type.value = "SURVEY_EVN_QUE_GRP";
	frm.spec_name.value = "mod_id";
	frm.spec_value.value = mod_id;
	frm.download.value = 1;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	
	frm.target = "rpt_win"+dateVar.getTime();
	frm.window_name.value ="rpt_win"+dateVar.getTime();
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt","rpt_type", "SURVEY_EVN_QUE_GRP","stylesheet","rpt_evn_svy_que_grp_res.xsl","download", "","window_name", frm.window_name.value);
	frm.submit();
}

function wbMgtRptInsertReportPrep(rte_id,rpt_type,stylesheet){
	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_tpl","rte_id",rte_id,"rpt_type",rpt_type,"stylesheet",stylesheet);
	window.location.href = url;
}

function wbMgtRptGetFBViewAll(que_id){
	var window_name = getUrlParam("window_name");
	var rsp_id = getUrlParam("rsp_id");
        var disp_url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt","rpt_type","SURVEY_QUE_GRP","stylesheet","rpt_svy_que_grp_res.xsl","que_id",que_id,"window_name",window_name,"rsp_id",rsp_id);	
	wb_utils_open_win(disp_url,window_name+"_"+que_id,"800","600");
	}


function wbMgtRptInsertReportExecPopup(frm,lang){
//	if(!gen_validate_empty_field(frm.rsp_title,eval("wb_msg_"+lang+"_rpt_name"),lang))
//		return;

	frm.rsp_title.value = wbUtilsTrimString(frm.rsp_title.value);
    if(frm.rsp_title.value.length == 0){
		alert(eval("wb_msg_rpt_title_wrong"));
		return;
	}
    if(getChars(frm.rsp_title.value) > 80){
    	alert(eval("wb_msg_"+lang+"_title_length"));
		return;
    }
    

	frm.module.value = "report.ReportModule";
	frm.cmd.value = "ins_rpt_spec";
	frm.download.value = "0";
	//frm.url_success.value = wb_utils_invoke_disp_servlet("env","wizb","module","report.ReportModule","cmd","get_rpt_lst","stylesheet","rpt_all.xsl","show_public","y");
	//frm.url_success.value = frm.url_success.value+"#"+frm.rpt_type.value;
	frm.url_success.value = "Javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT')";
	frm.url_failure.value = "Javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT')";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbMgtRptInsertReportResultPrepPopup(frm,rsp_id,rte_id,usr_ent_id,rpt_type,lang){
	var spec_name = getUrlParam("spec_name")
	var spec_value = getUrlParam("spec_value")	
	frm.cmd.value = "echo_spec_param";
	frm.rsp_id.value = rsp_id;
	frm.module.value = "report.ReportModule";
	frm.download.value = "0";	
	frm.rte_id.value = rte_id;
	frm.usr_ent_id.value = usr_ent_id;
	frm.rpt_type.value = rpt_type;
	frm.rpt_type_lst.value = rpt_type;
	frm.stylesheet.value = "rpt_lrn_srh_popup.xsl";
	frm.url_failure.value = parent.location.href;
	frm.spec_name.value = spec_name;
	frm.spec_value.value = spec_value
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.target = "saveWin";
	frm.submit();	
	
}

function wbMgtRptInsertReportPrepPopup(frm,rte_id,usr_ent_id,rpt_type,lang){
	var spec = new Spec()
	var i;
	if(frm.sort_col!=null){
	  var sortValue=frm.sort_col.value;
	  var showValue=transValue(sortValue);
	  
	     if(rpt_type != 'LEARNING_ACTIVITY_LRN' && rpt_type != 'LEARNING_ACTIVITY_COS' && rpt_type != 'LEARNING_ACTIVITY_BY_COS' && rpt_type != 'EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
		
          var flag=detectValue(frm,sortValue,showValue);
	  if(flag==0)
	     return;
		 }
	}
	
//	if(rpt_type=='LEARNING_ACTIVITY_LRN'){
//		var flag=detectValueLrn(frm);
//		if(flag==0)
//	           return;
//	}
        if(rpt_type=='LEARNING_ACTIVITY_COS' || rpt_type == 'EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
        	var flag=detectValueCos(frm);
        	if(flag==0)
        	    return;
        }
    
	if (rpt_type=='TRAIN_COST_STAT') {
    	var flag = detectValueTrainCostStat(frm);
    	if(flag==0) return;
    }    
        
	if(lang==null || lang==""){
		lang="en";
	}
	
	var rpt_type_learner = rpt_type.indexOf("LEARNER");
	if(rpt_type_learner<0) {
		rpt_type_learner = rpt_type.indexOf("learner");
	}
	if(rpt_type == 'LEARNER' || rpt_type == 'learner' || rpt_type_learner>=0){
		var require = 'yes';
	}else{
		var require = 'no';
	}

    if(rpt_type=='ASSESSMENT_QUE_GRP'||rpt_type=='LEARNING_ACTIVITY_LRN'||rpt_type=='LEARNING_ACTIVITY_COS' ||rpt_type=='LEARNING_ACTIVITY_BY_COS' || rpt_type == 'EXAM_PAPER_STAT'){
		if(_wbMgtRptGetFormSpec1(frm,lang,spec,require,rpt_type) == false){
			return;
		}
	} else{
		if(_wbMgtRptGetFormSpec(frm,lang,spec,require) == false){
			return;
		}
    }
	
	if(rpt_type =='TARGET_LEARNER' || rpt_type == 'TARGET_LEARNER'){
		spec.name += 'sort_col' + ':_:_:'
		spec.value += frm.sort_col.value + ':_:_:'
		
		spec.name += 'sort_order'
		spec.value += frm.sort_order.value
	}
		
	//datetime_restriction
	spec.name+='datetime_restriction'
	spec.value+='att_create_timestamp'	
	frm.cmd.value = "echo_spec_param";
	frm.module.value = "report.ReportModule";
	frm.download.value = "0";	
	frm.rte_id.value = rte_id;
	frm.usr_ent_id.value = usr_ent_id;
	frm.rpt_type.value = rpt_type;
	frm.rpt_type_lst.value = rpt_type;
	frm.stylesheet.value = "rpt_lrn_srh_popup.xsl";
	frm.url_failure.value = parent.location.href;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value
	frm.action = wb_utils_disp_servlet_url;
	frm.target = window.name;
	//frm.method = "get";
	frm.method = "post";
	frm.window_name.value = "";
	frm.submit();
}

function wbMgtRptUpdateReportPrep(rsp_id,rte_id,rpt_type,stylesheet){
	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_tpl","rsp_id",rsp_id,"rte_id",rte_id,"rpt_type",rpt_type,"stylesheet",stylesheet);
	window.location.href = url;
}

function wbMgtRptUpdateReportExec(frm,rte_id,rsp_id,usr_ent_id,rpt_type,lang){
	//检测选择的排序字段是否在Report Status中
	//Exist(frm);
	var i;

	frm.rsp_title.value = wbUtilsTrimString(frm.rsp_title.value);
    if(frm.rsp_title.value.length == 0){
		alert(eval("wb_msg_rpt_title_wrong"));
		return;
	}
    if(getChars(frm.rsp_title.value) > 80){
    	alert(eval("wb_msg_"+lang+"_title_length"));
		return;
    }
    
    
	if(frm.sort_col!=null){
		var sortValue=frm.sort_col.value;
		var showValue=transValue(sortValue);
		if(rpt_type != 'LEARNING_ACTIVITY_LRN' && rpt_type != 'LEARNING_ACTIVITY_COS' && rpt_type != 'LEARNING_ACTIVITY_BY_COS' && rpt_type != 'EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
        	var flag=detectValue(frm,sortValue,showValue);
        	if(flag==0)
        		return;
		}
	}
			
//        if(rpt_type=='LEARNING_ACTIVITY_LRN'){
//		var flag=detectValueLrn(frm);
//		if(flag==0)
//	           return;
//	}
	if(rpt_type=='LEARNING_ACTIVITY_COS' || rpt_type=='EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
		var flag=detectValueCos(frm);
		if(flag==0)
			return;
	}	
    
    if(rpt_type=='TRAIN_COST_STAT') {
    	var flag = detectValueTrainCostStat(frm);
    	if(flag==0) return;
    }
	
	if(lang==null || lang==""){lang="en"}
	var download = "0";
	var url_success = _wbMgtRptGetReportListURL();
	var url_failure = parent.location.href;
	var spec = new Spec()
	var rpt_type_learner = rpt_type.indexOf("LEARNER");
	if(rpt_type_learner<0) {
		rpt_type_learner = rpt_type.indexOf("learner");
	}
	if(rpt_type == 'LEARNER' || rpt_type == 'learner'){
		var require = 'yes';
	}else{
		var require = 'no';
	}
	
	if(rpt_type=='ASSESSMENT_QUE_GRP' || rpt_type=='LEARNING_ACTIVITY_LRN' || rpt_type=='LEARNING_ACTIVITY_COS' || rpt_type=='LEARNING_ACTIVITY_BY_COS' || rpt_type=='EXAM_PAPER_STAT'){
		if(_wbMgtRptGetFormSpec1(frm,lang,spec,require,rpt_type) == false){
			return;
		}
	} else {
		if(_wbMgtRptGetFormSpec(frm,lang,spec,require) == false){
			return;
		}
	}
	spec.name+='datetime_restriction' + ':_:_:'
	spec.value+='att_create_timestamp' + ':_:_:'
	
	if(rpt_type =='TARGET_LEARNER' || rpt_type == 'TARGET_LEARNER'){
		spec.name += 'sort_col' + ':_:_:'
		spec.value += frm.sort_col.value + ':_:_:'
		
		spec.name += 'sort_order'
		spec.value += frm.sort_order.value
	}	
			
	frm.module.value = "report.ReportModule";
	frm.cmd.value = "upd_rpt_spec";
	frm.download.value = download;
	frm.rsp_id.value = rsp_id;
	frm.rte_id.value = rte_id;
	frm.usr_ent_id.value = usr_ent_id;
	frm.rpt_type.value = rpt_type;
	frm.rpt_type_lst.value = rpt_type;
	frm.url_success.value = url_success;
	frm.url_failure.value = url_failure;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.target = "_parent";
	frm.window_name.value="";
	frm.submit();
}




function wbMgtRptUpdateReportExecJsp(frm,rte_id,rsp_id,usr_ent_id,rpt_type,lang){
    //检测选择的排序字段是否在Report Status中
    //Exist(frm);
    var i;

    frm.rsp_title.value = wbUtilsTrimString(frm.rsp_title.value);
    if(frm.rsp_title.value.length == 0){
            layer.alert(eval("wb_msg_rpt_title_wrong"),{title:fetchLabel("label_core_report_145")});
        //alert(eval("wb_msg_rpt_title_wrong"));
        return;
    }
    if(getChars(frm.rsp_title.value) > 80){
            layer.alert(eval("wb_msg_"+lang+"_title_length"),{title:fetchLabel("label_core_report_145")});
        //alert(eval("wb_msg_"+lang+"_title_length"));
        return;
    }
    
    
    if(frm.sort_col!=null){
        var sortValue=frm.sort_col.value;
        var showValue=transValue(sortValue);
        if(rpt_type != 'LEARNING_ACTIVITY_LRN' && rpt_type != 'LEARNING_ACTIVITY_COS' && rpt_type != 'LEARNING_ACTIVITY_BY_COS' && rpt_type != 'EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
            var flag=detectValue(frm,sortValue,showValue);
            if(flag==0)
                return;
        }
    }
            

    if(rpt_type=='LEARNING_ACTIVITY_COS' || rpt_type=='EXAM_PAPER_STAT' && rpt_type != 'TRAIN_FEE_STAT'){
        var flag=detectValueCos(frm);
        if(flag==0)
            return;
    }   
    
    if(rpt_type=='TRAIN_COST_STAT') {
        var flag = detectValueTrainCostStat(frm);
        if(flag==0) return;
    }
    
    if(lang==null || lang==""){lang="en"}
    var download = "0";
    var url_success = _wbMgtRptGetReportListURL();
    var url_failure = parent.location.href;
    var spec = new Spec()
    var rpt_type_learner = rpt_type.indexOf("LEARNER");
    if(rpt_type_learner<0) {
        rpt_type_learner = rpt_type.indexOf("learner");
    }
    if(rpt_type == 'LEARNER' || rpt_type == 'learner'){
        var require = 'yes';
    }else{
        var require = 'no';
    }
    
    if(!validateForm()){
          return;
    }
    if(_wbMgtRptGetFormSpecJsp(frm,lang,spec,require,rpt_type) == false){
         return;
    }
    spec.name+='datetime_restriction' + ':_:_:'
    spec.value+='att_create_timestamp' + ':_:_:'
    
    if(rpt_type =='TARGET_LEARNER' || rpt_type == 'TARGET_LEARNER'){
        spec.name += 'sort_col' + ':_:_:'
        spec.value += frm.sort_col.value + ':_:_:'
        
        spec.name += 'sort_order'
        spec.value += frm.sort_order.value
    }   
            
    frm.module.value = "report.ReportModule";
    frm.cmd.value = "upd_rpt_spec";
    frm.download.value = download;
    frm.rsp_id.value = rsp_id;
    frm.rte_id.value = rte_id;
    frm.usr_ent_id.value = usr_ent_id;
    frm.rpt_type.value = rpt_type;
    frm.rpt_type_lst.value = rpt_type;
    frm.url_success.value = url_success;
    frm.url_failure.value = url_failure;
    frm.spec_name.value = spec.name;
    frm.spec_value.value = spec.value;
    frm.action = wb_utils_disp_servlet_url;
    frm.method = "post";
    frm.target = "_parent";
    frm.window_name.value="";
    frm.submit();
}


/*取得在sort_order中取得的值
function getSortShowValue(sortValue,lang){
	var trueValue=new Array(new Array("usr_id","User ID","用户名","sss"),
                                new Array("usr_display_bil","Name","达到","sss"),
                                new Array("itm_code","Course Code","地方","sss"),
                                new Array("itm_title","Course Title","达到","ss"),
                                new Array("att_create_timestamp","Enrollment Date","达到到达到","sss"),
                                new Array("att_timestamp","Completion Date","士","sss"));  
	var currValue=new Array(4);
	var showValue="";
	for(i=0;i<trueValue.length;i++){
		currValue=trueValue[i];
		if(sortValue==currValue[0]){
			if(lang=="en"){
				showValue=currValue[1];
				break;
			}
			if(lang=="gb"){
				showValue=currValue[2];
				break;
			}
			if(lang=="ch"){
				showValue=currValue[3];
				break;
			}
		}
	}
	return showValue;
}*/

function detectValue(frm,sortValue,showValue){
	var i;
	var flag=0;
	//user_content
	if(frm.usr_content!=null){
	 if(flag==0){
	  var m=eval("frm."+sortValue+"_fieldname.value");
	  for(i=0;i<frm.usr_content.length;i++){
            var ele=frm.usr_content[i];
            if(ele.checked==true){
            	 if(ele.value==m){
            	 	flag=1;
            	 	break;
            	}
             }
           }
          }
        }
	//course information
	if(frm.itm_content!=null){
	if(flag==0){

		var m=eval("frm."+sortValue+"_fieldname.value");
		for(i=0;i<frm.itm_content.length;i++){
		   var ele=frm.itm_content[i];
		   if(ele.checked==true){
		      if(ele.value==m){		      
		   	     flag=1;
		             break;	
		            }	       
		     }
		 }   //for结束
	   }
        }
	//other information
	if(frm.content!=null){
	if(flag==0){
		for(i=0;i<frm.content.length;i++){
			var ele=frm.content[i];
			if(ele.checked==true){
				if(ele.value==sortValue){
					flag=1;
					break;
				}
			}
		}
           }
         }
        if(flag==0){
        	//alert("choice"+showValue);
        	alert(eval("wb_msg_sort"));
        }
        return flag;
}

function detectValueLrn(frm){
	            var count = 0;
	       	    var flag = 0;
	       	    var showValue1;
	       	    var showValue2;
	       	    while(count < frm.itm_content.length){
	       	    	var ele = frm.itm_content[count];
	       	    	if(ele.id=='label_field01')
	       	    	    showValue1 = ele.value;
	       	    	if(ele.id == 'label_field01'&& ele.checked == true){
	       	    		flag=1;
	       	    		//break;
	       	    	}
	       	    	if(ele.id == 'label_field02')
	       	    	    showValue2=ele.value;
	       	    	if(ele.id == 'label_field02'&& ele.checked == true){
	       	    		flag=1;
	       	    		//break;
	       	    	}
	       	    	count++;
	       	    }
	       	    var m=eval("frm."+showValue1+".value");
	       	    var n=eval("frm."+showValue2+".value");
	       	    if(flag==0){
	       	    	alert(eval("wb_msg_either")+m+eval("wb_msg_or")+n+eval("wb_msg_choose"));
	            }
	            return flag;
}

function detectValueCos(frm){
	       	    var count = 0;
	       	    var flag = 0;
	       	    var showValue1;
	       	    var showValue2;
	       	    while(count < frm.usr_content.length){
	       	    	var ele = frm.usr_content[count];
	       	    	if(ele.id=='label_usr_id')
	       	    	    showValue1=ele.value;
	       	    	if(ele.id == 'label_usr_id'&& ele.checked == true){
	       	    		flag=1;
	       	    		//break;
	       	    	}
	       	    	if(ele.id=='label_usr_display_bil')
	       	    	    showValue2=ele.value;
	       	    	if(ele.id == 'label_usr_display_bil'&& ele.checked == true){
	       	    		flag=1;
	       	    		//break;
	       	    	}
	       	    	count++;
	       	    }
	       	    //Alert(showValue2);
	       	    var m=eval("frm."+showValue1+".value");
	       	    var n=eval("frm."+showValue2+".value");
	       	    if(flag==0){
	       	    	alert(eval("wb_msg_either")+m+eval("wb_msg_or")+n+eval("wb_msg_choose"));
	            }
	            return flag;
}

function detectValueTrainCostStat(frm){

    var flag = 0;
    var showValue1;
    var showValue2;

    for (var i = 0, ele; ele = frm.content[i]; i++) {
    	if(ele.id == 'label_p_itm_code') {
    	    showValue1 = ele.value;
    	    if (ele.checked === true) {
    	    	flag=1;
    	    	break;
    	    }
    	}
    	if(ele.id == 'label_p_itm_title') {
    	    showValue2=ele.value;
    	    if (ele.checked === true) {
	    		flag=1;
	    		break;
	    	}
    	}
    }
    var m = frm.p_itm_code.value;
    var n = frm.p_itm_title.value;
    if(flag==0){
    	alert(eval("wb_msg_either")+m+eval("wb_msg_or")+n+eval("wb_msg_choose"));
    }
    return flag;
}

function transValue(sortValue){
	//alert(sortValue);
	var m=eval("frm."+sortValue+"_display.value");
	return m;	
}


function wbMgtRptDeleteReport(rsp_id,lang,rpt_type){
	if(confirm(eval("wb_msg_"+lang+"_del_it"))){
		var url_prev = _wbMgtRptGetReportListURL(rpt_type);
		var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","del_rpt_spec","rsp_id",rsp_id,"url_success",url_prev);
		window.location.href = url;
	}
}

function wbMgtRptDownloadReport(rsp_id,rpt_type,stylesheet,rpt_name){
	var rpt_type;
	var url;
	if (getUrl('rpt_type')) {
		rpt_type = getUrlParam('rpt_type');
	}
	if (rpt_type=='SURVEY_QUE_GRP') {
	   url = wb_utils_disp_servlet_url+"?env=wizb&module=report.ReportModule&cmd=get_rpt&rsp_id="+rsp_id+"&rpt_type="+rpt_type+"&page_size=-1&download=4&stylesheet="+stylesheet;
	}
	
	if (rpt_type=='ASSESSMENT_QUE_GRP') {
	   url = wb_utils_disp_servlet_url+"?env=wizb&module=report.ReportModule&cmd=get_rpt&rsp_id="+rsp_id+"&rpt_type="+rpt_type+"&page_size=-1&download=4&stylesheet="+stylesheet;
	    if (rpt_name != null && rpt_name != '') {
	        url = url + "&rpt_name="+rpt_name;
	    }
		wbUtilsOpenWin(url,'');
		return;
	}
	

	if (rpt_type == 'LEARNER' || rpt_type == 'LEARNING_MODULE'||rpt_type=='LEARNING_ACTIVITY_COS' ||rpt_type=='LEARNING_ACTIVITY_BY_COS' ||rpt_type=='LEARNING_ACTIVITY_LRN' 
	        || rpt_type == 'EXAM_PAPER_STAT' || rpt_type=='CREDIT' || rpt_type =='TRAIN_FEE_STAT' || rpt_type =='TRAIN_COST_STAT') {
		var dateVar = new Date();
		var window_name ="rpt_win"+dateVar.getTime();
		var newurl = wb_utils_invoke_servlet('cmd', 'get_prof', 'rpt_type', rpt_type, 'rsp_id', rsp_id, 'stylesheet', 'rpt_dl_progress.xsl', 'window_name', window_name, 'download', '4');
		wb_utils_open_win(newurl, window_name+'export', 450, 150);
	} else {
		var download = 3;
		if(rpt_type == 'SURVEY_QUE_GRP') {
			download = 4;
		}
		var url = wb_utils_disp_servlet_url+"?env=wizb&module=report.ReportModule&cmd=get_rpt&rsp_id="+rsp_id+"&rpt_type="+rpt_type+"&page_size=-1&download=" + download + "&stylesheet="+stylesheet+'&window_name='+window_name;
    if (rpt_name != null && rpt_name != '') {
        url = url + "&rpt_name="+rpt_name;
    }
		if (rpt_type == 'SURVEY_QUE_GRP') {
			wbUtilsOpenWin(url,'');
		} else {
			window.location.href = url;
		}
	}
}

function wbMgtRptDownloadReportAdvanced(frm,rpt_type,rte_id,stylesheet,lang){
	if(lang==null || lang==""){lang="en"}
	var download = "3";

	var spec = new Spec()
	var rpt_type_learner = rpt_type.indexOf("LEARNER");
	if(rpt_type_learner<0) {
		rpt_type_learner = rpt_type.indexOf("learner");
	}
	if(rpt_type == 'LEARNER' || rpt_type == 'learner'){
		var require = 'yes';
	}else{
		var require = 'no';
	}

	if(_wbMgtRptGetFormSpec(frm,lang,spec,require) == false){
		return;
	}
		spec.name+='datetime_restriction'
		spec.value+='att_create_timestamp'	
	
	
	frm.module.value = "report.ReportModule";
	frm.page_size.value = "-1";
	frm.cmd.value =  "get_rpt";
	if(frm.rpt_title){
	frm.rpt_name.value = frm.rsp_title.value;
	}
	frm.rpt_type.value = rpt_type;
	frm.stylesheet.value = stylesheet;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value;
	frm.download.value = download;
	frm.rte_id.value = rte_id;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.target = "_parent";
	frm.submit();
}

function wbMgtRptDownloadRptModuleList(frm,rte_id,rpt_type,lang){
	var _delimiter = ':_:_:'
	var _mod_id_lst = _wbMgtRptGetRptModuleIdList(frm)
	var _cos_id_lst = _wbMgtRptGetRptModuleCosIdList(frm)
	var _mod_attempt_num = _wbMgtRptGetRptModuleAttemptNumList(frm)	
	var _validation_passed = false
	
	if (_mod_id_lst != '' && _mod_id_lst != null) {_validation_passed = true;}
	else {		
		_validation_passed = false
		alert(eval('wb_msg_' + lang + '_select_itm'))
		return;
	}
	
	if (_validation_passed){				
		frm.cmd.value = 'get_rpt'
		frm.module.value = 'report.ReportModule'
		frm.download.value = '1'
		frm.rte_id.value = rte_id
		frm.rpt_type.value = rpt_type
		frm.url_failure.value = self.location.href
		switch(rpt_type){
			case 'MODULE_IND' :
				//frm.stylesheet.value = 'rpt_dl_mod_ind.xsl'
				frm.spec_name.value =  'cos_id' + _delimiter + 'mod_id' + _delimiter + 'attempt_nbr'
				frm.spec_value.value = _cos_id_lst + _delimiter + _mod_id_lst + _delimiter + _mod_attempt_num
				frm.rpt_name.value = 'individual_rpt'
				break;
			case 'MODULE_CMP' :
				//frm.stylesheet.value = 'rpt_dl_mod_cmp.xsl'
				frm.spec_name.value =  'mod_id_lst' + _delimiter + 'attempt_nbr'
				frm.spec_value.value = _mod_id_lst + _delimiter + _mod_attempt_num
				frm.rpt_name.value = 'quiz_rpt'
				break;
			case 'MODULE_QUE' :
				//frm.stylesheet.value = 'rpt_dl_mod_que.xsl'
				frm.spec_name.value =  'cos_id' + _delimiter + 'mod_id' + _delimiter + 'attempt_nbr'
				frm.spec_value.value = _cos_id_lst + _delimiter + _mod_id_lst + _delimiter + _mod_attempt_num
				frm.rpt_name.value = 'question_rpt'
				break;
			default :
				break;
		}
			
		
		frm.method = "post"
		frm.action = wb_utils_disp_servlet_url
		frm.submit()
	}
}

function wbMgtRptGetRptModuleSubmissionList(mod_id, itm_id, startDate, endDate, run_id_lst){
	var spec_name = 'itm_id' + ':_:_:' + 'mod_id' + ':_:_:' + 'start_datetime' + ':_:_:' + 'end_datetime' + ':_:_:' + 'run_id_lst' + ':_:_:' ;
	var spec_value = itm_id + ':_:_:' + mod_id + ':_:_:' + startDate + ':_:_:' + endDate + ':_:_:' + run_id_lst + ':_:_:';

	var url = wb_utils_invoke_disp_servlet('cmd','get_rpt','stylesheet', 'rpt_svy_cos_ind_res.xsl', 'download', '0', 'rpt_type', 'SURVEY_IND', 
					'spec_name', spec_name, 'spec_value', spec_value, 
					'module', 'report.ReportModule')

	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '420'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		wbUtilsOpenWin(url, 'submission_list', false, str_feature);	
}




// ------------ Private Method ------------
function _wbMgtRptGetGroupEnrolmentSearchEntIdLst(frm){
	var i,n,str
	str = ""
		
	ele = frm.ent_id_lst
	if (ele){			
		if (ele.options.length > 0){
			for (i = 0; i < ele.options.length ; i++){					
				if (ele.options[i].value != "") {str += ele.options[i].value + "~";}					
			}
		}
	}
		
	if (str != "") {str = str.substring(0, str.length-1)}
	return str;
}

function _wbMgtRptGetGroupEnrolmentSearchTndIdLst(frm){
	var i,n,str
	str = ""
		
	ele = frm.tnd_id
	if (ele){			
		if (ele.options.length > 0){
			for (i = 0; i < ele.options.length ; i++){					
				if (ele.options[i].value != "") {str += ele.options[i].value + "~";}					
			}
		}
	}
		
	if (str != "") {str = str.substring(0, str.length-1)}
	return str;
}

function _checkCourseReportValid(frm,lang){
	if(lang==null)
		lang = 'en';
	sel_content = 0;
	for(i=0;i<frm.content.length;i++)
		if(frm.content[i].checked)
			sel_content++;
	if(sel_content==0){
		alert(eval('wb_msg_'+lang+'_select_content'));
		return false;
	}
	return true;
}

function _wbMgtRptGetRptCourseIdList(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.checked && ele.name == "cos_sel") {
			if ( ele.value != "")
				str = str + ele.value + "~"
		}
	}	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}

function _wbMgtRptGetRptModuleIdList(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.checked && ele.name == "module_sel") {
			if ( ele.value != "")
				str = str + ele.value + "~"
		}
	}	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}

function _wbMgtRptGetRptModuleCosIdList(frm){
	var i, n, ele, ele2, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.checked && ele.name == "module_sel") {
			ele2 = frm.elements[i+1]			
			if (ele2.value != "")
				str = str + ele2.value + "~"
		}
	}	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}	
	return str;
}

function _wbMgtRptGetRptModuleAttemptNumList(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.checked && ele.name == "module_sel") {			
				str = str + "1" + "~"
		}
	}	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}

function _wbMgtRptGetAttStatusLst(frm){
	if (frm.ats_id_progress && frm.ats_id_progress.checked){
		frm.ats_id_lst.value += frm.ats_id_progress.value;
	}
	return frm.ats_id_lst.value;
}
function _wbMgtRptGetCheckboxLst(ele,checked){
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

// New Added by man============================
function _wbMgtRptGetContentLst(frm,spec,type){
	var temp_lst = "";
	if(type){
	var content =eval('frm.' + type + 'content')
	}else{
		var content = frm.content
	}
	if(content){
		if(content.length > 1){
		for(i=0;i<content.length;i++){
			if(content[i].checked)
				temp_lst += content[i].value + d_v;
		}	
		}else{
			if(content.checked){
				temp_lst += content.value + d_v;
			}
		}
	}	
	temp_lst = temp_lst.substr(0,temp_lst.length-1)	
	if(type){
		spec.name += type + "content_lst" + d_f;
	}else{
		spec.name += "content_lst" + d_f;
	}
	spec.value += temp_lst + d_f;			
	return true;
}


function _wbMgtRptGetItemIDLst(frm,lang,spec){

	var temp_lst = "";
	if(frm.itm_id_lst){
		if(frm.itm_id_lst.options.length == 1 ) {
			temp_lst = frm.itm_id_lst.options[0].value;
			//alert("love u");
			if(temp_lst =="" && (!frm.course_sel_type || frm.course_sel_type[1].checked)){
				alert(eval('wb_msg_'+lang+'_select_course_title'));
				return false;
			}else{
				spec.name += "itm_id" + d_f;
				spec.value += temp_lst + d_f;			
			}
		} else {
			for(i=0;i<frm.itm_id_lst.options.length;i++) {
				temp_lst += frm.itm_id_lst.options[i].value + d_v;
			}
			if(temp_lst =="" && (!frm.course_sel_type || frm.course_sel_type[1].checked)){
				alert(eval('wb_msg_'+lang+'_select_course_title'));
				return false;
			} else {
				temp_lst = temp_lst.substr(0,temp_lst.length-1)
				spec.name+="itm_id"+d_f;
				spec.value+=temp_lst + d_f;
			}
		}
	}
	return true;
}

function _wbMgtRptGetRspLrnOrCos(frm, spec) {
	
	if(frm.lrn_content) {
		if(frm.lrn_content[0].checked && frm.lrn_content[0].disabled == false) {
			spec.name +="answer_for_lrn" + d_f;
			spec.value += "1" + d_f;
		} else {
			spec.name +="answer_for_lrn" + d_f;
			spec.value += "0" + d_f;		
		}
		if(frm.lrn_content[1].checked && frm.lrn_content[1].disabled == false) {
			spec.name +="answer_for_course_lrn" + d_f;
			spec.value += "1" + d_f;
		} else {
			spec.name +="answer_for_course_lrn" + d_f;
			spec.value += "0" + d_f;		
		}
	}
	if(frm.cos_content) {
		if(frm.cos_content[0].checked && frm.cos_content[0].disabled == false) {
			spec.name +="answer_for_course" + d_f;
			spec.value += "1" + d_f;
		} else {
			spec.name +="answer_for_course" + d_f;
			spec.value += "0" + d_f;		
		}
		if(frm.cos_content[1].checked && frm.cos_content[1].disabled == false) {
			spec.name +="answer_for_lrn_course" + d_f;
			spec.value += "1" + d_f;
		} else {
			spec.name +="answer_for_lrn_course" + d_f;
			spec.value += "0" + d_f;		
		}
	}
}
function _wbMgtRptGetEntIDLst(frm,lang,spec,require){
	var temp_lst = "";
	var temp_lst_usr = "";
	var defined = false;
	if(frm.ent_id_lst || frm.usr_ent_id_lst || frm.usg_ent_id_lst){
    	if (frm.usr_sel_all_user){
    		
    		//alert("love1");
    		if (frm.usr_sel_all_user[0].checked){
    			//alert("love11");
    			defined = true;
    			// for deleted user. dont supply ent_id 
    //				spec.name += "ent_id" + d_f;
    //				spec.value += frm.usr_sel_all_user[0].value + d_f;		
    			spec.name += "all_user_ind" + d_f;
    			spec.value += "1" + d_f;		
    		}else{
    			//alert("love12");
    			spec.name += "all_user_ind" + d_f;
    			spec.value += "0" + d_f;		
    		}
    	}

		if (defined != true && frm.s_usg_ent_id_lst){
			//alert("love3");
			var checkedIdx = -1;
			for (var i = 0; i < frm.s_usg_ent_id_lst.length; i++) {
			        if (frm.s_usg_ent_id_lst[i].checked) {
			            checkedIdx = i;
			            break;
			        }
			}
			s_usg_ent_id_lst = frm.s_usg_ent_id_lst[checkedIdx].value;
			if (s_usg_ent_id_lst != ''){
				defined = true;
			}
		}
        
		if (defined == false){
            if (frm.ent_id_lst && frm.ent_id_lst){
                for(i=0;i<frm.ent_id_lst.options.length;i++){
                    temp_lst += frm.ent_id_lst.options[i].value + d_v;
                }
                if(temp_lst ==""){
                    //alert(require);
                    if(require == 'yes' ){
                    var lab_group = frm.lab_group ? frm.lab_group.value : "Group(sys)"
                    alert(eval("wb_msg_"+lang+"_sel_lrn"));
                    return false;
                    }
                }else{
                    temp_lst = temp_lst.substr(0,temp_lst.length-1)	
                    spec.name += "ent_id" + d_f;
                    spec.value += temp_lst + d_f;		
                }
            }
    	}

        if (frm.usr_sel_all_user && frm.usr_sel_all_user.length > 2) {
            if (frm.usr_sel_all_user[2].checked==true){
                for(i=0;i<frm.usg_ent_id_lst.options.length;i++){
                    temp_lst += frm.usg_ent_id_lst.options[i].value + d_v;
                }
                if(temp_lst ==""){
                    if(require == 'no'){
                        var lab_group = frm.lab_group ? frm.lab_group.value : "Group(sys)"
                        alert(eval("wb_msg_"+lang+"_sel_lrn_grp"));
                        return false;
                    }
                }else{
                    temp_lst = temp_lst.substr(0,temp_lst.length-1)	
                    spec.name += "usg_ent_id" + d_f;
                    spec.value += temp_lst + d_f;		
                }
            } 
        }

        if(frm.usr_ent_id_lst && frm.usr_sel_all_user[1]){
            if (frm.usr_sel_all_user[1].checked==true){
                for(i=0;i<frm.usr_ent_id_lst.options.length;i++){
                    temp_lst_usr += frm.usr_ent_id_lst.options[i].value + d_v;
                }
                if(temp_lst_usr ==""){
                    if(require == 'no'){
                    var lab_group = frm.lab_group ? frm.lab_group.value : "Group(sys)"
                    alert(eval("wb_msg_"+lang+"_sel_lrn"));
                    return false;
                    }
                }else{
                    temp_lst_usr = temp_lst_usr.substr(0,temp_lst_usr.length-1)	
                    spec.name += "usr_ent_id" + d_f;
                    spec.value += temp_lst_usr + d_f;
                }
            }
        }
	}
	return true;
}

function _wbMgtRptGetSUsgEntIdLst(frm,lang,spec,require){
	if (frm.s_usg_ent_id_lst){
		for (i=0; i<frm.s_usg_ent_id_lst.length; i++){
			if(frm.s_usg_ent_id_lst[i].checked){
				spec.name += "s_usg_ent_id_lst" + d_f;
				spec.value += frm.s_usg_ent_id_lst[i].value + d_f;		
			}
		}
	}
}


function _wbMgtRptGetTndIdLst(frm,lang,spec,require){
	if( frm.tnd_id || frm.catalog_checkbox_ind){
		var temp_lst = ""
		if( frm.catalog_checkbox_ind ) {
			temp_lst = _wbMgtRptConcateTreeNodeId(frm);
		} else {
			if(frm.tnd_id){
				for(i=0;i<frm.tnd_id.options.length;i++)
					temp_lst += frm.tnd_id.options[i].value + d_v;
			}
		}
		//alert("!?!!!"+require);
		if(temp_lst == "" && (!frm.course_sel_type || frm.course_sel_type[2].checked)){
			if(require == false){
				alert(eval("wb_msg_"+lang+"_sel_catalog"));
				return false;
			}
		}else{
			temp_lst = temp_lst.substr(0,temp_lst.length-1)
			spec.name += "tnd_id" + d_f;
			spec.value += temp_lst + d_f;	
		}
	}
	return true;
}

function _wbMgtGetModId(frm,lang,spec){

	if(frm.mod_id){
		if(frm.mod_id.type.search('select') != -1){

			if(frm.mod_id.options[frm.mod_id.selectedIndex].value == ""){
				alert(eval("wb_msg_"+lang+"_select_course_evaluation"));
				return false;
			}else{
				spec.name += "mod_id" + d_f;
				spec.value += frm.mod_id.options[frm.mod_id.selectedIndex].value + d_f;
			}			
		}else{
			if(frm.mod_id.value == ""){
	
				alert(eval("wb_msg_"+lang+"_select_course_evaluation"));
				return false;
			}else{
				spec.name += "mod_id" + d_f;
				spec.value += frm.mod_id.value + d_f;
			}
		}
	}
}

function _wbMgtGetTargetType(frm, lang, spec){
	if(frm.target_type_lst) {
		frm.target_type_lst.value = _wbMgtRptGetCheckboxLst(frm.target_type_checkbox,true);
		if(frm.target_type_lst.value==""){
			alert(eval("wb_msg_"+lang+"_sel_target_lrn_type"));
			return false;
		}else{
			spec.name += "target_type" + d_f;
			spec.value += frm.target_type_lst.value + d_f;			
		}
	}
	return true;
}

function _wbMgtGetLrnCreteria(frm, lang, spec){
	if(frm.lrn_criteria_lst) {
		/*
		frm.lrn_criteria_lst.value = _wbMgtRptGetCheckboxLst(frm.lrn_criteria_lst_checkbox,true);
		if(frm.lrn_criteria_lst.value!=""){
			spec.name += "search_criteria" + d_f;
			spec.value += frm.lrn_criteria_lst.value + d_f;
		}
		*/
		spec.name += "search_criteria" + d_f;
		for(i=0; i<3; i++){
			if(frm.lrn_criteria_lst_checkbox[i].checked){
				if(eval('frm.type_' + (i+1) + '[0].checked')){
					spec.value += eval('frm.type_' + (i+1) + '[0].value') + '~';
				} else if(eval('frm.type_' + (i+1) + '[1].checked')){
					spec.value += eval('frm.type_' + (i+1) + '[1].value') + '~';
				}
			}
		}
		spec.value += d_f;
		
		spec.name += "search_criteria_condition" + d_f;
		spec.value += "AND" + d_f;
		/*
		alert(spec.name);
		alert(spec.value);
		*/
	}
}


function _wbMgtGetItmTitle(frm,spec){
	if(frm.itm_title){
		spec.name += "itm_title" + d_f;
		spec.value += frm.itm_title.value + d_f;
	}
}

function _wbMgtGetItmTitlePartialInd(frm,spec){
	if(frm.itm_title_partial){
		spec.name += "itm_title_partial_ind" + d_f;
		spec.value  += frm.itm_title_partial.checked ? ("0" + d_f) : ("1" + d_f);
	}
}

function _wbMgtGetItmType(frm,spec){
	if(frm.itm_type){
		spec.name += "itm_type" + d_f;
		if(frm.itm_type.type && frm.itm_type.type.search('select') == -1){
			spec.value += frm.itm_type.value + d_f;
		}else if(frm.itm_type.type && frm.itm_type.type === 'checkbox') {
			if (frm.itm_type.checked) {
				spec.value += frm.itm_type.value + d_f;
			} else {
				alert(eval("wb_msg_select_train_type"));
				return false
			}
		}else if(frm.itm_type.length) {
			var cnt = 0;
			for (var i = 0; i < frm.itm_type.length; i++) {
				if (frm.itm_type[i].checked) {
					if (cnt > 0) {
						spec.name += "itm_type" + d_f;
					}
					spec.value += frm.itm_type[i].value + d_f;
					cnt++;
				}
			}
			if (cnt == 0) {
				alert(eval("wb_msg_select_train_type"));
				return false
			}
		}else{
			spec.value += frm.itm_type.options[frm.itm_type.selectedIndex].value + d_f;
		}
	}
	return true
}

function _wbMgtGetShowOldVersion(frm,spec) {
	if (frm.show_old_version) {
		if( frm.show_old_version.type = 'hidden' ) {
			spec.name += "show_old_version" + d_f;
			spec.value += frm.show_old_version.value + d_f;		
		} else {
	        var checkedIdx = -1;
		    for (var i = 0; i < frm.show_old_version.length; i++) {
		        if (frm.show_old_version[i].checked) {
		            checkedIdx = i;
		            break;
		        }
		    }
			spec.name += "show_old_version" + d_f;
			spec.value += frm.show_old_version[checkedIdx].value + d_f;
		}
	}
}

function _wbMgtGetAtsId(frm,lang,spec){

//	if(frm.ats_id_lst_chkbox){
	if (frm.ats_id_lst){
		frm.ats_id_lst.value = _wbMgtRptGetAttStatusLst(frm);
		if(frm.ats_id_lst.value==""){
			alert(eval("wb_msg_"+lang+"_sel_att_status"));
			return false;
		}else{
			spec.name += "ats_id" + d_f;
			spec.value += frm.ats_id_lst.value + d_f;			
		}
	}

	return true;
}		

function _wbMgtGetAttStartDatetime(frm,lang,spec){
	if(frm.att_start_datetime_yy){
		spec.name += "att_start_datetime" + d_f;
		if((frm.att_start_datetime_yy.value!=""&&frm.att_start_datetime_yy.value!=null)||
		   (frm.att_start_datetime_mm.value!=""&&frm.att_start_datetime_mm.value!=null)||
		   (frm.att_start_datetime_dd.value!=""&&frm.att_start_datetime_dd.value!=null)){
		   	var txtFldName = '';
		   	if (frm.lab_rpt_datetime && frm.lab_rpt_datetime.value.length > 0) {
		   		txtFldName = frm.lab_rpt_datetime.value;
		   	} else {
		   		txtFldName = "period";
		   	}
			if(!wbUtilsValidateDate("document." + frm.name + ".att_start_datetime",txtFldName)){
				return false;
			}
			spec.value += frm.att_start_datetime_yy.value + "-" + frm.att_start_datetime_mm.value + "-" + frm.att_start_datetime_dd.value + " 00:00:00.00" + d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}

function _wbMgtGetAttEndDatetime(frm,lang,spec){
	if(frm.att_end_datetime_yy){
		spec.name += "att_end_datetime" + d_f;
		if((frm.att_end_datetime_yy.value!=""&&frm.att_end_datetime_yy.value!=null)||
		   (frm.att_end_datetime_mm.value!=""&&frm.att_end_datetime_mm.value!=null)||
		   (frm.att_end_datetime_dd.value!=""&&frm.att_end_datetime_dd.value!=null)){
		   	var txtFldName = '';
		   	if (frm.lab_rpt_datetime && frm.lab_rpt_datetime.value.length > 0) {
		   		txtFldName = frm.lab_rpt_datetime.value;
		   	} else {
		   		txtFldName = "period";
		   	}
			if(!wbUtilsValidateDate("document." + frm.name + ".att_end_datetime",txtFldName)){
				return false;
			}
			spec.value += frm.att_end_datetime_yy.value + "-" + frm.att_end_datetime_mm.value + "-" + frm.att_end_datetime_dd.value + " 23:59:59.00"+ d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}

function _wbMgtGetAttCreateStartDatetime(frm,lang,spec){
	if(frm.att_create_start_datetime_yy){
		spec.name += "att_create_start_datetime" + d_f;
		if((frm.att_create_start_datetime_yy.value!=""&&frm.att_create_start_datetime_yy.value!=null)||
		   (frm.att_create_start_datetime_mm.value!=""&&frm.att_create_start_datetime_mm.value!=null)||
		   (frm.att_create_start_datetime_dd.value!=""&&frm.att_create_start_datetime_dd.value!=null)){
			var label = eval('wb_msg_' + lang + '_start_date');
			// 特例：特殊用法，直接参入提示语
			if(frm.special_time_title != null || frm.special_time_title != undefined) {
				label = frm.special_time_title.value;
			}
			if(!wbUtilsValidateDate("document." + frm.name + ".att_create_start_datetime",label)){
				return false;
			}
			spec.value += frm.att_create_start_datetime_yy.value + "-" + frm.att_create_start_datetime_mm.value + "-" + frm.att_create_start_datetime_dd.value + " 00:00:00.00" + d_f;
		}else{
			spec.value += d_f;
		}
	} else if(frm.att_create_start_datetime_json) {
		spec.name += "att_create_start_datetime" + d_f;
		if(frm.enroll_start_date.value === "") {
			frm.att_create_start_datetime.value = "";
		} else {
			frm.att_create_start_datetime.value = frm.enroll_start_date.value + " 00:00:00.00";
		}
		spec.value += frm.att_create_start_datetime.value + d_f;
	}
	return true;
}

function _wbMgtGetAttCreateEndDatetime(frm,lang,spec){
	if(frm.att_create_end_datetime_yy){
		spec.name += "att_create_end_datetime" + d_f;
		if((frm.att_create_end_datetime_yy.value!=""&&frm.att_create_end_datetime_yy.value!=null)||
		   (frm.att_create_end_datetime_mm.value!=""&&frm.att_create_end_datetime_mm.value!=null)||
		   (frm.att_create_end_datetime_dd.value!=""&&frm.att_create_end_datetime_dd.value!=null)){
			var label = eval('wb_msg_' + lang + '_start_date');
			// 特例：特殊用法，直接参入提示语
			if(frm.special_time_title != null || frm.special_time_title != undefined) {
				label = frm.special_time_title.value;
			}
			if(!wbUtilsValidateDate("document." + frm.name + ".att_create_end_datetime",label)){
				return false;
			}
			spec.value += frm.att_create_end_datetime_yy.value + "-" + frm.att_create_end_datetime_mm.value + "-" + frm.att_create_end_datetime_dd.value + " 23:59:59.00"+ d_f;
		}else{
			spec.value += d_f;
		}
	} else if(frm.att_create_end_datetime_json) {
		spec.name += "att_create_end_datetime" + d_f;
		if(frm.enroll_end_date.value === "") {
			frm.att_create_end_datetime.value = "";
		} else {
			frm.att_create_end_datetime.value = frm.enroll_end_date.value + " 00:00:00.00";
		}
		spec.value += frm.att_create_end_datetime.value.replace(/00:00:00.00/g, "23:59:59.00") + d_f;
	}
	return true;
}
function _wbMgtGetStartDatetime(frm,lang,spec){
	if(frm.start_datetime_yy){
		spec.name += "start_datetime" + d_f;
		if((frm.start_datetime_yy.value!=""&&frm.start_datetime_yy.value!=null)||
		   (frm.start_datetime_mm.value!=""&&frm.start_datetime_mm.value!=null)||
		   (frm.start_datetime_dd.value!=""&&frm.start_datetime_dd.value!=null)){
		   	var txtFldName = '';
		   	if (frm.lab_rpt_datetime && frm.lab_rpt_datetime.value.length > 0) {
		   		txtFldName = frm.lab_rpt_datetime.value;
		   	} else {
		   		txtFldName = "period";
		   	}
			if(!wbUtilsValidateDate("document." + frm.name + ".start_datetime",txtFldName)){
				return false;
			}
			spec.value += frm.start_datetime_yy.value + "-" + frm.start_datetime_mm.value + "-" + frm.start_datetime_dd.value + " 00:00:00.00" + d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}

function _wbMgtGetEndDatetime(frm,lang,spec){
	if(frm.end_datetime_yy){
		spec.name += "end_datetime" + d_f;
		if((frm.end_datetime_yy.value!=""&&frm.end_datetime_yy.value!=null)||
		   (frm.end_datetime_mm.value!=""&&frm.end_datetime_mm.value!=null)||
		   (frm.end_datetime_dd.value!=""&&frm.end_datetime_dd.value!=null)){
		   	var txtFldName = '';
		   	if (frm.lab_rpt_datetime && frm.lab_rpt_datetime.value.length > 0) {
		   		txtFldName = frm.lab_rpt_datetime.value;
		   	} else {
		   		txtFldName = "period";
		   	}
			if(!wbUtilsValidateDate("document." + frm.name + ".end_datetime",txtFldName)){
				return false;
			}
			spec.value += frm.end_datetime_yy.value + "-" + frm.end_datetime_mm.value + "-" + frm.end_datetime_dd.value + " 23:59:59.00"+ d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}
function _wbMgtRptGetFormSpec(frm,lang,spec,require){

		//ent_id_lst		
		if(_wbMgtRptGetEntIDLst(frm,lang,spec,require) == false){return false;}
		//mod_id_lst
		if(_wbMgtRptGetModIDLst(frm,lang,spec,require) == false){return false;}
		// my staff / my direct staff
		if(_wbMgtRptGetSUsgEntIdLst(frm,lang,spec,require) == false){return false;}
		//tnd_id_lst
		if(_wbMgtRptGetTndIdLst(frm,lang,spec,false) == false){return false;}
		// mod_id (survey)

		if(_wbMgtGetModId(frm,lang,spec) == false){return false;}
		
		
		// itm_id 
		if(_wbMgtGetItmId(frm,lang,spec) == false){return false;}
		//itm_title

		if( _wbMgtRptGetItemIDLst(frm, lang, spec) == false ) {return false};
		
		//tc_id
		if(_wbMgtRptGetTCId(frm, spec) == false){return false;}
		
		if(_wbMgtGetTargetType(frm,lang,spec) == false){return false;}
		
		if( _wbMgtRptGetCourseSelType(frm,lang,spec) == false){return false};
		
		if( _wbMgtRptGetCatalogAndItmTypeLst(frm,lang,spec) == false){return false};
		
		_wbMgtGetLrnCreteria(frm, lang, spec);
		

		_wbMgtGetItmTitle(frm,spec);
		//itm_title_partial_ind

		_wbMgtGetItmTitlePartialInd(frm,spec);
				//itm_type
		if (_wbMgtGetItmType(frm,spec) == false){return false;}

		if (_wbMgtRptGetItmTypeLst(frm, lang, spec)==false){return false;};

		//training scope
		_wbMgtGetTrainScope(frm,spec);
		
		//ats_id
		//if( _wbMgtGetAtsId(frm,lang,spec) == false ) { return false;}  Updated
		//att_start_date_time
		if(_wbMgtGetAttStartDatetime(frm,lang,spec) == false){return false;}
		//att_end_date_time

		if(_wbMgtGetAttEndDatetime(frm,lang,spec) == false){return false;}	
		//start_date_time
		
		if(_wbMgtGetAttCreateStartDatetime(frm,lang,spec) == false){return false;}
		//att_end_date_time

		if(_wbMgtGetAttCreateEndDatetime(frm,lang,spec) == false){return false;}	
		//start_date_time		

		//comparison
		if(frm.att_create_start_datetime_yy && frm.att_create_start_datetime_yy.value.length > 0 && frm.att_create_end_datetime_yy.value.length > 0) {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'att_create_start_datetime', 
				end_obj : 'att_create_end_datetime'
				})) {
				return false;	
			}
		}
		if(frm.start_datetime_yy && frm.start_datetime_yy.value.length > 0 && frm.end_datetime_yy.value.length > 0) {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'start_datetime', 
				end_obj : 'end_datetime'
				})) {
				return false;	
			}
		}
		
		if(_wbMgtGetStartDatetime(frm,lang,spec) == false){return false;}
		//end_date_time

		if(_wbMgtGetEndDatetime(frm,lang,spec) == false){return false;}		
			
		//show old version
		_wbMgtGetShowOldVersion(frm,spec)
		//lrn_content_lst
		//_wbMgtRptGetContentLst(frm,spec,'lrn_')	
		//cos_content_lst
		//_wbMgtRptGetContentLst(frm,spec,'cos_')
		_wbMgtRptGetRspLrnOrCos(frm, spec)
		//usr_content_lst
		
		
		_wbMgtRptGetContentLst(frm,spec,'usr_')	
		//itm_content_lst
		_wbMgtRptGetContentLst(frm,spec,'itm_')	
		//run_content_lst
		_wbMgtRptGetContentLst(frm,spec,'run_')		
		//content_lst
		_wbMgtRptGetContentLst(frm,spec)		
		//datetime_restriction
		
		_wbMgtRptGetFacType(frm, spec)
		

		//for hidden spec_name and spec_value
		if(frm.hidden_spec_name){
			if(frm.hidden_spec_name.length){
				var i=0;
				for(i=0;i<frm.hidden_spec_name.length;i++){
					spec.name += frm.hidden_spec_name[i].value + d_f;;
					spec.value += frm.hidden_spec_value[i].value + d_f;;					
				}	
			}else{
				spec.name += frm.hidden_spec_name.value + d_f;;
				spec.value += frm.hidden_spec_value.value + d_f;;
			}
		}
		//新增的参数
	        //完成状态
	        if(frm.ats_id!=null){
                   var count=0;
	           while(count<frm.ats_id.length){
	              if(frm.ats_id[count].checked==true){
	                spec.name+="ats_id"+ ':_:_:';
	                spec.value+=frm.ats_id[count].value+ ':_:_:';
	                break;
	               }
	               count++;  
	            }      
	        }

	      //排序字段
	      if(frm.sort_col!=null){
	         spec.name+="sort_col"+ ':_:_:';
	         spec.value+=frm.sort_col.value+ ':_:_:';
	        }
	      //asc or desc
	      if(frm.sort_order!=null){
	        spec.name+="sort_order"+ ':_:_:';
	        spec.value+=frm.sort_order.value+ ':_:_:';
	      }
	      //Item per page
	      if(frm.page_size!=null){
	        spec.name+="page_size"+ ':_:_:';
	        spec.value+=frm.page_size.value+ ':_:_:';
	      }
		  //jifen
	      if(frm.rpt_type.value == 'CREDIT'){
			if(_wbMgtGetJiFenUsgEntIdLst(frm,lang,spec,require) == false){return false;}
			//show deleted user
			if(frm.include_del_usr_ind!=null) {
				for (i=0;i<frm.include_del_usr_ind.length;i++) {
					 if(frm.include_del_usr_ind[i].checked==true) {
					 	spec.name+="include_del_usr_ind"+':_:_:';
					 	spec.value+=frm.include_del_usr_ind[i].value+':_:_:';
					 	break;
					}
				}
			}
			//is detail report
			if(frm.is_detail_ind!=null) {
				for (i=0;i<frm.is_detail_ind.length;i++) {
					 if(frm.is_detail_ind[i].checked==true) {
					 	spec.name+="is_detail_ind"+':_:_:';
					 	spec.value+=frm.is_detail_ind[i].value+':_:_:';
					 	break;
					 }
				}
			}
			//show usg only
			if(frm.show_usg_only!=null) {
				for (i=0;i<frm.show_usg_only.length;i++) {
					 if(frm.show_usg_only[i].checked==true) {
					 	spec.name+="show_usg_only"+':_:_:';
					 	spec.value+=frm.show_usg_only[i].value+':_:_:';
					 	break;
					}
				}
			} 
		}
		return true;

}

function _wbMgtRptGetFormSpec1(frm,lang,spec,require,rpt_type) {
	         //ent_id_lst
		if(_wbMgtRptGetEntIDLst(frm,lang,spec,require) == false){return false;}
		// my staff / my direct staff
		if(_wbMgtRptGetSUsgEntIdLst(frm,lang,spec,require) == false){return false;}
		//tnd_id_lst
		if(_wbMgtRptGetTndIdLst(frm,lang,spec,false) == false){return false;}
		// mod_id (survey)
        if(rpt_type == 'ASSESSMENT_QUE_GRP'){
        	if(_wbMgtGetModId1(frm,lang,spec) == false){return false;} 
        }else{
			if(_wbMgtGetModId(frm,lang,spec) == false){return false;} 
		}
		
		if(_wbMgtGetModIdLst(frm,lang,spec) == false){return false;}
		
		//itm_title

		if( _wbMgtRptGetItemIDLst(frm, lang, spec) == false ) {return false};
		
		if(_wbMgtGetTargetType(frm,lang,spec) == false){return false;}
		
		if( _wbMgtRptGetCourseSelType(frm,lang,spec) == false){return false};
		
		if( _wbMgtRptGetCatalogAndItmTypeLst(frm,lang,spec) == false){return false};
		
		_wbMgtGetLrnCreteria(frm, lang, spec);
		

		_wbMgtGetItmTitle(frm,spec);
		//itm_title_partial_ind

		_wbMgtGetItmTitlePartialInd(frm,spec);
		//itm_type
		_wbMgtGetItmType(frm,spec)
		//ats_id
		//if( _wbMgtGetAtsId(frm,lang,spec) == false ) { return false;}  Updated
		//att_start_date_time

		if(_wbMgtGetAttStartDatetime(frm,lang,spec) == false){return false;}
		//att_end_date_time

		if(_wbMgtGetAttEndDatetime(frm,lang,spec) == false){return false;}	
		//start_date_time
		
		if(_wbMgtGetAttCreateStartDatetime(frm,lang,spec) == false){return false;}
		//att_end_date_time

		if(_wbMgtGetAttCreateEndDatetime(frm,lang,spec) == false){return false;}	
		//start_date_time		

		//comparison
		if(frm.att_create_start_datetime_yy && frm.att_create_start_datetime_yy.value.length > 0 && frm.att_create_end_datetime_yy.value.length > 0) {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'att_create_start_datetime', 
				end_obj : 'att_create_end_datetime'
				})) {
				return false;	
			}
		}
		
		if(_wbMgtGetStartDatetime(frm,lang,spec) == false){return false;}
		//end_date_time

		if(_wbMgtGetEndDatetime(frm,lang,spec) == false){return false;}	
		
		//attempt start_date_time
		
		if(_WbmgtgetattemptStartdatetime(frm,lang,spec) == false){return false;}
		//attempt end_date_time

		if(_WbmgtgetattemptEnddatetime(frm,lang,spec) == false){return false;}	
		
		//comparison
		if(frm.attempt_start_datetime_yy && frm.attempt_start_datetime_yy.value.length > 0 && frm.attempt_end_datetime_yy.value.length > 0) {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'attempt_start_datetime', 
				end_obj : 'attempt_end_datetime'
				})) {
				return false;	
			}
		}
		
		// itm_id 
		if(_wbMgtGetItmId(frm,lang,spec) == false){return false;}		
			
		//show old version
		_wbMgtGetShowOldVersion(frm,spec)
		//lrn_content_lst
		//_wbMgtRptGetContentLst(frm,spec,'lrn_')	
		//cos_content_lst
		//_wbMgtRptGetContentLst(frm,spec,'cos_')
		_wbMgtRptGetRspLrnOrCos(frm, spec)
		//usr_content_lst
		_wbMgtRptGetContentLst(frm,spec,'usr_')	
		//itm_content_lst
		_wbMgtRptGetContentLst(frm,spec,'itm_')	
		//run_content_lst
		_wbMgtRptGetContentLst(frm,spec,'run_')		
		//content_lst
		_wbMgtRptGetContentLst(frm,spec)		
		//datetime_restriction
		
		//for hidden spec_name and spec_value
		if(frm.hidden_spec_name){
			if(frm.hidden_spec_name.length){
				var i=0;
				for(i=0;i<frm.hidden_spec_name.length;i++){
					spec.name += frm.hidden_spec_name[i].value + d_f;;
					spec.value += frm.hidden_spec_value[i].value + d_f;;					
				}	
			}else{
				spec.name += frm.hidden_spec_name.value + d_f;;
				spec.value += frm.hidden_spec_value.value + d_f;;
			}
		}
	        //完成状态
	        if(frm.ats_id!=null){
                   var count=0;
	           while(count<frm.ats_id.length){
	              if(frm.ats_id[count].checked==true){
	                spec.name+="ats_id"+ ':_:_:';
	                spec.value+=frm.ats_id[count].value+ ':_:_:';
	                break;
	               }
	               count++;  
	            }      
	        }  

	      //排序字段
	      if(frm.sort_col!=null){
	         spec.name+="sort_col"+ ':_:_:';
	         spec.value+=frm.sort_col.value+ ':_:_:';
	        }
	        
//	      if(rpt_type=='LEARNING_ACTIVITY_LRN'){
//	      	  //alert("!!!!");
//	      	  var count=0;
//	      	  while(count<frm.itm_content.length){
//	      	  	var ele=frm.itm_content[count];
//	      	  	if(ele.id =='label_field01'&& ele.checked == true){
//	      	  		spec.name +="sort_col1"+ ':_:_:';
//	      	  		spec.value += "itm_code"+':_:_:';
//	      	  		break;
//	      	  	}
//	      	  	count++;
//	          }
//	          //alert(spec.name);
//	          //alert(spec.value);
//	          if(count==frm.itm_content.length){
//	          	spec.name += "sort_col1"+ ':_:_:';
//	      	  	spec.value += "itm_title"+':_:_:';
//	           }
//	       }
	       
	       if(rpt_type=='LEARNING_ACTIVITY_COS'){
	       	    var count = 0;
	       	    while(count < frm.usr_content.length){
	       	    	var ele = frm.usr_content[count];
	       	    	if(ele.id == 'label_usr_id'&& ele.checked == true){
	       	    		spec.name += "sort_col1"+":_:_:";
	       	    		spec.value += "usr_ste_usr_id"+':_:_:';
	       	    		break;
	       	    	}
	       	    	count++;
	       	    }
	       	    if(count==frm.usr_content.length){
	       	    	 spec.name += "sort_col1"+":_:_:";
	       	    	 spec.value += "usr_display_bil"+':_:_:';
	       	    }
	       }
	      
	      //asc or desc
	      if(frm.sort_order!=null){
	        spec.name+="sort_order"+ ':_:_:';
	        spec.value+=frm.sort_order.value+ ':_:_:';
	      }
	      //Item per page
	      if(frm.page_size!=null){
	        spec.name+="page_size"+ ':_:_:';
	        spec.value+=frm.page_size.value+ ':_:_:';
	      }

	        //show no record
         if(frm.include_no_record!=null) {
         	for (i=0;i<frm.include_no_record.length;i++) {
         		 if(frm.include_no_record[i].checked==true) {
         		 	spec.name+="include_no_record"+':_:_:';
         		 	spec.value+=frm.include_no_record[i].value+':_:_:';
         		 	break;
         		}
         	}
         }
         
         //show stat only
         if(frm.show_stat_only!=null) {
         	for (i=0;i<frm.show_stat_only.length;i++) {
         		 if(frm.show_stat_only[i].checked==true) {
         		 	spec.name+="show_stat_only"+':_:_:';
         		 	spec.value+=frm.show_stat_only[i].value+':_:_:';
         		 	break;
         		}
         	}
        }
         if(rpt_type=='LEARNING_ACTIVITY_BY_COS' ){
         	spec.name+="show_stat_only"+':_:_:';
         	spec.value+='true:_:_:';
         }
        //sort type and report columns
         if(rpt_type == 'ASSESSMENT_QUE_GRP'){
        	if(_wbMgtGetAttemptType(frm,lang,spec) == false){return false;} 
            if(_wbMgtGetContentLst(frm,lang,spec) == false){return false;} 
            if(_wbMgtGetGroupBy(frm,lang,spec) == false){return false;} 
            _wbMgtGetGroupBy
        }
        
		return true;
}

function _wbMgtRptGetReportListURL(rpt_type_lst, isShowPublic) {
    if (rpt_type_lst == null) {
        rpt_type_lst = '';
    }
    if (isShowPublic == null || isShowPublic == '') {
        isShowPublic = 'y';
    }
	url = wb_utils_invoke_disp_servlet('module','report.ReportModule','cmd','get_rpt_lst','rpt_type_lst',rpt_type_lst,'show_public',isShowPublic,'stylesheet','rpt_all.xsl');
	return url;
}


function _wbMgtRptGetCourseSelType(frm,lang,spec){

	if(frm.course_sel_type){
		for(i=0; i<frm.course_sel_type.length; i++){
			if( frm.course_sel_type[i].checked ){
				return true;
			}
		}
		alert(eval('wb_msg_' + lang+'_sel_cos_title_or_type_or_catalog'));
		return false;
	}
	return true;
}

function _wbMgtRptGetCatalogAndItmTypeLst(frm, lang, spec){
	
	if( frm.itm_type_lst && frm.tnd_id ) {
	
		if( !frm.course_sel_type || frm.course_sel_type[2].checked ){
			var temp_lst = "";
			var tnd_id_lst = "";
			
			if( frm.itm_type_lst.length ) {
				for(i=0;i<frm.itm_type_lst.length;i++){
					if(frm.itm_type_lst[i].checked == true){
						temp_lst += frm.itm_type_lst[i].value + d_v;
					}
				}
			}else if(frm.itm_type_lst.type == 'hidden'){
				temp_lst += frm.itm_type_lst.value + d_v;
			}else{
				if( frm.itm_type_lst.checked )
					temp_lst += frm.itm_type_lst.value + d_v;
			}
			if( temp_lst == "" && frm.tnd_id.length == 0 ){
				alert(eval('wb_msg_' + lang + '_sel_cos_type_or_catalog'));
				return false;
			}
			
			if( temp_lst != "" ) {
				temp_lst = temp_lst.substr(0,temp_lst.length-1);
				spec.name += "itm_type" + d_f;
				spec.value += temp_lst + d_f;
			}			
/*			
			for(i=0; i<frm.tnd_id.options.length; i++){
				tnd_id_lst += frm.tnd_id.options[i].value + "~";
			}
			if( tnd_id_lst != "" ) {
				tnd_id_lst = tnd_id_lst.substr(0,tnd_id_lst.length-1);
				spec.name += "tnd_id" + d_f;
				spec.value += tnd_id_lst + d_f;
			}
*/
		}else if(!frm.course_sel_type || frm.course_sel_type[0].checked){
			var temp_lst = "";
			if( frm.itm_type_lst.length ) {
				for(i=0;i<frm.itm_type_lst.length;i++){
					if(frm.itm_type_lst[i].checked == true){
						temp_lst += frm.itm_type_lst[i].value + d_v;
					}
				}
			}else if(frm.itm_type_lst.type == 'hidden'){
				temp_lst += frm.itm_type_lst.value + d_v;
			}else{
				if( frm.itm_type_lst.checked )
					temp_lst += frm.itm_type_lst.value + d_v;
			}		
			if( temp_lst != "" ) {
				temp_lst = temp_lst.substr(0,temp_lst.length-1);
				spec.name += "itm_type" + d_f;
				spec.value += temp_lst + d_f;
			}				
		}
	}
	return true;
}

function _wbMgtRptGetModIDLst(frm,lang,spec,require){
	var temp_lst_mod = "";
	if(frm.mod_id_lst){
        for(i=0;i<frm.mod_id_lst.options.length;i++){
            temp_lst_mod += frm.mod_id_lst.options[i].value + d_v;
        }
        if(temp_lst_mod ==""){
            alert(wb_pls_specify_mod);
            return false;
        }else{
            temp_lst_mod = temp_lst_mod.substr(0,temp_lst_mod.length-1)	
            spec.name += "mod_id" + d_f;
            spec.value += temp_lst_mod + d_f;
        }
	}
	return true;
}

function _wbMgtGetModId1(frm,lang,spec){
	if(frm.mod_id){
		if(frm.mod_id.type.search('select') != -1){

			if(frm.mod_id.options[frm.mod_id.selectedIndex].value == ""){
				  alert(wb_pls_specify_mod);
				return false;
			}else{
				spec.name += "mod_id" + d_f;
				spec.value += frm.mod_id.options[frm.mod_id.selectedIndex].value + d_f;
			}			
		}else{
			if(frm.mod_id.value == ""){
	
				alert(wb_pls_specify_mod);
				return false;
			}else{
				spec.name += "mod_id" + d_f;
				spec.value += frm.mod_id.value + d_f;
			}
		}
	}
}

function _wbMgtGetModIdLst(frm,lang,spec) {
	if(frm.mod_id_lst){
		if (!frm.course_sel_type || frm.course_sel_type[3].checked) {
			var temp_lst = ""
			for(i=0;i<frm.mod_id_lst.options.length;i++) {
				temp_lst += frm.mod_id_lst.options[i].value + d_v;
			}
	
			if(temp_lst == ""){
				alert(wb_pls_specify_tst);
				return false;
			}else{
				temp_lst = temp_lst.substr(0,temp_lst.length-1)
				spec.name += "mod_id" + d_f;
				spec.value += temp_lst + d_f;
				if (frm.mod_itm_id) {
					spec.name += "mod_itm_id" + d_f;
					spec.value += frm.mod_itm_id.value + d_f;	
				}
			}
		}
	}
}

function _wbMgtGetAttemptType(frm,lang,spec){
	if(frm.attempt_type){
		if (frm.attempt_type[0].checked || frm.attempt_type[1].checked){
			var temp = "";
			if(frm.attempt_type[0].checked && frm.attempt_type[1].checked){
				temp = frm.attempt_type[0].value + d_v + frm.attempt_type[1].value
			}else if(frm.attempt_type[0].checked){
				temp = frm.attempt_type[0].value
			}else if(frm.attempt_type[1].checked){
				temp = frm.attempt_type[1].value
			}
			spec.name += "attempt_type" + d_f;
			spec.value += temp + d_f;
		}else{
			alert(wb_pls_specify_attempt_type);
	     	return false;
		}
	}
	return true;
}

function _wbMgtGetContentLst(frm,lang,spec){
	var temp_lst_content = "";
	if(frm.content_lst){
		if( frm.content_lst.length ) {
			    var has_selected = false;
				for(i=0;i<frm.content_lst.length;i++){
					if(frm.content_lst[i].checked == true && frm.content_lst[i].disabled == false){
						temp_lst_content += frm.content_lst[i].value + d_v;
						has_selected = true;
					}
				}
				if(!has_selected){
					alert(wb_pls_specify_content_lst);
					return false;
				}
			}else if(frm.content_lst.type == 'hidden'){
				temp_lst_content += frm.content_lst.value + d_v;
			}else{
				if( frm.content_lst.checked )
					temp_lst_content += frm.content_lst.value + d_v;
			}
			spec.name += "content_lst" + d_f;
            spec.value += temp_lst_content + d_f;
			return true;
		}
}

function _WbmgtgetattemptStartdatetime(frm,lang,spec){
	if(frm.attempt_start_datetime_yy){
		spec.name += "attempt_start_datetime" + d_f;
		if((frm.attempt_start_datetime_yy.value!=""&&frm.attempt_start_datetime_yy.value!=null)||
		   (frm.attempt_start_datetime_mm.value!=""&&frm.attempt_start_datetime_mm.value!=null)||
		   (frm.attempt_start_datetime_dd.value!=""&&frm.attempt_start_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".attempt_start_datetime",frm.lab_sub_date.value)){
				return false;
			}
			spec.value += frm.attempt_start_datetime_yy.value + "-" + frm.attempt_start_datetime_mm.value + "-" + frm.attempt_start_datetime_dd.value + " 00:00:00.00" + d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}

function _WbmgtgetattemptEnddatetime(frm,lang,spec){
	
	if(frm.attempt_end_datetime_yy){
		spec.name += "attempt_end_datetime" + d_f;
		if((frm.attempt_end_datetime_yy.value!=""&&frm.attempt_end_datetime_yy.value!=null)||
		   (frm.attempt_end_datetime_mm.value!=""&&frm.attempt_end_datetime_mm.value!=null)||
		   (frm.attempt_end_datetime_dd.value!=""&&frm.attempt_end_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".attempt_end_datetime",eval('wb_msg_' + lang + '_enrollment_date'))){
				return;
			}
			spec.value += frm.attempt_end_datetime_yy.value + "-" + frm.attempt_end_datetime_mm.value + "-" + frm.attempt_end_datetime_dd.value + " 23:59:59.00"+ d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}

function _wbMgtGetGroupBy(frm,lang,spec){
	if(frm.group_by){
		if(frm.group_by.type.search('select') != -1){
			spec.name += "group_by" + d_f;
			spec.value += frm.group_by.options[frm.group_by.selectedIndex].value + d_f;
		}
    }
}

function _wbMgtGetItmId(frm,lang,spec){
	if(frm.itm_id){
		spec.name += "itm_id" + d_f;
		spec.value += frm.itm_id.value + d_f;
	}
	return true;
}

function _wbMgtRptGetTCId(frm, spec) {
	if (frm.training_center) {
		var tcr_id = frm.training_center.options[0].value;
		if (tcr_id != '') {
			spec.name += "tcr_id" + d_f;
			spec.value += frm.training_center.options[0].value + d_f;
		} else {
			alert(wb_msg_pls_input_tcr);
			return false;
		}
	}
	return true
}

function _wbMgtGetTrainScope(frm, spec){
	if (frm.train_scope) {
		for (var i = 0; i < frm.train_scope.length; i++) {
			if (frm.train_scope[i].checked) {
				spec.name += "train_scope" + d_f;
				spec.value += frm.train_scope[i].value + d_f;
			}
		}
	} else {
		//if don't has train_scope radio, use "UNPLAN" as default (system is STANDARD)
		spec.name += "train_scope" + d_f;
		spec.value += "UNPLAN" + d_f;
	}
}

function _wbMgtRptGetFacType(frm,spec) {
	if (frm.fac_type) {
		spec.name += "fac_type" + d_f;
		spec.value += frm.fac_type.value + d_f;
	}
}

function _wbMgtGetJiFenUsgEntIdLst(frm,lang,spec,require){
	var temp_lst = "";
	if(frm.usg_ent_id_lst){
		if(frm.all_usg_ind){
			if (frm.all_usg_ind[0].checked){		
	    			spec.name += "all_usg_ind" + d_f;
	    			spec.value += "1" + d_f;		
	    		} else if (frm.all_usg_ind[1].checked==true){
	    			spec.name += "all_usg_ind" + d_f;
	    			spec.value += "0" + d_f;
		                for(i=0;i<frm.usg_ent_id_lst.options.length;i++){
		                    temp_lst += frm.usg_ent_id_lst.options[i].value + d_v;
		                }
		                if(temp_lst ==""){
		                    if(require == 'no'){
		                        var lab_group = frm.lab_group ? frm.lab_group.value : "Group(sys)"
		                        alert(eval("wb_msg_"+lang+"_sel_lrn_grp"));
		                        return false;
		                    }
		                }else{
		                    temp_lst = temp_lst.substr(0,temp_lst.length-1)	
		                    spec.name += "usg_ent_id" + d_f;
		                    spec.value += temp_lst + d_f;		
		                }
		        } 	
		}	
	}	
}

function _wbMgtRptGetItmTypeLst(frm, lang, spec){
	//console.log(frm.course_sel_type[0]);
	if(frm.course_sel_type && frm.course_sel_type[0].checked && frm.itm_type_lst){
		if( frm.itm_type_lst.length ) {
			for(i=0;i<frm.itm_type_lst.length;i++){
				if(frm.itm_type_lst[i].checked == true){
					return true;
				}
			}
		}
		alert(eval("wb_msg_"+lang+"_select_add_item_type"));
		return false;
	}else{
		return true;
	}
	
}

function wbMgtRptInsertReportPrepPopupJsp(frm,rte_id,usr_ent_id,rpt_type,lang){
	var spec = new Spec()
	var i; 
        
	if(lang==null || lang==""){
		lang="en";
	}
	
	var rpt_type_learner = rpt_type.indexOf("LEARNER");
	if(rpt_type_learner<0) {
		rpt_type_learner = rpt_type.indexOf("learner");
	}
	if(rpt_type == 'LEARNER' || rpt_type == 'learner' || rpt_type_learner>=0){
		var require = 'yes';
	}else{
		var require = 'no';
	}

    if(!validateForm()){
          return;
    }
    if(rpt_type=='ASSESSMENT_QUE_GRP'||rpt_type=='LEARNING_ACTIVITY_LRN'||rpt_type=='LEARNING_ACTIVITY_COS' ||rpt_type=='LEARNING_ACTIVITY_BY_COS' || rpt_type == 'EXAM_PAPER_STAT'){
		if(_wbMgtRptGetFormSpec1(frm,lang,spec,require,rpt_type) == false){
			return;
		}
	} else{
		if(_wbMgtRptGetFormSpecJsp(frm,lang,spec,require) == false){
			return;
		}
    }
	
	//datetime_restriction
	spec.name+='datetime_restriction'
	spec.value+='att_create_timestamp'	
	frm.cmd.value = "echo_spec_param";
	frm.module.value = "report.ReportModule";
	frm.download.value = "0";	
	frm.rte_id.value = rte_id;
	frm.usr_ent_id.value = usr_ent_id;
	frm.rpt_type.value = rpt_type;
	frm.rpt_type_lst.value = rpt_type;
	frm.stylesheet.value = "rpt_lrn_srh_popup.xsl";
	frm.url_failure.value = parent.location.href;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value
	frm.action = wb_utils_disp_servlet_url;
	frm.target = window.name;
	//frm.method = "get";
	frm.method = "post";
	frm.window_name.value = "";
	frm.submit();
}

function _wbMgtRptGetFormSpecJsp(frm,lang,spec,require){
	var temp_lst = "";
	var temp_lst_usr = "";
	var defined = false;
	
	if(frm.exportUser){
		/*类型选择*/
    	if (frm.exportUser[0].checked){
    		defined = true;	
    		spec.name += "exportUser" + d_f;
    		if($("input[name='includeDelUser']:checked").val()){
                 spec.value += "0" +","+$("input[name='includeDelUser']:checked").val() + d_f;        
    		}else{
                spec.value += "0" + d_f;        
    		}
           
    	}else if (frm.exportUser[1].checked){
    		spec.name += "exportUser" + d_f;
    		spec.value += "1" + d_f;		
    	}else{
    		spec.name += "exportUser" + d_f;
    		spec.value += "2" + d_f;	
    	}
    	
    	/*指定学员*/
    	if (defined == false && frm.exportUser[1].checked==true){
            if (frm.includeDelUser){
                for(i=0;i<frm.exportUserIds.options.length;i++){
                    temp_lst += frm.exportUserIds.options[i].value + d_v;
                }
                if(temp_lst ==""){
                    if(require == 'yes' ){
                    var lab_group = frm.exportUser ? frm.exportUser.value : "Group(sys)"
                   // layer.alert(fetchLabel("label_core_report_141"),{title:fetchLabel("label_core_report_145")});
                    return false;
                    }
                }else{
                    temp_lst = temp_lst.substr(0,temp_lst.length-1)	
                    
                    spec.name += "exportUserIds" + d_f;
                    spec.value += frm.pageExportUserIds.value + d_f;        
                    
                    spec.name += "exportUserIdsName" + d_f;
                    spec.value += frm.pageExportUserIdsText.value + d_f;		
                }
            }
    	}
    	/*指定用户组*/
    	if (defined == false && frm.exportUser[2].checked==true){
            for(i=0;i<frm.exportGroupIds.options.length;i++){
                temp_lst += frm.exportGroupIds.options[i].value + d_v;
            }
            if(temp_lst ==""){
                if(require == 'yes'){
                    var lab_group = frm.exportUser ? frm.exportUser.value : "Group(sys)"
                    layer.alert(fetchLabel("label_core_report_142"),{title:fetchLabel("label_core_report_145")});
                    return false;
                }
            }else{
                temp_lst = temp_lst.substr(0,temp_lst.length-1)	
                spec.name += "exportGroupIds" + d_f;
                spec.value += frm.pageExportUserIds.value + d_f;        
                    
                spec.name += "exportGroupIdsName" + d_f;
                spec.value += frm.pageExportUserIdsText.value + d_f;        
            }
        } 
    }
	
	var definedCourse  = false;
	/*课程、考试类型组*/
	if (frm.exportCourse[0].checked){
		definedCourse = true;	
		spec.name += "exportCourse" + d_f;
		spec.value += "0" + d_f;		
	}else if (frm.exportCourse[1].checked){
		spec.name += "exportCourse" + d_f;
		spec.value += "1" + d_f;		
	}else{
		spec.name += "exportCourse" + d_f;
		spec.value += "2" + d_f;	
	}
	
	
    temp_lst = "";
	/*指定课程/考试*/
    if (definedCourse == false && frm.exportCourse[1].checked==true){
                for(i=0;i<frm.exportCourseIds.options.length;i++){
                    temp_lst += frm.exportCourseIds.options[i].value + d_v;
                }
                if(temp_lst ==""){
                    if(require == 'yes' ){
                            layer.alert(fetchLabel("label_core_report_143"),{title:fetchLabel("label_core_report_145")});
                            return false;
                    }
                }else{
                    temp_lst = temp_lst.substr(0,temp_lst.length-1) 
                    
                    spec.name += "exportCourseIds" + d_f;
                    spec.value += frm.pageExportCourseIds.value + d_f;        
                    
                    spec.name += "exportCourseIdsName" + d_f;
                    spec.value += frm.pageExportCourseIdsText.value + d_f;        
                }
     }
    temp_lst = "";
     /*指定课程/考试目录*/
    if (definedCourse == false && frm.exportCourse[2].checked==true){
            for(i=0;i<frm.exportCatalogIds.options.length;i++){
                temp_lst += frm.exportCatalogIds.options[i].value + d_v;
            }
            if(temp_lst ==""){
                if(require == 'yes'){
                            layer.alert(fetchLabel("label_core_report_144"),{title:fetchLabel("label_core_report_145")});
                            return false;
                }
            }else{
                temp_lst = temp_lst.substr(0,temp_lst.length-1) 
                spec.name += "exportCatalogIds" + d_f;
                spec.value += frm.pageExportCourseIds.value + d_f;        
                    
                spec.name += "exportCatalogIdsName" + d_f;
                spec.value += frm.pageExportCourseIdsText.value + d_f;        
            }
      } 
    
    
	
	/*课程、考试类型*/
    if(frm.courseType!=null){
        spec.name+="courseType"+ ':_:_:';
        spec.value+=frm.pageCourseType.value+ ':_:_:';    
    }
    
    /*报名开始时间*/
    if(frm.appnStartDatetime){
		spec.name += "appnStartDatetime" + d_f;
		if((frm.appn_start_datetime_yy.value!=""&&frm.appn_start_datetime_yy.value!=null)||
		   (frm.appn_start_datetime_mm.value!=""&&frm.appn_start_datetime_mm.value!=null)||
		   (frm.appn_start_datetime_dd.value!=""&&frm.appn_start_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".appn_start_datetime",eval('wb_msg_' + lang + '_start_date'))){
				return false;
			}
			spec.value += frm.appn_start_datetime_yy.value + "-" + frm.appn_start_datetime_mm.value + "-" + frm.appn_start_datetime_dd.value + " 00:00:00.00" + d_f;
		}else{
			spec.value += d_f;
		}
	}
    
    /*报名结束时间*/
    if(frm.appnEndDatetime){
		spec.name += "appnEndDatetime" + d_f;
		if((frm.appn_end_datetime_yy.value!=""&&frm.appn_end_datetime_yy.value!=null)||
		   (frm.appn_end_datetime_mm.value!=""&&frm.appn_end_datetime_mm.value!=null)||
		   (frm.appn_end_datetime_dd.value!=""&&frm.appn_end_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".appn_end_datetime",eval('wb_msg_' + lang + '_start_date'))){
				return false;
			}
			spec.value += frm.appn_end_datetime_yy.value + "-" + frm.appn_end_datetime_mm.value + "-" + frm.appn_end_datetime_dd.value + " 23:59:59.00"+ d_f;
		}else{
			spec.value += d_f;
		}
	}
    
    /*结训开始时间*/
    if(frm.attStartTime){
		spec.name += "attStartTime" + d_f;
		if((frm.att_start_time_yy.value!=""&&frm.att_start_time_yy.value!=null)||
		   (frm.att_start_time_mm.value!=""&&frm.att_start_time_mm.value!=null)||
		   (frm.att_start_time_dd.value!=""&&frm.att_start_time_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".att_start_time",eval('wb_msg_' + lang + '_start_date'))){
				return false;
			}
			spec.value += frm.att_start_time_yy.value + "-" + frm.att_start_time_mm.value + "-" + frm.att_start_time_dd.value + " 00:00:00.00" + d_f;
		}else{
			spec.value += d_f;
		}
	}
    
    /*结训结束时间*/
    if(frm.attEndTime){
		spec.name += "attEndTime" + d_f;
		if((frm.att_end_time_yy.value!=""&&frm.att_end_time_yy.value!=null)||
		   (frm.att_end_time_mm.value!=""&&frm.att_end_time_mm.value!=null)||
		   (frm.att_end_time_dd.value!=""&&frm.att_end_time_dd.value!=null)){
			if(!wbUtilsValidateDate("document." + frm.name + ".att_end_time",eval('wb_msg_' + lang + '_start_date'))){
				return false;
			}
			spec.value += frm.att_end_time_yy.value + "-" + frm.att_end_time_mm.value + "-" + frm.att_end_time_dd.value + " 23:59:59.00"+ d_f;
		}else{
			spec.value += d_f;
		}
	}
    
    /*报名、学习状态*/
    if(frm.appStatus!=null){
        spec.name+="appStatus"+d_f;
        spec.value+=frm.pageAppStatus.value+ d_f;
    }
    if(frm.courseStatus!=null){
	    spec.name+="courseStatus"+ d_f;
	    spec.value+=frm.pageCourseStatus.value+d_f;
    }
    
    /*结果数据统计方式*/
    //0课程 1学员
    if(frm.resultDataStatistic.value==0){
        spec.name+="resultDataStatistic"+d_f;
        spec.value += "0" + d_f;  
        if($("input[name='includeNoDataCourse']:checked").val()==1){
                 spec.name+="includeNoDataCourse"+d_f;
                 spec.value += $("input[name='includeNoDataCourse']:checked").val() + d_f;        
        }
    }else{
        spec.name+="resultDataStatistic"+d_f;
        spec.value += "1" + d_f; 
        if($("input[name='includeNoDataUser']:checked").val()==1){
                 spec.name+="includeNoDataUser"+d_f;
                 spec.value += $("input[name='includeNoDataUser']:checked").val() + d_f;        
        }
    }

    
    /*同时导出明细记录*/
    if($("input[name='isExportDetail']:checked").val() == 'true'){
    	spec.name+="isExportDetail"+ d_f;
        spec.value+="true"+d_f;
    }else{
    	spec.name+="isExportDetail"+ d_f;
        spec.value+="false"+ d_f;
    }
    
    /*用户信息*/
    //if(frm.isExportDetail.value == 'true'){
    	var temp_lst = "";   	
    	if(frm.userInfo){
    		var userInfo =eval('frm.userInfo')
    		}else{
    			var userInfo = frm.userInfo
    		}
    	if(frm.userInfo){
    		if(frm.userInfo){
        		if(userInfo.length > 1){
	        		temp_lst += frm.pageUserInfo.value + d_v;	
        		}else{
        			if(userInfo.checked){
        				temp_lst += frm.pageUserInfo.value + d_v;
        			}
        		}
        	}	
        	temp_lst = temp_lst.substr(0,temp_lst.length-1)	
        	spec.name += "userInfo" + d_f;
    	}   	
    	spec.value += temp_lst + d_f;			
   // }
    /*课程信息*/
   // if(frm.isExportDetail.value == 'true'){
    	var temp_lst = "";    	
    	if(frm.courseInfo){
    		var courseInfo =eval('frm.courseInfo')
    		}else{
    			var courseInfo = frm.courseInfo
    		}
    	if(frm.courseInfo){
    		if(frm.courseInfo){
        		if(courseInfo.length > 1){
	        		temp_lst += frm.pageCourseInfo.value + d_v;	
        		}else{
        			if(courseInfo.checked){
        				temp_lst += frm.pageCourseInfo.value + d_v;
        			}
        		}
        	}	
        	temp_lst = temp_lst.substr(0,temp_lst.length-1)	
        	spec.name += "courseInfo" + d_f;
    	}   	
    	spec.value += temp_lst + d_f;			
   // }
    /*其他信息*/
    //if(frm.isExportDetail.value == 'true'){
    	var temp_lst = "";  	
    	if(frm.otherInfo){
    		var otherInfo =eval('frm.otherInfo')
    		}else{
    			var otherInfo = frm.otherInfo
    		}
    	if(frm.otherInfo){
    		if(frm.otherInfo){
        		if(otherInfo.length > 1){
	        		temp_lst += frm.pageOtherInfo.value + d_v;
        		}else{
        			if(otherInfo.checked){
        				temp_lst += frm.pageOtherInfo.value + d_v;
        			}
        		}
        	}	
        	temp_lst = temp_lst.substr(0,temp_lst.length-1)	
        	spec.name += "otherInfo" + d_f;
    	}   	
    	spec.value += temp_lst + d_f;			
   // }
    
    
}