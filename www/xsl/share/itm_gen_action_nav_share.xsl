<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<xsl:import href="../utils/wb_ui_nav_link.xsl" />
	<xsl:import href="../utils/escape_js.xsl" />
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	
	<xsl:variable name="lab_cos_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1154')" />
	<xsl:variable name="lab_requisite" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1155')" />
	<xsl:variable name="lab_g_txt_btn_tarlrn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1156')" />
	<xsl:variable name="lab_g_txt_btn_process_enrol" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1157')" />
	<xsl:variable name="lab_ils_btn_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1158')" />
	<xsl:variable name="lab_g_txt_btn_item_cost" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1159')" />
	<xsl:variable name="lab_g_txt_btn_ann" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1160')" />
	<xsl:variable name="lab_g_txt_btn_content" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1161')" />
	<xsl:variable name="lab_earlier_model" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1162')" />
	<xsl:variable name="lab_course_result" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1163')" />
	<xsl:variable name="lab_grad_term" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1164')" />
	<xsl:variable name="lab_score_record" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1165')" />
	<xsl:variable name="lab_grad_record" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1166')" />
	<xsl:variable name="lab_tracking_rpt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1167')" />
	<xsl:variable name="lab_g_txt_btn_comments" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1168')" />
	<xsl:variable name="lab_g_txt_btn_workflow" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1169')" />

	<xsl:variable name="lab_run_info_exam" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1172')" />
	<xsl:variable name="lab_run_info_class" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1171')" />
	<xsl:variable name="lab_run_exam_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1172')" />
	<xsl:variable name="lab_run_class_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1173')" />
	
	<xsl:variable name="lab_run_tarlrn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1174')" />
	<xsl:variable name="lab_g_txt_btn_joining_inst" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1175')" />
	<xsl:variable name="lab_g_txt_btn_book_sys" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1176')" />
	<xsl:variable name="lab_completion_cri" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1179')" />
	<xsl:variable name="lab_att_rate" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1181')" />
	<xsl:variable name="lab_project_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1184')" />
	<xsl:variable name="lab_g_txt_btn_courses" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1185')" />
	<xsl:variable name="lab_itm_action_nav" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1201')" />
	<xsl:variable name="lab_eva_report" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')" />
    <xsl:variable name="label_core_cpt_d_management_119" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_cpd.label_core_cpt_d_management_119')" />
    <xsl:variable name="label_core_cpt_d_management_195" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_cpd.label_core_cpt_d_management_195')" />
	
	<!-- 发布 框架信息-->
	<xsl:variable name="label_core_training_management_255" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_255')" />
	<!-- 已发布 -->
	<xsl:variable name="label_core_training_management_221" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_221')" />
	<!-- 未发布 -->
	<xsl:variable name="label_core_training_management_222" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_222')" />
	
	<!-- 更改发布对像 -->
	<xsl:variable name="label_core_training_management_256" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_256')" />
	<!-- 更多 -->
	<xsl:variable name="label_core_training_management_257" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_257')" />
	<!-- 收起 -->
	<xsl:variable name="label_core_training_management_258" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_258')" />
	<!-- 还没有添加在线学习内容 -->
	<xsl:variable name="label_core_training_management_259" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_259')" />
	<!-- 还没有设定计分项目，学员将无法获得分数 -->
	<xsl:variable name="label_core_training_management_260" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_260')" />
	<!-- 还没设置课程的完成规则，学员将无法结训 -->
	<xsl:variable name="label_core_training_management_261" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_261')" />
	<!-- 还没有添加培训内容 -->
	<xsl:variable name="label_core_training_management_267" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_267')" />
	<!-- 签到记录 -->
	<xsl:variable name="label_core_training_management_270" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_270')" />
	<!-- 考试费用 -->
	<xsl:variable name="label_core_training_management_286" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_286')" />
	<!-- 公告 -->
	<xsl:variable name="label_core_training_management_236" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_236')" />
	<!-- 更多 -->
	<xsl:variable name="global_more" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global.global_more')" />
	<!-- 请先设置好该离线课程内容模式后，才能进一步设置其它内容。 -->
	<xsl:variable name="label_core_training_management_310" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_310')" />
	<!-- 该离级课程是独内容模式的，“结训条件”，“在线学习内容，“先修模块”，“计分项目”，“结训条件” 这些内容需要分别独立到每个班级中设置。
