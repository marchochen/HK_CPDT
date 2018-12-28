function wbPoster() {
	this.poster_send_form = wbPosterSendForm
}

function wbPosterSendForm(frm, lang, rpt_type){
		frm.method = 'post';
		frm.action = wb_utils_servlet_url;
		frm.cmd.value = 'update_poster';
		frm.url_success.value = wb_utils_invoke_servlet('cmd', 'get_poster', 'stylesheet', 'poster_details.xsl','rpt_type',rpt_type,'tabId',tabVal);
		frm.rpt_type.value = rpt_type;
		//bill.lai 2016-01-27 设置验证登陆背景视频
		if(frm.login_bg_video){
			var loginBgFileName=frm.login_bg_video.value.toLowerCase();
			if(loginBgFileName!=''){
				s = loginBgFileName.lastIndexOf(".");
				l = loginBgFileName.length;
				suffx = loginBgFileName.substr(s+1,l);
				if(suffx != 'mp4' && suffx != 'MP4'){
					alert(eval('wb_msg_video_not_support'));
					if(frm.login_bg_video!==undefined){
						frm.login_bg_video.focus();
					}else{
						frm.login_bg_video.focus();
					}
					return false;
				}
			}			
		}
		
		if(frm.show_login_header_ind){
			if(frm.show_login_header_ind.checked){
				frm.show_login_header_ind.value = "true";
			} else {
				frm.show_login_header_ind.value = "false";
			}
		}
		
		if(frm.show_all_footer_ind){
			if(frm.show_all_footer_ind.checked){
				frm.show_all_footer_ind.value = "true";
			} else {
				frm.show_all_footer_ind.value = "false";
			}
		}
		
		if(frm.sp_welcome_word){
			if(frm.sp_welcome_word.value == ""){
				frm.sp_welcome_word.value = "";
			}
			
			if(getChars(frm.sp_welcome_word.value)>80){
				alert(eval('label_ss.label_core_system_setting_140'));
				return false;
			}
		}
		
		if(frm.mb_welcome_word){
			if(frm.mb_welcome_word.value == ""){
				frm.mb_welcome_word.value = frm.tmp_welcome_pc.value;
			}
		}
		if(frm.sp_logo_file_cn){
			var radio =getValueByName('rdo_sp_logo_cn');
			if(radio==3)
			{
				var newlogoFilename=frm.tmp_sp_logo_cn.value;
				if(newlogoFilename == '')
				{
					alert(eval('wb_msg_select_uploaded_picture'));
					return false;
				}
			}
			var logoFilename = frm.sp_logo_file_cn.value.toLowerCase();
			if(logoFilename != '' && wbMediaGetType(logoFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_logo_not_support'))
				if(frm.tmp_sp_logo_cn !== undefined) {
					frm.tmp_sp_logo_cn.focus();
				} else {
					frm.sp_logo_file_cn.focus();
				}
				return false;
			} 		
		}
		
		if(frm.sp_logo_file_hk){
			var radio = getValueByName('rdo_sp_logo_hk');
			if(radio==3)
			{
				var newlogoFilename=frm.tmp_sp_logo_hk.value;
				if(newlogoFilename == '')
				{
					alert(eval('wb_msg_select_uploaded_picture'));
					return false;
				}
			}
			var logoFilename = frm.sp_logo_file_hk.value.toLowerCase();
			if(logoFilename != '' && wbMediaGetType(logoFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_logo_not_support'))
				if(frm.tmp_sp_logo_hk !== undefined) {
					frm.tmp_sp_logo_hk.focus();
				} else {
					frm.sp_logo_file_hk.focus();
				}
				return false;
			}			
		}
		
		if(frm.sp_logo_file_us){
			var radio = getValueByName('rdo_sp_logo_us');
			if(radio==3)
			{
				var newlogoFilename=frm.tmp_sp_logo_us.value;
				if(newlogoFilename == '')
				{
					alert(eval('wb_msg_select_uploaded_picture'));
					return false;
				}
			}
			var logoFilename = frm.sp_logo_file_us.value.toLowerCase();
			if(logoFilename != '' && wbMediaGetType(logoFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_logo_not_support'))
				if(frm.tmp_sp_logo_us !== undefined) {
					frm.tmp_sp_logo_us.focus();
				} else {
					frm.sp_logo_file_cus.focus();
				}
				return false;
			}			
		}
		
		if(frm.tmp_sp_logo_cn != undefined && frm.rdo_sp_logo_cn !=undefined && getValueByName('rdo_sp_logo_cn') ==3){
			var logoFilename = frm.tmp_sp_logo_cn.value.toLowerCase()
			if(logoFilename != '' && wbMediaGetType(logoFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_logo_cn !== undefined) {
					frm.tmp_sp_logo_cn.focus();
				}
				return false;
			}
		}else if(frm.tmp_sp_logo_cn != undefined &&frm.rdo_sp_logo_cn !=undefined && getValueByName('rdo_sp_logo_cn') !=3){
			frm.tmp_sp_logo_cn.value = "";
		}
		
		if(frm.tmp_sp_logo_hk != undefined && frm.rdo_sp_logo_hk !=undefined && getValueByName('rdo_sp_logo_hk') ==3){
			var logoFilename = frm.tmp_sp_logo_hk.value.toLowerCase()
			if(logoFilename != '' && wbMediaGetType(logoFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_logo_hk !== undefined) {
					frm.tmp_sp_logo_hk.focus();
				}
				return false;
			}
		}else if(frm.tmp_sp_logo_hk != undefined &&frm.rdo_sp_logo_hk !=undefined && getValueByName('rdo_sp_logo_hk') !=3){
			frm.tmp_sp_logo_hk.value = "";
		}
		
		if(frm.tmp_sp_logo_us != undefined && frm.rdo_sp_logo_us !=undefined && getValueByName('rdo_sp_logo_us') ==3){
			var logoFilename = frm.tmp_sp_logo_us.value.toLowerCase()
			if(logoFilename != '' && wbMediaGetType(logoFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_logo_us !== undefined) {
					frm.tmp_sp_logo_us.focus();
				}
				return false;
			}
		}else if(frm.tmp_sp_logo_us != undefined &&frm.rdo_sp_logo_us !=undefined && getValueByName('rdo_sp_logo_us') !=3){
			frm.tmp_sp_logo_us.value = "";
		}
		
		if(frm.sp_media_file){
			var mediaFilename = frm.sp_media_file.value.toLowerCase()
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01 !== undefined) {
					frm.tmp_sp_media01.focus();
				} else {
					frm.sp_media_file.focus();
				}
				return false;
			}			
		}
		
		if(frm.sp_url.value){
			_url = frm.sp_url.value.toLowerCase()
			if (_url!='' && _url.substring(0,7) != "http://"&& _url.substring(0,8) != "https://"){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_poster_url.value + '\"')
				frm.sp_url.focus()
				return false;
			}			
		}
		
		if(frm.sp_media_file_1){
			var mediaFilename = frm.sp_media_file_1.value.toLowerCase()
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01_1 !== undefined) {
					frm.tmp_sp_media01_1.focus();
				} else {
					frm.sp_media_file_1.focus();
				}
				return false;
			}			
		}
		
		if(frm.sp_url_1){
			_url = frm.sp_url_1.value.toLowerCase()
			if (_url!='' && _url.substring(0,7) != "http://"&& _url.substring(0,8) != "https://"){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_poster_url.value + '\"')
				frm.sp_url_1.focus()
				return false;
			}
		}
		
		if(frm.sp_media_file_2){
			var mediaFilename = frm.sp_media_file_2.value.toLowerCase()
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01_2 !== undefined) {
					frm.tmp_sp_media01_2.focus();
				} else {
					frm.sp_media_file_2.focus();
				}
				return false;
			}			
		}
		
		if(frm.sp_url_2){
			_url = frm.sp_url_2.value.toLowerCase()
			if (_url!='' && _url.substring(0,7) != "http://"&& _url.substring(0,8) != "https://"){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_poster_url.value + '\"')
				frm.sp_url_2.focus()
				return false;
			}
		}
		
		if(frm.sp_media_file_3){
			var mediaFilename = frm.sp_media_file_3.value.toLowerCase()
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01_3 !== undefined) {
					frm.tmp_sp_media01_3.focus();
				} else {
					frm.sp_media_file_3.focus();
				}
				return false;
			}
		}
		
		if(frm.sp_url_3){
			_url = frm.sp_url_3.value.toLowerCase()
			if (_url!='' && _url.substring(0,7) != "http://"&& _url.substring(0,8) != "https://"){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_poster_url.value + '\"')
				frm.sp_url_3.focus()
				return false;
			}
		}		
		
		if(frm.sp_media_file_4){
			var mediaFilename = frm.sp_media_file_4.value.toLowerCase()
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01_4 !== undefined) {
					frm.tmp_sp_media01_4.focus();
				} else {
					frm.sp_media_file_4.focus();
				}
				return false;
			}
		}
					
		if(frm.sp_url_4){
			_url = frm.sp_url_4.value.toLowerCase()
			if (_url!='' && _url.substring(0,7) != "http://"&& _url.substring(0,8) != "https://"){
				alert(eval('wb_msg_' + lang + '_enter_valid') + ' \"' + frm.lab_poster_url.value + '\"')
				frm.sp_url_4.focus()
				return false;
			}
		}
		
		if(frm.tmp_sp_media01 != undefined && frm.rdo_sp_media01 !=undefined && getValueByName('rdo_sp_media01') ==3){
			var mediaFilename = frm.tmp_sp_media01.value.toLowerCase();
			if(mediaFilename == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01 !== undefined) {
					frm.tmp_sp_media01.focus();
				}
				return false;
			}
		}else if(frm.tmp_sp_media01 != undefined &&frm.rdo_sp_media01 !=undefined && getValueByName('rdo_sp_media01') !=3){
			frm.tmp_sp_media01.value = "";
		}
		
		if(frm.tmp_sp_media01_1 != undefined && frm.rdo_sp_media01_1 !=undefined && getValueByName('rdo_sp_media01_1') ==3){
			var mediaFilename = frm.tmp_sp_media01_1.value.toLowerCase();
			if(mediaFilename == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01_1 !== undefined) {
					frm.tmp_sp_media01_1.focus();
				}
				return false;
			}
		}else if(frm.tmp_sp_media01_1 != undefined &&frm.rdo_sp_media01_1 !=undefined && getValueByName('rdo_sp_media01_1') !=3){
			frm.tmp_sp_media01_1.value = "";
		}
		
		if(frm.tmp_sp_media01_2 != undefined && frm.rdo_sp_media01_2 !=undefined && getValueByName('rdo_sp_media01_2') ==3){
			var mediaFilename = frm.tmp_sp_media01_2.value.toLowerCase();
			if(mediaFilename == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01_2 !== undefined) {
					frm.tmp_sp_media01_2.focus();
				}
				return false;
			}
		}else if(frm.tmp_sp_media01_2 != undefined &&frm.rdo_sp_media01_2 !=undefined && getValueByName('rdo_sp_media01_2') !=3){
			frm.tmp_sp_media01_2.value = "";
		}
		
		if(frm.tmp_sp_media01_3 != undefined && frm.rdo_sp_media01_3 !=undefined && getValueByName('rdo_sp_media01_3') ==3){
			var mediaFilename = frm.tmp_sp_media01_3.value.toLowerCase();
			if(mediaFilename == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(mediaFilename != '' && wbMediaGetType(mediaFilename) == MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'))
				if(frm.tmp_sp_media01_3 !== undefined) {
					frm.tmp_sp_media01_3.focus();
				}
				return false;
			}
		}else if(frm.tmp_sp_media01_3 != undefined &&frm.rdo_sp_media01_3 !=undefined && getValueByName('rdo_sp_media01_3') !=3){
			frm.tmp_sp_media01_3.value = "";
		}
		
		if(frm.tmp_login_bg_file5 != undefined && frm.rdo_login_bg5 !=undefined && getValueByName('rdo_login_bg5') ==3){
			var loginBgFileName=frm.tmp_login_bg_file5.value.toLowerCase();
			if(loginBgFileName == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file5!==undefined){
					frm.tmp_login_bg_file5.focus();
				}
				return false;
			}
		}else if(frm.tmp_login_bg_file5 != undefined &&frm.rdo_login_bg5 !=undefined && getValueByName('rdo_login_bg5') !=3){
			frm.tmp_login_bg_file5.value = "";
		}
		
		if(frm.tmp_login_bg_file4 != undefined && frm.rdo_login_bg4 !=undefined && getValueByName('rdo_login_bg4') ==3){
			var loginBgFileName=frm.tmp_login_bg_file4.value.toLowerCase();
			if(loginBgFileName == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file4!==undefined){
					frm.tmp_login_bg_file4.focus();
				}
				return false;
			}
		}else if(frm.tmp_login_bg_file4 != undefined &&frm.rdo_login_bg4 !=undefined && getValueByName('rdo_login_bg4') !=3){
			frm.tmp_login_bg_file4.value = "";
		}
		
		if(frm.tmp_login_bg_file3 != undefined && frm.rdo_login_bg3 !=undefined && getValueByName('rdo_login_bg3') ==3){
			var loginBgFileName=frm.tmp_login_bg_file3.value.toLowerCase();
			if(loginBgFileName == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file3!==undefined){
					frm.tmp_login_bg_file3.focus();
				}
				return false;
			}
		}else if(frm.tmp_login_bg_file3 != undefined &&frm.rdo_login_bg3 !=undefined && getValueByName('rdo_login_bg3') !=3){
			frm.tmp_login_bg_file3.value = "";
		}
		
		if(frm.tmp_login_bg_file2 != undefined && frm.rdo_login_bg2 !=undefined && getValueByName('rdo_login_bg2') ==3){
			var loginBgFileName=frm.tmp_login_bg_file2.value.toLowerCase();
			if(loginBgFileName == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file2!==undefined){
					frm.tmp_login_bg_file2.focus();
				}
				return false;
			}
		}else if(frm.tmp_login_bg_file2 != undefined &&frm.rdo_login_bg2 !=undefined && getValueByName('rdo_login_bg2') !=3){
			frm.tmp_login_bg_file2.value = "";
		}
		
		if(frm.tmp_login_bg_file1 != undefined && frm.rdo_login_bg1 !=undefined && getValueByName('rdo_login_bg1') ==3){
			var loginBgFileName=frm.tmp_login_bg_file1.value.toLowerCase();
			if(loginBgFileName == '') {
				alert(eval('wb_msg_select_uploaded_picture'));
				return false;
			}
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file1!==undefined){
					frm.tmp_login_bg_file1.focus();
				}
				return false;
			}
		}else if(frm.tmp_login_bg_file1 != undefined &&frm.rdo_login_bg1 !=undefined && getValueByName('rdo_login_bg1') !=3){
			frm.tmp_login_bg_file1.value = "";
		}
		
		if(frm.tmp_login_bg_video != undefined  && frm.rdo_login_video !=undefined && getValueByName('rdo_login_video') ==3){
			var loginBgFileName=frm.tmp_login_bg_video.value.toLowerCase();
			if(loginBgFileName == '') {
				alert(eval('wb_msg_select_uploaded_file'));
				return false;
			}
			var flag = false;
			s = loginBgFileName.lastIndexOf(".");
			if (s == -1)
				flag = true;
			l = loginBgFileName.length;
			suffx = loginBgFileName.substr(s+1,l);
			suffx = suffx.toLowerCase();
			if (suffx != "mp4")
				flag = true;
			
			if(loginBgFileName!=''&&flag){
				alert(eval('wb_msg_video_not_support'));
				if(frm.tmp_login_bg_video!==undefined){
					frm.tmp_login_bg_video.focus();
				}
				return false;
			}
		}else if(frm.tmp_login_bg_video != undefined &&frm.rdo_login_video !=undefined && getValueByName('rdo_login_video') !=3){
			frm.tmp_login_bg_video.value = "";
		}
		
		if(frm.login_bg_file1){
			var loginBgFileName=frm.login_bg_file1.value.toLowerCase();
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file1!==undefined){
					frm.tmp_login_bg_file1.focus();
				}else{
					frm.login_bg_file1.focus();
				}
				return false;
			}
		}
		
		if(frm.login_bg_file2){
			var loginBgFileName=frm.login_bg_file2.value.toLowerCase();
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file2!==undefined){
					frm.tmp_login_bg_file2.focus();
				}else{
					frm.login_bg_file2.focus();
				}
				return false;
			}			
		}
		
		if(frm.login_bg_file3){
			var loginBgFileName=frm.login_bg_file3.value.toLowerCase();
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file3!==undefined){
					frm.tmp_login_bg_file3.focus();
				}else{
					frm.login_bg_file3.focus();
				}
				return false;
			}			
		}
		if(frm.login_bg_file4){
			var loginBgFileName=frm.login_bg_file4.value.toLowerCase();
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file4!==undefined){
					frm.tmp_login_bg_file4.focus();
				}else{
					frm.login_bg_file4.focus();
				}
				return false;
			}			
		}
		if(frm.login_bg_file5){
			var loginBgFileName=frm.login_bg_file5.value.toLowerCase();
			if(loginBgFileName!=''&&wbMediaGetType(loginBgFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_login_bg_file5!==undefined){
					frm.tmp_login_bg_file5.focus();
				}else{
					frm.login_bg_file5.focus();
				}
				return false;
			}			
		}
		
		if(frm.guide_file1){
			var guideFileName=frm.guide_file1.value.toLowerCase();
			if(guideFileName!=''&&wbMediaGetType(guideFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_guide_file1!==undefined){
					frm.tmp_guide_file1.focus();
				}else{
					frm.guide_file1.focus();
				}
				return false;
			}	
		}
		if(frm.guide_file2){
			var guideFileName=frm.guide_file2.value.toLowerCase();
			if(guideFileName!=''&&wbMediaGetType(guideFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_guide_file2!==undefined){
					frm.tmp_guide_file2.focus();
				}else{
					frm.guide_file2.focus();
				}
				return false;
			}	
		}
		if(frm.guide_file3){
			var guideFileName=frm.guide_file3.value.toLowerCase();
			if(guideFileName!=''&&wbMediaGetType(guideFileName)==MEDIA_TYPE_UNKNOWN){
				alert(eval('wb_msg_media_not_support'));
				if(frm.tmp_guide_file3!==undefined){
					frm.tmp_guide_file3.focus();
				}else{
					frm.guide_file3.focus();
				}
				return false;
			}	
		}
		if(!(frm.rdo_sp_media01 && frm.rdo_sp_media01[1] && frm.rdo_sp_media01[1].checked) 
				&& !(frm.rdo_sp_media01_1 && frm.rdo_sp_media01_1[1] && frm.rdo_sp_media01_1[1].checked)
				&& !(frm.rdo_sp_media01_2 && frm.rdo_sp_media01_2[1] && frm.rdo_sp_media01_2[1].checked)
				&& !(frm.rdo_sp_media01_3 && frm.rdo_sp_media01_3[1] && frm.rdo_sp_media01_3[1].checked)
				){
			if(frm.sp_media_file.value != '' && frm.sp_media_file_1.value != '' && frm.sp_media_file_2.value != '' && frm.sp_media_file_3.value != ''){
				wb_utils_preloading();
			}
		}
		if(!(frm.rdo_login_bg1&&frm.rdo_login_bg1[1]&&frm.rdo_login_bg1[1].checked)
			&&!(frm.rdo_login_bg2&&frm.rdo_login_bg2[1]&&frm.rdo_login_bg2[1].checked)
			&&!(frm.rdo_login_bg3&&frm.rdo_login_bg3[1]&&frm.rdo_login_bg3[1].checked)
			&&!(frm.rdo_login_bg4&&frm.rdo_login_bg4[1]&&frm.rdo_login_bg4[1].checked)
			&&!(frm.rdo_login_bg5&&frm.rdo_login_bg5[1]&&frm.rdo_login_bg5[1].checked)){
			if(frm.login_bg_file1 &&frm.login_bg_file1.value != '' && frm.login_bg_file2 && frm.login_bg_file2.value != '' &&  frm.login_bg_file3 && frm.login_bg_file3.value != '' && frm.login_bg_file4 && frm.login_bg_file4.value != '' && frm.login_bg_file5 && frm.login_bg_file5.value != ''){
				wb_utils_preloading();
			}
		}
		if(!(frm.rdo_guide_bg1&&frm.rdo_guide_bg1[1]&&frm.rdo_guide_bg1[1].checked)
				&&!(frm.rdo_guide_bg2&&frm.rdo_guide_bg2[1]&&frm.rdo_guide_bg2[1].checked)
				&&!(frm.rdo_guide_bg3&&frm.rdo_guide_bg3[1]&&frm.rdo_guide_bg3[1].checked)){
			if(frm.guide_file1.value != '' && frm.guide_file2 && frm.guide_file2.value != '' &&  frm.guide_file3 && frm.guide_file3.value != '' ){
				wb_utils_preloading();
			}
		}
		frm.submit();
}

//获取radio选中的value
function getValueByName(name){
	 var New = document.getElementsByName(name);
	 for(var i=0;i<New.length;i++){
	    if(New.item(i).checked){
	    	return New.item(i).getAttribute("value"); 
	    	break;
	    }else{
	    	continue;
	    }
	 }
}
