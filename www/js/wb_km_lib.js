// ------------------ wizBank Knowledge Management Library object ------------------- 
// Convention:
//   public functions : use "wbKMLib" prefix 
//   private functions: use "_wbKMLib" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 
var wbKMLibModule = 'km.library.KMLibraryModule'
var wbKMModule = 'km.KMModule'
/* constructor */

function wbKMLib(){	
	this.checkin = new wbKMCheckIn
	this.checkout = new wbKMCheckOut
	this.renew = new wbKMRenew
	this.borrow = new wbKMBorrow
	this.reserve = new wbKMReserve
	this.inquiry = new wbKMInquiry
	this.item = new wbKMItem
	this.domain = new wbKMDomain
	this.overdue = new wbKMOverdue
	this.lib_cata = new wbKMLibCatalog //for learner view
	this.goHome = wbKMLibGoHome
	this.rpt = new wbKMLibReport
	this.user_edit=wbKMLibUserEdit
	this.user_edit_exec=wbKMLibUserEditExec
	this.delivery_save=wbKMLibDeliverySave
	this.delivery_filter=wbKMLibDeliveryFilter
	this.query_by_usr_code_name=wbKMQueryByUsrCodeName
	this.query_by_usr_display_bil=wbKMQueryByUsrDisplayBil
	this.end_of_contract=wbKMEndOfContract
	this.ok_of_contract=wbKMOkOfContract
	kmlib_cookie_renew_from_url = 'kmlib_renew_from_url'
	kmlib_cookie_reserve_from_url = 'kmlib_reserve_from_url'
	kmlib_cookie_borrow_from_url = 'kmlib_borrow_from_url'
	kmlib_cookie_checkout_from_url = 'kmlib_checkout_from_url'
	this.print_labels = wbPrintLabels
}

function wbKMLibReport() {
    this.top_rpt_prep = wbKMTopReportPrep
    this.get_top_rpt = wbKMGetTopReport
    this.itm_list_prep = wbKMItemListPrep
    this.get_itm_list = wbKMGetItemList
}

function wbKMItemListPrep() {
	var url =  wb_utils_invoke_disp_servlet('module',wbKMLibModule, 'cmd','get_itm_list_prep','stylesheet','km_lib_itm_list_prep.xsl')
	window.location.href = url;
}

function wbKMGetItemList(frm,lang) {
	var dateVar = new Date();
    var spec_name = '';
    var spec_value = '';
    spec_name += 'itm_type' + ':_:_:';
	if (frm.itm_type_book && frm.itm_type_book.checked) {
	    spec_value += frm.itm_type_book.value + '~';
	}
	if (frm.itm_type_mut && frm.itm_type_mut.checked) {
	    spec_value += frm.itm_type_mut.value + '~';
	}
	if (frm.itm_type_maga && frm.itm_type_maga.checked) {
	    spec_value += frm.itm_type_maga.value + '~';
	}
	if(frm.itm_type_ebook && frm.itm_type_ebook.checked){
		spec_value += frm.itm_type_ebook.value + '~';
	}
	spec_value += ':_:_:';
	
	if (frm.include_offline[0].checked) {
    	spec_name += 'include_offline' + ':_:_:';
    	spec_value += 'true' + ':_:_:';
	}

	if (frm.include_checkout[0].checked) {
    	spec_name += 'include_checkout' + ':_:_:';
    	spec_value += 'true' + ':_:_:';
	}
    
	
    spec_name += 'cur_lang' + ':_:_:';
    spec_value += lang + ':_:_:';
    
    spec_name += 'window_name' + ':_:_:';
    spec_value += "rpt_win"+dateVar.getTime() + ':_:_:';
	
	if(frm.catalog[0].checked){
		spec_name +='catalog' + ':_:_:';
		spec_value += 'true' + ':_:_:';
	}else{
		spec_name +='catalog' + ':_:_:';
		spec_value +='false'+ ':_:_:';
	}
	
    if (frm.cat_id_lst) {
        spec_name += 'cat_id_lst' + ':_:_:';
        if(frm.catalog[1].checked && frm.cat_id_lst.length==0 ){
        	alert(eval('wb_msg_' + lang + '_select_search_in'));
        	return ;
        }else{
	        if(frm.cat_id_lst.length>0){
	        	for (i=0; i<frm.cat_id_lst.length; i++){
	    		    spec_value += frm.cat_id_lst[i].value + '~';
	    		}
	        }else{
	        	  spec_value += '0' + '~';
	        }
        }
    	spec_value += ':_:_:';
    }
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','get_itm_list','spec_name',spec_name, 'spec_value', spec_value);
	window.open(url,'');
}

function wbKMTopReportPrep() {
	var url =  wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','get_top_rpt_prep','stylesheet','km_lib_top_rpt_prep.xsl')
	window.location.href = url;
}

