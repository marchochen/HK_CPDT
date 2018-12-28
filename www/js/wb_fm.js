// ------------------ wizBank Facility Management object ------------------- 
// Convention:
//   public functions : use "wbFm" prefix 
//   private functions: use "_wbFm" prefix
// Dependency:
//   gen_utils.js
//   wb_utils.js
// ------------------------------------------------------------ 

/* constructor */
var isExcludes = false
function wbFm(isExcludesParams){
	if(isExcludesParams != null && isExcludesParams != undefined && isExcludesParams != ""){
		isExcludes = isExcludesParams;
	}
	
	//gen
	this.get_fm = wbFmGetFacilityManagement
	this.get_itm_fm = wbFmGetItemFacilityManagement
	this.flush_cart = wbFmFlushCart //remove cart item when close win
	this.flush_cart_exec = wbFmFlushCartExec
	this.get_blank_url =wbFmGetBlankURL
	this.check_timeslot = _wbFmCheckTimeSlot
	this.clear_rsv_id = wbFmClearReservationId
	
	this.get_facility_list_url = wbFmGetFacilityListURL
	this.get_cart = wbFmGetCart
	
	//new rsv
	this.get_new_rsv = wbFmGetNewReservation
	this.feed_fac_id = wbFmFeedRsvFacId
	this.feed_start_date = wbFmFeedRsvStartDate
	this.feed_end_date = wbFmFeedRsvEndDate 
	this.pick_run_prep = wbFmPickRunPrep;
	this.pick_run_exec = wbFmPickRunExec;
	
	//reservation
	this.new_rsv_exec = wbFmNewReservationExec
	this.new_rsv_conflict_exec = wbFmNewReservationConflictExec
	
	this.get_cart = wbFmGetCart
	this.get_rsv_details = wbFmGetReservationDetails
	this.get_rsv_details_url = wbFmGetReservationDetailsUrl
	this.rsv_add_exec = wbFmReservationAddExec
	 
	this.rsv_upd_prep = wbFmReservationUpdatePrep
	this.rsv_upd_exec = wbFmReservationUpdateExec
	
	//remove reservation
	this.cancel_rsv_prep = wbFmCancelReservationPrep
	this.cancel_rsv_exec = wbFmCancelReservationExec
	
	//remove reservation facility
	this.cancel_rsv_fac_prep = wbFmCancelReservationFacilityPrep
	this.cancel_rsv_fsh_prep = wbFmCancelReservationFshPrep
	this.cancel_rsv_fac_exec = wbFmCancelReservationFacilityExec
	
	this.cancel_fs_exec = wbFmCancelCartFsExec
	this.cancel_fs_single_exec = wbFmCancelCartFsSingleExec
	
	//rsv calendar
	this.get_rsv_calendar_prep = wbFmGetReservationCalendarPrep
	this.get_rsv_calendar_exec = wbFmGetReservationCalendarExec
	
	this.get_rsv_calendar_prev = wbFmGetReservationCalendarPrev
	this.get_rsv_calendar_next = wbFmGetReservationCalendarNext
	
	//rsv record srh
	this.get_rsv_record_srh = wbFmGetReservationRecordSrh
	this.get_rsv_record_srh_exec = wbFmGetReservationRecordSrhExec
	
	//download rsv record
	this.get_rsv_record_csv = wbFmGetReservationRecordCSV
	
	//facility info
	this.get_facility_info_lst = wbFmGetFacilityInformationList	
	
	this.ins_facility_details_prep = wbFmInsFacilityDetailsPrep
	this.ins_facility_details_exec = wbFmInsFacilityDetailsExec
	
	this.get_facility_details = wbFmGetFacilityDetails
	this.get_facility_details_win = wbFmGetFacilityDetailsWin
	this.get_facility_details_url = wbFmGetFacilityDetailsURL
	
	this.edit_facility_details_prep = wbFmEditFacilityDetailsPrep
	this.edit_facility_details_exec = wbFmEditFacilityDetailsExec
	
	this.del_facility_details = wbFmDelFacilityDetails	
	this.set_cart = wbFmSetCart
	
	this.search_facility_fee_prep = wbFmSearchFacFeePrep
}
/* public functions */
function wbFmSetCart(val){
	if(val!=null && val!="" && (wb_utils_fm_get_cookie("cur_rsv_id")!="" || wb_utils_fm_get_cookie("cart")!="")){
		wb_utils_fm_set_cookie("get_cart",val);
		wb_utils_fm_set_cookie("cur_rsv_id","");
	}else{
		wb_utils_fm_set_cookie("get_cart","");
	}
}

function wbFmClearReservationId(){
	wb_utils_fm_set_cookie('_fm_rsv_id','')
	//wb_utils_fm_set_cookie('rsv_itm_title','')
}


function wbFmGetBlankURL(){
	var url
	url= wb_utils_invoke_servlet('cmd','get_prof', 'stylesheet','fm_blank.xsl')
	url += "&isExcludes="+ isExcludes
	return url
}
function wbFmFlushCartExec(frm){
/*../servlet/Dispatcher?module=fm.FMModule&cmd=del_fsh&fsh_fac_id=5~6~7&fsh_start_time={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}&fsh_upd_timestamp={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}*/
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.cmd.value = 'del_fsh'
			frm.module.value = 'fm.FMModule'
			frm.submit()
			window.close()
}

function wbFmFlushCart(){
		var url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','get_cart', 'stylesheet','fm_flush.xsl')
		url += "&isExcludes="+ isExcludes
		var str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '100'
			+ ',height=' 				+ '100'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',screenX='				+ '-500'
			+ ',screenY='				+ '-500'
			+ ',status='				+ 'yes'
		if(document.all){
			str_feature += ',top='		+ '-500'
			str_feature += ',left='		+ '-500'
		}	
		fm_flush_win = wbUtilsOpenWin(url,'fm_flush',false,str_feature)
		fm_flush_win.blur();
}

function wbFmGetFacilityManagement(){
		wb_utils_fm_set_cookie('old_rsv_return_url',wb_utils_fm_get_cookie('rsv_return_url'))
		wb_utils_fm_set_cookie('old_rsv_itm_id',wb_utils_fm_get_cookie('rsv_itm_id'))
		wb_utils_fm_set_cookie('old_rsv_itm_title',wb_utils_fm_get_cookie('rsv_itm_title'))
		wb_utils_fm_set_cookie('old_work_rsv_itm_title',wb_utils_fm_get_cookie('work_rsv_itm_title'))
		wb_utils_fm_set_cookie('old_url_success',wb_utils_fm_get_cookie('url_success'))
		wb_utils_fm_set_cookie('old_cur_rsv_id',wb_utils_fm_get_cookie('cur_rsv_id'))
		wb_utils_fm_set_cookie('old_work_rsv_id',wb_utils_fm_get_cookie('work_rsv_id'))
		wb_utils_fm_set_cookie('old_cart',wb_utils_fm_get_cookie('cart'))
	
		wb_utils_fm_set_cookie('rsv_return_url','')
		wb_utils_fm_set_cookie('rsv_itm_id','')
		wb_utils_fm_set_cookie('rsv_itm_title','')
		wb_utils_fm_set_cookie('work_rsv_itm_title','')
		wb_utils_fm_set_cookie('url_success','')
		wb_utils_fm_set_cookie('cur_rsv_id','')
		wb_utils_fm_set_cookie('work_rsv_id','')
		wb_utils_fm_set_cookie('cart','')
		wb_utils_fm_set_cookie('get_cart','')
	
		var url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','get_cart', 'stylesheet','fm_frame.xsl')
		url += "&isExcludes="+ isExcludes
		var str_feature = 'width=' 				+ '780'
			+ ',height=' 				+ '500'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',toolbar='				+ 'yes'
			+ ',screenX='				+ '10'
			+ ',screenY='				+ '10'
			+ ',status='				+ 'yes'
		if(document.all){
			str_feature += ',top='		+ '10'
			str_feature += ',left='		+ '10'
		}	
		fm_win = wbUtilsOpenWin(url,'fm',false,str_feature)
}

