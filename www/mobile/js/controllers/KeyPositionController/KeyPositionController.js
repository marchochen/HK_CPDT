$.include([
],'../../');

$(function(){
	
	var KeyPositionModule = angular.module("KeyPositionModule",['globalCwn']);
	
	KeyPositionModule.controller("listController",['$scope','Ajax','UserService',function($scope,Ajax,UserService){

		$scope.serverHost = serverHost;
		
		//1.获取关键岗位的个数，我的岗位
		var url = "/app/learningmap/positionMapInfoJson";
		Ajax.post(url,{},function(data){
			$scope.keyPositionCount = data.count;//关键岗位的个数
			$scope.myPosition = data.title;
		});
		
		//2.获取当前用户
		//用户信息
		UserService.userInfo(function(data){
			$scope.serverHost = window.serverHost;
			if(data && data.regUser){
				$scope.user = data.regUser;
			}
		});
		
		//3.获取系数较高的关键岗位列表
		url = "/app/learningmap/positionMapListJson";
		Ajax.post(url,{},function(data){
			enc_upc_id(data);
			$scope.positionList = data;
		});
		
		//4.获取关键岗位类型列表
		url = "/app/learningmap/positionCatalogListJson";
		Ajax.post(url,{},function(data){
			enc_upc_id(data);
			$scope.positionCatalogList = data;
		});
		
		//跳转到岗位详情页面
		//@param cid 岗位类别id required
		//@param pid 岗位id optional
		$scope.toDetail = function(cid,pid){
			var url = "keyPositionDetail.html?cid="+cid + (pid ? "&pid="+pid : "");
			javascript:clicked(url,true);
		};
		
		function enc_upc_id(items){
			for(var i=0;i<items.length;i++){
				var item = items[i];
				item.enc_upc_id = wbEncryptor.encrypt(item.upc_id);
				item.enc_upt_id = wbEncryptor.encrypt(item.upt_id);
			}
		}
		
	}]);
	
	KeyPositionModule.controller("detailController",['$scope','Ajax','Loader','$filter','alertService',function($scope,Ajax,Loader,$filter,alertService){
		
		$scope.serverHost = serverHost;
		
		//1.获取岗位类型id
		var cid = app.getUrlParam("cid");//岗位类型id
		
		//2.获取具体关键岗位的id，如果没有传，则默认展示的是该岗位类型的第一个关键岗位
		var pid = app.getUrlParam("pid");//岗位id，注：如果没有传，则默认展示的是cid下面的第一个岗位
		
		//3.获取该岗位类型下的关键岗位列表（导航条）
		var url = "/app/learningmap/getPostMapListByUpcId?upc_id="+cid;
		Ajax.post(url,{},function(data){
			
			if(!data.positionLrnMaps || data.positionLrnMaps.length <= 0){
				alertService.add("warning",'map_position_no_key_tip',2000,function(){
					back();
				});
			}
			
			$scope.positionLrnMaps = data.positionLrnMaps;
			
			$scope.pageTitle = $scope.positionLrnMaps[0].upc_title || $filter("translate")("map_position_other_category");
			
			pid = pid || $scope.positionLrnMaps[0].upm_upt_id;//如果pid没有传，则取cid类型下第一个关键岗位（系数最高）对应学习地图的详细信息
			$scope.currentPid = pid;//记录当前选择的关键岗位

			for(var i=0;i<$scope.positionLrnMaps.length;i++){//设置滑动条选中的下标
				if(pid == $scope.positionLrnMaps[i].upm_upt_id){
					$scope.scrollerSelectedIndex = i;
					break;
				}
			}
			
			//4.获取详细信息
				//4.1.岗位简介,岗位学习地图图片
				//4.2.推荐课程列表
			var loadDetail = function loadDetailF(pid){
				
				var detailUrl = "/app/learningmap/getPostMapByUptId?upt_id=" + pid;
				
				Ajax.post(detailUrl,{},function(data){
					$scope.positionMapDetail = data.PostMap;
				});
				
				var cosUrl = "/app/learningmap/getRecomendCourseByUptId";
				var params = {upt_id:pid};
				$scope.positionCourseLoader = new Loader(cosUrl,params,function(data){
					for(var i=0;i<data.items.length;i++){
						var item = data.items[i];
						item.encItmId = wbEncryptor.encrypt(item.itm_id);
						item.cnt_comment_count = item.cnt_comment_count || 0;
						item.cnt_like_count = item.snsCount && item.snsCount.s_cnt_like_count ? item.snsCount.s_cnt_like_count : 0;
					}
				});
				return loadDetailF;
				
			}(pid);
			
			$scope.scrollerItemClick = function(item){
				loadDetail(item.upt_id);
			}
			
		});
		
	}]);
	
});