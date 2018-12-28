// ------------------ wizBank Forum object -------------------
// Convention:
//   public functions : use "wbForum" prefix
//   private functions: use "_wbForum" prefix
// ------------------------------------------------------------

/* constructor */
function wbForum(){
	this.main_forum = wbForumMaintainForum;

	this.ins_forum_prep = wbForumInsertForumPrep;
	this.ins_forum_exec = wbForumInsertForumExec;
	this.upd_forum_prep = wbFormUpdateForumPrep;
	this.upd_forum_exec = wbFormUpdateForumExec;
	this.del_forum = wbForumDeleteForum;

	this.start = wbForumStart
	this.start_url = wbForumStartURL
	this.start_topic = wbForumStartTopic
	this.start_msg = wbForumStartMessage

	this.search_topic_prep = wbForumSearchTopicPrep
	this.search_topic_exec = wbForumSearchTopicExec

	this.ins_topic_prep = wbForumInsTopicPrep
	this.ins_topic_exec = wbForumInsTopicExec

	this.ins_msg_prep = wbForumInsMessagePrep

	this.del_topic = wbForumDelTopic
	this.del_msg = wbForumDelMessage

	this.reply_prep = wbForumReplyPrep
	this.reply_exec = wbForumReplyExec

	this.search_msg_prep = wbForumSearchMessagePrep
	this.search_msg_exec = wbForumSearchMessageExec
	this.search_msg_result = wbForumSearchMessageResult

	this.start_msg_lst = wbForumStartMessageList
	this.start_msg_lst_url = wbForumStartMessageListURL

	this.mark_read = wbForumMarkRead
	this.mark_unread = wbForumMarkUnread
}

// public function
function wbForumMaintainForum(lang, isMaintain){
	url = wb_utils_invoke_servlet("cmd", "get_public_forum", "ismaintain", isMaintain, "stylesheet", 'forum_maintain.xsl');
	parent.location.href = url;
}

function wbForumInsertForumPrep(){
	url = wb_utils_invoke_servlet('cmd', 'get_tpl', 'tpl_type', 'FOR', 'tpl_subtype', 'FOR', 'dpo_view', 'IST_EDIT', 'stylesheet', 'forum_insert.xsl');
	parent.location.href = url;
}

function wbForumInsertForumExec(frm, lang){
	frm.mod_title.value = wbUtilsTrimString(frm.mod_title.value);
	if(frm.mod_title.value == "" || frm.mod_title.value == null){
		alert(eval("wb_msg_"+lang+"_enter_title"));
		return;
	}

	frm.method = "get"
	frm.action = wb_utils_servlet_url
	frm.mod_type.value = 'MOD'
	list = "";

	for(i = 0; i < frm.mod_instructor_id_lst.options.length; i++) list += frm.mod_instructor_id_lst.options[i].value + "~";

	/*if(list == ""){
		alert(eval('wb_msg_' + lang + '_enter_value') + " \"" + eval('wb_msg_' + lang + '_moderator') + "\"");
		return;
	}*/

	frm.mod_instructor_ent_id_lst.value = list.substr(0, list.length - 1);
	frm.url_success.value = wb_utils_invoke_servlet("cmd", "get_public_forum", "ismaintain", "true", "stylesheet", 'forum_maintain.xsl');
	frm.url_failure.value = parent.location.href;

	if(frm.start_date[0].checked == true) {
		frm.mod_eff_start_datetime.value = "IMMEDIATE"
	}else {
		if(!wbUtilsValidateDate('document.frmXml.start', frm.lab_start_datetime.value, '','ymdhm')) {
			return false;
		}
		frm.mod_eff_start_datetime.value = frm.start_yy.value + "-" + frm.start_mm.value + "-" + frm.start_dd.value + " " + frm.start_hour.value + ":" + frm.start_min.value + ":00"
	}
	if(frm.end_date[0].checked == true) {
		frm.mod_eff_end_datetime.value = "UNLIMITED"
	}else {
		if(!wbUtilsValidateDate('document.frmXml.end', frm.lab_end_datetime.value, '','ymdhm')) {
			return false;
		}
		frm.mod_eff_end_datetime.value = frm.end_yy.value + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " " + frm.end_hour.value + ":" + frm.end_min.value + ":00"
	}
	//comparison1
	if (frm.start_date[1].checked == true && frm.end_date[1].checked == true){
		if (!wb_utils_validate_date_compare({
			frm : 'document.' + frm.name,
			start_obj : 'start',
			end_obj : 'end',
			start_nm : frm.lab_start_datetime.value,
			end_nm : frm.lab_end_datetime.value
			})){
			return false;
		}
	}

	//comparison2
	if (frm.start_date[0].checked == true && frm.end_date[1].checked == true){
		if (!wb_utils_validate_date_compare({
			frm : 'document.' + frm.name,
			start_obj : 'cur_dt',
			end_obj : 'end',
			start_nm : frm.lab_start_datetime.value,
			end_nm : frm.lab_end_datetime.value,
			focus_obj : 'end'
			})){
			return false;
		}
	}

	frm.submit();
}

