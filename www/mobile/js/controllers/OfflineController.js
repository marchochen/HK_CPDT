$.include([
    'lib/angular/angular-touch.min.js'
], '../../');

var alertObj;
var filter;
var AjaxObj;
var modalObj;
var video = null;
var duration = 0;
var modKey = "";
var durInterval;
var keyArray = new Array();

$(function(){
	var offlineModule = angular.module('module', ['globalCwn', 'ngTouch']);
	
	//获取已下载的离线学习模块
	offlineModule.controller('offlineController', ['$scope', '$translate', '$filter', 'Store', 'alertService', 'modalService', 
			function($scope, $translate, $filter, Store, alertService, modalService){
		filter = $filter;
		alertObj = alertService;
		modalObj = modalService;
		var userId;
		if(window.plus){
			plusReady();
		}else{
			document.addEventListener('plusready',plusReady,false);
		}
		function plusReady(){
			userId = plus.storage.getItem("loginUserId");
			var keyStr = plus.storage.getItem("modKey" + userId);
			var modArray = new Array();
			if(keyStr != null && keyStr != ""){
				keyArray = keyStr.split("[|]");
				for(var i=keyArray.length-1;i>=0;i--){
					modArray.push(JSON.parse(plus.storage.getItem(keyArray[i])));
				}
			}
			if(!$scope.$$phase){
				$scope.$apply(function(){
					$scope.modArray = modArray;
				});
			} else {
				$scope.modArray = modArray;
			}
			
			plus.key.addEventListener('backbutton',function(){
				plus.webview.currentWebview().hide('auto', 200);
			}, false);
		}
		
		$scope.startPlay = function(src, tkh_id, mod_id){
			startPlay('_downloads/' + userId + '/' + src, 'tkhId' + tkh_id + mod_id);
		}
		
		$scope.delete = function(src, tkh_id, mod_id){
			deleteFile('_downloads/' + userId + '/' + src, 'tkhId' + tkh_id + mod_id);
		}
		
		$scope.back = function(){
			plus.webview.currentWebview().hide('auto', 200);
		}
		
		//离线学习页面是缓存下来的，修改语言时需要动态修改，暴露出修改语言的方法，供外部webview调用
		registerChangeLanguageFunction($translate);
		
	}]);
	
	//进入各模块学习界面
	offlineModule.controller('videoController', ['Ajax', 'alertService', function(Ajax, alertService){
		AjaxObj = Ajax;
		alertObj = alertService;
	}]);
});

//开始学习
function startPlay(src, key){
	var w = plus.webview.create("mod_video.html","mod_video.html",{scrollIndicator:'none',scalable:true,bounce:"all"});
	w.addEventListener("loaded", function(){
		w.evalJS("init('" + src + "','" + key + "')");
	}, false);
	w.addEventListener("close", function(){
		w = null;
	}, false);
	w.show("slide-in-right", 300);
}

//删除文件
function deleteFile(src, modKey){
	modalObj.modal(function($scope, $modalInstance) {
		$scope.modalText = 'mod_delete_confirm';
		$scope.modalOk = function() {
			plus.io.resolveLocalFileSystemURL(src, function(entry){
				entry.remove(function(){
					var newArray = new Array();
					for(var i=0;i<keyArray.length;i++){
						if(keyArray[i] != modKey){
							newArray.push(keyArray[i]);
						}
					}
					keyArray = newArray;
					plus.storage.setItem("modKey" + plus.storage.getItem("loginUserId"), keyArray.join("[|]"));
					plus.storage.removeItem(modKey);
					$("#" + modKey).remove();
					changeNullPrompt('downloaded');
				}, function(){
					alertObj.add('danger', 'mod_delete_fail');
				})
			}, function(){
				alertObj.add('danger', 'mod_find_error');
			});
			$modalInstance.close();
		};
		$scope.modalCancel = function() {
			$modalInstance.dismiss('cancel');
		};
	});
	return;
}

