
// ------------------ wizBank Login object ------------------- 
// Convention:
//   public functions : use "wbLogin prefix 
//   private functions: use "_wbLogin" prefix
// ------------------------------------------------------------ 
function wbLogin(){
	this.submitFrm = _wbLoginSubmitFrm
	this.changePage = _wbLoginChangePage
	this.selectCurrLang = _wbLoginSelectCurrLang
	this.init = _wbLoginInit
}

// -----------------Private Functons ------------------ 
function _wbLoginSubmitFrm(frm, lang){

	// populate hidden form data
	var url_success = getUrlParam('url_success');

	if(url_success == ""){
		url_success = gen_get_cookie('url_login_success');
	}
	// Check if the user need to change pwd immediately
	frm.url_change_pwd.value = wb_utils_app_base + 'app/personal/passwordModify?fromlogin=true';

	frm.stylesheet.value = 'home.xsl';

	frm.action = wb_utils_disp_servlet_url + "?isExcludes=true";

	gen_del_cookie('url_login_success')

//	frm.url_failure.value = wb_utils_invoke_servlet('cmd', 'start', 'stylesheet', 'login.xsl');
	frm.cmd.value = 'auth';
	frm.module.value = 'login.LoginModule';

	if(frm.label_lan.value == ''){
		frm.label_lan.value = 'ISO-8859-1';
	}
	if(_wbLoginValidateFields(frm, lang)){
		wb_utils_set_cookie('login_lan', frm.label_lan.value);
		wb_utils_set_cookie('usr_id', frm.usr_id.value);
		var temp_date = new Date(2099, 12, 31)
		gen_set_cookie('site', frm.site_id.value, temp_date);

		if(frm.site_id.options != null){
			frm.style.value = eval('frm.site_style' + frm.site_id.options[frm.site_id.selectedIndex].value + '.value');
		}else{
			frm.style.value = eval('frm.site_style' + frm.site_id.value + '.value');
		}
		frm.submit();
	}
}

function _wbLoginChangePage(){
	// Prevents inside other frame.
	if(self.parent.frames.length != 0){
		self.parent.location = document.location;
	}
}

function _wbLoginValidateFields(frm, lang){
	var lab_login_id = frm.lab_login_id ? frm.lab_login_id.value : 'Login ID(sys)';

	var lab_passwd = frm.lab_passwd ? frm.lab_passwd.value : 'Password(sys)';

	if(!_wbUtilsValidateUserId(frm.usr_id, lab_login_id, false, '', frm.login_lan)){
		return false;
	}

	if(!_wbUtilsValidateUserPassword(frm.usr_pwd, lab_passwd, '', '', frm.login_lan)){
		return false;
	}

	if(frm.site_id.options != null){
		if(!wb_utils_validate_site_id(frm.site_id.options[frm.site_id.selectedIndex], lang)){
			alert('Please select an organization');
			return false;
		}
	}

	if(frm.label_lan.value == ''){
		alert('please select a language');
		return false
	}
	return true;
}

function _wbUtilsValidateUserPassword(fld_usr_pwd, txtFldName, min, max, fld_login_lan){

	if(fld_usr_pwd.value.length == 0){
		Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_please_enter_the'] + '"' + txtFldName + '"',function(){
			fld_usr_pwd.focus();
		});
		return false;
	}

	if( min != '' ) {
		if(fld_usr_pwd.value.length < min){
			Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_enter_valid'] + '"' + txtFldName + '"' + '. ' + labels[fld_login_lan.value]['wb_msg_usr_min_character'] + min,function(){
				fld_usr_pwd.focus();
			});
			return false;
		}
	}

	if(fld_usr_pwd.value.search(/[^A-Za-z0-9-_]/) != -1){
		Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_enter_valid'] + '"' + txtFldName + '"',function(){
			fld_usr_pwd.focus();
		});
		return false;
	}

	if(max != '' ) {
		if(fld_usr_pwd.value.length > max){
			Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_enter_valid'] + '"' + txtFldName + '"',function(){
				fld_usr_pwd.focus();
			});
			return false;
		}
	}
	return true;
}

function _wbUtilsValidateUserId(fld_usr_id, txtFldName, min, max, fld_login_lan){

	if(fld_usr_id.value.search(/[^A-Za-z0-9_-]/) != -1){
		Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_enter_english_char'] + '"' + txtFldName + '"',function(){
			fld_usr_id.focus();
		});
		return false;
	}

	if(fld_usr_id.value.length == 0){
		Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_please_enter_the'] + '"' + txtFldName + '"',function(){
			fld_usr_id.focus();
		});
		return false;
	}

	if( min != '' ) {
		if(fld_usr_id.value.length < min) {
			Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_enter_valid'] + '"' + txtFldName + '"' + '. ' + labels[fld_login_lan.value]['wb_msg_usr_min_character'] + min,function(){
				fld_usr_id.focus();
			});
			return false;
		}
	}

	if(max != '' ) {
		if(fld_usr_id.value.length > max){
			Dialog.alert(labels[fld_login_lan.value]['wb_msg_usr_enter_valid'] + '"' + txtFldName+ '"',function(){
				fld_usr_id.focus();
			})
			return false;
		}
	}
	return true;
}

function _wbLoginSelectCurrLang(frm, select_lang){
	frm.label_lan.value = select_lang;
}

function _wbLoginInit(frm, site_count, encoding, code){
	var i = 0;
	frm.usr_id.focus();

	frm.label_lan.value = wb_utils_get_cookie('login_lan');
	var site = gen_get_cookie('site');

	if(frm.curr_lang_selection != null){
		if(encoding == 'ISO-8859-1'){
			frm.curr_lang_selection.checked = true;
			frm.label_lan.value = encoding;
		}else{
			if(frm.label_lan.value == encoding){
				frm.curr_lang_selection[0].checked = true;
			}else if(frm.label_lan.value == 'ISO-8859-1'){
				frm.curr_lang_selection[1].checked = true;
			}else{
				frm.curr_lang_selection[0].checked = true;
				frm.label_lan.value = encoding;
			}
		}
	}
	if(site_count != 1){
		for (i = 0; i < frm.site_id.length; i++){
			if(frm.site_id.options[i].value == site){
				frm.site_id.options[i].selected = true;
			}
		}
	}
	
	// show login error message
	var err_code = code;
	var err_msg_txt = '';
	switch (err_code) {
		case "" :
			err_msg_txt = "";
			break;
		/*case "LGF04" :
			err_msg_txt = frm.lab_login_fail.value;
			break;*/
		case "LGF05" :
			err_msg_txt = frm.lab_account_suspended.value;
			break;
		case "LGF08" :
			err_msg_txt = frm.lab_login_fail_08.value;
			break;
		case "LGF09" :
			err_msg_txt = frm.lab_over_validity_period.value;
			break;
		case "LGF13" :
			err_msg_txt = frm.lab_error_user_system_issue.value;
			break;
		default :
			err_msg_txt = frm.lab_login_fail.value;
			var maxTrialObj = frm["site_login_max_trial" + site];
			var login_is_active = frm["login_is_active" + site].value;
			if (maxTrialObj !== undefined && maxTrialObj.value.length > 0 && login_is_active == "true") {
				err_msg_txt += (frm.lab_trial_limit1.value + maxTrialObj.value + frm.lab_trial_limit2.value);
			}
	}
	if (err_msg_txt !== '') {
		$(".xyd-form-error").show();
		$(".xyd-form-error span").html(err_msg_txt);
	}
}