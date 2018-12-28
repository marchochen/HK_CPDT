$.include([
    'js/services/knowledgeCenterService.js',
    'js/common/courseAndExamCommon.js'
], '../../');
$(function(){
    var rankModule = angular.module('rank', ['globalCwn']);
    rankModule.filter('toint', function(){
        return function(input){
            if(!input || isNaN(input))return 0;
            return Math.floor(input);
        };
    });
    rankModule.filter('timeFormat', function(){
        return function(input){
            return input>9 ? input : '0' + input;
        };
    });
    rankModule.filter('ranking', ['Store', function(Store){
        return function(input, rank){
            var msg = input;
            if(Store.get('globalLang') == 'en-us' && rank && rank > 0){
                switch(rank){
                    case 1 : msg = 'st'; break;
                    case 2 : msg = 'nd'; break;
                    case 3 : msg = 'rd'; break;
                    case 4 :
                    case 5 :
                    case 6 :
                    case 7 :
                    case 8 :
                    case 9 : msg = 'th'; break;
                    default : msg = ''; break;
                }
            }
            return msg;
        };
    }]);
    rankModule.filter('kbTypeFilter', function() {
        return function(input) {
            var types = {
                'ARTICLE': 'knowledge_type_article',
                'DOCUMENT': 'knowledge_type_document',
                'IMAGE': 'knowledge_type_image',
                'VEDIO': 'knowledge_type_vedio',
            };
            if (input) {
                return types[input] || '';
            }
            return '';
        }
    });
    rankModule.controller('mainController', ['$scope', 'Ajax', 'Store', function($scope, Ajax, Store){
        var url = '/app/rank/mobile/main/json';
        $scope.creditDetail = {};
        $scope.learnDetail = {};
        Ajax.post(url, {}, function(data){
            $scope.creditDetail = data.creditDetail;
            $scope.learnDetail = data.learnDetail;
        });
    }]);

    rankModule.controller('creditsController', ['$scope', 'Store', 'Ajax', 'Loader', 'UserService',function($scope, Store, Ajax, Loader,UserService){

        var url = '/app/rank/credit_rank';
        var myCreditsUrl = '/app/rank/my_credit';
        $scope.myCredits = {};
        $scope.serverHost = serverHost;
        $scope.loginUserId = Store.get('loginUser');
        $scope.creditsRank = new Loader(url, {});
        Ajax.post(myCreditsUrl, {}, function(data){
            $scope.myCredits = data.my_credit_detail;
        });
		UserService.userInfo(function(data){
			$scope.serverHost = window.serverHost;
			if(data && data.regUser){
				$scope.user = data.regUser;
			}
		});
    }]);

    rankModule.controller('learningSituationController', ['$scope', 'Store', 'Ajax', 'Loader', 'UserService',function($scope, Store, Ajax, Loader,UserService){

        var url = '/app/rank/learningSituationRank';
        var myLearningSituationUrl = '/app/rank/myLearningSituation';
        $scope.myLearningSituation = {};
        $scope.serverHost = serverHost;
        $scope.loginUserId = Store.get('loginUser');
        $scope.learningSituationRank = new Loader(url, {});
        Ajax.post(myLearningSituationUrl, {}, function(data){
            $scope.myLearningSituation = data.learnDetail;
        });
        UserService.userInfo(function(data){
			$scope.serverHost = window.serverHost;
			if(data && data.regUser){
				$scope.user = data.regUser;
			}
		});
    }]);
    rankModule.controller('courseController', ['$scope', 'Loader', function($scope, Loader){
    	
    	var showType = app.getUrlParam('show');//是否显示最新课程的tab
    	$scope.showNewCourse = true;
    	$scope.showLearingCourse = false;
    	$scope.showLikeCourse = false;
    	if("newCourse" === showType){
    		$scope.showNewCourse = true;    		
    	}else if("learingCourse" === showType){
    		$scope.showLearingCourse = true;
    	}else{
    		$scope.showLikeCourse = true;
    	}
    	
    	$scope.serverHost = serverHost;
    	
        var url = '/app/rank/course_rank';
        var paramsLike = {
        	showMobileOnly : true,
            sortname : 's_cnt_like_count',
            sortorder : 'desc',
        };
         var paramsNews = {
        	showMobileOnly : true,
            sortname : 'itm_publish_timestamp',
            sortorder : 'desc',
        };
         var paramsLearing = {
        	showMobileOnly : true,
            sortname : '"app.app_total"',
            sortorder : 'desc',
        };
         
        var getCourseListLoader = function(url,params){
        	return new Loader(url,params,function(data){
        		for(var i=0;i<data.items.length;i++){
            		var item = data.items[i];
            		item.encItmId = wbEncryptor.encrypt(item.itm_id);
            		item.cla = courseAndExamModule.getItemIcon("course",item.itm_type);
            	}
        	});
        } 
        
        $scope.likeCourseList = getCourseListLoader(url, paramsLike);
        $scope.newsCourseList = getCourseListLoader(url, paramsNews);
        $scope.learningCourseList = getCourseListLoader(url, paramsLearing);
        
    }]);
    rankModule.controller('knowledgeController', ['$scope', 'Loader', 'knowledgeCenterService' , function($scope, Loader ,knowledgeCenterService){
        var url_latest = '/app/kb/center/index/latest';
        var url_hottest = '/app/kb/center/index/hottest';
        var url_popularity = '/app/kb/center/index/popularity';
        $scope.serverHost = serverHost;
        $scope.latestList = new Loader(url_latest, {},function(result){
        	setKnowledgeInfo(result);
        });
        $scope.hottestList = new Loader(url_hottest, {},function(result){
        	setKnowledgeInfo(result);
        });
        $scope.popularityList = new Loader(url_popularity, {} ,function(result){
        	setKnowledgeInfo(result);
        });
        
        var setKnowledgeInfo = function(result){
        	for(var i in result.items){
				var item = result.items[i];
				item.enc_kbi_id = wbEncryptor.encrypt(item.kbi_id);
				item.info = knowledgeCenterService.getKnowledgeInfo(item.kbi_type,item.docType);
			}
        };
    }])
});