// ------------------ wizBank Application object ------------------- 
// Convention:
//	public functions : use "wbComp" prefix 
//	private functions: use "_wbComp" prefix
// Dependency:
//	gen_utils.js
//	wb_utils.js
// ---------------------------------------------------------------------------- 

/* constructor */
function wbCompetence(){
	this.get_all_scale = wbCompGetAllScale;
	this.get_scale = wbCompGetScale;
	this.add_scale_prep = wbCompAddScalePrep;
	this.add_scale_exec = wbCompAddScaleExec;
	this.edit_scale_prep = wbCompEditScalePrep;
	this.edit_scale_exec = wbCompEditScaleExec;
	this.del_scale = wbCompDelScale;
	this.srh_simple_scale = wbCompSearchScaleSimple;
	this.srh_adv_scale = wbCompSearchScaleAdvanced;
	
	this.get_all_skillgp = wbCompGetAllSkillGp;
	this.get_skillgp = wbCompGetSkillGp;
	this.ins_skillgp_prep = wbCompAddSkillGpPrep;
	this.ins_skillgp_exec = wbCompAddSkillGpExec;
	this.del_skillgp = wbCompDelSkillGp;
	this.edit_skillgp_prep = wbCompEditSkillGpPrep;
	this.edit_skillgp_exec = wbCompEditSkillGpExec;
	
	this.get_skill = wbCompGetSkill;
	this.edit_skill_prep = wbCompEditSkillPrep;
	this.edit_skill_exec = wbCompEditSkillExec;
	this.edit_skill_scale_prep = wbCompEditSkillScalePrep;
	this.edit_new_skill_scale_prep = wbCompEditNewSkillScalePrep;
	this.edit_skill_frame = wbCompEditFramePrep;
	this.ins_skill_frame = wbCompInsFramePrep;
	this.ins_skill_prep = wbCompInsSkillPrep;
	this.ins_skill_scale_prep = wbCompInsSkillScalePrep;
	this.edit_skill_scale_exec = wbCompEditSkillScaleExec;
	this.ins_skill_exec = wbCompInsSkillExec;
	this.del_skill = wbCompDelSkill;
	this.upd_skill_que_prep = wbCompUpdateSkillQuestionPrep;
	this.upd_skill_que_exec = wbCompUpdateSkillQuestionExec;
	this.view_skill_que = wbCompViewSkillQuestion;
	this.srh_skill_simple = wbCompSearchSkillSimple;
	this.srh_skill_adv = wbCompSearchSkillAdvanced;
	
	this.get_trash_skill = wbCompGetTrashSkill;
	this.del_trash_skill = wbCompDelTrashSkill;
	
	this.cancel = wbCompCancel;
	
	this.get_skill_set_list = wbCompGetSkillSetList;
	this.get_skill_set = wbCompGetSkillSet;
	this.add_skill_set_prep = wbCompAddSkillSetPrep;
	this.add_skill_set_exec = wbCompAddSkillSetExec;
	this.del_skill_set = wbCompDelSkillSet;
	this.edit_skill_set_prep = wbCompEditSkillSetPrep;
	this.edit_skill_set_exec = wbCompEditSkillSetExec;
	this.del_skill_from_set = wbCompDelSkillFromSet;
	this.pick_skill_prep = wbCompPickSkillPrep;
	this.pick_skill_sub_prep = wbCompPickSkillSubPrep;
	this.pick_skill_sub_exec = wbCompPickSkillSubExec;
	
	this.get_svy_list = wbCompGetSurveyList;
	this.add_svy_sk_prep = wbCompAddSurveySkillPrep;
	this.add_svy_sk_sub_prep = wbCompAddSurveySkillSubPrep;
	this.add_svy_sk_sub_exec = wbCompAddSurveySkillSubExec;
	this.edit_svy_prep = wbCompEditSvyPrep;
	this.edit_svy_exec = wbCompEditSvyExec;
	this.add_svy_prep = wbCompAddSvyPrep;
	this.add_svy_exec = wbCompAddSvyExec;
	this.del_svy = wbCompDelSvy;
	this.view_svy = wbCompViewSvy;
	this.preview_svy = wbCompPreviewSurvey;
	this.view_skill_set = wbCompViewSkillSet;
	this.pick_skill_set = wbCompPickSkillSet;
	this.del_svy_sk = wbCompDelSvySkill;
	this.edit_skill_que_prep = wbCompEditSkillQuePrep;
	this.edit_skill_que_exec = wbCompEditSkillQueExec;
	
	this.get_ass_list = wbCompGetAssessmentList;
	this.get_ass_pg = wbCompGetAssessmentPage;
	this.add_ass_prep = wbCompAddAssessmentPrep;
	this.add_ass_exec = wbCompAddAssessmentExec;
	this.edit_ass_prep = wbCompEditAssessmentPrep;
	this.edit_ass_exec = wbCompEditAssessmentExec;
	this.del_ass = wbCompDeleteAssessment;
	this.get_ass_dtl = wbCompGetAssessmentDetail;
	this.pick_notify_prep = wbCompPickNotifyPrep;
	this.pick_notify_sub_prep = wbCompPickNotifySubPrep;
	this.pick_selected_usr = wbCompPickSelectedUser;
	this.pick_notify_exec = wbCompPickNotifyExec;
	this.pick_svy_prep = wbCompPickSvyPrep;
	this.pick_svy_exec = wbCompPickSvyExec;
	this.pick_ass_prep = wbCompPickAssesseePrep;
	this.pick_ass_sub_prep = wbCompPickAssesseeSubPrep;
	this.pick_ass_exec = wbCompPickAssesseeExec;
	this.del_assessor = wbCompDelAssessor;
	this.view_ass = wbCompViewAssessment;
	this.get_ass_res = wbCompGetAssessmentResult;
	this.get_usr_ass = wbCompGetUserAssessment;
	this.send_ass_prep = wbCompSendAssessmentPrep;
	this.send_ass_exec = wbCompSendAssessmentExec;
	this.view_ass_res = wbCompViewAssessorResult;
	this.save_ass_res = wbCompSaveAssessorResult;
	this.upd_ass_usr_info = wbCompUpdateAssessorUserInfo;
	this.upd_ass_usr_info_exec = wbCompUpdateAssessorUserInfoExec;
	
	this.get_comp_profile = wbCompGetCompareProfile;
	this.comp_usr_bet_prof = wbCompUserBetCompetencyProfile;
	this.comp_usr_bet_usr = wbCompUserBetUser;
	this.comp_prof_bet_prof = wbCompProfileBetProfile;
	this.pick_usr_prep = wbCompPickUserPrep;
	this.pick_usr_exec = wbCompPickUserExec;
	this.get_recommend_cos = wbCompGetRecommendCourse;
	this.comp_skill_gap = wbCompCompareSkillGap;
	this.comp_skill_gap_url = wbCompCompareSkillGapURL;
	this.show_usr_comp = wbCompShowUserCompetency;
	this.show_usr_comp_url = wbCompShowUserCompetencyURL;
	this.get_itm_skill = wbCompGetItemSkill;
	
	this.srh_sk_prep = wbCompSearchSkillPrep;
	this.srh_sk_url = wbCompSearchSkillURL;
	this.srh_sk_exec = wbCompSearchSkillExec;
	this.pick_srh_sks = wbCompPickSearchSkillSet;
	this.pick_srh_sk_prep = wbCompPickSearchSkillPrep;
	this.pick_srh_sk_sub_prep = wbCompPickSearchSkillSubPrep;
	this.pick_srh_sk_exec = wbCompPickSearchSkillExec;
	this.rem_srh_sk = wbCompRemoveSearchSkill;
	this.get_srh_usr_sk = wbCompGetSearchUserSkill;
	
	this.sort_by_ftn = wbCompSortByFunction;
	this.copy_node = wbCompCopyNode;
	this.paste_node = wbCompPasteNode;
}


