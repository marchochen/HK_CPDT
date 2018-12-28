
// ------------------ wizBank Course object ------------------- 
// Convention:
//   public functions : use "wbCourse" prefix 
//   private functions: use "_wbCourse" prefix
// ------------------------------------------------------------ 

/* constructor */
function wbCourse(){
	//v3
	this.edit_cos = wbCourseEditCourse
	this.edit_cos_url = wbCourseEditCourseURL
	this.cos_wizard = wbCourseWizard
	this.export_aicc = wbCourseExportAICCCourse

	// for ins module type = 'ASS'
	this.ins_mod_ins_ass_file_desc_prep = wbCourseInsModuleInsAssignmentFileDescPrep
	this.ind_mod_ins_ass_file_des_exec = wbCourseInsModuleInsAssignmentFileDescExec
	
	this.start = wbCourseStart          // for learner
	this.ins_mod_select_url = wbCourseInsModuleSelectURL
	this.ins_mod_test_prep = wbCourseInsModuleTestPrep
	this.ins_mod_test_prep_step_2 = wbCourseInsModuleTestPrepStep2
	this.ins_mod_prep = wbCourseInsModulePrep
	this.ins_mod_prep_url = wbCourseInsModulePrepUrl
	this.ins_mod_exec = wbCourseInsModuleExec
	this.del_mod_url = wbCourseDelModuleURL
	this.ins_res_netg_exec = wbCourseInsResNetgExec
	this.upd_res_netg = wbCourseUpdResNetg
	this.upd_res_netg_exec = wbCourseUpdResNetgExec
	this.ins_res_presentation_exec = wbCourseInsResPresentationExec

	// for instructor
	this.view_info_url = wbCourseViewInformationURL
	
	this.course_designate = wbCourseDesignate
	
	this.check = _wbCourseValidateFrm
}

/* private functions */
function _wbCourseCheckAICCFile(frm, lang){
	if(_wbCourseCheckFileEmpty(frm.aicc_crs, 'crs', lang) && _wbCourseCheckAICCExtension(frm.aicc_crs, 'crs', lang)){
		frm.aicc_crs_filename.value = _wbCourseGetAICCFilename(frm.aicc_crs)
	}else{
		return false;
	}

	if(_wbCourseCheckFileEmpty(frm.aicc_cst, 'cst', lang) && _wbCourseCheckAICCExtension(frm.aicc_cst, 'cst', lang)){
		frm.aicc_cst_filename.value = _wbCourseGetAICCFilename(frm.aicc_cst);
	}else{
		return false;
	}

	if(_wbCourseCheckFileEmpty(frm.aicc_des, 'des', lang) && _wbCourseCheckAICCExtension(frm.aicc_des, 'des', lang)){
		frm.aicc_des_filename.value = _wbCourseGetAICCFilename(frm.aicc_des);
	}else{
		return false;
	}

	if(_wbCourseCheckFileEmpty(frm.aicc_au, 'au', lang) && _wbCourseCheckAICCExtension(frm.aicc_au, 'au', lang)){
		frm.aicc_au_filename.value = _wbCourseGetAICCFilename(frm.aicc_au);
	}else{
		return false;
	}

	if(frm.aicc_ort.value != ''){
		if(frm.aicc_ort.value != '' && _wbCourseCheckAICCExtension(frm.aicc_ort, 'ort', lang)){
			frm.aicc_ort_filename.value = _wbCourseGetAICCFilename(frm.aicc_ort);
		}else{
			return false;
		}
	}else{
		return true;
	}

	// check whether all file names are the same or not
	if(aicc_filename(frm, lang)){
		// do nothing
		}
	else{
		return false;
	}
	return true
}

function _wbCourseCheckNETGFile(frm, lang){
	if(_wbCourseCheckNETGFileEmpty(frm.netg_cdf, 'cdf', lang) && _wbCourseCheckNETGExtension(frm.netg_cdf, 'cdf', lang)){
		frm.netg_cdf_filename.value = _wbCourseGetNETGFilename(frm.netg_cdf)
	}else{
		return false;
	}
	return true;
}

function _wbCourseCheckAICCExtension(obj, ext, lang){
	req_ext = '.' + ext
	req_ext_length = req_ext.length
	file_str = obj.value
	file_str_length = obj.value.length
	file_ext = file_str.substring((file_str_length - req_ext_length), file_str_length)
	file_ext = file_ext.toLowerCase()
	if(file_ext != req_ext){
		Dialog.alert(eval('wb_msg_' + lang + '_aicc_file_not_support') + req_ext)
		return false;
	}else{
		return true;
	}
}

function _wbCourseCheckNETGExtension(obj, ext, lang){
	req_ext = '.' + ext

	req_ext_length = req_ext.length
	file_str = obj.value
	file_str_length = obj.value.length
	file_ext = file_str.substring((file_str_length - req_ext_length), file_str_length)
	file_ext = file_ext.toLowerCase()
	if(file_ext != req_ext){
		Dialog.alert(eval('wb_msg_' + lang + '_netg_file_not_support'))
		return false;
	}else{
		return true;
	}
}

function _wbCourseGetAICCFilename(obj){
	file_str = obj.value

	file_str_length = obj.value.length
	file_str_start = file_str.lastIndexOf('\\')
	filename = file_str.substring(++file_str_start, file_str_length)
	return filename;
}

function _wbCourseGetNETGFilename(obj){
	file_str = obj.value

	file_str_length = obj.value.length
	file_str_start = file_str.lastIndexOf('\\')
	filename = file_str.substring(++file_str_start, file_str_length)
	return filename;
}

function _wbCourseCheckFileEmpty(obj, ext, lang){
	if(obj.value == ''){
		Dialog.alert(eval('wb_msg_' + lang + '_upload_file') + ' (' + ext + ')')

		obj.focus()
		return false
	}else{
		return true;
	}
}

function _wbCourseCheckNETGFileEmpty(obj, ext, lang){
	if(obj.value == ''){
		Dialog.alert(eval('wb_msg_' + lang + '_netg_file_not_input'))

		obj.focus()
		return false
	}else{
		return true;
	}
}

