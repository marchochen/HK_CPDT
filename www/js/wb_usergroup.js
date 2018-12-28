// ------------------ wizBank UserGroup object -------------------
// Convention:
//   public functions : use "wbUserGroup" prefix
//   private functions: use "_wbUserGroup" prefix
// ------------------------------------------------------------

/* constructor */
function wbUserGroup(){
	//new API
	this.user = new wbUserGroupUser
	this.group = new wbUserGroupGroup
	this.grade = new wbUserGrade
	this.utils = new wbUserGroupUtils
	this.search = new wbUserGroupSearch
	this.register = new wbUserGroupRegister
	this.activate = new wbUserGroupActivate
	this.position = new wbUserPosition
	//Method
	this.prof_maintain = wbUserGroupProfileMaintain
}

function wbUserGrade() {
	this.maintain = wbUserGradeMaintenance
}

function wbUserGradeMaintenance() {
	url = wb_utils_invoke_servlet('cmd','get_ugr_tree', 'stylesheet', 'ugr_tree.xsl')
	window.parent.location.href = url;
}

//SubClass wbUserGroupUser
function wbUserGroupUser(){
	//My Profile
	this.my_profile = wbUserGroupUserMyProfile

	//My Biography
	this.my_biography = wbUserGroupUserMyBiography
	this.upd_biography = wbUserGroupUserUpdBiography

	//View User Biography
	this.get_usr_biography = wbUserGroupUserGetUsrBiography
	this.get_usr_biography_popup = wbUserGroupUserGetUsrBiographyPopUp

	this.ins_prep = wbUserGroupUserInsPrep
	this.ins_exec = wbUserGroupUserInsExec

	this.edit_prep = wbUserGroupUserEditPrep
	this.edit_exec = wbUserGroupUserEditExec

	this.rename_prep = wbUserGroupUserRenamePrep
	this.rename_exec = wbUserGroupUserRenameExec

	this.restore_exec = wbUserGroupUserRestoreExec
	this.restore_exec2 = wbUserGroupUserRestoreExec2

	this.upd_pwd_prep = wbUserGroupUserUpdPwdPrep
	this.upd_pwd_exec = wbUserGroupUserUpdPwdExec

	this.usr_ancestor_usg_lst_popup = wbUserAncestorUsgLstPopup
	this.usr_ancestor_usg_lst_add = wbUserAncestorUsgLstAdd
	this.update_usg_confirm_add = wbUserGroupConfirmBeforeAdd;
	this.update_usg_confirm_remove = wbUserGroupConfirmBeforeRemove;

	this.reset_pwd_prep = wbUserGroupUserResetPwdPrep
	this.reset_pwd_exec = wbUserGroupUserResetPwdExec

	this.del_usr = wbUserGroupUserDel
	this.del_multi_usr = wbUserGroupUserDelMultiUser
	this.del_multi_trash_usr = wbUserGroupUserDelMultiTrashUser
	this.del_trash_usr = wbUserGroupUserDelTrash

	this.manage_usr = wbUserGroupUserManage
	this.manage_instr = wbUserGroupInstrManage
	this.manage_usg_popup = wbUserGroupManageUserGroup
	this.manage_usr_popup = wbUserGroupUserManagePopup
	this.manage_del_usr = wbUserGroupUserManageDelUser

	this.unchk_remove_entity = wbUserGroupInsUserUncheckRemoveEntity

	this.add_instr_exec = wbUserGroupUserAddInstrExec
	this.upd_profile_exec = wbUserGroupUserUpdProfileExec
	
	this.forget_pwd = wbUserForgetPwdPrep
	
	this.delete_user = wbUserGroupUserDeleteUser
	this.all_delete_user = wbAllUserGroupUserDeleteUser
	
	this.upd_batch_Prep = wbUserUpdBatchPrep  
	this.upd_batch_exec = wbUserUpdBatchExec
    this.upd_batch_exec2 = wbUserUpdBatchExec2
}

//SubClass wbUserGroupGroup
function wbUserGroupGroup(){
	this.ins_prep = wbUserGroupGroupInsPrep
	this.ins_exec = wbUserGroupGroupInsExec

	this.edit_prep = wbUserGroupGroupEditPrep
	this.edit_exec = wbUserGroupGroupEditExec

	this.del_grp = wbUserGroupGroupDel

	this.manage_grp = wbUserGroupGroupManage
	this.manage_grp_del_usr = wbUserGroupDelUserManage
}

//SubClass wbUserGroupSearch
function wbUserGroupSearch(){
	this.popup_search = wbUserGroupPopupSearch //Search Again
	this.popup_search_prep = wbUserGroupPopupSearchPrep
	this.popup_search_prep_ind = wbUserGroupPopupSearchPrepInd
	this.popup_search_exec = wbUserGroupPopupSearchExec

	this.search_exec = wbUserGroupSearchExec
	this.search_result = wbUserGroupSearchResult
	this.adv_search_prep = wbUserGroupAdvSearchPrep
	this.adv_search_exec = wbUserGroupAdvSearchExec
	this.instr_adv_search_exec = wbInstrAdvSearchExec
	this.get_popup_usr_lst = wbUserGoupGetPopupUsrLst
}

//SubClass wbUserGroupUtils
function wbUserGroupUtils(){
	this.copy = wbUserGroupUtilsCopy
	this.cut = wbUserGroupUtilsCut
	this.paste = wbUserGroupUtilsPaste
}

//SubClass wbUserGroupRegister
function wbUserGroupRegister(){
	this.guest_reg_usr_prep = wbUserGuestRegisterUserPrep

	this.reg_usr_prep = wbUserRegisterUserPrep
	this.reg_usr_exec = wbUserRegisterUserExec
	this.reg_usr_approval_lst = wbUserRegisterUserApprovalLst
	this.reg_usr_approval = wbUserRegisterUserApproval
	this.reg_usr_disapproval = wbUserRegisterUserDisapproval
	this.reg_usr_approval_exec = wbUserRegisterUserApprovalExec
}

//SubClass wbUserGroupActivate
function wbUserGroupActivate(){
	this.reactivate_lst = wbUserActivateReactivateLst
	this.reactivate_prep = wbUserActivateReactivatePrep
	this.reactivate_exec = wbUserActivateReactivateExec
}

//Public Functions
//=======================================================================================
function wbUserGroupProfileMaintain(){
	var url = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'usr_manager.xsl', 'filter_user_group', 1)
	//window.location.href = url
	top.location.href = url
}

//==================================== wbUserGroupUser ===================================
function wbUserGroupUserMyProfile(usr_id, stylesheet){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', stylesheet, 'usr_ent_id', usr_id)
	window.location.href = url
}

function wbUserGroupUserMyBiography(usr_ent_id){
	var url = wb_utils_invoke_disp_servlet('cmd', 'get_biography', 'module', 'personalization.PsnBiographyModule', 'stylesheet', 'my_biography.xsl', 'usr_ent_id', usr_ent_id)
	window.location.href = url
}

function wbUserGroupUserUpdBiography(frm, usr_ent_id){
	var i = 0;
	var opt_lst = "";

	for(i = 0; i < frm.elements.length; i++){
		var ele = frm.elements[i]
		if(ele.type == 'radio' && ele.value == 'true' && ele.checked == true){
			if( ele.name == 'rdo_self_desc' ){
				if( frm.self_desc.value.length == 0 ) {
					alert(wb_msg_usr_enter_self_desc);
					frm.self_desc.focus();
					return;
				}
			}
			opt_lst += ele.name + "~"

		}
	}

	if(frm.self_desc.value.length > wb_utils_text_limit){
		alert(wb_msg_usr_self_desc_too_long);
		frm.self_desc.focus();
		return;
	}

	frm.option_lst.value = opt_lst
	frm.action = wb_utils_disp_servlet_url
	frm.module.value = 'personalization.PsnBiographyModule'
	frm.cmd.value = 'save_my_biography'
	frm.method = 'post'
	frm.url_success.value = "javascript:wb_utils_gen_home()"
	frm.url_failure.value = wb_utils_invoke_disp_servlet('cmd', 'get_biography', 'module', 'personalization.PsnBiographyModule', 'stylesheet', 'my_biography.xsl', 'usr_ent_id', usr_ent_id)
	frm.submit()
}

function wbUserGroupUserGetUsrBiographyPopUp(usr_ent_id){
	var str_feature = 'toolbar=' + 'no' + ',width=' + '600' + ',height=' + '400' + ',scrollbars=' + 'yes' + ',resizable=' + 'no' + ',status=' + 'yes';
	var url = wb_utils_invoke_disp_servlet('cmd', 'get_biography', 'module', 'personalization.PsnBiographyModule', 'stylesheet', 'usr_biography_popup.xsl', 'usr_ent_id', usr_ent_id)
	var biography = wbUtilsOpenWin(url, 'biography', false, str_feature)
}

function wbUserGroupUserGetUsrBiography(usr_ent_id){
	var url = wb_utils_invoke_disp_servlet('cmd', 'get_biography', 'module', 'personalization.PsnBiographyModule', 'stylesheet', 'usr_biography.xsl', 'usr_ent_id', usr_ent_id)
	window.location.href = url;
}

function wbUserGroupUserInsPrep(id, lang, active_tab){
	var url = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'ent_id', id, 'stylesheet', 'usr_detail_ins.xsl','active_tab',active_tab)
	window.location.href = url;
}
//新增用户
function wbUserGroupUserInsExec(frm, parent_id, lang,active_tab){
	if(_wbUserGroupInsUserValidateFrm(frm, lang)){
		frm.cmd.value = 'ins_usr'
		frm.ent_id_parent.value = parent_id
		frm.url_success.value = wbUserGroupGroupManageURL(parent_id, '', '', '','','',active_tab)
		frm.url_failure.value = self.location.href
		frm.url_failure1.value = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'usr_manager.xsl');
		frm.method = "post"
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

function wbUserGroupUserEditPrep(usr_id, stylesheet, ent_id, stype, stimestamp, lang){
	var url = wbUserGroupUserEditPrepURL(usr_id, stylesheet, ent_id, stype, stimestamp, lang)
	window.location.href = url
}

function wbUserGroupUserEditPrepURL(usr_id, stylesheet, ent_id, stype, stimestamp, lang){
	var url = ''

	if(stylesheet == null || stylesheet == ''){
		stylesheet = 'usr_detail_upd.xsl';
	}

	if(stype != null){
		url = wb_utils_invoke_servlet('cmd', 'get_usr', 'ent_id', ent_id, 'stype', stype, 'stimestamp', stimestamp, 'stylesheet', stylesheet, 'usr_ent_id', usr_id)
	}else{
		url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', stylesheet, 'usr_ent_id', usr_id)
	}
	return url;
}

function wbUserGroupUserEditExec(frm, lang, trim_profile){
	if(trim_profile == null || trim_profile == ''){
		trim_profile = '0';
	}

	var _stype = stype ? stype : '';
	var _stimestamp = stimestamp ? stimestamp : '';
	if(_wbUserGroupUpdUserValidateFrm(frm, lang)){
		frm.cmd.value = "upd_usr_on_demand";
		if(trim_profile == '1'){
			frm.url_success.value = "javascript:wb_utils_gen_home()"
			frm.url_failure.value = self.location.href
		}else{
			var _usr_id = getUrlParam("usr_ent_id")
			frm.stylesheet.value = 'tc_upd_msg_box.xsl';
			frm.url_success.value = wbUserGroupUserManageURL(_usr_id, '', _stype, _stimestamp, '')
			frm.url_failure.value = self.location.href
			frm.url_failure1.value = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'usr_manager.xsl');
		}

		frm.action = wb_utils_servlet_url
		frm.method = 'post'
		frm.submit()
	}
}

function wbUserGroupUserRenamePrep(usr_ent_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', 'usr_rename.xsl', 'usr_ent_id', usr_ent_id)
	window.location.href = url
}

function wbUserGroupUserRenameExec(frm, lang, trim_profile){
	if(_wbUserGroupInsUserValidateFrm(frm, lang)){
		var _usr_ent_id = getUrlParam("usr_ent_id")
		frm.cmd.value = "rename_usr"
		frm.url_success.value = wbUserGroupUserManageURL(_usr_ent_id, '', '', '', '')
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_servlet_url
		frm.method = 'post'
		frm.submit()
	}
}

function wbUserGroupUserRestoreExec2(usr_id,lang,from){
	if (!confirm(eval('wb_msg_' + lang + '_valid_confirm'))) {
		return;
	}
	wbUserGroupUserRestoreExec(usr_id,from);
}

function wbUserGroupUserRestoreExec(usr_id, from){
	var url_success = ''
	
	if(from == 'usr_manager'){
		url_success = window.location.href;
	}else if(from == 'usr_detail'){
		url_success = wbUserGroupUserManageURL(usr_id, '', '', '', '')
	}
	
	if(usr_id==''){
        alert('请选择要还原的用户');
        var url_failure = window.location.href;
        return ;
    }

	var url_failure = window.location.href;
	var url = wb_utils_invoke_servlet('cmd', 'restore_usr', 'ent_id_lst', usr_id, 'url_success', url_success, 'url_failure', url_failure);
//	var url = wb_utils_invoke_servlet('cmd', 'restore_usr', 'usr_ent_id', usr_id, 'url_success', url_success, 'url_failure', url_failure)
	window.location.href = url
}

function wbUserGroupUserUpdPwdPrep(usr_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', 'usr_pwd_upd.xsl', 'usr_ent_id', usr_id)
	window.location.href = url
}

function wbUserGroupUserUpdPwdExec(frm, lang){
	if(_wbUserGroupUpdPwdValidateFrm(frm, lang)){
		frm.cmd.value = "upd_usr_pwd"
		var url_success = getUrlParam('url_success')

		if(url_success == ""){
			url_success = "javascript:wb_utils_gen_home()"
		}

		frm.url_success.value = url_success
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_servlet_url
		frm.method = 'post'
		frm.submit()
	}
}

function wbUserGroupUserResetPwdPrep(usr_ent_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'stylesheet', 'usr_pwd_reset.xsl', 'usr_ent_id', usr_ent_id)
	window.location.href = url
}
//管理员修改用户密码
function wbUserGroupUserResetPwdExec(frm, lang){
	if(_wbUserGroupResetPwdValidateFrm(frm, lang)){
		var _stype = stype ? stype : '';
		var _stimestamp = stimestamp ? stimestamp : '';
		var _usr_id = getUrlParam("usr_ent_id")
		frm.cmd.value = "reset_usr_pwd"
		frm.url_success.value = wbUserGroupUserManageURL(_usr_id, '', _stype, _stimestamp, '')
		frm.url_failure.value = self.location.href
		frm.action = wb_utils_servlet_url
		frm.method = 'post'
		frm.submit()
	}
}

function wbUserGroupUserDel(id, parent_id, timestamp, lang, active_tab){
	if(confirm(eval('wb_msg_' + lang + '_confirm'))){
		var url_success = wbUserGroupGroupManageURL(parent_id, '', '','','','',active_tab)
		var url_failure = self.location.href
		var url = wb_utils_invoke_servlet('cmd', 'del_usr', 'usr_ent_id', id, 'ent_id_parent', parent_id, 'usr_timestamp', timestamp, 'url_success', url_success, 'url_failure', url_failure)
		window.location.href = url
	}
}

function wbUserGroupUserDelMultiUser(frm, lang){
	if(_wbUserGetCheckedUserLst(frm) == ""){
		alert(eval("wb_msg_" + lang + "_sel_usr"));
		return;
	}
	if(confirm(eval('wb_msg_' + lang + '_confirm'))){
		var str_feature = 'toolbar=' + 'no' + ',width=' + screen.availWidth + ',height=' + screen.availHeight + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'yes';

		wb_utils_set_cookie("appn_usr_name", "");
		wb_utils_set_cookie("current", "");
		wb_utils_set_cookie("total", "");
		wb_utils_set_cookie("type", "usergroup");
		var url = "../htm/application_frame_window.htm?lang=" + lang;
		wbUtilsOpenWin(url, 'del_user_win', false, str_feature);
	}
}

function wbUserGroupUserDelMultiTrashUser(frm, lang){
	if(_wbUserGetCheckedUserLst(frm) == ""){
		alert(eval("wb_msg_" + lang + "_sel_usr"));
		return;
	}
	if(confirm(eval('wb_msg_' + lang + '_confirm_del_usr'))){
		var str_feature = 'toolbar=' + 'no' + ',width=' + '400' + ',height=' + '180' + ',scrollbars=' + 'no' + ',resizable=' + 'no' + ',status=' + 'yes';
		wb_utils_set_cookie("appn_usr_name", "");
		wb_utils_set_cookie("current", "");
		wb_utils_set_cookie("total", "");
		wb_utils_set_cookie("type", "usergroup");
		var url = "../htm/application_frame_window.htm?lang=" + lang;
		wbUtilsOpenWin(url, 'del_user_win', false, str_feature);
	}
}

function wbUserGroupUserDelTrash(id, parent_id, timestamp, lang,active_tab){
	if(confirm(eval('wb_msg_' + lang + '_confirm_del_usr'))){
		var url_success = wbUserGroupGroupManageURL(parent_id, '', '','','','',active_tab)
		var url_failure = wbUserGroupUserManageURL(id, '', '', '', '')
		var url = wb_utils_invoke_servlet('cmd', 'trash_usr', 'usr_ent_id_lst', id, 'usr_timestamp_lst', timestamp, 'url_success', url_success, 'url_failure', url_failure)
		window.location.href = url
	}
}

function wbUserAncestorUsgLstPopup(usg_id){
	var url = "";

	url = wb_utils_invoke_servlet('cmd', 'get_ancestor_usg', 'group_id', usg_id, 'stylesheet', 'usr_ancestor_usg_lst.xsl')
	var str_feature = ',width=' + '500' + ',height=' + '350' + ',scrollbars=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10' + ',status=' + 'yes'
	wbUtilsOpenWin(url, "usrWin", false, str_feature);
}

