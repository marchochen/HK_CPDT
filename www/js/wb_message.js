// ------------------ wizBank Message object ------------------- 
// Convention:
//   public functions : use "wbMsg" prefix 
//   private functions: use "_wbMsg" prefix
// ------------------------------------------------------------ 

/* constructor */
function wbMessage() {	
	
	this.init = wbMsgInitPrep
	
	//comment, notify, link notify, invite target learner
	this.ins_exec = wbMsgInsExec
	this.ins_lnk_notify_exec = wbMsgInsLnkNotifyExec
	
	//invite target learner
	this.get_target_grp_lst = wbMsgGetTargetGrouplst
	
	//JI
	this.ji_preview = wbMsgJIPreview
	this.ji_ins = wbMsgJIIns
	this.ji_ins_exec = wbMsgJIInsExec
	this.ji_edit = wbMsgJIEdit
	this.ji_edit_exec = wbMsgJIEditExec
	this.ins_ji_exec = wbMsgInsJIExec
	this.ji_status = wbMsgJIStatus
}

/* public functions */
function wbMsgInitPrep(msg_type,id,id_type,msg_url,target_group_lst,app_process_status,ent_id_lst){
	var stylesheet, win_height
	stylesheet = ""
		
	if (msg_url != null && msg_url != '') {
		msg_url = ".." + msg_url.substring(msg_url.indexOf('/servlet'),msg_url.length)
		msg_url = msg_url.replace(/&/g,"%26");
	}
	
	if (msg_type == 'comment'){win_height = '277'}
	else if (msg_type == 'notify' || msg_type == 'enrollment_notify' || msg_type == 'link_notify' || msg_type == 'invite_target_learner'){win_height = '700'}
	else {win_height = '420'}
	
	if (msg_type == 'comment'){
			str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '500'
			+ ',height=' 				+ win_height
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'yes';
	}else{
		str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '750'
			+ ',height=' 				+ win_height
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'yes';
	}
			
	if (msg_type == 'comment' || msg_type == 'notify' || msg_type == 'enrollment_notify' || msg_type == 'link_notify' || msg_type == 'course_approval_request' || msg_type == 'invite_target_learner')
		stylesheet = 'msg_notify.xsl';	
	else if (msg_type == 'ji')
		stylesheet = 'msg_ji.xsl';
		
	if (id == null){id = '';}	
	if (id_type == null){id_type = '';}
	if (app_process_status == null){app_process_status = '';}
	
	if (msg_url != null && msg_url != ''){wb_utils_set_cookie('msg_' + msg_type + '_url',msg_url)}
	wb_utils_get_cookie('msg_' + msg_type + '_url')
		

	url = wb_utils_invoke_ae_servlet(
		'cmd','init_msg',
		'msg_type',msg_type,
		'stylesheet',stylesheet,
		'id',id,
		'id_type',id_type,
		'app_process_status',app_process_status
	)
	
	if(target_group_lst != null && target_group_lst != '') {url += '&target_group=' + target_group_lst;}
	
	if(ent_id_lst != null && ent_id_lst != '') {url += '&ent_id_lst=' + ent_id_lst;}
	if (msg_type == 'ji' || msg_type == 'course_approval_request')
		self.location.href = url;
	else
		wbUtilsOpenWin(url,'msg_'+ msg_type, false,str_feature);	
}

function wbMsgInsExec(frm,lang,msg_type,isExcludes){
	
	frm.ent_ids.value = _wbMsgGetInsNotifyEntIdsLst(frm,frm.ent_ids_lst)
	if (frm.cc_ent_ids){
		frm.cc_ent_ids.value = _wbMsgGetInsNotifyEntIdsLst(frm,frm.cc_ent_ids_lst)
	}
	
	if (msg_type == 'enrollment_notify') {msg_type = 'NOTIFY';}
	
	if (_wbMsgValidateInsNotify(frm,lang)){
		
		frm.cmd.value = 'ins_xmsg'
		frm.msg_bcc_sys_ind.value = 'false'
		frm.url_redirect.value = _wbMsgGetRedirectParam(frm,msg_type)
		frm.url_success.value = '../htm/close_window.htm'
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_ae_servlet_url;
		if(isExcludes){
			frm.action+="?isExcludes=true";
		}
		frm.method = 'post'			
		frm.submit()
	}
}