function wbFmGetItemFacilityManagement(itm_id,rsv_id,itm_title){
		wb_utils_fm_set_cookie('old_rsv_return_url',wb_utils_fm_get_cookie('rsv_return_url'))
		wb_utils_fm_set_cookie('old_rsv_itm_id',wb_utils_fm_get_cookie('rsv_itm_id'))
		wb_utils_fm_set_cookie('old_rsv_itm_title',wb_utils_fm_get_cookie('rsv_itm_title'))
		wb_utils_fm_set_cookie('old_work_rsv_itm_title',wb_utils_fm_get_cookie('work_rsv_itm_title'))
		wb_utils_fm_set_cookie('old_url_success',wb_utils_fm_get_cookie('url_success'))
		wb_utils_fm_set_cookie('old_cur_rsv_id',wb_utils_fm_get_cookie('cur_rsv_id'))
		wb_utils_fm_set_cookie('old_work_rsv_id',wb_utils_fm_get_cookie('work_rsv_id'))
		wb_utils_fm_set_cookie('old_cart',wb_utils_fm_get_cookie('cart'))
		
		wb_utils_fm_set_cookie('rsv_return_url',wb_utils_invoke_ae_servlet('cmd','ae_upd_itm_rsv','itm_id',itm_id))
		wb_utils_fm_set_cookie('rsv_itm_id',itm_id)
		wb_utils_fm_set_cookie('rsv_itm_title',itm_title)
		wb_utils_fm_set_cookie('work_rsv_itm_title',itm_title)
		wb_utils_fm_set_cookie('url_success','')
		wb_utils_fm_set_cookie('work_rsv_id',rsv_id)
		wb_utils_fm_set_cookie('cur_rsv_id','')
		wb_utils_fm_set_cookie('cart','')
		wb_utils_fm_set_cookie('get_cart','')
		
		if (rsv_id == null) {rsv_id = '';}			
		var url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','get_cart','stylesheet','fm_itm_frame.xsl','rsv_id',rsv_id)
		url += "&isExcludes="+ isExcludes
		var str_feature =
			 'width=' 				+ '780'
			+ ',height=' 				+ '500'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',toolbar='				+ 'yes'
			+ ',screenX='				+ '10'
			+ ',screenY='				+ '10'
			+ ',status='				+ 'yes'
		if(document.all){
			str_feature += ',top='		+ '10'
			str_feature += ',left='		+ '10'
		}			
		fm_win = wbUtilsOpenWin(url,'fm',false,str_feature)
}

function wbFmGetCart(){
	url = _wbFmGetCartURL()
	window.location.href = url
}

function wbFmGetNewReservation(fac_type,fac_id,start_date,end_date,rsv_id){
/*
	fac_id_lst 
	start_date
	end_date are optional  param for reservation calendar
*/
	if(wb_utils_fm_get_cookie('work_rsv_id')!='' && wb_utils_fm_get_cookie('cur_rsv_id')==''){
		wb_utils_fm_set_cookie('rsv_itm_title',wb_utils_fm_get_cookie('work_rsv_itm_title'));
		window.location.href = fm.get_rsv_details_url(wb_utils_fm_get_cookie('work_rsv_id'))
	}else{
	if(wb_utils_fm_get_cookie('cur_rsv_id')=="" && wb_utils_fm_get_cookie('work_rsv_id')==""){
		wb_utils_fm_set_cookie('rsv_itm_title',wb_utils_fm_get_cookie('work_rsv_itm_title'));
	}
	
		if(fac_type == null){
			fac_type = ''
		}
		if(fac_id == null){
			fac_id = ''
		}
		if(start_date == null){
			start_date = ''
		}	
		if(end_date == null){
			end_date = ''
		}			
		if(rsv_id == null){
			rsv_id = ''
		}
		url =this.get_facility_list_url(fac_type,'fm_cart_add.xsl')
		
		url+= '&fac_id=' + fac_id + '&start_date=' + start_date + '&end_date=' + end_date + '&rsv_id=' + rsv_id
		if(wb_utils_fm_get_cookie("get_cart")=="true"){
			if(wb_utils_fm_get_cookie("cur_rsv_id")==""){
				window.location.href = _wbFmGetCartURL()
			}else{
				window.location.href = _wbFmGetRsvDtlsURL(wb_utils_fm_get_cookie("cur_rsv_id"),'fm_rsv_update.xsl')
			}
		}else{
		/*
			if(wb_utils_fm_get_cookie('rsv_itm_id')==''){
				wb_utils_fm_set_cookie('rsv_itm_title','');
			}
		*/
			window.location.href = url
		}
	}
}

function wbFmGetReservationDetailsUrl(rsv_id){
	_wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_details.xsl')
	return url;
}

function wbFmGetReservationDetails(rsv_id,title){
	if(title!=null)
	wb_utils_fm_set_cookie('rsv_itm_title',title);
	_wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_details.xsl')
	window.location.href = url;
}

//=====================================================
//Rsv Add

function wbFmReservationAddExec(frm,lang,fsh_obj){
	if(fsh_obj.length!= 0){
	//check have facility sch or not
		fsh_obj = _wbFmReservationFormSyncStatus(frm,fsh_obj)//Sync Status
		if(_wbFmReservationFormVaildateFrm(frm,lang,fsh_obj)){
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.cmd.value = 'ins_rsv'
			frm.module.value = 'fm.FMModule'
			frm.fsh_fac_id.value = _wbFmGetFshObjLst(fsh_obj,'fsh_fac_id') 
			frm.fsh_start_time.value = _wbFmGetFshObjLst(fsh_obj,'fsh_start_time') 
			frm.fsh_upd_timestamp.value = _wbFmGetFshObjLst(fsh_obj,'fsh_upd_timestamp') 
			frm.fsh_status.value = _wbFmGetFshObjLst(fsh_obj,'fsh_status') 
			frm.rsv_ent_id.value = frm.rsv_ent_id_box.options[0].value
			//frm.url_success.value = this.get_facility_list_url('','fm_cart_add.xsl')
			frm.stylesheet.value = 'fm_rsv_return_url.xsl'
			frm.url_failure.value =  this.get_facility_list_url('','fm_cart_add.xsl')
			
			if(wb_utils_fm_get_cookie('rsv_itm_id')!='')
				wb_utils_fm_set_cookie('rsv_return_url',wb_utils_invoke_ae_servlet('cmd','ae_upd_itm_rsv','itm_id',wb_utils_fm_get_cookie('rsv_itm_id')) + '&isExcludes=' + isExcludes);
			frm.submit()		
		}
	}
}
//=====================================================
//Rsv Upd

function wbFmReservationUpdatePrep(rsv_id){
	wb_utils_fm_set_cookie('cur_rsv_id',rsv_id)
	var url = _wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_update.xsl')
	window.location.href = url
}


