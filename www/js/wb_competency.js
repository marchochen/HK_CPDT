function wbCompetency(){
	//Competency Model - Skill Definition API
	this.get_skill_grp_lst = wbCompetencyGetSkillGroupList;
	this.get_skill_grp_lst_url = wbCompetencyGetSkillGroupListURL;
	this.get_skill_grp = wbCompetencyGetSkillGroup;
	this.ins_skill_grp_prep = wbCompetencyInsertSkillGroupPrep;
	this.ins_skill_grp_exec = wbCompetencyInsertSkillGroupExec;
	this.upd_skill_grp_prep = wbCompetencyUpdateSkillGroupPrep;
	this.upd_skill_grp_exec = wbCompetencyUpdateSkillGroupExec;
	this.del_skill_grp = wbCompetencyDeleteSkillGroup;
	this.empty_recycle_bin = wbCompetencyEmptyRecycleBin;
	this.copy_skill_grp = wbCompetencyCopySkillGroup;
	this.paste_skill_grp = wbCompetencyPasteSkillGroup;
	this.srh_skill_simple = wbCompetencySearchSkillSimple;
	this.srh_skill_advance_prep = wbCompetencySearchSkillAdvancedPrep;
	this.srh_skill_advance_exec = wbCompetencySearchSkillAdvancedExec;
	this.get_skill = wbCompetencyGetSkill;
	this.ins_skill_prep = wbCompetencyInsertSkillPrep;
	this.ins_skill_exec = wbCompetencyInsertSkillExec;
	this.upd_skill_prep = wbCompetencyUpdateSkillPrep;
	this.upd_skill_exec = wbCompetencyUpdateSkillExec;

	this.ins_comp_grp_prep = wbCompetencyInsertCompetencyGroupPrep
	this.ins_comp_grp_exec = wbCompetencyInsertCompetencyGroupExec
	this.upd_comp_grp_prep = wbCompetencyUpdateCompetencyGroupPrep
	this.upd_comp_grp_exec = wbCompetencyUpdateCompetencyGroupExec
	this.del_comp_grp_exec = wbCompetencyDeleteCompetencyGroupExec
	this.get_comp_grp_content = wbCompetencyGetCompetencyGroupContent

	this.ins_comp_skill_prep = wbCompetencyInsertCompetencyPrep
	this.ins_comp_skill_exec = wbCompetencyInsertCompetencyExec
	this.upd_comp_skill_prep = wbCompetencyUpdateCompetencyPrep
	this.upd_comp_skill_exec = wbCompetencyUpdateCompetencyExec
	this.del_comp_skill = wbCompetencyDeleteCompetencyExec
	this.del_comp_skill_list = wbCompetencyDeleteCompetencyList
	this.get_comp_skill = wbCompetencyGetCompetency





	//Competency Model - Assessment Scale API
	this.get_scale_lst = wbCompetencyGetScaleList;
	this.get_scale_lst_url = wbCompetencyGetScaleListURL;
	this.get_scale = wbCompetencyGetScale;
	this.get_scale_url = wbCompetencyGetScaleURL;
	this.ins_scale_prep = wbCompetencyInsertScalePrep;
	this.ins_scale_exec = wbCompetencyInsertScaleExec;
	this.upd_scale_prep = wbCompetencyUpdateScalePrep;
	this.upd_scale_exec = wbCompetencyUpdateScaleExec;
	this.del_scale = wbCompetencyDeleteScale;
	this.srh_scale_simple = wbCompetencySearchScaleSimple;
	this.srh_scale_advance_prep = wbCompetencySearchScaleAdvancedPrep;
	this.srh_scale_advance_exec = wbCompetencySearchScaleAdvancedExec;

	//Competency Model - Competency Profile API
	this.get_cpty_profile_lst = wbCompetencyGetCompetenceProfileList;
	this.get_cpty_profile_lst_url = wbCompetencyGetCompetenceProfileListURL;
	this.get_cpty_profile = wbCompetencyGetCompetenceProfile;
	this.get_cpty_profile_popup = wbCompetencyGetCompetenceProfilePopup;
	this.ins_skill_set_prep = wbCompetencyInsertSkillSetPrep;
	this.ins_skill_set_exec = wbCompetencyInsertSkillSetExec;
	this.upd_skill_set_prep = wbCompetencyUpdateSkillSetPrep;
	this.upd_skill_set_exec = wbCompetencyUpdateSkillSetExec;
	this.del_skill_set = wbCompetencyDeleteSkillSet;
	this.del_skill_set_check = wbCompetencyDeleteSkillSetCheck;
	this.pick_skill = wbCompetencyPickSkill;
	this.drop_skill = wbCompetencyDropSkill;

	//Competency Assessment - Survey API
	this.get_svy_lst_url = wbCompetencyGetSurveyListURL;
	this.get_svy_lst = wbCompetencyGetSurveyList;
	this.get_svy = wbCompetencyGetSurvey;
	this.ins_svy_prep = wbCompetencyInsertSurveyPrep;
	this.ins_svy_exec = wbCompetencyInsertSurveyExec;
	this.upd_svy_prep = wbCompetencyUpdateSurveyPrep;
	this.upd_svy_exec = wbCompetencyUpdateSurveyExec;
	this.del_svy = wbCompetencyDeleteSurvey;
	this.pick_svy_skill = wbCompetencyPickSurveySkill;
	this.drop_svy_skill = wbCompetencyDropSurveySkill;
	this.preview_svy = wbCompetencyPreviewSurvey;
	this.upd_que_prep = wbCompetencyUpdateQuestionPrep;
	this.upd_que_exec = wbCompetencyUpdateQuestionExec;

	//Competency Profile - Assessment API
	this.get_ass_lst = wbCompetencyGetAssessmentList;
	this.get_ass_lst_url = wbCompetencyGetAssessmentListURL;
	this.get_ass = wbCompetencyGetAssessment;
	this.ins_ass_prep = wbCompetencyInsertAssessmentPrep;
	this.ins_ass_single_prep = wbCompetencyInsertAssessmentSinglePrep;
	this.ins_ass_echo = wbCompetencyInsertAssessmentEcho;
	this.ins_ass_exec = wbCompetencyInsertAssessmentExec;
	this.ins_ass_single_exec = wbCompetencyInsertAssessmentSingleExec;
	this.upd_ass_prep = wbCompetencyUpdateAssessmentPrep;
	this.upd_ass_exec = wbCompetencyUpdateAssessmentExec;
	this.del_ass = wbCompetencyDeleteAssessment;
	this.pick_svy_prep = wbCompetencyPickSurveyPrep;
	this.pick_svy_exec = wbCompetencyPickSurveyExec;
	this.pick_assessor_prep = wbCompetencyPickAssessorPrep;
	this.pick_assessor_exec = wbCompetencyPickAssessorExec;
	this.upd_assessor_prep = wbCompetencyUpdateAssessorPrep;
	this.upd_assessor_exec = wbCompetencyUpdateAssessorExec;
	this.del_assessor = wbCompetencyDeleteAssessor;
	this.preview_ass = wbCompetencyPreviewAssessment;
	this.get_ass_res = wbCompetencyGetAssessmentResult;
	this.resolve_ass_res = wbCompetencyResolveAssessmentResult;
	this.get_usr_ass_res = wbCompetencyGetUserAssessmentResult;
	this.pick_appr_prep = wbCompetencyPickApproverPrep;

	//Skill Inventory - Skill Gap Analysis API
	this.sk_gap_ana_prep = wbCompetencySkillGapAnalysisPrep;
	this.sk_gap_ana_prep_url = wbCompetencySkillGapAnalysisPrepURL;
	this.sk_gap_ana_exec = wbCompetencySkillGapAnalysisExec;
	this.pick_usr_grp = wbCompetencyPickUserGroup;
	this.show_recommend_cos = wbCompetencyShowRecommendedCourse;

	//Skill Inventory - Personal / Group Competency API
	this.get_usr_cpty_prep_url = wbCompetencyGetUserCompetencyPrepURL;
	this.get_usr_cpty_exec = wbCompetencyGetUserCompetencyExec;

	//Skill Inventory - Skill Search API
	this.sk_srh_prep_url = wbCompetencySkillSearchPrepURL;
	this.sk_srh_exec = wbCompetencySkillSearchExec;
	this.pick_srh_skill = wbCompetencyPickSearchSkill;
	this.drop_srh_skill = wbCompetencyDropSearchSkill;
	this.show_usr_skill = wbCompetencyShowUserSkill;
	this.sk_srh_by_cpty_exec = wbCompetencySkillSearchByCompetencyProfileExec;
	this.show_usr_skill_alt = wbCompetencyShowUserSkillAlternative;

	//Learner: To-do list API
	this.submit_ass_prep = wbCompetencySubmitAssessmentPrep;
	this.submit_ass_exec = wbCompetencySubmitAssessmentExec;
	this.submit_mod_ass_prep = wbCompetencySubmitModuleAssessmentPrep;
	this.cancel_ass_exec = wbCompetencyCancelAssessmentExec;

	//Manager: To-do list API
	this.get_to_do_list = wbHomeCompGetAssignedAssessmentList;

	//General Functions API
	this.sort = wbCompetencySort;
}



function wbCompetencyUpdateCompetencyPrep(skb_id){
	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","get_competency",
					"skl_skb_id", skb_id,
					"stylesheet","cpty_upd_competency.xsl"
				);
	self.location.href = url;
	return;
}


function wbCompetencyUpdateCompetencyExec(frm, lang){

	var hasIndicator = false;
	if( frm.skb_title) {
		frm.skb_title.value = wbUtilsTrimString(frm.skb_title.value);
		if (!gen_validate_empty_field(frm.skb_title, eval("wb_msg_" + lang + "_sk_title"), lang)) {
			frm.skb_title.focus();
			return;
		}
	}
	if(frm.skb_description) {
		frm.skb_description.value = wbUtilsTrimString(frm.skb_description.value);
		if (frm.skb_description.value.length > 4000) {
			alert(eval("wb_msg_" + lang + "_short_desc"));
			frm.skb_description.focus();
			return;
		}
	}
	
	var skb_description;
	for(i=1; i<=10; i++){
		skb_description = eval("frm.skb_description_" + i);
        if (skb_description) {
			skb_description.value = wbUtilsTrimString(skb_description.value);
    		if(eval("frm.skb_description_" + i + ".value.length")>4000){
    			alert(eval("wb_msg_"+lang+"_short_desc"));
    			eval("frm.skb_description_" + i + ".focus()");
    			return;
    		}

    		if( eval("frm.skb_description_" + i + ".value") != '' || eval("frm.skb_order_" + i + ".value") != '' ) {
    			hasIndicator = true;
    			if( !gen_validate_empty_field(eval("frm.skb_description_" + i), eval("wb_msg_"+lang+"_desc"),lang) ) {
    				eval("frm.skb_description_" + i).focus();
    				return;
    			}
    			if(!gen_validate_positive_integer(eval("frm.skb_order_" + i),eval("wb_msg_"+lang+"_order"),lang)) {
    				eval("frm.skb_order_" + i).focus();
    				return;
    			}

    			frm.behav_description.value += eval("frm.skb_description_" + i + ".value") + ":_:_:";
    			if( eval("frm.skb_order_" + i + ".value") == '' ) {
    				frm.behav_order.value += i + ":_:_:";
    			} else {
    				frm.behav_order.value += eval("frm.skb_order_" + i + ".value") + ":_:_:";
    			}
    		}

    		if( eval("frm.behav_skb_id_list[" + (i-1) + "].value") > 0 ) {
    			frm.behav_skb_id.value += eval("frm.behav_skb_id_list[" + (i-1) + "].value") + ":_:_:";
    		}
        }
	}


	frm.method = "get";
	frm.skl_rating_ind.value = "true";
	/*
	if( frm.rating_ind && frm.rating_ind[0].checked ){
		frm.skl_rating_ind.value = "true";
	} else {
		frm.skl_rating_ind.value = "false";
		if( !hasIndicator ) {
			alert(eval("wb_msg_" + lang + "_empty_behavioral_indicators"));
			return;
		}
	}
	*/
	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","get_competency",
					"skl_skb_id", frm.skb_id.value,
					"stylesheet","cpty_view_competency.xsl"
				);
	frm.url_success.value = url;
	frm.url_failure.value = url;

	frm.submit();
	return;

}

function wbCompetencyDeleteCompetencyList(frm, lang){
	var skb_id_lst = '';
	if (frm.skb_id.length) {
    	for(i=0; i<frm.skb_id.length; i++){
    		if( frm.skb_id[i].checked ) {
    			skb_id_lst += frm.skb_id[i].value + '~';
    		}
    	}
    } else {
        if (frm.skb_id.checked) {
            skb_id_lst = frm.skb_id.value;
        }
    }

	if( skb_id_lst == '' ) {
		alert(eval("wb_msg_" + lang + "_plz_pick_sk"));
		return;
	}
	var url_failure = self.location.href;
	var url_success = self.location.href;
	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","del_competency_list",
					"skb_id_lst", skb_id_lst,
					"url_success", url_success,
					"url_failure", url_failure
				);
	self.location.href = url;
	return;
}

function wbCompetencyDeleteCompetencyExec(skb_id, parent_skb_id, timestamp){

	var url_success = wb_utils_invoke_disp_servlet(
						"module", "competency.CompetencyModule",
						"cmd", "get_comp_grp",
						"stn_skb_id", parent_skb_id,
						"stylesheet", "cpty_comp_grp_view.xsl"
						)
	var url_failure = self.location.href;

	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","del_competency",
					"skl_skb_id", skb_id,
					"url_success", url_success,
					"url_failure", url_failure,
					"skb_update_timestamp", timestamp
				);
	self.location.href = url;

	return;

}

function wbCompetencyGetCompetency(skb_id){

	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","get_competency",
					"skl_skb_id", skb_id,
					"stylesheet","cpty_view_competency.xsl"
				);
	self.location.href = url;
	return;

}

function wbCompetencyInsertCompetencyPrep(skb_id){

	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","prep_ins_competency",
					"stn_skb_id", skb_id,
					"stylesheet","cpty_ins_competency.xsl"
				);
	self.location.href = url;
	return;

}

