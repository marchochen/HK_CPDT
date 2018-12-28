// ------------------ wizBank Announcement object ------------------- 
// Convention:
//   public functions : use "wbAnnouncement" prefix 
//   private functions: use "_wbAnnouncement" prefix
// ------------------------------------------------------------ 

/* constructor */
// Resource Announcement	
function wbAnnouncement() {	
	this.get_ann_dtl = wbAnnouncementGetAnnDetail;
	this.sys_lst = wbAnnouncementSystemAnnLst;
	this.add_sys_ann_lst = wbAnnouncementAddSysAnnLst;
	this.upd_sys_ann_lst = wbAnnouncementUpdSysAnnLst;
	this.add_sys_ann_exec = wbAnnouncementAddUpdSystemAnnExec;
	this.del_sys_ann_lst = wbAnnouncementDelSystemAnnLst;
	this.multi_del_sys_ann_lst = wbAnnouncementMultiDelSystemAnnLst;
	this.show_content = wbAnnouncementShowContent;
	
	this.get_ann_detail = getAnnouncementDetail;
	this.show_receipt_views = showReceiptViews;

	this.search_result_notice = wbAnnouncementSearchResultNotice;
}

function wbAnnouncementSearchResultNotice(isMobile){
	var title_code = wbUtilsTrimString($('#title_code').val());
	
	var tcr_id = getUrlParam('msg_tcr_id');
	var read_only = getUrlParam('msg_readonly');
    url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', 'announ_lst.xsl', 'msg_type', 'SYS', 'cur_page', 1,'sortCol', 'msg_begin_date', 'sortOrder', 'DESC', 'timestamp', '', 'msg_readonly', read_only, 'isMobile', isMobile,'msg_tcr_id', tcr_id,'title_code',title_code);
	window.location.href = url;
}

function getAnnouncementDetail(msg_id,type,page){
	//这个page使用来定位资讯在第多少页。以便跳转到页面的时候控制
	if(!page){
		page = 8;
	}
	window.location.href = wb_utils_app_base + "app/ann/default?msg_id="+msg_id+"&page="+page;
}


function wbAnnouncementGetAnnDetail(msg_id,msg_type, readOnly, isLearner,item_id,msg_belong_exam_ind){
	var url = "/app/admin/announce?id=" + msg_id;
	if(msg_type != ""){
		url = url + "&msg_type="+msg_type;
	}
	if(item_id != "" && item_id != undefined){
		url = url + "&item_id="+item_id;
	}
	if(msg_belong_exam_ind != "" && msg_belong_exam_ind != undefined){
		url = url + "&msg_belong_exam_ind="+msg_belong_exam_ind;
	}
	window.location.href = url;
}

function wbAnnouncementSystemAnnLst(ann_type,msg_type,res_id,cur_page,page_size,sort_col,sort_order,timestamp,openWin){		
	
	if (sort_col == null || sort_col == '') {sort_col = 'msg_begin_date';}
	if (sort_order == null || sort_order == '') {sort_order = 'DESC';}
	if (cur_page == null || cur_page == '') {cur_page = '1';}
	if (page_size == null || page_size == '') {page_size = '10';}
	if (timestamp == null || timestamp == '') {timestamp = '';}
	if (msg_type == null || msg_type == '') {msg_type = 'SYS';}
	
	if (openWin != null && openWin){
		str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '1000'
			+ ',height=' 				+ '400'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
	}
	
	if (openWin == null || openWin == '' || !openWin){
		url = wbAnnouncementSystemAnnLstUrl(ann_type,msg_type,res_id,cur_page,page_size,sort_col,sort_order,timestamp);
		window.location.href = url;
	}else{
		url = wbAnnouncementSystemAnnLstUrl(ann_type,msg_type,res_id,cur_page,page_size,sort_col,sort_order,timestamp,'announ_lst_popup.xsl');
		window.location.href = url;
	}
}
		