function wbMsgInsLnkNotifyExec(frm,lang,msg_type){
	
	frm.ent_ids.value = _wbMsgGetInsNotifyEntIdsLst(frm,frm.ent_ids_lst)
	frm.cc_ent_ids.value = _wbMsgGetInsNotifyEntIdsLst(frm,frm.cc_ent_ids_lst)	

	if (_wbMsgValidateInsNotify(frm,lang)){
		
		if (frm.msg_bcc_sys_ind) {
			if (msg_type == 'invite_target_learner') {frm.msg_bcc_sys_ind.value = 'false';}
		}
		frm.cmd.value = 'ins_xmsg'
		frm.url_redirect.value = _wbMsgGetLinkRedirectParam(frm,msg_type)
		if (frm.id_type.value == 'item') {
			if (msg_type == 'course_approval_request'){				
				frm.url_success.value = wb_utils_get_cookie('course_approval_request_url')
			}else{				
				if(window.opener && !window.opener.closed){
					frm.url_success.value = '../htm/close_and_reload_window.htm';
				}else{
					frm.url_success.value = '../htm/close_window.htm';
				}
			}
		}
		else {			
			frm.url_success.value = '../htm/close_window.htm';
		}
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_ae_servlet_url
		frm.method = 'post'						
		frm.submit()
	}	
}

function wbMsgInsJIExec(frm,lang,msg_type){
	
	frm.ent_ids.value = _wbMsgGetInsNotifyEntIdsLst(frm,frm.ent_ids_lst)
	frm.cc_ent_ids.value = _wbMsgGetInsNotifyEntIdsLst(frm,frm.cc_ent_ids_lst)
	
	if (msg_type == null || msg_type == '')
		msg_type = 'ji';
	
	if (_wbMsgValidateInsJI(frm,lang)){
		frm.msg_bcc_sys_ind.value = 'true'
		frm.cmd.value = 'ae_send_notify'
		frm.url_success.value = wbMsgJIPreviewUrl(frm.itm_id.value)
		frm.url_failure.value = self.location.href
		frm.url_redirect.value = _wbMsgGetJIRedirectParam(frm,msg_type)
		frm.action = wb_utils_ae_servlet_url
		frm.method = 'post'		
		frm.submit()
	}	
}

function wbMsgJIPreview(id,nm,target_win){
	
	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ '776'
		+ ',height=' 				+ '539'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',status='				+ 'yes';
	
	url = wb_utils_invoke_disp_servlet(
		'module', 'message.MessageModule',
		'cmd', 'preview',
		'msg_type', 'ji',
		'itm_id', id,
		'msg_subtype', 'html',
		'stylesheet', 'msg_ji_preview.xsl'
	)
	if (nm != null && nm != '')
		wb_utils_set_cookie('ji_preview_itm_name',nm);
	
	if (target_win != null && target_win != '')
		target_win.location.href = url;
	else
		wbUtilsOpenWin(url,'msg_ji', false, str_feature);	
}

function wbMsgJIPreviewUrl(id){
	url = wb_utils_invoke_disp_servlet(
		'module', 'message.MessageModule',
		'cmd', 'preview',
		'msg_type', 'ji',
		'itm_id', id,
		'msg_subtype', 'html',
		'stylesheet', 'msg_ji_preview.xsl'
	)
	return url;	
}

function wbMsgJIIns(id){
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_itm','itm_id',id,'tvw_id','JI_VIEW','stylesheet','msg_ji_ins.xsl','ji_view','true')
	self.location.href = url;	
}

