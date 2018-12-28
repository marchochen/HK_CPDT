<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/template.jsp"%>

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	wb_image = '${wb_image}', wb_lang = '${lang}', wb_label_lan = '${label_lan}';
	contextPath = '${ctx}'; globalPageSize = '${sessionScope.globalPageSize}', loginUserId = '${auth_login_profile.usr_id}'
</script>
 <script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
 <script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
 <script type="text/javascript" src="${ctx}/static/js/bootstrap/js/bootstrap.min.js"></script>
 <link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" href="${ctx}/static/css/base.css" />
<link rel="stylesheet" href="${ctx}/static/css/learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery.qtip/jquery.qtip.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css"/>
<script type="text/javascript" src="${ctx}/static/js/jQuery-jcMarquee.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_bdm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_tm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wzb_utils.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>

<title></title>
</head>
<body>
<%@ include file="../../common/meta.kindeditor.jsp"%>
<script type="text/javascript">
	var course,itmParams;
	$(function() {
		 var p = {
				 ust_id:'${ust_id }'
             }
		 itmParams = $.extend(itmParams, p);
		course = $(".wbtabcont").table({
			url : '${ctx}/app/learningmap/specialCourseJson',
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
				url : '${ctx}/app/learningmap/specialCourseJson',
				params : {
					ust_id:'${ust_id }',
					sortname : sortname,
					sortorder : sortorder
				}
			});
		});
		
		$.ajax({  
	        type:'post',      
	        url : '${ctx}/app/learningmap/specialDetailJson',
	        data:{ust_id:'${ust_id }'},
	        cache:false,
	        dataType:'json',  
	         success:function(data){
	        $("#top").html($("#topTemplate").render(data));
	         }  
	   });  
		$.ajax({  
	        type:'post',      
	        url : '${ctx}/app/learningmap/specialExpertsJson',
	        data:{ust_id:'${ust_id }'},
	        cache:false,
	        dataType:'json',
	         success:function(data){
             if(data.length==0){
            	 $(".bg-8").hide();
             }
	        	 for(var i=0; i<data.length; i++){
	        		 data[i].href="${ctx}/app/instr/detail/"+data[i].use_ent_id
	        		 if(data[i].iti_type== 'P'){
							data[i].iti_type = fetchLabel("label_core_basic_data_management_10");
						}else if(data[i].iti_type == 'F'){
							data[i].iti_type= fetchLabel("label_core_basic_data_management_11");
						}else{
							data[i].iti_type="--";
						}
	        		//设置讲师的国际化label
						if(data[i].iti_level== 'J'){
							data[i].iti_level = fetchLabel("label_core_basic_data_management_5");
						}else if(p.iti_level == 'M'){
							data[i].iti_level= fetchLabel("label_core_basic_data_management_6");
						}else if(data[i].iti_level == 'S'){
							data[i].iti_level= fetchLabel("label_core_basic_data_management_7");
						}else if(data[i].iti_level == 'D'){
							data[i].iti_level = fetchLabel("label_core_basic_data_management_8");
						}
						
	           				if(data[i].iti_expertise_areas==null){
	           					data[i].areas="--"
	           				}else{
	           				data[i].areas=data[i].iti_expertise_areas;
	           				}
	        		  data[i].href=contextPath + '/app/instr/detail/' + data[i].use_ent_id;
	        		  var praise="";
		        		if(data[i].iti_score==null){data[i].iti_score=0}
						for(var f=1;f<data[i].iti_score+1;f++)
						{
							if(data[i].iti_score+1>f+0.5)
							{
                        
								praise+="<i class='start-w start-w-full'></i>";
							}
						}
						for(var f=1;f<=5-data[i].iti_score+0.5;f++)
						{
							praise+="<i class='start-w'></i>";
						}
						data[i].iti_score=praise;
	                }
	        	 if(data.length<=4){
	        		 $("#expertul").css({width:200*data.length+10+"px",margin:"0 auto"});
	        	 }
	        	 if(data.length<3){
	                $("#Marquee_x").html($("#expertTemplate").render(data));
	        	 }else{
	        		 $("#mx").html($("#expertTemplate").render(data));
	        	 }
             
            
	         }  
	   });  
		
		
		
		
	})

	var colModel = [ {
		format : function(data) {
			var is_desc_sub;
			var itm_desc_sub = data.itm_desc ? substr(data.itm_desc, 0, len) : "";
			if(data.itm_desc == undefined || getChars(data.itm_desc) < len) {
				 is_desc_sub = 'display:none';
			}
			var tcss="wzb-link01";
			var p = {
				itm_desc_sub : itm_desc_sub,
				is_desc_sub : is_desc_sub,
				title : data.itm_title,
				itm_desc : data.itm_desc,
				itm_id : data.itm_id,
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

	<!-- wzb-breadcrumb End-->
	<div class="wzb-panel" align="center" style="background:none;">

		<div class="panel-body" style="padding:0;">
        <div class="wzb-item-main">
        <!--
            <ul role="tablist" class="nav nav-tabs page-tabs">
                <li class="active" role="presentation">
                    <a href="#basic" data-toggle="tab" role="tab" aria-controls="basic"><lb:get key="label_lm.label_core_learning_map_95" /></a>
                </li>
                <li role="presentation" class="">
                    <a href="#senior" data-toggle="tab" role="tab" aria-controls="senior"><lb:get key="label_lm.label_core_learning_map_96" /></a>
                </li>
            </ul>
     -->
            <!--tab-content start-->
            <!-- <div class="tab-content"> -->
                <!-- tab-pane start -->
                <div id="basic" class="tab-pane active" role="tabpanel">                                
                    <!-- wzb-model-10 start -->
                    <%-- <div style="width:100%;background:#fff;">
                         <img src="${ctx}/static/images/ta01.png" width="984px" alt="" style="display:block;margin:0 auto;">
                    </div> --%>
	<div class="xyd-wrapper" align="left" style="background:none;">
		<div class="zhuanti-bg01" style="background-position:0 0;">
		    <div class="clearfix" style="padding-top:0;background:none;">
		        <!-- wzb-model-10 start -->
		        <div class="wzb-model-10" id="top" style="background:none;padding:30px 30px 35px 40px;"></div>
		                <script type="text/javascript">
		                    $("#shou").click(function(){
		                        $("#shouqi").slideToggle();
		                        if($(this).hasClass("tool-content-hide")){
		                          $(this).removeClass("tool-content-hide");
		                        }else{
		                          $(this).addClass("tool-content-hide");
		                        };
		                    })
		                </script>
		        <!-- wzb-model-10 end -->
				
				<div style="padding-top:0;background:none; padding:0 30px 0;">
			        <div class="bg-8">
			            <span style="font-size:16px;color:#999;"><lb:get key="label_lm.label_core_learning_map_68" /></span>
			                 <input type="hidden" id="flag" value="false">       
			            <div class="module-tuwen clearfix" id="Marquee_x">
			                  <ul id="expertul" ${expertcss}>
			                  <li id="mx">
			                      
			                  </li>
			                </ul>
			            </div>
			            
			            <script>
			            $(function(){
			        			$('#Marquee_x').jcMarquee({ 'marquee':'x','margin_right':'10px','speed':20 });
			              // DIVCSS5提示：10px代表间距，第二个20代表滚动速度
			            });
			            </script>
			
					</div>
		        </div>
		
		        <!-- wzb-model-10 start -->
		        <div class="wzb-model-10">
		            <div class="wzb-tab-2">
		                <!-- nav-tabs start -->
		              	<ul class="nav nav-tabs" role="tablist" id="sortul">
		 					<span>
		 						<!--  课程列表  -->
		                     	<lb:get key="label_lm.label_core_learning_map_47" />
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
		        <!-- wzb-model-10 end -->
		    </div>
		</div>
	</div> <!-- xyd-wrap End-->
</div> <!-- xyd-wrap End-->
 <!-- tab-pane end -->
                <!-- tab-pane start -->
                <div id="senior" class="tab-pane" role="tabpanel" style="background:#f2f2f2;width:414px;">
                   
                </div>
                <!-- tab-pane end -->
            </div>
            <!--tab-content end-->

</div>
        </div>
<!-- template start -->
<script id="topTemplate" type="text/x-jsrender">
<div>
            <div class="tuwen-title-1">
                <h3>{{>ust_title}}</h3>
                    <p>{{>ust_summary}}</p>
            </div>
            <div class="tuwen-pic-1"><img src="${ctx}{{:abs_img }}" alt=""></div>
             {{if  ust_content }}
            <div style="border-top:1px solid rgba(255, 255, 255, 0.5);margin:30px 0 0;">
                <!--<h4 class="tool-content-show" id="shou"><lb:get key="label_lm.label_core_learning_map_66" /></h4>
                <p>{{>date}}</p>-->
                <div id="shouqi" style="padding:30px 0 0;">
                <p>{{: ust_content}}</p>
                </div>
            </div>
      {{/if}}
 </div>
</script>
<!-- template end -->
<!-- template start -->
<script id="expertTemplate" type="text/x-jsrender">

<!-- 一个讲师的情况 -->
    {{if num ==1  }}
              <dl class="wzb-list-18" style="margin-top:-20px;padding:15px 0 0 140px;">
                    <dd><a title="" href=""><img src="{{:abs_img}}"></a></dd>
                    <dt>
                        <p><a class="wzb-link04" href="" title="">{{>title}}</a></p>
                               <p style="margin:0 0 0;display:inline-block;">
                            <span class="color-gray999" style="float:left;"><lb:get key="label_core_basic_data_management_45" />：</span>
                            <span style="display:inline-block;margin:3px 0 0 0;">
                                  {{: iti_score}}
                            </span>
                        </p>
                        <div class="offheight clearfix">
                          <div class="offwidth"><span class="color-gray999"><lb:get key="label_core_basic_data_management_3" />：</span>{{>iti_level}}</div> 
                          <div class="offwidth"><span class="color-gray999"><lb:get key="label_core_basic_data_management_9" />：</span>{{>iti_type}}</div></div>
                        <p>
                            <span class="color-gray999"><lb:get key="label_core_basic_data_management_26" />：</span>
                            <span>{{>areas}}</span>
                        </p>
                    </dt>
                </dl>
{{/if}}
 <!-- 两个讲师的情况 -->
   {{if num ==2 && #index == 0  }}
                <dl class="wzb-list-18" style="margin-top:-20px;width:46%;float:left;padding:15px 0 0 140px;">
                    <dd><a title="" href=""><img src="{{:abs_img}}"></a></dd>
                    <dt>
                        <p><a class="wzb-link04" href="" title="">{{:title}}</a></p>
                               <p style="margin:0 0 0;display:inline-block;">
                            <span class="color-gray999" style="float:left;"><lb:get key="label_core_basic_data_management_45" />：</span>
                            <span style="display:inline-block;margin:3px 0 0 0;">
                            {{: iti_score}}
                            </span>
                        </p>
                        <div class="offheight clearfix">
                          <div class="offwidth"><span class="color-gray999"><lb:get key="label_core_basic_data_management_3" />：</span>{{:iti_level}}</div> 
                          <div class="offwidth"><span class="color-gray999"><lb:get key="label_core_basic_data_management_9" />：</span>{{>iti_type}}</div></div>
                        <p>
                             <span class="color-gray999"><lb:get key="label_core_basic_data_management_26" />：</span>
                            <span data="{{>iti_expertise_areas}}">{{>areas}}</span>
                        
                        </p>
                    </dt>
                </dl>
     {{/if}}
        {{if num ==2 && #index == 1  }}
                <dl class="wzb-list-18" style="margin-top:-20px;width:46%;float:right;padding:15px 0 0 140px;">
                    <dd><a title="" href="{{>href}}"><img src="{{:abs_img}}"></a></dd>
                    <dt>
                        <p><a class="wzb-link04" href="" title="">{{:title}}</a></p>
                                <p style="margin:0 0 0;display:inline-block;">
                            <span class="color-gray999" style="float:left;"><lb:get key="label_core_basic_data_management_45" />：</span>
                            <span style="display:inline-block;margin:3px 0 0 0;">
                              {{: iti_score}}
                            </span>
                        </p>
                        <div class="offheight clearfix">
                          <div class="offwidth"><span class="color-gray999"><lb:get key="label_core_basic_data_management_3" />：</span>{{:iti_level}}</div> 
                          <div class="offwidth"><span class="color-gray999"><lb:get key="label_core_basic_data_management_9" />：</span>{{>iti_type}}</div></div>
                        <p>
                            <span class="color-gray999"><lb:get key="label_core_basic_data_management_26" />：</span>
                                  <span data="{{>iti_expertise_areas}}">{{>areas}}</span>
                        </p>
                    </dt>
                </dl> 
{{/if}}
<!-- 三个或三个以上讲师的情况 -->
   {{if num >2 && #index%4==0}}
             <a href="" class="module-tuwen-teacher">
                          <div class="module-tuwen-bg module-tuwen-bg-green"><!-- 这个颜色有4个循环的 -->
                              <img src="{{:abs_img}}" alt="" class="module-tuwen-bg-img">
                              <p class="module-tuwen-tit-1">{{:title}}</p>
                              <p class="module-tuwen-tit-2">{{:iti_level}}</p>
                              <div class="module-color-green"><!-- 和上面的颜色对应 -->
                                  <span class="xyd-tool-pingfen">
                             {{: iti_score}}
                                  </span>
                              </div>
                          </div>
                      </a>
{{/if}}
  {{if num >2 && #index%4==1}}
                      <a href="" class="module-tuwen-teacher">
                          <div class="module-tuwen-bg module-tuwen-bg-pink">
                              <img src="{{:abs_img}}" alt="" class="module-tuwen-bg-img">
                             <p class="module-tuwen-tit-1">{{:title}}</p>
                              <p class="module-tuwen-tit-2">{{:iti_level}}</p>
                              <div class="module-color-pink">
                                        <span class="xyd-tool-pingfen">
                             {{: iti_score}}
                                  </span>
                              </div>
                          </div>
                      </a>
{{/if}}
  {{if num >2 && #index%4==2}}
                     <a href="" class="module-tuwen-teacher">
                          <div class="module-tuwen-bg module-tuwen-bg-blue">
                               <img src="{{:abs_img}}" alt="" class="module-tuwen-bg-img">
                           <p class="module-tuwen-tit-1">{{:title}}</p>
                              <p class="module-tuwen-tit-2">{{:iti_level}}</p>
                              <div class="module-color-blue">
                                         <span class="xyd-tool-pingfen">
                             {{: iti_score}}
                                  </span>
                              </div>
                          </div>
                      </a>
{{/if}}
  {{if num >2 && #index%4==3}}
                      <a href="" class="module-tuwen-teacher">
                          <div class="module-tuwen-bg module-tuwen-bg-gold">
                              <img src="{{:abs_img}}" alt="" class="module-tuwen-bg-img">
                             <p class="module-tuwen-tit-1">{{:title}}</p>
                              <p class="module-tuwen-tit-2">{{:iti_level}}</p>
                              <div class="module-color-gold">
                                        <span class="xyd-tool-pingfen">
                             {{: iti_score}}
                                  </span>
                              </div>
                          </div>
                      </a>
{{/if}}
</script>
<!-- template end -->
<!-- template start -->
<script id="courseTemplate" type="text/x-jsrender">
<dl class="wzb-list-6">
	 <dd>
 {{if itm_canread==0}}
		  <a class="fwim" href="javascript:void(0);">
   <div class="main_img">
					<img class="fwpic" src="{{>itm_icon}}">
			   </div>
		  </a>
{{else}}
 <a class="fwim" href="javascript:void(0);">
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
		  <p class="{{>titlecss}}" style="color:#6C6C6C"><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind && itm_mobile_ind=='yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</p>
  {{else}}
   <p><a class="{{>titlecss}}" href="javascript:void(0);" title="{{>title}}"><img src="${ctx}/static/images/pc-icon-p.png" width="26px" height="22px"/>{{if itm_mobile_ind && itm_mobile_ind=='yes'}}<img src="${ctx}/static/images/phone-icon-g.png" width="21px" height="22px"/>{{/if}}{{>title}}</a></p>
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
</div>
<%@ include file="../../common/footer.jsp"%>
</body>
</html>