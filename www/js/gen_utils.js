// ------------------ General untility functions -------------------
// Convention:
//   public functions : use "gen_" prefix
//   private functions: use "_gen_" prefix
// Dependency:
//   none
// ------------------------------------------------------------

// -------------- constants --------------
MEDIA_TYPE_GIF = 'image/gif'
MEDIA_TYPE_JPG = 'image/jpg'
MEDIA_TYPE_SWF = 'application/x-shockwave-flash'
MEDIA_TYPE_UNKNOWN ='unknown'

// -------------------- image swap functions --------------------
function gen_img_act(imgName) {
	imgOn = eval(imgName + 'on.src');
	document[imgName].src = imgOn;
}

function gen_img_inact(imgName) {
	imgOff = eval(imgName + 'off.src');
	document[imgName].src = imgOff;
}

//----------------iLayer/layer image swap function ----------
function gen_layer_img_act(imgName) {
	if (document.all){
	imgOn = eval(imgName + 'on.src');
	document[imgName].src = imgOn;
	}
}

function gen_layer_img_inact(imgName) {
if (document.all){
	imgOff = eval(imgName + 'off.src');
	document[imgName].src = imgOff;}
}

// -------------------- numeric functions --------------------
function gen_get_random_num(n) {
	return Math.floor(Math.random() * n)
}

function gen_is_float(strValue) {
	floatValue = parseFloat(strValue);
	newStrValue = String(floatValue);
	if (strValue == newStrValue) {
		return true;
	}
	else {
		return false;
	}
}

function gen_is_int(strValue) {
	intValue = parseInt(strValue);
	newStrValue = String(intValue);
	if (strValue == newStrValue) {
		return true;
	}
	else {
		return false;
	}
}

function gen_trim_float(in_val, pos){
	var out_val, float_val, round_up, round_up_val, i, str
	str = ''
	if (isNaN(in_val) != true) {in_val = in_val.toString();}
	if (in_val.indexOf('.')==-1)
		float_val = ''
	else
		float_val = in_val.substring(in_val.indexOf('.')+1, in_val.length)
	//Dialog.alert("["+float_val+"]");
	out_val = in_val.substring(0, in_val.length-(float_val.length-pos))
	//Dialog.alert("["+out_val+"]");
	if (float_val.length > pos){
		if (parseInt(in_val.substring(in_val.length-(float_val.length-pos),in_val.length-(float_val.length-pos)+1)) >= 5){
			round_up = true;
		}else{
			round_up = false;
		}

		if (round_up == true){
			str = '0.'
			for(i = 0; i < pos-1; i++) {str += '0'}
			round_up_val = parseFloat(str+1)
			out_val = parseFloat(out_val) + round_up_val
			return out_val;
		}else{
			return out_val;
		}

	}else if(float_val.length < pos){
		out_val = in_val
		if(out_val.indexOf('.')==-1){out_val += '.'}
		for(i = 0; i < (pos-float_val.length); i++){out_val += '0';}
		return out_val;
	}else{
		return out_val;
	}
}

// -------------------- string functions --------------------
function gen_validate_usr_id(fld_usr_id,lang,checkCase) {
	var lab_login_id = fld_usr_id.form.lab_login_id ? fld_usr_id.form.lab_login_id.value :'Login ID'
//	if ((checkCase==null||checkCase)&&fld_usr_id.value.toLowerCase() != fld_usr_id.value) {
//		Dialog.alert(wb_msg_pls_enter_lower_case_characters + '"'+lab_login_id+'"');
//		return false;
//	}

	if (fld_usr_id.value.search(/[^A-Za-z0-9_\-]/) != -1) {
		Dialog.alert(wb_msg_pls_enter_lower_case_alpha_num_underscore_hyphen + '"'+lab_login_id+'"');
		fld_usr_id.focus();
		return false;
	}

	if (fld_usr_id.value.length == 0) {
		Dialog.alert(wb_msg_pls_enter_the + '"'+lab_login_id+'"');

		fld_usr_id.focus();
		return false;
	}

	return true;
}


