// ------------------ wizBank Eassy object ------------------- 
// Convention:
//   public functions : use 'wbES' prefix 
//   private functions: use '_wbES' prefix
/* constructor */
// --------------------- constants ---------------------

MODE_EDIT = 'edit'
MODE_INSERT = 'insert'

// -------------- constants -------------- 

MEDIA_TYPE_GIF = 'image/gif'
MEDIA_TYPE_JPG = 'image/jpg'
MEDIA_TYPE_SWF = 'application/x-shockwave-flash'
MEDIA_TYPE_UNKNOWN = 'unknown'

// ------------------------- function declaration ------------------------- 
/* constructor */
function wbES(){ this.sendFrm = wbESSendForm }

function _wbESValidateForm(frm, lang){
//	var mediaFilename = frm.que_media.value.toLowerCase();
	var mod_type = getUrlParam('mod_type')
//	if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
//		alert(eval('wb_msg_' + lang + '_media_not_support'))
//		if(frm.tmp_que_media01 !== undefined) {
//			frm.tmp_que_media01.focus();
//		} else {
//			frm.que_media.focus();
//		}
//		return false;
//	}

	frm.que_body_1.value = wbUtilsTrimString(frm.que_body_1.value);
	frm.que_body_1.value = removeEditorSpaces(frm.que_body_1.value);
	if(frm.que_body_1.value == ''){
		alert(wb_msg_pls_specify_value + eval('wb_msg_' + lang + '_que_txt'));
		editor.focus();
		return false;
	}
	if(getChars(frm.que_body_1.value) > 2000){
		Dialog.alert(eval('wb_msg_' + lang + '_que_txt') + fetchLabel('label_core_training_management_372'),function(){
			editor.focus();
		});
		return false;
	}
	
	if(frm.inter_opt_exp_1){
		frm.inter_opt_exp_1.value = wbUtilsTrimString(frm.inter_opt_exp_1.value);
		if(getChars(frm.inter_opt_exp_1.value) > 2000){
			Dialog.alert(fetchLabel("label_core_training_management_461") + fetchLabel('label_core_training_management_372'),function(){
				frm.inter_opt_exp_1.focus();
			});
			return false;
		}
	}
	
	frm.inter_opt_score_1.value = wbUtilsTrimString(frm.inter_opt_score_1.value);
	if(!gen_validate_empty_field(frm.inter_opt_score_1, eval('wb_msg_' + lang + '_score_txt'),lang) || !gen_validate_integer(frm.inter_opt_score_1, eval('wb_msg_' + lang + '_score_txt'),lang)) {
		return false;
	}
	
	frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
	if(!gen_validate_empty_field(frm.que_title, eval('wb_msg_' + lang + '_title'), lang)){
		return false;
	}
	
	if(getChars(frm.que_title.value) > 80){
		Dialog.alert(eval('wb_msg_usr_title_too_long'),function(){
			frm.que_title.focus();
		});
		return false;
	}
	
	if(frm.que_desc){
		frm.que_desc.value = wbUtilsTrimString(frm.que_desc.value);
		if(getChars(frm.que_desc.value) > 400){
			Dialog.alert(eval('wb_msg_usr_desc_too_long'),function(){
				frm.que_desc.focus();
			});
			return false;
		}
	}
	
//	if(!gen_validate_empty_field(frm.que_body_1, eval('wb_msg_' + lang + '_que_txt'),lang)) {
//		return false;
//	}
	

	
	if(frm.inter_opt_score_1.value <= 0 ){
		alert(fetchLabel("label_core_training_management_355"));
		return false;
	}
	
	if (frm.inter_opt_exp_1) {
		frm.inter_opt_exp_1.value = wbUtilsTrimString(frm.inter_opt_exp_1.value);
	}
	
	return true;
}

function _wbESGenForm(frm){
//	frm.que_media_.value = wbMediaGetFileName(frm.que_media.value);
	frm.que_body_.value = frm.que_body_1.value;
//	if(frm.asHTML.checked == true) {
		frm.que_html_.value = 'Y';
//	} else {
//		frm.que_html_.value = 'N';
//	}
	if( frm.submitFile && frm.submitFile.checked == true ) {
		frm.que_submit_file_ind.value = 'Y';	
	}else{
		frm.que_submit_file_ind.value = 'N';
	}

	//Option
	frm.inter_opt_score_.value = frm.inter_opt_score_1.value;
	frm.inter_opt_exp_.value = frm.inter_opt_exp_1.value;
	//Additional Information
	frm.que_status_.value = frm.que_status.value;
	frm.que_title_.value = frm.que_title.value;
	frm.que_desc_.value = frm.que_desc.value;
	return;
}

function wbESSendForm(frm, lang, mod_type,isOpen){
	var mod_type = getUrlParam('mod_type')
	if (frm.editor) {
		editor.sync();
	}
	if(_wbESValidateForm(frm, lang)){
		_wbESGenForm(frm);
		frm.method = 'post'
		frm.action = wb_utils_servlet_url
		if(isOpen==true){
			frm.action += "?isExcludes=true";
		}
		var hasFrame = false;
		var content = parent.document.getElementById("content");
		if (content != undefined) {
			hasFrame = true;					
		}
		if(hasFrame){
			url = window.parent.location.href
			if(url.indexOf('search') == -1){
				frm.url_success.value = 'javascript:window.parent.location.href=\'' + url + '\';document.write(\'\');'
				if(mod_type  != 'SVY'){
					if(frm.que_difficulty.options){
						wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.options[frm.que_difficulty.selectedIndex].value);
					}else{
						wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.value);
					}
				}
			}
		}else frm.url_success.value = gen_get_cookie('url_success')

//		if(!(frm.rdo_que_media01 && frm.rdo_que_media01[1] && frm.rdo_que_media01[1].checked)){
//			if(frm.que_media.value != ''){
//				wb_utils_preloading();
//			}
//		}
		frm.submit();
		if(isOpen){
			if (null!=window.parent && typeof(window.parent)!="undefined" ){
				window.parent.location.reload();
			}　
		}
	}
}

//==============================================================================//
