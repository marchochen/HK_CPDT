<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>	
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- other utils -->
	<xsl:import href="share/ist_lrn_rpt_gen_tab.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="share/gen_lrn_rpt_cos.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>	
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="course_id" select="/report/course/@id"/>
	<xsl:variable name="itm_id" select="/report/course/@itm_id"/>
	<xsl:variable name="page_size" select="/report /course/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/report /course/pagination/@cur_page"/>
	<xsl:variable name="total" select="/report/course/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/report/course/pagination/@timestamp"/>
	<!-- =============================================================== -->
	<xsl:variable name="order_by" select="/report/course/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/report/course/pagination/@sort_order"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc' or $cur_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="current_role" select="/report/current_role"/>
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
			<xsl:with-param name="lab_name">全名</xsl:with-param>
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_status">完成狀態</xsl:with-param>
			<xsl:with-param name="lab_total_time">訪問時長</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_score">成績</xsl:with-param>
			<xsl:with-param name="lab_detail">詳細報告</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">己查閱</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">未查閱</xsl:with-param>
			<xsl:with-param name="lab_all">全部學生</xsl:with-param>
			<xsl:with-param name="lab_no_rpt_grp">還沒有任何小組</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇類型:</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">按用戶或課程單元查看在線內容的跟蹤報告。</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users"><xsl:value-of select="$lab_const_enrolled"/>學員</xsl:with-param>
			<xsl:with-param name="lab_qr_users">試讀學員</xsl:with-param>
			<xsl:with-param name="lab_any_date">任何日子</xsl:with-param>
			<xsl:with-param name="lab_last_acc_date">以最近一次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_complete_date">以完成時間</xsl:with-param>
			<xsl:with-param name="lab_first_acc_date">以第一次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_any_acc_date">以任何一次訪問時間</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_no_record">沒有記錄</xsl:with-param>
			<xsl:with-param name="current_role" select="$current_role"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="lab_search_val">請輸入全名搜索</xsl:with-param>

		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_name">全名</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_status">完成状态</xsl:with-param>
			<xsl:with-param name="lab_total_time">访问时长</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次访问时间</xsl:with-param>
			<xsl:with-param name="lab_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_detail">详细报告</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">己查阅</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">未查阅</xsl:with-param>
			<xsl:with-param name="lab_all">全部学员</xsl:with-param>
			<xsl:with-param name="lab_no_rpt_grp">还没有任何小组</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择类型：</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">选择“用户报告”，查看单个用户学习状况；<br/>选择“使用报告”，查看课程模块总体学习状况。</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users"><xsl:value-of select="$lab_const_enrolled"/> 学员</xsl:with-param>
			<xsl:with-param name="lab_qr_users">试读学员</xsl:with-param>
			<xsl:with-param name="lab_any_date">任何日期</xsl:with-param>
			<xsl:with-param name="lab_last_acc_date">以最近一次访问时间</xsl:with-param>
			<xsl:with-param name="lab_complete_date">以完成时间</xsl:with-param>
			<xsl:with-param name="lab_first_acc_date">以第一次访问时间</xsl:with-param>
			<xsl:with-param name="lab_any_acc_date">以任何一次访问时间</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_no_record">没有记录</xsl:with-param>
			<xsl:with-param name="current_role" select="$current_role"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="lab_search_val">请输入全名搜索</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="pre_content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_name">Full name</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_group">Group</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_total_time">Time spent</xsl:with-param>
			<xsl:with-param name="lab_last_access">Last accessed</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_detail">Details</xsl:with-param>
			<xsl:with-param name="lab_status_viewed">Viewed</xsl:with-param>
			<xsl:with-param name="lab_status_not_viewed">Not viewed</xsl:with-param>
			<xsl:with-param name="lab_all">All learners</xsl:with-param>
			<xsl:with-param name="lab_no_rpt_grp">There is no group.</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select type :</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report_desc">View the online content tracking report by users or course modules.</xsl:with-param>
			<xsl:with-param name="lab_enrolled_users"><xsl:value-of select="$lab_const_enrolled"/> learners</xsl:with-param>
			<xsl:with-param name="lab_qr_users">Quick referenced learners</xsl:with-param>
			<xsl:with-param name="lab_any_date">Any date</xsl:with-param>
			<xsl:with-param name="lab_last_acc_date">with last access</xsl:with-param>
			<xsl:with-param name="lab_complete_date">with completion</xsl:with-param>
			<xsl:with-param name="lab_first_acc_date">with first access</xsl:with-param>
			<xsl:with-param name="lab_any_acc_date">with any access</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_no_record">No record found.</xsl:with-param>
			<xsl:with-param name="current_role" select="$current_role"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="lab_search_val">full name</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="pre_content">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_performance"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_total_time"/>
		<xsl:param name="lab_last_access"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_detail"/>
		<xsl:param name="lab_status_viewed"/>
		<xsl:param name="lab_status_not_viewed"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_showing"/>
		<xsl:param name="lab_comma"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_no_rpt_grp"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_tracking_report_desc"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_enrolled_users"/>
		<xsl:param name="lab_qr_users"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_any_date"/>
		<xsl:param name="lab_first_acc_date"/>
		<xsl:param name="lab_last_acc_date"/>
		<xsl:param name="lab_complete_date"/>
		<xsl:param name="lab_any_acc_date"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="current_role"/>
		<xsl:param name="parent_code"/>
		<xsl:param name="lab_search_val"/>
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
			<xsl:with-param name="lab_name" select="$lab_name"/>
			<xsl:with-param name="lab_performance" select="$lab_performance"/>
			<xsl:with-param name="lab_group" select="$lab_group"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_total_time" select="$lab_total_time"/>
			<xsl:with-param name="lab_last_access" select="$lab_last_access"/>
			<xsl:with-param name="lab_score" select="$lab_score"/>
			<xsl:with-param name="lab_detail" select="$lab_detail"/>
			<xsl:with-param name="lab_status_viewed" select="$lab_status_viewed"/>
			<xsl:with-param name="lab_status_not_viewed" select="$lab_status_not_viewed"/>
			<xsl:with-param name="lab_all" select="$lab_all"/>
			<xsl:with-param name="lab_showing" select="$lab_showing"/>
			<xsl:with-param name="lab_comma" select="$lab_comma"/>
			<xsl:with-param name="lab_prev" select="$lab_prev"/>
			<xsl:with-param name="lab_next" select="$lab_next"/>
			<xsl:with-param name="lab_no_rpt_grp" select="$lab_no_rpt_grp"/>
			<xsl:with-param name="lab_item_type" select="$lab_item_type"/>
			<xsl:with-param name="lab_user_report" select="$lab_user_report"/>
			<xsl:with-param name="lab_usage_report" select="$lab_usage_report"/>
			<xsl:with-param name="lab_tracking_report" select="$lab_tracking_report"/>
			<xsl:with-param name="lab_tracking_report_desc" select="$lab_tracking_report_desc"/>
			<xsl:with-param name="lab_g_form_btn_close" select="$lab_g_form_btn_close"/>
			<xsl:with-param name="lab_enrolled_users" select="$lab_enrolled_users"/>
			<xsl:with-param name="lab_qr_users" select="$lab_qr_users"/>
			<xsl:with-param name="lab_to" select="$lab_to"/>
			<xsl:with-param name="lab_any_date" select="$lab_any_date"/>
			<xsl:with-param name="lab_first_acc_date" select="$lab_first_acc_date"/>
			<xsl:with-param name="lab_last_acc_date" select="$lab_last_acc_date"/>
			<xsl:with-param name="lab_complete_date" select="$lab_complete_date"/>
			<xsl:with-param name="lab_any_acc_date" select="$lab_any_acc_date"/>
			<xsl:with-param name="lab_no_record" select="$lab_no_record"/>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="lab_search_val" select="$lab_search_val"/>
			<xsl:with-param name="nav_link">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/report/course/nav/item" mode="nav">
								<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
								<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
								<xsl:with-param name="current_role" select="$current_role"/>
							</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_tracking_report"/></span>
			</xsl:with-param>
			<xsl:with-param name="btn_ok_link">rpt.open_cos_lrn_lst</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
