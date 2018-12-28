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
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- other utils -->
	<xsl:import href="share/ist_lrn_rpt_gen_tab.xsl"/>
	<xsl:import href="share/ist_gen_lrn_rpt_cond.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="course_id" select="/applyeasy/item/@cos_res_id"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_can_qr_ind" select="/applyeasy/item/@can_qr_ind"/>
	<xsl:variable name="current_role" select="/applyeasy/current_role"/>
	
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
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_search_criteria">報告標準</xsl:with-param>
			<xsl:with-param name="lab_criteria_desc">指定製作學員跟蹤報告的標準。</xsl:with-param>
			<xsl:with-param name="lab_data_type">學員類型</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users">
				<xsl:value-of select="$lab_const_enrolled"/>學員</xsl:with-param>
			<xsl:with-param name="lab_qr_users">試讀學員</xsl:with-param>
			<xsl:with-param name="lab_view_report">查看報告</xsl:with-param>
			<xsl:with-param name="lab_date_range">覆蓋時間</xsl:with-param>
			<xsl:with-param name="lab_no_restriction">所有時間內的學員成績</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始日期</xsl:with-param>
			<xsl:with-param name="lab_end_date">結束日期</xsl:with-param>
			<xsl:with-param name="lab_users_results_between">於以下時間內的學員成績</xsl:with-param>
			<xsl:with-param name="lab_last_acc_date">使用最近一次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_complete_date">使用完成時間 (只適用於 <xsl:value-of select="$lab_const_enrolled"/>學員)</xsl:with-param>
			<xsl:with-param name="lab_first_acc_date">使用第一次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_any_acc_date">以上皆是</xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="current_role" select="$current_role"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_search_criteria">报告条件</xsl:with-param>
			<xsl:with-param name="lab_criteria_desc">为创建学员学习报告指定条件。</xsl:with-param>
			<xsl:with-param name="lab_data_type">学员类型</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users">
				<xsl:value-of select="$lab_const_enrolled"/>学员</xsl:with-param>
			<xsl:with-param name="lab_qr_users">试读学员</xsl:with-param>
			<xsl:with-param name="lab_view_report">查看报告</xsl:with-param>
			<xsl:with-param name="lab_date_range">时间范围</xsl:with-param>
			<xsl:with-param name="lab_no_restriction">所有时间内的学员成绩</xsl:with-param>
			<xsl:with-param name="lab_start_date">起始日期</xsl:with-param>
			<xsl:with-param name="lab_end_date">截止日期</xsl:with-param>
			<xsl:with-param name="lab_users_results_between">以下时间内的学员成绩</xsl:with-param>
			<xsl:with-param name="lab_last_acc_date">使用最近一次访问时间</xsl:with-param>
			<xsl:with-param name="lab_complete_date">使用完成时间(只适用于 <xsl:value-of select="$lab_const_enrolled"/>学员)</xsl:with-param>
			<xsl:with-param name="lab_first_acc_date">使用第一次访问时间</xsl:with-param>
			<xsl:with-param name="lab_any_acc_date">以上全部</xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="current_role" select="$current_role"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_search_criteria">Report criteria</xsl:with-param>
			<xsl:with-param name="lab_criteria_desc">Specify the criteria for generating learner report.</xsl:with-param>
			<xsl:with-param name="lab_data_type">Learner type</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users">
				<xsl:value-of select="$lab_const_enrolled"/> learners</xsl:with-param>
			<xsl:with-param name="lab_qr_users">Quick referenced learners</xsl:with-param>
			<xsl:with-param name="lab_view_report">View report</xsl:with-param>
			<xsl:with-param name="lab_date_range">Coverage</xsl:with-param>
			<xsl:with-param name="lab_no_restriction">Learners with results in all period</xsl:with-param>
			<xsl:with-param name="lab_start_date">Start date</xsl:with-param>
			<xsl:with-param name="lab_end_date">End date</xsl:with-param>
			<xsl:with-param name="lab_users_results_between">Learners with results between</xsl:with-param>
			<xsl:with-param name="lab_last_acc_date">Use last access date</xsl:with-param>
			<xsl:with-param name="lab_complete_date">Use completion date (applicable to <xsl:value-of select="$lab_const_enrolled"/> learners only)</xsl:with-param>
			<xsl:with-param name="lab_first_acc_date">Use first access date</xsl:with-param>
			<xsl:with-param name="lab_any_acc_date">Any of the above</xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="current_role" select="$current_role"/>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="pre_content">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_performance"/>
		<xsl:param name="lab_search_criteria"/>
		<xsl:param name="lab_criteria_desc"/>
		<xsl:param name="lab_data_type"/>
		<xsl:param name="lab_enrolled_users"/>
		<xsl:param name="lab_qr_users"/>
		<xsl:param name="lab_view_report"/>
		<xsl:param name="lab_date_range"/>
		<xsl:param name="lab_no_restriction"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_users_results_between"/>
		<xsl:param name="lab_last_acc_date"/>
		<xsl:param name="lab_complete_date"/>
		<xsl:param name="lab_first_acc_date"/>
		<xsl:param name="lab_any_acc_date"/>
		<xsl:param name="parent_code"/>
		<xsl:param name="current_role"/>
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
			<xsl:with-param name="lab_tracking_report" select="$lab_tracking_report"/>
			<xsl:with-param name="lab_performance" select="$lab_performance"/>
			<xsl:with-param name="lab_search_criteria" select="$lab_search_criteria"/>
			<xsl:with-param name="lab_criteria_desc" select="$lab_criteria_desc"/>
			<xsl:with-param name="lab_data_type" select="$lab_data_type"/>
			<xsl:with-param name="lab_enrolled_users" select="$lab_enrolled_users"/>
			<xsl:with-param name="lab_qr_users" select="$lab_qr_users"/>
			<xsl:with-param name="lab_view_report" select="$lab_view_report"/>
			<xsl:with-param name="lab_date_range" select="$lab_date_range"/>
			<xsl:with-param name="lab_no_restriction" select="$lab_no_restriction"/>
			<xsl:with-param name="lab_start_date" select="$lab_start_date"/>
			<xsl:with-param name="lab_end_date" select="$lab_end_date"/>
			<xsl:with-param name="lab_users_results_between" select="$lab_users_results_between"/>
			<xsl:with-param name="lab_last_acc_date" select="$lab_last_acc_date"/>
			<xsl:with-param name="lab_complete_date" select="$lab_complete_date"/>
			<xsl:with-param name="lab_first_acc_date" select="$lab_first_acc_date"/>
			<xsl:with-param name="lab_any_acc_date" select="$lab_any_acc_date"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="nav_link">
				<xsl:choose>
					
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
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
			<xsl:with-param name="btn_view_rpt_link">rpt.get_usr_tk_rpt</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