function wbFmReservationUpdateExec(frm,lang,fsh_obj,rsv_id){
	if(fsh_obj.length!= 0){
	//check have facility sch or not
		fsh_obj = _wbFmReservationFormSyncStatus(frm,fsh_obj)//Sync Status
		if(_wbFmReservationFormVaildateFrm(frm,lang,fsh_obj)){
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.cmd.value = 'upd_rsv'
			frm.module.value = 'fm.FMModule'
			frm.fsh_fac_id.value = _wbFmGetFshObjLst(fsh_obj,'fsh_fac_id') 
			frm.fsh_start_time.value = _wbFmGetFshObjLst(fsh_obj,'fsh_start_time') 
			frm.fsh_upd_timestamp.value = _wbFmGetFshObjLst(fsh_obj,'fsh_upd_timestamp') 
			frm.fsh_status.value = _wbFmGetFshObjLst(fsh_obj,'fsh_status') 
			//frm.url_success.value = _wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_details.xsl')
			frm.stylesheet.value = 'fm_rsv_return_url.xsl'
			frm.url_failure.value = _wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_details.xsl')
			frm.rsv_ent_id.value = frm.rsv_ent_id_box.options[0].value
			if(wb_utils_fm_get_cookie('cur_rsv_id')!=wb_utils_fm_get_cookie('work_rsv_id') || wb_utils_fm_get_cookie('rsv_itm_id')=='')
				wb_utils_fm_set_cookie('rsv_return_url','');
			else
				wb_utils_fm_set_cookie('rsv_return_url',wb_utils_invoke_ae_servlet('cmd','ae_upd_itm_rsv','itm_id',wb_utils_fm_get_cookie('rsv_itm_id')) + '&isExcludes=' + isExcludes);
			//wb_utils_fm_set_cookie('rsv_itm_title','');
			frm.submit()		
		}
	}
}
//=====================================================
//Rsv Cancel
function wbFmCancelReservationPrep(rsv_id,rsv_upd_timestamp){
//../servlet/Dispatcher?module=fm.FMModule&cmd=prep_cancel_rsv&rsv_id=1&rsv_upd_timestamp={YYYY-MM-DD HH:MM:SS.NNN}
	url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','prep_cancel_rsv','rsv_id',rsv_id,'rsv_upd_timestamp',rsv_upd_timestamp,'stylesheet','fm_rsv_cancel.xsl')
	url+= '&isExcludes=' + isExcludes
	window.location.href = url
}

function wbFmCancelReservationExec(frm,lang){
//../servlet/Dispatcher?module=fm.FMModule&cmd=cancel_rsv&rsv_id=1&rsv_upd_timestamp={YYYY-MM-DD HH:MM:SS.NNN}&cancel_type_id=1&cancel_reason=The teacher is sick
	if(_wbFmCancelReservationValidate(frm,lang)){
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.cmd.value = 'cancel_rsv'
			frm.module.value = 'fm.FMModule'
			frm.url_success.value = this.get_facility_list_url('','fm_rsv_record_srh.xsl')
			frm.url_failure.value = this.get_facility_list_url('','fm_rsv_record_srh.xsl')
			frm.submit()
	}

}

//=====================================================
//Cancel Fsh
function wbFmCancelReservationFshPrep(rsv_id,fsh_fac_id,fsh_start_time,fsh_upd_timestamp){
	url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','prep_cancel_fsh','rsv_id',rsv_id,'fsh_fac_id',fsh_fac_id,'fsh_start_time',fsh_start_time,'fsh_upd_timestamp',fsh_upd_timestamp,'stylesheet','fm_fs_cancel.xsl')
	url += '&isExcludes=' + isExcludes
	window.location.href = url
}

function wbFmCancelReservationFacilityPrep(frm,lang,rsv_id,Fsh,fac_type_id,main_fac_id,main_fac_rsv_num){
//../servlet/Dispatcher?module=fm.FMModule&cmd=prep_cancel_fsh&rsv_id=1&fsh_fac_id=5~6~7&fsh_start_time={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}&fsh_upd_timestamp={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}
	
	var fsh_time_obj = _wbFmReservationGetCancelTimeStamp(frm,Fsh,fac_type_id)
	var fsh_start_time = fsh_time_obj[1]
	var fsh_fac_id = fsh_time_obj[2]
	var fsh_upd_timestamp = fsh_time_obj[0]
	if(fsh_fac_id == '' || fsh_fac_id == null){
		alert(eval('wb_msg_' + lang + '_select_facility'))
	}else if((frm.rsv_main_fac_id)&&(main_fac_id!=null && main_fac_id!="")&&(main_fac_rsv_num!=null && main_fac_rsv_num!="")){
		var tmp_fac_id = fsh_fac_id.split("~");
		var tmp_cnt = 0;
		for(var i=0;i<tmp_fac_id.length;i++){
			if(tmp_fac_id[i]==main_fac_id){
				tmp_cnt++;
			}
		}
		if(main_fac_rsv_num>0&&frm.rsv_main_fac_id.options[frm.rsv_main_fac_id.selectedIndex].value==main_fac_id&&tmp_cnt==main_fac_rsv_num){
			//if(!confirm(eval("wb_msg_"+lang+"_specify_another_main_rm")+frm.rsv_main_fac_id.options[frm.rsv_main_fac_id.selectedIndex].text))
			if(!confirm(eval("wb_msg_"+lang+"_specify_another_main_rm")))
				return;
		}
		url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','prep_cancel_fsh','rsv_id',rsv_id,'fsh_fac_id',fsh_fac_id,'fsh_start_time',fsh_start_time,'fsh_upd_timestamp',fsh_upd_timestamp,'stylesheet','fm_fs_cancel.xsl')
		url += '&isExcludes=' + isExcludes
		window.location.href = url
	}else{
	url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','prep_cancel_fsh','rsv_id',rsv_id,'fsh_fac_id',fsh_fac_id,'fsh_start_time',fsh_start_time,'fsh_upd_timestamp',fsh_upd_timestamp,'stylesheet','fm_fs_cancel.xsl')
	url += '&isExcludes=' + isExcludes
	window.location.href = url
	}
}

function wbFmCancelReservationFacilityExec(frm,lang){
//../servlet/Dispatcher?module=fm.FMModule&cmd=cancel_fsh&rsv_id=1&fsh_fac_id=5~6~7&fsh_start_time={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}&fsh_upd_timestamp={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}&cancel_type_id=1&cancel_reason=The teacher is sick

if(_wbFmCancelReservationValidate(frm,lang)){
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.cmd.value = 'cancel_fsh'
			frm.module.value = 'fm.FMModule'
			frm.url_success.value = _wbFmGetRsvDtlsURL(frm.rsv_id.value,'fm_rsv_update.xsl')
			frm.url_failure.value = _wbFmGetRsvDtlsURL(frm.rsv_id.value,'fm_rsv_update.xsl')
			//frm.url_success.value = _wbFmGetRsvDtlsURL(frm.rsv_id.value, 'fm_rsv_details.xsl')
			//frm.url_failure.value = _wbFmGetRsvDtlsURL(frm.rsv_id.value, 'fm_rsv_details.xsl')
			frm.submit()
		}
}
//cancel cart facility
function wbFmCancelCartFsExec(frm,lang,Fsh,fac_type_id){
/*../servlet/Dispatcher?module=fm.FMModule&cmd=del_fsh&fsh_fac_id=5~6~7&fsh_start_time={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}&fsh_upd_timestamp={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}*/
	var fsh_time_obj = _wbFmReservationGetCancelTimeStamp(frm,Fsh,fac_type_id)
			frm.fsh_start_time.value = fsh_time_obj[1]
			frm.fsh_upd_timestamp.value = fsh_time_obj[0]
			frm.fsh_fac_id.value =fsh_time_obj[2]
	if(frm.fsh_fac_id.value  == '' || frm.fsh_fac_id.value  == null){
		alert(eval('wb_msg_' + lang + '_select_facility'))
	}else{			
			frm.action = wb_utils_disp_servlet_url
			frm.method = 'post'
			frm.cmd.value = 'del_fsh'
			frm.module.value = 'fm.FMModule'	
			frm.url_success.value =  _wbFmGetCartURL()
			frm.url_failure.value = _wbFmGetCartURL()
			wb_utils_fm_set_cookie("cart","");
			wb_utils_fm_set_cookie("get_cart","");
			//alert(frm.url_success.value);
			frm.submit()
		}
}

