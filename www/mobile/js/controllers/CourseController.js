$.include([
    'lib/angular/angular-touch.min.js',
    'js/directives/SNSDirectives.js',
    'js/directives/CourseDirective.js',
    'js/directives/CatalogDirective.js',
    'js/directives/CommentDirective.js',
    'js/services/SNSService.js',
    'lib/easypiechart/angular.easypiechart.js',
    'js/common/courseAndExamCommon.js'
], '../../');
var alertObj;
$(function(){
	
   var courseModule = angular.module('course', ['globalCwn', 'CatalogDirective', 'easypiechart', 'ngTouch']);
   
   courseModule.filter('unlimit', ['$filter', function($filter){
   		return function(input){
   			return  input == 0 ? $filter('translate')('global_unlimit') : input;
   		};
   }]);
   var params = {
   };

  
   //课程类型
   var course_type = [];
   course_type.push({id : 'all', value:'', title : 'cos_type_all', select : true});
   course_type.push({id : 'selfstudy', value : 'selfstudy', title : 'cos_type_selfstudy'});
   course_type.push({id : 'classroom', value : 'classroom', title : 'cos_type_classroom'});
   course_type.push({id : 'integrated', value : 'integrated', title : 'cos_type_integrated'});
   
   var app_status = [];
   app_status.push({id : 'all', value:'', title:'cos_type_all', select : true});
   app_status.push({id : 'notapp', value:'notapp', title:'cos_app_notapp'});
   app_status.push({id : 'inprocessed', value:'inprocessed', title:'cos_app_inprogress'});
   app_status.push({id : 'pending', value:'pending', title:'cos_app_pending'});
   app_status.push({id : 'completed', value:'completed', title:'cos_app_completed'});
   
   var _recommend = [];
   _recommend.push({id : 'all', value:'', title:'cos_type_all', select : true});
   _recommend.push({id : 'tadm', value:'tadm', title:'cos_recommend_tadm'});
   _recommend.push({id : 'grade', value:'grade', title:'cos_recommend_grade'});
   _recommend.push({id : 'group', value:'group', title:'cos_recommend_group'});
   _recommend.push({id : 'sup', value:'sup', title:'cos_recommend_sup'});
   _recommend.push({id : 'position', value:'position', title:'cos_recommend_position'});

   courseModule.controller('catalogController', ['$scope', '$http', 'Loader', 'Ajax', '$translate', function($scope, $http, Loader, Ajax, $translate) {
   	   var url = "/app/course/catalogCourseJson";

   	   var cosType = "";
   	   var tndId = "";
   	   $scope.serverHost = serverHost;
       $scope.catalog = new Loader(url, params);
       
       //课程类型
       $scope.cosTypes = course_type;
       
       $scope.selectType = function(id){
           for (var index in $scope.cosTypes) {
        	   if($scope.cosTypes[index].id == id){
        		   $scope.cosTypes[index].select = true;
        	   } else {
        		   $scope.cosTypes[index].select = false;
        	   }
           }
       };

       $scope.toggleDir = function(){		   
    	   toogleDir();  //隐藏和现实切换
       };
       
       $scope.sureCheck = function(){
    	   for(var index in $scope.firstCatalogs){
    		   if($scope.firstCatalogs[index].select){
    			   tndId = $scope.firstCatalogs[index].tnd_id;
    			   break;
    		   }
    	   }
           for (var index in $scope.cosTypes) {
        	   if($scope.cosTypes[index].select){
        		   cosType = $scope.cosTypes[index].value;
        		   break;
        	   }
           }
    	   params = {
    		   pageSize : '4',
    		   itemType : cosType,
    		   tndId : tndId
    	   };
           $scope.catalog = new Loader(url, params);           
           toogleDir();
       };
      
   }]).controller('recommendController', ['$scope', '$http', 'Loader', 'Ajax', function($scope, $http, Loader, Ajax) {
		var url = "/app/course/recommendJson";
		var cosType = ""; // 课程类型
		var selectType = ""; // 推荐类型
		var appStatus = ""; // 学习状态

		var pane1_params = angular.extend({
			isCompulsory : ''
		}, params); // 全部
		var pane2_params = angular.extend({
			isCompulsory : '1'
		}, params); // 必修
		var pane3_params = angular.extend({
			isCompulsory : '0'
		}, params); // 选修

		$scope.serverHost = serverHost;

		$scope.pane1 = new Loader(url, pane1_params);
		$scope.pane2 = new Loader(url, pane2_params);
		$scope.pane3 = new Loader(url, pane3_params);

		// 推荐类型
		$scope.recommendTypes = _recommend;

		// 课程类型
		$scope.cosTypes = course_type;

		// 学习状态
		$scope.allAppStatus = app_status;

		$scope.toggleDir = function() {
			toogleDir(); // 隐藏和现实切换
		};

		$scope.selectType = function(id) {
			for ( var index in $scope.cosTypes) {
				if ($scope.cosTypes[index].id == id) {
					$scope.cosTypes[index].select = true;
				} else {
					$scope.cosTypes[index].select = false;
				}
			}
		};

		$scope.selectAppStatus = function(id) {
			for ( var index in $scope.allAppStatus) {
				if ($scope.allAppStatus[index].id == id) {
					$scope.allAppStatus[index].select = true;
				} else {
					$scope.allAppStatus[index].select = false;
				}
			}
		};

		$scope.selectRecommendStatus = function(id) {
			for ( var index in $scope.recommendTypes) {
				if ($scope.recommendTypes[index].id == id) {
					$scope.recommendTypes[index].select = true;
				} else {
					$scope.recommendTypes[index].select = false;
				}
			}
		};
		//确认选择条件
		$scope.sureCheck = function() {
			for ( var index in $scope.cosTypes) {
				if ($scope.cosTypes[index].select) {
					cosType = $scope.cosTypes[index].value;
					break;
				}
			}
			for ( var index in $scope.recommendTypes) {
				if ($scope.recommendTypes[index].select) {
					selectType = $scope.recommendTypes[index].value;
					break;
				}
			}
			for ( var index in $scope.allAppStatus) {
				if ($scope.allAppStatus[index].select) {
					appStatus = $scope.allAppStatus[index].value;
					break;
				}
			}
			params = {
				itemType : cosType,
				selectType : selectType,
				appStatus : appStatus
			};
			angular.extend(pane1_params, params);
			angular.extend(pane2_params, params);
			angular.extend(pane3_params, params);

			$scope.pane1 = new Loader(url, pane1_params);
			$scope.pane2 = new Loader(url, pane2_params);
			$scope.pane3 = new Loader(url, pane3_params);

			toogleDir();
		};
       
      
   }]).controller('signupController', ['$scope', '$http', 'Loader', 'Ajax', function($scope, $http, Loader, Ajax) {
   	   var url = "/app/course/signupJson";
   	   var cosType = "";

   	   var inprogress_params = angular.extend({appStatus : 'inprogeress'}, params)
   	   var completed_params = angular.extend({appStatus : 'completed'}, params)
   	   var pending_params = angular.extend({appStatus : 'pending'}, params)
   	   
       $scope.serverHost = serverHost;
   	   
       $scope.inprogress = new Loader(url, inprogress_params);
       $scope.completed = new Loader(url, completed_params);
       $scope.pending = new Loader(url, pending_params);
       

       //课程类型
	   var type = course_type;
       $scope.cosTypes = type;
       
       
       $scope.toggleDir = function(){
    	   toogleDir();  //隐藏和现实切换
       };
       
       $scope.selectType = function(id){
           for (var index in $scope.cosTypes) {
        	   if($scope.cosTypes[index].id == id){
        		   $scope.cosTypes[index].select = true;
        	   } else {
        		   $scope.cosTypes[index].select = false;
        	   }
           }
       };
       
       $scope.sureCheck = function(){
           for (var index in $scope.cosTypes) {
        	   if($scope.cosTypes[index].select){
        		   cosType = $scope.cosTypes[index].value;
        		   break;
        	   }
           }
    	   params = {
    		   itemType : cosType,
    	   };
       	   angular.extend(inprogress_params, params)
       	   angular.extend(completed_params, params)
       	   angular.extend(pending_params, params)

           $scope.inprogress = new Loader(url, inprogress_params);
           $scope.completed = new Loader(url, completed_params);
           $scope.pending = new Loader(url, pending_params);
        
           toogleDir();
       };
       
   }]).controller('courseController', ['$scope', '$http', 'Loader', 'Ajax', function($scope, $http, Loader, Ajax) {
	   
	   //统计数量
	   var url = "/app/course/signup/count";
	   Ajax.post(url, {isExam : 0}, function(data){
		   var count = data.count;
		   $scope.signupCount = count > 99 ? 99 : count;
	   })
	   
   	   //进入课程首页，菜单下面的列表默认显示已报名课程
	   var url = "/app/course/signupJson";

   	   params = angular.extend({appStatus : 'inprogeress'}, params)
   	   
       $scope.serverHost = serverHost;
   	   
       $scope.signup = new Loader(url, params);
       
   }]).controller('detailController', ['$scope', '$http', 'Store', 'Ajax', 'alertService', 'dialogService' , function($scope, $http, Store, Ajax, alertService,dialogService) {
	   
	   alertObj = alertService;
	   var itmId = app.getUrlParam("itmId") || Store.get("itmId") 
   	   var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
   	   $scope.tabs = 3;
   	   if(!itmId) {
	       alertService.add('danger', '出错啦！课程ID不能为空!');
   	   }
   	   
	   var url = "/app/course/detailJson/" + itmId;
	   var params = {
	       tkhId : (tkhId == "" || tkhId == undefined)? 0 : tkhId,
	       mobileInd : 1
	   }   	 
	   $scope.itmId = itmId;
       $scope.serverHost = serverHost;
       $scope.appInd = appInd;
	   //饼状图
	   $scope.cov_progress = 10;
	   $scope.options = {
		    animate: 1500,
			delay: 3000,
			barColor: '#0ad0ed',
			trackColor: '#f4f4f4',
			scaleColor: false,
			lineWidth: 15,
			lineCap: 'circle',
			trackWidth: 16,
			size : 80
	   }
	   //分数
	   $scope.myScore = "0";
	   //学习时长
	   $scope.ls_learn_duration = "00:00:00";
	   
	   var isClass = false;		//是班级
	   var isOffline = false;   //是面授课程
	   var isIntegrated = false;//是培训式项目
	   var isSelfStudy = false; //是网上课程
	   
		$scope.like = {
			type : 'like',
		};  //用来存储文章点赞信息
		$scope.collect = {
			type : 'collect',
		}; // 用来存储文章收藏信息
		$scope.share = {
			type : 'share',
		}; // 用来存储文章分享信息
		$scope.btnParams = [];
		
		$scope.courseLoaded = false;
		
		$scope.contentPermitted = true;
		
	   Ajax.post(url, params, function(data){
		   if(!data.item) return;
		   var coscontent = data.coscontent;
		   var dircontents = [];
		   var modcontents = [];
		   var integratedcontents = [];
		   
			var itemApp = data.app;
			var curDate = data.curDate;
			var userEntId = data.userEntId;
			var item = data.item;
			var rcov = data.courseEvaluation;
			
			// /r/n换行替换
			if(data.item && data.item.ies){
				data.item.ies.ies_contents = MreplaceVlaue(data.item.ies.ies_contents);
				data.item.ies.ies_objective = MreplaceVlaue(data.item.ies.ies_objective);
				data.item.ies.ies_prerequisites = MreplaceVlaue(data.item.ies.ies_prerequisites);
				data.item.ies.ies_exemptions =  MreplaceVlaue(data.item.ies.ies_exemptions);
				data.item.ies.ies_remarks = MreplaceVlaue(data.item.ies.ies_remarks);
				data.item.ies.ies_audience = MreplaceVlaue(data.item.ies.ies_audience);
				data.item.ies.ies_duration = MreplaceVlaue(data.item.ies.ies_duration);  
				data.item.ies.ies_schedule = MreplaceVlaue(data.item.ies.ies_schedule);
			}
			

			// 报名日期
			if(data.att_create_timestamp){
				$scope.att_create_timestamp = data.att_create_timestamp;
			}
			

			 var itm_retake_ind = item.itm_retake_ind;//是否允许重读
			$scope.resId = data.resId;
			$scope.userEntId = userEntId;
			$scope.itmTitle = item.itm_title;
			$scope.curDate = curDate;
			$scope.app = itemApp;
			$scope.rcov = rcov;
			$scope.messages = data.messages;
			//报名id
			tkhId = (itemApp && itemApp.app_tkh_id) ? itemApp.app_tkh_id : 0;
			
			$scope.tkhId = tkhId;
		    $scope.lessons = data.lessons;
		
			//app_status 学习状态
		   var appStatus = data.app ? data.app.app_status : "cos_app_notapp";
		   var courseEvaluation = data.courseEvaluation;
		   if(courseEvaluation) {
			    //学习进度
			    if(courseEvaluation.cov_progress) {
				    $scope.cov_progress = courseEvaluation.cov_progress;
				}
				
			    //学习时长
			    if(courseEvaluation.cov_total_time) {
			    	var ms = parseInt(courseEvaluation.cov_total_time) * 1000;
				    $scope.ls_learn_duration = getTime(ms);

			    }
				   
			    //分数
			    $scope.myScore = courseEvaluation.cov_score ? courseEvaluation.cov_score :'0';
			      
			    //app_status 学习状态
			    if(courseEvaluation.cov_status) {
			    	appStatus = courseEvaluation.cov_status;
			    } else if(itemApp && itemApp.app_status) {
			    	appStatus = itemApp.app_status;
			    }
		   }
		   //获取学习状态，appStatus 从服务器传回来，是大写的 ICPF，未报名以及等待审批等也要考虑在里面
		   $scope.appStatus = courseAndExamModule.getAppStatusJson(appStatus);
			
		    if(tkhId < 1 && appStatus != "Pending") {//学习状态为待审的时候，也需要显示我的状态”Tab,进度显示为：0
				$scope.tabs = 2;
			} else {
				$scope.tabs = 3;
				$scope.showMyStatusTab = true;//显示我的状态tab
				
				if(appStatus == "Pending"){//学习状态为待审的时候,学习进度为 0
					$scope.cov_progress = 0; 
				}
			}
		    
			//判断是不是考试, 页面显示的label控制
			var typeKinds = 'cos';
			var isExam = item && item.itm_exam_ind && item.itm_exam_ind > 0;
			if(isExam) {
				typeKinds = 'exam';
			}
			$scope.typeKinds = typeKinds;
			//课程详情
			$scope.course_type =  typeKinds + '_type_selfstudy';
			if(item.itm_run_ind == 1) {
				isClass = true;				
				$scope.course_type =  typeKinds + '_type_class';
			} else if(item.itm_integrated_ind == 1){
				isIntegrated = true;
				$scope.course_type =  typeKinds + '_type_integrated';
			} else if(item.itm_create_run_ind == 1){
				isOffline = true;
				$scope.course_type =  typeKinds + '_type_classroom';
			} else {
				isSelfStudy = true;
			}
			$scope.isClass = isClass;
			$scope.isIntegrated = isIntegrated;
			$scope.isOffline = isOffline;
			$scope.isSelfStudy = isSelfStudy;
			
			$scope.itemIconClass = courseAndExamModule.getItemIconDetail(typeKinds,item.itm_type);
			
			$scope.btnParams = {
					btn : data.btn,
					itm_id : item.itm_id,
					app_id : itemApp ? itemApp.app_id:'',
					app_tkh_id : itemApp ? itemApp.app_tkh_id:'',
					itm_title : item.itm_title,
					itm_fee : item.itm_fee,
					itm_icon : item.itm_icon,
					itm_type : item.itm_type,
					appnTimeValide : !data.appnTimeValide && item.itm_create_run_ind == 0 ? true : false,
					canApp : (rcov == undefined || (rcov != undefined && (rcov.cov_status == 'C' || rcov.cov_status == 'F' || rcov.cov_status == 'W') ))
			}
			
			var reload = function(){
				window.location.replace("detail.html?itmId="+itmId);
		    };
			
			//是否可以显示【我想放弃学习】的按钮
			if(data.btn === 2 || data.btn === 5 || data.btn === 6){
				$scope.canShowCancelBtn = true;
				
				//取消报名 action
				$scope.cancelEnrol = function(){
					
					if(!window.cancelEnrolFlag){//防止重复点击
						window.cancelEnrolFlag = true;
						var appId = $scope.btnParams.app_id;
				   		if(appInd){
				   			var wait = plus.nativeUI.showWaiting();
				    	}else{
				    		app.showWaiting();
				    	}
						var url = '/app/application/cancel?app_id=' + appId;
						Ajax.post(url, null ,function(result){
							dialogService.modal("delete_enroll","o",function(){
							
								if(appInd){
									changeWebviewDetail('signup.html');
									changeWebviewDetail('courseIndex',function(){
										wait.close();
										reload();
									});
								} else {
									reload();
								}
								
							});
							
						})
					}
					
				};
			}
			
			//课程目录
			var catalogs = data.catalogs;
			var catalogStr = [];
			angular.forEach(catalogs, function(val, i){
				if(val && val.tnd_title) {
					catalogStr.push(val.tnd_title);
				}
			})
			if(catalogStr.length > 0){
				$scope.catalogStr = catalogStr.join(",");
			}
			
			//点赞，分享，收藏
			if(!isClass) {
				var module = "Course";
				var id = data.item.itm_id;
				
				$scope.like = {
					count : data.snsCount ? data.snsCount.s_cnt_like_count : 0,
					flag : data.sns ? data.sns.like : false,
					id : id,
					module : module,
					tkhId : tkhId,
					clickFlag : true //是否可点击的标记，避免重复提交
				};
				$scope.collect = {
					count : data.snsCount ? data.snsCount.s_cnt_collect_count : 0,
					flag : data.sns ? data.sns.collect : false,
					id : id,
					module : module,
					tkhId : tkhId,
					clickFlag : true //是否可点击的标记，避免重复提交
				};
				$scope.share = {
					count : data.snsCount ? data.snsCount.s_cnt_share_count : 0,
					flag : data.sns ? data.sns.share : false,
					id : id,
					module : module,
					tkhId : tkhId,
					clickFlag : true // //是否可点击的标记，避免重复提交
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
				
			}

			//全局的变量
			if(itemApp && itemApp.app_status == 'Admitted' && itemApp.app_id >0) {
				$scope.isApp = true;	//是否报名
			}
			$scope.timeValide = data.timeValide;	//内容时间是否有效
			
			//详情
		    if(item.itm_app_approval_type) {
			   item.itm_app_approval_type = 'cos_is_approval_yes' 
		    } else {
			   item.itm_app_approval_type = 'cos_is_approval_no' 
		    }
			
		    //网上课程，班级的， 集成培训
			if(isSelfStudy || isClass || isIntegrated){
			
		   	   //报名限期
			   if(item.itm_appn_start_datetime){
				   item.itm_appn_start_datetime = app.formatDate(app.parseDate(item.itm_appn_start_datetime, 'yyyy-MM-dd HH:mm:ss'), 'yyyy-MM-dd HH:mm:ss');
			   }
			   if(item.itm_appn_end_datetime){
				   item.itm_appn_end_datetime = item.itm_appn_end_datetime != '9999-12-31 23:59:59'?
						app.formatDate(app.parseDate(item.itm_appn_end_datetime, 'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') : 'global_unlimit';
			   }
			   //内容期限
			   if(item.itm_content_eff_start_time){
				   item.itm_content_eff_start_time = app.formatDate(app.parseDate(item.itm_content_eff_start_time, 'yyyy-MM-dd HH:mm:ss'), 'yyyy-MM-dd HH:mm:ss');
			   }
			   if(item.itm_content_eff_end_time){
				   item.itm_content_eff_end_time = item.itm_content_eff_end_time != '9999-12-31 23:59:59'?
						app.formatDate(app.parseDate(item.itm_content_eff_end_time, 'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') : 'global_unlimit';
			   }
			   
			   //讲师
			   $scope.instructors = data.instructors;
			   if(!$scope.instructors || $scope.instructors.length<=0){
				   $scope.tabs--;
			   }
			   
		       //讲师评分
		       var instructorComment = data.instructorComment;
		       if(instructorComment){
			       $scope.itc_style_score = instructorComment.itc_style_score.toFixed(2);
			       $scope.itc_quality_score = instructorComment.itc_quality_score.toFixed(2);
			       $scope.itc_structure_score = instructorComment.itc_structure_score.toFixed(2);
			       $scope.itc_interaction_score = instructorComment.itc_interaction_score.toFixed(2);
			       $scope.itc_score = instructorComment.itc_score.toFixed(2);
			       $scope.itc_comment = instructorComment.itc_comment;
		       }
		     
			   if(isIntegrated) {
				   // 集成培训 必修选修条件
				   angular.forEach(data.child, function(val, i){
					   var obj = {
						   icd_type : val[0].icd.icd_type,
						   icd_completed_item_count : val[0].icd.icd_completed_item_count,
						   child : val
					   }
					   integratedcontents.push(obj);
				   });
				   $scope.integratedcontents = integratedcontents// data.child;//integratedcontents;
			   } else {
				   //网上内容，班级和网上课程才有
				   angular.forEach(coscontent, function(val, i) {
					   var itemtype = val.itemtype;
					   //FDR  MOD
					   if(itemtype == 'FDR') {
						   //目录下的模块
						   //var fdrcontent = val.children;
						   var subchilds = [];
						   angular.forEach(val.children, function(cld, i){
							   var obj = cld.resources || {};
							   var appOK = itemApp && itemApp.app_id > 0 && itemApp.app_status == 'Admitted';
							   cld.rcov = rcov;
							   cld.appOK = appOK;

							   if(obj.mov && obj.mov.mov_total_time >0 ){
								   var mov_total_time = obj.mov.mov_total_time * 1000;
								   cld.resources.mov.mov_total_time_str = getTime(mov_total_time)
							   }
							   if(cld.resources) {		//如果已发布
								   cld.title = MreplaceModValue(cld.title);
								   subchilds.push(cld);
							   }
							   ///val[i].children = fdrcontent;
						   })
						   val.children = subchilds;
						   val.title = MreplaceModValue(val.title);
						   dircontents.push(val);
						   //TODO 循环子集，给子集加上是否完成，是否有先修条件
					   } else {
						   //
						   var appOK = itemApp && itemApp.app_id > 0 && itemApp.app_status == 'Admitted';
						   var obj = val.resources || {};
						   val.rcov = rcov;
						   val.appOK = appOK;
						   
						   if(obj.mov && obj.mov.mov_total_time >0 ){
							   var mov_total_time = obj.mov.mov_total_time*1000
							   val.resources.mov.mov_total_time_str = getTime(mov_total_time)
						   }
						   //独立模块，没有目录  
						   if(val.resources){		//如果已发布
							   val.title = MreplaceModValue(val.title);
							   modcontents.push(val);
						   }
					   }
					   $scope.dircontents = dircontents;
					   $scope.modcontents = modcontents;
				   });
			   }
			   
		   } else if(isOffline) {
			   //离线课程
			   var classlist = item.childrens;
			   angular.forEach(classlist, function(val, i) {
				   if(val.itm_content_eff_start_time){
					   val.itm_content_eff_start_time = app.formatDate(app.parseDate(val.itm_content_eff_start_time, 'yyyy-MM-dd HH:mm'), 'yyyy-MM-dd HH:mm');
				   }
				   if(val.itm_content_eff_end_time){
					   val.itm_content_eff_end_time = val.itm_content_eff_end_time != '9999-12-31 23:59:59'?
							app.formatDate(app.parseDate(val.itm_content_eff_end_time, 'yyyy-MM-dd HH:mm'),'yyyy-MM-dd HH:mm') : 'global_unlimit';
				   }
			   });
			  
			   $scope.classlist = item.childrens;

		   } 
		   $scope.course = item;		   
		   $scope.snsCount = data.snsCount;
		   
		   $scope.courseLoaded = true;
		   
		   //课程学习内容权限拦截
		   
		   var enrol = function(itm_id, itm_type) {
			   
			   if(!window.enrolFlag){//防止重复点击
				   window.enrolFlag = true;
				   if (itm_id > 0) {
						if(appInd){
							var wait = plus.nativeUI.showWaiting();
				    	}else{
				    		app.showWaiting();
				    	}
						Store.remove("tkhId");
						var url = '/app/application/app?itm_id=' + itm_id;
						Ajax.post(url, null, function(data){
							if (data.status == -1 && data.msg) {
								if(appInd){
									wait.close();
								}else{
									app.dismissWaiting();
								}
						    	alertService.add('danger', data.msg);
						    	window.enrolFlag = false;
							} else {
								if(appInd){
									changeWebviewDetail('courseIndex',function(){
										wait.close();
										reload();
									});
								} else {
									reload();
								}
							}
						})
					}
			   }
		   };
			
		   var showContent = function(){
			   	$scope.contentPermitted = true;
		   };
		   $scope.isApplybtn = true;
		   $scope.isDislable = false;
		   $scope.contentTipDesc = "";
		   $scope.contentTipBtnText = "";
		   $scope.contentTipBtnAction = function(){
				enrol($scope.btnParams.itm_id , $scope.btnParams.itm_type);
		   };
		   if(!appStatus || "cos_app_notapp" == appStatus || 'Rejected'== appStatus || 'Withdrawn' == appStatus){//还没报名
				switch($scope.btnParams.btn){
					case 1 : //未报名，但报名成功后马上可以开始学习
						$scope.contentPermitted = false;
						$scope.contentTipBtnText = $scope.typeKinds + "_content_click_to_begin";//马上点这里开始学习
					break;
					case 4 : //未报名，但该课程是需要报名审批通过后才能学习的情况
						$scope.contentPermitted = false;
						/*$scope.contentTipDesc = "cos_content_apply_tip";*///该课程需要先提交学习申请，等管理员或上司审批通过才能开始学习
						$scope.contentTipBtnText =　"cos_content_click_to_apply" //马上申请！
					break;
					case 7:  //未报名，但名额已满
						$scope.contentPermitted = false;
						$scope.contentTipBtnText = "cos_content_waiting_tip";//客官，你来慢了，学习名额已满。
						$scope.isDislable = true;
					break;
					case 8:
						$scope.contentPermitted = false;
						$scope.contentTipBtnText = "cos_content_waiting_list";//放上等待名單
					break;
					case -1://未报名，但还没开始报名，或报名时间已过
						$scope.contentPermitted = false;
					//	$scope.contentTipDesc = "cos_content_not_permitted";//该课程目前不开发在线学习申请。
					//	$scope.contentTipBtnText =　"cos_content_click_to_view" //知道了！我只浏览一下
						$scope.contentTipBtnAction = showContent;
					break;
				}
		   }else{//已经报名的
				if( ("C" == appStatus || "F" == appStatus) && ($scope.btnParams.btn == 1 || $scope.btnParams.btn == 4)){////已完成（包括 ：已完成和未完成），但允许重复学习而且是允许报名的
					//在“状态”那个Tab下，显示一个按钮“再学一次”
					if(itm_retake_ind != 0){ //允许重读
						$scope.contentPermitted = true;
						$scope.canReStudy = true;//可以重新学习
					}
				}
/*				else if($scope.btnParams.btn == 5){ //已报名，但报名记录还在申批中。
					$scope.contentTipDesc = "cos_content_pending_tip";//客官，别急，你的申请还在等待审批中。
					$scope.contentTipBtnText =　"cos_content_click_to_view" //知道了！我只浏览一下
					$scope.contentTipBtnAction = showContent;
					$scope.contentPermitted = false;
				}*/
				else if($scope.btnParams.btn == 6){ //已报名，但还在等待队列
					$scope.contentTipDesc = "cos_content_waiting_tip";//客官，别急，目前学习人数太多，请耐心等待。
					$scope.contentTipBtnText =　"cos_cancel_enroll" //取消报名
					$scope.contentTipBtnAction = showContent;
					$scope.contentPermitted = false;
					$scope.isApplybtn = false;
				}
			}
		   
	   });
	   
	   $scope.preview_aicc_au = function(src_link){
		   url = getAiccAuUrl(src_link);
		   window.open(url);
	   }
	   
	   $scope.preview_scorm = function(src_link){
		   window.location.href = getScormUrl(src_link);
	   }
	   
	   if(appInd){
		   if(window.plus){
			   plus.storage.setItem("nowItmId" , '' + itmId);
		   } else {
			   document.addEventListener('plusready', function(){
				   plus.storage.setItem("nowItmId" , '' + itmId);
			   });
		   }
      }
       
   }]).controller('lessonController', ['$scope', '$http', 'Store', 'Ajax', 'alertService', function($scope, $http, Store, Ajax, alertService) {
	   //日程表
	   var itmId = app.getUrlParam("itmId") || Store.get("itmId"); 
   	   if(!itmId) {
	       alertService.add('danger', '出错啦！课程ID不能为空!');
   	   }
	   var url = "/app/course/lesson/" + itmId;
	   
	   Ajax.post(url, null, function(data){
		   $scope.lessons = data.lessons;
		   for(var i in $scope.lessons){
		   		var teachers = '';
		   		for(var j in $scope.lessons[i].userList){
		   			teachers += $scope.lessons[i].userList[j].usr_display_bil+',';
		   		}
		   		$scope.lessons[i].teachers = teachers=='' ? false : teachers.substring(0,teachers.length-1);
		   }
	   })
	   
   }]).controller("messageController", ['$scope', '$http', 'Store', 'Ajax', 'alertService', '$window', '$sce',
        function($scope, $http, Store, Ajax, alertService, $window, $sce) {
	   var isExam = app.getUrlParam("isExam");
	   $scope.isExam = isExam;
	   // 课程公告
	   $scope.isDetail = false;
	   var itmId = app.getUrlParam("itmId") || Store.get("itmId"); 
   	   if(!itmId) {
	       alertService.add('danger', '出错啦！课程ID不能为空!');
   	   }
	   var url = "/app/course/message/" + itmId;
	   Ajax.post(url, null , function(data){
		   if(data){
			   $scope.messages = data.messages;
		   }
	   });
	   
	   var message;
	   $scope.showDetail = function(msgId) {
		   angular.forEach($scope.messages, function(val,i){
			   if(val.msg_id == msgId) {
				   message = val;
				   $scope.isDetail = true;
				   return;
			   }
		   });
		   $scope.message = message;
	   }
	   //左右滑动
   	   $scope.showNear = function(forward){
   		   var index = 0;
		   angular.forEach($scope.messages, function(val, i){
			   if(val.msg_id == $scope.message.msg_id) {
				   message = val;
				   index = i;
				   $scope.isDetail = true;
				   return;
			   }
		   });
		   if(index+1 < $scope.messages.length && forward == 'next'){
			   $scope.message = $scope.messages[++index];
		   }
		   if(index-1 >= 0 && forward == 'prev'){
			   $scope.message = $scope.messages[--index];
		   }
   	   };
	   //返回
	   $scope.back = function(){
		   if($scope.isDetail) {
			   $scope.isDetail = false;
		   } else {
			   back();
		   }
	   }
	   
   }]).controller("instructorEvaluateController", ['$scope', '$http', 'Store', 'Ajax', 'alertService', '$window',
        function($scope, $http, Store, Ajax, alertService, $window) {
	   
	   var flag = true;
	   $(".xing li").mouseenter(function(){
			$(this).addClass("xing1").nextAll().removeClass("xing1");
			$(this).prevAll().addClass("xing1");
			$(this).parent().find("li").mouseout(function () {
				$(this).parent().find("li").removeClass("xing1");
			});
			flag = false;
		}).mouseout(function(){
			$(this).parent().find("li").removeClass("xing1");
		}).click(function(){
			$(this).parent().find("li").unbind("mouseout");
			$(this).addClass('xing1').prevAll().addClass("xing1");
			$(this).nextAll().removeClass("xing1");
			var index = ($(".xing li").index(this) + 1) % 5;
			if(index==0) {index = 5;}
			else if(index == 1 && flag){
				$(this).removeClass("xing1");
				index = 0;
			}
			var name = $(this).parent().next()[0].name;
			if(name == "itc_style_score"){
				$scope.itc_style_score = index;
			}else if(name == "itc_quality_score"){
				$scope.itc_quality_score = index;
			} else if(name == "itc_structure_score"){
				$scope.itc_structure_score = index;
			} else if(name == "itc_interaction_score"){
				$scope.itc_interaction_score = index;
			}
			flag = true;
			$scope.$apply();
		});
	   
	   var itmId = app.getUrlParam("itmId") || Store.get("itmId"); 
   	   if(!itmId) {
	       alertService.add('danger', '出错啦！课程ID不能为空!');
   	   }
	   var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId"); 
   	   if(!tkhId) {
	       alertService.add('danger', '出错啦！课程没有报名!');
   	   }
   	   var url = '/app/comment/instructorComment/' + itmId;
   	   Ajax.post(url, null, function(result){
   		    var data = result.instructorComment;
   		    if(data){
	      		$scope.itc_comment = data.itc_comment,
	      		$scope.itc_style_score = data.itc_style_score;
	      		$scope.itc_quality_score = data.itc_quality_score;
	      		$scope.itc_structure_score = data.itc_structure_score;
	      		$scope.itc_interaction_score = data.itc_interaction_score;
	      		if($scope.itc_style_score){
	      			if($scope.itc_style_score == 5){
	      				$("input[name='itc_style_score']").prev().children().addClass("xing1");
	      			}
	     			$("input[name='itc_style_score']").prev().children().eq($scope.itc_style_score).prevAll().addClass("xing1");
	     		}
	      		if($scope.itc_quality_score){
	      			if($scope.itc_quality_score == 5){
	      				$("input[name='itc_quality_score']").prev().children().addClass("xing1");
	      			}
	     			$("input[name='itc_quality_score']").prev().children().eq($scope.itc_quality_score).prevAll().addClass("xing1");
	     		}
	      		if($scope.itc_structure_score){
	      			if($scope.itc_structure_score == 5){
	      				$("input[name='itc_structure_score']").prev().children().addClass("xing1");
	      			}
	     			$("input[name='itc_structure_score']").prev().children().eq($scope.itc_structure_score).prevAll().addClass("xing1");
	     		}
	      		if($scope.itc_interaction_score){
	      			if($scope.itc_interaction_score == 5){
	      				$("input[name='itc_interaction_score']").prev().children().addClass("xing1");
	      			}
	     			$("input[name='itc_interaction_score']").prev().children().eq($scope.itc_interaction_score).prevAll().addClass("xing1");
	     		}
   		    }

   	   });
	   url = '/app/comment/instructor/' + itmId + '/' + tkhId;
	   $scope.saveForm = function(){

		   if ($scope.frmEvaluate.itc_style_score.$invalid){
		   		alertService.add('', 'instructor_error_input', 2000);
		   		return;
		   }
		   if ($scope.frmEvaluate.itc_quality_score.$invalid){
		   		alertService.add('', 'instructor_error_input', 2000);
		   		return;
		   }
		   if ($scope.frmEvaluate.itc_structure_score.$invalid){
		   		alertService.add('', 'instructor_error_input', 2000);
		   		return;
		   }
		   if ($scope.frmEvaluate.itc_interaction_score.$invalid){
		   		alertService.add('', 'instructor_error_input', 2000);
		   		return;
		   }
	       if ($scope.alerts.length){
	           return;
	       }

		   var data = {
		   		note : "",
		   		styleScore : $scope.itc_style_score,
		   		qualityScore : $scope.itc_quality_score,
		   		structureScore : $scope.itc_structure_score,
		   		interactionScore : $scope.itc_interaction_score,
		   		isMobile : 1
		   }
		   
		   Ajax.post(url, data, function(data){
			   if(data.status == 'success') {
				   //提交成功
			       alertService.add('', 'know_tip_content_success', 2000, function(){
					   if(appInd){
						   changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
			    				back();
			    			});
					   }else{
						   back();
					   }
			       });
			   }else{
				   alertService.add('', data.msg, 2000);
			   }
		   });
	   }
	   
   }]).controller("instructorCommentController", ['$scope', '$http', 'Loader', 'Ajax', 'alertService', '$window',
       function($scope, $http, Loader, Ajax, alertService, $window) {
	   //
	   var id = app.getUrlParam("id");
   	   if(!id) {
	       alertService.add('danger', 'error instructor id is null!');
	       return;
   	   }
	   var url = "/app/comment/instructor/" + id;
       $scope.comments = new Loader(url, {});
	   
	   
   }]);
   
   function toogleDir(){
       $(".header-guide").slideToggle();
       $(".header-overlay").toggleClass("show");
	   $(".guide-menu-cont").css("height",$(".guide-menu-list-1").height());
	   $("#main").toggle();
   }
   
   //把秒转成时间格式显示
   function getTime(ms) {
   		var sec=0,min=0,hour=0,day=0;
		if (ms > 0) {
			// 秒
			sec = (ms / 1000);
			// 分
			if (sec >= 60) {
				min = Math.floor(sec / 60);
				sec = (sec % 60);
			}
			// 时
			if (min >= 60) {
				hour = Math.floor(min / 60);
				min = min % 60;
			}
			// 天
			if (hour >= 24) {
				day = Math.floor(hour / 24);
				hour = hour % 24;
			}
			if (hour < 10) {
				hour = "0" + hour;
			}
			if (min < 10) {
				min = "0" + min;
			}
			if (sec < 10) {
				sec = "0" + sec;
			}
		}
		return hour + ":" + min + ":" + Math.round(sec); //秒数取整，四舍五入

	}
   
});


function _wbModuleGetTime(datetime){

	if ( datetime.getMonth() > 9 )
		month = datetime.getMonth() + 1
	else
		month = '0' + (datetime.getMonth() + 1 )

	if ( datetime.getDate() > 9 )
		day = datetime.getDate()
	else
		day = '0' + datetime.getDate()

	if ( datetime.getHours() > 9 )
		hour = datetime.getHours()
	else
		hour = '0' + (datetime.getHours() + 1 )

	if ( datetime.getMinutes() > 9 )
		minutes = datetime.getMinutes()
	else
		minutes = '0' + datetime.getMinutes()

	if ( datetime.getSeconds() > 9 )
		seconds = datetime.getSeconds()
	else
		seconds = '0' + datetime.getSeconds()

	time = datetime.getFullYear() + month + day + hour  + minutes  + seconds

	return time;

}
function getAiccUrl(sco_ver) {
	var result = null;
	var servlet = '';
	if (sco_ver && sco_ver == '2004') {
		servlet = "SCO2004CMI";
	} else {
		servlet = "CMI";
	}
	
	var virtual_path_loc = self.location.pathname.indexOf('/app/');
	if (virtual_path_loc === -1) {
		virtual_path_loc = self.location.pathname.indexOf('/servlet/');
	}
	if (virtual_path_loc > -1) {
		var virtual_path = self.location.pathname.substring(0, virtual_path_loc);
		result = encodeURIComponent(self.location.protocol + "//" + self.location.host + virtual_path + "/servlet/" + servlet + "?");
	} else {
		result = encodeURIComponent(serverHost+"/servlet/" + "CMI" + "?");
	}
	return result;
}

function getScormPlayer(sco_ver, is_mobile) {
	if(is_mobile){
		if (sco_ver == '2004') {
			return serverHost + '/scorm2004Player/player_mobile.html';
		} else {
			return serverHost + '/htm/sco_player_mobile.htm';
		}
	}else{
		if (sco_ver == '2004') {
			return serverHost + '/scorm2004Player/player.html';
		} else {
			return serverHost + '/htm/sco_player.htm';
		}
	}	
}
function getScormUrl(play_style,src_link, ent_id, cos_id, mod_id, tkh_id, sco_version){
	if(src_link.indexOf('../') >= 0){
		src_link = "/"+ src_link.substring(src_link.lastIndexOf('../') + 3, src_link.length)	
	}
	today = new Date
	today = _wbModuleGetTime(today)

	var aicc_url = getAiccUrl();
	if (aicc_url === null) {
		return;
	}
	if (sco_version == '2004') {
		//thisURL="url=courses/course1/imsmanifest.xml&cid=21321123&uid=test01";
		//url为课件根目录下的imsmanifest.xml；cid为整个课件ID；uid为学员ID；
		//如果没有url，不能够打开课件；如果没有cid或uid，则为浏览模式，非正常学习；
		thisURL = '..' + escape(src_link) + '?cid=' + mod_id + "&uid=" + tkh_id;
		url = getScormPlayer(sco_version, true) + "?url=" + thisURL + "&play_style=" + play_style;/// + "&rm=" + Math.random()
	} else {
		var mod_vendor = "", is_multi_sco = "";
		var aicc_sid = null;
		if (ent_id && cos_id && mod_id && tkh_id) {
			aicc_sid = escape(ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:');
		} else {
			aicc_sid = 'VIEWONLY';
		}
		url = getScormPlayer(sco_version, true) + '?env=wizb&mod_id='+ mod_id+ '&cos_url=' + escape(src_link) + '&aicc_sid=' + aicc_sid + '&aicc_url=' + aicc_url;

		url = url + '&cos_id=' + cos_id;
		url = url + '&tkh_id=' + tkh_id;
		url = url + '&mod_id=' + mod_id;
		url = url + '&is_multi_sco=' + is_multi_sco;
		url = url + '&play_style=' + play_style;
	}
	
	url = url + '&lang=' + window.localStorage.getItem("globalLang");
	
	return url;
}

function getAiccAuUrl(src_link, ent_id, cos_id, mod_id, tkh_id, mod_vendor){
	
	var appPath=window.document.location.href;  
	var src_path = '';
	//判断是否已封装成App
	if(appInd){
		src_path = serverHost;
	}else{
		//获取主机地址之后的目录，如： uimcardprj/share/meun.js  
		var pathName=window.document.location.pathname;  
		var pos=appPath.indexOf(pathName);  
		src_path=appPath.substring(0,pos);
	}
   if(src_link.indexOf('../') >= 0){
    	src_link = '/' + src_link.substring(src_link.lastIndexOf('../') + 3, src_link.length)	
    }
	today = new Date
	today = _wbModuleGetTime(today)
	
	var aicc_url = getAiccUrl();
	if (aicc_url === null) {
		return;
	}
	var aicc_sid = null;
	if (ent_id && cos_id && mod_id && tkh_id) {
		aicc_sid = escape(ent_id + ':_:_:' + cos_id + ':_:_:' + mod_id + ':_:_:' + mod_vendor + ":_:_:" + today + ':_:_:' + tkh_id + ':_:_:');
	} else {
		aicc_sid = 'VIEWONLY';
	}
	url = src_path + src_link + '?aicc_sid=' + aicc_sid + '&aicc_url=' + aicc_url;
	return url
}

function alertText(text){
	var domElt = $("body")
	scope = angular.element(domElt).scope();
	scope.$apply(function(){
		alertObj.add('danger', text, 2000);
	})
}

//下载学习
function download(tkhId, resId, itmTitle, resource){
	var webview = plus.webview.getWebviewById("offline");
	if(webview == undefined){
		webview = plus.webview.create("../module/offline.html","offline",{scrollIndicator:'none',scalable:false});
	}
	
	var url = "";
	
	if(resource.res_src_type && resource.res_src_type == "URL" && resource.res_src_link){//网址
		
		url = resource.res_src_link;
		
	}else{//文件上传
		
		url = serverHost + '/resource/' + resource.res_id + '/' + resource.res_src_link;
		
	}
	
	
	$.ajax({
		url : url,
	 	type : 'HEAD',
		success: function() {
			var key = 'tkhId' + tkhId + resource.res_id;
			var userId = plus.storage.getItem("loginUserId");
			var modKey = plus.storage.getItem("modKey" + userId) == null ? "" : plus.storage.getItem("modKey" + userId);
			if(modKey.indexOf(key) < 0){
				var data = {
					isMobile : true,
					tkh_id : tkhId,
					res_id : resId,
					mod_id : resource.res_id,
					src : resource.res_src_link,
					modTitle : resource.res_title,
					itmTitle : itmTitle,
					required_time : resource.mod.mod_required_time,
					totalDuration : 0,
					duration : 0,
					last_time : ''
				}
				webview.evalJS("createDownload('" + key + "','" + JSON.stringify(data) + "')");
			} else {
				alertObj.add('danger', 'mod_down_arleady');
			}
		},
		error : function() {
			alertObj.add('danger', 'mod_down_file_null');
		}
	});
}


//换行调用
function MreplaceVlaue(str){
	if(str!=undefined && str!='')
	{
		str=str.replace(/\r\n/g,"<br/>").replace(/\r/g,"<br/>").replace(/\n/g,"<br/>").replace(/\\r?\\n?/g,"<br/>");
	}
	return str;
}
//转义&amp;
function MreplaceModValue(str){
	if(str!=undefined && str!=''){
		str = str.replace(/&amp;/g,"&").replace(/&gt;/g,'>').replace(/&lt;/g, "<").replace(/&quot;/g,"\"");
	}
	return str;
}