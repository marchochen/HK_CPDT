// ------------------ wizBank Payment object ------------------- 
// Convention:
//   public functions : use "aePayment" prefix 
//   private functions: use "_aePayment" prefix
// Dependency:
//   gen_utils.js
//   ae_utils.js
// 	 wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function aePayment(){
	
	this.add = aePaymentAdd
	this.cash = aePaymentCountCash
	this.reset = aePaymentReset
	this.change_item_amount = aePaymentChangeItemAmount
	this.lrn_view_tran = aePaymentViewTranscation
	this.lrn_view_open_item = aePaymentViewOpenItem
	this.lrn_online = aePaymentLrnOnline
	this.lrn_online_confirm = aePaymentLrnOnlineConfirm
	this.lrn_online_header_url = aePaymentLrnOnlineHeaderURL
	this.lrn_online_content_url = aePaymentLrnOnlineContentURL
	this.adm_view_usr_detail = aePaymentAdmViewUserDetail
	this.adm_view_usr_detail_url = aePaymentAdmViewUserDetailURL	
	this.adm_view_usr_trn_detail = aePaymentAdmViewUserTranscationDetail
	this.adm_view_usr_trn_detail_url = aePaymentAdmViewUserTranscationDetailURL	
	this.adm_online = aePaymentAdmOnline
	this.adm_online_back = aePaymentAdmOnlineBack
	this.adm_online_confirm = aePaymentAdmOnlineConfirm
	this.adm_online_result = aePaymentAdmOnlineResult
	//Adm search open item
	this.adm_search_prep = aePaymentAdmSearchPrep
	this.adm_search_exec = aePaymentAdmSearchExec
	this.adm_search_order_by=aePaymentAdmSearchOrderBy
	this.adm_search_page=aePaymentAdmSearchPage
}

// -------------------------------
function _aePaymentGetQueNumber(frm) {
		var i, n, ele, str
		str = ''
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == 'checkbox' && ele.checked) {
				if (ele.value !='')
					str = str + ele.value + '~'
			}
		}
		
		if (str != '') {
			str = str.substring(0, str.length-1);
		}

		return str;	
}


function _aePaymentGetAmount(frm) {
		var i, n, ele, str
		str = ''
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.name.indexOf('paid')!= -1 && ele.name.indexOf('_os') == -1) {
				if ( ele.value == '' )
					ele.value = '0'
				str = str + ele.value + '~'
			}
		}
		
		if (str != '') {
			str = str.substring(0, str.length-1);
		}
		return str;	
}

function _aePaymentGetItemId(frm) {
		var i, n, ele, str
		str = ''
		n = frm.elements.length;
		
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
		
			if (ele.name.indexOf('paid') != -1 && ele.name.indexOf('_os') == -1) {
				str = str + ele.name.substring(4) + '~'
			}
		}
		
		if (str != '') {
			str = str.substring(0, str.length-1);
		}
		return str;	
}

function _aePaymentValidateSearchFrm(frm,lang){


	if (frm.search_type[0].checked == true ){
		if (!gen_validate_empty_field(frm.oi_id, eval('ae_msg_'+ lang + '_pay_oi_id'),lang)) {
			return false
		}
		
		if (!gen_validate_positive_integer(frm.oi_id,eval('ae_msg_' + lang + '_pay_sch_open_itm_id'),lang)){
			return false
		}	
	}
	
	
	if (frm.usr_id.value != '' && frm.search_type[1].checked == true){
		if (!gen_validate_usr_id(frm.usr_id,lang)) {
			return false
		}
	}
	
	
	/*if(frm.usr_email.value != '' && frm.usr_email_type[1].checked == true ){
		if (!gen_validate_email(frm.usr_email,lang)) {
			return false
		}
	}*/
	
	
	
	/*
	if (frm.oi_due_from_mm.value.length != 0 || frm.oi_due_from_yy.value.length != 0 || frm.oi_due_from_dd.value.length != 0 ){
		if (!gen_validate_date('document.frmXml.oi_due_from',eval('ae_msg_' + lang + '_pay_sch_open_itm_fr'),lang))
				return false	
		
	}
	
	if (frm.oi_due_to_mm.value.length != 0 || frm.oi_due_to_yy.value.length != 0 || frm.oi_due_to_dd.value.length != 0 ){
		if (!gen_validate_date('document.frmXml.oi_due_to',eval('ae_msg_' + lang + '_pay_sch_open_itm_fr'),lang))
				return false	
		
	}
	
	if ( frm.oi_due_from_mm.value.length != 0 && frm.oi_due_from_yy.value.length != 0 && frm.oi_due_from_dd.value.length != 0  && frm.oi_due_to_mm.value.length != 0 && frm.oi_due_to_yy.value.length != 0 && frm.oi_due_to_dd.value.length != 0 ){
		start = frm.oi_due_from_yy.value + frm.oi_due_from_mm.value + frm.oi_due_from_dd.value 
		end = frm.oi_due_to_yy.value + frm.oi_due_to_mm.value + frm.oi_due_to_dd.value 
	
		if (Number(start) >= Number(end)){
			alert(eval('ae_msg_' + lang + '_sch_pay_start_end'))
			frm.oi_due_to_mm.focus();
			return false
		}
	}
	*/
	return true;
}

