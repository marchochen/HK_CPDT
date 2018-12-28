function wbInstructor() {
	this.int_pickup = wbInstructorIntPickup;
	this.ext_ins_upd_prep = wbInstructorExtInsUpdPrep;
	this.int_ins_upd_prep = wbInstructorIntInsUpdPrep;
	this.save_exec = wbInstructorSaveExec;
	this.del_exec = wbInstructorDelExec;
	this.srh_exec = wbInstructorSrhExec;
	this.add_course = wbInstructorAddCourse;
	this.add_view_course = wbInstructorAddViewCourse;
	this.del_course = wbInstructorDelCourse;
	this.comment = new wbinstructorcomment;
	this.view_course = wbInstructorViewCourse;
	this.recommend_instr = wbInstructorRecommend;

	this.int_search = wbInstructorSearch;
	this.int_search_poup = instrSearchPoup;

	this.inst_set_change = wbChangeInsSet;
	this.edit_lesson_ins = wbLessInstrEdit;

	this.get = wbInstructorGet;
	this.list = wbInstructorList;
}

function wbInstructorList(type) {
	self.location.href = wbInstructorListUrl(type);
}

function wbInstructorRecommend(id, recommend) {
	var url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'inst_recommend', 'url_success', self.location.href, 'iti_ent_id', id, 'iti_recommend', recommend );
	self.location.href = url;
}

function wbInstructorListUrl(type) {
	var url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'inst_list', 'stylesheet', 'instructor_list.xsl', 'iti_type_mark', type);
	return url;
}

function wbInstructorGet(ent_id, type,isExcludes) {
	self.location.href = wbInstructorGetUrl(ent_id, type,isExcludes);
}

function wbInstructorGetUrl(ent_id, type,isExcludes) {
	var stylesheet = 'instructor_ext_view.xsl';
	if (type == 'IN') {
		stylesheet = 'instructor_int_view.xsl';
	}
	var url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'view_instr', 'iti_ent_id', ent_id, 'stylesheet', stylesheet);
	if(isExcludes){
		url += '&isExcludes=true';
	}
	return url;
}

function wbinstructorcomment() {
	this.get_ins_comment_view = wbInstructorCommentView;
}

function wbInstructorSearch(frm, type) {
	/*
	 * private String iti_name; //讲师姓名 private String iti_level; //讲师级别 private
	 * String iti_gw_str; //岗位 private String iti_main_course; //主讲课程 private
	 * float iti_score_from; //评分范围 private float iti_score_to; //评分范围 private
	 * String iti_expertise_areas ; //擅长领域 private String iti_type_mark ;
	 * //EXT：外部讲师；IN：内部讲师 private String iti_training_company ; //所在机构
	 */
	var val = frm.iti_score_from.value;
	if (val.length == 0 || val.search(/^\s+$/) != -1) {
	} else {
		if (!gen_validate_float_less_than_5(frm.iti_score_from, frm.lab_iti_score.value)) {
			return;
		}
	}

	val = frm.iti_score_to.value;
	if (val.length == 0 || val.search(/^\s+$/) != -1) {
	} else {
		if (!gen_validate_float_less_than_5(frm.iti_score_to, frm.lab_iti_score.value)) {
			return;
		}
	}

	var cmd = 'inst_list';
	var stylesheet = "instructor_list.xsl";
	if (type == 'EXT') {
		url = wb_utils_invoke_disp_servlet("module", "instructor.InstructorModule", "cmd", cmd, 'stylesheet', stylesheet, 'iti_name', frm.iti_name.value, 'iti_level', frm.iti_level.value, 'iti_main_course',
				frm.iti_main_course.value, 'iti_score_from', frm.iti_score_from.value, 'iti_score_to', frm.iti_score_to.value, 'iti_expertise_areas', frm.iti_expertise_areas.value, 'iti_training_company',
				frm.iti_training_company.value, 'js_name', frm.js_name.value, 'max_select', frm.max_select.value, 'is_poup', frm.is_poup.value, 'iti_type_mark', type, 'for_time_table', frm.for_time_table.value,
				'ils_id', frm.ils_id.value);
	} else if (type == 'IN') {
		url = wb_utils_invoke_disp_servlet("module", "instructor.InstructorModule", "cmd", cmd, 'stylesheet', stylesheet, 'iti_name', frm.iti_name.value, 'iti_level', frm.iti_level.value, 'iti_gw_str',
				frm.iti_gw_str.value, 'iti_main_course', frm.iti_main_course.value, 'iti_score_from', frm.iti_score_from.value, 'iti_score_to', frm.iti_score_to.value, 'js_name', frm.js_name.value, 'max_select',
				frm.max_select.value, 'is_poup', frm.is_poup.value, 'iti_type_mark', type, 'for_time_table', frm.for_time_table.value, 'ils_id', frm.ils_id.value);
	}
	window.location.href = url;
}

