

function wbSupplier() {
		this.get_supplier_prep = supplier_prep
		this.sort_supplier_list = supplier_sort
		this.search_supplier_list = supplier_search
		this.ins_upd_supplier = supplier_insUpd
		//this.supplier_del = supplier_del
		this.get_supplier_view = supplier_view
		this.supplier_del_id = supplier_del_id
		this.down_supplier_list = down_supplier_list
		this.get_supplier_list = get_supplier_list
		this.comm= new wbSupplierComment
};
function get_supplier_list(){
	var url = wb_utils_invoke_disp_servlet('module',
			'supplier.SupplierModule', 'cmd', 'get_supplier_list',
			'stylesheet', 'supplier_list.xsl');
	window.location.href = url;
}

function wbSupplierComment(){
	this.get_supplier_comment_view =wbSupplierCommentView
	this.supplier_comment_add =wbSupplierCommentAdd
	this.supplier_comment_submit =wbSupplierCommentSub
}

function supplier_prep(id) {
	var url = wb_utils_invoke_disp_servlet('module',
			'supplier.SupplierModule', 'cmd', 'get_supplier_prep',
			'stylesheet', 'supplier_ins_upd.xsl', 'splId', id);
	window.location.href = url;
}

function down_supplier_list(){
	var url = wb_utils_invoke_disp_servlet('module',
			'supplier.SupplierModule', 'cmd', 'down_supplier_list',
			'stylesheet', 'supplier_list.xsl');
	window.location.href = url;
}

function supplier_view(id){
	var url = wb_utils_invoke_disp_servlet('module',
			'supplier.SupplierModule', 'cmd', 'get_supplier_view',
			'stylesheet', 'supplier_view.xsl', 'splId', id);
	window.location.href = url;
}
function supplier_sort(frm, sort_col, sort_order) {
//    var spl_status = frm.splStatus.value;
//    var spl_name = frm.splName.value;
//    var sce_itm_name = frm.sceItmName.value;
//    var spl_course = frm.splCourse.value;
	var spl_status = "";
    var spl_name = "";
    var sce_itm_name = "";
    var spl_course = "";
	var url = wb_utils_invoke_disp_servlet('module',
			'supplier.SupplierModule', 'cmd', 'get_supplier_list',
			'stylesheet', "supplier_list.xsl", 'sort_col', sort_col, 'sort_order', sort_order,'splStatus',spl_status, 'splName',spl_name, 'sceItmName',sce_itm_name,'splCourse',spl_course);
	window.location.href = url;
}

function supplier_search(frm) {
	
    var spl_status = frm.splStatus.value;
    var spl_name = frm.splName.value;
    var sce_itm_name = frm.sceItmName.value;
    var spl_course = frm.splCourse.value;

	url = wb_utils_invoke_disp_servlet("module","supplier.SupplierModule","cmd", 'get_supplier_list', 'stylesheet', 'supplier_list.xsl', 'splStatus',spl_status, 'splName',spl_name, 'sceItmName',sce_itm_name,'splCourse',spl_course,'isSearch','1' );
	window.location.href = url;
	
}
function supplier_insUpd(frm, lang) {
	
//	if(!wbUtilsValidateEmptyField(frm.spl_established_date, frm.lab_spl_established_date.value)){
//		frm.spl_established_date.focus();
//		return;
//	}
	
	if(!wbUtilsValidateEmptyField(frm.splName, frm.lab_spl_name.value)){
		frm.splName.focus();
		return;
	}
	if(!wbUtilsValidateEmptyField(frm.splRepresentative, frm.lab_spl_representative.value)){
		frm.splRepresentative.focus();
		return;
	}
	if(frm.splEstablishedDate_yy && frm.splEstablishedDate_mm && frm.splEstablishedDate_dd){
	    if(!wbUtilsValidateDate("document." + frm.name + ".splEstablishedDate", frm.lab_spl_established_date.value)) {
	    	return;
		}
	    frm.splEstablishedDate.value = frm.splEstablishedDate_yy.value+"-"+frm.splEstablishedDate_mm.value+"-"+frm.splEstablishedDate_dd.value+' 00:00:00'
	}

	if(frm.splRegisteredCapital.value!=''&&frm.splRegisteredCapital.value!=undefined&&frm.splRegisteredCapital.value!=null){
		if(!isDouble(frm.splRegisteredCapital.value)){
			alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_spl_registered_capital.value + '\"')
			frm.splRegisteredCapital.focus();
			return;
		}else{
			//alert(toDecimal2(frm.splRegisteredCapital.value));
			if(toDecimal2(frm.splRegisteredCapital.value)){
				frm.splRegisteredCapital.value = toDecimal2(frm.splRegisteredCapital.value);
			}else{
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_spl_registered_capital.value + '\"')
				frm.splRegisteredCapital.focus();
				return;
			}
		}
	}
