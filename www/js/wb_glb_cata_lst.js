// ------------------ WizBank GlobalCatalogobject ------------------- 
// Convention:
//   public functions : use "wbGlbCataLst" prefix 
//   private functions: use "_wbGlbCataLst" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js	 
// ------------------------------------------------------------ 

/* constructor */
function wbGlbCataLst(){
	this.get_cata_list = wbGlbCataLstGetCataLst
	this.get_node_list = wbGlbCataLstGetNodeLst
	this.add_prep = wbGlbCataLstAddCatalogPrep	
	this.add_exec = wbGlbCataLstAddCatalogExecute
	this.edit_prep = wbGlbCataLstEditCatalogPrep
	this.edit_exec = wbGlbCataLstEditCatalogExecute	
	this.del_exec = wbGlbCataLstDelCatalogExecute	
	this.node_list = new wbGlbNodeList
}
function wbGlbNodeList(){
	this.add_prep = wbGlbNodeLstAddPrep
	this.edit_prep = wbGlbNodeLstEditNodePrep
	this.add_exec = wbGlbNodeLstAddNodeExecute
	this.edit_exec = wbGlbNodeLstEditNodeExecute
	this.del_exec = wbGlbNodeLstDelNodeExecute		
}
function wbGlbCataLstGetCataLst(){
	var url = wb_utils_invoke_ae_servlet('cmd','ae_get_glb_cat_lst','stylesheet','glb_catalog_lst.xsl')
	window.location.href = url;
}

function wbGlbCataLstGetNodeLst(tnd_id, list_type, order_by, sort_order, cur_page, page_size){
	var url = _wbGlbCataAdmNodeLstUrl(tnd_id, list_type, order_by, sort_order, cur_page, page_size)
	window.parent.location.href = url;
}

function wbGlbCataLstAddCatalogPrep(){
	var url = wb_utils_invoke_ae_servlet('cmd','ae_prep_ins_glb_cat','stylesheet','glb_edit_cata.xsl')
	window.location.href = url;
}

function wbGlbCataLstEditCatalogPrep(cata_id){
	var url = wb_utils_invoke_ae_servlet('cmd','ae_get_cat','cat_id',cata_id,'stylesheet','glb_edit_cata.xsl','url_failure',self.location.href)
	window.location.href = url;
}

function wbGlbNodeLstAddPrep(prt_tnd_id,link_tnd_id){
	var url = wb_utils_invoke_ae_servlet('cmd','ae_prep_ins_tnd','stylesheet','glb_edit_node.xsl','tnd_parent_tnd_id',prt_tnd_id,'url_failure',self.location.href)
	window.location.href = url;
}

function wbGlbCataLstDelCatalogExecute(id,timestamp,lang){
	if (confirm(eval('wb_msg_'+lang+'_confirm_del_cata'))){
		var url_success = wb_utils_invoke_ae_servlet('cmd','ae_get_glb_cat_lst','stylesheet','glb_catalog_lst.xsl')
		var url_failure = self.location.href
		var url = wb_utils_invoke_ae_servlet('cmd','ae_del_cat','cat_id',id,'cat_upd_timestamp',timestamp,'url_success',url_success,'url_failure',url_failure)
		window.location.href = url;		
	}
}

function wbGlbCataLstAddCatalogExecute(frm,lang){
	frm.cat_acc_ent_id_list.value = _wbGlbCataLstGetCatalogOrgLst(frm,lang)
	frm.ity_id_lst.value = 'ALL_TYPES'
	if (_wbGlbCataLstValidateFrm(frm,lang)){			
		frm.cat_public_ind.value = 'true'
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_ins_cat'
		frm.stylesheet.value = 'glb_add_cata_done.xsl'
		frm.url_failure.value = self.location.href
		frm.method = 'post'	
		frm.submit()		
	}
}

function  wbGlbCataLstEditCatalogExecute(frm,lang){
	frm.cat_acc_ent_id_list.value = _wbGlbCataLstGetCatalogOrgLst(frm,lang)
	frm.ity_id_lst.value = 'ALL_TYPES'
	if (_wbGlbCataLstValidateFrm(frm,lang)){
		frm.cat_public_ind.value = 'true'	
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_upd_cat'
		frm.url_success.value = _wbGlbCataAdmNodeLstUrl(frm.node_id.value)
		frm.url_failure.value = self.location.href
		frm.method = "post"			
		frm.submit()			
	}	

}

function wbGlbNodeLstEditNodePrep(tnd_id){
	url = wb_utils_invoke_ae_servlet('cmd','ae_get_tnd','stylesheet','glb_edit_node.xsl','tnd_id',tnd_id)
	window.location.href = url;
}