function gen_validate_usr_pwd(fld_usr_pwd,lang) {
	_MIN_FIELD_LEN = 1

	if (fld_usr_pwd.value.length < _MIN_FIELD_LEN && _MIN_FIELD_LEN == 1) {
		Dialog.alert(wb_msg_pls_enter_password);

		fld_usr_pwd.focus();
		return false;
	} else if (fld_usr_pwd.value.length < _MIN_FIELD_LEN) {
		Dialog.alert(wb_msg_pls_enter_at_least + _MIN_FIELD_LEN + wb_msg_characters_for_password);

		fld_usr_pwd.focus();
		return false;
	}

	return true;
}

function gen_validate_email(fld_email,lang) {
//Deprecated, Please use the new function at wb_utils.js
	if (fld_email.value.search(/^[^@]+@[^\.]+/) == -1 || fld_email.value.search(/\s/) != -1) {
		Dialog.alert(wb_msg_pls_enter_valid_email);

		fld_email.focus();
		return false;
	}

	if (fld_email.value.search(/[^A-Za-z0-9_\-@.&!\/]/) != -1) {
		Dialog.alert(wb_msg_pls_enter_valid_email);
		fld_email.focus();
		return false;
	}
	return true;
}

function gen_validate_integer(fld, txtFldName,lang) {
//Deprecated, Please use the new function at wb_utils.js
	var valPass = true
	val = wbUtilsTrimString(fld.value)

	if (fld.value.indexOf('.') != -1){
		valPass = false;
	}else if (val.length == 0 || val.search(/[^0-9]/) != -1  ) {
		if ( isNaN(Number(val)) ) {valPass = false;}
	}
	if (Number(val) > 99999) {
		valPass = false;
	}

	//值为0也提示
	if (valPass == false || val == 0){
		Dialog.alert(wb_msg_pls_enter_integer_less_than_99999_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_integer_less_than_99999_2);
		fld.focus();
		return false;
	}else {return true;}
}

function gen_validate_float(fld, txtFldName, lang) {
	var val = wbUtilsTrimString(fld.value);
	if(txtFldName == fetchLabel('lab_credit_test') || txtFldName == fetchLabel('lab_fee_test')){
		if ( (val.search(/^\d{1,9}$/) == -1 && val.search(/^\d{1,9}\.\d{1,2}$/) == -1)  || Number(val) <= 0 || Number(val) >=1000) {
			Dialog.alert(wb_msg_pls_enter_value_less_than_999_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_value_less_than_999_2);
			fld.focus();
			return false;
		}
	}else {
		if ( ((val.search(/^\d{1,9}$/) == -1 && val.search(/^\d{1,9}\.\d{1,2}$/) == -1)) || Number(val) <= 0 ) {
			if(val != -1) {
				Dialog.alert(wb_msg_pls_enter_value_less_than_999999999_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_value_less_than_999999999_2);
				fld.focus();
				return false;
			}
		}
	}	
	return true;
}

function gen_validate_float1000(fld, txtFldName, lang) {
	var val = wbUtilsTrimString(fld.value);
	if ( (val.search(/^\d{1,9}$/) == -1 && val.search(/^\d{1,9}\.\d{1,2}$/) == -1) || Number(val) >=1000) {
		Dialog.alert(wb_msg_pls_enter_value_less_than_999_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_value_less_than_999_2);
		fld.focus();
		return false;
	}
	return true;
}

function gen_validate_float_less_than_100(fld, txtFldName) {
	var val = wbUtilsTrimString(fld.value);
	if ( (val.search(/^\d{1,3}$/) == -1 && val.search(/^\d{1,3}\.\d{1,2}$/) == -1) || parseFloat(val) > 100) {

		Dialog.alert(wb_msg_pls_enter_value_less_than_100_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_value_less_than_100_2);
		fld.focus();
		return false;
	}
	return true;
}

