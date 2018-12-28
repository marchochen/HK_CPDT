angular.module("SearchDirective", []).directive('panel',['Ajax', function(Ajax) {
   var html = 
   '<div class="panel">'+
	   '<div class="panel-title-4">'+
	   	'<a class="" href="###" title=""><em class="panel-num-1">15</em>课程</a>'+
	   '</div>'+ 
	   '<div class="mod-list">'+
	    '    <a class="mod-list-box" href="###" title="">'+
	    '       <div class="mod-list-pic"><img src="../../images/adv16.jpg" alt=""/></div>'+
	     '      <div class="mod-list-tit-1">移动课程A01</div>'+
	     '      <p class="mod-list-tit-2">移动化、即时化、游戏化、碎片化...</p>'+
	     '   </a>'+
	   '</div>'+
  ' </div>'
	return {
        restrict: 'A',
        template: html,
        replace: true,
        link: function(scope, element, attrs) {
            
        }
    };
}]).directive('panelModel',['Ajax', function(Ajax) {
	   var html = 
		   '<div class="panel">'+
			   '<div class="panel-title-4">'+
			   	'<a class="" href="###" title=""><em class="panel-num-1">15</em>课程</a>'+
			   '</div>'+ 
			   '<div class="mod-list">'+
			    '    <a class="mod-list-box" href="###" title="">'+
			    '       <div class="mod-list-pic"><img src="../../images/adv16.jpg" alt=""/></div>'+
			     '      <div class="mod-list-tit-1">移动课程A01</div>'+
			     '      <p class="mod-list-tit-2">移动化、即时化、游戏化、碎片化...</p>'+
			     '   </a>'+
			   '</div>'+
		  ' </div>'
			return {
		        restrict: 'A',
		        template: html,
		        replace: true,
		        link: function(scope, element, attrs) {
		            
		        }
		    };
}]);