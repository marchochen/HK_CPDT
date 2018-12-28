// ------------------ wizBank Search object ------------------- 
// Convention:
//   public functions : use "wbSearch" prefix 
//   private functions: use "_wbSearch" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbSearch() {
	this.id_exact = '';
	this.id_after = '';
	this.id_before = '';
	this.simple = wbSearchSimple
	this.adv_prep = wbSearchAdvancedPrep
	this.adv_exec = wbSearchAdvancedExec
	this.chg_id_criteria=wbSearchChangeIdCriteria
	this.transfer_val = wbSearchTransferValue
	this.changeSec = wbSearchChangeSec
	this.previous_page = wbSearchResultPrevious
	this.next_page = wbSearchResultNext
}


function _wbSearchValidateCompareField(frm,field_name,txtFieldName,lang){

	fld_before = 'frm.' + field_name + '_before'
	fld_after = 'frm.' + field_name + '_after'
	if ( eval(fld_before).value.length != 0 ){
		if ( !gen_validate_positive_integer(eval(fld_before),txtFieldName,lang))
				return false;
	}
	
	if( eval(fld_after).value.length !=0 ){
		if ( !gen_validate_positive_integer(eval(fld_after),txtFieldName,lang) )
				return false;
	}
	if (eval(fld_before).value.length != 0  && eval(fld_after).value.length !=0 && Number(eval(fld_before).value) < Number(eval(fld_after).value) ) {
		alert(eval('wb_msg_' + lang + '_from_larger_to'));
		return false;
	}				
	return true;	
}


function _wbSearchValidateTwoDateField(frm,fldName,txtfldName,lang){
		
		var before = 0
		var	after = 0
				
		fld_pre = 'frm.' + fldName	
		
	
		if ( eval(fld_pre + '_a_yy').value.length != 0 || eval(fld_pre + '_a_mm').value.length != 0 || eval(fld_pre + '_a_dd').value.length != 0 ) {
			before = 1
			if ( !wbUtilsValidateDate('document.adv_search.'+ fldName + '_a',txtfldName)){
				return false		
			}				
		}

	
		if ( eval(fld_pre + '_b_yy').value.length != 0 || eval(fld_pre + '_b_mm').value.length != 0 || eval(fld_pre + '_b_dd').value.length != 0  ) {
			after = 1			
			if ( !wbUtilsValidateDate('document.adv_search.'+fldName + '_b',txtfldName))
				return false

		}	
		
		// compare the dates
		if(eval(fld_pre + '_a_yy').value.length != 0 && eval(fld_pre + '_b_yy').value.length != 0) {
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : fldName + '_a', 
				end_obj : fldName + '_b'
				})) {
				return false;
			}
		}
			
			
	return true

}


function _wbSearchValidateDifficulty(frm){

	if (frm.search_diff_before.value != 0  && frm.search_diff_after.value !=0 && frm.search_diff_before.value < frm.search_diff_after.value ) {
		temp = frm.search_diff_before.value;
		frm.search_diff_before.value = frm.search_diff_after.value
		frm.search_diff_after.value = temp				
	}				
	
}


function _wbSearchValidateFrm(frm,lang){
	
	if (frm.search_id.value != '' && !gen_validate_integer(frm.search_id,eval('wb_msg_'+lang+'_res_id'),lang)) {
			return false
	}
	
	if (!_wbSearchValidateCompareField(frm,'search_id',eval('wb_msg_'+lang+'_res_id'),lang)) {
			return false
	}
	
	if (!_wbSearchValidateTwoDateField(frm,'c',eval('wb_msg_'+lang+'_create_time'),lang))
			return false
	
	if (!_wbSearchValidateTwoDateField(frm,'u',eval('wb_msg_'+lang+'_upd_time'),lang))
			return false
	
	if (frm.search_dur && !_wbSearchValidateCompareField(frm,'search_dur',eval('wb_msg_'+lang+'_duration'),lang)) {
			return false
	}
	
	/*_wbSearchValidateDifficulty(frm)*/
		
		
	return true
		
}

function _wbSearchGetCSSPElement(id) {
        if (doAll) 
          return document.all[id]
        else
          return document.layers[id]
}