function _aePaymentValidateFrm(frm,lang){

		if ( frm.axn_method[1].checked == true ){
			if (!gen_validate_empty_field(frm.cref_no, eval('ae_msg_'+ lang + '_pay_cheque_ref'),lang)) {
				return false
			}		
		}
		
		if ( frm.axn_method[2].checked == true ){
			if (!gen_validate_empty_field(frm.pref_no, eval('ae_msg_'+ lang + '_pay_post_ref'),lang)) {
				return false
			}		
		}
		
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == 'text' && ele.name.indexOf('ref_no') == -1 && ele.name != 'onaccount') {
				if ( ele.value.length != 0 ){
					if (!gen_validate_float(ele,eval('ae_msg_' + lang + '_pay_amount'),lang)){
						return false
					}	
					
					if ( ele.name.indexOf('paid') != -1 && ele.name.indexOf('_os') == -1){
						ele_nm = 'frm.' + ele.name + '_os.value'
						if ( Number(ele.value) > Number(eval(ele_nm)) ){
							alert(eval('ae_msg_' + lang + '_pay_os_pay'))
							ele.focus()
							return false
						}
					}
				}
			}
		}
	
		total = 0;
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.name.indexOf('paid') != -1 && ele.name.indexOf('_os') == -1) {
				total += Number(ele.value)
			}
		}
		
		total += Number(frm.onaccount.value)

		if ( Number(total) > Number(cur_amount) || Number(frm.onaccount.value) < 0 || Number(total) == 0 ){
			alert(eval('ae_msg_' + lang + '_pay_not_enough'))
			return false;
		}
		

return true;

}
// -------------------------------------

function aePaymentAdd(value,id) {
	if (eval('document.frmXml.' +  id + '.checked') == true){
		total = (total-0) + (value-0)
		document.frmXml.elements['total'].value = total;
		}else{
			total = (total-0) - (value-0)
			if (total < 0){
			document.frmXml.elements['total'].value = 0;
			total = 0;
			}else{
			document.frmXml.elements['total'].value = total;
			}
		}
}



	
function aePaymentSetFocus(name,type){
		eval('document.frmXml.' + type + '.focus()');

		document.frmXml.cash.value = ''
}


	
function aePaymentCountCash(amount) {

		
		if (amount == '' ) 
			amount = 0;
			
		cur_amount = amount;
		n = document.frmXml.elements.length;
		for (i = 0; i < n; i++) {
			ele = document.frmXml.elements[i]
			if (ele.name.indexOf('paid') != -1 && ele.name.indexOf('_os') == -1) {
				ele_nm = 'document.frmXml.' + ele.name + '_os.value'
				if ( Number(eval(ele_nm)) > Number(amount) ){
					ele.value = amount
					amount = '0'
				}else if (amount != 0 ){
					ele.value = eval(ele_nm)
					amount = Number(amount) -  Number(eval(ele_nm))
				}					
			}
		}
		document.frmXml.onaccount.value = amount
		
		
		
		
}


function aePaymentReset(frm){

		n = frm.elements.length;
			for (i = 0; i < n; i++) {
				ele = frm.elements[i]
				if (ele.type == 'text' ) {
					ele.value = ''			
				}
			}

}

