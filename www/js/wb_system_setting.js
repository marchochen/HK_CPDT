function wbSystemSetting() {
	this.prep = wbSystemSettingPrep;
	this.exec = wbSystemSettingExec;
	this.updateWechatMenu = wbUpdateWechatMenu;
}

function wbSystemSettingPrep(type) {
	var stylesheet;
	if (type === 'sys') {
		stylesheet = "system_setting.xsl";
	} else {
		stylesheet = "wechat_setting.xsl";
	}
	url = wb_utils_invoke_disp_servlet('module',
			'systemSetting.SystemSettingModule', 'cmd', 'prep', 'stylesheet',
			stylesheet, 'type', type)
	window.location.href = url;
}

function wbSystemSettingExec(frm) {
	var protocol = document.getElementById("protocol");
	var protocol_sel_val = protocol.options[protocol.selectedIndex].value;
	frm.mail_scheduler_domain.value = protocol_sel_val + frm.mail_scheduler_domain.value;
	
	if (frm.type.value === 'sys') {
		if (frm.mail_server_auth_enabled.value == "true") {
			if (frm.mail_server_user) {
				if (frm.mail_server_user.value.length == 0) {
					alert(eval("wb_msg_system_setting_name"));
					return;
				}
			}
			if (frm.mail_server_password) {
				if (frm.mail_server_password.value.length == 0) {
					alert(eval("wb_msg_system_setting_pass"));
					return;
				}
			}
			if (frm.mail_scheduler_domain) {
				if (frm.mail_scheduler_domain.value.length == 0) {
					alert(eval("wb_msg_system_setting_domain"));
					return;
				}
			}
		}
		
		if (frm.password_policy_period && frm.password_policy_period.value.length>0) {
			if ((frm.password_policy_period.value == 0 && frm.password_policy_period.value.length>1) || !/^[1-9]\d*|0$/.test(frm.password_policy_period.value) || frm.password_policy_period.value<0 || frm.password_policy_period.value>1000) {
				alert(fetchLabel("valid_length").replace('{min}','0').replace('{max}','1000') + fetchLabel("label_core_system_setting_150"));
				frm.password_policy_period.focus();
				return;
			}
		}
		if (frm.password_policy_compare_count && frm.password_policy_compare_count.value.length>0) {
			if ((frm.password_policy_compare_count.value == 0 && frm.password_policy_compare_count.value.length>1) || !/^[1-9]\d*|0$/.test(frm.password_policy_compare_count.value) || frm.password_policy_compare_count.value<0 || frm.password_policy_compare_count.value>10) {
				alert(fetchLabel("valid_length").replace('{min}','0').replace('{max}','10') + fetchLabel("label_core_system_setting_151"));
				frm.password_policy_compare_count.focus();
				return;
			}
		}
		//默认密码
		if(frm.system_default_user_password){
			var default_password = frm.system_default_user_password.value;
			var min = frm.minLength.value; 
			var max = frm.maxLength.value;
			if ($.trim(default_password).length == 0) {
				alert(fetchLabel("usr_password")+fetchLabel("usr_is_not_null"));
				frm.system_default_user_password.focus();
				return;
			}
			if(default_password.search(/[^a-zA-Z0-9_-]/) != -1){
				alert(fetchLabel('usr_password_type'));
				return
			}
			if(!/^(?=.*[A-Za-z]+)(?=.*[0-9]+)[A-za-z0-9_-]*$/.test(default_password)){
				alert(fetchLabel('usr_password_check'));
				return 
			}
			if(default_password.length < min || default_password.length > max){
				alert(fetchLabel('usr_password_size').replace(/min/g,min).replace(/max/g,max));
				return
			}
		}
		
	}
	
	frm.cmd.value = 'exec';
	frm.module.value = 'systemSetting.SystemSettingModule';
	frm.url_success.value = self.location.href;
	frm.url_failure.value = self.location.href;
	frm.method = "post";
	frm.action = wb_utils_disp_servlet_url;
	frm.submit()
}
function wbUpdateWechatMenu(frm) {
	$("#update-btn").attr("disabled",true);
	var tokenUrl = frm.wechat_token_url.value;
	var wechatMenu = frm.wechat_menu.value;
	var menuJson;
	if (!tokenUrl) {
		alert(eval("wb_msg_system_setting_wechat_token"));
		$("#update-btn").attr("disabled",false);
		return;
	}
	if (!wechatMenu) {
		alert(eval("wb_msg_system_setting_wechat_menu"));
		$("#update-btn").attr("disabled",false);
		return;
	}
	try {
		menuJson = eval("(" + wechatMenu + ")");
	} catch (e) {
		alert(eval("wb_msg_system_setting_wechat_menu_error"));
		$("#update-btn").attr("disabled",false);
		return;
	}
	var param = {
		tokenUrl:tokenUrl,
		menuStr:wechatMenu
	};
	wb_utils_preloading();
	$.ajax({
		dataType : "json",
		type : "POST",
		url : wb_utils_controller_base+"admin/wechat/updateMenu",
		data : param,
		success : function(msg) {
			closeLoading();
			if(0 === msg.errcode){
				alert(eval("wb_msg_system_setting_wechat_update_success"));
			}else{
				alert(eval("wb_msg_system_setting_wechat_update_error")+" : "+msg.errmsg);
			}
			$("#update-btn").attr("disabled",false);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown){
			closeLoading();
			alert(eval("wb_msg_system_setting_wechat_update_error"));
			$("#update-btn").attr("disabled",false);
		}
	});
}

function closeLoading(){
	for(var i=0;i<2;i++){
		document.body.removeChild(document.body.childNodes[3]);
	}
}