function _wbSearchWriteElement(id, contents) {
        var pEl = _wbSearchGetCSSPElement(id)
        if (pEl!=null)
          if (doAll)
            pEl.innerHTML = contents
          else {
            pEl.document.open()
            pEl.document.write(contents) 
            pEl.document.close()
          }
}




/* public functions */

function wbSearchSimple(frm,lang){

	if (_wbSearchSimple(frm,lang)){
		frm.action = wb_utils_servlet_url
		frm.stylesheet.value = 'res_search_result.xsl'	
		frm.cmd.value = 'simple_search'
		frm.search_title.value = frm.search.value
		frm.search_items_per_page.value = '20'		
		frm.method = "get";
		wb_utils_set_cookie('search_for',frm.search_title.value);
		frm.submit();
	}
}
function _wbSearchSimple(frm,lang){
	
	if(!gen_validate_empty_field(frm.search,eval('wb_msg_'+lang+'_search_field'),lang)){
		frm.search.focus()
		return false;
	}
	return true;
}

function wbSearchAdvancedPrep(){
		var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','res_adv_search.xsl');
		window.location.href = url;

}

function wbSearchAdvancedExec(frm,lang){
		
	if (_wbSearchValidateFrm(frm,lang)){	

	
		frm.action = wb_utils_servlet_url
		frm.stylesheet.value = 'res_search_result.xsl'	
		if ( frm.c_a_yy.value.length != 0 )
			frm.search_create_time_after.value = frm.c_a_yy.value+ '-' + frm.c_a_mm.value + '-' + frm.c_a_dd.value + ' 00:00:00'
		else
			frm.search_create_time_after.value = ""
		if ( frm.c_b_yy.value.length != 0 )
			frm.search_create_time_before.value = frm.c_b_yy.value + '-' + frm.c_b_mm.value + '-' + frm.c_b_dd.value + ' 23:59:59'
		else
			frm.search_create_time_before.value = ""
		if ( frm.u_a_yy.value.length != 0 )
			frm.search_update_time_after.value = frm.u_a_yy.value + '-' + frm.u_a_mm.value + '-' + frm.u_a_dd.value + ' 00:00:00'
		else
			frm.search_update_time_after.value = ""
		if ( frm.u_b_yy.value.length != 0 )
			frm.search_update_time_before.value = frm.u_b_yy.value + '-' + frm.u_b_mm.value + '-' + frm.u_b_dd.value + ' 23:59:59'
		else
			frm.search_update_time_before.value = ""
		
		if(frm.search_diff_lst) {
			frm.search_diff_lst.value = "";
			var delimiter = "";
			if(frm.chk_diff_easy.checked) {
				frm.search_diff_lst.value += '1';
				delimiter = "~";
			}
			if(frm.chk_diff_normal.checked) {
				frm.search_diff_lst.value += delimiter + '2';
				delimiter = "~";
			}
			if(frm.chk_diff_hard.checked) {
				frm.search_diff_lst.value += delimiter + '3';
			}
			if (frm.search_diff_lst.value == '1~2~3'){
				frm.search_diff_lst.value = '';
			}
		}

		wb_utils_set_cookie('search_for',frm.search_title.value);
		frm.method = "get";
		frm.submit();
	}
}

function wbSearchResultPrevious(index) {
index -= 21;
url = wb_utils_invoke_servlet('cmd','search_result','search_items_per_page','20','search_start_index',index,'stylesheet','res_search_result.xsl')
		window.location.href = url;	
	//frm.submit();
}

function wbSearchResultNext(index) {
index += 19;
url = wb_utils_invoke_servlet('cmd','search_result','search_items_per_page','20','search_start_index',index,'stylesheet','res_search_result.xsl')
		window.location.href = url;	
	//frm.submit();	
}


