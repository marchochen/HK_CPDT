/* 获取Cookie值 */
function getCookie(c_name) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=");
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1;
			c_end = document.cookie.indexOf(";", c_start);
			if (c_end == -1)
				c_end = document.cookie.length;
			return unescape(document.cookie.substring(c_start, c_end));
		}
	}
	return ""
}
/* 设置Cookie值 */
function setCookie(c_name, value, expiredays, path) {
	var exdate = new Date();
	
	exdate.setTime(exdate.getTime() + expiredays*24*60*60*1000);
	if(path != null && path != undefined){
		if(expiredays == null || expiredays == "" || expiredays == undefined){
			document.cookie = c_name + "="+ escape (value) + ";path=" + path;
		}else{
			document.cookie = c_name + "="+ escape (value) + ";expires=" + exdate.toGMTString() + ";path=" + path;
		}
	}else{
		document.cookie = c_name + "=" + escape(value);
		+ ((expiredays == null || expiredays == undefined) ? "" : ";expires=" + exdate.toGMTString());
	}
}

/* 检查Cookie，如果没有，则提示设置，如果有了，则弹其Cookie值 */
function checkCookie() {
	username = getCookie('username');
	if (username != null && username != "") {
		alert('Welcome again ' + username + '!')
	} else {
		username = prompt('Please enter your name:', "")
		if (username != null && username != "") {
			setCookie('username', username, 365);
		}
	}
}