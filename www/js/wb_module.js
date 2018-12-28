// ------------------ wizBank Module object -------------------
// Convention:
//   public functions : use "wbModule" prefix
//   private functions: use "_wbModule" prefix
// ------------------------------------------------------------
/* constructor */
function wbModule() {
	this.get_view_url = wbModuleGetURL
	this.upd_tst_prep = wbModuleUpdTestPrep		// for update information of a test ( DXT/STX/EXC/TST )
	this.upd_svy_prep = wbModuleUpdSvyPrep
	this.upd_prep = wbModuleUpdPrep				// for update basic information of a module  (should remove this fucntion in version 3)
	this.upd_prep_url = wbModuleUpdPrepURL		// for update basic information of a module
	this.upd_exec = wbModuleUpdExec				// for update basic information of module
	this.upd_exec2 = wbModuleUpdExec2			// for update basic information of module (of a class of a common content course)

	// for instructor
	this.usr_report = wbModuleUserReport
	this.preview_exec = wbModulePreviewExec
	this.preview_content_url = wbModulePreviewContentURL

	this.view_info_url = wbModuleViewInformationURL
	this.preview_ref = wbModulePreviewRef
	this.edit_ref_exec = wbModuleEditRefExec
	this.cancel_add = wbModuleCancelAdd
	this.save_rating_que = wbModuleSaveRatingQuestion
	this.define_rating_que = wbModuleDefineRatingQuestion
	this.dl_report = wbModuleDownloadReport
	this.dl_report_by_itm = wbModuleDownloadReportByItm

	// for learner
	this.start_exec = wbModuleStartExec
	this.start_bkmark_exec = wbModuleStartBookmarkExec
	this.start_ref_exec = wbModuleStartRefExec
	this.start_content_url = wbModuleStartContentURL
	this.reload = wbModuleReload

	// for self exercies only (STX)
	this.stx_pick_obj = wbModuleSTXPcikObjective
	this.start_stx = wbModuleStartSTX

	// for wizpack only
	this.wizpack_header_url = wbModuleWizPackHeaderURL
	this.wizpack_footer_url = wbMoudleWizPackFooterURL
	this.wizpack_content_url = wbModuleWizPackContentURL

	// for aicc_course
	this.start_aicc_au = wbModuleStartAICCAu
	this.preview_aicc_au = wbModulePreviewAICCAu

	// for scorm_course
	this.start_scorm = wbModuleStartSCORM
	this.preview_scorm = wbModulePreviewSCORM
	this.point_scorm = wbModulePointSCORM
	// for netg_course
	this.start_netg = wbModuleStartNETg
	this.preview_netg = wbModulePreviewNETg

	// Glossary
	this.upd_glo_key = wbModuleUpdGloKey
	this.del_glo_key = wbModuleDelGloKey
	this.ins_glo_key = wbModuleInsGloKey
	this.add_glo_key = wbModuleAddGloKey
	this.get_glo_keys = wbModuleGetGloKeys
	this.get_glo_key = wbModuleGetGloKey

	//View SVY Submission
	this.sort_subn_list = wbModuleSortSubmissionList
	this.get_subn_list_by_status = wbModuleGetSubmissionListByStatus
	this.get_subn_list = wbModuleGetSubmissionList
	this.chg_user_mov_status = wbModuleChangeUserStatus
	this.view_svy_res = wbModuleViewSvyResult

	this.view_evn_res = wbModuleViewEvnResult
	this.export_report = wbModuleExportReport
	this.export_report_by_tkhids = wbModuleExportReportByTkhIds

	// for EAS
	this.get_mod_eas_status_list = wbModuleEASGetSubmissionListByStatus
	this.edit_mod_eas_score = wbModuleEASEditScore
	this.view_mod_eas_status_list = wbModuleEASGetSubmissionList
	this.get_mod_eas_individual = wbModuleEASGetIndividual
	this.mod_eas_view_result = wbModuleEASViewResult
	this.mod_eas_grade_individual = wbModuleEASGradeIndividual
	this.mod_eas_grade_all = wbModuleEASGradeAll
	this.mod_eas_mass_update_one = wbModuleEASMassUpdOne
	this.mod_eas_reset_individual = wbModuleEASResetIndividual
	
	// View DXT TST Submission
	this.view_submission_lst = wbModuleViewSubmissionLst
	this.back_submission_lst = wbModuleBackSubmissionLst
	this.view_submission = wbModuleViewSubmission
	this.grade_submission = wbModuleGradeSubmission
	this.grade_submission_exec = wbModuleGradeSubmissionExec
	
	this.start_prev = wbModuleStartPrev
	this.gen_test_progress_url = wbModuleGenTestURL
	
	this.set_tst_status = wbModuleSetTstStatus
	
	this.show_mgt_content = wbModuleMgtContent
	
	this.vod_res_main_info = wbVodResMainInfo;
}

/* private functions */
function wbModuleViewSubmissionLst(cos_id,mod_id,ass_queue){
	var s_usr_id_name='';
	if(ass_queue == null){
		ass_queue = 'all';
	}
	if(ass_queue == 'search'){
		s_usr_id_name=$('#s_usr_id_name').val()
		ass_queue=$("#ass_queue").val();
	}
	var url = wb_utils_invoke_servlet('cmd','get_submission_lst','course_id',cos_id,'mod_id',mod_id,'ass_queue',ass_queue,'stylesheet','tst_sub_lst.xsl','user_code',s_usr_id_name)
	window.location.href = url;
	return;	
}

function wbModuleBackSubmissionLst(cos_id,mod_id,ass_queue){
	if(ass_queue == null){
		ass_queue = 'all';
	}
	var url = wb_utils_invoke_servlet('cmd','get_submission_lst','course_id',cos_id,'mod_id',mod_id,'ass_queue',ass_queue,'stylesheet','tst_sub_lst.xsl')
	window.location.href = url;
}

function wbModuleViewSubmission(rpt_usr_id,mod_id,attempt_nbr,que_id_lst,tkh_id,course_id){
	if(que_id_lst == null || que_id_lst == ''){
		que_id_lst = 0;
	}
	var url = wb_utils_invoke_servlet('cmd','get_rpt_usr','rpt_usr_id',rpt_usr_id,'mod_id',mod_id,'attempt_nbr',attempt_nbr,'que_id_lst',que_id_lst,'tkh_id',tkh_id,'stylesheet','tst_rpt.xsl','course_id',course_id)
	window.location.href = url;
}

function wbModuleGradeSubmission(rpt_usr_id,mod_id,attempt_nbr,que_id_lst,tkh_id,course_id){
	if(que_id_lst == null || que_id_lst == ''){
		que_id_lst = 0;
	}
	var url = wb_utils_invoke_servlet('cmd','get_rpt_usr','rpt_usr_id',rpt_usr_id,'mod_id',mod_id,'attempt_nbr',attempt_nbr,'que_id_lst',que_id_lst,'tkh_id',tkh_id,'stylesheet','tst_grade.xsl','isExcludes',false,'course_id',course_id)
	window.location.href = url;
}

function wbModuleGradeSubmissionExec(frm, isExcludes,lang){
		qlist = frm.que_id_lst.value.split("~");
		retValue = 'true';
		var score_list = '';
		for(i=0;i<qlist.length;i++) {
			if(retValue =='true') {
				if(qlist[i]!='') {
					que = eval('frm.score_'+qlist[i])
					que_score = eval('frm.score_'+qlist[i]+'.value');
					if(score_list!='') {
						score_list += '~';
					} 
					score_list += que_score;
					max_score = eval('frm.max_'+qlist[i]+'.value')-0;
					if(que_score == '') {
						Dialog.alert(wb_msg_specify_score);
						retValue = false;
					} else if(gen_is_int(que_score)==false) {
						Dialog.alert(wb_msg_specify_integer);
						retValue = false;
					} else {
						que_score=que_score-0;
						if(que_score < 0) {
							Dialog.alert(wb_msg_specify_integer);
							retValue = false;
						} else if(que_score > max_score) {
							//Dialog.alert(wb_msg_score_cannot_larger_1 + max_score + wb_msg_score_cannot_larger_2);
							Dialog.alert(eval('wb_msg_' + lang + '_not_greater_max_score'));
							retValue = false;
						}
					}
				}
			} else {
				i = qlist.length + 1;
			}
		}
		if(retValue) {
			frm.que_score_lst.value = score_list			
			frm.url_failure.value =  wb_utils_invoke_servlet('cmd','get_submission_lst','course_id',frm.cos_id.value,'mod_id',frm.mod_id.value,'ass_queue','all','stylesheet','tst_sub_lst.xsl')	
			if(isExcludes == 'true' || isExcludes == true){
				frm.url_success.value =  wb_utils_invoke_servlet('cmd','get_submission_lst','course_id',frm.cos_id.value,'mod_id',frm.mod_id.value,'ass_queue','all','stylesheet','tst_sub_lst.xsl')
			} else {
				frm.url_success.value =  wb_utils_controller_base + "admin/module/test";
			}
			frm.action = wb_utils_servlet_url
			frm.method = 'post'
			frm.cmd.value = 'grade_all_que'
			frm.submit();
		} else {
			que.focus();
		}
	
}


function _wbModuleCheckAICCFile(frm,lang){
	if( _wbModuleCheckFileEmpty(frm.aicc_crs,'crs',lang) && _wbModuleCheckAICCExtension(frm.aicc_crs,'crs',lang)){
		frm.aicc_crs_filename.value = _wbModuleGetAICCFilename(frm.aicc_crs)
	}else{
		return false;
	}
	if(_wbModuleCheckFileEmpty(frm.aicc_cst,'cst',lang) && _wbModuleCheckAICCExtension(frm.aicc_cst,'cst',lang) ){
		frm.aicc_cst_filename.value = _wbModuleGetAICCFilename(frm.aicc_cst);
	}else{
		return false;
	}
	if(_wbModuleCheckFileEmpty(frm.aicc_des,'des',lang) && _wbModuleCheckAICCExtension(frm.aicc_des,'des',lang) ){
		frm.aicc_des_filename.value = _wbModuleGetAICCFilename(frm.aicc_des);
	}else{
		return false;
	}
	if(_wbModuleCheckFileEmpty(frm.aicc_au,'au',lang) && _wbModuleCheckAICCExtension(frm.aicc_au,'au',lang) ){
		frm.aicc_au_filename.value = _wbModuleGetAICCFilename(frm.aicc_au);
	}else{
		return false;
	}
	if(frm.aicc_ort.value != ''){
		if(frm.aicc_ort.value != '' && _wbModuleCheckAICCExtension(frm.aicc_ort,'ort',lang)){
			frm.aicc_ort_filename.value = _wbModuleGetAICCFilename(frm.aicc_ort);
		}else{
			return false;
		}
	}else{
		return true;
	}
	return true;
}

function _wbModuleCheckAICCExtension(obj,ext,lang){
	req_ext = '.' + ext
	req_ext_length = req_ext.length
	file_str = obj.value
	file_str_length = obj.value.length
	file_ext = file_str.substring((file_str_length - req_ext_length),file_str_length)
	file_ext = file_ext.toLowerCase()

	if( file_ext != req_ext){
		Dialog.alert(eval('wb_msg_' + lang + '_aicc_file_not_support') + req_ext)
		return false;
	}else{
	return true;
	}
}

function _wbModuleGetAICCFilename(obj){
	file_str = obj.value
	file_str_length = obj.value.length
	file_str_start = file_str.lastIndexOf('\\')
	filename = file_str.substring(++file_str_start,file_str_length)
	return filename;
}

function _wbModuleCheckFileEmpty(obj,ext,lang){
	if (obj.value == ''){
		Dialog.alert(eval('wb_msg_' + lang + '_aicc_file_not_input') + ' (' + ext + ')')
		obj.focus()
		return false;
	}else{
	return true;
	}
}

