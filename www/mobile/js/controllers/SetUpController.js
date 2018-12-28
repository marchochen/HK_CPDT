$.include([
    'js/services/UserService.js'
], '../../');
$(function(){
	var setUpModule = angular.module('setUp', ['globalCwn', 'userService']);
	setUpModule.filter('privacyText', ['$filter', function($filter){
		return function(input){
		    input=input ? input : 0;			
			if(input == 0){
				input = $filter('translate')('set_privacy_all_see');
			}
			if(input == 1){
				input = $filter('translate')('set_privacy_my_attention_see');
			}
			if(input == 2){
				input = $filter('translate')('set_privacy_all_not_see');
			}							
			return input;
		}
	}]);	
	setUpModule.directive('textdel', function(){
		return {
			restrict: 'EA',
        	template: 
        		'<div class="pure-form-box-4">'+
        		 	'<input type="password" class="pure-form-txt-1" ng-model="model" placeholder="{{tip | translate}}">'+
             		'<a class="pure-form-delete" ng-show="showDel" ng-click="delText()" href="javascript:void(0)"></a>'+
         		'</div>',
        	replace: true,
        	scope:{
        		model : '=',
        		tip : '@',
        	},
        	link: function(scope, element, attrs) {
        		scope.showDel = true;
				scope.$watch('model', function(){
	    			if(scope.model == ''){
	    				scope.showDel = false;
	    			}else{
	    				scope.showDel = true;
	    			}
	    		});
	    		scope.delText = function(){
	    			scope.model = '';
	    		}
        	}
		};
	});
	setUpModule.controller('mainController', ['$scope', '$window', '$translate', 'User', 'Ajax', 'Store', 'modalService', function($scope, $window, $translate, User, Ajax, Store, modalService){
		$scope.user = {};
		$scope.serverHost = serverHost;
		User.userInfo(function(data){
			$scope.user = data.regUser;			
		});
		
		var exeLogout = function(callback){
			var url= '/app/login/out';
			Ajax.post(url, {}, function(){
				
				/**
				 * 除了globalLang，其他都清除
				 */
				var storage = Store.getStorage();
				var globalLang = storage.getItem("globalLang");
				storage.clear();
				storage.setItem("globalLang",globalLang);
				
				window.sessionStorage.clear();
				
				callback && callback();
				
				backLogin("../../");
			});
		};
		
		$scope.loginOut = function(){
			
			modalService.modal(function($scope, $modalInstance) {
				$scope.modalText = 'set_login_out_tip';
				$scope.modalOk = function() {
					if(appInd){
						callNative(function(){
							plus.nativeUI.showWaiting();
							plus.storage.removeItem("token");
							//删除APPClient记录
							(function(){
								var mobileInd = plus.os.name.toUpperCase();
								var clientInfo = plus.push.getClientInfo();
								var clientId;
								if("IOS" == mobileInd){
									clientId = clientInfo.token;
								}else{//android
									clientId = clientInfo.clientid;
								}
								var appId = clientInfo.appid;
								var appClientParam = {
									mobileInd : mobileInd,
									clientId : clientId,
									appId : appId,
									status : 'online'
								}
								
								Ajax.post("/app/appClient/delete?token="+Store.get("token"),appClientParam,function(){
									exeLogout(function(){
										plus.nativeUI.closeWaiting();
									});
								});
								
							})();
						});
					}else{
						exeLogout();
					}
					
				};
				$scope.modalCancel = function() {
					$modalInstance.dismiss('cancel');
				};
			});
		}
		
		$scope.gotoAbout = function() {
			var logoImage = '';
			var flag = false;
			var token = null;
			if(window.localStorage){
		     	token = window.localStorage.getItem("token") || window.sessionStorage.getItem("token");
			}
			var url = serverHost+'/app/user/sitePoster';
			Ajax.post(url, {token : token, developer : 'mobile'}, function(data) {
				var sitePoster = data.sitePoster;
				if(sitePoster && sitePoster.login_bg_file1 && sitePoster.login_bg_file1 != '' ){
	        		logoImage = serverHost + '/poster/' + data.site + '/' + sitePoster.sp_logo_file_hk;
	        	}
				if(logoImage != '') {
    				flag = true;
    			}
        		Store.set('logoImage', logoImage);
    			Store.set('flag', flag);
    			clicked('about.html', true, 'home');
			});
		}
	}]);
	
	setUpModule.controller('aboutController', ['$scope', '$window', '$filter','User', 'Ajax', 'alertService', 'Store',function($scope, $window, $filter, User, Ajax, alertService,Store){
		$scope.flag = Store.get('flag');
		$scope.logoImage = Store.get('logoImage');
	}]);
	
	setUpModule.controller('passwordController', ['$scope', '$window', '$filter','User', 'Ajax', 'alertService', 'Store',function($scope, $window, $filter, User, Ajax, alertService,Store){
		
		var fromIndex = app.getUrlParam("fromIndex");
		$scope.fromIndex = fromIndex;
		
		$scope.loaded = false;//loaded为true之后，才做些显示操作，防止页面元素闪动
		$scope.serverHost = serverHost;
		
		Ajax.post("/app/personal/passwordPolicyInfo",{},function(data){
			$scope.loaded = true;
			$scope.passwordPolicyInfo = data;
			if($scope.passwordPolicyInfo.forceChange){//如果要强制修改密码，则禁掉返回键
				if(appInd){
					changeBackButtion(function(){
					});
				}
			}
		});
		
		$scope.pwdOld = '';
		$scope.pwdNew = '';
		$scope.pwdConfirm = '';
		var status = false;
		var loginUser;
		User.userInfo(function(data){
			status = true;
			loginUser = data == undefined?null:data.regUser?data.regUser:null;
			$scope.user = loginUser;
		});		
		//修改密码方法
		$scope.setPwd = function(){
			if(status){
				//验证密码输入格式	
				if($scope.pwdOld == ''){
					alertService.add('warning', 'set_tip_pwd_old_notNull', 2000);
					return;
				}		
				if($scope.pwdNew == ''){
					alertService.add('warning', 'set_tip_pwd_new_notNull', 2000);
					return;
				}
				if($scope.pwdConfirm == ''){
					alertService.add('warning', 'set_tip_pwd_confirm_notNull', 2000);
					return;
				}
				
				if($scope.pwdNew.search(/[^a-zA-Z0-9_-]/) != -1){
					alertService.add('warning', 'usr_password_type', 2000);
					return;
				}
				if(!/^(?=.*[A-Za-z]+)(?=.*[0-9]+)[A-za-z0-9_-]*$/.test($scope.pwdNew)){
					alertService.add('warning', 'usr_password_check', 2000);
					return;
				}
				
				var min = $scope.passwordPolicyInfo.minLength;
				var max = $scope.passwordPolicyInfo.maxLength;
				
				if($scope.pwdOld.length < min || $scope.pwdOld.length > max || $scope.pwdNew.length < min || $scope.pwdNew.length > max || $scope.pwdConfirm.length < min || $scope.pwdConfirm.length > max ){
					alertService.add("warning",$filter('translate')('set_tip_pwd_length',{min:min,max:max}),2000);
					return;
				}
				//验证密码是否一致
				if($scope.pwdNew != $scope.pwdConfirm){						
					alertService.add('warning', 'set_tip_pwd_not_same', 2000);
					return;
				}
				//提交请求
				var url = "/app/personal/updatePassword";
				var params = {
					old_password : $scope.pwdOld,
					new_password : $scope.pwdNew
				}
				status = false;
				alertService.add('info', 'set_tip_pwd_update_loading');
				Ajax.post(url,params,function(data){
					var result = data.result;
					var message = data.message;//提示信息
					alertService.closeAllAlerts();
					alertService.add('success', message, 1000);//提示信息
					
					switch(result){
					case 'update_ok' ://更新成功
						if(appInd){//如果是app，3000之后再返回，修复提示时间不一致
							window.setTimeout(function(){
								back();
							},3000);
						}else{
							back();
						}
						break;
					case 'usr_password_has_existed' ://新密码和之前历史记录一样
						$scope.pwdNew = '';
						$scope.pwdConfirm = '';
						break;
					case 'usr_old_password_error' ://密码错误
						$scope.pwdOld = '';
						break;
					case 'old_and_new_similar' ://旧密码和新密码一样
						$scope.pwdNew = '';
						$scope.pwdConfirm = '';
						break;
					}
					
					status = true;				
				});				
			}			
								
		}
		
		$scope.back = function(){
			Store.set("ignoreChangePwd","true");
			back();
		}
		
	}]);
	setUpModule.controller('languageController', ['$scope','$window', '$translate', '$filter','User', 'Ajax', 'Store', 'alertService', function($scope, $window, $translate, $filter,User, Ajax, Store, alertService){			
		$scope.lanSelect = Store.get('globalLang');
	
		$scope.select = function(key){
			$scope.lanSelect = key;
		}

		$scope.changeLan = function(){
			if(appInd){
				var wa = plus.nativeUI.showWaiting();
			}
			alertService.add('info', 'set_tip_lan_change_loading');
			var url = '/app/user/changeLang/' + $scope.lanSelect;
			Ajax.post(url, {}, function(){
				Store.set('globalLang', $scope.lanSelect);
				if(appInd){
					plus.storage.setItem("globalLang",$scope.lanSelect);
					changeWebviewDetail('home',function(){
						alertService.closeAllAlerts();
						alertService.add('success', 'set_tip_lan_change_success', 1000);
						setTimeout(function(){
							plus.webview.getWebviewById("offline") && plus.webview.getWebviewById("offline").evalJS("changeLan('"+$scope.lanSelect+"')");
							wa.close();
							if(plus.webview.getWebviewById('setMain') != undefined){
								plus.webview.getWebviewById('setMain').close('auto','slide-in-right');
							}
							back();
						},1000);
					});
					clicked('../index.html',true,'home');
				} else {										
					clicked('../index.html',true,'home');	
				}
			});	
		}
	}]);
	setUpModule.controller('privacyController', ['$scope', '$window', '$filter', 'Ajax', 'Store', 'alertService', function($scope, $window, $filter, Ajax, Store, alertService){
		$scope.snsSetting = '';	
		var snsSettingUrl = '/app/personal/personalPrivacySet/getSnsSetting/' + Store.get('loginUser');
		var curPrivacy = '';
		$scope.curVal = 0;
		$scope.curTitle = '';
		Ajax.post(snsSettingUrl, {}, function(data){
			if(data && data.snsSetting)			
				$scope.snsSetting = data.snsSetting;
			else
				initSetting();
		});
		$scope.showSelect = function(name, title){
		   	if(name && title){
		   		curPrivacy = name;
		   		$scope.curTitle = title;
		   		$scope.curVal = $scope.snsSetting[curPrivacy];
		   	}
		   toggleSelect();
		};
		$scope.select = function(val){
			$scope.snsSetting[curPrivacy] = val;			
			//toggleSelect();
		}; 		
		function initSetting(){
			$scope.snsSetting ={
				s_set_group : 0,
				s_set_my_follow : 0,
				s_set_my_fans : 0,
				s_set_like : 0,
				s_set_doing : 0,
				s_set_my_files : 0,
				s_set_my_collection : 0
			}
		}
		$scope.changePrivacy = function(){
			var changeUrl = '/app/personal/personalPrivacySet/changePrivacySetMobile';
			var params = {
				s_set_group : $scope.snsSetting.s_set_group,
				s_set_my_follow : $scope.snsSetting.s_set_my_follow,
				s_set_my_fans : $scope.snsSetting.s_set_my_fans,
				s_set_like : $scope.snsSetting.s_set_like,
				s_set_doing : $scope.snsSetting.s_set_doing,
				s_set_my_files : $scope.snsSetting.s_set_my_files,
				s_set_my_collection : $scope.snsSetting.s_set_my_collection
			};			
			alertService.add('info', 'set_tip_privacy_loading');
		    Ajax.post(changeUrl, params, function(data){
		    	alertService.closeAllAlerts();
		    	if(data && data.message && data.message == 'update_ok'){		    		
		    		alertService.add('success', 'set_tip_privacy_success', 1000);
		    		//$window.location.href = "setMain.html";	
		    		back();
		    	}		    		    
		    });
		}
	}]);	
	function toggleSelect(){
		$(".yinsi-desc").width($("#wizwrap").width()).toggleClass("show");
		$(".content").slideToggle();
	    $(".yinsi-info").width($("#wizwrap").width()).slideToggle();
		$(".yinsi-desc").height($(window).height());
	}
});