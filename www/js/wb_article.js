

function wbArticle() {
		this.get_article_prep = article_prep;
		this.sort_article_list = article_sort;
		this.search_key_article_list = article_key_search;
		this.search_article_list = article_search;
		this.ins_upd_article = article_insUpd;
		//this.article_del = article_del
		this.get_article_view = article_view;
		this.article_del_id = article_del_id;
		this.get_article_list = get_article_list;
		
		this.get_article_type_maintain = article_type_maintain;
		this.get_article_type_prep = article_type_prep;
		this.ins_upd_article_type = article_type_insUpd;
		this.del_article_type = article_type_del;
};
function get_article_list(){
	var url = wb_utils_invoke_disp_servlet('module',
			'article.ArticleModule', 'cmd', 'get_article_list',
			'stylesheet', 'article_list.xsl');
	window.location.href = url;
}

function article_prep(id, tcr_id) {
	if(id == undefined)
	{
		id = '';
	}
	if(tcr_id == null || tcr_id == '') tcr_id = 0;
	var url = wb_utils_invoke_disp_servlet('module',
			'article.ArticleModule', 'cmd', 'get_article_prep',
			'stylesheet', 'article_ins_upd.xsl', 'encryp_tart_id', id, 'tcr_id', tcr_id);
	window.location.href = url;
}

function down_article_list(){
	var url = wb_utils_invoke_disp_servlet('module',
			'article.ArticleModule', 'cmd', 'down_article_list',
			'stylesheet', 'article_list.xsl');
	window.location.href = url;
}

function article_view(id){
	var url = wb_utils_controller_base + "admin/article/mgt_comments?id="+id;
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 				+ '1100'
	+ ',height=' 				+ '800'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'no' 
	//wbUtilsOpenWin(url, 'article', false, str_feature);
	window.location.href = url;
}
function article_sort(frm, sort_col, sort_order) {
	var url = wb_utils_invoke_disp_servlet('module',
			'article.ArticleModule', 'cmd', 'get_article_list',
			'stylesheet', "article_list.xsl", 'sort_col', sort_col, 'sort_order', sort_order);
	window.location.href = url;
}

function article_search(art_tcr_id, art_type,is_mobile) {
	url = wb_utils_invoke_disp_servlet("module","article.ArticleModule","cmd", 'get_article_list', 'stylesheet', 'article_list.xsl', 'art_tcr_id',art_tcr_id,'art_type',art_type,'art_push_mobile',is_mobile);
	window.location.href = url;
}


function article_key_search(frm,is_mobile,art_tcr_id,art_type) {
	var art_keywords = frm.art_keywords.value;
	url = wb_utils_invoke_disp_servlet("module","article.ArticleModule","cmd", 'get_article_list', 'stylesheet', 'article_list.xsl','art_tcr_id',art_tcr_id,'art_type',art_type, 'art_keywords',art_keywords,'art_push_mobile',is_mobile);
	window.location.href = url;
}

