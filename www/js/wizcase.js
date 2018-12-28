//url
wb_utils_disp_servlet_url = '../servlet/Dispatcher'

//cookie
_GEN_COOKIE_DELIMITER = "\f"
_GEN_COOKIE_DELIMITER_ESCAPED = "\\f"


function concate_string() {
	var path = '';
	for (i = 0; i < arguments.length; i = i + 2) {	
		path += '&' + arguments[i] + '=' + arguments[i+1]
	}
	return path;
}

function setCheck(args,check)
{
if (args.length >= 1) {	
	var oneChar;
	var end;
	var start=0;
	var value;
	for (i=0;i<args.length;i++) {
		oneChar=args.charAt(i);
		 if (oneChar == "|") {
		 	end = i;
			
			value=args.substr(start,end-start);
			if (eval("document.frmXml." + check + ".checked"))
				{
					eval("document.frmXml." + value + ".checked=true");				
				}else{
					eval("document.frmXml." + value + ".checked=false");
				}
			start=end+1;
		}
	}
	
	
}
}
function number_format(n) {
  if(n == "-") return n;
  var isPositive = eval(n<0);//check the number is positive
  if(isPositive == true)	n = -n;
  var arr=new Array('0'), i=0; 
  while (n>0){
  arr[i]=''+n%1000; n=Math.floor(n/1000); i++;}
  arr=arr.reverse();
  for (var i in arr) if (i>0) //padding zeros
    while (arr[i].length<3) arr[i]='0'+arr[i];
  	
	if(isPositive == true){//Negative Number with ()
  		var temp = arr;
		arr = '(' + temp + ')';
		//arr = '-' + arr;
	}else if(arr == "0")arr = "-";
	
  return arr;
  //return arr.join();
  
}


function validated(string,name) {
	if((string.charAt(0))== "("||(string.charAt(string.lenght-1)) == ")"){
	string = "-" + string;
	
	}
	for (var i=0, output='', valid="-.1234567890", seperate="(),"; i<string.length; i++){
      if (valid.indexOf(string.charAt(i)) != -1){
          output += string.charAt(i);
	  }else if(seperate.indexOf(string.charAt(i)) != -1){
	  }else{
	   	if(string.length-1 == i){
			alert("Please enter integer ");
			//eval('document.frmXml.' + name  + '.focus()');
		}
	  }	
	}	  
	output = Math.round(output);
    return output;
}
function getBack(val,name){
	num = validated(val,name);
	output = number_format(num);
	return output;
}


function wizcase_utils_init_css() {

	frm = arguments[0];
	
	if (document.all || document.getElementById!=null){	
		n = frm.elements.length;
		for (i = 0; i < n; i++) {
			ele = frm.elements[i]
			
			for ( j = 1; j < arguments.length; j++ ) {
				if (ele.type == arguments[j])
					//alert(ele.type)
					ele.className="wizcaseGenInputFrm"				
			}	
		}
	}
}

function wizcase_invoke_servlet() {
	var path = '../servlet/Dispatcher?module=wizcase.WizCaseModule' 
	
	//path += 'env=wizb';	
	for (i = 0; i < arguments.length; i = i + 2) {	
		if (arguments[i] == 'stylesheet'){
			path += '&' + escape(arguments[i]) + '=' + arguments[i+1];
		}else{
			path += '&' + escape(arguments[i]) + '=' + escape(arguments[i+1]);
		}
	}
	return path;
}