function _wbModuleValidateFrm(frm,attp,lang,src) {//src  作业修改时 未选择新的文件替换  则用原文件路径
	var _debug_msg = true;
	//mod_title
	if (frm.mod_title){
		frm.mod_title.value = wbUtilsTrimString(frm.mod_title.value);
		if (!gen_validate_empty_field(frm.mod_title, eval('wb_msg_' + lang + '_title'),lang)) {
			frm.mod_title.focus()
			return false;
		}
	}
	//mod_desc
	if (frm.mod_desc) {
		frm.mod_desc.value = wbUtilsTrimString(frm.mod_desc.value);
		if(frm.mod_desc.value.length > 1000 ) {
			Dialog.alert(eval('wb_msg_'+lang+'_desc_too_long'))
			frm.mod_desc.focus()
			return false
		}
	}
	//mod_vod_duration
	if (frm.mod_vod_duration){
		if(frm.mod_vod_duration.value.search(/^\d{1,9}$/) == -1){
			Dialog.alert(wb_msg_cata_order_enter_positive_integer);
			frm.mod_vod_duration.focus();
			return false;
		}
	}
	if(frm.mod_vod_img){
		var mediaFilename = frm.mod_vod_img.value.toLowerCase()
		if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
			Dialog.alert(wb_msg_img_type_limit);
			frm.mod_vod_img.focus();
			return false;
		}
	}
	
	// mod_required_time
	if (frm.mod_required_time) {
		frm.mod_required_time.value = wbUtilsTrimString(frm.mod_required_time.value);
		if (frm.mod_required_time.value && frm.mod_required_time.value.length != '') {
			var reg = /^(([1-9])|0|([1-9][0-9]{1,4}))$/;
			if (!reg.test(frm.mod_required_time.value)) {
				Dialog.alert(eval('wb_vod_' + lang + '_req_time_format'))
				frm.mod_required_time.focus()
				return false
			}
		}
	}

	//mod_instr
	if (frm.mod_instr){
		frm.mod_instr.value = wbUtilsTrimString(frm.mod_instr.value);
		if (frm.mod_instr.value.length > 2000){
			Dialog.alert(eval('wb_msg_'+lang+'_inst_too_long'))
			frm.mod_instr.focus()
			return false
		}
	}
	//ass_submission
	if (frm.ass_submission){
		frm.ass_submission.value = wbUtilsTrimString(frm.ass_submission.value);
		if (frm.ass_submission.value.length > 2000){
			Dialog.alert(eval('wb_msg_'+lang+'_ass_submission_too_long'))
			frm.ass_submission.focus()
			return false
		}
	}
	//max_usr_attempt_unlimited_num
	if (frm.max_usr_attempt_unlimited_num){
		frm.max_usr_attempt_unlimited_num.value = wbUtilsTrimString(frm.max_usr_attempt_unlimited_num.value);
		if ((frm.mod_subtype.value == 'TST' || frm.mod_subtype.value == 'DXT') && getRadioValueByName("max_usr_attempt_rad") == 1){
			if(!gen_validate_positive_integer(frm.max_usr_attempt_unlimited_num, eval('wb_msg_' + lang + '_max_usr_attempt'),lang)){
				frm.max_usr_attempt_unlimited_num.focus()
				return false;
			}
		}
	}
	
	//Normal Case (with Src)
	if (frm.mod_subtype.value == 'VOD' || frm.mod_subtype.value == 'LCT' || frm.mod_subtype.value == 'TUT' || frm.mod_subtype.value == 'RDG' || frm.mod_subtype.value == 'GAG'){
		if ( frm.mod_subtype.value == 'LCT' ||  frm.mod_subtype.value == 'TUT' || frm.mod_subtype.value == 'RDG'){
			if(frm.mod_subtype.value == 'RDG'){
				if(mod_check_src_file(frm,src)){
					Dialog.alert(fetchLabel("label_core_training_management_360"));
					return false;
				}
				if(mod_check_src_url(frm)){
					Dialog.alert(fetchLabel("label_core_training_management_361"));
					return false;
				}
			}
			if ( mod_check_src_zipfile(frm)|| mod_check_src_wizpack(frm) || mod_check_src_url(frm) || mod_check_src_pick_res(frm) || mod_check_src_file(frm,src) ) {
				Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'))
				return false
			}
		}else {
			if ( mod_check_src_zipfile(frm) ||mod_check_src_url(frm)  ||mod_check_src_pick_res(frm) || mod_check_src_file(frm,src)){
				Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'))

				return false
			}
			if(mod_check_online_video(frm)) {
				Dialog.alert(eval('wb_msg_src_not_valid'))
				return false
			}
		}
		if(frm.mod_subtype.value == 'VOD' || frm.mod_subtype.value == 'RDG'){
			if (frm.src_type[src_type_file_id].checked == true){
				if(frm.mod_file.value == ""){
					if ( frm.mod_src_type.value != 'FILE' ){
						if(src != ""){//第一次不是上传文件，却选择上传文件
							Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'));
							return false;
						}
					}
					file = frm.org_file.value;
				} else {
					file = frm.mod_file.value;
				}
				if(frm.mod_subtype.value == 'VOD'){
					file = file.substring(file.lastIndexOf('.')+1).toLowerCase();
					if(frm.cos_type && frm.cos_type.value == 'MOBILE'){
						if ( file != 'mp4'){
							Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_video_mp4'))
							return false	
						}
					}else{
						if ( file != 'mp4' && file != '3gp' && file != 'avi' && file != 'flv'&& file != 'mpeg' && file != 'wmv'){
							Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_video'))
							return false
						}
					}
				}
			}
		}
		if(frm.mod_subtype.value != 'VOD' && frm.mod_subtype.value != 'RDG'){
		if (frm.src_type[src_type_zipfile_id].checked == true &&  frm.org_zipfile.value == ""){
			file = frm.mod_zipfilename.value
			if ( file.substring(file.lastIndexOf('.')+1).toLowerCase() != 'zip' ){
				Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_zipfile'))
				return false
			}
		}
		if (!isNaN(src_type_wizpack_id)&& frm.src_type[src_type_wizpack_id].checked == true || frm.source_type.value == 'WIZPACK' ){
			if ( frm.mod_wizpack.value != '' ){
				file = frm.mod_wizpack.value
				if ( file.substring(file.length - 11).toLowerCase() != 'wizpack.zip' ){
					Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_wizpack'))
					return false
				}
			}
			template = frm.tpl_name[frm.tpl_name.selectedIndex].text
			if ( template.indexOf('wizPack') == -1){
				Dialog.alert(eval('wb_msg_'+lang+'_select_wizpack_template'))
				return false
			}
		}else if(frm.mod_subtype.value != 'GAG'){
			template = frm.tpl_name[frm.tpl_name.selectedIndex].text
			if ( template.indexOf('wizPack') != -1){
				Dialog.alert(eval('wb_msg_'+lang+'_select_another_template'))
				return false
			}
		}
		}
	}
	//AICC_AU
	if ( frm.mod_subtype.value == 'AICC_AU' ){

		if (!isNaN(src_type_pick_aicc_res_id) && frm.src_type[src_type_pick_aicc_res_id].checked == true && frm.copy_media_from.value == ''){
			Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'))
			return false
		}else{
			if (!isNaN(src_type_aicc_id) && frm.src_type[src_type_aicc_id].checked == true && !_wbCourseCheckAICCFile(frm,lang) )
				return false;
		}
		if(!isNaN(src_type_pick_aicc_res_id)){
		if (frm.src_type[src_type_pick_aicc_res_id].checked == true){
					frm.aicc_crs.value = ''
					frm.aicc_cst.value = ''
					frm.aicc_des.value = ''
					frm.aicc_au.value = ''
					frm.aicc_ort.value = ''
					frm.aicc_crs_filename.value = ''
					frm.aicc_cst_filename.value = ''
					frm.aicc_des_filename.value = ''
					frm.aicc_au_filename.value = ''
					frm.aicc_ort_filename.value = ''
		}
		}
	}
	
	
	if((frm.mod_subtype.value == 'AICC_AU' || frm.mod_subtype.value == 'SCO') && frm.mod_max_score && frm.mod_pass_score){
		frm.mod_max_score.value = wbUtilsTrimString(frm.mod_max_score.value);
		frm.mod_pass_score.value = wbUtilsTrimString(frm.mod_pass_score.value);
		if(frm.mod_pass_score.value =='' && frm.mod_max_score.value ==''){
			
		}else{
	
			if(!gen_validate_float1000(frm.mod_max_score, eval('wb_msg_' + lang + '_max_score'), lang)){
				frm.gen_validate_float_less_than_100.focus();
				return false;
			}		
			frm.mod_pass_score.value = wbUtilsTrimString(frm.mod_pass_score.value);	
			if(!gen_validate_float1000(frm.mod_pass_score, eval('wb_msg_' + lang + '_passing_score'), lang)){
				frm.mod_pass_score.focus()
				return false;
			}
			
			if(parseFloat(frm.mod_max_score.value) < parseFloat(frm.mod_pass_score.value)){
					Dialog.alert(eval('wb_msg_'+lang+'_max_pass_score'));
					frm.mod_max_score.focus();
					return false;
			}	
		}
			
	}

	if(frm.mod_subtype.value == 'TST' || frm.mod_subtype.value == 'DXT'|| frm.mod_subtype.value == 'EXC' || frm.mod_subtype.value == 'LCT' || frm.mod_subtype.value == 'TUT' || frm.mod_subtype.value == 'RDG' || frm.mod_subtype.value == 'GAG'){
		if(frm.mod_subtype.value == 'TST' || frm.mod_subtype.value == 'DXT'){
			lab_name = eval('wb_msg_' + lang + '_time_limit')
		}else if ( frm.mod_subtype.value == 'LCT'){
			lab_name = eval('wb_msg_' + lang + '_duration')
		}else{
			lab_name = eval('wb_msg_' + lang + '_suggested_time')
		}
		if (frm.mod_duration && !gen_validate_float(frm.mod_duration, lab_name,lang)){
				frm.mod_duration.focus()
				return false
		}
	}
	//ASS
	if(frm.mod_subtype.value == 'ASS'){
		if ((frm.src_type[src_type_zipfile_id].checked == true && frm.mod_zipfilename.value != '' && frm.mod_default_page.value == '' )  ||( frm.org_zipfile.value == '' && frm.src_type[src_type_zipfile_id].checked == true && (frm.mod_default_page.value == '' || frm.mod_zipfilename.value == '')) ||mod_check_src_file(frm)  || mod_check_src_url(frm)  || ( frm.mod_instr.value == '' && frm.src_type[src_type_ass_inst_id].checked == true)){
			var flg=false;
			if ( frm.src_type[src_type_file_id].checked == true ){
				//var a=$(frm.src_type[src_type_file_id]).next().next();
				var fileSrc=$(frm.src_type[src_type_file_id]).next().next().attr("href");
				if(fileSrc!=undefined){
					frm.org_file.value=src;
					flg=true;
				}
				//console.log(a);
			}
			/*else if(frm.src_type[src_type_zipfile_id].checked == true){
				
				var fileSrc=$(frm.src_type[src_type_file_id]).next().next().attr("href");
				console.log(fileSrc);
				if(fileSrc!=undefined){
					frm.org_zipfile.value=src;
					flg=true;
				}
			}
			*/
			if(flg==false){
				if (frm.src_type[src_type_ass_inst_id].checked == true){
					Dialog.alert(fetchLabel("label_core_training_management_373"));
					frm.mod_instr.focus();
				}else if ( frm.src_type[src_type_url_id].checked == true ){
					Dialog.alert(fetchLabel("label_core_training_management_374"));
					frm.mod_url.focus();
				}else if ( frm.src_type[src_type_file_id].checked == true ){
					Dialog.alert(fetchLabel("label_core_training_management_375"));
					frm.mod_file.focus();
				}else if ( frm.src_type[src_type_zipfile_id].checked == true ){
					Dialog.alert(fetchLabel("label_core_training_management_371"))
					frm.mod_zipfilename.focus();
				}
					
				return false
				
			}
		}
		if(frm.mod_instr.value.length > 3000){
			Dialog.alert(eval('wb_msg_' + lang + '_inst_too_long'))
			return false
		}
		if ( frm.src_type[src_type_zipfile_id].checked == true  && frm.mod_zipfilename.value != '' &&  frm.org_zipfile.value == ""){
			file = frm.mod_zipfilename.value
			if ( file.substring(file.lastIndexOf('.')+1).toLowerCase() != 'zip' ){
				Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_zipfile'))

				return false
			}
		}
		if (frm.ass_due_datetime && frm.ass_due_date_day){
			if(frm.ass_due_date_rad[1].checked == true){
				if (!wbUtilsValidateDate('document.frmXml.due',eval('wb_msg_' + lang + '_ass_due_datetime'),'','ymdhm')){
					return false
				}

			}else if(frm.ass_due_date_rad[2].checked == true){
				if(!gen_validate_positive_integer(frm.ass_due_date_day, eval('wb_msg_' + lang + '_ass_due_datetime'),lang)){
					frm.ass_due_date_day.focus()
					return false;
				}
				if(frm.ass_due_date_day.value <= '0'){
					Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + eval('wb_msg_' + lang + '_ass_due_datetime') + '"' + wb_msg_pls_enter_positive_integer_2);

					frm.ass_due_date_day.focus()
					return false;
				}
			}
		}
	}

	if ( frm.mod_subtype.value != 'ASS' && frm.mod_subtype.value != 'EAS' ){
		if (frm.mod_subtype.value != 'CHT' && frm.mod_subtype.value != 'VCR' && frm.mod_subtype.value != 'VOD' && frm.mod_subtype.value != 'FOR' && frm.mod_subtype.value != 'FAQ' && frm.mod_subtype.value != 'STX' && frm.mod_subtype.value != 'AICC_AU' && frm.mod_subtype.value != 'REF' && frm.mod_subtype.value != 'GLO' && frm.mod_subtype.value != 'SVY' && frm.mod_subtype.value != 'EVN' && frm.mod_subtype.value != 'NETG_COK'){
			if ( frm.mod_subtype.value == 'TST' || frm.mod_subtype.value == 'DXT' )
				lab_name = eval('wb_msg_' + lang + '_time_limit')
			else if ( frm.mod_subtype.value == 'LCT' || frm.mod_subtype.value == 'VDO' || frm.mod_subtype.value == 'ADO')
				lab_name = eval('wb_msg_' + lang + '_duration')
			else
				lab_name = eval('wb_msg_' + lang + '_suggested_time')

			if (frm.mod_duration) {
				frm.mod_duration.value = wbUtilsTrimString(frm.mod_duration.value);
				if (!gen_validate_float(frm.mod_duration, lab_name, lang)) {
					frm.mod_duration.focus()
					return false
				}
			}
		}
	}


	if ( frm.mod_subtype.value == 'VCR' ){
		if (frm.mod_src_link) {
			frm.mod_src_link.value = wbUtilsTrimString(frm.mod_src_link.value);
			if (!gen_validate_empty_field(frm.mod_src_link, eval('wb_msg_' + lang + '_url'), lang)) {
				frm.mod_src_link.focus()
				return false;
			}
		}
	}

	if ( frm.mod_subtype.value == 'VST'  || frm.mod_subtype.value == 'GAG' || frm.mod_subtype.value == 'EXM'  || frm.mod_subtype.value == 'ORI' ){
		if (!wbUtilsValidateDate('document.frmXml.evt',eval('wb_msg_' + lang + '_evt_datetime'),'','ymdhm'))
			return false
		if (frm.evt_venue) {	
			frm.evt_venue.value = wbUtilsTrimString(frm.evt_venue.value);
			if(frm.evt_venue.value.length > 150 ) {
				Dialog.alert(eval('wb_msg_'+lang+'_evt_venue_too_long'))
				frm.evt_venue.focus()
				return false
			}
		}
	}

	if ( frm.mod_subtype.value == 'TST'  || frm.mod_subtype.value == 'DXT' || frm.mod_subtype.value == 'STX'){
		if (frm.mod_pass_score) {
			frm.mod_pass_score.value = wbUtilsTrimString(frm.mod_pass_score.value);
			if (!gen_validate_pencentage(frm.mod_pass_score, eval('wb_msg_' + lang + '_pass_score'),lang)){
				frm.mod_pass_score.focus()
				return false
			}
		}
	}

	if ( frm.mod_subtype.value == 'VCR' || frm.mod_subtype.value == 'FAQ' || frm.mod_subtype.value == 'FOR' || frm.mod_subtype.value == 'CHT' || frm.mod_subtype.value == 'ASS' || frm.mod_subtype.value == 'EAS'){
			if (frm.mod_instructor) {
				frm.mod_instructor.value = wbUtilsTrimString(frm.mod_instructor.value);
				if (!gen_validate_empty_field(frm.mod_instructor, eval('wb_msg_' + lang + '_instructor'), lang)) {
					frm.mod_instructor.focus()
					return false;
				}
			}
		}
	if (frm.start_date && frm.end_date){
		if (frm.start_date[1].checked == true){
			if (!wbUtilsValidateDate('document.frmXml.start',eval('wb_msg_' + lang + '_start_datetime'),'','ymdhm'))
				return false
		}

		if (frm.end_date[1].checked == true){
			if (!wbUtilsValidateDate('document.frmXml.end',eval('wb_msg_' + lang + '_end_datetime'),'','ymdhm'))
				return false
		}

		//Date Validation
		if (frm.start_date[1].checked == true && frm.end_date[1].checked == true){
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'start', 
				end_obj : 'end', 
				start_nm : frm.lab_start_date.value, 
				end_nm : frm.lab_end_date.value
				})) {
				return false;	
			}
		}

		if (frm.start_date[0].checked == true && frm.end_date[1].checked == true){
			if(!wb_utils_validate_date_compare({
				frm : 'document.' + frm.name, 
				start_obj : 'cur_dt', 
				end_obj : 'end', 
				start_nm : frm.lab_start_date.value, 
				end_nm : frm.lab_end_date.value, 
				focus_obj : 'end'
				})) {
				return false;	
			}
		}
	}
	if(frm.mod_annotation){
		frm.mod_annotation.value = wbUtilsTrimString(frm.mod_annotation.value);
		if(frm.mod_annotation.value.length > 1000){
			Dialog.alert(eval('wb_msg_' + lang + '_annonation_too_long'));
			return false;
		}
	}	
	return true;
}



/* public functions */
//v3
function mod_check_src_pick_res(frm){
	if (frm.copy_media_from) {
		frm.copy_media_from.value = wbUtilsTrimString(frm.copy_media_from.value);
	}
	if(!isNaN(src_type_pick_res_id) && frm.src_type[src_type_pick_res_id].checked == true && frm.copy_media_from.value == ''){
		return true;
	}else{
		return false;
	}
}
function mod_check_src_file(frm,src){
//	if (frm.mod_file) {
//		frm.mod_file.value = wbUtilsTrimString(frm.mod_file.value);
//	}
	
	if (frm.org_file) {
		frm.org_file.value = wbUtilsTrimString(frm.org_file.value);
	}

	if(!isNaN(src_type_file_id) && frm.src_type[src_type_file_id].checked == true && frm.mod_file.value == '' && frm.org_file.value == ''){
		if(src!='' && typeof(src)!="undefined" && src!=null){
			frm.org_file.value = src;
			return false;
		}
		return true
	}else{
		return false
	}
}

