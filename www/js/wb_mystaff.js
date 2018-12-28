// ------------------ wizBank MyStaff Management object ------------------- 
// Convention:
//   public functions : use "wbMyStaff" prefix 
//   private functions: use "_wbMyStaff" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

function wbMyStaff(){
	this.get_staff_review = wbMyStaffGetStaffReview 
	this.staff_review_nav_url = wbMyStaffStaffReviewNavURL
	this.staff_review_srh_res_url = wbMyStaffReviewSearchResultURL 
	this.staff_review_srh_go = wbMyStaffStaffReviewSrhGo
	this.search_staff_prep = wbMyStaffSearchStaffPrep
	this.search_staff_prep_url = wbMyStaffSearchStaffPrepURL
	this.popup_search_prep = wbMyStaffPopupSearchPrep
	this.popup_search_exec = wbMyStaffPopupSearchExec
	this.search_staff_exec = wbMyStaffSearchStaffExec
	this.staff_review = wbMyStaffReview
	this.search_staff = wbMyStaffSearchStaff
	this.get_staff_enrolled_cos = wbMyStaffGetStaffEnrolledCourse
	this.get_staff_lrn_plan = wbMyStaffGetStaffLearningPlan
	this.get_staff_lrn_history = wbMyStaffGetLearningHistory	
	this.get_staff_lrn_report = wbMyStaffGetLearningReport
}

function wbMyStaffGetStaffReview(s_usg_ent_id_lst){
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','my_staff_frame.xsl','s_usg_ent_id_lst',s_usg_ent_id_lst)

	window.location.href = url
}
function wbMyStaffStaffReviewSrhGo(s_usg_ent_id_lst,target){
	var url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule','cmd','search_ent_lst','s_usr_display_bil','','s_usr_id','','s_sort_by','','s_order_by','','s_usg_ent_id_lst',s_usg_ent_id_lst,'ent_id','','stylesheet','my_staff_usr_lst.xsl')

	if(target == null){
		target = window
	}
	//target.location.href = url
	if(document.all){
		//Fix IE Progress Bar non-stop Problem
		setTimeout('parent.' + target.name + '.location.href = "' + url+'"' ,10)
	}else{
		target.location.href = url
	}		
}

function wbMyStaffStaffReviewNavURL(){
	var url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule','cmd','get_staff_count','stylesheet','my_staff_nav.xsl')
	return url
}

function wbMyStaffReviewSearchResultURL (){
	var s_usg_ent_id_lst = getUrlParam('s_usg_ent_id_lst')
	if(s_usg_ent_id_lst == '' ){
		var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','my_staff_inst.xsl')
	}else{
		var url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule','cmd','search_ent_lst','s_usr_display_bil','','s_usr_id','','s_sort_by','','s_order_by','','s_usg_ent_id_lst',s_usg_ent_id_lst,'ent_id','','stylesheet','my_staff_usr_lst.xsl')
	}
	return url
}

function wbMyStaffSearchStaffPrep(target){
	var url = wbMyStaffSearchStaffPrepURL()
	if(target == null){
		target = window
	}
	if(document.all){
		//Fix IE Progress Bar non-stop Problem
		setTimeout('parent.' + target.name + '.location.href = wbMyStaffSearchStaffPrepURL()',10)
	}else{
		target.location.href = wbMyStaffSearchStaffPrepURL()
	}
	return;
}

function wbMyStaffPopupSearchPrep(){
	
	var screenLeft, screenTop, screenAvailWidth, screenAvailHeight, docWidth, docHeight, openScreenLeft, openScreenTop
	
	if(document.all){//Internet Explorer
		screenLeft = window.screenLeft
		screenTop = window.screenTop
		openScreenLeft = screenLeft + 130
		openScreenTop = screenTop + 130
	}else{//Netscape & Others
		screenLeft = window.screenX
		screenTop = window.screenY
		docWidth = window.outerWidth
		docHeight = window.outerHeight		
		openScreenLeft = screenLeft + 130
		openScreenTop = screenTop + 130
	}		
	
	var width, height
	width = '528'
	height = '367'
	
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ width
			+ ',height=' 				+ height
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'no';	
		
	if(document.all) {str_feature += ',top=' + openScreenTop + ',left=' + openScreenLeft;}	
	else {str_feature += ',screenX=' + openScreenLeft + ',screenY=' + openScreenTop;}
		
	url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule', 'cmd','get_meta','stylesheet','my_staff_simple_search.xsl','isExcludes','true')
	
	popup_mystaff_search = wbUtilsOpenWin(url,'popup_mystaff_search',false, str_feature);	
}


function wbMyStaffSearchStaff(frm, target, stylesheet) {
	if (frm.usr_group_lst.options[0]) {
		frm.ent_id.value = frm.usr_group_lst.options[0].value;
	}
	if (frm.s_grade_lst.options[0]) {
		frm.usr_grade.value = frm.s_grade_lst.options[0].value;
	}
    if (frm.s_usg_ent_id_lst) {
        // to make it such that the search result is marked as "Search Results"
        // instead of "All My Staff" nor "My Direct Reports"
        frm.s_usg_ent_id_lst.value = '';
	}
    frm.method = 'GET';
    frm.action = wb_utils_disp_servlet_url;
    frm.target = 'left';
    frm.submit();
}

