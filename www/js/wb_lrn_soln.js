// ------------------ wizBank Course object ----------------------------------------
// Convention:
//   public functions : use "wbLrnSoln" prefix 
//   private functions: use "_wbLrnSoln" prefix
// Dependency:
//   wb_utils.js
// ---------------------------------------------------------------------------------

// ------------- Constructor -------------
function wbLearnSolution(){
	// Learning Solution
	this.view_lrn_plan_run_more = wbLrnSolnViewLearnPlanRunMore;
	this.view_lrn_plan_more = wbLrnSolnViewLearnPlanMore;
	this.view_cos = wbLrnSolnViewCourse;// from learner view
	this.ist_view_cos_popup = wbLrnSolnIstViewCoursePopUp;//from manager view
	this.plan_cos_prep = wbLrnSolnPlanCoursePrep;
	this.plan_cos_exec = wbLrnSolnPlanCourseExec;
	this.del_cos = wbLrnSolnDeleteCourse;
	//this.cancel = wbLrnSolnCancel;
	//Learning History Report
	this.get_itm = wbLrnSolnGetItem;
	this.view_lrn_history = wbLrnSolnViewLrnHistory;
	this.srh_lrn_history_exec = wbLrnSolnSearchLrnHistoryExec;
	//Learner Item Details add to My Learning Solution
	this.add_lrn_soln = wbLrnSolnAddLrnSoln
	
	//learning history review
	this.lrn_tracking_rpt = wbLrnTrackingReport

	
	//Staff Learning Plan
	this.srh_criteria_prep = wbLrnSolnSearchCriteriaPrep;
	this.srh_criteria_exec = wbLrnSolnSearchCriteriaExec;
	this.srh_criteria_exec_hp = wbLrnSolnSearchCriteriaExecHP;
	this.srh_recommend_cos_prep = wbLrnSolnSearchRecommendCoursePrep;
	this.srh_recommend_cos_exec = wbLrnSolnSearchRecommendCourseExec;
	this.add_lrn_soln_exec = wbLrnSolnAddLrnSolnExec;
	this.refresh_lrn_history = wbLrnRefreshHistory;
}

// ------------- Public Functions -------------
// -- Learning Solution -- //
function wbLrnRefreshHistory(frm, lang){
	url = window.location.href;
	var replaceName = 'itm_content_lst';
	var replaceValue = 'catalog';

	if((frm.att_create_start_datetime_yy.value!=""&&frm.att_create_start_datetime_yy.value!=null)||
	   (frm.att_create_start_datetime_mm.value!=""&&frm.att_create_start_datetime_mm.value!=null)||
	   (frm.att_create_start_datetime_dd.value!=""&&frm.att_create_start_datetime_dd.value!=null)){
		if(!gen_validate_date("document." + frm.name + ".att_create_start_datetime", "Enrollment Period", lang)){
			return;
		}
		frm.att_create_start_datetime.value = frm.att_create_start_datetime_yy.value + "-" + frm.att_create_start_datetime_mm.value + "-" + frm.att_create_start_datetime_dd.value + " 00:00:00.00";
	}
	
	if((frm.att_create_end_datetime_yy.value!=""&&frm.att_create_end_datetime_yy.value!=null)||
	   (frm.att_create_end_datetime_mm.value!=""&&frm.att_create_end_datetime_mm.value!=null)||
	   (frm.att_create_end_datetime_dd.value!=""&&frm.att_create_end_datetime_dd.value!=null)){
		if(!gen_validate_date("document." + frm.name + ".att_create_end_datetime", "Enrollment period", lang)){
			return;
		}
		frm.att_create_end_datetime.value = frm.att_create_end_datetime_yy.value + "-" + frm.att_create_end_datetime_mm.value + "-" + frm.att_create_end_datetime_dd.value + " 23:59:59.00";
	}
	
	if( frm.att_create_start_datetime.value != '' ) {
		replaceName += ':_:_:att_create_start_datetime';
		replaceValue += ':_:_:' + frm.att_create_start_datetime.value;
	}
	url = setUrlParam('att_create_start_datetime', frm.att_create_start_datetime.value, url);
	
		
	if( frm.att_create_end_datetime.value != '' ) {
		replaceName += ':_:_:att_create_end_datetime';
		replaceValue += ':_:_:' + frm.att_create_end_datetime.value;
	}
	url = setUrlParam('att_create_end_datetime', frm.att_create_end_datetime.value, url);
	
	
	url = setUrlParam('spec_name', replaceName, url);
	url = setUrlParam('spec_value', replaceValue, url);
	window.location.href = url;
	return;
}

