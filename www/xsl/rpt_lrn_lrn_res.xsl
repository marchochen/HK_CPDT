<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">

	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/rpt_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_type" select="/report/report_body/template/@type"/>
	<xsl:variable name="ent_id" select="/report/meta/cur_usr/@ent_id"/>
	<xsl:variable name="template_id" select="/report/report_body/template/@id"/>
	<xsl:variable name="rpt_name" select="/report/report_body/spec/title"/>
	<xsl:variable name="rsp_id" select="/report/report_body/spec/@spec_id"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="attendance_status_list" select="/report/report_body/meta/attendance_status_list"/>
	<xsl:variable name="student_id" select="report/report_body/student/@ent_id"/>
	<xsl:variable name="num" select="count(report/report_body/report_list/report_group/record/student[@ent_id=$student_id])"/>
	<xsl:variable name="total" select="count(report/report_body/report_list/report_group)"/>
	<xsl:variable name="cur_page" select="/report/report_body/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/report/report_body/pagination/@page_size"/>
	<xsl:variable name="total_rec" select="/report/report_body/report_list/report_summary/total_learners/@value"/>
	<xsl:variable name="cur_sort_col" select="/report/report_body/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/report/report_body/pagination/@sort_order"/>
	<xsl:variable name="page_timestamp" select="/report/report_body/pagination/@timestamp"/>
	<xsl:variable name="itm_type_lst" select="/report/report_body/meta/item_type_list"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/report_group) > 0">
				<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'usr_content_lst' or @name = 'itm_content_lst' or @name='run_content_lst' or @name = 'content_lst'])"/>
			</xsl:when>
			<xsl:otherwise>2</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order = 'asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="rpt_xls" select="/report/report_body/template/xsl_list/xsl[@type = 'download']"/>
	<!-- =============================================================== -->
	<xsl:variable name="isStudent">
		<xsl:choose>
			<xsl:when test="report/report_body/student">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="show_stat_only" select="report/report_body/report_list/show_stat_only/text()"/>
	<xsl:variable name="lab_course_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_course_code')"/>
	<xsl:variable name="lab_course_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_course_title')"/>
	<xsl:variable name="lab_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_type')"/>
	<xsl:variable name="lab_categories" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_categories')"/>
	<xsl:variable name="lab_tc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/>
	<xsl:variable name="lab_att_create_timestamp" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_att_create_timestamp')"/>
	<xsl:variable name="lab_att_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_att_status')"/>
	<xsl:variable name="lab_att_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_att_date')"/>
	<xsl:variable name="lab_cov_commence_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_cov_commence_datetime')"/>
	<xsl:variable name="lab_cov_last_acc_datetime" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_cov_last_acc_datetime')"/>
	<xsl:variable name="lab_total_attempt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_total_attempt')"/>
	<xsl:variable name="lab_total_time" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_total_time')"/>
	<xsl:variable name="lab_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_score')"/>
	
	<!-- =============================================================== -->
	<xsl:template match="/report">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var mgt_rpt = new wbManagementReport;
			unit = 0;
			
			lrn_soln = new wbLearnSolution;
			
			function sortCol(colName, order){
				wb_utils_nav_get_urlparam('sort_col',colName,'sort_order',order,'timestamp','');
			}
		
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="rsp_id" value=""/>
				<input type="hidden" name="spec_name" value=""/>
				<input type="hidden" name="spec_value" value=""/>
				<input type="hidden" name="download" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="rte_id" value=""/>
				<input type="hidden" name="usr_ent_id" value=""/>
				<input type="hidden" name="rpt_type" value=""/>
				<input type="hidden" name="rpt_type_lst" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_mgmt_rpt">學員學習報告</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">報告詳情</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">用戶</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_all_my_staff">我的所有下屬</xsl:with-param>
			<xsl:with-param name="lab_my_direct_staff">我的直屬下屬</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_cos">課程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有課程</xsl:with-param>
			<xsl:with-param name="lab_include_no_record_learner">包含無學習記錄學員</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_show_in_progress_attendance">顯示<b>進行中</b>的學習活動</xsl:with-param>
			<xsl:with-param name="lab_period">完成日期</xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">內容</xsl:with-param>
			<!-- content attribute text of course -->
			<xsl:with-param name="lab_item_content">課程內容</xsl:with-param>
			<!-- content attribute text of run -->
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>內容</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_app_ext_4">Application Ext 4</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">報名日期</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_att_rate">
				<xsl:value-of select="$lab_const_session"/>到課率</xsl:with-param>
			<xsl:with-param name="lab_att_remark">考勤備註</xsl:with-param>
			<xsl:with-param name="lab_att_status">考勤狀況</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">第一次訪問</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">最後一次訪問</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成績</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">學習時間</xsl:with-param>
			<xsl:with-param name="lab_others_content">其他</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">嘗試</xsl:with-param>
			<!--statistic information-->
			<xsl:with-param name="lab_total_courses">課程總數</xsl:with-param>
			<xsl:with-param name="lab_total_learners">學員總數</xsl:with-param>
			<xsl:with-param name="lab_report_summary">報告摘要</xsl:with-param>
			<xsl:with-param name="lab_total_enroll">學習記錄總數</xsl:with-param>
			<xsl:with-param name="lab_learner_id">用戶名</xsl:with-param>
			<xsl:with-param name="lab_learner_name">暱稱</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">嘗試總數</xsl:with-param>
			<xsl:with-param name="lab_total_timespent">學習總時長</xsl:with-param>
			<xsl:with-param name="lab_average_score">平均成績</xsl:with-param>
			<xsl:with-param name="lab_day">天</xsl:with-param>
			<!-- content attribute text of learner -->
			<xsl:with-param name="lab_user_content">學員資料</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_grand_total">總計</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_enrollment">學習記錄</xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用匯出查看全部記錄）</xsl:with-param>
			<xsl:with-param name="lab_show">顯示</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">詳情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">匯出摘要</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_d">匯出詳細</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我負責的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我負責的課程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">報讀我負責課程的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我負責學員報讀的課程</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_mgmt_rpt">学员学习报告</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">报告详情</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">用户</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有学员</xsl:with-param>
			<xsl:with-param name="lab_all_my_staff">我的所有下属</xsl:with-param>
			<xsl:with-param name="lab_my_direct_staff">我的直属下属</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">课程目录</xsl:with-param>
			<xsl:with-param name="lab_cos">课程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有课程</xsl:with-param>
			<xsl:with-param name="lab_include_no_record_learner">包含无学习记录学员</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_show_in_progress_attendance">显示<b>进行中</b>的学习活动</xsl:with-param>
			<xsl:with-param name="lab_period">完成日期</xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<!-- content attribute text of course -->
			<xsl:with-param name="lab_item_content">课程内容</xsl:with-param>
			<!-- content attribute text of run -->
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/>内容</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_app_ext_4">培训合同</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">录取日期</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_att_rate">
				<xsl:value-of select="$lab_const_session"/>到课率</xsl:with-param>
			<xsl:with-param name="lab_att_remark">考勤备注</xsl:with-param>
			<xsl:with-param name="lab_att_status">完成状态</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">首次访问</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">上次访问</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">学习时长</xsl:with-param>
			<xsl:with-param name="lab_others_content">其他</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">尝试</xsl:with-param>
			<!--statistic information-->
			<xsl:with-param name="lab_total_courses">课程总数</xsl:with-param>
			<xsl:with-param name="lab_total_learners">学员总数</xsl:with-param>
			<xsl:with-param name="lab_report_summary">报告摘要</xsl:with-param>
			<xsl:with-param name="lab_total_enroll">学习记录总数</xsl:with-param>
			<xsl:with-param name="lab_learner_id">用戶名</xsl:with-param>
			<xsl:with-param name="lab_learner_name">昵称</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">尝试总数</xsl:with-param>
			<xsl:with-param name="lab_total_timespent">学习总时长</xsl:with-param>
			<xsl:with-param name="lab_average_score">平均成绩</xsl:with-param>
			<xsl:with-param name="lab_day">天</xsl:with-param>
			<!-- content attribute text of learner -->
			<xsl:with-param name="lab_user_content">学员资料</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_grand_total">总计</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_enrollment">学习记录</xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用导出查看全部记录）</xsl:with-param>
			<xsl:with-param name="lab_show">显示</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">详情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">导出摘要</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_d">导出详细</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我负责的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">我负责的课程</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">报读我负责课程的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">我负责学员报读的课程</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_mgmt_rpt">Management report</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">Report details</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">Learner</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">Learner groups</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">All learners</xsl:with-param>
			<xsl:with-param name="lab_all_my_staff">All my staff</xsl:with-param>
			<xsl:with-param name="lab_my_direct_staff">My direct reports</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">Course catalog</xsl:with-param>
			<xsl:with-param name="lab_cos">Course</xsl:with-param>
			<xsl:with-param name="lab_att_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_include_no_record_learner">Include learner with no enrollment</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_show_in_progress_attendance">Show in progress activities</xsl:with-param>
			<xsl:with-param name="lab_period">Completion date</xsl:with-param>
			<xsl:with-param name="lab_not_specified">-- All --</xsl:with-param>
			<!-- content attribute text -->
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_all_cos">All courses</xsl:with-param>
			<!-- content attribute text of course -->
			<xsl:with-param name="lab_item_content">Course content</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_run_content">
				<xsl:value-of select="$lab_const_run"/> content</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_others_content">Others</xsl:with-param>
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<xsl:with-param name="lab_att_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_att_remark">Attendance remarks</xsl:with-param>
			<xsl:with-param name="lab_app_ext_4">Application ext 4</xsl:with-param>
			<xsl:with-param name="lab_cov_score">Score</xsl:with-param>
			<xsl:with-param name="lab_att_rate">
				<xsl:value-of select="$lab_const_session"/> attendance rate</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">Last access</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">First access</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">Attempts</xsl:with-param>
			<xsl:with-param name="lab_cov_total_time">Time spent</xsl:with-param>
			<!--statistic information-->
			<xsl:with-param name="lab_total_courses">Total courses</xsl:with-param>
			<xsl:with-param name="lab_total_learners">Total learners</xsl:with-param>
			<xsl:with-param name="lab_report_summary">Report summary</xsl:with-param>
			<xsl:with-param name="lab_total_enroll">Total enrollments</xsl:with-param>
			<xsl:with-param name="lab_learner_id">User ID</xsl:with-param>
			<xsl:with-param name="lab_learner_name">Name</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">Total attempts</xsl:with-param>
			<xsl:with-param name="lab_total_timespent">Total time spent</xsl:with-param>
			<xsl:with-param name="lab_average_score">Average score</xsl:with-param>
			<xsl:with-param name="lab_day">days</xsl:with-param>
			<!-- content attribute text of learner -->
			<xsl:with-param name="lab_user_content">User information</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_grand_total">Total credit earned</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_learner">learners</xsl:with-param>
			<xsl:with-param name="lab_enrollment">enrollments</xsl:with-param>
			<xsl:with-param name="lab_more_record">(Use export to show all records)</xsl:with-param>
			<xsl:with-param name="lab_show">Show </xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">Detail</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">Export summary</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export_d">Export details</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">My responsible learners</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course">My responsible courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">Learners that have enrolled in my responsible courses</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn_course">Courses that have been enrolled by my responsible learners</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ==================================content===================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_rpt_details"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_all_my_staff"/>
		<xsl:param name="lab_my_direct_staff"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_include_no_record_learner"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_show_in_progress_attendance"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_report_summary"/>
		<xsl:param name="lab_total_courses"/>
		<xsl:param name="lab_total_learners"/>
		<xsl:param name="lab_total_enroll"/>
		<xsl:param name="lab_learner_id"/>
		<xsl:param name="lab_learner_name"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_total_timespent"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="lab_not_specified"/>
		<xsl:param name="lab_item_content"/>
		<xsl:param name="lab_user_content"/>
		<xsl:param name="lab_others_content"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_enrollment"/>
		<xsl:param name="lab_more_record"/>
		<xsl:param name="lab_show"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_att_remark"/>
		<xsl:param name="lab_app_ext_4"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<xsl:param name="lab_grand_total"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_g_form_btn_export"/>
		<xsl:param name="lab_g_form_btn_export_d"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_period"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$rpt_name != ''">
						<xsl:value-of select="$rpt_name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_rte_title">
							<xsl:with-param name="rte_type" select="$rpt_type"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/report_group) = 0">
				<xsl:call-template name="report_criteria">
					<xsl:with-param name="this_width" select="$wb_gen_table_width"/>
					<xsl:with-param name="lab_cos" select="$lab_cos"/>
					<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
					<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
					<xsl:with-param name="lab_include_no_record_learner" select="$lab_include_no_record_learner"/>
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
					<xsl:with-param name="lab_lrn" select="$lab_lrn"/>
					<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
					<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
					<xsl:with-param name="lab_all_my_staff" select="$lab_all_my_staff"/>
					<xsl:with-param name="lab_my_direct_staff" select="$lab_my_direct_staff"/>
					<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
					<xsl:with-param name="lab_from" select="$lab_from"/>
					<xsl:with-param name="lab_na" select="$lab_na"/>
					<xsl:with-param name="lab_to" select="$lab_to"/>
					<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
					<xsl:with-param name="lab_all" select="$lab_all"/>
					<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
					<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
					<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
					<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>	
				</xsl:call-template>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<xsl:if test="$show_stat_only = 'true'">
					<tr>
						<td>
							<xsl:call-template name="report_criteria">
								<xsl:with-param name="this_width">100%</xsl:with-param>
								<xsl:with-param name="lab_cos" select="$lab_cos"/>
								<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
								<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
								<xsl:with-param name="lab_include_no_record_learner" select="$lab_include_no_record_learner"/>
								<xsl:with-param name="lab_yes" select="$lab_yes"/>
								<xsl:with-param name="lab_no" select="$lab_no"/>
								<xsl:with-param name="lab_lrn" select="$lab_lrn"/>
								<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
								<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
								<xsl:with-param name="lab_all_my_staff" select="$lab_all_my_staff"/>
								<xsl:with-param name="lab_my_direct_staff" select="$lab_my_direct_staff"/>
								<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
								<xsl:with-param name="lab_from" select="$lab_from"/>
								<xsl:with-param name="lab_na" select="$lab_na"/>
								<xsl:with-param name="lab_to" select="$lab_to"/>
								<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
								<xsl:with-param name="lab_all" select="$lab_all"/>
								<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
								<xsl:with-param name="lab_answer_for_course" select="$lab_answer_for_course"/>
								<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
								<xsl:with-param name="lab_answer_for_lrn_course" select="$lab_answer_for_lrn_course"/>	
							</xsl:call-template>
						</td>
					</tr>
					<!--================= Report Summary ================ -->
					<tr>
						<td>
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td colspan="{$col_size + 2}">
										<xsl:call-template name="wb_ui_head">
											<xsl:with-param name="text">
												<xsl:value-of select="$lab_report_summary"/>
											</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="wb_ui_line">
											<xsl:with-param name="width">100%</xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="wb_ui_space">
											<xsl:with-param name="height">5</xsl:with-param>
										</xsl:call-template>
										<xsl:variable name="img_name" select="report_body/report_list/report_summary/total_enrollments/enrollment_img_name"/>
										<xsl:if test="$img_name !=''">
											<img src="{$img_name}"/>
										</xsl:if>
									</td>
								</tr>
								<tr>
									<td class="wzb-form-label">
										<span class="TitleText">
											<xsl:value-of select="$lab_total_courses"/>
											<xsl:text>：</xsl:text>
										</span>
									</td>
									<td class="wzb-form-control">
										<span class="StatDataText">
											<xsl:value-of select="report_body/report_list/report_summary/total_courses/@value"/>
										</span>
									</td>
								</tr>
								<tr>
									<td class="wzb-form-label">
										<span class="TitleText">
											<xsl:value-of select="$lab_total_learners"/>
											<xsl:text>：</xsl:text>
										</span>
									</td>
									<td class="wzb-form-control">
											<span class="StatDataText">
												<xsl:value-of select="report_body/report_list/report_summary/total_learners/@value"/>
											</span>
									</td>
								</tr>
								<tr>
									<td class="wzb-form-label">
										<span class="TitleText">
											<xsl:value-of select="$lab_total_attempts"/>
											<xsl:text>：</xsl:text>
										</span>
									</td>
									<td class="wzb-form-control">
										<span class="StatDataText">
											<xsl:value-of select="report_body/report_list/report_summary/total_attempts/@value"/>
										</span>
									</td>
								</tr>
								<tr>
									<td class="wzb-form-label">
										<xsl:value-of select="$lab_total_timespent"/>
										<xsl:text>：</xsl:text>
									</td>
									<td class="wzb-form-control">
										<span class="StatDataText">
											<xsl:value-of select="report_body/report_list/report_summary/total_time_spent/@day"/>
											<xsl:text> </xsl:text>
											<xsl:value-of select="$lab_day"/>
											<xsl:text> </xsl:text>
											<xsl:value-of select="report_body/report_list/report_summary/total_time_spent/@time"/>
										</span>
									</td>
								</tr>
								<tr>
									<td class="wzb-form-label">
										<xsl:value-of select="$lab_average_score"/>
										<xsl:text>：</xsl:text>
									</td>
									<td class="wzb-form-control">
										<span class="StatDataText">
											<xsl:value-of select="report_body/report_list/report_summary/average_score/@value"/>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					</xsl:if>
					<xsl:choose>
						<xsl:when test="$show_stat_only = 'true'">
							<xsl:call-template name="report_group_show_main_only">
								<xsl:with-param name="lab_na" select="$lab_na"/>
								<xsl:with-param name="lab_recycle_bin" select="$lab_recycle_bin"/>
								<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
								<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
								<xsl:with-param name="lab_att_date" select="$lab_att_date"/>
								<xsl:with-param name="lab_att_remark" select="$lab_att_remark"/>
								<xsl:with-param name="lab_app_ext_4" select="$lab_app_ext_4"/>
								<xsl:with-param name="lab_cov_score" select="$lab_cov_score"/>
								<xsl:with-param name="lab_att_rate" select="$lab_att_rate"/>
								<xsl:with-param name="lab_cov_last_acc_datetime" select="$lab_cov_last_acc_datetime"/>
								<xsl:with-param name="lab_cov_commence_datetime" select="$lab_cov_commence_datetime"/>
								<xsl:with-param name="lab_total_attempt" select="$lab_total_attempt"/>
								<xsl:with-param name="lab_cov_total_time" select="$lab_cov_total_time"/>
								<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
								<xsl:with-param name="lab_report_summary" select="$lab_report_summary"/>
								<xsl:with-param name="lab_total_enroll" select="$lab_total_enroll"/>
								<xsl:with-param name="lab_learner_id" select="$lab_learner_id"/>
								<xsl:with-param name="lab_learner_name" select="$lab_learner_name"/>
								<xsl:with-param name="lab_learner_id" select="$lab_learner_id"/>
								<xsl:with-param name="lab_learner_name" select="$lab_learner_name"/>
								<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
								<xsl:with-param name="lab_day" select="$lab_day"/>
								<xsl:with-param name="lab_total_attempts" select="$lab_total_attempts"/>
								<xsl:with-param name="lab_total_timespent" select="$lab_total_timespent"/>
								<xsl:with-param name="lab_average_score" select="$lab_average_score"/>
								<xsl:with-param name="this" select="."/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="report_body/report_list/report_group">
								<xsl:with-param name="lab_na" select="$lab_na"/>
								<xsl:with-param name="lab_recycle_bin" select="$lab_recycle_bin"/>
								<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
								<xsl:with-param name="lab_att_status" select="$lab_att_status"/>
								<xsl:with-param name="lab_att_date" select="$lab_att_date"/>
								<xsl:with-param name="lab_att_remark" select="$lab_att_remark"/>
								<xsl:with-param name="lab_app_ext_4" select="$lab_app_ext_4"/>
								<xsl:with-param name="lab_cov_score" select="$lab_cov_score"/>
								<xsl:with-param name="lab_att_rate" select="$lab_att_rate"/>
								<xsl:with-param name="lab_cov_last_acc_datetime" select="$lab_cov_last_acc_datetime"/>
								<xsl:with-param name="lab_cov_commence_datetime" select="$lab_cov_commence_datetime"/>
								<xsl:with-param name="lab_total_attempt" select="$lab_total_attempt"/>
								<xsl:with-param name="lab_cov_total_time" select="$lab_cov_total_time"/>
								<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
								<xsl:with-param name="lab_report_summary" select="$lab_report_summary"/>
								<xsl:with-param name="lab_total_enroll" select="$lab_total_enroll"/>
								<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
								<xsl:with-param name="lab_day" select="$lab_day"/>
								<xsl:with-param name="lab_total_attempts" select="$lab_total_attempts"/>
								<xsl:with-param name="lab_total_timespent" select="$lab_total_timespent"/>
								<xsl:with-param name="lab_average_score" select="$lab_average_score"/>
								<xsl:with-param name="lab_enrollment" select="$lab_enrollment"/>
								<xsl:with-param name="lab_more_record" select="$lab_more_record"/>
								<xsl:with-param name="lab_show" select="$lab_show"/>
								<xsl:with-param name="this" select="."/>
							</xsl:apply-templates>
						</xsl:otherwise>
					</xsl:choose>
				</table>
				
				<xsl:if test="$show_stat_only = 'true'">
				<table cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr class="SecBg">
					<td width="60%" valign="top" align="left">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:choose>
							<xsl:when test="$total_rec = 0 ">
								<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>		
							</xsl:when>
							<xsl:otherwise>
						   		<span class="SecBg">
									<xsl:value-of select="$lab_showing"/>
									<xsl:text>&#160;1</xsl:text>
									<xsl:text>&#160;-&#160;</xsl:text>
									<xsl:value-of select="$total"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$lab_page_of"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$total_rec"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$lab_page_piece"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$lab_learner"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:value-of select="$lab_more_record"/>
									</span>
								</xsl:otherwise>
						</xsl:choose>
					</td>
					<td align="right" width="40%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				 </tr>
            </table>
            </xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:if test="count(report_body/report_list/report_group)&gt;0">
							<xsl:variable name="title">
								<xsl:call-template name="escape_js">
									<xsl:with-param name="input_str" select="report_body/spec/title/text()"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:if test="$show_stat_only = 'true'">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_export"/>
									<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="$rpt_xls"/>','<xsl:value-of select="$title"/>','true')</xsl:with-param>
								</xsl:call-template>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_export_d"/>
									<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="$rpt_xls"/>','<xsl:value-of select="$title"/>','false')</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:window.close()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<!-- ===========================report group==================================== -->
	<xsl:template match="report_group">
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_att_remark"/>
		<xsl:param name="lab_app_ext_4"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_report_summary"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_total_enroll"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_total_timespent"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="lab_enrollment"/>
		<xsl:param name="lab_more_record"/>
		<xsl:param name="lab_show"/>
		<xsl:param name="this"/>
		<xsl:if test="position()!=1">
			<tr>
				<td height="15" colspan="{$col_size}"/>
			</tr>
		</xsl:if>
		<!-- start draw table header -->
		<tr>
			<td colspan="{12 + 2}">
				<span class="TitleText">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="user/@id"/>
							<xsl:text> - </xsl:text>
							<xsl:value-of select="user/display_name"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line">
						<xsl:with-param name="width">100%</xsl:with-param>
					</xsl:call-template>
				</span>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="height">5</xsl:with-param>
				</xsl:call-template>
				<xsl:if test="count(record) > 0">
					<img src="{group_summary/total_enrollments/enrollment_img_name}"/>
					<br/>
					<span class="TitleText">
						<table>
							<tr>
								<td class="wzb-form-label"><xsl:value-of select="$lab_total_attempts"/>：</td>
								<td class="wzb-form-control">
									<span class="StatDataText">
										<xsl:value-of select="group_summary/total_attempts/@value"/>
									</span>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label"><xsl:value-of select="$lab_total_timespent"/>：</td>
								<td class="wzb-form-control">
									<span class="StatDataText">
										<xsl:value-of select="group_summary/total_time_spent/@day"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="$lab_day"/>
										<xsl:text> </xsl:text>
										<xsl:value-of select="group_summary/total_time_spent/@time"/>
									</span>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label"><xsl:value-of select="$lab_average_score"/> ：</td>
								<td class="wzb-form-control">
									<span class="StatDataText">
										<xsl:value-of select="group_summary/average_score/@value"/>
									</span>
								</td>
							</tr>
						</table>
					</span>
				</xsl:if>
			</td>
		</tr>
		<xsl:choose>
			<xsl:when test="count(record) > 0">
			<tr>
				<td>
					<table class="table wzb-ui-table">
						<tr class="SecBg wzb-ui-table-head">
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_course_code"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_course_title"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_type"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_categories"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_tc"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_att_create_timestamp"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_att_status"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_att_date"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_cov_commence_datetime"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_cov_last_acc_datetime"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_total_attempt"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_total_time"/></span>
							</td>
							<td align="left" nowrap="nowrap">
								<span class="SmallText"><xsl:value-of select="$lab_score"/></span>
							</td>
						</tr>
						<xsl:apply-templates select="record">
							<xsl:with-param name="lab_na" select="$lab_na"/>
							<xsl:with-param name="lab_recycle_bin" select="$lab_recycle_bin"/>
							<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
						</xsl:apply-templates>
					</table>
				</td>
			</tr>
				<!-- 
				<tr>					
				 <td colspan="{12 + 2}"  width="100%">
				   		<span>
							<xsl:value-of select="$lab_show"/>
							<xsl:text>&#160;1</xsl:text>
							<xsl:text>&#160;-&#160;</xsl:text>
							<xsl:value-of select="count(record)"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_page_of"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="group_summary/total_enrollments/@value"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_page_piece"/>	
							<xsl:value-of select="$lab_enrollment"/>
						</span>
					</td>
				 </tr> -->
				<tr class="Line">
					<td colspan="{13 + 2}" height="1">
						<img width="1" src="{$wb_img_path}tp.gif" height="1" border="0"/>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="RowsEven">
					<td height="15" colspan="{12 + 2}" align="left">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_item"/>
							<xsl:with-param name="top_line">false</xsl:with-param>
							<xsl:with-param name="bottom_line">false</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ==============================record================================= -->
	<xsl:template match="record">
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:variable name="usr_ent_id" select="student/@ent_id"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">StatRowsEven</xsl:when>
				<xsl:otherwise>StatRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="">
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:value-of select="item/@code"/>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:value-of select="item/@title"/>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="dummy_type" select="item/@dummy_type"/>
					</xsl:call-template>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="catalog">
							<xsl:for-each select="catalog/node_list/node">
								<xsl:value-of select="title"/>
								<xsl:if test="position()!=last()">, </xsl:if>
							</xsl:for-each>	
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:value-of select="item/@tcr_title"/>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="item/@att_create_timestamp"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
				<xsl:variable name="status_id">
					<xsl:choose>
						<xsl:when test="item/@att_ats_id=''">0</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="item/@att_ats_id"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
					<xsl:apply-templates select="$attendance_status_list/status[@id=$status_id]"/>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="item/@att_timestamp != ''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="item/@att_timestamp"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="item/@cov_commence_datetime != ''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="item/@cov_commence_datetime"/>
								</xsl:with-param>
							</xsl:call-template>		
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
						<xsl:when test="item/@cov_last_acc_datetime != ''">
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp">
									<xsl:value-of select="item/@cov_last_acc_datetime"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
					
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:value-of select="item/@attempt"/>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
					<xsl:choose>
							<xsl:when test="item/@used_time != ''">
								<xsl:value-of select="substring-before(item/@used_time,'.')"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
				</span>
			</td>
			<td valign="top" align="left" nowrap="nowrap">
				<span class="SmallText">
				<xsl:choose>
					<xsl:when test="item/@score > 0">
						<xsl:call-template name="display_score">
							<xsl:with-param name="score">
								<xsl:value-of select="item/@score"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>--</xsl:otherwise>
				</xsl:choose>
					
				</span>
			</td>
		</tr>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template match="status">
		<xsl:call-template name="get_ats_title">
			<xsl:with-param name="ats_id" select="@id"/>
		</xsl:call-template>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="report_criteria">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_include_no_record_learner"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_all_my_staff"/>
		<xsl:param name="lab_my_direct_staff"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:param name="lab_answer_for_lrn_course"/>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$this_width"/>
		</xsl:call-template>
		<table cellpadding="0" cellspacing="0" border="0" width="{$this_width}" class="Bg">
			<tr>
				<td width="150" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<!-- learner / group -->
			<xsl:if test="report_body/spec/data_list/data[@name='ent_id'] or report_body/spec/data_list/data[@name='s_usg_ent_id_lst'] or  report_body/spec/data_list/data[@name='all_user_ind'] ">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='all_user_ind']/@value = '1' or count(report_body/spec/data_list/data[@name='usr_ent_id'])  != 0 ">
									<xsl:value-of select="$lab_lrn"/>
									<xsl:text>：</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_lrn_group"/>
									<xsl:text>：</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='all_user_ind']/@value = '1'">
									<xsl:choose>
										<xsl:when test="$tc_enabled='true'">
											<xsl:choose>
												<xsl:when test="report_body/spec/data_list/data[@name='answer_for_lrn']/@value = '1' and report_body/spec/data_list/data[@name='answer_for_course_lrn']/@value = '0' ">
													<xsl:value-of select="$lab_answer_for_lrn"/>
												</xsl:when>
												<xsl:when test="report_body/spec/data_list/data[@name='answer_for_lrn']/@value = '0' and report_body/spec/data_list/data[@name='answer_for_course_lrn']/@value = '1' ">
													<xsl:value-of select="$lab_answer_for_course_lrn"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_by_all_user"/>
												</xsl:otherwise>										
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_by_all_user"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:when test="report_body/spec/data_list/data[@name='s_usg_ent_id_lst']/@value = 'my_staff'">
									<xsl:value-of select="$lab_all_my_staff"/>
								</xsl:when>
								<xsl:when test="report_body/spec/data_list/data[@name='s_usg_ent_id_lst']/@value = 'my_direct_staff'">
									<xsl:value-of select="$lab_my_direct_staff"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="report_body/spec/data_list/data[@name='usr_ent_id'or @name='usg_ent_id']">
										<xsl:variable name="usr_ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:variable name="usg_ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:value-of select="/report/report_body/presentation/data[ (@name='usr_ent_id' and @value=$usr_ent_id) or (@name='usg_ent_id' and @value=$usg_ent_id)]/@display"/>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
				<tr>
					<td width="150" align="right" valign="top" class="wzb-form-label">
						<span class="TitleText"><xsl:value-of select="$lab_include_no_record_learner"/>：</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="/report/report_body/spec/data_list/data[@name='include_no_record' and @value='true'] or not(/report/report_body/spec/data_list/data[@name='include_no_record'])">
									<xsl:value-of select="$lab_yes"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_no"/>
								</xsl:otherwise>
							</xsl:choose> 
						</span>
					</td>
				</tr>
			<!-- display either specific courses or specific course catalogs or all courses -->
			<xsl:choose>
				<!-- course id -->
				<xsl:when test="count(report_body/spec/data_list/data[@name='itm_id']) !=0">
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:for-each select="report_body/spec/data_list/data[@name='itm_id']">
									<xsl:variable name="itm_id">
										<xsl:value-of select="@value"/>
									</xsl:variable>
									<xsl:value-of select="/report/report_body/presentation/data[@name='itm_id' and @value=$itm_id]/@display"/>
									<xsl:if test="position()!=last()">, </xsl:if>
								</xsl:for-each>
							</span>
						</td>
					</tr>
				</xsl:when>
				<!-- course catalog -->
				<xsl:when test="report_body/spec/data_list/data[@name='tnd_id']">
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos_catalog"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:for-each select="report_body/spec/data_list/data[@name='tnd_id']">
									<xsl:variable name="tnd_id">
										<xsl:value-of select="@value"/>
									</xsl:variable>
									<xsl:value-of select="/report/report_body/presentation/data[@name='tnd_id' and @value=$tnd_id]/@display"/>
									<xsl:if test="position()!=last()">, </xsl:if>
								</xsl:for-each>
							</span>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:choose>
									<xsl:when test="$tc_enabled='true'">
										<xsl:choose>
											<xsl:when test="report_body/spec/data_list/data[@name='answer_for_course']/@value = '1' and report_body/spec/data_list/data[@name='answer_for_lrn_course']/@value = '0' ">
												<xsl:value-of select="$lab_answer_for_course"/>
											</xsl:when>
											<xsl:when test="report_body/spec/data_list/data[@name='answer_for_course']/@value = '0' and report_body/spec/data_list/data[@name='answer_for_lrn_course']/@value = '1' ">
												<xsl:value-of select="$lab_answer_for_lrn_course"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_all_cos"/>
											</xsl:otherwise>										
										</xsl:choose>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_all_cos"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!-- enrollment date -->
			<xsl:choose>
				<xsl:when test="count(report_body/spec/data_list/data[@name='att_create_start_datetime']) = 0 and count(report_body/spec/data_list/data[@name='att_create_end_datetime']) = 0">
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_att_create_timestamp"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:value-of select="$lab_from"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='att_create_start_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_create_start_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_to"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='att_create_end_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_create_end_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!-- completion status -->
			<xsl:if test="report_body/spec/data_list/data[@name='ats_id']">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_att_status"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:variable name="cur_ats_id" select="/report/report_body/spec/data_list/data[@name='ats_id']/@value"/>
							<xsl:choose>
								<xsl:when test="$cur_ats_id='0'">
									<xsl:value-of select="$lab_all"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="get_ats_title">
										<xsl:with-param name="ats_id" select="$cur_ats_id"/>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space"/>
	</xsl:template>
	<!-- ===========================report group show main only==================================== -->
	<xsl:template name="report_group_show_main_only">
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_recycle_bin"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_att_remark"/>
		<xsl:param name="lab_app_ext_4"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_report_summary"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_total_enroll"/>
		<xsl:param name="lab_learner_id"/>
		<xsl:param name="lab_learner_name"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_total_timespent"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="this"/>
		<table width="{$wb_gen_table_width}" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td><img border="0" height="5" src="{$wb_img_path}tp.gif" width="1"/></td>
          </tr>
       </table>
       <table width="{$wb_gen_table_width}" border="0" class="Bg table wzb-ui-table" cellspacing="0" cellpadding="3">
          <tr class="SecBg wzb-ui-table-head"> 
	    	  <td><img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/></td>
            <td valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_learner_id"/></span></td>
            <td valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_learner_name"/></span></td>
            <td align="right" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_total_enroll"/></span></td>
            <xsl:for-each select="report_body/report_list/report_group/group_summary/total_enrollments/attendance">
          	  <xsl:if test="position()&lt;=count(/report/report_body/meta/attendance_status_list/status)">
				<xsl:variable name="att_id">
					<xsl:value-of select="@id"/>
				</xsl:variable>
	            <td align="right" valign="bottom"><span class="SmallText"><xsl:apply-templates select="$attendance_status_list/status[@id=$att_id]"/></span></td>
	            <td align="right" valign="bottom"><span class="SmallText">(%)</span></td>		
	            </xsl:if>
			  </xsl:for-each>
            <td valign="bottom" align="right"><span class="SmallText"><xsl:value-of select="$lab_total_attempts"/></span></td>
            <td valign="bottom" align="right"><span class="SmallText"><xsl:value-of select="$lab_total_timespent"/></span></td>
            <td valign="bottom" align="right"><span class="SmallText"><xsl:value-of select="$lab_average_score"/></span></td>
	    	  <td><img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/></td>
          </tr>
          <xsl:for-each select="report_body/report_list/report_group">
					<xsl:variable name="position" select="(position() mod 2)"/>
					<xsl:variable name="row_class">
						<xsl:choose>
							<xsl:when test="$position = 0">RowsEven</xsl:when>
							<xsl:otherwise>RowsOdd</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
          			       <tr class="{$row_class}"> 
					 		<td><img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/></td>
				        	<td><span>
						        <xsl:choose>
					        		<xsl:when test="group_summary/total_enrollments/@value > 0">
					        			<a href="Javascript:mgt_rpt.get_lrn_by_lrn_view_lrn_appr({user/@usr_ent_id})"><xsl:value-of select="user/@id"/></a>
					        		</xsl:when>
					        		<xsl:otherwise>
					        		<xsl:value-of select="user/@id"/>
					        		</xsl:otherwise>
					        	</xsl:choose>
				        	</span><xsl:text>&#160;</xsl:text></td>
				        	<td><span><xsl:value-of select="user/display_name"/></span><xsl:text>&#160;</xsl:text></td>
				        <xsl:choose>
						<xsl:when test="group_summary/total_enrollments/@value='0'">
							<td colspan="12" align="center"><xsl:value-of select="$lab_no_item"/></td>
						</xsl:when>
						<xsl:otherwise>
						        <td align="right"><span class="SmallText"><xsl:value-of select="group_summary/total_enrollments/@value"/></span><xsl:text>&#160;</xsl:text></td>
						        <xsl:for-each select="group_summary/total_enrollments/attendance">
									<xsl:if test="position()&lt;=count(/report/report_body/meta/attendance_status_list/status)">
										<xsl:variable name="att_id">
											<xsl:value-of select="@id"/>
										</xsl:variable>
						        		<td align="right"><span class="SmallText"><xsl:value-of select="@value"/></span><xsl:text>&#160;</xsl:text></td>
						       		<td align="right"><span class="SmallText"><xsl:value-of select="@percentage"/></span><xsl:text>&#160;</xsl:text></td>
									</xsl:if>
							 	 </xsl:for-each>
						        <td align="right"><span class="SmallText"><xsl:value-of select="group_summary/total_attempts/@value"/></span><xsl:text>&#160;</xsl:text></td>
						        <td align="right" nowrap="nowrap">
						        		<span class="SmallText">
									<xsl:value-of select="group_summary/total_time_spent/@day"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="$lab_day"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="group_summary/total_time_spent/@time"/>
									</span><xsl:text>&#160;</xsl:text>
							 </td>
						        <td align="right"><span class="SmallText"><xsl:value-of select="group_summary/average_score/@value"/></span><xsl:text>&#160;</xsl:text></td>
						</xsl:otherwise>
					</xsl:choose>
				        </tr>
          		</xsl:for-each>
       </table>
	</xsl:template>
</xsl:stylesheet>
