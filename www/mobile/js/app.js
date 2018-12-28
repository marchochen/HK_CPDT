//全局module
var appModule = angular.module('globalCwn', ['pascalprecht.translate', 'ui.bootstrap']);
//定义默认的语言
appModule.constant('$globalLang', 'en-us');
appModule.constant('$pageSize', 10);
//判断是否点击登录
var isLogin = false;
//label
appModule.config(function($translateProvider, $globalLang, $compileProvider){
	var storage = window.localStorage;
	var lang = storage.getItem("globalLang");
	if(!lang) {
		lang = $globalLang;
		storage.setItem("globalLang",lang);
	}
	$translateProvider.preferredLanguage(lang);
//	$translateProvider.useLoader('asyncLoader');
	var translations;
	if (lang === 'en-us') {
    	translations = cwn_en_us;
    } else if(lang === 'zh-hk'){
    	translations = cwn_zh_hk;
    }else{
    	translations = cwn_zh_cn;           
    }
	$translateProvider.translations(lang, translations);

	$compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|javascript):/);
});

//自定义js异步加载器
appModule.factory('asyncLoader', function ($q, $timeout) {
    return function (options) {
    	var deferred = $q.defer();
    	
        var scriptEle = document.createElement("script");
        scriptEle.src = getRelatievePathPrefix()+"js/i18n/"+options.key+".js";
        document.head.appendChild(scriptEle);
        scriptEle.addEventListener("load",function(){
        	var translations;
        	if (options.key === 'en-us') {
            	translations = cwn_en_us;
            } else if(options.key === 'zh-hk'){
            	translations = cwn_zh_hk;
            }else{
            	translations = cwn_zh_cn;           
            }
            deferred.resolve(translations);
        });
   
      return deferred.promise;
    };
});

appModule.run(function($templateCache) {
	$templateCache.put('modalContentTemplate.html', "<div tabindex=\"-1\" role=\"dialog\" class=\"modal fade\" ng-class=\"{in: animate}\" ng-style=\"{'z-index': 105000 + index*10, display: 'block'}\" ng-click=\"close($event)\">\n"+"    <div modal-transclude></div>\n"+"</div>");
});

//协议（只对app有效）
//注：开发调试可以用HTTP，上传到AppStore需要改为HTTPS，因为苹果引进了app传输安全（ATS），所有iOSapp需使用安全的HTTPS链接与服务器进行通信，所以也要求服务器配置了HTTPS证书
var protocal = "";

//主机名
var serverHost = '';
//var serverHost = 'http://localhost:8080';
//var serverHost = 'http://192.168.2.202:80';
//var serverHost = 'http://192.168.2.33:7004';

//封装成app时请把下面和index.html的appInd改为true
var appInd = false;
if(appInd){
	serverHost = window.localStorage.getItem("serverHost");
}

