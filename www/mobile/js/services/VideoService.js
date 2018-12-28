var viewCourseId = '';
var viewUserId = '';
var courseNum = '';
var courseInfo = "";
var viewTime = "";
var currCourse = "";
var player;
var isChange = false;
var current_time = "";

var init = false;

appModule.service('wizVideo', ['Store',function(Store){
	return {
		setWizVideo : videoCwn
	};
	
	function videoCwn(id){
		
		var total_ss = 0;
		var t = 0;
		
		if(player == undefined){
			player = videojs(id);
		}	
		
		//Store里面保存的时间
		courseInfo = getObj("open"+viewCourseId+viewUserId+courseNum)!=null?getObj("open"+viewCourseId+viewUserId+courseNum):'';
		init = true;
	
		
		// 检测播放时间
		player.on('timeupdate', function () {
			if(!isChange){ //切换视频时设置为TRUE。不让他设置
				playHandler();
			}
		});
		// 开始或恢复播放
		player.on('play', function() {
			
			isChange = false;
			
			if(init){
				init = false;
			}else{
				courseInfo = getObj("open"+viewCourseId+viewUserId+courseNum)!=null?getObj("open"+viewCourseId+viewUserId+courseNum):'';
			}
			
			if(courseInfo != ""){
				total_ss = timeToSecond(courseInfo.viewTime);
				
				if(total_ss >= 0){
					player.currentTime(total_ss);
				}
			}
		});
		// 暂停播放
		player.on('pause', function() {  
			playHandler();
		});
		
		player.on('playing',function() {
			if(appInd){
				if(total_ss != t){
					t = total_ss;
					player.currentTime(total_ss);
				}
			}
		});
		
		document.addEventListener('pause', function(){
   			
   			player.pause();
   			
   		});
		
	}
	
	function playHandler(){
		//取到当前 //获取播放进度
		viewTime = player.currentTime();
		viewTime = parseInt(viewTime);
		
		current_time = formatTime(viewTime);//视频观看时间
		
		if(viewTime%1==0){
			setObj();
		}
		
	}
	
	function setObj(){
		var currTime = formatTime(viewTime);
		var obj={
				courseuserid : viewCourseId + viewUserId,
				viewTime : currTime
		};
		setStore(obj);
	}
	
	function getObj(str){
		var obj = strToJson(getStore(str));
		return obj;
	}
	
	function getStore(name){
		var j = Store.get(name);
		if(j != null && j != ""){
			return j;
		}else{
			return null;
		}
	}
	
	function setStore(option){
		var d= new Date();
		d.setHours(d.getHours() + (24 * 30)); //保存一个月
		var json=jsonToString(option);
		currCourse = "open"+viewCourseId + viewUserId+courseNum;
		
		Store.set(currCourse,json);
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
	
	
}]);
