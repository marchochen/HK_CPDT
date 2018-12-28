$.include([
   'js/services/knowledgeCenterService.js',
   'js/common/courseAndExamCommon.js',
   'js/services/InstructorService.js',
   'lib/update.js'
], '../');

var AjaxObj;
var filter;
var StoreObj;

$(function(){
	var indexModule_new = angular.module('cwn', ['globalCwn']);
	indexModule_new.controller("IndexController_new",function($scope,Loader,Ajax,$filter,knowledgeCenterService,alertService,UserService,Store,InstructorService){
		
		filter = $filter;
		
		if(appInd){//app升级提示
			callNative(function(){
				if("Android" == plus.os.name){//Android才提示更新
					initUpdate(function(updateFlag){
						checkPasswordUpdate();//用户是否需要修改密码
					});
				}else{
					checkPasswordUpdate();//用户是否需要修改密码
				}
			});
		}else{
			checkPasswordUpdate();//用户是否需要修改密码
		}
		
		$scope.serverHost = serverHost;
		AjaxObj = Ajax;
	    
	    StoreObj = Store;
	    
	    $scope.cur_lan_en = StoreObj.get("globalLang");
	   //bill 2016-01-14
		$scope.toogleType = function(){
		     $('#main').toggle();
		     $scope.isFristLogin = false;
		}		
		
		
		if(StoreObj.get("isFristLogin") == "true"){
			UserService.userInfo(function(data){
				if(data && data.regUser){
					$scope.user = data.regUser;
					var usr_ent_id = $scope.user.usr_ent_id;
					var url = '/app/personal/home/json/' + usr_ent_id;
					$scope.totalCredits = 0;             //总积分数
					AjaxObj.post(url, {}, function(credit){
						$scope.totalCredits = credit.total_credits;
					});
					
					$scope.ucl_point = StoreObj.get("loginCredit");
					StoreObj.set("isFristLogin", false);
					$scope.isFristLogin = true;
				}
			});
		}
				
		var url, params;
		
		//新增：加上 In progress 的課程 (按 my course 的樣式)
		url = "/app/course/getMyCourse";
		params = {
			orderByPubType:true,
			pageSize : 2,
			appStatus:"I"
		};
		$scope.progressCourseLoader = new Loader(url,params,function(data){
			for(var i in data.items){
				var app = data.items[i];
				
				app.item.cla = courseAndExamModule.getItemIcon("course",app.item.itm_type);
				app.appStatusJson = courseAndExamModule.getAppStatusJson(app.app_status);
				
				app.item.encItmId = wbEncryptor.encrypt(app.item.itm_id);
				
				if(app.cov && app.cov.cov_progress){
					app.cov_progress = app.cov.cov_progress;
				}else if(app.app_tkh_id > 0){
					app.cov_progress = 10; 
				}
			}
		});
		$scope.progressCourseLoader.loaderMore(1);
		
		url = "/app/poster/Json";
		//宣传栏
		Ajax.post(url,{'isMobile':1},function(data){
			
			if(!data){
				return;
			}
			var poster = data.poster[0];
			
			var html = [];
			var btnHtml = [];
			//注意一个数组内容记录一个
			if(poster.sp_media_file != undefined && poster.sp_media_file != '' && poster.sp_status =='ON'){
		        html.push('<div class="touchslider-item">' + '<img src="'
		        		+ serverHost + poster.sp_media_file + '"  alt=""/></div>');
			}
			if(poster.sp_media_file1 != undefined && poster.sp_media_file1 != '' && poster.sp_status1 =='ON'){
		        html.push('<div class="touchslider-item">' + '<img src="'
		        		+ serverHost + poster.sp_media_file1 +'"  alt=""/></div>');
			}
			if(poster.sp_media_file2 != undefined && poster.sp_media_file2 != ''  && poster.sp_status2 =='ON'){
		        html.push('<div class="touchslider-item">' + '<img src="'
		        		+ serverHost + poster.sp_media_file2 + '"  alt=""/></div>');
			}
			if(poster.sp_media_file3 != undefined && poster.sp_media_file3 != ''  && poster.sp_status3 =='ON'){
		        html.push('<div class="touchslider-item">' + '<img src="'
		        		+ serverHost + poster.sp_media_file3 + '"  alt=""/></div>');
			}
			
			if(html.length < 1){
				$(".touchslider").hide();
				return;
			}
			
			for(var i=0; i<html.length; i++){
				btnHtml.push('<span class="touchslider-nav-item ');
				if(i==0){
					btnHtml.push('touchslider-nav-item-current');
				}
				btnHtml.push('"></span>');
			}
			$(".touchslider-navbox").html(html.length > 1 && btnHtml.join(''));
			$(".touchslider-viewport").html(html.join(''));

			$(".touchslider").touchSlider({mouseTouch:true, autoplay:true});
		});
		
		//1必修课或推荐课程 ，如果有必修课程就显示必修课程，如果没有必修课程就显示“推荐课程” 如果两都都没有，就不显示这一栏
		$scope.compulsoryOrrecomendTitle = "cos_my_compulsory_courses";//标题对应也要改，默认是我的必修课
		url = "/app/course/recommendJson";
		params = {
			showMobileOnly : true, 
	    	pageSize : 2,
	    	isCompulsory : 1//必修
	    };
		$scope.isCompulsory = true;
		
		$scope.recommendcos = new Loader(url, params,function(data){
			
			if(data.items.length <= 0){
				
				$scope.isCompulsory = false;
				$scope.compulsoryOrrecomendTitle = "cos_recommend_course_for_you";//标题修改为 【为你推荐】
				
				$scope.recommendcos = new Loader("/app/course/recommendJson?pageSize=2", {showMobileOnly : true},function(data){
					for(var i in data.items){
						var app = data.items[i];
						app.item.encItmId = wbEncryptor.encrypt(app.item.itm_id);
						app.item.cla = courseAndExamModule.getItemIcon("course",app.item.itm_type);
					}
				});
				
				$scope.recommendcos.loaderMore(1);
				
			}else{
				for(var i in data.items){
					var app = data.items[i];
					app.item.encItmId = wbEncryptor.encrypt(app.item.itm_id);
					app.item.cla = courseAndExamModule.getItemIcon("course",app.item.itm_type);
				}
			}
			
		});
		
		$scope.recommendcos.loaderMore(1);
		
		$scope.showLive = false;
		//获取直播频道的个数，如果个数为0，则隐藏菜单的【直播】
		var url = "/app/live/getLiveCount";
		Ajax.post(url,{},function(data){
			if(data != undefined && data.lv_count > 0){
				$scope.showLive = true;
			}
		});
		
		//2精品课程
		url = '/app/rank/course_rank';
        params = {
        	showMobileOnly : true,
            sortname : 's_cnt_like_count',
            sortorder : 'desc',
            pageSize : 3
        };
        /*$scope.newsCourseList = new Loader(url, params,function(data){
        	for(var i in data.items){
				var item = data.items[i];
				item.encItmId = wbEncryptor.encrypt(item.itm_id);
				item.cla = courseAndExamModule.getItemIcon("course",item.itm_type);
			}
        });
		$scope.newsCourseList.loaderMore(1);*/
		
		//3公开课
		url = "/app/course/openJson";
	    params = {
	    	pageSize : 3
	    };
		$scope.opencos = new Loader(url, params,function(data){
			for(var i in data.items){
				var item = data.items[i];
				item.encItmId = wbEncryptor.encrypt(item.itm_id);
			}
		});
		
		$scope.opencos.loaderMore(1);
		
		//4讲师风采
		/*params = {
	    	pageSize : 3
	    };
		$scope.instrLoader = InstructorService.getInstructorLoader(params);
		$scope.instrLoader.loaderMore(1);*/
		
		//5问答
		/*url = "/app/know/getLatestQuestionList";
		params = {
		    pageSize : 3,
		    sortname : 'que_update_timestamp',
		    sortorder : 'desc'
		};
		
		var questionType = {
			'FAQ' : {'label':'know_faq','cla':'box-num-cont-bluer'},
			'UNSOLVED' : {'label':'know_type_UNSOLVED','cla':'box-num-cont-pink'},
			'SOLVED' : {'label':'know_type_SOLVED','cla':'box-num-cont-yellow'},
			'POPULAR' : {'label':'know_popular','cla':'box-num-cont-purple'}
		};
		
		$scope.questionList = new Loader(url, params, function(result){
			var items = result.items;
			for(var i=0;i<items.length;i++){
				items[i].typeInfo = questionType[items[i].que_type];
				items[i].enc_que_id = wbEncryptor.encrypt(items[i].que_id);
			}
		});
		$scope.questionList.loaderMore(1);*/
		
		//6知识中心
		/*$scope.knCenterList = knowledgeCenterService.getKnCenterListLoader({pageSize:3});
		$scope.knCenterList.loaderMore(1);*/
		
		$scope.$on("learningMapCount",function(e,data){
			
			var gradeMapCont = data.gradeMapCont;//职级发展个数
			var positionMapCount = data.positionMapCount;//岗位学习地图个数
			
			//获取关键岗位的个数，如果个数为0，则关键岗位图片入口
			$scope.showPositionMap = (positionMapCount > 0);
			
			//获取职级发展学习地图的个数，如果个数为0，则隐藏职级发展学习地图图片入口
			$scope.showGradeMap = (gradeMapCont > 0);
			
		});
		
		//7 获取统计数据
		$scope.announceCount = 0;
		$scope.messageCount = 0;
		var url = "/app/home/count";
		Ajax.post(url, {isMobile : 1}, function(data){
			
			var setCount = function(count){//设置显示的格式，如果大于99 显示 99+，如果是0，则不显示数字
				if(count > 99){
					return "99+";
				}
				
				if(count == 0){
					return "";
				}
				return count;
			}
			
			var totalCount = data.announceCount + data.messageCount  + data.approvalCount + data.evaluationCount + data.votingCount;
			$scope.totalCount = setCount(totalCount);
			
			var count = data.announceCount;
		    $scope.announceCount = setCount(count);

		    count = data.messageCount;
			$scope.messageCount = setCount(count);
			
			count = data.approvalCount;
			$scope.approvalCount = setCount(count);
			
			count = data.evaluationCount;
			$scope.evaluationCount = setCount(count);
			
			count = data.votingCount;
			$scope.votingCount = setCount(count);
			
			//是否拥有下属
			$scope.hasStaff = data.hasStaff;
			
		});
		
		//8 精彩专题:获取推送到首页的专题培训作为轮播图
		var url = "/app/learningmap/getSpecialListIndex";
		Ajax.post(url,{ust_showindex:1},function(data){
			
			for(var i=0;i<data.length;i++){
				var item = data[i];
				item.enc_ust_id = wbEncryptor.encrypt(item.ust_id);
			}
			
			$scope.specialListForIndex = data;
		});
		
		//用户是否需要修改密码
		function checkPasswordUpdate(){
			
			var exec = function(){
				if(!Store.get("ignoreChangePwd")){
					var url = "/app/personal/pwdNeedChangeInd";
					Ajax.post(url,{},function(data){
						if(data){
							var token = Store.get("token");
							if(token != '') {
								if(appInd){
									callNative(function(){
										clicked("setUp/setPassword.html?fromIndex=true");
									});
								}else{
									clicked("setUp/setPassword.html?fromIndex=true");
								}
							} else {
								Store.getStorage().setItem("globalLang",Store.get("globalLang"));
								setTimeout(function(){
									clicked("login.html");
								}, 1000);
							}
						}
					});
				}
			}
			
			if("complete" == document.readyState){
				exec();
			}else{
				window.addEventListener("load",function(){
					exec();
				});
			}
			
		}
		
		
		$scope.toQuestionPage = function(know){
			javascript:clicked('know/detail.html?type='+know.que_type+'&id='+know.enc_que_id,true);
		};
		
		$scope.toggleTaskModal = function(){
			getStatistics();
			$scope.taskShow = !$scope.taskShow;
			$(".header-overlay").removeClass("hide");
		};
		
		$scope.toUrl = function(url){
			javascript:clicked(url,true);
		};
		
		if(appInd){
			$scope.appInd = appInd;
			var exitInd = false;
			
			callNative(function(){
				changeBackButtion(function(){
					exitProgram(alertService);
				});
				var offline_flag = StoreObj.get("offlineFlag");
				if(!offline_flag || offline_flag != "true"){
					getOffline();
					StoreObj.set("offlineFlag", true);
				}
			});

			function getOffline(){
				//预加载离线学习界面
				webview = plus.webview.getWebviewById("offline");
				if(webview != undefined){
					webview.close();
				}
				plus.webview.create("module/offline.html","offline",{scrollIndicator:'none',scalable:false});

				//自动提交离线学习的学习记录
				var keyStr = plus.storage.getItem("modKey" + plus.storage.getItem("loginUserId"));
				if(keyStr != undefined && keyStr != null && keyStr != ""){
					var keyArray = keyStr.split("[|]");
					for(var i=0;i<keyArray.length;i++){
						var modKey = keyArray[i];
						var mod = JSON.parse(plus.storage.getItem(modKey));
						if(mod.duration > 0 && plus.networkinfo.getCurrentType() != 1){
							Ajax.post('/app/module/sendModuleTrack', mod, function(){
								mod.duration = 0;
								plus.storage.setItem(modKey, JSON.stringify(mod));
							});
						}
					}
				}
			}

			//进入离线学习界面
			$scope.showOffline = function(){
				webview = plus.webview.getWebviewById("offline");
				if(webview == undefined){
					webview = plus.webview.create("module/offline.html","offline",{scrollIndicator:'none',scalable:false});
				}
				webview.show('slide-in-right', 200);
			}
		}
		
	});
	
});

