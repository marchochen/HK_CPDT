// ------------------ wizBank Report object ------------------- 
// Convention:
//   public functions : use "wbReport" prefix 
//   private functions: use "_wbReport" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbReport() {	
	// for instructor only
	this.get_ent_lst = wbReportGetEntList
	this.report_grp = wbReportGroup
	this.report_usr = wbReportUser
	this.grp_atm = wbReportGroupAtm
	this.open_cos_lrn_lst = wbOpenReportCosLrnLst
	this.open_cos_lrn_lst_search = wbOpenReportCosLrnLstSearch
	this.export_data_prep = wbExportDataPrep
	this.export_data = wbExportData
	this.get_usr_tk_rpt = wbGetUserTrackingReport

	// for learner only
	this.usr_atm = wbReportUserAtm
	this.usr_ind = wbReportUserInd	
	this.usr_detail = wbReportUserDetail
	
	// for learner and usage report
	this.cos_lrn_lst = wbReportCosLrnLst
	this.lrn_mod_lst = wbReportLrnModLst
	this.upd_cos_lrn = wbReportUpdCosLrn
	this.lrn_loc_lst = wbReportLrnLocLst
	this.usage_mod_lst = wbReportUsageModLst
	this.usage_mod_lst_in_date = wbReportUsageModLstInDate
	this.usage_mod_lst_prep = wbReportUsageModLstPrep
	this.init_report = wbReportInitReport	
	this.lrn_skillsoft_lst = wbReportSkillSoftLst
	this.lrn_skillsoft_rpt = wbReportSkillSoftLrnReport // for learner to view skillsoft report
	this.aicc_rpt = wbReportAICCReport
	this.netg_rpt = wbReportNETGReport
	//for instructor
	this.instr_open_cos_lrn_lst = wbInstrOpenReportCosLrnLst
	this.instr_usage_mod_lst_prep = wbInstrReportUsageModLstPrep
	this.instr_export_data_prep = wbInstrExportDataPrep
	this.instr_get_usr_tk_rpt = wbInstrGetUserTrackingReport
	this.instr_usage_mod_lst_in_date = wbInstrReportUsageModLstInDate
	
	this.sort_list = wbReportSortList
}

function wbReportSortList(sort_col, sort_order) {
	wb_utils_nav_get_urlparam('sort_col', sort_col, 'sort_order', sort_order);
}

/* private functions */
function _wbReportOpenWin(url,win_name,winWidth,winHeight) {
		if(winWidth == null || winWidth == '') {winWidth ='780';}
		if(winHeight == null || winHeight == '') {winHeight ='500';}
		
		str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ winWidth
			+ ',height=' 				+ winHeight
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

		wbUtilsOpenWin(url, win_name, false, str_feature);
}

function _wbReportGetQueNumber(frm) {
		var i, n, ele, str
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.checked) {
				if (ele.value !="")
					str = str + ele.value + "~"
			}
		}
		
		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		return str;	
}

/* public functions */
function wbReportGetEntList(mod_id,course_id,id,nm,cur_page,pagesize) {

		if ( cur_page == null || cur_page == '')
			cur_page = 1
		if ( pagesize == null || pagesize == '' )
			pagesize = 10
			
		url = wb_utils_invoke_servlet('cmd','get_tst_enrol_lst','stylesheet','ist_tst_enrol_lst.xsl','ent_id_parent',id,'mod_id',mod_id,'course_id',course_id,'cur_page',cur_page,'pagesize',pagesize)
		if ( nm != '' )
			wb_utils_set_cookie('grp_name',nm)
			
		wb_utils_set_cookie('grp_id',id)
		window.location.href = url ;	

}

