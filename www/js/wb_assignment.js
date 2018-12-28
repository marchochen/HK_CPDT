// ------------------ wizBank Course object ------------------- 
// Convention:
//   public functions : use "wbAssignment" prefix 
//   private functions: use "_wbAssignment" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
//   wb_media.js
// ------------------------------------------------------------ 

/* constructor */
function wbAssignment() {	

	// for learner 
	this.submit_step_one = wbAssignmentSubmitSetpOne
	this.submit_step_two = wbAssignmentSubmitSetpTwo
	this.submit_step_three = wbAssignmentSubmitSetpThree
	this.submit_step_four = wbAssignmentSubmitSetpFour
	this.re_submit = wbAssignmentReSubmit
	this.view_result = wbAssignmentViewResult
	this.remove = wbAssignmentRemove
	this.submit_unlimit_step_one = wbAssignmentSubmitUnlimitStepOne
	 		

	// for instructor
	this.view_submission = wbAssignmentViewSubmission
	this.view_submission_graded = wbAssignmentViewSubmissionGraded
	this.view_submission_ungraded = wbAssignmentViewSubmissionUnGraded
	this.view_submission_all = wbAssignmentViewSubmissionAll
	this.view_submission_not_submit = wbAssignmentViewSubmissionNotSubmit
	this.grade = wbAssignmentGrade
	this.grade_exec = wbAssignmentGradeExec
	this.download = wbAssignmentDownload
	this.cancel = wbAssignmentCancel
	this.start_exec = wbAssignmentStartExec
}

/* private functions */
function _wbAssignmentGetQueNumber(frm) {
		var i, n, ele, str
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.checked && ele.name=='tkh_id') {
				if (ele.value !="")
					str = str + ele.value + "~"
			}
		}
		
		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		return str;	
}


/* public functions */
function wbAssignmentSubmitSetpOne(id, no_of_upload, tkh_id, not_popup, usr_id){

	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '880'
			+ ',height=' 				+ '450'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
				
	if ( no_of_upload != null )
		url = wb_utils_invoke_servlet('cmd','submit_ass','mod_id',id, 'step', '1', 'no_of_upload',no_of_upload,'stylesheet','ass_submit.xsl', 'tkh_id', tkh_id, 'isExcludes', true)
	else
		url = wb_utils_invoke_servlet('cmd','submit_ass','mod_id',id, 'step', '1', 'stylesheet','ass_submit.xsl', 'tkh_id', tkh_id, 'isExcludes', true)
	gen_set_cookie('url_failure','javascript:window.close()')
	
	if (not_popup || no_of_upload != "") {
	    self.location.href = url; 
	}else {
		wbUtilsOpenWin(url, 'ass_submit' + usr_id + '_' + tkh_id + '_' + id, false, str_feature);
	}
		

}

function wbAssignmentSubmitUnlimitStepOne(frm,lang){
	if ( !wbUtilsValidateNonZeroPositiveInteger(frm.no_of_upload, eval('wb_msg_'+lang+'_upload_file_num'))){					
		return;		
	}
		
	frm.cmd.value = "submit_ass"
	frm.stylesheet.value = 'ass_submit1.xsl'	
	frm.method = "post";
	frm.step.value = '1'
	frm.action = wb_utils_servlet_url+"?isExcludes=true";	
	frm.submit();
}



function wbAssignmentSubmitSetpTwo(frm,lang){
		file_eles = new Array;
		file_paths = new Array;
		var index = 0;
		var cur_path = '';
		var cur_file = '';
		var filename = '';
		frm.num_of_files.value = max_upload;
		for ( i=1; i<= max_upload; i++ ){
			if ((cur_path=eval('frm.file_name' + i + '.value'))!= '' ){
				file_eles[index] = eval('frm.file_name'+i+'.name');
				file_paths[index] = cur_path;
				cur_file = eval('frm.file' +i);
				file_name = wb_utils_get_filename_from_path(cur_path);
				if(!wb_utils_check_chinese_char(file_name)){
				 eval('frm.'+file_eles[index]+'.focus()');
				 return;
				}
				//to allow submit filename with '&><'
				cur_file.value = file_name;
				index++;
				}else{
					if((cur_path=eval('frm.file'+i+'.value'))!=''){
						file_eles[index] = eval('frm.file_name'+i+'.name');
						file_paths[index] = cur_path;
						cur_file = eval('frm.file'+i);
						cur_file.value = cur_path;
						index++;
					}
				}
			//简介长度判断
			if(getChars(eval('frm.comment'+i+'.value')) > 400){
				Dialog.alert(eval('wb_msg_introduction_too_longer'));
				return;
			}
			
		}
		if(wb_utils_check_duplicate_filename(frm,file_eles,file_paths)){
			return;
		}
		if ( file_paths.length > 0){
			frm.cmd.value = "submit_ass";
			frm.stylesheet.value = 'ass_submit2.xsl';	
			frm.step.value = '5';
			frm.method = "post";
			frm.action = wb_utils_servlet_url + "?isExcludes=true";
			wb_utils_preloading();
			frm.submit()
		}else{
			Dialog.alert(eval('wb_msg_' + lang + '_upload_file'));
		}
}

