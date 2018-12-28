// ------------------ WizBank Code_Data Object ---------------------
// Convention:
//   public functions : use "wbCodeData" prefix 
//   private functions: use "_wbCodeData" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js	 
// ------------------------------------------------------------ 
/* constructor */
function wbCodeData(){	
	this.ins_code_data_prep = wbCodeDataInsertCodeDataPrep;
	this.ins_code_data_exec = wbCodeDataInsertCodeDataExec;
	this.upd_code_data_prep = wbCodeDataUpdateCodeDataPrep;
	this.upd_code_data_exec = wbCodeDataUpdateCodeDataExec;
	this.del_code_data = wbCodeDataDeleteCodeData;
	this.get_code_data_lst = wbCodeDataGetCodeData;
	this.get_code_data_detail = wbCodeDataGetDetail	;
}
// ------------------------------------------------------------ 
function wbCodeDataGetCodeData(code_data_type){
	url = wbCodeDataGetCodeDataUrl(code_data_type)
	parent.location.href = url;
}
function wbCodeDataInsertCodeDataPrep(ctb_type){
	url = wb_utils_invoke_disp_servlet(
		'module','codetable.CodeDataModule',
		'ctb_type',ctb_type,
		'cmd','ins_code_data_pre',
		'stylesheet','ins_code_data_pre.xsl'
	)
	parent.location.href = url;
}

function wbCodeDataInsertCodeDataExec(frm,lang,ctb_type){		
	if(_validateForm(frm,lang)){
		frm.method = "post";
		frm.action = wb_utils_disp_servlet_url
		frm.url_success.value = wb_utils_invoke_disp_servlet(
		'module','codetable.CodeDataModule',
		'ctb_type',ctb_type,
		'cmd','get_code_data_lst',
		'stylesheet','get_code_data.xsl'
		)
		frm.url_failure.value = parent.location.href;
		frm.submit();	
	}
}

function wbCodeDataUpdateCodeDataPrep(ctb_type,ctb_id,ctb_title){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_code_data_detail',
		'module','codetable.CodeDataModule',
		'ctb_type',ctb_type,'ctb_id',ctb_id,
		'stylesheet','upd_code_data.xsl',
		'url_failure',wbCodeDataGetCodeDataUrl(ctb_type)
		)
	parent.location.href = url;
}

function wbCodeDataUpdateCodeDataExec(frm,lang,ctb_id,ctb_type,code_data_id,code_data_type){
	
	if(_validateForm(frm,lang)){
		frm.url_failure.value = wbCodeDataGetCodeDataUrl(ctb_type);		
//		frm.url_success.value = wb_utils_invoke_disp_servlet(
//		'module','codetable.CodeDataModule',
//		'ctb_type',ctb_type,
//		'cmd','get_code_data_lst',
//		'stylesheet','get_code_data.xsl'
//		)
		frm.url_success.value = wb_utils_invoke_disp_servlet(
		'module','codetable.CodeDataModule',
		'ctb_type',ctb_type,
		'cmd','get_code_data_lst',
		'stylesheet','get_code_data.xsl'
		)

		//frm.url_failure.value = parent.location.href;
		frm.method = "post"			
		frm.action = wb_utils_disp_servlet_url
		frm.submit()
	}
}

function wbCodeDataDeleteCodeData(ctb_type,ctb_id,lang){
	
	if(!confirm(eval('wb_msg_' + lang + '_confirm'))) {return;}

	url = wb_utils_invoke_disp_servlet(
		'cmd','del_code_data',
		'module','codetable.CodeDataModule',
		'ctb_id',ctb_id,'ctb_type',ctb_type,
		'url_success', wbCodeDataGetCodeDataUrl(ctb_type),
		'url_failure', wbCodeDataGetCodeDataUrl(ctb_type)
	)
	parent.location.href = url;
}

function wbCodeDataGetCodeDataUrl(code_data_type){
	url = wb_utils_invoke_disp_servlet(
		'module','codetable.CodeDataModule',
		'ctb_type',code_data_type,
		'cmd','get_code_data_lst',
		'stylesheet','get_code_data.xsl'
	)
	return url;
}

function wbCodeDataGetDetail(code_data_id,code_data_type){
	url = wb_utils_invoke_disp_servlet(
		'module','codetable.CodeDataModule',
		'ctb_id',code_data_id,'ctb_type',code_data_type,
		'cmd','get_code_data_detail',
		'stylesheet','get_code_data_detail.xsl'
		)
	parent.location.href = url;
}

// validate function ----------------------------------------------- 
function _validateForm(frm,lang){
	if(frm.ctb_id){
		frm.ctb_id.value = wbUtilsTrimString(frm.ctb_id.value);
		if (!gen_validate_empty_field(frm.ctb_id, eval('wb_msg_'+ lang + '_code'),lang)){
			return false;
		}
	}
	return true;
}