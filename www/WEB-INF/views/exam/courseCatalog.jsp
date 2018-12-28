<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>

<title></title>
<script type="text/javascript">
	var len = 80;
	var tcrId = 0;
	var tndId;	
	var course;
	var selectType;
	var itemType;
	var periods;	
	var moduleType = "exam";
	$(function() {
		course = $(".wbtabcont").table({
			url : '${ctx}/app/'+ moduleType +'/catalogCourseJson',
			colModel : colModel,
			rp : globalPageSize,
			showpager : 10,
			hideHeader : true,
			usepager : true
		})

		
		//课程类型 
		$("#course_type a").click(function() {
			itemType = $(this).attr("data");
			$("#course_type i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$(this).children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")
			$(course).reloadTable({
				url : '${ctx}/app/'+ moduleType +'/catalogCourseJson',
				params : {
					selectTcrId : tcrId,
					tndId : tndId,
					selectType : selectType,
					itemType : itemType,
					periods : periods
				}
			});
		})

		//时间段
		$("#periods a").click(function() {
			periods = $(this).attr("data");
			$("#periods i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$(this).children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")
			$(course).reloadTable({
				url : '${ctx}/app/'+ moduleType +'/catalogCourseJson',
				params : {
					selectTcrId : tcrId,
					tndId : tndId,
					selectType : selectType,
					itemType : itemType,
					periods : periods
				}
			});
		})

		$("#sortul li").click(function(){
			//alert($(this).index());
			var sortname = $(this).attr("id");
			var sortorder = $(this).attr("data");
			if(sortorder == 'asc') {
				$(this).attr("data","desc");
			} else if(sortorder == 'desc') {
				$(this).attr("data","asc");
			}
			//css 切换
			$(this).addClass("active").siblings().removeClass("active");
			if($(this).find("i").hasClass("fa-caret-up")){
				$(this).find("i").addClass("fa-caret-down").removeClass("fa-caret-up");
			} else {
				$(this).find("i").addClass("fa-caret-up").removeClass("fa-caret-down");
			}
			$(course).reloadTable({
				url : '${ctx}/app/'+ moduleType +'/catalogCourseJson',
				params : {
					selectTcrId : tcrId,
					tndId : tndId,
					selectType : selectType,
					itemType : itemType,
					periods : periods,
					sortname : sortname,
					sortorder : sortorder
				}
			});
		});
		
		$(".catalogTransfer").click(function(){
			
			$(".catalogTransfer").removeClass("fa-xian");
			$(".catalogContainer").hide();
			
			var ref = $(this).attr("ref");
			$("#"+ref).show();
			$(this).addClass("fa-xian");
		});
		
		$(".cwn-dropdown").hover(function(){
			$(this).toggleClass('open');
		});
		
	})


	var colModel = [ {
		format : function(data) {
			var is_desc_sub;
			var itm_desc_sub = data.itm_desc ? substr(data.itm_desc, 0, len) : "";
			if(getChars(data.itm_desc) > len){  
				itm_desc_sub+="...";  
            } 
			if(data.itm_desc == undefined || getChars(data.itm_desc) < len) {
				 is_desc_sub = 'display:none';
			}
			var p = {
				itm_desc_sub : itm_desc_sub.replace(/\r\n/g,"<br />").replace(/\n/g,"<br />"),
				is_desc_sub : is_desc_sub,
				title : data.itm_title,
				itm_desc : data.itm_desc,
				itm_id : data.itm_id,
				encItmId : wbEncrytor().cwnEncrypt(data.itm_id),
				itm_type : fetchLabel("exam_"+data.itm_type.toLowerCase()),
				time : Wzb.displayTime(data.itm_publish_timestamp, Wzb.time_format_ymd),
				itm_icon : data.itm_icon,
				itm_mobile_ind : data.itm_mobile_ind,
				is_compulsory : data.itd? data.itd.itd_compulsory_ind == '1' ? "<div class=wzb-bixiu><lb:get key='required'/></div>" : "" :"" 
			};
			return $('#courseTemplate').render(p);
		}
	} ];

	var setting = {
		view : {
			selectedMulti : false
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : zTreeOnClick
		}
	};
	
	function zTreeOnClick(event, treeId, treeNode) {
		tndId = treeNode.id;
		var p = {
			selectTcrId : tcrId,
			tndId : tndId,
			selectType : selectType,
			itemType : itemType,
			periods : periods
		};
		$(course).reloadTable({
			url : '${ctx}/app/'+moduleType+'/catalogCourseJson',
			params : p
		});
	}
	
	
	/**
	* 获取目录列表中的根节点（父节点id为0的）（获取的根节点包含孩子）
	*/
	function getRootNodes(nodeList){
		
		if(!nodeList || nodeList.length <=0){
			return [];
		}
		
		/**
		* 递归获取节点的孩子节点
		*/
		var getChildren = function(parent){
			var children = [];
			for(var i=0;i<nodeList.length;i++){
				var item = nodeList[i];
				if(item.pId == parent.id){
					children.push(item);
				}
			}
			parent.children = children;
			
			if(children.length == 0){
				return;
			}
			
			for(var i=0;i<children.length;i++){
				var item = children[i];
				getChildren(item);
			}
			
		}
		
		var result = [];
		
		for(var i=0;i<nodeList.length;i++){
			var item = nodeList[i];
			if(item.pId == 0){
				result.push(item);
				getChildren(item);
			}
		}
		
		return result;
	}
	
	
	//初始化 课程层级结构树
	var initHierarchyCatalog = function(aeTreeNodeVoList){
		//设置课程层级结构，默认最多显示5个，点击加载更多，展开其他
		var maxCount = 5;
		var rootNodes = getRootNodes(aeTreeNodeVoList);
		var nodesLength = rootNodes.length;
		
		$("#hierarchyCatalog .wzb-list-31").empty();
		$("#hierarchyCatalogMore .wzb-list-31").empty();
		$("#openMore").hide();
		$("#hideMore").hide();
		$("#hierarchyCatalogMore").hide();
		
		if(nodesLength <= maxCount){
			for(var i=0;i<nodesLength;i++){
				var html = $('#hierarchyCatalogTemplate').render(rootNodes[i]);
				$("#hierarchyCatalog .wzb-list-31").append(html);
			}
			$("#openMore").hide();//隐藏掉加载更多的按钮
		}else{
			for(var i=0;i<maxCount;i++){//append到默认展示的区域
				var html = $('#hierarchyCatalogTemplate').render(rootNodes[i]);
				$("#hierarchyCatalog .wzb-list-31").append(html);
			}
			
			for(var i=maxCount;i<nodesLength;i++){//append到【更多】的容器
				var html = $('#hierarchyCatalogTemplate').render(rootNodes[i]);
				$("#hierarchyCatalogMore .wzb-list-31").append(html);
			}
			
			$("#openMore").unbind("hover").unbind("click");
			$("#hideMore").unbind("hover").unbind("click");
			
			$("#openMore").hover(function () {//按钮滑动效果
            	$(this).css("background","#30bd89");
            	$(this).children().remove();
            	$(this).append("<img class='btn-run-down' src='${ctx}/static/images/jiantou_01.png'/>");
		    },function(){
		    	$(this).css("background","#fff");
		     	$(this).children().remove();
		       	$(this).append("<img class='btn-run-down' src='${ctx}/static/images/jiantou_02.png'/>");
		    }).click(function(){//点击隐藏【更多】
		        $(this).hide();
		        $("#hideMore").show();
		        $("#hierarchyCatalogMore").toggle();
		    });
			    
		    $("#hideMore").hover(function(){//按钮滑动效果
			    $(this).css("background","#30bd89");
			    $(this).children().remove();
			    $(this).append("<img class='btn-run-down' src='${ctx}/static/images/jiantou_011.png'/>");
			},function(){
			    $(this).css("background","#fff");
			    $(this).children().remove();
			    $(this).append("<img class='btn-run-down' src='${ctx}/static/images/jiantou_022.png'/>");
			}).click(function(){//点击展开【更多】
			    $(this).hide();
			    $("#openMore").show();
			    $("#hierarchyCatalogMore").toggle();
			});
			
		    $("#openMore").show();
		}
	}
	
	$(document).ready(function() {
		$.getJSON("${ctx}/app/catalog/treeJson?cos_type=exam&tcr_id=-1", function(result) {
			$.fn.zTree.init($("#zTree"), setting, result.aeTreeNodeVoList);
			
			initHierarchyCatalog(result.aeTreeNodeVoList);
			
		});
	});
