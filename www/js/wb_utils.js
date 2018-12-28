// ------------------ wizBank Utils object ----------------------------------------
// Convention:
//   public functions : use "wb_utils" prefix
//   private functions: use "_wb_utils" prefix
// ---------------------------------------------------------------------------------
// Index:
// A. Servlet
// B. JSP
// C. Invoke Servlet Functions
// D. Navigation Functions
// E. Generic Functions
// F. Classified Functions
// G.For loading applet
// H. Validate Functions with new Label System

// --------------------- A. Servlet ------------------------------------------------
wb_utils_app_base = '/';
wb_utils_controller_base = wb_utils_app_base + 'app/';

wb_utils_servlet_package_qdbaction = 'qdbAction';
wb_utils_servlet_package_aeaction = 'aeAction';
wb_utils_servlet_package_dispatcher = 'Dispatcher';
wb_utils_servlet_package_cmi = 'CMI';
wb_utils_servlet_package_sco2004cmi = 'SCO2004CMI'
wb_utils_servlet_url = wb_utils_app_base + 'servlet/' + wb_utils_servlet_package_qdbaction;
wb_utils_ae_servlet_url = wb_utils_app_base + 'servlet/' + wb_utils_servlet_package_aeaction;
wb_utils_disp_servlet_url = wb_utils_app_base + 'servlet/' + wb_utils_servlet_package_dispatcher;

wb_utils_jsp_root_url = wb_utils_app_base + 'jsp/';

//Man:Limit for Common TextField
wb_utils_text_limit = 10000;
wb_image_path = wb_utils_app_base + 'wb_image/';

// constants for test player frameset
wb_utils_testplayer_fs_row = '60,*,0';
wb_utils_testplayer_fs_row_evn = '100,0';
wb_utils_testplayer_fs_row_progress_height = '100';
wb_utils_testplayer_fs_row_progress_subfix = ',*,0,0';
wb_utils_testplayer_fs_row_progress = wb_utils_testplayer_fs_row_progress_height + wb_utils_testplayer_fs_row_progress_subfix;

// ---------------------- B.JSP --------------------------------------------------
wb_utils_jsp_home = 'home.jsp';
var browserIE = (navigator.appName.indexOf("Microsoft Internet Explorer") == 0);

// --------------------- C. Invoke Servlet Functions
// -------------------------------
function wb_utils_invoke_servlet() {
	var path = wb_utils_servlet_url + '?';
	path += 'env=wizb';
	for ( var i = 0; i < arguments.length; i = i + 2) {
		if (arguments[i] == 'stylesheet') {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + arguments[i + 1];
		} else {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + encodeURIComponent(arguments[i + 1]);
		}
	}
	return path;
}

function wb_utils_invoke_disp_servlet() {
	var path = wb_utils_disp_servlet_url + '?';
	path += 'env=wizb';
	for ( var i = 0; i < arguments.length; i = i + 2) {
		if (arguments[i] == 'stylesheet') {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + arguments[i + 1];
		} else {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + encodeURIComponent(arguments[i + 1]);
		}
	}
	return path;
}

function wb_utils_invoke_ae_servlet() {
	var path = wb_utils_ae_servlet_url + '?';
	path += 'env=wizb';
	for ( var i = 0; i < arguments.length; i = i + 2) {
		if (arguments[i] == 'stylesheet') {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + arguments[i + 1];
		} else {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + encodeURIComponent(arguments[i + 1]);
		}
	}
	return path;
}

function wb_utils_invoke_controller(action) {
	var path = wb_utils_controller_base + action + '?';
	for ( var i = 1; i < arguments.length; i = i + 2) {
		if (arguments[i] == 'stylesheet') {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + arguments[i + 1];
		} else {
			path += '&' + encodeURIComponent(arguments[i]) + '=' + encodeURIComponent(arguments[i + 1]);
		}
	}
	return path;
}

// --------------------- D. Navigation Functions -----------------------------------
// *** Get previous token of navigation ***
function wb_utils_nav_get_urlparam(){
	var url = self.location.href;

	for(var i = 0; i < arguments.length; i = i + 2){
		if(arguments[i] == 'app_lst_page' && arguments[i + 1] != null) {
			wb_utils_set_cookie("page",arguments[i + 1]);
		}
		if(arguments[i] == 'app_lst_page_size' && arguments[i + 1] != null) {
			wb_utils_set_cookie("page_size",arguments[i + 1]);
		}
		if(document.all || document.getElementById != null || arguments[i] != 'timestamp'){
			if(getUrl(arguments[i])){
				url = setUrlParam(arguments[i], arguments[i + 1], url);
			}else{
				url = url + '&' + arguments[i] + '=' + arguments[i + 1];
			}
		}
	}
	self.location.href = url;
}

function wb_utils_escape_url_anchor(url){
	url = url.substring(0, url.indexOf("#"));
	return url;
}
// --------------------- E. Generic Functions --------------------------------------
function wb_utils_preloading(showed_msg, showed_img){
	var msg, img;
	if(showed_msg && showed_msg != '') {
		msg = showed_msg;
	} else {
		msg = wb_msg_processing_msg;
	}
	if(showed_img && showed_img != '') {
		img = showed_img;
	} else {
		img = 'time_counter.gif';
	}

	newResize="_wbUtilsRedrawPrompt();";
	newScroll="_wbUtilsRedrawPrompt();";

	//if have old onresize, add newResize after it, else set onresize=newResize
	if(window.onresize)
	{
		var _oldResize_str = window.onresize.toString();
		if(_oldResize_str.indexOf('_wbUtilsRedrawPrompt') < 0){
			//get the content of (function)window.onresize
			_oldResize_str = _oldResize_str.substring(_oldResize_str.indexOf('{')+1, _oldResize_str.lastIndexOf('}'));
			window.onresize=new Function(_oldResize_str + ";" + newResize);
		}

	}
	else {
		window.onresize=_wbUtilsRedrawPrompt;
	}

	//if have old onscroll, add newScroll after it, else set onscroll=newScroll
	if(window.onscroll){
		var _oldScroll_str = window.onscroll.toString();
		if(_oldScroll_str.indexOf('_wbUtilsRedrawPrompt') < 0){
			//get the content of (function)window.onscroll
			_oldScroll_str = _oldScroll_str.substring(_oldScroll_str.indexOf('{')+1, _oldScroll_str.lastIndexOf('}'));
			window.onscroll=new Function(_oldScroll_str + ";" + newScroll);
		}
	}
	else {
		window.onscroll=_wbUtilsRedrawPrompt;
	}

	//get the max zIndex of all div
	var z_index = 0;
	for(var i = 0; i < document.getElementsByTagName("div").length; i++){
        if(z_index < document.getElementsByTagName("div")[i].style.zIndex){
            z_index = parseInt(document.getElementsByTagName("div")[i].style.zIndex);
        }
    }

	var wbUtilsMaskDiv=document.createElement("div");

	wbUtilsMaskDiv.id = "wbUtilsMaskDiv";
	wbUtilsMaskDiv.style.visibility = "visible";
	wbUtilsMaskDiv.style.zIndex = z_index + 1;
	wbUtilsMaskDiv.style.position = "absolute";
	wbUtilsMaskDiv.style.background = "#808080";
	wbUtilsMaskDiv.style.filter = "alpha(opacity=50)";	// IE only
	wbUtilsMaskDiv.style.opacity = "0.5";				// FireFox only

	document.body.appendChild(wbUtilsMaskDiv);

	var wbUtilsPromptDiv=document.createElement("div");

	wbUtilsPromptDiv.id = "wbUtilsPromptDiv";
	wbUtilsPromptDiv.style.visibility = "visible";
	wbUtilsPromptDiv.style.zIndex = z_index + 2;
	wbUtilsPromptDiv.style.position = "absolute";
	wbUtilsPromptDiv.style.width = 200;
	wbUtilsPromptDiv.style.height = 100;
	wbUtilsPromptDiv.innerHTML = '<table width="100%" height="100%"><tr><td align="center" valign="middle"><table cellpadding="10" cellspacing="0" style="border:2px solid #000000;"><tr><td bgcolor="white" nowrap="nowrap" id="mask_msg">' + msg + '<img src="' + wb_image_path + img + '" border="0"></td></tr></table></td></tr></table>';

	document.body.appendChild(wbUtilsPromptDiv);

	_wbUtilsRedrawPrompt();
}
//set the position of the div
function _wbUtilsRedrawPrompt() {

	var wbUtilsMaskDiv = document.getElementById("wbUtilsMaskDiv");
	var wbUtilsPromptDiv = document.getElementById("wbUtilsPromptDiv");

	wbUtilsMaskDiv.style.width = document.body.clientWidth;
	wbUtilsMaskDiv.style.height = document.body.clientHeight;
	wbUtilsMaskDiv.style.left = document.body.scrollLeft;
	wbUtilsMaskDiv.style.top = document.body.scrollTop;
	wbUtilsPromptDiv.style.left = (document.body.clientWidth - parseInt(wbUtilsPromptDiv.style.width)) / 2 + document.body.scrollLeft;
	wbUtilsPromptDiv.style.top = (document.body.clientHeight - parseInt(wbUtilsPromptDiv.style.height)) / 2 + document.body.scrollTop;
}

function wb_utils_close_preloading(){
	if(parent.tmpWin && !parent.tmpWin.closed){
		parent.tmpWin.close();
	}
}

function wb_utils_set_cookie(token_nm, token_val){
	gen_set_cookie_token('wb', token_nm, token_val, '');
}

function wb_utils_get_cookie(token_nm){
	return gen_get_cookie_token('wb', token_nm);
}

function wb_utils_logout(lang){
	if(confirm(eval('wb_msg_' + lang + '_confirm'))){
		url = wb_utils_invoke_servlet('cmd', 'logout')
		self.location.href = url;
	}
}

function wb_utils_cancelChangePwd(){
		url = wb_utils_invoke_servlet('cmd', 'logout')
		self.location.href = url;
}


