angular.module("wzmSwiper",[]).directive("wzmSwiper",[function(){
	return {
        restrict: 'E',
        replace: true,
        transclude: true,
        scope: {
        	options : '@options',
        	cssStyle : '@cssStyle'
        },
        template : '<div class="swiper-container"> \
            			<div class="swiper-wrapper" ng-transclude style="{{cssStyle}}"> \
        				</div> \
        			</div>',
        controller: function($scope,$element, $attrs) {
        	
        	var swiperOptions = {
	    		slidesPerView: 'auto',
	            slideToClickedSlide:true,
	            spaceBetween: 20
		    };
        	
			if ($scope.options) angular.extend(swiperOptions, $scope.$eval($scope.options));
			
			this.ready = function(){
				var mySwiper = new Swiper(".swiper-container",swiperOptions);
			}

        }
    };
}]).directive("wzmSlide",['$timeout',function($timeout){
	return {
        restrict: 'E',
        replace: true,
        require: '^wzmSwiper',
        transclude: true,
        template : '<div class="swiper-slide" ng-transclude><div>',
        link: function(scope, elem, attrs, swiper) {
        	if(attrs.slideclass){
        		elem.addClass(attrs.slideclass);
        	}
        	if(scope.$last) {
				$timeout(function(){
					swiper.ready();
				});
			}
        }
    };
}]);