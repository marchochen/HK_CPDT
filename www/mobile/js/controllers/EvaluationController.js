$.include([], '../../');
$(function(){
	var evaluationModule = angular.module('evaluation', ['globalCwn']);	
	evaluationModule.controller('evaluationController', ['$scope', '$window', 'Store', 'Loader', function($scope, $window, Store, Loader) {
		var url = '/app/personal/evaluationList';
		var paramsF = {	
			isMobile : true,
			status : 0,
			sortorder : 'asc'
		};	
		var paramsC = {	
			isMobile : true,
		    status : 1,
		    sortorder : 'asc'
		};		
		$scope.evaListF = new Loader(url,paramsF);		
		$scope.evaListC = new Loader(url,paramsC);
		$scope.detail = function(eva){
			if(eva.mov != undefined && eva.mov.mov_status != undefined){
				return;
			}
			Store.set('evaluation', eva.res_id);
			clicked('detail.html', true);
		};
		$scope.show = {}; //控制选项卡显示
		$scope.show.evaWaitAnwser = false;
		$scope.show.evaAnwsered = false;
		var showType = app.getUrlParam('show');
		$scope.backvalue = false;
		if (showType) {
			$scope.backvalue = true;
			switch(showType){
			case 'evaWaitAnwser' : 
				$scope.show.evaWaitAnwser = true;
				$scope.show.evaAnwsered = false;
				break;
			case 'evaAnwsered' : 
				$scope.show.evaWaitAnwser = false;
				$scope.show.evaAnwsered = true;
				break;
			}
		}else{//默认待回答
			$scope.show.evaWaitAnwser = true;
		}
 	}]);

	evaluationModule.controller('detailController', ['$scope', '$window', '$timeout','Ajax', 'Store', 'alertService', 'modalService', 'dialogService' ,function($scope, $window, $timeout, Ajax, Store, alertService, modalService ,dialogService){		
		var personalId = Store.get('loginUser');
		var homeUrl = '/app/personal/home/json/' + personalId;
		var url = '/app/evaluation/detailJson';
		var itmId = app.getUrlParam('itmId');
		var itemUrl = '/app/course/detailJson/' + itmId;
		
		var modId = app.getUrlParam('evaluation') || Store.get('evaluation') || app.getUrlParam('modId') || Store.get('modId');
		
		var from = app.getUrlParam('from');//页面时公用的，如果 from == course,则是从课程模块页面链接过来的，否则是从调查问卷模块过来的
		
		var params = {
			mod_id : modId
		}	
		var starTime;
		$scope.itmTitle = decodeURIComponent(app.getUrlParam('itmTitle') == null? null : app.getUrlParam('itmTitle').replace(/\[\|\]/g,"%"));
		if(itmId != null && itmId != undefined){
			Ajax.post(itemUrl, {}, function(item_data){
				$scope.itmTitle = item_data.item.itm_title;
			});
		}
		
		$scope.user = {};
		$scope.evaluation = {};
		$scope.serverHost = serverHost;		
		$scope.anwsered = 0;
		$scope.noanwser = 0;
		Ajax.post(homeUrl, {}, function(data){
			$scope.user = data.regUser;
		});
		Ajax.post(url, params, function(data){
			$scope.evaluation = data.result.public_eval;			
			$scope.noanwser = data.result.public_eval.total;			
			starTime = data.startTime;
		});
		$scope.select = function(queId,optId){
			for(var i = 0; i < $scope.evaluation.questions.length; i++){
				if($scope.evaluation.questions[i].id == queId){
					for(var j = 0; j < $scope.evaluation.questions[i].options.length; j++){
						if($scope.evaluation.questions[i].type == 'SINGLE'){
							$scope.evaluation.questions[i].options[j].select = false;						
						}
						if($scope.evaluation.questions[i].options[j].id == optId){
							$scope.evaluation.questions[i].options[j].select = true;
							if($scope.evaluation.questions[i].type == 'SINGLE'){								
								$scope.evaluation.questions[i].value = optId;
							}else{
								if(!$scope.evaluation.questions[i].value || $scope.evaluation.questions[i].value == ''){
									$scope.evaluation.questions[i].value = optId;
								}else if($scope.evaluation.questions[i].value.indexOf(optId)!=-1){
									$scope.evaluation.questions[i].options[j].select = false;
									if($scope.evaluation.questions[i].value.indexOf(optId) == 0){
										$scope.evaluation.questions[i].value = $scope.evaluation.questions[i].value.substring(optId.length+1);
										
									}else{										
										$scope.evaluation.questions[i].value = $scope.evaluation.questions[i].value.substring(0,$scope.evaluation.questions[i].value.indexOf(optId)-1) + $scope.evaluation.questions[i].value.substring( $scope.evaluation.questions[i].value.indexOf(optId) + optId.length);										
									}
								}else{
									$scope.evaluation.questions[i].value = $scope.evaluation.questions[i].value + '~' + optId;
								}
							}																		
						}
					}
					break;
				}
			}
		};
		$scope.submit = function(){
			var que = '';
			var ans = '';			
			var flag = false; 
			var subUrl = '/app/evaluation/subEvaluation';
			for(var i = 0; i < $scope.evaluation.questions.length; i++){
				if($scope.evaluation.questions[i].id){
					flag = true;
					que += $scope.evaluation.questions[i].id + "[|]";
					if($scope.evaluation.questions[i].value == undefined){
                            $scope.evaluation.questions[i].value = '[||]';
                        }
		     	   	ans += $scope.evaluation.questions[i].value + "[|]";
				}
			}			
			/*if(que == '' || ans == ''){
				alertService.add('warning', 'eva_tip_submit_before', 2000);
				return;
			}*/
			if(que != ''){
		     	que = que.substring(0,que.length-3);
		    }
		    if(ans != ''){
		     	ans = ans.substring(0,ans.length-3);
		    }
            
            var noanwser = false;
            if($scope.noanwser > 0){
            	noanwser = true;
            }
		    modalService.modal(function($scope, $modalInstance) {
		    	if(noanwser){
		    		$scope.modalText = 'mod_tst_submit_notice';
		    	}else{
		    		$scope.modalText = 'global_sure';
		    	}
				$scope.modalOk = function() {
					ans = encodeURIComponent(ans);
					var subParams = {
		    			mod_id : modId,
		    			que_id_lst : que,
		    			que_anwser_option_lst : ans,
		    			startTime : starTime
				    }		
				    Ajax.post(subUrl, subParams, function(){
				    	//更新首页任务数量
			  			 if(appInd){
				                webview = plus.webview.getWebviewById("home");
				                var wvs=plus.webview.all();
								for(var i = wvs.length-1;i >= 0; i--){
									if(wvs[i].id == "home"){
										webview = wvs[i];
										break;
									}
								}
								if(webview == undefined){
									webview = plus.webview.create("views/index.html","home",{scrollIndicator:'none',scalable:false});
								}
								webview.evalJS("getStatistics()");
						}
			  			 
				    	dialogService.modal("evaluation_successs","w",function(){
				    		
				    		if(appInd){				    		
				    			changeWebviewDetail(plus.webview.currentWebview().opener().id,function(){
					    			Store.set('evaluation', "");
			    					Store.set('modId', "");
			    					if(from == 'course'){//页面时公用的，如果 from == course,则是从课程模块页面链接过来的，否则是从调查问卷模块过来的
			    						back();
			    					}else{
			    						window.location.href="./evaluation.html?show=evaAnwsered";
			    					}
				    			});
					    	} else {
				    			alertService.closeAllAlerts();	    	
				    			Store.set('evaluation', "");
		    					Store.set('modId', "");
		    					if(from == 'course'){
		    						back();
		    					}else{
		    						clicked('./evaluation.html?show=evaAnwsered',true);
		    					}
			    			}
				    		
				    	});
		    		});
					$modalInstance.dismiss('cancel');
				};
				$scope.modalCancel = function() {
					$modalInstance.dismiss('cancel');
				};
			});
		    
		};
		
		$scope.$watch('evaluation.questions', function(){
			if($scope.evaluation.questions){
				var anwserCount = 0;
				for(var i = 0; i < $scope.evaluation.questions.length; i++){
					if($scope.evaluation.questions[i].value){					
						anwserCount ++;							
					}
				}
				$scope.noanwser = $scope.noanwser + $scope.anwsered - anwserCount;
				$scope.anwsered = anwserCount;
			}			
		},true);
	}]);

});