function wb_utils_nav_go(nav_id,   ent_id, lang, curTime, rol_ext_id){
	var url = null;

	if(lang == null || lang == ''){
		lang = 'en';
	}
	switch(nav_id){

		case "USR_OWN_MAIN" :
			url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', 'my_profile.xsl');
			break;

		case "USR_PWD_UPD" :
			url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', 'usr_pwd_upd.xsl');
			break;

		case "CAT_LIST" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_cat_lst', 'stylesheet', 'catalog_lst.xsl');
			break;

		case "FTN_AMD_CAT_MAIN" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_cat_lst', 'stylesheet', 'catalog_lst.xsl','cat_tcr_id','0');
			break;

		case "STAFF_SOLN_LINK" :
			url = wb_utils_invoke_servlet("cmd", "search_ent_lst", "ent_id", "", "s_role_types", "", "stylesheet", "staff_lrn_plan_srh_res.xsl", "s_usr_id", "", "s_usr_first_name_bil", "", "s_usr_last_name_bil", "", "s_usr_display_bil", "", "s_grade_lst_single", "", "s_grade", "", "s_usg_ent_id_lst", "my_approval");
			break;

		case "SSO_LINK_QUERY" :
			url = wb_utils_invoke_servlet("cmd", "get_sso_link_query", "stylesheet", "sso_link_query.xsl");
			break;

		case "ITM_INTEGRATED_MAIN" :
			var trainType = 'INTEGRATED';
			var orderby=getOrderCol(trainType);
			var sort=getOrderSort(trainType);
			url = wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','stylesheet','itm_search_result.xsl','all_ind','true','training_type',trainType,'orderby',orderby,'sortorder',sort,'tcr_id_lst',-1);
			break;

/*		case "FTN_AMD_ITM_COS_MAIN" :
			var orderby=getOrderCol('COS');
			var sort=getOrderSort('COS');
			url = wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','stylesheet','itm_search_result.xsl','all_ind','true','training_type','COS','orderby',orderby,'sortorder',sort,'tcr_id_lst',-1);
			break;*/

		case "ITM_EXAM_MAIN" :
			var orderby=getOrderCol('EXAM');
			var sort=getOrderSort('EXAM');
			url = wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','stylesheet','itm_search_result.xsl','all_ind','true','training_type','EXAM','orderby',orderby,'sortorder',sort,'tcr_id_lst',-1);
			break;

		case "ITM_REF_MAIN" :
			var orderby=getOrderCol('REF');
			var sort=getOrderSort('REF');
			url = wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','stylesheet','itm_search_result.xsl','all_ind','true','training_type','REF','orderby',orderby,'sortorder',sort,'tcr_id_lst',-1);
			break;

		case "RUN_MAIN" :
			url = wb_utils_invoke_ae_servlet('cmd','get_run_ref_data','stylesheet','run_search_main.xsl');
			break;

		case "ITM_NEW" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_all_ity_form', 'stylesheet', 'ls_add_form_prep.xsl');
			break;

		case "FTN_AMD_RES_MAIN" :
			url = wb_utils_adm_syb_lst_url('','','true')
			break;

		case "LRN_RES_ADMIN" :
			url = wb_utils_adm_syb_lst_url()
			break;

		case "ENR_APP_LINK" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_workflow_lst', 'url_failure', wb_utils_gen_home_url(), 'stylesheet', 'application_workflow_lst.xsl');
			break;

		case "ENR_APP_COS_LINK" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_approver_appn_cos_lst', 'cur_page', '1', 'page_size', '15', 'sort_col', 'parent_itm_title', 'sort_order', 'ASC', 'url_failure', '', 'stylesheet', 'appr_cos_lst.xsl');
			break;

		case "FTN_AMD_TRAINING_REPORT_MGT" :
			url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'get_rpt_lst', 'stylesheet', 'rpt_all.xsl', 'show_public', 'y');
			break;
			
		case "FTN_AMD_LEARNNING" :
			url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'echo_spec_param', 'usr_ent_id', ent_id, 'rpt_type', 'LEARNER', 'rte_id', '2', 'stylesheet', 'rpt_lrn_srh_popup.xsl', 'spec_name', 'usr_content_lst', 'spec_value', 'usr_content_lst');
			break;

		case "GLB_RPT_LINK" :
			url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'get_rpt_lst', 'stylesheet', 'rpt_glb_all.xsl', 'show_public', 'Y');
			break;

		case "LRN_HIST_LINK" :
			url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'lrn_soln_hist', 'usr_ent_id', ent_id,  'stylesheet', 'lrn_history.xsl', 'p', '1', 'sort_col', 'itm_code', 'sort_order', 'asc', 'spec_name', 'itm_content_lst', 'spec_value', 'catalog','itm_p_node','true');
			break;

		case "LRN_SOLN_LINK" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_lrn_soln', 'usr_ent_id', ent_id, 'item_type', '', 'stylesheet', 'lrn_soln_view_cos.xsl', 'targeted_item_apply_method_lst', '', 'all_ind', 'true','order_by','title','sort_by','ASC');
			break;

		case "FTN_AMD_FACILITY_BOOK_CREATE" ://FTN_AMD_FACILITY_BOOK_CREATE BOOK_MAIN
			url = wb_utils_invoke_disp_servlet('module', 'fm.FMModule', 'cmd', 'get_cart', 'stylesheet', 'fm_frame.xsl');

			var str_feature = 'toolbar=' + '1' + ',width=' + '780' + ',height=' + '500' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10' + ',status=' + 'yes';

			if(document.all){
				str_feature += ',top=' + '10';
				str_feature += ',left=' + '10';
			}

			wb_utils_fm_set_cookie('old_rsv_return_url', wb_utils_fm_get_cookie('rsv_return_url'));
			wb_utils_fm_set_cookie('old_rsv_itm_id', wb_utils_fm_get_cookie('rsv_itm_id'));
			wb_utils_fm_set_cookie('old_rsv_itm_title', wb_utils_fm_get_cookie('rsv_itm_title'));
			wb_utils_fm_set_cookie('old_work_rsv_itm_title', wb_utils_fm_get_cookie('work_rsv_itm_title'));
			wb_utils_fm_set_cookie('old_url_success', wb_utils_fm_get_cookie('url_success'));
			wb_utils_fm_set_cookie('old_cur_rsv_id', wb_utils_fm_get_cookie('cur_rsv_id'));
			wb_utils_fm_set_cookie('old_work_rsv_id', wb_utils_fm_get_cookie('work_rsv_id'));
			wb_utils_fm_set_cookie('old_cart', wb_utils_fm_get_cookie('cart'));

			wb_utils_fm_set_cookie('rsv_return_url', '');
			wb_utils_fm_set_cookie('rsv_itm_id', '');
			wb_utils_fm_set_cookie('rsv_itm_title', '');
			wb_utils_fm_set_cookie('work_rsv_itm_title', '');
			wb_utils_fm_set_cookie('url_success', '');
			wb_utils_fm_set_cookie('cur_rsv_id', '');
			wb_utils_fm_set_cookie('work_rsv_id', '');
			wb_utils_fm_set_cookie('cart', '');
			//fm_win = wbUtilsOpenWin(url, 'fm', false, str_feature);
			//url = null;
			break;
		case "BOOK_QUERY" :
			url = wb_utils_invoke_disp_servlet("module", "fm.FMModule", "cmd", "search_fac_fee_prep", "stylesheet", "fm_fee_search_prep.xsl","noheader","true");
			var str_feature = 'toolbar=' + '0' + ',width=' + '780' + ',height=' + '500' + ',scrollbars=' + 'no' + ',resizable=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10' + ',status=' + 'yes';
			if(document.all){
				str_feature += ',top=' + '10';
				str_feature += ',left=' + '10';
			}
			fm_win = wbUtilsOpenWin(url, 'fm', false, str_feature);
			url = null;
			break;
		case "CM_MAIN" :
			url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "get_comp_grp_list", "stylesheet", "cpty_skill_grp_lst.xsl");
			break;

		case "CM_ASS_MAIN" :
			url = wb_utils_invoke_disp_servlet("module", "competency.CompetencyModule", "cmd", "refresh_ass_list", "prepared", "true", "collected", "true", "notified", "true", "resolved", "true", "pagesize", "10", "cur_page", "1", "sort_by", "DESC", "order_by", "asm_eff_start_datetime", "stylesheet", 'comp_ass_get_list.xsl');
			break;

		case "CM_SKL_ANALYSIS" :
			wb_utils_set_cookie("sess_url", "");
			url = wb_utils_invoke_disp_servlet("module", "competency.SkillGapModule", "cmd", "view_skill_gap", "stylesheet", 'sk_ana_compare.xsl');
			break;

		case "ENR_ASS_LINK" :
			url = wb_utils_invoke_servlet("cmd", "search_ent_lst", "ent_id", '', "s_role_types", "", "stylesheet", 'enrol_assignment_lrn_lst.xsl', "s_usr_id", "", "s_usr_first_name_bil", "", "s_usr_last_name_bil", "", "s_usr_display_bil", "", "s_grade_lst_single", "", "s_grade", "", "s_usg_ent_id_lst", "my_approval");
			break;

		case "FTN_AMD_USR_INFO" :
			url = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'usr_manager.xsl', 'filter_user_group', 1);
			break;

		case "MOTE_MAIN" :
			url = wb_utils_invoke_disp_servlet('cmd', 'get_resp_mote_lst', 'module', 'mote.MoteModule', 'mote_status', 'progress', 'cur_page', '0', 'page_size', '10', 'sort_col', 'duedate', 'sort_order', 'ASC', 'timestamp', '', 'stylesheet', 'mote_lst.xsl');
			break;

		case "ACL_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'accesscontrol.AccessControlModule', 'cmd', 'get_rol_ftn', 'stylesheet', 'adm_acc_control.xsl');
			break;

		case "FOR_MAIN" :
			url = wb_utils_invoke_servlet("cmd", "get_public_forum", "ismaintain", 'true', "stylesheet", 'forum_maintain.xsl');
			break;

		case "FOR_LINK" :
			url = wb_utils_invoke_servlet("cmd", "get_public_forum", "ismaintain", 'true', "stylesheet", 'forum_maintain.xsl');
			break;

		case "FTN_AMD_SYS_MSG_LIST" :
			url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', 'announ_lst.xsl', 'msg_type', 'SYS', 'cur_page', '1', 'sortCol', 'msg_begin_date', 'sortOrder', 'DESC', 'timestamp', '', 'msg_readonly', 'true', 'msg_show_all', 'true');
			break;

		case "LRN_LIST" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_lrn_plan', 'usr_ent_id', ent_id, 'item_type', '', 'stylesheet', 'lrn_plan_more.xsl');
			break;

		case "SUPERVISE_MODULE" :
			url = wb_utils_invoke_disp_servlet('module', 'supervise.SuperviseModule', 'cmd', 'get_staff_count', 'stylesheet', 'my_staff_lst.xsl');
			break;

		case "USR_REG_APPROVAL_BOX" :
			url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 's_sort_by','usr_signup_date', 's_order_by','ASC', 's_usg_ent_id_lst', 'my_approval', 's_status', 'PENDING', 'stylesheet', 'usr_reg_approval_lst.xsl');
			break;

		case "FTN_AMD_EVN_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'content.EvaluationModule', 'cmd', 'get_public_eval_lst', 'stylesheet', 'evn_maintain_lst.xsl','tcr_id',0);
			break;

		case "EVN_LIST" :
			url = wb_utils_invoke_disp_servlet('module', 'content.EvaluationModule', 'cmd', 'get_public_eval_lst', 'filter', 'true', 'stylesheet', 'evn_lst.xsl');
			break;

		case "KM_LINK" :
			url = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'km_lib_cata_frame.xsl');
			break;

		case "KM_MGT" :
			url = wb_utils_invoke_servlet('cmd', 'home', 'stylesheet', 'km_lib_main.xsl');

			var str_feature = 'toolbar=' + '1' + ',width=' + '780' + ',height=' + '500' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10' + ',status=' + 'yes';

			if(document.all){
				str_feature += ',top=' + '10';
				str_feature += ',left=' + '10';
			}

			var km_win = wbUtilsOpenWin(url, 'km', false, str_feature);
			url = null;
			break;

		case "IMS_DATA_MGT" :
			url = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'bp_lst.xsl');
			break;

		case "USR_OWN_PREFER" :
			url = wb_utils_invoke_disp_servlet('cmd', 'get_my_preference', 'module', 'personalization.PsnPreferenceModule', 'stylesheet', 'psn_preference_upd.xsl');
			break;

		case "FTN_AMD_EVN_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'content.CosEvaluationModule', 'cmd', 'get_cos_eval_lst', 'stylesheet', 'cos_evn_form_maintain_lst.xsl');
			break;

		case "GLB_CAT_MAIN" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_glb_cat_lst', 'stylesheet', 'glb_catalog_lst.xsl');
			break;

		case "FTN_AMD_TEACHING_COURSE_LIST" :
			url = wb_utils_invoke_ae_servlet('cmd', 'my_responsible_itm', 'stylesheet', 'tch_cos_list.xsl');
			break;

		case "LRN_BLUEPRINT_VIEW" :
			url = wb_utils_invoke_disp_servlet('module', 'blp.BlueprintModule', 'cmd', 'open', 'stylesheet', '');
			//window.open(url,"","toolbar=no,scrollbars=auto,status=no,resizable=yes");
			//window.showModalDialog(url,"","dialogHeight:210px;dialogWidth:400px;help:no;center:yes;scroll:no;");
			wbUtilsOpenWin(url,null,false, "toolbar=no,scrollbars=yes,status=no,resizable=yes");
			url = null;
			break;

		case "LRN_BLUEPRINT_ADMIN" :
			url = wb_utils_invoke_disp_servlet('module', 'blp.BlueprintModule', 'cmd', 'view', 'stylesheet', 'lrn_blp_view.xsl');
			window.open(url,null,"toolbar=no,scrollbars=yes,status=no,resizable=yes");
			url = null;
			break;

		case "LRN_CALENDAR" :
			url = wb_utils_invoke_ae_servlet('cmd', 'get_itm_sch', 'stylesheet', 'ae_schedule.xsl');
			break;

		case "FTN_AMD_TC_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'trainingcenter.TrainingCenterModule', 'cmd', 'tc_details', 'stylesheet', 'tc_details.xsl', 'url_failure', wb_utils_gen_home_url());
			break;
		case "CODE_DATA_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'codetable.CodeDataModule', 'cmd', 'code_data_type_lst','stylesheet','code_data_type_lst.xsl');
			break;

		case "INSTR_MAIN" :
			url = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd', 'search_instr','stylesheet', 'instr_search_page.xsl');
			break;

		case "FTN_AMD_SYS_SETTING_MAIN" :
			url = wb_utils_invoke_servlet('cmd', 'get_sys_setting', 'stylesheet', 'threshold_sys_setting.xsl');
			break;

		case "FTN_AMD_MSG_MAIN" :
		    url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', 'announ_lst.xsl', 'msg_type', 'SYS', 'cur_page', 1,'sortCol', 'msg_begin_date', 'sortOrder', 'DESC', 'timestamp', '', 'msg_readonly', 'false', 'isMobile', 'false','msg_tcr_id','0')
			break;

		case "FTN_AMD_MSG_MAIN" :
		    url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', 'announ_lst.xsl', 'msg_type', 'SYS', 'cur_page', 1,'sortCol', 'msg_begin_date', 'sortOrder', 'DESC', 'timestamp', '', 'msg_readonly', 'false')
			break;

		case "KNOW_MAIN" :
		    url = wb_utils_invoke_disp_servlet('module', 'JsonMod.know.KnowModule', 'cmd', 'get_kca_lst', 'stylesheet', 'know_catalog_lst.xsl');
			break;

		case "MSG_MGT_SYS" :
		    url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', 'announ_lst.xsl', 'msg_type', 'SYS', 'cur_page', 1,'sortCol', 'msg_begin_date', 'sortOrder', 'DESC', 'timestamp', '', 'msg_readonly', 'false')
		    break;

		case "MOBILE_MSG_MGT_SYS" :
			url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', 'announ_lst.xsl', 'msg_type', 'SYS', 'cur_page', 1,'sortCol', 'msg_begin_date', 'sortOrder', 'DESC', 'timestamp', '', 'msg_readonly', 'false', 'isMobile', 'true')
			break;

		case "STUDY_GROUP_MAIN" :
		    url = wb_utils_invoke_disp_servlet('module', 'JsonMod.studyGroup.StudyGroupModule', 'cmd', 'get_sgp_lst', 'stylesheet', 'study_group_lst.xsl')
			break;

		case "APPR_APP_LIST" :
			url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_my_appn_approval_lst', 'cur_page', '1', 'aal_status', 'PENDING', 'stylesheet', 'enrollment_list.xsl')
			break;
		case "FTN_AMD_PLAN_CARRY_OUT" :
			/* 默认进入为当前年-月  
			 * gen_set_cookie("year", "");
			gen_set_cookie("month", "");
			gen_set_cookie("tpn_status", "");
			'tpn_date_month','0','tpn_date_year','0'   改为   'tpn_status_lst','APPROVED~IMPLEMENTED'
			*/ 
			url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan_lst', 'tpn_status', 'ALL','tpn_date_month','0','tpn_date_year','0','stylesheet', 'training_plan_lst.xsl');
			break;
		case "FTN_AMD_YEAR_PALN" :
			gen_set_cookie("year", "");
			gen_set_cookie("month", "");
			gen_set_cookie("tpn_status", "");
			url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_year_plan_lst', 'stylesheet', 'training_plan_year_lst.xsl');
			break;

		case "FTN_AMD_MAKEUP_PLAN":
			gen_set_cookie("year", "");
			gen_set_cookie("month", "");
			gen_set_cookie("tpn_status", "");
			url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_training_plan_lst', 'tpn_status', 'ALL', 'tpn_status_lst', 'PREPARED~PENDING~APPROVED~DECLINED~IMPLEMENTED', 'stylesheet', 'training_plan_adhoc_lst.xsl', 'tpn_type', 'MAKEUP');
			break;

		case "FTN_AMD_YEAR_PLAN_APPR":
			url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_auditing_yearPlan_lst', 'status', 'ALL', 'page_size', '10', 'stylesheet', 'training_plan_year_approval.xsl')
			break;

		case "FTN_AMD_MAKEUP_PLAN_APPR":
			url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'get_out_training_plan_lst', 'status', 'ALL', 'page_size', '10', 'stylesheet', 'training_plan_adhoc_approval.xsl');
			break;

		case "FTN_AMD_YEAR_SETTING":
			var url = wb_utils_invoke_disp_servlet('module', 'tpplan.tpPlanModule', 'cmd', 'set_year_config_prepare', 'stylesheet', 'training_plan_year_setting.xsl', 'year', '0');
			break;

		case "FTN_AMD_EXAM_LIST":
			url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'get_exam_list', 'stylesheet', 'exam_list.xsl');
			break;

		case "FS_LINK_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'JsonMod.links.LinksModule', 'cmd', 'get_fslinks_list', 'stylesheet', 'fs_links_list.xsl');
			break;

		case "FTN_AMD_POSTER_MAIN":
			url = wb_utils_invoke_servlet('cmd', 'get_poster', 'stylesheet', 'poster_details.xsl', 'rpt_type', 'FTN_AMD_POSTER_MAIN');
			break;

		case "FTN_AMD_MOBILE_POSTER_MAIN":
			url = wb_utils_invoke_servlet('cmd', 'get_poster', 'stylesheet', 'poster_details.xsl', 'rpt_type', 'FTN_AMD_MOBILE_POSTER_MAIN');
			break;

		case "FTN_AMD_CREDIT_SETTING_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'JsonMod.credit.CreditModule', 'cmd', 'get_credit_main', 'stylesheet', 'credit_admin_main.xsl');
			break;

		case "CREDIT_OTHER_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'JsonMod.credit.CreditModule', 'cmd', 'get_credit_main', 'stylesheet', 'credit_admin_main.xsl');
			break;

