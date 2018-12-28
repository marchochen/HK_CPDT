appModule.factory('Loader', function ($http) {
     var loader = function (url, params, callback) {
         this.items = [];
         this.busy = false;
         this.page = 1;
         this.hasNext = true;
         this.loaderMore = loaderMore;
         this.url = url;
         this.params = params;
         this.show = true;
         this.totalRecord = 0;
         this.pageSize = 0;
         this.callback = callback;
     };
     loaderMore = function (page, callback) {
    	 if (!this.hasNext) return;
         if (!this.url) return;
         if (this.busy) return;
         this.busy = true;
         if(page){
        	 this.page = page;
         }
         if(this.params){
 	        this.params.pageNo = this.page;
         } else {
         	this.params = {
         		pageNo : this.page
         	}
         }
         var url = app.urlAddParams(this.params, this.url);
         $http.jsonp(url).success(function (data) {
        	 if(data && data.rows){
	             var items = data.rows;
	             var count = parseInt(data.total);
	             this.pageSize = parseInt(data.pageSize);
	             this.totalRecord = count;
	             if(items && items.length>0){
		             for (var i = 0; i < items.length; i++) {
		                 this.items.push(items[i]);
		             }
	             } else {
	            	 this.show = false;
	             }
				 if(count == this.items.length){
					this.hasNext = false;
				 }
	             this.busy = false;
        	 }
             this.page = parseInt(this.page) + 1;
             
             if(callback){
            	this.callback = callback;
             } 
             if(this.callback){
            	this.callback(this);
             }
         }.bind(this));
     };
 
     return loader;
}).service('Pager', function ($http, $pageSize) {
    var pager = function (url, params) {
        this.busy = false;
        this.selectPage = selectPage;
        this.url = url;		
        this.page = 1;		//当前页
        this.numPages = 5;	//显示的页数
        this.items = [];	//列表数据
        this.hasNext = true;		//是否有下一页
        this.params = params;
        this.show = true;
    };
    selectPage = function (page) {
        if (this.busy) return;
        this.busy = true;
        if(!page){
       	    page = this.page;
        }
        var pageSize;
        if(this.params){
	        pageSize = this.params['pageSize'];
	        if(!pageSize){
	        	pageSize = $pageSize;
	        }
	        this.params.pageNo = page;
        } else {
        	this.params = {
        		pageNo : page
        	};
        	pageSize = $pageSize;
        }
        var url = app.urlAddParams(this.params, this.url);
        $http.jsonp(url).success(function (data) {
        	this.items = data.rows;
        	this.numPages = Math.ceil(data.total / pageSize);
            this.busy = false;
            //如果没有下一页，隐藏按钮
            if(this.items && this.items.length<1){
            	this.show = false;
            }
            if(data.rows && data.total == data.rows.length){
            	this.hasNext = false;
            }
        }.bind(this));
    };
    return pager;
}).service('Ajax',function ($http) {
	var ajax = this;        
	this.post = function(url, param, success){
		var params = param || {};        
		var query = '';           
		$http.jsonp(app.urlAddParams(params, url)).success(function(data) {
            if(success){
                success(data);    
            }         	
        });
	};		
	return ajax;

}).service('Swipe', function(Loader){
	var swipe =  function(url, pageNo, curId){
		this.pageNo = pageNo; //当前页码
		this.totalPage = 0;					 //总共页
		this.index = 0;						 //记录当前第几条
		this.curId = curId;							 //当前id
		this.loader;							 //分页请求，注意（该对象会把所加载的都存放入items，不只是一页）
		this.params = [];					 //请求参数	
		this.getNearId = getNearId;
		this.url = url;
		this.list = [];
	};
	getNearId =  function(params, list, idName, forward, callback) {
		var swipe = this;
		if(forward == 'next') {
			this.index++;
		} else if(forward == 'prev') {
			this.index--;
		}
    	if(!this.list || this.list.length < 1) {
    		this.loader = new Loader(this.url, params);
    		if(!this.pageNo) this.pageNo = 1;
    		this.loader.loaderMore(this.pageNo, function(data){
    			swipe.list = data.items;
    			swipe.totalPage = data.totalRecord % data.pageSize == 0 ? data.totalRecord/data.pageSize : data.totalRecord/data.pageSize + 1;  
    			if(swipe.list && swipe.list.length > 0){
    				angular.forEach(swipe.list, function(val, i) {
    					if(eval("val." + idName) == swipe.curId){
    						swipe.index = i;
    						swipe.index++;
    					}
    				})
    				if(swipe.index < swipe.list.length){
        				callback(eval("swipe.list[swipe.index]." + idName), swipe.list);
    				} else {
    					nextPage(swipe);
    				}
    			}
    		});
    	} else if(forward == 'next' && list && eval("this.list[this.list.length-1]." + idName) == this.curId) {
    		nextPage(swipe);
    	} else {
    		if(this.index < 0) this.index = 0;
    		callback(eval("this.list[this.index]." + idName));
    	}
    	
    	function nextPage(swipe){
    		if(swipe.pageNo < swipe.totalPage){
    			swipe.pageNo++;
    			swipe.loader.loaderMore(swipe.pageNo, function(data){
    				swipe.list = data.items;
              		if(swipe.list && swipe.index < swipe.list.length) {
              			callback(eval("swipe.list[swipe.index]." + idName), swipe.list);
              		}
        		})
    		} else {
    			swipe.index--;
    		}
    	}
    }
	return swipe;
}).factory('modalService', function ($modal) {
	var modal = {};
	modal.modal = function(controller, size) {
		if(!size) size = "sm";
		var modalInstance = $modal.open({
			template :
		   '<div class="wzm-modal">' +
	        '<div class="modal-body">'+
			'	{{modalText | translate}}'+
	       ' </div>'+
	       ' <div class="modal-footer">'+
	       '     <button class="btn wzm-btn-left" ng-click="modalOk()">{{btnSure?btnSure:"btn_sure" | translate}}</button>'+
	       '	 <span ng-show="!noShowCancel" style="padding-left:5px;">&nbsp;&nbsp;</span>'+
	       '     <button ng-show="!noShowCancel" class="btn wzm-btn-right" ng-click="modalCancel()">{{btnCancel?btnCancel:"btn_cancel" | translate}}</button>'+
	       ' </div>' +
	       '</div>',
			controller : controller,
			size : size,
			backdrop: 'static',
			windowTemplateUrl : 'modalContentTemplate.html'
		});
	};
	return modal;
}).factory('UserService',function($http,Store) {
	var url = "/app/userInfo/detail";
	return {
		userInfo : function(callback) {

			var userInfoStr = Store.get("userInfo");
			
			if(userInfoStr){
				var userInfoJson = JSON.parse(userInfoStr);
				if(userInfoJson){
					userInfoJson.regUser.enc_usr_ent_id = wbEncryptor.encrypt(userInfoJson.regUser.usr_ent_id);//已加密的usr_ent_id
					callback(userInfoJson);
					return;
				}
			}
			
			$http.jsonp(url).success(function(data) {
				data.regUser.enc_usr_ent_id = wbEncryptor.encrypt(data.regUser.usr_ent_id);//已加密的usr_ent_id
				Store.set("userInfo",JSON.stringify(data));
				callback(data);
			}.bind(this));
		},
		updateUserInfoCache : function(callBack){//更新用户信息缓存
			$http.jsonp(url).success(function(data) {
				data.regUser.enc_usr_ent_id = wbEncryptor.encrypt(data.regUser.usr_ent_id);//已加密的usr_ent_id
				Store.set("userInfo",JSON.stringify(data));
				if(callBack){
					callBack();
				}
			}.bind(this));
		}
	};
}).factory('dialogService', function ($modal,$filter,Store,$timeout) {
	
	
	var modal = {};
	//@param message:提示语的label
	//@param type : w:欢迎类提示，o:操作类提示
	modal.modal = function(message,type,callBack,timeout) {
		
		var	controller = function($scope, $modalInstance) {
			$scope.modalDismiss = function() {
				$modalInstance.dismiss('cancel');
				if(callBack){
					callBack();
				}
			};
		}
		
		message = $filter("translate")(message)+"！";
		var html = "";
		var backdrop = "static";
		if("w" === type){
			var usr_photo;
			var userInfo = Store.get("userInfo");
			if(userInfo){
				usr_photo = Store.get("serverHost") + JSON.parse(userInfo).regUser.usr_photo;
			}
			html =  '<div class="panel-list-well">'+
							'<div style="height:60px;">'+
						   		'<div class="panel-list-well-user"><img src="'+usr_photo+'" alt=""></div>' +
						   		'<div class="panel-list-well-close" ng-click="modalDismiss()"></div>' +
						   	'</div>'+
						   	'<div class="panel-list-well-remark">'+message+'</div>' +
					 '</div>';
		}else{
			html = '<div class="panel-list-hint-remark">'+message+'</div>';
			if(!timeout){
				timeout = 2000//操作类默认自动结束
			}
			backdrop = false;
		}
		var modalInstance = $modal.open({
			template : html,
			windowTemplateUrl : 'modalContentTemplate.html',
			controller : controller,
			backdrop: backdrop
		});
		
		if (timeout) {
			$timeout(function() {
				modalInstance.dismiss('cancel');
				if(callBack){
					callBack();
				}
			}, timeout);
		}
	};
	return modal;
});