function wbMyStaffPopupSearchExec(frm) {
	if (frm.usr_group_lst.options[0]) {
		frm.ent_id.value = frm.usr_group_lst.options[0].value;
	}
	if (frm.s_grade_lst.options[0]) {
		frm.usr_grade.value = frm.s_grade_lst.options[0].value;
	}
    if (frm.s_usg_ent_id_lst) {
        // to make it such that the search result is marked as "Search Results"
        // instead of "All My Staff" nor "My Direct Reports"
        frm.s_usg_ent_id_lst.value = '';
	}
	
	var url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule',
										   'cmd','search_ent_lst',
										   'ent_id', frm.ent_id.value,
										   's_usr_display_bil', frm.s_usr_display_bil.value,
										   's_usr_id', frm.s_usr_id.value,
										   's_order_by', frm.s_order_by.value,
										   's_sort_by', frm.s_sort_by.value,
										   's_usg_ent_id_lst', '',
										   'usr_grade', frm.usr_grade.value,
										   'stylesheet','usr_sim_search_result.xsl',
										   'search_in_mystaff','true'
										  );
	parent.location.href = url;			  
	return;
}

function wbMyStaffSearchStaffExec(frm) {
	if (frm.usr_group_lst.options[0]) {
		frm.ent_id.value = frm.usr_group_lst.options[0].value;
	}
	
	var url = wb_utils_invoke_disp_servlet('module','JsonMod.supervise.SuperviseModule',
										   'cmd','search_staff',
										   'group_id', frm.ent_id.value,
										   'search_staff_str', frm.s_usr_name.value,
										   'stylesheet','usr_sim_search_result.xsl',
										   'return_xml','true',
										   'isExcludes','true'
										  );
	parent.location.href = url;			  
	return;
}

function wbMyStaffSearchStaffPrepURL(){
	var url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule',
										   'cmd','get_meta',
										   'stylesheet','my_staff_search_form.xsl'
										  )
	return url;
}

function wbMyStaffReview(ent_id,target){
	if(target == null){
		target = parent.right
	}
	var url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule','cmd','get_usr','usr_ent_id',ent_id,'stylesheet','my_staff_search_res_detail.xsl','url_failure',wbMyStaffSearchStaffPrepURL())
	//target.location.href = url;
	if(document.all){
		//Fix IE Progress Bar non-stop Problem
		setTimeout('parent.' + target.name + '.location.href = "' + url+'"' ,10)
	}else{
		target.location.href = url
	}	
}


function wbMyStaffGetStaffEnrolledCourse(usr_ent_id){
	
	var url = wb_utils_invoke_disp_servlet('module','supervise.SuperviseModule',
										'env', 'wizb',
										'cmd', 'ae_lrn_plan',
										'usr_ent_id', usr_ent_id,
										'item_type', '',
										'stylesheet', 'lrn_course_view.xsl'
									  );

	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '500'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '10'
		+ ',screenY='				+ '10'
		+ ',status='				+ 'yes'
	if(document.all){
		str_feature += ',top='		+ '10'
		str_feature += ',left='		+ '10'
	}
	win = wbUtilsOpenWin(url,'my_staff_enrolled_course_' + usr_ent_id ,false,str_feature)
	url = null;
	return;
}

function wbMyStaffGetStaffLearningPlan(usr_ent_id){
	var url = wb_utils_invoke_disp_servlet('module', 'supervise.SuperviseModule',
										'env', 'wizb',
										'cmd', 'ae_lrn_soln',
										'usr_ent_id', usr_ent_id,
										'stylesheet', 'lrn_soln_ist_view_cos_popup.xsl',
										'targeted_item_apply_method_lst', '',
										'all_ind' ,'true'
									  );
	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '500'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '10'
		+ ',screenY='				+ '10'
		+ ',status='				+ 'yes'
	if(document.all){
		str_feature += ',top='		+ '10'
		str_feature += ',left='		+ '10'
	}
	win = wbUtilsOpenWin(url,'my_staff_lrn_plan_' + usr_ent_id,false,str_feature)
	url = null;
	return;
}


function wbMyStaffGetLearningHistory(ent_id){	
		year = "0";
		var url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'lrn_soln_hist', 'usr_ent_id', ent_id,  'stylesheet', 'lrn_history_view.xsl', 'p', '1', 'sort_col', 'att_timestamp', 'sort_order', 'desc');
		str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '500'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
		wbUtilsOpenWin(url,'my_staff_lrn_his_' + ent_id, false, str_feature);
}

function wbMyStaffGetLearningReport(){
	url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_tpl","rpt_type","LEARNER","stylesheet",'my_staff_rpt_lrn_srh.xsl');
	parent.location.href = url;
}