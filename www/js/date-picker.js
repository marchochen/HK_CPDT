var weekend = [0,6];
var weekendColor = "#8DCBF0";
var fontface = "Verdana";
var fontsize = 2;

var gNow = new Date();
var ggWinCal;
var gCharacter;
var gCssPath;
isNav = (navigator.appName.indexOf("Netscape") != -1) ? true : false;
isIE = (navigator.appName.indexOf("Microsoft") != -1) ? true : false;

var DateTitle = "";

Calendar.Months = [wb_msg_calendar_jan, wb_msg_calendar_feb, wb_msg_calendar_mar, wb_msg_calendar_apr, 
	wb_msg_calendar_may, wb_msg_calendar_jun, wb_msg_calendar_jul, wb_msg_calendar_aug, 
	wb_msg_calendar_sep, wb_msg_calendar_oct, wb_msg_calendar_nov, wb_msg_calendar_dec];
Calendar.Weeks = [wb_msg_calendar_sun, wb_msg_calendar_mon, wb_msg_calendar_tue, wb_msg_calendar_wed, 
	wb_msg_calendar_thu, wb_msg_calendar_fri, wb_msg_calendar_sat];


// Non-Leap year Month days..
Calendar.DOMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
// Leap year Month days..
Calendar.lDOMonth = [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

function Calendar(p_item, p_WinCal, p_month, p_year, p_format, wb_Character, p_CssPath) {
	this.gYear = p_year;
	this.gFormat = p_format;
	this.gBGColor = "white";
	this.gFGColor = "black";
	this.gTextColor = "black";
	this.gHeaderColor = "black";
	this.gReturnItem = p_item;
	
	gCharacter = wb_Character;
	gCssPath = p_CssPath;
	
	
	
	if ((p_month == null) && (p_year == null))	return;

	if (p_WinCal == null)
		this.gWinCal = ggWinCal;
	else
		this.gWinCal = p_WinCal;
	
	if (p_month == null) {
		this.gMonthName = null;
		this.gMonth = null;
		this.gYearly = true;
	} else {
		this.gMonthName = Calendar.get_month(p_month);
		this.gMonth = new Number(p_month);
		this.gYearly = false;
	}
    
	if (wb_Character == 'gb') {
		DateTitle = this.gYear + wb_msg_calendar_year + (this.gMonth + 1) + wb_msg_calendar_month;
	} else if (wb_Character == 'ch') {
		DateTitle = this.gYear + wb_msg_calendar_year + (this.gMonth + 1) + wb_msg_calendar_month;
	} else {
		DateTitle = this.gMonthName + ", " + this.gYear;
	}

}

Calendar.get_month = Calendar_get_month;
Calendar.get_week = Calendar_get_week;
Calendar.get_daysofmonth = Calendar_get_daysofmonth;
Calendar.calc_month_year = Calendar_calc_month_year;
Calendar.print = Calendar_print;

function Calendar_get_month(monthNo) {
	return Calendar.Months[monthNo];
}

function Calendar_get_week(weekNo) {
	return Calendar.Weeks[weekNo];
}

function Calendar_get_daysofmonth(monthNo, p_year) {
	/* 
	Check for leap year ..
	1.Years evenly divisible by four are normally leap years, except for... 
	2.Years also evenly divisible by 100 are not leap years, except for... 
	3.Years also evenly divisible by 400 are leap years. 
	*/
	if ((p_year % 4) == 0) {
		if ((p_year % 100) == 0 && (p_year % 400) != 0)
			return Calendar.DOMonth[monthNo];
	
		return Calendar.lDOMonth[monthNo];
	} else
		return Calendar.DOMonth[monthNo];
}

function Calendar_calc_month_year(p_Month, p_Year, incr) {
	/* 
	Will return an 1-D array with 1st element being the calculated month 
	and second being the calculated year 
	after applying the month increment/decrement as specified by 'incr' parameter.
	'incr' will normally have 1/-1 to navigate thru the months.
	*/
	var ret_arr = new Array();
	
	if (incr == -1) {
		// B A C K W A R D
		if (p_Month == 0) {
			ret_arr[0] = 11;
			ret_arr[1] = parseInt(p_Year) - 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) - 1;
			ret_arr[1] = parseInt(p_Year);
		}
	} else if (incr == 1) {
		// F O R W A R D
		if (p_Month == 11) {
			ret_arr[0] = 0;
			ret_arr[1] = parseInt(p_Year) + 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) + 1;
			ret_arr[1] = parseInt(p_Year);
		}
	}
	
	return ret_arr;
}

