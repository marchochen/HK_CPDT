$.include([], '../');
$(function(){
	var forwardModule = angular.module('cwn', ['globalCwn']);
	forwardModule.controller('forwardController', ['$scope', '$http', '$window', 'Store', '$timeout',
	    function($scope, $http, $window, Store, $timeout) {
		var token = app.getUrlParam("token");
		var id = app.getUrlParam("id");
		var tkhId = app.getUrlParam("tkhId");
		var url = app.getUrlParam("url");
		var userEntId = app.getUrlParam("uid");
		var msgId = app.getUrlParam("wmsg_id");

		
		Store.getStorage().clear();
		window.sessionStorage.clear();
		Store.set("token", token);
		
		///alert(Store.get("token") + "     token : " + token + " userEntId :" + userEntId);
		
		Store.set("loginUser", userEntId);

		var newUrl = "";
	
		forward(url);
		
		function forward(url) {
			switch (url){
			case 'article_list' : 
				newUrl = 'article/article.html';
				break;
			case 'article_detail' : 
				newUrl = 'article/articleDetail.html?article=' + id;
				break;
			case 'course' :	/** wizMobile页面名称：已报名课程列表*/
				newUrl = "course/signup.html"
				$window.location.href = newUrl;
				break;
			case 'course_detail' :	/** wizMobile页面名称：课程详情*/
				newUrl = "course/detail.html?itmId=" + id ;
				if(tkhId){
					newUrl += "&tkhId=" + tkhId
				}
				$window.location.href = newUrl;
				break;
			case 'recommend' : 
				newUrl = "course/recommend.html;"
				$window.location.href = newUrl;
				break;
			case 'test' :	/** wizMobile页面名称：已报名课程列表*/
				newUrl = "exam/signup.html"
				$window.location.href = newUrl;
				break;
			case 'test_detail' :	/** wizMobile页面名称：课程详情*/
				newUrl = "course/detail.html?itmId=" + id ;
				if(tkhId){
					newUrl += "&tkhId=" + tkhId
				}
				$window.location.href = newUrl;
				break;
			case 'open' :	/** wizMobile页面名称：已报名课程列表*/
				newUrl = "course/openCourse.html"
				$window.location.href = newUrl;
				break;
			case 'open_detail' :	/** wizMobile页面名称：课程详情*/
				newUrl = "course/openDetail.html?itmId=" + id ;
				$window.location.href = newUrl;
				break;			
			case 'announcement_list' :	/** wizMobile页面名称：公告列表*/
				newUrl = "announce/announce.html";
				$window.location.href = newUrl;
				break;
			case 'announcement_detail' :	/** wizMobile页面名称：公告详情*/
				newUrl = "announce/announce.html?id="+id;
				$window.location.href = newUrl;
				break;
			case 'message_list' :	/** wizMobile页面名称：站内信列表*/
				newUrl = "message/message.html?uid="+ userEntId + "&msg_id="+msgId;
				$window.location.href = newUrl;
				break;
			default : newUrl = "index.html";				
		}
		
		$window.location.href = newUrl;
		}

		
	}]);

});