function wbUserAncestorUsgLstAdd(frm){
	var lab_group = frm.lab_group ? frm.lab_group.value : 'Group(sys)'

	var delimiter = "~%~";
	var retValue = "";
	var value = "";
	var ele = frm.usg_id;

	if(ele.length){
		for (i = 0; i < ele.length; i++){
			if(ele[i].checked == true){
				retValue = delimiter + ele[i].value + delimiter + eval('frm.usg_id_' + ele[i].value + '.value') + delimiter;
				value = ele[i].value;
			}
		}
	}else{
		if(ele.checked == true){
			value = ele.value;
			retValue = delimiter + ele.value + delimiter + eval('frm.usg_id_' + ele.value + '.value') + delimiter;
		}
	}

	if(retValue == ""){
		Dialog.alert(wb_msg_usr_please_select_a + lab_group)
		return false;
	}
	if(opener){
		opener.usr_app_approval_usg(retValue);
		opener.document.frmXml.usr_app_approval_usg_ent_id.value = value;
		window.close();
	}
}

function wbUserGroupConfirmBeforeAdd(frm, lang, tree_type, select_type, field_name, item_type_list, pick_leave, approved_list, flag, close_option, pick_root, override_appr_usg, tree_subtype, get_supervise_group, complusory_tree, get_direct_supervise, filter_user_group){
	if(filter_user_group == 'false') {
		filter_user_group = 0;
	} else {
		filter_user_group = 1;
	}
	if( frm.usr_app_approval_usg_ent_id && frm.usr_app_approval_usg_ent_id.value > 0) {
		if(!confirm(eval('wb_msg_' + lang + '_reset_grp_supervisor'))){
			return false;
		}
	}
	goldenman.opentree(tree_type, select_type, field_name, '', pick_leave, approved_list, flag, close_option, pick_root, override_appr_usg, tree_subtype, get_supervise_group, complusory_tree, get_direct_supervise, '', '', '', filter_user_group);

	if( frm.usr_app_approval_usg_ent_id ) {
		frm.usr_app_approval_usg_ent_id.value = '';
		usr_app_approval_usg();
		frm.genremoveapproval_usg.disabled = false;
		frm.genaddapproval_usg.disabled = false;
	}
	return;
}

function wbUserGroupConfirmBeforeRemove(frm, lang, field){

	if( eval(frm + '.usr_app_approval_usg_ent_id') && eval(frm + '.usr_app_approval_usg_ent_id.value') > 0) {
		if(!confirm(eval('wb_msg_' + lang + '_reset_grp_supervisor'))) {
			return false;
		}
	}
	RemoveSingleOption(eval(frm + '.' + field + '_single'), eval(frm + '.' + field));
	if( eval(frm + '.usr_app_approval_usg_ent_id') ) {
		usr_app_approval_usg();
		eval(frm + '.usr_app_approval_usg_ent_id').value = '';
		removeEle = eval(frm + '.genremoveapproval_usg');
		removeEle.disabled = true;
		addEle = eval(frm + '.genaddapproval_usg');
		addEle.disabled = true;
	}
	return;
}

function wbUserGroupUserManage(usr_id, parent_id, stype, stimestamp, nm){
	var url = wbUserGroupUserManageURL(usr_id, parent_id, stype, stimestamp, nm)
	window.location.href = url
}
function wbUserGroupInstrManage(usr_id, parent_id, stype, stimestamp, nm){
	var url = wbUserGroupUserManageURL(usr_id, parent_id, stype, stimestamp, nm)
	wb_utils_open_win(url,'Instructor',780,500);
}
function wbUserGroupUserManageURL(usr_id, parent_id, stype, stimestamp, nm){
	if(stype != null){
		var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', usr_id, 'ent_id', parent_id, 'stype', stype, 'stimestamp', stimestamp, 'stylesheet', 'usr_detail.xsl')
	}else{
		var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', usr_id, 'stylesheet', 'usr_detail.xsl')
	}
	return url
}

function wbUserGroupManageUserGroup(usr_ent_id, usg_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usg', 'group_id', usg_id, 'usr_ent_id', usr_ent_id, 'stylesheet', 'usg_detail_popup.xsl')
	var str_feature = ',width=' + '400' + ',height=' + '200' + ',scrollbars=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10' + ',status=' + 'yes'
	wbUtilsOpenWin(url, "usrWin", false, str_feature);
}

function wbUserGroupUserManagePopup(usr_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', usr_id, 'stylesheet', 'usr_detail_popup.xsl')
	var str_feature = ',width=' + '570' + ',height=' + '260' + ',scrollbars=' + 'yes' + ',screenX=' + '10' + ',screenY=' + '10' + ',status=' + 'yes'
	wbUtilsOpenWin(url, "usrWin", false, str_feature);
}

function wbUserGroupUserManageDelUser(usr_id, parent_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', usr_id, 'ent_id', parent_id, 'stylesheet', 'usr_detail.xsl')
	window.location.href = url
}

function wbUserGroupUserAddInstrExec(frm, lang){

	if(_wbUserGroupUpdUserValidateFrm(frm, lang)){
		frm.cmd.value = "add_instr_on_demand";
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',frm.usr_ste_usr_id.value,'stylesheet','instr_detail.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','add_instr','stylesheet','instr_add_page.xsl');
		frm.action = wb_utils_servlet_url
		frm.method = 'post'
		frm.submit()
	}
}

function wbUserGroupUserUpdProfileExec(frm, lang){

	if(_wbUserGroupUpdUserValidateFrm(frm, lang)){
		frm.cmd.value = "upd_usr_profile_on_demand";
		frm.url_success.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',frm.usr_ste_usr_id.value,'stylesheet','instr_detail.xsl');
		frm.url_failure.value = wb_utils_invoke_disp_servlet('module','instructorpool.InstructorModule','cmd','get_instr','usr_ste_usr_id',frm.usr_ste_usr_id.value,'stylesheet','instr_detail.xsl');
		frm.action = wb_utils_servlet_url
		frm.method = 'post'
		frm.submit()
	}
}

//==================================== wbUserGroupGroup ===================================
function wbUserGroupGroupInsPrep(id, active_tab){
	var url = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'ent_id', id, 'stylesheet', 'usr_ins_grp.xsl','active_tab', active_tab)
	window.location.href = url
}

function wbUserGroupGroupInsExec(frm, ent_id_parent, ent_id_root, lang,active_tab){
	if(_wbUserGroupValidateFrm(frm, lang)){
		frm.ent_id_parent.value = ent_id_parent
		frm.ent_id_root.value = ent_id_root
		frm.url_success.value = wbUserGroupGroupManageURL(ent_id_parent, '', '','','','',active_tab)
		frm.url_failure.value = self.location.href
		frm.url_failure1.value = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'usr_manager.xsl');
		frm.cmd.value = 'ins_usg'
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

function wbUserGroupGroupEditPrep(id,active_tab){
	var url = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'ent_id', id, 'stylesheet', 'usr_grp_upd.xsl','active_tab',active_tab)
	window.location.href = url
}

function wbUserGroupGroupEditExec(frm, ent_id, usg_role, timestamp, lang,active_tab){
	if(_wbUserGroupValidateFrm(frm, lang)){
		frm.ent_id.value = ent_id
		frm.usg_timestamp.value = timestamp
		frm.url_success.value = wbUserGroupGroupManageURL(ent_id, '', '','','','',active_tab)
		frm.url_failure.value = self.location.href
		frm.url_failure1.value = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'usr_manager.xsl');
		frm.cmd.value = 'upd_usg'
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

function wbUserGroupGroupDel(id, parent_id, timestamp, lang, active_tab){
	if(confirm(eval('wb_msg_' + lang + '_confirm'))){
		var url_success = wbUserGroupGroupManageURL(parent_id, '', '','','','',active_tab)
		var url_failure = self.location.href
		var url_failure1 = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'stylesheet', 'usr_manager.xsl');
		var url = wb_utils_invoke_servlet('cmd', 'trash_usg', 'usg_ent_id_lst', id, 'usg_timestamp_lst', timestamp, 'url_success', url_success, 'url_failure', url_failure, 'url_failure1', url_failure1)
		window.location.href = url
	}
}

function wbUserGroupDelUserManage(parent_id, cur_page, pagesize,active_tab){
	cur_page = (cur_page == null) ? '' : cur_page;
	pagesize = (pagesize == null) ? '' : pagesize;
	var url = wb_utils_invoke_servlet('cmd', 'get_del_ent_lst', 'cur_page', cur_page, 'pagesize', pagesize, 'stylesheet', 'usr_manager.xsl','active_tab', active_tab)
	window.location.href = url
}
//进入用户列表
function wbUserGroupGroupManage(id, nm, parent_id, cur_page, pagesize, active_tab){
	sort_order = "ASC";
	var url = wbUserGroupGroupManageURL(id, nm, parent_id, cur_page, pagesize, sort_order, active_tab)
	window.location.href = url
}

function wbUserGroupGroupManageURL(id, nm, parent_id, cur_page, pagesize, sort_order, active_tab){
	cur_page = (cur_page == null) ? '' : cur_page;
	pagesize = (pagesize == null) ? '' : pagesize;
	var url = wb_utils_invoke_servlet('cmd', 'get_ent_lst', 'cur_page', cur_page, 'pagesize', pagesize, 'stylesheet', 'usr_manager.xsl', 'ent_id', id, 'sortOrder',sort_order, 'active_tab', active_tab)
	return url
}
//==================================== wbUserGroupSearch ===================================
function wbUserGroupPopupSearch(ent_id){
	var stylesheet = 'usr_simple_search.xsl';
	var s_itm_id = getUrlParam('s_itm_id');
	var s_search_enrolled = getUrlParam('s_search_enrolled');
	var s_search_role = getUrlParam('s_search_role');
	var s_ftn_ext_id = getUrlParam('s_ftn_ext_id');
	var refreshOpt = getUrlParam('refreshOpt');
	var disabledOpt = getUrlParam('disabled_opt');
	var close_opt = getUrlParam('close_opt');
	var sel_opt = getUrlParam('sel_opt');
	var fld = getUrlParam('fld');
	var s_tcr_id = getUrlParam('s_tcr_id');
	var s_role_types = getUrlParam('s_role_types');
	var url = wb_utils_invoke_servlet('cmd', 'get_meta', 'ent_id', ent_id, 'stylesheet', stylesheet, 'fld', fld, 'sel_opt', sel_opt, 'close_opt', close_opt, 's_ftn_ext_id', s_ftn_ext_id,'s_role_types',s_role_types)


	if(s_itm_id != null && s_itm_id != ''){
		url += '&s_itm_id=' + s_itm_id;
	}
	
	if(s_tcr_id != null && s_tcr_id != ''){
		url += '&s_tcr_id=' + s_tcr_id;
	}

	if(s_search_enrolled != null && s_search_enrolled != ''){
		url += '&s_search_enrolled=' + s_search_enrolled;
	}

	if(s_search_role != null && s_search_role != ''){
		url += '&s_search_role=' + s_search_role;
	}

	if(refreshOpt != null && refreshOpt != ''){
		url += '&refresh_opt=' + refreshOpt;
	}

	if(disabledOpt != null && disabledOpt != ''){
		url += '&disabled_opt=' + disabledOpt;
	}

	if(getUrlParam('auto_enroll_ind') != "") {
		auto_enroll_ind = getUrlParam('auto_enroll_ind');
    	url += '&auto_enroll_ind=' + auto_enroll_ind;
	}
	
	if(getUrlParam('filter_user_group') != "") {
		filter_user_group = getUrlParam('filter_user_group');
    	url += '&filter_user_group=' + filter_user_group;
	}

	//gobal variable
	window.location.href = url;
}

function wbUserGroupPopupSearchPrep(fld_name, selectOpt, root_ent_id, closeOpt, s_itm_id, s_search_enrolled, s_search_role, refreshOpt, disabledOpt, stylesheet, ftn_ext_id, filter_user_group, s_tcr_id, s_role_types, s_instr){
	var screenLeft, screenTop, screenAvailWidth, screenAvailHeight, docWidth, docHeight, openScreenLeft, openScreenTop

	if (document.all) { // Internet Explorer
		screenLeft = window.screenLeft
		screenTop = window.screenTop
		openScreenLeft = screenLeft + 130
		openScreenTop = screenTop + 130
	} else { // Netscape & Others
		screenLeft = window.screenX
		screenTop = window.screenY
		docWidth = window.outerWidth
		docHeight = window.outerHeight
		openScreenLeft = screenLeft + 130
		openScreenTop = screenTop + 130
	}

	if (selectOpt == null || selectOpt == '') {
		selectOpt = '1';
	}

	if (closeOpt == null || closeOpt == '') {
		closeOpt = '1';
	}

	if (s_search_role == null || s_search_role == '') {
		s_search_role = '1';
	}

	if (refreshOpt == null || refreshOpt == '') {
		refreshOpt = '0';
	}

	if (disabledOpt == null || disabledOpt == '') {
		disabledOpt = '0';
	}

	if (filter_user_group == null || filter_user_group == '') {
		filter_user_group = '1';
	}

	var width, height
	var width = '528'
	var height = '400'

	str_feature = 'toolbar=' + 'no' + ',width=' + width + ',height=' + height + ',scrollbars=' + 'yes' + ',resizable=' + 'no' + ',status=' + 'no';

	if (document.all) {
		str_feature += ',top=' + openScreenTop + ',left=' + openScreenLeft;
	} else {
		str_feature += ',screenX=' + openScreenLeft + ',screenY=' + openScreenTop;
	}

	if (stylesheet == null || stylesheet == '') {
		stylesheet = 'usr_simple_search.xsl';
	}
	var url = wb_utils_invoke_servlet('cmd', 'get_meta', 'stylesheet', stylesheet, 'fld', fld_name, 'sel_opt', selectOpt, 'close_opt', closeOpt, 'url_failure', 'javascript:self.close();')

	if (s_itm_id != null && s_itm_id != '') {
		url += '&s_itm_id=' + s_itm_id;
	}

	if (s_search_enrolled != null && s_search_enrolled != '') {
		url += '&s_search_enrolled=' + s_search_enrolled;
	}

	if (s_search_role != null && s_search_role != '') {
		url += '&s_search_role=' + s_search_role;
	}

	if (s_tcr_id != null && s_tcr_id != '') {
		url += '&s_tcr_id=' + s_tcr_id;
	}

	if (s_role_types != null && s_role_types != '') {
		url += '&s_role_types=' + s_role_types;
	}

	if (s_instr != null && s_instr != '') {
		url += '&s_instr=' + s_instr;
	}

	if (refreshOpt != null && refreshOpt != '') {
		url += '&refresh_opt=' + refreshOpt;
	}

	if (disabledOpt != null && disabledOpt != '') {
		url += '&disabled_opt=' + disabledOpt;
	}

	if (ftn_ext_id != null && ftn_ext_id != '') {
		url += '&ftn_ext_id=' + ftn_ext_id;
	}

	if (filter_user_group != null && filter_user_group != '') {
		url += '&filter_user_group=' + filter_user_group;
	}
	// gobal variable
	popup_usr_search = wbUtilsOpenWin(url, 'popup_usr_search', false, str_feature);
}
//for auto_enroll_ind
function wbUserGroupPopupSearchPrepInd(fld_name, selectOpt, root_ent_id, closeOpt, s_itm_id, s_search_enrolled, s_search_role, refreshOpt, disabledOpt, stylesheet, ftn_ext_id, auto_enroll_ind, filter_user_group){
	var screenLeft, screenTop, screenAvailWidth, screenAvailHeight, docWidth, docHeight, openScreenLeft, openScreenTop
		if(document.all){ //Internet Explorer
		screenLeft = window.screenLeft
		screenTop = window.screenTop
		openScreenLeft = screenLeft + 130
		openScreenTop = screenTop + 130
	}else{ //Netscape & Others
		screenLeft = window.screenX
		screenTop = window.screenY
		docWidth = window.outerWidth
		docHeight = window.outerHeight
		openScreenLeft = screenLeft + 130
		openScreenTop = screenTop + 130
	}

	if(selectOpt == null || selectOpt == ''){
		selectOpt = '1';
	}

	if(closeOpt == null || closeOpt == ''){
		closeOpt = '1';
	}

	if(s_search_role == null || s_search_role == ''){
		s_search_role = '1';
	}

	if(refreshOpt == null || refreshOpt == ''){
		refreshOpt = '0';
	}

	if(disabledOpt == null || disabledOpt == ''){
		disabledOpt = '0';
	}
	
	if(filter_user_group == null || filter_user_group == ''){
		filter_user_group = '1';
	}

	var width, height
	var width = '528'
	var height = '367'

	str_feature = 'toolbar=' + 'no' + ',width=' + width + ',height=' + height + ',scrollbars=' + 'yes' + ',resizable=' + 'no' + ',status=' + 'no';

	if(document.all){
		str_feature += ',top=' + openScreenTop + ',left=' + openScreenLeft;
	}else{
		str_feature += ',screenX=' + openScreenLeft + ',screenY=' + openScreenTop;
	}

	if( stylesheet == null || stylesheet == '' ) {
		stylesheet = 'usr_simple_search.xsl';
	}
	var url = wb_utils_invoke_servlet('cmd', 'get_meta', 'stylesheet', stylesheet, 'fld', fld_name, 'sel_opt', selectOpt, 'close_opt', closeOpt)

	if(s_itm_id != null && s_itm_id != ''){
		url += '&s_itm_id=' + s_itm_id;
	}

	if(s_search_enrolled != null && s_search_enrolled != ''){
		url += '&s_search_enrolled=' + s_search_enrolled;
	}

	if(s_search_role != null && s_search_role != ''){
		url += '&s_search_role=' + s_search_role;
	}

	if(refreshOpt != null && refreshOpt != ''){
		url += '&refresh_opt=' + refreshOpt;
	}

	if(disabledOpt != null && disabledOpt != ''){
		url += '&disabled_opt=' + disabledOpt;
	}

	if(ftn_ext_id != null && ftn_ext_id != ''){
		url += '&ftn_ext_id=' + ftn_ext_id;
	}
	
	if(filter_user_group != null && filter_user_group != ''){
		url += '&filter_user_group=' + filter_user_group;
	}
	url += '&auto_enroll_ind=' + auto_enroll_ind;
	//gobal variable
	popup_usr_search = wbUtilsOpenWin(url, 'popup_usr_search', false, str_feature);
}
//for auto_enrol_ind
function wbUserGroupAdvSearchPrep(ent_id, lang){
	var url = wb_utils_invoke_servlet('cmd', 'get_meta', 'ent_id', ent_id, 'stylesheet', 'usr_adv_search.xsl')
	window.location.href = url;
}