/* public method */
function wbCompGetAllScale(cur_page,lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale_list","all","N","cur_page",cur_page,"pagesize","20","stylesheet",'all_scale.xsl',"order_by","ssl_title");
	window.location.href = url;
}

function wbCompGetScale(scale_id,title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale","scale_id",scale_id,"stylesheet",'manage_scale.xsl');
	parent.location.href = url;
}

function wbCompAddScalePrep(lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale_list","all","Y","stylesheet",'ins_scale.xsl');
	parent.location.href = url;
}

function wbCompAddScaleExec(frm,lang){
	if(!_wbCompCheckFormValidate(frm,lang))
		return;
	num = _checkNumOfScaleLevel(frm);
	if (num<2){
		alert(eval("wb_msg_"+lang+"_at_least_two_lvl"));
		return;
	}
	_wbCompSubmitForm(frm,"cmd","ins_scale",
		"module","competency.CompetencyModule",
		"level_total",num,
		"url_success",_wbCompGetPrevNavigation(wb_nav),
		"url_failure",parent.location.href);
}

function wbCompEditScalePrep(scale_id,lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale","scale_id",scale_id,"stylesheet",'edit_scale.xsl');
	parent.location.href = url;
}

function wbCompEditScaleExec(frm,id,timestamp,lang){
	if(!_wbCompCheckFormValidate(frm,lang))
		return;
	num = _checkNumOfScaleLevel(frm);
	if (num<2){
		alert(eval("wb_msg_"+lang+"_at_least_two_lvl"));
		return;
	}
	_wbCompSubmitForm(frm,"cmd","upd_scale",
		"module","competency.CompetencyModule",
		"level_total",num,
		"timestamp",timestamp,
		"scale_id",id,
		"url_success",_wbCompGetPrevNavigation(wb_nav,frm.scale_title.value),
		"url_failure",parent.location.href);
}