function wbFmCancelCartFsSingleExec(fsh_fac_id,fsh_start_time,fsh_upd_timestamp){
/*../servlet/Dispatcher?module=fm.FMModule&cmd=del_fsh&fsh_fac_id=5~6~7&fsh_start_time={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}&fsh_upd_timestamp={YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}~{YYYY-MM-DD HH:MM:SS.NNN}*/
		var	url_success =  _wbFmGetCartURL()
		var	url_failure = _wbFmGetCartURL()
	url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','del_fsh','fsh_fac_id',fsh_fac_id,'fsh_start_time',fsh_start_time,'fsh_upd_timestamp',fsh_upd_timestamp,'url_success',url_success,'url_failure',url_failure)
	url += '&isExcludes=' + isExcludes
	wb_utils_fm_set_cookie("cart","");
	wb_utils_fm_set_cookie("get_cart","");
	//alert(wb_utils_fm_get_cookie("cart"));
	window.location.href = url
}

//=====================================================
function wbFmGetReservationCalendarPrep(fac_type){
	if(fac_type == null){
		fac_type = ''
	}
	var url = _wbFmGetReservationCalendarPrepURL(fac_type)
	window.location.href = url
}

function wbFmGetReservationCalendarExec(frm,lang){
/*../servlet/Dispatcher?module=fm.FMModule&cmd=get_calendar&start_date={YYYY-MM-DD HH:MM:SS.NNN}&end_date={YYYY-MM-DD HH:MM:SS.NNN}&fac_id=1~2~3&ext_fac_id=4~5~6*/
	frm.fac_id.value = _wbFmGetFacilitiesList(frm)
	frm.ext_fac_id.value = _wbFmGetExtFacilitiesList(frm)
	if(_wbFmGetReservationCalendarVaildateFrm(frm,lang)){
		var yy = Number(frm.start_date_yy.value)
		var mm = Number(frm.start_date_mm.value)
		var dd =Number(frm.start_date_dd.value)
		
		var	_StartDate = new Date(yy,(mm-1),dd)
		var _temp_date = _StartDate.getDate() - (_StartDate.getDay())
		_StartDate.setDate(_temp_date)
		var _EndDate = new Date(_StartDate)
		var _temp_end_date = _EndDate.getDate() + ((frm.week_count.value * 7)-1)
		_EndDate.setDate(_temp_end_date)	
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'get'
		frm.cmd.value = 'get_calendar'
		frm.module.value = 'fm.FMModule'
		frm.stylesheet.value = 'fm_calendar.xsl'
		frm.start_date.value =  _StartDate.getFullYear() + '-' + (Number(_StartDate.getMonth()+1))  + '-' + _StartDate.getDate()  + ' ' + '00:00:00.0'
		wb_utils_fm_set_cookie("FM_CAL_START_DATE",_StartDate.getFullYear()+'-'+(Number(_StartDate.getMonth()+1))+'-'+_StartDate.getDate()+' '+frm.week_count.value);
		frm.end_date.value = _EndDate.getFullYear() + '-' + (Number(_EndDate.getMonth()+1)) +'-' +_EndDate.getDate() + ' ' + '00:00:00.0'		
		frm.submit()
	}
}

function wbFmGetReservationCalendarPrev(frm,fac_id_lst,timestamp){
		//start =timestamp2002-02-16 00:00:00.0'
		//end = timestamp + 7 day
		var raw_date = timestamp.substring(0,timestamp.lastIndexOf(' '))
		var _dd = raw_date.substring(raw_date.lastIndexOf('-')+1,raw_date.length)
		var _mm = raw_date.substring(raw_date.lastIndexOf('-'),raw_date.indexOf('-')+1)
		var _yy = raw_date.substring(0,raw_date.indexOf('-'))
		
		_end_date = new Date(Number(_yy),(Number(_mm)-1),Number(_dd))
		var temp_date = _end_date.getDate() + 7
		_end_date.setDate(temp_date)

		frm.fac_id.value = fac_id_lst
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.cmd.value = 'get_calendar'
		frm.module.value = 'fm.FMModule'
		frm.stylesheet.value = 'fm_calendar.xsl'	
		frm.start_date.value = timestamp
		frm.end_date.value  = _end_date.getFullYear() + '-' + (Number(_end_date.getMonth()+1)) + '-' + (_end_date.getDate()-1) +' ' +'00:00:00.0'
		frm.submit()
}

function wbFmGetReservationCalendarNext(frm,fac_id_lst,timestamp){
		var raw_date = timestamp.substring(0,timestamp.lastIndexOf(' '))
		var _dd = raw_date.substring(raw_date.lastIndexOf('-')+1,raw_date.length)
		var _mm = raw_date.substring(raw_date.lastIndexOf('-'),raw_date.indexOf('-')+1)
		var _yy = raw_date.substring(0,raw_date.indexOf('-'))
		_start_date = new Date(Number(_yy),(Number(_mm)-1),Number(_dd))
		var temp_date = _start_date.getDate() - 7
		_start_date.setDate(temp_date)

		frm.fac_id.value = fac_id_lst
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'
		frm.cmd.value = 'get_calendar'
		frm.module.value = 'fm.FMModule'
		frm.stylesheet.value = 'fm_calendar.xsl'	
		frm.end_date.value = timestamp
		frm.start_date.value  = _start_date.getFullYear() + '-' + (Number(_start_date.getMonth()+1)) + '-' + (_start_date.getDate()+1) +' ' +'00:00:00.0'		
		frm.submit()
}

function wbFmGetReservationRecordSrh(fac_type){
	if(fac_type == null){
		fac_type = ''
	}
	url =this.get_facility_list_url(fac_type,'fm_rsv_record_srh.xsl')
	window.location.href = url
}
function wbFmGetReservationRecordCSV(start_date,end_date,fac_id,status,own_type){
//start_date={YYYY-MM-DD HH:MM:SS.NNN}&end_date={YYYY-MM-DD HH:MM:SS.NNN}&fac_id=1~2~3&status=reserved~pencilled-in~cancelled&own_type={created|reserved}&download=1&filename=rsv_record'
	if(start_date == null){
		start_date = ''
	}
	if(end_date == null){
		end_date = ''
	}	
	if(fac_id == null){
		fac_id = ''
	}	
	if(status == null){
		status = ''
	}	
	if(own_type == null){
		own_type = ''
	}	
	url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','get_rsv_lst','start_date',start_date,'end_date',end_date,'fac_id',fac_id,'status',status,'own_type',own_type,'download','1','filename','rsv_record','stylesheet','fm_rsv_record_csv.xsl')
	url += "&isExcludes="+ isExcludes
	window.parent.location.href = url;
}


