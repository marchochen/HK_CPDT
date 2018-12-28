// ------------------ WizBank Global Management Report Object ---------------------
// Convention:
//   public functions : use "wbGlbMgtRpt" prefix
//   private functions: use "_wbGlbMgtRpt" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------
//Delimiter Constant
d_f = ":_:_:"; //field
d_v = "~"; //value

function Spec(){
	this.name = "";
	this.value = "";	
}

/* constructor */
function wbGlbMgtRpt(){
	this.get_rpt_lst = wbGlbMgtRptGetReportList;
	
	this.ins_rpt_prep = wbGlbMgtRptInsertReportPrep;
	
	this.upd_rpt_prep = wbGlbMgtRptUpdateReportPrep;
	this.upd_rpt_exec = wbGlbMgtRptUpdateReportExec;	
	
	this.get_rpt = wbGlbMgtRptGetReport;	
	this.get_rpt_adv = wbGlbMgtRptGetReportAdvanced;

	this.ins_rpt_prep_popup = wbGlbMgtRptInsertReportPrepPopup;
	this.ins_rpt_exec_popup = wbGlbMgtRptInsertReportExecPopup;		
	
	this.del_rpt = wbGlbMgtRptDeleteReport;	
	
	this.dl_rpt = wbGlbMgtRptDownloadReport;	
	this.rslt_dl_rpt_adv = wbGlbMgtRptResultDownloadReportAdvanced;	

	this.ins_rpt_res_prep_popup = wbGlbMgtRptInsertReportResultPrepPopup;
}