function gen_validate_float_less_than_5(fld, txtFldName) {
	var val = wbUtilsTrimString(fld.value);
	if ( (val.search(/^\d{1,3}$/) == -1 && val.search(/^\d{1,3}\.\d{1,2}$/) == -1) || parseFloat(val) > 5) {

		Dialog.alert(wb_msg_pls_enter_value_less_than_5_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_value_less_than_5_2);
		fld.focus();
		return false;
	}
	return true;
}


function gen_validate_pencentage(fld, txtFldName,lang) {
	val = wbUtilsTrimString(fld.value);

	if (val.search(/[^0-9]/) != -1 || val == '') {
		Dialog.alert(wb_msg_pls_enter_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_integer_2);
		fld.focus();
		return false;
	}else if (fld.value > 100 ){
		Dialog.alert(wb_msg_pls_enter_0_to_100_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_0_to_100_2);
		fld.focus();
		return false;
	}
	else if(fld.value < 0 ){
		Dialog.alert(wb_msg_pls_enter_0_to_100_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_0_to_100_2);
		fld.focus();
		return false;
	}

	return true;
}


function gen_validate_positive_integer(fld,txtFldName,lang){

	val = wbUtilsTrimString(fld.value)

	if ( val.search(/[^0-9]/) != -1 || Number(val) <= 0 ){
		Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);

		fld.focus();
		return false;

	}
	if ( val > 999999999 ){
		Dialog.alert(txtFldName + wb_msg_pls_enter_smaller_number);
		fld.focus();
		return false;

	}
	return true;
}

function gen_validate_positive_int(fld,txtFldName,lang){
	val = wbUtilsTrimString(fld.value)
	if ( val.search(/[^0-9]/) != -1 || Number(val) < 0 ){
		Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}
	if ( val > 999999999 ){
		Dialog.alert(txtFldName + wb_msg_pls_enter_smaller_number);
		fld.focus();
		return false;
	}
	return true;
}


function gen_validate_negative_integer(fld,txtFldName,lang){

	if ( Number(fld.value) < -999999999 ){
		Dialog.alert(wb_msg_pls_enter_larger_number);

		fld.focus();
		return false;

	}
	return true;
}

function gen_validate_empty_field(fld, txtFldName,lang) {
//Deprecated, Please use the new function at wb_utils.js
	if (fld.type.toLowerCase().indexOf('select') != -1) {
		val = fld.options[fld.selectedIndex].value
	} else {
		val = fld.value
	}

	if (val.length == 0 || val.search(/^\s+$/) != -1) {
		Dialog.alert(wb_msg_pls_specify_value + txtFldName);
		if((fld.type=='textarea' || fld.type=='text') && fld.style.display != "none")
			fld.focus();
		return false;
	}

	return true;
}

function gen_validate_max_integer(fld,txtFldName,lang){

	val = wbUtilsTrimString(fld.value)

	if ( val.search(/[^0-9]/) != -1 ){
		Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);

		fld.focus();
		return false;

	}
	if ( val > 9999 ){
		Dialog.alert(txtFldName + wb_msg_pls_enter_smaller_number.replace("999999999", "9999"));
		fld.focus();
		return false;

	}
	return true;
}

function gen_validate_max_integer_diy_value(fld,txtFldName,lang, max_value, is_eq_zero){

	val = wbUtilsTrimString(fld.value)

	if (val.search(/[^0-9]/) != -1 || Number(val) < 0 || (is_eq_zero == true && Number(val) == 0)){
		Dialog.alert(wb_msg_pls_enter_positive_integer_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_positive_integer_2);
		fld.focus();
		return false;
	}
	if (val > max_value){
		Dialog.alert(txtFldName + wb_msg_pls_enter_smaller_number.replace("999999999", max_value));
		fld.focus();
		return false;

	}
	return true;
}


