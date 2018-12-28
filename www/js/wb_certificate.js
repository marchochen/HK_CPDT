function wbCertificate() {
	this.ins_cert_prep = wbCertificateIns;// add prep
	this.ins_cert_exec = wbCertificateExec;// submit(check form)
	this.del_cert = wbCertificateDel;
	this.preview_cert = wbCertificatePreview;// preview
	this.download_cert = wbCertificateDownload;// download
	this.show_mgt_content = wbCertShowContent;
	
	this.init_cert_set  = wbInitCertSet
	this.itm_preview_cert = wbItmPreviewCert
	this.change_cert_set = wbChangeCertSet
	this.set_cert_default = wbSetCertDefault
	this.search_cert = wbCertificateSearch

}

function wbCertificateIns(cert_id, cert_tc_id) {
	if(cert_tc_id == null) cert_tc_id = '';
	url = wb_utils_invoke_disp_servlet('module',
			'cert.CertificateModule', 'cmd', 'ins_upd_cert_prep',
			'stylesheet', 'certificate_form.xsl', 'cert_id', cert_id, 'cert_tc_id', cert_tc_id);
	parent.location.href = url;
}

function checkStringSymbol(txtName)
{
	if(txtName.indexOf('\'') != -1 || txtName.indexOf('\"') != -1 )
	{
		return true;
	}
	if(txtName.indexOf('\’') != -1 || txtName.indexOf('\“') != -1  || txtName.indexOf('\”') != -1 || txtName.indexOf('\‘') != -1)
	{
		return true;
	}
	return false;
}


function wbCertificateExec(frm, lang, cert_id) {
	if(frm.cert_core !=null){
		frm.cert_core.value = wbUtilsTrimString(frm.cert_core.value);
		if(getChars(frm.cert_core.value)==0){
			alert(wb_cert_core);
			return;
		}else if(getChars(frm.cert_core.value) > 20){
			alert(wb_cert_core_length);
			return;
		}else if(checkStringSymbol(frm.cert_core.value)){
			alert(label_tm.label_core_training_management_362);
			return;
		}
	}else{
		alert(wb_cert_core);
		return;
	}
	
	if(frm.cert_title !=null){
		if(checkStringSymbol(frm.cert_title.value)){
			alert(label_tm.label_core_training_management_363);
			return;
		}
	}
	
	if(frm.cert_title){
		frm.cert_title.value = wbUtilsTrimString(frm.cert_title.value);
		if (!gen_validate_empty_field(frm.cert_title, eval('wb_msg_'+ lang + '_title'),lang)){
			return false;
		}
	}
		if(frm.cert_title){
		frm.cert_title.value = wbUtilsTrimString(frm.cert_title.value);
		if (!gen_validate_empty_field(frm.cert_title, eval('wb_msg_'+ lang + '_title'),lang)){
			return false;
		}
	}
	
	if(frm.cert_title != null){
		frm.cert_title.value = wbUtilsTrimString(frm.cert_title.value);
		 if(getChars(frm.cert_title.value) > 80){
			alert(label_rm.label_core_requirements_management_55);
			return;
		}
	}
	
	if (frm.tc_id_single.value == '') {
		alert(wb_msg_tc)
		return false;
	}
	
	var file_path = frm.cert_img.value;
	if (cert_id == '') {
		if(file_path == ''){
			alert(wb_msg_select_uploaded_picture);
			return false;
		}
	}
	if (file_path != '') {
		var img = getFileExt(file_path);
		img = img.toLowerCase();
		if( img != 'jpeg' && img != 'jpg' && img != 'gif' && img != 'png'){
			alert(wb_msg_img_type_limit);
			return false;
		}
		var fileName = getFileName(file_path);
		if (check_chinese_character(fileName) == false){
			alert(wb_msg_name_with_CN);
			return false;
		}
	}
	
	for (i = 0; i < frm.mod_status_ind.length; i++) {
		if (frm.mod_status_ind[i].checked) {
			frm.mod_status.value = frm.mod_status_ind[i].value;
		}
	}
	
	
	if(frm.cert_end_yy.value !='' && frm.cert_end_mm.value !='' && frm.cert_end_dd.value !='' && frm.cert_end_yy !=null && frm.cert_end_mm !=null && frm.cert_end_dd !=null){
		var cert_end  = frm.cert_end_yy.value+"-"+frm.cert_end_mm.value+"-"+frm.cert_end_dd.value
		if(!isDate(cert_end)) {
			alert(wb_msg_enter_vaild_join_date);
			return false;
		}else{
			frm.cert_end.value = cert_end + " 23:59:59.0";
		}
	}else{
		alert(wb_msg_enter_vaild_join_date);
		return false;
	}
	
		
	
	frm.cert_id.value = cert_id;
	frm.cert_tc_id.value = frmXml.tc_id.options[0].value;
	frm.module.value = 'cert.CertificateModule';
	frm.url_success.value = wbCertListUrl();
	frm.url_failure.value = wbCertListUrl();
	frm.method = 'post';
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();

}

