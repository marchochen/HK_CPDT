// ------------------ wizBank Objective object -------------------
// Convention:
//   public functions : use "wbObjective" prefix
//   private functions: use "_wbObjective" prefix
// ------------------------------------------------------------

/* constructor */
function wbObjective() {

	this.select_obj = wbObjectiveSelectQueObjective		
	this.select_res_obj = wbObjectiveSelectResObjective
	this.select_obj_url = wbObjectiveSelectQueObjectiveURL					//for pick/gen question
	this.upd_obj = wbObjectiveUpdQueObjective						//for pick/gen question (dynamic test only)
	this.get_pick_que_lst_url = wbObjectiveGetPickQuestionListURL	//for pick/gen question
	this.get_pick_res_lst_url = wbObjectiveGetPickResourceListURL
	this.pick_que_exec = wbObjectivePickQuestionExec
	this.pick_res_exec = wbObjectivePickResourceExec				//for pick resource (include questions)
	this.gen_que_exec = wbObjectiveGenQuestionExec
	this.select_all_que = wbObjectiveSelectAllQuestion
	this.get_res_lst_url = wbObjectiveGetResourceListURL    //for question manager
	this.show_obj_lst = wbObjectiveShowObjectiveList		//for question manager
	this.refresh_obj_lst = wbObjectiveRefreshObjectiveList
	this.show_trash_obj_lst = wbObjectiveShowTrashObjectiveList // Recycle Bin
	this.get_trash_obj_lst_hdr_url = wbObjectiveGetTrashObjectiveListHeaderURL// Recycle Bin
	this.get_trash_res_lst_url = wbObjectiveGetTrashResourceListURL // Recycle Bin
	this.select_folder = wbObjectiveSelectFolder     //select the folders between "all folders" and "my folders"

	// for syllabus manager only
	this.upd_prep = wbObjectiveUpdPrep
	this.upd_exec = wbObjectiveUpdExec
	this.del = wbObjectiveDel
	this.cut = wbObjectiveCut
	//this.view_right = wbObjectiveRight
	//this.view_right_read = wbObjectiveRightRead
	this.upd_acl_exec = wbObjectiveUpdAclExec
	this.view_right = wbObjectiveUpdAclPrep
	this.view_right_read = wbObjectiveAclRead
	this.move = wbObjectiveMove
	this.paste = wbObjectivePaste
	this.ins_prep = wbObjectiveInsPrep						// for ins assessment  or syllabus
	this.ins_exec = wbObjectiveInsExec  					 // for ins assessment  or syllabus
	this.manage_obj_lst = wbObjectiveManageObjectiveList
	this.manage_ass = wbObjectiveManageAssessment
    this.read_obj_lst = wbObjectiveReaderObjectiveList
	this.contri_obj_lst = wbObjectiveContriObjectiveList
	this.noaccess_obj_lst = wbObjectiveNoaccessObjectiveList

	
	
	
	//pick_objective frame
	this.pick_obj_hidden_frame_url = wbObjectivePickObjectiveHiddenFrameURL
	this.pick_obj_url = wbObjectivePickObjectiveURL
	this.pick_obj_frame = wbObjectivePickObjectiveFrame
	this.pick_obj_frame_url = wbObjectivePickObjectiveFrameURL
	this.upd_obj_url = wbObjectiveUpdObjectiveURL
	this.pick_que_inst_url = wbObjectivePickQueInstURL
	this.pick_res_inst_url = wbObjectivePickResInstURL
	//For Standard Test
	this.pick_auto_que = wbObjectivePickAutoQue
	this.pick_direct_que = wbObjectivePickDirectQue
	this.add_obj = wbObjectiveAddObjective
	
	this.show_all_obj_lst = wbObjectiveShowAllList
	this.del_mutil_obj = wbObjectiveDeleteMultiObjective
}
function wbObjectiveShowAllList(privilege, choice,show_all, tcr_id) {
	if(privilege == null || privilege == ''){
		privilege = 'AUTHOR';
	}
	if(tcr_id ==null || tcr_id == '') {
		tcr_id = 0;
	}
	var url = wb_utils_invoke_servlet('cmd', 'get_syb_obj', 'syb_privilege', privilege, 'res_type', 'QUE~GEN~AICC~ASM~SCORM~NETGCOK', 'res_lan', '', 'stylesheet', 'res_obj_lst.xsl','folders',choice, 'show_all', show_all, 'obj_tcr_id', tcr_id);
	window.location.href = url;
}
function wbObjectiveSelectFolder(frm,choice, show_all, tcr_id){
	//window.open("http://www.sina.com.cn");
	if(frm.folders != null){
        choice = frm.folders.value
	}
     //alert(choice);
	url = wbTopObjectiveListUrl(choice, show_all, tcr_id);
	window.location.href = url;
}

function wbTopObjectiveListUrl(choice, show_all, tcr_id){
	if(show_all =='' || show_all==null){
		show_all = false;
	}
	if(tcr_id=='' || tcr_id == null) {
		tcr_id = 0;
	}
	var url = wb_utils_invoke_servlet('cmd','get_syb_obj','res_type','QUE~GEN~AICC~ASM~SCORM~NETGCOK','stylesheet','res_obj_lst.xsl','folders',choice,'show_all', show_all, 'obj_tcr_id', tcr_id);
	return url;
}
function wbObjectivePickQueInstURL(){
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','tst_que_add_inst.xsl');
	return url
}