function gen_Leap(y) {
	return ((y % 400 == 0) || (y % 100 != 0 && y % 4 == 0));
}

function gen_Month_Length(month, year) {
	if(month == 2)
		return 28 + gen_Leap(year);
	else if(month == 4 || month == 6 || month == 9 || month == 11)
	   return 30;
	else
		return 31;
}


function gen_validate_date(fldName,txtFldName,lang) {
//Deprecated, Please use the new function wbUtilsValidateDate at wb_utils.js
// form name should be "frmXml"

// validate year
		fld = fldName + '_yy'

		eval(fld).value = wbUtilsTrimString(eval(fld).value)

		if ( eval(fld).value.length != 4  || Number(eval(fld).value) < 1800  ){
			Dialog.alert(wb_msg_pls_enter_valid_time + '"' + txtFldName + '"');

			eval(fld).focus();
			return false;
		}

		if (!gen_validate_integer(eval(fld),txtFldName,lang))
			return false;

		// validate month
		fld = fldName + '_mm'
		eval(fld).value = wbUtilsTrimString(eval(fld).value)
		if ( Number(eval(fld).value) < 10  && eval(fld).value.length == 1  ){
			eval(fld).value = '0' + eval(fld).value
		}

		if ( eval(fld).value.length != 2  || eval(fld).value > 12 || eval(fld).value < 1 ){
			Dialog.alert(wb_msg_pls_enter_valid_time + '"' + txtFldName + '"');
			eval(fld).focus();
			return false;
		}

		if (!gen_validate_integer(eval(fld),txtFldName,lang))
			return false;


		// validate day

		fld = fldName + '_dd'

		eval(fld).value = wbUtilsTrimString(eval(fld).value)
		if ( Number(eval(fld).value) < 10 && eval(fld).value.length == 1  ){
			eval(fld).value = '0' + eval(fld).value
		}


		if ( eval(fld).value.length != 2 || eval(fld).value > gen_Month_Length(Number(eval(fldName+'_mm.value')), eval(fldName+'_yy.value')) || eval(fld).value < 1){
			Dialog.alert(wb_msg_pls_enter_valid_time + '"' + txtFldName + '"');
			eval(fld).focus();
			return false;
		}

		if (!gen_validate_integer(eval(fld),txtFldName,lang))
			return false;

	return true;
}

function gen_validate_date_compare(frm, start_obj, end_obj, start_nm, end_nm, lang, focus_obj){

	var _start_date, _end_date

	_start_date = new Date(eval('frm.' + start_obj + '_yy.value'), Number(eval('frm.' + start_obj + '_mm.value'))-1, Number(eval('frm.' + start_obj + '_dd.value')), Number(eval('frm.' + start_obj + '_hour.value')), Number(eval('frm.' + start_obj + '_min.value')))
	_end_date = new Date(eval('frm.' + end_obj + '_yy.value'), Number(eval('frm.' + end_obj + '_mm.value'))-1, Number(eval('frm.' + end_obj + '_dd.value')), Number(eval('frm.' + end_obj + '_hour.value')), Number(eval('frm.' + end_obj + '_min.value')))

	if (focus_obj == null || focus_obj == '') {focus_obj = start_obj;}
	if(_start_date >= _end_date){
		Dialog.alert('"' + end_nm + '" ' + wb_msg_cannot_earlier_than + ' "' + start_nm + '"');
		eval('frm.' + focus_obj + '_yy.focus()')
		return false;
	}
	return true;
}

