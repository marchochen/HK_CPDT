// ------------------ wizBank Mote object ------------------- 
// Convention:
//   public functions : use "wbMote" prefix 
//   private functions: use "_wbMote" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbMote() {
	this.mote_lst = wbMoteLst
	this.mote_itm_lst = wbMoteItemLst
	this.mote_dtl = wbMoteDetail
	this.mote_itm_dtl = wbMoteItemDetail
	this.mote_upd_status = wbMoteUpdateStatusExec
	this.mote_upd = wbMoteUpdatePrep
	this.mote_upd_exec = wbMoteUpdateExec
	this.mote_ins = wbMoteInsPrep
	this.mote_ins_exec = wbMoteInsExec
	this.mote_del_exec = wbMoteDelExec
	this.get_res_detail = wbMoteGetResourceDetailWin
}

/* public functions */
function wbMoteLst(status,cur_page,page_size,sort_col,sort_order,timestamp){
	if (status == null) {status = 'progress'};	
	if (sort_col == null || sort_col == '') {sort_col = 'duedate';}
	if (sort_order == null || sort_order == '') {sort_order = 'ASC';}
	if (cur_page == null || cur_page == '') {cur_page = '0';}
	if (page_size == null || page_size == '') {page_size = '10';}
	if (timestamp == null || timestamp == '') {timestamp = '';}
		
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_resp_mote_lst',
		'module','mote.MoteModule',
		'mote_status',status,
		'cur_page',cur_page,
		'page_size',page_size,
		'sort_col',sort_col,
		'sort_order',sort_order,
		'timestamp',timestamp,
		'stylesheet','mote_lst.xsl'
	)	
	window.location.href = url;
}

function wbMoteItemLst(itm_id,status,cur_page,page_size,sort_col,sort_order,timestamp){	
	url = wbMoteItemLstUrl(itm_id,status,cur_page,page_size,sort_col,sort_order,timestamp)
	window.location.href = url;
}

function wbMoteItemLstUrl(itm_id,status,cur_page,page_size,sort_col,sort_order,timestamp){
	if (status == null) {status = ''};	
	if (sort_col == null || sort_col == '') {sort_col = '';}
	if (sort_order == null || sort_order == '') {sort_order = 'ASC';}
	if (cur_page == null || cur_page == '') {cur_page = '0';}
	if (page_size == null || page_size == '') {page_size = '10';}
	if (timestamp == null || timestamp == '') {timestamp = '';}
	
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_mote_lst_by_itm',
		'module','mote.MoteModule',
		'mote_status',status,
		'cur_page',cur_page,
		'page_size',page_size,
		'sort_col',sort_col,
		'sort_order',sort_order,
		'timestamp',timestamp,
		'stylesheet','mote_itm_lst.xsl',
		'itm_id',itm_id
	)
	return url;	
}


function wbMoteDetail(imt_id,itm_id){
	url = wbMoteDetailUrl(imt_id,itm_id)	
	window.location.href = url;
}

function wbMoteDetailUrl(imt_id,itm_id){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_mote',
		'module','mote.MoteModule',
		'imt_id',imt_id,
		'itm_id',itm_id,
		'stylesheet','mote_dtl.xsl'
	)
	return url;
}


function wbMoteItemDetail(itm_id){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_top_mote_by_itm',
		'module','mote.MoteModule',
		'itm_id',itm_id,
		'url_failure',self.location.href,
		'stylesheet','mote_dtl.xsl'
	)
	window.location.href = url;
}

function wbMoteUpdateStatusExec(frm,imt_id,status){
	
	url = wb_utils_invoke_disp_servlet(
		'cmd','upd_mote_status',
		'module','mote.MoteModule',
		'imt_id',imt_id,
		'status',status,
		'url_success',wbMoteDetailUrl(imt_id,frm.item_id.value),
		'url_failure',wbMoteDetailUrl(imt_id,frm.item_id.value)
	)	
	window.location.href = url;
}

