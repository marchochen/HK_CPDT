$.include([
   'lib/angular/angular-touch.min.js',
   'js/services/ModuleService.js',
   'js/services/VideoService.js',
   'js/directives/webViewIframe.js',
], '../../');

var num = 1;
var timeLeft = 0;
var filter;
var alertObj;
var tstDetailConScope;
$(function(){
	var modModule = angular.module('module', ['globalCwn', 'ngTouch','WebviewIframe']);
	
	//考试开始
	modModule.controller('tstStartController', ['$scope', 'Store', 'Ajax', 'modalService', '$filter', function($scope, Store, Ajax, modalService, $filter) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		$scope.itmId = app.getUrlParam("itmId") || Store.get("itmId");
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId
		};

		$scope.user = {};
		$scope.data = {};
		Ajax.post('/app/module/getTstStart', params, function(data){
			$scope.data = data.rows[0];
			if($scope.data != undefined && $scope.data.mod_managed_ind != undefined && $scope.data.mod_started_ind != undefined && $scope.data.mod_managed_ind == 1 && $scope.data.mod_started_ind != 1){
				modalService.modal(function($scope, $modalInstance) {
					$scope.modalText = $filter('translate')('label_not_start_desc');
					$scope.modalOk = function() {
						$modalInstance.close();
						back();
					};
					$scope.noShowCancel = true;
				});
				return;
			}
			$scope.serverHost = serverHost;
			$scope.user = data.user;
			$scope.duration = timeToStr($scope.data.res.res_duration*60);
		});
		$scope.start = function(){
			Store.set("tkhId", tkhId);
			Store.set("modId", modId);
			Store.set("itmId", $scope.itmId);
			Store.set("window_name", "tst_play");
			Store.set("user", angular.toJson($scope.user));
			clicked('tst_detail.html', true);
		}
		
		$scope.leave = function(){
			if(appInd){				    		
    			changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
    				back();
    			});
	    	}else{
				back();
			}
		}
		
	}]);
	
	//进行考试
	modModule.controller('tstDetailController', ['$scope', 'Store', 'Ajax', 'modalService', 'alertService','$filter','$http',
			function($scope, Store, Ajax, modalService, alertService,$filter,$http) {
		tstDetailConScope = $scope;
		var tkhId = Store.get("tkhId");
		var modId = Store.get("modId");
		var itmId = Store.get("itmId");
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId,  	
			window_name : Store.get("window_name")
		};
		$scope.question = {};
		$scope.header = {};
		$scope.total = 0;
		$scope.duration = timeToStr(0);
		filter = $filter; 
		alertObj = alertService;
		$scope.showPrev = false;
		$scope.serverHost = serverHost;
		
		//获取试题
		$scope.isLanding = true;
		
		/*
		 * 换成使用post请求。不使用Ajax，ajax的jsonp()方式请求对于一些数据不能正确解析
		 */
		$http({
			url: "/app/module/getTstDetail",
			method: 'POST',
			data: params,
			dataType: 'json'
			}).success(function(data){
				if(data.hashMap.error != undefined && data.hashMap.error != ""){
					modalService.modal(function($scope, $modalInstance) {
						$scope.modalText = data.hashMap.error;
						$scope.modalOk = function() {
							$modalInstance.close();
							back();
						};
						$scope.noShowCancel = true;
					});
					return;
				}
				$scope.question = data.hashMap.question;
				$scope.header = data.hashMap.header;
				$scope.total = data.hashMap.question.length;
				$scope.start_time = data.hashMap.start_time;
				timeLeft = data.hashMap.header[0].duration*60;
				$scope.duration = timeToStr(timeLeft);
				for(var i=0;i<$scope.question.length;i++){
					if($scope.question[i].header[0].type == 'FSC' || $scope.question[i].header[0].type == 'DSC'){
						var new_que_id = "";
						for(var j=0;j<$scope.question[i].question_list[0].question.length;j++){
							new_que_id += $scope.question[i].question_list[0].question[j].id + "[|]";
						}
						$scope.question[i].id = new_que_id.substring(0,new_que_id.length-3);
					}
				}
				$scope.isLanding = false;
				startInterval();
				
				if($scope.total <= 1){
					$scope.showNext = false;
				}else{
					$scope.showNext = true;
				}
			});
		
		$scope.prev = function(){
			if(num>1){
				num--;
				$("dl").hide();
				$("dl").eq(num-1).show();
				
				$scope.showNext = true;
				
				if(num == 1){
					$scope.showPrev = false;
				}
				
			}
		}

		$scope.next = function(){
			if(num<$("dl").length){
				num++;
				$("dl").hide();
				$("dl").eq(num-1).show();
				
				$scope.showPrev = true;
				
				if(num == $("dl").length){
					$scope.showNext = false;
				}
			}
		}
		
		$scope.open = function(val){
			show();
			num = val;
			$("dl").hide();
			$("dl").eq(num-1).show();
			
			if(num == 1){
				$scope.showPrev = false;
			}else{
				$scope.showPrev = true;
			}
			
			if(num == $("dl").length){
				$scope.showNext = false;
			}else{
				$scope.showNext = true;
			}
		}
		
		$scope.submit = function(){
			$(".header-datika").slideUp();
			$(".header-overlay").removeClass("show");
			if($("#noanswer").html() != 0){
				modalService.modal(function($scope, $modalInstance) {
					$scope.modalText = 'mod_tst_submit_notice';
					$scope.modalOk = function() {
						$modalInstance.close();
						submitTst();
					};
					$scope.modalCancel = function() {
						$modalInstance.dismiss('cancel');
					};
				});
			} else {
				submitTst();
			}
		}
		
		$scope.analogClick = function(target){
			$("#" + target).click().focus();
		}
		
		//自动计时
		function startInterval(){
			self.setInterval(function(){
				timeLeft--;
				$("#timeLeft").html(timeToStr(timeLeft));
				if(timeLeft == 0){
					submitTst();
				}
			}, 1000);
		}
		
		//提交答案
		function submitTst(){
			alertService.add('danger', 'mod_tst_submiting', 2000);
			if(appInd){
				wait = plus.nativeUI.showWaiting();
			} else {
				$scope.isLanding = true;
			}
			var que_id_str = "";
			var que_anwser_option_str = "";
			var que_anwser_option_id_str = "";
			//统计回答情况
			var type = "MC";
			$(".content-3 dl").each(function(){
				type = $(this).attr("type");
				que_id_str += $(this).attr("id") + "[|]";
				que_anwser_option_id_str += $(this).attr("length") + "[|]";
				var ans_opetion_str = "";
				var ans_opetion_id_str = "";
				if(type == "MC" || type == "TF"){  //选择题
					$(this).find("input:checked").each(function(){
						ans_opetion_str += $(this).val() + "~";
					})
				}else if(type == "FB"){ //填空题
					$(this).find("input").each(function(){
						ans_opetion_str += $(this).val().replace(/\~/g, "-") + "~";
					})
				}else if(type == "ES"){ //问答题
					$(this).find("textarea").each(function(){
						ans_opetion_str += $(this).val().replace(/\~/g, "-") + "~";
					})
				}else if(type == "MT"){ //配对题
					$(this).find(".select-on").find("input").each(function(){
						ans_opetion_str += $(this).val() + "~";
					})
				}
				if(ans_opetion_str != ""){
					ans_opetion_str = ans_opetion_str.substring(0,ans_opetion_str.length-1)
				}
				for(var i=0;i<$(this).attr("id").split("[|]").length;i++){
					que_anwser_option_str += ans_opetion_str + "[|]";
				}
			})
			var sub_params = {
				isMobile : true, 
				tkh_id : tkhId,
				mod_id : modId,
				que_id_str : que_id_str.substring(0,que_id_str.length-3),
				que_anwser_option_str : que_anwser_option_str.substring(0,que_anwser_option_str.length-3),
				que_anwser_option_id_str : que_anwser_option_id_str.substring(0,que_anwser_option_id_str.length-3),
				start_time : $scope.start_time
			}
			
			$http({
				url: "/app/module/submitTst",
				method: 'POST',
				data: sub_params,
				dataType: 'json'
				}).success(function(data){
					if(data.score.error != undefined && data.score.error != ""){
						modalService.modal(function($scope, $modalInstance) {
							$scope.modalText = data.score.error;
							$scope.modalOk = function() {
								if(appInd){
									wait.close();
								}
								$modalInstance.close();
								back();
							};
							$scope.noShowCancel = true;
						});
						return;
					}
					Store.set("score", angular.toJson(data.score));
					if(appInd){
						changeWebviewDetail('item' + plus.storage.getItem("nowItmId"), function(){
							clicked('tst_score.html?itmId='+plus.storage.getItem("nowItmId")+'&tkhId='+tkhId+'&modId='+modId+'', true);
							wait.close();
						});
					}else{
						Store.set("score", "");
						clicked('tst_score.html?itmId='+itmId+'&tkhId='+tkhId+'&modId='+modId+'', true);
					}
				});
		}
	}]);
	
	//考试成绩
	modModule.controller('tstScoreController', ['$scope', 'Store', 'Ajax', function($scope, Store, Ajax) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		$scope.itmId = app.getUrlParam("itmId") || Store.get("itmId");
		$scope.tkhId = tkhId;
		
		var isGoDirectly = app.getUrlParam("isGoDirectly") || false;
		
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId
		};
		$scope.data = {};
		$scope.serverHost = serverHost;
		
		$scope.show = function(){
            $("#tika-and").show();
            $("#tika-more").hide();
            $("#tika-shou").show();
        };
        $scope.hide = function(){
        	$("#tika-shou").hide();
            $("#tika-and").hide();
            $("#tika-more").show();
        };
		
        $scope.changeNavColor = function(){
        	angular.forEach($scope.question, function(que,ind){
        		//问答题 (得到了分数但没得到满分，将那题改为部分正确的)
				if(que.header[0].type=='ES' && que.body[0].interaction && que.body[0].interaction[0].score > $scope.rows[ind].pgr_score && $scope.rows[ind].pgr_score > 0){
					$scope.rows[ind].pgr_score = -2;
					$scope.correct_cnt = $scope.correct_cnt - 1,//答错题目数
					$scope.partially_correctt_cnt = $scope.partially_correctt_cnt + 1; //部分正确题目数
				}
				//多选题 与  配对题 与 填空题
				if((que.header[0].type=='MC' && (que.outcome[0].logic=='AND' || que.outcome[0].logic=='OR')) 
						|| (que.header[0].type == 'MT' && que.body[0].source != null)
						|| (que.header[0].type == 'FB')){
					var data_source = null;
					if(que.header[0].type=='MC'){
						data_source = que.body[0].interaction[0].option;
					}else if(que.header[0].type == 'MT'){
						data_source = que.outcome;
					}else if(que.header[0].type == 'FB'){
						data_source = que.body[0].interaction;
					}
					angular.forEach(data_source, function(ans,i){ //循环所有的选项
						var orderid = que.order-1; //题目顺序
						
						if((que.header[0].type=='MC' &&　que.outcome[0].feedback[i].score != undefined && que.outcome[0].feedback[i].score >= 1 
								&& $scope.isOk(que.outcome[0].feedback[i].condition , orderid))
								|| (que.header[0].type == 'MT' && $scope.result[orderid].interaction[i].response && ans.feedback[0].condition == $scope.result[orderid].interaction[i].response[0].text)
								|| (que.header[0].type == 'FB' && $scope.result[orderid].interaction[i].response && $scope.result[orderid].interaction[i].response[0].text.trim() == que.explanation[i].rationale[0].condition.trim())){
							//学员存在有答对的答案的时候，题目导航用正确的显示，回答正确的数目加1，回答错误的数目减1
							if($scope.rows[orderid].pgr_score == 0){
								$scope.rows[orderid].pgr_score = -2; //知识为了题目导航显示颜色使用
								$scope.incorrect_cnt = $scope.incorrect_cnt - 1,//答错题目数
								$scope.partially_correctt_cnt = $scope.partially_correctt_cnt + 1; //部分正确题目数
							}
						}
					});
				}
			});
        }
        
        $scope.tstDeatil = function(attempt,init_flag){
        	Ajax.post('/app/module/getTstScore/'+attempt, params, function(data){
        		$scope.attempt_nbr = data.rows[0].pgr_attempt_nbr;
        		$scope.total_score = Math.round(data.rows[0].pgr_score);
        		$scope.completion_status = data.rows[0].pgr_completion_status;
        		$scope.incorrect_cnt = 0;
        		$scope.correct_cnt = 0;
        		$scope.not_score_cnt = 0;
        		$scope.partially_correctt_cnt = 0;
        		for(var i=0;i<data.rows.length;i++){
        			if(data.rows[i].pgr_correct_ind == 0){
        				$scope.incorrect_cnt = data.rows[i].pgr_total;
        			} else {
        				$scope.correct_cnt = data.rows[i].pgr_total;
        			}
        		}
        		
        		if(init_flag){
        			$scope.user = data.user;
        			$scope.total_attempt_nbr = data.rows[0].pgr_attempt_nbr;//最高次数（总次数）
        			$scope.max_score_pgr_attempt = data.rows[0].pgr_attempt_nbr;
        			$scope.full_score = data.rows[0].pgr_max_score;
        			$scope.pass_score = data.rows[0].pgr_max_score * data.rows[0].mod.mod_pass_score / 100;
        			$scope.pass_percent = data.rows[0].mod.mod_pass_score  + "%";
        			
        			var arrayObj = new Array();
        			for(var i=0;i<$scope.attempt_nbr;i++){
        				arrayObj.push(i+1);
        			}
        			$scope.attempt_list = arrayObj;
        			
        			//最高分数
        			Ajax.post('/app/module/getMaxProgressAttempt', params, function(data){
        				if(data.progressList != undefined && data.progressList.length > 0){
        					$scope.max_score_pgr_attempt = data.progressList[0].pgr_attempt_nbr;//分数最高的次数
        				}
        			});
        		}
        		
        		//未评分
        		Ajax.post('/app/module/selectNotScore?attempt='+$scope.attempt_nbr, params, function(data){
        			$scope.not_score_cnt = data.long;
        			$scope.incorrect_cnt = $scope.incorrect_cnt-data.long;
        		});
        		
        		/**
        		 * 获取每个题目考试题目对错情况
        		 */
        		var ansparams = {
        				isMobile : true,  
        				tkh_id : tkhId,
        				mod_id : modId,
        				attempt : $scope.attempt_nbr	
        		}
        		Ajax.post('/app/module/getAllAnswerDetail', ansparams, function(data){
        			$scope.rows = data.rows;
        			$scope.subrows = data.rows.slice(15);
        		});
        		
        		/**
        		 * 获取考试答题详情
        		 */
        		var que_id_list = [0];
        		var tstparams = {
        				isMobile : true,  
        				tkh_id : tkhId,
        				mod_id : modId,
        				que_id_lst : que_id_list,
        				attempt : $scope.attempt_nbr
        		};
        		Ajax.post('/app/module/getTstReportDetail', tstparams, function(data){
        			
        			$scope.mod_showInd = data.hashMap.mod_showInd;
    				$scope.mod_passedInd = data.hashMap.mod_passedInd;
    				
        			if(data.hashMap.student != undefined){
        				
        				$scope.question = data.hashMap.student[0].test[0].body[0].question;
        				$scope.result = data.hashMap.student[0].test[0].body[0].result;
        				$scope.changeNavColor();
        				
        			}
        		});
        		
        	});
        }
        
        //初始化
        $scope.tstDeatil(0,true);
        
        $scope.back = function(){
			if(isGoDirectly == 'true' || isGoDirectly == true){
				back();
			}else{
				if(appInd){
					var itmId = wbEncryptor.decrypt(app.getUrlParam("itmId"));
					changeWebviewDetail("item"+itmId,function(){
						back(3);
					});
				}else{
					back(3);
				}
			}
		}
		
		if(appInd){
			$(function(){
				changeBackButtion(function(){
					if(isGoDirectly == 'true' || isGoDirectly == true){
						back();
					}else{
						if(appInd){
							var itmId = wbEncryptor.decrypt(app.getUrlParam("itmId"));
							changeWebviewDetail("item"+itmId,function(){
								back(3);
							});
						}else{
							back(3);
						}
					}
				});
			});
		}
        
		$scope.isAnwser = function(orderid,ansid){
			var res = $scope.result[orderid].interaction[0].response;
			if(res != undefined){
				for (var int = 0; int < res.length; int++) {
					if(res[int].text==ansid){
						return true;
					}
				}
			}
			return false;
		}
		
		$scope.isOk = function(condition, orderid){
			var res = $scope.result[orderid].interaction[0].response;
			if(res != undefined){
				for (var int = 0; int < res.length; int++) {
					if(res[int].text==condition){
						return true;
					}
				}
			}
			return false;
		}
		
	}]);
	
	//作业提交第一步
	modModule.controller('assSub1Controller', ['$scope', 'Store', 'alertService', function($scope, Store, alertService) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var resId = app.getUrlParam("resId") || Store.get("resId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		$scope.itmId = app.getUrlParam("itmId") || Store.get("itmId");
		Store.set("tkhId", tkhId);
		Store.set("resId", resId);
		Store.set("modId", modId);
		Store.set("itmId", $scope.itmId);
		$scope.file_total = Store.get("file_total");
		$scope.getContent = function(){
			clicked('ass_content.html', true);
		}
		
		$scope.next = function(){ 
			var reg = /^[1-9]\d*$/;
			if(!reg.test($("#file_total").val())){
				alertService.add('warning', 'mod_positive_integer_error', 2000);
				return;
			}
			Store.set("file_total", $("#file_total").val());
			clicked('ass_submit2.html', true);
		}
	}]);
	
	//作业提交第二步
	modModule.controller('assSub2Controller', ['$scope', 'Store', 'alertService', function($scope, Store, alertService) {
		$scope.itmId = app.getUrlParam("itmId") || Store.get("itmId");
		$scope.user = {};
		$scope.data = {};
		var arrayObj = new Array();
		for(var i=0;i<Store.get("file_total");i++){
			arrayObj.push(i+1);
		}
		$scope.list = arrayObj;
		$scope.next = function(){
			var desc_list = "";
			var file_detail_str = "";
			for(var i=0;i<$("input[type='file']").length;i++){
				var file_name = $("input[type='file']").eq(i).val();
				var file_desc = $("textarea[name='comment']").eq(i).val();
				if(file_name == ""){
					continue;
				}
				if(file_name == ""){
					alertService.add('warning', 'mod_upload_file_error', 2000);
					return;
				}
				if(file_name != decodeURI(file_name)){
					alertService.add('warning', 'upload_file_name_warning', 2000);
					return;
				}
				
				if(file_desc == ""){
//					alertService.add('warning', 'mod_upload_desc_error', 2000);
//					return;
					file_desc ="[||]";
				}
				desc_list += $("textarea[name='comment']").eq(i).val() + "~";
				file_detail_str += file_name.substring(file_name.lastIndexOf("\\")+1, file_name.length)
						 + "[|]" + file_desc + "[|]";
			}
			if(file_detail_str == ""){
				alertService.add('warning', 'mod_upload_file_error', 2000);
				return;
			}
			
			$("input[name='tkh_id']").val(Store.get("tkhId"));
			$("input[name='mod_id']").val(Store.get("modId"));
			$("input[name='step']").val(5);
			
			
			//显示进度条
			var template = '<div class="header-overlay" style="display:block;"><div class="jiazai-mobile"><div class="jiazai-wbload"><span id="upload-progress-bar" style="width:0%;"></span></div></div></div>';
			var body = document.body;
			var progressEl = document.createElement("div");
			progressEl.innerHTML = template;
			body.appendChild(progressEl);
			
			//提交作业文件
			$("#assForm").ajaxSubmit({
				url : serverHost + '/app/module/submitAss',
				type : 'post',
				success : function(){
					Store.set("desc_list", desc_list.substring(0, desc_list.length-1));
					Store.set("file_detail_str", file_detail_str.substring(0, file_detail_str.length-3));
					
					window.setTimeout(function(){//延时取消，让用户看到效果
	   					body.removeChild(progressEl);
	   					clicked('ass_submit3.html', true);
	   				},1000);
				},
				uploadProgress: function(event, position, total, percentComplete) {
					//设置进度
	   				var progressBar = document.getElementById("upload-progress-bar");
	   				progressBar.style.width = percentComplete+"%";
				}
			});
		}
	}]);
	
	//作业提交第三步
	modModule.controller('assSub3Controller', ['$scope', 'Store', function($scope, Store) {
		$scope.itmId = app.getUrlParam("itmId") || Store.get("itmId");
		$scope.desc_list = Store.get("desc_list").split("~");
		$scope.finish = function(){
			$("input[name='tkh_id']").val(Store.get("tkhId"));
			$("input[name='mod_id']").val(Store.get("modId"));
			$("input[name='step']").val(6);
			$("input[name='file_detail_str']").val(Store.get("file_detail_str"));
			$("#assForm").ajaxSubmit({
				url : serverHost + '/app/module/submitAss',
				type : 'post',
				success : function(){
					Store.set("file_total", "");
					Store.set("desc_list", "");
					if(appInd){
						changeWebviewDetail('item' + wbEncryptor.decrypt(plus.storage.getItem("nowItmId")), function(){
							back(3);
						});
					} else {
						back(3);
					}
				}
			});
		}
	}]);
	
	//作业成绩
	modModule.controller('assScoreController', ['$scope', 'Store','Ajax','alertService',  function($scope, Store, Ajax,alertService) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var resId = app.getUrlParam("resId") || Store.get("resId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		$scope.itmId = app.getUrlParam("itmId") || Store.get("itmId");
		Store.set("tkhId", tkhId);
		Store.set("resId", resId);
		Store.set("modId", modId);
		Store.set("itmId", $scope.itmId);
		var cur_time;
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId
		};
		$scope.ass = {};
		$scope.serverHost = serverHost;
		Ajax.post('/app/module/getAssScore', params, function(data){
			$scope.ass = data.hashMap.student[0].assignment[0];
			$scope.file_list = $scope.ass.body[0].uploadPath[0].file;
			$scope.frist_file = $scope.file_list[0]
			$scope.last_file = $scope.file_list[$scope.file_list.length - 1];
		});
		
		$scope.open = function(url){
			alertService.add('warning', 'label_assscore_openfile_warning', 2000);
		}
		
		$scope.getContent = function(){
			clicked('ass_content.html', true);
		}
	}]);
	
	//作业内容
	modModule.controller('assContentController', ['$scope', '$sce', 'Store', 'Ajax', 'alertService','moduleService', 'wizVideo',
			function($scope, $sce, Store, Ajax, alertService,moduleService,wizVideo) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var resId = app.getUrlParam("resId") || Store.get("resId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		var userEntId = app.getUrlParam("userEntId") || Store.get("userEntId");
		
		viewUserId = userEntId;
	    viewCourseId = resId;
		courseNum = modId;
		
		var token = Store.get("token");
		var cur_time;
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId
		};
		$scope.data = {};
		$scope.ass = {};
		$scope.serverHost = serverHost;
		
		Ajax.post('/app/module/getAssContent', params, function(data){
			$scope.data = data.hashMap;
			$scope.ass = data.hashMap.header[0];
			var type = $scope.ass.source[0].type;
			var src = $scope.ass.source[0].text;
			
			var fileExt = src.substring(src.lastIndexOf(".")+1, src.length);
			if(fileExt.indexOf("?") > -1){
				fileExt = fileExt.substring(0, fileExt.lastIndexOf("?"));
			}
			
			var isDoc = function(fileExt){
				if(!fileExt){
					return false;
				}
				fileExt = fileExt.substring(0,3).toLowerCase();
				
				if(fileExt == "doc"||fileExt == "xls"||fileExt == "ppt" || fileExt == "pdf"){
					return true;
				}
				return false;
			}
			
			if(src.indexOf("http://") < 0 && src.indexOf("https://") < 0){
				
				src = 'resource/' + modId + '/' + src;
				if(type != 'FILE' || !isDoc(fileExt)){
					src = serverHost + "/" + src;
				}
			}
			
			if(type == 'FILE'){
				if(isDoc(fileExt)){
					$scope.showDoc = true;
					Ajax.post("/app/idv/previewUrl",{filePath:src},function(data){
						$scope.previewUrl = data;
					});
				} else if(fileExt == 'mp4'){
					$scope.video = true;
					src = src + "?token="+token;
					if(appInd){
						unlockOrientation();
					}
					
					window.onload = function (){				
						wizVideo.setWizVideo("video");
					}
					
				} else if(fileExt == 'jpg' || fileExt == 'png' || fileExt == 'gif' || fileExt == 'jpeg'){
					$scope.img = true;
					src = src + "?token="+token;
				} else {
					$scope.showIfm = true;
				}
				$scope.src = $sce.trustAsResourceUrl(src);
			} else if(type == 'URL'){
				if(isDoc(fileExt)){
					$scope.showDoc = true;
					Ajax.post("/app/idv/previewUrl",{filePath:src},function(data){
						$scope.previewUrl = data;
					});
				} else if(fileExt == 'mp4'){
					$scope.video = true;
					if(appInd){
						unlockOrientation();
					}
					
					window.onload = function (){				
						wizVideo.setWizVideo("video");
					}
					
				}else if(fileExt == 'jpg' || fileExt == 'png' || fileExt == 'gif' || fileExt == 'jpeg'){
					$scope.img = true;
				}else {
					$scope.showDoc = true;
					$scope.previewUrl = $sce.trustAsResourceUrl(src);
				}
				src = src.replace(/&amp;/g, "&");
				$scope.src = $sce.trustAsResourceUrl(src);
			}else if(type == 'ZIPFILE'){ //作业 zip 类型
				$scope.showDoc = true;
				$scope.previewUrl = $sce.trustAsResourceUrl(src);
			}
			cur_time = new Date();
		});
		
		$scope.leave = function(){
			if(appInd){				    		
    			changeWebviewDetail(plus.webview.currentWebview().opener().id,null,true);
	    	}
			moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time);
		}
		
		if(appInd){
			$(function(){
				changeBackButtion(function(){
					moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time);
				});
			});
		}
	}]);
	
	//教材内容
	modModule.controller('rdgContentController', ['$scope', '$sce', 'Store','Ajax', 'alertService','moduleService','wizVideo',
			function($scope, $sce, Store, Ajax, alertService,moduleService,wizVideo) {
		
		$scope.hideHeader = false;
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var resId = app.getUrlParam("resId") || Store.get("resId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		var userEntId = app.getUrlParam("userEntId") || Store.get("userEntId");
		
		viewUserId = userEntId;
	    viewCourseId = resId;
		courseNum = modId;
		
		var token = Store.get("token");
		
		var cur_time;
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId
		};
		$scope.data = {};
		$scope.rdg = {};
		$scope.win_height=$("#testdiv").height()*0.955;
		$scope.win_width=$("#testdiv").width();
		var bIsAndroid = navigator.userAgent.toLowerCase().match(/android/i) == "android"; 
		if(bIsAndroid){
			$scope.win_height='100%';
			$scope.win_width='100%';
		}else{
			
			var is5= navigator.userAgent.toLowerCase().match(/applewebkit\/5/)=="applewebkit/5";
			if(is5){
				$scope.win_height=$("#testdiv").height()*1.089;
				$scope.win_width=$("#testdiv").width()*1.081;
			}else{
				$scope.win_height=$("#testdiv").height()*0.955;
				$scope.win_width=$("#testdiv").width();
			}		
		}
		Ajax.post('/app/module/getRdgContent', params, function(data){
			$scope.data = data.hashMap;
			$scope.rdg = data.hashMap.header[0];
			var type = $scope.rdg.source[0].type;
			var src = $scope.rdg.source[0].text;
			var fileExt = src.substring(src.lastIndexOf(".")+1, src.length);
			if(fileExt.indexOf("?") > -1){
				fileExt = fileExt.substring(0, fileExt.lastIndexOf("?"));
			}
			var isDoc = function(fileExt){
				if(!fileExt){
					return false;
				}
				fileExt = fileExt.substring(0,3).toLowerCase();
				
				if(fileExt == "doc"||fileExt == "xls"||fileExt == "ppt" || fileExt == "pdf" || fileExt == "txt"){
					return true;
				}
				return false;
			}
			
			if(src.indexOf("http://") < 0 && src.indexOf("https://") < 0){
				
				src = 'resource/' + modId + '/' + src;
				if(type != 'FILE' || !isDoc(fileExt)){
					src = serverHost + "/" + src;
				}
			}
			
			if(type == 'FILE'){
				if(isDoc(fileExt)){
					$scope.showDoc = true;
					Ajax.post("/app/idv/previewUrl",{filePath:src},function(data){
						$scope.previewUrl = data;
					});
				} else if(fileExt == 'mp4'){
					$scope.video = true;
					src = src + "?token="+token;
					if(appInd){
						unlockOrientation();
					}
					
					window.onload = function (){				
						wizVideo.setWizVideo("video");
					}
					
				}else if(fileExt == 'jpg' || fileExt == 'png' || fileExt == 'gif' || fileExt == 'jpeg'){
					$scope.img = true;
				}else {
					$scope.showIfm = true;
				}
				$scope.src = $sce.trustAsResourceUrl(src);
			} else if(type == 'URL'){
				if(isDoc(fileExt)){
					$scope.showDoc = true;
					Ajax.post("/app/idv/previewUrl",{filePath:src},function(data){
						$scope.previewUrl = data;
					});
				} else if(fileExt == 'mp4'){
					$scope.video = true;
					if(appInd){
						unlockOrientation();
					}
					
					window.onload = function (){				
						wizVideo.setWizVideo("video");
					}
					
				}else if(fileExt == 'jpg' || fileExt == 'png' || fileExt == 'gif' || fileExt == 'jpeg'){
					$scope.img = true;
				}else {
					$scope.showDoc = true;
					$scope.previewUrl = $sce.trustAsResourceUrl(src);
				}
				src = src.replace(/&amp;/g, "&");
				$scope.src = $sce.trustAsResourceUrl(src);
			}
			cur_time = new Date();
		});
		
		$scope.leave = function(){
			if(appInd){	
    			changeWebviewDetail(plus.webview.currentWebview().opener().id,null,true);
	    	}
			if(app.getUrlParam("previewInd")){
				back();
			} else {
				moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time,true);
			}
		}
		
		if(appInd){
			$(function(){
				changeBackButtion(function(){
					if(app.getUrlParam("previewInd")){
						back();
					} else {
						moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time);
					}
				});
			});
		}
		
	}]);
	
	//参考内容
	modModule.controller('refContentController', ['$scope', 'Store','Ajax', 'alertService','moduleService',
			function($scope, Store, Ajax, alertService,moduleService) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var resId = app.getUrlParam("resId") || Store.get("resId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		var cur_time;
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId
		};
		$scope.data = {};
		$scope.ref_list = {};
		
		Ajax.post('/app/module/getRefContent', params, function(data){
			$scope.data = data.hashMap;
			$scope.ref_list = data.hashMap.reference_list[0];
			cur_time = new Date();
		});
		
		$scope.open = function(ref_url){
			openOtherSiteUrl(ref_url);
		}
		
		$scope.leave = function(){
			if(appInd){				    		
    			changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
    			},true);
	    	}
			moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time);
		}
		
		if(appInd){
			$(function(){
				changeBackButtion(function(){
					moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time);
				});
			});
		}
	}]);
	
	//视频点播内容
	modModule.controller('vodContentController', ['$scope', '$sce', 'Store', 'Ajax', 'alertService','moduleService','wizVideo',
			function($scope, $sce, Store, Ajax, alertService,moduleService,wizVideo) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var resId = app.getUrlParam("resId") || Store.get("resId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		var userEntId = app.getUrlParam("userEntId") || Store.get("userEntId");
		
		viewUserId = userEntId;
	    viewCourseId = resId;
		courseNum = modId;
		
		var token = Store.get("token");
		
		var cur_time;
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			mod_id : modId
		};
		$scope.data = {};
		$scope.rdg = {};
		
		Ajax.post('/app/module/getRdgContent', params, function(data){
			$scope.data = data.hashMap;
			$scope.vod = data.hashMap.header[0];
			var src = $scope.vod.source[0].text;
			if($scope.vod.source[0].type == 'URL'){
				src = $scope.vod.source[0].text;
			} else if($scope.vod.source[0].type.indexOf('ONLINEVIDEO_') != -1) {
				src = $scope.vod.src[0].link;
				$("#video").remove();
				$("section").append('<iframe id="api_video" sandbox="allow-scripts allow-same-origin" class="video-js vjs-default-skin vjs-big-play-centered" src="'+src+'" width="100%" height="320px"/>')
			} else{
				src = serverHost + '/resource/' + modId + '/' + src + "?token="+token;
			}
			$scope.type = $scope.vod.source[0].type;
			src = src.replace(/&amp;/g, "&");
			$scope.src = $sce.trustAsResourceUrl(src);
			cur_time = new Date();
		});
		$scope.leave = function(){
			if(appInd){
    			changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
    			},true);
	    	}
			if(app.getUrlParam("previewInd")){
				back();
			} else {
				moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time,'','VOD');
			}
		};
		if(appInd){
			unlockOrientation();
			$(function(){
				changeBackButtion(function(){
					if(app.getUrlParam("previewInd")){
						back();
					} else {
						moduleService.saveRecord(alertService, $scope.data, modId, resId, tkhId, cur_time,'','VOD');
					}
				});
			});
		}
		
		window.onload = function (){
			wizVideo.setWizVideo("video");
		}
		
	}]);
	
	//aicc课件学习报告
	modModule.controller('aiccReportController', ['$scope', 'Store', 'Ajax', function($scope, Store, Ajax) {
		var tkhId = app.getUrlParam("tkhId") || Store.get("tkhId");
		var resId = app.getUrlParam("resId") || Store.get("resId");
		var modId = app.getUrlParam("modId") || Store.get("modId");
		var params = {
			isMobile : true,  
			tkh_id : tkhId,
			res_id : resId,
			mod_id : modId
		};
		$scope.aicc = {};
		
		Ajax.post('/app/module/getAiccReport', params, function(data){
			$scope.aicc = data.hashMap.aicc_au_report[0];
		});
		
	}]);
	
});