function gen_validate_date_compare_larger(frm, start_obj, end_obj, start_nm, end_nm, lang, focus_obj){

	var _start_date, _end_date

	_start_date = new Date(eval('frm.' + start_obj + '_yy.value'), Number(eval('frm.' + start_obj + '_mm.value'))-1, Number(eval('frm.' + start_obj + '_dd.value')), Number(eval('frm.' + start_obj + '_hour.value')), Number(eval('frm.' + start_obj + '_min.value')))
	_end_date = new Date(eval('frm.' + end_obj + '_yy.value'), Number(eval('frm.' + end_obj + '_mm.value'))-1, Number(eval('frm.' + end_obj + '_dd.value')), Number(eval('frm.' + end_obj + '_hour.value')), Number(eval('frm.' + end_obj + '_min.value')))

	if (focus_obj == null || focus_obj == '') {focus_obj = start_obj;}
	if(_start_date > _end_date){
		Dialog.alert('"' + end_nm + '" ' + wb_msg_cannot_earlier_than + ' "' + start_nm + '"');
		eval('frm.' + focus_obj + '_yy.focus()')
		return false;
	}
	return true;
}

//Deprecated, Please use the new function wbUtilsValidateDate at wb_utils.js
function gen_validate_time(fldName,txtFldName,lang) {
// form name should be "frmXml"

		// validate hour
		fld = fldName + '_hour'
		eval(fld).value = wbUtilsTrimString(eval(fld).value)

		if ( Number(eval(fld).value) < 10  && eval(fld).value.length == 1  ){
			eval(fld).value = '0' + eval(fld).value
		}



		if ( eval(fld).value.length != 2  || eval(fld).value > 23 || eval(fld).value < 0 ){
			Dialog.alert(wb_msg_pls_enter_valid_hour_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_valid_hour_2);
			eval(fld).focus();
			return false;
		}


		if (!gen_validate_integer(eval(fld),txtFldName,lang))
			return false;
		// validate minute
		fld = fldName + '_min'

		eval(fld).value = wbUtilsTrimString(eval(fld).value)

		if ( Number(eval(fld).value) < 10  && eval(fld).value.length == 1  ){
			eval(fld).value = '0' + eval(fld).value
		}



		if ( eval(fld).value.length != 2  || eval(fld).value > 59 || eval(fld).value < 0 ){
			Dialog.alert(wb_msg_pls_enter_valid_minute_1 + '"' + txtFldName + '"' + wb_msg_pls_enter_valid_minute_2);

			eval(fld).focus();
			return false;
		}

		if (!gen_validate_integer(eval(fld),txtFldName,lang))
			return false;

	return true;
}


function gen_validate_cur_time(fldName,txtFldName,lang){
	var now_date, fldName_date

	now_date = new Date()
	fldName_date = new Date(eval(fldName + '_yy.value'), Number(eval(fldName + '_mm.value'))-1, Number(eval(fldName + '_dd.value')))
	if (fldName_date < now_date){
		Dialog.alert(wb_msg_cannot_earlier_than_present_1 + '"' + txtFldName + '"' + wb_msg_cannot_earlier_than_present_2)
		return false;
	}
	return true;
}

function gen_validate_equal_and_greater_cur_time(fldName,txtFldName,lang){
	var now_date, fldName_date

	now_date = new Date()
	if(eval(fldName + '_hour')) {
		fldName_date = new Date(eval(fldName + '_yy.value'), Number(eval(fldName + '_mm.value'))-1, Number(eval(fldName + '_dd.value')), eval(fldName + '_hour.value'), eval(fldName + '_min.value'), 59)
	} else {
		fldName_date = new Date(eval(fldName + '_yy.value'), Number(eval(fldName + '_mm.value'))-1, Number(eval(fldName + '_dd.value')), 23, 59, 59)
	}
	if (fldName_date < now_date){
		Dialog.alert(wb_msg_cannot_earlier_than_present_1 + '"' + txtFldName + '"' + wb_msg_cannot_earlier_than_present_2)
		return false;
	}
	return true;
}


//--------------------- Netscape CSS Layer Fix------------------
function WQ_netscapeCssFix() {
  if (document.WQ.WQ_netscapeCssFix.initWindowWidth != window.innerWidth || document.WQ.WQ_netscapeCssFix.initWindowHeight != window.innerHeight) {
    document.location = document.location;
  }
}