//		case "FTN_AMD_SYS_ROLE_MAIN":
//			url = wb_utils_invoke_disp_servlet('module', 'JsonMod.role.RoleModule', 'cmd', 'get_role_list', 'stylesheet', 'role_list.xsl');
//			break;
		case "FTN_AMD_SUPPLIER_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'supplier.SupplierModule', 'cmd', 'get_supplier_list', 'stylesheet', 'supplier_list.xsl');
			break;

		case "FTN_AMD_CERTIFICATE_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'cert.CertificateModule', 'cmd', 'get_certificate_list', 'stylesheet', 'certificate_list.xsl', 'cur_page', 1,'cert_tc_id','0');
			break;

		case "FTN_AMD_INT_INSTRUCTOR_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'inst_list', 'stylesheet', 'instructor_list.xsl','iti_type_mark', 'IN');
			break;

		case "FTN_AMD_EXT_INSTRUCTOR_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'inst_list', 'stylesheet', 'instructor_list.xsl','iti_type_mark', 'EXT');
			break;


		case "FTN_AMD_ARTICLE_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'article.ArticleModule', 'cmd', 'get_article_list', 'stylesheet', 'article_list.xsl','art_tcr_id',0);
			break;

		case "SMS_GROUP_MANAGE":
			url = wb_utils_controller_base + 'group/groupList/0';
			wbUtilsOpenWin(url, 'SMS_GROUP_MANAGE', false, 'status=yes,scrollbars=yes,width=1100,height=800')
			return;
			break;

		case "FTN_AMD_PROFESSION_MAIN":
