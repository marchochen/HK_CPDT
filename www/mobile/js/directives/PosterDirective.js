angular.module("posterDirective", ['ngTouch']).directive('poster',['$swipe','$document', function($swipe,$document) {
   var html = 
    '<div class="touchslider">'+
		'<div class="touchslider-viewport">'+
		     '<div class="touchslider-item" ng-swipe-right="swipeLeft({{$index-1}})" ng-swipe-left="swipeLeft({{$index+1}})" ng-show="($index==index)" ng-repeat="item in poster_items">'+
		     	'<a href="{{item.url}}" title="" ><img ng-src="http://192.168.2.33:7004{{item.file}}" alt=""/></a>'+
		     '</div>'+
		'</div>'+
		'<div class="touchslider-navtag">'+
		    ' <div class="touchslider-navbox"  ng-click="checkImg({{$index}})" ng-repeat="item in poster_items">'+
		       '   <span class="touchslider-nav-item" ng-class="{\'touchslider-nav-item-current\' : $index == indexNum}"></span>'+
		   '  </div>'+
		'</div>'+
	'</div>';

	return {
        restrict: 'A',
        template: html,
        replace: true,
        link: function(scope, element, attrs) {
            scope.$watch('indexNum', function(value) {
            	//alert(value);
            });
            scope.index = 1;
            scope.checkImg =  function(index){
            	//alert("222");
            	scope.index = index;
            }
            scope.swipeLeft = function(index){
            	scope.index = index;
            }
            
        var startX = 0, 
             startY = 0, 
             x = 0, 
             y = 0; 
             element.css({ 
                position: 'relative', 
                border: '1px solid red', 
                backgroundColor: 'lightgrey', 
                cursor: 'pointer' 
            }); 
            element.bind('mousedown', function (event) { 
                startX = event.screenX - x; 
                //startY = event.screenY - y; 
                $document.bind('mousemove', mousemove); 
                $document.bind('mouseup', mouseup); 
            }); 
    
            function mousemove(event) { 
               //y = event.screenY - startY; 
               x = event.screenX - startX; 
               element.css({ 
                   top: y + 'px', 
                   left: x + 'px' 
                }); 
            } 
    
           function mouseup() { 
               $document.unbind('mousemove', mousemove); 
               $document.unbind('mouseup', mouseup); 
           } 
                
            
        }
    };
}])