function mod_check_src_zipfile(frm){
	if (frm.mod_zipfilename) {
		frm.mod_zipfilename.value = wbUtilsTrimString(frm.mod_zipfilename.value);
	}
	if (frm.mod_default_page) {
		frm.mod_default_page.value = wbUtilsTrimString(frm.mod_default_page.value);
	}
	if(!isNaN(src_type_zipfile_id) &&  frm.org_zipfile.value == '' 
		&& frm.src_type[src_type_zipfile_id].checked == true 
		&& ( frm.mod_default_page.value == '' || frm.mod_zipfilename.value == '')){
		return true;
	}else{
		return false;
	}
}

function mod_check_src_url(frm){
	 //正则表达式 格式：http://host:port/.../xxx.mp4;
	 //var reg = /^http:\/\/(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\.(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\.(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\.(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])\/[.*\/]*.*\.mp4$/;
	var reg = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?\.mp4$/;  
	if (frm.mod_url) {
	 	frm.mod_url.value = wbUtilsTrimString(frm.mod_url.value);
	 }
	 if(!isNaN(src_type_url_id) && frm.src_type[src_type_url_id].checked == true && (frm.mod_url.value == '' || 
			 !reg.test(frm.mod_url.value))){
			 //frm.mod_url.value == "http://")){
	 	return true;
	 }else{
	 	return false;
	 }
}

function customize_online_video(req_url) {
	  var url = req_url;
	  var ctrl = "";
	  if (url.search(/^https?\:\/\/([^\.]+\.)?youtu\.be/i) > -1 ||
			  url.search(/^https?:\/\/([^\.]+\.)?youtube\.com/i) > -1){	/* Youtube */
	    ctrl = "showinfo=0&rel=0";
	  } else if (url.search(/^https?:\/\/([^\.]+\.)?vimeo\.com/i) > -1){	/* vimeo */
	    ctrl = "title=0&byline=0&portrait=0";
	  } else if (url.search(/^https?\:\/\/([^\.]+\.)?dailymotion\.com/i) > -1 ||
			  url.search(/^https?:\/\/([^\.]+\.)?dai\.ly/i) > -1){	/* dailymotion */
	    ctrl = "api=0&autoplay=0&subtitles-default=0&info=0&logo=0&social=0&related=0";
	  } else {
	  }
	  
	  if (ctrl != "") {
	    url = (url.indexOf("?") > -1) ? url.replace("?", "?" + ctrl + "&") : url + "?" + ctrl;
	  }
	  return url;
}

function mod_check_online_video(frm){
	if(frm.src_type[src_type_video_id].checked == true) {
		var video_type = frm.video_type.options[frm.video_type.selectedIndex].value;
		var url = frm.mod_video.value;
		var success = false;
		if(url != '' && url != undefined) {
			if(video_type == 1) {
				frm.mod_src_online_link.value = 'http://www.youtube.com/oembed?url=' + url;
			} else if(video_type == 2) {
				frm.mod_src_online_link.value = 'https://vimeo.com/api/oembed.json?url=' + url;
			} else if(video_type == 3) {
				frm.mod_src_online_link.value = 'https://www.dailymotion.com/services/oembed?url=' + url;
			} /*else if(video_type == 4) {
				frm.mod_src_online_link.value = 'https://mix.office.com/oembed?url=' + url;
			}*/
			$.ajax({
				url : '/app/admin/course/parseOnlineAPIJson',
				type : 'POST',
				data : {url:frm.mod_src_online_link.value},
				dataType : "JSON",
				async : false,
				success : function(data) {
					success = data.success;
					if(data.success) {
						frm.mod_src_online_link.value = customize_online_video(data.link);
					}
				}
			});
			if(success) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
}

function mod_check_src_wizpack(frm){
		if (frm.mod_wizpack) {
			frm.mod_wizpack.value = wbUtilsTrimString(frm.mod_wizpack.value);
		}
		if(src_type_wizpack_id && frm.src_type[src_type_wizpack_id].checked == true && frm.mod_wizpack.value == ''){
			return true;
		}else{
			return false;
		}
}

function _wbModuleGetResNumber(frm) {
		var i, n, ele, str
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.checked) {
				if ( ele.value != "")
					str = str + ele.value + "~"
			}
		}

		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		return str;
}


function _wbModuleValidateSTXFrm(frm,lang){

	if ( frm.msp_duration.value.length != 0 ) {
		if (!gen_validate_integer(frm.msp_duration, eval('wb_msg_'+lang+'_dur_que'),lang)){
			frm.msp_duration.focus()
			return false;
		}
	}

	if ( frm.msp_score.value.length != 0 ) {
	if (!gen_validate_integer(frm.msp_score, eval('wb_msg_'+lang+'_score_que'),lang)){
		frm.msp_score.focus()
		return false;
	}
	}

	if (!gen_validate_empty_field(frm.msp_qcount, eval('wb_msg_'+lang+'_ttl_que'),lang)){
		frm.msp_qcount.focus()
		return false;
	}else{
		if (!gen_validate_integer(frm.msp_qcount, eval('wb_msg_'+lang+'_ttl_que'),lang)){
			frm.msp_qcount.focus()
			return false;
		}
	}


	if (!gen_validate_integer(frm.time_limit, eval('wb_msg_'+lang+'_time_limit'),lang)){
		frm.time_limit.focus()
		return false;
	}


	return true;
}


function _wbModuleGetTime(datetime){

		if ( datetime.getMonth() > 9 )
			month = datetime.getMonth() + 1
		else
			month = '0' + (datetime.getMonth() + 1 )

		if ( datetime.getDate() > 9 )
			day = datetime.getDate()
		else
			day = '0' + datetime.getDate()

		if ( datetime.getHours() > 9 )
			hour = datetime.getHours()
		else
			hour = '0' + (datetime.getHours() + 1 )

		if ( datetime.getMinutes() > 9 )
			minutes = datetime.getMinutes()
		else
			minutes = '0' + datetime.getMinutes()

		if ( datetime.getSeconds() > 9 )
			seconds = datetime.getSeconds()
		else
			seconds = '0' + datetime.getSeconds()

		time = datetime.getFullYear() + month + day + hour  + minutes  + seconds

		return time;

}



/* public functions */

function wbModuleGetURL(course_id,mod_id,stylesheet,view) {
	return wb_utils_invoke_servlet('cmd','get_mod','course_id',course_id,'mod_id',mod_id,'stylesheet',stylesheet,'dpo_view',view)
}

function wbModuleUpdTestPrep(id,course_id,usr_id, status, attempted, mod_type,lang){
	var confirm_msg = eval('wb_msg_' + lang + '_confirm_change_offline')
	if ( status == 'ON' && confirm(confirm_msg) ){
		//Set Current Module to Offline
		var submit_url = wb_utils_invoke_servlet('cmd','upd_res_sts','res_status','OFF','res_id_lst',id,'url_success',window.location.href)
		window.location.href = submit_url;				
	}else if(status == 'ON'){
		return;
	}
	//Man :change command from "get_prof" to "get_mod"
	var url = wb_utils_invoke_servlet('cmd','get_mod','course_id',course_id,'mod_id',id,'dpo_view','IST_READ','stylesheet','tst_info_frame.xsl','usr_id',usr_id)
	var str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '980'
			+ ',height=' 				+ '680'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
	var tst_builder_win = wbUtilsOpenWin(url, 'tst_builder_win', false, str_feature);
	return;
}


function wbModuleUpdSvyPrep(id,course_id,usr_id, status, attempted, mod_type,lang){
	var confirm_msg = eval('wb_msg_' + lang + '_confirm_change_inactive')
	if ( status == 'ON' && confirm(confirm_msg) ){
		//Set Current Module to Offline
		var submit_url = wb_utils_invoke_servlet('cmd','upd_res_sts','res_status','OFF','res_id_lst',id,'url_success',window.location.href,'url_failure',window.location.href)
		window.location.href = submit_url;				
	}else if(status == 'ON'){
		return;
	}
	
	//Kim :change command from "get_prof" to "get_mod"
	var url = wb_utils_invoke_servlet('cmd','get_mod','course_id',course_id,'mod_id',id,'dpo_view','IST_READ','attempted',attempted, 'stylesheet','cos_evn_info_frame.xsl','usr_id',usr_id)
	var str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '982'
			+ ',height=' 				+ '772'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
	var tst_builder_win = wbUtilsOpenWin(url, 'tst_builder_win', false, str_feature);
	return;
}



function wbModuleUpdPrep(id,course_id,mod_type,lang,isEnrollment_related){
		wb_utils_set_cookie('url_prev',window.location.href);
		var xslName;
		if (isEnrollment_related == 'true') {
			xslName = 'tst_upd2.xsl';
		} else {
			xslName = 'tst_upd.xsl';
		}
		url = wb_utils_invoke_servlet('cmd','get_mod','course_id',course_id,'mod_id',id,'stylesheet',xslName,'dpo_view','IST_EDIT')
		window.location.href = url;
}

function wbModuleUpdPrepURL(id,course_id){
		url = wb_utils_invoke_servlet('cmd','get_mod','course_id',course_id,'mod_id',id,'stylesheet','tst_upd.xsl','dpo_view','IST_EDIT')
		return url;
}


function wbModuleUpdExec(frm,attp,lang,src){ 
	if (_wbModuleValidateFrm(frm,attp,lang,src)) {
		
		// get the course structure XML from the course authoring applet and at the same time lock the applet's button
		if (parent.NavPage != null ) {
			parent.NavPage.getCosStructureXML();
			//parent.NavPage.document.CourseAuthoring.getCosStructureXML();
		}
		//
			if (frm.mod_subtype.value == 'VOD' || frm.mod_subtype.value == 'LCT' || frm.mod_subtype.value == 'TUT' 
				|| frm.mod_subtype.value == 'RDG' || frm.mod_subtype.value == 'GAG' || frm.mod_subtype.value == 'MBL'){

				if ( frm.src_type[src_type_file_id].checked == true ){
					if(frm.mod_file.value == ""){
					if ( frm.mod_src_type.value != 'FILE' ){
						if(src != ""){//第一次不是上传文件，却选择上传文件
							Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'));
							return false;
						}
					}
						frm.mod_src_link.value = frm.org_file.value
					}else{
						frm.mod_src_link.value = wbMediaGetFileName(frm.mod_file.value)
					}
					frm.mod_src_type.value = 'FILE'
					frm.copy_media_from.value = ''
				}else if ( frm.src_type[src_type_url_id].checked == true ){
					frm.mod_src_link.value = frm.mod_url.value
					frm.mod_src_type.value = 'URL'
					frm.copy_media_from.value = ''
				}else if (!isNaN(src_type_pick_res_id) && frm.src_type[src_type_pick_res_id].checked == true){
					frm.mod_src_link.value = frm.source_content.value
					frm.mod_src_type.value = frm.source_type.value

				}else if (!isNaN(src_type_zipfile_id) && frm.src_type[src_type_zipfile_id].checked == true){
					if ( frm.mod_default_page.value == '' )
						frm.mod_src_link.value = frm.org_zipfile.value
					else
						frm.mod_src_link.value = frm.mod_default_page.value
					frm.zip_filename.value = wbMediaGetFileName(frm.mod_zipfilename.value)
					frm.mod_src_type.value = 'ZIPFILE'
					frm.copy_media_from.value = ''

				}else if ( frm.src_type[src_type_video_id].checked == true ){
					frm.mod_src_link.value = frm.mod_video.value
					frm.mod_src_type.value = 'ONLINEVIDEO_' + frm.video_type.options[frm.video_type.selectedIndex].value;
					frm.copy_media_from.value = ''
				}else if ( frm.mod_subtype.value == 'LCT' ||frm.mod_subtype.value == 'TUT' ||frm.mod_subtype.value == 'RDG' ||frm.mod_subtype.value == 'EXP'){
					if ( frm.src_type[src_type_wizpack_id].checked == true ){
						if ( frm.mod_wizpack.value == '' )
							frm.mod_src_link.value = frm.org_wizpack.value
						else
							frm.mod_src_link.value = wbMediaGetFileName(frm.mod_wizpack.value)
						frm.mod_src_type.value = 'WIZPACK'
						frm.copy_media_from.value = ''
					}
				}


				if (frm.mod_subtype.value != 'VOD' && frm.mod_subtype.value != 'VST' && frm.mod_subtype.value != 'GAG' 
					&& frm.mod_subtype.value != 'ORI' && frm.mod_subtype.value != 'EXM' && frm.mod_subtype.value != 'MBL'
					&& frm.mod_subtype.value != 'RDG'){
					if (frm.asHTML.checked)
						frm.annotation_html.value = "Y"
					else
						frm.annotation_html.value = "N"
				}
			}
		if(frm.mod_subtype.value == 'VOD' && frm.mod_vod_img && frm.mod_vod_img_link){
			if (frm.mod_vod_img.value == '' ){
				frm.mod_vod_img_link.value = frm.org_file_img.value;
			}else{
				frm.mod_vod_img_link.value = wbMediaGetFileName(frm.mod_vod_img.value);
			}
		}
		//
		if ( frm.mod_subtype.value == 'ASS'){
			if (!isNaN(src_type_file_id) &&  frm.src_type[src_type_file_id].checked == true ){
				if ( frm.mod_file.value == '' ){
					frm.mod_src_link.value = frm.org_file.value
				}
				else
					frm.mod_src_link.value = wbMediaGetFileName(frm.mod_file.value)
				frm.mod_src_type.value = 'FILE'
				frm.mod_instr.value = ''
			}else if ( frm.src_type[src_type_zipfile_id].checked == true ){
				if ( frm.mod_default_page.value == '' )
					frm.mod_src_link.value = frm.org_zipfile.value
				else
					frm.mod_src_link.value = frm.mod_default_page.value
				frm.zip_filename.value = wbMediaGetFileName(frm.mod_zipfilename.value)
				frm.mod_src_type.value = 'ZIPFILE'
				frm.mod_instr.value = ''
			}else if ( frm.src_type[src_type_url_id].checked == true ){
				frm.mod_src_link.value = frm.mod_url.value
				frm.mod_src_type.value = 'URL'
				frm.mod_instr.value = ''
			}else if ( frm.src_type[src_type_ass_inst_id].checked == true ){
				frm.mod_src_link.value = ''
			}

			if (frm.ass_due_datetime && frm.ass_due_date_day){
				frm.ass_due_datetime.value = '';
				if (frm.ass_due_date_rad[0].checked) {
					frm.ass_due_date_day.value = '0';
				}
				else
				if (frm.ass_due_date_rad[1].checked) {
					frm.ass_due_datetime.value = frm.due_yy.value + "-" + frm.due_mm.value + "-" + frm.due_dd.value + " " + frm.due_hour.value + ":" + frm.due_min.value + ":00";
					frm.ass_due_date_day.value = '0';
				}
			}
		}

		//
		if ( frm.mod_subtype.value == 'VST' || frm.mod_subtype.value == 'GAG' || frm.mod_subtype.value == 'EXM' || frm.mod_subtype.value == 'ORI')
			frm.evt_datetime.value = frm.evt_yy.value + "-" + frm.evt_mm.value + "-" +frm.evt_dd.value + " " + frm.evt_hour.value + ":" +frm.evt_min.value + ":00"

		//
		if ( frm.mod_subtype.value != 'EAS'){
			if (!frm.start_date){
				frm.mod_eff_start_datetime.value = "IMMEDIATE";
			}else{
				if ((Number(frm.cur_dt.value) < Number(frm.eff_dt.value)) || (frm.mod_subtype.value != 'CHT' && frm.mod_subtype.value !='VCR')){
					if ( frm.start_date[0].checked == true)
						frm.mod_eff_start_datetime.value = "IMMEDIATE"
					else
						frm.mod_eff_start_datetime.value = frm.start_yy.value + "-" + frm.start_mm.value + "-" +frm.start_dd.value + " " + frm.start_hour.value + ":" +frm.start_min.value + ":00"
				}
			}
		}

		if ( frm.mod_subtype.value == 'AICC_AU'){
			frm.src_type.value = 'AICC_FILES'
			if (!isNaN(src_type_pick_aicc_res_id) && frm.src_type[src_type_pick_aicc_res_id].checked == true){
				frm.mod_src_link.value = frm.source_content.value
			}
		}

		//
		if ( frm.mod_subtype.value == 'DXT' && frm.mod_logic){
			frm.mod_logic.value = 'RND';
		}

		//
		if ( frm.mod_subtype.value != 'EAS'){
			if (!frm.end_date){
				frm.mod_eff_start_datetime.value = "IMMEDIATE";
			}else{
				if ( frm.end_date[0].checked == true){
					frm.mod_eff_end_datetime.value = "UNLIMITED"}
				else{
					frm.mod_eff_end_datetime.value = frm.end_yy.value + "-" + frm.end_mm.value + "-" +frm.end_dd.value + " " + frm.end_hour.value + ":" +frm.end_min.value + ":00"
				}
			}
		}
		else
			frm.mod_eff_end_datetime.value = "UNLIMITED"
		frm.url_success.value = this.view_info_url(frm.mod_id.value)


		// for cos_struct_xml
		frm.course_timestamp.value = parent.course_timestamp;
		frm.course_struct_xml_cnt.value = parent.course_struct_xml_cnt;
		frm.course_struct_xml_1.value = parent.course_struct_xml_1;


		if(frm.mod_instructor && frm.rdo_ist && frm.rdo_ist[0].checked){
			frm.mod_instructor_ent_id_lst.value = mod_instructor_ent_id[frm.mod_instructor.selectedIndex];
		}

		if(frm.mod_subtype.value == 'SVY'){
			frm.max_usr_attempt.value = 1;
		}

		if (frm.mod_subtype.value=='TST' || frm.mod_subtype.value=='DXT'){
			if (frm.max_usr_attempt_unlimited_num.value != ''){
				frm.mod_max_usr_attempt.value = frm.max_usr_attempt_unlimited_num.value;
			}else{
				frm.mod_max_usr_attempt.value = '0';
			}
			if (frm.mod_show_answer_ind) {
				if(frm.mod_show_answer_ind_chk[0].checked) {
					frm.mod_show_answer_ind.value = '1';
					frm.mod_show_answer_after_passed_ind.value = '0';
				} else if(frm.mod_show_answer_ind_chk[1].checked) {
					frm.mod_show_answer_ind.value = '0';
					frm.mod_show_answer_after_passed_ind.value = '0';
				}else {
						frm.mod_show_answer_ind.value='0'
						if(frm.mod_show_answer_ind_chk[2].checked) {
							frm.mod_show_answer_after_passed_ind.value = '1';
						 } else {
							frm.mod_show_answer_after_passed_ind.value = '0';
						 }
				}
			}
			if (frm.mod_sub_after_passed_ind) {
				if(frm.mod_sub_after_passed_chk.checked) {
					frm.mod_sub_after_passed_ind.value = '0';
				} else {
					frm.mod_sub_after_passed_ind.value = '1';
				}
			}
			if (frm.mod_show_save_and_suspend_ind) {
				if(frm.mod_show_save_and_suspend_ind_chk.checked) {
					frm.mod_show_save_and_suspend_ind.value = '1';
				} else {
					frm.mod_show_save_and_suspend_ind.value = '0';
				}
			}
			if (frm.mod_managed_ind) {
				if(frm.mod_managed_ind_chk.checked) {
					frm.mod_managed_ind.value = '1';
				} else {
					frm.mod_managed_ind.value = '0';
				}
			}
		}

		frm.method = "post"

		frm.action = wb_utils_servlet_url + '?cmd=upd_mod';
		if(frm.mod_src_type && (frm.mod_src_type.value=="AICC_FILES" || frm.mod_src_type.value=="WIZPACK" || frm.mod_src_type.value=="FILE" || frm.mod_src_type.value=="ZIPFILE")){
			wb_utils_preloading();
		}

		frm.submit()
	}
}