function wbMsgJIInsExec(frm,lang,id){
	
	if (_wbMsgValidateJIInsFrm(frm,lang)){
		
		frm.cmd.value = 'ins_ji'
		frm.module.value = 'message.MessageModule'
		frm.msg_subtype.value = 'html'
		
		frm.param1_type.value = 'DYNAMIC'
		frm.param1_name.value = 'ent_ids'
		frm.param1_value.value = 'GET_ENT_ID'
		frm.rem_param1_value.value = frm.param1_value.value
		
		frm.param2_type.value = 'STATIC'
		frm.param2_name.value = 'sender_id'
		frm.param2_value.value = frm.sender_id.value
		frm.rem_param2_value.value = frm.param2_value.value
		
		frm.param3_type.value = 'STATIC'
		frm.param3_name.value = 'cmd'
		frm.param3_value.value = 'ae_notify_xml'
		frm.rem_param3_value.value = frm.param3_value.value
		
		frm.param4_type.value = 'STATIC'
		frm.param4_name.value = 'intro'
		frm.param4_value.value = frm.intro.value
		frm.rem_param4_value.value = 'N'		
		
		frm.param5_type.value = 'STATIC'
		frm.param5_name.value = 'env'
		frm.param5_value.value = 'wizb'
		frm.rem_param5_value.value = frm.param5_value.value
		
		frm.param6_type.value = 'STATIC'
		frm.param6_name.value = 'label_lan'
		frm.param6_value.value = frm.encoding.value
		frm.rem_param6_value.value = frm.param6_value.value
		
		frm.param7_type.value = 'STATIC'
		frm.param7_name.value = 'site_id'
		frm.param7_value.value = frm.root_ent_id.value
		frm.rem_param7_value.value = frm.param7_value.value
		
		frm.param8_type.value = 'STATIC'
		frm.param8_name.value = 'style'
		frm.param8_value.value = frm.style.value
		frm.rem_param8_value.value = frm.param8_value.value
		
		frm.param9_type.value = 'STATIC'
		frm.param9_name.value = 'url_redirect'
		frm.param9_value.value = '..%2Fservlet%2F' + wb_utils_servlet_package_qdbaction + '%3Fenv=wizb%26cmd=get_res%26stylesheet=res_srh_res_ind.xsl%26res_id='
		frm.rem_param9_value.value = frm.param9_value.value
		
		frm.param10_type.value = 'STATIC'
		frm.param10_name.value = 'itm_id'
		frm.param10_value.value = frm.itm_id.value
		frm.rem_param10_value.value = frm.param10_value.value
		
		frm.param11_type.value = 'STATIC'
		frm.param11_name.value = 'tvw_id'
		frm.param11_value.value = 'JI_VIEW'
		frm.rem_param11_value.value = frm.param11_value.value
		
		frm.url_success.value = wbMsgJIPreviewUrl(id)
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		
		frm.submit()
	}
}

function wbMsgJIEdit(id){
	url = wb_utils_invoke_disp_servlet(
		'module', 'message.MessageModule',
		'cmd', 'preview',
		'msg_type', 'ji',
		'itm_id', id,
		'msg_subtype', 'html',
		'stylesheet', 'msg_ji_edit.xsl'
	)
	window.location.href = url;
}