function wbCompDelScale(scale_id,timestamp,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_scale")))
		return;
	url_success = _wbCompGetPrevNavigation(wb_nav);
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_scale","scale_id",scale_id,"timestamp",timestamp,"url_success",url_success,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompSearchScaleSimple(frm,lang){
	if(!gen_validate_empty_field(frm.search,eval('wb_msg_'+lang+'_search_field'),lang)){
		frm.search.focus()
		return;
	}
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "search_scale_simple";
	frm.ssl_title.value = frm.search.value;
	frm.stylesheet.value = 'scale_srh_simple_res.xsl';
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

function wbCompSearchScaleAdvanced(frm,lang){
	if (frm == null){
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet",'scale_srh_advanced.xsl');
		parent.location.href = url;
	}else{
		frm.module.value = "competency.CompetencyModule";
		frm.cmd.value = "search_scale_adv";
		frm.stylesheet.value = 'scale_srh_simple_res.xsl';
		frm.action = wb_utils_disp_servlet_url;
		frm.method = "get";
		frm.submit();
	}
}

function wbCompGetAllSkillGp(cur_page,lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","cur_page",cur_page,"pagesize","10","stylesheet",'all_comp.xsl');
	window.location.href = url;
}

function wbCompGetSkillGp(title,parent_id,cur_page){
	wb_utils_set_cookie("url_home",parent.location.href);
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","parent_id",parent_id,"cur_page",cur_page,"pagesize","20","stylesheet",'manage_comp.xsl');
	parent.location.href = url;
}

function wbCompAddSkillGpPrep(id,lang){
	wb_utils_set_cookie("parent_id",id);
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet",'ins_comp.xsl');
	parent.location.href = url;
}

function wbCompAddSkillGpExec(frm,lang){
	if (!_wbCompCheckForm(frm)){
		alert(eval("wb_msg_"+lang+"_enter_value")+" "+eval("wb_msg_"+lang+"_tnd_title"));
		return;
	}
	parent_id = wb_utils_get_cookie("parent_id");
	if (parent_id == null || parent_id == "")
		parent_id = 0;
	_wbCompSubmitForm(frm,"cmd","ins_node",
		"module","competency.CompetencyModule",
		"parent_id",parent_id,
		"url_success",_wbCompGetPrevNavigation(wb_nav),
		"url_failure",parent.location.href);
}

function wbCompDelSkillGp(id,timestamp,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_cata")))
		return;
	url_success = _wbCompGetPrevNavigation(wb_nav);
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_node","node_id",id,"timestamp",timestamp,"url_success",url_success,"url_failure",self.location.href);
	parent.location.href = url;
}

function wbCompEditSkillGpPrep(id,lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node","node_id",id,"stylesheet",'upd_comp.xsl');
	parent.location.href = url;
}

function wbCompEditSkillGpExec(frm,lang){
	if (!_wbCompCheckForm(frm)){
		alert(eval("wb_msg_"+lang+"_enter_value")+" "+eval("wb_msg_"+lang+"_tnd_title"));
		return;
	}
	_wbCompSubmitForm(frm,"cmd","upd_node",
		"module","competency.CompetencyModule",
		"url_success",_wbCompGetPrevNavigation(wb_nav,frm.title.value),
		"url_failure",parent.location.href);
}

function wbCompCopyNode(node_id,timestamp,type,lang){
	wb_utils_set_cookie("copy_node_id",node_id);
	wb_utils_set_cookie("copy_node_timestamp",timestamp);
	wb_utils_set_cookie("copy_node_type",type);
	alert(eval("wb_msg_"+lang+"_copy_ent"));
}

function wbCompPasteNode(parent_id,lang){
	copy_node_id = wb_utils_get_cookie("copy_node_id");
	copy_node_timestamp = wb_utils_get_cookie("copy_node_timestamp");
	copy_node_type = wb_utils_get_cookie("copy_node_type");
	if(copy_node_id == "" || copy_node_id == null){
		alert(eval("wb_msg_"+lang+"_copy_skgrp_first"));
		return;
	}
	if(copy_node_id == parent_id){
		alert(wb_msg_en_paste_same_grp);
		return;
	}
	if(copy_node_type=="SKILLGRP")
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","paste_node","node_id",copy_node_id,"parent_id",parent_id,"timestamp",copy_node_timestamp,"url_success",parent.location.href,"url_failure",parent.location.href);
	else if(copy_node_type=="SKILL")
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","paste_skill","skill_id",copy_node_id,"parent_id",parent_id,"timestamp",copy_node_timestamp,"url_success",parent.location.href,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompUpdateSkillQuestionPrep(skill_id,skill_title){
	if (skill_id==null)
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_q","url_failure","../htm/close_window.htm","stylesheet",'edit_skill_que.xsl');
	else
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_q","skill_id",skill_id,"skill_title",skill_title,"url_failure","../htm/close_window.htm","stylesheet",'edit_skill_que.xsl');
	_wbCompOpenWindow(url,480,780);
}

function wbCompUpdateSkillQuestionExec(frm){
	wb_utils_set_cookie("assesseeQ",frm.assesseeQText.value);
	wb_utils_set_cookie("assessorQ",frm.assessorQText.value);
	window.close();
}

function wbCompViewSkillQuestion(skill_id,skill_title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_q","skill_id",skill_id,"skill_title",skill_title,"url_failure","../htm/close_window.htm","stylesheet",'view_skill_que.xsl');
	_wbCompOpenWindow(url,480,780);
}

function wbCompSearchSkillSimple(frm,lang){
	if(!gen_validate_empty_field(frm.search,eval('wb_msg_'+lang+'_search_field'),lang)){
		frm.search.focus()
		return;
	}
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "search_skill_simple";
	frm.skl_title.value = frm.search.value;
	frm.stylesheet.value = 'sk_srh_simple_res.xsl';
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

function wbCompSearchSkillAdvanced(frm,lang){
	if (frm==null){
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet",'sk_srh_advanced.xsl');
		parent.location.href = url;
	}else{
		frm.module.value = "competency.CompetencyModule";
		frm.cmd.value = "search_skill_adv";
		frm.stylesheet.value = 'sk_srh_simple_res.xsl';
		frm.action = wb_utils_disp_servlet_url;
		frm.method = "get";
		frm.submit();
	}
}

function wbCompGetSkill(skill_id,title,stylesheet){
	if(stylesheet==""||stylesheet==null)
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill","skill_id",skill_id,"stylesheet",'view_skill.xsl');
	else
		url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill","skill_id",skill_id,"stylesheet",stylesheet);
	parent.location.href = url;
}

function wbCompEditSkillPrep(skill_id,lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill","skill_id",skill_id,"stylesheet",'upd_skill.xsl');
	return url;
}

function wbCompEditSkillScalePrep(skill_id,scale_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill","skill_id",skill_id,"scale_id",scale_id,"all","Y","stylesheet",'view_scale.xsl');
	return url;
}

function wbCompEditNewSkillScalePrep(skill_id,scale_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill","skill_id",skill_id,"scale_id",scale_id,"all","Y","stylesheet",'upd_scale.xsl');
	self.location.href = url;
}

function wbCompEditSkillExec(frm,title,description,lang){
	if(!_wbCompCheckFormValidate(frm,lang))
		return;
	num = _checkNumOfScaleLevel(frm);
	if (num<2){
		alert(eval("wb_msg_"+lang+"_at_least_two_lvl"));
		return;
	}
	if (title=="" || description==""){
		alert(eval("wb_msg_"+lang+"_enter_title"));
		return;
	}
	frm.target = "_parent";
	_wbCompSubmitForm(frm,"cmd","upd_skill",
		"module","competency.CompetencyModule",
		"title",title,
		"level_total",num,
		"description",description,
		"assesseeQ",wb_utils_get_cookie("assesseeQ"),
		"assessorQ",wb_utils_get_cookie("assessorQ"),
		"url_success",_wbCompGetPrevNavigation(wb_nav,title),
		"url_failure",parent.location.href);
}

function wbCompDelSkill(skill_id,timestamp,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_skill")))
		return;
	url_success = _wbCompGetPrevNavigation(wb_nav);
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_skill","skill_id",skill_id,"timestamp",timestamp,"url_success",url_success,"url_failure",parent.location.href);
	self.location.href = url;
}


function wbCompEditFramePrep(skill_id,scale_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet",'edit_skill_frame.xsl',"skill_id",skill_id,"scale_id",scale_id);
	parent.location.href = url;
}

function wbCompInsFramePrep(parent_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet",'ins_skill_frame.xsl',"parent_id",parent_id);
	parent.location.href = url;
}

function wbCompInsSkillPrep(parent_id,lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet",'ins_skill.xsl',"parent_id",parent_id);
	return url;
}

function wbCompInsSkillScalePrep(parent_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale_list","all","Y","stylesheet",'ins_skill_scale.xsl',"parent_id",parent_id);
	return url;
}

function wbCompEditSkillScaleExec(scale_id){
	if (scale_id=="")
		return;
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale_list","all","Y","stylesheet",'edit_skill_scale.xsl',"parent_id",parent_id,"scale_id",scale_id);
	self.location.href = url;
}

function wbCompInsSkillExec(frm,title,description,parent_id,lang){
	if(!_wbCompCheckFormValidate(frm,lang))
		return;
	num = _checkNumOfScaleLevel(frm);
	if (num<2){
		alert(eval("wb_msg_"+lang+"_at_least_two_lvl"));
		return;
	}
	if (title=="" || description==""){
		alert(eval("wb_msg_"+lang+"_enter_title"));
		return;
	}
	frm.target = "_parent";
	_wbCompSubmitForm(frm,"cmd","ins_skill",
		"module","competency.CompetencyModule",
		"level_total",num,
		"parent_id",parent_id,
		"title",title,
		"description",description,
		"assesseeQ",wb_utils_get_cookie("assesseeQ"),
		"assessorQ",wb_utils_get_cookie("assessorQ"),
		"url_success",_wbCompGetPrevNavigation(wb_nav),
		"url_failure",parent.location.href);
}

function wbCompGetTrashSkill(cur_page,lang){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","cur_page",cur_page,"pagesize","10","stylesheet",'manage_trash.xsl');
	parent.location.href = url;
}

function wbCompDelTrashSkill(frm){
	skill_lst = _wbCompGetCheckedList(frm,true,true,"name");
	if (skill_lst=="")
		return;
	timestamp_lst = _wbCompGetCheckedList(frm,true,true,"value");
	_wbCompSubmitForm(frm,"cmd","trash_skill",
		"module","competency.CompetencyModule",
		"skill_id",skill_lst,
		"timestamp",timestamp_lst,
		"url_success",parent.location.href,
		"url_failure",parent.location.href);
}

function wbCompCancel(nm){
	parent.location.href = _wbCompGetPrevNavigation(nm);
}

function wbCompGetSkillSetList(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKP","stylesheet",'sks_get_list.xsl',"order_by","title","sort_by","ASC");
	_wbCompInitNavigation("manage_skill_set_","Competency Profile",url);
	parent.location.href = url;
}

function wbCompAddSkillSetPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","stylesheet",'sks_add_set.xsl',"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompAddSkillSetExec(frm,count,lang){
	if (frm.sks_title.value=="" || frm.sks_title.value==null || count==0){
		alert(eval("wb_msg_"+lang+"_plz_pick_sk"));
		return;
	}
	var list = new Array();
	list = _wbCompGetListArray(frm,true,"level_","priority_");
	if (list == null)
		return;
	_wbCompSubmitForm(frm,"cmd","ins_skill_set",
		"module","competency.CompetencyModule",
		"skl_id_lst",list[0],
		"level_lst",list[1],
		"priority_lst",list[2],
		"url_success",_wbCompGetPrevNavigation("manage_skill_set_"),
		"url_failure",parent.location.href);
}

function wbCompDelSkillSet(frm,lang){
	sks_id_lst = _wbCompGetCheckedList(frm,true,true,"name");
	if (sks_id_lst == "")
		return;
	if(!confirm(eval("wb_msg_"+lang+"_rm_comp_prof")))
		return;
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_skill_set","sks_id_lst",sks_id_lst,"url_success",parent.location.href,"url_failure",parent.location.href);
	window.location.href = url;
}

function wbCompGetSkillSet(sks_id,title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","sks_id",sks_id,"stylesheet",'sks_get_set.xsl',"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompEditSkillSetPrep(sks_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","sks_id",sks_id,"stylesheet",'sks_upd_set.xsl');
	parent.location.href = url;
}

function wbCompEditSkillSetExec(frm,sks_id,sks_title,count){
	if (count == "0" || frm.sks_title.value=="" || frm.sks_title.value==null)
		return;
	var list = new Array();
	list = _wbCompGetListArray(frm,true,"level_","priority_");
	if (list == null)
		return;
	_wbCompSubmitForm(frm,"cmd","upd_skill_set",
		"module","competency.CompetencyModule",
		"sks_id",sks_id,
		"skl_id_lst",list[0],
		"level_lst",list[1],
		"priority_lst",list[2],
		"url_success",_wbCompGetPrevNavigation("manage_skill_set_",frm.sks_title.value),
		"url_failure",parent.location.href);
}

function wbCompDelSkillFromSet(frm,title,url_success){
	var list = new Array();
	list = _wbCompGetListArray(frm,false,"level_","priority_");
	if (list==null)
		return;
	_wbCompSubmitForm(frm,"cmd","pick_skill",
		"module","competency.CompetencyModule",
		"picked_skl_id_lst",list[0],
		"level_lst",list[1],
		"priority_lst",list[2],
		"url_success",url_success,
		"url_failure",parent.location.href);
}

function wbCompPickSkillPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","cur_page","1","pagesize","10","stylesheet",'sks_pick_list.xsl',"url_failure","../htm/close_window.htm");
	//url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet","sks_add_set_frame.xsl");
	_wbCompInitNavigation("manage_pick_skill_","All",url);
	_wbCompOpenWindow(url,480,780);
}

function wbCompPickSkillSubPrep(title,node_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","parent_id",node_id,"cur_page","1","pagesize","10","stylesheet",'sks_pick_sub_list.xsl',"url_failure",parent.location.href);
	self.location.href = url;
}

function wbCompPickSkillSubExec(frm1,frm2){
	skl_id_lst = _wbCompGetCheckedList(frm2,true,true,"name");
	if (skl_id_lst=="")
		return;
	var list = new Array();
	list = _wbCompGetListArray(frm1,true,"level_","priority_");
	_wbCompSubmitForm(frm2,"cmd","pick_skill",
		"module","competency.CompetencyModule",
		"sks_title",frm1.sks_title.value,
		"picked_skl_id_lst",list[0],
		"skl_id_lst",skl_id_lst,
		"level_lst",list[1],
		"priority_lst",list[2],
		"url_success","../htm/close_and_call_opener_function.htm");
}

function wbCompGetSurveyList(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKT","stylesheet",'comp_svy_get_list.xsl',"order_by","title","sort_by","ASC");
	_wbCompInitNavigation("maintain_svy_","Survey",url);
	parent.location.href = url;
}

function wbCompAddSurveySkillPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","cur_page","1","pagesize","10","stylesheet",'comp_svy_pick_list.xsl',"url_failure","../htm/close_window.htm");
	_wbCompInitNavigation("manage_pick_skill_","All",url);
	_wbCompOpenWindow(url,480,780);
}

function wbCompAddSurveySkillSubPrep(title,node_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","parent_id",node_id,"cur_page","1","pagesize","10","stylesheet",'comp_svy_pick_sub_list.xsl',"url_failure",parent.location.href);

	parent.location.href = url;
}

function wbCompAddSurveySkillSubExec(frm1,frm2){
	skl_id_lst = _wbCompGetCheckedList(frm2,true,true,"name");
	if (skl_id_lst=="")
		return;
	picked_skl_id_lst = frm1.picked_skl_id_lst.value;
	if (picked_skl_id_lst!="")
		picked_skl_id_lst = picked_skl_id_lst.substr(0,picked_skl_id_lst.length-1);
	_wbCompSubmitForm(frm2,"cmd","pick_survey_skill",
		"module","competency.CompetencyModule",
		"sks_comment_ind",frm1.sks_comment_ind.value,
		"sks_title",frm1.sks_title.value,
		"picked_skl_id_lst",picked_skl_id_lst,
		"skl_id_lst",skl_id_lst,
		"order_lst","",
		"url_success","../htm/close_and_call_opener_function.htm");
}

function wbCompEditSvyPrep(sks_id,title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_edit_svy.xsl');
	parent.location.href = url;
}

function wbCompPreviewSurvey(sks_id,title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_preview_svy.xsl');
	parent.location.href = url;
}

function wbCompEditSvyExec(frm,sks_id){
	if (frm.sks_title.value=="" || frm.sks_title.value==null)
		return;
	list = "";
	for (i=0; i<frm.sid.length; i++)
		list += frm.sid.options[i].value + "~";
	if (list == "")
		return;
	list = list.substr(0,list.length-1);
	_wbCompSubmitForm(frm,"cmd","upd_survey",
		"module","competency.CompetencyModule",
		"skl_id_lst",list,
		"sks_id",sks_id,
		"url_success",_wbCompGetPrevNavigation("maintain_svy_",frm.sks_title.value),
		"url_failure",parent.location.href);
}

function wbCompAddSvyPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","stylesheet",'comp_svy_ins_svy.xsl');
	parent.location.href = url;
}