function wbUserGroupPopupSearchExec(frm, lang, stylesheet) {
	if (_wbUserGroupValidateSearchFrm(frm, lang)) {

		// grade
		if (frm.s_grade && frm.s_grade_lst.options[0]) {
			frm.s_grade.value = frm.s_grade_lst.options[0].value;
		}

		// group
		if (frm.usr_group_lst && frm.usr_group_lst.options[0]) {
			frm.ent_id.value = frm.usr_group_lst.options[0].value;
		}
		        
        // gender
		if (frm.s_usr_gender) {
			if (frm.usr_gender[0].checked) {
				frm.s_usr_gender.value = frm.usr_gender[0].value;
			}
			if (frm.usr_gender[1].checked) {
				frm.s_usr_gender.value = frm.usr_gender[1].value;
			}
		}

		        // no use any more
		if (frm.s_idc_fcs && frm.s_idc_fcs_lst.options[0]) {
			frm.s_idc_fcs.value = frm.s_idc_fcs_lst.options[0].value;
		}
		if (frm.s_idc_int && frm.s_idc_int_lst.options[0]) {
			frm.s_idc_int.value = frm.s_idc_int_lst.options[0].value;
		}

		if (frm.fld) {
			if (getUrlParam('fld')) {
				frm.fld.value = getUrlParam('fld');
			}
		}

		if (frm.sel_opt) {
			if (getUrlParam('sel_opt')) {
				frm.sel_opt.value = getUrlParam('sel_opt');
			}
		}

		if (frm.close_opt) {
			if (getUrlParam('close_opt')) {
				frm.close_opt.value = getUrlParam('close_opt');
			}
		}

		if (frm.ent_id && frm.s_group_lst) {
			if (frm.s_group_lst.options[0]) {
				frm.ent_id.value = frm.s_group_lst.options[0].value;
			}
		}

		if (frm.s_role_types && getUrlParam('s_role_types') != "") {
			frm.s_role_types.value = getUrlParam('s_role_types');
		} else if (frm.s_role_types) {
			frm.s_role_types.value = _wbUserGroupAdvSearchRoleLst(frm);
		}
		
		if (frm.s_itm_id && getUrlParam("s_itm_id") != "") {
			frm.s_itm_id.value = getUrlParam("s_itm_id");
		}

		if (frm.s_instr && getUrlParam("s_instr") != "") {
			frm.s_instr.value = getUrlParam("s_instr");
		}

		if (frm.s_search_enrolled && getUrlParam("s_search_enrolled") != "") {
			frm.s_search_enrolled.value = getUrlParam("s_search_enrolled");
		}

		if (frm.s_search_role && getUrlParam('s_search_role') != "") {
			frm.s_search_role.value = getUrlParam("s_search_role");
		}

		if (frm.refresh_opt && getUrlParam('refresh_opt') != "") {
			frm.refresh_opt.value = getUrlParam("refresh_opt");
		}

		if (frm.s_ftn_ext_id && getUrlParam('ftn_ext_id') != "") {
			frm.s_ftn_ext_id.value = getUrlParam("ftn_ext_id");
		}

		if (frm.s_ftn_ext_id && getUrlParam('s_ftn_ext_id') != "") {
			frm.s_ftn_ext_id.value = getUrlParam("s_ftn_ext_id");
		}

		if (frm.s_tcr_id && getUrlParam('s_tcr_id') != "") {
			frm.s_tcr_id.value = getUrlParam('s_tcr_id');
		}

		if (frm.disabled_opt && getUrlParam('disabled_opt') != "") {
			frm.disabled_opt.value = getUrlParam("disabled_opt");
		}

		frm.action = wb_utils_servlet_url

		if (stylesheet != null && stylesheet != '') {
			frm.stylesheet.value = 'usr_supervisor_search_result.xsl';
		} else {
			if (frm.sel_opt) {
				if (frm.sel_opt.value == 0 || frm.sel_opt.value == '0') {
					frm.stylesheet.value = 'usr_sim_search_result_single.xsl'
				} else {
					frm.stylesheet.value = 'usr_sim_search_result.xsl'
				}
			} else {
				frm.stylesheet.value = 'usr_sim_search_result.xsl'
			}
		}
		if (frm.auto_enroll_ind && getUrlParam('auto_enroll_ind') != "") {
			frm.auto_enroll_ind.value = getUrlParam('auto_enroll_ind');
		}
		if (frm.filter_user_group && getUrlParam('filter_user_group') != "") {
			frm.filter_user_group.value = getUrlParam('filter_user_group');
		}
		frm.cmd.value = 'search_ent_lst'
		frm.method = "get"
		frm.submit()
	}
}

function wbUserGroupAdvSearchExec(frm, lang){
	//Trim spacing
	if(frm.s_usr_id){
		frm.s_usr_id.value = wbUtilsTrimString(frm.s_usr_id.value);
	}
	if(frm.s_usr_display_bil){
		frm.s_usr_display_bil.value = wbUtilsTrimString(frm.s_usr_display_bil.value);
	}
	
	if(frm.s_usr_gender) {
	    if (frm.usr_gender[0].checked) {
    		frm.s_usr_gender.value = frm.usr_gender[0].value;
	    }
	    if (frm.usr_gender[1].checked) {
    		frm.s_usr_gender.value = frm.usr_gender[1].value;
	    }
	}
	
	if(frm.s_usr_email){
		frm.s_usr_email.value = wbUtilsTrimString(frm.s_usr_email.value);
	}	
	if(frm.s_usr_tel){
		frm.s_usr_tel.value = wbUtilsTrimString(frm.s_usr_tel.value);
	}
	//
	if(_wbUserGroupValidateSearchFrm(frm, lang)){
		if(frm.ent_id && frm.s_group_lst){
			if(frm.s_group_lst.options[0]){
				frm.ent_id.value = frm.s_group_lst.options[0].value;
			}
		}
		
		if(frm.s_grade && frm.s_grade_lst.options[0]){
			frm.s_grade.value = frm.s_grade_lst.options[0].value;
		}

		if(frm.usr_group_lst && frm.usr_group_lst.options[0]){
			frm.ent_id.value = frm.usr_group_lst.options[0].value;
		}

		if(frm.s_idc_fcs && frm.s_idc_fcs_lst.options[0]){
			frm.s_idc_fcs.value = frm.s_idc_fcs_lst.options[0].value;
		}

		if(frm.s_idc_int && frm.s_idc_int_lst.options[0]){
			frm.s_idc_int.value = frm.s_idc_int_lst.options[0].value;
		}

		if(frm.s_role_types){
			frm.s_role_types.value = _wbUserGroupAdvSearchRoleLst(frm);
		}

		frm.action = wb_utils_servlet_url
		frm.stylesheet.value = 'usr_search_result.xsl'
		frm.cmd.value = 'search_ent_lst'
		frm.method = "get"
		frm.submit()
	}
}
function wbInstrAdvSearchExec(frm, lang){
	if(frm.s_usr_id){
		frm.s_usr_id.value = wbUtilsTrimString(frm.s_usr_id.value);
	}
	if(frm.s_usr_display_bil){
		frm.s_usr_display_bil.value = wbUtilsTrimString(frm.s_usr_display_bil.value);
	}
	if(frm.s_itm_code){
		frm.s_itm_code.value = wbUtilsTrimString(frm.s_itm_code.value);
	}
	//Trim spacing
	if(frm.s_usr_id){
		frm.s_usr_id.value = wbUtilsTrimString(frm.s_usr_id.value);
	}
	if(frm.s_usr_display_bil){
		frm.s_usr_display_bil.value = wbUtilsTrimString(frm.s_usr_display_bil.value);
	}
	
	if(frm.s_usr_gender) {
	    if (frm.usr_gender[0].checked) {
    		frm.s_usr_gender.value = frm.usr_gender[0].value;
	    }
	    if (frm.usr_gender[1].checked) {
    		frm.s_usr_gender.value = frm.usr_gender[1].value;
	    }
	}
	
	if(frm.s_usr_email){
		frm.s_usr_email.value = wbUtilsTrimString(frm.s_usr_email.value);
	}
	if(frm.s_usr_tel){
		frm.s_usr_tel.value = wbUtilsTrimString(frm.s_usr_tel.value);
	}
	if(frm.s_usr_job_title){
		frm.s_usr_job_title.value = wbUtilsTrimString(frm.s_usr_job_title.value);
	}
	//
	if(_wbUserGroupValidateSearchFrm(frm, lang)){


		if(frm.ent_id && frm.s_group_lst){
			if(frm.s_group_lst.options[0]){
				frm.ent_id.value = frm.s_group_lst.options[0].value;
			}
		}
		
		if(frm.s_grade && frm.s_grade_lst.options[0]){
			frm.s_grade.value = frm.s_grade_lst.options[0].value;
		}

		if(frm.s_grade && frm.usr_group_lst.options[0]){
			frm.ent_id.value = frm.usr_group_lst.options[0].value;
		}

		if(frm.s_idc_fcs && frm.s_idc_fcs_lst.options[0]){
			frm.s_idc_fcs.value = frm.s_idc_fcs_lst.options[0].value;
		}

		if(frm.s_idc_int && frm.s_idc_int_lst.options[0]){
			frm.s_idc_int.value = frm.s_idc_int_lst.options[0].value;
		}

		if(frm.s_role_types){
			frm.s_role_types.value = _wbUserGroupAdvSearchRoleLst(frm);
		}
	}
	if(frm.s_itm_title){
		frm.s_itm_title.value = wbUtilsTrimString(frm.s_itm_title.value);
	}
	if(frm.is_whole_word_match_itm_title.checked){
		frm.s_is_whole_word_match_itm_title.value='true';
	}
	if(frm.s_ils_title){
		frm.s_ils_title.value = wbUtilsTrimString(frm.s_ils_title.value);
	}
	if(frm.is_whole_word_match_ils_title.checked){
		frm.s_is_whole_word_match_ils_title.value='true';
	}
	frm.action = wb_utils_servlet_url;
	frm.stylesheet.value = 'instr_adv_search_result.xsl';
	frm.cmd.value = 'search_ent_lst';
	frm.method = "get";
	frm.submit();
}
function wbUserGroupSearchExec(frm, lang){
	if(frm.s_usr_id_display_bil){
		frm.s_usr_id_display_bil.value = wbUtilsTrimString(frm.s_usr_id_display_bil.value);
	}

	if(!wbUtilsValidateEmptyField(frm.s_usr_id_display_bil, eval('wb_msg_' + lang + '_search_field'), lang)){
		frm.s_usr_id_display_bil.focus()
		return;
	}else{
		frm.cmd.value = 'search_ent_lst'
		frm.stylesheet.value = 'usr_search_result.xsl'
		frm.action = wb_utils_servlet_url
		frm.method = 'get'
		frm.submit()
	}
}

function wbUserGroupSearchResult(ent_id, search_time, sort_by, order_by, page, page_size, s_usr_id_display_bil){
	if (sort_by == null && order_by == null && page == null) {
	    var url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 'ent_id', ent_id, 's_timestamp', search_time, 'stylesheet', 'usr_search_result.xsl');
	} else {
	    var statusValue = getUrlParam('s_status').toUpperCase();
	    if (statusValue == 'DELETED') {
	        var url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 'page', page, 's_sort_by', sort_by, 's_order_by', order_by, 's_timestamp', search_time, 'page_size', page_size, 'stylesheet', 'usr_search_result.xsl', 's_status', statusValue,'filter_user_group',1);
	    } else {
	        var url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 'ent_id', ent_id, 'page', page, 's_sort_by', sort_by, 's_order_by', order_by, 's_timestamp', search_time, 'page_size', page_size, 's_usr_id_display_bil', s_usr_id_display_bil, 'stylesheet', 'usr_search_result.xsl','filter_user_group',1);
	    }
	}
	window.location.href = url
}

//==================================== wbUserGroupUtils ===================================
function wbUserGroupUtilsCopy(parent_id, child_id, type, role, lang){
	alert(eval('wb_msg_' + lang + '_copy_ent'))
	wb_utils_set_cookie('ent_id_copy', child_id)
	wb_utils_set_cookie('ent_id_parent', parent_id)
	wb_utils_set_cookie('ent_id_type', type)
}

function wbUserGroupUtilsCut(parent_id, child_id, type, role, lang){
	alert(eval('wb_msg_' + lang + '_cut_ent'))
	wb_utils_set_cookie('ent_id_copy', child_id)
	wb_utils_set_cookie('ent_id_parent', parent_id)
	wb_utils_set_cookie('ent_id_type', type)
}

function wbUserGroupUtilsPaste(parent_id, parent_role, root_ent_id, lang){
	var ent_id_parent = new Array()
	var ent_id_parent_str = new String(wb_utils_get_cookie('ent_id_parent'))
	var ent_id_lst = wb_utils_get_cookie('ent_id_copy');
	var ent_id_parent = ent_id_parent_str.split(',')
	var ent_id_type = wb_utils_get_cookie('ent_id_type')
	var url_success = self.location.href
	var url_failure = self.location.href

	if(ent_id_lst != ''){
		if(!_wbUserGroupPasteValidateEntIdParent(parent_id, ent_id_parent)){
			alert(eval('wb_msg_' + lang + '_paste_same_grp'))
		}else{
			//var url = '../servlet/qdb.qdbAction?env=qdb_dev&cmd=paste_ent' + '&ent_id=' +  ent_id + '&ent_id_parent=' + parent_id + '&url_success=' + escape(url_success) + '&url_failure=' + escape(url_failure) ;
			url = wb_utils_invoke_servlet('cmd', 'paste_ent', 'ent_id_lst', ent_id_lst, 'ent_id_parent', parent_id, 'url_success', url_success, 'url_failure', url_failure)
			window.location.href = url
		}
	}else{
		alert(eval('wb_msg_' + lang + '_cut_usr'));
	}

	wb_utils_set_cookie('ent_id_parent', '');
	wb_utils_set_cookie('ent_id_copy', '');
	wb_utils_set_cookie('ent_id_type', '');
}

//==================================== wbUserGroupRegister ===================================
function wbUserGuestRegisterUserPrep(enrol_itm_id, site_id, label_lan, style){
	var url = wbUserGuestRegisterUserPrepUrl(enrol_itm_id, site_id, label_lan, style)
	window.location.href = url;
}

function wbUserGuestRegisterUserPrepUrl(enrol_itm_id, site_id, label_lan, style){
	if(site_id == null || site_id == ""){
		site_id = '1';
	}

	if(style == null || style == ""){
		style = 'cw';
	}

	if(label_lan == null || label_lan == ""){
		label_lan = 'ISO-8859-1';
	}
	
	//var url_success = wbUserRegisterUserPrepUrl();
	var url_success = "../app/user/register";

	var url = wb_utils_invoke_disp_servlet('module', 'login.LoginModule', 'cmd', 'guest_login', 'site_id', site_id, 'style', style, 'label_lan', label_lan, 'url_success', url_success)
	return url;
}

function wbUserRegisterUserPrep(enrol_itm_id){
	var url = wbUserRegisterUserPrepUrl(enrol_itm_id)
	window.location.href = url;
}

function wbUserRegisterUserPrepUrl(enrol_itm_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_self_reg_form', 'stylesheet', 'usr_detail_register.xsl')

	if(enrol_itm_id != null && enrol_itm_id != ''){
		url += '&enrol_itm_id=' + enrol_itm_id;
	}
	return url;
}