function wbObjectivePickResInstURL(){
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','res_add_inst.xsl');
	return url
}

function wbObjectiveAddObjective(cos_id,mod_id,obj_id,status){
	res_subtype = wb_utils_get_cookie('res_subtype');
	if(res_subtype == 'FAS'){
	 	var url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',mod_id,'stylesheet','tst_que_add_obj_frame.xsl', 'obj_id',obj_id, 'url_failure',parent.location.href)
	}else{
		var url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'course_id',cos_id,'obj_id',obj_id,'dpo_view', 'IST_READ','stylesheet','tst_que_add_obj_frame.xsl','upd_tst_que','true','url_failure',parent.location.href)
	}
	parent.location.href = url;
}

function wbObjectivePickAutoQue(obj_id){
	var url = wb_utils_invoke_servlet('cmd','get_que_lst','que_obj_id',obj_id,'stylesheet','tst_que_parm.xsl')
	window.location.href = url;
}

function wbObjectivePickDirectQue(mod_id,obj_id){
//man:replaced
	var url_failure = parent.location.href;
	var url_success  = wb_utils_invoke_servlet('cmd','get_prof','que_id',obj_id,'res_type','QUE','stylesheet','tst_que_add_frame.xsl', 'res_privilege','CW')
	var url = wb_utils_invoke_servlet('cmd','ins_mod_obj','mod_id',mod_id,'obj_id',obj_id,'url_success',url_success,'url_failure',url_failure)
	window.location.href = url;
}

function wbObjectivePickObjectiveFrame(cos_id,mod_id){
	var url = wbObjectivePickObjectiveFrameURL(cos_id,mod_id);
	parent.window.location.href = url;
}
function wbObjectiveUpdObjectiveURL(mod_id,obj_id, msp_id, res_type){
		var url_failure  = parent.location.href;
		if (res_type == 'FAS' || res_type == 'DAS'){
			var url = wb_utils_invoke_disp_servlet('isExcludes','true','module','quebank.QueBankModule','cmd','get_crit_spec','res_id',mod_id,'qcs_id',msp_id,'res_type','ASM','res_subtype',res_type,'stylesheet','tst_que_upd_parm.xsl','url_failure',url_failure);
		}else{
			var url = wb_utils_invoke_servlet('cmd','get_msp','mod_id',mod_id,'msp_id', msp_id, 'obj_id',obj_id,'stylesheet','tst_que_upd_parm.xsl','url_failure',url_failure)
		}
		return url;
}

function wbObjectivePickObjectiveFrameURL(cos_id,mod_id){
	res_subtype = wb_utils_get_cookie('res_subtype');
	if (res_subtype == 'FAS' ) {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',mod_id,'stylesheet','tst_info_frame.xsl')
	} else if (res_subtype == 'DAS' ){
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',mod_id,'stylesheet','tst_info_frame.xsl')
	}else{
		url = wb_utils_invoke_servlet('cmd','get_mod','course_id',cos_id,'mod_id',mod_id,'dpo_view','IST_READ','stylesheet','tst_info_frame.xsl')
	}
	return url;
}

function wbObjectivePickObjectiveHiddenFrameURL(cos_id,mod_id){
	res_subtype = wb_utils_get_cookie('res_subtype');
	if (res_subtype == 'DAS' || res_subtype == 'FAS' ) {
		url = wb_utils_invoke_disp_servlet('isExcludes','true','module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',mod_id,'stylesheet','tst_que_pick_obj_hidden_frame.xsl')
	}else{
	        url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'course_id',cos_id,'dpo_view', 'IST_READ','stylesheet','tst_que_pick_obj_hidden_frame.xsl')
	}
	return url;
}


function wbObjectivePickObjectiveURL(type,tcr_id){
	var url = ''
	//Man: Open Pick Objective tree		
	if(type == null){
		type = '';
	}
	if(type == 'QUE'){
		url = wb_utils_invoke_servlet('cmd','gen_tree','tree_type','syllabus_and_object','tree_subtype',type,'stylesheet','tst_que_pick_obj.xsl','pick_root',0,'pick_method',2,'complusory_tree',0,'create_tree','y', 'parent_tcr_id', tcr_id)
	}else if(type == 'GEN' || type == 'AICC' || type == 'ASM' || type == 'SCORM' || type == 'NETGCOK'){
		url = wb_utils_invoke_servlet('cmd','gen_tree','tree_type','syllabus_and_object','tree_subtype',type,'stylesheet','res_pick_obj.xsl','pick_root',0,'pick_method',2,'complusory_tree',0,'create_tree','y', 'parent_tcr_id', tcr_id)
	}
	return url;
}

// private function
function _wbObjectiveValidateFrm(frm,field_name, lab_course_nm,lang) {
	var field = eval('frm.' + field_name);
	if (field) {
		field.value = wbUtilsTrimString(field.value);
		if (!gen_validate_empty_field(field, lab_course_nm,lang)) {
			eval('frm.' + field_name).focus()
			return false;
		}
		if (getChars(field.value) > 80) {
			alert(eval('wb_msg_'+lang+'_title_length'));
			eval('frm.' + field_name).focus()
			return false;
		}
	}

	return true;
}

