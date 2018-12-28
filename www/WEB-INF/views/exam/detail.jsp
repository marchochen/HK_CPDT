<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.dialogue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/validate.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>

<script type="text/javascript" src="${ctx}/js/wb_report.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_module.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_assignment.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_usergroup.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_certificate.js"></script>

<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cpd_${lang}.js"></script>

<title></title>
<script type="text/javascript">

var module_lst = new wbModule;
var ass = new wbAssignment;
var rpt = new wbReport;
usr = new wbUserGroup;
app = new wbApplication;
cert = new wbCertificate;

var messages;
var itmId = '${itmId}';
var tkhId = '${param.tkhId}';		//js getParam
var sns = new Sns();
//var isOnline = true;	//
var isClass = false;
var isOffline = false;
var isIntegrated = false;
var cpdIsEnd = false;
var nowDate = new Date();
var allResult;

var ua = navigator.userAgent.toLowerCase();
var isMobile = (
(ua.indexOf("android") > -1)
|| (ua.indexOf('iphone')>-1)
|| (ua.indexOf('ipod')>-1)
|| (ua.indexOf('ipad')>-1)
|| (ua.indexOf('Windows Phone')>-1)
|| (ua.indexOf('BlackBerry')>-1)
|| (ua.indexOf('webOS')>-1)
);


if(isMobile == null || isMobile == 'undefined'){
	isMobile = false;
}

