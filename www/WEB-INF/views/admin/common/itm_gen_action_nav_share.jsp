<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<% 
	String cur_node_id=request.getParameter("cur_node_id");
%>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_itm_req.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/gen_utils.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_utils.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_item.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_course.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_announcement.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_report.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/js/wb_criteria.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/js/wb_mote.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_application.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_attendance.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_usergroup.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_module.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_cos_wizard.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_fm.js"></script>
<script language="JavaScript" type="text/javascript" src="${ctx}/js/wb_scorescheme.js"></script>
<script type="text/javascript">
	fm = new wbFm(true)
	crit = new wbCriteria
	itm_lst = new wbItem
	ann = new wbAnnouncement
	course_lst = new wbCourse
	var module_lst = new wbModule;	
	rpt = new wbReport
	mote = new wbMote
	app = new wbApplication
	attn = new wbAttendance
	itmReq = new wbItemReq
	usr = new wbUserGroup
	wiz = new wbCosWizard
	cmt = new wbScoreScheme;
	
	var rsv_itm_title = "${item.parent.itm_title}" + " - " + "${item.itm_title}";
	
</script>

<c:choose>
<c:when test="${item.itm_status_off}">
<div id="navNode" class="panel-content">
	<c:choose>
		<c:when test="${item.itm_type  eq 'SELFSTUDY'}">
			<!--课程信息 -->
			<a id="100" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-dcf4fd active">
					<i class="kcxinxi"></i>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_231"/>
					</span>
				</div>
			</a>
			
			<!--网上内容 -->
			<a id="106" href="javascript:course_lst.edit_cos('${item.course.cos_res_id}','${item.itm_type}','${item.itm_create_run_ind eq 0 ? false : true}','${item.itm_content_def}','${item.course.cos_structure_xml eq null ? false : true}')">
				<div class="wzb-tab-6 margin-right20 bg-f8f5b5 margin-bottom10" data="OnlineContent">
					<i class="wsneirong"></i>
					<c:if test="${courseTabsRemind.rmdOnlineContent}">
						<i class="kchint"></i>
					</c:if>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_237"/>
					</span>
				</div>
			</a>
			
			<!--课程分数设置 -->
			<a id="108" href="javascript:cmt.get_score_scheme_list(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-e3fbca margin-bottom10" data="CourseScoreSettings">
					<i class="jfguize"></i>
					<c:if test="${courseTabsRemind.rmdCourseScoreSettings}">
						<i class="kchint"></i>
					</c:if>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_239"/>
					</span>
				</div>
			</a>
			
			<!--结训条件设置 -->
			<a id="109" href="javascript:cmt.get_criteria(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-ddeaff margin-bottom10" data="CompletionCriteriaSettings">
					<i class="jxtiaojian"></i>
					<c:if test="${courseTabsRemind.rmdCompletionCriteriaSettings}">
						<i class="kchint"></i>
					</c:if>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_240"/>
					</span>
				</div>
			</a>
			
			<!--目标学员 -->
			<a id="102" href="javascript:itm_lst.get_target_rule(${item.itm_id},'TARGET_LEARNER')">
				<div class="wzb-tab-6 margin-right20 bg-fee0e2 margin-bottom10">
					<i class="mbxueyuan"></i>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_233"/>
					</span>
				</div>
			</a>
			
			<!--发布 -->
			<a id="115" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-fce3be margin-bottom10">
					<i class="fabu"></i>
					<span class="tab-6-tit">
						<c:choose>
							<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
								<lb:get key="label_tm.label_core_training_management_256"/>
							</c:when>
							<c:otherwise>
								<lb:get key="label_tm.label_core_training_management_255"/>
							</c:otherwise>
						</c:choose>
					</span>
				</div>
			</a>
			
			<div class="wzb-tab-6 wzb-tab-6-more bg-eeeeee margin-bottom10">
				<i class="gengduo"></i>
				<span class="tab-6-tit-2"><lb:get key="global.global_more"/></span>
				<div class="wzb-tab-7 more-daohang" style="display:none;">
					<i class="more-san"></i>
					
					<!--结训记录 -->
					<a id="111" href="javascript:attn.get_grad_record(${item.itm_id})">
						<span class="jxjilu">
							<em>
								<lb:get key="label_tm.label_core_training_management_242"/>
							</em>
						</span>
					</a>
					
					<!--处理报名 -->
					<a id="103" href="javascript:app.get_application_list('',${item.itm_id},'','','','')">
						<span class="clbaoming">
							<em>
								<lb:get key="label_tm.label_core_training_management_234"/>
							</em>
						</span>
					</a>
					
					<!--计分记录 -->
					<a id="110" href="javascript:cmt.get_scoring_itm_lst(${item.itm_id})">
						<span class="jfjilu">
							<em>
								<lb:get key="label_tm.label_core_training_management_241"/>
							</em>
						</span>
					</a>
					
					<!--跟踪报告 -->
					<a id="112" href="javascript:rpt.open_cos_lrn_lst(${item.itm_id})">
						<span class="gzbaogao">
							<em>
								<lb:get key="label_tm.label_core_training_management_243"/>
							</em>
						</span>
					</a>
					
					<!--评论管理 -->
					<a id="113" href="javascript:itm_lst.get_itm_comment_lst(${item.itm_id})">
						<span class="plguanli">
							<em>
								<lb:get key="label_tm.label_core_training_management_244"/>
							</em>
						</span>
					</a>
					
					<!--先修课程 -->
					<a id="101" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
						<span class="xxkecheng">
							<em>
								<lb:get key="label_tm.label_core_training_management_232"/>
							</em>
						</span>
					</a>
					
					<!--先修模块设置 -->
					<a id="107" href="javascript:itm_lst.get_mod_pre(${item.itm_id})">
						<span class="xxmkshezhi">
							<em>
								<lb:get key="label_tm.label_core_training_management_238"/>
							</em>
						</span>
					</a>
					
					<!--课程费用 -->
					<a id="104" href="javascript:itm_lst.get_item_cost(${item.itm_id})">
						<span class="kcfeiyong">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}">
										<lb:get key="label_tm.label_core_training_management_286"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_235"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<!--课程公告 -->
					<a id="105" href="javascript:ann.sys_lst('all','RES','${item.course.cos_res_id}','','','','','',true)">
						<span class="kcgonggao">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}">
										<lb:get key="label_tm.label_core_training_management_287"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_236"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<!--报名工作流 -->
					<a id="114" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
						<span class="bmgzuoliu">
							<em>
								<lb:get key="label_tm.label_core_training_management_245"/>
							</em>
						</span>
					</a>
				</div>
			</div>
		</c:when>
		<c:when test="${item.itm_type eq 'CLASSROOM' and item.itm_run_ind ne 1}">
			<c:if test="${item.itm_content_def eq null or item.itm_content_def eq '' or item.itm_content_def eq 'CHILD'}">
				<!--课程信息 -->
				<a id="200" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-dcf4fd active">
						<i class="kcxinxi"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_231"/>
						</span>
					</div>
				</a>
				
				<!--班级管理/考试场次 -->
				<a id="201" href="javascript:itm_lst.get_item_run_list(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-f8f5b5 margin-bottom10">
						<i class="bjguanli"></i>
						<span class="tab-6-tit">
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}"><lb:get key="label_tm.label_core_training_management_249"/></c:when>
								<c:otherwise><lb:get key="label_tm.label_core_training_management_246"/></c:otherwise>
							</c:choose>
						</span>
					</div>
				</a>
				
				<!--目标学员 -->
				<a id="203" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_LEARNER')">
					<div class="wzb-tab-6 margin-right20 bg-e3fbca margin-bottom10">
						<i class="mbxueyuan"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_233"/>
						</span>
					</div>
				</a>
				
				<!--发布 -->
				<a id="212" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-ddeaff margin-bottom10">
						<i class="fabu"></i>
						<span class="tab-6-tit">
							<c:choose>
								<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
									<lb:get key="label_tm.label_core_training_management_256"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_255"/>
								</c:otherwise>
							</c:choose>
						</span>
					</div>
				</a>
				
				<div class="wzb-tab-6 wzb-tab-6-more bg-eeeeee margin-bottom10">
					<i class="gengduo"></i>
					<span class="tab-6-tit-2"><lb:get key="global.global_more"/></span>
					<div class="wzb-tab-7 more-daohang" style="display:none;">
						<i class="more-san"></i>
	
						<!--日程表 -->
						<a id="204" href="javascript:itm_lst.ae_get_course_lesson(${item.itm_id})">
							<span class="rcbiaos">
								<em>
									<lb:get key="label_tm.label_core_training_management_247"/>
								</em>
							</span>
						</a>
						
						<!--先修课程 -->
						<a id="202" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
							<span class="xxkecheng">
								<em>
									<lb:get key="label_tm.label_core_training_management_232"/>
								</em>
							</span>
						</a>
						
						<!-- 设置内容模式 -->
						<a id="206" href="javascript:itm_lst.ae_get_online_content_info(${item.itm_id})">
							<span class="clbaoming">
								<em>
									<lb:get key="label_tm.label_core_training_management_237"/>
								</em>
							</span>
						</a>
						
						<!--报名工作流 -->
						<a id="211" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
							<span class="bmgzuoliu">
								<em>
									<lb:get key="label_tm.label_core_training_management_245"/>
								</em>
							</span>
						</a>
	
						<!--评论管理 -->
						<a id="210" href="javascript: itm_lst.get_itm_comment_lst(${item.itm_id})">
							<span class="plguanli">
								<em>
									<lb:get key="label_tm.label_core_training_management_244"/>
								</em>
							</span>
						</a>
					</div>
				</div>
			</c:if>
			<c:if test="${item.itm_content_def eq 'PARENT'}">
				<!--课程信息 -->
				<a id="200" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-dcf4fd active">
						<i class="kcxinxi"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_231"/>
						</span>
					</div>
				</a>
				
				<!--课程分数设置 -->
				<a id="208" href="javascript:cmt.get_score_scheme_list(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-f8f5b5 margin-bottom10" data="CourseScoreSettings">
						<c:if test="${courseTabsRemind.rmdCourseScoreSettings}">
							<i class="kchint"></i>
						</c:if>
						<i class="jfguize"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_239"/>
						</span>
					</div>
				</a>
				
				<!--结训条件设置 -->
				<a id="209" href="javascript:cmt.get_criteria(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-e3fbca margin-bottom10" data="CompletionCriteriaSettings">
						<c:if test="${courseTabsRemind.rmdCompletionCriteriaSettings}">
							<i class="kchint"></i>
						</c:if>
						<i class="jxtiaojian"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_240"/>
						</span>
					</div>
				</a>
				
				<!--目标学员 -->
				<a id="203" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_LEARNER')">
					<div class="wzb-tab-6 margin-right20 bg-ddeaff margin-bottom10">
						<i class="mbxueyuan"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_233"/>
						</span>
					</div>
				</a>
				
				<!--班级管理/考试场次 -->
				<a id="201" href="javascript:itm_lst.get_item_run_list(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-fee0e2 margin-bottom10">
						<i class="bjguanli"></i>
						<span class="tab-6-tit">
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}"><lb:get key="label_tm.label_core_training_management_249"/></c:when>
								<c:otherwise><lb:get key="label_tm.label_core_training_management_246"/></c:otherwise>
							</c:choose>
						</span>
					</div>
				</a>
				
				<!--发布 -->
				<a id="212" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-fce3be margin-bottom10">
						<i class="fabu"></i>
						<span class="tab-6-tit">
							<c:choose>
								<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
									<lb:get key="label_tm.label_core_training_management_256"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_255"/>
								</c:otherwise>
							</c:choose>
						</span>
					</div>
				</a>
				
				<div class="wzb-tab-6 wzb-tab-6-more bg-eeeeee margin-bottom10">
					<i class="gengduo"></i>
					<span class="tab-6-tit-2"><lb:get key="global.global_more"/></span>
					<div class="wzb-tab-7 more-daohang" style="display:none;">
						<i class="more-san"></i>
	
						<!--网上内容 -->
						<a id="206" href="javascript:course_lst.edit_cos('${item.course.cos_res_id}','${item.itm_type}','${item.itm_create_run_ind eq 0 ? false : true}','${item.itm_content_def}','${item.course.cos_structure_xml eq null ? false : true}')">
							<span class="wsneirongs">
								<em>
									<lb:get key="label_tm.label_core_training_management_237"/>
								</em>
							</span>
						</a>
						
						<!--先修课程 -->
						<a id="202" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
							<span class="xxkecheng">
								<em>
									<lb:get key="label_tm.label_core_training_management_232"/>
								</em>
							</span>
						</a>
						
						<!--先修模块设置 -->
						<a id="207" href="javascript:itm_lst.get_mod_pre(${item.itm_id})">
							<span class="xxmkshezhi">
								<em>
									<lb:get key="label_tm.label_core_training_management_238"/>
								</em>
							</span>
						</a>
					
						<!--日程表 -->
						<a id="204" href="javascript:itm_lst.ae_get_course_lesson(${item.itm_id})">
							<span class="rcbiaos">
								<em>
									<lb:get key="label_tm.label_core_training_management_247"/>
								</em>
							</span>
						</a>
						
						<!--课程公告 -->
						<a id="205" href="javascript:ann.sys_lst('all','RES','${cos_res_id}','','','','','',true">
							<span class="kcgonggao">
								<em>
									<c:choose>
										<c:when test="${item.itm_exam_ind eq 1}">
											<lb:get key="label_tm.label_core_training_management_287"/>
										</c:when>
										<c:otherwise>
											<lb:get key="label_tm.label_core_training_management_236"/>
										</c:otherwise>
									</c:choose>
								</em>
							</span>
						</a>
						
						<!--报名工作流 -->
						<a id="211" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
							<span class="bmgzuoliu">
								<em>
									<lb:get key="label_tm.label_core_training_management_245"/>
								</em>
							</span>
						</a>
	
						<!--评论管理 -->
						<a id="210" href="javascript: itm_lst.get_itm_comment_lst(${item.itm_id})">
							<span class="plguanli">
								<em>
									<lb:get key="label_tm.label_core_training_management_244"/>
								</em>
							</span>
						</a>
					</div>
				</div>
			</c:if>
		</c:when>
		<c:when test="${item.itm_type eq 'CLASSROOM' and item.itm_run_ind eq 1}">
			<!--课程信息 -->
			<a id="300" href="javascript:itm_lst.get_item_run_detail(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-dcf4fd active">
					<i class="bjxinxi"></i>
					<span class="tab-6-tit">
						<c:choose>
							<c:when test="${item.itm_exam_ind eq 1}">
								<lb:get key="label_tm.label_core_training_management_251"/>
							</c:when>
							<c:otherwise>
								<lb:get key="label_tm.label_core_training_management_250"/>
							</c:otherwise>
						</c:choose>
					</span>
				</div>
			</a>
			
			<!--设置内容模式 -->
			<c:if test="${content_def eq '' or content_def eq 'CHILD'}">
				<!--课程分数设置 -->
				<a id="311" href="javascript:cmt.get_score_scheme_list(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-f8f5b5 margin-bottom10" data="CourseScoreSettings">
						<c:if test="${courseTabsRemind.rmdCourseScoreSettings}">
							<i class="kchint"></i>
						</c:if>
						<i class="jfguize"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_239"/>
						</span>
					</div>
				</a>
				
				<!--结训条件设置 -->
				<a id="312" href="javascript:cmt.get_criteria(${item.itm_id})">
					<div class="wzb-tab-6 margin-right20 bg-e3fbca margin-bottom10" data="CompletionCriteriaSettings">
						<c:if test="${courseTabsRemind.rmdCompletionCriteriaSettings}">
							<i class="kchint"></i>
						</c:if>
						<i class="jxtiaojian"></i>
						<span class="tab-6-tit">
							<lb:get key="label_tm.label_core_training_management_240"/>
						</span>
					</div>
				</a>
			</c:if>
			
			<!--日程表 -->
			<a id="304" href="javascript:itm_lst.ae_get_run_lesson(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-ddeaff margin-bottom10">
					<i class="rcbiao"></i>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_247"/>
					</span>
				</div>
			</a>
			
			<!--可报名学员 -->
			<%-- <a id="301" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_ENROLLMENT')">
				<div class="wzb-tab-6 margin-right20">
					<i class="kbmxueyuan"></i>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_269"/>
					</span>
				</div>
			</a> --%>
			
			<!--发布 -->
			<a id="318" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-fee0e2 margin-bottom10">
					<i class="fabu"></i>
					<span class="tab-6-tit">
						<c:choose>
							<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
								<lb:get key="label_tm.label_core_training_management_256"/>
							</c:when>
							<c:otherwise>
								<lb:get key="label_tm.label_core_training_management_255"/>
							</c:otherwise>
						</c:choose>
					</span>
				</div>
			</a>
			
			<div class="wzb-tab-6 wzb-tab-6-more bg-eeeeee margin-bottom10">
				<i class="gengduo"></i>
				<span class="tab-6-tit-2"><lb:get key="global.global_more"/></span>
				<div class="wzb-tab-7 more-daohang" style="display:none;">
					<i class="more-san"></i>
					
					<!--处理报名 -->
					<a id="303" href="javascript:app.get_application_list('',${item.itm_id},'','','',''">
						<span class="clbaoming">
							<em>
								<lb:get key="label_tm.label_core_training_management_234"/>
							</em>
						</span>
					</a>
					
					<!--结训记录 -->
					<a id="315" href="javascript:attn.get_grad_record(${item.itm_id})">
						<span class="jxjilu">
							<em>
								<lb:get key="label_tm.label_core_training_management_242"/>
							</em>
						</span>
					</a>
					
					<!--网上内容 -->
					<a id="309" href="javascript:course_lst.edit_cos('${item.course.cos_res_id}','${item.itm_type}','${item.itm_create_run_ind eq 0 ? false : true}','${item.itm_content_def}','${item.course.cos_structure_xml eq null ? false : true}')">
						<span class="wsneirongs">
							<em>
								<lb:get key="label_tm.label_core_training_management_237"/>
							</em>
						</span>
					</a>
					
					<!--计分记录 -->
					<a id="313" href="javascript:cmt.get_scoring_itm_lst(${item.itm_id})">
						<span class="jfjilu">
							<em>
								<lb:get key="label_tm.label_core_training_management_241"/>
							</em>
						</span>
					</a>
					
					<!--出席率 -->
					<a id="314" href="javascript:attn.get_attd_rate_lst(${item.itm_id})">
						<span class="cxlv">
							<em>
								<lb:get key="label_tm.label_core_training_management_252"/>
							</em>
						</span>
					</a>
					
					<!--签到记录 -->
					<a id="319" href="javascript:attn.get_qiandao_Lst(${item.itm_id},0)">
						<span class="rcbiaos">
							<em>
								<lb:get key="label_tm.label_core_training_management_270"/>
							</em>
						</span>
					</a>
					
					<!--跟踪报告 -->
					<a id="316" href="javascript:rpt.open_cos_lrn_lst(${item.itm_id})">
						<span class="gzbaogao">
							<em>
								<lb:get key="label_tm.label_core_training_management_243"/>
							</em>
						</span>
					</a>
					
					<!--先修课程 -->
					<%-- <a id="302" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
						<span class="xxkecheng">
							<em>
								<lb:get key="label_tm.label_core_training_management_232"/>
							</em>
						</span>
					</a> --%>
					
					<!--先修模块设置 -->
					<a id="310" href="javascript:itm_lst.get_mod_pre(${item.itm_id})">
						<span class="xxmkshezhi">
							<em>
								<lb:get key="label_tm.label_core_training_management_238"/>
							</em>
						</span>
					</a>
					
					<!--开课通知 -->
					<a id="305" href="javascript:itm_lst.itm_ji_msg('${item.itm_id}')">
						<span class="kktongzhi">
							<em>
								<lb:get key="label_tm.label_core_training_management_254"/>
							</em>
						</span>
					</a>
					
					<!--预订设施 -->
					<a id="306" href="javascript:fm.get_itm_fm(${item.itm_id},'${item.itm_rsv_id}',rsv_itm_title)">
						<span class="ssyuding">
							<em>
								<lb:get key="label_tm.label_core_training_management_253"/>
							</em>
						</span>
					</a>
					
					<!--课程费用 -->
					<a id="307" href="itm_lst.get_item_cost(${item.itm_id})">
						<span class="kcfeiyong">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}">
										<lb:get key="label_tm.label_core_training_management_286"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_235"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<!--课程公告 -->
					<a id="308" href="javascript:ann.sys_lst('all','RES','${item.course.cos_res_id}','','','','','',true">
						<span class="kcgonggao">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}">
										<lb:get key="label_tm.label_core_training_management_287"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_236"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<c:if test="${content_def eq 'PARENT'}">
						<!--课程分数设置 -->
						<a id="311" href="javascript:cmt.get_score_scheme_list(${item.itm_id})">
							<span class="jfguizes">
								<em>
									<lb:get key="label_tm.label_core_training_management_239"/>
								</em>
							</span>
						</a>
						
						<!--结训条件设置 -->
						<a id="312" href="javascript:cmt.get_criteria(${item.itm_id})">
							<span class="jxtiaojians">
								<em>
									<lb:get key="label_tm.label_core_training_management_240"/>
								</em>
							</span>
						</a>
					</c:if>
					
					<!--评论管理 -->
					<c:if test="${item.itm_exam_ind ne 1}">
						<a id="317" href="javascript:itm_lst.get_itm_comment_lst(${item.itm_id})">
							<span class="plguanli">
								<em>
									<lb:get key="label_tm.label_core_training_management_244"/>
								</em>
							</span>
						</a>
					</c:if>
				</div>
			</div>
		</c:when>
		<c:when test="${item.itm_type eq 'INTEGRATED'}">
			<!--课程信息 -->
			<a id="400" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-dcf4fd active">
					<i class="kcxinxi"></i>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_231"/>
					</span>
				</div>
			</a>
			
			<!--课程包 -->
			<a id="404" href="javascript:itm_lst.get_course_list(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-f8f5b5 margin-bottom10" data="CoursePackage">
					<i class="kcbao"></i>
					<c:if test="${courseTabsRemind.rmdCoursePackage}">
						<i class="kchint"></i>
					</c:if>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_248"/>
					</span>
				</div>
			</a>
			
			<!--目标学员 -->
			<a id="402" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_LEARNER')">
				<div class="wzb-tab-6 margin-right20 bg-e3fbca margin-bottom10">
					<i class="mbxueyuan"></i>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_233"/>
					</span>
				</div>
			</a>
			
			<!--发布 -->
			<a id="408" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-ddeaff margin-bottom10">
					<i class="fabu"></i>
					<span class="tab-6-tit">
						<c:choose>
							<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
								<lb:get key="label_tm.label_core_training_management_256"/>
							</c:when>
							<c:otherwise>
								<lb:get key="label_tm.label_core_training_management_255"/>
							</c:otherwise>
						</c:choose>
					</span>
				</div>
			</a>
			<div class="wzb-tab-6 wzb-tab-6-more bg-eeeeee margin-bottom10">
				<i class="gengduo"></i>
				<span class="tab-6-tit-2"><lb:get key="global.global_more"/></span>
				<div class="wzb-tab-7 more-daohang" style="display:none;">
					<i class="more-san"></i>
					
					<!--先修课程 -->
					<a id="401" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
						<span class="xxkecheng">
							<em>
								<lb:get key="label_tm.label_core_training_management_232"/>
							</em>
						</span>
					</a>
					
					<!--处理报名 -->
					<a id="403" href="javascript:app.get_application_list('',${item.itm_id},'','','',''">
						<span class="clbaoming">
							<em>
								<lb:get key="label_tm.label_core_training_management_234"/>
							</em>
						</span>
					</a>
					
					<!--课程公告 -->
					<a id="405" href="javascript:ann.sys_lst('all','RES','${item.course.cos_res_id}','','','','','',true">
						<span class="kcgonggao">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}">
										<lb:get key="label_tm.label_core_training_management_287"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_236"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<!--结训记录 -->
					<a id="406" href="javascript:attn.get_grad_record(${item.itm_id})">
						<span class="jxjilu">
							<em>
								<lb:get key="label_tm.label_core_training_management_242"/>
							</em>
						</span>
					</a>
					
					<!--报名工作流 -->
					<a id="407" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
						<span class="bmgzuoliu">
							<em>
								<lb:get key="label_tm.label_core_training_management_245"/>
							</em>
						</span>
					</a>
				</div>
			</div>
		</c:when>
		<c:when test="${item.itm_type eq 'AUDIOVIDEO'}">
			<!--课程信息 -->
			<a id="500" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-dcf4fd active">
					<i class="kcxinxi"></i>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_231"/>
					</span>
				</div>
			</a>
			
			<!--网上内容 -->
			<a id="501" href="javascript:course_lst.edit_cos(${item.course.cos_res_id},'${item.itm_type}','false','','false')">
				<div class="wzb-tab-6 margin-right20 bg-f8f5b5 margin-bottom10" data="OnlineContent">
					<i class="wsneirong"></i>
					<c:if test="${courseTabsRemind.rmdOnlineContent}">
						<i class="kchint"></i>
					</c:if>
					<span class="tab-6-tit">
						<lb:get key="label_tm.label_core_training_management_237"/>
					</span>
				</div>
			</a>
			
			<!--发布 -->
			<a id="503" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
				<div class="wzb-tab-6 margin-right20 bg-e3fbca margin-bottom10">
					<i class="fabu"></i>
					<span class="tab-6-tit">
						<c:choose>
							<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
								<lb:get key="label_tm.label_core_training_management_256"/>
							</c:when>
							<c:otherwise>
								<lb:get key="label_tm.label_core_training_management_255"/>
							</c:otherwise>
						</c:choose>
					</span>
				</div>
			</a>
			
			<div class="wzb-tab-6 wzb-tab-6-more bg-eeeeee margin-bottom10">
				<i class="gengduo"></i>
				<span class="tab-6-tit-2"><lb:get key="global.global_more"/></span>
				<div class="wzb-tab-7 more-daohang" style="display:none;">
					<i class="more-san"></i>
	
					<!--评论管理 -->
					<a id="502" href="javascript: itm_lst.get_itm_comment_lst(${item.itm_id})">
						<span class="plguanli">
							<em>
								<lb:get key="label_tm.label_core_training_management_244"/>
							</em>
						</span>
					</a>
				</div>
			</div>
		</c:when>
	</c:choose>
