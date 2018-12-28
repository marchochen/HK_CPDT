﻿(function(w){
// 空函数
function shield(){
	return false;
}
//document.addEventListener('touchstart',shield,false);//取消浏览器的所有事件，使得active的样式在手机上正常生效
//document.oncontextmenu=shield;//屏蔽选择函数
// H5 plus事件处理
var ws=null,as='slide-in-right',at=200;
function plusReady(){
	ws=plus.webview.currentWebview();
	// Android处理返回键
	plus.key.addEventListener('backbutton',function(){
		back();
	},false);
	
}
if(w.plus){
	plusReady();
}else{
	document.addEventListener('plusready',plusReady,false);
}

w.callNative = function(func){
	if(w.plus){
		func();
	}else{
		document.addEventListener('plusready',func,false);
	}
}

// DOMContentLoaded事件处理
var domready=false;
document.addEventListener('DOMContentLoaded',function(){
	domready=true;
	gInit();
	document.body.onselectstart=shield;
	compatibleAdjust();
},false);
// 处理返回事件
w.back=function(index){
	if(w.plus){
		if(index != undefined){
			for(var i=0;i<index-1;i++){
				if('iOS'==plus.os.name){
					ws = plus.webview.all()[plus.webview.all().length-2-i];
				} else {
					ws = plus.webview.all()[plus.webview.all().length-2];
				}
				ws.close('auto',at);
				ws = null;
			}
		}
		ws||(ws=plus.webview.currentWebview());
		if(ws.children().length > 0){
			for(var i=0;i<ws.children().length;i++){
				ws.children()[i].close();
			}
		}
		
		//android 4.2bug：关闭webView后，webView中的vedio音频还在响，所以需要手动关闭
		var vedios = document.getElementsByTagName("video");
		if(vedios && vedios.length > 0){
			for(var i=0;i<vedios.length;i++){
				var vedio = vedios[i];
				vedio.pause();
			}
		}
		
		var aicc_cos = plus.storage.getItem("aicc_cos");
		var parent_url = plus.storage.getItem("parent_url");
		if(aicc_cos = "aicc_cos" && parent_url != null) {
			w.aicc_back(parent_url);
		} else {
			ws.close('auto',at);
		}
	}else if(history.length>1){
		history.go(-(index != undefined?index:1));
	}else{
		w.close();
	}
};
//退出登录，返回登录页面
w.backLogin=function(rp){
	if(appInd){
		var webViews = plus.webview.all() || [];
		var cs = plus.webview.currentWebview();
		var webviewLength = webViews.length;
		
		for(var i=0;i<webviewLength;i++){
			var webView = webViews[i];
			
			if(webView != cs){
				webView.close();
				webView = null;
			}
		}
	}
	
	window.location.href = rp+"views/login.html";
};
/**
 * 改变返回按钮的事件
 * @param {function} buttonEvent : 按钮事件
 */
w.changeBackButtion=function(buttonEvent){
	if(w.plus){
		changeBackBtn();
	}else{
		document.addEventListener('plusready',changeBackBtn,false);
	}
	function changeBackBtn(){
		plus.key.removeEventListener('backbutton',function(){},false);
		plus.key.addEventListener('backbutton',function(){
			buttonEvent(this);
		},false);
	}
};
//退出程序
var exitInd = false;
w.exitProgram=function(alertService){
	if(exitInd){
		plus.runtime.quit();
	}
	alertService.add('danger', 'exit_prompt');
	exitInd = true;
	setTimeout(function(){
		exitInd = false;
	},3000);
};
// 处理点击事件
var openw=null,waiting=null;
/**
 * 打开新窗口
 * @param {URIString} url : 要打开页面url
 * @param {boolean} wa : 是否显示等待框
 * @param {String} id : 窗口标记
 * @param {boolean} hardwareAccelerated : 是否启用硬件加速
 */
w.clicked=function(url,wa,id,hardwareAccelerated){
	if(openw){//避免多次打开同一个页面
		return null;
	}
	if(w.plus){
		var webViewStyle = {'popGesture':'none','scrollIndicator':'none','scalable':false};
		if(hardwareAccelerated){
			webViewStyle.hardwareAccelerated = true;
		}
		wa&&(waiting=plus.nativeUI.showWaiting());
		if(id == undefined || id == ''){
			id = url;
		}
		openw=plus.webview.create(url,id,webViewStyle);
		//		页面加载完成后才显示
		openw.addEventListener('titleUpdate',function(){
		//	setTimeout(function(){//延后显示可避免低端机上动画时白屏
			//判断打开的页面和所要的页面url是否相同，防止打开页面不是所需页面
			if(openw != null && openw.getURL() != null && openw.getURL().indexOf("file:") == 0 && openw.getURL().indexOf(url.substring(0,url.indexOf('?')).replace(/\.\.\//g, '')) < 0){
				
				openw.evalJS("window.location.href='" + url + "'");
			} else {
				openw = null;
				closeWaiting();
			}
		//	},200);
		},false);
		openw.addEventListener('close',function(){//页面关闭后可再次打开
			openw=null;
		},false);
		if(openw != null){
			openw.show(as,at);
		}
		return openw;
	}else{
		w.location.href = url;
	}
	return null;
};
/**
 * 关闭等待框
 */
w.closeWaiting=function(){
	waiting&&waiting.close();
	waiting=null;
}
// 兼容性样式调整
var adjust=false;
function compatibleAdjust(){
	if(adjust||!w.plus||!domready){
		return;
	}	// iOS平台使用滚动的div
	if('iOS'==plus.os.name){
		as='pop-in';
		at=300;
		var t=document.getElementById("dcontent");
		t&&(t.className="sdcontent");
		t=document.getElementById("content");
		t&&(t.className="scontent");
		//iOS8横竖屏切换div不更新滚动问题
		var lasto=window.orientation;
		window.addEventListener("orientationchange",function(){
			var nowo=window.orientation;
			if(lasto!=nowo&&(90==nowo||-90==nowo)){
				dcontent&&(0==dcontent.scrollTop)&&(dcontent.scrollTop=1);
			}
			lasto=nowo;
		},false);
	}
	adjust=true;
};
// 通用元素对象
var dcontent=null;
w.gInit=function(){
	dcontent=document.getElementById("dcontent");
};
// 格式化时长字符串，格式为"hh:mm:ss"
w.timeToStr=function(ts){
	if(isNaN(ts)){
		return "--:--:--";
	}
	var h=parseInt(ts/3600);
	var m=parseInt((ts%3600)/60);
	var s=parseInt(ts%60);
	return (ultZeroize(h)+":"+ultZeroize(m)+":"+ultZeroize(s));
};
// 格式化日期时间字符串，格式为"yyyy-MM-dd hh:mm:ss"
w.dateToStr=function(d){
	return (d.getFullYear()+"-"+ultZeroize(d.getMonth()+1)+"-"+ultZeroize(d.getDate())+" "+ultZeroize(d.getHours())+":"+ultZeroize(d.getMinutes())+":"+ultZeroize(d.getSeconds()));
};
/**
 * zeroize value with length(default is 2).
 * @param {Object} v
 * @param {Number} l
 * @return {String}
 */
w.ultZeroize=function(v,l){
	var z="";
	l=l||2;
	v=String(v);
	for(var i=0;i<l-v.length;i++){
		z+="0";
	}
	return z+v;
};
/**
 * 改变总数
 * @param {String} id : 标签标记
 * @param {String} type : 加还是减
 * @param {int} num : 变化数
 */
w.changeTotal=function(id, type, num){
	num = num != undefined?num:1;
	if(type == 'add'){
		$("#" + id).html(parseInt($("#" + id).html()) + parseInt(num));
	} else {
		$("#" + id).html(parseInt($("#" + id).html()) - parseInt(num));
	}
};
/**
 * 更新头像
 * @param {String} path : 图片路径
 */
w.changePhoto=function(path){
	var els = document.querySelectorAll('#user_photo_' + plus.storage.getItem('loginUserId'));
	if(els && els.length>0){
		for(var i=0;i<els.length;i++){
			els[i].src = path;
		}
	}
};
/**
 * 刷新窗口信息
 * @param {String} wsId : 窗口标记
 * @param {long} index : 返回上几个窗口（默认上一个,值为0就不执行返回）
 * @param {boolean} : 是否隐藏loading，默认是显示的
 */
w.changeWebviewDetail=function(wsId, callback ,hideWaiting){
	var rws = plus.webview.getWebviewById(wsId);
	var wa;
	if(rws != undefined){
		if(!hideWaiting){
			wa = plus.nativeUI.showWaiting();
		}
		rws.addEventListener('loaded',function(){
			if(callback){
				if(window.plus){
					if(wa){
						wa.close();
					}
					callback(this);
				}else{
					document.addEventListener('plusready',function(){
						if(wa){
							wa.close();
						}
						callback(this);
					},false);
				}
			} else {
				if(wa){
					wa.close();
				}
			}
		},false);
		rws.reload();
	} else {
		if(callback){
			callback(this);
		}
	}
};
//打开其他网站的url @see /wizbank/src/com/cwn/wizbank/web/WzbMappingJackson2HttpMessageConverter.java
w.openOtherSiteUrl = function(url){
	var protocol = serverHost.substring(0,serverHost.indexOf("://"));
	if(!protocol){
		protocol = window.location.protocol.substring(0,window.location.protocol.indexOf(":"));
	}
	if(url.indexOf("/") == 0){
		url = protocol + "://" + window.location.host + url;
	} else if(url.indexOf("http://") < 0 && url.indexOf("https://") < 0) {
		url = protocol + "://" + url;
	}
	if(appInd){
		plus.runtime.openURL(url);
	} else {
		w.open(url);
	}
};

w.webViewOpenOtherSiteUrl = function(url, parent_url, back_btn, screen_type) {
	var protocol = serverHost.substring(0,serverHost.indexOf("://"));
	if(!protocol){
		protocol = window.location.protocol.substring(0,window.location.protocol.indexOf(":"));
	}
	if(url.indexOf("/") == 0){
		url = protocol + "://" + window.location.host + url;
	} else if(url.indexOf("http://") < 0 && url.indexOf("https://") < 0) {
		url = protocol + "://" + url;
	}
	// 创建webview
	if(appInd){
		if("only" == screen_type) {		// 绑定竖屏
			plus.screen.lockOrientation("portrait");
		} else if("many" == screen_type) {	// 绑定横屏
			plus.screen.lockOrientation("landscape");
		}
		plus.storage.setItem("aicc_cos", "aicc_cos");
		plus.storage.setItem("parent_url", parent_url);
		var webViewStyle = {
			'popGesture':'none',
			'scrollIndicator':'none',
			'scalable':false,
			titleNView: {
				backgroundColor: '#f88600',
				'titleColor' : '#ffffff',
				autoBackButton:true,
				'titleText':'',
				buttons: [{ //后退按钮
					'float': 'left',
					'fontSize' : '14.6px',
					text: back_btn,
					onclick: this.aicc_back.bind(this,parent_url)
				}]
			}
		};
		webview = plus.webview.create(url, 'browser', webViewStyle);
		waiting = plus.nativeUI.showWaiting();
		webview.addEventListener('titleUpdate', function(){
			closeWaiting();
			webview.show();
		});
	} else {
		w.open(url);
	}
};

// 后退
w.aicc_back = function(url) {
	w.unlockOrientation();
	waiting = plus.nativeUI.showWaiting();
	webViewStyle = {'popGesture':'none','scrollIndicator':'none', 'scalable':false};
	webviewShow = plus.webview.create(url, '', webViewStyle);
	webviewShow.addEventListener("loaded", function() {
		plus.storage.removeItem("aicc_cos");
		plus.storage.removeItem("parent_url");
		closeWaiting();
		this.close();
		this.back();
		webviewShow.show("slide-in-right", 0);
	}, false);
};

// 关闭
w.close = function() {
	this.webview.hide('slide-out-right');
	this.webview.clear();
};

//解除屏幕锁定
w.unlockOrientation = function(){
	if(w.plus){
		plus.screen.unlockOrientation();
	}else{
		document.addEventListener('plusready',function(){
			plus.screen.unlockOrientation();
		},false);
	}
};
//锁定横屏
w.landscape = function(){
	if(w.plus){
		plus.screen.lockOrientation("landscape");
	}else{
		document.addEventListener('plusready',function(){
			plus.screen.lockOrientation("landscape");
		},false);
	}
}
//上传文件
var wait = null;
w.upload = function(id){
	if(id == undefined || id == '' || id == null){
		id = 'file';
	}
	var uploadType = $('.cont-review-tool a:eq(0)').attr('data');
  	var value = document.getElementById(id).value;
  	//如果上传的是图片就做文件格式验证
  	if(uploadType == 'Img'){
		var fileTypes = ['JPG', 'GIF', 'PNG', 'JPEG', 'jpeg', 'jpg', 'gif', 'png'];
      	var type = value.substring(value.lastIndexOf('.') + 1);
      	var flag = false;
      	for(var i in fileTypes){
          	if(type == fileTypes[i]){
              	flag = true;
              	break;
          	}
      	}
      	if(!flag){
      		alertObj.add('danger', 'img_type_limit', 2000);
          	return;
      	}
  	}

	var option = {
		file : document.getElementById(id).files[0],
		url : uploadUrl + uploadType,
		success : function(data){
			if(data){
   				var image = imageTpl.replace(/\{file\}/g, id).replace(/\{index\}/g, fileIndex++).replace(/\{id\}/g, data.tmf.mtf_id);
           		if(id != "file"){
           			$('#'+id+'_box').prepend(image);
           		}else{
           			$('.cont-review-info').prepend(image);
           		}
      			if( data.tmf.mtf_type == 'Img'){
                    showPhoto(id + (fileIndex-1), data.fullPath,id);
				}
			}
		},
		showProgress : true
	};
	uploadFile(option);
};
//显示图片
w.showPhoto = function(val, fullPath,id){
	if(id == undefined || id == '' || id == null){
		id = 'file';
	}
	if(appInd){
		document.getElementById(val).src = serverHost + '/' + fullPath.replace(/\\/g, '/');
    } else {
		var reader = new FileReader();
		reader.onload = function (e){
			document.getElementById(val).src = e.target.result;
		};
		reader.readAsDataURL(document.getElementById(id).files[0]);
	}
};
//加载文件
w.loadFile = function(val, path,id){
	if(id == undefined || id == '' || id == null){
		id = 'file';
	}
	var url,name;
    if(val.mtf_file_type == 'url'){
    	url = val.mtf_url;
      	name = val.mtf_url;
 	} else {
      	url = serverHost + "/" + path + "/" + val.mtf_file_rename;
      	name = val.mtf_file_name;
	}
	if(name == '' || name == undefined || url == undefined || url == ''){
      	return;
  	}
   	var image = imageTpl.replace(/\{file\}/g, id).replace(/\{index\}/g, fileIndex++).replace(/\{id\}/g, val.mtf_id);
 	if(val.mtf_type == 'Img'){
 		if(id != "file"){
   			$('#'+id+'_box').prepend(image);
   			$('#'+id+'_box img:eq(0)').attr('src', url);
   		}else{
   			$('.cont-review-info').prepend(image);
   			$('.cont-review-info img:eq(0)').attr('src', url);
   		}
   	} else {
      	$('.cont-review-info').prepend(image);
    }
};
//删除文件
w.deleteFile = function(obj, id){
	$.ajax({
     	url : serverHost + '/app/upload/del/'+ id ,
       	type: "GET",
      	dataType: 'jsonp',
       	async : true,
      	jsonp: 'callback',
      	data : {
           	token : token,
            developer : 'mobile'
       	},
       	beforeSend: function(request) {
         	request.setRequestHeader("Accept", "application/javascript");
      	},
      	success : function(){
          	$(obj).parent().remove();
     	}
 	});
};
//删除所有图片
w.deleteAllImg = function(){
	$('.cont-review-info .rewiew-tool').remove();
};
//创建嵌套页面
w.createWebviewEmbed = function(url){
	var ws = plus.webview.currentWebview();
	var embed = plus.webview.create(url, "embed", {top:"46px",bottom:"0px"});
	ws.append(embed);
	
	return embed;
	
};

//公共js中，获取相对于移动端根文件夹【mobile】下面文件的路径前缀
w.getRelatievePathPrefix = function(){
	var url = window.location.href;
	
	if(url.indexOf("views/") == -1){
		return "views/";
	}
	
	var subStr = url.substring(url.indexOf("views/"));
	var tempArr = subStr.split("/");
	var len = tempArr.length - 2;

	var result = "";

	for(var i = 0;i<len;i++){
	   result += "../";
	}
	
	return result + "../";
};


/**
 * 注册改变语言的方法，抽出此方法，是为了显式的传递【$translate】参数，而不直接使用window.$translate
 */
w.registerChangeLanguageFunction = function($translate){
    this.changeLan = function(lan){
        $translate.use(lan);
    }
};

	//加载icon
	$(function () { 
		var iconURL = '../../images/favicon.ico',
	    linkTag = $('<link rel="icon" href="' + iconURL + '" rel="stylesheet" type="image/x-icon"' +'" />');
		//动态将link标签添加到head里   
		$($('head')[0]).append(linkTag);
	});

})(window);