function wbMoteUpdatePrep(imt_id,itm_id){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_mote',
		'module','mote.MoteModule',
		'imt_id',imt_id,
		'itm_id',itm_id,
		'stylesheet','mote_upd.xsl'
	)	
	window.location.href = url;	
}

function wbMoteInsPrep(imd_id){
	url = wb_utils_invoke_disp_servlet(
		'cmd', 'ins_prep_mote',
		'module','mote.MoteModule',
		'imd_id',imd_id,
		'stylesheet','mote_ins.xsl',
		'itm_id',getUrlParam('itm_id')
	)
	window.location.href = url;
}

function wbMoteInsExec(frm,lang){

	if(_wbMoteValidateInsFrm(frm,lang)){
				
		if (frm.level1_ind.checked){frm.level1_ind.value = 1;}
		else{frm.level1_ind.value = 0;}
			
		if (frm.level2_ind.checked){frm.level2_ind.value = 1;}
		else{frm.level2_ind.value = 0;}
			
		if (frm.level3_ind.checked){frm.level3_ind.value = 1;}
		else{frm.level3_ind.value = 0;}
		
		if (frm.level4_ind.checked){frm.level4_ind.value = 1;}
		else{frm.level4_ind.value = 0;}
		
		//alert(frm.level1_ind.value + " " + frm.level2_ind.value + " " + frm.level3_ind.value + " " + frm.level4_ind.value)
		
		frm.module.value = 'mote.MoteModule'
		frm.cmd.value = 'ins_mote'		
		frm.url_success.value = wbMoteItemLstUrl(getUrlParam('itm_id'))
		frm.url_failure.value = self.location.href	
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.submit()		
	}	
}

function wbMoteUpdateExec(frm,lang){
	
	if (_wbMoteValidateUpdFrm(frm,lang)){
		
		if (frm.itm_type.value != 'CLASSROOM' && frm.itm_type.value != 'SELFSTUDY' && frm.itm_type.value != 'VIDEO'){			
			if (frm.level1_ind.checked){frm.level1_ind.value = 1;}
			else{frm.level1_ind.value = 0;}
				
			if (frm.level2_ind.checked){frm.level2_ind.value = 1;}
			else{frm.level2_ind.value = 0;}
				
			if (frm.level3_ind.checked){frm.level3_ind.value = 1;}
			else{frm.level3_ind.value = 0;}
			
			if (frm.level4_ind.checked){frm.level4_ind.value = 1;}
			else{frm.level4_ind.value = 0;}
		}	
		
		if (frm.plan_xml && frm.mote_plan)
			frm.plan_xml.value = _gen_xml_code(frm,'mote_plan')
		if (frm.attend_comment_xml)
			frm.attend_comment_xml.value = _gen_xml_code(frm,'attend_comment');
		if (frm.rating_comment_xml)
			frm.rating_comment_xml.value = _gen_xml_code(frm,'rating_comment');
		if (frm.cost_comment_xml)
			frm.cost_comment_xml.value = _gen_xml_code(frm,'cost_comment');
		if (frm.time_comment_xml)
			frm.time_comment_xml.value = _gen_xml_code(frm,'time_comment');
		if (frm.comment_xml)
			frm.comment_xml.value = _gen_xml_code(frm,'comment');			
		if (frm.itm_type.value == 'CLASSROOM'){
			if (frm.rating_actual_xml)
				frm.rating_actual_xml.value = _gen_xml_code(frm,'rating_actual');
		}
		
		if (frm.attch1_xml && frm.attch1){ frm.attch1_xml.value = _gen_xml_code(frm,'attch1')}
		if (frm.attch2_xml && frm.attch2){ frm.attch2_xml.value = _gen_xml_code(frm,'attch2')}
		if (frm.attch3_xml && frm.attch3){ frm.attch3_xml.value = _gen_xml_code(frm,'attch3')}
		if (frm.attch4_xml && frm.attch4){ frm.attch4_xml.value = _gen_xml_code(frm,'attch4')}
		
		frm.cmd.value = 'upd_mote'		
		frm.module.value = 'mote.MoteModule'		
		frm.url_success.value = wbMoteDetailUrl(frm.imt_id.value,frm.item_id.value)	
		frm.url_failure.value = window.location.href
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		//_wbMoteGetMoteDebugger(frm)		
		frm.submit()
	}
}