function wbLrnSolnViewLearnPlanRunMore(type,pgm_id,pgm_run_id){
	url = wb_utils_invoke_ae_servlet("cmd","ae_lrn_plan","item_type",type,"pgm_id",pgm_id,"pgm_run_id",pgm_run_id,"stylesheet",'lrn_plan_run_more.xsl');
	parent.location.href = url;
}

function wbLrnSolnViewLearnPlanMore(usr_ent_id,item_type){
	if (item_type == null) {item_type = '';}
	url = wb_utils_invoke_ae_servlet("cmd","ae_lrn_plan","usr_ent_id",usr_ent_id,"item_type",item_type,"stylesheet",'lrn_plan_more.xsl','targeted_item_apply_method_lst','');
	parent.location.href = url;
}

function wbLrnSolnViewCourse(usr_ent_id){
	url = wb_utils_invoke_ae_servlet("cmd","ae_lrn_soln","usr_ent_id",usr_ent_id,"item_type", "", "stylesheet",'lrn_soln_view_cos.xsl','targeted_item_apply_method_lst','','url_failure','Javascript:history.back()',"all_ind","true");
	wb_utils_set_cookie("lrn_plan_usr_id",usr_ent_id);
	parent.location.href = url;
}

function wbLrnSolnIstViewCoursePopUp(usr_ent_id,viewer_ent_id,viewer_role){
	url = wb_utils_invoke_ae_servlet("cmd","ae_lrn_soln","usr_ent_id",usr_ent_id,"item_type", "", "stylesheet",'lrn_soln_ist_view_cos_popup.xsl','targeted_item_apply_method_lst','','url_failure','Javascript:history.back()',"all_ind","true",'viewer_ent_id', viewer_ent_id, 'viewer_role', viewer_role);
	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '500'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'

	wbUtilsOpenWin(url, 'plan', false, str_feature);
}

function wbLrnSolnPlanCoursePrep(usr_ent_id,soln_type){
	url = wb_utils_invoke_ae_servlet("cmd","ae_lrn_soln","usr_ent_id",usr_ent_id,"soln_type",soln_type,"item_type", "CLASSROOM~SELFSTUDY~VIDEO", "stylesheet",'lrn_soln_plan_cos.xsl');
	parent.location.href= url;
}

function wbLrnSolnPlanCourseExec(frm,usr_ent_id,count){
	item_lst = "";
	period_lst = "";
	for (i=1;i<=count;i++){
		item_lst += eval("frm.itm_id_"+i+".value") + "~";
		if (eval("frm.itm_"+i+".type")!="hidden"){
			for(j=0;j<eval("frm.itm_"+i+".length");j++){
				if(eval("frm.itm_"+i+"["+j+"].checked")){
					period_lst += eval("frm.itm_"+i+"["+j+"].value") + "~";
				}
			}
		}else{
			period_lst += eval("frm.itm_"+i+".value") + "~";
		}
	}
	frm.cmd.value = "ae_upd_lrn_soln";
	frm.usr_ent_id.value = usr_ent_id;
	frm.item_lst.value = item_lst.substr(0,item_lst.length-1);
	frm.period_lst.value = period_lst.substr(0,period_lst.length-1);
	frm.url_failure.value = parent.location.href;
	frm.url_success.value = wb_utils_invoke_ae_servlet("cmd","ae_lrn_soln","usr_ent_id",usr_ent_id,"item_type", "", "stylesheet",'lrn_soln_view_cos.xsl','targeted_item_apply_method_lst','',"all_ind","true");
	frm.method = "post";
	frm.action = wb_utils_ae_servlet_url;
	frm.submit();
}