function wbGlbMgtRptInsertReportResultPrepPopup(frm,rsp_id,rte_id,usr_ent_id,rpt_type,lang){
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

function wbGlbMgtRptGetReportList(){
	var url = _wbGlbMgtRptGetReportListURL()
	window.location.href = url;
}


function wbGlbMgtRptInsertReportPrep(rte_id,rpt_type,stylesheet){
	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_tpl","rte_id",rte_id,"rpt_type",rpt_type,"stylesheet",stylesheet);
	window.location.href = url;
}


function wbGlbMgtRptUpdateReportPrep(rsp_id,rte_id,rpt_type,stylesheet){
	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","get_rpt_tpl","rsp_id",rsp_id,"rte_id",rte_id,"rpt_type",rpt_type,"stylesheet",stylesheet);
	window.location.href = url;
}

function wbGlbMgtRptGetReport(rsp_id,rpt_type,title,stylesheet){
	var cmd = 'get_rpt'
	var url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd",cmd,"rsp_id",rsp_id,"rpt_type",rpt_type,"stylesheet",stylesheet);
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


function wbGlbMgtRptGetReportAdvanced(frm,rpt_type,rte_id,stylesheet,current,lang){
	if(lang==null || lang==""){lang="en"}
	var download = "0";
	var spec = new Spec();
	if(_wbGlbMgtRptGetFormSpec(frm,lang,spec) == false){
		return;
	}	
	var rpt_win = wbUtilsOpenWin("../htm/loading.htm?lang=" + lang, "rpt_win");	
	frm.module.value ="report.ReportModule";
	frm.cmd.value = "get_rpt";
	frm.rpt_type.value = rpt_type;
	if(frm.rsp_title)
		frm.rpt_name.value = frm.rsp_title.value;
	frm.stylesheet.value = stylesheet;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";	
	frm.target = "rpt_win"
	frm.submit();	
}

function wbGlbMgtRptInsertReportPrepPopup(frm,rte_id,rpt_type,lang){
	if(lang==null || lang==""){lang="en"}
	var spec = new Spec();
	if(_wbGlbMgtRptGetFormSpec(frm,lang,spec) == false){
		return;
	}	
	//datetime_restriction
	frm.cmd.value = "echo_spec_param";
	frm.module.value = "report.ReportModule";
	frm.download.value = "0";	
	frm.rte_id.value = rte_id;
	frm.rpt_type.value = rpt_type;
	frm.rpt_type_lst.value = rpt_type;
	frm.stylesheet.value = 'rpt_glb_enrol_srh_popup.xsl';
	frm.url_failure.value = parent.location.href;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.target = "saveWin";
	frm.submit();
}

function wbGlbMgtRptInsertReportExecPopup(frm,lang){
	if(!gen_validate_empty_field(frm.rsp_title,eval("wb_msg_"+lang+"_rpt_name"),lang))
		return;
	frm.module.value = "report.ReportModule";
	frm.cmd.value = "ins_rpt_spec";
	frm.download.value = "0";
	frm.url_success.value = "Javascript:window.close()";
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}


function wbGlbMgtRptUpdateReportExec(frm,rte_id,rsp_id,rpt_type,lang){
	if(lang==null || lang==""){lang="en"}
	var spec = new Spec()
	if(_wbGlbMgtRptGetFormSpec(frm,lang,spec) == false){
		return;
	}
	frm.module.value = "report.ReportModule";
	frm.cmd.value = "upd_rpt_spec";
	frm.download.value =  "0";
	frm.rsp_id.value = rsp_id;
	frm.rte_id.value = rte_id;
	frm.rpt_type.value = rpt_type;
	frm.rpt_type_lst.value = rpt_type;
	frm.url_success.value = _wbGlbMgtRptGetReportListURL()
	frm.url_failure.value = parent.location.href;
	frm.spec_name.value = spec.name;
	frm.spec_value.value = spec.value;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.target = "_parent";
	frm.submit();
}

function wbGlbMgtRptDeleteReport(rsp_id,lang,rpt_type){
	if(confirm(eval("wb_msg_"+lang+"_del_it"))){
		url_prev = _wbGlbMgtRptGetReportListURL()
		url = wb_utils_invoke_disp_servlet("module","report.ReportModule","cmd","del_rpt_spec","rsp_id",rsp_id,"url_success",url_prev);
		parent.location.href = url;
	}
}

function wbGlbMgtRptResultDownloadReportAdvanced(stylesheet, rpt_name){
	url = self.location.href;
	
	if (getUrl('download')) {
		url = setUrlParam('download','3',url);
	}else{
		url = url + "&download=3";
	}
	
	url = url + "&rpt_name=" + rpt_name;
		
	if (getUrl('stylesheet')) {
		url = setUrlParam('stylesheet',stylesheet,url)
	}
	window.location.href = url;
}

function wbGlbMgtRptDownloadReport(rsp_id,rpt_type,stylesheet,rpt_name){
	var url = wb_utils_invoke_disp_servlet('module','report.ReportModule','cmd','get_rpt','rsp_id',rsp_id,'rpt_type',rpt_type,'download',3,'stylesheet',stylesheet);
    if (rpt_name != null && rpt_name != '') {
        url = url + "&rpt_name="+rpt_name;
    }
	window.location.href = url;
}

/* Public Funtions */
function _wbGlbMgtRptGetReportListURL(){
	var url = wb_utils_invoke_disp_servlet('module','report.ReportModule','cmd','get_rpt_lst','stylesheet','rpt_glb_all.xsl','show_public','Y')
	return url;
}

function _wbGlbMgtRptGetFormSpec(frm,lang,spec){
	if(_wbGlbMgtGetDateType(frm,lang,spec) == false){return false;}	
	
	if(frm.rdo_date_type && frm.rdo_date_type.length){
		if(frm.rdo_date_type[1].checked == true){
			if(frm.start_datetime_yy.value == "" ){
				alert(eval('wb_msg_'+ lang +'_enrol_date'))
				frm.start_datetime_yy.focus()
				return false;
			}	
			if(frm.start_datetime_mm.value == "" ){
				alert(eval('wb_msg_'+ lang +'_enrol_date'))
				frm.start_datetime_mm.focus()
				return false;
			}	
			if(frm.start_datetime_dd.value == "" ){
				alert(eval('wb_msg_'+ lang +'_enrol_date'))
				frm.start_datetime_dd.focus()
				return false;
			}									
			if(frm.end_datetime_yy.value == "" ){
				alert(eval('wb_msg_'+ lang +'_enrol_date'))
				frm.end_datetime_yy.focus()
				return false;
			}	
			if(frm.end_datetime_mm.value == "" ){
				alert(eval('wb_msg_'+ lang +'_enrol_date'))
				frm.end_datetime_mm.focus()
				return false;
			}		
			if(frm.end_datetime_dd.value == "" ){
				alert(eval('wb_msg_'+ lang +'_enrol_date'))
				frm.end_datetime_dd.focus()
				return false;
			}											
			if (frm.start_datetime_dd && frm.start_datetime_mm && frm.start_datetime_yy && frm.end_datetime_dd && frm.end_datetime_mm && frm.end_datetime_yy){
				if (!gen_validate_date_compare_larger(frm,'start_datetime','end_datetime', eval('wb_msg_' + lang + '_start_date'), eval('wb_msg_' + lang + '_end_date'), lang)){
					return false;
				}
			}		
			//start_date_time		
			if(_wbGlbMgtGetStartDatetime(frm,lang,spec) == false){return false;}
			//end_date_time
			if(_wbGlbMgtGetEndDatetime(frm,lang,spec) == false){return false;}	

		}else{
			if(_wbGlbMgtGetDateSelection(frm,lang,spec) == false){return false;}
		}
	}
	if(_wbGlbMgtGetAllOrgInd(frm,lang,spec) == false){return false;}
	
	if(frm.all_org_ind && frm.all_org_ind.length && frm.all_org_ind[1].checked == true){
		if(_wbGlbMgtGetEntId(frm,lang,spec) == false){
			return false;
		}
	}
	
	if( frm.rdo_report_view && frm.rdo_report_view.length ) {
		spec.name += 'report_view' + d_f;
		if( frm.rdo_report_view[0].checked ) {
			spec.value += 1 + d_f;
		} else if( frm.rdo_report_view[1].checked ) {
			spec.value += 2 + d_f;
		} else if( frm.rdo_report_view[2].checked ) {
			spec.value += 3 + d_f;
			if( frm.course_code.value == '' ) {
				alert(eval('wb_msg_'+ lang + '_select_course_code'));
				return false;
			} else { 
				spec.name += 'course_code' + d_f;
				spec.value += frm.course_code.value + d_f;
			}
		} else {
			return false;
		}
	} else if( frm.report_view && frm.report_view.type == 'hidden' ) {
		spec.name += 'report_view' + d_f;
		spec.value += frm.report_view.value + d_f;
	}
}

function _wbGlbMgtGetEntId(frm,lang,spec){
	if(frm.ent_id){
		if(frm.ent_id.length){
			var i=0;
			var checked = false;
			var temp_ent_id_lst = "";
			for(i=0;i<frm.ent_id.length;i++){
				if(frm.ent_id[i].checked == true){
					temp_ent_id_lst += frm.ent_id[i].value + d_v;
				}
			}
			if(temp_ent_id_lst == ""){
				alert(eval('wb_msg_'+lang+'_select_site'));
				return false;
			}else{
				temp_ent_id_lst = temp_ent_id_lst.substr(0,temp_ent_id_lst.length-1)	
				spec.name	+= "ent_id" + d_f;
				spec.value	+= temp_ent_id_lst + d_f;						
			}
		}else{
			if(frm.ent_id.type == "checkbox" && frm.ent_id.checked == false){
				alert(eval('wb_msg_'+lang+'_select_site'));
				return false;
			}else{
				spec.name	+= "ent_id" + d_f;
				spec.value	+= frm.ent_id.value + d_f;
			}
		}
	}
	return true;
}

function _wbGlbMgtGetAllOrgInd(frm,lang,spec){
	if(frm.all_org_ind && frm.all_org_ind.length){
		spec.name += "all_org_ind" + d_f;
		if(frm.all_org_ind[1].checked == true){
			spec.value += "0" + d_f;
		}else{
			spec.value += "1" + d_f;
		}	
	}else if(frm.all_org_ind && frm.all_org_ind.type == 'hidden'){
		spec.name += "all_org_ind" + d_f;
		spec.value += frm.all_org_ind.value + d_f;
	}
	return true;
}

function _wbGlbMgtGetDateType(frm,lang,spec){
	if(frm.rdo_date_type){
		spec.name	+= "rdo_date_type" + d_f;
		if(frm.rdo_date_type[1].checked == true){
			spec.value	+= "1" + d_f;
		}else{
			spec.value	+= "0" + d_f;
		}
	}
	return true;
}

function _wbGlbMgtGetDateSelection(frm,lang,spec){
	if(frm.date_selection && frm.date_selection.type == 'select-one'){
		if(frm.date_selection.selectedIndex == 0){
			alert(eval('wb_msg_'+ lang +'_enrol_date'))
			return false;
		}else{
			spec.name	+= "date_selection" + d_f;
			spec.value	+= frm.date_selection[frm.date_selection.selectedIndex].value + d_f;
		}
	}
	return true;
}

function _wbGlbMgtGetStartDatetime(frm,lang,spec){
	if(frm.start_datetime_yy){
		spec.name += "start_datetime" + d_f;
		if((frm.start_datetime_yy.value!=""&&frm.start_datetime_yy.value!=null)||
		   (frm.start_datetime_mm.value!=""&&frm.start_datetime_mm.value!=null)||
		   (frm.start_datetime_dd.value!=""&&frm.start_datetime_dd.value!=null)){
			if(!gen_validate_date("document." + frm.name + ".start_datetime","period","en")){
				return false;
			}
			spec.value += frm.start_datetime_yy.value + "-" + frm.start_datetime_mm.value + "-" + frm.start_datetime_dd.value + " 00:00:00.00" + d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}

function _wbGlbMgtGetEndDatetime(frm,lang,spec){
	if(frm.end_datetime_yy){
		spec.name += "end_datetime" + d_f;
		if((frm.end_datetime_yy.value!=""&&frm.end_datetime_yy.value!=null)||
		   (frm.end_datetime_mm.value!=""&&frm.end_datetime_mm.value!=null)||
		   (frm.end_datetime_dd.value!=""&&frm.end_datetime_dd.value!=null)){
			if(!gen_validate_date("document." + frm.name + ".end_datetime","period","en")){
				return false;
			}
			spec.value += frm.end_datetime_yy.value + "-" + frm.end_datetime_mm.value + "-" + frm.end_datetime_dd.value + " 23:59:59.00"+ d_f;
		}else{
			spec.value += d_f;
		}
	}
	return true;
}