function wbCompAddSvyExec(frm){
	if (frm.sks_title.value=="" || frm.sks_title.value==null)
		return;
	list = frm.picked_skl_id_lst.value;
	if (list== "")
		return;
	list = list.substr(0,list.length-1);
	_wbCompSubmitForm(frm,"cmd","ins_survey",
		"module","competency.CompetencyModule",
		"skl_id_lst",list,
		"url_success",_wbCompGetPrevNavigation("maintain_svy_"),
		"url_failure",parent.location.href);
}

function wbCompDelSvy(sks_id,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_svy")))
		return;
	url_success = _wbCompGetPrevNavigation("maintain_svy_");
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_survey","sks_id",sks_id,"url_success",url_success,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompViewSvy(){
	url = parent.location.href;
	url = setUrlParam("show_skill_path","false",url);
	url = setUrlParam("stylesheet",'comp_svy_view_svy.xsl',url);
	_wbCompOpenWindow(url,480,780);
}

function wbCompViewSkillSet(id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","sks_id",id,"stylesheet",'comp_svy_view_sks.xsl');
	_wbCompOpenWindow(url,480,780);
}

function wbCompPickSkillSet(frm,url_success){
	_wbCompSubmitForm(frm,"cmd","pick_skill_set",
		"module","competency.CompetencyModule",
		"sks_id",frm.id.value,
		"order_lst","",
		"url_success",url_success,
		"url_failure",parent.location.href);
}

function wbCompDelSvySkill(picked_skl_id_lst,url_success,frm){
	_wbCompSubmitForm(frm,"cmd","pick_survey_skill",
		"module","competency.CompetencyModule",
		"picked_skl_id_lst",picked_skl_id_lst,
		"skl_id_lst","",
		"order_lst","",
		"url_success",url_success);
}

function wbCompEditSkillQuePrep(skl_id,sks_id, skl_title){
	wb_utils_set_cookie("skl_id",skl_id);
	wb_utils_set_cookie("skl_title",skl_title);
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","getQ","skl_id",skl_id,"sks_id",sks_id,"stylesheet",'comp_svy_edit_que.xsl');
	_wbCompOpenWindow(url,480,780);
}

function wbCompEditSkillQueExec(frm){
	if (frm.assessee[0].checked)
		frm.assesseeQ.value = frm.assesseeQText.value;
	if (frm.assessor[0].checked)
		frm.assessorQ.value = frm.assessorQText.value;
	_wbCompSubmitForm(frm,"cmd","saveQ",
		"module","competency.CompetencyModule",
		"skl_id",wb_utils_get_cookie("skl_id"),
		"url_success","../htm/close_window.htm",
		"url_failure",parent.location.href);
}

function wbCompGetAssessmentList(prepared,notified,collected,resolved){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","refresh_ass_list","prepared",prepared,"collected",collected,"notified",notified,"resolved",resolved,"pagesize","10","cur_page","1","sort_by","asc","order_by","asm_eff_start_datetime","stylesheet",'comp_ass_get_list.xsl');
	_wbCompInitNavigation("maintain_ass_","Assessment",url);
	parent.location.href = url;
}

function wbCompGetAssessmentPage(cur_page,prepared,notified,collected,resolved){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_list","pagesize","10","cur_page",cur_page,"sort_by","asc","order_by","asm_title","stylesheet",'comp_ass_get_list.xsl',"prepared",prepared,"notified",notified,"collected",collected,"resolved",resolved);
	parent.location.href = url;
}

function wbCompAddAssessmentPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_assessment","refresh","true","stylesheet",'comp_ass_ins_ass.xsl');
	parent.location.href = url;
}

