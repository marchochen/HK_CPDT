$.include([
    'js/directives/SNSDirectives.js',
    'js/directives/DoingDirective.js',
    'js/services/SliderService.js',
    'js/services/SNSService.js',
    'js/services/UploadService.js',
    'js/services/UserService.js',
    'js/directives/DatePickerDirective.js',
    'js/json2.min.js',
    'js/services/knowledgeCenterService.js',
    'js/common/courseAndExamCommon.js'
], '../../');

var alertObj;
var uploadUrl;

$(function(){
	var personalModule = angular.module('personal', ['globalCwn', 'userService']);
	personalModule.filter('creditsName', ['$filter', function($filter){
		return function(input){
			if(input.cty_manual_ind && input.cty_title != "SYS_ANWSER_BOUNTY" && input.cty_title != "INTEGRAL_EMPTY" && input.cty_title != "ITM_INTEGRAL_EMPTY" && input.cty_title != "ITM_IMPORT_CREDIT" &&  input.cty_title != "API_UPDATE_CREDITS" && input.cty_title != "SYS_QUESTION_BOUNTY"){
				return input.cty_title;
			}else{
				return $filter('translate')('credits_'+input.cty_title);
			}
		};
	}]);
    personalModule.filter('sexText', ['$filter', function($filter){
    	return function(input){
    		var msg = $filter('translate')('personal_files_null');
    		if(input&&input!=''){
    			if(input == 'M'){
    				msg = $filter('translate')('personal_files_sex_M');
    			}else if(input == 'F'){
    				msg = $filter('translate')('personal_files_sex_F');
    			}
    		}
    		return msg;
    	};
    }]);
	personalModule.controller('homeController', ['$scope', '$filter', 'Ajax', 'Store', 'alertService', 'User', 'dialogService', function($scope, $filter, Ajax, Store, alertService, User,dialogService) {
		var personalId = app.getUrlParam('person') || Store.get('person');
		var url = '/app/personal/home/json/' + personalId;
		$scope.personalId = personalId;
		$scope.serverHost = serverHost;
		$scope.loginUserId = Store.get('loginUser');
		$scope.showWrite = false;            //控制编辑站内信的显示
		$scope.loginUser = {};               //登录用户的信息
		$scope.user = {};                    //用户信息
		$scope.likes = 0;                    //赞总数
		$scope.attent = 0;                   //关注数
		$scope.fans = 0;                     //粉丝数
		$scope.snsSetting = {};              //权限设置
		$scope.isMeInd = false;              //是否是本人进入
		$scope.totalCredits = 0;             //积分数
		$scope.msgText = '';
		$scope.messageCount = ''             //用户未读站内信个数
		
		$scope.appInd = appInd;
		
		Ajax.post(url, {}, function(data){
			if(data){
				$scope.user = data.regUser;
				$scope.likes = data.likes;
				$scope.attent = data.attent;
				$scope.fans = data.fans;
				$scope.snsSetting = data.snsSetting;
				$scope.isMeInd = data.isMeInd;
				$scope.totalCredits = data.total_credits;
				$scope.hasStaff = data.hasStaff;
				
				//未读站内信，等于0不显示，大于99显示99+
				$scope.messageCount = data.messageCount == 0 ? '' : (data.messageCount > 99 ? "99+" : data.messageCount);
			}
		});
		User.userInfo(function(data){
			$scope.loginUser = data.regUser;
		});
		$scope.toggleWrite = function (){
			$scope.showWrite = $scope.showWrite ? false : true;
		};
		$scope.changeAttention = function (){
			var attentUrl = '';
			var params = {};
			if( $scope.user.snsAttention != undefined &&
				$scope.user.snsAttention.s_att_target_uid != undefined &&
				$scope.user.snsAttention.s_att_source_uid != 0){
				attentUrl = '/app/personal/cancelAttention/' + personalId;
			}else{
				attentUrl = '/app/personal/addAttention/' + personalId;
			}
			Ajax.post(attentUrl, params, function(){

				if( $scope.user.snsAttention != undefined &&
					$scope.user.snsAttention.s_att_target_uid != undefined &&
					$scope.user.snsAttention.s_att_source_uid != 0){
					dialogService.modal("attend_cancel","o",function(){
						if(appInd){
							if(plus.webview.getWebviewById("personal" + $scope.loginUserId) != undefined){
								plus.webview.getWebviewById("personal" + $scope.loginUserId).evalJS("changeTotal('attent_" + $scope.loginUserId + "', 'cancel')");
							}
							if(plus.webview.getWebviewById("personalAttention" + $scope.loginUserId) != undefined){
								plus.webview.getWebviewById("personalAttention" + $scope.loginUserId).evalJS("refreshAttentionList()");
							}
						}
						$scope.fans--;
						$scope.user.snsAttention = {};
					});
					
				}else{
					dialogService.modal("attend_success","o",function(){
						if(appInd){
							if(plus.webview.getWebviewById("personal" + $scope.loginUserId) != undefined){
								plus.webview.getWebviewById("personal" + $scope.loginUserId).evalJS("changeTotal('attent_" + $scope.loginUserId + "', 'add')");
							}
							if(plus.webview.getWebviewById("personalAttention" + $scope.loginUserId) != undefined){
								plus.webview.getWebviewById("personalAttention" + $scope.loginUserId).evalJS("refreshAttentionList()");
							}
						}
						$scope.fans++;
						if(!$scope.user.snsAttention) $scope.user.snsAttention = {};
						$scope.user.snsAttention.s_att_source_uid = $scope.loginUserId;
						$scope.user.snsAttention.s_att_target_uid = $scope.user.usr_ent_id;
					});
				}
			});
		}
		//进入离线学习界面
		$scope.showOffline = function(){
			webview = plus.webview.getWebviewById("offline");
			if(webview == undefined){
				webview = plus.webview.create("../module/offline.html","offline",{scrollIndicator:'none',scalable:false});
			}
			webview.show('slide-in-right', 200);
		};
		
		//发送站内信
		$scope.publishMsg = function(){
			if($scope.msgText == ''){
				alertService.add('warning', 'detail_comment_not_empty', 2000);
				return;
			}
			var url = '/app/subordinate/sendWebMessage/' + personalId;
			var params = {
				wmsg_subject : $filter('translate')('personal_msg_post_title', {value:$scope.loginUser.usr_display_bil}),
				wmsg_content : $scope.msgText
			}
			Ajax.post(url, params, function(){
				alertService.add('warning', 'send_ok', 2000);
				$scope.toggleWrite();
			});
		}
	}]);
	personalModule.controller('creditsController', ['$scope', 'Loader', 'Ajax', 'Store',
		function($scope, Loader, Ajax, Store){
			var personalId = app.getUrlParam('person') || Store.get('person');
			var url = '/app/personal/home/json/' + personalId;
			var listUrl = '/app/personal/getCreditDetail/' + personalId;
			$scope.totalCredits = 0;             //积分数
			$scope.activityCredits = 0;             //活动积分数
			$scope.trainCredits = 0;             //培训积分数
			$scope.isMeInd = false;
			Ajax.post(url, {}, function(data){
				$scope.totalCredits = data.total_credits;
				$scope.activityCredits = data.activity_credits;
				$scope.trainCredits = data.train_credits;
				$scope.isMeInd = data.isMeInd;
			});
			$scope.creditsList = new Loader(listUrl,{});
		}
	]);
	personalModule.controller('likeController', ['$scope', 'Loader', 'Store', function($scope, Loader, Store){
		var personalId = app.getUrlParam('person') || Store.get('person');
		var url = '/app/personal/getLikeList/' + personalId;
		var params = {
			isMobile : true
		};
		$scope.serverHost = serverHost;
		$scope.likeList = new Loader(url,params,function(data){
			for(var i in data.items){
				var item = data.items[i];
				data.items[i].title = item.title.replace(/javasc#ript/g,"javascript").replace("onclick=\"openOtherSiteUrl(\'", "onclick=\"").replace(" ')\"", "\"").replace("')\"", "\"");
			}
		});
	}]);
	personalModule.controller('fansController', ['$scope', 'Loader', 'Store', 'Ajax', function($scope, Loader, Store, Ajax){
		var personalId = app.getUrlParam('person') || Store.get('person');
		var url = '/app/personal/getUserList/fans/' + personalId;
		$scope.serverHost = serverHost;
		$scope.loginUserId = Store.get('loginUser');
		$scope.searchContent = '';
		$scope.fansList = {};
		loaderList();
		$scope.search = function(){
			loaderList();
		};
		$scope.changeAttention = function (index){
			var fans = $scope.fansList.items[index];
			var attentUrl = '';
			var params = {};
			if( fans.snsAttention != undefined &&
				fans.snsAttention.s_att_target_uid != undefined){
				attentUrl = '/app/personal/cancelAttention/' + fans.usr_ent_id;
			}else{
				attentUrl = '/app/personal/addAttention/' + fans.usr_ent_id;
			}
			Ajax.post(attentUrl, params, function(){
				if( fans.snsAttention != undefined &&
					fans.snsAttention.s_att_target_uid != undefined){
					if(appInd){
						if(plus.webview.getWebviewById("personal" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personal" + $scope.loginUserId).evalJS("changeTotal('attent_" + $scope.loginUserId + "', 'cancel')");
						}
						if(plus.webview.getWebviewById("personalAttention" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personalAttention" + $scope.loginUserId).evalJS("refreshAttentionList()");
						}
					}
					$scope.fansList.items[index].snsAttention = {};
				}else{
					if(appInd){
						if(plus.webview.getWebviewById("personal" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personal" + $scope.loginUserId).evalJS("changeTotal('attent_" + $scope.loginUserId + "', 'add')");
						}
						if(plus.webview.getWebviewById("personalAttention" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personalAttention" + $scope.loginUserId).evalJS("refreshAttentionList()");
						}
					}
					if(!$scope.fansList.items[index].snsAttention) $scope.fansList.items[index].snsAttention = {};
					if($scope.loginUserId == personalId){
						$scope.fansList.items[index].snsAttention.s_att_source_uid = $scope.loginUserId;
					}
					$scope.fansList.items[index].snsAttention.s_att_target_uid = fans.usr_ent_id;
				}
			});
		};
		function loaderList(){
			$scope.fansList = new Loader(url, {searchContent : $scope.searchContent});
		}
	}]);
	personalModule.controller('attentionController', ['$scope', 'Loader', 'Store', 'Ajax', function($scope, Loader, Store, Ajax){
		var personalId = app.getUrlParam('person')  || Store.get('person');
		var url = '/app/personal/getUserList/attention/' + personalId;
		$scope.serverHost = serverHost;
		$scope.loginUserId = Store.get('loginUser');
		$scope.searchContent = '';
		$scope.attList = {};
		loaderList();
		$scope.search = function(){
			loaderList();
		};
		$scope.changeAttention = function (index){
			var attention = $scope.attList.items[index];
			var attentUrl = '';
			var params = {};
			if( attention.snsAttention != undefined &&
				attention.snsAttention.s_att_target_uid != undefined){
				attentUrl = '/app/personal/cancelAttention/' + attention.usr_ent_id;
			}else{
				attentUrl = '/app/personal/addAttention/' + attention.usr_ent_id;
			}
			Ajax.post(attentUrl, params, function(){
				if( attention.snsAttention != undefined &&
					attention.snsAttention.s_att_target_uid != undefined){
					if(appInd){
						if(plus.webview.getWebviewById("personal" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personal" + $scope.loginUserId).evalJS("changeTotal('attent_" + $scope.loginUserId + "', 'cancel')");
						}
						if(plus.webview.getWebviewById("personalAttention" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personalAttention" + $scope.loginUserId).evalJS("refreshAttentionList()");
						}
					}
					$scope.attList.items[index].snsAttention = {};
					if($scope.loginUserId == personalId){
						$scope.attList.items.splice(index, 1);
					}
				}else{
					if(appInd){
						if(plus.webview.getWebviewById("personal" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personal" + $scope.loginUserId).evalJS("changeTotal('attent_" + $scope.loginUserId + "', 'add')");
						}
						if(plus.webview.getWebviewById("personalAttention" + $scope.loginUserId) != undefined){
							plus.webview.getWebviewById("personalAttention" + $scope.loginUserId).evalJS("refreshAttentionList()");
						}
					}
					if(!$scope.attList.items[index].snsAttention) $scope.attList.items[index].snsAttention = {};
					$scope.attList.items[index].snsAttention.s_att_target_uid = attention.usr_ent_id;
				}
			});
		};
		function loaderList(){
			$scope.attList = new Loader(url, {searchContent : $scope.searchContent});
		}
	}]);
	personalModule.controller('collectController', ['$scope', 'Loader', 'Store', 'modalService', 'Ajax','knowledgeCenterService',
		function($scope, Loader, Store, modalService, Ajax,knowledgeCenterService){
		
		$scope.cur_lan_en = Store.get("globalLang");
		$scope.showType = app.getUrlParam("showType");//从知识中心点收藏“应该看到收藏知识那个Tab”
		
		var personalId = app.getUrlParam('person') || Store.get('person');
		var loginUserId = Store.get('loginUser');
		var collectCourseUrl = '/app/personal/getCollectList/Course/' + personalId;
		var collectOpenCourseUrl = '/app/personal/getCollectList/OpenCourse/' + personalId;
		var collectArticleUrl = '/app/personal/getCollectList/Article/' + personalId;
		var collectKnowledgeUrl = '/app/personal/getCollectList/Knowledge/' + personalId;
		var params = {
			isMobile : true
		}
		$scope.serverHost = serverHost;
		$scope.isMeInd = ( wbEncryptor.decrypt(personalId) == loginUserId );
		init();
		function init(){
			$scope.courseList = new Loader(collectCourseUrl, {},function(data){
				for(var i in data.items){
					var item = data.items[i];
					item.encItmId = wbEncryptor.encrypt(item.itm_id);
					item.cla = courseAndExamModule.getItemIcon( (item.itm_exam_ind == 1 ? 'exam' : 'course') , item.itm_type );
				}
			});
			$scope.openCourseList = new Loader(collectOpenCourseUrl, {},function(data){
				for(var i in data.items){
					var item = data.items[i];
					item.encItmId = wbEncryptor.encrypt(item.itm_id);
				}
			});
			
			$scope.articleList = new Loader(collectArticleUrl, params,function(data){
				for(var i in data.items){
					var item = data.items[i];
					item.enc_art_id = wbEncryptor.encrypt(item.art_id);
				}
			});
			
			$scope.knowledgeList = new Loader(collectKnowledgeUrl, params,function(result){
				for(var i in result.items){
					var item = result.items[i];
					item.enc_kbi_id = wbEncryptor.encrypt(item.kbi_id);
					item.info = knowledgeCenterService.getKnowledgeInfo(item.kbi_type,item.docType);
				}
			});
		}
		$scope.cancelCollect = function(module, id, $event){
			var url = '/app/personal/cancelCollect/' + id + '/' + module;
			modalService.modal(function($scope, $modalInstance) {
				$scope.modalText = 'collect_confirm_delete';
				$scope.modalOk = function() {
					Ajax.post(url, {}, function(){
						init();
						$modalInstance.dismiss('cancel');
					});
				};
				$scope.modalCancel = function() {
					$modalInstance.dismiss('cancel');
				};
			});
			$event && $event.stopPropagation();
		}
	}]);
	personalModule.controller('filesController', ['$scope', '$filter', 'Ajax', 'Store', 'alertService', 'wizUpload','UserService',
		function($scope, $filter, Ajax, Store, alertService, wizUpload,UserService){
		var personalId = app.getUrlParam('person') || Store.get('person');
		var url = '/app/personal/home/json/' + personalId;
		$scope.serverHost = serverHost;
		$scope.user = {};                  	 //用户信息
		$scope.isMeInd = false;              //是否是本人进入
		$scope.edit = {
			title : '',
			text : '',
			key :''
		};
		$scope.icon = '';
		$scope.showFiles = true;
		$scope.showEditMsg = false;
		$scope.showEditDate = false;
		$scope.date = null;  //用来储存编辑日期
		Ajax.post(url, {}, function(data){
			if(data){
				$scope.user = data.regUser;
				$scope.isMeInd = data.isMeInd;
				$scope.mySupervise = data.mySupervise;
			}
		});
		//显示选择页面
		$scope.showSelect = function(){
			if($scope.isMeInd){
				toggleSelect();
			}
		};
		//选择性别
		$scope.selectSex = function(val){
			$scope.user.usr_gender = val;
			//toggleSelect();
		};
		//跳转到编辑页面
		$scope.changeMsg = function(title,key){
			if($scope.isMeInd){
				$scope.edit.title = $filter('translate')(title);
				$scope.edit.key = key;
				if($scope.edit.key.indexOf('.') != -1){
					var keys = $scope.edit.key.split('.');
					if($scope.user[keys[0]] == undefined){
						$scope.user[keys[0]] = {};
					}
					if($scope.user[keys[0]][keys[1]] == undefined){
						$scope.user[keys[0]][keys[1]] = "";
					}
					$scope.edit.text = $scope.user[keys[0]][keys[1]];
				}else{
					$scope.edit.text = $scope.user[$scope.edit.key];
				}
				$scope.showFiles = false;
				$scope.showEditMsg = true;
				$scope.showEditDate = false;
			}
		};
		//bill.lai 
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth();
		var day = now.getDate();
		var monthStr = month + 1 > 9 ? month + 1 : '0' + (month + 1);
		var dayStr = day > 9 ? day : '0' + day;
		var yearStr = year;
		$scope.toDate = yearStr + '-' + monthStr + '-' + dayStr;
		$scope.changeDate = function(title,key){
			if($scope.isMeInd){
				$scope.edit.title = $filter('translate')(title);
				$scope.edit.key = key;
				if($scope.edit.key.indexOf('.') != -1){
					var keys = $scope.edit.key.split('.');
					$scope.date = $scope.user[keys[0]][keys[1]];
				}else{
					$scope.date = $scope.user[$scope.edit.key];
				}
				$scope.showFiles = false;
				$scope.showEditMsg = false;
				$scope.showEditDate = true;
				if($scope.date == null){
					$scope.date = $scope.toDate;
				}
			}

		}
		//显示档案页面
		$scope.showFileWindow = function(){
			$scope.showFiles = true;
			$scope.showEditMsg = false;
			$scope.showEditDate = false;
		};
		//保存文本编辑
		$scope.saveText = function(){
			if($scope.edit.key.indexOf('.') != -1){
				var keys = $scope.edit.key.split('.');
				$scope.user[keys[0]][keys[1]] = $scope.edit.text;
			}else{
				$scope.user[$scope.edit.key] = $scope.edit.text;
			}
			$scope.showFiles = true;
			$scope.showEditMsg = false;
			$scope.showEditDate = false;
		};
		//保存日期信息
		$scope.saveDate = function(){
			if($scope.edit.key.indexOf('.') != -1){
				var keys = $scope.edit.key.split('.');
				$scope.user[keys[0]][keys[1]] = $scope.date;
			}else{
				$scope.user[$scope.edit.key] = $scope.date;
			}
			$scope.showFiles = true;
			$scope.showEditMsg = false;
			$scope.showEditDate = false;
		};
		//上次头像
		$scope.UploadPhoto = function(){
			if($scope.isMeInd){
				if(appInd){
					showActionSheet();
				} else {
					document.getElementById('file').click();
				}
			}
		}
		function showActionSheet(){
			var bts=[{title : $filter('translate')('app_camera')}, {title : $filter('translate')('app_gallery')}];
			plus.nativeUI.actionSheet({cancel : $filter('translate')('btn_cancel'), buttons : bts},
				function(e){
					if(e.index == 1){
						cameraIamge();
					} else if(e.index == 2){
						galleryIamge();
					}
				}
			);
		}
		var imagePath = "";
		//照相
		function cameraIamge(){
			plus.camera.getCamera().captureImage(function(p){
				plus.io.resolveLocalFileSystemURL(p, function(entry){
					document.getElementById('image').src = entry.toLocalURL();
					imagePath = entry.toLocalURL();
				});
			});
		}
		//从相册中选择
		function galleryIamge(){
		    plus.gallery.pick(function(path){
		    	document.getElementById('image').src = path;
		    	imagePath = path;
		    });
		}
		$scope.updateFiles = function(){
			if($scope.user.usr_display_bil == ''){
				alertService.add('warning', 'personal_tip_name_notNull', 2000);
				return;
			}
			if( $scope.user.usr_email &&
				$scope.user.usr_email !='' &&
				!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test($scope.user.usr_email)){
				alertService.add('warning', 'personal_tip_email_error', 2000);
				return;
			}
			if( $scope.user.usr_tel_1 &&
				$scope.user.usr_tel_1 != '' &&
				 !/^((\d{11})|((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})))$/.test($scope.user.usr_tel_1)){
				alertService.add('warning', 'personal_tip_phone_error', 2000);
				return;
			}
			var url = serverHost + '/app/personal/personalDetail/update/userDetailMobile';
			
			if(!appInd){
				alertService.add('info', 'personal_tip_loading');
			}
			
			if(appInd){
				wa = plus.nativeUI.showWaiting();
				var task = plus.uploader.createUpload(url, {}, function(t, status){
					// 上传完成
					if(status == 200){
						data = eval("(" + eval("(" + t.responseText + ")") + ")");
						$scope.$apply(function(){
							alertService.closeAllAlerts();
							if(data && data.status == 'success'){
								var allws = plus.webview.all();
								for(var i=1;i<allws.length-1;i++){
									allws[i].evalJS("changePhoto('" + imagePath + "')");
								}
								alertService.add('success', 'personal_tip_success', 2000,function(){
									UserService.updateUserInfoCache(function(){
										window.location.reload();
									});
								});
							} else if(data && data.status == 'error'){
								alertService.add('danger', data.msg);
							}
						})
					} else {
						alertService.add('success', 'error_connect_fail', 2000);
					}
					wa.close();
				});
				if(imagePath != "" && imagePath != null){
					task.addFile(imagePath, {key:"file"});
				}
				task.addData("joinTime", $scope.user.usr_join_datetime ? $scope.user.usr_join_datetime : '');
				task.addData("birthday", $scope.user.usr_bday ? $scope.user.usr_bday : '');
				task.addData("json", JSON.stringify($scope.user));
				task.start();
			} else {
				var params = {
					joinTime : $scope.user.usr_join_datetime ? $scope.user.usr_join_datetime : '',
					birthday : $scope.user.usr_bday ? $scope.user.usr_bday : '',
					json : JSON.stringify($scope.user)
				};
				wizUpload({
						file : document.getElementById('file').files[0],
						url : url,
						data : params,
						success : function(data){
							$scope.$apply(function(){
								alertService.closeAllAlerts();
								if(data && data.status == 'success'){
									alertService.add('success', 'personal_tip_success', 2000,function(){
										UserService.updateUserInfoCache(function(){
											window.location.reload();
										});
									});
								} else if(data && data.status == 'error'){
									alertService.add('danger', data.msg);
								}
							})
						}
				});
			}
		};
	}]);
	personalModule.controller('learnStuController', ['$scope', 'Ajax', 'Store', function($scope, Ajax, Store){
		var personalId = app.getUrlParam('person') || Store.get('person');
		var homeUrl = '/app/personal/learningSituation/json/' + personalId;
		var params = {
			isMobile : true
		}
		$scope.serverHost = serverHost;
		$scope.user = {};
		$scope.learningSituation = {};
		$scope.isMeInd = false;
		$scope.show = {
			list : true,
			situation : false,
			course : false,
			exam : false,
			sns : false
		};
		Ajax.post(homeUrl, params, function(data){
			if(data){
				$scope.user = data.regUser;
				$scope.learningSituation = data.learningSituation;
				$scope.isMeInd = data.isMeInd;
			}
		});
		$scope.showWindow = function(name){
			for(var key in $scope.show){
				$scope.show[key] = false;
			}
			$scope.show[name] = true;
		};
	}]);
	personalModule.controller('groupController', ['$scope', 'Loader', 'Store', 'Ajax', function($scope, Loader, Store, Ajax){
		var personalId = app.getUrlParam('person') || Store.get('person');
		var url = '/app/personal/getPersonalGroupList/' + personalId;
		var homeUrl = '/app/personal/home/json/' + personalId;
		$scope.serverHost = serverHost;
		$scope.isMeInd = false;
		$scope.groupList = new Loader(url, {});
		Ajax.post(homeUrl, {}, function(data){
			if(data){
				$scope.isMeInd = data.isMeInd;
			}
		});
	}]);
	personalModule.controller('doingController', ['$scope', 'Loader', 'Ajax', 'Store', 'SNS', 'alertService', 'dialogService',function($scope, Loader, Ajax, Store, SNS, alertService,dialogService){
		alertObj = alertService;
		uploadUrl = serverHost + '/app/upload/mobile/Doing/';
		var personalId = app.getUrlParam('person') || Store.get('person');
		var url = '/app/doing/user/json/' + personalId + '/Doing/' + personalId;
		var params = {
			isMobile : true
		};
		$scope.fileValue = '';
		$scope.showWrite = false;
		$scope.doingText = '';
		$scope.uploadType = 'ALL';
		$scope.serverHost = serverHost;
		$scope.personalId = personalId;
		$scope.loginUserId = Store.get('loginUser');
		$scope.doingList = {};
		LoaderList();

		function LoaderList(){
			$scope.doingList = new Loader(url, params, formatDoing);
		};

		function formatDoing(data){
			for(var i in data.items){
				data.items[i].doingLike = {
					type : 'like',
					count : data.items[i].snsCount ? data.items[i].snsCount.s_cnt_like_count : '0',
					flag : data.items[i].is_user_like,
					id :  data.items[i].s_doi_id,
					module : 'Doing',
					tkhId : 0
				};
				data.items[i].replyShow=false;
				for(var j in data.items[i].replies){
					data.items[i].replies[j].commentLike = {
						type : 'like',
						count : data.items[i].replies[j].snsCount ? data.items[i].replies[j].snsCount.s_cnt_like_count:0,
						flag : data.items[i].replies[j].is_user_like,
						id : data.items[i].replies[j].s_cmt_id,
						module: 'Doing',
						isComment : 1,
						tkhId : 0
					};
				}
			}
		}

		$scope.toggleWrite = function(){
			$scope.showWrite = $scope.showWrite ? false : true;
		};

		$scope.checkType = function(type){
            $scope.uploadType = type;
        }

		$scope.publishDoing = function(){
			if($scope.doingText == ''){
				alertService.add('warning', 'personal_tip_doing_notNull', 2000);//'评论不能为空！'
				return;
			}
			if($scope.doingText.length > 200){
				alertService.add('warning', 'personal_tip_doing_length', 2000);//'不能超过100个字符'
				return;
			}
			SNS.addDoing($scope.doingText, 'doing', 'Doing', $scope.loginUserId, function(data){
				if(data.status == 'success'){
					dialogService.modal("doing_message_post","o",function(){
						$scope.toggleWrite();
						$scope.doingText = '';
						deleteAllImg();
						LoaderList();
					});
				}
			});
		}
	}]);
	function toggleSelect(){
		$(".yinsi-desc").width($("#wizwrap").width()).toggleClass("show");
//		$(".content-2").slideToggle();
    	$(".yinsi-info").width($("#wizwrap").width()).slideToggle();
    	$(".yinsi-desc").height($(window).height());
	}
});
function showPhoto (val){
	var reader = new FileReader();
	reader.onload = function (e){
		document.getElementById('image').src = e.target.result;
	};
	reader.readAsDataURL(document.getElementById('file').files[0]);
}

function refreshAttentionList(){
	$("#searchBtn").click();
}
