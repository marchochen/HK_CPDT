$.include([
    'lib/angular/angular-touch.min.js',
], '../../');
$(function() {
    var annModule = angular.module('announce', ['globalCwn', 'ngTouch']);
    annModule.controller('annController', ['$scope', '$http', 'Loader', 'Ajax', 'Swipe', function($scope, $http, Loader, Ajax, Swipe) {
        var annListUrl = '/app/announce/pageJson/0';
        var params = {
            sortname: 'msg_upd_date',//msg_begin_date按發佈時間  msg_upd_date按照更新時間
            sortorder: 'desc'
        };
        var pageNo = null;
        var curId = null;
        $scope.annList = new Loader(annListUrl, params, function(data) {
            pageNo = data.params.pageNo;
        });
        $scope.showDetail = false;
        $scope.showList = true;
        $scope.annDetail = {};
        
        var cur_ann;
        
        $scope.lookDetail = function(annId, tcenter,ann) {
        	
        	cur_ann = ann;
        	
            if (annId && annId != '') {
                $scope.showDetail = true;
                $scope.showList = false;
                curId = annId;
                show(annId, tcenter,ann);
            }
        };

        //是否是详细
        var id = app.getUrlParam("id");
        if (id != undefined) {
            $scope.lookDetail(id);
        }

        var swipe = new Swipe(annListUrl, pageNo, curId);
        var list = [];
        $scope.showNear = function(forward) {
            swipe.getNearId(params, list, 'msg_id', forward, function(gId, result) {
                if (gId) {
                    if (result) {
                        list = result;
                        for (var i = 0; i < result.length; i++) {
                            if (result[i].msg_id == gId) {
                                show(swipe.curId = gId, result[i].tcenter);
                                break;
                            }
                        }
                    } else {
                        show(swipe.curId = gId);
                    }
                }
            });
        };

        $scope.insReceipt = function(isReceipted, msgId) {
            if (!isReceipted) {
                Ajax.post('/app/announce/insReceipt/' + msgId, {}, function() {
                    $scope.isReceipted = !isReceipted;
                    if(cur_ann){
                    	cur_ann.curUserIsRead = true;
                    }
                    
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
                    
                });
            }
        }
        
        function show(annId, tcenter, ann) {
            var detailurl = '/app/announce/detailJson/' + annId;
            Ajax.post(detailurl, {}, function(data) {
                $scope.annDetail = data.detail;
                $scope.isReceipted = data.isReceipted;
                if (tcenter) $scope.annDetail.tcenter = tcenter;
                
                //需求变更
	                //如果不需要回执，则需要在回执表中直接插入数据，表示当前用户已经阅读
	                //如果需要回执，则需要用户手动点击【我已阅读】才在回执表插入数据，表示当前用户已经阅读
                if(!$scope.annDetail.msg_receipt){
                	$scope.insReceipt(false,$scope.annDetail.msg_id);
                	if(ann){
                		ann.curUserIsRead = true;
                	}
                }
                
            });
        }
        

        $scope.back = function() {
            var isReturn = app.getUrlParam("isReturn");
            if ($scope.showList || isReturn) {
                back();
            }
            if ($scope.showDetail) {
                $scope.showDetail = false;
                $scope.showList = true;
            }
            return false;
        };
    }]);
});