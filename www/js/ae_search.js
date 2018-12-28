// ------------------ ApplyEasy Search object ------------------- 
// Convention:
//   public functions : use "aeSearch" prefix 
//   private functions: use "_aeSearch" prefix
// Dependency:
//   gen_utils.js
//   ae_utils.js
// ------------------------------------------------------------ 

/* constructor */
function aeSearch() {
	
	this.simple = aeSearchSimple
	this.simple_page = aeSearchSimplePage
	this.adv_prep = aeSearchAdvancedPrep
	this.adv_exec = aeSearchAdvancedExec
	
	this.lrn_simple = aeSearchLearnerSimple
	this.lrn_simple_page = aeSearchLearnerSimplePage
	this.lrn_adv_prep = aeSearchLearnerAdvancedPrep
	this.lrn_adv_exec = aeSearchLearnerAdvancedExec
}

//private functions



//public functions 
function aeSearchSimple(frm,lang){

	if (_aeSearhValidateSimple(frm,lang)){			

//		frm.cmd.value = 'ae_search_itm'
		frm.cmd.value = 'ae_lookup_itm'
//		frm.phrase.value = frm.search.value
		frm.title.value = frm.search.value		
		frm.stylesheet.value = ae_utils_xsl_adm_simple_seh_result
		frm.url_failure.value = self.location.href
		frm.action = ae_utils_servlet_url		
		frm.method = 'post'
		frm.submit();
	}
}

function aeSearchSimplePage(frm,lang,page,search_timestamp,orderby,sortorder) {

		if(orderby != null)
		  frm.orderby.value = orderby
		else
		  frm.orderby.value = 'itm_code'
		if(sortorder != null)
		  frm.sortorder.value = sortorder  
		else
		  frm.sortorder.value = 'asc'
//		frm.cmd.value = 'ae_search_itm'
		frm.cmd.value = 'ae_lookup_itm'
//		frm.phrase.value = frm.search.value
//		frm.title.value = frm.search.value
		frm.stylesheet.value = ae_utils_xsl_adm_simple_seh_result
		frm.url_failure.value = self.location.href

		frm.page.value = page

		frm.timestamp.value = search_timestamp;

		frm.action = ae_utils_servlet_url
		frm.method = 'post'
		frm.submit();

}

function aeSearchAdvancedPrep(frm){
		
		url = ae_utils_invoke_servlet('cmd','ae_get_prof','stylesheet',ae_utils_xsl_adm_adv_seh,'nav_id',frm.tnd_id.value,'nav_type','CATALOG')
		window.location.href = url;
}

function aeSearchAdvancedExec(frm,lang){	
		
	if (_aeSearhValidateAdvance(frm,lang)){
//		frm.cmd.value = 'ae_search_itm'
		frm.cmd.value = 'ae_lookup_itm'
		frm.url_failure.value = self.location.href
		frm.stylesheet.value = ae_utils_xsl_adm_adv_seh_result
		frm.action = ae_utils_servlet_url
		frm.method = 'post'
		frm.submit();
	}		
}

function aeSearchLearnerSimple(frm,lang){

	if (_aeSearhValidateSimple(frm,lang)){	
//		frm.cmd.value = 'ae_search_itm'
		frm.cmd.value = 'ae_lookup_itm'
//		frm.phrase.value = frm.search.value
		frm.title.value = frm.search.value
		frm.stylesheet.value = ae_utils_xsl_lrn_simple_seh_result
		frm.url_failure.value = self.location.href
		frm.action = ae_utils_servlet_url		
		frm.method = 'post'
		frm.submit();
	}
}

function aeSearchLearnerSimplePage(frm,lang,page,search_timestamp,orderby,sortorder) {
	
		if(orderby != null)
		  frm.orderby.value = orderby
		else
		  frm.orderby.value = 'itm_code'  

		if(sortorder != null)
		  frm.sortorder.value = sortorder  
		else
		  frm.sortorder.value = 'asc'
//		frm.cmd.value = 'ae_search_itm'
		frm.cmd.value = 'ae_lookup_itm'
//		frm.phrase.value = frm.search.value
		frm.stylesheet.value = ae_utils_xsl_lrn_simple_seh_result
		frm.page.value = page;
		frm.timestamp.value = search_timestamp;
		frm.url_failure.value = self.location.href
		frm.action = ae_utils_servlet_url		
		frm.method = 'post'
		frm.submit();
					
}



