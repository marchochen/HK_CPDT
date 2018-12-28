<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
<script type="text/javascript">
	var showMonth = 4;
	var baseMonth = 1;
	var startDate;
	var tempDate;
	var type = "M";
	var appStatus, isCompulsory;

	$(function() {
		$("#menu").removeClass("xyd-fixbox");
		$("#menu>div").removeClass("xyd-fixcut"); //菜单修改

		$(".xyd-learn-content .xyd-learn-title").click(function() {
			$(this).find("span i").removeClass("fa-plus-square").removeClass("fa-minus-square").toggleClass(
			$(this).next(".loadInfo").is(":hidden") ? 'fa-minus-square': 'fa-plus-square');

			$(this).next(".loadInfo").toggle();
		});

		$.templates({
			divTemplate : '<em>{{>text}}</em>',
			iconTemplate : '<img class="margin-left10" src="{{>src}}" alt="{{>title}}" title="{{>title}}" />',
		});

		$(".nav-tabs>li").click(function() {
			$(this).addClass("active").siblings().removeClass("active");
			type = $(this).attr("data");
			drawMap('', type);
		})

		drawMap();

		$(".xyd-learn-next").click(function() {
			//$(this).parent("div").addClass("skin-bg");
			//$(".fa-angle-left").parent("div").removeClass("skin-bg");
			if (startDate == undefined || startDate == '')
				startDate = new Date();
			if (type == "M") {
				startDate.setMonth(startDate.getMonth() + 5);
				// 			if(startDate.getMonth() == 0){
				// 				startDate.setMonth(1);
				// 			}
			} else {
				startDate.setFullYear(startDate.getFullYear() + 1);
				startDate.setMonth(1);
			}
			drawMap(startDate, type)
		})

		$(".xyd-learn-prev").click(function() {
			//$(this).parent("div").addClass("skin-bg");
			//$(".fa-angle-right").parent("div").removeClass("skin-bg");
			if (startDate == undefined || startDate == '')
				startDate = new Date();
			if (type == "M") {
				startDate.setMonth(startDate.getMonth() - 3);
				// 			if(startDate.getMonth() == 0){
				// 				startDate.setMonth(1);
				// 			}
			} else {
				startDate.setFullYear(startDate.getFullYear() - 1);
				startDate.setMonth(1);
			}
			drawMap(startDate, type)
		})

		//课程类型
		$(".itemType").click(function() { 
			var itemType = $(this).attr("data");
			
			$(".learninfo a").removeClass("cur");
			$(this).addClass("cur");

			drawMap(tempDate, type, itemType, "", isCompulsory, appStatus);
		})

		$(".isExam").click(function() {
			var isExam = $(this).attr("data");
			
			$(".learninfo a").removeClass("cur");
			$(this).addClass("cur");
			drawMap(tempDate, type, "", isExam, isCompulsory, appStatus);
		})

		$(".appStatus").click(
				function() {
					appStatus = $(this).attr("data");
					isCompulsory = "";
					$(".learninfo a").removeClass("cur")
					$(this).addClass("cur").parent().siblings().find("a")
							.removeClass("cur");
					drawMap(tempDate, type, "", "", "", appStatus);
				})

		$(".isCompulsory").click(
				function() {
					isCompulsory = $(this).attr("data");
					appStatus = "";
					$(this).addClass("cur").parent().siblings().find("a")
							.removeClass("cur");
					drawMap(tempDate, type, "", "", isCompulsory, "");
				})

				
		//导航顶部贴片
		if ($(".xyd-fixbox").offset() !== undefined) {
			var v_modTop = $(".xyd-fixbox").offset().top;
			var on_scroll = function() {
				var top = $(document).scrollTop();

				if (top > v_modTop) {
					if ('undefined' == typeof (document.body.style.maxHeight)) { //兼容iE6
						$('.xyd-fixcut').addClass('abs');
						$('.xyd-fixcut').css('top', $(window).scrollTop());
					} else {
						$('.xyd-fixcut').addClass('fixed');
					}
				} else {
					$('.xyd-fixcut').removeClass('abs');
					$('.xyd-fixcut').removeClass('fixed');
				}
			}
			$(window).scroll(on_scroll);
			on_scroll();
		}

	})

	function drawMap(date, type, itemType, isExam, isCompulsory, appStatus) {
		var dateStr; 
		if (date != undefined && date != '') {
			dateStr = date.getFullYear() + "-" + date.getMonth() + "-01"
					+ " 00:00:00";
			tempDate = date;
		}
		$.ajax({
			url : "${ctx}/app/course/mapJson",
			type : 'post',
			dataType : 'json',
			async : false,
			data : {
				start : dateStr,
				type : type,
				itemType : itemType,
				isExam : isExam,
				isCompulsory : isCompulsory,
				appStatus : appStatus,
			},
			success : function(result) {
				startDate = newDate(result.start);
				type = result.type;
				if (type == 'M') {
					var weekHtml = [];
					var week_en = text_label['map_week_en'];
					var week_cn = text_label['map_week_cn'];
					for (var i = 1; i <= 5; i++) {
						weekHtml.push($.render.divTemplate({
							text : week_en + i + week_cn
						}));
					}
					var tempDate = newDate(result.start);
					$("#learnWeekArea").empty();
					tempDate.setMonth(tempDate.getMonth() - 1)
					for (var i = 0; i < showMonth; i++) {
						;
						tempDate.setMonth(tempDate.getMonth() + 1);
						var weekDate = tempDate.getFullYear() + "-"
								+ (Number(tempDate.getMonth()) + 1);
						$("#learnWeekArea").append(
								$("#learnWeekTemplate").render({
									weekDate : weekDate
								}));
						$("#learnWeekArea .xyd-learn-list:last").find(".xyd-learn-week")
								.append(weekHtml);
					}
				} else {
					$("#learnWeekArea").empty();
					var monthHtml = [];
					for (var i = 1; i <= 12; i++) {
						var month_num = "map_month_" + i;
						monthHtml.push($.render.divTemplate({
							text : text_label[month_num]
						}));
					}
					$("#learnWeekArea").append($("#learnYearTemplate").render({
						year : startDate.getFullYear()
					}));
					$("#learnWeekArea .xyd-learn-biao:last").find(".xyd-learn-yue")
							.append(monthHtml);

				}

				if (result.hashMap != undefined) {
					$("#groupDetail").empty();
					$("#gradeDetail").empty();
					$("#positionDetail").empty();
					$("#supDetail").empty();
					$("#tadmDetail").empty();
					$("#otherDetail").empty();
				}

				var groups = result.hashMap.groups;
				if (groups != undefined) {
					showItemHtml($("#groupDetail"), groups);
				}

				var grades = result.hashMap.grades;
				if (grades != undefined) {
					showItemHtml($("#gradeDetail"), grades);
				}

				var positions = result.hashMap.positions;
				if (positions != undefined) {
					showItemHtml($("#positionDetail"), positions);
				}

				var sups = result.hashMap.sups;
				if (sups != undefined) {
					showItemHtml($("#supDetail"), sups);
				}

				var tadms = result.hashMap.tadms;
				if (tadms != undefined) {
					showItemHtml($("#tadmDetail"), tadms);
				}

				var others = result.hashMap.others;
				if (others != undefined) {
					showItemHtml($("#otherDetail"), others);
				}

				$('[data-toggle="tooltip"]').tooltip();
				$('.js-popover').popover();

				$(".xyd-learn-prev").css("height", $(".xyd-learn-content").outerHeight(true));
				$(".xyd-learn-next").css("height", $(".xyd-learn-content").outerHeight(true));
			}
		})
	};

	function showItemHtml(obj, items) {
		$.each(items,function(i, val) {
							$.extend(val,{width : val.mapVo ? val.mapVo.width : 0,
										 marginLeft : val.mapVo ? val.mapVo.left : 0,
										 encItmId : wbEncrytor().cwnEncrypt(val.itm_id),
										 itm_eff_end_datetime : val.itm_eff_end_datetime ? val.itm_eff_end_datetime : '不限'
							})
							var itemHtml = $("#itemDescTemplate").render(val);
							$(obj).append(itemHtml);
							var color = "xyd-learn-orange";
							if (val.itm_type == "CLASSROOM") {
								if (val.itm_exam_ind == 1) {
									color = "xyd-learn-navy";
								} else {
									color = "xyd-learn-green";
								}
							} else if (val.itm_type == "SELFSTUDY") {
								if (val.itm_exam_ind == 1) {
									color = "xyd-learn-blue";
								} else {
									color = "xyd-learn-yellow";
								}
							} else if (val.itm_type == "INTEGRATED") {
								color = "xyd-learn-orange";
							}

							$(obj).find(".xyd-learn-info:last").find(".xyd-learn-you")
									.find("span:eq(0)").removeClass(
											"xyd-learn-orange").addClass(color);

							//icon
							var app = val.app;
							/* 		i  3  进行中
							 c  1 已完成
							 f  2 未完成
							 p  1 已通过
							 w  2 已放弃 */
							var imgIndex = 2;
							var iconTitle = fetchLabel('status_inprogress');//'进行中';
							if (app != undefined) {
								if (app.app_status == 'C') {
									imgIndex = 1;
									iconTitle = fetchLabel('status_completed');//'已完成';
								} else if (app.app_status == 'F'
										|| app.app_status == 'W') {
									imgIndex = 2;
									iconTitle = fetchLabel('status_fail');//'未完成';
								} else if (app.app_status == 'I') {
									imgIndex = 3;
								}
							}else{
								var imgIndex = 2; 
								iconTitle = fetchLabel('status_fail');//'未完成';
							}
							var iconHtml = $.render.iconTemplate({
								src : "${ctx}/static/images/learn0" + imgIndex
										+ ".png",
								title : iconTitle
							});
							if (val.itd && val.itd.itd_compulsory_ind == 1) {
								iconHtml += $.render.iconTemplate({
									src : "${ctx}/static/images/learn04.png",
									title : fetchLabel('required')
								}) //'必修'
							}

							$(obj).find(".xyd-learn-info:last").find(".iconArea")
									.html(iconHtml);
						})
	}