$(function(){
	var detailUrl = "${ctx}/app/course/detailJson/" + itmId;
	if(tkhId != undefined && tkhId != ''){
		detailUrl += "?tkhId=" + tkhId
	}
	$.getJSON(detailUrl, function(result) {
 	//	try{
 			var app = result.app;
			var curDate = result.curDate;
			var item = result.item;
			var rcov = result.courseEvaluation;
            var hasCPDFunction = result.hasCPDFunction;
            var aeAttendance = result.aeAttendance;
            var att_create_timestamp = result.att_create_timestamp;
            if(hasCPDFunction && null != result.hasCPDColumn && result.hasCPDColumn.length > 0){
            	hasCPDFunction = true;
            }else{
            	hasCPDFunction = false;
            }
             //CPD时数
            if(hasCPDFunction){
                var searchTb;
                var itm_id =item.itm_id;
                    parent_itm_id = 0;
                    $.ajax({  
                        type:'post',      
                        url:'${ctx}/app/admin/learnerCPDHours/listJson',  
                        data:{"itm_id":itm_id,
                              "parent_itm_id":parent_itm_id},  
                        dataType:'json',
                        success:function(data){  
                                if(undefined != data.aeItemCPDItem){
                                    if(!isClass){
                                        if(undefined != data.end_date && data.end_date!=''){
                                            var end_date = new Date(data.end_date.replace(/\-/g, "\/"));  
                                            if(nowDate > end_date){
                                                cpdIsEnd=true;   //当前时间大于获取CPT/D时数截止日期，现在学习则无法获得CPD时数
                                            }
                                        }
                                   }
                                } 
                        }
                        });  
            } 
             
			allResult = result;
 			//定义模板
 			$.templates({
				navTemplate: '<li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">{{>name}}</a></li>',
				tableTemplate : '<table class="wbtable wzb-table-three">{{>text}}</table>',
				trTemplate : '<tr>{{>text}}</tr>',
				tdTemplate : '<td>{{>text}}</td>',
				tdRightTemplate : '<td class="text-right">{{>text}}</td>',
				btnTemplate : '<input type="button" onclick="{{>href}}" value="{{>name}}" class="btn wzb-btn-yellow"></input>',
				moduleField : '<div class="col-xs-6 moduletext">{{>text}}</div>',
				moduleBtnField : '<div class="col-xs-6 div-btn"></div>',
				btnIconTemplate : '<input title="{{>label}}" type="button" onclick="{{>handler}}" class="btn wzb-btn-orange wzb-btn-big" value="{{>label}}" name="frmSubmitBtn">'
			});
			//课程详情
			if(item.itm_run_ind == 1) {
				isClass = true;
			} else if(item.itm_integrated_ind == 1){
				isIntegrated = true;
			} else if(item.itm_create_run_ind == 1){
				isOffline = true;
			}

			//tab 创建选项卡
			var navs = [];
			if(isOffline){
				navs.push('detail_nav_timetable');
				navs.push('detail_exam_detail');
				navs.push('exam_class');
                if(hasCPDFunction){
                	navs.push('detail_course_cpdHours');
                }
                
			} else {
				if(isIntegrated){//如果是项目式培训，标题改为课程列表
					navs.push('course_list');
				}else{
					navs.push('detail_online_content');
				}
				navs.push('detail_nav_timetable');
				
				if(isClass){
					navs.push('detail_exam_detail');
                    if(hasCPDFunction){
                    	navs.push('detail_course_cpdHours');
                    }
                    
					navs.push('detail_class_notice');
				} else {
					navs.push('detail_exam_detail');
                    if(!isIntegrated && hasCPDFunction){
                        navs.push('detail_course_cpdHours');
                    }
					navs.push('detail_exam_notice');
				}
				if(app && app.app_status
						&& app.app_status != 'Waiting'
							&& app.app_status != 'Withdrawn'
								&& app.app_status != 'Rejected'){
					navs.push('detail_my_status');
				}
			}
	   	 	$.each(navs, function(i,val){
		   	 	var p = {
					name : fetchLabel(val)
				}
	   	 		$("#nav1").append($.render.navTemplate(p));
			});

	   		//报名限期
			item.itm_appn_start_datetime = Wzb.displayTime(item.itm_appn_start_datetime, Wzb.time_format_ymdhm);
			item.itm_appn_end_datetime = item.itm_appn_end_datetime != '9999-12-31 23:59:59'?
					Wzb.displayTime(item.itm_appn_end_datetime, Wzb.time_format_ymdhm) : fetchLabel('course_date_nolimit');

			//内容限期
			item.itm_content_eff_start_time = isClass ?
					 Wzb.displayTime(item.itm_content_eff_start_time, Wzb.time_format_ymd) : fetchLabel('course_signup_date');
			item.itm_content_eff_end_time = item.itm_content_eff_end_time ?
					 Wzb.displayTime(item.itm_content_eff_end_time, Wzb.time_format_ymd) : fetchLabel('course_date_nolimit');
		 	if(item.itm_content_eff_duration){
				//item.itm_content_eff_end_time = item.itm_content_eff_duration + fetchLabel('course_end_day');
			}
			//catalogs			//课程目录
			var catalogs = result.catalogs;
			var catalogStr = [];
			$.each(catalogs, function(i, val){
				if(val && val.tnd_title) {
					catalogStr.push(val.tnd_title);
				}
			})
			item.catalogs = catalogStr.join(",");

			//课程介绍
			
			item.itm_desc = replaceVlaue(item.itm_desc);
			
			var is_desc_sub;
			len = 134;
			var itm_desc_sub = item.itm_desc ? substr(item.itm_desc, 0, len) : "";
			if(getChars(item.itm_desc) > len){  
				itm_desc_sub+="...";  
            }
			if(item.itm_desc == undefined || getChars(item.itm_desc) < len) {
				 is_desc_sub = 'display:none';
			}
			var instructors = result.instructors;
			var instructorStr = [];
			if(instructors != undefined && instructors != '') {
				$.each(instructors, function(i,val){
					if(val.user != undefined && val.user.usr_display_bil != undefined) {
						instructorStr.push(val.user.usr_display_bil);
					}
				});
			} else {
				//$("#instructorComment").hide();
				//$("#nav2 li:last").hide();
			}
			//学习状态
			var appStatus,appStatusLabel,appPercent;
			if(result.courseEvaluation && result.courseEvaluation.cov_status) {
				appStatus = result.courseEvaluation.cov_status
			} else if(app) {
				appStatus = app.app_status;
			}
			
			//报名日期
			if(aeAttendance == null){
				$("#ishidden").html($("#enrollment").render(aeAttendance));
			}else{
				aeAttendance.att_create_timestamp = Wzb.displayTime(aeAttendance.att_create_timestamp, Wzb.time_format_ymd);
				$("#ishidden").empty().html($("#enrollment").render(aeAttendance));
			}
			
			if(appStatus == 'Waiting' || appStatus == 'Pending'){
				$("#study_status").html(fetchLabel('detail_app_status'));	//如果是等待审批，或者是审批中，提示字段显示为审批状态
			}
			appStatusLabel = getAppStatusStr(appStatus);
			$("#progress_type").html(appStatusLabel);
			if(!isIntegrated) $("#progress_type_desc").hide();	//如果是集成培训，不显示提示


			//学习状态 end
			var itemTypeStr = getExamTypeStr(item.itm_type);
			$.extend(item,{is_desc_sub:is_desc_sub,itm_desc_sub : itm_desc_sub,instructorStr : instructorStr.join(','),
				itemTypeStr: itemTypeStr, appStatus : appStatusLabel, isOffline : isOffline})

			if(isClass){
				if(item.ies){
					item.ies.ies_contents = replaceVlaue(item.ies.ies_contents);
					item.ies.ies_objective = replaceVlaue(item.ies.ies_objective);
					item.ies.ies_prerequisites = replaceVlaue(item.ies.ies_prerequisites);
					item.ies.ies_exemptions =  replaceVlaue(item.ies.ies_exemptions);
					item.ies.ies_remarks = replaceVlaue(item.ies.ies_remarks);
					item.ies.ies_audience = replaceVlaue(item.ies.ies_audience);
					item.ies.ies_duration = replaceVlaue(item.ies.ies_duration);  
					item.ies.ies_schedule = replaceVlaue(item.ies.ies_schedule);
				}else{
					item.ies = {};
				}
				$(".wzb-list-19").empty().html($("#classInfoTemplate").render(item));
				$("#detail").html($('#classDetailTemplate').render(item));
				var lessons = result.lessons;
				if(lessons == undefined || lessons==''){
					$("#nav1 li").eq(1).hide();
					$("#box1 .wbtabcont:eq(1)").hide();
				}
			} else {
				if(isOffline){
					$(".wzb-list-19").empty().html($("#offflineInfoTemplate").render(item));
					//面授课程去掉学员评论区域
					$("#evaluationDiv").remove();
					
					//面授课程默认打开详情页
					$("#nav1 li").eq(0).remove();
					$("#box1 .tab-pane:eq(0)").attr("id", "detail");
					$("#box1 .tab-pane:eq(1)").attr("id", "courseList");
                    if(hasCPDFunction){
                    	$("#box1 .tab-pane:eq(2)").attr("id", "cpdHoursHtml");
                    }
                    
				} else {
					//隐藏第二个
					$("#nav1 li").eq(1).hide();
					
					if(!hasCPDFunction){
                        //$("#nav1 li").eq(3).hide();
                    }
					
					$("#box1 .tab-pane:eq(1)").hide();
					$(".wzb-list-19").empty().html($("#infoTemplate").render(item));
				}
				if(item.ies){
					item.ies.ies_contents = replaceVlaue(item.ies.ies_contents);
					 item.ies.ies_objective = replaceVlaue(item.ies.ies_objective);
					 item.ies.ies_prerequisites = replaceVlaue(item.ies.ies_prerequisites);
					 item.ies.ies_exemptions =  replaceVlaue(item.ies.ies_exemptions);
					 item.ies.ies_remarks = replaceVlaue(item.ies.ies_remarks);
					 item.ies.ies_audience = replaceVlaue(item.ies.ies_audience);
					 item.ies.ies_duration = replaceVlaue(item.ies.ies_duration);
				}else{
					item.ies = {};
				}
				$("#detail").html($('#detailTemplate').render(item));
                if(hasCPDFunction){
                	  $("#cpdHoursHtml").html($("#cpdHours").html());
                }
              
			}
			
			//显示第一个
			$("#nav1 li").eq(0).addClass("active").show();
			$("#box1 .tab-pane:eq(0)").show();

			//赞，分享，收藏
			$("#itm_like_count").like({
				count : result.snsCount?result.snsCount.s_cnt_like_count:0,
				flag : item.is_user_like,
				id : itmId,
				module: 'Course',
				tkhId : tkhId
			})
			$("#itm_collect_count").collect({
				count : result.snsCount?result.snsCount.s_cnt_collect_count:0,
				flag : result.sns.collect,
				id : itmId,
				module: 'Course',
				tkhId : tkhId
			})

			if(${sns_enabled} == true){
				$("#itm_share_count").share({
					count : result.snsCount?result.snsCount.s_cnt_share_count:0,
					flag : result.sns.share,
					id : itmId,
					module: 'Course',
					tkhId : tkhId,
					width : $("#itm_share_count").width()/2
				})
			} else {
				$("#itm_share_count").remove();
			}


			//课程按钮
			var data = {
				btn : result.btn,
				itm_id : item.itm_id,
				app_id : app ? app.app_id:'',
				app_tkh_id : app ? app.app_tkh_id:'',
				itm_title : item.itm_title,
				itm_fee : item.itm_fee,
				itm_icon : item.itm_icon,
				itm_type : item.itm_type,
				canApp : (rcov == undefined || (rcov != undefined && (rcov.cov_status == 'C' || rcov.cov_status == 'F' || rcov.cov_status == 'W') ))
			}
			
			//修改：只有已报名的才会显示【我想放弃学习】的按钮，而之前的报名操作移到了网上内容的区域
			if(data.btn === 2 || data.btn === 5 || data.btn === 6){
				$('.bm.margin-top15').empty().html("");
				
				//“放弃”按扭放到我的状态那个体Tab里面。  放在学习进度那一行，靠右。
				//var btnContent = "<span style='float:right'>"+Wzb.getButton(data, result.userEntId,'exam')+"</span>";
				//$("#status-progress").append(btnContent);
				if(app && app.app_status && app.app_status != 'Pending'
                    && app.app_status != 'Waiting'
                        && app.app_status != 'Withdrawn'
                            && app.app_status != 'Rejected'){
					var btnContent = "<span style='float:left'>"+Wzb.getButton(data, result.userEntId)+"</span>";
	                $('.bm.margin-top15').append(btnContent); 
				}
                
			}else{
				$('.bm.margin-top15').empty().html("");
			}
			
			//提名
			if(result.btn != 6 && result.btn != 7 && result.btn != 8 && !result.isFull){
				if(result.canNorminate){
					$('.bm.margin-top15').append($.render.btnIconTemplate({
						label : fetchLabel('btn_recommend_under') ,//'推荐给下属',
						handler : 'javascript:supervisor_nominate('+ item.itm_id + ', '+ item.itm_id + ', ' + result.userEntId + ', 0);'
					}));
					searchSubordinate();//初始化下属的列表
				}
			}

			//网上内容
			var cosContent = result.coscontent;
			if(tkhId == undefined || tkhId == ''){
				tkhId = app ? app.app_tkh_id: '';
			}
/**********************网上课程****************************************************************************/
			
			var contentFlag = false;
			var contentTipDesc = "";
			var contentTipBtnText = "";
			var contentTipBtnAction = 'javascript: Wzb.enrol("' + data.itm_id + '", "' + data.itm_type + '")';
			showContent = function(){
				$("#content-tip").hide();
				$("#contentList").show();
			};
			notSubmit = function(){	
			};
			if(!appStatus || 'Rejected'==appStatus || 'Withdrawn'==appStatus){//还没报名
				switch(result.btn){
					case 1 : //未报名，但报名成功后马上可以开始学习
						contentTipBtnText = "label_core_training_management_343";//马上点这里开始学习
						contentFlag = true;
					break;
					case 4 : //未报名，但该课程是需要报名审批通过后才能学习的情况
						//考试与课程提示不一样
                        if(item.itm_exam_ind == 1){
                            contentTipDesc = "label_core_training_management_532";//该考试需要先提交学习申请，等管理员或上司审批通过才能开始学习
                        }else{
                            contentTipDesc = "label_core_training_management_276";//该课程需要先提交学习申请，等管理员或上司审批通过才能开始学习
                        }
						contentTipBtnText =　"label_core_training_management_343" //马上申请！
						contentFlag = true;
					break;
					case 7:  //未报名，但名额已满
						//contentTipDesc = "label_core_training_management_279";//客官，你来慢了，学习名额已满。
						contentTipBtnText = "label_core_training_management_284";//名額已滿
                        contentTipBtnAction = "notSubmit()";
						//contentFlag = true;
                        var btnHtml = "<span style='float: left;'>"+"<a onclick='"+contentTipBtnAction+"' class='btn wzb-btn-gray wzb-btn-big margin-right15' href='javascript: void(0);' style='margin-left: 10px;'>"+fetchLabel('label_core_training_management_284')+"</a></span>";
                        $('.bm.margin-top15').append($(btnHtml)); 
					break;
					case 8:  //未报名，但报名成功后马上可以开始学习（名额满，进入等待名单）
                        contentTipBtnText = "label_core_training_management_467_hk";//放上等待名單
                        $('.bm.margin-top15').append("<br/>").append(fetchLabel('label_core_training_management_284')); 
                        contentFlag = true;
                    break;
					case -1://未报名，但还没开始报名，或报名时间已过
						contentTipDesc = "label_core_training_management_280";//该课程目前不开发在线学习申请。
						//contentTipBtnText =　"label_core_training_management_281" //知道了！我只浏览一下
						contentTipBtnAction = "showContent()";
						contentFlag = true;
					break;
				}
			}else{//已经报名的
				if( ("C" == appStatus || "F" == appStatus) && (result.btn == 1 || result.btn == 4) ){//已完成（包括 ：已完成和未完成），但允许重复学习而且是允许报名的
					//在“状态”那个Tab下，显示一个按钮“再学一次”
					var btnHtml = "<span style='float: right;'>"+"<a onclick='"+contentTipBtnAction+"' class='btn wzb-btn-orange wzb-btn-big margin-right15' href='javascript: void(0);' style='margin-left: 10px;'>"+fetchLabel('label_core_training_management_282')+"</a></span>";
					$("#status-progress").append($(btnHtml)); 
				}else if(result.btn == 5){ //已报名，但报名记录还在申批中。
					contentTipDesc = "label_core_training_management_283";//客官，别急，你的申请还在等待审批中。
					//contentTipBtnText =　"label_core_training_management_281" //知道了！我只浏览一下
					contentTipBtnAction = "showContent()";
					contentFlag = true;
				}else if(result.btn == 6){ //已报名，但还在等待队列
					var btnContent = "<span style='float:left'>"+Wzb.getButton(data, result.userEntId)+"</span>"+"</br>";
	                $('.bm.margin-top15').append(btnContent); 
					contentTipDesc = "label_core_training_management_284";//客官，別急，目前學習人數太多，請耐心等待。
					//contentTipBtnText =　"label_core_training_management_281" //知道了！我只浏览一下
					contentTipBtnAction = "showContent()";
					contentFlag = true;
				}
			}
			
			$(".xyd-wang .xyd-wang-box").append($('#moduleTitleTemplate').render());
			if(cosContent != undefined){
				$.each(cosContent, function(i,val){
					var dirChildren = val.children;
					if(val.id < 1){
						var hasChildFlag = false;
						
						$.extend(val, {index:i});
						$(".xyd-wang .dirMd").append($("#moduleDirTemplate").render(val));
						$.each(val.children, function(j,res){
							if(res.resources){
								var resObj = res.resources;
								if(resObj.mov){
									$.extend(resObj.mov,{statusStr : getAppStatusStr(resObj.mov.mov_status,resObj.res_subtype)});
								}
								hasChildFlag = true;
								$("#moduleDir_" + i).append($('#moduleContentTemplate').render(resObj));
								showModule(resObj, rcov, result);
							}
						});
						
						if(!hasChildFlag){
							$("#moduleDir_" + i).prev().remove();
							$("#moduleDir_" + i).remove();
						}
						
					} else {
	/* 					if($(".menuPane .dirMd .moduleDir").length >0 ){
							$(".menuPane .noDirMd").append("<hr/>");
						} */
						if(val.resources){
							var resObj = val.resources;
							if(resObj.mov){
								$.extend(resObj.mov,{statusStr : getAppStatusStr(resObj.mov.mov_status,resObj.res_subtype)});
							}

							$(".xyd-wang .noDirMd").append($('#moduleContentTemplate').render(resObj));

							showModule(resObj, rcov, result);
						}
					}

				});
			}
			//展开收起选中第一个
			if($(".xyd-wang .dirMd .moduleDir").length >0){
				//在没有文件夹的模块前面加上下划线
				$(".xyd-wang .noDirMd").prepend("<hr/>");
				$(".xyd-wang .xyd-pane-info:eq(0)").show();
				$(".xyd-wang .xyd-pane-title:eq(0)").addClass("skin-color");
				$(".xyd-wang .xyd-pane-title:eq(0) i").removeClass("fa-plus-square").addClass("fa-minus-square");
			}
			$('[data-toggle="tooltip"]').tooltip();
/**********************网上课程****************************************************************************/

			//网上课程模块选中展开收起
			$(".xyd-wang-bigbox").bind('hover', function(){
				
				var el = $(this);
				
				if(el.find(".xyd-mwd05 .wzb-icon-begin").attr("data-url")){
					el.find(".xyd-mwd05 span").toggle();
					el.find(".xyd-mwd05 .wzb-icon-begin").toggle();
				}
				
			 var html = $.trim(el.children(".xyd-wang-info").find(".moduletext:last").html())
						+ $.trim(el.children(".xyd-wang-info").find(".div-btn:last").html());
				if(html != undefined && html != ''){
					el.children(".xyd-wang-info").toggle(); 
				}
			});
			
			//循环判断哪个可以展开收起
			$(".xyd-wang .xyd-wang-info").each(function(){
				var html  = $.trim($(this).find(".moduletext:last").html()) + $.trim($(this).find(".div-btn:last").html());
				if(html != undefined && html != ''){
					$(this).prev(".xyd-wang-title").find(".xyd-mwd06").find("i").addClass("skin-color");
				}
			});
			//默认展开第一个模块
			if($('.fa-chevron-circle-down').length>0){
				$($('.fa-chevron-circle-down')[0]).click();
			}

			//集成培训 【选修，必修】 展开收起
			$(".xyd-wang .xyd-pane-title").live('click', function(){
				var _this=this;
				$(this).addClass("skin-color");
				$(this).find("i").removeClass("fa-plus-square").addClass("fa-minus-square");
				$(this).next(".menuInfo").slideToggle(300,function(){
				       if($(_this).next().is(":hidden")){
				    	   $(_this).find("i").addClass("fa-plus-square").removeClass("fa-minus-square");
						//   $(_this).removeClass("skin-color");
				       }
				}).siblings(".menuInfo").slideUp("slow");
				// $(this).siblings().removeClass("skin-color");
			});
			//课程列表【选修，必修】
			var child = result.child;
			if(child != undefined && child != ''){
				$("#courseList .xyd-wang").empty();
				$.each(child, function(i,val){
					var obj = child[i];
					var p = {
						count : fetchLabel('detail_need_completed_course',[obj[0].icd.icd_completed_item_count]),	//必须完成以下 {{>count}} 门的课程/测试
						icd_type : obj[0].icd.icd_type
					}
					$("#courseList .xyd-wang").append($('#conditionTemplate').render(p));
					$("#courseList .xyd-wang .wzb-ui-table:last tr").after($('#courseTemplate').render(obj));
				});
				$(".xyd-wang .xyd-wang-info:eq(0)").show();
				$(".xyd-wang .xyd-pane-title:eq(0)").addClass("skin-color");
			}
			
			if(contentFlag && !isOffline){
				/* var contentTipHtml = "<div class='xyd-wang' style='text-align:center'>" + 
					(contentTipDesc ? fetchLabel(contentTipDesc) : '') + 
					(contentTipBtnText ? "<div class='bm margin-top15'><a class='btn wzb-btn-orange wzb-btn-big margin-right15' onclick='"+contentTipBtnAction+"' href='javascript: void(0);'>"+fetchLabel(contentTipBtnText)+"</a></div>" : '')+
				"</div>" */
				
				//$("#content-tip").html(contentTipHtml);
				
				//$("#contentList").hide();
				
				var contentTipHtml = ''
				if(contentTipDesc){
					contentTipHtml += (contentTipDesc ? fetchLabel(contentTipDesc) : '');
					$('.bm.margin-top15').append("<p class='margin-top10 margin-bottom10'>"+contentTipHtml+"</p>");
					if(data.itm_type == "SELFSTUDY" || data.itm_type == "CLASSROOM"){
						contentTipHtml = (contentTipBtnText ? "<a class='btn wzb-btn-orange wzb-btn-big margin-right15' onclick='"+contentTipBtnAction+"' href='javascript: void(0);'>"+fetchLabel(contentTipBtnText)+"</a>" : '')
						//添加报名按钮
						$('.bm.margin-top15').prepend(contentTipHtml);
					}
				}else{
					contentTipHtml += (contentTipBtnText ? "<a class='btn wzb-btn-orange wzb-btn-big margin-right15' onclick='"+contentTipBtnAction+"' href='javascript: void(0);'>"+fetchLabel(contentTipBtnText)+"</a>" : '')
					//添加报名按钮
					$('.bm.margin-top15').prepend(contentTipHtml);
				}
				
			}
			
			//班级列表
			var childrens = result.item.childrens;
			if(childrens != undefined && childrens != ''){
				$("#courseList").empty();
 				$("#courseList").html($.render.tableTemplate({}));
 				$.each(childrens, function(i,item){
 					item.itm_content_eff_start_time = item.itm_content_eff_start_time ?
 							 Wzb.displayTime(item.itm_content_eff_start_time, Wzb.time_format_ymd) : fetchLabel('course_signup_date');
 					item.itm_content_eff_end_time = item.itm_content_eff_end_time ?
 							 Wzb.displayTime(item.itm_content_eff_end_time, Wzb.time_format_ymd) : fetchLabel('course_date_nolimit');
					$("#courseList table").append($('#classListTemplate').render(item));
				});
			}
			var lessons = result.lessons;
			if(lessons != undefined && lessons !=''){
				$("#dateList table tr:last").after($("#itemLessonTemplate").render(lessons));
			}
			//讲师评分
			var instructorComment = result.instructorComment;
			if(instructorComment != undefined && instructorComment != '') {
				setInstructorComment(instructorComment);
				$("#submitInstructor").hide();
			} else {
				if(${sns_enabled} == false){
					$("#evaluationDiv").remove();
				}
			}

			//公告
			var messages = result.messages;
			if(app && messages != undefined && messages != ''){
				var html = [];
		   	 	$.each(messages, function(i,val){
					html.push($('#messageTemplate').render(val))
				});
				$("#messages").append(html).show();
			} else {
				if(hasCPDFunction){
                    $("#nav1 li:eq(4)").hide();
                }else{
                    $("#nav1 li:eq(3)").hide();
                }
                if(isIntegrated || !hasCPDFunction){
                    $("#nav1 li:eq(3)").hide();
                }
				$("#messages").hide();
			}
			//证书
			var certificate = result.certificate;
			var cfc_expired = result.cfc_expired;
			if(certificate != undefined && certificate != '') {
				$.extend(certificate,{itm_id:item.itm_id,tkh_id:app.app_tkh_id})
				if(cfc_expired == 'ON'){
					$("#certificate").next(".fettle_table").find("tr").eq(0).after($("#certificateTemplate").render(certificate));
				}else{
					$("#certificate").next(".fettle_table").find("tr").eq(0).after($("#expiredCertificateTemplate").render(certificate));
				}
			} else {
				$("#certificate").hide();
				$("#certificate").next().hide();
			}
			//进度
			var courseEvaluation = result.courseEvaluation;
			if(courseEvaluation != undefined && courseEvaluation != '') {
				var cov_progress = courseEvaluation.cov_progress;
				if(cov_progress == 0 || cov_progress == undefined) cov_progress = 10;
				
				if(courseEvaluation.cov_status == "I"){//进行中才显示进度条
					$("#progress div").css("width", cov_progress+"%");
				}else{
					$("#progress").remove();
				}
				
				$("#progress").attr('title', courseEvaluation.cov_progress);
			}
			if(app && app.app_status == 'Pending'){
				$("#progress").remove();
			}

			var ccr = result.ccr;
			var isEmptyCrr = true;
			if(ccr != undefined && ccr != ''){
				//计分项目
				if(ccr.score_itm_lst && ccr.score_itm_lst.length > 0){
					$.each(ccr.score_itm_lst, function(i,val){
						if(val.mov){
							$.extend(val.mov, {statusStr : getAppStatusStr(val.mov.mov_status)});
						}
						$("#score_itm_lst tr:last").after($("#ccrTemplate").render(val));
					});
				} else {
					$("#score_itm_lst").hide().prev("div").hide();
				}
				//参与要求
 				if(ccr.cmt_lst && ccr.cmt_lst.length>0) {
					isEmptyCrr = false;
					$.each(ccr.cmt_lst, function(i,val){
						var html = $.render.tdTemplate({text : val.cmt_title});
						var status = val.cmt_status;
						if(val.res != undefined && val.res !='' && val.res.res_type != undefined) {
							if(val.res.res_type == 'RDG' || val.res.res_type == 'REF' || val.res.res_type == 'GLO') {
								status = fetchLabel('module_need_preview');//"需浏览";
							} else if(val.res.res_type == 'DXT' || val.res.res_type == 'NETG_COK' || val.res.res_type == 'SCO' || val.res.res_type == 'TST' || val.res.res_type == 'VOD') {
								if(status == 'IFCP'){
									status = fetchLabel('module_need_preview');//"需浏览"
								} else {
									status = fetchLabel('module_need_pass'); //"需合格";
								}
							} else if(val.res.res_type == 'ASS'){
								if(status == 'IFCP' ){
									status = fetchLabel('module_need_attent');//需參與
								} else {
									status = fetchLabel('module_need_pass'); //"需合格"
								}
							} else if(val.res.res_type == 'SVY'){
								if(status == 'CP' ){
									status = fetchLabel('module_need_submit');//需提交
								} else {
									status = fetchLabel('module_need_attent'); //"需參與"
								}
							} else if(val.res.res_type == 'FOR' || val.res.res_type == 'FAQ'){
								status = fetchLabel('module_need_attent'); //需参与;
							} else {
								if(status == 'IFCP'){
									status = fetchLabel('module_need_preview');//"需浏览"
								} else {
									status = fetchLabel('module_need_pass'); //"需合格";
								}
							}
						}
						html += $.render.tdTemplate({text : status});
						//打不打勾
						if(val.mov && val.cmt_status && val.cmt_status.indexOf(val.mov.mov_status) > -1){
							html += $.render.tdRightTemplate({text : '<i class="fa mL3 f18 fa-check"></i>'});
						}else{
							html += $.render.tdRightTemplate({text : '<i class="fa mL3 f18"></i>'});
						}
						$("#cmt_lst tr:first td:first").attr("rowspan",ccr.cmt_lst.length + 1);
						$("#cmt_lst tr:first").after(Wzb.htmlDecode($.render.trTemplate({text:html})));
					})
				} else {
					$("#cmt_lst").hide().prev("div").hide();
				}
				//出席率要求
				if(ccr.ccr_attendance_rate != undefined && ccr.ccr_attendance_rate != ''){
					isEmptyCrr = false;
					var tdStr = $.render.tdTemplate({text:ccr.ccr_attendance_rate + "%"}) + $.render.tdRightTemplate({text: courseEvaluation && courseEvaluation.att.att_rate? courseEvaluation.att.att_rate + "%":''});
					$("#attendance_required tr:last").after(Wzb.htmlDecode($.render.trTemplate({text:tdStr})))
				} else {
					$("#attendance_required").hide();
				}
				//分数要求
				if(ccr.ccr_pass_score != undefined && ccr.ccr_pass_score > 0 || (courseEvaluation && courseEvaluation.cov_score >0)){
					isEmptyCrr = false;
					var tdStr = $.render.tdTemplate({text : fetchLabel('detail_pass_score') + "："+ccr.ccr_pass_score}) + $.render.tdRightTemplate({text : courseEvaluation?courseEvaluation.cov_score :''});
					$("#score_required tr:last").after(Wzb.htmlDecode($.render.trTemplate({text:tdStr})));
				} else {
					$("#score_required").hide();
				}
				//隐藏完成条件的标题
				if(isEmptyCrr){
					$("#completed_condition").hide();
				}

			} else {
				$("#cmt_lst").hide().prev("div").hide();
				$("#score_required").hide();
				$("#score_itm_lst").hide().prev("div").hide();
				$("#attendance_required").hide().prev("div").hide();
				$("#completed_condition").hide();
			}


			if(isClass){
				$("#courseSNS").hide();		//隐藏赞，收藏，分享
			}
			//将不能跳转的a标签变灰
			$('.xyd-wang-title a').each(function(){
				if($(this).attr('href') == 'javascript:;'){
					$(this).css("color","#666");
				}
			});
 		//} catch(e) {
 		//	throw new Error(e.message);
		//}
 		
			//CPD时数
            if(hasCPDFunction){
                var searchTb;
                var itm_id =item.itm_id;
                
                   /*  var parent_itm_id = '${item.parent.itm_id}';
                    if(parent_itm_id == ''){
                        parent_itm_id = 0;
                    } */
                    parent_itm_id = 0;

                    $.ajax({  
                        type:'post',      
                        url:'${ctx}/app/admin/learnerCPDHours/listJson',  
                        data:{"itm_id":itm_id,
                              "parent_itm_id":parent_itm_id},  
                        dataType:'json',
                        success:function(data){  
                                if(undefined != data.aeItemCPDItem){
                                    $("#accredition_code").text(data.aeItemCPDItem.aci_accreditation_code); 
                                    if(isClass  || item.itm_type == "CLASSROOM"){
                                        $("#detail_cpdHours_end_time").hide();
                                   }
                                    if(undefined != data.end_date && data.end_date!=''){
                                        $("#aci_hours_end_date").text(data.end_date);
                                    }
                                } 
                                
                                    // 用小牌列表排序 
                                    for(var i=data.cpdGroupList.length-1; i >= 0; i--){
                                        //if(undefined != data.aeItemCPDItem){
                                            for(var j=0; j < data.aeItemCPDItem.aeCPDGourpItemList.length; j++){
                                                if(data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_cg_id ==  data.cpdGroupList[i].cg_id){
                                                     p = {
                                                             cg_alias : data.cpdGroupList[i].cg_alias,
                                                             cg_code : data.cpdGroupList[i].cg_code,
                                                             acgi_award_core_hours : data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_core_hours,
                                                             acgi_award_non_core_hours : data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_non_core_hours
                                                            };
                                                   if(data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_core_hours > 0 || data.aeItemCPDItem.aeCPDGourpItemList[j].acgi_award_non_core_hours > 0){
	                                                    var html =  $('#text-info-template').render(p);
	                                                    $('#cpt_set_hours_list').after(html);
                                                    }
                                                    break;
                                                }
                                                if(j == data.aeItemCPDItem.aeCPDGourpItemList.length-1){
                                                    p = {
                                                             cg_alias : data.cpdGroupList[i].cg_alias,
                                                             cg_code : data.cpdGroupList[i].cg_code,
                                                             acgi_award_core_hours : '0.00',
                                                             acgi_award_non_core_hours : '0.00'
                                                            };
                                                    var html =  $('#text-info-template').render(p);
                                                    $('#cpt_set_hours_list').after(html);
                                                }
                                            }
                                        /* }else{
                                             p = {
                                                     cg_alias : data.cpdGroupList[i].cg_alias,
                                                     cg_code : data.cpdGroupList[i].cg_code,
                                                     acgi_award_core_hours : '0.00',
                                                     acgi_award_non_core_hours : '0.00'
                                                    };
                                            var html =  $('#text-info-template').render(p);
                                            $('#cpt_set_hours_list').after(html);
                                        } */
                                    }
                                    
                           }
                        });  
            }
			
			
	});

	//nav 选中显示
	$("#nav1 li").live('click',function(){
		$(this).addClass("active").siblings().removeClass("active");
		$("#box1 .tab-pane:eq("+$(this).index()+")").show().siblings().hide();
	});

	/* $("#nav2 li").click(function(){
		$(this).addClass("active").siblings().removeClass("active");
		$("#box2 .tab-pane:eq("+$(this).index()+")").show().siblings().hide();
		//$("#nav2 li").eq(0).click();
	}); */
	//公告弹出框
	$(".msg").live('click', function(){
		 $('#msgBox').modal('show');
		 var id = $(this).attr('data');
		 $.getJSON("${ctx}/app/announce/detailJson/" + id, function(result){
		 	var data = result.detail;
		 	$("#msgBox .modal-title").html(Wzb.htmlDecode(data.msg_title));
		 	$("#msgBox .modal-body").html(Wzb.htmlDecode(data.msg_body));
		 })
	});
	
	var itc_score = parseFloat($("#instructorComment input[name='itc_style_score']").val()) + 
	parseFloat($("#instructorComment input[name='itc_quality_score']").val()) + 
	parseFloat($("#instructorComment input[name='itc_structure_score']").val()) + 
	parseFloat($("#instructorComment input[name='itc_interaction_score']").val());
	if(isNaN(itc_score) || itc_score <= 0){ 
		$("#lab_had_submit_score").hide();
		$("#lab_average").hide();
	}else{
		$("#lab_had_submit_score").show();
		$("#lab_average").show();
	}
})