function step(frm,type,next_cmd,next_step,url_success){

	var str="";
	var i,j;
	//Check
	if ( type == 'SELECT_AND_ANSWER' ){
		frm.stylesheet.value = "wizcase_selection_n_answer.xsl"
	}else if( type == 'ANSWER'  ){
		frm.stylesheet.value = "wizcase_answer.xsl"
	}else if( type == 'TOC'  ){
		frm.stylesheet.value = "wizcase_toc.xsl"
	}else if( type == 'INDEX'  ){
		frm.stylesheet.value = 'wizcase_index.xsl'
		frm.form_filename.value = 'index.xml'
	}else if (type == 'PREVIEW'){
		frm.stylesheet.value = 'wizcase_preview.xsl'
	}
	if ( next_cmd == 'prev_step' )
		frm.next_type.value = next_step

	if (getUrlParam('form_filename') != '')
		frm.form_filename.value = getUrlParam('form_filename');
	frm.cmd.value = next_cmd
	//frm.cmd.value = "next_step_xml";
	frm.method = "post"
	frm.action = wb_utils_disp_servlet_url
	frm.module.value = "wizcase.WizCaseModule"
	if ( url_success != null )
		frm.url_success.value = url_success
	n = frm.elements.length;
	
	if(frm.form_filename.value != 'toc.xml'){//toc.xml not need to using cookie
		cookie_name = frm.form_filename.value.substring(0,frm.form_filename.value.lastIndexOf('.')) + '_trial'
		if ( get_cookie(cookie_name) == 1 && frm.cmd.value != 'prev_step')
			frm.next_type.value = 'CHECK'
		if ( get_cookie(cookie_name) >= 2 && frm.cmd.value != 'prev_step')
			frm.next_type.value = 'ANSWER'
	}
	
	
	i=0;
	j=0;//form elements	
	k=0;//for line break
	var m = new Number;
	
	for(i=0;i<=paramName.length;i++){
		if(paramName[i] != ""){
			while(j<frm.elements.length){
				if(frm.elements[j].value == 'cmd')break;
				if(frm.elements[j].name == paramName[i]){
					if(lineBrk[k] == paramId[i]){
						str += paramId[i] + unescape("%10") + unescape("%10") + unescape("%10") + start_page + unescape("%11")
						if(eval("frm." + paramName[i] + ".type")){
						 str += paramId[i] + unescape("%10") + paramName[i] + unescape("%10") + frm.elements[j].value + unescape("%10") + unescape("%11")
						
						}else{
						if( eval("frm." + paramName[i] + ".length") > 0){
							m = 0;//loop radio btn
							while(m<eval("frm." + paramName[i] + ".length")){
								if(eval("frm." + paramName[i] + "[" + m + "].checked") == true){
									str += paramId[i] + unescape("%10") + paramName[i] + unescape("%10") + frm.elements[j].value + unescape("%10") + unescape("%11")
									break;
								}
								m++;
							}
							}
						}
						start_page += 1;
						k += 1;
					}else{
						if(eval("frm." + paramName[i] + ".type")){
						//	if ( eval("frm." + paramName[i] + ".length") == 0){
								str += paramId[i] + unescape("%10") + paramName[i] + unescape("%10") + frm.elements[j].value + unescape("%10") + unescape("%11")
						//	}
						}else{
						if ( eval("frm." + paramName[i] + ".length") > 0){
							m = 0;//loop radio btn
							while(m<eval("frm." + paramName[i] + ".length")){
								if(eval("frm." + paramName[i] + "[" + m + "].checked") == true){
									str += paramId[i] + unescape("%10") + paramName[i] + unescape("%10") + eval("frm." + paramName[i] + "[" + m + "].value") + unescape("%10") + unescape("%11")
									break;
								}else if(m == eval("frm." + paramName[i] + ".length")-1){
									str += paramId[i] + unescape("%10") + paramName[i] + unescape("%10") + unescape("%10") + unescape("%11")
								} 
								m++;
							}
						}
						
						
						}
					}
					break;
				}
				j++
			}
		}else{
			//alert(lineBrk[k] + paramId)
			if(lineBrk[k] == paramId[i]){
				//alert("lineBrk2!!!!" + paramId[i] + " : " + start_page)
				str += paramId[i] + unescape("%10") + unescape("%10") + unescape("%10") + start_page + unescape("%11")
				start_page += 1;
				k += 1;
			}else{
				str += paramId[i] + unescape("%10") + unescape("%10") + unescape("%10") + unescape("%11")
			}
					
			
		}
	}
	
	frm.params.value = str;
	
	//alert(frm.params.value);
	frm.submit();
}