function aeSearchLearnerAdvancedPrep(frm){
	
		url = ae_utils_invoke_servlet('cmd','ae_get_prof','stylesheet',ae_utils_xsl_lrn_adv_seh,'nav_id',frm.tnd_id.value,'nav_type','CATALOG')
		window.location.href = url;
}

function aeSearchLearnerAdvancedExec(frm,lang){	
	
	if (_aeSearhValidateAdvance(frm,lang)){				
//		frm.cmd.value = 'ae_search_itm'
		frm.cmd.value = 'ae_lookup_itm'		
		frm.url_failure.value = self.location.href
		frm.stylesheet.value = ae_utils_xsl_lrn_adv_seh_result
		frm.action = ae_utils_servlet_url
		frm.method = 'post'
		frm.submit();
	}
}

//Validation Function

function _aeSearhValidateSimple(frm,lang){
	
	if(!gen_validate_empty_field(frm.search,eval('ae_msg_'+lang+'_search_field'),lang)){
		frm.search.focus()
		return false;
	}
	return true;
}

function _aeSearhValidateAdvance(frm,lang){
	
	if (!_aeSearchValidateAdvanceEmpty(frm,lang)){		
		return false;
	}
	if(!_aeSearchValidateAdvanceDateTime(frm,lang)){
		return false;
	}
	return true;			
}

function _aeSearchValidateAdvanceEmpty(frm,lang){
	var i,str 
	i = 0
	str = ''
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == 'text') {
			str += ele.value
		}else if(ele.type == 'checkbox' && ele.checked){
			str += ele.value		
		}else if(ele.name == 'status' && ele.selectedIndex != 0){
			str += ele.value			
		}
		
	}
	if ( str == ''){
		alert(eval('ae_msg_' + lang + '_input_seh_fld'))
		frm.code.focus()
		return false;
	}else{
		return true;
	}
}

function _aeSearchValidateAdvanceDateTime(frm,lang){
	
	if (frm.appn_from_dd.value.length != 0 || frm.appn_from_yy.value.length != 0 || frm.appn_from_dd.value.length != 0 ){
		if (gen_validate_date('document.frmAction.appn_from',eval('ae_msg_'+lang+'_appn_date'),lang)){
			frm.appn_from.value = frm.appn_from_yy.value + '-' +  frm.appn_from_mm.value + '-' +  frm.appn_from_dd.value + ' 00:00:00.0'
		}else{
			return false;
		}
	}	
	
	if (frm.appn_to_mm.value.length != 0 || frm.appn_to_yy.value.length != 0 || frm.appn_to_dd.value.length != 0 ){
		if (gen_validate_date('document.frmAction.appn_to',eval('ae_msg_'+lang+'_appn_date'),lang)){
			frm.appn_to.value = frm.appn_to_yy.value + '-' +  frm.appn_to_mm.value + '-' +  frm.appn_to_dd.value + ' 23:59:59.0'
		}else{
			return false;
		}
	}
	
	if (frm.eff_from_dd.value.length != 0 || frm.eff_from_yy.value.length != 0 || frm.eff_from_dd.value.length != 0 ){
		if (gen_validate_date('document.frmAction.eff_from',eval('ae_msg_'+lang+'_eff_date'),lang)){
			frm.eff_from.value = frm.eff_from_yy.value + '-' +  frm.eff_from_mm.value + '-' +  frm.eff_from_dd.value + ' 00:00:00.0'
		}else{
			return false;
		}
	}
	
	if (frm.eff_to_mm.value.length != 0 || frm.eff_to_yy.value.length != 0 || frm.eff_to_dd.value.length != 0 ){
		if (gen_validate_date('document.frmAction.eff_to',eval('ae_msg_'+lang+'_eff_date'),lang)){
			frm.eff_to.value = frm.eff_to_yy.value + '-' +  frm.eff_to_mm.value + '-' +  frm.eff_to_dd.value + ' 23:59:59.0'
		}else{
			return false;
		}
	}
	return true;
		
}