// ------------------ wizBank Multiple Choice object ------------------- 
// Convention:
//   public functions : use "wbMC" prefix 
//   private functions: use "_wbMC" prefix
/* constructor */
// --------------------- constants ---------------------

MODE_EDIT = 'edit'
MODE_INSERT = 'insert'

// ------------------------- function declaration -------------------------

function wbSC(){
	this.sendFrm = wbMCSendForm
}

//---private function----
function _wbMCGenFrm(frm, lang){
	var mod_type = getUrlParam('mod_type')
	
	frm.que_body_.value = ''
	frm.que_media_.value = ''
	frm.que_html_.value = ''

	if(mod_type != 'SVY'){
		frm.que_diff_.value = ''
	}

	frm.que_status_.value = ''
	frm.que_title_.value = ''


	frm.inter_opt_num_.value = ''

	frm.que_body_.value = frm.qst_text.value;
//	frm.que_media_.value = wbMediaGetFileName(frm.que_media.value);

//	if(frm.ck_qst_text.checked == true){
		frm.que_html_.value = 'Y';
//	}else{
//		frm.que_html_.value = 'N';
//	}

	if(frm.que_shuffle && frm.que_shuffle.checked == true){
		frm.sc_que_shuffle.value = 'Y';
	}else{
		frm.sc_que_shuffle.value = 'N';

	}

	frm.que_title_.value = frm.que_title.value;

	if(mod_type != 'SVY'){
		if(frm.que_difficulty.options && frm.que_status.options){
		   frm.que_diff_.value = frm.que_difficulty.options[frm.que_difficulty.selectedIndex].value;
		   frm.que_status_.value = frm.que_status.options[frm.que_status.options.selectedIndex].value;	
		}else{
	    	frm.que_diff_.value = frm.que_difficulty.value;
		    frm.que_status_.value = frm.que_status.value;
		}
	}else{
		frm.que_status_.value = frm.que_status.value;
	}

}
function _wbMCValidateSVYRequiredFields(frm,lang){
	var lab_title = eval('wb_msg_' + lang + '_topic')
	
	frm.qst_text.value = wbUtilsTrimString(frm.qst_text.value);
	if(!gen_validate_empty_field(frm.qst_text, eval('wb_msg_' + lang + '_que_txt'), lang)){
		return false
	}
	
	frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
	if(!gen_validate_empty_field(frm.que_title, lab_title, lang)){
		return false
	}	
	var empty = 0;
	for(var k = 1; k <= 10; k++){
		opt_body = eval('frm.choice_' + k + '_body');
		opt_score = eval('frm.choice_' + k + '_score');
		frm.opt_body.value = wbUtilsTrimString(frm.opt_body.value);
		frm.opt_score.value = wbUtilsTrimString(frm.opt_score.value);
		if(empty == 0){
			if(opt_body.value.length > 0 && opt_score.value.length > 0){
				if(!gen_validate_positive_integer(opt_score, eval('wb_msg_' + lang + '_score_txt'), lang)){
					opt_score.focus();
					return false;
				}
			}else if(opt_body.value.length == 0 && opt_score.value.length == 0){
				if(k < 3){
					Dialog.alert(eval('wb_msg_' + lang + '_please_select_2_choices'))
					opt_body.focus();
					return false
				}else{
					empty = k;
				}
			}else if(opt_body.value.length == 0){
				Dialog.alert(eval('wb_msg_' + lang + '_input_choice') + k);
				opt_body.focus();
				return false;
			}else if(opt_score.value.length == 0){
				Dialog.alert(eval('wb_msg_' + lang + '_input_choice_score') + k);
				opt_score.focus();
				return false;
			}
		}else{
			if(opt_body.value.length > 0 || opt_score.value.length > 0){
				Dialog.alert(eval('wb_msg_' + lang + '_input_choice') + empty);
				empty_field = eval('frm.choice_' + empty + '_body');
				empty_field.focus();
				return false;
			}
		}
	}	
	return true;
}


function _wbMCValidateRequiredFields(frm, lang){
	var mod_type = getUrlParam('mod_type')
	
	if(mod_type == 'SVY'){
		if(_wbMCValidateSVYRequiredFields(frm,lang) == true){
			return true;
		}
	}else{
		var lab_title = eval('wb_msg_' + lang + '_title')
		
		frm.qst_text.value = wbUtilsTrimString(frm.qst_text.value);
		frm.qst_text.value = removeEditorSpaces(frm.qst_text.value);
		if(frm.qst_text.value == ''){
			Dialog.alert(wb_msg_pls_specify_value + eval('wb_msg_' + lang + '_que_txt'));
			editor.focus();
			return false;
		}
//		if(!gen_validate_empty_field(frm.qst_text, eval('wb_msg_' + lang + '_que_txt'), lang)){
//			return false;
//		}
		
		frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
		if(!gen_validate_empty_field(frm.que_title, lab_title, lang)){
			return false;
		}	
		return true;			
	}	
	

}

function _wbMCValidateForm(frm, lang){
	var mod_type = getUrlParam('mod_type')
	if(!_wbMCValidateRequiredFields(frm, lang)) return false;

	if(frm.que_desc){
		frm.que_desc.value = wbUtilsTrimString(frm.que_desc.value);
		if(frm.que_desc.value.length > 1000){
			Dialog.alert(eval('wb_msg_' + lang + '_desc_too_long'))
			frm.que_desc.focus()
			return false
		}
	}

//	var mediaFilename = frm.que_media.value.toLowerCase()
//
//	if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
//		Dialog.alert(eval('wb_msg_' + lang + '_media_not_support'))
//		if(frm.tmp_que_media01 !== undefined) {
//			frm.tmp_que_media01.focus();
//		} else {
//			frm.que_media.focus();
//		}
//		return false;
//	}

	return true;
}

function setAppletFocus(){
	document.applets['MULTIPLECHOICE'].setFocus();
}

function setFocus(){
	document.frmXml.ans_shuffle.focus();
}

function backHTML(str){
	str = str.replace(/@/g, '\\');
	return str;
}

function set_media_file(media_name, new_media_name){
	media_name = backHTML(media_name);
	document.applets['MULTIPLECHOICE'].add_media_file(media_name.toLowerCase(), new_media_name.toLowerCase());
	return;
}
//-----public function ----
function wbMCSendForm(frm, lang){
	mod_type = getUrlParam('mod_type');
	if (frm.editor) {
		editor.sync();
	}
	if(_wbMCValidateForm(frm, lang)){
		_wbMCGenFrm(frm, lang);
		frm.method = 'post';
		frm.action = wb_utils_servlet_url;

		var hasFrame = false;
		var content = parent.document.getElementById("content");
		if (content != undefined) {
			hasFrame = true;					
		}
		if(hasFrame){
			url = window.parent.location.href
			if(url.indexOf('search') == -1){

				frm.url_success.value = 'javascript:window.parent.location.href=\'' + url + '\';document.write(\'\');'
				if(mod_type != 'SVY'){
					if(frm.que_difficulty.options && frm.que_status.options){
						wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.options[frm.que_difficulty.selectedIndex].value);
					}else{
						wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.value);
					}
				}
			}
		}else{
			frm.url_success.value = gen_get_cookie('url_success');
		}
		
//		if(!(frm.rdo_que_media01 && frm.rdo_que_media01[1] && frm.rdo_que_media01[1].checked)){
//			if(frm.que_media.value != ''){
//				wb_utils_preloading();
//			}
//		}
		frm.submit();

	}
}