function wbKMGetTopReport(frm, lang) {
    var spec_name = '';
    var spec_value = '';
	if((frm.eff_start_datetime_yy.value!=""&&frm.eff_start_datetime_yy.value!=null) || 
	   (frm.eff_start_datetime_mm.value!=""&&frm.eff_start_datetime_mm.value!=null) || 
	   (frm.eff_start_datetime_dd.value!=""&&frm.eff_start_datetime_dd.value!=null)){
		if(!gen_validate_date("document." + frm.name + ".eff_start_datetime",frm.lab_datetime.value,lang)) {return false;}
		spec_name += 'eff_start_datetime' + ':_:_:';
		spec_value += frm.eff_start_datetime_yy.value + "-" + frm.eff_start_datetime_mm.value + "-" + frm.eff_start_datetime_dd.value + " 00:00:00.00" + ":_:_:";
	}
	
	if((frm.eff_end_datetime_yy.value!=""&&frm.eff_end_datetime_yy.value!=null) || 
	   (frm.eff_end_datetime_mm.value!=""&&frm.eff_end_datetime_mm.value!=null) || 
	   (frm.eff_end_datetime_dd.value!=""&&frm.eff_end_datetime_dd.value!=null)){
		if(!gen_validate_date("document." + frm.name + ".eff_end_datetime",frm.lab_datetime.value,lang)) {return;}
		spec_name += 'eff_end_datetime' + ':_:_:';
		spec_value += frm.eff_end_datetime_yy.value + "-" + frm.eff_end_datetime_mm.value + "-" + frm.eff_end_datetime_dd.value + " 23:59:59.00" + ':_:_:';
	}
	
    spec_name += 'itm_type' + ':_:_:';
	if (frm.itm_type_book && frm.itm_type_book.checked) {
	    spec_value += frm.itm_type_book.value + '~';
	}
	if (frm.itm_type_mut && frm.itm_type_mut.checked) {
	    spec_value += frm.itm_type_mut.value + '~';
	}
	if (frm.itm_type_mag && frm.itm_type_mag.checked) {
	    spec_value += frm.itm_type_mag.value + '~';
	}
	spec_value += ':_:_:';
	
	if (gen_validate_empty_field(frm.itm_num, frm.itm_num_lab.value, lang)) {
    	if (gen_validate_positive_integer(frm.itm_num, frm.itm_num_lab.value, lang)) {
        	spec_name += 'itm_num' + ':_:_:';
        	spec_value += frm.itm_num.value + ':_:_:';
    	} else {
    	    return false;
    	}
	} else {
	    return false;
	}
	
	if (frm.breakdown[0].checked) {
    	spec_name += 'show_breakdown' + ':_:_:';
    	spec_value += 'true' + ':_:_:';
	}
	
	spec_name += 'sort_order' + ':_:_:';
	spec_value += frm.sort_order.value + ':_:_:';
	
	frm.module.value = wbKMLibModule;
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'get_top_rpt';
	frm.stylesheet.value = 'km_lib_top_rpt_result.xsl';
	frm.spec_name.value = spec_name;
	frm.spec_value.value = spec_value;
	
	frm.submit();
}

function wbKMCheckIn(){
	this.prep = wbKMCheckInPrep
	this.confirm = wbKMCheckInConfirm
	this.checkindirect = wbKMCheckInDirect
	this.exec = wbKMCheckInExec
}

function wbKMCheckOut(){
    this.prep = wbKMCheckoutPrep
    this.confirm = wbKMCheckoutConfirm
	this.get_list = wbKMCheckOutGetList
	this.get_details = wbKMCheckOutGetDetails
	this.exec = wbKMCheckOutExec
	this.checkout_for_user = wbKMCheckOutForUser
	this.checkoutdirect = wbKMCheckOutDirect
}

function wbKMRenew(){
	this.confirm = wbKMRenewConfirm
	this.exec = wbKMRenewExec
}

function wbKMReserve(){
	this.confirm = wbKMReserveConfirm
	this.exec = wbKMReserveExec
	this.reserve_from_confirm_exec = wbKMReserveFromConfirmExec
	this.cancel = wbKMReserveCancel
}

function wbKMBorrow(){
	this.confirm = wbKMBorrowConfirm
	this.borrow_from_confirm_exec = wbKMBorrowFromConfirmExec
	this.exec = wbKMBorrowExec
	this.cancel = wbKMBorrowCancel
}

function wbKMInquiry(){
	this.inquiry_user_main = wbKMInquiryUserMain
	this.inquiry_user_exec = wbKMInquiryUserExec
	this.inquiry_user_exec_simple = wbKMInquiryUserExecSimple
	this.inquiry_item_main = wbKMInquiryItemMain
	this.inquiry_item_exec = wbKMInquiryItemExec
	this.user_history = wbKMInquiryUserHistory
	this.copy_history = wbKMInquiryCopyHistory
	this.inquiry_item_detail = wbKMInquiryItemDetail
}

function wbKMItem(){
	this.item_main = wbKMItemMain
}

function wbKMDomain(){
	this.domain_main = wbKMDomainMain
}

function wbKMOverdue(){
	this.overdue_item_main = wbKMOverdueItemMain
	this.overdue_get_list = wbKMOverdueList
}

function wbKMLibCatalog(){
	this.cata_main = wbKMLibCatalogMain
	this.cata_content = wbKMLibCatalogContent
	this.my_lib_list = wbKMLibMyLibList
	this.cata_main_url = wbKMLibCatalogMainURL
	this.cata_header_url = wbKMLibCatalogHeaderURL
	this.cata_menu_url = wbKMLibCatalogMenuURL
	this.cata_content_url  = wbKMLibCatalogContentURL	
	this.cata_down_url = wbKMLibCatalogDownURL	
	this.new_item_url = wbKMLibNewItmURL
	this.cata_get_item_list = wbKMLibCataLogGetItemList
	this.cata_menu_chg=wbKMLibCatalogMenuChg
	this.new_itm_review=wbKMLibNewItmReview
}

/* =================================== */
function wbKMLibUserEditExec(frm,lang){
	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'user_edit_exec'
	frm.module.value =  wbKMLibModule
	frm.submit();
	window.close();
}
function wbKMLibUserEdit(lang,nod_id,usr_ent_id){
	var url= wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','user_edit_prep','nod_id',nod_id,'usr_ent_id',usr_ent_id,'stylesheet','km_lib_user_info.xsl')
	var str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '760'
			+ ',height=' 				+ '350'
			+ ',scrollbars='				+ 'no'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'yes';
	window.open(url,'usr_win', str_feature);
}
function wbKMLibGoHome() {
	var url = wb_utils_invoke_servlet('cmd', 'home', 'stylesheet', 'km_lib_main.xsl')
	window.location.href = url;
}
/* =================================== */
function wbKMCheckInPrep(){
	var url = wbKMCheckInPrepURL()
	window.location.href = url;
}