//创建下载任务
function createDownload(key, data){
	var mod = JSON.parse(data);
	if($("#"+key).html() == undefined){
		var url = serverHost + '/resource/' + mod.mod_id + '/' + mod.src;
		var required = filter('translate')('mod_must_learn', {value : mod.required_time});
		if(mod.required_time == 0){
			required = filter('translate')('mod_must_open');
		}
		var html = '<div id="' + key + '" class="noticebox"><div class="list"><div style="font-size:12px;min-height:75px;"><div class="videoImg" onclick="changeState(\''+ key + '\')">'
					+ '<img src="../../images/adv03.jpg"/><span class="tuwen-list-1-bg"><i id="state' + key + '" class="tuwen-list-2-png"></i></span></div>'
					+ '<div class="videoContent"><div class="cname">' + mod.modTitle + '</div><div class="cart">' + filter('translate')('mod_learn_unit') + ': ' 
					+ mod.itmTitle + '</div><div id="size_' + key + '" class="cart"><span>0</span><a class="deleteImg" onclick="cancelDownload(\''+ key + '\')"></a></div></div></div></div></div>';
		$("#downloading").prepend(html);
		changeNullPrompt('downloading');
		
		var userId = plus.storage.getItem("loginUserId");
		var dtask = plus.downloader.createDownload(url,{method:"GET"});
		dtask.addEventListener("statechanged", function(task,status){
			switch(task.state){
				case 0:;
				case 1:;
	    		case 2: // 开始
	    			if(task.state == 2)
	    				alertObj.add('danger', '"' + mod.modTitle + '"' + filter('translate')('mod_down_start'));
	    			break;
	    		case 3:	// 已接收到数据
	    			$("#size_" + key + " span").html(task.downloadedSize + '/' + task.totalSize);
	    			break;
	    		case 4:	// 下载完成
	    			plus.io.resolveLocalFileSystemURL('_downloads/', function(entry){
						entry.getDirectory(userId + '/', {create:true}, function(pEntry){
							plus.io.resolveLocalFileSystemURL('_downloads/' + mod.src, function(cEntry){
			    				cEntry.moveTo(pEntry, mod.src, function(){
			    					var keyArray = new Array();
					    			var modKey = plus.storage.getItem("modKey" + userId);
									if(modKey != null){
										keyArray.push(modKey);
									}
									keyArray.push(key);
					    			plus.storage.setItem(key, data);
									plus.storage.setItem("modKey" + userId, keyArray.join("[|]"));
					    			alertObj.add('danger', '"' + mod.modTitle + '"' + filter('translate')('mod_down_finish'));
					    			$("#" + key).remove();
					    			$("#downloaded").prepend(html);
					    			$("#size_" + key).html(filter('translate')('mod_learn_duration') + ': <span class="duration">00:00</span></span><a class="deleteImg"></a>');
					    			$("#" + key + " .videoImg").click(function(){
					    				startPlay('_downloads/' + userId + '/' + mod.src, key);
					    			});
					    			$("#" + key + " .deleteImg").click(function(){
					    				deleteFile('_downloads/' + userId + '/' + mod.src, key);
					    			});
					    			changeNullPrompt('downloading');
					    			changeNullPrompt('downloaded');
					    			document.getElementById("state" + key).className = "tuwen-list-1-png";
			    				});
			    			});
						});
					});
					break;
	    	}
		});
		document.getElementById(key).dtask = dtask;
		dtask.start();
	} else {
		alertObj.add('danger', 'mod_down_unagain');
	}
}

function changeNullPrompt(id){
	if($("#" + id + " .noticebox").length == 0){
		if($("#" + id + " .panel-list-boxdate").length == 0){
			$("#" + id).html('<div class="panel-list-boxdate"><div class="panel-list-nodate">' + filter('translate')('loader_no_data') +'</div></div>');
		} else {
			$("#" + id + " .panel-list-boxdate").show();
		}
	} else {
		$("#" + id + " .panel-list-boxdate").hide();
	}
}

