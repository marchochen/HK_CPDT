<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<title></title>
<script type="text/javascript">
	var course;
	var len = 80;
	var template = "#inProgressTemplate";
	var appStatus = 'I';
	var userEntId = window.location.href.replace(/.*\//,"");
	
	$(function() {
		course = $(".wbtabcont").table({
			url : '${ctx}/app/course/historyJson/' + userEntId,
			colModel : colModel,
			rp : 10,
			showpager : 10,
			params : {
				appStatus : appStatus
			},
			hideHeader : true,
			usepager : true
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
			$(course).reloadTable({
				url : '${ctx}/app/course/historyJson/' + userEntId,
	 			params : {
					appStatus : appStatus
				},
				colModel : colModel,
			});
		});

	})

	var colModel = [ {
		format : function(data) {
			var is_desc_sub;
			var itm_desc_sub = "";
			if(data.item.itm_desc ){
				itm_desc_sub =  substr(data.item.itm_desc, 0, len);
				if(getChars(data.item.itm_desc) > len){
					itm_desc_sub += "...";
				}
			}
			if(data.item.itm_desc == undefined || getChars(data.item.itm_desc) < len) {
				 is_desc_sub = 'display:none';
			}
			var type = data.item.itm_type;
			var itemType;
			var itemTypeStr;
			var itemDescStr;
			if(data.item.itm_exam_ind == '1'){
				itemTypeStr = fetchLabel('exam_type');
				itemType = getExamTypeStr(type);
				itemDescStr = fetchLabel('exam_introduction');
			} else {
				itemTypeStr = fetchLabel('course_type');
				itemType = getCourseTypeStr(type);
				itemDescStr = fetchLabel('course_introduction');
			}
			var p = {
				title : data.item.itm_title,
				itm_desc_sub : itm_desc_sub,
				is_desc_sub : is_desc_sub,
				itm_desc : data.item.itm_desc,
				time : Wzb.displayTime(data.item.itm_publish_timestamp, Wzb.time_format_ymd),
				start_time : data.item.itm_content_eff_start_time ?
						 Wzb.displayTime(data.item.itm_content_eff_start_time, Wzb.time_format_ymd) : fetchLabel('course_signup_date'),
				end_time : data.item.itm_content_eff_end_time ?
						 Wzb.displayTime(data.item.itm_content_eff_end_time, Wzb.time_format_ymd) : fetchLabel('course_date_nolimit'),
				itm_type : cwn.getItemType(data.item.itm_type),
				itm_id : data.item.itm_id,
				encItmId : wbEncrytor().cwnEncrypt(data.item.itm_id),
				itm_icon : data.item.itm_icon,
				itm_mobile_ind : data.item.itm_mobile_ind,
				parent_title : data.item.parent? data.item.parent.itm_title + "  >  " : "",
				parent_id : data.item.parent? data.item.parent.itm_id : "",
				is_compulsory : data.itd ? data.itd.itd_compulsory_ind == '1' ? "<div class=wzb-bixiu><lb:get key='required'/></div>":"" :"",
				progress : data.cov? data.cov.cov_progress ? data.cov.cov_progress : '0' : '0',
				app_status : getAppStatusStr(data.app_status),
				app_tkh_id : data.app_tkh_id,
				att_timestamp : data.att? Wzb.displayTime(data.att.att_timestamp, Wzb.time_format_ymd):'',		//结训日期,
				itm_exam_ind : data.item.itm_exam_ind,
				item_type_str : itemTypeStr,
				item_type : itemType,
				item_desc_str : itemDescStr
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
					<img class="fwpic" src="${ctx}{{>itm_icon}}">
					<div class="show">
						 <span class="imgArea">
							   <em><lb:get key='index_click_to_study'/></em>
						 </span>
					</div>
			   </div>
			   {{>is_compulsory}}
		  </a>
	 </dd>
	 <dt>
		  {{include tmpl="#itemHrefTemplate"/}}
		  
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999">{{>item_type_str}}：</span>{{>item_type}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="course_progress"/>：</span><div class="progress progress-xs" style="width:60%;"><div class="progress-bar progress-bar-orange" style="width:{{if progress ==0}}1{{/if}}{{>progress}}px"></div></div></div></div>
		  <div class="offheight clearfix"><span class="color-gray999"><lb:get key="course_content_date"/>：</span>{{>start_time}} <lb:get key="time_to"/> {{>end_time}}</div>
		  <p><span class="color-gray999">{{>item_desc_str}}：</span>
				<span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> 
				<a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" >
						<span><lb:get key="click_down"/></span>
						<i class="fa fa-angle-down"></i>
				</a>
		  </p>
	 </dt>
</dl>
</script>

<script id="notIAndCTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
		  <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
			   <div class="main_img">
					<img class="fwpic" src="${ctx}{{>itm_icon}}">
					<div class="show">
						 <span class="imgArea">
							   <em><lb:get key='index_click_to_study'/></em>
						 </span>
					</div>
			   </div>
			   {{>is_compulsory}}
		  </a>
	 </dd>
	 <dt>
		  {{include tmpl="#itemHrefTemplate"/}}
		  
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999">{{>item_type_str}}：</span>{{>item_type}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="course_status"/>：</span>{{>app_status}}</div></div>
		  <div class="offheight clearfix"><span class="color-gray999"><lb:get key="course_content_date"/>：</span>{{>start_time}} <lb:get key="time_to"/> {{>end_time}}</div>
		  <p><span class="color-gray999">{{>item_desc_str}}:</span><span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" ><span><lb:get key="click_down"/></span><i class="fa fa-angle-down"></i></a></p>
	 </dt>
</dl>
</script>

<script id="completedTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
		  <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
			   <div class="main_img">
					<img class="fwpic" src="${ctx}{{>itm_icon}}">
					<div class="show">
						 <span class="imgArea">
							   <em><lb:get key='index_click_to_study'/></em>
						 </span>
					</div>
			   </div>
			   {{>is_compulsory}}
		  </a>
	 </dd>
	 <dt>
		  {{include tmpl="#itemHrefTemplate"/}}
		  
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999">{{>item_type_str}}：</span>{{>item_type}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="course_status"/>：</span>{{>app_status}}</div></div>
		  <div class="offheight clearfix"><span class="color-gray999"><lb:get key="course_content_date"/>：</span>{{>att_timestamp}}</div>
		  <p><span class="color-gray999">{{>item_desc_str}}:</span><span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" ><span><lb:get key="click_down"/></span><i class="fa fa-angle-down"></i></a></p>
	 </dt>
</dl>

</script>
<!-- template end -->
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
			<div role="tabpanel" class="wzb-tab-3">
				<!-- Nav tabs -->
				<ul class="nav nav-tabs wblearn" role="tablist">
					<li role="presentation" class="active" data="I"><a href="#waiting" style="padding-left:0;" aria-controls="waiting" role="tab" data-toggle="tab"><lb:get key="status_inprogress"/></a></li>
					<li role="presentation" data="C"><a href="#refusal" aria-controls="refusal" role="tab" data-toggle="tab"><lb:get key="status_completed"/></a></li>
					<li role="presentation" data="notIAndC"><a href="#passing" aria-controls="passing" role="tab" data-toggle="tab"><lb:get key="status_others"/></a></li>
				</ul>
			            
			    <div class="tab-content">
			        <div class="wbtabcont">
			             
			        </div>
			    </div>
			</div>   
		</div> <!-- xyd-article End-->	
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>