function wbCompetencyInsertCompetencyExec(frm, lang){
	
	if( frm.skb_title ) {
		frm.skb_title.value = wbUtilsTrimString(frm.skb_title.value);
		if (!gen_validate_empty_field(frm.skb_title, eval("wb_msg_" + lang + "_sk_title"), lang)) {
			frm.skb_title.focus();
			return;
		}
	}
	
	if(frm.skb_description) {
		frm.skb_description.value = wbUtilsTrimString(frm.skb_description.value);
		if (frm.skb_description.value.length > 4000) {
			alert(eval("wb_msg_" + lang + "_short_desc"));
			frm.skb_description.focus();
			return;
		}
	}

	var hasIndicator = false;
	var skb_description;
	for(i=1; i<=10; i++){
		skb_description = eval("frm.skb_description_" + i);
		if(skb_description) {
			frm.skb_description.value = wbUtilsTrimString(frm.skb_description.value);
			if (eval("frm.skb_description_" + i + ".value.length") > 4000) {
				alert(eval("wb_msg_" + lang + "_short_desc"));
				eval("frm.skb_description_" + i + ".focus()");
				return;
			}
		}
   		if( eval("frm.skb_description_" + i + ".value") != '' ) {
   			hasIndicator = true;
   			if( !gen_validate_empty_field(eval("frm.skb_description_" + i), eval("wb_msg_"+lang+"_desc"),lang) ) {
   				eval("frm.skb_description_" + i).focus();
   				return;
   			}
   			frm.behav_description.value += eval("frm.skb_description_" + i + ".value") + ":_:_:";
   		}
	}

	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_grp","stn_skb_id", frm.skb_parent_skb_id.value,"stylesheet","cpty_comp_grp_view.xsl");
	frm.url_failure.value = self.location.href;
	frm.method = "get";
	if(frm.rating_ind[0].checked){
		frm.skl_rating_ind.value = "true";
	} else {
		frm.skl_rating_ind.value = "false";
		if (!hasIndicator) {
			alert(eval("wb_msg_" + lang + "_empty_behavioral_indicators"));
			if (frm.skb_description_1) {
				frm.skb_description_1.focus();
			}
			return;
		}
	}
	frm.submit();
	return;

}

function wbCompetencyGetCompetencyGroupContent(skb_id){

	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","get_comp_grp",
					"stn_skb_id", skb_id,
					"stylesheet","cpty_comp_grp_view.xsl"
				);
	self.location.href = url;
	return;
}

function wbCompetencyInsertCompetencyGroupPrep(){

	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","prep_ins_comp_group",
					"stylesheet","cpty_ins_comp_grp.xsl"
				);
	self.location.href = url;
	return;
}


function wbCompetencyInsertCompetencyGroupExec(frm, usr_ent_id, lang){
	if(frm.skb_title) {
		frm.skb_title.value = wbUtilsTrimString(frm.skb_title.value);
		if (!gen_validate_empty_field(frm.skb_title, eval("wb_msg_" + lang + "_sk_title"), lang)) {
			frm.skb_title.focus();
			return;
		}
	}
	
	if(frm.skb_description) {
		frm.skb_description.value = wbUtilsTrimString(frm.skb_description.value);
		if (frm.skb_description.value.length > 4000) {
			alert(eval("wb_msg_" + lang + "_short_desc"));
			frm.skb_description.focus();
			return;
		}
	}
	frm.skb_ssl_id.value = frm.sel_scale.options[frm.sel_scale.selectedIndex].value;
	if( frm.skb_ssl_id.value == ""){
		alert(eval("wb_msg_" + lang + "_plz_sel_scale"));
		return;
	}
	frm.url_success.value = "javascript:wb_utils_nav_go('CM_MAIN', " + usr_ent_id + " , '" + lang + "' )";
	frm.url_failure.value = self.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
	return;
}

function wbCompetencyDeleteCompetencyGroupExec(skb_id, usr_ent_id, lang, timestamp){

	var url_success = "javascript:wb_utils_nav_go('CM_MAIN', " + usr_ent_id + " , '" + lang + "' )";
	var url_failure = self.location.href;
	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","del_comp_group",
					"stn_skb_id", skb_id,
					"skb_update_timestamp", timestamp,
					"url_success", url_success,
					"url_failure", url_failure
				);
	self.location.href = url;
	return;
}


function wbCompetencyUpdateCompetencyGroupPrep(skb_id){

	var url = wb_utils_invoke_disp_servlet(
					"module","competency.CompetencyModule",
					"cmd","prep_upd_comp_group",
					"stn_skb_id", skb_id,
					"stylesheet","cpty_upd_comp_grp.xsl"
				);
	self.location.href = url;
	return;
}


function wbCompetencyUpdateCompetencyGroupExec(frm, usr_ent_id, lang){
	
	if(frm.skb_title) {
		frm.skb_title.value = wbUtilsTrimString(frm.skb_title.value);
		if (!gen_validate_empty_field(frm.skb_title, eval("wb_msg_" + lang + "_sk_title"), lang)) {
			frm.skb_title.focus();
			return;
		}
	}
	if(frm.skb_description) {
		frm.skb_description.value = wbUtilsTrimString(frm.skb_description.value);
		if (frm.skb_description.value.length > 4000) {
			alert(eval("wb_msg_" + lang + "_short_desc"));
			frm.skb_description.focus();
			return;
		}
	}
	if (frm.sel_scale) {
    	frm.skb_ssl_id.value = frm.sel_scale.options[frm.sel_scale.selectedIndex].value;
    	if( frm.skb_ssl_id.value == ""){
    		alert(eval("wb_msg_" + lang + "_plz_sel_scale"));
    		return;
    	}
	}
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_grp","stn_skb_id", frm.stn_skb_id.value,"stylesheet","cpty_comp_grp_view.xsl");
	frm.url_failure.value = self.location.href;
	frm.method = "get";
	frm.submit();
	return;
}



function wbHomeCompGetAssignedAssessmentList(prepared,notified,collected,resolved){
	url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assigned_ass","stylesheet",'comp_assigned_ass_list.xsl');
	window.location.href = url;
}

function wbCompetencyGetSkillGroupList(type,lang,cur_page,pagesize){

	var url = wb_utils_invoke_disp_servlet(
				"module", "competency.CompetencyModule",
				"cmd", "get_comp_grp_list",
				"stylesheet", "cpty_skill_grp_lst.xsl"
			)
	self.location.href = url;
	return;
}

function wbCompetencyGetSkillGroupListURL(lang,cur_page,pagesize){
	if(cur_page==null || cur_page=="")
		cur_page = "1";
	if(pagesize==null || pagesize=="")
		pagesize = "10";
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","cur_page",cur_page,"pagesize",pagesize,"stylesheet","cpty_skill_grp_lst.xsl");
	wb_utils_set_cookie(wb_nav + 'cnt', 1);
	return url;
}

function wbCompetencyGetSkillGroup(skill_group_nm,parent_id,cur_page,pagesize){
	if(cur_page==null || cur_page=="")
		cur_page = "1";
	if(pagesize==null || pagesize=="")
		pagesize = "10";
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","skill_id",parent_id,"cur_page",cur_page,"pagesize",pagesize,"stylesheet","cpty_sub_skill_grp_lst.xsl");
	parent.location.href = url;
}

function wbCompetencySearchSkillSimple(frm,lang){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "search_skill_simple";
	frm.skl_title.value = frm.search.value;
	frm.stylesheet.value = "cpty_srh_skill_res.xsl";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

function wbCompetencySearchSkillAdvancedPrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet","cpty_srh_skill_advance.xsl");
	parent.location.href = url;
}