//			url = wb_utils_invoke_disp_servlet('module', 'profession.ProfessionModule', 'cmd', 'get_pfs_tree', 'stylesheet', 'pfs_tree.xsl');
			url =  wb_utils_controller_base + "admin/profession";
			break;

		case "FTN_AMD_MESSAGE_TEMPLATE_MAIN":
			url = wb_utils_invoke_disp_servlet('module', 'newmessage.MessageModule', 'cmd', 'get_msg_template_list', 'stylesheet', 'message_template_list.xsl');
			break;

		case "FTN_AMD_EIP_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'JsonMod.eip.EIPModule', 'cmd', 'get_eip_list', 'stylesheet', 'eip_list.xsl');
			break;

		case "FTN_AMD_COURSE_ASSIGN":
			url = wb_utils_invoke_disp_servlet('module', 'ln.course.CourseModule', 'cmd', 'prep', 'stylesheet', 'course_designate_prep.xsl')
			break;

		case "KB_MGT" :
			url = wb_utils_controller_base + '/kb/admin/index';
			wbUtilsOpenWin(url, 'SMS_GROUP_MANAGE', false, 'status=yes,scrollbars=yes,width=1100,height=800')
			return;
			break;
		case "FTN_AMD_POSITION_MAIN":
			url =  wb_utils_controller_base + "admin/position"
			break;
		case "FTN_AMD_USR_REGIETER_APP":
			url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 's_sort_by', 'usr_signup_date', 's_order_by', 'DESC', 's_status', 'PENDING', 'stylesheet', 'usr_reg_approval_lst.xsl', 'filter_user_group', '1')
			break;
		case "FTN_AMD_USR_ACTIVATE":
			url = wb_utils_invoke_servlet('cmd', 'get_suspense_usr', 'page', '1', 'pagesize', '10', 's_sort_by', 'usr_display_bil', 's_order_by', 'ASC', 'stylesheet', 'usr_reactivate_lst.xsl')
			break;
		case "FTN_AMD_GRADE_MAIN":
			url = wb_utils_invoke_servlet('cmd','get_ugr_tree', 'stylesheet', 'ugr_tree.xsl')
			break;
//		case "FTN_AMD_FACILITY_BOOK_CREATE"://fm_rsv_add fm_cart_add
//			url = wb_utils_invoke_disp_servlet("module", "fm.FMModule", "cmd", "get_cart", "fac_type", '','stylesheet','fm_cart_add.xsl')
//			break;
		case "FTN_AMD_FACILITY_BOOK_CALENDAR":
			url = wb_utils_invoke_disp_servlet("module", "fm.FMModule", "cmd", "get_fac_lst", "fac_type", '','stylesheet','fm_calendar_prep.xsl')
			break;
		case "FTN_AMD_FACILITY_BOOK_HISTORY":
			url = wb_utils_invoke_disp_servlet("module", "fm.FMModule", "cmd", "get_fac_lst", "fac_type", '','stylesheet','fm_rsv_record_srh.xsl')
			break;
		case "FTN_AMD_FACILITY_INFO":
			url = wb_utils_invoke_disp_servlet("module", "fm.FMModule", "cmd", "get_fac_lst", "fac_type", '','stylesheet', "fm_facility_list.xsl")
			break;
		case "FTN_AMD_KNOWLEDGE_STOREGE":
			url =  wb_utils_controller_base + "admin/kb/storege"
			break;
		case "FTN_AMD_KNOWLEDEG_CATALOG":
			url =  wb_utils_controller_base + "admin/kbCatalog/index"
			break;
		case "FTN_AMD_KNOWLEDEG_TAG":
			url =  wb_utils_controller_base + "admin/kbTag/index"
			break;
		case "FTN_AMD_KNOWLEDEG_APP":
			url =  wb_utils_controller_base + "admin/kb/approval"
			if(curTime != "" && curTime != undefined){
				url += "?app_status=PENDING"
			}
			break;
		case "FTN_AMD_SNS_GROUP_VIEW": //case "FTN_AMD_SNS_GROUP_MAIN":
			url =  wb_utils_controller_base + "admin/group?isView=true"
			break;
		case "FTN_AMD_Q_AND_A_VIEW": //	case "FTN_AMD_Q_AND_A_MAIN":
			url =  wb_utils_controller_base + "admin/know/allKnow?ftn_type=FTN_AMD_Q_AND_A_VIEW&isView=true"
			break;
		case "FTN_AMD_POSITION_MAP_MAIN": //	case "FTN_AMD_POSITION_MAP_MAIN":
			url =  wb_utils_controller_base + "admin/positionMap"
			break;
		case "FTN_AMD_SPECIALTOPIC_MAIN": //	case "FTN_AMD_SPECIALTOPIC_MAIN":
			url =  wb_utils_controller_base + "admin/specialTopic"
			break;
		case "FTN_AMD_Q_AND_A_VIEW": //	case "FTN_AMD_Q_AND_A_MAIN":
			url =  wb_utils_controller_base + "admin/know/allKnow?ftn_type=FTN_AMD_Q_AND_A_VIEW"
			break;
			
		case "FTN_AMD_SNS_GROUP_MAIN": //case "FTN_AMD_SNS_GROUP_MAIN":
			url =  wb_utils_controller_base + "admin/group"
			break;
		case "FTN_AMD_Q_AND_A_MAIN": //	case "FTN_AMD_Q_AND_A_MAIN":
			url =  wb_utils_controller_base + "admin/know/allKnow?ftn_type=FTN_AMD_Q_AND_A_MAIN"
			break;

		case "FTN_AMD_SYS_ROLE_MAIN":
			url = wb_utils_controller_base + "admin/role/list";
			break;
		case "FTN_AMD_VOTING_MAIN":
			url = wb_utils_controller_base + "admin/voting";
			break;	
		case "SYS_MESSAGE":
			url = wb_utils_controller_base + "admin/message/main";
			break;
		case "FTN_AMD_EXAM_MGT":
			url = wb_utils_controller_base + "admin/course?type=exam";
			break;
		case "FTN_AMD_ITM_COS_MAIN":
			url = wb_utils_controller_base + "admin/course";
			break;
		case "FTN_AMD_OPEN_COS_MAIN":
			url = wb_utils_controller_base + "admin/course?type=open";
			break;
		case "HOMEWORK_CORRECTION":
			url = wb_utils_controller_base + "admin/module/ass";
			break;
		case "FUN_APPR_APP_LIST":
			url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', 'ae_get_my_appn_approval_lst','cur_page', '1', 'aal_status', 'PENDING', 'stylesheet', 'enrollment_list.xsl');
			break;
		case "EXAMINATION_PAPERS":
			url = wb_utils_controller_base + "admin/module/test";
			break;
		case "FUN_EXAMINATION_PAPERS":
			url = wb_utils_controller_base + "admin/module/test";
			break;
		case "FTN_AMD_COS_EVN_MAIN" :
			url = wb_utils_invoke_disp_servlet('module', 'content.CosEvaluationModule', 'cmd', 'get_cos_eval_lst','cur_page', '1', 'page_size', '10', 'stylesheet', 'cos_evn_form_maintain_lst.xsl');
			break;
		case "HELP_CENTER":
			url = wb_utils_controller_base + "help/";
			window.open(url,null,"toolbar=no,scrollbars=yes,status=no,resizable=yes");
			url = null;
			break;
		case "FTN_AMD_LIVE_MAIN": //	case "FTN_AMD_SPECIALTOPIC_MAIN":
			url =  wb_utils_controller_base + "admin/live/list"
			break;
		case "FTN_AMD_SYS_SETTING_LOG":
			url = wb_utils_invoke_servlet('cmd', 'get_thd_syn_log', 'stylesheet', 'threshold_syn_log_prep.xsl');
			break;
		case "FTN_AMD_CPT_D_LIST":  //牌照管理
			url = wb_utils_controller_base + "admin/cpdManagement/index";
			break;
		case "FTN_AMD_CPT_D_LICENSE_LIST":  //牌照注册管理
			url = wb_utils_controller_base + "admin/cpdtRegistrationMgt/list";
			break;
		case "FTN_AMD_CPT_D_HISTORY":  //历史保存记录
			url = wb_utils_controller_base + "admin/cpdHistoryRecord/index";
			break;
		case "FTN_AMD_CPT_D_NOTE":   //报表备注维护
			url = wb_utils_controller_base + 'admin/cpdReportRemark/list';
			break;
        case "FTN_AMD_CPT_D_IMPORT_AWARDED_HOURS":   //导入用户获得CPT/D时数
            url = wb_utils_controller_base + 'admin/cpdtImportAwardedHours/toCpdtHoursAwaededImport';
            break;

	}
	if (url != null) {
		if (window.parent == null) {
			window.location.href =  url;
		} else {
			window.parent.location.href =  url;
		}
	}
}

function wb_utils_fm_set_cookie(token_nm, token_val){
	gen_set_cookie_token('fm', token_nm, token_val, '');
}

function wb_utils_fm_get_cookie(token_nm){
	return gen_get_cookie_token('fm', token_nm);
}

function wb_utils_cancel(){
	window.location.href = wb_utils_get_cookie('url_prev');
}

function wb_utils_go_url(url){
	window.location.href = url;
}

function wb_utils_XmlEscape(str){
	str = str.replace(/&/g, "&amp;")
	str = str.replace(/</g, "&lt;")
	str = str.replace(/>/g, "&gt;")
	return str;
}