function wbMsgJIEditExec(frm,lang,id){
	
	if (_wbMsgValidateJIInsFrm(frm,lang)){
		
		_wbMsgAssignInsJIParam(frm)		
		frm.cmd.value = 'upd_ji'
		frm.module.value = 'message.MessageModule'
		frm.msg_subtype.value = 'html'
		frm.msg_bcc_sys_ind.value = 'true'
		
		frm.param1_type.value = 'DYNAMIC'
		frm.param1_name.value = 'ent_ids'
		frm.param1_value.value = 'GET_ENT_ID'
		frm.rem_param1_value.value = frm.param1_value.value
		
		frm.param2_type.value = 'STATIC'
		frm.param2_name.value = 'sender_id'
		frm.param2_value.value = frm.sender_id.value
		frm.rem_param2_value.value = frm.param2_value.value
		
		frm.param3_type.value = 'STATIC'
		frm.param3_name.value = 'cmd'
		frm.param3_value.value = 'ae_notify_xml'
		frm.rem_param3_value.value = frm.param3_value.value
		
		frm.param4_type.value = 'STATIC'
		frm.param4_name.value = 'intro'
		frm.param4_value.value = frm.intro.value
		frm.rem_param4_value.value = 'N'
		
		frm.param5_type.value = 'STATIC'
		frm.param5_name.value = 'env'
		frm.param5_value.value = 'wizb'
		frm.rem_param5_value.value = frm.param5_value.value
		
		frm.param6_type.value = 'STATIC'
		frm.param6_name.value = 'label_lan'
		frm.param6_value.value = frm.encoding.value
		frm.rem_param6_value.value = frm.param6_value.value		
		
		frm.param7_type.value = 'STATIC'
		frm.param7_name.value = 'site_id'
		frm.param7_value.value = frm.root_ent_id.value
		frm.rem_param7_value.value = frm.param7_value.value
		
		frm.param8_type.value = 'STATIC'
		frm.param8_name.value = 'style'
		frm.param8_value.value = frm.style.value
		frm.rem_param8_value.value = frm.param8_value.value
		
		frm.param9_type.value = 'STATIC'
		frm.param9_name.value = 'url_redirect'
		frm.param9_value.value = '..%2Fservlet%2F' + wb_utils_servlet_package_qdbaction + '%3Fenv=wizb%26cmd=get_res%26stylesheet=res_srh_res_ind.xsl%26res_id='
		frm.rem_param9_value.value = frm.param9_value.value
		
		frm.param10_type.value = 'STATIC'
		frm.param10_name.value = 'itm_id'
		frm.param10_value.value = frm.itm_id.value
		frm.rem_param10_value.value = frm.param10_value.value
		
		frm.param11_type.value = 'STATIC'
		frm.param11_name.value = 'tvw_id'
		frm.param11_value.value = 'JI_VIEW'	
		frm.rem_param11_value.value = frm.param11_value.value		
		
		frm.url_success.value = wbMsgJIPreviewUrl(id)
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.submit()
	}
}

function wbMsgJIStatus (id,cur_page,page_size,sort_col,sort_order,timestamp){
	
	if (sort_col == null || sort_col == '')
		sort_col = 'usr_display_bil';
	if (sort_order == null || sort_order == '')
		sort_order = 'ASC';
	if (cur_page == null || cur_page == '')
		cur_page = '1';
	if (page_size == null || page_size == '')
		page_size = '10';
	if (timestamp == null || timestamp == '')
		timestamp = '';
	
	url = wb_utils_invoke_ae_servlet(
		'cmd','ae_notify_status',
		'itm_id',id,
		'cur_page',cur_page,
		'page_size',page_size,
		'sort_col',sort_col,
		'sort_order',sort_order,
		'timestamp',timestamp,
		'stylesheet','msg_ji_status_lst.xsl'
	)
	self.location.href = url;	
}

function wbMsgInsPopupSrchUsrLst(fld_name,rawIdLst,rawNameLst){
	var i, args, args2 , idLst, nameLst
	idLst = new Array()
	nameLst = new Array()
	
	args = new String(rawIdLst)
	args2 = new String(rawNameLst)							
	idLst = args.split("~%~")
	nameLst = args2.split("~%~")				
	
	for (i=0; i<idLst.length; i++){
		var j, tmpOpt, can_add, OptCnt
		tmpOpt = new Array();
		can_add = true;
		OptCnt = eval('document.frmXml.' + fld_name + '.options.length');
		
		tmpOpt[i] = new Option
		tmpOpt[i].text =  nameLst[i]
		tmpOpt[i].value = idLst[i]								
		
		for (j=0; j<OptCnt; j++){
			if (eval('document.frmXml.' + fld_name + '.options[' + j + '].value') == tmpOpt[i].value){
				can_add = false
				break;
			}
		}
		if (can_add == true) {addOption(eval('document.frmXml.' + fld_name), tmpOpt[i]);}					
	}				
}


/* private functions */
function concate_string() {
	var path = '';
	
	// temp 
	//path += 'env=wizb';	
	for (i = 0; i < arguments.length; i = i + 2) {	
		//path += '&' + escape(arguments[i]) + '=' + escape(arguments[i+1])
		path +=  arguments[i] + '=' + arguments[i+1] + '&'
	}
	if (path != ""){
		path = path.substring(0, path.length-1);
	}
	return path;
}