function next_objective(frm,xml,type,cmd){
	
	if ( cmd != null )
		frm.cmd.value = cmd
	else
		frm.cmd.value = 'next_step'
		
	if ( type == 'SELECT_AND_ANSWER' ){
		frm.stylesheet.value = "wizcase_selection_n_answer.xsl"
		start_type = 'SELECTION'
	}
	else if( type == 'ANSWER'  ){
		frm.stylesheet.value = "wizcase_answer.xsl"
		start_type = 'INPUT'
	}
	else if( type == 'TOC'  ){
		frm.stylesheet.value = "wizcase_toc.xsl"
			start_type = 'INPUT'
	}
	else if( type == 'INDEX'  ){
		frm.stylesheet.value = 'wizcase_index.xsl'
			start_type = 'INPUT'
	}else if (type == 'PREVIEW'){
		frm.stylesheet.value = 'wizcase_preview.xsl'
			start_type = 'SELECTION'
	}
	cookie_name = xml.substring(0,xml.lastIndexOf('.')) + '_trial'
	if ( get_cookie(cookie_name) == 1 && frm.cmd.value != 'prev_step' && type !='SELECT_AND_ANSWER')
		start_type = 'CHECK'
	if ( get_cookie(cookie_name) >= 2 && frm.cmd.value != 'prev_step')
		start_type = 'ANSWER'		
	
	if ( frm.toc != null ){
			url= '../servlet/Dispatcher?module=wizcase.WizCaseModule&next_type=' + start_type + '&stylesheet=' + frm.stylesheet.value + '&form_filename=' + xml + '&cmd=' +frm.cmd.value +'&toc=' + frm.toc.value
	}else
		url= '../servlet/Dispatcher?module=wizcase.WizCaseModule&next_type=' + start_type + '&stylesheet=' + frm.stylesheet.value + '&form_filename=' + xml + '&cmd='+frm.cmd.value 	
	window.location.href = url
}

function next_objective_url(frm,xml,type,cmd){
	
	if ( cmd != null )
		frm.cmd.value = cmd
	else
		frm.cmd.value = 'next_step'
		
	if ( type == 'SELECT_AND_ANSWER' ){
		frm.stylesheet.value = "wizcase_selection_n_answer.xsl"
		start_type = 'SELECTION'
	}
	else if( type == 'ANSWER'  ){
		frm.stylesheet.value = "wizcase_answer.xsl"
		start_type = 'INPUT'
	}
	else if( type == 'TOC'  ){
		frm.stylesheet.value = "wizcase_toc.xsl"
			start_type = 'INPUT'
	}
	else if( type == 'INDEX'  ){
		frm.stylesheet.value = 'wizcase_index.xsl'
			start_type = 'INPUT'
	}else if (type == 'PREVIEW'){
		frm.stylesheet.value = 'wizcase_preview.xsl'
			start_type = 'SELECTION'
	}
	cookie_name = xml.substring(0,xml.lastIndexOf('.')) + '_trial'
	if ( get_cookie(cookie_name) == 1 && frm.cmd.value != 'prev_step' && type !='SELECT_AND_ANSWER')
		start_type = 'CHECK'
	if ( get_cookie(cookie_name) >= 2 && frm.cmd.value != 'prev_step')
		start_type = 'ANSWER'		
	
	if ( frm.toc != null ){
			url= '../servlet/Dispatcher?module=wizcase.WizCaseModule&next_type=' + start_type + '&stylesheet=' + frm.stylesheet.value + '&form_filename=' + xml + '&cmd=' +frm.cmd.value +'&toc=' + frm.toc.value
	}else
		url= '../servlet/Dispatcher?module=wizcase.WizCaseModule&next_type=' + start_type + '&stylesheet=' + frm.stylesheet.value + '&form_filename=' + xml + '&cmd='+frm.cmd.value 	
	return url
}

