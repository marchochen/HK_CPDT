// ------------------ wizBank StudyGroup object -------------------
// Convention:
// public functions : use "wbStudyGroup" prefix
// private functions: use "_wbStudyGroup" prefix
// ------------------------------------------------------------

function wbStudyGroup() {

	this.get_sgp_prep = wbStudyGroupGetSgpPrep
	this.del_sgp = wbStudyGroupDelSgp
	this.ins_upd_sgp = wbStudyGroupInsUpdSgp
	this.show_content = wbStudyGroupShowSgpByTC

}

function wbStudyGroupGetSgpPrep(sgp_id) {
	var url = wb_utils_invoke_disp_servlet('module',
			'JsonMod.studyGroup.StudyGroupModule', 'cmd', 'get_sgp_prep',
			'stylesheet', 'study_group_ins_upd.xsl', 'sgp_id', sgp_id);
	window.location.href = url;
}
function wbStudyGroupInsUpdSgp(frm, old_tcr_id) {
	
	if(!wbUtilsValidateEmptyField(frm.sgp_title, frm.lab_sgp_title.value)){
		frm.sgp_title.focus()
		return;
	}
	if(frm.tcr_id_lst.options.length==0 ||frm.tcr_id_lst.options[0].value ==''){
		alert(wb_sgp_please_enter_tcr);
		return;
	}
	frm.tcr_id.value=frm.tcr_id_lst.options[0].value;
	if(frm.manager_lst.options.length==0){
		alert(wb_sgp_please_enter_manager);
		return;
	}
	
	if(frm.sgp_desc.value.length > 255){
		alert(wb_sgp_desc_not_longer + 255);
		return;
	}
	
	if (old_tcr_id == 0
			|| frm.tcr_id.value == old_tcr_id
			|| (frm.tcr_id .value!= old_tcr_id && confirm(wb_sgp_reset_tcr_comfirm))) {
		
		
		var sgp_mgt_str="";
		var idsize=frm.manager_lst.options.length;
		for(var i=0; i<idsize; i++){
			if(i!=0){
					sgp_mgt_str=sgp_mgt_str+"~"+frm.manager_lst.options[i].value;
			}else{
					sgp_mgt_str=sgp_mgt_str+frm.manager_lst.options[i].value;
			}
		}
		frm.sgp_mgt_str.value=sgp_mgt_str;
		
		frm.module.value = 'JsonMod.studyGroup.StudyGroupModule';
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','JsonMod.studyGroup.StudyGroupModule','cmd', 'get_sgp_lst','stylesheet', 'study_group_lst.xsl', 'tcr_id', frm.tcr_id.value);
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','JsonMod.studyGroup.StudyGroupModule','cmd', 'get_sgp_lst','stylesheet', 'study_group_lst.xsl');
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit();
		
	}
}
function wbStudyGroupDelSgp(frm, sgp_id, sgp_upd_timestamp) {
	if (confirm(wb_sgp_delete_sgp_comfirm) && sgp_id > 0) {
		frm.module.value = 'JsonMod.studyGroup.StudyGroupModule';
		frm.cmd.value = 'del_sgp';
		frm.url_success.value = self.location.href;
		frm.url_failure.value = self.location.href;
		frm.sgp_id.value = sgp_id;
		frm.sgp_upd_timestamp.value = sgp_upd_timestamp;
		frm.method = 'post';
		frm.action = wb_utils_disp_servlet_url;
		frm.submit();
	}
}

function wbStudyGroupShowSgpByTC(tcr_id){
	url = wb_utils_invoke_disp_servlet('module','JsonMod.studyGroup.StudyGroupModule','cmd', 'get_sgp_lst','stylesheet', 'study_group_lst.xsl','tcr_id', tcr_id);
	window.location.href = url;
}