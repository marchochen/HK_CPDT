$.include([
], '../');

var filter;

$(function(){
	
	var loginModule = angular.module('cwn', ['globalCwn']);
	loginModule.controller('LoginController', ['$scope', '$http', '$window', '$filter', 'Store', 'alertService','Ajax',
	    function($scope, $http, $window, $filter, Store, alertService,Ajax) {
			
			// 如果有弹窗记录弹窗的类型，在选择语言时候可以切换
			var alterError = "";
		
			if(Store.get("token") ){//非app记住我，如果有token，直接跳转到首页
				window.location.href = "index.html";
			}
			
			if(Store.get("globalLang") == "zh-cn") {
				$("#jian").css("color", "#00aeef");
			} else if(Store.get("globalLang") == "zh-hk") {
				$("#fan").css("color", "#00aeef");
			} else {
				$("#ying").css("color", "#00aeef");
			}
			
			$scope.login_pls_host = labels[Store.get("globalLang")]["login_pls_host"];
			$scope.login_pls_name = labels[Store.get("globalLang")]["login_pls_name"];
			$scope.login_pls_password = labels[Store.get("globalLang")]["login_pls_password"];
			$scope.btn_login = labels[Store.get("globalLang")]["btn_login"];
			$scope.login_remember_me = labels[Store.get("globalLang")]["login_remember_me"];
			$scope.login_forgot_password = labels[Store.get("globalLang")]["login_forgot_password"];
			
			var logoImage = '';
			var flag = false;
			var token = null;
			if(window.localStorage){
		     	token = window.localStorage.getItem("token") || window.sessionStorage.getItem("token");
			}
			var ImgUrl = serverHost+'/app/user/sitePoster';
			Ajax.post(ImgUrl, {token : token, developer : 'mobile'}, function(data) {
				var sitePoster = data.sitePoster;
				if(sitePoster && sitePoster.login_bg_file1 && sitePoster.login_bg_file1 != '' ){
					logoImage = serverHost + '/poster/' + data.site + '/' + sitePoster.sp_logo_file_cn;
				}
				if(logoImage != '') {
					flag = true;
				}
				$scope.flag = flag;
				$scope.logoImage = logoImage;
			});
			
			filter = $filter;
			$scope.appInd = appInd;
			$scope.sitePoster = {};
			$scope.site = '';
			$scope.username = '';
			$scope.password = '';
			if(appInd){
				var interval = self.setInterval(function(){
					if(window.plus){
						plus.webview.currentWebview().reload();
					}
				}, 10000);
				
				if(window.plus){
					getStorage();
				}else{
					document.addEventListener('plusready',getStorage,false);
				}
				function getStorage(){
					var serverHost = plus.storage.getItem("serverHost") == null || plus.storage.getItem("serverHost") == "" ? "" : plus.storage.getItem("serverHost");
					//截取字符串，赋值给全局变量
					if(serverHost.indexOf("http://") != -1 && serverHost.indexOf("https://") != -1){
						//截取url
						serverHost = serverHost.substring(serverHost.indexOf("//")+2);
						//截取协议头
						protocal = serverHost.substring(0,serverHost.indexOf("//")+2);
					}
					
					if(serverHost){
						window.serverHost = protocal + serverHost;
					}
					if(!$scope.$$phase){
						$scope.$apply(function(){
							$scope.serverHost = serverHost;
							$scope.username = plus.storage.getItem("username");
							$scope.password = plus.storage.getItem("password");
						})
						plus.navigator.closeSplashscreen();
						window.clearInterval(interval);
					} else {
						$scope.serverHost = serverHost;
						$scope.username = plus.storage.getItem("username");
						$scope.password = plus.storage.getItem("password");
					}
					$(function(){
						$(".list-pic-5").css("paddingTop", plus.display.resolutionHeight);
						$(".list-pic-5-bg").css("height", plus.display.resolutionHeight);
						plus.navigator.closeSplashscreen();
						window.clearInterval(interval);
						changeBackButtion(function(){
							exitProgram(alertService);
						});
					});
					
				}
				
			}
		
		if(appInd){
			$scope.checked = true;
		}else{
			$scope.checked = false;
		}
		
		$scope.checkRemember = function(){
			$scope.checked = !$scope.checked;
		}

		$scope.clearInput = function(type){
			if("serverHost" === type){
				$scope.serverHost = "";
			}else if("username" === type){
				$scope.username = "";
			}else if("password" === type){
				$scope.password = "";
			}
		}
		
		//发送手机推送信息到服务器，为了和登录业务解耦，单独发异步请求
		var sendClientRequest = function(){
			
			if(!appInd){
				return;
			}
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
				appId : appId
			}
			Ajax.post("/app/appClient",appClientParam);
		};
		
		$scope.isLanding = false;
		$scope.login = function(){
			isLogin = true;
			if(!appInd){
				if($scope.isLanding){
					return;
				}
				$scope.isLanding = true;
			}
			if(appInd){
				if ($scope.frmLogin.serverHost.$invalid){
					alertService.closeAllAlerts();
					alterError = "server_host_not_null";
			    	alertService.add('danger', labels[Store.get("globalLang")]["server_host_not_null"]);
			    	return;
			   	}
				serverHost = protocal + $scope.serverHost;
//				var req = /((([1-9]?|1\d)\d|2([0-4]\d|5[0-5]))\.){3}(([1-9]?|1\d)\d|2([0-4]\d|5[0-5]))/;
//				if(serverHost.indexOf('.cyberwisdom.net.cn') < 0 && !req.test($scope.serverHost.substring(0,$scope.serverHost.indexOf(":")-1))){
//					serverHost += '.cyberwisdom.net.cn';
//				}
			}
		    if ($scope.frmLogin.username.$invalid){
		    	alertService.closeAllAlerts();
		    	alterError = "user_name_not_null";
		    	alertService.add('danger', labels[Store.get("globalLang")]["user_name_not_null"]);
		    	$scope.isLanding = false;
		    	return;
		    }
	        if ($scope.frmLogin.password.$invalid){
	        	alertService.closeAllAlerts();
	        	alterError = "password_not_null";
		    	alertService.add('danger', labels[Store.get("globalLang")]["password_not_null"]);
		    	$scope.isLanding = false;
		    	return;
	        }
	        
	        //如果没有网络且现在输入的帐号密码和上次保存的一样就进入离线学习
	        if(appInd){
		        if (plus.networkinfo.getCurrentType() == 1 && $scope.username == plus.storage.getItem("username") && $scope.password == plus.storage.getItem("password")){
		        	waiting=plus.nativeUI.showWaiting();
		        	plus.storage.setItem("loginUserId", plus.storage.getItem("saveUserId"));
		        	webview = plus.webview.create("module/offline.html","offline",{scrollIndicator:'none',scalable:false});
		        	webview.addEventListener('loaded',function(){//页面加载完成后才显示
						webview.show('slide-in-right',200);
						waiting.close();
					},false);
					return;
		        }
	        }
	        
/*	        if ($scope.alerts.length){
	            return;
	        }*/
	        var url = '/app/login?username=' + $scope.username + "&password=" + $scope.password + "&loginLan=" + Store.get("globalLang");
	        //记住密码
	        if($scope.checked) {
	        	url += "&isRemember=1"
	        }
	        if(appInd){
	        	wa = plus.nativeUI.showWaiting();
	        }
			$http({
				method : 'jsonp',
				url : url
			}).success(function(data) {
				$scope.isLanding = false;
				if(appInd){
					wa.close();
				}
				// 当相应准备就绪时调用
				if(data.status && data.status == 'LGS01') {
					
					//发送手机推送信息到服务器，为了和登录业务解耦，单独发异步请求
					sendClientRequest();
					
					if($scope.checked || appInd) {
						Store.getStorage().clear();
						Store.set("token", data.token);
						Store.set("loginUser",data.loginUser.usr_ent_id);
					} else {
						var sessionStorage = window.sessionStorage;
						Store.getStorage().clear();
						sessionStorage.setItem("token", data.token);
						sessionStorage.setItem("loginUser",data.loginUser.usr_ent_id);
					}
					Store.set("globalLang",data.loginUser.cur_lan);		
					if(appInd){
						plus.storage.setItem("serverHost", $scope.serverHost);
						plus.storage.setItem("loginUserId", '' + data.loginUser.usr_ent_id);
						plus.storage.setItem("globalLang", '' + data.loginUser.cur_lan);
						plus.storage.setItem("token", data.token);
						
						if($scope.checked){
							plus.storage.setItem("saveUserId", '' + data.loginUser.usr_ent_id);
							plus.storage.setItem("username", $scope.username);
							plus.storage.setItem("password", $scope.password);
						}
						Store.set("serverHost", serverHost);
					}
					Store.set("isFristLogin", false);
					var loginMsgType = data.loginUser.show_alert_msg_type;
					if(loginMsgType != undefined && (loginMsgType.indexOf("first_day_login") != -1
							|| loginMsgType.indexOf("first_login") != -1)){
						Store.set("isFristLogin", true);
						Store.set("loginCredit", parseInt(data.loginUser.loginCredit)+'');
					}
					
					//每月第一次登录，且非首次登录,进入学习足迹页面
					/*if(loginMsgType && loginMsgType.indexOf("first_year_login") != -1 && loginMsgType.indexOf("first_login") === -1 ){
						Store.set("showLearningHistory",true);
					}else{
						Store.set("showLearningHistory",false);
					}*/
					
					clicked('index.html',false,'home');
				} else if(data.status=='LGS05') {
					if(appInd) {
						Store.set('serverHost', serverHost);
					}
					Store.set("usr_ste_usr_id",data.loginUser.usr_ste_usr_id);
					Store.set("usr_dis_bil", data.loginUser.usr_display_bil);
					Store.set("usr_photo", data.loginUser.usr_photo);
					Store.set("usr_policy", data.loginUser.usr_policy);
					Store.set("cur_lan", data.loginUser.cur_lan);
					clicked('resetPassword.html',true,'resetPassword');
				} else {
					if(data.status=='LGF00'){
						//CODE_UNKOWN_ERROR
						alertService.closeAllAlerts();
						alterError = "ERROR_UNKNOW";
				    	alertService.add('danger', labels[Store.get("globalLang")]["ERROR_UNKNOW"]);
					} else if(data.status=='LGF01') {
						alertService.closeAllAlerts();
						alterError = "ERROR_USER_NOT_EXISTS";
				    	alertService.add('danger', labels[Store.get("globalLang")]["ERROR_USER_NOT_EXISTS"]);
					} else if(data.status=='LGF04') {
						alertService.closeAllAlerts();
						alterError = "ERROR_PWD_ERROR";
				    	alertService.add('danger', labels[Store.get("globalLang")]["ERROR_PWD_ERROR"]);
					} else if(data.status=='LGF05') {
						alertService.closeAllAlerts();
						alterError = "ERROR_USER_LOCKED";
				    	alertService.add('danger', labels[Store.get("globalLang")]["ERROR_USER_LOCKED"]);
					} else if(data.status=='LGF09') {
						alertService.closeAllAlerts();
						alterError = "ERROR_USER_EXPIRE";
				    	alertService.add('danger', labels[Store.get("globalLang")]["ERROR_USER_EXPIRE"]);
					} else if(data.status == "LGF08"){
						alertService.closeAllAlerts();
						alterError = "ERROR_SYS_BUSY";
						alertService.add('danger', labels[Store.get("globalLang")]["ERROR_SYS_BUSY"]);
					} else if(data.status == "LGF012"){
						alertService.closeAllAlerts();
						alterError = "ERROR_IS_NOT_LEARNER_ACCOUNT";
						alertService.add('danger', labels[Store.get("globalLang")]["ERROR_IS_NOT_LEARNER_ACCOUNT"]);
					} else if(data.status == 'LGF10'){
						alertService.closeAllAlerts();
						alterError = "ERROR_USER_NOT_ROLE";
						alertService.add('danger',"ERROR_USER_NOT_ROLE");
					} else if(data.status == 'LGF13'){
						alertService.closeAllAlerts();
						alterError = "ERROR_USER_SYSTEM_ISSUE";
						alertService.add('danger', labels[Store.get("globalLang")]["ERROR_USER_SYSTEM_ISSUE"]);
					}else if(data.status == "error"){
						alertService.closeAllAlerts();
						alertService.add('danger', "登录失败 : " + data.msg);
					} else {
						alertService.closeAllAlerts();
						alertService.add('danger', "登录失败：code : " + data.status);
					}
				}
					
			}).error(function(data, status, headers, config) {
					// 当响应以错误状态返回时调用
			});
		};
		
		function getURL(url, appInd) {
			var req_url = '';
			if(appInd) {
				if($scope.serverHost.indexOf(protocal) != -1) {
					req_url = $scope.serverHost + url;
				} else {
					req_url = protocal + $scope.serverHost + url;
				}
			} else {
				req_url = url;
			}
			return req_url;
		}
		
		$scope.select = function(curLang) {
			if(curLang == 'zh-cn') {
				$("#jian").css("color", "#00aeef");
				$("#fan").css("color", "#999");
				$("#ying").css("color", "#999");
			} else if(curLang == 'zh-hk') {
				$("#jian").css("color", "#999");
				$("#fan").css("color", "#00aeef");
				$("#ying").css("color", "#999");
			} else {
				$("#jian").css("color", "#999");
				$("#fan").css("color", "#999");
				$("#ying").css("color", "#00aeef");
			}
			Store.set("globalLang",curLang);
			$("#username").attr("placeholder", labels[curLang]['login_pls_name']);
			$("#password").attr("placeholder", labels[curLang]['login_pls_password']);
			$("#login").html(labels[curLang]['btn_login']);
			$("#remember").html(labels[curLang]['login_remember_me']);
			$("#forgetPwd").html(labels[curLang]['login_forgot_password']);
			// 弹窗语言更改
			if(alterError != null && alterError.length > 0) {
				alertService.closeAllAlerts();
				alertService.add('danger', labels[Store.get("globalLang")][alterError]);
			}
			
		}
		
		$scope.forgetPwd = function() {
			if(appInd) {
				Store.set('serverHost', $scope.serverHost);
			}
			clicked('forgetPassword.html',true,'forgetPassword');
		}
}]);
	
loginModule.controller('ChangePwdController', ['$scope', '$window', '$filter','Ajax', 'alertService', 'Store',
	 	                                         function($scope, $window, $filter, Ajax, alertService,Store){
	$scope.appInd = appInd;
	var serverHost = Store.get("serverHost");
	
	$scope.serverHost = serverHost;
	
	$scope.user = {
		'disBil' : Store.get("usr_dis_bil"),
		'photo' : Store.get("usr_photo"),
		'policy' : Store.get("usr_policy")
	};
	$scope.setPwd = function() {
		alertService.closeAlert();
		
		var oldPwd = $scope.usr_pwd_old;
		var newPwd = $scope.usr_pwd_new;
		var confirmPwd = $scope.usr_pwd_confirm;
		
		if(oldPwd == '' || oldPwd == undefined) {
			alertService.closeAllAlerts();
			alertService.add('danger', 'reset_pwd_old_tip');
			return;
		}
		if(newPwd == '' || newPwd == undefined) {
			alertService.add('danger', 'reset_pwd_new_tip');
			return;
		}
		if(newPwd.length < 3) {
			alertService.add('danger', 'reset_pwd_length');
			return;
		}
		if(!newPwd.match(/^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[A-Za-z0-9\-\_]*$/)) {
			alertService.add('danger', 'reset_pwd_valid');
			return;
		}
		if(confirmPwd == '' || confirmPwd == undefined) {
			alertService.add('danger', 'reset_pwd_confirm_tip');
			return;
		}
		if(confirmPwd != newPwd) {
			alertService.add('danger', 'reset_pwd_new_confirm');
			return;
		}
		
		var url = "";
		if(appInd) {
			if(serverHost.indexOf(protocal) != -1) {
				url = serverHost + '/app/login/changePwd/' + Store.get('globalLang');
			} else {
				url = protocal + serverHost + '/app/login/changePwd/' + Store.get('globalLang');
			}
		} else {
			url = '/app/login/changePwd/' + Store.get('globalLang');
		}
		var params = {
				usr_ste_usr_id : Store.get("usr_ste_usr_id"),
				usr_pwd_old :oldPwd,
				usr_pwd_new : newPwd
		}
		Ajax.post(url, params, function(data) {
			var result = data.result;
			switch(result) {
				case 'update_ok' :
					//sendClientRequest();
					if($scope.checked || appInd) {
						Store.getStorage().clear();
					} else {
						var sessionStorage = window.sessionStorage;
						Store.getStorage().clear();
						sessionStorage.setItem("token", data.token.atk_id);
						sessionStorage.setItem("loginUser",data.loginUser.usr_ste_usr_id);
					}
					Store.set("token", data.token.atk_id);
					Store.set("globalLang",Store.get("cur_lan"));		
					if(appInd){
						plus.storage.setItem("serverHost", $scope.serverHost);
						plus.storage.setItem("loginUserId", '' + data.loginUser.usr_ent_id);
						plus.storage.setItem("globalLang", '' + data.loginUser.cur_lan);
						plus.storage.setItem("token", data.token.atk_id);
						
						if($scope.checked){
							plus.storage.setItem("saveUserId", '' + data.loginUser.usr_ent_id);
							plus.storage.setItem("username", data.loginUser.usr_ste_usr_id);
							plus.storage.setItem("password", $scope.usr_pwd_new);
						}
						Store.set("serverHost", serverHost);
					}
					Store.set("isFristLogin", false);
					var loginMsgType = data.loginUser.show_alert_msg_type;
					if(loginMsgType != undefined && (loginMsgType.indexOf("first_day_login") != -1
							|| loginMsgType.indexOf("first_login") != -1)){
						Store.set("isFristLogin", true);
						Store.set("loginCredit", parseInt(data.loginUser.loginCredit)+'');
					}
					
					alertService.add('success', "set_tip_pwd_update_success");
					setTimeout(function(){
						clicked('index.html',true,'home');
					},3000);
					$scope.$apply();
					break;
				case 'old_and_new_similar' :
					alertService.add('danger', 'reset_pwd_new_old_same');
					$scope.$apply();
					break;
				case 'usr_password_has_existed' :
					alertService.add('danger', 'reset_pwd_repeat');
					$scope.$apply();
					break;
				case 'pwd_error' :
					alertService.add('danger', 'set_tip_pwd_error');
					$scope.$apply();
					break;
			}
		});
		
	}
	
}]);

loginModule.controller('RetrievePwdController', ['$scope', '$window', '$filter','Ajax', 'alertService', 'Store',
	 	                                         function($scope, $window, $filter, Ajax, alertService,Store){
		$scope.appInd = appInd;
		var serverHost = Store.get("serverHost");
		if(appInd) {
			$scope.serverHost = serverHost;
		}
	
		$scope.submit = function() {
			alertService.closeAlert();
			
			if(appInd) {
				if ($scope.serverHost == '' || $scope.serverHost == undefined) {
					alertService.add('danger', 'server_host_not_null');
					return;
				}
			}
			
			if ($scope.usr_id == '' || $scope.usr_id == undefined) {
				alertService.add('danger', 'user_name_not_null');
				return;
			}
			if ($scope.usr_email == '' || $scope.usr_email == undefined) {
				alertService.add('danger', 'usr_email_not_null');
				return;
			}
			var url = "";
			if(appInd) {
				if(serverHost.indexOf(protocal) != -1) {
					url = $scope.serverHost + '/app/login/forgetPassword';
				} else {
					url = protocal + $scope.serverHost + '/app/login/forgetPassword';
				}
			} else {
				url = '/app/login/forgetPassword';
			}
			var params = {
				usr_ste_usr_id : $scope.usr_id,
				usr_email : $scope.usr_email
			}
			Ajax.post(url, params, function(data) {
				if(data.type == 'find') {
					alertService.add('success', "send_reset_mail_msg");
					setTimeout(function(){
						clicked('login.html',true,'login');
					},3000);
					$scope.$apply();
				} else {
					alertService.add('danger', data.message);
					$scope.$apply();
				}
			});
		}
		
	}]);

});