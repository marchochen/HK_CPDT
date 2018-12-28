// ------------------ WizBank Catalogobject ------------------- 
// Convention:
//   public functions : use "wbCataLst" prefix 
//   private functions: use "_wbCataLst" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js	 
// ------------------------------------------------------------ 

/* constructor */
function wbCataLst(){	
	this.edit_prep = wbCataLstEditCatalogPrep
	this.add_prep = wbCataLstAddCatalogPrep	
	this.del_exec = wbCataLstDelCatalogExecute
	this.mult_del_exec = wbCataLstMultDelCatalogExecute
	this.add_exec = wbCataLstAddCatalogExecute
	this.edit_exec = wbCataLstEditCatalogExecute
	this.order_cata_lst_prep = wbCataOrderCatalogListPrep
	this.order_cata_lst_exec = wbCataOrderCatalogListExec
	this.node_list = new wbNodeList
	this.show_content = wbCataGetContentByTc
}
function wbNodeList(){
	this.add_prep = wbNodeLstAddNodePrep
	this.edit_prep = wbNodeLstEditNodePrep
	this.add_exec = wbNodeLstAddNodeExecute
	this.edit_exec = wbNodeLstEditNodeExecute
	this.cancel = wbNodeLstCancel	
	this.del_exec = wbNodeLstDelNodeExecute	
	
	this.set_mobile_cata_exec = wbSetMobileCataExec		// 设置移动课程目录
	this.cancel_mobile_cata_exec = wbCancelMobileCataExec	// 取消移动课程目录
}

function wbSetMobileCataExec(id, timestamp, lang) {
	if (confirm(eval('ae_msg_'+lang+'_confirm_set_mobile'))) {
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_upd_mobile_cat_status', 'cat_id', id, 'cat_upd_timestamp', timestamp,
										 'cat_mobile_ind', true, 'url_success', self.location.href, 'url_failure', self.location.href);
		window.location.href = url;
	} else {
		return;
	}
}

function wbCancelMobileCataExec(id, timestamp, lang) {
	if (confirm(eval('ae_msg_'+lang+'_confirm_cancel_mobile'))) {
		url = wb_utils_invoke_ae_servlet('cmd', 'ae_upd_mobile_cat_status', 'cat_id', id, 'cat_upd_timestamp', timestamp,
										 'cat_mobile_ind', false, 'url_success', self.location.href, 'url_failure', self.location.href);
		window.location.href = url;
	} else {
		return;
	}
}

/* Public Function */
function wbNodeLstDelNodeExecute(id,timestamp,node_id,lang){
	if (confirm(eval('wb_msg_'+lang+'_confirm_del_node'))){
		url_success = _wb_cata_adm_node_lst_url(node_id)
		url = wb_utils_invoke_ae_servlet('cmd','ae_del_multi_tnd','tnd_id_lst',id,'tnd_upd_timestamp_lst',timestamp,'url_success',url_success,'url_failure',self.location.href)
		window.location.href = url
	}
}

function wbNodeLstAddNodeExecute(frm,lang,prt_tnd_id,cat_id,tnd_link_tnd_id){
	
	if (_wbNodeLstAddNodeValidate(frm,tnd_link_tnd_id,lang)){
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_ins_tnd'
		frm.tnd_parent_tnd_id.value = prt_tnd_id
		frm.tnd_cat_id.value = cat_id
		frm.url_success.value = _wb_cata_adm_node_lst_url(prt_tnd_id)
		frm.url_failure.value = self.location.href
		frm.method = 'post'
		wb_utils_set_cookie('tnd_title','')
		frm.submit()	
			
		
	}
}

function wbNodeLstEditNodeExecute(frm,lang,tnd_id,prt_tnd_id,link_id){
		
	if (_wbNodeLstEditNodeValidate(frm,lang)){
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_upd_tnd'
		frm.tnd_id.value = tnd_id
		frm.tnd_parent_tnd_id.value = prt_tnd_id
		frm.tnd_link_tnd_id.value = link_id
		frm.url_success.value = _wb_cata_adm_node_lst_url(tnd_id)
		frm.url_failure.value = self.location.href
		frm.method = "get"
		wb_utils_set_cookie('tnd_title','')	
		frm.submit()
	}	
}