function wbReportGroup(mod_id,frm,lang){


		rpt_lst = _wbReportGetQueNumber(frm)
		if (rpt_lst == "") {
			alert(eval('wb_msg_' + lang + '_select_rpt'))
			return
		}
		
		if ( rpt_lst.indexOf('~') == -1 ){
		
			n = frm.elements.length;
			for (i = 0; i < n; i++) {
				ele = frm.elements[i]
				if (ele.value == rpt_lst)
					id = ele.name
			}
		
			if (id.indexOf('grp') == -1 )
				this.report_usr(ele.value,mod_id)
			else{
				url = wb_utils_invoke_servlet('cmd','get_rpt','que_id_lst',0,'rpt_group_lst',rpt_lst,'mod_id',mod_id,'attempt_nbr',1,'url_failure',gen_get_cookie('url_failure'),'stylesheet','tst_rpt_usg_header.xsl')
				window.location.href = url
			}			
		}else{
			url = wb_utils_invoke_servlet('cmd','get_rpt','que_id_lst',0,'rpt_group_lst',rpt_lst,'mod_id',mod_id,'attempt_nbr',1,'url_failure',gen_get_cookie('url_failure'),'stylesheet','tst_rpt_usg_header.xsl')
			window.location.href = url
		}

}


function wbReportUser(usr_id,mod_id,tkh_id,attempt_nbr){

		if (attempt_nbr == null) attempt_nbr = 1
		url = wb_utils_invoke_servlet('cmd','get_rpt_usr','stylesheet','tst_rpt_usr_header.xsl','que_id_lst',0,'rpt_usr_id',usr_id,'mod_id',mod_id,'url_failure',self.location.href,'attempt_nbr',attempt_nbr,'tkh_id', tkh_id)
		
		_wbReportOpenWin(url, 'report_2')
		
}		


function wbReportGroupAtm(mod_id,rpt_usg_id,attempt_nbr){
		url = wb_utils_invoke_servlet('cmd','get_rpt','que_id_lst',0,'mod_id',mod_id,'rpt_group_lst',rpt_usg_id,'stylesheet','tst_rpt_usg_header.xsl','attempt_nbr',attempt_nbr)
		window.location.href = url
}


function wbReportUserAtm(mod_id,rpt_usr_id,attempt_nbr,role,tkh_id, mod_type){
		if (tkh_id == null) { tkh_id = ''}
		var cmd = 'get_rpt_usr';
		if ( role != 'student' )
			stylesheet = 'tst_rpt_usr_header.xsl'
		else {
			stylesheet = 'lrn_tst_rpt_usr_header.xsl'
			if(mod_type == 'TST' || mod_type == 'DXT'){ 
                cmd = 'get_rpt_usr_test_html'; 
            }
        }
        url = wb_utils_invoke_servlet('cmd',cmd,'rpt_usr_id',rpt_usr_id,'mod_id',mod_id,'stylesheet',stylesheet,'que_id_lst',0,'attempt_nbr',attempt_nbr, 'tkh_id', tkh_id)
		//url = wb_utils_invoke_servlet('cmd','get_rpt_usr','rpt_usr_id',rpt_usr_id,'mod_id',mod_id,'stylesheet',stylesheet,'que_id_lst',0,'attempt_nbr',attempt_nbr, 'tkh_id', tkh_id)
		window.location.href = url
}

function wbReportUserInd(id,mod_id,rpt_usr_id,attempt_nbr,role,tkh_id, mod_type){
        var cmd = 'get_rpt_usr';
		if ( role != 'student' ){
			stylesheet = 'tst_rpt_usr_detail.xsl'
		}
		else {
			stylesheet = 'lrn_tst_rpt_usr_detail.xsl'
			if(mod_type == 'TST' || mod_type == 'DXT'){ 
                cmd = 'get_rpt_usr_test';  
            }
		}  

		url = wb_utils_invoke_servlet('cmd', cmd,'attempt_nbr',attempt_nbr,'stylesheet',stylesheet,'mod_id',mod_id,'rpt_usr_id',rpt_usr_id,'que_id_lst',id, 'tkh_id', tkh_id)
		window.location.href = url
}