function instrSearchPoup(root_ent_id, js_name, max_select, lang, type, for_time_table, ils_id) {
	stylesheet = 'instructor_list.xsl';

	url = wb_utils_invoke_disp_servlet("module", "instructor.InstructorModule", "cmd", 'inst_list', 'stylesheet', stylesheet, 'js_name', js_name, 'max_select', max_select, 'is_poup', 'true', 'iti_type_mark', type,
			'for_time_table', for_time_table, 'ils_id', ils_id);

	feature = 'toolbar=' + 'no' + ',location=' + 'no' + ',width=' + 820 + ',height=' + 500 + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',screenX=' + '100' + ',screenY=' + '100' + ',status=' + 'no';

	wbUtilsOpenWin(url, 'win', false, feature);
}

function wbInstructorIntPickup() {
	usr.search.popup_search_prep('ent_ids_lst', '0', 1, '1', '', '0', '0', '0', '1', 'usr_simple_search.xsl', '', '1', '', '', 'INSTR');
}

function wbInstructorExtInsUpdPrep(iti_ent_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'ins_upd_prep', 'iti_ent_id', iti_ent_id, 'stylesheet', 'instructor_ext_ins_upd.xsl');
	self.location.href = url;
}

function wbInstructorIntInsUpdPrep(iti_ent_id, ref_ent_id) {
	var url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'ins_upd_prep', 'ref_ent_id', ref_ent_id, 'iti_ent_id', iti_ent_id, 'stylesheet', 'instructor_int_ins_upd.xsl');
	self.location.href = url;
}