//修改提交参数
appModule.config(function($httpProvider) {
	$httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
	$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

	// Override $http service's default transformRequest
	$httpProvider.defaults.transformRequest = [function(data) {
        /**
         * The workhorse; converts an object to x-www-form-urlencoded serialization.
         * @param {Object} obj
         * @return {String}
         */
        var param = function(obj) {
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;

            for (name in obj) {
                value = obj[name];

                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        //fullSubName = '' + i + '';
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = subName;
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(name) + '='
                            + encodeURIComponent(value) + '&';
                }
            }
            return query.length ? query.substr(0, query.length - 1) : query;
        };
        return angular.isObject(data) && String(data) !== '[object File]'
                ? param(data)
                : data;
    }];

});
//存储的方法
appModule.service("Store", function(){
	//html5本地存储
	var storage = window.localStorage || null;
	var sessionStorage = window.sessionStorage || null;
	var store = {
			get : function(key) {
				var value = sessionStorage.getItem(key);
				if(value) {
					return value;
				}
				if(storage){
					value =  storage.getItem(key);
					if(value){
						return value;
					} else {
						return "";
					}
				}
			},
			set : function(key, value){
				if(storage){
					return storage.setItem(key, value);
				}
			},
			remove : function(key){
				if(storage){
					return storage.removeItem(key);
				}
			},
			getStorage : function(){
				return storage;
			}
		};
		return store;

}).factory('alertService', function ($rootScope, $timeout, $filter) {
    var alertService = {};
    // 创建一个全局的 alert 数组
    $rootScope.alerts = [];

	alertService.add = function(type, msg, timeout, callback) {
		if(appInd){
			if(window.plus){
				plus.nativeUI.toast($filter('translate')(msg));
				if(callback) callback(this);
			}else{
				document.addEventListener("plusready",function(){
					plus.nativeUI.toast($filter('translate')(msg));
					if(callback) callback(this);
				},false);
			}

		} else {
			//type : danger, success, ""
			var alert = {
				'type' : type,
				'msg' : msg,
				'close' : function() {
					alertService.closeAlert(this);
				}
			};
			$rootScope.alerts.push(alert);
			if (timeout) {
				$timeout(function() {
					angular.forEach($rootScope.alerts, function(a, i) {
						if (alert != a)
							return;
						$rootScope.alerts.splice(i, 1);
					});
					if(callback) callback(this);

				}, timeout);
			}
		}
	};

    alertService.closeAlert = function(alert) {
      alertService.closeAlertIdx($rootScope.alerts.indexOf(alert));
    };

    alertService.closeAlertIdx = function(index) {
      $rootScope.alerts.splice(index, 1);
    };

    alertService.closeAllAlerts = function(){
        if($rootScope.alerts && $rootScope.alerts.length > 0){
            $rootScope.alerts = [];
        }
    };

    return alertService;

});