function wbCompetencySearchSkillAdvancedExec(frm,lang){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "search_skill_adv";
	frm.stylesheet.value = "cpty_srh_skill_res.xsl";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

function wbCompetencyInsertSkillGroupPrep(parent_id,scale_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","prep_ins_skill","stylesheet","cpty_upd_skill_grp.xsl");
	if(parent_id!=null)
		url += "&parent_id=" + parent_id;
	if(scale_id!=null)
		url += "&scale_id_lst=" + scale_id;
	parent.location.href = url;
}

function wbCompetencyInsertSkillGroupExec(frm,lang,parent_id,scale_id){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.cmd.value = "ins_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.parent_id.value = parent_id;
	frm.derive_rule.value = frm.rule_type.options[frm.rule_type.selectedIndex].value;
	if(frm.sel_scale)
		frm.scale_id.value = frm.sel_scale.options[frm.sel_scale.selectedIndex].value;
	else
		frm.scale_id.value = scale_id;
	if(parent_id!=""){
		var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","skill_id",parent_id,"cur_page","1","pagesize","10","stylesheet","cpty_sub_skill_grp_lst.xsl");
	}else{
		var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","cur_page","1","pagesize","10","stylesheet","cpty_skill_grp_lst.xsl");
	}
	frm.url_success.value = url_success;
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyUpdateSkillGroupPrep(id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","prep_upd_skill","skill_id",id,"stylesheet","cpty_upd_skill_grp.xsl");
	parent.location.href = url;
}

function wbCompetencyUpdateSkillGroupExec(frm,lang,skill_id,timestamp,scale_id){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	if(frm.sel_scale)
		frm.scale_id.value = frm.sel_scale.options[frm.sel_scale.selectedIndex].value;
	else
		frm.scale_id.value = scale_id;
	frm.skill_id.value = skill_id;
	frm.timestamp.value = timestamp;
	frm.derive_rule.value = frm.rule_type.options[frm.rule_type.selectedIndex].value;
	frm.cmd.value = "upd_skill";
	frm.module.value = "competency.CompetencyModule";
	var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","skill_id",skill_id,"cur_page","1","pagesize","10","stylesheet","cpty_sub_skill_grp_lst.xsl");
	frm.url_success.value = url_success;
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyDeleteSkillGroup(id,timestamp,lang,parent_id){
	if(!confirm(eval("wb_msg_"+lang+"_rm_sk")))
		return;
	if(parent_id!=""){
		var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","skill_id",parent_id,"cur_page","1","pagesize","10","stylesheet","cpty_sub_skill_grp_lst.xsl");
	}else{
		var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","cur_page","1","pagesize","10","stylesheet","cpty_skill_grp_lst.xsl");
	}
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_skill","skill_id",id,"timestamp",timestamp,"url_success",url_success,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencyEmptyRecycleBin(frm,lang){
	var i;
	var skill_id_lst = "";
	var update_timestamp_lst = "";
	if(frm.id.length){
		for(i=0; i<frm.id.length; i++){
			if(frm.id[i].checked){
				skill_id_lst += frm.id[i].value + "~";
				update_timestamp_lst += frm.update_timestamp[i].value + "~";
			}
		}
		skill_id_lst = skill_id_lst.substr(0,skill_id_lst.length-1);
		update_timestamp_lst = update_timestamp_lst.substr(0,update_timestamp_lst.length-1);
	}else{
		if(frm.id.checked){
			skill_id_lst = frm.id.value;
			update_timestamp_lst = frm.update_timestamp.value;
		}
	}
	if(skill_id_lst==""){
		alert(eval("wb_msg_"+lang+"_select_itm"));
		return;
	}
	frm.method = "post";
	frm.action = wb_utils_disp_servlet_url;
	frm.cmd.value = "trash_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.timestamp.value = update_timestamp_lst;
	frm.skill_id.value = skill_id_lst;
	frm.url_success.value = parent.location.href;
	frm.url_failure.value = parent.location.href;
	frm.submit();
}

function wbCompetencyCopySkillGroup(id,timestamp,type,lang){
	wb_utils_set_cookie("copy_node_id",id);
	wb_utils_set_cookie("copy_node_timestamp",timestamp);
	wb_utils_set_cookie("copy_node_type",type);
	alert(eval("wb_msg_"+lang+"_copy_ent"));
}

function wbCompetencyPasteSkillGroup(parent_id,lang){
	var copy_node_id = wb_utils_get_cookie("copy_node_id");
	var copy_node_timestamp = wb_utils_get_cookie("copy_node_timestamp");
	var copy_node_type = wb_utils_get_cookie("copy_node_type");
	if(copy_node_id == "" || copy_node_id == null){
		alert(eval("wb_msg_"+lang+"_copy_skgrp_first"));
		return;
	}
	if(copy_node_id == parent_id){
		alert(eval("wb_msg_"+lang+"_paste_same_grp"));
		return;
	}
	if(copy_node_type=="SKILLGRP")
		var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","paste_node","node_id",copy_node_id,"parent_id",parent_id,"timestamp",copy_node_timestamp,"url_success",parent.location.href,"url_failure",parent.location.href);
	else if(copy_node_type=="SKILL")
		var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","paste_skill","skill_id",copy_node_id,"parent_id",parent_id,"timestamp",copy_node_timestamp,"url_success",parent.location.href,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencyGetSkill(title,skill_id,type){
	if(type==null || type=="" || type=="detail")
		var stylesheet = "cpty_skill_detail.xsl";
	else if(type=="deleted")
		var stylesheet = "cpty_del_skill_detail.xsl"
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill","skill_id",skill_id,"stylesheet",stylesheet);
	parent.location.href = url;
}

function wbCompetencyInsertSkillPrep(parent_id,scale_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","prep_ins_skill","stylesheet","cpty_upd_skill.xsl");
	if(parent_id!=null)
		url += "&parent_id=" + parent_id;
	if(scale_id!=null)
		url += "&scale_id_lst=" + scale_id;
	parent.location.href = url;
}

function wbCompetencyInsertSkillExec(frm,lang,parent_id,scale_id){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	if(frm.sel_scale)
		frm.scale_id.value = frm.sel_scale.options[frm.sel_scale.selectedIndex].value;
	else
		frm.scale_id.value = scale_id;
	frm.parent_id.value = parent_id;
	frm.cmd.value = "ins_skill";
	frm.module.value = "competency.CompetencyModule";
	if(parent_id!=""){
		var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","skill_id",parent_id,"cur_page","1","pagesize","10","stylesheet","cpty_sub_skill_grp_lst.xsl");
	}else{
		var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_comp_skill_content","cur_page","1","pagesize","10","stylesheet","cpty_skill_grp_lst.xsl");
	}
	frm.url_success.value = url_success;
	frm.url_failure.value = parent.location.href;
	frm.submit();
}

function wbCompetencyUpdateSkillPrep(skill_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","prep_upd_skill","skill_id",skill_id,"stylesheet","cpty_upd_skill.xsl");
	parent.location.href=url;
}

function wbCompetencyUpdateSkillExec(frm,lang,skill_id,timestamp,scale_id){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	if(frm.sel_scale)
		frm.scale_id.value = frm.sel_scale.options[frm.sel_scale.selectedIndex].value;
	else
		frm.scale_id.value = scale_id;
	frm.skill_id.value = skill_id;
	frm.timestamp.value = timestamp;
	frm.cmd.value = "upd_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill","skill_id",skill_id,"stylesheet","cpty_skill_detail.xsl");
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyGetScaleList(cur_page,pagesize){
	if(cur_page==null || cur_page=="")
		cur_page = "1";
	if(pagesize==null || pagesize=="")
		pagesize = "20";
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale_list","all","N","cur_page",cur_page,"pagesize",pagesize,"stylesheet","cpty_scale_lst.xsl","order_by","ssl_title","sort_by","ASC");
	parent.location.href = url;
}

function wbCompetencyGetScaleListURL(lang,cur_page,pagesize){
	if(cur_page==null || cur_page=="")
		cur_page = "1";
	if(pagesize==null || pagesize=="")
		pagesize = "20";
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale_list","all","N","cur_page",cur_page,"pagesize",pagesize,"stylesheet","cpty_scale_lst.xsl","order_by","ssl_title","sort_by","ASC");
	return url;
}

function wbCompetencyGetScale(scale_id,title){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale","scale_id",scale_id,"stylesheet","cpty_scale_detail.xsl");
	parent.location.href = url;
}

function wbCompetencyGetScaleURL(scale_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale","scale_id",scale_id,"stylesheet","cpty_scale_detail.xsl");
	return url;
}

function wbCompetencyInsertScalePrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale_list","all","Y","stylesheet","cpty_ins_scale.xsl");
	parent.location.href = url;
}

function wbCompetencyInsertScaleExec(frm,lang){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	_wbCompetencyFeedScaleLevel(frm);
	frm.cmd.value = "ins_scale";
	frm.module.value = "competency.CompetencyModule";
	frm.level_total.value = _wbCompetencyGetScaleLevel(frm);
	frm.url_success.value = wbCompetencyGetScaleListURL(lang);
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyUpdateScalePrep(scale_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_scale","scale_id",scale_id,"stylesheet","cpty_upd_scale.xsl");
	parent.location.href = url;
}

function wbCompetencyUpdateScaleExec(frm,lang,scale_id,timestamp){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	_wbCompetencyFeedScaleLevel(frm);
	frm.cmd.value = "upd_scale";
	frm.module.value = "competency.CompetencyModule";
	frm.level_total.value = _wbCompetencyGetScaleLevel(frm);
	frm.scale_id.value = scale_id;
	frm.timestamp.value = timestamp;
	frm.url_success.value = wbCompetencyGetScaleURL(scale_id);
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyDeleteScale(scale_id,timestamp,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_scale")))
		return;
	var url_success = wbCompetencyGetScaleListURL(lang);
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_scale","scale_id",scale_id,"timestamp",timestamp,"url_success",url_success,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencySearchScaleSimple(frm,lang){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "search_scale_simple";
	frm.ssl_title.value = frm.search.value;
	frm.stylesheet.value = "cpty_srh_scale_res.xsl";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

function wbCompetencySearchScaleAdvancedPrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_prof","stylesheet","cpty_srh_scale_advance.xsl");
	parent.location.href = url;
}

function wbCompetencySearchScaleAdvancedExec(frm,lang){
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "search_scale_adv";
	frm.stylesheet.value = "cpty_srh_scale_res.xsl";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

function wbCompetencyGetCompetenceProfileList(cur_page,pagesize,order_by,sort_by){
	if(cur_page==null || cur_page=="")
		cur_page = "1";
	if(pagesize==null || pagesize=="")
		pagesize = "20";
	if(order_by==null || order_by=="")
		order_by = "title";
	if(sort_by==null || sort_by=="")
		sort_by = "ASC";
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page",cur_page,"pagesize",pagesize,"sks_type","SKP","stylesheet",'sks_get_list.xsl',"order_by","title","sort_by","ASC");
	parent.location.href = url;
}

function wbCompetencyGetCompetenceProfileListURL(lang,cur_page,pagesize,order_by,sort_by){
	if(cur_page==null || cur_page=="")
		cur_page = "1";
	if(pagesize==null || pagesize=="")
		pagesize = "20";
	if(order_by==null || order_by=="")
		order_by = "title";
	if(sort_by==null || sort_by=="")
		sort_by = "ASC";
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page",cur_page,"pagesize",pagesize,"sks_type","SKP","stylesheet",'sks_get_list.xsl',"order_by","title","sort_by","ASC");
	return url;
}

function wbCompetencyGetCompetenceProfile(sks_id,title){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","sks_id",sks_id,"stylesheet",'sks_get_set.xsl',"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencyGetCompetenceProfilePopup(sks_id,lang){
	if(sks_id==null || sks_id==""){
		alert(eval("wb_msg_"+lang+"_plz_sel_cpty_prof"));
		return;
	}
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","sks_id",sks_id,"stylesheet","comp_svy_view_sks.xsl","url_failure",parent.location.href);
	wb_utils_open_win(url,"cptyProfileWin",780,200);
}

function wbCompetencyInsertSkillSetPrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","stylesheet",'sks_add_set.xsl',"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencyInsertSkillSetExec(frm,lang){
	var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id);
	frm.skl_id_lst.value = skill_id_lst;
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.level_lst.value =  _wbCompetencyGetPickedSkillLevelList(frm);
	frm.priority_lst.value =  _wbCompetencyGetPickedSkillPriorityList(frm);
	frm.cmd.value = "ins_skill_set";
	frm.module.value = "competency.CompetencyModule";
	frm.url_success.value = wbCompetencyGetCompetenceProfileListURL(lang);
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyUpdateSkillSetPrep(sks_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","sks_id",sks_id,"stylesheet",'sks_add_set.xsl');
	parent.location.href = url;
}

function wbCompetencyUpdateSkillSetExec(frm,lang,sks_id,timestamp){
	var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id);
	frm.skl_id_lst.value = skill_id_lst;
	frm.sks_id.value = sks_id;
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.level_lst.value =  _wbCompetencyGetPickedSkillLevelList(frm);
	frm.priority_lst.value =  _wbCompetencyGetPickedSkillPriorityList(frm);
	frm.last_upd_timestamp.value = timestamp;
	frm.cmd.value = "upd_skill_set";
	frm.module.value = "competency.CompetencyModule";
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","sks_id",sks_id,"stylesheet",'sks_get_set.xsl',"url_failure",parent.location.href);
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyDeleteSkillSet(frm,lang){
		if(!confirm(eval("wb_msg_"+lang+"_rm_comp_prof")))
			return;
		url_success=wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page",1,"pagesize",10,"sks_type","SKP","stylesheet",'sks_get_list.xsl',"order_by","title","sort_by","ASC");
		cmd = "del_skill_set";
		skill_set_id  = getUrlParam('sks_id_lst');
		url=wb_utils_invoke_disp_servlet('cmd',cmd,'module','competency.CompetencyModule','sks_id_lst',skill_set_id,'url_success','javascript:window.close();window.opener.location.href=window.opener.location;');
		window.location.href = url;
		//window.close();
}

function wbCompetencyDeleteSkillSetCheck(frm,lang){
	if(frm.skill_set_id){
		frm.sks_id_lst.value = _wbCompetencyGetCheckboxLst(frm.skill_set_id,true);
		if(frm.sks_id_lst.value==""){
			alert(eval("wb_msg_"+lang+"_plz_sel_cpty_prof"));
			return;
		}
		var sks_id_lst=frm.sks_id_lst.value ;
		var url=wb_utils_invoke_disp_servlet('cmd','del_skill_set_check','module','competency.CompetencyModule','stylesheet','skillset_del.xsl','sks_id_lst',sks_id_lst);
		wb_utils_open_win(url,"delSkillSetWin",800,500);
	}

}

function wbCompetencyPickSkill(frm,sks_id){
	var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id);
	frm.picked_skl_id_lst.value = skill_id_lst;
	frm.level_lst.value =  _wbCompetencyGetPickedSkillLevelList(frm);
	frm.priority_lst.value =  _wbCompetencyGetPickedSkillPriorityList(frm);
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","false","stylesheet",'sks_add_set.xsl',"sks_id",sks_id);
	frm.url_failure.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","stylesheet",'sks_add_set.xsl',"sks_id",sks_id);
	frm.cmd.value = "pick_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyDropSkill(frm,sks_id){
	frm.picked_skl_id_lst.value =  _wbCompetencyGetPickedSkillList(frm,false);
	frm.level_lst.value =  _wbCompetencyGetPickedSkillLevelList(frm,false);
	frm.priority_lst.value =  _wbCompetencyGetPickedSkillPriorityList(frm,false);
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","false","stylesheet",'sks_add_set.xsl',"sks_id",sks_id);
	frm.url_failure.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set","refresh","true","stylesheet",'sks_add_set.xsl',"sks_id",sks_id);
	frm.cmd.value = "pick_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyGetSurveyListURL(lang){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKT","stylesheet",'comp_svy_get_list.xsl',"order_by","title","sort_by","ASC");
	return url;
}

function wbCompetencyGetSurveyList(lang){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKT","stylesheet",'comp_svy_get_list.xsl',"order_by","title","sort_by","ASC");
	parent.location.href = url;
}

function wbCompetencyGetSurvey(sks_id,title){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_preview_svy.xsl');
	parent.location.href = url;
}

function wbCompetencyInsertSurveyPrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","stylesheet",'comp_svy_ins_svy.xsl');
	parent.location.href = url;
}

function wbCompetencyInsertSurveyExec(frm,lang){
	var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id);
	frm.skl_id_lst.value = skill_id_lst;
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	if(frm.sks_comment_ind_chk.checked)
		frm.sks_comment_ind.value = "true";
	else
		frm.sks_comment_ind.value = "false";
	frm.cmd.value = "ins_survey";
	frm.module.value = "competency.CompetencyModule";
	frm.url_success.value =  wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKT","stylesheet",'comp_svy_get_list.xsl',"order_by","title","sort_by","ASC");
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyUpdateSurveyPrep(frm,sks_id){

	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","sks_id",sks_id,"refresh","true","show_skill_path","true","stylesheet",'comp_svy_ins_svy.xsl');
	parent.location.href = url;
}

function wbCompetencyUpdateSurveyExec(frm,lang,sks_id,timestamp){
	var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id);
	frm.skl_id_lst.value = skill_id_lst;
	frm.sks_id.value = sks_id;
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	if(frm.sks_comment_ind_chk.checked)
		frm.sks_comment_ind.value = "true";
	else
		frm.sks_comment_ind.value = "false";
	frm.cmd.value = "upd_survey";
	frm.module.value = "competency.CompetencyModule";
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_preview_svy.xsl');
	frm.last_upd_timestamp.value = timestamp;
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyDeleteSurvey(sks_id,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_svy")))
		return;
	var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKT","stylesheet",'comp_svy_get_list.xsl',"order_by","title","sort_by","ASC");
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_survey","sks_id",sks_id,"url_success",url_success,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencyPickSurveySkill(frm,sks_id){
	var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id);
	if(frm.sks_comment_ind_chk.checked)
		frm.sks_comment_ind.value = "true";
	else
		frm.sks_comment_ind.value = "false";
	frm.picked_skl_id_lst.value = skill_id_lst;
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","false","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_ins_svy.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_ins_svy.xsl');
	frm.cmd.value = "pick_survey_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.target = "_self";
	frm.submit();
}

function wbCompetencyDropSurveySkill(frm,sks_id){
	var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id,false);
	if(frm.sks_comment_ind_chk.checked)
		frm.sks_comment_ind.value = "true";
	else
		frm.sks_comment_ind.value = "false";
	frm.picked_skl_id_lst.value = skill_id_lst;
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","false","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_ins_svy.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_ins_svy.xsl');
	frm.cmd.value = "pick_survey_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.target = "_self";
	frm.submit();
}

function wbCompetencyPreviewSurvey(sks_id,refresh){
	if(refresh==false){
		wb_utils_open_win("","previewSVYWin",780,480);
		var skill_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id);
		if(frm.sks_comment_ind_chk.checked)
			frm.sks_comment_ind.value = "true";
		else
			frm.sks_comment_ind.value = "false";
		frm.picked_skl_id_lst.value = skill_id_lst;
		if(sks_id!="" && sks_id!=null){
			frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","false","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_view_svy.xsl');
		}else{
			frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","false","show_skill_path","true","stylesheet",'comp_svy_view_svy.xsl');
		}
		frm.url_failure.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_survey","refresh","true","show_skill_path","true","sks_id",sks_id,"stylesheet",'comp_svy_view_svy.xsl');
		frm.cmd.value = "pick_survey_skill";
		frm.module.value = "competency.CompetencyModule";
		frm.action = wb_utils_disp_servlet_url;
		frm.method = "post";
		frm.target = "previewSVYWin";
		frm.submit();
	}else{
		var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","view_survey","refresh","true","show_skill_path","false","sks_id",sks_id,"stylesheet",'comp_svy_view_svy.xsl');
		wb_utils_open_win(url,"previewSVYWin",780,480);
	}
}

function wbCompetencyUpdateQuestionPrep(skill_id,sks_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","getQ","skl_id",skill_id,"sks_id",sks_id,"stylesheet",'comp_svy_edit_que.xsl');
	wb_utils_open_win(url,"updQuestionWin",780,280);
}