function wbSearchChangeIdCriteria(frm) {
		
		if (frm.rdo_search_id[0].checked) {
			if (frm.search_id.value == '') {
				frm.search_id.value = this.id_exact;
			}			
			if (frm.search_id_after.value != '') {
				this.id_after = frm.search_id_after.value;
				frm.search_id_after.value = '';
			}		
			if (frm.search_id_before.value != '') {
				this.id_before = frm.search_id_before.value;
				frm.search_id_before.value = '';
			}
		} else {
			if (frm.search_id_after.value == '') {
				frm.search_id_after.value = this.id_after;
			}
			if (frm.search_id_before.value == '') {
				frm.search_id_before.value = this.id_before;
			}
			if (frm.search_id.value != '') {
				this.id_exact = frm.search_id.value;
				frm.search_id.value = '';
			}
		}
}


function wbSearchTransferValue(frm){

	 	frm = document.adv_search
	 	frm.search_que_type.value = ""
	 	frm.search_mod_type.value =  ""
		
		type = frm.search_type.value
	 	
	 	if (type == 'QUE')
		{
			if (doAll)
			{
				i = document.frm_que.que_type.selectedIndex
				frm.search_que_type.value = document.frm_que.que_type.options[i].value
			}
			else
			{
				i =document['layer1'].document.frm_que.que_type.selectedIndex;
				frm.search_que_type.value = document['layer1'].document.frm_que.que_type.options[i].value;
				
			}
				
		}
		else if ( type == 'MOD' )
		{
			if (doAll)
				{
					i = document.frm_mod.mod_type.selectedIndex
					frm.search_mod_type.value = document.frm_mod.mod_type.options[i].value
				
				}
				else
				{
					i =document['layer1'].document.frm_mod.mod_type.selectedIndex;
					frm.search_mod_type.value = document['layer1'].document.frm_mod.mod_type.options[i].value;
					
				}
		}
	 	
		
}
	  
	  
function wbSearchChangeSec(frm){

	 	var i = frm.search_type_selection.selectedIndex;
		var curr_val = frm.search_type_selection.options[i].value;

		if (curr_val == 'QUE~GEN') {
			frm.search_type.value = '';
			frm.search_sub_type.value = '';			
		} 
		else if (curr_val == 'QUE') {
			frm.search_type.value = 'QUE';
			frm.search_sub_type.value = '';			
		} 
		else if (curr_val == 'MC' || curr_val == 'FB' || curr_val == 'MT' || curr_val == 'TF' || curr_val == 'ES') {
			frm.search_type.value = 'QUE';
			frm.search_sub_type.value = curr_val;						
		}
		else if (curr_val == 'WCT' || curr_val == 'NAR' || curr_val == 'ADO' || curr_val == 'VDO' || curr_val == 'FIG') {
			frm.search_type.value = 'GEN';
			frm.search_sub_type.value = curr_val;						
		}
		else if (curr_val == 'GEN') {
			frm.search_type.value = 'GEN';
			frm.search_sub_type.value = '';			
		} 
		else if (curr_val == 'GEN~AICC~SCORM~NETGCOK') {
			frm.search_type.value = 'GEN~AICC~SCORM~NETGCOK';
			frm.search_sub_type.value = '';			
		} 
		else if (curr_val == 'AICC') {
			frm.search_type.value = 'AICC';
			frm.search_sub_type.value = '';			
		} 
		else if (curr_val == 'SSC') {
			frm.search_type.value = 'AICC';
			frm.search_sub_type.value = 'SSC';			
		}
		else if (curr_val == 'SCORM') {
			frm.search_type.value = 'SCORM';
			frm.search_sub_type.value = 'RES_SCO';			
		}
		else if (curr_val == 'NETGCOK') {
			frm.search_type.value = 'NETGCOK';
			frm.search_sub_type.value = 'RES_NETG_COK';			
		}		
		else if (curr_val == 'ASM') {
			frm.search_type.value = 'ASM';
			frm.search_sub_type.value = '';
		} 
		else if (curr_val == 'FAS' || curr_val == 'DAS') {
			frm.search_type.value = 'ASM';
			frm.search_sub_type.value = curr_val;
		} 
		else if (curr_val == 'FSC') {
			frm.search_type.value = 'QUE';
			frm.search_sub_type.value = curr_val;
		} 
		else if (curr_val == 'DSC') {
			frm.search_type.value = 'QUE';
			frm.search_sub_type.value = curr_val;
		} 


}
		