var alertCount = 0;	//全局提示次数
//拦截器
appModule.factory('tokenInterceptor', function($q, $rootScope, $pageSize, Store, $window, alertService, $timeout) {
	$rootScope.cwn_title = "cwn_title"

	var url = $window.location.href;
	var count = 0;

	url = url.substring(url.indexOf("views/") + 6);
	count = url.split("\/");

	var prefix = "";
	for(var i = 1; i < count.length; i++) {
		prefix += "../";
	}
		
	var logout = function(){
		if(appInd){
			callNative(function(){
				plus.storage.removeItem("token");
			});
		}
		Store.getStorage().clear();
		window.sessionStorage.clear();
		backLogin(getRelatievePathPrefix());
	};
	
	var interceptor = {
		'request' : function(req) {
			var token = Store.get("token");
			var developer = 'mobile';
			var callback = 'JSON_CALLBACK';
			var reqtime = new Date().getTime();
			//请求默认加上
			if(req.method == 'POST'){
				req.data = req.data || {};
				if (!req.data.token) {
					req.data.token = token;
				}
				if(!req.data.developer) {
					req.data.developer = developer;
				}
				if(!req.data.pageSize) {
					req.data.pageSize = $pageSize;
				}
				if(!req.data.callback) {
					req.data.callback = callback;
				}
				if(!req.data.reqtime) {
					req.data.reqtime = reqtime;
				}
			} else if(req.url.indexOf(".html") > 0){
				//请求静态方法，直接跳过
				return req;
			} else {
				req.params = req.params || {};
				if (!req.params.token) {
					req.params.token = token;
				}
				if(!req.params.developer) {
					req.params.developer = developer;
				}
				if(!req.params.pageSize && req.url.indexOf('pageSize') < 0) {
					req.params.pageSize = $pageSize;
				}
				if(!req.params.callback) {
					req.params.callback = callback;
				}
				if(!req.params.reqtime) {
					req.params.reqtime = reqtime;
				}

			}
			if(req.url.indexOf(serverHost) < 0){
				req.url = serverHost + req.url;
			}
			// 成功的请求方法
			return req;
		},
		'response' : function(response) {
			// 响应成功
			var data = response.data;
			if(data && data.status == 'error' && response.config.url.indexOf('app/login?username=') < 0){
				if(alertCount < 1){
					alertService.closeAllAlerts();
					alertCount++;
			    	if(data.msg == "ERROR_TOKEN_INVALID") {
			    		alertService.add('danger', data.msg, 2000, function(){
			    			Store.getStorage().clear();
							window.sessionStorage.clear();
			    			logout();
			    		});
			    	}else if(data.msg == "error_landed_somewhere_else"){//账号在其他地方登录，包括移动和PC端
			    		
			    		alertService.add('danger', data.msg, 2000, function(){
			    			logout();
			    		});
			    		
			    	} else {
			    		if(data.msg_level && data.msg_level < 0){
					    	alertService.add('danger', data.msg, 2000, function(){
					    		if(appInd){
					    			if(window.plus){
					    				plus.nativeUI.closeWaiting();
									}else{
										document.addEventListener("plusready",function(){
											plus.nativeUI.closeWaiting();
										},false);
									}
					    		}
								back();
					    	});
			    		} else {
			    			alertService.add('danger', data.msg, 2000);
			    			if(appInd){
				    			plus.nativeUI.closeWaiting();
				    		}
					    	$rootScope.$broadcast('msg:error');
			    		}
			    	}

				}
			}
			return response;
		},
		'responseError' : function(rejection) {
			if(appInd){
				if(isLogin){
					//如果用户没有输入http或者https协议头，则提示错误
					if(rejection.config.url.indexOf("http://") != -1 || rejection.config.url.indexOf("https://") != -1){
						//存在协议头，但不存在网址的情况
						alertService.add('success', labels[Store.get("globalLang")]['ERROR_CONNECT_FAIL'], 2000);
					}else{
						//不存在协议头的情况
						alertService.add('success', labels[Store.get("globalLang")]['ERROR_CONNECT_URL'], 2000);
					}
				}
			} else {
				switch (rejection.status) {
				case 401:
					//未授权：登录失败 如果当前不是在登录页面
					if (rejection.config.url.indexOf('login.html') < 0){
						alertService.add('danger', 'session_timeOut', 2000, function(){
						});
						$rootScope.$broadcast('page:loginRequired');
					}
					break;
				case 403:	// 禁止访问
					$rootScope.$broadcast('page:forbidden');
					break;
				case 404:	//无法找到文件
					$rootScope.$broadcast('page:notFound');
					alertService.add('danger', 'request_failed', 2000, function(){
					});
					break;
				case 500:
					$rootScope.$broadcast('page:error');
					alertService.add('danger', 'server_error', 2000, function(){
					});
					break;
				}
			}
			return rejection;
		}
	};
	return interceptor;
});
//注册拦截器
appModule.config(function($httpProvider) {
	$httpProvider.interceptors.push('tokenInterceptor');
});

//removeIndexs 可为单个下标，也可以是下标的数组
/*Array.prototype.remove = function(removeIndexs){
	if(removeIndexs instanceof Array){
		for(var i=0;i<removeIndexs.length;i++){
			var index = removeIndexs[i] - i;
			this.remove(index);
		}
	}else{
		for(var i=removeIndexs+1;i<this.length;i++){
			this[i-1] = this[i];
		}
		this.length = this.length - 1;
	}
}*/
//删除数组元素

