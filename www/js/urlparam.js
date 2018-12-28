// URL Parameter Manipulation 
function getUrl(name,win){
	if (name != null && name != '') {name = '&' + name;}
	if (win==null)
		strParam = window.location.search
	else
		strParam = eval(win+".location.search");
	idx = strParam.indexOf(name + "=")
	if (idx != -1)
		return true;
	else
		return false;	
}


function getUrlParam(name,win) {
	name = getSafeStringXSS(name);
	if (name != null && name != '') {name = '&' + name;}
	if (win==null) {strParam = window.location.search;}
	else {strParam = eval(win+".location.search");}
	
	if (strParam != null && strParam != '') {
		strParam = '&' + strParam.substring(1,strParam.length);
	}
	
	idx1 = strParam.indexOf(name + "=")	
	
	if (idx1 == -1)	{return "";}

	idx1 = idx1 + name.length + 1
	idx2 = strParam.indexOf("&", idx1)

	if (idx2 != -1) {len = idx2 - idx1;}
	else {len = strParam.length;}
	
	//return unescape(strParam.substr(idx1, len));
	return unescape(getSafeStringXSS(strParam.substr(idx1, len),name));
}	

function setUrlParam(name, value, url) {
	if (url) {
		idx0 = url.indexOf('?')
		if (idx0 == -1) {
			strParam = ""
		} else {
			strParam = url.substr(idx0,url.length)
		}
	} else {
		strParam = window.location.search
	}
		
	idx1 = strParam.indexOf(name + "=")
	if (idx1 == -1)	{
		if (strParam == "") {
			strParam = "?" + name + "=" + value
		} else {
			strParam = strParam + "&" + name + "=" + value
		}
	} else {
		idx1 = idx1 + name.length + 1
		idx2 = strParam.indexOf("&", idx1)
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

function getParentUrlParam(name) {
	strParam = window.parent.location.search
	if (strParam != null && strParam != '') {
		strParam = '&' + strParam.substring(1,strParam.length);
	}
	
	idx1 = strParam.indexOf(name + "=")
	if (idx1 == -1)	return ""

	idx1 = idx1 + name.length + 1
	idx2 = strParam.indexOf("&", idx1)

	if (idx2 != -1)
		len = idx2 - idx1
	else
		len = strParam.length

	//return unescape(strParam.substr(idx1, len))
	return unescape(getSafeStringXSS(strParam.substr(idx1, len),name))
	}
	
	function getSafeStringXSS(str, name){
	str = str.replace(/</g, "&lt;");
	str = str.replace(/>/g, "&gt;");
	str = str.replace(/"/g, "&#039;");
	str = str.replace(/'/g, "&quot;");
	str = str.replace(/ /g, "&nbsp;");
	str = str.replace(/onclick/g, "on click");
	str = str.replace(/onfocus/g, "on focus");
	str = str.replace(/alert/g, "a lert");
	if('&lang'==name || 'lang'==name){
		 if(str=='en'){
		 	str= 'en';
		 }else  if(str=='ch'){
		 	str= 'ch';
		 }else  if(str=='gb'){
		 	str= 'gb';
		 }else if(str=='en'){
		 	str= 'en';
		 }else if(str=='en-us'){
		 	str= 'en-us';
		 }else if(str=='zh-cn'){
		 	str= 'zh-cn';
		 }else if(str=='zh-hk'){
		 	str= 'zh-hk';
		 }else if(str=='ISO-8859-1'){
		 	str= 'ISO-8859-1';
		 }else if(str=='GB2312'){
		 	str= 'GB2312';
		 }else if(str=='Big5'){
		 	str= 'Big5';
		 }else{
		 	str= 'en';
		 }
	}
	return str;
}