function wbUserPosition(){
	this.search = SearchUserPositionList;
	this.add = AddUserPosition;
	this.modify = ModifyUserPosition;
	this.eff = EffectUserPosition;
	this.ins = InsertUserPosition;
	this.upd = UpdateUserPosition;
	this.del = DeleteUserPosition;
}

function SearchUserPositionList(frm){
	var search_info = '';
	if(frm.upt_search_text){
		search_info = frm.upt_search_text.value;
	}
	url = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','search_position_list', 'stylesheet', 'usr_position_list.xsl', 'search_info', search_info);
	window.parent.location.href = url;
}

function AddUserPosition(){
	url = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','add_position', 'stylesheet', 'usr_position_addORupd.xsl');
	window.parent.location.href = url;
}

function ModifyUserPosition(code){
	url = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','modify_position', 'stylesheet', 'usr_position_addORupd.xsl', 'upt_code', code);
	window.parent.location.href = url;
}

function EffectUserPosition(frm){
	if(frm.upt_code_list == undefined){
		return;
	}
	var code_list = "";
	if(frm.upt_code_list.length == undefined && frm.upt_code_list.checked){
		code_list = frm.upt_code_list.value  + "[|]";
	} else {
		for(var i=0;i<frm.upt_code_list.length;i++){
			if(frm.upt_code_list[i].checked){
				code_list += frm.upt_code_list[i].value + "[|]";
			}
		}
	}
	if(code_list == ""){
		alert(wb_msg_choose_del_upt);
		return;
	}
	url = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','get_eff_position', 'stylesheet', 
			'usr_position_del.xsl', 'upt_code_list', code_list.substring(0, code_list.length-3));
	window.parent.location.href = url;
}

function InsertUserPosition(frm){
	if (frm.upt_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		  	alert(wb_msg_pls_input_tcr);
	    	return;
	    } else {
	    	frm.upt_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	
	if(!frm.upt_code.value.length>0){
		alert(wb_msg_upt_code_not_null);
		return;
	}
	if(!frm.upt_title.value.length>0){
		alert(wb_msg_upt_title_not_null);
		return;
	}
	
	frm.module.value = 'position.UserPositionModule';	
	frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','get_position_list', 'stylesheet', 'usr_position_list.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','add_position', 'stylesheet', 'usr_position_addORupd.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function UpdateUserPosition(frm){
	var upt_tcr_id = "";
	if (frm.upt_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		  	alert(wb_msg_pls_input_tcr);
	    	return;
	    } else {
	    	frm.upt_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	
	if(!frm.upt_code.value.length>0){
		alert(wb_msg_upt_code_not_null);
		return;
	}
	if(!frm.upt_title.value.length>0){
		alert(wb_msg_upt_title_not_null);
		return;
	}
	
	frm.module.value = 'position.UserPositionModule';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','get_position_list', 'stylesheet', 'usr_position_list.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function DeleteUserPosition(frm){
	if(!confirm(frm.confirm_del_upt.value)){
		return;
	}
	
	frm.module.value = 'position.UserPositionModule';
	frm.cmd.value = 'del_position';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','get_position_list', 'stylesheet', 'usr_position_list.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
	
}