function wbAssignmentSubmitSetpThree(frm){
	
		frm.cmd.value = "submit_ass"
		frm.stylesheet.value = 'ass_submit1.xsl'	
		frm.step.value = '3'
		frm.method = "post";
		frm.action = wb_utils_servlet_url + "?isExcludes=true";
		frm.submit()

}

function wbAssignmentSubmitSetpFour(frm,notify){
	frm.stylesheet.value = 'ass_submit_finish.xsl'
	frm.url_failure.value = "../htm/close_window.htm"
	frm.step.value = '6';
	frm.method = "post";
	frm.action = wb_utils_servlet_url + "?isExcludes=true";
	frm.submit();
}


function wbAssignmentReSubmit(){

	

}

function wbAssignmentViewResult(id,usr_id,isPopup,tkh_id){
	
	url = wbAssignmentViewResultUrl(id,usr_id,tkh_id);
	
	if (isPopup){	
		window.location.href = url;
	}else{
		str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '787'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
		wbUtilsOpenWin(url, 'report' + usr_id + '_' + tkh_id + '_' + id, false, str_feature);
	}
}

function wbAssignmentViewResultUrl(id,usr_id,tkh_id){	
	if (tkh_id == null) {
		tkh_id = ''		
	}
	url = wb_utils_invoke_servlet('cmd','get_ass_rpt_usr','mod_id',id,'rpt_usr_id',usr_id,'attempt_nbr',1, 'stylesheet','ass_result.xsl', 'tkh_id', tkh_id, 'isExcludes', true)
	return url;

}

function wbAssignmentRemove(frm){
	
	var i = frm.file_name.selectedIndex;	
	cur_index--;
	for ( j = i ; j < cur_index ; j++){
		file_desc[j] = file_desc[j+1];
		frm.file_name.options[j].text = frm.file_name.options[j+1].text
	}
	
	delete file_desc[j]
	
	frm.file_name.options.length = cur_index;
	if ( cur_index != 0 ){
		if ( i == cur_index )
			i--;
		frm.file_name.options[i].selected = true
		frm.description.value = file_desc[i]
	}else
		frm.description.value = ""
		

	
}

function wbAssignmentViewSubmission(cos_id,mod_id,ass_queue,page,isFromIframe){
	
	if(isFromIframe == "" || isFromIframe == undefined){
		isFromIframe = false;
	}		
	url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',page,'ent_id_parent',0,'stylesheet', 'ass_sub_all.xsl','isFromIframe',isFromIframe)
	window.parent.location.href = url;

}

function wbAssignmentViewSubmissionGraded(cos_id,mod_id,ass_queue,page,ass_timestamp,isFromIframe){
	if(isFromIframe == "" || isFromIframe == undefined){
		isFromIframe = false;
	}
	
	if (ass_timestamp == ''){
		url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',page,'ass_timestamp',ass_timestamp,'ent_id_parent',0,'stylesheet', 'ass_sub_graded.xsl',"isFromIframe",isFromIframe)
		window.location.href = url;
	}else{
		url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',page,'ent_id_parent',0,'stylesheet', 'ass_sub_graded.xsl',"isFromIframe",isFromIframe)
		window.location.href = url;
	}
}

function wbAssignmentViewSubmissionUnGraded(cos_id,mod_id,ass_queue,page,ass_timestamp,isFromIframe){
	if(isFromIframe == "" || isFromIframe == undefined){
		isFromIframe = false;
	}
	url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',page,'ass_timestamp',ass_timestamp,'ent_id_parent',0,'stylesheet', 'ass_sub_ungraded.xsl',"isFromIframe",isFromIframe)
	window.location.href = url;

}