function _wbObjectiveValidateGenFrm(frm,lang){
	frm.msp_score.value = wbUtilsTrimString(frm.msp_score.value);
	if (!wbUtilsValidateEmptyField(frm.msp_score, eval('wb_msg_'+lang+'_score_que'))){
		frm.msp_score.focus()
		return false;
	}else{
		if (!wbUtilsValidateNonZeroPositiveInteger(frm.msp_score, eval('wb_msg_'+lang+'_score_que'))){
			frm.msp_score.focus()
			return false;
		}
		if (frm.msp_score.value.length > 5){
			Dialog.alert(eval('wb_msg_'+lang+'_score_que')+eval('wb_msg_'+lang+'_score_max'));
			frm.msp_score.focus()
			return false;
		}
	}
	frm.msp_qcount.value = wbUtilsTrimString(frm.msp_qcount.value);
	if (!wbUtilsValidateEmptyField(frm.msp_qcount, eval('wb_msg_'+lang+'_ttl_que'))){
		frm.msp_qcount.focus()
		return false;
	}else{
		if (!wbUtilsValidateNonZeroPositiveInteger(frm.msp_qcount, eval('wb_msg_'+lang+'_ttl_que'))){
			frm.msp_qcount.focus()
			return false;
		}
		
		if (frm.msp_qcount.value.length > 5){
			Dialog.alert(eval('wb_msg_'+lang+'_faq_que_num')+eval('wb_msg_'+lang+'_score_max'));
			frm.msp_qcount.focus()
			return false;
		}
	}

	return true;
}
// public function
function _wbObjectiveGetResNumber(frm, lang) {
		var i, n, ele, str, count=0
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.checked && (ele.name != 'sel_all_checkbox')) {
				if ( ele.value != ""){
					count++
					str = str + ele.value + "~"
				}
			}
		}

		if (count > 1000) {
			alert(eval('wb_msg_' + lang + '_more_que'))
			return false;
		}

		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		return str;
}

function wbObjectiveSelectResObjective(obj_id,res_type){
	var url = wb_utils_invoke_servlet('cmd','get_prof','que_id',obj_id,'res_type',res_type,'stylesheet','res_add_frame.xsl', 'res_privilege','')
	window.location.href = url;
}

function wbObjectiveSelectQueObjective(obj_id,mod_subtype){
		var url = wbObjectiveSelectQueObjectiveURL(obj_id,mod_subtype);
		window.location.href = url;
}

function wbObjectiveSelectQueObjectiveURL(obj_id,mod_subtype){
		var mod_id  = parent.hidden.document.frmXml.mod_id.value;
		if(mod_subtype == 'DXT'){
			var url = wb_utils_invoke_servlet('cmd','get_que_lst','que_obj_id',obj_id,'stylesheet','tst_que_parm.xsl')
		}else if(mod_subtype == 'TST'){
			var url_failure = parent.location.href;
			var url_success  = wb_utils_invoke_servlet('cmd','get_prof','que_id',obj_id,'res_type','QUE','stylesheet','tst_que_add_frame.xsl', 'res_privilege','')
			var url = wb_utils_invoke_servlet('cmd','ins_mod_obj','mod_id',mod_id,'obj_id',obj_id,'url_success',url_success,'url_failure',url_failure)
		}else if (mod_subtype == 'FAS'){
			var url_failure  = parent.location.href;
			var url_success  = wb_utils_invoke_servlet('cmd','get_prof','que_id',obj_id,'res_type','QUE','stylesheet','tst_que_add_frame.xsl', 'res_privilege','')
			var url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','add_que_container_obj','res_type','ASM','res_subtype',mod_subtype,'res_id',mod_id,'obj_id',obj_id,'url_success',url_success,'url_failure',url_failure);
		} else if (mod_subtype == 'DAS'){
			var url = wb_utils_invoke_servlet('cmd','get_que_lst','que_obj_id',obj_id,'stylesheet','tst_que_parm.xsl')
		}
		return url;
}

function wbObjectiveUpdQueObjective(cos_id,mod_id,obj_id, msp_id, res_type){
	if (res_type == 'DAS' || res_type == 'FAS' ){
		var url = wb_utils_invoke_disp_servlet('isExcludes','true','module','quebank.QueBankModule','cmd','get_assessment_obj','msp_id',msp_id, 'obj_id',obj_id, 'res_id',mod_id,'stylesheet','tst_que_upd_obj_frame.xsl','url_failure',parent.location.href)
	}else{
		var url = wb_utils_invoke_servlet('cmd','get_mod','msp_id',msp_id, 'mod_id',mod_id,'course_id',cos_id,'obj_id',obj_id,'dpo_view', 'IST_READ','stylesheet','tst_que_upd_obj_frame.xsl','upd_tst_que','true','url_failure',parent.location.href)
	}
	parent.location.href = url;
}

