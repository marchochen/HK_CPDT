<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/js/urlparam.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>

<title></title>
<script type="text/javascript">
	var course;
	var selectType;
	var itemType;
	var template = "#inProgressTemplate";
	var appStatus = getUrlParam('data');
	var len = 75;
	var app_status_id = "inprogeress";
	if (appStatus != undefined && appStatus == 'C') {
		template = "#completedTemplate";
		app_status_id = "completed";
	} else if (appStatus != undefined && appStatus == 'notIAndC') {
		template = "#notIAndCTemplate";
		app_status_id = "notIAndC";
	} else if (appStatus != undefined && appStatus == 'I') {
		template = "#inProgressTemplate";
		app_status_id = "inprogeress";
	}
	
	$(function() {
		//个人主页学习概括链接跳转
		if (appStatus) {
			$("#"+app_status_id).addClass('active').siblings().removeClass('active');
		}else{
			appStatus = 'I'
		}

		course = $(".wbtabcont").table({
			url : '${ctx}/app/exam/getMyExam',
			colModel : colModel,
			rp : globalPageSize,
			params : {
				appStatus : appStatus
			},
			showpager : 10,
			hideHeader : true,
			usepager : true
		})

		//课程类型
		$("#course>li").click(function() {
			$("#course a").removeClass("cur");
			$(this).children("a").addClass('cur');
			itemType = $(this).attr("data");
			$(course).reloadTable({
				url : '${ctx}/app/exam/getMyExam',
				params : {
					itemType : itemType,
					appStatus : appStatus
				}
			});
			
			$("#isCompulsory i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$("#isCompulsory a[data='']").children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")
			
// 			$(".wblearn>li:eq(0)").addClass('active').siblings().removeClass('active');

		})
		
		//必修选修
		$("#isCompulsory a").click(function() {
			var isCompulsory = $(this).attr("data");
			$("#isCompulsory i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$(this).children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")
			$(course).reloadTable({
				url : '${ctx}/app/exam/getMyExam',
				params : {
					selectType : selectType,
					itemType : itemType,
					isCompulsory : isCompulsory,
					appStatus : appStatus
				}
			});
		})

		// 学习中 审批中 已结束
		$(".wblearn>li").click(function() {
			$(this).addClass('active').siblings().removeClass('active');
			appStatus = $(this).attr("data");
			if (appStatus == 'C') {
				template = "#completedTemplate"
			} else if (appStatus == 'notIAndC') {
				template = "#notIAndCTemplate"
			} else if (appStatus == 'I') {
				template = "#inProgressTemplate"
			}
			var p = {
				appStatus : appStatus,
				selectType : selectType,
				itemType : itemType
			}
			$(course).reloadTable({
				url : '${ctx}/app/exam/getMyExam',
				params : p,
				colModel : colModel,
			});
		});

		

	})

	var colModel = [ {
		format : function(data) {
			var is_desc_sub;
			var itm_desc_sub = data.item.itm_desc ? substr(data.item.itm_desc, 0, len) : "";
			if(getChars(data.item.itm_desc) > len){  
				itm_desc_sub+="...";  
            }
			if(data.item.itm_desc == undefined || getChars(data.item.itm_desc) < len) {
				 is_desc_sub = 'display:none';
			}
			var p = {
				title : data.item.itm_title,
				itm_desc_sub : itm_desc_sub.replace(/\r\n/g,"<br />").replace(/\n/g,"<br />"),
				is_desc_sub : is_desc_sub,
				itm_desc : data.item.itm_desc,
				time : Wzb.displayTime(data.item.itm_publish_timestamp, Wzb.time_format_ymd),
				start_time : data.item.itm_content_eff_start_time ?
						 Wzb.displayTime(data.item.itm_content_eff_start_time, Wzb.time_format_ymd) : fetchLabel('course_signup_date'),
				end_time : data.item.itm_content_eff_end_time ?
						 Wzb.displayTime(data.item.itm_content_eff_end_time, Wzb.time_format_ymd) : fetchLabel('course_date_nolimit'),
				itm_period_type : data.item.itm_online_content_period,
				itm_type : cwn.getItemType(data.item.itm_type),
				itm_type_db : data.item.itm_type,
				itm_id : data.item.itm_id,
				encItmId : wbEncrytor().cwnEncrypt(data.item.itm_id),
				itm_icon : data.item.itm_icon,
				parent_title : data.item.parent? data.item.parent.itm_title + "  >  " : "",
				parent_id : data.item.parent? data.item.parent.itm_id : "",
				is_compulsory : data.itd ? data.itd.itd_compulsory_ind == '1' ? "<div class=wzb-bixiu><lb:get key='required'/></div>":"" :"",
				progress : data.cov? data.cov.cov_progress ? data.cov.cov_progress : '0' : '0',
				app_status : getAppStatusStr(data.app_status),
				app_tkh_id : data.app_tkh_id,
				itm_mobile_ind : data.item.itm_mobile_ind,
				att_timestamp : data.att? Wzb.displayTime(data.att.att_timestamp, Wzb.time_format_ymd):''		//结训日期
			};
			return $(template).render(p);
		}
	} ];
</script>

<!-- template start -->
<script id="itemHrefTemplate" type="text/x-jsrender">
<p>
	<a class="wzb-link01" href="${ctx}/app/course/detail/{{>parent_id}}" title="" >{{>parent_title}}</a>
	<a class="wzb-link01" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}" title="" ><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind == 'yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</a>
