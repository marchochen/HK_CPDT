// ----------------------- Access Control -----------------------
// Convention:
//   public functions : use "wbAccessControl" prefix 
//   private functions: use "_wbAccessControl" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js	 
// --------------------------------------------------------------------- 

function wbAccessControl(){
	this.load_role = wbAccessControlLoadRole;
	this.save_ass_ftn = wbAccessControlSaveFtn;
}

function wbAccessControlLoadRole(frm){
	rol_ext_id = frm.role.options[frm.role.selectedIndex].value;
	url = wb_utils_invoke_disp_servlet("module","accesscontrol.AccessControlModule","cmd","get_rol_ftn","rol_ext_id",rol_ext_id,"stylesheet","adm_acc_control.xsl")
	window.location.href = url;
}

function wbAccessControlSaveFtn(frm){
	str = "";
	if(frm.assigned_ecdn.options.length>0){
		for(i=0; i<frm.assigned_ecdn.options.length-1; i++)
			str = str + frm.assigned_ecdn.options[i].value + "~";
		str = str+frm.assigned_ecdn.options[frm.assigned_ecdn.options.length-1].value;
	}
	ftn_ext_id_lst= str;
	rol_ext_id = frm.role.options[frm.role.selectedIndex].value;
	url_success = 	wb_utils_invoke_disp_servlet("module","accesscontrol.AccessControlModule","cmd","get_rol_ftn","rol_ext_id",rol_ext_id,"stylesheet","adm_acc_control.xsl")
	url = wb_utils_invoke_disp_servlet("module","accesscontrol.AccessControlModule","cmd","save_rol_ftn","rol_ext_id",rol_ext_id,"ftn_ext_id_lst",ftn_ext_id_lst,"url_success",url_success)
	window.location.href = url;
}