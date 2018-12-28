// ------------------ wizBank Fill in the Blank object ------------------- 
// Convention:
//   public functions : use "wbFB" prefix 
//   private functions: use "_wbFB" prefix
/* constructor */
// --------------------- constants ---------------------

MODE_EDIT = 'edit'
MODE_INSERT = 'insert'

// -------------- constants -------------- 

MEDIA_TYPE_GIF = 'image/gif'
MEDIA_TYPE_JPG = 'image/jpg'
MEDIA_TYPE_SWF = 'application/x-shockwave-flash'
MEDIA_TYPE_UNKNOWN = 'unknown'
SEPARATOR = "[|]";
BLANK_SEPARATOR = "[blank]";
var num_blank = 0;
var que_body = "", num_option = "", ans_lst = "", score_lst = "",explanation_lst = "";

// ------------------------- function declaration ------------------------- 
/* constructor */
function wbFB(){ this.sendFrm = wbFBSendForm }

function _wbFBValidateForm(frm, lang, wb_common_img_path){
	if (frm.editor) {
		editor.sync();
	}
	
	var mod_type = getUrlParam("mod_type");
	
	if(frm.que_media != undefined){
		var mediaFilename = frm.que_media.value.toLowerCase();
		var mediaFileType = mediaFilename.substr(mediaFilename.lastIndexOf(".") + 1, mediaFilename.length);
		if(mediaFileType != "" && mediaFileType != 'gif' && mediaFileType != "jpg" && mediaFileType != "jpeg" && mediaFileType != "png"){
			alert(eval('wb_msg_media_not_support_fb'));
			frm.que_media.focus();
			return false;
		}
	}
	if(frm.qst_body) {
		var content = removeEditorSpaces(frm.qst_body.value);
		if(content == ''){
			alert(wb_msg_pls_specify_value + eval('wb_msg_' + lang + '_que_txt'));
			editor.focus();
			return false;
		}
		if(getChars(content) > 2000){
			Dialog.alert(eval('wb_msg_' + lang + '_que_txt') + fetchLabel('label_core_training_management_372'),function(){
				editor.focus();
			});
			return false;
		}
		que_body = content;
		var blank = content.split('FB_blank[|]_answer=');
		if(blank.length > 0) {
			for(i = 0; i < blank.length; i++){
				if(blank[i].indexOf('[FB_split~]') == 0) {
					var ans = blank[i].split('[FB_split~]');
					if(ans.length >= 4) {
						num_blank++;
						num_option += 1 + SEPARATOR;
						ans_lst += ans[1] + SEPARATOR;
						score_lst += ans[2] + SEPARATOR;
						explanation_lst += ans[3] + SEPARATOR;
						
						var source_body = '<img id="'+ans[2]+'" title="'+ans[1]+'" alt="FB_blank[|]_answer=[FB_split~]'+ans[1]+'[FB_split~]'+ans[2]+'[FB_split~]'+ans[3]+'[FB_split~]" src="'+location.protocol+'//'+location.host+wb_utils_app_base+'wb_image/ans.gif" />';
						que_body = que_body.replace(source_body, BLANK_SEPARATOR);
						
					}
				}
			}
		}
		
		while(que_body.indexOf('<img ') >= 0){
			var temp_1 =  que_body.substring(0,que_body.indexOf('<img ')) 
			var temp_2 =  que_body.substring(que_body.indexOf('<img '),que_body.length) 

			var temp_3 = temp_2.substring(temp_2.indexOf('/>') +2,temp_2.length);
			

			que_body = temp_1 +BLANK_SEPARATOR+ temp_3 ;
			
		}
	}
	if(frm.que_body_1) {
		frm.que_body_1.value = wbUtilsTrimString(frm.que_body_1.value);
		if (!gen_validate_empty_field(frm.que_body_1, eval('wb_msg_' + lang + '_que_txt'), lang)) {
			return false
		}
		
		if(getChars(frm.que_body_1.value) > 10000){
			Dialog.alert(eval('wb_msg_question') + fetchLabel('label_core_requirements_management_66'),function(){
				frm.que_body_1.focus();
			});
			return false;
		}
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
	
	if((mod_type != 'SVY' && mod_type != 'EVN') && num_blank < 1) {

		alert(eval('wb_msg_' + lang + '_no_blank'));
		return false;
	}
	
//	if((mod_type != 'SVY' ) && document.applets["fbAnswer"].returnNumBlank() < 1){
//		alert(eval('wb_msg_' + lang + '_no_blank'));
//		return false;
//	}

//	if((mod_type != 'SVY') && document.applets["fbAnswer"].returnNumOption() == ''){
//		document.applets["fbQuestion"].focus();
//		return false;
//	}
	
	if (frm.maxChar) {
		frm.maxChar.value = wbUtilsTrimString(frm.maxChar.value);
	}
	
	return true;
}
function _wbFBGenSVYForm(frm){
	frm.que_body_.value = frm.que_body_1.value;
	if(frm.que_media != undefined){
		frm.que_media_.value = wbMediaGetFileName(frm.que_media.value);
	}
/*	if(frm.asHTML.checked == true){*/
		frm.que_html_.value = 'Y';
/*	}else{
		frm.que_html_.value = 'N';
	}*/	
	frm.inter_type_.value = 'FB[|]';
	frm.inter_type_.value = wbUtilsTrimString(frm.inter_type_.value);
	frm.inter_num_.value = '1';
	frm.inter_opt_num_.value = '1';	
	frm.que_status_.value = frm.que_status.value;	
	frm.que_title_.value = frm.que_title.value;
	frm.que_desc_.value = frm.que_desc.value;	
	return;
}

function _wbFBGenForm(frm){
	var mod_type = getUrlParam('mod_type')
	if(mod_type == 'SVY' || mod_type == 'EVN'){
		//do SVY
		_wbFBGenSVYForm(frm);
	}else{
		frm.que_body_.value = que_body;//document.applets["fbQuestion"].returnQuestionBody();
//		frm.que_media_.value = wbMediaGetFileName(frm.que_media.value);
//		if(frm.asHTML.checked == true){
			frm.que_html_.value = 'Y';
//		}else{
//			frm.que_html_.value = "N";
//		}
		for(i = 0; i < num_blank; i++){
			frm.inter_type_.value += "FB[|]";
			frm.inter_type_.value = gen_trim_string(frm.inter_type_.value);
		}
		frm.inter_num_.value = num_blank;//document.applets["fbAnswer"].returnNumBlank();
		frm.inter_opt_num_.value = num_option;//document.applets["fbAnswer"].returnNumOption();
//		frm.inter_score_.value = document.applets["fbAnswer"].returnMaxScore();
//		frm.inter_length_.value = document.applets["fbAnswer"].returnMaxCharValue();	
		frm.inter_opt_body_.value = ans_lst;//document.applets["fbAnswer"].returnAnswerValue();
		frm.inter_opt_score_.value = score_lst;//document.applets["fbAnswer"].returnScoreValue();
		frm.inter_opt_exp_.value = explanation_lst;//document.applets["fbAnswer"].returnExplanationValue();
//		frm.inter_opt_case_.value = document.applets["fbAnswer"].returnCaseSensitive();
//		frm.inter_opt_space_.value = document.applets["fbAnswer"].returnSpaceSensitive();
//		frm.inter_opt_type_.value = document.applets["fbAnswer"].returnTypeValue();
		if(frm.que_difficulty.options){
		  frm.que_diff_.value = frm.que_difficulty.selectedIndex + 1;
		}else{
			frm.que_diff_.value = frm.que_difficulty.value;
		}
		if(frm.que_status.options){
		   frm.que_status_.value = frm.que_status.options[frm.que_status.selectedIndex].value;		
		}else{
			frm.que_status_.value = frm.que_status.value;
		}
		frm.que_title_.value = frm.que_title.value;
		frm.que_desc_.value = frm.que_desc.value;						
	}
	return;
}

function wbFBSendForm(frm, lang, mod_type, wb_common_img_path,isOpen){
	var mod_type = getUrlParam('mod_type')
	if(_wbFBValidateForm(frm, lang, wb_common_img_path)){
		_wbFBGenForm(frm);
		frm.method = 'post'
		frm.action = wb_utils_servlet_url;
		if(isOpen){  //父级窗口为弹出窗模式
			frm.action +=  "?isExcludes=true";
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
				if(mod_type  != 'SVY' && mod_type != 'EVN'){
					   if(frm.que_difficulty.options){
						 wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.options[frm.que_difficulty.selectedIndex].value);
					   }else{
                          wb_utils_set_cookie('prev_res_difficulty', frm.que_difficulty.value);
					   }
				}
			}
		}else frm.url_success.value = gen_get_cookie('url_success')


//		if(!(frm.rdo_que_media01 && frm.rdo_que_media01[1] && frm.rdo_que_media01[1].checked)){
//			if(frm.que_media.value != ""){
//				wb_utils_preloading(document["gen_btn_save1"], lang);
//			}
//		}
		frm.submit();
		/**
		 * 后面这块代码，影响了火狐浏览器提交上面的表单(因不知去掉有什么影响。所以加上判断。当为svy类型时不跑) 公共调查问卷也有该问题
		 */
		if(isOpen && mod_type != 'SVY' && mod_type != 'EVN'){
			if (null!=window.parent && typeof(window.parent)!="undefined" ){
				window.parent.location.reload();
			}　
		}
	}
}

