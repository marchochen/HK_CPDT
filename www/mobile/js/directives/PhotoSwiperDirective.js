angular.module("PhotoSwiper",[]).directive("photoSwiper",[function(){
	return {
        restrict: 'E',
        replace: false,
        scope: {
            imageList: '=imageList',
            postImg : '@postImg'
        },
        template : '<div> \
        				<img src="{{postImg}}" ng-click="openPhotoSwiper()" style="width:100%"/> \
        				<div class="pswp" tabindex="-1" role="dialog" aria-hidden="true"> \
        					<div class="pswp__bg"></div> \
				        	<div class="pswp__scroll-wrap"> \
				        		<div class="pswp__container"> \
						        	<div class="pswp__item"></div> \
								    <div class="pswp__item"></div> \
								    <div class="pswp__item"></div> \
				        		</div> \
        						<div class="pswp__ui pswp__ui--hidden"> \
        							<div class="pswp__top-bar"> \
        								<button class="pswp__button pswp__button--close" title="Close (Esc)"></button> \
        								<div class="pswp__counter"></div> \
							        	<div class="pswp__preloader"> \
										    <div class="pswp__preloader__icn"> \
										      <div class="pswp__preloader__cut"> \
											<div class="pswp__preloader__donut"></div> \
										      </div> \
										    </div> \
										</div> \
        							</div> \
						        	<div class="pswp__share-modal pswp__share-modal--hidden pswp__single-tap"> \
										<div class="pswp__share-tooltip"></div> \
								    </div> \
								    <button class="pswp__button pswp__button--arrow--left"> \
								    </button> \
								    <button class="pswp__button pswp__button--arrow--right"> \
								    </button> \
								    <div class="pswp__caption"> \
										<div class="pswp__caption__center"></div> \
								    </div> \
        						</div> \
				        	</div> \
        				</div> \
        			</div> \
        			',
        link: function(scope, element, attrs) {
           // load resource...
           element.append("<link type='text/css' rel='stylesheet' href='"+"../../"+"css/photoSwipe/photoswipe.css'/>");
           element.append("<link type='text/css' rel='stylesheet' href='"+"../../"+"css/photoSwipe/default-skin/default-skin.css'/>");
           element.append("<script type='text/javascript' src='"+"../../"+"lib/photoSwipe/photoswipe.min.js'></script>");
           element.append("<script type='text/javascript' src='"+"../../"+"lib/photoSwipe/photoswipe-ui-default.min.js'></script>");

           var photos = scope.imageList;
           if(!photos || photos.length < 1){
        	  return; 
           }
           scope.postImg = scope.postImg || photos[0].src;
           
           var openPhotoSwipe = function() {
        	   
	   			var pswpElement = document.querySelectorAll('.pswp')[0];
	   			var options = {
	   		        history: false,
	   		        focus: false,
	   		        showAnimationDuration: 0,
	   		        hideAnimationDuration: 0,
	   		        loop:true,
	   				closeOnScroll : false,
	   				closeOnVerticalDrag : false,
	   				pinchToClose : false,
	   				escKey: true,
	   				arrowKeys: false,
	   				barsSize: {top:46, bottom:'auto'},
	   				shareEl : false,
	   				closeEl: true,
	   				tapToToggleControls : true,
	   				clickToCloseNonZoomable: false
	   		    };
	   			var gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, photos, options);
	   			gallery.init();
   		   };
           
           scope.openPhotoSwiper = function(){
        	   openPhotoSwipe();
           };
           
        }
    };
	
}]);