/*	if(!wbUtilsValidateEmptyField(frm.splRegisteredCapital, frm.lab_spl_registered_capital.value)){
		frm.splRegisteredCapital.focus();
		return;
	}*/
	if(!wbUtilsValidateEmptyField(frm.splType, frm.lab_spl_type.value)){
		frm.splType.focus();
		return;
	}
	if(!wbUtilsValidateEmptyField(frm.splAddress, frm.lab_spl_address.value)){
		frm.splAddress.focus();
		return;
	}
	if(!wbUtilsValidateEmptyField(frm.splContact, frm.lab_spl_contact.value)){
		frm.splContact.focus();
		return;
	}
	if(!wbUtilsValidateEmptyField(frm.splTel, frm.lab_spl_tel.value)){
		frm.splTel.focus();
		return;
	}	
	if(!wbUtilsValidateEmptyField(frm.splMobile, frm.lab_spl_mobile.value)){
		frm.splMobile.focus();
		return;
	}
	//email
	if(!wbUtilsValidateEmptyField(frm.splEmail, frm.lab_spl_email.value)){
		frm.splEmail.focus();
		return;
	}
	if(!isEmail(frm.splEmail.value)){
		alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_spl_email.value + '\"')
		frm.splEmail.focus()
		return;
	}
	//机构人数
	if(!wbUtilsValidateEmptyField(frm.splTotalStaff, frm.lab_spl_total_staff.value)){
		frm.splTotalStaff.focus()
		return;
	}
	if(!isGtEqZero(frm.splTotalStaff.value)){
		alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_spl_total_staff.value + '\"')
		frm.splTotalStaff.focus();
		return;
	}
	if(!wbUtilsValidateEmptyField(frm.splFullTimeInst, frm.lab_spl_full_time_inst.value)){
		frm.splFullTimeInst.focus()
		return;
	}	
	if(!isGtEqZero(frm.splFullTimeInst.value)){
		alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_spl_full_time_inst.value + '\"')
		frm.splFullTimeInst.focus()
		return ;
	}
	if(!wbUtilsValidateEmptyField(frm.splPartTimeInst, frm.lab_spl_part_time_inst.value)){
		frm.splPartTimeInst.focus()
		return;
	}
	if(!isGtEqZero(frm.splPartTimeInst.value)){
		alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_spl_part_time_inst.value + '\"')
		frm.splPartTimeInst.focus()
		return;
	}
	
	if(!wbUtilsValidateEmptyField(frm.splExpertise, frm.lab_spl_expertise.value)){
		frm.splExpertise.focus()
		return;
	}	
	if(!wbUtilsValidateEmptyField(frm.splCourse, frm.lab_spl_course.value)){
		frm.splCourse.focus()
		return;
	}
	var fileFlag = true; 
	//file  思路：如果选了delete或者是original（原来）的话，把之前的input name改掉，然后新增一个隐藏框，由此判断是否删除，保留
	for ( var i = 1; i <= 10; i++) {
		var ckVal = $("input[name=r_splAttachment" + i + "]:checked").val();
		//alert(ckVal);
		if(ckVal == 'delete'){
			$("input[name=splAttachment" + i + "]").attr("name","rmSplAttachment"+i);
			$("input[name=rmSplAttachment" + i + "]").after("<input type=\"hidden\" name=\"splAttachment"+i+"\" value =\"delete\" />");
		}else if(ckVal == 'original'){
			$("input[name=splAttachment" + i + "]").attr("name","rmSplAttachment"+i);
			$("input[name=rmSplAttachment" + i + "]").after("<input type=\"hidden\" name=\"splAttachment"+i+"\" value =\"original\" />");
		}
		
/*		var filename = $("input[name=splAttachment"+i+"]").val();
		if(isNotNull(filename)){
			if(!wb_utils_check_chinese_char(filename)){
				fileFlag = false;
				return;
			}
		}else{
			continue;
		}*/
	}