function WQ_netscapeCssFixCheckIn() {
  if ((navigator.appName == 'Netscape') && (parseInt(navigator.appVersion) == 4)) {
    if (typeof document.WQ == 'undefined'){
      document.WQ = new Object;
    }
    if (typeof document.WQ.WQ_scaleFont == 'undefined') {
      document.WQ.WQ_netscapeCssFix = new Object;
      document.WQ.WQ_netscapeCssFix.initWindowWidth = window.innerWidth;
      document.WQ.WQ_netscapeCssFix.initWindowHeight = window.innerHeight;
    }
    window.onresize = WQ_netscapeCssFix;
  }
}

// -------------------- cookie functions --------------------

//_GEN_COOKIE_DELIMITER = "\f\n~\t\r"
//_GEN_COOKIE_DELIMITER_ESCAPED = "\\f\\n~\\t\\r"
_GEN_COOKIE_DELIMITER = '\f'
_GEN_COOKIE_DELIMITER_ESCAPED = '\\f'


function gen_get_cookie(name) {
    var start = document.cookie.indexOf(name+'=');

    var len = start+name.length+1;
    if ((!start) && (name != document.cookie.substring(0,name.length))) return '';

    if (start == -1) return '';
    var end = document.cookie.indexOf(';',len);
    if (end == -1) end = document.cookie.length;
    return unescape(document.cookie.substring(len,end));
}

function gen_set_cookie(name,value,expires,path,domain,secure) {
    document.cookie = name + '=' +escape(value) +
        ( (expires) ? ';expires=' + expires.toGMTString() : '') +
        ( (path) ? ';path=' + path : ';path=/') +
        ( (domain) ? ';domain=' + domain : '') +
        ( (document.location.protocol == 'https:') ? ';secure' : '');


}

function gen_del_cookie(name,path,domain) {
    if (gen_get_cookie(name)) document.cookie = name + '=' +
        ( (path) ? ';path=' + path : ';path=/') +
        ( (domain) ? ';domain=' + domain : '') +
        ';expires=Thu, 01-Jan-70 00:00:01 GMT';
}

function gen_get_expiry_date(howmanyyear) {
	day = new Date();
	yr = day.getFullYear() + howmanyyear;
	day.setFullYear(yr);

	return day;
}

function gen_set_cookie_token(cookie_nm, token_nm, token_val, expires) {

	var re = new RegExp(_GEN_COOKIE_DELIMITER_ESCAPED + token_nm + '=' + '[^' + _GEN_COOKIE_DELIMITER_ESCAPED + ']*' + _GEN_COOKIE_DELIMITER_ESCAPED);
	var cookie_val = gen_get_cookie(cookie_nm)
	var token = token_nm + '=' + token_val;

	if (cookie_val.search(re) != -1) {
		cookie_val = cookie_val.replace(re, _GEN_COOKIE_DELIMITER + token + _GEN_COOKIE_DELIMITER);
	} else {
		cookie_val = cookie_val +  _GEN_COOKIE_DELIMITER + token + _GEN_COOKIE_DELIMITER;
	}
	if (expires)
		gen_set_cookie(cookie_nm, cookie_val, expires)
	else
		gen_set_cookie(cookie_nm, cookie_val)
}

function gen_get_cookie_token(cookie_nm, token_nm) {

	var re = new RegExp(_GEN_COOKIE_DELIMITER_ESCAPED + token_nm + '=' + '[^' + _GEN_COOKIE_DELIMITER_ESCAPED + ']*' + _GEN_COOKIE_DELIMITER_ESCAPED);
	var re = new RegExp(_GEN_COOKIE_DELIMITER_ESCAPED + token_nm + '=' + '[^' + _GEN_COOKIE_DELIMITER_ESCAPED + ']*' + _GEN_COOKIE_DELIMITER_ESCAPED);
	var cookie_val = gen_get_cookie(cookie_nm)
	var token_val
	if (cookie_val.search(re) != -1) {
		var token = cookie_val.match(re)[0]
		token_val = token.substring(token_nm.length + 2, token.length-1)
	} else {
		token_val = ''
	}

	return token_val;
}