//==============================================================================//
function noOption(blankNum, lang){
	alert(eval('wb_msg_' + lang + '_no_option_for_blank') + blankNum);
	return;
}

function check_type(){
	if(document.frmXml.type.selectedIndex == 1){
		document.frmXml.ss.checked = false;
		document.frmXml.cs.checked = false;
	}
	return;
}

function addBlank(blankNum){
	document.applets['fbAnswer'].addBlank(blankNum);
	return;
}

function delBlank(blankNum){
	document.applets['fbAnswer'].delBlank(blankNum);
	return;
}

function showOption(answerValue, explanationValue, scoreValue, typeValue, caseSensitive, spaceSensitive){
	if(answerValue != ''){
		document.frmXml.answer.value = answerValue;
		document.frmXml.explanation.value = explanationValue;
		document.frmXml.score.value = scoreValue;
		document.frmXml.type.selectedIndex = typeValue;

		if(caseSensitive == 'true'){
			document.frmXml.cs.checked = true;
		}else{
			document.frmXml.cs.checked = false;
		}
		if(spaceSensitive == 'true'){
			document.frmXml.ss.checked = true;
		}else{
			document.frmXml.ss.checked = false;
		}
	}

	document.frmXml.answer.focus();
	return;
}

function resetAnsList(){
	document.applets['fbLabel'].setLabel('-1');
	document.applets['fbAnswer'].resetAnsList();
	return;
}