function wbInstructorSaveExec(frm,instructorType) {
	// 姓名
	if (frm.iti_name && frm.lab_iti_name)
		if (!_wbInstructorValidateEmpty(frm.iti_name, frm.lab_iti_name.value, true))
			return;

	// 性别
	if (frm.iti_gender && frm.lab_iti_gender)
		if (!_wbInstructorValidateEmpty(frm.iti_gender, frm.lab_iti_gender.value, true))
			return;

	// 出生日期
	if (frm.iti_bday && frm.lab_iti_bday) {
		if (frm.iti_bday_input_yy && frm.iti_bday_input_mm && frm.iti_bday_input_dd) {
			if (frm.iti_bday_input_yy.value != '' || frm.iti_bday_input_mm.value != '' || frm.iti_bday_input_dd.value != '') {
				if (!wbUtilsValidateDate("document." + frm.name + ".iti_bday_input", frm.lab_iti_bday.value)) {
					return;
				}
				frm.iti_bday.value = frm.iti_bday_input_yy.value + "-" + frm.iti_bday_input_mm.value + "-" + frm.iti_bday_input_dd.value + " 00:00:00.000";
				var _cur_date = new Date()
				var _d_date = new Date(frm.iti_bday_input_yy.value, Number(frm.iti_bday_input_mm.value) - 1, Number(frm.iti_bday_input_dd.value))
				var lab_bday = frm.lab_iti_bday ? frm.lab_iti_bday.value : 'Date of Birth(sys)'
				if(_d_date >= _cur_date){
					alert(wb_msg_usr_enter_vaild + lab_bday)
					frm.iti_bday_input_dd.focus();
					return;
				}
			}
		}

	}

	// 手机号码
	if (frm.iti_mobile && frm.lab_iti_mobile)
		if (!_wbInstructorValidateEmpty(frm.iti_mobile, frm.lab_iti_mobile.value, true))
			return;

	// 电子邮箱
	if (frm.iti_email && frm.lab_iti_email)
		if (_wbInstructorValidateEmpty(frm.iti_email, frm.lab_iti_email.value, true)) {
			if (!wbUtilsValidateEmail(frm.iti_email, frm.lab_iti_email.value)) {
				return;
			}
		} else {
			return;
		}
	
	//外部讲师头像
	if(document.getElementById("local_image")!= null &&document.getElementById("local_image").checked) {
		frm.default_image[1].defaultValue = '';
		if(frm.file_photo_url && frm.file_photo_url.disabled === false){
			//frm.file_photo_url.value = wbUtilsTrimString(frm.file_photo_url.value);
			var pathName = frm.file_photo_url.value;
			var file_type = pathName.substring(pathName.lastIndexOf('.') + 1).toLowerCase();
			var types = ["jpg", "gif", "png"];
			var len = types.length;
			var ret = false;
			var str = "";
			if(file_type == '')
			{
				alert(wb_msg_select_uploaded_picture);
				return ret;
			}
			for(var i=0; i<len; i++) {
				if(file_type === types[i]) {
					ret = true;
				}
				str += types[i];
				if(i !== len - 1) {
					str += ", ";
				}
			}
			if(ret === false) {
				alert(wb_file_input_not_support + str);
				return ret;
			}
		}
	}
	
	

	// 讲师级别
	if (frm.iti_level && frm.lab_iti_level)
		if (!_wbInstructorValidateEmpty(frm.iti_level, frm.lab_iti_level.value, true))
			return;

	// 讲师类型
	if (frm.iti_type && frm.lab_iti_type)
		if (!_wbInstructorValidateEmpty(frm.iti_type, frm.lab_iti_type.value, true))
			return;

	// 授课类别
	if (frm.iti_cos_type && frm.lab_iti_cos_type)
		if (!_wbInstructorValidateEmpty(frm.iti_cos_type, frm.lab_iti_cos_type.value, true))
			return;

	// 主讲课程
	if (frm.iti_main_course && frm.lab_iti_main_course)
		if (!_wbInstructorValidateTextarea(frm.iti_main_course, frm.lab_iti_main_course.value, true, 500))
			return;

	// 通讯地址
	if (frm.iti_address && frm.lab_iti_address)
		if (!_wbInstructorValidateTextarea(frm.iti_address, frm.lab_iti_address.value, false, 500))
			return;

	// 工作经历
	if (frm.iti_work_experience && frm.lab_iti_work_experience)
		if (!_wbInstructorValidateTextarea(frm.iti_work_experience, frm.lab_iti_work_experience.value, false, 500))
			return;

	// 教育经历
	if (frm.iti_education_experience && frm.lab_iti_education_experience)
		if (!_wbInstructorValidateTextarea(frm.iti_education_experience, frm.lab_iti_education_experience.value, false, 500))
			return;

	// 受训经历
	if (frm.iti_training_experience && frm.lab_iti_training_experience)
		if (!_wbInstructorValidateTextarea(frm.iti_training_experience, frm.lab_iti_training_experience.value, false, 500))
			return;

	// 讲师简介
	if (frm.iti_introduction && frm.lab_iti_introduction)
		if (!_wbInstructorValidateTextarea(frm.iti_introduction, frm.lab_iti_introduction.value, true, 500))
			return;

	// 擅长领域
	if (frm.iti_expertise_areas && frm.lab_iti_expertise_areas)
		if (!_wbInstructorValidateTextarea(frm.iti_expertise_areas, frm.lab_iti_expertise_areas.value, true, 500))
			return;
	
     if(instructorType!='EXT')
     {

 		// 擅长行业
 		if (frm.iti_good_industry && frm.lab_iti_good_industry){
 			if (!_wbInstructorValidateEmpty(frm.iti_good_industry, frm.lab_iti_good_industry.value, true)){
 				return;
 			}
 				
 			if(getChars(frm.iti_good_industry.value) > 2000)
 			{
 				alert(frm.lab_iti_good_industry.value + wb_msg_gb_too_long + ', ' + wb_msg_gb_word_limit + ' '
 						+ 2000+wb_msg_gb_characters+'(1000'+wb_msg_gb_characters_zh_cn+')');
 				frm.lab_iti_good_industry.focus();
 				return ;
 			}
 		}
 		
    	 
		// 所在机构
		if (frm.iti_training_company && frm.lab_iti_training_company)
			if (!_wbInstructorValidateEmpty(frm.iti_training_company, frm.lab_iti_training_company.value, true))
				return;
	
		// 培训机构联系人
		if (frm.iti_training_contacts && frm.lab_iti_training_contacts)
			if (!_wbInstructorValidateEmpty(frm.iti_training_contacts, frm.lab_iti_training_contacts.value, true))
				return;
	
		// 培训机构电话
		if (frm.iti_training_tel && frm.lab_iti_training_tel)
			if (!_wbInstructorValidateEmpty(frm.iti_training_tel, frm.lab_iti_training_tel.value, true))
				return;
	
		// 培训机构邮箱
		if (frm.iti_training_email && frm.lab_iti_training_email){
			var val = frm.iti_training_email.value;
			if (!(val.length == 0 || val.search(/^\s+$/) != -1)) {
				if (!wbUtilsValidateEmail(frm.iti_training_email, frm.lab_iti_training_email.value)) {
					return;
				}
			} else {
				return;
			}
		}
     }else{
  		// 擅长行业
  		if (frm.iti_good_industry && frm.lab_iti_good_industry){
  				
  			if(getChars(frm.iti_good_industry.value) > 2000)
  			{
  				alert(frm.lab_iti_good_industry.value + wb_msg_gb_too_long + ', ' + wb_msg_gb_word_limit + ' '
  						+ 2000+wb_msg_gb_characters+'(1000'+wb_msg_gb_characters_zh_cn+')');
  				frm.lab_iti_good_industry.focus();
  				return ;
  			}
  		}
		// 培训机构邮箱
		if (frm.iti_training_email && frm.lab_iti_training_email){
			var val = frm.iti_training_email.value;
			if (!(val.length == 0 || val.search(/^\s+$/) != -1)) {
				if (!wbUtilsValidateEmail(frm.iti_training_email, frm.lab_iti_training_email.value)) {
					return;
				}
			}
		}
     }

	// 处理可提供课程
	var ics_title = '';
	var ics_fee = '';
	var ics_hours = '';
	var ics_target = '';
	var ics_content = '';
	var has_cos_error = false;
	$('#instr_cos_contariner tr.instr_cos_itm').each(function() {
		if (_wbInstructorValidateEmpty($('input[name=ics_title_]', this).get(0), frm.lab_ics_title.value, true)) {
			ics_title += (ics_title === '' ? '' : '~|~') + $('input[name=ics_title_]', this).val();
		} else {
			has_cos_error = true;
			return false;
		}
		if (_wbInstructorValidateFloat($('input[name=ics_fee_]', this).get(0), frm.lab_ics_fee.value, true)) {
			ics_fee += (ics_fee === '' ? '' : '~|~') + $('input[name=ics_fee_]', this).val();
		} else {
			has_cos_error = true;
			return false;
		}
		if (_wbInstructorValidateEmpty($('input[name=ics_target_]', this).get(0), frm.lab_ics_target.value, true)) {
			ics_target += (ics_target === '' ? '' : '~|~') + $('input[name=ics_target_]', this).val();
		} else {
			has_cos_error = true;
			return false;
		}
		if (_wbInstructorValidateFloat($('input[name=ics_hours_]', this).get(0), frm.lab_ics_hours.value, true, 1)) {
			ics_hours += (ics_hours === '' ? '' : '~|~') + $('input[name=ics_hours_]', this).val();
		} else {
			has_cos_error = true;
			return false;
		}
		if (_wbInstructorValidateEmpty($('input[name=ics_content_]', this).get(0), frm.lab_ics_content.value, true)) {
			ics_content += (ics_content === '' ? '' : '~|~') + $('input[name=ics_content_]', this).val();
		} else {
			has_cos_error = true;
			return false;
		}
	});
	if (has_cos_error) {
		return;
	}
	if (frm.ics_title) {
		frm.ics_title.value = ics_title;
	}
	if (frm.ics_fee) {
		frm.ics_fee.value = ics_fee;
	}
	if (frm.ics_hours) {
		frm.ics_hours.value = ics_hours;
	}
	if (frm.ics_target) {
		frm.ics_target.value = ics_target;
	}
	if (frm.ics_content) {
		frm.ics_content.value = ics_content;
	}
	frm.cmd.value = 'ins_upd_exec';
	frm.module.value = 'instructor.InstructorModule';
	if (frm.iti_ent_id.value != '') {
		frm.url_success.value = wbInstructorGetUrl(frm.iti_ent_id.value, frm.iti_type_mark.value);
		frm.url_failure.value = wbInstructorGetUrl(frm.iti_ent_id.value, frm.iti_type_mark.value);
	} else {
		frm.url_success.value = wbInstructorGetUrl('$id', frm.iti_type_mark.value);
		frm.url_failure.value = wbInstructorListUrl(frm.iti_type_mark.value);
	}
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}