function wbFmGetReservationRecordSrhExec(frm,lang,fac_id,start_date,end_date){
//../servlet/Dispatcher?module=fm.FMModule&cmd=get_rsv_lst&start_date={YYYY-MM-DD HH:MM:SS.NNN}&end_date={YYYY-MM-DD HH:MM:SS.NNN}&fac_id=1~2~3&status=reserved~pencilled-in~cancelled&own_type={created|reserved}

	if(fac_id != null){
		//Click From Calendar
		frm.method = 'get'
		frm.action = wb_utils_disp_servlet_url
		frm.cmd.value = 'get_rsv_lst'
		frm.module.value = 'fm.FMModule'
		frm.fac_id.value = fac_id
		frm.start_date.value =start_date
		frm.end_date.value =end_date
		frm.url_failure.value = _wbFmGetReservationCalendarPrepURL()
		frm.stylesheet.value = 'fm_rsv_record.xsl'
		frm.submit()
	}else{
		//Click From Reservation Record Search
		frm.status.value = ''
		if(frm.status_reserved.checked){
			frm.status.value += frm.status_reserved.value + '~'	
		}
		if(frm.status_pencilled.checked){
			frm.status.value += frm.status_pencilled.value + '~'	
		}	
		if(frm.status_cancelled.checked){
			frm.status.value += frm.status_cancelled.value + '~'	
		}				
		if(frm.status.value != ''){
			frm.status.value = frm.status.value.substring(0, frm.status.value.length-1);
		}
		frm.fac_id.value = _wbFmGetFacilitiesList(frm)
		var syy = Number(frm.start_date_yy.value)
		var smm = Number(frm.start_date_mm.value)
		var sdd =Number(frm.start_date_dd.value)		
		var shour = Number(frm.start_date_hour.value)
		
		var eyy = Number(frm.end_date_yy.value)
		var emm = Number(frm.end_date_mm.value)
		var edd =Number(frm.end_date_dd.value)			
		var ehour = Number(frm.end_date_hour.value)	
		frm.start_date.value =  syy + '-' + smm + '-' + sdd + ' ' + '00:00:00.0'
		frm.end_date.value = eyy + '-' + emm + '-' + edd + ' ' + '00:00:00.0'
		wb_utils_fm_set_cookie("FM_REC_START_DATE",syy+'-'+smm+'-'+sdd);
		wb_utils_fm_set_cookie("FM_REC_END_DATE",eyy+'-'+emm+'-'+edd);
		if(_wbFmGetReservationRecordVaildateFrm(frm,lang)){
		frm.method='get'
		frm.action = wb_utils_disp_servlet_url
		frm.cmd.value = 'get_rsv_lst'
		frm.module.value = 'fm.FMModule'
		frm.stylesheet.value = 'fm_rsv_record.xsl'	
		frm.submit()
		}

	}
	
}

function wbFmGetFacilityInformationList(fac_type){
	if(fac_type == null){
		fac_type = ''
	}
	url =this.get_facility_list_url(fac_type,'fm_facility_list.xsl')
	window.location.href = url
}

function wbFmGetFacilityDetails(id,stylesheet){
	url = wb_utils_invoke_disp_servlet("module","fm.FMModule", "cmd", "get_fac", "fac_id", id, "stylesheet",stylesheet + "_details.xsl")
	url += "&isExcludes="+ isExcludes
	window.location.href = url
}

function wbFmGetFacilityDetailsWin(id,stylesheet){
	stylesheet = stylesheet.replace('_record','')
	url = wb_utils_invoke_disp_servlet("module","fm.FMModule", "cmd", "get_fac", "fac_id", id, "stylesheet",stylesheet + "_details.xsl")
	url += "&isExcludes="+ isExcludes
	
		var str_feature = 'toolbar='		+ 'no'
			+ ',width=' 				+ '780'
			+ ',height=' 				+ '420'
			+ ',scrollbars='			+ 'yes'
			+ ',resizable='				+ 'yes'
			+ ',screenX='				+ '0'
			+ ',screenY='				+ '0'
			+ ',status='				+ 'yes'
		if(document.all){
			str_feature += ',top='		+ '0'
			str_feature += ',left='		+ '0'
		}	
		fm_fac_dtl_win = wbUtilsOpenWin(url,'fm_fac_details',false,str_feature)
	
}

function wbFmNewReservationExec(frm,act,lang){
	frm.fac_id.value = _wbFmGetFacilitiesList(frm)
	frm.start_time_min.value = _wbFmTimefloor(frm.start_time_min.value)
	frm.end_time_min.value = _wbFmTimefloor(frm.end_time_min.value)	
	var rsv_id = wb_utils_fm_get_cookie('_fm_rsv_id')
	if(rsv_id != null || rsv_id!= ''){
		frm.rsv_id.value = rsv_id
	}
	if(_wbFmNewReservationValidateFrm(frm,lang)){
	
		var rsv_id = getUrlParam('rsv_id')
		if(rsv_id=="")
			rsv_id = wb_utils_fm_get_cookie("cur_rsv_id");
		if(rsv_id != '' && rsv_id != null){
			frm.rsv_id.value = rsv_id
		}
		
		var syy = frm.start_date_yy.value
		var smm = frm.start_date_mm.value
		var sdd = frm.start_date_dd.value		
		
		var eyy = frm.end_date_yy.value
		var emm = frm.end_date_mm.value
		var edd = frm.end_date_dd.value	
		
		var _start_date = new Date(syy,(smm-1),sdd)
		var _end_date = new Date(eyy,(emm-1),edd)

		if((_end_date.valueOf() - _start_date.valueOf()) > 7776000000){
			alert(eval('wb_msg_' + lang + '_fm_date_range_too_large'))
			frm.end_date_yy.focus()
		}else{
		
		frm.start_date.value =  syy + '-' + smm + '-' + sdd + ' ' + '00:00:00.0'				
		frm.end_date.value =  eyy + '-' + emm + '-' + edd + ' ' + '00:00:00.0'	
		frm.start_time.value = frm.start_time_hour.value + ':' + frm.start_time_min.value
		frm.end_time.value = frm.end_time_hour.value + ':' + frm.end_time_min.value		
		wb_utils_fm_set_cookie("FM_NEW_START_DATETIME",syy+'-'+smm+'-'+sdd+' '+frm.start_time.value);
		wb_utils_fm_set_cookie("FM_NEW_END_DATETIME",eyy+'-'+emm+'-'+edd+' '+frm.end_time.value);
		frm.cmd.value = 'check_fac_avail'
		frm.module.value = 'fm.FMModule'
		frm.act.value = act
		if(rsv_id != ''){
			frm.url_success.value = _wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_update.xsl')
			frm.url_failure.value =_wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_update.xsl')
		}else{
			frm.url_success.value = _wbFmGetCartURL()
			frm.url_failure.value = _wbFmGetCartURL()
		}
		
		if(act =='reserve'){
			frm.stylesheet.value = 'fm_cart_clash_list.xsl'
		}else if(act == 'search'){
			frm.stylesheet.value = 'fm_cart_availability_list.xsl'
		}else if(act == 'check'){
			frm.stylesheet.value = 'fm_cart_conflicts_list.xsl'
		}
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'get'

		frm.submit()
		}
		
	}
}

function wbFmNewReservationConflictExec(frm,lang,Fsh){
/*../servlet/Dispatcher?module=fm.FMModule&cmd=check_fsh_conflict&act={reserve|search|check}&fsh_fac_id=3~4~5&fsh_date={YYYY-MM-DD HH:MM:SS.NNN}&fsh_start_time={YYYY-MM-DD HH:MM:SS.NNN}&fsh_end_time={YYYY-MM-DD HH:MM:SS.NNN}*/
	var rsv_id = wb_utils_fm_get_cookie('_fm_rsv_id')
	if(rsv_id != null || rsv_id!= ''){
		frm.rsv_id.value = rsv_id
	}
		var fsh_data = _wbFmReservationGetFshObj(frm,Fsh)
		frm.fsh_date.value = fsh_data[0]
		frm.fsh_start_time.value = fsh_data[1]
		frm.fsh_end_time.value = fsh_data[2]
		frm.fsh_fac_id.value = fsh_data[3]
	if(frm.fsh_fac_id.value == '' || frm.fsh_fac_id.value == null){
		alert(eval('wb_msg_' + lang + '_select_facility'))
	}else{		
		frm.cmd.value = 'check_fsh_conflict'
		frm.module.value = 'fm.FMModule'
		frm.act.value = 'reserve'
		if(rsv_id != ''){			
			frm.url_success.value = _wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_update.xsl')
			frm.url_failure.value = _wbFmGetRsvDtlsURL(rsv_id,'fm_rsv_update.xsl')
		}else{
			frm.url_success.value = _wbFmGetCartURL()
			frm.url_failure.value = _wbFmGetCartURL()
		}		
		
		frm.stylesheet.value = 'fm_cart_clash_list.xsl'
		frm.action = wb_utils_disp_servlet_url
		frm.method = 'post'	
		frm.submit()
	}
}

//=====================================================