var app = {
		LZ : function(x) {
			return (x < 0 || x > 9 ? "" : "0") + x
		},
		isInteger : function(val) {
			return (new RegExp(/^\d+$/).test(val));
		},
		getUrlPath : function(path){
			var url = window.location.href;
			var count = 0;

			url = url.substring(url.indexOf("views/") + 6);
			count = url.split("\/");

			var prefix = "";
			for(var i = 1; i < count.length; i++) {
				prefix += "../";
			}
			return prefix+path;
		},
		urlAddParams : function (params, url){
   			var query = '';
    		for (name in params) {
            	query += '&' + name + '=' + params[name];
        	}
    		if (query != '') {
        		if (url.indexOf('?') > 0) {
            		url += query;
        		} else {
            		url += '?' + query.substring(1);
        		}
    		}
    		return url;
		},
		getUrlParam : function (name)
		{
     		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     		var r = window.location.search.substr(1).match(reg);
     		if(r!=null)return  unescape(r[2]); return null;
		},
		getInt : function(str, i, minlength, maxlength) {
			for (var x = maxlength; x >= minlength; x--) {
				var token = str.substring(i, i + x);
				if (token.length < minlength) {
					return null;
				}
				if (app.isInteger(token)) {
					return token;
				}
			}
			return null;
		},
		formatDate : function (date, format) {
			format = format + "";
			var result = "";
			var i_format = 0;
			var c = "";
			var token = "";
			var y = date.getFullYear() + "";
			var M = date.getMonth() + 1;
			var d = date.getDate();
			var E = date.getDay();
			var H = date.getHours();
			var m = date.getMinutes();
			var s = date.getSeconds();
			var u = date.getMilliseconds();
			var yyyy, yy, MMM, MM, dd, hh, h, mm, ss, ampm, HH, H, KK, K, kk, k;
			// Convert real date parts into formatted versions
			var value = {};
			if (y.length < 4) {
				y = "" + (y - 0 + 1900);
			}
			value["y"] = "" + y;
			value["yyyy"] = "" + y;
			value["yy"] = y.substring(2, 4);
			value["M"] = M;
			value["MM"] = app.LZ(M);
			value["MMM"] = app.MONTH_NAMES[M - 1];
			value["NNN"] = app.MONTH_NAMES[M + 11];
			value["d"] = d;
			value["dd"] = app.LZ(d);
			value["E"] = app.DAY_NAMES[E + 7];
			value["EE"] = app.DAY_NAMES[E];
			value["H"] = H;
			value["HH"] = app.LZ(H);
			if (H == 0) {
				value["h"] = 12;
			} else if (H > 12) {
				value["h"] = H - 12;
			} else {
				value["h"] = H;
			}
			value["hh"] = app.LZ(value["h"]);
			if (H > 11) {
				value["K"] = H - 12;
			} else {
				value["K"] = H;
			}
			value["k"] = H + 1;
			value["KK"] = app.LZ(value["K"]);
			value["kk"] = app.LZ(value["k"]);
			if (H > 11) {
				value["a"] = "PM";
			} else {
				value["a"] = "AM";
			}
			value["m"] = m;
			value["mm"] = app.LZ(m);
			value["s"] = s;
			value["ss"] = app.LZ(s);
			value["u"] = u;
			while (i_format < format.length) {
				c = format.charAt(i_format);
				token = "";
				while ((format.charAt(i_format) == c) && (i_format < format.length)) {
					token += format.charAt(i_format++);
				}
				if (value[token] != null) {
					result += value[token];
				} else {
					result += token;
				}
			}
			return result;
		},
		MONTH_NAMES : new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct',
				'Nov', 'Dec'),
		DAY_NAMES : new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'),
		parseDate : function(val, format) {
			val = val + "";
			format = format + "";
			var i_val = 0;
			var i_format = 0;
			var c = "";
			var token = "";
			var token2 = "";
			var x, y;
			var now = new Date();
			var year = now.getFullYear();
			var month = now.getMonth() + 1;
			var date = 1;
			var hh = now.getHours();
			var mm = now.getMinutes();
			var ss = now.getSeconds();
			var ampm = "";

			while (i_format < format.length) {
				// Get next token from format string
				c = format.charAt(i_format);
				token = "";
				while ((format.charAt(i_format) == c) && (i_format < format.length)) {
					token += format.charAt(i_format++);
				}
				// Extract contents of value based on format token
				if (token == "yyyy" || token == "yy" || token == "y") {
					if (token == "yyyy") {
						x = 4;
						y = 4;
					}
					if (token == "yy") {
						x = 2;
						y = 2;
					}
					if (token == "y") {
						x = 2;
						y = 4;
					}
					year = app.getInt(val, i_val, x, y);
					if (year == null) {
						return 0;
					}
					i_val += year.length;
					if (year.length == 2) {
						if (year > 70) {
							year = 1900 + (year - 0);
						} else {
							year = 2000 + (year - 0);
						}
					}
				} else if (token == "MMM" || token == "NNN") {
					month = 0;
					for (var i = 0; i < app.MONTH_NAMES.length; i++) {
						var month_name = app.MONTH_NAMES[i];
						if (val.substring(i_val, i_val + month_name.length).toLowerCase() == month_name.toLowerCase()) {
							if (token == "MMM" || (token == "NNN" && i > 11)) {
								month = i + 1;
								if (month > 12) {
									month -= 12;
								}
								i_val += month_name.length;
								break;
							}
						}
					}
					if ((month < 1) || (month > 12)) {
						return 0;
					}
				} else if (token == "EE" || token == "E") {
					for (var i = 0; i < app.DAY_NAMES.length; i++) {
						var day_name = app.DAY_NAMES[i];
						if (val.substring(i_val, i_val + day_name.length).toLowerCase() == day_name.toLowerCase()) {
							i_val += day_name.length;
							break;
						}
					}
				} else if (token == "MM" || token == "M") {
					month = app.getInt(val, i_val, token.length, 2);
					if (month == null || (month < 1) || (month > 12)) {
						return 0;
					}
					i_val += month.length;
				} else if (token == "dd" || token == "d") {
					date = app.getInt(val, i_val, token.length, 2);
					if (date == null || (date < 1) || (date > 31)) {
						return 0;
					}
					i_val += date.length;
				} else if (token == "hh" || token == "h") {
					hh = app.getInt(val, i_val, token.length, 2);
					if (hh == null || (hh < 1) || (hh > 12)) {
						return 0;
					}
					i_val += hh.length;
				} else if (token == "HH" || token == "H") {
					hh = app.getInt(val, i_val, token.length, 2);
					if (hh == null || (hh < 0) || (hh > 23)) {
						return 0;
					}
					i_val += hh.length;
				} else if (token == "KK" || token == "K") {
					hh = app.getInt(val, i_val, token.length, 2);
					if (hh == null || (hh < 0) || (hh > 11)) {
						return 0;
					}
					i_val += hh.length;
				} else if (token == "kk" || token == "k") {
					hh = app.getInt(val, i_val, token.length, 2);
					if (hh == null || (hh < 1) || (hh > 24)) {
						return 0;
					}
					i_val += hh.length;
					hh--;
				} else if (token == "mm" || token == "m") {
					mm = app.getInt(val, i_val, token.length, 2);
					if (mm == null || (mm < 0) || (mm > 59)) {
						return 0;
					}
					i_val += mm.length;
				} else if (token == "ss" || token == "s") {
					ss = app.getInt(val, i_val, token.length, 2);
					if (ss == null || (ss < 0) || (ss > 59)) {
						return 0;
					}
					i_val += ss.length;
				} else if (token == "u") {
					u = app.getInt(val, i_val, token.length, 3);
					if (u == null || (u < 0) || (u > 999)) {
						return 0;
					}
					i_val += u.length;
				} else if (token == "a") {
					if (val.substring(i_val, i_val + 2).toLowerCase() == "am") {
						ampm = "AM";
					} else if (val.substring(i_val, i_val + 2).toLowerCase() == "pm") {
						ampm = "PM";
					} else {
						return 0;
					}
					i_val += 2;
				} else {
					if (val.substring(i_val, i_val + token.length) != token) {
						return 0;
					} else {
						i_val += token.length;
					}
				}
			}
			// If there are any trailing characters left in the value, it doesn't
			// match
			if (i_val != val.length) {
				//return 0;
			}
			// Is date valid for month?
			if (month == 2) {
				// Check for leap year
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) { // leap
					// year
					if (date > 29) {
						return 0;
					}
				} else {
					if (date > 28) {
						return 0;
					}
				}
			}
			if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
				if (date > 30) {
					return 0;
				}
			}
			// Correct hours value
			if (hh < 12 && ampm == "PM") {
				hh = hh - 0 + 12;
			} else if (hh > 11 && ampm == "AM") {
				hh -= 12;
			}
			return new Date(year, month - 1, date, hh, mm, ss);
		},
		//截取指定字节长度的字符串，中文占2位
		substr : function (str, startp, endp) {
			var i = 0;
			c = 0;
			unicode = 0;
			rstr = '';
			var len = str.length;
			var sblen = this.getChars(str);
			if (startp < 0) {
				startp = sblen + startp;
			}
			if (endp < 1) {
				endp = sblen + endp;// - ((str.charCodeAt(len-1) < 127) ? 1 : 2);
			}
			// 寻找起点
			for (i = 0; i < len; i++) {
				if (c >= startp) {
					break;
				}
				var unicode = str.charCodeAt(i);
				if (unicode < 127) {
					c += 1;
				} else {
					c += 2;
				}
			}
			// 开始取
			for (; i < len; i++) {
				var unicode = str.charCodeAt(i);
				if (unicode < 127) {
					c += 1;
				} else {
					c += 2;
				}
				rstr += str.charAt(i);
				if (c >= endp) {
					break;
				}
			}
			return rstr;
		},
		getChars : function (str) {
			var i = 0;
			var c = 0.0;
			var unicode = 0;
			var len = 0;
			if (str == null || str == "") {
				return 0;
			}
			len = str.length;
			for (i = 0; i < len; i++) {
				unicode = str.charCodeAt(i);
				if (unicode < 127) { //判断是单字符还是双字符
					c += 1;
				} else { //chinese
					c += 2;
				}
			}
			return c;
		},
		showWaiting : function(){
			var overLayDiv = document.createElement("div");
			overLayDiv.id = "waiting-overlay";
			overLayDiv.innerHTML = '<div class="lo"></div><div class="loading"><span class="loading-logo"></span></div>';
			var wrap = document.querySelector(".wrap");
			if(wrap){
				wrap.appendChild(overLayDiv);
				return overLayDiv;
			}
		},
		dismissWaiting : function(){
			var wrap = document.querySelector(".wrap");
			var overLayDiv = document.getElementById("waiting-overlay");
			if(wrap && overLayDiv){
				wrap.removeChild(overLayDiv);
			}
		},
		isJsonStr : function(input){//判断是不是有效的JSON字符串
			try{
				if(!input){
					return false;
				}
				return JSON.parse(input);
			}catch(e){
				return false;
			}
		},
		alert : function(msg,timeout){
			if(appInd){
				if(window.plus){
					plus.nativeUI.toast(msg);
				}else{
					document.addEventListener("plusready",function(){
						plus.nativeUI.toast(msg);
					},false);
				}
			} else {
				timeout = timeout || 3000;
				var template = '<div style="padding-left:20px; padding-right:20px; width:90%; position:fixed; top:20px; z-index:999;padding-left:20px; padding-right:20px; width:90%; position:fixed; top:20px; z-index:999"><div class="alert alert-warning alert-dismissable"><div><span>'+msg+'</span></div></div></div>';
				
				var elem = document.createElement("div");
				elem.innerHTML = template;
				
				var body = document.body;
				body.appendChild(elem);
				
				window.setTimeout(function(){
					body.removeChild(elem);
				},timeout);
			}
		}
}