</script>

<!-- template start -->
<script id="learnWeekTemplate" type="text/x-jsrender">

<div class="xyd-learn-list">
     <div class="xyd-learn-year">
		{{>weekDate}}	
	 </div>
     <div class="xyd-learn-week">
	</div>
</div>
</script>
<script id="learnYearTemplate" type="text/x-jsrender">
<div class="xyd-learn-biao">
     <div class="xyd-learn-nian">
          {{>year}}
     </div>
     
     <div class="xyd-learn-yue">

    </div>
</div>



</script>
<script id="itemDescTemplate" type="text/x-jsrender">
<div class="xyd-learn-info">
     <div class="xyd-learn-zuo">
	 	<a class="wzb-link03" href="${ctx}/app/course/detail/{{>encItmId}}{{if app}}?tkhId={{>app.app_tkh_id}}{{/if}}" title="{{if parent}}{{>parent.itm_title}} > {{/if}}{{>itm_title}}">
			{{if parent}}
	 			{{>parent.itm_title}} >
			{{/if}}
			{{>itm_title}}
		</a>
	</div>

     <div class="xyd-learn-you">
		<span class="xyd-learn-load xyd-learn-orange" data-toggle="tooltip" data-placement="top" title="{{>itm_eff_start_datetime}} <lb:get key="time_to"/> {{>itm_eff_end_datetime}}" style="width:{{>width}}px; margin-left:{{>marginLeft}}px;">
		</span>
     	<span class="iconArea">
			 <img class="" src="${ctx}/static/images/learn01.png" alt="" />
	     	<img class="" src="${ctx}/static/images/learn04.png" alt="" />
	 	</span>
     </div>