function wbModuleUserReport(usr_id,mod_id,tkh_id, mod_type, not_popup){
		if (tkh_id == null) {
			tkh_id = '';
		}
		//to optimize test module for learner 
		var cmd = "get_rpt_usr";
		var stylesheet = 'lrn_tst_rpt_usr_header.xsl'
		if(mod_type == 'TST' || mod_type == 'DXT'){ 
            cmd = 'get_rpt_usr_test_html';
        }
		url = wb_utils_invoke_servlet('cmd',cmd,'stylesheet',stylesheet,'que_id_lst',0,'rpt_usr_id',usr_id,'mod_id',mod_id, 'tkh_id', tkh_id, 'url_success',self.location.href)
		//url = wb_utils_invoke_servlet('cmd','get_rpt_usr','stylesheet','lrn_tst_rpt_usr_header.xsl','que_id_lst',0,'rpt_usr_id',usr_id,'mod_id',mod_id, 'tkh_id', tkh_id, 'url_success',self.location.href)
		str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '900'
			+ ',height=' 				+ '500'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',screenX=' 				+ '100' 
			+ ',screenY='				+ '100';
			
		if (not_popup) {
			self.location.href = url;
		} else {
			wbUtilsOpenWin(url, 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id , false, str_feature);
		}
}
function wbVodResMainInfo(vod_res_id){
	var url = wb_utils_invoke_servlet('cmd','vod_res_main_info','vod_res_id',vod_res_id,'stylesheet','vod_res_main_info.xsl','url_failure', '../htm/close_window.htm')
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '550'
			+ ',height=' 				+ '500'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

		window.open(url, 'vod_res_main_info', str_feature);
}
function wbModulePreviewExec(mod_type,mod_id,tpl_use,cos_id,not_popup){

		if (mod_type == 'TST' || mod_type == 'DXT' || mod_type == 'EXC' || mod_type == 'STX' || mod_type == 'VOD' || mod_type == 'VCR' || mod_type == 'FOR' || mod_type == 'CHT' || mod_type == 'SVY' || mod_type == 'FAQ' || mod_type == 'GLO' || mod_type == 'EVN') {isFullScreen = false;}
		else {isFullScreen = true;}

		var cmd = ''
		var str_feature = _wbModuleGetContentWindowSize(mod_type,isFullScreen)

		if (mod_type == 'EXC' || mod_type == 'TST' || mod_type == 'DXT' || mod_type == 'SVY'  || mod_type == 'EVN' ) {
			cmd = 'get_tst'
		} else {
			cmd = 'get_mod'
		}


		wb_utils_set_cookie('isWizpack','false')

		if ( mod_type == 'STX' ){
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,'stylesheet', 'stx_wizard.xsl',
				'mod_id',mod_id,
				'url_failure','../htm/close_window.htm'
			)
		}else if ( mod_type == 'FOR'){
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,
				'stylesheet', tpl_use,
				'mod_id',mod_id,
				'cos_id',cos_id,
				'url_failure','../htm/close_window.htm',
				'cur_page','1',
				'sort_col','fto_title',
				'sort_order','asc',
				'timestamp',''
			)
		}else if (mod_type == 'REF') {
			url_success = wb_utils_invoke_disp_servlet(
				'cmd','get_reference_list',
				'module','content.ReferenceModule',
				'mod_id',mod_id,
				'stylesheet',tpl_use
			)
		}else if(mod_type == 'GLO') {
			url_success = wb_utils_invoke_disp_servlet(
				'cmd','get_keys',
				'module','content.GlossaryModule',
				'glo_res_id',mod_id,
				'stylesheet','glo_preview.xsl'
			)
		}else if(mod_type == 'TST' || mod_type == 'DXT' || mod_type == 'SVY'){
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,'stylesheet',
				tpl_use,'mod_id',mod_id,
				'cos_id',cos_id,
				'res_preview_ind','1',
				'url_failure','../htm/close_window.htm',
				'url_success','../htm/close_window.htm',
				'page','0'
			)
		}else{
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,'stylesheet',
				tpl_use,'mod_id',mod_id,
				'cos_id',cos_id,
				'url_failure','../htm/close_window.htm',
				'page','0'
			);
		}
		if (mod_type = 'EVN' && mod_type != 'RDG' && mod_type != 'ASS') {
			url_success += "&res_preview_ind=1";
		}
		if (not_popup) {
			self.location.href = url_success;
		} else {
			test_player = wbUtilsOpenWin(url_success, 'test_player' + mod_id, false, str_feature);
			test_player.focus();
		}
}

function wbModuleViewInformationURL(mod_id){
	url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'dpo_view','IST_READ','stylesheet','ist_view_module.xsl')
	return url;
}

function wbModulePreviewContentURL(mod_type,mod_id,tpl_use,cos_id){
		var url = wb_utils_invoke_servlet(
			'cmd','get_mod',
			'stylesheet',tpl_use,
			'mod_id',mod_id,
			'cos_id',cos_id,
			'url_failure','javascript:window.close()',
			'page','0'
		)
		return url;
}

function wbModulePreviewRef(mod_type,mod_id,tpl_use,cos_id,not_popup){
	str_feature = 	  'toolbar='				+ 'no'
					+ ',width=' 				+ '780'
					+ ',height=' 				+ '420'
					+ ',scrollbars='			+ 'yes'
					+ ',resizable='				+ 'yes'
					+ ',status='				+ 'no'

	var url = wb_utils_invoke_disp_servlet(
		'env','wizb',
		'module','content.ReferenceModule',
		'cmd','get_reference_list',
		'stylesheet',tpl_use,
		'mod_id',mod_id
	)
	if (not_popup) {
		self.location.href = url;
	} else {
		test_player = wbUtilsOpenWin(url, 'test_player' + mod_id, false, str_feature);
		test_player.focus();
	}
}

function wbModuleStartRefExec(mod_type,mod_id,tpl_use,cos_id,tkh_id, not_popup, usr_id, mod_end_time,itm_content_end_date){
	
	//检查在线学学习内容的时间是否已过
	if(wbModeEnd(mod_end_time,itm_content_end_date)){
		return;
	}
	str_feature = 	  'toolbar='				+ 'no'
					+ ',width=' 				+ '1050'
					+ ',height=' 				+ '500'
					+ ',scrollbars='			+ 'yes'
					+ ',resizable='				+ 'yes'
					+ ',status='				+ 'no'

	var url = wb_utils_invoke_disp_servlet(
		'env','wizb',
		'module','content.ReferenceModule',
		'cmd','get_reference_list',
		'tpl_use',tpl_use,
		'mod_id',mod_id,
		'mod_type',mod_type,
		'cos_id',cos_id,
		'tkh_id',tkh_id,
		'url_failure','../htm/close_window.htm',
		'url_success','../htm/close_window.htm',
		'stylesheet','start_module.xsl'
	)
    
    if (not_popup) {
        self.location.href = url;
    } else {
    	test_player = wbUtilsOpenWin(url, 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id, false, str_feature);
    	test_player.focus();    
    }

}

function wbModuleStartExec(mod_type,mod_id,tpl_use,cos_id,lang, tkh_id, max_attempt, usr_attempt, mov_status, sub_after_passed_ind, /*essay_grade_status, */not_popup, usr_id, test_style, mod_end_time,itm_content_end_date){
	

	if(test_style != undefined && test_style != ''){
		if(test_style =='many')
			tpl_use = 'tst_view_many.xsl';   //test_view_share_many.xsl
		else
			tpl_use = 'tst_player1.xsl';
	}
	
	//检查在线学学习内容的时间是否已过
	if(wbModeEnd(mod_end_time,itm_content_end_date)){
		return;
	}
		if (tkh_id == null) {
			tkh_id=0
		}

		if (mod_type == 'TST' || mod_type == 'DXT' || mod_type == 'EXC' || mod_type == 'STX' || mod_type == 'VOD' || mod_type == 'VCR' || mod_type == 'FOR' || mod_type == 'CHT' || mod_type == 'SVY' || mod_type == 'FAQ' || mod_type == 'GLO' || mod_type == 'EVN' || mod_type == 'ASS' ) {
			isFullScreen = false;
		}else {
			isFullScreen = true;
		}

		var str_feature = _wbModuleGetContentWindowSize(mod_type,isFullScreen)

		if (mod_type == 'TST' || mod_type == 'DXT') {wb_utils_set_cookie('lrn_tst_rpt_reload_parent','true');}

		if (mod_type == 'EXC' || mod_type == 'TST' || mod_type == 'DXT' || mod_type == 'SVY' || mod_type == 'EVN' ) {
			cmd = 'get_tst'
		} else {
			cmd = 'get_mod'
		}
		wb_utils_set_cookie('isWizpack', 'false')
		
		if(mod_type == 'VOD'){
			url = wb_utils_invoke_servlet(
					'cmd','get_mod',
					'mod_id',mod_id,
					'mod_type',mod_type,
					'tpl_use', tpl_use,
					'cos_id',cos_id,
					'tkh_id',tkh_id,
					'test_style',test_style,
					'stylesheet', 'lrn_vod.xsl',
					'url_failure','../htm/close_window.htm',
					'page','0'
				)
		}else{
			url = wb_utils_invoke_servlet(
					'cmd','get_mod_status',
					'mod_id',mod_id,
					'mod_type',mod_type,
					'tpl_use', tpl_use,
					'cos_id',cos_id,
					'tkh_id', tkh_id,
					'test_style',test_style,
					'url_failure','../htm/close_window.htm',
					'stylesheet','start_module.xsl'
			)
		}
		
        if (not_popup || mod_type == 'TST' || mod_type == 'DXT') {
            self.location.href = url;
        } else {
    		test_player = wbUtilsOpenWin(url, 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id, false, str_feature);
    //		test_player.document.write('<font size="2" face="Arial">'+eval('wb_msg_'+lang+'_loading_msg')+'</font>');
    		test_player.focus();
        }
}

