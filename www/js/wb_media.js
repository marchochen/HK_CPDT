// ------------------ wizBank Enrolment object ------------------- 
// Convention:
//   public functions : use "wbMedia" prefix 
//   private functions: use "_wbMedia" prefix
// Dependency:
//   
//   
// ------------------------- function declaration ------------------------- 

MEDIA_TYPE_GIF = "image/gif"
MEDIA_TYPE_JPG = "image/jpg"
MEDIA_TYPE_SWF = "application/x-shockwave-flash"
MEDIA_TYPE_UNKNOWN ="unknown"
FILE_TYPE ="application"

//-----------------------------------------------------------------------------

/* constructor */

function wbMediaGetType(filename,file_type) {	
	s = filename.lastIndexOf(".");
	if (s == -1)
		return MEDIA_TYPE_UNKNOWN;

	l = filename.length;
	suffx = filename.substr(s+1,l);

	if ( file_type == 'file') 
		return FILE_TYPE
	else {
		
		suffx = suffx.toLowerCase();
		
		if (suffx == "gif")
			return MEDIA_TYPE_GIF;
		else if (suffx == "jpg" || suffx == "jpeg" || suffx == "png")
			return MEDIA_TYPE_JPG;
//		else if (suffx == "swf")
//			return MEDIA_TYPE_SWF;
		else
			return MEDIA_TYPE_UNKNOWN;
	}
}


function wbMediaShow(file_name) {
	
	var url = '../htm/show_media.htm?' + file_name;
	newBrowser = wbUtilsOpenWin(url,'ShowMediaFile',false,'width=380, height=320, scrollbars=yes');
	
	return;
}	

function wbMediaAdd(){
	url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','get_media.xsl')
	newBrowser = wbUtilsOpenWin(url, 'GetMediaFile',false,'width=350, height=115');
	return;
}
	
function wbMediaDel(lang,type) {
	if(confirm(eval('wb_msg_'+lang+'_confirm'))){
		if (type == 'mc' )
			document.MULTIPLECHOICE.del_media_file();
		else if ( type == 'mt' )
			document.MATCHING.del_media_file();
		else if ( type == 'fb' )
			document.FB_QUESTION.del_media_file();
	}
	return;
}	
	
function wbMediaSetFile(media_name,type) {
	if (type == 'MC' )
			document.MULTIPLECHOICE.add_media_file(media_name);
		else if ( type == 'MT' )
			document.MATCHING.add_media_file(media_name);			
		else if ( type == 'FB' )
			document.FB_QUESTION.add_media_file(media_name);
	return;
}		


function wbMediaGetFileName(pathname) {

	s = pathname.lastIndexOf("\\");
	if (s == -1) {
		s = pathname.lastIndexOf("/");
	}
	if (s == -1) {
		return pathname;
	}
	l = pathname.length - s;
	return pathname.substr(s+1,l);
}

function wbMediaSendForm(frm,lang,file_type){
	frm.action = wb_utils_servlet_url
	if(frm.file.value == "") {
		close();
		return; 
	}
	
	var mediaFilename = frm.file.value;
	if (wbMediaGetType(mediaFilename,file_type) == MEDIA_TYPE_UNKNOWN) {
		alert(eval('wb_msg_' + lang + '_media_not_support'))
		frm.file.focus();		
	}else {
		frm.res_filename.value = escHTML(frm.file.value)
		wb_utils_preloading();
		frm.submit();
		
	}
}

function escHTML(str) {
	str = str.replace(/\\/g, "@")
	return str
}


function backHTML(str) {

//str = str.replace(/\\/g, "[slash]")
	str = str.replace(/@/g, "\\")
	return str
}


/*
function wbMediaSendFileName(file_name) {
	opener.set_media_file(file_name);		
}
*/
function wbMediaSendFileName(file_name, new_file_name) {
	opener.set_media_file(file_name, new_file_name);
}