function wbReportUserDetail(list,mod_id,rpt_usr_id,attempt_nbr,role,tkh_id){
		if (tkh_id == null) {tkh_id = ''}
		if ( role != 'student' )
				stylesheet = 'tst_rpt_usr_detail.xsl'
			else 
				stylesheet = 'lrn_tst_rpt_usr_detail.xsl'

		url = wb_utils_invoke_servlet('cmd','get_rpt_usr','attempt_nbr',attempt_nbr,'stylesheet',stylesheet,'mod_id',mod_id,'rpt_usr_id',rpt_usr_id,'que_id_lst',list, 'tkh_id', tkh_id)
		window.location.href = url
		
}

function wbReportCosLrnLst(cos_id,grp_id,cur_page,pagesize){

		
		if (cos_id == null){
			cos_id = wb_utils_get_cookie('course_id');
		}
		if ( cur_page == null || cur_page == '' )
			cur_page = 1
		if ( pagesize == null || pagesize == '' )
			pagesize = 10
				
		if ( grp_id == null ){
			url = wb_utils_invoke_servlet('cmd','get_lrn_cos_rpt','cur_grp_id','','cur_page',cur_page,'pagesize',pagesize,'stylesheet','ist_lrn_rpt_cos.xsl','course_id',cos_id)
		}else{
			
			url = wb_utils_invoke_servlet('cmd','get_lrn_cos_rpt','cur_grp_id',grp_id,'cur_page',cur_page,'pagesize',pagesize,'stylesheet','ist_lrn_rpt_cos.xsl','course_id',cos_id)
			
		}
		window.location.href = url
		
		
}

function wbGetUserTrackingReport(frm, lang){
	if (frm.date_range_ind[1].checked == true){
		if((frm.start_datetime_yy.value!=""&&frm.start_datetime_yy.value!=null)||
		   (frm.start_datetime_mm.value!=""&&frm.start_datetime_mm.value!=null)||
		   (frm.start_datetime_dd.value!=""&&frm.start_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate('frmXml.start_datetime',eval('wb_msg_' + lang + '_start_date'))){
				return false;	
			}
			frm.start_datetime.value = frm.start_datetime_yy.value + "-" + frm.start_datetime_mm.value + "-" +frm.start_datetime_dd.value + " 00:00:00.000"//+ " " + 00 + ":" + 00 + ":00"
		}else{
			frm.start_datetime.value = "";
		}
		if((frm.end_datetime_yy.value!=""&&frm.end_datetime_yy.value!=null)||
		   (frm.end_datetime_mm.value!=""&&frm.end_datetime_mm.value!=null)||
		   (frm.end_datetime_dd.value!=""&&frm.end_datetime_dd.value!=null)){
			if(!wbUtilsValidateDate('frmXml.end_datetime',eval('wb_msg_' + lang + '_end_date'))){
				return false;	
			}
			frm.end_datetime.value = frm.end_datetime_yy.value + "-" + frm.end_datetime_mm.value + "-" +frm.end_datetime_dd.value + " " + 23 + ":" + 59 + ":59"
		}else{
			frm.end_datetime.value = "";
		}

		if (frm.start_datetime.value=="" && frm.end_datetime.value==""){
			alert(eval('wb_msg_' + lang + '_select_date_range'));
			return false;
		}

		if (frm.start_datetime.value!="" && frm.end_datetime.value!=""){			
			if (!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name,
				start_obj : 'start_datetime',
				end_obj : 'end_datetime', 
				start_nm : eval('wb_msg_' + lang + '_start_date'), 
				end_nm : eval('wb_msg_' + lang + '_end_date')
				})){
				return false;
			}
		}

		n = frm.date_range_subtype.length;
		for (i=0; i<n; i++){
			if (frm.date_range_subtype[i].checked == true)
				frm.rpt_date_type.value = frm.date_range_subtype[i].value
		}
	
	}else {
		frm.rpt_date_type.value='NOT_SPECIFIED';
	}

		frm.action = wb_utils_servlet_url
		frm.cmd.value = "get_user_tk_rpt"
		frm.stylesheet.value = 'ist_lrn_rpt_cos.xsl'
		frm.submit();
		
}

