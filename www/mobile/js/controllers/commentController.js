$.include([
	'js/directives/CommentDirective.js',
	'js/services/SNSService.js',
	'js/directives/SNSDirectives.js'
], '../../');

$(function(){
	
	var commentModule = angular.module('comment', ['globalCwn']);
	commentModule.controller("commentController",function($scope){
		$scope.module = app.getUrlParam("module");
		$scope.id = app.getUrlParam("id");
		$scope.commentId = app.getUrlParam("commentId");
		$scope.tkhId = app.getUrlParam("tkhId");
		$scope.count = app.getUrlParam("count");
		
		$scope.back = function(){
			if(appInd){
				changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
    				back();
    			});
			}else{
				back();
			}
		}
		
	});
	
});