function wbFormUpdateForumPrep(course_id, mod_id, lang){
	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'course_id', course_id, 'mod_id', mod_id, 'stylesheet', 'forum_update.xsl', 'dpo_view', 'IST_EDIT');

	parent.location.href = url;
}

function wbFormUpdateForumExec(frm, lang){
	frm.mod_title.value = wbUtilsTrimString(frm.mod_title.value);
	if(frm.mod_title.value == "" || frm.mod_title.value == null){
		alert(eval('wb_msg_' + lang + '_enter_title'));
		return;
	}

	if((Number(frm.cur_dt.value) < Number(frm.eff_dt.value)) || (frm.mod_subtype.value != 'CHT' && frm.mod_subtype.value != 'VCR')){
		if(frm.start_date[0].checked == true) {
			frm.mod_eff_start_datetime.value = "IMMEDIATE"
		}else {
			if(!wbUtilsValidateDate('document.frmXml.start', frm.lab_start_datetime.value, '','ymdhm')) {
				return false;
			}
			frm.mod_eff_start_datetime.value = frm.start_yy.value + "-" + frm.start_mm.value + "-" + frm.start_dd.value + " " + frm.start_hour.value + ":" + frm.start_min.value + ":00"
		}
	}

	if(frm.end_date[0].checked == true){
		frm.mod_eff_end_datetime.value = "UNLIMITED"
	}else{
		if(!wbUtilsValidateDate('document.frmXml.end', frm.lab_end_datetime.value, '','ymdhm')) {
			return false;
		}
		frm.mod_eff_end_datetime.value = frm.end_yy.value + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " " + frm.end_hour.value + ":" + frm.end_min.value + ":00"
	}

	//comparison1
	if (frm.start_date[1].checked == true && frm.end_date[1].checked == true){
		if (!wb_utils_validate_date_compare({
		frm : 'document.' + frm.name,
		start_obj : 'start',
		end_obj : 'end',
		start_nm : frm.lab_start_datetime.value,
		end_nm : frm.lab_end_datetime.value
		})){
			return false;
		}
	}

	//comparison2
	if (frm.start_date[0].checked == true && frm.end_date[1].checked == true){
		if (!wb_utils_validate_date_compare({
			frm : 'document.' + frm.name,
			start_obj : 'start',
			end_obj : 'end',
			start_nm : frm.lab_start_datetime.value,
			end_nm : frm.lab_end_datetime.value,
			focus_obj : 'end'
			})){
			return false;
		}
	}

	for(i = 0; i < frm.mod_status_ind.length; i++)
		if (frm.mod_status_ind[i].checked) frm.mod_status.value = frm.mod_status_ind[i].value;

	list = "";

	for(i = 0; i < frm.mod_instructor_id_lst.options.length; i++) list += frm.mod_instructor_id_lst.options[i].value + "~";

	/*if(list == ""){
		alert(eval('wb_msg_' + lang + '_enter_value') + " \"" + eval('wb_msg_' + lang + '_moderator') + "\"");
		return;
	}*/

	frm.mod_instructor_ent_id_lst.value = list.substr(0, list.length - 1);
	frm.url_success.value = wb_utils_invoke_servlet("cmd", "get_public_forum", "ismaintain", "true", "stylesheet", 'forum_maintain.xsl');
	frm.url_failure.value = parent.location.href;
	frm.method = "post"
	frm.action = wb_utils_servlet_url
	frm.submit()
}