function wbOpenReportCosLrnLstSearch(frm,itm_id){
	var url = self.location.href;
	url = setUrlParam("rpt_search_full_name",frm.rpt_search_full_name.value, url);
//	wb_utils_set_cookie("rpt_search_full_name",frm.rpt_search_full_name.value);
	self.location.href = url;
	
}

function wbOpenReportCosLrnLst(itm_id){
		if (itm_id == null || itm_id ==0){
			itm_id = wb_utils_get_cookie('itm_id');
		}
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_content_info','stylesheet','ist_lrn_rpt_cos_prep.xsl','itm_id',itm_id)
		window.location.href=url
}

function wbExportDataPrep(itm_id){
		if (itm_id == null || itm_id ==0){
			itm_id = wb_utils_get_cookie('itm_id');
		}

		url = wb_utils_invoke_ae_servlet('cmd','ae_get_content_info','stylesheet','ist_export_data_prep.xsl','itm_id',itm_id)
		window.location.href=url
}

function wbExportData(frm, lang){
		if (frm.date_range_ind[1].checked == true){
			if((frm.start_datetime_yy.value!=""&&frm.start_datetime_yy.value!=null)||
			   (frm.start_datetime_mm.value!=""&&frm.start_datetime_mm.value!=null)||
			   (frm.start_datetime_dd.value!=""&&frm.start_datetime_dd.value!=null)){
				if(!wbUtilsValidateDate('frmXml.start_datetime',eval('wb_msg_' + lang + '_start_date'))){
					return false;	
				}
				frm.start_datetime.value = frm.start_datetime_yy.value + "-" + frm.start_datetime_mm.value + "-" +frm.start_datetime_dd.value + " 00:00:00.000"//+ " " + 00 + ":" + 00 + ":00"
			}else{
				frm.start_datetime.value = "";
			}
			if((frm.end_datetime_yy.value!=""&&frm.end_datetime_yy.value!=null)||
			   (frm.end_datetime_mm.value!=""&&frm.end_datetime_mm.value!=null)||
			   (frm.end_datetime_dd.value!=""&&frm.end_datetime_dd.value!=null)){
				if(!wbUtilsValidateDate('frmXml.end_datetime',eval('wb_msg_' + lang + '_end_date'))){
					return false;	
				}
				frm.end_datetime.value = frm.end_datetime_yy.value + "-" + frm.end_datetime_mm.value + "-" +frm.end_datetime_dd.value + " " + 23 + ":" + 59 + ":59"
			}else{
				frm.end_datetime.value = "";
			}

			if (frm.start_datetime.value=="" && frm.end_datetime.value==""){
				alert(eval('wb_msg_' + lang + '_select_date_range'));
				return false;
			}

			if (frm.start_datetime.value!="" && frm.end_datetime.value!=""){			
				if (!wb_utils_validate_date_compare({
					frm : 'document.' + frm.name,
					start_obj : 'start_datetime',
					end_obj : 'end_datetime', 
					start_nm : eval('wb_msg_' + lang + '_start_date'), 
					end_nm : eval('wb_msg_' + lang + '_end_date')
					})){
					return false;
				}
			}
		}else {
			frm.start_datetime.value = "";
			frm.end_datetime.value = "";
		}
		frm.all_mod_ind.value = frm.lab_all_mod.value;
		frm.all_enrolled_lrn_ind.value = frm.lab_all_enrolled_lrn.value;
	
		frm.rpt_type.value = 'MOD_EVN_OF_COS';
		var dateVar = new Date();
		frm.window_name.value = 'rpt_win' + dateVar.getTime();
		
		var newurl = wb_utils_invoke_servlet('cmd', 'get_prof', 'rpt_type', 'MOD_EVN_OF_COS', 'stylesheet', 'mod_evn_export_progress.xsl', 'window_name', frm.window_name.value, 'download', '4');
		wb_utils_open_win(newurl,frm.window_name.value+'export', 450, 150);
}