function wbFmInsFacilityDetailsPrep(fac_type,stylesheet){
	url = wb_utils_invoke_servlet('cmd','get_prof','fac_type',fac_type,'stylesheet',stylesheet + "_ins.xsl")
	url+= '&isExcludes=' + isExcludes
	window.location.href = url
}

function wbFmInsFacilityDetailsExec(frm,lang){

	// function _ValidateAdditionalInfoFrm should define in HTML ;
	// if no additioanl information to vailidate, create a function retrun true
	
	if ( _wbFmFacDtlValidateFrm(frm,lang) && _ValidateAdditionalInfoFrm(frm,lang) && wbUtilsValidateImgType(frm.fac_url,lang)) {
			var fac_type = getUrlParam('fac_type')
		 	frm.url_success.value = this.get_facility_list_url('','fm_facility_list.xsl')
		 	frm.url_failure.value = this.get_facility_list_url('','fm_facility_list.xsl')
			frm.cmd.value = "ins_fac"
			frm.module.value = "fm.FMModule"
			frm.fac_type.value = fac_type
			frm.action = wb_utils_disp_servlet_url
			frm.method = "post"
			frm.submit()
		}
}

function wbFmGetFacilityDetailsURL(id,stylesheet){
	url = wb_utils_invoke_disp_servlet("module","fm.FMModule", "cmd", "get_fac", "fac_id", id, "stylesheet",stylesheet + "_details.xsl")
	url += "&isExcludes="+ isExcludes
	return url
}

function wbFmEditFacilityDetailsPrep(id,stylesheet){
	url = wb_utils_invoke_disp_servlet("module","fm.FMModule", "cmd", "get_fac", "fac_id", id, "stylesheet",stylesheet + "_update.xsl")
	url += "&isExcludes="+ isExcludes
	window.location.href = url
}

function wbFmEditFacilityDetailsExec(frm,lang){
	// function _ValidateAdditionalInfoFrm should define in HTML ;
	// if no additioanl information to vailidate, create a function retrun true
	
	if ( _wbFmFacDtlValidateFrm(frm,lang) && _ValidateAdditionalInfoFrm(frm,lang) && wbUtilsValidateImgType(frm.fac_url,lang)) {
			
			frm.url_success.value = this.get_facility_details_url(frm.fac_id.value,frm.stylesheet.value)
			frm.url_failure.value = this.get_facility_list_url('','fm_facility_list.xsl')
			frm.cmd.value = "upd_fac"
			frm.module.value = "fm.FMModule"
			frm.action = wb_utils_disp_servlet_url
			frm.method = "post"
			frm.submit()
		}
}

function wbFmDelFacilityDetails(frm,fac_id,timestamp,lang){
	if(confirm(eval("wb_msg_"+lang+"_confirm"))){
		frm.action = wb_utils_disp_servlet_url
		frm.method = "post"
	
		frm.url_success.value = this.get_facility_list_url('','fm_facility_list.xsl')
		frm.module.value = 'fm.FMModule'
		frm.cmd.value = 'del_fac'
		frm.fac_id.value = fac_id		
		frm.fac_upd_timestamp.value = timestamp		
		frm.submit()
	}
}

//=====================================================

function wbFmGetFacilityListURL(fac_type,stylesheet){
	if (fac_type == null )
		url = wb_utils_invoke_disp_servlet("module", "fm.FMModule", "cmd", "get_fac_lst", "fac_type", '','stylesheet',stylesheet)
	else
		url = wb_utils_invoke_disp_servlet("module", "fm.FMModule", "cmd", "get_fac_lst", "fac_type", fac_type,'stylesheet',stylesheet);	
	url += "&isExcludes="+ isExcludes
	return url;
}

function wbFmFeedRsvFacId(frm,fac_id){
	var i, n, ele
	var check_name = 'fm_fac_id_lst_'
	n = frm.elements.length
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
			if (ele.type == "checkbox"  && (ele.name.search(check_name) != -1) && (ele.value == fac_id)){
				ele.checked = true
				break;
			}				
	}				
}
//=====================================================
function wbFmFeedRsvStartDate(frm,start_date){
	var yy,mm,dd
	yy = start_date.substring(0,4)
	mm = start_date.substring(5,7)
	dd = start_date.substring(8,10)
	if(typeof frm.start_date_yy != 'undefined'){
		frm.start_date_yy.value = yy
	}
	if(typeof frm.start_date_yy != 'undefined'){
		frm.start_date_mm.value = mm
	}
	if(typeof frm.start_date_yy != 'undefined'){
		frm.start_date_dd.value = dd
	}
}

function wbFmFeedRsvEndDate(frm,end_date){
	var yy,mm,dd
	yy = end_date.substring(0,4)
	mm = end_date.substring(5,7)
	dd = end_date.substring(8,10)
	if(typeof frm.end_date_yy != 'undefined'){
		frm.end_date_yy.value = yy
	}
	if(typeof frm.end_date_yy != 'undefined'){
		frm.end_date_mm.value = mm
	}
	if(typeof frm.start_date_yy != 'undefined'){
		frm.end_date_dd.value = dd
	}
}

function wbFmPickRunPrep(frm){
	url = wb_utils_invoke_ae_servlet('cmd','ae_lookup_itm','all_ind','true','exact','false','type','CLASSROOM','stylesheet','fm_cart_pick_run.xsl','tvw_id','LIST_VIEW','show_respon','true','show_run_ind','true','itm_life_status_not_equal_lst','CANCELLED','title',frm.rsv_itm_title.value,'orderby','p_itm_title','sortorder','asc');
	url += "&isExcludes="+ isExcludes
	var str_feature = 'width=' 				+ '600'
		+ ',height=' 				+ '340'
		+ ',scrollbars='				+ 'yes'
		+ ',resizable='				+ 'yes'
		+ ',toolbar='				+ 'no'
		+ ',screenX='				+ '10'
		+ ',screenY='				+ '10'
		+ ',status='				+ 'yes'
	if(document.all){
		str_feature += ',top='		+ '10'
		str_feature += ',left='		+ '10'
	}	
	fm_pick_run_win = wbUtilsOpenWin(url,'fm_pick_run',false,str_feature)
}

