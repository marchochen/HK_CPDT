// ------------------ ApplyEasy Item Requirement Object --------------
// Convention:
//   public functions : use "aeItemReq" prefix 
//   private functions: use "_aeItemReq" prefix
// Dependency:
//   wb_utils.js
// ------------------------------------------------------------ 
/* constructor */
function wbItemReq(){	
	this.itm_req_lst 	= wbItemReqList
	this.itm_run_req_lst 	= wbItemRunReqList

	this.itm_pre_ins_prep = wbItemReqPreInsertPrep
	this.itm_exm_ins_prep = wbItemReqExmInsertPrep
	this.itm_usr_exm_ins_prep = wbItemReqUserExmInsertPrep

	this.itm_pre_upd_prep 	  = wbItemReqPreUpdatePrep
	this.itm_exm_upd_prep 	  = wbItemReqExmUpdatePrep
	this.itm_usr_exm_upd_prep 	  = wbItemReqUserExmUpdatePrep
	this.itm_run_pre_upd_prep = wbItemRunReqPreUpdatePrep

	this.itm_pre_exec = wbItemReqPreExec
	this.itm_exm_exec = wbItemReqExmExec
	this.itm_usr_exm_exec = wbItemReqUserExmExec

	this.itm_req_del_exec = wbItemReqDel;

	this.itm_req_save_due_date = wbItemReqSaveDueDate;
}


function wbItemReqSaveDueDate(frm, lang, itmId, itrOrder) {
	if(!gen_validate_date("frm.due_datetime",eval("wb_msg_"+lang+"_due_date"),lang)) {
		return false;
	}
	var req_due_date = frm.due_datetime_yy.value+"-"+frm.due_datetime_mm.value+"-"+frm.due_datetime_dd.value+" "+"23:59:59.0";
	var url_success = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_run_req_list.xsl', 'itm_id', itmId);
	var url_failure = window.location.href;
	var url = wb_utils_invoke_ae_servlet('cmd','ae_save_itm_req_due_date', 'itm_id', itmId, 'itr_order', itrOrder, 'req_due_date', req_due_date, 'url_success', url_success, 'url_failure', url_failure);
	window.location.href = url;
}


function wbItemReqList(itmId) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_list.xsl', 'itm_id', itmId);
	window.location.href = url;	
}

function wbItemRunReqList(itmId) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_run_req_list.xsl', 'itm_id', itmId);
	window.location.href = url;	
}

function wbItemReqPreInsertPrep(itmId, itrOrder) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_prep_itm_req','stylesheet','itm_req_edit_pre.xsl', 'itm_id', itmId, 'itr_order', itrOrder, 'req_type', 'PREREQUISITE');
	window.location.href = url;	
}

function wbItemReqExmInsertPrep(itmId, itrOrder) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_prep_itm_req','stylesheet','itm_req_edit_exm.xsl', 'itm_id', itmId, 'itr_order', itrOrder, 'req_type', 'EXEMPTION' ,'req_subtype', 'COURSE');
	window.location.href = url;	
}

function wbItemReqUserExmInsertPrep(itmId, itrOrder) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_prep_itm_req','stylesheet','itm_req_edit_usr_exm.xsl', 'itm_id', itmId, 'itr_order', itrOrder, 'req_type', 'EXEMPTION' ,'req_subtype', 'USER');
	window.location.href = url;	
}

function _wbItemTest(ele, frm) {
	for(var i=0;i<ele.options.length;i++){
	    if (parseInt(ele.options[i].value) == parseInt(frm.wb_itm_id.value)) {
	        return false;
	    }
	}
	return true;    
}

