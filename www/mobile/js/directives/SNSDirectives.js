appModule.directive('sns', [ '$filter', 'SNS', 'dialogService' , function($filter, SNS , dialogService) {
	return {
		restrict : 'A',
		replace : false,
		scope : {
        	sns : '='
        },
        link : function(scope, element, attrs){
         	scope.$watch('sns', function(){
        		if(!scope.sns.count){
        			scope.sns.count = 0;
        		}
        		if(!scope.sns.thkId){
        			scope.sns.thkId = '0';
        		}
        		if(!scope.sns.isComment){
        			scope.sns.isComment = '0';
        		}
       		 });
        	switch(scope.sns.type){
		  		case 'like' :
		  			element.bind('click', function(){
		  				$(this).toggleClass("active");
		  				if(scope.sns.flag){//取消点赞
		  					if(scope.sns.clickFlag || typeof scope.sns.clickFlag === 'undefined'){//避免重复提交
		  						scope.sns.clickFlag = false;
		  						SNS.cancelLike(scope.sns.id, scope.sns.module, scope.sns.isComment, function(data){
			  						if(scope.sns.count>0){
			  							scope.sns.count--;
			  							scope.sns.flag = false;
			  						}
			  						scope.sns.clickFlag = true;
			  						dialogService.modal("like_cancel","o");
			  					});
		  					}
		  				}else{//点赞
		  					if(scope.sns.clickFlag || typeof scope.sns.clickFlag === 'undefined'){
		  						scope.sns.clickFlag = false;
		  						SNS.addLike(scope.sns.id, scope.sns.module, scope.sns.tkhId, scope.sns.isComment, function(data){
			  						scope.sns.count++;
			  						scope.sns.flag = true;
			  						scope.sns.clickFlag = true;
			  						dialogService.modal("thank_like","o");
			  					});
		  					}
		  				}
		  			});
		  		break;
		  		case 'collect' :
		  			element.bind('click', function(){
		  				$(this).toggleClass("active");
		  				if(scope.sns.flag){//取消收藏
		  					if(scope.sns.clickFlag || typeof scope.sns.clickFlag === 'undefined'){
		  						scope.sns.clickFlag = false;
		  						SNS.cancelCollect(scope.sns.id, scope.sns.module, function(data){
			  						if(scope.sns.count>0){
			  							scope.sns.count--;
			  							scope.sns.flag = false;
			  						}
			  						scope.sns.clickFlag = true;
			  						dialogService.modal("collect_cancel","o");
			  					});
		  					}
		  				}else{//收藏
		  					if(scope.sns.clickFlag || typeof scope.sns.clickFlag === 'undefined'){
		  						scope.sns.clickFlag = false;
		  						SNS.addCollect(scope.sns.id, scope.sns.module, scope.sns.tkhId, function(data){
			  						scope.sns.count++;
			  						scope.sns.flag = true;
			  						
			  						scope.sns.clickFlag = true;
			  						
			  						dialogService.modal("collect_success","o");
			  					});
		  					}
		  				}
		  			});
		  			break;
		  		case 'share' :
		  			element.bind('click', function(){
		  				$(this).addClass("active");
		  				if(!scope.sns.flag){
		  					if(scope.sns.clickFlag || typeof scope.sns.clickFlag === 'undefined'){
			  					scope.sns.clickFlag = false;
			  					SNS.addShare(scope.sns.id, scope.sns.module, $filter('translate')('global_share_title'), scope.sns.tkhId,  function(data){
			  						scope.sns.count++;
			  						scope.sns.flag = true;
			  						scope.sns.clickFlag = true;
			  						dialogService.modal("share_success","o");
			  					});
		  					}
		  				}else{
		  					dialogService.modal("already_share","o");
		  				}
		  			});
		  			break;
		  		default : break;
		  }
		  scope.$watch('sns.count', function(){
			  	element.html(scope.sns.count);
		  	  	if(scope.sns.flag){
		  		//改变样式
		  	  	}else{
		  		//改变样式
		  	  	}
		  });
	   }
	};
}])