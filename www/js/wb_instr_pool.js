function instrPool(){
	this.search_instr = SearchInstructor;
	this.research_instr = ResearchInstructor;
	this.get_instr = GetInstructor;
	this.goto_add_instr = AddInstructorPage;
	this.add_instr = AddInstructor;
	this.add_instr_exec = AddInstructorExec;
	this.remove_instr = RemoveInstructor;
	this.edit_instr = EditInstructor;
	this.upd_instr = UpdInstructor;
}
function GetInstructor(usr_ste_usr_id){
	var url = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',usr_ste_usr_id,'stylesheet','instr_detail.xsl');
	window.location.href = url;
}
function AddInstructorPage(){
	var url = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','add_instr','stylesheet','instr_add_page.xsl');
	window.location.href = url;
}
function SearchInstructor(frm){
	/*
	frm.module.value="instructorpool.InstructorModule";
	frm.cmd.value="search";
	frm.stylesheet.value="instr_searching_result.xsl";
	gen_instr_search_condition(frm);
	var url = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','search','stylesheet','instr_searching_result.xsl','instr_search_condition',frm.instr_search_condition.value);
	window.location.href = url;
	*/
	str1 = "";
	str2 = "";
	if(frm.s_usr_id) {
		frm.s_usr_id.value = wbUtilsTrimString(frm.s_usr_id.value);
		if(frm.s_usr_id.value.length > 0) {
			str1 += "usr_ste_usr_id~";
			str2 += frm.s_usr_id.value + "~";
		}
	}
	if(frm.s_usr_display_bil) {
		frm.s_usr_display_bil.value = wbUtilsTrimString(frm.s_usr_display_bil.value);
		if(frm.s_usr_display_bil.value.length > 0) {
			str1 += "usr_display_bil~";
			str2 += frm.s_usr_display_bil.value + "~";
		}
	}
	if(frm.s_usr_extra_1) {
		frm.s_usr_extra_1.value = wbUtilsTrimString(frm.s_usr_extra_1.value);
		if(frm.s_usr_extra_1.value.length > 0) {
			str1 += "usr_extra_1~";
			str2 += frm.s_usr_extra_1.value + "~";
		}
	}
	if(frm.s_usr_extra_2) {
		frm.s_usr_extra_2.value = wbUtilsTrimString(frm.s_usr_extra_2.value);
		if(frm.s_usr_extra_2.value.length > 0) {
			str1 += "usr_extra_2~";
			str2 += frm.s_usr_extra_2.value + "~";
		}
	}
	if(frm.s_usr_extra_3) {
		frm.s_usr_extra_3.value = wbUtilsTrimString(frm.s_usr_extra_3.value);
		if(frm.s_usr_extra_3.value.length > 0) {
			str1 += "usr_extra_3~";
			str2 += frm.s_usr_extra_3.value + "~";
		}
	}
	if(frm.s_usr_extra_singleoption_21) {
		if(frm.s_usr_extra_singleoption_21[frm.s_usr_extra_singleoption_21.selectedIndex].value.length > 0) {
			str1 += "urx_extra_singleoption_21~";
			str2 += frm.s_usr_extra_singleoption_21[frm.s_usr_extra_singleoption_21.selectedIndex].value + "~";
		}
	}
	if(str1 != ""){
		frm.usr_srh_col_lst.value = str1.substring(0, str1.length - 1);
	}
	if(str2 != ""){
		frm.usr_srh_value_lst.value = str2.substring(0, str2.length - 1);
	}

	frm.action = wb_utils_servlet_url
	frm.cmd.value="search_ent_lst";
	frm.stylesheet.value="instr_searching_result.xsl";
	frm.method="get";
	frm.submit();
}

function ResearchInstructor(){
	var url = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','search_instr','stylesheet','instr_search_page.xsl');
	window.location.href = url;
}
function EditInstructor(usr_ste_usr_id){
	var url = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',usr_ste_usr_id,'stylesheet','instr_detail_upd.xsl');
	window.location.href = url;
}

function AddInstructor(frm){
	if(frm.usr_ste_usr_id.value==''){
		if(!wbUtilsValidateEmptyField(frm.usr_ste_usr_id, frm.lab_login_id.value)){
			return;
		}
	}
	frm.module.value = 'instructorpool.InstructorModule';
	frm.cmd.value = 'prep_add_instr';
	frm.stylesheet.value = 'add_instr.xsl'
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "get";
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','add_instr','stylesheet','instr_add_page.xsl');
	frm.submit();
}

function AddInstructorExec(frm){
	if(frm.usr_ste_usr_id.value==''){
		alert(wb_msg_ip_usr_id_empty);
		return;
	}
	frm.module.value = 'instructorpool.InstructorModule';
	frm.cmd.value = 'add';
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',frm.usr_ste_usr_id.value,'stylesheet','instr_detail.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','add_instr','stylesheet','instr_add_page.xsl');
	frm.submit();
}

function RemoveInstructor(frm,lang){
	if(confirm(eval('wb_msg_'+lang+'_confirm'))){
		frm.module.value = 'instructorpool.InstructorModule';
		frm.cmd.value = 'remove_instr';
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','search_instr','stylesheet','instr_search_page.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',frm.usr_ste_usr_id,'stylesheet','instr_detail.xsl');
		frm.method = "post";
		frm.action = wb_utils_disp_servlet_url;
		frm.submit();
	}else return;
}
function UpdInstructor(frm){
	frm.module.value ='instructorpool.InstructorModule';
	frm.cmd.value = 'upd_instr';
	frm.action = wb_utils_disp_servlet_url;
	frm.method = "post";
	frm.url_success.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',frm.usr_ste_usr_id.value,'stylesheet','instr_detail.xsl');
	frm.url_failure.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','search_instr','stylesheet','instr_search_page.xsl');
	frm.submit();
}
function gen_instr_search_condition(frm){
	frm.instr_search_condition.value="";
	if(frm.usr_ste_usr_id.value!=""){
		frm.instr_search_condition.value+="and usr_ste_usr_id like "+"'%"+frm.usr_ste_usr_id.value+"%' ";
	}
	if(frm.usr_display_bil.value!=""){
		frm.instr_search_condition.value+="and usr_display_bil like "+"'%"+frm.usr_display_bil.value+"%' ";
	}
	if(frm.usr_extra_singleoption_21.value!=""){
		frm.instr_search_condition.value+="and urx_extra_singleoption_21="+"'"+frm.usr_extra_singleoption_21.value+"' ";
	}
}
