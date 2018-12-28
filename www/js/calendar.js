
function wbCalendar() {	
	this.selectLoad = wbCalendarSelectLoad
	this.selectCurrent = wbCalendarSelectCurrent
	this.selectNext = wbCalendarSelectNext
	this.selectPrev = wbCalendarSelectPrev
	this.selectNextYear = wbCalendarSelectNextYear
	this.selectPrevYear = wbCalendarSelectPrevYear
	this.calendar = wbCalendarCalendar
	this.calLrnDayDetails = wbCalendarLrnDayDetails
	this.calIstDayDetails = wbCalendarIstDayDetails
	this.calLrnURL = wbCalendarLRNURL
	this.calIstURL = wbCalendarISTURL

}

// private funtion 

function _wbCalendarMakeArray() {
	this[0] = _wbCalendarMakeArray.arguments.length;
	for (i = 0; i < _wbCalendarMakeArray.arguments.length; i = i + 1)
		this[i+1] = _wbCalendarMakeArray.arguments[i];
}



var wbCalendarchMonth = new _wbCalendarMakeArray(
	wb_msg_calendar_jan, wb_msg_calendar_feb, wb_msg_calendar_mar, wb_msg_calendar_apr, 
	wb_msg_calendar_may, wb_msg_calendar_jun, wb_msg_calendar_jul, wb_msg_calendar_aug, 
	wb_msg_calendar_sep, wb_msg_calendar_oct, wb_msg_calendar_nov, wb_msg_calendar_dec);
	
var wbCalendarenMonth = new _wbCalendarMakeArray(
	wb_msg_calendar_jan, wb_msg_calendar_feb, wb_msg_calendar_mar, wb_msg_calendar_apr, 
	wb_msg_calendar_may, wb_msg_calendar_jun, wb_msg_calendar_jul, wb_msg_calendar_aug, 
	wb_msg_calendar_sep, wb_msg_calendar_oct, wb_msg_calendar_nov, wb_msg_calendar_dec);
	
var wbCalendargbMonth = new _wbCalendarMakeArray(
	wb_msg_calendar_jan, wb_msg_calendar_feb, wb_msg_calendar_mar, wb_msg_calendar_apr, 
	wb_msg_calendar_may, wb_msg_calendar_jun, wb_msg_calendar_jul, wb_msg_calendar_aug, 
	wb_msg_calendar_sep, wb_msg_calendar_oct, wb_msg_calendar_nov, wb_msg_calendar_dec);

var wbCalendarchweekDay = new _wbCalendarMakeArray(
	wb_msg_calendar_s_sun, wb_msg_calendar_s_mon, wb_msg_calendar_s_tue, wb_msg_calendar_s_wed, 
	wb_msg_calendar_s_thu, wb_msg_calendar_s_fri, wb_msg_calendar_s_sat);

var wbCalendarenweekDay = new _wbCalendarMakeArray(
	wb_msg_calendar_s_sun, wb_msg_calendar_s_mon, wb_msg_calendar_s_tue, wb_msg_calendar_s_wed, 
	wb_msg_calendar_s_thu, wb_msg_calendar_s_fri, wb_msg_calendar_s_sat);

var wbCalendargbweekDay = new _wbCalendarMakeArray(
	wb_msg_calendar_s_sun, wb_msg_calendar_s_mon, wb_msg_calendar_s_tue, wb_msg_calendar_s_wed, 
	wb_msg_calendar_s_thu, wb_msg_calendar_s_fri, wb_msg_calendar_s_sat);

function _wbCalendarLeap(y) {
	return ((y % 400 == 0) || (y % 100 != 0 && y % 4 == 0));
}

function civMonthLength(month, year) {
	if(month == 2)
		return 28 + _wbCalendarLeap(year);
	else if(month == 4 || month == 6 || month == 9 || month == 11)
	   return 30;
	else
		return 31;
}


function wbCalendarSelectLoad() {
	var now = new Date();
	m = now.getMonth();
	y = now.getYear();
	if(y < 1000)
		y += 1900;

	var url = parent.location.href
	url = setUrlParam('cal_m',m, url);
	url = setUrlParam('cal_y',y, url);
	window.location.href = url
}

function wbCalendarSelectCurrent() {
	var now = new Date();
	y = now.getYear();
	m = now.getMonth();
	if(y < 1000)
		y += 1900;


	var url = parent.location.href
	url = setUrlParam('cal_m',m, url);
	url = setUrlParam('cal_y',y, url);
	window.location.href = url
}