function wbObjectivePickQuestionExec(lang,isExcludes){
	    var frm=document.frmQue;
	    if(!frm){
		   frm=window.menu.document.frmQue;
	    }
	    var mod_type = parent.parent.hidden.document.frmXml.mod_type.value;
		var mod_subtype = parent.parent.hidden.document.frmXml.mod_subtype.value;
		if (mod_subtype == 'FAS' ){
			frm.res_id.value = parent.parent.hidden.document.frmXml.mod_id.value
			frm.action = wb_utils_disp_servlet_url + "?isExcludes=true"
			frm.res_type.value ="ASM"
			frm.res_subtype.value = "FAS"
			frm.module.value = "quebank.QueBankModule"
			frm.cmd.value = "add_que"
		}else{
			frm.mod_id.value =parent.parent.hidden.document.frmXml.mod_id.value
			frm.action = wb_utils_servlet_url
			if(isExcludes == true || isExcludes == 'true'){
				frm.action += "?isExcludes=true";
			}
		}
		
		frm.url_success.value = obj.pick_obj_frame_url(parent.parent.hidden.document.frmXml.cos_id.value,parent.parent.hidden.document.frmXml.mod_id.value)		
		frm.url_failure.value = obj.pick_obj_frame_url(parent.parent.hidden.document.frmXml.cos_id.value,parent.parent.hidden.document.frmXml.mod_id.value)
		frm.que_id_lst.value = _wbObjectiveGetResNumber(frm, lang)
		//校验题目总数不通过，返回
		if(frm.que_id_lst.value == 'false'){
			return;
		}
		if (frm.que_id_lst.value == "") {
			alert(eval('wb_msg_' + lang + '_no_que'))
			return
		}
		frm.submit();
}

function wbObjectivePickResourceExec(frm,res_id,mod_type){

	if(mod_type == 'unknow'){
		mod_type = parent.parent.hidden.document.frmXml.mod_subtype.value;
	}

	if (mod_type == 'TST' ||  mod_type == 'DXT'){
		var url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'dpo_view','IST_EDIT','res_subtype',mod_type,'stylesheet','pick_res_from_rm.xsl','url_failure',parent.location.href,'replace', 'Y','isExcludes',true);
	} else if (mod_type == 'SCORM') {
		cos_id = gen_get_cookie_token('wiz', 'cos_id');
		
		var url_success =  'window.top.opener.top.location.reload();exedjs_function(1)';
		var url = wb_utils_invoke_disp_servlet('env','wizb','module','importcos.ImportCosModule','cmd','IMPORT_SCORM_1_2','src_res_id',res_id,'cos_id',cos_id,'url_success',url_success,'isExcludes',true);
		
		if(window.top.opener && window.top.opener.frmXml && window.top.opener.frmXml.mod_mobile_ind){
			var checkedValue = 0;
			var radioArr = window.top.opener.frmXml.mod_mobile_ind;
			for(var i=0;i<radioArr.length;i++){
				if(radioArr[i].checked){
					checkedValue = radioArr[i].value;
				}
			}
			url += "&mod_mobile_ind="+checkedValue;
		}
		
		parent.parent.location = url;

		return;
	}else{
		var url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'dpo_view','IST_EDIT','res_subtype',mod_type,'stylesheet','pick_res_from_rm.xsl','url_failure',parent.location.href,'replace', 'Y','isExcludes',true);
	}
	
	window.parent.location = url
}

function wbObjectiveGetPickQuestionListURL(que_obj_id,privilege,sort_order,sort_col,gLan,gDifficulty,gType,gSubType){
		var url = wb_utils_invoke_servlet('cmd','get_que_lst','que_obj_id',que_obj_id,'stylesheet','tst_que_add_menu.xsl','res_privilege',privilege,'sort_order',sort_order,'sort_col',sort_col,'res_lan',gLan,'res_difficulty',gDifficulty,'res_type',gType,'res_subtype',gSubType,'res_status','ON')
		return url
}

function wbObjectiveGetPickResourceListURL(que_obj_id,privilege,sort_order,sort_col,gLan,gDifficulty,gType,gSubType){
		var url = wb_utils_invoke_servlet('cmd','get_que_lst','que_obj_id',que_obj_id,'stylesheet','tst_res_add_menu.xsl','res_privilege',privilege,'sort_order',sort_order,'sort_col',sort_col,"page_size","100000",'res_lan',gLan,'res_difficulty',gDifficulty,'res_type',gType,'res_subtype',gSubType,'res_status','ON')
		return url
}

function wbObjectiveGenQuestionExec(frm,mod_id,obj_id,lang,mod_subtype,cos_id){
		var url = "";
		if (_wbObjectiveValidateGenFrm(frm,lang)) {
			var out_qtype = "";
			var out_qdiff = "";
			for(var i=0;i<frm.mod_qtype_rdo.length;i++){
				if(frm.mod_qtype_rdo[i].checked){
					out_qtype += frm.mod_qtype_rdo[i].value +'~';
				}
			}	
			out_qtype = out_qtype.substring(0,out_qtype.length -1);		
			for(var i=0;i<frm.mod_qdiff.length;i++){
				if(frm.mod_qdiff[i].checked){
					out_qdiff = frm.mod_qdiff[i].value;
					break;
				}
			}
			if(out_qtype == ''){
				Dialog.alert(wb_msg_select_que_type);
				return;
			}
			
			frm.url_success.value = wbObjectivePickObjectiveFrameURL(cos_id,mod_id);
			frm.url_failure.value = wbObjectivePickObjectiveFrameURL(cos_id,mod_id);
			frm.method = 'get';
			
			if (mod_subtype == 'DAS'){
				frm.action = wb_utils_disp_servlet_url;
				if (frm.qcs_id && frm.qcs_id.value != ''){
					frm.cmd.value = 'upd_crit_spec';
				}else{
					frm.cmd.value = 'add_crit_spec';
				}
				frm.module.value = 'quebank.QueBankModule';
				frm.res_id.value = mod_id;
				if (frm.qcs_obj_id)
					frm.qcs_obj_id.value = obj_id;
				frm.qcs_type.value = out_qtype;
				frm.qcs_score.value = frm.msp_score.value;
				frm.qcs_difficulty.value = out_qdiff; 
				frm.qcs_qcount.value = frm.msp_qcount.value;
			}else{
				frm.action = wb_utils_servlet_url;
				frm.cmd.value = 'upd_msp';
				frm.mod_id.value = mod_id;
			}
			if(frm.isExcludes){
				frm.isExcludes.value = true; //过滤页面
			}
			frm.obj_id.value = obj_id;
			frm.msp_type.value = out_qtype;
			frm.msp_difficulty.value = out_qdiff;
			frm.target = '_top';
			frm.submit();
			
		}
}


