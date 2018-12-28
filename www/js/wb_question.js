// ------------------ wizBank Question object ------------------- 
// Convention:
//   public functions : use "wbQuestion" prefix 
//   private functions: use "_wbQuestion" prefix
// ------------------------------------------------------------ 

/* constructor */
function wbQuestion() {	
	this.get = wbQuestionGet
	this.read = wbQuestionRead							// read in search result
	this.add_que_prep = wbQuestionAddQuePrep
	this.add_evn_que_prep = wbEvnQuestionAddQuePrep
	this.del = wbQuestionDelete
	this.del_in_search = wbQuestionDeleteInSearch
	this.edit = wbQuestionEdit
	this.edit_in_search = wbQuestionEditInSearch
	this.paste = wbQuestionPaste
	this.copy = wbQuestionCopy
	this.imp_evn_que_prep = wbQuestionImpPrep
	
	// for question that added in module
	this.edit_in_module = wbQuestionEditInModule
	this.del_in_module = wbQuestionDelInModule
	this.read_in_module = wbQuestionReadInModule
	this.read_in_select_que = wbQuestionReadInSelectQuesiton
	

}

/* public functions */
function wbQuestionGet(que_id){
	var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','que_get.xsl');
	window.location.href = url;
}

function wbQuestionRead(que_id){
	var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','res_srh_que_ind.xsl');
	window.location.href = url;
}

function wbQuestionAddQuePrep(que_obj_id,que_type,mod_type,is_add_res){
	if(is_add_res==null || is_add_res==''){
		is_add_res = false;
	}
	var url = wb_utils_invoke_servlet('cmd','get_que_frm','que_obj_id',que_obj_id,'que_type',que_type,'stylesheet','que_ins.xsl',"mod_type",mod_type,'is_add_res', is_add_res);
	window.location.href = url;
}

function wbEvnQuestionAddQuePrep(que_obj_id,que_type,mod_type,course_id){
	var url_failure = wb_utils_invoke_servlet('cmd','get_mod','mod_id',que_obj_id,'dpo_view','IST_READ','stylesheet','cos_evn_info_content.xsl');
	var url = wb_utils_invoke_servlet('cmd','get_que_frm','que_obj_id',que_obj_id,'que_type',que_type,'stylesheet','cm_que_get_ins.xsl',"mod_type",mod_type,'url_failure',url_failure);
	window.parent.content.location.href = url;
}

function wbQuestionDelete(que_id,timestamp,lang, obj_id){
		if (confirm(eval('wb_msg_' + lang + '_confirm'))){				
			var url = wb_utils_invoke_servlet('cmd','del_obj_res','res_id_lst',que_id,'obj_id', obj_id, 'que_timestamp',timestamp,'url_success','javascript:parent.location.reload()','url_failure','javascript:parent.location.reload()');
			window.parent.content.location.href = url;
		}
}

function wbQuestionDeleteInSearch(res_id,obj_id,lang){			
		if (confirm(eval('wb_msg_' + lang + '_confirm'))){	
			var url = wb_utils_invoke_servlet('cmd','del_obj_res','res_id_lst',res_id,'obj_id',obj_id,'url_success',wb_utils_get_cookie('search_result_url'),'url_failure',wb_utils_get_cookie('search_result_url'))
			//var url = wb_utils_invoke_servlet('cmd','del_q','que_id',que_id,'que_timestamp',timestamp,'url_success',wb_utils_adm_syb_lst_url(),'url_failure',wb_utils_get_cookie('search_result_url'));
			window.location.href = url;
		}	
}

function wbQuestionEdit(que_id){		
		var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','que_upd.xsl');
		window.location.href = url;
}

function wbQuestionEditInSearch(que_id){
		wb_utils_set_cookie('url_prev',self.location.href)
		var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','res_srh_que_upd.xsl');
		window.location.href = url;
}
		
function wbQuestionPaste(que_obj_id,lang){	
		que_id = wb_utils_get_cookie('res_id_copy');
		if (que_id > 0)  {
			var url = wb_utils_invoke_servlet('cmd','get_q','stylesheet','que_pst.xsl','que_id',que_id,'que_obj_id',que_obj_id);
			window.parent.content.location.href = url;
		}else {
			alert(eval('wb_msg_' + lang + '_copy_que_first'));
		}
}


function wbQuestionCopy(que_id,lang){
		wb_utils_set_cookie('res_id_copy',que_id);
		wb_utils_set_cookie('res_type_copy','QUE');
		alert(eval('wb_msg_' + lang + '_copy_que'));

}
function wbQuestionEditInModule(que_id,lang){

		mod_status = wb_utils_get_cookie('mod_status')
		if (mod_status == 'ON') {
			alert(eval('wb_msg_' + lang + '_change_offline'))
			return;
		}
		mod_type = getUrlParam("mod_type")
		if(mod_type==null||mod_type=="")
			var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','cm_que_upd.xsl','upd_tst_que',true,'url_failure',window.parent.content.location.href)
		else
			var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','cm_que_upd.xsl','upd_tst_que',true,"mod_type",mod_type,'url_failure',window.parent.content.location.href)
		window.parent.content.location.href=url
}

function wbQuestionDelInModule(que_id,lang){

		var mod_status = wb_utils_get_cookie('mod_status')
		if (mod_status == 'ON') {
			alert(eval('wb_msg_' + lang + '_change_offline'))
			return;
		}
		
		if (confirm(eval('wb_msg_' + lang + '_confirm'))) {	
			
			var mod_subtype = parent.get_mod_type();
			if(mod_subtype == 'FAS' || mod_subtype == 'FSC' || mod_subtype == 'DSC'){
				url_success = window.parent.location.href
				url_failure = window.parent.location.href		
				res_id = parent.get_mod_id()
				res_timestamp = parent.get_mod_timestamp();
				url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','del_que','res_id',res_id,'que_id_lst',que_id,'res_timestamp',res_timestamp,'url_success',url_success, 'url_failure',url_failure);		
				window.parent.location.href = url;
			}else{			
				var mod_id = parent.get_mod_id()	
				var mod_timestamp = parent.get_mod_timestamp()
				var url_success = window.parent.location.href;
				var url_failure = window.parent.location.href;                        //isExcludes=true  去掉装饰
				var url = wb_utils_invoke_servlet('cmd','del_mod_que','que_id_lst',que_id,'mod_timestamp',mod_timestamp,'mod_id',mod_id,'url_success',url_success,'url_failure',url_failure,'isExcludes','true')
				window.parent.location.href = url;
			}
		}

}


function wbQuestionReadInModule(que_id,mod_type){
	if(mod_type==null||mod_type==""){
		var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','cm_que_get_upd.xsl')
	}else{
		var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','cm_que_get_upd.xsl',"mod_type",mod_type)
	}
	window.parent.content.location.href = url

}


function wbQuestionReadInSelectQuesiton(que_id){
	var url = wb_utils_invoke_servlet('cmd','get_q','que_id',que_id,'stylesheet','cm_que_get_read.xsl')
	window.parent.content.location.href = url
}

function wbQuestionImpPrep(que_obj_id,que_type,mod_type){
	var url = wb_utils_invoke_disp_servlet('module', 'content.ConQueImpModule', 'cmd','get_upload_res_prep', 'stylesheet','content_que_import_prep.xsl','que_obj_id',que_obj_id,'que_type',que_type,"mod_type",mod_type);	
	window.parent.content.location.href = url;
}
