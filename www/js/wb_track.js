// ------------------ wizBank Course object ------------------- 
// Convention:
//   public functions : use "wbTrack" prefix 
//   private functions: use "_wbTrack" prefix
// ------------------------------------------------------------ 
// -----------------------------------------------------------

/* constructor */
function wbTrack() {	
	this.defaultRefreshParent = true;
	this.track = wbTrackProgress
	this.send_prog = wbTrackSendProgress
	this.send_last_prog = wbTrackSendLastProgress
	this.send_last_prog_url = wbTrackSendLastProgressUrl
	this.module_track_url = wbTrackModuleTrackURL
	this.send_module_data = wbTrackSendModuleData
	this.wizpack_track_url = wbTrackWiaPackTrackURL
	this.set_cookie = wbTrackSetCookie
	this.get_cookie = wbTrackGetCookie
	this.del_auto_save = wbTrackDelAutoSave
}
// private function

function _wbTrackGetTime(datetime){

		if ( datetime.getHours() > 9 )
			hour = datetime.getHours()
		else
			hour = '0' + (datetime.getHours() + 1 )
		
		if ( datetime.getMinutes() > 9 )
			minutes = datetime.getMinutes()
		else
			minutes = '0' + datetime.getMinutes()
		
		if ( datetime.getSeconds() > 9 )
			seconds = datetime.getSeconds()
		else
			seconds = '0' + datetime.getSeconds()			
			
		time = hour + ':' + minutes + ':' + seconds
		
		return time;	

}


function _wbTrackGetDate(datetime){

		if ( datetime.getMonth() > 9 )
			month = datetime.getMonth() + 1 
		else
			month = '0' + (datetime.getMonth() + 1 )
		if ( datetime.getDate() > 9 )
			day = datetime.getDate()
		else
			day = '0' + datetime.getDate()
			
		date = datetime.getFullYear() + '/' + month + '/' + day
		
		return date;

}

function _wbTrackChangeDurationFormat(millisecond){
	total_second = millisecond/1000	
	hour = (total_second - total_second%3600)/3600
	if ( hour < 10 )
		hour = '0' + hour
	total_second = total_second%3600
	minutes = (total_second - total_second%60)/60
	if ( minutes < 10 )
		minutes = '0' + minutes
		
	second = total_second%60
	if ( second < 10 )
		second = '0' + second
	return hour +':' + minutes + ':' + second

}

// public function 
function wbTrackProgress(){

	cur_item = window.parent.wizpack_content.location.href
	cur_item = cur_item.substring(cur_item.indexOf('resource/'))
	if ( cur_item != prev_item ){
		
		for(j=total_record-new_record ; j< total_record; j++){
			cur_datetime = new Date()
			record[j].date = _wbTrackGetDate(cur_datetime)
			record[j].time = _wbTrackGetTime(cur_datetime)
		}	
		new_record=0;
		
		for (i in  items){
			if ( cur_item == items[i]){
				record[total_record] = new RecordData(i)	
				total_record ++;
				new_record++;
			}		
		}				
		prev_item = cur_item		
	}else{
		for(j=total_record-new_record ; j< total_record; j++){
			record[j].duration ++;					
		}	
	}		
	
}

function wbTrackSendProgress(){


	frm = window.parent.wizpack_track.document.frmXml
	
	frm.element_location.value = ''
	frm.date.value = ''
	frm.time.value = ''
	frm.time_in_element.value = ''
	frm.why_left.value = ''
	frm.status.value = ''
		
	var i = 0;

	while ( i< total_record){
		if ( record[i].date != '' ){	
			frm.element_location.value += record[i].ele_location +'~'
			duration = _wbTrackChangeDurationFormat(record[i].duration * 1000)
			frm.time_in_element.value += duration + '~'
			frm.date.value += record[i].date + '~'
			frm.time.value += record[i].time + '~'
			frm.why_left.value += 'S~'
			frm.status.value += 'C~'
												
			for (j=i; j< total_record-1; j++)
				record[j] = record[j+1]			
			delete record[total_record-1]
			total_record --;			
		}else
			i++
	}
	
	frm.action = wb_utils_servlet_url
	frm.url_success.value =  window.parent.wizpack_track.location.href
	frm.cmd.value = 'putpath'
	frm.method = "get"	

	if ( frm.element_location.value.length > 0 )
		frm.submit()
	
	
}