function wbReportLrnModLst(usr_ent_id, cos_id, tkh_id){
		url = wb_utils_invoke_servlet('cmd','get_lrn_mod_rpt','stylesheet','ist_lrn_rpt_mod.xsl','course_id',cos_id,'student_id',usr_ent_id, 'tkh_id', tkh_id)
		_wbReportOpenWin(url,'report')
}

function _wbReportValidateFrd(frm,lang){
	if ( !gen_validate_float(frm.score, eval('wb_msg_' + lang + '_rpt_score'),lang)){
		frm.score.focus()
		return false
	}else{
		return true
	}
}

function wbReportUpdCosLrn(frm,lang) {
   	if (_wbReportValidateFrd(frm, lang)){
		frm.action = wb_utils_servlet_url
	   	frm.url_success.value = wb_utils_get_cookie("url_prev");
		frm.submit()
	}	
}

function wbReportLrnLocLst(usr_ent_id, mod_id, stylesheet, isPopup,tkh_id){
	
	if(isPopup == null || isPopup == '') {isPopup = 'false';}	
	if (stylesheet == null || stylesheet == '') {stylesheet = 'ist_lrn_rpt_pack.xsl';}
	
	url = wbReportLrnLocLstUrl(usr_ent_id, mod_id, stylesheet,tkh_id)
	
	if (isPopup == 'true') {_wbReportOpenWin(url,'report');}
	else {window.location.href = url;}
}

function wbReportLrnLocLstUrl(usr_ent_id, mod_id, stylesheet,tkh_id){	
	url = wb_utils_invoke_servlet('cmd','get_lrn_pack_rpt','stylesheet',stylesheet,'lesson_id',mod_id,'student_id',usr_ent_id,'tkh_id', tkh_id)	
	return url;	
}


function wbReportUsageModLst(cos_id,identifier,nm,rpt_type){
		if (rpt_type == null || rpt_type == '') {
			rpt_type = getUrlParam("rpt_type");
		}
		if (rpt_type == null || rpt_type == '') {
			rpt_type = 'APP'
		}
		
		if (cos_id == null){
			cos_id = wb_utils_get_cookie('course_id');
		}
		
		
		if ( identifier == null ){
			url = wb_utils_invoke_servlet('cmd','get_mod_rpt','stylesheet','ist_rpt_mod.xsl','course_id',cos_id,'rpt_type',rpt_type)
		}else{
			alert(nm)
			url = wb_utils_invoke_servlet('cmd','get_mod_rpt','location',identifier,'stylesheet','ist_rpt_mod.xsl','course_id',cos_id, 'rpt_type', rpt_type)
		}
		window.location.href = url
}