function wbObjectiveGetResourceListURL(que_obj_id,privilege,status,sort_order,sort_col,gLan,gDifficulty,gType,gSubType){
		var url = wb_utils_invoke_servlet('cmd','get_que_lst','que_obj_id',que_obj_id,'stylesheet','res_lst.xsl','res_privilege',privilege,'res_status',status,'sort_order',sort_order,'sort_col',sort_col,'res_lan',gLan,'res_difficulty',gDifficulty,'res_type',gType,'res_subtype',gSubType);
		return url;
}



function wbObjectiveSelectAllQuestion(frm,checkedVal){

		var i, n, ele

		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox")
				ele.checked = checkedVal;
		}
}


function wbObjectiveShowObjectiveList(syb_id,obj_id,frm,choice, show_all){
	if(frm.folders){
		 choice = frm.folders.value;
	}
	if(show_all =='' || show_all==null){
		show_all = false;
	}
	var url = wb_utils_invoke_servlet('cmd','test_get_obj_lst_hdr','syb_id',syb_id,'obj_id',obj_id,'res_type','PST~QUE~GEN~AICC~ASM~SCORM~NETGCOK','stylesheet','res_hdr.xsl','res_privilege','','folders',choice,'show_all',show_all)
	window.location.href = url;
}

function wbObjectiveShowTrashObjectiveList(que_obj_id_syb){
	var url = wb_utils_invoke_servlet('cmd','test_get_obj_lst_hdr','obj_id',que_obj_id_syb,'res_type','','stylesheet','res_obj_trash_fra.xsl')
	window.location.href = url
}

function wbObjectiveGetTrashObjectiveListHeaderURL(que_obj_id,que_obj_id_ass,choice,show_all){
	if (que_obj_id_ass == null){
		var url = wb_utils_invoke_servlet('cmd','test_get_obj_lst','obj_id',que_obj_id,'stylesheet','res_trash_lst_hdr.xsl','folders',choice,'show_all', show_all);
	}else{
		var url = wb_utils_invoke_servlet('cmd','test_get_obj_lst_hdr','obj_id',que_obj_id,'que_obj_id_ass',que_obj_id_ass,'stylesheet','res_hdr.xsl','folders',choice,'show_all', show_all);
	}
	return url;
}

function wbObjectiveGetTrashResourceListURL(que_obj_id,privilege,status,sort_order,sort_col,gLan,gDifficulty,gType,gSubType){
		var	url = wb_utils_invoke_servlet('cmd','get_que_lst','que_obj_id',que_obj_id,'stylesheet','res_trash_lst.xsl','res_privilege',privilege,'res_status',status,'sort_order',sort_order,'sort_col',sort_col,'res_lan',gLan,'res_difficulty',gDifficulty,'res_type',gType,'res_subtype',gSubType)
		return url
}

