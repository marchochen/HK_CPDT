<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- other utils -->
	<xsl:import href="share/ist_lrn_rpt_gen_tab.xsl"/>
	<xsl:import href="share/gen_export_data_prep.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="course_id" select="/applyeasy/item/@cos_res_id"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_can_qr_ind" select="/applyeasy/item/@can_qr_ind"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_online_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_search_criteria">報告標準</xsl:with-param>
			<xsl:with-param name="lab_criteria_desc">指定製作詳細報告的標準。</xsl:with-param>
			<xsl:with-param name="lab_data_type">學員類型</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users">
				<xsl:value-of select="$lab_const_enrolled"/> 學員</xsl:with-param>
			<xsl:with-param name="lab_qr_users">試讀學員</xsl:with-param>
			<xsl:with-param name="lab_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_date_range">覆蓋時間（上次訪問時間）</xsl:with-param>
			<xsl:with-param name="lab_no_restriction">所有</xsl:with-param>
			<xsl:with-param name="lab_start_date">由</xsl:with-param>
			<xsl:with-param name="lab_end_date">至</xsl:with-param>
			<xsl:with-param name="lab_export_data_between">匯出資料介乎於</xsl:with-param>
			<xsl:with-param name="lab_date_range_desc">注：不包含”還未進行“的記錄和未開始學習的學員</xsl:with-param>
			<xsl:with-param name="lab_all_mod">包含”還未進行“的記錄</xsl:with-param>
			<xsl:with-param name="lab_all_enrolled_lrn">包含未開始學習的學員</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_online_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_search_criteria">报告条件</xsl:with-param>
			<xsl:with-param name="lab_criteria_desc">为创建详细报告指定条件。</xsl:with-param>
			<xsl:with-param name="lab_data_type">学员类型</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users">
				<xsl:value-of select="$lab_const_enrolled"/>学员</xsl:with-param>
			<xsl:with-param name="lab_qr_users">试读学员</xsl:with-param>
			<xsl:with-param name="lab_export">导出</xsl:with-param>
			<xsl:with-param name="lab_date_range">时间范围（上次访问时间）</xsl:with-param>
			<xsl:with-param name="lab_no_restriction">所有</xsl:with-param>
			<xsl:with-param name="lab_start_date">由</xsl:with-param>
			<xsl:with-param name="lab_end_date">至</xsl:with-param>
			<xsl:with-param name="lab_export_data_between">导出以下时间内的数据</xsl:with-param>
			<xsl:with-param name="lab_date_range_desc">注：不包含“还未进行”的记录和未开始学习的学员</xsl:with-param>
			<xsl:with-param name="lab_all_mod">包含“还未进行”的记录</xsl:with-param>
			<xsl:with-param name="lab_all_enrolled_lrn">包含未开始学习的学员</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_online_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_search_criteria">Report criteria</xsl:with-param>
			<xsl:with-param name="lab_criteria_desc">Specify the criteria for generating details report.</xsl:with-param>
			<xsl:with-param name="lab_data_type">Learner type</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users">
				<xsl:value-of select="$lab_const_enrolled"/> learners</xsl:with-param>
			<xsl:with-param name="lab_qr_users">Quick referenced learners</xsl:with-param>
			<xsl:with-param name="lab_export">Export</xsl:with-param>
			<xsl:with-param name="lab_date_range">Coverage(last access date)</xsl:with-param>
			<xsl:with-param name="lab_no_restriction">All</xsl:with-param>
			<xsl:with-param name="lab_start_date">From</xsl:with-param>
			<xsl:with-param name="lab_end_date">To</xsl:with-param>
			<xsl:with-param name="lab_export_data_between">Export data between</xsl:with-param>
			<xsl:with-param name="lab_date_range_desc">Note: exclude "Not attempted" records and learners who have not yet attempted</xsl:with-param>
			<xsl:with-param name="lab_all_mod">Include "Not attempted" records</xsl:with-param>
			<xsl:with-param name="lab_all_enrolled_lrn">Include learners who have not yet attempted</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="pre_content">
			<xsl:param name="lab_run_info"/>
			<xsl:param name="lab_tracking_report"/>
			<xsl:param name="lab_online_performance"/>
			<xsl:param name="lab_search_criteria"/>
			<xsl:param name="lab_criteria_desc"/>
			<xsl:param name="lab_data_type"/>
			<xsl:param name="lab_enrolled_users"/>
			<xsl:param name="lab_qr_users"/>
			<xsl:param name="lab_export"/>
			<xsl:param name="lab_date_range"/>
			<xsl:param name="lab_no_restriction"/>
			<xsl:param name="lab_start_date"/>
			<xsl:param name="lab_end_date"/>
			<xsl:param name="lab_export_data_between"/>
			<xsl:param name="lab_date_range_desc"/>
			<xsl:param name="lab_all_mod"/>
			<xsl:param name="lab_all_enrolled_lrn"/>
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
			<xsl:with-param name="lab_tracking_report" select="$lab_tracking_report"/>
			<xsl:with-param name="lab_online_performance" select="$lab_online_performance"/>
			<xsl:with-param name="lab_search_criteria" select="$lab_search_criteria"/>
			<xsl:with-param name="lab_criteria_desc" select="$lab_criteria_desc"/>
			<xsl:with-param name="lab_data_type" select="$lab_data_type"/>
			<xsl:with-param name="lab_enrolled_users" select="$lab_enrolled_users"/>
			<xsl:with-param name="lab_qr_users" select="$lab_qr_users"/>
			<xsl:with-param name="lab_export" select="$lab_export"/>
			<xsl:with-param name="lab_date_range" select="$lab_date_range"/>
			<xsl:with-param name="lab_no_restriction" select="$lab_no_restriction"/>
			<xsl:with-param name="lab_start_date" select="$lab_start_date"/>
			<xsl:with-param name="lab_end_date" select="$lab_end_date"/>
			<xsl:with-param name="lab_export_data_between" select="$lab_export_data_between"/>
			<xsl:with-param name="lab_date_range_desc" select="$lab_date_range_desc"/>
			<xsl:with-param name="nav_link">
				<xsl:choose>
					<xsl:when test="/applyeasy/item/@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/applyeasy/item/title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_tracking_report"/>
				</span>
			</xsl:with-param>
			<xsl:with-param name="lab_all_mod" select="$lab_all_mod"/>
			<xsl:with-param name="lab_all_enrolled_lrn" select="$lab_all_enrolled_lrn"/>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================================================== -->
</xsl:stylesheet>