function wbKMCheckInPrepURL(){
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','check_in_prep','stylesheet','km_lib_checkin_prep.xsl')
	return url;
}

function wbKMCheckInConfirm(frm,lang,call_num,copy_num){
	if (!(call_num == null || call_num == '')){
		frm.call_num.value = call_num
	}

	if (!(copy_num == null || copy_num == '')){
		frm.copy_num.value = copy_num
	}

	if (frm.call_num){
		if (!gen_validate_empty_field(frm.call_num,wb_msg_km_call_num,lang)) {return false;};
	}
	frm.method = 'get'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'check_in_list'
	frm.module.value =  wbKMLibModule
	frm.stylesheet.value = 'km_lib_checkin_confirm.xsl'
	frm.url_failure.value = window.location.href
	frm.submit();
}
function wbKMLibDeliverySave(frm){
	frm.method='get'
	frm.action=wb_utils_disp_servlet_url
	frm.cmd.value='delivery_save'
	frm.module.value=wbKMLibModule
	frm.url_success.value=window.location.href
	frm.submit();
}
function wbKMLibDeliveryFilter(frm,sort_order,sort_col,cur_page,page_size){
	
	if(sort_order == null || sort_order == ''){
		sort_order = 'ASC'
	}
	if(sort_col == null || sort_col == ''){
		sort_col = 'call_number'
	}	
	if(cur_page == null || cur_page == ''){
		cur_page = '1'
	}	
	if(page_size == null || page_size == ''){
		page_size = '10'
	}				
	
	var url = wb_utils_invoke_disp_servlet('cmd','check_out_list','module',wbKMLibModule,'sort_order',sort_order,'sort_col',sort_col,'cur_page',cur_page,'page_size',page_size,'stylesheet','km_lib_checkout_list.xsl')
	frm.method='post'
	frm.action=url
	frm.url_success.value=window.location.href
	frm.submit();
}
function wbKMCheckInDirect(call_num, copy_num) {
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','check_in_list','stylesheet','km_lib_checkin_confirm.xsl', 'call_num', call_num, 'loc_copy_num', copy_num);
	window.location.href = url;
}

function wbKMCheckInExec(frm, type){
	frm.method = 'get'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'check_in_exec'
	frm.module.value =  wbKMLibModule
	if (type) {
    	frm.check_in_type.value = type;
	} else {
    	frm.check_in_type.value = '';
	}
	url_success = getUrlParam('url_failure');
	if (url_success == '') {
    	frm.url_success.value = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','check_in_prep','stylesheet','km_lib_checkin_prep.xsl');
	} else {
    	frm.url_success.value = getUrlParam('url_failure');
	}
	frm.stylesheet.value = 'km_lib_checkin_request.xsl'
	frm.submit();
}
/* =================================== */

function wbKMCheckoutPrep () {
    var url =  wb_utils_invoke_servlet('cmd','get_prof','stylesheet','km_lib_checkout_prep.xsl');
	window.location.href = url;
}

function wbKMCheckoutConfirm(frm, lang, call_num, staff_num) {
	if (!(call_num == null || call_num == '')){
		frm.call_num.value = call_num
	}

	if (!(staff_num == null || staff_num == '')){
		frm.staff_num.value = staff_num
	}

	if (frm.call_num){
		if (!gen_validate_empty_field(frm.call_num,wb_msg_km_call_num,lang)) {return false;};
	}
	if (frm.usr_code_name){
		if (!gen_validate_empty_field(frm.usr_code_name,frm.lab_staff_num.value,lang)) {return false;};
	}
	//page setting
    frm.sort_order.value = 'ASC'
    frm.sort_col.value = 'call_number'
    frm.cur_page.value = '1'
    frm.page_size.value = '10'
	
	frm.method = 'get'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'check_out_confirm'
	frm.module.value =  wbKMLibModule
	frm.stylesheet.value = 'km_lib_checkout_confirm.xsl'
	frm.url_failure.value = window.location.href
	frm.submit();
    
}

function wbKMCheckOutGetList(sort_order,sort_col,cur_page,page_size, from){

	if(sort_order == null || sort_order == ''){
		sort_order = 'ASC'
	}
	if(sort_col == null || sort_col == ''){
		sort_col = 'call_number'
	}	
	if(cur_page == null || cur_page == ''){
		cur_page = '1'
	}	
	if(page_size == null || page_size == ''){
		page_size = '10'
	}				
	
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','check_out_list','sort_order',sort_order,'sort_col',sort_col,'cur_page',cur_page,'page_size',page_size,'stylesheet','km_lib_checkout_list.xsl')

	if(from != 'MAIN') {
		var url_from = gen_get_cookie(kmlib_cookie_checkout_from_url, url_from);
		if(url_from != null && url_from != '') {
			url = url_from;
			gen_del_cookie(kmlib_cookie_checkout_from_url);
		}
	}

	window.location.href = url;
}

function wbKMCheckOutGetDetails(call_id, from,delivery,posit){
	if (document.frmXml && document.frmXml.is_overdue && document.frmXml.is_overdue.value == 'true') {
		if (!confirm(wb_msg_km_obj_staff_overdue)) {
			return;
		}	
	}
	if(from == 'ITEM_INQ' || from == 'USER_INQ' || from == 'LIST') {
		var url_from = parent.location.href;
		gen_set_cookie(kmlib_cookie_checkout_from_url, url_from);
	}

	var url = wbKMCheckOutGetDetailsURL(call_id)
	
	if(from=='detail'&&delivery.length){
	url+="&cur_delivery="+delivery[posit-1].value+"&posit="+posit
	url+="&from_lst=false"
	}else
	url+="&from_lst=true"
	window.location.href = url;
}

