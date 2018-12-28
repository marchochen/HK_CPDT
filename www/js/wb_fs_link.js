function wbFsLink() {
	this.get_fslink_prep = wbFriendShipLinkPrep
	this.ins_upd_fslink = wbFriendShipLinkInsUpd
	this.del_fslink = wbFriendShipLinkDel
	this.sort_fslink_list = wbFriendShipLinkSort
}

function wbFriendShipLinkPrep(fsl_id) {
	var url = wb_utils_invoke_disp_servlet('module',
			'JsonMod.links.LinksModule', 'cmd', 'get_fslink_prep',
			'stylesheet', 'fs_link_ins_upd.xsl', 'fsl_id', fsl_id);
	window.location.href = url;
}

function wbFriendShipLinkSort(sort_col, sort_order) {
	var url = wb_utils_invoke_disp_servlet('module',
			'JsonMod.links.LinksModule', 'cmd', 'get_fslinks_list',
			'stylesheet', 'fs_links_list.xsl', 'sort_col', sort_col, 'sort_order', sort_order);
	window.location.href = url;
}

function wbFriendShipLinkInsUpd(frm, lang) {
	
	if(!wbUtilsValidateEmptyField(frm.fsl_title, frm.lab_link_title.value)){
		frm.fsl_title.focus()
		return;
	}
	
	_url = frm.fsl_url.value.toLowerCase()
	if(!wbUtilsValidateEmptyField(frm.fsl_url, frm.lab_link_url.value)){
		frm.fsl_url.focus()
		return;
	}
	
	if( _url == 'http://'){
		alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_link_url.value + '\"')
		frm.fsl_url.focus()
		return false;
	}
	if ( _url.substring(0,7) != "http://"){
		alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_link_url.value + '\"')
		frm.fsl_url.focus()
		return false;
	}
	
	if(!wbUtilsValidateEmptyField(frm.fsl_url, frm.lab_link_url.value)){
		frm.fsl_url.focus()
		return;
	}
	frm.module.value = 'JsonMod.links.LinksModule';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','JsonMod.links.LinksModule','cmd', 'get_fslinks_list','stylesheet', 'fs_links_list.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','JsonMod.links.LinksModule','cmd', 'get_fslinks_list','stylesheet', 'fs_links_list.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function wbFriendShipLinkDel(frm) {
		var isMultiple = false;
	
		frm.fsl_id_lst.value = "";
		frm.fsl_update_timestamp_lst.value = "";
		if(frm.fsl_id){	
			if(frm.fsl_id.length){
				for(i = 0; i < frm.fsl_id.length; i++){
					if(frm.fsl_id[i].checked){
						isMultiple = true;
		
						frm.fsl_id_lst.value += frm.fsl_id[i].value + "~";
						frm.fsl_update_timestamp_lst.value += frm.link_timestamp[i].value + "~";
					}
				}
				if(isMultiple == true){
					frm.fsl_id_lst.value = frm.fsl_id_lst.value.substr(0, frm.fsl_id_lst.value.length - 1);
					frm.fsl_update_timestamp_lst.value = frm.fsl_update_timestamp_lst.value.substr(0, frm.fsl_update_timestamp_lst.value.length - 1);
				}
			}else if(frm.fsl_id.checked){
				frm.fsl_id_lst.value = frm.fsl_id.value;
				frm.fsl_update_timestamp_lst.value = frm.link_timestamp.value;
			}
		}
		if(frm.fsl_id_lst.value == ""){
			alert(eval('wb_msg_sel_del_link'));
		}else if(confirm(eval('wb_msg_del_link_confirm'))){
			frm.module.value = 'JsonMod.links.LinksModule';
			frm.cmd.value = 'del_link';
			frm.url_success.value = self.location.href;
			frm.url_failure.value = self.location.href;
			frm.method = 'post';
			frm.action = wb_utils_disp_servlet_url;
			frm.submit();
		}
		return;
}