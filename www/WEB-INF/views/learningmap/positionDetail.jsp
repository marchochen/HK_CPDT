<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css" />
<script type="text/javascript" src="${ctx}/static/js/jQuery-jcMarquee.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_bdm_${lang}.js"></script>
<title></title>
</head>
<body>
	<script type="text/javascript">
	wbEncrytor = new wbEncrytor;
	
	var course,itmParams;
	$(function() {
		 var p = {
				 upm_id:'${lrnMap.upm_id }'
             }
		 itmParams = $.extend(itmParams, p);
		course = $(".wbtabcont").table({
			url : '${ctx}/app/learningmap/positionCourseJson',
			colModel : colModel,
			rp : globalPageSize,
			showpager : 10,
			hideHeader : true,
			usepager : true,
			params : itmParams
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
				url : '${ctx}/app/learningmap/positionCourseJson',
				params : {
					upm_id:'${lrnMap.upm_id }',
					sortname : sortname,
					sortorder : sortorder
				}
			});
		});
		
	})

	var colModel = [ {
		format : function(data) {
			var is_desc_sub;
			var itm_desc_sub = data.itm_desc ? substr(data.itm_desc, 0, len) : "";
			if(data.itm_desc == undefined || getChars(data.itm_desc) < len) {
				 is_desc_sub = 'display:none';
			}
			tcss="wzb-link01";
			var p = {
				itm_desc_sub : itm_desc_sub,
				is_desc_sub : is_desc_sub,
				title : data.itm_title,
				itm_desc : data.itm_desc,
				itm_id : wbEncrytor.cwnEncrypt(data.itm_id),
				itm_type : fetchLabel(data.itm_type.toLowerCase()),
				time : Wzb.displayTime(data.itm_publish_timestamp, Wzb.time_format_ymd),
				itm_icon : data.itm_icon,
				itm_canread:data.itm_canread,
				itm_mobile_ind : data.itm_mobile_ind,
				titlecss:tcss
			};
			return $('#courseTemplate').render(p);
		}
	} ];
	</script>
	<script type="text/javascript">
	function randomPosition(){
		$.ajax({  
	        type:'post',      
	        url : '${ctx}/app/learningmap/otherPositionJson',  
	        data:{upm_id:'${lrnMap.upm_id }'}, 
	        cache:false,
	        dataType:'json',  
	         success:function(data){
	        	 if(data.length==0){
	        		 $("#other").hide();
	        	 }else{
	        		 for(var i=0; i<data.length; i++){
	        			 (data[i])["upt_id"] = wbEncrytor.cwnEncrypt((data[i])["upt_id"]);
	        		 }
	       	 $("#wbptcont").html($("#otherPositionTemplate").render(data));
	        	 }
	         }  
	   });  
	}
	$(function() {
		randomPosition();
		
	})

	</script>
<div class="xyd-wrapper">
<div class="xyd-main-3 clearfix" style="height:428px;">
  <div class="bg-5">
    <div class="bg-6"><!-- 如果用户有上传图片就用类名 “bg-6”；没有就用 “bg-7” -->
     <img src="${ctx}${lrnMap.abs_img}" alt=""><!-- 用户上传的图片 -->
        <h3 class="wenzi-4">${lrnMap.upt_title}</h3>
        <p class="wenzi-5"><lb:get key="label_lm.label_core_learning_map_64" />：</p>
        <p class="wenzi-6">${lrnMap.upt_desc}</p>
    </div>
  </div>
</div>
</div> <!-- xyd-wrap End-->

