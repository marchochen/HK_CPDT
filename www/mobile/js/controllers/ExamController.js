$.include([
    'js/directives/CatalogDirective.js',
    'js/services/UserService.js'
], '../../');

$(function(){
	
   var examModule = angular.module('exam', ['globalCwn', 'CatalogDirective','userService']);
   
   var params = {
   };
	   
   //课程类型
   var course_type = [];
   course_type.push({id : 'all', value:'', title : 'cos_type_all', select : true});
   course_type.push({id : 'selfstudy', value : 'selfstudy', title : 'exam_type_selfstudy'});
   course_type.push({id : 'classroom', value : 'classroom', title : 'exam_type_classroom'});
   
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

   examModule.controller('catalogController', ['$scope', '$http', 'Loader', 'Ajax', '$translate', function($scope, $http, Loader, Ajax, $translate) {
   	   var url = "/app/exam/catalogCourseJson";

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
		var url = "/app/exam/recommendJson";
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
   	   var url = "/app/exam/signupJson";
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
       
   }]).controller('examController', ['$scope', '$http', 'Loader', 'Ajax', 'User', function($scope, $http, Loader, Ajax, User) {

   	   
        $scope.serverHost = serverHost;
   	   
		//用户信息
		User.userInfo(function(data){
			$scope.serverHost = serverHost;
			if(data && data.regUser){
				$scope.user = data.regUser;
			}
		});
		
		//统计信息
		var url = "/app/course/signup/count";
		Ajax.post(url, {isExam : 1}, function(data){
			 var count = data.count;
			 $scope.signupCount = count > 99 ? 99 : count;
		})
		
       
   }]);
   
   function toogleDir(){
       $(".header-guide").slideToggle();
       $(".header-overlay").toggleClass("show");
	   $(".guide-menu-cont").css("height",$(".guide-menu-list-1").height());
     $('#main').toggle();
   }
});
