function isDate(dt){
	var res = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-9]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
	var re = new RegExp(res);
	return re.test(dt)
}

function getFileName(pathname) {
	var s = pathname.lastIndexOf("\\");
	if (s == -1) {s = pathname.lastIndexOf("/");}
	if (s == -1) {return pathname;}	
	var l = pathname.length - s;
	return pathname.substr(s+1,l);
}

function getFileExt(pathname) {
	var s = pathname.lastIndexOf(".");
	if (s == -1) {return pathname;}	
	var l = pathname.length - s;
	return pathname.substr(s+1,l);
}

function check_chinese_character(name) {
	if(name.search(/[^A-Za-z0-9 ~`!@%&()_=|{}:;"'<>,.\/\#\$\^\*\-\+\[\]\?\\]/)!=-1){
		return false;
	}
	return true;	
}

function wbCertificateDel(cert_id, lang) {
	msg = '';
	if(!confirm(wb_cert_delete_confirm)) {return;}
	
	url = wb_utils_invoke_disp_servlet(
	'module','cert.CertificateModule', 
	'cmd','del_cert','cert_id',cert_id,
	'url_success', wbCertListUrl(),
	'url_failure', wbCertListUrl());
	parent.location.href = url;
}

function wbCertificatePreview(cert_id, itm_id) {
	url = wb_utils_invoke_disp_servlet('module',
			'cert.CertificateModule', 'cmd', 'preview_cert',
			'cert_id', cert_id,'itm_id',itm_id);
	window.open(url, 'cert_preview',false,'toolbar=no,width=480,height=200,resizable=yes,status=no')
}


function wbCertificateDownload(cert_id, itm_id, tkh_id) {
	url = wb_utils_invoke_disp_servlet('module',
			'cert.CertificateModule', 'cmd', 'download_cert',
			'cert_id', cert_id,'itm_id',itm_id,'tkh_id',tkh_id);
	parent.location.href = url;
}

function wbCertListUrl() {
	return wb_utils_invoke_disp_servlet(
		'module', 'cert.CertificateModule', 
		'cmd', 'get_certificate_list',
		'stylesheet', 'certificate_list.xsl',
		'cert_tc_id',0
	)
}


function wbCertShowContent(tcr_id) {
  	url = wb_utils_invoke_disp_servlet("module","cert.CertificateModule","cmd", 'get_certificate_list', 'stylesheet', 'certificate_list.xsl', 'cert_tc_id', tcr_id);
	window.location.href = url;
}


function wbSetCertDefault(frm,lab_sel){
	   var obj_div = document.getElementById('cert_sel_form')
	   	if(obj_div != undefined){
	       pre_tcr_id = 0;
	       eval('frm.'+ field_name)[0].checked='checked';
	       eval('frm.'+ field_name+'_box').disabled='disabled';
	        eval('frm.'+ field_name+'_button').disabled='disabled';
			tempOption =  new Option
			tempOption.text = lab_sel;
			tempOption.value ='';
			addOption(eval('frm.'+ field_name+'_box'),tempOption)
			var obj_div = document.getElementById('cert_sel_form');
			var obj_div_no_cert = document.getElementById('no_cert')
			obj_div.style.display = 'none';
			obj_div_no_cert.style.display ='none';
		}	
		
	}

   function wbChangeCertSet(frm,obj,field_name,lab_sel){
      var obj_div = document.getElementById('cert_sel_form');
      var obj_div_no_cert = document.getElementById('no_cert');
      obj_div_no_cert.style.display ='none';
			if(obj.value == 'true' && frm.training_center__box.options[0].value != ''){
				
				var show = 0;
				for(i = 0; i < cert_tcr_id_list.length ; i++){
					 if(frm.training_center__box.options[0].value == cert_tcr_id_list[i]){
					   show = 1;
					 }
				}
				if(show == 1){
					obj_div_no_cert.style.display = 'none';
					obj_div.style.display = '';
					 eval('frm.'+ field_name+'_box').disabled='';
					eval('frm.'+ field_name+'_button').disabled='';
					tempOption =  new Option
					tempOption.text = lab_sel;
					tempOption.value ='';
//					addOption(eval('frm.'+ field_name+'_box'),tempOption)
					if(pre_tcr_id != frm.training_center__box.options[0].value){
						build_option(frm,frm.training_center__box.options[0].value,field_name);
						
					}
					 
				}else{
					 obj_div_no_cert.style.display ='';
				}
				pre_tcr_id = frm.training_center__box.options[0].value;
			}else{
					eval('frm.'+ field_name+'_box').disabled='disabled';
					eval('frm.'+ field_name+'_button').disabled='disabled';
					obj_div.style.display = 'none';
					eval('frm.'+ field_name+'_box').length =0;
					pre_tcr_id = 0;
			}
			
        }


  function wbItmPreviewCert(frm,field_name,desc_name_n,itm_id){
	   frm.itm_cfc_id.value = wbUtilsTrimString(eval('frm.'+ field_name+'_box').value);
			 if (frm.itm_cfc_id.value == '') {
			   alert(wb_msg_usr_please_select_a + ' ' + desc_name_n + ' ');
				  eval('frm.'+ field_name+'_box').focus()
				return false;
			 }else{
				  cert.preview_cert(frm.itm_cfc_id.value,itm_id);
			 }
 }



 function wbInitCertSet(frm,lab_sel,field_name){
			var obj = document.getElementById('cert_sel_form');
			tempOption =  new Option
			tempOption.text = lab_sel;
			tempOption.value ='';
			addOption(eval('frm.'+ field_name+'_box'),tempOption);
			if((itm_cfc_id > 0  ||eval('frm.'+ field_name)[1].checked == true) && frm.training_center__box.options[0].value != ''){
			    obj.style.display = '';
			    eval('frm.'+ field_name+'_box').disabled='';
				eval('frm.'+ field_name+'_button').disabled='';
			}else{
				 
			      eval('frm.'+ field_name+'_box').disabled='disabled';
				 eval('frm.'+ field_name+'_button').disabled='disabled';
				 obj.style.display = 'none';
			}
			if(frm.training_center__box.options[0].value != ''){
				build_option(frm,frm.training_center__box.options[0].value,field_name);
			}
			 pre_tcr_id = frm.training_center__box.options[0].value;
        }
        
  function build_option(frm,tcr_id, field_name){
            var select_index = 0;
            var opt_index = 1;
            if(eval('frm.'+ field_name+'_box').options.length==0){
            	opt_index = 0;
            }
	            for(i = 0; i < cert_id_list.length ; i++){
	                if(tcr_id == cert_tcr_id_list[i]){
					   tempOption =  new Option;
					   tempOption.text = cert_title_list[i];
					   tempOption.value = cert_id_list[i];
					   if(itm_cfc_id == cert_id_list[i]){
						   select_index = opt_index;
					   }
					   addOption(eval('frm.'+ field_name+'_box'),tempOption);
					   opt_index = opt_index +1;
	                }
	            }
            eval('frm.'+ field_name+'_box').options[select_index].selected=true;
        }
  function wbCertificateSearch(frm, tcr_id){
	    var cert_core = frm.cert_core.value;
	    var cert_status_sear = frm.cert_status_sear.value;
	    var cert_title = "";
		url = wb_utils_invoke_disp_servlet("module","cert.CertificateModule","cmd", 'search_certificate_list', 'stylesheet', 'certificate_list.xsl', 'cert_core',cert_core, 'cert_title',cert_title, 'cert_status_sear',cert_status_sear,'cert_tc_id',tcr_id );
		window.location.href = url;
  }
  
        