function _wbInstructorValidateEmpty(field, label, not_empty) {
	if (field) {
		var val = (field.type.toLowerCase().indexOf('select') != -1) ? field.options[field.selectedIndex].value : field.value;

		if (val.length == 0 || val.search(/^\s+$/) != -1) {
			if (field.type == "text") {
				Dialog.alert(wb_msg_usr_please_specify_value + '"' + label + '"',function(){
					field.focus();
				});
			} else {
				alert(wb_msg_usr_please_select_an + '"' + label + '"');
			}
			return false;
		}
	}
	return true;
}

function _wbInstructorValidateFloat(field, label, not_empty, len) {
	if (field) {
		field.value = wbUtilsTrimString(field.value);

		if (not_empty && field.value.length == 0) {
			alert(wb_msg_usr_please_specify_value + '"' + label + '"');
			field.focus();
			return false;
		}

		if (not_empty && field.value.length > 0) {
			var pattern = /^[0-9]+([.]\d{1,2})?$/;
			if (len === 1) {
				pattern = /^[0-9]+([.]\d{1})?$/;
			}

			if (!pattern.test(field.value)) {
				alert(wb_msg_usr_enter_valid + '"' + label + '"');
				field.focus();
				return false;
			}
		}
	}
	return true;
}

function _wbInstructorValidateTextarea(field, label, not_empty, maxlen) {
	if (field) {
		field.value = wbUtilsTrimString(field.value);

		if (not_empty === true) {
			if (field.value.length <= 0) {
				alert(wb_msg_usr_please_specify_value + '"' + label + '"');
				field.focus();
				return false;
			}
		}

		maxlen = maxlen > 0 ? maxlen : 255;
		if (field.value.length > maxlen) {
			alert(label + wb_msg_gb_too_long + ', ' + wb_msg_gb_word_limit + ' ' + maxlen);
			field.focus();
			return false;
		}
	}
	return true;
}