//排序
function wbObjectiveRefreshObjectiveList(frm,flag){
		var url = parent.location.href
				
		if(frm.res_status.options && frm.res_status.options != null){
			url = setUrlParam('res_status',frm.res_status.options[frm.res_status.selectedIndex].value, url);
		}else if(frm.res_status){
			url = setUrlParam('res_status',frm.res_status.value, url);
		}		
		url = setUrlParam('res_lan','', url);
		if (frm.res_difficulty.type != 'hidden'){
			if(frm.res_difficulty.options != null)
				url = setUrlParam('res_difficulty',frm.res_difficulty.options[frm.res_difficulty.selectedIndex].value, url);
		}
		//时间
		if(frm.sort_order != undefined && frm.sort_order.options && frm.sort_order.options != null ){
			var sort_order = frm.sort_order.options[frm.sort_order.selectedIndex].value;
			var sort_col = '';
			if(sort_order.indexOf("~")!=0)
			{
				sort_col = sort_order.substring(0,sort_order.indexOf("~"));
			    sort_order =  sort_order.substring(sort_order.indexOf("~")+1);
			}else{
				sort_order = sort_order.replace('~', '');
			}
			url = setUrlParam('sort_order',sort_order, url);
			url = setUrlParam('sort_col',sort_col, url);
		}
		
		if(frm.res_type.options != null){
		   type = frm.res_type.options[frm.res_type.selectedIndex].value;
		}else{
		   type = frm.res_type.value;
		}
		if ( type == 'QUE~GEN~AICC~ASM~SCORM~NETGCOK' || type == 'GEN~AICC~SCORM~NETGCOK' || type == 'QUE~GEN' || type == 'QUE' || type == 'GEN' || type == 'AICC' || type == 'SCORM' || type == 'NETGCOK'){
				url = setUrlParam('res_type',type, url)
				url = setUrlParam('res_subtype',frm.res_subtype.value,url)
				if(flag === 'true'){
					url = setUrlParam('cur_page','1', url);
				}
		}else if (type == 'ASM'){
			url = setUrlParam('res_type','ASM',url)
			url = setUrlParam('res_subtype','',url)
		}else if (type == 'FAS' || type == 'DAS'){
			url = setUrlParam('res_type','ASM',url)
			url = setUrlParam('res_subtype',type,url)
		}else if (type == 'FSC' || type == 'DSC'){
			url = setUrlParam('res_type','QUE',url)
			url = setUrlParam('res_subtype',type,url)
		}else {
				if ( type == 'MC' ||  type == 'FB' ||  type == 'MT' || type == 'TF' || type == 'ES' )
				{
					url = setUrlParam('res_type','QUE', url)
				}
				else
				{
					if(type == 'SSC'){
						url = setUrlParam('res_type','AICC',url)
					}else{
						url = setUrlParam('res_type','GEN', url)
					}
				}
				url = setUrlParam('res_subtype',type, url);

		}
		parent.location.href= url;
}

function wbObjectiveAclRead(id,choice){
	wb_utils_set_cookie('url_prev',self.location.href)
	var url = wb_utils_invoke_servlet('cmd','get_obj','stylesheet','res_obj_acl_read.xsl','obj_id',id,'url_failure',self.location.href,'folders',choice)
	window.location.href=url;
}

function wbObjectiveUpdAclPrep(id,choice){
	wb_utils_set_cookie('url_prev',self.location.href)
	var url = wb_utils_invoke_servlet('cmd','get_obj','stylesheet','res_obj_upd_acl.xsl','obj_id',id,'url_failure',self.location.href,'folders',choice)
	window.location.href=url;
}


function wbObjectiveUpdPrep(id){
	wb_utils_set_cookie('url_prev',self.location.href)
	var url = wb_utils_invoke_servlet('cmd','get_obj','stylesheet','res_obj_upd.xsl','obj_id',id,'url_failure',self.location.href)
	window.location.href=url;
}

function wbObjectiveUpdAclExec(frm,choice) {
	//check  training center
	if(frm.usr_group_lst){
		if(frm.usr_group_lst.options[0]){
			if(frm.usr_group_lst.options[0].value=='') {
				alert(eval("wb_pls_select_tc"));
				return;
			}
			frm.obj_tcr_id.value = frm.usr_group_lst.options[0].value;
		}
	}	
	//if exist reader_ent_id_lst
	if(frm.reader_ent_id_lst){
		if(frm.user_sel_all_usr[0].checked) {
				frm.reader_ent_id_lst.value = '-1';
		} else if(frm.user_sel_all_usr[1].checked){
				frm.reader_ent_id_lst.value = '0';
		}else{
			frm.reader_ent_id_lst.value = _wbObjectiveGetSelectionIdLst(frm,frm.reader_id_lst);
			if(frm.reader_id_lst.options.length <=0){
			    alert(eval('wb_msg_access_control_select_reader_ent_lst'));
			    return;
			 }
		}
	}
	//if exist author_ent_id_lst
	if(frm.author_ent_id_lst){			
		if(frm.user_sel_all_author[0].checked){
			frm.author_ent_id_lst.value = '-1';
		}else if(frm.user_sel_all_author[1].checked){
		    frm.author_ent_id_lst.value = '0';
		} else {
			frm.author_ent_id_lst.value = _wbObjectiveGetSelectionIdLst(frm,frm.author_id_lst);
			if(frm.author_id_lst.options.length <=0){
				alert(eval('wb_msg_access_control_select_author_ent_lst'));
				return;
			}
		}
	}
	//if exist owner_ent_id_lst
	if(frm.owner_ent_id_lst){
		frm.owner_ent_id_lst.value = _wbObjectiveGetSelectionIdLst(frm,frm.owner_id_lst);
		if(frm.owner_id_lst.options.length <= 0){
			alert(eval('wb_msg_access_control_select_owner_ent_lst'));
			return;
		}
	}
	frm.url_success.value = wb_utils_get_cookie('url_prev')
	frm.url_failure.value = self.location.href
	frm.action = wb_utils_servlet_url
	frm.method = 'POST'
	frm.submit();
}