function wbMoteDelExec(frm,lang){
	var imt_id_lst = _wbMoteGetDelMoteLst(frm)
	
	if (imt_id_lst == '') {
			alert(eval('wb_msg_' + lang + '_select_mote'))
	}else{
		if(confirm(eval('wb_msg_' + lang + '_confirm_del_mote'))){		
			url = wb_utils_invoke_disp_servlet(
				'cmd', 'del_multi_mote',
				'module', 'mote.MoteModule',
				'itm_id', getUrlParam('itm_id'),
				'imt_id_lst', imt_id_lst,
				'url_success', wbMoteItemLstUrl(getUrlParam('itm_id')),
				'url_failure', wbMoteItemLstUrl(getUrlParam('itm_id'))				
			)
			window.location.href = url;				
		}
	}
}

function wbMoteGetResourceDetailWin(res_id){

	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','res_srh_res_ind.xsl','url_failure',self.location.href)	
	feature = 'toolbar='		+ 'no'
		+ ',location='				+ 'no'
		+ ',width=' 				+ 800
		+ ',height=' 				+ 400
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',left='				+ '100'
		+ ',top='				+ '100'		
		+ ',status='				+ 'no'
	wbUtilsOpenWin(url, 'win',false,feature)	
}

/* private functions */
function _gen_xml_code(frm,xml_obj){
	var str, ele, i, n
	str = ''
	
	if (xml_obj == 'attend_comment' || xml_obj == 'rating_comment' || xml_obj == 'time_comment' || xml_obj == 'cost_comment'){
		str += "<comment>"
		str += wb_utils_XmlEscape(eval("frm." + xml_obj + ".value"))
		str += "</comment>"
		
	}else if (xml_obj == 'rating_actual'){
		n = eval("frm." + xml_obj + ".length")
		for (i = 0; i < n; i++){
			ele = eval("frm." + xml_obj + "[" + i + "]")
			if (i == 0){
				str += '<actual type=\"OVERALL\">' + wb_utils_XmlEscape(ele.value) + '</actual>'
			}else{
				str += '<actual type=\"RUN\" id=\"' + eval("frm." + xml_obj + "_title[" + i + "].value") + '\">' 
				str += wb_utils_XmlEscape(ele.value) 
				str += '</actual>'
			}
		}
	}else if (xml_obj == 'comment'){
		str += '<comment>'
		str += '<positive>' + wb_utils_XmlEscape(frm.pos_cmt.value) + '</positive>'
		str += '<negative>' + wb_utils_XmlEscape(frm.neg_cmt.value) + '</negative>'
		str += '<cm>' + wb_utils_XmlEscape(frm.cm_cmt.value) + '</cm>'
		str += '<im>' + wb_utils_XmlEscape(frm.im_cmt.value) + '</im>'
		str += '<suggest>' + wb_utils_XmlEscape(frm.suggest_cmt.value) + '</suggest>'
		str += '</comment>'		
	}else if (xml_obj.substring(0,5) == 'attch'){
		
		n = eval("frm." + xml_obj + ".options.length")
		str += '<attachment><resource_list>'
		for (i=0; i < n; i++){
			str += '<resource id=\"' + wb_utils_XmlEscape(eval("frm." + xml_obj + ".options[" + i + "].value")) + '\">' 
			str += wb_utils_XmlEscape(eval("frm." + xml_obj + ".options[" + i + "].text"))
			str += '</resource>'
		}
		str += '</resource_list></attachment>';
	}else if (xml_obj == 'mote_plan'){
		n = eval("frm." + xml_obj + ".options.length")
		str += '<mote_plan><resource_list>'
		for (i=0; i < n; i++){
			str += '<resource id=\"' + wb_utils_XmlEscape(eval("frm." + xml_obj + ".options[" + i + "].value")) + '\">' 
			str += wb_utils_XmlEscape(eval("frm." + xml_obj + ".options[" + i + "].text"))
			str += '</resource>'
		}
		str += '</resource_list></mote_plan>'
	}
	return str;
}