function wbKMCheckOutGetDetailsURL(call_id){
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','check_out_details','call_id',call_id,'stylesheet','km_lib_checkout_details.xsl')
	return url
}

function wbKMCheckOutForUser(frm, lang) {
	if (frm.kus_status && frm.kus_status.value == 'false') {
		alert(wb_msg_km_obj_act_not_allow_for_end);
	} else {
		if (frm && frm.is_overdue && frm.is_overdue.value == 'true') {
			if (!confirm(wb_msg_km_obj_staff_overdue)) {
				return;
			}
		}
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.module.value = wbKMLibModule
		frm.cmd.value = 'check_out_exec'
		frm.stylesheet.value = 'km_lib_checkout_slip.xsl'
		frm.url_success.value = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'km_lib_checkout_prep.xsl');
		frm.url_failure.value = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'km_lib_checkout_prep.xsl');
		frm.submit();
	}
}

function wbKMCheckOutExec(frm,lang){
	if (!frm.copy_id || !frm.copy_id.type) {
		alert(frm.lab_no_copies.value);
		return false;
	}
	
	if(frm.copy_id.type == 'select-one'){
		if(frm.copy_id.options[frm.copy_id.selectedIndex].value == ''){
			alert(wb_msg_km_select_copy)
			frm.copy_id.focus()
			return;
		}
	}
	if(frm.usr_ent_id){
		var i=0,j=0,selected=false;
		for(i=0;i<frm.usr_ent_id.length;i++){
			if(frm.usr_ent_id[i].checked == true && frm.usr_ent_id[i].disabled == false){
				selected = true;
				frm.rec_usr_ent_id.value = frm.usr_ent_id[i].value;
			}
		}
		if(frm.usr_ent_id.checked){
			selected = true;
			frm.rec_usr_ent_id.value = frm.usr_ent_id.value;
		}
		if(selected == false){
			alert(wb_msg_km_select_request)
			for(j=0;j<frm.usr_ent_id.length;j++){
				if(frm.disabled == false){
					frm.usr_ent_id[j].focus();
					break;
				}
			}
			return;
		}
		if (eval("frm.status_" + frm.rec_usr_ent_id.value + ".value == 'false'")) {
			alert(wb_msg_km_obj_act_not_allow_for_end);
			return;	
		}
		if (eval("frm.is_overdue_" + frm.rec_usr_ent_id.value + ".value == 'true'")) {
			if (!confirm(wb_msg_km_obj_staff_overdue)) {
				return;
			}
		}
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.module.value = wbKMLibModule
		frm.cmd.value = 'check_out_exec'
		frm.stylesheet.value = 'km_lib_checkout_slip.xsl'
		
		var url_succsee_tep = '';
		if (frm.check_out_direct) {
            url_succsee_tep = gen_get_cookie(kmlib_cookie_checkout_from_url);
            if(url_succsee_tep != null && url_succsee_tep != '') {
                gen_del_cookie(kmlib_cookie_checkout_from_url);
            }
		} else {
		    url_succsee_tep = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','check_out_list','sort_order','ASC','sort_col','call_number','cur_page',1,'page_size',10,'stylesheet','km_lib_checkout_list.xsl');
		}
		
		frm.url_success.value = (url_succsee_tep != '') ? url_succsee_tep : wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','check_out_list','sort_order','ASC','sort_col','call_number','cur_page',1,'page_size',10,'stylesheet','km_lib_checkout_list.xsl');
		frm.cc_usr_ent_ids.value='';
		frm.bcc_usr_ent_ids.value='';
		frm.message_subject.value = '';
		frm.sender_usr_id.value = 's1u4';
		frm.submit();
		//alert(frm.rec_usr_ent_id.value);
	}
}

function wbKMCheckOutDirect(call_num, copy_num, confirm_type) {
	var url_failure = parent.location.href;
	var staff_id = wb_utils_get_cookie('staff');

    var usr_id = prompt(wb_msg_pls_input_usr_ste_id,staff_id);
    if (usr_id == null) {
        return;
    }
    if (usr_id == '') {
        alert(wb_msg_pls_input_usr_ste_id);
        return;
    }
    var book_info = call_num + copy_num;

	document.frmXml.method = "post";
	document.frmXml.action = wb_utils_invoke_disp_servlet();
	document.frmXml.module.value = wbKMLibModule;
	document.frmXml.cmd.value = "check_out_confirm";
	document.frmXml.call_num.value = book_info;
	document.frmXml.usr_code_name.value = usr_id;
	document.frmXml.stylesheet.value = "km_lib_checkout_confirm.xsl";
	document.frmXml.url_failure.value = url_failure;
	document.frmXml.confirm_type.value = confirm_type;
	document.frmXml.submit();
}

/* =================================== */

function wbKMRenewConfirm(usr_ent_id, lob_id, call_id, frm, is_ta) {
	// by learner role
	if (frm && frm.learn_is_overdue && frm.learn_is_overdue.value == 'true') {
		alert(wb_msg_km_obj_staff_overdue_learner);
		return;
	}
	// by adm role
	if (frm && frm.is_overdue && frm.is_overdue.value == 'true') {
		if (!confirm(wb_msg_km_obj_staff_overdue)) {
			return;
		}
	}

	var url_from = parent.location.href;
	gen_set_cookie(kmlib_cookie_renew_from_url, url_from);

	var url_failure = parent.location.href;
	url_failure = '../htm/close_window.htm';
	
	var stylesheet = 'km_lib_renew_confirm.xsl';
	if (is_ta && is_ta === 1) {
		stylesheet = 'km_lib_renew_confirm_ta.xsl';
	}
	var url = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'renew_prep', 'usr_ent_id', usr_ent_id, 'lob_id', lob_id, 
			'call_id', call_id, 'stylesheet', stylesheet, 'url_failure', url_failure)
	var str_feature = 'toolbar=' + 'no' + ',width=' + '1000' + ',height=' + '560' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'yes';
	window.open(url, 'renew_win', str_feature);
}