function showModule(obj, rcov, result){
	var app = result.app;
	var curDate = result.curDate;
	var userEntId = result.userEntId;
	var item = result.item;
	
	var mod_eff_end_datetime = obj.mod.mod_eff_end_datetime;
	var itm_content_eff_end_time = item.itm_content_eff_end_time;

	var cs = getIconClass(obj.res_subtype);

	var baseObj = $(".resId" + obj.res_id);
	var iconObj = $(baseObj).find("i");
	$(iconObj).addClass(cs);
	$(iconObj).attr("title", obj.res_title);	//图标的提示
	$(baseObj).html($(baseObj).html() + fetchLabel('type_' + obj.res_subtype));

	//创建文字div
	$(baseObj).parent().next().find(".div-temp").empty().append($.render.moduleField({text: obj.res_desc}));
	$(baseObj).parent().next().find(".div-temp").find(".col-xs-6").removeClass("col-xs-6");
	//创建按钮div
	$(baseObj).parent().next().find(".div-temp").append($.render.moduleBtnField({}));

	//链接
	var url,btnUrl;
	
	//是否成功报名
    var appOK = (app && app.app_status == 'Admitted' && app.app_id > 0);
	
  	//是否在网上学习时间内
    var is_time_valide = (app && result.timeValide && app.app_id > 0 && obj.mod && obj.mod.mod_eff_start_datetime <= curDate && mod_eff_end_datetime >= curDate);
    
    //学习状态还是进行中状态
    var is_inprogress = (rcov == undefined || (rcov != undefined && rcov.cov_status == 'I'));
    
    //是否已完成先修模块
	var completed_pre_mod = (obj.mod && (obj.mod.pre_mod_had_completed || obj.mod.pre_mod_had_completed == undefined || obj.mod.pre_res_title == undefined));

	//测验
	if(obj.res_subtype == 'DXT' || obj.res_subtype == 'TST'){
		//展开内容
		var fields = [];
		fields.push({text: fetchLabel('module_max_test_count') + '：' + (obj.mod.mod_max_usr_attempt == 0? fetchLabel('module_unlimited'):obj.mod.mod_max_usr_attempt) });			//测试次数限额
		fields.push({text: fetchLabel('module_timelimit') + '：' + obj.res_duration + fetchLabel('time_minute')});	//限时		分钟
		fields.push({text: fetchLabel('module_max_score') +'：' + obj.mod.mod_max_score});				//满分
		fields.push({text: fetchLabel('module_percent_of_pass') +'：' + obj.mod.mod_pass_score + '%'});			//及格率
		$(baseObj).parent().next().find(".div-temp").empty().append($.render.moduleField(fields));
		$(baseObj).parent().next().find(".div-temp").append($.render.moduleBtnField({}));
		
		if(appOK && is_time_valide && is_inprogress && completed_pre_mod){
		    if(obj.isRestore){
		    	url = "javascript:module_lst.start_prev('"+ obj.res_subtype +"', '" + obj.res_id + "', '" + result.resId + "', '" + tkhId + "', " + isMobile + ", '" + userEntId + "', true, false, true, '"+mod_eff_end_datetime+"' ,'"+itm_content_eff_end_time+"');";
		    }else if(obj.mod && obj.mod.mod_sub_after_passed_ind == 0 && (obj.mov && obj.mov.mov_status && (obj.mov.mov_status == 'C' || obj.mov.mov_status == 'P'))){
				//模块中设置了已及格不能提交，且学习已及格
			}else if(obj.mod && obj.mov && (obj.mod.mod_max_usr_attempt != 0 && obj.mov.mov_total_attempt >= obj.mod.mod_max_usr_attempt)){
				//已超过最大提交次数
				url = "javascript:Dialog.alert(fetchLabel('error_limit_reached'))";
			}else{
				url = "javascript:module_lst.start_prev('"+ obj.res_subtype +"', '" + obj.res_id + "', '" + result.resId + "', '" + tkhId + "', " + isMobile + ", '" + userEntId + "', true, false, true, '"+mod_eff_end_datetime+"' ,'"+itm_content_eff_end_time+"');";
			}
		}

		if(appOK && obj.mov && obj.mov.mov_total_attempt && obj.mov.mov_total_attempt > 0 && obj.submit_num && obj.submit_num > 0){
		   //如果报名成功，且已提交了考试
			//btnUrl = "javascript:module_lst.usr_report('" + userEntId + "', '" + obj.res_id + "', '" + tkhId + "', '"+ obj.res_subtype +"', " + isMobile + ");";
			btnUrl = "javascript:showReport('${ctx}/app/module/report?mod_id="+obj.res_id+"&tkh_id="+tkhId+"&isMobile="+isMobile+"')";
			//如果不是考试
			btnName = fetchLabel('module_study_report');	//学习报告
			$(baseObj).parent().next().find(".div-btn:last").append($.render.btnTemplate({
				name : btnName,
				href : btnUrl
			}));
		}
	} 

	if(url==undefined && app && !is_time_valide && appOK  && is_inprogress && completed_pre_mod){
		url="javascript:Dialog.alert(fetchLabel('warning_end_notice'))";
	}
	
	if(!appOK){
		$(baseObj).siblings(".xyd-mwd05").find(".modStatus").hide();
	}
	
	if(url && appOK){
		$(baseObj).siblings(".xyd-mwd01").find(".wzb-link02").attr("href","javascript:cpdPastTime(\""+url+"\")");
		$(baseObj).siblings(".xyd-mwd05").find(".wzb-icon-begin").attr("data-url",url);
		$(baseObj).siblings(".xyd-mwd05").find(".wzb-icon-begin").click(function(){
			 if(cpdIsEnd){ 
                 Dialog.confirm({text:"<lb:get key='label_cpd.label_core_cpt_d_management_281' />", callback: function (answer) {
                    if(!answer){
                        return;
                    }else{
                        eval(url);
                    } 
               }
               });  
            }else{
                eval(url);
            } 
		});
	}
	
	if(obj.mod && obj.mod.pre_res_title != undefined && obj.mod.pre_res_title!=''&& !obj.mod.pre_mod_had_completed && is_inprogress){
		//"先修提示：您必须先学习完 ' " + obj.mod.pre_res_title + " '，才能学习下以模块。
		var obj_mod_type = obj.mod.mod_type;
		var obj_mod_status = obj.mod.rrq_status;
		var la = fetchLabel('module_prep_requeid', [obj.mod.pre_res_title]);
		if(obj_mod_type == 'ASS' || obj_mod_type == 'DXT' || obj_mod_type == 'TST'){
			if(obj_mod_status == 'I' ){
				la = fetchLabel('module_prep_requeid_submitted', [obj.mod.pre_res_title]);//需提交
			} else {
				la = fetchLabel('module_prep_requeid_passed', [obj.mod.pre_res_title]); //需合格
			}
		} 
		$(baseObj).siblings(".xyd-mwd01").parent(".xyd-wang-title").parent(".xyd-wang-bigbox").attr("title",la);
	}

}