</script>

<!-- template start -->
<script id="courseTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	  <dd>
		  <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}">
			   <div class="main_img">
					<img class="fwpic" src="{{>itm_icon}}">
					<div class="show">
						 <span class="imgArea">
							   <em><lb:get key="index_click_to_exam"/></em>
						 </span>
					</div>
			   </div>
			   {{>is_compulsory}}
		  </a>
	  </dd>
	  <dt>
		  <p><a class="wzb-link01" href="${ctx}/app/course/detail/{{>encItmId}}" title=""><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind == 'yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</a></p>
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999"><lb:get key="exam_type"/>：</span>{{>itm_type}}</div></div>
		  <div class="offheight clearfix"><span class="color-gray999"><lb:get key="course_publish_date"/>：</span>{{>time}}</div>
		  <p><span class="color-gray999"><lb:get key="exam_introduction"/>：</span><span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" ><span><lb:get key="click_down"/></span><i class="fa fa-angle-down"></i></a></p>
	  </dt>
</dl>
</script>

<script id="hierarchyCatalogTemplate" type="text/x-jsrender">
	        <ul>
	            <li class="wzb-list-31-li">
	                <div class="wzb-list-31-title">
	                    <span class="wzb-list-hang"></span>
	                    <a href="javascript:zTreeOnClick(null,null,{id:'{{>id}}'})">{{>name}}</a>
	                </div>
					{{if children && children.length > 0}}
	                <div class="wzb-title-13">
	                    <div class="wzb-title-13-title">
							{{for children}}
	                        	<div class="wzb-title-13-box">
	                           		<h4><a href="javascript:zTreeOnClick(null,null,{id:'{{>id}}'})">{{>name}}</a></h4>
									{{if children && children.length > 0}}
										{{for children}}
	                            			<span class="wzb-title-13-biao"><a href="javascript:zTreeOnClick(null,null,{id:'{{>id}}'})">{{>name}}</a></span>
										{{/for}}
									{{/if}}
	                        	</div>
							{{/for}}
	                    </div>
	                </div>
					{{/if}}
	            </li>
	        </ul>