function wb_utils_JsEscape(str){
	str = str.replace(/\\/g, "\\\\")
	str = str.replace(/'/g, "\\'")
	str = str.replace(/"/g, "\\\"")
	return str;
}

function addOption(obj, addobj){
	obj.options[obj.options.length] = addobj;
}

function removeSelectedOptions(frm){
	for(var i = (frm.options.length - 1); i >= 0; i--){
		var o = frm.options[i];
		if(o.selected){
			frm.options[i] = null;
		}
	}
	frm.selectedIndex = -1;
}

function doAutoCheck(objFileUpload, objRadioButtonToCheck, objRadioButtonDefault, objHidden) {
	if (objFileUpload.value!='') {
		objHidden.value = objFileUpload.value
	}
}

//========Move Options Up and Down Function===================
function moveOptionsUp(obj){
	for (i = 0; i < obj.options.length; i++){
		if(obj.options[i].selected){
			if(i != 0 && !obj.options[i - 1].selected){
				swapOptions(obj, i, i - 1);
				obj.options[i - 1].selected = true;
			}
		}
	}
}

function moveOptionsDown(obj){
	for (i = obj.options.length - 1; i >= 0; i--){
		if(obj.options[i].selected){
			if(i != (obj.options.length - 1) && !obj.options[i + 1].selected){
				swapOptions(obj, i, i + 1);
				obj.options[i + 1].selected = true;
			}
		}
	}
}

function swapOptions(obj, i, j){
	var o = obj.options;

	var i_selected = o[i].selected;
	var j_selected = o[j].selected;
	var temp = new Option(o[i].text, o[i].value, o[i].defaultSelected, o[i].selected);
	var temp2 = new Option(o[j].text, o[j].value, o[j].defaultSelected, o[j].selected);
	o[i] = temp2;
	o[j] = temp;
	o[i].selected = j_selected;
	o[j].selected = i_selected;
}

//=======================================================
function wb_utils_popup_close(){
	for(i = 0; i < arguments.length; i++){
		obj = eval('window.' + arguments[i])
		if(obj){
			obj.close();
		}
	}
	self.close();
}

function wb_utils_gen_form_focus(frm, priority_obj_nm){
	var i, ele_len, ele

	ele_len = frm.elements.length
	for(i = 0; i < ele_len; i++){
		ele = frm.elements[i]
		if(priority_obj_nm != null && priority_obj_nm != ""){
			if(ele.name == priority_obj_nm){
				if(ele.disabled != true){
					ele.focus()
					return;
				}
			}
		}else{
			//text,textarea,checkbox,radio,select-multiple,select-one
			if(ele.type == 'text' || ele.type == 'textarea' || ele.type == 'checkbox' || ele.type == 'radio' || ele.type == 'select-multiple' || ele.type == 'select-one'){
				if(ele.disabled != true){
					ele.focus()
					return;
				}
			}
		}
	}
}
function wb_utils_s_usr_display_focus(frm){
	document.frmXml.s_usr_display_bil.focus();
}

// --------------------- F. Classified Functions -----------------------------------
// Home ============================================================================
// *** Gen Home Url ***
function wb_utils_gen_home(go_home){
	
	url = wb_utils_gen_home_url(go_home)
	top.location.href = url;
	
}

function wb_utils_gen_home_url(go_home){
	if(go_home == true) {
		url = wb_utils_invoke_servlet('cmd', 'go_home')
	} else {
		url = wb_utils_invoke_servlet('cmd', 'home', 'stylesheet', 'home.xsl')
	}
	return url;
}

// *** Change Role ***
function wb_utils_change_role(frm, lang){
	url = wb_utils_invoke_servlet('cmd', 'change_role', 'rol_ext_id', frm.role_ext_id[frm.role_ext_id.selectedIndex].value);

	if(lang == null){
		lang = 'en'
	}

	var load_msg = eval('wb_msg_' + lang + '_loading_msg')

	if(document.all){
		document.write('<font size="2" face="arial">' + load_msg + '</font>')
	}
	window.location.href = url;
}

function wb_utils_lrn_change_role(select) {
	url = wb_utils_invoke_servlet('cmd', 'change_role', 'rol_ext_id', select[select.selectedIndex].value);
	window.location.href = url;
}

function wb_utils_lrn_change_role_lrn(role) {
	url = wb_utils_invoke_servlet('cmd', 'change_role', 'rol_ext_id', role);
	window.location.href = url;
}

function wb_utils_rpt_go(rte_id, rpt_type, stylesheet) {
	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_lst","stylesheet",stylesheet,"rpt_type",rpt_type,"rte_id",rte_id);
	window.location.href = url;
}

// *** Change Language ***
function wb_utils_change_lang(encoding, url_success){
	if(url_success == null || url_success == ""){
		url_success = parent.location.href;
	}
	var url = wb_utils_invoke_servlet("cmd", "change_lan", "label_lan", encoding, "url_success", url_success);
	parent.location.href = url;
}

// *** Catalog Root ***
function wb_utils_cata_lst(show_all,cat_tcr_id){
    var url='';
    if (show_all) {
        if (show_all=='true') {
            url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_cat_lst', 'stylesheet', 'catalog_lst.xsl', 'show_all', 'true');
        } else {
            url = wb_utils_invoke_servlet('cmd', 'get_tree_frame', 'stylesheet', 'wb_tree_frame.xsl', 'tree_frame_type', 'course_catalog')
        }
    } else {
	    url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_cat_lst', 'stylesheet', 'catalog_lst.xsl',"cat_tcr_id",cat_tcr_id);
    }
		window.location.href = url;
}

// *** Node List ***
function wb_utils_node_lst(tnd_id, list_type, order_by, sort_order, cur_page, page_size,cat_tcr_id){
	var url = wb_utils_node_lst_url(tnd_id, list_type, order_by, sort_order, cur_page, page_size,cat_tcr_id);
		window.parent.location.href = url;
}

function wb_utils_node_lst_url(tnd_id, list_type, order_by, sort_order, cur_page, page_size,cat_tcr_id){
	var url_failure = '';
	var url = '';

	if(list_type == null || list_type == 'item'){
		if(order_by == null){
			if(sort_order == null){
				sort_order = 'asc'
			}
			order_by = 'itm_code'
		}else{
			if(sort_order == null){
				sort_order = 'asc'
			}
		}

		if(cur_page == null || cur_page == ''){
			cur_page = '0';
		}

		if(page_size == null || page_size == ''){
			page_size = '10';
		}

		var show_all = '';
		if (document.frmSearch && document.frmSearch.show_all) {
		    show_all = document.frmSearch.show_all.value;
		}
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_tnd_cnt_lst', 'tnd_id', tnd_id, 'list', 'item', 'sort_col', order_by, 'sort_order', sort_order, 'page_size', page_size, 'cur_page', cur_page, 'url_failure', url_failure, 'stylesheet', 'itm_node_lst.xsl', 'show_all', show_all)
	}else{
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_tnd_cnt_lst', 'tnd_id', tnd_id, 'list', list_type, 'url_failure', url_failure, 'stylesheet', 'itm_node_lst.xsl',"cat_tcr_id",cat_tcr_id)
	}
	return url;
}

// *** Global Node List ***
function wb_utils_glb_node_lst(tnd_id){
	var url_failure = ''
	var url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_tnd_cnt_lst', 'tnd_id', tnd_id, 'list', 'item', 'stylesheet', 'glb_itm_node_lst.xsl')
	window.parent.location.href = url;
}

// *** Resource Manager ***
function wb_utils_adm_syb_lst(privilege,choice,show_all,tcr_id){
	if(!choice) choice = "";
	var url = wb_utils_adm_syb_lst_url(privilege,choice,show_all, tcr_id)
	window.parent.location.href = url;
}

function wb_utils_adm_syb_lst_url(privilege,choice,show_all,tcr_id){
	if(!choice || choice =="")  choice="myFolder";
	if(privilege == null || privilege == ''){
		privilege = 'AUTHOR';
	}
	if(show_all=='' || show_all==null){
		show_all =false;
	}
	if(tcr_id == '' || tcr_id== null){
		tcr_id = 0;
	}
	var url = wb_utils_invoke_servlet('cmd', 'get_syb_obj', 'syb_privilege', privilege, 'res_type', 'QUE~GEN~AICC~ASM~SCORM~NETGCOK', 'res_lan', '', 'stylesheet', 'res_obj_lst.xsl','folders',choice,'show_all',show_all, 'obj_tcr_id', tcr_id)
	return url;
}

// CSS Initiation ==================================================================
function wb_utils_init_css(){
	//Man: to be deprecated from V4
	frm = arguments[0];
	if(document.all || document.getElementById != null){
		n = frm.elements.length;
		for(i = 0; i < n; i++){
			ele = frm.elements[i]
			for(j = 1; j < arguments.length; j++){
				if(ele.type == arguments[j]) ele.className = "wbGenInputFrm"
			}
		}
	}
}

function wb_utils_adm_acc_control(rol_ext_id){
	var url = wb_utils_invoke_disp_servlet("module", "accesscontrol.AccessControlModule", "cmd", "get_rol_ftn", "rol_ext_id", rol_ext_id, "stylesheet", 'adm_acc_control.xsl')
	window.location.href = url;
}

// Learner 2nd level links =========================================================
function wb_utils_lrn_cos_lrn_lst(course_id, orderby, sortby, pagesize, cur_page){
	var str_feature = 'toolbar=' + 'no' + ',width=' + '780' + ',height=' + '400' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes';

	if(orderby == null){
		orderby = 'usr_display_bil';
	}

	if(sortby == null){
		sortby = 'ASC';
	}

	if(pagesize == null){
		pagesize = '10';
	}

	if(cur_page == null){
		cur_page = '1';
	}

	var url = wb_utils_invoke_servlet('course_id', course_id, 'cmd', 'get_cos_lrn_lst', 'order_by', orderby, 'sort_by', sortby, 'pagesize', pagesize, 'cur_page', cur_page, 'stylesheet', 'lrn_cos_lrn_lst.xsl', 'url_failure', '')
	wbUtilsOpenWin(url, 'cos_lrn_lst', false, str_feature);
}

function wb_utils_lrn_course_lst(){
	var url = wb_utils_invoke_ae_servlet('cmd','ae_get_all_enrol','stylesheet','lrn_course_lst.xsl');
	window.parent.location.href = url
}

// ================================================================================
function wb_utils_mt_help(){
	var url = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'help.xsl')
	wbUtilsOpenWin(url, 'HelpPage', false, 'status=yes,scrollbars=yes,width=380,height=130')
}

function wb_utils_validate_site_id(fld, lang){
	if(fld.value == ''){
		return false;
	}else{
		return true;
	}
}

// -------------------- G. For loading applet ----------------------------------
// while the applet is finish loading
function wb_utils_finish_loading_applet(){
	// I.E.
	if(document.all){
		self.DIV2.style.left = -1500;
		self.DIV1.style.left = 0;
	}
	// NS6
		else if(document.getElementById){
		document.getElementById("DIV2").style.left = -500;
	}
	// NS other than 6
		else{
		document.layers["DIV2"].left = -500;
	}
}

