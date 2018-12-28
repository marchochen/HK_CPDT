// ------------------ wizBank Matching object ------------------- 
// Convention:
//   public functions : use "wbMT" prefix 
//   private functions: use "_wbMT" prefix
/* constructor */
// --------------------- constants ---------------------

MODE_EDIT = 'edit'
MODE_INSERT = 'insert'

// --------------------- global vars to be initialized ---------------------

gUrlSubmit = ''
gFrmMethod = ''
gCmd = ''
gCharset = ''
gObjective = ''
gQuestionType = ''
gMode = ''

// ------------------------- for edit mode only ------------------------- 
gQuestionId = ''
gQuestionTimestamp = ''
// -------------- constants -------------- 
MEDIA_TYPE_GIF = 'image/gif'
MEDIA_TYPE_JPG = 'image/jpg'
MEDIA_TYPE_SWF = 'application/x-shockwave-flash'
MEDIA_TYPE_UNKNOWN = 'unknown'

/* constructor */
function wbMT(){
	this.sendFrm = wbMTSendForm
	this.confirm_del = wbMTConfirmDel
	this.same_def = wbMTSameDef
	this.score_err = wbMTScoreError
	this.connect_err = wbMTConnectError
	this.dimen_err = wbMTDimensionError
	
	this.add_option = wbMtAddOption	
	this.del_option = wbMtDeleteOption
}

//var source_num = 2;
//var target_num = 2;
var source_media = '';
var target_media = '';
function wbMtAddOption(){
	if(Number(source_num) < 30){
		source_num = Number(source_num) + 1;
		var html = '';
		
		html += '<div id="s_div_'+source_num+'"  style="padding:0 0 10px 0">';
		html += '<table>';
		html += '<tr>';
		html += '<td width="2%">'+source_num+'.</td>';
		html += '<td width="40%"><input type="text" name="source_text_'+source_num+'" style="width:350px;" value="source '+source_num+'" class="wzb-inputText" maxlength="300"/></td>';
		html += '<td width="10%"><input type="text" name="answer_num_'+source_num+'" style="width:50px;" class="wzb-inputText" maxlength="3"/></td>';
		html += '<td width="10%"><input type="text" name="score_'+source_num+'" style="width:50px;" class="wzb-inputText" maxlength="3"/></td>';
		html += '<td width="30%"></td></tr>';
		html += '<tr>';
		html += '<td></td>';
		html += '<td>';
		html += '<div style=" margin: 2px 0 0 0; height: 0;" class="file"><input onchange="$(this).siblings(\'.file_txt\').val(this.value);" type="file" name="source_media_'+source_num+'" class="file_file" onfocus="" onblur="" onclick="" title="" value="" id=""><input value="'+fetchLabel("no_select_file")+'" class="file_txt"><div class="file_button-blue">'+fetchLabel("usr_browse")+'</div></div>';
		html += '<br/><span>'+fetchLabel("lab_mt_file_type")+'</span>';
		html += '<br/><span>'+fetchLabel("lab_mt_des")+'</span></td>';
		html += '</tr>';
		html += '</table>';
		html += '</div>';
		$(html).appendTo("#source_div");
		
		target_num = Number(target_num) + 1;
		var html = '';
		html += '<div id="t_div_'+target_num+'" style="padding:10px 0 0 0">';
		html += '<table>';
		html += '<tr>';
		html += '<td width="2%">'+target_num+'.</td>';
		html += '<td width="60%"><input type="text" name="target_text_'+target_num+'" style="width:350px;" value="target '+target_num+'" class="wzb-inputText" maxlength="300"/></td>';
		html += '<td></td></tr>';
		html += '<tr>';
		html += '<td></td>';
		html += '<td>';
		html += '<div style=" margin: 2px 0 0 0; height: 0;" class="file"><input onchange="$(this).siblings(\'.file_txt\').val(this.value);" type="file" name="target_media_'+target_num+'" class="file_file" onfocus="" onblur="" onclick="" title="" value="" id=""><input value="'+fetchLabel("no_select_file")+'" class="file_txt"><div class="file_button-blue">'+fetchLabel("usr_browse")+'</div></div>';
		html += '<br/><span >'+fetchLabel("lab_mt_file_type")+'</span>';
		html += '<br/><span>'+fetchLabel("lab_mt_des")+'</span></td>';
		html += '</tr>';
		html += '</div>';
		$(html).appendTo("#target_div");
	}
	if(Number(source_num) > 2){
		document.getElementById("del_opt_btn").style.display="";//显示
	}
	
}
function wbMtDeleteOption(frm){
	if(Number(source_num) > 2){
		$("#s_div_"+source_num).remove();
		source_num = Number(source_num) - 1;
		
		$("#t_div_"+target_num).remove();
		target_num = Number(target_num) - 1;
	}
	if(Number(source_num) <= 2){
		document.getElementById("del_opt_btn").style.display="none";//隐藏
	}
	
}