/*	if(!fileFlag){
		return;
	}*/
	//return;
	//String sce = 
	var pflag = get_proj_list(frm,lang);
	//alert(pflag+"   p");
	if(!pflag){
		return;
	}
	var cflag = get_course_list(frm,lang);
	//alert(cflag);
	if(!cflag){
		return;
	}
	//frm.sceString.value = proj_html;
	//frm.smcString.value = course_html;
	//alert($("#spl_course_table").find('.course_tr').length);
	//alert($("#spl_project_table").find('.proj_tr').length);
	//alert(frm.sceString.value);
	//alert(frm.smcString.value);

	//return;
	frm.module.value = 'supplier.SupplierModule';	
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','supplier.SupplierModule','cmd', 'get_supplier_list','stylesheet', 'supplier_list.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','supplier.SupplierModule','cmd', 'get_supplier_list','stylesheet', 'supplier_list.xsl');
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function supplier_del_id(frm){
	if(frm.splId.value == ""){
		alert(eval('wb_msg_sel_del_supplier'));
	}else if(confirm(eval('wb_msg_del_supplier_confirm'))){
		frm.module.value = 'supplier.SupplierModule';
		frm.cmd.value = 'supplier_del_id';
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','supplier.SupplierModule','cmd', 'get_supplier_list','stylesheet', 'supplier_list.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','supplier.SupplierModule','cmd', 'get_supplier_list','stylesheet', 'supplier_list.xsl');
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

function wbSupplierCommentView(spl_id){
	var  url= wb_utils_invoke_disp_servlet('module','supplier.SupplierModule','cmd', 'get_supplierr_comment_list','spl_id',spl_id,'stylesheet', 'suppliercomment_list.xsl');
	self.location.href = url;
}

function wbSupplierCommentAdd(spl_id,ent_id){
	url = wb_utils_invoke_disp_servlet('module', 'supplier.SupplierModule', 'cmd', 'supplierr_comment_add_prep','spl_id',spl_id,'scm_ent_id',ent_id, 'stylesheet', 'suppliercomment_add.xsl');
	self.location.href = url;
}

function wbSupplierCommentSub(frm){
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
	frm.module.value = 'supplier.SupplierModule';
	if(frm.up_scm_ent_id && frm.up_scm_ent_id.value >0){
		frm.cmd.value = 'supplierr_comment_up';
	}else	
		frm.cmd.value = 'supplierr_comment_add';
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','supplier.SupplierModule','cmd', 'get_supplierr_comment_list','spl_id',frm.spl_id.value,'stylesheet', 'suppliercomment_list.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','supplier.SupplierModule','cmd', 'get_supplierr_comment_list','spl_id',frm.spl_id.value,'stylesheet', 'suppliercomment_list.xsl');
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
	//supplier_flag = false;
	//this.supplier_flag  = true;
	//alert(this.supplier_flag +"c");
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
			
			//项目名不填仍然可以提交
            /*if(!Jq_ValidateEmptyField($(sceItmName), frm.lab_sce_itm_name.value)){

				$(sceItmName).focus();
				proj_flag = false;
				return true; ;
            }*/

			
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

function add_proj_input(){
	var proj_tb = $("#spl_project_table");
	var startCount = "_s"+proj_tb.find('.proj_tr').length;
	var endCount = "_e"+proj_tb.find('.proj_tr').length;
	var inputCount = proj_tb.find('.proj_tr').length;
	//alert(startCount);
	var html = '<tr class="proj_tr">'
			 +'<td valign="top" align="left" style="padding:10px 0 0 0;">'
			 +'<input value="" name="sceStartDate'+startCount+'" type="hidden">'
			 +'<input type="text" name="sceStartDate'+startCount+'_yy" maxlength="4" size="4" class="wzb-inputText" value="" onkeyup="javascript:auto_focus_field(document.frmXml.sceStartDate'+startCount+'_yy,4,document.frmXml.sceStartDate'+startCount+'_mm)">'
			 +'-<input type="text" name="sceStartDate'+startCount+'_mm" maxlength="2" size="2" class="wzb-inputText" value="" onkeyup="javascript:auto_focus_field(document.frmXml.sceStartDate'+startCount+'_mm,2,document.frmXml.sceStartDate'+startCount+'_dd)">-'
			 +'<input type="text" name="sceStartDate'+startCount+'_dd" maxlength="2" size="2" class="wzb-inputText" value="" onkeyup="javascript:auto_focus_field(document.frmXml.sceStartDate'+startCount+'_dd,2,document.frmXml.sceStartDate'+startCount+'_hour)"> '
		html += ' ' + wb_time_format;
	    html += '&nbsp;<script src="../js/date-picker.js" language="JavaScript"></script>'
			 +"<a onmouseout=\"window.status='';return true;\" onmouseover=\"window.status='Date Picker';return true;\" href=\"javascript:show_calendar('document.frmXml.sceStartDate"+startCount+"', '','','','"
		html += wb_cur_lan;
		html += "','../cw/skin4/images/gb/css/wb_ui.css');\"><img align=\"absmiddle\" border=\"0\" src=\"../wb_image/btn_calendar.gif\"></a>&nbsp;"
			 +"</td><td valign=\"top\" align=\"left\" style=\"padding:10px 0 0 0;\">"
			 +"<input value=\"\" name=\"sceEndDate"+endCount+"\" type=\"hidden\">"
			 +'<input type="text" name="sceEndDate'+endCount+'_yy" maxlength="4" size="4" class="wzb-inputText" value="" onkeyup="javascript:auto_focus_field(document.frmXml.sceStartDate'+endCount+'_yy,4,document.frmXml.sceStartDate'+endCount+'_mm)">'
			 +'-<input type="text" name="sceEndDate'+endCount+'_mm" maxlength="2" size="2" class="wzb-inputText" value="" onkeyup="javascript:auto_focus_field(document.frmXml.sceStartDate'+endCount+'_mm,2,document.frmXml.sceStartDate'+endCount+'_dd)">'
			 +'-<input type="text" name="sceEndDate'+endCount+'_dd" maxlength="2" size="2" class="wzb-inputText" value="" onkeyup="javascript:auto_focus_field(document.frmXml.sceStartDate'+endCount+'_dd,2,document.frmXml.sceStartDate'+endCount+'_hour)">'
		html += ' ' + wb_time_format;
		html +='&nbsp;<script src="../js/date-picker.js" language="JavaScript"></script>'
			 +"<a onmouseout=\"window.status='';return true;\" onmouseover=\"window.status='Date Picker';return true;\" href=\"javascript:show_calendar('document.frmXml.sceEndDate"+endCount+"', '','','','"
		html += wb_cur_lan;
		html +="','../cw/skin4/images/gb/css/wb_ui.css');\"><img align=\"absmiddle\" border=\"0\" src=\"../wb_image/btn_calendar.gif\"></a>&nbsp;</td><td valign=\"top\" align=\"center\" style=\"padding:10px 0 0 5px;\">"
			 +"<input class=\"wzb-inputText\" value=\"\" name=\"sceItmName"
			 +inputCount+"\" maxlength=\"30\" style=\"width:135px;\" type=\"Text\"></td><td valign=\"top\" align=\"center\" style=\"padding:10px 0 0 5px;\"><input class=\"wzb-inputText\" value=\"\" name=\"sceDesc"
			 +inputCount+"\" maxlength=\"200\" style=\"width:135px;\" type=\"Text\"></td><td valign=\"top\" align=\"center\" style=\"padding:10px 0 0 5px;\"><input class=\"wzb-inputText\"  name='sceDpt"
			 +inputCount+"' maxlength='30' style='width:135px;' type=\"Text\"></td>"
			 +"<td align='right' style='padding:10px 0 0 0;' valign='top' ><label style='display:none;visibility:hidden;'>[</label><a onclick='del_proj_input(this);' href='javascript:;' class='btn wzb-btn-blue'>";
		html += wb_btn_delete;
		html +="</a><label style='display:none;visibility:hidden;'>]</label></td></tr>";
 
	proj_tb.append(html);	
	if(proj_tb.find('.proj_tr').length > 0){
		$("#spl_tr_title").remove();
		$("#spl_tr_img").remove();
	}
}
function isNotNull(val){
	if(val!= null && val !='' && val.length>0 && val != undefined){
		return true;
	}
	return false;
}
//delete added input
function del_proj_input(obj){
	var parent_tr = $(obj).parent().parent('.proj_tr');
	parent_tr.remove();
	none_title();
}

function none_title(){
	var proj_tb = $("#spl_project_table");
	if(proj_tb.find('.proj_tr').length <= 0){
		proj_tb.append("<tr id='spl_tr_img'><td height='30' colspan='6' align='center'><div class='losedata' style='margin-top:50px'><i class='fa fa-folder-open-o'></i></div></td></tr><tr id='spl_tr_title'><td height='30' colspan='6' align='center'><span class='Text'>"+wb_msg_show_experience+"</span></td></tr>");
	}	
}

function course_none_title(){
	var proj_tb = $("#spl_course_table");
	if(proj_tb.find('.course_tr').length <= 0){
		proj_tb.append("<tr id='cou_tr_img'><td height='30' colspan='6' align='center'><div class='losedata' style='margin-top:50px'><i class='fa fa-folder-open-o'></i></div></td></tr><tr id='course_tr_title'><td height='30' colspan='6' align='center'><span class='Text'>"+wb_msg_show_course+"</span></td></tr>");
	}	
}

function del_course_input(obj){
	var parent_tr = $(obj).parent().parent('.course_tr');
	parent_tr.remove();
	course_none_title();
}

function add_course_input(){
	var course_tb = $("#spl_course_table");
	var course_tr = $("#spl_course_table").find(".course_tr");
	var inputCount = course_tr.length;
	var html = '<tr class="course_tr"><td align="left"  valign="top" style="padding:10px 0 0 0;"><input type="Text" style="width:135px;" maxlength="50" name="smcName'
		+inputCount+'" value="" class="wzb-inputText" maxlength="50"/></td><td  align="left" valign="top" style="padding:10px 0 0 0;"><input type="Text" style="width:135px;" maxlength="50" name="smcInst'
		+inputCount+'" value="" class="wzb-inputText" maxlength="30"/></td><td align="left" valign="top" style="padding:10px 0 0 0;"><input type="Text" style="width:135px;" maxlength="50" name="smcPrice'
		+inputCount+'" value="" class="wzb-inputText" /></td><td  align="right" style="padding:10px 0 0 0;" valign="top"><label style="display:none;visibility:hidden;">[</label><a class="btn wzb-btn-blue" href="javascript:;" onclick="del_course_input(this);">'
		html += wb_btn_delete;
		html += '</a><label style="display:none;visibility:hidden;">]</label></td></tr>';
	course_tb.append(html);	
	if($("#spl_course_table").find(".course_tr").length > 0){
		$("#course_tr_title").remove();
		$("#cou_tr_img").remove();
	}
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