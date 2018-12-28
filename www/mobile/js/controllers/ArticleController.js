$.include([

    'lib/angular/angular-touch.min.js',
    'js/directives/SNSDirectives.js',
    'js/directives/CommentDirective.js',
    'js/services/SNSService.js'
], '../../');
$(function(){
   var articleModule = angular.module('article', ['globalCwn', 'ngTouch']);
   articleModule.filter('articleUrl', function(){
        return function(input, p){
            var url = 'articleDetail.html?article='+ input + "&artKinds=" + p;
            return 'javascript:clicked("' + url + '",true);';
        };
   });

   var articleListUrl = "/app/article/pageJson/";
   var params_new = {
   		 push_mobile : '1',
	     sortname : 'art_create_datetime',
	     sortorder : 'desc'
   };
   var params_popularity = {
		 push_mobile : '1',
		 sortname : 's_vtl_log_id_count',
		 sortorder : 'desc'
   };
   articleModule.controller('articleController', ['$scope', '$filter', 'Loader', 'Ajax', 'Store', function($scope, $filter, Loader, Ajax, Store) {
	   var art_type = 0;
	   Store.set("artType", art_type);
       $scope.serverHost = serverHost;
       $scope.article_type = [{aty_title : $filter('translate')('art_all'), aty_id : '0', selected : true}];
       
       var encrypArticleId = function(items){
    	   for(var i=0;i<items.length;i++){
    		   items[i].art_id = wbEncryptor.encrypt(items[i].art_id);
    	   }
       }
       
       $scope.articleList_new = new Loader(articleListUrl + art_type, params_new, function(data){
    	   Store.set("artPageNo", data.params.pageNo);
    	   if(data){
    		   encrypArticleId(data.items);
    	   }
       });

       $scope.articleList_popularity = new Loader(articleListUrl + art_type, params_popularity,function(data){
    	   if(data){
    		   encrypArticleId(data.items);
    	   }
       });
       
       $scope.showDetail = false;
       $scope.showList = true;
     
       $scope.selectedTabsCallback = function(pane){
       	switch(pane.index){
    	   case 0:
    		  $scope.showList = true;
    		  $scope.showDetail = false;
    		  if(art_type){
    	    	$scope.selectType(art_type);
    	      }
    		  break;
    	   case 1 : 
    		  $scope.showList = false;
    		  $scope.showDetail = true;
    		  if(art_type){
      	    	$scope.selectType(art_type);
      	      }
    		   break;
    	   }
       };
       
       /**
        *显示分类的方法
        */
       $scope.showType = function(){
    	   $(".header-guide").slideToggle();
    	   var mySwiper1 = new Swiper('#scroller',{
			  freeMode : true,
			  slidesPerView : 'auto',
		  });
       }
       $(".header-guide").hide();
       
       //初始化数据
       if($scope.article_type.length == 1){
	    	 var artTypeUrl = "/app/article/articleType";
	    	 Ajax.post(artTypeUrl, {}, function(data){
	    		 var artTypes = data.article_type_list;
	    		 for(var artType in artTypes){
	    			 artTypes[artType]['selected'] = false;
	    			 $scope.article_type.push(artTypes[artType]);
	    		 }
	    	 });
	     }
       
       /**
        *选择分类的方法
        */
       $scope.selectType = function(atyId){
    	     for(var artType in  $scope.article_type){
    	    	 $scope.article_type[artType]['selected'] = false;
    	    	 if($scope.article_type[artType]['aty_id'] == atyId){
    	    		 $scope.article_type[artType]['selected'] = true;
    	    		 art_type = atyId;
    	    		 Store.set("artType", art_type);
    	    	 }
    	     }
    	     $scope.getArtByType();
       }
       /**
        *根据分类加载方法
        */
       $scope.getArtByType = function(){    	     
    	     if($scope.showList){
         		$scope.articleList_new = new Loader(articleListUrl + art_type, params_new);
         	}else{
         		$scope.articleList_popularity = new Loader(articleListUrl + art_type, params_popularity);    	     
         	}
       }
      /**
        *点击取消按钮
        */
       $scope.cancel = function(){
    	   toogleType();
       }
      /**
       *评论输入框获得焦点
       */

   }]);
   articleModule.controller('detailController',['$scope', 'Store', 'Ajax', 'Loader', 'Swipe',
       function($scope, Store, Ajax, Loader, Swipe){

		$scope.artLike = {
			type : 'like',
			count : '0',
			flag : false,
			id : '',
			module : 'Article',
			tkhId : 0
		};                   //用来存储文章点赞信息
		$scope.artCollect = {
			type : 'collect',
			count : '0',
			flag : false,
			id : '',
			module : 'Article',
			tkhId : 0
		}; // 用来存储文章收藏信息
		$scope.artShare = {
			type : 'share',
			count : '0',
			flag : false,
			id : '',
			module : 'Article',
			tkhId : 0
		}; // 用来存储文章分享信息

		//获取单个文章进行查看
		function show(id){
			var artId;
			if(id) {
				artId = id;
			} else{
				artId = app.getUrlParam('article') || Store.get('article');
			}
	        var detailUrl = "/app/article/detailJson/" + artId;
	        Ajax.post(detailUrl, {}, function(data) {
				$scope.detailData = data;
				$scope.artLike = {
					count : data.snsCount ? data.snsCount.s_cnt_like_count : 0,
					flag : data.sns ? data.sns.like : false,
					id : data.article.art_id,
					module : 'Article',
					tkhId : 0
				};
				$scope.artCollect = {
					count : data.snsCount ? data.snsCount.s_cnt_collect_count : 0,
					flag : data.sns ? data.sns.collect : false,
					id : data.article.art_id,
					module : 'Article',
					tkhId : 0
				};
				$scope.artShare = {
					count : data.snsCount ? data.snsCount.s_cnt_share_count : 0,
					flag : data.sns ? data.sns.share : false,
					id : data.article.art_id,
					module : 'Article',
					tkhId : 0,
				};
				if($scope.artLike.flag || typeof $scope.artLike.flag != 'undefined'){
					$(".gps-zan").addClass("active");
				}
				if($scope.artCollect.flag || typeof $scope.artCollect.flag != 'undefined'){
					$(".gps-shoucang").addClass("active");
				}
				if($scope.artShare.flag || typeof $scope.artShare.flag != 'undefined'){
					$(".gps-fenxiang").addClass("active");
				}
				
			});
		}

		//获取单个进行查看，第一次调用
		show();

		// 临时列表, 记录当前页的记录，用来滑动上一条下一条
		var list_new = [];
		var list_popularity = [];

		var pageNo = Store.get("artPageNo"); //当前页码
		var curId = Store.get('article');
       	var type = Store.get('artType');
    	var kinds = Store.get('artKinds')
		var url = articleListUrl + type;
		var swipe = new Swipe(url, pageNo, curId);

		//滑动显示临近的
        $scope.showNear = function(forward){
        	if(kinds == 'new') {
        		params = params_new;
        		list = list_new;
        	} else {
        		params = params_popularity;
        		list = list_popularity;
        	}
        	swipe.getNearId(params, list, 'art_id', forward, function(gId, result){
               	if(gId){
            		show(swipe.curId = gId);
            		if(result) {
                    	if(kinds == 'new') {
                    		list_new = result;
                    	} else {
                    		list_popularity = result;
                    	}
            		}
            	}
        	});
        }

   }]);

   function toogleType(){
	   $(".header-guide").slideToggle();
	   $(".header-overlay").toggleClass("show");
     $('#main').toggle();
   }

});