function wbObjectiveUpdExec(frm,lang){
	if (_wbObjectiveValidateFrm(frm,'obj_desc', eval('wb_msg_res_obj_nm'),lang)) {
		if(frm.tcr_id_lst){
			if(frm.tcr_id_lst.options[0]){
				if(frm.tcr_id_lst.options[0].value=='') {
					alert(eval("wb_pls_select_tc"));
					return;
				}
				frm.obj_tcr_id.value = frm.tcr_id_lst.options[0].value;
			}
		}	
		frm.url_success.value = wb_utils_get_cookie('url_prev')
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

function wbObjectiveDel(frm,obj_id,obj_id_parent,lang,choice){

	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {

		if (obj_id_parent == '0' || obj_id_parent == '') {
			url_success = wb_utils_adm_syb_lst_url('',choice,true);
		}
		else {
			//url_success = wbObjectiveManageObjectiveListUrl(frm,obj_id_parent,'',choice);
			//alert(choice+obj_id_parent);
			url_success = wbObjectiveManageObjectiveListUrl(choice,obj_id_parent);
		}
		url_failure = self.location.href
		url = wb_utils_invoke_servlet('cmd','del_obj','res_type','QUE~GEN~AICC~ASM','obj_id',obj_id,'url_success',url_success,'url_failure',url_failure)
		window.location.href=url
	}
}


function wbObjectiveCut(child_id,parent_id,type,lang){

		alert(wb_msg_cut_obj);
		wb_utils_set_cookie('obj_id_copy' , child_id)
		wb_utils_set_cookie('obj_id_parent' , parent_id)
		wb_utils_set_cookie('obj_id_type' , type)
}


function wbObjectiveMove(id){
	//David
	//To open a tree
	var move_objective;
	var gen_tree;
	str_feature = 'toolbar=' + 'no' + ',width=' + '600' + ',height=' + '400' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',screenX=' + '0' + ',screenY=' + '10' + ',left=' + '0' + ',top=' + '10'
	url = wb_utils_invoke_servlet('cmd', 'gen_tree' , 'tree_type', 'move_objective', 'stylesheet', 'gen_tree.xsl', 'pick_leave', '0', 'pick_root','1', 'pick_method', '2' ,'node_id', id ,'flag', 'true' ,'tree_subtype' ,'' ,'js' ,'obj.paste','close_option','1' ,'override_appr_usg','0' ,'get_supervise_group','0', 'complusory_tree' ,'1' ,'get_direct_supervise','0','create_tree','y')
	wbUtilsOpenWin(url, 'move', false, str_feature);
}


function wbObjectivePaste(str){
	str = unescape(str);
	var parent_id;
	
	obj_id = document.frmXml.obj_id.value;
	strArray = str.split('~%~');
	parent_id = strArray[1];
	//if(obj_id!=parent_id){
		url_success = self.location.href
		url_failure = self.location.href
		url = wb_utils_invoke_servlet('cmd','paste_obj','obj_id',obj_id,'ent_id_parent',parent_id,'url_success',url_success,'url_failure',url_failure)
		window.location.href=url
 	//}else{
		//alert("Can not move to the same location");
	//}

}

/*
function wbObjectivePaste(parent_id,syb_id,lang){

		var obj_id = parseInt(wb_utils_get_cookie('obj_id_copy'))
		var obj_id_parent = parseInt(wb_utils_get_cookie('obj_id_parent'))
		var obj_id_type = wb_utils_get_cookie('obj_id_type')

		url_success = self.location.href
		url_failure = self.location.href


		if (obj_id > 0)  {
			if (obj_id_parent == parent_id || obj_id == parent_id) {
				alert(eval('wb_msg_' + lang + '_same_pos'))
				return;
			}else {

				url = wb_utils_invoke_servlet('cmd','paste_obj','obj_id',obj_id,'ent_id_parent',parent_id,'url_success',url_success,'url_failure',url_failure,'syb_id',syb_id)

				if (parent_id == 0 && obj_id_type == 'ASS') {
						alert(eval('wb_msg_' + lang + '_paste_level'))
				}else {
						window.location.href=url
				}
			}
		}else {
			alert(eval('wb_msg_' + lang + '_select_objective'))
			return;
		}
}
*/
/*
function wbObjectiveInsPrep(syb_id,obj_id,choice){
	url = wb_utils_invoke_servlet('cmd','test_get_obj_lst_hdr','syb_id',syb_id,'obj_id',obj_id,'stylesheet','res_obj_ins.xsl','folders',choice)
	window.location.href=url

} */

function wbObjectiveInsPrep(syb_id,obj_id,frm,choice,show_all,obj_tcr_id){
	if(frm.folders){
		 choice = frm.folders.value;
	}
	if(obj_id == '0') {
		stylesheet = 'res_obj_top_ins.xsl'
	} else {
		stylesheet = 'res_obj_ins.xsl'
	}
	if(show_all ==''||show_all==null) {
		show_all=false;
	}
	if(obj_tcr_id == undefined || obj_tcr_id == ''){
		obj_tcr_id = 0;
	}
	url = wb_utils_invoke_servlet('cmd','test_get_obj_lst_hdr','syb_id',syb_id,'obj_id',obj_id,'stylesheet',stylesheet,'folders',choice,'show_all',show_all,'obj_tcr_id',obj_tcr_id)
	
	window.location.href=url

}

function wbObjectiveInsExec(frm,lang,choice,show_all){
	if (_wbObjectiveValidateFrm(frm,'obj_desc', eval('wb_msg_'+lang+'_tnd_title'),lang)) {
		if (frm.obj_type.value == ''){
			alert(eval('wb_msg_'+lang+'_select_obj_type'))
			return false;

		}else{
		    if(frm.usr_group_lst){
				if(frm.usr_group_lst.options[0]){
					if(frm.usr_group_lst.options[0].value=='') {
						alert(eval("wb_pls_select_tc"));
						return;
					}
					frm.obj_tcr_id.value = frm.usr_group_lst.options[0].value;
				}
			}		    
			if(show_all=='' || show_all==null) {
				show_all=false;
			}
			if (frm.obj_id_parent.value == '0') {frm.url_success.value = wb_utils_adm_syb_lst_url('',choice,show_all, frm.obj_tcr_id.value);}
			else  {frm.url_success.value = wbObjectiveManageObjectiveListUrl(choice,frm.obj_id_parent.value);}
			frm.url_failure.value = self.location.href
			frm.method = 'post'
			frm.action = wb_utils_servlet_url
			
			//if exist reader_ent_id_lst
//			if(frm.reader_ent_id_lst){
//				if(frm.user_sel_all_usr[0].checked) {
//					frm.reader_ent_id_lst.value = '-1';
//				} else if(frm.user_sel_all_usr[1].checked){
//					frm.reader_ent_id_lst.value = '0';
//				}else{
//					frm.reader_ent_id_lst.value = _wbObjectiveGetSelectionIdLst(frm,frm.reader_id_lst);
//					if(frm.reader_id_lst.options.length <=0){
//					    alert(eval('wb_msg_access_control_select_reader_ent_lst'));
//					    return;
//					 } 
//				}
//
//			}
			//if exist author_ent_id_lst
//			if(frm.author_ent_id_lst){			
//				if(frm.user_sel_all_author[0].checked){
//					frm.author_ent_id_lst.value = '-1';
//				}else if(frm.user_sel_all_author[1].checked){
//					frm.author_ent_id_lst.value = '0';
//				}else{
//					frm.author_ent_id_lst.value = _wbObjectiveGetSelectionIdLst(frm,frm.author_id_lst);
//					if(frm.author_id_lst.options.length <=0){
//						alert(eval('wb_msg_access_control_select_author_ent_lst'));
//						return;
//					}
//				}
//
//			}
			//if exist owner_ent_id_lst
			if(frm.owner_ent_id_lst){
				frm.owner_ent_id_lst.value = frm.owner_id_lst.value;
			}
//			if(frm.owner_ent_id_lst){
//				frm.owner_ent_id_lst.value = _wbObjectiveGetSelectionIdLst(frm,frm.owner_id_lst);
//				if(frm.owner_id_lst.options.length <= 0){
//					alert(eval('wb_msg_access_control_select_owner_ent_lst'));
//					return;
//				}
//			}                       
		  }
		frm.submit();
		}
	}


function wbObjectiveManageObjectiveList(frm,obj_id,obj_name,choice, show_all){
	if(frm){
	  if(frm.folders != null){
        choice = frm.folders.value
	  }
	}
	url = wbObjectiveManageObjectiveListUrl(choice,obj_id,show_all)
	window.location.href = url;
}

function wbObjectiveManageObjectiveListUrl(choice,obj_id, show_all){
	if(show_all =='' || show_all==null){
		show_all = false;
	}
	url = wb_utils_invoke_servlet('cmd','test_get_obj_lst','obj_id',obj_id,'res_type','QUE~GEN~AICC~ASM~SCORM~NETGCOK','res_lan','','stylesheet', 'res_obj_node_lst.xsl','folders',choice,'show_all', show_all);
	return url;
}

//new function for new accesses
function wbObjectiveReaderObjectiveList(){
	 alert("Read!")
}

function wbObjectiveReaderObjectiveListUrl(){
}


function wbObjectiveContriObjectiveList(){
	 alert("Contributor!")
}

function wbObjectiveContriObjectiveListUrl(){
}

function wbObjectiveNoaccessObjectiveList(){
	 alert("No access!")
}

function wbObjectiveNoaccessObjectiveListUrl(){
}


function wbObjectiveManageAssessment(obj_id,nm){
		url = wb_utils_invoke_servlet('cmd','get_obj','stylesheet','ass_manager.xsl','obj_id',obj_id)
		window.location.href=url
}

function _wbObjectiveGetSelectionIdLst(frm,obj){
	var str, i, n
	str = ''
	n = obj.options.length
	for (i=0; i < n; i++){
		if (obj.options[i].value != ""){
			str += obj.options[i].value + '~'
		}		
	}
	if (str != "") {
		str = str.substring(0, str.length-1);
	}
	return str;
}
function wbObjectiveDeleteMultiObjective(frm,lang){
	var obj_id_lst = _wbObjectiveGetMultiActionLst(frm)
	if(obj_id_lst == ''){
		alert(eval("wb_msg_"+ lang +"_select_res_folder"));
		return;
	}else{
		frm.obj_id_lst.value = obj_id_lst
		//
		url = window.location.href;
		frm.url_success.value = url
		frm.url_failure.value = url
		frm.res_type.value='QUE~GEN~AICC';
		frm.cmd.value = 'del_multi_obj';
		frm.action = wb_utils_servlet_url;
		frm.method = 'post';
		if(confirm(eval('wb_msg_' + lang + '_confirm'))){
			frm.submit();
		}
	}
}


function _wbObjectiveGetMultiActionLst(frm) {
	var i, n, ele, str
	str = ""
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name=='obj_id') {
			if (ele.value !="")
				str = str + ele.value + "~"
		}
	}
	if (str != "") {str = str.substring(0, str.length-1);}
	return str;
}