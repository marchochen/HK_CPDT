// ------------------ wizBank Knowledge Management object ------------------- 
// Convention:
//   public functions : use "wbKM" prefix 
//   private functions: use "_wbKM" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 
var wbKMModule = 'km.KMModule'

function wbKM(){	
	this.goHome = wbKMGoHome	
	this.item = new wbKMItem
	this.domain = new wbKMDomain
	this.workplace = new wbKMWorkPlace
	this.subscribe = new wbKMSubScribe
	this.profile = new wbKMProfile
	this.search = new wbKMSearch
}

function wbKMItem(){
	this.item_main = wbKMItemMain
}

function wbKMDomain(){
	this.domain_main = wbKMDomainMain
}

function wbKMWorkPlace(){
	this.workplace_main = wbKMWorkPlaceMain
}

function wbKMSubScribe(){
	this.subscribe_main = wbKMSubscribeMain
}

function wbKMProfile(){
	this.upd_profile = wbKMProfileUpdateProfile
}

function wbKMSearch(){
	this.sim_search_exec = wbKMSearchSimSearchExec
}
/* =================================== */
function wbKMGoHome(){
	if(wb_utils_servlet_url == '../../../servlet/qdbAction'){
		//at home
	}else{
		window.location.href = '../cw/skin1/jsp/km_home.jsp'
	}
	//window.location.href = url;
}
/* =================================== */
function wbKMItemMain(){
	var url = wbKMItemMainURL()
	window.location.href = url;
}

function wbKMItemMainURL(){
	var url = wb_utils_invoke_disp_servlet('cmd','get_folder_main','stylesheet','km_work_lst.xsl','module',wbKMModule,'type','WORK')
	return url;
}

/* =================================== */
function wbKMDomainMain(){
	var url = wbKMDomainMainURL()
	window.location.href = url;
}

function wbKMDomainMainURL(){
	var url = wb_utils_invoke_disp_servlet('cmd','get_folder_main','stylesheet','km_domain_lst.xsl','module',wbKMModule,'type','DOMAIN')
	return url;
}
/* =================================== */
function wbKMWorkPlaceMain(){
	var url = wb_utils_invoke_disp_servlet('cmd','get_my_workplace','module',wbKMModule,'stylesheet','km_my_workplace.xsl')
	window.location.href = url;
}

function wbKMSubscribeMain(){
	var url = wb_utils_invoke_disp_servlet('cmd','get_sub_lst','module',wbKMModule,'stylesheet','km_subscribe_lst.xsl')
	window.location.href = url
}
/* =================================== */
function wbKMProfileUpdateProfile(ent_id,lang){
	var url = wb_utils_invoke_servlet('cmd','get_usr','usr_ent_id',ent_id,'stylesheet','km_usr_detail_upd_trim.xsl')
	window.location.href = url;
}
/* ===================================*/
function wbKMSearchSimSearchExec(frm,cur_page,page_size){
	if ( cur_page == null || cur_page == '' ) {cur_page = '1';}
	if ( page_size == null || page_size == '' )	{page_size = '10';}
	
	if (frm.words.value != ''){
		frm.cmd.value = 'simple_search'
		frm.module.value = 'km.KMModule'
		frm.url_failure.value = ''
		frm.cur_page.value = cur_page
		frm.page_size.value = page_size
		frm.stylesheet.value = 'km_obj_search_result.xsl'
		frm.action = wb_utils_disp_servlet_url	
		frm.method = 'get'	
		frm.submit()
	}else {
		alert(wb_msg_km_gen_search_words_empty);
	}
}