function wbTrackSendLastProgress(){

	for(j=0 ; j< total_record; j++){	
		if ( record[j].date == ''){
			cur_datetime = new Date()
			record[j].date = _wbTrackGetDate(cur_datetime)
			record[j].time = _wbTrackGetTime(cur_datetime)
		}
	}		

	
	frm = window.parent.wizpack_track.document.frmXml
	
	frm.element_location.value = ''
	frm.date.value = ''
	frm.time.value = ''
	frm.time_in_element.value = ''
	frm.why_left.value = ''
	frm.status.value = ''
		
	var i = 0;

	while ( i< total_record){		
			frm.element_location.value += record[i].ele_location +'~'
			duration = _wbTrackChangeDurationFormat(record[i].duration * 1000)
			frm.time_in_element.value += duration + '~'
			frm.date.value += record[i].date + '~'
			frm.time.value += record[i].time + '~'
			frm.why_left.value += 'S~'
			frm.status.value += 'C~'		
			i++
	}
	
	this.set_cookie('last_element_location',record[i-1].ele_location)
	
//	frm.action = wb_utils_servlet_url
	//frm.url_success.value = 'javascript:window.close()'
	//frm.method = "post"	
	
//	frm.submit()
	this.set_cookie('course_id',frm.course_id.value)
	this.set_cookie('student_id',frm.student_id.value)
	this.set_cookie('lesson_id',frm.lesson_id.value)
	this.set_cookie('element_location',frm.element_location.value)
	this.set_cookie('date',frm.date.value)
	this.set_cookie('time',frm.time.value)
	this.set_cookie('time_in_element',frm.time_in_element.value)					
	this.set_cookie('why_left',frm.why_left.value)		
	this.set_cookie('status',frm.status.value)		
					
	
	str_feature = 'toolbar='		+ 'no'
			+ ',titlebar='		+ 'no'
			+ ',width=' 				+ '1'
			+ ',height=' 				+ '1'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',z-lock='				+ 'yes'
			
	url = wb_utils_invoke_servlet('cmd','getpath','course_id',frm.course_id.value,'student_id',frm.student_id.value,'lesson_id',frm.lesson_id.value,'stylesheet','wizpack_last_track.xsl','url_failure','../htm/close_window.htm')
	url = "javascript:self.blur();window.location.href='" + url + "'"
	wbUtilsOpenWin(url, 'wizpack_tracking', false, str_feature);		
	
	
	
}
	
function wbTrackSendLastProgressUrl(){
	
	var refreshParent = this.defaultRefreshParent;
	var url_failure;
	if (refreshParent == false) {url_failure = '../htm/close_window.htm';}
	else {url_failure = '../htm/close_and_reload_window.htm';}	

	for(j=0 ; j< total_record; j++){	
		if ( record[j].date == ''){
			cur_datetime = new Date()
			record[j].date = _wbTrackGetDate(cur_datetime)
			record[j].time = _wbTrackGetTime(cur_datetime)
		}
	}		
	
	frm = window.parent.wizpack_track.document.frmXml	
	frm.element_location.value = ''
	frm.date.value = ''
	frm.time.value = ''
	frm.time_in_element.value = ''
	frm.why_left.value = ''
	frm.status.value = ''
		
	var i = 0;

	while ( i< total_record){		
			frm.element_location.value += record[i].ele_location +'~'
			duration = _wbTrackChangeDurationFormat(record[i].duration * 1000)
			frm.time_in_element.value += duration + '~'
			frm.date.value += record[i].date + '~'
			frm.time.value += record[i].time + '~'
			frm.why_left.value += 'S~'
			frm.status.value += 'C~'		
			i++
	}
	
	this.set_cookie('last_element_location',record[i-1].ele_location)
	
	//	frm.action = wb_utils_servlet_url
		//frm.url_success.value = 'javascript:window.close()'
		//frm.method = "post"	
		
	//	frm.submit()
	this.set_cookie('course_id',frm.course_id.value)
	this.set_cookie('student_id',frm.student_id.value)
	this.set_cookie('lesson_id',frm.lesson_id.value)
	this.set_cookie('element_location',frm.element_location.value)
	this.set_cookie('date',frm.date.value)
	this.set_cookie('time',frm.time.value)
	this.set_cookie('time_in_element',frm.time_in_element.value)					
	this.set_cookie('why_left',frm.why_left.value)		
	this.set_cookie('status',frm.status.value)		
					
	
	str_feature = 'toolbar='		+ 'no'
			+ ',titlebar='		+ 'no'
			+ ',width=' 				+ '1'
			+ ',height=' 				+ '1'
			+ ',scrollbars='			+ 'no'
			+ ',resizable='				+ 'no'
			+ ',z-lock='				+ 'yes'
			
	url = wb_utils_invoke_servlet(
		'cmd','getpath',
		'course_id',frm.course_id.value,
		'student_id',frm.student_id.value,
		'lesson_id',frm.lesson_id.value,
		'stylesheet','wizpack_last_track.xsl',
		'url_failure',url_failure
	)
	url = "javascript:self.blur();window.location.href='" + url + "'"	
	return url;
}

