angular.module("CatalogDirective", []).directive('catalogList',['$filter', 'Ajax', function($filter, Ajax) {
   var html = 
	 '<div class="guide-menu clearfix">'+
	 '  <ul class="guide-menu-list-1 panel-list-cont-13 guide-menu-click">'+
	 '      <li ng-repeat="item in firstCatalogs" class="guide-menu-list-1-newbg" ng-click="checkFirst(item.tnd_id)" ng-class="{\'select\' :item.select}">'+
	 '		<a href="javascript:;"  title="">{{item.tnd_title}}<em></em></a>'+
	 '		</li>'+
	 '  </ul> '+
	 '  '+
	 '  <div class="guide-menu-cont">'+
	 '       <ul class="guide-menu-list-2" >'+
	 '      	 <li ng-class="{\'noline\' : $index == secondCatalogs.length-1}" ng-repeat="item in secondCatalogs" ng-click="start(item.tnd_id)"><a href="javascript:;" title="">{{item.tnd_title}}</a></li>'+
	 '       </ul>'+
	 '  </div>  '+
	 '</div> ';
	return {
        restrict: 'A',
        template: html,
        scope : {
        	selectCallback : '=',
        	type : '@'
        },
        replace: true,
        link: function(scope, element, attrs) {

            scope.load = function(tndId, callback){
            	if(!tndId) tndId = 0;
                var params = {
                        cos_type : scope.type,
                    	tndId : tndId
                };
                var url = "/app/catalog/mobileJson";
                Ajax.post(url, params, callback);
            };
            //选中二级目录
            scope.start = function(tndId){
            	scope.load(tndId, function(data){
            		if(data && data.catalog){
            			//如果选中了二级目录，为空的话，一级目录就显示成二级目录
            			if(data.catalog.first && data.catalog.first.length < 2 && tndId > 0 ){
            				scope.firstCatalogs = scope.secondCatalogs;
            				scope.secondCatalogs = null;
            			} else {
                			//如果选中了一级目录
            				scope.firstCatalogs = data.catalog.first;
            			}
            			//二级目录
            			if(data.catalog.second && data.catalog.second){
            				scope.secondCatalogs = data.catalog.second;
            			}
            			var ctg = null;

            			if(tndId && tndId > 0){
            				angular.forEach(scope.firstCatalogs, function(val, key){
            					if(tndId == val.tnd_id){
            						val.select = true;
            						scope.selectCallback && scope.selectCallback(val);
            						ctg = val;
            					} else {
            						val.select = false;
            					}
            				});
            			}
						scope.$watch('firstCatalogs.select',function(){
						});
                        if(tndId==0 && !isHaveAll(scope.firstCatalogs)){
                            scope.firstCatalogs.unshift({
                                tnd_parent_tnd_id:0,
                                tnd_title : $filter('translate')('text_all'),
                                tnd_id:'',
                                tnd_type:'CATALOG',
                                flag : true,
                                select : true
                            });
                        }                      
	                	if(scope.secondCatalogs && scope.secondCatalogs.length > 0){
	                		element.siblings(".header-guide-lei").children(".header-guide-prev").unbind('click').bind('click', function(){
	                			if(ctg){
	                				scope.back(ctg.tnd_parent_tnd_id);
	                			}
	                		})
	                		element.find(".guide-menu-list-1").addClass("guide-menu-click");
		                	//element.find(".guide-menu-list-1").find("li").siblings().addClass("guide-menu-list-1-newbg").removeClass("guide-menu-list-1-bg");
	                		element.find(".guide-menu-list-1").siblings(".guide-menu-cont").show();
	                	    element.siblings(".header-guide-lei").children(".header-guide-prev").show();
							
							
	                	} else {
	                		element.find(".guide-menu-list-1").removeClass("guide-menu-click");
	                		element.find(".guide-menu-list-1").siblings(".guide-menu-cont").hide();
	                	    if(tndId > 0){
		                		element.find(".guide-menu-list-1").addClass("guide-menu-click");
	                	    }
	                	}
                		if(scope.secondCatalogs && scope.secondCatalogs.length < 1 && tndId > 0) {
	                	    element.find(".guide-menu-list-1").removeClass("guide-menu-click");
                		}
            		}
            	});
            }
            //加载第一次
            scope.start(0);
            
            //选中一级目录
            scope.checkFirst = function(tndId){
                if(tndId == ''){
                    angular.forEach(scope.firstCatalogs, function(val, key){
                        if(tndId == val.tnd_id){
                            val.select = true;
                            scope.selectCallback && scope.selectCallback(val);
                        } else {
                            val.select = false;
                        }
                    });
                    element.find(".guide-menu-list-1").removeClass("guide-menu-click");
                    element.find(".guide-menu-list-1").siblings(".guide-menu-cont").hide();
                    return;
                }
            	scope.load(tndId, function(data){
            		if(data && data.catalog){
           			
            			var ctg = null;
            			if(tndId && tndId > 0){
            				angular.forEach(scope.firstCatalogs, function(val, key){
            					if(tndId == val.tnd_id){
            						val.select = true;
            						scope.selectCallback && scope.selectCallback(val);
            						ctg = val;
            					} else {
            						val.select = false;
            					}
            				});
            			}
            			
						scope.$watch('firstCatalogs',function(){
						});
						
	            		scope.secondCatalogs = data.catalog.second;	            		
	                	if(scope.secondCatalogs && scope.secondCatalogs.length>0){
	                		if(ctg){
		                		element.siblings(".header-guide-lei").children(".header-guide-prev").unbind('click').bind('click', function(){
		                			scope.back(ctg.tnd_parent_tnd_id);
		                		})
	                		}

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
	                	    if(!ctg.tnd_parent_tnd_id){
	                	    	element.siblings(".header-guide-lei").children(".header-guide-prev").hide();
	                	    }
	                	}

            		}
            	});
            };
            
            scope.back = function(tndId){
            	scope.load(tndId, function(data){
            		if(data && data.catalog){

            			var ctg = null;
        				scope.secondCatalogs = data.catalog.second;
        				scope.firstCatalogs = data.catalog.first;

            			if(tndId && tndId > 0){
            				angular.forEach(scope.firstCatalogs, function(val, key){
            					if(tndId == val.tnd_id){
            						val.select = true;
            						scope.selectCallback && scope.selectCallback(val);
            					} else {
            						val.select = false;
            					}
            					if(tndId == val.tnd_parent_tnd_id){
            						ctg = val;
            					}
            					
            				});
            			}
						scope.$watch('firstCatalogs.select',function(){
						});
                        if(scope.firstCatalogs[0].tnd_parent_tnd_id==0 && !isHaveAll(scope.firstCatalogs)){
                            scope.firstCatalogs.unshift({
                                tnd_parent_tnd_id:0,
                                tnd_title : $filter('translate')('text_all'),
                                tnd_id:'',
                                tnd_type:'CATALOG',
                                flag : true
                            });
                        }
						if(element.siblings(".header-guide-lei").children(".header-guide-prev").is(":visible")){
							if(tndId < 1){
								element.siblings(".header-guide-lei").children(".header-guide-prev").hide();
							} 

							element.find(".guide-menu-list-1").removeClass("guide-menu-click");
							element.find(".guide-menu-list-1").siblings(".guide-menu-cont").hide();

						}
						
                		element.siblings(".header-guide-lei").children(".header-guide-prev").unbind('click').bind('click', function(){
                			if(ctg){
                                var url = "/app/catalog/getJson";
                                var params = {tndId:ctg.tnd_parent_tnd_id}
                                Ajax.post(url, params, function(data){
                                	if(data && data.catalog){
    	                				scope.back(data.catalog.tnd_parent_tnd_id);
                                	}
                                });
                			}
                		})

            		}
            	});
            }
        }
    };
}]).directive('hierarchyCatalog',function(Ajax){
	return {
		restrict: 'AE',
        template:  '<div> \
	        		   <div ng-repeat="c in catalogList" ng-if="catalogList && catalogList.length>0"> \
					      <h2 class="panel-title-1 panel-bg-product panel-num-15 panel-background"> \
					        <a class="panel-class-open padding-model-1" href="javascript:clicked(\'{{c.rp}}views/learning/catalogDetail.html?firstLevel={{c.id}}&cosType={{c.type}}\',true)" title="">{{c.name}}</a> \
					      </h2> \
					      <ul class="panel-title-22 panel-background" ng-if="c.children && c.children.length>0" ng-style="{\'border-bottom\':($last ? \'none\' : \'\')}"> \
					          <li ng-repeat="cc in c.children">•   <a href="javascript:clicked(\'{{cc.rp}}views/learning/catalogDetail.html?firstLevel={{c.id}}&secondLevel={{cc.id}}&cosType={{cc.type}}\',true)">{{cc.name}}</a></li> \
					      </ul> \
					   </div> \
        			   <div class="panel-list-boxdate" ng-if="!catalogList || catalogList.length<=0"><div class="panel-list-nodate">{{\'loader_no_data\' | translate}}</div></div><div class="panel-list-foot" ng-if="!catalogList || catalogList.length<=0"></div> \
        	       </div>',
        scope : {
        	rp : '@',
        	selectCallback : '=',
        	type : '@'
        },
        replace: true,
        link: function(scope, element, attrs) {
        	//获取课程目录层级结构
			var getHierarchyCatalog = function(cataLogList){
				var getParentByPid = function(pId,list){
					var result = {};
					for(var i in list){
						if(list[i].id == pId){
							return list[i];
						}
					}
					return result;
				};
				var result = [];
				var childTempArr = [];
				var item;
				if(cataLogList && cataLogList.length > 0){
					for(var i in cataLogList){
						item = cataLogList[i];
						item.rp = scope.rp;
						item.type = scope.type;
						if(item.pId === 0){
							result.push(item);
						}else{
							childTempArr.push(item);
						}
					}
					var childItem;
					for(var i in childTempArr){
						childItem = childTempArr[i];
						var parent = getParentByPid(childItem.pId,result);
						if(parent.children){
							parent.children.push(childItem);
						}else{
							parent.children=[];
							parent.children.push(childItem);
						}
					}
				}
				return result;
			};
			
			//1获取一级和二级课程
			var url = "/app/catalog/treeJson?cos_type="+scope.type;
			Ajax.post(url,{},function(data){
				scope.catalogList = getHierarchyCatalog(data.aeTreeNodeVoList);
			});
        }
	}
}).directive('catalogMenu',function(Ajax){
	return {
		restrict: 'AE',
		scope : {
        	rp : '@',
        	type : '@'
        },
        template: '<div id="header-guide"> \
        	      		<div class="guide-menu-bg mm-panel mm-opened mm-current" id="mm-m0-p0"> \
        	       			<div class="header-guide-title main-title-blue" translate="cos_catalog" style="border-bottom:0px;">{{(type==\'exam\' ? \'exam_catalog\' : \'cos_catalog\') | translate}}</div> \
        	                <div> \
        	                	<ul class="guide-menu-list-1"> \
        							<li class="guide-menu-list-1-bg" ng-repeat="c in catalogs"><a ng-click="toCatalogDetail(c)" href="javascript:void(0)" title="">{{c.tnd_title}}</a></li> \
        						</ul> \
        	                </div> \
        				</div> \
        		   </div>',
        replace: true,
        link: function(scope, element, attrs) {
        	
        	var tndId = 0;
            var params = {
               cos_type : scope.type,
               tndId : 0
            };
            var url = "/app/catalog/mobileJson";
            Ajax.post(url, params, function(data){
            	scope.catalogs = [];
            	if(data && data.catalog && data.catalog.first){
            		scope.catalogs = data.catalog.first;
            	}
            });
        	
        	if(!jQuery.mmenu){
        		element.append("<link type='text/css' rel='stylesheet' href='"+scope.rp+"css/jquery.mmenu.css'/>");
            	element.append("<script type='text/javascript' src='"+scope.rp+"lib/jquery/jquery.mmenu.min.js'></script>");
        	}
        	$(element).mmenu({
        		slidingSubmenus: true
            });
        	
        	scope.toCatalogDetail = function(catalog){
        		var api = $("#header-guide").data( "mmenu" );
				api.close();
				javascript:clicked(scope.rp+'views/learning/catalogDetail.html?firstLevel='+catalog.tnd_id+'&cosType='+scope.type,true);
        	}
        }
	}
});

function isHaveAll(obj){
    if(!obj) return false;
    for(var i in obj){
        if(obj[i].flag){
            return true;
        }
    }
    return false;
}