function cpdPastTime(url){
    if(cpdIsEnd){ 
        Dialog.confirm({text:"<lb:get key='label_cpd.label_core_cpt_d_management_281' />", callback: function (answer) {
           if(!answer){
               return;
           }else{
               eval(url);
           } 
      }
      });  
   }else{
       eval(url);
   } 
}

//考试报告
function showReport(url){
	layer.open({
		type: 2,
		title: fetchLabel('label_report_test_report'),
		shadeClose: true,
		scrollbar: false,
		shade: 0.5,
		area: ['90%', '90%'],
		content: url
	}); 
}

function getPopupUsrLst(fld_name, id_lst, nm_lst, blank, auto_enroll_ind, usr_id_lst, lang) {
	tmp_ent_id_lst = id_lst.replace(/~%~/g, "~");
	tmp_ent_nm_lst = nm_lst.replace(/~%~/g, "~");
	var parent_itm_id = wb_utils_get_cookie('sup_nom_parent_itm_id');
	var itm_id = wb_utils_get_cookie('sup_nom_itm_id');
	app.new_enrollment_exec(document.frmAction, lang, tmp_ent_id_lst, 'doFeedParam', parent_itm_id, itm_id, 'close_popup_usr_search');
}

//推荐下属
function supervisor_nominate(parent_itm_id, itm_id, ent_id, root_ent_id) {
	$("#userList").modal("show");
}

//切换下属Tab
function changeTab(thisObj){
	$("#sub_type .now").removeClass("now");
	$(thisObj).addClass("now");
	searchSubordinate();
}

//下属关键字搜索
function searchSubordinate(){
	var searchContent = $("input[name='searchContent']").val();
	if(searchContent == fetchLabel('attention_find_desc')){
		searchContent = '';
	}
	getSubordinateData(searchContent);
}

//获取下属列表
function getSubordinateData(searchContent){
	$(".wzb-percent").html('');
	$(".wzb-percent").table({
		url : '${ctx}/app/subordinate/getSubordinateList/all',
		params : {
			searchContent : searchContent,
			itmId : itmId
		},
		gridTemplate : function(data){
			var addInd = true;
			if($("#send_user_" + data.usr_ent_id).attr("class") != undefined){
				addInd = false;
			}
			p = {
				image : data.usr_photo,
				usr_ent_id : data.usr_ent_id,
				usr_display_bil : data.usr_display_bil,
				usg_display_bil : data.usg_display_bil,
				ugr_display_bil : data.ugr_display_bil,
				a : 'javascript:;',
				addInd : addInd
			}
			return $('#add-user-template').render(p);
		},
		rowCallback : function(){
			photoDoing();
		},
		view : 'grid',
		rowSize : 3,
		rp : 6,
		showpager : 5,
		hideHeader : true,
		usepager : true
	});
}

//给下属报名
function addEnrollment(usr_ent_id){
	if(usr_ent_id != undefined && usr_ent_id.length == 0){
		Dialog.alert(fetchLabel("lab_app_conflict_not_nominate"));
		return;
	}
	var url = "${ctx}/app/course/aeInsMultiAppn";
	$.ajax({
		url : url,
		data : {itm_id : itmId, ent_id_lst : usr_ent_id, auto_enroll_ind : false},
		dataType : "json",
		type : 'POST',
		async : false,
		success : function(data) {
			if(data.hashMap != null && data.hashMap != undefined){
				if(data.hashMap.code == "200"){
					Dialog.alert(fetchLabel("lab_app_conflict"));
				}else{
					if(data.hashMap.conflict != null && data.hashMap.conflict != undefined && data.hashMap.conflict.length > 0){
						var conflict;
						var lab = "";
						for(var i = 0;i<data.hashMap.conflict.length;i++){
							conflict = data.hashMap.conflict[i];
							if(conflict != null && conflict != undefined && conflict.id != undefined){
								lab += fetchLabel("lab_app_conflict_info_l")+ data.hashMap.usr_display_bil + fetchLabel("lab_app_conflict_info_r") + "：";
								if("CONF_1" == conflict.id){
									lab += fetchLabel("lab_app_conflict_1");
								}
								if("CONF_2"==conflict.id){
									lab += fetchLabel("lab_app_conflict_2");
								}
								if("CONF_3"==conflict.id){
									lab += fetchLabel("lab_app_conflict_3");
								}
								if("CONF_5"==conflict.id){
									lab += fetchLabel("lab_app_conflict_5");
								}
							}
						}
						Dialog.alert(lab);
						return;
					}else if(data.hashMap && data.hashMap.code == "500"){
                        if(data.hashMap.msg){
                            Dialog.alert(data.hashMap.msg);
                            return;
                        }
                    }
				}
			}
			searchSubordinate();
		}
	});
}

//给当前列表的全部下属报名
function addAll(){
	var ent_id_lst = "";
	for(var i=0;i<$("a[name='addUser']").length;i++){
		ent_id_lst += $("a[name='addUser']").eq(i).attr("value") + "~";
	}
	addEnrollment(ent_id_lst.substring(0, ent_id_lst.length - 1));
}

function close_popup_usr_search() {
	if (popup_usr_search != null) {
		popup_usr_search.close();
	}
	location.reload();
}

function doFeedParam() {
	param = new Array();
	tmpObj1 = new Array();
	tmpObj2 = new Array();
	tmpObj3 = new Array();

	tmpObj1[0] = 'cmd';
	tmpObj1[1] = 'ae_ins_multi_appn';
	param[param.length] = tmpObj1;

	tmpObj2[0] = 'ent_id_lst';
	ent_id_lst = tmp_ent_id_lst;
	tmpObj2[1] = ent_id_lst.split("~");
	param[param.length] = tmpObj2;

	return param;
}

function setInstructorComment(instructorComment){
	$("#instructorComment input[name='itc_style_score']").val(instructorComment.itc_style_score.toFixed(2));
	$("#instructorComment input[name='itc_quality_score']").val(instructorComment.itc_quality_score.toFixed(2));
	$("#instructorComment input[name='itc_structure_score']").val(instructorComment.itc_structure_score.toFixed(2));
	$("#instructorComment input[name='itc_interaction_score']").val(instructorComment.itc_interaction_score.toFixed(2));
	$("#instructorComment input[name='itc_score']").val(instructorComment.itc_score.toFixed(2));
	//$("#instructorComment textarea[name='itc_comment']").val(Wzb.htmlDecode(instructorComment.itc_comment));

	var starScore = {
			'itc_style_score':$("#instructorComment input[name='itc_style_score']").val(),
			'itc_quality_score':$("#instructorComment input[name='itc_quality_score']").val(),
			'itc_structure_score':$("#instructorComment input[name='itc_structure_score']").val(),
			'itc_interaction_score':$("#instructorComment input[name='itc_interaction_score']").val(),
			'itc_score': Math.round($("#instructorComment input[name='itc_score']").val())
		};
	for (var key in starScore){
        if(starScore[key] > 0){
       		var objli = $("#"+key).children();
       		for(var i in objli){
       			if(i < parseInt(starScore[key])){
	       			$(objli[i]).addClass('xing1');
       			}
       		}
    	}
    }
	
	$(".wzb-form-control li").unbind("mouseenter mouseout click");
	var itc_score = parseFloat($("#instructorComment input[name='itc_style_score']").val()) + 
	parseFloat($("#instructorComment input[name='itc_quality_score']").val()) + 
	parseFloat($("#instructorComment input[name='itc_structure_score']").val()) + 
	parseFloat($("#instructorComment input[name='itc_interaction_score']").val());
	
	if(isNaN(itc_score) || itc_score <= 0){ 
		$("#lab_had_submit_score").hide();
	}else{
		$("#lab_had_submit_score").show();
		$("#lab_average").show();
	}
}

function getIconClass(type){
	switch(type){
		case "DXT" : return "fa-file-text-o";	//动态测验
		case "TST" : return "fa-file-text-o";	//静态测验
		case "ASS" : return "fa-clipboard"; //作业
		case "RDG" : return "fa-book";			//教材
		case "AICC_AU" : return "fa-file";		//AICC
		case "REF" : return "fa-bookmark-o";	//参考
		case "VOD" : return "fa-video-camera";	//视频点播
		case "TEST" : return "fa-file-text-o";	//测验
		case "SCO" : return "fa-file-o";	//SCORM
		case "SVY" : return "fa-pencil-square-o";	//调查问卷
		case "FOR" : return "fa-comments";	//论坛
		case "FAQ" : return "fa-comments";	//答疑栏
		case "GLO" : return "fa-list-alt";	//词汇表
	}
}