function return_num_source_connected(frm){
 	var num ;
 	var temp = '';
	for(i=1;i <= target_num;i++){
		num = 0;
		for(j = 1;j <= source_num; j++){
			var answer_num_value = frm['answer_num_'+j].value;
			if(answer_num_value == i){
				num++;
			}
		}
		temp+= num + "[|]";
	} 
    return temp;
}
function return_connected_source(frm) {
	var temp = '';
	for(i=1;i <= target_num;i++){
		for(j = 1;j <= source_num; j++){
			var answer_num_value = frm['answer_num_'+j].value;
			if(answer_num_value == i){
				temp+= j + "[|]";
			}
		}
	} 
    return temp;
} 

function return_source_text(frm) {
	var temp = '';
	for (i = 1; i <= source_num; i++){
		var text = frm['source_text_'+i].value;
		temp += text + "[|]";
	}
	return temp;
}
function return_target_text(frm) {
	var temp = '';
	for (i = 1; i <= target_num; i++){
		var text = frm['target_text_'+i].value;
		temp += text + "[|]";
	}
	return temp;
}
function return_relation_score(frm) {
	var temp = '';
	for (i = 1; i <= target_num; i++) {
		for (j = 1; j <= source_num; j++) {
			var answer_num_value = frm['answer_num_'+j].value;
			if(answer_num_value == i){
				var score = frm['score_'+j].value;
				temp += score + "[|]";
			}
		}
	}
	return temp;
}
function return_source_media(frm,lang){
	var temp = '';
	for (i = 1; i <= source_num; i++){
		if(frm['source_media_'+i] != undefined){
			
			var source_media_value = frm['source_media_'+i].value;
			if(frm['tmp_source_media_'+i] &&　frm['tmp_source_media_'+i].value != ''){
				source_media_value = frm['tmp_source_media_'+i].value;
			}
			var mediaFilename = wbMediaGetFileName(source_media_value);
			if (check_chinese_character(mediaFilename) == false){
				Dialog.alert(wb_msg_name_with_CN);
				return false;
			}
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				Dialog.alert(eval('wb_msg_' + lang + '_media_not_support'))
				return false;
			}
			temp+=mediaFilename+"[|]";
		}
	}
	source_media = temp;
	return true;
}