</div>

</script>

<!-- template end -->
</head>
<body>

	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
			<div class="wzb-model-13">
				<div class="wzb-title-10">
					<!-- 我的学习地图 -->
					<lb:get key="map_my_map" />
				</div>

				<div role="tabpanel" class="wzb-tab-5 margin-top15">
					<div class="xyd-learn xyd-fixbox">
						<div class="xyd-fixcut">
							<ul class="nav nav-tabs" role="tablist">
								<li role="presentation" class="active" data="M"><a href="#review"
									aria-controls="review" role="tab" data-toggle="tab">
										<!--本月任务 -->
										<lb:get key="map_month_task" />
								</a></li>
								<li role="presentation" data="Y"><a href="#score"
									aria-controls="score" role="tab" data-toggle="tab">
										<!--本年任务 -->
										<lb:get key="map_year_task"/>
								</a></li>
							</ul>
							

							<dl class="xyd-learn-tool">
								<dd>
									<a href="javascript:;" class="cur appStatus" data="">
										<!--全部 -->
										<lb:get key="status_all" />
									</a>
								</dd>
								<dd class="xyd-learn-01">
									<a href="javascript:;" class="appStatus" data="completed">
										<!--已完成 -->
										<lb:get key="status_completed" />
									</a>
								</dd>
								<dd class="xyd-learn-02">
									<a href="javascript:;" class="appStatus" data="notcompleted">
										<!--未完成 -->
										<lb:get key="status_fail" />
									</a>
								</dd>
								<dd class="xyd-learn-03">
									<a href="javascript:;" class="appStatus" data="inprogeress">
										<!--进行中 -->
										<lb:get key="status_inprogress" />
									</a>
								</dd>
								<dd class="xyd-learn-04">
									<a href="javascript:;" class="isCompulsory" data="1">
										<!--必修课 -->
										<lb:get key="required" />
									</a>
								</dd>
							</dl>

							<div class="xyd-learn-tool learninfo">
								<a class="wzb-link03 itemType" href="javascript:;" data="selfstudy">
									<!--网上课程-->
									<lb:get key="selfstudy" />
								</a><span class="xyd-learn-load xyd-learn-yellow"></span> <a
									class="wzb-link03 itemType" href="javascript:;" data="classroom">
									<!--面授课程 -->
									<lb:get key="classroom" />
								</a><span class="xyd-learn-load xyd-learn-green"></span> <a
									class="wzb-link03 itemType" href="javascript:;" data="integrated">
									<!--项目式培训 -->
									<lb:get key="integrated" />
								</a><span class="xyd-learn-load xyd-learn-orange"></span> <a
									class="wzb-link03 isExam" href="javascript:;" data="onLine">
									<!--在线考试 -->
									<lb:get key="exam_selfstudy" />
								</a><span class="xyd-learn-load xyd-learn-blue"></span> <a
									class="wzb-link03 isExam" href="javascript:;" data="offLine">
									<!--离线考试 -->
									<lb:get key="exam_classroom" />
								</a><span class="xyd-learn-load xyd-learn-navy"></span>
							</div>

							<div class="xyd-learn-box clearfix">
								<div class="xyd-learn-left">
									<!--任务名称 -->
									<lb:get key="map_task_name" />
								</div>

								<div class="xyd-learn-right" id="learnWeekArea">

								</div>
								<!-- xyd-learn-right End-->
							</div>
						</div>
					</div>
					<!-- xyd-fixbox End-->

					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="review">
							<div class="xyd-learn-content">
								<div class="xyd-learn-prev"
									style="height: 1085px; display: block;"></div>
								<div class="xyd-learn-next"
									style="height: 1085px; display: block;"></div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i><!--部门推荐 --><lb:get key="recommend_group"/></span>
								</div>
								
								<div class="loadInfo" id="groupDetail">
	                  			</div>
								
								<div class="loadInfo" id="groupDetail">
	 
	                  			</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i><!--职级推荐 --><lb:get key="recommend_grade"/></span>
								</div>
								<div class="loadInfo" id="gradeDetail">
	
	                  			</div>


								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i><!--岗位推荐 --><lb:get key="recommend_position"/></span>
								</div>
								<div class="loadInfo" id="positionDetail">
	 
	                 			 </div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i><!--上司推荐 --><lb:get key="recommend_sup"/></span>
								</div>
								
								<div class="loadInfo" id="supDetail">
	                  			</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i><!--管理员推荐 --><lb:get key="recommend_tadm"/></span>
								</div>
								<div class="loadInfo" id="tadmDetail">
	 
	                  			</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i><!--自选课程 --><lb:get key="recommend_myself"/></span>
								</div>
								
								<div class="loadInfo" id="otherDetail">
	
	                  			</div>
							</div>
						</div>
						
						
						<div role="tabpanel" class="tab-pane" id="score">
							<div class="xyd-learn-content">
								<div class="xyd-learn-prev"
									style="height: 1085px; display: block;"></div>
								<div class="xyd-learn-next"
									style="height: 1085px; display: block;"></div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-yellow"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 78px;"
												data-original-title="2014-01-18至2014-03-25"></span>
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-green"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 150px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-01.png"
												alt="已完成">
										</div>
									</div>
								</div>


								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-blue"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 90px; margin-left: 52px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-02.png"
												alt="未完成">
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-orange"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 256px; margin-left: 82px;"
												data-original-title="2014-01-18至2014-03-25"></span>
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-navy"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 190px; margin-left: 112px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-03.png"
												alt="进行中"><img class="margin-left10"
												src="images/xyd-learn-04.png" alt="必修">
										</div>
									</div>

									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-yellow"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 65px;"
												data-original-title="2014-01-18至2014-03-25"></span>
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-navy"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 300px; margin-left: 182px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-01.png"
												alt="已完成">
										</div>
									</div>

									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-blue"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 78px;"
												data-original-title="2014-01-18至2014-03-25"></span>
										</div>
									</div>

									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-green"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 178px; margin-left: 122px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-03.png"
												alt="进行中">
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-orange"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 190px; margin-left: 282px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-01.png"
												alt="已完成"><img class="margin-left10"
												src="images/xyd-learn-02.png" alt="未完成">
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-yellow"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 230px; margin-left: 92px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-03.png"
												alt="进行中">
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-green"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 48px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-04.png" alt="必修">
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-orange"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 169px; margin-left: 252px;"
												data-original-title="2014-01-18至2014-03-25"></span>
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-blue"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 250px; margin-left: 292px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-02.png"
												alt="未完成"><img class="margin-left10"
												src="images/xyd-learn-03.png" alt="进行中">
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-green"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 180px; margin-left: 112px;"
												data-original-title="2014-01-18至2014-03-25"></span>
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-navy"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 68px;"
												data-original-title="2014-01-18至2014-03-25"></span>
										</div>
									</div>
								</div>

								<div class="xyd-learn-title">
									<span><i class="fa skin-color fa-minus-square"></i>22222公司事业部</span>
								</div>
								<div class="xyd-learn-main">
									<div class="xyd-learn-info">
										<div class="xyd-learn-zuo">
											<a class="wzb-link03" href="###" title="信息管理部规章制度">信息管理部规章制度</a>
										</div>
										<div class="xyd-learn-you">
											<span class="xyd-learn-load xyd-learn-blue"
												data-toggle="tooltip" data-placement="top" title=""
												style="width: 278px; margin-left: 182px;"
												data-original-title="2014-01-18至2014-03-25"></span><img
												class="margin-left10" src="images/xyd-learn-01.png"
												alt="已完成"><img class="margin-left10"
												src="images/xyd-learn-02.png" alt="未完成"><img
												class="margin-left10" src="images/xyd-learn-03.png"
												alt="进行中">
										</div>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
				<!-- wzb-tab-5 End-->

			</div>

		</div>
		<!-- xyd-main End-->
	</div>



</body>

</html>