function Calendar_print() {
	ggWinCal.print();
}

function Calendar_calc_month_year(p_Month, p_Year, incr) {
	/* 
	Will return an 1-D array with 1st element being the calculated month 
	and second being the calculated year 
	after applying the month increment/decrement as specified by 'incr' parameter.
	'incr' will normally have 1/-1 to navigate thru the months.
	*/
	var ret_arr = new Array();
	
	if (incr == -1) {
		// B A C K W A R D
		if (p_Month == 0) {
			ret_arr[0] = 11;
			ret_arr[1] = parseInt(p_Year) - 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) - 1;
			ret_arr[1] = parseInt(p_Year);
		}
	} else if (incr == 1) {
		// F O R W A R D
		if (p_Month == 11) {
			ret_arr[0] = 0;
			ret_arr[1] = parseInt(p_Year) + 1;
		}
		else {
			ret_arr[0] = parseInt(p_Month) + 1;
			ret_arr[1] = parseInt(p_Year);
		}
	}
	
	return ret_arr;
}

// This is for compatibility with Navigator 3, we have to create and discard one object before the prototype object exists.
new Calendar();

Calendar.prototype.getMonthList = function(){
	var vMonthList = "<select name='monthlise' class=\"Select\" onchange=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', this.options[this.selectedIndex].value, '" + (parseInt(this.gYear)) + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");" +
		"\">";
	for( i=0; i<12; i++) {
		if(i == this.gMonth)
			vMonthList +="<option value='"+i+"' selected>"+Calendar.get_month(i)+"</option>";
		else
			vMonthList +="<option value='"+i+"'>"+Calendar.get_month(i)+"</option>";
	}
	vMonthList += "</select>";
	return vMonthList;
	
}

Calendar.prototype.getMonthlyCalendarCode = function() {
	var vCode = "";
	var vHeader_Code = "";
	var vData_Code = "";
	
	// Begin Table Drawing code here..
	vCode = vCode + "<TABLE WIDTH=\"100%\" BORDER=\"0\" BGCOLOR=\"" + this.gBGColor + "\" cellpadding=\"0\">";
	
	vHeader_Code = this.cal_header();
	vData_Code = this.cal_data();
	vCode = vCode + vHeader_Code + vData_Code;
	
	vCode = vCode + "</TABLE>";
	
	return vCode;
}