function gen_del_cookie_token(cookie_nm, token_nm){
}

// -------------------- url parameter functions --------------------
function gen_get_url_param(name) {
	var strParam = window.location.search

	idx1 = strParam.indexOf(name + '=')
	if (idx1 == -1)	return ''

	idx1 = idx1 + name.length + 1
	idx2 = strParam.indexOf('&', idx1)

	if (idx2 != -1)
		len = idx2 - idx1
	else
		len = strParam.length

	return unescape(strParam.substr(idx1, len))
}

function gen_set_url_param(name, value, url) {

	if (url) {
		idx0 = url.indexOf('?')
		if (idx0 == -1) {
			strParam = ''
		} else {
			strParam = url.substr(idx0,url.length)
		}
	} else {
		strParam = window.location.search
	}

	idx1 = strParam.indexOf(name + '=')
	if (idx1 == -1)	{
		if (strParam == '') {
			strParam = '?' + name + '=' + value
		} else {
			strParam = strParam + '&' + name + '=' + value
		}
	} else {
		idx1 = idx1 + name.length + 1
		idx2 = strParam.indexOf('&', idx1)
		if (idx2 == -1) {
			strParam = strParam.substr(0,idx1) + value
		} else {
			suffx = strParam.substr(idx2, strParam.length)
			strParam = strParam.substr(0,idx1) + value + suffx
		}
	}

	if (url) {
		if (idx0 == -1) {
			return url + strParam
		} else {
			return url.substr(0, idx0) + strParam
		}
	} else {
		return window.location.pathname + strParam
	}
}

//--------------------- Netscape CSS Layer Fix------------------
function WQ_netscapeCssFix() {
  if (document.WQ.WQ_netscapeCssFix.initWindowWidth != window.innerWidth || document.WQ.WQ_netscapeCssFix.initWindowHeight != window.innerHeight) {
    document.location = document.location;
  }
}

function WQ_netscapeCssFixCheckIn() {
  if ((navigator.appName == 'Netscape') && (parseInt(navigator.appVersion) == 4)) {
    if (typeof document.WQ == 'undefined'){
      document.WQ = new Object;
    }
    if (typeof document.WQ.WQ_scaleFont == 'undefined') {
      document.WQ.WQ_netscapeCssFix = new Object;
      document.WQ.WQ_netscapeCssFix.initWindowWidth = window.innerWidth;
      document.WQ.WQ_netscapeCssFix.initWindowHeight = window.innerHeight;
    }
    window.onresize = WQ_netscapeCssFix;
  }
}

// -------------- file name --------------

function getFilename(pathname) {
	s = pathname.lastIndexOf('\\')
	if (s == -1) {
		s = pathname.lastIndexOf('/')
	}
	if (s == -1) {
		return pathname
	}

	l = pathname.length - s
	return pathname.substr(s+1,l)
}

function getMediaType(filename) {

	s = filename.lastIndexOf('.')
	if (s == -1)
		return MEDIA_TYPE_UNKNOWN

	l = filename.length
	suffx = filename.substr(s+1,l)

	if (suffx == 'gif')
		return MEDIA_TYPE_GIF
	else if (suffx == 'jpg' || suffx == 'jpeg')
		return MEDIA_TYPE_JPG
	else if (suffx == 'swf')
		return MEDIA_TYPE_SWF
	else
		return MEDIA_TYPE_UNKNOWN
}


// -------------- auto foucs for date field --------------
function auto_focus_field(org_field,length,focus_field){
if(document.all || document.getElementById!=null){
	if ( event.keyCode != 9 && event.keyCode != 16 ){
		if ( org_field.value.length == length )
			focus_field.select()
	}
}
}

