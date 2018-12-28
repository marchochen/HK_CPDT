// ------------------ wizBank Course object ------------------- 
// Convention:
//   public functions : use "wbFaq" prefix 
//   private functions: use "_wbFaq" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
//   wb_media.js
// ------------------------------------------------------------ 

/* constructor */
function wbFaq() {	


	// for instructor
	this.view = wbFaqView
	this.view_url = wbFaqViewURL
	this.view_topic = wbFaqViewTopic
	this.ist_ins_topic_prep = wbFaqIstInsTopicPrep
	this.ist_ins_topic_exec = wbFaqIstInsTopicExec
	this.del_topic = wbFaqDelTopic	
	this.ist_ins_que_prep = wbFaqIstInsQuestionPrep
	this.ist_ins_que_exec = wbFaqIstInsQuestionExec
	this.del_que = wbFaqDelQuestion
	this.view_que = wbFaqViewQuestion
	this.ist_ins_ans_prep = wbFaqIstInsAnswerPrep				// for edit/ins answer
	this.ist_ins_ans_exec = wbFaqIstInsAnswerExec				// for edit/ins answer
	this.ist_ins_comment_prep = wbFaqIstInsCommentPrep
	this.ist_ins_comment_exec = wbFaqIstInsCommentExec	
	this.ist_search_topic_prep = wbFaqIstSearchTopicPrep
	this.ist_search_topic_exec = wbFaqIstSearchTopicExec
	this.ist_search_question_prep = wbFaqIstSearchQuestionPrep
	this.ist_search_question_exec = wbFaqIstSearchQuestionExec
	this.ist_search_topic_result = wbFaqIstSearchTopicResult
	this.ist_search_que_result = wbFaqIstSearchQuestionResult		
	
	//for learner
	this.start = wbFaqStart
	this.start_url = wbFaqStartURL
	this.start_topic = wbFaqStartTopic
	this.start_que = wbFaqStartQuestion
	this.lrn_ins_que_prep = wbFaqLrnInsQuestionPrep
	this.lrn_ins_que_exec = wbFaqLrnInsQuestionExec	
	this.lrn_ins_comment_prep = wbFaqLrnInsCommentPrep
	this.lrn_ins_comment_exec = wbFaqLrnInsCommentExec
	this.lrn_search_topic_prep = wbFaqLrnSearchTopicPrep
	this.lrn_search_topic_exec = wbFaqLrnSearchTopicExec
	this.lrn_search_question_prep = wbFaqLrnSearchQuestionPrep
	this.lrn_search_question_exec = wbFaqLrnSearchQuestionExec	
	this.lrn_search_topic_result = wbFaqLrnSearchTopicResult
	this.lrn_search_que_result = wbFaqLrnSearchQuestionResult	


	
}

// private function
function _wbFaqValidateFrm(frm,lang){
	frm.title.value = wbUtilsTrimString(frm.title.value);
	if (!gen_validate_empty_field(frm.title, eval('wb_msg_' + lang + '_title'),lang)) {
		frm.title.focus()
		return false;
	}
	

	return true;
}

function _wbFaqQuestionValidateFrm(frm,lang){

	if (frm.msg_title) {
		frm.msg_title.value = wbUtilsTrimString(frm.msg_title.value);
		if (!gen_validate_empty_field(frm.msg_title, eval('wb_msg_' + lang + '_faq_que'),lang)) {
			frm.msg_title.focus()
			return false;
		}
	}
	frm.msg.value = wbUtilsTrimString(frm.msg.value);
	if (!gen_validate_empty_field(frm.msg, eval('wb_msg_' + lang + '_faq_que'),lang)) {
		frm.msg.focus()
		return false;
	}
	
	return true;
}

function _wbFaqAnswerValidateFrm(frm,lang){
	frm.msg.value = wbUtilsTrimString(frm.msg.value);
	if (!gen_validate_empty_field(frm.msg, eval('wb_msg_' + lang + '_faq_answer'),lang)) {
		frm.msg.focus()
		return false;
	}
	

	return true;
}

function _wbFaqCommentsValidateFrm(frm,lang){
	frm.msg.value = wbUtilsTrimString(frm.msg.value);
	if (!gen_validate_empty_field(frm.msg, eval('wb_msg_' + lang + '_faq_comment'),lang)) {
		frm.msg.focus()
		return false;
	}
	

	return true;
}