function getStyleSheet(type){
	switch(type){
		case "REF" : return "lrn_get_reference_lst.xsl";	//参考
		case "VOD" : return "lrn_vod.xsl";	//视频点播
		case "FOR" : return "forum.xsl";	//论坛
		case "FAQ" : return "lrn_faq.xsl";	//答疑栏
		case "SVY" : return "svy_player.xsl";
		case "SVY" : return "svy_player.xsl";
		default : return "blank_template.xsl";
	}
}


//评论星星
$(function(){
	p={id : 'itc_style_score'}
	$('#itc_style_score').html($("#starTemplate").render(p));

	p={id : 'itc_quality_score'}
	$('#itc_quality_score').html($("#starTemplate").render(p));

	p={id : 'itc_structure_score'}
	$('#itc_structure_score').html($("#starTemplate").render(p));

	p={id : 'itc_interaction_score'}
	$('#itc_interaction_score').html($("#starTemplate").render(p));

	p={id : 'itc_score'}
	$('#itc_score').html($("#starTemplate").render(p));

	$(".wzb-form-control li").mouseenter(function(){
		$(this).addClass("xing1-mouseenter").nextAll().removeClass("xing1-mouseenter").addClass("xing2-mouseout");
		$(this).prevAll().addClass("xing1-mouseenter");
		$(this).parent().find("li").mouseout(function () {
			$(this).parent().find("li").removeClass("xing1-mouseenter xing2-mouseout");
		})
	}).mouseout(function(){
		$(this).parent().find("li").removeClass("xing2-mouseout xing1-mouseenter");
	}).click(function(){
		$(this).parent().find("li").unbind("mouseout");
		$(this).addClass('xing1').prevAll().addClass("xing1");
		$(this).nextAll().removeClass("xing1");
		$(this).parent().next().val($(this).attr("data"));
		
		var itc_score = parseFloat($("#instructorComment input[name='itc_style_score']").val()) + 
						parseFloat($("#instructorComment input[name='itc_quality_score']").val()) + 
						parseFloat($("#instructorComment input[name='itc_structure_score']").val()) + 
						parseFloat($("#instructorComment input[name='itc_interaction_score']").val());
		if(itc_score > 0){ 
			$("#instructorComment input[name='itc_score']").val(parseFloat((itc_score/4).toFixed(1)));
		}
		
	});
	
})
</script>
<!-- template start -->
<script id="starTemplate" type="text/x-jsrender">
	<li class="xing2" data="1" title="1"></li>
	<li class="xing2" data="2" title="2"></li>
	<li class="xing2" data="3" title="3"></li>
	<li class="xing2" data="4" title="4"></li>
	<li class="xing2" data="5" title="5"></li>	
</script>
<!-- 网上课程模块列表 -->
<script id="moduleTitleTemplate" type="text/x-jsrender">
	<div class="xyd-mwd01"><!--名称--><lb:get key="global_name"/></div>
	<div class="xyd-mwd02"><!--类型--><lb:get key="global_kinds"/></div>
	<div class="xyd-mwd03"><!--上次参与--><lb:get key="detail_last_attent"/></div>
	<div class="xyd-mwd04"><!--最佳分数--><lb:get key="detail_best_score"/></div>
	<div class="xyd-mwd05"><!--状态--><lb:get key="global_current_status"/> / <lb:get key="global_require_status"/></div>

</script>
<script id="moduleDirTemplate" type="text/x-jsrender">

	<p class="xyd-pane-title skin-color">
		<span>
			<i class="fa mr5 fa-plus-square"></i>{{>title}}
		</span>
	</p>

	<div class="xyd-pane-info moduleDir menuInfo" id="moduleDir_{{>index}}">
	</div>

</script>
<script id="moduleContentTemplate" type="text/x-jsrender">
<div class="xyd-wang-bigbox" data-toggle="tooltip" data-placement="right" title="" data-original-title="">
	<div class="xyd-wang-title clearfix">
		<div class="xyd-mwd01">
			<a class="wzb-link02" href="javascript:;">{{>res_title}}</a>
			<span style="display:none;">{{>res_title}}</span>
		</div>
		<div class="xyd-mwd02 resId{{>res_id}}">
			<i class="fa"></i>
		</div>
		<div class="xyd-mwd03">
			{{if mov}}
			{{>mov.mov_last_acc_datetime}}
			{{else}}
				--
			{{/if}}
		</div>
		<div class="xyd-mwd04">
			{{if res_subtype=='SVY'}}
				--
			{{else}}
				{{if mov}}
					{{if mov.mov_score >= 0}}
						{{>mov.mov_score}}
					{{else}}
						--
					{{/if}}
					{{else}}
					--
				{{/if}}
			{{/if}}
		</div>
		<div class="xyd-mwd05">
			<span class="modStatus">
				{{if mov}}
					{{if mov.mov_status == 'I' && pgr_status == 'NOT GRADED'}}
						{{if isTrainingCourse}}
							{{if trainingStatus.indexOf('[CP]') != -1}}
								<lb:get key="course_grading" /> / <lb:get key="course_unfulfill" /> <img src="${ctx}/static/images/N.png" style="width:15px;height:15px;">
							{{else}}
								{{if trainingStatus.indexOf('[null]') != -1}}
									<lb:get key="course_grading" /> / --
								{{else}}
									<lb:get key="course_grading" /> / <lb:get key="course_fulfill" /> <img src="${ctx}/static/images/Y.png" style="width:15px;height:15px;">
								{{/if}}
							{{/if}}
						{{else}}
							<lb:get key="course_grading" /> / --
						{{/if}}
					{{else}}
						{{if isTrainingCourse}}
							{{if trainingStatus.indexOf('[CP]') != -1}}
								{{if res_subtype == 'TST' || res_subtype == 'DXT' || res_subtype == 'ASS'}}
									{{if pgr_completion_status == 'P'}}
										{{>mov.statusStr}} / <lb:get key="course_fulfill" /> <img src="${ctx}/static/images/Y.png" style="width:15px;height:15px;">
									{{else}}
										{{if res_subtype == 'ASS'}}
											<lb:get key="course_fail_resubmit" /> / <lb:get key="course_unfulfill" /> <img src="${ctx}/static/images/N.png" style="width:15px;height:15px;">
										{{else}}
											{{>mov.statusStr}} / <lb:get key="course_unfulfill" /> <img src="${ctx}/static/images/N.png" style="width:15px;height:15px;">
										{{/if}}
									{{/if}}
								{{else}}
									{{if mov.mov_status == 'C' || mov.mov_status == 'P'}}
										{{>mov.statusStr}} / <lb:get key="course_fulfill" /> <img src="${ctx}/static/images/Y.png" style="width:15px;height:15px;">
									{{else}}
										{{>mov.statusStr}} / <lb:get key="course_unfulfill" /> <img src="${ctx}/static/images/N.png" style="width:15px;height:15px;">
									{{/if}}
								{{/if}}
							{{else}}
								{{if trainingStatus.indexOf('[null]') != -1}}
									{{>mov.statusStr}} / --
								{{else}}
									{{>mov.statusStr}} / <lb:get key="course_fulfill" /> <img src="${ctx}/static/images/Y.png" style="width:15px;height:15px;">
								{{/if}}
							{{/if}}
						{{else}}
							{{>mov.statusStr}} / --
						{{/if}}
					{{/if}}
				{{else}}
					{{if isTrainingCourse}}
						{{if trainingStatus.indexOf('[null]') != -1}}
							<lb:get key="status_not_started" /> / --
						{{else}}
							<lb:get key="status_not_started" /> / <lb:get key="course_unfulfill" /> <img src="${ctx}/static/images/N.png" style="width:15px;height:15px;">
						{{/if}}
					{{else}}
						<lb:get key="status_not_started" /> / --
					{{/if}}
				{{/if}}
			</span>
			{{if res_subtype == 'ASS'}}
				{{if (mov != undefined && mov != '') && mov.mov_total_attempt > 0 && (submit_num != undefined && submit_num > 0)}}
					<div style="display:none" class="wzb-icon-begin btn wzb-btn-yellow margin-top4"><lb:get key='label_core_training_management_30'/><!--成绩 --></div>
				{{else}}
					<div style="display:none" class="wzb-icon-begin btn wzb-btn-blue margin-top4"><lb:get key='label_core_training_management_345'/><!--开始 --></div>
				{{/if}}
			{{else}}
				<div style="display:none" class="wzb-icon-begin btn wzb-btn-blue margin-top4"><lb:get key='label_core_training_management_345'/><!--开始 --></div>
			{{/if}}
		</div>
	</div>


	<div class="xyd-wang-info clearfix">

		 <div class="div-temp">

         	<div class="col-xs-6">
         	</div>

		 </div>
    </div>

</div>
</script>
<!-- 必修选修条件块模板 -->
<script id="conditionTemplate" type="text/x-jsrender">
	<p class="xyd-pane-title skin-color">
		<span class="skin-color">
			<i class="fa mr5 fa-plus-square"></i> {{>icd_type}}： {{>count}}
		</span>
	</p>

	<div class="xyd-pane-info">
         <table class="table wzb-ui-table">
                <tbody>
					<tr class="wzb-ui-table-head">
                        <td width="50%"><!--课程名称 --><lb:get key='course_title' /></td>
                        <td width="30%"><!--课程类型 --><lb:get key='exam_type' /></td>
                        <td width="20%" align="right"><!--状态--><lb:get key='course_status' /></td>
                    </tr>
         		</tbody>
		 </table>
     </div>