function show(){
	$(".header-datika").slideToggle();
	$(".header-overlay").toggleClass("show");
}

function selected(thisObj){
	$(thisObj).parents("dl").find("p").removeClass("diaocha-cur"); 
	$(thisObj).parents("dl").find("input:checked").parent("p").addClass("diaocha-cur");
	$(".kaoshi-dati-info a").removeClass("kaoshi-dati-ok").addClass("kaoshi-dati-no");
	count_test();
}

function toggleCur(thisObj){
	$(thisObj).addClass("cur");
	$(thisObj).focus(function(){
		$(thisObj).addClass("cur");
	}).blur(function(){
		$(thisObj).removeClass("cur");
		count_test();
	})
}

function toggle_mt(thisObj){
    $(thisObj).removeClass("cur").next().find("ul").toggle();
    
    $(thisObj).parents("dl").children().find(".select-on").find("input").each(function(){
    	var select_data = $(this).val();
    	$(thisObj).next().find("ul li").each(function(){
    		if($(this).find(".diaocha-term-select").attr("data") == select_data.trim()){
				$(this).find(".diaocha-term").addClass("diaocha-cur");	
			}
    	});
		
	})
    
    $(thisObj).next().find("ul li").click(function(){
    	var flag = false; //判断是否已经选择过
    	var select_data = $(this).find(".diaocha-term-select").attr("data");
    	
		$(thisObj).parents("dd").siblings().find(".select-on").find("input").each(function(){
			if(flag) return;
			/*if($(this).val() != undefined && $(this).val() != "" && $(this).val() == select_data){
				alertObj.add('danger', 'selected_prompt', 2000);
				tstDetailConScope.$apply();
				flag = true;
				return;
			}*/
		});	
		if(flag) return;
		
        $(this).removeClass('cur');
        
    	$(this).find(".diaocha-term").addClass("diaocha-cur");
        $(thisObj).find("span").html(filter('translate')('selected_source')+$(this).find(".diaocha-term-select").html());
         //设置选项值
    	$(thisObj).find("input").val($(this).find(".diaocha-term-select").attr("data"));
	    
	    $(thisObj).parents("dl").children().find(".select-on").find("input").each(function(){
	    	select_data = $(this).val();
	    	$(thisObj).next().find("ul li").each(function(){
	    		if($(this).find(".diaocha-term-select").attr("data") == select_data.trim()){
					$(this).find(".diaocha-term").addClass("diaocha-cur");	
				}else{
					$(this).find(".diaocha-term").removeClass("diaocha-cur");
				}
	    	});
		
		})
        
        $(thisObj).removeClass("cur").addClass("curtent");
        $(this).addClass('cur').parent().hide();
        
        count_test(); //计算已答题目
    });
}


