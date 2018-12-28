// ------------------ ApplyEasy Node List Object --------------
// Convention:
//   public functions : use "aeNodeLst" prefix 
//   private functions: use "_aeNodeLst" prefix
// Dependency:
//   gen_utils.js
//   ae_utils.js	 
// ------------------------------------------------------------ 

/* constructor */
function aeNodeLst(){	
	
	this.edit_prep = aeNodeLstEditNodePrep
	this.add_prep = aeNodeLstAddNodePrep
	this.pick_node_prep = aeNodeLstPickNodePrep
	
	this.add_exec = aeNodeLstAddNodeExecute
	this.edit_exec = aeNodeLstEditNodeExecute
	this.cancel = aeNodeLstCancel
	//this.del_multi_exec = aeNodeLstDelMultiNodeExecute	// no longer uesed
	this.del_exec = aeNodeLstDelNodeExecute	
	
	this.pick_cata_lst = aeNodeLstPickCatalogLst
	this.pick_node_lst = aeNodeLstPickNodeLst
	this.pick_show_lst = aeNodeLstPickShowNodeLst
	this.pick_node_exec = aeNodeLstPickNodeExecute
	
}

// public function
function aeNodeLstEditNodePrep(tnd_id){
	
	url = ae_utils_invoke_servlet('cmd','ae_get_tnd','stylesheet',ae_utils_xsl_adm_edit_node,'tnd_id',tnd_id)
	window.location.href = url;
}

function aeNodeLstAddNodePrep(prt_tnd_id,link_tnd_id){
	url = ae_utils_invoke_servlet('cmd','ae_prep_ins_tnd','stylesheet',ae_utils_xsl_adm_edit_node,'tnd_parent_tnd_id',prt_tnd_id,'tnd_link_tnd_id',link_tnd_id,'url_failure',self.location.href)
	window.location.href = url;
}

/*
function aeNodeLstDelMultiNodeExecute(frm,lang){

	frm.tnd_id_lst.value = _aeNodeLstGetNodeId(frm)
	if (frm.tnd_id_lst.value == ''){
		alert(eval('ae_msg_' + lang + '_sel_del_node'))
	}else if (confirm(eval('ae_msg_'+lang+'_confirm_del_node'))){
		
		frm.tnd_upd_timestamp_lst.value = _aeNodeLstGetTimeStamp(frm)
		
		frm.action = ae_utils_servlet_url
		frm.cmd.value = 'ae_del_multi_tnd'
		frm.url_success.value = self.location.href
		frm.url_failure.value = self.location.href
		
		frm.method = "post"
		frm.submit()
	}
}
*/

function aeNodeLstDelNodeExecute(id,timestamp,node_id,lang){

	if (confirm(eval('ae_msg_'+lang+'_confirm_del_node'))){
		
		url_success = ae_utils_adm_node_lst_url(node_id)
		url = ae_utils_invoke_servlet('cmd','ae_del_multi_tnd','tnd_id_lst',id,'tnd_upd_timestamp_lst',timestamp,'url_success',url_success,'url_failure',self.location.href)
		window.location.href = url
	}
}


function aeNodeLstAddNodeExecute(frm,lang,prt_tnd_id,cat_id,tnd_link_tnd_id){
	
	if (_aeNodeLstAddNodeValidate(frm,tnd_link_tnd_id,lang)){
		frm.action = ae_utils_servlet_url
		frm.cmd.value = 'ae_ins_tnd'
		frm.tnd_parent_tnd_id.value = prt_tnd_id
		frm.tnd_cat_id.value = cat_id
		frm.url_success.value = ae_utils_adm_node_lst_url(prt_tnd_id)
		frm.url_failure.value = self.location.href
		
		frm.method = 'post'
		ae_utils_set_cookie('tnd_title','')
		frm.submit()	
			
		
	}
}

function aeNodeLstEditNodeExecute(frm,lang,tnd_id,prt_tnd_id,link_id){
		
	if (_aeNodeLstEditNodeValidate(frm,lang)){
		frm.action = ae_utils_servlet_url
		frm.cmd.value = 'ae_upd_tnd'
		frm.tnd_id.value = tnd_id
		frm.tnd_parent_tnd_id.value = prt_tnd_id
		frm.tnd_link_tnd_id.value = link_id
		frm.url_success.value = ae_utils_adm_node_lst_url(tnd_id)
		frm.url_failure.value = self.location.href
		frm.method = 'get'
		ae_utils_set_cookie('tnd_title','')	
		frm.submit()
	}	
}

function aeNodeLstCancel(tnd_id){
	
	ae_utils_set_cookie('tnd_title','')
	ae_utils_adm_node_lst(tnd_id);	
}