</script>
<!-- 必修选修下的课程列表 -->
<script id="courseTemplate" type="text/x-jsrender">
	<tr>
        <td>
			<a class="wzb-link02" href="${ctx}/app/course/detail/{{>itm_id}}{{if app}}?tkhId={{>app.app_tkh_id}}{{/if}}" title="">{{if itm_mobile_ind == 'yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{:itm_title}}</a>
		</td>
        <td>{{>itm_type}}</td>
        <td align="right">

			<div style="width:60%;" class="progress progress-xs">
				<div class="progress-bar progress-bar-orange" style="width:{{if cov}}{{>cov.cov_progress}}{{else}}0{{/if}}px">
				</div>
			</div>

			{{if cov}}
					{{>cov.cov_status}}
				{{else}}
					<!-- 未报名-->
					<lb:get key='status_notapp' />
			{{/if}}
		</td>
    </tr>
</script>
<!-- 课程图片 赞分享收藏 -->
<script id="infoHeaderTemplate" type="text/x-jsrender">
    <dd>
		<img src="${ctx}{{>itm_icon}}" alt=""/>

         <div class="wzb-toolbox clearfix" id="courseSNS">
              <a class="margin-left35"  href="javascript:;" title="" id="itm_collect_count" style="text-align:center;"><i class="fa skin-color fa-star" style="margin:0;"></i><p><!--收藏--><lb:get key="sns_collect"/>(<span >0</span>)</p></a>
         </div>
    </dd>
</script>
<!-- 按钮 -->
<script id="infoBtnTemplate" type="text/x-jsrender">

	<div class="bm margin-top15">
        <!--报 名--><input type="button" onclick="javascript:void(0);" class="btn wzb-btn-orange wzb-btn-big margin-right15" value="<lb:get key="course_enrollment"/>" name="frmSubmitBtn">
        <!--推荐给下属--><input type="button" onclick="javascript:void(0);" class="btn wzb-btn-orange wzb-btn-big" value="<lb:get key="course_recommend_under"/>" name="frmSubmitBtn">
     </div>

</script>
<script id="openDescTemplate" type="text/x-jsrender">


		<span data="{{:itm_desc}}">{{if itm_desc}}{{:itm_desc_sub}}{{else}}<lb:get key="detail_no_message"/>{{/if}}</span>
		<a class="wzb-show skin-color open_desc" id="wbshow" style="{{>is_desc_sub}}">
			<!--展开--><span><lb:get key="click_down"/></span>
			<i class="fa fa-angle-down"></i>
		</a>
	</p>

</script>

<script id="courseTitleTemplate" type="text/x-jsrender">

	<h3>
		{{if parent}}
			<a class="wzb-link01" href="${ctx}/app/course/detail/{{>parent.itm_id}}" title="">
				{{:parent.itm_title}}
			</a> >
		{{/if}}
		<span style="display: inline-block;"><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind == 'yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{:itm_title}}</span>
	</h3>

</script>
<!-- top课程信息 -->
<script id="infoTemplate" type="text/x-jsrender">
	{{include tmpl="#infoHeaderTemplate"/}}
	<dt>
		{{include tmpl="#courseTitleTemplate"/}}
		<div class="offheight clearfix">
			<div class="offwidth">
				<span class="color-gray999"><!--课程类型--><lb:get key="exam_type"/>：</span>{{>itemTypeStr}}
			</div>
			<div class="offwidth">
				<span class="color-gray999"><lb:get key="content_duration"/>：</span>
				<!-- <lb:get key="label_core_community_management_214"/> {{>itm_content_eff_start_time}} <lb:get key="time_to"/> {{>itm_content_eff_end_time}} -->
				{{if itm_online_content_period == 0}}
					<lb:get key="time_unlimited"/>
				{{else}}
					{{if itm_online_content_period == 1}}
						<lb:get key="time_by"/> {{>itm_content_eff_end_time}} <lb:get key="time_before"/>
					{{else}}
						{{if itm_is_enrol == 0}}
							{{>itm_content_eff_duration}}<lb:get key="time_after_day"/>
						{{else}}
							<lb:get key="time_by"/> {{>itm_content_eff_end_time}} <lb:get key="time_before"/>
						{{/if}}
					{{/if}}
				{{/if}}
			</div>
		</div>


		<div class="offheight clearfix">
			<div class="offwidth">
				<span class="color-gray999" id="course_credit">
						<lb:get key="course_credit"/>：
				</span>
				{{if ies}}{{if ies.ies_credit}}{{>ies.ies_credit}}{{else}}--{{/if}}{{else}}--{{/if}}
			</div>
			<div class="offwidth">
				<span class="color-gray999" id="study_status">
					<!--学习状态--><lb:get key="detail_exam_status"/>：
				</span>
				{{if appStatus}}{{>appStatus}}{{else}}--{{/if}}
			</div>
		</div>

		<p>
			<span class="color-gray999"><!--课程简介--><lb:get key="exam_introduction"/>：</span>
			{{include tmpl="#openDescTemplate"/}}{{include tmpl="#infoBtnTemplate"/}}

	</dt>
</script>
<!-- 课程详细模板 -->
<script id="detailTemplate" type="text/x-jsrender">
   <table class="wzb-table-three">
		  <tr>
              <td class="wzb-table-title" valign="top">
<!--课程名称--><lb:get key="exam_title"/>：
			  </td>
              <td class="wzb-table-content">{{:itm_title}}</td>
          </tr>
		  <tr>
              <td class="wzb-table-title" valign="top">
<!--  课程编号--><lb:get key="exam_code"/>：</td>
              <td class="wzb-table-content">{{>itm_code}}</td>
          </tr>
          <tr>
              <td class="wzb-table-title" valign="top">
<!--课程类型--><lb:get key="exam_type"/>：</td>
              <td class="wzb-table-content">{{>itemTypeStr}}</td>
          </tr>
          <tr>
              <td class="wzb-table-title" valign="top">
<!--培训中心--><lb:get key="traning_center"/>：
			  </td>
              <td class="wzb-table-content">{{>tcr.tcr_title}}</td>
          </tr>
		  {{if catalogs}}
          <tr>
              <td class="wzb-table-title" valign="top">
<!--课程目录--><lb:get key="menu_test_catalog"/>：
			  </td>
              <td class="wzb-table-content">{{>catalogs}}</td>
          </tr>
		  {{/if}}
		  {{if itm_cfc_title}}
          <tr>
              <td class="wzb-table-title" valign="top">
<!--证书--><lb:get key="certificate"/>：
			  </td>
              <td class="wzb-table-content">{{>itm_cfc_title}}</td>
          </tr>
		  {{/if}}
          <tr>
              <td class="wzb-table-title" valign="top">
<!-- 需要审批--><lb:get key="course_is_approval"/>：</td>
              <td class="wzb-table-content">
				{{if itm_app_approval_type}}
<!--是--><lb:get key="status_yes"/>
				{{else}}
<!--否--><lb:get key="status_no"/>
				{{/if}}
			  </td>
          </tr>
		  {{if ies && ies.ies_duration}}
          <tr>
              <td  class="wzb-table-title" valign="top">
<!-- 学习时长 --><lb:get key="course_time_long"/>：
              </td>
              <td class="wzb-table-content">
			  {{:ies && ies.ies_duration}}
			  </td>
          </tr>
		  {{/if}}
		  {{if !isOffline}}
          	<tr>
              <td class="wzb-table-title" valign="top">
<!--  报名期限--><lb:get key="enrollment_duration"/>：</td>
              <td class="wzb-table-content"><lb:get key="label_core_community_management_214"/> {{>itm_appn_start_datetime}} <lb:get key="time_to"/> {{>itm_appn_end_datetime}}</td>
          	</tr>
		  {{/if}}
		  {{if item_fee}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!-- 报名费--><lb:get key="course_fee"/>：</td>
              <td class="wzb-table-content">{{>item_fee}}</td>
          </tr>
		  {{/if}}

		  {{if ies && ies.ies_credit}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!--积分--><lb:get key="course_credit"/>：</td>
              <td class="wzb-table-content">{{>ies.ies_credit}}</td>
          </tr>
		  {{/if}}

		  {{if ies && ies.ies_objective}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!--目标--><lb:get key="course_objective"/>：</td>
              <td class="wzb-table-content">{{:ies.ies_objective}}</td>
          </tr>
		  {{/if}}

		  {{if ies && ies.ies_contents}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!--内容--><lb:get key="course_contents"/>：</td>
              <td class="wzb-table-content">{{:ies.ies_contents}}</td>
          </tr>
		  {{/if}}

		  {{if ies && ies.ies_prerequisites}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!--必备基础知识--><lb:get key="course_prerequisites"/>：</td>
              <td class="wzb-table-content">{{:ies.ies_prerequisites}}</td>
          </tr>
		  {{/if}}

		  {{if ies && ies.ies_exemptions}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!--免修条件--><lb:get key="course_exemptions"/>：</td>
              <td class="wzb-table-content">{{:ies.ies_exemptions}}</td>
          </tr>
		  {{/if}}

		  {{if ies && ies.ies_remarks}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!--备注--><lb:get key="course_remarks"/>：</td>
              <td class="wzb-table-content">{{:ies.ies_remarks}}</td>
          </tr>
		  {{/if}}

		  {{if instructorStr}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!-- 讲师名称--><lb:get key="instructor_name"/>：</td>
              <td class="wzb-table-content">{{>instructorStr}}</td>
          </tr>
		  {{/if}}
		  {{if ies && ies.ies_audience}}
          <tr>
              <td class="wzb-table-title" valign="top">
<!-- 学习对象--><lb:get key="course_audience"/>：</td>
              <td class="wzb-table-content">
				{{:ies.ies_audience}}
			  </td>
          </tr>
		  {{/if}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!-- 课程简介--><lb:get key="exam_introduction"/>：</td>
              <td class="wzb-table-content">
				{{if itm_desc}}
					{{:itm_desc}}
				{{else}}
					<lb:get key="detail_no_message"/>
				{{/if}}
			  </td>
          </tr>
   </table>
</script>

<!-- CPD 时数模板 -->
<script id="text-info-template" type="text/x-jsrender">
             <tr>
              <td class="wzb-form-label"  width="40%">{{>cg_alias}}({{>cg_code}})：</td>
              <td width="20%" style="color:#666;padding-left: 20px">{{>acgi_award_core_hours}}&nbsp;hour(s)</td>
              <td width="20%" style="color:#666;padding-left: 20px">{{>acgi_award_non_core_hours}}&nbsp;hour(s)</td>
              <td class="wzb-form-control" width="20%"></td>
             </tr>
</script>

<!-- 公告模板 -->
<script id="messageTemplate" type="text/x-jsrender">

<tr>
    <td><a data="{{>msg_id}}" class="wzb-link03 msg" href="###" title="{{>msg_title}}"><i class="fa fa-volume-up"></i> {{>msg_title}}</a></td>
    <td align="right"><span class="color-gray666">{{>msg_begin_date}}</span></td>
</tr>

</script>
<!-- 证书模板 -->
<script id="certificateTemplate" type="text/x-jsrender">

<tr>
     <td><span title="{{>cfc_title}}">{{>cfc_title}}</span></td>
     <td>{{>cfc_end_date}}</td>
     <td align="right"><a class="skin-color" href="javascript: cert.download_cert('{{>cfc_id}}', '{{>itm_id}}', '{{>tkh_id}}');" title="<lb:get key="btn_download"/>"><lb:get key="btn_download"/></a></td>
</tr>

</script>

<!-- 过期证书模板 -->
<script id="expiredCertificateTemplate" type="text/x-jsrender">

<tr>
     <td><a class="wzb-link03" href="javascript:;" title="{{>cfc_title}}">{{>cfc_title}}</a></td>
     <td>{{>cfc_end_date}}</td>
     <td align="right"><a class="skin-color" href="javascript: ;" title="<lb:get key="btn_download"/>"><!--已过期--> <lb:get key="certificate_download_error"/></td>
</tr>

</script>

<!-- 班级模板 -->
<script id="classDetailTemplate" type="text/x-jsrender">
   <table  class="wzb-table-three">
          <tr>
              <td class="wzb-table-title" valign="top">
<!--               班级编号 --><lb:get key="class_code"/>
              :</td>
              <td class="wzb-table-content">{{>itm_code}}</td>
          </tr>
          <tr>
              <td class="wzb-table-title" valign="top">
<!--               班级名称 --><lb:get key="class_title"/>
              :</td>
              <td class="wzb-table-content">{{:itm_title}}</td>
          </tr>
<tr>
              <td class="wzb-table-title" valign="top">
<!--               学分 --><lb:get key="course_credit"/>
              :</td>
              <td class="wzb-table-content">
				{{if ies}}
					{{if ies.ies_credit}}
						{{>ies.ies_credit}}
					{{else}}
						--
					{{/if}}
					{{else}}
						--
				{{/if}}
			  </td>
          </tr>
          <tr>
              <td class="wzb-table-title" valign="top">
<!--               班级地点 --><lb:get key="class_address"/>
              :</td>
              <td class="wzb-table-content">
				{{if ies}}
					{{if ies.ies_schedule}}
						{{:ies.ies_schedule}}
					{{else}}
						--
					{{/if}}
				{{/if}}
			  </td>
          </tr>
		  <tr>
			<td class="wzb-table-title" valign="top">
				<!--  报名期限--><lb:get key="enrollment_duration"/>
			:</td>
			<td class="wzb-table-content">
				<lb:get key="label_core_community_management_214"/> {{>itm_appn_start_datetime}} <lb:get key="time_to"/> {{>itm_appn_end_datetime}}
			</td>
		  </tr>
		  <tr>
              <td class="wzb-table-title" valign="top">
<!--               班级简介 --><lb:get key="class_introduction"/>
              :</td>
              <td class="wzb-table-content">{{:itm_desc}}</td>
          </tr>
		  {{if ies.ies_remarks}}
          <tr>
              <td class="wzb-table-title" valign="top">
 <!--备注--><lb:get key="course_remarks"/>：</td>
              <td class="wzb-table-content">{{:ies.ies_remarks}}</td>
          </tr>
		  {{/if}}
   </table>
</script>

<script id="classInfoTemplate" type="text/x-jsrender">
	{{include tmpl="#infoHeaderTemplate"/}}
    <dt>
		 {{include tmpl="#courseTitleTemplate"/}}

		<div class="offheight clearfix">
			<div class="offwidth">
				<span class="color-gray999"><!--培训地点--><lb:get key="course_class_period"/>：</span>
				<lb:get key="label_core_community_management_214"/> {{>itm_content_eff_start_time}} <lb:get key="time_to"/>{{>itm_content_eff_end_time}}
			</div>
			<div class="offwidth">
				<span class="color-gray999">
					<!--培训地点--><lb:get key="course_train_address"/>：
				</span>
				{{if ies}}
					{{if ies.ies_schedule}}
						{{:ies.ies_schedule}}
					{{else}}
						--
					{{/if}}
					{{else}}
						--
				{{/if}}
			</div>
		</div>


		<div class="offheight clearfix">
			<div class="offwidth">
				<span class="color-gray999" id="course_credit">
						<!--培训地点--><lb:get key="course_credit"/>：
				</span>
				{{if ies}}
					{{if ies.ies_credit}}
						{{>ies.ies_credit}}
					{{else}}
						--
					{{/if}}
					{{else}}
						--
				{{/if}}
			</div>
			<div class="offwidth">
				<span class="color-gray999" id="study_status">
					<!--学习状态--><lb:get key="detail_study_status"/>：
				</span>
				{{if appStatus}}
					{{>appStatus}}
				{{else}}
					--
				{{/if}}
			</div>
		</div>

		<p>
			<span class="color-gray999"><!--班级简介--><lb:get key="exam_introduction"/>：</span>

		 {{include tmpl="#openDescTemplate"/}}
		 {{include tmpl="#infoBtnTemplate"/}}
    </dt>
</script>

<!-- 日程表 -->
<script id="indexWrapperTemplate" type="text/x-jsrender">
		{{if #index > 0}},{{/if}}
		{{include tmpl=#content/}}
</script>
<script id="itemLessonTemplate" type="text/x-jsrender">

	<tr>
        <td>
			<span class="font-size16">
				{{>ils_title}}
			</span>
		</td>
        <td  align="center">
			{{for userList tmpl='#indexWrapperTemplate'}}
				{{>usr_display_bil}}
				{{else}}
					--
			{{/for}}
		</td>
        <td align="center">
			<lb:get key="label_core_community_management_214"/> {{>ils_start_time}} <lb:get key="time_to"/> {{>ils_end_time}}
		</td>
        <td align="right">
			{{>ils_place}}
		</td>
    </tr>

</script>
<!-- 离线课程 -->
<script id="offflineInfoTemplate" type="text/x-jsrender">
	{{include tmpl="#infoHeaderTemplate"/}}
    <dt>
		 {{include tmpl="#courseTitleTemplate"/}}
         <div class="offheight clearfix">
			<div class="offwidth">
				<span class="grayC999">
					<!--课程类型--><lb:get key="exam_type"/>：
				</span>
				{{>itemTypeStr}}
			</div>
		 </div>

         <p>
			<span class="color-gray999">
				<!--课程简介--><lb:get key="exam_introduction"/>：
			</span>
			{{include tmpl="#openDescTemplate"/}}
			{{include tmpl="#infoBtnTemplate"/}}
    </dt>
</script>
<!-- 离线课程 -->
<script id="classListTemplate" type="text/x-jsrender">
 <tr>
		<td colspan="3">
			<h3 style="margin:0;"><a title="" href="${ctx}/app/course/detail/{{>itm_id}}{{if app}}?tkhId={{>app.app_tkh_id}}{{/if}}" class="wzb-link01">{{:itm_title}}</a></h3>
		</td>
	</tr>
 	<tr>
       <td class="wzb-table-title"><span class="color-gray999"><!--班级编号--><lb:get key="exam_code"/>：</span></td>
		<td class="wzb-table-content"> {{>itm_code}}</td>
       <td width="15%" align="right"><a class="btn wzb-btn-yellow" href="${ctx}/app/course/detail/{{>itm_id}}{{if app}}?tkhId={{>app.app_tkh_id}}{{/if}}" title="" ><!--详情--><lb:get key="label_core_community_management_215"/></a></td>
	</tr>
     <tr>
		  <td class="wzb-table-title"><span class="color-gray999"><!--培训地点--><lb:get key="exam_address"/>：</span></td>
		  <td class="wzb-table-content"  colspan="2">
			{{if ies}}
					{{if ies.ies_schedule}}
						{{:ies.ies_schedule}}
					{{else}}
						--
					{{/if}}
			{{/if}}
		</td>
	 </tr>
      <tr>
			<td class="wzb-table-title" style="padding-bottom: 30px;"><span class="color-gray999"><!--培训期限--><lb:get key="exam_interval"/>：</span></td>
		   <td class="wzb-table-content" colspan="2" style="padding-bottom:30px;"><lb:get key="label_core_community_management_214"/> {{>itm_content_eff_start_time}} <lb:get key="time_to"/>{{>itm_content_eff_end_time}}</td>
	</tr>
</script>
<!-- 积分项目 -->
<script id="ccrTemplate" type="text/x-jsrender">
<tr>
     <td class="width230r">
		<a class="toolcolor" href="javascript:;" >{{>cmt_title}}</a>
	 </td>
     <td>
		{{>cmt_max_score}}
	 </td>
     <td>
		{{if res == null  || res.res_type == null || res.res_type ==''}} {{>cmt_pass_score}}
        {{else}}
			{{if cmt_max_score && cmt_max_score > 0}} {{>cmt_pass_score*cmt_max_score/100}}
			  {{else}}{{>cmt_pass_score}}
			{{/if}}
		{{/if}}
	 </td>
     <td>
		{{>cmt_contri_rate}}%
	 </td>
	{{if mov}}
	 <td>
		{{>mov.mov_score}}
	 </td>
     <td class="text-right">
		{{>mov.statusStr}}
	 </td>
	{{else}}
	 <td>
	 </td>
     <td class="text-right">
	 </td>
	{{/if}}
</tr>
</script>

<!-- 我的状态 -->
<script id="enrollment" type="text/x-jsrender">
	<span class="color-gray999" > <!--报名日期 --> 
		<lb:get key="course_enrollment_date" />：
	</span>
	<span id="enrollment_date">
		{{>att_create_timestamp}}
	</span>

</script>

<script id="add-user-template" type="text/x-jsrender">

	<div class="wzb-percent-3 wzb-display-01">
         <dl class="wzb-list-7 clearfix">
               <dd>
                   <div class="wzb-user wzb-user68">
                       <a href="{{>a}}"><img class="wzb-pic" src="{{>image}}" alt="<lb:get key="know_ta"/>"></a>
                       <p class="companyInfo" style="display: none;"><lb:get key="know_ta"/></p>
                       <div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
                       <div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
                       <div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
                       <div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
                   </div>
               </dd>

               <dt>
                   <a class="wzb-link04" href="{{>a}}" title="">{{>usr_display_bil}}</a>
                   <p>{{>usg_display_bil}}</p>
                   <p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
              	</dt>
         </dl>

		<span id="user_{{>usr_ent_id}}"  class="wzb-user-plus">
			<a name="addUser" class="wzb-link04" href="javascript:;" onclick="addEnrollment({{>usr_ent_id}})" value="{{>usr_ent_id}}">
					<i class="fa fa-plus"></i>
					<lb:get key="button_add"/>
			</a>
		</span>

     </div>


</script>
<!-- template end -->
</head>
<body>
	<form name="frmAction">
		<input type="hidden" name="ent_id_lst" value=""/>
	</form>
	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
			<dl class="wzb-list-19 clearfix">
				<dd>
					<img src="${ctx}/static/images/course2.png" alt="" />

					<div class="wzb-toolbox clearfix">
						<a title='<lb:get key="sns_click_like"/>'
							href="javascript:sns.praise.add('18','Course')"><i
							class="fa skin-color fa-thumbs-o-up"></i>
							<p>
								<lb:get key="sns_like" />
								()
							</p></a> <a class="margin-left35"
							title='<lb:get key="sns_click_collect"/>' href="#"><i
							class="fa skin-color fa-star"></i>
							<p>
								<lb:get key="sns_collect" />
								()
							</p></a>
					</div>
				</dd>


				<dt>
					<h3>
						<a class="wzb-link01" href="online_courses.html"></a>
					</h3>
					<div class="offheight clearfix">
						<div class="offwidth">
							<span class="color-gray999"><lb:get key="course_type" />：</span>
						</div>
						<div class="offwidth">
							<span class="color-gray999"><lb:get key="content_duration" />：</span>
						</div>
					</div>
					<div class="offheight clearfix">
						<div class="offwidth">
							<span class="color-gray999"><lb:get key="content_duration" />：</span>
						</div>
						<div class="offwidth">
							<span class="color-gray999"><lb:get
									key="detail_study_status" />：</span>--
						</div>
					</div>
					<p>
						<span class="color-gray999"><lb:get
								key="course_introduction" />：</span><a class="wzb-show skin-color"
							id="wzb-show"><lb:get key="click_down" /><i
							class="fa fa-angle-down"></i></a>
					</p>
					
					
					
				</dt>
			</dl>
			<!-- wzrow End-->

			<div role="tabpanel" class="wzb-tab-5">
				<ul class="nav nav-tabs" role="tablist" id="nav1">
				</ul>

				<div class="tab-content" id="box1" style="min-height:260px;">
					<div role="tabpanel" class="tab-pane" id="courseList">
						 <div id="content-tip">
						 </div>
						 <div class="xyd-wang" id="contentList">
							<!--列表 -->
			                <div class="xyd-wang-box">
			                </div>

			                <div class="dirMd">
			                </div>

			                <div class="noDirMd">

			                </div>
              			</div>
					</div>
					<div role="tabpanel" class="tab-pane" id="dateList">
						<!--         日程表 -->
						<table class="table wzb-ui-table">

							<tr class="wzb-ui-table-head">
								<td width="33%"><lb:get key="lesson_title" /></td>
								<td width="20%" align="center"><lb:get key="lesson_instructor" /></td>
								<td width="17%" align="center"><lb:get
										key="lesson_duration" /></td>
								<td width="20%" align="right"><lb:get key="lesson_address" /></td>
							</tr>

						</table>
					</div>
					<div role="tabpanel" class="tab-pane" id="detail"></div>
					
					<!-- CPD时数 -->
                    <c:if test="${hasCPDFunctions eq true }">
                    <div role="tabpanel" class="tab-pane" id="cpdHours">
                    <div class="wzb-item-main margin-top28">
                        <table cellpadding="0" cellspacing="0" style="border-bottom:1px solid #ccc;">
                            <tbody>
                                <tr>
                                    <td class="text-left" width="50%">
                                        <div class="wzb-title-2 margin-top20"><lb:get key="detail_cpdHours_information"/></div>
                                    </td>
                                    <td class="text-right" width="50%">
                                    </td>
                                </tr>
                            </tbody>
                        </table>
            
                        <table cellpadding="0" cellspacing="0" class="margin-top20">
                            <tbody>
                                <tr>
                                    <td class="wzb-form-label"><lb:get key="label_cpd.label_core_cpt_d_management_208"/>：</td>
                                    <td class="wzb-form-control"><span id="accredition_code">--</span></td>
                                </tr>
                                 <c:if test="${item.itm_run_ind != '1'}">
                                    <tr id="detail_cpdHours_end_time">
                                        <td class="wzb-form-label"><lb:get key="detail_cpdHours_end_time"/>：</td>
                                        <td class="wzb-form-control"><span id="aci_hours_end_date"><lb:get key="detail_cpdHours_unlimied"/></span></td>
                                    </tr>
                                 </c:if>
                            </tbody>
                        </table>
             
            
                        <table cellpadding="0" cellspacing="0" class="margin-top20" style="border-bottom:1px solid #ccc;">
                            <tbody>
                                <tr>
                                    <td class="text-left">
                                        <div class="wzb-title-2 margin-top20"><lb:get key="detail_award_hours"/></div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
            
                        <table cellpadding="0" cellspacing="0" class="margin-top20">
                            <tbody>
                                <tr id="cpt_set_hours_list">
                                    <td class="wzb-form-label" width="40%"></td>
                                    <td   width="20%" style="color:#666;padding-left: 20px"><lb:get key="detail_core_hours"/></td>
                                    <td    width="20%" style="color:#666;padding-left: 20px"><lb:get key="detail_non_core_hours"/></td>
                                    <td class="wzb-form-control" width="20%"></td>
                                </tr>
                                  
                      
                            </tbody>
                        </table>
                    
                    </div>
                    </div>
                    </c:if>
                    
					<div role="tabpanel" class="tab-pane" id="notice">
						<table class="table wzb-ui-table" id="messages">
							<tr>
								<th><lb:get key="global_title" /></th>
								<th class="text-right"><lb:get key="course_publish_time" /></th>
							</tr>
						</table>

						<!-- myModal弹出框 Start-->
						<div id="msgBox" class="modal fade" tabindex="-1" role="dialog"
							aria-labelledby="myModalLabel" aria-hidden="true" >
							<div class="modal-dialog">
								<div class="modal-content" style="max-height: 500px; overflow: auto; min-height: 200px;">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true">&times;</button>
										<h4 class="modal-title" id="myModalLabel"></h4>
									</div>
									<div class="modal-body" style='max-height:386px;overflow:auto'></div>
									<div class="modal-footer">
										<button type="button" class="btn wzb-btn-yellow wzb-btn-big align-bottom"
											data-dismiss="modal">
											<lb:get key="button_close" />
										</button>
									</div>
								</div>
							</div>
						</div>
						<!-- myModal弹出框 End-->

					</div>
					<div role="tabpanel" class="tab-pane" id="status">
						<div class="margin-top20 margin-bottom25" id="status-progress">
						
							<div id="ishidden">
								<span class="color-gray999" > <!--报名日期 --> </span>
								<span id="enrollment_date" ></span>
							</div></br>
							
							<span class="color-gray999"> <!--学习进度 --> <lb:get
									key="exam_progress" />
							</span>
							<div style="width: 18%;" class="progress progress-xs" id="progress">
								<div style="width: 20%" class="progress-bar progress-bar-orange"></div>
							</div>

							<span id="progress_type"></span>

							<!--进行中（进度每天更新一次）_ -->
							<span id="progress_type_desc"> <lb:get
									key="course_progress_desc" />
							</span>

						</div>

						<div class="wzb-title-8 skin-color">
							<lb:get key="detail_count_project" />
							<lb:get key="detail_count_project_desc" />
						</div>

						<table class="table wzb-ui-table" id="score_itm_lst">
							<tbody>
								<tr class="wzb-ui-table-head">
									<th width="30%">
										<!-- 标题 --> <lb:get key="global_title" />
									</th>
									<th>
										<!-- 总分 --> <lb:get key="detail_sum_score" />
									</th>
									<th>
										<!-- 及格分 --> <lb:get key="detail_pass_score" />
									</th>
									<th>
										<!-- 及格分 --> <lb:get key="detail_contri_rate" />
									</th>
									<th>
										<!-- 我的分数 --> <lb:get key="detail_my_score" />
									</th>
									<th class="text-right">
										<!-- 状态 --> <lb:get key="global_status" />
									</th>
								</tr>
							</tbody>
						</table>


						<div class="wzb-title-8 skin-color" id="completed_condition">
							<!-- 完成条件 -->
							<lb:get key="detail_complate_condition" />
						</div>

						<div>

							<table class="wzb-table-two" id="cmt_lst">
								<tbody>
									<tr>
										<td class="wzb-table-tdbg" rowspan="3">
											<!-- <p>参与</p>
											<p>要求</p> -->
											<!-- 参与要求 --><lb:get key="detail_access_condition"/>
										</td>
										<td><!-- 标题 --><lb:get key="global_title"/></td>
										<td><!-- 要求 --><lb:get key="detail_need_to"/></td>
										<td class="text-right"><!-- 状态 --><lb:get key="global_status"/></td>
									</tr>
								</tbody>
							</table>

							<table class="wzb-table-two" id="score_required">
								<tbody>
									<tr class="wzb-ui-table-head">
										<td class="wzb-table-tdbg" rowspan="4">
											<p><lb:get key="detail_score_condition"/></p>
											<!-- <p>要求</p> -->
										</td>
										<td><!-- 要求 --><lb:get key="detail_need_to"/></td>
	                        			<td class="text-right"><!-- 我的分数 --><lb:get key="detail_my_score"/></td>
									</tr>


								</tbody>
							</table>

							<table class="wzb-table-two" id="attendance_required">
								<tbody>
									<tr>
										<td class="wzb-table-tdbg" rowspan="2">
											<!-- <p>出席率</p><p>要求</p> -->
											<lb:get key="detail_attendance_condition"/>
										</td>
										<td><!-- 要求 --><lb:get key="detail_need_to"/></td>
										<td><!-- 我的出席率 --><lb:get key="detail_my_attendance"/></td>
									</tr>
								</tbody>
							</table>

						</div>



						<div class="wzb-title-8 skin-color" id="certificate">
							<!--   -->
							<lb:get key="certificate" />
						</div>
						<table class="wzb-table-two fettle_table">
							<!-- <tbody>
								<tr>
									<td>标题</td>
									<td>有效期</td>
									<td></td>
								</tr>
								<tr>
									<td><a class="wzb-link03" href="###" title="谈判技巧资格">谈判技巧资格</a></td>
									<td>2015-12-20</td>
									<td align="right"><a class="skin-color" href="###"
										title="下载">下载</a></td>
								</tr>
								<tr>
									<td colspan="3" align="right">(*只有完成状态下，才能查看。)</td>
								</tr>
							</tbody> -->

							<tr>
								<td>
									<!-- 标题 --> <lb:get key="global_title" />
								</td>
								<td>
									<!-- 有效期 --> <lb:get key="certificate_period_of_validity" />
								</td>
								<td></td>
							</tr>

							<tr> <!-- 列表 after 在这里-->
								<td class="noline text-right" colspan="3"><span
									class="grayC999">(*<!-- 只有完成状态下，才能查看证书 --> <lb:get
											key="certificate_view_need_text" />)
								</span></td>
							</tr>

						</table>
					</div>
				</div>
			</div>

			<c:if test="${hasInstrucotrs == true}">
			<div role="tabpanel" class="wzb-tab-5 margin-top15" id="evaluationDiv">
				<ul class="nav nav-tabs" role="tablist" id="nav2">
					

					<c:if test="${hasInstrucotrs == true}">
						<li class="active" role="presentation"><a style="cursor: text;" href="#score" aria-controls="score"
							role="tab" data-toggle="tab"> <!-- 讲师评分_ --> <lb:get
									key="detail_teacher_valuation" /></a></li>
					</c:if>
				</ul>

				<div class="tab-content" id="box2">
					<%-- <c:if test="${sns_enabled == true}">
					<div role="tabpanel" class="tab-pane active" id="comment_lst">
						<div class="panel-content">
							<form action="#" method="post" class="margin-top10">
								<textarea class="wzb-textarea-01 align-bottom margin-right10" id="courseComment"></textarea>
								<input id="courseCommentSubmit" type="button" value="<lb:get key="btn_submit"/>"
									class="btn wzb-btn-yellow wzb-btn-big align-bottom">
							</form>

							<div class="wzb-title-2 margin-top15" id="commentCount">
								<span> 评论(共0_条)__</span>

								<!-- <em id="wzb-show" class="wzb-show skin-color"><span>展开全部</span><i class="fa fa-angle-down"></i></em> -->
							</div>

							<script type="text/javascript">
								var sns = new Sns();
								var targetId = itmId;
								var module = 'Course';
							</script>
	            			<jsp:include page="../common/comment.jsp"/>

							<!--评论在此处 -->
							<div id="comment_lst_content"></div>
							<script type="text/javascript">
								//评论列表加载
								loadComment(module);
							</script>
						</div>
					</div>
					</c:if> --%>
					
					<div role="tabpanel" class="tab-pane  active" id="instructorComment">
						<div class="margin-top10 color-gray999"><!--满分为5分,请为以下项打分 --><lb:get key="detail_comment_title"/>：</div>

						<form method="post" class="margin-top15" action="#"  style="text-align:center">
							<div class="margin-bottom25">
								<table>
									<tr><!--授课风格 -->
										<td class="wzb-form-label" style="width:43%;padding: 0px 6px 6px 0px;"><lb:get key="comment_style"/>：</td>
										<td class="wzb-form-control" align="left">
											<span id="itc_style_score"></span>
											<input type="hidden" name="itc_style_score" class="wzb-text-01" placeholder='<lb:get key="comment_max_score"/>'>
										</td>
									</tr>
									<tr><!-- 教学质量 -->
										<td class="wzb-form-label" style="width:43%;padding: 0px 6px 6px 0px;"><lb:get key="comment_quality"/>：</td>
										<td class="wzb-form-control" align="left">
											<span id="itc_quality_score"></span>
											<input type="hidden" name="itc_quality_score" class="wzb-text-01" placeholder='<lb:get key="comment_max_score"/>'>
										</td>
									</tr>
									<tr><!--内容结构 -->
										<td class="wzb-form-label" style="width:43%;padding: 0px 6px 6px 0px;"><lb:get key="comment_structure"/>：</td>
										<td class="wzb-form-control" align="left">
											<span id="itc_structure_score"></span>
											<input type="hidden" class="wzb-text-01" name="itc_structure_score" placeholder='<lb:get key="comment_max_score"/>'>
										</td>
									</tr>
									<tr><!--交流互动 -->
										<td class="wzb-form-label" style="width:43%;padding: 0px 6px 6px 0px;"><lb:get key="comment_interaction"/>：</td>
										<td class="wzb-form-control" align="left">
											<span id="itc_interaction_score"></span>
										<input type="hidden" class="wzb-text-01" name="itc_interaction_score"  placeholder='<lb:get key="comment_max_score"/>'>
										</td>
									</tr>
									<tr id='lab_average'><!-- 平均分 -->
										<td class="wzb-form-label" style="width:43%;padding: 0px 6px 6px 0px;"><lb:get key="commnet_avg_score"/>：</td>
										<td class="wzb-form-control" align="left">
											<span id="itc_score"></span>
											<input type="hidden"  name="itc_score" class="wzb-text-01" readonly="readonly" >
										</td>
									</tr>
									<tr id='lab_had_submit_score'><!-- 你已经提交评分了 -->
										<td class="wzb-form-label" style="width:43%;padding: 0px 6px 6px 0px;"></td>
										<td class="wzb-form-control" align="left">
											<div class="margin-top10 color-gray999" ><lb:get key="comment_had_submit_score"/></div>
										</td>
									</tr>
								</table>
							</div>

							<%-- <textarea class="wzb-textarea-01 align-bottom margin-right10" name="itc_comment" placeholder='<lb:get key="comment_please"/>'></textarea> --%>
							<input id="submitInstructor" type="button" value='<lb:get key="btn_submit"/>'
								class="btn wzb-btn-yellow wzb-btn-big align-bottom">
						</form>
					</div>
				</div>
			</div>
			</c:if>

			<script>
				$(function(){
				   $('[data-toggle="tooltip"]').tooltip();
				   $('.js-popover').popover();
				   $("form .wbarea input").prompt();
				   $("textarea.wbpl").prompt();

				   $("input[name=itc_style_score],input[name=itc_quality_score],input[name=itc_structure_score],input[name=itc_interaction_score]").blur(function(){
					  var avgVal = (Number($("input[name=itc_style_score]").val())
							   +Number($("input[name=itc_quality_score]").val())
							   +Number($("input[name=itc_structure_score]").val())
							   +Number($("input[name=itc_interaction_score]").val()))/4
					   if(!(typeof avgVal == undefined) && !isNaN(avgVal)){
					   		$("input[name=itc_score]").val(avgVal);
					   }
				   })

				   //讲师评论
				   $("#submitInstructor").click(function(){
					   if(tkhId == '' || tkhId == undefined){
						   tkhId = 0;
					   }
					   //根据按钮判断是否报名
					  if(allResult.btn==1 || allResult.btn==4  || allResult.btn== 7 || allResult.btn==-1){
						  if(!allResult.app || allResult.app.app_status != 'Admitted'){
							  Dialog.alert( fetchLabel('error_msg_need_app'));
							  return;
						  }
					   }
					   var url = '${ctx}/app/comment/instructor/' + itmId + '/' + tkhId;
				 	   if(!validateScore($("input[name=itc_style_score]"))){
				 		   Dialog.alert(fetchLabel('comment_style')+fetchLabel('know_teacher_score')+fetchLabel('validate_not_null')+"，"+fetchLabel('comment_prompt'));
				 		   return;
					   }
					   if(!validateScore($("input[name=itc_quality_score]"))){
						   Dialog.alert(fetchLabel('comment_quality')+fetchLabel('know_teacher_score')+fetchLabel('validate_not_null')+"，"+fetchLabel('comment_prompt'));
						   return;
					   }
					   if(!validateScore($("input[name=itc_structure_score]"))){
						   Dialog.alert(fetchLabel('comment_structure')+fetchLabel('know_teacher_score')+fetchLabel('validate_not_null')+"，"+fetchLabel('comment_prompt'));
						   return;
					   }
					   if(!validateScore($("input[name=itc_interaction_score]"))){
						   Dialog.alert(fetchLabel('comment_interaction')+fetchLabel('know_teacher_score')+fetchLabel('validate_not_null')+"，"+fetchLabel('comment_prompt'));
						   return;
					   }

					   var data = {
							//note : $("textarea[name=itc_comment]").val(),
							styleScore : $("input[name=itc_style_score]").val(),
							qualityScore : $("input[name=itc_quality_score]").val(),
							structureScore : $("input[name=itc_structure_score]").val(),
							interactionScore : $("input[name=itc_interaction_score]").val(),
					   }
					  /* if(data.note == '' || data.note == undefined || data.note == $("textarea[name=itc_comment]").attr("prompt")){
						   $("textarea[name=itc_comment]").tooltip().attr("data-original-title", fetchLabel('validate_not_null')).focus();
						   return;
					   } */

					 	$.ajax({
							url : url,
							data: data,
							type: 'post',
							async : false,
							dataType: 'json',
							success : function(result){
								if(result.status == 'success'){
									Dialog.alert( fetchLabel('global_operate_'+result.status));
									setInstructorComment(result.instructorComment);
									$("#lab_had_submit_score").show();
									$("#submitInstructor").hide();
									$("#lab_average").show();
								}
							}
						})

				    })

				})

				//验证评分
				function validateScore(obj){
				   var alertText = fetchLabel('validate_must_be') + fetchLabel('validate_gt_eq_0');
				   var alertMaxText = fetchLabel('validate_max_5') + fetchLabel('units_points');
				   $(obj).tooltip();
				   if(!Validate.isDouble($(obj).val())){
					   $(obj).attr("data-original-title", alertText);
					   $(obj).focus().val('');
					   return false;
				   } else if($(obj).val() > 5){
					   $(obj).attr("data-original-title", alertMaxText);
					   $(obj).focus();
					   return false;
				   }else if($(obj).val() <= 0){
					   $(obj).focus();
					   return false;
				   }
				   return true;
				}
			</script>
		</div>
	</div>

	<!-- 选择下属弹出框start -->

	<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="" ria-hidden="true" id="userList">

		<div id="TB_window" style="margin-left: -340px; width: 680px; margin-top: -310px; display: block">
			<div id="TB_title">
				<div id="TB_ajaxWindowTitle"></div>
				<div id="TB_closeAjaxWindow">
					<a aria-label="Close" data-dismiss="modal"
						href="javascript:void(0)" class="TB_closeWindowButton"></a>
				</div>
			</div>
			<div id="TB_ajaxContent" style="width: 680px; height: 620px">
				<div class="wzb-model-3">
					<div class="form-search form-tool">
						<input name="searchContent" type="text" class="form-control"
							placeholder="<lb:get key="attention_find_desc"/>"><input
							type="button" class="form-submit" value=""
							onclick="searchSubordinate()">
					</div>

					<div class="form-tool wzb-title-3">
						<lb:get key="subordinate_filter_result" />

						<div class="form-tool-right">
							<input onclick="addAll()" type="button"
								class="btn wzb-btn-yellow"
								value="<lb:get key="button_add_all"/>">
						</div>
					</div>

					<div class="wzb-percent clearfix"></div>

				</div>
			</div>
		</div>

	</div>

	<!-- 选择下属弹出框end -->


</body>
</html>