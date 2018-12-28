// ------------------ wizBank True False object ------------------- 
// Convention:
//   public functions : use "wbTF" prefix 
//   private functions: use "_wbTF" prefix
// --------------------- constants ---------------------

MODE_EDIT = 'edit'
MODE_INSERT = 'insert'

// ------------------------- function declaration ------------------------- 
/* constructor */
function wbTF(){ this.sendFrm = wbTFSendForm }

//---private function----
function _wbTFGenFrm(frm, lang){
	frm.que_body_.value = ''
	frm.que_media_.value = ''
	frm.que_html_.value = ''
	frm.que_diff_.value = ''
	frm.que_status_.value = ''
	frm.que_title_.value = ''
	frm.que_desc_.value = ''
	frm.inter_num_.value = ''
	frm.inter_score_.value = ''
	frm.inter_type_.value = ''
	frm.inter_shuffle_.value = ''
	frm.inter_opt_num_.value = ''
	frm.inter_opt_body_.value = ''
	frm.inter_opt_media_.value = ''
	frm.inter_opt_cond_.value = ''
	frm.inter_opt_exp_.value = ''
	frm.inter_opt_html_.value = ''
	frm.inter_logic_.value = ''
	frm.que_body_.value = frm.qst_text.value;
//	frm.que_media_.value = wbMediaGetFileName(frm.que_media.value);

//	if(frm.ck_qst_text.checked == true){
		frm.que_html_.value = 'Y';
//	}else{
//		frm.que_html_.value = 'N';
//	}

	if(frm.ans_shuffle.checked == true){
		frm.inter_shuffle_.value = 'Y';
	}else{
		frm.inter_shuffle_.value = 'N';
	}

	frm.inter_score_.value = frm.que_score.value;
	frm.que_title_.value = frm.que_title.value;


    if(frm.que_difficulty.options && frm.que_status.options){
	  frm.que_diff_.value = frm.que_difficulty.options[frm.que_difficulty.selectedIndex].value;
	  frm.que_status_.value = frm.que_status.options[frm.que_status.options.selectedIndex].value;
	}else{
       frm.que_diff_.value = frm.que_difficulty.value;
	   frm.que_status_.value = frm.que_status.value;
	}

	frm.inter_type_.value = 'TF';
	frm.inter_num_.value = 1;
	frm.que_desc_.value = frm.que_desc.value;

	frm.inter_opt_num_.value = '2';

	frm.inter_opt_body_.value = 'True[|]False[|]';

	frm.inter_opt_media_.value = '[|][|]';

	frm.inter_opt_html_.value = 'n[|]n[|]';

	// get score
	frm.inter_opt_score_.value = frm.inter_opt_score_.value.replace('#', frm.que_score.value);

	frm.inter_opt_exp_.value = "";
//	for(i = 1; i <= frm.inter_opt_num_.value; i++){
//		frm.inter_opt_exp_.value += frm.exp_text.value + '[|]';
//	}
	if(frm.score_choice.selectedIndex == 0){
		frm.inter_logic_.value = 'AND'
	}else if(frm.score_choice.selectedIndex == 1){
		frm.inter_logic_.value = 'OR'
	}else{
		frm.inter_logic_.value = 'SINGLE'
	}
}

function _wbTFValidateRequiredFields(frm, lang){
	frm.qst_text.value = wbUtilsTrimString(frm.qst_text.value);
	frm.qst_text.value = removeEditorSpaces(frm.qst_text.value);
	if(frm.qst_text.value == ''){
		Dialog.alert(wb_msg_pls_specify_value + eval('wb_msg_' + lang + '_que_txt'));
		editor.focus();
		return false;
	}
	if(getChars(frm.qst_text.value) > 2000){
		Dialog.alert(eval('wb_msg_' + lang + '_que_txt') + fetchLabel('label_core_training_management_372'),function(){
			editor.focus();
		});
		return false;
	}
	
	frm.que_score.value = wbUtilsTrimString(frm.que_score.value);
	if(!gen_validate_integer(frm.que_score, eval('wb_msg_' + lang + '_score_txt'), lang) || !gen_validate_empty_field(frm.que_score, eval('wb_msg_' + lang + '_score_txt'),lang)){
		return false;
	}
	frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
	if(!gen_validate_empty_field(frm.que_title, eval('wb_msg_' + lang + '_title'), lang)){
		return false
	}

	return true;
}

function _wbTFValidateForm(frm, lang){

	if(!_wbTFValidateRequiredFields(frm, lang)){
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
	//问答题、判断题分数判断
	if(frm.que_score.value < 0 ){
		Dialog.alert(fetchLabel("label_core_training_management_355"));
		return false;
	}

	if (frm.exp_text) {
		frm.exp_text.value = wbUtilsTrimString(frm.exp_text.value);
	}
	
	return true;
}

function setAppletFocus(){
	document.applets['MULTIPLECHOICE'].setFocus();
}

function setFocus(){
	document.frmXml.ans_shuffle.focus();
}

function backHTML(str){
	str = str.replace(/@/g, '\\')
	return str
}

function set_media_file(media_name, new_media_name){
	media_name = backHTML(media_name)
	return;
}
//-----public function ----
function wbTFSendForm(frm, lang, isOpen){
	if (frm.editor) {
		editor.sync();
	}
	if(_wbTFValidateForm(frm, lang)){
		_wbTFGenFrm(frm, lang);
		frm.method = 'post';
		frm.action = wb_utils_servlet_url;
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
				if(frm.que_difficulty.options && frm.que_status.options){
				    wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.options[frm.que_difficulty.selectedIndex].value);
				}else{
                     wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.value);
				}
			}
		}else frm.url_success.value = gen_get_cookie('url_success');

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