function wbNodeLstCancel(tnd_id){
	
	wb_utils_set_cookie('tnd_title','')
	_wb_cata_adm_node_lst(tnd_id);	
}


function wbNodeLstEditNodePrep(tnd_id, show_all){
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_tnd','stylesheet','ae_adm_edit_node.xsl','tnd_id',tnd_id, 'show_all', show_all)
	window.location.href = url;
}
function wbNodeLstAddNodePrep(prt_tnd_id,link_tnd_id){
	url = wb_utils_invoke_ae_servlet('cmd','ae_prep_ins_tnd','stylesheet','ae_adm_edit_node.xsl','tnd_parent_tnd_id',prt_tnd_id,'tnd_link_tnd_id',link_tnd_id,'url_failure',self.location.href)
	window.location.href = url;
}

function wbCataOrderCatalogListPrep(){
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '300'
			+ ',height=' 				+ '220'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'no'
	url = wbCataOrderCatalogListPrepUrl()
	cata_order_lst = wbUtilsOpenWin(url,'cata_order_lst',false,str_feature);		
}

function wbCataOrderCatalogListPrepUrl(){
	url = wb_utils_invoke_ae_servlet(
		'cmd','ae_get_cat_lst',
		'stylesheet','catalog_order_lst.xsl'
	)
	return url;
}

function wbCataOrderCatalogListExec(frm){
	frm.tnd_id_lst.value = _wbCataLstGetCataOrderLst(frm)
	frm.cmd.value = 'ae_upd_tnd_order'
	frm.url_success.value = '../htm/close_and_reload_window.htm'
	frm.url_failure.value = wbCataOrderCatalogListPrepUrl()
	frm.action = wb_utils_ae_servlet_url
	frm.method = 'post'
	frm.submit()
}



function _wbCataLstGetCataOrderLst(frm) {
	var i, n, ele, str
	str = ""
	n = frm.tnd_id_sel.options.length;
	for (i = 0; i < n; i++) {
		ele = frm.tnd_id_sel.options[i]
		if (ele.value != '') {				
			if (ele.value !="")
				str = str + ele.value + "~"
		}
	}	
	if (str != "") {str = str.substring(0, str.length-1);}		
	return str;	
}

// public function
function wbCataLstEditCatalogPrep(cata_id,cat_tcr_id){
	wb_utils_set_cookie('url_prev',self.location.href)
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_cat','cat_id',cata_id,'stylesheet','ae_adm_edit_cata.xsl','url_failure',self.location.href,"cat_tcr_id",cat_tcr_id)
	window.location.href = url;
}

function wbCataLstAddCatalogPrep(show_all, cat_tcr_id){
	url = wb_utils_invoke_ae_servlet('cmd','ae_prep_ins_cat','stylesheet','ae_adm_edit_cata.xsl', 'show_all', show_all, 'cat_tcr_id', cat_tcr_id);
	window.location.href = url;
}


function wbCataLstDelCatalogExecute(id,timestamp,lang){
	if (confirm(eval('wb_msg_'+lang+'_confirm_del_cata'))){
		url_success = wb_utils_invoke_ae_servlet('cmd','ae_get_cat_lst','stylesheet','catalog_lst.xsl')
		url_failure = self.location.href
		url = wb_utils_invoke_ae_servlet('cmd','ae_del_cat','cat_id',id,'cat_upd_timestamp',timestamp,'url_success',url_success,'url_failure',url_failure)
		window.location.href = url;		
	}
}

function wbCataLstMultDelCatalogExecute(frm, lang) {
	frm.cat_id_lst.value = _wbCataGetMultiDelCatLst(frm);
	if (frm.cat_id_lst.value == '') {
		alert(eval('wb_msg_' + lang + '_sel_del_node'))
	}else{
    	if (confirm(eval('wb_msg_'+lang+'_confirm_del_cata'))){
    		frm.cat_upd_timestamp_lst.value = _wbCataGetMultiActionTimeStampeLst(frm)
			frm.cmd.value = 'ae_multi_del_cat'
			frm.url_success.value = self.location.href;
			frm.url_failure.value = self.location.href;
			frm.method = 'post'
			frm.action = wb_utils_ae_servlet_url			
			frm.submit();
		}
	}
}