function wbReportUsageModLstInDate(frm, lang, cos_id,identifier,nm,rpt_type){
		if(frm != null && lang != null) {
			if (frm.date_range_ind[1].checked == true){
				if((frm.start_datetime_yy.value!=""&&frm.start_datetime_yy.value!=null)||
				   (frm.start_datetime_mm.value!=""&&frm.start_datetime_mm.value!=null)||
				   (frm.start_datetime_dd.value!=""&&frm.start_datetime_dd.value!=null)){
					if(!wbUtilsValidateDate('frmXml.start_datetime',eval('wb_msg_' + lang + '_start_date'))){
						return false;	
					}
					frm.start_datetime.value = frm.start_datetime_yy.value + "-" + frm.start_datetime_mm.value + "-" +frm.start_datetime_dd.value + " 00:00:00.000"//+ " " + 00 + ":" + 00 + ":00"
					start_datetime = frm.start_datetime.value;
				}else{
					frm.start_datetime.value = "";
					start_datetime = frm.start_datetime.value;
				}
				if((frm.end_datetime_yy.value!=""&&frm.end_datetime_yy.value!=null)||
				   (frm.end_datetime_mm.value!=""&&frm.end_datetime_mm.value!=null)||
				   (frm.end_datetime_dd.value!=""&&frm.end_datetime_dd.value!=null)){
					if(!wbUtilsValidateDate('frmXml.end_datetime',eval('wb_msg_' + lang + '_end_date'))){
						return false;	
					}
					frm.end_datetime.value = frm.end_datetime_yy.value + "-" + frm.end_datetime_mm.value + "-" +frm.end_datetime_dd.value + " " + 23 + ":" + 59 + ":59"
					end_datetime = frm.end_datetime.value;
				}else{
					frm.end_datetime.value = "";
					end_datetime = frm.end_datetime.value;
				}

				if (frm.start_datetime.value=="" && frm.end_datetime.value==""){
					alert(eval('wb_msg_' + lang + '_select_date_range'));
					return false;
				}

				if (frm.start_datetime.value!="" && frm.end_datetime.value!=""){			
					if (!wb_utils_validate_date_compare({
						frm : 'document.' + frm.name,
						start_obj : 'start_datetime',
						end_obj : 'end_datetime', 
						start_nm : eval('wb_msg_' + lang + '_start_date'), 
						end_nm : eval('wb_msg_' + lang + '_end_date')
						})){
						return false;
					}
				}
			}else {
				frm.start_datetime.value = "";
				start_datetime = frm.start_datetime.value;
				frm.end_datetime.value = "";
				end_datetime = frm.end_datetime.value;
				frm.rpt_date_type.value='NOT_SPECIFIED';
			}
		} else {
			start_datetime = "";
			end_datetime = "";
		}

		if (rpt_type == null || rpt_type == '') {
			rpt_type = getUrlParam("rpt_type");
		}
		if (rpt_type == null || rpt_type == '') {
			rpt_type = 'APP'
		}
		
		if (cos_id == null){
			cos_id = wb_utils_get_cookie('course_id');
		}
		
		url = wb_utils_invoke_servlet('cmd','get_mod_rpt_in_date','stylesheet','ist_rpt_mod_date.xsl','course_id',cos_id,'rpt_type',rpt_type,'start_datetime', start_datetime,'end_datetime', end_datetime)
		window.location.href = url
}

function wbReportUsageModLstPrep(itm_id){

		if (itm_id == null || itm_id==0){
			itm_id = wb_utils_get_cookie('itm_id');
		}
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_content_info','stylesheet','ist_rpt_mod_prep.xsl','itm_id',itm_id)
		window.location.href=url
}

function wbReportInitReport(cos_id, cos_title){
		wb_utils_set_cookie('course_id',cos_id)
		wb_utils_set_cookie('course_title',cos_title)
}

function wbReportSkillSoftLst(cos_id,mod_id,usr_ent_id,tkh_id){

	url = wb_utils_invoke_servlet('cmd','get_lrn_skillsoft_rpt','course_id',cos_id,'lesson_id',mod_id,'student_id',usr_ent_id,'stylesheet','ist_lrn_rpt_skilsoft.xsl','tkh_id', tkh_id)
	_wbReportOpenWin(url,'report_2')

}

function wbReportSkillSoftLrnReport(cos_id,mod_id,usr_ent_id,tkh_id){
	if (tkh_id == null) {
		tkh_id = ''
	}
	url = wb_utils_invoke_servlet('cmd','get_lrn_skillsoft_rpt','course_id',cos_id,'lesson_id',mod_id,'student_id',usr_ent_id,'stylesheet','lrn_rpt_skillsoft.xsl', 'tkh_id', tkh_id)
	_wbReportOpenWin(url,'report_2','470','300')
}