function wbAnnouncementSystemAnnLstUrl(ann_type,msg_type,res_id,cur_page,page_size,sort_col,sort_order,timestamp,stylesheet){
	
	if (sort_col == null || sort_col == '') {sort_col = 'msg_begin_date';}
	if (sort_order == null || sort_order == '') {sort_order = 'DESC';}
	if (cur_page == null || cur_page == '') {cur_page = '1';}
	if (page_size == null || page_size == '') {page_size = '10';}
	if (timestamp == null || timestamp == '') {timestamp = '';}
	if (msg_type == null || msg_type == '') {msg_type = 'SYS';}
	if (stylesheet == null || stylesheet == '') {stylesheet = 'announ_lst.xsl';}
	
	if (ann_type == 'all') {
		url = wb_utils_invoke_servlet(
			'cmd', 'get_all_msg', 
			'stylesheet', stylesheet, 
			'msg_type', msg_type,
			'cur_page',cur_page,
			//'page_size',page_size,
			'sortCol',sort_col,
			'sortOrder',sort_order,
			'timestamp',timestamp,
			'msg_tcr_id',0
		);
	
	if (msg_type == 'RES' && res_id != null && res_id != '') {url += '&res_id=' + res_id ;}
	}else{
		url = wb_utils_invoke_servlet(
			'cmd', 'get_msg', 
			'stylesheet', stylesheet, 
			'msg_type', msg_type,
			'cur_page',cur_page,
			//'page_size',page_size,
			'sortCol',sort_col,
			'sortOrder',sort_order,
			'timestamp',timestamp
		);
		if (msg_type == 'RES' && res_id != null && res_id != '') {url += '&res_id=' + res_id ;}
	}
	return url;
}

function wbAnnouncementAddSysAnnLst(msg_type,res_id,popup, from_show_all, isMobile,itm_exam_ind, tcr_id){	
	if(tcr_id == null || tcr_id == '') tcr_id = 0;
	if(popup == true){
		var stylesheet = 'announ_ins_rte_popup.xsl'
	}else{
		var stylesheet = 'announ_ins_rte.xsl'
	}
	url = wb_utils_invoke_servlet('cmd','PREP_INS_MSG','msg_type',msg_type,'stylesheet',stylesheet, 'msg_show_all', from_show_all, 'isMobile', isMobile,'msg_belong_exam_ind',itm_exam_ind, 'tcr_id', tcr_id);
	if (msg_type == 'RES') { url += '&res_id=' + res_id}
	window.location.href  = url
}

function wbAnnouncementUpdSysAnnLst(msg_type,msg_id,res_id,popup, show_all, is_upd, isMobile,itm_exam_ind){
	if(popup == true){
		var stylesheet = 'announ_upd_rte_popup.xsl'
	}else{
		var stylesheet = 'announ_upd_rte.xsl'
	}	
	url = wb_utils_invoke_servlet('cmd','get_msg','msg_type',msg_type,'encrypt_msg_id',msg_id,'stylesheet',stylesheet, 'msg_show_all', show_all, 'is_upd', is_upd, 'isMobile',  isMobile,'msg_belong_exam_ind',itm_exam_ind);
	if (msg_type == 'RES') { url += '&res_id=' + res_id}
	window.location.href  = url
}

function showReceiptViews(msg_id){
	url = wb_utils_invoke_disp_servlet('module', 'JsonMod.Ann.AnnModule', 'cmd', 'get_receipt_views', 'msg_id',msg_id,'stylesheet', 'ann_receipt_views.xsl');
	window.location.href  = url;
//	window.open(url);
}