function wbAssignmentViewSubmissionAll(cos_id,mod_id,ass_queue,page,ass_timestamp,isFromIframe){
	if(isFromIframe == "" || isFromIframe == undefined){
		isFromIframe = false;
	}
	url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',page,'ass_timestamp',ass_timestamp,'ent_id_parent',0,'stylesheet', 'ass_sub_all.xsl',"isFromIframe",isFromIframe)
	window.location.href = url;

}

function wbAssignmentViewSubmissionNotSubmit(cos_id,mod_id,ass_queue,page,ass_timestamp,isFromIframe){
	if(isFromIframe == "" || isFromIframe == undefined){
		isFromIframe = false;
	}
	url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',page,'ass_timestamp',ass_timestamp,'ent_id_parent',0,'stylesheet', 'ass_not_submit.xsl',"isFromIframe",isFromIframe)
	window.location.href = url;

}


	

function wbAssignmentGrade(id,usr_id,tkh_id,isExcludes,isFromIframe,course_id){
	url = wb_utils_invoke_servlet('cmd','get_ass_rpt_usr','mod_id',id,'rpt_usr_id',usr_id,'attempt_nbr',1, 'stylesheet','ass_grading.xsl', 'tkh_id', tkh_id,'isExcludes',isExcludes,'isFromIframe',isFromIframe,'course_id',course_id)
	window.location.href = url

}

function wbAssignmentGradeExec(frm,lang,cos_id,mod_id,ass_queue,is_item,isFromIframe){
	
	if ( frm.send_email.checked != true )
		frm.email.value = ""

	if ( frm.ass_upload_file.value != '' ){
		frm.ass_filename.value = wbMediaGetFileName(frm.ass_upload_file.value)		
		
	}
	    
	if (frm.grading_format.value == 'score'){
		if ( Number(frm.pgr_score.value) > Number(frm.max_score.value)){
			Dialog.alert(eval('wb_msg_' +lang+ '_not_greater_max_score'))
			frm.pgr_score.focus();
			return;
		}
		 var regu = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
		 if(frm.pgr_score.value!='' && !regu.test(frm.pgr_score.value)){
			 Dialog.alert(fetchLabel('label_core_training_management_379'));
			 frm.pgr_score.focus();
			 return;
		 }
	}
	
	if(frm.ass_comment.value!=''){
		if(getChars(frm.ass_comment.value) > 2000){
			Dialog.alert(fetchLabel("label_core_training_management_378"));
			frm.ass_comment.focus();
			return;
		}
	}
	
	if(!wb_utils_check_chinese_char(wb_utils_get_filename_from_path(frm.ass_upload_file.value))){
		frm.ass_upload_file.focus();
		return;
	}
	url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',0,'ent_id_parent',0,'stylesheet', 'ass_sub_graded.xsl','isFromIframe',isFromIframe)
	
	if(is_item == "true"){
		//从在线模块那里进去对作业评分
		frm.url_success.value = url
	}else{
		frm.url_success.value  = wb_utils_controller_base +"admin/module/ass"
	}
	
	frm.method = "post";
	frm.action = wb_utils_servlet_url;
	if(is_item == "true"){
		frm.action = wb_utils_servlet_url;
	}
	wb_utils_preloading();
	frm.submit()

}

function wbAssignmentDownload(frm, mod_id, lang, download_lst, useTKH){
	if ( download_lst == null || download_lst == "" ){
		download_lst = _wbAssignmentGetQueNumber(frm)
		if (download_lst == "") {
			Dialog.alert(eval('wb_msg_' + lang + '_select_usr'))
			return
		}
	}
	url = wb_utils_invoke_servlet('cmd','download_ass','mod_id',mod_id,'download_lst',download_lst, 'use_tkh_ind', useTKH)
	window.location.href = url;
	return;
}

function wbAssignmentCancel(tkh_id,reloadParent){
	var ua = navigator.userAgent.toLowerCase();
	if(((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))){
		window.location.href = wb_utils_app_base + 'app/course/return/' + tkh_id;
	} else if (typeof(window.opener) == 'undefined') {
		window.opener = null;
    }
	
	reloadParent && window.opener && window.opener.location.reload(); 
	
	window.close();
}

function wbAssignmentStartExec(subtype,mod_id,course_id,lang,tkh_id,ent_id){
	var ua = navigator.userAgent.toLowerCase();
	var isMobile = ((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))?true:false;
	javascript:module_lst.start_exec(subtype,mod_id,'blank_template.xsl',course_id,lang,tkh_id,'','','','',isMobile,ent_id);
}