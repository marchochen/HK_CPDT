// ------------------ wizBank Multiple Choice object ------------------- 
// Convention:
//   public functions : use "wbMC" prefix 
//   private functions: use "_wbMC" prefix
/* constructor */
// --------------------- constants ---------------------

MODE_EDIT = 'edit'
MODE_INSERT = 'insert'
var DELIMITER = '[|]';

// ------------------------- function declaration -------------------------

function wbMC(){
	this.sendFrm = wbMCSendForm
}

//---private function----
function _wbMCGenFrm(frm, lang){
	var mod_type = getUrlParam('mod_type')
	
	frm.que_body_.value = ''
	frm.que_media_.value = ''
	frm.que_html_.value = ''

	if(mod_type != 'SVY' && mod_type != 'EVN'){
		frm.que_diff_.value = ''
	}

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
	frm.inter_opt_score_.value = ''
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

	if(frm.ans_shuffle && frm.ans_shuffle.checked == true){
		frm.inter_shuffle_.value = 'Y';
	}else{
		frm.inter_shuffle_.value = 'N';
	}

	if(mod_type != 'SVY' && mod_type != 'EVN'){
		frm.inter_score_.value = frm.que_score.value;
	}else{
		frm.inter_score_.value = '1';
	}

	frm.que_title_.value = frm.que_title.value;

	if(mod_type != 'SVY' && mod_type != 'EVN'){
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

	frm.inter_type_.value = 'MC';
	frm.inter_num_.value = 1;
	frm.que_desc_.value = frm.que_desc.value;

	var numoption = 0;
	var answervalue = '';
	var htmllist = '';
	var score = '';
	
	if (frm.mc_option_chx && frm.mc_option.length > 0) {
		for (i = 0; i < frm.mc_option.length; i++) {
			if (frm.mc_option[i].value.length > 0) {
				numoption++;
				answervalue += frm.mc_option[i].value;
				htmllist += 'Y';
			} else {
				htmllist += 'N';
			}
			answervalue += DELIMITER;
			htmllist += DELIMITER;
		}
	}
	if (frm.mc_option_chx && frm.mc_option_chx.length > 0) {
		for (i = 0; i < frm.mc_option_chx.length; i++) {
			if (frm.mc_option_chx[i].checked) {
				score += frm.inter_score_.value;
			} else {
				score += '0';
			}
			score += DELIMITER;
		}
	}
	
	if(numoption > 0){
		frm.inter_opt_num_.value = numoption;
		
		frm.inter_opt_body_.value = answervalue;
		
		frm.inter_opt_html_.value = htmllist;
		
		frm.inter_opt_score_.value = score;

	/*}

	if(document.applets && document.applets['MULTIPLECHOICE']){
		frm.inter_opt_num_.value = document.applets['MULTIPLECHOICE'].return_numoption();
		
		frm.inter_opt_body_.value = document.applets['MULTIPLECHOICE'].return_answervalue();

		frm.inter_opt_media_.value = document.applets['MULTIPLECHOICE'].return_mediafilepath();

		frm.inter_opt_html_.value = document.applets['MULTIPLECHOICE'].return_htmllist();

		frm.inter_opt_score_.value = document.applets['MULTIPLECHOICE'].return_answer(frm.inter_score_.value);
*/
	}else{
		var optNum = 0;
		for (var k = 1; k <= 10; k++){
			opt_body = eval('frm.choice_' + k + '_body');
			opt_score = eval('frm.choice_' + k + '_score');
			if(opt_body.disabled == false && opt_score.disabled == false
				&& opt_body.value.length > 0 && opt_score.value.length > 0){
				optNum = optNum + 1;

				frm.inter_opt_body_.value = frm.inter_opt_body_.value + opt_body.value + '[|]';
				frm.inter_opt_score_.value = frm.inter_opt_score_.value + opt_score.value + '[|]';
				frm.inter_opt_media_.value = frm.inter_opt_media_.value + '[|]';
				frm.inter_opt_html_.value = frm.inter_opt_html_.value + 'N[|]';
			}
			frm.inter_opt_num_.value = optNum;
		}
	}
	

	for(i = 1; i <= frm.inter_opt_num_.value; i++){
		if(frm.exp_text){
			frm.inter_opt_exp_.value += frm.exp_text.value + '[|]';
		}else{
			frm.inter_opt_exp_.value += '[|]';
		}
	}
	if(mod_type != 'SVY' && mod_type != 'EVN'){
		if(frm.score_choice.selectedIndex == 0){
			frm.inter_logic_.value = 'AND'
		}else if(frm.score_choice.selectedIndex == 1){
			frm.inter_logic_.value = 'OR'
		}else{
			frm.inter_logic_.value = 'SINGLE'
		}
	} else {
		if(frm.que_mc_type) {
			frm.inter_logic_.value = frm.que_mc_type.value;
		}
	}
	return false;
}
function _wbMCValidateSVYRequiredFields(frm,lang){
	var mod_type = getUrlParam('mod_type')
	var lab_title = eval('wb_msg_' + lang + '_title')
	frm.qst_text.value = wbUtilsTrimString(frm.qst_text.value);
	if(!gen_validate_empty_field(frm.qst_text, eval('wb_msg_' + lang + '_que_txt'), lang)){
		return false
	}
	
	if(frm.qst_text && getChars(frm.qst_text.value) > 10000){
		Dialog.alert(eval('wb_msg_question') + fetchLabel('label_core_requirements_management_66'),function(){
			frm.qst_text.focus();
		});
		return false;
	}
	
	if (frm.que_title) {
		frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
		if(!gen_validate_empty_field(frm.que_title, lab_title, lang)){
			return false
		}	
	}
	var empty = 0;
	for(var k = 1; k <= 10; k++){
		opt_body = eval('frm.choice_' + k + '_body');
		opt_score = eval('frm.choice_' + k + '_score');
		opt_body.value = wbUtilsTrimString(opt_body.value);
		opt_score.value = wbUtilsTrimString(opt_score.value);
		if(empty == 0){
			if(opt_body.value.length > 0 && opt_score.value.length > 0){
				if(!gen_validate_positive_int(opt_score, eval('wb_msg_' + lang + '_score_txt'), lang)){
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
				if(mod_type == 'SVY'){
					opt_score.value = 0;
				}else{
					Dialog.alert(eval('wb_msg_' + lang + '_input_choice_score') + k);
					opt_score.focus();
					return false;
				}
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
	// check other option
	opt_body_other = eval('frm.choice_11_body');
	opt_score_other = eval('frm.choice_11_score');
	if(opt_body_other && opt_score_other && opt_body_other.disabled == false) {
		opt_body_other.value = wbUtilsTrimString(opt_body_other.value);
		opt_score_other.value = wbUtilsTrimString(opt_score_other.value);
		var lab_other_option = 'other';
		if(frm.lab_other_option && frm.lab_other_option.value.length > 0) {
			lab_other_option = frm.lab_other_option.value;
		} 
		if(opt_body_other.value.length == 0) { 
			Dialog.alert(eval('wb_msg_' + lang + '_input_choice') + '\"'+ lab_other_option + '\"');
			opt_body_other.focus();
			return false;
		}
		if(!gen_validate_positive_int(opt_score_other, eval('wb_msg_' + lang + '_score_txt'), lang)){
			opt_score_other.focus();
			return false;
		}
		if(opt_score_other.value.length == 0) { 
			Dialog.alert(eval('wb_msg_' + lang + '_input_choice_score'));
			opt_score_other.focus();
			return false;
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
	} else if (mod_type == 'EVN') {
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
		
		if (frm.mc_option_chx && frm.mc_option.length > 0) {
			for (i = 0; i < frm.mc_option.length; i++) {
				frm.mc_option[i].value = removeEditorSpaces(frm.mc_option[i].value);
				var before = frm.mc_option[i].value;
				if(getChars(before) > 400){
					var i1 = i+1;
					Dialog.alert(eval('wb_msg_' + lang + '_answer') + " " + i1 + " " + fetchLabel("label_core_training_management_382"),function(){
						frm.mc_option[i].focus();
					});
					return false;
				}
				if (i + 1 != frm.mc_option.length) {
					frm.mc_option[i + 1].value = removeEditorSpaces(frm.mc_option[i + 1].value);
					var after = frm.mc_option[i + 1].value;
					if (before == '' && before.length <= 0 && after != '' && after.length > 0) {
						Dialog.alert(wb_msg_pls_specify_option_value + document.getElementsByName('option_label')[i].innerText + wb_msg_pls_specify_option_value_two);
						optionFocus(i, frm);
						return false;
					}
				}
				if (frm.mc_option_chx[i].checked && before == '' && before.length <= 0) {
					Dialog.alert(wb_msg_pls_specify_option_value + document.getElementsByName('option_label')[i].innerText + wb_msg_pls_specify_option_value_two);
					optionFocus(i, frm);
					return false;
				}
			}
		}
		var mod_type = getUrlParam('mod_type')
		if(mod_type != 'SVY' && mod_type != 'EVN'){
			frm.que_score.value = wbUtilsTrimString(frm.que_score.value);
			if(!gen_validate_integer(frm.que_score, eval('wb_msg_' + lang + '_score_txt'), lang) || !gen_validate_empty_field(frm.que_score, eval('wb_msg_' + lang + '_score_txt'),lang)){
				return false;
			}
			
			if(frm.que_score.value <= 0 ){
				Dialog.alert(fetchLabel("label_core_training_management_355"));
				return false;
			}
		}
		if (frm.que_title) {
			frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
			if (!gen_validate_empty_field(frm.que_title, lab_title, lang)) {
				return false;
			}
		}
		
		var umanswer = 0;
		if (frm.mc_option_chx && frm.mc_option_chx.length > 0) {
			for (i = 0; i < frm.mc_option_chx.length; i++) {
				if (frm.mc_option_chx[i].checked) {
					umanswer++;
				}
			}
		}

		if(/*document.applets['MULTIPLECHOICE'].return_numanswer()*/umanswer <= 0){
			Dialog.alert(eval('wb_msg_' + lang + '_choose_correct_answer'))
			return false
		}		
		if(/*document.applets['MULTIPLECHOICE'].return_numanswer()*/umanswer > 1 &&/*(*/ frm.score_choice.selectedIndex == 2/*|| frm.score_choice[3].checked)*/){
			Dialog.alert(eval('wb_msg_' + lang + '_more_than_one_answer'))
			return false
		}	
		
		var numoption = 0;
		if (frm.mc_option_chx && frm.mc_option.length > 0) {
			for (i = 0; i < frm.mc_option.length; i++) {
				if (frm.mc_option[i].value.length > 0) {
					numoption++;
				}
			}
		}

		if(/*document.applets['MULTIPLECHOICE'].return_numoption()*/numoption < 2){
			Dialog.alert(eval('wb_msg_' + lang + '_please_select_2_choices'))
			return false
		}
		return true;			
	}	
	

}

function _wbMCValidateForm(frm, lang){
	
	if(!_wbMCValidateRequiredFields(frm, lang)) return false;

	if(getChars(frm.que_title.value) > 80){
		Dialog.alert(eval('wb_msg_usr_title_too_long'),function(){
			frm.que_title.focus();
		});
		return false;
	}
	if(getChars(frm.qst_text.value) > 2000){
		Dialog.alert(eval('wb_msg_'+lang+'_que_txt')+eval('label_tm.label_core_training_management_372'),function(){
			frm.qst_text.focus();
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
//		alert(eval('wb_msg_' + lang + '_media_not_support'))
//		if(frm.tmp_que_media01 !== undefined) {
//			frm.tmp_que_media01.focus();
//		} else {
//			frm.que_media.focus();
//		}
//		return false;
//	}

	
	if (frm.exp_text) {
		frm.exp_text.value = wbUtilsTrimString(frm.exp_text.value);
	}

	return true;
}

function setAppletFocus(){
//	document.applets['MULTIPLECHOICE'].setFocus();
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
//	document.applets['MULTIPLECHOICE'].add_media_file(media_name, new_media_name);
	return;
}
//-----public function ----
function wbMCSendForm(frm, lang,isOpen){
	mod_type = getUrlParam('mod_type');
	
	if (frm.editor) {
		editor.sync();
	}
	if (frm.mc_option_editor_1) {
		mc_option_editor_1.sync();
	}
	if (frm.mc_option_editor_2) {
		mc_option_editor_2.sync();
	}
	if (frm.mc_option_editor_3) {
		mc_option_editor_3.sync();
	}
	if (frm.mc_option_editor_4) {
		mc_option_editor_4.sync();
	}
	if (frm.mc_option_editor_5) {
		mc_option_editor_5.sync();
	}
	if (frm.mc_option_editor_6) {
		mc_option_editor_6.sync();
	}
	if (frm.mc_option_editor_7) {
		mc_option_editor_7.sync();
	}
	if (frm.mc_option_editor_8) {
		mc_option_editor_8.sync();
	}
	if (frm.mc_option_editor_9) {
		mc_option_editor_9.sync();
	}
	if (frm.mc_option_editor_10) {
		mc_option_editor_10.sync();
	}
	
	if(_wbMCValidateForm(frm, lang)){

		_wbMCGenFrm(frm, lang);
		frm.method = 'post';
		if(mod_type == 'EVN' || mod_type == 'SVY'){
			frm.action = wb_utils_servlet_url + "?cmd=que_mc&isExcludes=true";
		}else{
			frm.action = wb_utils_servlet_url;
			if(isOpen){
				frm.action +=  "?isExcludes=true";
			}
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
				if(mod_type != 'SVY' && mod_type != 'EVN'){
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
		/**
		 * 后面这块代码，影响了火狐浏览器提交上面的表单(因不知去掉有什么影响。所以加上判断。当为svy类型时不跑)公共调查问卷也有该问题
		 */
		if(isOpen && mod_type != 'SVY'  && mod_type != 'EVN'){
			if (null!=window.parent && typeof(window.parent)!="undefined" ){
				window.parent.location.reload();
			}　
		}
	}
}

function optionFocus(i, frm){
	if (i == 0 && frm.mc_option_editor_1) {
		mc_option_editor_1.focus();
	}
	if (i == 1 && frm.mc_option_editor_2) {
		mc_option_editor_2.focus();
	}
	if (i == 2 && frm.mc_option_editor_3) {
		mc_option_editor_3.focus();
	}
	if (i == 3 && frm.mc_option_editor_4) {
		mc_option_editor_4.focus();
	}
	if (i == 4 && frm.mc_option_editor_5) {
		mc_option_editor_5.focus();
	}
	if (i == 5 && frm.mc_option_editor_6) {
		mc_option_editor_6.focus();
	}
	if (i == 6 && frm.mc_option_editor_7) {
		mc_option_editor_7.focus();
	}
	if (i == 7 && frm.mc_option_editor_8) {
		mc_option_editor_8.focus();
	}
	if (i == 8 && frm.mc_option_editor_9) {
		mc_option_editor_9.focus();
	}
	if (i == 9 && frm.mc_option_editor_10) {
		mc_option_editor_10.focus();
	}
}