//Date Utils
function _wbUtilsCalendarMakeArray(){
	this[0] = _wbUtilsCalendarMakeArray.arguments.length;
	for(i = 0; i < _wbUtilsCalendarMakeArray.arguments.length; i = i + 1) this[i + 1] = _wbUtilsCalendarMakeArray.arguments[i];
}

//----------------------H. Validate Functions with new Label System----------------------
// ================================================================================
function wbUtilsValidateEmptyField(fld, txtFldName){
	var val = (fld.type.toLowerCase().indexOf('select') != -1) ? fld.options[fld.selectedIndex].value : val = fld.value;

	if(val.length == 0 || val.search(/^\s+$/) != -1){
		Dialog.alert(wb_msg_usr_please_specify_value + '"' + txtFldName + '"' );
		if(fld.type == 'textarea' || fld.type == "text") fld.focus();
		return false;
	}
	return true;
}

// ================================================================================
function wbUtilsValidateDate(fldName, txtFldName, vType,dType){
	// form name should be "frmXml"
	//时间格式检验
	if(vType == null) vType = '';//check year?/month?/day?, '' check all
	if(dType === undefined || dType === '') {
		dType = 'ymd';
	}
	

	// validate year
	if(vType == '' || vType == 'YY'){
		var fld = eval(fldName + '_yy')

		fld.value = wbUtilsTrimString(fld.value)

		if(fld.value.length != 4 || Number(fld.value) < 1800){
			if(dType === 'ymd') {
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymd);
			} else if(dType === 'ymdhm'){
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymdhm);
			}else{
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ym);
			}

			fld.focus();
			return false;
		}

		if(!wbUtilsValidateInteger(fld, txtFldName)) return false;
	}
	// validate month
	if(vType == '' || vType == 'MM'){
		var fld = eval(fldName + '_mm')
		fld.value = wbUtilsTrimString(fld.value)

		if(Number(fld.value) < 10 && fld.value.length == 1){
			fld.value = '0' + fld.value
		}

		if(fld.value.length != 2 || fld.value > 12 || fld.value < 1){
			if(dType === 'ymd') {
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymd);
			} else if(dType === 'ymdhm'){
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymdhm);
			}else{
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ym);
			}
			fld.focus();
			return false;
		}

		if(!wbUtilsValidateInteger(fld, txtFldName)) return false;
	}
	// validate day
	if(vType == '' || vType == 'DD'){
		var fld = eval(fldName + '_dd')

		fld.value = wbUtilsTrimString(fld.value)

		if(Number(fld.value) < 10 && fld.value.length == 1){
			fld.value = '0' + fld.value
		}
		if(fld.value.length != 2 || fld.value >gen_Month_Length(Number(eval(fldName + '_mm.value')), eval(fldName + '_yy.value')) || fld.value < 1){
			if(dType === 'ymd') {
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymd);
			} else {
				Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymdhm);
			}
			fld.focus();
			return false;
		}
		if(!wbUtilsValidateInteger(fld, txtFldName)) return false;
	}
	if(dType === 'ymdhm') {
			// validate hour
		if(vType == '' || vType == 'hh') {
			var fld = eval(fldName + '_hour')
				fld.value = wbUtilsTrimString(fld.value)

				if ( Number(fld.value) < 10  && fld.value.length == 1 ){
					fld.value = '0' + fld.value
				}
				if ( fld.value.length != 2  || fld.value > 23 || fld.value < 0 ){
					Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymdhm);
						fld.focus();
						return false;
				}

				if (!wbUtilsValidatePositiveInteger(fld, txtFldName)) {
					return false;
				}
			}
			// validate minute
			if(vType == '' || vType == 'mm') {
				fld = eval(fldName + '_min')
				fld.value = wbUtilsTrimString(fld.value)
				if ( Number(fld.value) < 10  && fld.value.length == 1  ){
					fld.value = '0' + fld.value
				}

				if ( fld.value.length != 2  || fld.value > 59 || fld.value < 0 ){
					Dialog.alert('"' + txtFldName + '"' + wb_msg_datetype_ymdhm);
						fld.focus();
						return false;
				}

				if (!wbUtilsValidatePositiveInteger(fld,txtFldName)) {
					return false;
				}
			}
	}

	return true;
}

// ================================================================================

function wbUtilsValidateTime(fldName, txtFldName) {
		// validate hour
			var fld = eval(fldName + '_hour')
			fld.value = wbUtilsTrimString(fld.value)
			if ( Number(fld.value) < 10  && fld.value.length == 1 ){
					fld.value = '0' + fld.value
				}
				if ( fld.value.length != 2  || fld.value > 23 || fld.value < 0 ){
						alert(wb_msg_pls_enter_valid_hour_1 + txtFldName + wb_msg_pls_enter_valid_hour_2);
						fld.focus();
						return false;
				}

				if (!wbUtilsValidatePositiveInteger(fld, txtFldName)) {
					return false;
				}

		// validate minute
			fld = eval(fldName + '_min')
			fld.value = wbUtilsTrimString(fld.value)
			if ( Number(fld.value) < 10  && fld.value.length == 1  ){
					fld.value = '0' + fld.value
				}

				if ( fld.value.length != 2  || fld.value > 59 || fld.value < 0 ){
						alert(wb_msg_pls_enter_valid_hour_1 + txtFldName + wb_msg_pls_enter_valid_hour_2);
						fld.focus();
						return false;
				}

				if (!wbUtilsValidatePositiveInteger(fld,txtFldName)) {
					return false;
				}
			return true;
}

// ================================================================================
function wb_utils_validate_date_compare(config){
	var frm,start_obj,end_obj,start_nm, end_nm, focus_obj,_start_date, _end_date
	frm  = config.frm
  start_obj = config.start_obj
  end_obj = config.end_obj
	start_nm = config.start_nm
	end_nm = config.end_nm
	focus_obj = config.focus_obj

	_start_date = new Date(eval(frm + '.' + start_obj + '_yy.value'), Number(eval(frm + '.' + start_obj + '_mm.value'))-1, Number(eval(frm + '.' + start_obj + '_dd.value')), Number(eval(frm + '.' + start_obj + '_hour.value')), Number(eval(frm + '.' + start_obj + '_min.value')))
	_end_date = new Date(eval(frm + '.' + end_obj + '_yy.value'), Number(eval(frm + '.' + end_obj + '_mm.value'))-1, Number(eval(frm + '.' + end_obj + '_dd.value')), Number(eval(frm + '.' + end_obj + '_hour.value')), Number(eval(frm + '.' + end_obj + '_min.value')))

	if (focus_obj === undefined || focus_obj === '') {focus_obj = start_obj;}
	if(_start_date > _end_date){
		if(start_nm === undefined || start_nm === '') {
			Dialog.alert(wb_msg_from_larger_to)
		} else {
			Dialog.alert('"' + end_nm + '" ' + wb_msg_cannot_earlier_than + ' "' + start_nm + '"'); 
		}
		eval(frm + '.' + focus_obj + '_yy.focus()')
		return false;
	}
	else if(_start_date == _end_date){
		if(start_nm === undefined || start_nm === '') {
			Dialog.alert(wb_msg_from_larger_to)
		}
		return true;
	}
	return true;
}

// ================================================================================
function wbUtilsValidateInteger(fld, txtFldName, CourseName){
	var valPass = true
	var val = wbUtilsTrimString(fld.value)

	//if((fld.value.indexOf('.') == 0) || (fld.value.lastIndexOf('.') == fld.value.length - 1)){
	if((fld.value.indexOf('.') >= 0) ){
		valPass = false;
	}else if(val.length == 0 || val.search(/[^0-9]|\-[^0-9]/) != -1){
		if(isNaN(Number(val))){
			valPass = false;
		}
	}
	if (val<1) {
	    valPass = false;
	}
	if(valPass == false){
		if(CourseName != null){
            alert(wb_msg_pls_enter_positive_integer_1 + txtFldName + wb_msg_enter_less_cnt1 + CourseName + wb_msg_enter_less_cnt2);
        }else {
            //不正确(YYYY-MM-DD hh:mm)提示 --- 公告
            alert(txtFldName + wb_msg_datetype_ymdhm);
        }
        fld.focus();
		return false;
	}else{
		return true;
	}
}
//===================================================================
function wbUtilsValidateAllFloat(fld, txtFldName){
	var val = wbUtilsTrimString(fld.value);
	if ( (val.search(/^\d{1,3}$/) == -1 && val.search(/^\d{1,3}\.\d{1,2}$/) == -1) || parseFloat(val) >100) {
		alert(wb_msg_pls_enter_value_less_than_100_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_value_less_than_100_2);
		fld.focus();
		return false;
	}
	return true;
}
// ================================================================================
function wbUtilsValidateAllInteger(fld, txtFldName){
	var valPass = true

	var val = wbUtilsTrimString(fld.value)

	if((fld.value.indexOf('.') >= 0) ){
		valPass = false;
	}else if(val.length == 0 || val.search(/[^0-9]|\-[^0-9]/) != -1){
		if(isNaN(Number(val))){
			valPass = false;
		}
	}
	if(valPass == false){
		alert(wb_msg_pls_enter_integer_1 + txtFldName  + wb_msg_pls_enter_integer_2);
		fld.focus();
		return false;
	}else{
		return true;
	}
}