Calendar.prototype.show = function() {
	var vCode = "";
	
	this.gWinCal.document.open();

	// Setup the page...
	this.wwrite("<html>");
	this.wwrite("<head><title>Calendar</title>");
	this.wwrite("<link rel=\"stylesheet\" type=\"text/css\" href=\""+gCssPath+"\">")
	this.wwrite("</head>");

	this.wwrite("<body " + 
		"link=\"" + this.gLinkColor + "\" " + 
		"vlink=\"" + this.gLinkColor + "\" " +
		"alink=\"" + this.gLinkColor + "\" " +
		"text=\"" + this.gTextColor + "\">");
	this.wwrite("<form>");
	this.wwrite("<TABLE WIDTH=\"100%\" HEIGHT=\"20\" BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\" class=\"Bg\"><TR class='wbCalTitleBg'>");		
	this.wwriteA("<TD ALIGN=left><Font CLASS='TitleText'>");

	this.wwriteA(DateTitle);
	this.wwriteA("</FONT></TD>");
	this.wwriteA("<TD ALIGN=right>");
	
	this.wwriteA("<A CLASS='TitleText' HREF=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', '" + gNow.getMonth() + "', '" + gNow.getFullYear().toString() + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");");
		this.wwriteA("\">[ " + wb_msg_calendar_today + " ]<\/A>");
	this.wwriteA("</TD></TR></TABLE>");

	// Show navigation buttons
	var prevMMYYYY = Calendar.calc_month_year(this.gMonth, this.gYear, -1);
	var prevMM = prevMMYYYY[0];
	var prevYYYY = prevMMYYYY[1];

	var nextMMYYYY = Calendar.calc_month_year(this.gMonth, this.gYear, 1);
	var nextMM = nextMMYYYY[0];
	var nextYYYY = nextMMYYYY[1];
	vCode = this.getMonthlyCalendarCode();	
	this.wwrite(vCode);	
	this.wwrite("<TABLE WIDTH=\"100%\" BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\" class=\"Bg\"><TR>");
	this.wwrite("<TD ALIGN=center><A  CLASS='TitleText'  HREF=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', '" + this.gMonth + "', '" + (parseInt(this.gYear)-1) + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");" +
		"\">[<<]<\/A></TD>");
	this.wwrite("<TD ALIGN=center><A  CLASS='TitleText' HREF=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', '" + prevMM + "', '" + prevYYYY + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");" +
		"\">[<]<\/A></TD>");
	this.wwrite("<TD ALIGN=center>"+this.getMonthList()+"</TD>");
	this.wwrite("<TD ALIGN=center><A  CLASS='TitleText' HREF=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', '" + nextMM + "', '" + nextYYYY + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");" +
		"\">[>]<\/A></TD>");			
	this.wwrite("<TD ALIGN=center><A  CLASS='TitleText'  HREF=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', '" + this.gMonth + "', '" + (parseInt(this.gYear)+1) + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");" +
		"\">[>>]<\/A></TD>");
	this.wwrite("</TR></TABLE><BR>");
	// Get the complete calendar code for the month..

	this.wwrite("</font></form></body></html>");
	this.gWinCal.document.close();
}

Calendar.prototype.showY = function() {
	var vCode = "";
	var i;
	var vr, vc, vx, vy;		// Row, Column, X-coord, Y-coord
	var vxf = 285;			// X-Factor
	var vyf = 200;			// Y-Factor
	var vxm = 10;			// X-margin
	var vym;				// Y-margin
	if (isIE)	vym = 75;
	else if (isNav)	vym = 25;
	
	this.gWinCal.document.open();

	this.wwrite("<html>");
	this.wwrite("<head><title>Calendar</title>");
	this.wwrite("<style type='text/css'>\n<!--");
	for (i=0; i<12; i++) {
		vc = i % 3;
		if (i>=0 && i<= 2)	vr = 0;
		if (i>=3 && i<= 5)	vr = 1;
		if (i>=6 && i<= 8)	vr = 2;
		if (i>=9 && i<= 11)	vr = 3;
		
		vx = parseInt(vxf * vc) + vxm;
		vy = parseInt(vyf * vr) + vym;

		this.wwrite(".lclass" + i + " {position:absolute;top:" + vy + ";left:" + vx + ";}");
	}
	this.wwrite("-->\n</style>");
	this.wwrite("</head>");

	this.wwrite("<body " + 
		"link=\"" + this.gLinkColor + "\" " + 
		"vlink=\"" + this.gLinkColor + "\" " +
		"alink=\"" + this.gLinkColor + "\" " +
		"text=\"" + this.gTextColor + "\"><form>");
	this.wwrite("<FONT FACE='" + fontface + "' SIZE=2><B>");
	this.wwrite("Year : " + this.gYear);
	this.wwrite("</B><BR>");

	// Show navigation buttons
	var prevYYYY = parseInt(this.gYear) - 1;
	var nextYYYY = parseInt(this.gYear) + 1;
	
	this.wwrite("<TABLE WIDTH='100%' BORDER=1 CELLSPACING=0 CELLPADDING=0 BGCOLOR='#e0e0e0'><TR><TD ALIGN=center>");
	this.wwrite("[<A HREF=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', null, '" + prevYYYY + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");" +
		"\" alt='Prev Year'><<<\/A>]</TD><TD ALIGN=center>");
	//this.wwrite("[<A HREF=\"javascript:window.print();\">Print</A>]</TD><TD ALIGN=center>");
	this.wwrite("[<A HREF=\"" +
		"javascript:window.opener.Build(" + 
		"'" + this.gReturnItem + "', null, '" + nextYYYY + "', '" + this.gFormat + "', '" + gCharacter+ "', '" + gCssPath + "'" +
		");" +
		"\">>><\/A>]</TD></TR></TABLE><BR>");

	// Get the complete calendar code for each month..
	var j;
	for (i=11; i>=0; i--) {
		if (isIE)
			this.wwrite("<DIV ID=\"layer" + i + "\" CLASS=\"lclass" + i + "\">");
		else if (isNav)
			this.wwrite("<LAYER ID=\"layer" + i + "\" CLASS=\"lclass" + i + "\">");

		this.gMonth = i;
		this.gMonthName = Calendar.get_month(this.gMonth);
		vCode = this.getMonthlyCalendarCode();
		this.wwrite(this.gMonthName + "/" + this.gYear + "<BR>");
		this.wwrite(vCode);

		if (isIE)
			this.wwrite("</DIV>");
		else if (isNav)
			this.wwrite("</LAYER>");
	}

	this.wwrite("</font><BR></form></body></html>");
	this.gWinCal.document.close();
}