</p>
</script>

<script id="inProgressTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
		  <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
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
	      {{include tmpl="#itemHrefTemplate"/}}
		 
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999"><lb:get key="exam_type"/>：</span> {{>itm_type}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="course_progress"/>：</span><div class="progress progress-xs" style="width:60%;"><div class="progress-bar progress-bar-orange" style="width:{{if progress != 0}}{{>progress}}{{else}}10{{/if}}px"></div></div></div></div>
		  <div class="offheight clearfix"><span class="color-gray999">
		  	{{if itm_type_db == 'SELFSTUDY'}}
				<lb:get key="content_duration"/>：</span>
				{{if itm_period_type == 0}}
					<lb:get key="time_unlimited"/>
				{{else}}
					<lb:get key="time_by"/> {{>end_time}} <lb:get key="time_before"/>
				{{/if}}
		 	 {{else}}
				<lb:get key="course_class_period"/>：</span>
				<lb:get key="time_from"/> {{>start_time}} <lb:get key="time_to"/> {{>end_time}}</div>
				<p><span class="color-gray999"><lb:get key="course_introduction"/>： </span><span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" ><span><lb:get key="click_down"/></span><i class="fa fa-angle-down"></i></a></p>
		 	 {{/if}}
		  </div>
	 </dt>
</dl>
</script>

<script id="notIAndCTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
		  <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
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
	      {{include tmpl="#itemHrefTemplate"/}}
		 
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999"><lb:get key="course_type"/>：</span> {{>itm_type}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="course_status"/>：</span>{{>app_status}}</div></div>
		  <div class="offheight clearfix"><span class="color-gray999">
			  {{if itm_type_db == 'SELFSTUDY'}}
					<lb:get key="content_duration"/>：</span>
					{{if itm_period_type == 0}}
						<lb:get key="time_unlimited"/>
					{{else}}
						<lb:get key="time_by"/> {{>end_time}} <lb:get key="time_before"/>
					{{/if}}
			  {{else}}
					<lb:get key="course_class_period"/>：</span>
					<lb:get key="time_from"/> {{>start_time}} <lb:get key="time_to"/> {{>end_time}}</div>
					<p><span class="color-gray999"><lb:get key="course_introduction"/>： </span><span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" ><span><lb:get key="click_down"/></span><i class="fa fa-angle-down"></i></a></p>
		  	  {{/if}}
		  </div>
	 </dt>