function wbCompetencyUpdateQuestionExec(frm,lang,skill_id){
	/*
	if (frm.assessee[0].checked){
		if(!gen_validate_empty_field(frm.assesseeQText,eval("wb_msg_"+lang+"_que_txt"),lang))
			return;
		else
			frm.assesseeQ.value = frm.assesseeQText.value;
	}
	*/
	if (frm.assessor[1].checked){
		if(!gen_validate_empty_field(frm.assessorQText,eval("wb_msg_"+lang+"_que_txt"),lang)){
			return;
		}else{
			frm.assessorQ.value = frm.assessorQText.value;
			frm.assesseeQ.value = frm.assessorQText.value;
		}
	}
	frm.cmd.value = "saveQ";
	frm.skl_id.value = skill_id;
	if(frm.comment_ind_chk.checked)
		frm.comment_ind.value = "true";
	else
		frm.comment_ind.value = "false";
	frm.module.value = "competency.CompetencyModule";
	frm.url_success.value = "../htm/close_window.htm";
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyGetAssessmentList(lang,prepared,notified,collected,resolved,timestamp){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","refresh_ass_list","prepared",prepared,"collected",collected,"notified",notified,"resolved",resolved,"pagesize","10","cur_page","1","sort_by","DESC","order_by","asm_eff_start_datetime","stylesheet",'comp_ass_get_list.xsl');
	if(timestamp!=null && timestamp!="")
		url = setUrlParam("timestamp",timestamp,url);
	parent.location.href = url;
}

function wbCompetencyGetAssessmentListURL(lang,prepared,notified,collected,resolved){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","refresh_ass_list","prepared",prepared,"collected",collected,"notified",notified,"resolved",resolved,"pagesize","10","cur_page","1","sort_by","DESC","order_by","asm_eff_start_datetime","stylesheet",'comp_ass_get_list.xsl');
	return url;
}

function wbCompetencyGetAssessment(asm_id,title){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_detail","asm_id",asm_id,"stylesheet",'comp_ass_get_ass.xsl');
	parent.location.href = url;
}

function wbCompetencyInsertAssessmentPrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_assessment","refresh","true","stylesheet",'comp_ass_ins_ass.xsl');
	parent.location.href = url;
}

function wbCompetencyInsertAssessmentSinglePrep(){
	var _stylesheet = 'comp_ass_prep_ass_single.xsl';
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ins_assessment","refresh","true","stylesheet",_stylesheet);
	parent.location.href = url;
}

function wbCompetencyInsertAssessmentPrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ins_assessment","refresh","true","stylesheet",'comp_ass_prep_ass.xsl');
	parent.location.href = url;
}

function wbCompetencyInsertAssessmentEcho(frm,lang){
	frm.assessee_ent_id_lst.value = _wbCompetencyGetGoldenManLst(frm.assessee_ent_id_lst_fld);
	frm.sks_id.value = _wbCompetencyGetGoldenManLst(frm.sks_id_fld,true);



	if(frm.self_assess_chk.checked)
		frm.self_assess.value = "true";
	else
		frm.self_assess.value = "false";
	if(frm.auto_resolved_ind_chk.checked)
		frm.auto_resolved_ind.value = "true";
	else
		frm.auto_resolved_ind.value = "false";
	frm.review_start.value = frm.review_start_yy.value+"-"+frm.review_start_mm.value+"-"+frm.review_start_dd.value+" "+"00:00:00.0";
	frm.review_end.value = frm.review_end_yy.value+"-"+frm.review_end_mm.value+"-"+frm.review_end_dd.value+" "+"23:59:59.0";

	if(frm.self_notify_date_yy && frm.self_notify_date_mm && frm.self_notify_date_dd)
		frm.self_notify_date.value = frm.self_notify_date_yy.value+"-"+frm.self_notify_date_mm.value+"-"+frm.self_notify_date_dd.value+" "+"00:00:00.0";
	if(frm.self_due_date_yy && frm.self_due_date_mm && frm.self_due_date_dd)
		frm.self_due_date.value = frm.self_due_date_yy.value+"-"+frm.self_due_date_mm.value+"-"+frm.self_due_date_dd.value+" "+"00:00:00.0";
	if(frm.approver_role_notify_date_yy && frm.approver_role_notify_date_mm && frm.approver_role_notify_date_dd)
		frm.approver_role_notify_date.value = frm.approver_role_notify_date_yy.value+"-"+frm.approver_role_notify_date_mm.value+"-"+frm.approver_role_notify_date_dd.value+" "+"00:00:00.0";
	if(frm.approver_role_due_date_yy && frm.approver_role_due_date_mm && frm.approver_role_due_date_dd)
		frm.approver_role_due_date.value = frm.approver_role_due_date_yy.value+"-"+frm.approver_role_due_date_mm.value+"-"+frm.approver_role_due_date_dd.value+" "+"00:00:00.0";
	//if(frm.resolver_role_notify_date_yy && frm.resolver_role_notify_date_mm && frm.resolver_role_notify_date_dd)
	//	frm.resolver_role_notify_date.value = frm.resolver_role_notify_date_yy.value+"-"+frm.resolver_role_notify_date_mm.value+"-"+frm.resolver_role_notify_date_dd.value+" "+"00:00:00.0";
	//if(frm.resolver_role_due_date_yy && frm.resolver_role_due_date_mm && frm.resolver_role_due_date_dd)
	//	frm.resolver_role_due_date.value = frm.resolver_role_due_date_yy.value+"-"+frm.resolver_role_due_date_mm.value+"-"+frm.resolver_role_due_date_dd.value+" "+"00:00:00.0";
	if(frm.self_notify_date_yy && frm.self_notify_date_yy.value != ""
	    && !gen_validate_date_compare(frm, "self_notify_date", "self_due_date", "Notification Date", "Due Date", lang, "self_notify_date")) {
	    	return false;
	}
	if(frm.approver_notify_date_yy && frm.approver_notify_date_yy.value != ""
	    && !gen_validate_date_compare(frm, "approver_notify_date", "approver_due_date", "Notification Date", "Due Date", lang, "approver_notify_date")) {
	    	return false;
	}
	/*if(frm.resolver_notify_date_yy && frm.resolver_notify_date_yy.value != ""
	    && !gen_validate_date_compare(frm, "resolver_notify_date", "resolver_due_date", "Notification Date", "Due Date", lang, "resolver_notify_date")) {
	    	return false;
	}*/

	frm.approver_role_lst.value = '';
    for (var i = 0; i<frm.elements.length; i++) {
        if ((frm.elements[i].name.indexOf('approver_role_type') > -1)) {
			if(frm.elements[i].checked) {
				if(frm.approver_role_lst.value == '')
					frm.approver_role_lst.value = frm.elements[i].value;
				else
					frm.approver_role_lst.value = frm.elements[i].value + '~' + frm.elements[i].value;
			}
        }
    }

	/*frm.resolver_role_lst.value = '';
    for (var i = 0; i<frm.elements.length; i++) {
        if ((frm.elements[i].name.indexOf('resolver_role_type') > -1)) {
			if(frm.elements[i].checked) {
				if(frm.resolver_role_type.value != '') {
					if(frm.resolver_role_lst.value == '')
						frm.resolver_role_lst.value = frm.resolver_role_type.value;
					else
						frm.resolver_role_lst.value = frm.resolver_role_lst.value + '~' + frm.resolver_role_type.value;
				}
			}
		}
	}*/
	if(!_wbCompetencyValidateForm(frm,lang))
		return;

	frm.url_failure.value = parent.location.href;
	frm.url_success.value = parent.location.href;
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "get_prep_assessment";
//		to be implement(fai)
	frm.stylesheet.value = 'comp_ass_echo_ass.xsl';
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.notify_msg_subject.value = "Assessment Notify";
	frm.collect_msg_subject.value = "Assessment Due Date Notify";

	frm.submit();
}

function wbCompetencyInsertAssessmentExec(frm,lang){
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
		var str_feature = 'toolbar='		+ 'no'
				+ ',width=' 				+ '400'
				+ ',height=' 				+ '180'
				+ ',scrollbars='				+ 'no'
				+ ',resizable='				+ 'no'
				+ ',status='				+ 'yes';
		wb_utils_set_cookie("appn_usr_name","");
		wb_utils_set_cookie("current","");
		wb_utils_set_cookie("total","");
		wb_utils_set_cookie("type","ins_assessment");
		var url = "../htm/application_frame_window.htm?lang="+lang;
		wbUtilsOpenWin(url,'del_user_win', false, str_feature);
	}

}

function wbCompetencyInsertAssessmentSingleExec(frm, lang){
	var _stylesheet;
	_stylesheet = 'comp_ass_get_list.xsl';
	frm.assessee_ent_id.value = _wbCompetencyGetGoldenManLst(frm.assessee_ent_id_fld);
	frm.self_ent_id.value = frm.assessee_ent_id.value;
	frm.sks_id.value = _wbCompetencyGetGoldenManLst(frm.sks_id_fld,true);

	if(!_wbCompetencyInsertValidateForm(frm,lang))
		return false;

	_wbCompetencyInsertGetLst(frm);
	frm.review_start.value = frm.review_start_yy.value+"-"+frm.review_start_mm.value+"-"+frm.review_start_dd.value+" "+"00:00:00.0";
	frm.review_end.value = frm.review_end_yy.value+"-"+frm.review_end_mm.value+"-"+frm.review_end_dd.value+" "+"23:59:59.0";

	if(frm.auto_resolved_ind_chk){
		if(frm.auto_resolved_ind_chk.checked){
			frm.auto_resolved_ind.value = "true";
		}else{
			frm.auto_resolved_ind.value = "false";
		}
	}

	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","refresh_ass_list","prepared","true","collected","true","notified","true","resolved","true","pagesize","10","cur_page","1","sort_by","asc","order_by","asm_eff_start_datetime","stylesheet",_stylesheet);

	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "ins_assessment";
	frm.action = wb_utils_disp_servlet_url;
	frm.notify_msg_subject.value = "Assessment Notify";
	frm.collect_msg_subject.value = "Assessment Due Date Notify";
	frm.method = "post";
	frm.submit();
}

function wbCompetencyUpdateAssessmentPrep(asm_id){
//	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","edit_assessment","refresh","true","asm_id",asm_id,"stylesheet",'comp_ass_ins_ass.xsl');
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","update_prep_assessment","refresh","true","asm_id",asm_id,"stylesheet","comp_ass_upd_ass.xsl");
	parent.location.href = url;
}


function _wbCompetencyInsertValidateForm(frm, lang) {
	if(!_wbCompetencyUpdateValidateForm(frm, lang)) {
		return false;
	}
	return true;
}

// private method to append assessor, aua related list
function _wbCompetencyInsertGetLstHelper(frm, ele, eleText, eleNotifyDate, eleDueDate) {
	if(ele!="") {
		// append the assessor related list
		if(frm.assessor_ent_id_lst.value!="") {
			frm.assessor_ent_id_lst.value += "~";
			frm.assessor_type_lst.value += "~";
			frm.weight_lst.value += "~";

			frm.aua_type_lst.value += "~";
			frm.aua_notify_date_lst.value += "~";
			frm.aua_due_date_lst.value += "~";
		}

		eleList = ele.split(",");
		for (i=0; i<eleList.length; i++){
			if(i!=0) {
				frm.assessor_ent_id_lst.value += "~";
				frm.assessor_type_lst.value += "~";
				frm.weight_lst.value += "~";
			}

			frm.assessor_ent_id_lst.value += eleList[i];
			frm.assessor_type_lst.value += eleText;
			frm.weight_lst.value += 1;
		}

		// find the min and max date for asm_notify_date and asm_due_date
		// ignore the resolver entry, only handle assessor datetime
		if(eleText != "RESOLVED") {
			if(frm.asm_notify_date.value == "") {
				frm.asm_notify_date.value = _wbCompetencyUpdateGetDatetime(frm, eleNotifyDate);
			} else {
				dateTimeArray = frm.asm_notify_date.value.split(" ");
				dateArray = dateTimeArray[0].split("-");

				var originalDate, newDate;

				originalDate = new Date(dateArray[0], dateArray[1]-1, dateArray[2], 0, 0);
				newDate = new Date(Number(eval(eleNotifyDate + "_yy.value")), Number(eval(eleNotifyDate + "_mm.value"))-1, Number(eval(eleNotifyDate + "_dd.value")), 0,0);
				if(newDate < originalDate) {
					frm.asm_notify_date.value = _wbCompetencyUpdateGetDatetime(frm, eleNotifyDate);
				}
			}

			if(frm.asm_due_date.value == "") {
				frm.asm_due_date.value = _wbCompetencyUpdateGetDatetime(frm, eleDueDate, "23:59:59.0");
			} else {
				dateTimeArray = frm.asm_due_date.value.split(" ");
				dateArray = dateTimeArray[0].split("-");

				var originalDate, newDate;

				originalDate = new Date(dateArray[0], dateArray[1]-1, dateArray[2], 0, 0);
				newDate = new Date(Number(eval(eleDueDate + "_yy.value")), Number(eval(eleDueDate + "_mm.value"))-1, Number(eval(eleDueDate + "_dd.value")), 0,0);
				if(newDate > originalDate) {
					frm.asm_due_date.value = _wbCompetencyUpdateGetDatetime(frm, eleDueDate, "23:59:59.0");
				}
			}
		}

		// append the aua related list
		frm.aua_type_lst.value += eleText;
		frm.aua_notify_date_lst.value += _wbCompetencyUpdateGetDatetime(frm, eleNotifyDate);
		frm.aua_due_date_lst.value += _wbCompetencyUpdateGetDatetime(frm, eleDueDate, "23:59:59.0");
	}
}

function _wbCompetencyInsertGetLst(frm) {
	var mgmt = _wbCompetencyGetGoldenManLst(frm.mgmt_assessor_ent_id_lst_fld,null,",");
	var peers = _wbCompetencyGetGoldenManLst(frm.peers_assessor_ent_id_lst_fld,null,",");
	var clients = _wbCompetencyGetGoldenManLst(frm.clients_assessor_ent_id_lst_fld,null,",");
	var reports = _wbCompetencyGetGoldenManLst(frm.reports_assessor_ent_id_lst_fld,null,",");
	//var resolved = _wbCompetencyGetGoldenManLst(frm.resolver_ent_id_lst_fld,null,",");
	var self = "";

	if(frm.self_assess_chk.checked) {
		self = frm.self_ent_id.value;
	}

	frm.assessor_ent_id_lst.value = "";
	frm.assessor_type_lst.value = "";
	frm.weight_lst.value = "";
	frm.aua_type_lst.value = "";
	frm.aua_notify_date_lst.value = "";
	frm.aua_due_date_lst.value = "";

	_wbCompetencyInsertGetLstHelper(frm, self, "SELF", "frm.self_notify_datetime", "frm.self_due_datetime");
	_wbCompetencyInsertGetLstHelper(frm, mgmt, "MGMT", "frm.mgmt_notify_datetime", "frm.mgmt_due_datetime");
	_wbCompetencyInsertGetLstHelper(frm, peers, "PEERS", "frm.peers_notify_datetime", "frm.peers_due_datetime");
	_wbCompetencyInsertGetLstHelper(frm, reports, "REPORTS", "frm.reports_notify_datetime", "frm.reports_due_datetime");
	_wbCompetencyInsertGetLstHelper(frm, clients, "CLIENTS", "frm.clients_notify_datetime", "frm.clients_due_datetime");
	//_wbCompetencyInsertGetLstHelper(frm, resolved, "RESOLVED", "frm.resolver_notify_datetime", "frm.resolver_due_datetime");

	return;
}

