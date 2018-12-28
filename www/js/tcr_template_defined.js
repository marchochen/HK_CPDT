var Template_defined = {
	getTemplateModuleListFromTcr : function(tcr_id, n,  w, h, s){
	    var url = wb_utils_invoke_disp_servlet('cmd', 'get_tcr_template_module', 'module', 'JsonMod.tcrTemplate.TcrTemplateModule', 'stylesheet', 'tcr_template_module.xsl', 'tcr_id', tcr_id);
	    var sb = s == "1" ? "1" : "0";
	    var l = (screen.width - w) / 2;
	    var t = (screen.height - h) / 2;
	    
	    var sFeatures = "left=" + l + ",top=" + t + ",height=" + h + ",width=" + w + ",center=1,scrollbars=" + sb + ",status=0,directories=0,channelmode=0";
	    window.open(url, n, sFeatures);
	},
	
	openRevertDeletedModulePage : function(n, w, h, s, styleSheet){
		var url = wb_utils_invoke_disp_servlet('cmd', 'get_revert_deleted_module', 'module', 'JsonMod.tcrTemplate.TcrTemplateModule', 'stylesheet', styleSheet);
		var sb = s == "1" ? "1" : "0";
	    var l = (screen.width - w) / 2;
	    var t = (screen.height - h) / 2;
	   
	    var sFeatures = "left=" + l + ",top=" + t + ",height=" + h + ",width=" + w + ",center=1,scrollbars=" + sb + ",status=0,directories=0,channelmode=0";
		if(w == '0' && h == '0'){
			window.open(url, n, '');
		}else{
			window.open(url, n, sFeatures);
		}
    },
	
	saveTcrTemplate : function (frm, lang){
		frm.module.value = 'JsonMod.tcrTemplate.TcrTemplateModule';
        frm.cmd.value = 'save_tcr_template';
        frm.method = "post";
        frm.action = wb_utils_disp_servlet_url;
		frm.url_success.value = wb_utils_invoke_disp_servlet('cmd', 'closeWindows', 'module', 'JsonMod.tcrTemplate.TcrTemplateModule');
		frm.url_failure.value = frm.url_success.value;
		frm.submit();
	},
	
	openPreviewTemplate : function(n, w, h, s){
		var url = wb_utils_invoke_disp_servlet('cmd', 'dis_preView_tempalte_pre', 'module', 'JsonMod.tcrTemplate.TcrTemplateModule', 'stylesheet', 'preViewTemplateModule.xsl');
		var sb = s == "1" ? "1" : "0";
	    var l = (screen.width - w) / 2;
	    var t = (screen.height - h) / 2;
	   
	    var sFeatures = "left=" + l + ",top=" + t + ",height=" + h + ",width=" + w + ",center=1,scrollbars=" + sb + ",status=0,directories=0,channelmode=0";
		if(w == '0' && h == '0'){
			window.open(url, n, '');
		}else{
			window.open(url, n, sFeatures);
		}
	},
	
	preTcrTemplate : function(frm){
		frm.module.value = 'JsonMod.tcrTemplate.TcrTemplateModule';
        frm.cmd.value = 'preview_tcr_template';
        frm.method = "post";
        frm.action = wb_utils_disp_servlet_url;
		frm.submit();
	}
}