function wbAnnouncementAddUpdSystemAnnExec(frm,lang, show_all){

	if (editor) {
		try {
			editor.sync();
		} catch(e) {}
	}
	//alert(frm.msg_type.value + "/" + frm.res_id.value)
	if ( frm.start_date[0].checked == true)
		frm.msg_begin_date.value = "IMMEDIATE";
	else{
		var hour ="";
		var min ="";
		var mm="";
		var dd="";
		if(frm.start_hour.value<10 && frm.start_hour.value.length < 2){
			hour ="0";
		}
		if(frm.start_min.value<10 && frm.start_min.value.length < 2){
			min ="0";
		}
		if(frm.start_mm.value<10 && frm.start_mm.value.length < 2){
			mm ="0";
		}
		if(frm.start_dd.value<10 && frm.start_dd.value.length < 2){
			dd ="0";
		}
		frm.msg_begin_date.value = frm.start_yy.value + "-" +mm+ frm.start_mm.value + "-" +dd+frm.start_dd.value + " " + hour+frm.start_hour.value + ":" +min+frm.start_min.value + ":00"
	}
		
				
	if ( frm.end_date[0].checked == true)
		frm.msg_end_date.value = "UNLIMITED";
	else{
		var hour ="";
		var min ="";
		var mm="";
		var dd="";
		if(frm.end_hour.value<10 && frm.end_hour.value.length < 2){
			hour ="0";
		}
		if(frm.end_min.value<10 && frm.end_min.value.length < 2){
			min ="0";
		}
		if(frm.end_mm.value<10 && frm.end_mm.value.length < 2){
			mm ="0";
		}
		if(frm.end_dd.value<10 && frm.end_dd.value.length < 2){
			dd ="0";
		}
		frm.msg_end_date.value = frm.end_yy.value + "-" + mm+frm.end_mm.value + "-" +dd+frm.end_dd.value + " " + hour+frm.end_hour.value + ":" +min+frm.end_min.value + ":00"
	}
		
	
	if (frm.msg_type) {frm.msg_type.value = getUrlParam('msg_type');}
	if (frm.res_id) {
			if (frm.msg_type.value == 'RES') { frm.res_id.value = getUrlParam('res_id');}
	}
	var isReceipt = '';
	if(frm.msg_receipt && frm.msg_receipt.value == 'Yes'){
		isReceipt = 'true';
	}else{
		isReceipt = 'false';
	}
	frm.isReceipt.value = isReceipt;
	if(_wbAnnouncementValidateFrm(frm,lang)){		
		frm.method = "post";
		if(frm.msg_type.value == 'RES'){
			frm.url_success.value = wbAnnouncementSystemAnnLstUrl('all','RES',frm.res_id.value,'','','','','','announ_lst_popup.xsl')
		}else{
			frm.url_success.value = wbAnnouncementSystemAnnLstUrl('all','SYS')
		}
		if (show_all=='true') {
		    frm.url_success.value +='&msg_show_all=true';
		}
		var isMobile = getUrlParam('isMobile');
		if(isMobile){
			frm.url_success.value += '&isMobile=' + isMobile;
		}
		
		frm.url_failure.value = self.location.href;
		frm.action = wb_utils_servlet_url;
		
		if(frm.msg_tcr_id && frm.msg_type.value != 'RES'){
			var msg_title = frm.msg_title.value;
			var tcr_id = frm.msg_tcr_id.value;
			var msg_id = 0;
			if(frm.msg_id){
				msg_id = frm.msg_id.value;
			}
			$.ajax({
			        url : contextPath +"/app/admin/message/checkExistTitle",
			        type : 'POST',
			        data: {title : msg_title,id : msg_id, msg_tcr_id : tcr_id}, 
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	if(data.success == true){
			        		Dialog.alert(fetchLabel('label_core_requirements_management_69'));

			        	}else{
			        		frm.submit();
			        	}
			        }
			     }); 
		}else{
			frm.submit();
		}
		
	}
}

function wbAnnouncementDelSystemAnnLst(msg_id,msg_type,res_id,msg_timestamp,lang){
	if (confirm(eval('wb_msg_' + lang + '_confirm'))){
		url = wb_utils_invoke_servlet('cmd','del_msg','msg_id',msg_id,'msg_type',msg_type,'res_id',res_id,'msg_timestamp', msg_timestamp, 'url_success',self.location.href,'url_failure',self.location.href);
		window.location.href = url;
	}
}

