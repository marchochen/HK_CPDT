$.include([
    'lib/angular/angular-route.min.js'
], '../../');
$(function(){
	var searchModule = angular.module('search', ['ngRoute', 'globalCwn']);
	
	searchModule.config(['$routeProvider',function($routeProvider){
		$routeProvider.when('/index', {controller: 'IndexController', template : 
	    	'<div class="cont-info-title-3">{{"sch_can_search" | translate}}</div>'+
	    	'<div class="souso-tag clearfix"> '+
	            '<span class="souso-tag-1"  ng-repeat="type in types" >'+
	                '<span class="icon-nav-model">'+
	                    '<i class="{{type.icon}}"></i>'+
	                '</span>'+
	                '<div class="nav-box-biao" translate="{{type.name}}"></div>'+
	            '</span>'+
	    	'</div>'
	    });
	    $routeProvider.when('/list', {controller: 'ListController', template: 
		  '<div class="cont-list-tuwen"><div class="panel" ng-repeat="item in result | orderBy:\'name\'" ng-show="item.first || (item.name == \'All\' && item.count > 0)">'+
			   '<div class="panel-title-4">'+
			   	'<a class="" title="" ng-click="getMore(item.name)"><em class="panel-num-1">{{item.count}}</em>{{\'sch_type_\' + item.name | lowercase | translate}}</a>'+
			   '</div>'+ 
			   '<div class="tuwen-list-1" ng-show="item.first">'+
			    '    <a class="tuwen-list-1-box clearfix" href="{{item.first.url}}" title="">'+
			    '       <div class="tuwen-list-1-pic" ng-show="item.first.photo"><img ng-src="{{serverHost}}{{item.first.photo}}" alt=""/></div>'+
			     '      <div class="tuwen-list-1-tit-1"><i class="wzb-pc-icon" ng-show="(item.first.type == \'Course\' || item.first.type == \'Exam\')"></i><i class="wzb-phone-icon" ng-show="(item.first.type == \'Course\' || item.first.type == \'Exam\')&& (item.first.itm_mobile_ind && item.first.itm_mobile_ind == \'yes\' )"></i>{{item.first.title}}</div>'+
			     '      <p class="tuwen-list-1-tit-3" ng-show="item.first.introduction">{{item.first.introduction}}</p>'+
			     '   </a>'+
			   '</div>'+
		  ' </div>' +
		  '<div class="panel-list-more" ng-show="isBusy"><img width="20px" height="20px;" src="../../images/loading.gif"/>&nbsp;{{"loader_loading" | translate}}</div>'+
		  '<div class="panel-list-boxdate" ng-show="isAllEmpty && !isBusy"><div class="panel-list-nodate">{{"loader_no_data" | translate}}</div></div></div>'
	    });
	    $routeProvider.when('/detail/:type', {controller: 'DetailController', template:
	    	'<div class="panel-title-5">{{"sch_result_count" | translate: {value : detail.totalRecord} }}</div> '+

	    	'<div class="cont-list-tuwen"><div class="tuwen-list-1" ng-repeat="item in detail.items">'+
	    	'     <a class="tuwen-list-1-box clearfix" href="{{item.url}}" title="">'+
	    	'        <div class="tuwen-list-1-pic" ng-show="item.photo"><img ng-src="{{serverHost}}{{item.photo}}" alt=""/></div>'+
	    	      
	    	'        <div class="tuwen-list-1-tit-1"><em class="tuwen-list-1-tip">{{\'sch_type_\' + item.type | lowercase | translate}}</em>{{item.title}}</div>'+
	    	'        <p class="tuwen-list-1-tit-3" ng-show="item.introduction">{{item.introduction}}</p>'+
	    	'     </a>'+
	    	'</div></div>'+
	    	'<div loader="detail"></div>'
	    });
	    $routeProvider.otherwise({redirectTo: '/index'});
	}]);
	
	//搜索页面主控制器
	searchModule.controller('searchController', ['$scope', '$location', '$filter', 'Ajax', 'alertService',
	    function($scope, $location, $filter, Ajax, alertService) {
	    	$scope.searchText = '';
	    	$scope.showDel = true;
	    	$scope.$watch('searchText', function(){
	    		if($scope.searchText == ''){
	    			$scope.showDel = false;
	    		}else{
	    			$scope.showDel = true;
	    		}
	    	});

	    	$scope.delText = function(){
	    		$scope.searchText = '';
	    	};
			
		    $scope.serverHost = serverHost;
		    
			$scope.doSearch = function() {
				 var searchText = $scope.searchText;
				 if(!searchText) {
				     alertService.add('', 'sch_can_not_empty', 1000);
					 return;
				 } else {
					 $location.path("list");
				 }
				 $scope.isBusy = true;
				 var url = "/app/search/searchCount?isMobile=1&showMobileOnly=true&searchText=" + searchText;
				 Ajax.post(url, null, function(data){
					var result = [];
					var isAllEmpty = true;
					if(data && data.hashMap){
						for(name in data.hashMap) {
							var list = data.hashMap[name].results;
							var items = [];
							angular.forEach(list, function(val, i){
								var enc_id = wbEncryptor.encrypt(val.id);
								url = "";
								if(val.type == "Article"){
									url = $filter('articleUrl')(enc_id, true)
								} else if(val.type == "Exam" || val.type == "Course"){
									url = $filter('courseUrl')({itmId: enc_id, examInd : true})
								} else if(val.type == "Answer"){
									//这里的val.id不需要加密，在knowUrl已经加好了
									url = $filter("knowUrl")({que_id:val.id, que_type : 'all', annotherTnd : true});
								} else if(val.type == "Open"){
									url = $filter("openCourseUrl")({itmId:enc_id, annotherTnd : true});
								} else if(val.type == "Group"){
									url = $filter("groupUrl")(enc_id);
								} else if(val.type == "Message"){
									url = 'javascript:clicked("../announce/announce.html?id=' + enc_id + '&isReturn=true",true);';
								} else if(val.type == "Contacts"){
									url = $filter("personUrl")(enc_id);
								} else if(val.type == "Knowledge"){
									url = $filter("kbDetailUrl")(enc_id, true);
								}
								val.url = url;
								items.push(val);
							})
							list = items;
							
							var count = data.hashMap[name].totalRecord;
							if(count > 0) isAllEmpty = false;
							if(name == 'All') list = null;
							result.push({
								name : name,
								count : count,
								first : list?list[0]:null
							});
						}
					}
					$scope.result = result;
					$scope.isAllEmpty = isAllEmpty;
					$scope.isBusy = false;
				});				 
			};
			$scope.search = function(text){
				$scope.searchText = $filter('translate')(text);
				$scope.doSearch();
			}
		}
	//默认显示type的控制器
	]).controller('IndexController', ['$scope', '$window',
	    function($scope, $window) {
			$scope.$parent.searchText = "";

			$scope.$parent.back = function(){
				if(appInd){
					back();
				} else {
					back();
				}
			}		
			$scope.types = [
				{name : 'sch_type_message', icon : 'icon-nav-gong'},
				{name : 'sch_type_article', icon : 'icon-nav-zhang'},
				{name : 'sch_type_course', icon : 'icon-nav-cheng'},
				/*{name : 'sch_type_exam', icon : 'icon-nav-shi'},*/
				/*{name : 'sch_type_answer', icon : 'icon-nav-wen'},*/
				/*{name : 'sch_type_group', icon : 'icon-nav-qun'},*/
				{name : 'sch_type_open', icon : 'icon-nav-kai'},
				/*{name : 'sch_type_contacts', icon : 'icon-nav-renmai'},*/
				/*{name : 'sch_type_knowledge', icon : 'icon-nav-zhongxin'}*/
			];			               
		}
	//统计列表（按类型分组）
	]).controller('ListController', ['$scope', '$http', 'Ajax', '$location',
	    function($scope, $http, Ajax, $location) {
			//$scope.result = [];
			$scope.$parent.back = function(){
				$scope.searchText = "";
//				$location.path('index');
				back();
			}
			$scope.getMore = function(type){
				$location.path('detail/' + type.toLocaleLowerCase());
			}
		}
	//列表详情
	]).controller('DetailController', ['$scope', '$http', 'Loader', '$routeParams', '$location', '$filter',
	    function($scope, $http, Loader, $routeParams, $location, $filter) {
/*		sch_type_course : '课程',
		sch_type_exam : '考试',
		sch_type_announce : '公告',
		sch_type_article : '文章',
		sch_type_answer : '问答',
		sch_type_open : '公开课',
		sch_type_group : '群组',
		sch_type_message : '消息',
		sch_type_contacts : '人脉',*/
		 	var searchText = $scope.searchText;
			var url = "/app/search/Json";
			var params = {
				type : $routeParams.type[0].toUpperCase() + $routeParams.type.slice(1),
				isMobile : 1,
				searchValue : searchText,
                showMobileOnly : true
			}
			var detail = new Loader(url, params, function(dt){
				var items = [];
				angular.forEach(dt.items, function(val, i){
					url = "";
					var enc_id = wbEncryptor.encrypt(val.id);
					if(val.type == "Article"){
						url = $filter('articleUrl')(enc_id, true)
					} else if(val.type == "Exam" || val.type == "Course"){
						url = $filter('courseUrl')({itmId: enc_id, examInd : true})
					} else if(val.type == "Answer"){
						//这里的val.id不需要加密，在knowUrl已经加好了
						url = $filter("knowUrl")({que_id:val.id, que_type : 'all', annotherTnd : true});
					} else if(val.type == "Open"){
						url = $filter("openCourseUrl")({itmId:enc_id, annotherTnd : true});
					} else if(val.type == "Group"){
						url = $filter("groupUrl")(enc_id);
					} else if(val.type == "Message"){
						url = 'javascript:clicked("../announce/announce.html?id=' + enc_id + '&isReturn=true",true);';
					} else if(val.type == "Contacts"){
						url = $filter("personUrl")(enc_id);
					} else if(val.type == "Knowledge"){
						url = $filter("kbDetailUrl")(enc_id, true);
					}
					val.url = url;
					items.push(val);
				})
				detail.items = items;
			});
			$scope.detail = detail;
			
			$scope.$parent.back = function(){
				back();
			}
		}
	]);
	
});