Calendar.prototype.wwrite = function(wtext) {
	this.gWinCal.document.writeln(wtext);
}

Calendar.prototype.wwriteA = function(wtext) {
	this.gWinCal.document.write(wtext);
}

Calendar.prototype.cal_header = function() {
	var vCode = "";
	
	vCode = vCode + "<TR class=\"SecBg\">";
	for (i=0; i<7; i++) {
		vCode = vCode + "<TD WIDTH=\"14%\" ALIGN=\"CENTER\"><FONT CLASS='Text'><B>"+Calendar.get_week(i)+"</B></FONT></TD>";		
	} 
	vCode = vCode + "</TR>";
	
	return vCode;
}

Calendar.prototype.cal_data = function() {
	var vDate = new Date();
	var vLine = 0;
	vDate.setDate(1);
	vDate.setMonth(this.gMonth);
	vDate.setFullYear(this.gYear);

	var vFirstDay=vDate.getDay();
	var vDay=1;
	var vLastDay=Calendar.get_daysofmonth(this.gMonth, this.gYear);
	var vOnLastDay=0;
	var vCode = "";

	/*
	Get day for the 1st of the requested month/year..
	Place as many blank cells before the 1st day of the month as necessary. 
	*/

	vCode = vCode + "<TR>";
	for (i=0; i<vFirstDay; i++) {
		vCode = vCode + "<TD WIDTH='14%'" + this.write_weekend_string(i) + "></TD>";
	}

	// Write rest of the 1st week
	for (j=vFirstDay; j<7; j++) {		
		vCode = vCode + "<TD WIDTH='14%'" + this.write_weekend_string(j) + ">" + 
			"<A HREF='#' CLASS='wbPopCal' onClick=\"" + 
				/*
				"self.opener.document." + this.gReturnItem + ".value='" + 
				this.format_data(vDay) + "';"+
				*/
				"self.opener." + this.gReturnItem + "_yy.value='" + 
				(this.gYear) +
				"';self.opener." + this.gReturnItem + "_mm.value='" + 
				(this.gMonth+1) +
				"';self.opener." + this.gReturnItem + "_dd.value='" + 
				(vDay) +
				"';window.close();\">" + 
				this.format_day(vDay) + 
			"</A>" + 
			"</TD>";
		vDay=vDay + 1;
	}
	vCode = vCode + "</TR>";

	// Write the rest of the weeks
	for (k=2; k<7; k++) {
		vCode = vCode + "<TR CLASSPATH='wbCalBg'>";
		vLine++;
		for (j=0; j<7; j++) {
			vCode = vCode + "<TD WIDTH='14%'" + this.write_weekend_string(j) + " >" + 
				"<A HREF='#' CLASS='wbPopCal' onClick=\"" + 
					/*
					"self.opener.document." + this.gReturnItem + ".value='" + 
					this.format_data(vDay) + "';" +
					*/
					"self.opener." + this.gReturnItem + "_yy.value='" + 
					(this.gYear) +
					"';self.opener." + this.gReturnItem + "_mm.value='" + 
					(this.gMonth+1) +
					"';self.opener." + this.gReturnItem + "_dd.value='" + 
					(vDay) +
					"';window.close();\">" + 
				this.format_day(vDay) + 
				"</A>" + 
				"</FONT></TD>";
			vDay=vDay + 1;

			if (vDay > vLastDay) {
				vOnLastDay = 1;
				break;
			}
		}

		if (j == 6)
			vCode = vCode + "</TR>";
		if (vOnLastDay == 1)
			break;
	}
	
	// Fill up the rest of last week with proper blanks, so that we get proper square blocks
	for (m=1; m<(7-j); m++) {
		if (this.gYearly)
			vCode = vCode + "<TD WIDTH='14%'" + this.write_weekend_string(j+m) + 
			"><FONT CLASS='Text'> </FONT></TD>";
		else
			vCode = vCode + "<TD WIDTH='14%'" + this.write_weekend_string(j+m) + 
			"><FONT CLASS='Text'>" + m + "</FONT></TD>";
	}
	
	if (vLine<5) {
		vCode = vCode + "<TR>";
		for (m=0; m<7; m++) {		
			vCode = vCode + "<TD WIDTH='14%' align='center'><FONT CLASS='Text'>&nbsp; </FONT></TD>";		
		}
		vCode = vCode + "</TR>";
	}
	
	return vCode;
}