function _wbFaqGetNumber(frm) {
		var i, n, ele, str
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.checked && ele.name != 'sel_all_checkbox') {
				if (ele.value !="")
					str = str + ele.value + "~"
			}
		}
		
		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		return str;	
}


// public function

function wbFaqView(mod_id,page){

	if (page != null )
		url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'page',page,'stylesheet', 'ist_faq.xsl')
	else
		url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'stylesheet', 'ist_faq.xsl')
	window.location.href = url;
}

function wbFaqViewURL(mod_id){

	url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'stylesheet', 'ist_faq.xsl')
	return url;
}


function wbFaqIstInsTopicPrep(mod_id){

	url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'stylesheet','ist_faq_ins_topic.xsl')
	window.location.href = url;
	


}

function wbFaqIstInsTopicExec(frm,lang){

 	if (_wbFaqValidateFrm(frm,lang)){
	
		frm.action = wb_utils_servlet_url
		frm.url_success.value = this.view_url(frm.mod_id.value) + '&page=0';
		frm.method = "post"
		frm.submit()
	}
	


}

function wbFaqDelTopic(frm,mod_id,lang){


		topic_lst = _wbFaqGetNumber(frm)
		if (topic_lst == "") {
			alert(eval('wb_msg_' + lang + '_select_faq_topic'))
			return
		}
		
		url_success = setUrlParam('page',0, self.location.href)
		url = wb_utils_invoke_servlet('cmd','del_faq_topic','mod_id',mod_id,'topic_lst',topic_lst, 'url_success', url_success,'url_failure',url_success)	
		window.location.href = url;
}


function wbFaqViewTopic(cos_id,mod_id,topic_id,page){
	window.location.href = _wbFaqViewTopicUrl(cos_id,mod_id,topic_id,page);
}

function _wbFaqViewTopicUrl(cos_id,mod_id,topic_id,page){
	if (page != null )
		url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id',mod_id,'topic_id',topic_id,'page',page,'stylesheet', 'ist_faq2.xsl')
	else
		url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id',mod_id,'topic_id',topic_id,'stylesheet', 'ist_faq2.xsl')
	return url;
}

function wbFaqIstInsQuestionPrep(cos_id,mod_id,topic_id){

	url_failure = self.location.href
	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id', mod_id, 'topic_id',topic_id, 'stylesheet','ist_faq_ins_que.xsl','url_failure',url_failure)
	window.location.href = url;

}

function wbFaqIstInsQuestionExec(frm,lang){
 	if (_wbFaqQuestionValidateFrm(frm,lang)){
	
		frm.action = wb_utils_servlet_url
		//url_success = wb_utils_get_cookie('url_prev')
		url_success = _wbFaqViewTopicUrl(getUrlParam('course_id'),getUrlParam('mod_id'),getUrlParam('topic_id'));
		frm.url_success.value = setUrlParam('page',0, url_success);
		frm.method = "post"
		frm.submit()
	}
}

function wbFaqDelQuestion(frm,mod_id,topic_id,lang){

		if(confirm(eval('wb_msg_' + lang + '_confirm'))){
		msg_lst = _wbFaqGetNumber(frm)
		if (msg_lst == "") {
			alert(eval('wb_msg_' + lang + '_select_faq_que'))
			return
		}
		url = wb_utils_invoke_servlet('cmd','del_faq_msg','mod_id',mod_id,'topic_id',topic_id,'msg_lst',msg_lst, 'url_success', frm.url_success.value,'url_failure',self.location.href)	
		window.location.href = url;
		}
		
}



function wbFaqViewQuestion(cos_id,mod_id,topic_id,que_id){


	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id', mod_id, 'topic_id',topic_id,'msg_id',que_id,'stylesheet','ist_faq_view_que.xsl')
	window.location.href = url;
	
}

function wbFaqIstInsAnswerPrep(cos_id,mod_id,topic_id,que_id){

	url_failure = self.location.href
	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id', mod_id, 'topic_id',topic_id, 'msg_id',que_id,'stylesheet','ist_faq_ins_ans.xsl','url_failure',url_failure)
	window.location.href = url;

}