function wbForumDeleteForum(frm, lang){
	var isMultiple = false;

	frm.res_id_lst.value = "";
	frm.res_timestamp_lst.value = "";

	if(frm.forum_id.length){
		for(i = 0; i < frm.forum_id.length; i++){
			if(frm.forum_id[i].checked){
				isMultiple = true;

				frm.res_id_lst.value += frm.forum_id[i].value + "~";
				frm.res_timestamp_lst.value += frm.forum_timestamp[i].value + "~";
			}
		}
		if(isMultiple == true){
			frm.res_id_lst.value = frm.res_id_lst.value.substr(0, frm.res_id_lst.value.length - 1);
			frm.res_timestamp_lst.value = frm.res_timestamp_lst.value.substr(0, frm.res_timestamp_lst.value.length - 1);
		}
	}else if(frm.forum_id.checked){
		frm.res_id_lst.value = frm.forum_id.value;
		frm.res_timestamp_lst.value = frm.forum_timestamp.value;
	}

	if(frm.res_id_lst.value == ""){
		alert(eval('wb_msg_' + lang + '_sel_del_forum'));
	}else if(confirm(eval('wb_msg_' + lang + '_confirm_del_forum'))){
		frm.url_success.value = parent.location.href;

		frm.url_failure.value = parent.location.href;
		frm.cmd.value = "del_for_lst";
		frm.action = wb_utils_servlet_url;
		frm.method = "post";
		frm.submit();
	}
	return;
}

function wbForumInsTopicPrep(mod_id){
	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'mod_id', mod_id, 'stylesheet', 'forum_ins_topic.xsl')
	window.location.href = url;
}

function wbForumInsTopicExec(frm, lang){
	frm.msgBody.value = '1'
	if(_wbForumValidateFrm(frm, lang)){
		frm.msgBody.value = ''

		frm.action = wb_utils_servlet_url
		frm.url_success.value = this.start_url(frm.mod_id.value)
		frm.url_failure.value = this.start_url(frm.mod_id.value)
		frm.method = "post"
		frm.submit()
	}
}

function wbForumDelTopic(frm, mod_id, lang){
	topic_lst = _wbForumGetNumber(frm)

	if(topic_lst == ""){
		alert(eval('wb_msg_' + lang + '_select_topic'))
		return
	}

	if(!confirm(eval("wb_msg_" + lang + "_del_forum_topic"))) return;

	url_success = self.location.href
	url = wb_utils_invoke_servlet('cmd', 'del_for_topic', 'mod_id', mod_id, 'topic_lst', topic_lst, 'url_success', url_success, 'url_failure', url_success)
	window.location.href = url;
}

function wbForumReplyPrep(cos_id, mod_id, topic_id, msg_id){
	url_failure = '';
	url = wb_utils_invoke_servlet('cmd', 'reply_for_msgs', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'msg_lst', msg_id, 'stylesheet', 'forum_reply.xsl', 'url_failure', url_failure)
	window.location.href = url;
}