function aeNodeLstPickNodePrep(tnd_parent,tnd_id,tnd_link_tnd_id,tnd_status){
	
	var doAll = (document.all!=null) // IE
	var doDOM =(document.getElementById!=null) //Netscape 6 DOM 1.0
	var doLayer =(document.layers!=null) // Netscape 4.x
	
	/*
	if (doAll) {
		var tnd_title_encode = encodeURI(frm.tnd_title.value);
	}else if (doDOM || doLayer){
		var tnd_title_encode = escape(frm.tnd_title.value)
	}
	*/
	// use cookie by marcus
	ae_utils_set_cookie('tnd_title',frm.tnd_title.value);
		
	//tnd_title_encode = encodeURI(frm.tnd_title.value)
	
	//tnd_title,tnd_status,tnd_parent,tnd_id,tnd_link_tnd_id
	//url = ae_utils_invoke_servlet('cmd','ae_pick_tnd','stylesheet',ae_utils_xsl_adm_pick_cata_lst,'tnd_parent_tnd_id',tnd_parent,'tnd_id',tnd_id,'tnd_link_tnd_id',tnd_link_tnd_id,'tnd_title',tnd_title_encode)
	url = ae_utils_invoke_servlet('cmd','ae_pick_tnd','stylesheet',ae_utils_xsl_adm_pick_cata_lst,'tnd_parent_tnd_id',tnd_parent,'tnd_id',tnd_id,'tnd_link_tnd_id',tnd_link_tnd_id)
	//window.location.href = url;
	/*frm.action = ae_utils_servlet_url
	frm.cmd.value = 'ae_pick_tnd'
	frm.stylesheet.value = ae_utils_xsl_adm_pick_cata_lst
	frm.tnd_id.value = tnd_id
	frm.tnd_status = tnd_status
	frm.tnd_parent_tnd_id.value = tnd_parent
	frm.tnd_link_tnd_id.value = tnd_link_tnd_id
	frm.method = "get"
	frm.submit ()*/
	
	str_feature = 'toolbar='		+ 'no'
			+ ',titlebar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '440'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
	wbUtilsOpenWin(url, 'pick_node', false, str_feature);			
	//parent.location.href = url
}


function aeNodeLstPickCatalogLst(){
	
	url = ae_utils_invoke_servlet('cmd','ae_get_cat_lst','stylesheet',ae_utils_xsl_adm_pick_cata_lst)
	window.location.href = url;
	
}

function aeNodeLstPickNodeLst(tnd_id){
	url = ae_utils_invoke_servlet('cmd','ae_get_tnd_cnt_lst','tnd_id',tnd_id,'stylesheet',ae_utils_xsl_adm_pick_node_lst,'url_failure',self.location.href)
	window.location.href = url;
		
}

function aeNodeLstPickShowNodeLst(){

}

function aeNodeLstPickNodeExecute(tnd_id){
	
	url = ae_utils_invoke_servlet('cmd','ae_pick_tnd_done','tnd_link_tnd_id',tnd_id,'stylesheet',ae_utils_xsl_adm_edit_node)
	opener.location.href = url
	window.close()
}

// Validation  Functions
function _aeNodeLstAddNodeValidate(frm,tnd_link_tnd_id,lang){
	
	if (!gen_validate_empty_field(frm.tnd_title, eval('ae_msg_' + lang + '_tnd_title'),lang)) {
		frm.tnd_title.focus()
		return false;
	}else if (frm.tnd_title.value.length >= 50){
		alert(eval('ae_msg_' + lang + '_title_length'))
		frm.tnd_title.focus()
		return false;
	}else if (frm.tnd_desc.value.length > 100){
		alert(eval('wb_msg_' + lang + '_desc_length'))
		frm.tnd_desc.focus()
		return false;
	}
	return true;
}

function _aeNodeLstEditNodeValidate(frm,lang){
	if (!gen_validate_empty_field(frm.tnd_title, eval('ae_msg_' + lang + '_tnd_title'),lang)) {
		frm.tnd_title.focus()
		return false;
	}else if (frm.tnd_title.value.length >= 50){
		alert(eval('ae_msg_' + lang + '_title_length'))
		frm.tnd_title.focus()
		return false;
	}else if (frm.tnd_desc.value.length > 100){
		alert(eval('wb_msg_' + lang + '_desc_length'))
		frm.tnd_title.focus()
		return false;
	}
	
	return true;	
}

function _aeNodeLstGetNodeId(frm) {
		var i, n, ele, str
		str = ''
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == 'checkbox' && ele.checked) {
				if ( ele.value != '')
					str = str + ele.value + '~'
			}
		}
		
		if (str != '') {
			str = str.substring(0, str.length-1);
		}
		return str;	
}

function _aeNodeLstGetTimeStamp(frm) {

		var i, n, ele, str
		str = ''
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == 'checkbox' && ele.checked) {
				if (ele.id !='')
					str = str + ele.id + '~'
			}
		}
		
		if (str != '') {
			str = str.substring(0, str.length-1);
		}
		return str;	
}