function wbModuleStartNETg(cos_id, mod_id, res_src_url, tkh_id, not_popup, usr_id){

	var cur_url = parent.location.href;
	var cur_domain = cur_url.substr(0,cur_url.search("/servlet/"));

	var str_feature
	var screenAvailWidth, screenAvailHeight, isFullScreen
	screenAvailWidth = screen.availWidth-10
	screenAvailHeight = screen.availHeight-50	//with taskbar
	//Dialog.alert(screenAvailWidth + '/' + screenAvailHeight)

	isFullScreen = false;

	if (isFullScreen == true){
		str_feature = 'toolbar='	+ 'no'
		+ ',width=' 				+ screenAvailWidth
		+ ',height=' 				+ screenAvailHeight
		+ ',scrollbars='			+ 'no'
		+ ',resizable='				+ 'no';
	}else{
		str_feature = 'toolbar='				+ 'no'
		+ ',width='	 				+ '780'
		+ ',height=' 				+ '430'
		+ ',scrollbars='				+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',status='					+ 'no';
	}

	var home = wb_utils_invoke_disp_servlet(
		'env','wizb',
		'module','netg.NETgTrackingModule',
		'cmd','exit_cookie_course',
		'url_success','../htm/close_window.htm',
		'url_failure','../htm/close_window.htm'
	)
	home = cur_domain + home.substring(2,home.length)
	home += "&mod_id=" + mod_id;
	home += "&cos_id=" + cos_id;
	home += "&tkh_id=" + tkh_id;
	home += "&usr_id=" + usr_id;


	var url = wb_utils_invoke_disp_servlet(
		'module', 'netg.NETgTrackingModule',
		'cmd', 'LAUNCH_COOKIE_COURSE',
		'cos_id', cos_id,
		'mod_id', mod_id,
		'tkh_id', tkh_id,
		'cos_url', res_src_url,
		'home', home,
		'url_success', '../htm/close_window.htm',
		'url_failure','../htm/close_window.htm'
	)
    if(not_popup) {
        self.location.herf = url;
    } else {
    	test_player = wbUtilsOpenWin(url, 'netg_test_player' + usr_id + '_' + tkh_id + '_' + mod_id, false, str_feature)
    }
}

function wbModulePreviewNETg(cos_id, mod_id, res_src_url){
	var cur_url = parent.location.href;
	var cur_domain = cur_url.substr(0,cur_url.search("/servlet/"));

	var str_feature
	var screenAvailWidth, screenAvailHeight, isFullScreen
	screenAvailWidth = screen.availWidth-10
	screenAvailHeight = screen.availHeight-50	//with taskbar
	//Dialog.alert(screenAvailWidth + '/' + screenAvailHeight)

	isFullScreen = false;

	if (isFullScreen == true){
		str_feature = 'toolbar='	+ 'no'
		+ ',width=' 				+ screenAvailWidth
		+ ',height=' 				+ screenAvailHeight
		+ ',scrollbars='			+ 'no'
		+ ',resizable='				+ 'no';
	}else{
		str_feature = 'toolbar='				+ 'no'
		+ ',width='	 				+ '780'
		+ ',height=' 				+ '430'
		+ ',scrollbars='				+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',status='					+ 'no';
	}

	var home = cur_domain + '/htm/close_window.htm'

	var url = wb_utils_invoke_disp_servlet(
		'module', 'netg.NETgTrackingModule',
		'cmd', 'LAUNCH_COOKIE_COURSE',
		'cos_id', cos_id,
		'mod_id', mod_id,
		'cos_url', res_src_url,
		'home', home,
		'url_success', '../htm/close_window.htm'
	)

	test_player = wbUtilsOpenWin(url,'netg_test_player',false,str_feature)
}

function wbModuleStartBookmarkExec(mod_type,mod_id,tpl_use,cos_id){

		if ( mod_type == 'CHT' ){
			str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '615'
			+ ',height=' 				+ '415'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
		}else if ( mod_type == 'FOR' || mod_type == 'FAQ' ){
			str_feature = 'toolbar='		+ 'yes'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
		}else if ( mod_type == 'VCR' ){
			str_feature = 'toolbar='		+ 'yes'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '400'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
		}else if ( mod_type == 'VOD' ){
			str_feature = 'toolbar='		+ 'yes'
			+ ',width=' 				+ '400'
			+ ',height=' 				+ '300'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
		}
		else{
			str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',status='				+ 'no'
		}


		if (mod_type == 'EXC' || mod_type == 'TST' || mod_type == 'DXT') {
			cmd = 'get_tst'
		} else {
			cmd = 'get_mod'
		}
		wb_utils_set_cookie('isWizpack', 'false')
		url = wb_utils_invoke_servlet('cmd',cmd,'tpl_use', tpl_use,'mod_id',mod_id,'mod_type',mod_type,'cos_id',cos_id,'url_failure','../htm/close_window.htm','stylesheet','start_module.xsl')
		test_player = wbUtilsOpenWin(url, 'test_player' + mod_id, false,str_feature);
		test_player.focus();

}

function wbModuleStartContentURL(mod_type,mod_id,tpl_use,cos_id, tkh_id, win_name){
		var url,url_success;
		if (mod_type == 'EXC' || mod_type == 'SVY' || mod_type == 'EVN') {
			var cmd = 'get_tst'
		}else if( mod_type == 'TST' || mod_type == 'DXT'){  // to optimize test module for learner
			var cmd = 'get_tst_test_html'
	    }else {
			var cmd = 'get_mod'
		}
		if ( mod_type == 'STX' ){
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,'stylesheet', 'stx_wizard.xsl',
				'mod_id',mod_id,
				'tkh_id', tkh_id,
				'url_failure','../htm/close_window.htm'
			)
		}else if ( mod_type == 'FOR'){
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,
				'stylesheet', tpl_use,
				'mod_id',mod_id,
				'cos_id',cos_id,
				'tkh_id',tkh_id,
				'url_failure','../htm/close_window.htm',
				'cur_page','1',
				'sort_col','fto_title',
				'sort_order','asc',
				'timestamp',''
			)
		}else if (mod_type == 'REF') {
			url_success = wb_utils_invoke_disp_servlet(
				'cmd','get_reference_list',
				'module','content.ReferenceModule',
				'mod_id',mod_id,
				'tkh_id',tkh_id,
				'stylesheet',tpl_use
			)
		}else if(mod_type == 'GLO') {
			url_success = wb_utils_invoke_disp_servlet(
				'cmd','get_keys',
				'module','content.GlossaryModule',
				'glo_res_id',mod_id,
				'tkh_id', tkh_id,
				'stylesheet','glo_preview.xsl',
				'url_failure','../htm/close_window.htm'
			)
		}else if(mod_type == 'TST' || mod_type == 'DXT'){
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,'stylesheet',
				tpl_use,'mod_id',mod_id,
				'cos_id',cos_id,
				'tkh_id',tkh_id,
				'url_success','../htm/close_window.htm',
				'url_failure','../htm/close_window.htm',
				'page','0',
				'window_name', win_name
			)
		}else{
			url_success = wb_utils_invoke_servlet(
				'cmd',cmd,'stylesheet',
				tpl_use,'mod_id',mod_id,
				'cos_id',cos_id,
				'tkh_id',tkh_id,
				'url_failure','../htm/close_window.htm',
				'page','0'
			)
		}
		return url_success;
}


function wbModuleSTXPcikObjective(frm,lang){

	obj_lst = _wbModuleGetResNumber(frm)

	if (obj_lst == "") {
		Dialog.alert(eval('wb_msg_' + lang + '_select_stx_obj'))
		return
	}

	url = wb_utils_invoke_servlet('cmd','get_prof','mod_id',frm.mod_id.value,'obj_id_lst',obj_lst,'usr_id',frm.usr_id.value,'tpl_use',frm.tpl_use.value,'stylesheet','stx_que_parm.xsl')
	window.location.href = url
}

function wbModuleStartSTX(frm,lang){

	if (_wbModuleValidateSTXFrm(frm,lang)) {

			frm.mod_id.value = getUrlParam('mod_id')
			frm.obj_id_lst.value = getUrlParam('obj_id_lst')
			frm.stylesheet.value = getUrlParam('tpl_use')
			frm.url_failure.value = self.location.href
			frm.msp_type.value = frm.mod_qtype.options[frm.mod_qtype.selectedIndex].value;
			frm.msp_difficulty.value = frm.mod_qdiff.options[frm.mod_qdiff.selectedIndex].value;

			frm.cmd.value = 'get_tst'
			frm.method = "get"
			frm.action = wb_utils_servlet_url
			frm.submit()
		}
}

function wbModuleWizPackHeaderURL(mod_id){
	url = wb_utils_invoke_servlet('cmd','get_res_imscp','res_id',mod_id,'url_failure','../htm/close_window.htm','stylesheet','wizpack_header.xsl')
	return url
}

function wbMoudleWizPackFooterURL(mod_id){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','wizpack_footer.xsl')
	return url
}

function wbModuleWizPackContentURL(mod_id){

	url = wb_utils_invoke_servlet('cmd','get_res_imscp','res_id',mod_id,'url_failure','../htm/close_window.htm','stylesheet','wizpack_tree.xsl')
	return url
}


//------------------------TST/EXC-------------------------------------
/* constructor */
function wbTst() {
	this.ins_obj = wbTstInsObjective
	this.del_obj = wbTstDelObjective
	this.info_nav_url = wbTstInfoNavURL
	this.info_menu_url = wbTstInfoMenuURL
	this.info_content_url = wbTstInfoContentURL
	this.validate = wbTstValidate
}

 wbTst.prototype = new wbModule

/* private functions */
function _wbTstIsOnline(status) {
		if ( status == 'ON')
			return true;
		else
			return false;
}

function _wbTstGetQueNumber(frm) {
		var i, n, ele, str
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.checked) {
				if (ele.value !="")
					str = str + ele.value + "~"
			}
		}

		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		return str;
}


function _wbTstCheckOnline(mod_id,status,timestamp,lang){
		tst = new wbModule
		if (_wbTstIsOnline(status)) {
			if (confirm(eval('wb_msg_' + lang + '_confirm_change_offline'))) {
				url_success = parent.location.href
				tst.upd_sts_exec(mod_id,status,timestamp)
			}
			return false;
		}
		return true;
}


function _wbTstCheckAttempt(lang){

		if (wb_utils_get_cookie('mod_attempted') == "TRUE") {
			Dialog.alert(eval('wb_msg_' + lang + '_attempted'))
			return true;
		} else {
			return false;
		}

}

function _wbTstCheckIntegrity(mod_id,status,timestamp,lang){
	if ( _wbTstCheckAttempt(lang)) return false;
		//if (! _wbTstCheckOnline(mod_id,status,timestamp,lang)) return false;
		return true;
}

/* public functions */
function wbTstInsObjective(cos_id,mod_id,type){
	if (type == 'DAS' || type == 'FAS' ) {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',mod_id,'stylesheet','tst_que_pick_obj_frame.xsl','url_failure',parent.location.href)
	}else{	
		var url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'course_id',cos_id,'dpo_view', 'IST_READ','stylesheet','tst_que_pick_obj_frame.xsl','upd_tst_que','true','url_failure',parent.location.href)
	}
	parent.location.href = url;
}


function wbTstDelObjective(obj_id,mod_id,timestamp,status,lang){
	res_subtype = wb_utils_get_cookie('res_subtype');
	if (res_subtype == 'FAS' ) {
		if (confirm(wb_msg_confirm_remove_que)) {	
			url_success = parent.location.href
			url_failure = parent.location.href
			url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','del_que_container_obj','res_id',mod_id,'obj_id',obj_id,'url_success',url_success, 'url_failure',url_failure,'isExcludes',true);
			parent.location.href = url
		}
	}else{
		if (! _wbTstCheckIntegrity(mod_id,status,timestamp,lang)) return;

		if (confirm(wb_msg_confirm_remove_que)) {
			url_success = parent.location.href
			url_failure = parent.location.href
			url = wb_utils_invoke_servlet('cmd','del_mod_obj','mod_id',mod_id, 'obj_id',obj_id,'url_success',url_success,'url_failure',url_failure,'mod_timestamp',timestamp,'isExcludes',true)
			parent.location.href = url
		}
	}


}

function wbTstInfoNavURL(id,mod_id, res_subtype, mode){
	if (res_subtype == 'FAS' ) {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',mod_id,'stylesheet','tst_info_nav.xsl','url_failure',self.location.href, 'mode', mode)
	}else if (res_subtype == 'DAS' ){
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',mod_id,'stylesheet','tst_info_nav.xsl','url_failure',self.location.href, 'mode', mode)
	}else if (res_subtype == 'FSC') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_fsc_content','res_id',mod_id,'stylesheet','res_sc_info_nav.xsl','url_failure',self.location.href, 'mode', mode)
	}else if (res_subtype == 'DSC') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dsc_content','res_id',mod_id,'stylesheet','res_sc_info_nav.xsl','url_failure',self.location.href, 'mode', mode)
	}else {
		url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'course_id',id,'dpo_view', 'IST_READ','stylesheet','tst_info_nav.xsl', 'mode', mode)
	}
	return url;
}

function wbTstInfoMenuURL(id, res_subtype, mode){
    var obj_id = 0;
    if (getUrlParam('obj_id')) {
        obj_id = getUrlParam('obj_id');
    }
	if (res_subtype == 'DAS' || res_subtype == 'FAS' ) {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_fixed_assessment','res_id',id,'stylesheet','tst_info_menu.xsl','url_failure',self.location.href, 'mode', mode)
	}else if (res_subtype == 'FSC') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_fsc_content','res_id',id,'obj_id',obj_id,'stylesheet','res_sc_info_menu.xsl','url_failure',self.location.href, 'mode', mode)
	}else if (res_subtype == 'DSC') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dsc_content','res_id',id,'obj_id',obj_id,'stylesheet','res_sc_info_menu.xsl','url_failure',self.location.href, 'mode', mode)
	}else{
		url = wb_utils_invoke_servlet('cmd','get_res_cnt_lst','res_id',id,'dpo_view','IST_READ','stylesheet','tst_info_menu.xsl')
	}
	return url

}

function wbTstInfoContentURL(mod_id,course_id, res_subtype, mode){
	if (res_subtype == 'FAS' ) {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_assessment_obj','res_id',mod_id,'stylesheet','tst_info_content.xsl','url_failure',self.location.href, 'mode', mode);
	}else if (res_subtype == 'DAS'){
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dynamic_assessment','res_id',mod_id,'stylesheet','tst_info_content.xsl','url_failure',self.location.href, 'mode', mode);
	}else if (res_subtype == 'FSC') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_fsc_content','res_id',mod_id,'stylesheet','res_sc_info_content.xsl','url_failure',self.location.href, 'mode', mode)
	}else if (res_subtype == 'DSC') {
		url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_dsc_content','res_id',mod_id,'stylesheet','res_sc_info_content.xsl','url_failure',self.location.href, 'mode', mode)
	}else{
		url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'course_id',course_id,'dpo_view','IST_READ','stylesheet','tst_info_content.xsl', 'mode', mode)
	}
	return url
}


function wbTstValidate(mod_id){
	url = wb_utils_invoke_servlet('cmd','validate_dxt','mod_id',mod_id,'url_success','exedjs_function(1)','url_failure','exedjs_function(1)');

	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '620'
			+ ',height=' 				+ '220'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

	wbUtilsOpenWin(url, 'dxt_validate', false, str_feature);
	
}

//------------------------STST/EXC-------------------------------------
/* constructor */
function wbExc() {
	this.reorder_que_prep = wbExcReorderQuestionPrep
	this.reorder_que_exec = wbExcReorderQuestionExec
}

wbExc.prototype = new wbTst

function wbExcReorderQuestionPrep(mod_id,status,timestamp,lang){
	res_subtype = wb_utils_get_cookie('res_subtype');
	var url_failure = wbTstInfoMenuURL(mod_id, res_subtype)
	if (res_subtype == 'FAS' || res_subtype == 'DAS'){
		var url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_fixed_assessment','res_id',mod_id,'stylesheet','tst_info_menu_reorder.xsl','url_failure',url_failure)
	}else if (res_subtype == 'FSC' || res_subtype == 'DSC'){
		var url = wb_utils_invoke_disp_servlet('module','quebank.QueBankModule','cmd','get_fixed_assessment','res_id',mod_id,'stylesheet','tst_info_menu_reorder.xsl','url_failure',url_failure)
	}else{
		var url = wb_utils_invoke_servlet('cmd','get_res_cnt_lst','res_id',mod_id, 'dpo_view','IST_READ','stylesheet','tst_info_menu_reorder.xsl','upd_tst_que','true','url_failure',url_failure)
	}
	if ( !_wbTstCheckIntegrity(mod_id,status,timestamp,lang))
		return;

	window.location.href = url;

}