function wbCalendarSelectNext(m,y) {

	if( m < 12){
		m++;		
	}else {
		m = 1;
		y++;
	}

	if ( m < 10 )
		m = '0' + m

	start_datetime = y + '-' + m + '-01 00:00:00.0'
	end_datetime =  y + '-' + m + '-' +  civMonthLength(Number(m),y) + ' 23:59:59.0'

	var url = self.location.href
	url = setUrlParam('start_datetime',escape(start_datetime), url);
	url = setUrlParam('end_datetime',escape(end_datetime), url);
	window.location.href = url
}

function wbCalendarSelectPrev(m,y) {

	
	if( m > 1){
		m --;		
	}else {
		m = 12;
		y--;
	}
	
	if ( m < 10 )
		m = '0' + m

	start_datetime = y + '-' + m + '-01 00:00:00.0'
	end_datetime =  y + '-' + m + '-' +  civMonthLength(Number(m),y) + ' 23:59:59.0'
	
	var url = self.location.href
	url = setUrlParam('start_datetime',escape(start_datetime), url);
	url = setUrlParam('end_datetime',escape(end_datetime), url);
	window.location.href = url
}

function wbCalendarSelectNextYear(num) {

	y = parseInt(getUrlParam('cal_y'))
	m = parseInt(getUrlParam('cal_m'))
	y += num;
	var url = parent.location.href
	url = setUrlParam('cal_m',m, url);	
	url = setUrlParam('cal_y',y, url);
	window.location.href = url
}

function wbCalendarSelectPrevYear(num) {

	y = parseInt(getUrlParam('cal_y'))
	m = parseInt(getUrlParam('cal_m'))
	y -= num;

	var url = parent.location.href
	url = setUrlParam('cal_m',m, url);
	url = setUrlParam('cal_y',y, url);
	window.location.href = url
}

/*function selectForm(form) {

	
	y = parseInt(getUrlParam('cal_y'))
	m = parseInt(getUrlParam('cal_m'))
	
	var url = parent.location.href
	url = setUrlParam('cal_m',m, url);
	url = setUrlParam('cal_y',y, url);
	window.location.href = url
}


function doCal(month, year) {

	var ret = calendar(month, year);	
	draw_cal(ret)	

}*/

function wbCalendarCalendar(selM, selY) {
	var m = selM + 1;
	var y = selY;
	var d = civMonthLength(m, y);
	var firstOfMonth = new Date (y, selM, 1);
	var startPos = firstOfMonth.getDay() + 1;
	var retVal = new Object();
	retVal[1] = startPos;
	retVal[2] = d;
	retVal[3] = m;
	retVal[4] = y;
	return (retVal);
}

function wbCalendarLrnDayDetails(start_datetime,end_datetime,course_id){
	
	if ( course_id != null )
			url = wb_utils_invoke_servlet('cmd', 'get_cos_cnt_lst','start_datetime',start_datetime,'end_datetime',end_datetime,'course_id',course_id,'dpo_view','LRN_READ','stylesheet', 'lrn_calendar_details.xsl')
	else	
		url = wb_utils_invoke_servlet('cmd', 'get_cos_cnt_lst','start_datetime',start_datetime,'end_datetime',end_datetime,'dpo_view','LRN_READ','stylesheet', 'lrn_calendar_details.xsl')
	parent.window.location.href = url
}

function wbCalendarIstDayDetails(start_datetime,end_datetime){
		
	url = wb_utils_invoke_servlet('cmd', 'get_cos_cnt_lst','start_datetime',start_datetime,'end_datetime',end_datetime,'dpo_view','IST_READ','stylesheet', 'ist_calendar_details.xsl')
	parent.window.location.href = url
}


function wbCalendarLRNURL(start_datetime,end_datetime,cos_id){
if (cos_id == null)
	url = wb_utils_invoke_servlet('cmd','get_cos_cnt_lst', 'start_datetime',start_datetime,'end_datetime',end_datetime,'stylesheet', 'lrn_calendar.xsl' )
else
	url = wb_utils_invoke_servlet('cmd','get_cos_cnt_lst', 'course_id' ,cos_id ,'start_datetime',start_datetime,'end_datetime',end_datetime,'stylesheet', 'lrn_calendar.xsl' );
return url
}
		
function wbCalendarISTURL(start_datetime,end_datetime){

	url = wb_utils_invoke_servlet('cmd','get_cos_cnt_lst', 'start_datetime',start_datetime,'end_datetime',end_datetime,'stylesheet', 'ist_calendar.xsl' )
	return url;
}			