function wbReportAICCReport(course_id,lesson_id,student_id,openWin,tkh_id,isExcludes){
	if (tkh_id == null) {	tkh_id = ''}
	url = wb_utils_invoke_servlet("cmd","get_lrn_aicc_rpt","course_id",course_id,"lesson_id",lesson_id,"student_id",student_id,"stylesheet",'lrn_rpt.xsl', 'tkh_id', tkh_id);
	
	if(isExcludes){
		url += "&isExcludes=true";
	}
	
	if(openWin)
		window.location.href = url;
	else
		_wbReportOpenWin(url,'report_2_' + student_id + '_' + tkh_id);
}

function wbReportNETGReport(course_id,lesson_id,student_id,openWin,tkh_id){
	if (tkh_id == null) {	tkh_id = ''}
	url = wb_utils_invoke_servlet("cmd","get_lrn_aicc_rpt","course_id",course_id,"lesson_id",lesson_id,"student_id",student_id,"stylesheet",'lrn_rpt_netg.xsl', 'tkh_id', tkh_id);
	if(openWin)
		_wbReportOpenWin(url,'report_2_' + student_id + '_' + tkh_id);
	else
		window.location.href = url;
}
//for instructor
function wbInstrOpenReportCosLrnLst(itm_id){
		if (itm_id == null || itm_id ==0){
			itm_id=wb_utils_get_cookie('itm_id');
		}
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_content_info','stylesheet','instr_ist_lrn_rpt_cos_prep.xsl','itm_id',itm_id);
		window.location.href=url
}
function wbInstrReportUsageModLstPrep(itm_id){

		if (itm_id == null || itm_id==0){
			itm_id = wb_utils_get_cookie('itm_id');
		}
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_content_info','stylesheet','instr_ist_rpt_mod_prep.xsl','itm_id',itm_id)
		window.location.href=url
}
function wbInstrExportDataPrep(itm_id){
		if (itm_id == null || itm_id ==0){
			itm_id = wb_utils_get_cookie('itm_id');
		}
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_content_info','stylesheet','instr_ist_export_data_prep.xsl','itm_id',itm_id)
		window.location.href=url
}
function wbInstrGetUserTrackingReport(frm, lang){
	if (frm.date_range_ind[1].checked == true){
		if((frm.start_datetime_yy.value!=""&&frm.start_datetime_yy.value!=null)||
		   (frm.start_datetime_mm.value!=""&&frm.start_datetime_mm.value!=null)||
		   (frm.start_datetime_dd.value!=""&&frm.start_datetime_dd.value!=null)){
			if(!gen_validate_date('frmXml.start_datetime',eval('wb_msg_' + lang + '_start_date'),lang)){
				return false;	
			}
			frm.start_datetime.value = frm.start_datetime_yy.value + "-" + frm.start_datetime_mm.value + "-" +frm.start_datetime_dd.value + " 00:00:00.000"//+ " " + 00 + ":" + 00 + ":00"
		}else{
			frm.start_datetime.value = "";
		}
		if((frm.end_datetime_yy.value!=""&&frm.end_datetime_yy.value!=null)||
		   (frm.end_datetime_mm.value!=""&&frm.end_datetime_mm.value!=null)||
		   (frm.end_datetime_dd.value!=""&&frm.end_datetime_dd.value!=null)){
			if(!gen_validate_date('frmXml.end_datetime',eval('wb_msg_' + lang + '_end_date'),lang)){
				return false;	
			}
			frm.end_datetime.value = frm.end_datetime_yy.value + "-" + frm.end_datetime_mm.value + "-" +frm.end_datetime_dd.value + " " + 23 + ":" + 59 + ":59"
		}else{
			frm.end_datetime.value = "";
		}

		if (frm.start_datetime.value=="" && frm.end_datetime.value==""){
			alert(eval('wb_msg_' + lang + '_select_date_range'));
			return false;
		}

		if (frm.start_datetime.value!="" && frm.end_datetime.value!=""){			
			if (!gen_validate_date_compare_larger(frm,'start_datetime','end_datetime', eval('wb_msg_' + lang + '_start_date'), eval('wb_msg_' + lang + '_end_date'), lang)){
				return false;
			}
		}

		n = frm.date_range_subtype.length;
		for (i=0; i<n; i++){
			if (frm.date_range_subtype[i].checked == true)
				frm.rpt_date_type.value = frm.date_range_subtype[i].value
		}
	
	}else {
		frm.rpt_date_type.value='NOT_SPECIFIED';
	}

		frm.action = wb_utils_servlet_url
		frm.cmd.value = "get_user_tk_rpt"
		frm.stylesheet.value = 'instr_ist_lrn_rpt_cos.xsl'
		frm.submit();
}
function wbInstrReportUsageModLstInDate(frm, lang, cos_id,identifier,nm,rpt_type){
		if(frm != null && lang != null) {
			if (frm.date_range_ind[1].checked == true){
				if((frm.start_datetime_yy.value!=""&&frm.start_datetime_yy.value!=null)||
				   (frm.start_datetime_mm.value!=""&&frm.start_datetime_mm.value!=null)||
				   (frm.start_datetime_dd.value!=""&&frm.start_datetime_dd.value!=null)){
					if(!gen_validate_date('frmXml.start_datetime',eval('wb_msg_' + lang + '_start_date'),lang)){
						return false;	
					}
					frm.start_datetime.value = frm.start_datetime_yy.value + "-" + frm.start_datetime_mm.value + "-" +frm.start_datetime_dd.value + " 00:00:00.000"//+ " " + 00 + ":" + 00 + ":00"
					start_datetime = frm.start_datetime.value;
				}else{
					frm.start_datetime.value = "";
					start_datetime = frm.start_datetime.value;
				}
				if((frm.end_datetime_yy.value!=""&&frm.end_datetime_yy.value!=null)||
				   (frm.end_datetime_mm.value!=""&&frm.end_datetime_mm.value!=null)||
				   (frm.end_datetime_dd.value!=""&&frm.end_datetime_dd.value!=null)){
					if(!gen_validate_date('frmXml.end_datetime',eval('wb_msg_' + lang + '_end_date'),lang)){
						return false;	
					}
					frm.end_datetime.value = frm.end_datetime_yy.value + "-" + frm.end_datetime_mm.value + "-" +frm.end_datetime_dd.value + " " + 23 + ":" + 59 + ":59"
					end_datetime = frm.end_datetime.value;
				}else{
					frm.end_datetime.value = "";
					end_datetime = frm.end_datetime.value;
				}

				if (frm.start_datetime.value=="" && frm.end_datetime.value==""){
					alert(eval('wb_msg_' + lang + '_select_date_range'));
					return false;
				}

				if (frm.start_datetime.value!="" && frm.end_datetime.value!=""){			
					if (!gen_validate_date_compare_larger(frm,'start_datetime','end_datetime', eval('wb_msg_' + lang + '_start_date'), eval('wb_msg_' + lang + '_end_date'), lang)){
						return false;
					}
				}
			}else {
				frm.start_datetime.value = "";
				start_datetime = frm.start_datetime.value;
				frm.end_datetime.value = "";
				end_datetime = frm.end_datetime.value;
				frm.rpt_date_type.value='NOT_SPECIFIED';
			}
		} else {
			start_datetime = "";
			end_datetime = "";
		}

		if (rpt_type == null || rpt_type == '') {
			rpt_type = getUrlParam("rpt_type");
		}
		if (rpt_type == null || rpt_type == '') {
			rpt_type = 'APP'
		}
		
		if (cos_id == null){
			cos_id = wb_utils_get_cookie('course_id');
		}
		
		url = wb_utils_invoke_servlet('cmd','get_mod_rpt_in_date','stylesheet','instr_ist_rpt_mod_date.xsl','course_id',cos_id,'rpt_type',rpt_type,'start_datetime', start_datetime,'end_datetime', end_datetime)
		window.location.href = url
}