function wbExcReorderQuestionExec(frm,n,mod_id,timestamp,lang){
	res_subtype = wb_utils_get_cookie('res_subtype');
	if (res_subtype == 'DAS' || res_subtype == 'FAS' || res_subtype == 'FSC' || res_subtype == 'DSC') {
		frm.action = wb_utils_disp_servlet_url
		frm.module.value = 'quebank.QueBankModule'
		frm.cmd.value = 'reorder_que'
		frm.res_id.value = mod_id
		frm.res_upd_timestamp.value = timestamp
		
	}else{
		frm.action = wb_utils_servlet_url + "?isExcludes=true";
		frm.cmd.value = 'order_mod_que'
	
	}
		
		var que_order_lst = "";
		var que_id_lst = "";
			for(var i=0;i<frm.que_lst.options.length;i++){
				que_order_lst += i+1 + "~";
				que_id_lst += frm.que_lst.options[i].value + "~";
			}
		if(que_order_lst != ''){
			que_order_lst = que_order_lst.substring(0,que_order_lst.length -1)
			que_id_lst = que_id_lst.substring(0,que_id_lst.length -1)
		}
		frm.method = 'post'
		frm.mod_id.value = mod_id
		frm.que_id_lst.value = que_id_lst
		frm.que_order_lst.value = que_order_lst
		frm.mod_timestamp.value = timestamp
		frm.url_success.value = parent.location.href
		frm.url_failure.value = parent.location.href
		frm.target = parent.name
		frm.submit()
}

function wbModuleReload(){
	var curFrame = window;
	cur_fr_num = 0;
	prev_fr_num = -1;
	while(curFrame && cur_fr_num!=prev_fr_num){
		cur_fr_num = curFrame.frames.length;
		curFrame = curFrame.parent;
		prev_fr_num = curFrame.frames.length;
	}
	if(curFrame.opener)
		curFrame.opener.location.reload();
}

function getAiccUrl(sco_ver) {
	var result = null;
	var servlet = '';
	if (sco_ver && sco_ver == '2004') {
		servlet = wb_utils_servlet_package_sco2004cmi;
	} else {
		servlet = wb_utils_servlet_package_cmi;
	}
	
	var virtual_path_loc = self.location.pathname.indexOf('/app/');
	if (virtual_path_loc === -1) {
		virtual_path_loc = self.location.pathname.indexOf('/servlet/');
	}
	if (virtual_path_loc > -1) {
		var virtual_path = self.location.pathname.substring(0, virtual_path_loc);
		result = encodeURIComponent(self.location.protocol + "//" + self.location.host + virtual_path + "/servlet/" + servlet + "?");
	} else {
		//Dialog.alert('aicc_url error:' + self.location.pathname);
		result = encodeURIComponent("/servlet/" + wb_utils_servlet_package_cmi + "?");
	}
	return result;
}