function wbKMRenewExec(frm,lang){
	var url_from = gen_get_cookie(kmlib_cookie_renew_from_url);
	gen_del_cookie(kmlib_cookie_renew_from_url);

	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'renew_exec'
	frm.module.value =  wbKMLibModule
	url_from = '../htm/close_and_reload_window.htm';
	frm.url_success.value = url_from;
	frm.url_failure.value = url_from;
	frm.submit();
}

/* =================================== */
function wbKMReserveConfirm(usr_ent_id, call_id, from, frm, is_ta) {
	if (from == 'BORROW') {
		url_from = gen_get_cookie(kmlib_cookie_borrow_from_url);
		gen_set_cookie(kmlib_cookie_reserve_from_url, url_from);
		url_failure = url_from;
		url_failure = '../htm/close_window.htm';
		var url = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'reserve_prep', 'usr_ent_id', usr_ent_id, 'call_id', call_id, 'stylesheet',
				'km_lib_reserve_confirm.xsl', 'url_failure', url_failure)
		frm.method = 'post'
		frm.action = url
		var lab_e_mail = 'Email';
		if(frm.lab_email){
		  lab_e_mail = frm.lab_email.value ;
		  if (!wbUtilsValidateEmail(frm.user_email, lab_e_mail)) 
			  return;
		}
		 frm.submit();
		
	} else if (from == 'RENEW') {
		url_from = gen_get_cookie(kmlib_cookie_renew_from_url);
		gen_set_cookie(kmlib_cookie_reserve_from_url, url_from);
		url_failure = url_from;
		url_failure = '../htm/close_window.htm';

		var stylesheet = 'km_lib_reserve_confirm.xsl';
		if (is_ta && is_ta === 1) {
			stylesheet = 'km_lib_reserve_confirm_ta.xsl';
		}
		var url = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'reserve_prep', 'usr_ent_id', usr_ent_id, 'call_id', call_id, 'stylesheet', stylesheet,
				'url_failure', url_failure)
		window.location.href = url;
	} else {
		if (frm && frm.learn_is_overdue && frm.learn_is_overdue.value == 'true') {
			alert(wb_msg_km_obj_staff_overdue_learner);
			return;
		}
		url_from = parent.location.href;
		gen_set_cookie(kmlib_cookie_reserve_from_url, url_from);
		url_failure = parent.location.href;
		url_failure = '../htm/close_window.htm';
		var url = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'reserve_prep', 'usr_ent_id', usr_ent_id, 'call_id', call_id, 'stylesheet',
				'km_lib_reserve_confirm.xsl', 'url_failure', url_failure)
		var str_feature = 'toolbar=' + 'no' + ',width=' + '1000' + ',height=' + '560' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'yes';

		window.open(url, 'reserve_win', str_feature);
	}
}

function wbKMReserveFromConfirmExec(frm, lang) {
	frm.rec_usr_ent_id.value = '';
	frm.cc_usr_ent_ids.value = '';
	frm.bcc_usr_ent_ids.value = '';
	frm.message_subject.value = '';

	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'reserve_exec'
	frm.module.value = wbKMLibModule
	var temp_url = getUrlParam('url_failure');
	var inquiryUrl = _wbGetInquiryURL(frm.curr_call_num.value);
	frm.url_success.value = (temp_url != '' && temp_url != null) ? temp_url : inquiryUrl;
	frm.url_failure.value = (inquiryUrl != '' && inquiryUrl != null) ? inquiryUrl : wbKMInquiryItemMainURL();
	frm.submit();
}

function wbKMReserveExec(frm, lang) {
	var url_from = gen_get_cookie(kmlib_cookie_reserve_from_url);
	gen_del_cookie(kmlib_cookie_reserve_from_url);

	frm.rec_usr_ent_id.value = '';
	frm.cc_usr_ent_ids.value = '';
	frm.bcc_usr_ent_ids.value = '';
	frm.message_subject.value = '';

	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'reserve_exec'
	frm.module.value = wbKMLibModule
	url_from = '../htm/close_and_reload_window.htm';
	frm.url_success.value = url_from;
	frm.url_failure.value = url_from;
	frm.submit();
}

function wbKMReserveCancel(lang, usr_ent_id, call_id) {
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
		var url_success = window.location.href;
		var url_failure = window.location.href;
		var url = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'reserve_cancel', 'usr_ent_id', usr_ent_id, 'call_id', call_id, 'url_success', url_success, 'url_failure', url_failure)
		self.location.href = url;
	}
}
/* =================================== */

function wbKMBorrowConfirm(usr_ent_id, call_id, from,frm, adm_email, adm_tel){
	if(from =='RESERVE') {
		url_from = gen_get_cookie(kmlib_cookie_reserve_from_url);
		gen_set_cookie(kmlib_cookie_borrow_from_url, url_from);
		url_failure = parent.location.href;
		url_failure = '../htm/close_window.htm';
		var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','reserve_prep','usr_ent_id',usr_ent_id,'call_id',call_id, 'stylesheet','km_lib_reserve_confirm.xsl', 'url_failure', url_failure)
		frm.method = 'post'
		frm.action=url
		var lab_e_mail = frm.lab_email ? frm.lab_email.value : 'Email'
	if (wbUtilsValidateEmail(frm.user_email, lab_e_mail)){
	frm.submit();
	}
	} else {
		if (!_wbKMCheckKusStatus(frm, adm_email, adm_tel)) {
			return;
		}
		if (frm && frm.learn_is_overdue && frm.learn_is_overdue.value == 'true') {
			alert(wb_msg_km_obj_staff_overdue_learner);
			return;
		}
		url_from = parent.location.href;
		gen_set_cookie(kmlib_cookie_borrow_from_url, url_from);
		url_failure = parent.location.href;
		url_failure = '../htm/close_window.htm';
		var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','borrow_prep','usr_ent_id',usr_ent_id,'call_id',call_id, 'stylesheet','km_lib_borrow_confirm.xsl', 'url_failure', url_failure)
		var str_feature = 'toolbar='		+ 'no'
				+ ',width=' 				+ '1000'
				+ ',height=' 				+ '450'
				+ ',scrollbars='				+ 'no'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'yes';
		window.open(url,'borrow_win' , str_feature);
	}
}