// --------------- form select all checkbox ------------------
function gen_frm_sel_all_checkbox(frm,trigger_checkbox,ele_nm){

	var i, n, ele
	n = frm.elements.length
	for (i = 0; i < n; i++){
		ele = frm.elements[i]

		if (ele_nm != null && ele_nm != ''){
			if (ele.type == 'checkbox'  && ele.name == ele_nm)

				if (trigger_checkbox.type == 'checkbox'){
					if (trigger_checkbox.checked == true)
						ele.checked = true;
					else
						ele.checked = false;
				}else if (trigger_checkbox.type == 'hidden'){
					if (trigger_checkbox.value == 'true'){
						ele.checked = false;
					}else{
						ele.checked = true;
					}
				}
		}else{
			if (ele.type == 'checkbox')

				if (trigger_checkbox.type == 'checkbox'){
					if (trigger_checkbox.checked == true)
						ele.checked = true;
					else
						ele.checked = false;
				}else if (trigger_checkbox.type == 'hidden'){
					if (trigger_checkbox.value == 'true'){
						ele.checked = false;
					}else{
						ele.checked = true;
					}
				}
		}
	}

	if (trigger_checkbox.type == 'hidden'){
		if (trigger_checkbox.value == 'true')
			trigger_checkbox.value = 'false';
		else
			trigger_checkbox.value = 'true';
	}
}

// --------------- form select all checkbox ------------------
function gen_frm_sel_all_enabled_checkbox(frm,trigger_checkbox,ele_nm){

	var i, n, ele
	n = frm.elements.length
	for (i = 0; i < n; i++){
		ele = frm.elements[i]

		if (ele_nm != null && ele_nm != ''){
			if (ele.type == 'checkbox'  && ele.name == ele_nm && ele.disabled == false)

				if (trigger_checkbox.type == 'checkbox'){
					if (trigger_checkbox.checked == true)
						ele.checked = true;
					else
						ele.checked = false;
				}else if (trigger_checkbox.type == 'hidden'){
					if (trigger_checkbox.value == 'true'){
						ele.checked = false;
					}else{
						ele.checked = true;
					}
				}
		}else{
			if (ele.type == 'checkbox' && ele.disabled == false)

				if (trigger_checkbox.type == 'checkbox'){
					if (trigger_checkbox.checked == true)
						ele.checked = true;
					else
						ele.checked = false;
				}else if (trigger_checkbox.type == 'hidden'){
					if (trigger_checkbox.value == 'true'){
						ele.checked = false;
					}else{
						ele.checked = true;
					}
				}
		}
	}

	if (trigger_checkbox.type == 'hidden'){
		if (trigger_checkbox.value == 'true')
			trigger_checkbox.value = 'false';
		else
			trigger_checkbox.value = 'true';
	}
}

// --------------- DHTML functions ------------------
function gen_dhtml_change_class(src, newClassName){
	if (document.all){src.className = newClassName;};
}

//--------------- close window without confirm ------
function gen_close_win (winObj) {
    if (typeof(winObj.opener) == 'undefined') {
        winObj.opener = null;
    }

    winObj.close();
}
function gen_validate_compare(v1 , v2 , staff_no , staff_name) {
	if(Number(v1) > Number(v2)){
		Dialog.alert(staff_no + " "+ staff_name + ": " + wb_compare_cpd_core);
		return false;
	}
	return true;
}
function gen_trim_string(str) {
	str =  str.replace(/\s+$/, "")
	str =  str.replace(/^\s+/, "")
	return str
}
function mobile_close(winObj, tkh_id){
	var ua = navigator.userAgent.toLowerCase();
	if (((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))) {
		winObj.location.href = wb_utils_app_base + 'app/course/return/' + tkh_id;
	} else {
		gen_close_win(winObj);
	}
}