function _wbCompetencyUpdateGetLst(frm) {
	var mgmt = _wbCompetencyGetGoldenManLst(frm.mgmt_assessor_ent_id_lst_fld,null,",");
	var peers = _wbCompetencyGetGoldenManLst(frm.peers_assessor_ent_id_lst_fld,null,",");
	var clients = _wbCompetencyGetGoldenManLst(frm.clients_assessor_ent_id_lst_fld,null,",");
	var reports = _wbCompetencyGetGoldenManLst(frm.reports_assessor_ent_id_lst_fld,null,",");
	//var resolved = _wbCompetencyGetGoldenManLst(frm.resolver_ent_id_lst_fld,null,",");
	var self = "";

	if(frm.self_assess_chk.checked) {
		self = frm.self_ent_id.value;
	}

	frm.assessor_ent_id_lst.value = self + "~" + mgmt + "~" + peers + "~" + reports + "~" + clients;
	frm.assessor_type_lst.value = "SELF~MGMT~PEERS~REPORTS~CLIENTS";
	frm.weight_lst.value = "1~1~1~1~1";

	frm.aua_type_lst.value = "SELF~MGMT~PEERS~REPORTS~CLIENTS";

	frm.aua_notify_date_lst.value = _wbCompetencyUpdateGetDatetime(frm, "frm.self_notify_datetime") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.mgmt_notify_datetime") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.peers_notify_datetime") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.reports_notify_datetime") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.clients_notify_datetime");
					// + "~" +
					//_wbCompetencyUpdateGetDatetime(frm, "frm.resolver_notify_datetime");

	frm.aua_due_date_lst.value = 	_wbCompetencyUpdateGetDatetime(frm, "frm.self_due_datetime", "23:59:59.0") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.mgmt_due_datetime", "23:59:59.0") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.peers_due_datetime", "23:59:59.0") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.reports_due_datetime", "23:59:59.0") + "~" +
					_wbCompetencyUpdateGetDatetime(frm, "frm.clients_due_datetime", "23:59:59.0");
					// + "~" +
					//_wbCompetencyUpdateGetDatetime(frm, "frm.resolver_due_datetime", "23:59:59.0");
	return;
}

function _wbCompetencyUpdateGetDatetime(frm, ele_name, time) {
	var yy = eval(ele_name + "_yy");
	var mm = eval(ele_name + "_mm");
	var dd = eval(ele_name + "_dd");
	if(time == null) {
		time = "00:00:00.0";
	}

	if(yy.value != null && yy.value != '') {
		return yy.value + "-" + mm.value + "-" + dd.value + " " + time;
	} else {
		return "";
	}

}


function _wbCompetencyUpdateValidateForm(frm, lang) {
	if(!_wbCompetencyValidateForm(frm,lang))
		return false;

	var mgmt = _wbCompetencyGetGoldenManLst(frm.mgmt_assessor_ent_id_lst_fld,null,",");
	var peers = _wbCompetencyGetGoldenManLst(frm.peers_assessor_ent_id_lst_fld,null,",");
	var clients = _wbCompetencyGetGoldenManLst(frm.clients_assessor_ent_id_lst_fld,null,",");
	var reports = _wbCompetencyGetGoldenManLst(frm.reports_assessor_ent_id_lst_fld,null,",");
	//var resolver = _wbCompetencyGetGoldenManLst(frm.resolver_ent_id_lst_fld,null,",");
	if(frm.assessee_ent_id_fld){
		if(_wbCompetencyGetGoldenManLst(frm.assessee_ent_id_fld) == ""){
				alert(eval("wb_msg_"+lang+"_plz_sel_assessee"));
			return false;
		}
	}

	var self = "";
	if(frm.self_assess_chk.checked) {
		if(frm.self_ent_id.value == ''){
			alert(eval("wb_msg_"+lang+"_plz_sel_assessee"));
			return false;
		}else{
			self = frm.self_ent_id.value;
		}
	}
	if(self == "" && mgmt == "" && peers == "" && clients == "" && reports == "" ){
		alert(eval('wb_msg_'+lang+'_plz_sel_assessors'))
		return false;
	}

	if(frm.self_notify_datetime_yy
	   && (frm.self_notify_datetime_yy.value != ""
	   || frm.self_notify_datetime_mm.value != ""
	   || frm.self_notify_datetime_dd.value != "")
	   && !wbUtilsValidateDate("frm.self_notify_datetime",eval("wb_msg_"+lang+"_nofity_date"))){
		return false;
	}
	if(frm.self_due_datetime_yy
	    && (frm.self_due_datetime_yy.value != ""
	    || frm.self_due_datetime_mm.value != ""
	    || frm.self_due_datetime_dd.value != "")
	    && !wbUtilsValidateDate("frm.self_due_datetime",eval("wb_msg_"+lang+"_due_date"))){
		return false;
	}

	if(frm.mgmt_notify_datetime_yy
	   && (frm.mgmt_notify_datetime_yy.value != ""
	   || frm.mgmt_notify_datetime_mm.value != ""
	   || frm.mgmt_notify_datetime_dd.value != "")
	   && !wbUtilsValidateDate("frm.mgmt_notify_datetime",eval("wb_msg_"+lang+"_nofity_date"))){
		return false;
	}
	if(frm.mgmt_due_datetime_yy
	    && (frm.mgmt_due_datetime_yy.value != ""
	    || frm.mgmt_due_datetime_mm.value != ""
	    || frm.mgmt_due_datetime_dd.value != "")
	    && !wbUtilsValidateDate("frm.mgmt_due_datetime",eval("wb_msg_"+lang+"_due_date"))){
		return false;
	}
	if(frm.peers_notify_datetime_yy
	   && (frm.peers_notify_datetime_yy.value != ""
	   || frm.peers_notify_datetime_mm.value != ""
	   || frm.peers_notify_datetime_dd.value != "")
	   && !wbUtilsValidateDate("frm.peers_notify_datetime",eval("wb_msg_"+lang+"_nofity_date"))){
		return false;
	}
	if(frm.peers_due_datetime_yy
	    && (frm.peers_due_datetime_yy.value != ""
	    || frm.peers_due_datetime_mm.value != ""
	    || frm.peers_due_datetime_dd.value != "")
	    && !wbUtilsValidateDate("frm.peers_due_datetime",eval("wb_msg_"+lang+"_due_date"))){
		return false;
	}
	if(frm.reports_notify_datetime_yy
	   && (frm.reports_notify_datetime_yy.value != ""
	   || frm.reports_notify_datetime_mm.value != ""
	   || frm.reports_notify_datetime_dd.value != "")
	   && !wbUtilsValidateDate("frm.reports_notify_datetime",eval("wb_msg_"+lang+"_nofity_date"))){
		return false;
	}
	if(frm.reports_due_datetime_yy
	    && (frm.reports_due_datetime_yy.value != ""
	    || frm.reports_due_datetime_mm.value != ""
	    || frm.reports_due_datetime_dd.value != "")
	    && !wbUtilsValidateDate("frm.reports_due_datetime",eval("wb_msg_"+lang+"_due_date"))){
		return false;
	}
	if(frm.clients_notify_datetime_yy
	   && (frm.clients_notify_datetime_yy.value != ""
	   || frm.clients_notify_datetime_mm.value != ""
	   || frm.clients_notify_datetime_dd.value != "")
	   && !wbUtilsValidateDate("frm.clients_notify_datetime",eval("wb_msg_"+lang+"_nofity_date"))){
		return false;
	}
	if(frm.clients_due_datetime_yy
	    && (frm.clients_due_datetime_yy.value != ""
	    || frm.clients_due_datetime_mm.value != ""
	    || frm.clients_due_datetime_dd.value != "")
	    && !wbUtilsValidateDate("frm.clients_due_datetime",eval("wb_msg_"+lang+"_due_date"))){
		return false;
	}
	/*if(frm.resolver_notify_datetime_yy
	   && (frm.resolver_notify_datetime_yy.value != ""
	   || frm.resolver_notify_datetime_mm.value != ""
	   || frm.resolver_notify_datetime_dd.value != "")
	   && !gen_validate_date("frm.resolver_notify_datetime",eval("wb_msg_"+lang+"_nofity_date"),lang)){
		return false;
	}
	if(frm.resolver_due_datetime_yy
	    && (frm.resolver_due_datetime_yy.value != ""
	    || frm.resolver_due_datetime_mm.value != ""
	    || frm.resolver_due_datetime_dd.value != "")
	    && !gen_validate_date("frm.resolver_due_datetime",eval("wb_msg_"+lang+"_due_date"),lang)){
		return false;
	}*/
	if(self !== '') {
		if(frm.self_notify_datetime_yy && frm.self_notify_datetime_yy.value != ""
		    && !wb_utils_validate_date_compare({
		    	frm : 'document.' + frm.name, 
		    	start_obj : "self_notify_datetime", 
		    	end_obj : "self_due_datetime", 
		    	start_nm : eval("wb_msg_"+lang+"_nofity_date"), 
		    	end_nm : eval("wb_msg_"+lang+"_due_date") 
		    	})) {
		    	return false;
		}
	}
	if(mgmt !== ''){
		if(frm.mgmt_notify_datetime_yy && frm.mgmt_notify_datetime_yy.value != ""
		    && !wb_utils_validate_date_compare({
		    	frm : 'document.' + frm.name, 
		    	start_obj : "mgmt_notify_datetime", 
		    	end_obj : "mgmt_due_datetime", 
		    	start_nm : eval("wb_msg_"+lang+"_nofity_date"), 
		    	end_nm : eval("wb_msg_"+lang+"_due_date")
		    	})) {
		    	return false;
		}
	}
	if(peers !== ''){
		if(frm.peers_notify_datetime_yy && frm.peers_notify_datetime_yy.value != ""
		    && !wb_utils_validate_date_compare({
		    	frm : 'document.' + frm.name, 
		    	start_obj : "peers_notify_datetime", 
		    	end_obj : "peers_due_datetime", 
		    	start_nm : eval("wb_msg_"+lang+"_nofity_date"), 
		    	end_nm : eval("wb_msg_"+lang+"_due_date")
		    	})) {
		    	return false;
		}
	}
	if(reports !== ''){
		if(frm.reports_notify_datetime_yy && frm.reports_notify_datetime_yy.value != ""
		    && !wb_utils_validate_date_compare({
		    	frm : 'document.' + frm.name, 
		    	start_obj : "reports_notify_datetime", 
		    	end_obj : "reports_due_datetime", 
		    	start_nm : eval("wb_msg_"+lang+"_nofity_date"), 
		    	end_nm : eval("wb_msg_"+lang+"_due_date")
		    	})) {
		    	return false;
		}
	}
	if(clients !== ''){
		if(frm.clients_notify_datetime_yy && frm.clients_notify_datetime_yy.value != ""
		    && !wb_utils_validate_date_compare({
		    	frm : 'document.' + frm.name, 
		    	start_obj : "clients_notify_datetime", 
		    	end_obj : "clients_due_datetime", 
		    	start_nm : eval("wb_msg_"+lang+"_nofity_date"), 
		    	end_nm : eval("wb_msg_"+lang+"_due_date")
		    	})) {
		    	return false;
		}
	}
	/*if(frm.resolvers_notify_datetime_yy && frm.resolvers_notify_datetime_yy.value != ""
	    && !gen_validate_date_compare(frm, "resolvers_notify_datetime", "resolvers_due_datetime", eval("wb_msg_"+lang+"_nofity_date"), eval("wb_msg_"+lang+"_due_date"), lang, "resolvers_notify_datetime")) {
	    	return false;
	}*/
	if(self != "") {
		if(frm.self_notify_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_notify_date"));
			frm.self_notify_datetime_yy.focus();
			return false;
		}
		if(frm.self_due_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_due_date"));
			frm.self_due_datetime_yy.focus();
			return false;
		}
	}
	/*if(frm.self_notify_datetime_yy.value != "" && self == "") {
		alert(eval("wb_msg_"+lang+"_select_self"));
	}*/
	if(mgmt != "") {
		if(frm.mgmt_notify_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_notify_date"));
			frm.mgmt_notify_datetime_yy.focus();
			return false;
		}
		if(frm.mgmt_due_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_due_date"));
			frm.mgmt_due_datetime_yy.focus();
			return false;
		}
	}
	/*if(frm.mgmt_notify_datetime_yy.value != "" && mgmt == "") {
		alert(eval("wb_msg_"+lang+"_select_mgmt"));
	}*/
	if(peers != "") {
		if(frm.peers_notify_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_notify_date"));
			frm.peers_notify_datetime_yy.focus();
			return false;
		}
		if(frm.peers_due_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_due_date"));
			frm.peers_due_datetime_yy.focus();
			return false;
		}
	}
	/*if(frm.peers_notify_datetime_yy.value != "" && peers == "") {
		alert(eval("wb_msg_"+lang+"_select_peers"));
	}*/
	if(reports != "") {
		if(frm.reports_notify_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_notify_date"));
			frm.reports_notify_datetime_yy.focus();
			return false;
		}
		if(frm.reports_due_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_due_date"));
			frm.reports_due_datetime_yy.focus();
			return false;
		}
	}
	/*if(frm.reports_notify_datetime_yy.value != "" && reports == "") {
		alert(eval("wb_msg_"+lang+"_select_reports"));
	}*/
	if(clients != "") {
		if(frm.clients_notify_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_notify_date"));
			frm.clients_notify_datetime_yy.focus();
			return false;
		}
		if(frm.clients_due_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_due_date"));
			frm.clients_due_datetime_yy.focus();
			return false;
		}
	}
	/*if(frm.clients_notify_datetime_yy.value != "" && clients == "") {
		alert(eval("wb_msg_"+lang+"_select_clients"));
	}*/
	/*if(resolver != "") {
		if(frm.resolver_notify_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_notify_date"));
			frm.resolver_notify_datetime_yy.focus();
			return false;
		}
		if(frm.resolver_due_datetime_yy.value == "") {
			alert(eval("wb_msg_"+lang+"_enter_due_date"));
			frm.resolver_due_datetime_yy.focus();
			return false;
		}
	}
	if(frm.resolver_notify_datetime_yy.value != "" && resolver == "") {
		alert(eval("wb_msg_"+lang+"_select_resolver"));
		return false;
	}	*/

	var vEle = new Array();
	var vSelf = new Array();
	vSelf[0] = frm.assessee_ent_id.value;
	vEle[0] = vSelf;
	vEle[1] = frm.mgmt_assessor_ent_id_lst_fld;
	vEle[2] = frm.peers_assessor_ent_id_lst_fld;
	vEle[3] = frm.reports_assessor_ent_id_lst_fld;
	vEle[4] = frm.clients_assessor_ent_id_lst_fld;

	if(!_wbCompetencyValidateRedundantGoldenMan(vEle)) {
		alert(eval("wb_msg_"+lang+"_redundant_assessor"));
		return false;
	}

	return true;
}