function wbAnnouncementMultiDelSystemAnnLst(frm,msg_type,res_id,lang){
	frm.msg_lst.value = _wbAnnouncementGetMultiDelAnnLst(frm);
	if (frm.msg_lst.value == '') {
		Dialog.alert(eval('wb_msg_' + lang + '_sel_del_ann'));
	}else{
		if (confirm(eval('wb_msg_' + lang + '_confirm'))){			
			frm.cmd.value = 'del_msg_lst';
			frm.url_success.value = self.location.href;
			frm.url_failure.value = self.location.href;
			frm.msg_type.value = msg_type;
			if (res_id != null && res_id != '') {frm.res_id.value = res_id;}
			frm.method = 'post';
			frm.action = wb_utils_servlet_url;
			frm.submit();
		}
	}
}

/* validate functions */
function _wbAnnouncementValidateFrm(frm,lang){
	if (frm.msg_title && frm.msg_title.type == 'text'){
		frm.msg_title.value = wbUtilsTrimString(frm.msg_title.value);
		if (!gen_validate_empty_field(frm.msg_title, eval('wb_msg_' + lang + '_title'),lang)) {
			frm.msg_title.focus();
			return false;
		}else{
			if(getChars(wbUtilsTrimString(frm.msg_title.value))>80){//标题不能超过80字符
				alert(fetchLabel('label_core_requirements_management_55'));
				return false;
			}
		}
	}
	
	if (frm.msg_icon_select) {
		var msg_icon_select_rs = '';
		for (var i = 0; i < frm.msg_icon_select.length; i++) {
			var msg_icon_select_item = frm.msg_icon_select[i];
			if (msg_icon_select_item.checked == true) {
				msg_icon_select_rs = msg_icon_select_item.value;
			}
		}
		
		if (msg_icon_select_rs == 'msg_icon_change') {
			var msg_icon = frm.msg_icon_file.value;
			if (msg_icon != null && msg_icon != '') {
				var file_ext = msg_icon.substring(msg_icon.lastIndexOf(".") + 1);
				if (file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png') {
					alert(eval('wb_msg_' + lang + '_icon_ext'));
				}
			} else {
				alert(eval('wb_msg_' + lang + '_icon_upload'));
				return;
			}
		}
		frm.msg_icon_result.value = msg_icon_select_rs;
	}
	//if (frm.msg_body && frm.msg_body.type == 'textarea'){
	if (frm.msg_body) {
        var space_ptn = /&nbsp;/ig;          //过滤空格&&标签结尾
        frm.msg_body.value = frm.msg_body.value.replace(space_ptn,"");
		frm.msg_body.value = wbUtilsTrimString(frm.msg_body.value);
		if (!gen_validate_empty_field(frm.msg_body, eval('wb_msg_' + lang + '_content'),lang)) {
			if (frm.msg_body.type == 'textarea') {
				frm.msg_body.focus()			
			}
			return false;		
		}else{ //检查是否有kindeditor生成的 '<p>&nbsp;</p>'格式的空白字符串
			var bodyStartIndex = frm.msg_body.value.indexOf(">");
			var bodyLastIndex = frm.msg_body.value.lastIndexOf("<");
			if(bodyStartIndex!=-1 && bodyLastIndex!=-1){
				var msg_body_sub = frm.msg_body.value.substring(bodyStartIndex,bodyLastIndex);
		        var space_ptn = /&nbsp;/ig;          //过滤空格&&标签结尾
		        msg_body_sub = msg_body_sub.replace(space_ptn,"");
		        msg_body_sub = wbUtilsTrimString(msg_body_sub);
		        //alert(msg_body_sub);
		        if(msg_body_sub == ''){
		        	 alert(wb_msg_pls_specify_value + eval('wb_msg_' + lang + '_content'));
		        	 frm.msg_body.focus();
		        	 return false;
		        }
			}
		}
		if(getChars(wbUtilsTrimString(frm.msg_body.value))>10000){//内容不能超过10000字符
			alert(fetchLabel('label_core_requirements_management_66'));
			return false;
		}
		
	}
	
	//msg_title_lan
	if (frm.msg_title_lan){
		if (!_wbAnnouncementValidateMsgTitleLan(frm,lang)){
			return false;			
		}
	}
	
	//msg_body_lan
	if (frm.msg_body_lan){	
		if (!_wbAnnouncementValidateMsgBodyLan(frm,lang)){
			return false;			
		}
	}
	
	//msg_extra_1_lan
	if (frm.msg_extra_1_lan){
		if (!_wbAnnouncementValidateMsgExtra1Lan(frm,lang)){return false;}
	}
	
	//msg_start_date	
	if(frm.start_date[1].checked == true){
		if (!wbUtilsValidateDate('document.frmXml.start',eval('wb_msg_' + lang + '_ann_begin_datetime'),'','ymdhm')){
			return false;
		}
		if (!wbUtilsValidateDate('document.frmXml.start',eval('wb_msg_' + lang + '_ann_begin_datetime'),'','ymdhm')){
			return false;
		}
	}
	
	//msg_end_date
	if(frm.end_date[1].checked == true){		
		if (!wbUtilsValidateDate('document.frmXml.end',eval('wb_msg_' + lang + '_ann_end_datetime'),'','ymdhm')){
			return false;
		}	
		if (!wbUtilsValidateDate('document.frmXml.end',eval('wb_msg_' + lang + '_ann_end_datetime'),'','ymdhm')){
			return false;
		}
	}
	
	//comparison1
	if (frm.start_date[1].checked == true && frm.end_date[1].checked == true){				
		if (!wb_utils_validate_date_compare({
			frm:'document.' + frm.name,
			start_obj:'start',
			end_obj:'end', 
			start_nm:eval('wb_msg_' + lang + '_ann_begin_datetime'), 
			end_nm:eval('wb_msg_' + lang + '_ann_end_datetime')
			})){
			return false;
		}	
	}
	
	//comparison2
	if (frm.start_date[0].checked == true && frm.end_date[1].checked == true){	
		if (!wb_utils_validate_date_compare({
			frm:'document.' + frm.name,
			start_obj:'cur_dt',
			end_obj:'end', 
			start_nm:eval('wb_msg_' + lang + '_ann_begin_datetime'), 
			end_nm:eval('wb_msg_' + lang + '_ann_end_datetime'), 
			focus_obj:'end'
			})){
			return false;
		}
	}
	
	//tcr_id
	if (frm.msg_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		    alert(wb_msg_pls_input_tcr);
	        return false;
	    } else {
		    frm.msg_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	return true;
}

function _wbAnnouncementValidateMsgTitleLan(frm,lang){
	var i,n;
	
	if (frm.lan_encoding_cnt.value > 1){
		n = frm.msg_title_lan.length;
		for (i = 0; i < n; i++){
			ele = frm.msg_title_lan[i];
			if (!gen_validate_empty_field(ele, eval('wb_msg_' + lang + '_title'),lang)) {
				ele.focus();
				return false;
			}				
		}
	}else{
		ele = frm.msg_title_lan;
		if (!gen_validate_empty_field(ele, eval('wb_msg_' + lang + '_title'),lang)) {
			ele.focus();
			return false;
		}
	}
	return true;	
}

function _wbAnnouncementValidateMsgBodyLan(frm,lang){
	var i,n;
	
	if (frm.lan_encoding_cnt.value > 1){
		n = frm.msg_body_lan.length;
		for (i = 0; i < n; i++){
			ele = frm.msg_body_lan[i];
			if (!gen_validate_empty_field(ele, eval('wb_msg_' + lang + '_content'),lang)) {
				ele.focus();
				return false;
			}
		}
	}else{
		ele = frm.msg_body_lan;
		if (!gen_validate_empty_field(ele, eval('wb_msg_' + lang + '_content'),lang)) {
			ele.focus();
			return false;
		}
	}
	return true;	
}

function _wbAnnouncementValidateMsgExtra1Lan(frm,lang){
	var i,n;
	
	if (frm.lan_encoding_cnt.value > 1){
		n = frm.msg_extra_1_lan.length;
		for (i = 0; i < n; i++){
			ele = frm.msg_extra_1_lan[i];
			if (!gen_validate_empty_field(ele, eval('wb_msg_' + lang + '_ann_extra_1'),lang)) {
				ele.focus();
				return false;
			}				
		}
	}else{
		ele = frm.msg_extra_1_lan;
		if (!gen_validate_empty_field(ele, eval('wb_msg_' + lang + '_ann_extra_1'),lang)) {
			ele.focus();
			return false;
		}
	}
	return true;	
}

/* private functions */
function _wbAnnouncementGetMultiDelAnnLst(frm){
	var i, n, ele, str;
	str = "";
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i];
		if (ele.type == "checkbox" && ele.checked && ele.name != 'sel_all_checkbox') {
			if ( ele.value != "") {str = str + ele.value + "~";}
		}
	}	
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;		
}

