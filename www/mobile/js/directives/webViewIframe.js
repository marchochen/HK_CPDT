//appInd为true时，使用webView，appInd为false时，使用iframe
angular.module("WebviewIframe",[]).directive("webviewIframe",['$sce','$timeout',function($sce,$timeout){
	return {
        restrict: 'E',
        replace: true,
        scope: {
            url: '@url'
        },
        template : '<div ng-style="{\'height\':\'calc(100% - 46px)\'}" id="div_ifm_container"><iframe id="wb_ifm" style="height: 100%;" width="100%"  scrolling="yes" frameborder="0"/></div>',
        link: function(scope, element, attrs) {
        	
        	scope.$watch("url",function(){
        		if(scope.url){
        			var url = scope.url;
        			var appInd = false;
                	if(appInd){
                		var plusReady = function(){
                			var ws = plus.webview.currentWebview();
                			var embed = plus.webview.create(url, "embed", {top:"46px",bottom:'0px'});
                			ws.append(embed);
                		};
                		
                		if(window.plus){
                			plusReady();
                		}else{
                			document.addEventListener('plusready',plusReady,false);
                		}
                	}
                	$timeout(function(){
                		if(!appInd){
                			$("#wb_ifm").attr("src",url);
                		}else{
                			$("#div_ifm_container").remove();
                		}
        			});
        		}
        	});
        	
        }
    };
	
}]);