function getStatistics(){
	$(function(){		
		var url = serverHost + "/app/home/count";
		$.ajax({
			url : url,
		 	method: 'POST',
			dataType: 'json',
		 	data : {isMobile : 1},
			success:  function(data){	
				var setCount = function(count){//设置显示的格式，如果大于99 显示 99+，如果是0，则不显示数字
					if(count > 99){
						return "99+";
					}				
					if(count == 0){
						return "";
					}
					return count;
				}
				
				var totalCount = data.announceCount + data.messageCount  + data.approvalCount + data.evaluationCount + data.votingCount;
				totalCount = setCount(totalCount);
				if(totalCount != undefined && totalCount != ""){
				}else{
					$("#totalCount").prev("em").hide();
				}
				
			    var announceCount = setCount(data.announceCount);
			    if(announceCount != undefined && announceCount != ""){
					$("#announceCount").html(announceCount);
					$("#memuAnnounceCount").html(announceCount);
				}else{
					$("#announceCount").prev("em").hide();
					$("#memuAnnounceCount").hide();
					$("#announceCount").html("");
				}
		
				var messageCount = setCount(data.messageCount);
				if(messageCount != undefined && messageCount != ""){
					$("#messageCount").html(messageCount);
				}else{
					$("#messageCount").prev("em").hide();
					$("#messageCount").html("");
				}
				
				var approvalCount = setCount(data.approvalCount);
				if(approvalCount != undefined && approvalCount != ""){
					$("#approvalCount").html(approvalCount);
				}else{
					$("#approvalCount").prev("em").hide();
					$("#approvalCount").html("");
				}
				
				var evaluationCount = setCount(data.evaluationCount);
				if(evaluationCount != undefined && evaluationCount != ""){
					$("#evaluationCount").html(evaluationCount);
				}else{
					$("#evaluationCount").prev("em").hide();
					$("#evaluationCount").html("");
				}
				
				var votingCount = setCount(data.votingCount);
				if(votingCount != undefined && votingCount != ""){
					$("#votingCount").html(votingCount);
				}else{
					$("#votingCount").prev("em").hide();
					$("#votingCount").html("");
				}
				
			}
			
		});
	})
}