function article_insUpd(frm, lang) {
	if (editor) {
		try {
			editor.sync();
		} catch(e) {}
	}
	if(!wbUtilsValidateEmptyField(frm.art_title, frm.lab_article_field_title.value)){
		frm.article_title.focus();
		return;
	}
	if(frm.art_icon_select){
		if(frm.msg_icon_select2.checked){
			if(!wbUtilsValidateEmptyField(frm.art_icon_file, frm.lab_article_field_icon.value)){
				frm.art_icon_file.focus();
				return;
			}
			frm.default_image.value = '';
		}

	}else{
		if(!wbUtilsValidateEmptyField(frm.art_icon_file, frm.lab_article_field_icon.value)){
			frm.art_icon_file.focus();
			return;
		}
	}
	if(!wbUtilsValidateEmptyField(frm.art_introduction, frm.lab_desc.value)){
		frm.art_introduction.focus();
		return;
	} else {
		if($("textarea[name='art_introduction']").attr('prompt') == frm.art_introduction.value){
			alert(wb_msg_usr_please_specify_value + '"' + frm.lab_desc.value + '"' );
			frm.art_introduction.focus();
			return;
		}
	}
	if(getChars(frm.art_title.value) > 80){
		Dialog.alert(fetchLabel('label_core_requirements_management_55'));
		//alert($("textarea[name='art_introduction']").attr('prompt'));
		return;
	}
	if(getChars(frm.art_introduction.value) > 400){
		alert($("textarea[name='art_introduction']").attr('prompt'));
		return;
	}
	if(!wbUtilsValidateEmptyField(frm.art_content, frm.lab_content.value)){
		frm.art_content.focus();
		return;
	} else{
		var contetn=frm.art_content.value.replace(/&nbsp;/ig,'').replace(/(^\s*)|(\s*$)/g,'');
		if(contetn==''){
			alert(wb_msg_usr_please_specify_value + '"' + frm.lab_content.value + '"' );
			frm.art_introduction.focus();
			return;
		}
		if(getChars(contetn) > 20000){
			alert(fetchLabel('label_core_requirements_management_66'));
			frm.art_introduction.focus();
			return;
		}
	}
	//tcr_id
	if (frm.art_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		    alert(wb_msg_pls_input_tcr);
	        return false;
	    } else {
		    frm.art_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	//return;
	frm.module.value = 'article.ArticleModule';	
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_list','stylesheet', 'article_list.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_list','stylesheet', 'article_list.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}


function article_del_id(frm,lang){
	var artID = [];
	$("input[name='art_id']:checked").each(function(){
		artID.push($(this).val());
	});
	if(artID == ''){
		Dialog.alert(fetchLabel("label_core_information_management_50"))
		return;
	}
	if (confirm(eval('wb_msg_' + lang + '_confirm'))){
		frm.module.value = 'article.ArticleModule';
		frm.cmd.value = 'article_del_id';
		frm.art_id_str.value = artID.join(',');
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_list','stylesheet', 'article_list.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_list','stylesheet', 'article_list.xsl');
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit();
	}
}

function compareDate(strDate1,strDate2)
{
     var date1 = new Date(strDate1.replace(/\-/g, "\/"));
     var date2 = new Date(strDate2.replace(/\-/g, "\/"));
     return date1-date2;
}


function toDecimal2(x) {  
	
    if (!isDouble(x)) {  
        return false;  
    }  
    return (x*1).toFixed(2);  
} 

function wbarticleCommentView(spl_id){
	var  url= wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_articler_comment_list','spl_id',spl_id,'stylesheet', 'articlecomment_list.xsl');
	self.location.href = url;
}

function wbarticleCommentAdd(spl_id,ent_id){
	url = wb_utils_invoke_disp_servlet('module', 'article.ArticleModule', 'cmd', 'articler_comment_add_prep','spl_id',spl_id,'scm_ent_id',ent_id, 'stylesheet', 'articlecomment_add.xsl');
	self.location.href = url;
}

function wbarticleCommentSub(frm){
	var scm_design_score = frm.scm_design_score.value;
	var scm_price_score = frm.scm_price_score.value;
	var scm_teaching_score = frm.scm_teaching_score.value;
	var scm_management_score = frm.scm_management_score.value;
	if(scm_design_score ==''){
		alert(wb_supp_score);
		frm.scm_design_score.focus();
		return;
	}else if(scm_price_score ==''){
		alert(wb_supp_score);
		frm.scm_price_score.focus();
		return;
	}else if(scm_teaching_score ==''){
		alert(wb_supp_score);
		frm.scm_teaching_score.focus();
		return;
	}else if(scm_management_score ==''){
		alert(wb_supp_score);
		frm.scm_management_score.focus();
		return;
	}
	if(frm.scm_comment !=null){
		frm.scm_comment.value=frm.scm_comment.value.replace(/(^\s*)|(\s*$)/g, "");
		if(frm.scm_comment.value.length ==0){
			alert(wb_supp_comment_empty);
			return;
		}else if(frm.scm_comment.value.length >200){
			alert(wb_supp_comment_length);
			return;
		}
	}else{
		alert(wb_supp_comment_empty);
		return;
	}
	frm.scm_score.value = frm.scm_score.value;
	frm.module.value = 'article.ArticleModule';
	if(frm.up_scm_ent_id && frm.up_scm_ent_id.value >0){
		frm.cmd.value = 'articler_comment_up';
	}else	
		frm.cmd.value = 'articler_comment_add';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_articler_comment_list','spl_id',frm.spl_id.value,'stylesheet', 'articlecomment_list.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_articler_comment_list','spl_id',frm.spl_id.value,'stylesheet', 'articlecomment_list.xsl');
	frm.action = wb_utils_disp_servlet_url;
	frm.method = 'post';
	frm.submit();
}

/** 
* 此函数进行Email格式检测. 
* @param str 待检测字符串. 
* @return 是Email格式返回真. 
*/ 
function isEmail(str){ 
	var res = /^[0-9a-zA-Z_\-\.]+@[0-9a-zA-Z_\-]+(\.[0-9a-zA-Z_\-]+)*$/; 
	var re = new RegExp(res); 
	return !(str.match(re) == null); 
} 
/** 
* 此函数大于等于0. 
* @param val 待检测. 
* @return 是返回真. 
*/ 
function isGtEqZero(val){
	var res = /^(0|[1-9][0-9]*)$/;
	var re = new RegExp(res);
	return re.test(val)
}

function isDate(dt){
	var res = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-9]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
	var re = new RegExp(res);
	return re.test(dt)
}