function wbForumReplyExec(frm, lang){
	if (editor) {
		try {
		editor.sync();
		} catch(e) {}
	}
	if(gen_validate_empty_field(frm.msg, eval('wb_msg_' + lang + '_content'), lang) && gen_validate_empty_field(frm.title, eval('wb_msg_' + lang + '_msg_subject'), lang)){
		if(getUrlParam('type') == 'post_msg'){
			frm.msg_id_parent.value = '0'
		}
		url_prev = wb_utils_get_cookie('url_prev')
		url_prev = setUrlParam('page', 0, url_prev)
		frm.url_success.value = url_prev
		frm.url_failure.value = url_prev
		frm.action = wb_utils_servlet_url
		frm.method = "post"
		frm.submit()
	}
}

function wbForumInsMessagePrep(cos_id, mod_id, topic_id){

	url_failure = ''

	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'extend', '1', 'stylesheet', 'forum_reply.xsl', 'url_failure', url_failure, 'type', 'post_msg')
	window.location.href = url;
}

function wbForumSearchTopicPrep(mod_id){
	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'mod_id', mod_id, 'stylesheet', 'forum_search_topic.xsl')
	window.location.href = url;
}

function wbForumSearchTopicExec(frm, lang){
	if(frm.search_type_topic.checked == false && frm.search_type_msg.checked == false){
		alert(eval('wb_msg_' + lang + '_select_search_type'))
		return;
	}

	if(frm.created_date.selectedIndex == 0) frm.created_after.value = ''
	else{
		if(frm.created_date.selectedIndex == 1) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 1, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		else if (frm.created_date.selectedIndex == 2) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 5, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		else if (frm.created_date.selectedIndex == 3) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 10, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		else if (frm.created_date.selectedIndex == 4) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 30, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		frm.created_after.value = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':00.0'
	}

	frm.action = wb_utils_servlet_url
	frm.stylesheet.value = 'forum_search_top_result.xsl'
	frm.method = "get"
	frm.submit()
}

function wbForumSearchMessagePrep(cos_id, mod_id, topic_id){
	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'extend', '1', 'stylesheet', 'forum_search_msg.xsl')
	window.location.href = url;
}

function wbForumSearchMessageExec(frm){
	if(frm.created_date.selectedIndex == 0) frm.created_after.value = ''
	else{
		if(frm.created_date.selectedIndex == 1) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 1, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		else if (frm.created_date.selectedIndex == 2) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 5, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		else if (frm.created_date.selectedIndex == 3) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 10, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		else if (frm.created_date.selectedIndex == 4) date = new Date(frm.current_date.value.substring(0, 4), frm.current_date.value.substring(5, 7) - 1, frm.current_date.value.substring(8, 10) - 30, frm.current_date.value.substring(11, 13), frm.current_date.value.substring(14, 16))
		frm.created_after.value = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':00.0'
	}

	frm.action = wb_utils_servlet_url
	frm.stylesheet.value = 'forum_search_msg_result.xsl'
	frm.method = "get"
	frm.submit()
}

function wbForumSearchMessageResult(frm, page){
	frm.action = wb_utils_servlet_url

	frm.stylesheet.value = 'forum_search_msg_result.xsl'
	frm.page.value = page
	frm.method = "post"
	frm.submit()
}

function wbForumStart(mod_id, page){
	if(page == null){
		page = 0;
	}

	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'mod_id', mod_id, 'page', page, 'stylesheet', 'forum.xsl')
	window.location.href = url;
}

function wbForumStartURL(mod_id, page){
	if(page == null){
		page = 0;
	}

	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'mod_id', mod_id, 'page', page, 'stylesheet', 'forum.xsl')
	return url;
}

function wbForumStartTopic(cos_id, mod_id, topic_id){
	if(wb_utils_get_cookie('forum_default_view') == 'msg_lst'){
		this.start_msg_lst(cos_id, mod_id, topic_id)
	}else{
		url = wb_utils_invoke_servlet('cmd', 'get_mod', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'extend', '1', 'stylesheet', 'forum2.xsl')
		window.location.href = url;
	}
}