function _wbCataGetMultiDelCatLst(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name != 'sel_all_checkbox') {
			if ( ele.value != "") {str = str + ele.value + "~";}
		}
	}	
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;		
}

function _wbCataGetMultiActionTimeStampeLst(frm){
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='cat_id') {
			if (ele.value !="")
				str = str + eval("frm.cat_upd_timestamp_"+ele.value+".value") + "~"
		}
	}

	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}

function wbCataLstAddCatalogExecute(frm,lang){
	//frm.cat_acc_ent_id_list.value = _wbCataLstGetCatalogUsrLst(frm,'cata_usr_lst')
	//Man: Access current root_ent_id only
	frm.cat_acc_ent_id_list.value = frm.root_ent_id.value
	//Man: ALL item types added
	//frm.ity_id_lst.value = _wbCataLstGetItemTypeLst(frm)
	frm.ity_id_lst.value =  'ALL_TYPES'
	if (_wbCataLstValidateFrm(frm,lang)){			
									
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_ins_cat'
		//frm.stylesheet.value = 'ae_adm_add_cata_done.xsl'
		frm.url_success.value = wb_utils_node_lst_url('$id');
		frm.url_failure.value = self.location.href
		frm.method = 'post'	
		frm.submit()
		
	}

}

function  wbCataLstEditCatalogExecute(frm,lang){
    var new_tcr_id;
    if (frm.cat_tcr_id) {
        new_tcr_id = frm.tcr_id.options[0].value;
    }
    
	//frm.cat_acc_ent_id_list.value = _wbCataLstGetCatalogUsrLst(frm,'cata_usr_lst')
	//Man: Access current root_ent_id only
	frm.cat_acc_ent_id_list.value = frm.root_ent_id.value	
	
	//if (frm.cat_acc_ent_id_list.value == '0') {frm.cat_acc_ent_id_list.value = frm.root_ent_id.value;}
	if (_wbCataLstValidateFrm(frm,lang)){
        if (frm.org_tcr_id && _tcrChanged(frm.org_tcr_id.value, new_tcr_id)) {
            if (confirm(wb_msg_item_moved)) {
                //do nothing
            } else {
                return;
            }
        }
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_upd_cat'
		frm.url_success.value = _wb_cata_adm_node_lst_url(frm.node_id.value,wb_utils_get_cookie('cata_view'))
		frm.url_failure.value = self.location.href
		frm.method = "post"			
		frm.submit()			
	}	

}

function _tcrChanged(org_id, new_id) {
    if (org_id && new_id) {
        if (org_id != new_id) {
            return true;
        }
    }
}