function wbTrackModuleTrackURL(usr_id,mod_type,mod_id,cos_id, tkh_id){

	url = wb_utils_invoke_servlet('cmd','get_mod_status','usr_id',usr_id,'stylesheet','module_track.xsl','mod_type',mod_type,'mod_id',mod_id,'cos_id',cos_id,'tkh_id',tkh_id)
	return url;
}

function wbTrackSendModuleData(frm, isParent, send_url, mod_type, tkh_id, closeParentOnSuccess) {
	if (isParent == null || isParent == '') {
		isParent = false;
	}
	if (!frm) {
		frm = document.frmXml;
	}
	if (isParent == true) {
		url = send_url;
	} else {		
		cur_datetime = new Date()
		org_time = new Date(frm.cur_time.value)
		d = cur_datetime - org_time
		duration = _wbTrackChangeDurationFormat(d)	
		url = wb_utils_invoke_servlet(
			'cmd','putparam',
			'course_id',frm.course_id.value,
			'student_id',frm.student_id.value,
			'lesson_id',frm.lesson_id.value,
			'lesson_location',frm.lesson_location.value,
			'lesson_status',frm.lesson_status.value,
			'time',duration,
			'start_time', frm.start_time.value,
			'encrypted_start_time', frm.encrypted_start_time.value,
			'tkh_id', tkh_id
		)
	}
	if ( wb_utils_get_cookie('isWizpack') == 'true' ){
		if (isParent == true){	
			//alert('inset')
			//alert(_getVariableUrlParam('course_id',url))
			//alert(_getVariableUrlParam('student_id',url))
			//alert(_getVariableUrlParam('lesson_id',url))
			//alert(_getVariableUrlParam('lesson_location',url))
			//alert(_getVariableUrlParam('time',url))
			wbTrackSetCookie('mod_course_id',_getVariableUrlParam('course_id',url))
			wbTrackSetCookie('mod_student_id',_getVariableUrlParam('student_id',url))
			wbTrackSetCookie('mod_lesson_id',_getVariableUrlParam('lesson_id',url))
			wbTrackSetCookie('mod_lesson_location',_getVariableUrlParam('lesson_location',url))
			wbTrackSetCookie('mod_time',_getVariableUrlParam('time',url))			
		}else{
			wbTrackSetCookie('mod_course_id',frm.course_id.value)
			wbTrackSetCookie('mod_student_id',frm.student_id.value)
			wbTrackSetCookie('mod_lesson_id',frm.lesson_id.value)
			wbTrackSetCookie('mod_lesson_location',frm.lesson_location.value)
			wbTrackSetCookie('mod_time',duration)		
		}
	}else{		
//		var trk_req = getXMLHttpRequest();
//		trk_req.open("GET", url, true);
//		trk_req.send();

		$.ajax({
			url : url,
			async : false,
			success : function(req, options) {
				if (closeParentOnSuccess) {
					var ua = navigator.userAgent.toLowerCase();
					if (window.parent && window.parent.closed === false && !((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))) {
						window.parent.close();
					}
				}
			},
			error : function(req, options) {
				
			},
			method : 'GET'
		});
	}
}

function wbTrackWiaPackTrackURL(usr_id){

	url = wb_utils_invoke_servlet('cmd','get_prof','usr_id',usr_id,'stylesheet','wizpack_track.xsl')
	return url;
}	


function wbTrackSetCookie(token_nm,token_val){
	gen_set_cookie_token('track', token_nm, token_val, '')
}

function wbTrackGetCookie(token_nm){
	return gen_get_cookie_token('track', token_nm)
}

function _getVariableUrlParam(name,url) {
	if (name != null && name != '') {name = '&' + name;}
	strParam = url.substring(url.indexOf('?'), url.length)
	
	idx1 = strParam.indexOf(name + "=")
	if (idx1 == -1)	return ""

	idx1 = idx1 + name.length + 1
	idx2 = strParam.indexOf("&", idx1)

	if (idx2 != -1)
		len = idx2 - idx1
	else
		len = strParam.length
	
	return unescape(strParam.substr(idx1, len))
}

function wbTrackDelAutoSave(frm, mod_id, tkh_id) {
	frm.method = "get";
	frm.module.value = "autosave.AutoSaveModule";
	frm.cmd.value = 'del_auto_save';
	frm.tkh_id.value = tkh_id;
	frm.mod_id.value = mod_id;
	frm.action = wb_utils_disp_servlet_url;
	frm.submit();
}