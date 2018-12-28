$.include([
	'js/directives/CatalogDirective.js',
	'js/common/courseAndExamCommon.js'
], '../../');

$(function(){
	
	var couseCatalogModule = angular.module('learning', ['globalCwn','CatalogDirective']);
	
	couseCatalogModule.controller("courseCatalogDetailController",function($scope,Store,Ajax,Loader){
		
		$scope.cur_lan_en = Store.get("globalLang");
		$scope.serverHost = serverHost;
		var cosType = app.getUrlParam("cosType");
		$scope.cosType = cosType;
	   	var firstLevel = app.getUrlParam("firstLevel");
	   	$scope.firstLevel = firstLevel;
		var secondLevel = app.getUrlParam("secondLevel");
		
		if(firstLevel || secondLevel){
			var subCtgUrl = "/app/catalog/getSubCtg";
			var params = {};
			
			if(firstLevel){
				params.tndId = firstLevel; 
			}
			
			if(secondLevel){
				params.tndId = secondLevel;
			}
			
			if(cosType){
				params.cos_type = cosType;
			}
			
			$scope.selectedItem = secondLevel;//记录当前选中的二级目录
			
			Ajax.post(subCtgUrl,params,function(data){
				$scope.subCatalogs = data.catalog;
				$scope.target = data.target;
				if($scope.selectedItem != null){					
					if(firstLevel && $scope.subCatalogs.length>0){
						for(var i = 0;i < $scope.subCatalogs.length;i++){
							var sc = $scope.subCatalogs[i];
							if(sc.tnd_id != null && $scope.selectedItem == sc.tnd_id){
								$scope.target.tnd_title = sc.tnd_title;
								$scope.subCatalogs = null;
								break;
							}
						}
					}					
				}
			});
		}
		
		if(cosType === "exam"){
			$scope.itemTypes = courseAndExamModule.getExamTypes();//考试类型集合
		}else{
			$scope.itemTypes = courseAndExamModule.getItemTypes();//课程类型集合
		}
		
		var url = "/app/course/catalogCourseJson";
		if(cosType && cosType === "exam"){
			url = "/app/exam/catalogCourseJson";
		}
		var params = {
		   pageSize : '4',
		   showMobileOnly:true,
		   isMobile : 1
	    };
		
		if(secondLevel){
			params.tndId = secondLevel;
		}else{
			params.tndId = firstLevel;
		}
		
		var loadCourse = function(url,params){
			$scope.catalogCourseLoader = new Loader(url, params,function(data){
				for(var i in data.items){
					var item = data.items[i];
					item.encItmId = wbEncryptor.encrypt(item.itm_id);
					item.cla = courseAndExamModule.getItemIcon(cosType,item.itm_type);
				}
				if($scope.selectedItem != null){					
					if(firstLevel && $scope.subCatalogs.length>0){
						for(var i = 0;i < $scope.subCatalogs.length;i++){
							var sc = $scope.subCatalogs[i];
							if(sc.tnd_id != null && $scope.selectedItem == sc.tnd_id){
								$scope.target.tnd_title = sc.tnd_title;
								$scope.subCatalogs = null;
								break;
							}
						}
					}					
				}
			});
		};
		
		loadCourse(url,params);
        
        var itemType = "";
		
		$scope.selectItemType = function(id){
           for (var index in $scope.itemTypes) {
        	   if($scope.itemTypes[index].id == id){
        		   $scope.itemTypes[index].select = true;
        		   itemType = $scope.itemTypes[index].value;
        	   } else {
        		   $scope.itemTypes[index].select = false;
        	   }
           }
           
           reload();
        };
        
        $scope.selectCatalog = function(tndId){
        	$scope.selectedItem = tndId;
        	reload();
        };
        
        
        var reload = function(){
			if($scope.selectedItem){
				params.tndId = $scope.selectedItem;
			}
			params.itemType = itemType;
			loadCourse(url,params);
        };
		
	});
});