// Validation  Functions
function _wbCataLstValidateFrm(frm,lang){
	frm.cat_title.value = wbUtilsTrimString(frm.cat_title.value);
	frm.cat_desc.value = wbUtilsTrimString(frm.cat_desc.value);
	if (!gen_validate_empty_field(frm.cat_title, eval('wb_msg_' + lang + '_cata_nm'),lang)) {
		frm.cat_title.focus()
		return false;		
	} //frm.cat_title.value.length
	else if (getChars(frm.cat_title.value)  > 80){
		alert(eval('wb_msg_' + lang + '_title_length'))
		frm.cat_title.focus()
		return false;
	}
	else if (frm.cat_title.value.indexOf("\"")>-1) {
		alert('"' + eval('wb_msg_usr_title') + '"' + eval('wb_msg_' + lang + '_double_quotation_marks'));
		frm.field02_.focus()
		return false;
	}
	else if (getChars(frm.cat_desc.value) > 2000){
		alert(eval('wb_msg_' + lang + '_desc_too_long'))
		frm.cat_desc.focus();
		return false;
	} 
	else if (frm.cat_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		    alert(wb_msg_pls_input_tcr);
	        return false;
	    } else {
		    frm.cat_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	return true;
}

function _wb_cata_adm_node_lst(tnd_id,list_type,order_by,sort_order, parent_open){
	url = _wb_cata_adm_node_lst_url(tnd_id,list_type,order_by,sort_order)
	if (parent_open) {
    	window.parent.location.href = url;
	}else {
    	window.location.href = url;
	}
}

function _wb_cata_adm_node_lst_url(tnd_id,list_type,order_by,sort_order,cur_page,page_size){
	gen_set_cookie('url_failure',self.location.href)
	url_failure = ''
	if ( list_type == null || list_type == 'item' ){
		if (order_by == null){
			if (sort_order == null){
				sort_order = 'asc'
			}
			order_by = 'itm_code'
		}
		else{
			if (sort_order == null){
				sort_order = 'asc'
			}

		}
		if (cur_page == null || cur_page == '') {cur_page = '0';}
		if (page_size == null || page_size == '') {page_size = '10';}		
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_tnd_cnt_lst','tnd_id',tnd_id,'list','item','sort_col', order_by,'sort_order',sort_order,'page_size',page_size,'cur_page',cur_page,'url_failure',url_failure,'stylesheet','itm_node_lst.xsl')
	}else{
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_tnd_cnt_lst','tnd_id',tnd_id,'list',list_type,'url_failure',url_failure,'stylesheet','itm_node_lst.xsl')
	}
	return url
}

function _wbNodeLstEditNodeValidate(frm,lang){
	frm.tnd_title.value = wbUtilsTrimString(frm.tnd_title.value);
	frm.tnd_desc.value = wbUtilsTrimString(frm.tnd_desc.value);
	if (!gen_validate_empty_field(frm.tnd_title, eval('wb_msg_' + lang + '_tnd_title'),lang)) {
		frm.tnd_title.focus()
		return false;
	}else if (getChars(frm.tnd_title.value) > 80){
		alert(eval('wb_msg_' + lang + '_title_length'))
		frm.tnd_title.focus()
		return false;
	}else if (getChars(frm.tnd_desc.value) > 2000){
		alert(eval('wb_msg_' + lang + '_desc_too_long'))
		frm.tnd_desc.focus()
		return false;
	}
	
	return true;	
}

function _wbNodeLstAddNodeValidate(frm,tnd_link_tnd_id,lang){
	frm.tnd_title.value = wbUtilsTrimString(frm.tnd_title.value);
	frm.tnd_desc.value = wbUtilsTrimString(frm.tnd_desc.value);
	if (!gen_validate_empty_field(frm.tnd_title, eval('wb_msg_' + lang + '_tnd_title'),lang)) {
		frm.tnd_title.focus()
		return false;
	}else if (getChars(frm.tnd_title.value) > 80){
		alert(eval('wb_msg_' + lang + '_title_length'))
		frm.tnd_title.focus()
		return false;
	}
	
	if (getChars(frm.tnd_desc.value) > 2000){
		alert(eval('wb_msg_' + lang + '_desc_too_long'))
		frm.tnd_desc.focus()
		return false;
	}
	return true;
}

function _wbCataLstGetCatalogUsrLst(){
	var i,n,str
	str = ""
	frm = arguments[0]
	for (j = 1; j < arguments.length; j++){			
		ele = eval('frm.' + arguments[j])			
		if (ele.length > 0){			
			for (i = 0; i < ele.length ; i++){
				if (ele.options[i].value == '0'){str += frm.root_ent_id.value + "~";}			
				else if (ele.options[i].value != "") {str += ele.options[i].value + "~";}				
			}
		}else{
			str += '';
		}
	}	
	if (str != "") {str = str.substring(0, str.length-1)}
	return str;
}
function _wbCataLstGetItemTypeLst(frm){
	
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.name == "ity_id" && ele.checked) {
			if ( ele.value != "")
				str = str + ele.value + "~"
		}
	}
	
	if (str != "") {
		str = str.substring(0, str.length-1)		
	}	
	return str;	
}

function wbCataGetContentByTc(tcr_id) {
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_cat_lst', 'stylesheet', 'catalog_lst.xsl', 'cat_tcr_id', tcr_id);
	window.location.href = url;
}