function wbLrnSolnDeleteCourse(usr_ent_id,item_id){
	url = wb_utils_invoke_ae_servlet("cmd","ae_upd_lrn_soln","usr_ent_id",usr_ent_id,"item_lst",item_id,"period_lst","-1","url_success",parent.location.href);
	parent.location.href = url;
}

// -- Learning History Report -- //
function wbLrnSolnGetItem(itm_id){
	var url_failure = self.location.href;
	url = wb_utils_invoke_ae_servlet("cmd","ae_get_itm","itm_id",itm_id,"tvw_id","LRN_VIEW","stylesheet",'itm_lrn_details.xsl',"prev_version","false","show_run_ind","true","show_session_ind","true", 'url_failure', url_failure);
	parent.location.href = url;
}

function wbLrnSolnViewLrnHistory(usr_ent_id,year){
	url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt","usr_ent_id",usr_ent_id,"calendar_view","1","calendar_year",year,"rpt_type","learner","stylesheet",'rpt_lrn_res.xsl',"p","1");
	parent.location.href = url;
}

function wbLrnSolnSearchLrnHistoryExec(frm,lang){
	if(!gen_validate_empty_field(frm.search,eval('wb_msg_'+lang+'_search_field'),lang)){
		frm.search.focus()
		return;
	}
	frm.cmd.value = "get_rpt";
	frm.module.value = "report.ReportModule";
	frm.download.value = "0";
	frm.spec_name.value = "itm_title:_:_:itm_title_partial_ind:_:_:ent_id";
	frm.spec_value.value = frm.search.value + ":_:_:" + "1" + ":_:_:" +frm.usr_ent_id.value;
	frm.stylesheet.value = "'rpt_lrn_res.xsl'";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

//Learner Item Details add learing solution
function wbLrnSolnAddLrnSoln(usr_ent_id,itm_id,period_id){
	if(period_id == null){
		period_id = ''
	}
	if(usr_ent_id == ""){
		usr_ent_id = wb_utils_get_cookie("lrn_plan_usr_id");
	}
	url_success = _wbLrnSolnGetLrnSolnURL(usr_ent_id)
	url_failure = self.location.href
	url = wb_utils_invoke_ae_servlet('cmd','ae_ins_lrn_soln','usr_ent_id',usr_ent_id,'itm_id',itm_id,'period_id',period_id,'url_success',url_success,'url_failure',url_failure)
	window.location.href = url
}

function wbLrnTrackingReport(usr_ent_id,course_id,isPopup, tkh_id){	
	if (isPopup == null || isPopup == '') {isPopup = false;}
	str_feature = 'toolbar='		+ 'yes'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '400'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
	url = wbLrnTrackingReportUrl(usr_ent_id,course_id, tkh_id)
	
	if (isPopup == true){
		popup_lrn_trancking_rpt = wbUtilsOpenWin(url,'lrn_trancking_rpt' + usr_ent_id + '_' + tkh_id, false, str_feature);	
	}else{
		window.location.href = url;
	}
}

function wbLrnTrackingReportUrl(usr_ent_id,course_id, tkh_id){
	url = wb_utils_invoke_servlet('cmd','get_lrn_mod_rpt','stylesheet','lrn_rpt_mod.xsl','course_id',course_id,'student_id',usr_ent_id, 'tkh_id', tkh_id)
	return url;	
}

function wbLrnSolnSearchCriteriaPrep(){
	url = wb_utils_invoke_servlet('cmd','get_ent_lst','stylesheet','staff_lrn_plan_srh.xsl')
	window.location.href = url;
}

function wbLrnSolnSearchCriteriaExec(frm,lang){
	if (_wbLrnSolnValidateSearchFrm(frm,lang)) {
		
		if (frm.ent_id && frm.s_group_lst) {
			if (frm.s_group_lst.options[0]) {frm.ent_id.value = frm.s_group_lst.options[0].value;}
		}
		if (frm.s_grade && frm.s_grade_lst.options[0]) {frm.s_grade.value = frm.s_grade_lst.options[0].value;}
		
		frm.action = wb_utils_servlet_url	
		frm.stylesheet.value = 'staff_lrn_plan_srh_res.xsl'
		frm.cmd.value = 'search_ent_lst'
		frm.s_usg_ent_id_lst.value = "my_approval";
		frm.frmHP.value = "false";
		frm.method = "get"
		frm.submit()
	}
}

function wbLrnSolnSearchCriteriaExecHP(ent_id,frm,lang){
		var s_usr_display_bil = "";
		if(frm != null){
			if(!gen_validate_empty_field(frm.s_usr_display_bil,eval('wb_msg_'+lang+'_search_field'),lang)){
				frm.s_usr_display_bil.focus();
				return;
			}
			s_usr_display_bil = frm.s_usr_display_bil.value;
		}
		var url = wb_utils_invoke_servlet("cmd","search_ent_lst","ent_id",ent_id,"s_role_types","","stylesheet","staff_lrn_plan_srh_res.xsl","s_usr_id","","s_usr_first_name_bil","","s_usr_last_name_bil","","s_usr_display_bil",s_usr_display_bil,"s_grade_lst_single","","s_grade","","s_usg_ent_id_lst","my_approval");
		if(frm != null){
			url += "&frmHP=false";
		}
		parent.location.href = url;
}
function wbLrnSolnSearchRecommendCoursePrep(frm,lang){

	if(frm !=null && lang!=null && !_wbLrnSolnValidateSearchFrm(frm,lang))
		return;
	var url = wb_utils_invoke_ae_servlet('cmd','get_itm_ref_data','stylesheet','staff_lrn_plan_srh_cos.xsl');
	var str_feature = ',width=' 				+ '760'
							+ ',height=' 				+ '500'
							+ ',scrollbars='			+ 'yes'
							+ ',screenX='				+ '10'
							+ ',screenY='				+ '10'
							+ ',status='					+ 'yes'
	wbUtilsOpenWin(url,"srhWin",false,str_feature);
}

function wbLrnSolnSearchRecommendCourseExec(frm,lang,doValidate){
	if(((doValidate==null)||(doValidate!=null&&doValidate))&&!_wbLrnSolnValidateSearchFrm(frm,lang))
		return;
	if(frm.match && frm.match.checked)
		frm.exact.value = "true";
	else
		frm.exact.value = "false";
	frm.cmd.value = "ae_lookup_itm";
	frm.tvw_id.value = "LIST_VIEW";
	frm.tnd_id_lst.value = frm.catalog.options[frm.catalog.selectedIndex].value;
	if(frm.tnd_id_lst.value=="")
		frm.all_ind.value = "true";
	if(frm.orderby)
		frm.orderby.value = "itm_title";
	frm.type.value = frm.type.options[frm.type.selectedIndex].value;
	frm.stylesheet.value ="staff_lrn_plan_srh_cos_res.xsl";
	frm.url_failure.value = window.location.href;
	frm.method = "get";
	frm.action = wb_utils_ae_servlet_url;
	frm.submit();
}

function wbLrnSolnAddLrnSolnExec(frm,lang){
	if(!_wbLrnSolnValidateSearchFrm(frm,lang))
		return;
	var str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '400'
			+ ',height=' 				+ '180'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'yes';
	wb_utils_set_cookie("appn_usr_name","");
	wb_utils_set_cookie("current","");
	wb_utils_set_cookie("total","");
	var itm_id_lst = "";
	var itm_id = frm.itm_id_lst.value.split("~");
	if(opener.frmXml.usr_ent_id){
		var usr_id_lst = _wbLrnGetLst(opener.frmXml.usr_ent_id,true);
		var usr_id = usr_id_lst.split("~");
	}else{
		var usr_id = new Array();
		usr_id[0] = opener.frmXml.usr_ent_id_lst.value;
	}
	usr_id_lst = "";
	if(opener.frmXml.usr_ent_id){
		var list = "";
		if(opener.frmXml.usr_ent_id.length){
			for(var i=0;i<opener.frmXml.usr_ent_id.length;i++){
				if(opener.frmXml.usr_ent_id[i].checked){
					list += opener.frmXml.usr_ent_nm[i].value + "~";
				}
			}
		}else{
			if(opener.frmXml.usr_ent_id.checked){
				list = opener.frmXml.usr_ent_nm.value;
			}
		}
		if(list.indexOf("~")>-1){
			list = list.substr(0,list.length-1);
		}
		var usr_nm = list.split("~");
	}else{
		var usr_nm = new Array();
		usr_nm[0] = opener.frmXml.usr_ent_nm_lst.value;
	}
	var usr_nm_lst = "";
	var period_lst = "";
	for(var i=0; i<usr_id.length; i++){
		for(var j=0; j<itm_id.length; j++){
			itm_id_lst += itm_id[j] + "~";
			usr_id_lst += usr_id[i] + "~";
			usr_nm_lst += usr_nm[i] + "~";
			period_lst += "0" + "~";
		}
	}
	if(usr_id_lst.indexOf("~")>-1){
		usr_id_lst = usr_id_lst.substr(0,usr_id_lst.length-1);
		usr_nm_lst = usr_nm_lst.substr(0,usr_nm_lst.length-1);
	}
	if(itm_id_lst.indexOf("~")>-1)
		itm_id_lst = itm_id_lst.substr(0,itm_id_lst.length-1);
	if(period_lst.indexOf("~")>-1)
		period_lst = period_lst.substr(0,period_lst.length-1);
	frm.usr_id_lst.value = usr_id_lst;
	frm.usr_nm_lst.value = usr_nm_lst;				
	frm.itm_id_lst.value = itm_id_lst;				
	frm.period_lst.value = period_lst;				
	var url = "../htm/application_frame_window.htm?lang="+lang+"&functionName=doFeedParam";
	wbUtilsOpenWin(url,'addLrnSolnWin', false, str_feature);
}

// ------------- Private Functions -------------
function _wbLrnSolnValidateSearchFrm(frm,lang){
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
		frm.s_status.value = _wbLrnSolnGetUserSStatusLst(frm)
		if (frm.s_status.value == ""){
			alert(eval('wb_msg_'  + lang + '_sel_usr_s_status'))	
			return false;
		}
	}
	if(frm.usr_ent_id_lst){
		if(frm.usr_ent_id) {
			frm.usr_ent_id_lst.value = _wbLrnGetLst(frm.usr_ent_id,true);
			if(frm.usr_ent_id_lst.value == ""){
				alert(eval("wb_msg_"+lang+"_sel_staff"));
				return false;
			}
		} else {
			return false;
		}
	} 
	if(frm.itm_id){
		frm.itm_id_lst.value = _wbLrnGetLst(frm.itm_id,true);
		if(frm.itm_id_lst.value == ""){
			alert(eval("wb_msg_"+lang+"_select_course"));
			return false;
		}
	}
	return true;
	
}

function _wbLrnGetLst(ele,checked){
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

function _wbLrnSolnGetLrnSolnURL(usr_ent_id){
	url = wb_utils_invoke_ae_servlet('cmd','ae_lrn_soln','usr_ent_id',usr_ent_id,'item_type','','stylesheet','lrn_soln_view_cos.xsl','targeted_item_apply_method_lst','',"all_ind","true")
	return url
}

function _wbLrnSolnGetUserSStatusLst(frm){
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