function _wbGetInquiryURL(call_num){
    var url = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'elib_get_item_rec', 'tvw_id', 'READ_VIEW', 
	'call_num', call_num, 'stylesheet', 'km_lib_inquiry_item_result.xsl', 'url_failure', wbKMInquiryItemMainURL());
    return url;
}

function wbKMBorrowFromConfirmExec(frm, lang) {
	if (frm && frm.is_overdue && frm.is_overdue.value == 'true') {
		if (!confirm(wb_msg_km_obj_staff_overdue)) {
			return;
		}
	}
	frm.rec_usr_ent_id.value = 4;
	frm.cc_usr_ent_ids.value = '';
	frm.bcc_usr_ent_ids.value = '';
	frm.message_subject.value = '';

	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'borrow_exec'
	frm.module.value = wbKMLibModule
	var temp_url = getUrlParam('url_failure');
	var inquiryUrl = _wbGetInquiryURL(frm.curr_call_num.value);
	frm.url_success.value = (temp_url != '' && temp_url != null) ? temp_url : inquiryUrl;
	frm.url_failure.value = (inquiryUrl != '' && inquiryUrl != null) ? inquiryUrl : wbKMInquiryItemMainURL();
	frm.submit();
}

function wbKMBorrowExec(frm, lang) {
	frm.rec_usr_ent_id.value = 4;
	frm.cc_usr_ent_ids.value = '';
	frm.bcc_usr_ent_ids.value = '';
	frm.message_subject.value = '';

	frm.method = 'post'
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'borrow_exec'
	frm.module.value = wbKMLibModule
	url_from = '../htm/close_and_reload_window.htm';
	frm.url_success.value = url_from;
	frm.url_failure.value = url_from;
	frm.submit()
	
	if (parent.opener.parent.opener) {
		parent.opener.parent.opener.location.reload();
	}
}

function trim(str) { // 删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/, "");
}

function wbKMBorrowCancel(lang, usr_ent_id, call_id){
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
		var url_success = window.location.href;
		var url_failure = window.location.href;
		var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','borrow_cancel','usr_ent_id',usr_ent_id,'call_id',call_id, 'url_success', url_success, 'url_failure', url_failure)
		self.location.href = url;
	}
}

/* =================================== */

function wbKMInquiryUserMain(){
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','km_lib_inquiry_user_main.xsl')
	window.location.href = url;
}

function wbKMInquiryItemMain(){
	var url = wbKMInquiryItemMainURL()
	window.location.href = url;
}

function wbKMInquiryItemMainURL(){
	//var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','km_lib_inquiry_item_main.xsl')
	var url = wb_utils_invoke_disp_servlet('cmd','get_folder_main','module',wbKMModule,'stylesheet','km_lib_inquiry_item_search.xsl','type','DOMAIN')
	return url;
}

function wbKMInquiryUserExec(frm, lang) {
	if (frm.ent_id_lst_single.value == '') {
		alert(wb_msg_km_select_user)
		return false;
	} else {
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'get'
		frm.module.value = wbKMLibModule
		frm.cmd.value = 'elib_get_user_rec'
		//frm.usr_ent_id.value = frm.ent_id_lst.options[0].value
		frm.staff_id.value = frm.ent_id_lst_single.value
		frm.url_failure.value = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'km_lib_inquiry_user_main.xsl')
		frm.stylesheet.value = 'km_lib_inquiry_user_result.xsl'
		frm.submit()
	}
}

function wbKMInquiryUserExecSimple(inquiry_ent_id){
	var url = wb_utils_invoke_disp_servlet('cmd','elib_get_user_rec','module',wbKMLibModule,'usr_ent_id',inquiry_ent_id,'stylesheet','km_lib_inquiry_user_result.xsl','sort_by','lob_due_timestamp','order_by','ASC')
	window.location.href = url
}

function wbKMInquiryItemExec(frm,lang){
	frm.url_failure.value = wbKMInquiryItemMainURL()
	if(frm.search_type[0].checked == false && frm.search_type[1].checked == false){
		alert(wb_msg_km_select_inquiry_type);
		return;
	}else{
		if(frm.search_type[0].checked == true){
		//call num search
			if(frm.call_num_lst.value == ''){
				alert(wb_msg_km_select_call_number)
				return;				
			}else{
				frm.action = wb_utils_disp_servlet_url
				frm.method = 'get'
				frm.module.value = wbKMLibModule
				frm.cmd.value = 'elib_get_item_rec'
				frm.call_num.value = frm.call_num_lst.value
				frm.tvw_id.value = 'READ_VIEW'
				frm.stylesheet.value = 'km_lib_inquiry_item_result.xsl'

				frm.submit()			
			}
		}else{
		//item search
			if(frm.item_lst.options[0].value == ''){
				alert(wb_msg_km_select_item)
				return;	
			}else{
				//alert(frm.item_lst.options[0].value)
				frm.action = wb_utils_disp_servlet_url
				frm.method = 'get'
				frm.module.value = wbKMLibModule
				frm.cmd.value = 'elib_get_item_rec'
				frm.nod_id.value = frm.item_lst.options[0].value
				frm.tvw_id.value = 'READ_VIEW'

				frm.stylesheet.value = 'km_lib_inquiry_item_result.xsl'
				frm.submit()
			}
		}
	}
}

