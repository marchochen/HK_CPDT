var alertObj;

$.include([
    'lib/angular/angular-sanitize.min.js',
    'js/directives/CatalogDirective.js',
    'js/directives/SNSDirectives.js',
    'js/directives/CommentDirective.js',
    'js/services/SNSService.js',
    'lib/videogular/videogular.js',
    'lib/videogular/vg-controls.js',
    'lib/videogular/vg-overlay-play.js',
    'js/services/VideoService.js',
    'lib/videogular/vg-poster.js'
], '../../');
$(function(){
    var openModule = angular.module('openCourse', ['globalCwn', 'CatalogDirective',	"ngSanitize",
                                       			"com.2fdevs.videogular",
                                    			"com.2fdevs.videogular.plugins.controls",
                                    			"com.2fdevs.videogular.plugins.overlayplay",
                                    			"com.2fdevs.videogular.plugins.poster"]);
    openModule.controller('openCourseController',
        ['$scope', 'Loader', 'Ajax', function($scope, Loader, Ajax){
            var url = '/app/course/openJson';
            var tndId = '';
            var paramsHot = {
                sortname : 'ies_access_count',
                sortorder : 'desc'
            };
            var paramsNew = {
                sortname : 'itm_publish_timestamp',
                sortorder : 'desc'
            };
            var paramsPopularity = {
                sortname : '"snsCount.s_cnt_like_count"',
                sortorder : 'desc'
            };
            
            $scope.showController = {
            	showHost : true,
            	showNew : false,
            	showPopular : false
            };
            
            $scope.selectedTabsCallback = function(pane){
            	switch(pane.index){
         	   case 0 :
         		  $scope.showController.showHost = true;
         		  $scope.showController.showNew = false;
         		  $scope.showController.showPopular = false;
         		  break;
         	   case 1 : 
         		  $scope.showController.showHost = false;
         		  $scope.showController.showNew = true;
         		  $scope.showController.showPopular = false;
         		   break;
         	   case 2 : 
         		  $scope.showController.showHost = false;
         		  $scope.showController.showNew = false;
         		  $scope.showController.showPopular = true;
         		  break;
         	   }
               $scope.load();
            };
            
            $scope.selectedCatalog = "";
            
            $scope.selectCatalogCallback = function(catalog){
            	$scope.selectedCatalog = catalog.tnd_id;
            	$scope.load();
            }
            
            $scope.cosType = 'public';
            
            var getOpenListLoader = function(url,param){
            	return new Loader(url,param,function(data){
            		for(var i=0;i<data.items.length;i++){
            		   var item = data.items[i];
             		   item.encItmId = wbEncryptor.encrypt(item.itm_id);
             	    }
            	});
            };
            
            $scope.openListHot = getOpenListLoader(url,paramsHot);
            $scope.serverHost = serverHost;
            $scope.toggleDir = function(){
                toggleDir();
            };
            
            $scope.load = function(){
            	if($scope.showController.showHost){
            		paramsHot.tndId = $scope.selectedCatalog;
            		$scope.openListHot = getOpenListLoader(url,paramsHot);
            	}else if($scope.showController.showNew){
            		paramsNew.tndId = $scope.selectedCatalog;
            		$scope.openListNew = getOpenListLoader(url,paramsNew);
            	}else{
            		paramsPopularity.tndId = $scope.selectedCatalog;
            		$scope.openListPopularity = getOpenListLoader(url,paramsPopularity);
            	}
            };
            
            function toggleDir(){
                $(".header-guide-box").slideToggle();
            }
            
    }]).controller("openDetailController",
    		['$scope', '$http', 'Loader', 'Ajax', '$sce', 'Store', 'alertService', '$filter','wizVideo',
    		 	function($scope, $http, Loader, Ajax, $sce, Store, alertService, $filter,wizVideo){

    			var itmId = app.getUrlParam("itmId") || Store.get("itmId");
    			alertObj = alertService;
    			var url = "/app/course/openDetailJson/" + itmId;
    			var token = Store.get("token");
    			var userEntId = Store.get('loginUser');
    			$scope.serverHost = serverHost;
    			
    			$scope.itmId = itmId;
    			$scope.like = {
    					type : 'like',
				};  //用来存储点赞信息
				$scope.collect = {
					type : 'collect',
				}; // 用来存储收藏信息
				$scope.share = {
					type : 'share',
				}; // 用来存储分享信息
				$scope.video = null;
				
				$scope.videoUrl = '';

    			Ajax.post(url, {isMobile : true}, function(data){
    				if(data && data.item){
        				$scope.item = data.item;
        				var coscontent = data.coscontent;
        				if(coscontent != undefined && coscontent != '') {
        					$scope.coscontent = coscontent;
        				}

        				//
        				var module = "Course";
        				var id = data.item.itm_id;

        				$scope.like = {
        					count : data.snsCount ? data.snsCount.s_cnt_like_count : 0,
        					flag : data.sns ? data.sns.like : false,
        					id : id,
        					module : module,
        					tkhId : -1
        				};
        				$scope.collect = {
        					count : data.snsCount ? data.snsCount.s_cnt_collect_count : 0,
        					flag : data.sns ? data.sns.collect : false,
        					id : id,
        					module : module,
        					tkhId : -1
        				};
        				$scope.share = {
        					count : data.snsCount ? data.snsCount.s_cnt_share_count : 0,
        					flag : data.sns ? data.sns.share : false,
        					id : id,
        					module : module,
        					tkhId : -1
        				};
        				if($scope.like.flag || typeof $scope.like.flag != 'undefined'){
        					$(".gps-zan").addClass("active");
        				}
        				if($scope.collect.flag || typeof $scope.collect.flag != 'undefined'){
        					$(".gps-shoucang").addClass("active");
        				}
        				if($scope.share.flag || typeof $scope.share.flag != 'undefined'){
        					$(".gps-fenxiang").addClass("active");
        				}

        	   			//计算播放器的高
            			var scale = (2/3).toFixed(2);
            			var width = window.innerWidth;
            			var height = width*scale;
            			$scope.height = height;
            			$scope.width = width - 30;
            			
            			//默认第一个视频
            			$scope.video = null;
            			var video, videoUrl = "";
            			
            			var controller = this;
            			controller.config = {
            				preload: "none",
            				autoPlay : false,
            				theme: {
            					url: serverHost + "/mobile/lib/videogular/videogular.css"
            				},
            				plugins: {
            					poster: "../../images/course_open_video_post.png"
            				}
            			};
            			$scope.controller = controller;
            			
            			var is_online = false;
            			if(coscontent && coscontent.length > 0){
            				video = coscontent[0];
            				if(video) {
            					if(video && video.res_src_type == 'URL'){
            						videoUrl = video.res_src_link;
            					} else if(video && video.res_src_type.indexOf('ONLINEVIDEO_') != -1) {
            						videoUrl = video.res_src_online_link;
            						is_online = true;
            					} else {
            						videoUrl = serverHost + "/resource/" + video.res_id + "/" + video.res_src_link+"?token="+token;
            					}
                				$scope.video = video;
                				videoUrl = $sce.trustAsResourceUrl(videoUrl);
                				$scope.videoUrl = videoUrl;
                				$scope.controller.config.sources = [{src: videoUrl, type: "video/mp4"}];
                				if(appInd){
                					unlockOrientation();
                				}
                				
                    			courseNum = video.res_id;
            				}
            			} else {
                			alertObj.add('danger', 'open_noplay_resources', 2000);
            			}
            			
            			$scope.is_online = is_online;
            			viewUserId = userEntId;
        				viewCourseId = itmId;
            			
            			if(video && video.res_src_type.indexOf('ONLINEVIDEO_') != -1) {
            				$("#video").hide();
            				var online_iframe = '<iframe id="online_video" sandbox="allow-scripts allow-same-origin" src="' + videoUrl +'" class="video-js vjs-default-skin vjs-big-play-centered" width="100%" />'
            				$("#video_div").append(online_iframe)
            			} else{
            				window.onload = function (){				
            					wizVideo.setWizVideo("video");
            				}
            			}
    				}
    				
    			});

    			$scope.changeSrc = function(resId){
    				var video, video_Url;
    				angular.forEach($scope.coscontent, function(val, i){
    					if(resId == val.res_id) {
    						video = val;
    						return;
    					}
    				});

    				var is_online = false;
    				if(video) {
    					if(video && video.res_src_type == 'URL'){
    						video_Url = video.res_src_link;
    					} else if(video && video.res_src_type.indexOf('ONLINEVIDEO_') != -1) {
    						video_Url = video.res_src_online_link;
    						is_online = true;
    					} else {
    						video_Url = serverHost + "/resource/" + video.res_id + "/" + video.res_src_link +"?token="+token;
    					}
    					
    					//切换视频
    					courseNum = video.res_id;
    					isChange = true;
    					if(video && video.res_src_type.indexOf('ONLINEVIDEO_') != -1 && is_online) {
    						$("#video").hide();
    						$("#online_video").remove();
    						$("#video_div").append('<iframe id="online_video" sandbox="allow-scripts allow-same-origin" ng-show="' +is_online +'" src="'+video_Url+'" class="video-js vjs-default-skin vjs-big-play-centered" width="100%" />');
    					} else {
    						$("#video").show();
    						$("#online_video").hide();
    						$("video").attr("src",video_Url);
    						wizVideo.setWizVideo("video");
    					}
    					
        				if(video_Url){
    	        			$scope.controller.config.sources = [
    	         			   {src: $sce.trustAsResourceUrl(video_Url), type: "video/mp4"},
    	         			];
        				}
        				$scope.video = video;

        				//'正在切换到'
        				alertService.add("success", $filter('translate')('cos_switching') + "：" + video.res_title, 2000)
    				}
    				$scope.is_online = is_online;
    				$scope.$watch('controller.config.sources', function(){

    				})
    			}
    }]);
    
});

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
	var domElt = $("body")
	scope = angular.element(domElt).scope();
	scope.$apply(function(){
		alertObj.add('danger', 'mod_find_error');
	})
}