function wbItemReqPreExec(frm, lang, itmId, itrOrder, mode) {
	if(!_wbItemReqValidateForm(frm,lang))
		return;
	if(mode=='INSERT') {
		var cmd = 'ae_ins_itm_req';
	} else if(mode=='UPDATE') {
		var cmd = 'ae_upd_itm_req';
	}
	if (!_wbItemTest(frm.req_itm_id_lst_fld,frm)) {
	    alert(wb_msg_self_prerequisite);
	    return false;
	}
	var req_itm_id_lst = _wbItemReqGetGoldenManLst(frm.req_itm_id_lst_fld);
	if(req_itm_id_lst == "") {
		alert(eval("wb_msg_"+lang+"_plz_pre_lrn_soln"));
		return false;
	}
	var req_operator = 'OR';
	var req_type = 'PREREQUISITE';

	//var req_subtype = _wbItemReqGetCheckboxLst(frm.pre_type, true);
	var req_subtype = frm.pre_type.value;
	if(req_subtype=='') {
		alert(eval("wb_msg_"+lang+"_plz_pre_type"));
		return false;
	}

	if(frm.due_datetime_yy && frm.due_datetime_mm && frm.due_datetime_dd) {

		if(req_subtype=='COMPLETION' && !gen_validate_date("frm.due_datetime",eval("wb_msg_"+lang+"_due_date"),lang)) {
			return false;
		}

		if(req_subtype=='ENROLLMENT' && (frm.due_datetime_yy.value!='' || frm.due_datetime_mm.value!='' || frm.due_datetime_dd.value!='')) {
			alert(eval("wb_msg_"+lang+"_enrollment_due_date"));
			return false;
		}

		if((frm.due_datetime_yy.value!='' || frm.due_datetime_mm.value!='' || frm.due_datetime_dd.value!='') && !gen_validate_date("frm.due_datetime",eval("wb_msg_"+lang+"_due_date"),lang)) {
			return false;
		}
	}

	var req_restriction = '';
	var req_due_date = '';
	if(frm.due_datetime_yy && frm.due_datetime_mm && frm.due_datetime_dd) {
		if(frm.due_datetime_yy.value!='' && frm.due_datetime_mm.value!='' && frm.due_datetime_dd.value!='') {
			req_due_date = frm.due_datetime_yy.value+"-"+frm.due_datetime_mm.value+"-"+frm.due_datetime_dd.value+" "+"23:59:59.0";
		}
	}	
	if(req_subtype=='COMPLETION') {
		req_restriction = 'DUE_DATE';		
	}

	var appn_footnote_ind = 0;
	var cond_type = 'AICC_SCRIPT';
	var neg_actn_type = '';
	var neg_attn_status = '';
	if(req_subtype=='COMPLETION') {
		appn_footnote_ind = 0;
		neg_actn_type = 'IAA';
		neg_attn_status = 'F';
	} else {
		appn_footnote_ind = 1;
		neg_actn_type = '';
		neg_attn_status = '';
	}
	var upd_time = '';
	if(frm.upd_time.value != ''){
		upd_time = frm.upd_time.value;
	}
	var url_success = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_list.xsl', 'itm_id', itmId);
    var url_failure = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_edit_pre.xsl', 'itm_id', itmId, 'itr_order', itrOrder);

	url = wb_utils_invoke_ae_servlet('cmd', cmd,'itm_id',itmId,'itr_order',itrOrder,'req_type',req_type,'req_subtype',req_subtype,
		'req_restriction',req_restriction,'req_due_date',req_due_date,'appn_footnote_ind',appn_footnote_ind,'cond_type',cond_type,
		'req_itm_id_lst',req_itm_id_lst, 'req_operator', req_operator,
		'neg_actn_type',neg_actn_type,'neg_attn_status',neg_attn_status,
		'url_success', url_success,'url_failure',url_failure,'upd_time',upd_time);
	window.location.href = url;	
}

function wbItemReqUserExmExec(frm, lang, itmId, itrOrder, mode) {
	if(!_wbItemReqValidateForm(frm,lang))
		return;

	if(mode=='INSERT') {
		var cmd = 'ae_ins_itm_req';
	} else if(mode=='UPDATE') {
		var cmd = 'ae_upd_itm_req';
	}
	var req_ent_lst = frm.req_ent_lst.value;
	var req_operator = 'AND';
	var req_type = 'EXEMPTION';
	var req_subtype = "USER";
	var req_restriction = '';
	var req_due_date = '';
	var appn_footnote_ind = 1;
	var cond_type = 'AICC_SCRIPT';
	
	var url_success = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_list.xsl', 'itm_id', itmId);

	if(req_ent_lst == "") {
		alert(eval("wb_msg_"+lang+"_plz_exm_lrn_soln"));
		return false;
	}

	url = wb_utils_invoke_ae_servlet('cmd', cmd,'itm_id',itmId,'itr_order',itrOrder,'req_type',req_type,'req_subtype',req_subtype,
		'req_restriction',req_restriction,'req_due_date',req_due_date,'appn_footnote_ind',appn_footnote_ind,'cond_type',cond_type,
		'req_ent_lst',req_ent_lst, 'req_operator', req_operator,
		'url_success', url_success);
	window.location.href = url;	
}

