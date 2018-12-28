$.include([
    'js/directives/SNSDirectives.js',
    'js/directives/DoingDirective.js',
    'js/services/SNSService.js',
    'js/services/UserService.js',
    'js/services/UploadService.js',
    'js/json2.min.js'
], '../../');

var alertObj;
var uploadUrl;

$(function(){
    var groupModule = angular.module('group', ['globalCwn', 'userService']);
    groupModule.filter('private', function($filter){
        return function(input){          
            var msg = '';
            switch(input){
                case 0:
                    msg = $filter('translate')('group_type_public');
                    break;
                case 1:
                    msg = $filter('translate')('group_type_private');
                    break;
                case 2:
                    msg = $filter('translate')('group_type_open');
                    break;
            };
            return msg;
        };
    });
    groupModule.controller('mainController', ['$scope', 'Ajax', 'User', function($scope, Ajax, User){
        var url = '/app/group/main/json';
        $scope.user = {};
        $scope.serverHost = serverHost;
        $scope.myGroupTotal = 0;
        $scope.openGroupTotal = 0;
        $scope.findGroupTotal = 0;
        User.userInfo(function(data){
            $scope.user = data.regUser;
        });
        Ajax.post(url, {}, function(data){
            $scope.myGroupTotal = data.myGroupTotal;
            $scope.openGroupTotal = data.openGroupTotal;
            $scope.findGroupTotal = data.findGroupTotal;
        });
    }]);
    groupModule.controller('myGroupController', ['$scope', 'Store', 'Loader', function($scope, Store, Loader){
        var url = '/app/group/getSnsGroupList/my/' + Store.get('loginUser');
        $scope.serverHost = serverHost;
        $scope.groupList = new Loader(url, {},function(data){
        	for(var i=0;i<data.items.length;i++){
        		var item = data.items[i];
        		item.enc_s_grp_id = wbEncryptor.encrypt(item.s_grp_id);
        	}
        });   
        
        var addInd = app.getUrlParam("addInd");
		$scope.back=function(){
			if(addInd == null){
				back();
			} else {
				back(2);
			}
		}
		document.addEventListener('plusready',function(){
			if(addInd != null){
				changeBackButtion(function(){
					back(2);
				});
			}
		},false);
    }]);
    groupModule.controller('detailController', ['$scope', '$window','Store', 'Ajax', 'Loader', 'alertService', 'SNS','dialogService',
        function($scope, $window, Store, Ajax, Loader, alertService, SNS , dialogService){
    	alertObj = alertService;
        uploadUrl = serverHost + '/app/upload/mobile/Group/';
        var groupId = app.getUrlParam('groupId') || Store.get('groupId');
        var listUrl = '/app/doing/user/json/' + Store.get('loginUser')  + '/Group/' + groupId;
        var detailUrl = '/app/group/detail/json/'+groupId;
        var params = {
            isMobile : true
        };
        $scope.uploadType = 'ALL';
        $scope.doingList = {};
        $scope.doingText = '';
        $scope.isGroupMember = false;
        $scope.s_grp_private = 0;
        $scope.snsGroup = {};
        $scope.isManager = false;
        $scope.desc_length = 0;
        $scope.isNormal = false;
        $scope.showDetail = true;
        $scope.showWrite = false;
        loaderList();
        function loaderList(){
            $scope.doingList = new Loader(listUrl, params, function(data){
                for(var i in data.items){
                    data.items[i].doingLike = {
                        type : 'like',
                        count : data.items[i].snsCount ? data.items[i].snsCount.s_cnt_like_count : '0',
                        flag : data.items[i].is_user_like,
                        id :  data.items[i].s_doi_id,
                        module : 'Group',
                        tkhId : 0
                    };
                    data.items[i].replyShow=false;
                    for(var j in data.items[i].replies){
                        data.items[i].replies[j].commentLike = {
                            type : 'like',
                            count : data.items[i].replies[j].snsCount ? data.items[i].replies[j].snsCount.s_cnt_like_count:0,
                            flag : data.items[i].replies[j].is_user_like,
                            id : data.items[i].replies[j].s_cmt_id,
                            module: 'Group',
                            isComment : 1,
                            tkhId : 0
                        };
                    }
                }
                console.log(data);
            });
        }
        Ajax.post(detailUrl, {}, function(data){            
            $scope.isGroupMember = data.isGroupMember;
            $scope.s_grp_private = data.s_grp_private;
            $scope.snsGroup = data.snsGroup;
            $scope.isManager = data.isManager;
            $scope.desc_length = data.desc_length;
            $scope.isNormal = data.isNormal;
        });
        $scope.checkType = function(type){
            $scope.uploadType = type; 
        }
        $scope.toggleWrite = function(){
            $scope.showDetail = $scope.showDetail ? false : true;
            $scope.showWrite = $scope.showWrite ? false : true;
        }
        $scope.publishDoing = function(){
            if($scope.doingText == ''){
                alertService.add('warning', 'group_tip_doing_notNull', 2000);//'评论不能为空！'
                return;
            }
            if($scope.doingText.length > 200){
                alertService.add('warning', 'group_tip_doing_length', 2000);//'不能超过100个字符'
                return;
            }
            SNS.addDoing($scope.doingText, 'group', 'Group', groupId, function(data){
                if(data.status == 'success'){
                	dialogService.modal("group_message_post","o",function(){
                		$scope.toggleWrite();
                        $scope.doingText = '';
                        deleteAllImg();
                        loaderList();
                	});
                }
            });
        }
        $scope.showInfomation = function(){
            var url = 'information.html?groupId=' + groupId; 
            //$window.location.href = url;
            clicked(url, true);
        };
        //申请加入群组
        $scope.applyJoin = function(){
            var url = '/app/group/applyJoinGroup/' + groupId;
            Ajax.post(url, {}, function(){   
            	
            	dialogService.modal("welcome_join_group","o",function(){
            		if(appInd){
                		plus.webview.getWebviewById('groupMain').evalJS("changeTotal('mgt','add')");
                		plus.webview.getWebviewById('groupMain').evalJS("changeTotal('fgt','cancel')");
                	}
                    if(!$scope.snsGroup.s_gpm)
                        $scope.snsGroup.s_gpm = { s_gpm_status : 0 };
                    else
                        $scope.snsGroup.s_gpm.s_gpm_status = 0;
            	});
            });
        };        
    }]);
    groupModule.controller('informationController', ['$scope', '$window', '$filter', 'Store', 'Ajax', 'Loader', 'alertService', 'modalService',
        function($scope, $window, $filter, Store, Ajax, Loader, alertService, modalService){
        var groupId = app.getUrlParam('groupId') || Store.get('groupId');
        var detailUrl = '/app/group/detail/json/' + groupId;
        var memberUrl = '/app/group/getSnsGroupMemberList/myMember/' + groupId;
        var approveUrl = '/app/group/getSnsGroupMemberList/pending/' + groupId;
        $scope.isGroupMember = false;
        $scope.s_grp_private = 0;
        $scope.snsGroup = {};
        $scope.isManager = false;
        $scope.desc_length = 0;
        $scope.isNormal = false; 
        $scope.groupId = groupId;
        $scope.memberList = {};   //成员列表
        $scope.memberAppList = {}; //成员审批列表
        $scope.serverHost = serverHost;
        $scope.edit = {
            title : '',
            text : '',
            key :''
        };                       //用来存编辑信息
        $scope.show = {
            detail : true,
            editText : false,                        
        }
        Ajax.post(detailUrl, {}, function(data){            
            $scope.isGroupMember = data.isGroupMember;
            $scope.s_grp_private = data.s_grp_private;
            $scope.snsGroup = data.snsGroup;
            $scope.isManager = data.isManager;
            $scope.desc_length = data.desc_length;
            $scope.isNormal = data.isNormal;
            //加载成员列表
            if($scope.s_grp_private!=2 && $scope.isGroupMember != null && $scope.isGroupMember || !$scope.isNormal ){                
                $scope.memberList = new Loader(memberUrl, {pageSize : 8});
                $scope.memberList.loaderMore(1);
            }
            //加载成员审批
            if(($scope.s_grp_private != 2 && ($scope.isGroupMember != null && $scope.isGroupMember || !$scope.isNormal))
                && ( $scope.isManager != null && $scope.isManager || !$scope.isNormal)){
                $scope.memberAppList = new Loader(approveUrl, {});
                $scope.memberAppList.loaderMore(1);
            }
        });
        //指定显示哪个页面
        $scope.showPage = function(key){
            for(var i in $scope.show) $scope.show[i] = false;
            $scope.show[key] = true;
        }
        $scope.toggle = function(){
            toggle();
        };
        //所有编辑click事件都要经过的方法，判断可以编辑在跳转到编辑页面
        $scope.edit = function(type, title, key){
            if($scope.isManager != null && $scope.isManager || !$scope.isNormal ){
                switch(type){
                    case 'select':
                    	if($scope.s_grp_private !== 2){//在修改群组时，“开放性群组”不能转化成其它类型的群组
                    		$scope.toggle();
                    	}
                        break;
                    case 'text' :
                        showTextPage(title, key); 
                        break;
                }
            }
        };
        $scope.saveText = function(){
            if($scope.edit.text.replace(/(^\s*)|(\s*$)/g, '') != ''){
                $scope.snsGroup[$scope.edit.key] = $scope.edit.text.replace(/(^\s*)|(\s*$)/g, '');
            }   
            if($scope.edit.text == ''){
            	alertService.add('warning','group_name_not_null',2000);
            }else{
            	$scope.showPage('detail');
            	 return
            }
            
        }
        $scope.saveSelect = function(val){
            if(val == 'NAN') {
                $scope.toggle();   
                return;
            }
            $scope.snsGroup.s_grp_private = val;
            //$scope.toggle();
        }
        //退出该群/解散该群
        $scope.exit = function(){            
        	modalService.modal(function($scope, $modalInstance) {
                var url = '/app/group/signOutGroup/' + groupId;
                $scope.modalText = 'global_sure';
                $scope.modalOk = function() {
                	if(appInd){
                		var wait = plus.nativeUI.showWaiting();
                	}
                    Ajax.post(url, {}, function(){
                        if(appInd){
                        	if($scope.s_grp_private == 2){
	                    		plus.webview.getWebviewById('groupMain').evalJS("changeTotal('ogt','cancel')");
	                    	} else {
	                    		plus.webview.getWebviewById('groupMain').evalJS("changeTotal('mgt','cancel')");
	                    	}
                        	changeWebviewDetail(plus.webview.currentWebview().opener().opener().id, function(){
                        		back(2);
                        		wait.close();
                        	});
                        } else {
                        	back(2);
                        }
                    });
                };
                $scope.modalCancel = function() {
                    $modalInstance.dismiss('cancel');
                };
            });
           
        }
        //更新群组信息
        $scope.updateGroup = function(){
            var url = '/app/group/detail/mobile/update';
            var snsGroup = {
                s_grp_id : $scope.snsGroup.s_grp_id,
                s_grp_private : $scope.snsGroup.s_grp_private,
                s_grp_title : encodeURIComponent($scope.snsGroup.s_grp_title),
                s_grp_desc : encodeURIComponent($scope.snsGroup.s_grp_desc)
            }
            var params = {
                json : JSON.stringify(snsGroup)
            }            
            Ajax.post(url, params, function(data){
                if(data && data.status == 'success'){                   
                    toggleSuceess(function(){
                        //$window.location.href = 'myGroup.html';
                    	back(2);
                    });
                }
            });
        }
        function showTextPage(title, key){
            $scope.edit.title = $filter('translate')(title);
            $scope.edit.key = key;
            $scope.edit.text = $scope.snsGroup[key];
            $scope.showPage('editText');
        }
    }]);
    groupModule.controller('memberController', ['$scope', 'Store', 'Ajax', 'Loader', 'alertService', function($scope, Store, Ajax, Loader, alertService){
        var groupId = app.getUrlParam('groupId') || Store.get('groupId');
        var detailUrl = '/app/group/detail/json/' + groupId;
        var memberUrl = '/app/group/getSnsGroupMemberList/myMember/' + groupId;
        var findMemberUrl = '/app/group/findGroupMemberList/' + groupId; //添加成员列表的URL
        $scope.isGroupMember = false;        
        $scope.s_grp_private = 0;
        $scope.snsGroup = {};
        $scope.isManager = false;       
        $scope.isNormal = false;        
        $scope.serverHost = serverHost;	

		$scope.showRemove = function(){
			$scope.remove = !$scope.remove;
		};

        $scope.show = {
            member : true,
            add : false
        };
        
		//回退按钮
		$scope.goBack = function(){
			if($scope.remove){//如果是删除模式，则回退取消
				$scope.showRemove();
			}else{
				back();
			}
		}

        $scope.searchContent = '';
        $scope.findMemberList = {};
        $scope.memberList = new Loader(memberUrl, {});   //成员列表

		// 删除模式列表，点击每一项事件
		$scope.removeItemClick = function(index){
			var item = $scope.memberList.items[index];
			if($scope.remove){
				if(item.s_gpm_usr_id == $scope.snsGroup.s_grp_uid){
					alertService.add("warning",'group_admin_can_not_de_deleted',2000);
					return;
				}
				item.removeFlag = !item.removeFlag;
			}
		}

		Array.prototype.removeValue = function(val) {
		    var index = this.indexOf(val);
		    if (index > -1) {
		        this.splice(index, 1);
		    }
		};
		
		//删除已选择的群组成员
		// 发请求到服务端删除，成功之后，删除集合中对应项
		$scope.saveRemove = function(){
			var url = '/app/group/deleteGroupMembers/'+groupId;
            var usrEntIds = '';
            var totalMember = 0;
            
			for(var i=0;i<$scope.memberList.items.length;i++){
				var item = $scope.memberList.items[i];
				if(item.removeFlag){
					item.removeFlag = false;
					$scope.memberList.items.removeValue(item);
					i--;
					
					usrEntIds += item.user.usr_ent_id + ',';
					totalMember++;
				}
			}
			if( usrEntIds == '' ) return;
            usrEntIds = usrEntIds.substring(0,usrEntIds.length-1);
            
            Ajax.post(url, {usrEntIds : usrEntIds}, function(data){
                if(data && data.status == 'success'){
                	if(appInd){
                		plus.webview.currentWebview().opener().opener().evalJS("changeTotal('gmt','add','" + totalMember + "')");
                		plus.webview.currentWebview().opener().opener().opener().evalJS("changeTotal('tm_" + groupId + "','add','" + totalMember + "')");
                		plus.webview.currentWebview().opener().opener().opener().evalJS("changeTotal('gm_" + groupId + "','add','" + totalMember + "')");
                		changeWebviewDetail(plus.webview.currentWebview().opener().id, function(){
                			wait.close();
                			alertService.add('success', 'group_delete_success', 2000);
		                    $scope.memberList = new Loader(memberUrl, {});   //成员列表
		                    $scope.$apply(function(){
		                    	$scope.showPage('member');
		                    });
                		});
                	} else {
                		alertService.add('success', 'group_delete_success', 2000);
	                    $scope.memberList = new Loader(memberUrl, {});   //成员列表
	                    $scope.showPage('member');
                	}
                }
            }); 
            
		};

        $scope.showPage = function(key){
            $scope.searchContent = '';
            for(var i in $scope.show) $scope.show[i] = false;
            $scope.show[key] = true;
            if( key == 'add'){
                $scope.findMemberList = new Loader(findMemberUrl, {});
            }        
        };
        $scope.addMembers = function(){
        	if(appInd){
        		var wait = plus.nativeUI.showWaiting();
        	}
            var url = '/app/group/addGroupMembers/'+groupId;
            var usrEntIds = '';   
            var totalMember = 0;
            for(var i in $scope.findMemberList.items){
                if($scope.findMemberList.items[i].select){
                    usrEntIds += $scope.findMemberList.items[i].usr_ent_id + ',';
                    totalMember++;
                }
            }
            if( usrEntIds == '' ) return;
            usrEntIds = usrEntIds.substring(0,usrEntIds.length-1);            
            Ajax.post(url, {usrEntIds : usrEntIds}, function(data){
                if(data && data.status == 'success'){
                	if(appInd){
                		plus.webview.currentWebview().opener().opener().evalJS("changeTotal('gmt','add','" + totalMember + "')");
                		plus.webview.currentWebview().opener().opener().opener().evalJS("changeTotal('tm_" + groupId + "','add','" + totalMember + "')");
                		plus.webview.currentWebview().opener().opener().opener().evalJS("changeTotal('gm_" + groupId + "','add','" + totalMember + "')");
                		changeWebviewDetail(plus.webview.currentWebview().opener().id, function(){
                			wait.close();
                			alertService.add('success', 'group_add_success', 2000);
		                    $scope.memberList = new Loader(memberUrl, {});   //成员列表
		                    $scope.$apply(function(){
		                    	$scope.showPage('member');
		                    });
                		});
                	} else {
                		alertService.add('success', 'group_add_success', 2000);
	                    $scope.memberList = new Loader(memberUrl, {});   //成员列表
	                    $scope.showPage('member');
                	}
                }
            });       
        };
        Ajax.post(detailUrl, {}, function(data){
            $scope.isGroupMember = data.isGroupMember;
            $scope.s_grp_private = data.s_grp_private;
            $scope.snsGroup = data.snsGroup;
            $scope.isManager = data.isManager;           
            $scope.isNormal = data.isNormal;
            $scope.s_grp_private = data.s_grp_private;
        });
        $scope.select = function(index){
            $scope.findMemberList.items[index].select = $scope.findMemberList.items[index].select ? false : true;
        };
        
        //是否只显示讲师
        $scope.instrOnly = false;
        //显示的标签文本  options：【仅显示讲师】 【显示全部】
        $scope.instrOnlyLabel = 'group_search_instr_only';
        /**
         * 选择是否【只显示讲师】
         */
        $scope.selectSearchType = function(){
        	if($scope.instrOnly){
        		$scope.instrOnly = false;
        		$scope.instrOnlyLabel = "group_search_instr_only";
        	}else{
        		$scope.instrOnly = true;
        		$scope.instrOnlyLabel = "group_search_show_all";
        	}
        	$scope.search();
        }
        
        $scope.search = function(){
            var params = {
                searchContent : $scope.searchContent
            };
            if($scope.show.member){
                $scope.memberList = new Loader(memberUrl, params); 
            }else if($scope.show.add){
            	params.instrOnly = $scope.instrOnly;//是否只显示讲师
                $scope.findMemberList = new Loader(findMemberUrl, params);
            }
        };
        
    }]);
    groupModule.controller('managerController', ['$scope', '$window', 'Store', 'Ajax', 'Loader', 'alertService',
        function($scope, $window, Store, Ajax, Loader, alertService){
            var groupId = app.getUrlParam('groupId') || Store.get('groupId');
            var memberUrl = '/app/group/getSnsGroupMemberList/myMember/' + groupId;
            $scope.loginUserId = Store.get('loginUser');            
            $scope.serverHost = serverHost;
            $scope.searchContent = '';
            var params = {
                searchContent : $scope.searchContent
            };
            $scope.memberList = new Loader(memberUrl, params);   //成员列表
            $scope.select = function(index){
                for(var i in $scope.memberList.items) $scope.memberList.items[i].select = false;
                $scope.memberList.items[index].select = true ;
            };
            $scope.search = function(){
                params.searchContent = $scope.searchContent;                            
                $scope.memberList = new Loader(memberUrl, params);         
            };
            //转让管理员
            $scope.changeManager = function(){
                var userId = 0; 
                for(var i in $scope.memberList.items){
                    if($scope.memberList.items[i].select){
                        userId = $scope.memberList.items[i].user.usr_ent_id;
                        break;
                    }
                }
                var url = '/app/group/changeManager/' + groupId + '/' + userId;
                Ajax.post(url, {}, function(){
                	if(appInd){
                		changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
                			back();
                		});
                	} else {
                   		back();
                    }
                    //$window.location.href = 'information.html?groupId='+groupId;
                });
            };
    }]);
    groupModule.controller('approveController', ['$scope', 'Store', 'Ajax', 'Loader', function($scope, Store, Ajax, Loader){
        var groupId = app.getUrlParam('groupId') || Store.get('groupId');
        var pendingUrl = '/app/group/getSnsGroupMemberList/pending/' + groupId;
        var admittedUrl = '/app/group/getSnsGroupMemberList/admitted/' + groupId;
        var rejectedUrl = '/app/group/getSnsGroupMemberList/rejected/' +groupId;
        $scope.pendingList = new Loader(pendingUrl , {}, function(data){ console.log(data); });
        $scope.admittedList = new Loader(admittedUrl , {}, function(data){ console.log(data); });
        $scope.rejectedList = new Loader(rejectedUrl , {}, function(data){ console.log(data); });
        $scope.updateGpmStatus = function(index, status){
            var gmList = new Array();
            var usrEntIdList = new Array();
            gmList.push($scope.pendingList.items[index].s_gpm_id);
            usrEntIdList.push($scope.pendingList.items[index].s_gpm_usr_id);
            var url =  '/app/group/updateGpmStatus/' +groupId+ '/' + status + '/' + gmList + '/' + usrEntIdList;
            Ajax.post(url, {}, function(){
            	if(appInd && status == 1){
            		plus.webview.currentWebview().opener().opener().evalJS("changeTotal('gmt','add')");
            		plus.webview.currentWebview().opener().opener().opener().evalJS("changeTotal('tm_" + groupId + "','add')");
            		changeWebviewDetail(plus.webview.currentWebview().opener().id);
            	}
                $scope.pendingList = new Loader(pendingUrl , {}, function(data){ console.log(data); });
                $scope.admittedList = new Loader(admittedUrl , {}, function(data){ console.log(data); });
                $scope.rejectedList = new Loader(rejectedUrl , {}, function(data){ console.log(data); });
            });
        }
    }]);
    groupModule.controller('findController', ['$scope', 'Store', 'Loader', function($scope, Store, Loader){
        var usrEntId = Store.get('loginUser');
        var url = '/app/group/getSnsGroupList/find/' + usrEntId;
        $scope.serverHost = serverHost;
        $scope.findList = new Loader(url, {},function(data){
        	for(var i=0;i<data.items.length;i++){
        		var item = data.items[i];
        		item.enc_s_grp_id = wbEncryptor.encrypt(item.s_grp_id);
        	}
        });
    }]);
    groupModule.controller('openController', ['$scope', 'Store', 'Loader', function($scope, Store, Loader){
        var url = '/app/group/getSnsGroupList/open/' + Store.get('loginUser');
        $scope.serverHost = serverHost;
         $scope.openList = new Loader(url, {},function(data){
        	 for(var i=0;i<data.items.length;i++){
         		var item = data.items[i];
         		item.enc_s_grp_id = wbEncryptor.encrypt(item.s_grp_id);
         	}
         });
    }]);
    groupModule.controller('createController', ['$scope', '$window','$filter', 'Ajax', 'alertService', 'dialogService' ,function($scope, $window, $filter, Ajax, alertService ,dialogService){
        $scope.date = new Date();
        $scope.group = {
            s_grp_title : '',
            s_grp_desc : '',
            s_grp_private : 0           
        };
        $scope.edit = {
            title : '',
            text : '',
            tip : '',
            key :''
        };
        $scope.show = {
            create : true,
            edit : false
        };
        $scope.toggleShow = function(){
            $scope.show.create = $scope.show.create ? false : true;
            $scope.show.edit = $scope.show.edit ? false : true;            
        };
        $scope.showSelect = function(){
            toggle();
        };
        $scope.select = function(val){
            $scope.group.s_grp_private = val;
           // toggle();
        };
        $scope.edit = function(title, key){
            $scope.edit.title = $filter('translate')(title);
            $scope.edit.key = key;
            $scope.edit.text = $scope.group[key];
            if(key == 's_grp_title'){
                $scope.edit.tip = 'group_tip_name';
            }else{
                $scope.edit.tip = 'group_tip_content';
            }
            $scope.toggleShow();
        };
        $scope.saveText = function(){
            $scope.group[$scope.edit.key] = $scope.edit.text;
            $scope.toggleShow();
        };
        $scope.create = function(){
            var url = '/app/group/mobile/create';
            if($scope.group.s_grp_title == ''){
                alertService.add('warning', 'group_tip_title_not_null', 2000);
                return;
            }
            if(appInd){
            	var wait = plus.nativeUI.showWaiting();
            }
            alertService.add('warning', 'tip_wait');
            Ajax.post(url, $scope.group, function(data){
                alertService.closeAllAlerts();
                if(data && data.status == 'error'){
                    alertService.add('warning', 'group_tip_title_created', 3000);                    
                }else{
                	
                	dialogService.modal("group_tip_create_success","o",function(){
                		clicked('myGroup.html?addInd=true', true);
                    	if(appInd){
	                    	if($scope.group.s_grp_private == 2){
	                    		plus.webview.getWebviewById('groupMain').evalJS("changeTotal('ogt','add')");
	                    	} else {
	                    		plus.webview.getWebviewById('groupMain').evalJS("changeTotal('mgt','add')");
	                    	}
	                    	wait.close();
                    	}
                	});
                }
            });
        };

    }]);
    function toggle(){       
		$(".yinsi-desc").width($("#wizwrap").width()).toggleClass("show");
		$(".yinsi-info").width($("#wizwrap").width()).slideToggle();
		$(".yinsi-desc").height($(window).height());
    }
    function toggleSuceess(callback){
        $('.header-tip').slideToggle(callback);
        $(".header-overlay").toggleClass("show");
    }
    
});
