$.include([
	'lib/angular/angular-touch.min.js',
    'js/directives/SNSDirectives.js',
	'js/services/UserService.js',
	'js/services/SNSService.js', 
	'lib/easypiechart/angular.easypiechart.min.js',
	'js/services/UploadService.js',
    'js/json2.min.js'
], '../../');

var uploadUrl;
var alertObj;

$(function(){
	var knowModule = angular.module('know', ['globalCwn', 'userService', 'easypiechart']);
	knowModule.filter("queType", ['$filter', function($filter){
		return function(input){
			if(!input || input == '') return '';
			if(input == 'UNSOLVED' || input == 'SOLVED') return $filter('translate')('know_type_'+input);
			return input;
		};
	}]);
	knowModule.filter('percentFormat', function(){
		return function(input){					
			if(!input || input == '') return '0%';
			var msg = input + '';
			if(msg.indexOf('%') == -1){
				msg +='%';
			}
			return msg;
		};
	});
	
	knowModule.factory('KnowService',function($http,Store) {
		return {
			getQuestionTypes : function() {
				var questionTypes = {
					'ALL' : {'label':'text_all','value':'all'},
					'UNSOLVED' : {'label':'know_type_UNSOLVED','cla':'box-num-cont-pink','value':'UNSOLVED'},
					'SOLVED' : {'label':'know_type_SOLVED','cla':'box-num-cont-yellow','value':'SOLVED'},
					
					'FAQ' : {'label':'know_faq','cla':'box-num-cont-bluer','value':'FAQ'}
				};
				return questionTypes;
			}
		};
	});
	
	knowModule.directive("questionCatalog",['$filter','Ajax',function($filter,Ajax){
	   var html = 
		 '<div class="guide-menu clearfix">'+
		 '  <ul class="guide-menu-list-1 panel-list-cont-13">'+
		 '      <li ng-repeat="item in kcaIdOnes" ng-click="checkFirst(item.kca_id)" ng-class="{\'select\' :item.select}">'+
		 '		<a href="javascript:;"  title="">{{item.kca_title}}<em></em></a>'+
		 '		</li>'+
		 '  </ul> '+
		 '  '+
		 '  <div class="guide-menu-cont">'+
		 '       <ul class="guide-menu-list-2" >'+
		 '      	 <li ng-class="{\'noline\' : $index == kcaIdTwos.length-1,\'cur\' :item.select}" ng-repeat="item in kcaIdTwos" ng-click="checkSecond(item.kca_id)"><a href="javascript:;" title="">{{item.kca_title}}</a></li>'+
		 '       </ul>'+
		 '  </div>  '+
		 '</div> ';
		return {
	        restrict: 'AE',
	        template: html,
	        scope : {
	        	selectCallback : '=',
	        	showAllText : '=showAllText'
	        },
	        replace: true,
	        link: function(scope, element, attrs) {
	    		var cat1Url = '/app/know/allKnow/kca/CATALOG/0';
	    		var cat2Url = '/app/know/allKnow/kca/NORMAL/';
	    		scope.kcaIdOnes = [];
	    		scope.kcaIdTwos = [];
	    		
	    		Ajax.post(cat1Url, {}, function(data){//加载第一级目录
	    			scope.kcaIdOnes = data.kca;
	    			if(scope.kcaIdOnes.length > 0 && scope.$parent.iswrite == true){
	    				scope.checkFirst(scope.kcaIdOnes[0].kca_id);
	    			}
	    			if(scope.showAllText){
	    				scope.kcaIdOnes.unshift({
	                        kca_title : $filter('translate')('text_all'),
	                        kca_id: 0,
	                        select : true
	                    });
	    			}
	    		});
	    		
	    		scope.checkFirst = function(kca_id){
	    			
	    			angular.forEach(scope.kcaIdOnes, function(val, key){
                        if(kca_id == val.kca_id){
                            val.select = true;
                            val.cType = 'first';
                            scope.selectCallback && scope.selectCallback(val);
                        } else {
                            val.select = false;
                        }
                    });
	    			
	    			if(kca_id == 0){
	                    element.find(".guide-menu-list-1").removeClass("guide-menu-click");
	                    element.find(".guide-menu-list-1").siblings(".guide-menu-cont").hide();
	                    return;
	                }
	    			
	    			Ajax.post(cat2Url + kca_id, {}, function(data){
	    				scope.kcaIdTwos = data.kca;	            		
	                	if(scope.kcaIdTwos && scope.kcaIdTwos.length>0){
	                	    element.find(".guide-menu-list-1").addClass("guide-menu-click");
	                	    element.find(".guide-menu-list-1").siblings(".guide-menu-cont").show();
	                	    element.siblings(".header-guide-lei").children(".header-guide-prev").show();
	                	    
	                	    var setProperHeight = function(){
	                	    	var h1 = $(".guide-menu-list-1").height();
	                	    	var h2 = scope.kcaIdTwos.length * 39;
	                	    	var h;
	                	    	if(h1 > h2){
	                	    		h = h1;
	                	    	}else{
	                	    		h = h2;
	                	    	}
	                	    	if(h > 215){
	                	    		h = 215;
	                	    	}
	                	    	$(".guide-menu-cont").css("height",h);
                	    		$(".guide-menu-list-1").css("height",h);
	                	    };
	                	    
	                	    setProperHeight();
	                	    
	                	} else {

	                	    element.find(".guide-menu-list-1").removeClass("guide-menu-click");

	                	    element.find(".guide-menu-list-1").siblings(".guide-menu-cont").hide();
	                	}
	    			});
	    		};
	    		scope.checkSecond = function(kca_id){
	    			angular.forEach(scope.kcaIdTwos, function(val, key){
                        if(kca_id == val.kca_id){
                            val.select = true;
                            val.cType = 'second';
                            scope.selectCallback && scope.selectCallback(val);
                        } else {
                            val.select = false;
                        }
                    });
	    		};
	        }
	    };
	}]);
	
	knowModule.controller('mainController', ['$scope', 'User', 'Ajax',function($scope, User, Ajax){
		var url = '/app/know/main/json';
		$scope.user = {};
		$scope.serverHost = serverHost;
		$scope.solvedNum = 0;
		$scope.unsolvedNum = 0;
		$scope.popularNum = 0;
		$scope.faqNum = 0;
		User.userInfo(function(data){
			$scope.user = data.regUser			
		})
		Ajax.post(url, {}, function(data){
			$scope.solvedNum = data.solvedNum;
			$scope.unsolvedNum = data.unsolvedNum;
			$scope.popularNum = data.popularNum;
			$scope.faqNum = data.faqNum;
		});
	}]);
	
	knowModule.controller('myAnwserController', ['$scope', 'Loader', 'KnowService',function($scope, Loader,KnowService){
		var url = '/app/know/myKnow/my_answer';
		$scope.questionTypes = KnowService.getQuestionTypes();
		$scope.knowList = new Loader(url, {},function(data){
			for(var i in data.items){
				var item = data.items[i];
				item.typeInfo = $scope.questionTypes[item.que_type];
			}
		});
	}]);
	
	knowModule.controller('myQuestionController', ['$scope', 'Loader', 'KnowService',function($scope, Loader,KnowService){
		var url = '/app/know/myKnow/my_question';
		
		$scope.questionTypes = KnowService.getQuestionTypes();
		
		$scope.knowList = new Loader(url, {},function(data){
			for(var i in data.items){
				var item = data.items[i];
				item.typeInfo = $scope.questionTypes[item.que_type];
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
	
	knowModule.controller('myHelpQuestionController', ['$scope', 'Loader', 'KnowService',function($scope, Loader,KnowService){
		var url = '/app/know/myKnow/my_know_help';
		
		$scope.questionTypes = KnowService.getQuestionTypes();
		
		$scope.knowList = new Loader(url, {},function(data){
			for(var i in data.items){
				var item = data.items[i];
				item.typeInfo = $scope.questionTypes[item.que_type];
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

	knowModule.controller('askKnowController', ['$scope', '$window', '$filter', 'Ajax', 'alertService', 'dialogService' , 'Loader' ,function($scope, $window, $filter, Ajax, alertService , dialogService ,Loader){

		var creditsUrl = "/app/personal/credits/json";		
		$scope.bounty = ""; //输入悬赏积分的双向绑定
		$scope.credits = 0;
        $scope.serverHost = serverHost;
        alertObj = alertService;
        $scope.uploadType = 'ALL';
        uploadUrl = serverHost + '/app/upload/mobile/KnowQuestion/';
        
		$scope.displayCat = $filter('translate')('know_ask_tip_no_select');
		$scope.iswrite = true;
		$scope.show = {
			write : true,
			bounty : false,
			entIds : false
		};
		$scope.know = {
			queTitle : '',
			queContent : '',
			kcaIdOne : 0,
			kcaIdTwo : 0,
			queBounty : 0
		};		
		$scope.showPage = function(index){
			$scope.show.write = false;
			$scope.show.bounty = false;
			$scope.show.entIds = false;
			if(index == 0) {
				$scope.show.write = true;
			} else if(index == 1){
				$scope.show.bounty = true;
			} else if(index == 2){
				$scope.show.entIds = true;
				$scope.findMemberList = new Loader('/app/user/getInstructors', {});
			} else {
				$scope.show.write = true;
			}
		};


        $scope.select = function(index){
            $scope.findMemberList.items[index].select = $scope.findMemberList.items[index].select ? false : true;
        };
        $scope.search = function(){
            var params = {
                searchContent : $scope.searchContent
            };
            $scope.findMemberList = new Loader('/app/user/getInstructors', params);
        };
        $scope.saveInstructor = function(){
        	var entIds = [];
        	var usernames = [];
        	angular.forEach($scope.findMemberList.items, function(val, i){
        		if(val.select) {
        			entIds.push(val.usr_ent_id);
        			usernames.push(val.usr_display_bil);
        		}
        	})
			$scope.know.que_ask_ent_ids = entIds.join(',');
        	$scope.know.instructor = usernames.join(',');
        	$scope.instructorSize = usernames.length;
			$scope.showPage();
        }
        
		$scope.selectOne = function(index){
			for(var i in $scope.kcaIdOnes){
				$scope.kcaIdOnes[i].select = false;
			}
			$scope.kcaIdOnes[index].select = true;
			Ajax.post(cat2Url + $scope.kcaIdOnes[index].kca_id, {}, function(data){
				$scope.kcaIdTwos = data.kca;
				$scope.kcaIdTwos.unshift({
					kca_title : $filter('translate')('know_ask_tip_null'),					
					kca_id : '0',
					select : true 
				});
			});
		};
		$scope.selectTwo = function(index){
			for(var i in $scope.kcaIdTwos){
				$scope.kcaIdTwos[i].select = false;
			}
			$scope.kcaIdTwos[index].select = true;
		};

		$scope.toggle = function(){
			toggleCat();
		}
		$scope.saveCat = function(){
			for(var i in $scope.kcaIdOnes){
				if($scope.kcaIdOnes[i].select){
					$scope.know.kcaIdOne = $scope.kcaIdOnes[i].kca_id;
					$scope.displayCat = $scope.kcaIdOnes[i].kca_title;
				}
			}
			for(var i in $scope.kcaIdTwos){
				if($scope.kcaIdTwos[i].select){
					$scope.know.kcaIdTwo = $scope.kcaIdTwos[i].kca_id;
					if( $scope.kcaIdTwos[i].kca_id != 0)
						$scope.displayCat = $scope.displayCat + ','+$scope.kcaIdTwos[i].kca_title;
				}
			}
			toggleCat();
		};
		
		function toggleCat(){		
			$('.header-kang').slideToggle();
			$('.header-overlay').toggleClass('show');
		}
		
		$scope.saveBounty = function(){
			if($scope.bounty > $scope.credits){
				alertService.add('warning', 'know_ask_tip_credits_size', 2000);
				return;
			}
			if(isNaN($scope.bounty) ||	$scope.bounty <= 0 || !/^[1-9]\d*$/.test($scope.bounty) && $scope.bounty != 0){
				alertService.add('warning', 'know_ask_tip_must_num', 2000);
				return;
			}
			$scope.know.queBounty = $scope.bounty;
			$scope.showPage();
		};
		//发布问题
		$scope.submitKnow = function(){
			if($scope.know.queTitle == ''){
				alertService.add('warning', 'know_ask_tip_title_not_null', 2000);
				return;
			}
			if(app.getChars($scope.know.queTitle)>140){
				alertService.add('warning', 'know_ask_tip_title_size', 2000);
				return;
			}
			if($scope.know.kcaIdOne == 0){
				alertService.add('warning', 'know_ask_must_select_cat', 2000);
				return;
			}
			if($scope.know.queContent.length > 500){
				alertService.add('warning', 'know_ask_tip_content_size', 2000);
				return;
			}
			/*if($scope.know.queBounty <= 0){
				alertService.add('warning', 'know_ask_tip_must_num', 2000);
				return;
			}*/
			var url = '/app/know/addKnowQuestionMobile';
			Ajax.post(url, $scope.know, function(data){
				if( data && data.status == 'success'){
					
					dialogService.modal('question_submit','o' , function(){
						clicked('myQuestion.html?addInd=true', true);
					});
				}
			});
		};
		Ajax.post(creditsUrl, {}, function(data){
			$scope.credits = data.total_credits;
		});
		
		var tempCaTitle = "";
		
		$scope.selectCatalogCallback = function(c){
			if("first" === c.cType){
				if(c.kca_id == 0){
					return;
				}
				$scope.know.kcaIdOne = c.kca_id;
				tempCaTitle = c.kca_title;
				$scope.displayCat = tempCaTitle;
			}else if("second" === c.cType){
				$scope.know.kcaIdTwo = c.kca_id;
				$scope.displayCat = tempCaTitle + ','+c.kca_title;
			}
		};
		
		$scope.checkType = function(type){
            $scope.uploadType = type; 
        }
		
	}]);

	knowModule.controller('detailController', ['$scope', '$window', '$timeout', 'Loader', 'Store', 'Ajax', 'alertService', 'modalService', 'dialogService' ,
		function($scope, $window, $timeout, Loader, Store, Ajax, alertService, modalService, dialogService){
		//解析地址栏参数 f访问地址格式detail.html?type=SOLVED&id=1;
		var type = app.getUrlParam('type') || Store.get('type');
		var id = app.getUrlParam('id') || Store.get('id');	
		var url = '/app/know/knowDetail/json/'+type+'/'+id;	
		var answerUrl = '/app/know/answers/'+id;
		var relevantKnowUrl = '/app/know/relevantKnow/mobile/'+id+'/'+type+'/'; 
		$scope.content = ''; //用来控制问题补充内容的额填写
		$scope.anwser = '';
		
		//上传图片
		$scope.uploadType = 'ALL';
        uploadUrl = serverHost + '/app/upload/mobile/KnowQuestion/';
        $scope.checkType = function(type){
        	$scope.uploadType = "Img";
        }
        alertObj = alertService;
		
		$scope.show = {
			detail : true,
			answers : false,
			questions : false,
			content : false,
			writeAnwser : false			
		};
		$scope.knowDetail = {};
		$scope.bestAnswer = {
			ansLike : {
				type : 'like',				
			}
		};
		$scope.good_rate = 0;
		$scope.not_good_rate = 0;
		$scope.optionGood = {
			animate : 1500,
			barColor : '#33cc33',
			trackColor : "#f4f4f4",
			scaleColor : false,
			lineWidth : 15,
			lineCap : 'circle',
			size : 80
		};
		$scope.optionBad = {
			animate : 1500,
			barColor : '#0ad0ed',
			trackColor : "#f4f4f4",
			scaleColor : false,
			lineWidth : 15,
			lineCap : 'circle',
			size : 80		
		};
		$scope.loginUserId = Store.get('loginUser');
		$scope.serverHost = serverHost;	
		$scope.relAnswers = {};
		$scope.relQuestions = {};
		$scope.answers = new Loader();
		$scope.questions = new Loader();
		//显示隐藏控制
		$scope.showPage = function(key){
			if(key == "writeAnwser"){
            	uploadUrl = serverHost + '/app/upload/mobile/Know/';
            	$scope.uploadType = "Img";
            }else{
            	uploadUrl = serverHost + '/app/upload/mobile/KnowQuestion/';
            }
			for(var i in $scope.show){
				if($scope.show[i] && i=='answers'){
					$scope.relAnswers = new Loader(answerUrl, {pageSize:2}, function(data){
						for(var i in data.items){
							data.items[i].ansLike = {
								type : 'like',
								count : data.items[i].snsCount.s_cnt_like_count,
								flag : data.items[i].is_user_like,
								id : data.items[i].ans_id,
								module: 'Answer',
								tkhId : 0
							}
							//是否是指定回答的人
							data.items[i].is_specify = isSpecify($scope.specifyUsers,data.items[i]);
						}
					});
					$scope.relAnswers.loaderMore(1);
				}
				$scope.show[i] = false;
			}
			$scope.show[key] = true;
			if ( key == 'content' ) {
				$scope.content = $scope.knowDetail.que_content;
			}
			if( key == 'answers'){
				$scope.answers = new Loader(answerUrl, {}, function(data){
					for(var i in data.items){
						data.items[i].ansLike = {
							type : 'like',
							count : data.items[i].snsCount.s_cnt_like_count,
							flag : data.items[i].is_user_like,
							id : data.items[i].ans_id,
							module: 'Answer',
							tkhId : 0
						};
						//是否是指定回答的人
						data.items[i].is_specify = isSpecify($scope.specifyUsers,data.items[i]);
					}				
				});
			}
		};
		//设置最佳答案
		$scope.setBestAnswer = function (answer){
			modalService.modal(function($scope, $modalInstance) {
				$scope.modalText = 'know_tip_set_best_anwser';
				$scope.modalOk = function() {
					setBestAnswer(answer);					
					$modalInstance.dismiss('cancel');
					$scope.showPage('detail');
				};
				$scope.modalCancel = function() {
					$modalInstance.dismiss('cancel');
				};
			});			
		};
		function setBestAnswer(answer){
			if(appInd){
				var wait = plus.nativeUI.showWaiting();
			}
			var setUrl = '/app/know/setBestAnswer/' + answer.ans_id + '/' + answer.ans_create_ent_id + '/'+$scope.knowDetail.que_id;
			Ajax.post(setUrl, {}, function(data){
				if(appInd){
					changeWebviewDetail(plus.webview.currentWebview().opener().id, function(){
						changeDetail(data, answer);
						wait.close();
					});
				} else {
					changeDetail(data, answer);
				}
			});
		}
		
		function changeDetail(data, answer){
			$scope.bestAnswer = answer;
			$scope.knowDetail.que_type = 'SOLVED';
			$scope.relAnswers = new Loader(answerUrl, {pageSize:2}, function(data){
				for(var i in data.items){
					data.items[i].ansLike = {
						type : 'like',
						count : data.items[i].snsCount.s_cnt_like_count,
						flag : data.items[i].is_user_like,
						id : data.items[i].ans_id,
						module: 'Answer',
						tkhId : 0
					}
					//是否是指定回答的人
					data.items[i].is_specify = isSpecify($scope.specifyUsers,data.items[i]);
				}
			});
			$scope.relAnswers.loaderMore(1);
		}
		//回答评价
		$scope.changeAnsVote = function(goodInd){
			var url='/app/know/changeAnsVote/'+$scope.knowDetail.que_id+'/' + goodInd;
			Ajax.post(url, {}, function(data){
				if(!data)return;
				if(data.evaluationInd == true){
					//提示
					alertService.add('info', 'know_tip_norepeat', 1000);
				} else {
					$scope.bestAnswer.ans_vote_total = data.answerSituation.ans_vote_total;
					$scope.good_rate = data.good_rate;					
					$scope.not_good_rate = data.not_good_rate;					
				}
			});
		};
		//问题补充
		$scope.contentKnow = function(){
			$scope.content = $scope.content ? $scope.content : '';
			if($scope.content.length > 500){
				alertService.add('warning', 'know_tip_content_length', 1000);				
				return;
			}			
			var url = '/app/know/changeQueContent/' + $scope.knowDetail.que_id;
			var params = {
				queContent : $scope.content
			};
			Ajax.post(url, params, function(data){
				$scope.knowDetail.que_content = $scope.content;
				
				Ajax.post('/app/know/knowDetail/json/'+ $scope.knowDetail.que_type+'/'+ $scope.knowDetail.que_id, {}, function(data){
					$scope.knowDetail.fileList = data.know_detail.fileList;
				});
				
				$scope.showPage('detail');
			});
		};
		//显示删除提示
		$scope.deleteTip  = function(){
//			toggle('removeTip');
			modalService.modal(function($scope, $modalInstance) {
				$scope.modalText = 'know_tip_remove';
				$scope.modalOk = function() {
					deleteKnow();
					$modalInstance.dismiss('cancel');
				};
				$scope.modalCancel = function() {
					$modalInstance.dismiss('cancel');
				};
			});
		};
		
		//删除答案
		$scope.deleteAns  = function(Answer,ansEntId,_this){
			//toggle('removeTip');
			modalService.modal(function($scope, $modalInstance) {
				$scope.modalText = 'know_ans_remove';
				$scope.modalOk = function() {
					delAns(Answer.ans_id,ansEntId);
					$modalInstance.dismiss('cancel');
				};
				$scope.modalCancel = function() {
					$modalInstance.dismiss('cancel');
				};
			});
		};
		//
		function delAns(ansId,ansEntId){
			Ajax.post('/app/know/delThisAnswer/' + ansId + '/' + ansEntId + '/' + $scope.knowDetail.que_id, {}, function(){
				window.location.reload();
			});
		}
		function toggle(id, callback){
	        $('#' + id).slideToggle(callback);
	        $('.header-overlay').toggleClass('show');
			$(".guide-menu-cont").css("height",$(".guide-menu-list-1").height());
		}
		
		//取消提问
		function deleteKnow(){
			var url = '/app/know/delKnowQuestion/' + $scope.knowDetail.que_id;
			Ajax.post(url, {}, function(){
					dialogService.modal("message_delete_success","o",function(){
						if(appInd){
							plus.webview.currentWebview().opener().evalJS("deleteQuestion('" + $scope.knowDetail.que_id + "')");
						}
						back();
					});
			});
		};
		//是否是指定回答的人
		function isSpecify(specifyUsers,item){
			var is_specify = false;
			if(specifyUsers){
				for( var u in specifyUsers) {
					if(specifyUsers[u].usr_ent_id == item.ans_create_ent_id){
						is_specify = true;
					}
				}
			}
			return is_specify;
		}
		//添加回答
		$scope.addAnwser = function(){

			if($scope.anwser == ''){
				alertService.add('warning', 'know_tip_anwser_not_null', 1000);	
				return;		
			}
			if($scope.anwser.length > 10000){
				alertService.add('warning', 'know_tip_anwser_length', 1000);
				return;
			}
			var url = '/app/know/addKnowAnswer/' + $scope.knowDetail.que_id;
			var params = {
				ansContent: $scope.anwser,
				ansReferContent : ''
			};
			Ajax.post(url, params, function(data){ 
				dialogService.modal("thank_you_for_answer","o",function(){
					
					$scope.relAnswers = new Loader(answerUrl, {pageSize:2}, function(data){
						for(var i in data.items){
							data.items[i].ansLike = {
								type : 'like',
								count : data.items[i].snsCount.s_cnt_like_count,
								flag : data.items[i].is_user_like,
								id : data.items[i].ans_id,
								module: 'Answer',
								tkhId : 0
							}
							data.items[i].is_specify = isSpecify($scope.specifyUsers,data.items[i]);
						}				
					});
					$scope.relAnswers.loaderMore(1);
					$scope.answers = new Loader(answerUrl, {}, function(data){
						for(var i in data.items){
							data.items[i].ansLike = {
								type : 'like',
								count : data.items[i].snsCount.s_cnt_like_count,
								flag : data.items[i].is_user_like,
								id : data.items[i].ans_id,
								module: 'Answer',
								tkhId : 0
							};
							data.items[i].is_specify = isSpecify($scope.specifyUsers,data.items[i]);
						}				
					});
					$scope.showPage('detail');
					$scope.anwser = '';
					
				});
			});
		
		};
		Ajax.post(url, {}, function(data){
			$scope.knowDetail = data.know_detail;						
			$scope.bestAnswer = data.best_answer || data.answer || false;
			$scope.specifyUsers = data.users;//指定回答问题的人
			if($scope.bestAnswer){
				$scope.bestAnswer.ansLike = {				
					count : $scope.bestAnswer.snsCount.s_cnt_like_count,
					flag : $scope.bestAnswer.is_user_like,
					id : $scope.bestAnswer.ans_id,
					module: 'Answer',
					tkhId : 0
				};
				$scope.bestAnswer.is_specify = isSpecify($scope.specifyUsers,$scope.bestAnswer);
			}			
			$scope.good_rate = data.good_rate || 0;
			$scope.not_good_rate = data.not_good_rate || 0;
			$scope.relAnswers = new Loader(answerUrl, {pageSize:2}, function(data){
				for(var i in data.items){
					data.items[i].ansLike = {
						type : 'like',
						count : data.items[i].snsCount.s_cnt_like_count,
						flag : data.items[i].is_user_like,
						id : data.items[i].ans_id,
						module: 'Answer',
						tkhId : 0
					}
					data.items[i].is_specify = isSpecify($scope.specifyUsers,data.items[i]);
				}				
			});
			$scope.relAnswers.loaderMore(1);
			$scope.relQuestions = new Loader(relevantKnowUrl+$scope.knowDetail.knowCatalog.kca_id,  {pageSize:4});
			$scope.relQuestions.loaderMore(1);
			$scope.answers = new Loader(answerUrl, {}, function(data){
				for(var i in data.items){
					data.items[i].ansLike = {
						type : 'like',
						count : data.items[i].snsCount.s_cnt_like_count,
						flag : data.items[i].is_user_like,
						id : data.items[i].ans_id,
						module: 'Answer',
						tkhId : 0
					};
					data.items[i].is_specify = isSpecify($scope.specifyUsers,data.items[i]);
				}				
			});
			$scope.questions = new Loader(relevantKnowUrl+$scope.knowDetail.knowCatalog.kca_id, {});
		});
	}]);
	knowModule.controller('allController', ['$scope', '$filter','Ajax', 'Loader', 'KnowService',function($scope, $filter, Ajax, Loader,KnowService){
		
		$scope.selectedQueType = app.getUrlParam("show");
		
		$scope.pageTitle = "know_all";
		
		if('UNSOLVED' == $scope.selectedQueType){//待解决
			$scope.pageTitle = "know_wait_anwser";
		}else if('SOLVED' == $scope.selectedQueType){//已解决
			$scope.pageTitle = "know_anwsered";
		}else if('POPULAR' == $scope.selectedQueType){//精选
			$scope.pageTitle = "know_popular";
		}else if('FAQ' == $scope.selectedQueType){//FAQ
			$scope.pageTitle = "know_faq";
		}
		
		$scope.catId = 0;
		$scope.questionTypes = KnowService.getQuestionTypes();
		
		$scope.selectQueType = function(val){
			$scope.selectedQueType = val;
			loadList();
		}
		
		var loadList = function(){
			var selectedQueTypeVal = $scope.questionTypes[$scope.selectedQueType].value;
			var url = '/app/know/allKnow/que/' + selectedQueTypeVal + '/' + $scope.catId;
			$scope.questionList = new Loader(url, {},function(data){
				for(var i in data.items){
					var item = data.items[i];
					if(selectedQueTypeVal == "POPULAR"){ //当点击精选时，所查到的数据的类型改成精选，不伦其是什么类型（用于左下角显示）
						item.que_type = "POPULAR";
					}
					item.typeInfo = $scope.questionTypes[item.que_type];
				}
			});	
		}
		
		loadList();
		
		var toggleFlag = false;
		
		$scope.toggleSelection = function(){
			$('#que_selectioin').slideToggle();
			if(!toggleFlag){
				$("html,body").animate({scrollTop:"0px"},200);
				toggleFlag = true;
			}else{
				toggleFlag = false;
			}
		};
		
		$scope.selectCatalogCallback = function(c){
			$scope.catId = c.kca_id;
			loadList();
		}
		
	}]);

	
	knowModule.controller('memberController', ['$scope', 'Store', 'Ajax', 'Loader', 'alertService', function($scope, Store, Ajax, Loader, alertService){
        var groupId = app.getUrlParam('groupId') || Store.get('groupId');
        //var detailUrl = '/app/group/detail/json/' + groupId;
        //var memberUrl = '/app/know/askInstructors/ + ';
        var findMemberUrl = '/app/user/getInstructors'; //添加成员列表的URL
        $scope.isGroupMember = false;        
        $scope.s_grp_private = 0;
        $scope.snsGroup = {};
        $scope.isManager = false;       
        $scope.isNormal = false;        
        $scope.serverHost = serverHost;
        $scope.show = {
            member : true,
            add : false            
        };
        $scope.searchContent = '';
        $scope.findMemberList = {};
        //$scope.memberList = new Loader(memberUrl, {});   //成员列表
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

        $scope.select = function(index){
            $scope.findMemberList.items[index].select = $scope.findMemberList.items[index].select ? false : true;
        };
        $scope.search = function(){
            var params = {
                searchContent : $scope.searchContent
            };
            if($scope.show.member){
                $scope.memberList = new Loader(memberUrl, params); 
            }else if($scope.show.add){
                $scope.findMemberList = new Loader(findMemberUrl, params);
            }
        };
    }]);
    
    
	function toggleCat(){		
		$('.header-kang').slideToggle();
		$('.header-overlay').toggleClass('show');
		$(".guide-menu-cont").css("height",$(".guide-menu-list-1").height());
	}
	function toggleAllknowCat(){
		$('.header-wenda').slideToggle();
		$('.header-overlay').toggleClass('show');
		$(".guide-menu-cont").css("height",$(".guide-menu-list-1").height());
		$('#main').toggle();
	}
	function toggle(id, callback){
        $('#' + id).slideToggle(callback);
        $('.header-overlay').toggleClass('show');
		$(".guide-menu-cont").css("height",$(".guide-menu-list-1").height());
	}
});

function deleteQuestion(que_id){
	$("#que_" + que_id).remove();
}