function wbKMInquiryItemDetail(frm,lang,call_num,nod_id){
	frm.url_failure.value = wbKMInquiryItemMainURL()
	frm.action = wb_utils_disp_servlet_url
	frm.method = 'get'
	frm.module.value = wbKMLibModule
	//frm.call_num.value = call_num
	frm.nod_id.value = nod_id
	frm.cmd.value = 'elib_get_item_rec'
	frm.tvw_id.value = 'READ_VIEW'
	frm.stylesheet.value = 'km_lib_inquiry_item_result.xsl'
	frm.submit()			
		
}


function wbKMInquiryUserHistory(usr_ent_id){
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','hist_user','usr_ent_id',usr_ent_id,'stylesheet','km_lib_user_history.xsl')
	var	str_feature = 'toolbar='		+ '0'
			+ ',width=' 				+ '560'
			+ ',height=' 				+ '300'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',screenX='				+ '10'
			+ ',screenY='				+ '10'
			+ ',status='				+ 'yes'
		if(document.all){
			str_feature += ',top='		+ '10'
			str_feature += ',left='		+ '10'
		}	
		
		hist_win = window.open(url,'hist_win',str_feature)
}

function wbKMInquiryCopyHistory(loc_id){
//../servlet/Dispatcher?env=wizb&module=km.library.KMLibraryModule&cmd=HIST_COPY_XML&loc_id=2
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','hist_copy','loc_id',loc_id,'stylesheet','km_lib_copy_history.xsl')
	var	str_feature = 'toolbar='		+ '0'
			+ ',width=' 				+ '560'
			+ ',height=' 				+ '300'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',screenX='				+ '10'
			+ ',screenY='				+ '10'
			+ ',status='				+ 'yes'
		if(document.all){
			str_feature += ',top='		+ '10'
			str_feature += ',left='		+ '10'
		}	
		
		hist_win = window.open(url,'hist_win',str_feature)
}

/* =================================== */
function wbKMOverdueItemMainURL(){
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','get_overdue_list','sort_col','lob_due_timestamp','sort_order','asc','page_size','10','cur_page','1','stylesheet','km_lib_overdue_list.xsl')
	return url;
}

function wbKMOverdueItemMain(){
	var url = wbKMOverdueItemMainURL()
	window.location.href = url;
}

function wbKMOverdueList(sort_order,sort_col,cur_page,page_size){
	if(sort_order == null || sort_order == ''){
		sort_order = 'ASC'
	}
	if(sort_col == null || sort_col == ''){
		sort_col = 'lob_due_datetime'
	}	
	if(cur_page == null || cur_page == ''){
		cur_page = '1'
	}	
	if(page_size == null || page_size == ''){
		page_size = '10'
	}				
	
	var url = wb_utils_invoke_disp_servlet('module',wbKMLibModule,'cmd','get_overdue_list','sort_order',sort_order,'sort_col',sort_col,'cur_page',cur_page,'page_size',page_size,'stylesheet','km_lib_overdue_list.xsl')
	window.location.href = url;
}
/* ====================================== */
function wbKMLibCatalogMain(){
	var url = wbKMLibCatalogMainURL()
	window.location.href = url;
}

function wbKMLibCatalogMainURL(){
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet', 'km_lib_cata_frame.xsl');
	return url;
}

function wbKMLibCatalogHeaderURL() {
	var url = wb_utils_invoke_disp_servlet('cmd', 'get_prof', 'module', 'km.KMModule', 'stylesheet', 'km_lib_cata_header.xsl');
	return url;
}

function wbKMLibCatalogMenuURL() {
	var url = wb_utils_invoke_servlet('cmd', 'get_prof', 'module',wbKMModule, 'stylesheet', 'km_lib_cata_gen_tree.xsl');
	//var url = wb_utils_invoke_disp_servlet('cmd','get_prof','module',wbKMModule,'stylesheet','km_lib_cata_gen_tree.xsl')
	return url;
}

function wbKMLibCatalogMenuChg(cpty) {
	var url = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'km_lib_cata_gen_tree.xsl', 'cpty', cpty);
	parent.menu.location.href = url;

	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'km_lib_cata_content.xsl');
	parent.document.frames['content'].location.href = url;
}

function wbKMLibCatalogContent(target){
	var url = wbKMLibCatalogDownURL()
	if(target == null || target == ''){
		target=window;
	}
	target.location.href= url;
}

function wbKMLibCatalogContentURL(){
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','km_lib_cata_content.xsl');
	return url;
}

function wbKMLibCatalogDownURL(cpty){
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','km_lib_cata_frame_down.xsl','competency','All');
	return url;
}

function wbKMLibNewItmURL(cpty) {
	var url = wb_utils_invoke_disp_servlet('module','km.library.KMLibraryModule','cmd','elib_get_itm_review_lst','stylesheet','km_lib_itm_review_lst.xsl');
	return url;	
}


function wbKMLibMyLibList(target,usr_ent_id){
	var url = wb_utils_invoke_disp_servlet('module','km.library.KMLibraryModule','cmd','elib_get_user_rec','usr_ent_id',usr_ent_id,'sort_by','lob_due_timestamp','stylesheet','km_lib_my_lib_list.xsl')
	if(target == null || target == ''){
		target=window;
	}
	target.location.href= url;
}

function wbKMLibNewItmReview(target,usr_ent_id){
	var url = wb_utils_invoke_disp_servlet('module','km.library.KMLibraryModule','cmd','elib_get_itm_review_lst','stylesheet','km_lib_itm_review_lst.xsl')
	if(target == null || target == ''){
		target=window;
	}
	target.location.href= url;
}

