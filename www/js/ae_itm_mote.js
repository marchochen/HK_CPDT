// ------------------ wizBank Item Mote object -------------------
// Convention:
//   public functions : use "aeItem" prefix
//   private functions: use "_aeItem" prefix
// Dependency:
//   gen_utils.js
//   ae_utils.js
// ------------------------------------------------------------

/* constructor */
function aeItemMote(){
	this.edit_prep = aeItemMoteEditPrep;
	this.edit_exec = aeItemMoteEditExecute;
	this.triggle_mk = aeItemMoteTriggleMark;
}

/* public functions */
function aeItemMoteEditExecute(frm){
	frm.cmd.value = 'ae_upd_itm_mote';
	frm.action = ae_utils_servlet_url;
	frm.method = 'post';
	frm.url_success.value = ae_utils_get_cookie('url_prev');
	frm.submit();
}

function aeItemMoteEditPrep(itm_id){
	url = ae_utils_invoke_servlet('cmd','ae_get_itm_mote','itm_id',itm_id,'stylesheet',ae_cos_mote_udp);
	window.location.href = url;
}

function aeItemMoteTriggleMark(frm){
	frm.cmd.value = 'ae_upd_itm_mote_status';
	if (frm.imt_status.value == 'REVIEWED')
		frm.imt_status.value = 'NOTREVIEWED';
	else
		frm.imt_status.value = 'REVIEWED';
	frm.action = ae_utils_servlet_url;
	frm.method = 'post';
	frm.url_success.value = ae_utils_get_cookie('url_prev');
	frm.submit();
}