function _wbAnnouncementMsgTitleGenXml(frm){
	var str, i, n;
	str = '';
	
	if (frm.lan_encoding_cnt.value > 1){
		n = frm.msg_title_lan.length;
		for (i = 0; i < n; i++){	
			ele = frm.msg_title_lan[i];
			ele2 = frm.msg_title_encoding[i];
			str += '<' + wb_utils_XmlEscape(ele2.value) + '>';
			str += wb_utils_XmlEscape(ele.value);
			str += '</' + wb_utils_XmlEscape(ele2.value) + '>'
		}
	}else{
		ele = frm.msg_title_lan;
		ele2 = frm.msg_title_encoding;
		str += '<' + wb_utils_XmlEscape(ele2.value) + '>';
		str += wb_utils_XmlEscape(ele.value);
		str += '</' + wb_utils_XmlEscape(ele2.value) + '>'
	}
	return str;		
}

function _wbAnnouncementMsgBodyGenXml(frm){
	var str, i, n, ele, ele2;
	str = '';
	
	if (frm.msg_extra_1_lan){
		if (frm.lan_encoding_cnt.value > 1){
			n = frm.msg_extra_1_lan.length;
			str += '<company>';
			for (i = 0; i < n; i++){
				ele = frm.msg_extra_1_lan[i];
				ele2 = frm.msg_extra_1_encoding[i];
				str += '<' + wb_utils_XmlEscape(ele2.value) + '>';
				str += wb_utils_XmlEscape(ele.value);
				str += '</' + wb_utils_XmlEscape(ele2.value) + '>'	
			}
			str += '</company>'
		}else{
			ele = frm.msg_extra_1_lan;
			ele2 = frm.msg_extra_1_encoding;
			str += '<company>';
			str += '<' + wb_utils_XmlEscape(ele2.value) + '>';
			str += wb_utils_XmlEscape(ele.value);
			str += '</' + wb_utils_XmlEscape(ele2.value) + '>';
			str += '</company>'
		}
	}
	
	if (frm.lan_encoding_cnt.value > 1){
		n = frm.msg_body_lan.length;
		for (i = 0; i < n; i++){	
			ele = frm.msg_body_lan[i];
			ele2 = frm.msg_body_encoding[i];
			ele3 = frm.msg_body_save_html[i];
			str += '<' + wb_utils_XmlEscape(ele2.value) + '>';
			if (ele3.checked == true) {str += '<html>';}
			str += wb_utils_XmlEscape(ele.value);
			if (ele3.checked == true) {str += '</html>';}
			str += '</' + wb_utils_XmlEscape(ele2.value) + '>'			
		}
	}else{
		ele = frm.msg_body_lan;
		ele2 = frm.msg_body_encoding;
		ele3 = frm.msg_body_save_html;
		str += '<' + wb_utils_XmlEscape(ele2.value) + '>';
		if (ele3.checked == true) {str += '<html>';}
		str += wb_utils_XmlEscape(ele.value);
		if (ele3.checked == true) {str += '</html>';}
		str += '</' + wb_utils_XmlEscape(ele2.value) + '>'		
	}	
	return str;		
}

function wbAnnouncementShowContent(tcr_id, isMobile) {
    var read_only = getUrlParam('msg_readonly');
    url = wb_utils_invoke_servlet('cmd', 'get_all_msg', 'stylesheet', 'announ_lst.xsl', 'msg_type', 'SYS', 'cur_page', 1,'sortCol', 'msg_begin_date', 'sortOrder', 'DESC', 'timestamp', '', 'msg_readonly', read_only, 'isMobile', isMobile,'msg_tcr_id', tcr_id);
	window.location.href = url;
}