function wbCompAddAssessmentExec(frm){
	if (frm.usr_ent_id.value=="" || frm.sks_id.value=="")
		return;
	_wbCompSubmitForm(frm,"cmd","ins_assessment",
		"module","competency.CompetencyModule",
		"review_start",_wbCompGetDate(frmXml,"review_start"),
		"review_end",_wbCompGetDate(frmXml,"review_end"),
		"notify_date",_wbCompGetDate(frmXml,"notify_date"),
		"due_date",_wbCompGetDate(frmXml,"due_date"),
		"url_failure",parent.location.href,
		"url_success",_wbCompGetPrevNavigation("maintain_ass_"));
}

function wbCompEditAssessmentPrep(asm_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_assessment","refresh","true","asm_id",asm_id,"stylesheet",'comp_ass_ins_ass.xsl');
	parent.location.href = url;
}

function wbCompEditAssessmentExec(frm,asm_id,assessee){
	if (frm.usr_ent_id.value=="" || frm.sks_id.value=="")
		return;
	_wbCompSubmitForm(frm,"cmd","upd_assessment",
		"module","competency.CompetencyModule",
		"asm_id",asm_id,
		"review_start",_wbCompGetDate(frmXml,"review_start"),
		"review_end",_wbCompGetDate(frmXml,"review_end"),
		"notify_date",_wbCompGetDate(frmXml,"notify_date"),
		"due_date",_wbCompGetDate(frmXml,"due_date"),
		"url_failure",parent.location.href,
		"url_success",_wbCompGetPrevNavigation("maintain_ass_",assessee));
}

function wbCompDeleteAssessment(asm_id,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_ass")))
		return;
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_assessment","asm_id",asm_id,"url_success",_wbCompGetPrevNavigation("maintain_ass_"));
	parent.location.href = url;
}

function wbCompGetAssessmentDetail(asm_id,title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_detial","asm_id",asm_id,"stylesheet",'comp_ass_get_ass.xsl');
	parent.location.href = url;
}

function wbCompPickNotifyPrep(usg_ent_id,asm_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ent_lst","cur_page","1","pagesize","10","usg_ent_id",usg_ent_id,"asm_id",asm_id,"stylesheet",'comp_ass_pick_usr.xsl');
	_wbCompInitNavigation("pick_usr_","All",url);
	_wbCompOpenWindow(url,480,780);
}

function wbCompPickNotifySubPrep(usg_ent_id,title,asm_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ent_lst","cur_page","1","pagesize","10","usg_ent_id",usg_ent_id,"asm_id",asm_id,"stylesheet",'comp_ass_pick_usr.xsl');
	parent.location.href = url;
}

function wbCompPickSelectedUser(frm,asm_id){
	usr_id_lst = _wbCompGetCheckedList(frm,true,true,"name");
	if (usr_id_lst=="")
		return;
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","selected_user","usr_ent_id_lst",usr_id_lst,"asm_id",asm_id,"stylesheet",'comp_ass_upd_usr.xsl');
	parent.location.href = url;
}

function wbCompPickNotifyExec(frm,asm_id,count){
	usr_type_lst = _wbCompGetTextList(frm,"usr_type_",count);
	usr_weight_lst = _wbCompGetTextList(frm,"usr_weight_",count);
	usr_ent_id_lst = frm.usr_ent_id_lst.value;
	usr_ent_id_lst = usr_ent_id_lst.substr(0,usr_ent_id_lst.length-1);
	_wbCompSubmitForm(frm,"cmd","pick_user",
		"module","competency.CompetencyModule",
		"usr_ent_id_lst",usr_ent_id_lst,
		"usr_type_lst",usr_type_lst,
		"usr_weight_lst",usr_weight_lst,
		"asm_id",asm_id,
		"url_failure",parent.location.href,
		"url_success","../htm/close_and_reload_window.htm");
}

function wbCompPickSvyPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKT","order_by","title","sort_by","ASC","stylesheet",'comp_ass_pick_svy.xsl',"url_failure","../htm/close_window.htm");
	_wbCompOpenWindow(url,480,780);
}

function wbCompPickSvyExec(frm){
	sks_id = _wbCompGetCheckedList(frm,true,false,"name");
	if (sks_id=="")
		return;
	sks_title = _wbCompGetCheckedList(frm,true,false,"value");
	_wbCompSubmitForm(frm,"cmd","pick_survey",
		"module","competency.CompetencyModule",
		"usr_ent_id",window.opener.frmXml.usr_ent_id.value,
		"sks_id",sks_id,
		"sks_title",sks_title,
		"review_start",_wbCompGetDate(window.opener.frmXml,"review_start"),
		"review_end",_wbCompGetDate(window.opener.frmXml,"review_end"),
		"notify_date",_wbCompGetDate(window.opener.frmXml,"notify_date"),
		"due_date",_wbCompGetDate(window.opener.frmXml,"due_date"),
		"url_failure",parent.location.href,
		"url_success","../htm/close_and_call_opener_function.htm");
}

function wbCompPickAssesseePrep(usg_ent_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ent_lst","cur_page","1","pagesize","10","usg_ent_id",usg_ent_id,"stylesheet",'comp_ass_pick_usr.xsl');
	_wbCompInitNavigation("pick_usr_","All",url);
	_wbCompOpenWindow(url,480,780);
}

function wbCompPickAssesseeSubPrep(usg_ent_id,title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ent_lst","cur_page","1","pagesize","10","usg_ent_id",usg_ent_id,"stylesheet",'comp_ass_pick_usr.xsl');
	parent.location.href = url;
}

function wbCompPickAssesseeExec(frm){
	usr_id = _wbCompGetCheckedList(frm,true,false,"name");
	if (usr_id=="")
		return;
	_wbCompSubmitForm(frm,"cmd","pick_user",
		"module","competency.CompetencyModule",
		"usr_ent_id",usr_id,
		"sks_id",window.opener.frmXml.sks_id.value,
		"sks_title",window.opener.frmXml.sks_title.value,
		"review_start",_wbCompGetDate(window.opener.frmXml,"review_start"),
		"review_end",_wbCompGetDate(window.opener.frmXml,"review_end"),
		"notify_date",_wbCompGetDate(window.opener.frmXml,"notify_date"),
		"due_date",_wbCompGetDate(window.opener.frmXml,"due_date"),
		"url_failure",parent.location.href,
		"url_success","../htm/close_and_call_opener_function.htm");
}

function wbCompDelAssessor(frm,asm_id){
	usr_ent_id_lst = _wbCompGetCheckboxLst(frm.assessor_id);
	if (usr_ent_id_lst=="")
		return;
	_wbCompSubmitForm(frm,"cmd","del_assessor",
		"module","competency.CompetencyModule",
		"usr_ent_id_lst",usr_ent_id_lst,
		"asm_id",asm_id,
		"url_failure",parent.location.href,
		"url_success",parent.location.href);
}

function wbCompViewAssessment(asm_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","preview_ass","asm_id",asm_id,"stylesheet",'comp_ass_view_ass.xsl');
	_wbCompOpenWindow(url,480,780);
}

function wbCompGetAssessmentResult(asm_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_result","asm_id",asm_id,"stylesheet",'comp_ass_get_res.xsl');
	parent.location.href = url;
}

function wbCompGetUserAssessment(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assigned_ass","stylesheet",'comp_ass_get_usr_ass.xsl');
	_wbCompInitNavigation("conduct_ass_","Conduct Assessment",url);
	parent.location.href = url;
}

function wbCompSendAssessmentPrep(asm_id,usr_ent_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assessor_result","asm_id",asm_id,"usr_ent_id",usr_ent_id,"stylesheet",'comp_ass_submit_ass.xsl');
	wb_utils_set_cookie("url_prev",parent.location.href);
	parent.location.href = url;
}