function _wbMoteGetMoteDebugger(frm,obj){
	var str
	str = ''
	str = 'cmd: ' + frm.cmd.value + '\n' +
		  'imt_id: ' + frm.imt_id.value + '\n' +
		  'itm_type: ' + frm.itm_type.value + '\n' +
		  'imd_id: ' + frm.imd_id.value + '\n' +
		  '----------------------------------------------------------------------------------------------' + '\n'		
	
	if (frm.itm_type.value == 'SELFSTUDY' || frm.itm_type.value == 'VIDEO'){
		str +=	'title: ' + frm.title.value + '\n' +
				'eff_start_dd: ' + frm.eff_start_dd.value + '\n' +
				'eff_start_mm: ' + frm.eff_start_mm.value + '\n' +
				'eff_start_yy: ' + frm.eff_start_yy.value + '\n' +
				'eff_start_date: ' + frm.eff_start_date.value + '\n' +		
				'eff_end_dd: ' + frm.eff_end_dd.value + '\n' +
				'eff_end_mm: ' + frm.eff_end_mm.value + '\n' +
				'eff_end_yy: ' + frm.eff_end_yy.value + '\n' +
				'eff_end_date: ' + frm.eff_end_date.value + '\n' +
				'due_dd: ' + frm.due_dd.value + '\n' +
				'due_mm: ' + frm.due_mm.value + '\n' +
				'due_yy: ' + frm.due_yy.value + '\n'
	}
	
	str +=	'due_date: ' + frm.due_date.value + '\n' +
			'status : ' + frm.status.value + '\n' +
			'----------------------------------------------------------------------------------------------' + '\n' +
			'plan_xml: ' + frm.plan_xml.value + '\n' +
			'----------------------------------------------------------------------------------------------' + '\n' +
			'level1_ind: ' + frm.level1_ind.value + '\n' +
			'level2_ind: ' + frm.level2_ind.value + '\n' +
			'level3_ind: ' + frm.level3_ind.value + '\n' +
			'level4_ind: ' + frm.level4_ind.value + '\n' +
			'----------------------------------------------------------------------------------------------' + '\n' +
			'attend_comment: ' + frm.attend_comment.value + '\n' +
			'attend_comment_xml: ' + frm.attend_comment_xml.value + '\n' +
			'----------------------------------------------------------------------------------------------' + '\n' +
			'rating_target: ' + frm.rating_target.value + '\n'
	
	if (frm.itm_type.value == 'SELFSTUDY' || frm.itm_type.value == 'VIDEO')
		//str += 'rating_actual: ' + frm.rating_actual.value + '\n'
		str += '';
	else
		if (frm.rating_actual.length > 0){
			str += 'rating_actual: ' + frm.rating_actual[0].value + '\n' +
				   'rating_actual_xml: ' + frm.rating_actual_xml.value + '\n';
		}else{
			str += 'rating_actual: ' + frm.rating_actual.value + '\n' +
	               'rating_actual_xml: ' + frm.rating_actual_xml.value + '\n';
		}	
		
	str +=	'rating_comment: ' + frm.rating_comment.value + '\n' +
			'rating_comment_xml: ' + frm.rating_comment_xml.value + '\n' +
			'----------------------------------------------------------------------------------------------' + '\n' +
			'cost_target: ' + frm.cost_target.value + '\n' + 
			'cost_actual: ' + frm.cost_actual.value + '\n' +		
			'cost_comment: ' + frm.cost_comment.value + '\n' +
			'cost_comment_xml: ' + frm.cost_comment_xml.value + '\n' +
			'----------------------------------------------------------------------------------------------' + '\n' ;
	
	if (frm.time_target){
		str +=	'time_target: ' + frm.time_target.value + '\n' + 
				'time_actual: ' + frm.time_actual.value + '\n' +		
				'time_comment: ' + frm.time_comment.value + '\n' +
				'time_comment_xml: ' + frm.time_comment_xml.value + '\n' +
				'----------------------------------------------------------------------------------------------' + '\n';
	}
	
	str +=	'pos_cmt: ' + frm.pos_cmt.value + '\n' +
			'neg_cmt: ' + frm.neg_cmt.value + '\n' +
			'cm_cmt: ' + frm.cm_cmt.value + '\n' +
			'im_cmt: ' + frm.im_cmt.value + '\n' +
			'suggest_cmt: ' + frm.suggest_cmt.value + '\n' +
			'comment_xml: ' + frm.comment_xml.value + '\n' +
			'----------------------------------------------------------------------------------------------' + '\n';
		
	if (frm.attch1_xml)
		str += 'attch1_xml: ' + frm.attch1_xml.value + '\n';
	if (frm.attch2_xml)
		str += 'attch2_xml: ' + frm.attch2_xml.value + '\n';
	if (frm.attch3_xml)
		str += 'attch3_xml: ' + frm.attch3_xml.value + '\n';
	if (frm.attch4_xml)
		str += 'attch4_xml: ' + frm.attch4_xml.value + '\n';
		
	document.write(str)
	//alert(str)
}