-->
	<xsl:variable name="label_core_training_management_311" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_311')" />
	<!-- 高级设置 -->
	<xsl:variable name="label_core_training_management_312" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_312')" />
	<!-- 学习记录管理 -->
	<xsl:variable name="label_core_training_management_313" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_313')" />
	<!-- 内容模式-->
	<xsl:variable name="label_core_training_management_314" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_314')" />
	
	<!-- 返回-->
	<xsl:variable name="button_back" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'button_back')" />
	<!-- 框架信息-->
	<xsl:variable name="label_core_training_management_315" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_315')" />
	<!-- 该离级考试是独内容模式的，“在线学习内容，“结训条件”，“在线学习内容，“先修模块”，“计分项目”，“结训条件” 这些内容需要分别独立到每个班级中设置。-->
	<xsl:variable name="label_core_training_management_316" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_316')" />
	
	<!-- 您没有权限进行此操作 -->
	<xsl:variable name="error_no_authority_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'error_no_authority_desc')" />
	<!-- 发布 -->
	<xsl:variable name="global_publish" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global_publish')" />
	
	<!-- 报名管理 -->
	<xsl:variable name="label_core_training_management_384" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_384')" />
	
	<!-- 学习结果管理 -->
	<xsl:variable name="label_core_training_management_385" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_385')" />
	
	<!-- 分数与结训条件 -->
	<xsl:variable name="label_core_training_management_386" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_386')" />
	
	<!-- 先修模块设置 -->
	<xsl:variable name="label_core_training_management_238" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_238')" />
	
	<xsl:variable name="hasItmCosMain" select="//itm_action_nav/hasItmCosMain/text()" />
	<xsl:variable name="hasContentMain" select="//itm_action_nav/hasContentMain/text()" />
	<xsl:variable name="hasEnrollMain" select="//itm_action_nav/hasEnrollMain/text()" />
	<xsl:variable name="hasResultMain" select="//itm_action_nav/hasResultMain/text()" />
	<!-- 我的教学课程 -->
	<xsl:variable name="hasTeachingCourse" select="//itm_action_nav/hasTeachingCourse/text()" />
 
	<xsl:variable name="itm_id" select="//itm_action_nav/@itm_id" />
	<xsl:variable name="itm_exam_ind" select="//itm_action_nav/@itm_exam_ind" />
	<xsl:variable name="itm_type" select="//itm_action_nav/@itm_type" />
	<xsl:variable name="cos_res_id" select="//itm_action_nav/@cos_res_id" />
	<xsl:variable name="run_ind" select="//itm_action_nav/@itm_run_ind" />
	<xsl:variable name="ref_ind" select="//itm_action_nav/@itm_ref_ind" />
	<xsl:variable name="create_run_ind" select="//itm_action_nav/@itm_create_run_ind" />
	<xsl:variable name="content_def" select="//itm_action_nav/@itm_content_def" />
	<xsl:variable name="rsv_id" select="//itm_action_nav/@itm_rsv_id" />
	<xsl:variable name="has_mod" select="//itm_action_nav/@has_mod" />
	<xsl:variable name="itm_status" select="//itm_action_nav/@itm_status" />
	<xsl:variable name="itm_title" select="//itm_action_nav/@itm_title" />
	<xsl:variable name="parent_title" select="//itm_action_nav/@parent_title" />
	<xsl:variable name="isStatusOff" select="//itm_action_nav/@isStatusOff" />
	<xsl:variable name="hadPublishClass" select="//itm_action_nav/@has_run" />
	<xsl:variable name="has_lesson" select="//itm_action_nav/@has_lesson" />

	<!-- 判断是否有设置网上内容等，没有设置则在标签上面进行提示 -->
	<xsl:variable name="rmdClassManagement" select="//courseTabsRemind/rmdClassManagement" />
	<xsl:variable name="rmdCompletionCriteriaSettings" select="//courseTabsRemind/rmdCompletionCriteriaSettings" />
	<xsl:variable name="rmdCoursePackage" select="//courseTabsRemind/rmdCoursePackage" />
	<xsl:variable name="rmdCourseScoreSettings" select="//courseTabsRemind/rmdCourseScoreSettings" />
	<xsl:variable name="rmdOnlineContent" select="//courseTabsRemind/rmdOnlineContent" />
	<xsl:variable name="rmdTargetLearner" select="//courseTabsRemind/rmdTargetLearner" />
	<xsl:variable name="rmdTimetable" select="//courseTabsRemind/rmdTimetable" />
	<xsl:variable name="current_role" select="//current_role" />
	<!-- 是否开放CPT/D功能 -->
	<xsl:variable name="hasCPD" select="//hasCPD" />
	
	<!-- 公共链接定义start -->
		<!--计分规则-->
		<xsl:variable name="link103"><xsl:if test="$hasContentMain='true'">javascript:cmt.get_score_scheme_list(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
		<!--报名工作流 -->
		<xsl:variable name="link101"><xsl:if test="$hasItmCosMain='true'">javascript:itm_lst.upd_item_workflow_prep(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
		<!--结训记录 -->
		<xsl:variable name="link113"><xsl:if test="$hasResultMain='true'">javascript:attn.get_grad_record(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
		<!--课程费用 -->
		<xsl:variable name="link107"><xsl:if test="$hasItmCosMain='true'">javascript:itm_lst.get_item_cost(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
		<!-- 先修模块设置 -->
		<xsl:variable name="link104"><xsl:if test="$hasContentMain='true'">javascript:itm_lst.get_mod_pre(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
		<!--处理报名 -->
		<xsl:variable name="link111"><xsl:if test="$hasEnrollMain='true'">javascript:app.get_application_list('',<xsl:value-of select="$itm_id" />,'','','','','true')</xsl:if></xsl:variable>
		<!--评论管理 -->
		<xsl:variable name="link118">javascript: itm_lst.get_itm_comment_lst(wbencrytor.cwnEncrypt(<xsl:value-of select="$itm_id" />))</xsl:variable>
		<!--课程公告 -->
		<xsl:variable name="link110"><xsl:if test="$hasContentMain='true'">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_res_id" />','','','','','',true)</xsl:if></xsl:variable>
		
		<!--先修课程 -->
		<xsl:variable name="link112"><xsl:if test="$hasContentMain='true'">javascript:itmReq.itm_req_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
	
	    <!--课程设置cpd时数 -->
		<xsl:variable name="link119">javascript: itm_lst.set_itm_cpd_gourp_hour(wbencrytor.cwnEncrypt(<xsl:value-of select="$itm_id" />))</xsl:variable>
	     
	    <!--cpd获得时数 -->
		<xsl:variable name="link121">javascript: itm_lst.itm_cpd_hours_award_record(wbencrytor.cwnEncrypt(<xsl:value-of select="$itm_id" />))</xsl:variable>
	
	<!-- 公共链接定义end -->
	
	<xsl:variable name="belong_module">
		<xsl:choose>
			<xsl:when test="$itm_exam_ind='true'">FTN_AMD_EXAM_MGT</xsl:when>
			<xsl:otherwise>FTN_AMD_TEACHING_COURSE_LIST</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="parent_code">
		<xsl:choose>
			<xsl:when test="$current_role = 'INSTR_1'">FTN_AMD_TEACHING_COURSE_LIST</xsl:when>
			<xsl:when  test="$itm_type = 'AUDIOVIDEO'">FTN_AMD_OPEN_COS_MAIN</xsl:when>
			<xsl:when test="$itm_exam_ind='false'">FTN_AMD_ITM_COS_MAIN</xsl:when>
			<xsl:when test="$itm_exam_ind='true'">FTN_AMD_EXAM_MGT</xsl:when>
		</xsl:choose>
	</xsl:variable>

	<!-- 整个导航栏模板 start -->
	<xsl:template name="itm_action_nav">
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add">false</xsl:param> <!--true: 添加课程；  false： 维护现有课程 --> 

		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_itm_req.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js" />
		<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js" />
		<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_mote.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js" />
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js" />
		<script language="JavaScript" type="text/javascript">
			<![CDATA[
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
			wbencrytor = new wbEncrytor;
			
			nod_id = ]]>'<xsl:value-of select="$cur_node_id" />'<![CDATA[
			
			 $(document).ready(function(){
			 	
			    
			    $("#buzhou>div[data='noRight']").qtip({position:{my:"bottom left",at:"top center"},content:']]><xsl:value-of select="$error_no_authority_desc" /><![CDATA['})
			    $(".wzb-tab-7-box>a[data='noRight']").qtip({position:{my:"bottom left",at:"top center"},content:']]><xsl:value-of select="$error_no_authority_desc" /><![CDATA['})
			});
			
			rsv_itm_title = ']]><xsl:call-template name="escape_js">
				<xsl:with-param name="input_str">
					<xsl:value-of select="$parent_title" />
					<xsl:text> - </xsl:text>
					<xsl:value-of select="$itm_title" />
				</xsl:with-param>
			</xsl:call-template><![CDATA['
		    ]]>
	    </script>

		<xsl:choose>
			<!-- 权限判断start -->
			<xsl:when test="$hasItmCosMain = 'true' or $hasContentMain = 'true' or $hasEnrollMain = 'true' or $hasResultMain = 'true' or $hasTeachingCourse ='true'">
				
				<xsl:choose>
					<!-- 如果是讲师 -->
					<xsl:when test="$hasTeachingCourse ='true' ">
						<xsl:if test ="$create_run_ind !='true'">
							<xsl:call-template name="nav_for_teacher">
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"></xsl:value-of></xsl:with-param>
								<xsl:with-param name="is_add"><xsl:value-of select="$is_add"></xsl:value-of></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<!-- 第一级导航栏start -->
						<xsl:call-template name="first_nav">
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"></xsl:value-of></xsl:with-param>
							<xsl:with-param name="is_add"><xsl:value-of select="$is_add"></xsl:value-of></xsl:with-param>
						</xsl:call-template>
						<!-- 第一级导航栏end -->
						
						<!-- 第一级导航和第二级导航的分割start -->
						<div class="wzb-tab-7-show"></div>
						<!-- 第一级导航和第二级导航的分割end -->
						
						<!-- 二级导航start -->
						<xsl:call-template name="second_nav">
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"></xsl:value-of></xsl:with-param>
							<xsl:with-param name="is_add"><xsl:value-of select="$is_add"></xsl:value-of></xsl:with-param>
						</xsl:call-template>
						<!-- 二级导航end -->
					</xsl:otherwise>
					
				</xsl:choose>
				
			</xsl:when>
			<!-- 权限判断end -->
			
			<xsl:otherwise></xsl:otherwise><!-- 无权限，不显示 -->
		</xsl:choose>
	</xsl:template>
	<!-- 整个导航栏模板 end -->
	
	<!-- 第一级导航模板start -->
	<xsl:template name="first_nav">
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"></xsl:param><!--true: 添加课程；  false： 维护现有课程 -->
		
		<xsl:variable name="cssWidth">
			<xsl:choose>
				<!-- 网上课程-->
		    	<xsl:when test="$itm_type = 'SELFSTUDY'">646</xsl:when>
		    	<!-- 项目式培训-->
			   	<xsl:when test="$itm_type = 'INTEGRATED'">546</xsl:when>
			   	<!-- 离线课程 -->
			   	<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true'">765</xsl:when>
			   	<!-- 班级-->
		    	<xsl:when test="$itm_type = 'CLASSROOM' and  $run_ind ='true'">765</xsl:when>
		    	<xsl:otherwise>360</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<div style="width:{$cssWidth}px;position:relative;height:98px;margin:0 auto;" id="buzhou">
			<div class="buzhou">
				<!-- 框架信息节点start 
					   所有类型课程（包括班级）共有的步骤节点
				-->
				<xsl:variable name="link01">
	  				<xsl:choose>
		   				<xsl:when test="$is_add='true'">javascript:void(0)</xsl:when>
		   				<xsl:when test=" $run_ind ='true'">javascript:itm_lst.get_item_run_detail(<xsl:value-of select="$itm_id" />)</xsl:when>
		   				<xsl:otherwise>javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id" />)</xsl:otherwise>
	  				</xsl:choose>
		 		</xsl:variable>
		        <xsl:call-template name="progress_step">
					<xsl:with-param name="step_num">01</xsl:with-param>
					<xsl:with-param name="id">01</xsl:with-param>
					<xsl:with-param name="step_title"><xsl:value-of select="$label_core_training_management_315" /></xsl:with-param>
					<xsl:with-param name="step_url"><xsl:value-of select="$link01"/></xsl:with-param>
					<xsl:with-param name="is_last">true</xsl:with-param>
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$is_add='true'">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:with-param>
					<xsl:with-param name="is_add"><xsl:value-of select="$is_add"/></xsl:with-param>
					<xsl:with-param name="is_last">false</xsl:with-param>
				</xsl:call-template>
				<!-- 框架信息节点end 
					   所有类型课程（包括班级）共有的步骤节点
				-->
				
				<xsl:choose>
					<!-- 网上课程-->
		    		<xsl:when test="$itm_type = 'SELFSTUDY'">
		    			
		    			<!-- 网上学习内容 -->
		    			<xsl:call-template name="first_online_content">
		    				<xsl:with-param name="step_num">02</xsl:with-param>
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
		    			</xsl:call-template>
						
						<!--分数与结训条件-->
						<xsl:call-template name="first_score_and_completed_condition">
							<xsl:with-param name="step_num">03</xsl:with-param>
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!--发布-->
						<xsl:call-template name="first_publish">
							<xsl:with-param name="step_num">04</xsl:with-param>
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 报名管理 -->
						<xsl:call-template name="first_enroll_management">
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 学习结果管理 -->
						<xsl:call-template name="first_learning_result_management">
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 更多 -->
						<xsl:call-template name="first_more">
		    				<xsl:with-param name="is_last">true</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
		    		</xsl:when>
			        <!-- 项目式培训-->
			    	<xsl:when test="$itm_type = 'INTEGRATED'">
			    		
			    	    <!-- 课程包 -->
			    		<xsl:variable name="link06"><xsl:if test="$is_add!='true' and $hasItmCosMain = 'true'">javascript:itm_lst.get_course_list(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
			    		<xsl:call-template name="progress_step">
							<xsl:with-param name="step_num">02</xsl:with-param>
							<xsl:with-param name="id">06</xsl:with-param>
							<xsl:with-param name="step_title"><xsl:value-of select="$lab_g_txt_btn_courses" /> </xsl:with-param>
							<xsl:with-param name="step_url"><xsl:value-of select="$link06" /></xsl:with-param>
							<xsl:with-param name="is_last">false</xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$is_add='true' or $rmdCoursePackage='true'">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:with-param>
							<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
						</xsl:call-template>
						
						<!--发布-->
						<xsl:call-template name="first_publish">
							<xsl:with-param name="step_num">03</xsl:with-param>
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 报名管理 -->
						<xsl:call-template name="first_enroll_management">
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 结训记录 -->
						<xsl:variable name="progress_step_link">
							<xsl:choose>
								<xsl:when test="$is_add!='true' and $hasItmCosMain = 'true'"><xsl:value-of select="$link113" /></xsl:when>
								<xsl:otherwise>javascript:void(0)</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:call-template name="progress_step">
							<xsl:with-param name="step_num"></xsl:with-param>
							<xsl:with-param name="id">113</xsl:with-param>
							<xsl:with-param name="step_title"><xsl:value-of select="$lab_grad_record" /></xsl:with-param>
							<xsl:with-param name="is_last">false</xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="is_full">false</xsl:with-param>
							<xsl:with-param name="step_url"><xsl:value-of select="$progress_step_link" /></xsl:with-param>
							<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
							<xsl:with-param name="space_img"></xsl:with-param>
						</xsl:call-template>
						
						<!-- 更多 -->
						<xsl:call-template name="first_more">
		    				<xsl:with-param name="is_last">true</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
			    	</xsl:when>
			    	<!-- 离线课程 -->
			    	<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true'">
					
			   			<xsl:choose>
			   				<!-- 如果是添加课程或者还没有设置内容模式 -->
			   				<xsl:when test="$is_add='true' or $content_def = '' ">
			   					<!-- 内容模式 -->
			   					<xsl:variable name="link3"><xsl:if test="$is_add!='true' and $hasItmCosMain = 'true'">javascript:itm_lst.ae_get_online_content_info('<xsl:value-of select="$itm_id" />')</xsl:if></xsl:variable>
								<xsl:call-template name="progress_step">
									<xsl:with-param name="step_num">02</xsl:with-param>
									<xsl:with-param name="id">05</xsl:with-param>
									<xsl:with-param name="step_title"><xsl:value-of select="$label_core_training_management_314" /> </xsl:with-param>
									<xsl:with-param name="step_url"><xsl:value-of select="$link3" /></xsl:with-param>
									<xsl:with-param name="is_last">false</xsl:with-param>
									<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
									<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$is_add='true' or $content_def = ''">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:with-param>
									<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
									<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
								</xsl:call-template>
								
								<!--日程表 -->
								<!-- <xsl:call-template name="first_schedule">
									<xsl:with-param name="step_num">03</xsl:with-param>
									<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template> -->
			   				</xsl:when>
			   				<xsl:otherwise>
			   				
			   					<!-- 网上学习内容 -->
				    			<xsl:call-template name="first_online_content">
				    				<xsl:with-param name="step_num">02</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
				    			</xsl:call-template>
				    				<!--日程表 -->
								<!-- <xsl:call-template name="first_schedule">
									<xsl:with-param name="step_num">03</xsl:with-param>
									<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template> -->
								
				   				
			   				</xsl:otherwise>
			   			</xsl:choose>
			   			
			    		<xsl:choose>
					    	<xsl:when test="$content_def = 'PARENT' ">
				         		
				         		<!--分数与结训条件-->
								<xsl:call-template name="first_score_and_completed_condition">
									<xsl:with-param name="step_num">03</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template>
				         		
				         		<!-- 班级管理 -->
				         		<xsl:call-template name="first_classes_management">
				         			<xsl:with-param name="step_num">04</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
				         		</xsl:call-template>
								
								<!--发布-->
								<xsl:call-template name="first_publish">
									<xsl:with-param name="step_num">05</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template>
								
					        </xsl:when>
					        
					        <xsl:otherwise>
					        	<!-- 班级管理 -->
				         		<xsl:call-template name="first_classes_management">
				         			<xsl:with-param name="step_num">03</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
				         		</xsl:call-template>
								
								<!--发布-->
								<xsl:call-template name="first_publish">
									<xsl:with-param name="step_num">04</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template>
					        </xsl:otherwise>
						</xsl:choose>
						
						<!-- 报名管理 -->
						<xsl:call-template name="first_enroll_management">
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 更多 -->
						<xsl:call-template name="first_more">
		    				<xsl:with-param name="is_last">true</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
			    	</xsl:when>
			    	
			    	<!-- 班级-->
		    		<xsl:when test="$itm_type = 'CLASSROOM' and  $run_ind ='true'">
		    		
		    			<!-- 网上学习内容 -->
		    			<xsl:call-template name="first_online_content">
		    				<xsl:with-param name="step_num">02</xsl:with-param>
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
		    			</xsl:call-template>
		    		
		    			<!--日程表 -->
						<xsl:call-template name="first_schedule">
							<xsl:with-param name="step_num">03</xsl:with-param>
							<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
			   			
			   			
						
						<xsl:choose>
							<xsl:when test="$content_def = 'PARENT'">
								<!--发布-->
								<xsl:call-template name="first_publish">
									<xsl:with-param name="step_num">04</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<!--分数与结训条件-->
								<xsl:call-template name="first_score_and_completed_condition">
									<xsl:with-param name="step_num">04</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template>
								<!--发布-->
								<xsl:call-template name="first_publish">
									<xsl:with-param name="step_num">05</xsl:with-param>
				    				<xsl:with-param name="is_last">false</xsl:with-param>
				    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
				    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
						
						<!--处理报名 -->
						<xsl:variable name="link3"><xsl:if test="$is_add!='true' and $hasItmCosMain = 'true'">javascript:itm_lst.ae_get_online_content_info('<xsl:value-of select="$itm_id" />')</xsl:if></xsl:variable>
						<xsl:call-template name="progress_step">
							<xsl:with-param name="step_num"></xsl:with-param>
							<xsl:with-param name="id">111</xsl:with-param>
							<xsl:with-param name="step_title"><xsl:value-of select="$lab_g_txt_btn_process_enrol" /></xsl:with-param>
							<xsl:with-param name="step_url"><xsl:value-of select="$link111" /></xsl:with-param>
							<xsl:with-param name="is_last">false</xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="is_full">false</xsl:with-param>
							<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasEnrollMain" /></xsl:with-param>
							<xsl:with-param name="space_img"></xsl:with-param>
						</xsl:call-template>
						
						<!-- 学习结果管理 -->
						<xsl:call-template name="first_learning_result_management">
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 更多 -->
						<xsl:call-template name="first_more">
		    				<xsl:with-param name="is_last">true</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
		    		</xsl:when>
			    	<xsl:otherwise>
			    	
			    	    <!-- 网上学习内容 -->
		    			<xsl:call-template name="first_online_content">
		    				<xsl:with-param name="step_num">02</xsl:with-param>
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
		    			</xsl:call-template>
		    			
						<!--发布-->
						<xsl:call-template name="first_publish">
							<xsl:with-param name="step_num">03</xsl:with-param>
		    				<xsl:with-param name="is_last">false</xsl:with-param>
		    				<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
		    				<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
						</xsl:call-template>
						
						<!-- 评论管理
						<xsl:variable name="link">
							<xsl:choose>
								<xsl:when test="$is_add!='true' and $hasContentMain = 'true'"><xsl:value-of select="$link118" /></xsl:when>
								<xsl:otherwise>javascript:void(0)</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						
						<xsl:call-template name="progress_step">
							<xsl:with-param name="step_num"></xsl:with-param>
							<xsl:with-param name="id">118</xsl:with-param>
							<xsl:with-param name="step_title"><xsl:value-of select="$lab_g_txt_btn_comments" /></xsl:with-param>
							<xsl:with-param name="step_url"><xsl:value-of select="$link" /></xsl:with-param>
							<xsl:with-param name="is_last">true</xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="is_full">false</xsl:with-param>
							<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
						</xsl:call-template>
						 -->
			    	</xsl:otherwise>
			    </xsl:choose>
			</div>
		</div>
	</xsl:template>
	<!-- 第一级导航模板end -->
	
	<!-- 第二级导航模板start -->
	<xsl:template name="second_nav">
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"></xsl:param> <!--true: 添加课程；  false： 维护现有课程 -->
		<xsl:variable name="second_nav_class">
			wzb-tab-7<!-- 默认的样式类名 -->
			<!-- 在上面默认类名，不同类型的课程还需要添加额外的类名 -->
			<xsl:choose>
				<!-- 网上课程-->
				<xsl:when test="$itm_type = 'SELFSTUDY'"> erjiyi</xsl:when>
				<!-- 项目式培训-->
				<xsl:when test="$itm_type = 'INTEGRATED'"> erjisi</xsl:when>
				<!-- 离线课程 -->
				<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true'"> erjier</xsl:when>
				<!-- 班级-->
				<xsl:when test="$itm_type = 'CLASSROOM' and  $run_ind ='true'"> erjisan</xsl:when>
				<!-- 公开课 -->
				<xsl:otherwise></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<!-- 是否显示二级菜单 -->
		<xsl:variable name="show_second_nav">
			<xsl:choose>
				<xsl:when test="$cur_node_id = 103 or $cur_node_id = 03">true</xsl:when>
				<xsl:when test="$cur_node_id = 101 or $cur_node_id = 106 or ($cur_node_id = 111 and  $run_ind !='true')">true</xsl:when>
				<xsl:when test="($cur_node_id = 113 and $itm_type != 'INTEGRATED') or $cur_node_id = 114 or $cur_node_id = 117 or $cur_node_id=115 or $cur_node_id=116 or $cur_node_id=120">true</xsl:when>
				<xsl:when test="$cur_node_id = 105 or $cur_node_id= 104 or $cur_node_id = 107 or $cur_node_id = 110 or $cur_node_id = 118 or $cur_node_id = 112">true</xsl:when>
				<xsl:otherwise>false</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:if test="$show_second_nav = 'true'">
			<div class="{$second_nav_class}" style="display: inline-block;">
				<xsl:choose>
					<!--<xsl:when test="$cur_node_id = 104">
						 网上学习内容 
						<xsl:variable name="link102"><xsl:if test="$is_add!='true' and $hasContentMain = 'true'">javascript:course_lst.edit_cos('<xsl:value-of select="$cos_res_id" />','<xsl:value-of select="$itm_type" />','<xsl:value-of select="$create_run_ind" />','<xsl:value-of select="$content_def" />','<xsl:value-of select="$has_mod" />')</xsl:if></xsl:variable>
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">102</xsl:with-param>
							<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_content" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link102" /></xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="icon">wsneirongs</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
						</xsl:call-template>
						  先修模块设置
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">104</xsl:with-param>
							<xsl:with-param name="title"><xsl:value-of select="$label_core_training_management_238"></xsl:value-of></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link104" /></xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="icon">xxmkshezhi</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
						</xsl:call-template>
						
					</xsl:when>
					 -->
					<xsl:when test="$cur_node_id = 103 or $cur_node_id = 03">
						<!--结训条件设置 -->
						<xsl:variable name="link03"><xsl:if test="$is_add!='true' and $hasContentMain = 'true'">javascript:cmt.get_criteria(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">03</xsl:with-param><!-- 第几步的数字 -->
							<xsl:with-param name="title"><xsl:value-of select="$lab_completion_cri" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link03" /></xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"/></xsl:with-param>
							<xsl:with-param name="icon">jxtiaojians</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
						</xsl:call-template>
						<!--计分规则-->
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">103</xsl:with-param>    
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="title"><xsl:value-of select="$lab_course_result" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link103" /></xsl:with-param>
							<xsl:with-param name="icon">jfguizes</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
						</xsl:call-template>
						
					</xsl:when>
					
					<xsl:when test="$cur_node_id = 101 or $cur_node_id = 106 or $cur_node_id = 111">
					
							<!--处理报名 -->
						<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )  or $itm_type = 'INTEGRATED' or $itm_type = 'SELFSTUDY'">
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">111</xsl:with-param>    
								<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_process_enrol" /></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link111" /></xsl:with-param>
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"/></xsl:with-param>
								<xsl:with-param name="icon">clbaoming</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasEnrollMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<!--报名工作流 -->
						<xsl:variable name="link101"><xsl:if test="$hasItmCosMain='true'">javascript:itm_lst.upd_item_workflow_prep(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">101</xsl:with-param>   
							<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_workflow" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link101" /></xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"/></xsl:with-param>
							<xsl:with-param name="icon">bmgzuoliu</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
						</xsl:call-template>
						
						<!--目标学员 -->
						<xsl:variable name="link106"><xsl:if test="$hasItmCosMain='true'">javascript:itm_lst.get_target_rule(<xsl:value-of select="$itm_id" />, 'TARGET_LEARNER');</xsl:if></xsl:variable>
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">106</xsl:with-param>     
							<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_tarlrn" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link106" /></xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"/></xsl:with-param>
							<xsl:with-param name="icon">mbxueyuans</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
						</xsl:call-template>
						
					
						
					</xsl:when>
					
					<xsl:when test="$cur_node_id = 113 or $cur_node_id = 114 or $cur_node_id = 117 or $cur_node_id = 115 or $cur_node_id = 116 or $cur_node_id = 120 or $cur_node_id = 121">
						<!--结训记录 -->
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">113</xsl:with-param>  
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="title"><xsl:value-of select="$lab_grad_record" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link113" /></xsl:with-param>
							<xsl:with-param name="icon">jxjilu</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasResultMain" /></xsl:with-param>
						</xsl:call-template>
						<!--计分记录 -->
						<xsl:variable name="link114"><xsl:if test="$hasResultMain='true'">javascript:cmt.get_scoring_itm_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">114</xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>                                                           <!-- 功能项目的ID -->
							<xsl:with-param name="title"><xsl:value-of select="$lab_score_record" /></xsl:with-param>             <!-- 功能的标题 -->
							<xsl:with-param name="url"><xsl:value-of select="$link114" /></xsl:with-param>                <!-- 点击后连接到的URL-->
							<xsl:with-param name="icon">jfjilu</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasResultMain" /></xsl:with-param>
						</xsl:call-template>
						<!--签到记录  -->
						<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )">
							<xsl:variable name="link15"><xsl:if test="$hasResultMain='true'">javascript:attn.get_qiandao_Lst(<xsl:value-of select="$itm_id" />,0)</xsl:if></xsl:variable>
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">115</xsl:with-param>   
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>                                                        <!-- 功能项目的ID -->
								<xsl:with-param name="title"><xsl:value-of select="$label_core_training_management_270" /></xsl:with-param>             <!-- 功能的标题 -->
								<xsl:with-param name="url"><xsl:value-of select="$link15" /></xsl:with-param>                <!-- 点击后连接到的URL-->
								<xsl:with-param name="icon">rcbiaos</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasResultMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<!--出席率 -->
						<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )">
							<xsl:variable name="link16"><xsl:if test="$hasResultMain='true'">javascript:javascript:attn.get_attd_rate_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">116</xsl:with-param>   
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
								<xsl:with-param name="title"><xsl:value-of select="$lab_att_rate" /></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link16" /></xsl:with-param>
								<xsl:with-param name="icon">cxlv</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasResultMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<!--跟踪报告 -->
					    <xsl:variable name="link117"><xsl:if test="$hasResultMain='true'">javascript:rpt.open_cos_lrn_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">117</xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="title"><xsl:value-of select="$lab_tracking_rpt" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link117" /></xsl:with-param>
							<xsl:with-param name="icon">gzbaogao</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasResultMain" /></xsl:with-param>
						</xsl:call-template>
						<!--测评结果 -->
					    <xsl:variable name="link120"><xsl:if test="$hasResultMain='true'">javascript:itm_lst.itm_evaluation_report(<xsl:value-of select="$cos_res_id" />,'TST')</xsl:if></xsl:variable>
						<xsl:call-template name="second_nav_item">
							<xsl:with-param name="id">120</xsl:with-param>
							<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
							<xsl:with-param name="title"><xsl:value-of select="$lab_eva_report" /></xsl:with-param>
							<xsl:with-param name="url"><xsl:value-of select="$link120" /></xsl:with-param>
							<xsl:with-param name="icon">cpjieguo</xsl:with-param>
							<xsl:with-param name="hasRight"><xsl:value-of select="$hasResultMain" /></xsl:with-param>
						</xsl:call-template>
						<!--CPT/D获得时数 -->
						<xsl:if test="$itm_type != 'INTEGRATED' and $hasCPD ='true'">
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">121</xsl:with-param>
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
								<xsl:with-param name="title"><xsl:value-of select="$label_core_cpt_d_management_119" /></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link121" /></xsl:with-param>
								<xsl:with-param name="icon">cptkhdshishu</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasResultMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</xsl:when>
					
					<xsl:when test="$cur_node_id = 107 or $cur_node_id = 110 or $cur_node_id = 105 or $cur_node_id=104 or $cur_node_id = 118 or $cur_node_id = 112 or $cur_node_id = 119">
					
							<!--  先修模块设置 -->
						<xsl:if test="($itm_type = 'CLASSROOM' and  $create_run_ind ='true' and $content_def = 'PARENT') or ($itm_type = 'CLASSROOM' and  $run_ind ='true' )  or $itm_type = 'SELFSTUDY'">
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">104</xsl:with-param>
								<xsl:with-param name="title"><xsl:value-of select="$label_core_training_management_238"></xsl:value-of></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link104" /></xsl:with-param>
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
								<xsl:with-param name="icon">xxmkshezhi</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						
						<!--先修课程 -->
						<xsl:if test="($itm_type = 'CLASSROOM' and  $create_run_ind ='true' ) or  $itm_type = 'INTEGRATED' or $itm_type = 'SELFSTUDY'">
							<xsl:variable name="link105"><xsl:if test="$hasItmCosMain='true'">javascript:itmReq.itm_req_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">105</xsl:with-param>    
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
								<xsl:with-param name="title"><xsl:value-of select="$lab_requisite" /></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link105" /></xsl:with-param>
								<xsl:with-param name="icon">xxkecheng</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						
						
						<!--课程费用 -->
						<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )  or $itm_type = 'SELFSTUDY'">
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">107</xsl:with-param>     
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
								<xsl:with-param name="title">
									<xsl:choose>
										<xsl:when test="$itm_exam_ind = 'true'">
											<xsl:value-of select="$label_core_training_management_286"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_g_txt_btn_item_cost" />
										</xsl:otherwise>
									</xsl:choose>
									</xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link107" /></xsl:with-param>
								<xsl:with-param name="icon">kcfeiyong</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasItmCosMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
	
						<!--课程公告 -->
						<xsl:if test = "$itm_type != 'AUDIOVIDEO'">
                            <xsl:call-template name="second_nav_item">
                                <xsl:with-param name="id">110</xsl:with-param>
                                <xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
                                <xsl:with-param name="title">
                                    <xsl:choose>
                                        <xsl:when test="$itm_exam_ind = 'true'">
                                            <xsl:value-of select="$label_core_training_management_236"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="$label_core_training_management_236" />
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    </xsl:with-param>
                                <xsl:with-param name="url"><xsl:value-of select="$link110" /></xsl:with-param>
                                <xsl:with-param name="icon">kcgonggao</xsl:with-param>
                                <xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
                            </xsl:call-template>
                        </xsl:if>
						
						<!--开课通知 -->
						<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )">
								<xsl:variable name="link12"><xsl:if test="$hasEnrollMain='true'">javascript:itm_lst.itm_ji_msg('<xsl:value-of select="$itm_id" />')</xsl:if></xsl:variable>
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">112</xsl:with-param>   
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id"/></xsl:with-param>
								<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_joining_inst" /></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link12" /></xsl:with-param>
								<xsl:with-param name="icon">kktongzhi</xsl:with-param>
								<xsl:with-param name="hasRight"><xsl:value-of select="$hasEnrollMain" /></xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						
						<!-- 评论管理 
						<xsl:if test="not($itm_type = 'CLASSROOM' and  $create_run_ind ='true')">
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">118</xsl:with-param>   
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
								<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_comments" /></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link118" /></xsl:with-param>
								<xsl:with-param name="icon">plguanli</xsl:with-param>
								<xsl:with-param name="hasRight">true</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						-->
						<!--CPT/D时数设置 -->
						<xsl:if test="$itm_type != 'INTEGRATED'  and $hasCPD ='true'">
							<xsl:call-template name="second_nav_item">
								<xsl:with-param name="id">119</xsl:with-param>   
								<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
								<xsl:with-param name="title"><xsl:value-of select="$label_core_cpt_d_management_195" /></xsl:with-param>
								<xsl:with-param name="url"><xsl:value-of select="$link119" /></xsl:with-param>
								<xsl:with-param name="icon">cptssshezhi</xsl:with-param>
								<xsl:with-param name="hasRight">true</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						
					</xsl:when>
					
				</xsl:choose>
	        </div>
		</xsl:if>
		
	</xsl:template>
	<!-- 第二级导航模板end -->
		    			
	<!-- 第一级节点【网上学习内容】模板start -->	    			
	<xsl:template name="first_online_content">
		<xsl:param name="step_num"/>
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		
		<xsl:variable name="link">
			<xsl:if test="$is_add!='true' and $hasContentMain = 'true'">
				<xsl:choose>
					<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true' and ($content_def = 'CHILD' or $content_def ='')">javascript:itm_lst.ae_get_online_content_info('<xsl:value-of select="$itm_id" />')</xsl:when>
					<xsl:when test="$ref_ind = 'true'">javascript:course_lst.edit_cos('<xsl:value-of select="$cos_res_id" />','<xsl:value-of select="$itm_type" />','<xsl:value-of select="$create_run_ind" />','<xsl:value-of select="$content_def" />','<xsl:value-of select="$has_mod" />')</xsl:when>
					<xsl:otherwise>javascript:course_lst.edit_cos('<xsl:value-of select="$cos_res_id" />','<xsl:value-of select="$itm_type" />','<xsl:value-of select="$create_run_ind" />','<xsl:value-of select="$content_def" />','<xsl:value-of select="$has_mod" />')</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:variable>
								
								
		
		<xsl:call-template name="progress_step">  
			<xsl:with-param name="step_num"><xsl:value-of select="$step_num"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="id">05</xsl:with-param>
			<xsl:with-param name="step_title"><xsl:value-of select="$lab_g_txt_btn_content" /></xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link" /></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$rmdOnlineContent='true' or $is_add='true'">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【网上学习内容】模板end -->
	
	
	<!-- 第一级节点【分数与结训条件】模板start -->	    			
	<xsl:template name="first_score_and_completed_condition">
		<xsl:param name="step_num"/>
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		
		<!--结训条件设置 -->
		<xsl:variable name="link"><xsl:if test="$is_add!='true' and $hasContentMain = 'true'">javascript:cmt.get_criteria(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
		
		<xsl:call-template name="progress_step">
			<xsl:with-param name="step_num"><xsl:value-of select="$step_num"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="id">03</xsl:with-param>
			<xsl:with-param name="step_title"><xsl:value-of select="$label_core_training_management_386"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link" /></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$rmdCompletionCriteriaSettings='true' or $is_add='true'">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【分数与结训条件】模板end -->
	
	<!-- 第一级节点【发布】模板start -->	    			
	<xsl:template name="first_publish">
		<xsl:param name="step_num"/>
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		<xsl:variable name="link13"><xsl:if test="$hasItmCosMain='true'"><xsl:if test="$is_add!='true' and ($itm_type != 'CLASSROOM'  or ($itm_type = 'CLASSROOM' and $content_def != ''))">javascript:itm_lst.get_item_publish(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:if></xsl:variable>
		<xsl:call-template name="progress_step">
			<xsl:with-param name="step_num">
				<xsl:choose>
					<xsl:when test="$itm_status = 'ON' or $itm_status = 'ALL'  or $itm_status='TARGET_LEARNER'">
					</xsl:when>
					<xsl:otherwise><xsl:value-of select="$step_num"></xsl:value-of></xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="id">publish</xsl:with-param>
			<xsl:with-param name="step_title">
				<xsl:choose>
					<xsl:when test="$itm_status = 'ON' or $itm_status = 'ALL'  or $itm_status='TARGET_LEARNER'"><xsl:value-of select="$label_core_training_management_221"></xsl:value-of></xsl:when>
					<xsl:otherwise><xsl:value-of select="$global_publish"></xsl:value-of></xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link13" /></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$itm_status = 'ON' or $itm_status = 'ALL'  or $itm_status='TARGET_LEARNER'" >true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
			<xsl:with-param name="space_img"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【发布】模板end -->
	
	<!-- 第一级节点【报名管理】模板start -->	    			
	<xsl:template name="first_enroll_management">
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		
		<xsl:variable name="link">
			<xsl:choose>
				<xsl:when test="$is_add!='true' and $itm_type = 'CLASSROOM' and  $create_run_ind ='true' and $content_def = ''">javascript:void(0)</xsl:when>
				<xsl:when test="$is_add!='true' and $itm_type = 'CLASSROOM' and  $create_run_ind ='true'"><xsl:value-of select="$link101" /></xsl:when>
				<xsl:when test="($is_add!='true' and $hasEnrollMain  = 'true') or ($is_add!='true' and $hasContentMain = 'true')"><xsl:value-of select="$link111" /></xsl:when>
					
				<xsl:otherwise>javascript:void(0)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:call-template name="progress_step">
			<xsl:with-param name="step_num"></xsl:with-param>
			<xsl:with-param name="id">enroll_manager</xsl:with-param>
			<xsl:with-param name="step_title"><xsl:value-of select="$label_core_training_management_384"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link"/></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full">false</xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
			<xsl:with-param name="space_img"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【报名管理】模板end -->
	
	<!-- 第一级节点【学习结果管理】模板start -->	    			
	<xsl:template name="first_learning_result_management">
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		
		<xsl:variable name="link">
			<xsl:choose>
				<xsl:when test="($is_add!='true' and $hasContentMain = 'true') or ($is_add!='true' and $hasResultMain = 'true') "><xsl:value-of select="$link113" /></xsl:when>
				<xsl:otherwise>javascript:void(0)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:call-template name="progress_step">
			<xsl:with-param name="step_num"></xsl:with-param>
			<xsl:with-param name="id">learning_result_manager</xsl:with-param>
			<xsl:with-param name="step_title"><xsl:value-of select="$label_core_training_management_385"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link"/></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full">false</xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
			<xsl:with-param name="space_img"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【学习结果管理】模板end -->
	
	<!-- 第一级节点【更多】模板start -->	    			
	<xsl:template name="first_more">
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		
		<xsl:variable name="link">
			<xsl:choose>
				<xsl:when test="$is_add!='true' and ($itm_type != 'CLASSROOM'  or ($itm_type = 'CLASSROOM' and $content_def != ''))">
					<xsl:choose>
						<xsl:when test="$itm_type = 'SELFSTUDY' or $run_ind ='true' or ($itm_type = 'CLASSROOM' and $content_def = 'PARENT' and $create_run_ind ='true')"><xsl:value-of select="$link104"/></xsl:when><!-- 如果是网上课程或者是班级，链接到课程费用 -->
						<xsl:otherwise><xsl:value-of select="$link112"/></xsl:otherwise><!-- 先修课程 -->
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>javascript:void(0)</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:call-template name="progress_step">
			<xsl:with-param name="step_num"></xsl:with-param>
			<xsl:with-param name="id">more_function</xsl:with-param>
			<xsl:with-param name="step_title"><xsl:value-of select="$label_core_training_management_257" /></xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link" /></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full">false</xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【更多】模板end -->
	
	<!-- 第一级节点【日程表】模板start -->	    			
	<xsl:template name="first_schedule">
		<xsl:param name="step_num"/>
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		
		<xsl:variable name="link108">
			<xsl:if test="$hasItmCosMain='true'">
  				<xsl:choose>
   				<xsl:when test=" $run_ind ='true'">javascript:itm_lst.ae_get_run_lesson(<xsl:value-of select="$itm_id" />)</xsl:when>
   				<xsl:otherwise>javascript:itm_lst.ae_get_course_lesson(<xsl:value-of select="$itm_id" />)</xsl:otherwise>
  				</xsl:choose>
 			</xsl:if>
 		</xsl:variable>
		
		<xsl:variable name="link">
			<xsl:choose>
				<xsl:when test="$is_add!='true' and ($itm_type != 'CLASSROOM'  or ($itm_type = 'CLASSROOM' and $content_def != ''))"><xsl:value-of select="$link108"/></xsl:when>
				<xsl:otherwise></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:call-template name="progress_step">
			<xsl:with-param name="step_num"><xsl:value-of select="$step_num"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="id">108</xsl:with-param>
			<xsl:with-param name="step_title"><xsl:value-of select="$lab_ils_btn_content" /></xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link" /></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$has_lesson='false' or $is_add='true'">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【日程表】模板end -->
	
	<!-- 第一级节点【班级管理】模板start -->	    			
	<xsl:template name="first_classes_management">
		<xsl:param name="step_num"/>
		<xsl:param name="is_last"/>
		<xsl:param name="cur_node_id"/>
		<xsl:param name="is_add"/>
		<xsl:variable name="link6"><xsl:if test="$is_add!='true' and ($content_def = 'CHILD' or $content_def = 'PARENT' )">javascript:itm_lst.get_item_run_list(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
		<xsl:call-template name="progress_step">
			<xsl:with-param name="step_num"><xsl:value-of select="$step_num"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="id">04</xsl:with-param>
			<xsl:with-param name="step_title">
				<xsl:choose>
					<xsl:when test="$itm_exam_ind='true'">
						<xsl:value-of select="$lab_run_exam_info" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_run_info_class" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="step_url"><xsl:value-of select="$link6" /></xsl:with-param>
			<xsl:with-param name="is_last"><xsl:value-of select="$is_last"></xsl:value-of></xsl:with-param>
			<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
			<xsl:with-param name="is_full"><xsl:choose><xsl:when test="$hadPublishClass='false' or $is_add='true'">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:with-param>
			<xsl:with-param name="is_add"><xsl:value-of select="$is_add" /></xsl:with-param>
			<xsl:with-param name="hasRight"><xsl:value-of select="$hasContentMain" /></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- 第一级节点【班级管理】模板end -->
	
	<!-- 第一级导航栏每个步骤的模板start -->
	<xsl:template name="progress_step">
		<xsl:param name="id"></xsl:param>  <!-- 功能项目的ID -->
		<xsl:param name="step_num">...</xsl:param>  <!-- 第几步的数字 -->
		<xsl:param name="step_title"/>              <!-- 步骤的标题 -->
		<xsl:param name="step_url">javascript:void(0)</xsl:param>   <!-- 点击后连接到的URL-->
		<xsl:param name="is_last"/>                  <!-- 是否为最后一个结点-->
		<xsl:param name="cur_node_id"/>              <!-- 当前先中结点-->
		<xsl:param name="is_full">false</xsl:param>  <!-- 是否需要填允-->
		<xsl:param name="is_add">false</xsl:param>   <!-- 是否需要填允-->
		<xsl:param name="space_img">../../static/images/wzb-dian-2.png</xsl:param><!-- 结点之间分隔图-->
		<xsl:param name="space_img_none">../../static/images/wzb-dian-0.png</xsl:param><!-- 没有结点之间分隔图-->
		<xsl:param name="hasRight">true</xsl:param>
		
		<!-- 判断是否为当前节点 -->
		<xsl:variable name="is_cur">
			<xsl:choose>
				<xsl:when test="$cur_node_id= $id">true</xsl:when>
				<xsl:when test="$id= 02"><xsl:if test="$cur_node_id=102 ">true</xsl:if></xsl:when>
				<xsl:when test="$id= 03"><xsl:if test="$cur_node_id=103 or $cur_node_id=03">true</xsl:if></xsl:when>
				<xsl:when test="$id= 'enroll_manager'"><xsl:if test="$cur_node_id=101 or $cur_node_id=106 or $cur_node_id=111">true</xsl:if></xsl:when>
				<xsl:when test="$id= 'learning_result_manager'"><xsl:if test="$cur_node_id=113 or $cur_node_id=114 or $cur_node_id=117 or $cur_node_id=115 or $cur_node_id=116 or $cur_node_id=120">true</xsl:if></xsl:when>
				<xsl:when test="$id= 'more_function'"><xsl:if test="$cur_node_id=105 or $cur_node_id=104 or $cur_node_id=107 or $cur_node_id=110 or $cur_node_id=118 or $cur_node_id=112">true</xsl:if></xsl:when>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="class">
			wzb-float wzb-buzhou1<!-- 默认的样式类名 -->
			<!-- 在上面默认类名，不同类型的课程还需要添加额外的类名 -->
			<xsl:choose>
				<!-- 网上课程-->
				<xsl:when test="$itm_type = 'SELFSTUDY'"></xsl:when>
				<!-- 项目式培训-->
				<xsl:when test="$itm_type = 'INTEGRATED'"> wzb-buzhou4</xsl:when>
				<!-- 离线课程 -->
				<xsl:when test="$itm_type = 'CLASSROOM' and  $create_run_ind ='true'"> wzb-buzhou2</xsl:when>
				<!-- 班级-->
				<xsl:when test="$itm_type = 'CLASSROOM' and  $run_ind ='true'"> wzb-buzhou3</xsl:when>
				<!-- 公开课 -->
				<xsl:otherwise> wzb-buzhou5</xsl:otherwise>
			</xsl:choose>
			
			<!-- 每个步骤的不同情况下有不同的类名 -->
			<!-- 该节点是否当前的节点，包括其子节点 -->
			<xsl:if test="$is_cur = 'true'"> cur</xsl:if>
			
			<!-- 没有URL，即不可点击 -->
			<xsl:if test="$step_url=''"> wzb-buzhou-no</xsl:if>
			
			<!-- 有URL，可点击 -->
			<xsl:choose>
				<xsl:when test="$step_url != '' and $step_url !='javascript:void(0)'"> wzb-hover</xsl:when>
				<xsl:otherwise> wzb-buzhou-no-2</xsl:otherwise>
			</xsl:choose>
			
			<!-- 是否实心 -->
			<xsl:if test="$is_full= 'true'"> over</xsl:if>
			
			<xsl:choose>
				<xsl:when test="$id='publish' and ($itm_status = 'ON' or $itm_status = 'ALL'  or $itm_status='TARGET_LEARNER')"> wzb-buzhou1_unpublish</xsl:when>
				<xsl:when test="$id='enroll_manager' or $id='learning_result_manager' or $id='more_function' or $id='113' or $id='118' or $id='111'"> wzb-quan-wu
					<xsl:choose>
						<xsl:when test="$id='more_function'"> wzb-buzhou-more</xsl:when>
						<xsl:when test="$id='enroll_manager'"> wzb-buzhou-baominggl</xsl:when>
						<xsl:when test="$id='learning_result_manager'"> wzb-buzhou-xuexijggl</xsl:when>
						<xsl:when test="$id='118'"> wzb-buzhou-pinglungl</xsl:when>
						<xsl:when test="$id='113'"> wzb-buzhou-jiexunjl</xsl:when>
						<xsl:when test="$id='111'"> wzb-buzhou-chulibm</xsl:when>
					</xsl:choose>
				</xsl:when>
			</xsl:choose> 
		</xsl:variable>
		
		<a>
			<xsl:attribute name="href">
				<xsl:choose>
					<xsl:when test="$step_url=''">javascript:void(0)</xsl:when>
					<xsl:otherwise><xsl:value-of select="$step_url"></xsl:value-of></xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<div class="{$class}" id="{$id}" cur="{$cur_node_id}">
				<xsl:value-of select= "$step_num"/>
				<span><xsl:value-of select="$step_title"/></span>
				<div class="wzb-buzhou-jiao sjcolor-5"></div>
			</div>
		</a>
		
		<xsl:choose>
			<xsl:when test="$is_last='true'">
			</xsl:when>
			<xsl:otherwise>
				<div class="wzb-float wzb-fudian">
					<xsl:choose>
						<xsl:when test="$space_img = ''">
							<img src="{$space_img_none}"/>
						</xsl:when>
						<xsl:otherwise>
							<img src="{$space_img}"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 第一级导航栏每个步骤的模板end -->
	
	<!-- 二级导航每一项的模板start -->
	<xsl:template name="second_nav_item">
		<xsl:param name="id"></xsl:param><!-- 功能项目的ID -->
		<xsl:param name="title"/> <!-- 步骤的标题 -->
		<xsl:param name="url">javascript:void(0)</xsl:param><!-- 点击后连接到的URL-->
		<xsl:param name="cur_node_id"/><!-- 当前先中结点-->
		<xsl:param name="hasRight">true</xsl:param>
		<xsl:param name="icon"></xsl:param><!-- 最左侧的图标 -->
		
		<xsl:variable name="class">
			<xsl:if test="$cur_node_id= $id"> active</xsl:if>
		</xsl:variable>
		
		<xsl:choose>
			<xsl:when test="$hasRight='true'">
				<a href="{$url}" class="{$class}" id="{$id}"><span class="{$icon}"><em><xsl:value-of select="$title"/></em></span></a>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
		
	</xsl:template>
	<!-- 二级导航每一项的模板end -->
	
	<!-- 讲师导航模板start -->
	<xsl:template name="nav_for_teacher">
		<xsl:param name="is_add">false</xsl:param> <!--true: 添加课程；  false： （默认值）维护现有课程 --> 
		<xsl:param name="cur_node_id"/><!--当前结点标记 -->   

		<div class="wzb-tab-7" style="display: inline-block;">
		 	<!--网上内容-->
			<xsl:if test="($itm_type = 'CLASSROOM' or  $itm_type = 'SELFSTUDY')">
				<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:course_lst.edit_cos('<xsl:value-of select="$cos_res_id" />','<xsl:value-of select="$itm_type" />','<xsl:value-of select="$create_run_ind" />','<xsl:value-of select="$content_def" />','<xsl:value-of select="$has_mod" />')</xsl:if></xsl:variable>
				<xsl:call-template name="second_nav_item">
					<xsl:with-param name="id">102</xsl:with-param> 
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_content" /></xsl:with-param>
					<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
					<xsl:with-param name="icon">wsneirongs</xsl:with-param>
					<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="$itm_type = 'INTEGRATED'">
				<xsl:variable name="link"><xsl:if test="$hasTeachingCourse = 'true'">javascript:itm_lst.get_course_list(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
				<xsl:call-template name="second_nav_item">
					<xsl:with-param name="id">102</xsl:with-param> 
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="title"><xsl:value-of select="$lab_g_txt_btn_courses" /></xsl:with-param>
					<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
					<xsl:with-param name="icon">wsneirongs</xsl:with-param>
					<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!--日程表 -->
			<xsl:if test="$itm_type = 'CLASSROOM'" >
				<xsl:variable name="link6">
					<xsl:if test="$hasItmCosMain='false'">
		   				<xsl:choose>
			   				<xsl:when test=" $run_ind ='false'">javascript:itm_lst.ae_get_run_lesson(<xsl:value-of select="$itm_id" />)</xsl:when>
			   				<xsl:otherwise>javascript:itm_lst.ae_get_course_lesson(<xsl:value-of select="$itm_id" />)</xsl:otherwise>
		   				</xsl:choose>
	   				</xsl:if>
	   			</xsl:variable>
				<xsl:call-template name="second_nav_item">
					<xsl:with-param name="id">108</xsl:with-param>   
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="title"><xsl:value-of select="$lab_ils_btn_content" /></xsl:with-param>
					<xsl:with-param name="url"><xsl:value-of select="$link6" /></xsl:with-param>
					<xsl:with-param name="icon">rcbiaos</xsl:with-param>
					<xsl:with-param name="hasRight">true</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		
			<!--计分项目-->
			<xsl:if test="($itm_type = 'CLASSROOM' or  $itm_type = 'SELFSTUDY')">
				<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:cmt.get_score_scheme_list(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
				<xsl:call-template name="second_nav_item">
					<xsl:with-param name="id">103</xsl:with-param>    
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="title"><xsl:value-of select="$lab_course_result" /></xsl:with-param>
					<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
					<xsl:with-param name="icon">jfguizes</xsl:with-param>
					<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
		  	<xsl:if test="($itm_type = 'CLASSROOM' or  $itm_type = 'SELFSTUDY')">
				<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:cmt.get_criteria(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
				<xsl:call-template name="second_nav_item">
					<xsl:with-param name="id">03</xsl:with-param>    
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="title"><xsl:value-of select="$lab_completion_cri" /></xsl:with-param>
					<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
					<xsl:with-param name="icon">jfguizes</xsl:with-param>
					<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!--先修模块设置 -->
			<xsl:if test="($itm_type = 'CLASSROOM' or  $itm_type = 'SELFSTUDY')">
				<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:itm_lst.get_mod_pre(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
				<xsl:call-template name="second_nav_item">
					<xsl:with-param name="id">104</xsl:with-param>    
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="title"><xsl:value-of select="$lab_earlier_model" /></xsl:with-param>
					<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
					<xsl:with-param name="icon">xxmkshezhi</xsl:with-param>
					<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
	
			<!--课程公告 -->
			<xsl:if test="$itm_type = 'CLASSROOM' or $itm_type = 'SELFSTUDY'  or $itm_type = 'INTEGRATED'">
				<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_res_id" />','','','','','',true)</xsl:if></xsl:variable>
				<xsl:call-template name="second_nav_item">
					<xsl:with-param name="id">110</xsl:with-param>  
					<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
					<xsl:with-param name="title">
						<xsl:choose>
							<xsl:when test="$itm_exam_ind = 'true'">
								<xsl:value-of select="$label_core_training_management_236"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$label_core_training_management_236" />
							</xsl:otherwise>
						</xsl:choose>
						</xsl:with-param>
					<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
					<xsl:with-param name="icon">kcgonggao</xsl:with-param>
					<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="not($itm_type = 'CLASSROOM' and  $create_run_ind ='true' )">
				<!--结训记录 -->
				<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )  or $itm_type = 'INTEGRATED' or $itm_type = 'SELFSTUDY'">
					<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:attn.get_grad_record(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
					<xsl:call-template name="second_nav_item">
						<xsl:with-param name="id">113</xsl:with-param>  
						<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
						<xsl:with-param name="title"><xsl:value-of select="$lab_grad_record" /></xsl:with-param>
						<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
						<xsl:with-param name="icon">jxjilu</xsl:with-param>
						<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				
				<!--计分记录 -->
				<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' ) or $itm_type = 'SELFSTUDY'">
					<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:cmt.get_scoring_itm_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
					<xsl:call-template name="second_nav_item">
						<xsl:with-param name="id">114</xsl:with-param>
						<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
						<xsl:with-param name="title"><xsl:value-of select="$lab_score_record" /></xsl:with-param>
						<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
						<xsl:with-param name="icon">jfjilu</xsl:with-param>
						<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				
				<!--签到记录  -->
				<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )">
					<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:attn.get_qiandao_Lst(<xsl:value-of select="$itm_id" />,0)</xsl:if></xsl:variable>
					<xsl:call-template name="second_nav_item">
						<xsl:with-param name="id">115</xsl:with-param>   
						<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
						<xsl:with-param name="title"><xsl:value-of select="$label_core_training_management_270" /></xsl:with-param>
						<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
						<xsl:with-param name="icon">rcbiaos</xsl:with-param>
						<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				
				<!--出席率 -->
				<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )">
					<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:javascript:attn.get_attd_rate_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
					<xsl:call-template name="second_nav_item">
						<xsl:with-param name="id">116</xsl:with-param>   
						<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
						<xsl:with-param name="title"><xsl:value-of select="$lab_att_rate" /></xsl:with-param>
						<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
						<xsl:with-param name="icon">cxlv</xsl:with-param>
						<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				
				<!--跟踪报告 -->
				<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' ) or $itm_type = 'SELFSTUDY'">
					<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:rpt.open_cos_lrn_lst(<xsl:value-of select="$itm_id" />)</xsl:if></xsl:variable>
					<xsl:call-template name="second_nav_item">
						<xsl:with-param name="id">117</xsl:with-param>
						<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
						<xsl:with-param name="title"><xsl:value-of select="$lab_tracking_rpt" /></xsl:with-param>
						<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
						<xsl:with-param name="icon">gzbaogao</xsl:with-param>
						<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				
				<!--测评报告 -->
				<xsl:if test="($itm_type = 'CLASSROOM' and  $run_ind ='true' )  or $itm_type = 'INTEGRATED' or $itm_type = 'SELFSTUDY'">
					<xsl:variable name="link"><xsl:if test="$hasTeachingCourse='true'">javascript:itm_lst.itm_evaluation_report(<xsl:value-of select="$cos_res_id" />,'TST')</xsl:if></xsl:variable>
					<xsl:call-template name="second_nav_item">
						<xsl:with-param name="id">120</xsl:with-param>  
						<xsl:with-param name="cur_node_id"><xsl:value-of select="$cur_node_id" /></xsl:with-param>
						<xsl:with-param name="title"><xsl:value-of select="$lab_eva_report" /></xsl:with-param>
						<xsl:with-param name="url"><xsl:value-of select="$link" /></xsl:with-param>
						<xsl:with-param name="icon">jxjilu</xsl:with-param>
						<xsl:with-param name="hasRight"><xsl:value-of select="$hasTeachingCourse" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</xsl:if>
		</div>
		
	</xsl:template>
	<!-- 讲师导航模板end -->
	
</xsl:stylesheet>


