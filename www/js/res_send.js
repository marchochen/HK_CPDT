function wbRES() {	
	this.sendFrm = wbRESSendForm	
}

function wbRESSendForm(frm,lang) {

	if (_wbRESValidateForm(frm,lang)) {	
		_wbRESGenFrm(frm,lang)
		frm.method = 'post'
		frm.action = wb_utils_servlet_url
//frm.action = "http://127.0.0.1:8080/servlet/qdb.qdbAction"
		
		alert(frm.res_annotation.value)
		alert(frm.asHTML.value)
		alert(frm.res_url.value)
		alert(frm.upload_filename.value)
		alert(frm.res_title.value)
		alert(frm.res_desc.value)
		alert(frm.res_lan.value)
		alert(frm.res_difficulty.value)
		alert(frm.res_duration.value)
		alert(frm.res_privilege.value)
		alert(frm.res_status.value)
		alert(frm.cmd.value)
		alert(frm.res_type.value)
		alert(frm.res_subtype.value)
		alert(frm.res_filename.value)
		alert(frm.obj_id.value)
		
		frm.submit();
	}
	
}

function xmlEscape(str) {
	str = str.replace(/&/g, '&amp;')
	str = str.replace(/</g, '&lt;')
	return str
}

function _wbRESGenFrm(frm,lang) {
	
	if (frm.asHTML.checked) 
		frm.res_annotation.value = '<html>' + xmlEscape(frm.res_annotation.value) + '</html>'
	
	if( frm.radiobutton[0].checked == true )
		frm.res_filename.value = '';			
	else
		if( frm.radiobutton[1].checked == true || frm.radiobutton[2].checked == true )
		{	frm.res_url.value = '';
			frm.res_filename.value = frm.upload_filename.value;
		}
			
			
}
function _wbRESValidateForm(frm,lang) {
	frm.res_duration.value = wbUtilsTrimString(frm.res_duration.value);
	if (!gen_validate_float(frm.res_duration, 'Duration')) {		
		return false
	} 	
	frm.res_title.value = wbUtilsTrimString(frm.res_title.value);
	if ( !gen_validate_empty_field(frm.res_title, 'Title') ) {		
		return false
	}
	if ( frm.radiobutton[1].checked == true && !gen_validate_empty_field(frm.upload_filename, 'Upload File')){
		return false
	}		
	if ( frm.radiobutton[0].checked == true && frm.res_url.value == 'http://' ){//!gen_validate_empty_field(frm.res_url, 'URL')){
		return false
	}
	
		
/*	if ( frm.res_url.value == "http://" )
		return false
	}
	*/
	return true;
}