function wbModuleStartAICCAu(src_link,ent_id,cos_id,mod_id,web_launch,mod_vendor, tkh_id, not_popup, mod_end_time,itm_content_end_date){
	
	//检查在线学学习内容的时间是否已过
	if(wbModeEnd(mod_end_time,itm_content_end_date)){
		return;
	}
	
	if(src_link.indexOf('../') >= 0){
    	src_link = wb_utils_app_base + src_link.substring(src_link.lastIndexOf('../') + 3, src_link.length)	
    }
	today = new Date
	today = _wbModuleGetTime(today)

	var aicc_url = getAiccUrl();
	if (aicc_url === null) {
		return;
	}
	if ( web_launch == '' )
		url = src_link + '?aicc_sid=' + escape(ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:' ) + '&aicc_url=' + aicc_url
	else
		url = src_link + '?aicc_sid=' + escape(ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:' ) + '&aicc_url=' + aicc_url + '&' + web_launch
		
	
	
	var screenAvailWidth = screen.availWidth-10
	var screenAvailHeight = screen.availHeight-50
	var scr_width = (screenAvailWidth > 800) ? 850 : screenAvailWidth ;
	var scr_Height = (screenAvailHeight > 600) ? 600 : screenAvailHeight;
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ scr_width
			+ ',height=' 				+ scr_Height
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

    if (not_popup) {
        self.location.href = url;
    } else {
    	test_player = wbUtilsOpenWin(url, 'test_player' + ent_id + '_' + tkh_id + '_' + mod_id, false, str_feature);
    	test_player.focus();
    }
}

function wbModulePreviewAICCAu(src_link,ent_id,cos_id,mod_id,web_launch,mod_vendor,tkh_id,is_multi_sco){
	if(src_link.indexOf('../') >= 0){
		src_link = wb_utils_app_base + src_link.substring(src_link.lastIndexOf('../') + 3, src_link.length)	
	}
	if (tkh_id == null || tkh_id=='') {
		tkh_id=0
	}
	today = new Date
	today = _wbModuleGetTime(today)


	var aicc_url = getAiccUrl();
	if (aicc_url === null) {
		return;
	}

	var aicc_sid = null;
	if (ent_id && cos_id && mod_id && mod_vendor && tkh_id) {
		aicc_sid = escape(ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:');
	} else {
		aicc_sid = 'VIEWONLY';
	}
	
	if ( web_launch == '' )
		url = src_link + '?aicc_sid=' + aicc_sid + '&aicc_url=' + aicc_url
	else
		url = src_link + '?aicc_sid=' + aicc_sid + '&aicc_url=' + aicc_url + '&' + web_launch

	var screenAvailWidth = screen.availWidth-10
	var screenAvailHeight = screen.availHeight-50
	var scr_width = (screenAvailWidth > 800) ? 850 : screenAvailWidth ;
	var scr_Height = (screenAvailHeight > 600) ? 600 : screenAvailHeight;

	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ scr_width
			+ ',height=' 				+ scr_Height
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'


	test_player = wbUtilsOpenWin(url, 'test_player' + mod_id, false, str_feature);
	test_player.focus();
}

function wbModuleStartSCORM(src_link,ent_id,cos_id,mod_id,web_launch,mod_vendor, tkh_id, not_popup, is_multi_sco, sco_version, mod_end_time,itm_content_end_date){
	
	//检查在线学学习内容的时间是否已过
	if(wbModeEnd(mod_end_time,itm_content_end_date)){
		return;
	}
	if(src_link.indexOf('../') >= 0){
		src_link = wb_utils_app_base + src_link.substring(src_link.lastIndexOf('../') + 3, src_link.length)	
	}
	
	today = new Date
	today = _wbModuleGetTime(today)

	var aicc_url = getAiccUrl(sco_version);
	if (aicc_url === null) {
		return;
	}
	if (sco_version == '2004') {
		//thisURL="url=courses/course1/imsmanifest.xml&cid=21321123&uid=test01";
		//url为课件根目录下的imsmanifest.xml；cid为整个课件ID；uid为学员ID；
		//如果没有url，不能够打开课件；如果没有cid或uid，则为浏览模式，非正常学习；
		thisURL = '..'+escape(src_link)+'?cid='+mod_id+'&uid='+tkh_id;
		url =getScormPlayer(sco_version) +"?url="+thisURL+"&rm="+Math.random()
	
		
	}else{
		if ( web_launch == '' ){
			url = getScormPlayer(sco_version) + '?env=wizb&mod_id='+ mod_id+ '&cos_url=' + escape(src_link) + '&aicc_sid=' + escape(ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:') + '&aicc_url=' + aicc_url;
	
		}else{
	
			url = getScormPlayer(sco_version) + '?env=wizb&mod_id='+ mod_id+  '&cos_url=' + escape(src_link) + '&aicc_sid=' + escape(ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:') + '&aicc_url=' + aicc_url + '&web_launch=' + web_launch;
	
		}
		url = url + '&cos_id=' + cos_id;
		url = url + '&tkh_id=' + tkh_id;
		url = url + '&mod_id=' + mod_id;
		url = url + '&is_multi_sco=' + is_multi_sco;
	}
	var screenAvailWidth = screen.availWidth-10
	var screenAvailHeight = screen.availHeight-50
	var scr_width = (screenAvailWidth > 800) ? 850 : screenAvailWidth ;
	var scr_Height = (screenAvailHeight > 600) ? 600 : screenAvailHeight;

	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ scr_width
		+ ',height=' 				+ scr_Height
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		
		str_feature="";
    if (not_popup) {
        self.location.href = url;   
    } else {
    	test_player = wbUtilsOpenWin(url, 'test_player' + ent_id + '_' + tkh_id + '_' + mod_id, false, str_feature);
    	test_player.focus();
    }
}

function wbModulePointSCORM(src_link,ent_id,cos_id,mod_id,mod_vendor,tkh_id){
	if(src_link.indexOf('../') >= 0){
    	src_link = wb_utils_app_base + src_link.substring(src_link.lastIndexOf('../') + 3, src_link.length)	
    }
	today = new Date
	today = _wbModuleGetTime(today)
	aicc_url = "http://" + parent.location.href.substring(7,parent.location.href.indexOf('htm')) + "servlet/" + wb_utils_servlet_package_cmi + "?"
	if(tkh_id==null || tkh_id==''){
		aicc_sid = 'VIEWONLY';
	}else{
		aicc_sid = ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:';
	}
	sco_ver = parent.sco_ver;
	if (sco_ver && sco_ver == '2004') {
		parent.API_1484_11.Terminate('');
		parent.API_1484_11.clearState();
		parent.API_1484_11.setSessionID(aicc_sid);
	} else {
		API = parent.frames["adapter"].API;
		API.setCmiURL(aicc_url);
		API.setSessionID(aicc_sid);
		API.setCbtAiccVersion('2.2');
		API.getParam();
	}
	parent.frames["content"].location = src_link;
	/*
	url = '../htm/sco_player.htm?env=wizb' + '&cos_url=' + escape(src_link) + '&aicc_sid=' + escape(aicc_sid) + '&aicc_url=' + aicc_url + '&cos_id=' + cos_id + '&tkh_id=' + tkh_id + '&mod_id=' +mod_id;
//	parent.location.href = url;
	parent.frames["content"].location = src_link;*/
}

function wbModulePreviewSCORM(src_link,ent_id,cos_id,mod_id,web_launch,mod_vendor,is_multi_sco,sco_version){
	today = new Date
	today = _wbModuleGetTime(today)
	
	if(src_link.indexOf('../') >= 0){
		src_link = wb_utils_app_base + src_link.substring(src_link.lastIndexOf('../') + 3, src_link.length)	
	}
	
	var aicc_url = getAiccUrl(sco_version);
	if (aicc_url === null) {
		return;
	}
	
	if (sco_version == '2004') {
		//thisURL="url=courses/course1/imsmanifest.xml&cid=21321123&uid=test01";
		//url为课件根目录下的imsmanifest.xml；cid为整个课件ID；uid为学员ID；
		//如果没有url，不能够打开课件；如果没有cid或uid，则为浏览模式，非正常学习；
		thisURL = '..'+escape(src_link)+'?cid='+mod_id;
		url =getScormPlayer(sco_version) +"?url="+thisURL+"&rm="+Math.random()
	
		
	}else{
		if ( web_launch == '' ){
			url = getScormPlayer(sco_version) + '?env=wizb' + '&cos_url=' + escape(src_link) + '&aicc_sid=VIEWONLY' + '&aicc_url=' + aicc_url;
		}else{
			url = getScormPlayer(sco_version) + '?env=wizb' + '&cos_url=' + escape(src_link) + '&aicc_sid=VIEWONLY' + '&aicc_url=' + aicc_url + '&web_launch=' + web_launch;
		}
		url = url + '&cos_id=' + cos_id;
		url = url + '&mod_id=' + mod_id;
		url = url + '&is_multi_sco=' + is_multi_sco;
	}
	/*url = url + '&mod_id=' + mod_id;
	url = url + '&is_multi_sco=' + is_multi_sco;*/
	var screenAvailWidth = screen.availWidth-10
	var screenAvailHeight = screen.availHeight-50
	var scr_width = (screenAvailWidth > 800) ? 850 : screenAvailWidth ;
	var scr_Height = (screenAvailHeight > 600) ? 600 : screenAvailHeight;

	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ scr_width
		+ ',height=' 				+ scr_Height
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
	test_player = wbUtilsOpenWin(url, 'test_player' + mod_id, false, str_feature);
	test_player.focus();
}

function getScormPlayer(sco_ver, is_mobile) {
	if(is_mobile){
		if (sco_ver == '2004') {
			return wb_utils_app_base + 'scorm2004Player/player_mobile.html';
		} else {
			return wb_utils_app_base + 'htm/sco_player_mobile.htm';
		}
	}else{
		if (sco_ver == '2004') {
			return wb_utils_app_base + 'scorm2004Player/player.html';
		} else {
			return wb_utils_app_base + 'htm/sco_player.htm';
		}
	}	
}
/* constructor */
function wbSvy() {
	this.info_nav_url = wbSvyInfoNavURL
	this.info_menu_url = wbSvyInfoMenuURL
	this.info_content_url = wbSvyInfoContentURL
}

 wbSvy.prototype = new wbModule

/* private functions */

function wbSvyInfoNavURL(id,mod_id){
	var url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'course_id',id,'dpo_view', 'IST_READ','stylesheet','cos_evn_info_nav.xsl')
	return url;

}

function wbSvyInfoMenuURL(id){
	var url = wb_utils_invoke_servlet('cmd','get_res_cnt_lst','res_id',id,'dpo_view','IST_READ','stylesheet','cos_evn_info_menu.xsl')
	return url

}

function wbSvyInfoContentURL(mod_id,course_id){
	var url = wb_utils_invoke_servlet('cmd','get_mod','mod_id',mod_id,'course_id',course_id,'dpo_view','IST_READ','stylesheet','cos_evn_info_content.xsl')
	return url
}

// Glossary --------------------------------------------------------------------------------
function wbModuleGetGloKey(glo_id,res_id) {
	wb_utils_set_cookie('RESID',res_id);
	url = wb_utils_invoke_disp_servlet('module','content.GlossaryModule','cmd','get_key','glo_id',glo_id,'stylesheet','glo_edit_key.xsl')
	window.location.href = url;
}

function wbModuleGetGloKeys(res_id, first_char,timestamp,stylesheet, tkh_id) {

	if(stylesheet == 'true'){
		stylesheet = 'glo_preview.xsl'
	}else{
		stylesheet = 'glo_edit.xsl'
	}

	if(timestamp == null ){
		url = wb_utils_invoke_disp_servlet('module','content.GlossaryModule','cmd','get_keys','glo_res_id',res_id,'stylesheet',stylesheet)
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 				+ '780'
	+ ',height=' 				+ '420'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'no'
		test_player = wbUtilsOpenWin(url, 'test_player',false, str_feature);
		test_player.focus();
	}else{
		url = wb_utils_invoke_disp_servlet('module','content.GlossaryModule','cmd','get_keys','glo_first_char',first_char,'glo_res_id',res_id,'glo_timestamp',timestamp,'stylesheet',stylesheet, 'tkh_id', tkh_id, 'url_failure', '../htm/close_window.htm')
		window.location.href = url;
	}

}

function wbModuleAddGloKey(res_id) {
	wb_utils_set_cookie('RESID',res_id);
	wb_utils_set_cookie('url_success',window.location.href)
	var url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','glo_add_key.xsl');
	window.location.href = url;
}

function wbModuleUpdGloKey(frm, lang) {
	frm.glo_key.value = wbUtilsTrimString(frm.glo_key.value);
	frm.glo_def.value = wbUtilsTrimString(frm.glo_def.value);
	if ( !gen_validate_empty_field(frm.glo_key, eval('wb_msg_' + lang + '_keyword'),lang)
	||   !gen_validate_empty_field(frm.glo_def, eval('wb_msg_' + lang + '_def'),lang) )
		return



	first_char = frm.glo_key.value.substring(0,1).toUpperCase()
	res_id = wb_utils_get_cookie('RESID')
	url_success = wb_utils_invoke_disp_servlet('module','content.GlossaryModule','cmd','get_keys','glo_first_char',first_char,'glo_res_id',res_id,'stylesheet','glo_edit.xsl')
	frm.url_success.value =url_success

	frm.action = wb_utils_disp_servlet_url
	frm.method = 'POST'
	frm.module.value = "content.GlossaryModule"
	frm.cmd.value = 'upd_key'
	frm.submit()
}

function wbModuleDelGloKey(frm, lang) {

	frm.cmd.value = 'del_key'

	first_char = frm.glo_key.value.substring(0,1).toUpperCase()
	res_id = wb_utils_get_cookie('RESID')
	url_success = wb_utils_invoke_disp_servlet('module','content.GlossaryModule','cmd','get_keys','glo_first_char',first_char,'glo_res_id',res_id,'stylesheet','glo_edit.xsl')
	frm.url_success.value = url_success
	frm.module.value = "content.GlossaryModule"
	frm.action = wb_utils_disp_servlet_url
	frm.method = 'POST'
	frm.submit()
}

function wbModuleInsGloKey(frm, lang) {
	frm.glo_key.value = wbUtilsTrimString(frm.glo_key.value);
	frm.glo_def.value = wbUtilsTrimString(frm.glo_def.value);
	if ( !gen_validate_empty_field(frm.glo_key, eval('wb_msg_' + lang + '_keyword'), lang)
	||   !gen_validate_empty_field(frm.glo_def, eval('wb_msg_' + lang + '_def'), lang) )
		return

	frm.glo_res_id.value = wb_utils_get_cookie('RESID')
	first_char = frm.glo_key.value.substring(0,1).toUpperCase()
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','content.GlossaryModule','cmd','get_keys','glo_first_char',first_char,'glo_res_id',frm.glo_res_id.value,'stylesheet','glo_edit.xsl')
	frm.action = wb_utils_disp_servlet_url
	frm.method = 'POST'
	frm.module.value = "content.GlossaryModule"
	frm.cmd.value = "ins_key"
	frm.submit()
}

function wbModuleEditRefExec(mod_id,tpl_use){
	url = wb_utils_invoke_disp_servlet('module','content.ReferenceModule','cmd','get_reference_list','mod_id',mod_id,'stylesheet',tpl_use)
	str_feature = 'toolbar='		+ 'no'
	+ ',width=' 				+ '780'
	+ ',height=' 				+ '420'
	+ ',scrollbars='			+ 'yes'
	+ ',resizable='				+ 'yes'
	+ ',status='				+ 'no'


	test_player = wbUtilsOpenWin(url, 'test_player',false, str_feature);
	test_player.focus();
}

function wbModuleCancelAdd(course_id) {
	if(window.document["gen_btn_cancel0"]){
		window.document["gen_btn_cancel0"].width = 0;
		window.document["gen_btn_cancel0"].height = 0;
	}
	window.parent.cancelAdd()
	window.location.href = course_lst.view_info_url(course_id);
}

function wbModuleSaveRatingQuestion(frm,itm_id,cos_id){
	rate = "";
	for(i=0;i<frm.ans.length;i++)
		if(frm.ans[i].checked)
			rate = frm.ans[i].value;
	if(rate=="")
		return;

	/*WaiLun: Used to the rating question answered by other
	ent_id = getUrlParam('ent_id');
	if( ent_id != '' ){
		url_prev = "../htm/close_and_call_opener_function.htm";
		url = wb_utils_invoke_ae_servlet("cmd","ae_save_rating","itm_id",itm_id,"rate",rate,"url_success",url_prev,'ent_id',ent_id);
	}else{
	*/
		url_prev = "../htm/close_and_reload_window.htm";
		url = wb_utils_invoke_ae_servlet("cmd","ae_save_rating","itm_id",itm_id,"rate",rate,"url_success",url_prev);
	//}
	self.location.href = url;
}

function wbModuleDefineRatingQuestion(){
	url = wb_utils_invoke_ae_servlet("cmd","ae_get_rating_defination_xml","stylesheet","");
	parent.location.href = url;
}

function wbModuleDownloadReport(mod_id){
	url = wb_utils_invoke_disp_servlet("module","content.SurveyModule","cmd","dl_svy_rpt","mod_id",mod_id,"stylesheet","tst_rpt_csv.xsl");
	parent.location.href = url;
}

function wbModuleExportReport(mod_id, mov_status){
	url = wb_utils_invoke_disp_servlet("module","content.SurveyModule","cmd","export_svy_rpt","mod_id",mod_id,"mov_status", mov_status, "stylesheet","tst_rpt_csv.xsl");
	parent.location.href = url;
}

function wbModuleExportReportByTkhIds(mod_id, mov_status, frm){
	var tkhIds = "";
	var checkboxs = document.getElementsByName("tkh_id"); 
    for(var i=0; i < checkboxs.length; i++){
    	if(checkboxs[i].checked){
    		tkhIds += checkboxs[i].value + ",";
    	}
    	if(checkboxs.length == i+1 && tkhIds != undefined && tkhIds.length > 0){
    		tkhIds = tkhIds.substring(0, tkhIds.length - 1);
    	}
    }
    
    if(tkhIds == undefined || tkhIds != undefined && tkhIds.length == 0){
		Dialog.alert(fetchLabel("label_core_training_management_474"));
		return;
	}
    
	url = wb_utils_invoke_disp_servlet("module","content.SurveyModule","cmd","export_svy_rpt","mod_id",mod_id,"mov_status", mov_status, "stylesheet","tst_rpt_csv.xsl","tkh_ids", tkhIds);
	parent.location.href = url;
}

function wbModuleDownloadReportByItm(mod_id, itm_id, ent_id_lst, startDate, endDate, run_id_lst){
	url = wb_utils_invoke_disp_servlet("module","content.SurveyModule","cmd","dl_svy_rpt_by_itm","mod_id",mod_id,"itm_id", itm_id, 'ent_id_lst', ent_id_lst, 'start_datetime', startDate,'end_datetime', endDate, 'run_id_lst', run_id_lst, "stylesheet","tst_rpt_csv.xsl");
	parent.location.href = url;
}



function wbModuleGetSubmissionListByStatus(mod_id, status,course_id){
	url = wb_utils_invoke_servlet('cmd','get_user_mod_status_list','cur_page','1','page_size','10','sort_col','usr_display_bil','sort_order','asc','mov_mod_id',mod_id,'mov_status',status,'stylesheet','module_user_status_list.xsl','course_id',course_id);
	window.location.href = url;
	return;
}



function wbModuleGetSubmissionList(mod_id,course_id){
	url = wb_utils_invoke_servlet('cmd','get_user_mod_status_list','cur_page','1','page_size','10','sort_col','usr_display_bil','sort_order','asc','mov_mod_id',mod_id,'stylesheet','module_user_status_list.xsl','course_id',course_id);

	window.location.href = url;
	return;
}

function wbModuleSortSubmissionList(col, order){
	wb_utils_nav_get_urlparam('sort_col',col,'sort_order',order,'cur_page','1');
}

function wbModuleChangeUserStatus(frm, status, lang){
	frm.to_status.value = status;
	n = frm.elements.length
	count = 0;
	for (i = 0; i < n; i++){
		ele = frm.elements[i];
		if (ele.type == "checkbox" && ele.name == 'tkh_id'){
			if (ele.checked == true){
				count++;
			}
		}
	}

	if( count == 0 ) {
		Dialog.alert(eval("wb_msg_"+ lang +"_select_learner"));
	} else {
		wb_utils_set_cookie("appn_usr_name","");
		wb_utils_set_cookie("current","");
		wb_utils_set_cookie("total","");
		wb_utils_set_cookie("type","change_mov_status");
		str_feature = 'toolbar='		+ 'no'
				+ ',width=' 				+ '400'
				+ ',height=' 				+ '180'
				+ ',scrollbars='			+ 'no'
				+ ',resizable='				+ 'no'
				+ ',status='				+ 'yes';
		url = "../htm/application_frame_window.htm?lang="+lang+"&processEndFunction=self_refresh";
		wbUtilsOpenWin(url,'change_mov_status', false, str_feature);
	}
	return;
}

function wbModuleViewSvyResult(mod_id, usr_ent_id, tkh_id){

	if (tkh_id == null) {
		tkh_id = '';
	}

    url_failure = '../htm/close_window.htm';
	url = wb_utils_invoke_servlet('cmd','get_lrn_tst_res','ent_id',usr_ent_id,'mod_id',mod_id,'stylesheet','svy_res.xsl', 'tkh_id', tkh_id, 'url_failure', url_failure);

	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '420'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		wbUtilsOpenWin(url, 'svy_result', false, str_feature);

}

function wbModuleViewEvnResult(mod_id, usr_ent_id, tkh_id){

	if (tkh_id == null) {
		tkh_id = '';
	}

	url = wb_utils_invoke_servlet('cmd','get_lrn_tst_res','ent_id',usr_ent_id,'mod_id',mod_id,'stylesheet','evn_res.xsl', 'tkh_id', tkh_id);

	str_feature = 'toolbar='		+ 'no'
		+ ',width=' 				+ '780'
		+ ',height=' 				+ '420'
		+ ',scrollbars='			+ 'yes'
		+ ',resizable='				+ 'yes'
		wbUtilsOpenWin(url, 'evn_result', false, str_feature);

}

function wbModuleEASGetSubmissionListByStatus(mod_id, status, mode){
	url = wb_utils_invoke_servlet('cmd','get_mod_eas_status_list','cur_page','1','page_size','1000','sort_col','usr_display_bil','sort_order','asc','mov_mod_id',mod_id,'mov_status',status,'stylesheet','mod_eas_status_list.xsl', 'mode', mode);
	window.location.href = url;
	return;
}


function wbModuleEASEditScore(mod_id, status, cur_page, sort_order, mode){
	url = wb_utils_invoke_servlet('cmd','get_mod_eas_status_list','cur_page','1','page_size','1000','sort_col','usr_display_bil','sort_order','asc','mov_mod_id',mod_id,'mov_status',status,'stylesheet','mod_eas_status_list.xsl', 'mode', mode);
	window.location.href = url;
	return;
}


function wbModuleEASGetSubmissionList(mod_id, status){
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

	url = wb_utils_invoke_servlet('cmd','get_mod_eas_status_list','cur_page','1','page_size','1000','sort_col','usr_display_bil','sort_order','asc','mov_mod_id',mod_id,'mov_status',status,'stylesheet','mod_eas_status_list.xsl');
	wbUtilsOpenWin(url, 'view_EAS', false, str_feature);
}


function wbModuleEASGetIndividual(mod_id, ent_id, tkh_id){
	url = wb_utils_invoke_servlet('cmd','get_mod_eas_individual','usr_ent_id',ent_id,'mov_mod_id',mod_id,'tkh_id', tkh_id, 'stylesheet','mod_eas_individual_list.xsl' );
	window.location.href = url;
	return;
}



function wbModuleEASViewResult(mod_id, ent_id){
	url = wb_utils_invoke_servlet('cmd','get_mod_eas_individual','usr_ent_id',ent_id,'mov_mod_id',mod_id,'stylesheet','mod_eas_individual_result.xsl' );
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'

	wbUtilsOpenWin(url, 'result_EAS', false, str_feature);
}

function wbModuleEASResetIndividual(frm, mod_id, ent_id, status, mov_last_upd_timestamp, mode, tkh_id){

	url = wb_utils_invoke_servlet('cmd','get_mod_eas_status_list','cur_page','1','page_size','1000','sort_col','usr_display_bil','sort_order','asc','mov_mod_id',mod_id,'mov_status',status,'stylesheet','mod_eas_status_list.xsl','mode', mode, 'tkh_id', tkh_id)

	frm.cmd.value = 'mod_eas_reset_individual'
	frm.url_success.value = url
	frm.usr_ent_id.value = ent_id
	frm.tkh_id.value = tkh_id
	frm.mov_last_upd_timestamp.value = mov_last_upd_timestamp
	frm.method = "post";
	frm.action = wb_utils_servlet_url;
	frm.submit()
}


function wbModuleEASGradeIndividual(frm,mod_id, lang, status, tkh_id){

	if (frm.grading_format.value == 'score'){
		if ( Number(frm.pgr_score.value) > Number(frm.max_score.value) || isNaN(Number(frm.pgr_score.value)) ){
			Dialog.alert(eval('wb_msg_' +lang+ '_not_greater_max_score'))
			frm.pgr_score.focus();
			return;
		}

		if (frm.pgr_score.value != '' && !gen_validate_float(frm.pgr_score, eval('wb_msg_' +lang+ '_rpt_score') ,lang)){
				return;
		}


	}
	if ( frm.ass_upload_file.value != '' ){
		frm.ass_filename.value = wbMediaGetFileName(frm.ass_upload_file.value)
		wb_utils_preloading();
	}

	url = wb_utils_invoke_servlet('cmd','get_mod_eas_status_list','cur_page','1','page_size','1000','sort_col','usr_display_bil','sort_order','asc','mov_mod_id',mod_id,'mov_status',status,'stylesheet','mod_eas_status_list.xsl', 'tkh_id', tkh_id);

	frm.url_success.value = url
	frm.method = "post";
	frm.action = wb_utils_servlet_url;
	frm.submit()

}


function wbModuleEASMassUpdOne(frm,lang,cos_id,mod_id,ass_queue){

	if ( frm.ass_upload_file.value != '' ){
		frm.ass_filename.value = wbMediaGetFileName(frm.ass_upload_file.value)
		wb_utils_preloading();
	}

	if (frm.grading_format.value == 'score'){
		if ( Number(frm.pgr_score.value) > Number(frm.max_score.value) || isNaN(Number(frm.pgr_score.value)) ){
			Dialog.alert(eval('wb_msg_' +lang+ '_not_greater_max_score'))
			frm.pgr_score.focus();
			return;
		}

	}

	url = wb_utils_invoke_servlet('cmd','get_ass_enrol_lst','course_id',cos_id, 'mod_id',mod_id,'ass_queue',ass_queue,'page',0,'ent_id_parent',0,'stylesheet', 'ass_sub_graded.xsl')
	frm.url_success.value = url
	frm.method = "post";
	frm.action = wb_utils_servlet_url;
	frm.submit()

}

function wbModuleEASGradeAll(frm, lang){
	ele = frm.pgr_grade
	if (frm.grading_format.value == 'score'){
		ele = frm.pgr_score
	}

	nullflag = 'true';
	for (i = 0; i < ele.length; i++){
		if (ele[i].value != ''){
			nullflag = 'false';
			break;
		}
	}
	if(nullflag == 'true'){
		Dialog.alert(eval('wb_msg_' +lang+ '_plz_score_grade'))
		ele[1].focus();
		return;
	}

	for (i = 0; i < ele.length; i++){
			if (ele[i].value == '' && frm.graded[i].value == 'y'){
				Dialog.alert(eval('wb_msg_' +lang+ '_plz_score_grade'))
				ele[i].focus();
				return;
			}
	}

	if (frm.grading_format.value == 'score'){
		n = frm.pgr_score.length
		count = 0;
		for (i = 0; i < n; i++){
			ele = frm.pgr_score[i];
			if (ele.value != '' && Number(ele.value) > Number(frm.mod_max_score.value) || isNaN(Number(ele.value)) ){
				Dialog.alert(eval('wb_msg_' +lang+ '_not_greater_max_score'))
				ele.focus();
				return;
			}
			if (ele.value != '' && !gen_validate_float(ele, eval('wb_msg_' +lang+ '_rpt_score') ,lang)){
				return;
			}

		}


	}

	wb_utils_set_cookie("appn_usr_name","");
	wb_utils_set_cookie("current","");
	wb_utils_set_cookie("total","");
	wb_utils_set_cookie("type","eas_mass_grade");
	str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '400'
			+ ',height=' 				+ '180'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',status='				+ 'yes';
	url = "../htm/application_frame_window.htm?lang="+lang+"&processEndFunction=self_refresh";
	wbUtilsOpenWin(url,'change_mov_status', false, str_feature);
	return;
}

function _wbModuleGetContentWindowSize(mod_type,isFullScreen){
		var screenAvailWidth, screenAvailHeight, isFullScreen
		var screenAvailWidth = screen.availWidth-10
		var screenAvailHeight = screen.availHeight-50	//with taskbar

		var scr_width = (screenAvailWidth > 800) ? 800 : screenAvailWidth ;
		var scr_Height = (screenAvailHeight > 600) ? 600 : screenAvailHeight;
 
		if ( mod_type == 'CHT' ){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'no'
				+ ',resizable='				+ 'no';
			}else{
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ '656'
				+ ',height=' 				+ '400'
				+ ',scrollbars='			+ 'no'
				+ ',resizable='				+ 'no';
			}
		}else if ( mod_type == 'FOR'){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}else{
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ '1040'
				+ ',height=' 				+ '735'
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}
		}else if ( mod_type == 'FAQ' ){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}else{
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ '830'
				+ ',height=' 				+ '505'
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}
		}else if ( mod_type == 'VCR' ){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}else{
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ '830'
				+ ',height=' 				+ '400'
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}
		}else if ( mod_type == 'VOD' ){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}else{
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ '1050'
				+ ',height=' 				+ '500'
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';
			}
		}else if ( mod_type == 'GLO' ){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'auto'
				+ ',resizable='				+ 'no';
			}else{
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ '650'
				+ ',height=' 				+ '600'
				+ ',scrollbars='			+ 'auto'
				+ ',resizable='				+ 'no';
			}
		}else if( mod_type == 'RDG'){
				str_feature = 'toolbar='	+ 'yes'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes';

		}else if( mod_type == 'ASS'){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ scr_width
				+ ',height=' 				+ scr_Height
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'yes';
			}else{
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ '1050'
				+ ',height=' 				+ '500'
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'no';
			}
		}else if ( mod_type == 'TST' || mod_type == 'DXT' || mod_type == ''){
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ screenAvailWidth
				+ ',height=' 				+ screenAvailHeight
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'yes';
			}else{
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ '1218'
				+ ',height=' 				+ '650'
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'yes';
			}
		}else{
			if (isFullScreen == true){
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ scr_width
				+ ',height=' 				+ scr_Height
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'yes';
			}else{
				str_feature = 'toolbar='	+ 'no'
				+ ',width=' 				+ '984'
				+ ',height=' 				+ '520'
				+ ',scrollbars='			+ 'yes'
				+ ',resizable='				+ 'yes'
				+ ',status='				+ 'yes';
			}
		}
		if(document.all){
			if (isFullScreen == true) {str_feature += ',top=' + '0' + ',left=' + '0';}
			else {str_feature += ',top=' + '10' + ',left=' + '10';}
		}else{
			if (isFullScreen == true) {str_feature += ',screenX=' + '0' + ',screenY=' + '0';}
			else {str_feature += ',screenX=' + '10' + ',screenY=' + '10';}
		}
		return str_feature;
}