function wbFmPickRunExec(frm){
	if(opener==null)
		return;
	if(frm.cos_run_id.length){
		for(i=0;i<frm.cos_run_id.length;i++){
			if(frm.cos_run_id[i].checked){
				opener.document.frmAction.cos_run_id.value = frm.cos_run_id[i].value;
				opener.document.frmAction.rsv_itm_id.value = frm.rsv_itm_id[i].value;
				opener.document.frmAction.rsv_itm_title.value = frm.cos_run_title[i].value;
				opener.loadPage();
			}
		}
	}else{
		if(frm.cos_run_id.checked){
			opener.document.frmAction.cos_run_id.value = frm.cos_run_id.value;
			opener.document.frmAction.rsv_itm_id.value = frm.rsv_itm_id.value;
			opener.document.frmAction.rsv_itm_title.value = frm.cos_run_title.value;
			opener.loadPage();
		}
	}
	window.close();
}
/* private functions*/
function _wbFmFacDtlValidateFrm(frm,lang){
	if (frm.fac_title) {
		frm.fac_title.value = wbUtilsTrimString(frm.fac_title.value);
		if (!gen_validate_empty_field(frm.fac_title,eval('wb_msg_'+lang+'_fm_title'),lang)){
			frm.fac_title.focus()
			return;
		}
	}
	
	if (frm.fac_desc) {
		frm.fac_desc.value = wbUtilsTrimString(frm.fac_desc.value);
		if (frm.fac_desc.value.length > 0 && frm.fac_desc.value.length > 1000){
			alert(eval('wb_msg_'+lang+'_fm_des_too_long'))
			frm.fac_desc.focus()
			return false	
		}
	}
	
	if (frm.fac_fee) {
		frm.fac_fee.value = wbUtilsTrimString(frm.fac_fee.value);
		if (frm.fac_fee.value.length > 0){
			if (!wbUtilsValidateFloat(frm.fac_fee, frm.lab_fac_fee.value)) {
				return false	
			}
		}
	}
	
	if (frm.fac_remarks) {
		frm.fac_remarks.value = wbUtilsTrimString(frm.fac_remarks.value);
		if (frm.fac_remarks.value.length > 0 && frm.fac_remarks.value.length > 1000){
			alert(eval('wb_msg_'+lang+'_fm_remarks_too_long'))
			frm.fac_remarks.focus()
			return false	
		}
	}

	if (frm.addfac_size) {
		frm.addfac_size.value = wbUtilsTrimString(frm.addfac_size.value);
	}
	if (frm.addfac_capacity) {
		frm.addfac_capacity.value = wbUtilsTrimString(frm.addfac_capacity.value);
	}
	if (frm.addfac_white_board) {
		frm.addfac_white_board.value = wbUtilsTrimString(frm.addfac_white_board.value);
	}
	if (frm.addfac_network_port) {
		frm.addfac_network_port.value = wbUtilsTrimString(frm.addfac_network_port.value);
	}
	if (frm.addfac_power_port) {
		frm.addfac_power_port.value = wbUtilsTrimString(frm.addfac_power_port.value);
	}
	if (frm.addfac_projector_screen) {
		frm.addfac_projector_screen.value = wbUtilsTrimString(frm.addfac_projector_screen.value);
	}
	if (frm.addfac_LCD_projector) {
		frm.addfac_LCD_projector.value = wbUtilsTrimString(frm.addfac_LCD_projector.value);
	}
	if (frm.addfac_other_facilities) {
		frm.addfac_other_facilities.value = wbUtilsTrimString(frm.addfac_other_facilities.value);
	}
	return true;
}


function _wbFmAdditioalInfoXML(frm,field_prefix){
		// example of field_prefix : field_prefix = "addfac_"
		var i, n, ele, str
		str=''
		//str = "<additional>"
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]			
			if (ele.name.indexOf(field_prefix) != -1 ) {				
					str += '<' + ele.name + '>' +  wb_utils_XmlEscape(wbUtilsTrimString(ele.value)) + '</' + ele.name +'>'
			}
		}
		//str += "</additional>"
		return str;	

}
function _wbFmGetReservationRecordVaildateFrm(frm,lang){
	if(!wbUtilsValidateDate('document.' + frm.name + '.start_date',eval('wb_msg_' + lang + '_start_date'))){
		return false;
	}

	if(!wbUtilsValidateDate('document.' + frm.name + '.end_date',eval('wb_msg_' + lang + '_end_date'))){
		return false;
	}	
	
	if(!wb_utils_validate_date_compare({
		frm : 'document.' + frm.name, 
		start_obj : 'start_date', 
		end_obj : 'end_date'
		})) {
			return false;
	}

	if(frm.status.value ==''){
		alert(eval('wb_msg_' + lang + '_select_status'))
		return false;
	}
	
	if(frm.fac_id.value == ''){
		alert(eval('wb_msg_' + lang + '_select_facility'))
		return false;
	}		
		return true;
}

function _wbFmGetReservationCalendarVaildateFrm(frm,lang){
	if(!wbUtilsValidateDate('document.' + frm.name + '.start_date',eval('wb_msg_' + lang + '_start_date'))){
		return false;
	}
	if(frm.fac_id.value == '' && frm.ext_fac_id.value == ''){
		alert(eval('wb_msg_' + lang + '_select_facility'))
		return false;
	}		
		return true;
}

function _wbFmNewReservationValidateFrm(frm,lang){
	if(typeof frm.start_date !=  'undefined' || typeof frm.end_date !=  'undefined' ){
		if(!wbUtilsValidateDate('document.' + frm.name + '.start_date',eval('wb_msg_' + lang + '_start_date'))){
			return false;
		}
		if(!wbUtilsValidateDate('document.' + frm.name + '.end_date',eval('wb_msg_' + lang + '_end_date'))){
			return false;
		}	

		var start_date = Number(frm.start_date_yy.value.toString() + frm.start_date_mm.value.toString() + frm.start_date_dd.value.toString())
		var end_date = Number(frm.end_date_yy.value.toString() + frm.end_date_mm.value.toString() +frm.end_date_dd.value.toString())
		
		if( start_date > end_date){
			alert('"' + eval('wb_msg_' + lang + '_end_date') + '" ' + eval('wb_msg_' + lang + '_can_not_early_than')+ ' "' + eval('wb_msg_' + lang + '_start_date')  + '"')
			frm.end_date_yy.focus()
			return false;
		
		}
		if(!wbUtilsValidateTime('document.' + frm.name + '.start_time',eval('wb_msg_' + lang + '_start_time'))){
			return false;
		}
		if(!wbUtilsValidateTime('document.' + frm.name + '.end_time',eval('wb_msg_' + lang + '_end_time'))){
			return false;
		}	
		
		var start_time = Number(frm.start_time_hour.value.toString() + frm.start_time_min.value.toString())
		var end_time = Number(frm.end_time_hour.value.toString() + frm.end_time_min.value.toString())
		//alert((start_time > end_time) && (start_date == end_date))
		if(start_time > end_time){
			alert('"' + eval('wb_msg_' + lang + '_end_time') + '" ' + eval('wb_msg_' + lang + '_can_not_early_than')+ ' "' + eval('wb_msg_' + lang + '_start_time')  + '"')
			frm.end_time_hour.focus()
			return false;
		}else if(start_time == end_time){
			alert(eval('wb_msg_' + lang + '_fm_time_intervals_same'))
			frm.end_time_hour.focus()
			return false;
		}
	}
	if(frm.fac_id.value == ''){
		alert(eval('wb_msg_' + lang + '_select_facility'))
		return false;
	}
	return true;
}

function _wbFmGetFacilitiesList(frm){
	var i, n, ele
	var str = ''
	var check_name = 'fm_fac_id_lst_'
	n = frm.elements.length
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
			if (ele.type == "checkbox"  && (ele.name.search(check_name) != -1)){
				if(ele.checked){
					str+= ele.value + '~'
				}
			}				
	}
	
	if(str!=''){
		str = str.substring(0, str.length-1);
	}
	return str
}

function _wbFmGetExtFacilitiesList(frm){
	var i, n, ele
	var str = ''
	var check_name = 'fm_ext_fac_id_lst_'
	n = frm.elements.length
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
			if (ele.type == "checkbox"  && (ele.name.search(check_name) != -1)){
				if(ele.checked){
					str+= ele.value + '~'
				}
			}				
	}
	
	if(str!=''){
		str = str.substring(0, str.length-1);
	}
	return str
}

function _wbFmReservationGetCancelTimeStamp(frm,fsh_obj,fac_type_id){
	var i, n, ele
	var str = ''
	var check_name = 'fm_fac_id_lst_'
	n = frm.elements.length
	m = fsh_obj.length
	k = null
	str=new Array()
	str[0] = ''
	str[1] = ''
	str[2] = ''
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
			if (ele.type == "checkbox"  && (ele.name.search(check_name) != -1)){
			for(j=0;j<m;j++){
				if(fac_type_id == null){
					if(fsh_obj[j].fsh_checkbox_id == ele.value){
						k =j
						break;
					}
				}else{
					if((fsh_obj[j].fsh_checkbox_id == ele.value) && (fsh_obj[j].fsh_fac_type_id == fac_type_id)){
						k=j
						break;
					}
				}
			}
			if(k != null){
				if(ele.checked){
					str[0]+= fsh_obj[k].fsh_upd_timestamp + '~'
					str[1]+= fsh_obj[k].fsh_start_time + '~'
					str[2]+= fsh_obj[k].fsh_fac_id + '~'
				}
			}
			k= null			
		}		
	}
	if(str!=''){
		str[0] = str[0].substring(0, str[0].length-1);
		str[1] = str[1].substring(0, str[1].length-1);
		str[2] = str[2].substring(0, str[2].length-1);
	}
	return str
}