function isDouble(val){
	var res = /^\d+\.?\d+$|^\d+$/;
	var re = new RegExp(res);
	return re.test(val);
}
//jquery
/*function renameCourse(){
	var proj_tb = $("#spl_course_table");
	proj_tb.find('.course_tr').each(function(i){
		
	}
}
*/
function get_course_list(frm,lang){
	var proj_tb = $("#spl_course_table");
	//alert($(proj_tb).is("table"));
	var html = [];
	var course_flag = true;
	//alert(proj_tb.find('.course_tr').length);
	proj_tb.find('.course_tr').each(function(i){
		var mark = 3;

		var smcName = $(this).find(".smcName");
		var smcInst = $(this).find(".smcInst");
		var smcPrice = $(this).find(".smcPrice");
		var smcNameVal = $(smcName).val();
		var smcInstVal = $(smcInst).val();
		var smcPriceVal = $(smcPrice).val();
		var smcUpdateDatetime = $(this).find(".smcUpdateDatetime").val();
		if(isNotNull(smcNameVal)){
			--mark;
		}
		if(isNotNull(smcInstVal)){
			--mark;
		}
		if(isNotNull(smcPriceVal)){
			--mark;
		}
		if(mark<3){
			if(!Jq_ValidateEmptyField($(smcName), frm.lab_smc_name.value)){
				$(smcName).focus();
				course_flag = false;
				return false ;
			}
			if(!Jq_ValidateEmptyField($(smcInst), frm.lab_smc_inst.value)){
				$(smcInst).focus();
				course_flag = false;
				return false;
			}
			if(!Jq_ValidateEmptyField($(smcPrice), frm.lab_smc_price.value)){
				$(smcPrice).focus();
				course_flag = false;
				return false;
			}
			if(!isDouble(smcPriceVal)){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_smc_price.value + '\"')
				$(smcPrice).focus();
				course_flag = false;
				return false;
			}else{
				if(toDecimal2(smcPriceVal)){
					smcPriceVal = toDecimal2(smcPriceVal);
				}else{
					alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_smc_price.value + '\"')
					$(smcPrice).focus();
					course_flag = false;
					return false ;
				}	
			}
			html.push("{");
			html.push("smcName:'");html.push(smcNameVal+"',");
			html.push("smcInst:'");html.push(smcInstVal+"',");
			if(isDate(smcUpdateDatetime)){
				html.push("smcUpdateDatetime:'");html.push(smcUpdateDatetime+"',");
			}
			html.push("smcPrice:'");html.push(smcPriceVal+"'}");		//注意有大括号
			html.push(",");	
		}

	});
	if(html.length > 0){
		html.pop();
		html.unshift("[");
		html.push("]");
	}
	//console.log(html.join(''));
	//article_flag = false;
	//this.article_flag  = true;
	//alert(this.article_flag +"c");
	frm.smcString.value = html.join('');
	return course_flag;
}