</dl>

</script>

<script id="completedTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
		  <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
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
	      {{include tmpl="#itemHrefTemplate"/}}
		 
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999"><lb:get key="course_type"/>：</span> {{>itm_type}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="course_status"/>：</span>{{>app_status}}</div></div>
		  <div class="offheight clearfix"><span class="color-gray999">
			  {{if itm_type_db == 'SELFSTUDY'}}
					<lb:get key="content_duration"/>：</span>
					{{if itm_period_type == 0}}
						<lb:get key="time_unlimited"/>
					{{else}}
						<lb:get key="time_by"/> {{>end_time}} <lb:get key="time_before"/>
					{{/if}}
			  {{else}}
					<lb:get key="course_class_period"/>：</span>
					<lb:get key="time_from"/> {{>start_time}} <lb:get key="time_to"/> {{>end_time}}</div>
					<p><span class="color-gray999"><lb:get key="course_introduction"/>： </span><span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" ><span><lb:get key="click_down"/></span><i class="fa fa-angle-down"></i></a></p>
		  	  {{/if}}
		  </div>
	 </dt>
</dl>
</script>
<!-- template end -->
</head>
<body>
	<div class="xyd-wrapper">
        
		<div id="main" class="xyd-main clearfix">	
			<div class="xyd-sidebar">
                <h3 class="wzb-title-4 skin-bg"><lb:get key="exam_signup"/> <i class="fa fa-sanzuo fa-caret-up"></i></h3>
                
                <h3 class="wzb-title-7 skin-color"><lb:get key="exam_type"/></h3>
                
                <ul class="wzb-list-17" id="course">
                    <li data=""><a class="cur" href="javascript:;" title=""><lb:get key="status_all"/> </a></li>
                    <li data="selfstudy"><a href="javascript:;" title=""><lb:get key="exam_selfstudy"/> </a></li>
                    <li data="classroom"><a href="javascript:;" title=""><lb:get key="exam_classroom"/> </a></li>
                    <%--  <li data="integrated"><a href="javascript:;" title=""><lb:get key="integrated"/> </a></li>  --%>
                </ul>
                
                <%-- <div class="wzb-model-8">     
                    <dl class="wzb-list-4" id="isCompulsory">
                        <dt><lb:get key="condition"/>：</dt>
                        <dd><a href="javascript:;" title="" data=""><i class="fa margin-right10 fa-circle skin-color"></i><lb:get key="status_all"/></a></dd>
                        <dd><a href="javascript:;" title="" data="1"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="recommend_exam"/></a></dd>
                        <dd><a href="javascript:;" title="" data="0"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="exam_myself"/></a></dd>
                    </dl> 
                </div> --%>
            </div> <!-- xyd-sidebar End-->

			<div class="xyd-article">
				 <div role="tabpanel" class="wzb-tab-3">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs wblearn" role="tablist">
                        <li id="inprogeress" data="I" role="presentation" class="active"><a href="#waiting" aria-controls="waiting" role="tab" data-toggle="tab"><lb:get key="status_inprogress"/></a></li>
                        <li id="completed" data="C" role="presentation"><a href="#refusal" aria-controls="refusal" role="tab" data-toggle="tab"><lb:get key="status_completed"/></a></li>
                        <li id="notIAndC" data="notIAndC" role="presentation"><a href="#passing" aria-controls="passing" role="tab" data-toggle="tab"><lb:get key="status_others"/></a></li>
                    </ul>
                    
                    <!-- Tab panes -->
                    <div class="tab-content">
                         <div class="wbtabcont">
                         
                         </div>
                    </div>
               </div> <!-- wbtab end -->
			</div> <!-- xyd-article End-->

		</div>
	</div>
	<!-- xyd-wrapper End-->

</body>

</html>