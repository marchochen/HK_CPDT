$.include([
    'lib/angular/angular-touch.min.js'
], '../../');
$(function(){
	var appModule = angular.module('approval', ['globalCwn', 'ngTouch']);
	appModule.controller('appController',['$scope', 'Loader','Ajax', function($scope, Loader, Ajax) {
		var pendingListUrl = '/app/subordinate/getApprovalList/PENDING';
		var approvedListUrl = '/app/subordinate/getApprovalList/HISTORY';
		var pageNo = null;
	    
	    pendingLoader();
	    approvedList();
	    
	    $scope.pass = function(app_id,app_upd_timestamp){   	  		
			approval(app_id,app_upd_timestamp,1,'subordinate_app_waiting','subordinate_approval_operation');
   	  	};
   	  	
   	  	$scope.refuse = function(app_id,app_upd_timestamp){   	
   	  		approval(app_id,app_upd_timestamp,2,'subordinate_app_not_approved','button_refuse');
	  	};
	  	
	  	function approval(app_id, upd_time, action_id, to, verb){
			var params = {
				isMobile : true, 
				upd_time : upd_time,
				process_id : 1,
				fr : 'subordinate_app_waiting',
				to : to,
				verb : verb,
				action_id : action_id,
				status_id : 1
			};
			Ajax.post("/app/subordinate/registrationApproval/" + app_id, params,function(){
				if(appInd){
					plus.webview.getWebviewById('home').evalJS("changeTotal('approval_count'),'cancel'");
				}
				pendingLoader();
	    		approvedList();
	    		
	    		//更新首页任务数量
	  			 if(appInd){
		                webview = plus.webview.getWebviewById("home");
		                var wvs=plus.webview.all();
						for(var i = wvs.length-1;i >= 0; i--){
							if(wvs[i].id == "home"){
								webview = wvs[i];
								break;
							}
						}
						if(webview == undefined){
							webview = plus.webview.create("views/index.html","home",{scrollIndicator:'none',scalable:false});
						}
						webview.evalJS("getStatistics()");
				}
	    		
			});
		}
	  	
	  	function pendingLoader(){
			$scope.pendingList = new Loader(pendingListUrl, null, function(data){
				var i = (data.params.pageNo - 1) * data.pageSize;
				for(;i<data.items.length;i++){
					var app = data.items[i];
					if(app.aal_action_timestamp != undefined && app.aal_user_name != undefined && app.aal_action_taken != undefined){
						app.aal_action_taken = "app_" + app.aal_action_taken;
					} else {
						app.aal_action_taken = "data_null";
					}
				}
		    });	
		}
	  	
	  	function approvedList(){
	  		$scope.approvedList = new Loader(approvedListUrl, null, function(data){
				var i = (data.params.pageNo - 1) * data.pageSize;
				for(;i<data.items.length;i++){
					var app = data.items[i];
					if(app.aal_user_name != undefined){
						app.aal_user_name = "app_group_leader";
					} else {
						app.aal_user_name = "data_null";
					}
				}
		    });
	  	}
	}]);
});

function show(thisObj){
	$(thisObj).siblings(".tipBox").slideToggle();
}
