<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript">
			var ctx = "${ctx}";
		</script>
		<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
		<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/front/home/index.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_bdm_${lang}.js"></script>
		<!-- template start -->
		
			<!-- 公开课模板 -->
			<script id="openTemplate" type="text/x-jsrender">
				<li>
	 				<div class="wzb-list-hover">
						<div class="subim">
							<a class="subxian" href="${ctx}/app/course/detail/{{>encItmId}}" title=""><img src="${ctx}{{>itm_icon}}"></a>
			 				<a class="subbox" href="${ctx}/app/course/detail/{{>encItmId}}" title="">{{>itm_title}}</a>

			 				<div class="subcont">
				 				 <div class="subarea">
					  				 <div class="subbar">
											<a class="subinfo" title="" href="${ctx}/app/course/detail/{{>encItmId}}"><em>{{>itm_title}}</em></a>
											<div class="subdesc">
<!--<a title="" href="javascript:;" class="pull-left color-gray666 like"><i class="fa skin-color fa-thumbs-o-up"></i> <span>{{>s_cnt_like_count}}</span></a>-->
 <span class="pull-left"><i class="fa skin-color fa-clock-o"></i>{{>itm_publish_timestamp}}</span></div>
					  		 		</div>
					  		 		<div class="subfilter"></div>
				 		 		</div>
							 </div>
						</div>
					</div>
				</li> 	
			</script>
			
			<!-- 讲师模板 -->
			<script id="instructorTemplate" type="text/x-jsrender">
			  <li>
                 <a href="${ctx}/app/instr/detail/{{>enc_iti_ent_id}}"  class="module-tuwen-teacher">
                     <div class="module-tuwen-bg module-tuwen-bg-{{>color}}" style="margin-right: 10px;">
                        <img src="${ctx}{{>iti_img}}" alt="" class="module-tuwen-bg-img">
                        <p class="module-tuwen-tit-1">{{>iti_name}}</p>
                        <div class="xyd-tool-pingfen">
                        </div>
                        <p class="module-tuwen-tit-2">{{>iti_level}}</p>
                    </div>
                    <div class="module-color-{{>color}} wzb-tab-7-tit">
                        <i class=" tool-station-f"></i>
                        <span>{{>iti_expertise_areas}}</span>
                    </div>
                </a>
              </li>
			</script>
			
			<!-- 精彩专题模板 -->
			<script id="topicTemplate" type="text/x-jsrender">
			<li onclick="window.location.href = '${ctx}/app/learningmap/specialDetail?id={{>enc_ust_id}}'" style="{{>style}}">
		         <div>
		             <img src="${ctx}{{>abs_img}}" alt="">
		             <span class="wzb-list-27-title">{{>ust_title}}</span>
		             <span class="wzb-list-27-tit">{{>ust_summary}}</span>
		         </div>
		         <p style="padding:10px;margin:0;">
		             <i class="list-tool-dian color-gray999">{{>ust_hits}}</i>
		             <i class="list-tool-you"></i>
		         </p>
		    </li>
			</script>
			
			<!-- 公告模板 -->
			<script id="announceTemplate" type="text/x-jsrender">
			    <li><p><i class="fa fa-volume-down {{if !curUserIsRead}}skin-color{{/if}}"></i><a style="display:inline;" href="${ctx}/app/announce?id={{>msg_id}}">{{>msg_title}}</a><span style="float:right;">{{>msg_upd_date}}</span></p></li>
            </script>
			
			<!-- 课程模板 -->
			<script id="courseTemplate" type="text/x-jsrender">
				<li style="{{>style}}">
	               <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
	                   <div class="main_img">
	                       <img class="fwpic" src="${ctx}{{>itm_icon}}"/>
							<div class="show">
								<span class="imgArea"><em><lb:get key='index_click_to_study'/></em></span>
			   				</div>
	                   </div>
	                  <div class="fwimcont">{{>itm_title}}</div>
	                  <div class="fwimbg"></div>
	               </a>
	               <div class="fwdesc">
		     			<div class="clearfix"><span class="pull-left color-gray666"><lb:get key="course_progress"/>：</span>{{if app_status=='I'}}<div style="width:59%;"class="progress progress-xs"><div style="width:{{>cov_progress}}%" class="progress-bar progress-bar-orange"></div></div>{{else}}<span>{{>app_status_str}}<span>{{/if}}</div>
			 			<div class="color-gray666"><lb:get key="index_last_access"/>：{{>cov_last_acc_datetime}}</div>
					</div>
	             </li>
			</script>
			
			
			<!-- 必修和推荐课程模板-->
			<script id="compulsoryAndRecommend" type="text/x-jsrender">
				<li style="{{>style}}">
	               <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">
	                   <div class="main_img">
	                       <img class="fwpic" src="${ctx}{{>itm_icon}}"/>
							<div class="show">
								<span class="imgArea"><em><lb:get key='index_click_to_study'/></em></span>
			   				</div>
	                   </div>
	                  <div class="fwimcont">{{>itm_title}}</div>
	                  <div class="fwimbg"></div>
	               </a>

				  <div class="fwdesc clearfix">
					<div class="pull-left">
      					<i class="fa skin-color fa-thumbs-o-up"></i>
      					<span class="color-gray666">{{>s_cnt_like_count}}</span>
    				</div>
    				<div class="pull-right">
      					<i class="fa skin-color fa-clock-o"></i>
      					<span class="color-gray666">{{>itm_publish_timestamp}}</span>
    				</div>

				  </div>	               

	             </li>
			</script>
			
			
			<!-- 最新课程模板 -->
			<script id="newestCourseTemplate" type="text/x-jsrender">
				<li style="{{>style}}">
					 <a class="fwim" href="${ctx}/app/course/detail/{{>encItmId}}{{if app_tkh_id}}?tkhId={{>app_tkh_id}}{{/if}}">  
						  <div class="main_img">
							   <img class="fwpic" src="${ctx}{{:itm_icon}}">
							   <div class="show">
									<span class="imgArea"><em><lb:get key='index_click_to_study'/></em></span>
							   </div>
						 </div> 
						 <div class="fwimcont">{{>itm_title}}</div>
						 <div class="fwimbg"></div>
					</a>
					
					<div class="fwdesc">
						 <div class="clearfix"><span class="pull-left color-gray666"><lb:get key= "course_number"/>：{{>app_total}}</span></div>
						 <div class="color-gray666"><lb:get key="course_publish_date"/>：{{>itm_publish_timestamp}}</div>
					</div>
				</li>
		   </script>
		   
	 <!-- template end -->
	
	</head>
	
	<body>
		<div class="xyd-wrapper">
			
			<!-- 页面上方轮播图区域 start -->
		    <div class="xyd-main-3 clearfix">
				<!-- 公告start -->
			        <div id="announceContainer" class="xyd-main-3 clearfix" style="background:#ffffcc;display:none;">
			           <div style="padding:6px 0; height:40px;width:984px;margin:0 auto;"> 
			            <span class="gonggao" style="cursor:pointer;" onclick="window.location.href='${ctx}/app/announce'"><lb:get key="announce_title"/></span>
			           <!--  <span class="gonggao-bg"></span> -->
			            <span class="wzb-close-b" onclick="announceContainer_hide();" id="wzb-close-gg"></span>
			            <span class="wzb-list-29" style="overflow: hidden;height: 30px">
			                <ul id="announceList" class="guanggao" style="height: 28px;">
			                </ul>
			            </span>
			           </div> 
			        </div>
			    <!-- 公告end -->
		        <div id="xyd-banner">
		        	<!-- 导航图片容器 start -->
		            <ul id="xyd-banner-pic">
		            </ul>
		            <!-- 导航图片容器 end -->
		            
		            <!-- 导航小圆点容器 start -->
		            <ul id="xyd-banner-pagination">
		            </ul>
		            <!-- 导航小圆点容器 start -->
		        </div>
		    </div>
		    <!-- 页面上方轮播图区域 end -->
		    
		    <!-- 下方主内容区域 start -->
		    <div class="xyd-main clearfix" style="padding-top:0;">
		    
		        <div class="wzb-model-2" style="padding:25px 0 0 0;">
		            <!-- 选课中心开始 -->
		            <div class="xyd-train" style="margin:0;display:none;" id="courseBtnContainer">
		                <div class="wzb-tab-3" style="display:inline-block;">
		                    <ul class="nav nav-tabs wblearn wzb-xuanke">
		                        <li class="active" id="myCourseBtn"><a href="${ctx}/app/course/signup" style="padding-left:0;"><lb:get key="menu_singup_course"/></a></li>
		                        <li id="compulsoryCourseBtn" style="display: none;"><a href="${ctx}/app/course/recommend?r=1" style="padding-left:0;"><lb:get key="label_core_training_management_296"/></a></li>
		                        <li id="recommentCourseBtn"  style="display: none;"><a href="${ctx}/app/course/recommend" style="padding-left:0;"><lb:get key="label_core_training_management_294"/></a></li>
		                        <li id="newestCourseBtn" style="display: none;"><a href="${ctx}/app/course/courseCatalog" style="padding-left:0;"><lb:get key="label_core_training_management_295"/></a></li>
		                    	<li id="moreCourseBtn"><a href="${ctx}/app/course/signup" class="nav-more"><lb:get key="global_more"/></a></li>
		                    </ul>
		                </div>
		            	<span onclick="window.location.href='${ctx}/app/course/courseCatalog'" class="btn wzb-btn-blue-border clearfix" style="float:right;margin:-2px 0 0 0;"><i class="xuexi-r"><lb:get key="label_core_training_management_338"/></i></span>
		            </div>
		            <div class="tab-content" id="courseListContainer">
		                <div class="wbtabcont">
		                    <div class="home">
		                        <ul class="wzb-list-14 wzb-list-14-1 clearfix datatable" id="courseList" style="width:1000px;">
		                            <div style="clear:both;"></div>
		                        </ul>
		                    </div>
		                </div>
		            </div>
		            <!-- 选课中心结束 -->
		            <!-- 精彩专题开始 -->
		            <div class="xyd-train" style="margin:25px 0 0 0;" id="topicTitle">
		                <div class="wzb-tab-3" style="display:inline-block;">
		                    <ul class="nav nav-tabs wblearn wzb-xuanke">
		                        <li class="active" id="myCourseBtn"><a href="${ctx}/app/learningmap/specialTopic" style="padding-left:0;"><lb:get key="label_core_learning_map_82"/></a></li>
		                        <li><a href="${ctx}/app/learningmap/specialTopic" class="nav-more"><lb:get key="global_more"/></a></li>
		                    </ul>
		                </div>
		            </div>
		            <ul id="topicList" class="wzb-list-27 wzb-list-27-1 clearfix">
		            </ul>
		            <!-- 精彩专题结束 -->
		            <!-- 分类开始 -->
		            <%-- <div class="wzb-tab-6">
		                <ul>
		                  <li onclick="window.location.href='${ctx}/app/course/schedule'"><i class="rcbiao" ></i><span style="right: 85px"><lb:get key="label_core_training_management_247"/></span></li>
		                  <li onclick="window.location.href='${ctx}/app/kb/center/index'"><i class="zshi" ></i><span><lb:get key="lab_kb_item"/></span></li>
		                  <li onclick="window.location.href='${ctx}/app/know/allKnow'"><i class="wda"></i><span><lb:get key="label_core_system_setting_59"/></span></li>
		                  <li onclick="window.location.href='${ctx}/app/group/groupList/0'"><i class="qzu"></i><span><lb:get key="label_core_system_setting_62"/></span></li>
		                </ul>
		            </div> --%>
		            <!-- 分类结束 -->
		            <!-- 职级发展学习地图开始 -->
		            <div class="zhiji-bg" id="gradeMapBanner" onclick="window.location.href = '${ctx}/app/learningmap/professionMap'"><lb:get key="lab_profession_map" /><!-- 职级发展学习地图 --></div>
		            <!-- 职级发展学习地图结束 -->
		            <!-- 关键岗位开始 -->
		            <div id="keyPositionBanner" class="gangwei-bg" onclick="window.location.href = '${ctx}/app/learningmap/index'"><lb:get key="label_core_learning_map_23"/></div>
		            <!-- 关键岗位结束 -->
		            
		            <!-- 公开课开始 -->
		            <div class="xyd-train" style="margin:25px 0 0 0;" id="open_title">
		                <div class="wzb-tab-3" style="display:inline-block;">
		                    <ul class="nav nav-tabs wblearn wzb-xuanke">
		                        <li class="active" id="myCourseBtn"><a href="${ctx}/app/course/open" style="padding-left:0;"><lb:get key="label_core_training_management_81"/></a></li>
		                        <li><a href="${ctx}/app/course/open" class="nav-more"><lb:get key="global_more"/></a></li>
		                    </ul>
		                </div>
		            </div>
		            <ul class="wzb-list-14 wzb-width-14 clearfix" id="open_lst">
		            </ul>
		            <!-- 公开课结束 -->
		            
		            <!-- 讲师风采开始 -->
		            <%-- <div class="xyd-train" style="margin:25px 0 0 0;" id="instructorTitle">
		                <div class="wzb-tab-3" style="display:inline-block;">
		                    <ul class="nav nav-tabs wblearn wzb-xuanke">
		                        <li class="active" id="myCourseBtn"><a href="${ctx}/app/instr" style="padding-left:0;"><lb:get key="label_core_basic_data_management_1"/></a></li>
		                        <li><a href="${ctx}/app/instr" class="nav-more"><lb:get key="global_more"/></a></li>
		                    </ul>
		                </div>
		            </div>
		            <div id="instructorInfo" class="module-tuwen wzb-tab-7 clearfix" style="padding:0;width:1010px;">
		                <ul id="instructorList">
		                </ul>
		            </div> --%>
		            <!-- 讲师风采结束 -->
		        </div>
		    </div>
		    <!-- 下方主内容区域 end -->
		</div>
		<!-- 人太多了，提示 -->
		<c:if test="${sys_warning == true}">
			<div class="xyd_warning" id="xyd_warning">
			    <div class="xyd_warning_middle">
			        <em class="xyd_warning_bg"></em>
			        <i class="xyd_warning_deng"></i>
			        <b class="font-size16" style="color:#DAFA10;"><lb:get key="sys_warning" />：</b><lb:get key="sys_warning_user_msg"/>
			        <span class="btn wzb-btn-yellow-border" id="Iknow" style="margin:0 0 0 20px;"><lb:get key="sys_warning_know"/></span>
			    </div>
			</div>
       	</c:if>
	</body>
</html> 