function get_proj_list(frm,lang){
	var proj_tb = $("#spl_project_table");
	var html = [];
	var proj_flag = true;
	//alert(proj_tb.find('.proj_tr').length);
	proj_tb.find('.proj_tr').each(function(i){
		//var sceStartDate = $(this).find("input[name=sceStartDate]").val();
		//var sceEndDate = $(this).find("input[name=sceEndDate]").val();
		var startDate_obj = $(this).find(".sceStartDate:eq(0)");
		var startDate_yy = $(startDate_obj).val();
		var startDate_mm = $(this).find(".sceStartDate:eq(1)").val();
		var startDate_dd = $(this).find(".sceStartDate:eq(2)").val();
		if(startDate_mm){
			if(startDate_mm.length<2){
				startDate_mm = '0'+startDate_mm;
			}
		}		
		if(startDate_dd){
			if(startDate_dd.length<2){
				startDate_dd = '0'+startDate_dd;
			}
		}
		//alert(startDate_yy);
		//alert(startDate_mm);
		//alert(startDate_dd);
		var endDate_obj = $(this).find(".sceEndDate:eq(0)");
		var endDate_yy = $(endDate_obj).val();
		var endDate_mm = $(this).find(".sceEndDate:eq(1)").val();
		var endDate_dd = $(this).find(".sceEndDate:eq(2)").val();
		if(endDate_mm){
			if(endDate_mm.length<2){
				endDate_mm = '0'+endDate_mm;
			}
		}
		if(endDate_dd){
			if(endDate_dd.length<2){
				endDate_dd = '0'+endDate_dd;
			}
		}
		var sceStartDate = startDate_yy+'-'+startDate_mm+'-'+startDate_dd;
		var sceEndDate = endDate_yy+'-'+endDate_mm+'-'+endDate_dd;
		
		var nullDate = '--';
		
		var mark = 5;
		//alert(sceStartDate);
			
		var sceItmName = $(this).find(".sceItmName");	
		
		var sceItmNameVal = $(this).find(".sceItmName").val();
		var sceDescVal = $(this).find(".sceDesc").val();
		var sceDptVal = $(this).find(".sceDpt").val();
		var sceUpdateDatetime = $(this).find(".sceUpdateDatetime").val();
		//alert(sceStartDate);
		if(sceStartDate != nullDate){
			--mark;
		}
		if(sceEndDate != nullDate){
			--mark;
		}
		if(isNotNull(sceItmNameVal)){
			--mark;
		}
		if(isNotNull(sceDescVal)){
			--mark;
		}
		if(isNotNull(sceDptVal)){
			--mark;
		}
		//alert(mark);
		if(mark<5){
			
			if(!Jq_ValidateEmptyField($(sceItmName), frm.lab_sce_itm_name.value)){
				$(sceItmName).focus();
				proj_flag = false;
				return false; ;
			}
			
			if(sceStartDate != nullDate && !isDate(sceStartDate)){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_sce_startdate.value + '\"')
				$(startDate_obj).focus();
				proj_flag = false;
				return false; ;
			}
			//alert(sceEndDate+i);
			if(sceEndDate != nullDate && !isDate(sceEndDate)){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_sce_enddate.value + '\"')
				$(endDate_obj).focus();
				proj_flag = false;
				return false; ;
			}
			if(compareDate(sceStartDate,sceEndDate)>0){
				alert(eval('wb_msg_start_date_before_end_date') );
				$(endDate_yy).focus();
				proj_flag = false;
				return false;;
			}
			html.push("{");
			if(sceStartDate == nullDate){
				sceStartDate = "";
			}else{
				sceStartDate += ' 00:00:00';
				//alert(sceStartDate);
				html.push("sceStartDate:'");html.push(sceStartDate+"',");
			}
			if(sceEndDate == nullDate){
				sceEndDate = "";
			}else{
				sceEndDate += ' 00:00:00';
				//alert(sceEndDate);

				html.push("sceEndDate:'");html.push(sceEndDate+"',")
			}
			html.push("sceItmName:'");html.push(sceItmNameVal+"',");
			html.push("sceDesc:'");html.push(sceDescVal+"',");
			if(isDate(sceUpdateDatetime)){
				html.push("sceUpdateDatetime:'");html.push(sceUpdateDatetime+"',");
			}
			html.push("sceDpt:'");html.push(sceDptVal+"'}");
			html.push(",");			
		}
	});
	if(html.length > 0){
		html.pop();
		html.unshift("[");
		html.push("]");
	}
	frm.sceString.value = html.join('');
	return proj_flag ;
}


