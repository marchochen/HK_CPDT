// ------------------ wizBank Credit object -------------------
// Convention:
//   public functions : use "wbCredit" prefix
//   private functions: use "_wbCredit" prefix
// ------------------------------------------------------------
var creditModule = "JsonMod.credit.CreditModule"
/* constructor */
function wbCredit() {
	this.get_manage_page = wbCreditGetManagePage;
	//for auto credit point
  this.get_auto_credit = wbCreditGetAutoPoint;
  this.upd_autobonus = wbCreditUpdAutoBonusPoint;
  //for manual credit point
	this.set_manual_point = wbCreditSetManualPoint;
	this.add_manual_point = wbCreditAddManualPoint;
	this.del_manual_point = wbCreditDeleteManualPoint;

	//set learner point
	this.set_learner_point = wbCreditSetLearnerPoint;
	this.set_learner_point_exec = wbCreditSetLearnerPointExec;
	this.set_cos_point_perp = wbCreditSetCoursePointPrep;
	
	this.empty_learner_point_exec = wbCreditEmptyLearnerPointExec;
	
	this.import_prep = wbCreditBonusImportPrep;
}
//Public Functions
//=======================================================================================
function wbCreditGetManagePage() {
	url = wb_utils_invoke_disp_servlet('module',creditModule,'cmd','get_credit_main','stylesheet','credit_admin_main.xsl');
	window.location.href = url;
}

function wbCreditGetAutoPoint() {
	url = wb_utils_invoke_disp_servlet('module',creditModule,'cmd','get_auto_credit','stylesheet','credit_auto_point_setting.xsl');
	window.location.href = url;
}

function wbCreditUpdAutoBonusPoint(frm) {
    if(_wbCreditAutoBpFormVaild(frm)) {
      frm.module.value = creditModule;
    	frm.cmd.value = 'auto_bp_setting_upd';
    	frm.action = wb_utils_disp_servlet_url;
    	frm.method = 'post';
    	frm.url_success.value = wb_utils_invoke_disp_servlet('module',creditModule,'cmd','get_auto_credit','stylesheet','credit_auto_point_setting.xsl');
    	frm.url_failure.value = wb_utils_invoke_disp_servlet('module',creditModule,'cmd','get_auto_credit','stylesheet','credit_auto_point_setting.xsl');
    	frm.submit();
    }
    return false;
}

function wbCreditSetManualPoint(deduction_ind,orderby) {
	if(orderby=='asc'){
		orderby = 'desc';
	}else{
		orderby = 'asc';
	}
	var url = wb_utils_invoke_disp_servlet('module', creditModule, 'cmd', 'get_manual_credit', 'cty_deduction_ind', deduction_ind, 'stylesheet', 'credit_manual_point_lst.xsl','sort_order',orderby);
	window.location.href = url;
}

function wbCreditAddManualPoint(frm, deduction_ind) {
	if (frm.cty_code){
		if (!gen_validate_empty_field(frm.cty_code, frm.lab_cty_code.value)) {
			frm.cty_code.focus()
			return;
		} 
		if(getChars(frm.cty_code.value) > 80)
		{
			alert(wb_msg_gb_title_length);
			return;
		}
		frm.cty_deduction_ind.value = deduction_ind;
		frm.module.value = creditModule;
		frm.cmd.value = 'ins_manual_point';
		frm.url_success.value = wb_utils_invoke_disp_servlet('module', creditModule, 'cmd', 'get_manual_credit', 'cty_deduction_ind', deduction_ind, 'stylesheet', 'credit_manual_point_lst.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module', creditModule, 'cmd', 'get_manual_credit', 'cty_deduction_ind', deduction_ind, 'stylesheet', 'credit_manual_point_lst.xsl');
		frm.submit();
		
	}
}

function wbCreditDeleteManualPoint(cty_id, deduction_ind) {
	if(confirm(eval('wb_msg_confirm'))){
		var url_success = wb_utils_invoke_disp_servlet('module', creditModule, 'cmd', 'get_manual_credit', 'cty_deduction_ind', deduction_ind, 'stylesheet', 'credit_manual_point_lst.xsl');
		var url_failure = wb_utils_invoke_disp_servlet('module', creditModule, 'cmd', 'get_manual_credit', 'cty_deduction_ind', deduction_ind, 'stylesheet', 'credit_manual_point_lst.xsl');
		var url = wb_utils_invoke_disp_servlet('module', creditModule, 'cmd', 'del_manual_point', 'cty_id', cty_id, 'url_success', url_success, 'url_failure', url_failure);
		window.location.href = url;
	}	
}

