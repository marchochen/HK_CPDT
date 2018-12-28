// -------- wizBank UserManager object ----------
// Convention:
//   public functions : use "wbUsrMgt" prefix 
//   private functions: use "_wbUsrMgt" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbUserManager() {
	this.get_usr_lst = wbUsrMgtGetUserList;
	this.get_usr_dtl = wbUsrMgtGetUserDetail;
}

function wbUsrMgtGetUserList(id, group_name, cur_page, pagesize){
	if ( cur_page == null || cur_page == '' )
		cur_page = 1;
	if ( pagesize == null || pagesize == '' )
		pagesize = 10;
	url = wb_utils_invoke_servlet('cmd','get_ent_lst','cur_page',cur_page,'pagesize',pagesize,'stylesheet','adm_usr_list.xsl','ent_id',id);
	window.location.href = url;
}

function wbUsrMgtGetUserDetail(usr_id, usr_name){
	url = wb_utils_invoke_servlet('cmd','get_usr','usr_ent_id',usr_id,'stylesheet','adm_usr_dtl.xsl');
	window.location.href = url;
}