function wbFaqIstInsAnswerExec(frm,lang){

 	if (_wbFaqAnswerValidateFrm(frm,lang)){
	
		frm.action = wb_utils_servlet_url
		frm.url_success.value = wb_utils_get_cookie('url_prev')
		frm.method = "post"
		frm.submit()
	}
}




function wbFaqIstInsCommentPrep(cos_id,mod_id,topic_id,que_id){

	url_failure = self.location.href
	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id', mod_id, 'topic_id',topic_id, 'msg_id',que_id,'stylesheet','ist_faq_ins_comment.xsl','url_failure',url_failure)
	window.location.href = url;

}

function wbFaqIstInsCommentExec(frm,lang){

 	if (_wbFaqCommentValidateFrm(frm,lang)){
	
		frm.action = wb_utils_servlet_url
		frm.url_success.value = wb_utils_get_cookie('url_prev')
		frm.method = "post"
		frm.submit()
	}
}



function wbFaqIstSearchTopicPrep(mod_id){
	
	url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'stylesheet', 'ist_faq_search_topic.xsl')
	window.location.href = url;
}
	
function wbFaqIstSearchTopicExec(frm,lang){

	if (frm.search_ans.checked == false && frm.search_que.checked == false && frm.search_com.checked == false){
		alert(eval('wb_msg_' + lang + '_select_search_type'))
		return;
	}
		
	if (frm.created_date.selectedIndex == 0 )
		frm.created_after.value = ''
	else {
		if (frm.created_date.selectedIndex == 1 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-1,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 2 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-5,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 3 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-10,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 4 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-30,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
				
		frm.created_after.value =  date.getFullYear() + '-' + (date.getMonth() + 1 ) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':00.0'					

	}
	
	frm.action = wb_utils_servlet_url	
	frm.stylesheet.value = 'ist_faq_search_top_result.xsl'
	frm.method = "get"
	frm.submit()

}
	
function wbFaqIstSearchQuestionPrep(cos_id,mod_id,topic_id){

	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id',mod_id,'topic_id',topic_id,'stylesheet', 'ist_faq_search_topic.xsl')
	window.location.href = url;

}

	
function wbFaqIstSearchQuestionExec(frm,lang){

	if (frm.search_ans.checked == false && frm.search_que.checked == false && frm.search_com.checked == false){
		alert(eval('wb_msg_' + lang + '_select_search_type'))
		return;
	}

	if (frm.created_date.selectedIndex == 0 )
		frm.created_after.value = ''
	else {
		if (frm.created_date.selectedIndex == 1 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-1,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 2 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-5,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 3 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-10,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 4 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-30,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
				
		frm.created_after.value =  date.getFullYear() + '-' + (date.getMonth() + 1 ) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':00.0'					

	}
	
	frm.action = wb_utils_servlet_url	
	frm.stylesheet.value = 'ist_faq_search_que_result.xsl'	
	frm.method = "get"
	frm.submit()
	
}

function wbFaqIstSearchTopicResult(frm,page){

	
	frm.action = wb_utils_servlet_url	
	frm.stylesheet.value = 'ist_faq_search_top_result.xsl'
	frm.page.value = page
	frm.method = "post"
	frm.submit()
	
}

function wbFaqIstSearchQuestionResult(frm,page){

	frm.action = wb_utils_servlet_url	
	frm.stylesheet.value = 'ist_faq_search_que_result.xsl'
	frm.page.value = page
	frm.method = "post"
	frm.submit()
}
	
	

// for learner

function wbFaqStart(mod_id,page){

	if (page != null )
		url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'page',page,'stylesheet', 'lrn_faq.xsl')
	else
		url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'stylesheet', 'lrn_faq.xsl')
		
	window.location.href = url;		
}

function wbFaqStartURL(mod_id){

	url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'stylesheet', 'lrn_faq.xsl')
		
	return url;
}



function wbFaqStartTopic(cos_id,mod_id,topic_id,page){

	if (page != null )
		url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id',mod_id,'topic_id',topic_id,'page',page,'stylesheet', 'lrn_faq2.xsl')
	else
			url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id',mod_id,'topic_id',topic_id,'stylesheet', 'lrn_faq2.xsl')
	window.location.href = url;
	
}