function wbCreditSetLearnerPoint(isEmpty) {
	if(isEmpty){
	      var url = wb_utils_invoke_disp_servlet("module", creditModule, "cmd", "search_manual_point_lst", "stylesheet", "credit_empty_learner_point.xsl",'cty_set_type','INTEGRAL_EMPTY');
	}else{
	     var url = wb_utils_invoke_disp_servlet("module", creditModule, "cmd", "search_manual_point_lst", "stylesheet", "credit_set_learner_point.xsl");
	}
	
	window.location.href = url;
}

function wbCreditSetLearnerPointExec(frm) {
	//ent_id_lst
	if(_wbCreditGetEntIDLst(frm) == false){return false;}
	
	obj_type = eval('frm.change_type');
	
	obj_bpt = eval('frm.cty_id');
	if (obj_bpt) {
		if (obj_bpt.options[obj_bpt.selectedIndex].value == 0) {
			if(obj_type && obj_type.options[obj_type.selectedIndex].value == 1){
				alert(eval('wb_msg_sel_point'));
			}else{
				alert(eval('wb_msg_buckle_point'));
			}
			return;
		}
	}
	obj_point = eval('frm.input_point');
	if (obj_point) {
		if (!gen_validate_float1000(obj_point, obj_point.id)) {
			return;
		}
		frm.change_point.value = obj_point.value;
	}
	
	if (obj_type) {
		if (obj_type.options[obj_type.selectedIndex].value == 1) {
			frm.cty_deduction_ind.value = false;
		} else {
			frm.cty_deduction_ind.value = true;
		}
	}
	frm.module.value = creditModule;
	frm.cmd.value = 'set_learner_point_exec';
	frm.action = wb_utils_disp_servlet_url;
	frm.method = 'post';
	frm.url_success.value = window.location.href;
	frm.url_failure.value = window.location.href;
	frm.submit();
}
function wbCreditEmptyLearnerPointExec(frm) {	
	 if(!_wbCreditGetEntIDLst(frm)){
		 return;
	 }
	obj_type = eval('frm.change_type');
	if (obj_type) {
		if (obj_type.value ==-1) {
			frm.cty_deduction_ind.value = true;
		} 
	}
	if( !confirm(frm.msg.value)){
		return;
	}
	frm.input_point.value = -1;
	frm.module.value = creditModule;
	frm.cmd.value = 'set_learner_point_exec';
	frm.action = wb_utils_disp_servlet_url;
	frm.method = 'post';
	frm.url_success.value = window.location.href;
	frm.url_failure.value = window.location.href;
	frm.submit();
}

function _wbCreditGetEntIDLst(frm){
	var temp_lst = "";
	if (frm.ent_id_lst){	
		for(i=0;i<frm.ent_id_lst.options.length;i++){
			temp_lst += frm.ent_id_lst.options[i].value + "~";
		}
		if(temp_lst ==""){
			alert(eval("wb_msg_sel_lrn_grp"));
			return false;
		}else{
			temp_lst = temp_lst.substr(0,temp_lst.length-1)			
		}
   	}
   	frm.usr_n_usg_id_lst.value = temp_lst; 	
   	return true;	
}

function wbCreditSetCoursePointPrep() {
	var url = wb_utils_invoke_disp_servlet('module', creditModule,'cmd', 'get_credit_main', 'stylesheet', 'credit_set_cos_point.xsl');
			feature = 'toolbar='		+ 'no'
		+ ',location='				+ 'no'
		+ ',width=' 				+ 500
		+ ',height=' 				+ 600
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',status='				+ 'no'
	
	wbUtilsOpenWin(url, 'win',false,feature)
}

function _wbCreditAutoBpFormVaild(frm) {
    var result = 0;
    var inpus = document.getElementsByTagName("INPUT")
    for(var i=0; i<inpus.length; i++){
  	 
  	  if(inpus[i].type=="text"){
	        	if(inpus[i].name.substring(0,6) == "score_"){
					//分数类判断是否为空
		        	if(!wbUtilsValidateEmptyField(inpus[i], inpus[i].attributes.label.nodeValue)) {
			            return false;
		            }
		            if(!wbUtilsValidateAllFloat(inpus[i], inpus[i].attributes.label.nodeValue)) {
			            return false;
		            }
				}else if(inpus[i].name.substring(0,4) == "hit_"){  
					//判断次数
		        	if(!wbUtilsValidateEmptyField(inpus[i], inpus[i].attributes.label.nodeValue)) {
			            return false;
		            }
		            if(!wbUtilsValidatePositiveInteger(inpus[i], inpus[i-1].attributes.label.nodeValue+""+inpus[i].attributes.label.nodeValue)) {
		            	return false;
		            }
				}
		 }
  }
    return true;
}

function wbCreditBonusImportPrep() {
	url = wb_utils_invoke_disp_servlet('cmd','upload_user_credits_prep','module','upload.UploadModule','stylesheet','bp_usr_credits_import_prep.xsl');
	window.location.href = url;
}