function _wbMsgGetInsNotifyEntIdsLst(frm,obj){
	var str, ele, i, n
	str = ''
	n = obj.options.length
	for (i=0; i < n; i++){
		if (obj.options[i].value != ""){
			str += obj.options[i].value + '~'
		}		
	}
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}

function _wbMsgValidateInsJI(frm,lang){
	
	if (frm.ent_ids.value == ""){
		alert(eval('wb_msg_' + lang + '_sel_notify_usr_grp'))
		return false;
	}
	
	if (frm.ji_status_sel){
		if(!_wbMsgValidateInsJIStatus(frm,lang)){return false;}
	}
	if (frm.rem_status_sel){
		if(!_wbMsgValidateInsJIReminder(frm,lang)){return false;}
	}
	return true;
}

function validateEmail(fld_email, txtFldName) {
	var p = /^[a-zA-Z0-9\._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	if (p.test(fld_email)) {
		return true;
	} else {
		alert(wb_msg_usr_enter_valid + txtFldName);
		return false;
	}
}

function _wbMsgValidateInsNotify(frm,lang){

	if (frm.ent_ids.value == ""){
		alert(eval('wb_msg_' + lang + '_sel_notify_usr_grp'))
		return false;
	}
	
	if(frm.cc_email_address) {
		var cc_email = frm.cc_email_address.value;
		if(cc_email.length > 0) {
			var emails = cc_email.split(',');
			for(var i=0; i<emails.length; i++) {
				if(emails[i] != '' && !validateEmail(wbUtilsTrimString(emails[i]), eval('wb_msg_' + lang + '_msg_cc_email'))){
					frm.cc_email_address.focus()
					return false;
				}
			}
		}
	}
	
	if (frm.msg_subject){
		frm.msg_subject.value = wbUtilsTrimString(frm.msg_subject.value);
		
		if(!_wbMsgValidateIns(frm.msg_subject, eval('wb_msg_' + lang + '_msg_subject'))){
			return false;
		}
		
		if(getChars(frm.msg_subject.value) > 80){
			Dialog.alert(fetchLabel('label_core_requirements_management_68') ,function(){
				if((frm.msg_subject.type=='textarea' || frm.msg_subject.type=='text') && frm.msg_subject.style.display != "none"){
					frm.msg_subject.focus();
				}
			});
			return false;
		}
	}
	
	if (frm.msg_type){
		frm.msg_body.value = wbUtilsTrimString(frm.msg_body.value);
		if (frm.msg_type.value == 'comment'){
			
			if(!_wbMsgValidateIns(frm.msg_body, eval('wb_msg_' + lang + '_comment'))){
				return false;
			}
			
			if(getChars(frm.msg_body.value) > 2000){
				Dialog.alert(fetchLabel('label_core_requirements_management_65') ,function(){
					if((frm.msg_body.type=='textarea' || frm.msg_body.type=='text') && frm.msg_body.style.display != "none"){
						frm.msg_body.focus();
					}
				});
				return false;
			}
			
		}else if(frm.msg_type.value == 'course_approval_request'){
			frm.msg_body.value = ''
		}else{
			if(!_wbMsgValidateIns(frm.msg_body, eval('wb_msg_' + lang + '_content'))){
				return false;
			}
			
			if(getChars(frm.msg_body.value) > 2000){
				Dialog.alert(fetchLabel('label_core_requirements_management_65'),function(){
					if((frm.msg_body.type=='textarea' || frm.msg_body.type=='text') && frm.msg_body.style.display != "none"){
						frm.msg_body.focus();
					}
				});
				return false;
			}
		}
	
	}
	
	
	if (frm.msg_sched_sel){
		if(!_wbMsgValidateInsNotifySched(frm,lang)){return false;}
	}
	return true;
}

function _wbMsgGetRedirectParam(frm,msg_type){	
	
	url = wb_utils_disp_servlet_url + '?'
	
	url += concate_string(	
		'msg_type', msg_type,	
		'module', 'message.MessageModule',
		'cmd', 'ins_notify',
		//'msg_body', frm.msg_body.value,
		'usr_id', frm.sender_id.value,
		
		'param1_type', 'STATIC',
		'param1_name', 'cmd',
		'param1_value', 'notify_xml',
		
		'param2_type', 'STATIC',
		'param2_name', 'sender_id',
		'param2_value', frm.sender_id.value,
		
		'param3_type', 'DYNAMIC',
		'param3_name', 'ent_ids',
		'param3_value', 'GET_ENT_ID',
		
		'param4_type', 'DYNAMIC',
		'param4_name', 'cc_ent_ids',
		'param4_value', 'GET_CC_ENT_ID'		
	)
	
	if (frm.msg_subtype){
		url += '&' + concate_string('msg_subtype', frm.msg_subtype.value)
	}else{
		url += '&' + concate_string('msg_subtype', 'html')
	}
	
	if (frm.msg_datetime_date){		
		url += '&' + concate_string('msg_datetime', frm.msg_datetime_date.value)
	}
	return url;
}

function _wbMsgGetJIRedirectParam(frm,msg_type){
	url = wb_utils_disp_servlet_url + '?'
	
	url += concate_string(
		'module', 'message.MessageModule',
		'cmd', 'ins_recip',
		'msg_type', msg_type,
		'rem_status', frm.rem_status.value,
		'msg_subtype', 'html',
		'rem_send_date', frm.rem_send_date.value
		
	)
	return url;
}

function _wbMsgGetLinkRedirectParam(frm,msg_type){	
	
	url = wb_utils_disp_servlet_url + '?'
	
	//param8_value = wb_utils_get_cookie('msg_' + msg_type + '_url')
	if( msg_type == 'invite_target_learner' ) {
		param8_value = '..%2Fservlet%2Fcw.ae.aeAction%3Fenv=wizb%26cmd=ae_get_itm%26tvw_id=LRN_VIEW%26stylesheet=itm_lrn_details.xsl%26prev_version=false%26show_run_ind=true%26itm_id=' + frm.id.value
		//param8_value = param8_value.replace(/&/g,"%26");
	} else if ( msg_type == 'course_approval_request' ) {
		param8_value = '..%2Fservlet%2Fcw.ae.aeAction%3Fenv=wizb%26cmd=ae_get_itm%26tvw_id=DETAIL_VIEW%26stylesheet=itm_details.xsl%26url_failure=%26tnd_id=%26prev_version_ind=false%26show_run_ind=false%26itm_id=' + frm.id.value
	}
	url += concate_string(		
	
		'msg_type', msg_type,
		'module', 'message.MessageModule',
		'cmd', 'ins_link_notify',
		//'msg_subject', frm.msg_subject.value,
		//'msg_body', frm.msg_body.value,
		'msg_subtype', frm.msg_subtype.value,
		'usr_id', frm.sender_id.value,
		'msg_datetime', frm.msg_datetime_date.value,
		
		'param1_type', 'DYNAMIC',
		'param1_name', 'ent_ids',
		'param1_value', 'GET_ENT_ID',
		
		'param2_type', 'STATIC',
		'param2_name', 'sender_id',
		'param2_value', frm.sender_id.value,
		
		'param3_type', 'STATIC',
		'param3_name', 'cmd',
		'param3_value', 'link_notify_xml',
		
		'param4_type', 'STATIC',
		'param4_name', 'id',
		'param4_value', frm.id.value,
		
		'param5_type', 'STATIC',
		'param5_name', 'label_lan',
		'param5_value', frm.encoding.value,
		
		'param6_type', 'STATIC',
		'param6_name', 'site_id',
		'param6_value', frm.root_ent_id.value,
		
		'param7_type', 'STATIC',
		'param7_name', 'style',
		'param7_value', frm.style.value,
		
		'param8_type', 'STATIC',
		'param8_name', 'url_redirect',
		'param8_value', param8_value,//wb_utils_get_cookie('msg_' + msg_type + '_url'),
		
		'param9_type', 'STATIC',
		'param9_name', 'id_type',
		'param9_value', frm.id_type.value		
	)
	return url;
}

function _wbMsgAssignInsJIParam(frm){
	var str, ele, i, n
	str = ''
	n = frm.elements.length
	for (i=0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "radio" && ele.name.substring(0,11) == "rad_static_" && ele.checked){
			ele2 = eval('frm.' + ele.name.substring(11,ele.name.length))						
			ele2.value = ele.value;
		}else if (ele.type == "radio" && ele.name.substring(0,12) == "rad_dynamic_" && ele.checked){
			ele2 = eval('frm.dynamic_param_value_' + ele.name.substring(12,ele.name.length))			
			ele2.value = ele.value;
		}			
	}	
}

