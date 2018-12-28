<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:template match="title | hidden" mode="draw_row"/>
	<!-- =============================================================== -->
	<xsl:output indent="yes"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="parent_itm_id" select="/applyeasy/item/@parent_itm_id"/>
	<xsl:variable name="itm_title" select="/applyeasy/item/title"/>
	<xsl:variable name="itm_type" select="/applyeasy/item/@type"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="itm_status" select="/applyeasy/item/@status"/>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="turn_itm_status">
		<xsl:choose>
			<xsl:when test="$itm_status = 'OFF'">ON</xsl:when>
			<xsl:otherwise>OFF</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cur_month" select="/applyeasy/item/child_items/cur_time/@month"/>
	<xsl:variable name="cur_year" select="/applyeasy/item/child_items/cur_time/@year"/>
	<xsl:variable name="itm_type_list_root" select="/applyeasy/item/child_items/item_type_list"/>
	<xsl:variable name="create_run_ind" select="/applyeasy/item/@create_run_ind"/>
	<xsl:variable name="itm_apply_ind" select="/applyeasy/item/@apply_ind"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/><!-- 课堂 -->
	<xsl:variable name="rsv_id" select="/applyeasy/item/@rsv_id"/>
	<xsl:variable name="itm_blend_ind" select="/applyeasy/item/item_type_meta/@blend_ind"/>
	<xsl:variable name="training_type" select="/applyeasy/training_type"/>
	<xsl:variable name="tp_plan">
		<xsl:choose>
			<xsl:when test="/applyeasy/item/@plan_code !=''">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<!-- 基本信息 -->
	<xsl:variable name="label_core_training_management_229" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_229')"/>
	<!-- 高级信息 -->
	<xsl:variable name="label_core_training_management_230" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_230')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy/item"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy/item">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
				<TITLE>
					<xsl:value-of select="$wb_wizbank"/>
				</TITLE>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_message.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_mote.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_itm_req.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					fm = new wbFm(true)
					crit = new wbCriteria
					msg = new wbMessage
					itm_lst = new wbItem
					ann = new wbAnnouncement
					course_lst = new wbCourse
					rpt = new wbReport				
					app = new wbApplication	
					attn = new wbAttendance
					mote = new wbMote
					itmReq = new wbItemReq
				]]></SCRIPT>
			</head>
			<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmXml">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</BODY>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="valued_template">
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>
				訊息
			</xsl:with-param>
			<xsl:with-param name="lab_home">首頁</xsl:with-param>
			<xsl:with-param name="lab_code">編號</xsl:with-param>
			<xsl:with-param name="lab_name">課程名稱</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_ppl_allow">類型</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_prpts">狀態</xsl:with-param>
			<xsl:with-param name="lab_stdy_mrks">學分</xsl:with-param>
			<xsl:with-param name="lab_start_time">開始日期</xsl:with-param>
			<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_require">必選</xsl:with-param>
			<xsl:with-param name="lab_elective">選修</xsl:with-param>
			<xsl:with-param name="lab_day">日</xsl:with-param>
			<xsl:with-param name="lab_type_other">其他</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">此類沒有課程</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_unlimit">無限</xsl:with-param>
			<xsl:with-param name="lab_prog_dtl">課程</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_run_details">課程<xsl:value-of select="$lab_const_run"/>詳情 - </xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> 訊息</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_session_details">課程<xsl:value-of select="$lab_const_session"/>詳情 - </xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_book_sys">預訂設施</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_process_enrol">處理<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_joining_inst">開課通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_result">考勤</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_show">轉為線上</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_hide">轉為離線</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_run">取消<xsl:value-of select="$lab_const_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_session">取消<xsl:value-of select="$lab_const_session"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_chg_prpts">更改屬性</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_session_info">
				<xsl:value-of select="$lab_const_session"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_online_content">線上內容</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_pre_exm">前提和特例</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_figure">學分</xsl:with-param>
			<xsl:with-param name="lab_g_btn_content">内容</xsl:with-param>
			<xsl:with-param name="lab_g_btn_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_comments">學員課程評論</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_cost">費用</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_target_enrol">可報名學員</xsl:with-param>
			<xsl:with-param name="lab_requisite">先修條件</xsl:with-param>
			<xsl:with-param name="lab_ils_btn_content">日程表</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="valued_template">
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>信息
			</xsl:with-param>
			<xsl:with-param name="lab_home">首页</xsl:with-param>
			<xsl:with-param name="lab_code">编号</xsl:with-param>
			<xsl:with-param name="lab_name">课程名称</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_ppl_allow">类型</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_prpts">状态</xsl:with-param>
			<xsl:with-param name="lab_stdy_mrks">学分</xsl:with-param>
			<xsl:with-param name="lab_start_time">开始日期</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_require">必选</xsl:with-param>
			<xsl:with-param name="lab_elective">选修</xsl:with-param>
			<xsl:with-param name="lab_day">日</xsl:with-param>
			<xsl:with-param name="lab_type_other">其他</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">此类没有课程</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_unlimit">无限</xsl:with-param>
			<xsl:with-param name="lab_prog_dtl">课程</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_run_details">课程<xsl:value-of select="$lab_const_run"/>详情 - </xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> 信息</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_session_details">课程<xsl:value-of select="$lab_const_session"/>详情 - </xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_book_sys">预订设施</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_process_enrol">处理<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_joining_inst">开课通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_result">考勤</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_show">发布</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_hide">取消发布</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_run">取消<xsl:value-of select="$lab_const_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_session">取消<xsl:value-of select="$lab_const_session"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_chg_prpts">更改属性</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_session_info">
				<xsl:value-of select="$lab_const_session"/>信息</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_online_content">在线内容</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_pre_exm">前提和特例</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_figure">学分</xsl:with-param>
			<xsl:with-param name="lab_g_btn_content">内容</xsl:with-param>
			<xsl:with-param name="lab_g_btn_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_comments">学员课程评论</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_cost">费用</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_target_enrol">可报名学员</xsl:with-param>
			<xsl:with-param name="lab_requisite">先修条件</xsl:with-param>
			<xsl:with-param name="lab_ils_btn_content">日程表</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="valued_template">
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose> information
			</xsl:with-param>
			<xsl:with-param name="lab_home">Home</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_name">Course name</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_ppl_allow">Capacity</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_prpts">Properties</xsl:with-param>
			<xsl:with-param name="lab_stdy_mrks">Marks</xsl:with-param>
			<xsl:with-param name="lab_start_time">Start date</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_require">Require</xsl:with-param>
			<xsl:with-param name="lab_elective">Elective</xsl:with-param>
			<xsl:with-param name="lab_day">day</xsl:with-param>
			<xsl:with-param name="lab_type_other">Other</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">No Item in this category</xsl:with-param>
			<xsl:with-param name="lab_month">Month</xsl:with-param>
			<xsl:with-param name="lab_unlimit">Unlimit</xsl:with-param>
			<xsl:with-param name="lab_prog_dtl">Item</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_run_details">Learning solution  <xsl:value-of select="$lab_const_run"/> details - </xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> information</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_session_details">Learning solution  <xsl:value-of select="$lab_const_session"/> details - </xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_book_sys">Facility</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_process_enrol">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_joining_inst">Joining instruction</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_result">Result</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_show">Publish</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_hide">Unpublish</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_run">Cancel <xsl:value-of select="$lab_const_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_session">Cancel <xsl:value-of select="$lab_const_session"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_chg_prpts">Change properties</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_session_info">
				<xsl:value-of select="$lab_const_session"/> information</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_online_content">Content</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_pre_exm">Prerequisite and exemption</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_figure">Figure</xsl:with-param>
			<xsl:with-param name="lab_g_btn_content">Content</xsl:with-param>
			<xsl:with-param name="lab_g_btn_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_comments">Learners' comments</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_cost">Expenditure</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_target_enrol">Target enrollments</xsl:with-param>
			<xsl:with-param name="lab_requisite">Prerequisite</xsl:with-param>
			<xsl:with-param name="lab_ils_btn_content">Timetable</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="valued_template">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_home"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_ppl_allow"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_prpts"/>
		<xsl:param name="lab_stdy_mrks"/>
		<xsl:param name="lab_start_time"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_require"/>
		<xsl:param name="lab_elective"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_type_other"/>
		<xsl:param name="lab_itm_empty"/>
		<xsl:param name="lab_month"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_prog_dtl"/>
		<xsl:param name="lab_learning_solution_run_details"/>
		<xsl:param name="lab_session_info"/>
		<xsl:param name="lab_learning_solution_session_details"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_book_sys"/>
		<xsl:param name="lab_g_txt_btn_process_enrol"/>
		<xsl:param name="lab_g_txt_btn_joining_inst"/>
		<xsl:param name="lab_g_txt_btn_result"/>
		<xsl:param name="lab_g_txt_btn_change_show"/>
		<xsl:param name="lab_g_txt_btn_change_hide"/>
		<xsl:param name="lab_g_txt_btn_cancel_run"/>
		<xsl:param name="lab_g_txt_btn_cancel_session"/>
		<xsl:param name="lab_g_form_btn_chg_prpts"/>
		<xsl:param name="lab_g_form_btn_add"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_session_info"/>
		<xsl:param name="lab_g_txt_btn_online_content"/>
		<xsl:param name="lab_g_txt_btn_pre_exm"/>
		<xsl:param name="lab_g_txt_btn_item_figure"/>
		<xsl:param name="lab_g_btn_content"/>
		<xsl:param name="lab_g_btn_performance"/>
		<xsl:param name="lab_g_txt_btn_comments"/>
		<xsl:param name="lab_g_txt_btn_item_cost"/>
		<xsl:param name="lab_g_txt_btn_target_enrol"/>
		<xsl:param name="lab_requisite"/>
		<xsl:param name="lab_ils_btn_content"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
			<xsl:with-param  name="cur_node_id">01</xsl:with-param>
		</xsl:call-template>
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="../nav/item" mode="nav">
					<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
					<xsl:with-param name="lab_session_info" select="$lab_session_info"/>
				</xsl:apply-templates>
			</xsl:with-param>
		</xsl:call-template>
		<table>
		<tr>
			<td style="border-bottom: 1px solid #ddd;">
				<ul class="nav nav-tabs page-tabs" role="tablist" style="border-bottom:0;margin-bottom:-10px;">
					<li role="presentation" class="active"><a
						aria-controls="basic" role="tab" data-toggle="tab"
						href="#basic"><xsl:value-of select="$label_core_training_management_229"/></a></li>
					<li role="presentation"><a aria-controls="senior" role="tab"
						data-toggle="tab" href="#senior"><xsl:value-of select="$label_core_training_management_230"/></a></li>
				</ul>
			</td>
			<td valign="bottom" style="border-bottom: 1px solid #ddd;">
			    <xsl:choose>
			<xsl:when test="position() != 1">
				<xsl:call-template name="wb_ui_space"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="table_style"></xsl:with-param>
					<xsl:with-param name="extra_td">
						<td align="right">
                            <!--<xsl:if test="$run_ind = 'true' and $page_variant/@hasItmEditBtn = 'true'">
                                <xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_target_enrol"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_ENROLLMENT')</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
							</xsl:if>-->
							<xsl:if test="$page_variant/@hasItmEditBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.upd_item_prep(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<!--<xsl:if test="$page_variant/@hasItmPrerequisiteBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_requisite"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itmReq.itm_req_lst(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
							</xsl:if>
                            <xsl:if test="$page_variant/@hasCourseLessonBtn = 'true'  and $page_variant/@hasItmEditBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_ils_btn_content"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ae_get_run_lesson(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
							</xsl:if>
							<xsl:if test="$page_variant/@hasItmCostBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_item_cost"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_item_cost(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
							</xsl:if>-->
							<xsl:if test="$page_variant/@hasItmBookSystemBtn = 'true'">
								<script language="javascript" type="text/javascript"><![CDATA[ rsv_itm_title = ']]><xsl:call-template name="escape_js">
										<xsl:with-param name="input_str">
											<xsl:value-of select="/applyeasy/item/@parent_title"/>
											<xsl:text> - </xsl:text>
											<xsl:value-of select="/applyeasy/item/title"/>
										</xsl:with-param>
									</xsl:call-template><![CDATA[']]></script>
								<!--<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_book_sys"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:fm.get_itm_fm(<xsl:value-of select="$itm_id"/>,'<xsl:value-of select="$rsv_id"/>',rsv_itm_title)</xsl:with-param>
								</xsl:call-template>-->
							</xsl:if>
							<!--<xsl:if test="$page_variant/@hasCourseContentBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_btn_content"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ae_get_setting_info(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_btn_performance"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.ae_get_online_performance_info(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
							</xsl:if>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_comments"/>
								<xsl:with-param name="wb_gen_btn_href">javascript: itm_lst.get_itm_comment_lst(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
							</xsl:call-template>-->
							<!-- <xsl:if test="$page_variant/@hasItmOnOffBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:choose>
											<xsl:when test="$turn_itm_status = 'ON'">
												<xsl:value-of select="$lab_g_txt_btn_change_show"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_g_txt_btn_change_hide"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.upd_itm_status(<xsl:value-of select="$itm_id"/>,'<xsl:value-of select="$turn_itm_status"/>','<xsl:value-of select="$itm_updated_timestamp"/>', '<xsl:value-of select="$run_ind"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if> -->
							<!--<xsl:if test="$page_variant/@hasItmProcEnrolBtn = 'true'">
								<xsl:if test="$itm_apply_ind = 'true' or $run_ind = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_process_enrol"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:app.get_application_list('',<xsl:value-of select="$itm_id"/>,'','','','',true)</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</xsl:if>
							<xsl:if test="$page_variant/@hasItmJIBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_joining_inst"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.itm_ji_msg('<xsl:value-of select="$itm_id"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>-->
							<xsl:if test="$page_variant/@hasItmCancelRunBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_cancel_run"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.cancel_item_prep(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<!-- -->
							<!-- -->
							<!-- -->
							<!--
					<xsl:if test="$page_variant/@hasItmAttendanceBtn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"  select="$lab_g_txt_btn_result"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:attn.usr_lst(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				 -->
							<xsl:if test="$page_variant/@hasItmCancelSessionBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_cancel_session"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.cancel_item_prep(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$page_variant/@hasItmSessionInfoBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_session_info"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.session.get_session_list(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="$page_variant/@hasItmDelBtn = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.del_item_exec(<xsl:value-of select="$itm_id"/>,'<xsl:value-of select="$itm_updated_timestamp"/>','<xsl:value-of select="$wb_lang"/>','<xsl:choose>
											<xsl:when test="$session_ind = 'true'">session</xsl:when>
											<xsl:when test="$run_ind = 'true'">run</xsl:when>
										</xsl:choose>','<xsl:if test="$session_ind = 'true' or $run_ind = 'true'">
											<xsl:value-of select="$parent_itm_id"/>
										</xsl:if>', '', '<xsl:value-of select="$tp_plan"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<!-- -->
							<!-- Prerequsite and Exemption-->
							<!--
					<xsl:if test="$page_variant/@hasItmRequirementBtn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_pre_exm"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">Javascript:itmReq.itm_run_req_lst(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					-->
						</td>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
			</td>
		</tr>
		</table>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="basic">
				<xsl:apply-templates select="section[@id = 1]">
					<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
					<xsl:with-param name="lab_g_txt_btn_book_sys" select="$lab_g_txt_btn_book_sys"/>
					<xsl:with-param name="lab_g_txt_btn_process_enrol" select="$lab_g_txt_btn_process_enrol"/>
					<xsl:with-param name="lab_g_txt_btn_joining_inst" select="$lab_g_txt_btn_joining_inst"/>
					<xsl:with-param name="lab_g_txt_btn_result" select="$lab_g_txt_btn_result"/>
					<xsl:with-param name="lab_g_txt_btn_change_show" select="$lab_g_txt_btn_change_show"/>
					<xsl:with-param name="lab_g_txt_btn_change_hide" select="$lab_g_txt_btn_change_hide"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_run" select="$lab_g_txt_btn_cancel_run"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_session" select="$lab_g_txt_btn_cancel_session"/>
					<xsl:with-param name="lab_g_txt_btn_session_info" select="$lab_g_txt_btn_session_info"/>
					<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
					<xsl:with-param name="lab_g_form_btn_chg_prpts" select="$lab_g_form_btn_chg_prpts"/>
					<xsl:with-param name="lab_g_form_btn_add" select="$lab_g_form_btn_add"/>
					<xsl:with-param name="lab_g_form_btn_remove" select="$lab_g_form_btn_remove"/>
					<xsl:with-param name="lab_g_txt_btn_online_content" select="$lab_g_txt_btn_online_content"/>
					<xsl:with-param name="lab_g_txt_btn_pre_exm" select="$lab_g_txt_btn_pre_exm"/>
					<xsl:with-param name="lab_g_txt_btn_item_figure" select="$lab_g_txt_btn_item_figure"/>
					<xsl:with-param name="lab_g_btn_content" select="$lab_g_btn_content"/>
					<xsl:with-param name="lab_g_btn_performance" select="$lab_g_btn_performance"/>
					<xsl:with-param name="lab_g_txt_btn_comments" select="$lab_g_txt_btn_comments"/>
					<xsl:with-param name="lab_g_txt_btn_item_cost" select="$lab_g_txt_btn_item_cost"/>
					<xsl:with-param name="lab_g_txt_btn_target_enrol" select="$lab_g_txt_btn_target_enrol"/>
					<xsl:with-param name="lab_requisite" select="$lab_requisite"/>
					<xsl:with-param name="lab_ils_btn_content" select="$lab_ils_btn_content"/>
				</xsl:apply-templates>
			</div>
			<div role="tabpanel" class="tab-pane" id="senior">
				<xsl:apply-templates select="section[@id = 2]">
					<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
					<xsl:with-param name="lab_g_txt_btn_book_sys" select="$lab_g_txt_btn_book_sys"/>
					<xsl:with-param name="lab_g_txt_btn_process_enrol" select="$lab_g_txt_btn_process_enrol"/>
					<xsl:with-param name="lab_g_txt_btn_joining_inst" select="$lab_g_txt_btn_joining_inst"/>
					<xsl:with-param name="lab_g_txt_btn_result" select="$lab_g_txt_btn_result"/>
					<xsl:with-param name="lab_g_txt_btn_change_show" select="$lab_g_txt_btn_change_show"/>
					<xsl:with-param name="lab_g_txt_btn_change_hide" select="$lab_g_txt_btn_change_hide"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_run" select="$lab_g_txt_btn_cancel_run"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_session" select="$lab_g_txt_btn_cancel_session"/>
					<xsl:with-param name="lab_g_txt_btn_session_info" select="$lab_g_txt_btn_session_info"/>
					<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
					<xsl:with-param name="lab_g_form_btn_chg_prpts" select="$lab_g_form_btn_chg_prpts"/>
					<xsl:with-param name="lab_g_form_btn_add" select="$lab_g_form_btn_add"/>
					<xsl:with-param name="lab_g_form_btn_remove" select="$lab_g_form_btn_remove"/>
					<xsl:with-param name="lab_g_txt_btn_online_content" select="$lab_g_txt_btn_online_content"/>
					<xsl:with-param name="lab_g_txt_btn_pre_exm" select="$lab_g_txt_btn_pre_exm"/>
					<xsl:with-param name="lab_g_txt_btn_item_figure" select="$lab_g_txt_btn_item_figure"/>
					<xsl:with-param name="lab_g_btn_content" select="$lab_g_btn_content"/>
					<xsl:with-param name="lab_g_btn_performance" select="$lab_g_btn_performance"/>
					<xsl:with-param name="lab_g_txt_btn_comments" select="$lab_g_txt_btn_comments"/>
					<xsl:with-param name="lab_g_txt_btn_item_cost" select="$lab_g_txt_btn_item_cost"/>
					<xsl:with-param name="lab_g_txt_btn_target_enrol" select="$lab_g_txt_btn_target_enrol"/>
					<xsl:with-param name="lab_requisite" select="$lab_requisite"/>
					<xsl:with-param name="lab_ils_btn_content" select="$lab_ils_btn_content"/>
				</xsl:apply-templates>
			</div>
		</div>
	</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:choose>
					<xsl:when test="position()!=last()">
						<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:choose>
					<xsl:when test="position()!=last()">
						<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="position()!=last()">
						<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
							<xsl:value-of select="title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="section">
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_book_sys"/>
		<xsl:param name="lab_g_txt_btn_process_enrol"/>
		<xsl:param name="lab_g_txt_btn_joining_inst"/>
		<xsl:param name="lab_g_txt_btn_result"/>
		<xsl:param name="lab_g_txt_btn_change_show"/>
		<xsl:param name="lab_g_txt_btn_change_hide"/>
		<xsl:param name="lab_g_txt_btn_cancel_run"/>
		<xsl:param name="lab_g_txt_btn_cancel_session"/>
		<xsl:param name="lab_g_txt_btn_session_info"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_online_content"/>
		<xsl:param name="lab_g_txt_btn_pre_exm"/>
		<xsl:param name="lab_g_txt_btn_item_figure"/>
		<xsl:param name="lab_g_btn_content"/>
		<xsl:param name="lab_g_btn_performance"/>
		<xsl:param name="lab_g_txt_btn_comments"/>
		<xsl:param name="lab_g_txt_btn_item_cost"/>
		<xsl:param name="lab_g_txt_btn_target_enrol"/>		
		<xsl:param name="lab_requisite"/>
		<xsl:param name="lab_ils_btn_content"/>
		
		<table cellpadding="0" cellspacing="0" border="0" class="Bg" width="{$wb_gen_table_width}">
			<tr>
				<td height="10" align="right" width="20%">
				</td>
				<td width="80%">
				</td>
			</tr>
			<xsl:apply-templates select="*" mode="draw_row"/>
			<tr>
				<td height="10" align="right" width="20%">
				</td>
				<td width="80%">
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- Blocker for draw_row mode =============================================-->
	<xsl:template match="title | hidden" mode="draw_row"/>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="draw_row">
		<xsl:variable name="draw">
			<xsl:call-template name="get_show"/>
		</xsl:variable>
		<xsl:if test="contains($draw,'true')">
			<tr>
				<td align="right" valign="top" style="padding:10px 0; text-align:right; color:#666;">
					<xsl:if test="name() != 'link_list'">
						<xsl:variable name="field_desc">
							<xsl:call-template name="get_desc"/>
						</xsl:variable>
						<xsl:if test="$field_desc != '' and not(@blend_ind and @blend_ind !=$itm_blend_ind)">
							<xsl:value-of select="$field_desc"/>
							<xsl:text>：</xsl:text>
						</xsl:if>
					</xsl:if>
				</td>
				<td valign="top" style="padding:10px 0 10px 10px; color:#333;">
					<xsl:if test="prefix">
						<xsl:call-template name="prefix"/>
					</xsl:if>
					<xsl:apply-templates select="." mode="gen_field">
						<xsl:with-param name="text_class">Text</xsl:with-param>
						<xsl:with-param name="itm_blend_ind" select="$itm_blend_ind"/>
					</xsl:apply-templates>
					<xsl:if test="suffix">
						<xsl:call-template name="suffix"/>
					</xsl:if>
				</td>
			</tr>
			<xsl:call-template name="draw_separate"/>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- For Cancelled Item only ================================================ -->
	<xsl:template match="*[@type = 'iac_constant']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:variable name="result_tree" select="assigned_role_list/role/entity"/>
		<xsl:if test="$result_tree">
			<xsl:value-of select="$result_tree/@display_bil"/>
			<!--xsl:text>&#160;(ext.&#160;</xsl:text><xsl:value-of select="$result_tree/@tele"/><xsl:text>)</xsl:text-->
		</xsl:if>
	</xsl:template>
	<xsl:template match="*[@type = 'iac_select']" mode="gen_field">
		<xsl:param name="text_class"/>
		<xsl:variable name="result_tree" select="assigned_role_list/role/entity[@selected='true']"/>
		<xsl:if test="$result_tree">
			<xsl:value-of select="$result_tree/@display_bil"/>
			<!--xsl:text>&#160;(ext.&#160;</xsl:text><xsl:value-of select="$result_tree/@tele"/><xsl:text>)</xsl:text-->
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="course_lst">
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_require"/>
		<xsl:param name="lab_elective"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_type_cos"/>
		<xsl:param name="lab_type_prog"/>
		<xsl:param name="lab_type_wizb"/>
		<xsl:param name="lab_type_other"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_month"/>
		<xsl:param name="row_class"/>
		<tr valign="middle" class="{$row_class}">
			<td width="5">
			</td>
			<td align="center">
				<input type="checkbox" name="itm_lst_{@item_id}" value="{@item_id}"/>
				<input type="hidden" name="itm_lst_{@item_id}_timestamp" value="{last_updated/@timestamp}"/>
			</td>
			<td>
				<xsl:value-of select="@item_code"/>&#160;
			</td>
			<td>
				<a href="javascript:itm_lst.get_item_detail({@item_id})">
					<xsl:value-of select="title"/>
				</a>
			</td>
			<td align="center">
				<xsl:variable name="my_type" select="@item_type"/>
				<xsl:choose>
					<xsl:when test="$itm_type_list_root/item_type[@id = $my_type]">
						<xsl:call-template name="get_ity_title">
							<xsl:with-param name="itm_type" select="$my_type"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_type_other"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@capacity = 0">
						<xsl:value-of select="$lab_unlimit"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@capacity"/>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@status = 'OFF'">
						<xsl:value-of select="$lab_status_off"/>
					</xsl:when>
					<xsl:when test="@status = 'ON'">
						<xsl:value-of select="$lab_status_on"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="@core_ind='true' ">
						<xsl:value-of select="$lab_require"/>
					</xsl:when>
					<xsl:when test="@core_ind = 'false'">
						<xsl:value-of select="$lab_elective"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test=" @unit != '' ">
						<xsl:value-of select="@unit"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="eff_start_datetime/@month = $cur_month and eff_start_datetime/@year &gt;= $cur_year">
						<xsl:value-of select="eff_start_datetime/@day"/>
						<xsl:value-of select="$lab_day"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="eff_start_datetime/@month = $cur_month +1 and eff_start_datetime/@year &gt;= $cur_year">
						<xsl:value-of select="eff_start_datetime/@day"/>
						<xsl:value-of select="$lab_day"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="eff_start_datetime/@month = $cur_month+2 and eff_start_datetime/@year &gt;= $cur_year">
						<xsl:value-of select="eff_start_datetime/@day"/>
						<xsl:value-of select="$lab_day"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="eff_start_datetime/@month &gt; $cur_month+2 and eff_start_datetime/@year &gt;= $cur_year">
						<xsl:value-of select="eff_start_datetime/@month"/>
						<xsl:value-of select="$lab_month"/>
						<xsl:value-of select="eff_start_datetime/@day"/>
						<xsl:value-of select="$lab_day"/>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