function wbCompetencyUpdateAssessmentExec(frm,lang,asm_id,timestamp,sks_id){

	frm.assessee_ent_id.value = _wbCompetencyGetGoldenManLst(frm.assessee_ent_id_fld);
	if(frm.sks_type[1].checked){
		frm.sks_id.value = _wbCompetencyGetGoldenManLst(frm.sks_id_fld,true);
	}else{
		frm.sks_id.value = sks_id;
	}
	if(!_wbCompetencyUpdateValidateForm(frm,lang))
		return;

	_wbCompetencyUpdateGetLst(frm);

	frm.review_start.value = frm.review_start_yy.value+"-"+frm.review_start_mm.value+"-"+frm.review_start_dd.value+" "+"00:00:00.0";
	frm.review_end.value = frm.review_end_yy.value+"-"+frm.review_end_mm.value+"-"+frm.review_end_dd.value+" "+"23:59:59.0";

	if(frm.auto_resolved_ind_chk){
		if(frm.auto_resolved_ind_chk.checked){
			frm.auto_resolved_ind.value = "true";
		}else{
			frm.auto_resolved_ind.value = "false";
		}
	}
	frm.url_failure.value = parent.location.href;
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_detail","asm_id",asm_id,"stylesheet",'comp_ass_get_ass.xsl');
	frm.asm_id.value = asm_id;
	frm.last_upd_timestamp.value = timestamp;
	frm.module.value = "competency.CompetencyModule";
	frm.cmd.value = "update_assessment";
	frm.action = wb_utils_disp_servlet_url;
	frm.notify_msg_subject.value = "Assessment Notify";
	frm.collect_msg_subject.value = "Assessment Due Date Notify";
	frm.method = "post";
	frm.submit();
}