function _wbCourseValidateInsFrm(frm, lang){
	if(!gen_validate_empty_field(frm.course_nm, eval('wb_msg_' + lang + '_course_name'), lang)){
		frm.course_nm.focus()
		return false
	}

	if(frm.course_desc.value.length > 500){
		Dialog.alert(eval('wb_msg_' + lang + '_desc_too_long'))
		frm.course_desc.focus()
		return false
	}

	//Date Validation
	if(frm.start_date[1].checked == true && frm.end_date[1].checked == true){
		start_time = frm.start_yy.value + frm.start_mm.value + frm.start_dd.value + frm.start_hour.value + frm.start_min.value
		end_time = frm.end_yy.value + frm.end_mm.value + frm.end_dd.value + frm.end_hour.value + frm.end_min.value
		if(Number(start_time) >= Number(end_time)){
			Dialog.alert(eval('wb_msg_' + lang + '_start_end_time'))
			return false
		}
	}

	if(frm.start_date[0].checked == true && frm.end_date[1].checked == true){
		start_time = frm.cur_dt.value
		start_time = start_time.substring(0, start_time.length - 2)
		end_time = frm.end_yy.value + frm.end_mm.value + frm.end_dd.value + frm.end_hour.value + frm.end_min.value
		if(Number(start_time) >= Number(end_time)){
			Dialog.alert(eval('wb_msg_' + lang + '_start_end_time'))
			return false
		}
	}

	if(frm.start_date[1].checked == true){
		if(!wbUtilsValidateDate('document.frmXml.start', eval('wb_msg_' + lang + '_start_datetime'), '', 'ymdhm')) return false
	}

	if(frm.end_date[1].checked == true){
		if(!wbUtilsValidateDate('document.frmXml.end', eval('wb_msg_' + lang + '_end_datetime'), '', 'ymdhm')) return false
	}
	return true
}