function _wbModuleValidateFrm2(frm, lang) {
	if (frm.ass_due_datetime && frm.ass_due_date_day){
		if(frm.ass_due_date_rad[1].checked == true){
			if (!wbUtilsValidateDate('document.frmXml.due',eval('wb_msg_' + lang + '_ass_due_datetime'),'','ymdhm')){
				return false
			}

		}else
		if(frm.ass_due_date_rad[2].checked == true){
			if(!gen_validate_positive_integer(frm.ass_due_date_day, eval('wb_msg_' + lang + '_ass_due_datetime'),lang)){
				frm.ass_due_date_day.focus()
				return false;
			}
			if(frm.ass_due_date_day.value <= '0'){
				alert(1);
				Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + eval('wb_msg_' + lang + '_ass_due_datetime') + '"' + wb_msg_pls_enter_positive_integer_2);

				frm.ass_due_date_day.focus()
				return false;
			}
		}
	}

	if (frm.start_date && frm.end_date){
		if (frm.start_date[1].checked == true){
			if (!wbUtilsValidateDate('document.frmXml.start',eval('wb_msg_' + lang + '_start_datetime'),'','ymdhm'))
				return false
		}

		if (frm.end_date[1].checked == true){
			if (!wbUtilsValidateDate('document.frmXml.end',eval('wb_msg_' + lang + '_end_datetime'),'','ymdhm'))
				return false
		}

		//Date Validation
		if (frm.start_date[1].checked == true && frm.end_date[1].checked == true){
			start_time = frm.start_yy.value + frm.start_mm.value + frm.start_dd.value + frm.start_hour.value + frm.start_min.value
			end_time = frm.end_yy.value + frm.end_mm.value + frm.end_dd.value + frm.end_hour.value + frm.end_min.value

			if (Number(start_time) >= Number(end_time)){
				Dialog.alert(eval('wb_msg_' + lang + '_start_end_time'))
				return false
			}
		}

		if (frm.start_date[0].checked == true && frm.end_date[1].checked == true){
			start_time = frm.cur_dt.value
			start_time = start_time.substring(0, start_time.length - 2)
			end_time = frm.end_yy.value + frm.end_mm.value + frm.end_dd.value + frm.end_hour.value + frm.end_min.value
			if (Number(start_time) >= Number(end_time)){
				Dialog.alert(eval('wb_msg_' + lang + '_start_end_time'))
				return false
			}
		}
	}
	return true;
}

function wbModuleUpdExec2(frm, lang){
	if (_wbModuleValidateFrm2(frm, lang)) {
		if (frm.ass_due_datetime && frm.ass_due_date_day){
			frm.ass_due_datetime.value = '';
			if (frm.ass_due_date_rad[0].checked) {
				frm.ass_due_date_day.value = '0';
			}
			else
			if (frm.ass_due_date_rad[1].checked) {
				frm.ass_due_datetime.value = frm.due_yy.value + "-" + frm.due_mm.value + "-" + frm.due_dd.value + " " + frm.due_hour.value + ":" + frm.due_min.value + ":00";
				frm.ass_due_date_day.value = '0';
			}
		}

		if (frm.start_date) {
			if (frm.start_date[0].checked) {
				frm.mod_eff_start_datetime.value = "IMMEDIATE";
			} else {
				frm.mod_eff_start_datetime.value = frm.start_yy.value + "-" + frm.start_mm.value + "-" +frm.start_dd.value + " " + frm.start_hour.value + ":" +frm.start_min.value + ":00";
			}
		} else {
			frm.mod_eff_start_datetime.value = "IMMEDIATE";
		}

		if (frm.end_date) {
			if (frm.end_date[0].checked) {
				frm.mod_eff_end_datetime.value = "UNLIMITED";
			} else {
				frm.mod_eff_end_datetime.value = frm.end_yy.value + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " " + frm.end_hour.value + ":" + frm.end_min.value + ":00";
			}
		} else {
			frm.mod_eff_end_datetime.value = "UNLIMITED";
		}
		
		frm.url_success.value = this.view_info_url(frm.mod_id.value);
		frm.url_failure.value = this.view_info_url(frm.mod_id.value);
		frm.method = "post"
		frm.action = wb_utils_servlet_url+"?isExcludes=true"
		frm.submit()
	}
}

function wbModuleStartPrev(mod_type, mod_id, cos_id, tkh_id, not_popup, usr_id, is_start_test, is_sso, isFullScreen, mod_end_time,itm_content_end_date) {
	if (tkh_id == null) {
		tkh_id=0
	}
	//检查在线学学习内容的时间是否已过
	if(wbModeEnd(mod_end_time,itm_content_end_date)){
		return;
	}
	

	var isFull = true;
	if (isFullScreen !== undefined) {
		isFull = isFullScreen;
	}
	
	var str_feature = _wbModuleGetContentWindowSize(mod_type,isFull)
	url = wb_utils_invoke_servlet(
			'cmd','check_tst_test',
			'mod_id',mod_id,
			'mod_type',mod_type,
			'cos_id',cos_id,
			'tkh_id', tkh_id,
			'is_start_test', is_start_test,
			'is_sso', is_sso,
			'url_failure','../htm/close_window.htm',
			'stylesheet','check_test_player.xsl'
		  )
    if (not_popup) {
        self.location.href = url;
    } else {
		check_player = wbUtilsOpenWin(url, 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id, false, str_feature);
		check_player.focus();
    }
}

function getModuleImage(moduleImage_path,type){
	//var moduleImage_path = "/wb_image/";
	var img = "";
	switch(type){
		case 'ROOT' :     img = "root.gif"          ;break;
		case 'ADO' :      img = "sico_ado.gif"      ;break;
		case 'AICC_AU' :  img = "sico_aicc_au.gif"  ;break;
		case 'ASS' :      img = "sico_ass.gif"      ;break;
		case 'CHT' :      img = "sico_cht.gif"      ;break;
		case 'DXT' :      img = "sico_dxt.gif"      ;break;
		case 'EAS' :      img = "sico_eas.gif"      ;break;
		case 'EXC' :      img = "sico_exc.gif"      ;break;
		case 'EXM' :      img = "sico_exm.gif"      ;break;
		case 'EXP' :      img = "sico_exp.gif"      ;break;
		case 'FAQ' :      img = "sico_faq.gif"      ;break;
		case 'FIG' :      img = "sico_fig.gif"      ;break;
		case 'FOR' :      img = "sico_for.gif"      ;break;
		case 'FWK' :      img = "sico_fwk.gif"      ;break;
		case 'GAG' :      img = "sico_gag.gif"      ;break;
		case 'GLO' :      img = "sico_glo.gif"      ;break;
		case 'GRP' :      img = "sico_grp.gif"      ;break;
		case 'LCT' :      img = "sico_lct.gif"      ;break;
		case 'NETG_COK' : img = "sico_netg_cok.gif" ;break;
		case 'ORI' :      img = "sico_ori.gif"      ;break;
		case 'RDG' :      img = "sico_rdg.gif"      ;break;
		case 'REF' :      img = "sico_ref.gif"      ;break;
		case 'SCO' :      img = "sico_sco.gif"      ;break;
		case 'stx' :      img = "sico_stx.gif"      ;break;
		case 'SVY' :      img = "sico_svy.gif"      ;break;
		case 'TST' :      img = "sico_tst.gif"      ;break;
		case 'TUT' :      img = "sico_tut.gif"      ;break;
		case 'VCR' :      img = "sico_vcr.gif"      ;break;
		case 'VOD' :      img = "sico_vod.gif"      ;break;
		case 'VST' :      img = "sico_vst.gif"      ;break;							
	}
	//Dialog.alert(moduleImage_path+img);
	return moduleImage_path + img;
}	

function wbModuleGenTestURL(window_name) {
	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'stylesheet', 'gen_test_progress.xsl', 'window_name', window_name);
	return url;
}

function wbModuleSetTstStatus(mod_id, _status) {
	var cmd = '';
	if (_status == 0) {
		cmd = 'end_test';
	}
	if (_status == 1) {
		cmd = 'start_test'
	}
	document.location.href = wb_utils_invoke_disp_servlet('cmd',cmd,'module','JsonMod.exam.ExamModule','mod_id',mod_id,'url_success',document.location.href,'url_failure',document.location.href,"isExcludes", "true");
}

/***** Salem 2010-11-25********************/
var hasIframe=false;
function creatSCORM_iframe(this_aicc_sid,this_aicc_url){
var IframeObj = document.createElement("iframe");
IframeObj.id= "myScormData";
IframeObj.width =0;
IframeObj.height =0;
IframeObj.frameborder = 0;
IframeObj.src="../htm/scorm_send_adapter.htm?cmd=PutParam&my_aicc_sid="+this_aicc_sid+"&my_aicc_url="+this_aicc_url;
document.body.appendChild(IframeObj);
hasIframe=true;
}
window.onunload=function(){
if(hasIframe){
test_player.frames["adapter"].parent.incaseParentClose();
}
}

function wbModuleMgtContent(tcr_id,cur_page,page_size){
	url = wb_utils_invoke_disp_servlet('module', 'content.CosEvaluationModule', 'cmd', 'get_cos_eval_lst', 'stylesheet', 'cos_evn_form_maintain_lst.xsl', 'mod_tcr_id', tcr_id);
	if(cur_page != undefined && cur_page != '' && cur_page != 0){
		url = wb_utils_invoke_disp_servlet('module', 'content.CosEvaluationModule', 'cmd', 'get_cos_eval_lst', 'stylesheet', 'cos_evn_form_maintain_lst.xsl', 'mod_tcr_id', tcr_id,'cur_page',cur_page,'page_size',page_size);
	}
	window.location.href = url;
}

//判断在线模块学习时间是否结束
//mod_end_time: 模块在线学习结束时间
//itm_content_end_date: 课程线学习结束时间
function wbModeEnd(mod_end_time,itm_content_end_date){
	var result = false;
	var curDate = new Date;
	//判断在线模块学习时间是否结束
	if(mod_end_time !== undefined && mod_end_time !== '' &&  mod_end_time !== 'null' &&  mod_end_time !== 'NULL'){
		
		if(mod_end_time.length < 11){
		//当日期格式只有yyyy-YY-dd时
			mod_end_time = mod_end_time + ' 23:59:59';
		}
		
		mod_end_time = mod_end_time.replace(/-/g,"/");
		var end = new Date(mod_end_time );
		if(end <= curDate){
			Dialog.alert(fetchLabel('warning_end_notice'));
			result = true
		}
	}
	
	//判断课程学习时间是否结束
	if(itm_content_end_date !== undefined && itm_content_end_date !== '' &&  itm_content_end_date !== 'null' &&  itm_content_end_date !== 'NULL'){
		if(itm_content_end_date.length < 11){
		//当日期格式只有yyyy-YY-dd时
			itm_content_end_date = itm_content_end_date + ' 23:59:59';
		}
		itm_content_end_date = itm_content_end_date.replace(/-/g,"/");
		var end = new Date(itm_content_end_date );
		if(end <= curDate){
			Dialog.alert(fetchLabel('warning_end_notice'));
			result = true
		}
	}
	return result;
}

