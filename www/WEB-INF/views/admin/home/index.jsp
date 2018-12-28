<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title><lb:get key='global.page_title'/></title>
		<script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
		<link rel="stylesheet" href="${ctx}/static/js/bootstrap/css/bootstrap.css"/>
		<link rel="stylesheet" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css"/>
		<link rel="stylesheet" href="${ctx}/mobile/css/basic.css"/>
		<link rel="stylesheet" href="${ctx}/static/css/base.css"/>
		<link rel="stylesheet" href="${ctx}/static/admin/css/admin.css"/>
		<link href="${ctx}/static/admin/css/scrollbar/jquery.mCustomScrollbar.css" rel="stylesheet" />
		<script src="${ctx}/static/admin/js/scrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
		<script type="text/javascript">
		    contextPath = '${ctx}';
		</script>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/global_${lang}.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
		<script type="text/javascript" src="${ctx}/js/gen_utils.js"></script>
		<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
		<script type="text/javascript" src="${ctx}/static/admin/js/base.js"></script>
		<script type="text/javascript" src="${ctx}/js/${label_lan}/wb_label.js"></script>

		<script type="text/javascript" src="${ctx}/static/admin/js/home/index.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/jquery.mousewheel.min.js"></script>

		<script type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
		<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>

		<!-- layer -->
		<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
		<link rel="stylesheet" href="${ctx}/static/js/layer/skin/layer.css"/>

		<c:if test="${lang eq 'en-us'}">
			<!-- 兼容英文的css -->
			<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
			<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
		</c:if>

		<script>
			 itm_lst = new wbItem;
		</script>

		<!-- 快速通道彈出框每一項模板 -->
		<script type="text/x-jquery-tmpl" id="favorite_fun">
				<div class="wzb-box-pupup">
					<h4><em class="quan-no {{if checked }}quan-yes{{/if}}"></em>{{>ftn_name}}</h4>
					{{if subFunctions.length > 0}}
						<div class="wzb-box-pubg">
							{{for subFunctions}}
								<a name="fun" value="{{>ftn_id }}" class="wzb-box-1-4 {{if uff_fun_id > 0 }}current{{/if}}" href="javascript:void(0)" title=""><div class="wzb-box-1-5"><i class="{{>icon}}"></i></div><div class="wzb-box-1-6">{{>ftn_name}}</div></a>
							{{/for}}
						</div>
					{{/if}}
				</div>
		</script>

		<!-- 快速通道功能项显示模板 -->
		<script type="text/x-jquery-tmpl" id="fun_item">
			<a name="fun" class="wzb-box-1-4" href="{{>url}}">
				<div class="wzb-box-1-5"><i class="{{>icon}}"></i></div>
				<div class="wzb-box-1-6">{{>ftn_name}}</div>
			</a>
		</script>

		<!--[if lt IE 9]>
		    <script src="bootstrap/js/html5shiv.min.js"></script>
		    <script src="bootstrap/js/respond.min.js"></script>
		<![endif]-->

	</head>

	<body>
		<div class="wzb-body">
			<jsp:include page="../common/menu.jsp"></jsp:include>

			<div class="wzb-wrapper">
				<div class="wzb-home">
					<!-- section01 start -->
					<div class="section01" style="${(prof.current_role eq 'TADM_1') ? '' : 'min-height:900px;'}">
						<div class="wzb-box-1-sbox">
							<div class="${(prof.current_role eq 'TADM_1') ? 'wzb-box-1-bgg' : (prof.current_role eq 'ADM_1' ? 'wzb-box-1-bgxx' : 'wzb-box-1-bgtt')}"></div>
							<div class="${(prof.current_role eq 'TADM_1') ? 'wzb-box-1-bg' : (prof.current_role eq 'ADM_1' ? 'wzb-box-1-bgx' : 'wzb-box-1-bgt')}"></div>
			                
			                    <div class="wzb-box clearfix" style="padding:32px 0 0 0;">

			                    	<!-- 待办事项 start -->
			                        <div class="col-md-6 padding-l106">
			                          <span class="wzb-box-1-14">
			                          	<c:set var="appCount">
			                                ${assAppCount + testAppCount + itemWaitAppCount + kbWaitAppCount }
			                            </c:set>
			                            <c:if test="${appCount > 0 }">
			                                <em class="wzb-badge-2 wzb-badge-pink"></em>
			                            </c:if>
			                          </span>
			                          <h3 class="wzb-box-1-14-h3"><lb:get key="global.lab_todolist"/></h3>
			                          <div class="wzb-box-1-1">
			                              <div class="wzb-box-1-3" onclick="wb_utils_nav_go('APPR_APP_LIST',${prof.usr_ent_id},'${label_lan}')"><span><!-- 报名审批 --><lb:get key="global.lab_ftn_APPR_APP_LIST"/><em>${itemWaitAppCount}</em></span></div>

		                           <%--        <wb:has-any-permission permission="FTN_AMD_KNOWLEDEG_APP"><!-- 具备知识管理的权限，才显示知识审批任务 -->
		                                  	<div class="wzb-box-1-3" onclick="javascript:wb_utils_nav_go('FTN_AMD_KNOWLEDEG_APP', ${prof.usr_ent_id}, '${label_lan}','PENDING')"><span><!-- 知识审批 --><lb:get key="global.lab_knowledge_approve"/><em>${kbWaitAppCount}</em></span></div>
		                                  </wb:has-any-permission>
 --%>
			                              <!-- 培训管理员和讲师才有作业审批和试卷审批-->
			                              <c:if test="${prof.current_role eq 'TADM_1' or prof.current_role eq 'INSTR_1'}">
				                          	<div class="wzb-box-1-3" onclick="wb_utils_nav_go('HOMEWORK_CORRECTION',${prof.usr_ent_id},'${label_lan}')"><span><!-- 作业批改 --><lb:get key="global.lab_assignment_correction"/><em>${assAppCount}</em></span></div>
				                          	<div class="wzb-box-1-3" onclick="wb_utils_nav_go('EXAMINATION_PAPERS',${prof.usr_ent_id},'${label_lan}')"><span><!-- 试卷批改 --><lb:get key="global.lab_exam_correction"/><em>${testAppCount }</em></span></div>
			                              </c:if>

			                          </div>
			                        </div>
			                        <!-- 待办事项 end -->

			                        <!-- 培训管理员才有的【移动管理】按钮 -->
			                        <%-- <c:choose>
			                        	<c:when test="${prof.current_role eq 'TADM_1'}">
			                        		<div class="col-md-6 wzb-box-1-12 wzb-opacity" onclick="window.location.href='#temp'"><h3 style="color:#fff;padding:20px 0 0 110px;"><!-- 移动管理 --><lb:get key="lab_mobile_management"/><p style="padding:12px 0 0 0;"><lb:get key="global_enter_quickly"/></p></h3></div>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<div class="col-md-6 wzb-box-1-12-t"></div>
			                        	</c:otherwise>
			                        </c:choose> --%>
			                    </div>
			                <!-- </div> -->
			            </div>

			            <!-- 培训管理员【快速通道】常用功能 和数据统计 start -->
			            <c:if test="${prof.current_role eq 'TADM_1'}">
				            <div class="wzb-box clearfix" style="padding:34px 0 30px 0;">
				            	<%-- <div class="col-md-3"><h3 class="wzb-box-1-13"><!-- 快速通道 --><lb:get key="lab_index_fast_track"/></h3></div> --%>

				            	<!-- 添加课程、添加考试、专题是固定的,只需判断是否有权限，有就显示，没有就隐藏 -->
				            	<div class="col-md-9" style = "width: 100%;">
				            		<wb:has-any-permission permission="FTN_AMD_ITM_COS_MAIN_VIEW">
					            		<div class="wzb-box-1-4" href="javascript:void(0)" title="" id="kecheng" style="position:relative;">
					                        <div class="wzb-box-1-5"><i class="Tjkcheng"></i></div>
					                        <div class="wzb-box-1-6"><!-- 新增课程 --><lb:get key="label_core_training_management_33"/></div>
					                          <ul style="list-style:none;display:none;" id="cos_type_choose" class="wzb-index-choose">
					                              <li><a href="javascript:itm_lst.select_add_item_type_prep('COS','SELFSTUDY');"><lb:get key="label_core_training_management_11"/><!-- 网上课程 --></a></li>
					                              <li><a href="javascript:itm_lst.select_add_item_type_prep('COS','CLASSROOM');"><lb:get key="label_core_training_management_13"/><!-- 面授课程 --></a></li>
					                              <%-- <li><a href="javascript:itm_lst.select_add_item_type_prep('INTEGRATED','INTEGRATED');"><lb:get key="label_core_training_management_14"/><!-- 项目式培训 --></a></li> --%>
					                          </ul>
					                    </div>
					                    <script type="text/javascript">
					                          $("#kecheng").hover(function(){
					                              $("#cos_type_choose").stop().slideToggle("fast");
					                          });
					                    </script>
				                    </wb:has-any-permission>

				                   <%--  <wb:has-any-permission permission="FTN_AMD_EXAM_MAIN_VIEW">
					                     <div class="wzb-box-1-4" href="javascript:void(0)" title="" id="kaoshi" style="position:relative;">
					                        <div class="wzb-box-1-5"><i class="Tjkshi"></i></div>
					                        <div class="wzb-box-1-6"><!-- 新增考试 --><lb:get key="label_core_training_management_24"/></div>
				                           <ul style="list-style:none;display:none;" id="exam_type_choose" class="wzb-index-choose">
				                              <li><a href="javascript:itm_lst.select_add_item_type_prep('EXAM','SELFSTUDY');"><!-- 网上考试 --><lb:get key="label_core_training_management_12"/></a></li>
				                              <li><a href="javascript:itm_lst.select_add_item_type_prep('EXAM','CLASSROOM');"><!-- 离线考试 --><lb:get key="label_core_training_management_36"/></a></li>
				                           </ul>
				                         </div>
				                         <script type="text/javascript">
					                          $("#kaoshi").hover(function(){
					                              $("#exam_type_choose").stop().slideToggle("show");
					                          });
					                     </script>
					                 </wb:has-any-permission> --%>
				                     <wb:has-any-permission permission="FTN_AMD_SPECIALTOPIC_MAIN">
					                     <a class="wzb-box-1-4" href="${ctx}/app/admin/specialTopic/create" title="">
					                        <div class="wzb-box-1-5"><i class="Tjzti"></i></div>
					                        <div class="wzb-box-1-6"><!-- 添加专题 --><lb:get key="label_core_learning_map_30"/></div>
					                     </a>
				                     </wb:has-any-permission>
				                     <!-- 快速通道常用功能列表容器 start-->
				                     <span id="favorite_funs">
		                             </span>
		                             <!-- 快速通道常用功能列表容器 end-->

		                             <!-- 常用功能添加按钮 start-->
		                             <a class="wzb-box-1-4" href="javascript:void(0)" title="" id="addFavoriteFunction">
					                       <div class="wzb-box-1-5-1"><i class="fa-tianjia-2"></i></div>
					                       <div class="wzb-box-1-6" style="color:#00aeef;"></div><%-- <lb:get key="button_add"/> --%>
					                 </a>
					                 <!-- 常用功能添加按钮 end-->
				            	</div>
				            </div>

				            <!-- 数据统计格子 start -->
				            <div class="wzb-box clearfix">
				                <div class="col-sm-4 padding-right0 padding-left0">
				                    <div class="wzb-box-1-7 wzb-bg-green">
				                        <a class="wzb-box-1-8 wzb-box-1-8-pic off-all" href="javascript:void(0)">
				                            <img src="../../static/images/wzb-s02.png" width="80px" alt="">
				                        </a>
				                        <a auth="FTN_AMD_ITM_COS_MAIN" class="wzb-box-1-8 wzb-box-1-8_hover " href="${ctx }/app/admin/course?type=course&cos_type=selfstudy" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_web_base_couse_count}</div>
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 网上课程数 --><lb:get key="label_core_training_management_11"/></div>
				                        </a>
				                        <a auth="FTN_AMD_ITM_COS_MAIN" class="wzb-box-1-8  wzb-box-1-8_hover" href="${ctx }/app/admin/course?type=course&cos_type=classroom" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_classroom_course_count}</div> <!-- 最多显示8位数 -->
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 面授课程数 --><lb:get key="label_core_training_management_13"/></div>
				                        </a>
				                        <%-- <a auth="FTN_AMD_ITM_COS_MAIN" class="wzb-box-1-8 wzb-box-1-8_hover" href="${ctx }/app/admin/course?type=course&cos_type=integrated" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_integrated_course_count}</div>
				                            <div class="wzb-box-1-6" style="color:#fff;"><lb:get key="label_core_training_management_14"/><!-- 项目式培训 --></div>
				                        </a> --%>
				                         <%-- <a auth="FTN_AMD_EXAM_MGT" class="wzb-box-1-8 wzb-box-1-8_hover" href="${ctx }/app/admin/course?type=exam&cos_type=selfstudy" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_web_base_exam_count}</div>
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 网上考试数 --><lb:get key="label_core_training_management_12"/></div>
				                        </a> --%>
				                        <%-- <a auth="FTN_AMD_EXAM_MGT" class="wzb-box-1-8 wzb-box-1-8_hover" href="${ctx }/app/admin/course?type=exam&cos_type=classroom" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_classroom_exam_count}</div>
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 离线考试数 --><lb:get key="label_core_training_management_36"/></div>
				                        </a> --%>
				                    </div>
				                </div>

				                <div class="col-sm-4 padding-right0">
				                    <div auth="FTN_AMD_OPEN_COS_MAIN" onclick="wb_utils_nav_go('FTN_AMD_OPEN_COS_MAIN',${prof.usr_ent_id},'${label_lan}')" class="wzb-bg-yellow wzb-opacity wzb-box-1-9-pic" style="margin:0 0 10px 0;height:89px;">
				                        <a class="wzb-box-1-9" href="javascript:void(0)" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_open_course_count}</div>
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 公开课总数 --><lb:get key="label_core_training_management_81"/></div>
				                        </a>
				                    </div>
				                    <div auth="FTN_AMD_SPECIALTOPIC_MAIN" onclick="wb_utils_nav_go('FTN_AMD_SPECIALTOPIC_MAIN', ${prof.usr_ent_id}, '${label_lan}')" class="wzb-bg-blue wzb-opacity wzb-box-1-9-img" style="height:89px;">
				                        <a class="wzb-box-1-9" href="javascript:void(0)" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_special_topic_count}</div>
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 专题总数 --><lb:get key="lab_index_topic_count"/></div>
				                        </a>
				                    </div>
				                </div>

				                <%--<div class="col-sm-4 padding-right0">
				                    <div auth="FTN_AMD_KNOWLEDGE_STOREGE" class="wzb-box-1-10 wzb-opacity wzb-bg-purple wzb-box-1-8_hover " onclick="javascript:wb_utils_nav_go('FTN_AMD_KNOWLEDGE_STOREGE',${prof.usr_ent_id},'${label_lan}')">
				                        <h4><lb:get key="lab_kb_center"/></h4>
				                        <a href="javascript:void(0)" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_admin_know_share_count}</div> <!-- 最多显示11位数 -->
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 管理员分享量 --><lb:get key="lab_index_admin_knowledge_share_count"/></div>
				                        </a>
				                        <a href="javascript:void(0)" title="">
				                            <div class="wzb-box-1-8-num">${statistics.ssc_learner_know_share_count}</div>
				                            <div class="wzb-box-1-6" style="color:#fff;"><!-- 学员分享量 --><lb:get key="lab_index_learner_knowledge_share_count"/></div>
				                        </a>
				                    </div>--%>
				                </div>
				            </div>
				            <!-- 数据统计格子 end -->
			            </c:if>
			            <!-- 培训管理员【快速通道】常用功能 和数据统计 end -->

			           	<!-- 【当前在线人数】 【总用户数start】 【用户组】 start -->
			            <div class="wzb-box clearfix">
			                <div class="col-md-12">
			                    <a class="wzb-box-1-8" href="javascript:void(0)" style="cursor:text;">
			                        <div class="wzb-box-1-11">
			                            <div class="wzb-box-1-8-num" style="color:#99d562;">${statistics.ssc_user_online_count}</div>
			                            <div class="wzb-box-1-6"><!-- 当前在线人数 --><lb:get key="lab_current_online_number"/></div>
			                        </div>
			                        <div class="wzb-box-1-11"><i class="Dqzxrshu"></i>
			                        	<div id="dong">
	                        				<ul class="zhi-dong" style="margin-top: -5.03377px; opacity: 0.8382;">
											<li><img src="../../../static/images/zhi.png"></li><li><img src="../../../static/images/zhi.png"></li></ul>
                        				</div>
			                        </div>
			                    </a>
			                    <a auth="FTN_AMD_USR_INFO" class="wzb-box-1-8" style="cursor:text;" href="javascript:void(0)" title="">
			                        <div class="wzb-box-1-11">
			                            <div class="wzb-box-1-8-num" style="color:#eb098f;">${statistics.ssc_user_count}</div> <!-- 最多显示13位数 -->
			                            <div class="wzb-box-1-6" title="<lb:get key="lab_index_user_total_count_title"/>"><!-- 总用户数 --><lb:get key="lab_index_user_total_count"/></div>
			                        </div>
			                        <div class="wzb-box-1-11"><i class="Zyhshu"></i></div>
			                    </a>
			                    <a auth="FTN_AMD_USR_INFO" class="wzb-box-1-8" style="cursor:text;" href="javascript:void(0)" title="">
			                        <div class="wzb-box-1-11">
			                            <div class="wzb-box-1-8-num" style="color:#58b5e1;">${statistics.ssc_user_group_count}</div>
			                            <div class="wzb-box-1-6"><!-- 用户组 --><lb:get key="lab_index_user_group_total_count"/></div>
			                        </div>
			                        <div class="wzb-box-1-11"><i class="Yhzu"></i></div>
			                    </a>
			                </div>
			            </div>
			            <!-- 【当前在线人数】 【总用户数start】 【用户组】 end -->

			            <!-- 数据更新时间提醒 start 培训管理员才有 -->
			            <c:if test="${prof.current_role eq 'TADM_1'}">
			            <div id="temp" style="padding:20px 0;margin:-40px 0 0 0;">
			            </div><!--  class="wzb-box clearfix" -->
			            </c:if>
			            <!-- 数据更新时间提醒end -->

			            <!-- 功能格子 非培训管理员，其他角色通用的 start-->
			            <c:if test="${!(prof.current_role eq 'TADM_1')}">
			            <div class="wzb-box clearfix" style="background:#f2f2f2;padding:15px 0 0 0;">
			                <div class="row">
			                   <div class="col-md-12 padding-left0">
			                        <div class="row clearfix">
			                          <c:set var="fCount" value="0"></c:set>
			                          <c:set var="cssClass" value="${fn:split('wzb-background-4,wzb-background-3,wzb-background-2,wzb-background-6,wzb-background-8,wzb-background-12,wzb-background-11,wzb-background-10', ',')}" />

			                          <c:forEach items="${prof.roleFunctions }" var="fun">
			                            <c:if test="${fn:length(fun.subFunctions) > 0 }">
			                                <c:forEach items="${fun.subFunctions }" var="sub">
			                                	<c:if test="${sub.ftn_assign eq '0' }">
				                                	<c:set var="sub_class">
				                                        <%@ include file="functionIcon.jsp"%>
				                                     </c:set>

				                                     <div class="col-sm-3 col-xs-4 margin-bottom15 padding-right0">
						                                <div class="wzb-box-1-background ${cssClass[fCount % 8] }">
						                                    <a class="wzb-box-inner" href="javascript:wb_utils_nav_go('${sub.ftn_ext_id }',${prof.usr_ent_id},'${label_lan}')">
						                                       <div class="wzb-box-2"><i class="${sub_class}"></i></div>
						                                       <div class="wzb-box-2-title"><lb:get key="${sub.ftn_ext_id }" /></div>
						                                    </a>
						                                </div>
						                             </div>

						                             <c:set var="fCount" value="${fCount + 1 }"></c:set>

			                                	</c:if>
			                                </c:forEach>
			                            </c:if>
			                          </c:forEach>

			                          <c:forEach items="${prof.roleFunctions }" var="fun">
			                            <c:if test="${fn:length(fun.subFunctions) > 0 }">
			                                <c:forEach items="${fun.subFunctions }" var="sub">
			                                	<c:if test="${sub.ftn_assign eq '1' }">
				                                	<c:set var="sub_class">
				                                        <%@ include file="functionIcon.jsp"%>
				                                     </c:set>

				                                     <div class="col-sm-3 col-xs-4 margin-bottom15 padding-right0">
						                                <div class="wzb-box-1-background ${cssClass[fCount % 8] }">
						                                    <a class="wzb-box-inner" href="javascript:wb_utils_nav_go('${sub.ftn_ext_id }',${prof.usr_ent_id},'${label_lan}')">
						                                       <div class="wzb-box-2"><i class="fa ${sub_class}"></i></div>
						                                       <div class="wzb-box-2-title"><lb:get key="${sub.ftn_ext_id }" /></div>
						                                    </a>
						                                </div>
						                             </div>

						                             <c:set var="fCount" value="${fCount + 1 }"></c:set>

			                                	</c:if>
			                                </c:forEach>
			                            </c:if>
			                          </c:forEach>

			                        </div>
			                   </div>
			                   <div class="clearfix visible-sm-block"></div>
			               </div>
			            </div>
			            </c:if>
			            <!-- 功能格子 非培训管理员，其他角色通用的 end-->

					</div>
					<!-- section01 end -->

					<c:if test="${prof.current_role eq 'TADM_1'}">
					<!-- section02 start -->
					<%-- <div class="section02">
			            <div class="wzb-box clearfix" style="position:relative;">
			                <div class="col-sm-12" style="position:absolute;bottom:0;"><img src="../../static/images/wzb-s07.png" width="984px" alt=""></div>
			                <div class="col-sm-4">
			                    <h3 class="wzb-box-2-7"><!-- 移动管理 --><lb:get key="lab_index_mobile_management"/></h3>
			                    <div style="margin-top: 60px;"><img src="../../static/images/wzb-s05.png" width="328px" alt=""></div>
			                    <div style="margin-top: 60px;" class="wzb-box-2-1"><!-- 已有 --><lb:get key="lab_index_have"/><em style="color:#F1FF48;">${statistics.ssc_mobile_app_user_count}<!-- 人 --></em><lb:get key="lab_index_account_unit"/><!-- 使用移动APP登录 --></div>
			                    <div class="wzb-box-2-1"><lb:get key="lab_index_use_app"/></div>
			                    <div style="margin-top: 60px;"><img src="../../static/images/wzb-s06.png" width="328px" alt=""></div>
			                </div>
			                <div class="col-sm-4 wzb-box-2-2">
			                    <div class="wrap" style="background:rgba(255, 255, 255, 0.6);">
			                        <header class="header mm-background" style="z-index:99;">
			                            <a class="header-sosuo-er off-all" href="javascript:void(0)"></a>
			                            <span class="header-title" style="line-height:47px;"><!-- 首页 --><lb:get key="lab_menu_started"/></span>
			                        </header> <!-- header End -->

			                        <!-- gps start -->
			                        <div class="cont-box gps">
			                            <a href="javascript:void(0)" style="width:100%;">
			                                <span class="gps-index" style="background:#ddd;"></span>
			                            </a>
			                        </div><!-- gps end -->

			                        <!-- content-2 start -->
			                        <section class="content-2">
			                            <!-- touchslider开始 -->
			                            <div class="touchslider">
			                                <div style="cursor:pointer;">
			                                	<a auth="FTN_AMD_MOBILE_POSTER_MAIN" href="javascript:url = wb_utils_invoke_servlet('cmd', 'get_poster', 'stylesheet', 'poster_details.xsl', 'rpt_type', 'FTN_AMD_MOBILE_POSTER_MAIN','tabId','poster');window.location.href=url">
			                                    	<div class="touchslider-item"><img src="../../mobile/images/banner01.jpg" alt="" width="320px"></div>
			                                	</a>
			                                </div>
			                            </div> <!-- touchslider End -->

			                            <!-- memu start -->
			                            <div class="memu clearfix">
			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_ITM_COS_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_ITM_COS_MAIN">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-danlv">
			                                        <i class="icon-memu-lesson"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 选课中心 --><lb:get key="label_core_training_management_297"/></div>
			                                </a>

			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_ITM_COS_MAIN', ${prof.usr_ent_id}, '${label_lan}')"  auth="FTN_AMD_ITM_COS_MAIN">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-orange">
			                                        <i class="icon-memu-new-course"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 最新课程 --><lb:get key="label_core_training_management_302"/></div>
			                                </a>

			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_EXAM_MGT', ${prof.usr_ent_id}, '${label_lan}');" auth="FTN_AMD_EXAM_MGT">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-blue">
			                                        <i class="icon-memu-new-course"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 考试 --><lb:get key="lab_menu_test"/></div>
			                                </a>

			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_KNOWLEDGE_STOREGE', ${prof.usr_ent_id}, '${label_lan}')"  auth="FTN_AMD_KNOWLEDGE_MGT">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-pink">
			                                        <i class="icon-memu-Knowledge"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 知识中心 --><lb:get key="lab_kb_center"/></div>
			                                </a>

			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_MSG_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_MSG_MAIN">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-purple">
			                                        <i class="icon-memu-post"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 公告 --><lb:get key="global.FTN_AMD_SYS_MSG_LIST"/></div>
			                                </a>

			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_ARTICLE_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_ARTICLE_MAIN">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-yellow">
			                                        <i class="icon-memu-article"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 资讯 --><lb:get key="lab_menu_article"/></div>
			                                </a>

			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_EVN_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_EVN_MAIN">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-green">
			                                        <i class="icon-memu-check"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 调查问卷 --><lb:get key="personal_evaluation"/></div>
			                                </a>

			                                <a class="memu-box-4 wzb-opacity" href="javascript:wb_utils_nav_go('FTN_AMD_VOTING_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_VOTING_MAIN">
			                                    <span class="icon-memu-model icon-memu-model-two icon-memu-salmon">
			                                        <i class="icon-memu-toupiao"></i>
			                                    </span>
			                                    <div class="memu-box-title"><!-- 投票 --><lb:get key="label_core_requirements_management_2"/></div>
			                                </a>
			                            </div> <!-- memu End -->

			                            <!-- panel start -->
			                            <div class="list-pic-12 wzb-opacity wzb-box-2-3">
			                                <a href="javascript:wb_utils_nav_go('FTN_AMD_SPECIALTOPIC_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_SPECIALTOPIC_MAIN">
			                                    <img src="../../mobile/images/M_c01.jpg" width="100%" alt="">
			                                    <div><i><!-- 精彩专题 --><lb:get key="label_core_learning_map_82"/></i></div>
			                                </a>
			                            </div><!-- panel end -->

			                            <!-- panel start -->
			                            <div class="list-pic-12 wzb-opacity wzb-box-2-3">
			                                <a href="javascript:wb_utils_nav_go('FTN_AMD_PROFESSION_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_PROFESSION_MAIN">
			                                    <img src="../../mobile/images/M_a01.jpg" width="100%" alt="">
			                                    <div><!-- <i>职级</i>发展学习地图 --><lb:get key="lab_index_grade_develop_map"/></div>
			                                </a>
			                            </div><!-- panel end -->

			                            <!-- panel start -->
			                            <div class="list-pic-12 wzb-opacity wzb-box-2-3">
			                                <a href="javascript:wb_utils_nav_go('FTN_AMD_POSITION_MAP_MAIN', ${prof.usr_ent_id}, '${label_lan}')" auth="FTN_AMD_POSITION_MAP_MAIN">
			                                    <img src="../../mobile/images/M_b01.jpg" width="100%" alt="">
			                                    <div><!-- 关键<i>岗位</i>学习地图 --><lb:get key="lab_index_key_position_map"/></div>
			                                </a>
			                            </div><!-- panel end -->

			                            <!-- 公开课开始 -->
			                            <div class="panel wzb-box-2-4">
			                                <h3 class="panel-title-2">
			                                    <a auth="FTN_AMD_OPEN_COS_MAIN" style="width: 100%;height: 44px;" class="panel-title-subject panel-jingpin" href="javascript:wb_utils_nav_go('FTN_AMD_OPEN_COS_MAIN', ${prof.usr_ent_id}, '${label_lan}')" title=""><!-- 公开课 --><lb:get key="label_core_training_management_81"/></a>
			                                </h3>
			                            </div>
			                            <!-- 公开课结束 -->

			                            <!-- 问答开始 -->
			                            <div class="panel wzb-box-2-4">
			                                <h3 class="panel-title-2">
			                                    <a auth="FTN_AMD_Q_AND_A_MAIN" style="width: 100%;height: 44px;" class="panel-title-subject panel-jingpin" href="javascript:wb_utils_nav_go('FTN_AMD_Q_AND_A_MAIN', ${prof.usr_ent_id}, '${label_lan}')" title=""><!-- 问答 --><lb:get key="FTN_AMD_Q_AND_A_VIEW"/></a>
			                                </h3>
			                            </div>
			                            <!-- 问答结束 -->

			                            <!-- 群组开始 -->
			                            <div class="panel wzb-box-2-4">
			                                <h3 class="panel-title-2">
			                                    <a auth="FTN_AMD_SNS_GROUP_MAIN" style="width: 100%;height: 44px;" class="panel-title-subject panel-jingpin" href="javascript:wb_utils_nav_go('FTN_AMD_SNS_GROUP_MAIN', ${prof.usr_ent_id}, '${label_lan}')" title=""><!-- 群组 --><lb:get key="lab_menu_group"/></a>
			                                </h3>
			                            </div>
			                            <!-- 群组结束 -->

			                            <!-- 内部讲师开始 -->
			                            <div class="panel wzb-box-2-4">
			                                <h3 class="panel-title-2">
			                                    <a auth="FTN_AMD_INT_INSTRUCTOR_MAIN" style="width: 100%;height: 44px;" class="panel-title-subject panel-jingpin" href="javascript:wb_utils_nav_go('FTN_AMD_INT_INSTRUCTOR_MAIN', ${prof.usr_ent_id}, '${label_lan}')" title=""><!-- 内部讲师 --><lb:get key="label_core_basic_data_management_13"/></a>
			                                </h3>
			                            </div>
			                            <!-- 内部讲师结束 -->

			                            <!-- 外部讲师开始 -->
			                            <div class="panel wzb-box-2-4">
			                                <h3 class="panel-title-2">
			                                    <a auth="FTN_AMD_EXT_INSTRUCTOR_MAIN" style="width: 100%;height: 44px;" class="panel-title-subject panel-jingpin" href="javascript:wb_utils_nav_go('FTN_AMD_EXT_INSTRUCTOR_MAIN', ${prof.usr_ent_id}, '${label_lan}')" title=""><!-- 外部讲师 --><lb:get key="label_core_basic_data_management_14"/></a>
			                                </h3>
			                            </div>
			                            <!-- 外部讲师结束 -->
			                        </section><!-- content-2 end -->
			                    </div>
			                </div>
			                <div class="col-sm-4" style="padding:272px 0 0 10px;">
			                    <div class="wzb-box-2-6"><img src="../../static/images/wzb-s09.png" width="100px" alt=""></div>
			                    <div class="wzb-box-2-5">
			                        <!-- 点击进入各功能管理维护 --><lb:get key="lab_index_click_to_access_function_management"/>
			                    </div>
			                    <!-- <div class="wzb-box-2-8"><img src="../../static/images/wzb-s08.png" width="166px" alt=""></div> -->
			                    <div><img src="../../static/images/wzb-s11.png" width="400px" alt=""></div>
			                </div>
			            </div>
			        </div> --%>
					<!-- section02 end -->

					<!-- section3 start ISO10015 培训质量管理框架 -->
					<%-- <div class="section03">
						<div class="wzb-box">
						     <div class="wzb-box-3-title">ISO10015&nbsp;<lb:get key="global.iso"/></div>
						     <div class="wzb-box-3 clearfix">
						            <c:forEach items="${prof.roleFunctions }" var="fun">
						               <c:if test="${fun.ftn_ext_id eq 'FTN_AMD_DEMAND_MGT' or fun.ftn_ext_id eq 'FTN_AMD_PLAN_MGT' or fun.ftn_ext_id eq 'FTN_AMD_TRAINING_MGT' }">
						               <c:set var="parent_class">
						                    <c:choose>
						                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_DEMAND_MGT'}">
						                        wzb-box-3-red fa-cubes
						                        </c:when>
						                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_PLAN_MGT'}">
						                        wzb-box-3-yellow fa-list-alt
						                        </c:when>
						                        <c:when test="${fun.ftn_ext_id eq 'FTN_AMD_TRAINING_MGT'}">
						                        wzb-box-3-purple fa-calendar
						                        </c:when>
						                    </c:choose>
						              </c:set>

						              <div class="wzb-box-3-0 wzb-box-3-leftarrow">
						                   <a class="wzb-box-3-outer" href="javascript:;" title="">
						                        <div class="wzb-box-3-1"><i class="fa ${parent_class }"></i></div>
						                        <div class="wzb-box-3-2"><lb:get key="global.${fun.ftn_ext_id }" />
						                        </div>
						                   </a>
						                   <div class="wzb-box-3-3">
						                        <c:if test="${fn:length(fun.subFunctions) > 0 }">
						                            <c:forEach items="${fun.subFunctions }" var="sub">
						                                <p><a href="javascript:wb_utils_nav_go('${sub.ftn_ext_id }',${prof.usr_ent_id},'${label_lan}')"  title=""><lb:get key="global.${sub.ftn_ext_id }" /></a></p>
						                            </c:forEach>
						                        </c:if>
						                   </div>
						              </div>
						              </c:if>
						            </c:forEach>

						          <div class="wzb-box-3-0">
						               <a class="wzb-box-3-outer" href="javascript:;" title="">
						                    <div class="wzb-box-3-1"><i class="fa wzb-box-3-blue fa-database"></i></div>
						                    <div class="wzb-box-3-2"> <lb:get key="global.lab_evaluation_training_result"/></div>
						               </a>
						               <div class="wzb-box-3-3">
						                    <p>
							                    <a href="javascript:wb_utils_nav_go('FTN_AMD_EXAM_MGT',${prof.usr_ent_id},'${label_lan}')"  title="">
													<!--考试管理 --><lb:get key="global.FTN_AMD_EXAM_MGT"/>
							                    </a>
						                    </p>
						               </div>
						          </div>
						     </div>
						</div>
					</div> --%>
					<!-- section3 end -->
					</c:if>
					<%@ include file="../../common/footer.jsp"%>
				</div>
			</div>
		</div>
	</body>
	<script>
	$(function(){               
			function top(){
					var inerheight = $(".zhi-dong li:first-child").height();
					var i = 0;
					$('.zhi-dong').animate({
						"marginTop":- inerheight  + "px",
						"opacity":i+=0.1
					},800,function(){
						$(".zhi-dong").css({
							"marginTop":0,
							"opacity":1
						}),
						$(".zhi-dong li:first-child").appendTo(".zhi-dong")
					})
			}
			
   		var timer = setInterval(function(){
   			top()
   		},2000)
		})
	</script>
</html>