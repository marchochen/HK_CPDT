$.include([], '../../');

$(function(){
	
	var learningHistoryModule = angular.module('learningHistory', ['globalCwn']);
	
	learningHistoryModule.controller("learningHistory",['$scope','Ajax','UserService','$window','$document','Store',function($scope,Ajax,UserService,$window,$document,Store){
		
		$scope.serverHost = serverHost;
		
		UserService.userInfo(function(data){
			$scope.user = data.regUser;
		});
	
		
		$scope.goBack = function(){
			
			var flag = Store.get("showLearningHistoryFromIndex");
			if("true" === flag){
				Store.set("showLearningHistoryFromIndex", false);
				javascript:clicked("../index.html",false,'home');
			}else{
				javascript:back();
			}
		}
		
		var touchListener = function(e){
			e.preventDefault();
		}
		
		document.addEventListener('touchmove', touchListener, false);
		
		Ajax.post("/app/personal/learningHistory/json",{},function(data){
			
			$scope.historyData = data.learningHistoryJson;
			
			$scope.courseLink = "";
			if($scope.historyData && $scope.historyData.ls_total_courses && $scope.historyData.ls_total_courses > 0){
				$scope.courseLink = "../learning/courseCatalog.html?show=myCourse";
			}else{
				$scope.courseLink = "../learning/courseCatalog.html?show=courseCatalog";
			}
			
			$window = angular.element($window);
			$document = angular.element($document);
			
			var scrollHandler = function(){
	            var windowBottom = $window.height() + $window.scrollTop();
	            var dh = $document.height();
	            if(dh <= windowBottom){
	            	if(scroller != null){
	            		clearInterval(scroller);
	            		document.removeEventListener("touchmove",touchListener);
	            		scroller = null;
	            	}
	            }
			}
			
			$window.on('scroll',scrollHandler);
            
            var scroller;
            
            $scope.$on('$destroy', function() {
            	if(scroller!=null){
            		clearInterval(scroller);
            		document.removeEventListener("touchmove",touchListener);
	            	scroller = null;
            	}
            	return $window.off('scroll', scrollHandler);
            });
            
            
            $(".panel-list-well-user-2").addClass("wiz-user-dong-yi");

            setTimeout(function(){
                $(".wiz-xian-1-1").addClass("wiz-xian-1 wiz-xian-yi");
            }, 500); 

            setTimeout(function(){
                $(".wiz-wenzi-1-1").fadeIn(500);
                $(".wiz-wenzi-1-1").addClass("wiz-wenzi-dong-yi");
            }, 1000);

            setTimeout(function(){
                $(".wiz-ball-1").fadeOut(500,function(){
                    $(".icon-model-2-shouye").fadeIn(500);
                    $(".icon-model-2-shouye").addClass("icon-model-dong-yi");
                });
            }, 1500);

            setTimeout(function(){
                $(".wiz-xian-1").css("opacity","0.3");
                $(".wiz-xian-2-2").addClass("wiz-xian-2 wiz-xian-yi");
            }, 2500); 

            setTimeout(function(){
                $(".wiz-wenzi-1-2").fadeIn(500);
                $(".wiz-wenzi-1-2").addClass("wiz-wenzi-dong-er");
                
            }, 3000);

            setTimeout(function(){
                $(".wiz-ball-2").fadeOut(500,function(){
                    $(".icon-model-2-xuexi").fadeIn(500)
                    $(".icon-model-2-xuexi").addClass("icon-model-dong-er");
                });
            }, 3500);

            setTimeout(function(){
                $(".wiz-icon-go-1").fadeIn(500);
            },4500);

            setTimeout(function(){
                $(".wiz-xian-2").css("opacity","0.3");
                $(".wiz-xian-3-3").addClass("wiz-xian-3 wiz-xian-yi");
                $(".icon-model-2-xuexi").addClass("icon-model-2-xuexi-2");
            }, 5500); 

            setTimeout(function(){
                $(".wiz-wenzi-1-3").fadeIn(500);
                $(".wiz-wenzi-1-3").addClass("wiz-wenzi-dong-san");
            }, 6000);

            setTimeout(function(){
                $(".wenzi-1-13").addClass("wiz-wenzi-dong-shi");
            },6500);

            setTimeout(function(){
                $(".wiz-ball-3").fadeOut(500,function(){
                    $(".icon-model-2-mulu").fadeIn(500)
                    $(".icon-model-2-mulu").addClass("icon-model-dong-san");
                });
            },7000); 
             
            setTimeout(function(){
                $(".wiz-icon-go-2").fadeIn(500,function(){
                	scroller = setInterval(function(){
                		window.scrollBy(0,4);
                	},100);
                });
            },7500); 

            setTimeout(function(){
                $(".wiz-xian-3").css("opacity","0.3");
                $(".wiz-xian-4-4").addClass("wiz-xian-4 wiz-xian-yi");
                $(".icon-model-2-mulu").addClass("icon-model-2-mulu-2");
            }, 8000); 

            setTimeout(function(){
                $(".wiz-wenzi-1-4").fadeIn(500);
                $(".wiz-wenzi-1-4").addClass("wiz-wenzi-dong-wu");
            }, 8500);

            setTimeout(function(){
                $(".wiz-ball-4").fadeOut(500,function(){
                    $(".icon-model-2-zan").fadeIn(500)
                    $(".icon-model-2-zan").addClass("icon-model-dong-liu");
                });
            }, 9000);

            setTimeout(function(){
                $(".wiz-wenzi-1-10").fadeIn(500);
                $(".wiz-wenzi-1-10").addClass("wiz-wenzi-dong-wu");
            },9500);

            setTimeout(function(){
                $(".wenzi-1-14").addClass("wiz-wenzi-dong-shi");
            },10000);

            setTimeout(function(){
                $(".wiz-xian-4").css("opacity","0.3");
                $(".wiz-xian-5-5").addClass("wiz-xian-5 wiz-xian-yi");
            },10500); 

            setTimeout(function(){
                $(".wiz-wenzi-1-5").fadeIn(100);
                $(".wiz-wenzi-1-5").addClass("wiz-wenzi-dong-qi");
            },11000);

            setTimeout(function(){
                $(".wenzi-1-16").addClass("wiz-wenzi-dong-shi");
            },11000);

            setTimeout(function(){
                $(".wiz-ball-5").fadeOut(500,function(){
                    $(".wiz-user-fans").fadeIn(500)
                    $(".wiz-user-fans").addClass("icon-model-dong-qi");
                    $(".wiz-wenzi-1-11").fadeIn(100);
                    $(".wiz-wenzi-1-11").addClass("wiz-wenzi-dong-qi");
                });
            }, 11500);

            setTimeout(function(){
                $(".wiz-xian-5").css("opacity","0.3");
                $(".wiz-xian-6-6").addClass("wiz-xian-6 wiz-xian-yi");
            },12000); 

            setTimeout(function(){
                $(".wiz-wenzi-1-6").fadeIn(500);
                $(".wiz-wenzi-1-6").addClass("wiz-wenzi-dong-ba");
            },12500);

            setTimeout(function(){
                $(".wiz-ball-6").fadeOut(500,function(){
                    $(".icon-model-2-wenti").fadeIn(500)
                    $(".icon-model-2-wenti").addClass("icon-model-dong-si");
                });
            },13000);

            setTimeout(function(){
                $(".wenzi-1-17").fadeIn(500,function(){
                    $(".wenzi-1-18").addClass("wiz-wenzi-dong-shi");
                });
            },13500);  

            setTimeout(function(){
                $(".wenzi-1-19").fadeIn(500,function(){
                    $(".wenzi-1-20").addClass("wiz-wenzi-dong-shi");
                });
            },14000);                   

            setTimeout(function(){
                $(".wiz-xian-6").css("opacity","0.3");
                $(".wiz-xian-7-7").addClass("wiz-xian-7 wiz-xian-yi");
            }, 14500); 

            setTimeout(function(){
                $(".wiz-wenzi-1-7").fadeIn(500);
                $(".wiz-wenzi-1-7").addClass("wiz-wenzi-dong-liu");
                $(".icon-model-2-wenti").addClass("icon-model-2-wenti-2");
            },15000);

            setTimeout(function(){
                $(".wiz-ball-7").fadeOut(500,function(){
                    $(".icon-model-2-zhishi").fadeIn(500)
                    $(".icon-model-2-zhishi").addClass("icon-model-dong-ba");
                });
            },15500);

            setTimeout(function(){
                $(".wiz-xian-7").css("opacity","0.3");
                $(".wiz-xian-8-8").addClass("wiz-xian-8 wiz-xian-yi");
            }, 16000); 

            setTimeout(function(){
                $(".wiz-wenzi-1-8").fadeIn(500);
                $(".wiz-wenzi-1-8").addClass("wiz-wenzi-dong-jiu");
            },16500);

            setTimeout(function(){
            	$(".icon-model-2-zhishi").addClass("icon-model-2-zhishi-2");
                $(".wiz-ball-8").fadeOut(500,function(){
                    $(".wiz-user-angle").fadeIn(500);
                    $(".wiz-user-angle").addClass("icon-model-dong-liu");
                    $(".wiz-wenzi-1-12").fadeIn(500);
                    $(".wiz-wenzi-1-12").addClass("wiz-wenzi-dong-qi");
                });
            },17000);

            setTimeout(function(){
                $(".wiz-zuji-footer").fadeIn(500);
                $(".wiz-zuji-footer").addClass("wiz-zuji-pic-foot");
            },19000);

            setTimeout(function(){
                $(".wiz-zuji-people").fadeIn(1000);
                $(".wiz-zuji-people").addClass("wiz-zuji-ren");
            },20000);

            setTimeout(function(){
                $(".wiz-xian-8").css("opacity","0.3");
                $(".btn-footer").fadeIn(500);
            },20500);
            
			
		});
		
		$scope.toCoursePage = function(){
			javascript:clicked($scope.courseLink,true);
		};
		
		
	}]);
	
});