function wbCompSendAssessmentExec(frm,count,send){
	_wbCompSubmitForm(frm,"cmd","submit_ass",
		"module","competency.CompetencyModule",
		"ass_submit",send,
		"total_section",count,
		"url_success","Javascript:wb_utils_gen_home()",
		"url_failure",parent.location.href);
}

function wbCompViewAssessorResult(asm_id,usr_ent_id,usr){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assessor_result","asm_id",asm_id,"usr_ent_id",usr_ent_id,"stylesheet",'comp_ass_view_res.xsl');
	parent.location.href = url;
}

function wbCompSaveAssessorResult(frm,asm_id,count){
	score_lst = _wbCompGetTextList(frm,"que_",count);
	if (_checkInputGTMaxLvl(frm,"que_","sle_max_lvl_",count))
		return;
	if (score_lst == "")
		return;
	_wbCompSubmitForm(frm,"cmd","save_resolved",
		"module","competency.CompetencyModule",
		"asm_id",asm_id,
		"score_lst",score_lst,
		"url_success",_wbCompGetPrevNavigation("maintain_ass_"),
		"url_failure",parent.location.href);
}

function wbCompUpdateAssessorUserInfo(asm_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_detial","asm_id",asm_id,"stylesheet",'comp_ass_upd_usr_info.xsl');
	parent.location.href = url;
}

function wbCompUpdateAssessorUserInfoExec(frm,asm_id){
	usr_ent_id_lst = "";
	usr_type_lst = "";
	usr_weight_lst = "";
	invalid = false;
	for (i=0; i<frm.elements.length; i++)
		if (frm.elements[i].type=="hidden"){
			usr_ent_id_lst += frm.elements[i].name + "~";
			if (eval("frm.status_"+frm.elements[i].name+".value")=="")
				invalid = true;
			usr_type_lst += eval("frm.status_"+frm.elements[i].name+".value") + "~";
			if (eval("frm.weight_"+frm.elements[i].name+".value")=="")
				invalid = true;
			usr_weight_lst += eval("frm.weight_"+frm.elements[i].name+".value") + "~";
		}
	if (usr_ent_id_lst.indexOf("~")>-1){
		usr_ent_id_lst = usr_ent_id_lst.substr(0,usr_ent_id_lst.length-1);
		usr_type_lst = usr_type_lst.substr(0,usr_type_lst.length-1);
		usr_weight_lst = usr_weight_lst.substr(0,usr_weight_lst.length-1);
	}
	if (invalid)
		return;
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","upd_au_list","usr_ent_id_lst",usr_ent_id_lst,"usr_type_lst",usr_type_lst,"usr_weight_lst",usr_weight_lst,"asm_id",asm_id,"url_failure",parent.location.href,"url_success",_wbCompGetPrevNavigation("maintain_ass_"));
	parent.location.href = url;
}

function wbCompGetCompareProfile(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","url_failure",parent.location.href,"stylesheet",'sk_ana_get_prof.xsl');
	_wbCompInitNavigation("skill_analysis_","Skill Analysis",url);
	parent.location.href = url;
}

function wbCompUserBetCompetencyProfile(source_id,target_id,frm,src_type){
	if (source_id==null || target_id==null || frm == null){
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_ent_prof_gap","stylesheet",'sk_ana_usr_prof.xsl');
	}else if(src_type=="USR"){
		for (i=0; i<frm.type.length; i++)
			if (frm.type[i].checked)
				url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_ent_prof_gap","source_id",source_id,"target_id",target_id,frm.type[i].value,eval("frm."+frm.type[i].value+".value"),"stylesheet",'sk_ana_usr_prof.xsl');
	}else{
		cur_date = _wbCompGetDate(frm,"cur_date");
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_ent_prof_gap","source_id",source_id,"target_id",target_id,"source_date",cur_date,"stylesheet",'sk_ana_usr_prof.xsl');
	}
	parent.location.href = url;
}

function wbCompUserBetUser(frm,src_type,tgt_type){
	if (frm==null){
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_ent_gap","stylesheet",'sk_ana_usr_grp.xsl');		
	}else{
		if (src_type=="USR"){
			for (i=0; i<frm.src_type.length; i++)
				if (frm.src_type[i].checked){
					src_tag = frm.src_type[i].value;
					src_val = eval("frm."+frm.src_type[i].value+".value");
				}
		}else if (src_type=="USG"){
			src_tag = "source_date";
			src_val = _wbCompGetDate(frm,"src_date");
		}
		if (tgt_type=="USR"){
			for (i=0; i<frm.tgt_type.length; i++)
				if (frm.tgt_type[i].checked){
					tgt_tag = frm.tgt_type[i].value;
					tgt_val = eval("frm."+frm.tgt_type[i].value+".value");
				}
		}else if (tgt_type=="USG"){
			tgt_tag = "target_date";
			tgt_val = _wbCompGetDate(frm,"tgt_date");
		}
		source_id = getUrlParam("source_id");
		target_id = getUrlParam("target_id");
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_ent_gap","source_id",source_id,"target_id",target_id,src_tag,src_val,tgt_tag,tgt_val,"stylesheet",'sk_ana_usr_grp.xsl');
	}
	parent.location.href = url;
}

function wbCompProfileBetProfile(frm){
	if (frm==null){
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_prof_gap","stylesheet",'sk_ana_prof_prof.xsl');
	}else
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_prof_gap","source_id",frm.source_id.value,"target_id",frm.target_id.value,"stylesheet",'sk_ana_prof_prof.xsl');
	parent.location.href = url;
}

function wbCompPickUserPrep(usg_ent_id,id_type,usg_title){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ent_lst","cur_page","1","pagesize","10","usg_ent_id",usg_ent_id,"stylesheet",'sk_ana_pick_usr.xsl',"type",id_type);
	if (usg_ent_id == "0"){
		_wbCompInitNavigation("pick_usr_","All",url);
		_wbCompOpenWindow(url,480,780);
	}else{
		parent.location.href = url;
	}
}

function wbCompPickUserExec(frm,id_type){
	id = "";
	if (frm.ent_id.length==null && frm.ent_id.checked)
		id = frm.ent_id.value;
	else
		for (i=0; i<frm.ent_id.length; i++)
			if (frm.ent_id[i].checked)
				id = frm.ent_id[i].value;
	if (id=="")
		return;
	url = window.opener.location.href;
	if (getUrlParam(id_type,"window.opener")=="")
		url += "&" + id_type + "=" + id;
	else
		url = setUrlParam(id_type,id,url);
	if (id_type=="source_id")
		if (getUrlParam("source_type","window.opener")=="")
			url += "&source_type=USR";
		else
			url = setUrlParam("source_type","USR",url);
	else if (id_type=="target_id")
		if (getUrlParam("target_type","window.opener")=="")
			url += "&target_type=USR";
		else
			url = setUrlParam("target_type","USR",url);
	window.opener.location.href = url;
	self.close();
}

function wbCompCompareSkillGapURL(){
	url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_skill_gap","stylesheet",'sk_ana_compare.xsl');
	_wbCompInitNavigation("skill_analysis_","Skill Inventory",url);

	return url;
}

function wbCompGetRecommendCourse(usr_ent_id,skl_id){
	url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_recommend_itm","usr_ent_id",usr_ent_id,"skl_id",skl_id,"stylesheet","sk_ana_recommend_cos.xsl");
	wb_utils_open_win(url,'RecommendCosWin',600,400);
}