/* validate functions */
function _wbMsgValidateInsNotifySubtype(frm,lang){	

	var i,str 
	i = 0
	str = ''
	n = frm.elements.length
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.name == "msg_subtype_lst" && ele.checked){
			str += ele.value + "~"
		}		
	}
	if ( str == ''){
		return false;
	}else{
		frm.msg_subtype.value = str.substring(0, str.length-1);
	}
	return true;
}

function _wbMsgValidateInsNotifySched(frm,lang){
	var i,str 
	i = 0
	str = ''
	n = frm.elements.length
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if(ele.type == "radio" && ele.name == "msg_sched_sel" && ele.checked){
			str = ele.value
		}		
	}
	if (str == 'schedule'){
		if(!_wbMsgValidateInsNotifyDateTime(frm,lang)){
			return false;
		}else{
			return true;
		}		
	}else if (str == 'immediate'){
		frm.msg_datetime_date.value = ''
		return true;
	}
}

function _wbMsgValidateInsJIStatus(frm,lang){
	var i,str 
	i = 0
	str = ''
	n = frm.elements.length
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if(ele.type == "radio" && ele.name == "ji_status_sel" && ele.checked){
			str = ele.value
		}		
	}
	if (str == 'SYSTEM'){
		
		if(!_wbMsgValidateInsJISystemDateTime(frm,lang)){return false;}
		else{frm.ji_status.value = '1';return true;}		
		
	}else if (str == 'MANAUAL'){
		
		if(!_wbMsgValidateInsJIManualDateTime(frm,lang)){return false;}
		else{frm.ji_status.value = '2';return true;}
	}
}

