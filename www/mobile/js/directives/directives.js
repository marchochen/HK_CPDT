appModule.directive('pager', function() {
    var tempHtml =
        '<ul ng-show="pager.hasNext" class="pagination"' +
        'num-pages="tasks.pageCount"' +
        'current-page="tasks.currentPage"' +
        ' on-select-page="selectPage(page)">' +
        ' <li ng-class="{disabled: noPrevious()}">' +
        '     <a ng-click="selectPrevious()">&laquo;</a>' +
        ' </li>' +
        ' <li ng-repeat="page in pages"' +
        '     ng-class="{active: isActive(page)}">' +
        '     <a ng-click="selectPage(page)">{{page}}</a>' +
        ' </li>' +
        ' <li ng-class="{disabled: noNext()}">' +
        '     <a ng-click="selectNext()">&raquo;</a>' +
        ' </li>' +
        '</ul>';
    return {
        restrict: 'A',
        template: tempHtml,
        replace: false,
        scope: {
            pager: '=pager'
        },
        link: function(scope, element, attrs) {
            scope.$watch('pager.numPages', function(value) {
                scope.pages = [];
                for (var i = 1; i <= value; i++) {
                    scope.pages.push(i);
                }
                if (scope.currentPage > value) {
                    scope.pager.selectPage(value);
                }
            });
            scope.isActive = function(page) {
                return scope.pager.page === page;
            };
            scope.selectPage = function(page) {
                if (!scope.isActive(page)) {
                    scope.pager.page = page;
                    scope.pager.selectPage(page);
                }
            };
            scope.selectPrevious = function() {
                if (!scope.noPrevious()) {
                    scope.pager.page -= 1;
                    scope.pager.selectPage(scope.pager.page);
                }
            };
            scope.selectNext = function() {
                if (!scope.noNext()) {
                    scope.pager.page += 1;
                    scope.pager.selectPage(scope.pager.page);
                }
            };
            scope.noPrevious = function() {
                return scope.pager.page == 1;
            };
            scope.noNext = function() {
                return scope.pager.page == scope.pager.numPages;
            };
            //排序
            scope.orderByCol = 'id';
            scope.orderByReversed = false;
            scope.flipOrderBy = function(col) {
                scope.orderByReversed = (col == scope.orderByCol) ? !scope.orderByReversed : false;
                scope.orderByCol = col;
            };
            //get first page
            scope.pager.selectPage(1);
        }
    };
}).directive('loader', function($window) {
    return {
        restrict: 'A',
        template: '<div>' +
            '<div class="panel-list-more" ng-show="loader.busy"><img width="20px" height="20px;" ng-src="{{rp}}images/loading.gif"/>&nbsp;{{\'loader_loading\' | translate}}</div>' +
            '<div class="panel-list-boxdate" ng-hide="hideNull();"><div class="panel-list-nodate">{{\'loader_no_data\' | translate}}</div></div><div class="panel-list-foot" ng-hide="hideNull();"></div>' +
            '<a class="panel-list-more" href="javascript:;" ng-show="loader.hasNext && !loader.busy" ng-click="loader.loaderMore(false,callback)" translate="loader_more">load more</a>' +
            '<a href="javascript:void(0)" class="panel-list-top"></a>' +
            '</div>',
        replace: false,
        require: '?^pane',
        scope: {
            loader: '=loader',
            callback: '=',
            showNull: '=shownull',
            rp : '@rp'
        },
        link: function(scope, element, attrs , paneCtrl) {
        	
        	scope.rp = scope.rp || "../../";
        	
            scope.hideNull = function() {
            	return (scope.loader != undefined && scope.loader.items && scope.loader.items.length>0) || (scope.showNull != undefined && !scope.showNull);
            };
            scope.$watch('loader', function() {
                if (scope.loader && scope.loader.loaderMore) {
                    scope.loader.loaderMore(1, scope.callback);
                }
            });
            
            $window = angular.element($window);
            
            var handler = function() {
            	var scrollDistance = 0;
                var elementBottom, remaining, shouldScroll, windowBottom;
                windowBottom = $window.height() + $window.scrollTop();
                elementBottom = element.offset().top + element.height();
                remaining = elementBottom - windowBottom;
                shouldScroll = remaining <= $window.height() * scrollDistance;
                if (shouldScroll && element.parent().css("display") === "block") {
                	scope.loader.loaderMore(false,scope.callback);
                }
                
                if(element.parent().css("display") === "block"){
                	if($window.scrollTop()>100){
                    	$(".panel-list-top").stop().fadeIn(400);
                    }else{      
                    	$(".panel-list-top").stop().fadeOut(400);
                    }
                }
            };
            
            $(".panel-list-top").click(function(){
            	$("html,body").animate({scrollTop:"0px"},200);
            });

            
            $window.on('scroll',handler);
            
            scope.$on('$destroy', function() {
            	return $window.off('scroll', handler);
            });
            
        }
    };
}).directive('htmlDecode', function() {
    function htmlDecode(value) {
        return !value ? value : String(value).replace(/&amp;/g, "&").replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"').replace(/&#34;/g, '"');
    }
    return {
        restrict: 'A',
        replace: false,
        scope: {
            text: '=htmlDecode'
        },
        link: function(scope, element, attrs) {
            scope.$watch('text', function() {
                element.html(htmlDecode(scope.text));
            });
        }
    };
}).directive('tabs', function() {
    return {
        restrict: 'EA',
        transclude: true,
        scope: {
            css: '@',
            selectedCallback : '=',
            hideHeader : '='
        },
        controller: function($scope, $attrs) {
            var panes = $scope.panes = [];
            $scope.select = function(pane,index) {
                angular.forEach(panes, function(pane) {
                    pane.selected = false;
                });
                pane.selected = true;
                pane.index = index;
                if($scope.selectedCallback){
                	$scope.selectedCallback(pane);
                }
            }

            this.addPane = function(pane) {
                if (pane.select != undefined && pane.select != null) {
                    if (pane.select) {
                        pane.selected = true;
                    } else {
                        pane.selected = false;
                    }

                } else if (panes.length < 1) {
                    pane.selected = true;
                }
                panes.push(pane);
            }
            if (!$scope.css) {
                $scope.css = 1;
            }
        },
        template: '<div class="panel-tab-1 pub-tab">' +
            '<ul class="panel-tabnav-{{css}} pub-tabnav" ng-style="{\'display\':(hideHeader ? \'none\' : \'\')}">' +
            '<li ng-repeat="pane in panes"  ng-click="select(pane,$index)" style="white-space:nowrap;" ng-class="{cur:pane.selected}">' +
            '{{pane.title}}' +
            '</li>' +
            '</ul>' +
            '<div ng-transclude></div>' +
            '</div>',
        replace: true
    };
}).directive('pane', function() {
    return {
        require: '^tabs',
        restrict: 'EA',
        transclude: true,
        scope: {
            title: '@',
            style: '@',
            select: '='
        },
        link: function(scope, element, attrs, tabsCtrl) {
            var css = scope.$parent.$parent.css ? scope.$parent.$parent.css : 1;
            scope.style = scope.style ? scope.style : 'panel-tabcont-' + css + ' pub-tabcont';
            tabsCtrl.addPane(scope);
        },
        template: '<div class="{{style}}" ng-show="selected" ng-transclude></div>',
        replace: true
    };
}).directive('forward', ['$window', 'Store', function($window, Store) {
    return {
        restrict: 'A',
        replace: false,
        scope: {
            page: '=forward', // html? a=s&b=n;
        },
        link: function(scope, element, attrs) {
            var url = '';
            var paramStrs = new Array();
            scope.$watch('page', function() {
                url = scope.page.substring(0, scope.page.indexOf('?'));
                paramStrs = scope.page.substring(scope.page.indexOf('?') + 1).split('&');
            });
            element.bind('click', function() {
                //for(var i = 0; i < paramStrs.length; i++){
                //Store.set(paramStrs[i].split('=')[0], paramStrs[i].split('=')[1]);
                //}
                url = scope.page;
                if (scope.page.indexOf("javascript:") < 0) {
                    url = app.getUrlPath(url);
                }
                $window.location.href = url;
            });
        }
    };
}]).directive('focusMe', ['$window', function($window) {
    return {
        restrict: 'A',
        replace: false,
        scope: {
            focusMe: "="
        },
        link: function(scope, element, attrs) {
            scope.$watch('focusMe.$viewValue', function(o, n) {
                if (o != n && scope.focusMe.$invalid) {
                    element.focus();
                    element.css("border", "1px red solid")
                } else if (!scope.focusMe.$invalid) {
                    element.css("border", "")
                }
            })

        }
    };
}]).directive('textareaChange', function() {
    return {
        restrict: 'A',
        require: 'ng-model',
        replace: false,
        link: function(scope, element, attrs) {
            scope.$watch('question.value', function(o, n) {
                if (o == '') {
                    element.removeClass("pure-form-txt-6-nobg").addClass("pure-form-txt-6")
                } else if (o != n) {
                    element.removeClass("pure-form-txt-6").addClass("pure-form-txt-6-nobg")
                }
            })

        }
    }

}).directive('wzmDocker',function(modalService,Ajax,Store,UserService){

	return {
        restrict: 'AE',
        replace: true,
        scope : {
        	rp : '@rp',
        	target : '@'
        },
        template : '<div class="docker-menu gps"> \
			        	<ul> \
				        	<a href="javascript:clicked(\'{{rp}}views/learning/courseCatalog.html?show=myCourse\',true)"><span class="gps-learn"></span><p class="gps-learn-p" translate="cos_title"></p></a> \
        					<a href="javascript:clicked(\'{{rp}}views/learning/courseCatalog.html?show=courseCatalog\',true)"><span class="gps-catalog"></span><p class="gps-catalog-p" translate="cos_catalog2"></p></a> \
				        	<a href="javascript:clicked(\'{{rp}}views/index.html\',false,\'home\')"><span class="gps-index"></span></a> \
				        	<a href="javascript:clicked(\'{{rp}}views/personal/personal.html?person={{user.enc_usr_ent_id}}\',true,\'personal{{user.usr_ent_id}}\')"><span class="gps-user"></span><p class="gps-user-p" translate="dynamic_type_mine_title"></p></a> \
				        	<a href="#nav"><span class="gps-more"></span><p translate="more_text"></p></a> \
			        	</ul> \
				        <nav id="nav"> \
				            <div> \
				                <div class="list-pic-3"> \
				                    <div class="own-box"> \
				                        <div class="own-box-area"> \
				                            <ul> \
				                                <li class="nav-user clearfix" ng-click="toPersonPage()"> \
				                                    <a class="nav-user-pic" href="javascript:void(0)">\
				                                        <div><img id="user_photo_{{user.usr_ent_id}}" ng-src="{{serverHost}}{{user.usr_photo}}" /></div> \
				                                    </a>\
				                                    <a class="nav-user-title" href="javascript:void(0)" style="color:#fff;" ng-bind="user.usr_display_bil"></a> \
				                                </li> \
				                            </ul> \
				                        </div> \
				                    </div> \
				                    <img class="list-pic-3-pic" alt="" src="{{rp}}images/celan_a01.png"> \
				                </div> \
				                <div class="cont-info-2" style="margin:-50px 0 0 0;position:relative;z-index:2;"> \
				                    <ul> \
				                        <li class="nav-article nav-link border"><a href="{{{url:rp+\'views/article/article.html\'} | appUrl}}" translate="article"></a></li> \
				                        <li class="nav-border"></li> \
				                    </ul> \
				                </div> \
				                <div class="cont-info-2"> \
				                    <ul> \
        								<li class="nav-video nav-link border"><a href="{{{url:rp+\'views/course/openCourse.html\'} | appUrl}}" translate="openCourse"</li> \
        								<li ng-if="showLive" class="nav-live nav-link border"><a href="{{{url:rp+\'views/live/live.html\'} | appUrl}}" translate="lab_live_title"</li> \
				                        <li ng-if="showSpecialTopic" class="nav-zhuanti nav-link border"><a href="{{{url:rp+\'views/specialTopic/specialTopicList.html\'} | appUrl}}" translate="map_special_topic"><!--专题培训--></a></li> \
				                        <li class="nav-border"></li> \
				                    </ul> \
				                </div> \
				                <div class="cont-info-2" style="position:relative;z-index:3;"> \
				                    <ul> \
				                        <!-- <li class="nav-qun nav-link border"><a href="{{{url:rp+\'views/group/main.html\'} | appUrl : \'true\'}}" translate="personal_group">--><!--群组--></a></li> \
				                        <!-- <li class="nav-ask nav-link border"><a href="{{{url:rp+\'views/know/main.html\'} | appUrl : \'true\'}}" translate="know"></a></li> --> <!--问答--> \
        								<!-- <li class="nav-zhongxin nav-link border"><a href="{{ {url:rp+\'views/knowledge/main.html\'} | appUrl}}" translate="knowledge_center">--><!--知识中心--></a></li> \
				                        <!--<li class="nav-border"></li> -->\
				                    </ul> \
				                </div> \
				                <div class="cont-info-2" style="position:relative;z-index:2;" ng-show="showGradeMap || showPositionMap"> \
				                    <ul> \
        								<li ng-if="showGradeMap" class="nav-zhiji nav-link border"><a href="{{{url:rp+\'views/gradeMap/gradeMapList.html\'} | appUrl}}" translate="map_profession_develop"><!--职级发展学习地图--></a></li>\
        								<li class="nav-gangwei nav-link border" ng-show="showPositionMap"><a href="{{{url:rp+\'views/keyPosition/keyPositionList.html\'} | appUrl}}" translate="map_position_learning"><!--岗位学习地图--></a></li> \
        								<li class="nav-border"></li> \
				                    </ul> \
				                </div> \
				                <div class="cont-info-2 list-pic-4-pic"> \
				                    <a class="souso-tag-1" href="{{{url:rp+\'views/setUp/setMain.html\'} | appUrl}}" style="margin:0;"> \
				                        <span class="icon-nav-model"> \
				                              <i class="icon-nav-set"></i> \
				                        </span> \
				                        <div class="nav-box-biao" translate="setting"></div> <!--设置 --> \
				                     </a> \
				                    <a ng-if="appInd" class="souso-tag-1" href="javascript:void(0)" ng-click="openQRCode()" style="margin:0;float:right;"> \
				                        <span class="icon-nav-model"> \
				                              <i class="nav-lixian"></i> \
				                        </span> \
				                        <div class="nav-box-biao" translate="qiandao_offline"></div><!--离线签到--> \
				                     </a> \
				                    <a ng-if="!appInd" class="souso-tag-1" href="javascript:void(0)" ng-click="loginOut()" style="margin:0;float:right;"> \
				                        <span class="icon-nav-model"> \
				                              <i class="nav-quit"></i> \
				                        </span> \
				                        <div class="nav-box-biao" translate="exit"></div><!--退出--> \
				                     </a> \
				                </div> \
				            </div> \
				        </nav> \
        			</div>',
        link: function($scope, element, attrs) {
        	
        	if(!jQuery.mmenu){
        		element.append("<link type='text/css' rel='stylesheet' href='"+$scope.rp+"css/jquery.mmenu.css'/>");
            	element.append("<script type='text/javascript' src='"+$scope.rp+"lib/jquery/jquery.mmenu.min.js'></script>");
        	}
        	
        	$scope.toPersonPage = function(){
        		var usrId = $scope.user.enc_usr_ent_id;
        		var url = $scope.rp + "views/personal/personal.html?person=" + usrId;
        		javascript:clicked(url,true,'personal'+usrId);
        	}
        	
        	element.children("#nav").mmenu({
				slidingSubmenus: true
			});
        	
        	$scope.appInd = appInd;
        	
        	switch($scope.target){
        	case 'learning' :
        		var url = window.location.href;
        		if(url.indexOf("show=myCourse") != -1) {
        			$(".gps-learn").addClass("active");
        			$(".gps-learn-p").addClass("cur");
        		} else {
            		$(".gps-catalog").addClass("active");
            		$(".gps-catalog-p").addClass("cur");
        		}
        		break;
        	/*case 'exam' :
        		$(".gps-exam").addClass("active");
        		$(".gps-exam-p").addClass("cur");
        		break;*/
        	case 'my' :
        		$(".gps-user").addClass("active");
        		$(".gps-user-p").addClass("cur");
        		break;
        	case 'index' :
        		$(".gps-index").addClass("active");
        		break;
        	}
        	
        	//进入离线学习界面
			$scope.showOffline = function(){
				webview = plus.webview.getWebviewById("offline");
				if(webview == undefined){
					webview = plus.webview.create($scope.rp+"views/module/offline.html","offline",{scrollIndicator:'none',scalable:false});
				}
				webview.show('slide-in-right', 200);
			};
			
			$scope.openQRCode = function(){
				clicked($scope.rp+'views/barcode_scan.html');
			};
			var rp = $scope.rp;
			$scope.loginOut = function(){
				var url= '/app/login/out';
				modalService.modal(function($scope, $modalInstance) {
					$scope.modalText = 'set_login_out_tip';
					$scope.modalOk = function() {
						
						Ajax.post(url, {}, function(){
							
							/**
							 * 除了globalLang，其他都清除
							 */
							var storage = Store.getStorage();
							var globalLang = storage.getItem("globalLang");
							storage.clear();
							storage.setItem("globalLang",globalLang);
							
							window.sessionStorage.clear();
							
							backLogin(rp);
							
						});
					};
					$scope.modalCancel = function() {
						$modalInstance.dismiss('cancel');
					};
				});
			};
			
			//用户信息
			UserService.userInfo(function(data){
				$scope.serverHost = window.serverHost;
				if(data && data.regUser){
					$scope.user = data.regUser;
				}
			});
			
			$scope.approvalCount = 0;
			
			Ajax.post("/app/home/count", {isMobile : 1}, function(data){
				var count = data.approvalCount;
				$scope.approvalCount = count > 99 ? 99 : count;
			});
			
			var url = "/app/learningmap/getMapCount";
			Ajax.post(url,{},function(data){
				
				var gradeMapCont = data.gradeMapCont;//职级发展个数
				var SpecialTopic = data.SpecialTopic;//专题培训个数
				var positionMapCount = data.positionMapCount;//岗位学习地图个数
				
				//获取关键岗位的个数，如果个数为0，则隐藏菜单的【关键岗位】
				$scope.showPositionMap = (positionMapCount > 0);
				
				//获取专题培训的个数，如果个数为0，则隐藏菜单的【专题培训】
				$scope.showSpecialTopic = (SpecialTopic > 0);
				
				//获取职级发展学习地图的个数，如果个数为0，则隐藏菜单的【职级发展学习地图】
				$scope.showGradeMap = (gradeMapCont > 0);
				
				//获取直播的个数，如果个数为0，则隐藏菜单的【直播】
				$scope.showLive = (data.liveCont > 0);
				
				$scope.$emit('learningMapCount', data);
			});
			
        }
    };
}).directive("wzmNavigatorScroller",function(){//页面可滑动导航栏，目前集成的是iscroll插件
	return {
		template : '<div id="{{wrapperId}}" style="background: #fff;border-bottom: 1px solid #d9d9d9;"> \
						<div id="{{scrollerId}}" class="header-guide-info-2" style="height: 35px;border-bottom:0"> \
						    <ul> \
						        <li ng-repeat="i in items" ng-click="onItemClick(i,$index);">\
						        	<a ng-class="{\'select\':($index == selectedIndex)}" href="javascript:;" ng-bind="i[displayFieldName]"></a>\
						        </li>\
						    </ul>\
					    </div> \
				    </div>',
	    replace: true,
        scope : {
        	wrapperId : '@wrapperId',
        	scrollerId : '@scrollerId',
        	items : '=items',
        	displayFieldName : '@displayFieldName',
        	selectedIndex : '=selectedIndex',
        	itemClickCallback : '=itemClickCallback',
        	rp : '@rp'
        },
        link: function($scope, element, attrs) {
        	
        	var dynamicGetScrolWidth = function(){
        		
        		var de_a = document.createElement("a");
        		de_a.id = "temp";
        		document.body.appendChild(de_a);
        		
    			var scroller_width = 0;
    			var $_tempD = $("#temp");
    			angular.forEach($scope.items, function(item){
    				var title = item[$scope.displayFieldName];
    				$_tempD.html(title);
					scroller_width += $_tempD.width() + (10 * 2);
    			});
    		
    			document.body.removeChild(de_a);
    			
    			return scroller_width; 
    		}
        	
        	$scope.rp = $scope.rp || "../../";
    		$scope.wrapperId = $scope.wrapperId || "wrapperId";
    		$scope.scrollerId = $scope.scrollerId || "scrollerId";
    		$scope.selectedIndex = $scope.selectedIndex || 0; 
    		
    		if(!window.IScroll){
            	element.append("<script type='text/javascript' src='"+$scope.rp+"lib/iscroll/iscroll.js'></script>");
        	}
        	
    		$scope.onItemClick = function(item,index){
    			$scope.selectedIndex = index;
    			if($scope.itemClickCallback){
    				$scope.itemClickCallback(item);
    			}
    		}
    		
        	$scope.$watch('items',function(){
        		
        		if(!$scope.items){
        			return;
        		}
            	
            	var scroller_width = dynamicGetScrolWidth();
            	
    			$("#"+$scope.scrollerId).css('width', scroller_width + "px");
    			new IScroll("#"+$scope.wrapperId, { scrollX: true, scrollY: false, mouseWheel: false,click: true });
        	});
        	
        	$scope.$watch('selectedIndex',function(){});
        	
        }
	};
});