function wbInstructorDelExec(frm, type, ent_id) {
	if (confirm(frm.lab_del_confirm.value)) {
		frm.module.value = 'instructor.InstructorModule';
		frm.cmd.value = 'del_instr';
		frm.url_success.value = wbInstructorListUrl(type);
		frm.url_failure.value = wbInstructorGetUrl(ent_id, type);
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit();
	}
}

function wbInstructorSrhExec() {
}

function wbInstructorAddCourse(frm, ics_title, ics_fee, ics_hours, ics_target, ics_content) {
	var lab_btn_del = frm.lab_btn_del.value;
	ics_title = ics_title == undefined ? '' : ics_title;
	ics_fee = ics_fee == undefined ? '' : ics_fee;
	ics_hours = ics_hours == undefined ? '' : ics_hours;
	ics_target = ics_target == undefined ? '' : ics_target;
	ics_content = ics_content == undefined ? '' : ics_content;

	var html = '';
	html += '<tr class="instr_cos_itm">';
	html += '<td style="padding:0 0 10px 0"><input value="' + ics_title + '" maxlength="30" style="width:150px;" size="25" name="ics_title_" type="text" class="wzb-inputText"></td>';
	html += '<td style="padding:0 0 10px 0"><input value="' + ics_fee + '" maxlength="20" style="width:150px;" size="25" name="ics_fee_" type="text" class="wzb-inputText"></td>';
	html += '<td style="padding:0 0 10px 0"><input value="' + ics_target + '" maxlength="20" style="width:150px;" size="25" name="ics_target_" type="text" class="wzb-inputText"></td>';
	html += '<td style="padding:0 0 10px 0"><input value="' + ics_hours + '" maxlength="20" style="width:150px;" size="25" name="ics_hours_" type="text" class="wzb-inputText"></td>';
	html += '<td style="padding:0 0 10px 0"><input value="' + ics_content + '" maxlength="100" style="width:150px;" size="25" name="ics_content_" type="text" class="wzb-inputText"></td>';
	html += '<td align="center" valign="top"><a class="btn wzb-btn-blue"  onclick="javascript:instr.del_course(this);">' + lab_btn_del + '</a></td>';
	html += '</tr>';
	$(html).appendTo($('#instr_cos_contariner'));
}