function _wbFmReservationGetFshObj(frm,fsh_obj){
	var i, n, ele
	var str = ''
	var check_name = 'fm_fac_id_lst_'
	n = frm.elements.length
	m = fsh_obj.length
	k = null
	str=new Array()
	str[0] = ''
	str[1] = ''
	str[2] = ''
	str[3] =''
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
			if (ele.type == "checkbox"  && (ele.name.search(check_name) != -1)){
			for(j=0;j<m;j++){
				if(fsh_obj[j].fsh_checkbox_id == ele.value){
					k =j
					break;
				}
			}
			if(k != null){
				if(ele.checked){
					str[0]+= fsh_obj[k].fsh_date + '~'
					str[1]+= fsh_obj[k].fsh_start_time + '~'
					str[2]+= fsh_obj[k].fsh_end_time + '~'
					str[3]+= fsh_obj[k].fsh_fac_id + '~'
				}
			}
			k= null			
		}		
	}
	if(str!=''){
		str[0] = str[0].substring(0, str[0].length-1);
		str[1] = str[1].substring(0, str[1].length-1);
		str[2] = str[2].substring(0, str[2].length-1);
		str[3] = str[3].substring(0, str[3].length-1);
	}
	return str
}

function _wbFmGetReservationCalendarPrepURL(fac_type){
	if(fac_type == null){
		fac_type = ''
	}
	url = wbFmGetFacilityListURL(fac_type,'fm_calendar_prep.xsl')
	return url
}

function _wbFmReservationFormVaildateFrm(frm,lang,fsh_obj){
	if(frm.rsv_main_fac_id && frm.rsv_main_fac_id.options && frm.rsv_main_fac_id.options[frm.rsv_main_fac_id.selectedIndex].value==""){
		alert(eval("wb_msg_"+lang+"_specify_main_rm"));
		frm.rsv_main_fac_id.focus();
		return false;
	}
	
	frm.rsv_purpose.value = wbUtilsTrimString(frm.rsv_purpose.value);
	frm.rsv_desc.value = wbUtilsTrimString(frm.rsv_desc.value);
	frm.rsv_participant_no.value = wbUtilsTrimString(frm.rsv_participant_no.value);
	
	if(frm.rsv_purpose.value.length > 255){
		alert(eval('wb_msg_' + lang + '_fm_purpose_too_long')+' 255 ' +eval('wb_msg_' + lang + '_characters'))
		frm.rsv_purpose.focus()
		return false
	}
	if(frm.rsv_desc.value.length > 1000){
		alert(eval('wb_msg_' + lang + '_fm_desc_too_long')+' 1000 ' +eval('wb_msg_' + lang + '_characters'))
		frm.rsv_desc.focus()
		return false
	}	
	
	if(!gen_validate_empty_field(frm.rsv_purpose, eval('wb_msg_' + lang + '_fm_purpose'),lang)){
		return false
	}
	
	if(!gen_validate_empty_field(frm.rsv_desc, eval('wb_msg_' + lang + '_fm_desc'),lang)){
		return false
	}	
	
	if(!gen_validate_empty_field(frm.rsv_participant_no,eval('wb_msg_' + lang + '_fm_no_of_participant'),lang)){
		return false
	}		
	if(!gen_validate_positive_integer(frm.rsv_participant_no,eval('wb_msg_' + lang + '_fm_no_of_participant'),lang)){
		return false
	}
	if(frm.rsv_participant_no && frm.rsv_participant_no.value<1){
		alert(eval('wb_msg_'+lang+'_enter_value') + ' ' + eval('wb_msg_' + lang + '_fm_no_of_participant'));
		return false;
	}
	return true
}

function _wbFmReservationFormSyncStatus(frm,fsh_obj){
	var i, n, j,ele
	var str = ''
	var check_name = 'fsh_status_'
	n = frm.elements.length
	m = fsh_obj.length
	k = null
	for (i = 0; i < n; i++){
		ele = frm.elements[i]
			if (ele.type == "checkbox"  && (ele.name.search(check_name) != -1)){
			for(j=0;j<m;j++){
				if(fsh_obj[j].fsh_checkbox_id == ele.value){
					k =j
					break;
				}
			}
			if(k != null){
				if(ele.checked){
					fsh_obj[k].fsh_status = 'PENCILLED_IN'	
				}else{
					fsh_obj[k].fsh_status = 'RESERVED'
				}
			}
			k= null		
		}				
	}
	return fsh_obj
}

function _wbFmGetFshObjLst(fsh_obj,name){
	var i=0
	var ln = fsh_obj.length
	var result = ''
	for(i=0;i<ln;i++){
		result += eval('fsh_obj['+ i +'].'+ name)
		if(i!=(ln-1)){
			result+='~'
		}	
	}
	return result
}

function _wbFmCancelReservationValidate(frm,lang){
	if(frm.cancel_type[frm.cancel_type.selectedIndex].value == ''){
		alert(eval('wb_msg_' + lang + '_fm_select_cancellation_type'))
		return false;
	}
	if(frm.cancel_type[frm.cancel_type.selectedIndex].value == 'Others' && !gen_validate_empty_field(frm.cancel_reason, eval('wb_msg_' + lang + '_fm_reason'),lang)){
		return false;
	}
	return true
}

function _wbFmGetRsvDtlsURL(rsv_id,stylesheet){
//../servlet/Dispatcher?module=fm.FMModule&cmd=get_rsv&rsv_id=1
	url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','get_rsv','rsv_id',rsv_id,'stylesheet',stylesheet)
	url += "&isExcludes="+ isExcludes
	return url
}

function _wbFmTimefloor(time){
	int_time = Number(time)
	if( int_time < 15){
		return '00'
	}else if(int_time > 14 && int_time < 30){
		return '15'
	}else if(int_time > 29 && int_time < 45){
		return '30'
	}else if(int_time > 44 && int_time <60){
		return '45'
	}else{
		return '00'
	}
}

function _wbFmGetCartURL(){
//../servlet/Dispatcher?module=fm.FMModule&cmd=get_cart&
var url = wb_utils_invoke_disp_servlet('module','fm.FMModule','cmd','get_cart','stylesheet','fm_rsv_add.xsl')
url += "&isExcludes="+ isExcludes
return url
}

	function _wbFmCheckTimeSlot(){
						var today = new Date()
						var hours = today.getHours()
						var mins = today.getMinutes()
						if( hours > 7 && hours < 13){
							if( hours == 8){
								if(mins >29){
									return 0;
								}else{
									return 1;
								}
							}
							if(hours == 12){
								if(mins > 30){
									return 2;
								}else{
									return 1;
								}
							}
							return 1;
						}else if(hours > 12 && hours < 18){
							if(hours == 13){
								
								if(mins >29){
									return 2;
								}else{
									return 0;
								}
							}
							if(hours == 17){
								if(mins >30){
									return 3;
								}else{
									return 2;
								}
							}
						return 2;
						}else if(hours > 17 && hours <23){
							if(hours == 22){
								if(mins == 0){
									return 3
								}else{
									return 0
								}
							return 3	
							}
							
						return 3
						}else{
							return 0;
						}
			}
			
function wbFmSearchFacFeePrep() {
	var url = wb_utils_invoke_disp_servlet('module', 'fm.FMModule', 'cmd', 'search_fac_fee_prep', 'stylesheet', 'fm_fee_search_prep.xsl');
	url+= '&isExcludes=' + isExcludes
	window.location.href = url;
}	