function _wbMoteGetDelMoteLst(frm){
	var str, ele, i, n
	str = ''
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name == "imt_id") {
			if ( ele.value != "")
				str = str + ele.value + "~"
		}
	}
	
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;	
}

/* validate functions */
function _wbMoteValidateInsFrm(frm,lang){	
	
	if (!gen_validate_empty_field(frm.title, eval('wb_msg_'+ lang + '_title'),lang)) {
		return false;
	}
	
	//Date Validation				
	if(!_wbMoteValidateDateEmpty(frm,'eff_start',lang))
		return false;	
	if(!_wbMoteValidateDate(frm,'eff_start',lang)){
		return false;
	}else{
		frm.eff_start_date.value = frm.eff_start_yy.value + '-' +  frm.eff_start_mm.value + '-' +  frm.eff_start_dd.value + ' 00:00:00.0';
	}
	
	if(!_wbMoteValidateDateEmpty(frm,'eff_end',lang))
		return false;	
	if(!_wbMoteValidateDate(frm,'eff_end',lang)){
		return false;
	}else{
		frm.eff_end_date.value = frm.eff_end_yy.value + '-' +  frm.eff_end_mm.value + '-' +  frm.eff_end_dd.value + ' 23:59:00.0';
	}
	
	if (!gen_validate_date_compare(frm,'eff_start','eff_end', eval('wb_msg_' + lang + '_period_start_date'), eval('wb_msg_' + lang + '_period_end_date'), lang)){
		return false;
	}
	
	if (frm.due_yy.value != '' || frm.due_mm.value != '' || frm.due_dd.value != ''){
		//if(!_wbMoteValidateDateEmpty(frm,'due',lang))
			//return false;
		if(!_wbMoteValidateDate(frm,'due',lang)){
			return false;
		}else{
			frm.due_date.value = frm.due_yy.value + '-' +  frm.due_mm.value + '-' +  frm.due_dd.value + ' 00:00:00.0';
		}	
	}
	return true;
}