Calendar.prototype.format_day = function(vday) {
	var vNowDay = gNow.getDate();
	var vNowMonth = gNow.getMonth();
	var vNowYear = gNow.getFullYear();

	if (vday == vNowDay && this.gMonth == vNowMonth && this.gYear == vNowYear)
		return ("<FONT CLASS='TextBold'>" + vday + "</FONT>");
	else
		return ("<FONT CLASS='Text'>" + vday + "</FONT>");
}

Calendar.prototype.write_weekend_string = function(vday) {
	var i;

	// Return special formatting for the weekend day.
	for (i=0; i<weekend.length; i++) {
		if (vday == weekend[i])
			return ("CLASS='Bg' ALIGN='CENTER'");
	}
	
	return " ALIGN='CENTER'";
}

Calendar.prototype.format_data = function(p_day) {
	var vData;
	var vMonth = 1 + this.gMonth;
	vMonth = (vMonth.toString().length < 2) ? "0" + vMonth : vMonth;
	var vMon = Calendar.get_month(this.gMonth).substr(0,3).toUpperCase();
	var vFMon = Calendar.get_month(this.gMonth).toUpperCase();
	var vY4 = new String(this.gYear);
	var vY2 = new String(this.gYear.substr(2,2));
	var vDD = (p_day.toString().length < 2) ? "0" + p_day : p_day;

	switch (this.gFormat) {
		case "MM\/DD\/YYYY" :
			vData = vMonth + "\/" + vDD + "\/" + vY4;
			break;
		case "MM\/DD\/YY" :
			vData = vMonth + "\/" + vDD + "\/" + vY2;
			break;
		case "MM-DD-YYYY" :
			vData = vMonth + "-" + vDD + "-" + vY4;
			break;
		case "MM-DD-YY" :
			vData = vMonth + "-" + vDD + "-" + vY2;
			break;

		case "DD\/MON\/YYYY" :
			vData = vDD + "\/" + vMon + "\/" + vY4;
			break;
		case "DD\/MON\/YY" :
			vData = vDD + "\/" + vMon + "\/" + vY2;
			break;
		case "DD-MON-YYYY" :
			vData = vDD + "-" + vMon + "-" + vY4;
			break;
		case "DD-MON-YY" :
			vData = vDD + "-" + vMon + "-" + vY2;
			break;

		case "DD\/MONTH\/YYYY" :
			vData = vDD + "\/" + vFMon + "\/" + vY4;
			break;
		case "DD\/MONTH\/YY" :
			vData = vDD + "\/" + vFMon + "\/" + vY2;
			break;
		case "DD-MONTH-YYYY" :
			vData = vDD + "-" + vFMon + "-" + vY4;
			break;
		case "DD-MONTH-YY" :
			vData = vDD + "-" + vFMon + "-" + vY2;
			break;

		case "DD\/MM\/YYYY" :
			vData = vDD + "\/" + vMonth + "\/" + vY4;
			break;
		case "DD\/MM\/YY" :
			vData = vDD + "\/" + vMonth + "\/" + vY2;
			break;
		case "DD-MM-YYYY" :
			vData = vDD + "-" + vMonth + "-" + vY4;
			break;
		case "DD-MM-YY" :
			vData = vDD + "-" + vMonth + "-" + vY2;
			break;

		default :
			vData = vMonth + "\/" + vDD + "\/" + vY4;
	}

	return vData;
}