function wbForumStartMessageList(cos_id, mod_id, topic_id, page){
	if(page != null) url = wb_utils_invoke_servlet('cmd', 'get_mod', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'extend', '1', 'page', page, 'unthread', '1', 'stylesheet', 'forum_msg_lst.xsl')
	else url = wb_utils_invoke_servlet('cmd', 'get_mod', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'extend', '1', 'unthread', '1', 'stylesheet', 'forum_msg_lst.xsl')
	window.location.href = url;
}

function wbForumStartMessageListURL(cos_id, mod_id, topic_id){
	url = wb_utils_invoke_servlet('cmd', 'get_mod', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'extend', '1', 'unthread', '1', 'stylesheet', 'forum_msg_lst.xsl')
	return url;
}

function wbForumDelMessage(frm, mod_id, topic_id, lang, msg_id){
	if(confirm(eval('wb_msg_' + lang + '_del_forum_msg'))){
		if(msg_id != null){
			msg_lst = msg_id
			url_success = wb_utils_get_cookie('url_prev')
		}else{
			msg_lst = _wbForumGetNumber(frm)

			if(msg_lst == ""){
				alert(eval('wb_msg_' + lang + '_select_msg'))
				return
			}
			url_success = self.location.href
		}

		url = wb_utils_invoke_servlet('cmd', 'del_for_msg', 'mod_id', mod_id, 'topic_id', topic_id, 'msg_lst', msg_lst, 'url_success', url_success, 'url_failure', url_success)
		window.location.href = url;
	}
}

function wbForumStartMessage(cos_id, mod_id, topic_id, msg_id){
	url_success = wb_utils_invoke_servlet('cmd', 'view_for_msgs', 'course_id', cos_id, 'mod_id', mod_id, 'topic_id', topic_id, 'msg_lst', msg_id, 'stylesheet', 'forum_view_msg.xsl')
	url = wb_utils_invoke_servlet('cmd', 'mark_for_msgs', 'mod_id', mod_id, 'topic_id', topic_id, 'msg_lst', msg_id, 'url_success', url_success)
	window.location.href = url;
}

function wbForumMarkRead(frm, mod_id, topic_id, lang){
	mark_lst = _wbForumGetNumber(frm)

	if(mark_lst == ""){
		alert(eval('wb_msg_' + lang + '_select_msg'))
		return
	}

	url_success = self.location.href
	url = wb_utils_invoke_servlet('cmd', 'mark_for_msgs', 'mod_id', mod_id, 'topic_id', topic_id, 'msg_lst', mark_lst, 'url_success', url_success)
	window.location.href = url
}

function wbForumMarkUnread(frm, mod_id, topic_id, lang){
	mark_lst = _wbForumGetNumber(frm)

	if(mark_lst == ""){
		alert(eval('wb_msg_' + lang + '_select_msg'))
		return
	}

	url_success = self.location.href
	url = wb_utils_invoke_servlet('cmd', 'unmark_for_msgs', 'mod_id', mod_id, 'topic_id', topic_id, 'msg_lst', mark_lst, 'url_success', url_success)
	window.location.href = url
}

// private function

function _wbForumValidateFrm(frm, lang){
	frm.title.value = wbUtilsTrimString(frm.title.value);
	if(!gen_validate_empty_field(frm.title, eval('wb_msg_' + lang + '_title'), lang)){
		frm.title.focus()
		return false;
	}

	if(frm.msgBody){
		frm.msgBody.value = wbUtilsTrimString(frm.msgBody.value);
		if(!gen_validate_empty_field(frm.msgBody, eval('wb_msg_' + lang + '_content'), lang)){
			//frm.msg.focus()
			return false;
		}
	}

	if(frm.msg){
		frm.msg.value = wbUtilsTrimString(frm.msg.value);
		if(!gen_validate_empty_field(frm.msg, eval('wb_msg_' + lang + '_content'), lang)){
			//frm.msg.focus()
			return false;
		}
	}
	return true;
}

function _wbForumGetNumber(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.checked && ele.name != 'sel_all_checkbox'){
			if(ele.value != "") str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}