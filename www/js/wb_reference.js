// ------------------ wizBank Reference object ------------------- 
// Convention:
//   public functions : use "wbReference" prefix 
//   private functions: use "_wbReference" prefix
// Dependency:
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
function wbReference() {	
	// for instructor
	this.del_ref = wbReferenceDelRef
	this.ins_ref = wbReferenceInsRef
	this.ins_ref_prep = wbReferenceInsRefPrep
	this.upd_ref = wbReferenceUpdRef
	this.upd_ref_prep = wbReferenceUpdRefPrep
	
	// for learner
	this.go_ref = wbReferenceGoRef
}

function _wbReferenceGetNumber(frm) {
		var i, n, ele, str
		str = ""
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			if (ele.type == "checkbox" && ele.checked && ele.name == 'rdo_ref_id') {
				if (ele.value !="")
					str = str + ele.value + "~"
			}
		}
		
		if (str != "") {
			str = str.substring(0, str.length-1);
		}
		return str;	
}

function wbReferenceInsRefPrep(frm) {
	url = wb_utils_disp_servlet_url + '?env=' + 'wizb' + '&module=' + 'content.ReferenceModule' + '&cmd=' + 'get_reference_list' + '&stylesheet=' + 'ist_ins_reference.xsl' + '&mod_id=' + frm.mod_id.value
	window.location = url
}

function wbReferenceUpdRefPrep(frm, ref_id) {
	url = wb_utils_disp_servlet_url + '?env=' + 'wizb' + '&module=' + 'content.ReferenceModule' + '&cmd=' + 'get_reference' + '&stylesheet=' + 'ist_upd_reference.xsl' + '&mod_id=' + frm.mod_id.value + "&ref_id=" + ref_id
	window.location = url
}

function wbReferenceDelRef(frm,lang){
		frm.ref_id_list.value = _wbReferenceGetNumber(frm)
		if (frm.ref_id_list.value == "") {
			alert(eval('wb_msg_' + lang + '_select_faq_topic'))
			return;
		}else{
			frm.cmd.value = 'del_reference'
			frm.module.value= 'content.ReferenceModule'
			frm.url_success.value = window.location;
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.submit();
		}
}

function wbReferenceInsRef(frm,lang){
		frm.ref_title.value = wbUtilsTrimString(frm.ref_title.value);
		if (frm.ref_url) {
			frm.ref_url.value = wbUtilsTrimString(frm.ref_url.value);
		}
		if (frm.ref_description) {
			frm.ref_description.value = wbUtilsTrimString(frm.ref_description.value);
		}
		if (frm.ref_title.value == "") {
			Dialog.alert(eval('wb_msg_' + lang + '_enter_title'))
			return;
		}
		if (getChars(wbUtilsTrimString(frm.ref_title.value)) > 80) {
			Dialog.alert(eval('wb_msg_' + lang + '_title_not_longer'))
			return;
		}
		if (getChars(wbUtilsTrimString(frm.ref_description.value)) > 400) {
			Dialog.alert(eval('wb_msg_' + lang + '_content')+eval('label_tm.label_core_training_management_382'))
			return;
		}
		else{
			frm.cmd.value = 'ins_reference'
			frm.module.value= 'content.ReferenceModule'
			frm.url_success.value = wb_utils_disp_servlet_url + '?env=wizb&module=content.ReferenceModule&cmd=get_reference_list&stylesheet=ist_get_reference_lst.xsl' + '&mod_id=' + frm.mod_id.value;
			frm.url_failure.value = window.location
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.submit();
		}
}

function wbReferenceUpdRef(frm,lang){
		frm.ref_title.value = wbUtilsTrimString(frm.ref_title.value);
		if (frm.ref_url) {
			frm.ref_url.value = wbUtilsTrimString(frm.ref_url.value);
		}
		if (frm.ref_description) {
			frm.ref_description.value = wbUtilsTrimString(frm.ref_description.value);
		}
		if (frm.ref_title.value == "") {
			Dialog.alert(eval('wb_msg_' + lang + '_enter_title'))
			return;
		}
		if (getChars(wbUtilsTrimString(frm.ref_title.value)) > 80) {
			Dialog.alert(eval('wb_msg_' + lang + '_title_not_longer'))
			return;
		}
		if (getChars(wbUtilsTrimString(frm.ref_description.value)) > 400) {
			Dialog.alert(eval('wb_msg_' + lang + '_content')+eval('label_tm.label_core_training_management_382'))
			return;
		}
		else{
			frm.cmd.value = 'upd_reference'
			frm.module.value= 'content.ReferenceModule'
			frm.url_success.value = wb_utils_disp_servlet_url + '?env=wizb&module=content.ReferenceModule&cmd=get_reference_list&stylesheet=ist_get_reference_lst.xsl' + '&mod_id=' + frm.mod_id.value;
			frm.url_failure.value = window.location
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.submit();
		}
}

function wbReferenceGoRef(ref_url,url_name){
	
	if(null != url_name && document.getElementById(url_name)){
		var ref_url_by_id = document.getElementById(url_name).value;
		if(null != ref_url_by_id && ref_url_by_id != undefined){
			ref_url = ref_url_by_id;
		}
	}
	
	wbUtilsOpenWin(ref_url, '_blank');
}