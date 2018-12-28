$.include([
    'js/directives/SNSDirectives.js',
    'js/directives/DoingDirective.js',
    'js/services/UserService.js',
    'js/services/SNSService.js'
], '../../');
$(function(){
    var doingModule = angular.module('doing', ['globalCwn', 'userService']);
    doingModule.controller('doingController', ['$scope', '$filter', 'User', 'Store', 'Ajax', 'Loader', 
        function($scope, $filter, User, Store, Ajax, Loader){
            var url = '/app/doing/dynamicJson/';
            $scope.user = {};
            $scope.serverHost = serverHost;
            $scope.selectType = 'all';
            $scope.title = 'dynamic_type_all_text';
            $scope.doingList = {};
            var params = {
                isMobile : true
            };
            loaderList(url + $scope.selectType);
            function loaderList(url){
                $scope.doingList = new Loader(url, params, function(data){
                    for(var i in data.items){
                        data.items[i].doingLike = {
                            type : 'like',
                            count : data.items[i].snsCount ? data.items[i].snsCount.s_cnt_like_count : '0',
                            flag : data.items[i].is_user_like,
                            id :  data.items[i].s_doi_id,
                            module : 'Doing',
                            tkhId : 0
                        };
                        data.items[i].replyShow=false;
                        for(var j in data.items[i].replies){
                            data.items[i].replies[j].commentLike = {
                                type : 'like',
                                count : data.items[i].replies[j].snsCount ? data.items[i].replies[j].snsCount.s_cnt_like_count : '0',
                                flag : data.items[i].replies[j].is_user_like,
                                id : data.items[i].replies[j].s_cmt_id,
                                module: 'Doing',
                                isComment : 1,
                                tkhId : 0
                            };
                        }
                    }
                });
            };
            $scope.doingType = [
                {
                    type : 'all',
                    title : 'dynamic_type_all_title',
                    text : 'dynamic_type_all_text'
                },{
                    type : 'course',
                    title : 'dynamic_type_course_title',
                    text : 'dynamic_type_course_text'
                },{
                    type : 'group',
                    title : 'dynamic_type_group_title',
                    text : 'dynamic_type_group_text'
                },{
                    type : 'answer',
                    title : 'dynamic_type_answer_title',
                    text : 'dynamic_type_answer_text'
                },{
                    type : 'mine',
                    title : 'dynamic_type_mine_title',
                    text : 'dynamic_type_mine_text'
                }
            ];
            $scope.toggleSelect = function(){
            	$(".header-guide").slideToggle();
            	$(".list-pic-3").slideToggle()
                $(".header-overlay").toggleClass("show");            	
//                toggle();
            };
            $scope.select = function(type){
                $scope.selectType = type;
                $scope.saveSelect();
            };
            $scope.saveSelect = function(){
                var listUrl ='';
                if($scope.selectType == 'mine'){
                    var meId = Store.get('loginUser');
                    listUrl = '/app/doing/user/json/' + meId + '/Doing/' + meId;
                }else{
                    listUrl = url + $scope.selectType;                                    
                }
                for(var i in $scope.doingType){
                    if($scope.doingType[i].type == $scope.selectType){
                        $scope.title = $scope.doingType[i].text;
                        break;
                    }
                }               
                loaderList(listUrl);
//                $scope.toggleSelect();
            };
            User.userInfo(function(data){
                $scope.user = data.regUser;
            });            
    }]);
    function toggle(){
        $(".header-guide").slideToggle();
        $(".header-overlay").toggleClass("show");
        $('#main').toggle();
    }
});