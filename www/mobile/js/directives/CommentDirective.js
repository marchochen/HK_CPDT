appModule.directive('comments', ['$filter','Loader','Store','modalService','SNS','dialogService',function($filter,Loader,Store,modalService,SNS,dialogService) {
	function convert(obj,module){
		return {
			type : 'like',
			count : obj.snsCount ? obj.snsCount.s_cnt_like_count : 0,
			flag : obj.is_user_like ? obj.is_user_like : false,
			module : module,
			id :  obj.s_cmt_id,
			isComment : 1,
			tkhId : 0
		};
	}
	return {
		restrict : 'E',
		scope : {
			module : '@',
			id : '@',
			commentId : '@',
			tkhId : '@',
			count :'@'
		},
		link : function(scope, element, attrs){
		    var pageSize = 10;
		    var replyToId = 0;
		    var toUserId = 0;
		    var targetId = scope.id;
		    var displayName = '';
			scope.serverHost = serverHost;
			scope.note = '';
			scope.showNull = true;
			scope.showDel = true;
			scope.tip = $filter('translate')('comment_tips');
			scope.$watch('note', function(){
	    		if(scope.note == ''){
	    			scope.showDel = false;
	    		}else{
	    			scope.showDel = true;
	    		}
	    	});
	    	scope.delText = function(){
	    		scope.note = '';
	    	}
			scope.loginUser = Store.get('loginUser');
			angular.element('#'+scope.commentId).bind('click', function(){
				scope.sendFocus();
			});
			scope.addLikeObj = function(commmentlist){
				for(var i = (commmentlist.page - 2) * pageSize; i < commmentlist.items.length; i++){
					commmentlist.items[i].commentLike = convert(commmentlist.items[i], scope.module);
					commmentlist.items[i].replyShow = commmentlist.items[i].replyShow ? commmentlist.items[i].replyShow : false;
				}
			};
			scope.toggleReply = function(index, tarId, repToId, usrId, usrName){
				scope.commentList.items[index].replyShow = scope.commentList.items[index].replyShow ? false : true;
				if(scope.commentList.items[index].replyShow){
					scope.sendFocus(tarId, repToId, usrId, usrName);
				}
			};
			//输入框获得焦点
			scope.sendFocus = function(tarId, repToId, usrId, name){
				targetId = tarId || scope.id;
				replyToId = repToId || 0;
				toUserId = usrId || 0;
				displayName = name;
				element.find('#sendText').focus();
			}
			//发送评论
			scope.sendComment = function(){
				if(!scope.note || scope.note == '') return;
				SNS.addComment(targetId, replyToId, toUserId, scope.tkhId?scope.tkhId:'0', scope.module, scope.note, function(data){
					if(data){
						if(data.replies){
							for(var i = 0; i<scope.commentList.items.length; i++){
								if(scope.commentList.items[i].s_cmt_id == targetId){
									scope.commentList.items[i].replies = data.replies;
									scope.commentList.items[i].replyShow = true;
									break;
								}
							}
						}else if(data.comment){
							data.comment.commentLike = convert(data.comment, scope.module);
							data.comment.replyShow = false;
							scope.commentList.items.unshift(data.comment);
							scope.count++;
						}
					}
					replyToId = 0;
		    		toUserId = 0;
		    		targetId = scope.id;
		    		scope.showNull = false;
		    		
		    		dialogService.modal("comment_post","o");
		    		
				});
				scope.note = '';
			}
			//删除
			scope.remove = function(cmtId, targetId, module, isReply, event){
				modalService.modal(function($scope, $modalInstance) {
					$scope.modalText = 'global_sure_delete';
					$scope.modalOk = function() {
						deleteComment(cmtId, targetId, module, isReply);
						$modalInstance.dismiss('cancel');
					};
					$scope.modalCancel = function() {
						$modalInstance.dismiss('cancel');
					};
				});
				if(event){
					event.stopPropagation();
				}
			};
			function deleteComment(cmtId, targetId, module, isReply){
				SNS.cancelComment(cmtId, targetId, module, function(){
					if(isReply){
						var flag = false;
						for(var i in scope.commentList.items){
							for(var j in scope.commentList.items[i].replies){
								if(scope.commentList.items[i].replies[j].s_cmt_id == cmtId){
									scope.commentList.items[i].replies.splice(j, 1);
									flag = true;
									break;
								}
							}
							if(flag)break;
						}
					}else{
						for(var i = 0; i<scope.commentList.items.length; i++){
							if(scope.commentList.items[i].s_cmt_id == cmtId){
								scope.commentList.items.splice(i, 1);
								break;
							}
						}
						scope.count--;
					}
					location.reload();
				});
			}
			scope.focus = false;
			scope.showClass = function(){
				if(replyToId == 0 && toUserId == 0 && targetId == scope.id)
					scope.tip = $filter('translate')('comment_tips');
				else
					scope.tip = $filter('translate')('comment_reply_tip') + ' ' + displayName + ':';
				scope.focus = true;
			}
			scope.hideClass = function(){
				scope.focus = false;
			}
			scope.$watch('id', function(){
				if(scope.id){
					var commentUrl = '/app/comment/' + scope.module + '/commentPageJson/' + scope.id;
			        var params = {
			        	pageSize : pageSize,
						sortname : 's_cmt_create_datetime',
    	 				sortorder : 'desc'
					};
					scope.commentList = new Loader(commentUrl, params);
					targetId = scope.id;
			 	}
			});

		},
		template :
		    '<div>'+
        	'<div class="{{commentList.items.length > 0?\'cont-info-pl\':\'\'}}" style="padding-bottom: 10px;"><div class="pl-roll-area">'+
          		'<div class="pl-roll clearfix" ng-repeat="comment in commentList.items">'+
                	'<a href="javascript:;" forward="comment.user.usr_ent_id | personUrl" class="pl-roll-pic" title=""><img ng-src="{{serverHost}}{{comment.user.usr_photo}}"/></a>'+//加入点击之后跳到个人主页
                	'<div class="pl-roll-box">'+
             		'<div class="pl-roll-fright">'+
                  		'<a class="list-tool-1-laud" href="javascript:;" sns="comment.commentLike" title=""></a> &nbsp;'+
                  		'<a class="list-tool-1-review" href="javascript:;" ng-click="toggleReply($index, comment.s_cmt_id, comment.s_cmt_id, comment.user.usr_ent_id, comment.user.usr_display_bil);" title="">{{comment.replies.length}}</a>'+
                  		'<a href="javascript:;" ng-click="remove(comment.s_cmt_id, comment.s_cmt_target_id, comment.s_cmt_module, false);" ng-show="loginUser==comment.user.usr_ent_id" class="list-tool-1-close"></a>'+
             		'</div>'+
                		'<div class="pl-roll-desc">'+
                     		'<a href="javascript:;" class="pl-roll-user" forward="comment.user.usr_ent_id | personUrl" title="">{{comment.user.usr_display_bil}}</a>'+//加入点击之后跳到个人主页
                		'</div>'+
                		'<p class="pl-roll-time">{{comment.s_cmt_create_datetime | toDate : "yyyy-MM-dd HH:mm" }}</p>'+
                		'<div class="pl-roll-cont" ng-click="sendFocus(comment.s_cmt_id,comment.s_cmt_id,comment.user.usr_ent_id, comment.user.usr_display_bil);">{{comment.s_cmt_content}}</div>'+
                		'<div class="pl-roll-info" ng-click="sendFocus(comment.s_cmt_id,reply.s_cmt_id,reply.user.usr_ent_id, reply.user.usr_display_bil);" ng-show="comment.replyShow" ng-repeat="reply in comment.replies">'+
                		'<a href="javascript:;" ng-click="remove(reply.s_cmt_id, reply.s_cmt_target_id, reply.s_cmt_module, true, $event);" ng-show="loginUser==reply.user.usr_ent_id" class="list-tool-1-shut"></a>'+
                			'<a class="pl-roll-color" title="" href="javascript:;" forward="reply.user.usr_ent_id | personUrl">'+
                    			'{{reply.user.usr_display_bil}}'+
                    		'</a>'+
                    		'<span class="color-gray999">'+
                    			'{{ "comment_reply" | translate }}<!--回复了-->'+
                    		'</span>'+
                    		'<a class="pl-roll-color" title="" href="javascript:;" forward="reply.toUser.usr_ent_id | personUrl">'+
                    			'{{reply.toUser.usr_display_bil}}'+
                    		'</a><span class="color-gray999">：</span>{{reply.s_cmt_content}}'+
                		'</div>'+
                	'</div>'+
          		'</div></div>'+
          		'<div loader="commentList" shownull="showNull" callback="addLikeObj"></div>'+
     		'</div>'+
     		'<div class="cont-info-send">'+
     			'<div class="pure-form-box-3">'+
     				'<div class="pure-form-bi" ng-class="{\'send-focus\' : focus}"></div>'+
          			'<div class="pure-form-kang"><input id="sendText" type="text" ng-class="{\'send-text\' : true}" ng-focus="showClass()" ng-blur="hideClass()" ng-model="note" placeholder="{{tip}}"/></div>'+
          			'<a class="pure-form-delete" ng-show="showDel"  ng-click="delText();" href="javascript:void(0)"></a>'+
          		'</div>'+
          		'<input type="button" class="send-button" ng-class="{\'send-button-er\' : showDel}" ng-click="sendComment()" value="{{ \'comment_send\' | translate }}">'+
     		'</div>'+
     		'</div>',
        replace : true
	};
}]);
