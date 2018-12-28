$.include([
], '../../');
$(function() {
	var votingModule = angular.module('voting', ['globalCwn']);
	votingModule.controller('indexController', ['$scope','Loader',function($scope,Loader) {

		var ingUrl = "/app/voting/pageJsonForIng";
		var edUri = "/app/voting/pageJsonForEd";

		var params = {
			pageSize : '10'
		};

		$scope.serverHost = serverHost;

		$scope.ingList = new Loader(ingUrl, params);
		$scope.edList = new Loader(edUri, params);

	}]);

	votingModule.controller('viewResultController', ['$scope','Ajax','$sce',function($scope,Ajax,$sce) {

		var url = "/app/voting/viewResultForMobile";
		var vot_id = app.getUrlParam("vot_id");
		var param = {
			enc_vot_id:vot_id
		};

		Ajax.post(url,param,function(data){
			$scope.voting = data.voting;
			$scope.voting.vot_content = $sce.trustAsHtml($scope.voting.vot_content);
			$scope.responseResult = data.responseResult;
			setPieChart(data.responseResult);
		});

		function setPieChart(responseResult){
			var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
			var pieChart = new Chart(pieChartCanvas);
			if(!responseResult){
				return;
			}
			var pieColor = ['#00c0ef','#dd4b39','#00a65a','#f39c12','#c86af1','#ffd21d','#2d3e50','#ff0000','#657694','#428bca'];
			var pieClass1 = ['progress-bar-blue','progress-bar-purple','progress-bar-green','progress-bar-orange','progress-bar-pink','progress-bar-yellow','progress-bar-indigo','progress-bar-red','progress-bar-light','progress-bar-breen'];
			var pieClass2 = ['progress-bar progress-bar-blue','progress-bar progress-bar-purple','progress-bar progress-bar-green','progress-bar progress-bar-orange','progress-bar progress-bar-pink','progress-bar progress-bar-yellow','progress-bar progress-bar-indigo','progress-bar progress-bar-red','progress-bar progress-bar-light','progress-bar progress-bar-breen'];
			var pieData = [];
			for(var i=0;i<responseResult.length;i++){
				responseResult[i].cssClass1 = pieClass1[i];
				responseResult[i].cssClass2 = pieClass2[i];
				var pieItem = {
					value: responseResult[i].count,
					color: pieColor[i],
					highlight: pieColor[i],
					label: responseResult[i].label
				};
				pieData.push(pieItem);
			}
			var pieOptions = {
					segmentShowStroke: true,
					segmentStrokeColor: "#fff",
					segmentStrokeWidth: 1,
					percentageInnerCutout: 50,
					animationSteps: 100,
					animationEasing: "easeOutBounce",
					animateRotate: true,
					animateScale: false,
					responsive: true,
					maintainAspectRatio: false,
					legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>",
					tooltipTemplate: "<%=value %> <%=label%>"
			};
			pieChart.Doughnut(pieData, pieOptions);
		}
	}]);
	votingModule.controller('voteController', ['$scope','Ajax','$sce','alertService','$filter',function($scope,Ajax,$sce,alertService,$filter){
		
		var url = "/app/voting/getVoting";
		var voteUrl = "/app/voting/voteForMobile";
		var vot_id = app.getUrlParam("vot_id");
		var param = {
			enc_vot_id:vot_id
		};

		var answers = [];

		Ajax.post(url,param,function(data){
			$scope.voting = data;
			
			$scope.voting.enc_vot_id = wbEncryptor.encrypt($scope.voting.vot_id);
			
			$scope.voteOptions = data.voteQuestion.voteOptions;
			for(var i=0;i<$scope.voteOptions.length;i++){
				$scope.voteOptions[i].checked = false;
				$scope.voteOptions[i].value = $scope.voteOptions[i].vto_id;
			}
			$scope.voting.vot_content = $sce.trustAsHtml($scope.voting.vot_content);
		});

		Array.prototype.removeValue = function(val) {
		    var index = this.indexOf(val);
		    if (index > -1) {
		        this.splice(index, 1);
		    }
		};

		$scope.check = function(field){
			$scope.voteOptions[field.$index].checked = field.answers;
			if(field.answers){
				answers.push($scope.voteOptions[field.$index].value);
			}else{
				answers.removeValue($scope.voteOptions[field.$index].value);
			}
		}

		var showSuccess = function(){
		     $(".header-tip").slideToggle();
             $(".header-overlay").toggleClass("show");
	   	}

		$scope.formSubmit = function(){
			var flag = false;
			for(var i=0;i<$scope.voteOptions.length;i++){
				if($scope.voteOptions[i].checked){
					flag = true;
					break;
				}
			}
			if(!flag){
				alertService.add('warning', $filter("translate")("voting_select_option_tip"));
				return;
			}
			param.answers = answers;
			param.vot_id = wbEncryptor.decrypt(vot_id);
			Ajax.post(voteUrl,param,function(data){
				if("-1" === data){
					alertService.add('warning', $filter("translate")("voting_repeat_vote_tip"));
				}else if("-2" === data){
					alertService.add('warning', $filter("translate")("voting_select_option_tip"));
				}else {
					showSuccess();
				}
				
				//更新首页任务数量和父窗口页面
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
						
						//更新父窗口
						changeWebviewDetail(plus.webview.currentWebview().opener().id);
				}
				
			});
		};
		
//		投票结果
		var result_url = "/app/voting/viewResultForMobile";

		Ajax.post(result_url,param,function(data){
			$scope.result_voting = data.voting;
			$scope.result_voting.vot_content = $sce.trustAsHtml($scope.result_voting.vot_content);
			$scope.responseResult = data.responseResult;
			setPieChart(data.responseResult);
		});

		function setPieChart(responseResult){
			var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
			var pieChart = new Chart(pieChartCanvas);
			if(!responseResult){
				return;
			}
			var pieColor = ['#00c0ef','#dd4b39','#00a65a','#f39c12','#c86af1','#ffd21d','#2d3e50','#ff0000','#657694','#428bca'];
			var pieClass1 = ['progress-bar-blue','progress-bar-purple','progress-bar-green','progress-bar-orange','progress-bar-pink','progress-bar-yellow','progress-bar-indigo','progress-bar-red','progress-bar-light','progress-bar-breen'];
			var pieClass2 = ['progress-bar progress-bar-blue','progress-bar progress-bar-purple','progress-bar progress-bar-green','progress-bar progress-bar-orange','progress-bar progress-bar-pink','progress-bar progress-bar-yellow','progress-bar progress-bar-indigo','progress-bar progress-bar-red','progress-bar progress-bar-light','progress-bar progress-bar-breen'];
			var pieData = [];
			for(var i=0;i<responseResult.length;i++){
				responseResult[i].cssClass1 = pieClass1[i];
				responseResult[i].cssClass2 = pieClass2[i];
				var pieItem = {
					value: responseResult[i].count,
					color: pieColor[i],
					highlight: pieColor[i],
					label: responseResult[i].label
				};
				pieData.push(pieItem);
			}
			var pieOptions = {
					segmentShowStroke: true,
					segmentStrokeColor: "#fff",
					segmentStrokeWidth: 1,
					percentageInnerCutout: 50,
					animationSteps: 100,
					animationEasing: "easeOutBounce",
					animateRotate: true,
					animateScale: false,
					responsive: true,
					maintainAspectRatio: false,
					legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>",
					tooltipTemplate: "<%=value %> <%=label%>"
			};
			pieChart.Doughnut(pieData, pieOptions);
		}
		
	}]);
});