</div>
	
</c:when>
<c:otherwise>
<!-- 分界点，下面是对于已发布课程，或已过期的课程导航按钮 -->
<div id="navNode" class="panel-content">
	<c:choose>
		<c:when test="${item.itm_type  eq 'SELFSTUDY'}">
			<div class="wzb-tab-7 span-margin" style="display:inline-block;">
				<!--课程信息 -->
				<a id="100" href="javascript:itm_lst.get_item_detail(${item.itm_id})" class="active">
					<span class="kcxinxis">
						<em>
							<lb:get key="label_tm.label_core_training_management_231"/>
						</em>
					</span>
				</a>
				
				<!--处理报名 -->
				<a id="103" href="javascript:app.get_application_list('',${item.itm_id},'','','','')">
					<span class="clbaoming">
						<em>
							<lb:get key="label_tm.label_core_training_management_234"/>
						</em>
					</span>
				</a>
				
				<!--结训记录 -->
				<a id="111" href="javascript:attn.get_grad_record(${item.itm_id})">
					<span class="jxjilu">
						<em>
							<lb:get key="label_tm.label_core_training_management_242"/>
						</em>
					</span>
				</a>
				
				<!--计分记录 -->
				<a id="110" href="javascript:cmt.get_scoring_itm_lst(${item.itm_id})">
					<span class="jfjilu">
						<em>
							<lb:get key="label_tm.label_core_training_management_241"/>
						</em>
					</span>
				</a>
				
				<!--跟踪报告 -->
				<a id="112" href="javascript:rpt.open_cos_lrn_lst(${item.itm_id})">
					<span class="gzbaogao">
						<em>
							<lb:get key="label_tm.label_core_training_management_243"/>
						</em>
					</span>
				</a>
				
				<!--网上内容 -->
				<a id="106" href="javascript:course_lst.edit_cos('${item.course.cos_res_id}','${item.itm_type}','${item.itm_create_run_ind eq 0 ? false : true}','${item.itm_content_def}','${item.course.cos_structure_xml eq null ? false : true}')">
					<span class="wsneirongs">
						<em>
							<lb:get key="label_tm.label_core_training_management_237"/>
						</em>
					</span>
				</a>
				
				<!--课程分数设置 -->
				<a id="108" href="javascript:cmt.get_score_scheme_list(${item.itm_id})">
					<span class="jfguizes">
						<em>
							<lb:get key="label_tm.label_core_training_management_239"/>
						</em>
					</span>
				</a>
				
				<!--结训条件设置 -->
				<a id="109" href="javascript:cmt.get_criteria(${item.itm_id})">
					<span class="jxtiaojians">
						<em>
							<lb:get key="label_tm.label_core_training_management_240"/>
						</em>
					</span>
				</a>
				
				<!--先修模块设置 -->
				<a id="107" href="javascript:itm_lst.get_mod_pre(${item.itm_id})">
					<span class="xxmkshezhi">
						<em>
							<lb:get key="label_tm.label_core_training_management_238"/>
						</em>
					</span>
				</a>
				
				<!--先修课程 -->
				<a id="101" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
					<span class="xxkecheng">
						<em>
							<lb:get key="label_tm.label_core_training_management_232"/>
						</em>
					</span>
				</a>
				
				<!--目标学员 -->
				<a id="102" href="javascript:itm_lst.get_target_rule(${item.itm_id},'TARGET_LEARNER')">
					<span class="mbxueyuans">
						<em>
							<lb:get key="label_tm.label_core_training_management_233"/>
						</em>
					</span>
				</a>
				
				<!--课程费用 -->
				<a id="104" href="javascript:itm_lst.get_item_cost(${item.itm_id})">
					<span class="kcfeiyong">
						<em>
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}">
									<lb:get key="label_tm.label_core_training_management_286"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_235"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
				
				<!--课程公告 -->
				<a id="105" href="javascript:ann.sys_lst('all','RES','${item.course.cos_res_id}','','','','','',true)">
					<span class="kcgonggao">
						<em>
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}">
									<lb:get key="label_tm.label_core_training_management_287"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_236"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
				
				<!--报名工作流 -->
				<a id="114" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
					<span class="bmgzuoliu">
						<em>
							<lb:get key="label_tm.label_core_training_management_245"/>
						</em>
					</span>
				</a>
				
				<!--评论管理 -->
				<a id="113" href="javascript:itm_lst.get_itm_comment_lst(${item.itm_id})">
					<span class="plguanli">
						<em>
							<lb:get key="label_tm.label_core_training_management_244"/>
						</em>
					</span>
				</a>
				
				<!--发布 -->
				<a id="115" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
					<span class="fabus">
						<em>
							<c:choose>
								<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
									<lb:get key="label_tm.label_core_training_management_256"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_255"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
			</div>
		</c:when>
		<c:when test="${item.itm_type eq 'CLASSROOM' and item.itm_run_ind ne 1}">
			<c:if test="${item.itm_content_def eq null or item.itm_content_def eq '' or item.itm_content_def eq 'CHILD'}">
				<div class="wzb-tab-7 span-margin" style="display:inline-block;">
					<!--课程信息 -->
					<a id="200" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
						<span class="kcxinxis">
							<em>
								<lb:get key="label_tm.label_core_training_management_231"/>
							</em>
						</span>
					</a>
					
					<!--班级管理/考试场次 -->
					<a id="201" href="javascript:itm_lst.get_item_run_list(${item.itm_id})">
						<span class="bjguanlis">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}"><lb:get key="label_tm.label_core_training_management_249"/></c:when>
									<c:otherwise><lb:get key="label_tm.label_core_training_management_246"/></c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<!--目标学员 -->
					<a id="203" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_LEARNER')">
						<span class="mbxueyuans">
							<em>
								<lb:get key="label_tm.label_core_training_management_233"/>
							</em>
						</span>
					</a>
					
					<!--日程表 -->
					<a id="204" href="javascript:itm_lst.ae_get_course_lesson(${item.itm_id})">
						<span class="rcbiaos">
							<em>
								<lb:get key="label_tm.label_core_training_management_247"/>
							</em>
						</span>
					</a>
					
					<!--先修课程 -->
					<a id="202" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
						<span class="xxkecheng">
							<em>
								<lb:get key="label_tm.label_core_training_management_232"/>
							</em>
						</span>
					</a>
					
					<!--报名工作流 -->
					<a id="211" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
						<span class="bmgzuoliu">
							<em>
								<lb:get key="label_tm.label_core_training_management_245"/>
							</em>
						</span>
					</a>
					
					<!-- 设置内容模式 -->
					<a id="206" href="javascript:itm_lst.ae_get_online_content_info(${item.itm_id})">
						<span class="clbaoming">
							<em>
								<lb:get key="label_tm.label_core_training_management_237"/>
							</em>
						</span>
					</a>
					
					<!--报名工作流 -->
					<a id="211" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
						<span class="bmgzuoliu">
							<em>
								<lb:get key="label_tm.label_core_training_management_245"/>
							</em>
						</span>
					</a>
					
					<!--评论管理 -->
					<a id="210" href="javascript: itm_lst.get_itm_comment_lst(${item.itm_id})">
						<span class="plguanli">
							<em>
								<lb:get key="label_tm.label_core_training_management_244"/>
							</em>
						</span>
					</a>
					
					<!--发布 -->
					<a id="212" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
						<span class="fabus">
							<em>
								<c:choose>
									<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
										<lb:get key="label_tm.label_core_training_management_256"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_255"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
				</div>
			</c:if>
			<c:if test="${item.itm_content_def eq 'PARENT'}">
				<div class="wzb-tab-7 span-margin" style="display:inline-block;">
					<!--课程信息 -->
					<a id="200" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
						<span class="kcxinxis">
							<em>
								<lb:get key="label_tm.label_core_training_management_231"/>
							</em>
						</span>
					</a>
					
					<!--班级管理/考试场次 -->
					<a id="201" href="javascript:itm_lst.get_item_run_list(${item.itm_id})">
						<span class="bjguanlis">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}"><lb:get key="label_tm.label_core_training_management_249"/></c:when>
									<c:otherwise><lb:get key="label_tm.label_core_training_management_246"/></c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<!--目标学员 -->
					<a id="203" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_LEARNER')">
						<span class="mbxueyuans">
							<em>
								<lb:get key="label_tm.label_core_training_management_233"/>
							</em>
						</span>
					</a>
					
					<!--日程表 -->
					<a id="204" href="javascript:itm_lst.ae_get_course_lesson(${item.itm_id})">
						<span class="rcbiaos">
							<em>
								<lb:get key="label_tm.label_core_training_management_247"/>
							</em>
						</span>
					</a>
					
					<!--先修课程 -->
					<a id="202" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
						<span class="xxkecheng">
							<em>
								<lb:get key="label_tm.label_core_training_management_232"/>
							</em>
						</span>
					</a>
					
					<!--报名工作流 -->
					<a id="211" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
						<span class="bmgzuoliu">
							<em>
								<lb:get key="label_tm.label_core_training_management_245"/>
							</em>
						</span>
					</a>
					
					<!--网上内容 -->
					<a id="206" href="javascript:course_lst.edit_cos('${item.course.cos_res_id}','${item.itm_type}','${item.itm_create_run_ind eq 0 ? false : true}','${item.itm_content_def}','${item.course.cos_structure_xml eq null ? false : true}')">
						<span class="wsneirongs">
							<em>
								<lb:get key="label_tm.label_core_training_management_237"/>
							</em>
						</span>
					</a>
					
					<!--课程分数设置 -->
					<a id="208" href="javascript:cmt.get_score_scheme_list(${item.itm_id})">
						<span class="jfguizes">
							<em>
								<lb:get key="label_tm.label_core_training_management_239"/>
							</em>
						</span>
					</a>
					
					<!--结训条件设置 -->
					<a id="209" href="javascript:cmt.get_criteria(${item.itm_id})">
						<span class="jxtiaojians">
							<em>
								<lb:get key="label_tm.label_core_training_management_240"/>
							</em>
						</span>
					</a>
					
					<!--先修模块设置 -->
					<a id="207" href="javascript:itm_lst.get_mod_pre(${item.itm_id})">
						<span class="xxmkshezhi">
							<em>
								<lb:get key="label_tm.label_core_training_management_238"/>
							</em>
						</span>
					</a>
					
					<!--课程公告 -->
					<a id="205" href="javascript:ann.sys_lst('all','RES','${cos_res_id}','','','','','',true">
						<span class="kcgonggao">
							<em>
								<c:choose>
									<c:when test="${item.itm_exam_ind eq 1}">
										<lb:get key="label_tm.label_core_training_management_287"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_236"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
					
					<!--报名工作流 -->
					<a id="211" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
						<span class="bmgzuoliu">
							<em>
								<lb:get key="label_tm.label_core_training_management_245"/>
							</em>
						</span>
					</a>
					
					<!--评论管理 -->
					<a id="210" href="javascript: itm_lst.get_itm_comment_lst(${item.itm_id})">
						<span class="plguanli">
							<em>
								<lb:get key="label_tm.label_core_training_management_244"/>
							</em>
						</span>
					</a>
					
					<!--发布 -->
					<a id="212" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
						<span class="fabus">
							<em>
								<c:choose>
									<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
										<lb:get key="label_tm.label_core_training_management_256"/>
									</c:when>
									<c:otherwise>
										<lb:get key="label_tm.label_core_training_management_255"/>
									</c:otherwise>
								</c:choose>
							</em>
						</span>
					</a>
				</div>
			</c:if>
		</c:when>
		<c:when test="${item.itm_type eq 'CLASSROOM' and item.itm_run_ind eq 1}">
			<div class="wzb-tab-7 span-margin" style="display:inline-block;">
				<!--课程信息 -->
				<a id="300" href="javascript:itm_lst.get_item_run_detail(${item.itm_id})">
					<span class="bjxinxis">
						<em>
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}">
									<lb:get key="label_tm.label_core_training_management_251"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_250"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
				
				<!--处理报名 -->
				<a id="303" href="javascript:app.get_application_list('',${item.itm_id},'','','',''">
					<span class="clbaoming">
						<em>
							<lb:get key="label_tm.label_core_training_management_234"/>
						</em>
					</span>
				</a>
				
				<!--结训记录 -->
				<a id="315" href="javascript:attn.get_grad_record(${item.itm_id})">
					<span class="jxjilu">
						<em>
							<lb:get key="label_tm.label_core_training_management_242"/>
						</em>
					</span>
				</a>
				
				<!--计分记录 -->
				<a id="313" href="javascript:cmt.get_scoring_itm_lst(${item.itm_id})">
					<span class="jfjilu">
						<em>
							<lb:get key="label_tm.label_core_training_management_241"/>
						</em>
					</span>
				</a>
				
				<!--出席率 -->
				<a id="314" href="javascript:attn.get_attd_rate_lst(${item.itm_id})">
					<span class="cxlv">
						<em>
							<lb:get key="label_tm.label_core_training_management_252"/>
						</em>
					</span>
				</a>
				
				<!--签到记录 -->
				<a id="319" href="javascript:attn.get_qiandao_Lst(${item.itm_id},0)">
					<span class="rcbiaos">
						<em>
							<lb:get key="label_tm.label_core_training_management_270"/>
						</em>
					</span>
				</a>
				
				<!--跟踪报告 -->
				<a id="316" href="javascript:rpt.open_cos_lrn_lst(${item.itm_id})">
					<span class="gzbaogao">
						<em>
							<lb:get key="label_tm.label_core_training_management_243"/>
						</em>
					</span>
				</a>
				
				<!--网上内容 -->
				<a id="309" href="javascript:course_lst.edit_cos('${item.course.cos_res_id}','${item.itm_type}','${item.itm_create_run_ind eq 0 ? false : true}','${item.itm_content_def}','${item.course.cos_structure_xml eq null ? false : true}')">
					<span class="wsneirongs">
						<em>
							<lb:get key="label_tm.label_core_training_management_237"/>
						</em>
					</span>
				</a>
				
				<!--课程分数设置 -->
				<a id="311" href="javascript:cmt.get_score_scheme_list(${item.itm_id})">
					<span class="jfguizes">
						<em>
							<lb:get key="label_tm.label_core_training_management_239"/>
						</em>
					</span>
				</a>
				
				<!--结训条件设置 -->
				<a id="312" href="javascript:cmt.get_criteria(${item.itm_id})">
					<span class="jxtiaojians">
						<em>
							<lb:get key="label_tm.label_core_training_management_240"/>
						</em>
					</span>
				</a>
				
				<%-- <!--先修课程 -->
				<a id="302" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
					<span class="xxkecheng">
						<em>
							<lb:get key="label_tm.label_core_training_management_232"/>
						</em>
					</span>
				</a> --%>
				
				<!--先修模块设置 -->
				<a id="310" href="javascript:itm_lst.get_mod_pre(${item.itm_id})">
					<span class="xxmkshezhi">
						<em>
							<lb:get key="label_tm.label_core_training_management_238"/>
						</em>
					</span>
				</a>
				
				<!--可报名学员 -->
				<%-- <a id="301" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_ENROLLMENT')">
					<span class="kbmxueyuans">
						<em>
							<lb:get key="label_tm.label_core_training_management_269"/>
						</em>
					</span>
				</a> --%>
				
				<!--日程表 -->
				<a id="304" href="javascript:itm_lst.ae_get_run_lesson(${item.itm_id})">
					<span class="rcbiaos">
						<em>
							<lb:get key="label_tm.label_core_training_management_247"/>
						</em>
					</span>
				</a>
				
				<!--开课通知 -->
				<a id="305" href="javascript:itm_lst.itm_ji_msg('${item.itm_id}')">
					<span class="kktongzhi">
						<em>
							<lb:get key="label_tm.label_core_training_management_254"/>
						</em>
					</span>
				</a>
				
				<!--预订设施 -->
				<a id="306" href="javascript:fm.get_itm_fm(${item.itm_id},'${item.itm_rsv_id}',rsv_itm_title)">
					<span class="ssyuding">
						<em>
							<lb:get key="label_tm.label_core_training_management_253"/>
						</em>
					</span>
				</a>
				
				<!--课程费用 -->
				<a id="307" href="itm_lst.get_item_cost(${item.itm_id})">
					<span class="kcfeiyong">
						<em>
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}">
									<lb:get key="label_tm.label_core_training_management_286"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_235"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
				
				<!--课程公告 -->
				<a id="308" href="javascript:ann.sys_lst('all','RES','${item.course.cos_res_id}','','','','','',true">
					<span class="kcgonggao">
						<em>
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}">
									<lb:get key="label_tm.label_core_training_management_287"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_236"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
				
				<!--评论管理 -->
				<c:if test="${item.itm_exam_ind ne 1}">
					<a id="317" href="javascript:itm_lst.get_itm_comment_lst(${item.itm_id})">
						<span class="plguanli">
							<em>
								<lb:get key="label_tm.label_core_training_management_244"/>
							</em>
						</span>
					</a>
				</c:if>
				
				<!--发布 -->
				<a id="318" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
					<span class="fabus">
						<em>
							<c:choose>
								<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
									<lb:get key="label_tm.label_core_training_management_256"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_255"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
			</div>
		</c:when>
		<c:when test="${item.itm_type eq 'INTEGRATED'}">
			<div class="wzb-tab-7 span-margin" style="display:inline-block;">
				<!--课程信息 -->
				<a id="400" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
					<span class="kcxinxis">
						<em>
							<lb:get key="label_tm.label_core_training_management_231"/>
						</em>
					</span>
				</a>
				
				<!--处理报名 -->
				<a id="403" href="javascript:app.get_application_list('',${item.itm_id},'','','',''">
					<span class="clbaoming">
						<em>
							<lb:get key="label_tm.label_core_training_management_234"/>
						</em>
					</span>
				</a>
				
				<!--结训记录 -->
				<a id="406" href="javascript:attn.get_grad_record(${item.itm_id})">
					<span class="jxjilu">
						<em>
							<lb:get key="label_tm.label_core_training_management_242"/>
						</em>
					</span>
				</a>
				
				<!--课程包 -->
				<a id="404" href="javascript:itm_lst.get_course_list(${item.itm_id})">
					<span class="kcbaos">
						<em>
							<lb:get key="label_tm.label_core_training_management_248"/>
						</em>
					</span>
				</a>
				
				<!--先修课程 -->
				<a id="401" href="javascript:itmReq.itm_req_lst(${item.itm_id})">
					<span class="xxkecheng">
						<em>
							<lb:get key="label_tm.label_core_training_management_232"/>
						</em>
					</span>
				</a>
				
				<!--目标学员 -->
				<a id="402" href="javascript:itm_lst.get_target_rule(${item.itm_id}, 'TARGET_LEARNER')">
					<span class="mbxueyuans">
						<em>
							<lb:get key="label_tm.label_core_training_management_233"/>
						</em>
					</span>
				</a>
				
				<!--课程公告 -->
				<a id="405" href="javascript:ann.sys_lst('all','RES','${item.course.cos_res_id}','','','','','',true">
					<span class="kcgonggao">
						<em>
							<c:choose>
								<c:when test="${item.itm_exam_ind eq 1}">
									<lb:get key="label_tm.label_core_training_management_287"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_236"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
				
				<!--报名工作流 -->
				<a id="407" href="javascript:itm_lst.upd_item_workflow_prep(${item.itm_id})">
					<span class="bmgzuoliu">
						<em>
							<lb:get key="label_tm.label_core_training_management_245"/>
						</em>
					</span>
				</a>
				
				<!--发布 -->
				<a id="408" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
					<span class="fabus">
						<em>
							<c:choose>
								<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
									<lb:get key="label_tm.label_core_training_management_256"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_255"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
			</div>
		</c:when>
		<c:when test="${item.itm_type eq 'AUDIOVIDEO'}">
			<div class="wzb-tab-7 span-margin" style="display:inline-block;;">
				<!--课程信息 -->
				<a id="500" href="javascript:itm_lst.get_item_detail(${item.itm_id})">
					<span class="kcxinxis">
						<em>
							<lb:get key="label_tm.label_core_training_management_231"/>
						</em>
					</span>
				</a>
				
				<!--网上内容 -->
				<a id="501" href="javascript:course_lst.edit_cos(${item.course.cos_res_id},'${item.itm_type}','false','','false')">
					<span class="wsneirongs">
						<em>
							<lb:get key="label_tm.label_core_training_management_237"/>
						</em>
					</span>
				</a>
				
				<!--评论管理 -->
				<a id="502" href="javascript: itm_lst.get_itm_comment_lst(${item.itm_id})">
					<span class="plguanli">
						<em>
							<lb:get key="label_tm.label_core_training_management_244"/>
						</em>
					</span>
				</a>
				
				<!--发布 -->
				<a id="503" href="javascript:itm_lst.get_item_publish(${item.itm_id})">
					<span class="fabus">
						<em>
							<c:choose>
								<c:when test="${item.itm_status eq 'ON' or item.itm_status eq 'ALL'}">
									<lb:get key="label_tm.label_core_training_management_256"/>
								</c:when>
								<c:otherwise>
									<lb:get key="label_tm.label_core_training_management_255"/>
								</c:otherwise>
							</c:choose>
						</em>
					</span>
				</a>
			</div>
		</c:when>
	</c:choose>
