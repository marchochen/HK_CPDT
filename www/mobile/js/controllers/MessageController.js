$.include([
    'lib/angular/angular-touch.min.js',
], '../../');

$(function(){
	var msgModule = angular.module('message', ['globalCwn', 'ngTouch']);
	msgModule.controller('indexController', ['$scope','$filter','Loader','Ajax', 'Swipe', 'modalService','alertService',function($scope,$filter, Loader, Ajax, Swipe,modalService,alertService) {

		$scope.showList = true;
		$scope.sysMsg =  $filter('translate')('sys_msg');

		var url = "/app/message/getMyWebMessage/";
   	  	var rec_params = {
   	  		sortname : 'wmsg_create_timestamp',
   	  		sortorder : 'desc',
   	  		wmsgIdValue : 0
   	  	};
   	  	var send_params = {
    	  		sortname : 'wmsg_create_timestamp',
    	  		sortorder : 'desc',
    	  		wmsgIdValue : 0
	  	};
   	  	var rec_type = "rec_message";
   	  	var send_type = "send_message";
   	  	
   	  	var msg_id = app.getUrlParam('msg_id');
   	  	
   	  	//收件箱列表
   	  	$scope.msgList = new Loader(url + rec_type, rec_params,function(data){
   	  		data.page = 1;
   	  		if(data.totalRecord < data.pageSize){
   	  			data.hasNext = false;
   	  		}
   	  		if(data.items && data.items.length > 0 ){
   	  			rec_params.wmsgIdValue =data.items[data.items.length-1].wmsg_id;
   	  		}
            for(var i in data.items){
            	 if(data.items[i].wmsg_type == 'PERSON'){
            		 data.items[i].wmsg_subject = $filter('translate')('personal_msg_post_title', {value : data.items[i].sendUser.usr_display_bil});
            	 }
            }
   	  	});

   	  	//发件箱列表
   		$scope.send_msgList = new Loader(url + send_type, send_params,function(data){
   	  		data.page = 1;
   	  		if(data.totalRecord < data.pageSize){
   	  			data.hasNext = false;
   	  		}
   	  		if(data.items && data.items.length > 0 ){
   	  			send_params.wmsgIdValue =data.items[data.items.length-1].wmsg_id;
   	  		}
   	  	});

   		//删除信息
   		$scope.delById = function(msgId,message_type,field,click_flag){
	   	  	modalService.modal(function($scope, $modalInstance) {
				$scope.modalText = 'global_sure_delete';
				$scope.modalOk = function() {
					var del_url = "/app/message/delById";
					Ajax.post(del_url,{id : msgId,message_type : message_type},function(){
						//更新首页任务数量
						if(appInd && !click_flag){
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
						$modalInstance.dismiss('cancel');
						field.msg.noShow = true;
						alertService.add("success",$filter('translate')('message_delete_success'),2000);
		   	  		});
				};
				$scope.modalCancel = function() {
					$modalInstance.dismiss('cancel');
				};
			});
   	  	};

   		$scope.publish = function(){
   			clicked("publish.html",true);
		};

		$scope.back = function(){
			if($scope.showList){
				javascript:back();
			}else{
				$scope.showList = true;
			}
		}

		$scope.showDetail = function(msgId,message_type,field){
			field.msg.clicked = true;
			field.msg.readHistory = "TEMP";
			$scope.showList = false;
			$scope.type = message_type;
			$scope.isReply = false;
			var detailurl = "/app/message/getWebMessageDetail/" + msgId;
			var readUrl = "/app/message/readWebMessage/" + msgId;
			Ajax.post(readUrl, {message_type : message_type});
			Ajax.post(detailurl, {message_type : message_type}, function(data){
				data.webMessage.wmsg_content_mobile = data.webMessage.wmsg_content_mobile.replace(/javasc#ript/g,"javascript").replace("onclick=\"openOtherSiteUrl(\'", "onclick=\"").replace(" ')\"", "\"")
	  			$scope.msgDetail = data.webMessage;
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
		};
		
		$scope.reply = function(){
  			$scope.isReply = true;
  		};

  		$scope.sendMessage = function(){
  			if(!$scope.messageBody){
				alertService.add('warning', $filter('translate')('message_content_not_empty'));
				return;
			}

  			if(app.getChars($scope.messageBody) > 1000){
  				alertService.add("warning",$filter('translate')('global_content_length_limit',{value1:'1000',value2:'500'}));
				return;
  			}
  			
  			var param = {
				usr_ent_id_str : $scope.msgDetail.wmsg_send_ent_id,
				wmsg_subject : $filter('translate')('comment_reply_tip')+$scope.msgDetail.wmsg_subject,
				wmsg_content_pc : $scope.messageBody
  			};

  			var sendUrl = "/app/message/sendMessage";

  			Ajax.post(sendUrl, param, function(data){
  				alertService.add('success', $filter('translate')('message_reply_success'),1000,function(){
  					window.location.reload();
  				});
  	  		});
  		}
		
  		window.onload=function(){
  			if(msg_id != undefined && msg_id != null && msg_id != 0 && msg_id != ""){
//  			$scope.showDetail(msg_id,'rec_message',$("#msg_"+msg_id));
  				$("#msg_"+msg_id).click();
  			}
  		}
	}])
	.controller("publishController",function($scope,alertService,Ajax,$filter,Loader){
		
		$scope.serverHost = serverHost;
		
		/**
		 * 回退按钮
		 */
		$scope.goBack = function(){
			if($scope.show.main){
				back();
			}else if($scope.show.receiverPage){
				initPage();
				$scope.show.main = true;
			}else if($scope.show.receiverSelection || $scope.show.remove){
				initPage();
				$scope.show.receiverPage = true;
			}
		};
		
		//控制页面显示的对象
		$scope.show = {};
		var initPage = function(){
			$scope.show.main = false;//界面一开始显示发送的主界面
			$scope.show.receiverPage = false;//已选择的联系人界面
			$scope.show.receiverSelection = false;//选择联系人界面
			$scope.show.remove = false;//删除联系人显示
		}
		initPage();
		//进入页面显示的是发信息主页面
		$scope.show.main = true;
		
		var sendingMsg = false;
		//【发布】按钮事件
		$scope.publish = function(){
			
			//控制表单重复提交
			if(!sendingMsg){
				sendingMsg = true;
			}else{
				return;
			}
			
			var userIdArr = [];
			
			for(var i = 0;i<$scope.receivers.length;i++){
				userIdArr.push($scope.receivers[i].usr_ent_id);
			}
			
			var params = {
				usr_ent_id_str : userIdArr.join("~"),
				wmsg_subject : $scope.wmsg_subject,
				wmsg_content_pc : $scope.wmsg_content_pc
			};
			if(!params.wmsg_subject){
				alertService.add("warning",$filter('translate')('message_publish_subject_not_empty'));
				sendingMsg = false;
				return;
			}
			
			if(app.getChars(params.wmsg_subject) > 80){
				alertService.add("warning",$filter('translate')('global_title_length_limit',{value1:'80',value2:'40'}));
				sendingMsg = false;
				return;
			}
			
			if(!params.wmsg_content_pc){
				alertService.add("warning",$filter('translate')('message_publish_content_not_empty'));
				sendingMsg = false;
				return;
			}
			
			if(app.getChars(params.wmsg_content_pc) > 1000){
				alertService.add("warning",$filter('translate')('global_content_length_limit',{value1:'1000',value2:'500'}));
				sendingMsg = false;
				return;
			}
			
			if(!params.usr_ent_id_str){
				alertService.add("warning",$filter('translate')('message_please_select_receivers'));
				sendingMsg = false;
				return;
			}
			
	  		var sendUrl = "/app/message/sendMessage";

  			Ajax.post(sendUrl, params, function(data){
  				alertService.add('success', $filter('translate')('send_ok'),1000,function(){
  					if(appInd){				    		
  		    			changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
  		    				back();
  		    			});
  			    	}else{
  			    		back();
  			    	}
  				});
  	  		});
		};
		
		//【发送给谁】按钮事件，显示已选择的收件人页面
		$scope.showReceivers = function(){
			sessionStorage.wmsg_subject = $scope.wmsg_subject;
			sessionStorage.wmsg_content_pc = $scope.wmsg_content_pc;
			initPage();
			$scope.show.receiverPage = true;
		};
		
		//已选择收件人页面 左上角 【人仔添加】 按钮事件，点击显示搜索页面
		$scope.selectReceiver = function(){
			initPage();
			$scope.show.receiverSelection = true;
		};
		
		//已选择收件人页面，【人仔减少】 按钮事件，点击，列表进入删除模式
		$scope.showRemove = function(){
			initPage();
			$scope.show.remove = true;
		}

		// 删除模式列表，点击每一项事件
		$scope.removeItemClick = function(index){
			if($scope.show.remove){
				$scope.receivers[index].removeFlag = !$scope.receivers[index].removeFlag;
			}
		}

		Array.prototype.removeValue = function(val) {
		    var index = this.indexOf(val);
		    if (index > -1) {
		        this.splice(index, 1);
		    }
		};

		
		//删除已选择的联系人
		$scope.saveRemove = function(){
			
			var hasSelect = false;
			
			for(var i=0;i<$scope.receiverList.items.length;i++){
				var item = $scope.receiverList.items[i]; 
				if(item.removeFlag){
					item.removeFlag = false;
					item.selected = false;
					$scope.receivers.removeValue(item);
					
					hasSelect = true;
				}
			}
			
			if(!hasSelect){
				alertService.add("warning",$filter('translate')('message_please_select_receivers'),2000);
			}
		};
		
		/*************************搜索收件人列表start****************************/
		var receiverListUrl = "/app/message/findUserList";
		$scope.receiverList = new Loader(receiverListUrl, {search_name:""});
		$scope.search = function(){
			var searchText = "";
			if($scope.searchText){
				searchText = $scope.searchText;
			}
			$scope.receiverList = new Loader(receiverListUrl, {search_name:searchText},function(data){
				for(var i=0;i<data.items.length;i++){
					var item = data.items[i];
					
					var inSelection = false;
					for(var j = 0;j<$scope.receivers.length;j++){
						if(item.usr_ent_id == $scope.receivers[j].usr_ent_id){
							inSelection = true;
							break;
						}
					}
					if(inSelection){
						item.selected = true;
					}
				}
			});
		}
		
		//搜索页面，点击每个item事件，选中当前item
		$scope.clickReceiver = function(field){
			field.receiver.selected = !field.receiver.selected;
		}
		
		$scope.receivers = [];//保存已添加的接受者
		
		// 【保存联系人】按钮事件
		$scope.saveReceivers = function(){
			$scope.receivers.length = 0;
			for(var i=0;i<$scope.receiverList.items.length;i++){
				var item = $scope.receiverList.items[i]; 
				if(item.selected){
					$scope.receivers.push(item);
				}
			}
			initPage();
			$scope.show.receiverPage = true;
		};
		
		/*************************搜索收件人列表end*****************************/
		
	});
});



