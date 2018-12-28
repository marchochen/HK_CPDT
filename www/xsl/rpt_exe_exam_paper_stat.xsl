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
	<xsl:variable name="total_rec" select="/report/report_body/report_list/report_summary/tstCnt"/>
	<xsl:variable name="cur_sort_col" select="/report/report_body/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/report/report_body/pagination/@sort_order"/>
	<xsl:variable name="page_timestamp" select="/report/report_body/pagination/@timestamp"/>
	<xsl:variable name="itm_type_lst" select="/report/report_body/meta/item_type_list"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/report_group) > 0">
				<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'usr_content_lst']) + 5"/>
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
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_total_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '792')"/>
	<!-- =============================================================== -->
	<xsl:variable name="isStudent">
		<xsl:choose>
			<xsl:when test="report/report_body/student">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
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
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">用戶</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_all_my_staff">我的所有下屬</xsl:with-param>
			<xsl:with-param name="lab_my_direct_staff">我的直屬下屬</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_cos">課程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有課程</xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_att_create_timestamp">測驗開始日期</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">第一次訪問</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">最後一次訪問</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成績</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">嘗試</xsl:with-param>
			<xsl:with-param name="lab_is_pass">是否合格</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<!--statistic information-->
			<xsl:with-param name="lab_total_courses">課程總數</xsl:with-param>
			<xsl:with-param name="lab_total_test">試卷總數</xsl:with-param>
			<xsl:with-param name="lab_total_learners">學員總數</xsl:with-param>
			<xsl:with-param name="lab_report_summary">報告摘要</xsl:with-param>
			<xsl:with-param name="lab_course_code">課程編號</xsl:with-param>
			<xsl:with-param name="lab_course_title">課程名稱</xsl:with-param>
			<xsl:with-param name="lab_tst_title">測驗名稱</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">嘗試總數</xsl:with-param>
			<xsl:with-param name="lab_max_score">最高分</xsl:with-param>
			<xsl:with-param name="lab_lowest_score">最低分</xsl:with-param>
			<xsl:with-param name="lab_average_score">平均成績</xsl:with-param>
			<xsl:with-param name="lab_lowest_score">最低分</xsl:with-param>
			<xsl:with-param name="lab_average_score">平均成绩</xsl:with-param>
			<xsl:with-param name="lab_pass_cnt">合格人次</xsl:with-param>
			<xsl:with-param name="lab_pass_rate">合格率</xsl:with-param>
			<xsl:with-param name="lab_examinee_cnt">參加測驗人次</xsl:with-param>
			<xsl:with-param name="lab_excellent">優秀</xsl:with-param>
			<xsl:with-param name="lab_good">良好</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_passed">合格</xsl:with-param>
			<xsl:with-param name="lab_failed">不合格</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_grand_total">總計</xsl:with-param>
			<xsl:with-param name="lab_course">測驗</xsl:with-param>
			<xsl:with-param name="lab_enrollment">測驗記錄</xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用匯出查看全部記錄）</xsl:with-param>
			<xsl:with-param name="lab_show">顯示</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">詳情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">匯出</xsl:with-param>
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
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn">用户</xsl:with-param>
			<xsl:with-param name="lab_lrn_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有学员</xsl:with-param>
			<xsl:with-param name="lab_all_my_staff">我的所有下属</xsl:with-param>
			<xsl:with-param name="lab_my_direct_staff">我的直属下属</xsl:with-param>
			<xsl:with-param name="lab_cos_catalog">课程目录</xsl:with-param>
			<xsl:with-param name="lab_cos">课程</xsl:with-param>
			<xsl:with-param name="lab_all_cos">所有课程</xsl:with-param>
			<xsl:with-param name="lab_not_specified">未指定</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_att_create_timestamp">测验开始日期</xsl:with-param>
			<xsl:with-param name="lab_att_date">完成日期</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">首次访问</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">上次访问</xsl:with-param>
			<xsl:with-param name="lab_cov_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">尝试</xsl:with-param>
			<xsl:with-param name="lab_is_pass">是否合格</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<!--statistic information-->
			<xsl:with-param name="lab_total_courses">课程总数</xsl:with-param>
			<xsl:with-param name="lab_total_test">试卷总数</xsl:with-param>
			<xsl:with-param name="lab_total_learners">学员总数</xsl:with-param>
			<xsl:with-param name="lab_report_summary">报告摘要</xsl:with-param>
			<xsl:with-param name="lab_course_code">课程编号</xsl:with-param>
			<xsl:with-param name="lab_course_title">课程名称</xsl:with-param>
			<xsl:with-param name="lab_tst_title">测验名称</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">尝试总数</xsl:with-param>
			<xsl:with-param name="lab_max_score">最高分</xsl:with-param>
			<xsl:with-param name="lab_lowest_score">最低分</xsl:with-param>
			<xsl:with-param name="lab_average_score">平均成绩</xsl:with-param>
			<xsl:with-param name="lab_pass_cnt">合格人次</xsl:with-param>
			<xsl:with-param name="lab_pass_rate">合格率</xsl:with-param>
			<xsl:with-param name="lab_examinee_cnt">参加测验人次</xsl:with-param>
			<xsl:with-param name="lab_excellent">优秀</xsl:with-param>
			<xsl:with-param name="lab_good">良好</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_passed">合格</xsl:with-param>
			<xsl:with-param name="lab_failed">不合格</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_grand_total">总计</xsl:with-param>
			<xsl:with-param name="lab_course">测验</xsl:with-param>
			<xsl:with-param name="lab_enrollment">测验记录</xsl:with-param>
			<xsl:with-param name="lab_more_record">（使用导出查看全部记录）</xsl:with-param>
			<xsl:with-param name="lab_show">显示</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">详情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">导出</xsl:with-param>
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
			<xsl:with-param name="lab_all_cos">All courses</xsl:with-param>
			<xsl:with-param name="lab_att_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_not_specified">-- All --</xsl:with-param>
			<!-- content attribute text of others -->
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<xsl:with-param name="lab_att_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">Test date</xsl:with-param>
			<xsl:with-param name="lab_cov_commence_datetime">First access</xsl:with-param>
			<xsl:with-param name="lab_cov_last_acc_datetime">Last access</xsl:with-param>
			<xsl:with-param name="lab_cov_score">Score</xsl:with-param>
			<xsl:with-param name="lab_total_attempt">Attempts</xsl:with-param>
			<xsl:with-param name="lab_is_pass">Passed or not</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<!--statistic information-->
			<xsl:with-param name="lab_total_courses">Total courses</xsl:with-param>
			<xsl:with-param name="lab_total_test">Total tests</xsl:with-param>
			<xsl:with-param name="lab_total_learners">Total learners</xsl:with-param>
			<xsl:with-param name="lab_report_summary">Report summary</xsl:with-param>
			<xsl:with-param name="lab_course_code">Course code</xsl:with-param>
			<xsl:with-param name="lab_course_title">Course title</xsl:with-param>
			<xsl:with-param name="lab_tst_title">Test title</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_total_attempts">Total attempts</xsl:with-param>
			<xsl:with-param name="lab_max_score">Top score</xsl:with-param>
			<xsl:with-param name="lab_lowest_score">Lowest score</xsl:with-param>
			<xsl:with-param name="lab_average_score">Average score</xsl:with-param>
			<xsl:with-param name="lab_pass_cnt">No. of attendee passed</xsl:with-param>
			<xsl:with-param name="lab_pass_rate">Passing mark</xsl:with-param>
			<xsl:with-param name="lab_examinee_cnt">Test attendees</xsl:with-param>
			<xsl:with-param name="lab_excellent">Excellent</xsl:with-param>
			<xsl:with-param name="lab_good">Good</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_passed">Passed</xsl:with-param>
			<xsl:with-param name="lab_failed">Failed</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_grand_total">Total credit earned</xsl:with-param>
			<xsl:with-param name="lab_course">tests</xsl:with-param>
			<xsl:with-param name="lab_enrollment">attempts</xsl:with-param>
			<xsl:with-param name="lab_more_record">(Use export to show all records)</xsl:with-param>
			<xsl:with-param name="lab_show">Show </xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">Detail</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">Export</xsl:with-param>
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
		<xsl:param name="lab_lrn"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_all_my_staff"/>
		<xsl:param name="lab_my_direct_staff"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_att_date"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_report_summary"/>
		<xsl:param name="lab_total_courses"/>
		<xsl:param name="lab_total_test"/>
		<xsl:param name="lab_total_learners"/>
		<xsl:param name="lab_course_code"/>
		<xsl:param name="lab_course_title"/>
		<xsl:param name="lab_tst_title"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_lowest_score"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="lab_pass_cnt"/>
		<xsl:param name="lab_pass_rate"/>
		<xsl:param name="lab_examinee_cnt"/>
		<xsl:param name="lab_excellent"/>
		<xsl:param name="lab_good"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_passed"/>
		<xsl:param name="lab_failed"/>
		<xsl:param name="lab_not_specified"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_enrollment"/>
		<xsl:param name="lab_more_record"/>
		<xsl:param name="lab_show"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_is_pass"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_grand_total"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_g_form_btn_export"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_save"/>
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
					<tr>
						<td colspan="{$col_size + 2}">
							<xsl:call-template name="report_criteria">
								<xsl:with-param name="this_width">100%</xsl:with-param>
								<xsl:with-param name="lab_cos" select="$lab_cos"/>
								<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
								<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
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
								<xsl:with-param name="lab_course" select="$lab_course"/>
							</xsl:call-template>
						</td>
					</tr>
					<!--================= Report Summary ================ -->
					<tr>
						<td colspan="{$col_size + 2}">
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td colspan="2">
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
											<span class="StatDataText ">
												<xsl:value-of select="report_body/report_list/report_summary/itmCnt"/>
											</span>
										</td>
									</tr>
									<tr>
										<td class=" wzb-form-label">
											<span class="TitleText">
												<xsl:value-of select="$lab_total_test"/>
												<xsl:text>：</xsl:text>
											</span>
										</td>
										<td class=" wzb-form-control">	
											<span class="StatDataText">
												<xsl:value-of select="report_body/report_list/report_summary/tstCnt"/>
											</span>
										</td>
									</tr>
									<tr>
										<td class=" wzb-form-label">
											<span class="TitleText">
												<xsl:value-of select="$lab_total_learners"/>
												<xsl:text>：</xsl:text>
											</span>
										</td>
										<td class=" wzb-form-control">
											<span class="StatDataText ">
												<xsl:value-of select="report_body/report_list/report_summary/usrCnt"/>
											</span>
										</td>
									</tr>
									<tr>
										<td class="wzb-form-label">
											<span class="TitleText">
												<xsl:value-of select="$lab_examinee_cnt"/>
												<xsl:text>：</xsl:text>
											</span>
										</td>
										<td class="wzb-form-control">
											<span class="StatDataText">
												<xsl:value-of select="report_body/report_list/report_summary/examineeCnt"/>
											</span>
									</td>
								</tr>
							</table>
							<xsl:call-template name="wb_ui_space">
								<xsl:with-param name="height">15</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<xsl:choose>
						<xsl:when test="/report/report_body/spec/data_list/data[@name='show_stat_only']/@value='true'">
							<xsl:call-template name="report_group_show_main_only">
								<xsl:with-param name="lab_na" select="$lab_na"/>
								<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
								<xsl:with-param name="lab_cov_score" select="$lab_cov_score"/>
								<xsl:with-param name="lab_total_attempt" select="$lab_total_attempt"/>
								<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
								<xsl:with-param name="lab_report_summary" select="$lab_report_summary"/>
								<xsl:with-param name="lab_course_code" select="$lab_course_code"/>
								<xsl:with-param name="lab_course_title" select="$lab_course_title"/>
								<xsl:with-param name="lab_tst_title" select="$lab_tst_title"/>
								<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
								<xsl:with-param name="lab_total_attempts" select="$lab_total_attempts"/>
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="lab_lowest_score" select="$lab_lowest_score"/>
								<xsl:with-param name="lab_average_score" select="$lab_average_score"/>
								<xsl:with-param name="lab_pass_cnt" select="$lab_pass_cnt"/>
								<xsl:with-param name="lab_pass_rate" select="$lab_pass_rate"/>
								<xsl:with-param name="lab_examinee_cnt" select="$lab_examinee_cnt"/>
								<xsl:with-param name="lab_excellent" select="$lab_excellent"/>
								<xsl:with-param name="lab_good" select="$lab_good"/>
								<xsl:with-param name="lab_normal" select="$lab_normal"/>
								<xsl:with-param name="lab_passed" select="$lab_passed"/>
								<xsl:with-param name="lab_failed" select="$lab_failed"/>
								<xsl:with-param name="this" select="."/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="report_body/report_list/report_group">
								<xsl:with-param name="lab_na" select="$lab_na"/>
								<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
								<xsl:with-param name="lab_cov_score" select="$lab_cov_score"/>
								<xsl:with-param name="lab_cov_last_acc_datetime" select="$lab_cov_last_acc_datetime"/>
								<xsl:with-param name="lab_cov_commence_datetime" select="$lab_cov_commence_datetime"/>
								<xsl:with-param name="lab_total_attempt" select="$lab_total_attempt"/>
								<xsl:with-param name="lab_is_pass" select="$lab_is_pass"/>
								<xsl:with-param name="lab_yes" select="$lab_yes"/>
								<xsl:with-param name="lab_no" select="$lab_no"/>
								<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
								<xsl:with-param name="lab_report_summary" select="$lab_report_summary"/>
								<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
								<xsl:with-param name="lab_total_attempts" select="$lab_total_attempts"/>
								<xsl:with-param name="lab_max_score" select="$lab_max_score"/>
								<xsl:with-param name="lab_lowest_score" select="$lab_lowest_score"/>
								<xsl:with-param name="lab_average_score" select="$lab_average_score"/>
								<xsl:with-param name="lab_pass_cnt" select="$lab_pass_cnt"/>
								<xsl:with-param name="lab_pass_rate" select="$lab_pass_rate"/>
								<xsl:with-param name="lab_examinee_cnt" select="$lab_examinee_cnt"/>
								<xsl:with-param name="lab_enrollment" select="$lab_enrollment"/>
								<xsl:with-param name="lab_more_record" select="$lab_more_record"/>
								<xsl:with-param name="lab_show" select="$lab_show"/>
								<xsl:with-param name="this" select="."/>
							</xsl:apply-templates>						
						</xsl:otherwise>
					</xsl:choose>
				</table>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="height">15</xsl:with-param>
				</xsl:call-template>
				<table cellpadding="3" cellspacing="0" border="0" width="100%">
					<tr class="SecBg">
						<td width="60%" valign="top">
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
									   <xsl:value-of select="$lab_course"/>
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
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:if test="count(report_body/report_list/report_group)&gt;0">
							<xsl:variable name="title">
								<xsl:call-template name="escape_js">
									<xsl:with-param name="input_str" select="report_body/spec/title/text()"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_export"/>
								<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="$rpt_xls"/>','<xsl:value-of select="$title"/>')</xsl:with-param>
							</xsl:call-template>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_is_pass"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_report_summary"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_lowest_score"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="lab_pass_cnt"/>
		<xsl:param name="lab_pass_rate"/>
		<xsl:param name="lab_examinee_cnt"/>
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
			<td colspan="{$col_size + 2}">
				<span class="TitleText">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="itm_title"/> - <xsl:value-of select="res_title"/>
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
					<img src="{group_summary/stat_image_path}"/>
					<br/>
					<span class="TitleText">
						<xsl:value-of select="$lab_total_score"/>
						<xsl:text>：</xsl:text>
						<span class="StatDataText">
							<xsl:value-of select="group_summary/mod_max_score"/>
						</span>
					</span>
					<xsl:text>, </xsl:text>
					<span class="TitleText">
						<xsl:value-of select="$lab_average_score"/>
						<xsl:text>：</xsl:text>
						<span class="StatDataText">
							<xsl:value-of select="group_summary/average_score"/>
						</span>
					</span>
					<xsl:text>, </xsl:text>
					<span class="TitleText">
						<xsl:value-of select="$lab_max_score"/>
						<xsl:text>：</xsl:text>
						<span class="StatDataText">
							<xsl:value-of select="group_summary/max_score"/>
						</span>
					</span>
					<xsl:text>, </xsl:text>
					<span class="TitleText">
						<xsl:value-of select="$lab_lowest_score"/>
						<xsl:text>：</xsl:text>
						<span class="StatDataText">
							<xsl:value-of select="group_summary/least_score"/>
						</span>
					</span>
					<xsl:text>, </xsl:text>
					<span class="TitleText">
						<xsl:value-of select="$lab_pass_cnt"/>
						<xsl:text>：</xsl:text>
						<span class="StatDataText">
							<xsl:value-of select="group_summary/pass_cnt"/>
						</span>
					</span>
					<xsl:text>, </xsl:text>
					<span class="TitleText">
						<xsl:value-of select="$lab_pass_rate"/>
						<xsl:text>：</xsl:text>
						<span class="StatDataText">
							<xsl:value-of select="group_summary/pass_rate"/>
						</span>
					</span>
					<xsl:text>, </xsl:text>
					<span class="TitleText">
						<xsl:value-of select="$lab_examinee_cnt"/>
						<xsl:text>：</xsl:text>
						<span class="StatDataText">
							<xsl:value-of select="group_summary/examineeCnt"/>
						</span>
					</span>
				</xsl:if>
			</td>
		</tr>
			<xsl:choose>
				<xsl:when test="count(record) > 0">
					<tr class="SecBg">
						<table class="table wzb-ui-table">
							<tr class="SecBg wzb-ui-table-head">
								<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'usr_content_lst']" mode="title"/>
								<xsl:call-template name="usr_mod_title">
									<xsl:with-param name="lab_is_pass" select="$lab_is_pass"/>
									<xsl:with-param name="lab_cov_score" select="$lab_cov_score"/>
									<xsl:with-param name="lab_cov_last_acc_datetime" select="$lab_cov_last_acc_datetime"/>
									<xsl:with-param name="lab_cov_commence_datetime" select="$lab_cov_commence_datetime"/>
									<xsl:with-param name="lab_total_attempt" select="$lab_total_attempt"/>
								</xsl:call-template>
								<xsl:apply-templates select="record">
									<xsl:with-param name="lab_na" select="$lab_na"/>
									<xsl:with-param name="lab_g_form_btn_detail" select="$lab_g_form_btn_detail"/>
									<xsl:with-param name="lab_yes" select="$lab_yes"/>
									<xsl:with-param name="lab_no" select="$lab_no"/>
								</xsl:apply-templates>
							</tr>
							<tr>					
								<td colspan="{$col_size + 2}"  width="100%">
									<span>
										<xsl:value-of select="$lab_show"/>
										<xsl:text>&#160;1</xsl:text>
										<xsl:text>&#160;-&#160;</xsl:text>
										<xsl:value-of select="count(record)"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_page_of"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="group_summary/examineeCnt"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="$lab_page_piece"/>
										<xsl:value-of select="$lab_enrollment"/>
										<xsl:text>&#160;</xsl:text>	
									   <xsl:value-of select="$lab_more_record"/>
									</span>
								</td>
							</tr>
						</table>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr class="RowsEven">
						<td height="15" colspan="{$col_size + 2}" align="left">
							<!--<xsl:value-of select="$lab_no_item"/>-->
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
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:variable name="usr_ent_id" select="student/@ent_id"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">StatRowsEven</xsl:when>
				<xsl:otherwise>StatRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<!-- user -->
			<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'usr_content_lst']" mode="value">
				<xsl:with-param name="lab_na" select="$lab_na"/>
				<xsl:with-param name="this" select="."/>
			</xsl:apply-templates>
			<xsl:call-template name="usr_mod_value">
				<xsl:with-param name="this" select="."/>
				<xsl:with-param name="lab_na" select="$lab_na"/>
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
			</xsl:call-template>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="usr_mod_title">
		<xsl:param name="lab_is_pass"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<td nowrap="nowrap">
			<span class="SmallText"><xsl:value-of select="$lab_cov_commence_datetime"/></span>
		</td>
		<td nowrap="nowrap">
			<span class="SmallText"><xsl:value-of select="$lab_cov_last_acc_datetime"/></span>
		</td>
		<td nowrap="nowrap" align="right">
			<span class="SmallText"><xsl:value-of select="$lab_total_attempt"/></span>
		</td>
		<td nowrap="nowrap" align="center">
			<span class="SmallText"><xsl:value-of select="$lab_is_pass"/></span>
		</td>
		<td nowrap="nowrap" align="right">
			<span class="SmallText"><xsl:value-of select="$lab_cov_score"/></span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="usr_mod_value">
		<xsl:param name="this"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="$this/user/start_visit_time != ''">
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="$this/user/start_visit_time"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
		<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="$this/user/last_visit_time != ''">
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="$this/user/last_visit_time"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
		<td valign="top" nowrap="nowrap" align="right">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="$this/user/mov_total_attempt != ''">
						<xsl:call-template name="omit_title">
							<xsl:with-param name="title">
								<xsl:value-of select="$this/user/mov_total_attempt"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
		<td valign="top" nowrap="nowrap" align="center">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="$this/user/is_pass != ''">
						<xsl:call-template name="omit_title">
							<xsl:with-param name="title">
								<xsl:choose>
									<xsl:when test="$this/user/is_pass = 'true'">
										<xsl:value-of select="$lab_yes"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_no"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
		<td valign="top" nowrap="nowrap" align="right">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="$this/user/mov_score != ''">
						<xsl:call-template name="display_score">
							<xsl:with-param name="score">
								<xsl:value-of select="$this/user/mov_score"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<span class="SmallText">
							<xsl:value-of select="$lab_na"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'usr_content_lst']" mode="title">
		<td nowrap="nowrap">
			<xsl:choose>
				<xsl:when test="@value = 'usr_id' or @value = 'usr_display_bil' or @value = 'usr_email' or @value = 'usr_tel_1' or @value = 'USR_PARENT_USG' or @value = 'USR_CURRENT_UGR' or @value = 'IDC_PARENT_IDC' or @value = 'usr_email_2' or @value = 'usr_full_name_bil' or @value = 'usr_other_id_type' or @value = 'usr_tel_2' or @value = 'usr_country_bil' or @value = 'usr_state_bil' or @value = 'usr_city_bil' or @value = 'usr_occupation_bil' or @value = 'usr_income_level' or @value = 'usr_edu_role' or @value = 'usr_edu_level' or @value = 'usr_school_bil' or @value = 'usr_class' or @value = 'usr_cost_center' or @value = 'usr_extra_1' or @value = 'usr_extra_2' or @value = 'usr_extra_3' or @value = 'usr_extra_4' or @value = 'usr_extra_5' or @value = 'usr_extra_6' or @value = 'usr_extra_7' or @value = 'usr_extra_8' or @value = 'usr_extra_9' or @value = 'usr_extra_10' or contains(@value,'USR_CURRENT_CLASS')">
					<xsl:attribute name="align">LEFT</xsl:attribute>
				</xsl:when>
				<xsl:when test="@value = 'usr_gender' or @value = 'usr_bday' or @value = 'usr_ent_id' or @value = 'usr_other_id_no' or @value = 'usr_postal_code_bil' or @value = 'usr_class_number' or @value = 'usr_signup_date' or @value = 'usr_special_date_1' or @value = 'usr_status' or @value = 'usr_upd_date' ">
					<xsl:attribute name="align">CENTER</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="align">LEFT</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="@value = 'usr_id'">
						<xsl:value-of select="$lab_login_id"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_display_bil'">
						<xsl:value-of select="$lab_dis_name"/>
					</xsl:when>
					<xsl:when test="@value = 'USR_CURRENT_UGR'">
						<xsl:value-of select="$lab_grade"/>
					</xsl:when>
					<xsl:when test="@value = 'USR_PARENT_USG'">
						<xsl:value-of select="$lab_group"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_email'">
						<xsl:value-of select="$lab_e_mail"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_tel_1'">
						<xsl:value-of select="$lab_tel_1"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_competency'">
						<xsl:value-of select="$lab_competency"/>
					</xsl:when>
					<xsl:when test="@value = 'usr_extra_2'">
						<xsl:value-of select="$lab_staff_no"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@value"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'usr_content_lst']" mode="value">
		<xsl:param name="this"/>
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<td valign="top" nowrap="nowrap">
			<span class="SmallText">
				<xsl:choose>
					<xsl:when test="@value = 'usr_id'">
						<xsl:choose>
							<xsl:when test="$this/user/usr_ste_usr_id != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/usr_ste_usr_id"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_display_bil'">
						<xsl:choose>
							<xsl:when test="$this/user/usr_display_bil != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/usr_display_bil"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_email'">
						<xsl:choose>
							<xsl:when test="$this/user/usr_email != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/usr_email"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_tel_1'">
						<xsl:choose>
							<xsl:when test="$this/user/usr_tel_1 != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/usr_tel_1"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'USR_PARENT_USG'">
						<xsl:choose>
							<xsl:when test="$this/user/usg_display_bil != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/usg_display_bil"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'USR_CURRENT_UGR'">
						<xsl:choose>
							<xsl:when test="$this/user/ugr_display_bil != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/ugr_display_bil"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_competency'">
						<xsl:choose>
							<xsl:when test="$this/user/sks_title != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/sks_title"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:when test="@value = 'usr_extra_2'">
						<xsl:choose>
							<xsl:when test="$this/user/usr_extra_2 != ''">
								<xsl:call-template name="omit_title">
									<xsl:with-param name="title">
										<xsl:value-of select="$this/user/usr_extra_2"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@value"/>
					</xsl:otherwise>
				</xsl:choose>
