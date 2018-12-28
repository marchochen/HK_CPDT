$.include(['js/services/UserService.js'], '../../');
$(function() {
	var len1 = Math.ceil(window.screen.width*0.7);
	var liveModule = angular.module('live', ['globalCwn','userService']);
	liveModule.controller('indexController', ['$scope','Loader','$filter',function($scope,Loader,$filter) {

		var url = "/app/live/liveList";

		var living_params = {
			status : '1',
			isMobile : true
		};
		var live_booking_params = {
			status : '2',
			isMobile : true
		};
		var lived_params = {
			status : '3',
			isMobile : true
		};
		
		$scope.serverHost = serverHost;
		
		$scope.livingList = new Loader(url, living_params,function(data){
			angular.forEach(data.items, function(live,index,array){
				var is_desc_sub="true";
				if(live.lv_desc == undefined || live.lv_desc.replace(/[\d]/g,'').length*14 < len1) {
					is_desc_sub = "false";
				}
				live.is_desc_sub = is_desc_sub;
			});
		});
		
		$scope.liveBookingList = new Loader(url, live_booking_params,function(data){
			angular.forEach(data.items, function(live,index,array){
				var is_desc_sub="true";
				if(live.lv_desc == undefined || live.lv_desc.replace(/[\d]/g,'').length*14 < len1) {
					is_desc_sub = "false";
				}
				live.is_desc_sub = is_desc_sub;
				
				live.time_id = 'time_' + live.lv_id;
				new TimeShow({
					element:{
						tian : "." + live.time_id + ' .tian',
						shi1 : "." + live.time_id + ' .shi1',
						shi2 : "." + live.time_id + ' .shi2',
						fen1 : "." + live.time_id + ' .fen1',
						fen2 : "." + live.time_id + ' .fen2'
					},
					mode: 2
				}, live.lv_start_datetime.replace(new RegExp('-','gm'),'/'));
			});
		});
		
		$scope.LivedList = new Loader(url, lived_params,function(data){
			angular.forEach(data.items, function(live,index,array){
				var is_desc_sub="true";
				if(live.lv_desc == undefined || live.lv_desc.replace(/[\d]/g,'').length*14 < len1) {
					is_desc_sub = "false";
				}
				live.is_desc_sub = is_desc_sub;
				
				live.has_record_for_gensee = false;
				if(live.lv_gensee_record_url !=  null && live.lv_gensee_record_url != undefined && live.lv_gensee_record_url != 'false' && live.lv_gensee_record_url.length > 0){
					live.has_record_for_gensee = true;
				}
				
			});
		});
		
		//要点折叠展开
		$scope.OpenOrRetract = function(_this){
        	var id = $(_this.currentTarget).find(".zhibo-arrow");
            if($(id).prev().css('display') == 'block'){
                $(id).prev().css({'display':'-webkit-box'});
                $(id).css({'background-position':'0px -1369px'})
            }else{
                $(id).prev().css({'display':'block'});
                $(id).css({'display':'block','background-position':'0px -1388px'});
            }
        }
		
		
	}]);
	
	liveModule.controller('detailController', ['$scope','Ajax','$sce','User','$interval','dialogService','alertService','$filter',function($scope,Ajax,$sce,User,$interval,dialogService,alertService,$filter) {
		
		$scope.serverHost = serverHost;
		
		var lvId = app.getUrlParam('lvId');
		
		var url = "/app/live/detail/" + lvId;
		/**
		 * 获取当前用户
		 */
		User.userInfo(function(data){
			$scope.user = data.regUser;
		});
		
		$scope.show = false;
		/**
		 * 获取数据
		 */
		Ajax.post(url, {},function(data){
			$scope.records_url = $sce.trustAsResourceUrl("http://e.vhall.com/webinar/inituser/"+data.live.lv_webinar_id+"?embed=video&email="+$scope.user.usr_ste_usr_id+"@cyberwisdom.net&name="+$scope.user.usr_display_bil);
			$scope.records_url_chat = $sce.trustAsResourceUrl("http://e.vhall.com/webinar/inituser/"+data.live.lv_webinar_id+"?email="+$scope.user.usr_ste_usr_id+"@cyberwisdom.net&name="+$scope.user.usr_display_bil);
			$scope.lv = data.live;
			$scope.liveRecordsList = data.rows;
			$scope.show = $scope.lv.lv_need_pwd;
			
			$scope.gensee_url = $sce.trustAsResourceUrl(data.live.lv_student_join_url + "?nickname=" + $scope.user.usr_display_bil);
			
			if(data.live.lv_mode_type == 'GENSEE' ){
				$scope.isVhall = 3;
				if(data.live.lv_type == 3){
					$scope.gensee_url = $sce.trustAsResourceUrl(data.live.lv_gensee_record_url + "?nickname=" + $scope.user.usr_display_bil);
				}
			}else if(data.live.lv_mode_type == 'QCLOUD' ){
				$scope.isVhall = 2;
				var player = new TcPlayer('id_video', {
					"m3u8": data.live.lv_hls_downstream_address,
					"autoplay" : true,      //iOS下safari浏览器是不开放这个能力的
					"coverpic" : "",
					"wording": {
						1: $filter('translate')('lab_live_qcloud_error_1'),
						2: $filter('translate')('lab_live_qcloud_error_2'),
						3: $filter('translate')('lab_live_qcloud_error_1'),
						4: $filter('translate')('lab_live_qcloud_error_4'),
						1001: $filter('translate')('lab_live_qcloud_error_1'),
						1002: $filter('translate')('lab_live_qcloud_error_4'),
					    2032: $filter('translate')('lab_live_qcloud_error_2032'),
					    2048: $filter('translate')('lab_live_qcloud_error_2048')
					},
					'live' : true,
					"width" :  '100%',//视频的显示宽度，请尽量使用视频分辨率宽度
					"height" : '50%'//视频的显示高度，请尽量使用视频分辨率高度
				});
			}else{
				$scope.isVhall = 1;
			}
			
			if(data.lv_excess){
				$(".content").html('');
				alertService.add('warning', 'lab_live_excess', 3000,function(){
					changRecordsStatus();
					back();
				});
			}
		});
		
		function changRecordsStatus(){
			var url = "/app/live/updateRecordsStatus/" + lvId;
			Ajax.post(url, {status:0},function(data){
			});
		}
		
		$scope.back = function(){
			changRecordsStatus();
			back();
		}
		
		
		$scope.livePwd = "";
		
		/**
		 * 判断密码
		 */
		$scope.submitPwd = function(){
			var pwd = $scope.livePwd;
			pwd = $("input[name='livePwd']").val();
			if(pwd == undefined || pwd == ""){
				alertService.add('warning', 'password_not_null', 2000);
				return;
			}
			
			var param = {password : pwd, lv_enc_id : lvId }
			Ajax.post("/app/live/checkPwd", param,function(data){
				
				var result = data.status;
				if(result != undefined && result){
					$scope.show = false;
				}else{
					alertService.add('warning', 'ERROR_PWD_ERROR', 2000);
				}
				
			});
		}
		
	}]);
	
});