<div class="xyd-wrapper">
    <div class="xyd-main clearfix" style="padding-top:0;">
        <div class="wzb-model-10">
            <div class="wzb-tab-2">
                <!-- nav-tabs start -->
              	<ul class="nav nav-tabs" role="tablist" id="sortul">
 					<span>
 						<!--  课程列表  -->
                     	<lb:get key="course_list"/>
 					</span>
 					
 					<li id="itm_publish_timestamp" data="asc" role="presentation" class="active">
 						<a href="#home" aria-controls="home" role="tab" data-toggle="tab">
 							<!-- 发布日期  --><lb:get key="course_publish_date"/>
 							<i class="fa fa-caret-up"></i>
 						</a>
 					</li>
 					
 					<li id="itm_title" data="asc" role="presentation" class="">
 						<a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">
 							<!-- 标题 --> <lb:get key="global_title"/>
 							<i class="fa fa-caret-down"></i>
 						</a>
 					</li>
 					
				</ul><!-- nav-tabs end -->
                
                <!-- tab-content start -->
                <div class="tab-content">
                    <div class="wbtabcont datatable" style="width: 100%;">
                      
                    </div><!-- wbtabcont end -->
                </div><!-- tab-content end -->
            </div>
        </div>
        
    </div>

  <div class="xyd-main clearfix" id="other">

        <!-- nav-tabs start -->
        <ul>
            <li style="float:left;font-size:16px;color:#333;"><lb:get key="label_lm.label_core_learning_map_62" /></li>
          <c:if test="${otherNum > 8 }">
            <li style="float:right;">
                <a href="javascript:void(0);" onclick="randomPosition();" style="color:#00aeef;">
                   <lb:get key="label_lm.label_core_learning_map_63" />
                </a>
            </li>
            </c:if>
        </ul><!-- nav-tabs end -->
        <div class="wzb-list-25 wzb-list-26">
            <ul id="wbptcont">
            </ul>
        </div>
    </div>
</div> <!-- xyd-wrap End-->
<!-- template start -->
<script id="courseTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
  {{if itm_canread==0}}
		  <span class="fwim" href="javascript:void(0);">
			<i class="wuquanxian"></i>
   				<div class="main_img">
					<img class="fwpic" src="{{>itm_icon}}">
			   </div>
		  </span>
{{else}}
 <a class="fwim" href="${ctx}/app/course/detail/{{>itm_id}}">
   <div class="main_img">
					<img class="fwpic" src="{{>itm_icon}}">
					<div class="show">
						 <span class="imgArea">
							   <em><lb:get key='index_click_to_study'/></em>
						 </span>
					</div>
			   </div>
		  </a>
{{/if}}
			
	 </dd>
	 <dt>
 {{if itm_canread==0}}
<p class="{{>titlecss}}"  style="color:#6C6C6C"><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind && itm_mobile_ind=='yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</p>
  {{else}}
   <p><a class="{{>titlecss}}" href="${ctx}/app/course/detail/{{>itm_id}}"><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind && itm_mobile_ind=='yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</a></p>
{{/if}}
		  <div class="offheight clearfix"><div class="offwidth"><span class="color-gray999"><lb:get key="course_type"/>：</span>{{>itm_type}}</div></div>
		  <div class="offheight clearfix"><span class="color-gray999"><lb:get key="course_publish_date"/>：</span>{{>time}}</div>
		  <p>
		  	<span class="color-gray999"><lb:get key="course_introduction"/>：</span>
			<span data="{{>itm_desc}}">{{>itm_desc_sub}}</span>
			<a class="wzb-show skin-color open_desc" style="{{>is_desc_sub}}" >
				<span><lb:get key="click_down"/></span>
				<i class="fa fa-angle-down"></i>
			</a>
		 </p>

	 </dt>
</dl>
</script>
<!-- template end -->

<script id="otherPositionTemplate" type="text/x-jsrender">
  {{if #index== 0}}
          <li class="bgcolor-00cf97 main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                    <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  {{if #index== 1}}
                <li class="bgcolor-f1a90e main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                     <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  {{if #index== 2}}
                <li class="bgcolor-00afef main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                   <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  {{if #index== 3}}
                <li class="bgcolor-db1e72 main_img_2" style="margin-right:0;" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                    <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  {{if #index== 4}}
                <li class="bgcolor-00afef main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                   <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  {{if #index== 5}}
                <li class="bgcolor-a760c3 main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                  <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  {{if #index== 6}}
                <li class="bgcolor-99cd32 main_img_2" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                 <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  {{if #index== 7}}
                <li class="bgcolor-407ce1 main_img_2" style="margin-right:0;" onclick="window.location.href='${ctx}/app/learningmap/detail?id={{:upt_id }}'">
                  <img src="${ctx}{{:abs_img}}" alt="">
                    <i>
                        <a href="#">{{:upt_title  }}</a>
                    </i>
                </li>
{{/if}}
  
                       
                       
</script>
<script id="indexTemplate" type="text/x-jsrender">

</script>



</body>
</html>