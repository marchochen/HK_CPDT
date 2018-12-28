var companyQQ = {
	delCpq : function (frm, lang){
		  var checked = false;
		  var cpq_id_ele_lst = document.getElementsByName('cpq_id_lst');
		  if(cpq_id_ele_lst !== undefined && cpq_id_ele_lst !== null && cpq_id_ele_lst.length > 0){
		      for(var n=0; n<cpq_id_ele_lst.length; n++){
		        if(cpq_id_ele_lst[n].checked === true){
		          checked = true;
		          break;
		        }
		      }
		  }
		  if(checked){
		     frm.module.value = 'JsonMod.qqConsultation.QQConsultationModule';
		     frm.cmd.value = 'operatingComnanyQQExe';
		     frm.method = 'post';
		     frm.action = wb_utils_disp_servlet_url;
		     frm.url_success.value = window.location.href;
		     frm.url_failure.value = window.location.href;
			 frm.submit();
		  }else{
		     alert(eval('wb_msg_select_qq_' + lang));
		  }
	},
	
	openOpratingQQPre : function(oprating, cpq_id){
		var url = wb_utils_invoke_disp_servlet('cmd', 'disOperatingPre', 'module', 'JsonMod.qqConsultation.QQConsultationModule', 
		          'stylesheet', 'opratingCompanyQQPre.xsl', 'operating', oprating, 'cpq_id', cpq_id);
		window.location.href = url;
	},
	
	opratingQQExe : function(oprating, frm, lang){
		if(_validate_company_QQ(frm, lang)){
			 frm.module.value = 'JsonMod.qqConsultation.QQConsultationModule';
		     frm.cmd.value = 'operatingComnanyQQExe';
		     frm.method = 'post';
		     frm.action = wb_utils_disp_servlet_url;
		     frm.url_success.value = wb_utils_invoke_disp_servlet('cmd', 'getCompanyQQLst', 'module', 
			 'JsonMod.qqConsultation.QQConsultationModule', 'stylesheet', 'companyQQList.xsl');
		     frm.url_failure.value = frm.url_success.value;
			 frm.submit();
		}
	},
	updateQQStatus : function(operating, frm){
		frm.module.value = 'JsonMod.qqConsultation.QQConsultationModule';
		frm.cmd.value = 'updateQQStatus';
		frm.operating.value = operating; 
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.url_success.value = wb_utils_invoke_disp_servlet('cmd', 'getCompanyQQLst', 'module', 
		'JsonMod.qqConsultation.QQConsultationModule', 'stylesheet', 'companyQQList.xsl');
		frm.url_failure.value = frm.url_success.value;
		frm.submit();
		
	}
}


function _validate_company_QQ(frm, lang){
	if (frm.cpq_code) {
		var cpq_code_value = frm.cpq_code.value;
        if (cpq_code_value === undefined || cpq_code_value === null || cpq_code_value === '') {
			alert(eval('wb_msg_' + lang + '_code_id'));
            return false;
        }
    }
	
	if (frm.cpq_title) {
		var cpq_title_value = frm.cpq_title.value;
        if (cpq_title_value === undefined || cpq_title_value === null || cpq_title_value === '') {
			alert(eval('wb_msg_vulgo_' + lang));
            return false;
        }
    }
	
	if (frm.cpq_number) {
		var cpq_number_value = frm.cpq_number.value;
        if (cpq_number_value === undefined || cpq_number_value === null || cpq_number_value === '') {
			alert(eval('wb_msg_qqNumber_' + lang));
            return false;
        }
    }
	
	if (frm.cpq_number) {
        if(isNaN(frm.cpq_number.value)) {
			alert(eval('wb_msg_qqNumberIsNotNumber_' + lang));
            return false;
        }
    }
	
	return true;
}