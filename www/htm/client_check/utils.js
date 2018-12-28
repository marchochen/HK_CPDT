var StatusNG = 0;
var StatusUP = 1;
var StatusOK = 2;

var IdxBrowser = 0;
var IdxJs      = 1;
var IdxCookie  = 2;
var IdxPopup   = 3;
var IdxFlash   = 4;
var IdxJre     = 5;

var CekBrowser = 'BROWSER';
var CekJs      = 'JS';
var CekCookie  = 'COOKIE';
var CekPopup   = 'POPUP';
var CekFlash   = 'FLASH';
var CekJre   = 'JRE';

//cekItem for only check,if give
function showEachClientCheckResult(cekItem) {
    var checkRes = 0
    if(cekItem == null){
    	cekItem = '';
    }
    
    if(cekItem == '' || cekItem == CekBrowser){
	    checkRes = getBrowserStatus();
	    if (checkRes < StatusOK) {
	        document.getElementById("browser_").style.display = 'inline';
	        document.getElementById("browser_detail").style.display = 'inline';
	        document.getElementById("browser_check").innerHTML = Msg[IdxBrowser][checkRes];
	        return;//not continue;
	    }
	}
	if(cekItem == '' ||  cekItem == CekCookie){
	    checkRes = getCookieStatus();
        if (checkRes < StatusOK) {
            document.getElementById("cookies_").style.display = 'inline';
            document.getElementById("cookies_detail").style.display = 'inline';
            document.getElementById("cookie_check").innerHTML = Msg[IdxCookie][checkRes];
            return;//not continue;
        }
	}
	
	if(cekItem == '' ||  cekItem == CekFlash){
	    checkRes = getFlashStatus();
        if (checkRes < StatusOK) {
            document.getElementById("flash_").style.display = 'inline';
            document.getElementById("flash_detail").style.display = 'inline';
            document.getElementById("flash_check").innerHTML = Msg[IdxFlash][checkRes];
            return;//not continue;
        }
	}
	
	if(cekItem == '' ||  cekItem == CekJre){
	    /*checkRes = getJreStatus();
	    if (checkRes < StatusOK) {
            document.getElementById("jre_").style.display = 'inline';
            document.getElementById("jre_detail").style.display = 'inline';
            document.getElementById("jre_check").innerHTML = Msg[IdxJre][checkRes];
            return;//not continue;
        }*/
	}
	
	// do not include this during check-all
	if(cekItem == CekPopup){
		checkRes = getPopupStatus("../../close_window.htm");
        if (checkRes < StatusOK) {
            document.getElementById("popup_").style.display = 'inline';
            document.getElementById("popup_detail").style.display = 'inline';
            document.getElementById("popup_check").innerHTML = Msg[IdxPopup][checkRes];
            return;//not continue;
        }
	}
	
	//if all pass
	document.getElementById("all_ok").style.display = 'inline';
}

function showOverallClientCheckResult(inMsg, cekItem) {
	if(cekItem == null){
		cekItem = '';
	}
    if (((cekItem == '' || cekItem == CekBrowser) && getBrowserStatus() < StatusOK)
     || ((cekItem == '' || cekItem == CekCookie) && getCookieStatus() < StatusOK)
     || (cekItem == CekPopup && getPopupStatus("../htm/close_window.htm") < StatusOK) // do not include this during check-all
     || ((cekItem == '' || cekItem == CekFlash) && getFlashStatus() < StatusOK)
	 || ((cekItem == '' || cekItem == CekJre) && getJreStatus() < StatusOK)
    ) {
        document.getElementById("client_check_link").innerHTML = inMsg;
        document.getElementById("client_check_td_above").height = 20;
        document.getElementById("client_check_td_below").height = 20;
    }
}

function getBrowserStatus() {
//    var status = StatusNG;
//    if (!checkBrowserName(" MSIE ") || !checkBrowserName("MSIE ") || !checkBrowserName("MSIE")) {
//        status = StatusNG;
//    } else {
//        status = StatusOK;
//    }
    return StatusOK;
}

function checkBrowserName(name) {
    var verStr = navigator.appVersion;
    var verNo = 0;
    var result = false;
    if (verStr.indexOf(name) != -1) {
        tempStr = verStr.split(name);
        verNo = parseFloat(tempStr[1]);
        if (verNo >= 6) {
            result = true;
        }
    }
    return result;
}

function getCookieStatus() {
    var status = StatusNG;
    var cookieStr = "wb_check=kcehc_bw";
    document.cookie = cookieStr;
    if (document.cookie.indexOf(cookieStr) > -1) {
        status = StatusOK;
        var date = new Date();
        date.setTime(date.getTime() - 1000);
        document.cookie = cookieStr + "; expires=" + date.toGMTString()+ ((document.location.protocol == 'https:') ? ';secure' : '');
    }
    return status;
}

function getPopupStatus(winUrl) {
    var status = StatusNG;
    var str_feature = 'toolbar=no'
    + ',menubar=no'
    + ',scrollbars=no'
    + ',resizable=no'
    + ',status=no'
    + ',width=1'
    + ',height=1'
    + ',top=0'
    + ',left=0'
    + ',screenX=0'
    + ',screenY=0';
    var popup_win = window.open(winUrl, "wb_check", str_feature);
    if (popup_win) {
        status = StatusOK;
    }
    return status;
}

function getFlashStatus() {
//    var MinVer = 7;
//    var status = StatusNG;
//    if (navigator.plugins && navigator.plugins.length && navigator.plugins.length > 0) {
//        var flashObj = navigator.plugins["Shockwave Flash"];
//        if (flashObj && flashObj.length && flashObj.length > 0) {
//            var flashMimeObj = flashObj["application/x-shockwave-flash"];
//            if (flashMimeObj) {
//                var tempStr = flashObj.description.split(" Flash ");
//                var verNo = parseFloat(tempStr[1]);
//                if (verNo >= MinVer) {
//                    status = StatusOK;
//                } else {
//                    status = StatusUP;
//                }
//            }
//        }
//    }
//    if (status == StatusNG) {
//        for (var i = MinVer; i > 0; i--) {
//            try {
//                var flashObj = new ActiveXObject("ShockwaveFlash.ShockwaveFlash." + i);
//                if (i == MinVer) {
//                    status = StatusOK;
//                } else {
//                    status = StatusUP;
//                }
//                break;
//            } catch(e) {
//                status = StatusNG;
//            }
//        }
//    }
    return StatusOK;
}

function getJreStatus() {
//    var status = StatusNG;
//    var jres = deployJava.getJREs();
//    for(var i = 0; i < jres.length; i++){
//        var jTem = jres[i].substring(0,3);
//        if(jTem >= 1.5){
//            status = StatusOK;
//            break;
//        }
//    }
    return StatusOK;
}