function _wbMsgValidateInsJIReminder(frm,lang){
	var i,str 
	i = 0
	str = ''
	n = frm.elements.length
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if(ele.type == "radio" && ele.name == "rem_status_sel" && ele.checked){
			str = ele.value
		}		
	}
	if (str == 'REMINDER'){
		if(!_wbMsgValidateInsJIReminderDatetime(frm,lang)){
			return false;
		}else{
			frm.rem_status.value = '1'
			return true;
		}		
	}else if (str == 'NOREMINDER'){
		frm.rem_status.value = '2'
		frm.rem_send_date.value = ''
		return true;
	}
}

function _wbMsgValidateInsNotifyDateTime(frm,lang){	
	if (frm.msg_datetime_mm.value.length != 0 || frm.msg_datetime_yy.value.length != 0 || frm.msg_datetime_dd.value.length != 0 ){
		if (gen_validate_date('frm.msg_datetime',eval('wb_msg_'+lang+'_msg_sch'),lang)){
			frm.msg_datetime_date.value = frm.msg_datetime_yy.value + '-' +  frm.msg_datetime_mm.value + '-' +  frm.msg_datetime_dd.value + ' 00:00:00.0'
			return true;
		}
	}else if(frm.msg_datetime_yy.value.length == 0){
		frm.msg_datetime_yy.focus();
		alert(eval('wb_msg_' + lang + '_input_msg_sched'))
		return false;
	}
}

function _wbMsgValidateJIInsFrm(frm,lang){
	if (frm.rad_static_intro[0].checked){
		if (!gen_validate_empty_field(frm.msg_body, eval('wb_msg_' + lang + '_intro'), lang))
			return false;
	}
	if (!_wbMsgValidateJIInsRadioBtn(frm,lang)){
		alert(eval('wb_msg_' + lang + '_select_content'))
		return false;
	}
	return true;
}