function return_target_media(frm,lang){
	var temp = '';
	for (i = 1; i <= target_num; i++){
		if(frm['target_media_'+i] != undefined){
			var target_media_value = frm['target_media_'+i].value;
			if(frm['tmp_target_media_'+i] && frm['tmp_target_media_'+i].value != ''){
				target_media_value = frm['tmp_target_media_'+i].value;
			}
			var mediaFilename = wbMediaGetFileName(target_media_value);
			if (check_chinese_character(mediaFilename) == false){
				Dialog.alert(wb_msg_name_with_CN);
				return false;
			}
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				Dialog.alert(eval('wb_msg_' + lang + '_media_not_support'))
				return false;
			}
			temp+=mediaFilename+"[|]";
		}
	}
	target_media = temp;
	return true;
}

    
function return_mt_type(){
	var temp = '';
	for (i = 1; i <= target_num; i++) {
			temp += "MT[|]";
	}
	return temp;
}
// ---private function----
function _wbMTValidateForm(frm, lang){
	frm.inter_num_.value = '';
	frm.inter_type_.value = '';
	frm.num_source_.value = '';
	frm.inter_opt_num_.value = '';
	frm.inter_opt_body_.value = '';
	frm.inter_opt_exp_.value = '';
	frm.inter_opt_score_.value = '';
	frm.source_text_.value = '';
	frm.target_text_.value = '';
	frm.source_media_.value = '';
	frm.target_media_.value = '';
	frm.que_body_.value = '';
	frm.que_media_.value = '';
	frm.que_html_.value = 'N';

	frm.que_diff_.value = 1;
	frm.que_dur_.value = 1.0;
	frm.que_status_.value = 'OFF';
	frm.que_title_.value = '';
	frm.que_desc_.value = '';
	
	frm.que_textfield.value = wbUtilsTrimString(frm.que_textfield.value);
	if(!gen_validate_empty_field(frm.que_textfield, eval('wb_msg_' + lang + '_que_txt'), lang)){
		return false
	}
	if(getChars(frm.que_textfield.value) > 2000){
		Dialog.alert(eval('wb_msg_' + lang + '_que_txt') + fetchLabel('label_core_training_management_372'),function(){
			editor.focus();
		});
		return false;
	}
	
	//Score
	if(source_num > 0 && target_num > 0){
		for (i = 1; i <= source_num; i++) {
			var lab_score = frm.lab_score.value;
			var score_obj = frm['score_'+i];
			var score = score_obj.value;
			if(score == ''){
				Dialog.alert(eval('wb_msg_' + lang + '_enter_answer') + i + fetchLabel("label_core_training_management_359"),function(){
					score_obj.focus();
				});
				return;
			}
			if(score <= 0){
				Dialog.alert(fetchLabel("label_core_training_management_355"),function(){
					score_obj.focus();
				});
				return;
			}
			if(!wbUtilsValidateAllInteger(score_obj,lab_score)){
				score_obj.focus();
				return;
			}
		}
	}
	
	frm.que_title.value = wbUtilsTrimString(frm.que_title.value);
	if(!gen_validate_empty_field(frm.que_title, eval('wb_msg_' + lang + '_title'), lang)){
		return false
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
	
	//修改配对题出错，页面也没有找到相关配置
//	frm.que_duration.value = wbUtilsTrimString(frm.que_duration.value);
//	if(!gen_validate_float(frm.que_duration, eval('wb_msg_' + lang + '_duration'), lang)){
//		return false
//	}
	
	if(source_num < 1){
		Dialog.alert(eval('wb_msg_' + lang + '_error_no_score'));
		return false;
	}
	if(target_num < 1){
		Dialog.alert(eval('wb_msg_' + lang + '_error_no_target'));
		return false;
	}
	if(source_num > 0){
		for (i = 1; i <= source_num; i++) {
			var source_obj = frm['source_text_'+i];
			var source_text_ = source_obj.value;
			if(source_text_ == ''){
				Dialog.alert(wb_msg_pls_specify_value + " " + fetchLabel("label_core_training_management_463") + i,function(){
					source_obj.focus();
				});
				return;
			}
			if(getChars(source_text_) > 400){
				Dialog.alert(fetchLabel("label_core_training_management_463") + " " + i + " "+ fetchLabel("label_core_training_management_382"),function(){
					source_obj.focus();
				});
				return false;
			}
		}
	}
	if(target_num > 0){
		for (i = 1; i <= target_num; i++) {
			var garget_obj = frm['target_text_'+i];
			var garget_text_ = garget_obj.value;
			if(garget_text_ == ''){
				Dialog.alert(wb_msg_pls_specify_value + " " + fetchLabel("label_core_training_management_462") + i,function(){
					garget_obj.focus();
				});
				return;
			}
			if(getChars(garget_text_) > 400){
				Dialog.alert(fetchLabel("label_core_training_management_462")  + " " + i + " " + fetchLabel("label_core_training_management_382"),function(){
					garget_obj.focus();
				});
				return false;
			}
		}
	}
	//Answer
	if(source_num > 0 && target_num > 0){
		var target_existed = new Array();
		for (i = 1; i <= source_num; i++) {
			var lab_answer_number = frm.lab_answer_number.value;
			var answer_num_obj = frm['answer_num_'+i];
			var answer_num_ = answer_num_obj.value;
			if(answer_num_ == ''){
				Dialog.alert(eval('wb_msg_' + lang + '_enter_answer') + i + fetchLabel("label_core_training_management_358"));
				answer_num_obj.focus();
				return;
			}
			if(!wbUtilsValidateAllInteger(answer_num_obj,lab_answer_number)){
				answer_num_obj.focus();
				return;
			}
			if(Number(answer_num_) > Number(target_num) || Number(answer_num_) <= 0){
				Dialog.alert(wb_msg_ans_err);
				answer_num_obj.focus();
				return;
			}
			if($.inArray(Number(answer_num_),target_existed) > -1){
				Dialog.alert(wb_msg_ans_selected);
				answer_num_obj.focus();
				return;
			}
			target_existed[Number(i-1)] = Number(answer_num_);
		}
	}
	
	//img
	if(!return_source_media(frm,lang)){
		return;
	}
	if(!return_target_media(frm,lang)){
		return;
	}
	return true;
}

function _wbMTGenForm(frm,lang){
	frm.num_source_.value = source_num;
	frm.inter_num_.value = target_num;
	frm.inter_opt_num_.value = return_num_source_connected(frm);//目标被连接次数
	frm.inter_opt_body_.value = return_connected_source(frm);//目标连接源的顺序
	
	frm.source_text_.value = return_source_text(frm);//源 描述
	frm.target_text_.value = return_target_text(frm);//目标描述
	frm.source_media_.value = source_media;//源 图片
	frm.target_media_.value = target_media;//目标图片
	frm.inter_opt_score_.value = return_relation_score(frm);//分数(按目标顺序排)
	frm.inter_type_.value = return_mt_type();
	
	frm.que_body_.value = frm.que_textfield.value;
	frm.que_html_.value = 'Y';
  if(frm.que_difficulty.options && frm.que_status.options){
	frm.que_diff_.value = frm.que_difficulty.options[frm.que_difficulty.selectedIndex].value;
	frm.que_status_.value = frm.que_status.options[frm.que_status.selectedIndex].value;
  }else{
	  frm.que_diff_.value = frm.que_difficulty.value;
	   frm.que_status_.value = frm.que_status.value;
  }

	frm.que_desc_.value = frm.que_desc.value;

	frm.que_title_.value = frm.que_title.value;

	return;
}

function backHTML(str){
	str = str.replace(/@/g, '\\')
	return str
}

function set_media_file(media_name, new_media_name){
	//	Dialog.alert(media_name);
	media_name = backHTML(media_name)

	document.applets['MATCHING'].add_media_file(media_name.toLowerCase(), new_media_name.toLowerCase());
	return;
}

//---- public function ---- 
function wbMTSendForm(frm, lang ,isOpen){
	if (frm.editor) {
		editor.sync();
	}
	if(_wbMTValidateForm(frm, lang)){
		_wbMTGenForm(frm,lang);

		var hasFrame = false;
		var content = parent.document.getElementById("content");
		if (content != undefined) {
			hasFrame = true;					
		}
		if(hasFrame){
			url = window.parent.location.href
			if(url.indexOf('search') == -1){
				frm.url_success.value = 'javascript:window.parent.location.href=\'' + url + '\';document.write(\'\');'
			}
		}else {
			frm.url_success.value = gen_get_cookie('url_success')
		}

		frm.que_hint_.value = 'y';
		frm.method = 'post'
		frm.action = wb_utils_servlet_url 
		if(isOpen==true){
			frm.action += "?isExcludes=true";
		}

		frm.submit();
		if(isOpen){
			if (null!=window.parent && typeof(window.parent)!="undefined" ){
				window.parent.location.reload();
			}　
		}
	}
}

function wbMTConfirmDel(lang, type){
	if(confirm(eval('wb_msg_' + lang + '_' + 'del_' + type)))
		document.applets['MATCHING'].delete_card();
	return;
}

function wbMTSameDef(lang, type){
	if(type == 'source') Dialog.alert(eval('wb_msg_' + lang + '_source_same_def'));
	else Dialog.alert(eval('wb_msg_' + lang + '_taget_same_def'));
	return;
}

function wbMTScoreError(lang){
	Dialog.alert(eval('wb_msg_' + lang + '_score_number'));
	return;
}

function wbMTContainNone(lang){
	Dialog.alert(eval('wb_msg_' + lang + '_contain_none'));
	return;
}

function wbMTConnectError(lang){
	confirm(eval('wb_msg_' + lang + '_target_one_source'))
	return;
}

function wbMTDimensionError(lang){
	Dialog.alert(eval('wb_msg_' + lang + '_dimen_error'));
	return;
}
function check_chinese_character(name) {
	if(name.search(/[^A-Za-z0-9 ~`!@%&()_=|{}:;"'<>,.\/\#\$\^\*\-\+\[\]\?\\]/)!=-1){
		return false;
	}
	return true;	
}