function wbKMLibCataLogGetItemList(nod_id, usr_ent_id, sort_col, sort_order, cur_page, page_size) {
	if (sort_col == null || sort_col == '') {
		sort_col = 'obj_title'
	}
	if (sort_order == null || sort_order == '') {
		sort_order = 'ASC'
	}
	if (cur_page == null || cur_page == '') {
		cur_page = 1
	}
	if (page_size == null || page_size == '') {
		page_size = 10
	}
	if (nod_id == 0) {
		var url = wbKMLibCatalogContentURL()
	} else {
		var url = wb_utils_invoke_disp_servlet('cmd', 'elib_get_item_list', 'module', wbKMLibModule, 'nod_id', nod_id, 'usr_ent_id', usr_ent_id, 
				'sort_col', sort_col, 'sort_order', sort_order, 'cur_page', cur_page, 'page_size', page_size, 'stylesheet', 'km_lib_cata_item_lst.xsl')
	}
	parent.content.location.href = url
}

/* =================================== */
function wbKMItemMain() {
	var url = wbKMItemMainURL()
	window.location.href = url;
}

function wbKMItemMainURL() {
	var url = wb_utils_invoke_disp_servlet('cmd', 'get_folder_main', 'stylesheet', 'km_lib_work_lst.xsl', 'module', wbKMModule, 'type', 'WORK')
	return url;
}

/* =================================== */
function wbKMDomainMain() {
	var url = wbKMDomainMainURL()
	window.location.href = url;
}

function wbKMDomainMainURL() {
	var url = wb_utils_invoke_disp_servlet('cmd', 'get_folder_main', 'stylesheet', 'km_lib_domain_lst.xsl', 'module', wbKMModule, 'type', 'DOMAIN')
	return url;
}

function wbKMReload() {
	var curFrame = window;
	cur_fr_num = 0;
	prev_fr_num = -1;
	while (curFrame && cur_fr_num != prev_fr_num) {
		cur_fr_num = curFrame.frames.length;
		curFrame = curFrame.parent;
		prev_fr_num = curFrame.frames.length;
	}
	if (curFrame.opener)
		curFrame.opener.location.reload();
}

function wbKMQueryByUsrCodeName(frm, lab_name, lang) {
	if (gen_validate_empty_field(frm.usr_code_name, lab_name, lang)) {
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module', 'km.library.KMLibraryModule', 'cmd', 'query_subordinate_book', 'stylesheet', 'km_lib_staff_search.xsl',
				'query_failure', 'true');
		frm.action = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'query_usr_code_name');
		frm.submit();
	}
}

function wbKMQueryByUsrDisplayBil(frm, lab_name, lang) {
	if (gen_validate_empty_field(frm.usr_display_bil, lab_name, lang)) {
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'query_usr_code_name', 'usr_code_name', frm.target_code_name.value, 'stylesheet',
				'km_lib_staff_search.xsl', 'query_failure', 'true');
		frm.action = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'query_usr_display_bil');

		frm.submit();
	}
}

function wbKMEndOfContract(frm, lang) {
	var number;
	if (confirm(wb_msg_km_obj_end_of_contract)) {
		do {
			number = gen_trim(prompt(wb_msg_km_obj_enter_contact_number, ''));
			if (number) {
				if (number.match(/(^\d*$)|(^\d+\-\d+$)/g) == null) {
					number = '';
					alert(wb_msg_km_obj_error_contact_number);
				} else if (number.length > 0) {
					frm.url_success.value = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'query_usr_display_bil', 'usr_code_name', frm.target_code_name.value,
							'usr_display_bil', frm.usr_display_bil.value, 'stylesheet', frm.stylesheet.value);
					frm.action = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'end_of_contract', 'usr_ent_id', frm.target_ent_id.value, 'kus_contact', number);
					frm.submit();
				}
			}
		} while (number == '')
	}
}

function wbKMOkOfContract(usr_ent_id, lang) {
	if (confirm(eval('ae_msg_' + lang + '_confirm'))) {
		var url = wb_utils_invoke_disp_servlet('module', wbKMLibModule, 'cmd', 'ok_of_contract', 'usr_ent_id', usr_ent_id, 'url_success', location.href);
		window.location.href = url;
	}
}

function _wbKMCheckKusStatus(frm, adm_email, adm_tel) {
	if (frm && frm.kus_status && frm.kus_status.value == 'false') {
		var msg = wb_msg_km_obj_act_not_allow_for_end + "\n" + wb_msg_km_obj_contact_adm;
		if (adm_email.length > 0 || adm_tel.length > 0) {
			msg += "( " + adm_email;
			if (adm_email.length > 0 && adm_tel.length > 0) {
				msg += "  ";
			}
			msg += adm_tel + " )";
		}
		alert(msg);
		return false;
	}
	return true;
}

function wbPrintLabels(frm, lang) {
	var delimiter = ':_:_:';

	var temp = '';
	var lob_id = document.getElementsByName('lob_id');
	for (i = 0; i < lob_id.length; i++) {
		if (eval('frm.label_' + lob_id[i].value).checked) {
			temp += delimiter + lob_id[i].value;
		}
	}
	if (temp.length == 0) {
		alert(eval('wb_msg_' + lang + '_label_empty'));
		return false;
	}

	var dateVar = new Date();
	var window_name = "prt_win" + dateVar.getTime();

	frm.spec_name.value = 'lob_id_lst';
	frm.spec_value.value = temp.substr(delimiter.length);

	frm.module.value = wbKMLibModule;
	frm.action = wb_utils_disp_servlet_url
	frm.cmd.value = 'print_label';
	frm.stylesheet.value = 'km_lib_print_label.xsl';
	frm.method = "post";
	// frm.target = "rpt_win" + dateVar.getTime();
	frm.submit();

}