//暂停或恢复下载
function changeState(id){
	var obj = document.getElementById(id);
	if(obj.dtask.state == 5){
		obj.dtask.resume();
		obj.dtask.state = 2;
		document.getElementById("state" + id).className = "tuwen-list-2-png";
	} else {
		obj.dtask.pause();
		obj.dtask.state = 5;
		document.getElementById("state" + id).className = "tuwen-list-3-png";
	}
}

//取消下载
function cancelDownload(id){
	var obj = document.getElementById(id);
	obj.dtask.abort();
	$("#" + id).remove();
}

//改变相应模块的已学习时长
function changeDuration(modKey, totalDuration){
	$("#" + modKey +" .duration").html(filter('formatTimeRemain')(totalDuration));
}

//学习页面初始化设置
function init(src, key){
	video = document.getElementById("video");
	if(window.plus){
		loadMedia(src, key);
	} else {
		document.addEventListener('plusready',loadMedia(src, key),false);
	}
}

//读取学习文件
function loadMedia(src, key){
	plus.io.resolveLocalFileSystemURL(src, function(entry){
		changeBackButtion(function(){
			closeVideo();
		});
		video.src = entry.fullPath;
		modKey = key;
		var mod = JSON.parse(plus.storage.getItem(modKey));
		$("#mod_title").html(mod.modTitle);
		if(mod.playTime == undefined){
			mod.playTime = 0;
		}
		video.addEventListener('loadeddata',function(){
			video.currentTime = mod.playTime;
		},false);
		video.addEventListener('play',function(){
			durInterval = setInterval(function(){
				duration++;
			},1000);
		},false);
		video.addEventListener('pause',function(){
			clearInterval(durInterval);
		},false);
		video.play();
		plus.screen.unlockOrientation();
	}, function(){
		videoError();
	});
}

//播放错误处理
function videoError(e){
//	switch(e.target.error.code){
//		case e.target.error.MEDIA_ERR_ABORTED:
//     		alertObj.add('danger', 'You aborted the video playback.');
//     		break;
//   	case e.target.error.MEDIA_ERR_NETWORK:
//     		alertObj.add('danger', 'A network error caused the video download to fail part-way.');
//     		break;
//   	case e.target.error.MEDIA_ERR_DECODE:
//	       alertObj.add('danger', 'The video playback was aborted due to a corruption problem or because the video used features your browser did not support.');
//	       break;
//		case e.target.error.MEDIA_ERR_SRC_NOT_SUPPORTED:
//	       alertObj.add('danger', 'The video could not be loaded, either because the server or network failed or because the format is not supported.');
//	       break;
//		default:
//	       alertObj.add('danger', 'An unknown error occurred.');
//	       break;
//	}
	changeBackButtion(function(){
		back();
	});
	alertObj.add('danger', 'mod_find_error');
	setTimeout(function(){
		back();
	},3000);
}

//结束学习和上传学习记录
function closeVideo(){
	webview = plus.webview.currentWebview();
	var mod = JSON.parse(plus.storage.getItem(modKey));
	mod.playTime = video.currentTime;
	mod.totalDuration += duration;
	mod.duration += duration;
	mod.last_time = dateToStr(new Date());
	if(plus.networkinfo.getCurrentType() != 1){
		AjaxObj.post('/app/module/sendModuleTrack', mod, function(){
			mod.duration = 0;
			plus.storage.setItem(modKey, JSON.stringify(mod));
		});
	}
	plus.storage.setItem(modKey, JSON.stringify(mod));
	webview.opener().evalJS("changeDuration('" + modKey + "'," + mod.totalDuration + ")");
	webview.close();
	plus.screen.lockOrientation("portrait");
}

function toggleHead(){
	$("#vieoHead").fadeIn('fast');
	setTimeout(function(){
		$("#vieoHead").fadeOut('fast');
	},3200);
}

function changePhoto(){
	return false;
}