</div>
</c:otherwise>
</c:choose>
<div class="clear"></div>
<!-- 导航栏所属功能模块 -->
<c:set var="parent_code">
	<c:choose>
		<c:when test="${sessionScope.prof.current_role eq 'INSTR_1'}">FTN_AMD_TEACHING_COURSE_LIST</c:when>
		<c:when  test="${item.itm_type eq 'AUDIOVIDEO'}">FTN_AMD_OPEN_COS_MAIN</c:when>
		<c:when test="${item.itm_exam_ind eq 0}">FTN_AMD_ITM_COS_MAIN</c:when>
		<c:when test="${item.itm_exam_ind eq 1}">FTN_AMD_EXAM_MGT</c:when>
	</c:choose>
</c:set>
<input type="hidden" name="parent_code" value="${parent_code}"/>
<script type="text/javascript">
	$(function() {
		$(".wzb-tab-6-more").mouseover(function(){
          	$(".more-daohang").stop().slideToggle();

      	})
      	$(".wzb-tab-6-more").mouseout(function(){
         	$(".more-daohang").stop().slideToggle();
      	})
      	var cur_node_id = <%=cur_node_id%>;
      	$("#navNode>a>div").removeClass("active")
	    $("#navNode>a[id='" + cur_node_id +"']>div").addClass("active");
	    $("#navNode .wzb-tab-7 a").removeClass("active");
	    $("#navNode .wzb-tab-7 a[id='" + cur_node_id +"']").addClass("active");
		
	    if(${courseTabsRemind.rmdOnlineContent}){
	    	$("#navNode>a>div[data='OnlineContent']").qtip({position:{my:"bottom left",at:"top center"},content:fetchLabel('label_core_training_management_259')})
	    }
	    if(${courseTabsRemind.rmdCourseScoreSettings}){
	    	$("#navNode>a>div[data='CourseScoreSettings']").qtip({position:{my:"bottom left",at:"top center"},content:fetchLabel('label_core_training_management_260')})
	    }
	    if(${courseTabsRemind.rmdCompletionCriteriaSettings}){
	    	$("#navNode>a>div[data='CompletionCriteriaSettings']").qtip({position:{my:"bottom left",at:"top center"},content:fetchLabel('label_core_training_management_261')})
	    }
	    if(${courseTabsRemind.rmdCoursePackage}){
	    	$("#navNode>a>div[data='CoursePackage']").qtip({position:{my:"bottom left",at:"top center"},content:fetchLabel('label_core_training_management_267')})
	    }
	    
	    //导航栏所属功能模块
	    $("#parent_title").text(fetchLabel($("input[name='parent_code']").val()));
		$("#parent_title").unbind("click");
    	$("#parent_title").bind("click",function(){
    		eval("javascript:wb_utils_nav_go('"+$("input[name='parent_code']").val()+"', '${prof.usr_ent_id }', '${label_lan }')");
    	});
	});
</script>