function getAnsList(blankNum){
	document.applets['fbLabel'].setLabel(blankNum);
	document.applets['fbAnswer'].getAnsList(blankNum);
	return;
}

function saveAnswer(lang){
	if(document.applets['fbQuestion'].getBlankNum() <= 0){
		alert(eval('wb_msg_' + lang + '_choose_blank'));
		return;
	}

	if(document.frmXml.answer.value == ''){
		document.frmXml.answer.focus();
		return;
	}

	if(document.frmXml.explanation.value == ''){
		document.frmXml.explanation.value = eval('wb_msg_' + lang + '_no_exp');
	}

	if(!gen_validate_integer(document.frmXml.score, eval('wb_msg_' + lang + '_score_txt'), lang)){
		return;
	}

	if(document.frmXml.type.selectedIndex == 1 && !gen_validate_float(document.frmXml.answer, eval('wb_msg_' + lang + '_answer'), lang)){
		return;
	}

	if(document.frmXml.type.selectedIndex == 1){
		document.applets['fbAnswer'].saveAnswerData(document.applets['fbQuestion'].getBlankNum(), document.frmXml.answer.value, document.frmXml.explanation.value, document.frmXml.score.value, 'Number', document.frmXml.cs.checked, document.frmXml.ss.checked);
	}else{
		document.applets['fbAnswer'].saveAnswerData(document.applets['fbQuestion'].getBlankNum(), document.frmXml.answer.value, document.frmXml.explanation.value, document.frmXml.score.value, 'Text', document.frmXml.cs.checked, document.frmXml.ss.checked);
	}

	resetOption();
	document.frmXml.answer.focus();
	return;
}

function resetOption(){
	document.frmXml.answer.value = '';
	document.frmXml.explanation.value = '';
	document.frmXml.score.value = '1';
	document.frmXml.type.selectedIndex = 0;
	document.frmXml.cs.checked = false;
	document.frmXml.ss.checked = false;
	return;
}

function setBlankNum(){
	blankNumMaxLength = document.applets['fbQuestion'].getBlankNum();
	return;
}

function setMaxChar(charLength){
	if(charLength != -1){
		document.frmXml.maxChar.value = charLength;
	}else{
		document.frmXml.maxChar.value = '';
	}
	return;
}

function checkMaxLength(lang){
	if(blankNumMaxLength > 0 && document.frmXml.maxChar.value != ''){
		if(!gen_validate_integer(document.frmXml.maxChar, eval('wb_msg_' + lang + '_input_number'), lang)){
			return;
		}

		if(document.applets['fbAnswer'].returnMaxChar(blankNumMaxLength) > document.frmXml.maxChar.value){
			alert(eval('wb_msg_' + lang + '_max_char'))
			document.frmXml.maxChar.focus();
			return;
		}
		document.applets['fbAnswer'].saveMaxLength(blankNumMaxLength, document.frmXml.maxChar.value);
	}

	blankNumMaxLength = 0;
	return;
}