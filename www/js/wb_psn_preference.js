// ------------------ wizBank Data Import and Export object ------------------- 
// Convention:
//   public functions : use "wbBatchProcess" prefix 
//   private functions: use "_wbBatchProcess" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

function wbPsnPreference(){
	this.get_my_preference = wbPsnPreferenceGetMyPreference;
	this.save_my_preference = wbPsnPreferenceSaveMyPreference;
	this.del_my_preference = wbPsnPreferenceDelMyPreference;
}
//------------------------------------------------------------------ 
function wbPsnPreferenceGetMyPreference(){
	window.location.href = wbPsnPreferenceGetMyPreferenceURL;
}

function wbPsnPreferenceGetMyPreferenceURL(){
	url = wb_utils_invoke_disp_servlet(
		'cmd','get_my_preference',
		'module','personalization.PsnPreferenceModule',
		'stylesheet','psn_preference_upd.xsl'
	)
	return url;
}

function wbPsnPreferenceDelMyPreference(){
	url = wb_utils_invoke_disp_servlet(
		'cmd','del_my_preference',
		'module','personalization.PsnPreferenceModule',
		'url_success',wb_utils_gen_home_url(),
		'url_failure',wb_utils_gen_home_url()
	)
	window.location.href = url;
}

function wbPsnPreferenceSaveMyPreference(frm,lang){
	if (_wbPsnPreferenceSaveValidateForm(frm, lang)){
		frm.cmd.value = 'save_my_preference'
		frm.module.value = 'personalization.PsnPreferenceModule'
		frm.url_success.value = wb_utils_gen_home_url()
		frm.url_failure.value = wb_utils_gen_home_url()
		frm.method = 'get'
		frm.action = wb_utils_disp_servlet_url	
		frm.submit()
	}
}

function _wbPsnPreferenceSaveValidateForm(frm,lang){
	if (frm.skin_id){
		if (!gen_validate_empty_field(frm.skin_id, eval('wb_msg_' + lang + '_skin') ,lang)){
			frm.skin_id.focus();
			return false;
		}
	}
	if (frm.lang){
		if (!gen_validate_empty_field(frm.lang, eval('wb_msg_' + lang + '_lang') ,lang)){
			frm.lang.focus();
			return false;
		}
	}
	return true;
}