function wizcase_selection_n_answer(frm,type,next_cmd,cur_type){
	if ( type == 'SELECT_AND_ANSWER' )
		frm.stylesheet.value = "wizcase_selection_n_answer.xsl"
	else if( type == 'ANSWER'  )
		frm.stylesheet.value = "wizcase_answer.xsl"
	else if( type == 'TOC'  )
		frm.stylesheet.value = "wizcase_toc.xsl"
	else if( type == 'INDEX'  )
		frm.stylesheet.value = 'wizcase_index.xsl'
	else if (type == 'PREVIEW')
		frm.stylesheet.value = 'wizcase_preview.xsl'
		
	str = '';
	//frm.form_filename.value = getUrlParam('form_filename');
	frm.cmd.value = next_cmd
	//frm.cmd.value = "next_step_xml";
	frm.method = "post"
	frm.action = wb_utils_disp_servlet_url
	frm.module.value = "wizcase.WizCaseModule"
	
	
	n = frm.elements.length;
	k=1;//paramBlockID
	str= ''
	for(i=0;i<n;i++){
		if(frm.elements[i].checked){
				if(frm.elements[i].name == "cmd")break;
				for(j=i+1;j<n;j++){
					if(frm.elements[j].type == "checkbox" || frm.elements[j].name == "cmd")break;
					if(frm.elements[j].disable){
					}else if(frm.elements[j].type == "hidden"){
							id = frm.elements[i].name;
							id = id.substring(1,id.length);
							str += id + unescape("%10") + frm.elements[j].name + unescape("%10") + frm.elements[j].value +unescape("%10")+unescape("%11")
					}//end if
				}//end for loop
		}else if(frm.elements[i].disabled){
			id = frm.elements[i-1].value;
			str += id + unescape("%10") + frm.elements[i].name + unescape("%10") + frm.elements[i].value +unescape("%10")+unescape("%11")
			}//end for loop
	}
	//alert(str);
	frm.params.value = str;
	//alert(frm.params.value);
	frm.submit();
}
function getUrlParam(name) {
	strParam = window.location.search
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

function edit_word(frm){
	
	

	url= '../servlet/Dispatcher?module=wizcase.WizCaseModule&next_type=WORD&stylesheet=wizcase_word.xsl&form_filename=' + _get_form_filename(frm) + '&cmd=preview'	
	window.open(url, 'word','')
}


function _get_form_filename(frm){
	str = 'index.xml~toc.xml~'
	n = frm.elements.length;
	for ( i=0; i< n; i++){
		if ( frm.elements[i].name == 'toc' && frm.elements[i].checked == true ){
			str += frm.elements[i].value + "~";	
		}	
	}
	str = str.substring(0,str.length-1);
	//alert(str);
	return str;
}

// cookie functions
function set_cookie(token_nm, token_val) {

	cookie_nm = 'wizcase'
	expires = ''
	
	var re = new RegExp(_GEN_COOKIE_DELIMITER_ESCAPED + token_nm + "=" + "[^" + _GEN_COOKIE_DELIMITER_ESCAPED + "]*" + _GEN_COOKIE_DELIMITER_ESCAPED);		
	var cookie_val = gen_get_cookie(cookie_nm)	
	var token = token_nm + "=" + token_val;

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

function gen_set_cookie(name,value,expires,path,domain,secure) {
    document.cookie = name + "=" +escape(value) +
        ( (expires) ? ";expires=" + expires.toGMTString() : "") +
        ( (path) ? ";path=" + path : ";path=/") + 
        ( (domain) ? ";domain=" + domain : "") +
        ( (secure) ? ";secure" : "");
		
		
}

function get_cookie(token_nm) {
	cookie_nm='wizcase'
	
	var re = new RegExp(_GEN_COOKIE_DELIMITER_ESCAPED + token_nm + "=" + "[^" + _GEN_COOKIE_DELIMITER_ESCAPED + "]*" + _GEN_COOKIE_DELIMITER_ESCAPED);
	var re = new RegExp(_GEN_COOKIE_DELIMITER_ESCAPED + token_nm + "=" + "[^" + _GEN_COOKIE_DELIMITER_ESCAPED + "]*" + _GEN_COOKIE_DELIMITER_ESCAPED);		
	var cookie_val = gen_get_cookie(cookie_nm)	
	var token_val
	
	if (cookie_val.search(re) != -1) {
		var token = cookie_val.match(re)[0]
		token_val = token.substring(token_nm.length + 2, token.length-1)
	} else {
		token_val = ""
	}
	return token_val;
}

function gen_get_cookie(name) {
    var start = document.cookie.indexOf(name+"=");
    
    var len = start+name.length+1;
    if ((!start) && (name != document.cookie.substring(0,name.length))) return "";
   
    if (start == -1) return "";
    var end = document.cookie.indexOf(";",len);
    if (end == -1) end = document.cookie.length;
    return unescape(document.cookie.substring(len,end));
}

function gen_del_cookie(name,path,domain) {
    if (gen_get_cookie(name)) document.cookie = name + "=" +
        ( (path) ? ";path=" + path : ";path=/") +
        ( (domain) ? ";domain=" + domain : "") +
        ";expires=Thu, 01-Jan-70 00:00:01 GMT";
}

//-------------------------------------------------------------------------------
var CourseAPI = FindCourseTemplateAPI(window.parent.opener);

function FindCourseTemplateAPI(win) {
if (win){
	if (win.MyCourseTemplateAPI != null) {
		return win.MyCourseTemplateAPI;
	}
	else if (win.parent == null || win.parent == win) {
		return null;
	}
	else {
		return FindCourseTemplateAPI(win.parent);
	}
} else
	return null;
}