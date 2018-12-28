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
	var course;
	var selectType;
	var itemType;
	var appStatus;
	var isCompulsory;
	var moduleType = "exam";
	var len = 75;
	$(function() {
		course = $(".wbtabcont").table({
			url : '${ctx}/app/'+moduleType+'/recommendJson',
			colModel : colModel,
			rp : globalPageSize,
			showpager : 10,
			hideHeader : true,
			usepager : true
		})
		
		

		//推荐类型
		$("#course>li").click(function() {
			$("#course a").removeClass("cur");
			$(this).children("a").addClass('cur');
			selectType = $(this).attr("data");
			$(course).reloadTable({
				url : '${ctx}/app/'+moduleType+'/recommendJson',
				params : {
					selectType : selectType
				}
			});
			//课程类型复位
			itemType = "";
			$("#course_type i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$("#course_type a[data='']").children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")
			//学习状态复位
			appStatus = "";
			$("#app_status i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$("#app_status a[data='']").children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")

			$(".wblearn>li:eq(0)").addClass('active').siblings().removeClass('active');
			
		})
		//课程类型 
		$("#course_type a").click(function() {
			itemType = $(this).attr("data");
			$("#course_type i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$(this).children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")
			$(course).reloadTable({
				url : '${ctx}/app/'+moduleType+'/recommendJson',
				params : {
					selectType : selectType,
					itemType : itemType,
					appStatus : appStatus,
					isCompulsory : isCompulsory
				}
			});
		})
		//学习状态
		$("#app_status a").click(function() {
			appStatus = $(this).attr("data");
			$("#app_status i").removeClass("fa-circle").removeClass("skin-color").addClass("fa-circle-thin");
			$(this).children("i").removeClass("fa-circle-thin").addClass("fa-circle skin-color")
			$(course).reloadTable({
				url : '${ctx}/app/'+moduleType+'/recommendJson',
				params : {
					selectType : selectType,
					itemType : itemType,
					appStatus : appStatus,
					isCompulsory : isCompulsory
				}
			});
		})


		// 所有课程  必修  选修
		$(".wblearn>li").click(function() {
			$(this).addClass('active').siblings().removeClass('active');
			isCompulsory = $(this).attr("data");
			var p = {
				isCompulsory : isCompulsory,
				selectType : selectType,
				itemType : itemType,
				appStatus : appStatus
			}
			$(course).reloadTable({
				url : '${ctx}/app/'+moduleType+'/recommendJson',
				params : p
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
				app_tkh_id : data.app?data.app.app_tkh_id:undefined,
				itm_desc_sub : itm_desc_sub.replace(/\r\n/g,"<br />").replace(/\n/g,"<br />"),
				is_desc_sub : is_desc_sub,
				title : data.item.itm_title,
				itm_desc : data.item.itm_desc,
				itm_type : fetchLabel('exam_'+data.item.itm_type.toLowerCase()),
				itm_id : data.item.itm_id,
				encItmId : wbEncrytor().cwnEncrypt(data.item.itm_id),
				itm_icon : data.item.itm_icon,
				parent_title : data.item.parent? data.item.parent.itm_title + "  >  " : "",
				parent_id : data.item.parent? data.item.parent.itm_id : "",
				itm_mobile_ind : data.item.itm_mobile_ind,
				status : data.app?data.app.app_status == undefined?'--':data.app.app_status:fetchLabel('status_notapp'),
				is_compulsory : data.itd_compulsory_ind == undefined ? "":data.itd_compulsory_ind == '1' ? "<div class=wzb-bixiu><lb:get key='required'/></div>": "" 
			};
			return $('#courseTemplate').render(p);
		}
	} ];
</script>

<!-- template start -->
<script id="courseTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
		  <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
			   <div class="main_img">
					<img class="fwpic" src="{{>itm_icon}}">
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
		  <p>
		     <a class="wzb-link01" href="${ctx}/app/course/detail/{{>parent_id}}" title="" >{{>parent_title}}</a>
			 <a class="wzb-link01" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}" title="" ><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind == 'yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</a>
		  </p>
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999"><lb:get key="exam_type"/>：</span>{{>itm_type}}</div> <div class="offwidth"><span class="color-gray999"><lb:get key="course_status"/>：</span>{{>status}}</div></div>
		  <p><span class="color-gray999"><lb:get key="exam_introduction"/>：</span><span data="{{>itm_desc}}">{{>itm_desc_sub}}</span> <a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" ><span><lb:get key="click_down"/></span><i class="fa fa-angle-down"></i></a></p>
	 </dt>
</dl>
</script>

<!-- template end -->
</head>
<body>

<div class="xyd-wrapper">
        
	  <div id="main" class="xyd-main clearfix">
		
		
		<div class="xyd-sidebar">
            <h3 class="wzb-title-4 skin-bg"><lb:get key="recommend_exam"/> <i class="fa fa-sanzuo fa-caret-up"></i></h3>
            <%-- 
            <h3 class="wzb-title-7 skin-color"><lb:get key="recommend_type"/></h3>
            
            <ul class="wzb-list-17" id="course">
                <li data=""><a class="cur" href="javascript:;" title=""><lb:get key="status_all"/></a></li>
                <li data="tadm"><a href="javascript:;" title=""><lb:get key="recommend_tadm"/></a></li>
                <li data="grade"><a href="javascript:;" title=""><lb:get key="recommend_grade"/></a></li>
                <li data="group"><a href="javascript:;" title=""><lb:get key="recommend_group"/></a></li>
                <li data="sup"><a href="javascript:;" title=""><lb:get key="recommend_sup"/></a></li>
                <li data="position"><a href="javascript:;" title=""><lb:get key="recommend_position"/></a></li>
            </ul>
             --%>
            <div class="wzb-model-8">
                 <div class="wzb-title-2 skin-color"><lb:get key="condition"/></div>
                 
                 <dl class="wzb-list-4" id="course_type">
                     <dt><lb:get key="exam_type"/>：</dt>
                     <dd><a href="javascript:;" title="" data=""><i class="fa margin-right10 fa-circle skin-color"></i><lb:get key="status_all"/></a></dd>
                     <dd><a href="javascript:;" title="" data="selfstudy"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="exam_selfstudy"/></a></dd>
                     <dd><a href="javascript:;" title="" data="classroom"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="exam_classroom"/></a></dd>
                 </dl> 
                 
                 <dl class="wzb-list-4" id="app_status">
                     <dt><lb:get key="recommend_study_status"/>：</dt>
                     <dd><a href="javascript:;" title="" data=""><i class="fa margin-right10 fa-circle skin-color"></i><lb:get key="status_all"/></a></dd>
                     <dd><a href="javascript:;" title="" data="notapp"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="status_notapp"/></a></dd>
                     <dd><a href="javascript:;" title="" data="inprocessed"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="status_inprogress"/></a></dd>
                     <dd><a href="javascript:;" title="" data="pending"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="status_pending"/></a></dd>
                     <dd><a href="javascript:;" title="" data="completed"><i class="fa margin-right10 fa-circle-thin"></i><lb:get key="status_completed"/></a></dd>
                 </dl> 
            </div>
         </div> <!-- xyd-sidebar End-->

		 <div class="xyd-article">
			  <div role="tabpanel" class="wzb-tab-3">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs wblearn" role="tablist">
                        <li data="" role="presentation" class="active"><a href="#waiting" aria-controls="waiting" role="tab" data-toggle="tab"><lb:get key="recommend_all_exam"/></a></li>
                        <li data="1" role="presentation"><a href="#passing" aria-controls="passing" role="tab" data-toggle="tab"><lb:get key="required"/></a></li>
                        <li data="0" role="presentation"><a href="#refusal" aria-controls="refusal" role="tab" data-toggle="tab"><lb:get key="elective"/></a></li>
                    </ul>
                    
                    <!-- Tab panes -->
                    <div class="tab-content">
                     <div class="wbtabcont">
                
                     </div>
                </div>
              </div> <!-- wzb-tab-3 end -->
		 </div> <!-- xyd-article End-->

		</div>
	</div>
	<!-- xyd-wrapper End-->

</body>
</html>