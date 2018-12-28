$.include([
	'lib/angular/angular-touch.min.js',
	'js/directives/SNSDirectives.js',
	'js/directives/CommentDirective.js',
	'js/services/SNSService.js',
	'lib/angular/angular-sanitize.min.js',
	'lib/videogular/videogular.js',
    'lib/videogular/vg-controls.js',
    'lib/videogular/vg-overlay-play.js',
    'lib/videogular/vg-poster.js',
	'js/services/UserService.js',
	'js/services/UploadService.js',
	'js/services/knowledgeCenterService.js',
	'js/directives/PhotoSwiperDirective.js',
	'js/services/VideoService.js',
	'js/directives/webViewIframe.js',
], '../../');
var alertObj;
$(function() {
	var knowledgeModule = angular.module('knowledge', ['globalCwn', 'userService','ngTouch', 'ngSanitize',
		'com.2fdevs.videogular',
		'com.2fdevs.videogular.plugins.controls',
		'com.2fdevs.videogular.plugins.overlayplay',
		'com.2fdevs.videogular.plugins.poster',
		'PhotoSwiper',
		'WebviewIframe'
	]);
	knowledgeModule.controller('mainController', ['$scope', 'knowledgeService', 'knowledgeCenterService','Store','$filter',function($scope, knowledgeService ,knowledgeCenterService ,Store ,$filter) {
		$scope.serverHost = serverHost;
		$scope.knowledgeType = '';
		$scope.kbcId = 0;
		$scope.tagId = 0;
		
		$scope.toggle = function(){
			$(".header-zhishi").slideToggle();
            $(".header-overlay").slideToggle();
		};
		$scope.toggleSelection = function(){
			$(".header-guide-box").slideToggle();
			$(".box-pic").slideToggle();
			$(".header-zhishi").hide();
            $(".header-overlay").hide();
           
            var mySwiper1 = new Swiper('#scroller',{
				  freeMode : true,
				  slidesPerView : 'auto',
			  }); 
            var mySwiper2 = new Swiper('#scroller_tag',{
				  freeMode : true,
				  slidesPerView : 'auto',
			  }); 
		};
		
		
		$scope.selectCatalog = function(cat) {
			$scope.kbcId = cat;
			$scope.reload();
		};
		$scope.selectTag = function(tag) {
			$scope.tagId = tag;
			$scope.reload();
		};
		$scope.selectKnowledgeType = function(type) {
			$scope.knowledgeType = type;
			$scope.reload();
		};
		
		init();
		
			
		
		
		//初始化加载数据
		
		function init() {
			
			//构造知识类型数组
			$scope.knowledgeTypes = [];
			$scope.knowledgeTypes.push({title:'text_all',cla:'',value:''});
			$scope.knowledgeTypes.push({title:'knowledge_type_article',cla:'wiz-article',value:'ARTICLE'});
			$scope.knowledgeTypes.push({title:'knowledge_type_document',cla:'wiz-document',value:'DOCUMENT'});
			$scope.knowledgeTypes.push({title:'knowledge_type_image',cla:'wiz-picture',value:'IMAGE'});
			$scope.knowledgeTypes.push({title:'knowledge_type_vedio',cla:'wiz-video',value:'VEDIO'});
			
			var allCat = {
				kbc_id: 0,
				kbc_title: $filter('translate')('text_all')
			};
			var allTag = {
				tag_id: 0,
				tag_title: $filter('translate')('text_all')
			};
			knowledgeService.getCat(function(data) {
				$scope.catalogs = data;
//				$(".header-guide-box").hide();
				$scope.catalogs.unshift(allCat);
			});
			knowledgeService.getTag(function(data) {
				$scope.tags = data;
				$(".header-guide-box").hide();
				$scope.tags.unshift(allTag);
			});
			
			$scope.personId = wbEncryptor.encrypt(Store.get('loginUser'));
			$scope.knCenterList = knowledgeCenterService.getKnCenterListLoader({pageSize:10});
		}
		
		$scope.reload = function(){
			var params = {
				kbc_id: $scope.kbcId == 0 ? '' : $scope.kbcId,
				tag_id: $scope.tagId == 0 ? '' : $scope.tagId,
				kbi_type: $scope.knowledgeType
			};
			$scope.knCenterList = knowledgeCenterService.getKnCenterListLoader(params);
		};
		
	}]);
	knowledgeModule.controller('libraryController', ['$scope', '$filter', 'Loader', 'knowledgeService', function($scope, $filter, Loader, knowledgeService) {
		$scope.serverHost = serverHost;
		$scope.kbcId = 0;
		$scope.tagId = 0;
		$scope.catalogs = [];
		$scope.tags = [];
		$scope.articles = {};
		$scope.images = {};
		$scope.vedios = {};
		$scope.documents = {};
		var showType = app.getUrlParam('show');
		$scope.show = {
			ARTICLE: true,
			IMAGE: false,
			VEDIO: false,
			DOCUMENT: false
		}; //控制选项卡显示
		if (showType) {
			for (var i in $scope.show) $scope.show[i] = false;
			$scope.show[showType] = true;
		}
		//切换显示 在页面上写的function
		$scope.toggle = toggle;
		$scope.selectCatalog = function(cat) {
			$scope.kbcId = cat;
		};
		$scope.selectTag = function(tag) {
			$scope.tagId = tag;
		};
		$scope.listLoad = function() {
			var url = '/app/kb/center/index/indexJson';
			$scope.articles = new Loader(url, getParams('ARTICLE'));
			$scope.images = new Loader(url, getParams('IMAGE'));
			$scope.vedios = new Loader(url, getParams('VEDIO'));
			$scope.documents = new Loader(url, getParams('DOCUMENT'));
		};
		init();
		//初始化加载数据
		function init() {
			var allCat = {
				kbc_id: 0,
				kbc_title: $filter('translate')('text_all')
			};
			var allTag = {
				tag_id: 0,
				tag_title: $filter('translate')('text_all')
			};
			knowledgeService.getCat(function(data) {
				$scope.catalogs = data;
				$scope.catalogs.unshift(allCat);
			});
			knowledgeService.getTag(function(data) {
				$scope.tags = data;
				$scope.tags.unshift(allTag);
			});
			$scope.listLoad();
		}

		function getParams(type) {
			return {
				kbc_id: $scope.kbcId == 0 ? '' : $scope.kbcId,
				tag_id: $scope.tagId == 0 ? '' : $scope.tagId,
				kbi_type: type,
				sortname: 'kbi_access_count',
				sortorder: 'desc'
			};
		}
	}]);
	knowledgeModule.controller('myShareController', ['$scope', 'Loader','knowledgeCenterService', function($scope, Loader, knowledgeCenterService) {
		$scope.serverHost = serverHost;
		$scope.knowledgesAppreoved = {};
		$scope.knowledgesPending = {};
		$scope.knowledgesReapproval = {};
		
		$scope.show = {}; //控制选项卡显示
		$scope.show.alreadyApprove = false; //已审批
		$scope.show.notApprove = false;  //未审批
		$scope.show.hasRefused = false;  //已拒绝
		var showType = app.getUrlParam('show');
		if (showType) {
			switch(showType){
				case 'alreadyApprove' : 
					$scope.show.alreadyApprove = true;
					break;
				case 'notApprove' : 
					$scope.show.notApprove = true;
					break;
				case 'hasRefused' : 
					$scope.show.hasRefused = true;
					break;
			}
		}else{//默认是已审批
			$scope.show.alreadyApprove = true;
		}
		init();
		if(app.getUrlParam("addInd")){
			$(".header-back").attr("href", "javascript:back(2);");
		}
		function init() {
			var url = '/app/kb/center/index/my_share';
			var paramsApproved = {
				kbi_app_status: 'APPROVED'
			};
			var paramsPending = {
				kbi_app_status: 'PENDING'
			};
			var paramsReapproval = {
				kbi_app_status: 'REAPPROVAL'
			};
			
			var getKnowledgesLoader = function(url, params){
				return new Loader(url, params, function(result){
					for(var i in result.items){
						var item = result.items[i];
						item.enc_kbi_id = wbEncryptor.encrypt(item.kbi_id);
						item.info = knowledgeCenterService.getKnowledgeInfo(item.kbi_type,item.docType);
					}
				});
			}
			
			$scope.knowledgesAppreoved = getKnowledgesLoader(url, paramsApproved);
			$scope.knowledgesPending = getKnowledgesLoader(url, paramsPending);
			$scope.knowledgesReapproval = getKnowledgesLoader(url, paramsReapproval);
		}
	}]);
	knowledgeModule.controller('detailController', ['$scope', '$sce', 'Store', 'knowledgeService', 'alertService','wizVideo',
		function($scope, $sce, Store, knowledgeService, alertService,wizVideo) {
			var id = app.getUrlParam('kbiId') || Store.get('kbiId');
			alertObj = alertService;
			
			var token = Store.get("token");
			
			$scope.serverHost = serverHost;
			$scope.type = {
				ARTICLE: 'knowledge_detail_article',
				IMAGE: 'knowledge_detail_image',
				VEDIO: 'knowledge_detail_vedio',
				DOCUMENT: 'knowledge_detail_document'
			}
			$scope.kbLike = {
				type: 'like',
				count: '0',
				flag: false,
				id: '',
				module: 'Knowledge',
				tkhId: 0
			};
			$scope.kbShare = {
				type: 'share',
				count: '0',
				flag: false,
				id: '',
				module: 'Knowledge',
				tkhId: 0
			};
			$scope.kbCollect = {
				type: 'collect',
				count: '0',
				flag: false,
				id: '',
				module: 'Knowledge',
				tkhId: 0
			};
			$scope.kbItem = {};
			$scope.showNum = 1;
			if (id && id != '') {
				knowledgeService.getDetail(id, detail);
			}

			$scope.back = function(){
				back();
				if(appInd){
					unlockOrientation();
				}
			};
			
			function detail(data) {
				$scope.kbItem = data.kbItem;
				$scope.kbLike = {
					count: data.kbItem.snsCount ? data.kbItem.snsCount.s_cnt_like_count : 0,
					flag: data.kbItem.sns ? data.kbItem.sns.like : false,
					id: data.kbItem.kbi_id,
					module: 'Knowledge',
					tkhId: 0
				};
				$scope.kbShare = {
					count: data.kbItem.snsCount ? data.kbItem.snsCount.s_cnt_share_count : 0,
					flag: data.kbItem.sns ? data.kbItem.sns.share : false,
					id: data.kbItem.kbi_id,
					module: 'Knowledge',
					tkhId: 0
				};
				$scope.kbCollect = {
					count: data.kbItem.snsCount ? data.kbItem.snsCount.s_cnt_collect_count : 0,
					flag: data.kbItem.sns ? data.kbItem.sns.collect : false,
					id: data.kbItem.kbi_id,
					module: 'Knowledge',
					tkhId: 0
				};
				if($scope.kbLike.flag || typeof $scope.kbLike.flag != 'undefined'){
					$(".gps-zan").addClass("active");
				}
				if($scope.kbCollect.flag || typeof $scope.kbCollect.flag != 'undefined'){
					$(".gps-shoucang").addClass("active");
				}
				if($scope.kbShare.flag || typeof $scope.kbShare.flag != 'undefined'){
					$(".gps-fenxiang").addClass("active");
				}
				
				if ($scope.kbItem.kbi_type == 'VEDIO') {
					vedioInit();
				}else if($scope.kbItem.kbi_type == 'DOCUMENT'){
					
					/**
					 * 获取文档对应的显示图片
					 */
					var getDocTypeImg = function(kbItem){
						var img = "";
						if(kbItem && kbItem.docType){
							var fileType = kbItem.docType;
							if(fileType == "XLS"){
								img = "../../images/banner_excel.jpg";
							}else if(fileType == "PPT"){
								img = "../../images/banner_ppt.jpg";
							}else if(fileType == "PDF"){
								img = "../../images/banner_pdf.jpg";
							}else{
								img = "../../images/banner_word.jpg";
							}
						}
						return img;
					}
					
					$scope.docTypeImg = getDocTypeImg($scope.kbItem);
					
					$scope.openDoc = function(){
						var url = $scope.kbItem.previewUrl;
						if(url){
							url = encodeURIComponent(url.replace(/&amp;/g,"&"));
						}
						Store.set("kbTitle",$scope.kbItem.kbi_title);//避免中文乱码，不采取url参数，而是存到本地，在docPage.html controller中取
						clicked("docPage.html?url="+url);
					};
					
				}else if($scope.kbItem.kbi_type === 'IMAGE'){
					var imgItems = [];
					for(var i = 0;i<$scope.kbItem.attachments.length;i++){
						var item = $scope.kbItem.attachments[i];
						var temArr = item.kba_url.substring(0,item.kba_url.lastIndexOf(".")).split("_");
					    var w; 
					    var h;
					    if(temArr && temArr.length==3){//取图片的宽高，数组的后两位
							w = temArr[temArr.length-2]; 
						    h = temArr[temArr.length-1];
						}else{
							w = 500;
							h = 500;
						}
					    
						var imgItem = {'src':item.kba_url,'w':w,'h':h};
						if(imgItem.src){
							imgItem.src = serverHost + imgItem.src;
						}
						imgItems.push(imgItem);
					}
					$scope.imageList = imgItems;
				}
			}
			
			/**
			 * 获取pdf对应的图片
			 */
			function getPdfImgItems(fileUrl){
				var url = fileUrl+'.js';
				var pages;
				var height;
				var width;
				var items =" [ ";
				$.ajaxSettings.async = false; 
				$.getJSON(url, function(data) {
					if(typeof(data)== "string"){
						data =$.parseJSON(data);
					}
					pages = data[0].pages;
					width = data[0].width;
					height = data[0].height;
					 
					for(var i=1;i<=pages;i++){
							
						var str_json = " {";
						str_json += "src: '" + fileUrl + "_"+i+".jpg',"+"w:"+width+",h:"+height+"}";
						
						if(i!=pages){
							str_json += ",";
						}
						items += str_json;
					}
					items += "]";
					items = eval("("+items+")");
					
				 });
				
				return items;
			}
			
			//视频初始化方法
			function vedioInit(){
				var scale = (2 / 3).toFixed(2);
				var width = window.innerWidth;
				var height = width * scale;
				$scope.height = height;
				$scope.width = width - 30;
				var videoUrl = "";
				if($scope.kbItem.attachments && $scope.kbItem.attachments.length>0 && $scope.kbItem.attachments[0].kba_url){
					videoUrl = serverHost + $scope.kbItem.attachments[0].kba_url + "?token="+token;
					courseNum = $scope.kbItem.attachments[0].kba_id; //视频cookie变量名
				}
				if($scope.kbItem.kbi_online && $scope.kbItem.kbi_online == 'ONLINE'){
					videoUrl = $scope.kbItem.kbi_content;
				}
				
				//cookie 变量
				viewUserId = Store.get('loginUser');
    		    viewCourseId = $scope.kbItem.kbi_id;
				
				if(videoUrl && videoUrl != ''){
					videoUrl = $sce.trustAsResourceUrl(videoUrl);
				}
				
				$scope.videoUrl = videoUrl;
				
				var controller = this;
				controller.config = {
					autoPlay:false,
					preload: "none",
					sources: [{
						src: videoUrl,
						type: "video/mp4"
					}, ],
					theme: {
						url: serverHost + "/mobile/lib/videogular/videogular.css"
					},
					plugins: {
						poster: "../../images/course_knowledge_video_post.png"
					}
				};
				$scope.controller = controller;
				if(appInd){
					unlockOrientation();
				}
				
				window.onload = function (){	
					wizVideo.setWizVideo("video");
				}
			}
			
		}
	]);
	
	knowledgeModule.controller("docPageController",['$scope','Store',function($scope,Store){
		$scope.title = Store.get("kbTitle");
		//clear
		Store.remove("kbTitle");
		$scope.url = app.getUrlParam('url');
	}]);
	
	knowledgeModule.controller('createController', ['$scope', '$filter', 'knowledgeService', 'User', 'alertService','dialogService',
	    	function($scope, $filter, knowledgeService, User, alertService,dialogService){
		alertObj = alertService;
		$scope.serverHost = serverHost;
		$scope.uploadType = app.getUrlParam('type');
		$scope.show = 'create';
		$scope.showCataLog = '';
		$scope.showTag = '';
		$scope.loginUser = {};
		$scope.title = '';
		$scope.desc = '';
		$scope.content = '';
//		$scope.$watch('show', function(){
//			if($scope.show == 'create'){
//				if($scope.tags && $scope.tags.length > 0){
//					$scope.showTag = arrayToString($scope.tags, 'tag_title', ',', 'select');
//				}
//				if($scope.catalogs && $scope.catalogs.length > 0){
//					$scope.showCataLog = arrayToString($scope.catalogs, 'kbc_title', ',', 'select');
//				}
//			}
//		});
		$scope.saveSelect = function(){
			if($scope.tags && $scope.tags.length > 0){
				$scope.showTag = arrayToString($scope.tags, 'tag_title', ',', 'select');
			}
			if($scope.catalogs && $scope.catalogs.length > 0){
				$scope.showCataLog = arrayToString($scope.catalogs, 'kbc_title', ',', 'select');
			}
			$scope.show = 'create'
		}
		$scope.create = function(){
			var tag = '';
			var catalog = '';
			if($scope.uploadType != 'ARTICLE'){
				$scope.content = arrayToString(attachments, 'kba_id', ',');
			}
			if($scope.tags && $scope.tags.length > 0){
				tag = arrayToString($scope.tags, 'tag_id', ',', 'select');
			}
			if($scope.catalogs && $scope.catalogs.length > 0){
				catalog = arrayToString($scope.catalogs, 'kbc_id', ',', 'select');
			}
			if(!validata())return;
			var kbItem = {
				kbi_title : $scope.title,
				kbi_desc : $scope.desc,
				kbi_content : $scope.content,
				kbi_type : $scope.uploadType,
				tag : tag,
				catalog : catalog
			};
			knowledgeService.addKbItem(kbItem, function(){
				dialogService.modal("share_success","o",function(){
					//成功之后跳转
					clicked('myShare.html?addInd=true&show=notApprove', true);
				});
			});
		}
		init();
		function init(){
			knowledgeService.getCat(function(data) {
				$scope.catalogs = data;
			});
			knowledgeService.getTag(function(data) {
				$scope.tags = data;
			});
			User.userInfo(function(data){
				$scope.loginUser = data.regUser;
				$scope.title = $filter('translate')('knowledge_title_tip_' + $filter('lowercase')($scope.uploadType), {value : $scope.loginUser.usr_display_bil})
			}) ;
		}
		function validata(){
			if($scope.title == ''){
				//提示标题不可为空
				alertService.add('warning', 'knowledge_tip_title', 2000);
				return false;
			}
			if($scope.content == ''){
				//提示没有内容
				alertService.add('warning', 'knowledge_tip_content', 2000);
				return false;
			}
			return true;
		}
		function arrayToString(arr, key, spit, flagkey){
			var str = '';
			if(arr && arr.length > 0){
				for(var i in arr){
					if(!flagkey || arr[i][flagkey])
						str = str + arr[i][key] + spit;
				}
				if(str != '')
					str = str.substring(0, str.length - 1);
			}
			return str;
		}
	}])
	knowledgeModule.filter('kbTypeFilter', function() {
		return function(input) {
			var types = {
				'ARTICLE': 'knowledge_type_article',
				'DOCUMENT': 'knowledge_type_document',
				'IMAGE': 'knowledge_type_image',
				'VEDIO': 'knowledge_type_vedio',
			};
			if (input) {
				return types[input] || '';
			}
			return '';
		}
	});
	knowledgeModule.filter('detailUrl', function(){
		
		return function(input){
			var url = 'detail.html?kbiId=' + input.enc_kbi_id;
			if("VEDIO" === input.kbi_type){
				return 'javascript:clicked("'+url+'",true,"",true)';
			}else{
				return 'javascript:clicked("' + url + '",true);';
			}
		}
	});
	knowledgeModule.service('knowledgeService', ['Ajax','$http', function(Ajax,$http) {
		var knowledge = this;
		this.getMainJson = function(callback) {
			var url = '/app/kb/center/main/mobile/Json';
			post(url, {}, function(data) {
				if (data && data.hashMap) {
					if (callback) callback(data.hashMap);
				}
			});
		};
		this.getCat = function(callback) {
			var url = '/app/kb/catalog/admin/getCatalogJson';
			post(url, {}, callback);
		};
		this.getTag = function(callback) {
			var url = '/app/kb/tag/admin/getTagJson';
			post(url, {}, callback);
		};
		this.getDetail = function(id, callback) {
			var url = "/app/kb/center/mobile/detailJson/" + id;
			post(url, {}, callback);
		};
		this.addKbItem = function(kbItem, callback){
			var defaultImage='/imglib/knowledge/h1/kc_a_01.jpg';
			var url = '/app/kb/center/mobile/insert';
			var params = {
				json :  JSON.stringify({
					kbi_title : kbItem.kbi_title,
					kbi_desc : kbItem.kbi_desc,
					kbi_content : kbItem.kbi_content,
					kbi_type : kbItem.kbi_type,
					kbi_default_image : defaultImage,
					kbi_online : 'OFFLINE'
				}),
				tag : kbItem.tag,
				catalog : kbItem.catalog
			};
			$http.post(url, params).then(function(data) {
	            if(callback && angular.isFunction(callback)){
	            	callback(data); 
	            }  
            });
		}
		function post(url, params, callback) {
			Ajax.post(url, params, function(data) {
				if (callback && angular.isFunction(callback)) callback(data);
			});
		}
		return knowledge;
	}]);
});
//播放错误处理
function videoError(e){
	var domElt = $("body")
	scope = angular.element(domElt).scope();
	scope.$apply(function(){
		alertObj.add('danger', 'mod_find_error');
	})
}

var exePhotoSwipe = function(){
	document.querySelector("#kn-ifrme").contentWindow.openPhotoSwipe(JSON.parse(window.localStorage.getItem("imgItem")));
	window.localStorage.removeItem("imgItem");//clear
};

var appendIframe = function(selector){
	
	function getProperHeight(){
		
		var width = window.innerWidth;
		var height = window.innerHeight;
		
		var newWidth , newHeight;
		
		if(appInd){
			newWidth = width > height ? width : height;
			newHeight = width < height ? width : height;
		}else{
			var newWidth = width;
			var newHeight = height;
		}
		
		var width = newWidth * (3/4);
		var height = newHeight*(3/4) > width ? width :newHeight*(3/4);
		return height;
	}
	
	window.onload = function(){
		var iframeSrc = serverHost + "/htm/photoSwipe/photeSwiper_em_mobile.htm"
		document.querySelector(selector).style.height = getProperHeight()+"px";
		document.querySelector(selector).innerHTML = '<iframe onload="exePhotoSwipe()" id="kn-ifrme" src="'+iframeSrc+'" scrolling="no" frameborder="0"  allowtransparency="true" allowfullscreen="true" name="photoSwiper" width="100%" height="100%"></iframe>';
	};
}