function wbUserRegisterUserExec(frm, parent_id, lang){
	if(_wbUserGroupInsUserValidateFrm(frm, lang)){
		alert(frm.usr_display_bil.value)
		return;
		frm.cmd.value = 'register_usr'
		frm.url_success.value = '../login/index.htm'
		frm.url_failure.value = wbUserRegisterUserPrepUrl()
		frm.method = 'post'
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

function wbUserRegisterUserApprovalLst(cur_page, page_size, sort_col, sort_order, timestamp){
	var url = wbUserRegisterUserApprovalLstUrl(cur_page, page_size, sort_col, sort_order, timestamp)
	//window.location.href = url;
	top.location.href = url;
}

function wbUserRegisterUserApprovalLstUrl(cur_page, page_size, sort_col, sort_order, timestamp){
	if(sort_col == null || sort_col == ''){
		sort_col = 'usr_signup_date';
	}

	if(sort_order == null || sort_order == ''){
		sort_order = 'ASC';
	}

	var url = wb_utils_invoke_servlet('cmd', 'search_ent_lst', 's_sort_by', sort_col, 's_order_by', sort_order, 's_status', 'PENDING', 'stylesheet', 'usr_reg_approval_lst.xsl', 'filter_user_group', '1')
	return url;
}

function wbUserRegisterUserApproval(usr_ent_id){
	var url = wbUserRegisterUserApprovalUrl(usr_ent_id)
	window.location.href = url;
}

function wbUserRegisterUserDisapproval(usr_ent_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', usr_ent_id, 'stylesheet', 'usr_reg_disapproval.xsl')
	window.location.href = url;
}

function wbUserRegisterUserApprovalUrl(usr_ent_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', usr_ent_id, 'stylesheet', 'usr_reg_approval.xsl')
	return url;
}

function wbUserRegisterUserApprovalExec(frm, usr_ent_id, approve_action, lang){
	if(_wbUserGroupInsUserValidateFrm(frm, lang)){
		if (frm.usr_approve_reason) {
			frm.usr_approve_reason.value = wbUtilsTrimString(frm.usr_approve_reason.value);
		}
		if(approve_action == 'disappr'){
			if(!gen_validate_empty_field(frm.usr_approve_reason, eval('wb_msg_' + lang + '_appr_comment'), lang)){
				return;
			};
		}

		if( getChars(frm.usr_approve_reason.value) > 800){
			alert(eval('wb_msg_' + lang + '_enter_valid_appr_comment'))
			frm.usr_approve_reason.focus();
			return;
		}

		if(approve_action == 'appr'){
			frm.cmd.value = 'appr_usr'
			frm.msg_subject.value = frm.msg_subject_approve.value
		}else{
			frm.cmd.value = 'disappr_usr'
			frm.msg_subject.value = frm.msg_subject_disapprove.value
		}

		frm.url_success.value = wbUserRegisterUserApprovalLstUrl()
		frm.url_failure.value = wbUserRegisterUserApprovalUrl(usr_ent_id)
		frm.method = 'post'
		frm.action = wb_utils_servlet_url
		frm.submit()
	}
}

//==================================== wbUserGroupActivate ===================================
function wbUserActivateReactivateLst(cur_page, page_size, sort_col, sort_order, timestamp){
	var url = wbUserActivateReactivateLstUrl(cur_page, page_size, sort_col, sort_order, timestamp);
	//window.location.href = url;
	top.location.href = url;
}

function wbUserActivateReactivateLstUrl(cur_page, page_size, sort_col, sort_order, timestamp){
	if(sort_col == null || sort_col == ''){
		sort_col = 'usr_display_bil';
	}

	if(sort_order == null || sort_order == ''){
		sort_order = 'ASC';
	}

	if(cur_page == null || cur_page == ''){
		cur_page = '1';
	}

	if(page_size == null || page_size == ''){
		page_size = '10';
	}

	if(timestamp == null || timestamp == ''){
		timestamp = '';
	}

	var url = wb_utils_invoke_servlet('cmd', 'get_suspense_usr', 'page', cur_page, 'pagesize', page_size, 's_sort_by', sort_col, 's_order_by', sort_order, 'stylesheet', 'usr_reactivate_lst.xsl')
	return url;
}

function wbUserActivateReactivatePrep(ent_id){
	var url = wbUserActivateReactivatePrepUrl(ent_id);
	window.location.href = url;
}

function wbUserActivateReactivatePrepUrl(ent_id){
	var url = wb_utils_invoke_servlet('cmd', 'get_usr', 'usr_ent_id', ent_id, 'stylesheet', 'usr_reactivate.xsl');
	return url;
}

function wbUserActivateReactivateExec(frm, ent_id){
	frm.cmd.value = 'reactivate_usr'
	frm.ent_id_lst.value = ent_id
	frm.url_success.value = wbUserActivateReactivateLstUrl()
	frm.url_failure.value = wbUserActivateReactivateLstUrl()
	frm.method = 'post'
	frm.action = wb_utils_servlet_url
	frm.submit()
}

function wbUserGoupGetPopupUsrLst(frm, lang,auto_enroll_ind){
	var fld_name, id_lst, nm_lst, usr_id_lst, sel_opt, args, rawIdLst, close_opt, refresh_opt, disabled_opt, usr_lst_argv,auto_enroll_ind;
	usr_lst_argv = "";
	
	if(getUrlParam('sel_opt')){
		sel_opt = getUrlParam('sel_opt');
	}

	if(getUrlParam('fld')){
		fld_name = getUrlParam('fld');
	}

	if(getUrlParam('close_opt')){
		close_opt = getUrlParam('close_opt');
	}

	if(getUrlParam('refresh_opt')){
		refresh_opt = getUrlParam('refresh_opt');
	}

	if(getUrlParam('disabled_opt')){
		disabled_opt = getUrlParam('disabled_opt');
	}

	if (sel_opt == '0') {
		id_lst = _wbUserGroupGetPopupEntIdSingle(frm)
		nm_lst = _wbUserGroupGetPopupEntNameSingle(frm)
		usr_id_lst = _wbUserGroupGetPopupUserIdSingle(frm)
	} else {
		id_lst = _wbUserGroupGetPopupEntId(frm)

		nm_lst = _wbUserGroupGetPopupEntName(frm)
		usr_lst_argv = _wbUserGroupGetPopupUsrArgv(frm)
		usr_id_lst = _wbUserGroupGetPopupUserId(frm)
	}
	if (id_lst == '') {
		Dialog.alert(eval('wb_msg_' + lang + '_sel_usr'));
	} else if (sel_opt == '0') {
		rawIdLst = new Array()

		rawIdLst = id_lst.split("~%~")
		if (rawIdLst.length != 1) {
			alert(eval('wb_msg_' + lang + '_sel_one_usr'))
		} else {
			if (window.opener != null) {
				if (window.opener.getPopupUsrLst != null) {
					window.opener.getPopupUsrLst(fld_name, id_lst, nm_lst, usr_lst_argv, auto_enroll_ind, usr_id_lst, lang,true)
					if (refresh_opt == '1') {
						window.location.reload();
					} else {
						if (close_opt == '1') {
							self.close();
						} else {
							_wbUserGroupClearPopupEntChkbox(frm);
							if (disabled_opt == '1') {
								_wbUserGroupDisablePopupEntChkBox(frm);
							}
						}
					}
				}
			}
		}
	} else {
		if (window.opener != null) {
			if (window.opener.getPopupUsrLst != null) {
				window.opener.getPopupUsrLst(fld_name, id_lst, nm_lst, usr_lst_argv, auto_enroll_ind, usr_id_lst, lang)
				if (refresh_opt == '1') {
					window.location.reload()
				} else {
					if (close_opt == '1') {
						self.close();
					} else {
						_wbUserGroupClearPopupEntChkbox(frm);
						if (disabled_opt == '1') {
							_wbUserGroupDisablePopupEntChkBox(frm);
						}
						self.close();
					}
				}
			}
		}
	}
}

/* validate function*/
function _wbUserGroupValidateFrm(frm, lang){
	var lab_group = frm.lab_group ? frm.lab_group.value : 'Group(sys)'
	
	if (frm.ent_ste_uid) {
		frm.ent_ste_uid.value = wbUtilsTrimString(frm.ent_ste_uid.value);
	}
	
	if (frm.usg_display_bil) {
		frm.usg_display_bil.value = wbUtilsTrimString(frm.usg_display_bil.value);
	}
	
	if(!wbUtilsValidateEmptyField(frm.ent_ste_uid, lab_group + wb_msg_usr_code, lang)){
		return false;
	}

	if(!wbUtilsValidateEmptyField(frm.usg_display_bil, wb_msg_usr_title, lang)){
		return false;
	}

	if(frm.ent_ste_uid.value.search(/,/)!=-1 ||frm.ent_ste_uid.value.search(/，/)!=-1){
		alert(wb_msg_usr_enter_vaild + wb_msg_usr_code)
		return false;
	}
	
	if(getChars(frm.ent_ste_uid.value)>80){
		alert(wb_msg_usr_code_too_long)
		return false;
	}

	if(frm.usg_display_bil.value.search(/\//)!=-1 || frm.usg_display_bil.value.search(/\\/) != -1 || frm.usg_display_bil.value.search(/\"/) != -1){
		alert(wb_msg_usr_enter_vaild + wb_msg_usr_title)
		return false;
	}
	
	if(getChars(frm.usg_display_bil.value)>80){
		alert(wb_msg_usr_title_too_long)
		return false;
	}
	
	if(frm.ent_syn_ind_chk){
		if(frm.ent_syn_ind_chk.checked){
			frm.ent_syn_ind.value = "false";
		}else{
			frm.ent_syn_ind.value = "true";
		}
	}

	if(frm.usg_desc){
		frm.usg_desc.value = wbUtilsTrimString(frm.usg_desc.value);
		if(getChars(frm.usg_desc.value) > 800){
			alert(wb_msg_usr_desc_too_long);

			frm.usg_desc.focus();
			return false;
		}
	}
	return true;
}

function _wbUserGroupValidateSearchFrm(frm, lang){
	if(frm.s_usr_id){
		if(frm.s_usr_id.value != ''){
			if(!gen_validate_usr_id(frm.s_usr_id, lang)){
				return false;
			}
		}
	}

    //birth day
	if(frm.bday_from_yy && frm.bday_from_mm && frm.bday_from_dd){
	    if (frm.bday_from_yy.value != '' || frm.bday_from_mm.value != '' || frm.bday_from_dd.value != '') {
	        if (!wbUtilsValidateDate("document." + frm.name + ".bday_from", frm.lab_dis_name_bday.value)) {
	            return;
	        }
	    }
		if(frm.bday_from_yy.value != '' && frm.bday_from_mm.value != '' && frm.bday_from_dd.value != '') {
		    frm.s_usr_bday_fr.value = frm.bday_from_yy.value + "-" + frm.bday_from_mm.value + "-" + frm.bday_from_dd.value + " 00:00:00";
	    }
	}

	if(frm.bday_to_yy && frm.bday_to_mm && frm.bday_to_dd){
	    if (frm.bday_to_yy.value != '' || frm.bday_to_mm.value != '' || frm.bday_to_dd.value != '') {
	        if (!wbUtilsValidateDate("document." + frm.name + ".bday_to", frm.lab_dis_name_bday.value)) {
	            return;
	        }
	    }
		if(frm.bday_to_yy.value != '' && frm.bday_to_mm.value != '' && frm.bday_to_dd.value != '') {
		    frm.s_usr_bday_to.value = frm.bday_to_yy.value + "-" + frm.bday_to_mm.value + "-" + frm.bday_to_dd.value + " 00:00:00";
	    }
	}
	
	
	if(frm.bday_from_yy && frm.bday_from_mm && frm.bday_from_dd && frm.bday_to_yy && frm.bday_to_mm && frm.bday_to_dd && frm.bday_from_yy.value != '' && frm.bday_to_yy.value != '') {
		if(!wb_utils_validate_date_compare({
			frm : 'document.' + frm.name, 
			start_obj : 'bday_from', 
			end_obj : 'bday_to'
			})) {
			return false;
		}
	}
	
	//join day
	if(frm.jday_from_yy && frm.jday_from_mm && frm.jday_from_dd){
	    if (frm.jday_from_yy.value != '' || frm.jday_from_mm.value != '' || frm.jday_from_dd.value != '') {
	        if (!wbUtilsValidateDate("document." + frm.name + ".jday_from", frm.lab_dis_name_jday.value)) {
	            return;
	        }
	    }
		if(frm.jday_from_yy.value != '' && frm.jday_from_mm.value != '' && frm.jday_from_dd.value != '') {
		    frm.s_usr_jday_fr.value = frm.jday_from_yy.value + "-" + frm.jday_from_mm.value + "-" + frm.jday_from_dd.value + " 00:00:00";
	    }
	}

	if(frm.jday_to_yy && frm.jday_to_mm && frm.jday_to_dd){
	    if (frm.jday_to_yy.value != '' || frm.jday_to_mm.value != '' || frm.jday_to_dd.value != '') {
	        if (!wbUtilsValidateDate("document." + frm.name + ".jday_to", frm.lab_dis_name_jday.value)) {
	            return;
	        }
	    }
		if(frm.jday_to_yy.value != '' && frm.jday_to_mm.value != '' && frm.jday_to_dd.value != '') {
		    frm.s_usr_jday_to.value = frm.jday_to_yy.value + "-" + frm.jday_to_mm.value + "-" + frm.jday_to_dd.value + " 00:00:00";
	    }
	}
	
	if(frm.jday_from_yy && frm.jday_from_mm && frm.jday_from_dd && frm.jday_to_yy && frm.jday_to_mm && frm.jday_to_dd && frm.jday_from_yy.value != '' && frm.jday_to_yy.value != '') {
		if(!wb_utils_validate_date_compare({
			frm : 'document.' + frm.name, 
			start_obj : 'jday_from', 
			end_obj : 'jday_to'
			})) {
			return false;
		}
	}
	
	//extension date type 
	for(i=0;i<=40;i++) {
	    var ex_yy = eval('frm.extension_' + i + '_from_yy');
	    var ex_mm = eval('frm.extension_' + i + '_from_mm');
	    var ex_dd = eval('frm.extension_' + i + '_from_dd');
	    if (ex_yy && ex_mm && ex_dd) {
		    if(ex_yy.value != '' || ex_mm.value != '' || ex_dd.value !='') {
    	        if (!wbUtilsValidateDate("document." + frm.name + ".extension_" + i + "_from", eval('frm.extension_' + i + '.value'))) {
    	            return;
    	        }
		    }
		    if(ex_yy.value != '' && ex_mm.value != '' && ex_dd.value !='') {
		        var s_ext_fr = eval('frm.s_ext_extension_' + i + '_fr');
		        s_ext_fr.value = ex_yy.value + "-" + ex_mm.value + "-" + ex_dd.value + " 00:00:00";
		    }
	    }
	}

	for(i=0;i<=40;i++) {
	    var ex_yy = eval('frm.extension_' + i + '_to_yy');
	    var ex_mm = eval('frm.extension_' + i + '_to_mm');
	    var ex_dd = eval('frm.extension_' + i + '_to_dd');
	    if (ex_yy && ex_mm && ex_dd) {
		    if(ex_yy.value != '' || ex_mm.value != '' || ex_dd.value !='') {
    	        if (!wbUtilsValidateDate("document." + frm.name + ".extension_" + i + "_to", eval('frm.extension_' + i + '.value'))) {
    	            return;
    	        }
		    }
		    if(ex_yy.value != '' && ex_mm.value != '' && ex_dd.value !='') {
		        var s_ext_to = eval('frm.s_ext_extension_' + i + '_to');
		        s_ext_to.value = ex_yy.value + "-" + ex_mm.value + "-" + ex_dd.value + " 00:00:00";
		    }
	    }
	}


	if(frm.s_status){
		frm.s_status.value = _wbUserGroupGetUserSStatusLst(frm)
		if(frm.s_status.value == ""){
			alert(eval('wb_msg_' + lang + '_sel_usr_s_status'))
			return false;
		}
	}
	return true;
}

//=====================================================================================
/* private functions */
function _wbUserGroupInsUserValidateFrm(frm, lang){
	//usr_id
	if(frm.usr_id && frm.usr_id_req_fld && frm.usr_id_req_fld.value == 'true'){
		var lab_login_id = frm.lab_login_id ? frm.lab_login_id.value : 'Login ID(sys)'
		if(!wbUtilsValidateUserId(frm.usr_id, lab_login_id, frm.usr_id_min_length.value, frm.usr_id_max_length.value)){

			return false;
		}
		;
	}
	return _wbUserGroupUpdUserValidateFrm(frm, lang)
}

function _wbUserGroupUpdUserValidateFrm(frm, lang){

	//usr_pwd
	if(frm.usr_pwd && frm.usr_pwd_req_fld && frm.usr_pwd_req_fld.value == 'true'){
		var lab_passwd = frm.lab_passwd ? frm.lab_passwd.value : 'Password(sys)'
		if(!wbUtilsValidateUserPassword(frm.usr_pwd, lab_passwd, frm.pwd_min_length.value, frm.pwd_max_length.value)){
			return false;
		}else if(!/^(?=.*[A-Za-z]+)(?=.*[0-9]+)[A-Za-z0-9_-]*$/.test(frm.usr_pwd.value)){
			alert(wb_msg_usr_enter_valid + lab_passwd)
			return false;
		}
	}

	//confirm_usr_pwd
	if(frm.usr_pwd && frm.confirm_usr_pwd && frm.confirm_usr_pwd_req_fld && frm.confirm_usr_pwd_req_fld.value == 'true'){
		var lab_passwd = frm.lab_passwd ? frm.lab_passwd.value : 'Password(sys)';
		var lab_con_passwd = frm.lab_con_passwd ? frm.lab_con_passwd.value : "Confirm Password(sys)";
		if(''==frm.confirm_usr_pwd.value){
			alert(wb_msg_usr_please_enter_the+'"' + lab_con_passwd + '"')
			frm.confirm_usr_pwd.focus();
			return false;
		}
		if(frm.usr_pwd.value != frm.confirm_usr_pwd.value){
			alert('"' + lab_con_passwd + '"' + wb_msg_usr_retyped_passwd_1 + '"' + lab_passwd + '"' + wb_msg_usr_retyped_passwd_2)

			frm.confirm_usr_pwd.value = '';
			frm.confirm_usr_pwd.focus()
			return false;
		}
	}

	//usr_pwd_need_change_ind
	if(frm.usr_pwd_need_change_ind_chk && frm.usr_pwd_need_change_ind){
		if(frm.usr_pwd_need_change_ind_chk.checked == true){
			frm.usr_pwd_need_change_ind.value = 'true';
		}else{
			frm.usr_pwd_need_change_ind.value = 'false';
		}
	}
	//usr_diplay_bil
	if (frm.usr_display_bil) {
		frm.usr_display_bil.value = wbUtilsTrimString(frm.usr_display_bil.value);
		if (frm.usr_display_bil_req_fld && frm.usr_display_bil_req_fld.value == 'true' && frm.usr_display_bil.type == 'text') {
			var lab_dis_name = frm.lab_dis_name ? frm.lab_dis_name.value : 'Display Name(sys)'
			if (!wbUtilsValidateEmptyField(frm.usr_display_bil, lab_dis_name)) {
				return false;
			}
		}
	}

	if(frm.usr_display_bil && frm.usr_display_bil.value != null && frm.usr_display_bil.value != undefined)
	{
		var lab_dis_name = frm.lab_dis_name ? frm.lab_dis_name.value : 'Display Name(sys)'
		if (!wbUtilsMaxOrMinLength(frm.usr_display_bil, lab_dis_name,'',80,lang)) {
			return false;
		}
	}
	
	if(frm.usr_nickname && frm.usr_nickname.value != null && frm.usr_nickname.value != undefined)
	{
		var lab_nickname = frm.lab_nickname ? frm.lab_nickname.value : 'Nickname(sys)'
		if (!wbUtilsMaxOrMinLength(frm.usr_nickname, lab_nickname,'',80,lang)) {
			return false;
		}
	}
	//var lab_dis_name_length = getChars(wbUtilsTrimString(frm.usr_display_bil.value));
	
	
	//usr_gender
	if(frm.usr_gender && frm.usr_gender_req_fld && frm.usr_gender_req_fld.value == 'true'){
		var lab_gender = frm.lab_gender ? frm.lab_gender.value : 'Gender(sys)'
		if(frm.usr_gender[0].checked == false && frm.usr_gender[1].checked == false){
			alert(wb_msg_usr_please_select_a + lab_gender)
			return false;
		}
	}

	//usr_bday
	if(frm.bday_yy && frm.bday_mm && frm.bday_dd && frm.usr_bday){
		var lab_bday = frm.lab_bday ? frm.lab_bday.value : 'Date of Birth(sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.bday_yy.value, Number(frm.bday_mm.value) - 1, Number(frm.bday_dd.value))
		if(frm.bday_dd.value == "" && frm.bday_mm.value == "" && frm.bday_yy.value == ""){
			if(frm.usr_bday_req_fld && frm.usr_bday_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.bday', lab_bday, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.bday', lab_bday, '')){
				return false;
			}else if(_d_date >= _cur_date){
				alert(wb_msg_usr_enter_vaild + lab_bday)

				frm.bday_dd.focus();
				return false;
			}else{
				frm.usr_bday.value = frm.bday_yy.value + "-" + frm.bday_mm.value + "-" + frm.bday_dd.value + " " + "00:00:00";
			}
		}
	}

	//usr_email
	if(frm.usr_email){
		var lab_e_mail = frm.lab_e_mail ? frm.lab_e_mail.value : 'Email(sys)'
		frm.usr_email.value = wbUtilsTrimString(frm.usr_email.value);
		if(frm.usr_email_req_fld && frm.usr_email_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_email, lab_e_mail)){
				return false;
			}
		}

		if(frm.usr_email.value != '' && !wbUtilsValidateEmail(frm.usr_email, lab_e_mail)){
			frm.usr_email.focus()
			return false;
		}
	}

	//usr_tel_1
	if(frm.usr_tel_1){
		var lab_tel_1 = frm.lab_tel_1 ? frm.lab_tel_1.value : 'Phone (sys)'
		frm.usr_tel_1.value = wbUtilsTrimString(frm.usr_tel_1.value);
		//for full telephone format Country-Area-Number
		if(frm.usr_tel_1_country && frm.usr_tel_1_area && frm.usr_tel_1_number){
			if(frm.usr_tel_1_country.value != ''){
				if(!wbUtilsValidatePositiveInteger(frm.usr_tel_1_country, lab_tel_1)){
					frm.usr_tel_1_country.focus()
					return false;
				}
			}

			if(frm.usr_tel_1_area.value != ''){
				if(!wbUtilsValidatePositiveInteger(frm.usr_tel_1_area, lab_tel_1)){
					frm.usr_tel_1_area.focus()
					return false;
				}
			}

			if(frm.usr_tel_1_number.value != ''){
				if(!wbUtilsValidatePositiveInteger(frm.usr_tel_1_number, lab_tel_1)){
					frm.usr_tel_1_number.focus()
					return false;
				}
			}

			if(frm.usr_tel_1_country.value != '' || frm.usr_tel_1_area.value != '' || frm.usr_tel_1_number.value != ''){
				if(!wbUtilsValidateEmptyField(frm.usr_tel_1_country, lab_tel_1)){
					return false;
				}

				if(!wbUtilsValidateEmptyField(frm.usr_tel_1_number, lab_tel_1)){
					return false;
				}
			}

			if(frm.usr_tel_1_country.value != ''){
				frm.usr_tel_1.value = frm.usr_tel_1_country.value;
			}else{
				frm.usr_tel_1.value = 'n/a';
			}

			frm.usr_tel_1.value += '-'

			if(frm.usr_tel_1_area.value != ''){
				frm.usr_tel_1.value += frm.usr_tel_1_area.value;
			}else{
				frm.usr_tel_1.value += 'n/a';
			}

			frm.usr_tel_1.value += '-'
			if(frm.usr_tel_1_number.value != ''){
				frm.usr_tel_1.value += frm.usr_tel_1_number.value;
			}else{
				frm.usr_tel_1.value += 'n/a';
			}
		}
		//End for full telephone format Country-Area-Number
		if(frm.usr_tel_1_req_fld && frm.usr_tel_1_req_fld.value == 'true'){
			if(frm.usr_tel_1_country && frm.usr_tel_1_area && frm.usr_tel_1_number){
				if(frm.usr_tel_1.value == 'n/a-n/a-n/a'){
					alert(wb_msg_usr_enter_valid + lab_tel_1)

					frm.usr_tel_1_country.focus()
					return false;
				}
			}else{
				if(!wbUtilsValidateEmptyField(frm.usr_tel_1, lab_tel_1)){
					return false;
				}
			}
		}

	/*if (!frm.usr_tel_1_country && !frm.usr_tel_1_area && !frm.usr_tel_1_number){
	if (frm.usr_tel_1.value.search(/[^0-9\-()]/) != -1){
		alert(wb_msg_usr_enter_valid + lab_tel_1)
		frm.usr_tel_1.focus();
		return false;
	}
	}*/
	}

	//usr_fax_1
	if(frm.usr_fax_1){
		var lab_fax_1 = frm.lab_fax_1 ? frm.lab_fax_1.value : 'Fax (sys)'
		frm.usr_fax_1.value = wbUtilsTrimString(frm.usr_fax_1.value);
		//for full fax format Country-Area-Number
		if(frm.usr_fax_1_country && frm.usr_fax_1_area && frm.usr_fax_1_number){
			if(frm.usr_fax_1_country.value != ''){
				if(!wbUtilsValidatePositiveInteger(frm.usr_fax_1_country, lab_fax_1)){
					frm.usr_fax_1_country.focus()
					return false;
				}
			}

			if(frm.usr_fax_1_area.value != ''){
				if(!wbUtilsValidatePositiveInteger(frm.usr_fax_1_area, lab_fax_1)){
					frm.usr_fax_1_area.focus()
					return false;
				}
			}

			if(frm.usr_fax_1_number.value != ''){
				if(!wbUtilsValidatePositiveInteger(frm.usr_fax_1_number, lab_fax_1)){
					frm.usr_fax_1_number.focus()
					return false;
				}
			}

			if(frm.usr_fax_1_country.value != '' || frm.usr_fax_1_area.value != '' || frm.usr_fax_1_number.value != ''){
				if(!wbUtilsValidateEmptyField(frm.usr_fax_1_country, lab_fax_1)){
					return false;
				}

				if(!wbUtilsValidateEmptyField(frm.usr_fax_1_number, lab_fax_1)){
					return false;
				}
			}

			if(frm.usr_fax_1_country.value != ''){
				frm.usr_fax_1.value = frm.usr_fax_1_country.value;
			}else{
				frm.usr_fax_1.value = 'n/a';
			}

			frm.usr_fax_1.value += '-'

			if(frm.usr_fax_1_area.value != ''){
				frm.usr_fax_1.value += frm.usr_fax_1_area.value;
			}else{
				frm.usr_fax_1.value += 'n/a';
			}

			frm.usr_fax_1.value += '-'
			if(frm.usr_fax_1_number.value != ''){
				frm.usr_fax_1.value += frm.usr_fax_1_number.value;
			}else{
				frm.usr_fax_1.value += 'n/a';
			}
		}
		//End for full fax format Country-Area-Number
		if(frm.usr_fax_1_req_fld && frm.usr_fax_1_req_fld.value == 'true'){
			if(frm.usr_fax_1_country && frm.fax_tel_1_area && frm.usr_fax_1_number){
				if(frm.usr_fax_1.value == 'n/a-n/a-n/a'){
					alert(wb_msg_usr_enter_valid + lab_fax_1)

					frm.usr_fax_1_country.focus()
					return false;
				}
			}else{
				if(!wbUtilsValidateEmptyField(frm.usr_fax_1, lab_fax_1)){
					return false;
				}
			}
		}
	/*
if (!frm.usr_fax_1_country && !frm.usr_fax_1_area && !frm.usr_fax_1_number){
	if (frm.usr_fax_1.value.search(/[^0-9\-()]/) != -1){
		alert(wb_msg_usr_enter_valid + lab_fax_1)
		frm.usr_fax_1.focus();
		return false;
	}
}*/
	}

	//usr_job_title
	if(frm.usr_job_title) {
		frm.usr_job_title.value = wbUtilsTrimString(frm.usr_job_title.value);
		if (frm.usr_job_title_req_fld && frm.usr_job_title_req_fld.value == 'true' && frm.usr_job_title.type == 'text') {
			var lab_job_title = frm.lab_job_title ? frm.lab_job_title.value : 'Job Title(sys)'
			
			if (!wbUtilsValidateEmptyField(frm.usr_job_title, lab_job_title)) {
				return false;
			}
		}
		if (getChars(frm.usr_job_title.value) > 80) {
			var lab_job_title = frm.lab_job_title ? frm.lab_job_title.value : 'Job Title(sys)'
			alert(lab_job_title + wb_msg_gb_too_long + ', ' + wb_msg_gb_word_limit + ' 80 '  + eval('wb_msg_'+lang+'_characters'))
			frm.usr_job_title.focus()
			return false;
		}
	}

	//ent_syn_ind
	if(frm.ent_syn_ind_chk){
		if(frm.ent_syn_ind_chk.checked){
			frm.ent_syn_ind.value = "false";
		}else{
			frm.ent_syn_ind.value = "true";
		}
	}

	//ent_syn_rol_ind
	if(frm.ent_syn_rol_ind_chk){
		if(frm.ent_syn_rol_ind_chk.checked){
			frm.ent_syn_rol_ind.value = "false";
		}else{
			frm.ent_syn_rol_ind.value = "true";
		}
	}

	//usr_grade_lst
	if(frm.usr_grade_lst_single){
		var lab_grade = frm.lab_grade ? frm.lab_grade.value : 'Grade(sys)'
		if(frm.usr_grade_lst_single.value == '' && frm.usr_grade_lst_req_fld && frm.usr_grade_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_grade)
			return false;
		}
	}

	//usr_group_lst
	if(frm.usr_grade_lst || frm.usr_group_lst){
		var lab_group = frm.lab_group ? frm.lab_group.value : 'Group(sys)'

		//for real goldenman
		if(frm.usr_group_lst && frm.usr_group_lst_req_fld && frm.usr_group_lst_req_fld.value == 'true' && frm.usr_group_lst.length == 0){
			alert(wb_msg_usr_please_select_a + lab_group)
			frm.usr_group_lst.focus()
			return false;
		}

		//for single goldenman
		if(frm.usr_group_lst && frm.usr_group_lst_single && frm.usr_group_lst_req_fld.value == 'true' && frm.usr_group_lst.options[0].value == ''){
			alert(wb_msg_usr_please_select_a + lab_group)
			frm.usr_group_lst_single.focus()
			return false;
		}

		frm.usr_attribute_relation_type_lst.value = _wbUserGroupGetUserAttbRelationLst(frm, 'usr_grade_lst', 'usr_group_lst', 'usr_spec_ind_sector_lst', 'usr_ind_sector_focus_lst', 'classification_type');
		frm.usr_attribute_ent_id_lst.value = _wbUserGroupGetUserAttbEntIdLst(frm, 'usr_grade_lst', 'usr_group_lst', 'usr_spec_ind_sector_lst', 'usr_ind_sector_focus_lst', 'classification_type');
	}

	//Direct Supervisor
	if(frm.direct_supervisor_ent){
		var lab_direct_supervisors = frm.lab_direct_supervisors ? frm.lab_direct_supervisors.value : 'Direct Supervisors(sys)'

		if(frm.direct_supervisor_ent_lst && frm.direct_supervisor_ent_lst_reg_fld && frm.direct_supervisor_ent_lst_reg_fld.value == 'true' && frm.direct_supervisor_ent.length == 0){
			alert(wb_msg_usr_please_select_a + lab_direct_supervisors)
			frm.direct_supervisor_ent.focus()
			return false;
		}
		_wbUserGroupGetUserDirectSupervisorLst(frm);
	}

	//usr_join_date
	if(frm.join_date_yy && frm.join_date_mm && frm.join_date_dd && frm.usr_join_date){
		var lab_join_date = frm.lab_join_date ? frm.lab_join_date.value : 'Join Date(sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.join_date_yy.value, Number(frm.join_date_mm.value) - 1, Number(frm.join_date_dd.value))
		if(frm.join_date_dd.value == "" && frm.join_date_mm.value == "" && frm.join_date_yy.value == ""){
			if(frm.usr_join_date_req_fld && frm.usr_join_date_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.join_date', lab_join_date, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.join_date', lab_join_date, '')){
				return false;
			}else if(_d_date >= _cur_date){
				alert(wb_msg_usr_enter_vaild + lab_join_date)

				frm.join_date_dd.focus();
				return false;
			}else{
				frm.usr_join_date.value = frm.join_date_yy.value + "-" + frm.join_date_mm.value + "-" + frm.join_date_dd.value + " " + "00:00:00";
			}
		}
	}

	//usr_role_lst
	if(frm.usr_role_lst && frm.usr_role){
		var lab_role = frm.lab_role ? frm.lab_role.value : 'Role (sys)'
		frm.usr_role_lst.value = _wbUserGroupGetUserRoleLst(frm, lang)
		frm.usr_role_start_lst.value = _wbUserGroupGetUserRoleDateLst(frm, lang, 'start')
		frm.usr_role_end_lst.value = _wbUserGroupGetUserRoleDateLst(frm, lang, 'end')

		if(frm.usr_role_lst.value == "" && frm.usr_role_lst_req_fld && frm.usr_role_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_role)
			return false;
		}
		if(frm.usr_role_appr_lst){
			if(frm.usr_role_appr_lst != null && frm.usr_role_appr_lst.value != ''){
				frm.usr_role_lst.value += '~' + (frm.usr_role_appr_lst.value).substring(0, frm.usr_role_appr_lst.value.length - 1);

				frm.usr_role_start_lst.value += '~' + (frm.usr_role_appr_start_lst.value).substring(0, frm.usr_role_appr_start_lst.value.length - 1);
				frm.usr_role_end_lst.value += '~' + (frm.usr_role_appr_end_lst.value).substring(0, frm.usr_role_appr_end_lst.value.length - 1);
			}
		}
	}

	//entity assignment
	if(frm.entity_assignment_cnt){
		if(frm.entity_assignment_cnt.value >= 1){
			if(_wbUserGroupCheckEntityAssignment(frm, lang) == false){
				return false;
			}

			frm.rol_target_ext_id_lst.value = _wbUserGroupGetRolTargetExtIdLst(frm);
			frm.rol_target_ent_group_lst.value = _wbUserGroupGetRolTargetEntGroupLst(frm);
			frm.rol_target_start_lst.value = _wbUserGroupGetRolTargetDateLst(frm, 'start')
			frm.rol_target_end_lst.value = _wbUserGroupGetRolTargetDateLst(frm, 'end')
		}
	}

	if(frm.supervise_target_ent){
		_wbUserGroupGetUserSuperviseLst(frm);
	}
	
	//usr_extra_1
	if(frm.usr_extra_1){
		frm.usr_extra_1.value = wbUtilsTrimString(frm.usr_extra_1.value);
		var lab_extra_1 = frm.lab_extra_1 ? frm.lab_extra_1.value : 'Extra 1(sys)'
		if(frm.usr_extra_1_req_fld && frm.usr_extra_1_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_1, lab_extra_1)){
				return false;
			}
		}
	}

	//usr_extra_2
	if(frm.usr_extra_2){
		frm.usr_extra_2.value = wbUtilsTrimString(frm.usr_extra_2.value);
		var lab_extra_2 = frm.lab_extra_2 ? frm.lab_extra_2.value : 'Extra 2(sys)'
		if(frm.usr_extra_2_req_fld && frm.usr_extra_2_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_2, lab_extra_2)){
				return false;
			}
		}
	}

	//usr_extra_3
	if(frm.usr_extra_3){
		frm.usr_extra_3.value = wbUtilsTrimString(frm.usr_extra_3.value);
		var lab_extra_3 = frm.lab_extra_3 ? frm.lab_extra_3.value : 'Extra 3(sys)'
		if(frm.usr_extra_3_req_fld && frm.usr_extra_3_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_3, lab_extra_3)){
				return false;
			}
		}
	}

	//usr_extra_4
	if(frm.usr_extra_4){
		frm.usr_extra_4.value = wbUtilsTrimString(frm.usr_extra_4.value);
		var lab_extra_4 = frm.lab_extra_4 ? frm.lab_extra_4.value : 'Extra 4(sys)'
		if(frm.usr_extra_4_req_fld && frm.usr_extra_4_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_4, lab_extra_4)){
				return false;
			}
		}
	}

	//usr_extra_5
	if(frm.usr_extra_5){
		frm.usr_extra_5.value = wbUtilsTrimString(frm.usr_extra_5.value);
		var lab_extra_5 = frm.lab_extra_5 ? frm.lab_extra_5.value : 'Extra 5(sys)'
		if(frm.usr_extra_5_req_fld && frm.usr_extra_5_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_5, lab_extra_5)){
				return false;
			}
		}
	}

	//usr_extra_6
	if(frm.usr_extra_6){
		frm.usr_extra_6.value = wbUtilsTrimString(frm.usr_extra_6.value);
		var lab_extra_6 = frm.lab_extra_6 ? frm.lab_extra_6.value : 'Extra 6(sys)'
		if(frm.usr_extra_6_req_fld && frm.usr_extra_6_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_6, lab_extra_6)){
				return false;
			}
		}
	}

	//usr_extra_7
	if(frm.usr_extra_7){
		frm.usr_extra_7.value = wbUtilsTrimString(frm.usr_extra_7.value);
		var lab_extra_7 = frm.lab_extra_7 ? frm.lab_extra_7.value : 'Extra 7(sys)'
		if(frm.usr_extra_7_req_fld && frm.usr_extra_7_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_7, lab_extra_7)){
				return false;
			}
		}
	}

	//usr_extra_8
	if(frm.usr_extra_8){
		frm.usr_extra_8.value = wbUtilsTrimString(frm.usr_extra_8.value);
		var lab_extra_8 = frm.lab_extra_8 ? frm.lab_extra_8.value : 'Extra 8(sys)'
		if(frm.usr_extra_8_req_fld && frm.usr_extra_8_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_8, lab_extra_8)){
				return false;
			}
		}
	}

	//usr_extra_9
	if(frm.usr_extra_9){
		frm.usr_extra_9.value = wbUtilsTrimString(frm.usr_extra_9.value);
		var lab_extra_9 = frm.lab_extra_9 ? frm.lab_extra_9.value : 'Extra 9(sys)'
		if(frm.usr_extra_9_req_fld && frm.usr_extra_9_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_9, lab_extra_9)){
				return false;
			}
		}
	}

	//usr_extra_10
	if(frm.usr_extra_10){
		frm.usr_extra_10.value = wbUtilsTrimString(frm.usr_extra_10.value);
		var lab_extra_10 = frm.lab_extra_10 ? frm.lab_extra_10.value : 'Extra 10(sys)'
		if(frm.usr_extra_10_req_fld && frm.usr_extra_10_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extra_10, lab_extra_10)){
				return false;
			}
		}
	}
	//usr_extra_datetime_11
	if(frm.extra_datetime_11_yy && frm.extra_datetime_11_mm && frm.extra_datetime_11_dd && frm.usr_extra_datetime_11){
		var lab_extra_datetime_11 = frm.lab_extra_datetime_11 ? frm.lab_extra_datetime_11.value : 'extra_datetime_11 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_11_yy.value, Number(frm.extra_datetime_11_mm.value) - 1, Number(frm.extra_datetime_11_dd.value))
		if(frm.extra_datetime_11_dd.value == "" && frm.extra_datetime_11_mm.value == "" && frm.extra_datetime_11_yy.value == ""){
			if(frm.usr_extra_datetime_11_req_fld && frm.usr_extra_datetime_11_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_11', lab_extra_datetime_11, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_11', lab_extra_datetime_11, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_11)
//				frm.extra_datetime_11_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_11.value = frm.extra_datetime_11_yy.value + "-" + frm.extra_datetime_11_mm.value + "-" + frm.extra_datetime_11_dd.value + " " + "23:59:59";
			}
		}
	}
	//usr_extra_datetime_12
	if(frm.extra_datetime_12_yy && frm.extra_datetime_12_mm && frm.extra_datetime_12_dd && frm.usr_extra_datetime_12){
		var lab_extra_datetime_12 = frm.lab_extra_datetime_12 ? frm.lab_extra_datetime_12.value : 'extra_datetime_12 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_12_yy.value, Number(frm.extra_datetime_12_mm.value) - 1, Number(frm.extra_datetime_12_dd.value))
		if(frm.extra_datetime_12_dd.value == "" && frm.extra_datetime_12_mm.value == "" && frm.extra_datetime_12_yy.value == ""){
			if(frm.usr_extra_datetime_12_req_fld && frm.usr_extra_datetime_12_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_12', lab_extra_datetime_12, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_12', lab_extra_datetime_12, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_12)
//				frm.extra_datetime_12_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_12.value = frm.extra_datetime_12_yy.value + "-" + frm.extra_datetime_12_mm.value + "-" + frm.extra_datetime_12_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_13
	if(frm.extra_datetime_13_yy && frm.extra_datetime_13_mm && frm.extra_datetime_13_dd && frm.usr_extra_datetime_13){
		var lab_extra_datetime_13 = frm.lab_extra_datetime_13 ? frm.lab_extra_datetime_13.value : 'extra_datetime_13 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_13_yy.value, Number(frm.extra_datetime_13_mm.value) - 1, Number(frm.extra_datetime_13_dd.value))
		if(frm.extra_datetime_13_dd.value == "" && frm.extra_datetime_13_mm.value == "" && frm.extra_datetime_13_yy.value == ""){
			if(frm.usr_extra_datetime_13_req_fld && frm.usr_extra_datetime_13_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_13', lab_extra_datetime_13, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_13', lab_extra_datetime_13, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_13)
//				frm.extra_datetime_13_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_13.value = frm.extra_datetime_13_yy.value + "-" + frm.extra_datetime_13_mm.value + "-" + frm.extra_datetime_13_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_14
	if(frm.extra_datetime_14_yy && frm.extra_datetime_14_mm && frm.extra_datetime_14_dd && frm.usr_extra_datetime_14){
		var lab_extra_datetime_14 = frm.lab_extra_datetime_14 ? frm.lab_extra_datetime_14.value : 'extra_datetime_14 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_14_yy.value, Number(frm.extra_datetime_14_mm.value) - 1, Number(frm.extra_datetime_14_dd.value))
		if(frm.extra_datetime_14_dd.value == "" && frm.extra_datetime_14_mm.value == "" && frm.extra_datetime_14_yy.value == ""){
			if(frm.usr_extra_datetime_14_req_fld && frm.usr_extra_datetime_14_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_14', lab_extra_datetime_14, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_14', lab_extra_datetime_14, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_14)
//				frm.extra_datetime_14_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_14.value = frm.extra_datetime_14_yy.value + "-" + frm.extra_datetime_14_mm.value + "-" + frm.extra_datetime_14_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_15
	if(frm.extra_datetime_15_yy && frm.extra_datetime_15_mm && frm.extra_datetime_15_dd && frm.usr_extra_datetime_15){
		var lab_extra_datetime_15 = frm.lab_extra_datetime_15 ? frm.lab_extra_datetime_15.value : 'extra_datetime_15 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_15_yy.value, Number(frm.extra_datetime_15_mm.value) - 1, Number(frm.extra_datetime_15_dd.value))
		if(frm.extra_datetime_15_dd.value == "" && frm.extra_datetime_15_mm.value == "" && frm.extra_datetime_15_yy.value == ""){
			if(frm.usr_extra_datetime_15_req_fld && frm.usr_extra_datetime_15_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_15', lab_extra_datetime_15, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_15', lab_extra_datetime_15, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//			alert(wb_msg_usr_enter_vaild + lab_extra_datetime_15)
//				frm.extra_datetime_15_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_15.value = frm.extra_datetime_15_yy.value + "-" + frm.extra_datetime_15_mm.value + "-" + frm.extra_datetime_15_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_16
	if(frm.extra_datetime_16_yy && frm.extra_datetime_16_mm && frm.extra_datetime_16_dd && frm.usr_extra_datetime_16){
		var lab_extra_datetime_16 = frm.lab_extra_datetime_16 ? frm.lab_extra_datetime_16.value : 'extra_datetime_16 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_16_yy.value, Number(frm.extra_datetime_16_mm.value) - 1, Number(frm.extra_datetime_16_dd.value))
		if(frm.extra_datetime_16_dd.value == "" && frm.extra_datetime_16_mm.value == "" && frm.extra_datetime_16_yy.value == ""){
			if(frm.usr_extra_datetime_16_req_fld && frm.usr_extra_datetime_16_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_16', lab_extra_datetime_16, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_16', lab_extra_datetime_16, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_16)
//				frm.extra_datetime_16_dd.focus();
//				return false;
//			}
	else{
				frm.usr_extra_datetime_16.value = frm.extra_datetime_16_yy.value + "-" + frm.extra_datetime_16_mm.value + "-" + frm.extra_datetime_16_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_17
	if(frm.extra_datetime_17_yy && frm.extra_datetime_17_mm && frm.extra_datetime_17_dd && frm.usr_extra_datetime_17){
		var lab_extra_datetime_17 = frm.lab_extra_datetime_17 ? frm.lab_extra_datetime_17.value : 'extra_datetime_17 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_17_yy.value, Number(frm.extra_datetime_17_mm.value) - 1, Number(frm.extra_datetime_17_dd.value))
		if(frm.extra_datetime_17_dd.value == "" && frm.extra_datetime_17_mm.value == "" && frm.extra_datetime_17_yy.value == ""){
			if(frm.usr_extra_datetime_17_req_fld && frm.usr_extra_datetime_17_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_17', lab_extra_datetime_17, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_17', lab_extra_datetime_17, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//			alert(wb_msg_usr_enter_vaild + lab_extra_datetime_17)
//				frm.extra_datetime_17_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_17.value = frm.extra_datetime_17_yy.value + "-" + frm.extra_datetime_17_mm.value + "-" + frm.extra_datetime_17_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_18
	if(frm.extra_datetime_18_yy && frm.extra_datetime_18_mm && frm.extra_datetime_18_dd && frm.usr_extra_datetime_18){
		var lab_extra_datetime_18 = frm.lab_extra_datetime_18 ? frm.lab_extra_datetime_18.value : 'extra_datetime_18 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_18_yy.value, Number(frm.extra_datetime_18_mm.value) - 1, Number(frm.extra_datetime_18_dd.value))
		if(frm.extra_datetime_18_dd.value == "" && frm.extra_datetime_18_mm.value == "" && frm.extra_datetime_18_yy.value == ""){
			if(frm.usr_extra_datetime_18_req_fld && frm.usr_extra_datetime_18_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_18', lab_extra_datetime_18, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_18', lab_extra_datetime_18, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_18)
//				frm.extra_datetime_18_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_18.value = frm.extra_datetime_18_yy.value + "-" + frm.extra_datetime_18_mm.value + "-" + frm.extra_datetime_18_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_19
	if(frm.extra_datetime_19_yy && frm.extra_datetime_19_mm && frm.extra_datetime_19_dd && frm.usr_extra_datetime_19){
		var lab_extra_datetime_19 = frm.lab_extra_datetime_19 ? frm.lab_extra_datetime_19.value : 'extra_datetime_19 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_19_yy.value, Number(frm.extra_datetime_19_mm.value) - 1, Number(frm.extra_datetime_19_dd.value))
		if(frm.extra_datetime_19_dd.value == "" && frm.extra_datetime_19_mm.value == "" && frm.extra_datetime_19_yy.value == ""){
			if(frm.usr_extra_datetime_19_req_fld && frm.usr_extra_datetime_19_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_19', lab_extra_datetime_19, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_19', lab_extra_datetime_19, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_19)
//				frm.extra_datetime_19_dd.focus();
//				return false;
//			}
else{
				frm.usr_extra_datetime_19.value = frm.extra_datetime_19_yy.value + "-" + frm.extra_datetime_19_mm.value + "-" + frm.extra_datetime_19_dd.value + " " + "00:00:00";
			}
		}
	}
		//usr_extra_datetime_20
	if(frm.extra_datetime_20_yy && frm.extra_datetime_20_mm && frm.extra_datetime_20_dd && frm.usr_extra_datetime_20){
		var lab_extra_datetime_20 = frm.lab_extra_datetime_20 ? frm.lab_extra_datetime_20.value : 'extra_datetime_20 (sys)'
		var _cur_date = new Date()
		var _d_date = new Date(frm.extra_datetime_20_yy.value, Number(frm.extra_datetime_20_mm.value) - 1, Number(frm.extra_datetime_20_dd.value))
		if(frm.extra_datetime_20_dd.value == "" && frm.extra_datetime_20_mm.value == "" && frm.extra_datetime_20_yy.value == ""){
			if(frm.usr_extra_datetime_20_req_fld && frm.usr_extra_datetime_20_req_fld.value == 'true'){
				wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_20', lab_extra_datetime_20, '')
				return false;
			}
		}else{
			if(!wbUtilsValidateDate('document.' + frm.name.toString() + '.extra_datetime_20', lab_extra_datetime_20, '')){
				return false;
			}
//			else if(_d_date >= _cur_date){
//				alert(wb_msg_usr_enter_vaild + lab_extra_datetime_20)
//				frm.extra_datetime_20_dd.focus();
//				return false;
//			}
			else{
				frm.usr_extra_datetime_20.value = frm.extra_datetime_20_yy.value + "-" + frm.extra_datetime_20_mm.value + "-" + frm.extra_datetime_20_dd.value + " " + "00:00:00";
			}
		}
	}
	//usr_extra_singleoption_21
	if(frm.usr_extra_singleoption_21){
		var lab_extra_singleoption_21 = frm.lab_extra_singleoption_21 ? frm.lab_extra_singleoption_21.value : 'Extra_singleoption_21 (sys)'
		if(frm.usr_extra_singleoption_21_req_fld && frm.usr_extra_singleoption_21_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_21, lab_extra_singleoption_21)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_22
	if(frm.usr_extra_singleoption_22){
		var lab_extra_singleoption_22 = frm.lab_extra_singleoption_22 ? frm.lab_extra_singleoption_22.value : 'Extra_singleoption_22 (sys)'
		if(frm.usr_extra_singleoption_22_req_fld && frm.usr_extra_singleoption_22_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_22, lab_extra_singleoption_22)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_23
	if(frm.usr_extra_singleoption_23){
		var lab_extra_singleoption_23 = frm.lab_extra_singleoption_23 ? frm.lab_extra_singleoption_23.value : 'Extra_singleoption_23 (sys)'
		if(frm.usr_extra_singleoption_23_req_fld && frm.usr_extra_singleoption_23_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_23, lab_extra_singleoption_23)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_24
	if(frm.usr_extra_singleoption_24){
		var lab_extra_singleoption_24 = frm.lab_extra_singleoption_24 ? frm.lab_extra_singleoption_24.value : 'Extra_singleoption_24 (sys)'
		if(frm.usr_extra_singleoption_24_req_fld && frm.usr_extra_singleoption_24_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_24, lab_extra_singleoption_24)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_25
	if(frm.usr_extra_singleoption_25){
		var lab_extra_singleoption_25 = frm.lab_extra_singleoption_25 ? frm.lab_extra_singleoption_25.value : 'Extra_singleoption_25 (sys)'
		if(frm.usr_extra_singleoption_25_req_fld && frm.usr_extra_singleoption_25_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_25, lab_extra_singleoption_25)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_26
	if(frm.usr_extra_singleoption_26){
		var lab_extra_singleoption_26 = frm.lab_extra_singleoption_26 ? frm.lab_extra_singleoption_26.value : 'Extra_singleoption_26 (sys)'
		if(frm.usr_extra_singleoption_26_req_fld && frm.usr_extra_singleoption_26_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_26, lab_extra_singleoption_26)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_27
	if(frm.usr_extra_singleoption_27){
		var lab_extra_singleoption_27 = frm.lab_extra_singleoption_27 ? frm.lab_extra_singleoption_27.value : 'Extra_singleoption_27 (sys)'
		if(frm.usr_extra_singleoption_27_req_fld && frm.usr_extra_singleoption_27_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_27, lab_extra_singleoption_27)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_28
	if(frm.usr_extra_singleoption_28){
		var lab_extra_singleoption_28 = frm.lab_extra_singleoption_28 ? frm.lab_extra_singleoption_28.value : 'Extra_singleoption_28 (sys)'
		if(frm.usr_extra_singleoption_28_req_fld && frm.usr_extra_singleoption_28_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_28, lab_extra_singleoption_28)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_29
	if(frm.usr_extra_singleoption_29){
		var lab_extra_singleoption_29 = frm.lab_extra_singleoption_29 ? frm.lab_extra_singleoption_29.value : 'Extra_singleoption_29 (sys)'
		if(frm.usr_extra_singleoption_29_req_fld && frm.usr_extra_singleoption_29_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_29, lab_extra_singleoption_29)){
				return false;
			}
		}
	}
	//usr_extra_singleoption_30
	if(frm.usr_extra_singleoption_30){
		var lab_extra_singleoption_30 = frm.lab_extra_singleoption_30 ? frm.lab_extra_singleoption_30.value : 'Extra_singleoption_30 (sys)'
		if(frm.usr_extra_singleoption_30_req_fld && frm.usr_extra_singleoption_30_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_extar_singleoption_30, lab_extra_singleoption_30)){
				return false;
			}
		}
	}
	
	//usr_extra_multipleoption_31
	if(frm.usr_extra_multipleoption_31_lst){
		var lab_extra_multipleoption_31 = frm.lab_extra_multipleoption_31 ? frm.lab_extra_multipleoption_31.value : 'Extra_multipleoption_31(sys)'
		frm.usr_extra_multipleoption_31_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_31');
//		alert("lst:"+frm.usr_extra_multipleoption_31_lst.value);
		if(frm.usr_extra_multipleoption_31_lst.value == "" && frm.usr_extra_multipleoption_31_lst_req_fld && frm.usr_extra_multipleoption_31_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_31)
			return false;
		}
	}
	//usr_extra_multipleoption_32
	if(frm.usr_extra_multipleoption_32_lst){
		var lab_extra_multipleoption_32 = frm.lab_extra_multipleoption_32 ? frm.lab_extra_multipleoption_32.value : 'Extra_multipleoption_32(sys)'
		frm.usr_extra_multipleoption_32_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_32')
		if(frm.usr_extra_multipleoption_32_lst.value == "" && frm.usr_extra_multipleoption_32_lst_req_fld && frm.usr_extra_multipleoption_32_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_32)
			return false;
		}
	}
	//usr_extra_multipleoption_33
	if(frm.usr_extra_multipleoption_33_lst){
		var lab_extra_multipleoption_33 = frm.lab_extra_multipleoption_33 ? frm.lab_extra_multipleoption_33.value : 'Extra_multipleoption_33(sys)'
		frm.usr_extra_multipleoption_33_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_33')
		if(frm.usr_extra_multipleoption_33_lst.value == "" && frm.usr_extra_multipleoption_33_lst_req_fld && frm.usr_extra_multipleoption_33_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_33)
			return false;
		}
	}
	//usr_extra_multipleoption_34
	if(frm.usr_extra_multipleoption_34_lst){
		var lab_extra_multipleoption_34 = frm.lab_extra_multipleoption_34 ? frm.lab_extra_multipleoption_34.value : 'Extra_multipleoption_34(sys)'
		frm.usr_extra_multipleoption_34_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_34')
		if(frm.usr_extra_multipleoption_34_lst.value == "" && frm.usr_extra_multipleoption_34_lst_req_fld && frm.usr_extra_multipleoption_34_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_34)
			return false;
		}
	}
	//usr_extra_multipleoption_35
	if(frm.usr_extra_multipleoption_35_lst){
		var lab_extra_multipleoption_35 = frm.lab_extra_multipleoption_35 ? frm.lab_extra_multipleoption_35.value : 'Extra_multipleoption_35(sys)'
		frm.usr_extra_multipleoption_35_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_35')
		if(frm.usr_extra_multipleoption_35_lst.value == "" && frm.usr_extra_multipleoption_35_lst_req_fld && frm.usr_extra_multipleoption_35_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_35)
			return false;
		}
	}
	//usr_extra_multipleoption_36
	if(frm.usr_extra_multipleoption_36_lst){
		var lab_extra_multipleoption_36 = frm.lab_extra_multipleoption_36 ? frm.lab_extra_multipleoption_36.value : 'Extra_multipleoption_36(sys)'
		frm.usr_extra_multipleoption_36_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_36')
		if(frm.usr_extra_multipleoption_36_lst.value == "" && frm.usr_extra_multipleoption_36_lst_req_fld && frm.usr_extra_multipleoption_36_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_36)
			return false;
		}
	}
	//usr_extra_multipleoption_37
	if(frm.usr_extra_multipleoption_37_lst){
		var lab_extra_multipleoption_37 = frm.lab_extra_multipleoption_37 ? frm.lab_extra_multipleoption_37.value : 'Extra_multipleoption_37(sys)'
		frm.usr_extra_multipleoption_37_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_37')
		if(frm.usr_extra_multipleoption_37_lst.value == "" && frm.usr_extra_multipleoption_37_lst_req_fld && frm.usr_extra_multipleoption_37_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_37)
			return false;
		}
	}
	//usr_extra_multipleoption_38
	if(frm.usr_extra_multipleoption_38_lst){
		var lab_extra_multipleoption_38 = frm.lab_extra_multipleoption_38 ? frm.lab_extra_multipleoption_38.value : 'Extra_multipleoption_38(sys)'
		frm.usr_extra_multipleoption_38_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_38')
		if(frm.usr_extra_multipleoption_38_lst.value == "" && frm.usr_extra_multipleoption_38_lst_req_fld && frm.usr_extra_multipleoption_38_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_38)
			return false;
		}
	}
	//usr_extra_multipleoption_39
	if(frm.usr_extra_multipleoption_39_lst){
		var lab_extra_multipleoption_39 = frm.lab_extra_multipleoption_39 ? frm.lab_extra_multipleoption_39.value : 'Extra_multipleoption_39(sys)'
		frm.usr_extra_multipleoption_39_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_39')
		if(frm.usr_extra_multipleoption_39_lst.value == "" && frm.usr_extra_multipleoption_39_lst_req_fld && frm.usr_extra_multipleoption_39_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_39)
			return false;
		}
	}
	//usr_extra_multipleoption_40
	if(frm.usr_extra_multipleoption_40_lst){
		var lab_extra_multipleoption_40 = frm.lab_extra_multipleoption_40 ? frm.lab_extra_multipleoption_40.value : 'Extra_multipleoption_40(sys)'
		frm.usr_extra_multipleoption_40_lst.value = _wbUserGroupGetExtraMultipleoptionLst(frm, 'usr_extra_multipleoption_40')
		if(frm.usr_extra_multipleoption_40_lst.value == "" && frm.usr_extra_multipleoption_40_lst_req_fld && frm.usr_extra_multipleoption_40_lst_req_fld.value == 'true'){
			alert(wb_msg_usr_please_select_a + lab_extra_multipleoption_40)
			return false;
		}
	}
	if(frm.not_syn_gpm_type){
		frm.usr_not_syn_relation_type_lst.value = _wbUserGroupGetUserNotSynRelationLst(frm);
	}
	
	if(frm.usr_nickname){
		frm.usr_nickname.value = wbUtilsTrimString(frm.usr_nickname.value);
		var lab_nickname = frm.lab_nickname ? frm.lab_nickname.value : 'nickname(sys)'
		if(frm.usr_nickname.value == "" && frm.usr_nickname_req_fld && frm.usr_nickname_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.usr_nickname, lab_nickname)){
				return false;
			}
		}
	}
	
	if(frm.usr_competency) {
		frm.usr_competency.value = _wbUserGroupGetUserAttbEntIdLst(frm, "usr_competency_profile");
	}

	if(document.getElementById("local_image")!= null &&document.getElementById("local_image").checked) {
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
			}
			return ret;
		}
	}
	

	if(frm.urx_extra_41){
		frm.urx_extra_41.value = wbUtilsTrimString(frm.urx_extra_41.value);
		var lab_extension_41 = frm.lab_extension_41 ? frm.lab_extension_41.value : 'MSN(sys)'
		if(frm.urx_extra_41_req_fld && frm.urx_extra_41_req_fld.value == 'true'){
			if(!wbUtilsValidateEmptyField(frm.urx_extra_41, lab_extension_41)){
				return false;
			}
		}
//		if(frm.urx_extra_41.value != '' && !wbUtilsValidateEmail(frm.urx_extra_41, lab_extension_41)){
//			return false;
//		}
	}
	if(frm.urx_extra_42) {
		frm.urx_extra_42.value = wbUtilsTrimString(frm.urx_extra_42.value);
	}
	
	if(frm.urx_extra_44) {
		frm.urx_extra_44.value = wbUtilsTrimString(frm.urx_extra_44.value);
		if (getChars(frm.urx_extra_44.value) > 400){
			var lab_urx_extra_44 = frm.lab_extension_44 ? frm.lab_extension_44.value : 'urx_extra_44(sys)'
			alert(lab_urx_extra_44 + wb_msg_gb_too_long + ', ' + wb_msg_gb_word_limit + ' 400 ' + eval('wb_msg_'+lang+'_characters'))					
			frm.urx_extra_44.focus()
			return false;
		}
	}
	if(frm.urx_extra_45) {
		frm.urx_extra_45.value = wbUtilsTrimString(frm.urx_extra_45.value);
		if (getChars(frm.urx_extra_45.value) > 400) {
			var lab_urx_extra_45 = frm.lab_extension_45 ? frm.lab_extension_45.value : 'urx_extra_44(sys)'
			alert(lab_urx_extra_45 + wb_msg_gb_too_long + ', ' + wb_msg_gb_word_limit + ' 400 '  + eval('wb_msg_'+lang+'_characters'))
			frm.urx_extra_45.focus()
			return false;
		}
	}
	return true;
}

function _wbUserGroupUpdPwdValidateFrm(frm, lang){
	//usr_old_pwd
	if(frm.usr_old_pwd && frm.usr_old_pwd_req_fld && frm.usr_old_pwd_req_fld.value == 'true'){
		var lab_old_passwd = frm.lab_old_passwd ? frm.lab_old_passwd.value : "Old Password(sys)"

		if(!wbUtilsValidateUserPassword(frm.usr_old_pwd, lab_old_passwd, frm.pwd_min_length.value, frm.pwd_max_length.value)){
			return false;
		}else
		if(frm.usr_old_pwd.value.search(/[^A-Za-z0-9_-]/) != -1){
			alert(wb_msg_usr_enter_valid + lab_old_passwd)
			frm.usr_old_pwd.focus()
			return false;
		}/*else if(frm.usr_old_pwd.value.length < frm.pwd_min_length.value || frm.usr_old_pwd.value.length > frm.pwd_max_length.value){
			alert(wb_msg_usr_enter_valid + lab_old_passwd)
			frm.usr_old_pwd.focus()
			return false;
		}*/
	}

	//usr_new_pwd
	if(frm.usr_new_pwd && frm.usr_new_pwd_req_fld && frm.usr_new_pwd_req_fld.value == 'true'){
		var lab_new_passwd = frm.lab_new_passwd ? frm.lab_new_passwd.value : "New Password(sys)"
		if(!wbUtilsValidateUserPassword(frm.usr_new_pwd, lab_new_passwd, frm.pwd_min_length.value, frm.pwd_max_length.value)){
			return false;
		}else if(frm.usr_new_pwd.value.search(/[^A-Za-z0-9_-]/) != -1){
			alert(wb_msg_usr_enter_valid + lab_new_passwd)
			return false;
		}else if(frm.usr_new_pwd.value.length < frm.pwd_min_length.value || frm.usr_new_pwd.value.length > frm.pwd_max_length.value){
			alert(wb_msg_usr_enter_valid + lab_new_passwd)
			frm.usr_new_pwd.focus()
			return false;
		}
	}
	//confirm_usr_pwd
	if(frm.usr_new_pwd && frm.confirm_usr_pwd && frm.confirm_usr_pwd_req_fld && frm.confirm_usr_pwd_req_fld.value == 'true'){
		var lab_con_passwd = frm.lab_con_passwd ? frm.lab_con_passwd.value : "Confirm Password(sys)"
			if(!wbUtilsValidateUserPassword(frm.confirm_usr_pwd, lab_con_passwd, frm.pwd_min_length.value, frm.pwd_max_length.value)){
				return false;
			}
			else if(frm.usr_new_pwd.value != frm.confirm_usr_pwd.value){
			alert('"' + lab_con_passwd + '"' + wb_msg_usr_retyped_passwd_1 + '"' + lab_new_passwd + '"' + wb_msg_usr_retyped_passwd_2)
			frm.confirm_usr_pwd.focus()
			return false;
		}
	}

	if( frm.usr_new_pwd && frm.usr_new_pwd_req_fld && frm.usr_new_pwd_req_fld.value == 'true' && frm.usr_old_pwd && frm.usr_old_pwd_req_fld && frm.usr_old_pwd_req_fld.value == 'true' ) {
		if( frm.usr_new_pwd.value == frm.usr_old_pwd.value ) {
			alert(wb_msg_usr_same_as_old_pwd);
			frm.usr_new_pwd.focus();
			return false;
		}
	}

	return true;
}

function _wbUserGroupResetPwdValidateFrm(frm, lang){
	//usr_new_pwd
	if(frm.usr_new_pwd && frm.usr_new_pwd_req_fld && frm.usr_new_pwd_req_fld.value == 'true'){
		var lab_new_passwd = frm.lab_new_passwd ? frm.lab_new_passwd.value : "New Password(sys)"
		if(!wbUtilsValidateUserPassword(frm.usr_new_pwd, lab_new_passwd, frm.pwd_min_length.value, frm.pwd_max_length.value)){
			return false;
		}else if(!/^(?=.*[A-Za-z]+)(?=.*[0-9]+)[A-Za-z0-9_-]*$/.test(frm.usr_new_pwd.value)){
			alert(wb_msg_usr_enter_valid + lab_new_passwd)
			return false;
		}else if(frm.usr_new_pwd.value.length < frm.pwd_min_length.value || frm.usr_new_pwd.value.length > frm.pwd_max_length.value){
			alert(wb_msg_usr_enter_valid + lab_new_passwd)
			frm.usr_new_pwd.focus()
			return false;
		}
	}

	//confirm_usr_pwd
	if(frm.usr_new_pwd && frm.confirm_usr_pwd && frm.confirm_usr_pwd_req_fld && frm.confirm_usr_pwd_req_fld.value == 'true'){
		var lab_con_passwd = frm.lab_con_passwd ? frm.lab_con_passwd.value : "Confirm Password(sys)"
		if(frm.usr_new_pwd.value != frm.confirm_usr_pwd.value){
			alert('"' + lab_con_passwd + '"' + wb_msg_usr_retyped_passwd_1 + '"' + lab_new_passwd + '"' + wb_msg_usr_retyped_passwd_2)
			frm.confirm_usr_pwd.focus()
			return false;
		}
	}

	//usr_pwd_need_change_ind
	if(frm.usr_pwd_need_change_ind_chk && frm.usr_pwd_need_change_ind){
		if(frm.usr_pwd_need_change_ind_chk.checked == true){
			frm.usr_pwd_need_change_ind.value = 'true';
		}else{
			frm.usr_pwd_need_change_ind.value = 'false';
		}
	}
	return true;
}

function _wbUserGroupAdvSearchRoleLst(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.checked && ele.name == "usr_role"){
			if(ele.value != "") str = str + ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1);
	}
	return str;
}

function _wbUserGroupGetUserRoleLst(frm, lang){
	var i, n, j, k, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.name == "usr_role" && ele.checked){
			if(ele.value != "") str += ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetUserRoleDateLst(frm, lang, date_type){
	var i, n, j, k, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.name == "usr_role" && ele.checked){
			if(ele.value != "")
			//haven't ent_assign
				if(date_type == 'start'){
				str += 'IMMEDIATE' + "~"
			}else{
				str += 'UNLIMITED' + "~"
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetUserSStatusLst(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.name == "s_status_rad" && ele.checked){
			if(ele.value != "") str += ele.value + "~"
		}else if(ele.type == "radio" && ele.name == "s_status_rad" && ele.checked){
			if(ele.value != "") str += ele.value + "~"
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetUserAttbRelationLst(){
	var i, n, str

	str = ""

	frm = arguments[0]

	for(j = 1; j < arguments.length; j++){
		ele = eval('frm.' + arguments[j])
		if(ele){
			if(arguments[j] == 'classification_type'){
				if(ele.length){
					for (i = 0; i < ele.length; i++){
						if(eval('frm.classification_' + frm.classification_type[i].value + '_lst')){
							str += eval('frm.classification_' + frm.classification_type[i].value + '_lst_type.value') + '~'
						}
					}
				}else if(ele){
					if(eval('frm.classification_' + frm.classification_type.value + '_lst')){
						str += eval('frm.classification_' + frm.classification_type.value + '_lst_type.value') + '~'
					}
				}
			}else{
				if(ele.options.length > 0){
					for (i = 0; i < ele.options.length; i++){
						if(ele.options[i].value != ""){
							str += eval('frm.' + arguments[j] + '_type.value') + "~";
						}else{
							str += eval('frm.' + arguments[j] + '_type.value') + "~";
						}
					}
				}else{
					str += eval('frm.' + arguments[j] + '_type.value') + '~'
				}
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetUserAttbEntIdLst(){
	var i, n, str

	str = ""

	frm = arguments[0]

	for(j = 1; j < arguments.length; j++){
		ele = eval('frm.' + arguments[j])
		if(ele){
			if(arguments[j] == 'classification_type'){
				if(ele.length){
					for (i = 0; i < ele.length; i++){
						if(eval('frm.classification_' + frm.classification_type[i].value + '_lst')){
							if(eval('frm.classification_' + frm.classification_type[i].value + '_lst.options[0].value') != ""){
								str += eval('frm.classification_' + frm.classification_type[i].value + '_lst.options[0].value') + "~";
							}else{
								str += '0~';
							}
						}
					}
				}else if(ele){
					if(eval('frm.classification_' + frm.classification_type.value + '_lst')){
						if(eval('frm.classification_' + frm.classification_type.value + '_lst.options[0].value') != ""){
							str += eval('frm.classification_' + frm.classification_type.value + '_lst.options[0].value') + "~";
						}else{
							str += '0~';
						}
					}
				}
			}else{
				if(ele.options.length > 0){
					for (i = 0; i < ele.options.length; i++){
						if(ele.options[i].value != ""){
							str += ele.options[i].value + "~";
						}else{
							str += '0~';
						}
					}
				}else{
					str += '0~'
				}
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetUserNotSynRelationLst(frm){
	var i, str

	str = ""

	if(frm.not_syn_gpm_type.length){
		for (i = 0; i < frm.not_syn_gpm_type.length; i++){
			if(frm.not_syn_gpm_type[i].checked){
				if(str != ""){
					str += "~"
				}
				str += frm.not_syn_gpm_type[i].value;
			}
		}
	}else{
		// only have one field "not_syn_gpm_type"
		if(frm.not_syn_gpm_type.checked){
			str += frm.not_syn_gpm_type.value;
		}
	}
	return str;
}

function _wbUserGroupGetRolTargetExtIdLst(frm){
	var i, j, n, k, str

	str = ""

	for(j = 1; j <= frm.entity_assignment_cnt.value; j++){
		ele = eval('multiple_lst' + [j])
		if(ele.length){
			for (i = 0; i < ele.length; i++){
				for(k = 0; k < ele[i].length; k++){
					if(ele[i][k].value != ""){
						str += ele.id + "~";
					}
				}
				if(ele[i].length == 0){
					str += ele.id + "~";
				}
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetPopupEntId(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.checked && ele.name == 'usr_id'){
			if(ele.value != ""){
				str += ele.value + "~%~";
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 3)
	}
	return str;
}

function _wbUserGroupGetPopupEntIdSingle(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "radio" && ele.checked && ele.name == 'usr_id'){
			if(ele.value != ""){
				str += ele.value + "~%~";
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 3)
	}
	return str;
}

function _wbUserGroupGetPopupEntName(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.checked && ele.name == 'usr_id'){
			if(frm.elements[i + 1].name.substring(0, 10) == 'usr_id_nm_' && frm.elements[i + 1].value != ''){
				str += frm.elements[i + 1].value + "~%~";
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 3)
	}
	return str;
}

function _wbUserGroupGetPopupUsrArgv(frm){
	var i, n, ele, str;

	str = "user_group_and_user" + "~%~"
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.checked && ele.name == 'usr_id'){
			if(frm.elements[i + 1].name.substring(0, 10) == 'usr_id_nm_' && frm.elements[i + 1].value != ''){
				str += ele.value + "~%~" + frm.elements[i + 1].value + "~%~" + 'USR' + "~%~";
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 3)
	}
	return str;
}

function _wbUserGroupGetPopupEntNameSingle(frm){
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "radio" && ele.checked && ele.name == 'usr_id'){
			if(frm.elements[i + 1].name.substring(0, 10) == 'usr_id_nm_' && frm.elements[i + 1].value != ''){
				str += frm.elements[i + 1].value + "~%~";
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 3)
	}
	return str;
}

function _wbUserGroupClearPopupEntChkbox(frm){
	var i, n, ele

	n = frm.elements.length;
	if(frm.sel_all_checkbox)
	if(frm.sel_all_checkbox.type =='checkbox'&& frm.sel_all_checkbox.checked){
    		frm.sel_all_checkbox.checked = false;
    	}

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == 'checkbox' && ele.checked && ele.name == 'usr_id'){
			ele.checked = false;
		}
	}
	return;
}

function _wbUserGroupDisablePopupEntChkBox(frm){
	var i, n, ele

	n = frm.elements.length;

	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == 'checkbox' && ele.name == 'usr_id'){
			ele.disabled = true;
		}
	}
	return;
}

function _wbUserGroupPasteValidateEntIdParent(parent_id, ent_id_parent){
	var i, n

	n = ent_id_parent.length

	for(i = 0; i < n; i++){
		if(ent_id_parent[i] == parent_id){
			return false;
		}
	}
	return true;
}

function _wbUserGetCheckedUserLst(frm){
	var list = "";

	if(frm.entity_id_chkbox.length){
		for (var i = 0; i < frm.entity_id_chkbox.length; i++){
			if(frm.entity_id_chkbox[i].checked){
				list += frm.entity_id_chkbox[i].value + "~";
			}
		}
	}else{
		if(frm.entity_id_chkbox.checked){
			list = frm.entity_id_chkbox.value;
		}
	}

	if(list.indexOf("~") > -1){
		list = list.substr(0, list.length - 1);
	}
	return list;
}

function _wbUserGetCheckedUserTimestampLst(frm){
	var list = "";

	if(frm.entity_id_chkbox.length){
		for (var i = 0; i < frm.entity_id_chkbox.length; i++){
			if(frm.entity_id_chkbox[i].checked){
				list += frm.usr_timestamp[i].value + "~";
			}
		}
	}else{
		if(frm.entity_id_chkbox.checked){
			list = frm.usr_timestamp.value;
		}
	}

	if(list.indexOf("~") > -1){
		list = list.substr(0, list.length - 1);
	}
	return list;
}

function _wbUserGetCheckedUserNameLst(frm){
	var list = "";

	if(frm.entity_id_chkbox.length){
		for (var i = 0; i < frm.entity_id_chkbox.length; i++){
			if(frm.entity_id_chkbox[i].checked){
				list += frm.usr_name[i].value + "~";
			}
		}
	}else{
		if(frm.entity_id_chkbox.checked){
			list = frm.usr_name.value;
		}
	}

	if(list.indexOf("~") > -1){
		list = list.substr(0, list.length - 1);
	}
	return list;
}
//==========multiple usr role list-==========
function show_list(id, object, target){
	var i = 0

	//put each option into goldenman
	//if(object[id]){
	for(i = 0; i < object[id].length; i++){
		if(object[id][i] != null){
			addOption(target, object[id][i])
		}
	}
	object.current_page_id = id
//}
}

function save_list_value(id, object, target){
	var i = 0

	var to_id = object.current_page_id
	var temp_eff_start_date = object[to_id].eff_start_date
	var temp_eff_end_date = object[to_id].eff_end_date
	object[to_id] = null
	object[to_id] = new Array()
	object[to_id].eff_start_date = temp_eff_start_date
	object[to_id].eff_end_date = temp_eff_end_date

	//save all goldenman option into variable
	for(i = 0; i < target.options.length; i++){
		var temp_obj = new Option

		temp_obj.text = target.options[i].text
		temp_obj.value = target.options[i].value
		object[to_id][i] = temp_obj
	}
	//remove all option from goldenman
	target.options.length = 0
}

function _wbUserGroupGetUploadFileName(pathname){
	s = pathname.lastIndexOf("\\");

	if(s == -1){
		s = pathname.lastIndexOf("/");
	}

	if(s == -1){
		return pathname;
	}

	l = pathname.length - s;
	return pathname.substr(s + 1, l);
}

function _wbUserGroupGetUserDirectSupervisorLst(frm){
	frm.direct_supervisor_ent_lst.value = "";
	frm.direct_supervisor_start_lst.value = "";
	frm.direct_supervisor_end_lst.value = "";

	for(var i = 0; i < frm.direct_supervisor_ent.options.length; i++){
		if(i > 0){
			frm.direct_supervisor_ent_lst.value += "~";

			frm.direct_supervisor_start_lst.value += "~";
			frm.direct_supervisor_end_lst.value += "~";
		}

		frm.direct_supervisor_ent_lst.value += frm.direct_supervisor_ent.options[i].value;
		frm.direct_supervisor_start_lst.value += "IMMEDIATE";
		frm.direct_supervisor_end_lst.value += "UNLIMITED";
	}
	return;
}

function _wbUserGroupGetUserSuperviseLst(frm){
	frm.supervise_target_ent_lst.value = "";
	frm.supervise_target_start_lst.value = "";
	frm.supervise_target_end_lst.value = "";

	for(var i = 0; i < frm.supervise_target_ent.options.length; i++){
		if(i > 0){
			frm.supervise_target_ent_lst.value += "~";

			frm.supervise_target_start_lst.value += "~";
			frm.supervise_target_end_lst.value += "~";
		}

		frm.supervise_target_ent_lst.value += frm.supervise_target_ent.options[i].value;
		frm.supervise_target_start_lst.value += "IMMEDIATE";
		frm.supervise_target_end_lst.value += "UNLIMITED";
	}
	return;
}

function wbUserGroupInsUserUncheckRemoveEntity(frm, obj, isChecked){
	if(isChecked == false){
		obj.options.length = 0;
	}
}

function _wbUserGroupCheckEntityAssignment(frm, lang){
	var i, n, j, k, ele, ele2, str

	str = ""
	n = frm.elements.length;

	//Check Case 1, check the unchecked ent_assign box, warn if have value
	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.name == "usr_role" && ele.checked == false){
			if(ele.value != ""){
				if(eval(ele.value + '_has_ent_assign') != null){
					//has ent_assign
					ele2 = frm.elements[i + 1]
					if(ele2.name.substring(0, 18) == 'multi_role_select'){
						cur_obj = eval('multiple_lst' + eval(ele.value + '_has_ent_assign'))
						for(j = 0; j < cur_obj.length; j++){
							if(cur_obj[j].length != 0){
								var _suffix

								_suffix = wb_msg_in + +'\"' + eval('frm.entity_assignment_name_' + ele.value + '.value') + '\" '

								alert(eval('wb_msg_' + lang + '_sel_usr_role') + _suffix)
								return false;
								break;
							}
						}
					}
				}
			}
		}
	}

	//Check Case 2, check the checked ent_assign box, but no user /usergroup selected
	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.name == "usr_role" && ele.checked){
			if(ele.value != "") if(eval(ele.value + '_has_ent_assign') != null){
				//has ent_assign
				cur_obj = eval('multiple_lst' + eval(ele.value + '_has_ent_assign'))
				for(j = 0; j < cur_obj.length; j++){
					if(cur_obj[j].length == 0){
						var _goldenman_type = eval('frm.entity_assignment_goldenman_type_' + ele.value + '.value')
						var _suffix

						_suffix = wb_msg_for + +'\"' + eval('frm.entity_assignment_name_' + ele.value + '.value') + '\" '

						if(_goldenman_type == 'USG'){
							alert(eval('wb_msg_' + lang + '_select_grp') + _suffix)
						}else if(_goldenman_type == 'USR_OR_USG'){
							alert(eval('wb_msg_' + lang + '_sel_usrgrp') + _suffix);
						}else if(_goldenman_type == 'UGR'){
							alert(eval('wb_msg_' + lang + '_sel_usr') + _suffix);
						}

						return false;
						break;
					}
				}
			}
		}
	}

	//Check Case 3, check user/ usergroup selection with out ALL TIME element
	for(i = 0; i < n; i++){
		ele = frm.elements[i]
		if(ele.type == "checkbox" && ele.name == "usr_role" && ele.checked){
			if(ele.value != ""){
				if(eval(ele.value + '_has_ent_assign') != null){
					ele2 = frm.elements[i + 1]
					if(ele2.type == 'select-multiple'){
						if(ele2.length == 0){
							var _suffix

							_suffix = wb_msg_for + +'\"' + eval('frm.entity_assignment_name_' + ele.value + '.value') + '\" '

							if(_goldenman_type == 'USG'){
								alert(eval('wb_msg_' + lang + '_select_grp') + _suffix)
							}else if(_goldenman_type == 'USR_OR_USG'){
								alert(eval('wb_msg_' + lang + '_sel_usrgrp') + _suffix);
							}else if(_goldenman_type == 'UGR'){
								alert(eval('wb_msg_' + lang + '_sel_usr') + _suffix);
							}

							return false;
							break;
						}
					}
				}
			}
		}
	}
	return true;
}

function _wbUserGroupGetRolTargetEntGroupLst(frm){
	var i, j, n, k, str

	str = ""

	for(j = 1; j <= frm.entity_assignment_cnt.value; j++){
		ele = eval('multiple_lst' + [j])
		if(ele.length){
			for (i = 0; i < ele.length; i++){
				for(k = 0; k < ele[i].length; k++){
					if(ele[i][k].value != ""){
						str += ele[i][k].value + "~";
					}
				}
				if(ele[i].length == 0){
					str += "0" + "~";
				}
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetRolTargetDateLst(frm, time_type){
	var i, j, n, k, str

	str = ""

	for(j = 1; j <= frm.entity_assignment_cnt.value; j++){
		ele = eval('multiple_lst' + [j])
		if(ele.length){
			for (i = 0; i < ele.length; i++){
				for(k = 0; k < ele[i].length; k++){
					if(ele[i][k].value != ""){
						if(time_type == 'start'){
							str += ele[i].eff_start_date + "~";
						}else{
							str += ele[i].eff_end_date + "~";
						}
					}
				}
				if(ele[i].length == 0){
					if(time_type == 'start'){
						str += ele[i].eff_start_date + "~";
					}else{
						str += ele[i].eff_end_date + "~";
					}
				}
			}
		}
	}

	if(str != ""){
		str = str.substring(0, str.length - 1)
	}
	return str;
}

function _wbUserGroupGetExtraMultipleoptionLst(frm,name){
	var i, n, j, k, ele, str

	str = " ";
	n = frm.elements.length;
	for(i = 0; i < n; i++){
		ele = frm.elements[i];
		if(ele.type == "checkbox" && ele.name == name && ele.checked){
			if(ele.value != " "){
				str = str + ele.value + " , ";
			}
		}
	}
	if(str != " "){
		str=str.substring(0,str.length-2);
	}else{
		str ="";
	}
	return str;
}

function wbUserForgetPwdPrep(enrol_itm_id, site_id, label_lan, style) {
	if (site_id == null || site_id == '') {
		site_id = '1';
	}
	if (style == null || style == '') {
		style = 'cw';
	}
	if (label_lan == null || label_lan == '') {
		label_lan = 'ISO-8859-1';
	}
	var url_success = '../app/user/forget_password';

	var url = wb_utils_invoke_disp_servlet('module', 'login.LoginModule', 'cmd', 'guest_login', 'site_id', site_id, 'style', style, 'label_lan', label_lan, 'url_success', url_success)
	window.location.href = url;
}


function wbUserGroupUserDeleteUser(id, lang) {
	if (confirm(eval('wb_msg_' + lang + '_confirm'))) {
		var url_success = self.location.href
		var url_failure = self.location.href
		var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.user.UserModule', 'cmd', 'delete_user', 'usr_ent_id', id, 'url_success', url_success, 'url_failure', url_failure)
		window.location.href = url
	}
}

function wbAllUserGroupUserDeleteUser(frm,lang){
	var select_usr = false;
	var usr_id_lst =  document.getElementsByName('usr_id_lst');
	var id_lst = '';
	var url_success = self.location.href
	var url_failure = self.location.href
	var url;
	for(var i=0; i<usr_id_lst.length; i++){
		if(usr_id_lst[i].checked){
			if (select_usr || confirm(eval('wb_msg_' + lang + '_confirm'))) {
				id_lst += usr_id_lst[i].value+'~';
				select_usr = true
			}else
				return ;
		}
	}
	if(!select_usr){
		alert(eval("wb_msg_" + lang + "_sel_usr"));
		return ;
	}
	id_lst =id_lst+'0';
	url = wb_utils_invoke_disp_servlet('module', 'JsonMod.user.UserModule', 'cmd', 'all_delete_user', 'id_lst', id_lst, 'url_success', url_success, 'url_failure', url_failure)
	window.location.href = url
	
}

function _wbUserGroupGetPopupUserId(frm) {
	var i, n, ele, str
	
	str = ""
		n = frm.elements.length;
	
	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "checkbox" && ele.checked && ele.name == 'usr_id') {
			if (frm.elements[i + 2].name.substring(0, 14) == 'usr_id_usr_id_' && frm.elements[i + 2].value != '') {
				str += frm.elements[i + 2].value + "~%~";
			}
		}
	}
	
	if (str != "") {
		str = str.substring(0, str.length - 3)
	}
	return str;
}

function _wbUserGroupGetPopupUserIdSingle(frm) {
	var i, n, ele, str

	str = ""
	n = frm.elements.length;

	for (i = 0; i < n; i++) {
		ele = frm.elements[i]
		if (ele.type == "radio" && ele.checked && ele.name == 'usr_id') {
			if (frm.elements[i + 2].name.substring(0, 14) == 'usr_id_usr_id_' && frm.elements[i + 2].value != '') {
				str += frm.elements[i + 2].value + "~%~";
			}
		}
	}

	if (str != "") {
		str = str.substring(0, str.length - 3)
	}
	return str;
}

function wbUserPosition() {
	this.maintain = wbUserPositionMaintenance
}

function wbUserPositionMaintenance() {
	url = wb_utils_invoke_disp_servlet('module', 'position.UserPositionModule', 'cmd','get_position_list', 'stylesheet', 'usr_position_list.xsl')
	window.parent.location.href = url;
}

//Open the page batch changes
function wbUserUpdBatchPrep(frm,lang){
	//If have no choice user pop-up "please select users" tips
	if(_wbUserGetCheckedUserLst(frm) == ""){
		alert(eval("wb_msg_" + lang + "_sel_usr"));
		return;
	}
	//The cache of the currently selected user id
	wb_utils_set_cookie("usg_ent_id_lst", _wbUserGetCheckedUserLst(frm));
	wb_utils_set_cookie("cur_user_group_id",frm.cur_user_group_id.value);  
	var url = wb_utils_invoke_servlet('cmd', 'user_upd_batch_prep', 'stylesheet', 'user_upd_batch_prep.xsl','ent_id',frm.cur_user_group_id.value)
	window.location.href = url;
}

//Perform batch of update user function
function wbUserUpdBatchExec(frm,lang, active_tab){
	//Form of authentication is correct,If a form validation success to submit the form
	if(validationFrom_batch(frm,lang)){
		var url
		var url_success = wbUserGroupGroupManageURL(trim(frm.cur_group_id.value), '', '', '', '','DESC', active_tab)
		if(frm.usr_extra_datetime_11!=undefined){
			url = wb_utils_invoke_servlet('cmd', 'user_upd_batch_exec', 'usr_group_id',frm.usr_group_lst.options[0].value, 'usr_grade_id',frm.usr_grade_lst.options[0].value,'usr_extra_datetime_11',frm.usr_extra_datetime_11.value,'usg_ent_id_lst',wb_utils_get_cookie("usg_ent_id_lst"),'url_success',url_success)
		}else{
		   url = wb_utils_invoke_servlet('cmd', 'user_upd_batch_exec', 'usr_group_id',frm.usr_group_lst.options[0].value, 'usr_grade_id',frm.usr_grade_lst.options[0].value,'usr_extra_datetime_11','','usg_ent_id_lst',wb_utils_get_cookie("usg_ent_id_lst"),'url_success',url_success)	
		}
	    window.location.href = url;
	}
}


//Perform batch of update user function
function wbUserUpdBatchExec2(frm,lang, active_tab){
    //Form of authentication is correct,If a form validation success to submit the form
    if(validationFrom_batch(frm,lang)){
        var url
        var url_success = wbUserGroupGroupManageURL(trim(frm.cur_group_id.value), '', '', '', '','DESC', active_tab)
        if(frm.usr_extra_datetime_11!=undefined){
            url = wb_utils_invoke_servlet('cmd', 'user_upd_batch_exec', 'usr_group_id',frm.usr_group_lst.options[0].value, 'usr_grade_id',frm.usr_grade_lst.options[0].value,'usr_extra_datetime_11',frm.usr_extra_datetime_11.value,'usg_ent_id_lst',wb_utils_get_cookie("usg_ent_id_lst"),'url_success',url_success,'confirm',0)
        }else{
           url = wb_utils_invoke_servlet('cmd', 'user_upd_batch_exec', 'usr_group_id',frm.usr_group_lst.options[0].value, 'usr_grade_id',frm.usr_grade_lst.options[0].value,'usr_extra_datetime_11','','usg_ent_id_lst',wb_utils_get_cookie("usg_ent_id_lst"),'url_success',url_success,'confirm',0)    
        }
        window.location.href = url;
    }
}


function validationFrom_batch(frm,lang){
	//If there's a valid account verify account validity
	if(frm.usr_extra_datetime_11!=undefined){
		//	Verify whether time conform to the rules		
		if(frm.user_effective_datetime_yy.value!=''&&frm.user_effective_datetime_mm.value!=''&&frm.user_effective_datetime_dd.value!=''){
			if(wbUtilsValidateDate('document.'+ frm.name  + '.user_effective_datetime','账号有效期','','ymd')){
				frm.usr_extra_datetime_11.value= frm.user_effective_datetime_yy.value
					+'-'+frm.user_effective_datetime_mm.value
					+'-'+frm.user_effective_datetime_dd.value
					+' '+frm.user_effective_datetime_hour.value
					+':'+frm.user_effective_datetime_min.value
					+':00.000';
			}else{
				return false;
			}
		}
	}
	//User group will choose validation, if not choose out "please select user group" tip
	if(frm.usr_group_lst.options[0].value==''){
		alert(eval("wb_msg_select_group_" + lang));
		frm.usr_group_lst_single.focus();
		return false;
	}
	//Duties will choose validation, if not choose out "please select position" tip
	if(frm.usr_grade_lst.options[0].value==''){
		alert(eval("wb_msg_select_grade_" + lang));
		frm.usr_grade_lst_single.focus();
		return false;
	}
	return true;	
}