&#160;</span>
		</td>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="report_criteria">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_all_cos"/>
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
		<xsl:param name="lab_course"/>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$this_width"/>
		</xsl:call-template>
		<table cellpadding="3" cellspacing="0" border="0" width="{$this_width}" class="Bg">
			<tr>
				<td width="150" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<!-- display either specific courses or specific course catalogs or all courses -->
			<xsl:choose>
				<!-- course id -->
				<xsl:when test="count(report_body/spec/data_list/data[@name='itm_id']) !=0">
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos"/>：</span>
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
								<xsl:value-of select="$lab_cos_catalog"/>：</span>
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
				<!-- test mod id -->
				<xsl:when test="report_body/spec/data_list/data[@name='mod_id']">
					<tr>
						<td align="right" valign="top" class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_course"/>：</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:for-each select="report_body/spec/data_list/data[@name='mod_id']">
									<xsl:variable name="mod_id">
										<xsl:value-of select="@value"/>
									</xsl:variable>
									<xsl:value-of select="/report/report_body/presentation/data[@name='mod_id' and @value=$mod_id]/@display"/>
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
								<xsl:value-of select="$lab_cos"/>：</span>
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
			<!-- learner / group -->
			<xsl:if test="report_body/spec/data_list/data[@name='ent_id'] or report_body/spec/data_list/data[@name='s_usg_ent_id_lst'] or  report_body/spec/data_list/data[@name='all_user_ind'] or report_body/spec/data_list/data[@name='usr_ent_id'] or report_body/spec/data_list/data[@name='usg_ent_id'] ">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='all_user_ind']/@value = '1' or count(report_body/spec/data_list/data[@name='usr_ent_id'])  != 0 ">
									<xsl:value-of select="$lab_lrn"/>：
									</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_lrn_group"/><xsl:text>：</xsl:text></xsl:otherwise>
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
									</xsl:choose>								</xsl:when>
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
			<!-- date -->
			<xsl:if test="count(report_body/spec/data_list/data[@name='att_start_datetime']) != 0 or count(report_body/spec/data_list/data[@name='att_end_datetime']) != 0">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_att_create_timestamp"/>：</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:value-of select="$lab_from"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='att_start_datetime']">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_start_datetime']/@value"/>
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
								<xsl:when test="report_body/spec/data_list/data[@name='att_end_datetime']">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_end_datetime']/@value"/>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_na"/>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- completion status -->
			<xsl:if test="report_body/spec/data_list/data[@name='ats_id']">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_att_status"/>：</span>
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
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_report_summary"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_res_title"/>
		<xsl:param name="lab_course_title"/>
		<xsl:param name="lab_tst_title"/>
		<xsl:param name="lab_total_attempts"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_lowest_score"/>
		<xsl:param name="lab_average_score"/>
		<xsl:param name="lab_pass_cnt"/>
		<xsl:param name="lab_pass_rate"/>
		<xsl:param name="lab_examinee_cnt"/>
		<xsl:param name="lab_excellent"/>
		<xsl:param name="lab_good"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_passed"/>
		<xsl:param name="lab_failed"/>
		<xsl:param name="this"/>
		<table width="{$wb_gen_table_width}" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td><img border="0" height="5" src="{$wb_img_path}tp.gif" width="1"/></td>
          </tr>
       </table>
       <table border="0" class="Bg" cellspacing="0" cellpadding="3">
			<tr class="SecBg"> 
				<td><img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/></td>
				<td nowrap="nowrap" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_course_title"/></span></td>
				<td nowrap="nowrap" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_tst_title"/></span></td>
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_examinee_cnt"/></span></td>
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_total_score"/></span></td>
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_average_score"/></span></td>
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_max_score"/></span></td>
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_lowest_score"/></span></td>
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_pass_cnt"/></span></td>
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_pass_rate"/></span></td>
				
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_excellent"/>90-100</span></td>	
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText">(%)</span></td>
				
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_good"/>80-89</span></td>	
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText">(%)</span></td>
				
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_normal"/>70-79</span></td>	
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText">(%)</span></td>
				
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_passed"/>60-69</span></td>	
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText">(%)</span></td>
				
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText"><xsl:value-of select="$lab_failed"/>0-59</span></td>	
				<td nowrap="nowrap" align="center" valign="bottom"><span class="SmallText">(%)</span></td>
				
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
					<td nowrap="nowrap"><span><xsl:value-of select="itm_title"/></span><xsl:text>&#160;</xsl:text></td>
					<td nowrap="nowrap"><span><xsl:value-of select="res_title"/></span><xsl:text>&#160;</xsl:text></td>
					<xsl:choose>
						<xsl:when test="count(group_summary) = 0">
							<td colspan="17" align="center"><xsl:value-of select="$lab_no_item"/></td>
						</xsl:when>
						<xsl:otherwise>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/examineeCnt"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/mod_max_score"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/average_score"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/max_score"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/least_score"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/pass_cnt"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/pass_rate"/></span><xsl:text>&#160;</xsl:text></td>
							
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_90_100"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_90_100/@percentage"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_80_90"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_80_90/@percentage"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_70_80"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_70_80/@percentage"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_60_70"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_60_70/@percentage"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_0_60"/></span><xsl:text>&#160;</xsl:text></td>
							<td nowrap="nowrap" align="right"><span class="SmallText"><xsl:value-of select="group_summary/score_0_60/@percentage"/></span><xsl:text>&#160;</xsl:text></td>

						</xsl:otherwise>
					</xsl:choose>
					<td><img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/></td>
				</tr>
          	</xsl:for-each>
       </table>
	</xsl:template>
</xsl:stylesheet>