function wbCompCompareSkillGap(frm){
	if (frm==null){
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_skill_gap","stylesheet",'sk_ana_compare.xsl');
		_wbCompInitNavigation("skill_analysis_","Skill Inventory",url);

	}else{
		url = parent.location.href;
		for (i=0; i<frm.source_type.length; i++)
			if (frm.source_type[i].checked)
				source_type = frm.source_type[i].value;
		for (i=0; i<frm.target_type.length; i++)
			if (frm.target_type[i].checked)
				target_type = frm.target_type[i].value;
		if (getUrlParam("source_type")=="")
			url += "&source_type=" + source_type;
		else
			url = setUrlParam("source_type",source_type,url);
		if (source_type=="PROF"){
			if (getUrlParam("source_id")=="")
				url += "&source_id=" + frm.source_id.value;
			else
				url = setUrlParam("source_id",frm.source_id.value,url);
		}else if (source_type=="USR" && frm.source_asm_type.length>0){
			for (i=0; i<frm.source_asm_type.length; i++)
				if (frm.source_asm_type[i].checked)
					if (getUrlParam(frm.source_asm_type[i].value)=="")
						url += "&"+frm.source_asm_type[i].value+"="+eval("frm."+frm.source_asm_type[i].value+".value");
					else
						url = setUrlParam(frm.source_asm_type[i].value,eval("frm."+frm.source_asm_type[i].value+".value"),url);
		}else{
			if (getUrlParam("source_date")=="")
				url += "&source_date=" + _wbCompGetDate(frm,"src_date");
			else
				url = setUrlParam("source_date",_wbCompGetDate(frm,"src_date"),url);
		}
		if (getUrlParam("target_type")=="")
			url += "&target_type=" + target_type;
		else
			url = setUrlParam("target_type",target_type,url);
		if (target_type=="PROF"){
			if (getUrlParam("target_id")=="")
				url += "&target_id=" + frm.target_id.value;
			else
				url = setUrlParam("target_id",frm.target_id.value,url);
		}else if (target_type=="USR" && frm.target_asm_type.length>0){
			for (i=0; i<frm.target_asm_type.length; i++)
				if (frm.target_asm_type[i].checked)
					if (getUrlParam(frm.target_asm_type[i].value)=="")
						url += "&"+frm.target_asm_type[i].value+"="+eval("frm."+frm.target_asm_type[i].value+".value");
					else
						url = setUrlParam(frm.target_asm_type[i].value,eval("frm."+frm.target_asm_type[i].value+".value"),url);
		}else{
			if (getUrlParam("target_date")=="")
				url += "&target_date=" + _wbCompGetDate(frm,"tgt_date");
			else
				url = setUrlParam("target_date",_wbCompGetDate(frm,"tgt_date"),url);
		}
		url = setUrlParam("show_course_recomm","true",url);
		/*
		if(getUrlParam("show_course_recomm")==""){
			if (frm.show.checked)
				url += "&show_course_recomm=true";
			else
				url += "&show_course_recomm=false";
		}else{
			if (frm.show.checked)
				url = setUrlParam("show_course_recomm","true",url);
			else
				url = setUrlParam("show_course_recomm","false",url);
		}
		*/
	}
	parent.location.href = url;
}

function wbCompShowUserCompetency(usr_ent_id,frm,id_type){
	if (usr_ent_id==null || frm==null || id_type==null){
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_usr_skill","stylesheet",'sk_ana_show_comp.xsl');
		_wbCompInitNavigation("skill_analysis_","Skill Inventory",url);
	}else{
		if (id_type=="USR"){
			for (i=0; i<frm.asm_type.length; i++)
				if (frm.asm_type[i].checked)
					url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_usr_skill","usr_ent_id",usr_ent_id,frm.asm_type[i].value,eval("frm."+frm.asm_type[i].value+".value"),"stylesheet",'sk_ana_show_comp.xsl');
		}else{
			resolved_date = _wbCompGetDate(frm,"cur_date");
			url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_usr_skill","usr_ent_id",usr_ent_id,"resolved_date",resolved_date,"stylesheet",'sk_ana_show_comp.xsl');
		}
	}
	parent.location.href = url;
}

function wbCompShowUserCompetencyURL(usr_ent_id,frm,id_type){
	if (usr_ent_id==null || frm==null || id_type==null){
		url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_usr_skill","stylesheet",'sk_ana_show_comp.xsl');
		_wbCompInitNavigation("skill_analysis_","Skill Inventory",url);
	}else{
		if (id_type=="USR"){
			for (i=0; i<frm.asm_type.length; i++)
				if (frm.asm_type[i].checked)
					url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_usr_skill","usr_ent_id",usr_ent_id,frm.asm_type[i].value,eval("frm."+frm.asm_type[i].value+".value"),"stylesheet",'sk_ana_show_comp.xsl');
		}else{
			resolved_date = _wbCompGetDate(frm,"cur_date");
			url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_usr_skill","usr_ent_id",usr_ent_id,"resolved_date",resolved_date,"stylesheet",'sk_ana_show_comp.xsl');
		}
	}
	return url;
}

function wbCompGetItemSkill(itm_id,title){
	url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_item_skill","itm_id",itm_id,"stylesheet",'sk_ana_get_itm.xsl');

	parent.location.href = url;
}

function wbCompSearchSkillPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","true","stylesheet",'sk_srh_pick_sk.xsl');
	_wbCompInitNavigation("skill_search_","Skill Inventory",url);

	parent.location.href = url;
}

function wbCompSearchSkillURL(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","true","stylesheet",'sk_srh_pick_sk.xsl');
	_wbCompInitNavigation("skill_search_","Skill Search",url);
	return url;
}

function wbCompSearchSkillExec(frm){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","search_user","skl_id_lst",_wbCompGetCheckboxLst(frm.skill),"level_lst",_wbCompGetSelectLst(frm.skill,frm.tgt_lvl),"stylesheet",'sk_srh_get_res.xsl');

	parent.location.href = url;
}

function wbCompPickSearchSkillSet(frm,url_success){
	_wbCompSubmitForm(frm,"module","competency.CompetencyModule",
		"cmd","pick_search_skill_set",
		"picked_skl_id_lst",_wbCompGetCheckboxLst(frm.skill),
		"level_lst",_wbCompGetSelectLst(frm.skill,frm.tgt_lvl),
		"url_success",url_success,
		"url_failure",parent.location.href);
}

function wbCompPickSearchSkillPrep(){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","cur_page","1","pagesize","10","stylesheet",'sk_srh_pick_lst.xsl',"url_failure","../htm/close_window.htm");
	_wbCompInitNavigation("manage_pick_skill_","All",url);
	_wbCompOpenWindow(url,480,780);
}

function wbCompPickSearchSkillSubPrep(title,node_id){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_node_list","parent_id",node_id,"cur_page","1","pagesize","10","stylesheet",'sk_srh_pick_sub_lst.xsl',"url_failure",parent.location.href);

	parent.location.href = url;
}

function wbCompPickSearchSkillExec(frm1,frm2){
	skl_id_lst = _wbCompGetCheckedList(frm1,true,true,"name");
	if (skl_id_lst == "")
		return;
	_wbCompSubmitForm(frm1,"module","competency.CompetencyModule",
		"cmd","pick_search_skill",
		"picked_skl_id_lst",_wbCompGetCheckboxLst(frm2.skill),
		"level_lst",_wbCompGetSelectLst(frm2.skill,frm2.tgt_lvl),
		"skl_id_lst",skl_id_lst,
		"url_success","../htm/close_and_reload_window.htm");
}

function wbCompRemoveSearchSkill(frm){
	//var list = new Array();
	//list = _wbCompGetListArray(frm,false,"tgt_lvl_");
	_wbCompSubmitForm(frm,"module","competency.CompetencyModule",
		"cmd","pick_search_skill",
		"picked_skl_id_lst",_wbCompGetCheckboxLst(frm.skill),
		"level_lst",_wbCompGetSelectLst(frm.skill,frm.tgt_lvl),
		"url_success",parent.location.href,
		"url_failure",parent.location.href);
}