</script>

<!-- template end -->
</head>
<body>

	<div class="xyd-wrapper">
		
        
		<div id="main" class="xyd-main clearfix">
			<div class="xyd-sidebar">
                <h3 class="wzb-title-4 skin-bg">
                	<lb:get key="exam_catalog"/>
                	<span class="wbx-xuexi-2 catalogTransfer" ref="treeCatalog"><i class="fa  fa-caret-up fa-xue"></i></span>
	            	<span class="wbx-xuexi-1 fa-xian catalogTransfer" ref="hierarchyCatalogContainer"><i class="fa  fa-caret-up fa-xue"></i></span>
                </h3>
                
                <span class="cwn-dropdown">
			        <button id="selectTcrBtn" class="btn btn-default butt" type="button">
			            <div id="tcr_text" class="tcr-text"><!-- 培训中心 --><lb:get key="traning_center_all"/></div>
			            <div class="select-img"><img src="${ctx}/static/images/wzb-select.png"></div>
			        </button>
			
			        <div class="cwn-dropdown-menu" style="top:25px; left:0;">
			            <a class="xyd-train-desc" href="javascript:;" title="<lb:get key='traning_center_all'/>" data="0"><lb:get key="traning_center_all"/></a>
					    <c:forEach items="${myTrainingCenter}" var="tc" varStatus="index">
						<a style="display:block;" class="xyd-train-desc" href="javascript:;" title="${tc.tcr_title }" data="${tc.tcr_id }" title="">${tc.tcr_title }</a> 
					    </c:forEach>
			        </div>
			    </span>
			    
			    <div id="hierarchyCatalogContainer" class="catalogContainer">
            
	            	<div id="hierarchyCatalog" class="wzb-list-31-box">
				    	<div class="wzb-list-31">
				        </div>
		            </div>
		            
		            <div id="hierarchyCatalogMore" style="display: none">
		            	<div class="wzb-list-31"></div>
		            </div>
		            
		            
		            <div class="wzb-btn-yuan btn-margin" id="openMore" style="background: rgb(255, 255, 255);">
		    			<img class="btn-run-down" src="${ctx}/static/images/jiantou_02.png">
		    		</div>
		    		
		    		<div class="wzb-btn-yuan btn-margin" id="hideMore" style="background: rgb(255, 255, 255);">
		    			<img class="btn-run-down" src="${ctx}/static/images/jiantou_022.png">
		    		</div>
	            
	            </div>
                
                <div class="catalogContainer" id="treeCatalog" style="display:none;margin-left:10px;">
	                <dl class="wzb-list-5">
	                    <dd>
	                         <div id="zTree" class="ztree" style="overflow-x:auto;padding-bottom:15px;min-height:0%; "></div>
	                    </dd>
	                </dl>
                </div>
                
                <div class="wzb-model-8">
                     <div class="wzb-title-2 skin-color"><lb:get key="condition"/></div>
                     
                     <dl class="wzb-list-4" id="course_type">
                         <dt><lb:get key="exam_type"/>：</dt>
                         <dd><a href="javascript:;" title="" data=""><i class="fa margin-right10 fa-circle skin-color"></i><lb:get key="status_all"/></a></dd>
                         <dd><a href="javascript:;" title="" data="selfstudy"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="exam_selfstudy"/></a></dd>
                         <dd><a href="javascript:;" title="" data="classroom"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="exam_classroom"/></a></dd>
                     </dl> 
                     
                     <dl class="wzb-list-4" id="periods">
                         <dt><lb:get key="start_time"/>：</dt>
                         <dd><a href="javascript:;" title="" data=""><i class="fa margin-right10 fa-circle skin-color"></i><lb:get key="not_limit"/></a></dd>
                         <dd><a href="javascript:;" title="" data="week"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="next_week"/></a></dd>
                         <dd><a href="javascript:;" title="" data="month"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="next_month"/></a></dd>
                         <dd><a href="javascript:;" title="" data="quarter"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="next_quarter"/></a></dd>
                     </dl> 
                </div>
            </div> <!-- xyd-sidebar End-->
			<script>
			$(function(){
				$(".xyd-train-desc").click(function(){
			
						$("#tcr_text").text($(this).text());
						
						tcrId = $(this).attr("data");
						$.getJSON("${ctx}/app/catalog/treeJson?cos_type=exam&tcr_id=" + tcrId, function(result) {
							//重新加载目录树，并清除目录的选择条件
	                    	tndId = 0;
							$.fn.zTree.init($("#zTree"), setting, result.aeTreeNodeVoList);
							initHierarchyCatalog(result.aeTreeNodeVoList);
						});
			
						var p = {
							selectTcrId : tcrId,
	               			selectType : selectType,
	               			itemType : itemType,
	               			periods : periods
	                	};
						
						$(course).reloadTable({
							url : '${ctx}/app/'+moduleType+'/catalogCourseJson',
							params : p
						});
			
						$(this).parents(".xyd-train-list").siblings().each(function(){
							$(this).find("dt").find("a").html($(this).find("dd").find(".xyd-train-desc:eq(0)").attr("title"))
						})
			
						$(this).parents(".xyd-train-list").find("dt").find("a").html($(this).attr("title"));
						
				});
			
				$(".xyd-train-list").hover(function(){
					$(this).find(".xyd-train-info").toggle();
				});
			})
			</script>
            
			<div class="xyd-article">
                		
        
	            <div role="tabpanel" class="wzb-tab-2">
	                <!-- Nav tabs -->
	                <ul class="nav nav-tabs" role="tablist" id="sortul">
	                    <span><lb:get key="exam_list"/></span>
	                    <li id="itm_publish_timestamp" data="asc" role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab"><lb:get key="course_publish_date"/> <i class="fa fa-caret-down"></i></a></li>
	                    <li id="itm_title" data="asc" role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="global_title"/> <i class="fa fa-caret-down"></i></a></li>
	                </ul>
	                
	                <!-- Tab panes -->
	                <div class="tab-content">
	                     <div class="wbtabcont">
	                
	                     </div>
	                </div>
	            </div> <!-- wzb-tab-2 end -->
            
			</div> <!-- xyd-article End-->

		</div>
	</div>
	<!-- xyd-wrapper End-->


</body>
</html>