function wbGlbNodeLstAddNodeExecute(frm,lang,prt_tnd_id,cat_id){
	if (_wbGlbNodeLstValidateNode(frm,lang)){
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_ins_tnd'
		frm.tnd_parent_tnd_id.value = prt_tnd_id
		frm.tnd_cat_id.value = cat_id
		frm.url_success.value = _wbGlbCataAdmNodeLstUrl(prt_tnd_id)
		frm.url_failure.value = self.location.href
		frm.method = 'post'
		frm.submit()	
	}
}

function wbGlbNodeLstEditNodeExecute(frm,lang,tnd_id,prt_tnd_id){
		
	if (_wbGlbNodeLstValidateNode(frm,lang)){
		frm.action = wb_utils_ae_servlet_url
		frm.cmd.value = 'ae_upd_tnd'
		frm.tnd_id.value = tnd_id
		frm.tnd_parent_tnd_id.value = prt_tnd_id
		frm.url_success.value = _wbGlbCataAdmNodeLstUrl(tnd_id)
		frm.url_failure.value = self.location.href
		frm.method = "post"
		frm.submit()
	}	
}



function wbGlbNodeLstDelNodeExecute(id,timestamp,node_id,lang){
	if (confirm(eval('wb_msg_'+lang+'_confirm_del_node'))){
		url_success = _wbGlbCataAdmNodeLstUrl(node_id)
		url = wb_utils_invoke_ae_servlet('cmd','ae_del_multi_tnd','tnd_id_lst',id,'tnd_upd_timestamp_lst',timestamp,'url_success',url_success,'url_failure',self.location.href)
		window.location.href = url
	}
}

//private functions
function _wbGlbCataLstGetCatalogOrgLst(frm,lang){
	if(frm.org_ind && frm.org_ind[1].checked == true){
		if(frm.ent_id && frm.ent_id.length){
			var i=0;
			var temp_list = '';
			for(i=0;i<frm.ent_id.length;i++){
				if(frm.ent_id[i].checked){
					temp_list += frm.ent_id[i].value + '~'
				}
			}
			return temp_list;
		}else if(frm.ent_id.value && frm.ent_id.checked){
			return frm.ent_id.value;
		}else{
			return '';
		}
	}else{
		return 0;
	}
}

function _wbGlbCataLstValidateFrm(frm,lang){
	frm.cat_title.value = wbUtilsTrimString(frm.cat_title.value);
	frm.cat_desc.value = wbUtilsTrimString(frm.cat_desc.value);
	if (!gen_validate_empty_field(frm.cat_title, eval('wb_msg_' + lang + '_cata_nm'),lang)) {
		frm.cat_title.focus()
		return false;
		
	}else if (frm.cat_title.value.length > 100){
		alert(eval('wb_msg_' + lang + '_title_length'))
		frm.cat_title.focus()
		return false;
	}
	if(frm.cat_acc_ent_id_list.value == ''){
		alert(eval('wb_msg_'+lang+'_select_org'))
		return false;
	}	
	if(frm.cat_desc.value.length > 1000) {
		alert(eval('wb_msg_'+lang+'_desc_too_long'));
		frm.cat_desc.focus();
		return false;
	}
	return true;
}

function _wbGlbNodeLstValidateNode(frm,lang){	
	if (!gen_validate_empty_field(frm.tnd_title, eval('wb_msg_' + lang + '_tnd_title'),lang)) {
		frm.tnd_title.focus()
		return false;
	}else if (frm.tnd_title.value.length > 100){
		alert(eval('wb_msg_' + lang + '_title_length'))
		frm.tnd_title.focus()
		return false;
	}else if (frm.tnd_desc.value.length > 1000){
		alert(eval('wb_msg_' + lang + '_desc_too_long'))
		frm.tnd_desc.focus()
		return false;
	}
	return true;
}

function _wbGlbCataAdmNodeLstUrl(tnd_id,list_type,order_by,sort_order,cur_page,page_size){
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
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_tnd_cnt_lst','tnd_id',tnd_id,'list','item','sort_col', order_by,'sort_order',sort_order,'page_size',page_size,'cur_page',cur_page,'url_failure',url_failure,'stylesheet','glb_itm_node_lst.xsl')
	}else{
		url = wb_utils_invoke_ae_servlet('cmd','ae_get_tnd_cnt_lst','tnd_id',tnd_id,'list',list_type,'url_failure',url_failure,'stylesheet','glb_itm_node_lst.xsl')
	}
	return url
}