function isNotNull(val){
	if(val!= null && val !='' && val.length>0 && val != undefined){
		return true;
	}
	return false;
}



function Jq_ValidateEmptyField(fld, txtFldName){
	var val = $(fld).val();
	//alert(val);
	//alert(new RegExp(/^\s+$/).test(val));
	if(val==undefined||val.length == 0 || new RegExp(/^\s+$/).test(val)){
		alert(wb_msg_usr_please_specify_value + '"' + txtFldName + '"' );
		if($(fld).is('textarea') || $(fld).is("input"))
			$(fld).focus();
		return false;
	}
	return true;
}

function article_type_maintain(){
	var url = wb_utils_invoke_disp_servlet('module', 'article.ArticleModule', 'cmd', 'get_article_type_maintain', 'stylesheet', 'article_type_maintain.xsl');
	window.location.href = url;
}

function article_type_prep(id){
	if(id == undefined)
	{
		id='';
	}
	var url = wb_utils_invoke_disp_servlet('module', 'article.ArticleModule', 'cmd', 'get_article_type_prep', 'stylesheet', 'article_type_ins_upd.xsl', 'aty_id', id, 'encryp_aty_id', id);
	window.location.href = url;
}

function article_type_insUpd(frm,lang){

	if(!wbUtilsValidateEmptyField(frm.aty_title, frm.lab_article_field_title.value)){
		frm.aty_title.focus();
		return;
	}
	
	if(frm.aty_title){
		if(getChars(frm.aty_title.value) > 20){
			Dialog.alert(fetchLabel("label_core_training_management_28") + " " + fetchLabel("label_core_training_management_381"),function(){
				frm.aty_title.focus();
			});
			return;
		}
	}
	//tcr_id
	if (frm.aty_tcr_id) {
	    if (frm.tcr_id.options[0].value == '') {
		    alert(wb_msg_pls_input_tcr);
	        return false;
	    } else {
		    frm.aty_tcr_id.value = frm.tcr_id.options[0].value;
		}
	}
	//return;
	frm.module.value = 'article.ArticleModule';	
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_type_maintain','stylesheet', 'article_type_maintain.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_type_prep','stylesheet', 'article_type_ins_upd.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function article_type_del(frm,lang){
	var atyID = [];
	$("input[name='aty_id']:checked").each(function(){
		atyID.push($(this).val());
	});
	if(atyID == ''){
		Dialog.alert(fetchLabel("label_core_information_management_51"));
		return;
	}
	if (confirm(eval('wb_msg_' + lang + '_confirm'))){
		frm.module.value = 'article.ArticleModule';
		frm.cmd.value = 'del_article_type';
		frm.aty_id_list.value = atyID.join(',');
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_type_maintain','stylesheet', 'article_type_maintain.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','article.ArticleModule','cmd', 'get_article_type_maintain','stylesheet', 'article_type_maintain.xsl');
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit();
	}
}