function _wbMoteValidateUpdFrm(frm,lang){
	
	if (frm.title){
		if (!gen_validate_empty_field(frm.title, eval('wb_msg_'+ lang + '_title'),lang)) {
			return false;
		}
	}
	
	if (frm.eff_start_yy && frm.eff_start_mm && frm.eff_start_dd){					
		if(!_wbMoteValidateDateEmpty(frm,'eff_start',lang))
			return false;		
		if(!_wbMoteValidateDate(frm,'eff_start',lang)){
			return false;
		}else{
			frm.eff_start_date.value = frm.eff_start_yy.value + '-' +  frm.eff_start_mm.value + '-' +  frm.eff_start_dd.value + ' 00:00:00.0';
		}
	}
	
	if (frm.eff_end_yy && frm.eff_end_mm && frm.eff_end_dd){
		if(!_wbMoteValidateDateEmpty(frm,'eff_end',lang))
			return false;
		if(!_wbMoteValidateDate(frm,'eff_end',lang)){
			return false;
		}else{
			frm.eff_end_date.value = frm.eff_end_yy.value + '-' +  frm.eff_end_mm.value + '-' +  frm.eff_end_dd.value + ' 23:59:00.0';
		}
	}
		
	if (frm.eff_start_date && frm.eff_end_date){
		if (!gen_validate_date_compare(frm,'eff_start','eff_end', eval('wb_msg_' + lang + '_eff_start_date'), eval('wb_msg_' + lang + '_eff_end_date'), lang)){
			return false;
		}
	}
				
	if (frm.due_yy && frm.due_mm && frm.due_dd){
		//if(!_wbMoteValidateDateEmpty(frm,'due',lang))
			//return false;
		if (frm.due_yy.value != '' || frm.due_mm.value != '' || frm.due_dd.value != ''){
			if(!_wbMoteValidateDate(frm,'due',lang)){
				return false;
			}else{
				frm.due_date.value = frm.due_yy.value + '-' +  frm.due_mm.value + '-' +  frm.due_dd.value + ' 00:00:00.0';
			}
		}
	}	
	
	if (frm.rating_target && frm.rating_target.value != ''){
		if (!gen_validate_float(frm.rating_target, eval("wb_msg_" + lang + "_rating_target"), lang))
			return false;
	}
	
	if (frm.rating_actual){
		if (!_wbMoteValidateRatingActual(frm,lang)){
			return false;
		}		
	}
	
	if (frm.cost_target && frm.cost_target.value != ''){
		if (!gen_validate_float(frm.cost_target, eval("wb_msg_" + lang + "_cost_target"), lang))
			return false;
	}
	
	if (frm.cost_actual && frm.cost_actual.value != ''){
		if (!gen_validate_float(frm.cost_actual, eval("wb_msg_" + lang + "_cost_actual"), lang)){
			return false;
		}
	}	
	
	if (frm.time_target && frm.time_target.value != ''){		
		if (!gen_validate_float(frm.time_target, eval("wb_msg_" + lang + "_time_target"), lang))
			return false;		
	}
	
	if (frm.time_actual && frm.time_actual.value != ''){
		if (!gen_validate_float(frm.time_actual, eval("wb_msg_" + lang + "_time_actual"), lang)){
			return false;
		}
	}
	
	return true;
}

function _wbMoteValidateDateEmpty(frm,obj_nm,lang){
	if (eval("frm." + obj_nm + "_dd.value") == "" || eval("frm." + obj_nm +"_mm.value") == "" || eval("frm." + obj_nm + "_yy.value") == ""){
		alert(eval("wb_msg_" + lang + "_enter_valid") + " \"" + eval("wb_msg_" + lang + "_" + obj_nm + "_date") + "\"")
		eval("frm." + obj_nm + "_yy.focus()")
		return false;
	}
	return true;
}

function _wbMoteValidateDate(frm,obj_nm,lang){
	
	if (eval("frm." + obj_nm + "_dd.length") != 0 || eval("frm." + obj_nm +"_mm.length") != 0 || eval("frm." + obj_nm + "_yy.length") != 0){
		if (gen_validate_date('document.frmXml.' + obj_nm,eval('wb_msg_' + lang + '_' + obj_nm + '_date'),lang)){
			return true;
		}else{
			return false;
		}
	}	
}

function _wbMoteValidateRatingActual(frm,lang){
	var i,n
	n = frm.rating_actual.length
	for (i = 1; i < n; i++){
		if (frm.rating_actual[i].value != '') {
			if (!gen_validate_float(frm.rating_actual[i],eval('wb_msg_' + lang + '_overall_rating'),lang)){
				return false;
			}
		}		
	}
	return true;
}