$.include([
      'js/services/InstructorService.js',
      'js/common/courseAndExamCommon.js'
],'../../');

$(function(){
	
	var instructorModule = angular.module("instructorModule",['globalCwn']);
	
	instructorModule.controller("listController",['$scope','InstructorService',function($scope,InstructorService){
		
		$scope.serverHost = serverHost;
		
		$scope.selectedLevel = "";
		$scope.instructorLevels = InstructorService.getInstructorLevels();
		$scope.selectedType = "";
		$scope.instructorTypes = InstructorService.getInstructorTypes();
		$scope.selectedSource = "";
		$scope.instructroSources = InstructorService.getInstructroSources();
		
		$scope.load = function loadF(){
			var param = {'iti_level':$scope.selectedLevel,'iti_type':$scope.selectedType,'iti_type_mark':$scope.selectedSource};
			$scope.instructorListLoader = InstructorService.getInstructorLoader(param);
			return loadF;
		}();
		
		$scope.toggleSelect = function(){
			$(".header-guide").slideToggle();
		};
		
		$scope.selectLevel = function(level){
			$scope.selectedLevel = level;
			$scope.load();
		};
		
		$scope.selectType = function(type){
			$scope.selectedType = type;
			$scope.load();
		};
		
		$scope.selectSource = function(source){
			$scope.selectedSource = source;
			$scope.load();
		};
		
		$scope.toDetail = function(id){
			javascript:clicked('instructorDetail.html?id='+id,true);
		};
		
	}]);
	
	instructorModule.controller("detailController",['$scope','Ajax','Loader','Store','InstructorService','$filter',function($scope,Ajax,Loader,Store,InstructorService){
		
		$scope.serverHost = serverHost;
		var id = app.getUrlParam("id");
		
		$scope.globalLang = Store.get("globalLang");
		
		//1获取详情信息
		InstructorService.getInstructorDetail(id,function(data){
			$scope.instructroDetail = data.instructorInf;
		});
		
		//2获取开课列表
		$scope.courseListLoader = InstructorService.getInstructorCourseListLoader(id);
		
	}]);
	
});