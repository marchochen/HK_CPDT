var viewCourseId = '';
var viewUserId = '';
var courseNum = '';
var courseInfo = "";
var viewTime = "";
var currCourse = "";
var player;
var error_flag = true;

function videoCwn(id){
	
	if(player == undefined){
		player = videojs(id);
	}
	
	//cookie里面保存的时间
	courseInfo = (getObj("open"+viewCourseId+viewUserId+courseNum)!=null?getObj("open"+viewCourseId+viewUserId+courseNum):'');
	
	if(courseInfo != ""){
		var total_s = timeToSecond(courseInfo.viewTime);
//		player.currentTime(total_s);
	}
	
	// 检测播放时间
	player.on('timeupdate', function () { 
		if(error_flag){
			playHandler();
		}
	});
	
	// 开始或恢复播放
	player.on('play', function() {
		if(error_flag){
			playHandler();
		}
	});
	
	// 暂停播放
	player.on('pause', function() {  
		if(error_flag){
			playHandler();
		}
	});
	
	player.on('error', function() {  
		error_flag = false;
		//视频出错，清空掉断点续播的记录    
    	var obj={
			courseuserid : viewCourseId + viewUserId,
			viewTime : formatTime(0)
    	};
    	setCookie(obj);
	});
	
	window.setTimeout(function(){
		if(courseInfo != ""){
			var total_s = timeToSecond(courseInfo.viewTime);
			player.currentTime(total_s);
		}
	},500);
	
	
	
	return player;
}

function setObj(){
	var currTime = formatTime(viewTime);
	var obj={
		courseuserid : viewCourseId + viewUserId,
       	viewTime : currTime
	};
	setCookie(obj);
}

function getObj(str){
   var obj = strToJson(getCookie(str));
   return obj;
}

function playHandler(){
	//取到当前 //获取播放进度
	viewTime = player.currentTime();
	viewTime = parseInt(viewTime);
	
	//传入iframe 里面
	window.parent.current_time = formatTime(viewTime);
	
	if(viewTime%2==0){
		setObj();
	}

}

function getCookie(name){
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null){
     return unescape(arr[2]);
    }else{
     return null;
    }
}

function formatTime(second) {
	return [parseInt(second / 60 / 60), parseInt(second / 60 % 60), second % 60].join(":")
	.replace(/\b(\d)\b/g, "0$1");
}

function timeToSecond(time){
	var h = time.split(":")[0];
	var m = time.split(":")[1];
	var s = time.split(":")[2];
	var second = parseInt(h) * 60 * 60 + parseInt(m) * 60 + parseInt(s);
	return second;
}

function setCookie(option){
	var d= new Date();
	d.setHours(d.getHours() + (24 * 30)); //保存一个月
	var json=jsonToString(option);
	currCourse = "open"+viewCourseId + viewUserId+courseNum;
    document.cookie =currCourse+'='+json+'; expires='+d.toGMTString();
}

function jsonToString(obj){
	var str='{';
	if(obj!=null){
	for(var key in obj){
	str=str+'"'+key+'"'+':'+'"'+obj[key]+'",';
	}
	str=str.substring(0,str.length-1);
	}
	str+='}';
	return str;
}

function  strToJson(str){
	return eval('('+str+')');
}