function Build(p_item, p_month, p_year, p_format, wb_Character, p_CssPath) {
	var p_WinCal = ggWinCal;
	gCal = new Calendar(p_item, p_WinCal, p_month, p_year, p_format, wb_Character, p_CssPath);

	// Customize your Calendar here..
	gCal.gBGColor="white";
	gCal.gLinkColor="black";
	gCal.gTextColor="black";
	gCal.gHeaderColor="darkgreen";

	// Choose appropriate show function
	if (gCal.gYearly)	gCal.showY();
	else	gCal.show();
}

function show_calendar() {
	/* 
		p_month : 0-11 for Jan-Dec; 12 for All Months.
		p_year	: 4-digit year
		p_format: Date format (mm/dd/yyyy, dd/mm/yy, ...)
		p_item	: Return Item.
	*/
	/*兼容IE*/
	if(document[0]){
		p_item = "document[0]."+arguments[0].split(".")[arguments[0].split(".").length-1];
	}else{
		p_item = arguments[0];
	}
	
	//p_item = arguments[0];
	//validate month
	/*	if (arguments[1] == null || arguments[1] == "") {
		if (eval(p_item+"_mm.value") == null || wbUtilsTrimString(eval(p_item+"_mm.value")) == "" ){
*/			p_month = new String(gNow.getMonth());
			/*} else {
			if(!wbUtilsValidateDate(p_item, wb_msg_calendar_month, 'MM')){
				return;
			}
			p_month = eval(p_item+"_mm.value")-1;
		}	
			} else {
		p_month = arguments[1];
	}*/
	//validate year
	/*	if (arguments[2] == "" || arguments[2] == null) {
		if (eval(p_item+"_yy.value") == null || wbUtilsTrimString(eval(p_item+"_yy.value")) == ""){
*/			p_year = new String(gNow.getFullYear().toString());
/*		} else	{
			if(!wbUtilsValidateDate(p_item, wb_msg_calendar_year, 'YY'))
				return;
			p_year = eval(p_item+"_yy.value");
		}
		} else {
		p_year = arguments[2];
	}*/
	
	if (arguments[3] == null)
		p_format = "MM/DD/YYYY";
	else
		p_format = arguments[3];	
	
	wb_Character = arguments[4];
	p_CssPath = arguments[5];
	
	vWinCal = wbUtilsOpenWin("", "Calendar", false,
		"width=250,height=200,status=no,resizable=no,top=200,left=200");
	vWinCal.opener = self;
	vWinCal.focus();
	ggWinCal = vWinCal;
	Build(p_item, p_month, p_year, p_format, wb_Character, p_CssPath);
}
/*
Yearly Calendar Code Starts here
*/
function show_yearly_calendar(p_item, p_year, p_format) {
	// Load the defaults..
	if (p_year == null || p_year == "")
		p_year = new String(gNow.getFullYear().toString());
	if (p_format == null || p_format == "")
		p_format = "MM/DD/YYYY";

	var vWinCal = wbUtilsOpenWin("", "Calendar", false, "scrollbars=yes");
	vWinCal.opener = self;
	ggWinCal = vWinCal;


	Build(p_item, null, p_year, p_format);
}


