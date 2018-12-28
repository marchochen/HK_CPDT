var DefinedProject = {
	get_defined_project : function(tcr_id, sort_col, sort_order){
		sort_col = (sort_col === undefined || sort_col === null || sort_col === '') ? 'dpt_code' : sort_col;
		sort_order = (sort_order === undefined || sort_order === null || sort_order === '') ? 'asc' : sort_order;
		var url = wb_utils_invoke_disp_servlet('cmd', 'get_project_lst', 'module', 'JsonMod.definedProject.DefinedProjectModule', 
		          'stylesheet', 'defined_project_lst.xsl', 'tcr_id', tcr_id, 'sort_col', sort_col, 'sort_order', sort_order);
		window.location.href = url;
	},
	
	dis_oprating_project_pre : function(tcr_id, oprating, dpt_id){
		var url = wb_utils_invoke_disp_servlet('cmd', 'dis_oprating_pre', 'module', 'JsonMod.definedProject.DefinedProjectModule', 
		          'stylesheet', 'oprating_defi_pro_pre.xsl', 'tcr_id', tcr_id, 'oprating', oprating, 'dpt_id', dpt_id);
		window.location.href = url;
	},
	
	oprating_project_exe : function(frm, lang){
		if(_validate_def_project(frm, lang)){
			frm.module.value = 'JsonMod.definedProject.DefinedProjectModule';
	        frm.cmd.value = "oprating_defined_project_exe";
	        frm.method = "post";
	        frm.action = wb_utils_disp_servlet_url;
	        frm.url_success.value = wb_utils_invoke_disp_servlet('cmd', 'get_project_lst', 'module', 'JsonMod.definedProject.DefinedProjectModule', 
		          'stylesheet', 'defined_project_lst.xsl', 'tcr_id', frm.tcr_id_lst.options[0].value, 'sort_col', 'dpt_code', 'sort_order', 'asc');
	        frm.url_failure.value = window.location.href;
			frm.tcr_id.value = frm.tcr_id_lst.options[0].value;
			frm.submit();
		}
	},
	
	delete_defined_project : function(frm, dpt_id, lang){
		if(confirm(eval('ae_msg_'+lang+'_confirm'))){
			frm.module.value = 'JsonMod.definedProject.DefinedProjectModule';
	        frm.cmd.value = "oprating_defined_project_exe";
	        frm.method = "post";
	        frm.action = wb_utils_disp_servlet_url;
			frm.url_success.value = window.location.href;
			frm.url_failure.value = window.location.href;
			frm.dpt_id.value = dpt_id;
			frm.submit();
		}
	},
	
	get_project_link_lst : function (tcr_id, dpt_id){
		var url = wb_utils_invoke_disp_servlet('cmd', 'get_project_link_lst', 'module', 'JsonMod.definedProject.DefinedProjectModule', 
		          'stylesheet', 'project_link_lst.xsl', 'tcr_id', tcr_id, 'dpt_id', dpt_id);
		window.location.href = url;
	},
	
	dis_oprating_link_pre : function(oprating, tcr_id, dpt_id, pjl_id){
		var url = wb_utils_invoke_disp_servlet('cmd', 'dis_oprating_project_link_pre', 'module', 'JsonMod.definedProject.DefinedProjectModule', 
		          'stylesheet', 'oprating_link_pre.xsl', 'oprating', oprating, 'tcr_id', tcr_id, 'dpt_id', dpt_id, 'pjl_id', pjl_id);
		window.location.href = url;
	},
	
	oprating_link_exe : function(frm, lang){
		if(_validate_def_project(frm, lang)){
			frm.module.value = 'JsonMod.definedProject.DefinedProjectModule';
	        frm.cmd.value = "oprating_project_link_exe";
	        frm.method = "post";
	        frm.action = wb_utils_disp_servlet_url;
	        frm.url_success.value = wb_utils_invoke_disp_servlet('cmd', 'get_project_link_lst', 'module', 'JsonMod.definedProject.DefinedProjectModule', 
		          'stylesheet', 'project_link_lst.xsl', 'tcr_id', frm.tcr_id.value, 'dpt_id', frm.dpt_id.value);
	        frm.url_failure.value = window.location.href;
			frm.submit();
		}
	},
	
	delete_link_exe : function(frm, pjl_id, lang){
		if(confirm(eval('ae_msg_'+lang+'_confirm'))){
			frm.module.value = 'JsonMod.definedProject.DefinedProjectModule';
	        frm.cmd.value = "oprating_project_link_exe";
	        frm.method = "post";
	        frm.action = wb_utils_disp_servlet_url;
			frm.url_success.value = window.location.href;
			frm.url_failure.value = window.location.href;
			frm.pjl_id.value = pjl_id;
			frm.submit();
		}
	}
	
}









function _validate_def_project(frm, lang){
	if (frm.dpt_code) {
		var dpt_code_value = frm.dpt_code.value;
        if (dpt_code_value === undefined || dpt_code_value === null || dpt_code_value === '') {
			alert(eval('wb_msg_' + lang + '_code_id'));
            return false;
        }
    }
	
	if (frm.dpt_title) {
		var dpt_title_value = frm.dpt_title.value;
        if (dpt_title_value === undefined || dpt_title_value === null || dpt_title_value === '') {
			alert(wb_msg_ils_name_empty);
            return false;
        }
    }
	
	if (frm.pjl_code) {
		var pjl_code_value = frm.pjl_code.value;
        if (pjl_code_value === undefined || pjl_code_value === null || pjl_code_value === '') {
			alert(eval('wb_msg_' + lang + '_code_id'));
            return false;
        }
    }
	
	if (frm.pjl_title) {
		var pjl_title_value = frm.pjl_title.value;
        if (pjl_title_value === undefined || pjl_title_value === null || pjl_title_value === '') {
			alert(wb_msg_ils_name_empty);
            return false;
        }
    }
	
	if (frm.pjl_url) {
		var pjl_url_value = frm.pjl_url.value;
        if (pjl_url_value === undefined || pjl_url_value === null || pjl_url_value === '' || pjl_url_value.indexOf('http://') !== 0 || pjl_url_value.length < 8) {
			alert(eval('wb_msg_rigth_url_' + lang));
            return false;
        }
    }
	
	return true;
}