function wbFaqLrnInsQuestionPrep(cos_id,mod_id,topic_id){

	url_failure = self.location.href
	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id', mod_id, 'topic_id',topic_id, 'stylesheet','lrn_faq_ins_que.xsl','url_failure',url_failure)
	window.location.href = url;

}

function wbFaqLrnInsQuestionExec(frm,lang){
	
 	if (_wbFaqQuestionValidateFrm(frm,lang)){
	
		frm.action = wb_utils_servlet_url
		url_success = wb_utils_get_cookie('url_prev')
		frm.url_success.value = setUrlParam('page',0, url_success);
		frm.method = "post"
		frm.submit()
	}
}

function wbFaqLrnInsCommentPrep(cos_id,mod_id,topic_id,que_id){

	url_failure = self.location.href
	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id', mod_id, 'topic_id',topic_id, 'msg_id',que_id,'stylesheet','lrn_faq_ins_comment.xsl','url_failure',url_failure)
	window.location.href = url;

}

function wbFaqLrnInsCommentExec(frm,lang){

 	if (_wbFaqCommentValidateFrm(frm,lang)){
	
		frm.action = wb_utils_servlet_url
		frm.url_success.value = wb_utils_get_cookie('url_prev')
		frm.method = "post"
		frm.submit()
	}
}





function wbFaqStartQuestion(cos_id,mod_id,topic_id,que_id){

	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id', mod_id, 'topic_id',topic_id,'msg_id',que_id,'stylesheet','lrn_faq_view_que.xsl')

	window.location.href = url;	

}


function wbFaqLrnSearchTopicPrep(mod_id){
	
	url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'stylesheet', 'lrn_faq_search_topic.xsl')
	window.location.href = url;
}


function wbFaqLrnSearchTopicExec(frm,lang){

	if (frm.search_ans.checked == false && frm.search_que.checked == false && frm.search_com.checked == false){
		alert(eval('wb_msg_' + lang + '_select_search_type'))
		return;
	}
	
	if (frm.created_date.selectedIndex == 0 )
		frm.created_after.value = ''
	else {
		if (frm.created_date.selectedIndex == 1 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-1,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 2 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-5,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 3 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-10,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 4 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-30,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
				
		frm.created_after.value =  date.getFullYear() + '-' + (date.getMonth() + 1 ) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':00.0'					
	}
	
	frm.action = wb_utils_servlet_url	
	frm.stylesheet.value = 'lrn_faq_search_top_result.xsl'
	frm.method = "get"
	frm.submit()


}





function wbFaqLrnSearchQuestionPrep(cos_id,mod_id,topic_id){

	url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id',mod_id,'topic_id',topic_id,'stylesheet', 'lrn_faq_search_topic.xsl')
	window.location.href = url;


}



function wbFaqLrnSearchQuestionExec(frm,lang){

	if (frm.search_ans.checked == false && frm.search_que.checked == false && frm.search_com.checked == false){
		alert(eval('wb_msg_' + lang + '_select_search_type'))
		return;
	}
	
	if (frm.created_date.selectedIndex == 0 )
		frm.created_after.value = ''
	else {
		if (frm.created_date.selectedIndex == 1 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-1,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 2 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-5,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 3 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-10,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
		else if (frm.created_date.selectedIndex == 4 )
			date = new Date(frm.current_date.value.substring(0,4),frm.current_date.value.substring(5,7)-1,frm.current_date.value.substring(8,10)-30,frm.current_date.value.substring(11,13),frm.current_date.value.substring(14,16))
				
		frm.created_after.value =  date.getFullYear() + '-' + (date.getMonth() + 1 ) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':00.0'					
	}
	
	frm.action = wb_utils_servlet_url	
	frm.stylesheet.value = 'lrn_faq_search_que_result.xsl'
	frm.method = "get"
	frm.submit()


}


function wbFaqLrnSearchTopicResult(frm,page){

	frm.stylesheet.value = 'lrn_faq_search_top_result.xsl'

	frm.action = wb_utils_servlet_url	
	frm.page.value = page
	frm.method = "post"
	frm.submit()
	
}


function wbFaqLrnSearchQuestionResult(frm,page){

	frm.stylesheet.value = 'lrn_faq_search_que_result.xsl'

	frm.action = wb_utils_servlet_url	
	frm.page.value = page
	frm.method = "post"
	frm.submit()


}
	