function count_test(){
	var type = "MC";
	var answer = 0;
	var noanswer = 0;
	$(".content-3 dl").each(function(){
		type = $(this).attr("type");
		if(type == "MC" || type == "TF"){  //选择题
			if($(this).find("input:checked").length > 0){
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-no").addClass("kaoshi-dati-ok");
				answer++;
			} else {
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-ok").addClass("kaoshi-dati-no");
				noanswer++;
			}
		}else if(type == "FB"){ //填空题
			var flag = false;
			$(this).find("input").each(function(){
				if($(this).val() == "" || $(this).val() == undefined){
					flag = true;
				}
			})
			if(!flag){
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-no").addClass("kaoshi-dati-ok");
				answer++;
			}else{
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-ok").addClass("kaoshi-dati-no");
				noanswer++;
			}
		}else if(type == "ES"){ //问答题
			var flag = false;
			$(this).find("textarea").each(function(){
				if($(this).val() == "" || $(this).val() == undefined){
					flag = true;
				}
			})
			if(!flag){
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-no").addClass("kaoshi-dati-ok");
				answer++;
			}else{
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-ok").addClass("kaoshi-dati-no");
				noanswer++;
			}
		}else if(type == "MT"){ //配对题
			var flag = false;
			$(this).find(".select-on").find("input").each(function(){
				if($(this).val() == "" || $(this).val() == undefined){
					flag = true;
				}
			})
			if(!flag){
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-no").addClass("kaoshi-dati-ok");
				answer++;
			}else{
				$("#que_" + $(this).attr("id")).removeClass("kaoshi-dati-ok").addClass("kaoshi-dati-no");
				noanswer++;
			}
		}
		
	});
	$("#answer").html(answer);
	$("#noanswer").html(noanswer);
}

function goToQuestion(num){
    $("html,body").animate({scrollTop:$("#"+num+"").offset().top-46},1000);
}