function wbCompetencyDeleteAssessment(asm_id,lang){
	if(!confirm(eval("wb_msg_"+lang+"_rm_ass")))
		return;
	var url_success = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","refresh_ass_list","prepared","true","collected","true","notified","true","resolved","true","pagesize","10","cur_page","1","sort_by","asc","order_by","asm_eff_start_datetime","stylesheet",'comp_ass_get_list.xsl');
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","del_assessment","asm_id",asm_id,"url_success",url_success,"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencyPickSurveyPrep(){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_skill_set_list","cur_page","1","pagesize","10","sks_type","SKT","order_by","title","sort_by","ASC","stylesheet",'comp_ass_pick_svy.xsl',"url_failure","../htm/close_window.htm");
	wb_utils_open_win(url,"pickSVYWin",780,480);
}

function wbCompetencyPickSurveyExec(lang,fromFrm,toFrm){
	var sks_id = _wbCompetencyGetCheckboxLst(fromFrm.sks_id,true);
	if(fromFrm.sks_id.length){
		for(var i=0; i<fromFrm.sks_id.length; i++){
			if(fromFrm.sks_id[i].checked){
				var sks_title = fromFrm.sks_title[i].value;
			}
		}
	}else{
		if(fromFrm.sks_id.checked)
			var sks_title = fromFrm.sks_title.value;
	}
	if(sks_id==""){
		alert(eval("wb_msg_"+lang+"_select_svy"));
		return;
	}
	toFrm.sks_id_fld.options[0].text = sks_title;
	toFrm.sks_id_fld.options[0].value = sks_id;
	toFrm.sks_id_fld_single.value = sks_title;
	if(toFrm.usr_ent_id_fld.options && toFrm.usr_ent_id_fld.options[0].value!=""){
		toFrm.sks_title.value = sks_title + " for " + toFrm.usr_ent_id_fld.options[0].text;
	}else{
		toFrm.sks_title.value = sks_title;
	}
	window.close();
}

function wbCompetencyPickAssessorPrep(frm,asm_id){
	var usr_id_lst = _wbCompetencyGetGoldenManLst(frm.ass_id);
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","selected_user","usr_ent_id_lst",usr_id_lst,"asm_id",asm_id,"stylesheet",'comp_ass_upd_usr.xsl');
	wb_utils_open_win(url,"pickAssWin",780,480);
}

function wbCompetencyPickAssessorExec(frm,asm_id){
	var usr_ent_id_lst = _wbCompetencyGetCheckboxLst(frm.usr_ent_id);
	var usr_type_lst = _wbCompetencyGetSelectedLst(frm.usr_type,true);
	var usr_weight_lst = _wbCompetencyGetSelectedLst(frm.usr_weight,true);
	frm.cmd.value = "pick_user";
	frm.module.value = "competency.CompetencyModule";
	frm.usr_ent_id_lst.value = usr_ent_id_lst;
	frm.usr_type_lst.value = usr_type_lst;
	frm.usr_weight_lst.value = usr_weight_lst;
	frm.asm_id.value = asm_id;
	frm.url_failure.value = parent.location.href;
	frm.url_success.value = "../htm/close_and_reload_window.htm";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyUpdateAssessorPrep(asm_id,lang){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_detail","asm_id",asm_id,"stylesheet",'comp_ass_upd_usr_info.xsl');
	parent.location.href = url;
}

function wbCompetencyUpdateAssessorExec(frm,lang,asm_id){
	if(!_wbCompetencyValidateWeight(frm,lang))
		return;
	var usr_ent_id_lst = _wbCompetencyGetCheckboxLst(frm.usr_ent_id);
	var usr_type_lst = _wbCompetencyGetArrayLst(frm.status,true);
	var usr_weight_lst = _wbCompetencyGetCheckboxLst(frm.weight);
	frm.cmd.value = "upd_au_list";
	frm.module.value = "competency.CompetencyModule";
	frm.usr_ent_id_lst.value = usr_ent_id_lst;
	frm.usr_type_lst.value = usr_type_lst;
	frm.usr_weight_lst.value = usr_weight_lst;
	frm.asm_id.value = asm_id;
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_detail","asm_id",asm_id,"stylesheet",'comp_ass_get_ass.xsl');
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.notify_msg_subject.value = "Assessment Notify";
	frm.collect_msg_subject.value = "Assessment Due Date Notify";
	frm.submit();
}

function wbCompetencyDeleteAssessor(frm,lang,asm_id){
	if(!_wbCompetencyValidateForm(frm,lang)){
		return;
	}
	var usr_ent_id_lst = _wbCompetencyGetCheckboxLst(frm.assessor_id,false);
	frm.cmd.value = "del_assessor";
	frm.module.value = "competency.CompetencyModule";
	frm.usr_ent_id_lst.value = usr_ent_id_lst;
	frm.asm_id.value = asm_id;
	frm.url_success.value = parent.location.href;
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyPreviewAssessment(asm_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","preview_ass","asm_id",asm_id,"stylesheet",'comp_ass_view_ass.xsl');
	wb_utils_open_win(url,"previewAssWin",780,480);
}

function wbCompetencyGetAssessmentResult(asm_id,lang,from){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_result","asm_id",asm_id,"stylesheet",'comp_ass_get_res.xsl',"from",from);
	parent.location.href = url;
}

function wbCompetencyResolveAssessmentResult(frm,lang,asm_id,timestamp){
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.score_lst.value = _wbCompetencyGetCheckboxLst(frm.que_resolved);
	frm.asm_id.value = asm_id;
	frm.last_upd_timestamp.value = timestamp;
	frm.cmd.value = "save_resolved";
	frm.module.value = "competency.CompetencyModule";
	frm.url_failure.value = parent.location.href;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	if(getUrlParam("from") == "homepage") {
		frm.url_success.value = wb_utils_invoke_servlet('cmd','go_home');
	} else 	if(getUrlParam("from") == "to_do_list") {
		frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assigned_ass","stylesheet",'comp_assigned_ass_list.xsl');
	} else {
		frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_ass_detail","asm_id",asm_id,"stylesheet",'comp_ass_get_ass.xsl');
	}
	for(i=0; i<frm.skb_id.length; i++){
		if(i == 0){
			frm.skb_id_lst.value += frm.skb_id[i].value;
		} else {
			frm.skb_id_lst.value += '~' + frm.skb_id[i].value;
		}
	}
	frm.submit();
}

function wbCompetencyGetUserAssessmentResult(lang,asm_id,usr_ent_id,display_bil){
	if(display_bil!=null && display_bil!=""){
		wb_utils_set_cookie("assessor",display_bil);
	}
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assessor_result","asm_id",asm_id,"usr_ent_id",usr_ent_id,"stylesheet",'comp_ass_view_res.xsl');
	wb_utils_open_win(url, 'win',780,480)
}

function wbCompetencyPickApproverPrep(ent_id,js_name,lang,max_select){
	if(ent_id=="" || ent_id==null){
		alert(eval("wb_msg_"+lang+"_plz_sel_assessee"));
		return;
	}

	if(max_select==null)
		max_select = "";

	url = wb_utils_invoke_servlet('cmd','search_ent_lst','js_name',js_name,'stylesheet','itm_usr_srh_result.xsl','s_sort_by' ,'usr_display_bil','s_target_usr_ent_id',ent_id,'max_select',max_select)
		feature = 'toolbar='		+ 'no'
		+ ',location='				+ 'no'
		+ ',width=' 				+ 500
		+ ',height=' 				+ 400
		+ ',scrollbars='			+ 'auto'
		+ ',resizable='				+ 'yes'
		+ ',screenX='				+ '100'
		+ ',screenY='				+ '100'
		+ ',status='				+ 'no'
	wbUtilsOpenWin(url, 'win',false,feature)

}

function wbCompetencySkillGapAnalysisPrep(lang){
	wb_utils_set_cookie("sess_url","");
	var url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_skill_gap","stylesheet",'sk_ana_compare.xsl');
	parent.location.href = url;
}

function wbCompetencySkillGapAnalysisPrepURL(lang,val){
	wb_utils_set_cookie("sess_url","");
	var url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_skill_gap","stylesheet",'sk_ana_compare.xsl');
	if(val!=null && val!="")
		url += "&source_id=" + val + "&source_type=USR";
	return url;
}

function wbCompetencyPickUserGroup(frm,type){
	if(wb_utils_get_cookie("sess_url")=="")
		var url = window.location.href;
	else
		var url = wb_utils_get_cookie("sess_url");
	if(type!="usr"){
		var ele = eval("frm."+type+"_ent_id");
		url = setUrlParam(type+"_id",ele.options[0].value,url);
		ele = eval("frm."+type+"_type");
		var ele_val = _wbCompetencyGetCheckboxLst(ele,true);
		url = setUrlParam(type+"_type",ele_val,url);
	}else{
		url = setUrlParam("ent_id",frm.usr_ent_id.options[0].value,url);
	}
	wb_utils_set_cookie("sess_url",url);
	parent.location.href = url;
}

function wbCompetencyShowRecommendedCourse(usr_ent_id,skl_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","get_recommend_itm","usr_ent_id",usr_ent_id,"skl_id",skl_id,"stylesheet","sk_ana_recommend_cos.xsl");
	wb_utils_open_win(url,'RecommendCosWin',600,400);
}

function wbCompetencyGetUserCompetencyPrepURL(lang,val){
	wb_utils_set_cookie("sess_url","");
	var url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_ent_skill","stylesheet",'sk_ana_show_comp.xsl');
	if(val!=null && val!="")
		url += "&ent_id=" + val;
	return url;
}

function wbCompetencyGetUserCompetencyExec(frm,lang){
	var url = wb_utils_get_cookie("sess_url");
	if(frm.asm_type){
		var type = _wbCompetencyGetCheckboxLst(frm.asm_type,true);
		if(type=="resolved_date"){
			var type_val = frm.resolved_date.value;
			url = setUrlParam(type,type_val,url);
			url = setUrlParam('asm_id','',url);
		}else{
			var type_val = _wbCompetencyGetSelectedLst(frm.asm_id,true);
			if(type_val==""){
				alert(eval("wb_msg_"+lang+"_plz_sel_ass"));
				return;
			}
			url = setUrlParam(type,type_val,url);
		}
	}else if(frm.resolved_date_yy){
		if(!gen_validate_date("frm.resolved_date",eval("wb_msg_"+lang+"_ass_period"),lang))
			return;
		url = setUrlParam("resolved_date",frm.resolved_date_yy.value+"-"+frm.resolved_date_mm.value+"-"+frm.resolved_date_dd.value+" 23:59:59.0",url);
	}else{
		alert(eval("wb_msg_"+lang+"_sel_usrgrp"));
		return;
	}
	parent.location.href = url;
}

function wbCompetencySkillSearchPrepURL(lang,method){
	if(method!=null || method=="profile")
		var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","true","stylesheet","sk_ana_show_comp_usr.xsl");
	else
		var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","true","stylesheet",'sk_srh_pick_sk.xsl');
	return url;
}

function wbCompetencySkillSearchExec(frm,lang){
	frm.skl_id_lst.value = _wbCompetencyGetGoldenManLst(frm.skill_id_fld);
	if(!_wbCompetencyValidateForm(frm,lang))
		return;
	frm.skill_id_lst.value = frm.skl_id_lst.value;
	frm.level_lst.value = _wbCompetencyGetCheckboxLst(frm.skill_level);
	frm.type_lst.value = _wbCompetencyGetCheckboxLst(frm.skill_type);
	frm.stylesheet.value = 'sk_srh_get_res.xsl';
	frm.cmd.value = "search_user_by_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.submit();
}

function wbCompetencyPickSearchSkill(frm){
	var picked_skl_id_lst = _wbCompetencyGetGoldenManLst(frm.skill_id_fld);
	frm.picked_skl_id_lst.value = picked_skl_id_lst;
	frm.level_lst.value = _wbCompetencyGetCheckboxLst(frm.skill_level);
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","false","stylesheet",'sk_srh_pick_sk.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","false","stylesheet",'sk_srh_pick_sk.xsl');
	frm.cmd.value = "pick_search_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyDropSearchSkill(frm,lang){
	var picked_skl_id_lst = _wbCompetencyGetCheckboxLst(frm.skill_id_chk,false);
	frm.picked_skl_id_lst.value = picked_skl_id_lst;
	frm.level_lst.value = _wbCompetencyGetCheckboxLst(frm.skill_level);
	frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","false","stylesheet",'sk_srh_pick_sk.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","set_user_search","refresh","false","stylesheet",'sk_srh_pick_sk.xsl');
	frm.cmd.value = "pick_search_skill";
	frm.module.value = "competency.CompetencyModule";
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.submit();
}

function wbCompetencyShowUserSkill(usr_id){
    // need to pop up a new window for gap analysis between a user and some competencies with target level
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_user_skill","usr_ent_id",usr_id,"show_all","true","stylesheet",'sk_srh_get_usr_sk.xsl');
	wb_utils_open_win(url,"userSkillWin",780,480);
}

function wbCompetencyShowUserSkillAlternative(usr_id,sks_id){
    // no need to pop up a new window for gap analysis between a user and a competency profile
	var url = wb_utils_invoke_disp_servlet("module","competency.SkillGapModule","cmd","view_skill_gap","source_id",usr_id,"source_type","USR","target_id",sks_id,"target_type","PROF","stylesheet",'sk_ana_compare.xsl');
	parent.location.href = url;
}

function wbCompetencySkillSearchByCompetencyProfileExec(sks_id,lang){
	if(sks_id==null || sks_id==""){
		alert(eval("wb_msg_"+lang+"_plz_sel_cpty_prof"));
		return;
	}
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","search_user_by_skill_set","skill_set_id",sks_id,"stylesheet",'sk_srh_get_res.xsl',"url_failure",parent.location.href);
	parent.location.href = url;
}

function wbCompetencySubmitAssessmentPrep(asm_id,usr_ent_id){
	var url = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assessor_result","asm_id",asm_id,"usr_ent_id",usr_ent_id,"stylesheet",'comp_ass_submit_ass.xsl');
	parent.location.href = url;
}


function wbCompetencySubmitAssessmentExec(frm,count,doSend,last_upd_timestamp,que_count,lang){
	///*
	if(doSend=="true" && !_wbCompetencyValidateSubmitQuestion(frm,que_count,lang))
		return;
	//*/
	_wbCompetencySubmitGetAnswer(frm);
	if(doSend=="true" && !confirm(eval("wb_msg_"+lang+"_confirm_submit_svy"))) {
		return;
	}
	frm.cmd.value = "submit_ass";
	frm.module.value = "competency.CompetencyModule";
	frm.ass_submit.value = doSend;
	frm.total_section.value = count;
	frm.url_failure.value = parent.location.href;
	frm.last_upd_timestamp.value = last_upd_timestamp;
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	var from = getUrlParam("from");
	if(from == "homepage") {	
				frm.url_success.value = "Javascript:parent.window.opener.location.reload();window.close();";
	} else {
		frm.url_success.value = wb_utils_invoke_disp_servlet("module","competency.CompetencyModule","cmd","get_assigned_ass","stylesheet",'comp_assigned_ass_list.xsl');
	}

	frm.submit();
}

function wbCompetencySubmitModuleAssessmentPrep(mod_type,mod_id,tpl_use,cos_id,lang,url_success,cookieNm){
	str_feature = 'toolbar='				+ 'no'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '420'
		+ ',scrollbars='				+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',status='				+ 'no'
	if(document.all){
		str_feature += ',top=' + '10' + ',left=' + '10'
	}else{
		str_feature += ',screenX=' + '10' + ',screenY=' + '10'
	}
	cmd = 'get_tst'
	wb_utils_set_cookie('isWizpack', 'false')
	gen_del_cookie(cookieNm);
	gen_del_cookie("quiz_time_left");
	gen_del_cookie("quiz_que_flg");
	gen_del_cookie("quiz_interaction");
	url = wb_utils_invoke_servlet('cmd','get_mod_status','mod_id',mod_id,'mod_type',mod_type,'tpl_use', tpl_use,'cos_id',cos_id,'url_failure','../htm/close_window.htm','stylesheet','start_module.xsl','url_success',url_success)
	test_player = wbUtilsOpenWin('', 'test_player', false, str_feature);
	test_player.document.write('<font size="2" face="Arial">'+eval('wb_msg_'+lang+'_loading_msg')+'</font>');
	test_player.location.href = url;
	test_player.focus();
}

function wbCompetencyCancelAssessmentExec(lang){
	if(confirm(eval('wb_msg_'+lang+'_cpty_cancel'))){
		window.close();//history.back()
	}
}

function wbCompetencySkillGapAnalysisExec(frm,lang){
	var url = wb_utils_get_cookie("sess_url");
	var type = _wbCompetencyGetCheckboxLst(frm.source_type,true);
	if(type=="USR"){
		var id = frm.source_ent_id.options[0].value;
		if(id==""){
			alert(eval("wb_msg_"+lang+"_sel_usrgrp_src"));
			return;
		}
		if(frm.source_asm_type.length){
			var asm_type = _wbCompetencyGetCheckboxLst(frm.source_asm_type,true);
			if(asm_type=="source_date"){
				var asm_type_value = frm.source_date.value;
			}else if(asm_type=="source_asm_id"){
				var asm_type_value = _wbCompetencyGetSelectedLst(frm.source_asm_id,true);
				if(asm_type_value==""){
					alert(eval("wb_msg_"+lang+"_plz_sel_ass"));
					return;
				}
			}
		}else{
			var asm_type = frm.source_asm_type.value;
			if(frm.source_date_yy && frm.source_date_mm && frm.source_date_dd){
				if(!gen_validate_date("frm.source_date",eval("wb_msg_"+lang+"_ass_period"),lang))
					return;
				var asm_type_value = frm.source_date_yy.value+"-"+frm.source_date_mm.value+"-"+frm.source_date_dd.value+" 23:59:59.0";
			}else{
				var asm_type_value = frm.source_date.value;
			}
		}
	}else if(type=="PROF"){
		var id = _wbCompetencyGetSelectedLst(frm.source_id,true);
		if(id==""){
			alert(eval("wb_msg_"+lang+"_plz_sel_cpty_prof"));
			return;
		}
	}
	url = setUrlParam("source_type",type,url);
	url = setUrlParam("source_id",id,url);
	if(asm_type!=null && asm_type_value!=null)
		url = setUrlParam(asm_type,asm_type_value,url);
	var type = _wbCompetencyGetCheckboxLst(frm.target_type,true);
	if(type=="USR"){
		var id = frm.target_ent_id.options[0].value;
		if(id==""){
			alert(eval("wb_msg_"+lang+"_sel_usrgrp_tgt"));
			return;
		}
		if(frm.target_asm_type.length){
			var asm_type = _wbCompetencyGetCheckboxLst(frm.target_asm_type,true);
			if(asm_type=="target_date"){
				var asm_type_value = frm.target_date.value;
			}else if(asm_type=="target_asm_id"){
				var asm_type_value = _wbCompetencyGetSelectedLst(frm.target_asm_id,true);
				if(asm_type_value==""){
					alert(eval("wb_msg_"+lang+"_plz_sel_ass"));
					return;
				}
			}
		}else{
			var asm_type = frm.target_asm_type.value;
			if(frm.target_date_yy && frm.target_date_mm && frm.target_date_dd){
				if(!gen_validate_date("frm.target_date",eval("wb_msg_"+lang+"_ass_period"),lang))
					return;
				var asm_type_value = frm.target_date_yy.value+"-"+frm.target_date_mm.value+"-"+frm.target_date_dd.value+" 23:59:59.0";
			}else{
				var asm_type_value = frm.target_date.value;
			}
		}
	}else if(type=="PROF"){
		var id = _wbCompetencyGetSelectedLst(frm.target_id,true);
		if(id==""){
			alert(eval("wb_msg_"+lang+"_plz_sel_cpty_prof"));
			return;
		}
	}
	url = setUrlParam("target_type",type,url);
	url = setUrlParam("target_id",id,url);
	if(asm_type!=null && asm_type_value!=null)
		url = setUrlParam(asm_type,asm_type_value,url);
	parent.location.href = url;
}

function wbCompetencySort(col,order,timestamp){
	var org_col = getUrlParam("order_by");
	if(org_col!=col){
		order = "ASC";
	}
	var url = setUrlParam("order_by",col,parent.location.href);
	url = setUrlParam("sort_by",order,url);
	if(timestamp!=null && timestamp!="")
		url = setUrlParam("timestamp",timestamp,url);
	parent.location.href = url;
}


function _wbCompetencySubmitGetAnswer(frm){
	var i, n, ele;
	frm.skill_id_lst.value = "";
	frm.answer_lst.value = "";
	n = frm.elements.length;
	for (i = 0; i < n; i++) {
		ele = frm.elements[i];
		if (ele.type == "radio" && ele.checked) {
			if (ele.value != "")
				frm.skill_id_lst.value = frm.skill_id_lst.value + ele.name + "~";
				frm.answer_lst.value = frm.answer_lst.value + ele.value + "~";
		}
	}
	if (frm.skill_id_lst.value != "") {
		frm.skill_id_lst.value = frm.skill_id_lst.value.substring(0, frm.skill_id_lst.value.length-1);
	}
	if (frm.answer_lst.value != "") {
		frm.answer_lst.value = frm.answer_lst.value.substring(0, frm.answer_lst.value.length-1);
	}
	return;
}

function _wbCompetencyValidateForm(frm,lang){

	if(frm.approver_role_lst && frm.self_assess && frm.approver_role_lst.value=='' && frm.self_assess.value=="false") {
//	if(frm.approver_role_lst && frm.resolver_role_lst && frm.self_assess && frm.approver_role_lst.value=='' && frm.resolver_role_lst.value=='' && frm.self_assess.value=="false") {
		alert(eval("wb_msg_"+lang+"_choose_assessment"));
		frm.self_assess_chk.focus();
		return false;
	}
	if(frm.approver_role_lst && frm.approver_role_lst.value!='' && frm.approver_role_notify_date_yy && !wbUtilsValidateDate("frm.approver_role_notify_date",eval("wb_msg_"+lang+"_nofity_date"),lang))
		return false;
	if(frm.approver_role_lst && frm.approver_role_lst.value!='' && frm.approver_role_due_date_yy && !wbUtilsValidateDate("frm.approver_role_due_date",eval("wb_msg_"+lang+"_due_date"),lang))
		return false;
	//if(frm.resolver_role_lst && frm.resolver_role_lst.value!='' && frm.resolver_role_notify_date_yy && !gen_validate_date("frm.resolver_role_notify_date",eval("wb_msg_"+lang+"_nofity_date"),lang))
	//	return false;
	//if(frm.resolver_role_lst && frm.resolver_role_lst.value!='' && frm.resolver_role_due_date_yy && !gen_validate_date("frm.resolver_role_due_date",eval("wb_msg_"+lang+"_due_date"),lang))
	//	return false;
	if(frm.self_assess && frm.self_assess.value=="true" && frm.self_notify_date_yy && !wbUtilsValidateDate("frm.self_notify_date",eval("wb_msg_"+lang+"_nofity_date"),lang))
		return false;
	if(frm.self_assess && frm.self_assess.value=="true" && frm.self_due_date_yy && !wbUtilsValidateDate("frm.self_due_date",eval("wb_msg_"+lang+"_due_date"),lang))
		return false;

	if(frm.search && !gen_validate_empty_field(frm.search,eval("wb_msg_"+lang+"_search_field"),lang)){
		frm.search.focus();
		return false;
	}
	if(frm.title) {
		frm.title.value = wbUtilsTrimString(frm.title.value);
		if (!gen_validate_empty_field(frm.title, eval("wb_msg_" + lang + "_sk_title"), lang)) {
			frm.title.focus();
			return false;
		}
	}
	if(frm.skl_description) {
		frm.skl_description.value = wbUtilsTrimString(frm.skl_description.value);
		if (frm.skl_description.value.length > 4000) {
			alert(eval("wb_msg_" + lang + "_short_desc"));
			frm.skl_description.focus();
			return false;
		}
	}
	if(frm.description) {
		frm.description.value = wbUtilsTrimString(frm.description.value);
		if (frm.description.value.length > 4000) {
			alert(eval("wb_msg_" + lang + "_short_desc"));
			frm.description.focus();
			return false;
		}
	}
	if(frm.scale_title) {
		frm.scale_title.value = wbUtilsTrimString(frm.scale_title.value);
		if (!gen_validate_empty_field(frm.scale_title, eval("wb_msg_" + lang + "_sk_title"), lang)) {
			frm.scale_title.focus();
			return false;
		}
	}
	if(frm.level_value && frm.level_label && frm.level_desc && !_wbCompetencyValidateScaleLevel(frm,lang)){
		return false;
	}
	if(frm.rule_type && frm.rule_type.options[frm.rule_type.selectedIndex].value==""){
		alert(eval("wb_msg_"+lang+"_plz_sel_rule"));
		return false;
	}
	if(frm.sel_scale && frm.sel_scale.options[frm.sel_scale.selectedIndex].value==""){
		alert(eval("wb_msg_"+lang+"_plz_sel_scale"));
		return false;
	}
	if(frm.usr_ent_id && !gen_validate_empty_field(frm.usr_ent_id,eval("wb_msg_"+lang+"_assessee"),lang)){
		return false;
	}
	if(frm.assessee_ent_id_lst && !gen_validate_empty_field(frm.assessee_ent_id_lst,eval("wb_msg_"+lang+"_assessee"),lang)){
		return false;
	}
	if(frm.sks_id && frm.sks_id.value==""){
		alert(eval("wb_msg_"+lang+"_select_svy"));
		return false;
	}
	if(frm.sks_title) {
		frm.sks_title.value = wbUtilsTrimString(frm.sks_title.value);
		if (!gen_validate_empty_field(frm.sks_title, eval("wb_msg_" + lang + "_sk_title"), lang)) {
			frm.sks_title.focus();
			return false;
		}
	}
	if(frm.skl_id_lst && frm.skl_id_lst.value==""){
		alert(eval("wb_msg_"+lang+"_plz_pick_sk"));
		return false;
	}
	if(frm.skill_level && !_wbCompetencyValidateSkillLevel(frm,lang)){
		return false;
	}
	if(frm.review_start_yy && !wbUtilsValidateDate("frm.review_start",eval("wb_msg_"+lang+"_review_period"))){
		return false;
	}
	if(frm.review_end_yy && !wbUtilsValidateDate("frm.review_end",eval("wb_msg_"+lang+"_review_period"))){
		return false;
	}
	if(frm.review_start_yy && frm.review_start_yy.value != ""
	    && frm.review_end_yy && frm.review_end_yy.value != ""
	    && !wb_utils_validate_date_compare({
	    	frm : 'document.' + frm.name, 
	    	start_obj : "review_start", 
	    	end_obj : "review_end"
	    	})) {
	    	return false;
	}
	
	if(frm.notify_date_yy && !wbUtilsValidateDate("frm.notify_date",eval("wb_msg_"+lang+"_nofity_date"))){
		return false;
	}
	if(frm.due_date_yy && !wbUtilsValidateDate("frm.due_date",eval("wb_msg_"+lang+"_due_date"))){
		return false;
	}
	if(frm.que_resolved && !_wbCompetencyValidateResolvedResult(frm,lang)){
		return false;
	}
	if(frm.assessor_id && _wbCompetencyGetCheckboxLst(frm.assessor_id,true)==""){
		alert(eval("wb_msg_"+lang+"_sel_one_usr"));
		return false;
	}
	if (frm.skill_priority && !_wbCompetencyValidateSkillPriority(frm, lang)) {
		return false;
	}
	return true;
}

function _wbCompetencyValidateResolvedResult(frm,lang){
	if(frm.que_resolved.length){
		for(var i=0;i<frm.que_resolved.length; i++){
			if(!gen_validate_empty_field(frm.que_resolved[i],eval("wb_msg_"+lang+"_resolved"),lang))
				return false;
			if(!gen_validate_float(frm.que_resolved[i],eval("wb_msg_"+lang+"_resolved"),lang))
				return false;
			if(parseFloat(frm.que_resolved[i].value)>parseFloat(frm.scale_max_lvl[i].value)){
				alert(eval("wb_msg_"+lang+"_plz_lt_max_scale_lvl"));
				return false;
			}
		}
	}else{
		if(!gen_validate_empty_field(frm.que_resolved,eval("wb_msg_"+lang+"_resolved"),lang))
			return false;
		if(!gen_validate_float(frm.que_resolved,eval("wb_msg_"+lang+"_resolved"),lang))
			return false;
		if(parseFloat(frm.que_resolved.value)>parseFloat(frm.scale_max_lvl.value)){
			alert(eval("wb_msg_"+lang+"_plz_lt_max_scale_lvl"));
			return false;
		}
	}
	return true;
}

function _wbCompetencyValidateSkillLevel(frm,lang){
	if(frm.skill_level.length){
		for(var i=0; i<frm.skill_level.length; i++){
			if(!gen_validate_empty_field(frm.skill_level[i],eval("wb_msg_"+lang+"_tgt_level"),lang)){
				frm.skill_level[i].focus();
				return false;
			}
			
			if(!gen_validate_integer(frm.skill_level[i],eval("wb_msg_"+lang+"_tgt_level"),lang)){
				frm.skill_level[i].focus();
				return false;
			}
			if(parseFloat(frm.skill_level[i].value)>parseFloat(frm.skill_level_max[i].value) || parseFloat(frm.skill_level[i].value)<0){
				alert(eval("wb_msg_"+lang+"_plz_lt_max_scale_lvl"));
				frm.skill_level[i].focus();
				return false;
			}
		}
	}else{
		if(!gen_validate_empty_field(frm.skill_level,eval("wb_msg_"+lang+"_tgt_level"),lang)){
			frm.skill_level.focus();
			return false;
		}
		if(!gen_validate_integer(frm.skill_level,eval("wb_msg_"+lang+"_tgt_level"),lang)){
			frm.skill_level.focus();
			return false;
		}
		if(parseFloat(frm.skill_level.value)>parseFloat(frm.skill_level_max.value) || parseFloat(frm.skill_level.value)<0){
			alert(eval("wb_msg_"+lang+"_bet")+"0 ~ "+frm.skill_level_max.value);
			frm.skill_level.focus();
			return false;
		}
	}
	return true;
}

function _wbCompetencyValidateWeight(frm,lang){
	if(frm.weight.length){
		for(var i=0;i<frm.weight.length;i++){
			if(!gen_validate_empty_field(frm.weight[i],eval("wb_msg_"+lang+"_weight"),lang))
				return false;
			if(!gen_validate_positive_integer(frm.weight[i],eval("wb_msg_"+lang+"_weight"),lang))
				return false;
		}
	}else{
		if(!gen_validate_empty_field(frm.weight,eval("wb_msg_"+lang+"_weight"),lang))
			return false;
		if(!gen_validate_positive_integer(frm.weight,eval("wb_msg_"+lang+"_weight"),lang))
			return false;
	}
	return true;
}

function _wbCompetencyValidateScaleLevel(frm,lang){
	var i, j;
	i = _wbCompetencyGetScaleLevel(frm);
	if(i<2){
		alert(eval("wb_msg_"+lang+"_at_least_two_lvl"));
		frm.level_value[i].focus();
		return false;
	}
	var hasSameLevel = false;
	var totalLevel = "";
	for(j=i;j>0;j--){
		frm.level_desc[j-1].value = wbUtilsTrimString(frm.level_desc[j-1].value);
		frm.level_value[j-1].value = wbUtilsTrimString(frm.level_value[j-1].value);
		if(frm.level_value[j-1].value=="" || frm.level_label[j-1].value==""){
			alert(eval("wb_msg_"+lang+"_enter_value")+" "+eval("wb_msg_"+lang+"_lvl")+" "+j);
			frm.level_value[j-1].focus();
			return false;
		}
		if(!gen_validate_positive_integer(frm.level_value[j-1],eval("wb_msg_"+lang+"_lvl"),lang))
			return false;
		if(frm.level_desc[j-1].value.length>4000){
			alert(eval("wb_msg_"+lang+"_short_desc"));
			frm.level_desc[j-1].focus();
			return false;
		}
		var aLevel = "~" + frm.level_value[j-1].value + "~";
		if (totalLevel.indexOf(aLevel) != -1) {
			alert(eval("wb_msg_"+lang+"_lvl")+eval("wb_msg_"+lang+"_user_exist"))
			frm.level_value[j-1].focus();
			return false;
		}
		totalLevel = totalLevel + aLevel;
	}
	return true;
}

///*
function _wbCompetencyValidateSubmitQuestion(frm,que_count,lang){
	var answerAll = true;
	var notAnsQu = "";
	var firstEmptyField = "";
	var firstEmptyQu = "";

	for(var i=1; i<=que_count; i++){

		var ele = eval("document."+frm.name+".answer_"+i);
		if (ele != null && ele.length){
			var thisAnswered = false;
			for(var j=0;j<ele.length;j++){
				if(ele[j].type && ele[j].type == 'radio' && ele[j].checked == true ){
					thisAnswered = true;
					break;
				}
			}
			if(thisAnswered == false){
				if(firstEmptyField == ""){
					firstEmptyField = ele[0]
					firstEmptyQu = i
				}
				answerAll = false;
				notAnsQu += i + ","
			}
		}



	}
	if(!answerAll) {
		notAnsQu = notAnsQu.substring(0,notAnsQu.length-1)
		alert(eval("wb_msg_"+lang+"_sel")+" " + eval("wb_msg_"+lang+"_que")+" "+notAnsQu)
			parent.location.href = "#q" + firstEmptyQu + "_1";
			return false;
	} else {
		return true;
	}
}
//*/

function _wbCompetencyFeedScaleLevel(frm){
	var i, ele;
	var total_level_cnt = frm.level_value.length;
	for(i=1;i<=total_level_cnt;i++){
		ele = eval("frm.level_value_"+i);
		ele.value = wbUtilsTrimString(frm.level_value[i-1].value);
		ele = eval("frm.level_label_"+i);
		ele.value = wbUtilsTrimString(frm.level_label[i-1].value);
		ele = eval("frm.level_desc_"+i);
		ele.value = wbUtilsTrimString(frm.level_desc[i-1].value);
	}
}

function _wbCompetencyGetScaleLevel(frm){
	var i;
	var total_level_cnt = frm.level_value.length;
	for(i=total_level_cnt;i>0;i--){
		if(frm.level_value[i-1].value!="" && frm.level_label[i-1].value!="")
			break;
	}
	return i;
}

function _wbCompetencyValidateRedundantGoldenMan(vEle) {
	var total = "";
	var val = "";

	for(var i=0; i<vEle.length; i++) {
		var ele = vEle[i];
		for(var j=0; j<ele.length; j++) {
			if(ele.options) {
				val = "~"+ele.options[j].value+"~"
			} else {
				val = "~"+ele+"~"
			}
			if(total.indexOf(val) > -1) {
				return false;
			} else {
				total += val;
			}
		}
	}
	return true;
}

function _wbCompetencyGetGoldenManLst(ele,selected,separator){
	var list = "";
	if(separator==null) {
		separator = "~";
	}
	for(var i=0;i<ele.options.length;i++){
		if(selected==null || (selected && ele.options[i].selected) || (!selected && !ele.options[i].selected))
			list += ele.options[i].value + separator;
	}
	if(list.indexOf(separator)>-1)
		list = list.substr(0,list.length-1);
	return list;
}

function _wbCompetencyGetSelectedLst(ele,selected){
	var list = "";
	if(ele.length && ele[0].options){
		for(var i=0; i<ele.length; i++){
			list += _wbCompetencyGetGoldenManLst(ele[i],selected) + "~";
		}
	}else{
		list = _wbCompetencyGetGoldenManLst(ele,selected);
	}
	if(list.indexOf("~")>-1)
		list = list.substr(0,list.length-1);
	return list;
}

function _wbCompetencyGetArrayLst(ele){
	var list = "";
	if(ele.length){
		for(var i=0; i<ele.length; i++){
			list += ele[i].value + "~";
		}
	}else{
		list = ele.value;
	}
	if(list.indexOf("~")>-1)
		list = list.substr(0,list.length-1);
	return list;
}

function _wbCompetencyGetCheckboxLst(ele,checked){
	var list = "";
	if(ele){
		if(ele.length){
			for(var i=0;i<ele.length;i++){
				if(checked==null || (checked && ele[i].checked) || (!checked && !ele[i].checked)){
					list += ele[i].value + "~";
				}
			}
		}else{
			if(checked==null || (checked && ele.checked) || (!checked && !ele.checked))
				list = ele.value;
		}
	}
	if(list.indexOf("~")>-1)
		list = list.substr(0,list.length-1);
	return list;
}

function  _wbCompetencyGetPickedSkillList(frm,checked){
	var skill = "";
	if(frm.skill_id_chkbox){
		if(frm.skill_id_chkbox.length){
			for(var i=0;i<frm.skill_id_chkbox.length;i++){
				if(checked==null || (checked && frm.skill_id_chkbox[i].checked) || (!checked && !frm.skill_id_chkbox[i].checked)){
					skill += frm.skill_id_chkbox[i].value + "~";
				}
			}
		}else{
			if(checked==null || (checked && frm.skill_id_chkbox.checked) || (!checked && !frm.skill_id_chkbox.checked))
				skill = frm.skill_id_chkbox.value;
		}
	}
	if(skill.indexOf("~")>-1)
		skill = skill.substr(0,skill.length-1);
	return skill;
}

function  _wbCompetencyGetPickedSkillLevelList(frm,checked){
	var level = "";
	if(frm.skill_level){
		if(frm.skill_id_chkbox.length){
			for(var i=0;i<frm.skill_level.length;i++){
				if(checked==null || (checked && frm.skill_id_chkbox[i].checked) || (!checked && !frm.skill_id_chkbox[i].checked)){
					level += frm.skill_level[i].value + "~";
				}
			}
		}else{
			if(checked==null || (checked && frm.skill_id_chkbox.checked) || (!checked && !frm.skill_id_chkbox.checked))
				level = frm.skill_level.value;
		}
	}
	if(level.indexOf("~")>-1)
		level = level.substr(0,level.length-1);
	return level;
}

function  _wbCompetencyGetPickedSkillPriorityList(frm,checked){
	var priority = "";
	if(frm.skill_priority){
		if(frm.skill_id_chkbox.length){
			for(var i=0;i<frm.skill_priority.length;i++){
				if(checked==null || (checked && frm.skill_id_chkbox[i].checked) || (!checked && !frm.skill_id_chkbox[i].checked)){
					priority += frm.skill_priority[i].value + "~";
				}
			}
		}else{
			if(checked==null || (checked && frm.skill_id_chkbox.checked) || (!checked && !frm.skill_id_chkbox.checked))
				priority = frm.skill_priority.value;
		}
	}
	if(priority.indexOf("~")>-1)
		priority = priority.substr(0,priority.length-1);
	return priority;
}

function _wbCompetencyValidateSkillPriority(frm,lang){
	if(frm.skill_priority.length){
		for(var i=0; i<frm.skill_priority.length; i++){
			if(!gen_validate_empty_field(frm.skill_priority[i],eval("wb_msg_"+lang+"_importance"),lang)){
				frm.skill_priority[i].focus();
				return false;
			}
			if(!gen_validate_integer(frm.skill_priority[i],eval("wb_msg_"+lang+"_importance"),lang)){
				frm.skill_priority[i].focus();
				return false;
			}
		}
	}else{
		if(!gen_validate_empty_field(frm.skill_priority,eval("wb_msg_"+lang+"_importance"),lang)){
			frm.skill_priority.focus();
			return false;
		}
		if(!gen_validate_integer(frm.skill_priority,eval("wb_msg_"+lang+"_importance"),lang)){
			frm.skill_priority.focus();
			return false;
		}
	}
	return true;
}