// ================================================================================
function wbUtilsValidateEmail(fld_email, txtFldName) {
	var p = /^[a-zA-Z0-9\._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	if (p.test(fld_email.value)) {
		return true;
	} else {
		alert(wb_msg_usr_enter_valid + txtFldName);
		fld_email.focus();
		return false;
	}
}

// ================================================================================
function wbUtilsValidatePositiveInteger(fld, txtFldName){
	// This function check for integer range  0  to  2^31
	// Notes: Include zero
	var val = wbUtilsTrimString(fld.value)
	if(val.search(/[^0-9]/) != -1){
		Dialog.alert(wb_msg_pls_enter_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}

	if(val > Math.pow(2, 31)){
		alert(wb_msg_pls_enter_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}
	return true;
}
// ================================================================================
function wbUtilsValidateNonZeroPositiveInteger(fld, txtFldName){
	// This function check for integer range  1  to  2^31
	// Notes: NOT include zero
	var val = wbUtilsTrimString(fld.value)
	if(val == 0){
		Dialog.alert(wb_msg_pls_enter_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}

	if(val.search(/[^0-9]/) != -1){
		Dialog.alert(wb_msg_pls_enter_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}

	if(val > Math.pow(2, 31)){
		Dialog.alert(wb_msg_pls_enter_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}
	return true;
}
// ================================================================================
function wbUtilsValidateNonZeroPositiveFloat(fld, txtFldName){
	// This function check for integer range  1  to  2^31
	// Notes: NOT include zero
	var val = wbUtilsTrimString(fld.value)
	if(val == 0){
		Dialog.alert(wb_msg_usr_enter_positive_integer + txtFldName);
		fld.focus();
		return false;
	}
	if(val.search(/[^0-9]\.\d{0,2}/) != -1){
		Dialog.alert(wb_msg_usr_enter_positive_integer + txtFldName);
		fld.focus();
		return false;
	}

	if(val > Math.pow(2, 31)){
		Dialog.alert(txtFldName + wb_msg_usr_enter_smaller_number);
		fld.focus();
		return false;
	}
	return true;
}
//=================================================================================
function wbUtilsValidateUserPassword(fld_usr_pwd, txtFldName, min, max){
	/*
	Format:	Alphabets
			numbers
			underscore (_)
			hypen (-)
	*/
	/*
	var _MIN_FIELD_LEN = 4

	var _MAX_FIELD_LEN = 20
	*/

	if(fld_usr_pwd.value.length == 0){
		alert(wb_msg_usr_please_enter_the + '"' + txtFldName + '"');
		fld_usr_pwd.focus();
		return false;
	}

	if( min != '' ) {
		if(fld_usr_pwd.value.length < min){
			//alert(wb_msg_usr_please_enter_at_least + min + wb_msg_usr_character_for_the + '"' + txtFldName + '"');
			alert(wb_msg_usr_enter_valid + '"' + txtFldName + '"' + '. ' + wb_msg_usr_min_character + min);
			fld_usr_pwd.focus();
			return false;
		}
	}

	if(fld_usr_pwd.value.search(/[^A-Za-z0-9-_]/) != -1){
		alert(wb_msg_usr_enter_valid + '"' + txtFldName + '"')
		fld_usr_pwd.focus();
		return false;
	}

	if( max != '' ) {
		if(fld_usr_pwd.value.length > max){
			alert(wb_msg_usr_enter_valid + '"' + txtFldName + '"')
			fld_usr_pwd.focus()
			return false;
		}
	}
	return true;
}

// ================================================================================
function wbUtilsValidateUserId(fld_usr_id, txtFldName, min, max){
	/*
	Format:	Lower case alphabets
			numbers
			underscore (_)
			hypen (-)
	*/
//	if(fld_usr_id.value.toLowerCase() != fld_usr_id.value){
//		alert(wb_msg_usr_enter_lower_case_char + '"' + txtFldName + '"');
//		return false;
//	}

	if(fld_usr_id.value.search(/[^A-Za-z0-9_-]/) != -1){
		alert(wb_msg_usr_enter_english_char + '"' + txtFldName + '"');
		fld_usr_id.focus();
		return false;
	}

	if(fld_usr_id.value.length == 0){
		alert(wb_msg_usr_please_enter_the + '"' + txtFldName + '"');
		fld_usr_id.focus();
		return false;
	}

	if( min != '' ) {
		if (fld_usr_id.value.length < min) {
			//alert(wb_msg_usr_please_enter_at_least+ min + wb_msg_usr_character_for_the +'"' + txtFldName + '"');
			alert(wb_msg_usr_enter_valid + '"' + txtFldName + '"' + '. ' + wb_msg_usr_min_character + min);
			fld_usr_id.focus();
			return false;
		}
	}

	if( max != '' ) {
		if(fld_usr_id.value.length > max){
			alert(wb_msg_usr_enter_valid + '"' + txtFldName+ '"')
			fld_usr_id.focus()
			return false;
		}
	}
	return true;
}

//=================================================================================
function wbUtilsMaxOrMinLength(fld_usr_id, txtFldName, min, max, lang){
	if( min != '' ) {
		if (getChars(fld_usr_id.value) < min) {
			alert(wb_msg_usr_enter_valid + '"' + txtFldName + '"' + '. ' + wb_msg_usr_min_character +' '+ min + ' ' + eval('wb_msg_'+lang+'_characters'));
			fld_usr_id.focus();
			return false;
		}
	}

	if( max != '' ) {
		 var s=getChars(fld_usr_id.value);
		if(getChars(fld_usr_id.value) > max){
			if(lang != '' && lang != undefined){
				alert(wb_msg_usr_enter_valid + '"' + txtFldName+ '"'+'. ' + eval('wb_msg_'+lang+'_word_limit') +' '+ max +' '+ eval('wb_msg_'+lang+'_characters'));
			}else{
				alert(wb_msg_usr_enter_valid + '"' + txtFldName+ '"'+'. ');
			}
			
			fld_usr_id.focus()
			return false;
		}
	}
	return true;
}

//wb_utils_check_chinese_char
function wb_utils_check_chinese_char(name) {
//		if(name.search(/[^A-Za-z0-9 ~`!@%&()_=|{}:;"'<>,.\/\#\$\^\*\-\+\[\]\?\\]/)!=-1){
//	        alert(wb_msg_name_with_CN);
//		return false;
//		}
	return true;
}

function wb_utils_check_invalid_filename(filename){
		if(name.search(/&<\"/)!=-1){
		alert(wb_msg_name_with_CN);
		return false;
		}
	return true;
}
//Christ.qiu
//wb_utils_get_filename_from_path
function wb_utils_get_filename_from_path(path){
	var indexOfLastFileSeperator = 0;
	var seperator1,seperator2;
	seperator1 = path.lastIndexOf('\\');
	seperator2 = path.lastIndexOf('/');
	if(seperator1==seperator2){
		return path;
	}
	else if(seperator1>seperator2){
		return path.substring(seperator1+1);
	}else{
		return path.substring(seperator2+1);
	}
}

//Christ.qiu
//wb_utils_check_duplicate_filename
function wb_utils_check_duplicate_filename(frm,file_eles,paths){
	var files = new Array;
	var cur_index = 0;
	var cur_filename = '';
	for (var i = 0 ; i < paths.length ; i++){
		cur_filename = wb_utils_get_filename_from_path(paths[i]);
		for(var j = 0 ; j < files.length ; j++){
			if(cur_filename!='' && cur_filename == files[j]){
				alert(wb_msg_duplicate_filename);
				eval("frm."+file_eles[i]).focus();
			return -1;
			}
		}
		files[i] = cur_filename;
	}
}
/*Christ qiu:select checkbox group
 *param: checkbox_group is the array name of checkbox
 */
function wb_utils_select_checkbox_group(checkbox_group){
	if(checkbox_group.type='checkbox'){
		//in case of checkbox_group is a checkbox not a checkbox group
		checkbox_group.checked = "checked";
	}
	for (var i=0;i<checkbox_group.length;i++){
		checkbox_group[i].checked="checked";
	}
}
/*Christ qiu:unselect checkbox group
 *param: checkbox_group is the array name of checkbox
 */
function wb_utils_unselect_checkbox_group(checkbox_group){
	if(checkbox_group.type='checkbox'){
		//in case of checkbox_group is a checkbox not a checkbox group
		checkbox_group.checked = "";
	}
	for (var i=0;i<checkbox_group.length;i++){
		checkbox_group[i].checked="";
	}
}

function getXMLHttpRequest() {
	req = false;
    // branch for native XMLHttpRequest object
    if(window.XMLHttpRequest) {
    	try {
			req = new XMLHttpRequest();
        } catch(e) {
			req = false;
        }
    // branch for IE/Windows ActiveX version
    } else if(window.ActiveXObject) {
       	try {
        	req = new ActiveXObject("Msxml2.XMLHTTP");
      	} catch(e) {
        	try {
          		req = new ActiveXObject("Microsoft.XMLHTTP");
        	} catch(e) {
          		req = false;
        	}
		}
	}
	if(req) {
		return req;
	}
}

	function getOrderCookie(type){
				var itm_order=gen_get_cookie('itm_sort_order');
				var old_order='';
				if(itm_order!=null &&itm_order.length>0){
					var order =itm_order.split(',');
					for(var i=0; i<order.length;i++){
						if(order[i].indexOf(type)!=-1){
							old_order=order[i];
						}
					}

				}
				return old_order;
			}

			function getOrderCol(type){
				var old_order=getOrderCookie(type);
				var order = new Array();
				if(old_order!=null && old_order.length>0){
					order=old_order.split('||');
					return order[1];
				}
				return '';
			}

			function getOrderSort(type){
				var old_order=getOrderCookie(type);
				var order = new Array();
				if(old_order!=null && old_order.length>0){
					order=old_order.split('||');
					return order[2];
				}
				return '';
			}

function createcssmenu2(){
	var cssmenuids = ["cssmenu2"] //Enter id(s) of CSS Horizontal UL menus, separated by commas
	var csssubmenuoffset = 0 //Offset of submenus from main menu. Default is 0 pixels.
	var ultags;
	for (var i = 0; i < cssmenuids.length; i++){
		if (document.getElementById(cssmenuids[i])) {
			ultags = document.getElementById(cssmenuids[i]).getElementsByTagName("ul");
		    for (var t = 0; t < ultags.length; t++){
		    	setWidth(ultags[t].getElementsByTagName("a"), ultags[t].getElementsByTagName("span"));
				ultags[t].style.top = ultags[t].parentNode.offsetHeight + csssubmenuoffset + "px";
		    	ultags[t].parentNode.onmouseover = function(){
					var curObj = this.getElementsByTagName("ul")[0];
					curObj.style.visibility = "visible";
					var menuShim = document.getElementById(curObj.id + "_shim");
					if (menuShim !== null) {
						menuShim.style.display = "block";
					} else {
						menuShim = document.createElement("iframe");
						menuShim.id = curObj.id + "_shim";
						menuShim.frameBorder = "no";
						menuShim.scrolling = "no";
						menuShim.width = curObj.clientWidth;
						menuShim.height = curObj.clientHeight;
						menuShim.style.backgroundColor = "#F9CF93";
						menuShim.style.position = "absolute";
						menuShim.style.left = getLeft(curObj);
						menuShim.style.top = getTop(curObj);
						document.body.appendChild(menuShim);
					}
		    	}
		    	ultags[t].parentNode.onmouseout = function(){
					var curObj = this.getElementsByTagName("ul")[0];
					curObj.style.visibility = "hidden";
					var menuShim = document.getElementById(curObj.id + "_shim");
					if (menuShim !== null) {
						menuShim.style.display = "none";
					}
		    	}
		    }
		}
	}
}

function getLeft(obj) {
	var thisValue = 0;
	if (obj != null) {
		thisValue = obj.offsetLeft + getLeft(obj.offsetParent);
	}
	return thisValue;
}

function getTop(obj) {
	var thisValue = 0;
	if (obj!=null) {
		thisValue = obj.offsetTop + getTop(obj.offsetParent);
	}
	return thisValue;
}

function setWidth(aArr, spanArr) {
	var length;
	var offset = 15;
	var defLegnth = 120;
	var maxlength = getMaxLength(aArr);
	length = offset * maxlength;
	if (length < defLegnth) {
		length = defLegnth;
	}
	for (var i = 0; i < aArr.length; i++) {
		aArr[i].style.width = length + 'px';
	}
	for (var i = 0; i < spanArr.length; i++) {
		spanArr[i].style.width = length + 'px';
	}
}

function getMaxLength(arr) {
	var maxlength = 0;
	for (var i = 0; i < arr.length; i++) {
		if (maxlength < arr[i].innerHTML.length) {
			maxlength = arr[i].innerHTML.length;
		}
	}
	return maxlength;
}

if (window.addEventListener)
	window.addEventListener("load", createcssmenu2, true);
else if (window.attachEvent)
	window.attachEvent("onload", createcssmenu2);


function wbUtilsValidateImgType(field,lang){
	if(field.value != null && field.value.length > 0){
		var img_url = field.value;
		var file_ext = img_url.substring( img_url.lastIndexOf('.')+1,img_url.length);
		file_ext =file_ext.toLowerCase();
		if(!(file_ext == 'jpg' || file_ext == 'gif' || file_ext == 'png')){
				alert(eval('wb_msg_'+lang+'_media_not_support'));
				field.focus();
				return false;
		}
	}
	return true;
}

// -------------------- open win functions --------------------
function wbUtilsOpenWin (win_url, win_name, is_full_screen, str_feature) {
	var win_obj = null;
	if (str_feature === undefined || str_feature === null
			|| str_feature.length === 0) {
		// use default window feature
		str_feature = 'location=0,menubar=0,resizable=1,scrollbars=1,status=1,toolbar=0';
	}
	if (win_name === undefined) {
		win_name = '';
	}
	win_obj = window.open(win_url, win_name, str_feature);
	if (win_obj != null) {
		if (is_full_screen) {
			win_obj.moveTo(0, 0);
			win_obj.resizeTo(screen.availWidth, screen.availHeight);
		}
		win_obj.focus();
	}
	return win_obj;
}

// -------------------- open win functions --------------------
function wb_utils_open_win(url, win_name, width, height) {
	var feature = 'toolbar='		+ 'yes'
		+ ',width=' 				+ width
		+ ',height=' 				+ height
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '10'
		+ ',screenY='				+ '10'
		+ ',status='				+ 'yes'
	if(document.all){
		feature += ',top='		+ '10'
		feature += ',left='		+ '10'
	}

	var win = wbUtilsOpenWin(url, win_name, false, feature);
	win.focus();
	return win;
}

// -------------------- delete the blank before/after the string --------------------
function wbUtilsTrimString(str) {
	if (typeof(str) === "string") {
		return str.replace(/(^(\s|\u3000)*)|((\s|\u3000)*$)/g, "");
	} else {
		return str;
	}
}

/*
 * 鼠标onmouseover,onmouseout出提示信息功能
 */
function wbUtilsChangeToolTipDis (th,event, obj_id, mode, div_inner_html) {
	var top_pos = 0;
	var left_pos = 0;
	//console.log(th.parentNode.parentNode.offsetLeft)
	if (mode === undefined || mode === '' || mode === 'hide') {
		mode = 'none';
	} else if (mode === 'show') {
		mode = 'block';
		/*left_pos = document.body.scrollLeft + event.clientX + 10;
		top_pos = document.body.scrollTop + event.clientY + 10;*/
		/*left_pos =  event.client-500 ;
		top_pos =  event.clientY -100;*/
	}
	if (obj_id !== undefined && obj_id !== '') {
		var obj = document.getElementById(obj_id);
		if (obj !== null) {
			if (div_inner_html !== undefined && div_inner_html !== '') {
				obj.innerHTML = div_inner_html;
			}
			obj.style.top =  th.parentNode.parentNode.offsetTop+th.offsetHeight*2+6+"px";
			obj.style.left = th.parentNode.parentNode.offsetLeft+th.offsetWidth*2+"px";
			obj.style.display = mode;
		}
	}
}

//form change checkbox
function wb_utils_change_checkbox(frm, is_checked, box_name){
	var is_all = (box_name && box_name.length) ? false : true;

	for (var i = 0, ele; ele = frm.elements[i]; i++){
		if (ele.type === 'checkbox' && (is_all ? true : ele.name === box_name)) {
			ele.checked = is_checked;
		}
	}
}

function wbUtilsValidateFloat(fld, txtFldName) {
	var val = wbUtilsTrimString(fld.value);
	if ( (val.search(/^\d{1,9}$/) == -1 && val.search(/^\d{1,9}\.\d{1,2}$/) == -1)) {
		alert(wb_msg_pls_enter_value_less_than_999999999_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_value_less_than_999999999_2);
		fld.focus();
		return false;
	}
	return true;
}

function wbUtilsButtonUp(obj) {
	var cells = obj.rows[0].cells;
	cells[0].style.backgroundImage = 'url(../wb_image/wb_utils_button_left_2.gif)';
	cells[1].style.backgroundImage = 'url(../wb_image/wb_utils_button_center_2.gif)';
	cells[2].style.backgroundImage = 'url(../wb_image/wb_utils_button_right_2.gif)';
}

function wbUtilsButtonOut(obj) {
	var cells = obj.rows[0].cells;
	cells[0].style.backgroundImage = 'url(../wb_image/wb_utils_button_left.gif)';
	cells[1].style.backgroundImage = 'url(../wb_image/wb_utils_button_center.gif)';
	cells[2].style.backgroundImage = 'url(../wb_image/wb_utils_button_right.gif)';
}

function getLabel(lab_name, datas) {
	var result = '!!!' + lab_name;
	if (page_label.hasOwnProperty(lab_name)) {
		var count = 0;
		result = page_label[lab_name].replace(/\$data/g, function(str, index) {
			if (datas === undefined || datas === null) {
				return result;
			}
			return datas[count++];
			});
	}
	return result;
}

function dyniframesize(down, h) {
	$('#' + down).height(h < 600 ? 600 : h);
	$(document).css('height', 'auto')
}

function r() {
	if (parent && parent.dyniframesize) {
		parent.dyniframesize('page_content', h());
	}
}

function h() {
	return $('#container').height() + 10;
}

function go(url) {
	window.location.href = url;
}

function ajaxtable(option, param, action, arg1, arg2) {
	if (typeof option == 'undefined') {
		return;
	}

	var page = 0;
	var pages = 0;
	var start = 0;
	var limit = 0;
	var total = 0;
	var sort = 0;
	var dir = 0;

	var newp = 0;
	var checkpass = false;
	if (action) {
		switch (action) {
			case 'first':
				newp = 1;
				checkpass = true;
				break;
			case 'prev':
				if (page > 1) {
					newp = page - 1;
				}
				checkpass = true;
				break;
			case 'next':
				if (page < pages) {
					newp = page + 1;
				}
				checkpass = true;
				break;
			case 'last':
				newp = pages;
				checkpass = true;
				break;
			case 'sort':
				if (typeof arg1 != 'undefined') {
					sort = arg1;
				}
				if (typeof arg2 != 'undefined') {
					dir = arg2;
				}
				checkpass = true;
				break;
			case 'page':
				if (typeof arg1 != 'undefined') {
					var nv = parseInt(arg1)
					if (isNaN(nv)) {
						nv = 1;
					}
					if (nv < 1) {
						nv = 1;
					} else if (nv > pages) {
						nv = pages;
					}
					newp = nv;
				}
				checkpass = true;
				break;
		}
	}

	if (checkpass) {
		sendParam = $.extend({
			start : start,
			limit : limit,
			sort : sort,
			dir : dir
		}, param);

		$.ajax({
			url : param.url,
			data : sendParam,
			dataType : 'html',
			cache : false
		}).done(function(data) {
			$(container).html(data);

			if (typeof option.callback == 'function') {
				option.callback();
			}
		});
	}
}
function removeEditorSpaces(value) {
    if (value.indexOf('<p>') == 0 || value.indexOf('&nbsp;') == 0) {
        var blank;
        if (value.indexOf('&nbsp;') == 0) {
            blank = value.replace(/\s/g, '').split('&nbsp;');
        } else {
            var start = value.indexOf('<p>') + 3;
            var end = value.indexOf('</p>');
            blank = value.substring(start, end).replace(/\s/g, '').split('&nbsp;');
        }
        var tmp = 0;
        if (blank != null && blank.length > 0) {
            for (j = 0; j < blank.length; j++) {
                if (blank[j] == '') {
                    tmp++;
                } else {
                    tmp--;
                }
            }
            if (tmp == blank.length) {
                value = '';
            }
        }
    }
    return value;
}

String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}


function changeLang(encoding, url_success) {
	if (url_success === undefined) {
		url_success = location.href;
	}
	if(url_success.endWith('/servlet/aeAction') || url_success.endWith('/servlet/qdbAction')|| url_success.endWith('/servlet/Dispatcher')){
		return;
	}
	
	url = wb_utils_invoke_disp_servlet('cmd', 'change_lang', 'module', 'JsonMod.user.UserModule', 'lang', encoding, 'url_success', url_success);
	location.href = url;
}

function doLogout() {
	if (confirm(getLabel('625'))) {
		var url = wb_utils_invoke_servlet('cmd', 'logout');
		self.location.href = url;
	}
}

function nav_go(url) {
	location.href = url;
}

function wbUtilsValidateNonZeroPositiveInteger2(fld, txtFldName){
	// This function check for integer range  1  to  2^31
	// Notes: NOT include zero
	var val = wbUtilsTrimString(fld.value)
	if(val == 0){
		Dialog.alert(txtFldName+wb_msg_usr_enter_positive_integer2 );
		fld.focus();
		return false;
	}

	if(val.search(/[^0-9]/) != -1){
		Dialog.alert(txtFldName+wb_msg_usr_enter_positive_integer2);
		fld.focus();
		return false;
	}

	if(val > Math.pow(2, 31)){
		Dialog.alert(txtFldName + wb_msg_usr_enter_smaller_number);
		fld.focus();
		return false;
	}
	return true;
}