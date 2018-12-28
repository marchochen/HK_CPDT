appModule.directive('doing', [ '$filter', 'SNS', 'Store', 'Ajax', 'modalService', 'dialogService',function( $filter, SNS, Store, Ajax, modalService,dialogService){
	return {
		restrict : 'A',
		scope : {
			doingList : '=doing',
			module : '@'
		},
		link : function(scope, element, attrs){
			var replyIds = [];
			var replyToId = 0;
			var toUserId = 0;
			var targetId = 0;
			scope.showWrite = false;
			scope.note = '';
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
			scope.serverHost = serverHost;
			scope.loginUserId = Store.get('loginUser');	
			
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
			
			scope.deleteDoing = function(doingId, targetId, module){
				modalService.modal(function($scope, $modalInstance) {
					$scope.modalText = 'global_sure_delete';
					$scope.modalOk = function() {
						deteleDoing(doingId, targetId, module,function(){
							dialogService.modal("doing_delete","o");
						});
						$modalInstance.dismiss('cancel');
					};
					$scope.modalCancel = function() {
						$modalInstance.dismiss('cancel');
					};
				});
			};
			function deteleDoing(doingId, targetId, module,callBack){
				SNS.cancelDoing(doingId, targetId, module, function(data){
					if(callBack){
						callBack();
					}
					for(var i in scope.doingList.items){
						if(scope.doingList.items[i].s_doi_id == doingId){
							scope.doingList.items.splice(i,1);
							break;
						}
					}
				});
			}
			scope.deleteComment = function(doid, id, targetId, module){
				modalService.modal(function($scope, $modalInstance) {
					$scope.modalText = 'global_sure_delete';
					$scope.modalOk = function() {
						deleteComment(doid, id, targetId, module);
						$modalInstance.dismiss('cancel');
					};
					$scope.modalCancel = function() {
						$modalInstance.dismiss('cancel');
					};
				});
			};
			function deleteComment(doid, id, targetId, module){
				SNS.cancelComment(id, targetId, module, function(data){
					if(data && data.status == 'success'){
						for(var i in scope.doingList.items){
							if(scope.doingList.items[i].s_doi_id == doid){
								findDeleteId(i, id);
								for(var j in replyIds){
									scope.removeReply(i, replyIds[j]);
								}
								replyIds = [];
							}
						}
					}
				});
			}
			scope.removeReply = function(index,id){
				for(var i in scope.doingList.items[index].replies){
					if(scope.doingList.items[index].replies[i].s_cmt_id == id ){
						scope.doingList.items[index].replies.splice(i, 1);
						break;
					}
				}
			}
			function findDeleteId(index,id){
				replyIds.push(id);
				for(var i in scope.doingList.items[index].replies){
					if(scope.doingList.items[index].replies[i].s_cmt_reply_to_id == id ){
						findDeleteId(index, scope.doingList.items[index].replies[i].s_cmt_id);
					}
				}
			}
			scope.flag = function(index){
				return scope.doingList.items[index].s_doi_target_type != '1' && 
					scope.doingList.items[index].s_doi_act != 'like' && 
					scope.doingList.items[index].s_doi_act != 'group_open' && 
					scope.doingList.items[index].s_doi_act != 'group_create' && 
					scope.doingList.items[index].s_doi_act != 'group_dissmiss' && 
					scope.doingList.items[index].s_doi_act != 'group_app' && 
					scope.doingList.items[index].s_doi_act != 'question_add' && 
					scope.doingList.items[index].s_doi_act != 'answer_add' && 
					scope.doingList.items[index].s_doi_act != 'completed_cos' && 
					scope.doingList.items[index].s_doi_act != 'enroll_cos';
			}
			scope.toggleShow = function(index, tarId, repToId, usrId){
				scope.doingList.items[index].replyShow = scope.doingList.items[index].replyShow ? false : true;
				if(scope.doingList.items[index].replyShow){
					scope.sendFocus(tarId, repToId, usrId, scope.doingList.items[index].user.usr_display_bil, index);
				}
			}
			scope.sendFocus = function(tarId, repToId, usrId, name, index){
				if(index!=undefined && index != null && !scope.flag(index)){
					return;
				}
				targetId = tarId || 0;
				replyToId = repToId ? repToId : targetId;
				toUserId = usrId || 0;
				displayName = name;
				angular.element('.cont-info-send').show();
				angular.element('#sendText').focus();
			}
			scope.sendComment = function(){
				if(!scope.note || scope.note == '') return;
				SNS.addComment(targetId, replyToId, toUserId,'0', scope.module, scope.note, function(data){
					if(data){
						for(var i = 0; i<scope.doingList.items.length; i++){
							if(scope.doingList.items[i].s_doi_id == targetId){
								for(var j in data.replies){
									data.replies[j].commentLike = {
										type : 'like',
										count : data.replies[j].snsCount ? data.replies[j].snsCount.s_cnt_like_count:0,
										flag : data.replies[j].is_user_like,
										id : data.replies[j].s_cmt_id,
										module: scope.module,
										isComment : 1,
										tkhId : 0
									};
								}
								scope.doingList.items[i].replies = data.replies;
								scope.doingList.items[i].replyShow = true;
								break;
							}
						}
					}
					replyToId = 0;
		    		toUserId = 0;
		    		targetId = 0;
				});				
				scope.note = '';	
				angular.element('.cont-info-send').hide();
			}
		},
		template : '<div>'+
						'<div class="cont-info-tuwen">'+
						'<div class="tuwen-list-2 clearfix" ng-repeat="doing in doingList.items">'+
                    		'<a ng-if="doing.s_doi_target_type == 1 && doing.operator" class="tuwen-list-2-pic" href="javascript:;" forward="doing.operator.usr_ent_id | personUrl" title="">'+
                        		'<img class="pic-user-w" ng-src="{{serverHost}}{{doing.operator.usr_photo}}" alt=""/>'+
                    		'</a>'+
                    		'<a ng-if="!(doing.s_doi_target_type == 1 && doing.operator)" class="tuwen-list-2-pic" href="javascript:;"  forward="doing.user.usr_ent_id | personUrl" title="">'+
	                         	'<img class="pic-user-w" ng-src="{{serverHost}}{{doing.user.usr_photo}}" alt=""/>'+
                    		'</a>'+
                    		'<div class="tuwen-list-2-box">'+
                         		'<div class="tuwen-list-2-tit-1">'+
                              		'<span class="tuwen-list-2-time">'+
                                   		'{{doing.s_doi_create_datetime | dateFormat}}'+
                              		'</span>'+
                              		'<a ng-if="doing.s_doi_target_type == 1 && doing.operator" href="javascript:;" forward="doing.operator.usr_ent_id | personUrl" class="tuwen-list-2-user" title="">'+
                                   		'{{doing.operator.usr_display_bil}}'+
                              		'</a>'+
                              		'<a ng-if="!(doing.s_doi_target_type == 1 && doing.operator)" href="javascript:;" forward="doing.user.usr_ent_id | personUrl" class="tuwen-list-2-user" title="">'+
                                   		'{{doing.user.usr_display_bil}}'+
                              		'</a>'+
                         		'</div>'+
                         		'<p class="tuwen-list-2-tit-2" ng-click="sendFocus(doing.s_doi_id, doing.s_doi_id, 0, doing.user.usr_display_bil, $index)" html-decode="doing.s_doi_title"></p>'+
                         		'<div class="tuwen-list-2-tit-3" ng-if="doing.fileList && doing.fileList.length>0">'+
               						'<a  ng-repeat="file in doing.fileList" ng-href="javascript:openOtherSiteUrl(\'{{serverHost}}{{file.mtf_url + \'/\' + file.mtf_file_rename}}\');">'+
               							'<img ng-if="file.mtf_type == \'Img\'" width="122" height="85" ng-src="{{serverHost}}{{file.mtf_url + \'/\' + file.mtf_file_rename}}" alt=""/>'+
               							'<img ng-if="file.mtf_type == \'Doc\'" width="122" height="85" src="../../images/wendang.jpg" alt=""/>'+
               							'<img ng-if="file.mtf_type == \'Video\'" width="122" height="85" src="../../images/video.png" alt=""/>'+
               						'</a>'+

          						'</div>'+
                         		'<div class="list-tool-4" ng-if=" flag($index) || doing.s_doi_uid == loginUserId">'+
                              		'<a ng-if = "flag($index)" title="" href="javascript:;" sns="doing.doingLike" class="list-tool-1-laud" ng-class="{1: \'active\', 0: \'\'}[doing.doingLike.flag]"></a>'+
                              		'<a ng-if = "flag($index)" title="" href="javascript:;" ng-click="toggleShow($index, doing.s_doi_id, doing.s_doi_id, 0)"class="list-tool-1-review">{{doing.replies.length}}</a>'+
                              		'<a href="javascript:;" ng-if=" doing.s_doi_uid==loginUserId" class="list-tool-1-close" ng-click="deleteDoing(doing.s_doi_id,doing.s_doi_target_id,doing.s_doi_module)"></a>'+
                         		'</div>'+
                          		'<div class="pl-sop" ng-show="doing.replies&&doing.replies.length>0&&doing.replyShow">'+
                               		'<div ng-repeat = "comment in doing.replies" >'+
                                   		'<div class="pl-sop-info" ng-click="sendFocus(doing.s_doi_id, comment.s_cmt_id, comment.user.usr_ent_id, comment.user.usr_display_bil );" ngCloak>'+
                                        	'<a class="pl-roll-color"  href="javascript:;" forward="comment.user.usr_ent_id | personUrl" title="" ngCloak>'+
                                            	 '{{comment.user.usr_display_bil}}'+
                                        	'</a>'+
                                        	'<span ng-if="comment.toUser!=undefined" class="grayC999" translate="comment_reply"></span>'+
                                        	'<a ng-if="comment.toUser!=undefined" class="pl-roll-color" title="" forward="comment.toUser.usr_ent_id | personUrl"  href="javascript:;" ngCloak>{{comment.toUser.usr_display_bil}}</a>：{{comment.s_cmt_content}}'+
                                   		'</div>'+
                                   		'<div class="pl-sop-tool" ngCloak>'+
                                        	'{{comment.s_cmt_create_datetime | toDate : "yyyy-MM-dd HH:mm" }} &nbsp;'+
                                        	'<a class="list-tool-1-laud"  href="javascript:;" sns="comment.commentLike" title=""></a>'+
                                        	'<a href="javascript:;" ng-if=" comment.s_cmt_uid==loginUserId" ng-click="deleteComment(doing.s_doi_id, comment.s_cmt_id, comment.s_cmt_target_id, comment.s_cmt_module)" class="list-tool-1-close"></a>'+
                                   		'</div>'+
                               		'</div>'+
                          		'</div>'+ 
                    		'</div>'+
               			'</div>'+
						'</div>'+
               			'<div class="cont-info-send" style="display:none">'+
                    		'<div class="pure-form-box-3">'+
                    			'<div class="pure-form-bi" ng-class="{\'send-focus\' : focus}"></div>'+
          						'<div class="pure-form-kang"><input id="sendText" type="text" ng-class="{\'send-text\' : true}" ng-focus="showClass()" ng-blur="hideClass()" ng-model="note" placeholder="{{tip}}"/></div>'+
          						'<a class="pure-form-delete" ng-show="showDel"  ng-click="delText();" href="javascript:void(0)"></a>'+
          					'</div>'+
                    		'<input type="button" class="send-button" ng-click="sendComment()" value="{{ \'comment_send\' | translate }}">'+
               			'</div>'+
               		'</div>',
		replace : true
	};
}]); 