function wbItemReqExmExec(frm, lang, itmId, itrOrder, mode) {
	if(!_wbItemReqValidateForm(frm,lang))
		return;

	if(mode=='INSERT') {
		var cmd = 'ae_ins_itm_req';
	} else if(mode=='UPDATE') {
		var cmd = 'ae_upd_itm_req';
	}
	var req_itm_id_lst = _wbItemReqGetGoldenManLst(frm.req_itm_id_lst_fld);
	var req_operator = 'OR';
	var req_type = 'EXEMPTION';
	var req_subtype = 'COURSE';
	var req_restriction = '';
	var req_due_date = '';
	var appn_footnote_ind = 1;
	var cond_type = 'AICC_SCRIPT';
	
	var url_success = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_list.xsl', 'itm_id', itmId);

	if(req_itm_id_lst == "") {
		alert(eval("wb_msg_"+lang+"_plz_exm_lrn_soln"));
		return false;
	}

	url = wb_utils_invoke_ae_servlet('cmd', cmd,'itm_id',itmId,'itr_order',itrOrder,'req_type',req_type,'req_subtype',req_subtype,
		'req_restriction',req_restriction,'req_due_date',req_due_date,'appn_footnote_ind',appn_footnote_ind,'cond_type',cond_type,
		'req_itm_id_lst',req_itm_id_lst, 'req_operator', req_operator,
		'url_success', url_success);
	window.location.href = url;	
}

function wbItemReqPreUpdatePrep(itmId, itrOrder) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_edit_pre.xsl', 'itm_id', itmId, 'itr_order', itrOrder);
	window.location.href = url;	
}

function wbItemRunReqPreUpdatePrep(itmId, itrOrder) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_run_req_edit_pre.xsl', 'itm_id', itmId, 'itr_order', itrOrder);
	window.location.href = url;	
}

function wbItemReqExmUpdatePrep(itmId, itrOrder) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_edit_exm.xsl', 'itm_id', itmId, 'itr_order', itrOrder);
	window.location.href = url;	
}

function wbItemReqUserExmUpdatePrep(itmId, itrOrder) {
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_edit_usr_exm.xsl', 'itm_id', itmId, 'itr_order', itrOrder);
	window.location.href = url;	
}

function wbItemReqDel(itmId, itrOrder) {
	var url_success = wb_utils_invoke_ae_servlet('cmd','ae_get_itm_req','stylesheet','itm_req_list.xsl', 'itm_id', itmId);
	url = wb_utils_invoke_ae_servlet('cmd','ae_del_itm_req', 'itm_id', itmId, 'itr_order', itrOrder, 'url_success', url_success);
		if(confirm(wb_msg_confirm)){
		   window.location.href = url;
	  }
}

function _wbItemReqGetGoldenManLst(ele,selected,separator){
	var list = "";
	if(separator==null) {
		separator = "~";
	}
	for(var i=0;i<ele.options.length;i++){
		if(selected==null || (selected && ele.options[i].selected) || (!selected && !ele.options[i].selected))
			list += ele.options[i].value + separator;
	}
	if(list.indexOf(separator)>-1)
		list = list.substr(0,list.length-1);
	return list;
}

/* Validation  Functions */

function _wbItemReqValidateForm(frm,lang){

	return true;
}

function _wbItemReqGetCheckboxLst(ele,checked){
	var list = "";
	if(ele){
		if(ele.length){
			for(var i=0;i<ele.length;i++){
				if(checked==null || (checked && ele[i].checked) || (!checked && !ele[i].checked)){
					list += ele[i].value + "~";
				}
			}
		}else{
			if(checked==null || (checked && ele.checked) || (!checked && !ele.checked))
				list = ele.value;
		}
	}
	if(list.indexOf("~")>-1)
		list = list.substr(0,list.length-1);
	return list;
}

