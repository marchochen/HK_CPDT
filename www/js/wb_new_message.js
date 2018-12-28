function wbNewMessage() {
		this.sort_message_template_list = wbMessageTemplateSort;
		this.get_message_template_view = wbMessageTemplateView;
		this.get_message_template_prep = wbMessageTemplatePrep;
		this.upd_template = wbMessageTemplateUpdate;
};

function wbMessageTemplatePrep(id) {
	var url = wb_utils_invoke_disp_servlet('module',
			'newmessage.MessageModule', 'cmd', 'get_msg_template_view',
			'stylesheet', 'message_template_upd.xsl', 'mtp_id', id);
	window.location.href = url;
}

function wbMessageTemplateView(id){
	var url = wb_utils_invoke_disp_servlet('module',
			'newmessage.MessageModule', 'cmd', 'get_msg_template_view',
			'stylesheet', 'message_template_view.xsl', 'mtp_id', id);
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 				+ '680'
	+ ',height=' 				+ '500'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'no' 
	wbUtilsOpenWin(url, 'article', false, str_feature);
}

function wbMessageTemplateSort(frm, sort_col, sort_order) {
	var url = wb_utils_invoke_disp_servlet('module',
			'newmessage.MessageModule', 'cmd', 'get_msg_template_list',
			'stylesheet', "message_template_list.xsl", 'sort_col', sort_col, 'sort_order', sort_order);
	window.location.href = url;
}

function wbMessageTemplateUpdate(frm, lang) {
	if (editor) {
		try {
			editor.sync();
		} catch(e) {}
	}
	if(!wbUtilsValidateEmptyField(frm.mtp_subject, frm.lab_template_subject.value)){
		frm.mtp_subject.focus();
		return;
	}
	
	var val= (frm.mtp_content.value).replace(/<[^>]+>/g,"").replace(/&nbsp;/ig, '');
	
	if(val.length == 0 || val.search(/^\s+$/) != -1){
		alert(wb_msg_usr_please_specify_value + '"' + frm.lab_content.value + '"' );
		frm.mtp_content.focus();
		return;
	};
	debugger
	if ($("input[name='header_img_select']:checked").val() == 'local') {
		if ($("input[name='mtp_header_img']").val() != '') {
			var file_ext = $("input[name='mtp_header_img']").val().substring(
					$("input[name='mtp_header_img']").val().lastIndexOf(".") + 1);
			if (file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png'
					&& file_ext != 'jpeg') {
				//alert(1);
				//inorout = false ;
				alert(wb_msg_img_type_limit);
				return;
			}else{
				//inorout = true ;
			}
		} else {
			alert(wb_msg_select_uploaded_picture);
			return;
		}
	}else{
		$("input[name='image']").attr("disabled", true);
	}
	
	if ($("input[name='footer_img_select']:checked").val() == 'local') {
		if ($("input[name='mtp_footer_img']").val() != '') {
			var file_ext = $("input[name='mtp_footer_img']").val().substring(
					$("input[name='mtp_footer_img']").val().lastIndexOf(".") + 1);
			if (file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png'
					&& file_ext != 'jpeg') {
				//alert(1);
				//inorout = false ;
				alert(wb_msg_img_type_limit);
				return;
			}else{
				//inorout = true ;
			}
		} else {
			alert(wb_msg_select_uploaded_picture);
			return;
		}
	}else{
		$("input[name='image']").attr("disabled", true);
	}
	
	frm.module.value = 'newmessage.MessageModule';	
	frm.cmd.value ='upd_msg_template';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','newmessage.MessageModule','cmd', 'get_msg_template_list','stylesheet', 'message_template_list.xsl');
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','newmessage.MessageModule','cmd', 'get_msg_template_list','stylesheet', 'message_template_list.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function changeImage(thisObj,id){
	if($(thisObj).parent().children(":hidden").length > 0){
		$("#" + id).attr("src", "../"+$(thisObj).parent().children(":hidden").val());
		$("input[name='" + id + "']").val($(thisObj).parent().children(":hidden").val());
		//$("input[name='mtp_" + id + "']").attr("disabled", true);
	} else {
		$("input[name='mtp_" + id + "']").attr("disabled", false);
	}
}