function _wbCourseValidateFrm(frm, lang){
	var _debug_msg = true;
	//mod_title
	if(frm.mod_title){
		frm.mod_title.value = wbUtilsTrimString(frm.mod_title.value);
		if(!gen_validate_empty_field(frm.mod_title, eval('wb_msg_' + lang + '_title'), lang)){
			frm.mod_title.focus()
			return false;
		}
		if(getChars(frm.mod_title.value)>80){
			Dialog.alert(eval('wb_msg_' + lang + '_title_not_longer'))
			return false;
		}
	}

	//moe_upload_filename
	if(frm.rdo_src_type_zipfile) {
		if(frm.rdo_src_type_zipfile.checked){
			if(!wb_utils_check_chinese_char(frm.mod_default_page.value))
			return false;
		}
	}
	//mod_desc
	if(frm.mod_desc){
		frm.mod_desc.value = wbUtilsTrimString(frm.mod_desc.value);
		if(getChars(frm.mod_desc.value) > 400){
			Dialog.alert(eval('wb_msg_usr_desc_too_long'));
			frm.mod_desc.focus();
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
	if(frm.mod_vod_img){
		var mediaFilename = frm.mod_vod_img.value.toLowerCase()
		if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
			Dialog.alert(wb_msg_img_type_limit);
			frm.mod_vod_img.focus();
			return false;
		}
	}
	// mod_instr
	if (frm.mod_instr) {
		frm.mod_instr.value = wbUtilsTrimString(frm.mod_instr.value);
		if(getChars(frm.mod_instr.value) > 2000){
			Dialog.alert(eval('wb_msg_' + lang + '_inst_too_long'))
			frm.mod_instr.focus()
			return false
		}
	}

	//ass_submission		
	if(frm.ass_submission){
		frm.ass_submission.value = wbUtilsTrimString(frm.ass_submission.value);
		if(frm.ass_submission.value.length > 2000){
			Dialog.alert(eval('wb_msg_' + lang + '_ass_submission_too_long'))
			frm.ass_submission.focus()
			return false
		}
	}

	//max_usr_attempt_unlimited_num
	if(frm.max_usr_attempt_unlimited_num){
		frm.max_usr_attempt_unlimited_num.value = wbUtilsTrimString(frm.max_usr_attempt_unlimited_num.value);
		if((frm.mod_type.value == 'TST' || frm.mod_type.value == 'DXT') && getRadioValueByName("max_usr_attempt_rad") == 1){
			if(!gen_validate_positive_integer(frm.max_usr_attempt_unlimited_num, eval('wb_msg_' + lang + '_max_usr_attempt'), lang)){
				frm.max_usr_attempt_unlimited_num.focus()
				return false;
			}
		}
	}

	//Normal Case (with Src)
	if(frm.mod_type.value == 'VOD' || frm.mod_type.value == 'LCT' || frm.mod_type.value == 'TUT' || frm.mod_type.value == 'RDG' || frm.mod_type.value == 'GAG'){
		if(frm.mod_type.value == 'LCT' || frm.mod_type.value == 'TUT' || frm.mod_type.value == 'RDG'){
			if(frm.mod_type.value == 'RDG'){
				if(check_src_file(frm)){
					Dialog.alert(fetchLabel("label_core_training_management_360"));
					return false;
				}
				if(check_src_url(frm)){
					Dialog.alert(fetchLabel("label_core_training_management_361"));
					return false;
				}
			}
			if(check_src_zipfile(frm) || check_src_wizpack(frm) || check_src_url(frm) || check_src_pick_res(frm) || check_src_file(frm)){
				Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'))
				return false
			}
		}else{
			if(check_src_zipfile(frm) || check_src_pick_res(frm) || check_src_file(frm)){
				Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'))
				return false
			}
			if( check_src_url(frm)){
				Dialog.alert(eval('wb_msg_' + lang + '_pick_src_url'))
				return false
			}
			if(check_src_online(frm)) {
				Dialog.alert(eval('wb_msg_src_not_valid'))
				return false
			}
		}
		if(frm.mod_type.value == 'VOD'){
			if (frm.src_type[src_type_file_id].checked == true){
				file = frm.mod_file.value;
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
		if (frm.mod_type.value != 'VOD' && frm.mod_type.value != 'RDG') {
			if (frm.src_type[src_type_zipfile_id].checked == true) {
				file = frm.mod_zipfilename.value
				if(file.substring(file.lastIndexOf('.') + 1).toLowerCase() != 'zip'){
					Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_zipfile'))
					return false
				}
			}
			if(!isNaN(src_type_wizpack_id) && frm.src_type[src_type_wizpack_id].checked == true || frm.source_type.value == 'WIZPACK'){
				if(frm.mod_wizpack.value != ''){
					file = frm.mod_wizpack.value
					if(file.substring(file.length - 11).toLowerCase() != 'wizpack.zip'){
						Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_wizpack'))
						return false
					}
				}

				template ="tst_player1.xsl" //frm.tpl_name[frm.tpl_name.selectedIndex].text
				if(template.indexOf('wizPack') == -1){
					Dialog.alert(eval('wb_msg_' + lang + '_select_wizpack_template'))
					return false
				}
			}else if(frm.mod_type.value != 'GAG'){
				template ="tst_player1.xsl"// frm.tpl_name[frm.tpl_name.selectedIndex].text
				if(template.indexOf('wizPack') != -1){
					Dialog.alert(eval('wb_msg_' + lang + '_select_another_template'))
					return false
				}
			}
		}
	}

	//AICC_AU
	if(frm.mod_type.value == 'AICC_AU'){
		if(!isNaN(src_type_pick_aicc_res_id) && frm.src_type[src_type_pick_aicc_res_id].checked == true && frm.copy_media_from.value == ''){
			Dialog.alert(eval('wb_msg_' + lang + '_pick_resource'))
			return false
		}else if(!isNaN(src_type_aicc_id) && frm.src_type[src_type_aicc_id].checked == true && !_wbCourseCheckAICCFile(frm, lang)){
			return false;
		}else {
			if (!isNaN(src_type_aicc_zip_id) && frm.src_type[src_type_aicc_zip_id].checked == true 
					&& (!_wbCourseCheckFileEmpty(frm.aicc_zip,'zip',lang)
							|| !_wbCourseCheckAICCExtension(frm.aicc_zip, 'zip', lang))) {
					return false;
			}
		}
		if(!isNaN(src_type_pick_aicc_res_id)){
			if(frm.src_type[src_type_pick_aicc_res_id].checked == true){
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

	//NETG_COK
	if(frm.mod_type.value == 'NETG_COK'){
		if(!_wbCourseCheckNETGFile(frm, lang)){
			return false;
		}
	}

	if(frm.mod_type.value == 'TST' || frm.mod_type.value == 'DXT' || frm.mod_type.value == 'EXC' || frm.mod_type.value == 'LCT' || frm.mod_type.value == 'TUT' || frm.mod_type.value == 'RDG' || frm.mod_type.value == 'GAG'){
		if(frm.mod_type.value == 'TST' || frm.mod_type.value == 'DXT'){
			lab_name = eval('wb_msg_' + lang + '_time_limit')
		}else if(frm.mod_subtype.value == 'LCT'){
			lab_name = eval('wb_msg_' + lang + '_duration')
		}else{
			lab_name = eval('wb_msg_' + lang + '_suggested_time')
		}
		if(frm.mod_duration && !gen_validate_float(frm.mod_duration, lab_name, lang)){
			frm.mod_duration.focus()
			return false
		}
	}

	//ASS
	if(frm.mod_type.value == 'ASS'){
		if(check_src_zipfile(frm)){
			if(''==frm.mod_zipfilename.value || ''==frm.mod_default_page.value ){
				Dialog.alert(fetchLabel("label_core_training_management_371"))
				frm.mod_zipfilename.focus();
			}
			return false
		}else if(check_src_zipfile(frm) || check_src_file(frm) || check_src_url(frm) || (frm.mod_instr.value == '' && frm.src_type[src_type_ass_inst_id].checked == true)){
			if(frm.src_type[src_type_ass_inst_id].checked == true){
				Dialog.alert(fetchLabel("label_core_training_management_373"));
				frm.mod_instr.focus();
			}
			else if (frm.src_type[src_type_url_id].checked == true){ 
				Dialog.alert(fetchLabel("label_core_training_management_374"));
				frm.mod_url.focus();
			}
			else if (frm.src_type[src_type_file_id].checked == true){ 
				Dialog.alert(fetchLabel("label_core_training_management_375"));
				frm.mod_file.focus();
			}
			else if (frm.src_type[src_type_zipfile_id].checked == true){ 
				Dialog.alert(fetchLabel("label_core_training_management_376"));
				frm.mod_zipfilename.focus();
			}
			return false
		}

		if(getChars(frm.mod_instr.value) > 2000){
			Dialog.alert(eval('wb_msg_' + lang + '_inst_too_long1'))
			return false
		}

		if(frm.src_type[src_type_zipfile_id].checked == true){
			file = frm.mod_zipfilename.value
			if(file.substring(file.lastIndexOf('.') + 1).toLowerCase() != 'zip'){
				Dialog.alert(eval('wb_msg_' + lang + '_upload_valid_zipfile'))
				return false
			}
		}

		if(frm.max_upload_file && frm.max_upload_file[1].checked == true){
			frm.ass_max_upload.value = wbUtilsTrimString(frm.ass_max_upload.value);
			if(!gen_validate_integer(frm.ass_max_upload, eval('wb_msg_' + lang + '_ass_max_upload'), lang)){
				frm.ass_max_upload.focus()
				return false
			}
		}

		if(frm.ass_due_datetime && frm.ass_due_date_day){
			
			if(frm.ass_due_date_rad[1].checked == true){
				if(!wbUtilsValidateDate('document.frmXml.due', eval('wb_msg_' + lang + '_ass_due_datetime'), '', 'ymdhm')){
					return false
				}
				
			}else if(frm.ass_due_date_rad[2].checked == true){
				frm.ass_due_date_day.value = wbUtilsTrimString(frm.ass_due_date_day.value);
				if(!gen_validate_positive_integer(frm.ass_due_date_day, eval('wb_msg_' + lang + '_ass_due_datetime'), lang)){
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
		if(frm.grading[1].checked == true){
			frm.mod_max_score.value = wbUtilsTrimString(frm.mod_max_score.value);
			if(!wbUtilsValidateEmptyField(frm.mod_max_score, eval('wb_msg_' + lang + '_max_score'))){
				frm.mod_max_score.focus()
				return false
			}
			if(!wbUtilsValidateNonZeroPositiveInteger(frm.mod_max_score, eval('wb_msg_' + lang + '_max_score'))){
				frm.mod_max_score.focus()
				return false
			}		
			if(frm.mod_max_score.value.length>4){
				Dialog.alert(wb_msg_gb_max_score + wb_msg_usr_enter_smaller_number_1);
				frm.mod_max_score.focus()
				return false
			}
			frm.mod_pass_score.value = wbUtilsTrimString(frm.mod_pass_score.value);
			
			if(!gen_validate_pencentage(frm.mod_pass_score, fetchLabel('label_core_training_management_377'), lang)){
				frm.mod_pass_score.focus()
				return false
			}
			
			if(parseFloat(frm.mod_max_score.value) < parseFloat(frm.mod_pass_score.value)){
					Dialog.alert(eval('wb_msg_'+lang+'_max_pass_score'));
					frm.mod_max_score.focus();
					return false;
			}
				
		}
	}

	if(frm.mod_type.value != 'ASS' && frm.mod_type.value != 'EAS'){
		if(frm.mod_type.value != 'CHT' && frm.mod_type.value != 'VCR' && frm.mod_type.value != 'VOD' && frm.mod_type.value != 'FOR' && frm.mod_type.value != 'FAQ' && frm.mod_type.value != 'STX' && frm.mod_type.value != 'AICC_AU' && frm.mod_type.value != 'REF' && frm.mod_type.value != 'GLO' && frm.mod_type.value != 'SVY' && frm.mod_type.value != 'TNA' && frm.mod_type.value != 'EVN' && frm.mod_type.value != 'NETG_COK'){
			if(frm.mod_type.value == 'TST' || frm.mod_type.value == 'DXT') lab_name = eval('wb_msg_' + lang + '_time_limit')
			else if (frm.mod_subtype.value == 'LCT' || frm.mod_subtype.value == 'VDO' || frm.mod_subtype.value == 'ADO') lab_name = eval('wb_msg_' + lang + '_duration')
			else lab_name = eval('wb_msg_' + lang + '_suggested_time')
			if(frm.mod_duration) {
				frm.mod_duration.value = wbUtilsTrimString(frm.mod_duration.value);
				if (!gen_validate_float(frm.mod_duration, lab_name, lang)) {
					frm.mod_duration.focus()
					return false
				}
			}
		}
	}

	if(frm.mod_type.value == 'VCR'){
		frm.mod_src_link.value = wbUtilsTrimString(frm.mod_src_link.value);
		if(!gen_validate_empty_field(frm.mod_src_link, eval('wb_msg_' + lang + '_url'), lang)){
			frm.mod_src_link.focus()
			return false;
		}
	}

	if(frm.mod_type.value == 'VST' || frm.mod_type.value == 'GAG' || frm.mod_type.value == 'EXM' || frm.mod_type.value == 'ORI'){
		if(!wbUtilsValidateDate('document.frmXml.evt', eval('wb_msg_' + lang + '_evt_datetime'), '', 'ymdhm')) return false

		if(frm.evt_venue.value.length > 150){
			Dialog.alert(eval('wb_msg_' + lang + '_evt_venue_too_long'))

			frm.evt_venue.focus()
			return false
		}
	}

	if(frm.mod_type.value == 'TST' || frm.mod_type.value == 'DXT' || frm.mod_type.value == 'STX'){
		if(!wbUtilsValidateEmptyField(frm.mod_pass_score, fetchLabel('label_core_training_management_465'))){
			frm.mod_pass_score.focus()
			return false
		}
		frm.mod_pass_score.value = wbUtilsTrimString(frm.mod_pass_score.value);
		if(!gen_validate_pencentage(frm.mod_pass_score, eval('wb_msg_' + lang + '_pass_score'), lang)){
			frm.mod_pass_score.focus()
			return false
		}
		frm.mod_pass_score.value = wbUtilsTrimString(frm.mod_pass_score.value);	
		
		if(!wbUtilsValidatePositiveInteger(frm.mod_pass_score, fetchLabel('label_core_training_management_465'))){
			frm.mod_pass_score.focus();
			return false;
		}	
	}

	if(frm.mod_subtype.value == 'VCR' || frm.mod_subtype.value == 'FAQ' || frm.mod_subtype.value == 'FOR' || frm.mod_subtype.value == 'CHT' || frm.mod_subtype.value == 'ASS' || frm.mod_subtype.value == 'EAS'){
		if(frm.mod_instructor) {
			frm.mod_instructor.value = wbUtilsTrimString(frm.mod_instructor.value);
			if (!gen_validate_empty_field(frm.mod_instructor, eval('wb_msg_' + lang + '_instructor'), lang)) {
				frm.mod_instructor.focus()
				return false;
			}
		}
	}

	if(frm.start_date && frm.end_date){
		if(frm.start_date[1].checked == true){
			if(!wbUtilsValidateDate('document.frmXml.start', eval('wb_msg_' + lang + '_start_datetime'), '', 'ymdhm')) return false
		}

		if(frm.end_date[1].checked == true){
			if(!wbUtilsValidateDate('document.frmXml.end', eval('wb_msg_' + lang + '_end_datetime'), '', 'ymdhm')) return false
		}

		//Date Validation
		if(frm.start_date[1].checked == true && frm.end_date[1].checked == true){
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
		if(frm.start_date[0].checked == true && frm.end_date[1].checked == true){
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
	
	if (frm.mod_managed_ind) {
		if(frm.mod_managed_ind_chk.checked) {
			frm.mod_managed_ind.value = '1';
		} else {
			frm.mod_managed_ind.value = '0';
		}
	}
	return true;
}

//计算码率
function checkFilekbps(file,time,lang){
	var tip1,tip2;
	switch (lang){
		case 'gb':
            tip1 = '播放该视频所需的下载网速约为';
            tip2 = '容易导致播放卡顿，强烈建议压缩视频体积大小。';
            break;
		case 'ch':
			tip1 = '無法上傳。播放該視頻所需的下載網速約為';
			tip2 = '容易導致播放卡頓，請壓縮視頻大小';
			break
		case 'en':
            tip1 = 'Unable to upload.They needed to play the video download speeds of about';
            tip2 = 'Easy to cause caton, compressed video size, please.';
			break

	}
	$("input[name=mod_required_time]").val(time);
	if (file != undefined && file != null){
        if (checkkbps(file.size,time,tip1,tip2)){
        	return true;
		}else{
        	return false;
		}
	}else{
		if ($("#fileSize")[0].title != null && $("#fileSize")[0].title != 0){
            if (checkkbps($("#fileSize")[0].title,time,tip1,tip2)){
                return true;
            }else{
                return false;
            }
		}else{
            $("#vod_tip").html("");
		}
	}
}
function checkkbps(sizeKb,time,tip1,tip2){
    var reg = /^[0-9]+([.][0-9]{1}){0,1}$/;
    if (time != null && time != "" && reg.test(time) && parseFloat(time) != 0){
        var size = sizeKb/(1024*1024);
        var duration = time * 60;
        var kbps = (size/duration)*1024;
        if (kbps > 200) {
            $("#vod_tip").html(tip1 + kbps.toFixed(1) + "KB/s，" + tip2);
			/*Dialog.alert("无法上传。播放该视频所需的下载网速约为"+arp.toFixed(1)+"KB/s，容易导致播放卡顿，请压缩视频大小。");*/
            return false;
        }else{
            $("#vod_tip").html("");
            return true;
        }
    }else{
        $("#vod_tip").html("");
    }
}

var i = 0
function cleanTotalTime(frm,type){
	if (type == 'add'){
        if (i > 0){
            frm.mod_required_time_hidden.value = '';
            $("#vod_tip").html("");
        }
        i++;
	}else if (type == 'update'){
        frm.mod_required_time_hidden.value = '';
        $("#vod_tip").html("");
	}
}

/* public functions */
function check_src_pick_res(frm){
	if(!isNaN(src_type_pick_res_id) && frm.src_type[src_type_pick_res_id].checked == true && frm.copy_media_from.value == ''){
		return true;
	}else{
		return false;
	}
}

function check_src_file(frm){
	if(!isNaN(src_type_file_id) && frm.src_type[src_type_file_id].checked == true && frm.mod_file.value == ''){
		return true;
	}else{
		return false;
	}
}

function check_src_zipfile(frm){
	if(!isNaN(src_type_zipfile_id) && frm.src_type[src_type_zipfile_id].checked == true && (frm.mod_default_page.value == '' || frm.mod_zipfilename.value == '')){
		return true;
	}else{
		return false;
	}
}

function check_src_url(frm){
	 //正则表达式 格式：http://host:port/.../xxx.mp4;
	 //var reg = /^http:\/\/(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\.(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\.(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\.(\d|[1-9]\d|1\d{2}|2[0-5][0-5])\:([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])\/[.*\/]*.*\.mp4$/;
	 var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?\.mp4$/;
	 if(!isNaN(src_type_url_id) && frm.src_type[src_type_url_id].checked == true && (frm.mod_url.value == '' || 
			 !reg.test(frm.mod_url.value))){
			 //frm.mod_url.value == "http://")){
		 return true;
	}else{
		return false;
	}
}

function check_src_wizpack(frm){
	if(src_type_wizpack_id && frm.src_type[src_type_wizpack_id].checked == true && frm.mod_wizpack.value == ''){
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

function check_src_online(frm){
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

function wbCourseExportAICCCourse(frm){
	frm.cmd.value = 'export_cos';

	frm.url_failure.value = "javascript:window.close()";
	frm.submit();
}

function wbCourseEditCourse(id, cosType, isCreateRun, contentDef, hasMod){
	if (cosType == 'CLASSROOM' && isCreateRun == 'false' && contentDef == 'PARENT' && hasMod == 'false') {
		Dialog.alert(wb_msg_no_learning_modules);
		//document.location.reload();
	} else {
		url_failure = '../htm/close_window.htm'
		url_redirect = wb_utils_invoke_servlet('cmd', 'get_cos', 'res_id', id, 'dpo_view', 'IST_READ', 'stylesheet', 'cos_wizard_lesson.xsl')
		url = wb_utils_invoke_servlet('cmd', 'get_cos', 'course_id', id, 'cos_type', cosType, 'stylesheet', 'course_authoring_ns.xsl',"isExcludes", "true")
	
		applet_width = 1100;
		applet_height = 772;
	
		str_feature = 'toolbar=' + 'no' + ',width=' + applet_width + ',height=' + applet_height + ',scrollbars=' + 'auto' + ',resizable=' + 'NO' + ',screenX=' + '0' + ',screenY=' + '10' + ',left=' + '0' + ',top=' + '10'
		
		wbUtilsOpenWin(url, 'course_content', false,str_feature);
//		window.location.href = url;
	}
}

function wbCourseEditCourseURL(id, is_new_cos){
	if(is_new_cos == null || is_new_cos==undefined){
		is_new_cos = false;
	}
	//url_failure = '../htm/close_window.htm'
	url = wb_utils_invoke_servlet('cmd', 'get_cos', 'course_id', id, 'stylesheet', 'course_authoring_ns.xsl', 'is_new_cos', is_new_cos)
	return url
}

function wbCourseWizard(id){
	str_feature = 'toolbar=' + 'no' + ',width=' + '777' + ',height=' + '460' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',screenX=' + '0' + ',screenY=' + '10' + ',left=' + '0' + ',top=' + '10'
	url = wb_utils_invoke_servlet('cmd', 'get_cos', 'res_id', id, 'dpo_view', 'IST_READ', 'stylesheet', 'cos_wizard_lesson.xsl')
	wbUtilsOpenWin(url, 'course_wizard', false, str_feature);
}

function wbCourseStart(id, location, tkh_id, isQR){
	url = wb_utils_invoke_ae_servlet('cmd', 'ae_get_enrol_cos', 'res_id', id, 'dpo_view', 'LRN_READ', 'stylesheet', 'lrn_tst_lst.xsl', 'nav_num_of_mod', '3', 'msg_type', 'RES')
	if(location != null){
		url += "&location=" + location
	}

	if(tkh_id != null){
		url += "&tkh_id=" + tkh_id
	}

	if(isQR != null){
		url += "&qr_ind=" + isQR
	}

	wb_utils_set_cookie('course_id', id)
	window.location.href = url;
}

function wbCourseInsModuleSelectURL(id){
	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'course_id', id, 'stylesheet', 'ins_mod_prep.xsl');
	return url;
}

function wbCourseInsModulePrep(id,tpl_subtype,subtype,cos_type){
	if(!cos_type){
		cos_type = '';
	}
	url = wb_utils_invoke_servlet('cmd', 'get_tpl', 'tpl_type', tpl_subtype, 'tpl_subtype', tpl_subtype, 'course_id', id,'cos_type',cos_type, 'dpo_view', 'IST_EDIT', 'stylesheet', 'tst_ins.xsl');
	window.location.href = url;
}

function wbCourseInsModuleTestPrep(id){
	url = wb_utils_invoke_servlet('cmd', 'get_cos', 'course_id', id, 'stylesheet', 'ins_mod_test_prep.xsl');
	self.location.href = url;
}

function wbCourseInsModuleTestPrepStep2(frm){
	if (_wbCourseValidateInsTestForm(frm)){	
		mod_type = frm.mod_type.value;
		if (frm.copy_media_from.value != ''){
			frm.cmd.value = 'get_assessment_with_tpl';
			frm.res_id.value = frm.copy_media_from.value
			frm.tpl_type.value = mod_type;
			frm.tpl_subtype.value = mod_type;
			frm.dpo_view.value = 'IST_EDIT'
			frm.stylesheet.value = 'tst_ins.xsl'
			frm.module.value = 'quebank.QueBankModule';
			frm.method = "get"
			frm.action = wb_utils_disp_servlet_url
			frm.submit();
		}else{
			url = wb_utils_invoke_servlet('cmd', 'get_tpl', 'tpl_type', mod_type, 'tpl_subtype', mod_type, 'course_id', frm.course_id.value, 'dpo_view', 'IST_EDIT', 'stylesheet', 'tst_ins.xsl');
			window.location.href = url;
		}			
	}
}

function wbCourseInsModulePrepUrl(id, tpl_subtype){
	url = wb_utils_invoke_servlet('cmd', 'get_tpl', 'tpl_type', tpl_subtype, 'tpl_subtype', tpl_subtype, 'course_id', id, 'dpo_view', 'IST_EDIT', 'stylesheet', 'tst_ins.xsl');
	return url;
}

function wbCourseInsModuleExec(frm, course_id, lang){

	frm.url_success.value = "javascript:window.location.href=wb_utils_invoke_servlet('cmd','get_mod','mod_id',wb_utils_get_cookie('mod_id'),'dpo_view','IST_READ','stylesheet','ist_view_module.xsl')"
	frm.url_failure.value = "javascript:history.back()"
	frm.course_id.value = course_id;
	
	if(_wbCourseValidateFrm(frm, lang)){
		if(frm.mod_type.value == 'VOD' || frm.mod_type.value == 'LCT' || frm.mod_type.value == 'TUT' 
			|| frm.mod_type.value == 'RDG' || frm.mod_type.value == 'GAG' || frm.mod_type.value == 'WCT' || frm.mod_type.value == 'MBL'){
			if(frm.src_type[src_type_file_id].checked == true){
				frm.mod_src_link.value = wbMediaGetFileName(frm.mod_file.value)
				frm.mod_src_type.value = 'FILE'
				frm.copy_media_from.value = ''
			}else if(frm.src_type[src_type_url_id].checked == true){
				frm.mod_src_link.value = frm.mod_url.value
				frm.mod_src_type.value = 'URL'
				frm.copy_media_from.value = ''
			}else if(!isNaN(src_type_pick_res_id) && frm.src_type[src_type_pick_res_id].checked == true){
				frm.mod_src_link.value = frm.source_content.value
				frm.mod_src_type.value = frm.source_type.value
			}else if(!isNaN(src_type_zipfile_id) && frm.src_type[src_type_zipfile_id].checked == true){
				frm.mod_src_link.value = frm.mod_default_page.value
				frm.zip_filename.value = wbMediaGetFileName(frm.mod_zipfilename.value)
				frm.mod_src_type.value = 'ZIPFILE'
				frm.copy_media_from.value = ''
			}else if(frm.src_type[src_type_video_id].checked == true){
				frm.mod_src_link.value = frm.mod_video.value
				frm.mod_src_type.value = 'ONLINEVIDEO_' + frm.video_type.options[frm.video_type.selectedIndex].value;
				frm.copy_media_from.value = ''
			}else if(frm.mod_type.value == 'LCT' || frm.mod_type.value == 'TUT' || frm.mod_type.value == 'RDG' || frm.mod_type.value == 'EXP'){
				if(frm.src_type[src_type_wizpack_id] != undefined && frm.src_type[src_type_wizpack_id].checked == true){
					frm.mod_src_link.value = wbMediaGetFileName(frm.mod_wizpack.value)
					frm.mod_src_type.value = 'WIZPACK'
					frm.copy_media_from.value = ''
				}
			}
			if(frm.mod_subtype.value != 'VOD' && frm.mod_subtype.value != 'VST' && frm.mod_subtype.value != 'GAG' 
				&& frm.mod_subtype.value != 'ORI' && frm.mod_subtype.value != 'EXM' && frm.mod_subtype.value != 'MBL'
				&& frm.mod_subtype.value != 'RDG'){
				if(frm.asHTML.checked){
					frm.annotation_html.value = "Y"
				}else{
					frm.annotation_html.value = "N"
				}
			}
		}
		
		if(frm.mod_type.value == 'VOD' && frm.mod_vod_img_link){
			frm.mod_vod_img_link.value = wbMediaGetFileName(frm.mod_vod_img.value)
		}

		if(frm.mod_type.value == 'AICC_AU'){
			frm.mod_src_type.value = 'AICC_FILES'
			if(!isNaN(src_type_pick_aicc_res_id) && frm.src_type[src_type_pick_aicc_res_id].checked == true){
				frm.mod_src_link.value = frm.source_content.value
			}
		}
		
		if(frm.mod_type.value == 'NETG_COK'){
			frm.mod_src_type.value = 'CDF'
		}

		if(frm.mod_type.value == 'ASS'){
			if(frm.src_type[src_type_file_id].checked == true){
				frm.mod_src_link.value = wbMediaGetFileName(frm.mod_file.value)
				frm.mod_src_type.value = 'FILE'
				frm.mod_instr.value = ''
			}else if(frm.src_type[src_type_zipfile_id].checked == true){
				frm.mod_src_link.value = frm.mod_default_page.value
				frm.zip_filename.value = wbMediaGetFileName(frm.mod_zipfilename.value)
				frm.mod_src_type.value = 'ZIPFILE'
				frm.mod_instr.value = ''
			}else if(frm.src_type[src_type_url_id].checked == true){
				frm.mod_src_link.value = frm.mod_url.value
				frm.mod_src_type.value = 'URL'
				frm.mod_instr.value = ''
			}else if(frm.src_type[src_type_ass_inst_id].checked == true){
				frm.mod_src_link.value = ''
			}

			if(frm.max_upload_file && frm.max_upload_file[0].checked == true) frm.ass_max_upload.value = '-1'

			if(frm.file_desc_lst.value == ''){
				for (i = 1; i <= frm.ass_max_upload.value; i++){
					frm.file_desc_lst.value += 'null:_:_:'
				}
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
			if(frm.grading[0].checked == true) frm.mod_max_score.value = -1
		}

		if(frm.mod_type.value == 'EAS'){
			if(!gen_validate_empty_field(frm.mod_desc, eval('wb_msg_' + lang + '_desc'), lang)){
				frm.mod_desc.focus()
				return false;
			}

			if(frm.grading[0].checked == true){
				frm.mod_max_score.value = -1
			}
			if(frm.grading[1].checked == true){
				if(!gen_validate_integer(frm.mod_max_score, eval('wb_msg_' + lang + '_max_score'), lang)){
					frm.mod_max_score.focus()
					return false
				}
				if(!gen_validate_pencentage(frm.mod_pass_score, eval('wb_msg_' + lang + '_pass_score'), lang)){
					frm.mod_pass_score.focus()
					return false
				}
			}
		}

		if(frm.mod_type.value == 'DXT'){
//			if(frm.dmod_logic[0].checked == true) frm.mod_logic.value = frm.dmod_logic[0].value
//			else frm.mod_logic.value = frm.dmod_logic[1].value
			frm.mod_logic.value = 'RND';
		}

		if(frm.mod_type.value == 'VST' || frm.mod_type.value == 'GAG' || frm.mod_type.value == 'EXM' || frm.mod_type.value == 'ORI') frm.evt_datetime.value = frm.evt_yy.value + "-" + frm.evt_mm.value + "-" + frm.evt_dd.value + " " + frm.evt_hour.value + ":" + frm.evt_min.value + ":00"

		if(frm.mod_type.value != 'EAS' && frm.cos_type.value != 'AUDIOVIDEO'){
			if(!frm.start_date || frm.start_date[0].checked == true) frm.mod_eff_start_datetime.value = "IMMEDIATE"
			else frm.mod_eff_start_datetime.value = frm.start_yy.value + "-" + frm.start_mm.value + "-" + frm.start_dd.value + " " + frm.start_hour.value + ":" + frm.start_min.value + ":00"
			if(!frm.end_date || frm.end_date[0].checked == true) frm.mod_eff_end_datetime.value = "UNLIMITED"
			else frm.mod_eff_end_datetime.value = frm.end_yy.value + "-" + frm.end_mm.value + "-" + frm.end_dd.value + " " + frm.end_hour.value + ":" + frm.end_min.value + ":00"
		}else{
			frm.mod_eff_start_datetime.value = "IMMEDIATE"
			frm.mod_eff_end_datetime.value = "UNLIMITED"
		}

		// for cos_struct_xml
		frm.course_timestamp.value = parent.course_timestamp;
		frm.course_struct_xml_cnt.value = parent.course_struct_xml_cnt;
		frm.course_struct_xml_1.value = parent.course_struct_xml_1;

		if(frm.mod_instructor){
			if(frm.rdo_ist){
				if(frm.rdo_ist[0].checked == true){
					frm.mod_instructor_ent_id_lst.value = mod_instructor_ent_id[frm.mod_instructor.selectedIndex];
				}
			}
		}

		if(frm.mod_type.value == 'SVY'){
			frm.max_usr_attempt.value = 1;
		}

		if(frm.mod_type.value == 'TST' || frm.mod_type.value == 'DXT'){
			if(frm.text_style){
				var style = document.getElementById("text_style_only_id");
				if(style.checked){
					frm.mod_test_style.value= "only";
				}
				else{
					frm.mod_test_style.value= "many";
				}
			}
			
			if(frm.mod_max_usr_attempt){
				if(frm.max_usr_attempt_unlimited_num.value != ''){
					frm.mod_max_usr_attempt.value = frm.max_usr_attempt_unlimited_num.value;
				}else{
					frm.mod_max_usr_attempt.value = '0';
				}
			}
			if (frm.mod_show_answer_ind) {
				if(frm.mod_show_answer_ind_chk[0].checked) {
					frm.mod_show_answer_ind.value = '1';
				} else if(frm.mod_show_answer_ind_chk[1].checked) {
					frm.mod_show_answer_ind.value = '0';
				}else {
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
		}

		frm.mod_type.value = 'MOD'
		frm.method = "post"
		frm.action = wb_utils_servlet_url + '?cmd=ins_mod';
		if (frm.mod_src_type && frm.mod_src_type.value == 'AICC_FILES') {
			if(frm.src_type && frm.src_type.length>2 && frm.src_type[src_type_aicc_zip_id].checked){
				frm.url_success.value = "javascript:parent.location.reload()";
				if(startStatusCheck(frm,lang,true)){
					frm.action = wb_utils_invoke_disp_servlet('upload_listener','true','isExcludes',true);
					frm.target = 'target_upload';
					frm.cmd.value = 'ins_mod_aicc';
					frm.module.value = 'JsonMod.courseware.CoursewareModule';
					frm.url_success.value = "javascript:window.location.href=wb_utils_invoke_servlet('cmd','get_mod','mod_id',wb_utils_get_cookie('mod_id'),'dpo_view','IST_READ','stylesheet','ist_view_module.xsl')";
				 	frm.submit();
				}
				return;
			}
		}
		if(frm.mod_src_type && (frm.mod_src_type.value == "AICC_FILES" || frm.mod_src_type.value == "WIZPACK" || frm.mod_src_type.value == "FILE" || frm.mod_src_type.value == "ZIPFILE" || frm.mod_src_type.value == "CDF")){
			wb_utils_preloading();
		}
		frm.submit();
		return true;
	}
}

function wbCourseInsResPresentationExec(frm, lang) {
	frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
	if (!gen_validate_empty_field(frm.res_title, eval('wb_msg_' + lang + '_title'), lang)) {
		return false
	}
	if (frm.res_desc) {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
	}
	frm.rename.value = 'no';
	frm.obj_id.value = getParentUrlParam('obj_id')
	frm.cmd.value = 'INS_RES';
	frm.env.value = 'wizb';
	frm.action = wb_utils_servlet_url;
	frm.method = 'post';
	frm.submit();
}

function wbCourseInsResNetgExec(frm,lang){
	if(!_wbCourseCheckNETGFile(frm, lang)){
		return false;
	}
	frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
	if (!gen_validate_empty_field(frm.res_title, eval('wb_msg_'+lang+'_title'),lang) ) {
		return false
	}
	
	if (frm.res_desc) {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
	}
	frm.rename.value = 'no';
	frm.obj_id.value = getParentUrlParam('obj_id')
	frm.cmd.value= 'INS_RES';
	frm.env.value = 'wizb';
	frm.action = wb_utils_servlet_url;
	frm.method = 'post';
	frm.submit();
}

function wbCourseUpdResNetg(res_id){
	url = wb_utils_invoke_servlet('cmd','get_res','res_id',res_id,'stylesheet','upd_res_netg.xsl')
	parent.document.getElementById("content").src = url;
}

function wbCourseUpdResNetgExec(frm,lang){
	if(frm.res_format[0].checked == true) {
		if(!_wbCourseCheckNETGFile(frm, lang)){
			return false;
		}
	}
	frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
	if (!gen_validate_empty_field(frm.res_title, eval('wb_msg_'+lang+'_title'),lang) ) {
		return false
	}
	
	if (frm.res_desc) {
		frm.res_desc.value = wbUtilsTrimString(frm.res_desc.value);
	}
	frm.rename.value = 'no';
	frm.obj_id.value = getParentUrlParam('obj_id')
	frm.cmd.value= 'UPD_RES';
	frm.env.value = 'wizb';
	frm.action = wb_utils_servlet_url;
	frm.method = 'post';
	frm.submit();
}

function wbCourseDelModuleURL(id,course_id,status,timestamp,attempted,lang,course_timestamp,jstree) {
	   if (attempted == "TRUE") {
			window.top.Dialog.alert(eval('wb_msg_' + lang + '_attempted'));
			window.parent.cancelDelete();
			return;
		}		
		else if (status == "ON") {
			window.top.Dialog.alert(eval('wb_msg_' + lang + '_change_offline'));
			window.parent.cancelDelete();
			return;
		}else if (confirm(eval('wb_msg_' + lang + '_confirm_del_mod'))) {
			//url_failure = 'javascript:window.parent.cancelDelete();window.location.href="' + gen_get_cookie('url_failure')  +'"';
			//url_success = 'javascript:window.parent.confirmDelete();window.location.href="' +  this.view_info_url(course_id) +'"' 
			url_failure = gen_get_cookie('url_failure');
			url_success = this.view_info_url(course_id); 
			url = wb_utils_invoke_servlet('cmd','del_mod','course_id',course_id,'mod_id',id,'mod_timestamp',timestamp,'url_success', url_success,'url_failure',url_failure);
			return url;
		}
		else{
			window.parent.cancelDelete();
		}

		/*var this_ = this;
		
		window.top.Dialog.confirm({text: eval('wb_msg_' + lang + '_confirm_del_mod'), callback: function (answer) {
			if(answer){
				url_failure = gen_get_cookie('url_failure');
				url_success = this_.view_info_url(course_id); 
				url = wb_utils_invoke_servlet('cmd','del_mod','course_id',course_id,'mod_id',id,'mod_timestamp',timestamp,'url_success', url_success,'url_failure',url_failure);
				
				if (url) {
					//  parent.show_frame_content(url);
					parent.delete_module(id, timestamp, course_id, course_timestamp, 1, jstree);
					get_cos_timestamp();
				}
				//return url;
			}else{
				window.parent.cancelDelete();
			}	
		}});*/
}

function wbCourseViewInformationURL(cos_id,itm_type){
	if(itm_type == null || itm_type == undefined || itm_type ==''){
		url = wb_utils_invoke_servlet('cmd', 'get_cos', 'course_id', cos_id, 'dpo_view', 'IST_READ', 'stylesheet', 'ist_view_cos_info.xsl', 'url_failure', self.location.href)
	}else{
		url = wb_utils_invoke_servlet('cmd', 'get_cos', 'course_id', cos_id, 'cos_type',itm_type,'dpo_view', 'IST_READ', 'stylesheet', 'ist_view_cos_info.xsl', 'url_failure', self.location.href);
	}
	return url;
}

function wbCourseInsModuleInsAssignmentFileDescPrep(file_no, lang){
	if(file_no == '' || Number(file_no) <= 0){
		Dialog.alert(eval('wb_msg_' + lang + '_enter_ass_max_upload_no'))
		document.frmXml.ass_max_upload.focus();
		return;
	}

	str_feature = 'toolbar=' + 'no' + ',width=' + '780' + ',height=' + '450' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes';
	url = wb_utils_invoke_servlet('cmd', 'get_prof', 'file_no', file_no, 'stylesheet', 'ass_enter_file_desc.xsl');
	gen_set_cookie('url_failure', 'javascript:window.close()');
	wbUtilsOpenWin(url, 'file_desc', false, str_feature);
}

function wbCourseInsModuleInsAssignmentFileDescExec(frm, file_no){
	file_desc_lst = ''

	for(i = 1; i <= file_no; i++){
		if(eval('frm.file_desc' + i + '.value') == '') file_desc_lst += 'null:_:_:'
		else file_desc_lst += eval('frm.file_desc' + i + '.value') + ':_:_:'
	}

	window.opener.document.frmXml.file_desc_lst.value = file_desc_lst
	window.close();
}

function _wbCourseValidateInsTestForm(frm){
	if (frm.create_test[0].checked){
		if (frm.mod_type.value == 'FAS'){
			frm.mod_type.value = 'TST'
		}else if (frm.mod_type.value == 'DAS'){
			frm.mod_type.value = 'DXT'
		}else{
			Dialog.alert(wb_msg_select_test_lrm);
			return false;
		}
	}else if (frm.create_test[1].checked){
		frm.mod_type.value = 'DXT'
	}else if (frm.create_test[2].checked){
		frm.mod_type.value = 'TST'
	}else{
		Dialog.alert(wb_msg_specify_option);
		return false;
	}
	return true;
}

function wbCourseDesignate(frm) {
	var valid = false;
	if (frm.tcr_id_lsts) {
		if (frm.tcr_id_lsts.length) {
			for (i = 0; i < frm.tcr_id_lsts.length; i++) {
				if (frm.tcr_id_lsts[i].checked == true) {
					valid = true;
				}
			}
		} else {
			if (frm.tcr_id_lsts.checked == true) {
				valid = true;
			}
		}
	}
	if (!valid) {
		Dialog.alert(frm.lab_LN049.value);
		return;
	}

	valid = false;
	if (frm.itm_id_lsts && frm.itm_id_lsts.options.length > 0) {
		for (i = 0; i < frm.itm_id_lsts.options.length; i++) {
			frm.itm_id_lsts.options[i].selected = true;
		}
		valid = true;
	}
	if (!valid) {
		Dialog.alert(frm.lab_LN050.value);
		return;
	}
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.cmd.value = "exec";
	frm.module.value = "ln.course.CourseModule";
	frm.url_failure.value = window.location.href;
	frm.url_success.value = window.location.href;
	frm.submit();
}

//获取radio选中的value
function getRadioValueByName(name){
	 var New = document.getElementsByName(name);
	 for(var i=0;i<New.length;i++){
	    if(New.item(i).checked){
	    	return New.item(i).getAttribute("value"); 
	    	break;
	    }else{
	    	continue;
	    }
	 }
}