function aePaymentChangeItemAmount(frm){

			var amount = cur_amount			
			n = frm.elements.length;			
			for (i = 0; i < n; i++) {
				ele = frm.elements[i]
				if (ele.name.indexOf('paid') != -1 && ele.name.indexOf('_os') == -1){		
					if (ele.value != '' ) {						
						amount = amount - parseFloat(ele.value)		
						if ( String(amount).indexOf('.') == -1)
							frm.onaccount.value = amount
						else			
							frm.onaccount.value = String(amount).substring(0,String(amount).indexOf('.')+3)
					}
				}
			}

		

}



function aePaymentViewTranscation(ent_id){

	url = ae_utils_invoke_servlet('cmd','get_oi_axn', 'usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_lrn_payment_transcation)
	window.location.href= url

}

function aePaymentViewOpenItem(ent_id){

	url = ae_utils_invoke_servlet('cmd','get_oi_axn', 'usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_lrn_payment)
	window.location.href= url
}

function aePaymentLrnOnline(ent_id){
	
	url = ae_utils_invoke_servlet('cmd','get_os_oi','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_lrn_online_payment)
	window.location.href = url;

}


function aePaymentLrnOnlineHeaderURL(ent_id){
	
	url = ae_utils_invoke_servlet('cmd','get_os_oi','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_lrn_online_payment_header)
	return url;
}

function aePaymentLrnOnlineContentURL(ent_id){
	
	url = ae_utils_invoke_servlet('cmd','get_os_oi','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_lrn_online_payment_content)
	return url;
}


function aePaymentLrnOnlineConfirm(frm,lang){

	frm.item_id.value = _aePaymentGetQueNumber(frm)
		if (frm.item_id.value == '') {
			alert(eval('wb_msg_' + lang + '_select_itm'))
			return
		}
	
	///servlet/cw.ae.aeAction?cmd=cmt_on_axn_xml&usr_ent_id=564&acn_type=applyeasy&axn_ccy=RMB&item_id=112~113
		frm.cmd.value = 'cmt_on_axn'
		frm.action = ae_utils_servlet_url
		frm.stylesheet_success.value = ae_utils_xsl_lrn_pay_success
		frm.stylesheet_fail.value = ae_utils_xsl_lrn_pay_fail
		frm.method = 'post'
		frm.submit()
	
}

function aePaymentAdmViewUserDetail(ent_id){

	//../servlet/cw.ae.aeAction?cmd=get_oi_axn&usr_ent_id=407&acn_type=applyeasy
	url = ae_utils_invoke_servlet('cmd','get_oi_axn','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_adm_view_usr_detail)
	window.location.href = url;

}

function aePaymentAdmViewUserDetailURL(ent_id){

	//../servlet/cw.ae.aeAction?cmd=get_oi_axn&usr_ent_id=407&acn_type=applyeasy
	url = ae_utils_invoke_servlet('cmd','get_oi_axn','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_adm_view_usr_detail)
	return url;

}


function aePaymentAdmViewUserTranscationDetail(ent_id){

	//../servlet/cw.ae.aeAction?cmd=get_oi_axn&usr_ent_id=407&acn_type=applyeasy
	url = ae_utils_invoke_servlet('cmd','get_oi_axn','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_adm_view_usr_trn_detail)
	window.location.href = url;

}

function aePaymentAdmViewUserTranscationDetailURL(ent_id){

	//../servlet/cw.ae.aeAction?cmd=get_oi_axn&usr_ent_id=407&acn_type=applyeasy
	url = ae_utils_invoke_servlet('cmd','get_oi_axn','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_adm_view_usr_trn_detail)
	return url;

}


	
function aePaymentAdmOnline(ent_id){

	url = ae_utils_invoke_servlet('cmd','get_os_oi','usr_ent_id',ent_id,'acn_type','applyeasy','stylesheet',ae_utils_xsl_adm_online_payment)
	window.location.href = url;
}


function aePaymentAdmOnlineConfirm(frm,lang){

	frm.item_id.value = _aePaymentGetItemId(frm)
	if (frm.item_id.value == '') {
		alert(eval('wb_msg_' + lang + '_select_payment'))
		return
	}
	frm.item_amount.value = _aePaymentGetAmount(frm)

	if (_aePaymentValidateFrm(frm,lang)) {
		
		frm.axn_type.value = 'PAYMENT'				
		if ( frm.axn_method[0].checked == true )		
			frm.axn_amt.value = frm.cash.value
		else if ( frm.axn_method[1].checked == true ){
			frm.axn_amt.value = frm.cheque.value
			frm.ref_no.value = frm.cref_no.value
		}
		else if ( frm.axn_method[2].checked == true ){
			frm.axn_amt.value = frm.post.value
			frm.ref_no.value = frm.pref_no.value
		}
		else if ( frm.axn_method[3].checked == true ){
			frm.axn_amt.value = frm.org_on_account_amount.value
			frm.axn_type.value = 'OFFSET'				
		}
		
		
		frm.action = ae_utils_servlet_url
//		frm.action = "http://127.0.0.1:8080/servlet/cw.ae.aeAction"
		frm.stylesheet.value = ae_utils_xsl_adm_online_payment_conf
		frm.cmd.value = 'chk_axn'
		frm.method = 'post'
		frm.submit()
	
	}	
	
	
}

function aePaymentAdmOnlineBack(frm){


	frm.action = ae_utils_servlet_url
	frm.stylesheet.value = ae_utils_xsl_adm_online_payment
	frm.cmd.value = 'get_back_os_oi'
	frm.method = 'post'
	frm.submit()


}


function aePaymentAdmOnlineResult(frm){


	frm.action = ae_utils_servlet_url
//	frm.action = "http://127.0.0.1:8080/servlet/cw.ae.aeAction"
	frm.url_success.value = this.adm_view_usr_detail_url(frm.usr_ent_id.value)
	frm.stylesheet.value = ae_utils_xsl_adm_online_payment_failure
//	frm.url_failure.value = url
	frm.cmd.value = 'cmt_axn'
	frm.method = 'post'
	frm.submit()
	
}
	
	
	
function aePaymentAdmSearchPrep(){
	
	url = ae_utils_invoke_servlet('cmd','ae_get_prof','stylesheet',ae_utils_xsl_adm_payment_search)
	window.location.href = url
}


function aePaymentAdmSearchExec(frm,lang){

	if (_aePaymentValidateSearchFrm(frm,lang)) {
		
		/*
		if (frm.oi_due_from_mm.value.length != 0 && frm.oi_due_from_yy.value.length != 0 && frm.oi_due_from_dd.value.length != 0 ){
			frm.oi_due_from.value = frm.oi_due_from_yy.value + '-' +  frm.oi_due_from_mm.value + '-' +  frm.oi_due_from_dd.value + ' 00:00:00.0'
		}
	
		if (frm.oi_due_to_mm.value.length != 0 && frm.oi_due_to_yy.value.length != 0 && frm.oi_due_to_dd.value.length != 0 ){
			frm.oi_due_to.value = frm.oi_due_to_yy.value + '-' +  frm.oi_due_to_mm.value + '-' +  frm.oi_due_to_dd.value + ' 23:59:59.0'
		}
		
		*/
		
		if ( frm.search_type[0].checked == true ){
			frm.usr_id.value = ''
			frm.usr_last_name.value = ''
			frm.usr_first_name.value = ''
			frm.stylesheet.value = ae_utils_xsl_adm_view_usr_detail
			frm.no_result_stylesheet.value = ae_utils_xsl_adm_payment_search_result
		}else if ( frm.search_type[1].checked == true ){
			frm.oi_id.value = ''
			frm.stylesheet.value = ae_utils_xsl_adm_payment_search_result
			frm.no_result_stylesheet.value = ae_utils_xsl_adm_payment_search_result
		}			
		
		frm.action = ae_utils_servlet_url		
		frm.cmd.value = 'ae_search_acc'
		frm.method = 'get'
		frm.submit()
	
	}
}
function aePaymentAdmSearchOrderBy(item_per_page,page_num,order_by,sort_by){
	//cmd=ae_search_acc&item_per_page=1&page_num=3&order_by={TOTALOI | SUMAMT | USRID | USRLASTNAME | USRFIRSTNAME | DISPNAME }
	url = ae_utils_invoke_servlet('cmd','ae_search_acc','item_per_page',item_per_page,'page_num',page_num,'order_by',order_by,'sort_by',sort_by,'stylesheet',ae_utils_xsl_adm_payment_search_result)
	window.location.href = url
}
function aePaymentAdmSearchPage(item_per_page,page_num){
	//cmd=ae_search_acc&item_per_page=3&page_num=3
	url = ae_utils_invoke_servlet('cmd','ae_search_acc','item_per_page',item_per_page,'page_num',page_num,'stylesheet',ae_utils_xsl_adm_payment_search_result)
	window.location.href = url
}