function _wbMsgValidateJIInsRadioBtn(frm,lang){
	var i,str 
	i = 0
	str = ''
	n = frm.elements.length
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if(ele.type == "radio" && ele.checked && ele.value == "Y"){
			str += ele.value + '~'
		}		
	}
	if (str == ''){return false;}
	else {return true;}		
}

function wbMsgGetTargetGrouplst(frm,lang){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked) {
			if ( ele.value != "")
				str = str + ele.value + "~"
		}
	}
	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}	
	frm.target_group.value = str;		
}


function _wbMsgValidateInsJISystemDateTime(frm,lang){	
	if (frm.msg_datetime_sys_mm.value.length != 0 || frm.msg_datetime_sys_yy.value.length != 0 || frm.msg_datetime_sys_dd.value.length != 0 ){
		if (gen_validate_date('frm.msg_datetime_sys',eval('wb_msg_'+lang+'_msg_sch'),lang)){
			frm.msg_datetime.value = frm.msg_datetime_sys_yy.value + '-' +  frm.msg_datetime_sys_mm.value + '-' +  frm.msg_datetime_sys_dd.value + ' 00:00:00.0'
			/*if (!gen_validate_cur_time('frm.msg_datetime_sys',eval('wb_msg_'+lang+'_msg_sch'),lang)){
				return false;
			}*/
			return true;
		}
	}else if(frm.msg_datetime_sys_yy.value.length == 0){
		frm.msg_datetime_sys_yy.focus();
		alert(eval('wb_msg_' + lang + '_input_system_datetime'))
		return false;
	}
}

function _wbMsgValidateInsJIManualDateTime(frm,lang){	
	if (frm.msg_datetime_manu_mm.value.length != 0 || frm.msg_datetime_manu_yy.value.length != 0 || frm.msg_datetime_manu_dd.value.length != 0 ){
		if (gen_validate_date('frm.msg_datetime_manu',eval('wb_msg_'+lang+'_manual_datetime'),lang)){
			frm.msg_datetime.value = frm.msg_datetime_manu_yy.value + '-' +  frm.msg_datetime_manu_mm.value + '-' +  frm.msg_datetime_manu_dd.value + ' 00:00:00.0'
			/*if (!gen_validate_cur_time('frm.msg_datetime_manu',eval('wb_msg_'+lang+'_manual_datetime'),lang)){
				return false;
			}*/
			return true;
		}
	}else if(frm.msg_datetime_manu_yy.value.length == 0){
		frm.msg_datetime_manu_yy.focus();
		alert(eval('wb_msg_' + lang + '_input_manual_datetime'))
		return false;
	}
}

function _wbMsgValidateInsJIReminderDatetime(frm,lang){	
	if (frm.rem_send_dd.value.length != 0 || frm.rem_send_yy.value.length != 0 || frm.rem_send_dd.value.length != 0 ){
		if (gen_validate_date('frm.rem_send',eval('wb_msg_'+lang+'_reminder_datetime'),lang)){
			frm.rem_send_date.value = frm.rem_send_yy.value + '-' +  frm.rem_send_mm.value + '-' +  frm.rem_send_dd.value + ' 00:05:00.0'
			/*if (!gen_validate_cur_time('frm.rem_send',eval('wb_msg_'+lang+'_reminder_datetime'),lang)){
				return false;
			}*/
			return true;
		}
	}else if(frm.rem_send_yy.value.length == 0){
		frm.rem_send_yy.focus();
		alert(eval('wb_msg_' + lang + '_input_reminder_datetime'))
		return false;
	}
}

function _wbMsgValidateIns(fld, txtFldName){
	if (fld.type.toLowerCase().indexOf('select') != -1) {
		val = fld.options[fld.selectedIndex].value
	} else {
		val = fld.value
	}

	if (val.length == 0 || val.search(/^\s+$/) != -1) {
		Dialog.alert(wb_msg_pls_specify_value + txtFldName ,function(){
			if((fld.type=='textarea' || fld.type=='text') && fld.style.display != "none"){
				fld.focus();
			}
		});
		return false;
	}
	
	return true;
}