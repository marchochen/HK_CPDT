// ------------------ wizBank Completion Criteria Condtion object ------------------- 
// Convention:
//   public functions : use "wbCompCond" prefix 
//   private functions: use "_wbCompCond" prefix
// ------------------------------------------------------------ 

/* Constructor*/

function wbCompCond(){
	this.validateRunform = wbCompCondValidateRunForm;
}

function wbCompCondValidateRunForm(frm, lang){
	if(frm.mark_status && frm.mark_status.checked == true){
    	if(!_wbCompCondCheckPercent(frm.pass_mark)){
    	    alert(wb_msg_score_input_0_100);
        	return;
        }
    }
    if(frm.attend_status && frm.attend_status.checked == true){
		if(!_wbCompCondCheckPercent(frm.pass_attend)){
		    alert(wb_msg_attend_input_0_100);
      		return;
      	}
    }

    if (frm.online_module.checked == true) {
        if (!chkOnlineModule(frm, "run")) {
        	return;
        }
    }
    
    if (frm.add_on && frm.add_on.checked == true) {
		frm.offline_cond.value = wbUtilsTrimString(frm.offline_cond.value);
        if (frm.offline_cond.value == '') {
            alert(wb_msg_pls_specify_value + frm.lab_offline_cond.value);
            frm.offline_cond.focus();
            return;
        }
        if(!_checkLength(frm.offline_cond, 2000, lang))
            return;
    }
	var url = wb_utils_invoke_disp_servlet('module', 'course.CourseCriteriaModule', 'cmd', 'mod_run_criteria_cond', 'itm_id', frm.itm_id.value, 'stylesheet', 'completion_criteria_cond_run.xsl');
    var url_;
	if(frm.is_new_cos.value == 'true'){
		url_ =  wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', frm.itm_id.value, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_publish_new_cos.xsl');
		if(frm.itm_run_ind.value){
			url_ =  wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', frm.itm_id.value, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_run_view.xsl');
		}
	}else{
     	url_ =  wb_utils_invoke_ae_servlet('cmd', 'ae_get_itm', 'itm_id', frm.itm_id.value, 'tvw_id', 'DETAIL_VIEW', 'stylesheet', 'itm_details.xsl');
	}
	frm.url_success.value = url_;
	frm.url_failure.value = url_;
	frm.action = url;
	frm.method = "post";
	frm.submit();
}

function _wbCompCondCheckPercent(el){
	var str = el.value;
	if(str.search(/^[0-9]{1,3}$/) >= 0){
		if(str > 0 && str <= 100)
			return true;
	}
	_setFocus(el);
	return false;
}

function _setFocus(el){
	try{
		el.focus();
		el.select();
	}catch(e){
	}	
}

function _checkLength(el, size, lang){
      var str = el.value;
    if(str.length < size){
        return true;
    }else{
        _setFocus(el);
        alert(eval('wb_msg_'+lang+'_desc_too_long'));
        return false;
    }
}

function chkOnlineModule(frm, mode){
	var count = frm.online_item_count.value;
	var desc, condSel, modSel;
	for (var i = 1; i <= count; i++) {
		//desc = eval("frm.o_item_mod_sel_" + i);
		//desc.value = wbUtilsTrimString(desc.value);
		condSel = eval("frm.o_item_cond_sel_" + i);
		if (mode.toLowerCase() == "run") {
			modSel = eval("frm.o_item_mod_sel_" + i);
			if (modSel.value != "") {
				/*if (desc.value == "") {
					alert(wb_msg_ils_desc_needed);
					desc.focus();
					return false;
				} else if (condSel.value == "") {
					alert(wb_msg_ils_criteria_needed);
					condSel.focus();
					return false;
				}*/
			}
		}
		/*if (condSel.value != "") {
			if (desc.value == "") {
				alert(wb_msg_ils_desc_needed);
				desc.focus();
				return false;
			}
		}*/
	}
	return true;
}