function wbCompGetSearchUserSkill(usr_id,usr_nm){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_user_skill","usr_ent_id",usr_id,"show_all","true","stylesheet",'sk_srh_get_usr_sk.xsl',"usr_name",usr_nm);
	_wbCompOpenWindow(url,480,780);
}

function wbCompSortByFunction(order_by, sort_by){
	url = parent.location.href;
	if (getUrlParam("sort_by")==null){
		if (sort_by=="DESC" || sort_by=="desc")
			url += "&sort_by=asc";
		else
			url += "&sort_by=desc";
	}else{
		if (sort_by=="DESC" || sort_by=="desc")
			url = setUrlParam("sort_by","asc",url);
		else
			url = setUrlParam("sort_by","desc",url);
	}
	if (getUrlParam("order_by")==null)
		url += "&order_by=" + order_by;
	else
		url = setUrlParam("order_by",order_by,url);
	parent.location.href = url;
}

/* private method */
function _wbCompCheckFormValidate(frm,lang){
	for(i=1;i<=8;i++){
		lvlValue = eval("frm.level_value_"+i);
		lvlLabel = eval("frm.level_label_"+i);
		lvlDesc = eval("frm.level_desc_"+i);
		lvlValue.value = wbUtilsTrimString(lvlValue.value);
		lvlLabel.value = wbUtilsTrimString(lvlLabel.value);
		lvlDesc.value = wbUtilsTrimString(lvlDesc.value);
		if(lvlValue && lvlLabel && lvlDesc && (lvlValue.value.length>0||lvlLabel.value.length>0||lvlDesc.value.length>0)){
			if(!gen_validate_positive_integer(lvlValue,"Level "+i+" value",lang))
				return false;
			if(!gen_validate_empty_field(lvlLabel,"Level "+i+" label",lang))
				return false;
			if(!gen_validate_empty_field(lvlDesc,"Level "+i+" description",lang))
				return false;
		}
	}
	return true;
}

function _checkNumOfScaleLevel(frm){
	count = 0;
	prev = 0;
	for(i=0; i<frm.elements.length; i++){
		if (frm.elements[i].type == "textarea" && frm.elements[i].value != "" && frm.elements[i].value != null){
			count++;
			str = frm.elements[i].name;
			prev = parseInt(str.substr(11,1));
			if (parseInt(prev)-count > 0)
				return -1;
		}else if(frm.elements[i].type=="text" && parseInt(frm.elements[i].value)<0){
			return -2;
		}
	}
	return count;
}

function _checkInputGTMaxLvl(frm,nm1,nm2,count){
	for (i=1; i<=count; i++){
		inputValue = parseInt(eval("frm."+nm1+i+".value"));
		maxLvlValue = parseInt(eval("frm."+nm2+i+".value"));
		if (inputValue>maxLvlValue){
			alert("The input value is greater than the maximum scale level");
			return true;
		}
	}
	return false;
}

function _wbCompSubmitForm(frm){
	for (i=1; i<arguments.length-1; i+=2){
		var ele = eval("frm."+arguments[i]);
		ele.value = arguments[i+1];
	}
	frm.method = "post";
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function _wbCompInitNavigation(nm_cookie,nm,url){
	wb_utils_set_cookie(nm_cookie + 'cnt', "1") 
	wb_utils_set_cookie(nm_cookie + 'nm_' + "1", nm);
	wb_utils_set_cookie(nm_cookie + 'url_' + "1", url);
}

function _wbCompGetPrevNavigation(nm_cookie,nm){
	url_id = parseInt(wb_utils_get_cookie(nm_cookie+'cnt')-1);
	wb_utils_set_cookie(nm_cookie+'cnt', url_id);
	if (nm != null)
		wb_utils_set_cookie(nm_cookie+"nm_"+url_id,nm);
	return wb_utils_get_cookie(nm_cookie+"url_"+url_id);
}

function _wbCompGetCheckedList(frm,pickChecked,pickMultiple,att){
	list = "";
	for (i=0; i<frm.elements.length; i++)
		if (frm.elements[i].type=="checkbox" && frm.elements[i].name!="select_all"){
			if (frm.elements[i].checked && pickChecked)
				list += eval("frm.elements["+i+"]."+att) + "~";
			if (!frm.elements[i].checked && !pickChecked)
				list += eval("frm.elements["+i+"]."+att) + "~";
		}
	if (list=="" || list.indexOf("~")<0)
		return list;
	else if (pickMultiple)
		return list.substr(0,list.length-1);
	else
		return list.substring(0,list.indexOf("~"));
}

function _wbCompGetTextList(frm,nm,num){
	list = "";
	invalid = false;
	for (i=1; i<=num; i++){
		if (eval("frm."+nm+i+".value")=="")
			invalid = true;
		list += eval("frm."+nm+i+".value") + "~";
	}
	if (invalid)
		return "";
	else
		return list.substr(0,list.length-1);
}

function _wbCompCheckForm(frm){
	for (i=0; i<frm.elements.length; i++)
		if ((frm.elements[i].type=="text" || frm.elements[i].type=="textarea")&&(frm.elements[i].value=="" || frm.elements[i].value==null))
			return false;
	return true;
}

function _wbCompGetListArray(frm,pickAll){
	invalid = false;
	var list = new Array();
	for (i=0; i<arguments.length-1; i++)
		list[i] = "";
	for (i=0; i<frm.elements.length; i++)
		if (frm.elements[i].type=="checkbox"&&frm.elements[i].name!="select_all"&&(!frm.elements[i].checked || pickAll)){
			list[0] += frm.elements[i].name + "~";
			for (j=1; j<arguments.length-1; j++){
				if (eval("frm."+arguments[j+1]+frm.elements[i].name+".value")=="")
					invalid = true;
				list[j] += eval("frm."+arguments[j+1]+frm.elements[i].name+".value") + "~";
			}
		}
	if (list[0].indexOf("~")>-1)
		for (i=0; i<arguments.length-1; i++)
			list[i] = list[i].substr(0,list[i].length-1);
	if (invalid)
		return null;
	else
		return list;
}

function _wbCompGetDate(frm,date_nm){
	cur_date_yy = eval("frm."+date_nm+"_yy.value");
	cur_date_mm = eval("frm."+date_nm+"_mm.value");
	cur_date_dd = eval("frm."+date_nm+"_dd.value");
	if (cur_date_yy=="" || cur_date_mm=="" || cur_date_dd=="")
		return eval("frm."+date_nm+".value");
	cur_date = cur_date_yy+"-"+cur_date_mm+"-"+cur_date_dd+" 23:59:59.00";
	return cur_date;
}

function _wbCompOpenWindow(url,height,width){
	feature = 'toolbar=' + 'no'
			+ ',status=' + 'yes'
			+ ',width=' + width
			+ ',height=' + height
			+ ',scrollbars=' + 'yes'
			+ ',resizable=' + 'no';
	wbUtilsOpenWin(url,"pickSkillWin",false,feature);
}

function _wbCompGetCheckboxLst(ele){
	var i;
	var list = "";
	if(ele){
		if(ele.length){
			for(i=0;i<ele.length;i++){
				if(!ele[i].checked){
					list += ele[i].value + "~";
				}
			}
			if(list.indexOf("~")>-1)
				list = list.substr(0,list.length-1);
		}else{
			if(ele.checked)
				list = ele.value;
		}
	}
	return list;
}

function _wbCompGetSelectLst(ele,subele){
	var i;
	var list = "";
	if(ele){
		if(ele.length){
			for(i=0;i<ele.length;i++){
				if(!ele[i].checked){
					list += subele[i].options[subele[i].selectedIndex].value + "~";
				}
			}
			if(list.indexOf("~")>-1)
				list = list.substr(0,list.length-1);
		}else{
			if(ele.checked)
				list = subele.options[subele.selectedIndex].value;
		}
	}
	return list;
}