function wbInstructorAddViewCourse(ics_title, ics_fee, ics_hours, ics_target, ics_content) {
	var row_class = ($('#instr_cos_contariner tr.instr_cos_itm').size() % 2 == 1) ? 'RowsEven' : 'RowsOdd';

	var html = '';
	html += '<tr class="instr_cos_itm ' + row_class + '">';
	html += '<td>' + ics_title + '</td>';
	html += '<td>' + ics_fee + '</td>';
	html += '<td>' + ics_target + '</td>';
	html += '<td>' + ics_hours + '</td>';
	html += '<td align="right">' + ics_content + '</td>';
	html += '</tr>';
	$(html).appendTo($('#instr_cos_contariner'));
}

function wbInstructorDelCourse(obj) {
	$(obj).parents('.instr_cos_itm').remove();
	none_title();
}

function none_title(){
	var proj_tb = $("#instr_cos_contariner");
	if(proj_tb.find('.instr_cos_itm').length <= 0){
		proj_tb.append("<tr><td height='30' colspan='6' align='center'><div class='losedata' style='margin-top:50px'><i class='fa fa-folder-open-o'></i></div></td></tr><tr id='spl_tr_title'><td height='30' colspan='6' align='center'><span class='Text'>"+wb_msg_no_course+"</span></td></tr>");
	}	
}

function wbInstructorViewCourse(ent_id, type) {
	var ref_ent_id = 0;
	if (type == 'IN') {
		ref_ent_id = ent_id;
	}
	var url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'ins_course_view', 'ref_ent_id', ref_ent_id, 'itc_iti_ent_id', ent_id, 'stylesheet', 'ins_course_details.xsl');
	self.location.href = url;
}

// InstructorComment
function wbInstructorCommentView(itm_id, itc_iti_ent_id) {
	str_feature = 'toolbar=' + 'no' + ',width=' + '880' + ',height=' + '300' + ',scrollbars=' + 'yes' + ',resizable=' + 'yes' + ',status=' + 'no';

	var url = wb_utils_invoke_disp_servlet('module', 'instructor.InstructorModule', 'cmd', 'ins_comment_view', 'itc_itm_id', itm_id, 'itc_iti_ent_id', itc_iti_ent_id, 'stylesheet', 'ins_comment_details.xsl');
	wbUtilsOpenWin(url, 'InstrConment', false, str_feature);
}

function wbChangeInsSet(frm, obj) {
	if (last_instr_set != obj.value) {
		last_instr_set = obj.value;
		if (document.frmXml.item_access_INSTR_box1.options.length > 0) {
			if (!confirm(wb_msg_inst_reset_dependent_1 + wb_msg_itp_reset_dependent_2)) {
				return false;
			}

			RemoveAllOptions(document.frmXml.item_access_INSTR_box1);
		}
	}
}

function wbLessInstrEdit(frm, msg, cmd) {
	var ili_usr_ent_id_lst_str = '';
	var check = false;
	if (frm.usr.length) {
		for ( var i = 0; i < frm.usr.length; i++) {
			if (frm.usr[i].checked) {
				ili_usr_ent_id_lst_str += frm.usr[i].value + '~';
				check = true;
			}
		}
	} else {
		if (frm.usr.checked == true) {
			ili_usr_ent_id_lst_str = frm.usr.value;
			check = true;
		}
	}

	if (!check) {
		Dialog.alert(msg)
		return;
	}
	url = wb_utils_invoke_ae_servlet('env', 'wizb', 'cmd', cmd, 'ils_id', frm.ils_id.value, 'stylesheet', 'ae_get_run_lesson_info.xsl', 'ili_usr_ent_id_lst_str', ili_usr_ent_id_lst_str, "url_success",
			"../htm/close_and_reload_window.htm", 'url_failure', '../htm/close_window.htm');

	self.location.href = url;
}
