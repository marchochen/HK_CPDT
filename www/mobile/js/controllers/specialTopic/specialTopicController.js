$.include([
	'js/services/InstructorService.js',
	'js/directives/SwiperDirective.js'
],'../../');

$(function(){
	
	var specialTopicModule = angular.module("specialTopicModule",['globalCwn','wzmSwiper']);
	
	specialTopicModule.controller("listController",['$scope','Ajax','Loader',function($scope,Ajax,Loader){
		
		$scope.serverHost = serverHost;
		
		//1.获取推送到首页的专题培训作为轮播图
		var url = "/app/learningmap/getSpecialListIndex";
		Ajax.post(url,{ust_showindex:1},function(data){
			
			for(var i=0;i<data.length;i++){
				data[i].enc_ust_id = wbEncryptor.encrypt(data[i].ust_id);
			}
			
			$scope.specialListForIndex = data;
			if(data.length == 1){
				$scope.special = data[0];
			}
		});
		
		//2.获取专题培训分页列表
		url = "/app/learningmap/getSpecialPageJson";
		$scope.specialListLoader = new Loader(url,{},function(data){
			for(var i=0;i<data.items.length;i++){
				var item = data.items[i];
				item.enc_ust_id = wbEncryptor.encrypt(item.ust_id);
			}
		});
		
		//链接到专题详情
		$scope.toDetail = function(ust_id){
			var url = "specialTopicDetail.html?ust_id="+ust_id;
			javascript:clicked(url,true);
		}
		
	}]);
	
	specialTopicModule.controller("detailController",['$scope','Ajax','Loader','Store','InstructorService','$sce',function($scope,Ajax,Loader,Store,InstructorService,$sce){
		
		$scope.serverHost = serverHost;
		var ust_id = app.getUrlParam("ust_id");
		
		$scope.globalLang = Store.get("globalLang");
		
		//1 获取专题培训详情
		var url = "/app/learningmap/specialDetailJson";
		Ajax.post(url,{ust_id:ust_id},function(data){
			$scope.specialDetail = data;
			$scope.specialDetail.ust_content = $sce.trustAsHtml($scope.specialDetail.ust_content);
		});
		
		//2 获取专题培训讲师
		url = "/app/learningmap/specialExpertsJson";
		Ajax.post(url,{ust_id:ust_id},function(data){
			
			$scope.specialExperts = data;
			for(var j=0;j<$scope.specialExperts.length;j++){
				var item = $scope.specialExperts[j];
				
				item.enc_use_ent_id = wbEncryptor.encrypt(item.use_ent_id);
				
				//星星 
				if(item.iti_score == undefined){
					item.iti_score = 0;
				}
				item.iti_score_star = [];
				item.iti_score_star_2 = [];
				for(var i = 0; i < Math.round(item.iti_score);i++){
					item.iti_score_star.push({"star":"start-w start-w-full"});
					item.iti_score_star_2.push({"star":"start-g start-g-full"});
				}
				for(var i = 0; i < 5 - Math.round(item.iti_score);i++){
					item.iti_score_star.push({"star":"start-w"});
					item.iti_score_star_2.push({"star":"start-g"});
				}
				item.iti_level = InstructorService.getInstructorLevelLabel(item.iti_level);
			}
			$scope.cssStyle = "";
			if($scope.specialExperts.length<4){
				var width = $scope.specialExperts.length * 120 - 20;
				$scope.cssStyle = "width:"+width+"px;margin:0 auto;";
			}
		});
		
		//3 获取专题课程
		url = "/app/learningmap/specialCourseJson";
		$scope.specialCourseLoader = new Loader(url,{ust_id:ust_id},function(data){
			for(var i=0;i<data.items.length;i++){
				var item = data.items[i];
				item.encItmId = wbEncryptor.encrypt(item.itm_id);
				item.cnt_comment_count = item.cnt_comment_count || 0;
				item.cnt_like_count = item.snsCount && item.snsCount.s_cnt_like_count ? item.snsCount.s_cnt_like_count : 0;
			}
		});
		
	}]);
	
});