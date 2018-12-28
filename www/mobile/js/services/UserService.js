angular.module('userService',[]).factory('User',function($http,Store) {
	return {
		userInfo : function(callback) {

			var userInfoStr = Store.get("userInfo");
			
			if(userInfoStr){
				try{
					var userInfoJson = JSON.parse(userInfoStr);
					if(userInfoJson){
						callback(userInfoJson);
						return;
					}
				}catch(e){
				}
				
			}
			
			var url = "/app/userInfo/detail";
			$http.jsonp(url).success(function(data) {
				Store.set("userInfo",JSON.stringify(data));
				callback(data);
			}.bind(this));
		},
		updateUserInfoCache : function(){//更新用户信息缓存
			$http.jsonp(url).success(function(data) {
				Store.set("userInfo",JSON.stringify(data));
			}.bind(this));
		}
	}
});