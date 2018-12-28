$.include([
	'js/directives/CatalogDirective.js',
	'js/common/courseAndExamCommon.js'
], '../../');

$(function(){
	
	var couseCatalogModule = angular.module('cwn', ['globalCwn','CatalogDirective']);
	couseCatalogModule.controller("examController",function($scope,Store,Ajax,Loader){
		
		$scope.cur_lan_en = Store.get("globalLang");
		$scope.serverHost = serverHost;
		
		var examCatalog = (function(){
			var examCatalog = {};
			
			examCatalog.load = function(){
				//$scope.pageTitle = 'exam_catalog';
			};
			return examCatalog;
		})();
		
		var myExam = (function(){
			var myExam = {};
			myExam.load = function(params){
				//$scope.pageTitle = 'my_exam';
				var url = "/app/exam/getMyExam";
				$scope.myExamLoader = new Loader(url,params,function(data){
					for(var i in data.items){
						var app = data.items[i];
						
						app.item.cla = courseAndExamModule.getItemIcon("exam",app.item.itm_type);
						app.appStatusJson = courseAndExamModule.getAppStatusJson(app.app_status);
						
						app.item.encItmId = wbEncryptor.encrypt(app.item.itm_id);
						
						if(app.cov && app.cov.cov_progress){
							app.cov_progress = app.cov.cov_progress;
						}else{
							app.cov_progress = 10; 
						}
					}
					
				});
			}
			return myExam;
		})();
		
		var recommendExam = (function(){
			var recommendExam = {};
			recommendExam.load = function(params){
				//$scope.pageTitle = 'exam_recommend';
				var url = "/app/exam/recommendJson";
				$scope.recommendExamLoader = new Loader(url,params,function(data){
					for(var i in data.items){
						var item = data.items[i];
						item.item.cla = courseAndExamModule.getItemIcon("exam",item.item.itm_type);
						item.item.encItmId = wbEncryptor.encrypt(item.item.itm_id);
					}
				});
			}
			return recommendExam;
		})();
		
		$scope.show = {}; //控制选项卡显示
		$scope.show.myExam = false;
		$scope.show.examCatalog = false;
		$scope.show.recommendExam = false;
		var showType = app.getUrlParam('show');
		if (showType) {
			switch(showType){
			case 'myExam' : 
				$scope.show.myExam = true;
				myExam.load({orderByPubType:true,showMobileOnly:true});
				break;
			case 'examCatalog' : 
				$scope.show.examCatalog = true;
				examCatalog.load({showMobileOnly:true});
				break;
			case 'recommendExam' : 
				$scope.show.recommendExam = true;
				recommendExam.load({showMobileOnly:true});
				break;
			}
		}else{//默认我的考试
			$scope.show.myExam = true;
		}
		
		$scope.examTypes = courseAndExamModule.getExamTypes();//考试类型集合
		
		$scope.appStatus = courseAndExamModule.getSimpleAppStatus();//学习状态类型简单集合
		$scope.appStatusSelectItemId = 'all';//记录选中的id
		
		$scope.recommendTypes = courseAndExamModule.getRecommendTypes();//推荐类型集合

	    $scope.compulsoryTypes = courseAndExamModule.getCompulsoryTypes();//是否必修选修
	   	    
		$scope.toggleSelection = function(){
			if($scope.show.examCatalog){
				var api = $("#header-guide").data( "mmenu" );
				api && api.open();
				return;
			}
			if($scope.show.myExam){
				$(".header-guide-box-toggle-myExam").slideToggle();
			}else{
				$(".header-guide-box-toggle-recommendExam").slideToggle();
			}
		};
	    
		$scope.selectExamType = function(id){
           for (var index in $scope.examTypes) {
        	   if($scope.examTypes[index].id == id){
        		   $scope.examTypes[index].select = true;
        	   } else {
        		   $scope.examTypes[index].select = false;
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
		
		   var examType,appStatus,compulsoryType,recommendType;//目录，课程类型，学习状态, 是否必修，推荐类型
		   
           for (var index in $scope.examTypes) {
        	   if($scope.examTypes[index].select){
        		   examType = $scope.examTypes[index].value;
        		   break;
        	   }
           }
           
           if($scope.show.examCatalog){//课程目录
        	   
        	   var firstLevel = "";//选中的一级目录
        	   var secondLevel = "";//选中的二级目录
        	   
        	   if($scope.catalogselectTarget){
            	   if($scope.catalogselectTarget.tnd_parent_tnd_id == 0){
            		   firstLevel = $scope.catalogselectTarget.tnd_id;
            	   }else{
            		   firstLevel = $scope.catalogselectTarget.tnd_parent_tnd_id;
            		   secondLevel = $scope.catalogselectTarget.tnd_id;
            	   }
               }
        	   
        	   var catalogDetailUrl = "../learning/catalogDetail.html?cosType=exam&itemType="+examType;
               catalogDetailUrl += "&firstLevel="+firstLevel+"&secondLevel="+secondLevel;
               javascript:clicked(catalogDetailUrl,true);
               return;
           }
           
           for(var index in $scope.appStatus){
    		   if($scope.appStatus[index].id == $scope.appStatusSelectItemId){
    			   appStatus = $scope.appStatus[index].value;
    			   break;
    		   }
    	   }
           
           var params = {
   		    	showMobileOnly:true,
   		    	'pageSize':4
   		   	};
    	   if(examType){
    		   params.itemType = examType;
    	   }
    	   if(appStatus){
    		   params.appStatus = appStatus;
    	   }
           
           if($scope.show.myExam){//我的课程
        	   params.orderByPubType = true;
        	   myExam.load(params);
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
           recommendExam.load(params);
       };
       
       $scope.selectedCallback = function(pane){
    	   switch(pane.index){
    	   case 0 :
    		   $scope.show.myExam = true;
    		   $scope.show.examCatalog = false;
    		   $scope.show.recommendExam = false;
    		   myExam.load({orderByPubType:true,showMobileOnly:true});
    		   break;
    	   case 1 : 
    		   $scope.show.myExam = false;
    		   $scope.show.examCatalog = true;
    		   $scope.show.recommendExam = false;
    		   examCatalog.load({showMobileOnly:true});
    		   break;
    	   case 2 : 
    		   $scope.show.myExam = false;
    		   $scope.show.examCatalog = false;
    		   $scope.show.recommendExam = true;
    		   recommendExam.load({showMobileOnly:true});
    		   break;
    	   }
       };
		
	});
});