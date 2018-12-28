$.include([
	'js/directives/CatalogDirective.js',
	'js/common/courseAndExamCommon.js'
], '../../');

$(function(){
	
	var couseCatalogModule = angular.module('cwn', ['globalCwn','CatalogDirective']);
	couseCatalogModule.controller("CourseCatalogController",function($scope,Store,Ajax,Loader,$filter){
		
		$scope.cur_lan_en = Store.get("globalLang");
		$scope.serverHost = serverHost;
		
		var courseCatalog = (function(){
			var courseCatalog = {};
			
			courseCatalog.load = function(){
				$scope.headerClass = "header-menu";
				//$scope.pageTitle = 'cos_catalog';
			};
			return courseCatalog;
		})();
		
		var myCourse = (function(){
			var myCourse = {};
			myCourse.load = function(params){
				$scope.headerClass = "header-sort";
				//$scope.pageTitle = 'my_course';
				var url = "/app/course/getMyCourse";
				params = params || {}; 
				$scope.myCourseLoader = new Loader(url,params,function(data){
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
			}
			return myCourse;
		})();
		
		var recommendCourse = (function(){
			var recommendCourse = {};
			recommendCourse.load = function(params){
				$scope.headerClass = "header-sort";
				//$scope.pageTitle = 'cos_recommend';
				var url = "/app/course/recommendJson";
				params = params || {}; 
				$scope.recommendCourseLoader = new Loader(url,params,function(data){
					for(var i in data.items){
						var item = data.items[i];
						item.item.cla = courseAndExamModule.getItemIcon("course",item.item.itm_type);
						item.item.encItmId = wbEncryptor.encrypt(item.item.itm_id);
					}
				});
			}
			return recommendCourse;
		})();
		
		$scope.itemTypes = courseAndExamModule.getItemTypes();//课程类型集合
		
		$scope.appStatus = courseAndExamModule.getSimpleAppStatus();//学习状态类型简单集合
		$scope.appStatusSelectItemId = 'all';//记录选中的id
		
		$scope.recommendTypes = courseAndExamModule.getRecommendTypes();//推荐类型集合

	    $scope.compulsoryTypes = courseAndExamModule.getCompulsoryTypes();//是否必修选修
		
		$scope.show = {}; //控制选项卡显示
		$scope.show.myCourse = false;
		$scope.show.courseCatalog = false;
		$scope.show.recommendCourse = false;
		var showType = app.getUrlParam('show');
		if (showType) {
			switch(showType){
			case 'myCourse' : 
				$scope.show.myCourse = true;
				myCourse.load({showMobileOnly:true});
				break;
			case 'courseCatalog' : 
				$scope.show.courseCatalog = true;
				courseCatalog.load({showMobileOnly:true});
				break;
			case 'recommendCourse' :
				var p = {showMobileOnly:true};
				$scope.show.recommendCourse = true;
				$scope.isCompulsory = app.getUrlParam('isCompulsory');//是否需要选择必修课
				if($scope.isCompulsory){
					p.isCompulsory = $scope.isCompulsory;
					
					for(var i=0;i<$scope.compulsoryTypes.length;i++){//选中
						var item = $scope.compulsoryTypes[i];
						if(item.value == $scope.isCompulsory){
							item.select = true;
							break;
						}
					}
				}
				recommendCourse.load(p);
				break;
			}
		}else{//默认我的课程
			$scope.show.myCourse = true;
		}
	    
		
		$scope.toggleSelection = function(){
			if($scope.show.courseCatalog){
				var api = $("#header-guide").data( "mmenu" );
				api.open();
				return;
			}
			if($scope.show.myCourse){
				$(".header-guide-box-toggle-myCourse").slideToggle();
			}else{
				$(".header-guide-box-toggle-recommendCourse").slideToggle();
			}
		};
	    
		$scope.selectItemType = function(id){
           for (var index in $scope.itemTypes) {
        	   if($scope.itemTypes[index].id == id){
        		   $scope.itemTypes[index].select = true;
        	   } else {
        		   $scope.itemTypes[index].select = false;
        	   }
           }
           sureCheck();
        };
       
        $scope.selectRecommendType = function(id) {
			for ( var index in $scope.recommendTypes) {
				if ($scope.recommendTypes[index].id == id) {
					$scope.recommendTypes[index].select = true;
				} else {
					$scope.recommendTypes[index].select = false;
				}
			}
			sureCheck();
		};
		
		$scope.catalogSelectCallback = function(catalog){
			$scope.catalogselectTarget = catalog;//记录选中的目录
		}
		
		$scope.selectCompulsoryType = function(id) {
			for ( var index in $scope.compulsoryTypes) {
				if ($scope.compulsoryTypes[index].id == id) {
					$scope.compulsoryTypes[index].select = true;
				} else {
					$scope.compulsoryTypes[index].select = false;
				}
			}
			sureCheck();
		};
       
        $scope.selectAppStatus = function(id){
    	    $scope.appStatusSelectItemId = id;
    	    sureCheck();
        }
		
		var sureCheck = function(){
			
		   var itemType,appStatus,compulsoryType,recommendType;//目录，课程类型，学习状态, 是否必修，推荐类型
		   
           for (var index in $scope.itemTypes) {
        	   if($scope.itemTypes[index].select){
        		   itemType = $scope.itemTypes[index].value;
        		   break;
        	   }
           }
           
           for(var index in $scope.appStatus){
    		   if($scope.appStatus[index].id == $scope.appStatusSelectItemId){
    			   appStatus = $scope.appStatus[index].value;
    			   break;
    		   }
    	   }
           var params = {
      		     'showMobileOnly':true,  
      		      'pageSize':4
      		      };
    	   if(itemType){
    		   params.itemType = itemType;
    	   }
    	   if(appStatus){
    		   params.appStatus = appStatus;
    	   }
    	   //我的课程
           if($scope.show.myCourse){
        	   params.orderByPubType = true;
        	   myCourse.load(params);
        	   return;
           }
           //推荐课程
           for (var index in $scope.compulsoryTypes) {
        	   if($scope.compulsoryTypes[index].select){
        		   compulsoryType = $scope.compulsoryTypes[index].value;
        		   break;
        	   }
           }
           for (var index in $scope.recommendTypes) {
        	   if($scope.recommendTypes[index].select){
        		   recommendType = $scope.recommendTypes[index].value;
        		   break;
        	   }
           }
           
           if(compulsoryType){
    		   params.isCompulsory = compulsoryType;
    	   }
    	   if(recommendType){
    		   params.selectType = recommendType;
    	   }
    	   params.showMobileOnly = true;
           recommendCourse.load(params);
       };
       
       $scope.selectedCallback = function(pane){
    	   switch(pane.index){
    	   case 0 :
    		   $scope.show.myCourse = true;
    		   $scope.show.courseCatalog = false;
    		   $scope.show.recommendCourse = false;
    		   myCourse.load({showMobileOnly:true});
    		   break;
    	   case 1 : 
    		   $scope.show.myCourse = false;
    		   $scope.show.courseCatalog = true;
    		   $scope.show.recommendCourse = false;
    		   courseCatalog.load({showMobileOnly:true});
    		   break;
    	   case 2 : 
    		   $scope.show.myCourse = false;
    		   $scope.show.courseCatalog = false;
    		   $scope.show.recommendCourse = true;
    		   recommendCourse.load({showMobileOnly:true});
    		   break;
    	   }
       };
       
	});
});